package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.ZhiweidiaodongEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.ZhiweidiaodongVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.ZhiweidiaodongView;


/**
 * 职位调动
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface ZhiweidiaodongService extends IService<ZhiweidiaodongEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ZhiweidiaodongVO> selectListVO(QueryWrapper<ZhiweidiaodongEntity> wrapper);
   	
   	ZhiweidiaodongVO selectVO(@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);
   	
   	List<ZhiweidiaodongView> selectListView(QueryWrapper<ZhiweidiaodongEntity> wrapper);
   	
   	ZhiweidiaodongView selectView(@Param("ew") QueryWrapper<ZhiweidiaodongEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<ZhiweidiaodongEntity> wrapper);

   	

}

