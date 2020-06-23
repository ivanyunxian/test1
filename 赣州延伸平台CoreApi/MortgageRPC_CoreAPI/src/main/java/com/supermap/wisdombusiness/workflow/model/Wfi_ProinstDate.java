package com.supermap.wisdombusiness.workflow.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_PROINSTDATE",schema="BDC_WORKFLOW")
public class Wfi_ProinstDate {
	private String id;
	private String file_number;
	private Date SMSSendingTime;
	private Date actinst_start_time ;
	private Date actinst_end_time;
	private Date NetSignConfirmTime;
	private Date TaxConfirmTime;
	private Date dbEndTime;
	private Date fzStartTime;
	private Date fzEndTime;
	private String areaCode;
	
	private String NetAuditOpinions;
	
	
	@Id
	@Column(name = "ID")
	public String getId() {
		if(id ==null){
			id = UUID.randomUUID().toString().replace("-", "");
		}
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "FILE_NUMBER")
	public String getFile_number() {
		return file_number;
	}
	public void setFile_number(String file_number) {
		this.file_number = file_number;
	}
	
	@Column(name = "SMSSENDINGTIME")
	public Date getSMSSendingTime() {
		return SMSSendingTime;
	}
	public void setSMSSendingTime(Date sMSSendingTime) {
		SMSSendingTime = sMSSendingTime;
	}
	
	@Column(name = "ACTINST_START_TIME")
	public Date getActinst_start_time() {
		return actinst_start_time;
	}
	public void setActinst_start_time(Date actinst_start_time) {
		this.actinst_start_time = actinst_start_time;
	}
	
	@Column(name = "ACTINST_END_TIME")
	public Date getActinst_end_time() {
		return actinst_end_time;
	}
	public void setActinst_end_time(Date actinst_end_time) {
		this.actinst_end_time = actinst_end_time;
	}
	
	@Column(name = "NETSIGNCONFIRMTIME")
	public Date getNetSignConfirmTime() {
		return NetSignConfirmTime;
	}
	public void setNetSignConfirmTime(Date netSignConfirmTime) {
		NetSignConfirmTime = netSignConfirmTime;
	}
	
	@Column(name = "TAXCONFIRMTIME")
	public Date getTaxConfirmTime() {
		return TaxConfirmTime;
	}
	public void setTaxConfirmTime(Date taxConfirmTime) {
		TaxConfirmTime = taxConfirmTime;
	}
	
	@Column(name = "DBENDTIME")
	public Date getDbEndTime() {
		return dbEndTime;
	}
	public void setDbEndTime(Date dbEndTime) {
		this.dbEndTime = dbEndTime;
	}
	
	@Column(name = "FZSTARTTIME")
	public Date getFzStartTime() {
		return fzStartTime;
	}
	public void setFzStartTime(Date fzStartTime) {
		this.fzStartTime = fzStartTime;
	}
	
	@Column(name = "FZENDTIME")
	public Date getFzEndTime() {
		return fzEndTime;
	}
	public void setFzEndTime(Date fzEndTime) {
		this.fzEndTime = fzEndTime;
	}
	
	@Column(name = "AREACODE")
	public String getAreaCode() {
		return areaCode;
	}
	
	@Column(name = "NETAUDITOPINIONS")
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getNetAuditOpinions() {
		return NetAuditOpinions;
	}
	public void setNetAuditOpinions(String netAuditOpinions) {
		NetAuditOpinions = netAuditOpinions;
	}
	
	
	
	

}
