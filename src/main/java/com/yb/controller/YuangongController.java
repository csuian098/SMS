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
import com.yb.service.TokenService;
import com.yb.entity.TokenEntity;
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

import com.yb.entity.YuangongEntity;
import com.yb.entity.view.YuangongView;

import com.yb.service.YuangongService;
import com.yb.utils.PageUtils;
import com.yb.utils.R;
import com.yb.utils.EncryptUtil;
import com.yb.utils.MPUtil;
import com.yb.utils.MapUtils;
import com.yb.utils.CommonUtil;
import java.io.IOException;

/**
 * 员工
 * 后端接口
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@RestController
@RequestMapping("/yuangong")
public class YuangongController {
    @Autowired
    private YuangongService yuangongService;

    private boolean shenfenzhengExists(String shenfenzheng, Long excludeId) {
        if(StringUtils.isBlank(shenfenzheng) || shenfenzheng.contains("*")) {
            return false;
        }
        QueryWrapper<YuangongEntity> wrapper = new QueryWrapper<YuangongEntity>().eq("shenfenzheng", shenfenzheng);
        if(excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return yuangongService.count(wrapper) > 0;
    }







	@Autowired
	private TokenService tokenService;

	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		// 根据登录查询用户信息
        YuangongEntity u = yuangongService.getOne(new QueryWrapper<YuangongEntity>().eq("gonghao", username));
        // 当用户不存在或md5方式验证密码不通过时
        if(u==null || !u.getMima().equals(EncryptUtil.md5(password))) {
            //账号或密码不正确提示
			return R.error("账号或密码不正确");
		}
        // 获取登录token
		String token = tokenService.generateToken(u.getId(), username,"yuangong",  "员工" );
        //返回token
		return R.ok().put("token", token);
	}



	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody YuangongEntity yuangong){
    	//ValidatorUtils.validateEntity(yuangong);
        //根据登录账号获取用户信息判断是否存在该用户，否则返回错误信息
    	YuangongEntity u = yuangongService.getOne(new QueryWrapper<YuangongEntity>().eq("gonghao", yuangong.getGonghao()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
        //判断是否存在相同工号，否则返回错误信息
        if(yuangongService.count(new QueryWrapper<YuangongEntity>().eq("gonghao", yuangong.getGonghao()))>0) {
            return R.error("工号已存在");
        }
        if(shenfenzhengExists(yuangong.getShenfenzheng(), null)) {
            return R.error("身份证不可重复");
        }
		Long uId = new Date().getTime();
		yuangong.setId(uId);
        //设置登录密码md5方式加密
        yuangong.setMima(EncryptUtil.md5(yuangong.getMima()));
        //保存用户
        yuangongService.save(yuangong);
        return R.ok();
    }



	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}

	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        YuangongEntity u = yuangongService.getById(id);
        return R.ok().put("data", u);
    }

    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	//根据登录账号判断是否存在用户信息，否则返回错误信息
        YuangongEntity u = yuangongService.getOne(new QueryWrapper<YuangongEntity>().eq("gonghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        //重置密码为123456，并使用md5方式加密
        u.setMima(EncryptUtil.md5("123456"));
        yuangongService.updateById(u);
        return R.ok("密码已重置为：123456");
    }

    /**
     * 获取账号列表
     */
    @RequestMapping("/accountList")
    public R getAccountList(@RequestParam Map<String, Object> params,YuangongEntity yuangong){
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
        QueryWrapper<YuangongEntity> wrapper =MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangong), params), params);
        List<Map> list = yuangongService.list(wrapper).stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", v.getId());
            map.put("account", v.getGonghao());
            return map;
        }).collect(Collectors.toList());
        return R.ok().put("data", list);
    }






    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YuangongEntity yuangong,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();


        //查询结果
		PageUtils page = yuangongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangong), params), params));
        Map<String, String> deSens = new HashMap<>();
        deSens.put("shenfenzheng","身");
        deSens.put("shouji","手");
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YuangongEntity yuangong,
                @RequestParam(required = false) Double qingjiatianshustart,
                @RequestParam(required = false) Double qingjiatianshuend,
		HttpServletRequest request){
        //设置查询条件
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
        if(qingjiatianshustart!=null) ew.ge("qingjiatianshu", qingjiatianshustart);
        if(qingjiatianshuend!=null) ew.le("qingjiatianshu", qingjiatianshuend);

        //查询结果
		PageUtils page = yuangongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangong), params), params));
        Map<String, String> deSens = new HashMap<>();
        deSens.put("shenfenzheng","身");
        deSens.put("shouji","手");
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YuangongEntity yuangong){
       	QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yuangong, "yuangong"));
        return R.ok().put("data", yuangongService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YuangongEntity yuangong){
        QueryWrapper< YuangongEntity> ew = new QueryWrapper< YuangongEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yuangong, "yuangong"));
		YuangongView yuangongView =  yuangongService.selectView(ew);
		return R.ok("查询员工成功").put("data", yuangongView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YuangongEntity yuangong = yuangongService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        deSens.put("shenfenzheng","身");
        deSens.put("shouji","手");
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(yuangong,deSens);
        return R.ok().put("data", yuangong);
    }

    /**
     * 前台详情
     */
    @RequestMapping("/rawInfo/{id}")
    public R rawInfo(@PathVariable("id") Long id){
        YuangongEntity yuangong = yuangongService.getById(id);
        return R.ok().put("data", yuangong);
    }

	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YuangongEntity yuangong = yuangongService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        deSens.put("shenfenzheng","身");
        deSens.put("shouji","手");
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(yuangong,deSens);
        return R.ok().put("data", yuangong);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增员工")
    public R save(@RequestBody YuangongEntity yuangong, HttpServletRequest request){
        //验证字段唯一性，否则返回错误信息
        if(yuangongService.count(new QueryWrapper<YuangongEntity>().eq("gonghao", yuangong.getGonghao()))>0) {
            return R.error("工号已存在");
        }
        //ValidatorUtils.validateEntity(yuangong);
        //验证账号唯一性，否则返回错误信息
        YuangongEntity u = yuangongService.getOne(new QueryWrapper<YuangongEntity>().eq("gonghao", yuangong.getGonghao()));
        if(u!=null) {
            return R.error("用户已存在");
        }
        if(shenfenzhengExists(yuangong.getShenfenzheng(), null)) {
            return R.error("身份证不可重复");
        }
    	yuangong.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
		yuangong.setId(new Date().getTime());
        //密码使用md5方式加密
        yuangong.setMima(EncryptUtil.md5(yuangong.getMima()));
        yuangongService.save(yuangong);
        return R.ok().put("data",yuangong.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增员工")
    @RequestMapping("/add")
    public R add(@RequestBody YuangongEntity yuangong, HttpServletRequest request){
        //验证字段唯一性，否则返回错误信息
        if(yuangongService.count(new QueryWrapper<YuangongEntity>().eq("gonghao", yuangong.getGonghao()))>0) {
            return R.error("工号已存在");
        }
        //ValidatorUtils.validateEntity(yuangong);
        //验证账号唯一性，否则返回错误信息
        YuangongEntity u = yuangongService.getOne(new QueryWrapper<YuangongEntity>().eq("gonghao", yuangong.getGonghao()));
        if(u!=null) {
            return R.error("用户已存在");
        }
        if(shenfenzhengExists(yuangong.getShenfenzheng(), null)) {
            return R.error("身份证不可重复");
        }
    	yuangong.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
		yuangong.setId(new Date().getTime());
        //密码使用md5方式加密
        yuangong.setMima(EncryptUtil.md5(yuangong.getMima()));
        yuangongService.save(yuangong);
        return R.ok().put("data",yuangong.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改员工")
    public R update(@RequestBody YuangongEntity yuangong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yuangong);
        //验证字段唯一性，否则返回错误信息
        if(yuangongService.count(new QueryWrapper<YuangongEntity>().ne("id", yuangong.getId()).eq("gonghao", yuangong.getGonghao()))>0) {
            return R.error("工号已存在");
        }
        if(shenfenzhengExists(yuangong.getShenfenzheng(), yuangong.getId())) {
            return R.error("身份证不可重复");
        }
	    YuangongEntity yuangongEntity = yuangongService.getById(yuangong.getId());
        //如果密码不为空，则判断是否和输入密码一致，不一致则重新设置
        if(StringUtils.isNotBlank(yuangong.getMima()) && !yuangong.getMima().equals(yuangongEntity.getMima())) {
            //密码使用md5方式加密
            yuangong.setMima(EncryptUtil.md5(yuangong.getMima()));
        }
        //如果脱敏字段提交回来的是带 * 的脱敏值，保留数据库原值，避免脏数据写回
        if(yuangong.getShouji()!=null && yuangong.getShouji().contains("*")) {
            yuangong.setShouji(yuangongEntity.getShouji());
        }
        if(yuangong.getShenfenzheng()!=null && yuangong.getShenfenzheng().contains("*")) {
            yuangong.setShenfenzheng(yuangongEntity.getShenfenzheng());
        }
        //全部更新
        yuangongService.updateById(yuangong);
        if(null!=yuangong.getGonghao())
        {
            // 修改token
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setUsername(yuangong.getGonghao());
            tokenService.update(tokenEntity, new UpdateWrapper<TokenEntity>().eq("userid", yuangong.getId()));
        }
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除员工")
    public R delete(@RequestBody Long[] ids){
        yuangongService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }





    // hasAlipay:否


    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, @RequestParam(required = false, defaultValue = "总和") String func, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangong_" + xColumnName + "_" + yColumnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        //构建查询统计条件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("method", func);
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
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

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }

        //获取结果
        List<Map<String, Object>> result = yuangongService.selectValue(params, ew);
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
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request)  throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangong_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        //构建查询统计条件
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
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

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = yuangongService.selectValue(params, ew);
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
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, @RequestParam(required = false, defaultValue = "总和") String func, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangong_" + xColumnName + "_" + yColumnName + "_"+timeStatType+".json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("timeStatType", timeStatType);
        params.put("method", func);
        //构建查询统计条件
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
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

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<Map<String, Object>> result = yuangongService.selectTimeStatValue(params, ew);
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
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam String yColumnNameMul, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request) throws IOException
    {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_yuangong_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + ".json");
        if (java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        //构建查询统计条件
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
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

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = yuangongService.selectTimeStatValue(params, ew);
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
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("group_yuangong_" + columnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)){
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        //构建查询统计条件
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<Map<String, Object>> result = yuangongService.selectGroup(params, ew);
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
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,YuangongEntity yuangong, HttpServletRequest request){
        QueryWrapper<YuangongEntity> ew = new QueryWrapper<YuangongEntity>();
        long count = yuangongService.count(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuangong), params), params));
        return R.ok().put("data", count);
    }

}
