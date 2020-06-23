package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/13 
//* ----------------------------------------
//* Internal Entity rt_unit_modulemanager 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateRT_UNIT_MODULEMANAGER implements SuperModel<String> {

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

	private String fIELDNAME;
	private boolean modify_fIELDNAME = false;

	public String getFIELDNAME() {
		return fIELDNAME;
	}

	public void setFIELDNAME(String fIELDNAME) {
		if (this.fIELDNAME != fIELDNAME) {
			this.fIELDNAME = fIELDNAME;
			modify_fIELDNAME = true;
		}
	}

	private String fIELDDESCRIPTION;
	private boolean modify_fIELDDESCRIPTION = false;

	public String getFIELDDESCRIPTION() {
		return fIELDDESCRIPTION;
	}

	public void setFIELDDESCRIPTION(String fIELDDESCRIPTION) {
		if (this.fIELDDESCRIPTION != fIELDDESCRIPTION) {
			this.fIELDDESCRIPTION = fIELDDESCRIPTION;
			modify_fIELDDESCRIPTION = true;
		}
	}

	private String fIELDTYPE;
	private boolean modify_fIELDTYPE = false;

	public String getFIELDTYPE() {
		return fIELDTYPE;
	}

	public void setFIELDTYPE(String fIELDTYPE) {
		if (this.fIELDTYPE != fIELDTYPE) {
			this.fIELDTYPE = fIELDTYPE;
			modify_fIELDTYPE = true;
		}
	}

	private String fIELDOPTION;
	private boolean modify_fIELDOPTION = false;

	public String getFIELDOPTION() {
		return fIELDOPTION;
	}

	public void setFIELDOPTION(String fIELDOPTION) {
		if (this.fIELDOPTION != fIELDOPTION) {
			this.fIELDOPTION = fIELDOPTION;
			modify_fIELDOPTION = true;
		}
	}

	private Integer nUMBER_L;
	private boolean modify_nUMBER_L = false;

	public Integer getNUMBER_L() {
		return nUMBER_L;
	}

	public void setNUMBER_L(Integer nUMBER_L) {
		if (this.nUMBER_L != nUMBER_L) {
			this.nUMBER_L = nUMBER_L;
			modify_nUMBER_L = true;
		}
	}

	private Integer nUMBER_R;
	private boolean modify_nUMBER_R = false;

	public Integer getNUMBER_R() {
		return nUMBER_R;
	}

	public void setNUMBER_R(Integer nUMBER_R) {
		if (this.nUMBER_R != nUMBER_R) {
			this.nUMBER_R = nUMBER_R;
			modify_nUMBER_R = true;
		}
	}

	private String vISIBLE;
	private boolean modify_vISIBLE = false;

	public String getVISIBLE() {
		return vISIBLE;
	}

	public void setVISIBLE(String vISIBLE) {
		if (this.vISIBLE != vISIBLE) {
			this.vISIBLE = vISIBLE;
			modify_vISIBLE = true;
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

	private String rEQUIERD;
	private boolean modify_rEQUIERD = false;

	public String getREQUIERD() {
		return rEQUIERD;
	}

	public void setREQUIERD(String rEQUIERD) {
		if (this.rEQUIERD != rEQUIERD) {
			this.rEQUIERD = rEQUIERD;
			modify_rEQUIERD = true;
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

	private Integer cOLSPAN;
	private boolean modify_cOLSPAN = false;

	public Integer getCOLSPAN() {
		return cOLSPAN;
	}

	public void setCOLSPAN(Integer cOLSPAN) {
		if (this.cOLSPAN != cOLSPAN) {
			this.cOLSPAN = cOLSPAN;
			modify_cOLSPAN = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_mODULEID = false;
		modify_bDCDYLX = false;
		modify_fIELDNAME = false;
		modify_fIELDDESCRIPTION = false;
		modify_fIELDTYPE = false;
		modify_fIELDOPTION = false;
		modify_nUMBER_L = false;
		modify_nUMBER_R = false;
		modify_vISIBLE = false;
		modify_eDITABLE = false;
		modify_rEQUIERD = false;
		modify_sXH = false;
		modify_cOLSPAN = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_mODULEID)
			listStrings.add("mODULEID");
		if (!modify_bDCDYLX)
			listStrings.add("bDCDYLX");
		if (!modify_fIELDNAME)
			listStrings.add("fIELDNAME");
		if (!modify_fIELDDESCRIPTION)
			listStrings.add("fIELDDESCRIPTION");
		if (!modify_fIELDTYPE)
			listStrings.add("fIELDTYPE");
		if (!modify_fIELDOPTION)
			listStrings.add("fIELDOPTION");
		if (!modify_nUMBER_L)
			listStrings.add("nUMBER_L");
		if (!modify_nUMBER_R)
			listStrings.add("nUMBER_R");
		if (!modify_vISIBLE)
			listStrings.add("vISIBLE");
		if (!modify_eDITABLE)
			listStrings.add("eDITABLE");
		if (!modify_rEQUIERD)
			listStrings.add("rEQUIERD");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_cOLSPAN)
			listStrings.add("cOLSPAN");

		return StringHelper.ListToStrings(listStrings);
	}
}
