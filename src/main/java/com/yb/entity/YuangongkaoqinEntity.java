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
 * 员工考勤
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
@TableName("yuangongkaoqin")
public class YuangongkaoqinEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public YuangongkaoqinEntity() {
		
	}
	
	public YuangongkaoqinEntity(T t) {
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
	 * 登记编号
	 */
					
	private String dengjibianhao;
	
	/**
	 * 姓名
	 */
					
	private String xingming;
	
	/**
	 * 工号
	 */
					
	private String gonghao;
	
	/**
	 * 正常次数
	 */
					
	private Integer zhengchangcishu;
	
	/**
	 * 迟到次数
	 */
					
	private Integer chidaocishu;
	
	/**
	 * 早退次数
	 */
					
	private Integer zaotuicishu;
	
	/**
	 * 出差次数
	 */
					
	private Integer chuchacishu;
	
	/**
	 * 加班次数
	 */
					
	private Integer jiabancishu;
	
	/**
	 * 请假次数
	 */
					
	private Integer qingjiacishu;
	
	/**
	 * 旷工次数
	 */
					
	private Integer kuanggongcishu;
	
	/**
	 * 登记日期
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	@DateTimeFormat 		
	private Date dengjiriqi;
	
	/**
	 * 备注
	 */
					
	private String beizhu;
	
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
	 * 设置：登记编号
	 */
	public void setDengjibianhao(String dengjibianhao) {
		this.dengjibianhao = dengjibianhao;
	}
	/**
	 * 获取：登记编号
	 */
	public String getDengjibianhao() {
		return dengjibianhao;
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
	 * 设置：正常次数
	 */
	public void setZhengchangcishu(Integer zhengchangcishu) {
		this.zhengchangcishu = zhengchangcishu;
	}
	/**
	 * 获取：正常次数
	 */
	public Integer getZhengchangcishu() {
		return zhengchangcishu;
	}
	/**
	 * 设置：迟到次数
	 */
	public void setChidaocishu(Integer chidaocishu) {
		this.chidaocishu = chidaocishu;
	}
	/**
	 * 获取：迟到次数
	 */
	public Integer getChidaocishu() {
		return chidaocishu;
	}
	/**
	 * 设置：早退次数
	 */
	public void setZaotuicishu(Integer zaotuicishu) {
		this.zaotuicishu = zaotuicishu;
	}
	/**
	 * 获取：早退次数
	 */
	public Integer getZaotuicishu() {
		return zaotuicishu;
	}
	/**
	 * 设置：出差次数
	 */
	public void setChuchacishu(Integer chuchacishu) {
		this.chuchacishu = chuchacishu;
	}
	/**
	 * 获取：出差次数
	 */
	public Integer getChuchacishu() {
		return chuchacishu;
	}
	/**
	 * 设置：加班次数
	 */
	public void setJiabancishu(Integer jiabancishu) {
		this.jiabancishu = jiabancishu;
	}
	/**
	 * 获取：加班次数
	 */
	public Integer getJiabancishu() {
		return jiabancishu;
	}
	/**
	 * 设置：请假次数
	 */
	public void setQingjiacishu(Integer qingjiacishu) {
		this.qingjiacishu = qingjiacishu;
	}
	/**
	 * 获取：请假次数
	 */
	public Integer getQingjiacishu() {
		return qingjiacishu;
	}
	/**
	 * 设置：旷工次数
	 */
	public void setKuanggongcishu(Integer kuanggongcishu) {
		this.kuanggongcishu = kuanggongcishu;
	}
	/**
	 * 获取：旷工次数
	 */
	public Integer getKuanggongcishu() {
		return kuanggongcishu;
	}
	/**
	 * 设置：登记日期
	 */
	public void setDengjiriqi(Date dengjiriqi) {
		this.dengjiriqi = dengjiriqi;
	}
	/**
	 * 获取：登记日期
	 */
	public Date getDengjiriqi() {
		return dengjiriqi;
	}
	/**
	 * 设置：备注
	 */
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	/**
	 * 获取：备注
	 */
	public String getBeizhu() {
		return beizhu;
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
