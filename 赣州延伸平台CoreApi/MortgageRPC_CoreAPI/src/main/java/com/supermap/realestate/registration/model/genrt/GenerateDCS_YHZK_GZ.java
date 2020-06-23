package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-25 
//* ----------------------------------------
//* Internal Entity bdcs_yhzk_gz 
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

public class GenerateDCS_YHZK_GZ implements SuperModel<String> {

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

	private String zHDM;
	private boolean modify_zHDM = false;

	public String getZHDM() {
		return zHDM;
	}

	public void setZHDM(String zHDM) {
		if (this.zHDM != zHDM) {
			this.zHDM = zHDM;
			modify_zHDM = true;
		}
	}

	private String yHFS;
	private boolean modify_yHFS = false;

	public String getYHFS() {
		return yHFS;
	}

	public void setYHFS(String yHFS) {
		if (this.yHFS != yHFS) {
			this.yHFS = yHFS;
			modify_yHFS = true;
		}
	}

	private Double yHMJ;
	private boolean modify_yHMJ = false;

	public Double getYHMJ() {
		return yHMJ;
	}

	public void setYHMJ(Double yHMJ) {
		if (this.yHMJ != yHMJ) {
			this.yHMJ = yHMJ;
			modify_yHMJ = true;
		}
	}

	private String jTYT;
	private boolean modify_jTYT = false;

	public String getJTYT() {
		return jTYT;
	}

	public void setJTYT(String jTYT) {
		if (this.jTYT != jTYT) {
			this.jTYT = jTYT;
			modify_jTYT = true;
		}
	}

	private Double sYJSE;
	private boolean modify_sYJSE = false;

	public Double getSYJSE() {
		return sYJSE;
	}

	public void setSYJSE(Double sYJSE) {
		if (this.sYJSE != sYJSE) {
			this.sYJSE = sYJSE;
			modify_sYJSE = true;
		}
	}

	private String dCXMID;
	private boolean modify_dCXMID = false;

	public String getDCXMID() {
		return dCXMID;
	}

	public void setDCXMID(String dCXMID) {
		if (this.dCXMID != dCXMID) {
			this.dCXMID = dCXMID;
			modify_dCXMID = true;
		}
	}

	private Date cREATETIME;
	private boolean modify_cREATETIME = false;

	public Date getCREATETIME() {
		return cREATETIME;
	}

	public void setCREATETIME(Date cREATETIME) {
		if (this.cREATETIME != cREATETIME) {
			this.cREATETIME = cREATETIME;
			modify_cREATETIME = true;
		}
	}

	private Date mODIFYTIME;
	private boolean modify_mODIFYTIME = false;

	public Date getMODIFYTIME() {
		return mODIFYTIME;
	}

	public void setMODIFYTIME(Date mODIFYTIME) {
		if (this.mODIFYTIME != mODIFYTIME) {
			this.mODIFYTIME = mODIFYTIME;
			modify_mODIFYTIME = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_xMBH = false;
		modify_zHDM = false;
		modify_yHFS = false;
		modify_yHMJ = false;
		modify_jTYT = false;
		modify_sYJSE = false;
		modify_dCXMID = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
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
		if (!modify_zHDM)
			listStrings.add("zHDM");
		if (!modify_yHFS)
			listStrings.add("yHFS");
		if (!modify_yHMJ)
			listStrings.add("yHMJ");
		if (!modify_jTYT)
			listStrings.add("jTYT");
		if (!modify_sYJSE)
			listStrings.add("sYJSE");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
