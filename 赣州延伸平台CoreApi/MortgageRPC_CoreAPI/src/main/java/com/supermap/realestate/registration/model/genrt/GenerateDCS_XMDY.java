package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-3 
//* ----------------------------------------
//* Internal Entity BDCS_XMDY 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateDCS_XMDY implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = (String) SuperHelper.GeneratePrimaryKey();
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

	private String lDYID;
	private boolean modify_lDYID = false;

	public String getLDYID() {
		return lDYID;
	}

	public void setLDYID(String lDYID) {
		if (this.lDYID != lDYID) {
			this.lDYID = lDYID;
			modify_lDYID = true;
		}
	}

	private String lDYBH;
	private boolean modify_lDYBH = false;

	public String getLDYBH() {
		return lDYBH;
	}

	public void setLDYBH(String lDYBH) {
		if (this.lDYBH != lDYBH) {
			this.lDYBH = lDYBH;
			modify_lDYBH = true;
		}
	}

	private String lQLRID;
	private boolean modify_lQLRID = false;

	public String getLQLRID() {
		return lQLRID;
	}

	public void setLQLRID(String lQLRID) {
		if (this.lQLRID != lQLRID) {
			this.lQLRID = lQLRID;
			modify_lQLRID = true;
		}
	}

	private String lQLID;
	private boolean modify_lQLID = false;

	public String getLQLID() {
		return lQLID;
	}

	public void setLQLID(String lQLID) {
		if (this.lQLID != lQLID) {
			this.lQLID = lQLID;
			modify_lQLID = true;
		}
	}

	private String xDYID;
	private boolean modify_xDYID = false;

	public String getXDYID() {
		return xDYID;
	}

	public void setXDYID(String xDYID) {
		if (this.xDYID != xDYID) {
			this.xDYID = xDYID;
			modify_xDYID = true;
		}
	}

	private String xDYBH;
	private boolean modify_xDYBH = false;

	public String getXDYBH() {
		return xDYBH;
	}

	public void setXDYBH(String xDYBH) {
		if (this.xDYBH != xDYBH) {
			this.xDYBH = xDYBH;
			modify_xDYBH = true;
		}
	}

	private String xQLRID;
	private boolean modify_xQLRID = false;

	public String getXQLRID() {
		return xQLRID;
	}

	public void setXQLRID(String xQLRID) {
		if (this.xQLRID != xQLRID) {
			this.xQLRID = xQLRID;
			modify_xQLRID = true;
		}
	}

	private String xQLID;
	private boolean modify_xQLID = false;

	public String getXQLID() {
		return xQLID;
	}

	public void setXQLID(String xQLID) {
		if (this.xQLID != xQLID) {
			this.xQLID = xQLID;
			modify_xQLID = true;
		}
	}

	private Date dJSJ;
	private boolean modify_dJSJ = false;

	public Date getDJSJ() {
		return dJSJ;
	}

	public void setDJSJ(Date dJSJ) {
		if (this.dJSJ != dJSJ) {
			this.dJSJ = dJSJ;
			modify_dJSJ = true;
		}
	}

	private String sQRID;
	private boolean modify_sQRID = false;

	public String getSQRID() {
		return sQRID;
	}

	public void setSQRID(String sQRID) {
		if (this.sQRID != sQRID) {
			this.sQRID = sQRID;
			modify_sQRID = true;
		}
	}

	private Integer bDCDYLX;
	private boolean modify_bDCDYLX = false;

	public Integer getBDCDYLX() {
		return bDCDYLX;
	}

	public void setBDCDYLX(Integer bDCDYLX) {
		if (this.bDCDYLX != bDCDYLX) {
			this.bDCDYLX = bDCDYLX;
			modify_bDCDYLX = true;
		}
	}

	private String bDCDYLXMC;
	private boolean modify_bDCDYLXMC = false;

	public String getBDCDYLXMC() {
		return bDCDYLXMC;
	}

	public void setBDCDYLXMC(String bDCDYLXMC) {
		if (this.bDCDYLXMC != bDCDYLXMC) {
			this.bDCDYLXMC = bDCDYLXMC;
			modify_bDCDYLXMC = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_lDYID = false;
		modify_lDYBH = false;
		modify_lQLRID = false;
		modify_lQLID = false;
		modify_xDYID = false;
		modify_xDYBH = false;
		modify_xQLRID = false;
		modify_xQLID = false;
		modify_dJSJ = false;
		modify_sQRID = false;
		modify_bDCDYLX = false;
		modify_bDCDYLXMC = false;
	}

	@Override
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_lDYID)
			listStrings.add("lDYID");
		if (!modify_lDYBH)
			listStrings.add("lDYBH");
		if (!modify_lQLRID)
			listStrings.add("lQLRID");
		if (!modify_lQLID)
			listStrings.add("lQLID");
		if (!modify_xDYID)
			listStrings.add("xDYID");
		if (!modify_xDYBH)
			listStrings.add("xDYBH");
		if (!modify_xQLRID)
			listStrings.add("xQLRID");
		if (!modify_xQLID)
			listStrings.add("xQLID");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if (!modify_sQRID)
			listStrings.add("sQRID");
		if (!modify_bDCDYLX)
			listStrings.add("bDCDYLX");
		if (!modify_bDCDYLXMC)
			listStrings.add("bDCDYLXMC");

		return StringHelper.ListToStrings(listStrings);
	}
}
