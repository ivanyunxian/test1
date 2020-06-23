
/**
 * 
 * 代码生成器自动生成[BDCS_ARCHIVE_LZ]
 * 
 */

package com.supermap.luzhouothers.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDCS_ARCHIVE_LZ", schema = "BDCK")
public class BDCS_ARCHIVE_LZ {

	private Integer Ac_Id;
	private Integer Hs_Publicarea;
	private String Hs_Planning_Use;
	private String Hs_Structure;
	private Integer Bd_Floors;
	private String Re_Acceptcode;
	private String Re_Housebookcode;
	private String Re_Housecertificatecode;
	private String Re_Landcertificatecode;
	private Date Re_Accepttime;
	private Date Re_Housebooktime;
	private Date Re_Checktime;
	private Date Re_Archivetime;
	private Integer Re_Praisalvalue;
	private Integer Re_Quality;
	private String Re_Housepropertystartdate;
	private String Re_Housepropertyenddate;
	private Short Re_Regstatus;
	private String Re_Cat_House;
	private String Re_Landpropertystartdate;
	private String Re_Landpropertyenddate;
	private String Hdjdydm;
	private String Re_Housecertificatecode_S;
	private String Re_Landcertificatecode_S;
	private String Ca_Source;
	private String Archive_Type;
	private String Ac_Cat_Biz;
	private String Ac_Name_Biz;
	private String Ps_Name;
	private String Ps_Idcardno;
	private String Ps_Category;
	private String Qhdjdydm;
	private String Djqdjdydm;
	private String Djzqdjdydm;
	private String Zddjdydm;
	private String Zdtybm;
	private String Pa_Location;
	private Integer Pa_Area;
	private Integer Hs_Pubparea;
	private Integer Hs_Alloparea;
	private String Pa_Use;
	private String Pa_Grade;
	private String Zhdjdydm;
	private String Zhdtybm;
	private String Hs_Location;
	private Integer Hs_Buildarea;
	private Integer Hs_Usearea;

	@Id
	@Column(name = "AC_ID")
	public Integer getAc_Id() {
		if (Ac_Id == null)
			Ac_Id = 0;
		return Ac_Id;
	}

	public void setAc_Id(Integer Ac_Id) {
		this.Ac_Id = Ac_Id;
	}

	@Column(name = "HS_PUBLICAREA")
	public Integer getHs_Publicarea() {
		return Hs_Publicarea;
	}

	public void setHs_Publicarea(Integer Hs_Publicarea) {
		this.Hs_Publicarea = Hs_Publicarea;
	}

	@Column(name = "HS_PLANNING_USE", length = 200)
	public String getHs_Planning_Use() {
		return Hs_Planning_Use;
	}

	public void setHs_Planning_Use(String Hs_Planning_Use) {
		this.Hs_Planning_Use = Hs_Planning_Use;
	}

	@Column(name = "HS_STRUCTURE", length = 200)
	public String getHs_Structure() {
		return Hs_Structure;
	}

	public void setHs_Structure(String Hs_Structure) {
		this.Hs_Structure = Hs_Structure;
	}

	@Column(name = "BD_FLOORS")
	public Integer getBd_Floors() {
		return Bd_Floors;
	}

	public void setBd_Floors(Integer Bd_Floors) {
		this.Bd_Floors = Bd_Floors;
	}

	@Column(name = "RE_ACCEPTCODE", length = 200)
	public String getRe_Acceptcode() {
		return Re_Acceptcode;
	}

	public void setRe_Acceptcode(String Re_Acceptcode) {
		this.Re_Acceptcode = Re_Acceptcode;
	}

	@Column(name = "RE_HOUSEBOOKCODE", length = 200)
	public String getRe_Housebookcode() {
		return Re_Housebookcode;
	}

	public void setRe_Housebookcode(String Re_Housebookcode) {
		this.Re_Housebookcode = Re_Housebookcode;
	}

	@Column(name = "RE_HOUSECERTIFICATECODE", length = 200)
	public String getRe_Housecertificatecode() {
		return Re_Housecertificatecode;
	}

	public void setRe_Housecertificatecode(String Re_Housecertificatecode) {
		this.Re_Housecertificatecode = Re_Housecertificatecode;
	}

	@Column(name = "RE_LANDCERTIFICATECODE", length = 2000)
	public String getRe_Landcertificatecode() {
		return Re_Landcertificatecode;
	}

	public void setRe_Landcertificatecode(String Re_Landcertificatecode) {
		this.Re_Landcertificatecode = Re_Landcertificatecode;
	}

	@Column(name = "RE_ACCEPTTIME", length = 7)
	public Date getRe_Accepttime() {
		return Re_Accepttime;
	}

	public void setRe_Accepttime(Date Re_Accepttime) {
		this.Re_Accepttime = Re_Accepttime;
	}

	@Column(name = "RE_HOUSEBOOKTIME", length = 7)
	public Date getRe_Housebooktime() {
		return Re_Housebooktime;
	}

	public void setRe_Housebooktime(Date Re_Housebooktime) {
		this.Re_Housebooktime = Re_Housebooktime;
	}

	@Column(name = "RE_CHECKTIME", length = 7)
	public Date getRe_Checktime() {
		return Re_Checktime;
	}

	public void setRe_Checktime(Date Re_Checktime) {
		this.Re_Checktime = Re_Checktime;
	}

	@Column(name = "RE_ARCHIVETIME", length = 7)
	public Date getRe_Archivetime() {
		return Re_Archivetime;
	}

	public void setRe_Archivetime(Date Re_Archivetime) {
		this.Re_Archivetime = Re_Archivetime;
	}

	@Column(name = "RE_PRAISALVALUE")
	public Integer getRe_Praisalvalue() {
		return Re_Praisalvalue;
	}

	public void setRe_Praisalvalue(Integer Re_Praisalvalue) {
		this.Re_Praisalvalue = Re_Praisalvalue;
	}

	@Column(name = "RE_QUALITY")
	public Integer getRe_Quality() {
		return Re_Quality;
	}

	public void setRe_Quality(Integer Re_Quality) {
		this.Re_Quality = Re_Quality;
	}

	@Column(name = "RE_HOUSEPROPERTYSTARTDATE", length = 200)
	public String getRe_Housepropertystartdate() {
		return Re_Housepropertystartdate;
	}

	public void setRe_Housepropertystartdate(String Re_Housepropertystartdate) {
		this.Re_Housepropertystartdate = Re_Housepropertystartdate;
	}

	@Column(name = "RE_HOUSEPROPERTYENDDATE", length = 200)
	public String getRe_Housepropertyenddate() {
		return Re_Housepropertyenddate;
	}

	public void setRe_Housepropertyenddate(String Re_Housepropertyenddate) {
		this.Re_Housepropertyenddate = Re_Housepropertyenddate;
	}

	@Column(name = "RE_REGSTATUS")
	public Short getRe_Regstatus() {
		return Re_Regstatus;
	}

	public void setRe_Regstatus(Short Re_Regstatus) {
		this.Re_Regstatus = Re_Regstatus;
	}

	@Column(name = "RE_CAT_HOUSE", length = 80)
	public String getRe_Cat_House() {
		return Re_Cat_House;
	}

	public void setRe_Cat_House(String Re_Cat_House) {
		this.Re_Cat_House = Re_Cat_House;
	}

	@Column(name = "RE_LANDPROPERTYSTARTDATE", length = 200)
	public String getRe_Landpropertystartdate() {
		return Re_Landpropertystartdate;
	}

	public void setRe_Landpropertystartdate(String Re_Landpropertystartdate) {
		this.Re_Landpropertystartdate = Re_Landpropertystartdate;
	}

	@Column(name = "RE_LANDPROPERTYENDDATE", length = 200)
	public String getRe_Landpropertyenddate() {
		return Re_Landpropertyenddate;
	}

	public void setRe_Landpropertyenddate(String Re_Landpropertyenddate) {
		this.Re_Landpropertyenddate = Re_Landpropertyenddate;
	}

	@Column(name = "HDJDYDM", length = 29)
	public String getHdjdydm() {
		return Hdjdydm;
	}

	public void setHdjdydm(String Hdjdydm) {
		this.Hdjdydm = Hdjdydm;
	}

	@Column(name = "RE_HOUSECERTIFICATECODE_S", length = 10)
	public String getRe_Housecertificatecode_S() {
		return Re_Housecertificatecode_S;
	}

	public void setRe_Housecertificatecode_S(String Re_Housecertificatecode_S) {
		this.Re_Housecertificatecode_S = Re_Housecertificatecode_S;
	}

	@Column(name = "RE_LANDCERTIFICATECODE_S", length = 400)
	public String getRe_Landcertificatecode_S() {
		return Re_Landcertificatecode_S;
	}

	public void setRe_Landcertificatecode_S(String Re_Landcertificatecode_S) {
		this.Re_Landcertificatecode_S = Re_Landcertificatecode_S;
	}

	@Column(name = "CA_SOURCE", length = 100)
	public String getCa_Source() {
		return Ca_Source;
	}

	public void setCa_Source(String Ca_Source) {
		this.Ca_Source = Ca_Source;
	}

	@Column(name = "ARCHIVE_TYPE", length = 4)
	public String getArchive_Type() {
		return Archive_Type;
	}

	public void setArchive_Type(String Archive_Type) {
		this.Archive_Type = Archive_Type;
	}

	@Column(name = "AC_CAT_BIZ", length = 80)
	public String getAc_Cat_Biz() {
		return Ac_Cat_Biz;
	}

	public void setAc_Cat_Biz(String Ac_Cat_Biz) {
		this.Ac_Cat_Biz = Ac_Cat_Biz;
	}

	@Column(name = "AC_NAME_BIZ", length = 80)
	public String getAc_Name_Biz() {
		return Ac_Name_Biz;
	}

	public void setAc_Name_Biz(String Ac_Name_Biz) {
		this.Ac_Name_Biz = Ac_Name_Biz;
	}

	@Column(name = "PS_NAME", length = 200)
	public String getPs_Name() {
		return Ps_Name;
	}

	public void setPs_Name(String Ps_Name) {
		this.Ps_Name = Ps_Name;
	}

	@Column(name = "PS_IDCARDNO", length = 200)
	public String getPs_Idcardno() {
		return Ps_Idcardno;
	}

	public void setPs_Idcardno(String Ps_Idcardno) {
		this.Ps_Idcardno = Ps_Idcardno;
	}

	@Column(name = "PS_CATEGORY", length = 80)
	public String getPs_Category() {
		return Ps_Category;
	}

	public void setPs_Category(String Ps_Category) {
		this.Ps_Category = Ps_Category;
	}

	@Column(name = "QHDJDYDM", length = 40)
	public String getQhdjdydm() {
		return Qhdjdydm;
	}

	public void setQhdjdydm(String Qhdjdydm) {
		this.Qhdjdydm = Qhdjdydm;
	}

	@Column(name = "DJQDJDYDM", length = 40)
	public String getDjqdjdydm() {
		return Djqdjdydm;
	}

	public void setDjqdjdydm(String Djqdjdydm) {
		this.Djqdjdydm = Djqdjdydm;
	}

	@Column(name = "DJZQDJDYDM", length = 40)
	public String getDjzqdjdydm() {
		return Djzqdjdydm;
	}

	public void setDjzqdjdydm(String Djzqdjdydm) {
		this.Djzqdjdydm = Djzqdjdydm;
	}

	@Column(name = "ZDDJDYDM", length = 29)
	public String getZddjdydm() {
		return Zddjdydm;
	}

	public void setZddjdydm(String Zddjdydm) {
		this.Zddjdydm = Zddjdydm;
	}

	@Column(name = "ZDTYBM", length = 100)
	public String getZdtybm() {
		return Zdtybm;
	}

	public void setZdtybm(String Zdtybm) {
		this.Zdtybm = Zdtybm;
	}

	@Column(name = "PA_LOCATION", length = 2000)
	public String getPa_Location() {
		return Pa_Location;
	}

	public void setPa_Location(String Pa_Location) {
		this.Pa_Location = Pa_Location;
	}

	@Column(name = "PA_AREA")
	public Integer getPa_Area() {
		return Pa_Area;
	}

	public void setPa_Area(Integer Pa_Area) {
		this.Pa_Area = Pa_Area;
	}

	@Column(name = "HS_PUBPAREA")
	public Integer getHs_Pubparea() {
		return Hs_Pubparea;
	}

	public void setHs_Pubparea(Integer Hs_Pubparea) {
		this.Hs_Pubparea = Hs_Pubparea;
	}

	@Column(name = "HS_ALLOPAREA")
	public Integer getHs_Alloparea() {
		return Hs_Alloparea;
	}

	public void setHs_Alloparea(Integer Hs_Alloparea) {
		this.Hs_Alloparea = Hs_Alloparea;
	}

	@Column(name = "PA_USE", length = 200)
	public String getPa_Use() {
		return Pa_Use;
	}

	public void setPa_Use(String Pa_Use) {
		this.Pa_Use = Pa_Use;
	}

	@Column(name = "PA_GRADE", length = 400)
	public String getPa_Grade() {
		return Pa_Grade;
	}

	public void setPa_Grade(String Pa_Grade) {
		this.Pa_Grade = Pa_Grade;
	}

	@Column(name = "ZHDJDYDM", length = 29)
	public String getZhdjdydm() {
		return Zhdjdydm;
	}

	public void setZhdjdydm(String Zhdjdydm) {
		this.Zhdjdydm = Zhdjdydm;
	}

	@Column(name = "ZHDTYBM", length = 100)
	public String getZhdtybm() {
		return Zhdtybm;
	}

	public void setZhdtybm(String Zhdtybm) {
		this.Zhdtybm = Zhdtybm;
	}

	@Column(name = "HS_LOCATION", length = 1000)
	public String getHs_Location() {
		return Hs_Location;
	}

	public void setHs_Location(String Hs_Location) {
		this.Hs_Location = Hs_Location;
	}

	@Column(name = "HS_BUILDAREA")
	public Integer getHs_Buildarea() {
		return Hs_Buildarea;
	}

	public void setHs_Buildarea(Integer Hs_Buildarea) {
		this.Hs_Buildarea = Hs_Buildarea;
	}

	@Column(name = "HS_USEAREA")
	public Integer getHs_Usearea() {
		return Hs_Usearea;
	}

	public void setHs_Usearea(Integer Hs_Usearea) {
		this.Hs_Usearea = Hs_Usearea;
	}

}
