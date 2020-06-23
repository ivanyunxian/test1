package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/14 
//* ----------------------------------------
//* Internal Entity bdcs_xtdz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.realestate.registration.util.ConstHelper;

import javax.persistence.Transient;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_XTDZ implements SuperModel<String> {

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

	private String jZH;
	private boolean modify_jZH = false;

	public String getJZH() {
		return jZH;
	}

	public void setJZH(String jZH) {
		if (this.jZH != jZH) {
			this.jZH = jZH;
			modify_jZH = true;
		}
	}

	private String lH;
	private boolean modify_lH = false;

	public String getLH() {
		return lH;
	}

	public void setLH(String lH) {
		if (this.lH != lH) {
			this.lH = lH;
			modify_lH = true;
		}
	}

	private Integer xH;
	private boolean modify_xH = false;

	public Integer getXH() {
		return xH;
	}

	public void setXH(Integer xH) {
		if (this.xH != xH) {
			this.xH = xH;
			modify_xH = true;
		}
	}

	private String xM;
	private boolean modify_xM = false;

	public String getXM() {
		return xM;
	}

	public void setXM(String xM) {
		if (this.xM != xM) {
			this.xM = xM;
			modify_xM = true;
		}
	}

	private String pYBM;
	private boolean modify_pYBM = false;

	public String getPYBM() {
		return pYBM;
	}

	public void setPYBM(String pYBM) {
		if (this.pYBM != pYBM) {
			this.pYBM = pYBM;
			modify_pYBM = true;
		}
	}

	private String sFZH;
	private boolean modify_sFZH = false;

	public String getSFZH() {
		return sFZH;
	}

	public void setSFZH(String sFZH) {
		if (this.sFZH != sFZH) {
			this.sFZH = sFZH;
			modify_sFZH = true;
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

	private String hH;
	private boolean modify_hH = false;

	public String getHH() {
		return hH;
	}

	public void setHH(String hH) {
		if (this.hH != hH) {
			this.hH = hH;
			modify_hH = true;
		}
	}

	private String yT;
	private boolean modify_yT = false;

	public String getYT() {
		return yT;
	}

	public void setYT(String yT) {
		if (this.yT != yT) {
			this.yT = yT;
			modify_yT = true;
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

	private String fCZH;
	private boolean modify_fCZH = false;

	public String getFCZH() {
		return fCZH;
	}

	public void setFCZH(String fCZH) {
		if (this.fCZH != fCZH) {
			this.fCZH = fCZH;
			modify_fCZH = true;
		}
	}

	private String sYQLX;
	private boolean modify_sYQLX = false;

	public String getSYQLX() {
		return sYQLX;
	}

	public void setSYQLX(String sYQLX) {
		if (this.sYQLX != sYQLX) {
			this.sYQLX = sYQLX;
			modify_sYQLX = true;
		}
	}

	private Date zZSJ;
	private boolean modify_zZSJ = false;

	public Date getZZSJ() {
		return zZSJ;
	}

	public void setZZSJ(Date zZSJ) {
		if (this.zZSJ != zZSJ) {
			this.zZSJ = zZSJ;
			modify_zZSJ = true;
		}
	}

	private Double fTXS;
	private boolean modify_fTXS = false;

	public Double getFTXS() {
		return fTXS;
	}

	public void setFTXS(Double fTXS) {
		if (this.fTXS != fTXS) {
			this.fTXS = fTXS;
			modify_fTXS = true;
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

	private Date fZSJ;
	private boolean modify_fZSJ = false;

	public Date getFZSJ() {
		return fZSJ;
	}

	public void setFZSJ(Date fZSJ) {
		if (this.fZSJ != fZSJ) {
			this.fZSJ = fZSJ;
			modify_fZSJ = true;
		}
	}

	private Integer bGCS;
	private boolean modify_bGCS = false;

	public Integer getBGCS() {
		return bGCS;
	}

	public void setBGCS(Integer bGCS) {
		if (this.bGCS != bGCS) {
			this.bGCS = bGCS;
			modify_bGCS = true;
		}
	}

	private Integer fZCS;
	private boolean modify_fZCS = false;

	public Integer getFZCS() {
		return fZCS;
	}

	public void setFZCS(Integer fZCS) {
		if (this.fZCS != fZCS) {
			this.fZCS = fZCS;
			modify_fZCS = true;
		}
	}

	private Double fTMJ;
	private boolean modify_fTMJ = false;

	public Double getFTMJ() {
		return fTMJ;
	}

	public void setFTMJ(Double fTMJ) {
		if (this.fTMJ != fTMJ) {
			this.fTMJ = fTMJ;
			modify_fTMJ = true;
		}
	}

	private Double fTMJ_CR;
	private boolean modify_fTMJ_CR = false;

	public Double getFTMJ_CR() {
		return fTMJ_CR;
	}

	public void setFTMJ_CR(Double fTMJ_CR) {
		if (this.fTMJ_CR != fTMJ_CR) {
			this.fTMJ_CR = fTMJ_CR;
			modify_fTMJ_CR = true;
		}
	}

	private Double fTMJ_HB;
	private boolean modify_fTMJ_HB = false;

	public Double getFTMJ_HB() {
		return fTMJ_HB;
	}

	public void setFTMJ_HB(Double fTMJ_HB) {
		if (this.fTMJ_HB != fTMJ_HB) {
			this.fTMJ_HB = fTMJ_HB;
			modify_fTMJ_HB = true;
		}
	}

	private String sFFZ;
	private boolean modify_sFFZ = false;

	public String getSFFZ() {
		return sFFZ;
	}

	public void setSFFZ(String sFFZ) {
		if (this.sFFZ != sFFZ) {
			this.sFFZ = sFFZ;
			modify_sFFZ = true;
		}
	}

	private Integer iD;
	private boolean modify_iD = false;

	public Integer getID() {
		return iD;
	}

	public void setID(Integer iD) {
		if (this.iD != iD) {
			this.iD = iD;
			modify_iD = true;
		}
	}

	private String gRZT;
	private boolean modify_gRZT = false;

	public String getGRZT() {
		return gRZT;
	}

	public void setGRZT(String gRZT) {
		if (this.gRZT != gRZT) {
			this.gRZT = gRZT;
			modify_gRZT = true;
		}
	}

	private String dY;
	private boolean modify_dY = false;

	public String getDY() {
		return dY;
	}

	public void setDY(String dY) {
		if (this.dY != dY) {
			this.dY = dY;
			modify_dY = true;
		}
	}

	private String dYNR;
	private boolean modify_dYNR = false;

	public String getDYNR() {
		return dYNR;
	}

	public void setDYNR(String dYNR) {
		if (this.dYNR != dYNR) {
			this.dYNR = dYNR;
			modify_dYNR = true;
		}
	}

	private Double dYQX;
	private boolean modify_dYQX = false;

	public Double getDYQX() {
		return dYQX;
	}

	public void setDYQX(Double dYQX) {
		if (this.dYQX != dYQX) {
			this.dYQX = dYQX;
			modify_dYQX = true;
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

	private String eCZT;
	private boolean modify_eCZT = false;

	public String getECZT() {
		return eCZT;
	}

	public void setECZT(String eCZT) {
		if (this.eCZT != eCZT) {
			this.eCZT = eCZT;
			modify_eCZT = true;
		}
	}

	private String lC;
	private boolean modify_lC = false;

	public String getLC() {
		return lC;
	}

	public void setLC(String lC) {
		if (this.lC != lC) {
			this.lC = lC;
			modify_lC = true;
		}
	}

	private String zSZT;
	private boolean modify_zSZT = false;

	public String getZSZT() {
		return zSZT;
	}

	public void setZSZT(String zSZT) {
		if (this.zSZT != zSZT) {
			this.zSZT = zSZT;
			modify_zSZT = true;
		}
	}

	private String fZLX;
	private boolean modify_fZLX = false;

	public String getFZLX() {
		return fZLX;
	}

	public void setFZLX(String fZLX) {
		if (this.fZLX != fZLX) {
			this.fZLX = fZLX;
			modify_fZLX = true;
		}
	}

	private String zDID;
	private boolean modify_zDID = false;

	public String getZDID() {
		return zDID;
	}

	public void setZDID(String zDID) {
		if (this.zDID != zDID) {
			this.zDID = zDID;
			modify_zDID = true;
		}
	}

	private String yWZLX;
	private boolean modify_yWZLX = false;

	public String getYWZLX() {
		return yWZLX;
	}

	public void setYWZLX(String yWZLX) {
		if (this.yWZLX != yWZLX) {
			this.yWZLX = yWZLX;
			modify_yWZLX = true;
		}
	}

	private String tDZ;
	private boolean modify_tDZ = false;

	public String getTDZ() {
		return tDZ;
	}

	public void setTDZ(String tDZ) {
		if (this.tDZ != tDZ) {
			this.tDZ = tDZ;
			modify_tDZ = true;
		}
	}

	private String tDZID;
	private boolean modify_tDZID = false;

	public String getTDZID() {
		return tDZID;
	}

	public void setTDZID(String tDZID) {
		if (this.tDZID != tDZID) {
			this.tDZID = tDZID;
			modify_tDZID = true;
		}
	}

	private String dJH;
	private boolean modify_dJH = false;

	public String getDJH() {
		return dJH;
	}

	public void setDJH(String dJH) {
		if (this.dJH != dJH) {
			this.dJH = dJH;
			modify_dJH = true;
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

	private String gXDJH;
	private boolean modify_gXDJH = false;

	public String getGXDJH() {
		return gXDJH;
	}

	public void setGXDJH(String gXDJH) {
		if (this.gXDJH != gXDJH) {
			this.gXDJH = gXDJH;
			modify_gXDJH = true;
		}
	}

	private String tDYT;
	private boolean modify_tDYT = false;

	public String getTDYT() {
		return tDYT;
	}

	public void setTDYT(String tDYT) {
		if (this.tDYT != tDYT) {
			this.tDYT = tDYT;
			modify_tDYT = true;
		}
	}

	private String zDSYQLX;
	private boolean modify_zDSYQLX = false;

	public String getZDSYQLX() {
		return zDSYQLX;
	}

	public void setZDSYQLX(String zDSYQLX) {
		if (this.zDSYQLX != zDSYQLX) {
			this.zDSYQLX = zDSYQLX;
			modify_zDSYQLX = true;
		}
	}

	private String qLRID;
	private boolean modify_qLRID = false;

	public String getQLRID() {
		return qLRID;
	}

	public void setQLRID(String qLRID) {
		if (this.qLRID != qLRID) {
			this.qLRID = qLRID;
			modify_qLRID = true;
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

	private String fWBDCDYID;
	private boolean modify_fWBDCDYID = false;

	public String getFWBDCDYID() {
		return fWBDCDYID;
	}

	public void setFWBDCDYID(String fWBDCDYID) {
		if (this.fWBDCDYID != fWBDCDYID) {
			this.fWBDCDYID = fWBDCDYID;
			modify_fWBDCDYID = true;
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

	private Integer yXBZ;
	private boolean modify_yXBZ = false;

	public Integer getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(Integer yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}
	
	private Integer iFSHOWTDZH;
	private boolean modify_iFSHOWTDZH = false;

	public Integer getIFSHOWTDZH() {
		return iFSHOWTDZH;
	}

	public void setIFSHOWTDZH(Integer iFSHOWTDZH) {
		if (this.iFSHOWTDZH != iFSHOWTDZH) {
			this.iFSHOWTDZH = iFSHOWTDZH;
			modify_iFSHOWTDZH = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_jZH = false;
		modify_lH = false;
		modify_xH = false;
		modify_xM = false;
		modify_pYBM = false;
		modify_sFZH = false;
		modify_dYH = false;
		modify_mPH = false;
		modify_hH = false;
		modify_yT = false;
		modify_jZMJ = false;
		modify_fCZH = false;
		modify_sYQLX = false;
		modify_zZSJ = false;
		modify_fTXS = false;
		modify_tDZH = false;
		modify_fZSJ = false;
		modify_bGCS = false;
		modify_fZCS = false;
		modify_fTMJ = false;
		modify_fTMJ_CR = false;
		modify_fTMJ_HB = false;
		modify_sFFZ = false;
		modify_iD = false;
		modify_gRZT = false;
		modify_dY = false;
		modify_dYNR = false;
		modify_dYQX = false;
		modify_bZ = false;
		modify_eCZT = false;
		modify_lC = false;
		modify_zSZT = false;
		modify_fZLX = false;
		modify_zDID = false;
		modify_yWZLX = false;
		modify_tDZ = false;
		modify_tDZID = false;
		modify_dJH = false;
		modify_zDMJ = false;
		modify_sYQMJ = false;
		modify_zL = false;
		modify_gXDJH = false;
		modify_tDYT = false;
		modify_zDSYQLX = false;
		modify_qLRID = false;
		modify_qLID = false;
		modify_bDCQZH = false;
		modify_dJDYID = false;
		modify_fWBDCDYID = false;
		modify_zDBDCDYID = false;
		modify_yXBZ = false;
		modify_iFSHOWTDZH = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_jZH)
			listStrings.add("jZH");
		if (!modify_lH)
			listStrings.add("lH");
		if (!modify_xH)
			listStrings.add("xH");
		if (!modify_xM)
			listStrings.add("xM");
		if (!modify_pYBM)
			listStrings.add("pYBM");
		if (!modify_sFZH)
			listStrings.add("sFZH");
		if (!modify_dYH)
			listStrings.add("dYH");
		if (!modify_mPH)
			listStrings.add("mPH");
		if (!modify_hH)
			listStrings.add("hH");
		if (!modify_yT)
			listStrings.add("yT");
		if (!modify_jZMJ)
			listStrings.add("jZMJ");
		if (!modify_fCZH)
			listStrings.add("fCZH");
		if (!modify_sYQLX)
			listStrings.add("sYQLX");
		if (!modify_zZSJ)
			listStrings.add("zZSJ");
		if (!modify_fTXS)
			listStrings.add("fTXS");
		if (!modify_tDZH)
			listStrings.add("tDZH");
		if (!modify_fZSJ)
			listStrings.add("fZSJ");
		if (!modify_bGCS)
			listStrings.add("bGCS");
		if (!modify_fZCS)
			listStrings.add("fZCS");
		if (!modify_fTMJ)
			listStrings.add("fTMJ");
		if (!modify_fTMJ_CR)
			listStrings.add("fTMJ_CR");
		if (!modify_fTMJ_HB)
			listStrings.add("fTMJ_HB");
		if (!modify_sFFZ)
			listStrings.add("sFFZ");
		if (!modify_iD)
			listStrings.add("iD");
		if (!modify_gRZT)
			listStrings.add("gRZT");
		if (!modify_dY)
			listStrings.add("dY");
		if (!modify_dYNR)
			listStrings.add("dYNR");
		if (!modify_dYQX)
			listStrings.add("dYQX");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_eCZT)
			listStrings.add("eCZT");
		if (!modify_lC)
			listStrings.add("lC");
		if (!modify_zSZT)
			listStrings.add("zSZT");
		if (!modify_fZLX)
			listStrings.add("fZLX");
		if (!modify_zDID)
			listStrings.add("zDID");
		if (!modify_yWZLX)
			listStrings.add("yWZLX");
		if (!modify_tDZ)
			listStrings.add("tDZ");
		if (!modify_tDZID)
			listStrings.add("tDZID");
		if (!modify_dJH)
			listStrings.add("dJH");
		if (!modify_zDMJ)
			listStrings.add("zDMJ");
		if (!modify_sYQMJ)
			listStrings.add("sYQMJ");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_gXDJH)
			listStrings.add("gXDJH");
		if (!modify_tDYT)
			listStrings.add("tDYT");
		if (!modify_zDSYQLX)
			listStrings.add("zDSYQLX");
		if (!modify_qLRID)
			listStrings.add("qLRID");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_dJDYID)
			listStrings.add("dJDYID");
		if (!modify_fWBDCDYID)
			listStrings.add("fWBDCDYID");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if(!modify_iFSHOWTDZH)
			listStrings.add("iFSHOWTDZH");

		return StringHelper.ListToStrings(listStrings);
	}
}
