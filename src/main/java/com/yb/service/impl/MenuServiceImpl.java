package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.MenuDao;
import com.yb.entity.MenuEntity;
import com.yb.service.MenuService;
import com.yb.entity.vo.MenuVO;
import com.yb.entity.view.MenuView;

@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MenuEntity> page = baseMapper.selectPage(new Query<MenuEntity>(params).getPage(),new QueryWrapper<MenuEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<MenuEntity> wrapper) {
        Page<MenuEntity> page = baseMapper.selectPage(new Query<MenuEntity>(params).getPage(),wrapper);
        Page<MenuView> pageVo = (Page<MenuView>)page.convert(MenuView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<MenuVO> selectListVO(QueryWrapper<MenuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public MenuVO selectVO(QueryWrapper<MenuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<MenuView> selectListView(QueryWrapper<MenuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public MenuView selectView(QueryWrapper<MenuEntity> wrapper) {
        MenuEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new MenuView(entity);
        } else {
            return null;
        }
	}


}
