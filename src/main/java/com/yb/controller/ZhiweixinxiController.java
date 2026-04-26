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

import com.yb.entity.ZhiweixinxiEntity;
import com.yb.entity.view.ZhiweixinxiView;

import com.yb.service.ZhiweixinxiService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 职位信息
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/zhiweixinxi")
public class ZhiweixinxiController {
    @Autowired
    private ZhiweixinxiService zhiweixinxiService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ZhiweixinxiEntity zhiweixinxi,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<ZhiweixinxiEntity> ew = new QueryWrapper<ZhiweixinxiEntity>();


        //查询结果
		PageUtils page = zhiweixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweixinxi), params), params));
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
    public R list(@RequestParam Map<String, Object> params,ZhiweixinxiEntity zhiweixinxi,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<ZhiweixinxiEntity> ew = new QueryWrapper<ZhiweixinxiEntity>();

        //查询结果
		PageUtils page = zhiweixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweixinxi), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ZhiweixinxiEntity zhiweixinxi){
       	QueryWrapper<ZhiweixinxiEntity> ew = new QueryWrapper<ZhiweixinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( zhiweixinxi, "zhiweixinxi"));
        return R.ok().put("data", zhiweixinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ZhiweixinxiEntity zhiweixinxi){
        QueryWrapper< ZhiweixinxiEntity> ew = new QueryWrapper< ZhiweixinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( zhiweixinxi, "zhiweixinxi"));
		ZhiweixinxiView zhiweixinxiView =  zhiweixinxiService.selectView(ew);
		return R.ok("查询职位信息成功").put("data", zhiweixinxiView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ZhiweixinxiEntity zhiweixinxi = zhiweixinxiService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(zhiweixinxi,deSens);
        return R.ok().put("data", zhiweixinxi);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ZhiweixinxiEntity zhiweixinxi = zhiweixinxiService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(zhiweixinxi,deSens);
        return R.ok().put("data", zhiweixinxi);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增职位信息")
    public R save(@RequestBody ZhiweixinxiEntity zhiweixinxi, HttpServletRequest request){
        //验证字段唯一性，否则返回错误信息
        if(zhiweixinxiService.count(new QueryWrapper<ZhiweixinxiEntity>().eq("zhiwei", zhiweixinxi.getZhiwei()))>0) {
            return R.error("职位已存在");
        }
        //ValidatorUtils.validateEntity(zhiweixinxi);
        zhiweixinxiService.save(zhiweixinxi);
        return R.ok().put("data",zhiweixinxi.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增职位信息")
    @RequestMapping("/add")
    public R add(@RequestBody ZhiweixinxiEntity zhiweixinxi, HttpServletRequest request){
        //验证字段唯一性，否则返回错误信息
        if(zhiweixinxiService.count(new QueryWrapper<ZhiweixinxiEntity>().eq("zhiwei", zhiweixinxi.getZhiwei()))>0) {
            return R.error("职位已存在");
        }
        //ValidatorUtils.validateEntity(zhiweixinxi);
        zhiweixinxiService.save(zhiweixinxi);
        return R.ok().put("data",zhiweixinxi.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改职位信息")
    public R update(@RequestBody ZhiweixinxiEntity zhiweixinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweixinxi);
        //验证字段唯一性，否则返回错误信息
        if(zhiweixinxiService.count(new QueryWrapper<ZhiweixinxiEntity>().ne("id", zhiweixinxi.getId()).eq("zhiwei", zhiweixinxi.getZhiwei()))>0) {
            return R.error("职位已存在");
        }
        //全部更新
        zhiweixinxiService.updateById(zhiweixinxi);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除职位信息")
    public R delete(@RequestBody Long[] ids){
        zhiweixinxiService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否




}
