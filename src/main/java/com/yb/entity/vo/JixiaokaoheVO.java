package com.yb.entity.vo;

import com.yb.entity.JixiaokaoheEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 绩效考核
 * @author 
 * @email 
 * @date 2026-01-30 23:21:49
 */
public class JixiaokaoheVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 工号
	 */
	
	private String gonghao;
		
	/**
	 * 员工考勤
	 */
	
	private Integer yuangongkaoqin;
		
	/**
	 * 工作态度
	 */
	
	private Integer gongzuotaidu;
		
	/**
	 * 业务技能
	 */
	
	private Integer yewujineng;
		
	/**
	 * 工作绩效
	 */
	
	private Integer gongzuojixiao;
		
	/**
	 * 总得分
	 */
	
	private Integer zongdefen;
		
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
	 * 设置：员工考勤
	 */
	 
	public void setYuangongkaoqin(Integer yuangongkaoqin) {
		this.yuangongkaoqin = yuangongkaoqin;
	}
	
	/**
	 * 获取：员工考勤
	 */
	public Integer getYuangongkaoqin() {
		return yuangongkaoqin;
	}
				
	
	/**
	 * 设置：工作态度
	 */
	 
	public void setGongzuotaidu(Integer gongzuotaidu) {
		this.gongzuotaidu = gongzuotaidu;
	}
	
	/**
	 * 获取：工作态度
	 */
	public Integer getGongzuotaidu() {
		return gongzuotaidu;
	}
				
	
	/**
	 * 设置：业务技能
	 */
	 
	public void setYewujineng(Integer yewujineng) {
		this.yewujineng = yewujineng;
	}
	
	/**
	 * 获取：业务技能
	 */
	public Integer getYewujineng() {
		return yewujineng;
	}
				
	
	/**
	 * 设置：工作绩效
	 */
	 
	public void setGongzuojixiao(Integer gongzuojixiao) {
		this.gongzuojixiao = gongzuojixiao;
	}
	
	/**
	 * 获取：工作绩效
	 */
	public Integer getGongzuojixiao() {
		return gongzuojixiao;
	}
				
	
	/**
	 * 设置：总得分
	 */
	 
	public void setZongdefen(Integer zongdefen) {
		this.zongdefen = zongdefen;
	}
	
	/**
	 * 获取：总得分
	 */
	public Integer getZongdefen() {
		return zongdefen;
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
			
}
