package com.supermap.luzhouothers.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Internal Entity bdc_v_compact_ownerinfo 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

public class GenerateV_COMPACT_OWNERINFO{

	private Integer id;
	private boolean modify_id = false;

	public Integer getId() {
		if (!modify_id && id == null)
		{
			modify_id = true;
		}
		return id;
	}

	public void setId(Integer id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String oWNERID;
	public String getOWNERID() {
		return oWNERID;
	}

	public void setOWNERID(String oWNERID) {
		if (this.oWNERID != oWNERID) {
			this.oWNERID = oWNERID;
		}
	}

	private String oWNER_NAME;
	public String getOWNER_NAME() {
		return oWNER_NAME;
	}

	public void setOWNER_NAME(String oWNER_NAME) {
		if (this.oWNER_NAME != oWNER_NAME) {
			this.oWNER_NAME = oWNER_NAME;
		}
	}

	private Date bIRTHDAY;
	public Date getBIRTHDAY() {
		return bIRTHDAY;
	}

	public void setBIRTHDAY(Date bIRTHDAY) {
		if (this.bIRTHDAY != bIRTHDAY) {
			this.bIRTHDAY = bIRTHDAY;
		}
	}

	private String iDTYPE;
	public String getIDTYPE() {
		return iDTYPE;
	}

	public void setIDTYPE(String iDTYPE) {
		if (this.iDTYPE != iDTYPE) {
			this.iDTYPE = iDTYPE;
		}
	}

	private String iDNO;
	public String getIDNO() {
		return iDNO;
	}

	public void setIDNO(String iDNO) {
		if (this.iDNO != iDNO) {
			this.iDNO = iDNO;
		}
	}

	private String bUYERATTR;
	public String getBUYERATTR() {
		return bUYERATTR;
	}

	public void setBUYERATTR(String bUYERATTR) {
		if (this.bUYERATTR != bUYERATTR) {
			this.bUYERATTR = bUYERATTR;
		}
	}

	private String aDDRESS;
	public String getADDRESS() {
		return aDDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		if (this.aDDRESS != aDDRESS) {
			this.aDDRESS = aDDRESS;
		}
	}

	private Integer oWNER_TYPE;
	public Integer getOWNER_TYPE() {
		return oWNER_TYPE;
	}

	public void setOWNER_TYPE(Integer oWNER_TYPE) {
		if (this.oWNER_TYPE != oWNER_TYPE) {
			this.oWNER_TYPE = oWNER_TYPE;
		}
	}

	public void resetModifyState() {
		modify_id = false;
	}

}
