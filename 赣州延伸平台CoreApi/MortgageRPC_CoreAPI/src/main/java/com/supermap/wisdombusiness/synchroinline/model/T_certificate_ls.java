package com.supermap.wisdombusiness.synchroinline.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_certificate_ls", schema = "inline_inner")
public class T_certificate_ls {
	 @Column
	 @Id
	  protected String id;
	 @Column
	  protected String bdcqzh;
	 @Column
	  protected String zsbh;
	 @Column
	  protected Date tbsj;
	 @Column
	  protected int is_del;
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getBdcqzh()
	{
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh)
	{
		this.bdcqzh = bdcqzh;
	}
	public String getZsbh()
	{
		return zsbh;
	}
	public void setZsbh(String zsbh)
	{
		this.zsbh = zsbh;
	}
	public Date getTbsj()
	{
		return tbsj;
	}
	public void setTbsj(Date tbsj)
	{
		this.tbsj = tbsj;
	}
	public int getIs_del()
	{
		return is_del;
	}
	public void setIs_del(int is_del)
	{
		this.is_del = is_del;
	}
	 
}
