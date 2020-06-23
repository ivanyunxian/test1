package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-26 
//* ----------------------------------------
//* Internal Entity bdcs_xmxx 
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

public class GeneratePUSHFAIL implements SuperModel<String> {

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


	private String yWLSH;
	private boolean modify_yWLSH = false;

	public String getYWLSH() {
		return yWLSH;
	}

	public void setYWLSH(String yWLSH) {
		if (this.yWLSH != yWLSH) {
			this.yWLSH = yWLSH;
			modify_yWLSH = true;
		}
	}

	private String CASENUM;
	private boolean modify_CASENUM = false;

	public String getCASENUM() {
		return CASENUM;
	}

	public void setCASENUM(String CASENUM) {
		if (this.CASENUM != CASENUM) {
			this.CASENUM = CASENUM;
			modify_CASENUM = true;
		}
	}
	private String BLJD;
	private boolean modify_BLJD = false;

	public String getBLJD() {
		return BLJD;
	}

	public void setBLJD(String BLJD) {
		if (this.BLJD != BLJD) {
			this.BLJD = BLJD;
			modify_BLJD = true;
		}
	}
	private String SBLX;
	private boolean modify_SBLX = false;

	public String getSBLX() {
		return SBLX;
	}

	public void setSBLX(String SBLX) {
		if (this.SBLX != SBLX) {
			this.SBLX = SBLX;
			modify_SBLX = true;
		}
	}
	private String FAILCAUSE;
	private boolean modify_FAILCAUSE = false;

	public String getFAILCAUSE() {
		return FAILCAUSE;
	}

	public void setFAILCAUSE(String FAILCAUSE) {
		if (this.FAILCAUSE != FAILCAUSE) {
			this.FAILCAUSE = FAILCAUSE;
			modify_FAILCAUSE = true;
		}
	}
	private Date TSSJ;
	private boolean modify_TSSJ = false;

	public Date getTSSJ() {
		return TSSJ;
	}

	public void setTSSJ(Date TSSJ) {
		if (this.TSSJ != TSSJ) {
			this.TSSJ = TSSJ;
			modify_TSSJ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_pROJECT_ID = false;
		modify_xMMC = false;
		modify_dJLX = false;
		modify_qLLX = false;
		modify_sLRY = false;
		modify_sLSJ = false;
		modify_yWLSH = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_sLRY)
			listStrings.add("sLRY");
		if (!modify_sLSJ)
			listStrings.add("sLSJ");
		if (!modify_yWLSH)
			listStrings.add("yWLSH");

		return StringHelper.ListToStrings(listStrings);
	}
}
