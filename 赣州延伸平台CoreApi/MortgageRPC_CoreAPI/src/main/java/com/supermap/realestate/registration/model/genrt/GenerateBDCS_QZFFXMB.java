package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2017-03-21 
//* ----------------------------------------
//* Internal Entity bdcs_qzffxmb 
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

public class GenerateBDCS_QZFFXMB implements SuperModel<String> {

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
	
	private String xMID;
	private boolean modify_xMID = false;

	public String getXMID() {
		return xMID;
	}

	public void setXMID(String xMID) {
		if (this.xMID != xMID) {
			this.xMID = xMID;
			modify_xMID = true;
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

	private String fFFS;
	private boolean modify_fFFS = false;

	public String getFFFS() {
		return fFFS;
	}

	public void setFFFS(String fFFS) {
		if (this.fFFS != fFFS) {
			this.fFFS = fFFS;
			modify_fFFS = true;
		}
	}

	private String lQRY;
	private boolean modify_lQRY = false;

	public String getLQRY() {
		return lQRY;
	}

	public void setLQRY(String lQRY) {
		if (this.lQRY != lQRY) {
			this.lQRY = lQRY;
			modify_lQRY = true;
		}
	}

	private String lQRYID;
	private boolean modify_lQRYID = false;

	public String getLQRYID() {
		return lQRYID;
	}

	public void setLQRYID(String lQRYID) {
		if (this.lQRYID != lQRYID) {
			this.lQRYID = lQRYID;
			modify_lQRYID = true;
		}
	}
	
	private String lQKS;
	private boolean modify_lQKS = false;

	public String getLQKS() {
		return lQKS;
	}

	public void setLQKS(String lQKS) {
		if (this.lQKS != lQKS) {
			this.lQKS = lQKS;
			modify_lQKS = true;
		}
	}

	private String lQKSID;
	private boolean modify_lQKSID = false;

	public String getLQKSID() {
		return lQKSID;
	}

	public void setLQKSID(String lQKSID) {
		if (this.lQKSID != lQKSID) {
			this.lQKSID = lQKSID;
			modify_lQKSID = true;
		}
	}

	private String fFYY;
	private boolean modify_fFYY = false;

	public String getFFYY() {
		return fFYY;
	}

	public void setFFYY(String fFYY) {
		if (this.fFYY != fFYY) {
			this.fFYY = fFYY;
			modify_fFYY = true;
		}
	}

	private String qZLX;
	private boolean modify_qZLX = false;

	public String getQZLX() {
		return qZLX;
	}

	public void setQZLX(String qZLX) {
		if (this.qZLX != qZLX) {
			this.qZLX = qZLX;
			modify_qZLX = true;
		}
	}

	private String qZZL;
	private boolean modify_qZZL = false;

	public String getQZZL() {
		return qZZL;
	}

	public void setQZZL(String qZZL) {
		if (this.qZZL != qZZL) {
			this.qZZL = qZZL;
			modify_qZZL = true;
		}
	}

	private Long qSQZBH;
	private boolean modify_qSQZBH = false;

	public Long getQSQZBH() {
		return qSQZBH;
	}

	public void setQSQZBH(Long qSQZBH) {
		if (this.qSQZBH != qSQZBH) {
			this.qSQZBH = qSQZBH;
			modify_qSQZBH = true;
		}
	}

	private Long jSQZBH;
	private boolean modify_jSQZBH = false;

	public Long getJSQZBH() {
		return jSQZBH;
	}

	public void setJSQZBH(Long jSQZBH) {
		if (this.jSQZBH != jSQZBH) {
			this.jSQZBH = jSQZBH;
			modify_jSQZBH = true;
		}
	}

	private String cJRY;
	private boolean modify_cJRY = false;

	public String getCJRY() {
		return cJRY;
	}

	public void setCJRY(String cJRY) {
		if (this.cJRY != cJRY) {
			this.cJRY = cJRY;
			modify_cJRY = true;
		}
	}

	private Date cJSJ;
	private boolean modify_cJSJ = false;

	public Date getCJSJ() {
		return cJSJ;
	}

	public void setCJSJ(Date cJSJ) {
		if (this.cJSJ != cJSJ) {
			this.cJSJ = cJSJ;
			modify_cJSJ = true;
		}
	}

	private String sFRK;
	private boolean modify_sFRK = false;

	public String getSFRK() {
		return sFRK;
	}

	public void setSFRK(String sFRK) {
		if (this.sFRK != sFRK) {
			this.sFRK = sFRK;
			modify_sFRK = true;
		}
	}

	private Date rKSJ;
	private boolean modify_rKSJ = false;

	public Date getRKSJ() {
		return rKSJ;
	}

	public void setRKSJ(Date rKSJ) {
		if (this.rKSJ != rKSJ) {
			this.rKSJ = rKSJ;
			modify_rKSJ = true;
		}
	}

	private String rKRY;
	private boolean modify_rKRY = false;

	public String getRKRY() {
		return rKRY;
	}

	public void setRKRY(String rKRY) {
		if (this.rKRY != rKRY) {
			this.rKRY = rKRY;
			modify_rKRY = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMMC = false;
		modify_fFFS = false;
		modify_lQRY = false;
		modify_lQRYID = false;
		modify_lQKS = false;
		modify_lQKSID = false;
		modify_fFYY = false;
		modify_qZLX = false;
		modify_qZZL = false;
		modify_qSQZBH = false;
		modify_jSQZBH = false;
		modify_cJRY = false;
		modify_cJSJ = false;
		modify_sFRK = false;
		modify_rKSJ = false;
		modify_rKRY = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_fFFS)
			listStrings.add("fFFS");
		if (!modify_lQRY)
			listStrings.add("lQRY");
		if (!modify_lQRYID)
			listStrings.add("lQRYID");
		if (!modify_lQKS)
			listStrings.add("lQKS");
		if (!modify_lQKSID)
			listStrings.add("lQKSID");
		if (!modify_fFYY)
			listStrings.add("fFYY");
		if (!modify_qZLX)
			listStrings.add("qZLX");
		if (!modify_qZZL)
			listStrings.add("qZZL");
		if (!modify_qSQZBH)
			listStrings.add("qSQZBH");
		if (!modify_jSQZBH)
			listStrings.add("jSQZBH");
		if (!modify_cJRY)
			listStrings.add("cJRY");
		if (!modify_cJSJ)
			listStrings.add("cJSJ");
		if (!modify_sFRK)
			listStrings.add("sFRK");
		if (!modify_rKSJ)
			listStrings.add("rKSJ");
		if (!modify_rKRY)
			listStrings.add("rKRY");

		return StringHelper.ListToStrings(listStrings);
	}
}
