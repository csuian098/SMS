package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.SyslogEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.SyslogVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.SyslogView;


/**
 * 系统日志
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface SyslogService extends IService<SyslogEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<SyslogVO> selectListVO(QueryWrapper<SyslogEntity> wrapper);
   	
   	SyslogVO selectVO(@Param("ew") QueryWrapper<SyslogEntity> wrapper);
   	
   	List<SyslogView> selectListView(QueryWrapper<SyslogEntity> wrapper);
   	
   	SyslogView selectView(@Param("ew") QueryWrapper<SyslogEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<SyslogEntity> wrapper);

   	

}

