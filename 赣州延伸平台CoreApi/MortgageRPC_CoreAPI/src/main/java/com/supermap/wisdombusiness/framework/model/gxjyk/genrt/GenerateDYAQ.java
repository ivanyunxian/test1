package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity dyaq 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GenerateDYAQ implements SuperModel<String> {

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

	private Date zWLXQSSJ;
	private boolean modify_zWLXQSSJ = false;

	public Date getZWLXQSSJ() {
		return zWLXQSSJ;
	}

	public void setZWLXQSSJ(Date zWLXQSSJ) {
		if (this.zWLXQSSJ != zWLXQSSJ) {
			this.zWLXQSSJ = zWLXQSSJ;
			modify_zWLXQSSJ = true;
		}
	}

	private Date zWLXJSSJ;
	private boolean modify_zWLXJSSJ = false;

	public Date getZWLXJSSJ() {
		return zWLXJSSJ;
	}

	public void setZWLXJSSJ(Date zWLXJSSJ) {
		if (this.zWLXJSSJ != zWLXJSSJ) {
			this.zWLXJSSJ = zWLXJSSJ;
			modify_zWLXJSSJ = true;
		}
	}

	private String bDCDJZMH;
	private boolean modify_bDCDJZMH = false;

	public String getBDCDJZMH() {
		return bDCDJZMH;
	}

	public void setBDCDJZMH(String bDCDJZMH) {
		if (this.bDCDJZMH != bDCDJZMH) {
			this.bDCDJZMH = bDCDJZMH;
			modify_bDCDJZMH = true;
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

	private String qSZT;
	private boolean modify_qSZT = false;

	public String getQSZT() {
		return qSZT;
	}

	public void setQSZT(String qSZT) {
		if (this.qSZT != qSZT) {
			this.qSZT = qSZT;
			modify_qSZT = true;
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

	private String bSM;
	private boolean modify_bSM = false;

	public String getBSM() {
		return bSM;
	}

	public void setBSM(String bSM) {
		if (this.bSM != bSM) {
			this.bSM = bSM;
			modify_bSM = true;
		}
	}
	private String tsbh;
	private boolean modify_tsbh = false;

	public String getTSBH() {
		return tsbh;
	}

	public void setTSBH(String tsbh) {
		if (this.tsbh != tsbh) {
			this.tsbh = tsbh;
			modify_tsbh = true;
		}
	}

	public void resetModifyState() {
		modify_id = false;
		modify_yWH = false;
		modify_bDCDYH = false;
		modify_qLID = false;
		modify_dJLX = false;
		modify_dJYY = false;
		modify_zWLXQSSJ = false;
		modify_zWLXJSSJ = false;
		modify_bDCDJZMH = false;
		modify_qXDM = false;
		modify_dJJG = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_fJ = false;
		modify_qSZT = false;
		modify_bDCDYID = false;
		modify_gXXMBH = false;
		modify_dYFS = false;
		modify_bDBZZQSE = false;
		modify_zGZQQDSS = false;
		modify_zGZQSE = false;
		modify_zXDYYWH = false;
		modify_zXDYYY = false;
		modify_zXSJ = false;
		modify_dYBDCLX = false;
		modify_zJJZWZL = false;
		modify_zJJZWDYFW = false;
		modify_dYR = false;
		modify_lYQLID = false;
		modify_ySDM = false;
		modify_dYMJ = false;
		modify_zSBH = false;
		modify_bZ = false;
		modify_bSM = false;
		modify_tsbh = false;
		modify_bDCQZH = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_dJYY)
			listStrings.add("dJYY");
		if (!modify_zWLXQSSJ)
			listStrings.add("zWLXQSSJ");
		if (!modify_zWLXJSSJ)
			listStrings.add("zWLXJSSJ");
		if (!modify_bDCDJZMH)
			listStrings.add("bDCDJZMH");
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
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_dYFS)
			listStrings.add("dYFS");
		if (!modify_bDBZZQSE)
			listStrings.add("bDBZZQSE");
		if (!modify_zGZQQDSS)
			listStrings.add("zGZQQDSS");
		if (!modify_zGZQSE)
			listStrings.add("zGZQSE");
		if (!modify_zXDYYWH)
			listStrings.add("zXDYYWH");
		if (!modify_zXDYYY)
			listStrings.add("zXDYYY");
		if (!modify_zXSJ)
			listStrings.add("zXSJ");
		if (!modify_dYBDCLX)
			listStrings.add("dYBDCLX");
		if (!modify_zJJZWZL)
			listStrings.add("zJJZWZL");
		if (!modify_zJJZWDYFW)
			listStrings.add("zJJZWDYFW");
		if (!modify_dYR)
			listStrings.add("dYR");
		if (!modify_lYQLID)
			listStrings.add("lYQLID");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_dYMJ)
			listStrings.add("dYMJ");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_bSM)
			listStrings.add("bSM");
		if (!modify_tsbh)
			listStrings.add("tsbh");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		return StringHelper.ListToStrings(listStrings);
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
}
