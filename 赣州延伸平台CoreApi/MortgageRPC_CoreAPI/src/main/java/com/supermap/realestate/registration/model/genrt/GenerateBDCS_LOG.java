package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/7/28 
//* ----------------------------------------
//* Internal Entity bdcs_log 
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

public class GenerateBDCS_LOG implements SuperModel<String>  {

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

	private String sTAFF;
	private boolean modify_sTAFF = false;

	public String getSTAFF() {
		return sTAFF;
	}

	public void setSTAFF(String sTAFF) {
		if (this.sTAFF != sTAFF) {
			this.sTAFF = sTAFF;
			modify_sTAFF = true;
		}
	}

	private String dEPT_CODE;
	private boolean modify_dEPT_CODE = false;

	public String getDEPT_CODE() {
		return dEPT_CODE;
	}

	public void setDEPT_CODE(String dEPT_CODE) {
		if (this.dEPT_CODE != dEPT_CODE) {
			this.dEPT_CODE = dEPT_CODE;
			modify_dEPT_CODE = true;
		}
	}

	private String dEPT_NAME;
	private boolean modify_dEPT_NAME = false;

	public String getDEPT_NAME() {
		return dEPT_NAME;
	}

	public void setDEPT_NAME(String dEPT_NAME) {
		if (this.dEPT_NAME != dEPT_NAME) {
			this.dEPT_NAME = dEPT_NAME;
			modify_dEPT_NAME = true;
		}
	}

	private String fILENAME;
	private boolean modify_fILENAME = false;

	public String getFILENAME() {
		return fILENAME;
	}

	public void setFILENAME(String fILENAME) {
		if (this.fILENAME != fILENAME) {
			this.fILENAME = fILENAME;
			modify_fILENAME = true;
		}
	}

	private Date oPERATE_TIME;
	private boolean modify_oPERATE_TIME = false;

	public Date getOPERATE_TIME() {
		return oPERATE_TIME;
	}

	public void setOPERATE_TIME(Date oPERATE_TIME) {
		if (this.oPERATE_TIME != oPERATE_TIME) {
			this.oPERATE_TIME = oPERATE_TIME;
			modify_oPERATE_TIME = true;
		}
	}

	private String iSSUCCESS;
	private boolean modify_iSSUCCESS = false;

	public String getISSUCCESS() {
		return iSSUCCESS;
	}

	public void setISSUCCESS(String iSSUCCESS) {
		if (this.iSSUCCESS != iSSUCCESS) {
			this.iSSUCCESS = iSSUCCESS;
			modify_iSSUCCESS = true;
		}
	}

	private Date fEEDBACKTIME;
	private boolean modify_fEEDBACKTIME = false;

	public Date getFEEDBACKTIME() {
		return fEEDBACKTIME;
	}

	public void setFEEDBACKTIME(Date fEEDBACKTIME) {
		if (this.fEEDBACKTIME != fEEDBACKTIME) {
			this.fEEDBACKTIME = fEEDBACKTIME;
			modify_fEEDBACKTIME = true;
		}
	}

	private String fILEPATH;
	private boolean modify_fILEPATH = false;

	public String getFILEPATH() {
		return fILEPATH;
	}

	public void setFILEPATH(String fILEPATH) {
		if (this.fILEPATH != fILEPATH) {
			this.fILEPATH = fILEPATH;
			modify_fILEPATH = true;
		}
	}

	private String cONTENT;
	private boolean modify_cONTENT = false;

	public String getCONTENT() {
		return cONTENT;
	}

	public void setCONTENT(String cONTENT) {
		if (this.cONTENT != cONTENT) {
			this.cONTENT = cONTENT;
			modify_cONTENT = true;
		}
	}

	private String cOMMENTS;
	private boolean modify_cOMMENTS = false;

	public String getCOMMENTS() {
		return cOMMENTS;
	}

	public void setCOMMENTS(String cOMMENTS) {
		if (this.cOMMENTS != cOMMENTS) {
			this.cOMMENTS = cOMMENTS;
			modify_cOMMENTS = true;
		}
	}

	private String dETAIL;
	private boolean modify_dETAIL = false;

	public String getDETAIL() {
		return dETAIL;
	}

	public void setDETAIL(String dETAIL) {
		if (this.dETAIL != dETAIL) {
			this.dETAIL = dETAIL;
			modify_dETAIL = true;
		}
	}

	private String tO_DETPCODE;
	private boolean modify_tO_DETPCODE = false;

	public String getTO_DETPCODE() {
		return tO_DETPCODE;
	}

	public void setTO_DETPCODE(String tO_DETPCODE) {
		if (this.tO_DETPCODE != tO_DETPCODE) {
			this.tO_DETPCODE = tO_DETPCODE;
			modify_tO_DETPCODE = true;
		}
	}

	private String tO_DEPTNAME;
	private boolean modify_tO_DEPTNAME = false;

	public String getTO_DEPTNAME() {
		return tO_DEPTNAME;
	}

	public void setTO_DEPTNAME(String tO_DEPTNAME) {
		if (this.tO_DEPTNAME != tO_DEPTNAME) {
			this.tO_DEPTNAME = tO_DEPTNAME;
			modify_tO_DEPTNAME = true;
		}
	}

	private String xMLID;
	private boolean modify_xMLID = false;

	public String getXMLID() {
		return xMLID;
	}

	public void setXMLID(String xMLID) {
		if (this.xMLID != xMLID) {
			this.xMLID = xMLID;
			modify_xMLID = true;
		}
	}  
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_pROJECT_ID = false;
		modify_sTAFF = false;
		modify_dEPT_CODE = false;
		modify_dEPT_NAME = false;
		modify_fILENAME = false;
		modify_oPERATE_TIME = false;
		modify_iSSUCCESS = false;
		modify_fEEDBACKTIME = false;
		modify_fILEPATH = false;
		modify_cONTENT = false;
		modify_cOMMENTS = false;
		modify_dETAIL = false;
		modify_tO_DETPCODE = false;
		modify_tO_DEPTNAME = false;
		modify_xMLID = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if (!modify_sTAFF)
			listStrings.add("sTAFF");
		if (!modify_dEPT_CODE)
			listStrings.add("dEPT_CODE");
		if (!modify_dEPT_NAME)
			listStrings.add("dEPT_NAME");
		if (!modify_fILENAME)
			listStrings.add("fILENAME");
		if (!modify_oPERATE_TIME)
			listStrings.add("oPERATE_TIME");
		if (!modify_iSSUCCESS)
			listStrings.add("iSSUCCESS");
		if (!modify_fEEDBACKTIME)
			listStrings.add("fEEDBACKTIME");
		if (!modify_fILEPATH)
			listStrings.add("fILEPATH");
		if (!modify_cONTENT)
			listStrings.add("cONTENT");
		if (!modify_cOMMENTS)
			listStrings.add("cOMMENTS");
		if (!modify_dETAIL)
			listStrings.add("dETAIL");
		if (!modify_tO_DETPCODE)
			listStrings.add("tO_DETPCODE");
		if (!modify_tO_DEPTNAME)
			listStrings.add("tO_DEPTNAME");
		if (!modify_xMLID)
			listStrings.add("xMLID");

		return StringHelper.ListToStrings(listStrings);
	}
}
