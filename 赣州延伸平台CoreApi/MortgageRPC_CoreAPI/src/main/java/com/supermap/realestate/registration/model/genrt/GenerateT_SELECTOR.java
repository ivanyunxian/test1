package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/12 
//* ----------------------------------------
//* Internal Entity t_selector 
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

public class GenerateT_SELECTOR implements SuperModel<String> {

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

	private String sELECTBDCDY;
	private boolean modify_sELECTBDCDY = false;

	public String getSELECTBDCDY() {
		return sELECTBDCDY;
	}

	public void setSELECTBDCDY(String sELECTBDCDY) {
		if (this.sELECTBDCDY != sELECTBDCDY) {
			this.sELECTBDCDY = sELECTBDCDY;
			modify_sELECTBDCDY = true;
		}
	}

	private String sELECTQL;
	private boolean modify_sELECTQL = false;

	public String getSELECTQL() {
		return sELECTQL;
	}

	public void setSELECTQL(String sELECTQL) {
		if (this.sELECTQL != sELECTQL) {
			this.sELECTQL = sELECTQL;
			modify_sELECTQL = true;
		}
	}

	private String sELECTQLLX;
	private boolean modify_sELECTQLLX = false;

	public String getSELECTQLLX() {
		return sELECTQLLX;
	}

	public void setSELECTQLLX(String sELECTQLLX) {
		if (this.sELECTQLLX != sELECTQLLX) {
			this.sELECTQLLX = sELECTQLLX;
			modify_sELECTQLLX = true;
		}
	}

	private String bDCDYLX;
	private boolean modify_bDCDYLX = false;

	public String getBDCDYLX() {
		return bDCDYLX;
	}

	public void setBDCDYLX(String bDCDYLX) {
		if (this.bDCDYLX != bDCDYLX) {
			this.bDCDYLX = bDCDYLX;
			modify_bDCDYLX = true;
		}
	}

	private String lY;
	private boolean modify_lY = false;

	public String getLY() {
		return lY;
	}

	public void setLY(String lY) {
		if (this.lY != lY) {
			this.lY = lY;
			modify_lY = true;
		}
	}

	private String cONDITION;
	private boolean modify_cONDITION = false;

	public String getCONDITION() {
		return cONDITION;
	}

	public void setCONDITION(String cONDITION) {
		if (this.cONDITION != cONDITION) {
			this.cONDITION = cONDITION;
			modify_cONDITION = true;
		}
	}

	private String sINGLESELECT;
	private boolean modify_sINGLESELECT = false;

	public String getSINGLESELECT() {
		return sINGLESELECT;
	}

	public void setSINGLESELECT(String sINGLESELECT) {
		if (this.sINGLESELECT != sINGLESELECT) {
			this.sINGLESELECT = sINGLESELECT;
			modify_sINGLESELECT = true;
		}
	}

	private String iDFIELDNAME;
	private boolean modify_iDFIELDNAME = false;

	public String getIDFIELDNAME() {
		return iDFIELDNAME;
	}

	public void setIDFIELDNAME(String iDFIELDNAME) {
		if (this.iDFIELDNAME != iDFIELDNAME) {
			this.iDFIELDNAME = iDFIELDNAME;
			modify_iDFIELDNAME = true;
		}
	}

	private String uSECONFIGSQL;
	private boolean modify_uSECONFIGSQL = false;

	public String getUSECONFIGSQL() {
		return uSECONFIGSQL;
	}

	public void setUSECONFIGSQL(String uSECONFIGSQL) {
		if (this.uSECONFIGSQL != uSECONFIGSQL) {
			this.uSECONFIGSQL = uSECONFIGSQL;
			modify_uSECONFIGSQL = true;
		}
	}

	private String cONFIGSQL;
	private boolean modify_cONFIGSQL = false;

	public String getCONFIGSQL() {
		return cONFIGSQL;
	}

	public void setCONFIGSQL(String cONFIGSQL) {
		if (this.cONFIGSQL != cONFIGSQL) {
			this.cONFIGSQL = cONFIGSQL;
			modify_cONFIGSQL = true;
		}
	}

	private String dEFAULTSELECTFIRT;
	private boolean modify_dEFAULTSELECTFIRT = false;

	public String getDEFAULTSELECTFIRT() {
		return dEFAULTSELECTFIRT;
	}

	public void setDEFAULTSELECTFIRT(String dEFAULTSELECTFIRT) {
		if (this.dEFAULTSELECTFIRT != dEFAULTSELECTFIRT) {
			this.dEFAULTSELECTFIRT = dEFAULTSELECTFIRT;
			modify_dEFAULTSELECTFIRT = true;
		}
	}

	private String sHOWDETAILALTERSELECT;
	private boolean modify_sHOWDETAILALTERSELECT = false;

	public String getSHOWDETAILALTERSELECT() {
		return sHOWDETAILALTERSELECT;
	}

	public void setSHOWDETAILALTERSELECT(String sHOWDETAILALTERSELECT) {
		if (this.sHOWDETAILALTERSELECT != sHOWDETAILALTERSELECT) {
			this.sHOWDETAILALTERSELECT = sHOWDETAILALTERSELECT;
			modify_sHOWDETAILALTERSELECT = true;
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
		modify_nAME = false;
		modify_sELECTBDCDY = false;
		modify_sELECTQL = false;
		modify_sELECTQLLX = false;
		modify_bDCDYLX = false;
		modify_lY = false;
		modify_cONDITION = false;
		modify_sINGLESELECT = false;
		modify_iDFIELDNAME = false;
		modify_uSECONFIGSQL = false;
		modify_cONFIGSQL = false;
		modify_dEFAULTSELECTFIRT = false;
		modify_sHOWDETAILALTERSELECT = false;
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
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_sELECTBDCDY)
			listStrings.add("sELECTBDCDY");
		if (!modify_sELECTQL)
			listStrings.add("sELECTQL");
		if (!modify_sELECTQLLX)
			listStrings.add("sELECTQLLX");
		if (!modify_bDCDYLX)
			listStrings.add("bDCDYLX");
		if (!modify_lY)
			listStrings.add("lY");
		if (!modify_cONDITION)
			listStrings.add("cONDITION");
		if (!modify_sINGLESELECT)
			listStrings.add("sINGLESELECT");
		if (!modify_iDFIELDNAME)
			listStrings.add("iDFIELDNAME");
		if (!modify_uSECONFIGSQL)
			listStrings.add("uSECONFIGSQL");
		if (!modify_cONFIGSQL)
			listStrings.add("cONFIGSQL");
		if (!modify_dEFAULTSELECTFIRT)
			listStrings.add("dEFAULTSELECTFIRT");
		if (!modify_sHOWDETAILALTERSELECT)
			listStrings.add("sHOWDETAILALTERSELECT");
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
