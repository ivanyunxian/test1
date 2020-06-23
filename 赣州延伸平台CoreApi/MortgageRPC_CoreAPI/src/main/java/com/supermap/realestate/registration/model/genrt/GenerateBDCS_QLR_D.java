package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Internal Entity bdcs_qlr_gz 
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

public class GenerateBDCS_QLR_D implements SuperModel<String>{

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

	private String zJZL;
	private boolean modify_zJZL = false;

	public String getZJZL() {
		return zJZL;
	}

	public void setZJZL(String zJZL) {
		if (this.zJZL != zJZL) {
			this.zJZL = zJZL;
			modify_zJZL = true;
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

	private String fZJG;
	private boolean modify_fZJG = false;

	public String getFZJG() {
		return fZJG;
	}

	public void setFZJG(String fZJG) {
		if (this.fZJG != fZJG) {
			this.fZJG = fZJG;
			modify_fZJG = true;
		}
	}

	private String sSHY;
	private boolean modify_sSHY = false;

	public String getSSHY() {
		return sSHY;
	}

	public void setSSHY(String sSHY) {
		if (this.sSHY != sSHY) {
			this.sSHY = sSHY;
			modify_sSHY = true;
		}
	}

	private String gJ;
	private boolean modify_gJ = false;

	public String getGJ() {
		return gJ;
	}

	public void setGJ(String gJ) {
		if (this.gJ != gJ) {
			this.gJ = gJ;
			modify_gJ = true;
		}
	}

	private String hJSZSS;
	private boolean modify_hJSZSS = false;

	public String getHJSZSS() {
		return hJSZSS;
	}

	public void setHJSZSS(String hJSZSS) {
		if (this.hJSZSS != hJSZSS) {
			this.hJSZSS = hJSZSS;
			modify_hJSZSS = true;
		}
	}

	private String xB;
	private boolean modify_xB = false;

	public String getXB() {
		return xB;
	}

	public void setXB(String xB) {
		if (this.xB != xB) {
			this.xB = xB;
			modify_xB = true;
		}
	}

	private String dH;
	private boolean modify_dH = false;

	public String getDH() {
		return dH;
	}

	public void setDH(String dH) {
		if (this.dH != dH) {
			this.dH = dH;
			modify_dH = true;
		}
	}

	private String dZ;
	private boolean modify_dZ = false;

	public String getDZ() {
		return dZ;
	}

	public void setDZ(String dZ) {
		if (this.dZ != dZ) {
			this.dZ = dZ;
			modify_dZ = true;
		}
	}

	private String yB;
	private boolean modify_yB = false;

	public String getYB() {
		return yB;
	}

	public void setYB(String yB) {
		if (this.yB != yB) {
			this.yB = yB;
			modify_yB = true;
		}
	}

	private String gZDW;
	private boolean modify_gZDW = false;

	public String getGZDW() {
		return gZDW;
	}

	public void setGZDW(String gZDW) {
		if (this.gZDW != gZDW) {
			this.gZDW = gZDW;
			modify_gZDW = true;
		}
	}

	private String dZYJ;
	private boolean modify_dZYJ = false;

	public String getDZYJ() {
		return dZYJ;
	}

	public void setDZYJ(String dZYJ) {
		if (this.dZYJ != dZYJ) {
			this.dZYJ = dZYJ;
			modify_dZYJ = true;
		}
	}

	private String qLRLX;
	private boolean modify_qLRLX = false;

	public String getQLRLX() {
		return qLRLX;
	}

	public void setQLRLX(String qLRLX) {
		if (this.qLRLX != qLRLX) {
			this.qLRLX = qLRLX;
			modify_qLRLX = true;
		}
	}

	private Double qLMJ;
	private boolean modify_qLMJ = false;

	public Double getQLMJ() {
		return qLMJ;
	}

	public void setQLMJ(Double qLMJ) {
		if (this.qLMJ != qLMJ) {
			this.qLMJ = qLMJ;
			modify_qLMJ = true;
		}
	}

	private String qLBL;
	private boolean modify_qLBL = false;

	public String getQLBL() {
		return qLBL;
	}

	public void setQLBL(String qLBL) {
		if (this.qLBL != qLBL) {
			this.qLBL = qLBL;
			modify_qLBL = true;
		}
	}

	private String gYFS;
	private boolean modify_gYFS = false;

	public String getGYFS() {
		return gYFS;
	}

	public void setGYFS(String gYFS) {
		if (this.gYFS != gYFS) {
			this.gYFS = gYFS;
			modify_gYFS = true;
		}
	}

	private String gYQK;
	private boolean modify_gYQK = false;

	public String getGYQK() {
		return gYQK;
	}

	public void setGYQK(String gYQK) {
		if (this.gYQK != gYQK) {
			this.gYQK = gYQK;
			modify_gYQK = true;
		}
	}

	private String fDDBR;
	private boolean modify_fDDBR = false;

	public String getFDDBR() {
		return fDDBR;
	}

	public void setFDDBR(String fDDBR) {
		if (this.fDDBR != fDDBR) {
			this.fDDBR = fDDBR;
			modify_fDDBR = true;
		}
	}

	private String fDDBRZJLX;
	private boolean modify_fDDBRZJLX = false;

	public String getFDDBRZJLX() {
		return fDDBRZJLX;
	}

	public void setFDDBRZJLX(String fDDBRZJLX) {
		if (this.fDDBRZJLX != fDDBRZJLX) {
			this.fDDBRZJLX = fDDBRZJLX;
			modify_fDDBRZJLX = true;
		}
	}

	private String fDDBRZJHM;
	private boolean modify_fDDBRZJHM = false;

	public String getFDDBRZJHM() {
		return fDDBRZJHM;
	}

	public void setFDDBRZJHM(String fDDBRZJHM) {
		if (this.fDDBRZJHM != fDDBRZJHM) {
			this.fDDBRZJHM = fDDBRZJHM;
			modify_fDDBRZJHM = true;
		}
	}

	private String fDDBRDH;
	private boolean modify_fDDBRDH = false;

	public String getFDDBRDH() {
		return fDDBRDH;
	}

	public void setFDDBRDH(String fDDBRDH) {
		if (this.fDDBRDH != fDDBRDH) {
			this.fDDBRDH = fDDBRDH;
			modify_fDDBRDH = true;
		}
	}

	private String dLRXM;
	private boolean modify_dLRXM = false;

	public String getDLRXM() {
		return dLRXM;
	}

	public void setDLRXM(String dLRXM) {
		if (this.dLRXM != dLRXM) {
			this.dLRXM = dLRXM;
			modify_dLRXM = true;
		}
	}

	private String dLJGMC;
	private boolean modify_dLJGMC = false;

	public String getDLJGMC() {
		return dLJGMC;
	}

	public void setDLJGMC(String dLJGMC) {
		if (this.dLJGMC != dLJGMC) {
			this.dLJGMC = dLJGMC;
			modify_dLJGMC = true;
		}
	}

	private String dLRZJLX;
	private boolean modify_dLRZJLX = false;

	public String getDLRZJLX() {
		return dLRZJLX;
	}

	public void setDLRZJLX(String dLRZJLX) {
		if (this.dLRZJLX != dLRZJLX) {
			this.dLRZJLX = dLRZJLX;
			modify_dLRZJLX = true;
		}
	}

	private String dLRZJHM;
	private boolean modify_dLRZJHM = false;

	public String getDLRZJHM() {
		return dLRZJHM;
	}

	public void setDLRZJHM(String dLRZJHM) {
		if (this.dLRZJHM != dLRZJHM) {
			this.dLRZJHM = dLRZJHM;
			modify_dLRZJHM = true;
		}
	}

	private String dLRLXDH;
	private boolean modify_dLRLXDH = false;

	public String getDLRLXDH() {
		return dLRLXDH;
	}

	public void setDLRLXDH(String dLRLXDH) {
		if (this.dLRLXDH != dLRLXDH) {
			this.dLRLXDH = dLRLXDH;
			modify_dLRLXDH = true;
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
	
	private String iSCZR;
	private boolean modify_iSCZR = false;

	public String getISCZR() {
		return iSCZR;
	}

	public void setISCZR(String iSCZR) {
		if (this.iSCZR != iSCZR) {
			this.iSCZR = iSCZR;
			modify_iSCZR = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_qLID = false;
		modify_sQRID = false;
		modify_sXH = false;
		modify_qLRMC = false;
		modify_bDCQZH = false;
		modify_zJZL = false;
		modify_zJH = false;
		modify_fZJG = false;
		modify_sSHY = false;
		modify_gJ = false;
		modify_hJSZSS = false;
		modify_xB = false;
		modify_dH = false;
		modify_dZ = false;
		modify_yB = false;
		modify_gZDW = false;
		modify_dZYJ = false;
		modify_qLRLX = false;
		modify_qLMJ = false;
		modify_qLBL = false;
		modify_gYFS = false;
		modify_gYQK = false;
		modify_fDDBR = false;
		modify_fDDBRZJLX = false;
		modify_fDDBRZJHM = false;
		modify_fDDBRDH = false;
		modify_dLRXM = false;
		modify_dLJGMC = false;
		modify_dLRZJLX = false;
		modify_dLRZJHM = false;
		modify_dLRLXDH = false;
		modify_yXBZ = false;
		modify_bZ = false;
		modify_dCXMID = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_iSCZR=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_sQRID)
			listStrings.add("sQRID");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_qLRMC)
			listStrings.add("qLRMC");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_zJZL)
			listStrings.add("zJZL");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_fZJG)
			listStrings.add("fZJG");
		if (!modify_sSHY)
			listStrings.add("sSHY");
		if (!modify_gJ)
			listStrings.add("gJ");
		if (!modify_hJSZSS)
			listStrings.add("hJSZSS");
		if (!modify_xB)
			listStrings.add("xB");
		if (!modify_dH)
			listStrings.add("dH");
		if (!modify_dZ)
			listStrings.add("dZ");
		if (!modify_yB)
			listStrings.add("yB");
		if (!modify_gZDW)
			listStrings.add("gZDW");
		if (!modify_dZYJ)
			listStrings.add("dZYJ");
		if (!modify_qLRLX)
			listStrings.add("qLRLX");
		if (!modify_qLMJ)
			listStrings.add("qLMJ");
		if (!modify_qLBL)
			listStrings.add("qLBL");
		if (!modify_gYFS)
			listStrings.add("gYFS");
		if (!modify_gYQK)
			listStrings.add("gYQK");
		if (!modify_fDDBR)
			listStrings.add("fDDBR");
		if (!modify_fDDBRZJLX)
			listStrings.add("fDDBRZJLX");
		if (!modify_fDDBRZJHM)
			listStrings.add("fDDBRZJHM");
		if (!modify_fDDBRDH)
			listStrings.add("fDDBRDH");
		if (!modify_dLRXM)
			listStrings.add("dLRXM");
		if (!modify_dLJGMC)
			listStrings.add("dLJGMC");
		if (!modify_dLRZJLX)
			listStrings.add("dLRZJLX");
		if (!modify_dLRZJHM)
			listStrings.add("dLRZJHM");
		if (!modify_dLRLXDH)
			listStrings.add("dLRLXDH");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if(!modify_iSCZR){
			listStrings.add("modify_iSCZR");
		}

		return StringHelper.ListToStrings(listStrings);
	}
}
