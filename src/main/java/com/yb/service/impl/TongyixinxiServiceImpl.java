package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.TongyixinxiDao;
import com.yb.entity.TongyixinxiEntity;
import com.yb.service.TongyixinxiService;
import com.yb.entity.vo.TongyixinxiVO;
import com.yb.entity.view.TongyixinxiView;

@Service("tongyixinxiService")
public class TongyixinxiServiceImpl extends ServiceImpl<TongyixinxiDao, TongyixinxiEntity> implements TongyixinxiService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TongyixinxiEntity> page = baseMapper.selectPage(new Query<TongyixinxiEntity>(params).getPage(),new QueryWrapper<TongyixinxiEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TongyixinxiEntity> wrapper) {
        Page<TongyixinxiEntity> page = baseMapper.selectPage(new Query<TongyixinxiEntity>(params).getPage(),wrapper);
        Page<TongyixinxiView> pageVo = (Page<TongyixinxiView>)page.convert(TongyixinxiView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<TongyixinxiVO> selectListVO(QueryWrapper<TongyixinxiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public TongyixinxiVO selectVO(QueryWrapper<TongyixinxiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<TongyixinxiView> selectListView(QueryWrapper<TongyixinxiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public TongyixinxiView selectView(QueryWrapper<TongyixinxiEntity> wrapper) {
        TongyixinxiEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new TongyixinxiView(entity);
        } else {
            return null;
        }
	}


}
