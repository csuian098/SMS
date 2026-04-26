package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.ZhiweixinxiEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.ZhiweixinxiVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.ZhiweixinxiView;


/**
 * 职位信息
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface ZhiweixinxiService extends IService<ZhiweixinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ZhiweixinxiVO> selectListVO(QueryWrapper<ZhiweixinxiEntity> wrapper);
   	
   	ZhiweixinxiVO selectVO(@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);
   	
   	List<ZhiweixinxiView> selectListView(QueryWrapper<ZhiweixinxiEntity> wrapper);
   	
   	ZhiweixinxiView selectView(@Param("ew") QueryWrapper<ZhiweixinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<ZhiweixinxiEntity> wrapper);

   	

}

