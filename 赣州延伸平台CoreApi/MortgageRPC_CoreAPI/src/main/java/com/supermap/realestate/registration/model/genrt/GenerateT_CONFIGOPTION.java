package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-26 
//* ----------------------------------------
//* Internal Entity t_configoption 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateT_CONFIGOPTION implements SuperModel<String> {

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

	private String oPTIONCLASS;
	private boolean modify_oPTIONCLASS = false;

	public String getOPTIONCLASS() {
		return oPTIONCLASS;
	}

	public void setOPTIONCLASS(String oPTIONCLASS) {
		if (this.oPTIONCLASS != oPTIONCLASS) {
			this.oPTIONCLASS = oPTIONCLASS;
			modify_oPTIONCLASS = true;
		}
	}

	private String oPTIONVALUE;
	private boolean modify_oPTIONVALUE = false;

	public String getOPTIONVALUE() {
		return oPTIONVALUE;
	}

	public void setOPTIONVALUE(String oPTIONVALUE) {
		if (this.oPTIONVALUE != oPTIONVALUE) {
			this.oPTIONVALUE = oPTIONVALUE;
			modify_oPTIONVALUE = true;
		}
	}

	private String oPTIONTEXT;
	private boolean modify_oPTIONTEXT = false;

	public String getOPTIONTEXT() {
		return oPTIONTEXT;
	}

	public void setOPTIONTEXT(String oPTIONTEXT) {
		if (this.oPTIONTEXT != oPTIONTEXT) {
			this.oPTIONTEXT = oPTIONTEXT;
			modify_oPTIONTEXT = true;
		}
	}

	private Integer yXBZ;
	private boolean modify_yXBZ = false;

	public Integer getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(Integer yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_oPTIONCLASS = false;
		modify_oPTIONVALUE = false;
		modify_oPTIONTEXT = false;
		modify_yXBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_oPTIONCLASS)
			listStrings.add("oPTIONCLASS");
		if (!modify_oPTIONVALUE)
			listStrings.add("oPTIONVALUE");
		if (!modify_oPTIONTEXT)
			listStrings.add("oPTIONTEXT");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
