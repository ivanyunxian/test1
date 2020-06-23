
/**
 * 
 * 代码生成器自动生成[WFD_PROCLASS]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_PROCLASS",schema = "BDC_WORKFLOW")
public class Wfd_ProClass {

	private String Prodefclass_Id;
	private String Prodefclass_Pid;
	private String Prodefclass_Name;
	private String Prodefclass_Desc;
	private Integer Prodefclass_Index;

	@Id
	@Column(name = "PRODEFCLASS_ID", length = 400)
	public String getProdefclass_Id() {
		if (Prodefclass_Id == null)
			Prodefclass_Id = UUID.randomUUID().toString().replace("-", "");
		return Prodefclass_Id;
	}

	public void setProdefclass_Id(String Prodefclass_Id) {
		this.Prodefclass_Id = Prodefclass_Id;
	}

	@Column(name = "PRODEFCLASS_PID", length = 400)
	public String getProdefclass_Pid() {
		return Prodefclass_Pid;
	}

	public void setProdefclass_Pid(String Prodefclass_Pid) {
		this.Prodefclass_Pid = Prodefclass_Pid;
	}

	@Column(name = "PRODEFCLASS_NAME", length = 2048)
	public String getProdefclass_Name() {
		return Prodefclass_Name;
	}

	public void setProdefclass_Name(String Prodefclass_Name) {
		this.Prodefclass_Name = Prodefclass_Name;
	}

	@Column(name = "PRODEFCLASS_DESC", length = 2048)
	public String getProdefclass_Desc() {
		return Prodefclass_Desc;
	}

	public void setProdefclass_Desc(String Prodefclass_Desc) {
		this.Prodefclass_Desc = Prodefclass_Desc;
	}

	@Column(name = "PRODEFCLASS_INDEX")
	public Integer getProdefclass_Index() {
		return Prodefclass_Index;
	}

	public void setProdefclass_Index(Integer Prodefclass_Index) {
		this.Prodefclass_Index = Prodefclass_Index;
	}

}
