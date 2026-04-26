package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.QingjiashenqingDao;
import com.yb.entity.QingjiashenqingEntity;
import com.yb.service.QingjiashenqingService;
import com.yb.entity.vo.QingjiashenqingVO;
import com.yb.entity.view.QingjiashenqingView;

@Service("qingjiashenqingService")
public class QingjiashenqingServiceImpl extends ServiceImpl<QingjiashenqingDao, QingjiashenqingEntity> implements QingjiashenqingService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<QingjiashenqingEntity> page = baseMapper.selectPage(new Query<QingjiashenqingEntity>(params).getPage(),new QueryWrapper<QingjiashenqingEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<QingjiashenqingEntity> wrapper) {
        Page<QingjiashenqingEntity> page = baseMapper.selectPage(new Query<QingjiashenqingEntity>(params).getPage(),wrapper);
        Page<QingjiashenqingView> pageVo = (Page<QingjiashenqingView>)page.convert(QingjiashenqingView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<QingjiashenqingVO> selectListVO(QueryWrapper<QingjiashenqingEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public QingjiashenqingVO selectVO(QueryWrapper<QingjiashenqingEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<QingjiashenqingView> selectListView(QueryWrapper<QingjiashenqingEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public QingjiashenqingView selectView(QueryWrapper<QingjiashenqingEntity> wrapper) {
        QingjiashenqingEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new QingjiashenqingView(entity);
        } else {
            return null;
        }
	}


}
