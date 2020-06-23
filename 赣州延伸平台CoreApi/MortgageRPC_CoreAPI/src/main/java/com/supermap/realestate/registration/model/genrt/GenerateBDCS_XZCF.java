package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/14 
//* ----------------------------------------
//* Internal Entity bdcs_xzcf 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;

import com.supermap.wisdombusiness.core.SuperModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_XZCF implements SuperModel<String> {

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

	private String yTDZID;
	private boolean modify_yTDZID = false;

	public String getYTDZID() {
		return yTDZID;
	}

	public void setYTDZID(String yTDZID) {
		if (this.yTDZID != yTDZID) {
			this.yTDZID = yTDZID;
			modify_yTDZID = true;
		}
	}

	private Double zSZT;
	private boolean modify_zSZT = false;

	public Double getZSZT() {
		return zSZT;
	}

	public void setZSZT(Double zSZT) {
		if (this.zSZT != zSZT) {
			this.zSZT = zSZT;
			modify_zSZT = true;
		}
	}

	private String dJJG;
	private boolean modify_dJJG = false;

	public String getDJJG() {
		return dJJG;
	}

	public void setDJJG(String dJJG) {
		if (this.dJJG != dJJG) {
			this.dJJG = dJJG;
			modify_dJJG = true;
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

	private String zDID;
	private boolean modify_zDID = false;

	public String getZDID() {
		return zDID;
	}

	public void setZDID(String zDID) {
		if (this.zDID != zDID) {
			this.zDID = zDID;
			modify_zDID = true;
		}
	}

	private String qLRID;
	private boolean modify_qLRID = false;

	public String getQLRID() {
		return qLRID;
	}

	public void setQLRID(String qLRID) {
		if (this.qLRID != qLRID) {
			this.qLRID = qLRID;
			modify_qLRID = true;
		}
	}

	private Double cFCS;
	private boolean modify_cFCS = false;

	public Double getCFCS() {
		return cFCS;
	}

	public void setCFCS(Double cFCS) {
		if (this.cFCS != cFCS) {
			this.cFCS = cFCS;
			modify_cFCS = true;
		}
	}

	private String sPFGRMXID;
	private boolean modify_sPFGRMXID = false;

	public String getSPFGRMXID() {
		return sPFGRMXID;
	}

	public void setSPFGRMXID(String sPFGRMXID) {
		if (this.sPFGRMXID != sPFGRMXID) {
			this.sPFGRMXID = sPFGRMXID;
			modify_sPFGRMXID = true;
		}
	}

	private String sQSJID;
	private boolean modify_sQSJID = false;

	public String getSQSJID() {
		return sQSJID;
	}

	public void setSQSJID(String sQSJID) {
		if (this.sQSJID != sQSJID) {
			this.sQSJID = sQSJID;
			modify_sQSJID = true;
		}
	}

	private String cFID;
	private boolean modify_cFID = false;

	public String getCFID() {
		return cFID;
	}

	public void setCFID(String cFID) {
		if (this.cFID != cFID) {
			this.cFID = cFID;
			modify_cFID = true;
		}
	}

	private String bGSPBID;
	private boolean modify_bGSPBID = false;

	public String getBGSPBID() {
		return bGSPBID;
	}

	public void setBGSPBID(String bGSPBID) {
		if (this.bGSPBID != bGSPBID) {
			this.bGSPBID = bGSPBID;
			modify_bGSPBID = true;
		}
	}

	private String t_ZID;
	private boolean modify_t_ZID = false;

	public String getT_ZID() {
		return t_ZID;
	}

	public void setT_ZID(String t_ZID) {
		if (this.t_ZID != t_ZID) {
			this.t_ZID = t_ZID;
			modify_t_ZID = true;
		}
	}

	private String tDZH;
	private boolean modify_tDZH = false;

	public String getTDZH() {
		return tDZH;
	}

	public void setTDZH(String tDZH) {
		if (this.tDZH != tDZH) {
			this.tDZH = tDZH;
			modify_tDZH = true;
		}
	}

	private String bS;
	private boolean modify_bS = false;

	public String getBS() {
		return bS;
	}

	public void setBS(String bS) {
		if (this.bS != bS) {
			this.bS = bS;
			modify_bS = true;
		}
	}

	private String dABH;
	private boolean modify_dABH = false;

	public String getDABH() {
		return dABH;
	}

	public void setDABH(String dABH) {
		if (this.dABH != dABH) {
			this.dABH = dABH;
			modify_dABH = true;
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

	private String dJH;
	private boolean modify_dJH = false;

	public String getDJH() {
		return dJH;
	}

	public void setDJH(String dJH) {
		if (this.dJH != dJH) {
			this.dJH = dJH;
			modify_dJH = true;
		}
	}

	private String cFWJ;
	private boolean modify_cFWJ = false;

	public String getCFWJ() {
		return cFWJ;
	}

	public void setCFWJ(String cFWJ) {
		if (this.cFWJ != cFWJ) {
			this.cFWJ = cFWJ;
			modify_cFWJ = true;
		}
	}

	private String cFWH;
	private boolean modify_cFWH = false;

	public String getCFWH() {
		return cFWH;
	}

	public void setCFWH(String cFWH) {
		if (this.cFWH != cFWH) {
			this.cFWH = cFWH;
			modify_cFWH = true;
		}
	}

	private String cFFY;
	private boolean modify_cFFY = false;

	public String getCFFY() {
		return cFFY;
	}

	public void setCFFY(String cFFY) {
		if (this.cFFY != cFFY) {
			this.cFFY = cFFY;
			modify_cFFY = true;
		}
	}

	private Date cFRQ;
	private boolean modify_cFRQ = false;

	public Date getCFRQ() {
		return cFRQ;
	}

	public void setCFRQ(Date cFRQ) {
		if (this.cFRQ != cFRQ) {
			this.cFRQ = cFRQ;
			modify_cFRQ = true;
		}
	}

	private String fJ;
	private boolean modify_fJ = false;

	public String getFJ() {
		return fJ;
	}

	public void setFJ(String fJ) {
		if (this.fJ != fJ) {
			this.fJ = fJ;
			modify_fJ = true;
		}
	}

	private Date cFQSSJ;
	private boolean modify_cFQSSJ = false;

	public Date getCFQSSJ() {
		return cFQSSJ;
	}

	public void setCFQSSJ(Date cFQSSJ) {
		if (this.cFQSSJ != cFQSSJ) {
			this.cFQSSJ = cFQSSJ;
			modify_cFQSSJ = true;
		}
	}

	private Date cFJZRQ;
	private boolean modify_cFJZRQ = false;

	public Date getCFJZRQ() {
		return cFJZRQ;
	}

	public void setCFJZRQ(Date cFJZRQ) {
		if (this.cFJZRQ != cFJZRQ) {
			this.cFJZRQ = cFJZRQ;
			modify_cFJZRQ = true;
		}
	}

	private String dBR;
	private boolean modify_dBR = false;

	public String getDBR() {
		return dBR;
	}

	public void setDBR(String dBR) {
		if (this.dBR != dBR) {
			this.dBR = dBR;
			modify_dBR = true;
		}
	}

	private Date dJSJ;
	private boolean modify_dJSJ = false;

	public Date getDJSJ() {
		return dJSJ;
	}

	public void setDJSJ(Date dJSJ) {
		if (this.dJSJ != dJSJ) {
			this.dJSJ = dJSJ;
			modify_dJSJ = true;
		}
	}

	private String qLR;
	private boolean modify_qLR = false;

	public String getQLR() {
		return qLR;
	}

	public void setQLR(String qLR) {
		if (this.qLR != qLR) {
			this.qLR = qLR;
			modify_qLR = true;
		}
	}

	private String zL;
	private boolean modify_zL = false;

	public String getZL() {
		return zL;
	}

	public void setZL(String zL) {
		if (this.zL != zL) {
			this.zL = zL;
			modify_zL = true;
		}
	}

	private Double zDMJ;
	private boolean modify_zDMJ = false;

	public Double getZDMJ() {
		return zDMJ;
	}

	public void setZDMJ(Double zDMJ) {
		if (this.zDMJ != zDMJ) {
			this.zDMJ = zDMJ;
			modify_zDMJ = true;
		}
	}

	private String tDZID;
	private boolean modify_tDZID = false;

	public String getTDZID() {
		return tDZID;
	}

	public void setTDZID(String tDZID) {
		if (this.tDZID != tDZID) {
			this.tDZID = tDZID;
			modify_tDZID = true;
		}
	}

	private Double yXBZ;
	private boolean modify_yXBZ = false;

	public Double getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(Double yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	private String zDBDCDYID;
	private boolean modify_zDBDCDYID = false;

	public String getZDBDCDYID() {
		return zDBDCDYID;
	}

	public void setZDBDCDYID(String zDBDCDYID) {
		if (this.zDBDCDYID != zDBDCDYID) {
			this.zDBDCDYID = zDBDCDYID;
			modify_zDBDCDYID = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_yTDZID = false;
		modify_zSZT = false;
		modify_dJJG = false;
		modify_bZ = false;
		modify_zDID = false;
		modify_qLRID = false;
		modify_cFCS = false;
		modify_sPFGRMXID = false;
		modify_sQSJID = false;
		modify_cFID = false;
		modify_bGSPBID = false;
		modify_t_ZID = false;
		modify_tDZH = false;
		modify_bS = false;
		modify_dABH = false;
		modify_xMBH = false;
		modify_dJH = false;
		modify_cFWJ = false;
		modify_cFWH = false;
		modify_cFFY = false;
		modify_cFRQ = false;
		modify_fJ = false;
		modify_cFQSSJ = false;
		modify_cFJZRQ = false;
		modify_dBR = false;
		modify_dJSJ = false;
		modify_qLR = false;
		modify_zL = false;
		modify_zDMJ = false;
		modify_tDZID = false;
		modify_yXBZ = false;
		modify_zDBDCDYID = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_yTDZID)
			listStrings.add("yTDZID");
		if (!modify_zSZT)
			listStrings.add("zSZT");
		if (!modify_dJJG)
			listStrings.add("dJJG");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_zDID)
			listStrings.add("zDID");
		if (!modify_qLRID)
			listStrings.add("qLRID");
		if (!modify_cFCS)
			listStrings.add("cFCS");
		if (!modify_sPFGRMXID)
			listStrings.add("sPFGRMXID");
		if (!modify_sQSJID)
			listStrings.add("sQSJID");
		if (!modify_cFID)
			listStrings.add("cFID");
		if (!modify_bGSPBID)
			listStrings.add("bGSPBID");
		if (!modify_t_ZID)
			listStrings.add("t_ZID");
		if (!modify_tDZH)
			listStrings.add("tDZH");
		if (!modify_bS)
			listStrings.add("bS");
		if (!modify_dABH)
			listStrings.add("dABH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_dJH)
			listStrings.add("dJH");
		if (!modify_cFWJ)
			listStrings.add("cFWJ");
		if (!modify_cFWH)
			listStrings.add("cFWH");
		if (!modify_cFFY)
			listStrings.add("cFFY");
		if (!modify_cFRQ)
			listStrings.add("cFRQ");
		if (!modify_fJ)
			listStrings.add("fJ");
		if (!modify_cFQSSJ)
			listStrings.add("cFQSSJ");
		if (!modify_cFJZRQ)
			listStrings.add("cFJZRQ");
		if (!modify_dBR)
			listStrings.add("dBR");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if (!modify_qLR)
			listStrings.add("qLR");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_zDMJ)
			listStrings.add("zDMJ");
		if (!modify_tDZID)
			listStrings.add("tDZID");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");

		return StringHelper.ListToStrings(listStrings);
	}
}
