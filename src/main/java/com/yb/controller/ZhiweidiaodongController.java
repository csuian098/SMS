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

import com.yb.entity.ZhiweidiaodongEntity;
import com.yb.entity.view.ZhiweidiaodongView;

import com.yb.service.ZhiweidiaodongService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 职位调动
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/zhiweidiaodong")
public class ZhiweidiaodongController {
    @Autowired
    private ZhiweidiaodongService zhiweidiaodongService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ZhiweidiaodongEntity zhiweidiaodong,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			zhiweidiaodong.setGonghao((String)request.getSession().getAttribute("username"));
		}
        //设置查询条件
        QueryWrapper<ZhiweidiaodongEntity> ew = new QueryWrapper<ZhiweidiaodongEntity>();


        //查询结果
		PageUtils page = zhiweidiaodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweidiaodong), params), params));
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
    public R list(@RequestParam Map<String, Object> params,ZhiweidiaodongEntity zhiweidiaodong,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date biandongriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date biandongriqiend,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<ZhiweidiaodongEntity> ew = new QueryWrapper<ZhiweidiaodongEntity>();
        if(biandongriqistart!=null) ew.ge("biandongriqi", biandongriqistart);
        if(biandongriqiend!=null) ew.le("biandongriqi", biandongriqiend);

        //查询结果
		PageUtils page = zhiweidiaodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweidiaodong), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ZhiweidiaodongEntity zhiweidiaodong){
       	QueryWrapper<ZhiweidiaodongEntity> ew = new QueryWrapper<ZhiweidiaodongEntity>();
      	ew.allEq(MPUtil.allEQMapPre( zhiweidiaodong, "zhiweidiaodong"));
        return R.ok().put("data", zhiweidiaodongService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ZhiweidiaodongEntity zhiweidiaodong){
        QueryWrapper< ZhiweidiaodongEntity> ew = new QueryWrapper< ZhiweidiaodongEntity>();
 		ew.allEq(MPUtil.allEQMapPre( zhiweidiaodong, "zhiweidiaodong"));
		ZhiweidiaodongView zhiweidiaodongView =  zhiweidiaodongService.selectView(ew);
		return R.ok("查询职位调动成功").put("data", zhiweidiaodongView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ZhiweidiaodongEntity zhiweidiaodong = zhiweidiaodongService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(zhiweidiaodong,deSens);
        return R.ok().put("data", zhiweidiaodong);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ZhiweidiaodongEntity zhiweidiaodong = zhiweidiaodongService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(zhiweidiaodong,deSens);
        return R.ok().put("data", zhiweidiaodong);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增职位调动")
    public R save(@RequestBody ZhiweidiaodongEntity zhiweidiaodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweidiaodong);
        zhiweidiaodongService.save(zhiweidiaodong);
        return R.ok().put("data",zhiweidiaodong.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增职位调动")
    @RequestMapping("/add")
    public R add(@RequestBody ZhiweidiaodongEntity zhiweidiaodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweidiaodong);
        zhiweidiaodongService.save(zhiweidiaodong);
        return R.ok().put("data",zhiweidiaodong.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改职位调动")
    public R update(@RequestBody ZhiweidiaodongEntity zhiweidiaodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweidiaodong);
        //全部更新
        zhiweidiaodongService.updateById(zhiweidiaodong);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除职位调动")
    public R delete(@RequestBody Long[] ids){
        zhiweidiaodongService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否




}
