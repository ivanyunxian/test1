package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-8-26 
//* ----------------------------------------
//* Internal Entity bdcs_maintainunit 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;


public class GenerateBDCS_MAINTAINUNIT implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;


	public String getId() {
		if (!modify_id && id == null)
		{
			id = (String) SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}


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

	private String lBDCDYH;
	private boolean modify_lBDCDYH = false;

	public String getLBDCDYH() {
		return lBDCDYH;
	}

	public void setLBDCDYH(String lBDCDYH) {
		if (this.lBDCDYH != lBDCDYH) {
			this.lBDCDYH = lBDCDYH;
			modify_lBDCDYH = true;
		}
	}

	private String lZL;
	private boolean modify_lZL = false;

	public String getLZL() {
		return lZL;
	}

	public void setLZL(String lZL) {
		if (this.lZL != lZL) {
			this.lZL = lZL;
			modify_lZL = true;
		}
	}

	private String lBDCQZH;
	private boolean modify_lBDCQZH = false;

	public String getLBDCQZH() {
		return lBDCQZH;
	}

	public void setLBDCQZH(String lBDCQZH) {
		if (this.lBDCQZH != lBDCQZH) {
			this.lBDCQZH = lBDCQZH;
			modify_lBDCQZH = true;
		}
	}

	private String lCQR;
	private boolean modify_lCQR = false;

	public String getLCQR() {
		return lCQR;
	}

	public void setLCQR(String lCQR) {
		if (this.lCQR != lCQR) {
			this.lCQR = lCQR;
			modify_lCQR = true;
		}
	}

	private String xBDCDYH;
	private boolean modify_xBDCDYH = false;

	public String getXBDCDYH() {
		return xBDCDYH;
	}

	public void setXBDCDYH(String xBDCDYH) {
		if (this.xBDCDYH != xBDCDYH) {
			this.xBDCDYH = xBDCDYH;
			modify_xBDCDYH = true;
		}
	}

	private String xZL;
	private boolean modify_xZL = false;

	public String getXZL() {
		return xZL;
	}

	public void setXZL(String xZL) {
		if (this.xZL != xZL) {
			this.xZL = xZL;
			modify_xZL = true;
		}
	}

	private String xBDCQZH;
	private boolean modify_xBDCQZH = false;

	public String getXBDCQZH() {
		return xBDCQZH;
	}

	public void setXBDCQZH(String xBDCQZH) {
		if (this.xBDCQZH != xBDCQZH) {
			this.xBDCQZH = xBDCQZH;
			modify_xBDCQZH = true;
		}
	}

	private String xCQR;
	private boolean modify_xCQR = false;

	public String getXCQR() {
		return xCQR;
	}

	public void setXCQR(String xCQR) {
		if (this.xCQR != xCQR) {
			this.xCQR = xCQR;
			modify_xCQR = true;
		}
	}

	private Date xGRQ;
	private boolean modify_xGRQ = false;

	public Date getXGRQ() {
		return xGRQ;
	}

	public void setXGRQ(Date xGRQ) {
		if (this.xGRQ != xGRQ) {
			this.xGRQ = xGRQ;
			modify_xGRQ = true;
		}
	}

	private String xGR;
	private boolean modify_xGR = false;

	public String getXGR() {
		return xGR;
	}

	public void setXGR(String xGR) {
		if (this.xGR != xGR) {
			this.xGR = xGR;
			modify_xGR = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_lBDCDYH = false;
		modify_lZL = false;
		modify_lBDCQZH = false;
		modify_lCQR = false;
		modify_xBDCDYH = false;
		modify_xZL = false;
		modify_xBDCQZH = false;
		modify_xCQR = false;
		modify_xGRQ = false;
		modify_xGR = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_lBDCDYH)
			listStrings.add("lBDCDYH");
		if (!modify_lZL)
			listStrings.add("lZL");
		if (!modify_lBDCQZH)
			listStrings.add("lBDCQZH");
		if (!modify_lCQR)
			listStrings.add("lCQR");
		if (!modify_xBDCDYH)
			listStrings.add("xBDCDYH");
		if (!modify_xZL)
			listStrings.add("xZL");
		if (!modify_xBDCQZH)
			listStrings.add("xBDCQZH");
		if (!modify_xCQR)
			listStrings.add("xCQR");
		if (!modify_xGRQ)
			listStrings.add("xGRQ");
		if (!modify_xGR)
			listStrings.add("xGR");

		return StringHelper.ListToStrings(listStrings);
	}
}
