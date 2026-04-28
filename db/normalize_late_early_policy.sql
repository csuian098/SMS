USE `springboot32e20828n`;
SET NAMES utf8mb4;

-- 考勤异常规则：
-- 每名员工每个月单独迟到天数 <= 3，单独早退天数 <= 3；
-- 同一天既迟到又早退的天数 <= 2。
-- 因此异常天数最大为 3 + 3 + 2 = 8 天，最小为 1 次迟到 + 1 次早退 = 2 天。
-- 页面日历口径：签到时间 > 08:00 显示迟到，签退时间 < 18:00 显示早退。

START TRANSACTION;

DROP TEMPORARY TABLE IF EXISTS tmp_attendance_days;
CREATE TEMPORARY TABLE tmp_attendance_days (
  emp_id bigint(20) NOT NULL,
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  ym char(7) COLLATE utf8mb4_unicode_ci NOT NULL,
  d date NOT NULL,
  PRIMARY KEY (gonghao, ym, d),
  KEY idx_emp_month (emp_id, ym, d)
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tmp_attendance_days (emp_id, gonghao, ym, d)
SELECT y.id AS emp_id,
       y.gonghao,
       DATE_FORMAT(q.qiandaoshijian, '%Y-%m') AS ym,
       DATE(q.qiandaoshijian) AS d
FROM yuangong y
JOIN yuangongqiandao q
  ON q.gonghao = y.gonghao
 AND q.qiandaodidian = '签到'
WHERE q.qiandaoshijian >= '2026-01-01'
  AND q.qiandaoshijian < '2026-05-01'
GROUP BY y.id, y.gonghao, DATE_FORMAT(q.qiandaoshijian, '%Y-%m'), DATE(q.qiandaoshijian);

DROP TEMPORARY TABLE IF EXISTS tmp_attendance_days_rank_source;
CREATE TEMPORARY TABLE tmp_attendance_days_rank_source (
  emp_id bigint(20) NOT NULL,
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  ym char(7) COLLATE utf8mb4_unicode_ci NOT NULL,
  d date NOT NULL,
  PRIMARY KEY (gonghao, ym, d)
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tmp_attendance_days_rank_source
SELECT * FROM tmp_attendance_days;

DROP TEMPORARY TABLE IF EXISTS tmp_attendance_ranked_days;
CREATE TEMPORARY TABLE tmp_attendance_ranked_days (
  emp_id bigint(20) NOT NULL,
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  ym char(7) COLLATE utf8mb4_unicode_ci NOT NULL,
  d date NOT NULL,
  rn int NOT NULL,
  PRIMARY KEY (gonghao, ym, d),
  KEY idx_rank (gonghao, ym, rn)
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tmp_attendance_ranked_days (emp_id, gonghao, ym, d, rn)
SELECT d1.emp_id,
       d1.gonghao,
       d1.ym,
       d1.d,
       COUNT(d2.d) AS rn
FROM tmp_attendance_days d1
JOIN tmp_attendance_days_rank_source d2
  ON d2.gonghao = d1.gonghao
 AND d2.ym = d1.ym
 AND d2.d <= d1.d
GROUP BY d1.emp_id, d1.gonghao, d1.ym, d1.d;

DROP TEMPORARY TABLE IF EXISTS tmp_late_early_targets;
CREATE TEMPORARY TABLE tmp_late_early_targets (
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  ym char(7) COLLATE utf8mb4_unicode_ci NOT NULL,
  d date NOT NULL,
  is_late tinyint(1) NOT NULL DEFAULT 0,
  is_early tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (gonghao, ym, d),
  KEY idx_target_month (gonghao, ym)
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tmp_late_early_targets (gonghao, ym, d, is_late, is_early)
SELECT gonghao,
       ym,
       d,
       CASE
         WHEN rn = 2 THEN 1
         WHEN rn = 8 AND MOD(emp_id, 10) IN (1, 3, 4, 5, 6, 7, 8) THEN 1
         WHEN rn = 14 AND MOD(emp_id, 10) IN (5, 7, 8) THEN 1
         WHEN rn = 6 AND MOD(emp_id, 10) IN (4, 5, 6, 7, 8, 9) THEN 1
         WHEN rn = 12 AND MOD(emp_id, 10) = 8 THEN 1
         ELSE 0
       END AS is_late,
       CASE
         WHEN rn = 4 THEN 1
         WHEN rn = 10 AND MOD(emp_id, 10) IN (2, 3, 4, 5, 6, 7, 8) THEN 1
         WHEN rn = 16 AND MOD(emp_id, 10) IN (6, 7, 8) THEN 1
         WHEN rn = 6 AND MOD(emp_id, 10) IN (4, 5, 6, 7, 8, 9) THEN 1
         WHEN rn = 12 AND MOD(emp_id, 10) = 8 THEN 1
         ELSE 0
       END AS is_early
FROM tmp_attendance_ranked_days;

UPDATE yuangongqiandao q
JOIN tmp_late_early_targets t
  ON t.gonghao = q.gonghao
 AND t.d = DATE(q.qiandaoshijian)
SET q.qiandaoshijian = TIMESTAMP(
      DATE(q.qiandaoshijian),
      CASE
        WHEN t.is_late = 1 THEN ADDTIME('08:06:00', SEC_TO_TIME(MOD(CRC32(CONCAT(q.gonghao, t.ym, 'late')), 16) * 60))
        ELSE ADDTIME('07:42:00', SEC_TO_TIME(MOD(CRC32(CONCAT(q.gonghao, t.ym, 'normal-in')), 16) * 60))
      END
    )
WHERE q.qiandaodidian = '签到'
  AND q.qiandaoshijian >= '2026-01-01'
  AND q.qiandaoshijian < '2026-05-01';

UPDATE yuangongqiandao q
JOIN tmp_late_early_targets t
  ON t.gonghao = q.gonghao
 AND t.d = DATE(q.qiandaoshijian)
SET q.qiandaoshijian = TIMESTAMP(
      DATE(q.qiandaoshijian),
      CASE
        WHEN t.is_early = 1 THEN ADDTIME('17:32:00', SEC_TO_TIME(MOD(CRC32(CONCAT(q.gonghao, t.ym, 'early')), 20) * 60))
        ELSE ADDTIME('18:05:00', SEC_TO_TIME(MOD(CRC32(CONCAT(q.gonghao, t.ym, 'normal-out')), 26) * 60))
      END
    )
WHERE q.qiandaodidian = '签退'
  AND q.qiandaoshijian >= '2026-01-01'
  AND q.qiandaoshijian < '2026-05-01';

DROP TEMPORARY TABLE IF EXISTS tmp_late_early_month_count;
CREATE TEMPORARY TABLE tmp_late_early_month_count AS
SELECT gonghao,
       ym,
       SUM(CASE WHEN is_late = 1 AND is_early = 0 THEN 1 ELSE 0 END) AS late_only_count,
       SUM(CASE WHEN is_late = 0 AND is_early = 1 THEN 1 ELSE 0 END) AS early_only_count,
       SUM(CASE WHEN is_late = 1 AND is_early = 1 THEN 1 ELSE 0 END) AS late_early_count,
       SUM(CASE WHEN is_late = 1 OR is_early = 1 THEN 1 ELSE 0 END) AS abnormal_days
FROM tmp_late_early_targets
GROUP BY gonghao, ym;
ALTER TABLE tmp_late_early_month_count ADD PRIMARY KEY (gonghao, ym);

UPDATE yuangongkaoqin k
LEFT JOIN tmp_late_early_month_count c
  ON c.gonghao = k.gonghao
 AND c.ym = DATE_FORMAT(k.dengjiriqi, '%Y-%m')
SET k.chidaocishu = IFNULL(c.late_only_count, 0),
    k.zaotuicishu = IFNULL(c.early_only_count, 0)
WHERE k.dengjiriqi >= '2026-01-01'
  AND k.dengjiriqi < '2026-05-01';

COMMIT;

SELECT ym,
       COUNT(*) AS employee_count,
       MIN(late_only_count) AS min_late_only,
       MAX(late_only_count) AS max_late_only,
       MIN(early_only_count) AS min_early_only,
       MAX(early_only_count) AS max_early_only,
       MIN(late_early_count) AS min_late_early,
       MAX(late_early_count) AS max_late_early,
       MIN(abnormal_days) AS min_abnormal_days,
       MAX(abnormal_days) AS max_abnormal_days
FROM tmp_late_early_month_count
GROUP BY ym
ORDER BY ym;
