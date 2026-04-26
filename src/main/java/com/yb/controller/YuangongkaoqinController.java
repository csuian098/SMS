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

import com.yb.entity.YuangongkaoqinEntity;
import com.yb.entity.view.YuangongkaoqinView;

import com.yb.service.YuangongkaoqinService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 员工考勤
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/yuangongkaoqin")
public class YuangongkaoqinController {
    @Autowired
    private YuangongkaoqinService yuangongkaoqinService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YuangongkaoqinEntity yuangongkaoqin,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqiend,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			yuangongkaoqin.setGonghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("renshiguanliyuan")) {
			yuangongkaoqin.setGuanlizhanghao((String)request.getSession().getAttribute("username"));
		}
        //设置查询条件
        QueryWrapper<YuangongkaoqinEntity> ew = new QueryWrapper<YuangongkaoqinEntity>();
        if(dengjiriqistart!=null) ew.ge("dengjiriqi", dengjiriqistart);
        if(dengjiriqiend!=null) ew.le("dengjiriqi", dengjiriqiend);


        //查询结果
		PageUtils page = yuangongkaoqinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongkaoqin), params), params));
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
    public R list(@RequestParam Map<String, Object> params,YuangongkaoqinEntity yuangongkaoqin,
                @RequestParam(required = false) Double zhengchangcishustart,
                @RequestParam(required = false) Double zhengchangcishuend,
                @RequestParam(required = false) Double chidaocishustart,
                @RequestParam(required = false) Double chidaocishuend,
                @RequestParam(required = false) Double zaotuicishustart,
                @RequestParam(required = false) Double zaotuicishuend,
                @RequestParam(required = false) Double chuchacishustart,
                @RequestParam(required = false) Double chuchacishuend,
                @RequestParam(required = false) Double jiabancishustart,
                @RequestParam(required = false) Double jiabancishuend,
                @RequestParam(required = false) Double qingjiacishustart,
                @RequestParam(required = false) Double qingjiacishuend,
                @RequestParam(required = false) Double kuanggongcishustart,
                @RequestParam(required = false) Double kuanggongcishuend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dengjiriqiend,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<YuangongkaoqinEntity> ew = new QueryWrapper<YuangongkaoqinEntity>();
        if(zhengchangcishustart!=null) ew.ge("zhengchangcishu", zhengchangcishustart);
        if(zhengchangcishuend!=null) ew.le("zhengchangcishu", zhengchangcishuend);
        if(chidaocishustart!=null) ew.ge("chidaocishu", chidaocishustart);
        if(chidaocishuend!=null) ew.le("chidaocishu", chidaocishuend);
        if(zaotuicishustart!=null) ew.ge("zaotuicishu", zaotuicishustart);
        if(zaotuicishuend!=null) ew.le("zaotuicishu", zaotuicishuend);
        if(chuchacishustart!=null) ew.ge("chuchacishu", chuchacishustart);
        if(chuchacishuend!=null) ew.le("chuchacishu", chuchacishuend);
        if(jiabancishustart!=null) ew.ge("jiabancishu", jiabancishustart);
        if(jiabancishuend!=null) ew.le("jiabancishu", jiabancishuend);
        if(qingjiacishustart!=null) ew.ge("qingjiacishu", qingjiacishustart);
        if(qingjiacishuend!=null) ew.le("qingjiacishu", qingjiacishuend);
        if(kuanggongcishustart!=null) ew.ge("kuanggongcishu", kuanggongcishustart);
        if(kuanggongcishuend!=null) ew.le("kuanggongcishu", kuanggongcishuend);
        if(dengjiriqistart!=null) ew.ge("dengjiriqi", dengjiriqistart);
        if(dengjiriqiend!=null) ew.le("dengjiriqi", dengjiriqiend);

        //查询结果
		PageUtils page = yuangongkaoqinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangongkaoqin), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YuangongkaoqinEntity yuangongkaoqin){
       	QueryWrapper<YuangongkaoqinEntity> ew = new QueryWrapper<YuangongkaoqinEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yuangongkaoqin, "yuangongkaoqin"));
        return R.ok().put("data", yuangongkaoqinService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YuangongkaoqinEntity yuangongkaoqin){
        QueryWrapper< YuangongkaoqinEntity> ew = new QueryWrapper< YuangongkaoqinEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yuangongkaoqin, "yuangongkaoqin"));
		YuangongkaoqinView yuangongkaoqinView =  yuangongkaoqinService.selectView(ew);
		return R.ok("查询员工考勤成功").put("data", yuangongkaoqinView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YuangongkaoqinEntity yuangongkaoqin = yuangongkaoqinService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(yuangongkaoqin,deSens);
        return R.ok().put("data", yuangongkaoqin);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YuangongkaoqinEntity yuangongkaoqin = yuangongkaoqinService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(yuangongkaoqin,deSens);
        return R.ok().put("data", yuangongkaoqin);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增员工考勤")
    public R save(@RequestBody YuangongkaoqinEntity yuangongkaoqin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongkaoqin);
        yuangongkaoqinService.save(yuangongkaoqin);
        return R.ok().put("data",yuangongkaoqin.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增员工考勤")
    @RequestMapping("/add")
    public R add(@RequestBody YuangongkaoqinEntity yuangongkaoqin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongkaoqin);
        yuangongkaoqinService.save(yuangongkaoqin);
        return R.ok().put("data",yuangongkaoqin.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改员工考勤")
    public R update(@RequestBody YuangongkaoqinEntity yuangongkaoqin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangongkaoqin);
        //全部更新
        yuangongkaoqinService.updateById(yuangongkaoqin);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除员工考勤")
    public R delete(@RequestBody Long[] ids){
        yuangongkaoqinService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否




}
