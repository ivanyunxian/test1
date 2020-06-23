package com.supermap.luzhouothers.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/23 
//* ----------------------------------------
//* Internal Entity qybaxx 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateV_QYBAXX implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
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

	private String hTH;
	private boolean modify_hTH = false;

	public String getHTH() {
		return hTH;
	}

	public void setHTH(String hTH) {
		if (this.hTH != hTH) {
			this.hTH = hTH;
			modify_hTH = true;
		}
	}

	private Date qYRQ;
	private boolean modify_qYRQ = false;

	public Date getQYRQ() {
		return qYRQ;
	}

	public void setQYRQ(Date qYRQ) {
		if (this.qYRQ != qYRQ) {
			this.qYRQ = qYRQ;
			modify_qYRQ = true;
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

	private Double dJ;
	private boolean modify_dJ = false;

	public Double getDJ() {
		return dJ;
	}

	public void setDJ(Double dJ) {
		if (this.dJ != dJ) {
			this.dJ = dJ;
			modify_dJ = true;
		}
	}

	private Double zJ;
	private boolean modify_zJ = false;

	public Double getZJ() {
		return zJ;
	}

	public void setZJ(Double zJ) {
		if (this.zJ != zJ) {
			this.zJ = zJ;
			modify_zJ = true;
		}
	}

	private String qZH;
	private boolean modify_qZH = false;

	public String getQZH() {
		return qZH;
	}

	public void setQZH(String qZH) {
		if (this.qZH != qZH) {
			this.qZH = qZH;
			modify_qZH = true;
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

	private Integer sEQID_W;
	private boolean modify_sEQID_W = false;

	public Integer getSEQID_W() {
		return sEQID_W;
	}

	public void setSEQID_W(Integer sEQID_W) {
		if (this.sEQID_W != sEQID_W) {
			this.sEQID_W = sEQID_W;
			modify_sEQID_W = true;
		}
	}

	private Integer sEQID_R;
	private boolean modify_sEQID_R = false;

	public Integer getSEQID_R() {
		return sEQID_R;
	}

	public void setSEQID_R(Integer sEQID_R) {
		if (this.sEQID_R != sEQID_R) {
			this.sEQID_R = sEQID_R;
			modify_sEQID_R = true;
		}
	}

	private Date cREATEDATETIME;
	private boolean modify_cREATEDATETIME = false;

	public Date getCREATEDATETIME() {
		return cREATEDATETIME;
	}

	public void setCREATEDATETIME(Date cREATEDATETIME) {
		if (this.cREATEDATETIME != cREATEDATETIME) {
			this.cREATEDATETIME = cREATEDATETIME;
			modify_cREATEDATETIME = true;
		}
	}

	private Date lOADDATETIME;
	private boolean modify_lOADDATETIME = false;

	public Date getLOADDATETIME() {
		return lOADDATETIME;
	}

	public void setLOADDATETIME(Date lOADDATETIME) {
		if (this.lOADDATETIME != lOADDATETIME) {
			this.lOADDATETIME = lOADDATETIME;
			modify_lOADDATETIME = true;
		}
	}

	private String fWBM;
	private boolean modify_fWBM = false;

	public String getFWBM() {
		return fWBM;
	}

	public void setFWBM(String fWBM) {
		if (this.fWBM != fWBM) {
			this.fWBM = fWBM;
			modify_fWBM = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_yWH = false;
		modify_dJDL = false;
		modify_dJXL = false;
		modify_hTH = false;
		modify_qYRQ = false;
		modify_gYFS = false;
		modify_dJ = false;
		modify_zJ = false;
		modify_qZH = false;
		modify_sQR = false;
		modify_sLSJ = false;
		modify_sLRY = false;
		modify_zT = false;
		modify_bLJD = false;
		modify_sEQID_W = false;
		modify_sEQID_R = false;
		modify_cREATEDATETIME = false;
		modify_lOADDATETIME = false;
		modify_fWBM = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_dJDL)
			listStrings.add("dJDL");
		if (!modify_dJXL)
			listStrings.add("dJXL");
		if (!modify_hTH)
			listStrings.add("hTH");
		if (!modify_qYRQ)
			listStrings.add("qYRQ");
		if (!modify_gYFS)
			listStrings.add("gYFS");
		if (!modify_dJ)
			listStrings.add("dJ");
		if (!modify_zJ)
			listStrings.add("zJ");
		if (!modify_qZH)
			listStrings.add("qZH");
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
		if (!modify_sEQID_W)
			listStrings.add("sEQID_W");
		if (!modify_sEQID_R)
			listStrings.add("sEQID_R");
		if (!modify_cREATEDATETIME)
			listStrings.add("cREATEDATETIME");
		if (!modify_lOADDATETIME)
			listStrings.add("lOADDATETIME");
		if (!modify_fWBM)
			listStrings.add("fWBM");

		return StringHelper.ListToStrings(listStrings);
	}
}
