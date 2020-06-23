package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/16 
//* ----------------------------------------
//* Internal Entity t_constraint 
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

public class GenerateT_CONSTRAINT implements SuperModel<String> {

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

	private String eXECUTECLASSNAME;
	private boolean modify_eXECUTECLASSNAME = false;

	public String getEXECUTECLASSNAME() {
		return eXECUTECLASSNAME;
	}

	public void setEXECUTECLASSNAME(String eXECUTECLASSNAME) {
		if (this.eXECUTECLASSNAME != eXECUTECLASSNAME) {
			this.eXECUTECLASSNAME = eXECUTECLASSNAME;
			modify_eXECUTECLASSNAME = true;
		}
	}

	private String eXECUTESQL;
	private boolean modify_eXECUTESQL = false;

	public String getEXECUTESQL() {
		return eXECUTESQL;
	}

	public void setEXECUTESQL(String eXECUTESQL) {
		if (this.eXECUTESQL != eXECUTESQL) {
			this.eXECUTESQL = eXECUTESQL;
			modify_eXECUTESQL = true;
		}
	}

	private String sQLRESULTEXP;
	private boolean modify_sQLRESULTEXP = false;

	public String getSQLRESULTEXP() {
		return sQLRESULTEXP;
	}

	public void setSQLRESULTEXP(String sQLRESULTEXP) {
		if (this.sQLRESULTEXP != sQLRESULTEXP) {
			this.sQLRESULTEXP = sQLRESULTEXP;
			modify_sQLRESULTEXP = true;
		}
	}

	private String rESULTTIP;
	private boolean modify_rESULTTIP = false;

	public String getRESULTTIP() {
		return rESULTTIP;
	}

	public void setRESULTTIP(String rESULTTIP) {
		if (this.rESULTTIP != rESULTTIP) {
			this.rESULTTIP = rESULTTIP;
			modify_rESULTTIP = true;
		}
	}

	private String uSERDEFINE;
	private boolean modify_uSERDEFINE = false;

	public String getUSERDEFINE() {
		return uSERDEFINE;
	}

	public void setUSERDEFINE(String uSERDEFINE) {
		if (this.uSERDEFINE != uSERDEFINE) {
			this.uSERDEFINE = uSERDEFINE;
			modify_uSERDEFINE = true;
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

	private String cREATOR;
	private boolean modify_cREATOR = false;

	public String getCREATOR() {
		return cREATOR;
	}

	public void setCREATOR(String cREATOR) {
		if (this.cREATOR != cREATOR) {
			this.cREATOR = cREATOR;
			modify_cREATOR = true;
		}
	}

	private String lASTMODIFIER;
	private boolean modify_lASTMODIFIER = false;

	public String getLASTMODIFIER() {
		return lASTMODIFIER;
	}

	public void setLASTMODIFIER(String lASTMODIFIER) {
		if (this.lASTMODIFIER != lASTMODIFIER) {
			this.lASTMODIFIER = lASTMODIFIER;
			modify_lASTMODIFIER = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_cLASSNAME = false;
		modify_nAME = false;
		modify_dESCRIPTION = false;
		modify_eXECUTETYPE = false;
		modify_eXECUTECLASSNAME = false;
		modify_eXECUTESQL = false;
		modify_sQLRESULTEXP = false;
		modify_rESULTTIP = false;
		modify_uSERDEFINE = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_cREATOR = false;
		modify_lASTMODIFIER = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cLASSNAME)
			listStrings.add("cLASSNAME");
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_dESCRIPTION)
			listStrings.add("dESCRIPTION");
		if (!modify_eXECUTETYPE)
			listStrings.add("eXECUTETYPE");
		if (!modify_eXECUTECLASSNAME)
			listStrings.add("eXECUTECLASSNAME");
		if (!modify_eXECUTESQL)
			listStrings.add("eXECUTESQL");
		if (!modify_sQLRESULTEXP)
			listStrings.add("sQLRESULTEXP");
		if (!modify_rESULTTIP)
			listStrings.add("rESULTTIP");
		if (!modify_uSERDEFINE)
			listStrings.add("uSERDEFINE");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_cREATOR)
			listStrings.add("cREATOR");
		if (!modify_lASTMODIFIER)
			listStrings.add("lASTMODIFIER");

		return StringHelper.ListToStrings(listStrings);
	}
}
