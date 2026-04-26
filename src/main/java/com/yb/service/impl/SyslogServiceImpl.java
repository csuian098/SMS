package com.yb.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.utils.PageUtils;
import com.yb.utils.Query;


import com.yb.dao.SyslogDao;
import com.yb.entity.SyslogEntity;
import com.yb.service.SyslogService;
import com.yb.entity.vo.SyslogVO;
import com.yb.entity.view.SyslogView;

@Service("syslogService")
public class SyslogServiceImpl extends ServiceImpl<SyslogDao, SyslogEntity> implements SyslogService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SyslogEntity> page = baseMapper.selectPage(new Query<SyslogEntity>(params).getPage(),new QueryWrapper<SyslogEntity>());
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<SyslogEntity> wrapper) {
        Page<SyslogEntity> page = baseMapper.selectPage(new Query<SyslogEntity>(params).getPage(),wrapper);
        Page<SyslogView> pageVo = (Page<SyslogView>)page.convert(SyslogView::new);
        return new PageUtils(pageVo);
 	}

    
    @Override
	public List<SyslogVO> selectListVO(QueryWrapper<SyslogEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public SyslogVO selectVO(QueryWrapper<SyslogEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<SyslogView> selectListView(QueryWrapper<SyslogEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public SyslogView selectView(QueryWrapper<SyslogEntity> wrapper) {
        SyslogEntity entity = baseMapper.selectOne(wrapper);
        if (null != entity) {
            return new SyslogView(entity);
        } else {
            return null;
        }
	}


}
