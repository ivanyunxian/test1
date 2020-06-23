package com.supermap.wisdombusiness.synchroinline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/* 附件资料 */
@Entity
@Table(name = "Pro_attachment")
public class Pro_attachment
{
	@Column
	@Id
	protected String id;
	@Column
	protected String name;
	@Column
	protected String path;
	@Column
	protected String suffix;
	@Column
	protected Date created;
	@Column
	protected String opby;
	@Column
	protected double status;
	@Column
	protected String datuminst_id;
	@Column
	protected String proinst_id;
	@Column
	private Long File_Index;//文件序号

	// 编号 ID
	public String getId()
	{
		return id;
	}

	public void setId(String value)
	{
		this.id = value;
	}

	// 资料名称 NAME
	public String getName()
	{
		return name;
	}

	public void setName(String value)
	{
		this.name = value;
	}

	// 路径 PATH
	public String getPath()
	{
		return path;
	}

	public void setPath(String value)
	{
		this.path = value;
	}

	// 扩展名 SUFFIX
	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String value)
	{
		this.suffix = value;
	}

	// 上传时间 CREATED
	public Date getCreated()
	{
		return created;
	}

	public void setCreated(Date value)
	{
		this.created = value;
	}

	// OPBY
	public String getOpby()
	{
		return opby;
	}

	public void setOpby(String value)
	{
		this.opby = value;
	}

	// 状态 STATUS
	public double getStatus()
	{
		return status;
	}

	public void setStatus(double value)
	{
		this.status = value;
	}

	// 模板实例编号 DATUMINST_ID
	public String getDatuminst_id()
	{
		return datuminst_id;
	}

	public void setDatuminst_id(String value)
	{
		this.datuminst_id = value;
	}

	/**
	 * @return the proinst_id
	 */
	public String getProinst_id()
	{
		return proinst_id;
	}

	/**
	 * @param proinst_id the proinst_id to set
	 */
	public void setProinst_id(String proinst_id)
	{
		this.proinst_id = proinst_id;
	}

	public Long getFile_Index() {
		return File_Index;
	}

	public void setFile_Index(Long file_Index) {
		File_Index = file_Index;
	}
}