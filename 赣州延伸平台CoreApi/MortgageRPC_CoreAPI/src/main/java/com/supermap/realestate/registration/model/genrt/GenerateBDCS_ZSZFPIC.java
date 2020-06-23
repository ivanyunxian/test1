package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/8 
//* ----------------------------------------
//* Internal Entity bdcs_zszfpic 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;


import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_ZSZFPIC implements SuperModel<String> {

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

	private String rKQZBID;
	private boolean modify_rKQZBID = false;

	public String getRKQZBID() {
		return rKQZBID;
	}

	public void setRKQZBID(String rKQZBID) {
		if (this.rKQZBID != rKQZBID) {
			this.rKQZBID = rKQZBID;
			modify_rKQZBID = true;
		}
	}

	private Integer sXH;
	private boolean modify_sXH = false;

	public Integer getSXH() {
		return sXH;
	}

	public void setSXH(Integer sXH) {
		if (this.sXH != sXH) {
			this.sXH = sXH;
			modify_sXH = true;
		}
	}

	private String zSPIC;
	private boolean modify_zSPIC = false;

	public String getZSPIC() {
		return zSPIC;
	}

	public void setZSPIC(String zSPIC) {
		if (this.zSPIC != zSPIC) {
			this.zSPIC = zSPIC;
			modify_zSPIC = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_rKQZBID = false;
		modify_sXH = false;
		modify_zSPIC = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_rKQZBID)
			listStrings.add("rKQZBID");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_zSPIC)
			listStrings.add("zSPIC");

		return StringHelper.ListToStrings(listStrings);
	}
}
