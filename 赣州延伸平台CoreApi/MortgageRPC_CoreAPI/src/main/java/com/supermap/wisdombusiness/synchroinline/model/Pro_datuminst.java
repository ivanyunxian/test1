package com.supermap.wisdombusiness.synchroinline.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 附件模板项目实例 */
@Entity
@Table(name = "Pro_datuminst")
public class Pro_datuminst
{
	@Column
	@Id
	protected String id;
	@Column
	protected String name;
	@Column
	protected String code;
	@Column
	protected int count;
	@Column
	protected String proinst_id;
	@Column
	protected String bz;
	@Column
	protected String required;

	// 编号 ID
	public String getId()
	{
		return id;
	}

	public void setId(String value)
	{
		this.id = value;
	}

	// 模板名称 NAME
	public String getName()
	{
		return name;
	}

	public void setName(String value)
	{
		this.name = value;
	}

	// 模板编号 CODE
	public String getCode()
	{
		return code;
	}

	public void setCode(String value)
	{
		this.code = value;
	}

	// 份数 COUNT
	public int getCount()
	{
		return count;
	}

	public void setCount(int value)
	{
		this.count = value;
	}

	// 项目编号 PROINST_ID
	public String getProinst_id()
	{
		return proinst_id;
	}

	public void setProinst_id(String value)
	{
		this.proinst_id = value;
	}

	// 备注
	public String getBz()
	{
		return bz;
	}

	public void setBz(String value)
	{
		this.bz = value;
	}

	/**
	 * @return the required
	 */
	public String getRequired()
	{
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(String required)
	{
		this.required = required;
	}
	
}
