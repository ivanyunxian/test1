package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity tdyt 
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
public class GenerateTDYT implements SuperModel<String> {

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

	private String rELATIONID;
	private boolean modify_rELATIONID = false;

	public String getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
			modify_rELATIONID = true;
		}
	}

	private String gXLX;
	private boolean modify_gXLX = false;

	public String getGXLX() {
		return gXLX;
	}

	public void setGXLX(String gXLX) {
		if (this.gXLX != gXLX) {
			this.gXLX = gXLX;
			modify_gXLX = true;
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

	private Date qLQSRQ;
	private boolean modify_qLQSRQ = false;

	public Date getQLQSRQ() {
		return qLQSRQ;
	}

	public void setQLQSRQ(Date qLQSRQ) {
		if (this.qLQSRQ != qLQSRQ) {
			this.qLQSRQ = qLQSRQ;
			modify_qLQSRQ = true;
		}
	}

	private Date qLZZRQ;
	private boolean modify_qLZZRQ = false;

	public Date getQLZZRQ() {
		return qLZZRQ;
	}

	public void setQLZZRQ(Date qLZZRQ) {
		if (this.qLZZRQ != qLZZRQ) {
			this.qLZZRQ = qLZZRQ;
			modify_qLZZRQ = true;
		}
	}

	private Integer sYQX;
	private boolean modify_sYQX = false;

	public Integer getSYQX() {
		return sYQX;
	}

	public void setSYQX(Integer sYQX) {
		if (this.sYQX != sYQX) {
			this.sYQX = sYQX;
			modify_sYQX = true;
		}
	}

	private Double cRJBZ;
	private boolean modify_cRJBZ = false;

	public Double getCRJBZ() {
		return cRJBZ;
	}

	public void setCRJBZ(Double cRJBZ) {
		if (this.cRJBZ != cRJBZ) {
			this.cRJBZ = cRJBZ;
			modify_cRJBZ = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_gXXMBH = false;
		modify_bZ = false;
		modify_rELATIONID = false;
		modify_gXLX = false;
		modify_tDYT = false;
		modify_tDYTMC = false;
		modify_sFZYT = false;
		modify_tDDJ = false;
		modify_tDJG = false;
		modify_qLQSRQ = false;
		modify_qLZZRQ = false;
		modify_sYQX = false;
		modify_cRJBZ = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_gXLX)
			listStrings.add("gXLX");
		if (!modify_tDYT)
			listStrings.add("tDYT");
		if (!modify_tDYTMC)
			listStrings.add("tDYTMC");
		if (!modify_sFZYT)
			listStrings.add("sFZYT");
		if (!modify_tDDJ)
			listStrings.add("tDDJ");
		if (!modify_tDJG)
			listStrings.add("tDJG");
		if (!modify_qLQSRQ)
			listStrings.add("qLQSRQ");
		if (!modify_qLZZRQ)
			listStrings.add("qLZZRQ");
		if (!modify_sYQX)
			listStrings.add("sYQX");
		if (!modify_cRJBZ)
			listStrings.add("cRJBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
