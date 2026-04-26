package com.yb.dao;

import com.yb.entity.JixiaokaoheEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.JixiaokaoheVO;
import com.yb.entity.view.JixiaokaoheView;


/**
 * 绩效考核
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface JixiaokaoheDao extends BaseMapper<JixiaokaoheEntity> {
	
	List<JixiaokaoheVO> selectListVO(@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);
	
	JixiaokaoheVO selectVO(@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);
	
	List<JixiaokaoheView> selectListView(@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);

	List<JixiaokaoheView> selectListView(Page page,@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);

	
	JixiaokaoheView selectView(@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);
	

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);



}
