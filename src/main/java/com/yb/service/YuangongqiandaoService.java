package com.yb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.utils.PageUtils;
import com.yb.entity.YuangongqiandaoEntity;
import java.util.List;
import java.util.Map;
import com.yb.entity.vo.YuangongqiandaoVO;
import org.apache.ibatis.annotations.Param;
import com.yb.entity.view.YuangongqiandaoView;


/**
 * 员工签到
 *
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public interface YuangongqiandaoService extends IService<YuangongqiandaoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YuangongqiandaoVO> selectListVO(QueryWrapper<YuangongqiandaoEntity> wrapper);
   	
   	YuangongqiandaoVO selectVO(@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);
   	
   	List<YuangongqiandaoView> selectListView(QueryWrapper<YuangongqiandaoEntity> wrapper);
   	
   	YuangongqiandaoView selectView(@Param("ew") QueryWrapper<YuangongqiandaoEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,QueryWrapper<YuangongqiandaoEntity> wrapper);

   	

}

