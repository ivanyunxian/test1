package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/11/5 
//* ----------------------------------------
//* Internal Entity bdcs_reportinfo 
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

public class GenerateBDCS_REPORTINFO implements SuperModel<String> {

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

	private Date rEPORTTIME;
	private boolean modify_rEPORTTIME = false;

	public Date getREPORTTIME() {
		return rEPORTTIME;
	}

	public void setREPORTTIME(Date rEPORTTIME) {
		if (this.rEPORTTIME != rEPORTTIME) {
			this.rEPORTTIME = rEPORTTIME;
			modify_rEPORTTIME = true;
		}
	}

	private String rEPORTUSER;
	private boolean modify_rEPORTUSER = false;

	public String getREPORTUSER() {
		return rEPORTUSER;
	}

	public void setREPORTUSER(String rEPORTUSER) {
		if (this.rEPORTUSER != rEPORTUSER) {
			this.rEPORTUSER = rEPORTUSER;
			modify_rEPORTUSER = true;
		}
	}

	private String bIZMSGID;
	private boolean modify_bIZMSGID = false;

	public String getBIZMSGID() {
		return bIZMSGID;
	}

	public void setBIZMSGID(String bIZMSGID) {
		if (this.bIZMSGID != bIZMSGID) {
			this.bIZMSGID = bIZMSGID;
			modify_bIZMSGID = true;
		}
	}

	private String dJDYID;
	private boolean modify_dJDYID = false;

	public String getDJDYID() {
		return dJDYID;
	}

	public void setDJDYID(String dJDYID) {
		if (this.dJDYID != dJDYID) {
			this.dJDYID = dJDYID;
			modify_dJDYID = true;
		}
	}

	private String qLID;
	private boolean modify_qLID = false;

	public String getQLID() {
		return qLID;
	}

	public void setQLID(String qLID) {
		if (this.qLID != qLID) {
			this.qLID = qLID;
			modify_qLID = true;
		}
	}

	private String pROINSTID;
	private boolean modify_pROINSTID = false;

	public String getPROINSTID() {
		return pROINSTID;
	}

	public void setPROINSTID(String pROINSTID) {
		if (this.pROINSTID != pROINSTID) {
			this.pROINSTID = pROINSTID;
			modify_pROINSTID = true;
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

	private String rECTYPE;
	private boolean modify_rECTYPE = false;

	public String getRECTYPE() {
		return rECTYPE;
	}

	public void setRECTYPE(String rECTYPE) {
		if (this.rECTYPE != rECTYPE) {
			this.rECTYPE = rECTYPE;
			modify_rECTYPE = true;
		}
	}

	private String rEPORTCONTENT;
	private boolean modify_rEPORTCONTENT = false;

	public String getREPORTCONTENT() {
		return rEPORTCONTENT;
	}

	public void setREPORTCONTENT(String rEPORTCONTENT) {
		if (this.rEPORTCONTENT != rEPORTCONTENT) {
			this.rEPORTCONTENT = rEPORTCONTENT;
			modify_rEPORTCONTENT = true;
		}
	}

	private String rEPORTTYPE;
	private boolean modify_rEPORTTYPE = false;

	public String getREPORTTYPE() {
		return rEPORTTYPE;
	}

	public void setREPORTTYPE(String rEPORTTYPE) {
		if (this.rEPORTTYPE != rEPORTTYPE) {
			this.rEPORTTYPE = rEPORTTYPE;
			modify_rEPORTTYPE = true;
		}
	}

	private String sUCCESSFLAG;
	private boolean modify_sUCCESSFLAG = false;

	public String getSUCCESSFLAG() {
		return sUCCESSFLAG;
	}

	public void setSUCCESSFLAG(String sUCCESSFLAG) {
		if (this.sUCCESSFLAG != sUCCESSFLAG) {
			this.sUCCESSFLAG = sUCCESSFLAG;
			modify_sUCCESSFLAG = true;
		}
	}

	private String rESPONSECODE;
	private boolean modify_rESPONSECODE = false;

	public String getRESPONSECODE() {
		return rESPONSECODE;
	}

	public void setRESPONSECODE(String rESPONSECODE) {
		if (this.rESPONSECODE != rESPONSECODE) {
			this.rESPONSECODE = rESPONSECODE;
			modify_rESPONSECODE = true;
		}
	}

	private String rESPONSEINFO;
	private boolean modify_rESPONSEINFO = false;

	public String getRESPONSEINFO() {
		return rESPONSEINFO;
	}

	public void setRESPONSEINFO(String rESPONSEINFO) {
		if (this.rESPONSEINFO != rESPONSEINFO) {
			this.rESPONSEINFO = rESPONSEINFO;
			modify_rESPONSEINFO = true;
		}
	}
	
	private String lOCALCHECK;
	private boolean modify_lOCALCHECK = false;

	public String getLOCALCHECK() {
		return lOCALCHECK;
	}

	public void setLOCALCHECK(String lOCALCHECK) {
		if (this.lOCALCHECK != lOCALCHECK) {
			this.lOCALCHECK = lOCALCHECK;
			modify_lOCALCHECK = true;
		}
	}
	
	private String lOCALCHECKINFO;
	private boolean modify_lOCALCHECKINFO = false;

	public String getLOCALCHECKINFO() {
		return lOCALCHECKINFO;
	}

	public void setLOCALCHECKINFO(String lOCALCHECKINFO) {
		if (this.lOCALCHECKINFO != lOCALCHECKINFO) {
			this.lOCALCHECKINFO = lOCALCHECKINFO;
			modify_lOCALCHECKINFO = true;
		}
	}
	
	private String cERTID;
	private boolean modify_cERTID = false;

	public String getCERTID() {
		return cERTID;
	}

	public void setCERTID(String cERTID) {
		if (this.cERTID != cERTID) {
			this.cERTID = cERTID;
			modify_cERTID = true;
		}
	}
	
	private String qRCODE;
	private boolean modify_qRCODE = false;

	public String getQRCODE() {
		return qRCODE;
	}

	public void setQRCODE(String qRCODE) {
		if (this.qRCODE != qRCODE) {
			this.qRCODE = qRCODE;
			modify_qRCODE = true;
		}
	}
	
	private Integer rESCOUNT;
	private boolean modify_rESCOUNT = false;

	public Integer getRESCOUNT() {
		return rESCOUNT;
	}

	public void setRESCOUNT(Integer rESCOUNT) {
		if (this.rESCOUNT != rESCOUNT) {
			this.rESCOUNT = rESCOUNT;
			modify_rESCOUNT = true;
		}
	}

	private String rESPENSECONTENT;
	private boolean modify_rESPENSECONTENT = false;

	public String getRESPENSECONTENT() {
		return rESPENSECONTENT;
	}

	public void setRESPENSECONTENT(String rESPENSECONTENT) {
		if (this.rESPENSECONTENT != rESPENSECONTENT) {
			this.rESPENSECONTENT = rESPENSECONTENT;
			modify_rESPENSECONTENT = true;
		}
	}
	
	private Date lASTRESTIME;
	private boolean modify_lASTRESTIME = false;

	public Date getLASTRESTIME() {
		return lASTRESTIME;
	}

	public void setLASTRESTIME(Date lASTRESTIME) {
		if (this.lASTRESTIME != lASTRESTIME) {
			this.lASTRESTIME = lASTRESTIME;
			modify_lASTRESTIME = true;
		}
	}

	private String yXBZ;
	private boolean modify_yXBZ = false;

	public String getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(String yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_rEPORTTIME = false;
		modify_rEPORTUSER = false;
		modify_bIZMSGID = false;
		modify_dJDYID = false;
		modify_qLID = false;
		modify_pROINSTID = false;
		modify_xMBH = false;
		modify_rECTYPE = false;
		modify_rEPORTCONTENT = false;
		modify_rEPORTTYPE = false;
		modify_sUCCESSFLAG = false;
		modify_rESPONSECODE = false;
		modify_rESPONSEINFO = false;
		modify_rESPENSECONTENT = false;
		modify_yXBZ = false;
		modify_cERTID=false;
		modify_qRCODE=false;
		modify_rESCOUNT=false;
		modify_lASTRESTIME=false;
		modify_lOCALCHECK=false;
		modify_lOCALCHECKINFO=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_rEPORTTIME)
			listStrings.add("rEPORTTIME");
		if (!modify_rEPORTUSER)
			listStrings.add("rEPORTUSER");
		if (!modify_bIZMSGID)
			listStrings.add("bIZMSGID");
		if (!modify_dJDYID)
			listStrings.add("dJDYID");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_pROINSTID)
			listStrings.add("pROINSTID");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_rECTYPE)
			listStrings.add("rECTYPE");
		if (!modify_rEPORTCONTENT)
			listStrings.add("rEPORTCONTENT");
		if (!modify_rEPORTTYPE)
			listStrings.add("rEPORTTYPE");
		if (!modify_sUCCESSFLAG)
			listStrings.add("sUCCESSFLAG");
		if (!modify_rESPONSECODE)
			listStrings.add("rESPONSECODE");
		if (!modify_rESPONSEINFO)
			listStrings.add("rESPONSEINFO");
		if (!modify_rESPENSECONTENT)
			listStrings.add("rESPENSECONTENT");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if(!modify_cERTID)
			listStrings.add("cERTID");
		if(!modify_qRCODE)
			listStrings.add("qRCODE");
		if(!modify_rESCOUNT)
			listStrings.add("rESCOUNT");
		if(!modify_lASTRESTIME){
			listStrings.add("lASTRESTIME");
		}
		if(!modify_lOCALCHECK)
			listStrings.add("lOCALCHECK");
		if(!modify_lOCALCHECKINFO)
			listStrings.add("lOCALCHECKINFO");

		return StringHelper.ListToStrings(listStrings);
	}
}
