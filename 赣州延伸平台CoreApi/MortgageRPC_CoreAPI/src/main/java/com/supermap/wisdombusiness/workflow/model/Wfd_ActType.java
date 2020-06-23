
/**
 * 
 * 代码生成器自动生成[WFD_ACTTYPE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_ACTTYPE",schema = "BDC_WORKFLOW")
public class Wfd_ActType {

	private String Acttype_Id;
	private Integer Acttype_Type;
	private String Acttype_Name;
	private String Actdef_Id;

	@Id
	@Column(name = "ACTTYPE_ID", length = 400)
	public String getActtype_Id() {
		if (Acttype_Id == null)
			Acttype_Id = UUID.randomUUID().toString().replace("-", "");
		return Acttype_Id;
	}

	public void setActtype_Id(String Acttype_Id) {
		this.Acttype_Id = Acttype_Id;
	}

	@Column(name = "ACTTYPE_TYPE")
	public Integer getActtype_Type() {
		return Acttype_Type;
	}

	public void setActtype_Type(Integer Acttype_Type) {
		this.Acttype_Type = Acttype_Type;
	}

	@Column(name = "ACTTYPE_NAME", length = 600)
	public String getActtype_Name() {
		return Acttype_Name;
	}

	public void setActtype_Name(String Acttype_Name) {
		this.Acttype_Name = Acttype_Name;
	}

	@Column(name = "ACTDEF_ID", length = 800)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

}
