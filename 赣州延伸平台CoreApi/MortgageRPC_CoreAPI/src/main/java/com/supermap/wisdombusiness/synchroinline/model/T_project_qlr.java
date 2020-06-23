package com.supermap.wisdombusiness.synchroinline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 前置机项目权利人关系表
 * 
 * @author duanyf
 *
 */
@Entity
@Table(name = "T_project_qlr", schema = "inline_inner")
public class T_project_qlr
{
	@Column
	@Id
	protected String id;
	@Column
	protected String xmbh;
	@Column
	protected String ywh;
	@Column
	protected String qlr;
	@Column
	protected String zjh;
	@Column
	protected String zjlx;
	@Column
	protected int zt;

	// 编号 id
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	// 项目编号 项目编号
	public String getXmbh()
	{
		return xmbh;
	}

	public void setXmbh(String xmbh)
	{
		this.xmbh = xmbh;
	}

	// 业务号 业务流水号
	public String getYwh()
	{
		return ywh;
	}

	public void setYwh(String ywh)
	{
		this.ywh = ywh;
	}

	// 权利人 权利人
	public String getQlr()
	{
		return qlr;
	}

	public void setQlr(String qlr)
	{
		this.qlr = qlr;
	}

	// 证件号 证件号
	public String getZjh()
	{
		return zjh;
	}

	public void setZjh(String zjh)
	{
		this.zjh = zjh;
	}

	// 证件类型 证件类型
	public String getZjlx()
	{
		return zjlx;
	}

	public void setZjlx(String zjlx)
	{
		this.zjlx = zjlx;
	}

	// 同步状态 0：未同步到外网，1：已同步到外网
	public int getZt()
	{
		return zt;
	}

	public void setZt(int zt)
	{
		this.zt = zt;
	}

}
