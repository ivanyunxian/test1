
/**
 * 
 * 代码生成器自动生成[WFD_ROLECLASS]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_ROLECLASS",schema = "BDC_WORKFLOW")
public class Wfd_RoleClass {

	private String Roleclass_Id;
	private String Parentclassid;
	private String Class_Name;
	private Integer Class_Index;
	private String Class_Desc;

	@Id
	@Column(name = "ROLECLASS_ID", length = 400)
	public String getRoleclass_Id() {
		if (Roleclass_Id == null)
			Roleclass_Id = UUID.randomUUID().toString().replace("-", "");
		return Roleclass_Id;
	}

	public void setRoleclass_Id(String Roleclass_Id) {
		this.Roleclass_Id = Roleclass_Id;
	}

	@Column(name = "PARENTCLASSID", length = 400)
	public String getParentclassid() {
		return Parentclassid;
	}

	public void setParentclassid(String Parentclassid) {
		this.Parentclassid = Parentclassid;
	}

	@Column(name = "CLASS_NAME", length = 200)
	public String getClass_Name() {
		return Class_Name;
	}

	public void setClass_Name(String Class_Name) {
		this.Class_Name = Class_Name;
	}

	@Column(name = "CLASS_INDEX")
	public Integer getClass_Index() {
		return Class_Index;
	}

	public void setClass_Index(Integer Class_Index) {
		this.Class_Index = Class_Index;
	}

	@Column(name = "CLASS_DESC", length = 600)
	public String getClass_Desc() {
		return Class_Desc;
	}

	public void setClass_Desc(String Class_Desc) {
		this.Class_Desc = Class_Desc;
	}

}
