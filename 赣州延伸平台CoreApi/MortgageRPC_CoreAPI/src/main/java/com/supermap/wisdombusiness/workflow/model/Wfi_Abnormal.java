
/**
* 
* 代码生成器自动生成[WFI_ABNORMAL]
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
@Table(name = "WFI_ABNORMAL", schema = "BDC_WORKFLOW")
public class Wfi_Abnormal {

	private String Abnormal_Id;
	private Integer Operation_Type;
	private String Operation_Msg;
	private String Staff_Id;
	private String Staff_Name;
	private String Tostaff_Id;
	private String Proinst_Id;
	private Date Opeartion_Date;
	private String Actinst_Id;

	private String FJ;

	@Id
	@Column(name = "ABNORMAL_ID", length = 400)
	public String getAbnormal_Id() {
		if (Abnormal_Id == null)
			Abnormal_Id = UUID.randomUUID().toString().replace("-", "");
		return Abnormal_Id;
	}

	public void setAbnormal_Id(String Abnormal_Id) {
		this.Abnormal_Id = Abnormal_Id;
	}

	@Column(name = "OPERATION_TYPE")
	public Integer getOperation_Type() {
		return Operation_Type;
	}

	public void setOperation_Type(Integer Operation_Type) {
		this.Operation_Type = Operation_Type;
	}

	@Column(name = "OPERATION_MSG", length = 400)
	public String getOperation_Msg() {
		return Operation_Msg;
	}

	public void setOperation_Msg(String Operation_Msg) {
		this.Operation_Msg = Operation_Msg;
	}

	@Column(name = "STAFF_ID", length = 200)
	public String getStaff_Id() {
		return Staff_Id;
	}

	public void setStaff_Id(String Staff_Id) {
		this.Staff_Id = Staff_Id;
	}

	@Column(name = "STAFF_NAME", length = 200)
	public String getStaff_Name() {
		return Staff_Name;
	}

	public void setStaff_Name(String Staff_Name) {
		this.Staff_Name = Staff_Name;
	}

	public String getTostaff_Id() {
		return Tostaff_Id;
	}

	public void setTostaff_Id(String Tostaff_Id) {
		this.Tostaff_Id = Tostaff_Id;
	}

	@Column(name = "PROINST_ID", length = 200)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "OPEARTION_DATE", length = 7)
	public Date getOpeartion_Date() {
		return Opeartion_Date;
	}

	public void setOpeartion_Date(Date Opeartion_Date) {
		this.Opeartion_Date = Opeartion_Date;
	}

	@Column(name = "ACTINST_ID", length = 400)
	public String getActinst_Id() {
		return Actinst_Id;
	}

	public void setActinst_Id(String Actinst_Id) {
		this.Actinst_Id = Actinst_Id;
	}

	@Column(name = "FJ", length = 2000)
	public String getFJ() {
		return FJ;
	}

	public void setFJ(String fJ) {
		FJ = fJ;
	}

}
