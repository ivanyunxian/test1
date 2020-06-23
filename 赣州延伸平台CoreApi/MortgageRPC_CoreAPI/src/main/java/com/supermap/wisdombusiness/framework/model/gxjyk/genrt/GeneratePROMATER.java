package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/9 
//* ----------------------------------------
//* Internal Entity promater 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GeneratePROMATER implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;


	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}


	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String mATERILINST_ID;
	private boolean modify_mATERILINST_ID = false;

	public String getMATERILINST_ID() {
		return mATERILINST_ID;
	}

	public void setMATERILINST_ID(String mATERILINST_ID) {
		if (this.mATERILINST_ID != mATERILINST_ID) {
			this.mATERILINST_ID = mATERILINST_ID;
			modify_mATERILINST_ID = true;
		}
	}

	private String mATERIAL_ID;
	private boolean modify_mATERIAL_ID = false;

	public String getMATERIAL_ID() {
		return mATERIAL_ID;
	}

	public void setMATERIAL_ID(String mATERIAL_ID) {
		if (this.mATERIAL_ID != mATERIAL_ID) {
			this.mATERIAL_ID = mATERIAL_ID;
			modify_mATERIAL_ID = true;
		}
	}

	private Integer mATERIAL_INDEX;
	private boolean modify_mATERIAL_INDEX = false;

	public Integer getMATERIAL_INDEX() {
		return mATERIAL_INDEX;
	}

	public void setMATERIAL_INDEX(Integer mATERIAL_INDEX) {
		if (this.mATERIAL_INDEX != mATERIAL_INDEX) {
			this.mATERIAL_INDEX = mATERIAL_INDEX;
			modify_mATERIAL_INDEX = true;
		}
	}

	private String mATERIAL_NAME;
	private boolean modify_mATERIAL_NAME = false;

	public String getMATERIAL_NAME() {
		return mATERIAL_NAME;
	}

	public void setMATERIAL_NAME(String mATERIAL_NAME) {
		if (this.mATERIAL_NAME != mATERIAL_NAME) {
			this.mATERIAL_NAME = mATERIAL_NAME;
			modify_mATERIAL_NAME = true;
		}
	}

	private Integer mATERIAL_TYPE;
	private boolean modify_mATERIAL_TYPE = false;

	public Integer getMATERIAL_TYPE() {
		return mATERIAL_TYPE;
	}

	public void setMATERIAL_TYPE(Integer mATERIAL_TYPE) {
		if (this.mATERIAL_TYPE != mATERIAL_TYPE) {
			this.mATERIAL_TYPE = mATERIAL_TYPE;
			modify_mATERIAL_TYPE = true;
		}
	}

	private Integer mATERIAL_NEED;
	private boolean modify_mATERIAL_NEED = false;

	public Integer getMATERIAL_NEED() {
		return mATERIAL_NEED;
	}

	public void setMATERIAL_NEED(Integer mATERIAL_NEED) {
		if (this.mATERIAL_NEED != mATERIAL_NEED) {
			this.mATERIAL_NEED = mATERIAL_NEED;
			modify_mATERIAL_NEED = true;
		}
	}

	private String mATERIALDEF_ID;
	private boolean modify_mATERIALDEF_ID = false;

	public String getMATERIALDEF_ID() {
		return mATERIALDEF_ID;
	}

	public void setMATERIALDEF_ID(String mATERIALDEF_ID) {
		if (this.mATERIALDEF_ID != mATERIALDEF_ID) {
			this.mATERIALDEF_ID = mATERIALDEF_ID;
			modify_mATERIALDEF_ID = true;
		}
	}

	private String pROINST_ID;
	private boolean modify_pROINST_ID = false;

	public String getPROINST_ID() {
		return pROINST_ID;
	}

	public void setPROINST_ID(String pROINST_ID) {
		if (this.pROINST_ID != pROINST_ID) {
			this.pROINST_ID = pROINST_ID;
			modify_pROINST_ID = true;
		}
	}

	private String iMG_PATH;
	private boolean modify_iMG_PATH = false;

	public String getIMG_PATH() {
		return iMG_PATH;
	}

	public void setIMG_PATH(String iMG_PATH) {
		if (this.iMG_PATH != iMG_PATH) {
			this.iMG_PATH = iMG_PATH;
			modify_iMG_PATH = true;
		}
	}

	private String mATERIALTYPE_ID;
	private boolean modify_mATERIALTYPE_ID = false;

	public String getMATERIALTYPE_ID() {
		return mATERIALTYPE_ID;
	}

	public void setMATERIALTYPE_ID(String mATERIALTYPE_ID) {
		if (this.mATERIALTYPE_ID != mATERIALTYPE_ID) {
			this.mATERIALTYPE_ID = mATERIALTYPE_ID;
			modify_mATERIALTYPE_ID = true;
		}
	}

	private Integer mATERIAL_STATUS;
	private boolean modify_mATERIAL_STATUS = false;

	public Integer getMATERIAL_STATUS() {
		return mATERIAL_STATUS;
	}

	public void setMATERIAL_STATUS(Integer mATERIAL_STATUS) {
		if (this.mATERIAL_STATUS != mATERIAL_STATUS) {
			this.mATERIAL_STATUS = mATERIAL_STATUS;
			modify_mATERIAL_STATUS = true;
		}
	}

	private Date mATERIAL_DATE;
	private boolean modify_mATERIAL_DATE = false;

	public Date getMATERIAL_DATE() {
		return mATERIAL_DATE;
	}

	public void setMATERIAL_DATE(Date mATERIAL_DATE) {
		if (this.mATERIAL_DATE != mATERIAL_DATE) {
			this.mATERIAL_DATE = mATERIAL_DATE;
			modify_mATERIAL_DATE = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_mATERILINST_ID = false;
		modify_mATERIAL_ID = false;
		modify_mATERIAL_INDEX = false;
		modify_mATERIAL_NAME = false;
		modify_mATERIAL_TYPE = false;
		modify_mATERIAL_NEED = false;
		modify_mATERIALDEF_ID = false;
		modify_pROINST_ID = false;
		modify_iMG_PATH = false;
		modify_mATERIALTYPE_ID = false;
		modify_mATERIAL_STATUS = false;
		modify_mATERIAL_DATE = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_mATERILINST_ID)
			listStrings.add("mATERILINST_ID");
		if (!modify_mATERIAL_ID)
			listStrings.add("mATERIAL_ID");
		if (!modify_mATERIAL_INDEX)
			listStrings.add("mATERIAL_INDEX");
		if (!modify_mATERIAL_NAME)
			listStrings.add("mATERIAL_NAME");
		if (!modify_mATERIAL_TYPE)
			listStrings.add("mATERIAL_TYPE");
		if (!modify_mATERIAL_NEED)
			listStrings.add("mATERIAL_NEED");
		if (!modify_mATERIALDEF_ID)
			listStrings.add("mATERIALDEF_ID");
		if (!modify_pROINST_ID)
			listStrings.add("pROINST_ID");
		if (!modify_iMG_PATH)
			listStrings.add("iMG_PATH");
		if (!modify_mATERIALTYPE_ID)
			listStrings.add("mATERIALTYPE_ID");
		if (!modify_mATERIAL_STATUS)
			listStrings.add("mATERIAL_STATUS");
		if (!modify_mATERIAL_DATE)
			listStrings.add("mATERIAL_DATE");

		return StringHelper.ListToStrings(listStrings);
	}
}
