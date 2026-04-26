package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.RenshiguanliyuanEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.RenshiguanliyuanVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.RenshiguanliyuanView;


/**
 * 人事管理员
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface RenshiguanliyuanService extends IService<RenshiguanliyuanEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<RenshiguanliyuanVO> selectListVO(QueryWrapper<RenshiguanliyuanEntity> wrapper);
   	
   	RenshiguanliyuanVO selectVO(@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);
   	
   	List<RenshiguanliyuanView> selectListView(QueryWrapper<RenshiguanliyuanEntity> wrapper);
   	
   	RenshiguanliyuanView selectView(@Param("ew") QueryWrapper<RenshiguanliyuanEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<RenshiguanliyuanEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,QueryWrapper<RenshiguanliyuanEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,QueryWrapper<RenshiguanliyuanEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,QueryWrapper<RenshiguanliyuanEntity> wrapper);



}

