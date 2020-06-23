package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity yydj 
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

public class GenerateYYDJ implements SuperModel<String> {

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

	private String yYSX;
	private boolean modify_yYSX = false;

	public String getYYSX() {
		return yYSX;
	}

	public void setYYSX(String yYSX) {
		if (this.yYSX != yYSX) {
			this.yYSX = yYSX;
			modify_yYSX = true;
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

	private String zXYYYY;
	private boolean modify_zXYYYY = false;

	public String getZXYYYY() {
		return zXYYYY;
	}

	public void setZXYYYY(String zXYYYY) {
		if (this.zXYYYY != zXYYYY) {
			this.zXYYYY = zXYYYY;
			modify_zXYYYY = true;
		}
	}

	private String zXYYDBR;
	private boolean modify_zXYYDBR = false;

	public String getZXYYDBR() {
		return zXYYDBR;
	}

	public void setZXYYDBR(String zXYYDBR) {
		if (this.zXYYDBR != zXYYDBR) {
			this.zXYYDBR = zXYYDBR;
			modify_zXYYDBR = true;
		}
	}

	private Date zXYYDJSJ;
	private boolean modify_zXYYDJSJ = false;

	public Date getZXYYDJSJ() {
		return zXYYDJSJ;
	}

	public void setZXYYDJSJ(Date zXYYDJSJ) {
		if (this.zXYYDJSJ != zXYYDJSJ) {
			this.zXYYDJSJ = zXYYDJSJ;
			modify_zXYYDJSJ = true;
		}
	}

	private String zXYYYWH;
	private boolean modify_zXYYYWH = false;

	public String getZXYYYWH() {
		return zXYYYWH;
	}

	public void setZXYYYWH(String zXYYYWH) {
		if (this.zXYYYWH != zXYYYWH) {
			this.zXYYYWH = zXYYYWH;
			modify_zXYYYWH = true;
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
		modify_ySDM = false;
		modify_yWH = false;
		modify_bDCDYH = false;
		modify_qLID = false;
		modify_yYSX = false;
		modify_bDCDJZMH = false;
		modify_qXDM = false;
		modify_dJJG = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_fJ = false;
		modify_qSZT = false;
		modify_bDCDYID = false;
		modify_gXXMBH = false;
		modify_zXYYYY = false;
		modify_zXYYDBR = false;
		modify_zXYYDJSJ = false;
		modify_zXYYYWH = false;
		modify_zSBH = false;
		modify_bZ = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_yYSX)
			listStrings.add("yYSX");
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
		if (!modify_zXYYYY)
			listStrings.add("zXYYYY");
		if (!modify_zXYYDBR)
			listStrings.add("zXYYDBR");
		if (!modify_zXYYDJSJ)
			listStrings.add("zXYYDJSJ");
		if (!modify_zXYYYWH)
			listStrings.add("zXYYYWH");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bZ)
			listStrings.add("bZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
