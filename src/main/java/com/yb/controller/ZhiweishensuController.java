package com.yb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.annotation.IgnoreAuth;
import com.yb.annotation.SysLog;
import com.yb.entity.YuangongxinziEntity;
import com.yb.entity.YuangongEntity;
import com.yb.entity.ZhiweidiaodongEntity;
import com.yb.entity.ZhiweishensuEntity;
import com.yb.entity.view.ZhiweishensuView;
import com.yb.service.YuangongService;
import com.yb.service.YuangongxinziService;
import com.yb.service.ZhiweidiaodongService;
import com.yb.service.ZhiweishensuService;
import com.yb.utils.DeSensUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 鑱屼綅鐢宠瘔
 */
@RestController
@RequestMapping("/zhiweishensu")
public class ZhiweishensuController {
    @Autowired
    private ZhiweishensuService zhiweishensuService;

    @Autowired
    private YuangongxinziService yuangongxinziService;

    @Autowired
    private YuangongService yuangongService;

    @Autowired
    private ZhiweidiaodongService zhiweidiaodongService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZhiweishensuEntity zhiweishensu, HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if ("yuangong".equals(tableName)) {
            zhiweishensu.setGonghao((String) request.getSession().getAttribute("username"));
        }
        if ("renshiguanliyuan".equals(tableName)) {
            zhiweishensu.setGuanlizhanghao((String) request.getSession().getAttribute("username"));
        }

        QueryWrapper<ZhiweishensuEntity> ew = new QueryWrapper<>();
        applyAppealTypeCondition(ew, params.get("shensuleixing"));
        PageUtils page = zhiweishensuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweishensu), params), params));
        enrichAppealRows(page.getList());
        DeSensUtil.desensitize(page, new HashMap<>());
        return R.ok().put("data", page);
    }

    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,
                  ZhiweishensuEntity zhiweishensu,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date shensuriqistart,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date shensuriqiend,
                  HttpServletRequest request) {
        QueryWrapper<ZhiweishensuEntity> ew = new QueryWrapper<>();
        applyAppealTypeCondition(ew, params.get("shensuleixing"));
        if (shensuriqistart != null) {
            ew.ge("shensuriqi", shensuriqistart);
        }
        if (shensuriqiend != null) {
            ew.le("shensuriqi", shensuriqiend);
        }

        PageUtils page = zhiweishensuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweishensu), params), params));
        enrichAppealRows(page.getList());
        DeSensUtil.desensitize(page, new HashMap<>());
        return R.ok().put("data", page);
    }

    @RequestMapping("/lists")
    public R lists(ZhiweishensuEntity zhiweishensu) {
        QueryWrapper<ZhiweishensuEntity> ew = new QueryWrapper<>();
        ew.allEq(MPUtil.allEQMapPre(zhiweishensu, "zhiweishensu"));
        return R.ok().put("data", zhiweishensuService.selectListView(ew));
    }

    @RequestMapping("/query")
    public R query(ZhiweishensuEntity zhiweishensu) {
        QueryWrapper<ZhiweishensuEntity> ew = new QueryWrapper<>();
        ew.allEq(MPUtil.allEQMapPre(zhiweishensu, "zhiweishensu"));
        ZhiweishensuView zhiweishensuView = zhiweishensuService.selectView(ew);
        return R.ok("鏌ヨ鎴愬姛").put("data", zhiweishensuView);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ZhiweishensuEntity zhiweishensu = zhiweishensuService.getById(id);
        enrichAppealRow(zhiweishensu);
        DeSensUtil.desensitize(zhiweishensu, new HashMap<>());
        return R.ok().put("data", zhiweishensu);
    }

    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ZhiweishensuEntity zhiweishensu = zhiweishensuService.getById(id);
        enrichAppealRow(zhiweishensu);
        DeSensUtil.desensitize(zhiweishensu, new HashMap<>());
        return R.ok().put("data", zhiweishensu);
    }

    @RequestMapping("/save")
    @SysLog("鏂板鑱屼綅鐢宠瘔")
    public R save(@RequestBody ZhiweishensuEntity zhiweishensu, HttpServletRequest request) {
        zhiweishensuService.save(zhiweishensu);
        return R.ok().put("data", zhiweishensu.getId());
    }

    @RequestMapping("/add")
    @SysLog("鏂板鑱屼綅鐢宠瘔")
    public R add(@RequestBody ZhiweishensuEntity zhiweishensu, HttpServletRequest request) {
        zhiweishensuService.save(zhiweishensu);
        return R.ok().put("data", zhiweishensu.getId());
    }

    @RequestMapping("/update")
    @Transactional
    @SysLog("淇敼鑱屼綅鐢宠瘔")
    public R update(@RequestBody ZhiweishensuEntity zhiweishensu, HttpServletRequest request) {
        zhiweishensuService.updateById(zhiweishensu);
        return R.ok();
    }

    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("瀹℃牳鑱屼綅鐢宠瘔")
    public R update(@RequestBody Long[] ids,
                    @RequestParam String sfsh,
                    @RequestParam(required = false, defaultValue = "") String shhf) {
        List<ZhiweishensuEntity> list = new ArrayList<>();
        Map<Long, YuangongxinziEntity> resetSalaryMap = new HashMap<>();
        Set<String> positionAppealGonghaos = new HashSet<>();

        for (Long id : ids) {
            ZhiweishensuEntity zhiweishensu = zhiweishensuService.getById(id);
            if (zhiweishensu == null) {
                continue;
            }

            boolean isSalaryAppeal = isSalaryAppeal(zhiweishensu);

            YuangongxinziEntity salary = null;
            if (zhiweishensu.getCrossrefid() != null) {
                QueryWrapper<YuangongxinziEntity> byRef = new QueryWrapper<>();
                byRef.eq("id", zhiweishensu.getCrossrefid());
                if (StringUtils.isNotBlank(zhiweishensu.getGonghao())) {
                    byRef.eq("gonghao", zhiweishensu.getGonghao());
                }
                salary = yuangongxinziService.getOne(byRef, false);
            }

            if (salary == null && isSalaryAppeal) {
                QueryWrapper<YuangongxinziEntity> fallback = new QueryWrapper<>();
                if (StringUtils.isNotBlank(zhiweishensu.getGonghao())) {
                    fallback.eq("gonghao", zhiweishensu.getGonghao());
                }
                if (StringUtils.isNotBlank(zhiweishensu.getGuanlizhanghao())) {
                    fallback.eq("guanlizhanghao", zhiweishensu.getGuanlizhanghao());
                }
                fallback.eq("ispay", "鐢宠瘔閿佸畾")
                        .orderByDesc("dengjiriqi")
                        .orderByDesc("id")
                        .last("limit 1");
                salary = yuangongxinziService.getOne(fallback, false);
            }

            if (salary != null && (isSalaryAppeal || "鐢宠瘔閿佸畾".equals(salary.getIspay()))) {
                salary.setSfsh("待审核");
                salary.setIspay("未支付");
                resetSalaryMap.put(salary.getId(), salary);
            }

            if (!isSalaryAppeal && StringUtils.isNotBlank(zhiweishensu.getGonghao())) {
                positionAppealGonghaos.add(zhiweishensu.getGonghao());
            }

            zhiweishensu.setSfsh(sfsh);
            zhiweishensu.setShhf(shhf);
            list.add(zhiweishensu);
        }

        if (!list.isEmpty()) {
            zhiweishensuService.updateBatchById(list);
        }
        if (!resetSalaryMap.isEmpty()) {
            yuangongxinziService.updateBatchById(new ArrayList<>(resetSalaryMap.values()));
        }
        for (String gonghao : positionAppealGonghaos) {
            recomputeEmployeePosition(gonghao);
        }

        return R.ok();
    }

    private boolean isApproved(String sfsh) {
        return "是".equals(sfsh) || "鏄?".equals(sfsh);
    }

    private void applyAppealTypeCondition(QueryWrapper<ZhiweishensuEntity> ew, Object shensuleixingObj) {
        String shensuleixing = shensuleixingObj == null ? "" : StringUtils.trimToEmpty(String.valueOf(shensuleixingObj));
        if (StringUtils.isBlank(shensuleixing)) {
            return;
        }
        if ("薪资异议".equals(shensuleixing)) {
            ew.and(wrapper -> wrapper.eq("shhf", "salary_appeal")
                    .or().like("shensuyuanyin", "工资")
                    .or().like("shensuyuanyin", "薪资")
                    .or().like("shensuyuanyin", "宸ヨ祫")
                    .or().like("shensuyuanyin", "钖祫"));
            return;
        }
        if ("职位异议".equals(shensuleixing)) {
            ew.and(wrapper -> wrapper.isNull("shhf").or().ne("shhf", "salary_appeal"));
            ew.and(wrapper -> wrapper.isNull("shensuyuanyin")
                    .or(condition -> condition.notLike("shensuyuanyin", "工资")
                            .notLike("shensuyuanyin", "薪资")
                            .notLike("shensuyuanyin", "宸ヨ祫")
                            .notLike("shensuyuanyin", "钖祫")));
        }
    }

    private void enrichAppealRows(List<?> rows) {
        if (rows == null || rows.isEmpty()) {
            return;
        }
        for (Object row : rows) {
            if (row instanceof ZhiweishensuEntity) {
                enrichAppealRow((ZhiweishensuEntity) row);
            }
        }
    }

    private void enrichAppealRow(ZhiweishensuEntity zhiweishensu) {
        if (zhiweishensu == null) {
            return;
        }
        if (isSalaryAppeal(zhiweishensu)) {
            zhiweishensu.setShensuleixing("薪资异议");
            return;
        }
        zhiweishensu.setShensuleixing("职位异议");
        if (zhiweishensu.getCrossrefid() == null) {
            return;
        }
        ZhiweidiaodongEntity diaodong = zhiweidiaodongService.getById(zhiweishensu.getCrossrefid());
        if (diaodong == null) {
            return;
        }
        zhiweishensu.setZhiwei(diaodong.getZhiwei());
        zhiweishensu.setXianzhiwei(diaodong.getXianzhiwei());
    }

    private boolean isSalaryAppeal(ZhiweishensuEntity zhiweishensu) {
        if (zhiweishensu == null) {
            return false;
        }
        String reason = StringUtils.defaultString(zhiweishensu.getShensuyuanyin());
        return "salary_appeal".equals(zhiweishensu.getShhf())
                || StringUtils.contains(reason, "工资")
                || StringUtils.contains(reason, "薪资")
                || StringUtils.contains(reason, "宸ヨ祫")
                || StringUtils.contains(reason, "钖祫");
    }

    private void recomputeEmployeePosition(String gonghao) {
        if (StringUtils.isBlank(gonghao)) {
            return;
        }
        ZhiweidiaodongEntity latest = zhiweidiaodongService.getOne(
                new QueryWrapper<ZhiweidiaodongEntity>()
                        .eq("gonghao", gonghao)
                        .orderByDesc("biandongriqi")
                        .orderByDesc("id")
                        .last("limit 1"),
                false
        );
        if (latest == null) {
            return;
        }
        String finalPosition = latest.getXianzhiwei();
        ZhiweishensuEntity appeal = zhiweishensuService.getOne(
                new QueryWrapper<ZhiweishensuEntity>()
                        .eq("gonghao", gonghao)
                        .eq("crossrefid", latest.getId())
                        .and(wrapper -> wrapper.isNull("shhf").or().ne("shhf", "salary_appeal"))
                        .and(wrapper -> wrapper.isNull("shensuyuanyin")
                                .or(condition -> condition.notLike("shensuyuanyin", "工资")
                                        .notLike("shensuyuanyin", "薪资")
                                        .notLike("shensuyuanyin", "宸ヨ祫")
                                        .notLike("shensuyuanyin", "钖祫")))
                        .orderByDesc("id")
                        .last("limit 1"),
                false
        );
        if (appeal != null && isApproved(appeal.getSfsh())) {
            finalPosition = latest.getZhiwei();
        }
        if (StringUtils.isBlank(finalPosition)) {
            return;
        }
        YuangongEntity yuangong = yuangongService.getOne(
                new QueryWrapper<YuangongEntity>().eq("gonghao", gonghao),
                false
        );
        if (yuangong == null) {
            return;
        }
        yuangong.setZhiwei(finalPosition);
        yuangongService.updateById(yuangong);
    }

    @RequestMapping("/delete")
    @SysLog("鍒犻櫎鑱屼綅鐢宠瘔")
    public R delete(@RequestBody Long[] ids) {
        zhiweishensuService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }
}

