package com.supermap.wisdombusiness.synchroinline.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_SCHEDULE", schema = "inline_inner")
public class T_SCHEDULE
{
	@Column
	@Id
	protected String id;
	@Column
	protected String serialnumber;
	@Column
	protected String processdefid;
	@Column
	protected String processname;
	@Column
	protected String activityname;
	@Column
	protected Date actstart;
	@Column
	protected Date actend;
	@Column
	protected String staffname;
	@Column
	protected int actstatus;
	@Column
	protected int issynchro;
	@Column
	protected String newprodefinfo;
	@Column
	protected String projectname;
	@Column
	protected String proinst_id;
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return the serialnumber
	 */
	public String getSerialnumber()
	{
		return serialnumber;
	}
	/**
	 * @param serialnumber the serialnumber to set
	 */
	public void setSerialnumber(String serialnumber)
	{
		this.serialnumber = serialnumber;
	}
	/**
	 * @return the processdefid
	 */
	public String getProcessdefid()
	{
		return processdefid;
	}
	/**
	 * @param processdefid the processdefid to set
	 */
	public void setProcessdefid(String processdefid)
	{
		this.processdefid = processdefid;
	}
	/**
	 * @return the processname
	 */
	public String getProcessname()
	{
		return processname;
	}
	/**
	 * @param processname the processname to set
	 */
	public void setProcessname(String processname)
	{
		this.processname = processname;
	}
	/**
	 * @return the activityname
	 */
	public String getActivityname()
	{
		return activityname;
	}
	/**
	 * @param activityname the activityname to set
	 */
	public void setActivityname(String activityname)
	{
		this.activityname = activityname;
	}
	/**
	 * @return the actstart
	 */
	public Date getActstart()
	{
		return actstart;
	}
	/**
	 * @param actstart the actstart to set
	 */
	public void setActstart(Date actstart)
	{
		this.actstart = actstart;
	}
	/**
	 * @return the actend
	 */
	public Date getActend()
	{
		return actend;
	}
	/**
	 * @param actend the actend to set
	 */
	public void setActend(Date actend)
	{
		this.actend = actend;
	}
	/**
	 * @return the staffname
	 */
	public String getStaffname()
	{
		return staffname;
	}
	/**
	 * @param staffname the staffname to set
	 */
	public void setStaffname(String staffname)
	{
		this.staffname = staffname;
	}
	/**
	 * @return the actstatus
	 */
	public int getActstatus()
	{
		return actstatus;
	}
	/**
	 * @param actstatus the actstatus to set
	 */
	public void setActstatus(int actstatus)
	{
		this.actstatus = actstatus;
	}
	/**
	 * @return the issynchro
	 */
	public int getIssynchro()
	{
		return issynchro;
	}
	/**
	 * @param issynchro the issynchro to set
	 */
	public void setIssynchro(int issynchro)
	{
		this.issynchro = issynchro;
	}
	/**
	 * @return the newprodefinfo
	 */
	public String getNewprodefinfo()
	{
		return newprodefinfo;
	}
	/**
	 * @param newprodefinfo the newprodefinfo to set
	 */
	public void setNewprodefinfo(String newprodefinfo)
	{
		this.newprodefinfo = newprodefinfo;
	}
	/**
	 * @return the projectname
	 */
	public String getProjectname()
	{
		return projectname;
	}
	/**
	 * @param projectname the projectname to set
	 */
	public void setProjectname(String projectname)
	{
		this.projectname = projectname;
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
	
	
}
