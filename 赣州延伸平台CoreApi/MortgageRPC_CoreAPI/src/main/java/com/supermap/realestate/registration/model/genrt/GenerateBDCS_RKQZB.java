package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Internal Entity bdcs_rkqzb 
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

public class GenerateBDCS_RKQZB implements SuperModel<String> {

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

	private String xMID;
	private boolean modify_xMID = false;

	public String getXMID() {
		return xMID;
	}

	public void setXMID(String xMID) {
		if (this.xMID != xMID) {
			this.xMID = xMID;
			modify_xMID = true;
		}
	}

	private Long qZBH;
	private boolean modify_qZBH = false;

	public Long getQZBH() {
		return qZBH;
	}

	public void setQZBH(Long qZBH) {
		if (this.qZBH != qZBH) {
			this.qZBH = qZBH;
			modify_qZBH = true;
		}
	}
	
	private String qZZL;
	private boolean modify_qZZL = false;

	public String getQZZL() {
		return qZZL;
	}

	public void setQZZL(String qZZL) {
		if (this.qZZL != qZZL) {
			this.qZZL = qZZL;
			modify_qZZL = true;
		}
	}
	
	private String lQRY;
	private boolean modify_lQRY = false;

	public String getLQRY() {
		return lQRY;
	}

	public void setLQRY(String lQRY) {
		if (this.lQRY != lQRY) {
			this.lQRY = lQRY;
			modify_lQRY = true;
		}
	}
	
	private String lQRYID;
	private boolean modify_lQRYID = false;

	public String getLQRYID() {
		return lQRYID;
	}

	public void setLQRYID(String lQRYID) {
		if (this.lQRYID != lQRYID) {
			this.lQRYID = lQRYID;
			modify_lQRYID= true;
		}
	}
	
	private String lQKS;
	private boolean modify_lQKS = false;
	
	public String getLQKS() {
		return lQKS;
	}
	
	public void setLQKS(String lQKS) {
		if (this.lQKS != lQKS) {
			this.lQKS = lQKS;
			modify_lQKS = true;
		}
	}
	
	private String lQKSID;
	private boolean modify_lQKSID = false;
	
	public String getLQKSID() {
		return lQKSID;
	}
	
	public void setLQKSID(String lQKSID) {
		if (this.lQKSID != lQKSID) {
			this.lQKSID = lQKSID;
			modify_lQKSID= true;
		}
	}
	
	private String sYQK;
	private boolean modify_sYQK = false;

	public String getSYQK() {
		return sYQK;
	}

	public void setSYQK(String sYQK) {
		if (this.sYQK != sYQK) {
			this.sYQK = sYQK;
			modify_sYQK= true;
		}
	}

	private Date cJSJ;
	private boolean modify_cJSJ = false;

	public Date getCJSJ() {
		return cJSJ;
	}

	public void setCJSJ(Date cJSJ) {
		if (this.cJSJ != cJSJ) {
			this.cJSJ = cJSJ;
			modify_cJSJ = true;
		}
	}

	private String sFSZ;
	private boolean modify_sFSZ = false;

	public String getSFSZ() {
		return sFSZ;
	}

	public void setSFSZ(String sFSZ) {
		if (this.sFSZ != sFSZ) {
			this.sFSZ = sFSZ;
			modify_sFSZ = true;
		}
	}

	private String sZR;
	private boolean modify_sZR = false;

	public String getSZR() {
		return sZR;
	}

	public void setSZR(String sZR) {
		if (this.sZR != sZR) {
			this.sZR = sZR;
			modify_sZR = true;
		}
	}

	private Date sZSJ;
	private boolean modify_sZSJ = false;

	public Date getSZSJ() {
		return sZSJ;
	}

	public void setSZSJ(Date sZSJ) {
		if (this.sZSJ != sZSJ) {
			this.sZSJ = sZSJ;
			modify_sZSJ = true;
		}
	}
	
	private String sZRY;
	private boolean modify_sZRY = false;

	public String getSZRY() {
		return sZRY;
	}

	public void setSZRY(String sZRY) {
		if (this.sZRY != sZRY) {
			this.sZRY = sZRY;
			modify_sZRY = true;
		}
	}
	
	private String qLRMC;
	private boolean modify_qLRMC = false;

	public String getQLRMC() {
		return qLRMC;
	}

	public void setQLRMC(String qLRMC) {
		if (this.qLRMC != qLRMC) {
			this.qLRMC = qLRMC;
			modify_qLRMC = true;
		}
	}
	
	private String qLRZJH;
	private boolean modify_qLRZJH = false;

	public String getQLRZJH() {
		return qLRZJH;
	}

	public void setQLRZJH(String qLRZJH) {
		if (this.qLRZJH != qLRZJH) {
			this.qLRZJH = qLRZJH;
			modify_qLRZJH = true;
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
	
	private String lZRY;
	private boolean modify_lZRY = false;

	public String getLZRY() {
		return lZRY;
	}

	public void setLZRY(String lZRY) {
		if (this.lZRY != lZRY) {
			this.lZRY = lZRY;
			modify_lZRY = true;
		}
	}
	
	private String lZRZJH;
	private boolean modify_lZRZJH = false;

	public String getLZRZJH() {
		return lZRZJH;
	}

	public void setLZRZJH(String lZRZJH) {
		if (this.lZRZJH != lZRZJH) {
			this.lZRZJH = lZRZJH;
			modify_lZRZJH = true;
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

	private String sFZF;
	private boolean modify_sFZF = false;

	public String getSFZF() {
		return sFZF;
	}

	public void setSFZF(String sFZF) {
		if (this.sFZF != sFZF) {
			this.sFZF = sFZF;
			modify_sFZF = true;
		}
	}

	private Date zFSJ;
	private boolean modify_zFSJ = false;

	public Date getZFSJ() {
		return zFSJ;
	}

	public void setZFSJ(Date zFSJ) {
		if (this.zFSJ != zFSJ) {
			this.zFSJ = zFSJ;
			modify_zFSJ = true;
		}
	}

	private String zFYY;
	private boolean modify_zFYY = false;

	public String getZFYY() {
		return zFYY;
	}

	public void setZFYY(String zFYY) {
		if (this.zFYY != zFYY) {
			this.zFYY = zFYY;
			modify_zFYY = true;
		}
	}

	private String zFR;
	private boolean modify_zFR = false;

	public String getZFR() {
		return zFR;
	}

	public void setZFR(String zFR) {
		if (this.zFR != zFR) {
			this.zFR = zFR;
			modify_zFR = true;
		}
	}

	private String qZLX;
	private boolean modify_qZLX = false;

	public String getQZLX() {
		return qZLX;
	}

	public void setQZLX(String qZLX) {
		if (this.qZLX != qZLX) {
			this.qZLX = qZLX;
			modify_qZLX = true;
		}
	}

	private String zSZT;
	private boolean modify_zSZT = false;

	public String getZSZT() {
		return zSZT;
	}

	public void setZSZT(String qZLX) {
		if (this.zSZT != qZLX) {
			this.zSZT = qZLX;
			modify_zSZT = true;
		}
	}
	
	private String lZRPIC;
	private boolean modify_lZRPIC = false;

	public String getLZRPIC() {
		return lZRPIC;
	}

	public void setLZRPIC(String lZRPIC) {
		if (this.lZRPIC != lZRPIC) {
			this.lZRPIC = lZRPIC;
			modify_lZRPIC = true;
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
	
	private String sLBH;
	private boolean modify_sLBH = false;

	public String getSLBH() {
		return sLBH;
	}

	public void setSLBH(String sLBH) {
		if (this.sLBH != sLBH) {
			this.sLBH = sLBH;
			modify_sLBH = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMID = false;
		modify_qZBH = false;
		modify_cJSJ = false;
		modify_sFSZ = false;
		modify_sZR = false;
		modify_sZSJ = false;
		modify_sFZF = false;
		modify_zFSJ = false;
		modify_zFYY = false;
		modify_zFR = false;
		modify_qZLX = false;
		modify_zSZT = false;
		modify_lZRPIC = false;
		modify_qZZL = false;
		modify_lQRY = false;
		modify_lQRYID = false;
		modify_lQKS = false;
		modify_lQKSID = false;
		modify_sYQK = false;
		modify_sZRY = false;
		modify_qLRMC = false;
		modify_qLRZJH = false;
		modify_bDCQZH = false;
		modify_sFFZ = false;
		modify_lZRY = false;
		modify_lZRZJH = false;
		modify_fZSJ = false;
		modify_zL = false;
		modify_sLBH = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMID)
			listStrings.add("xMID");
		if (!modify_qZBH)
			listStrings.add("qZBH");
		if (!modify_cJSJ)
			listStrings.add("cJSJ");
		if (!modify_sFSZ)
			listStrings.add("sFSZ");
		if (!modify_sZR)
			listStrings.add("sZR");
		if (!modify_sZSJ)
			listStrings.add("sZSJ");
		if (!modify_sFZF)
			listStrings.add("sFZF");
		if (!modify_zFSJ)
			listStrings.add("zFSJ");
		if (!modify_zFYY)
			listStrings.add("zFYY");
		if (!modify_zFR)
			listStrings.add("zFR");
		if (!modify_qZLX)
			listStrings.add("qZLX");
		if (!modify_zSZT)
			listStrings.add("zSZT");
		if (!modify_lZRPIC)
			listStrings.add("lZRPIC");
		if(!modify_qZZL)
			listStrings.add("qZZL");
		if(!modify_lQRY)
			listStrings.add("lQRY");
		if(!modify_lQRYID)
			listStrings.add("lQRYID");
		if(!modify_lQKS)
			listStrings.add("lQKS");
		if(!modify_lQKSID)
			listStrings.add("lQKSID");
		if(!modify_sYQK)
			listStrings.add("sYQK");
		if(!modify_sZRY)
			listStrings.add("sZRY");
		if(!modify_qLRMC)
			listStrings.add("qLRMC");
		if(!modify_qLRZJH)
			listStrings.add("qLRZJH");
		if(!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if(!modify_sFFZ)
			listStrings.add("sFFZ");
		if(!modify_lZRY)
			listStrings.add("lZRY");
		if(!modify_lZRZJH)
			listStrings.add("lZRZJH");
		if(!modify_fZSJ)
			listStrings.add("fZSJ");
		if(!modify_zL)
			listStrings.add("zL");
		if (!modify_sLBH) 
			listStrings.add("sLBH");
		return StringHelper.ListToStrings(listStrings);
	}
}
