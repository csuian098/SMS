package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.MenuEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.MenuVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.MenuView;


/**
 * 菜单
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface MenuService extends IService<MenuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<MenuVO> selectListVO(QueryWrapper<MenuEntity> wrapper);
   	
   	MenuVO selectVO(@Param("ew") QueryWrapper<MenuEntity> wrapper);
   	
   	List<MenuView> selectListView(QueryWrapper<MenuEntity> wrapper);
   	
   	MenuView selectView(@Param("ew") QueryWrapper<MenuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<MenuEntity> wrapper);

   	

}

