package com.supermap.wisdombusiness.synchroinline.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 前置端权利人短信推送信息表
 * @author daunyf
 */
@Entity
@Table(name = "Inline_dxts", schema = "inline_inner")
public class Inline_dxts
{
	@Column
	@Id
	  protected String id;
	@Column
	  protected String ywlsh;
	@Column
	  protected String qlr;
	@Column
	  protected String qlr_tel;
	@Column
	  protected String activityname;
	@Column
	  protected Date prostart;
	@Column
	  protected Date proend;
	@Column
	  protected int tszt;
	@Column
	  protected Date tbsj;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	//业务流水号 业务流水号
	public String getYwlsh()
	{
		return ywlsh;
	}
	public void setYwlsh(String ywlsh)
	{
		this.ywlsh = ywlsh;
	}
	//权利人 权利人名称
	public String getQlr()
	{
		return qlr;
	}
	public void setQlr(String qlr)
	{
		this.qlr = qlr;
	}
	//权利人电话 权利人电话号码
	public String getQlr_tel()
	{
		return qlr_tel;
	}
	public void setQlr_tel(String qlr_tel)
	{
		this.qlr_tel = qlr_tel;
	}
	//推送环节 推送环节名称
	public String getActivityname()
	{
		return activityname;
	}
	public void setActivityname(String activityname)
	{
		this.activityname = activityname;
	}
	//环节开始时间 环节开始时间
	public Date getProstart()
	{
		return prostart;
	}
	public void setProstart(Date prostart)
	{
		this.prostart = prostart;
	}
	//环节结束时间 环节结束时间
	public Date getProend()
	{
		return proend;
	}
	public void setProend(Date proend)
	{
		this.proend = proend;
	}
	 //推送状态 推送状态，0：未推送，1：已推送
	public int getTszt()
	{
		return tszt;
	}
	public void setTszt(int tszt)
	{
		this.tszt = tszt;
	}
	//同步时间 信息同步至前置端的时间
	public Date getTbsj()
	{
		return tbsj;
	}
	public void setTbsj(Date tbsj)
	{
		this.tbsj = tbsj;
	}
}
