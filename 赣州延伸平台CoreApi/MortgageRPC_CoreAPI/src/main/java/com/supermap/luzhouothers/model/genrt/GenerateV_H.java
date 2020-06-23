package com.supermap.luzhouothers.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/23 
//* ----------------------------------------
//* Internal Entity h 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateV_H implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
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

	private String zRZH;
	private boolean modify_zRZH = false;

	public String getZRZH() {
		return zRZH;
	}

	public void setZRZH(String zRZH) {
		if (this.zRZH != zRZH) {
			this.zRZH = zRZH;
			modify_zRZH = true;
		}
	}

	private String lJZH;
	private boolean modify_lJZH = false;

	public String getLJZH() {
		return lJZH;
	}

	public void setLJZH(String lJZH) {
		if (this.lJZH != lJZH) {
			this.lJZH = lJZH;
			modify_lJZH = true;
		}
	}

	private String cH;
	private boolean modify_cH = false;

	public String getCH() {
		return cH;
	}

	public void setCH(String cH) {
		if (this.cH != cH) {
			this.cH = cH;
			modify_cH = true;
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

	private Double sJCS;
	private boolean modify_sJCS = false;

	public Double getSJCS() {
		return sJCS;
	}

	public void setSJCS(Double sJCS) {
		if (this.sJCS != sJCS) {
			this.sJCS = sJCS;
			modify_sJCS = true;
		}
	}

	private Integer hH;
	private boolean modify_hH = false;

	public Integer getHH() {
		return hH;
	}

	public void setHH(Integer hH) {
		if (this.hH != hH) {
			this.hH = hH;
			modify_hH = true;
		}
	}

	private String sHBW;
	private boolean modify_sHBW = false;

	public String getSHBW() {
		return sHBW;
	}

	public void setSHBW(String sHBW) {
		if (this.sHBW != sHBW) {
			this.sHBW = sHBW;
			modify_sHBW = true;
		}
	}

	private String hX;
	private boolean modify_hX = false;

	public String getHX() {
		return hX;
	}

	public void setHX(String hX) {
		if (this.hX != hX) {
			this.hX = hX;
			modify_hX = true;
		}
	}

	private String hXJG;
	private boolean modify_hXJG = false;

	public String getHXJG() {
		return hXJG;
	}

	public void setHXJG(String hXJG) {
		if (this.hXJG != hXJG) {
			this.hXJG = hXJG;
			modify_hXJG = true;
		}
	}

	private String fWYT1;
	private boolean modify_fWYT1 = false;

	public String getFWYT1() {
		return fWYT1;
	}

	public void setFWYT1(String fWYT1) {
		if (this.fWYT1 != fWYT1) {
			this.fWYT1 = fWYT1;
			modify_fWYT1 = true;
		}
	}

	private String fWYT2;
	private boolean modify_fWYT2 = false;

	public String getFWYT2() {
		return fWYT2;
	}

	public void setFWYT2(String fWYT2) {
		if (this.fWYT2 != fWYT2) {
			this.fWYT2 = fWYT2;
			modify_fWYT2 = true;
		}
	}

	private String fWYT3;
	private boolean modify_fWYT3 = false;

	public String getFWYT3() {
		return fWYT3;
	}

	public void setFWYT3(String fWYT3) {
		if (this.fWYT3 != fWYT3) {
			this.fWYT3 = fWYT3;
			modify_fWYT3 = true;
		}
	}

	private Double yCJZMJ;
	private boolean modify_yCJZMJ = false;

	public Double getYCJZMJ() {
		return yCJZMJ;
	}

	public void setYCJZMJ(Double yCJZMJ) {
		if (this.yCJZMJ != yCJZMJ) {
			this.yCJZMJ = yCJZMJ;
			modify_yCJZMJ = true;
		}
	}

	private Double yCTNJZMJ;
	private boolean modify_yCTNJZMJ = false;

	public Double getYCTNJZMJ() {
		return yCTNJZMJ;
	}

	public void setYCTNJZMJ(Double yCTNJZMJ) {
		if (this.yCTNJZMJ != yCTNJZMJ) {
			this.yCTNJZMJ = yCTNJZMJ;
			modify_yCTNJZMJ = true;
		}
	}

	private Double yCFTJZMJ;
	private boolean modify_yCFTJZMJ = false;

	public Double getYCFTJZMJ() {
		return yCFTJZMJ;
	}

	public void setYCFTJZMJ(Double yCFTJZMJ) {
		if (this.yCFTJZMJ != yCFTJZMJ) {
			this.yCFTJZMJ = yCFTJZMJ;
			modify_yCFTJZMJ = true;
		}
	}

	private Double yCDXBFJZMJ;
	private boolean modify_yCDXBFJZMJ = false;

	public Double getYCDXBFJZMJ() {
		return yCDXBFJZMJ;
	}

	public void setYCDXBFJZMJ(Double yCDXBFJZMJ) {
		if (this.yCDXBFJZMJ != yCDXBFJZMJ) {
			this.yCDXBFJZMJ = yCDXBFJZMJ;
			modify_yCDXBFJZMJ = true;
		}
	}

	private Double yCQTJZMJ;
	private boolean modify_yCQTJZMJ = false;

	public Double getYCQTJZMJ() {
		return yCQTJZMJ;
	}

	public void setYCQTJZMJ(Double yCQTJZMJ) {
		if (this.yCQTJZMJ != yCQTJZMJ) {
			this.yCQTJZMJ = yCQTJZMJ;
			modify_yCQTJZMJ = true;
		}
	}

	private Double yCFTXS;
	private boolean modify_yCFTXS = false;

	public Double getYCFTXS() {
		return yCFTXS;
	}

	public void setYCFTXS(Double yCFTXS) {
		if (this.yCFTXS != yCFTXS) {
			this.yCFTXS = yCFTXS;
			modify_yCFTXS = true;
		}
	}

	private Double sCJZMJ;
	private boolean modify_sCJZMJ = false;

	public Double getSCJZMJ() {
		return sCJZMJ;
	}

	public void setSCJZMJ(Double sCJZMJ) {
		if (this.sCJZMJ != sCJZMJ) {
			this.sCJZMJ = sCJZMJ;
			modify_sCJZMJ = true;
		}
	}

	private Double sCTNJZMJ;
	private boolean modify_sCTNJZMJ = false;

	public Double getSCTNJZMJ() {
		return sCTNJZMJ;
	}

	public void setSCTNJZMJ(Double sCTNJZMJ) {
		if (this.sCTNJZMJ != sCTNJZMJ) {
			this.sCTNJZMJ = sCTNJZMJ;
			modify_sCTNJZMJ = true;
		}
	}

	private Double sCFTJZMJ;
	private boolean modify_sCFTJZMJ = false;

	public Double getSCFTJZMJ() {
		return sCFTJZMJ;
	}

	public void setSCFTJZMJ(Double sCFTJZMJ) {
		if (this.sCFTJZMJ != sCFTJZMJ) {
			this.sCFTJZMJ = sCFTJZMJ;
			modify_sCFTJZMJ = true;
		}
	}

	private Double sCDXBFJZMJ;
	private boolean modify_sCDXBFJZMJ = false;

	public Double getSCDXBFJZMJ() {
		return sCDXBFJZMJ;
	}

	public void setSCDXBFJZMJ(Double sCDXBFJZMJ) {
		if (this.sCDXBFJZMJ != sCDXBFJZMJ) {
			this.sCDXBFJZMJ = sCDXBFJZMJ;
			modify_sCDXBFJZMJ = true;
		}
	}

	private Double sCQTJZMJ;
	private boolean modify_sCQTJZMJ = false;

	public Double getSCQTJZMJ() {
		return sCQTJZMJ;
	}

	public void setSCQTJZMJ(Double sCQTJZMJ) {
		if (this.sCQTJZMJ != sCQTJZMJ) {
			this.sCQTJZMJ = sCQTJZMJ;
			modify_sCQTJZMJ = true;
		}
	}

	private Double sCFTXS;
	private boolean modify_sCFTXS = false;

	public Double getSCFTXS() {
		return sCFTXS;
	}

	public void setSCFTXS(Double sCFTXS) {
		if (this.sCFTXS != sCFTXS) {
			this.sCFTXS = sCFTXS;
			modify_sCFTXS = true;
		}
	}

	private Double gYTDMJ;
	private boolean modify_gYTDMJ = false;

	public Double getGYTDMJ() {
		return gYTDMJ;
	}

	public void setGYTDMJ(Double gYTDMJ) {
		if (this.gYTDMJ != gYTDMJ) {
			this.gYTDMJ = gYTDMJ;
			modify_gYTDMJ = true;
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

	private String fWLX;
	private boolean modify_fWLX = false;

	public String getFWLX() {
		return fWLX;
	}

	public void setFWLX(String fWLX) {
		if (this.fWLX != fWLX) {
			this.fWLX = fWLX;
			modify_fWLX = true;
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

	private String fCFHT;
	private boolean modify_fCFHT = false;

	public String getFCFHT() {
		return fCFHT;
	}

	public void setFCFHT(String fCFHT) {
		if (this.fCFHT != fCFHT) {
			this.fCFHT = fCFHT;
			modify_fCFHT = true;
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

	private String cID;
	private boolean modify_cID = false;

	public String getCID() {
		return cID;
	}

	public void setCID(String cID) {
		if (this.cID != cID) {
			this.cID = cID;
			modify_cID = true;
		}
	}

	private String lJZID;
	private boolean modify_lJZID = false;

	public String getLJZID() {
		return lJZID;
	}

	public void setLJZID(String lJZID) {
		if (this.lJZID != lJZID) {
			this.lJZID = lJZID;
			modify_lJZID = true;
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

	private String gXXMBH;
	private boolean modify_gXXMBH = false;

	public String getGXXMBH() {
		return gXXMBH;
	}

	public void setGXXMBH(String gXXMBH) {
		if (this.gXXMBH != gXXMBH) {
			this.gXXMBH = gXXMBH;
			modify_gXXMBH = true;
		}
	}

	private String fWZT;
	private boolean modify_fWZT = false;

	public String getFWZT() {
		return fWZT;
	}

	public void setFWZT(String fWZT) {
		if (this.fWZT != fWZT) {
			this.fWZT = fWZT;
			modify_fWZT = true;
		}
	}

	private Integer aCTIVEID;
	private boolean modify_aCTIVEID = false;

	public Integer getACTIVEID() {
		return aCTIVEID;
	}

	public void setACTIVEID(Integer aCTIVEID) {
		if (this.aCTIVEID != aCTIVEID) {
			this.aCTIVEID = aCTIVEID;
			modify_aCTIVEID = true;
		}
	}

	private Integer sEQID_W;
	private boolean modify_sEQID_W = false;

	public Integer getSEQID_W() {
		return sEQID_W;
	}

	public void setSEQID_W(Integer sEQID_W) {
		if (this.sEQID_W != sEQID_W) {
			this.sEQID_W = sEQID_W;
			modify_sEQID_W = true;
		}
	}

	private Integer sEQID_R;
	private boolean modify_sEQID_R = false;

	public Integer getSEQID_R() {
		return sEQID_R;
	}

	public void setSEQID_R(Integer sEQID_R) {
		if (this.sEQID_R != sEQID_R) {
			this.sEQID_R = sEQID_R;
			modify_sEQID_R = true;
		}
	}

	private Date cREATEDATETIME;
	private boolean modify_cREATEDATETIME = false;

	public Date getCREATEDATETIME() {
		return cREATEDATETIME;
	}

	public void setCREATEDATETIME(Date cREATEDATETIME) {
		if (this.cREATEDATETIME != cREATEDATETIME) {
			this.cREATEDATETIME = cREATEDATETIME;
			modify_cREATEDATETIME = true;
		}
	}

	private Date lOADDATETIME;
	private boolean modify_lOADDATETIME = false;

	public Date getLOADDATETIME() {
		return lOADDATETIME;
	}

	public void setLOADDATETIME(Date lOADDATETIME) {
		if (this.lOADDATETIME != lOADDATETIME) {
			this.lOADDATETIME = lOADDATETIME;
			modify_lOADDATETIME = true;
		}
	}

	private String fWYT1_Y;
	private boolean modify_fWYT1_Y = false;

	public String getFWYT1_Y() {
		return fWYT1_Y;
	}

	public void setFWYT1_Y(String fWYT1_Y) {
		if (this.fWYT1_Y != fWYT1_Y) {
			this.fWYT1_Y = fWYT1_Y;
			modify_fWYT1_Y = true;
		}
	}

	private String hXJG_Y;
	private boolean modify_hXJG_Y = false;

	public String getHXJG_Y() {
		return hXJG_Y;
	}

	public void setHXJG_Y(String hXJG_Y) {
		if (this.hXJG_Y != hXJG_Y) {
			this.hXJG_Y = hXJG_Y;
			modify_hXJG_Y = true;
		}
	}

	private String hX_Y;
	private boolean modify_hX_Y = false;

	public String getHX_Y() {
		return hX_Y;
	}

	public void setHX_Y(String hX_Y) {
		if (this.hX_Y != hX_Y) {
			this.hX_Y = hX_Y;
			modify_hX_Y = true;
		}
	}

	private Integer zGEDYZT;
	private boolean modify_zGEDYZT = false;

	public Integer getZGEDYZT() {
		return zGEDYZT;
	}

	public void setZGEDYZT(Integer zGEDYZT) {
		if (this.zGEDYZT != zGEDYZT) {
			this.zGEDYZT = zGEDYZT;
			modify_zGEDYZT = true;
		}
	}

	private Integer zYDJZT;
	private boolean modify_zYDJZT = false;

	public Integer getZYDJZT() {
		return zYDJZT;
	}

	public void setZYDJZT(Integer zYDJZT) {
		if (this.zYDJZT != zYDJZT) {
			this.zYDJZT = zYDJZT;
			modify_zYDJZT = true;
		}
	}

	private Integer zXDJZT;
	private boolean modify_zXDJZT = false;

	public Integer getZXDJZT() {
		return zXDJZT;
	}

	public void setZXDJZT(Integer zXDJZT) {
		if (this.zXDJZT != zXDJZT) {
			this.zXDJZT = zXDJZT;
			modify_zXDJZT = true;
		}
	}

	private Integer zJGCZGEZT;
	private boolean modify_zJGCZGEZT = false;

	public Integer getZJGCZGEZT() {
		return zJGCZGEZT;
	}

	public void setZJGCZGEZT(Integer zJGCZGEZT) {
		if (this.zJGCZGEZT != zJGCZGEZT) {
			this.zJGCZGEZT = zJGCZGEZT;
			modify_zJGCZGEZT = true;
		}
	}

	private Integer xFDYZT;
	private boolean modify_xFDYZT = false;

	public Integer getXFDYZT() {
		return xFDYZT;
	}

	public void setXFDYZT(Integer xFDYZT) {
		if (this.xFDYZT != xFDYZT) {
			this.xFDYZT = xFDYZT;
			modify_xFDYZT = true;
		}
	}

	private Integer qFDYZT;
	private boolean modify_qFDYZT = false;

	public Integer getQFDYZT() {
		return qFDYZT;
	}

	public void setQFDYZT(Integer qFDYZT) {
		if (this.qFDYZT != qFDYZT) {
			this.qFDYZT = qFDYZT;
			modify_qFDYZT = true;
		}
	}

	private Integer yGSPFDYYGZT;
	private boolean modify_yGSPFDYYGZT = false;

	public Integer getYGSPFDYYGZT() {
		return yGSPFDYYGZT;
	}

	public void setYGSPFDYYGZT(Integer yGSPFDYYGZT) {
		if (this.yGSPFDYYGZT != yGSPFDYYGZT) {
			this.yGSPFDYYGZT = yGSPFDYYGZT;
			modify_yGSPFDYYGZT = true;
		}
	}

	private Integer yGSPFYGZT;
	private boolean modify_yGSPFYGZT = false;

	public Integer getYGSPFYGZT() {
		return yGSPFYGZT;
	}

	public void setYGSPFYGZT(Integer yGSPFYGZT) {
		if (this.yGSPFYGZT != yGSPFYGZT) {
			this.yGSPFYGZT = yGSPFYGZT;
			modify_yGSPFYGZT = true;
		}
	}

	private Integer ySXKZT;
	private boolean modify_ySXKZT = false;

	public Integer getYSXKZT() {
		return ySXKZT;
	}

	public void setYSXKZT(Integer ySXKZT) {
		if (this.ySXKZT != ySXKZT) {
			this.ySXKZT = ySXKZT;
			modify_ySXKZT = true;
		}
	}

	private Integer zYZT;
	private boolean modify_zYZT = false;

	public Integer getZYZT() {
		return zYZT;
	}

	public void setZYZT(Integer zYZT) {
		if (this.zYZT != zYZT) {
			this.zYZT = zYZT;
			modify_zYZT = true;
		}
	}

	private Integer xFQYZT;
	private boolean modify_xFQYZT = false;

	public Integer getXFQYZT() {
		return xFQYZT;
	}

	public void setXFQYZT(Integer xFQYZT) {
		if (this.xFQYZT != xFQYZT) {
			this.xFQYZT = xFQYZT;
			modify_xFQYZT = true;
		}
	}

	private Integer xFRGZT;
	private boolean modify_xFRGZT = false;

	public Integer getXFRGZT() {
		return xFRGZT;
	}

	public void setXFRGZT(Integer xFRGZT) {
		if (this.xFRGZT != xFRGZT) {
			this.xFRGZT = xFRGZT;
			modify_xFRGZT = true;
		}
	}

	private Integer xFXZZT;
	private boolean modify_xFXZZT = false;

	public Integer getXFXZZT() {
		return xFXZZT;
	}

	public void setXFXZZT(Integer xFXZZT) {
		if (this.xFXZZT != xFXZZT) {
			this.xFXZZT = xFXZZT;
			modify_xFXZZT = true;
		}
	}

	private Integer xSXKZT;
	private boolean modify_xSXKZT = false;

	public Integer getXSXKZT() {
		return xSXKZT;
	}

	public void setXSXKZT(Integer xSXKZT) {
		if (this.xSXKZT != xSXKZT) {
			this.xSXKZT = xSXKZT;
			modify_xSXKZT = true;
		}
	}

	private Integer zHYYGZT;
	private boolean modify_zHYYGZT = false;

	public Integer getZHYYGZT() {
		return zHYYGZT;
	}

	public void setZHYYGZT(Integer zHYYGZT) {
		if (this.zHYYGZT != zHYYGZT) {
			this.zHYYGZT = zHYYGZT;
			modify_zHYYGZT = true;
		}
	}

	private Integer qFQYZT;
	private boolean modify_qFQYZT = false;

	public Integer getQFQYZT() {
		return qFQYZT;
	}

	public void setQFQYZT(Integer qFQYZT) {
		if (this.qFQYZT != qFQYZT) {
			this.qFQYZT = qFQYZT;
			modify_qFQYZT = true;
		}
	}

	private Integer qFRGZT;
	private boolean modify_qFRGZT = false;

	public Integer getQFRGZT() {
		return qFRGZT;
	}

	public void setQFRGZT(Integer qFRGZT) {
		if (this.qFRGZT != qFRGZT) {
			this.qFRGZT = qFRGZT;
			modify_qFRGZT = true;
		}
	}

	private Integer qFXZZT;
	private boolean modify_qFXZZT = false;

	public Integer getQFXZZT() {
		return qFXZZT;
	}

	public void setQFXZZT(Integer qFXZZT) {
		if (this.qFXZZT != qFXZZT) {
			this.qFXZZT = qFXZZT;
			modify_qFXZZT = true;
		}
	}

	private Integer hTBAZT;
	private boolean modify_hTBAZT = false;

	public Integer getHTBAZT() {
		return hTBAZT;
	}

	public void setHTBAZT(Integer hTBAZT) {
		if (this.hTBAZT != hTBAZT) {
			this.hTBAZT = hTBAZT;
			modify_hTBAZT = true;
		}
	}

	private Integer dYYGZT;
	private boolean modify_dYYGZT = false;

	public Integer getDYYGZT() {
		return dYYGZT;
	}

	public void setDYYGZT(Integer dYYGZT) {
		if (this.dYYGZT != dYYGZT) {
			this.dYYGZT = dYYGZT;
			modify_dYYGZT = true;
		}
	}

	private Integer dYQZT;
	private boolean modify_dYQZT = false;

	public Integer getDYQZT() {
		return dYQZT;
	}

	public void setDYQZT(Integer dYQZT) {
		if (this.dYQZT != dYQZT) {
			this.dYQZT = dYQZT;
			modify_dYQZT = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_fWBM = false;
		modify_ySDM = false;
		modify_zRZH = false;
		modify_lJZH = false;
		modify_cH = false;
		modify_zL = false;
		modify_mJDW = false;
		modify_sJCS = false;
		modify_hH = false;
		modify_sHBW = false;
		modify_hX = false;
		modify_hXJG = false;
		modify_fWYT1 = false;
		modify_fWYT2 = false;
		modify_fWYT3 = false;
		modify_yCJZMJ = false;
		modify_yCTNJZMJ = false;
		modify_yCFTJZMJ = false;
		modify_yCDXBFJZMJ = false;
		modify_yCQTJZMJ = false;
		modify_yCFTXS = false;
		modify_sCJZMJ = false;
		modify_sCTNJZMJ = false;
		modify_sCFTJZMJ = false;
		modify_sCDXBFJZMJ = false;
		modify_sCQTJZMJ = false;
		modify_sCFTXS = false;
		modify_gYTDMJ = false;
		modify_fTTDMJ = false;
		modify_dYTDMJ = false;
		modify_fWLX = false;
		modify_fWXZ = false;
		modify_fCFHT = false;
		modify_zT = false;
		modify_bDCDYID = false;
		modify_cID = false;
		modify_lJZID = false;
		modify_zRZBDCDYID = false;
		modify_gXXMBH = false;
		modify_fWZT = false;
		modify_aCTIVEID = false;
		modify_sEQID_W = false;
		modify_sEQID_R = false;
		modify_cREATEDATETIME = false;
		modify_lOADDATETIME = false;
		modify_fWYT1_Y = false;
		modify_hXJG_Y = false;
		modify_hX_Y = false;
		modify_zGEDYZT = false;
		modify_zYDJZT = false;
		modify_zXDJZT = false;
		modify_zJGCZGEZT = false;
		modify_xFDYZT = false;
		modify_qFDYZT = false;
		modify_yGSPFDYYGZT = false;
		modify_yGSPFYGZT = false;
		modify_ySXKZT = false;
		modify_zYZT = false;
		modify_xFQYZT = false;
		modify_xFRGZT = false;
		modify_xFXZZT = false;
		modify_xSXKZT = false;
		modify_zHYYGZT = false;
		modify_qFQYZT = false;
		modify_qFRGZT = false;
		modify_qFXZZT = false;
		modify_hTBAZT = false;
		modify_dYYGZT = false;
		modify_dYQZT = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_fWBM)
			listStrings.add("fWBM");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_zRZH)
			listStrings.add("zRZH");
		if (!modify_lJZH)
			listStrings.add("lJZH");
		if (!modify_cH)
			listStrings.add("cH");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_mJDW)
			listStrings.add("mJDW");
		if (!modify_sJCS)
			listStrings.add("sJCS");
		if (!modify_hH)
			listStrings.add("hH");
		if (!modify_sHBW)
			listStrings.add("sHBW");
		if (!modify_hX)
			listStrings.add("hX");
		if (!modify_hXJG)
			listStrings.add("hXJG");
		if (!modify_fWYT1)
			listStrings.add("fWYT1");
		if (!modify_fWYT2)
			listStrings.add("fWYT2");
		if (!modify_fWYT3)
			listStrings.add("fWYT3");
		if (!modify_yCJZMJ)
			listStrings.add("yCJZMJ");
		if (!modify_yCTNJZMJ)
			listStrings.add("yCTNJZMJ");
		if (!modify_yCFTJZMJ)
			listStrings.add("yCFTJZMJ");
		if (!modify_yCDXBFJZMJ)
			listStrings.add("yCDXBFJZMJ");
		if (!modify_yCQTJZMJ)
			listStrings.add("yCQTJZMJ");
		if (!modify_yCFTXS)
			listStrings.add("yCFTXS");
		if (!modify_sCJZMJ)
			listStrings.add("sCJZMJ");
		if (!modify_sCTNJZMJ)
			listStrings.add("sCTNJZMJ");
		if (!modify_sCFTJZMJ)
			listStrings.add("sCFTJZMJ");
		if (!modify_sCDXBFJZMJ)
			listStrings.add("sCDXBFJZMJ");
		if (!modify_sCQTJZMJ)
			listStrings.add("sCQTJZMJ");
		if (!modify_sCFTXS)
			listStrings.add("sCFTXS");
		if (!modify_gYTDMJ)
			listStrings.add("gYTDMJ");
		if (!modify_fTTDMJ)
			listStrings.add("fTTDMJ");
		if (!modify_dYTDMJ)
			listStrings.add("dYTDMJ");
		if (!modify_fWLX)
			listStrings.add("fWLX");
		if (!modify_fWXZ)
			listStrings.add("fWXZ");
		if (!modify_fCFHT)
			listStrings.add("fCFHT");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_cID)
			listStrings.add("cID");
		if (!modify_lJZID)
			listStrings.add("lJZID");
		if (!modify_zRZBDCDYID)
			listStrings.add("zRZBDCDYID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_fWZT)
			listStrings.add("fWZT");
		if (!modify_aCTIVEID)
			listStrings.add("aCTIVEID");
		if (!modify_sEQID_W)
			listStrings.add("sEQID_W");
		if (!modify_sEQID_R)
			listStrings.add("sEQID_R");
		if (!modify_cREATEDATETIME)
			listStrings.add("cREATEDATETIME");
		if (!modify_lOADDATETIME)
			listStrings.add("lOADDATETIME");
		if (!modify_fWYT1_Y)
			listStrings.add("fWYT1_Y");
		if (!modify_hXJG_Y)
			listStrings.add("hXJG_Y");
		if (!modify_hX_Y)
			listStrings.add("hX_Y");
		if (!modify_zGEDYZT)
			listStrings.add("zGEDYZT");
		if (!modify_zYDJZT)
			listStrings.add("zYDJZT");
		if (!modify_zXDJZT)
			listStrings.add("zXDJZT");
		if (!modify_zJGCZGEZT)
			listStrings.add("zJGCZGEZT");
		if (!modify_xFDYZT)
			listStrings.add("xFDYZT");
		if (!modify_qFDYZT)
			listStrings.add("qFDYZT");
		if (!modify_yGSPFDYYGZT)
			listStrings.add("yGSPFDYYGZT");
		if (!modify_yGSPFYGZT)
			listStrings.add("yGSPFYGZT");
		if (!modify_ySXKZT)
			listStrings.add("ySXKZT");
		if (!modify_zYZT)
			listStrings.add("zYZT");
		if (!modify_xFQYZT)
			listStrings.add("xFQYZT");
		if (!modify_xFRGZT)
			listStrings.add("xFRGZT");
		if (!modify_xFXZZT)
			listStrings.add("xFXZZT");
		if (!modify_xSXKZT)
			listStrings.add("xSXKZT");
		if (!modify_zHYYGZT)
			listStrings.add("zHYYGZT");
		if (!modify_qFQYZT)
			listStrings.add("qFQYZT");
		if (!modify_qFRGZT)
			listStrings.add("qFRGZT");
		if (!modify_qFXZZT)
			listStrings.add("qFXZZT");
		if (!modify_hTBAZT)
			listStrings.add("hTBAZT");
		if (!modify_dYYGZT)
			listStrings.add("dYYGZT");
		if (!modify_dYQZT)
			listStrings.add("dYQZT");

		return StringHelper.ListToStrings(listStrings);	
	}
}
