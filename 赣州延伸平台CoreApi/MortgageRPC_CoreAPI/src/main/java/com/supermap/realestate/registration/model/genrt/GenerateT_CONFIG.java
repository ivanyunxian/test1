package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-27 
//* ----------------------------------------
//* Internal Entity t_config 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateT_CONFIG implements SuperModel<String> {

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

	private String cLASSNAME;
	private boolean modify_cLASSNAME = false;

	public String getCLASSNAME() {
		return cLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		if (this.cLASSNAME != cLASSNAME) {
			this.cLASSNAME = cLASSNAME;
			modify_cLASSNAME = true;
		}
	}

	private String nAME;
	private boolean modify_nAME = false;

	public String getNAME() {
		return nAME;
	}

	public void setNAME(String nAME) {
		if (this.nAME != nAME) {
			this.nAME = nAME;
			modify_nAME = true;
		}
	}

	private String vALUE;
	private boolean modify_vALUE = false;

	public String getVALUE() {
		return vALUE;
	}

	public void setVALUE(String vALUE) {
		if (this.vALUE != vALUE) {
			this.vALUE = vALUE;
			modify_vALUE = true;
		}
	}

	private String vALUEDESCRIPTION;
	private boolean modify_vALUEDESCRIPTION = false;

	public String getVALUEDESCRIPTION() {
		return vALUEDESCRIPTION;
	}

	public void setVALUEDESCRIPTION(String vALUEDESCRIPTION) {
		if (this.vALUEDESCRIPTION != vALUEDESCRIPTION) {
			this.vALUEDESCRIPTION = vALUEDESCRIPTION;
			modify_vALUEDESCRIPTION = true;
		}
	}

	private String dESCRIPTION;
	private boolean modify_dESCRIPTION = false;

	public String getDESCRIPTION() {
		return dESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		if (this.dESCRIPTION != dESCRIPTION) {
			this.dESCRIPTION = dESCRIPTION;
			modify_dESCRIPTION = true;
		}
	}

	private String uRL;
	private boolean modify_uRL = false;

	public String getURL() {
		return uRL;
	}

	public void setURL(String uRL) {
		if (this.uRL != uRL) {
			this.uRL = uRL;
			modify_uRL = true;
		}
	}

	private String cONFIGNAME;
	private boolean modify_cONFIGNAME = false;

	public String getCONFIGNAME() {
		return cONFIGNAME;
	}

	public void setCONFIGNAME(String cONFIGNAME) {
		if (this.cONFIGNAME != cONFIGNAME) {
			this.cONFIGNAME = cONFIGNAME;
			modify_cONFIGNAME = true;
		}
	}

	private String vALUETYPE;
	private boolean modify_vALUETYPE = false;

	public String getVALUETYPE() {
		return vALUETYPE;
	}

	public void setVALUETYPE(String vALUETYPE) {
		if (this.vALUETYPE != vALUETYPE) {
			this.vALUETYPE = vALUETYPE;
			modify_vALUETYPE = true;
		}
	}

	private String oPTIONCLASS;
	private boolean modify_oPTIONCLASS = false;

	public String getOPTIONCLASS() {
		return oPTIONCLASS;
	}

	public void setOPTIONCLASS(String oPTIONCLASS) {
		if (this.oPTIONCLASS != oPTIONCLASS) {
			this.oPTIONCLASS = oPTIONCLASS;
			modify_oPTIONCLASS = true;
		}
	}

	private Integer fIRSTINDEX;
	private boolean modify_fIRSTINDEX = false;

	public Integer getFIRSTINDEX() {
		return fIRSTINDEX;
	}

	public void setFIRSTINDEX(Integer fIRSTINDEX) {
		if (this.fIRSTINDEX != fIRSTINDEX) {
			this.fIRSTINDEX = fIRSTINDEX;
			modify_fIRSTINDEX = true;
		}
	}

	private Integer sECONDINDEX;
	private boolean modify_sECONDINDEX = false;

	public Integer getSECONDINDEX() {
		return sECONDINDEX;
	}

	public void setSECONDINDEX(Integer sECONDINDEX) {
		if (this.sECONDINDEX != sECONDINDEX) {
			this.sECONDINDEX = sECONDINDEX;
			modify_sECONDINDEX = true;
		}
	}

	private Integer yXBZ;
	private boolean modify_yXBZ = false;

	public Integer getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(Integer yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	private Integer cONFIGED;
	private boolean modify_cONFIGED = false;

	public Integer getCONFIGED() {
		return cONFIGED;
	}

	public void setCONFIGED(Integer cONFIGED) {
		if (this.cONFIGED != cONFIGED) {
			this.cONFIGED = cONFIGED;
			modify_cONFIGED = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_cLASSNAME = false;
		modify_nAME = false;
		modify_vALUE = false;
		modify_vALUEDESCRIPTION = false;
		modify_dESCRIPTION = false;
		modify_uRL = false;
		modify_cONFIGNAME = false;
		modify_vALUETYPE = false;
		modify_oPTIONCLASS = false;
		modify_fIRSTINDEX = false;
		modify_sECONDINDEX = false;
		modify_yXBZ = false;
		modify_cONFIGED = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cLASSNAME)
			listStrings.add("cLASSNAME");
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_vALUE)
			listStrings.add("vALUE");
		if (!modify_vALUEDESCRIPTION)
			listStrings.add("vALUEDESCRIPTION");
		if (!modify_dESCRIPTION)
			listStrings.add("dESCRIPTION");
		if (!modify_uRL)
			listStrings.add("uRL");
		if (!modify_cONFIGNAME)
			listStrings.add("cONFIGNAME");
		if (!modify_vALUETYPE)
			listStrings.add("vALUETYPE");
		if (!modify_oPTIONCLASS)
			listStrings.add("oPTIONCLASS");
		if (!modify_fIRSTINDEX)
			listStrings.add("fIRSTINDEX");
		if (!modify_sECONDINDEX)
			listStrings.add("sECONDINDEX");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_cONFIGED)
			listStrings.add("cONFIGED");

		return StringHelper.ListToStrings(listStrings);
	}
}
