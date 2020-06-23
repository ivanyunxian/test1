package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-23 
//* ----------------------------------------
//* Internal Entity bdcs_syqzd_xz 
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

public class GenerateBDCS_SYQZD_XZ implements SuperModel<String> {

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

	private String ySDM;
	private boolean modify_ySDM = false;

	public String getYSDM() {
		return ySDM;
	}

	public void setYSDM(String ySDM) {
		if (this.ySDM != ySDM) {
			this.ySDM = ySDM;
			modify_ySDM = true;
		}
	}

	private String zDDM;
	private boolean modify_zDDM = false;

	public String getZDDM() {
		return zDDM;
	}

	public void setZDDM(String zDDM) {
		if (this.zDDM != zDDM) {
			this.zDDM = zDDM;
			modify_zDDM = true;
		}
	}

	private String zDTZM;
	private boolean modify_zDTZM = false;

	public String getZDTZM() {
		return zDTZM;
	}

	public void setZDTZM(String zDTZM) {
		if (this.zDTZM != zDTZM) {
			this.zDTZM = zDTZM;
			modify_zDTZM = true;
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

	private Double zDMJ;
	private boolean modify_zDMJ = false;

	public Double getZDMJ() {
		return zDMJ;
	}

	public void setZDMJ(Double zDMJ) {
		if (this.zDMJ != zDMJ) {
			this.zDMJ = zDMJ;
			modify_zDMJ = true;
		}
	}

	private String mJDW;
	private boolean modify_mJDW = false;

	public String getMJDW() {
		return mJDW;
	}

	public void setMJDW(String mJDW) {
		if (this.mJDW != mJDW) {
			this.mJDW = mJDW;
			modify_mJDW = true;
		}
	}

	private String yT;
	private boolean modify_yT = false;

	public String getYT() {
		return yT;
	}

	public void setYT(String yT) {
		if (this.yT != yT) {
			this.yT = yT;
			modify_yT = true;
		}
	}

	private String dJ;
	private boolean modify_dJ = false;

	public String getDJ() {
		return dJ;
	}

	public void setDJ(String dJ) {
		if (this.dJ != dJ) {
			this.dJ = dJ;
			modify_dJ = true;
		}
	}

	private Double jG;
	private boolean modify_jG = false;

	public Double getJG() {
		return jG;
	}

	public void setJG(Double jG) {
		if (this.jG != jG) {
			this.jG = jG;
			modify_jG = true;
		}
	}

	private String qLLX;
	private boolean modify_qLLX = false;

	public String getQLLX() {
		return qLLX;
	}

	public void setQLLX(String qLLX) {
		if (this.qLLX != qLLX) {
			this.qLLX = qLLX;
			modify_qLLX = true;
		}
	}

	private String qLXZ;
	private boolean modify_qLXZ = false;

	public String getQLXZ() {
		return qLXZ;
	}

	public void setQLXZ(String qLXZ) {
		if (this.qLXZ != qLXZ) {
			this.qLXZ = qLXZ;
			modify_qLXZ = true;
		}
	}

	private String qLSDFS;
	private boolean modify_qLSDFS = false;

	public String getQLSDFS() {
		return qLSDFS;
	}

	public void setQLSDFS(String qLSDFS) {
		if (this.qLSDFS != qLSDFS) {
			this.qLSDFS = qLSDFS;
			modify_qLSDFS = true;
		}
	}

	private String rJL;
	private boolean modify_rJL = false;

	public String getRJL() {
		return rJL;
	}

	public void setRJL(String rJL) {
		if (this.rJL != rJL) {
			this.rJL = rJL;
			modify_rJL = true;
		}
	}

	private String jZMD;
	private boolean modify_jZMD = false;

	public String getJZMD() {
		return jZMD;
	}

	public void setJZMD(String jZMD) {
		if (this.jZMD != jZMD) {
			this.jZMD = jZMD;
			modify_jZMD = true;
		}
	}

	private Double jZXG;
	private boolean modify_jZXG = false;

	public Double getJZXG() {
		return jZXG;
	}

	public void setJZXG(Double jZXG) {
		if (this.jZXG != jZXG) {
			this.jZXG = jZXG;
			modify_jZXG = true;
		}
	}

	private String zDSZD;
	private boolean modify_zDSZD = false;

	public String getZDSZD() {
		return zDSZD;
	}

	public void setZDSZD(String zDSZD) {
		if (this.zDSZD != zDSZD) {
			this.zDSZD = zDSZD;
			modify_zDSZD = true;
		}
	}

	private String zDSZX;
	private boolean modify_zDSZX = false;

	public String getZDSZX() {
		return zDSZX;
	}

	public void setZDSZX(String zDSZX) {
		if (this.zDSZX != zDSZX) {
			this.zDSZX = zDSZX;
			modify_zDSZX = true;
		}
	}

	private String zDSZN;
	private boolean modify_zDSZN = false;

	public String getZDSZN() {
		return zDSZN;
	}

	public void setZDSZN(String zDSZN) {
		if (this.zDSZN != zDSZN) {
			this.zDSZN = zDSZN;
			modify_zDSZN = true;
		}
	}

	private String zDSZB;
	private boolean modify_zDSZB = false;

	public String getZDSZB() {
		return zDSZB;
	}

	public void setZDSZB(String zDSZB) {
		if (this.zDSZB != zDSZB) {
			this.zDSZB = zDSZB;
			modify_zDSZB = true;
		}
	}

	private Double nYDMJ;
	private boolean modify_nYDMJ = false;

	public Double getNYDMJ() {
		return nYDMJ;
	}

	public void setNYDMJ(Double nYDMJ) {
		if (this.nYDMJ != nYDMJ) {
			this.nYDMJ = nYDMJ;
			modify_nYDMJ = true;
		}
	}

	private Double gDMJ;
	private boolean modify_gDMJ = false;

	public Double getGDMJ() {
		return gDMJ;
	}

	public void setGDMJ(Double gDMJ) {
		if (this.gDMJ != gDMJ) {
			this.gDMJ = gDMJ;
			modify_gDMJ = true;
		}
	}

	private Double lDMJ;
	private boolean modify_lDMJ = false;

	public Double getLDMJ() {
		return lDMJ;
	}

	public void setLDMJ(Double lDMJ) {
		if (this.lDMJ != lDMJ) {
			this.lDMJ = lDMJ;
			modify_lDMJ = true;
		}
	}

	private Double cDMJ;
	private boolean modify_cDMJ = false;

	public Double getCDMJ() {
		return cDMJ;
	}

	public void setCDMJ(Double cDMJ) {
		if (this.cDMJ != cDMJ) {
			this.cDMJ = cDMJ;
			modify_cDMJ = true;
		}
	}

	private Double qTNYDMJ;
	private boolean modify_qTNYDMJ = false;

	public Double getQTNYDMJ() {
		return qTNYDMJ;
	}

	public void setQTNYDMJ(Double qTNYDMJ) {
		if (this.qTNYDMJ != qTNYDMJ) {
			this.qTNYDMJ = qTNYDMJ;
			modify_qTNYDMJ = true;
		}
	}

	private Double jSYDMJ;
	private boolean modify_jSYDMJ = false;

	public Double getJSYDMJ() {
		return jSYDMJ;
	}

	public void setJSYDMJ(Double jSYDMJ) {
		if (this.jSYDMJ != jSYDMJ) {
			this.jSYDMJ = jSYDMJ;
			modify_jSYDMJ = true;
		}
	}

	private Double wLYDMJ;
	private boolean modify_wLYDMJ = false;

	public Double getWLYDMJ() {
		return wLYDMJ;
	}

	public void setWLYDMJ(Double wLYDMJ) {
		if (this.wLYDMJ != wLYDMJ) {
			this.wLYDMJ = wLYDMJ;
			modify_wLYDMJ = true;
		}
	}

	private String zDT;
	private boolean modify_zDT = false;

	public String getZDT() {
		return zDT;
	}

	public void setZDT(String zDT) {
		if (this.zDT != zDT) {
			this.zDT = zDT;
			modify_zDT = true;
		}
	}

	private String tFH;
	private boolean modify_tFH = false;

	public String getTFH() {
		return tFH;
	}

	public void setTFH(String tFH) {
		if (this.tFH != tFH) {
			this.tFH = tFH;
			modify_tFH = true;
		}
	}

	private String dJH;
	private boolean modify_dJH = false;

	public String getDJH() {
		return dJH;
	}

	public void setDJH(String dJH) {
		if (this.dJH != dJH) {
			this.dJH = dJH;
			modify_dJH = true;
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

	private String qXDM;
	private boolean modify_qXDM = false;

	public String getQXDM() {
		return qXDM;
	}

	public void setQXDM(String qXDM) {
		if (this.qXDM != qXDM) {
			this.qXDM = qXDM;
			modify_qXDM = true;
		}
	}

	private String dJQDM;
	private boolean modify_dJQDM = false;

	public String getDJQDM() {
		return dJQDM;
	}

	public void setDJQDM(String dJQDM) {
		if (this.dJQDM != dJQDM) {
			this.dJQDM = dJQDM;
			modify_dJQDM = true;
		}
	}

	private String dJZQDM;
	private boolean modify_dJZQDM = false;

	public String getDJZQDM() {
		return dJZQDM;
	}

	public void setDJZQDM(String dJZQDM) {
		if (this.dJZQDM != dJZQDM) {
			this.dJZQDM = dJZQDM;
			modify_dJZQDM = true;
		}
	}

	private String qXMC;
	private boolean modify_qXMC = false;

	public String getQXMC() {
		return qXMC;
	}

	public void setQXMC(String qXMC) {
		if (this.qXMC != qXMC) {
			this.qXMC = qXMC;
			modify_qXMC = true;
		}
	}

	private String dJQMC;
	private boolean modify_dJQMC = false;

	public String getDJQMC() {
		return dJQMC;
	}

	public void setDJQMC(String dJQMC) {
		if (this.dJQMC != dJQMC) {
			this.dJQMC = dJQMC;
			modify_dJQMC = true;
		}
	}

	private String dJZQMC;
	private boolean modify_dJZQMC = false;

	public String getDJZQMC() {
		return dJZQMC;
	}

	public void setDJZQMC(String dJZQMC) {
		if (this.dJZQMC != dJZQMC) {
			this.dJZQMC = dJZQMC;
			modify_dJZQMC = true;
		}
	}

	private String zH;
	private boolean modify_zH = false;

	public String getZH() {
		return zH;
	}

	public void setZH(String zH) {
		if (this.zH != zH) {
			this.zH = zH;
			modify_zH = true;
		}
	}

	private String zM;
	private boolean modify_zM = false;

	public String getZM() {
		return zM;
	}

	public void setZM(String zM) {
		if (this.zM != zM) {
			this.zM = zM;
			modify_zM = true;
		}
	}

	private String sYQLX;
	private boolean modify_sYQLX = false;

	public String getSYQLX() {
		return sYQLX;
	}

	public void setSYQLX(String sYQLX) {
		if (this.sYQLX != sYQLX) {
			this.sYQLX = sYQLX;
			modify_sYQLX = true;
		}
	}

	private String tDQSLYZMCL;
	private boolean modify_tDQSLYZMCL = false;

	public String getTDQSLYZMCL() {
		return tDQSLYZMCL;
	}

	public void setTDQSLYZMCL(String tDQSLYZMCL) {
		if (this.tDQSLYZMCL != tDQSLYZMCL) {
			this.tDQSLYZMCL = tDQSLYZMCL;
			modify_tDQSLYZMCL = true;
		}
	}

	private String gMJJHYFLDM;
	private boolean modify_gMJJHYFLDM = false;

	public String getGMJJHYFLDM() {
		return gMJJHYFLDM;
	}

	public void setGMJJHYFLDM(String gMJJHYFLDM) {
		if (this.gMJJHYFLDM != gMJJHYFLDM) {
			this.gMJJHYFLDM = gMJJHYFLDM;
			modify_gMJJHYFLDM = true;
		}
	}

	private String yBZDDM;
	private boolean modify_yBZDDM = false;

	public String getYBZDDM() {
		return yBZDDM;
	}

	public void setYBZDDM(String yBZDDM) {
		if (this.yBZDDM != yBZDDM) {
			this.yBZDDM = yBZDDM;
			modify_yBZDDM = true;
		}
	}

	private String bLC;
	private boolean modify_bLC = false;

	public String getBLC() {
		return bLC;
	}

	public void setBLC(String bLC) {
		if (this.bLC != bLC) {
			this.bLC = bLC;
			modify_bLC = true;
		}
	}

	private String pZYT;
	private boolean modify_pZYT = false;

	public String getPZYT() {
		return pZYT;
	}

	public void setPZYT(String pZYT) {
		if (this.pZYT != pZYT) {
			this.pZYT = pZYT;
			modify_pZYT = true;
		}
	}

	private Double pZMJ;
	private boolean modify_pZMJ = false;

	public Double getPZMJ() {
		return pZMJ;
	}

	public void setPZMJ(Double pZMJ) {
		if (this.pZMJ != pZMJ) {
			this.pZMJ = pZMJ;
			modify_pZMJ = true;
		}
	}

	private Double jZZDMJ;
	private boolean modify_jZZDMJ = false;

	public Double getJZZDMJ() {
		return jZZDMJ;
	}

	public void setJZZDMJ(Double jZZDMJ) {
		if (this.jZZDMJ != jZZDMJ) {
			this.jZZDMJ = jZZDMJ;
			modify_jZZDMJ = true;
		}
	}

	private Double jZMJ;
	private boolean modify_jZMJ = false;

	public Double getJZMJ() {
		return jZMJ;
	}

	public void setJZMJ(Double jZMJ) {
		if (this.jZMJ != jZMJ) {
			this.jZMJ = jZMJ;
			modify_jZMJ = true;
		}
	}

	private String jZDWSM;
	private boolean modify_jZDWSM = false;

	public String getJZDWSM() {
		return jZDWSM;
	}

	public void setJZDWSM(String jZDWSM) {
		if (this.jZDWSM != jZDWSM) {
			this.jZDWSM = jZDWSM;
			modify_jZDWSM = true;
		}
	}

	private String jZXZXSM;
	private boolean modify_jZXZXSM = false;

	public String getJZXZXSM() {
		return jZXZXSM;
	}

	public void setJZXZXSM(String jZXZXSM) {
		if (this.jZXZXSM != jZXZXSM) {
			this.jZXZXSM = jZXZXSM;
			modify_jZXZXSM = true;
		}
	}

	private String dCJS;
	private boolean modify_dCJS = false;

	public String getDCJS() {
		return dCJS;
	}

	public void setDCJS(String dCJS) {
		if (this.dCJS != dCJS) {
			this.dCJS = dCJS;
			modify_dCJS = true;
		}
	}

	private String dCR;
	private boolean modify_dCR = false;

	public String getDCR() {
		return dCR;
	}

	public void setDCR(String dCR) {
		if (this.dCR != dCR) {
			this.dCR = dCR;
			modify_dCR = true;
		}
	}

	private Date dCRQ;
	private boolean modify_dCRQ = false;

	public Date getDCRQ() {
		return dCRQ;
	}

	public void setDCRQ(Date dCRQ) {
		if (this.dCRQ != dCRQ) {
			this.dCRQ = dCRQ;
			modify_dCRQ = true;
		}
	}

	private String cLJS;
	private boolean modify_cLJS = false;

	public String getCLJS() {
		return cLJS;
	}

	public void setCLJS(String cLJS) {
		if (this.cLJS != cLJS) {
			this.cLJS = cLJS;
			modify_cLJS = true;
		}
	}

	private String cLR;
	private boolean modify_cLR = false;

	public String getCLR() {
		return cLR;
	}

	public void setCLR(String cLR) {
		if (this.cLR != cLR) {
			this.cLR = cLR;
			modify_cLR = true;
		}
	}

	private Date cLRQ;
	private boolean modify_cLRQ = false;

	public Date getCLRQ() {
		return cLRQ;
	}

	public void setCLRQ(Date cLRQ) {
		if (this.cLRQ != cLRQ) {
			this.cLRQ = cLRQ;
			modify_cLRQ = true;
		}
	}

	private String sHYJ;
	private boolean modify_sHYJ = false;

	public String getSHYJ() {
		return sHYJ;
	}

	public void setSHYJ(String sHYJ) {
		if (this.sHYJ != sHYJ) {
			this.sHYJ = sHYJ;
			modify_sHYJ = true;
		}
	}

	private String sHR;
	private boolean modify_sHR = false;

	public String getSHR() {
		return sHR;
	}

	public void setSHR(String sHR) {
		if (this.sHR != sHR) {
			this.sHR = sHR;
			modify_sHR = true;
		}
	}

	private Date sHRQ;
	private boolean modify_sHRQ = false;

	public Date getSHRQ() {
		return sHRQ;
	}

	public void setSHRQ(Date sHRQ) {
		if (this.sHRQ != sHRQ) {
			this.sHRQ = sHRQ;
			modify_sHRQ = true;
		}
	}

	private String zT;
	private boolean modify_zT = false;

	public String getZT() {
		return zT;
	}

	public void setZT(String zT) {
		if (this.zT != zT) {
			this.zT = zT;
			modify_zT = true;
		}
	}

	private String cQZT;
	private boolean modify_cQZT = false;

	public String getCQZT() {
		return cQZT;
	}

	public void setCQZT(String cQZT) {
		if (this.cQZT != cQZT) {
			this.cQZT = cQZT;
			modify_cQZT = true;
		}
	}

	private String dYZT;
	private boolean modify_dYZT = false;

	public String getDYZT() {
		return dYZT;
	}

	public void setDYZT(String dYZT) {
		if (this.dYZT != dYZT) {
			this.dYZT = dYZT;
			modify_dYZT = true;
		}
	}

	private String xZZT;
	private boolean modify_xZZT = false;

	public String getXZZT() {
		return xZZT;
	}

	public void setXZZT(String xZZT) {
		if (this.xZZT != xZZT) {
			this.xZZT = xZZT;
			modify_xZZT = true;
		}
	}

	private String bLZT;
	private boolean modify_bLZT = false;

	public String getBLZT() {
		return bLZT;
	}

	public void setBLZT(String bLZT) {
		if (this.bLZT != bLZT) {
			this.bLZT = bLZT;
			modify_bLZT = true;
		}
	}

	private String yYZT;
	private boolean modify_yYZT = false;

	public String getYYZT() {
		return yYZT;
	}

	public void setYYZT(String yYZT) {
		if (this.yYZT != yYZT) {
			this.yYZT = yYZT;
			modify_yYZT = true;
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

	private String dJZT;
	private boolean modify_dJZT = false;

	public String getDJZT() {
		return dJZT;
	}

	public void setDJZT(String dJZT) {
		if (this.dJZT != dJZT) {
			this.dJZT = dJZT;
			modify_dJZT = true;
		}
	}

	private String rELATIONID;
	private boolean modify_rELATIONID = false;

	public String getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
			modify_rELATIONID = true;
		}
	}
	
	private String fTXS;
	private boolean modify_fTXS = false;

	public String getFTXS() {
		return fTXS;
	}

	public void setFTXS(String fTXS) {
		if (this.fTXS != fTXS) {
			this.fTXS = fTXS;
			modify_fTXS = true;
		}
	}
	
	private String mS;
	private boolean modify_mS = false;

	public String getMS() {
		return mS;
	}

	public void setMS(String mS) {
		if (this.mS != mS) {
			this.mS = mS;
			modify_mS = true;
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
	/**
	 * 关联自然幢ID
	 */
	private String gLZRZID;
	private boolean modify_gLZRZID=false;
	
	public String getGLZRZID() {
		return gLZRZID;
	}

	public void setGLZRZID(String gLZRZID) {
		if(this.gLZRZID !=gLZRZID){
			this.modify_gLZRZID=true;
			this.gLZRZID = gLZRZID;
		}
	}

	private String sEARCHSTATE;
	private boolean modify_sEARCHSTATE = false;
	
	public String getSEARCHSTATE() {
		return sEARCHSTATE;
	}

	public void setSEARCHSTATE(String sEARCHSTATE) {
		if (this.sEARCHSTATE != sEARCHSTATE) {
			this.sEARCHSTATE = sEARCHSTATE;
			modify_sEARCHSTATE = true;
		}
	}
	
	//建设用地批准证书号
	private String jSYDPZZSH;
	private boolean modify_jSYDPZZSH = false;
	
	public String getJSYDPZZSH() {
		return jSYDPZZSH;
	}

	public void setJSYDPZZSH(String jSYDPZZSH) {
		if (this.jSYDPZZSH != jSYDPZZSH) {
			this.jSYDPZZSH = jSYDPZZSH;
			modify_jSYDPZZSH = true;
		}
	}
	
	private String tXWHTYPE;
	private boolean modify_tXWHTYPE = false;
	
	public String getTXWHTYPE() {
		return tXWHTYPE;
	}

	public void setTXWHTYPE(String tXWHTYPE) {
		if(this.tXWHTYPE != tXWHTYPE){
			this.tXWHTYPE = tXWHTYPE;
			modify_tXWHTYPE = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_ySDM = false;
		modify_zDDM = false;
		modify_zDTZM = false;
		modify_zL = false;
		modify_zDMJ = false;
		modify_mJDW = false;
		modify_yT = false;
		modify_dJ = false;
		modify_jG = false;
		modify_qLLX = false;
		modify_qLXZ = false;
		modify_qLSDFS = false;
		modify_rJL = false;
		modify_jZMD = false;
		modify_jZXG = false;
		modify_zDSZD = false;
		modify_zDSZX = false;
		modify_zDSZN = false;
		modify_zDSZB = false;
		modify_nYDMJ = false;
		modify_gDMJ = false;
		modify_lDMJ = false;
		modify_cDMJ = false;
		modify_qTNYDMJ = false;
		modify_jSYDMJ = false;
		modify_wLYDMJ = false;
		modify_zDT = false;
		modify_tFH = false;
		modify_dJH = false;
		modify_dCXMID = false;
		modify_qXDM = false;
		modify_dJQDM = false;
		modify_dJZQDM = false;
		modify_qXMC = false;
		modify_dJQMC = false;
		modify_dJZQMC = false;
		modify_zH = false;
		modify_zM = false;
		modify_sYQLX = false;
		modify_tDQSLYZMCL = false;
		modify_gMJJHYFLDM = false;
		modify_yBZDDM = false;
		modify_bLC = false;
		modify_pZYT = false;
		modify_pZMJ = false;
		modify_jZZDMJ = false;
		modify_jZMJ = false;
		modify_jZDWSM = false;
		modify_jZXZXSM = false;
		modify_dCJS = false;
		modify_dCR = false;
		modify_dCRQ = false;
		modify_cLJS = false;
		modify_cLR = false;
		modify_cLRQ = false;
		modify_sHYJ = false;
		modify_sHR = false;
		modify_sHRQ = false;
		modify_zT = false;
		modify_cQZT = false;
		modify_dYZT = false;
		modify_xZZT = false;
		modify_bLZT = false;
		modify_yYZT = false;
		modify_yXBZ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_dJZT = false;
		modify_rELATIONID = false;
		modify_fTXS = false;
		modify_mS = false;
		modify_bZ = false;
		modify_gLZRZID=false;
		modify_sEARCHSTATE = false;
		modify_jSYDPZZSH = false;
		modify_tXWHTYPE = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_zDTZM)
			listStrings.add("zDTZM");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_zDMJ)
			listStrings.add("zDMJ");
		if (!modify_mJDW)
			listStrings.add("mJDW");
		if (!modify_yT)
			listStrings.add("yT");
		if (!modify_dJ)
			listStrings.add("dJ");
		if (!modify_jG)
			listStrings.add("jG");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_qLXZ)
			listStrings.add("qLXZ");
		if (!modify_qLSDFS)
			listStrings.add("qLSDFS");
		if (!modify_rJL)
			listStrings.add("rJL");
		if (!modify_jZMD)
			listStrings.add("jZMD");
		if (!modify_jZXG)
			listStrings.add("jZXG");
		if (!modify_zDSZD)
			listStrings.add("zDSZD");
		if (!modify_zDSZX)
			listStrings.add("zDSZX");
		if (!modify_zDSZN)
			listStrings.add("zDSZN");
		if (!modify_zDSZB)
			listStrings.add("zDSZB");
		if (!modify_nYDMJ)
			listStrings.add("nYDMJ");
		if (!modify_gDMJ)
			listStrings.add("gDMJ");
		if (!modify_lDMJ)
			listStrings.add("lDMJ");
		if (!modify_cDMJ)
			listStrings.add("cDMJ");
		if (!modify_qTNYDMJ)
			listStrings.add("qTNYDMJ");
		if (!modify_jSYDMJ)
			listStrings.add("jSYDMJ");
		if (!modify_wLYDMJ)
			listStrings.add("wLYDMJ");
		if (!modify_zDT)
			listStrings.add("zDT");
		if (!modify_tFH)
			listStrings.add("tFH");
		if (!modify_dJH)
			listStrings.add("dJH");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_dJQDM)
			listStrings.add("dJQDM");
		if (!modify_dJZQDM)
			listStrings.add("dJZQDM");
		if (!modify_qXMC)
			listStrings.add("qXMC");
		if (!modify_dJQMC)
			listStrings.add("dJQMC");
		if (!modify_dJZQMC)
			listStrings.add("dJZQMC");
		if (!modify_zH)
			listStrings.add("zH");
		if (!modify_zM)
			listStrings.add("zM");
		if (!modify_sYQLX)
			listStrings.add("sYQLX");
		if (!modify_tDQSLYZMCL)
			listStrings.add("tDQSLYZMCL");
		if (!modify_gMJJHYFLDM)
			listStrings.add("gMJJHYFLDM");
		if (!modify_yBZDDM)
			listStrings.add("yBZDDM");
		if (!modify_bLC)
			listStrings.add("bLC");
		if (!modify_pZYT)
			listStrings.add("pZYT");
		if (!modify_pZMJ)
			listStrings.add("pZMJ");
		if (!modify_jZZDMJ)
			listStrings.add("jZZDMJ");
		if (!modify_jZMJ)
			listStrings.add("jZMJ");
		if (!modify_jZDWSM)
			listStrings.add("jZDWSM");
		if (!modify_jZXZXSM)
			listStrings.add("jZXZXSM");
		if (!modify_dCJS)
			listStrings.add("dCJS");
		if (!modify_dCR)
			listStrings.add("dCR");
		if (!modify_dCRQ)
			listStrings.add("dCRQ");
		if (!modify_cLJS)
			listStrings.add("cLJS");
		if (!modify_cLR)
			listStrings.add("cLR");
		if (!modify_cLRQ)
			listStrings.add("cLRQ");
		if (!modify_sHYJ)
			listStrings.add("sHYJ");
		if (!modify_sHR)
			listStrings.add("sHR");
		if (!modify_sHRQ)
			listStrings.add("sHRQ");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_cQZT)
			listStrings.add("cQZT");
		if (!modify_dYZT)
			listStrings.add("dYZT");
		if (!modify_xZZT)
			listStrings.add("xZZT");
		if (!modify_bLZT)
			listStrings.add("bLZT");
		if (!modify_yYZT)
			listStrings.add("yYZT");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_dJZT)
			listStrings.add("dJZT");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_fTXS)
			listStrings.add("fTXS");
		if (!modify_mS)
			listStrings.add("mS");
		if (!modify_bZ)
			listStrings.add("bZ");
		if(!modify_gLZRZID)
			listStrings.add(gLZRZID);
		if(!modify_sEARCHSTATE)
			listStrings.add("sEARCHSTATE");
		if(!modify_jSYDPZZSH)
			listStrings.add("jSYDPZZSH");
		if(!modify_tXWHTYPE)
			listStrings.add("tXWHTYPE");
		return StringHelper.ListToStrings(listStrings);
	}

	

}
