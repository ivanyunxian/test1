package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-4 
//* ----------------------------------------
//* Internal Entity log_mq 
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

public class GenerateLOG_MQ implements SuperModel<String> {

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

	private String bSM;
	private boolean modify_bSM = false;

	public String getBSM() {
		return bSM;
	}

	public void setBSM(String bSM) {
		if (this.bSM != bSM) {
			this.bSM = bSM;
			modify_bSM = true;
		}
	}

	private String keyID;
	private boolean modify_keyID = false;

	public String getKeyID() {
		return keyID;
	}

	public void setKeyID(String keyID) {
		if (this.keyID != keyID) {
			this.keyID = keyID;
			modify_keyID = true;
		}
	}

	private Date logTime;
	private boolean modify_logTime = false;

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		if (this.logTime != logTime) {
			this.logTime = logTime;
			modify_logTime = true;
		}
	}

	private String mQMessage;
	private boolean modify_mQMessage = false;

	public String getMQMessage() {
		return mQMessage;
	}

	public void setMQMessage(String mQMessage) {
		if (this.mQMessage != mQMessage) {
			this.mQMessage = mQMessage;
			modify_mQMessage = true;
		}
	}

	private String parentKeyID;
	private boolean modify_parentKeyID = false;

	public String getParentKeyID() {
		return parentKeyID;
	}

	public void setParentKeyID(String parentKeyID) {
		if (this.parentKeyID != parentKeyID) {
			this.parentKeyID = parentKeyID;
			modify_parentKeyID = true;
		}
	}

	private String success;
	private boolean modify_success = false;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		if (this.success != success) {
			this.success = success;
			modify_success = true;
		}
	}

	private Integer status;
	private boolean modify_status = false;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		if (this.status != status) {
			this.status = status;
			modify_status = true;
		}
	}

	private String xMMC;
	private boolean modify_xMMC = false;

	public String getXMMC() {
		return xMMC;
	}

	public void setXMMC(String xMMC) {
		if (this.xMMC != xMMC) {
			this.xMMC = xMMC;
			modify_xMMC = true;
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

	private String actinst_id;
	private boolean modify_actinst_id = false;

	public String getActinst_id() {
		return actinst_id;
	}

	public void setActinst_id(String actinst_id) {
		if (this.actinst_id != actinst_id) {
			this.actinst_id = actinst_id;
			modify_actinst_id = true;
		}
	}

	private String mQFrom;
	private boolean modify_mQFrom = false;

	public String getMQFrom() {
		return mQFrom;
	}

	public void setMQFrom(String mQFrom) {
		if (this.mQFrom != mQFrom) {
			this.mQFrom = mQFrom;
			modify_mQFrom = true;
		}
	}

	private String mQTo;
	private boolean modify_mQTo = false;

	public String getMQTo() {
		return mQTo;
	}

	public void setMQTo(String mQTo) {
		if (this.mQTo != mQTo) {
			this.mQTo = mQTo;
			modify_mQTo = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bSM = false;
		modify_keyID = false;
		modify_logTime = false;
		modify_mQMessage = false;
		modify_parentKeyID = false;
		modify_success = false;
		modify_status = false;
		modify_xMMC = false;
		modify_xMBH = false;
		modify_actinst_id = false;
		modify_mQFrom = false;
		modify_mQTo = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bSM)
			listStrings.add("bSM");
		if (!modify_keyID)
			listStrings.add("keyID");
		if (!modify_logTime)
			listStrings.add("logTime");
		if (!modify_mQMessage)
			listStrings.add("mQMessage");
		if (!modify_parentKeyID)
			listStrings.add("parentKeyID");
		if (!modify_success)
			listStrings.add("success");
		if (!modify_status)
			listStrings.add("status");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_actinst_id)
			listStrings.add("actinst_id");
		if (!modify_mQFrom)
			listStrings.add("mQFrom");
		if (!modify_mQTo)
			listStrings.add("mQTo");

		return StringHelper.ListToStrings(listStrings);
	}
}
