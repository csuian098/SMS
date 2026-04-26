package com.yb.entity.vo;

import com.yb.entity.ZhiweidiaodongEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 职位调动
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public class ZhiweidiaodongVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 工号
	 */
	
	private String gonghao;
		
	/**
	 * 原职位
	 */
	
	private String zhiwei;
		
	/**
	 * 现职位
	 */
	
	private String xianzhiwei;
		
	/**
	 * 变动原因
	 */
	
	private String biandongyuanyin;
		
	/**
	 * 变动日期
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date biandongriqi;
		
	/**
	 * 管理账号
	 */
	
	private String guanlizhanghao;
		
	/**
	 * 管理姓名
	 */
	
	private String guanlixingming;
				
	
	/**
	 * 设置：工号
	 */
	 
	public void setGonghao(String gonghao) {
		this.gonghao = gonghao;
	}
	
	/**
	 * 获取：工号
	 */
	public String getGonghao() {
		return gonghao;
	}
				
	
	/**
	 * 设置：原职位
	 */
	 
	public void setZhiwei(String zhiwei) {
		this.zhiwei = zhiwei;
	}
	
	/**
	 * 获取：原职位
	 */
	public String getZhiwei() {
		return zhiwei;
	}
				
	
	/**
	 * 设置：现职位
	 */
	 
	public void setXianzhiwei(String xianzhiwei) {
		this.xianzhiwei = xianzhiwei;
	}
	
	/**
	 * 获取：现职位
	 */
	public String getXianzhiwei() {
		return xianzhiwei;
	}
				
	
	/**
	 * 设置：变动原因
	 */
	 
	public void setBiandongyuanyin(String biandongyuanyin) {
		this.biandongyuanyin = biandongyuanyin;
	}
	
	/**
	 * 获取：变动原因
	 */
	public String getBiandongyuanyin() {
		return biandongyuanyin;
	}
				
	
	/**
	 * 设置：变动日期
	 */
	 
	public void setBiandongriqi(Date biandongriqi) {
		this.biandongriqi = biandongriqi;
	}
	
	/**
	 * 获取：变动日期
	 */
	public Date getBiandongriqi() {
		return biandongriqi;
	}
				
	
	/**
	 * 设置：管理账号
	 */
	 
	public void setGuanlizhanghao(String guanlizhanghao) {
		this.guanlizhanghao = guanlizhanghao;
	}
	
	/**
	 * 获取：管理账号
	 */
	public String getGuanlizhanghao() {
		return guanlizhanghao;
	}
				
	
	/**
	 * 设置：管理姓名
	 */
	 
	public void setGuanlixingming(String guanlixingming) {
		this.guanlixingming = guanlixingming;
	}
	
	/**
	 * 获取：管理姓名
	 */
	public String getGuanlixingming() {
		return guanlixingming;
	}
			
}
