package com.supermap.wisdombusiness.workflow.model;

import java.io.Serializable;
import java.util.UUID;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_TURNLIST",schema = "BDC_WORKFLOW")

public class Wfi_TurnList {

	private String TurnList_Id;
	private String ActInst_Id;
	private String FromStaffId;
	private String ToStaffId;
	private Date TurnDate;
	private String status;
	private String TurnMsg;
	
	public Wfi_TurnList(String turnList_Id, String actInst_Id,
			String fromStaffId, String toStaffId, Date turnDate ) {
		super();
		TurnList_Id = turnList_Id;
		ActInst_Id = actInst_Id;
		FromStaffId = fromStaffId;
		ToStaffId = toStaffId;
		TurnDate = turnDate;
	}
	public Wfi_TurnList () {
		
	}

	@Id
	@Column(name = "TURNLIST_ID", length = 400)
	public String getTurnList_Id() {
		if (TurnList_Id == null){
			TurnList_Id = UUID.randomUUID().toString().replace("-", "");
		}
		return TurnList_Id;
	}
	
	public void setTurnList_Id(String TurnList_Id) {
		this.TurnList_Id = TurnList_Id;
	}
	
	@Column(name = "ACTINST_ID", length = 400)
	public String getActInst_Id() {
		return ActInst_Id;
	}

	public void setActInst_Id(String ActInst_Id) {
		this.ActInst_Id = ActInst_Id;
	}
	
	@Column(name = "FROMSTAFFID", length = 400)
	public String getFromStaffId() {
		return FromStaffId;
	}

	public void setFromStaffId(String FromStaffId) {
		this.FromStaffId = FromStaffId;
	}
	
	@Column(name = "TOSTAFFID", length = 400)
	public String getToStaffId() {
		return ToStaffId;
	}

	public void setToStaffId(String ToStaffId) {
		this.ToStaffId = ToStaffId;
	}
	
	@Column(name = "TURNDATE", length = 400)
	public Date getTurnDate() {
		return TurnDate;
	}

	public void setTurnDate(Date TurnDate) {
		this.TurnDate = TurnDate;
	}
	
	@Column(name = "STATUS", length = 400)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "TURNMSG",length=1000)
	public String getTurnMsg() {
		return TurnMsg;
	}
	public void setTurnMsg(String turnMsg) {
		TurnMsg = turnMsg;
	}
	
	
	
	
}
