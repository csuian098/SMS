package com.yb.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.lang.*;
import java.math.*;
import com.yb.utils.*;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.yb.annotation.IgnoreAuth;
import com.yb.annotation.SysLog;

import com.yb.entity.YuangongxinziEntity;
import com.yb.entity.YuangongkaoqinEntity;
import com.yb.entity.QingjiashenqingEntity;
import com.yb.entity.YuangongEntity;
import com.yb.entity.YuangongqiandaoEntity;
import com.yb.entity.view.YuangongxinziView;

import com.yb.service.YuangongxinziService;
import com.yb.service.YuangongkaoqinService;
import com.yb.service.QingjiashenqingService;
import com.yb.service.YuangongService;
import com.yb.service.YuangongqiandaoService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 鍛樺伐钖祫
 * 鍚庣鎺ュ彛
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/yuangongxinzi")
public class YuangongxinziController {
    @Autowired
    private YuangongxinziService yuangongxinziService;
    @Autowired
    private YuangongkaoqinService yuangongkaoqinService;
    @Autowired
    private QingjiashenqingService qingjiashenqingService;
    @Autowired
    private YuangongService yuangongService;
    @Autowired
    private YuangongqiandaoService yuangongqiandaoService;
    private volatile boolean performanceNormalizedAtRuntime = false;

    @PostConstruct
    public void normalizePerformanceBonusOnStartup() {
        try {
            List<YuangongxinziEntity> list = yuangongxinziService.list(
                new QueryWrapper<YuangongxinziEntity>().orderByAsc("id"));
            if (list != null) {
                for (YuangongxinziEntity item : list) {
                    if (item == null) continue;
                    if (!org.apache.commons.lang3.StringUtils.isBlank(item.getGonghao())) {
                        fillAutoSalaryFields(item);
                    } else {
                        item.setJixiaojiangjin(0D);
                        item.setShifagongzi(calculateShifaWithoutPerformance(item));
                    }
                    yuangongxinziService.updateById(item);
                }
            }
            performanceNormalizedAtRuntime = true;
        } catch (Exception e) {
            System.err.println("[yuangongxinzi] startup recalc failed: " + e.getMessage());
        }
    }

    private synchronized void ensurePerformanceNormalizedOnce() {
        if (performanceNormalizedAtRuntime) {
            return;
        }
        try {
            normalizeAllPerformanceBonusAndSalary(false);
            performanceNormalizedAtRuntime = true;
        } catch (Exception e) {
            System.err.println("[yuangongxinzi] runtime normalize failed: " + e.getMessage());
        }
    }










    /**
     * 鍚庡彴鍒楄〃
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YuangongxinziEntity yuangongxinzi,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqiend,
		HttpServletRequest request){
        ensurePerformanceNormalizedOnce();
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			yuangongxinzi.setGonghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("renshiguanliyuan")) {
			yuangongxinzi.setGuanlizhanghao((String)request.getSession().getAttribute("username"));
		}
        //璁剧疆鏌ヨ鏉′欢
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        if(dengjiriqistart!=null) ew.ge("dengjiriqi", dengjiriqistart);
        if(dengjiriqiend!=null) ew.le("dengjiriqi", dengjiriqiend);


        //鏌ヨ缁撴灉
		PageUtils page = yuangongxinziService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongxinzi), params), params));
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
     * 鍓嶅彴鍒楄〃
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YuangongxinziEntity yuangongxinzi,
                @RequestParam(required = false) Double qingjiatianshustart,
                @RequestParam(required = false) Double qingjiatianshuend,
                @RequestParam(required = false) Double jibengongzistart,
                @RequestParam(required = false) Double jibengongziend,
                @RequestParam(required = false) Double jiabangongzistart,
                @RequestParam(required = false) Double jiabangongziend,
                @RequestParam(required = false) Double jixiaojiangjinstart,
                @RequestParam(required = false) Double jixiaojiangjinend,
                @RequestParam(required = false) Double gangweibutiestart,
                @RequestParam(required = false) Double gangweibutieend,
                @RequestParam(required = false) Double koukuanjinestart,
                @RequestParam(required = false) Double koukuanjineend,
                @RequestParam(required = false) Double jiaqikouxinstart,
                @RequestParam(required = false) Double jiaqikouxinend,
                @RequestParam(required = false) Double shifagongzistart,
                @RequestParam(required = false) Double shifagongziend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqiend,
		HttpServletRequest request){
        ensurePerformanceNormalizedOnce();
        //璁剧疆鏌ヨ鏉′欢
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        if(qingjiatianshustart!=null) ew.ge("qingjiatianshu", qingjiatianshustart);
        if(qingjiatianshuend!=null) ew.le("qingjiatianshu", qingjiatianshuend);
        if(jibengongzistart!=null) ew.ge("jibengongzi", jibengongzistart);
        if(jibengongziend!=null) ew.le("jibengongzi", jibengongziend);
        if(jiabangongzistart!=null) ew.ge("jiabangongzi", jiabangongzistart);
        if(jiabangongziend!=null) ew.le("jiabangongzi", jiabangongziend);
        if(jixiaojiangjinstart!=null) ew.ge("jixiaojiangjin", jixiaojiangjinstart);
        if(jixiaojiangjinend!=null) ew.le("jixiaojiangjin", jixiaojiangjinend);
        if(gangweibutiestart!=null) ew.ge("gangweibutie", gangweibutiestart);
        if(gangweibutieend!=null) ew.le("gangweibutie", gangweibutieend);
        if(koukuanjinestart!=null) ew.ge("koukuanjine", koukuanjinestart);
        if(koukuanjineend!=null) ew.le("koukuanjine", koukuanjineend);
        if(jiaqikouxinstart!=null) ew.ge("jiaqikouxin", jiaqikouxinstart);
        if(jiaqikouxinend!=null) ew.le("jiaqikouxin", jiaqikouxinend);
        if(shifagongzistart!=null) ew.ge("shifagongzi", shifagongzistart);
        if(shifagongziend!=null) ew.le("shifagongzi", shifagongziend);
        if(dengjiriqistart!=null) ew.ge("dengjiriqi", dengjiriqistart);
        if(dengjiriqiend!=null) ew.le("dengjiriqi", dengjiriqiend);

        //鏌ヨ缁撴灉
		PageUtils page = yuangongxinziService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongxinzi), params), params));
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 鍒楄〃
     */
    @RequestMapping("/lists")
    public R list( YuangongxinziEntity yuangongxinzi){
       	QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yuangongxinzi, "yuangongxinzi"));
        return R.ok().put("data", yuangongxinziService.selectListView(ew));
    }

	 /**
     * 鏌ヨ
     */
    @RequestMapping("/query")
    public R query(YuangongxinziEntity yuangongxinzi){
        QueryWrapper< YuangongxinziEntity> ew = new QueryWrapper< YuangongxinziEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yuangongxinzi, "yuangongxinzi"));
		YuangongxinziView yuangongxinziView =  yuangongxinziService.selectView(ew);
		return R.ok("鏌ヨ鍛樺伐钖祫鎴愬姛").put("data", yuangongxinziView);
    }

    /**
     * 鍚庡彴璇︽儏
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YuangongxinziEntity yuangongxinzi = yuangongxinziService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(yuangongxinzi,deSens);
        return R.ok().put("data", yuangongxinzi);
    }

    /**
     * 鍓嶅彴璇︽儏
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YuangongxinziEntity yuangongxinzi = yuangongxinziService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(yuangongxinzi,deSens);
        return R.ok().put("data", yuangongxinzi);
    }




    /**
     * 鍚庡彴淇濆瓨
     */
    @RequestMapping("/save")
    @SysLog("新增员工薪资")
    public R save(@RequestBody YuangongxinziEntity yuangongxinzi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongxinzi);
        fillAutoSalaryFields(yuangongxinzi);
        yuangongxinziService.save(yuangongxinzi);
        return R.ok().put("data",yuangongxinzi.getId());
    }

    /**
     * 鍓嶅彴淇濆瓨
     */
    @SysLog("新增员工薪资")
    @RequestMapping("/add")
    public R add(@RequestBody YuangongxinziEntity yuangongxinzi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongxinzi);
        fillAutoSalaryFields(yuangongxinzi);
        yuangongxinziService.save(yuangongxinzi);
        return R.ok().put("data",yuangongxinzi.getId());
    }





    /**
     * 淇敼
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改员工薪资")
    public R update(@RequestBody YuangongxinziEntity yuangongxinzi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongxinzi);
        //鍏ㄩ儴鏇存柊
        fillAutoSalaryFields(yuangongxinzi);
        yuangongxinziService.updateById(yuangongxinzi);
        return R.ok();
    }

    private void fillAutoSalaryFields(YuangongxinziEntity yuangongxinzi) {
        if (yuangongxinzi == null || StringUtils.isBlank(yuangongxinzi.getGonghao())) {
            return;
        }
        Date refDate = yuangongxinzi.getDengjiriqi() == null ? new Date() : yuangongxinzi.getDengjiriqi();
        if (yuangongxinzi.getDengjiriqi() == null) {
            yuangongxinzi.setDengjiriqi(refDate);
        }
        double jibengongzi = yuangongxinzi.getJibengongzi() == null ? 0D : yuangongxinzi.getJibengongzi();
        Map<String, Object> side = buildSalarySideComputation(yuangongxinzi.getGonghao().trim(), refDate, jibengongzi);

        int totalQingjiatianshu = ((Number) side.get("qingjiatianshu")).intValue();
        yuangongxinzi.setQingjiatianshu(totalQingjiatianshu);

        double jiabangongzi = ((Number) side.get("jiabangongzi")).doubleValue();
        yuangongxinzi.setJiabangongzi(jiabangongzi);

        // 璇峰亣鎵ｈ柂宸蹭笅绾匡紙鍓嶅悗绔潎涓嶅啀灞曠ず锛夛紝淇濈暀搴撳瓧娈靛吋瀹瑰巻鍙叉暟鎹苟鍥哄畾鍐?0
        yuangongxinzi.setQingjiakouxin(0D);
        yuangongxinzi.setWeiqiandaotianshu(((Number) side.get("weiqiandaotianshu")).intValue());
        yuangongxinzi.setWeiqiandaokouxin(((Number) side.get("weiqiandaokouxin")).doubleValue());
        yuangongxinzi.setJiaqikouxin(((Number) side.get("jiaqikouxin")).doubleValue());

        yuangongxinzi.setJixiaojiangjin(0D);
        double gangweibutie = yuangongxinzi.getGangweibutie() == null ? 0D : yuangongxinzi.getGangweibutie();
        double koukuanjine = yuangongxinzi.getKoukuanjine() == null ? 0D : yuangongxinzi.getKoukuanjine();
        double weiqiandaokouxin = yuangongxinzi.getWeiqiandaokouxin() == null ? 0D : yuangongxinzi.getWeiqiandaokouxin();
        double jiaqikouxin = yuangongxinzi.getJiaqikouxin() == null ? 0D : yuangongxinzi.getJiaqikouxin();
        // 璇峰亣鎵ｆ鎸夎鍋囧ぉ鏁?100锛屼粠瀹炲彂宸ヨ祫涓澶栨墸鍑?
        double shifagongzi = round2(jibengongzi + jiabangongzi + gangweibutie + weiqiandaokouxin + jiaqikouxin - koukuanjine);
        yuangongxinzi.setShifagongzi(shifagongzi);
    }

    private int normalizeAllPerformanceBonusAndSalary(boolean forceRecalcSalary) {
        List<YuangongxinziEntity> list = yuangongxinziService.list(new QueryWrapper<YuangongxinziEntity>().orderByAsc("id"));
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int updated = 0;
        for (YuangongxinziEntity item : list) {
            if (normalizePerformanceBonusAndSalary(item, forceRecalcSalary)) {
                yuangongxinziService.updateById(item);
                updated++;
            }
        }
        return updated;
    }

    private boolean normalizePerformanceBonusAndSalary(YuangongxinziEntity item, boolean forceRecalcSalary) {
        if (item == null) {
            return false;
        }
        boolean changed = false;
        double jixiao = item.getJixiaojiangjin() == null ? 0D : item.getJixiaojiangjin();
        if (Math.abs(jixiao) > 0.000001D) {
            item.setJixiaojiangjin(0D);
            changed = true;
        } else if (item.getJixiaojiangjin() == null) {
            item.setJixiaojiangjin(0D);
            changed = true;
        }

        double targetShifa = calculateShifaWithoutPerformance(item);
        double currentShifa = item.getShifagongzi() == null ? 0D : item.getShifagongzi();
        if (forceRecalcSalary || item.getShifagongzi() == null || Math.abs(currentShifa - targetShifa) > 0.009D) {
            item.setShifagongzi(targetShifa);
            changed = true;
        }
        return changed;
    }

    private double calculateShifaWithoutPerformance(YuangongxinziEntity item) {
        double jibengongzi = item.getJibengongzi() == null ? 0D : item.getJibengongzi();
        double jiabangongzi = item.getJiabangongzi() == null ? 0D : item.getJiabangongzi();
        double gangweibutie = item.getGangweibutie() == null ? 0D : item.getGangweibutie();
        double weiqiandaokouxin = item.getWeiqiandaokouxin() == null ? 0D : item.getWeiqiandaokouxin();
        double jiaqikouxin = item.getJiaqikouxin() == null ? 0D : item.getJiaqikouxin();
        double koukuanjine = item.getKoukuanjine() == null ? 0D : item.getKoukuanjine();
        return round2(jibengongzi + jiabangongzi + gangweibutie + weiqiandaokouxin + jiaqikouxin - koukuanjine);
    }

    /**
     * 涓庝繚瀛樻椂涓€鑷寸殑钖祫渚ц绠楋紙璇峰亣/鏈鍒?鍛ㄦ湯鍋囨湡鎵ｈ柂銆佸姞鐝伐璧勭瓑锛夛紝渚涘墠绔瑙堛€?
     */
    @RequestMapping("/salaryAssistant")
    public R salaryAssistant(@RequestParam String gonghao,
                             @RequestParam String dengjiriqi,
                             @RequestParam(required = false) Double jibengongzi) {
        if (StringUtils.isBlank(gonghao) || StringUtils.isBlank(dengjiriqi)) {
            return R.error("工号与登记日期不能为空");
        }
        try {
            Date refDate = new SimpleDateFormat("yyyy-MM-dd").parse(dengjiriqi.trim());
            double jb = jibengongzi == null ? 0D : jibengongzi;
            return R.ok().put("data", buildSalarySideComputation(gonghao.trim(), refDate, jb));
        } catch (ParseException e) {
            return R.error("鐧昏鏃ユ湡鏍煎紡椤讳负 yyyy-MM-dd");
        }
    }

    /**
     * 鎵归噺閲嶇畻锛氭寜褰撳墠瑙勫垯閲嶇畻鍏ㄩ儴鍛樺伐钖祫骞惰惤搴撱€?
     * 瑙勫垯鍖呮嫭锛氳鍋囨墸钖?0锛堜笅绾匡級銆佹湭绛惧埌鎵ｈ柂銆佸亣鏈熸墸钖€佸疄鍙戝伐璧勭瓑銆?
     */
    @RequestMapping("/recalculateAll")
    @Transactional
    @SysLog("批量重算员工薪资")
    public R recalculateAll() {
        List<YuangongxinziEntity> list = yuangongxinziService.list(new QueryWrapper<YuangongxinziEntity>().orderByAsc("id"));
        if (list == null || list.isEmpty()) {
            return R.ok().put("recalculated", 0).put("message", "鏃犺柂璧勮褰曞彲閲嶇畻");
        }
        int updated = 0;
        for (YuangongxinziEntity item : list) {
            if (item == null) {
                continue;
            }
            if (!StringUtils.isBlank(item.getGonghao())) {
                fillAutoSalaryFields(item);
            } else {
                item.setJixiaojiangjin(0D);
                item.setShifagongzi(calculateShifaWithoutPerformance(item));
            }
            yuangongxinziService.updateById(item);
            updated++;
        }
        performanceNormalizedAtRuntime = true;
        return R.ok().put("recalculated", updated);
    }

    private Map<String, Object> buildSalarySideComputation(String gonghao, Date refDate, double jibengongzi) {
        Map<String, Object> out = new HashMap<>();
        Date monthStart = getMonthStart(refDate);
        Date monthEnd = getMonthEnd(refDate);

        QueryWrapper<YuangongkaoqinEntity> kaoqinWrapper = new QueryWrapper<>();
        kaoqinWrapper.eq("gonghao", gonghao)
                .ge("dengjiriqi", monthStart)
                .le("dengjiriqi", monthEnd);
        int monthJiabancishu = yuangongkaoqinService.list(kaoqinWrapper).stream()
                .map(item -> item.getJiabancishu() == null ? 0 : item.getJiabancishu())
                .reduce(0, Integer::sum);
        double monthJiabanshichang = calcMonthJiabanshichang(gonghao, monthStart, monthEnd);

        List<QingjiashenqingEntity> allQingjia = qingjiashenqingService.list(
                new QueryWrapper<QingjiashenqingEntity>().eq("gonghao", gonghao).eq("sfsh", "是"));
        List<QingjiashenqingEntity> qingjiaList = allQingjia.stream()
                .filter(q -> leaveOverlapsMonth(q, monthStart, monthEnd))
                .collect(Collectors.toList());
        double monthQingjiatianshu = qingjiaList.stream()
                .map(item -> item.getQingjiatianshu() == null ? 0 : item.getQingjiatianshu())
                .reduce(0, Integer::sum);

        QueryWrapper<YuangongEntity> yuangongWrapper = new QueryWrapper<>();
        yuangongWrapper.eq("gonghao", gonghao).last("limit 1");
        YuangongEntity yuangong = yuangongService.getOne(yuangongWrapper, false);
        double baseQingjiatianshu = (yuangong != null && yuangong.getQingjiatianshu() != null) ? yuangong.getQingjiatianshu() : 0D;
        int totalQingjiatianshu = (int) Math.round(monthQingjiatianshu + baseQingjiatianshu);

        QueryWrapper<YuangongqiandaoEntity> qiandaoAll = new QueryWrapper<>();
        qiandaoAll.eq("gonghao", gonghao)
                .ge("qiandaoshijian", monthStart)
                .le("qiandaoshijian", monthEnd)
                .orderByAsc("qiandaoshijian");
        List<YuangongqiandaoEntity> qiandaoList = yuangongqiandaoService.list(qiandaoAll);

        SimpleDateFormat dayFmt = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> signInDays = new HashSet<>();
        Set<String> anyQiandaoDays = new HashSet<>();
        for (YuangongqiandaoEntity e : qiandaoList) {
            if (e.getQiandaoshijian() == null) {
                continue;
            }
            String dk = dayFmt.format(e.getQiandaoshijian());
            anyQiandaoDays.add(dk);
            String typ = StringUtils.trimToEmpty(e.getQiandaodidian());
            if ("签到".equals(typ)) {
                signInDays.add(dk);
            }
        }
        Set<String> leaveDays = new HashSet<>();
        for (QingjiashenqingEntity q : qingjiaList) {
            collectLeaveDaysInMonth(q, monthStart, monthEnd, leaveDays, dayFmt);
        }

        Date attendanceCountEnd = getCompletedAttendanceEnd(monthEnd);
        int weiqiandaoTianshu = countWeekdaysWithoutSignNoLeave(monthStart, attendanceCountEnd, signInDays, leaveDays);
        int weekendAbsent = countWeekendNoAttendance(monthStart, attendanceCountEnd, anyQiandaoDays, leaveDays);

        double hourly = jibengongzi <= 0D ? 0D : jibengongzi / 116D;
        double leaveDaily = jibengongzi <= 0D ? 0D : jibengongzi / 21.75D;
        double finalJiabanshichang = monthJiabanshichang > 0D ? monthJiabanshichang : monthJiabancishu;
        double jiabangongzi = round2(finalJiabanshichang * hourly);

        // 未签到扣薪规则：基本工资 / 21.75 * 未签到天数 * -1.5（按日工资 1.5 倍惩罚扣薪）
        double weiqiandaokouxin = round2(weiqiandaoTianshu * leaveDaily * -1.5D);
        // 鏂板彛寰勶細鍋囨湡鎵ｈ柂 = 鍩烘湰钖祫 / 21.75 脳 (-璇峰亣澶╂暟)
        double jiaqikouxin = round2(totalQingjiatianshu * -leaveDaily);

        out.put("qingjiatianshu", totalQingjiatianshu);
        out.put("jiabanshichang", monthJiabanshichang);
        out.put("jiabancishu", monthJiabancishu);
        out.put("jiabangongzi", jiabangongzi);
        // 璇峰亣鎵ｈ柂瀛楁宸蹭笅绾匡紝杩斿洖 0 鍏煎鏃у墠绔?鍘嗗彶璋冪敤
        out.put("qingjiakouxin", 0D);
        out.put("weiqiandaotianshu", weiqiandaoTianshu);
        out.put("weiqiandaokouxin", weiqiandaokouxin);
        out.put("jiaqikouxin", jiaqikouxin);
        return out;
    }

    private boolean leaveOverlapsMonth(QingjiashenqingEntity q, Date monthStart, Date monthEnd) {
        if (q == null || q.getQingjiashijian() == null) {
            return false;
        }
        Date a = q.getQingjiashijian();
        Date b = q.getJieshushijian() != null ? q.getJieshushijian() : q.getQingjiashijian();
        return !a.after(monthEnd) && !b.before(monthStart);
    }

    private void collectLeaveDaysInMonth(QingjiashenqingEntity q, Date monthStart, Date monthEnd, Set<String> out, SimpleDateFormat df) {
        if (q.getQingjiashijian() == null) {
            return;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(q.getQingjiashijian());
        stripToMidnight(c);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(q.getJieshushijian() != null ? q.getJieshushijian() : q.getQingjiashijian());
        stripToMidnight(endCal);
        Calendar ms = Calendar.getInstance();
        ms.setTime(monthStart);
        stripToMidnight(ms);
        Calendar me = Calendar.getInstance();
        me.setTime(monthEnd);
        stripToMidnight(me);
        if (c.before(ms)) {
            c.setTime(ms.getTime());
        }
        if (endCal.after(me)) {
            endCal.setTime(me.getTime());
        }
        while (!c.after(endCal)) {
            out.add(df.format(c.getTime()));
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void stripToMidnight(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    private Date getCompletedAttendanceEnd(Date monthEnd) {
        Calendar yesterdayEnd = Calendar.getInstance();
        stripToMidnight(yesterdayEnd);
        yesterdayEnd.add(Calendar.DAY_OF_MONTH, -1);
        yesterdayEnd.set(Calendar.HOUR_OF_DAY, 23);
        yesterdayEnd.set(Calendar.MINUTE, 59);
        yesterdayEnd.set(Calendar.SECOND, 59);
        yesterdayEnd.set(Calendar.MILLISECOND, 999);
        Date completedEnd = yesterdayEnd.getTime();
        return monthEnd.before(completedEnd) ? monthEnd : completedEnd;
    }

    private int countWeekdaysWithoutSignNoLeave(Date monthStart, Date monthEnd, Set<String> signInDays, Set<String> leaveDays) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int cnt = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(monthStart);
        stripToMidnight(cal);
        Calendar end = Calendar.getInstance();
        end.setTime(monthEnd);
        stripToMidnight(end);
        while (!cal.after(end)) {
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            if (dow != Calendar.SUNDAY && dow != Calendar.MONDAY) {
                String key = df.format(cal.getTime());
                if (!signInDays.contains(key) && !leaveDays.contains(key)) {
                    cnt++;
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return cnt;
    }

    private int countWeekendNoAttendance(Date monthStart, Date monthEnd, Set<String> anyQiandaoDays, Set<String> leaveDays) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int cnt = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(monthStart);
        stripToMidnight(cal);
        Calendar end = Calendar.getInstance();
        end.setTime(monthEnd);
        stripToMidnight(end);
        while (!cal.after(end)) {
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
                String key = df.format(cal.getTime());
                if (!anyQiandaoDays.contains(key) && !leaveDays.contains(key)) {
                    cnt++;
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return cnt;
    }

    private double calcMonthJiabanshichang(String gonghao, Date monthStart, Date monthEnd) {
        QueryWrapper<YuangongqiandaoEntity> qiandaoWrapper = new QueryWrapper<>();
        qiandaoWrapper.eq("gonghao", gonghao)
                .ge("qiandaoshijian", monthStart)
                .le("qiandaoshijian", monthEnd)
                .orderByAsc("qiandaoshijian");
        List<YuangongqiandaoEntity> qiandaoList = yuangongqiandaoService.list(qiandaoWrapper);
        Date start = null;
        long totalMinutes = 0L;
        for (YuangongqiandaoEntity item : qiandaoList) {
            if (item.getQiandaoshijian() == null || StringUtils.isBlank(item.getQiandaodidian())) {
                continue;
            }
            String type = item.getQiandaodidian().trim();
            if ("加班开始".equals(type)) {
                start = item.getQiandaoshijian();
            } else if ("加班结束".equals(type) && start != null && !item.getQiandaoshijian().before(start)) {
                totalMinutes += (item.getQiandaoshijian().getTime() - start.getTime()) / 60000L;
                start = null;
            }
        }
        return round2(totalMinutes / 60D);
    }

    private Date getMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private double round2(double value) {
        return Math.round(value * 100D) / 100D;
    }

    /**
     * 瀹℃牳
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核员工薪资")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam(required = false, defaultValue = "") String shhf){
        List<YuangongxinziEntity> list = new ArrayList<YuangongxinziEntity>();
        for(Long id : ids) {
            YuangongxinziEntity yuangongxinzi = yuangongxinziService.getById(id);
            yuangongxinzi.setSfsh(sfsh);
            yuangongxinzi.setShhf(shhf);
            list.add(yuangongxinzi);
        }
        yuangongxinziService.updateBatchById(list);
        return R.ok();
    }

    /**
     * 鏀粯鐘舵€佹壒閲忔洿鏂?     */
    @RequestMapping("/zfBatch")
    @Transactional
    @SysLog("支付员工薪资")
    public R zfBatch(@RequestBody Long[] ids, @RequestParam String ispay){
        List<YuangongxinziEntity> list = new ArrayList<YuangongxinziEntity>();
        for(Long id : ids) {
            YuangongxinziEntity yuangongxinzi = yuangongxinziService.getById(id);
            if(yuangongxinzi != null) {
                yuangongxinzi.setIspay(ispay);
                list.add(yuangongxinzi);
            }
        }
        if(!list.isEmpty()) {
            yuangongxinziService.updateBatchById(list);
        }
        return R.ok();
    }




    /**
     * 鍒犻櫎
     */
    @RequestMapping("/delete")
    @SysLog("删除员工薪资")
    public R delete(@RequestBody Long[] ids){
        yuangongxinziService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:鍚?


    /**
     * 锛堟寜鍊肩粺璁★級
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, @RequestParam(required = false, defaultValue = "鎬诲拰") String func, HttpServletRequest request) throws IOException {
        //璇诲彇鏂囦欢锛屽鏋滄枃浠跺瓨鍦紝鍒欎紭鍏堣繑鍥炴枃浠跺唴瀹?
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangongxinzi_" + xColumnName + "_" + yColumnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        //鏋勫缓鏌ヨ缁熻鏉′欢
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("method", func);
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                if (order.equals("asc")) {
                    ew.orderByAsc(orderType.equals("x") ? xColumnName : yColumnName);
                } else {
                    ew.orderByDesc(orderType.equals("x") ? xColumnName :yColumnName);
                }
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 澶勭悊鑼冨洿鏌ヨ锛氬鏋滃垪鍚嶅寘鍚€楀彿锛岃〃绀烘槸鑼冨洿鏌ヨ
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 绗竴涓垪鍚嶄娇鐢?>= 鏉′欢
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 绗簩涓垪鍚嶄娇鐢?<= 鏉′欢
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 鏅€氱瓑鍊兼煡璇?
                    ew.eq(column, value);
                }
            }
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yuangong")) {
            ew.eq("gonghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("renshiguanliyuan")) {
            ew.eq("guanlizhanghao", (String)request.getSession().getAttribute("username"));
        }

        //鑾峰彇缁撴灉
        List<Map<String, Object>> result = yuangongxinziService.selectValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * 锛堟寜鍊肩粺璁?澶?锛?
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request)  throws IOException {
        //璇诲彇鏂囦欢锛屽鏋滄枃浠跺瓨鍦紝鍒欎紭鍏堣繑鍥炴枃浠跺唴瀹?
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangongxinzi_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        //鏋勫缓鏌ヨ缁熻鏉′欢
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                if (order.equals("asc")) {
                    ew.orderByAsc(orderType.equals("x") ? Arrays.asList(xColumnName) : Arrays.asList(yColumnNames));
                } else {
                    ew.orderByDesc(orderType.equals("x") ? Arrays.asList(xColumnName) : Arrays.asList(yColumnNames));
                }
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 澶勭悊鑼冨洿鏌ヨ锛氬鏋滃垪鍚嶅寘鍚€楀彿锛岃〃绀烘槸鑼冨洿鏌ヨ
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 绗竴涓垪鍚嶄娇鐢?>= 鏉′欢
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 绗簩涓垪鍚嶄娇鐢?<= 鏉′欢
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 鏅€氱瓑鍊兼煡璇?
                    ew.eq(column, value);
                }
            }
        }
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yuangong")) {
            ew.eq("gonghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("renshiguanliyuan")) {
            ew.eq("guanlizhanghao", (String)request.getSession().getAttribute("username"));
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = yuangongxinziService.selectValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 锛堟寜鍊肩粺璁★級鏃堕棿缁熻绫诲瀷
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, @RequestParam(required = false, defaultValue = "鎬诲拰") String func, HttpServletRequest request) throws IOException {
        //璇诲彇鏂囦欢锛屽鏋滄枃浠跺瓨鍦紝鍒欎紭鍏堣繑鍥炴枃浠跺唴瀹?
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangongxinzi_" + xColumnName + "_" + yColumnName + "_"+timeStatType+".json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("timeStatType", timeStatType);
        params.put("method", func);
        //鏋勫缓鏌ヨ缁熻鏉′欢
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                if (order.equals("asc")) {
                    ew.orderByAsc(orderType.equals("x") ? xColumnName : yColumnName);
                } else {
                    ew.orderByDesc(orderType.equals("x") ? xColumnName :yColumnName);
                }
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 澶勭悊鑼冨洿鏌ヨ锛氬鏋滃垪鍚嶅寘鍚€楀彿锛岃〃绀烘槸鑼冨洿鏌ヨ
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 绗竴涓垪鍚嶄娇鐢?>= 鏉′欢
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 绗簩涓垪鍚嶄娇鐢?<= 鏉′欢
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 鏅€氱瓑鍊兼煡璇?
                    ew.eq(column, value);
                }
            }
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yuangong")) {
            ew.eq("gonghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("renshiguanliyuan")) {
            ew.eq("guanlizhanghao", (String)request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = yuangongxinziService.selectTimeStatValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * 锛堟寜鍊肩粺璁★級鏃堕棿缁熻绫诲瀷(澶?
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam String yColumnNameMul, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request) throws IOException
    {
        //璇诲彇鏂囦欢锛屽鏋滄枃浠跺瓨鍦紝鍒欎紭鍏堣繑鍥炴枃浠跺唴瀹?
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangongxinzi_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + ".json");
        if (java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        //鏋勫缓鏌ヨ缁熻鏉′欢
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                if (order.equals("asc")) {
                    ew.orderByAsc(orderType.equals("x") ? Arrays.asList(xColumnName) : Arrays.asList(yColumnNames));
                } else {
                    ew.orderByDesc(orderType.equals("x") ? Arrays.asList(xColumnName) : Arrays.asList(yColumnNames));
                }
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 澶勭悊鑼冨洿鏌ヨ锛氬鏋滃垪鍚嶅寘鍚€楀彿锛岃〃绀烘槸鑼冨洿鏌ヨ
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 绗竴涓垪鍚嶄娇鐢?>= 鏉′欢
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 绗簩涓垪鍚嶄娇鐢?<= 鏉′欢
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 鏅€氱瓑鍊兼煡璇?
                    ew.eq(column, value);
                }
            }
        }
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yuangong")) {
            ew.eq("gonghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("renshiguanliyuan")) {
            ew.eq("guanlizhanghao", (String)request.getSession().getAttribute("username"));
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = yuangongxinziService.selectTimeStatValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 鍒嗙粍缁熻
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request) throws IOException {
        //璇诲彇鏂囦欢锛屽鏋滄枃浠跺瓨鍦紝鍒欎紭鍏堣繑鍥炴枃浠跺唴瀹?
        java.nio.file.Path path = java.nio.file.Paths.get("group_yuangongxinzi_" + columnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)){
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        //鏋勫缓鏌ヨ缁熻鏉′欢
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 澶勭悊鑼冨洿鏌ヨ锛氬鏋滃垪鍚嶅寘鍚€楀彿锛岃〃绀烘槸鑼冨洿鏌ヨ
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 绗竴涓垪鍚嶄娇鐢?>= 鏉′欢
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 绗簩涓垪鍚嶄娇鐢?<= 鏉′欢
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 鏅€氱瓑鍊兼煡璇?
                    ew.eq(column, value);
                }
            }
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yuangong")) {
            ew.eq("gonghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("renshiguanliyuan")) {
            ew.eq("guanlizhanghao", (String)request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = yuangongxinziService.selectGroup(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }




    /**
     * 鎬绘暟閲?
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,YuangongxinziEntity yuangongxinzi, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yuangong")) {
            yuangongxinzi.setGonghao((String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("renshiguanliyuan")) {
            yuangongxinzi.setGuanlizhanghao((String)request.getSession().getAttribute("username"));
        }
        QueryWrapper<YuangongxinziEntity> ew = new QueryWrapper<YuangongxinziEntity>();
        long count = yuangongxinziService.count(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongxinzi), params), params));
        return R.ok().put("data", count);
    }

}

