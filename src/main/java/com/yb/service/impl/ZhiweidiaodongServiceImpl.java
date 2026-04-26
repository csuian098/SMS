package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.ZhiweidiaodongDao;
import com.yb.entity.ZhiweidiaodongEntity;
import com.yb.service.ZhiweidiaodongService;
import com.yb.entity.vo.ZhiweidiaodongVO;
import com.yb.entity.view.ZhiweidiaodongView;

@Service("zhiweidiaodongService")
public class ZhiweidiaodongServiceImpl extends ServiceImpl<ZhiweidiaodongDao, ZhiweidiaodongEntity> implements ZhiweidiaodongService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ZhiweidiaodongEntity> page = baseMapper.selectPage(new Query<ZhiweidiaodongEntity>(params).getPage(),new QueryWrapper<ZhiweidiaodongEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ZhiweidiaodongEntity> wrapper) {
        Page<ZhiweidiaodongEntity> page = baseMapper.selectPage(new Query<ZhiweidiaodongEntity>(params).getPage(),wrapper);
        Page<ZhiweidiaodongView> pageVo = (Page<ZhiweidiaodongView>)page.convert(ZhiweidiaodongView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<ZhiweidiaodongVO> selectListVO(QueryWrapper<ZhiweidiaodongEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public ZhiweidiaodongVO selectVO(QueryWrapper<ZhiweidiaodongEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<ZhiweidiaodongView> selectListView(QueryWrapper<ZhiweidiaodongEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ZhiweidiaodongView selectView(QueryWrapper<ZhiweidiaodongEntity> wrapper) {
        ZhiweidiaodongEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new ZhiweidiaodongView(entity);
        } else {
            return null;
        }
	}


}
