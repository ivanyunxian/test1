package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-12 
//* ----------------------------------------
//* Internal Entity bdcs_dybg 
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

public class GenerateBDCS_DYBG implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = (String) SuperHelper.GeneratePrimaryKey();
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

	private String lDJDYID;
	private boolean modify_lDJDYID = false;

	public String getLDJDYID() {
		return lDJDYID;
	}

	public void setLDJDYID(String lDJDYID) {
		if (this.lDJDYID != lDJDYID) {
			this.lDJDYID = lDJDYID;
			modify_lDJDYID = true;
		}
	}

	private String lBDCDYID;
	private boolean modify_lBDCDYID = false;

	public String getLBDCDYID() {
		return lBDCDYID;
	}

	public void setLBDCDYID(String lBDCDYID) {
		if (this.lBDCDYID != lBDCDYID) {
			this.lBDCDYID = lBDCDYID;
			modify_lBDCDYID = true;
		}
	}

	private String xDJDYID;
	private boolean modify_xDJDYID = false;

	public String getXDJDYID() {
		return xDJDYID;
	}

	public void setXDJDYID(String xDJDYID) {
		if (this.xDJDYID != xDJDYID) {
			this.xDJDYID = xDJDYID;
			modify_xDJDYID = true;
		}
	}

	private String xBDCDYID;
	private boolean modify_xBDCDYID = false;

	public String getXBDCDYID() {
		return xBDCDYID;
	}

	public void setXBDCDYID(String xBDCDYID) {
		if (this.xBDCDYID != xBDCDYID) {
			this.xBDCDYID = xBDCDYID;
			modify_xBDCDYID = true;
		}
	}

	private String xMBH;
	private boolean modify_xMBH = false;

	public String getXMBH() {
		return xMBH;
	}

	public void setXMBH(String xMBH) {
		if (this.xMBH != xMBH) {
			this.xMBH = xMBH;
			modify_xMBH = true;
		}
	}

	private Date createTime;
	private boolean modify_createTime = false;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		if (this.createTime != createTime) {
			this.createTime = createTime;
			modify_createTime = true;
		}
	}

	private Date modifyTime;
	private boolean modify_modifyTime = false;

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		if (this.modifyTime != modifyTime) {
			this.modifyTime = modifyTime;
			modify_modifyTime = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_lDJDYID = false;
		modify_lBDCDYID = false;
		modify_xDJDYID = false;
		modify_xBDCDYID = false;
		modify_xMBH = false;
		modify_createTime = false;
		modify_modifyTime = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_lDJDYID)
			listStrings.add("lDJDYID");
		if (!modify_lBDCDYID)
			listStrings.add("lBDCDYID");
		if (!modify_xDJDYID)
			listStrings.add("xDJDYID");
		if (!modify_xBDCDYID)
			listStrings.add("xBDCDYID");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");

		return StringHelper.ListToStrings(listStrings);
	}
}
