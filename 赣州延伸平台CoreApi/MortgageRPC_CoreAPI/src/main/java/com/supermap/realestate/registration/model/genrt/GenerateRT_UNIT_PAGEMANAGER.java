package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/11 
//* ----------------------------------------
//* Internal Entity rt_unit_pagemanager 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateRT_UNIT_PAGEMANAGER implements SuperModel<String> {

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

	private String mODULEID;
	private boolean modify_mODULEID = false;

	public String getMODULEID() {
		return mODULEID;
	}

	public void setMODULEID(String mODULEID) {
		if (this.mODULEID != mODULEID) {
			this.mODULEID = mODULEID;
			modify_mODULEID = true;
		}
	}

	private Integer sXH;
	private boolean modify_sXH = false;

	public Integer getSXH() {
		return sXH;
	}

	public void setSXH(Integer sXH) {
		if (this.sXH != sXH) {
			this.sXH = sXH;
			modify_sXH = true;
		}
	}

	private String tITLEVISIBLE;
	private boolean modify_tITLEVISIBLE = false;

	public String getTITLEVISIBLE() {
		return tITLEVISIBLE;
	}

	public void setTITLEVISIBLE(String tITLEVISIBLE) {
		if (this.tITLEVISIBLE != tITLEVISIBLE) {
			this.tITLEVISIBLE = tITLEVISIBLE;
			modify_tITLEVISIBLE = true;
		}
	}
	
	private String eDITABLE;
	private boolean modify_eDITABLE = false;

	public String getEDITABLE() {
		return eDITABLE;
	}

	public void setEDITABLE(String eDITABLE) {
		if (this.eDITABLE != eDITABLE) {
			this.eDITABLE = eDITABLE;
			modify_eDITABLE = true;
		}
	}

	private String tITLE;
	private boolean modify_tITLE = false;

	public String getTITLE() {
		return tITLE;
	}

	public void setTITLE(String tITLE) {
		if (this.tITLE != tITLE) {
			this.tITLE = tITLE;
			modify_tITLE = true;
		}
	}

	private String pAGEID;
	private boolean modify_pAGEID = false;

	public String getPAGEID() {
		return pAGEID;
	}

	public void setPAGEID(String pAGEID) {
		if (this.pAGEID != pAGEID) {
			this.pAGEID = pAGEID;
			modify_pAGEID = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_mODULEID = false;
		modify_sXH = false;
		modify_tITLEVISIBLE = false;
		modify_eDITABLE = false;
		modify_tITLE = false;
		modify_pAGEID = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_mODULEID)
			listStrings.add("mODULEID");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_tITLEVISIBLE)
			listStrings.add("tITLEVISIBLE");
		if (!modify_eDITABLE)
			listStrings.add("eDITABLE");
		if (!modify_tITLE)
			listStrings.add("tITLE");
		if (!modify_pAGEID)
			listStrings.add("pAGEID");

		return StringHelper.ListToStrings(listStrings);
	}
}
