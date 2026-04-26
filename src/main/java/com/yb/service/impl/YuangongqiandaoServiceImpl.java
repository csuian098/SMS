package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.YuangongqiandaoDao;
import com.yb.entity.YuangongqiandaoEntity;
import com.yb.service.YuangongqiandaoService;
import com.yb.entity.vo.YuangongqiandaoVO;
import com.yb.entity.view.YuangongqiandaoView;

@Service("yuangongqiandaoService")
public class YuangongqiandaoServiceImpl extends ServiceImpl<YuangongqiandaoDao, YuangongqiandaoEntity> implements YuangongqiandaoService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YuangongqiandaoEntity> page = baseMapper.selectPage(new Query<YuangongqiandaoEntity>(params).getPage(),new QueryWrapper<YuangongqiandaoEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<YuangongqiandaoEntity> wrapper) {
        Page<YuangongqiandaoEntity> page = baseMapper.selectPage(new Query<YuangongqiandaoEntity>(params).getPage(),wrapper);
        Page<YuangongqiandaoView> pageVo = (Page<YuangongqiandaoView>)page.convert(YuangongqiandaoView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<YuangongqiandaoVO> selectListVO(QueryWrapper<YuangongqiandaoEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YuangongqiandaoVO selectVO(QueryWrapper<YuangongqiandaoEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YuangongqiandaoView> selectListView(QueryWrapper<YuangongqiandaoEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YuangongqiandaoView selectView(QueryWrapper<YuangongqiandaoEntity> wrapper) {
        YuangongqiandaoEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new YuangongqiandaoView(entity);
        } else {
            return null;
        }
	}


}
