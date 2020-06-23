package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-24 
//* ----------------------------------------
//* Internal Entity bdcs_fsql_gz 
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

public class GenerateBDCS_FSQL_GZ implements SuperModel<String> {

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

	private String hYMJDW;
	private boolean modify_hYMJDW = false;

	public String getHYMJDW() {
		return hYMJDW;
	}

	public void setHYMJDW(String hYMJDW) {
		if (this.hYMJDW != hYMJDW) {
			this.hYMJDW = hYMJDW;
			modify_hYMJDW = true;
		}
	}

	private Double hYMJ;
	private boolean modify_hYMJ = false;

	public Double getHYMJ() {
		return hYMJ;
	}

	public void setHYMJ(Double hYMJ) {
		if (this.hYMJ != hYMJ) {
			this.hYMJ = hYMJ;
			modify_hYMJ = true;
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

	private String fDZL;
	private boolean modify_fDZL = false;

	public String getFDZL() {
		return fDZL;
	}

	public void setFDZL(String fDZL) {
		if (this.fDZL != fDZL) {
			this.fDZL = fDZL;
			modify_fDZL = true;
		}
	}

	private String tDSYQR;
	private boolean modify_tDSYQR = false;

	public String getTDSYQR() {
		return tDSYQR;
	}

	public void setTDSYQR(String tDSYQR) {
		if (this.tDSYQR != tDSYQR) {
			this.tDSYQR = tDSYQR;
			modify_tDSYQR = true;
		}
	}

	private Double dYTDMJ;
	private boolean modify_dYTDMJ = false;

	public Double getDYTDMJ() {
		return dYTDMJ;
	}

	public void setDYTDMJ(Double dYTDMJ) {
		if (this.dYTDMJ != dYTDMJ) {
			this.dYTDMJ = dYTDMJ;
			modify_dYTDMJ = true;
		}
	}

	private Double fTTDMJ;
	private boolean modify_fTTDMJ = false;

	public Double getFTTDMJ() {
		return fTTDMJ;
	}

	public void setFTTDMJ(Double fTTDMJ) {
		if (this.fTTDMJ != fTTDMJ) {
			this.fTTDMJ = fTTDMJ;
			modify_fTTDMJ = true;
		}
	}

	private Double fDCJYJG;
	private boolean modify_fDCJYJG = false;

	public Double getFDCJYJG() {
		return fDCJYJG;
	}

	public void setFDCJYJG(Double fDCJYJG) {
		if (this.fDCJYJG != fDCJYJG) {
			this.fDCJYJG = fDCJYJG;
			modify_fDCJYJG = true;
		}
	}

	private Date jGSJ;
	private boolean modify_jGSJ = false;

	public Date getJGSJ() {
		return jGSJ;
	}

	public void setJGSJ(Date jGSJ) {
		if (this.jGSJ != jGSJ) {
			this.jGSJ = jGSJ;
			modify_jGSJ = true;
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

	private Integer zCS;
	private boolean modify_zCS = false;

	public Integer getZCS() {
		return zCS;
	}

	public void setZCS(Integer zCS) {
		if (this.zCS != zCS) {
			this.zCS = zCS;
			modify_zCS = true;
		}
	}

	private String gHYT;
	private boolean modify_gHYT = false;

	public String getGHYT() {
		return gHYT;
	}

	public void setGHYT(String gHYT) {
		if (this.gHYT != gHYT) {
			this.gHYT = gHYT;
			modify_gHYT = true;
		}
	}

	private String fWJG;
	private boolean modify_fWJG = false;

	public String getFWJG() {
		return fWJG;
	}

	public void setFWJG(String fWJG) {
		if (this.fWJG != fWJG) {
			this.fWJG = fWJG;
			modify_fWJG = true;
		}
	}

	private String fWXZ;
	private boolean modify_fWXZ = false;

	public String getFWXZ() {
		return fWXZ;
	}

	public void setFWXZ(String fWXZ) {
		if (this.fWXZ != fWXZ) {
			this.fWXZ = fWXZ;
			modify_fWXZ = true;
		}
	}

	private Integer sZC;
	private boolean modify_sZC = false;

	public Integer getSZC() {
		return sZC;
	}

	public void setSZC(Integer sZC) {
		if (this.sZC != sZC) {
			this.sZC = sZC;
			modify_sZC = true;
		}
	}

	private Double zYJZMJ;
	private boolean modify_zYJZMJ = false;

	public Double getZYJZMJ() {
		return zYJZMJ;
	}

	public void setZYJZMJ(Double zYJZMJ) {
		if (this.zYJZMJ != zYJZMJ) {
			this.zYJZMJ = zYJZMJ;
			modify_zYJZMJ = true;
		}
	}

	private Double fTJZMJ;
	private boolean modify_fTJZMJ = false;

	public Double getFTJZMJ() {
		return fTJZMJ;
	}

	public void setFTJZMJ(Double fTJZMJ) {
		if (this.fTJZMJ != fTJZMJ) {
			this.fTJZMJ = fTJZMJ;
			modify_fTJZMJ = true;
		}
	}

	private Double lMMJ;
	private boolean modify_lMMJ = false;

	public Double getLMMJ() {
		return lMMJ;
	}

	public void setLMMJ(Double lMMJ) {
		if (this.lMMJ != lMMJ) {
			this.lMMJ = lMMJ;
			modify_lMMJ = true;
		}
	}

	private String fBF;
	private boolean modify_fBF = false;

	public String getFBF() {
		return fBF;
	}

	public void setFBF(String fBF) {
		if (this.fBF != fBF) {
			this.fBF = fBF;
			modify_fBF = true;
		}
	}

	private String zS;
	private boolean modify_zS = false;

	public String getZS() {
		return zS;
	}

	public void setZS(String zS) {
		if (this.zS != zS) {
			this.zS = zS;
			modify_zS = true;
		}
	}

	private String lZ;
	private boolean modify_lZ = false;

	public String getLZ() {
		return lZ;
	}

	public void setLZ(String lZ) {
		if (this.lZ != lZ) {
			this.lZ = lZ;
			modify_lZ = true;
		}
	}

	private String zLND;
	private boolean modify_zLND = false;

	public String getZLND() {
		return zLND;
	}

	public void setZLND(String zLND) {
		if (this.zLND != zLND) {
			this.zLND = zLND;
			modify_zLND = true;
		}
	}

	private String xDM;
	private boolean modify_xDM = false;

	public String getXDM() {
		return xDM;
	}

	public void setXDM(String xDM) {
		if (this.xDM != xDM) {
			this.xDM = xDM;
			modify_xDM = true;
		}
	}

	private String gYDR;
	private boolean modify_gYDR = false;

	public String getGYDR() {
		return gYDR;
	}

	public void setGYDR(String gYDR) {
		if (this.gYDR != gYDR) {
			this.gYDR = gYDR;
			modify_gYDR = true;
		}
	}

	private String dYQNR;
	private boolean modify_dYQNR = false;

	public String getDYQNR() {
		return dYQNR;
	}

	public void setDYQNR(String dYQNR) {
		if (this.dYQNR != dYQNR) {
			this.dYQNR = dYQNR;
			modify_dYQNR = true;
		}
	}

	private String dYWLX;
	private boolean modify_dYWLX = false;

	public String getDYWLX() {
		return dYWLX;
	}

	public void setDYWLX(String dYWLX) {
		if (this.dYWLX != dYWLX) {
			this.dYWLX = dYWLX;
			modify_dYWLX = true;
		}
	}

	private Integer dYSW;
	private boolean modify_dYSW = false;

	public Integer getDYSW() {
		return dYSW;
	}

	public void setDYSW(Integer dYSW) {
		if (this.dYSW != dYSW) {
			this.dYSW = dYSW;
			modify_dYSW = true;
		}
	}

	private String gRDR1;
	private boolean modify_gRDR1 = false;

	public String getGRDR1() {
		return gRDR1;
	}

	public void setGRDR1(String gRDR1) {
		if (this.gRDR1 != gRDR1) {
			this.gRDR1 = gRDR1;
			modify_gRDR1 = true;
		}
	}

	private String dYQNR1;
	private boolean modify_dYQNR1 = false;

	public String getDYQNR1() {
		return dYQNR1;
	}

	public void setDYQNR1(String dYQNR1) {
		if (this.dYQNR1 != dYQNR1) {
			this.dYQNR1 = dYQNR1;
			modify_dYQNR1 = true;
		}
	}

	private Double sCYQMJ;
	private boolean modify_sCYQMJ = false;

	public Double getSCYQMJ() {
		return sCYQMJ;
	}

	public void setSCYQMJ(Double sCYQMJ) {
		if (this.sCYQMJ != sCYQMJ) {
			this.sCYQMJ = sCYQMJ;
			modify_sCYQMJ = true;
		}
	}

	private String dYFS;
	private boolean modify_dYFS = false;

	public String getDYFS() {
		return dYFS;
	}

	public void setDYFS(String dYFS) {
		if (this.dYFS != dYFS) {
			this.dYFS = dYFS;
			modify_dYFS = true;
		}
	}

	private String zJGCJD;
	private boolean modify_zJGCJD = false;

	public String getZJGCJD() {
		return zJGCJD;
	}

	public void setZJGCJD(String zJGCJD) {
		if (this.zJGCJD != zJGCJD) {
			this.zJGCJD = zJGCJD;
			modify_zJGCJD = true;
		}
	}

	private Double bDBZZQSE;
	private boolean modify_bDBZZQSE = false;

	public Double getBDBZZQSE() {
		return bDBZZQSE;
	}

	public void setBDBZZQSE(Double bDBZZQSE) {
		if (this.bDBZZQSE != bDBZZQSE) {
			this.bDBZZQSE = bDBZZQSE;
			modify_bDBZZQSE = true;
		}
	}

	private String dBFW;
	private boolean modify_dBFW = false;

	public String getDBFW() {
		return dBFW;
	}

	public void setDBFW(String dBFW) {
		if (this.dBFW != dBFW) {
			this.dBFW = dBFW;
			modify_dBFW = true;
		}
	}

	private String zGZQQDSS;
	private boolean modify_zGZQQDSS = false;

	public String getZGZQQDSS() {
		return zGZQQDSS;
	}

	public void setZGZQQDSS(String zGZQQDSS) {
		if (this.zGZQQDSS != zGZQQDSS) {
			this.zGZQQDSS = zGZQQDSS;
			modify_zGZQQDSS = true;
		}
	}

	private Double zGZQSE;
	private boolean modify_zGZQSE = false;

	public Double getZGZQSE() {
		return zGZQSE;
	}

	public void setZGZQSE(Double zGZQSE) {
		if (this.zGZQSE != zGZQSE) {
			this.zGZQSE = zGZQSE;
			modify_zGZQSE = true;
		}
	}

	private String yGDJZL;
	private boolean modify_yGDJZL = false;

	public String getYGDJZL() {
		return yGDJZL;
	}

	public void setYGDJZL(String yGDJZL) {
		if (this.yGDJZL != yGDJZL) {
			this.yGDJZL = yGDJZL;
			modify_yGDJZL = true;
		}
	}

	private String yWR;
	private boolean modify_yWR = false;

	public String getYWR() {
		return yWR;
	}

	public void setYWR(String yWR) {
		if (this.yWR != yWR) {
			this.yWR = yWR;
			modify_yWR = true;
		}
	}

	private String yWRZJZL;
	private boolean modify_yWRZJZL = false;

	public String getYWRZJZL() {
		return yWRZJZL;
	}

	public void setYWRZJZL(String yWRZJZL) {
		if (this.yWRZJZL != yWRZJZL) {
			this.yWRZJZL = yWRZJZL;
			modify_yWRZJZL = true;
		}
	}

	private String yYSX;
	private boolean modify_yYSX = false;

	public String getYYSX() {
		return yYSX;
	}

	public void setYYSX(String yYSX) {
		if (this.yYSX != yYSX) {
			this.yYSX = yYSX;
			modify_yYSX = true;
		}
	}

	private String zXYYYY;
	private boolean modify_zXYYYY = false;

	public String getZXYYYY() {
		return zXYYYY;
	}

	public void setZXYYYY(String zXYYYY) {
		if (this.zXYYYY != zXYYYY) {
			this.zXYYYY = zXYYYY;
			modify_zXYYYY = true;
		}
	}

	private String cFLX;
	private boolean modify_cFLX = false;

	public String getCFLX() {
		return cFLX;
	}

	public void setCFLX(String cFLX) {
		if (this.cFLX != cFLX) {
			this.cFLX = cFLX;
			modify_cFLX = true;
		}
	}

	private Integer lHSX;
	private boolean modify_lHSX = false;

	public Integer getLHSX() {
		return lHSX;
	}

	public void setLHSX(Integer lHSX) {
		if (this.lHSX != lHSX) {
			this.lHSX = lHSX;
			modify_lHSX = true;
		}
	}

	private Date cFSJ;
	private boolean modify_cFSJ = false;

	public Date getCFSJ() {
		return cFSJ;
	}

	public void setCFSJ(Date cFSJ) {
		if (this.cFSJ != cFSJ) {
			this.cFSJ = cFSJ;
			modify_cFSJ = true;
		}
	}

	private String cFJG;
	private boolean modify_cFJG = false;

	public String getCFJG() {
		return cFJG;
	}

	public void setCFJG(String cFJG) {
		if (this.cFJG != cFJG) {
			this.cFJG = cFJG;
			modify_cFJG = true;
		}
	}

	private String cFWH;
	private boolean modify_cFWH = false;

	public String getCFWH() {
		return cFWH;
	}

	public void setCFWH(String cFWH) {
		if (this.cFWH != cFWH) {
			this.cFWH = cFWH;
			modify_cFWH = true;
		}
	}

	private String cFWJ;
	private boolean modify_cFWJ = false;

	public String getCFWJ() {
		return cFWJ;
	}

	public void setCFWJ(String cFWJ) {
		if (this.cFWJ != cFWJ) {
			this.cFWJ = cFWJ;
			modify_cFWJ = true;
		}
	}

	private String cFFW;
	private boolean modify_cFFW = false;

	public String getCFFW() {
		return cFFW;
	}

	public void setCFFW(String cFFW) {
		if (this.cFFW != cFFW) {
			this.cFFW = cFFW;
			modify_cFFW = true;
		}
	}

	private String jFJG;
	private boolean modify_jFJG = false;

	public String getJFJG() {
		return jFJG;
	}

	public void setJFJG(String jFJG) {
		if (this.jFJG != jFJG) {
			this.jFJG = jFJG;
			modify_jFJG = true;
		}
	}

	private String jFWH;
	private boolean modify_jFWH = false;

	public String getJFWH() {
		return jFWH;
	}

	public void setJFWH(String jFWH) {
		if (this.jFWH != jFWH) {
			this.jFWH = jFWH;
			modify_jFWH = true;
		}
	}

	private String jFWJ;
	private boolean modify_jFWJ = false;

	public String getJFWJ() {
		return jFWJ;
	}

	public void setJFWJ(String jFWJ) {
		if (this.jFWJ != jFWJ) {
			this.jFWJ = jFWJ;
			modify_jFWJ = true;
		}
	}

	private String zYSZ;
	private boolean modify_zYSZ = false;

	public String getZYSZ() {
		return zYSZ;
	}

	public void setZYSZ(String zYSZ) {
		if (this.zYSZ != zYSZ) {
			this.zYSZ = zYSZ;
			modify_zYSZ = true;
		}
	}

	private String zWRZJH;
	private boolean modify_zWRZJH = false;

	public String getZWRZJH() {
		return zWRZJH;
	}

	public void setZWRZJH(String zWRZJH) {
		if (this.zWRZJH != zWRZJH) {
			this.zWRZJH = zWRZJH;
			modify_zWRZJH = true;
		}
	}

	private String dBRZJH;
	private boolean modify_dBRZJH = false;

	public String getDBRZJH() {
		return dBRZJH;
	}

	public void setDBRZJH(String dBRZJH) {
		if (this.dBRZJH != dBRZJH) {
			this.dBRZJH = dBRZJH;
			modify_dBRZJH = true;
		}
	}

	private String hTBH;
	private boolean modify_hTBH = false;

	public String getHTBH() {
		return hTBH;
	}

	public void setHTBH(String hTBH) {
		if (this.hTBH != hTBH) {
			this.hTBH = hTBH;
			modify_hTBH = true;
		}
	}
	private String wSPZH;
	private boolean modify_wSPZH = false;

	public String getWSPZH() {
		return wSPZH;
	}

	public void setWSPZH(String wSPZH) {
		if (this.wSPZH != wSPZH) {
			this.wSPZH = wSPZH;
			modify_wSPZH = true;
		}
	}

	private Date hTSJ;
	private boolean modify_hTSJ = false;

	public Date getHTSJ() {
		return hTSJ;
	}

	public void setHTSJ(Date hTSJ) {
		if (this.hTSJ != hTSJ) {
			this.hTSJ = hTSJ;
			modify_hTSJ = true;
		}
	}

	private Date bASJ;
	private boolean modify_bASJ = false;

	public Date getBASJ() {
		return bASJ;
	}

	public void setBASJ(Date bASJ) {
		if (this.bASJ != bASJ) {
			this.bASJ = bASJ;
			modify_bASJ = true;
		}
	}

	private Double hTCJJG;
	private boolean modify_hTCJJG = false;

	public Double getHTCJJG() {
		return hTCJJG;
	}

	public void setHTCJJG(Double hTCJJG) {
		if (this.hTCJJG != hTCJJG) {
			this.hTCJJG = hTCJJG;
			modify_hTCJJG = true;
		}
	}

	private Date wSSJ;
	private boolean modify_wSSJ = false;

	public Date getWSSJ() {
		return wSSJ;
	}

	public void setWSSJ(Date wSSJ) {
		if (this.wSSJ != wSSJ) {
			this.wSSJ = wSSJ;
			modify_wSSJ = true;
		}
	}

	private Double wSJE;
	private boolean modify_wSJE = false;

	public Double getWSJE() {
		return wSJE;
	}

	public void setWSJE(Double wSJE) {
		if (this.wSJE != wSJE) {
			this.wSJE = wSJE;
			modify_wSJE = true;
		}
	}

	private Double dYMJ;
	private boolean modify_dYMJ = false;

	public Double getDYMJ() {
		return dYMJ;
	}

	public void setDYMJ(Double dYMJ) {
		if (this.dYMJ != dYMJ) {
			this.dYMJ = dYMJ;
			modify_dYMJ = true;
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

	private String zXDYYWH;
	private boolean modify_zXDYYWH = false;

	public String getZXDYYWH() {
		return zXDYYWH;
	}

	public void setZXDYYWH(String zXDYYWH) {
		if (this.zXDYYWH != zXDYYWH) {
			this.zXDYYWH = zXDYYWH;
			modify_zXDYYWH = true;
		}
	}

	private String zXDYYY;
	private boolean modify_zXDYYY = false;

	public String getZXDYYY() {
		return zXDYYY;
	}

	public void setZXDYYY(String zXDYYY) {
		if (this.zXDYYY != zXDYYY) {
			this.zXDYYY = zXDYYY;
			modify_zXDYYY = true;
		}
	}

	private Date zXSJ;
	private boolean modify_zXSJ = false;

	public Date getZXSJ() {
		return zXSJ;
	}

	public void setZXSJ(Date zXSJ) {
		if (this.zXSJ != zXSJ) {
			this.zXSJ = zXSJ;
			modify_zXSJ = true;
		}
	}

	private String jGZWBH;
	private boolean modify_jGZWBH = false;

	public String getJGZWBH() {
		return jGZWBH;
	}

	public void setJGZWBH(String jGZWBH) {
		if (this.jGZWBH != jGZWBH) {
			this.jGZWBH = jGZWBH;
			modify_jGZWBH = true;
		}
	}

	private String jGZWMC;
	private boolean modify_jGZWMC = false;

	public String getJGZWMC() {
		return jGZWMC;
	}

	public void setJGZWMC(String jGZWMC) {
		if (this.jGZWMC != jGZWMC) {
			this.jGZWMC = jGZWMC;
			modify_jGZWMC = true;
		}
	}

	private Integer jGZWSL;
	private boolean modify_jGZWSL = false;

	public Integer getJGZWSL() {
		return jGZWSL;
	}

	public void setJGZWSL(Integer jGZWSL) {
		if (this.jGZWSL != jGZWSL) {
			this.jGZWSL = jGZWSL;
			modify_jGZWSL = true;
		}
	}

	private Double jGZWMJ;
	private boolean modify_jGZWMJ = false;

	public Double getJGZWMJ() {
		return jGZWMJ;
	}

	public void setJGZWMJ(Double jGZWMJ) {
		if (this.jGZWMJ != jGZWMJ) {
			this.jGZWMJ = jGZWMJ;
			modify_jGZWMJ = true;
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

	private String tDHYSYQR;
	private boolean modify_tDHYSYQR = false;

	public String getTDHYSYQR() {
		return tDHYSYQR;
	}

	public void setTDHYSYQR(String tDHYSYQR) {
		if (this.tDHYSYQR != tDHYSYQR) {
			this.tDHYSYQR = tDHYSYQR;
			modify_tDHYSYQR = true;
		}
	}

	private Double tDHYSYMJ;
	private boolean modify_tDHYSYMJ = false;

	public Double getTDHYSYMJ() {
		return tDHYSYMJ;
	}

	public void setTDHYSYMJ(Double tDHYSYMJ) {
		if (this.tDHYSYMJ != tDHYSYMJ) {
			this.tDHYSYMJ = tDHYSYMJ;
			modify_tDHYSYMJ = true;
		}
	}

	private String gJZWLX;
	private boolean modify_gJZWLX = false;

	public String getGJZWLX() {
		return gJZWLX;
	}

	public void setGJZWLX(String gJZWLX) {
		if (this.gJZWLX != gJZWLX) {
			this.gJZWLX = gJZWLX;
			modify_gJZWLX = true;
		}
	}

	private String gJZWGHYT;
	private boolean modify_gJZWGHYT = false;

	public String getGJZWGHYT() {
		return gJZWGHYT;
	}

	public void setGJZWGHYT(String gJZWGHYT) {
		if (this.gJZWGHYT != gJZWGHYT) {
			this.gJZWGHYT = gJZWGHYT;
			modify_gJZWGHYT = true;
		}
	}

	private Double gJZWMJ;
	private boolean modify_gJZWMJ = false;

	public Double getGJZWMJ() {
		return gJZWMJ;
	}

	public void setGJZWMJ(Double gJZWMJ) {
		if (this.gJZWMJ != gJZWMJ) {
			this.gJZWMJ = gJZWMJ;
			modify_gJZWMJ = true;
		}
	}

	private Double cBSYQMJ;
	private boolean modify_cBSYQMJ = false;

	public Double getCBSYQMJ() {
		return cBSYQMJ;
	}

	public void setCBSYQMJ(Double cBSYQMJ) {
		if (this.cBSYQMJ != cBSYQMJ) {
			this.cBSYQMJ = cBSYQMJ;
			modify_cBSYQMJ = true;
		}
	}

	private String tDSYQXZ;
	private boolean modify_tDSYQXZ = false;

	public String getTDSYQXZ() {
		return tDSYQXZ;
	}

	public void setTDSYQXZ(String tDSYQXZ) {
		if (this.tDSYQXZ != tDSYQXZ) {
			this.tDSYQXZ = tDSYQXZ;
			modify_tDSYQXZ = true;
		}
	}

	private String sYTTLX;
	private boolean modify_sYTTLX = false;

	public String getSYTTLX() {
		return sYTTLX;
	}

	public void setSYTTLX(String sYTTLX) {
		if (this.sYTTLX != sYTTLX) {
			this.sYTTLX = sYTTLX;
			modify_sYTTLX = true;
		}
	}

	private String yZYFS;
	private boolean modify_yZYFS = false;

	public String getYZYFS() {
		return yZYFS;
	}

	public void setYZYFS(String yZYFS) {
		if (this.yZYFS != yZYFS) {
			this.yZYFS = yZYFS;
			modify_yZYFS = true;
		}
	}

	private String cYZL;
	private boolean modify_cYZL = false;

	public String getCYZL() {
		return cYZL;
	}

	public void setCYZL(String cYZL) {
		if (this.cYZL != cYZL) {
			this.cYZL = cYZL;
			modify_cYZL = true;
		}
	}

	private Integer sYZCL;
	private boolean modify_sYZCL = false;

	public Integer getSYZCL() {
		return sYZCL;
	}

	public void setSYZCL(Integer sYZCL) {
		if (this.sYZCL != sYZCL) {
			this.sYZCL = sYZCL;
			modify_sYZCL = true;
		}
	}

	private String lDSYQXZ;
	private boolean modify_lDSYQXZ = false;

	public String getLDSYQXZ() {
		return lDSYQXZ;
	}

	public void setLDSYQXZ(String lDSYQXZ) {
		if (this.lDSYQXZ != lDSYQXZ) {
			this.lDSYQXZ = lDSYQXZ;
			modify_lDSYQXZ = true;
		}
	}

	private String sLLMSYQR1;
	private boolean modify_sLLMSYQR1 = false;

	public String getSLLMSYQR1() {
		return sLLMSYQR1;
	}

	public void setSLLMSYQR1(String sLLMSYQR1) {
		if (this.sLLMSYQR1 != sLLMSYQR1) {
			this.sLLMSYQR1 = sLLMSYQR1;
			modify_sLLMSYQR1 = true;
		}
	}

	private String sLLMSYQR2;
	private boolean modify_sLLMSYQR2 = false;

	public String getSLLMSYQR2() {
		return sLLMSYQR2;
	}

	public void setSLLMSYQR2(String sLLMSYQR2) {
		if (this.sLLMSYQR2 != sLLMSYQR2) {
			this.sLLMSYQR2 = sLLMSYQR2;
			modify_sLLMSYQR2 = true;
		}
	}

	private String qY;
	private boolean modify_qY = false;

	public String getQY() {
		return qY;
	}

	public void setQY(String qY) {
		if (this.qY != qY) {
			this.qY = qY;
			modify_qY = true;
		}
	}

	private String lB;
	private boolean modify_lB = false;

	public String getLB() {
		return lB;
	}

	public void setLB(String lB) {
		if (this.lB != lB) {
			this.lB = lB;
			modify_lB = true;
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

	private String qSFS;
	private boolean modify_qSFS = false;

	public String getQSFS() {
		return qSFS;
	}

	public void setQSFS(String qSFS) {
		if (this.qSFS != qSFS) {
			this.qSFS = qSFS;
			modify_qSFS = true;
		}
	}

	private String sYLX;
	private boolean modify_sYLX = false;

	public String getSYLX() {
		return sYLX;
	}

	public void setSYLX(String sYLX) {
		if (this.sYLX != sYLX) {
			this.sYLX = sYLX;
			modify_sYLX = true;
		}
	}

	private Double qSL;
	private boolean modify_qSL = false;

	public Double getQSL() {
		return qSL;
	}

	public void setQSL(Double qSL) {
		if (this.qSL != qSL) {
			this.qSL = qSL;
			modify_qSL = true;
		}
	}

	private String qSYT;
	private boolean modify_qSYT = false;

	public String getQSYT() {
		return qSYT;
	}

	public void setQSYT(String qSYT) {
		if (this.qSYT != qSYT) {
			this.qSYT = qSYT;
			modify_qSYT = true;
		}
	}

	private Double kCMJ;
	private boolean modify_kCMJ = false;

	public Double getKCMJ() {
		return kCMJ;
	}

	public void setKCMJ(Double kCMJ) {
		if (this.kCMJ != kCMJ) {
			this.kCMJ = kCMJ;
			modify_kCMJ = true;
		}
	}

	private String kCFS;
	private boolean modify_kCFS = false;

	public String getKCFS() {
		return kCFS;
	}

	public void setKCFS(String kCFS) {
		if (this.kCFS != kCFS) {
			this.kCFS = kCFS;
			modify_kCFS = true;
		}
	}

	private String kCKZ;
	private boolean modify_kCKZ = false;

	public String getKCKZ() {
		return kCKZ;
	}

	public void setKCKZ(String kCKZ) {
		if (this.kCKZ != kCKZ) {
			this.kCKZ = kCKZ;
			modify_kCKZ = true;
		}
	}

	private String sCGM;
	private boolean modify_sCGM = false;

	public String getSCGM() {
		return sCGM;
	}

	public void setSCGM(String sCGM) {
		if (this.sCGM != sCGM) {
			this.sCGM = sCGM;
			modify_sCGM = true;
		}
	}

	private String xYDZL;
	private boolean modify_xYDZL = false;

	public String getXYDZL() {
		return xYDZL;
	}

	public void setXYDZL(String xYDZL) {
		if (this.xYDZL != xYDZL) {
			this.xYDZL = xYDZL;
			modify_xYDZL = true;
		}
	}

	private String gYDQLR;
	private boolean modify_gYDQLR = false;

	public String getGYDQLR() {
		return gYDQLR;
	}

	public void setGYDQLR(String gYDQLR) {
		if (this.gYDQLR != gYDQLR) {
			this.gYDQLR = gYDQLR;
			modify_gYDQLR = true;
		}
	}

	private String gYDQLRZJH;
	private boolean modify_gYDQLRZJH = false;

	public String getGYDQLRZJH() {
		return gYDQLRZJH;
	}

	public void setGYDQLRZJH(String gYDQLRZJH) {
		if (this.gYDQLRZJH != gYDQLRZJH) {
			this.gYDQLRZJH = gYDQLRZJH;
			modify_gYDQLRZJH = true;
		}
	}

	
	private Double dYPGJZ;
	private boolean modify_dYPGJZ = false;
	public Double getDYPGJZ() {
		return dYPGJZ;
	}

	public void setDYPGJZ(Double dYPGJZ) {
		if (this.dYPGJZ != dYPGJZ) {
			this.dYPGJZ = dYPGJZ;
			modify_dYPGJZ = true;
		}
	}
	
	private String dYBDCLX;
	private boolean modify_dYBDCLX = false;

	public String getDYBDCLX() {
		return dYBDCLX;
	}

	public void setDYBDCLX(String dYBDCLX) {
		if (this.dYBDCLX != dYBDCLX) {
			this.dYBDCLX = dYBDCLX;
			modify_dYBDCLX = true;
		}
	}

	private String zJJZWZL;
	private boolean modify_zJJZWZL = false;

	public String getZJJZWZL() {
		return zJJZWZL;
	}

	public void setZJJZWZL(String zJJZWZL) {
		if (this.zJJZWZL != zJJZWZL) {
			this.zJJZWZL = zJJZWZL;
			modify_zJJZWZL = true;
		}
	}

	private String zJJZWDYFW;
	private boolean modify_zJJZWDYFW = false;

	public String getZJJZWDYFW() {
		return zJJZWDYFW;
	}

	public void setZJJZWDYFW(String zJJZWDYFW) {
		if (this.zJJZWDYFW != zJJZWDYFW) {
			this.zJJZWDYFW = zJJZWDYFW;
			modify_zJJZWDYFW = true;
		}
	}

	private String bDCZL;
	private boolean modify_bDCZL = false;

	public String getBDCZL() {
		return bDCZL;
	}

	public void setBDCZL(String bDCZL) {
		if (this.bDCZL != bDCZL) {
			this.bDCZL = bDCZL;
			modify_bDCZL = true;
		}
	}

	private String yWRZJH;
	private boolean modify_yWRZJH = false;

	public String getYWRZJH() {
		return yWRZJH;
	}

	public void setYWRZJH(String yWRZJH) {
		if (this.yWRZJH != yWRZJH) {
			this.yWRZJH = yWRZJH;
			modify_yWRZJH = true;
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

	private String gYDQLRZJZL;
	private boolean modify_gYDQLRZJZL = false;

	public String getGYDQLRZJZL() {
		return gYDQLRZJZL;
	}

	public void setGYDQLRZJZL(String gYDQLRZJZL) {
		if (this.gYDQLRZJZL != gYDQLRZJZL) {
			this.gYDQLRZJZL = gYDQLRZJZL;
			modify_gYDQLRZJZL = true;
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

	private String zXFJ;
	private boolean modify_zXFJ = false;

	public String getZXFJ() {
		return zXFJ;
	}

	public void setZXFJ(String zXFJ) {
		if (this.zXFJ != zXFJ) {
			this.zXFJ = zXFJ;
			modify_zXFJ = true;
		}
	}

	private String dYR;
	private boolean modify_dYR = false;

	public String getDYR() {
		return dYR;
	}

	public void setDYR(String dYR) {
		if (this.dYR != dYR) {
			this.dYR = dYR;
			modify_dYR = true;
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
	
	private String zQDW="1";
	private boolean modify_zQDW = false;

	public String getZQDW() {
		return zQDW;
	}

	public void setZQDW(String zQDW) {
		if (this.zQDW != zQDW&&!org.springframework.util.StringUtils.isEmpty(zQDW)) {
			this.zQDW = zQDW;
			modify_zQDW = true;
		}
	}
	
	private String pLAINTIFF="";
	private boolean modify_pLAINTIFF = false;

	public String getPLAINTIFF() {
		return pLAINTIFF;
	}

	public void setPLAINTIFF(String pLAINTIFF) {
		if (this.pLAINTIFF != pLAINTIFF) {
			this.pLAINTIFF = pLAINTIFF;
			modify_pLAINTIFF = true;
		}
	}
	
	private String dEFENDANT="";
	private boolean modify_dEFENDANT = false;

	public String getDEFENDANT() {
		return dEFENDANT;
	}

	public void setDEFENDANT(String dEFENDANT) {
		if (this.dEFENDANT != dEFENDANT) {
			this.dEFENDANT = dEFENDANT;
			modify_dEFENDANT = true;
		}
	}
	
	
	private String tdpgjz="";
	private boolean modify_tdpgjz = false;

	public String getTDPGJZ() {
		return tdpgjz;
	}

	public void setTDPGJZ(String tdpgjz) {
		if (this.tdpgjz != tdpgjz) {
			this.tdpgjz = tdpgjz;
			modify_tdpgjz = true;
		}
	}
	
	
	private String zwr="";
	private boolean modify_zwr = false;

	public String getZWR() {
		return zwr;
	}

	public void setZWR(String zwr) {
		if (this.zwr != zwr) {
			this.zwr = zwr;
			modify_zwr = true;
		}
	}
	
	//用地总面积	
	private Double yDZMJ;
	private boolean modify_yDZMJ = false;

	public Double getYDZMJ() {
		return yDZMJ;
	}

	public void setYDZMJ(Double yDZMJ) {
		if (this.yDZMJ != yDZMJ) {
			this.yDZMJ = yDZMJ;
			modify_yDZMJ = true;
		}
	}
	//抵押用地总面积
	private Double dYYDMJ;
	private boolean modify_dYYDMJ = false;

	public Double getDYYDMJ() {
		return dYYDMJ;
	}

	public void setDYYDMJ(Double dYYDMJ) {
		if (this.dYYDMJ != dYYDMJ) {
			this.dYYDMJ = dYYDMJ;
			modify_dYYDMJ = true;
		}
	}
	//总建筑面积
	private Double zJZMJ;
	private boolean modify_zJZMJ = false;

	public Double getZJZMJ() {
		return zJZMJ;
	}

	public void setZJZMJ(Double zJZMJ) {
		if (this.zJZMJ != zJZMJ) {
			this.zJZMJ = zJZMJ;
			modify_zJZMJ = true;
		}
	}
	//抵押建筑面积
	private Double dYJZMJ;
	private boolean modify_dYJZMJ = false;

	public Double getDYJZMJ() {
		return dYJZMJ;
	}

	public void setDYJZMJ(Double dYJZMJ) {
		if (this.dYJZMJ != dYJZMJ) {
			this.dYJZMJ = dYJZMJ;
			modify_dYJZMJ = true;
		}
	}
	//土地用途
	private String dYTDYT;
	private boolean modify_dYTDYT = false;

	public String getDYTDYT() {
		return dYTDYT;
	}

	public void setDYTDYT(String dYTDYT) {
		if (this.dYTDYT != dYTDYT) {
			this.dYTDYT = dYTDYT;
			modify_dYTDYT = true;
		}
	}
	//抵押金性质
		private Integer dYDYJXZ;
		private boolean modify_dYDYJXZ = false;

		public Integer getDYDYJXZ() {
			return dYDYJXZ;
		}

		public void setDYDYJXZ(Integer dYDYJXZ) {
			if (this.dYDYJXZ != dYDYJXZ) {
				this.dYDYJXZ = dYDYJXZ;
				modify_dYDYJXZ = true;
			}
		}
		
		private Date fYSDSJ;// 法院送达时间
		private boolean modify_fYSDSJ = false;

		public Date getFYSDSJ() {
			return fYSDSJ;
		}

		public void setFYSDSJ(Date fYSDSJ) {
			if (this.fYSDSJ != fYSDSJ) {
				this.fYSDSJ = fYSDSJ;
				modify_fYSDSJ = true;
			}
		}
		
		private Double dGBDBZZQSE;//单个被担保主债权数额
		private boolean modify_dGBDBZZQSE = false;

		public Double getDGBDBZZQSE() {
			return dGBDBZZQSE;
		}

		public void setDGBDBZZQSE(Double dGBDBZZQSE) {
			if (this.dGBDBZZQSE != dGBDBZZQSE) {
				this.dGBDBZZQSE = dGBDBZZQSE;
				modify_dGBDBZZQSE = true;
			}
		}
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_dJDYID = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_qLID = false;
		modify_zDDM = false;
		modify_nYDMJ = false;
		modify_gDMJ = false;
		modify_lDMJ = false;
		modify_cDMJ = false;
		modify_qTNYDMJ = false;
		modify_jSYDMJ = false;
		modify_wLYDMJ = false;
		modify_sYQMJ = false;
		modify_sYJZE = false;
		modify_sYJBZYJ = false;
		modify_sYJJNQK = false;
		modify_hYMJDW = false;
		modify_hYMJ = false;
		modify_jZMJ = false;
		modify_fDZL = false;
		modify_tDSYQR = false;
		modify_dYTDMJ = false;
		modify_fTTDMJ = false;
		modify_fDCJYJG = false;
		modify_jGSJ = false;
		modify_zH = false;
		modify_zCS = false;
		modify_gHYT = false;
		modify_fWJG = false;
		modify_fWXZ = false;
		modify_sZC = false;
		modify_zYJZMJ = false;
		modify_fTJZMJ = false;
		modify_lMMJ = false;
		modify_fBF = false;
		modify_zS = false;
		modify_lZ = false;
		modify_zLND = false;
		modify_xDM = false;
		modify_gYDR = false;
		modify_dYQNR = false;
		modify_dYWLX = false;
		modify_dYSW = false;
		modify_gRDR1 = false;
		modify_dYQNR1 = false;
		modify_sCYQMJ = false;
		modify_dYFS = false;
		modify_zJGCJD = false;
		modify_bDBZZQSE = false;
		modify_dBFW = false;
		modify_zGZQQDSS = false;
		modify_zGZQSE = false;
		modify_yGDJZL = false;
		modify_yWR = false;
		modify_yWRZJZL = false;
		modify_yYSX = false;
		modify_zXYYYY = false;
		modify_cFLX = false;
		modify_lHSX = false;
		modify_cFSJ = false;
		modify_cFJG = false;
		modify_cFWH = false;
		modify_cFWJ = false;
		modify_cFFW = false;
		modify_jFJG = false;
		modify_jFWH = false;
		modify_jFWJ = false;
		modify_zYSZ = false;
		modify_zWRZJH = false;
		modify_dBRZJH = false;
		modify_hTBH = false;
		modify_dYMJ = false;
		modify_yXBZ = false;
		modify_zXDYYWH = false;
		modify_zXDYYY = false;
		modify_zXSJ = false;
		modify_jGZWBH = false;
		modify_jGZWMC = false;
		modify_jGZWSL = false;
		modify_jGZWMJ = false;
		modify_zL = false;
		modify_tDHYSYQR = false;
		modify_tDHYSYMJ = false;
		modify_gJZWLX = false;
		modify_gJZWGHYT = false;
		modify_gJZWMJ = false;
		modify_cBSYQMJ = false;
		modify_tDSYQXZ = false;
		modify_sYTTLX = false;
		modify_yZYFS = false;
		modify_cYZL = false;
		modify_sYZCL = false;
		modify_lDSYQXZ = false;
		modify_sLLMSYQR1 = false;
		modify_sLLMSYQR2 = false;
		modify_qY = false;
		modify_lB = false;
		modify_xB = false;
		modify_qSFS = false;
		modify_sYLX = false;
		modify_qSL = false;
		modify_qSYT = false;
		modify_kCMJ = false;
		modify_kCFS = false;
		modify_kCKZ = false;
		modify_sCGM = false;
		modify_xYDZL = false;
		modify_gYDQLR = false;
		modify_gYDQLRZJH = false;
		modify_dYBDCLX = false;
		modify_zJJZWZL = false;
		modify_zJJZWDYFW = false;
		modify_bDCZL = false;
		modify_yWRZJH = false;
		modify_qDJG = false;
		modify_gYDQLRZJZL = false;
		modify_dCXMID = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_zXDBR = false;
		modify_zXFJ = false;
		modify_dYR = false;
		modify_cASENUM = false;
		modify_gYRQK = false;
		modify_zQDW = false;
		modify_pLAINTIFF=false;
		modify_dEFENDANT=false;
		modify_tdpgjz = false;
		modify_zwr = false;
		modify_yDZMJ = false;
		modify_dYYDMJ = false;
		modify_zJZMJ = false;
		modify_dYJZMJ = false;
		modify_dYTDYT = false;
		modify_dYDYJXZ=false;
		modify_fYSDSJ = false;
		modify_dGBDBZZQSE = false;
		
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_dJDYID)
			listStrings.add("dJDYID");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_zDDM)
			listStrings.add("zDDM");
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
		if (!modify_sYQMJ)
			listStrings.add("sYQMJ");
		if (!modify_sYJZE)
			listStrings.add("sYJZE");
		if (!modify_sYJBZYJ)
			listStrings.add("sYJBZYJ");
		if (!modify_sYJJNQK)
			listStrings.add("sYJJNQK");
		if (!modify_hYMJDW)
			listStrings.add("hYMJDW");
		if (!modify_hYMJ)
			listStrings.add("hYMJ");
		if (!modify_jZMJ)
			listStrings.add("jZMJ");
		if (!modify_fDZL)
			listStrings.add("fDZL");
		if (!modify_tDSYQR)
			listStrings.add("tDSYQR");
		if (!modify_dYTDMJ)
			listStrings.add("dYTDMJ");
		if (!modify_fTTDMJ)
			listStrings.add("fTTDMJ");
		if (!modify_fDCJYJG)
			listStrings.add("fDCJYJG");
		if (!modify_jGSJ)
			listStrings.add("jGSJ");
		if (!modify_zH)
			listStrings.add("zH");
		if (!modify_zCS)
			listStrings.add("zCS");
		if (!modify_gHYT)
			listStrings.add("gHYT");
		if (!modify_fWJG)
			listStrings.add("fWJG");
		if (!modify_fWXZ)
			listStrings.add("fWXZ");
		if (!modify_sZC)
			listStrings.add("sZC");
		if (!modify_zYJZMJ)
			listStrings.add("zYJZMJ");
		if (!modify_fTJZMJ)
			listStrings.add("fTJZMJ");
		if (!modify_lMMJ)
			listStrings.add("lMMJ");
		if (!modify_fBF)
			listStrings.add("fBF");
		if (!modify_zS)
			listStrings.add("zS");
		if (!modify_lZ)
			listStrings.add("lZ");
		if (!modify_zLND)
			listStrings.add("zLND");
		if (!modify_xDM)
			listStrings.add("xDM");
		if (!modify_gYDR)
			listStrings.add("gYDR");
		if (!modify_dYQNR)
			listStrings.add("dYQNR");
		if (!modify_dYWLX)
			listStrings.add("dYWLX");
		if (!modify_dYSW)
			listStrings.add("dYSW");
		if (!modify_gRDR1)
			listStrings.add("gRDR1");
		if (!modify_dYQNR1)
			listStrings.add("dYQNR1");
		if (!modify_sCYQMJ)
			listStrings.add("sCYQMJ");
		if (!modify_dYFS)
			listStrings.add("dYFS");
		if (!modify_zJGCJD)
			listStrings.add("zJGCJD");
		if (!modify_bDBZZQSE)
			listStrings.add("bDBZZQSE");
		if (!modify_dBFW)
			listStrings.add("dBFW");
		if (!modify_zGZQQDSS)
			listStrings.add("zGZQQDSS");
		if (!modify_zGZQSE)
			listStrings.add("zGZQSE");
		if (!modify_yGDJZL)
			listStrings.add("yGDJZL");
		if (!modify_yWR)
			listStrings.add("yWR");
		if (!modify_yWRZJZL)
			listStrings.add("yWRZJZL");
		if (!modify_yYSX)
			listStrings.add("yYSX");
		if (!modify_zXYYYY)
			listStrings.add("zXYYYY");
		if (!modify_cFLX)
			listStrings.add("cFLX");
		if (!modify_lHSX)
			listStrings.add("lHSX");
		if (!modify_cFSJ)
			listStrings.add("cFSJ");
		if (!modify_cFJG)
			listStrings.add("cFJG");
		if (!modify_cFWH)
			listStrings.add("cFWH");
		if (!modify_cFWJ)
			listStrings.add("cFWJ");
		if (!modify_cFFW)
			listStrings.add("cFFW");
		if (!modify_jFJG)
			listStrings.add("jFJG");
		if (!modify_jFWH)
			listStrings.add("jFWH");
		if (!modify_jFWJ)
			listStrings.add("jFWJ");
		if (!modify_zYSZ)
			listStrings.add("zYSZ");
		if (!modify_zWRZJH)
			listStrings.add("zWRZJH");
		if (!modify_dBRZJH)
			listStrings.add("dBRZJH");
		if (!modify_hTBH)
			listStrings.add("hTBH");
		if (!modify_dYMJ)
			listStrings.add("dYMJ");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_zXDYYWH)
			listStrings.add("zXDYYWH");
		if (!modify_zXDYYY)
			listStrings.add("zXDYYY");
		if (!modify_zXSJ)
			listStrings.add("zXSJ");
		if (!modify_jGZWBH)
			listStrings.add("jGZWBH");
		if (!modify_jGZWMC)
			listStrings.add("jGZWMC");
		if (!modify_jGZWSL)
			listStrings.add("jGZWSL");
		if (!modify_jGZWMJ)
			listStrings.add("jGZWMJ");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_tDHYSYQR)
			listStrings.add("tDHYSYQR");
		if (!modify_tDHYSYMJ)
			listStrings.add("tDHYSYMJ");
		if (!modify_gJZWLX)
			listStrings.add("gJZWLX");
		if (!modify_gJZWGHYT)
			listStrings.add("gJZWGHYT");
		if (!modify_gJZWMJ)
			listStrings.add("gJZWMJ");
		if (!modify_cBSYQMJ)
			listStrings.add("cBSYQMJ");
		if (!modify_tDSYQXZ)
			listStrings.add("tDSYQXZ");
		if (!modify_sYTTLX)
			listStrings.add("sYTTLX");
		if (!modify_yZYFS)
			listStrings.add("yZYFS");
		if (!modify_cYZL)
			listStrings.add("cYZL");
		if (!modify_sYZCL)
			listStrings.add("sYZCL");
		if (!modify_lDSYQXZ)
			listStrings.add("lDSYQXZ");
		if (!modify_sLLMSYQR1)
			listStrings.add("sLLMSYQR1");
		if (!modify_sLLMSYQR2)
			listStrings.add("sLLMSYQR2");
		if (!modify_qY)
			listStrings.add("qY");
		if (!modify_lB)
			listStrings.add("lB");
		if (!modify_xB)
			listStrings.add("xB");
		if (!modify_qSFS)
			listStrings.add("qSFS");
		if (!modify_sYLX)
			listStrings.add("sYLX");
		if (!modify_qSL)
			listStrings.add("qSL");
		if (!modify_qSYT)
			listStrings.add("qSYT");
		if (!modify_kCMJ)
			listStrings.add("kCMJ");
		if (!modify_kCFS)
			listStrings.add("kCFS");
		if (!modify_kCKZ)
			listStrings.add("kCKZ");
		if (!modify_sCGM)
			listStrings.add("sCGM");
		if (!modify_xYDZL)
			listStrings.add("xYDZL");
		if (!modify_gYDQLR)
			listStrings.add("gYDQLR");
		if (!modify_gYDQLRZJH)
			listStrings.add("gYDQLRZJH");
		if (!modify_dYBDCLX)
			listStrings.add("dYBDCLX");
		if (!modify_zJJZWZL)
			listStrings.add("zJJZWZL");
		if (!modify_zJJZWDYFW)
			listStrings.add("zJJZWDYFW");
		if (!modify_bDCZL)
			listStrings.add("bDCZL");
		if (!modify_yWRZJH)
			listStrings.add("yWRZJH");
		if (!modify_qDJG)
			listStrings.add("qDJG");
		if (!modify_gYDQLRZJZL)
			listStrings.add("gYDQLRZJZL");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
			if (!modify_dYPGJZ)
				listStrings.add("dYPGJZ");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_zXDBR)
			listStrings.add("zXDBR");
		if (!modify_zXFJ)
			listStrings.add("zXFJ");
		if (!modify_dYR)
			listStrings.add("dYR");
		if (!modify_cASENUM)
			listStrings.add("cASENUM");
		if (!modify_gYRQK)
			listStrings.add("gYRQK");
		if (!modify_zQDW)
			listStrings.add("zQDW");
		if(!modify_pLAINTIFF)
			listStrings.add("pLAINTIFF");
		if(!modify_tdpgjz)
			listStrings.add("tdpgjz");
		if(!modify_dEFENDANT)
			listStrings.add("dEFENDANT");
		if(!modify_zwr)
			listStrings.add("zwr");
		if (!modify_yDZMJ)
			listStrings.add("yDZMJ");
		if (!modify_dYYDMJ) 
			listStrings.add("dYYDMJ");
		if (!modify_zJZMJ) 
			listStrings.add("zJZMJ");
		if (!modify_dYJZMJ) 
			listStrings.add("dYJZMJ");
		if (!modify_dYTDYT) 
			listStrings.add("dYTDYT");
		if (!modify_dYDYJXZ) 
			listStrings.add("dYDYJXZ");
		if (!modify_fYSDSJ) 
			listStrings.add("fYSDSJ");
		if (!modify_dGBDBZZQSE) 
			listStrings.add("dGBDBZZQSE");
		if (!modify_wSPZH)
			listStrings.add("wSPZH");
		if (!modify_hTSJ)
			listStrings.add("hTSJ");
		if (!modify_bASJ)
			listStrings.add("bASJ");
		if (!modify_hTCJJG)
			listStrings.add("hTCJJG");
		if (!modify_wSSJ)
			listStrings.add("wSSJ");
		if (!modify_wSJE)
			listStrings.add("wSJE");

		return StringHelper.ListToStrings(listStrings);
	}
}
