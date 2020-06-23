package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity ygdj 
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
public class GenerateYGDJ implements SuperModel<String> {

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

	private String bDCZL;
	private boolean modify_bDCZL = false;

	public String getBDCZL() {
		return bDCZL;
	}

	public void setBDCZL(String bDCZL) {
		if (this.bDCZL != bDCZL) {
			this.bDCZL = bDCZL;
			modify_bDCZL = true;
		}
	}

	private String yWR;
	private boolean modify_yWR = false;

	public String getYWR() {
		return yWR;
	}

	public void setYWR(String yWR) {
		if (this.yWR != yWR) {
			this.yWR = yWR;
			modify_yWR = true;
		}
	}

	private String yWRZJZL;
	private boolean modify_yWRZJZL = false;

	public String getYWRZJZL() {
		return yWRZJZL;
	}

	public void setYWRZJZL(String yWRZJZL) {
		if (this.yWRZJZL != yWRZJZL) {
			this.yWRZJZL = yWRZJZL;
			modify_yWRZJZL = true;
		}
	}

	private String yWRZJH;
	private boolean modify_yWRZJH = false;

	public String getYWRZJH() {
		return yWRZJH;
	}

	public void setYWRZJH(String yWRZJH) {
		if (this.yWRZJH != yWRZJH) {
			this.yWRZJH = yWRZJH;
			modify_yWRZJH = true;
		}
	}

	private String yGDJZL;
	private boolean modify_yGDJZL = false;

	public String getYGDJZL() {
		return yGDJZL;
	}

	public void setYGDJZL(String yGDJZL) {
		if (this.yGDJZL != yGDJZL) {
			this.yGDJZL = yGDJZL;
			modify_yGDJZL = true;
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

	private String fWXZ;
	private boolean modify_fWXZ = false;

	public String getFWXZ() {
		return fWXZ;
	}

	public void setFWXZ(String fWXZ) {
		if (this.fWXZ != fWXZ) {
			this.fWXZ = fWXZ;
			modify_fWXZ = true;
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

	private String sZC;
	private boolean modify_sZC = false;

	public String getSZC() {
		return sZC;
	}

	public void setSZC(String sZC) {
		if (this.sZC != sZC) {
			this.sZC = sZC;
			modify_sZC = true;
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
		modify_ySDM = false;
		modify_yWH = false;
		modify_bDCDYH = false;
		modify_qLID = false;
		modify_dJLX = false;
		modify_dJYY = false;
		modify_bDCDJZMH = false;
		modify_qXDM = false;
		modify_dJJG = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_fJ = false;
		modify_qSZT = false;
		modify_bDCDYID = false;
		modify_gXXMBH = false;
		modify_bDCZL = false;
		modify_yWR = false;
		modify_yWRZJZL = false;
		modify_yWRZJH = false;
		modify_yGDJZL = false;
		modify_tDSYQR = false;
		modify_gHYT = false;
		modify_fWXZ = false;
		modify_fWJG = false;
		modify_sZC = false;
		modify_zCS = false;
		modify_jZMJ = false;
		modify_qDJG = false;
		modify_zSBH = false;
		modify_bZ = false;
		modify_tsbh = false;
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
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_dJYY)
			listStrings.add("dJYY");
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
		if (!modify_bDCZL)
			listStrings.add("bDCZL");
		if (!modify_yWR)
			listStrings.add("yWR");
		if (!modify_yWRZJZL)
			listStrings.add("yWRZJZL");
		if (!modify_yWRZJH)
			listStrings.add("yWRZJH");
		if (!modify_yGDJZL)
			listStrings.add("yGDJZL");
		if (!modify_tDSYQR)
			listStrings.add("tDSYQR");
		if (!modify_gHYT)
			listStrings.add("gHYT");
		if (!modify_fWXZ)
			listStrings.add("fWXZ");
		if (!modify_fWJG)
			listStrings.add("fWJG");
		if (!modify_sZC)
			listStrings.add("sZC");
		if (!modify_zCS)
			listStrings.add("zCS");
		if (!modify_jZMJ)
			listStrings.add("jZMJ");
		if (!modify_qDJG)
			listStrings.add("qDJG");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_tsbh)
		listStrings.add("tsbh");

		return StringHelper.ListToStrings(listStrings);
	}
}
