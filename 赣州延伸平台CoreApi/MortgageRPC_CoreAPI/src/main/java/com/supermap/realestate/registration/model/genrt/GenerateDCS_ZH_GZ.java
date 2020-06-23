package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-25 
//* ----------------------------------------
//* Internal Entity bdcs_zh_gz 
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

public class GenerateDCS_ZH_GZ implements SuperModel<String> {

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

	private String zHTZM;
	private boolean modify_zHTZM = false;

	public String getZHTZM() {
		return zHTZM;
	}

	public void setZHTZM(String zHTZM) {
		if (this.zHTZM != zHTZM) {
			this.zHTZM = zHTZM;
			modify_zHTZM = true;
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

	private String xMMC;
	private boolean modify_xMMC = false;

	public String getXMMC() {
		return xMMC;
	}

	public void setXMMC(String xMMC) {
		if (this.xMMC != xMMC) {
			this.xMMC = xMMC;
			modify_xMMC = true;
		}
	}

	private String xMXX;
	private boolean modify_xMXX = false;

	public String getXMXX() {
		return xMXX;
	}

	public void setXMXX(String xMXX) {
		if (this.xMXX != xMXX) {
			this.xMXX = xMXX;
			modify_xMXX = true;
		}
	}

	private Double yHZMJ;
	private boolean modify_yHZMJ = false;

	public Double getYHZMJ() {
		return yHZMJ;
	}

	public void setYHZMJ(Double yHZMJ) {
		if (this.yHZMJ != yHZMJ) {
			this.yHZMJ = yHZMJ;
			modify_yHZMJ = true;
		}
	}

	private Double zHMJ;
	private boolean modify_zHMJ = false;

	public Double getZHMJ() {
		return zHMJ;
	}

	public void setZHMJ(Double zHMJ) {
		if (this.zHMJ != zHMJ) {
			this.zHMJ = zHMJ;
			modify_zHMJ = true;
		}
	}

	private String dB;
	private boolean modify_dB = false;

	public String getDB() {
		return dB;
	}

	public void setDB(String dB) {
		if (this.dB != dB) {
			this.dB = dB;
			modify_dB = true;
		}
	}

	private Double zHAX;
	private boolean modify_zHAX = false;

	public Double getZHAX() {
		return zHAX;
	}

	public void setZHAX(Double zHAX) {
		if (this.zHAX != zHAX) {
			this.zHAX = zHAX;
			modify_zHAX = true;
		}
	}

	private String yHLXA;
	private boolean modify_yHLXA = false;

	public String getYHLXA() {
		return yHLXA;
	}

	public void setYHLXA(String yHLXA) {
		if (this.yHLXA != yHLXA) {
			this.yHLXA = yHLXA;
			modify_yHLXA = true;
		}
	}

	private String yHLXB;
	private boolean modify_yHLXB = false;

	public String getYHLXB() {
		return yHLXB;
	}

	public void setYHLXB(String yHLXB) {
		if (this.yHLXB != yHLXB) {
			this.yHLXB = yHLXB;
			modify_yHLXB = true;
		}
	}

	private String yHWZSM;
	private boolean modify_yHWZSM = false;

	public String getYHWZSM() {
		return yHWZSM;
	}

	public void setYHWZSM(String yHWZSM) {
		if (this.yHWZSM != yHWZSM) {
			this.yHWZSM = yHWZSM;
			modify_yHWZSM = true;
		}
	}

	private String hDDM;
	private boolean modify_hDDM = false;

	public String getHDDM() {
		return hDDM;
	}

	public void setHDDM(String hDDM) {
		if (this.hDDM != hDDM) {
			this.hDDM = hDDM;
			modify_hDDM = true;
		}
	}

	private String hDMC;
	private boolean modify_hDMC = false;

	public String getHDMC() {
		return hDMC;
	}

	public void setHDMC(String hDMC) {
		if (this.hDMC != hDMC) {
			this.hDMC = hDMC;
			modify_hDMC = true;
		}
	}

	private String yDFW;
	private boolean modify_yDFW = false;

	public String getYDFW() {
		return yDFW;
	}

	public void setYDFW(String yDFW) {
		if (this.yDFW != yDFW) {
			this.yDFW = yDFW;
			modify_yDFW = true;
		}
	}

	private Double yDMJ;
	private boolean modify_yDMJ = false;

	public Double getYDMJ() {
		return yDMJ;
	}

	public void setYDMJ(Double yDMJ) {
		if (this.yDMJ != yDMJ) {
			this.yDMJ = yDMJ;
			modify_yDMJ = true;
		}
	}

	private String hDWZ;
	private boolean modify_hDWZ = false;

	public String getHDWZ() {
		return hDWZ;
	}

	public void setHDWZ(String hDWZ) {
		if (this.hDWZ != hDWZ) {
			this.hDWZ = hDWZ;
			modify_hDWZ = true;
		}
	}

	private String hDYT;
	private boolean modify_hDYT = false;

	public String getHDYT() {
		return hDYT;
	}

	public void setHDYT(String hDYT) {
		if (this.hDYT != hDYT) {
			this.hDYT = hDYT;
			modify_hDYT = true;
		}
	}

	private Double sYQMJ;
	private boolean modify_sYQMJ = false;

	public Double getSYQMJ() {
		return sYQMJ;
	}

	public void setSYQMJ(Double sYQMJ) {
		if (this.sYQMJ != sYQMJ) {
			this.sYQMJ = sYQMJ;
			modify_sYQMJ = true;
		}
	}

	private Double sYJZE;
	private boolean modify_sYJZE = false;

	public Double getSYJZE() {
		return sYJZE;
	}

	public void setSYJZE(Double sYJZE) {
		if (this.sYJZE != sYJZE) {
			this.sYJZE = sYJZE;
			modify_sYJZE = true;
		}
	}

	private String sYJBZYJ;
	private boolean modify_sYJBZYJ = false;

	public String getSYJBZYJ() {
		return sYJBZYJ;
	}

	public void setSYJBZYJ(String sYJBZYJ) {
		if (this.sYJBZYJ != sYJBZYJ) {
			this.sYJBZYJ = sYJBZYJ;
			modify_sYJBZYJ = true;
		}
	}

	private String sYJJNQK;
	private boolean modify_sYJJNQK = false;

	public String getSYJJNQK() {
		return sYJJNQK;
	}

	public void setSYJJNQK(String sYJJNQK) {
		if (this.sYJJNQK != sYJJNQK) {
			this.sYJJNQK = sYJJNQK;
			modify_sYJJNQK = true;
		}
	}

	private String zHT;
	private boolean modify_zHT = false;

	public String getZHT() {
		return zHT;
	}

	public void setZHT(String zHT) {
		if (this.zHT != zHT) {
			this.zHT = zHT;
			modify_zHT = true;
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

	private String hCJS;
	private boolean modify_hCJS = false;

	public String getHCJS() {
		return hCJS;
	}

	public void setHCJS(String hCJS) {
		if (this.hCJS != hCJS) {
			this.hCJS = hCJS;
			modify_hCJS = true;
		}
	}

	private String hCR;
	private boolean modify_hCR = false;

	public String getHCR() {
		return hCR;
	}

	public void setHCR(String hCR) {
		if (this.hCR != hCR) {
			this.hCR = hCR;
			modify_hCR = true;
		}
	}

	private Date hCRQ;
	private boolean modify_hCRQ = false;

	public Date getHCRQ() {
		return hCRQ;
	}

	public void setHCRQ(Date hCRQ) {
		if (this.hCRQ != hCRQ) {
			this.hCRQ = hCRQ;
			modify_hCRQ = true;
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
	
	private String sYRQZD;
	private boolean modify_sYRQZD = false;

	public String getSYRQZD() {
		return sYRQZD;
	}

	public void setSYRQZD(String sYRQZD) {
		if (this.sYRQZD != sYRQZD) {
			this.sYRQZD = sYRQZD;
			modify_sYRQZD = true;
		}
	}
	
	private String sYRQZX;
	private boolean modify_sYRQZX = false;

	public String getSYRQZX() {
		return sYRQZX;
	}

	public void setSYRQZX(String sYRQZX) {
		if (this.sYRQZX != sYRQZX) {
			this.sYRQZX = sYRQZX;
			modify_sYRQZX = true;
		}
	}
	
	private String sYRQZN;
	private boolean modify_sYRQZN = false;

	public String getSYRQZN() {
		return sYRQZN;
	}

	public void setSYRQZN(String sYRQZN) {
		if (this.sYRQZN != sYRQZN) {
			this.sYRQZN = sYRQZN;
			modify_sYRQZN = true;
		}
	}
	
	private String sYRQZB;
	private boolean modify_sYRQZB = false;

	public String getSYRQZB() {
		return sYRQZB;
	}

	public void setSYRQZB(String sYRQZB) {
		if (this.sYRQZB != sYRQZB) {
			this.sYRQZB = sYRQZB;
			modify_sYRQZB = true;
		}
	}
	
	private String qDFS;
	private boolean modify_qDFS = false;

	public String getQDFS() {
		return qDFS;
	}

	public void setQDFS(String qDFS) {
		if (this.qDFS != qDFS) {
			this.qDFS = qDFS;
			modify_qDFS = true;
		}
	}
	
	private String gZWLX;
	private boolean modify_gZWLX = false;

	public String getGZWLX() {
		return gZWLX;
	}

	public void setGZWLX(String gZWLX) {
		if (this.gZWLX != gZWLX) {
			this.gZWLX = gZWLX;
			modify_gZWLX = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_rELATIONID = false;
		modify_id = false;
		modify_bDCDYH = false;
		modify_ySDM = false;
		modify_xMBH = false;
		modify_zHDM = false;
		modify_zHTZM = false;
		modify_qXDM = false;
		modify_zL = false;
		modify_xMMC = false;
		modify_xMXX = false;
		modify_yHZMJ = false;
		modify_zHMJ = false;
		modify_dB = false;
		modify_zHAX = false;
		modify_yHLXA = false;
		modify_yHLXB = false;
		modify_yHWZSM = false;
		modify_hDDM = false;
		modify_hDMC = false;
		modify_yDFW = false;
		modify_yDMJ = false;
		modify_hDWZ = false;
		modify_hDYT = false;
		modify_sYQMJ = false;
		modify_sYJZE = false;
		modify_sYJBZYJ = false;
		modify_sYJJNQK = false;
		modify_zHT = false;
		modify_zT = false;
		modify_yXBZ = false;
		modify_dCXMID = false;
		modify_hCJS = false;
		modify_hCR = false;
		modify_hCRQ = false;
		modify_cLJS = false;
		modify_cLR = false;
		modify_cLRQ = false;
		modify_sHYJ = false;
		modify_sHR = false;
		modify_sHRQ = false;
		modify_bZ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_dJZT = false;
		modify_sYRQZD = false;
		modify_sYRQZX = false;
		modify_sYRQZN = false;
		modify_sYRQZB = false;
		modify_qDFS = false;
		modify_gZWLX = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_zHDM)
			listStrings.add("zHDM");
		if (!modify_zHTZM)
			listStrings.add("zHTZM");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_xMXX)
			listStrings.add("xMXX");
		if (!modify_yHZMJ)
			listStrings.add("yHZMJ");
		if (!modify_zHMJ)
			listStrings.add("zHMJ");
		if (!modify_dB)
			listStrings.add("dB");
		if (!modify_zHAX)
			listStrings.add("zHAX");
		if (!modify_yHLXA)
			listStrings.add("yHLXA");
		if (!modify_yHLXB)
			listStrings.add("yHLXB");
		if (!modify_yHWZSM)
			listStrings.add("yHWZSM");
		if (!modify_hDDM)
			listStrings.add("hDDM");
		if (!modify_hDMC)
			listStrings.add("hDMC");
		if (!modify_yDFW)
			listStrings.add("yDFW");
		if (!modify_yDMJ)
			listStrings.add("yDMJ");
		if (!modify_hDWZ)
			listStrings.add("hDWZ");
		if (!modify_hDYT)
			listStrings.add("hDYT");
		if (!modify_sYQMJ)
			listStrings.add("sYQMJ");
		if (!modify_sYJZE)
			listStrings.add("sYJZE");
		if (!modify_sYJBZYJ)
			listStrings.add("sYJBZYJ");
		if (!modify_sYJJNQK)
			listStrings.add("sYJJNQK");
		if (!modify_zHT)
			listStrings.add("zHT");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_hCJS)
			listStrings.add("hCJS");
		if (!modify_hCR)
			listStrings.add("hCR");
		if (!modify_hCRQ)
			listStrings.add("hCRQ");
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
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_dJZT)
			listStrings.add("dJZT");
		if (!modify_sYRQZD)
			listStrings.add("sYRQZD");
		if (!modify_sYRQZX)
			listStrings.add("sYRQZX");
		if (!modify_sYRQZN)
			listStrings.add("sYRQZN");
		if (!modify_sYRQZB)
			listStrings.add("sYRQZB");
		if (!modify_qDFS)
			listStrings.add("qDFS");
		if (!modify_gZWLX)
			listStrings.add("gZWLX");

		return StringHelper.ListToStrings(listStrings);
	}
}
