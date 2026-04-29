USE `springboot32e20828n`;
SET NAMES utf8mb4;

-- 职位调动/申诉口径：
-- 1. 职位调动是主记录，所有员工至少有一条职位调动记录。
-- 2. 员工同意职位调动时，不生成职位申诉。
-- 3. 员工对职位调动有异议时，在申诉管理中保留一条对应职位申诉。
-- 4. 工资/薪资类申诉不是职位调动申诉，本脚本不处理。

START TRANSACTION;

DROP TEMPORARY TABLE IF EXISTS tmp_position_appeal_emp;
CREATE TEMPORARY TABLE tmp_position_appeal_emp (
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL PRIMARY KEY
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO tmp_position_appeal_emp (gonghao)
SELECT DISTINCT gonghao
FROM zhiweishensu
WHERE IFNULL(shensuyuanyin, '') NOT LIKE '%工资%'
  AND IFNULL(shensuyuanyin, '') NOT LIKE '%薪资%';

INSERT INTO zhiweidiaodong (
  addtime, xingming, gonghao, zhiwei, xianzhiwei, biandongyuanyin, biandongriqi,
  guanlizhanghao, guanlixingming, sfsh, shhf, zhuangtai
)
SELECT '2026-04-30 10:00:00',
       y.xingming,
       y.gonghao,
       y.zhiwei,
       CASE y.zhiwei
         WHEN '市场推广专员' THEN '设计部UI设计师'
         WHEN '设计部UI设计师' THEN '人力资源专员'
         WHEN '人力资源专员' THEN '运营数据分析员'
         WHEN '运营数据分析员' THEN '财务会计主管'
         WHEN '财务会计主管' THEN '销售区域经理'
         WHEN '销售区域经理' THEN '技术支持工程师'
         WHEN '技术支持工程师' THEN '生产车间班组长'
         WHEN '生产车间班组长' THEN '市场推广专员'
         ELSE '人力资源专员'
       END,
       CASE
         WHEN pae.gonghao IS NOT NULL THEN '员工对岗位调整提出异议，补录原始调动记录'
         ELSE '部门岗位轮换，补录职位调动记录'
       END,
       DATE_ADD('2026-04-01', INTERVAL MOD(y.id, 26) DAY),
       CASE MOD(y.id, 8)
         WHEN 0 THEN '101'
         WHEN 1 THEN '102'
         WHEN 2 THEN '108'
         WHEN 3 THEN '104'
         WHEN 4 THEN '103'
         WHEN 5 THEN '107'
         WHEN 6 THEN '106'
         ELSE '105'
       END,
       CASE MOD(y.id, 8)
         WHEN 0 THEN '赵芳'
         WHEN 1 THEN '张雨'
         WHEN 2 THEN '李静'
         WHEN 3 THEN '孙俪'
         WHEN 4 THEN '赵敏'
         WHEN 5 THEN '李军'
         WHEN 6 THEN '王磊'
         ELSE '刘洋'
       END,
       '是',
       '人事已登记',
       CASE
         WHEN pae.gonghao IS NOT NULL OR MOD(y.id, 10) IN (2, 5, 8) THEN '已申诉'
         ELSE '待完成'
       END
FROM yuangong y
LEFT JOIN zhiweidiaodong d ON d.gonghao = y.gonghao
LEFT JOIN tmp_position_appeal_emp pae ON pae.gonghao = y.gonghao
WHERE d.id IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_primary_transfer;
CREATE TEMPORARY TABLE tmp_primary_transfer (
  gonghao varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL PRIMARY KEY,
  transfer_id bigint(20) NOT NULL
) ENGINE=Memory DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tmp_primary_transfer (gonghao, transfer_id)
SELECT d.gonghao, MIN(d.id)
FROM zhiweidiaodong d
JOIN tmp_position_appeal_emp pae ON pae.gonghao = d.gonghao
WHERE IFNULL(d.zhuangtai, '') <> '已同意'
GROUP BY d.gonghao;

UPDATE zhiweidiaodong d
JOIN tmp_primary_transfer p ON p.transfer_id = d.id
SET d.zhuangtai = '已申诉',
    d.sfsh = '是',
    d.shhf = '员工已提交职位申诉';

DELETE FROM zhiweishensu
WHERE IFNULL(shensuyuanyin, '') NOT LIKE '%工资%'
  AND IFNULL(shensuyuanyin, '') NOT LIKE '%薪资%';

INSERT INTO zhiweishensu (
  addtime, gonghao, xingming, shensuyuanyin, shensuriqi,
  guanlizhanghao, guanlixingming, crossuserid, crossrefid, sfsh, shhf
)
SELECT '2026-04-30 10:00:00',
       d.gonghao,
       d.xingming,
       CONCAT('对职位调动有异议：', IFNULL(d.zhiwei, ''), ' 调整为 ', IFNULL(d.xianzhiwei, '')),
       d.biandongriqi,
       d.guanlizhanghao,
       d.guanlixingming,
       y.id,
       d.id,
       '待审核',
       ''
FROM zhiweidiaodong d
JOIN yuangong y ON y.gonghao = d.gonghao
WHERE d.zhuangtai = '已申诉';

COMMIT;

SELECT COUNT(*) AS employees FROM yuangong;
SELECT COUNT(DISTINCT gonghao) AS employees_with_transfer, COUNT(*) AS transfer_rows FROM zhiweidiaodong;
SELECT COUNT(*) AS agreed_transfer_appeals
FROM zhiweishensu s
JOIN zhiweidiaodong d ON d.id = s.crossrefid
WHERE d.zhuangtai = '已同意'
  AND IFNULL(s.shensuyuanyin, '') NOT LIKE '%工资%'
  AND IFNULL(s.shensuyuanyin, '') NOT LIKE '%薪资%';
SELECT d.gonghao, d.xingming, d.zhiwei, d.xianzhiwei, d.biandongriqi, d.zhuangtai, COUNT(s.id) AS position_appeals
FROM zhiweidiaodong d
LEFT JOIN zhiweishensu s
  ON s.crossrefid = d.id
 AND IFNULL(s.shensuyuanyin, '') NOT LIKE '%工资%'
 AND IFNULL(s.shensuyuanyin, '') NOT LIKE '%薪资%'
WHERE d.gonghao = '002'
GROUP BY d.id, d.gonghao, d.xingming, d.zhiwei, d.xianzhiwei, d.biandongriqi, d.zhuangtai;
