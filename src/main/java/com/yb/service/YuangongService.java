package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.YuangongEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.YuangongVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.YuangongView;


/**
 * 员工
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongService extends IService<YuangongEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YuangongVO> selectListVO(QueryWrapper<YuangongEntity> wrapper);
   	
   	YuangongVO selectVO(@Param("ew") QueryWrapper<YuangongEntity> wrapper);
   	
   	List<YuangongView> selectListView(QueryWrapper<YuangongEntity> wrapper);
   	
   	YuangongView selectView(@Param("ew") QueryWrapper<YuangongEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<YuangongEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,QueryWrapper<YuangongEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,QueryWrapper<YuangongEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,QueryWrapper<YuangongEntity> wrapper);



}

