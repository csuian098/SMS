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
import com.yb.entity.ZhiweishensuEntity;

import com.yb.service.ZhiweidiaodongService;
import com.yb.service.YuangongService;
import com.yb.service.ZhiweishensuService;
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

    @Autowired
    private ZhiweishensuService zhiweishensuService;










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
        List<ZhiweidiaodongEntity> removeList = zhiweidiaodongService.listByIds(Arrays.asList(ids));
        Set<String> affectedGonghao = new HashSet<>();
        for (ZhiweidiaodongEntity item : removeList) {
            if (item != null && StringUtils.isNotBlank(item.getGonghao())) {
                affectedGonghao.add(item.getGonghao());
            }
        }
        zhiweidiaodongService.removeBatchByIds(Arrays.asList(ids));
        for (String gonghao : affectedGonghao) {
            recomputeEmployeePosition(gonghao);
        }
        return R.ok();
    }





    // hasAlipay:鍚?

    private void applyEmployeePositionChange(ZhiweidiaodongEntity zhiweidiaodong) {
        if (zhiweidiaodong == null || StringUtils.isBlank(zhiweidiaodong.getGonghao())) {
            return;
        }
        recomputeEmployeePosition(zhiweidiaodong.getGonghao());
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

    private boolean isApproved(String sfsh) {
        return "是".equals(sfsh) || "鏄?".equals(sfsh);
    }




}


