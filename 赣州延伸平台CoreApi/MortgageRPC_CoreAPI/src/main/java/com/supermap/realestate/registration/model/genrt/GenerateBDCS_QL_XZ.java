package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-25 
//* ----------------------------------------
//* Internal Entity bdcs_ql_xz 
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

public class GenerateBDCS_QL_XZ implements SuperModel<String> {

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

	private String dJDYID;
	private boolean modify_dJDYID = false;

	public String getDJDYID() {
		return dJDYID;
	}

	public void setDJDYID(String dJDYID) {
		if (this.dJDYID != dJDYID) {
			this.dJDYID = dJDYID;
			modify_dJDYID = true;
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

	private String fSQLID;
	private boolean modify_fSQLID = false;

	public String getFSQLID() {
		return fSQLID;
	}

	public void setFSQLID(String fSQLID) {
		if (this.fSQLID != fSQLID) {
			this.fSQLID = fSQLID;
			modify_fSQLID = true;
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

	private String dJLX;
	private boolean modify_dJLX = false;

	public String getDJLX() {
		return dJLX;
	}

	public void setDJLX(String dJLX) {
		if (this.dJLX != dJLX) {
			this.dJLX = dJLX;
			modify_dJLX = true;
		}
	}

	private String dJYY;
	private boolean modify_dJYY = false;

	public String getDJYY() {
		return dJYY;
	}

	public void setDJYY(String dJYY) {
		if (this.dJYY != dJYY) {
			this.dJYY = dJYY;
			modify_dJYY = true;
		}
	}

	private Date qLQSSJ;
	private boolean modify_qLQSSJ = false;

	public Date getQLQSSJ() {
		return qLQSSJ;
	}

	public void setQLQSSJ(Date qLQSSJ) {
		if (this.qLQSSJ != qLQSSJ) {
			this.qLQSSJ = qLQSSJ;
			modify_qLQSSJ = true;
		}
	}

	private Date qLJSSJ;
	private boolean modify_qLJSSJ = false;

	public Date getQLJSSJ() {
		return qLJSSJ;
	}

	public void setQLJSSJ(Date qLJSSJ) {
		if (this.qLJSSJ != qLJSSJ) {
			this.qLJSSJ = qLJSSJ;
			modify_qLJSSJ = true;
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

	private String dJJG;
	private boolean modify_dJJG = false;

	public String getDJJG() {
		return dJJG;
	}

	public void setDJJG(String dJJG) {
		if (this.dJJG != dJJG) {
			this.dJJG = dJJG;
			modify_dJJG = true;
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

	private String fJ;
	private boolean modify_fJ = false;

	public String getFJ() {
		return fJ;
	}

	public void setFJ(String fJ) {
		if (this.fJ != fJ) {
			this.fJ = fJ;
			modify_fJ = true;
		}
	}

	private Integer qSZT;
	private boolean modify_qSZT = false;

	public Integer getQSZT() {
		return qSZT;
	}

	public void setQSZT(Integer qSZT) {
		if (this.qSZT != qSZT) {
			this.qSZT = qSZT;
			modify_qSZT = true;
		}
	}

	private String qLLXMC;
	private boolean modify_qLLXMC = false;

	public String getQLLXMC() {
		return qLLXMC;
	}

	public void setQLLXMC(String qLLXMC) {
		if (this.qLLXMC != qLLXMC) {
			this.qLLXMC = qLLXMC;
			modify_qLLXMC = true;
		}
	}

	private Byte[] zSEWM;
	private boolean modify_zSEWM = false;

	public Byte[] getZSEWM() {
		return zSEWM;
	}

	public void setZSEWM(Byte[] zSEWM) {
		if (this.zSEWM != zSEWM) {
			this.zSEWM = zSEWM;
			modify_zSEWM = true;
		}
	}

	private String zSBH;
	private boolean modify_zSBH = false;

	public String getZSBH() {
		return zSBH;
	}

	public void setZSBH(String zSBH) {
		if (this.zSBH != zSBH) {
			this.zSBH = zSBH;
			modify_zSBH = true;
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

	private String cZFS;
	private boolean modify_cZFS = false;

	public String getCZFS() {
		return cZFS;
	}

	public void setCZFS(String cZFS) {
		if (this.cZFS != cZFS) {
			this.cZFS = cZFS;
			modify_cZFS = true;
		}
	}

	private String zSBS;
	private boolean modify_zSBS = false;

	public String getZSBS() {
		return zSBS;
	}

	public void setZSBS(String zSBS) {
		if (this.zSBS != zSBS) {
			this.zSBS = zSBS;
			modify_zSBS = true;
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

	private Double qDJG;
	private boolean modify_qDJG = false;

	public Double getQDJG() {
		return qDJG;
	}

	public void setQDJG(Double qDJG) {
		if (this.qDJG != qDJG) {
			this.qDJG = qDJG;
			modify_qDJG = true;
		}
	}

	private String lYQLID;
	private boolean modify_lYQLID = false;

	public String getLYQLID() {
		return lYQLID;
	}

	public void setLYQLID(String lYQLID) {
		if (this.lYQLID != lYQLID) {
			this.lYQLID = lYQLID;
			modify_lYQLID = true;
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

	private String tDZH;
	private boolean modify_tDZH = false;

	public String getTDZH() {
		return tDZH;
	}

	public void setTDZH(String tDZH) {
		if (this.tDZH != tDZH) {
			this.tDZH = tDZH;
			modify_tDZH = true;
		}
	}

	private String tDZHXH;
	private boolean modify_tDZHXH = false;

	public String getTDZHXH() {
		return tDZHXH;
	}

	public void setTDZHXH(String tDZHXH) {
		if (this.tDZHXH != tDZHXH) {
			this.tDZHXH = tDZHXH;
			modify_tDZHXH = true;
		}
	}

	private String tDSHYQR;
	private boolean modify_tDSHYQR = false;

	public String getTDSHYQR() {
		return tDSHYQR;
	}

	public void setTDSHYQR(String tDSHYQR) {
		if (this.tDSHYQR != tDSHYQR) {
			this.tDSHYQR = tDSHYQR;
			modify_tDSHYQR = true;
		}
	}

	private String pACTNO;
	private boolean modify_pACTNO = false;

	public String getPACTNO() {
		return pACTNO;
	}

	public void setPACTNO(String pACTNO) {
		if (this.pACTNO != pACTNO) {
			this.pACTNO = pACTNO;
			modify_pACTNO = true;
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

	private String cASENUM;
	private boolean modify_cASENUM = false;

	public String getCASENUM() {
		return cASENUM;
	}

	public void setCASENUM(String cASENUM) {
		if (this.cASENUM != cASENUM) {
			this.cASENUM = cASENUM;
			modify_cASENUM = true;
		}
	}

	private String iSCANCEL;
	private boolean modify_iSCANCEL = false;

	public String getISCANCEL() {
		return iSCANCEL;
	}

	public void setISCANCEL(String iSCANCEL) {
		if (this.iSCANCEL != iSCANCEL) {
			this.iSCANCEL = iSCANCEL;
			modify_iSCANCEL = true;
		}
	}

	private String iSPARTIAL;
	private boolean modify_iSPARTIAL = false;

	public String getISPARTIAL() {
		return iSPARTIAL;
	}

	public void setISPARTIAL(String iSPARTIAL) {
		if (this.iSPARTIAL != iSPARTIAL) {
			this.iSPARTIAL = iSPARTIAL;
			modify_iSPARTIAL = true;
		}
	}

	private String aRCHIVES_CLASSNO;
	private boolean modify_aRCHIVES_CLASSNO = false;

	public String getARCHIVES_CLASSNO() {
		return aRCHIVES_CLASSNO;
	}

	public void setARCHIVES_CLASSNO(String aRCHIVES_CLASSNO) {
		if (this.aRCHIVES_CLASSNO != aRCHIVES_CLASSNO) {
			this.aRCHIVES_CLASSNO = aRCHIVES_CLASSNO;
			modify_aRCHIVES_CLASSNO = true;
		}
	}

	private String aRCHIVES_BOOKNO;
	private boolean modify_aRCHIVES_BOOKNO = false;

	public String getARCHIVES_BOOKNO() {
		return aRCHIVES_BOOKNO;
	}

	public void setARCHIVES_BOOKNO(String aRCHIVES_BOOKNO) {
		if (this.aRCHIVES_BOOKNO != aRCHIVES_BOOKNO) {
			this.aRCHIVES_BOOKNO = aRCHIVES_BOOKNO;
			modify_aRCHIVES_BOOKNO = true;
		}
	}

	private String bDCQZHXH;
	private boolean modify_bDCQZHXH = false;

	public String getBDCQZHXH() {
		return bDCQZHXH;
	}

	public void setBDCQZHXH(String bDCQZHXH) {
		if (this.bDCQZHXH != bDCQZHXH) {
			this.bDCQZHXH = bDCQZHXH;
			modify_bDCQZHXH = true;
		}
	}

	private String hTH;
	private boolean modify_hTH = false;
	
	public String getHTH() {
		return hTH;
	}

	public void setHTH(String hTH) {
		if (this.hTH != hTH) {
			this.hTH = hTH;
			modify_hTH = true;
		}	
	}

	private String mSR;
	private boolean modify_mSR = false;
	
	public String getMSR() {
		return mSR;
	}

	public void setMSR(String mSR) {
		if (this.mSR != mSR) {
			this.mSR = mSR;
			modify_mSR = true;
		}	
	}
	
	private String mAINQLID;
	private boolean modify_mAINQLID = false;
	
	public String getMAINQLID() {
		return mAINQLID;
	}

	public void setMAINQLID(String mAINQLID) {
		if (this.mAINQLID != mAINQLID) {
			this.mAINQLID = mAINQLID;
			modify_mAINQLID = true;
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
	
	private String qS;
	private boolean modify_qS = false;
	
	public String getQS() {
		return qS;
	}

	public void setQS(String qS) {
		if (this.qS != qS) {
			this.qS = qS;
			modify_qS = true;
		}
	}

	private String tDZSRelationid;
	private boolean modify_tDZSRelationid = false;
	
	public String getTDZSRELATIONID() {
		return tDZSRelationid;
	}

	public void setTDZSRELATIONID(String tDZSRelationid) {
		if (this.tDZSRelationid != tDZSRelationid) {
			this.tDZSRelationid = tDZSRelationid;
			modify_tDZSRelationid = true;
		}
	}
	
	private String tDCasenum;
	private boolean modify_tDCasenum = false;
	
	public String getTDCASENUM() {
		return tDCasenum;
	}

	public void setTDCASENUM(String tDCasenum) {
		if (this.tDCasenum != tDCasenum) {
			this.tDCasenum = tDCasenum;
			modify_tDCasenum = true;
		}
	}
	private Integer gROUPID = 1;
	private boolean modify_gROUPID = false;

	public Integer getGROUPID() {
		return gROUPID;
	}

	public void setGROUPID(Integer gROUPID) {
		if (this.gROUPID != gROUPID&&!org.springframework.util.StringUtils.isEmpty(gROUPID)) {
			this.gROUPID = gROUPID;
			modify_gROUPID = true;
		}
	}
	private String gYDBDCDYID;
	private boolean modify_gYDBDCDYID = false;
	
	public String getGYDBDCDYID() {
		return gYDBDCDYID;
	}

	public void setGYDBDCDYID(String gYDBDCDYID) {
		if (this.gYDBDCDYID != gYDBDCDYID) {
			this.gYDBDCDYID = gYDBDCDYID;
			modify_gYDBDCDYID = true;
		}
	}
	
	private String gYDBDCDYLX;
	private boolean modify_gYDBDCDYLX = false;
	
	public String getGYDBDCDYLX() {
		return gYDBDCDYLX;
	}

	public void setGYDBDCDYLX(String gYDBDCDYLX) {
		if (this.gYDBDCDYLX != gYDBDCDYLX) {
			this.gYDBDCDYLX = gYDBDCDYLX;
			modify_gYDBDCDYLX = true;
		}
	}
	
	private String gYRQK;
	private boolean modify_gYRQK = false;
	
	public String getGYRQK() {
		return gYRQK;
	}

	public void setGYRQK(String gYRQK) {
		if (this.gYRQK != gYRQK) {
			this.gYRQK = gYRQK;
			modify_gYRQK = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_gYDBDCDYID=false;
		modify_gYDBDCDYLX=false;
		modify_gROUPID=false;
		modify_id = false;
		modify_dJDYID = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_fSQLID = false;
		modify_yWH = false;
		modify_qLLX = false;
		modify_dJLX = false;
		modify_dJYY = false;
		modify_qLQSSJ = false;
		modify_qLJSSJ = false;
		modify_bDCQZH = false;
		modify_qXDM = false;
		modify_dJJG = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_fJ = false;
		modify_qSZT = false;
		modify_qLLXMC = false;
		modify_zSEWM = false;
		modify_zSBH = false;
		modify_zT = false;
		modify_cZFS = false;
		modify_zSBS = false;
		modify_yXBZ = false;
		modify_dCXMID = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_qDJG = false;
		modify_lYQLID = false;
		modify_dJZT = false;
		modify_tDZH = false;
		modify_tDZHXH = false;
		modify_tDSHYQR = false;
		modify_pACTNO = false;
		modify_bZ = false;
		modify_cASENUM = false;
		modify_iSCANCEL = false;
		modify_bDCQZHXH = false;
		modify_iSPARTIAL = false;
		modify_aRCHIVES_CLASSNO = false;
		modify_aRCHIVES_BOOKNO = false;
		modify_hTH = false;
		modify_mSR = false;
		modify_mAINQLID = false;
		modify_qS = false;
		modify_bDCDYID = false;
		modify_gYRQK = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_gYDBDCDYID)
			listStrings.add("gYDBDCDYID");
		if (!modify_gYDBDCDYLX)
			listStrings.add("gYDBDCDYLX");
		if (!modify_gROUPID)
			listStrings.add("gROUPID");
		if (!modify_id)
			listStrings.add("id");
		if (!modify_dJDYID)
			listStrings.add("dJDYID");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_fSQLID)
			listStrings.add("fSQLID");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_dJYY)
			listStrings.add("dJYY");
		if (!modify_qLQSSJ)
			listStrings.add("qLQSSJ");
		if (!modify_qLJSSJ)
			listStrings.add("qLJSSJ");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_dJJG)
			listStrings.add("dJJG");
		if (!modify_dBR)
			listStrings.add("dBR");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if (!modify_fJ)
			listStrings.add("fJ");
		if (!modify_qSZT)
			listStrings.add("qSZT");
		if (!modify_qLLXMC)
			listStrings.add("qLLXMC");
		if (!modify_zSEWM)
			listStrings.add("zSEWM");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_cZFS)
			listStrings.add("cZFS");
		if (!modify_zSBS)
			listStrings.add("zSBS");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_qDJG)
			listStrings.add("qDJG");
		if (!modify_lYQLID)
			listStrings.add("lYQLID");
		if (!modify_dJZT)
			listStrings.add("dJZT");
		if (!modify_tDZH)
			listStrings.add("tDZH");
		if (!modify_tDZHXH)
			listStrings.add("tDZHXH");
		if (!modify_tDSHYQR)
			listStrings.add("tDSHYQR");
		if (!modify_pACTNO)
			listStrings.add("pACTNO");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_cASENUM)
			listStrings.add("cASENUM");
		if (!modify_iSCANCEL)
			listStrings.add("iSCANCEL");
		if (!modify_bDCQZHXH)
			listStrings.add("bDCQZHXH");
		if (!modify_iSPARTIAL) {
			listStrings.add("iSPARTIAL");
		}
		if (!modify_aRCHIVES_CLASSNO) {
			listStrings.add("aRCHIVES_CLASSNO");
		}
		if (!modify_aRCHIVES_BOOKNO) {
			listStrings.add("aRCHIVES_BOOKNO");
		}
		if (!modify_hTH) {
			listStrings.add("hTH");
		}
		if (!modify_mSR) {
			listStrings.add("mSR");
		}
		if (!modify_mAINQLID) {
			listStrings.add("modify_mAINQLID");
		}
		if(!modify_qS)
			listStrings.add("qS");
		if(!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if(!modify_gYRQK)
			listStrings.add("gYRQK");
		return StringHelper.ListToStrings(listStrings);
	}

}
