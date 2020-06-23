package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-12 
//* ----------------------------------------
//* Internal Entity bdcs_h_gz 
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

public class GenerateDCS_H_GZ implements SuperModel<String> {

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

	private String zDBDCDYID;
	private boolean modify_zDBDCDYID = false;

	public String getZDBDCDYID() {
		return zDBDCDYID;
	}

	public void setZDBDCDYID(String zDBDCDYID) {
		if (this.zDBDCDYID != zDBDCDYID) {
			this.zDBDCDYID = zDBDCDYID;
			modify_zDBDCDYID = true;
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

	private String sZC;
	private boolean modify_sZC = false;

	public String getSZC() {
		return sZC;
	}

	public void setSZC(String sZC) {
		if (this.sZC != sZC) {
			this.sZC = sZC;
			modify_sZC = true;
		}
	}
	
	private Double qSC;
	private boolean modify_qSC = false;

	public Double getQSC() {
		return qSC;
	}

	public void setQSC(Double qSC) {
		if (this.qSC != qSC) {
			this.qSC = qSC;
			modify_qSC = true;
		}
	}
	
	private Double zZC;
	private boolean modify_zZC = false;

	public Double getZZC() {
		return zZC;
	}

	public void setZZC(Double zZC) {
		if (this.zZC != zZC) {
			this.zZC = zZC;
			modify_zZC = true;
		}
	}

	private String dYH;
	private boolean modify_dYH = false;

	public String getDYH() {
		return dYH;
	}

	public void setDYH(String dYH) {
		if (this.dYH != dYH) {
			this.dYH = dYH;
			modify_dYH = true;
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
	
	private String fWJG1;
	private boolean modify_fWJG1 = false;

	public String getFWJG1() {
		return fWJG1;
	}

	public void setFWJG1(String fWJG1) {
		if (this.fWJG1 != fWJG1) {
			this.fWJG1 = fWJG1;
			modify_fWJG1 = true;
		}
	}
	
	private String fWJG2;
	private boolean modify_fWJG2 = false;

	public String getFWJG2() {
		return fWJG2;
	}

	public void setFWJG2(String fWJG2) {
		if (this.fWJG2 != fWJG2) {
			this.fWJG2 = fWJG2;
			modify_fWJG2 = true;
		}
	}
	
	private String fWJG3;
	private boolean modify_fWJG3 = false;

	public String getFWJG3() {
		return fWJG3;
	}

	public void setFWJG3(String fWJG3) {
		if (this.fWJG3 != fWJG3) {
			this.fWJG3 = fWJG3;
			modify_fWJG3 = true;
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

	private Double sYMJ;
	private boolean modify_sYMJ = false;

	public Double getSYMJ() {
		return sYMJ;
	}

	public void setSYMJ(Double sYMJ) {
		if (this.sYMJ != sYMJ) {
			this.sYMJ = sYMJ;
			modify_sYMJ = true;
		}
	}

	private String cQLY;
	private boolean modify_cQLY = false;

	public String getCQLY() {
		return cQLY;
	}

	public void setCQLY(String cQLY) {
		if (this.cQLY != cQLY) {
			this.cQLY = cQLY;
			modify_cQLY = true;
		}
	}

	private String qTGSD;
	private boolean modify_qTGSD = false;

	public String getQTGSD() {
		return qTGSD;
	}

	public void setQTGSD(String qTGSD) {
		if (this.qTGSD != qTGSD) {
			this.qTGSD = qTGSD;
			modify_qTGSD = true;
		}
	}

	private String qTGSX;
	private boolean modify_qTGSX = false;

	public String getQTGSX() {
		return qTGSX;
	}

	public void setQTGSX(String qTGSX) {
		if (this.qTGSX != qTGSX) {
			this.qTGSX = qTGSX;
			modify_qTGSX = true;
		}
	}

	private String qTGSN;
	private boolean modify_qTGSN = false;

	public String getQTGSN() {
		return qTGSN;
	}

	public void setQTGSN(String qTGSN) {
		if (this.qTGSN != qTGSN) {
			this.qTGSN = qTGSN;
			modify_qTGSN = true;
		}
	}

	private String qTGSB;
	private boolean modify_qTGSB = false;

	public String getQTGSB() {
		return qTGSB;
	}

	public void setQTGSB(String qTGSB) {
		if (this.qTGSB != qTGSB) {
			this.qTGSB = qTGSB;
			modify_qTGSB = true;
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

	private Integer zT;
	private boolean modify_zT = false;

	public Integer getZT() {
		return zT;
	}

	public void setZT(Integer zT) {
		if (this.zT != zT) {
			this.zT = zT;
			modify_zT = true;
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

	private String fH;
	private boolean modify_fH = false;

	public String getFH() {
		return fH;
	}

	public void setFH(String fH) {
		if (this.fH != fH) {
			this.fH = fH;
			modify_fH = true;
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

	private String bGZT;
	private boolean modify_bGZT = false;

	public String getBGZT() {
		return bGZT;
	}

	public void setBGZT(String bGZT) {
		if (this.bGZT != bGZT) {
			this.bGZT = bGZT;
			modify_bGZT = true;
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

	private String yFWJG;
	private boolean modify_yFWJG = false;

	public String getYFWJG() {
		return yFWJG;
	}

	public void setYFWJG(String yFWJG) {
		if (this.yFWJG != yFWJG) {
			this.yFWJG = yFWJG;
			modify_yFWJG = true;
		}
	}

	private String yFWXZ;
	private boolean modify_yFWXZ = false;

	public String getYFWXZ() {
		return yFWXZ;
	}

	public void setYFWXZ(String yFWXZ) {
		if (this.yFWXZ != yFWXZ) {
			this.yFWXZ = yFWXZ;
			modify_yFWXZ = true;
		}
	}

	private String yFWYT;
	private boolean modify_yFWYT = false;

	public String getYFWYT() {
		return yFWYT;
	}

	public void setYFWYT(String yFWYT) {
		if (this.yFWYT != yFWYT) {
			this.yFWYT = yFWYT;
			modify_yFWYT = true;
		}
	}

	private String yGHYT;
	private boolean modify_yGHYT = false;

	public String getYGHYT() {
		return yGHYT;
	}

	public void setYGHYT(String yGHYT) {
		if (this.yGHYT != yGHYT) {
			this.yGHYT = yGHYT;
			modify_yGHYT = true;
		}
	}

	private String yZL;
	private boolean modify_yZL = false;

	public String getYZL() {
		return yZL;
	}

	public void setYZL(String yZL) {
		if (this.yZL != yZL) {
			this.yZL = yZL;
			modify_yZL = true;
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

	private String fWCB;
	private boolean modify_fWCB = false;

	public String getFWCB() {
		return fWCB;
	}

	public void setFWCB(String fWCB) {
		if (this.fWCB != fWCB) {
			this.fWCB = fWCB;
			modify_fWCB = true;
		}
	}

	private String yFWCB;
	private boolean modify_yFWCB = false;

	public String getYFWCB() {
		return yFWCB;
	}

	public void setYFWCB(String yFWCB) {
		if (this.yFWCB != yFWCB) {
			this.yFWCB = yFWCB;
			modify_yFWCB = true;
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

	private String xMZL;
	private boolean modify_xMZL = false;

	public String getXMZL() {
		return xMZL;
	}

	public void setXMZL(String xMZL) {
		if (this.xMZL != xMZL) {
			this.xMZL = xMZL;
			modify_xMZL = true;
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

	private String fCFHTSLTX;
	private boolean modify_fCFHTSLTX = false;

	public String getFCFHTSLTX() {
		return fCFHTSLTX;
	}

	public void setFCFHTSLTX(String fCFHTSLTX) {
		if (this.fCFHTSLTX != fCFHTSLTX) {
			this.fCFHTSLTX = fCFHTSLTX;
			modify_fCFHTSLTX = true;
		}
	}

	private Integer sFLJQPJYC;
	private boolean modify_sFLJQPJYC = false;

	public Integer getSFLJQPJYC() {
		return sFLJQPJYC;
	}

	public void setSFLJQPJYC(Integer sFLJQPJYC) {
		if (this.sFLJQPJYC != sFLJQPJYC) {
			this.sFLJQPJYC = sFLJQPJYC;
			modify_sFLJQPJYC = true;
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

	private Date tDSYQQSRQ;
	private boolean modify_tDSYQQSRQ = false;

	public Date getTDSYQQSRQ() {
		return tDSYQQSRQ;
	}

	public void setTDSYQQSRQ(Date tDSYQQSRQ) {
		if (this.tDSYQQSRQ != tDSYQQSRQ) {
			this.tDSYQQSRQ = tDSYQQSRQ;
			modify_tDSYQQSRQ = true;
		}
	}

	private Date tDSYQZZRQ;
	private boolean modify_tDSYQZZRQ = false;

	public Date getTDSYQZZRQ() {
		return tDSYQZZRQ;
	}

	public void setTDSYQZZRQ(Date tDSYQZZRQ) {
		if (this.tDSYQZZRQ != tDSYQZZRQ) {
			this.tDSYQZZRQ = tDSYQZZRQ;
			modify_tDSYQZZRQ = true;
		}
	}

	private Integer tDSYNX;
	private boolean modify_tDSYNX = false;

	public Integer getTDSYNX() {
		return tDSYNX;
	}

	public void setTDSYNX(Integer tDSYNX) {
		if (this.tDSYNX != tDSYNX) {
			this.tDSYNX = tDSYNX;
			modify_tDSYNX = true;
		}
	}

	private String fWTDYT;
	private boolean modify_fWTDYT = false;

	public String getFWTDYT() {
		return fWTDYT;
	}

	public void setFWTDYT(String fWTDYT) {
		if (this.fWTDYT != fWTDYT) {
			this.fWTDYT = fWTDYT;
			modify_fWTDYT = true;
		}
	}
	/**
	 * 新不动产单元号 
	 */
	private String nBDCDYH;
	private boolean modify_nBDCDYH=false;
	public String getNBDCDYH(){
		return nBDCDYH;
	}
	public void setNBDCDYH(String nBDCDYH){
		if(this.nBDCDYH !=nBDCDYH){
			this.nBDCDYH=nBDCDYH;
			modify_nBDCDYH=true;
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
	
    private String mARKERZT;
	private boolean modify_mARKERZT = false;

	public String getMARKERZT() {
		return mARKERZT;
	}

	public void setMARKERZT(String mARKERZT) {
		if (this.mARKERZT != mARKERZT) {
			this.mARKERZT = mARKERZT;
			modify_mARKERZT = true;
		}
	}
	
	  private String mARKERZTMC;
	private boolean modify_mARKERZTMC = false;

	public String getMARKERZTMC() {
		return mARKERZTMC;
	}

	public void setMARKERZTMC(String mARKERZTMC) {
		if (this.mARKERZTMC != mARKERZTMC) {
			this.mARKERZTMC = mARKERZTMC;
			modify_mARKERZTMC = true;
		}
	}
	
	  private String mARKERSM;
	private boolean modify_mARKERSM = false;

	public String getMARKERSM() {
		return mARKERSM;
	}

	public void setMARKERSM(String mARKERSM) {
		if (this.mARKERSM != mARKERSM) {
			this.mARKERSM = mARKERSM;
			modify_mARKERSM = true;
		}
	}
	
	private Date mARKERTIME;
	private boolean modify_mARKERTIME = false;

	public Date getMARKERTIME() {
		return mARKERTIME;
	}

	public void setMARKERTIME(Date mARKERTIME) {
		if (this.mARKERTIME != mARKERTIME) {
			this.mARKERTIME = mARKERTIME;
			modify_mARKERTIME = true;
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
	
	private String bz;
	private boolean modify_bz = false;
	
	public String getBZ() {
		return bz;
	}

	public void setBZ(String bz) {
		if (this.bz != bz) {
			this.bz = bz;
			modify_bz = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_ySDM = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_fWBM = false;
		modify_zRZBDCDYID = false;
		modify_zDDM = false;
		modify_zDBDCDYID = false;
		modify_zRZH = false;
		modify_lJZID = false;
		modify_lJZH = false;
		modify_cID = false;
		modify_cH = false;
		modify_zL = false;
		modify_mJDW = false;
		modify_sZC = false;
		modify_qSC = false;
		modify_zZC = false;
		modify_dYH = false;
		modify_zCS = false;
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
		modify_tDSYQR = false;
		modify_fDCJYJG = false;
		modify_gHYT = false;
		modify_fWJG = false;
		modify_fWJG1 = false;
		modify_fWJG2 = false;
		modify_fWJG3 = false;
		modify_jGSJ = false;
		modify_fWLX = false;
		modify_fWXZ = false;
		modify_zDMJ = false;
		modify_sYMJ = false;
		modify_cQLY = false;
		modify_qTGSD = false;
		modify_qTGSX = false;
		modify_qTGSN = false;
		modify_qTGSB = false;
		modify_fCFHT = false;
		modify_zT = false;
		modify_qXDM = false;
		modify_qXMC = false;
		modify_dJQDM = false;
		modify_dJQMC = false;
		modify_dJZQDM = false;
		modify_dJZQMC = false;
		modify_yXBZ = false;
		modify_cQZT = false;
		modify_dYZT = false;
		modify_xZZT = false;
		modify_bLZT = false;
		modify_yYZT = false;
		modify_dCXMID = false;
		modify_fH = false;
		modify_dJZT = false;
		modify_bGZT = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_yFWJG = false;
		modify_yFWXZ = false;
		modify_yFWYT = false;
		modify_yGHYT = false;
		modify_yZL = false;
		modify_xMMC = false;
		modify_fWCB = false;
		modify_yFWCB = false;
		modify_gZWLX = false;
		modify_pACTNO = false;
		modify_xMZL = false;
		modify_rELATIONID = false;
		modify_fCFHTSLTX = false;
		modify_sFLJQPJYC = false;
		modify_qLXZ = false;
		modify_tDSYQQSRQ = false;
		modify_tDSYQZZRQ = false;
		modify_tDSYNX = false;
		modify_fWTDYT = false;
		modify_nBDCDYH=false;
		modify_yZB = false;
		modify_xZB = false;
		modify_mARKERZT = false;
		modify_mARKERZTMC = false;
		modify_mARKERSM=false;	
		modify_mARKERTIME = false;
		modify_sEARCHSTATE = false;
		modify_bz = false;
		modify_mSR = false;
		modify_hTH = false;
	}

	
	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_fWBM)
			listStrings.add("fWBM");
		if (!modify_zRZBDCDYID)
			listStrings.add("zRZBDCDYID");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");
		if (!modify_zRZH)
			listStrings.add("zRZH");
		if (!modify_lJZID)
			listStrings.add("lJZID");
		if (!modify_lJZH)
			listStrings.add("lJZH");
		if (!modify_cID)
			listStrings.add("cID");
		if (!modify_cH)
			listStrings.add("cH");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_mJDW)
			listStrings.add("mJDW");
		if (!modify_qSC)
			listStrings.add("qSC");
		if (!modify_sZC)
			listStrings.add("sZC");
		if (!modify_zZC)
			listStrings.add("zZC");
		if (!modify_dYH)
			listStrings.add("dYH");
		if (!modify_zCS)
			listStrings.add("zCS");
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
		if (!modify_tDSYQR)
			listStrings.add("tDSYQR");
		if (!modify_fDCJYJG)
			listStrings.add("fDCJYJG");
		if (!modify_gHYT)
			listStrings.add("gHYT");
		if (!modify_fWJG)
			listStrings.add("fWJG");
		if (!modify_fWJG1)
			listStrings.add("fWJG1");
		if (!modify_fWJG2)
			listStrings.add("fWJG2");
		if (!modify_fWJG3)
			listStrings.add("fWJG3");
		if (!modify_jGSJ)
			listStrings.add("jGSJ");
		if (!modify_fWLX)
			listStrings.add("fWLX");
		if (!modify_fWXZ)
			listStrings.add("fWXZ");
		if (!modify_zDMJ)
			listStrings.add("zDMJ");
		if (!modify_sYMJ)
			listStrings.add("sYMJ");
		if (!modify_cQLY)
			listStrings.add("cQLY");
		if (!modify_qTGSD)
			listStrings.add("qTGSD");
		if (!modify_qTGSX)
			listStrings.add("qTGSX");
		if (!modify_qTGSN)
			listStrings.add("qTGSN");
		if (!modify_qTGSB)
			listStrings.add("qTGSB");
		if (!modify_fCFHT)
			listStrings.add("fCFHT");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_qXMC)
			listStrings.add("qXMC");
		if (!modify_dJQDM)
			listStrings.add("dJQDM");
		if (!modify_dJQMC)
			listStrings.add("dJQMC");
		if (!modify_dJZQDM)
			listStrings.add("dJZQDM");
		if (!modify_dJZQMC)
			listStrings.add("dJZQMC");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
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
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_fH)
			listStrings.add("fH");
		if (!modify_dJZT)
			listStrings.add("dJZT");
		if (!modify_bGZT)
			listStrings.add("bGZT");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_yFWJG)
			listStrings.add("yFWJG");
		if (!modify_yFWXZ)
			listStrings.add("yFWXZ");
		if (!modify_yFWYT)
			listStrings.add("yFWYT");
		if (!modify_yGHYT)
			listStrings.add("yGHYT");
		if (!modify_yZL)
			listStrings.add("yZL");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_fWCB)
			listStrings.add("fWCB");
		if (!modify_yFWCB)
			listStrings.add("yFWCB");
		if (!modify_gZWLX)
			listStrings.add("gZWLX");
		if (!modify_pACTNO)
			listStrings.add("pACTNO");
		if (!modify_xMZL)
			listStrings.add("xMZL");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_fCFHTSLTX)
			listStrings.add("fCFHTSLTX");
		if (!modify_sFLJQPJYC)
			listStrings.add("sFLJQPJYC");
		if (!modify_qLXZ)
			listStrings.add("qLXZ");
		if (!modify_tDSYQQSRQ)
			listStrings.add("tDSYQQSRQ");
		if (!modify_tDSYQZZRQ)
			listStrings.add("tDSYQZZRQ");
		if (!modify_tDSYNX)
			listStrings.add("tDSYNX");
		if (!modify_fWTDYT)
			listStrings.add("fWTDYT");
		if(!modify_nBDCDYH)
			listStrings.add("NBDCDYH");	
		if (!modify_yZB)
			listStrings.add("yZB");
		if (!modify_xZB)
			listStrings.add("xZB");
		if (!modify_mARKERZT)
			listStrings.add("mARKERZT");
		if(!modify_mARKERZTMC)
			listStrings.add("mARKERZTMC");
		if(!modify_mARKERSM)
			listStrings.add("mARKERSM");
		if(!modify_mARKERTIME)
			listStrings.add("mARKERTIME");
		if(!modify_sEARCHSTATE)
			listStrings.add("sEARCHSTATE");
		if(!modify_bz)
			listStrings.add("bz");
		if (!modify_mSR)
			listStrings.add("mSR");
		if (!modify_hTH)
			listStrings.add("hTH");
		return StringHelper.ListToStrings(listStrings);
	}
}
