package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/8 
//* ----------------------------------------
//* Internal Entity bdcs_dyxz 
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

public class GenerateBDCS_DYXZ implements SuperModel<String> {

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

	private String bDCDYLX;
	private boolean modify_bDCDYLX = false;

	public String getBDCDYLX() {
		return bDCDYLX;
	}

	public void setBDCDYLX(String bDCDYLX) {
		if (this.bDCDYLX != bDCDYLX) {
			this.bDCDYLX = bDCDYLX;
			modify_bDCDYLX = true;
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

	private String bXZRMC;
	private boolean modify_bXZRMC = false;

	public String getBXZRMC() {
		return bXZRMC;
	}

	public void setBXZRMC(String bXZRMC) {
		if (this.bXZRMC != bXZRMC) {
			this.bXZRMC = bXZRMC;
			modify_bXZRMC = true;
		}
	}

	private String bXZRZJZL;
	private boolean modify_bXZRZJZL = false;

	public String getBXZRZJZL() {
		return bXZRZJZL;
	}

	public void setBXZRZJZL(String bXZRZJZL) {
		if (this.bXZRZJZL != bXZRZJZL) {
			this.bXZRZJZL = bXZRZJZL;
			modify_bXZRZJZL = true;
		}
	}

	private String bXZRZJHM;
	private boolean modify_bXZRZJHM = false;

	public String getBXZRZJHM() {
		return bXZRZJHM;
	}

	public void setBXZRZJHM(String bXZRZJHM) {
		if (this.bXZRZJHM != bXZRZJHM) {
			this.bXZRZJHM = bXZRZJHM;
			modify_bXZRZJHM = true;
		}
	}

	private String xZWJHM;
	private boolean modify_xZWJHM = false;

	public String getXZWJHM() {
		return xZWJHM;
	}

	public void setXZWJHM(String xZWJHM) {
		if (this.xZWJHM != xZWJHM) {
			this.xZWJHM = xZWJHM;
			modify_xZWJHM = true;
		}
	}

	private String xZDW;
	private boolean modify_xZDW = false;

	public String getXZDW() {
		return xZDW;
	}

	public void setXZDW(String xZDW) {
		if (this.xZDW != xZDW) {
			this.xZDW = xZDW;
			modify_xZDW = true;
		}
	}

	private Date sDTZRQ;
	private boolean modify_sDTZRQ = false;

	public Date getSDTZRQ() {
		return sDTZRQ;
	}

	public void setSDTZRQ(Date sDTZRQ) {
		if (this.sDTZRQ != sDTZRQ) {
			this.sDTZRQ = sDTZRQ;
			modify_sDTZRQ = true;
		}
	}

	private Date xZQSRQ;
	private boolean modify_xZQSRQ = false;

	public Date getXZQSRQ() {
		return xZQSRQ;
	}

	public void setXZQSRQ(Date xZQSRQ) {
		if (this.xZQSRQ != xZQSRQ) {
			this.xZQSRQ = xZQSRQ;
			modify_xZQSRQ = true;
		}
	}

	private Date xZZZRQ;
	private boolean modify_xZZZRQ = false;

	public Date getXZZZRQ() {
		return xZZZRQ;
	}

	public void setXZZZRQ(Date xZZZRQ) {
		if (this.xZZZRQ != xZZZRQ) {
			this.xZZZRQ = xZZZRQ;
			modify_xZZZRQ = true;
		}
	}

	private String sLR;
	private boolean modify_sLR = false;

	public String getSLR() {
		return sLR;
	}

	public void setSLR(String sLR) {
		if (this.sLR != sLR) {
			this.sLR = sLR;
			modify_sLR = true;
		}
	}

	private String sLRYJ;
	private boolean modify_sLRYJ = false;

	public String getSLRYJ() {
		return sLRYJ;
	}

	public void setSLRYJ(String sLRYJ) {
		if (this.sLRYJ != sLRYJ) {
			this.sLRYJ = sLRYJ;
			modify_sLRYJ = true;
		}
	}

	private String xZLX;
	private boolean modify_xZLX = false;

	public String getXZLX() {
		return xZLX;
	}

	public void setXZLX(String xZLX) {
		if (this.xZLX != xZLX) {
			this.xZLX = xZLX;
			modify_xZLX = true;
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

	private String lSXZ;
	private boolean modify_lSXZ = false;

	public String getLSXZ() {
		return lSXZ;
	}

	public void setLSXZ(String lSXZ) {
		if (this.lSXZ != lSXZ) {
			this.lSXZ = lSXZ;
			modify_lSXZ = true;
		}
	}

	private String xZFW;
	private boolean modify_xZFW = false;

	public String getXZFW() {
		return xZFW;
	}

	public void setXZFW(String xZFW) {
		if (this.xZFW != xZFW) {
			this.xZFW = xZFW;
			modify_xZFW = true;
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

	private String dBR;
	private boolean modify_dBR = false;

	public String getDBR() {
		return dBR;
	}

	public void setDBR(String dBR) {
		if (this.dBR != dBR) {
			this.dBR = dBR;
			modify_dBR = true;
		}
	}

	private String yWH;
	private boolean modify_yWH = false;

	public String getYWH() {
		return yWH;
	}

	public void setYWH(String yWH) {
		if (this.yWH != yWH) {
			this.yWH = yWH;
			modify_yWH = true;
		}
	}

	private String bZ;
	private boolean modify_bZ = false;

	public String getBZ() {
		return bZ;
	}

	public void setBZ(String bZ) {
		if (this.bZ != bZ) {
			this.bZ = bZ;
			modify_bZ = true;
		}
	}

	private Date zXDJSJ;
	private boolean modify_zXDJSJ = false;

	public Date getZXDJSJ() {
		return zXDJSJ;
	}

	public void setZXDJSJ(Date zXDJSJ) {
		if (this.zXDJSJ != zXDJSJ) {
			this.zXDJSJ = zXDJSJ;
			modify_zXDJSJ = true;
		}
	}

	private String zXDBR;
	private boolean modify_zXDBR = false;

	public String getZXDBR() {
		return zXDBR;
	}

	public void setZXDBR(String zXDBR) {
		if (this.zXDBR != zXDBR) {
			this.zXDBR = zXDBR;
			modify_zXDBR = true;
		}
	}

	private String zXYWH;
	private boolean modify_zXYWH = false;

	public String getZXYWH() {
		return zXYWH;
	}

	public void setZXYWH(String zXYWH) {
		if (this.zXYWH != zXYWH) {
			this.zXYWH = zXYWH;
			modify_zXYWH = true;
		}
	}

	private String zXBZ;
	private boolean modify_zXBZ = false;

	public String getZXBZ() {
		return zXBZ;
	}

	public void setZXBZ(String zXBZ) {
		if (this.zXBZ != zXBZ) {
			this.zXBZ = zXBZ;
			modify_zXBZ = true;
		}
	}

	private String zXYJ;
	private boolean modify_zXYJ = false;

	public String getZXYJ() {
		return zXYJ;
	}

	public void setZXYJ(String zXYJ) {
		if (this.zXYJ != zXYJ) {
			this.zXYJ = zXYJ;
			modify_zXYJ = true;
		}
	}

	private String zXXZWJHM;
	private boolean modify_zXXZWJHM = false;

	public String getZXXZWJHM() {
		return zXXZWJHM;
	}

	public void setZXXZWJHM(String zXXZWJHM) {
		if (this.zXXZWJHM != zXXZWJHM) {
			this.zXXZWJHM = zXXZWJHM;
			modify_zXXZWJHM = true;
		}
	}

	private String zXXZDW;
	private boolean modify_zXXZDW = false;

	public String getZXXZDW() {
		return zXXZDW;
	}

	public void setZXXZDW(String zXXZDW) {
		if (this.zXXZDW != zXXZDW) {
			this.zXXZDW = zXXZDW;
			modify_zXXZDW = true;
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
		modify_bDCDYLX = false;
		modify_bDCQZH = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_bXZRMC = false;
		modify_bXZRZJZL = false;
		modify_bXZRZJHM = false;
		modify_xZWJHM = false;
		modify_xZDW = false;
		modify_sDTZRQ = false;
		modify_xZQSRQ = false;
		modify_xZZZRQ = false;
		modify_sLR = false;
		modify_sLRYJ = false;
		modify_xZLX = false;
		modify_yXBZ = false;
		modify_lSXZ = false;
		modify_xZFW = false;
		modify_dJSJ = false;
		modify_dBR = false;
		modify_yWH = false;
		modify_bZ = false;
		modify_zXDJSJ = false;
		modify_zXDBR = false;
		modify_zXYWH = false;
		modify_zXBZ = false;
		modify_zXYJ = false;
		modify_zXXZWJHM = false;
		modify_zXXZDW = false;
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
		if (!modify_bDCDYLX)
			listStrings.add("bDCDYLX");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_bXZRMC)
			listStrings.add("bXZRMC");
		if (!modify_bXZRZJZL)
			listStrings.add("bXZRZJZL");
		if (!modify_bXZRZJHM)
			listStrings.add("bXZRZJHM");
		if (!modify_xZWJHM)
			listStrings.add("xZWJHM");
		if (!modify_xZDW)
			listStrings.add("xZDW");
		if (!modify_sDTZRQ)
			listStrings.add("sDTZRQ");
		if (!modify_xZQSRQ)
			listStrings.add("xZQSRQ");
		if (!modify_xZZZRQ)
			listStrings.add("xZZZRQ");
		if (!modify_sLR)
			listStrings.add("sLR");
		if (!modify_sLRYJ)
			listStrings.add("sLRYJ");
		if (!modify_xZLX)
			listStrings.add("xZLX");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_lSXZ)
			listStrings.add("lSXZ");
		if (!modify_xZFW)
			listStrings.add("xZFW");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if (!modify_dBR)
			listStrings.add("dBR");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_zXDJSJ)
			listStrings.add("zXDJSJ");
		if (!modify_zXDBR)
			listStrings.add("zXDBR");
		if (!modify_zXYWH)
			listStrings.add("zXYWH");
		if (!modify_zXBZ)
			listStrings.add("zXBZ");
		if (!modify_zXYJ)
			listStrings.add("zXYJ");
		if (!modify_zXXZWJHM)
			listStrings.add("zXXZWJHM");
		if (!modify_zXXZDW)
			listStrings.add("zXXZDW");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
