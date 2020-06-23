
/**
 * 
 * 代码生成器自动生成[WFD_HOLIDAY]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_HOLIDAY",schema = "BDC_WORKFLOW")
public class Wfd_Holiday {

	private String Holiday_Id;
	private String Holiday_Name;
	private Date Holiday_Startdate;
	private Date Holiday_Enddate;
	private String Holiday_Desc;
	private Integer Holiday_Status;
	private Integer Holiday_Type;

	@Id
	@Column(name = "HOLIDAY_ID", length = 400)
	public String getHoliday_Id() {
		if (Holiday_Id == null)
			Holiday_Id = UUID.randomUUID().toString().replace("-", "");
		return Holiday_Id;
	}

	public void setHoliday_Id(String Holiday_Id) {
		this.Holiday_Id = Holiday_Id;
	}

	@Column(name = "HOLIDAY_NAME", length = 2048)
	public String getHoliday_Name() {
		return Holiday_Name;
	}

	public void setHoliday_Name(String Holiday_Name) {
		this.Holiday_Name = Holiday_Name;
	}

	@Column(name = "HOLIDAY_STARTDATE", length = 7)
	public Date getHoliday_Startdate() {
		return Holiday_Startdate;
	}

	public void setHoliday_Startdate(Date Holiday_Startdate) {
		this.Holiday_Startdate = Holiday_Startdate;
	}

	@Column(name = "HOLIDAY_ENDDATE", length = 7)
	public Date getHoliday_Enddate() {
		return Holiday_Enddate;
	}

	public void setHoliday_Enddate(Date Holiday_Enddate) {
		this.Holiday_Enddate = Holiday_Enddate;
	}

	@Column(name = "HOLIDAY_DESC", length = 2048)
	public String getHoliday_Desc() {
		return Holiday_Desc;
	}

	public void setHoliday_Desc(String Holiday_Desc) {
		this.Holiday_Desc = Holiday_Desc;
	}

	@Column(name = "HOLIDAY_STATUS")
	public Integer getHoliday_Status() {
		return Holiday_Status;
	}

	public void setHoliday_Status(Integer Holiday_Status) {
		this.Holiday_Status = Holiday_Status;
	}

	@Column(name = "HOLIDAY_TYPE")
	public Integer getHoliday_Type() {
		return Holiday_Type;
	}

	public void setHoliday_Type(Integer Holiday_Type) {
		this.Holiday_Type = Holiday_Type;
	}

}
