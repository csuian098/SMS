package com.yb.entity.vo;

import com.yb.entity.YuangongxinziEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 员工薪资
 * @author 
 * @email 
 * @date 2026-01-30 23:21:48
 */
public class YuangongxinziVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 工号
	 */
	
	private String gonghao;
		
	/**
	 * 请假天数
	 */
	
	private Integer qingjiatianshu;
		
	/**
	 * 基本工资
	 */
	
	private Double jibengongzi;
		
	/**
	 * 加班工资
	 */
	
	private Double jiabangongzi;
		
	/**
	 * 绩效奖金
	 */
	
	private Double jixiaojiangjin;
		
	/**
	 * 岗位补贴
	 */
	
	private Double gangweibutie;
		
	/**
	 * 扣款金额
	 */
	
	private Double koukuanjine;
		
	/**
	 * 假期扣薪
	 */
	
	private Double jiaqikouxin;

	private Double qingjiakouxin;

	private Integer weiqiandaotianshu;

	private Double weiqiandaokouxin;
		
	/**
	 * 实发工资
	 */
	
	private Double shifagongzi;
		
	/**
	 * 扣款原因
	 */
	
	private String koukuanyuanyin;
		
	/**
	 * 登记日期
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date dengjiriqi;
		
	/**
	 * 管理账号
	 */
	
	private String guanlizhanghao;
		
	/**
	 * 管理姓名
	 */
	
	private String guanlixingming;
		
	/**
	 * 是否审核
	 */
	
	private String sfsh;
		
	/**
	 * 审核回复
	 */
	
	private String shhf;
		
	/**
	 * 是否支付
	 */
	
	private String ispay;
				
	
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
	 * 设置：请假天数
	 */
	 
	public void setQingjiatianshu(Integer qingjiatianshu) {
		this.qingjiatianshu = qingjiatianshu;
	}
	
	/**
	 * 获取：请假天数
	 */
	public Integer getQingjiatianshu() {
		return qingjiatianshu;
	}
				
	
	/**
	 * 设置：基本工资
	 */
	 
	public void setJibengongzi(Double jibengongzi) {
		this.jibengongzi = jibengongzi;
	}
	
	/**
	 * 获取：基本工资
	 */
	public Double getJibengongzi() {
		return jibengongzi;
	}
				
	
	/**
	 * 设置：加班工资
	 */
	 
	public void setJiabangongzi(Double jiabangongzi) {
		this.jiabangongzi = jiabangongzi;
	}
	
	/**
	 * 获取：加班工资
	 */
	public Double getJiabangongzi() {
		return jiabangongzi;
	}
				
	
	/**
	 * 设置：绩效奖金
	 */
	 
	public void setJixiaojiangjin(Double jixiaojiangjin) {
		this.jixiaojiangjin = jixiaojiangjin;
	}
	
	/**
	 * 获取：绩效奖金
	 */
	public Double getJixiaojiangjin() {
		return jixiaojiangjin;
	}
				
	
	/**
	 * 设置：岗位补贴
	 */
	 
	public void setGangweibutie(Double gangweibutie) {
		this.gangweibutie = gangweibutie;
	}
	
	/**
	 * 获取：岗位补贴
	 */
	public Double getGangweibutie() {
		return gangweibutie;
	}
				
	
	/**
	 * 设置：扣款金额
	 */
	 
	public void setKoukuanjine(Double koukuanjine) {
		this.koukuanjine = koukuanjine;
	}
	
	/**
	 * 获取：扣款金额
	 */
	public Double getKoukuanjine() {
		return koukuanjine;
	}
				
	
	/**
	 * 设置：假期扣薪
	 */
	 
	public void setJiaqikouxin(Double jiaqikouxin) {
		this.jiaqikouxin = jiaqikouxin;
	}
	
	/**
	 * 获取：假期扣薪
	 */
	public Double getJiaqikouxin() {
		return jiaqikouxin;
	}

	public Double getQingjiakouxin() {
		return qingjiakouxin;
	}

	public void setQingjiakouxin(Double qingjiakouxin) {
		this.qingjiakouxin = qingjiakouxin;
	}

	public Integer getWeiqiandaotianshu() {
		return weiqiandaotianshu;
	}

	public void setWeiqiandaotianshu(Integer weiqiandaotianshu) {
		this.weiqiandaotianshu = weiqiandaotianshu;
	}

	public Double getWeiqiandaokouxin() {
		return weiqiandaokouxin;
	}

	public void setWeiqiandaokouxin(Double weiqiandaokouxin) {
		this.weiqiandaokouxin = weiqiandaokouxin;
	}
				
	
	/**
	 * 设置：实发工资
	 */
	 
	public void setShifagongzi(Double shifagongzi) {
		this.shifagongzi = shifagongzi;
	}
	
	/**
	 * 获取：实发工资
	 */
	public Double getShifagongzi() {
		return shifagongzi;
	}
				
	
	/**
	 * 设置：扣款原因
	 */
	 
	public void setKoukuanyuanyin(String koukuanyuanyin) {
		this.koukuanyuanyin = koukuanyuanyin;
	}
	
	/**
	 * 获取：扣款原因
	 */
	public String getKoukuanyuanyin() {
		return koukuanyuanyin;
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
				
	
	/**
	 * 设置：是否审核
	 */
	 
	public void setSfsh(String sfsh) {
		this.sfsh = sfsh;
	}
	
	/**
	 * 获取：是否审核
	 */
	public String getSfsh() {
		return sfsh;
	}
				
	
	/**
	 * 设置：审核回复
	 */
	 
	public void setShhf(String shhf) {
		this.shhf = shhf;
	}
	
	/**
	 * 获取：审核回复
	 */
	public String getShhf() {
		return shhf;
	}
				
	
	/**
	 * 设置：是否支付
	 */
	 
	public void setIspay(String ispay) {
		this.ispay = ispay;
	}
	
	/**
	 * 获取：是否支付
	 */
	public String getIspay() {
		return ispay;
	}
			
}
