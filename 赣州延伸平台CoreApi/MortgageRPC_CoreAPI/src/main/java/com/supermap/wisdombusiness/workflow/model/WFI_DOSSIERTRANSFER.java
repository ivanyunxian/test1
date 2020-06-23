
/**
 * 
 * 代码生成器自动生成[WFI_DOSSIERTRANSFER]
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
@Table(name = "WFI_DOSSIERTRANSFER", schema = "BDC_WORKFLOW")
public class WFI_DOSSIERTRANSFER {

	private String Transferid;
	private String Fromstaff_Id;
	private String Fromstaff_Name;
	private String Fromdept_Name;
	private String Fromdept_Id;
	private String Tostaff_Id;
	private String Tostaff_Name;
	private String Todept_Name;
	private String Todept_Id;
	private Integer Send_Count;
	private Integer Receive_Count;
	private Date Send_Time;
	private Date Receive_Time;
	private Integer Status;
	private Integer Type;
	private String Ddh;
	private Integer Transfer_Type;
	private String TransferCode;

	@Id
	@Column(name = "TRANSFERID", length = 200)
	public String getTransferid() {
		if (Transferid == null)
			Transferid = UUID.randomUUID().toString().replace("-", "");
		return Transferid;
	}

	public void setTransferid(String Transferid) {
		this.Transferid = Transferid;
	}

	@Column(name = "FROMSTAFF_ID", length = 200)
	public String getFromstaff_Id() {
		return Fromstaff_Id;
	}

	public void setFromstaff_Id(String Fromstaff_Id) {
		this.Fromstaff_Id = Fromstaff_Id;
	}

	@Column(name = "FROMSTAFF_NAME", length = 200)
	public String getFromstaff_Name() {
		return Fromstaff_Name;
	}

	public void setFromstaff_Name(String Fromstaff_Name) {
		this.Fromstaff_Name = Fromstaff_Name;
	}

	@Column(name = "FROMDEPT_NAME", length = 200)
	public String getFromdept_Name() {
		return Fromdept_Name;
	}

	public void setFromdept_Name(String Fromdept_Name) {
		this.Fromdept_Name = Fromdept_Name;
	}

	@Column(name = "FROMDEPT_ID", length = 200)
	public String getFromdept_Id() {
		return Fromdept_Id;
	}

	public void setFromdept_Id(String Fromdept_Id) {
		this.Fromdept_Id = Fromdept_Id;
	}

	@Column(name = "TOSTAFF_ID", length = 200)
	public String getTostaff_Id() {
		return Tostaff_Id;
	}

	public void setTostaff_Id(String Tostaff_Id) {
		this.Tostaff_Id = Tostaff_Id;
	}

	@Column(name = "TOSTAFF_NAME", length = 200)
	public String getTostaff_Name() {
		return Tostaff_Name;
	}

	public void setTostaff_Name(String Tostaff_Name) {
		this.Tostaff_Name = Tostaff_Name;
	}

	@Column(name = "TODEPT_NAME", length = 200)
	public String getTodept_Name() {
		return Todept_Name;
	}

	public void setTodept_Name(String Todept_Name) {
		this.Todept_Name = Todept_Name;
	}

	@Column(name = "TODEPT_ID", length = 200)
	public String getTodept_Id() {
		return Todept_Id;
	}

	public void setTodept_Id(String Todept_Id) {
		this.Todept_Id = Todept_Id;
	}

	@Column(name = "SEND_COUNT")
	public Integer getSend_Count() {
		return Send_Count;
	}

	public void setSend_Count(Integer Send_Count) {
		this.Send_Count = Send_Count;
	}

	@Column(name = "RECEIVE_COUNT")
	public Integer getReceive_Count() {
		return Receive_Count;
	}

	public void setReceive_Count(Integer Receive_Count) {
		this.Receive_Count = Receive_Count;
	}

	@Column(name = "SEND_TIME", length = 7)
	public Date getSend_Time() {
		return Send_Time;
	}

	public void setSend_Time(Date Send_Time) {
		this.Send_Time = Send_Time;
	}

	@Column(name = "RECEIVE_TIME", length = 7)
	public Date getReceive_Time() {
		return Receive_Time;
	}

	public void setReceive_Time(Date Receive_Time) {
		this.Receive_Time = Receive_Time;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return Type;
	}

	public void setType(Integer Type) {
		this.Type = Type;
	}

	@Column(name = "DDH", length = 50)
	public String getDdh() {
		return Ddh;
	}

	public void setDdh(String dhh) {
		Ddh = dhh;
	}
	@Column(name = "TRANSFER_TYPE")
	public Integer getTransfer_Type() {
		return Transfer_Type;
	}

	public void setTransfer_Type(Integer transfer_Type) {
		Transfer_Type = transfer_Type;
	}
	
	@Column(name = "TRANSFERCODE")
	public String getTransferCode() {
		return TransferCode;
	}

	public void setTransferCode(String transferCode) {
		TransferCode = transferCode;
	}

}
