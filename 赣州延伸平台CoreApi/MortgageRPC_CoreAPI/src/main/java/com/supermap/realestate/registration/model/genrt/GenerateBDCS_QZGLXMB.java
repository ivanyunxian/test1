package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Internal Entity bdcs_qzglxmb 
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

public class GenerateBDCS_QZGLXMB implements SuperModel<String> {

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

	private String xMLX;
	private boolean modify_xMLX = false;

	public String getXMLX() {
		return xMLX;
	}

	public void setXMLX(String xMLX) {
		if (this.xMLX != xMLX) {
			this.xMLX = xMLX;
			modify_xMLX = true;
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

	private String sFYX;
	private boolean modify_sFYX = false;

	public String getSFYX() {
		return sFYX;
	}

	public void setSFYX(String sFYX) {
		if (this.sFYX != sFYX) {
			this.sFYX = sFYX;
			modify_sFYX = true;
		}
	}

	private Date sXSJ;
	private boolean modify_sXSJ = false;

	public Date getSXSJ() {
		return sXSJ;
	}

	public void setSXSJ(Date sXSJ) {
		if (this.sXSJ != sXSJ) {
			this.sXSJ = sXSJ;
			modify_sXSJ = true;
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
	
	private String lQZHRY;
	private boolean modify_lQZHRY = false;

	public String getLQZHRY() {
		return lQZHRY;
	}

	public void setLQZHRY(String lQZHRY) {
		if (this.lQZHRY != lQZHRY) {
			this.lQZHRY = lQZHRY;
			modify_lQZHRY = true;
		}
	}
	
	private String zSZT;
	private boolean modify_zSZT = false;

	public String getZSZT() {
		return zSZT;
	}
	
	public void setZSZT(String zSZT) {
		if (this.zSZT != zSZT) {
			this.zSZT = zSZT;
			modify_zSZT = true;
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
	
	private String qZLY;
	private boolean modify_qZLY = false;

	public String getQZLY() {
		return qZLY;
	}
	
	public void setQZLY(String qZLY) {
		if (this.qZLY != qZLY) {
			this.qZLY = qZLY;
			modify_qZLY = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMMC = false;
		modify_xMLX = false;
		modify_cJRY = false;
		modify_cJSJ = false;
		modify_sFRK = false;
		modify_rKSJ = false;
		modify_sFYX = false;
		modify_sXSJ = false;
		modify_qZLX = false;
		modify_qSQZBH = false;
		modify_jSQZBH = false;
		modify_lQZHRY = false;
		modify_zSZT = false;
		modify_qZZL = false;
		modify_rKRY = false;
		modify_qZLY = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_xMLX)
			listStrings.add("xMLX");
		if (!modify_cJRY)
			listStrings.add("cJRY");
		if (!modify_cJSJ)
			listStrings.add("cJSJ");
		if (!modify_sFRK)
			listStrings.add("sFRK");
		if (!modify_rKSJ)
			listStrings.add("rKSJ");
		if (!modify_sFYX)
			listStrings.add("sFYX");
		if (!modify_sXSJ)
			listStrings.add("sXSJ");
		if (!modify_qZLX)
			listStrings.add("qZLX");
		if (!modify_qSQZBH)
			listStrings.add("qSQZBH");
		if (!modify_jSQZBH)
			listStrings.add("jSQZBH");
		if (!modify_lQZHRY)
			listStrings.add("lQZHRY");
		if (!modify_zSZT)
			listStrings.add("zSZT");
		if(!modify_qZZL)
			listStrings.add("qZZL");
		if(!modify_rKRY)
			listStrings.add("rKRY");
		if(!modify_qZLY)
			listStrings.add("qZLY");

		return StringHelper.ListToStrings(listStrings);
	}
}
