package com.yb.dao;

import com.yb.entity.YuangongkaoqinEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.YuangongkaoqinVO;
import com.yb.entity.view.YuangongkaoqinView;


/**
 * 员工考勤
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongkaoqinDao extends BaseMapper<YuangongkaoqinEntity> {
	
	List<YuangongkaoqinVO> selectListVO(@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);
	
	YuangongkaoqinVO selectVO(@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);
	
	List<YuangongkaoqinView> selectListView(@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);

	List<YuangongkaoqinView> selectListView(Page page,@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);

	
	YuangongkaoqinView selectView(@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);
	

}
