USE `springboot32e20828n`;
SET NAMES utf8mb4;

START TRANSACTION;

DELETE FROM yuangongqiandao
WHERE qiandaoshijian >= '2026-04-30'
  AND qiandaoshijian < '2026-05-01';

DELETE FROM yuangongxinzi
WHERE dengjiriqi = '2026-04-30';

UPDATE yuangongkaoqin
SET dengjiriqi = '2026-04-29',
    beizhu = '4月考勤汇总（截至4月29日，4月30日用于演示现场签到与核算）'
WHERE dengjiriqi = '2026-04-30';

UPDATE jixiaokaohe
SET dengjiriqi = '2026-04-29'
WHERE dengjiriqi = '2026-04-30';

COMMIT;

SELECT 'qiandao_0430' AS item, COUNT(*) AS cnt
FROM yuangongqiandao
WHERE qiandaoshijian >= '2026-04-30'
  AND qiandaoshijian < '2026-05-01'
UNION ALL
SELECT 'xinzi_0430', COUNT(*)
FROM yuangongxinzi
WHERE dengjiriqi = '2026-04-30'
UNION ALL
SELECT 'kaoqin_0430', COUNT(*)
FROM yuangongkaoqin
WHERE dengjiriqi = '2026-04-30'
UNION ALL
SELECT 'jixiao_0430', COUNT(*)
FROM jixiaokaohe
WHERE dengjiriqi = '2026-04-30'
UNION ALL
SELECT 'qiandao_0429', COUNT(*)
FROM yuangongqiandao
WHERE qiandaoshijian >= '2026-04-29'
  AND qiandaoshijian < '2026-04-30'
UNION ALL
SELECT 'kaoqin_0429', COUNT(*)
FROM yuangongkaoqin
WHERE dengjiriqi = '2026-04-29'
UNION ALL
SELECT 'jixiao_0429', COUNT(*)
FROM jixiaokaohe
WHERE dengjiriqi = '2026-04-29';
