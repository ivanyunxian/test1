package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/8/18 
//* ----------------------------------------
//* Internal Entity bdcs_partiallimit 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_PARTIALLIMIT implements SuperModel<String> {

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

	private String bDCDYID;
	private boolean modify_bDCDYID = false;

	public String getBDCDYID() {
		return bDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		if (this.bDCDYID != bDCDYID) {
			this.bDCDYID = bDCDYID;
			modify_bDCDYID = true;
		}
	}

	private String qLID;
	private boolean modify_qLID = false;

	public String getQLID() {
		return qLID;
	}

	public void setQLID(String qLID) {
		if (this.qLID != qLID) {
			this.qLID = qLID;
			modify_qLID = true;
		}
	}

	private String qLRID;
	private boolean modify_qLRID = false;

	public String getQLRID() {
		return qLRID;
	}

	public void setQLRID(String qLRID) {
		if (this.qLRID != qLRID) {
			this.qLRID = qLRID;
			modify_qLRID = true;
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

	private String lIMITQLID;
	private boolean modify_lIMITQLID = false;

	public String getLIMITQLID() {
		return lIMITQLID;
	}

	public void setLIMITQLID(String lIMITQLID) {
		if (this.lIMITQLID != lIMITQLID) {
			this.lIMITQLID = lIMITQLID;
			modify_lIMITQLID = true;
		}
	}

	private String lIMITTYPE;
	private boolean modify_lIMITTYPE = false;

	public String getLIMITTYPE() {
		return lIMITTYPE;
	}

	public void setLIMITTYPE(String lIMITTYPE) {
		if (this.lIMITTYPE != lIMITTYPE) {
			this.lIMITTYPE = lIMITTYPE;
			modify_lIMITTYPE = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_qLID = false;
		modify_qLRID = false;
		modify_qLRMC = false;
		modify_zJH = false;
		modify_lIMITQLID = false;
		modify_lIMITTYPE = false;
		modify_xMBH = false;
		modify_yXBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_qLRID)
			listStrings.add("qLRID");
		if (!modify_qLRMC)
			listStrings.add("qLRMC");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_lIMITQLID)
			listStrings.add("lIMITQLID");
		if (!modify_lIMITTYPE)
			listStrings.add("lIMITTYPE");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
