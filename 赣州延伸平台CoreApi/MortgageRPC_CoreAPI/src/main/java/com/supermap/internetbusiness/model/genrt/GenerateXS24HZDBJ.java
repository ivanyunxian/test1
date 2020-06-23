package com.supermap.internetbusiness.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-20 
//* ----------------------------------------
//* Internal Entity bdcs_zs_xz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateXS24HZDBJ implements SuperModel<String> {

	private String bSM;
	private boolean modify_bSM = false;

	@Override
	public String getId() {
		if (!modify_bSM && bSM == null)
		{
			bSM = SuperHelper.GeneratePrimaryKey();
			modify_bSM = true;
		}
		return bSM;
	}

	@Override
	public void setId(String bSM) {
		if (this.bSM != bSM) {
			this.bSM = bSM;
			modify_bSM = true;
		}
	}

	private String zWLSH;
	private boolean modify_zWLSH = false;

	public String getZWLSH() {
		return zWLSH;
	}

	public void setZWLSH(String zWLSH) {
		if (this.zWLSH != zWLSH) {
			this.zWLSH = zWLSH;
			modify_zWLSH = true;
		}
	}

	private String dJLSH;
	private boolean modify_dJLSH = false;

	public String getDJLSH() {
		return dJLSH;
	}

	public void setDJLSH(String dJLSH) {
		if (this.dJLSH != dJLSH) {
			this.dJLSH = dJLSH;
			modify_dJLSH = true;
		}
	}
	
	private Date dQSJ;
	private boolean modify_dQSJ = false;

	public Date getDQSJ() {
		return dQSJ;
	}

	public void setDQSJ(Date dQSJ) {
		if (this.dQSJ != dQSJ) {
			this.dQSJ = dQSJ;
			modify_dQSJ = true;
		}
	}
	
	private String sFDB;
	private boolean modify_sFDB = false;

	public String getSFDB() {
		return sFDB;
	}

	public void setSFDB(String sFDB) {
		if (this.sFDB != sFDB) {
			this.sFDB = sFDB;
			modify_sFDB = true;
		}
	}

	private String dQZT;
	private boolean modify_dQZT = false;

	public String getDQZT() {
		return dQZT;
	}

	public void setDQZT(String dQZT) {
		if (this.dQZT != dQZT) {
			this.dQZT = dQZT;
			modify_dQZT = true;
		}
	}
	
	private Integer dQCS;
	private boolean modify_dQCS = false;
	
	public Integer getDQCS() {
		return dQCS;
	}
	
	public void setDQCS(Integer dQCS) {
		if (this.dQCS != dQCS) {
			this.dQCS = dQCS;
			modify_dQCS = true;
		}
	}

	private Date sCDQSJ;
	private boolean modify_sCDQSJ = false;

	public Date getSCDQSJ() {
		return sCDQSJ;
	}

	public void setSCDQSJ(Date sCDQSJ) {
		if (this.sCDQSJ != sCDQSJ) {
			this.sCDQSJ = sCDQSJ;
			modify_sCDQSJ = true;
		}
	}
	
	private String dQSBYY;
	private boolean modify_dQSBYY = false;
	
	public String getDQSBYY() {
		return dQSBYY;
	}
	
	public void setDQSBYY(String dQSBYY) {
		if (this.dQSBYY != dQSBYY) {
			this.dQSBYY = dQSBYY;
			modify_dQSBYY = true;
		}
	}	

	private String sFXXBL;
	private boolean modify_sFXXBL = false;
	
	public String getSFXXBL() {
		return sFXXBL;
	}
	
	public void setSFXXBL(String sFXXBL) {
		if (this.sFXXBL != sFXXBL) {
			this.sFXXBL = sFXXBL;
			modify_sFXXBL = true;
		}
	}
	
	private String sCYY;
	private boolean modify_sCYY = false;
	
	public String getSCYY() {
		return sCYY;
	}
	
	public void setSCYY(String sCYY) {
		if (this.sCYY != sCYY) {
			this.sCYY = sCYY;
			modify_sCYY = true;
		}
	}
	
	private String dJCODE;
	private boolean modify_dJCODE = false;
	
	public String getDJCODE() {
		return dJCODE;
	}
	
	public void setDJCODE(String dJCODE) {
		if (this.dJCODE != dJCODE) {
			this.dJCODE = dJCODE;
			modify_dJCODE = true;
		}
	}
	
	private String eRRORLOG;
	private boolean modify_eRRORLOG = false;
	
	public String getERRORLOG() {
		return eRRORLOG;
	}
	
	public void setERRORLOG(String eRRORLOG) {
		if (this.eRRORLOG != eRRORLOG) {
			this.eRRORLOG = eRRORLOG;
			modify_eRRORLOG = true;
		}
	}
	private String errorCode;
	private boolean modify_ErrorCode = false;
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		if (this.errorCode != errorCode) {
			this.errorCode = errorCode;
			modify_ErrorCode = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_bSM = false;
		modify_zWLSH = false;
		modify_dJLSH = false;
		modify_dQSJ = false;
		modify_sFDB = false;
		modify_dQZT = false;
		modify_dQCS = false;
		modify_sCDQSJ = false;
		modify_dQSBYY = false;
		modify_sFXXBL = false;
		modify_sCYY = false;
		modify_dJCODE = false;
		modify_eRRORLOG = false;
		modify_ErrorCode = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_bSM)
			listStrings.add("bSM");
		if (!modify_zWLSH)
			listStrings.add("zWLSH");
		if (!modify_dJLSH)
			listStrings.add("dJLSH");
		if (!modify_dQSJ)
			listStrings.add("dQSJ");
		if (!modify_sFDB)
			listStrings.add("sFDB");
		if (!modify_dQZT)
			listStrings.add("dQZT");
		if (!modify_dQCS)
			listStrings.add("dQCS");
		if (!modify_sCDQSJ)
			listStrings.add("sCDQSJ");
		if (!modify_dQSBYY)
			listStrings.add("dQSBYY");
		if (!modify_sFXXBL)
			listStrings.add("sFXXBL");
		if (!modify_sCYY)
			listStrings.add("sCYY");
		if (!modify_dJCODE)
			listStrings.add("dJCODE");
		if (!modify_eRRORLOG)
			listStrings.add("eRRORLOG");
		if (!modify_ErrorCode)
			listStrings.add("errorCode");

		return StringHelper.ListToStrings(listStrings);
	}
}
