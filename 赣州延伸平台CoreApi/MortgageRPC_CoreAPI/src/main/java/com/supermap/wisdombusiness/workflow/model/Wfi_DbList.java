
/**
 * 
 * 代码生成器自动生成[WFI_ACTINST]
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
@Table(name = "WFI_DBLIST",schema = "BDC_WORKFLOW")
public class Wfi_DbList {

	private String Id;
	private String StaffId;
	private String StaffName;
	private String Actinst_Id;
	private String Proinst_Id;
    private Date   Start_Time;
    private Date   End_Time;
    private String Msg;
    
	@Id
	@Column(name = "ID", length = 50)
	public String getId() {
		if (Id == null)
			Id = UUID.randomUUID().toString().replace("-", "");
		return Id;
	}
	public void setId(String id) {
		id = Id;
	}
	
	@Column(name = "STAFFID", length = 50)
	public String getStaffId() {
		return StaffId;
	}

	public void setStaffId(String staffId) {
		StaffId = staffId;
	}
	@Column(name = "STAFFNAME", length = 50)
	public String getStaffName() {
		return StaffName;
	}

	public void setStaffName(String staffName) {
		StaffName = staffName;
	}
	@Column(name = "ACTINST_ID", length = 50)
	public String getActinst_Id() {
		return Actinst_Id;
	}

	public void setActinst_Id(String actinst_Id) {
		Actinst_Id = actinst_Id;
	}
	@Column(name = "PROINST_ID", length = 50)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String proinst_Id) {
		Proinst_Id = proinst_Id;
	}
	@Column(name = "START_TIME")
	public Date getStart_Time() {
		return Start_Time;
	}

	public void setStart_Time(Date start_Time) {
		Start_Time = start_Time;
	}
	@Column(name = "END_TIME")
	public Date getEnd_Time() {
		return End_Time;
	}

	public void setEnd_Time(Date end_Time) {
		End_Time = end_Time;
	}

	@Column(name = "MSG", length = 200)
	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}
	
}
