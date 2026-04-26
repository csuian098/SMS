package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.YuangongxinziEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.YuangongxinziVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.YuangongxinziView;


/**
 * 员工薪资
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongxinziService extends IService<YuangongxinziEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YuangongxinziVO> selectListVO(QueryWrapper<YuangongxinziEntity> wrapper);
   	
   	YuangongxinziVO selectVO(@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);
   	
   	List<YuangongxinziView> selectListView(QueryWrapper<YuangongxinziEntity> wrapper);
   	
   	YuangongxinziView selectView(@Param("ew") QueryWrapper<YuangongxinziEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<YuangongxinziEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,QueryWrapper<YuangongxinziEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,QueryWrapper<YuangongxinziEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,QueryWrapper<YuangongxinziEntity> wrapper);



}

