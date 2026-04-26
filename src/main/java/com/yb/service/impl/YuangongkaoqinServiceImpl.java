package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.YuangongkaoqinDao;
import com.yb.entity.YuangongkaoqinEntity;
import com.yb.service.YuangongkaoqinService;
import com.yb.entity.vo.YuangongkaoqinVO;
import com.yb.entity.view.YuangongkaoqinView;

@Service("yuangongkaoqinService")
public class YuangongkaoqinServiceImpl extends ServiceImpl<YuangongkaoqinDao, YuangongkaoqinEntity> implements YuangongkaoqinService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YuangongkaoqinEntity> page = baseMapper.selectPage(new Query<YuangongkaoqinEntity>(params).getPage(),new QueryWrapper<YuangongkaoqinEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<YuangongkaoqinEntity> wrapper) {
        Page<YuangongkaoqinEntity> page = baseMapper.selectPage(new Query<YuangongkaoqinEntity>(params).getPage(),wrapper);
        Page<YuangongkaoqinView> pageVo = (Page<YuangongkaoqinView>)page.convert(YuangongkaoqinView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<YuangongkaoqinVO> selectListVO(QueryWrapper<YuangongkaoqinEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YuangongkaoqinVO selectVO(QueryWrapper<YuangongkaoqinEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YuangongkaoqinView> selectListView(QueryWrapper<YuangongkaoqinEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YuangongkaoqinView selectView(QueryWrapper<YuangongkaoqinEntity> wrapper) {
        YuangongkaoqinEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new YuangongkaoqinView(entity);
        } else {
            return null;
        }
	}


}
