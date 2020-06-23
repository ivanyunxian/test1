package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2017-05-24 
//* ----------------------------------------
//* Internal Entity t_config 
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

public class GenerateBDCS_GEO implements SuperModel<String> {

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

	private String bDCDYH;
	private boolean modify_bDCDYH = false;

	public String getBDCDYH() {
		return bDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		if (this.bDCDYH != bDCDYH) {
			this.bDCDYH = bDCDYH;
			modify_bDCDYH = true;
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

	private String zL;
	private boolean modify_zL = false;

	public String getZL() {
		return zL;
	}

	public void setZL(String zL) {
		if (this.zL != zL) {
			this.zL = zL;
			modify_zL = true;
		}
	}

	private String fWBM;
	private boolean modify_fWBM = false;

	public String getFWBM() {
		return fWBM;
	}

	public void setFWBM(String fWBM) {
		if (this.fWBM != fWBM) {
			this.fWBM = fWBM;
			modify_fWBM = true;
		}
	}

	private String zRZBDCDYID;
	private boolean modify_zRZBDCDYID = false;

	public String getZRZBDCDYID() {
		return zRZBDCDYID;
	}

	public void setZRZBDCDYID(String zRZBDCDYID) {
		if (this.zRZBDCDYID != zRZBDCDYID) {
			this.zRZBDCDYID = zRZBDCDYID;
			modify_zRZBDCDYID = true;
		}
	}
	
	private String zRZBDCDYH;
	private boolean modify_zRZBDCDYH = false;

	public String getZRZBDCDYH() {
		return zRZBDCDYH;
	}

	public void setZRZBDCDYH(String zRZBDCDYH) {
		if (this.zRZBDCDYH != zRZBDCDYH) {
			this.zRZBDCDYH = zRZBDCDYH;
			modify_zRZBDCDYH = true;
		}
	}
	
	private Double xZB;
	private boolean modify_xZB = false;

	public Double getXZB() {
		return xZB;
	}

	public void setXZB(Double xZB) {
		if (this.xZB != xZB) {
			this.xZB = xZB;
			modify_xZB = true;
		}
	}

	private Double yZB;
	private boolean modify_yZB = false;

	public Double getYZB() {
		return yZB;
	}

	public void setYZB(Double yZB) {
		if (this.yZB != yZB) {
			this.yZB = yZB;
			modify_yZB = true;
		}
	}

	private String gL;
	private boolean modify_gL = false;

	public String getGL() {
		return gL;
	}

	public void setGL(String gL) {
		if (this.gL != gL) {
			this.gL = gL;
			modify_gL = true;
		}
	}

	private String zD;
	private boolean modify_zD = false;

	public String getZD() {
		return zD;
	}

	public void setZD(String zD) {
		if (this.zD != zD) {
			this.zD = zD;
			modify_zD = true;
		}
	}

	private Date tIME;
	private boolean modify_tIME = false;

	public Date getTIME() {
		return tIME;
	}

	public void setTIME(Date tIME) {
		if (this.tIME != tIME) {
			this.tIME = tIME;
			modify_tIME = true;
		}
	}

	private String zRRY;
	private boolean modify_zRRY = false;

	public String getZRRY() {
		return zRRY;
	}

	public void setZRRY(String zRRY) {
		if (this.zRRY != zRRY) {
			this.zRRY = zRRY;
			modify_zRRY = true;
		}
	}

	private String sM;
	private boolean modify_sM = false;

	public String getSM() {
		return sM;
	}

	public void setSM(String sM) {
		if (this.sM != sM) {
			this.sM = sM;
			modify_sM = true;
		}
	}
	
	private String pICTURE;
	private boolean modify_pICTURE = false;

	public String getPICTURE() {
		return pICTURE;
	}

	public void setPICTURE(String pICTURE) {
		if (this.pICTURE != pICTURE) {
			this.pICTURE = pICTURE;
			modify_pICTURE = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_zL = false;
		modify_fWBM = false;
		modify_zRZBDCDYID = false;
		modify_zRZBDCDYH = false;
		modify_xZB = false;
		modify_yZB = false;
		modify_gL = false;
		modify_zD = false;
		modify_tIME = false;
		modify_zRRY = false;
		modify_sM = false;
		modify_pICTURE = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_fWBM)
			listStrings.add("fWBM");
		if (!modify_zRZBDCDYID)
			listStrings.add("zRZBDCDYID");
		if (!modify_zRZBDCDYH)
			listStrings.add("zRZBDCDYH");
		if (!modify_xZB)
			listStrings.add("xZB");
		if (!modify_yZB)
			listStrings.add("yZB");
		if (!modify_gL)
			listStrings.add("gL");
		if (!modify_zD)
			listStrings.add("zD");
		if (!modify_tIME)
			listStrings.add("tIME");
		if (!modify_zRRY)
			listStrings.add("zRRY");
		if (!modify_sM)
			listStrings.add("sM");
		if (!modify_pICTURE)
			listStrings.add("pICTURE");

		return StringHelper.ListToStrings(listStrings);
	}
}
