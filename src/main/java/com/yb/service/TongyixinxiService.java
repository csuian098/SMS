package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.TongyixinxiEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.TongyixinxiVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.TongyixinxiView;


/**
 * 同意信息
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface TongyixinxiService extends IService<TongyixinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<TongyixinxiVO> selectListVO(QueryWrapper<TongyixinxiEntity> wrapper);
   	
   	TongyixinxiVO selectVO(@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);
   	
   	List<TongyixinxiView> selectListView(QueryWrapper<TongyixinxiEntity> wrapper);
   	
   	TongyixinxiView selectView(@Param("ew") QueryWrapper<TongyixinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<TongyixinxiEntity> wrapper);

   	

}

