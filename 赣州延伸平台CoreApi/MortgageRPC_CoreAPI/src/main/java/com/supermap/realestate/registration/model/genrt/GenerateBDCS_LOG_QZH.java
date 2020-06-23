package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/10 
//* ----------------------------------------
//* Internal Entity bdcs_log_qzh 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_LOG_QZH implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null) {
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

	private Integer sTARTXH;
	private boolean modify_sTARTXH = false;

	public Integer getSTARTXH() {
		return sTARTXH;
	}

	public void setSTARTXH(Integer sTARTXH) {
		if (this.sTARTXH != sTARTXH) {
			this.sTARTXH = sTARTXH;
			modify_sTARTXH = true;
		}
	}

	private String sTARTQZH;
	private boolean modify_sTARTQZH = false;

	public String getSTARTQZH() {
		return sTARTQZH;
	}

	public void setSTARTQZH(String sTARTQZH) {
		if (this.sTARTQZH != sTARTQZH) {
			this.sTARTQZH = sTARTQZH;
			modify_sTARTQZH = true;
		}
	}

	private Integer gS;
	private boolean modify_gS = false;

	public Integer getGS() {
		return gS;
	}

	public void setGS(Integer gS) {
		if (this.gS != gS) {
			this.gS = gS;
			modify_gS = true;
		}
	}

	private String qZLX;
	private boolean modify_qZLX = false;

	public String getQZLX() {
		return qZLX;
	}

	public void setQZLX(String qZLX) {
		if (this.qZLX != qZLX) {
			this.qZLX = qZLX;
			modify_qZLX = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_sTARTXH = false;
		modify_sTARTQZH = false;
		modify_gS = false;
		modify_qZLX = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_sTARTXH)
			listStrings.add("sTARTXH");
		if (!modify_sTARTQZH)
			listStrings.add("sTARTQZH");
		if (!modify_gS)
			listStrings.add("gS");
		if (!modify_qZLX)
			listStrings.add("qZLX");

		return StringHelper.ListToStrings(listStrings);
	}
}
