
/**
 * 
 * 代码生成器自动生成[WFD_ROLE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_ROLE",schema = "BDC_WORKFLOW")
public class Wfd_Role {

	private String Role_Id;
	private String Role_Name;
	private String Role_Sign;
	private Integer Role_Type;
	private Integer Role_Status;
	private Integer Role_Index;
	private String Role_Desc;
	private String Roleclass_Id;

	@Id
	@Column(name = "ROLE_ID", length = 400)
	public String getRole_Id() {
		if (Role_Id == null)
			Role_Id = UUID.randomUUID().toString().replace("-", "");
		return Role_Id;
	}

	public void setRole_Id(String Role_Id) {
		this.Role_Id = Role_Id;
	}

	@Column(name = "ROLE_NAME", length = 200)
	public String getRole_Name() {
		return Role_Name;
	}

	public void setRole_Name(String Role_Name) {
		this.Role_Name = Role_Name;
	}

	@Column(name = "ROLE_SIGN", length = 200)
	public String getRole_Sign() {
		return Role_Sign;
	}

	public void setRole_Sign(String Role_Sign) {
		this.Role_Sign = Role_Sign;
	}

	@Column(name = "ROLE_TYPE")
	public Integer getRole_Type() {
		return Role_Type;
	}

	public void setRole_Type(Integer Role_Type) {
		this.Role_Type = Role_Type;
	}

	@Column(name = "ROLE_STATUS")
	public Integer getRole_Status() {
		return Role_Status;
	}

	public void setRole_Status(Integer Role_Status) {
		this.Role_Status = Role_Status;
	}

	@Column(name = "ROLE_INDEX")
	public Integer getRole_Index() {
		return Role_Index;
	}

	public void setRole_Index(Integer Role_Index) {
		this.Role_Index = Role_Index;
	}

	@Column(name = "ROLE_DESC", length = 600)
	public String getRole_Desc() {
		return Role_Desc;
	}

	public void setRole_Desc(String Role_Desc) {
		this.Role_Desc = Role_Desc;
	}

	@Column(name = "ROLECLASS_ID", length = 400)
	public String getRoleclass_Id() {
		return Roleclass_Id;
	}

	public void setRoleclass_Id(String Roleclass_Id) {
		this.Roleclass_Id = Roleclass_Id;
	}

}
