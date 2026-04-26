package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.JixiaokaoheDao;
import com.yb.entity.JixiaokaoheEntity;
import com.yb.service.JixiaokaoheService;
import com.yb.entity.vo.JixiaokaoheVO;
import com.yb.entity.view.JixiaokaoheView;

@Service("jixiaokaoheService")
public class JixiaokaoheServiceImpl extends ServiceImpl<JixiaokaoheDao, JixiaokaoheEntity> implements JixiaokaoheService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JixiaokaoheEntity> page = baseMapper.selectPage(new Query<JixiaokaoheEntity>(params).getPage(),new QueryWrapper<JixiaokaoheEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<JixiaokaoheEntity> wrapper) {
        Page<JixiaokaoheEntity> page = baseMapper.selectPage(new Query<JixiaokaoheEntity>(params).getPage(),wrapper);
        Page<JixiaokaoheView> pageVo = (Page<JixiaokaoheView>)page.convert(JixiaokaoheView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<JixiaokaoheVO> selectListVO(QueryWrapper<JixiaokaoheEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public JixiaokaoheVO selectVO(QueryWrapper<JixiaokaoheEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<JixiaokaoheView> selectListView(QueryWrapper<JixiaokaoheEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public JixiaokaoheView selectView(QueryWrapper<JixiaokaoheEntity> wrapper) {
        JixiaokaoheEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new JixiaokaoheView(entity);
        } else {
            return null;
        }
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, QueryWrapper<JixiaokaoheEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, QueryWrapper<JixiaokaoheEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, QueryWrapper<JixiaokaoheEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
