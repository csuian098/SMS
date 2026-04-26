package com.yb.dao;

import com.yb.entity.TongyixinxiEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.TongyixinxiVO;
import com.yb.entity.view.TongyixinxiView;


/**
 * 同意信息
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface TongyixinxiDao extends BaseMapper<TongyixinxiEntity> {
	
	List<TongyixinxiVO> selectListVO(@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);
	
	TongyixinxiVO selectVO(@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);
	
	List<TongyixinxiView> selectListView(@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);

	List<TongyixinxiView> selectListView(Page page,@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);

	
	TongyixinxiView selectView(@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);
	

}
