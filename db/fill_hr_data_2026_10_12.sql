USE `springboot32e20828n`;
SET NAMES utf8mb4;
SET collation_connection = 'utf8mb4_unicode_ci';

START TRANSACTION;

DELETE FROM yuangongqiandao
WHERE qiandaoshijian >= '2026-10-01'
  AND qiandaoshijian < '2027-01-01'
  AND gonghao IN (SELECT gonghao FROM yuangong);

DELETE FROM qingjiashenqing
WHERE qingjiashijian >= '2026-10-01'
  AND qingjiashijian < '2027-01-01'
  AND gonghao IN (SELECT gonghao FROM yuangong);

DELETE FROM yuangongkaoqin
WHERE dengjiriqi >= '2026-10-01'
  AND dengjiriqi < '2027-01-01';

DELETE FROM yuangongxinzi
WHERE dengjiriqi >= '2026-10-01'
  AND dengjiriqi < '2027-01-01';

DELETE FROM jixiaokaohe
WHERE dengjiriqi >= '2026-10-01'
  AND dengjiriqi < '2027-01-01';

DROP TEMPORARY TABLE IF EXISTS tmp_1012_nums;
CREATE TEMPORARY TABLE tmp_1012_nums (n int PRIMARY KEY) ENGINE=Memory;
INSERT INTO tmp_1012_nums (n) VALUES
(0),(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),
(16),(17),(18),(19),(20),(21),(22),(23),(24),(25),(26),(27),(28),(29),
(30),(31);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_months;
CREATE TEMPORARY TABLE tmp_1012_months (
  month_no int PRIMARY KEY,
  ym char(7) NOT NULL,
  month_label varchar(10) NOT NULL,
  start_date date NOT NULL,
  end_date date NOT NULL,
  leave_day date NOT NULL,
  overtime_day date NOT NULL,
  leave_type varchar(20) NOT NULL,
  leave_reason varchar(200) NOT NULL
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tmp_1012_months
(month_no, ym, month_label, start_date, end_date, leave_day, overtime_day, leave_type, leave_reason)
VALUES
(10, '2026-10', '10月', '2026-10-01', '2026-10-31', '2026-10-15', '2026-10-16', '年假', '季度工作结束安排年假1天'),
(11, '2026-11', '11月', '2026-11-01', '2026-11-30', '2026-11-12', '2026-11-18', '事假', '办理个人事务请假1天'),
(12, '2026-12', '12月', '2026-12-01', '2026-12-31', '2026-12-10', '2026-12-17', '调休', '年末项目收尾调休申请1天');

DROP TEMPORARY TABLE IF EXISTS tmp_1012_employees;
CREATE TEMPORARY TABLE tmp_1012_employees AS
SELECT y.id AS emp_id,
       y.gonghao,
       y.xingming,
       y.touxiang,
       COALESCE(x.guanlizhanghao, ELT(MOD(y.id, 8) + 1, '101','102','103','104','105','106','107','108')) AS guanlizhanghao,
       COALESCE(x.guanlixingming, ELT(MOD(y.id, 8) + 1, '赵芳','张雨','赵敏','孙俪','刘洋','王磊','李军','李静')) AS guanlixingming,
       IFNULL(x.jibengongzi, 5200) AS prev_jibengongzi,
       IFNULL(x.gangweibutie, 280) AS gangweibutie,
       IFNULL(x.koukuanjine, 0) AS koukuanjine,
       x.koukuanyuanyin
FROM yuangong y
LEFT JOIN yuangongxinzi x
  ON x.gonghao = y.gonghao
 AND x.dengjiriqi = (
       SELECT MAX(x2.dengjiriqi)
       FROM yuangongxinzi x2
       WHERE x2.gonghao = y.gonghao
         AND x2.dengjiriqi < '2026-10-01'
     );
ALTER TABLE tmp_1012_employees ADD PRIMARY KEY (gonghao);

INSERT INTO qingjiashenqing (
  addtime, gonghao, xingming, qingjialeixing, qingjiashijian, jieshushijian,
  qingjiatianshu, qingjiayuanyin, guanlixingming, guanlizhanghao, sfsh, shhf
)
SELECT TIMESTAMP(m.leave_day, '09:00:00'),
       e.gonghao,
       e.xingming,
       m.leave_type,
       TIMESTAMP(m.leave_day, '09:00:00'),
       TIMESTAMP(m.leave_day, '18:00:00'),
       1,
       m.leave_reason,
       e.guanlixingming,
       e.guanlizhanghao,
       '是',
       '同意'
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m;

DROP TEMPORARY TABLE IF EXISTS tmp_1012_workdays;
CREATE TEMPORARY TABLE tmp_1012_workdays AS
SELECT m.ym,
       m.month_no,
       DATE_ADD(m.start_date, INTERVAL n.n DAY) AS d
FROM tmp_1012_months m
JOIN tmp_1012_nums n
  ON DATE_ADD(m.start_date, INTERVAL n.n DAY) <= m.end_date
WHERE DAYOFWEEK(DATE_ADD(m.start_date, INTERVAL n.n DAY)) IN (3,4,5,6,7);
ALTER TABLE tmp_1012_workdays ADD PRIMARY KEY (ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_absence_count;
CREATE TEMPORARY TABLE tmp_1012_absence_count AS
SELECT e.emp_id,
       e.gonghao,
       m.ym,
       CASE
         WHEN MOD(CRC32(CONCAT(e.gonghao, m.ym, 'absence-count')), 10) = 0 THEN 0
         WHEN MOD(CRC32(CONCAT(e.gonghao, m.ym, 'absence-count')), 10) < 4 THEN 1
         WHEN MOD(CRC32(CONCAT(e.gonghao, m.ym, 'absence-count')), 10) < 8 THEN 2
         ELSE 3
       END AS absence_days
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m;
ALTER TABLE tmp_1012_absence_count ADD PRIMARY KEY (gonghao, ym);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_abs_candidates;
CREATE TEMPORARY TABLE tmp_1012_abs_candidates AS
SELECT e.emp_id,
       e.gonghao,
       w.ym,
       w.d
FROM tmp_1012_employees e
JOIN tmp_1012_workdays w
JOIN tmp_1012_months m ON m.ym = w.ym
WHERE w.d <> m.leave_day
  AND w.d <> m.overtime_day;
ALTER TABLE tmp_1012_abs_candidates ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_abs_candidates_src;
CREATE TEMPORARY TABLE tmp_1012_abs_candidates_src AS SELECT * FROM tmp_1012_abs_candidates;
ALTER TABLE tmp_1012_abs_candidates_src ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_ranked_abs_candidates;
CREATE TEMPORARY TABLE tmp_1012_ranked_abs_candidates AS
SELECT c1.emp_id,
       c1.gonghao,
       c1.ym,
       c1.d,
       COUNT(c2.d) AS rn
FROM tmp_1012_abs_candidates c1
JOIN tmp_1012_abs_candidates_src c2
  ON c2.gonghao = c1.gonghao
 AND c2.ym = c1.ym
 AND c2.d <= c1.d
GROUP BY c1.emp_id, c1.gonghao, c1.ym, c1.d;
ALTER TABLE tmp_1012_ranked_abs_candidates ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_absence_targets;
CREATE TEMPORARY TABLE tmp_1012_absence_targets AS
SELECT r.gonghao,
       r.ym,
       r.d
FROM tmp_1012_ranked_abs_candidates r
JOIN tmp_1012_absence_count c
  ON c.gonghao = r.gonghao
 AND c.ym = r.ym
WHERE (c.absence_days >= 1 AND r.rn = 3 + MOD(r.emp_id, 4))
   OR (c.absence_days >= 2 AND r.rn = 10 + MOD(r.emp_id, 5))
   OR (c.absence_days >= 3 AND r.rn = 17 + MOD(r.emp_id, 4));
ALTER TABLE tmp_1012_absence_targets ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_present_days;
CREATE TEMPORARY TABLE tmp_1012_present_days AS
SELECT e.emp_id,
       e.gonghao,
       e.xingming,
       e.touxiang,
       w.ym,
       w.d
FROM tmp_1012_employees e
JOIN tmp_1012_workdays w
JOIN tmp_1012_months m ON m.ym = w.ym
LEFT JOIN tmp_1012_absence_targets a
  ON a.gonghao = e.gonghao
 AND a.ym = w.ym
 AND a.d = w.d
WHERE w.d <> m.leave_day
  AND a.d IS NULL;
ALTER TABLE tmp_1012_present_days ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_present_days_src;
CREATE TEMPORARY TABLE tmp_1012_present_days_src AS SELECT * FROM tmp_1012_present_days;
ALTER TABLE tmp_1012_present_days_src ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_ranked_present_days;
CREATE TEMPORARY TABLE tmp_1012_ranked_present_days AS
SELECT p1.emp_id,
       p1.gonghao,
       p1.xingming,
       p1.touxiang,
       p1.ym,
       p1.d,
       COUNT(p2.d) AS rn
FROM tmp_1012_present_days p1
JOIN tmp_1012_present_days_src p2
  ON p2.gonghao = p1.gonghao
 AND p2.ym = p1.ym
 AND p2.d <= p1.d
GROUP BY p1.emp_id, p1.gonghao, p1.xingming, p1.touxiang, p1.ym, p1.d;
ALTER TABLE tmp_1012_ranked_present_days ADD PRIMARY KEY (gonghao, ym, d);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_late_early_targets;
CREATE TEMPORARY TABLE tmp_1012_late_early_targets AS
SELECT emp_id,
       gonghao,
       xingming,
       touxiang,
       ym,
       d,
       CASE
         WHEN rn = 2 THEN 1
         WHEN rn = 8 AND MOD(emp_id, 10) IN (1,3,4,5,6,7,8) THEN 1
         WHEN rn = 14 AND MOD(emp_id, 10) IN (5,7,8) THEN 1
         ELSE 0
       END AS is_late,
       CASE
         WHEN rn = 4 THEN 1
         WHEN rn = 10 AND MOD(emp_id, 10) IN (2,3,4,5,6,7,8) THEN 1
         WHEN rn = 16 AND MOD(emp_id, 10) IN (6,7,8) THEN 1
         ELSE 0
       END AS is_early
FROM tmp_1012_ranked_present_days;
ALTER TABLE tmp_1012_late_early_targets ADD PRIMARY KEY (gonghao, ym, d);

INSERT INTO yuangongqiandao (addtime, gonghao, xingming, touxiang, qiandaoshijian, qiandaodidian)
SELECT TIMESTAMP(t.d, '08:00:00'),
       t.gonghao,
       t.xingming,
       t.touxiang,
       TIMESTAMP(
         t.d,
         CASE
           WHEN t.is_late = 1 THEN ADDTIME('08:06:00', SEC_TO_TIME(MOD(CRC32(CONCAT(t.gonghao, t.d, 'late')), 16) * 60))
           ELSE ADDTIME('07:42:00', SEC_TO_TIME(MOD(CRC32(CONCAT(t.gonghao, t.d, 'normal-in')), 16) * 60))
         END
       ),
       '签到'
FROM tmp_1012_late_early_targets t;

INSERT INTO yuangongqiandao (addtime, gonghao, xingming, touxiang, qiandaoshijian, qiandaodidian)
SELECT TIMESTAMP(t.d, '18:00:00'),
       t.gonghao,
       t.xingming,
       t.touxiang,
       TIMESTAMP(
         t.d,
         CASE
           WHEN t.is_early = 1 THEN ADDTIME('17:32:00', SEC_TO_TIME(MOD(CRC32(CONCAT(t.gonghao, t.d, 'early')), 20) * 60))
           ELSE ADDTIME('18:05:00', SEC_TO_TIME(MOD(CRC32(CONCAT(t.gonghao, t.d, 'normal-out')), 26) * 60))
         END
       ),
       '签退'
FROM tmp_1012_late_early_targets t;

INSERT INTO yuangongqiandao (addtime, gonghao, xingming, touxiang, qiandaoshijian, qiandaodidian)
SELECT TIMESTAMP(m.overtime_day, '18:30:00'),
       e.gonghao,
       e.xingming,
       e.touxiang,
       TIMESTAMP(m.overtime_day, '18:30:00'),
       '加班开始'
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m;

INSERT INTO yuangongqiandao (addtime, gonghao, xingming, touxiang, qiandaoshijian, qiandaodidian)
SELECT TIMESTAMP(m.overtime_day, '20:30:00'),
       e.gonghao,
       e.xingming,
       e.touxiang,
       TIMESTAMP(m.overtime_day, '20:30:00'),
       '加班结束'
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m;

DROP TEMPORARY TABLE IF EXISTS tmp_1012_late_early_count;
CREATE TEMPORARY TABLE tmp_1012_late_early_count AS
SELECT gonghao,
       ym,
       SUM(CASE WHEN is_late = 1 THEN 1 ELSE 0 END) AS late_count,
       SUM(CASE WHEN is_early = 1 THEN 1 ELSE 0 END) AS early_count
FROM tmp_1012_late_early_targets
GROUP BY gonghao, ym;
ALTER TABLE tmp_1012_late_early_count ADD PRIMARY KEY (gonghao, ym);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_workday_count;
CREATE TEMPORARY TABLE tmp_1012_workday_count AS
SELECT ym, COUNT(*) AS workday_count
FROM tmp_1012_workdays
GROUP BY ym;
ALTER TABLE tmp_1012_workday_count ADD PRIMARY KEY (ym);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_actual_absence_count;
CREATE TEMPORARY TABLE tmp_1012_actual_absence_count AS
SELECT gonghao, ym, COUNT(*) AS absence_days
FROM tmp_1012_absence_targets
GROUP BY gonghao, ym;
ALTER TABLE tmp_1012_actual_absence_count ADD PRIMARY KEY (gonghao, ym);

DROP TEMPORARY TABLE IF EXISTS tmp_1012_overtime;
CREATE TEMPORARY TABLE tmp_1012_overtime AS
SELECT s.gonghao,
       DATE_FORMAT(s.qiandaoshijian, '%Y-%m') AS ym,
       ROUND(SUM(TIMESTAMPDIFF(MINUTE, s.qiandaoshijian, e.qiandaoshijian)) / 60, 2) AS overtime_hours
FROM yuangongqiandao s
JOIN yuangongqiandao e
  ON e.gonghao = s.gonghao
 AND DATE(e.qiandaoshijian) = DATE(s.qiandaoshijian)
 AND e.qiandaodidian = '加班结束'
 AND e.qiandaoshijian >= s.qiandaoshijian
WHERE s.qiandaodidian = '加班开始'
  AND s.qiandaoshijian >= '2026-10-01'
  AND s.qiandaoshijian < '2027-01-01'
GROUP BY s.gonghao, DATE_FORMAT(s.qiandaoshijian, '%Y-%m');
ALTER TABLE tmp_1012_overtime ADD PRIMARY KEY (gonghao, ym);

INSERT INTO yuangongkaoqin (
  addtime, dengjibianhao, xingming, gonghao, zhengchangcishu, chidaocishu,
  zaotuicishu, chuchacishu, jiabancishu, qingjiacishu, kuanggongcishu,
  dengjiriqi, beizhu, guanlizhanghao, guanlixingming
)
SELECT TIMESTAMP(m.end_date, '10:00:00'),
       CONCAT('D', DATE_FORMAT(m.end_date, '%Y%m'), LPAD(e.emp_id, 5, '0')),
       e.xingming,
       e.gonghao,
       wc.workday_count - 1 - IFNULL(a.absence_days, 0),
       IFNULL(le.late_count, 0),
       IFNULL(le.early_count, 0),
       MOD(e.emp_id + m.month_no, 2),
       2 + MOD(e.emp_id + m.month_no, 5),
       1,
       IFNULL(a.absence_days, 0),
       m.end_date,
       CONCAT(m.month_label, '考勤汇总'),
       e.guanlizhanghao,
       e.guanlixingming
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m
JOIN tmp_1012_workday_count wc ON wc.ym = m.ym
LEFT JOIN tmp_1012_late_early_count le
  ON le.gonghao = e.gonghao
 AND le.ym = m.ym
LEFT JOIN tmp_1012_actual_absence_count a
  ON a.gonghao = e.gonghao
 AND a.ym = m.ym;

DROP TEMPORARY TABLE IF EXISTS tmp_1012_salary_calc;
CREATE TEMPORARY TABLE tmp_1012_salary_calc AS
SELECT e.emp_id,
       e.gonghao,
       e.xingming,
       m.ym,
       m.month_no,
       m.month_label,
       m.end_date,
       e.guanlizhanghao,
       e.guanlixingming,
       1 AS qingjiatianshu,
       ROUND(e.prev_jibengongzi *
         CASE m.month_no
           WHEN 10 THEN 1.02
           WHEN 11 THEN 1.025
           ELSE 1.04
         END, 2) AS jibengongzi,
       IFNULL(o.overtime_hours, 0) AS overtime_hours,
       e.gangweibutie,
       e.koukuanjine,
       e.koukuanyuanyin,
       IFNULL(a.absence_days, 0) AS weiqiandaotianshu
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m
LEFT JOIN tmp_1012_overtime o
  ON o.gonghao = e.gonghao
 AND o.ym = m.ym
LEFT JOIN tmp_1012_actual_absence_count a
  ON a.gonghao = e.gonghao
 AND a.ym = m.ym;
ALTER TABLE tmp_1012_salary_calc ADD PRIMARY KEY (gonghao, ym);

INSERT INTO yuangongxinzi (
  addtime, xingming, gonghao, qingjiatianshu, jibengongzi, jiabangongzi,
  jixiaojiangjin, gangweibutie, koukuanjine, qingjiakouxin,
  weiqiandaotianshu, weiqiandaokouxin, jiaqikouxin, shifagongzi,
  koukuanyuanyin, dengjiriqi, guanlizhanghao, guanlixingming, sfsh, shhf, ispay
)
SELECT TIMESTAMP(end_date, '10:00:00'),
       xingming,
       gonghao,
       qingjiatianshu,
       jibengongzi,
       ROUND(overtime_hours * jibengongzi / 116, 2),
       0,
       gangweibutie,
       koukuanjine,
       0,
       weiqiandaotianshu,
       ROUND(weiqiandaotianshu * (jibengongzi / 21.75) * -1.5, 2),
       ROUND(qingjiatianshu * (jibengongzi / 21.75) * -1, 2),
       ROUND(
         jibengongzi
         + ROUND(overtime_hours * jibengongzi / 116, 2)
         + gangweibutie
         + ROUND(weiqiandaotianshu * (jibengongzi / 21.75) * -1.5, 2)
         + ROUND(qingjiatianshu * (jibengongzi / 21.75) * -1, 2)
         - koukuanjine,
         2
       ),
       koukuanyuanyin,
       end_date,
       guanlizhanghao,
       guanlixingming,
       '是',
       CONCAT(month_label, '薪资已按考勤、请假、加班自动核算'),
       '已支付'
FROM tmp_1012_salary_calc;

INSERT INTO jixiaokaohe (
  addtime, xingming, gonghao, yuangongkaoqin, gongzuotaidu, yewujineng,
  gongzuojixiao, zongdefen, dengjiriqi, guanlizhanghao, guanlixingming
)
SELECT TIMESTAMP(m.end_date, '10:00:00'),
       e.xingming,
       e.gonghao,
       GREATEST(6, 10 - IFNULL(le.late_count, 0) - IFNULL(le.early_count, 0) - IFNULL(a.absence_days, 0)),
       8 + MOD(e.emp_id + m.month_no, 3),
       8 + MOD(e.emp_id + m.month_no + 1, 3),
       8 + MOD(e.emp_id + m.month_no + 2, 3),
       GREATEST(70, 92 - IFNULL(a.absence_days, 0) * 4 - IFNULL(le.late_count, 0) - IFNULL(le.early_count, 0)),
       m.end_date,
       e.guanlizhanghao,
       e.guanlixingming
FROM tmp_1012_employees e
CROSS JOIN tmp_1012_months m
LEFT JOIN tmp_1012_late_early_count le
  ON le.gonghao = e.gonghao
 AND le.ym = m.ym
LEFT JOIN tmp_1012_actual_absence_count a
  ON a.gonghao = e.gonghao
 AND a.ym = m.ym;

COMMIT;

SELECT 'yuangongxinzi' AS table_name, DATE_FORMAT(dengjiriqi, '%Y-%m') AS ym, COUNT(*) AS row_count
FROM yuangongxinzi
WHERE dengjiriqi >= '2026-10-01'
  AND dengjiriqi < '2027-01-01'
GROUP BY DATE_FORMAT(dengjiriqi, '%Y-%m')
UNION ALL
SELECT 'yuangongkaoqin', DATE_FORMAT(dengjiriqi, '%Y-%m'), COUNT(*)
FROM yuangongkaoqin
WHERE dengjiriqi >= '2026-10-01'
  AND dengjiriqi < '2027-01-01'
GROUP BY DATE_FORMAT(dengjiriqi, '%Y-%m')
UNION ALL
SELECT 'qingjiashenqing', DATE_FORMAT(qingjiashijian, '%Y-%m'), COUNT(*)
FROM qingjiashenqing
WHERE qingjiashijian >= '2026-10-01'
  AND qingjiashijian < '2027-01-01'
GROUP BY DATE_FORMAT(qingjiashijian, '%Y-%m')
UNION ALL
SELECT 'jixiaokaohe', DATE_FORMAT(dengjiriqi, '%Y-%m'), COUNT(*)
FROM jixiaokaohe
WHERE dengjiriqi >= '2026-10-01'
  AND dengjiriqi < '2027-01-01'
GROUP BY DATE_FORMAT(dengjiriqi, '%Y-%m')
UNION ALL
SELECT 'yuangongqiandao', DATE_FORMAT(qiandaoshijian, '%Y-%m'), COUNT(*)
FROM yuangongqiandao
WHERE qiandaoshijian >= '2026-10-01'
  AND qiandaoshijian < '2027-01-01'
GROUP BY DATE_FORMAT(qiandaoshijian, '%Y-%m')
ORDER BY table_name, ym;
