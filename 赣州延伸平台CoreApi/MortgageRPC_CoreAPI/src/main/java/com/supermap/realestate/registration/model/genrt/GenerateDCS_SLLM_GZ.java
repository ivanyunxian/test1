package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-3-5 
//* ----------------------------------------
//* Internal Entity bdcs_sllm_gz 
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

public class GenerateDCS_SLLM_GZ implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id =SuperHelper.GeneratePrimaryKey();
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

	private String zYSZ;
	private boolean modify_zYSZ = false;

	public String getZYSZ() {
		return zYSZ;
	}

	public void setZYSZ(String zYSZ) {
		if (this.zYSZ != zYSZ) {
			this.zYSZ = zYSZ;
			modify_zYSZ = true;
		}
	}

	private Integer zS;
	private boolean modify_zS = false;

	public Integer getZS() {
		return zS;
	}

	public void setZS(Integer zS) {
		if (this.zS != zS) {
			this.zS = zS;
			modify_zS = true;
		}
	}

	private String lZ;
	private boolean modify_lZ = false;

	public String getLZ() {
		return lZ;
	}

	public void setLZ(String lZ) {
		if (this.lZ != lZ) {
			this.lZ = lZ;
			modify_lZ = true;
		}
	}

	private String qY;
	private boolean modify_qY = false;

	public String getQY() {
		return qY;
	}

	public void setQY(String qY) {
		if (this.qY != qY) {
			this.qY = qY;
			modify_qY = true;
		}
	}

	private Integer zLND;
	private boolean modify_zLND = false;

	public Integer getZLND() {
		return zLND;
	}

	public void setZLND(Integer zLND) {
		if (this.zLND != zLND) {
			this.zLND = zLND;
			modify_zLND = true;
		}
	}

	private String lB;
	private boolean modify_lB = false;

	public String getLB() {
		return lB;
	}

	public void setLB(String lB) {
		if (this.lB != lB) {
			this.lB = lB;
			modify_lB = true;
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

	private String xDM;
	private boolean modify_xDM = false;

	public String getXDM() {
		return xDM;
	}

	public void setXDM(String xDM) {
		if (this.xDM != xDM) {
			this.xDM = xDM;
			modify_xDM = true;
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

	private String dJQDM;
	private boolean modify_dJQDM = false;

	public String getDJQDM() {
		return dJQDM;
	}

	public void setDJQDM(String dJQDM) {
		if (this.dJQDM != dJQDM) {
			this.dJQDM = dJQDM;
			modify_dJQDM = true;
		}
	}

	private String dJZQDM;
	private boolean modify_dJZQDM = false;

	public String getDJZQDM() {
		return dJZQDM;
	}

	public void setDJZQDM(String dJZQDM) {
		if (this.dJZQDM != dJZQDM) {
			this.dJZQDM = dJZQDM;
			modify_dJZQDM = true;
		}
	}

	private String zH;
	private boolean modify_zH = false;

	public String getZH() {
		return zH;
	}

	public void setZH(String zH) {
		if (this.zH != zH) {
			this.zH = zH;
			modify_zH = true;
		}
	}

	private String qXMC;
	private boolean modify_qXMC = false;

	public String getQXMC() {
		return qXMC;
	}

	public void setQXMC(String qXMC) {
		if (this.qXMC != qXMC) {
			this.qXMC = qXMC;
			modify_qXMC = true;
		}
	}

	private String dJQMC;
	private boolean modify_dJQMC = false;

	public String getDJQMC() {
		return dJQMC;
	}

	public void setDJQMC(String dJQMC) {
		if (this.dJQMC != dJQMC) {
			this.dJQMC = dJQMC;
			modify_dJQMC = true;
		}
	}

	private String dJZQMC;
	private boolean modify_dJZQMC = false;

	public String getDJZQMC() {
		return dJZQMC;
	}

	public void setDJZQMC(String dJZQMC) {
		if (this.dJZQMC != dJZQMC) {
			this.dJZQMC = dJZQMC;
			modify_dJZQMC = true;
		}
	}

	private String zM;
	private boolean modify_zM = false;

	public String getZM() {
		return zM;
	}

	public void setZM(String zM) {
		if (this.zM != zM) {
			this.zM = zM;
			modify_zM = true;
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

	private String dCJS;
	private boolean modify_dCJS = false;

	public String getDCJS() {
		return dCJS;
	}

	public void setDCJS(String dCJS) {
		if (this.dCJS != dCJS) {
			this.dCJS = dCJS;
			modify_dCJS = true;
		}
	}

	private String dCR;
	private boolean modify_dCR = false;

	public String getDCR() {
		return dCR;
	}

	public void setDCR(String dCR) {
		if (this.dCR != dCR) {
			this.dCR = dCR;
			modify_dCR = true;
		}
	}

	private Date dCRQ;
	private boolean modify_dCRQ = false;

	public Date getDCRQ() {
		return dCRQ;
	}

	public void setDCRQ(Date dCRQ) {
		if (this.dCRQ != dCRQ) {
			this.dCRQ = dCRQ;
			modify_dCRQ = true;
		}
	}

	private String sHYJ;
	private boolean modify_sHYJ = false;

	public String getSHYJ() {
		return sHYJ;
	}

	public void setSHYJ(String sHYJ) {
		if (this.sHYJ != sHYJ) {
			this.sHYJ = sHYJ;
			modify_sHYJ = true;
		}
	}

	private String sHR;
	private boolean modify_sHR = false;

	public String getSHR() {
		return sHR;
	}

	public void setSHR(String sHR) {
		if (this.sHR != sHR) {
			this.sHR = sHR;
			modify_sHR = true;
		}
	}

	private Date sHRQ;
	private boolean modify_sHRQ = false;

	public Date getSHRQ() {
		return sHRQ;
	}

	public void setSHRQ(Date sHRQ) {
		if (this.sHRQ != sHRQ) {
			this.sHRQ = sHRQ;
			modify_sHRQ = true;
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

	private String dJZT;
	private boolean modify_dJZT = false;

	public String getDJZT() {
		return dJZT;
	}

	public void setDJZT(String dJZT) {
		if (this.dJZT != dJZT) {
			this.dJZT = dJZT;
			modify_dJZT = true;
		}
	}

	private String rELATIONID;
	private boolean modify_rELATIONID = false;

	public String getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
			modify_rELATIONID = true;
		}
	}

	private String sYQLX;
	private boolean modify_sYQLX = false;

	public String getSYQLX() {
		return sYQLX;
	}

	public void setSYQLX(String sYQLX) {
		if (this.sYQLX != sYQLX) {
			this.sYQLX = sYQLX;
			modify_sYQLX = true;
		}
	}

	private String zDTZM;
	private boolean modify_zDTZM = false;

	public String getZDTZM() {
		return zDTZM;
	}

	public void setZDTZM(String zDTZM) {
		if (this.zDTZM != zDTZM) {
			this.zDTZM = zDTZM;
			modify_zDTZM = true;
		}
	}

	private String tFH;
	private boolean modify_tFH = false;

	public String getTFH() {
		return tFH;
	}

	public void setTFH(String tFH) {
		if (this.tFH != tFH) {
			this.tFH = tFH;
			modify_tFH = true;
		}
	}

	private String qLXZ;
	private boolean modify_qLXZ = false;

	public String getQLXZ() {
		return qLXZ;
	}

	public void setQLXZ(String qLXZ) {
		if (this.qLXZ != qLXZ) {
			this.qLXZ = qLXZ;
			modify_qLXZ = true;
		}
	}

	private String zDSZD;
	private boolean modify_zDSZD = false;

	public String getZDSZD() {
		return zDSZD;
	}

	public void setZDSZD(String zDSZD) {
		if (this.zDSZD != zDSZD) {
			this.zDSZD = zDSZD;
			modify_zDSZD = true;
		}
	}

	private String zDSZN;
	private boolean modify_zDSZN = false;

	public String getZDSZN() {
		return zDSZN;
	}

	public void setZDSZN(String zDSZN) {
		if (this.zDSZN != zDSZN) {
			this.zDSZN = zDSZN;
			modify_zDSZN = true;
		}
	}

	private String zDSZX;
	private boolean modify_zDSZX = false;

	public String getZDSZX() {
		return zDSZX;
	}

	public void setZDSZX(String zDSZX) {
		if (this.zDSZX != zDSZX) {
			this.zDSZX = zDSZX;
			modify_zDSZX = true;
		}
	}

	private String zDSZB;
	private boolean modify_zDSZB = false;

	public String getZDSZB() {
		return zDSZB;
	}

	public void setZDSZB(String zDSZB) {
		if (this.zDSZB != zDSZB) {
			this.zDSZB = zDSZB;
			modify_zDSZB = true;
		}
	}

	private String tDYT;
	private boolean modify_tDYT = false;

	public String getTDYT() {
		return tDYT;
	}

	public void setTDYT(String tDYT) {
		if (this.tDYT != tDYT) {
			this.tDYT = tDYT;
			modify_tDYT = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_xMBH = false;
		modify_ySDM = false;
		modify_zDBDCDYID = false;
		modify_zDDM = false;
		modify_zL = false;
		modify_zYSZ = false;
		modify_zS = false;
		modify_lZ = false;
		modify_qY = false;
		modify_zLND = false;
		modify_lB = false;
		modify_xB = false;
		modify_xDM = false;
		modify_sYQMJ = false;
		modify_qXDM = false;
		modify_dJQDM = false;
		modify_dJZQDM = false;
		modify_zH = false;
		modify_qXMC = false;
		modify_dJQMC = false;
		modify_dJZQMC = false;
		modify_zM = false;
		modify_zT = false;
		modify_yXBZ = false;
		modify_dCXMID = false;
		modify_dCJS = false;
		modify_dCR = false;
		modify_dCRQ = false;
		modify_sHYJ = false;
		modify_sHR = false;
		modify_sHRQ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_dJZT = false;
		modify_rELATIONID = false;
		modify_sYQLX = false;
		modify_zDTZM = false;
		modify_tFH = false;
		modify_qLXZ = false;
		modify_zDSZD = false;
		modify_zDSZN = false;
		modify_zDSZX = false;
		modify_zDSZB = false;
		modify_tDYT = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_zYSZ)
			listStrings.add("zYSZ");
		if (!modify_zS)
			listStrings.add("zS");
		if (!modify_lZ)
			listStrings.add("lZ");
		if (!modify_qY)
			listStrings.add("qY");
		if (!modify_zLND)
			listStrings.add("zLND");
		if (!modify_lB)
			listStrings.add("lB");
		if (!modify_xB)
			listStrings.add("xB");
		if (!modify_xDM)
			listStrings.add("xDM");
		if (!modify_sYQMJ)
			listStrings.add("sYQMJ");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_dJQDM)
			listStrings.add("dJQDM");
		if (!modify_dJZQDM)
			listStrings.add("dJZQDM");
		if (!modify_zH)
			listStrings.add("zH");
		if (!modify_qXMC)
			listStrings.add("qXMC");
		if (!modify_dJQMC)
			listStrings.add("dJQMC");
		if (!modify_dJZQMC)
			listStrings.add("dJZQMC");
		if (!modify_zM)
			listStrings.add("zM");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_dCJS)
			listStrings.add("dCJS");
		if (!modify_dCR)
			listStrings.add("dCR");
		if (!modify_dCRQ)
			listStrings.add("dCRQ");
		if (!modify_sHYJ)
			listStrings.add("sHYJ");
		if (!modify_sHR)
			listStrings.add("sHR");
		if (!modify_sHRQ)
			listStrings.add("sHRQ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_dJZT)
			listStrings.add("dJZT");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_sYQLX)
			listStrings.add("sYQXZ");
		if (!modify_zDTZM)
			listStrings.add("zDTZM");
		if (!modify_tFH)
			listStrings.add("tFH");
		if (!modify_qLXZ)
			listStrings.add("qLXZ");
		if (!modify_zDSZD)
			listStrings.add("zDSZD");
		if (!modify_zDSZN)
			listStrings.add("zDSZN");
		if (!modify_zDSZX)
			listStrings.add("zDSZX");
		if (!modify_zDSZB)
			listStrings.add("zDSZB");
		if (!modify_tDYT)
			listStrings.add("tDYT");

		return StringHelper.ListToStrings(listStrings);
	}
}
