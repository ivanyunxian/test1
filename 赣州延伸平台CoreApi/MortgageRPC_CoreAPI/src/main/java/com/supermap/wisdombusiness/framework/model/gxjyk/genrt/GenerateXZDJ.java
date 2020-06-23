package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity xzdj 
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


public class GenerateXZDJ implements SuperModel<String> {

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

	private String cFLX;
	private boolean modify_cFLX = false;

	public String getCFLX() {
		return cFLX;
	}

	public void setCFLX(String cFLX) {
		if (this.cFLX != cFLX) {
			this.cFLX = cFLX;
			modify_cFLX = true;
		}
	}

	private String cFJG;
	private boolean modify_cFJG = false;

	public String getCFJG() {
		return cFJG;
	}

	public void setCFJG(String cFJG) {
		if (this.cFJG != cFJG) {
			this.cFJG = cFJG;
			modify_cFJG = true;
		}
	}

	private String cFWH;
	private boolean modify_cFWH = false;

	public String getCFWH() {
		return cFWH;
	}

	public void setCFWH(String cFWH) {
		if (this.cFWH != cFWH) {
			this.cFWH = cFWH;
			modify_cFWH = true;
		}
	}

	private String cFWJ;
	private boolean modify_cFWJ = false;

	public String getCFWJ() {
		return cFWJ;
	}

	public void setCFWJ(String cFWJ) {
		if (this.cFWJ != cFWJ) {
			this.cFWJ = cFWJ;
			modify_cFWJ = true;
		}
	}

	private String cFFW;
	private boolean modify_cFFW = false;

	public String getCFFW() {
		return cFFW;
	}

	public void setCFFW(String cFFW) {
		if (this.cFFW != cFFW) {
			this.cFFW = cFFW;
			modify_cFFW = true;
		}
	}

	private String jFJG;
	private boolean modify_jFJG = false;

	public String getJFJG() {
		return jFJG;
	}

	public void setJFJG(String jFJG) {
		if (this.jFJG != jFJG) {
			this.jFJG = jFJG;
			modify_jFJG = true;
		}
	}

	private String jFWH;
	private boolean modify_jFWH = false;

	public String getJFWH() {
		return jFWH;
	}

	public void setJFWH(String jFWH) {
		if (this.jFWH != jFWH) {
			this.jFWH = jFWH;
			modify_jFWH = true;
		}
	}

	private String jFWJ;
	private boolean modify_jFWJ = false;

	public String getJFWJ() {
		return jFWJ;
	}

	public void setJFWJ(String jFWJ) {
		if (this.jFWJ != jFWJ) {
			this.jFWJ = jFWJ;
			modify_jFWJ = true;
		}
	}

	private Date cFQSSJ;
	private boolean modify_cFQSSJ = false;

	public Date getCFQSSJ() {
		return cFQSSJ;
	}

	public void setCFQSSJ(Date cFQSSJ) {
		if (this.cFQSSJ != cFQSSJ) {
			this.cFQSSJ = cFQSSJ;
			modify_cFQSSJ = true;
		}
	}

	private Date cFJSSJ;
	private boolean modify_cFJSSJ = false;

	public Date getCFJSSJ() {
		return cFJSSJ;
	}

	public void setCFJSSJ(Date cFJSSJ) {
		if (this.cFJSSJ != cFJSSJ) {
			this.cFJSSJ = cFJSSJ;
			modify_cFJSSJ = true;
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

	private String jFYWH;
	private boolean modify_jFYWH = false;

	public String getJFYWH() {
		return jFYWH;
	}

	public void setJFYWH(String jFYWH) {
		if (this.jFYWH != jFYWH) {
			this.jFYWH = jFYWH;
			modify_jFYWH = true;
		}
	}

	private String jFDBR;
	private boolean modify_jFDBR = false;

	public String getJFDBR() {
		return jFDBR;
	}

	public void setJFDBR(String jFDBR) {
		if (this.jFDBR != jFDBR) {
			this.jFDBR = jFDBR;
			modify_jFDBR = true;
		}
	}

	private String jFDJSJ;
	private boolean modify_jFDJSJ = false;

	public String getJFDJSJ() {
		return jFDJSJ;
	}

	public void setJFDJSJ(String jFDJSJ) {
		if (this.jFDJSJ != jFDJSJ) {
			this.jFDJSJ = jFDJSJ;
			modify_jFDJSJ = true;
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


	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_yWH = false;
		modify_qLID = false;
		modify_cFLX = false;
		modify_cFJG = false;
		modify_cFWH = false;
		modify_cFWJ = false;
		modify_cFFW = false;
		modify_jFJG = false;
		modify_jFWH = false;
		modify_jFWJ = false;
		modify_cFQSSJ = false;
		modify_cFJSSJ = false;
		modify_qXDM = false;
		modify_dJJG = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_jFYWH = false;
		modify_jFDBR = false;
		modify_jFDJSJ = false;
		modify_fJ = false;
		modify_qSZT = false;
		modify_bDCDYID = false;
		modify_gXXMBH = false;
		modify_zSBH = false;
		modify_bZ = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_cFLX)
			listStrings.add("cFLX");
		if (!modify_cFJG)
			listStrings.add("cFJG");
		if (!modify_cFWH)
			listStrings.add("cFWH");
		if (!modify_cFWJ)
			listStrings.add("cFWJ");
		if (!modify_cFFW)
			listStrings.add("cFFW");
		if (!modify_jFJG)
			listStrings.add("jFJG");
		if (!modify_jFWH)
			listStrings.add("jFWH");
		if (!modify_jFWJ)
			listStrings.add("jFWJ");
		if (!modify_cFQSSJ)
			listStrings.add("cFQSSJ");
		if (!modify_cFJSSJ)
			listStrings.add("cFJSSJ");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_dJJG)
			listStrings.add("dJJG");
		if (!modify_dBR)
			listStrings.add("dBR");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if (!modify_jFYWH)
			listStrings.add("jFYWH");
		if (!modify_jFDBR)
			listStrings.add("jFDBR");
		if (!modify_jFDJSJ)
			listStrings.add("jFDJSJ");
		if (!modify_fJ)
			listStrings.add("fJ");
		if (!modify_qSZT)
			listStrings.add("qSZT");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bZ)
			listStrings.add("bZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
