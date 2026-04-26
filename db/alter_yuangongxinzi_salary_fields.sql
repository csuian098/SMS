-- 员工薪资表 yuangongxinzi：与实体 YuangongxinziEntity 对齐的新增字段
-- 在业务库上执行一次（若某列已存在会报错，跳过对应语句即可）

ALTER TABLE yuangongxinzi
  ADD COLUMN qingjiakouxin DOUBLE NULL DEFAULT NULL COMMENT '请假扣薪' AFTER koukuanjine;

ALTER TABLE yuangongxinzi
  ADD COLUMN weiqiandaotianshu INT NULL DEFAULT NULL COMMENT '未签到天数' AFTER qingjiakouxin;

ALTER TABLE yuangongxinzi
  ADD COLUMN weiqiandaokouxin DOUBLE NULL DEFAULT NULL COMMENT '未签到扣薪' AFTER weiqiandaotianshu;
