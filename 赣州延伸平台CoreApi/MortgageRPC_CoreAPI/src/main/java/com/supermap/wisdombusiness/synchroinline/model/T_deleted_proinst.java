package com.supermap.wisdombusiness.synchroinline.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 前置机记录被删除流程实例表
 * @author Administrator
 *
 */
@Entity
@Table(name = "T_deleted_proinst", schema = "inline_inner")
public class T_deleted_proinst
{
	@Column
	@Id
	protected String proinst_id;
	@Column
	protected String del_msg;
	@Column
	protected String del_uid;
	@Column
	protected String del_uname;
	@Column
	protected Date tbrq;
	@Column
	protected int is_del;

	/**
	 * @return the proinst_id
	 */
	public String getProinst_id()
	{
		return proinst_id;
	}

	/**
	 * @param proinst_id
	 *            the proinst_id to set
	 */
	public void setProinst_id(String proinst_id)
	{
		this.proinst_id = proinst_id;
	}

	/**
	 * @return the del_msg
	 */
	public String getDel_msg()
	{
		return del_msg;
	}

	/**
	 * @param del_msg
	 *            the del_msg to set
	 */
	public void setDel_msg(String del_msg)
	{
		this.del_msg = del_msg;
	}

	/**
	 * @return the del_uid
	 */
	public String getDel_uid()
	{
		return del_uid;
	}

	/**
	 * @param del_uid
	 *            the del_uid to set
	 */
	public void setDel_uid(String del_uid)
	{
		this.del_uid = del_uid;
	}

	/**
	 * @return the del_uname
	 */
	public String getDel_uname()
	{
		return del_uname;
	}

	/**
	 * @param del_uname
	 *            the del_uname to set
	 */
	public void setDel_uname(String del_uname)
	{
		this.del_uname = del_uname;
	}

	/**
	 * @return the tbrq
	 */
	public Date getTbrq()
	{
		return tbrq;
	}

	/**
	 * @param tbrq
	 *            the tbrq to set
	 */
	public void setTbrq(Date tbrq)
	{
		this.tbrq = tbrq;
	}

	/**
	 * @return the is_del
	 */
	public int getIs_del()
	{
		return is_del;
	}

	/**
	 * @param is_del the is_del to set
	 */
	public void setIs_del(int is_del)
	{
		this.is_del = is_del;
	}
	

}
