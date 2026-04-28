USE `springboot32e20828n`;
SET NAMES utf8mb4;

START TRANSACTION;

DROP TEMPORARY TABLE IF EXISTS tmp_hr_workdays;
CREATE TEMPORARY TABLE tmp_hr_workdays (
  d date PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_hr_workdays (d) VALUES
('2026-04-01'),('2026-04-02'),('2026-04-03'),('2026-04-04'),
('2026-04-07'),('2026-04-08'),('2026-04-09'),('2026-04-10'),('2026-04-11'),
('2026-04-14'),('2026-04-15'),('2026-04-16'),('2026-04-17'),('2026-04-18'),
('2026-04-21'),('2026-04-22'),('2026-04-23'),('2026-04-24'),('2026-04-25'),
('2026-04-28'),('2026-04-29'),('2026-04-30');

DROP TEMPORARY TABLE IF EXISTS tmp_hr_missing_dates;
CREATE TEMPORARY TABLE tmp_hr_missing_dates (
  d date PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_hr_missing_dates (d) VALUES
('2026-04-23'),('2026-04-24'),('2026-04-25'),('2026-04-28'),('2026-04-29'),('2026-04-30');

DELETE FROM yuangongqiandao
WHERE addtime = '2026-04-30 10:00:00'
  AND qiandaoshijian >= '2026-04-23'
  AND qiandaoshijian < '2026-05-01'
  AND qiandaodidian NOT IN ('签到', '签退');

DROP TEMPORARY TABLE IF EXISTS tmp_hr_nums;
CREATE TEMPORARY TABLE tmp_hr_nums (
  n int PRIMARY KEY
) ENGINE=Memory;

INSERT INTO tmp_hr_nums (n) VALUES
(0),(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),
(16),(17),(18),(19),(20),(21),(22),(23),(24),(25),(26),(27),(28),(29),(30);

DROP TEMPORARY TABLE IF EXISTS tmp_hr_leave_days;
CREATE TEMPORARY TABLE tmp_hr_leave_days (
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  d date NOT NULL,
  PRIMARY KEY (gonghao, d)
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO tmp_hr_leave_days (gonghao, d)
SELECT q.gonghao,
       DATE_ADD(DATE(q.qingjiashijian), INTERVAL n.n DAY) AS d
FROM qingjiashenqing q
JOIN tmp_hr_nums n
  ON DATE_ADD(DATE(q.qingjiashijian), INTERVAL n.n DAY) <= DATE(IFNULL(q.jieshushijian, q.qingjiashijian))
WHERE q.sfsh = '是'
  AND DATE_ADD(DATE(q.qingjiashijian), INTERVAL n.n DAY) BETWEEN '2026-04-01' AND '2026-04-30';

DROP TEMPORARY TABLE IF EXISTS tmp_hr_sign_pattern;
CREATE TEMPORARY TABLE tmp_hr_sign_pattern AS
SELECT y.gonghao,
       y.xingming,
       y.touxiang,
       MIN(CASE WHEN q.qiandaodidian = '签到' THEN TIME(q.qiandaoshijian) END) AS sign_in_time,
       MIN(CASE WHEN q.qiandaodidian = '签退' THEN TIME(q.qiandaoshijian) END) AS sign_out_time
FROM yuangong y
LEFT JOIN yuangongqiandao q
  ON q.gonghao = y.gonghao
 AND DATE(q.qiandaoshijian) = '2026-04-22'
GROUP BY y.gonghao, y.xingming, y.touxiang;

INSERT INTO yuangongqiandao (addtime, gonghao, xingming, touxiang, qiandaoshijian, qiandaodidian)
SELECT '2026-04-30 10:00:00',
       p.gonghao,
       p.xingming,
       p.touxiang,
       TIMESTAMP(d.d, ADDTIME('07:42:00', SEC_TO_TIME(MOD(CRC32(CONCAT(p.gonghao, d.d, 'normal-in')), 16) * 60))),
       '签到'
FROM tmp_hr_sign_pattern p
CROSS JOIN tmp_hr_missing_dates d
WHERE NOT EXISTS (
  SELECT 1 FROM yuangongqiandao q
  WHERE q.gonghao = p.gonghao
    AND DATE(q.qiandaoshijian) = d.d
    AND q.qiandaodidian = '签到'
);

INSERT INTO yuangongqiandao (addtime, gonghao, xingming, touxiang, qiandaoshijian, qiandaodidian)
SELECT '2026-04-30 10:00:00',
       p.gonghao,
       p.xingming,
       p.touxiang,
       TIMESTAMP(d.d, ADDTIME('18:05:00', SEC_TO_TIME(MOD(CRC32(CONCAT(p.gonghao, d.d, 'normal-out')), 26) * 60))),
       '签退'
FROM tmp_hr_sign_pattern p
CROSS JOIN tmp_hr_missing_dates d
WHERE NOT EXISTS (
  SELECT 1 FROM yuangongqiandao q
  WHERE q.gonghao = p.gonghao
    AND DATE(q.qiandaoshijian) = d.d
    AND q.qiandaodidian = '签退'
);

DROP TEMPORARY TABLE IF EXISTS tmp_hr_leave_count;
CREATE TEMPORARY TABLE tmp_hr_leave_count AS
SELECT gonghao, COUNT(*) AS leave_days
FROM tmp_hr_leave_days
GROUP BY gonghao;
ALTER TABLE tmp_hr_leave_count ADD PRIMARY KEY (gonghao);

DROP TEMPORARY TABLE IF EXISTS tmp_hr_sign_days;
CREATE TEMPORARY TABLE tmp_hr_sign_days AS
SELECT DISTINCT gonghao, DATE(qiandaoshijian) AS d
FROM yuangongqiandao
WHERE qiandaodidian = '签到'
  AND qiandaoshijian >= '2026-04-01'
  AND qiandaoshijian < '2026-05-01';
ALTER TABLE tmp_hr_sign_days ADD PRIMARY KEY (gonghao, d);

DROP TEMPORARY TABLE IF EXISTS tmp_hr_absence_count;
CREATE TEMPORARY TABLE tmp_hr_absence_count AS
SELECT y.gonghao, COUNT(*) AS weiqiandaotianshu
FROM yuangong y
JOIN tmp_hr_workdays wd
LEFT JOIN tmp_hr_leave_days ld
  ON ld.gonghao = y.gonghao
 AND ld.d = wd.d
LEFT JOIN tmp_hr_sign_days s
  ON s.gonghao = y.gonghao
 AND s.d = wd.d
WHERE ld.d IS NULL
  AND s.d IS NULL
GROUP BY y.gonghao;
ALTER TABLE tmp_hr_absence_count ADD PRIMARY KEY (gonghao);

DROP TEMPORARY TABLE IF EXISTS tmp_hr_overtime;
CREATE TEMPORARY TABLE tmp_hr_overtime AS
SELECT s.gonghao,
       ROUND(SUM(TIMESTAMPDIFF(MINUTE, s.qiandaoshijian, e.qiandaoshijian)) / 60, 2) AS overtime_hours
FROM yuangongqiandao s
JOIN yuangongqiandao e
  ON e.gonghao = s.gonghao
 AND DATE(e.qiandaoshijian) = DATE(s.qiandaoshijian)
 AND e.qiandaodidian = '加班结束'
 AND e.qiandaoshijian >= s.qiandaoshijian
WHERE s.qiandaodidian = '加班开始'
  AND s.qiandaoshijian >= '2026-04-01'
  AND s.qiandaoshijian < '2026-05-01'
GROUP BY s.gonghao;
ALTER TABLE tmp_hr_overtime ADD PRIMARY KEY (gonghao);

DROP TEMPORARY TABLE IF EXISTS tmp_hr_salary_calc;
CREATE TEMPORARY TABLE tmp_hr_salary_calc AS
SELECT m.gonghao,
       m.xingming,
       (IFNULL(l.leave_days, 0) + IFNULL(y.qingjiatianshu, 0)) AS qingjiatianshu,
       m.jibengongzi,
       ROUND(IFNULL(o.overtime_hours, 0) * IFNULL(m.jibengongzi, 0) / 116, 2) AS jiabangongzi,
       0 AS jixiaojiangjin,
       IFNULL(m.gangweibutie, 0) AS gangweibutie,
       IFNULL(m.koukuanjine, 0) AS koukuanjine,
       0 AS qingjiakouxin,
       IFNULL(a.weiqiandaotianshu, 0) AS weiqiandaotianshu,
       ROUND(IFNULL(a.weiqiandaotianshu, 0) * (IFNULL(m.jibengongzi, 0) / 21.75) * -1.5, 2) AS weiqiandaokouxin,
       ROUND((IFNULL(l.leave_days, 0) + IFNULL(y.qingjiatianshu, 0)) * (IFNULL(m.jibengongzi, 0) / 21.75) * -1, 2) AS jiaqikouxin,
       m.koukuanyuanyin,
       m.guanlizhanghao,
       m.guanlixingming
FROM yuangongxinzi m
JOIN yuangong y ON y.gonghao = m.gonghao
LEFT JOIN tmp_hr_leave_count l ON l.gonghao = m.gonghao
LEFT JOIN tmp_hr_absence_count a ON a.gonghao = m.gonghao
LEFT JOIN tmp_hr_overtime o ON o.gonghao = m.gonghao
WHERE m.dengjiriqi = '2026-03-31';

ALTER TABLE tmp_hr_salary_calc
  ADD PRIMARY KEY (gonghao);

UPDATE yuangongxinzi x
JOIN tmp_hr_salary_calc c ON c.gonghao = x.gonghao
SET x.xingming = c.xingming,
    x.qingjiatianshu = c.qingjiatianshu,
    x.jibengongzi = c.jibengongzi,
    x.jiabangongzi = c.jiabangongzi,
    x.jixiaojiangjin = c.jixiaojiangjin,
    x.gangweibutie = c.gangweibutie,
    x.koukuanjine = c.koukuanjine,
    x.qingjiakouxin = c.qingjiakouxin,
    x.weiqiandaotianshu = c.weiqiandaotianshu,
    x.weiqiandaokouxin = c.weiqiandaokouxin,
    x.jiaqikouxin = c.jiaqikouxin,
    x.shifagongzi = ROUND(c.jibengongzi + c.jiabangongzi + c.gangweibutie + c.weiqiandaokouxin + c.jiaqikouxin - c.koukuanjine, 2),
    x.koukuanyuanyin = c.koukuanyuanyin,
    x.dengjiriqi = '2026-04-30',
    x.guanlizhanghao = c.guanlizhanghao,
    x.guanlixingming = c.guanlixingming,
    x.sfsh = '是',
    x.shhf = '4月薪资已按4月30日考勤重算',
    x.ispay = '已支付'
WHERE x.dengjiriqi >= '2026-04-01'
  AND x.dengjiriqi < '2026-05-01';

INSERT INTO yuangongxinzi (
  addtime, xingming, gonghao, qingjiatianshu, jibengongzi, jiabangongzi,
  jixiaojiangjin, gangweibutie, koukuanjine, qingjiakouxin,
  weiqiandaotianshu, weiqiandaokouxin, jiaqikouxin, shifagongzi,
  koukuanyuanyin, dengjiriqi, guanlizhanghao, guanlixingming, sfsh, shhf, ispay
)
SELECT '2026-04-30 10:00:00',
       c.xingming,
       c.gonghao,
       c.qingjiatianshu,
       c.jibengongzi,
       c.jiabangongzi,
       c.jixiaojiangjin,
       c.gangweibutie,
       c.koukuanjine,
       c.qingjiakouxin,
       c.weiqiandaotianshu,
       c.weiqiandaokouxin,
       c.jiaqikouxin,
       ROUND(c.jibengongzi + c.jiabangongzi + c.gangweibutie + c.weiqiandaokouxin + c.jiaqikouxin - c.koukuanjine, 2),
       c.koukuanyuanyin,
       '2026-04-30',
       c.guanlizhanghao,
       c.guanlixingming,
       '是',
       '4月薪资已按4月30日考勤重算',
       '已支付'
FROM tmp_hr_salary_calc c
WHERE NOT EXISTS (
  SELECT 1 FROM yuangongxinzi x
  WHERE x.gonghao = c.gonghao
    AND x.dengjiriqi >= '2026-04-01'
    AND x.dengjiriqi < '2026-05-01'
);

UPDATE yuangongkaoqin k
LEFT JOIN tmp_hr_salary_calc c ON c.gonghao = k.gonghao
SET k.dengjiriqi = '2026-04-30',
    k.zhengchangcishu = (SELECT COUNT(*) FROM tmp_hr_workdays),
    k.qingjiacishu = IFNULL(c.qingjiatianshu, k.qingjiacishu),
    k.kuanggongcishu = IFNULL(c.weiqiandaotianshu, k.kuanggongcishu),
    k.beizhu = '4月考勤汇总（截至4月30日）'
WHERE k.dengjiriqi >= '2026-04-01'
  AND k.dengjiriqi < '2026-05-01';

UPDATE jixiaokaohe
SET dengjiriqi = '2026-04-30'
WHERE dengjiriqi >= '2026-04-01'
  AND dengjiriqi < '2026-05-01';

COMMIT;

SELECT 'yuangongqiandao' AS table_name, MIN(qiandaoshijian) AS min_time, MAX(qiandaoshijian) AS max_time, COUNT(*) AS row_count
FROM yuangongqiandao
UNION ALL
SELECT 'yuangongxinzi', MIN(dengjiriqi), MAX(dengjiriqi), COUNT(*)
FROM yuangongxinzi
UNION ALL
SELECT 'yuangongkaoqin', MIN(dengjiriqi), MAX(dengjiriqi), COUNT(*)
FROM yuangongkaoqin
UNION ALL
SELECT 'jixiaokaohe', MIN(dengjiriqi), MAX(dengjiriqi), COUNT(*)
FROM jixiaokaohe;
