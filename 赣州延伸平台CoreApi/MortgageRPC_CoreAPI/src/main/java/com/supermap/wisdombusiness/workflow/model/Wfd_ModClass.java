
/**
 * 
 * 代码生成器自动生成[WFD_MODCLASS]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_MODCLASS",schema = "BDC_WORKFLOW")
public class Wfd_ModClass {

	private String Modclass_Id;
	private String Modclass_Name;
	private String Parentclass_Id;
	private Integer Modclass_Index;
	private String Modclass_Desc;

	@Id
	@Column(name = "MODCLASS_ID", length = 400)
	public String getModclass_Id() {
		if (Modclass_Id == null)
			Modclass_Id = UUID.randomUUID().toString().replace("-", "");
		return Modclass_Id;
	}

	public void setModclass_Id(String Modclass_Id) {
		this.Modclass_Id = Modclass_Id;
	}

	@Column(name = "MODCLASS_NAME", length = 200)
	public String getModclass_Name() {
		return Modclass_Name;
	}

	public void setModclass_Name(String Modclass_Name) {
		this.Modclass_Name = Modclass_Name;
	}

	@Column(name = "PARENTCLASS_ID", length = 400)
	public String getParentclass_Id() {
		return Parentclass_Id;
	}

	public void setParentclass_Id(String Parentclass_Id) {
		this.Parentclass_Id = Parentclass_Id;
	}

	@Column(name = "MODCLASS_INDEX")
	public Integer getModclass_Index() {
		return Modclass_Index;
	}

	public void setModclass_Index(Integer Modclass_Index) {
		this.Modclass_Index = Modclass_Index;
	}

	@Column(name = "MODCLASS_DESC", length = 600)
	public String getModclass_Desc() {
		return Modclass_Desc;
	}

	public void setModclass_Desc(String Modclass_Desc) {
		this.Modclass_Desc = Modclass_Desc;
	}

}
