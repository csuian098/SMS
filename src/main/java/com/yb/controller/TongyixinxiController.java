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

import com.yb.entity.TongyixinxiEntity;
import com.yb.entity.view.TongyixinxiView;

import com.yb.service.TongyixinxiService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 同意信息
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/tongyixinxi")
public class TongyixinxiController {
    @Autowired
    private TongyixinxiService tongyixinxiService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongyixinxiEntity tongyixinxi,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			tongyixinxi.setGonghao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("renshiguanliyuan")) {
			tongyixinxi.setGuanlizhanghao((String)request.getSession().getAttribute("username"));
		}
        //设置查询条件
        QueryWrapper<TongyixinxiEntity> ew = new QueryWrapper<TongyixinxiEntity>();


        //查询结果
		PageUtils page = tongyixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongyixinxi), params), params));
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
    public R list(@RequestParam Map<String, Object> params,TongyixinxiEntity tongyixinxi,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date tongyishijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date tongyishijianend,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<TongyixinxiEntity> ew = new QueryWrapper<TongyixinxiEntity>();
        if(tongyishijianstart!=null) ew.ge("tongyishijian", tongyishijianstart);
        if(tongyishijianend!=null) ew.le("tongyishijian", tongyishijianend);

        //查询结果
		PageUtils page = tongyixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongyixinxi), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( TongyixinxiEntity tongyixinxi){
       	QueryWrapper<TongyixinxiEntity> ew = new QueryWrapper<TongyixinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongyixinxi, "tongyixinxi"));
        return R.ok().put("data", tongyixinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TongyixinxiEntity tongyixinxi){
        QueryWrapper< TongyixinxiEntity> ew = new QueryWrapper< TongyixinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongyixinxi, "tongyixinxi"));
		TongyixinxiView tongyixinxiView =  tongyixinxiService.selectView(ew);
		return R.ok("查询同意信息成功").put("data", tongyixinxiView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TongyixinxiEntity tongyixinxi = tongyixinxiService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(tongyixinxi,deSens);
        return R.ok().put("data", tongyixinxi);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TongyixinxiEntity tongyixinxi = tongyixinxiService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(tongyixinxi,deSens);
        return R.ok().put("data", tongyixinxi);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增同意信息")
    public R save(@RequestBody TongyixinxiEntity tongyixinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(tongyixinxi);
        tongyixinxiService.save(tongyixinxi);
        return R.ok().put("data",tongyixinxi.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增同意信息")
    @RequestMapping("/add")
    public R add(@RequestBody TongyixinxiEntity tongyixinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(tongyixinxi);
        tongyixinxiService.save(tongyixinxi);
        return R.ok().put("data",tongyixinxi.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改同意信息")
    public R update(@RequestBody TongyixinxiEntity tongyixinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(tongyixinxi);
        //全部更新
        tongyixinxiService.updateById(tongyixinxi);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除同意信息")
    public R delete(@RequestBody Long[] ids){
        tongyixinxiService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否




}
