/**
 * 代码生成器自动生成[WFD_MATERCLASS]
 */
package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "WFD_MATERCLASS",schema = "BDC_WORKFLOW")
public class Wfd_MaterClass {

	private String Materialtype_Id;
	private String Materialtype_Pid;
	private String Materialtype_Name;
	private Integer Materialtype_Index;
	private String Materialtype_Desc;
	private String Prodef_Id;

	@Id
	@Column(name = "MATERIALTYPE_ID", length = 400)
	public String getMaterialtype_Id() {
		if (Materialtype_Id == null)
			Materialtype_Id = UUID.randomUUID().toString().replace("-", "");
		return Materialtype_Id;
	}

	public void setMaterialtype_Id(String Materialtype_Id) {
		this.Materialtype_Id = Materialtype_Id;
	}

	@Column(name = "MATERIALTYPE_PID", length = 400)
	public String getMaterialtype_Pid() {
		return Materialtype_Pid;
	}

	public void setMaterialtype_Pid(String Materialtype_Pid) {
		this.Materialtype_Pid = Materialtype_Pid;
	}

	@Column(name = "MATERIALTYPE_NAME", length = 2048)
	public String getMaterialtype_Name() {
		return Materialtype_Name;
	}

	public void setMaterialtype_Name(String Materialtype_Name) {
		this.Materialtype_Name = Materialtype_Name;
	}

	@Column(name = "MATERIALTYPE_INDEX")
	public Integer getMaterialtype_Index() {
		return Materialtype_Index;
	}

	public void setMaterialtype_Index(Integer Materialtype_Index) {
		this.Materialtype_Index = Materialtype_Index;
	}

	@Column(name = "MATERIALTYPE_DESC", length = 2048)
	public String getMaterialtype_Desc() {
		return Materialtype_Desc;
	}

	public void setMaterialtype_Desc(String Materialtype_Desc) {
		this.Materialtype_Desc = Materialtype_Desc;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

}
