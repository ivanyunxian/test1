
/**
 * 
 * 代码生成器自动生成[WFI_ACTINSTSTAFF]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_ACTINSTSTAFF",schema = "BDC_WORKFLOW")
public class Wfi_ActInstStaff {

	private String Actstaffid;
	private String Actinst_Id;
	private String Staff_Id;
	private String Role_Id;
	private String Staff_Name;

	@Id
	@Column(name = "ACTSTAFFID", length = 200)
	public String getActstaffid() {
		if (Actstaffid == null)
			Actstaffid = UUID.randomUUID().toString().replace("-", "");
		return Actstaffid;
	}

	public void setActstaffid(String Actstaffid) {
		this.Actstaffid = Actstaffid;
	}

	@Column(name = "ACTINST_ID", length = 200)
	public String getActinst_Id() {
		return Actinst_Id;
	}

	public void setActinst_Id(String Actinst_Id) {
		this.Actinst_Id = Actinst_Id;
	}

	@Column(name = "STAFF_ID", length = 200)
	public String getStaff_Id() {
		return Staff_Id;
	}

	public void setStaff_Id(String Staff_Id) {
		this.Staff_Id = Staff_Id;
	}

	@Column(name = "ROLE_ID", length = 200)
	public String getRole_Id() {
		return Role_Id;
	}

	public void setRole_Id(String Role_Id) {
		this.Role_Id = Role_Id;
	}

	@Column(name = "STAFF_NAME", length = 200)
	public String getStaff_Name() {
		return Staff_Name;
	}

	public void setStaff_Name(String Staff_Name) {
		this.Staff_Name = Staff_Name;
	}
}
