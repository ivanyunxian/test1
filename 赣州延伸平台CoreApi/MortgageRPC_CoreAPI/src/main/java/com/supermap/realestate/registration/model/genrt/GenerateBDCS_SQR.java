package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/7/30 
//* ----------------------------------------
//* Internal Entity bdcs_sqr 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_SQR implements SuperModel<String> {

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

	private String sQRXM;
	private boolean modify_sQRXM = false;

	public String getSQRXM() {
		return sQRXM;
	}

	public void setSQRXM(String sQRXM) {
		if (this.sQRXM != sQRXM) {
			this.sQRXM = sQRXM;
			modify_sQRXM = true;
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

	private String zJLX;
	private boolean modify_zJLX = false;

	public String getZJLX() {
		return zJLX;
	}

	public void setZJLX(String zJLX) {
		if (this.zJLX != zJLX) {
			this.zJLX = zJLX;
			modify_zJLX = true;
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

	private String gJDQ;
	private boolean modify_gJDQ = false;

	public String getGJDQ() {
		return gJDQ;
	}

	public void setGJDQ(String gJDQ) {
		if (this.gJDQ != gJDQ) {
			this.gJDQ = gJDQ;
			modify_gJDQ = true;
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

	private String lXDH;
	private boolean modify_lXDH = false;

	public String getLXDH() {
		return lXDH;
	}

	public void setLXDH(String lXDH) {
		if (this.lXDH != lXDH) {
			this.lXDH = lXDH;
			modify_lXDH = true;
		}
	}

	private String tXDZ;
	private boolean modify_tXDZ = false;

	public String getTXDZ() {
		return tXDZ;
	}

	public void setTXDZ(String tXDZ) {
		if (this.tXDZ != tXDZ) {
			this.tXDZ = tXDZ;
			modify_tXDZ = true;
		}
	}

	private String yZBM;
	private boolean modify_yZBM = false;

	public String getYZBM() {
		return yZBM;
	}

	public void setYZBM(String yZBM) {
		if (this.yZBM != yZBM) {
			this.yZBM = yZBM;
			modify_yZBM = true;
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

	private String sQRLX;
	private boolean modify_sQRLX = false;

	public String getSQRLX() {
		return sQRLX;
	}

	public void setSQRLX(String sQRLX) {
		if (this.sQRLX != sQRLX) {
			this.sQRLX = sQRLX;
			modify_sQRLX = true;
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

	private String sXH;
	private boolean modify_sXH = false;

	public String getSXH() {
		return sXH;
	}

	public void setSXH(String sXH) {
		if (this.sXH != sXH) {
			this.sXH = sXH;
			modify_sXH = true;
		}
	}

	private String qLMJ;
	private boolean modify_qLMJ = false;

	public String getQLMJ() {
		return qLMJ;
	}

	public void setQLMJ(String qLMJ) {
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

	private String sQRLB;
	private boolean modify_sQRLB = false;

	public String getSQRLB() {
		return sQRLB;
	}

	public void setSQRLB(String sQRLB) {
		if (this.sQRLB != sQRLB) {
			this.sQRLB = sQRLB;
			modify_sQRLB = true;
		}
	}

	private String sQRLBMC;
	private boolean modify_sQRLBMC = false;

	public String getSQRLBMC() {
		return sQRLBMC;
	}

	public void setSQRLBMC(String sQRLBMC) {
		if (this.sQRLBMC != sQRLBMC) {
			this.sQRLBMC = sQRLBMC;
			modify_sQRLBMC = true;
		}
	}

	private String gLQLID;
	private boolean modify_gLQLID = false;

	public String getGLQLID() {
		return gLQLID;
	}

	public void setGLQLID(String gLQLID) {
		if (this.gLQLID != gLQLID) {
			this.gLQLID = gLQLID;
			modify_gLQLID = true;
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
	
	private String zWR;
	private boolean modify_zWR = false;
	
	public String getZWR() {
		return zWR;
	}

	public void setZWR(String zWR) {
		if (this.zWR != zWR) {
			this.zWR = zWR;
			modify_zWR = true;
		}
	}
	
	/**
	 * 身份证头像编码
	 */

	private String picturecode;

	@Transient
	public String getPICTURECODE() {
		return picturecode;
	}

	public void setPICTURECODE(String code) {
		this.picturecode = code;
	}
	
	/**
	 * 身份证头像编码
	 */

	private String picturecode_dlr;

	@Transient
	public String getPICTURECODE_DLR() {
		return picturecode_dlr;
	}

	public void setPICTURECODE_DLR(String code) {
		this.picturecode_dlr = code;
	}
	
	private String nATION;
	private boolean modify_nATION = false;
	
	public String getNATION() {
		return nATION;
	}

	public void setNATION(String nATION) {
		if (this.nATION != nATION) {
			this.nATION = nATION;
			modify_nATION = true;
		}
	}
	
	private String dLRNATION;
	private boolean modify_dLRNATION = false;
	
	public String getDLRNATION() {
		return dLRNATION;
	}

	public void setDLRNATION(String dLRNATION) {
		if (this.dLRNATION != dLRNATION) {
			this.dLRNATION = dLRNATION;
			modify_dLRNATION = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_sQRXM = false;
		modify_xB = false;
		modify_zJLX = false;
		modify_zJH = false;
		modify_fZJG = false;
		modify_gJDQ = false;
		modify_hJSZSS = false;
		modify_gZDW = false;
		modify_sSHY = false;
		modify_lXDH = false;
		modify_tXDZ = false;
		modify_yZBM = false;
		modify_dZYJ = false;
		modify_fDDBR = false;
		modify_fDDBRZJLX = false;
		modify_fDDBRDH = false;
		modify_sQRLX = false;
		modify_dLRXM = false;
		modify_dLJGMC = false;
		modify_dLRLXDH = false;
		modify_dLRZJLX = false;
		modify_dLRZJHM = false;
		modify_yXBZ = false;
		modify_sXH = false;
		modify_qLMJ = false;
		modify_qLBL = false;
		modify_gYFS = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_sQRLB = false;
		modify_sQRLBMC = false;
		modify_gLQLID = false;
		modify_iSCZR=false;
		modify_fDDBRZJHM=false;
		modify_zWR=false;
		modify_nATION = false;
		modify_dLRNATION = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_sQRXM)
			listStrings.add("sQRXM");
		if (!modify_xB)
			listStrings.add("xB");
		if (!modify_zJLX)
			listStrings.add("zJLX");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_fZJG)
			listStrings.add("fZJG");
		if (!modify_gJDQ)
			listStrings.add("gJDQ");
		if (!modify_hJSZSS)
			listStrings.add("hJSZSS");
		if (!modify_gZDW)
			listStrings.add("gZDW");
		if (!modify_sSHY)
			listStrings.add("sSHY");
		if (!modify_lXDH)
			listStrings.add("lXDH");
		if (!modify_tXDZ)
			listStrings.add("tXDZ");
		if (!modify_yZBM)
			listStrings.add("yZBM");
		if (!modify_dZYJ)
			listStrings.add("dZYJ");
		if (!modify_fDDBR)
			listStrings.add("fDDBR");
		if (!modify_fDDBRZJLX)
			listStrings.add("fDDBRZJLX");
		if (!modify_fDDBRDH)
			listStrings.add("fDDBRDH");
		if (!modify_sQRLX)
			listStrings.add("sQRLX");
		if (!modify_dLRXM)
			listStrings.add("dLRXM");
		if (!modify_dLJGMC)
			listStrings.add("dLJGMC");
		if (!modify_dLRLXDH)
			listStrings.add("dLRLXDH");
		if (!modify_dLRZJLX)
			listStrings.add("dLRZJLX");
		if (!modify_dLRZJHM)
			listStrings.add("dLRZJHM");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_qLMJ)
			listStrings.add("qLMJ");
		if (!modify_qLBL)
			listStrings.add("qLBL");
		if (!modify_gYFS)
			listStrings.add("gYFS");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_sQRLB)
			listStrings.add("sQRLB");
		if (!modify_sQRLBMC)
			listStrings.add("sQRLBMC");
		if (!modify_gLQLID)
			listStrings.add("gLQLID");
		if(!modify_iSCZR){
			listStrings.add("modify_iSCZR");
		}
		if(!modify_fDDBRZJHM){
			listStrings.add("modify_fDDBRZJHM");
		}
		if(!modify_zWR){
			listStrings.add("modify_zWR");
		}
		if (!modify_nATION)
			listStrings.add("nATION");
		if (!modify_dLRNATION)
			listStrings.add("dLRNATION");
		return StringHelper.ListToStrings(listStrings);
	}
}
