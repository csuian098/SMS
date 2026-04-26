package com.yb.dao;

import com.yb.entity.SyslogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.yb.entity.vo.SyslogVO;
import com.yb.entity.view.SyslogView;


/**
 * 系统日志
 * 
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface SyslogDao extends BaseMapper<SyslogEntity> {
	
	List<SyslogVO> selectListVO(@Param("ew") QueryWrapper<SyslogEntity> wrapper);
	
	SyslogVO selectVO(@Param("ew") QueryWrapper<SyslogEntity> wrapper);
	
	List<SyslogView> selectListView(@Param("ew") QueryWrapper<SyslogEntity> wrapper);

	List<SyslogView> selectListView(Page page,@Param("ew") QueryWrapper<SyslogEntity> wrapper);

	
	SyslogView selectView(@Param("ew") QueryWrapper<SyslogEntity> wrapper);
	

}
