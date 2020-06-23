package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/11 
//* ----------------------------------------
//* Internal Entity qlr 
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


public class GenerateQLR implements SuperModel<String> {

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

	private Integer sXH;
	private boolean modify_sXH = false;

	public Integer getSXH() {
		return sXH;
	}

	public void setSXH(Integer sXH) {
		if (this.sXH != sXH) {
			this.sXH = sXH;
			modify_sXH = true;
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

	private String zJZL;
	private boolean modify_zJZL = false;

	public String getZJZL() {
		return zJZL;
	}

	public void setZJZL(String zJZL) {
		if (this.zJZL != zJZL) {
			this.zJZL = zJZL;
			modify_zJZL = true;
		}
	}

	private String zJH;
	private boolean modify_zJH = false;

	public String getZJH() {
		return zJH;
	}

	public void setZJH(String zJH) {
		if (this.zJH != zJH) {
			this.zJH = zJH;
			modify_zJH = true;
		}
	}

	private String fZJG;
	private boolean modify_fZJG = false;

	public String getFZJG() {
		return fZJG;
	}

	public void setFZJG(String fZJG) {
		if (this.fZJG != fZJG) {
			this.fZJG = fZJG;
			modify_fZJG = true;
		}
	}

	private String sSHY;
	private boolean modify_sSHY = false;

	public String getSSHY() {
		return sSHY;
	}

	public void setSSHY(String sSHY) {
		if (this.sSHY != sSHY) {
			this.sSHY = sSHY;
			modify_sSHY = true;
		}
	}

	private String gJ;
	private boolean modify_gJ = false;

	public String getGJ() {
		return gJ;
	}

	public void setGJ(String gJ) {
		if (this.gJ != gJ) {
			this.gJ = gJ;
			modify_gJ = true;
		}
	}

	private String hJSZSS;
	private boolean modify_hJSZSS = false;

	public String getHJSZSS() {
		return hJSZSS;
	}

	public void setHJSZSS(String hJSZSS) {
		if (this.hJSZSS != hJSZSS) {
			this.hJSZSS = hJSZSS;
			modify_hJSZSS = true;
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

	private String dH;
	private boolean modify_dH = false;

	public String getDH() {
		return dH;
	}

	public void setDH(String dH) {
		if (this.dH != dH) {
			this.dH = dH;
			modify_dH = true;
		}
	}

	private String dZ;
	private boolean modify_dZ = false;

	public String getDZ() {
		return dZ;
	}

	public void setDZ(String dZ) {
		if (this.dZ != dZ) {
			this.dZ = dZ;
			modify_dZ = true;
		}
	}

	private String yB;
	private boolean modify_yB = false;

	public String getYB() {
		return yB;
	}

	public void setYB(String yB) {
		if (this.yB != yB) {
			this.yB = yB;
			modify_yB = true;
		}
	}

	private String gZDW;
	private boolean modify_gZDW = false;

	public String getGZDW() {
		return gZDW;
	}

	public void setGZDW(String gZDW) {
		if (this.gZDW != gZDW) {
			this.gZDW = gZDW;
			modify_gZDW = true;
		}
	}

	private String dZYJ;
	private boolean modify_dZYJ = false;

	public String getDZYJ() {
		return dZYJ;
	}

	public void setDZYJ(String dZYJ) {
		if (this.dZYJ != dZYJ) {
			this.dZYJ = dZYJ;
			modify_dZYJ = true;
		}
	}

	private String qLRLX;
	private boolean modify_qLRLX = false;

	public String getQLRLX() {
		return qLRLX;
	}

	public void setQLRLX(String qLRLX) {
		if (this.qLRLX != qLRLX) {
			this.qLRLX = qLRLX;
			modify_qLRLX = true;
		}
	}

	private String qLBL;
	private boolean modify_qLBL = false;

	public String getQLBL() {
		return qLBL;
	}

	public void setQLBL(String qLBL) {
		if (this.qLBL != qLBL) {
			this.qLBL = qLBL;
			modify_qLBL = true;
		}
	}

	private String gYFS;
	private boolean modify_gYFS = false;

	public String getGYFS() {
		return gYFS;
	}

	public void setGYFS(String gYFS) {
		if (this.gYFS != gYFS) {
			this.gYFS = gYFS;
			modify_gYFS = true;
		}
	}

	private String gYQK;
	private boolean modify_gYQK = false;

	public String getGYQK() {
		return gYQK;
	}

	public void setGYQK(String gYQK) {
		if (this.gYQK != gYQK) {
			this.gYQK = gYQK;
			modify_gYQK = true;
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

	private String iSCZR;
	private boolean modify_iSCZR = false;

	public String getISCZR() {
		return iSCZR;
	}

	public void setISCZR(String iSCZR) {
		if (this.iSCZR != iSCZR) {
			this.iSCZR = iSCZR;
			modify_iSCZR = true;
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

	private String qZYSXLH;
	private boolean modify_qZYSXLH = false;

	public String getQZYSXLH() {
		return qZYSXLH;
	}

	public void setQZYSXLH(String qZYSXLH) {
		if (this.qZYSXLH != qZYSXLH) {
			this.qZYSXLH = qZYSXLH;
			modify_qZYSXLH = true;
		}
	}

	private String gXLX;
	private boolean modify_gXLX = false;

	public String getGXLX() {
		return gXLX;
	}

	public void setGXLX(String gXLX) {
		if (this.gXLX != gXLX) {
			this.gXLX = gXLX;
			modify_gXLX = true;
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

	private String sQRLB;
	private boolean modify_sQRLB = false;

	public String getSQRLB() {
		return sQRLB;
	}

	public void setSQRLB(String sQRLB) {
		if (this.sQRLB != sQRLB) {
			this.sQRLB = sQRLB;
			modify_sQRLB = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_qLRID = false;
		modify_qLID = false;
		modify_sXH = false;
		modify_qLRMC = false;
		modify_bDCQZH = false;
		modify_zJZL = false;
		modify_zJH = false;
		modify_fZJG = false;
		modify_sSHY = false;
		modify_gJ = false;
		modify_hJSZSS = false;
		modify_xB = false;
		modify_dH = false;
		modify_dZ = false;
		modify_yB = false;
		modify_gZDW = false;
		modify_dZYJ = false;
		modify_qLRLX = false;
		modify_qLBL = false;
		modify_gYFS = false;
		modify_gYQK = false;
		modify_bZ = false;
		modify_iSCZR = false;
		modify_ySDM = false;
		modify_bDCDYH = false;
		modify_qZYSXLH = false;
		modify_gXLX = false;
		modify_gXXMBH = false;
		modify_sQRLB = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_qLRID)
			listStrings.add("qLRID");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_qLRMC)
			listStrings.add("qLRMC");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_zJZL)
			listStrings.add("zJZL");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_fZJG)
			listStrings.add("fZJG");
		if (!modify_sSHY)
			listStrings.add("sSHY");
		if (!modify_gJ)
			listStrings.add("gJ");
		if (!modify_hJSZSS)
			listStrings.add("hJSZSS");
		if (!modify_xB)
			listStrings.add("xB");
		if (!modify_dH)
			listStrings.add("dH");
		if (!modify_dZ)
			listStrings.add("dZ");
		if (!modify_yB)
			listStrings.add("yB");
		if (!modify_gZDW)
			listStrings.add("gZDW");
		if (!modify_dZYJ)
			listStrings.add("dZYJ");
		if (!modify_qLRLX)
			listStrings.add("qLRLX");
		if (!modify_qLBL)
			listStrings.add("qLBL");
		if (!modify_gYFS)
			listStrings.add("gYFS");
		if (!modify_gYQK)
			listStrings.add("gYQK");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_iSCZR)
			listStrings.add("iSCZR");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_qZYSXLH)
			listStrings.add("qZYSXLH");
		if (!modify_gXLX)
			listStrings.add("gXLX");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_sQRLB)
			listStrings.add("sQRLB");

		return StringHelper.ListToStrings(listStrings);
	}
}
