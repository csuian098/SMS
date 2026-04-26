package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.RenshiguanliyuanDao;
import com.yb.entity.RenshiguanliyuanEntity;
import com.yb.service.RenshiguanliyuanService;
import com.yb.entity.vo.RenshiguanliyuanVO;
import com.yb.entity.view.RenshiguanliyuanView;
import com.yb.service.TokenService;
import com.yb.entity.TokenEntity;

@Service("renshiguanliyuanService")
public class RenshiguanliyuanServiceImpl extends ServiceImpl<RenshiguanliyuanDao, RenshiguanliyuanEntity> implements RenshiguanliyuanService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RenshiguanliyuanEntity> page = baseMapper.selectPage(new Query<RenshiguanliyuanEntity>(params).getPage(),new QueryWrapper<RenshiguanliyuanEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<RenshiguanliyuanEntity> wrapper) {
        Page<RenshiguanliyuanEntity> page = baseMapper.selectPage(new Query<RenshiguanliyuanEntity>(params).getPage(),wrapper);
        Page<RenshiguanliyuanView> pageVo = (Page<RenshiguanliyuanView>)page.convert(RenshiguanliyuanView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<RenshiguanliyuanVO> selectListVO(QueryWrapper<RenshiguanliyuanEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public RenshiguanliyuanVO selectVO(QueryWrapper<RenshiguanliyuanEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<RenshiguanliyuanView> selectListView(QueryWrapper<RenshiguanliyuanEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public RenshiguanliyuanView selectView(QueryWrapper<RenshiguanliyuanEntity> wrapper) {
        RenshiguanliyuanEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new RenshiguanliyuanView(entity);
        } else {
            return null;
        }
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, QueryWrapper<RenshiguanliyuanEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, QueryWrapper<RenshiguanliyuanEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, QueryWrapper<RenshiguanliyuanEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
