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

import com.yb.entity.YuangongqiandaoEntity;
import com.yb.entity.view.YuangongqiandaoView;

import com.yb.service.YuangongqiandaoService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 员工签到
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/yuangongqiandao")
public class YuangongqiandaoController {
    @Autowired
    private YuangongqiandaoService yuangongqiandaoService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YuangongqiandaoEntity yuangongqiandao,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			yuangongqiandao.setGonghao((String)request.getSession().getAttribute("username"));
		}
        //设置查询条件
        QueryWrapper<YuangongqiandaoEntity> ew = new QueryWrapper<YuangongqiandaoEntity>();


        //查询结果
		PageUtils page = yuangongqiandaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongqiandao), params), params));
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
    public R list(@RequestParam Map<String, Object> params,YuangongqiandaoEntity yuangongqiandao,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date qiandaoshijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date qiandaoshijianend,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<YuangongqiandaoEntity> ew = new QueryWrapper<YuangongqiandaoEntity>();
        if(qiandaoshijianstart!=null) ew.ge("qiandaoshijian", qiandaoshijianstart);
        if(qiandaoshijianend!=null) ew.le("qiandaoshijian", qiandaoshijianend);

        //查询结果
		PageUtils page = yuangongqiandaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongqiandao), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YuangongqiandaoEntity yuangongqiandao){
       	QueryWrapper<YuangongqiandaoEntity> ew = new QueryWrapper<YuangongqiandaoEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yuangongqiandao, "yuangongqiandao"));
        return R.ok().put("data", yuangongqiandaoService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YuangongqiandaoEntity yuangongqiandao){
        QueryWrapper< YuangongqiandaoEntity> ew = new QueryWrapper< YuangongqiandaoEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yuangongqiandao, "yuangongqiandao"));
		YuangongqiandaoView yuangongqiandaoView =  yuangongqiandaoService.selectView(ew);
		return R.ok("查询员工签到成功").put("data", yuangongqiandaoView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YuangongqiandaoEntity yuangongqiandao = yuangongqiandaoService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(yuangongqiandao,deSens);
        return R.ok().put("data", yuangongqiandao);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YuangongqiandaoEntity yuangongqiandao = yuangongqiandaoService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(yuangongqiandao,deSens);
        return R.ok().put("data", yuangongqiandao);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增员工签到")
    public R save(@RequestBody YuangongqiandaoEntity yuangongqiandao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongqiandao);
        String validateMsg = validateSignAction(yuangongqiandao);
        if (StringUtils.isNotBlank(validateMsg)) {
            return R.error(validateMsg);
        }
        yuangongqiandaoService.save(yuangongqiandao);
        return R.ok().put("data",yuangongqiandao.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增员工签到")
    @RequestMapping("/add")
    public R add(@RequestBody YuangongqiandaoEntity yuangongqiandao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongqiandao);
        String validateMsg = validateSignAction(yuangongqiandao);
        if (StringUtils.isNotBlank(validateMsg)) {
            return R.error(validateMsg);
        }
        yuangongqiandaoService.save(yuangongqiandao);
        return R.ok().put("data",yuangongqiandao.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改员工签到")
    public R update(@RequestBody YuangongqiandaoEntity yuangongqiandao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongqiandao);
        //全部更新
        yuangongqiandaoService.updateById(yuangongqiandao);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除员工签到")
    public R delete(@RequestBody Long[] ids){
        yuangongqiandaoService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否

    private String validateSignAction(YuangongqiandaoEntity entity) {
        if (entity == null || StringUtils.isBlank(entity.getGonghao())) {
            return "工号不能为空，无法保存打卡记录";
        }
        String type = entity.getQiandaodidian() == null ? "" : entity.getQiandaodidian().trim();
        List<String> allowed = Arrays.asList("签到", "签退", "加班开始", "加班结束");
        if (!allowed.contains(type)) {
            return "打卡类型不正确，请重新选择";
        }
        Date signTime = entity.getQiandaoshijian() == null ? new Date() : entity.getQiandaoshijian();
        entity.setQiandaoshijian(signTime);

        Calendar c = Calendar.getInstance();
        c.setTime(signTime);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dayStart = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        Date dayEnd = c.getTime();

        QueryWrapper<YuangongqiandaoEntity> ew = new QueryWrapper<>();
        ew.eq("gonghao", entity.getGonghao())
          .ge("qiandaoshijian", dayStart)
          .le("qiandaoshijian", dayEnd)
          .orderByAsc("qiandaoshijian");
        List<YuangongqiandaoEntity> dayList = yuangongqiandaoService.list(ew);

        boolean hasSignIn = dayList.stream().anyMatch(i -> "签到".equals(i.getQiandaodidian()));
        boolean hasSignOut = dayList.stream().anyMatch(i -> "签退".equals(i.getQiandaodidian()));
        boolean hasOvertimeStart = dayList.stream().anyMatch(i -> "加班开始".equals(i.getQiandaodidian()));
        boolean hasOvertimeEnd = dayList.stream().anyMatch(i -> "加班结束".equals(i.getQiandaodidian()));

        if ("签到".equals(type)) {
            if (hasSignIn) return "该日期已签到，不能重复签到";
            if (hasSignOut || hasOvertimeStart || hasOvertimeEnd) return "该日期已有签退或加班记录，不能再补签到";
            return null;
        }
        if ("签退".equals(type)) {
            if (!hasSignIn) return "请先完成签到，再进行签退";
            if (hasSignOut) return "该日期已签退，不能重复签退";
            if (hasOvertimeStart || hasOvertimeEnd) return "该日期已进入加班流程，不能再签退";
            return null;
        }
        if ("加班开始".equals(type)) {
            if (!hasSignIn) return "请先完成签到，再开始加班";
            if (!hasSignOut) return "请先完成签退，再开始加班";
            if (hasOvertimeStart) return "该日期已加班开始，不能重复操作";
            if (hasOvertimeEnd) return "该日期已加班结束，不能再开始加班";
            return null;
        }
        if ("加班结束".equals(type)) {
            if (!hasOvertimeStart) return "请先进行加班开始，再加班结束";
            if (hasOvertimeEnd) return "该日期已加班结束，不能重复操作";
        }
        return null;
    }



}
