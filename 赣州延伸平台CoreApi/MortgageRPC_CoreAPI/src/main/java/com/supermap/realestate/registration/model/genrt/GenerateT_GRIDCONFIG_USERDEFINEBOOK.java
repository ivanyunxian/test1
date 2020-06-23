package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/4 
//* ----------------------------------------
//* Internal Entity t_gridconfig_userdefinebook 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateT_GRIDCONFIG_USERDEFINEBOOK implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
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

	private String cOLUMNTEXT;
	private boolean modify_cOLUMNTEXT = false;

	public String getCOLUMNTEXT() {
		return cOLUMNTEXT;
	}

	public void setCOLUMNTEXT(String cOLUMNTEXT) {
		if (this.cOLUMNTEXT != cOLUMNTEXT) {
			this.cOLUMNTEXT = cOLUMNTEXT;
			modify_cOLUMNTEXT = true;
		}
	}

	private String wIDTH;
	private boolean modify_wIDTH = false;

	public String getWIDTH() {
		return wIDTH;
	}

	public void setWIDTH(String wIDTH) {
		if (this.wIDTH != wIDTH) {
			this.wIDTH = wIDTH;
			modify_wIDTH = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_fIELDNAME = false;
		modify_cOLUMNTEXT = false;
		modify_wIDTH = false;
		modify_bOOKID = false;
		modify_cONSTTYPE = false;
		modify_vIEWORDER = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_fIELDNAME)
			listStrings.add("fIELDNAME");
		if (!modify_cOLUMNTEXT)
			listStrings.add("cOLUMNTEXT");
		if (!modify_wIDTH)
			listStrings.add("wIDTH");
		if (!modify_bOOKID)
			listStrings.add("bOOKID");
		if (!modify_cONSTTYPE)
			listStrings.add("cONSTTYPE");
		if (!modify_vIEWORDER)
			listStrings.add("vIEWORDER");

		return StringHelper.ListToStrings(listStrings);
	}
}
