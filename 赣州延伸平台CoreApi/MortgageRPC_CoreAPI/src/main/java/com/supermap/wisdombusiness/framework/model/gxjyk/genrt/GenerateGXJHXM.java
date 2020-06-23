package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity gxjhxm 
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


public class GenerateGXJHXM implements SuperModel<String> {

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

	private String dJDL;
	private boolean modify_dJDL = false;

	public String getDJDL() {
		return dJDL;
	}

	public void setDJDL(String dJDL) {
		if (this.dJDL != dJDL) {
			this.dJDL = dJDL;
			modify_dJDL = true;
		}
	}

	private String dJXL;
	private boolean modify_dJXL = false;

	public String getDJXL() {
		return dJXL;
	}

	public void setDJXL(String dJXL) {
		if (this.dJXL != dJXL) {
			this.dJXL = dJXL;
			modify_dJXL = true;
		}
	}

	private String sQR;
	private boolean modify_sQR = false;

	public String getSQR() {
		return sQR;
	}

	public void setSQR(String sQR) {
		if (this.sQR != sQR) {
			this.sQR = sQR;
			modify_sQR = true;
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

	private String bLJD;
	private boolean modify_bLJD = false;

	public String getBLJD() {
		return bLJD;
	}

	public void setBLJD(String bLJD) {
		if (this.bLJD != bLJD) {
			this.bLJD = bLJD;
			modify_bLJD = true;
		}
	}

	private String cASENUM;
	private boolean modify_cASENUM = false;

	public String getCASENUM() {
		return cASENUM;
	}

	public void setCASENUM(String cASENUM) {
		if (this.cASENUM != cASENUM) {
			this.cASENUM = cASENUM;
			modify_cASENUM = true;
		}
	}

	private String qLSDFS;
	private boolean modify_qLSDFS = false;

	public String getQLSDFS() {
		return qLSDFS;
	}

	public void setQLSDFS(String qLSDFS) {
		if (this.qLSDFS != qLSDFS) {
			this.qLSDFS = qLSDFS;
			modify_qLSDFS = true;
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

	private Date tSSJ;
	private boolean modify_tSSJ = false;

	public Date getTSSJ() {
		return tSSJ;
	}

	public void setTSSJ(Date tSSJ) {
		if (this.tSSJ != tSSJ) {
			this.tSSJ = tSSJ;
			modify_tSSJ = true;
		}
	}

	private Integer xMID;
	private boolean modify_xMID = false;

	public Integer getXMID() {
		return xMID;
	}

	public void setXMID(Integer xMID) {
		if (this.xMID != xMID) {
			this.xMID = xMID;
			modify_xMID = true;
		}
	}

	private Byte[] rELATIONID;
	private boolean modify_rELATIONID = false;

	public Byte[] getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(Byte[] rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
			modify_rELATIONID = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_qLLX = false;
		modify_gXLX = false;
		modify_xMMC = false;
		modify_zL = false;
		modify_gXXMBH = false;
		modify_dJDL = false;
		modify_dJXL = false;
		modify_sQR = false;
		modify_sLSJ = false;
		modify_sLRY = false;
		modify_zT = false;
		modify_bLJD = false;
		modify_cASENUM = false;
		modify_qLSDFS = false;
		modify_pROJECT_ID = false;
		modify_tSSJ = false;
		modify_xMID = false;
		modify_rELATIONID = false;
		modify_tSBH=false;
		modify_cQSJ=false;
		modify_cQSJ2=false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_gXLX)
			listStrings.add("gXLX");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_dJDL)
			listStrings.add("dJDL");
		if (!modify_dJXL)
			listStrings.add("dJXL");
		if (!modify_sQR)
			listStrings.add("sQR");
		if (!modify_sLSJ)
			listStrings.add("sLSJ");
		if (!modify_sLRY)
			listStrings.add("sLRY");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_bLJD)
			listStrings.add("bLJD");
		if (!modify_cASENUM)
			listStrings.add("cASENUM");
		if (!modify_qLSDFS)
			listStrings.add("qLSDFS");
		if (!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if (!modify_tSSJ)
			listStrings.add("tSSJ");
		if (!modify_xMID)
			listStrings.add("xMID");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if(modify_tSBH)
			listStrings.add("tSBH");
		if(modify_cQSJ)
			listStrings.add("modify_cQSJ");
		if(modify_cQSJ2)
			listStrings.add("modify_cQSJ2");

		return StringHelper.ListToStrings(listStrings);
	}

	private String tSBH;
	private boolean modify_tSBH = false;

	public String gettSBH() {
		return tSBH;
	}

	public void settSBH(String tSBH) {
		if (this.tSBH != tSBH) {
			this.tSBH = tSBH;
			modify_tSBH = true;
		}
	}

	private Date cQSJ;
	private boolean modify_cQSJ = false;

	public Date getCQSJ() {
		return cQSJ;
	}

	public void setCQSJ(Date cQSJ) {
		if (this.cQSJ != cQSJ) {
			this.cQSJ = cQSJ;
			modify_cQSJ = true;
		}
	}

	private Date cQSJ2;
	private boolean modify_cQSJ2 = false;

	public Date getCQSJ2() {
		return cQSJ2;
	}

	public void setCQSJ2(Date cQSJ2) {
		if (this.cQSJ2 != cQSJ2) {
			this.cQSJ2 = cQSJ2;
			modify_cQSJ2 = true;
		}
	}

}
