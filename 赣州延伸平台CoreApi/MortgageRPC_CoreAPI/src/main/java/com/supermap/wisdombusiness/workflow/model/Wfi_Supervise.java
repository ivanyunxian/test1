
/**
 * 
 * 代码生成器自动生成[WFI_SUPERVISE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_SUPERVISE",schema = "BDC_WORKFLOW")
public class Wfi_Supervise {

	private String Supervise_Id;
	private String Proinst_Id;
	private String Actinst_Id;
	private String Supervise_Staffid;
	private String Supervise_Staffname;
	private String Supervise_Msg;

	@Id
	@Column(name = "SUPERVISE_ID", length = 400)
	public String getSupervise_Id() {
		if (Supervise_Id == null)
			Supervise_Id = UUID.randomUUID().toString().replace("-", "");
		return Supervise_Id;
	}

	public void setSupervise_Id(String Supervise_Id) {
		this.Supervise_Id = Supervise_Id;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "ACTINST_ID", length = 400)
	public String getActinst_Id() {
		return Actinst_Id;
	}

	public void setActinst_Id(String Actinst_Id) {
		this.Actinst_Id = Actinst_Id;
	}

	@Column(name = "SUPERVISE_STAFFID", length = 400)
	public String getSupervise_Staffid() {
		return Supervise_Staffid;
	}

	public void setSupervise_Staffid(String Supervise_Staffid) {
		this.Supervise_Staffid = Supervise_Staffid;
	}

	@Column(name = "SUPERVISE_STAFFNAME", length = 600)
	public String getSupervise_Staffname() {
		return Supervise_Staffname;
	}

	public void setSupervise_Staffname(String Supervise_Staffname) {
		this.Supervise_Staffname = Supervise_Staffname;
	}

	@Column(name = "SUPERVISE_MSG", length = 600)
	public String getSupervise_Msg() {
		return Supervise_Msg;
	}

	public void setSupervise_Msg(String Supervise_Msg) {
		this.Supervise_Msg = Supervise_Msg;
	}

}
