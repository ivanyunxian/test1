package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-13 
//* ----------------------------------------
//* Internal Entity bdcs_zrz_ls 
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

public class GenerateDCS_ZRZ_LS implements SuperModel<String> {

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

	private String jZWMC;
	private boolean modify_jZWMC = false;

	public String getJZWMC() {
		return jZWMC;
	}

	public void setJZWMC(String jZWMC) {
		if (this.jZWMC != jZWMC) {
			this.jZWMC = jZWMC;
			modify_jZWMC = true;
		}
	}

	private Date jGRQ;
	private boolean modify_jGRQ = false;

	public Date getJGRQ() {
		return jGRQ;
	}

	public void setJGRQ(Date jGRQ) {
		if (this.jGRQ != jGRQ) {
			this.jGRQ = jGRQ;
			modify_jGRQ = true;
		}
	}

	private Double jZWGD;
	private boolean modify_jZWGD = false;

	public Double getJZWGD() {
		return jZWGD;
	}

	public void setJZWGD(Double jZWGD) {
		if (this.jZWGD != jZWGD) {
			this.jZWGD = jZWGD;
			modify_jZWGD = true;
		}
	}

	private Double zZDMJ;
	private boolean modify_zZDMJ = false;

	public Double getZZDMJ() {
		return zZDMJ;
	}

	public void setZZDMJ(Double zZDMJ) {
		if (this.zZDMJ != zZDMJ) {
			this.zZDMJ = zZDMJ;
			modify_zZDMJ = true;
		}
	}

	private Double zYDMJ;
	private boolean modify_zYDMJ = false;

	public Double getZYDMJ() {
		return zYDMJ;
	}

	public void setZYDMJ(Double zYDMJ) {
		if (this.zYDMJ != zYDMJ) {
			this.zYDMJ = zYDMJ;
			modify_zYDMJ = true;
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

	private Double dXSD;
	private boolean modify_dXSD = false;

	public Double getDXSD() {
		return dXSD;
	}

	public void setDXSD(Double dXSD) {
		if (this.dXSD != dXSD) {
			this.dXSD = dXSD;
			modify_dXSD = true;
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

	private Integer zTS;
	private boolean modify_zTS = false;

	public Integer getZTS() {
		return zTS;
	}

	public void setZTS(Integer zTS) {
		if (this.zTS != zTS) {
			this.zTS = zTS;
			modify_zTS = true;
		}
	}

	private String jZWJBYT;
	private boolean modify_jZWJBYT = false;

	public String getJZWJBYT() {
		return jZWJBYT;
	}

	public void setJZWJBYT(String jZWJBYT) {
		if (this.jZWJBYT != jZWJBYT) {
			this.jZWJBYT = jZWJBYT;
			modify_jZWJBYT = true;
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
	/**
	 * 开发商名称--KFSMC
	 */
	private String kFSMC;
	private boolean modify_kFSMC=false;
	public String getKFSMC(){
		return kFSMC;
	}
	public void setKFSMC(String kFSMC){
		if(this.kFSMC !=kFSMC){
			this.kFSMC=kFSMC;
			modify_kFSMC=true;
		}
	}
	/**
	 * 开发商证件号--KFSZJH
	 */
	private String kFSZJH;
	private boolean modify_kFSZJH=false;
	public String getKFSZJH(){
		return kFSZJH;
	}
	public void setKFSZJH(String kFSZJH){
		if(this.kFSZJH !=kFSZJH){
			this.kFSZJH=kFSZJH;
			modify_kFSZJH=true;
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
		modify_ySDM = false;
		modify_xMMC = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_zDDM = false;
		modify_zDBDCDYID = false;
		modify_zRZH = false;
		modify_zL = false;
		modify_jZWMC = false;
		modify_jGRQ = false;
		modify_jZWGD = false;
		modify_zZDMJ = false;
		modify_zYDMJ = false;
		modify_yCJZMJ = false;
		modify_sCJZMJ = false;
		modify_tDSYQR = false;
		modify_dYTDMJ = false;
		modify_fTTDMJ = false;
		modify_fDCJYJG = false;
		modify_zCS = false;
		modify_dSCS = false;
		modify_dXCS = false;
		modify_dXSD = false;
		modify_gHYT = false;
		modify_fWJG = false;
		modify_zTS = false;
		modify_jZWJBYT = false;
		modify_bZ = false;
		modify_zT = false;
		modify_qXDM = false;
		modify_qXMC = false;
		modify_dJQDM = false;
		modify_dJQMC = false;
		modify_dJZQDM = false;
		modify_dJZQMC = false;
		modify_dCXMID = false;
		modify_yXBZ = false;
		modify_dJZT = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_kFSMC=false;
		modify_kFSZJH=false;
		modify_tXWHTYPE = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");
		if (!modify_zRZH)
			listStrings.add("zRZH");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_jZWMC)
			listStrings.add("jZWMC");
		if (!modify_jGRQ)
			listStrings.add("jGRQ");
		if (!modify_jZWGD)
			listStrings.add("jZWGD");
		if (!modify_zZDMJ)
			listStrings.add("zZDMJ");
		if (!modify_zYDMJ)
			listStrings.add("zYDMJ");
		if (!modify_yCJZMJ)
			listStrings.add("yCJZMJ");
		if (!modify_sCJZMJ)
			listStrings.add("sCJZMJ");
		if (!modify_tDSYQR)
			listStrings.add("tDSYQR");
		if (!modify_dYTDMJ)
			listStrings.add("dYTDMJ");
		if (!modify_fTTDMJ)
			listStrings.add("fTTDMJ");
		if (!modify_fDCJYJG)
			listStrings.add("fDCJYJG");
		if (!modify_zCS)
			listStrings.add("zCS");
		if (!modify_dSCS)
			listStrings.add("dSCS");
		if (!modify_dXCS)
			listStrings.add("dXCS");
		if (!modify_dXSD)
			listStrings.add("dXSD");
		if (!modify_gHYT)
			listStrings.add("gHYT");
		if (!modify_fWJG)
			listStrings.add("fWJG");
		if (!modify_zTS)
			listStrings.add("zTS");
		if (!modify_jZWJBYT)
			listStrings.add("jZWJBYT");
		if (!modify_bZ)
			listStrings.add("bZ");
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
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_dJZT)
			listStrings.add("dJZT");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if(!modify_kFSMC)
			listStrings.add("kFSMC");
		if(!modify_kFSZJH)
			listStrings.add("kFSZJH");
		if(!modify_tXWHTYPE)
			listStrings.add("tXWHTYPE");
		return StringHelper.ListToStrings(listStrings);
	}
}
