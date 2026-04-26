package com.yb.dao;

import com.yb.entity.RenshiguanliyuanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.RenshiguanliyuanVO;
import com.yb.entity.view.RenshiguanliyuanView;


/**
 * 人事管理员
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface RenshiguanliyuanDao extends BaseMapper<RenshiguanliyuanEntity> {
	
	List<RenshiguanliyuanVO> selectListVO(@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);
	
	RenshiguanliyuanVO selectVO(@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);
	
	List<RenshiguanliyuanView> selectListView(@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);

	List<RenshiguanliyuanView> selectListView(Page page,@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);

	
	RenshiguanliyuanView selectView(@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);
	

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);



}
