package com.yb.dao;

import com.yb.entity.YuangongEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.YuangongVO;
import com.yb.entity.view.YuangongView;


/**
 * 员工
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongDao extends BaseMapper<YuangongEntity> {
	
	List<YuangongVO> selectListVO(@Param("ew") QueryWrapper<YuangongEntity> wrapper);
	
	YuangongVO selectVO(@Param("ew") QueryWrapper<YuangongEntity> wrapper);
	
	List<YuangongView> selectListView(@Param("ew") QueryWrapper<YuangongEntity> wrapper);

	List<YuangongView> selectListView(Page page,@Param("ew") QueryWrapper<YuangongEntity> wrapper);

	
	YuangongView selectView(@Param("ew") QueryWrapper<YuangongEntity> wrapper);
	

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<YuangongEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<YuangongEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<YuangongEntity> wrapper);



}
