package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_ljzc_gz 
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

public class GenerateBDCS_LJZC_GZ implements SuperModel<String> {

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

	private String cID;
	private boolean modify_cID = false;

	public String getCID() {
		return cID;
	}

	public void setCID(String cID) {
		if (this.cID != cID) {
			this.cID = cID;
			modify_cID = true;
		}
	}

	private String lJZID;
	private boolean modify_lJZID = false;

	public String getLJZID() {
		return lJZID;
	}

	public void setLJZID(String lJZID) {
		if (this.lJZID != lJZID) {
			this.lJZID = lJZID;
			modify_lJZID = true;
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

	private String dCXMID;
	private boolean modify_dCXMID = false;

	public String getDCXMID() {
		return dCXMID;
	}

	public void setDCXMID(String dCXMID) {
		if (this.dCXMID != dCXMID) {
			this.dCXMID = dCXMID;
			modify_dCXMID = true;
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
		modify_cID = false;
		modify_lJZID = false;
		modify_xMBH = false;
		modify_dCXMID = false;
		modify_createTime = false;
		modify_modifyTime = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cID)
			listStrings.add("cID");
		if (!modify_lJZID)
			listStrings.add("lJZID");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");

		return StringHelper.ListToStrings(listStrings);
	}
}
