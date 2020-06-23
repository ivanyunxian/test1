/**
 * 
 * 代码生成器自动生成[WFI_PROMATER]
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
@Table(name = "WFI_PROMATER", schema = "BDC_WORKFLOW")
public class Wfi_ProMater implements Cloneable{

	private String Materilinst_Id;
	private Integer Material_Isdossier;
	private Integer Dossier_Index;
	private String Material_Id;
	private Integer Material_Count;
	private String Material_Desc;
	private Integer Material_Index;
	private String Material_Name;
	private Integer Material_Pagecount;
	private Integer Material_Type;
	private Integer Material_Need;
	private String Materialdef_Id;
	private String Proinst_Id;
	private String Img_Path;
	private String Materialtype_Id;
	private Integer Material_Status;
	private Date Material_Date;
	private String Material_Bm;
	private String Material_Three;
	
	@Id
	@Column(name = "MATERILINST_ID", length = 400)
	public String getMaterilinst_Id() {
		if (Materilinst_Id == null)
			Materilinst_Id = UUID.randomUUID().toString().replace("-", "");
		return Materilinst_Id;
	}

	public void setMaterilinst_Id(String Materilinst_Id) {
		this.Materilinst_Id = Materilinst_Id;
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

	@Column(name = "MATERIAL_ID", length = 400)
	public String getMaterial_Id() {
		return Material_Id;
	}

	public void setMaterial_Id(String Material_Id) {
		this.Material_Id = Material_Id;
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

	@Column(name = "MATERIALDEF_ID", length = 400)
	public String getMaterialdef_Id() {
		return Materialdef_Id;
	}

	public void setMaterialdef_Id(String Materialdef_Id) {
		this.Materialdef_Id = Materialdef_Id;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "IMG_PATH", length = 2000)
	public String getImg_Path() {
		return Img_Path;
	}

	public void setImg_Path(String Img_Path) {
		this.Img_Path = Img_Path;
	}

	@Column(name = "MATERIALTYPE_ID", length = 400)
	public String getMaterialtype_Id() {
		return Materialtype_Id;
	}

	public void setMaterialtype_Id(String Materialtype_Id) {
		this.Materialtype_Id = Materialtype_Id;
	}

	@Column(name = "MATERIAL_STATUS")
	public Integer getMaterial_Status() {
		return Material_Status;
	}

	public void setMaterial_Status(Integer Material_Status) {
		this.Material_Status = Material_Status;
	}

	@Column(name = "MATERIAL_DATE", length = 7)
	public Date getMaterial_Date() {
		return Material_Date;
	}

	public void setMaterial_Date(Date Material_Date) {
		this.Material_Date = Material_Date;
	}
	
	@Column(name = "MATERIAL_BM", length = 200)
	public String getMaterial_Bm() {
		return Material_Bm;
	}

	public void setMaterial_Bm(String material_Bm) {
		Material_Bm = material_Bm;
	}

	
	@Column(name = "MATERIAL_THREE", length = 100)
	public String getMaterial_Three() {
		return Material_Three;
	}

	public void setMaterial_Three(String material_Three) {
		Material_Three = material_Three;
	}

	@Override
	public Object clone(){
		Wfi_ProMater promater = null;
		try {
			promater = (Wfi_ProMater) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return promater;
	}
}
