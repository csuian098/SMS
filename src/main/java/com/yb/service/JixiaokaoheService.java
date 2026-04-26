package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.JixiaokaoheEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.JixiaokaoheVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.JixiaokaoheView;


/**
 * 绩效考核
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface JixiaokaoheService extends IService<JixiaokaoheEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<JixiaokaoheVO> selectListVO(QueryWrapper<JixiaokaoheEntity> wrapper);
   	
   	JixiaokaoheVO selectVO(@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);
   	
   	List<JixiaokaoheView> selectListView(QueryWrapper<JixiaokaoheEntity> wrapper);
   	
   	JixiaokaoheView selectView(@Param("ew") QueryWrapper<JixiaokaoheEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<JixiaokaoheEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,QueryWrapper<JixiaokaoheEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,QueryWrapper<JixiaokaoheEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,QueryWrapper<JixiaokaoheEntity> wrapper);



}

