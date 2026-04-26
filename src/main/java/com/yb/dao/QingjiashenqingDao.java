package com.yb.dao;

import com.yb.entity.QingjiashenqingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.QingjiashenqingVO;
import com.yb.entity.view.QingjiashenqingView;


/**
 * 请假申请
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface QingjiashenqingDao extends BaseMapper<QingjiashenqingEntity> {
	
	List<QingjiashenqingVO> selectListVO(@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);
	
	QingjiashenqingVO selectVO(@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);
	
	List<QingjiashenqingView> selectListView(@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);

	List<QingjiashenqingView> selectListView(Page page,@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);

	
	QingjiashenqingView selectView(@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);
	

}
