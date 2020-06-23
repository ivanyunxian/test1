package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_constcls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_CONSTCLS implements SuperModel<Integer> {

	private Integer id;
	private boolean modify_id = false;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private Integer cONSTSLSID;
	private boolean modify_cONSTSLSID = false;

	public Integer getCONSTSLSID() {
		return cONSTSLSID;
	}

	public void setCONSTSLSID(Integer cONSTSLSID) {
		if (this.cONSTSLSID != cONSTSLSID) {
			this.cONSTSLSID = cONSTSLSID;
			modify_cONSTSLSID = true;
		}
	}

	private String cONSTCLSNAME;
	private boolean modify_cONSTCLSNAME = false;

	public String getCONSTCLSNAME() {
		return cONSTCLSNAME;
	}

	public void setCONSTCLSNAME(String cONSTCLSNAME) {
		if (this.cONSTCLSNAME != cONSTCLSNAME) {
			this.cONSTCLSNAME = cONSTCLSNAME;
			modify_cONSTCLSNAME = true;
		}
	}

	private String cONSTCLSTYPE;
	private boolean modify_cONSTCLSTYPE = false;

	public String getCONSTCLSTYPE() {
		return cONSTCLSTYPE;
	}

	public void setCONSTCLSTYPE(String cONSTCLSTYPE) {
		if (this.cONSTCLSTYPE != cONSTCLSTYPE) {
			this.cONSTCLSTYPE = cONSTCLSTYPE;
			modify_cONSTCLSTYPE = true;
		}
	}

	private String bZ;
	private boolean modify_bZ = false;

	public String getBZ() {
		return bZ;
	}

	public void setBZ(String bZ) {
		if (this.bZ != bZ) {
			this.bZ = bZ;
			modify_bZ = true;
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
		modify_cONSTSLSID = false;
		modify_cONSTCLSNAME = false;
		modify_cONSTCLSTYPE = false;
		modify_bZ = false;
		modify_createTime = false;
		modify_modifyTime = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cONSTSLSID)
			listStrings.add("cONSTSLSID");
		if (!modify_cONSTCLSNAME)
			listStrings.add("cONSTCLSNAME");
		if (!modify_cONSTCLSTYPE)
			listStrings.add("cONSTCLSTYPE");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");

		return StringHelper.ListToStrings(listStrings);
	}
}
