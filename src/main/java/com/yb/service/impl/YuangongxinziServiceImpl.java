package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.YuangongxinziDao;
import com.yb.entity.YuangongxinziEntity;
import com.yb.service.YuangongxinziService;
import com.yb.entity.vo.YuangongxinziVO;
import com.yb.entity.view.YuangongxinziView;

@Service("yuangongxinziService")
public class YuangongxinziServiceImpl extends ServiceImpl<YuangongxinziDao, YuangongxinziEntity> implements YuangongxinziService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YuangongxinziEntity> page = baseMapper.selectPage(new Query<YuangongxinziEntity>(params).getPage(),new QueryWrapper<YuangongxinziEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<YuangongxinziEntity> wrapper) {
        Page<YuangongxinziEntity> page = baseMapper.selectPage(new Query<YuangongxinziEntity>(params).getPage(),wrapper);
        Page<YuangongxinziView> pageVo = (Page<YuangongxinziView>)page.convert(YuangongxinziView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<YuangongxinziVO> selectListVO(QueryWrapper<YuangongxinziEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YuangongxinziVO selectVO(QueryWrapper<YuangongxinziEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YuangongxinziView> selectListView(QueryWrapper<YuangongxinziEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YuangongxinziView selectView(QueryWrapper<YuangongxinziEntity> wrapper) {
        YuangongxinziEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new YuangongxinziView(entity);
        } else {
            return null;
        }
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, QueryWrapper<YuangongxinziEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, QueryWrapper<YuangongxinziEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, QueryWrapper<YuangongxinziEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
