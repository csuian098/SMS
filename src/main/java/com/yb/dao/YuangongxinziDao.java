package com.yb.dao;

import com.yb.entity.YuangongxinziEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.YuangongxinziVO;
import com.yb.entity.view.YuangongxinziView;


/**
 * 员工薪资
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongxinziDao extends BaseMapper<YuangongxinziEntity> {
	
	List<YuangongxinziVO> selectListVO(@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);
	
	YuangongxinziVO selectVO(@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);
	
	List<YuangongxinziView> selectListView(@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);

	List<YuangongxinziView> selectListView(Page page,@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);

	
	YuangongxinziView selectView(@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);
	

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);



}
