package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.ZhiweixinxiDao;
import com.yb.entity.ZhiweixinxiEntity;
import com.yb.service.ZhiweixinxiService;
import com.yb.entity.vo.ZhiweixinxiVO;
import com.yb.entity.view.ZhiweixinxiView;

@Service("zhiweixinxiService")
public class ZhiweixinxiServiceImpl extends ServiceImpl<ZhiweixinxiDao, ZhiweixinxiEntity> implements ZhiweixinxiService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ZhiweixinxiEntity> page = baseMapper.selectPage(new Query<ZhiweixinxiEntity>(params).getPage(),new QueryWrapper<ZhiweixinxiEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ZhiweixinxiEntity> wrapper) {
        Page<ZhiweixinxiEntity> page = baseMapper.selectPage(new Query<ZhiweixinxiEntity>(params).getPage(),wrapper);
        Page<ZhiweixinxiView> pageVo = (Page<ZhiweixinxiView>)page.convert(ZhiweixinxiView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<ZhiweixinxiVO> selectListVO(QueryWrapper<ZhiweixinxiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public ZhiweixinxiVO selectVO(QueryWrapper<ZhiweixinxiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<ZhiweixinxiView> selectListView(QueryWrapper<ZhiweixinxiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ZhiweixinxiView selectView(QueryWrapper<ZhiweixinxiEntity> wrapper) {
        ZhiweixinxiEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new ZhiweixinxiView(entity);
        } else {
            return null;
        }
	}


}
