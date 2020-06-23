package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/12 
//* ----------------------------------------
//* Internal Entity t_baseworkflow 
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

public class GenerateT_BASEWORKFLOW implements SuperModel<String> {

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

	private String cLASS;
	private boolean modify_cLASS = false;

	public String getCLASS() {
		return cLASS;
	}

	public void setCLASS(String cLASS) {
		if (this.cLASS != cLASS) {
			this.cLASS = cLASS;
			modify_cLASS = true;
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

	private String sELECTORID;
	private boolean modify_sELECTORID = false;

	public String getSELECTORID() {
		return sELECTORID;
	}

	public void setSELECTORID(String sELECTORID) {
		if (this.sELECTORID != sELECTORID) {
			this.sELECTORID = sELECTORID;
			modify_sELECTORID = true;
		}
	}

	private String hANDLERID;
	private boolean modify_hANDLERID = false;

	public String getHANDLERID() {
		return hANDLERID;
	}

	public void setHANDLERID(String hANDLERID) {
		if (this.hANDLERID != hANDLERID) {
			this.hANDLERID = hANDLERID;
			modify_hANDLERID = true;
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

	private String uNITTYPE;
	private boolean modify_uNITTYPE = false;

	public String getUNITTYPE() {
		return uNITTYPE;
	}

	public void setUNITTYPE(String uNITTYPE) {
		if (this.uNITTYPE != uNITTYPE) {
			this.uNITTYPE = uNITTYPE;
			modify_uNITTYPE = true;
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

	private String uNITPAGEJSP;
	private boolean modify_uNITPAGEJSP = false;

	public String getUNITPAGEJSP() {
		return uNITPAGEJSP;
	}

	public void setUNITPAGEJSP(String uNITPAGEJSP) {
		if (this.uNITPAGEJSP != uNITPAGEJSP) {
			this.uNITPAGEJSP = uNITPAGEJSP;
			modify_uNITPAGEJSP = true;
		}
	}

	private String rIGHTSPAGEJSP;
	private boolean modify_rIGHTSPAGEJSP = false;

	public String getRIGHTSPAGEJSP() {
		return rIGHTSPAGEJSP;
	}

	public void setRIGHTSPAGEJSP(String rIGHTSPAGEJSP) {
		if (this.rIGHTSPAGEJSP != rIGHTSPAGEJSP) {
			this.rIGHTSPAGEJSP = rIGHTSPAGEJSP;
			modify_rIGHTSPAGEJSP = true;
		}
	}

	private String bOOKPAGEJSP;
	private boolean modify_bOOKPAGEJSP = false;

	public String getBOOKPAGEJSP() {
		return bOOKPAGEJSP;
	}

	public void setBOOKPAGEJSP(String bOOKPAGEJSP) {
		if (this.bOOKPAGEJSP != bOOKPAGEJSP) {
			this.bOOKPAGEJSP = bOOKPAGEJSP;
			modify_bOOKPAGEJSP = true;
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
	
	private String bUILDINGSELECTTYPE;
	private boolean modify_bUILDINGSELECTTYPE = false;

	public String getBUILDINGSELECTTYPE() {
		return bUILDINGSELECTTYPE;
	}

	public void setBUILDINGSELECTTYPE(String bUILDINGSELECTTYPE) {
		if (this.bUILDINGSELECTTYPE != bUILDINGSELECTTYPE) {
			this.bUILDINGSELECTTYPE = bUILDINGSELECTTYPE;
			modify_bUILDINGSELECTTYPE = true;
		}
	}
	

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_cLASS = false;
		modify_nAME = false;
		modify_sELECTORID = false;
		modify_hANDLERID = false;
		modify_dJLX = false;
		modify_qLLX = false;
		modify_uNITTYPE = false;
		modify_dESCRIPTION = false;
		modify_uNITPAGEJSP = false;
		modify_rIGHTSPAGEJSP = false;
		modify_bOOKPAGEJSP = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_cREATOR = false;
		modify_lASTMODIFIER = false;
		modify_bUILDINGSELECTTYPE = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cLASS)
			listStrings.add("cLASS");
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_sELECTORID)
			listStrings.add("sELECTORID");
		if (!modify_hANDLERID)
			listStrings.add("hANDLERID");
		if (!modify_dJLX)
			listStrings.add("dJLX");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_uNITTYPE)
			listStrings.add("uNITTYPE");
		if (!modify_dESCRIPTION)
			listStrings.add("dESCRIPTION");
		if (!modify_uNITPAGEJSP)
			listStrings.add("uNITPAGEJSP");
		if (!modify_rIGHTSPAGEJSP)
			listStrings.add("rIGHTSPAGEJSP");
		if (!modify_bOOKPAGEJSP)
			listStrings.add("bOOKPAGEJSP");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_cREATOR)
			listStrings.add("cREATOR");
		if (!modify_lASTMODIFIER)
			listStrings.add("lASTMODIFIER");
		if (!modify_bUILDINGSELECTTYPE)
			listStrings.add("bUILDINGSELECTTYPE");

		return StringHelper.ListToStrings(listStrings);
	}
}
