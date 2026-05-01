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
import com.yb.entity.YuangongEntity;

import com.yb.service.ZhiweidiaodongService;
import com.yb.service.YuangongService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 鑱屼綅璋冨姩
 * 鍚庣鎺ュ彛
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/zhiweidiaodong")
public class ZhiweidiaodongController {
    @Autowired
    private ZhiweidiaodongService zhiweidiaodongService;

    @Autowired
    private YuangongService yuangongService;










    /**
     * 鍚庡彴鍒楄〃
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ZhiweidiaodongEntity zhiweidiaodong,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yuangong")) {
			zhiweidiaodong.setGonghao((String)request.getSession().getAttribute("username"));
		}
        //璁剧疆鏌ヨ鏉′欢
        QueryWrapper<ZhiweidiaodongEntity> ew = new QueryWrapper<ZhiweidiaodongEntity>();


        //鏌ヨ缁撴灉
		PageUtils page = zhiweidiaodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweidiaodong), params), params));
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
    public R list(@RequestParam Map<String, Object> params,ZhiweidiaodongEntity zhiweidiaodong,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date biandongriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date biandongriqiend,
		HttpServletRequest request){
        //璁剧疆鏌ヨ鏉′欢
        QueryWrapper<ZhiweidiaodongEntity> ew = new QueryWrapper<ZhiweidiaodongEntity>();
        if(biandongriqistart!=null) ew.ge("biandongriqi", biandongriqistart);
        if(biandongriqiend!=null) ew.le("biandongriqi", biandongriqiend);

        //鏌ヨ缁撴灉
		PageUtils page = zhiweidiaodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhiweidiaodong), params), params));
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 鍒楄〃
     */
    @RequestMapping("/lists")
    public R list( ZhiweidiaodongEntity zhiweidiaodong){
       	QueryWrapper<ZhiweidiaodongEntity> ew = new QueryWrapper<ZhiweidiaodongEntity>();
      	ew.allEq(MPUtil.allEQMapPre( zhiweidiaodong, "zhiweidiaodong"));
        return R.ok().put("data", zhiweidiaodongService.selectListView(ew));
    }

	 /**
     * 鏌ヨ
     */
    @RequestMapping("/query")
    public R query(ZhiweidiaodongEntity zhiweidiaodong){
        QueryWrapper< ZhiweidiaodongEntity> ew = new QueryWrapper< ZhiweidiaodongEntity>();
 		ew.allEq(MPUtil.allEQMapPre( zhiweidiaodong, "zhiweidiaodong"));
		ZhiweidiaodongView zhiweidiaodongView =  zhiweidiaodongService.selectView(ew);
		return R.ok("鏌ヨ鑱屼綅璋冨姩鎴愬姛").put("data", zhiweidiaodongView);
    }

    /**
     * 鍚庡彴璇︽儏
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ZhiweidiaodongEntity zhiweidiaodong = zhiweidiaodongService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(zhiweidiaodong,deSens);
        return R.ok().put("data", zhiweidiaodong);
    }

    /**
     * 鍓嶅彴璇︽儏
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ZhiweidiaodongEntity zhiweidiaodong = zhiweidiaodongService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        //缁欓渶瑕佽劚鏁忕殑瀛楁鑴辨晱
        DeSensUtil.desensitize(zhiweidiaodong,deSens);
        return R.ok().put("data", zhiweidiaodong);
    }




    /**
     * 鍚庡彴淇濆瓨
     */
    @RequestMapping("/save")
    @SysLog("鏂板鑱屼綅璋冨姩")
    public R save(@RequestBody ZhiweidiaodongEntity zhiweidiaodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweidiaodong);
        zhiweidiaodongService.save(zhiweidiaodong);
        applyEmployeePositionChange(zhiweidiaodong);
        return R.ok().put("data",zhiweidiaodong.getId());
    }

    /**
     * 鍓嶅彴淇濆瓨
     */
    @SysLog("鏂板鑱屼綅璋冨姩")
    @RequestMapping("/add")
    public R add(@RequestBody ZhiweidiaodongEntity zhiweidiaodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweidiaodong);
        zhiweidiaodongService.save(zhiweidiaodong);
        applyEmployeePositionChange(zhiweidiaodong);
        return R.ok().put("data",zhiweidiaodong.getId());
    }





    /**
     * 淇敼
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("淇敼鑱屼綅璋冨姩")
    public R update(@RequestBody ZhiweidiaodongEntity zhiweidiaodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zhiweidiaodong);
        //鍏ㄩ儴鏇存柊
        zhiweidiaodongService.updateById(zhiweidiaodong);
        applyEmployeePositionChange(zhiweidiaodong);
        return R.ok();
    }





    /**
     * 鍒犻櫎
     */
    @RequestMapping("/delete")
    @SysLog("鍒犻櫎鑱屼綅璋冨姩")
    public R delete(@RequestBody Long[] ids){
        zhiweidiaodongService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:鍚?

    private void applyEmployeePositionChange(ZhiweidiaodongEntity zhiweidiaodong) {
        if (zhiweidiaodong == null
                || StringUtils.isBlank(zhiweidiaodong.getGonghao())
                || StringUtils.isBlank(zhiweidiaodong.getXianzhiwei())) {
            return;
        }
        YuangongEntity yuangong = yuangongService.getOne(
                new QueryWrapper<YuangongEntity>().eq("gonghao", zhiweidiaodong.getGonghao()),
                false
        );
        if (yuangong == null) {
            return;
        }
        yuangong.setZhiwei(zhiweidiaodong.getXianzhiwei());
        yuangongService.updateById(yuangong);
    }




}


