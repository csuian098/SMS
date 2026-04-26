package com.yb.dao;

import com.yb.entity.MenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.MenuVO;
import com.yb.entity.view.MenuView;


/**
 * 菜单
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface MenuDao extends BaseMapper<MenuEntity> {
	
	List<MenuVO> selectListVO(@Param("ew") QueryWrapper<MenuEntity> wrapper);
	
	MenuVO selectVO(@Param("ew") QueryWrapper<MenuEntity> wrapper);
	
	List<MenuView> selectListView(@Param("ew") QueryWrapper<MenuEntity> wrapper);

	List<MenuView> selectListView(Page page,@Param("ew") QueryWrapper<MenuEntity> wrapper);

	
	MenuView selectView(@Param("ew") QueryWrapper<MenuEntity> wrapper);
	

}
