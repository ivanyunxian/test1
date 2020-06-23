package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Internal Entity bdcs_ysbzgg 
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

public class GenerateBDCS_YSBZGG implements SuperModel<String> {

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

	private String bDCQZH;
	private boolean modify_bDCQZH = false;

	public String getBDCQZH() {
		return bDCQZH;
	}

	public void setBDCQZH(String bDCQZH) {
		if (this.bDCQZH != bDCQZH) {
			this.bDCQZH = bDCQZH;
			modify_bDCQZH = true;
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

	private Date gGQSSJ;
	private boolean modify_gGQSSJ = false;

	public Date getGGQSSJ() {
		return gGQSSJ;
	}

	public void setGGQSSJ(Date gGQSSJ) {
		if (this.gGQSSJ != gGQSSJ) {
			this.gGQSSJ = gGQSSJ;
			modify_gGQSSJ = true;
		}
	}

	private Date gGJSSJ;
	private boolean modify_gGJSSJ = false;

	public Date getGGJSSJ() {
		return gGJSSJ;
	}

	public void setGGJSSJ(Date gGJSSJ) {
		if (this.gGJSSJ != gGJSSJ) {
			this.gGJSSJ = gGJSSJ;
			modify_gGJSSJ = true;
		}
	}

	private String gGSM;
	private boolean modify_gGSM = false;

	public String getGGSM() {
		return gGSM;
	}

	public void setGGSM(String gGSM) {
		if (this.gGSM != gGSM) {
			this.gGSM = gGSM;
			modify_gGSM = true;
		}
	}

	private String bZZT;
	private boolean modify_bZZT = false;

	public String getBZZT() {
		return bZZT;
	}

	public void setBZZT(String bZZT) {
		if (this.bZZT != bZZT) {
			this.bZZT = bZZT;
			modify_bZZT = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_xMBH = false;
		modify_bDCQZH = false;
		modify_qLID = false;
		modify_gGQSSJ = false;
		modify_gGJSSJ = false;
		modify_gGSM = false;
		modify_bZZT = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_gGQSSJ)
			listStrings.add("gGQSSJ");
		if (!modify_gGJSSJ)
			listStrings.add("gGJSSJ");
		if (!modify_gGSM)
			listStrings.add("gGSM");
		if (!modify_bZZT)
			listStrings.add("bZZT");

		return StringHelper.ListToStrings(listStrings);
	}
}
