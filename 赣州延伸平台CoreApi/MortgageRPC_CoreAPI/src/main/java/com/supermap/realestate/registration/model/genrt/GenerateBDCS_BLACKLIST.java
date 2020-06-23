package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2017/04/13 
//* ----------------------------------------
//* Internal Entity bdcs_blacklist 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_BLACKLIST implements SuperModel<String> {

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

	private String qLRMC;
	private boolean modify_qLRMC = false;

	public String getQLRMC() {
		return qLRMC;
	}

	public void setQLRMC(String qLRMC) {
		if (this.qLRMC != qLRMC) {
			this.qLRMC = qLRMC;
			modify_qLRMC = true;
		}
	}

	private String zJH;
	private boolean modify_zJH = false;

	public String getZJH() {
		return zJH;
	}

	public void setZJH(String zJH) {
		if (this.zJH != zJH) {
			this.zJH = zJH;
			modify_zJH = true;
		}
	}
	
	private String qZH;
	private boolean modify_qZH = false;

	public String getQZH() {
		return qZH;
	}

	public void setQZH(String qZH) {
		if (this.qZH != qZH) {
			this.qZH = qZH;
			modify_qZH = true;
		}
	}
	
	private String yXBZ;
	private boolean modify_yXBZ = false;

	public String getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(String yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	private String dETAIL;
	private boolean modify_dETAIL = false;

	public String getDETAIL() {
		return dETAIL;
	}

	public void setDETAIL(String dETAIL) {
		if (this.dETAIL != dETAIL) {
			this.dETAIL = dETAIL;
			modify_dETAIL = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_qLRMC = false;
		modify_zJH = false;
		modify_dETAIL = false;
		modify_qZH = false;
		modify_yXBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_qLRMC)
			listStrings.add("qLRMC");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_dETAIL)
			listStrings.add("dETAIL");
		if(!modify_qZH)
			listStrings.add("qZH");
		if(!modify_yXBZ)
			listStrings.add("yXBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
