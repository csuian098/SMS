package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.QingjiashenqingEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.QingjiashenqingVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.QingjiashenqingView;


/**
 * 请假申请
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public interface QingjiashenqingService extends IService<QingjiashenqingEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<QingjiashenqingVO> selectListVO(QueryWrapper<QingjiashenqingEntity> wrapper);
   	
   	QingjiashenqingVO selectVO(@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);
   	
   	List<QingjiashenqingView> selectListView(QueryWrapper<QingjiashenqingEntity> wrapper);
   	
   	QingjiashenqingView selectView(@Param("ew") QueryWrapper<QingjiashenqingEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<QingjiashenqingEntity> wrapper);

   	

}

