
/**
 * 
 * 代码生成器自动生成[WFD_MATER]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_MATER",schema = "BDC_WORKFLOW")
public class Wfd_Mater {

	private String Materialdef_Id;
	private String Materialtype_Id;
	private Integer Material_Count;
	private String Material_Desc;
	private Integer Material_Index;
	private String Material_Name;
	private Integer Material_Pagecount;
	private String Material_Type;
	private String Material_Need;
	private String Material_Bm;

	@Id
	@Column(name = "MATERIALDEF_ID", length = 400)
	public String getMaterialdef_Id() {
		if (Materialdef_Id == null)
			Materialdef_Id = UUID.randomUUID().toString().replace("-", "");
		return Materialdef_Id;
	}

	public void setMaterialdef_Id(String Materialdef_Id) {
		this.Materialdef_Id = Materialdef_Id;
	}

	@Column(name = "MATERIALTYPE_ID", length = 400)
	public String getMaterialtype_Id() {
		return Materialtype_Id;
	}

	public void setMaterialtype_Id(String Materialtype_Id) {
		this.Materialtype_Id = Materialtype_Id;
	}

	@Column(name = "MATERIAL_COUNT")
	public Integer getMaterial_Count() {
		return Material_Count;
	}

	public void setMaterial_Count(Integer Material_Count) {
		this.Material_Count = Material_Count;
	}

	@Column(name = "MATERIAL_DESC", length = 2048)
	public String getMaterial_Desc() {
		return Material_Desc;
	}

	public void setMaterial_Desc(String Material_Desc) {
		this.Material_Desc = Material_Desc;
	}

	@Column(name = "MATERIAL_INDEX")
	public Integer getMaterial_Index() {
		return Material_Index;
	}

	public void setMaterial_Index(Integer Material_Index) {
		this.Material_Index = Material_Index;
	}

	@Column(name = "MATERIAL_NAME", length = 2048)
	public String getMaterial_Name() {
		return Material_Name;
	}

	public void setMaterial_Name(String Material_Name) {
		this.Material_Name = Material_Name;
	}

	@Column(name = "MATERIAL_PAGECOUNT")
	public Integer getMaterial_Pagecount() {
		return Material_Pagecount;
	}

	public void setMaterial_Pagecount(Integer Material_Pagecount) {
		this.Material_Pagecount = Material_Pagecount;
	}

	@Column(name = "MATERIAL_TYPE", length = 400)
	public String getMaterial_Type() {
		return Material_Type;
	}

	public void setMaterial_Type(String Material_Type) {
		this.Material_Type = Material_Type;
	}
	@Column(name = "MATERIAL_NEED")
	public String getMaterial_Need() {
		return Material_Need;
	}

	public void setMaterial_Need(String material_Need) {
		Material_Need = material_Need;
	}
	@Column(name = "MATERIAL_BM", length = 200)
	public String getMaterial_Bm() {
		return Material_Bm;
	}

	public void setMaterial_Bm(String material_Bm) {
		Material_Bm = material_Bm;
	}

}
