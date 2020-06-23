package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/8/24 
//* ----------------------------------------
//* Internal Entity bdcs_cx_print_log 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.realestate.registration.util.ConstHelper;

import javax.persistence.Transient;

import com.supermap.wisdombusiness.core.SuperModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_CX_PRINT_LOG implements SuperModel<String> {

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

	private String oPERATCONTEXT;
	private boolean modify_oPERATCONTEXT = false;

	public String getOPERATCONTEXT() {
		return oPERATCONTEXT;
	}

	public void setOPERATCONTEXT(String oPERATCONTEXT) {
		if (this.oPERATCONTEXT != oPERATCONTEXT) {
			this.oPERATCONTEXT = oPERATCONTEXT;
			modify_oPERATCONTEXT = true;
		}
	}

	private String cXLX;
	private boolean modify_cXLX = false;

	public String getCXLX() {
		return cXLX;
	}

	public void setCXLX(String cXLX) {
		if (this.cXLX != cXLX) {
			this.cXLX = cXLX;
			modify_cXLX = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_oPERATEUSER = false;
		modify_oPERATETIME = false;
		modify_oPERATETYPE = false;
		modify_oPERATCONTEXT = false;
		modify_cXLX = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_oPERATEUSER)
			listStrings.add("oPERATEUSER");
		if (!modify_oPERATETIME)
			listStrings.add("oPERATETIME");
		if (!modify_oPERATETYPE)
			listStrings.add("oPERATETYPE");
		if (!modify_oPERATCONTEXT)
			listStrings.add("oPERATCONTEXT");
		if (!modify_cXLX)
			listStrings.add("cXLX");

		return StringHelper.ListToStrings(listStrings);
	}
}
