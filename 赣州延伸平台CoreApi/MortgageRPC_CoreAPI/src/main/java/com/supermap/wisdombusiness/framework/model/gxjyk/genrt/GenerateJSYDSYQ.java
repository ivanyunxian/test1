package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity jsydsyq 
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

public class GenerateJSYDSYQ implements SuperModel<String> {

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

	private Date sYQQSSJ;
	private boolean modify_sYQQSSJ = false;

	public Date getSYQQSSJ() {
		return sYQQSSJ;
	}

	public void setSYQQSSJ(Date sYQQSSJ) {
		if (this.sYQQSSJ != sYQQSSJ) {
			this.sYQQSSJ = sYQQSSJ;
			modify_sYQQSSJ = true;
		}
	}

	private Date sYQJSSJ;
	private boolean modify_sYQJSSJ = false;

	public Date getSYQJSSJ() {
		return sYQJSSJ;
	}

	public void setSYQJSSJ(Date sYQJSSJ) {
		if (this.sYQJSSJ != sYQJSSJ) {
			this.sYQJSSJ = sYQJSSJ;
			modify_sYQJSSJ = true;
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

	private Integer qSZT;
	private boolean modify_qSZT = false;

	public Integer getQSZT() {
		return qSZT;
	}

	public void setQSZT(Integer qSZT) {
		if (this.qSZT != qSZT) {
			this.qSZT = qSZT;
			modify_qSZT = true;
		}
	}

	private Double qDJG;
	private boolean modify_qDJG = false;

	public Double getQDJG() {
		return qDJG;
	}

	public void setQDJG(Double qDJG) {
		if (this.qDJG != qDJG) {
			this.qDJG = qDJG;
			modify_qDJG = true;
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


	public void resetModifyState() {
		modify_id = false;
		modify_ySDM = false;
		modify_bDCDYH = false;
		modify_yWH = false;
		modify_qLLX = false;
		modify_dJLX = false;
		modify_dJYY = false;
		modify_sYQQSSJ = false;
		modify_sYQJSSJ = false;
		modify_bDCQZH = false;
		modify_qXDM = false;
		modify_dJJG = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_fJ = false;
		modify_qSZT = false;
		modify_qDJG = false;
		modify_qLID = false;
		modify_bDCDYID = false;
		modify_gXXMBH = false;
		modify_zSBH = false;
		modify_bZ = false;
		modify_zDDM = false;
		modify_sYQMJ = false;
		modify_gXLX = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_dJYY)
			listStrings.add("dJYY");
		if (!modify_sYQQSSJ)
			listStrings.add("sYQQSSJ");
		if (!modify_sYQJSSJ)
			listStrings.add("sYQJSSJ");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
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
		if (!modify_qDJG)
			listStrings.add("qDJG");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_sYQMJ)
			listStrings.add("sYQMJ");
		if (!modify_gXLX)
			listStrings.add("gXLX");

		return StringHelper.ListToStrings(listStrings);
	}
}
