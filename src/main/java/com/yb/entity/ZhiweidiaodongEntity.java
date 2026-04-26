package com.yb.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import com.baomidou.mybatisplus.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


/**
 * 职位调动
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@TableName("zhiweidiaodong")
public class ZhiweidiaodongEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public ZhiweidiaodongEntity() {
		
	}
	
	public ZhiweidiaodongEntity(T t) {
		try {
			BeanUtils.copyProperties(t,this);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * 主键id
	 */
    @TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 姓名
	 */
					
	private String xingming;
	
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
	@TableField(exist = false)
	private String zhuangtai;
	
	/**
	 * 变动日期
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：姓名
	 */
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	/**
	 * 获取：姓名
	 */
	public String getXingming() {
		return xingming;
	}
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

	public String getZhuangtai() {
		return zhuangtai;
	}

	public void setZhuangtai(String zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
}
