package com.yb.dao;

import com.yb.entity.ZhiweidiaodongEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.ZhiweidiaodongVO;
import com.yb.entity.view.ZhiweidiaodongView;


/**
 * 职位调动
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface ZhiweidiaodongDao extends BaseMapper<ZhiweidiaodongEntity> {
	
	List<ZhiweidiaodongVO> selectListVO(@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);
	
	ZhiweidiaodongVO selectVO(@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);
	
	List<ZhiweidiaodongView> selectListView(@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);

	List<ZhiweidiaodongView> selectListView(Page page,@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);

	
	ZhiweidiaodongView selectView(@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);
	

}
