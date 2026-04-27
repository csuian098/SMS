"""
随机给每个员工每个月生成 0~3 条未签到记录
- 工作日：周二~周六（避开周日/周一休息日）
- 避开已审批的请假日
- 每月数量分布：0:10%  1:30%  2:40%  3:20%
"""
import random
from datetime import date, timedelta

random.seed(20260427)  # 固定随机种子，可复现

DB_DIR = r"F:\springboot32e20828n\springboot32e20828n\db"
OUT_SQL = DB_DIR + r"\_random_absences.sql"

# 读员工
with open(DB_DIR + r"\_tmp_employees.txt", encoding="utf-8") as f:
    employees = [line.strip() for line in f if line.strip()]
print(f"employees: {len(employees)}")

# 读请假区间，展开为每日 set，按 gonghao 分组
leaves = {}  # gonghao -> set of date strings (yyyy-mm-dd)
with open(DB_DIR + r"\_tmp_leaves.txt", encoding="utf-8") as f:
    for line in f:
        parts = line.strip().split("\t")
        if len(parts) < 3:
            continue
        gh, sd, ed = parts
        try:
            sd = date.fromisoformat(sd)
            ed = date.fromisoformat(ed)
        except Exception:
            continue
        s = leaves.setdefault(gh, set())
        d = sd
        while d <= ed:
            s.add(d.isoformat())
            d += timedelta(days=1)
print(f"employees with leave: {len(leaves)}")

# 计算 Jan/Feb/Mar 2026 工作日（Tue~Sat = weekday 1~5；Sun=6, Mon=0）
def working_days(year, month):
    from calendar import monthrange
    days = []
    last = monthrange(year, month)[1]
    for d in range(1, last + 1):
        dt = date(year, month, d)
        wd = dt.weekday()  # Mon=0, Tue=1, ..., Sun=6
        if wd in (1, 2, 3, 4, 5):  # Tue~Sat
            days.append(dt.isoformat())
    return days

months = [(2026, 1), (2026, 2), (2026, 3)]
month_days = {m: working_days(*m) for m in months}
for m, ds in month_days.items():
    print(f"  {m}: {len(ds)} working days")

# 生成
def pick_count():
    r = random.random()
    if r < 0.10:
        return 0
    elif r < 0.40:
        return 1
    elif r < 0.80:
        return 2
    else:
        return 3

absences = []  # list of (gonghao, date)
stats = {0: 0, 1: 0, 2: 0, 3: 0}
for gh in employees:
    leave_set = leaves.get(gh, set())
    for (y, mo) in months:
        candidates = [d for d in month_days[(y, mo)] if d not in leave_set]
        n = pick_count()
        n = min(n, len(candidates))
        stats[n] = stats.get(n, 0) + 1
        if n == 0:
            continue
        chosen = random.sample(candidates, n)
        for d in chosen:
            absences.append((gh, d))

print(f"\ntotal absence records: {len(absences)}")
print(f"distribution per (employee,month): {stats}")

# 写 SQL
with open(OUT_SQL, "w", encoding="utf-8") as f:
    f.write("-- 随机生成的未签到删除脚本（删除对应日期的所有签到记录，让该日变红）\n")
    f.write(f"-- 共 {len(absences)} 条未签到\n\n")
    f.write("SET autocommit=0;\nSTART TRANSACTION;\n\n")
    for gh, d in absences:
        gh_esc = gh.replace("'", "''")
        f.write(f"DELETE FROM yuangongqiandao WHERE gonghao='{gh_esc}' AND DATE(qiandaoshijian)='{d}';\n")
    f.write("\nCOMMIT;\n")
    f.write("\n-- 验证：剩余签到记录数（应该比原来少）\n")
    f.write("SELECT COUNT(*) AS qiandao_remaining FROM yuangongqiandao;\n")

print(f"\nSQL written to: {OUT_SQL}")
