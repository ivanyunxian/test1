package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-3 
//* ----------------------------------------
//* Internal Entity bdcs_djsf 
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

public class GenerateBDCS_DJSF implements SuperModel<String> {

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

	private String jFRY;
	private boolean modify_jFRY = false;

	public String getJFRY() {
		return jFRY;
	}

	public void setJFRY(String jFRY) {
		if (this.jFRY != jFRY) {
			this.jFRY = jFRY;
			modify_jFRY = true;
		}
	}

	private Date jFRQ;
	private boolean modify_jFRQ = false;

	public Date getJFRQ() {
		return jFRQ;
	}

	public void setJFRQ(Date jFRQ) {
		if (this.jFRQ != jFRQ) {
			this.jFRQ = jFRQ;
			modify_jFRQ = true;
		}
	}

	private String sFKMMC;
	private boolean modify_sFKMMC = false;

	public String getSFKMMC() {
		return sFKMMC;
	}

	public void setSFKMMC(String sFKMMC) {
		if (this.sFKMMC != sFKMMC) {
			this.sFKMMC = sFKMMC;
			modify_sFKMMC = true;
		}
	}

	private String sFEWSF;
	private boolean modify_sFEWSF = false;

	public String getSFEWSF() {
		return sFEWSF;
	}

	public void setSFEWSF(String sFEWSF) {
		if (this.sFEWSF != sFEWSF) {
			this.sFEWSF = sFEWSF;
			modify_sFEWSF = true;
		}
	}

	private Double sFJS;
	private boolean modify_sFJS = false;

	public Double getSFJS() {
		return sFJS;
	}

	public void setSFJS(Double sFJS) {
		if (this.sFJS != sFJS) {
			this.sFJS = sFJS;
			modify_sFJS = true;
		}
	}

	private String sFLX;
	private boolean modify_sFLX = false;

	public String getSFLX() {
		return sFLX;
	}

	public void setSFLX(String sFLX) {
		if (this.sFLX != sFLX) {
			this.sFLX = sFLX;
			modify_sFLX = true;
		}
	}

	private Double ySJE;
	private boolean modify_ySJE = false;

	public Double getYSJE() {
		return ySJE;
	}

	public void setYSJE(Double ySJE) {
		if (this.ySJE != ySJE) {
			this.ySJE = ySJE;
			modify_ySJE = true;
		}
	}

	private Double zKHYSJE;
	private boolean modify_zKHYSJE = false;

	public Double getZKHYSJE() {
		return zKHYSJE;
	}

	public void setZKHYSJE(Double zKHYSJE) {
		if (this.zKHYSJE != zKHYSJE) {
			this.zKHYSJE = zKHYSJE;
			modify_zKHYSJE = true;
		}
	}

	private String sFRY;
	private boolean modify_sFRY = false;

	public String getSFRY() {
		return sFRY;
	}

	public void setSFRY(String sFRY) {
		if (this.sFRY != sFRY) {
			this.sFRY = sFRY;
			modify_sFRY = true;
		}
	}

	private Date sFRQ;
	private boolean modify_sFRQ = false;

	public Date getSFRQ() {
		return sFRQ;
	}

	public void setSFRQ(Date sFRQ) {
		if (this.sFRQ != sFRQ) {
			this.sFRQ = sFRQ;
			modify_sFRQ = true;
		}
	}

	private String fFF;
	private boolean modify_fFF = false;

	public String getFFF() {
		return fFF;
	}

	public void setFFF(String fFF) {
		if (this.fFF != fFF) {
			this.fFF = fFF;
			modify_fFF = true;
		}
	}

	private String sJFFR;
	private boolean modify_sJFFR = false;

	public String getSJFFR() {
		return sJFFR;
	}

	public void setSJFFR(String sJFFR) {
		if (this.sJFFR != sJFFR) {
			this.sJFFR = sJFFR;
			modify_sJFFR = true;
		}
	}

	private String sSJE;
	private boolean modify_sSJE = false;

	public String getSSJE() {
		return sSJE;
	}

	public void setSSJE(String sSJE) {
		if (this.sSJE != sSJE) {
			this.sSJE = sSJE;
			modify_sSJE = true;
		}
	}

	private String sFDW;
	private boolean modify_sFDW = false;

	public String getSFDW() {
		return sFDW;
	}

	public void setSFDW(String sFDW) {
		if (this.sFDW != sFDW) {
			this.sFDW = sFDW;
			modify_sFDW = true;
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

	private Double mJJS;
	private boolean modify_mJJS = false;

	public Double getMJJS() {
		return mJJS;
	}

	public void setMJJS(Double mJJS) {
		if (this.mJJS != mJJS) {
			this.mJJS = mJJS;
			modify_mJJS = true;
		}
	}

	private Double mJZL;
	private boolean modify_mJZL = false;

	public Double getMJZL() {
		return mJZL;
	}

	public void setMJZL(Double mJZL) {
		if (this.mJZL != mJZL) {
			this.mJZL = mJZL;
			modify_mJZL = true;
		}
	}

	private Double sFZL;
	private boolean modify_sFZL = false;

	public Double getSFZL() {
		return sFZL;
	}

	public void setSFZL(Double sFZL) {
		if (this.sFZL != sFZL) {
			this.sFZL = sFZL;
			modify_sFZL = true;
		}
	}

	private Double sFSX;
	private boolean modify_sFSX = false;

	public Double getSFSX() {
		return sFSX;
	}

	public void setSFSX(Double sFSX) {
		if (this.sFSX != sFSX) {
			this.sFSX = sFSX;
			modify_sFSX = true;
		}
	}

	private Double sFBL;
	private boolean modify_sFBL = false;

	public Double getSFBL() {
		return sFBL;
	}

	public void setSFBL(Double sFBL) {
		if (this.sFBL != sFBL) {
			this.sFBL = sFBL;
			modify_sFBL = true;
		}
	}

	private String jFDW;
	private boolean modify_jFDW = false;

	public String getJFDW() {
		return jFDW;
	}

	public void setJFDW(String jFDW) {
		if (this.jFDW != jFDW) {
			this.jFDW = jFDW;
			modify_jFDW = true;
		}
	}

	private String sFDYID;
	private boolean modify_sFDYID = false;

	public String getSFDYID() {
		return sFDYID;
	}

	public void setSFDYID(String sFDYID) {
		if (this.sFDYID != sFDYID) {
			this.sFDYID = sFDYID;
			modify_sFDYID = true;
		}
	}

	private String jSGS;
	private boolean modify_jSGS = false;

	public String getJSGS() {
		return jSGS;
	}

	public void setJSGS(String jSGS) {
		if (this.jSGS != jSGS) {
			this.jSGS = jSGS;
			modify_jSGS = true;
		}
	}

	private String cALTYPE;
	private boolean modify_cALTYPE = false;

	public String getCALTYPE() {
		return cALTYPE;
	}

	public void setCALTYPE(String cALTYPE) {
		if (this.cALTYPE != cALTYPE) {
			this.cALTYPE = cALTYPE;
			modify_cALTYPE = true;
		}
	}

	private String sQLEXP;
	private boolean modify_sQLEXP = false;

	public String getSQLEXP() {
		return sQLEXP;
	}

	public void setSQLEXP(String sQLEXP) {
		if (this.sQLEXP != sQLEXP) {
			this.sQLEXP = sQLEXP;
			modify_sQLEXP = true;
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

	private String xSGS;
	private boolean modify_xSGS = false;

	public String getXSGS() {
		return xSGS;
	}

	public void setXSGS(String xSGS) {
		if (this.xSGS != xSGS) {
			this.xSGS = xSGS;
			modify_xSGS = true;
		}
	}

	private Integer tS;
	private boolean modify_tS = false;

	public Integer getTS() {
		return tS;
	}

	public void setTS(Integer tS) {
		if (this.tS != tS) {
			this.tS = tS;
			modify_tS = true;
		}
	}
	
	private String sFBMMC;
	private boolean modify_sFBMMC = false;

	public String getSFBMMC() {
		return sFBMMC;
	}

	public void setSFBMMC(String sFBMMC) {
		if (this.sFBMMC != sFBMMC) {
			this.sFBMMC = sFBMMC;
			modify_sFBMMC = true;
		}
	}
	
	private String sFJB;
	private boolean modify_sFJB = false;

	public String getSFJB() {
		return sFJB;
	}

	public void setSFJB(String sFJB) {
		if (this.sFJB != sFJB) {
			this.sFJB = sFJB;
			modify_sFJB = true;
		}
	}
	
	//收费环节收费项前添加权利人名称
	private String QLRMC;
	
	public void setQLRMC(String qlrmc) {
		this.QLRMC =  qlrmc;
	}
	
	public String GetQLRMC() {
		return QLRMC;
	}
	

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_yWH = false;
		modify_ySDM = false;
		modify_jFRY = false;
		modify_jFRQ = false;
		modify_sFKMMC = false;
		modify_sFEWSF = false;
		modify_sFJS = false;
		modify_sFLX = false;
		modify_ySJE = false;
		modify_zKHYSJE = false;
		modify_sFRY = false;
		modify_sFRQ = false;
		modify_fFF = false;
		modify_sJFFR = false;
		modify_sSJE = false;
		modify_sFDW = false;
		modify_xMBH = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_mJJS = false;
		modify_mJZL = false;
		modify_sFZL = false;
		modify_sFSX = false;
		modify_sFBL = false;
		modify_jFDW = false;
		modify_sFDYID = false;
		modify_jSGS = false;
		modify_cALTYPE = false;
		modify_sQLEXP = false;
		modify_bZ = false;
		modify_xSGS = false;
		modify_tS = false;
		modify_sFBMMC=false;
		modify_sFJB=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_jFRY)
			listStrings.add("jFRY");
		if (!modify_jFRQ)
			listStrings.add("jFRQ");
		if (!modify_sFKMMC)
			listStrings.add("sFKMMC");
		if (!modify_sFEWSF)
			listStrings.add("sFEWSF");
		if (!modify_sFJS)
			listStrings.add("sFJS");
		if (!modify_sFLX)
			listStrings.add("sFLX");
		if (!modify_ySJE)
			listStrings.add("ySJE");
		if (!modify_zKHYSJE)
			listStrings.add("zKHYSJE");
		if (!modify_sFRY)
			listStrings.add("sFRY");
		if (!modify_sFRQ)
			listStrings.add("sFRQ");
		if (!modify_fFF)
			listStrings.add("fFF");
		if (!modify_sJFFR)
			listStrings.add("sJFFR");
		if (!modify_sSJE)
			listStrings.add("sSJE");
		if (!modify_sFDW)
			listStrings.add("sFDW");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_mJJS)
			listStrings.add("mJJS");
		if (!modify_mJZL)
			listStrings.add("mJZL");
		if (!modify_sFZL)
			listStrings.add("sFZL");
		if (!modify_sFSX)
			listStrings.add("sFSX");
		if (!modify_sFBL)
			listStrings.add("sFBL");
		if (!modify_jFDW)
			listStrings.add("jFDW");
		if (!modify_sFDYID)
			listStrings.add("sFDYID");
		if (!modify_jSGS)
			listStrings.add("jSGS");
		if (!modify_cALTYPE)
			listStrings.add("cALTYPE");
		if (!modify_sQLEXP)
			listStrings.add("sQLEXP");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_xSGS)
			listStrings.add("xSGS");
		if (!modify_tS)
			listStrings.add("tS");
		if(!modify_sFBMMC)
			listStrings.add("sFBMMC");
		if(!modify_sFJB)
			listStrings.add("sFJB");

		return StringHelper.ListToStrings(listStrings);
	}
}
