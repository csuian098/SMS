package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.ZhiweishensuEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.ZhiweishensuVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.ZhiweishensuView;


/**
 * 职位申诉
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface ZhiweishensuService extends IService<ZhiweishensuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ZhiweishensuVO> selectListVO(QueryWrapper<ZhiweishensuEntity> wrapper);
   	
   	ZhiweishensuVO selectVO(@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);
   	
   	List<ZhiweishensuView> selectListView(QueryWrapper<ZhiweishensuEntity> wrapper);
   	
   	ZhiweishensuView selectView(@Param("ew") QueryWrapper<ZhiweishensuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<ZhiweishensuEntity> wrapper);

   	

}

