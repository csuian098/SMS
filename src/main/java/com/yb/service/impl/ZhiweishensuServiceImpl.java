package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.ZhiweishensuDao;
import com.yb.entity.ZhiweishensuEntity;
import com.yb.service.ZhiweishensuService;
import com.yb.entity.vo.ZhiweishensuVO;
import com.yb.entity.view.ZhiweishensuView;

@Service("zhiweishensuService")
public class ZhiweishensuServiceImpl extends ServiceImpl<ZhiweishensuDao, ZhiweishensuEntity> implements ZhiweishensuService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ZhiweishensuEntity> page = baseMapper.selectPage(new Query<ZhiweishensuEntity>(params).getPage(),new QueryWrapper<ZhiweishensuEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ZhiweishensuEntity> wrapper) {
        Page<ZhiweishensuEntity> page = baseMapper.selectPage(new Query<ZhiweishensuEntity>(params).getPage(),wrapper);
        Page<ZhiweishensuView> pageVo = (Page<ZhiweishensuView>)page.convert(ZhiweishensuView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<ZhiweishensuVO> selectListVO(QueryWrapper<ZhiweishensuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public ZhiweishensuVO selectVO(QueryWrapper<ZhiweishensuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<ZhiweishensuView> selectListView(QueryWrapper<ZhiweishensuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ZhiweishensuView selectView(QueryWrapper<ZhiweishensuEntity> wrapper) {
        ZhiweishensuEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new ZhiweishensuView(entity);
        } else {
            return null;
        }
	}


}
