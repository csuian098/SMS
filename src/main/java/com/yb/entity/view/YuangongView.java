package com.yb.entity.view;

import com.yb.entity.YuangongEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.yb.utils.EncryptUtil;
 

/**
 * 员工
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@TableName("yuangong")
public class YuangongView  extends YuangongEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public YuangongView(){
	}
 
 	public YuangongView(YuangongEntity yuangongEntity){
 	try {
			BeanUtils.copyProperties(yuangongEntity,this);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
