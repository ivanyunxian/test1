package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity bgqh 
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



public class GenerateBGQH implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;


	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}


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


	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_ySDM = false;
		modify_bDCDYH = false;
		modify_fWBM = false;
		modify_zRZBDCDYID = false;
		modify_zRZH = false;
		modify_lJZID = false;
		modify_lJZH = false;
		modify_cID = false;
		modify_cH = false;
		modify_zL = false;
		modify_mJDW = false;
		modify_sJCS = false;
		modify_sZC = false;
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
		modify_gHYT = false;
		modify_fWJG = false;
		modify_fWLX = false;
		modify_fWXZ = false;
		modify_fCFHT = false;
		modify_zT = false;
		modify_xMMC = false;
		modify_rELATIONID = false;
		modify_gXXMBH = false;
		modify_fWZT = false;
		modify_zCS = false;
		modify_jGSJ = false;
		modify_dYH = false;
		modify_fH = false;
		modify_zDBDCDYID = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_fWBM)
			listStrings.add("fWBM");
		if (!modify_zRZBDCDYID)
			listStrings.add("zRZBDCDYID");
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
		if (!modify_sJCS)
			listStrings.add("sJCS");
		if (!modify_sZC)
			listStrings.add("sZC");
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
		if (!modify_gHYT)
			listStrings.add("gHYT");
		if (!modify_fWJG)
			listStrings.add("fWJG");
		if (!modify_fWLX)
			listStrings.add("fWLX");
		if (!modify_fWXZ)
			listStrings.add("fWXZ");
		if (!modify_fCFHT)
			listStrings.add("fCFHT");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_fWZT)
			listStrings.add("fWZT");
		if (!modify_zCS)
			listStrings.add("zCS");
		if (!modify_jGSJ)
			listStrings.add("jGSJ");
		if (!modify_dYH)
			listStrings.add("dYH");
		if (!modify_fH)
			listStrings.add("fH");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");

		return StringHelper.ListToStrings(listStrings);
	}
}
