package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-26 
//* ----------------------------------------
//* Internal Entity bdcs_tdyt_ls 
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

public class GenerateBDCS_TDYT_LS implements SuperModel<String> {

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

	private String sFZYT;
	private boolean modify_sFZYT = false;

	public String getSFZYT() {
		return sFZYT;
	}

	public void setSFZYT(String sFZYT) {
		if (this.sFZYT != sFZYT) {
			this.sFZYT = sFZYT;
			modify_sFZYT = true;
		}
	}

	private String tDYT;
	private boolean modify_tDYT = false;

	public String getTDYT() {
		return tDYT;
	}

	public void setTDYT(String tDYT) {
		if (this.tDYT != tDYT) {
			this.tDYT = tDYT;
			modify_tDYT = true;
		}
	}

	private String tDYTMC;
	private boolean modify_tDYTMC = false;

	public String getTDYTMC() {
		return tDYTMC;
	}

	public void setTDYTMC(String tDYTMC) {
		if (this.tDYTMC != tDYTMC) {
			this.tDYTMC = tDYTMC;
			modify_tDYTMC = true;
		}
	}

	private String tDDJ;
	private boolean modify_tDDJ = false;

	public String getTDDJ() {
		return tDDJ;
	}

	public void setTDDJ(String tDDJ) {
		if (this.tDDJ != tDDJ) {
			this.tDDJ = tDDJ;
			modify_tDDJ = true;
		}
	}

	private Double tDJG;
	private boolean modify_tDJG = false;

	public Double getTDJG() {
		return tDJG;
	}

	public void setTDJG(Double tDJG) {
		if (this.tDJG != tDJG) {
			this.tDJG = tDJG;
			modify_tDJG = true;
		}
	}

	private Date qSRQ;
	private boolean modify_qSRQ = false;

	public Date getQSRQ() {
		return qSRQ;
	}

	public void setQSRQ(Date qSRQ) {
		if (this.qSRQ != qSRQ) {
			this.qSRQ = qSRQ;
			modify_qSRQ = true;
		}
	}

	private Date zZRQ;
	private boolean modify_zZRQ = false;

	public Date getZZRQ() {
		return zZRQ;
	}

	public void setZZRQ(Date zZRQ) {
		if (this.zZRQ != zZRQ) {
			this.zZRQ = zZRQ;
			modify_zZRQ = true;
		}
	}
	
	private Double sYQX;
	private boolean modify_sYQX = false;

	public Double getSYQX() {
		return sYQX;
	}

	public void setSYQX(Double sYQX) {
		if (this.sYQX != sYQX) {
			this.sYQX = sYQX;
			modify_sYQX = true;
		}
	}
	
	private String qLXZ;
	private boolean modify_qLXZ = false;

	public String getQLXZ() {
		return qLXZ;
	}

	public void setQLXZ(String qLXZ) {
		if (this.qLXZ != qLXZ) {
			this.qLXZ = qLXZ;
			modify_qLXZ = true;
		}
	}
	
	private Double tDMJ;
	private boolean modify_tDMJ = false;

	public Double getTDMJ() {
		return tDMJ;
	}

	public void setTDMJ(Double tDMJ) {
		if (this.tDMJ != tDMJ) {
			this.tDMJ = tDMJ;
			modify_tDMJ = true;
		}
	}

	private Date cREATETIME;
	private boolean modify_cREATETIME = false;

	public Date getCREATETIME() {
		return cREATETIME;
	}

	public void setCREATETIME(Date cREATETIME) {
		if (this.cREATETIME != cREATETIME) {
			this.cREATETIME = cREATETIME;
			modify_cREATETIME = true;
		}
	}

	private Date mODIFYTIME;
	private boolean modify_mODIFYTIME = false;

	public Date getMODIFYTIME() {
		return mODIFYTIME;
	}

	public void setMODIFYTIME(Date mODIFYTIME) {
		if (this.mODIFYTIME != mODIFYTIME) {
			this.mODIFYTIME = mODIFYTIME;
			modify_mODIFYTIME = true;
		}
	}
	
	private String zYSZ;
	private boolean modify_zYSZ = false;

	public String getZYSZ() {
		return zYSZ;
	}

	public void setZYSZ(String zYSZ) {
		if (this.zYSZ != zYSZ) {
			this.zYSZ = zYSZ;
			modify_zYSZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_xMBH = false;
		modify_sFZYT = false;
		modify_tDYT = false;
		modify_tDYTMC = false;
		modify_tDDJ = false;
		modify_tDJG = false;
		modify_qSRQ = false;
		modify_zZRQ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_sYQX = false;
		modify_cRJBZ = false;
		modify_qLXZ = false;
		modify_tDMJ = false;
		modify_zYSZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_sFZYT)
			listStrings.add("sFZYT");
		if (!modify_tDYT)
			listStrings.add("tDYT");
		if (!modify_tDYTMC)
			listStrings.add("tDYTMC");
		if (!modify_tDDJ)
			listStrings.add("tDDJ");
		if (!modify_tDJG)
			listStrings.add("tDJG");
		if (!modify_qSRQ)
			listStrings.add("qSRQ");
		if (!modify_zZRQ)
			listStrings.add("zZRQ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_sYQX)
			listStrings.add("sYQX");
		if (!modify_cRJBZ)
			listStrings.add("cRJBZ");
		if (!modify_qLXZ)
			listStrings.add("qLXZ");
		if (!modify_tDMJ)
			listStrings.add("tDMJ");
		if (!modify_zYSZ)
			listStrings.add("zYSZ");	

		return StringHelper.ListToStrings(listStrings);
	}

	private String cRJBZ;
	private boolean modify_cRJBZ = false;
	
	public String getCRJBZ() {
		return cRJBZ;
	}

	public void setCRJBZ(String cRJBZ) {
		if (this.cRJBZ != cRJBZ) {
			this.cRJBZ = cRJBZ;
			modify_cRJBZ = true;
		}
	}
}
