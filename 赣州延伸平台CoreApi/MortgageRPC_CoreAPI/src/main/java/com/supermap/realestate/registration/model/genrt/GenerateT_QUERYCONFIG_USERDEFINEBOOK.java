package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/5 
//* ----------------------------------------
//* Internal Entity t_queryconfig_userdefinebook 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateT_QUERYCONFIG_USERDEFINEBOOK implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null) {
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}

	@Override
	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String fIELDNAME;
	private boolean modify_fIELDNAME = false;

	public String getFIELDNAME() {
		return fIELDNAME;
	}

	public void setFIELDNAME(String fIELDNAME) {
		if (this.fIELDNAME != fIELDNAME) {
			this.fIELDNAME = fIELDNAME;
			modify_fIELDNAME = true;
		}
	}

	private String fIELDCAPTION;
	private boolean modify_fIELDCAPTION = false;

	public String getFIELDCAPTION() {
		return fIELDCAPTION;
	}

	public void setFIELDCAPTION(String fIELDCAPTION) {
		if (this.fIELDCAPTION != fIELDCAPTION) {
			this.fIELDCAPTION = fIELDCAPTION;
			modify_fIELDCAPTION = true;
		}
	}

	private String bOOKID;
	private boolean modify_bOOKID = false;

	public String getBOOKID() {
		return bOOKID;
	}

	public void setBOOKID(String bOOKID) {
		if (this.bOOKID != bOOKID) {
			this.bOOKID = bOOKID;
			modify_bOOKID = true;
		}
	}

	private String fIELDTYPE;
	private boolean modify_fIELDTYPE = false;

	public String getFIELDTYPE() {
		return fIELDTYPE;
	}

	public void setFIELDTYPE(String fIELDTYPE) {
		if (this.fIELDTYPE != fIELDTYPE) {
			this.fIELDTYPE = fIELDTYPE;
			modify_fIELDTYPE = true;
		}
	}

	private Integer vIEWORDER;
	private boolean modify_vIEWORDER = false;

	public Integer getVIEWORDER() {
		return vIEWORDER;
	}

	public void setVIEWORDER(Integer vIEWORDER) {
		if (this.vIEWORDER != vIEWORDER) {
			this.vIEWORDER = vIEWORDER;
			modify_vIEWORDER = true;
		}
	}

	private String cONSTTYPE;
	private boolean modify_cONSTTYPE = false;

	public String getCONSTTYPE() {
		return cONSTTYPE;
	}

	public void setCONSTTYPE(String cONSTTYPE) {
		if (this.cONSTTYPE != cONSTTYPE) {
			this.cONSTTYPE = cONSTTYPE;
			modify_cONSTTYPE = true;
		}
	}

	private String hQLPRE;
	private boolean modify_hQLPRE = false;

	public String getHQLPRE() {
		return hQLPRE;
	}

	public void setHQLPRE(String hQLPRE) {
		if (this.hQLPRE != hQLPRE) {
			this.hQLPRE = hQLPRE;
			modify_hQLPRE = true;
		}
	}

	private String hQLSUFFIX;
	private boolean modify_hQLSUFFIX = false;

	public String getHQLSUFFIX() {
		return hQLSUFFIX;
	}

	public void setHQLSUFFIX(String hQLSUFFIX) {
		if (this.hQLSUFFIX != hQLSUFFIX) {
			this.hQLSUFFIX = hQLSUFFIX;
			modify_hQLSUFFIX = true;
		}
	}

	private String hQLTYPE;
	private boolean modify_hQLTYPE = false;

	public String getHQLTYPE() {
		return hQLTYPE;
	}

	public void setHQLTYPE(String hQLTYPE) {
		if (this.hQLTYPE != hQLTYPE) {
			this.hQLTYPE = hQLTYPE;
			modify_hQLTYPE = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_fIELDNAME = false;
		modify_fIELDCAPTION = false;
		modify_bOOKID = false;
		modify_fIELDTYPE = false;
		modify_vIEWORDER = false;
		modify_cONSTTYPE = false;
		modify_hQLPRE = false;
		modify_hQLSUFFIX = false;
		modify_hQLTYPE = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_fIELDNAME)
			listStrings.add("fIELDNAME");
		if (!modify_fIELDCAPTION)
			listStrings.add("fIELDCAPTION");
		if (!modify_bOOKID)
			listStrings.add("bOOKID");
		if (!modify_fIELDTYPE)
			listStrings.add("fIELDTYPE");
		if (!modify_vIEWORDER)
			listStrings.add("vIEWORDER");
		if (!modify_cONSTTYPE)
			listStrings.add("cONSTTYPE");
		if (!modify_hQLPRE)
			listStrings.add("hQLPRE");
		if (!modify_hQLSUFFIX)
			listStrings.add("hQLSUFFIX");
		if (!modify_hQLTYPE)
			listStrings.add("hQLTYPE");

		return StringHelper.ListToStrings(listStrings);
	}
}
