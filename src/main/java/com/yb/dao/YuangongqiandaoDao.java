package com.yb.dao;

import com.yb.entity.YuangongqiandaoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.YuangongqiandaoVO;
import com.yb.entity.view.YuangongqiandaoView;


/**
 * 员工签到
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongqiandaoDao extends BaseMapper<YuangongqiandaoEntity> {
	
	List<YuangongqiandaoVO> selectListVO(@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);
	
	YuangongqiandaoVO selectVO(@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);
	
	List<YuangongqiandaoView> selectListView(@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);

	List<YuangongqiandaoView> selectListView(Page page,@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);

	
	YuangongqiandaoView selectView(@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);
	

}
