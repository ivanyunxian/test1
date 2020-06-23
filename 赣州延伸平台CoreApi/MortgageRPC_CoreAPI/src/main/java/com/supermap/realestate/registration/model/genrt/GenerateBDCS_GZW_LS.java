package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/27 
//* ----------------------------------------
//* Internal Entity bdcs_gzw_ls 
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

public class GenerateBDCS_GZW_LS implements SuperModel<String> {

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

	private String xMBH;
	private boolean modify_xMBH = false;

	public String getXMBH() {
		return xMBH;
	}

	public void setXMBH(String xMBH) {
		if (this.xMBH != xMBH) {
			this.xMBH = xMBH;
			modify_xMBH = true;
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

	private String gZWMC;
	private boolean modify_gZWMC = false;

	public String getGZWMC() {
		return gZWMC;
	}

	public void setGZWMC(String gZWMC) {
		if (this.gZWMC != gZWMC) {
			this.gZWMC = gZWMC;
			modify_gZWMC = true;
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

	private String tDHYSYQR;
	private boolean modify_tDHYSYQR = false;

	public String getTDHYSYQR() {
		return tDHYSYQR;
	}

	public void setTDHYSYQR(String tDHYSYQR) {
		if (this.tDHYSYQR != tDHYSYQR) {
			this.tDHYSYQR = tDHYSYQR;
			modify_tDHYSYQR = true;
		}
	}

	private Double tDHYSYMJ;
	private boolean modify_tDHYSYMJ = false;

	public Double getTDHYSYMJ() {
		return tDHYSYMJ;
	}

	public void setTDHYSYMJ(Double tDHYSYMJ) {
		if (this.tDHYSYMJ != tDHYSYMJ) {
			this.tDHYSYMJ = tDHYSYMJ;
			modify_tDHYSYMJ = true;
		}
	}

	private String gJZWLX;
	private boolean modify_gJZWLX = false;

	public String getGJZWLX() {
		return gJZWLX;
	}

	public void setGJZWLX(String gJZWLX) {
		if (this.gJZWLX != gJZWLX) {
			this.gJZWLX = gJZWLX;
			modify_gJZWLX = true;
		}
	}

	private String gJZWGHYT;
	private boolean modify_gJZWGHYT = false;

	public String getGJZWGHYT() {
		return gJZWGHYT;
	}

	public void setGJZWGHYT(String gJZWGHYT) {
		if (this.gJZWGHYT != gJZWGHYT) {
			this.gJZWGHYT = gJZWGHYT;
			modify_gJZWGHYT = true;
		}
	}

	private Date jGSJ;
	private boolean modify_jGSJ = false;

	public Date getJGSJ() {
		return jGSJ;
	}

	public void setJGSJ(Date jGSJ) {
		if (this.jGSJ != jGSJ) {
			this.jGSJ = jGSJ;
			modify_jGSJ = true;
		}
	}

	private String mJDW;
	private boolean modify_mJDW = false;

	public String getMJDW() {
		return mJDW;
	}

	public void setMJDW(String mJDW) {
		if (this.mJDW != mJDW) {
			this.mJDW = mJDW;
			modify_mJDW = true;
		}
	}

	private double mJ;
	private boolean modify_mJ = false;

	public double getMJ() {
		return mJ;
	}

	public void setMJ(Double mJ) {
		if (this.mJ != mJ) {
			this.mJ = mJ;
			modify_mJ = true;
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

	private String dCXMID;
	private boolean modify_dCXMID = false;

	public String getDCXMID() {
		return dCXMID;
	}

	public void setDCXMID(String dCXMID) {
		if (this.dCXMID != dCXMID) {
			this.dCXMID = dCXMID;
			modify_dCXMID = true;
		}
	}

	private String yXBZ;
	private boolean modify_yXBZ = false;

	public String getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(String yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	private Date cREATETIME;
	private boolean modify_cREATETIME = false;

	public Date getCREATETIME() {
		return cREATETIME;
	}

	public void setCREATETIME(Date cREATETIME) {
		if (this.cREATETIME != cREATETIME) {
			this.cREATETIME = cREATETIME;
			modify_cREATETIME = true;
		}
	}

	private Date mODIFYTIME;
	private boolean modify_mODIFYTIME = false;

	public Date getMODIFYTIME() {
		return mODIFYTIME;
	}

	public void setMODIFYTIME(Date mODIFYTIME) {
		if (this.mODIFYTIME != mODIFYTIME) {
			this.mODIFYTIME = mODIFYTIME;
			modify_mODIFYTIME = true;
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

	private Double gJZWMJ;
	private boolean modify_gJZWMJ = false;

	public Double getGJZWMJ() {
		return gJZWMJ;
	}

	public void setGJZWMJ(Double gJZWMJ) {
		if (this.gJZWMJ != gJZWMJ) {
			this.gJZWMJ = gJZWMJ;
			modify_gJZWMJ = true;
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

	private byte[] gJZWPMT;
	private boolean modify_gJZWPMT = false;

	public byte[] getGJZWPMT() {
		return gJZWPMT;
	}

	public void setGJZWPMT(byte[] gJZWPMT) {
		if (this.gJZWPMT != gJZWPMT) {
			this.gJZWPMT = gJZWPMT;
			modify_gJZWPMT = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_ySDM = false;
		modify_xMBH = false;
		modify_bDCDYH = false;
		modify_zDBDCDYID = false;
		modify_zDDM = false;
		modify_gZWMC = false;
		modify_zL = false;
		modify_tDHYSYQR = false;
		modify_tDHYSYMJ = false;
		modify_gJZWLX = false;
		modify_gJZWGHYT = false;
		modify_jGSJ = false;
		modify_mJDW = false;
		modify_mJ = false;
		modify_zT = false;
		modify_dCXMID = false;
		modify_yXBZ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_yWH = false;
		modify_qLLX = false;
		modify_gJZWMJ = false;
		modify_qXDM = false;
		modify_gJZWPMT = false;
		modify_qSZT = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_gZWMC)
			listStrings.add("gZWMC");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_tDHYSYQR)
			listStrings.add("tDHYSYQR");
		if (!modify_tDHYSYMJ)
			listStrings.add("tDHYSYMJ");
		if (!modify_gJZWLX)
			listStrings.add("gJZWLX");
		if (!modify_gJZWGHYT)
			listStrings.add("gJZWGHYT");
		if (!modify_jGSJ)
			listStrings.add("jGSJ");
		if (!modify_mJDW)
			listStrings.add("mJDW");
		if (!modify_mJ)
			listStrings.add("mJ");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_gJZWMJ)
			listStrings.add("gJZWMJ");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_gJZWPMT)
			listStrings.add("gJZWPMT");
		if (!modify_qSZT)
			listStrings.add("qSZT");

		return StringHelper.ListToStrings(listStrings);
	}
}
