
/**
 * 
 * 代码生成器自动生成[WFI_MATERDATA]
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
@Table(name = "WFI_MATERCLASS",schema = "BDC_WORKFLOW")
public class Wfi_MaterClass {

	private String MaterialType_Id;
	private String MaterialType_Desc;
	private Integer MaterialType_Index;
	private String MaterialType_Name;
	private String MaterialType_Pid;
	private String Proinst_Id;
	
	@Id
	@Column(name = "MATERIALTYPE_ID", length = 400)
	public String getMaterialType_Id() {
		if (MaterialType_Id == null)
			MaterialType_Id = UUID.randomUUID().toString().replace("-", "");
		return MaterialType_Id;
	}

	public void setMaterialType_Id(String MaterialType_Id) {
		this.MaterialType_Id = MaterialType_Id;
	}

	@Column(name = "MATERIALTYPE_DESC", length = 400)
	public String getMaterialType_Desc() {
		return MaterialType_Desc;
	}

	public void setMaterialType_Desc(String materialType_Desc) {
		MaterialType_Desc = materialType_Desc;
	}

	@Column(name = "MATERIALTYPE_INDEX")
	public Integer getMaterialType_Index() {
		return MaterialType_Index;
	}

	public void setMaterialType_Index(Integer materialType_Index) {
		MaterialType_Index = materialType_Index;
	}

	@Column(name = "MATERIALTYPE_NAME", length = 400)
	public String getMaterialType_Name() {
		return MaterialType_Name;
	}

	public void setMaterialType_Name(String materialType_Name) {
		MaterialType_Name = materialType_Name;
	}

	@Column(name = "MATERIALTYPE_PID", length = 400)
	public String getMaterialType_Pid() {
		return MaterialType_Pid;
	}

	public void setMaterialType_Pid(String materialType_Pid) {
		MaterialType_Pid = materialType_Pid;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String proinst_Id) {
		Proinst_Id = proinst_Id;
	}
	
}
