package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Internal Entity bdcs_ljz_lsy 
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

public class GenerateBDCS_LJZ_LSY implements SuperModel<String> {

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

	private String mPH;
	private boolean modify_mPH = false;

	public String getMPH() {
		return mPH;
	}

	public void setMPH(String mPH) {
		if (this.mPH != mPH) {
			this.mPH = mPH;
			modify_mPH = true;
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

	private Double yCDXMJ;
	private boolean modify_yCDXMJ = false;

	public Double getYCDXMJ() {
		return yCDXMJ;
	}

	public void setYCDXMJ(Double yCDXMJ) {
		if (this.yCDXMJ != yCDXMJ) {
			this.yCDXMJ = yCDXMJ;
			modify_yCDXMJ = true;
		}
	}

	private Double yCQTMJ;
	private boolean modify_yCQTMJ = false;

	public Double getYCQTMJ() {
		return yCQTMJ;
	}

	public void setYCQTMJ(Double yCQTMJ) {
		if (this.yCQTMJ != yCQTMJ) {
			this.yCQTMJ = yCQTMJ;
			modify_yCQTMJ = true;
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

	private Double sCDXMJ;
	private boolean modify_sCDXMJ = false;

	public Double getSCDXMJ() {
		return sCDXMJ;
	}

	public void setSCDXMJ(Double sCDXMJ) {
		if (this.sCDXMJ != sCDXMJ) {
			this.sCDXMJ = sCDXMJ;
			modify_sCDXMJ = true;
		}
	}

	private Double sCQTMJ;
	private boolean modify_sCQTMJ = false;

	public Double getSCQTMJ() {
		return sCQTMJ;
	}

	public void setSCQTMJ(Double sCQTMJ) {
		if (this.sCQTMJ != sCQTMJ) {
			this.sCQTMJ = sCQTMJ;
			modify_sCQTMJ = true;
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

	private String jZWZT;
	private boolean modify_jZWZT = false;

	public String getJZWZT() {
		return jZWZT;
	}

	public void setJZWZT(String jZWZT) {
		if (this.jZWZT != jZWZT) {
			this.jZWZT = jZWZT;
			modify_jZWZT = true;
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

	private Integer dSCS;
	private boolean modify_dSCS = false;

	public Integer getDSCS() {
		return dSCS;
	}

	public void setDSCS(Integer dSCS) {
		if (this.dSCS != dSCS) {
			this.dSCS = dSCS;
			modify_dSCS = true;
		}
	}

	private Integer dXCS;
	private boolean modify_dXCS = false;

	public Integer getDXCS() {
		return dXCS;
	}

	public void setDXCS(Integer dXCS) {
		if (this.dXCS != dXCS) {
			this.dXCS = dXCS;
			modify_dXCS = true;
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

	@Override
	public void resetModifyState() {
		modify_rELATIONID = false;
		modify_id = false;
		modify_ySDM = false;
		modify_xMBH = false;
		modify_lJZH = false;
		modify_zRZBDCDYID = false;
		modify_zRZH = false;
		modify_mPH = false;
		modify_yCJZMJ = false;
		modify_yCDXMJ = false;
		modify_yCQTMJ = false;
		modify_sCJZMJ = false;
		modify_sCDXMJ = false;
		modify_sCQTMJ = false;
		modify_fWJG1 = false;
		modify_fWJG2 = false;
		modify_fWJG3 = false;
		modify_jZWZT = false;
		modify_fWYT1 = false;
		modify_fWYT2 = false;
		modify_fWYT3 = false;
		modify_zCS = false;
		modify_dSCS = false;
		modify_dXCS = false;
		modify_bZ = false;
		modify_dCXMID = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_id)
			listStrings.add("id");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_lJZH)
			listStrings.add("lJZH");
		if (!modify_zRZBDCDYID)
			listStrings.add("zRZBDCDYID");
		if (!modify_zRZH)
			listStrings.add("zRZH");
		if (!modify_mPH)
			listStrings.add("mPH");
		if (!modify_yCJZMJ)
			listStrings.add("yCJZMJ");
		if (!modify_yCDXMJ)
			listStrings.add("yCDXMJ");
		if (!modify_yCQTMJ)
			listStrings.add("yCQTMJ");
		if (!modify_sCJZMJ)
			listStrings.add("sCJZMJ");
		if (!modify_sCDXMJ)
			listStrings.add("sCDXMJ");
		if (!modify_sCQTMJ)
			listStrings.add("sCQTMJ");
		if (!modify_fWJG1)
			listStrings.add("fWJG1");
		if (!modify_fWJG2)
			listStrings.add("fWJG2");
		if (!modify_fWJG3)
			listStrings.add("fWJG3");
		if (!modify_jZWZT)
			listStrings.add("jZWZT");
		if (!modify_fWYT1)
			listStrings.add("fWYT1");
		if (!modify_fWYT2)
			listStrings.add("fWYT2");
		if (!modify_fWYT3)
			listStrings.add("fWYT3");
		if (!modify_zCS)
			listStrings.add("zCS");
		if (!modify_dSCS)
			listStrings.add("dSCS");
		if (!modify_dXCS)
			listStrings.add("dXCS");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
