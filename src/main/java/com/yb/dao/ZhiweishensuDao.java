package com.yb.dao;

import com.yb.entity.ZhiweishensuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.ZhiweishensuVO;
import com.yb.entity.view.ZhiweishensuView;


/**
 * 职位申诉
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface ZhiweishensuDao extends BaseMapper<ZhiweishensuEntity> {
	
	List<ZhiweishensuVO> selectListVO(@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);
	
	ZhiweishensuVO selectVO(@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);
	
	List<ZhiweishensuView> selectListView(@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);

	List<ZhiweishensuView> selectListView(Page page,@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);

	
	ZhiweishensuView selectView(@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);
	

}
