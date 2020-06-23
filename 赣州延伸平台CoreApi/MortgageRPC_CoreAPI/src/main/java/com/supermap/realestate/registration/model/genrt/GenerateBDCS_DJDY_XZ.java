package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-12 
//* ----------------------------------------
//* Internal Entity bdcs_djdy_xz 
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

public class GenerateBDCS_DJDY_XZ implements SuperModel<String> {

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

	private String dJDYID;
	private boolean modify_dJDYID = false;

	public String getDJDYID() {
		return dJDYID;
	}

	public void setDJDYID(String dJDYID) {
		if (this.dJDYID != dJDYID) {
			this.dJDYID = dJDYID;
			modify_dJDYID = true;
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

	private String bDCDYH;
	private boolean modify_bDCDYH = false;

	public String getBDCDYH() {
		return bDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		if (this.bDCDYH != bDCDYH) {
			this.bDCDYH = bDCDYH;
			modify_bDCDYH = true;
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

	private String bDCDYID;
	private boolean modify_bDCDYID = false;

	public String getBDCDYID() {
		return bDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		if (this.bDCDYID != bDCDYID) {
			this.bDCDYID = bDCDYID;
			modify_bDCDYID = true;
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
	
	private Integer gROUPID = 1;
	private boolean modify_gROUPID = false;

	public Integer getGROUPID() {
		return gROUPID;
	}

	public void setGROUPID(Integer gROUPID) {
		if (this.gROUPID != gROUPID&&!org.springframework.util.StringUtils.isEmpty(gROUPID)) {
			this.gROUPID = gROUPID;
			modify_gROUPID = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_dJDYID = false;
		modify_xMBH = false;
		modify_bDCDYH = false;
		modify_bDCDYLX = false;
		modify_bDCDYID = false;
		modify_dCXMID = false;
		modify_createTime = false;
		modify_modifyTime = false;
		modify_lY = false;
		modify_gROUPID=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_dJDYID)
			listStrings.add("dJDYID");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_bDCDYLX)
			listStrings.add("bDCDYLX");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_dCXMID)
			listStrings.add("dCXMID");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");
		if (!modify_lY)
			listStrings.add("lY");
		if (!modify_gROUPID)
			listStrings.add("gROUPID");

		return StringHelper.ListToStrings(listStrings);
	}
}
