package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-8-15 
//* ----------------------------------------
//* Internal Entity bdcs_constraint 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_CONSTRAINT implements SuperModel<String> {

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

	private String nAME;
	private boolean modify_nAME = false;

	public String getNAME() {
		return nAME;
	}

	public void setNAME(String nAME) {
		if (this.nAME != nAME) {
			this.nAME = nAME;
			modify_nAME = true;
		}
	}

	private String cODE;
	private boolean modify_cODE = false;

	public String getCODE() {
		return cODE;
	}

	public void setCODE(String cODE) {
		if (this.cODE != cODE) {
			this.cODE = cODE;
			modify_cODE = true;
		}
	}

	private String cLASSNAME;
	private boolean modify_cLASSNAME = false;

	public String getCLASSNAME() {
		return cLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		if (this.cLASSNAME != cLASSNAME) {
			this.cLASSNAME = cLASSNAME;
			modify_cLASSNAME = true;
		}
	}

	private String dESCRIPTION;
	private boolean modify_dESCRIPTION = false;

	public String getDESCRIPTION() {
		return dESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		if (this.dESCRIPTION != dESCRIPTION) {
			this.dESCRIPTION = dESCRIPTION;
			modify_dESCRIPTION = true;
		}
	}

	private String eXECUTETYPE;
	private boolean modify_eXECUTETYPE = false;

	public String getEXECUTETYPE() {
		return eXECUTETYPE;
	}

	public void setEXECUTETYPE(String eXECUTETYPE) {
		if (this.eXECUTETYPE != eXECUTETYPE) {
			this.eXECUTETYPE = eXECUTETYPE;
			modify_eXECUTETYPE = true;
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

	private String rESULTEXP;
	private boolean modify_rESULTEXP = false;

	public String getRESULTEXP() {
		return rESULTEXP;
	}

	public void setRESULTEXP(String rESULTEXP) {
		if (this.rESULTEXP != rESULTEXP) {
			this.rESULTEXP = rESULTEXP;
			modify_rESULTEXP = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_nAME = false;
		modify_cODE = false;
		modify_cLASSNAME = false;
		modify_dESCRIPTION = false;
		modify_eXECUTETYPE = false;
		modify_sQLEXP = false;
		modify_rESULTEXP = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_cODE)
			listStrings.add("cODE");
		if (!modify_cLASSNAME)
			listStrings.add("cLASSNAME");
		if (!modify_dESCRIPTION)
			listStrings.add("dESCRIPTION");
		if (!modify_eXECUTETYPE)
			listStrings.add("eXECUTETYPE");
		if (!modify_sQLEXP)
			listStrings.add("sQLEXP");
		if (!modify_rESULTEXP)
			listStrings.add("rESULTEXP");

		return StringHelper.ListToStrings(listStrings);
	}
}
