package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity c 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GenerateC implements SuperModel<String> {

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

	private String ySDM;
	private boolean modify_ySDM = false;

	public String getYSDM() {
		return ySDM;
	}

	public void setYSDM(String ySDM) {
		if (this.ySDM != ySDM) {
			this.ySDM = ySDM;
			modify_ySDM = true;
		}
	}

	private String cH;
	private boolean modify_cH = false;

	public String getCH() {
		return cH;
	}

	public void setCH(String cH) {
		if (this.cH != cH) {
			this.cH = cH;
			modify_cH = true;
		}
	}

	private String zRZBDCDYID;
	private boolean modify_zRZBDCDYID = false;

	public String getZRZBDCDYID() {
		return zRZBDCDYID;
	}

	public void setZRZBDCDYID(String zRZBDCDYID) {
		if (this.zRZBDCDYID != zRZBDCDYID) {
			this.zRZBDCDYID = zRZBDCDYID;
			modify_zRZBDCDYID = true;
		}
	}

	private String zRZH;
	private boolean modify_zRZH = false;

	public String getZRZH() {
		return zRZH;
	}

	public void setZRZH(String zRZH) {
		if (this.zRZH != zRZH) {
			this.zRZH = zRZH;
			modify_zRZH = true;
		}
	}

	private Integer sJC;
	private boolean modify_sJC = false;

	public Integer getSJC() {
		return sJC;
	}

	public void setSJC(Integer sJC) {
		if (this.sJC != sJC) {
			this.sJC = sJC;
			modify_sJC = true;
		}
	}

	private String mYC;
	private boolean modify_mYC = false;

	public String getMYC() {
		return mYC;
	}

	public void setMYC(String mYC) {
		if (this.mYC != mYC) {
			this.mYC = mYC;
			modify_mYC = true;
		}
	}

	private Double cJZMJ;
	private boolean modify_cJZMJ = false;

	public Double getCJZMJ() {
		return cJZMJ;
	}

	public void setCJZMJ(Double cJZMJ) {
		if (this.cJZMJ != cJZMJ) {
			this.cJZMJ = cJZMJ;
			modify_cJZMJ = true;
		}
	}

	private Double cTNJZMJ;
	private boolean modify_cTNJZMJ = false;

	public Double getCTNJZMJ() {
		return cTNJZMJ;
	}

	public void setCTNJZMJ(Double cTNJZMJ) {
		if (this.cTNJZMJ != cTNJZMJ) {
			this.cTNJZMJ = cTNJZMJ;
			modify_cTNJZMJ = true;
		}
	}

	private Double cYTMJ;
	private boolean modify_cYTMJ = false;

	public Double getCYTMJ() {
		return cYTMJ;
	}

	public void setCYTMJ(Double cYTMJ) {
		if (this.cYTMJ != cYTMJ) {
			this.cYTMJ = cYTMJ;
			modify_cYTMJ = true;
		}
	}

	private Double cGYJZMJ;
	private boolean modify_cGYJZMJ = false;

	public Double getCGYJZMJ() {
		return cGYJZMJ;
	}

	public void setCGYJZMJ(Double cGYJZMJ) {
		if (this.cGYJZMJ != cGYJZMJ) {
			this.cGYJZMJ = cGYJZMJ;
			modify_cGYJZMJ = true;
		}
	}

	private Double cFTJZMJ;
	private boolean modify_cFTJZMJ = false;

	public Double getCFTJZMJ() {
		return cFTJZMJ;
	}

	public void setCFTJZMJ(Double cFTJZMJ) {
		if (this.cFTJZMJ != cFTJZMJ) {
			this.cFTJZMJ = cFTJZMJ;
			modify_cFTJZMJ = true;
		}
	}

	private Double cG;
	private boolean modify_cG = false;

	public Double getCG() {
		return cG;
	}

	public void setCG(Double cG) {
		if (this.cG != cG) {
			this.cG = cG;
			modify_cG = true;
		}
	}

	private Double sPTYMJ;
	private boolean modify_sPTYMJ = false;

	public Double getSPTYMJ() {
		return sPTYMJ;
	}

	public void setSPTYMJ(Double sPTYMJ) {
		if (this.sPTYMJ != sPTYMJ) {
			this.sPTYMJ = sPTYMJ;
			modify_sPTYMJ = true;
		}
	}

	private Double cBQMJ;
	private boolean modify_cBQMJ = false;

	public Double getCBQMJ() {
		return cBQMJ;
	}

	public void setCBQMJ(Double cBQMJ) {
		if (this.cBQMJ != cBQMJ) {
			this.cBQMJ = cBQMJ;
			modify_cBQMJ = true;
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

	private String gXXMBH;
	private boolean modify_gXXMBH = false;

	public String getGXXMBH() {
		return gXXMBH;
	}

	public void setGXXMBH(String gXXMBH) {
		if (this.gXXMBH != gXXMBH) {
			this.gXXMBH = gXXMBH;
			modify_gXXMBH = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_cID = false;
		modify_ySDM = false;
		modify_cH = false;
		modify_zRZBDCDYID = false;
		modify_zRZH = false;
		modify_sJC = false;
		modify_mYC = false;
		modify_cJZMJ = false;
		modify_cTNJZMJ = false;
		modify_cYTMJ = false;
		modify_cGYJZMJ = false;
		modify_cFTJZMJ = false;
		modify_cG = false;
		modify_sPTYMJ = false;
		modify_cBQMJ = false;
		modify_lJZID = false;
		modify_gXXMBH = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cID)
			listStrings.add("cID");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_cH)
			listStrings.add("cH");
		if (!modify_zRZBDCDYID)
			listStrings.add("zRZBDCDYID");
		if (!modify_zRZH)
			listStrings.add("zRZH");
		if (!modify_sJC)
			listStrings.add("sJC");
		if (!modify_mYC)
			listStrings.add("mYC");
		if (!modify_cJZMJ)
			listStrings.add("cJZMJ");
		if (!modify_cTNJZMJ)
			listStrings.add("cTNJZMJ");
		if (!modify_cYTMJ)
			listStrings.add("cYTMJ");
		if (!modify_cGYJZMJ)
			listStrings.add("cGYJZMJ");
		if (!modify_cFTJZMJ)
			listStrings.add("cFTJZMJ");
		if (!modify_cG)
			listStrings.add("cG");
		if (!modify_sPTYMJ)
			listStrings.add("sPTYMJ");
		if (!modify_cBQMJ)
			listStrings.add("cBQMJ");
		if (!modify_lJZID)
			listStrings.add("lJZID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");

		return StringHelper.ListToStrings(listStrings);
	}
}
