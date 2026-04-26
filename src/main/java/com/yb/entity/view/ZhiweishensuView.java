package com.yb.entity.view;

import com.yb.entity.ZhiweishensuEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.yb.utils.EncryptUtil;
 

/**
 * 职位申诉
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@TableName("zhiweishensu")
public class ZhiweishensuView  extends ZhiweishensuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public ZhiweishensuView(){
	}
 
 	public ZhiweishensuView(ZhiweishensuEntity zhiweishensuEntity){
 	try {
			BeanUtils.copyProperties(zhiweishensuEntity,this);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
