package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.YuangongDao;
import com.yb.entity.YuangongEntity;
import com.yb.service.YuangongService;
import com.yb.entity.vo.YuangongVO;
import com.yb.entity.view.YuangongView;
import com.yb.service.TokenService;
import com.yb.entity.TokenEntity;

@Service("yuangongService")
public class YuangongServiceImpl extends ServiceImpl<YuangongDao, YuangongEntity> implements YuangongService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YuangongEntity> page = baseMapper.selectPage(new Query<YuangongEntity>(params).getPage(),new QueryWrapper<YuangongEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<YuangongEntity> wrapper) {
        Page<YuangongEntity> page = baseMapper.selectPage(new Query<YuangongEntity>(params).getPage(),wrapper);
        Page<YuangongView> pageVo = (Page<YuangongView>)page.convert(YuangongView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<YuangongVO> selectListVO(QueryWrapper<YuangongEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YuangongVO selectVO(QueryWrapper<YuangongEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YuangongView> selectListView(QueryWrapper<YuangongEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YuangongView selectView(QueryWrapper<YuangongEntity> wrapper) {
        YuangongEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new YuangongView(entity);
        } else {
            return null;
        }
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, QueryWrapper<YuangongEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, QueryWrapper<YuangongEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, QueryWrapper<YuangongEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
