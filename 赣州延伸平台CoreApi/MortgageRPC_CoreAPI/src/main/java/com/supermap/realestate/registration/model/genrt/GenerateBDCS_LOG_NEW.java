package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Internal Entity bdcs_c_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_LOG_NEW implements SuperModel<String> {

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

	private String xMBH;
	private boolean modify_xMBH = false;

	public String getXMBH() {
		return xMBH;
	}

	public void setXMBH(String xMBH) {
		if (this.xMBH != xMBH) {
			this.xMBH = xMBH;
			modify_xMBH = true;
		}
	}

	private Date oPERATETIME;
	private boolean modify_oPERATETIME = false;

	public Date getOPERATETIME() {
		return oPERATETIME;
	}

	public void setOPERATETIME(Date oPERATETIME) {
		if (this.oPERATETIME != oPERATETIME) {
			this.oPERATETIME = oPERATETIME;
			modify_oPERATETIME = true;
		}
	}

	private String oPERATEUSER;
	private boolean modify_oPERATEUSER = false;

	public String getOPERATEUSER() {
		return oPERATEUSER;
	}

	public void setOPERATEUSER(String oPERATEUSER) {
		if (this.oPERATEUSER != oPERATEUSER) {
			this.oPERATEUSER = oPERATEUSER;
			modify_oPERATEUSER = true;
		}
	}

	private String oPERATETYPE;
	private boolean modify_oPERATETYPE = false;

	public String getOPERATETYPE() {
		return oPERATETYPE;
	}

	public void setOPERATETYPE(String oPERATETYPE) {
		if (this.oPERATETYPE != oPERATETYPE) {
			this.oPERATETYPE = oPERATETYPE;
			modify_oPERATETYPE = true;
		}
	}
	private String oPERATETYPENAME;
	private boolean modify_oPERATETYPENAME = false;

	public String getOPERATETYPENAME() {
		return oPERATETYPENAME;
	}

	public void setOPERATETYPENAME(String oPERATETYPENAME) {
		if (this.oPERATETYPENAME != oPERATETYPENAME) {
			this.oPERATETYPENAME = oPERATETYPENAME;
			modify_oPERATETYPENAME = true;
		}
	}
	private String lOGCONTEXT;
	private boolean modify_lOGCONTEXT = false;

	public String getLOGCONTEXT() {
		return lOGCONTEXT;
	}

	public void setLOGCONTEXT(String lOGCONTEXT) {
		if (this.lOGCONTEXT != lOGCONTEXT) {
			this.lOGCONTEXT = lOGCONTEXT;
			modify_lOGCONTEXT = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_oPERATETIME = false;
		modify_oPERATEUSER = false;
		modify_oPERATETYPE = false;
		modify_oPERATETYPENAME = false;
		modify_lOGCONTEXT = false;
		
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_oPERATETIME)
			listStrings.add("oPERATETIME");
		if (!modify_oPERATEUSER)
			listStrings.add("oPERATEUSER");
		if (!modify_oPERATETYPE)
			listStrings.add("oPERATETYPE");
		if (!modify_oPERATETYPENAME)
			listStrings.add("oPERATETYPENAME");
		if (!modify_lOGCONTEXT)
			listStrings.add("lOGCONTEXT");

		return StringHelper.ListToStrings(listStrings);
	}
}
