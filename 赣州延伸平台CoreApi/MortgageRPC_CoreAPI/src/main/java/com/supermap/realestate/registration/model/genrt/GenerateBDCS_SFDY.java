package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-8-31 
//* ----------------------------------------
//* Internal Entity bdcs_sfdy 
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

public class GenerateBDCS_SFDY implements SuperModel<String> {

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

	private String sFDLMC;
	private boolean modify_sFDLMC = false;

	public String getSFDLMC() {
		return sFDLMC;
	}

	public void setSFDLMC(String sFDLMC) {
		if (this.sFDLMC != sFDLMC) {
			this.sFDLMC = sFDLMC;
			modify_sFDLMC = true;
		}
	}

	private String sFXLMC;
	private boolean modify_sFXLMC = false;

	public String getSFXLMC() {
		return sFXLMC;
	}

	public void setSFXLMC(String sFXLMC) {
		if (this.sFXLMC != sFXLMC) {
			this.sFXLMC = sFXLMC;
			modify_sFXLMC = true;
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

	private Double zLFYSX;
	private boolean modify_zLFYSX = false;

	public Double getZLFYSX() {
		return zLFYSX;
	}

	public void setZLFYSX(Double zLFYSX) {
		if (this.zLFYSX != zLFYSX) {
			this.zLFYSX = zLFYSX;
			modify_zLFYSX = true;
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

	private Date cREAT_TIME;
	private boolean modify_cREAT_TIME = false;

	public Date getCREAT_TIME() {
		return cREAT_TIME;
	}

	public void setCREAT_TIME(Date cREAT_TIME) {
		if (this.cREAT_TIME != cREAT_TIME) {
			this.cREAT_TIME = cREAT_TIME;
			modify_cREAT_TIME = true;
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
	
	private String cACSQL;
	private boolean modify_cACSQL = false;

	public String getCACSQL() {
		return cACSQL;
	}

	public void setCACSQL(String cACSQL) {
		if (this.cACSQL != cACSQL) {
			this.cACSQL = cACSQL;
			modify_cACSQL = true;
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
	
	private String sYMBOL;
	private boolean modify_sYMBOL = false;

	public String getSYMBOL() {
		return sYMBOL;
	}

	public void setSYMBOL(String sYMBOL) {
		if (this.sYMBOL != sYMBOL) {
			this.sYMBOL = sYMBOL;
			modify_sYMBOL = true;
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
	
	private String tJBZ;
	private boolean modify_tJBZ = false;

	public String getTJBZ() {
		return tJBZ;
	}

	public void setTJBZ(String tJBZ) {
		if (this.tJBZ != tJBZ) {
			this.tJBZ = tJBZ;
			modify_tJBZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_sFDLMC = false;
		modify_sFXLMC = false;
		modify_sFKMMC = false;
		modify_sFLX = false;
		modify_sFJS = false;
		modify_mJJS = false;
		modify_mJZL = false;
		modify_sFZL = false;
		modify_zLFYSX = false;
		modify_sFBL = false;
		modify_jSGS = false;
		modify_cREAT_TIME = false;
		modify_sFDW = false;
		modify_cALTYPE = false;
		modify_sQLEXP = false;
		modify_cACSQL = false;
		modify_bZ = false;
		modify_sYMBOL = false;
		modify_sFBMMC=false;
		modify_tJBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_sFDLMC)
			listStrings.add("sFDLMC");
		if (!modify_sFXLMC)
			listStrings.add("sFXLMC");
		if (!modify_sFKMMC)
			listStrings.add("sFKMMC");
		if (!modify_sFLX)
			listStrings.add("sFLX");
		if (!modify_sFJS)
			listStrings.add("sFJS");
		if (!modify_mJJS)
			listStrings.add("mJJS");
		if (!modify_mJZL)
			listStrings.add("mJZL");
		if (!modify_sFZL)
			listStrings.add("sFZL");
		if (!modify_zLFYSX)
			listStrings.add("zLFYSX");
		if (!modify_sFBL)
			listStrings.add("sFBL");
		if (!modify_jSGS)
			listStrings.add("jSGS");
		if (!modify_cREAT_TIME)
			listStrings.add("cREAT_TIME");
		if (!modify_sFDW)
			listStrings.add("sFDW");
		if (!modify_cALTYPE)
			listStrings.add("cALTYPE");
		if (!modify_sQLEXP)
			listStrings.add("sQLEXP");
		if (!modify_cACSQL)
			listStrings.add("cACSQL");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_sYMBOL)
			listStrings.add("sYMBOL");
		if(!modify_sFBMMC)
			listStrings.add("sFBMMC");
		if(!modify_tJBZ)
			listStrings.add("tJBZ");

		return StringHelper.ListToStrings(listStrings);
	}

}
