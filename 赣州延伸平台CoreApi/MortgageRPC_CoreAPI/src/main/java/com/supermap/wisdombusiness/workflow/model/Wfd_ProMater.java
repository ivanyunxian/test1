/**
 * 
 * 代码生成器自动生成[WFD_PROMATER]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_PROMATER", schema = "BDC_WORKFLOW")
public class Wfd_ProMater {

	private String Material_Id;
	private String Prodef_Id;
	private String Materialdef_Id;
	private Integer Transaction_Type;
	private String Material_Name;
	private Integer Material_Type;
	private Integer Material_Need;
	private Integer Material_Count;
	private Integer Material_Pagecount;
	private Integer Material_Index;
	private String Material_Desc;
	private String Materialtype_Id;
	private String Wfd_Promater;
	private Integer Material_Isdossier;
	private Integer Dossier_Index;
	private String  Material_Bm;

	@Id
	@Column(name = "MATERIAL_ID", length = 400)
	public String getMaterial_Id() {
		if (Material_Id == null)
			Material_Id = UUID.randomUUID().toString().replace("-", "");
		return Material_Id;
	}

	public void setMaterial_Id(String Material_Id) {
		this.Material_Id = Material_Id;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

	@Column(name = "MATERIALDEF_ID", length = 400)
	public String getMaterialdef_Id() {
		return Materialdef_Id;
	}

	public void setMaterialdef_Id(String Materialdef_Id) {
		this.Materialdef_Id = Materialdef_Id;
	}

	@Column(name = "TRANSACTION_TYPE")
	public Integer getTransaction_Type() {
		return Transaction_Type;
	}

	public void setTransaction_Type(Integer Transaction_Type) {
		this.Transaction_Type = Transaction_Type;
	}

	@Column(name = "MATERIAL_NAME", length = 2048)
	public String getMaterial_Name() {
		return Material_Name;
	}

	public void setMaterial_Name(String Material_Name) {
		this.Material_Name = Material_Name;
	}

	@Column(name = "MATERIAL_TYPE")
	public Integer getMaterial_Type() {
		return Material_Type;
	}

	public void setMaterial_Type(Integer Material_Type) {
		this.Material_Type = Material_Type;
	}

	@Column(name = "MATERIAL_NEED")
	public Integer getMaterial_Need() {
		return Material_Need;
	}

	public void setMaterial_Need(Integer Material_Need) {
		this.Material_Need = Material_Need;
	}

	@Column(name = "MATERIAL_COUNT")
	public Integer getMaterial_Count() {
		return Material_Count;
	}

	public void setMaterial_Count(Integer Material_Count) {
		this.Material_Count = Material_Count;
	}

	@Column(name = "MATERIAL_PAGECOUNT")
	public Integer getMaterial_Pagecount() {
		return Material_Pagecount;
	}

	public void setMaterial_Pagecount(Integer Material_Pagecount) {
		this.Material_Pagecount = Material_Pagecount;
	}

	@Column(name = "MATERIAL_INDEX")
	public Integer getMaterial_Index() {
		return Material_Index;
	}

	public void setMaterial_Index(Integer Material_Index) {
		this.Material_Index = Material_Index;
	}

	@Column(name = "MATERIAL_DESC", length = 2048)
	public String getMaterial_Desc() {
		return Material_Desc;
	}

	public void setMaterial_Desc(String Material_Desc) {
		this.Material_Desc = Material_Desc;
	}

	@Column(name = "MATERIALTYPE_ID", length = 400)
	public String getMaterialtype_Id() {
		return Materialtype_Id;
	}

	public void setMaterialtype_Id(String Materialtype_Id) {
		this.Materialtype_Id = Materialtype_Id;
	}

	@Column(name = "WFD_PROMATER", length = 400)
	public String getWfd_Promater() {
		return Wfd_Promater;
	}

	public void setWfd_Promater(String Wfd_Promater) {
		this.Wfd_Promater = Wfd_Promater;
	}

	@Column(name = "MATERIAL_ISDOSSIER")
	public Integer getMaterial_Isdossier() {
		return Material_Isdossier;
	}

	public void setMaterial_Isdossier(Integer Material_Isdossier) {
		this.Material_Isdossier = Material_Isdossier;
	}

	@Column(name = "DOSSIER_INDEX")
	public Integer getDossier_Index() {
		return Dossier_Index;
	}

	public void setDossier_Index(Integer Dossier_Index) {
		this.Dossier_Index = Dossier_Index;
	}
	@Column(name = "MATERIAL_BM", length = 200)
	public String getMaterial_Bm() {
		return Material_Bm;
	}

	public void setMaterial_Bm(String material_Bm) {
		Material_Bm = material_Bm;
	}

}
