package com.yb.dao;

import com.yb.entity.ZhiweixinxiEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.ZhiweixinxiVO;
import com.yb.entity.view.ZhiweixinxiView;


/**
 * 职位信息
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface ZhiweixinxiDao extends BaseMapper<ZhiweixinxiEntity> {
	
	List<ZhiweixinxiVO> selectListVO(@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);
	
	ZhiweixinxiVO selectVO(@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);
	
	List<ZhiweixinxiView> selectListView(@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);

	List<ZhiweixinxiView> selectListView(Page page,@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);

	
	ZhiweixinxiView selectView(@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);
	

}
