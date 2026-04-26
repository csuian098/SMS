package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.YuangongkaoqinEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.YuangongkaoqinVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.YuangongkaoqinView;


/**
 * 员工考勤
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongkaoqinService extends IService<YuangongkaoqinEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YuangongkaoqinVO> selectListVO(QueryWrapper<YuangongkaoqinEntity> wrapper);
   	
   	YuangongkaoqinVO selectVO(@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);
   	
   	List<YuangongkaoqinView> selectListView(QueryWrapper<YuangongkaoqinEntity> wrapper);
   	
   	YuangongkaoqinView selectView(@Param("ew") QueryWrapper<YuangongkaoqinEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<YuangongkaoqinEntity> wrapper);

   	

}

