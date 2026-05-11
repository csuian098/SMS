package com.yb.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.lang.*;
import java.math.*;
import com.yb.utils.*;
import java.util.stream.Collectors;
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

import com.yb.entity.QingjiashenqingEntity;
import com.yb.entity.view.QingjiashenqingView;

import com.yb.service.QingjiashenqingService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 请假申请
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
@RestController
@RequestMapping("/qingjiashenqing")
public class QingjiashenqingController {
    @Autowired
    private QingjiashenqingService qingjiashenqingService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,QingjiashenqingEntity qingjiashenqing,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			qingjiashenqing.setGonghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("renshiguanliyuan")) {
			qingjiashenqing.setGuanlizhanghao((String)request.getSession().getAttribute("username"));
		}
        //设置查询条件
        QueryWrapper<QingjiashenqingEntity> ew = new QueryWrapper<QingjiashenqingEntity>();


        //查询结果
		PageUtils page = qingjiashenqingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, qingjiashenqing), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,QingjiashenqingEntity qingjiashenqing,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date qingjiashijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date qingjiashijianend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date jieshushijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date jieshushijianend,
                @RequestParam(required = false) Double qingjiatianshustart,
                @RequestParam(required = false) Double qingjiatianshuend,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<QingjiashenqingEntity> ew = new QueryWrapper<QingjiashenqingEntity>();
        if(qingjiashijianstart!=null) ew.ge("qingjiashijian", qingjiashijianstart);
        if(qingjiashijianend!=null) ew.le("qingjiashijian", qingjiashijianend);
        if(jieshushijianstart!=null) ew.ge("jieshushijian", jieshushijianstart);
        if(jieshushijianend!=null) ew.le("jieshushijian", jieshushijianend);
        if(qingjiatianshustart!=null) ew.ge("qingjiatianshu", qingjiatianshustart);
        if(qingjiatianshuend!=null) ew.le("qingjiatianshu", qingjiatianshuend);

        //查询结果
		PageUtils page = qingjiashenqingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, qingjiashenqing), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( QingjiashenqingEntity qingjiashenqing){
       	QueryWrapper<QingjiashenqingEntity> ew = new QueryWrapper<QingjiashenqingEntity>();
      	ew.allEq(MPUtil.allEQMapPre( qingjiashenqing, "qingjiashenqing"));
        return R.ok().put("data", qingjiashenqingService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(QingjiashenqingEntity qingjiashenqing){
        QueryWrapper< QingjiashenqingEntity> ew = new QueryWrapper< QingjiashenqingEntity>();
 		ew.allEq(MPUtil.allEQMapPre( qingjiashenqing, "qingjiashenqing"));
		QingjiashenqingView qingjiashenqingView =  qingjiashenqingService.selectView(ew);
		return R.ok("查询请假申请成功").put("data", qingjiashenqingView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        QingjiashenqingEntity qingjiashenqing = qingjiashenqingService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(qingjiashenqing,deSens);
        return R.ok().put("data", qingjiashenqing);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        QingjiashenqingEntity qingjiashenqing = qingjiashenqingService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(qingjiashenqing,deSens);
        return R.ok().put("data", qingjiashenqing);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增请假申请")
    public R save(@RequestBody QingjiashenqingEntity qingjiashenqing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(qingjiashenqing);
        R leaveDayCheck = normalizeLeaveDays(qingjiashenqing);
        if (leaveDayCheck != null) {
            return leaveDayCheck;
        }
        R duplicateCheck = checkDuplicateLeave(qingjiashenqing, null);
        if (duplicateCheck != null) {
            return duplicateCheck;
        }
        qingjiashenqingService.save(qingjiashenqing);
        return R.ok().put("data",qingjiashenqing.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增请假申请")
    @RequestMapping("/add")
    public R add(@RequestBody QingjiashenqingEntity qingjiashenqing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(qingjiashenqing);
        R leaveDayCheck = normalizeLeaveDays(qingjiashenqing);
        if (leaveDayCheck != null) {
            return leaveDayCheck;
        }
        R duplicateCheck = checkDuplicateLeave(qingjiashenqing, null);
        if (duplicateCheck != null) {
            return duplicateCheck;
        }
        qingjiashenqingService.save(qingjiashenqing);
        return R.ok().put("data",qingjiashenqing.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改请假申请")
    public R update(@RequestBody QingjiashenqingEntity qingjiashenqing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(qingjiashenqing);
        R leaveDayCheck = normalizeLeaveDays(qingjiashenqing);
        if (leaveDayCheck != null) {
            return leaveDayCheck;
        }
        R duplicateCheck = checkDuplicateLeave(qingjiashenqing, qingjiashenqing.getId());
        if (duplicateCheck != null) {
            return duplicateCheck;
        }
        //全部更新
        qingjiashenqingService.updateById(qingjiashenqing);
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核请假申请")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<QingjiashenqingEntity> list = new ArrayList<QingjiashenqingEntity>();
        for(Long id : ids) {
            QingjiashenqingEntity qingjiashenqing = qingjiashenqingService.getById(id);
            qingjiashenqing.setSfsh(sfsh);
            qingjiashenqing.setShhf(shhf);
            list.add(qingjiashenqing);
        }
        qingjiashenqingService.updateBatchById(list);
        return R.ok();
    }




    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除请假申请")
    public R delete(@RequestBody Long[] ids){
        qingjiashenqingService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否




    private R normalizeLeaveDays(QingjiashenqingEntity qingjiashenqing) {
        if (StringUtils.isBlank(qingjiashenqing.getGonghao())) {
            return R.error("请先选择员工，工号不能为空");
        }
        if (qingjiashenqing.getQingjiashijian() == null || qingjiashenqing.getJieshushijian() == null) {
            return R.error("请假开始时间和结束时间不能为空，请完整选择请假时间");
        }
        if (!qingjiashenqing.getQingjiashijian().before(qingjiashenqing.getJieshushijian())) {
            return R.error("结束时间必须晚于请假开始时间，请重新选择");
        }
        int leaveDays = countLeaveWorkdays(qingjiashenqing.getQingjiashijian(), qingjiashenqing.getJieshushijian());
        if (leaveDays <= 0) {
            return R.error("请假时间未包含工作日，请重新选择请假时间");
        }
        qingjiashenqing.setQingjiatianshu(leaveDays);
        return null;
    }

    private int countLeaveWorkdays(Date start, Date end) {
        Calendar cur = Calendar.getInstance();
        cur.setTime(start);
        zeroTime(cur);
        Calendar last = Calendar.getInstance();
        last.setTime(end);
        zeroTime(last);
        int days = 0;
        while (!cur.after(last)) {
            if (!isSystemRestDay(cur)) {
                days++;
            }
            cur.add(Calendar.DATE, 1);
        }
        return days;
    }

    private boolean isSystemRestDay(Calendar calendar) {
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        return dow == Calendar.SUNDAY || dow == Calendar.MONDAY;
    }

    private void zeroTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private R checkDuplicateLeave(QingjiashenqingEntity qingjiashenqing, Long excludeId) {
        if (StringUtils.isBlank(qingjiashenqing.getGonghao())) {
            return R.error("请先选择员工，工号不能为空");
        }
        if (qingjiashenqing.getQingjiashijian() == null || qingjiashenqing.getJieshushijian() == null) {
            return R.error("请假开始时间和结束时间不能为空，请完整选择请假时间");
        }
        if (!qingjiashenqing.getQingjiashijian().before(qingjiashenqing.getJieshushijian())) {
            return R.error("结束时间必须晚于请假开始时间，请重新选择");
        }

        QueryWrapper<QingjiashenqingEntity> wrapper = new QueryWrapper<QingjiashenqingEntity>()
                .eq("gonghao", qingjiashenqing.getGonghao())
                .lt("qingjiashijian", qingjiashenqing.getJieshushijian())
                .gt("jieshushijian", qingjiashenqing.getQingjiashijian())
                .and(w -> w.isNull("sfsh").or().ne("sfsh", "否"));
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }

        List<QingjiashenqingEntity> overlapLeaves = qingjiashenqingService.list(wrapper);
        if (!overlapLeaves.isEmpty()) {
            boolean sameTime = overlapLeaves.stream().anyMatch(item ->
                    qingjiashenqing.getQingjiashijian().equals(item.getQingjiashijian())
                            && qingjiashenqing.getJieshushijian().equals(item.getJieshushijian()));
            if (sameTime) {
                return R.error("该时间段已有请假申请，不能重复提交；如需重新申请，请先由人事管理员将上一条审核为不通过");
            }
            return R.error("请假时间与已有申请重叠，不能重复提交；如需重新申请，请先由人事管理员将上一条审核为不通过");
        }
        return null;
    }

}
