/**
 * 
 * 代码生成器自动生成[WFD_CONST]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_CONST",schema = "BDC_WORKFLOW")
public class Wfd_Const {

	private String Const_Id;
	private String Const_Name;
	private String Const_Code;
	private String Const_Desc;
	private Integer Const_Status;
	private String Const_Type;

	@Id
	@Column(name = "CONST_ID", length = 400)
	public String getConst_Id() {
		if (Const_Id == null)
			Const_Id = UUID.randomUUID().toString().replace("-", "");
		return Const_Id;
	}

	public void setConst_Id(String Const_Id) {
		this.Const_Id = Const_Id;
	}

	@Column(name = "CONST_NAME", length = 80)
	public String getConst_Name() {
		return Const_Name;
	}

	public void setConst_Name(String Const_Name) {
		this.Const_Name = Const_Name;
	}

	@Column(name = "CONST_CODE", length = 80)
	public String getConst_Code() {
		return Const_Code;
	}

	public void setConst_Code(String Const_Code) {
		this.Const_Code = Const_Code;
	}

	@Column(name = "CONST_DESC", length = 400)
	public String getConst_Desc() {
		return Const_Desc;
	}

	public void setConst_Desc(String Const_Desc) {
		this.Const_Desc = Const_Desc;
	}

	@Column(name = "CONST_STATUS")
	public Integer getConst_Status() {
		return Const_Status;
	}

	public void setConst_Status(Integer Const_Status) {
		this.Const_Status = Const_Status;
	}

	@Column(name = "CONST_TYPE", length = 80)
	public String getConst_Type() {
		return Const_Type;
	}

	public void setConst_Type(String Const_Type) {
		this.Const_Type = Const_Type;
	}

}
