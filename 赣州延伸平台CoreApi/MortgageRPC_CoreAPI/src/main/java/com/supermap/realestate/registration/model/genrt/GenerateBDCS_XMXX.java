package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-26 
//* ----------------------------------------
//* Internal Entity bdcs_xmxx 
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

public class GenerateBDCS_XMXX implements SuperModel<String> {

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

	private String pROJECT_ID;
	private boolean modify_pROJECT_ID = false;

	public String getPROJECT_ID() {
		return pROJECT_ID;
	}

	public void setPROJECT_ID(String pROJECT_ID) {
		if (this.pROJECT_ID != pROJECT_ID) {
			this.pROJECT_ID = pROJECT_ID;
			modify_pROJECT_ID = true;
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

	private String sLLX1;
	private boolean modify_sLLX1 = false;

	public String getSLLX1() {
		return sLLX1;
	}

	public void setSLLX1(String sLLX1) {
		if (this.sLLX1 != sLLX1) {
			this.sLLX1 = sLLX1;
			modify_sLLX1 = true;
		}
	}

	private String sLLX2;
	private boolean modify_sLLX2 = false;

	public String getSLLX2() {
		return sLLX2;
	}

	public void setSLLX2(String sLLX2) {
		if (this.sLLX2 != sLLX2) {
			this.sLLX2 = sLLX2;
			modify_sLLX2 = true;
		}
	}

	private String sLRY;
	private boolean modify_sLRY = false;

	public String getSLRY() {
		return sLRY;
	}

	public void setSLRY(String sLRY) {
		if (this.sLRY != sLRY) {
			this.sLRY = sLRY;
			modify_sLRY = true;
		}
	}

	private String sLRYID;
	private boolean modify_sLRYID = false;

	public String getSLRYID() {
		return sLRYID;
	}

	public void setSLRYID(String sLRYID) {
		if (this.sLRYID != sLRYID) {
			this.sLRYID = sLRYID;
			modify_sLRYID = true;
		}
	}

	private Date sLSJ;
	private boolean modify_sLSJ = false;

	public Date getSLSJ() {
		return sLSJ;
	}

	public void setSLSJ(Date sLSJ) {
		if (this.sLSJ != sLSJ) {
			this.sLSJ = sLSJ;
			modify_sLSJ = true;
		}
	}

	private String xMQX;
	private boolean modify_xMQX = false;

	public String getXMQX() {
		return xMQX;
	}

	public void setXMQX(String xMQX) {
		if (this.xMQX != xMQX) {
			this.xMQX = xMQX;
			modify_xMQX = true;
		}
	}

	private String sFDB;
	private boolean modify_sFDB = false;

	public String getSFDB() {
		return sFDB;
	}

	public void setSFDB(String sFDB) {
		if (this.sFDB != sFDB) {
			this.sFDB = sFDB;
			modify_sFDB = true;
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

	private String sFHBZS;
	private boolean modify_sFHBZS = false;

	public String getSFHBZS() {
		return sFHBZS;
	}

	public void setSFHBZS(String sFHBZS) {
		if (this.sFHBZS != sFHBZS) {
			this.sFHBZS = sFHBZS;
			modify_sFHBZS = true;
		}
	}

	private String yWLSH;
	private boolean modify_yWLSH = false;

	public String getYWLSH() {
		return yWLSH;
	}

	public void setYWLSH(String yWLSH) {
		if (this.yWLSH != yWLSH) {
			this.yWLSH = yWLSH;
			modify_yWLSH = true;
		}
	}

	private String sFBJ;
	private boolean modify_sFBJ = false;

	public String getSFBJ() {
		return sFBJ;
	}

	public void setSFBJ(String sFBJ) {
		if (this.sFBJ != sFBJ) {
			this.sFBJ = sFBJ;
			modify_sFBJ = true;
		}
	}

	private Integer dAGH;
	private boolean modify_dAGH = false;

	public Integer getDAGH() {
		return dAGH;
	}

	public void setDAGH(Integer dAGH) {
		if (this.dAGH != dAGH) {
			this.dAGH = dAGH;
			modify_dAGH = true;
		}
	}
	
	private String sFSB;
	private boolean modify_sFSB = false;

	public String getSFSB() {
		return sFSB;
	}

	public void setSFSB(String sFSB) {
		if (this.sFSB != sFSB) {
			this.sFSB = sFSB;
			modify_sFSB = true;
		}
	}

	private Integer tSCS;
	private boolean modify_tSCS = false;

	public Integer getTSCS() {
		return tSCS;
	}

	public void setTSCS(Integer tSCS) {
		if (this.tSCS != tSCS) {
			this.tSCS = tSCS;
			modify_tSCS = true;
		}
	}
	
	private String aLTERDATA;
	
	/** 
	 * @return aLTERDATA 
	 */
	public String getALTERDATA() {
		return aLTERDATA;
	}

	/** 
	 * @param aLTERDATA 要设置的 aLTERDATA 
	 */
	public void setALTERDATA(String aLTERDATA) {
		this.aLTERDATA = aLTERDATA;
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
	
	private String fCYWH;
	private boolean modify_fCYWH = false;

	public String getFCYWH() {
		return fCYWH;
	}

	public void setFCYWH(String fCYWH) {
		if (this.fCYWH != fCYWH) {
			this.fCYWH = fCYWH;
			modify_fCYWH = true;
		}
	}
	
	private String FCJYZT;
	private boolean modify_FCJYZT = false;

	public String getFCJYZT() {
		return FCJYZT;
	}

	public void setFCJYZT(String fCYWH) {
		if (this.FCJYZT != fCYWH) {
			this.FCJYZT = fCYWH;
			modify_FCJYZT = true;
		}
	}
	
	
	private Boolean ZDBTN;

	@Transient
	public Boolean getZDBTN() {
		return ZDBTN;
	}
	
	@Transient
	public void setZDBTN(Boolean ZDBTN) {
		this.ZDBTN = ZDBTN;
	}

	private String sFFBGG;
	private boolean modify_sFFBGG = false;

	public String getSFFBGG() {
		return sFFBGG;
	}

	public void setSFFBGG(String sFFBGG) {
		if (this.sFFBGG != sFFBGG) {
			this.sFFBGG = sFFBGG;
			modify_sFFBGG = true;
		}
	}

	private String aJH;
	private boolean modify_aJH = false;

	public String getAJH() {
		return aJH;
	}

	public void setAJH(String aJH) {
		if (this.aJH != aJH) {
			this.aJH = aJH;
			modify_aJH = true;
		}
	}

	private String WLSH;
	private boolean modify_WLSH = false;

	public String getWLSH() {
		return WLSH;
	}

	public void setWLSH(String wLSH) {
		if (this.WLSH != wLSH) {
			this.WLSH = wLSH;
			modify_WLSH = true;
		}
	}

	private String SFQR;
	private boolean modify_SFQR = false;

	public String getSFQR() {
		return SFQR;
	}

	public void setSFQR(String SFQR) {
		if (this.SFQR != SFQR) {
			this.SFQR = SFQR;
			modify_SFQR = true;
		}
	}

	private String GJJYWLSH;
	private boolean modify_GJJYWLSH = false;
	public String getGJJYWLSH() {
		return GJJYWLSH;
	}

	public void setGJJYWLSH(String GJJYWLSH) {
		if (this.GJJYWLSH != GJJYWLSH) {
			this.GJJYWLSH = GJJYWLSH;
			modify_GJJYWLSH = true;
		}
	}
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_pROJECT_ID = false;
		modify_dCXMID = false;
		modify_xMMC = false;
		modify_dJLX = false;
		modify_qLLX = false;
		modify_sLLX1 = false;
		modify_sLLX2 = false;
		modify_sLRY = false;
		modify_sLRYID = false;
		modify_sLSJ = false;
		modify_xMQX = false;
		modify_sFDB = false;
		modify_yXBZ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_sFHBZS = false;
		modify_yWLSH = false;
		modify_sFBJ = false;
		modify_dAGH = false;
		modify_sFSB = false;
		modify_tSCS=false;
		modify_dJSJ=false;
		modify_fCYWH=false;
		modify_sFFBGG=false;
		modify_aJH = false;
		modify_WLSH = false;
		modify_SFQR = false;
		modify_GJJYWLSH=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_sLLX1)
			listStrings.add("sLLX1");
		if (!modify_sLLX2)
			listStrings.add("sLLX2");
		if (!modify_sLRY)
			listStrings.add("sLRY");
		if (!modify_sLRYID)
			listStrings.add("sLRYID");
		if (!modify_sLSJ)
			listStrings.add("sLSJ");
		if (!modify_xMQX)
			listStrings.add("xMQX");
		if (!modify_sFDB)
			listStrings.add("sFDB");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_sFHBZS)
			listStrings.add("sFHBZS");
		if (!modify_yWLSH)
			listStrings.add("yWLSH");
		if (!modify_sFBJ)
			listStrings.add("sFBJ");
		if (!modify_dAGH)
			listStrings.add("dAGH");
		if (!modify_sFSB)
			listStrings.add("sFSB");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if(!modify_fCYWH)
			listStrings.add("fCYWH");
		if(!modify_tSCS)
			listStrings.add("tSCS");
		if(!modify_sFFBGG)
			listStrings.add("sFFBGG");
		if(!modify_aJH)
			listStrings.add("aJH");
		if(!modify_WLSH)
			listStrings.add("WLSH");
		if(!modify_SFQR)
			listStrings.add("SFQR");
		if (!modify_GJJYWLSH)
			listStrings.add("gjjywlsh");
		return StringHelper.ListToStrings(listStrings);
	}
	
}
