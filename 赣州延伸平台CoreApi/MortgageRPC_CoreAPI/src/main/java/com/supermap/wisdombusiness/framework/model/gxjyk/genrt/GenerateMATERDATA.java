package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/9 
//* ----------------------------------------
//* Internal Entity materdata 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import javax.persistence.Transient;

import oracle.sql.BLOB;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GenerateMATERDATA implements SuperModel<String> {

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

	private String mATERIALDATA_ID;
	private boolean modify_mATERIALDATA_ID = false;

	public String getMATERIALDATA_ID() {
		return mATERIALDATA_ID;
	}

	public void setMATERIALDATA_ID(String mATERIALDATA_ID) {
		if (this.mATERIALDATA_ID != mATERIALDATA_ID) {
			this.mATERIALDATA_ID = mATERIALDATA_ID;
			modify_mATERIALDATA_ID = true;
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

	private String uPLOAD_NAME;
	private boolean modify_uPLOAD_NAME = false;

	public String getUPLOAD_NAME() {
		return uPLOAD_NAME;
	}

	public void setUPLOAD_NAME(String uPLOAD_NAME) {
		if (this.uPLOAD_NAME != uPLOAD_NAME) {
			this.uPLOAD_NAME = uPLOAD_NAME;
			modify_uPLOAD_NAME = true;
		}
	}

	private String uPLOAD_ID;
	private boolean modify_uPLOAD_ID = false;

	public String getUPLOAD_ID() {
		return uPLOAD_ID;
	}

	public void setUPLOAD_ID(String uPLOAD_ID) {
		if (this.uPLOAD_ID != uPLOAD_ID) {
			this.uPLOAD_ID = uPLOAD_ID;
			modify_uPLOAD_ID = true;
		}
	}

	private Date uPLOAD_DATE;
	private boolean modify_uPLOAD_DATE = false;

	public Date getUPLOAD_DATE() {
		return uPLOAD_DATE;
	}

	public void setUPLOAD_DATE(Date uPLOAD_DATE) {
		if (this.uPLOAD_DATE != uPLOAD_DATE) {
			this.uPLOAD_DATE = uPLOAD_DATE;
			modify_uPLOAD_DATE = true;
		}
	}

	private String fILE_NAME;
	private boolean modify_fILE_NAME = false;

	public String getFILE_NAME() {
		return fILE_NAME;
	}

	public void setFILE_NAME(String fILE_NAME) {
		if (this.fILE_NAME != fILE_NAME) {
			this.fILE_NAME = fILE_NAME;
			modify_fILE_NAME = true;
		}
	}

	private String fILE_POSTFIX;
	private boolean modify_fILE_POSTFIX = false;

	public String getFILE_POSTFIX() {
		return fILE_POSTFIX;
	}

	public void setFILE_POSTFIX(String fILE_POSTFIX) {
		if (this.fILE_POSTFIX != fILE_POSTFIX) {
			this.fILE_POSTFIX = fILE_POSTFIX;
			modify_fILE_POSTFIX = true;
		}
	}

	private String fILE_YEAR;
	private boolean modify_fILE_YEAR = false;

	public String getFILE_YEAR() {
		return fILE_YEAR;
	}

	public void setFILE_YEAR(String fILE_YEAR) {
		if (this.fILE_YEAR != fILE_YEAR) {
			this.fILE_YEAR = fILE_YEAR;
			modify_fILE_YEAR = true;
		}
	}

	private Integer sTORAGE_TYPE;
	private boolean modify_sTORAGE_TYPE = false;

	public Integer getSTORAGE_TYPE() {
		return sTORAGE_TYPE;
	}

	public void setSTORAGE_TYPE(Integer sTORAGE_TYPE) {
		if (this.sTORAGE_TYPE != sTORAGE_TYPE) {
			this.sTORAGE_TYPE = sTORAGE_TYPE;
			modify_sTORAGE_TYPE = true;
		}
	}

	private String fILE_PATH;
	private boolean modify_fILE_PATH = false;

	public String getFILE_PATH() {
		return fILE_PATH;
	}

	public void setFILE_PATH(String fILE_PATH) {
		if (this.fILE_PATH != fILE_PATH) {
			this.fILE_PATH = fILE_PATH;
			modify_fILE_PATH = true;
		}
	}

	private Integer fILE_INDEX;
	private boolean modify_fILE_INDEX = false;

	public Integer getFILE_INDEX() {
		return fILE_INDEX;
	}

	public void setFILE_INDEX(Integer fILE_INDEX) {
		if (this.fILE_INDEX != fILE_INDEX) {
			this.fILE_INDEX = fILE_INDEX;
			modify_fILE_INDEX = true;
		}
	}

	private Blob tHUMB;
	private boolean modify_tHUMB = false;

	public Blob getTHUMB() {
		return tHUMB;
	}

	public void setTHUMB(Blob tHUMB) {
		if (this.tHUMB != tHUMB) {
			this.tHUMB = tHUMB;
			modify_tHUMB = true;
		}
	}

	private String fILE_NUMBER;
	private boolean modify_fILE_NUMBER = false;

	public String getFILE_NUMBER() {
		return fILE_NUMBER;
	}

	public void setFILE_NUMBER(String fILE_NUMBER) {
		if (this.fILE_NUMBER != fILE_NUMBER) {
			this.fILE_NUMBER = fILE_NUMBER;
			modify_fILE_NUMBER = true;
		}
	}

	private Integer uPLOAD_STATUS;
	private boolean modify_uPLOAD_STATUS = false;

	public Integer getUPLOAD_STATUS() {
		return uPLOAD_STATUS;
	}

	public void setUPLOAD_STATUS(Integer uPLOAD_STATUS) {
		if (this.uPLOAD_STATUS != uPLOAD_STATUS) {
			this.uPLOAD_STATUS = uPLOAD_STATUS;
			modify_uPLOAD_STATUS = true;
		}
	}

	private String pATH;
	private boolean modify_pATH = false;

	public String getPATH() {
		return pATH;
	}

	public void setPATH(String pATH) {
		if (this.pATH != pATH) {
			this.pATH = pATH;
			modify_pATH = true;
		}
	}
	
	private String rELATIVEURL;
	private boolean modify_rELATIVEURL = false;

	public String getRELATIVEURL() {
		return rELATIVEURL;
	}

	public void setRELATIVEURL(String rELATIVEURL) {
		if (this.rELATIVEURL != rELATIVEURL) {
			this.rELATIVEURL = rELATIVEURL;
			modify_rELATIVEURL = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_mATERIALDATA_ID = false;
		modify_mATERILINST_ID = false;
		modify_uPLOAD_NAME = false;
		modify_uPLOAD_ID = false;
		modify_uPLOAD_DATE = false;
		modify_fILE_NAME = false;
		modify_fILE_POSTFIX = false;
		modify_fILE_YEAR = false;
		modify_sTORAGE_TYPE = false;
		modify_fILE_PATH = false;
		modify_fILE_INDEX = false;
		modify_tHUMB = false;
		modify_fILE_NUMBER = false;
		modify_uPLOAD_STATUS = false;
		modify_pATH = false;
		modify_rELATIVEURL=false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_mATERIALDATA_ID)
			listStrings.add("mATERIALDATA_ID");
		if (!modify_mATERILINST_ID)
			listStrings.add("mATERILINST_ID");
		if (!modify_uPLOAD_NAME)
			listStrings.add("uPLOAD_NAME");
		if (!modify_uPLOAD_ID)
			listStrings.add("uPLOAD_ID");
		if (!modify_uPLOAD_DATE)
			listStrings.add("uPLOAD_DATE");
		if (!modify_fILE_NAME)
			listStrings.add("fILE_NAME");
		if (!modify_fILE_POSTFIX)
			listStrings.add("fILE_POSTFIX");
		if (!modify_fILE_YEAR)
			listStrings.add("fILE_YEAR");
		if (!modify_sTORAGE_TYPE)
			listStrings.add("sTORAGE_TYPE");
		if (!modify_fILE_PATH)
			listStrings.add("fILE_PATH");
		if (!modify_fILE_INDEX)
			listStrings.add("fILE_INDEX");
		if (!modify_tHUMB)
			listStrings.add("tHUMB");
		if (!modify_fILE_NUMBER)
			listStrings.add("fILE_NUMBER");
		if (!modify_uPLOAD_STATUS)
			listStrings.add("uPLOAD_STATUS");
		if (!modify_pATH)
			listStrings.add("pATH");
		if(modify_rELATIVEURL)
			listStrings.add("modify_rELATIVEURL");

		return StringHelper.ListToStrings(listStrings);
	}
}
