package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-11 
//* ----------------------------------------
//* Internal Entity wfd_mapping 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateWFD_MAPPING implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null) {
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

	private String wORKFLOWCODE;
	private boolean modify_wORKFLOWCODE = false;

	public String getWORKFLOWCODE() {
		return wORKFLOWCODE;
	}

	public void setWORKFLOWCODE(String wORKFLOWCODE) {
		if (this.wORKFLOWCODE != wORKFLOWCODE) {
			this.wORKFLOWCODE = wORKFLOWCODE;
			modify_wORKFLOWCODE = true;
		}
	}

	private String wORKFLOWNAME;
	private boolean modify_wORKFLOWNAME = false;

	public String getWORKFLOWNAME() {
		return wORKFLOWNAME;
	}

	public void setWORKFLOWNAME(String wORKFLOWNAME) {
		if (this.wORKFLOWNAME != wORKFLOWNAME) {
			this.wORKFLOWNAME = wORKFLOWNAME;
			modify_wORKFLOWNAME = true;
		}
	}

	private String wORKFLOWCAPTION;
	private boolean modify_wORKFLOWCAPTION = false;

	public String getWORKFLOWCAPTION() {
		return wORKFLOWCAPTION;
	}

	public void setWORKFLOWCAPTION(String wORKFLOWCAPTION) {
		if (this.wORKFLOWCAPTION != wORKFLOWCAPTION) {
			this.wORKFLOWCAPTION = wORKFLOWCAPTION;
			modify_wORKFLOWCAPTION = true;
		}
	}

	private String hOUSEEDIT;
	private boolean modify_hOUSEEDIT = false;

	public String getHOUSEEDIT() {
		return hOUSEEDIT;
	}

	public void setHOUSEEDIT(String hOUSEEDIT) {
		if (this.hOUSEEDIT != hOUSEEDIT) {
			this.hOUSEEDIT = hOUSEEDIT;
			modify_hOUSEEDIT = true;
		}
	}

	private String lANDEDIT;
	private boolean modify_lANDEDIT = false;

	public String getLANDEDIT() {
		return lANDEDIT;
	}

	public void setLANDEDIT(String hOUSEEDIT) {
		if (this.lANDEDIT != hOUSEEDIT) {
			this.lANDEDIT = hOUSEEDIT;
			modify_lANDEDIT = true;
		}
	}

	private String nEWQZH;
	private boolean modify_nEWQZH = false;

	public String getNEWQZH() {
		return nEWQZH;
	}

	public void setNEWQZH(String nEWQZH) {
		if (this.nEWQZH != nEWQZH) {
			this.nEWQZH = nEWQZH;
			modify_nEWQZH = true;
		}
	}

	private String sHOWDATAREPORTBTN;
	private boolean modify_sHOWDATAREPORTBTN = false;

	public String getSHOWDATAREPORTBTN() {
		return sHOWDATAREPORTBTN;
	}

	private String sHOWBUILDINGTABLE;
	private boolean modify_sHOWBUILDINGTABLE = false;

	public String getSHOWBUILDINGTABLE() {
		return sHOWBUILDINGTABLE;
	}

	public void setSHOWBUILDINGTABLE(String sHOWBUILDINGTABLE) {
		if (this.sHOWBUILDINGTABLE != sHOWBUILDINGTABLE) {
			this.sHOWBUILDINGTABLE = sHOWBUILDINGTABLE;
			modify_sHOWBUILDINGTABLE = true;
		}
	}

	public void setSHOWDATAREPORTBTN(String sHOWDATAREPORTBTN) {
		if (this.sHOWDATAREPORTBTN != sHOWDATAREPORTBTN) {
			this.sHOWDATAREPORTBTN = sHOWDATAREPORTBTN;
			modify_sHOWDATAREPORTBTN = true;
		}
	}

	private String dYFS;
	private boolean modify_dYFS = false;

	public String getDYFS() {
		return dYFS;
	}

	public void setDYFS(String dYFS) {
		if (this.dYFS != dYFS) {
			this.dYFS = dYFS;
			modify_dYFS = true;
		}
	}

	private String cZFS;
	private boolean modify_cZFS = false;

	public String getCZFS() {
		return cZFS;
	}

	public void setCZFS(String cZFS) {
		if (this.cZFS != cZFS) {
			this.cZFS = cZFS;
			modify_cZFS = true;
		}
	}

	private String sFHBZS;
	private boolean modify_sFHBZS = false;

	public String getSFHBZS() {
		return sFHBZS;
	}

	public void setSFHBZS(String sFHBZS) {
		if (this.sFHBZS != sFHBZS) {
			this.sFHBZS = sFHBZS;
			modify_sFHBZS = true;
		}
	}

	private String uNITPAGEID;
	private boolean modify_uNITPAGEID = false;

	public String getUNITPAGEID() {
		return uNITPAGEID;
	}

	public void setUNITPAGEID(String uNITPAGEID) {
		if (this.uNITPAGEID != uNITPAGEID) {
			this.uNITPAGEID = uNITPAGEID;
			modify_uNITPAGEID = true;
		}
	}

	private String rIGHTPAGEID;
	private boolean modify_rIGHTPAGEID = false;

	public String getRIGHTPAGEID() {
		return rIGHTPAGEID;
	}

	public void setRIGHTPAGEID(String rIGHTPAGEID) {
		if (this.rIGHTPAGEID != rIGHTPAGEID) {
			this.rIGHTPAGEID = rIGHTPAGEID;
			modify_rIGHTPAGEID = true;
		}
	}

	private String pUSHTOZJK="1";
	private boolean modify_pUSHTOZJK = false;

	public String getPUSHTOZJK() {
		return pUSHTOZJK;
	}

	public void setPUSHTOZJK(String pUSHTOZJK) {
		if (this.pUSHTOZJK != pUSHTOZJK) {
			this.pUSHTOZJK = pUSHTOZJK;
			modify_pUSHTOZJK = true;
		}
	}

	private String sFYCXZ;
	private boolean modify_sFYCXZ = false;

	public String getSFYCXZ() {
		return sFYCXZ;
	}

	public void setSFYCXZ(String sFYCXZ) {
		if (this.sFYCXZ != sFYCXZ) {
			this.sFYCXZ = sFYCXZ;
			modify_sFYCXZ = true;
		}
	}

	private String yCXZLX;
	private boolean modify_yCXZLX = false;

	public String getYCXZLX() {
		return yCXZLX;
	}

	public void setYCXZLX(String yCXZLX) {
		if (this.yCXZLX != yCXZLX) {
			this.yCXZLX = yCXZLX;
			modify_yCXZLX = true;
		}
	}

	private String sFADDPZMJ;
	private boolean modify_sFADDPZMJ = false;

	public String getSFADDPZMJ() {
		return sFADDPZMJ;
	}

	public void setSFADDPZMJ(String sFADDPZMJ) {
		if (this.sFADDPZMJ != sFADDPZMJ) {
			this.sFADDPZMJ = sFADDPZMJ;
			modify_sFADDPZMJ = true;
		}
	}

	private String dATASTYLE;
	private boolean modify_dATASTYLE = false;

	public String getDATASTYLE() {
		return dATASTYLE;
	}

	public void setDATASTYLE(String dATASTYLE) {
		if (this.dATASTYLE != dATASTYLE) {
			this.dATASTYLE = dATASTYLE;
			modify_dATASTYLE = true;
		}
	}

	/**
	 * 是否继承产权的权利人
	 */
	private String sFJCCQDQLR;
	private boolean modify_sFJCCQDQLR = false;

	public String getSFJCCQDQLR() {
		return sFJCCQDQLR;
	}

	public void setSFJCCQDQLR(String sFJCCQDQLR) {
		if (this.sFJCCQDQLR != sFJCCQDQLR) {
			this.sFJCCQDQLR = sFJCCQDQLR;
			modify_sFJCCQDQLR = true;
		}
	}
	
	/**
	 * 是否继承产权的权利人
	 */
	private String iSREMOVESEAL;
	private boolean modify_iSREMOVESEAL = false;

	public String getISREMOVESEAL() {
		return iSREMOVESEAL;
	}

	public void setISREMOVESEAL(String iSREMOVESEAL) {
		if (this.iSREMOVESEAL != iSREMOVESEAL) {
			this.iSREMOVESEAL = iSREMOVESEAL;
			modify_iSREMOVESEAL = true;
		}
	}

	/**
	 * 是否同时解除期房抵押
	 */
	private String iSUNLOCKYCHDY;
	private boolean modify_iSUNLOCKYCHDY = false;

	public String getISUNLOCKYCHDY() {
		return iSUNLOCKYCHDY;
	}

	public void setISUNLOCKYCHDY(String iSUNLOCKYCHDY) {
		if (this.iSUNLOCKYCHDY != iSUNLOCKYCHDY) {
			this.iSUNLOCKYCHDY = iSUNLOCKYCHDY;
			modify_iSUNLOCKYCHDY = true;
		}
	}
	
	/**
	 * 是否在附记中初始化权利状态
	 */
	private String iSINITATATUS;
	private boolean modify_iSINITATATUS = false;

	public String getISINITATATUS() {
		return iSINITATATUS;
	}

	public void setISINITATATUS(String iSINITATATUS) {
		if (this.iSINITATATUS != iSINITATATUS) {
			this.iSINITATATUS = iSINITATATUS;
			modify_iSINITATATUS = true;
		}
	}
	
	/**
	 * 是否在附记中初始化权利状态
	 */
	private String cERTMODE;
	private boolean modify_cERTMODE = false;

	public String getCERTMODE() {
		return cERTMODE;
	}

	public void setCERTMODE(String cERTMODE) {
		if (this.cERTMODE != cERTMODE) {
			this.cERTMODE = cERTMODE;
			modify_cERTMODE = true;
		}
	}
	
	/**
	 * 是否移除预测抵押
	 */
	private String dELYCDY;
	private boolean modify_dELYCDY = false;

	public String getDELYCDY() {
		return dELYCDY;
	}

	public void setDELYCDY(String dELYCDY) {
		if (this.dELYCDY != dELYCDY) {
			this.dELYCDY = dELYCDY;
			modify_dELYCDY = true;
		}
	}
	private String deltdql;
	private boolean modify_deltdql = false;

	public String getDELTDQL() {
		return deltdql;
	}
	public void setDELTDQL(String deltdql) {
		if (this.deltdql != deltdql) {
			this.deltdql = deltdql;
			modify_deltdql = true;
		}
	}
	/**
	 * 审批表收件人 获取当前实例人员的名字
	 */
	private String rEADCONTRACTBTN;
	private boolean modify_rEADCONTRACTBTN = false;

	public String getREADCONTRACTBTN() {
		return rEADCONTRACTBTN;
	}

	public void setREADCONTRACTBTN(String rEADCONTRACTBTN) {
		if (this.rEADCONTRACTBTN != rEADCONTRACTBTN) {
			this.rEADCONTRACTBTN = rEADCONTRACTBTN;
			modify_rEADCONTRACTBTN = true;
		}
	}
	
	private String eNCRYPTION;
	private boolean modify_eNCRYPTION = false;

	public String getENCRYPTION() {
		return eNCRYPTION;
	}

	public void setENCRYPTION(String eNCRYPTION) {
		if (this.eNCRYPTION != eNCRYPTION) {
			this.eNCRYPTION = eNCRYPTION;
			modify_eNCRYPTION = true;
		}
	}
	
	private String sPBSJR;
	private boolean modify_sPBSJR = false;

	public String getSPBSJR() {
		return sPBSJR;
	}

	public void setSPBSJR(String sPBSJR) {
		if (this.sPBSJR != sPBSJR) {
			this.sPBSJR = sPBSJR;
			modify_sPBSJR = true;
		}
	}
	
	
	/**
	 *务流程配置表WFD_MAPPING增加预购商品房抵押预告转现房抵押时，是否自动读取期房的抵押信息——ISExtendMortgageInfo
	 */
	private String iSEXTENDMORTGAGEINFO;
	private boolean modify_iSEXTENDMORTGAGEINFO = false;
	
	public String getISEXTENDMORTGAGEINFO() {
		return iSEXTENDMORTGAGEINFO;
	}

	public void setISEXTENDMORTGAGEINFO(String iSEXTENDMORTGAGEINFO) {
		if (this.iSEXTENDMORTGAGEINFO != iSEXTENDMORTGAGEINFO) {
			this.iSEXTENDMORTGAGEINFO = iSEXTENDMORTGAGEINFO;
			modify_iSEXTENDMORTGAGEINFO = true;
		}
	
	}	

	/**
	 *业务流程配置表WFD_MAPPING增加默认附记---defaultFJ
	 */
	private String dEFAULTFJ;
	private boolean modify_dEFAULTFJ = false;
	
	public String getDEFAULTFJ() {
		return dEFAULTFJ;
	}

	public void setDEFAULTFJ(String dEFAULTFJ) {
		if (this.dEFAULTFJ != dEFAULTFJ) {
			this.dEFAULTFJ = dEFAULTFJ;
			modify_dEFAULTFJ = true;
		}
	}	
	/**
	 *业务流程配置表WFD_MAPPING增加预默认登记原因——defaultDJYY
	 */
	private String dEFAULTDJYY;
	private boolean modify_dEFAULTDJYY = false;
	
	public String getDEFAULTDJYY() {
		return dEFAULTDJYY;
	}

	public void setDEFAULTDJYY(String dEFAULTDJYY) {
		if (this.dEFAULTDJYY != dEFAULTDJYY) {
			this.dEFAULTDJYY = dEFAULTDJYY;
			modify_dEFAULTDJYY = true;
		}
	}	

	private String uSESPBDHTM;
	private boolean modify_uSESPBDHTM = false;
	
	public String getUSESPBDHTM() {
		return uSESPBDHTM;
	}

	public void setUSESPBDHTM(String uSESPBDHTM) {
		if (this.uSESPBDHTM != uSESPBDHTM) {
			this.uSESPBDHTM = uSESPBDHTM;
			modify_uSESPBDHTM = true;
		}
	}
	
	private String uSEAPPROVALHTM;
	private boolean modify_uSEAPPROVALHTM = false;
	
	public String getUSEAPPROVALHTM() {
		return uSEAPPROVALHTM;
	}

	public void setUSEAPPROVALHTM(String uSEAPPROVALHTM) {
		if (this.uSEAPPROVALHTM != uSEAPPROVALHTM) {
			this.uSEAPPROVALHTM = uSEAPPROVALHTM;
			modify_uSEAPPROVALHTM = true;
		}
	}
	
	private String uSEFJHTM;
	private boolean modify_uSEFJHTM = false;
	
	public String getUSEFJHTM() {
		return uSEFJHTM;
	}

	public void setUSEFJHTM(String uSEFJHTM) {
		if (this.uSEFJHTM != uSEFJHTM) {
			this.uSEFJHTM = uSEFJHTM;
			modify_uSEFJHTM = true;
		}
	}
	
	private String uSESQRHTM;
	private boolean modify_uSESQRHTM = false;
	
	public String getUSESQRHTM() {
		return uSESQRHTM;
	}

	public void setUSESQRHTM(String uSESQRHTM) {
		if (this.uSESQRHTM != uSESQRHTM) {
			this.uSESQRHTM = uSESQRHTM;
			modify_uSESQRHTM = true;
		}
	}
	
	private String cFCONFIG;
	private boolean modify_cFCONFIG = false;
	
	public String getCFCONFIG() {
		return cFCONFIG;
	}

	public void setCFCONFIG(String cFCONFIG) {
		if (this.cFCONFIG != cFCONFIG) {
			this.cFCONFIG = cFCONFIG;
			modify_cFCONFIG = true;
		}
	}

	private String iSALLOWADDSQR;
	@SuppressWarnings("unused")
	private boolean modify_iSALLOWADDSQR = false;
	
	public String getISALLOWADDSQR() {
		return iSALLOWADDSQR;
	}

	public void setISALLOWADDSQR(String iSALLOWADDSQR) {
		if (this.iSALLOWADDSQR != iSALLOWADDSQR) {
			this.iSALLOWADDSQR = iSALLOWADDSQR;
			modify_iSALLOWADDSQR = true;
		}
	}
	
	private String iSCOPYYCF2CF;
	private boolean modify_iSCOPYYCF2CF = false;
	
	public String getISCOPYYCF2CF() {
		return iSCOPYYCF2CF;
	}

	public void setISCOPYYCF2CF(String iSCOPYYCF2CF) {
		if (this.iSCOPYYCF2CF != iSCOPYYCF2CF) {
			this.iSCOPYYCF2CF = iSCOPYYCF2CF;
			modify_iSCOPYYCF2CF = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_wORKFLOWCODE = false;
		modify_wORKFLOWNAME = false;
		modify_wORKFLOWCAPTION = false;
		modify_hOUSEEDIT = false;
		modify_lANDEDIT = false;
		modify_nEWQZH = false;
		modify_sHOWDATAREPORTBTN = false;
		modify_sHOWBUILDINGTABLE = false;
		modify_dYFS = false;
		modify_cZFS = false;
		modify_sFHBZS = false;
		modify_uNITPAGEID = false;
		modify_rIGHTPAGEID = false;
		modify_pUSHTOZJK = false;
		modify_sFYCXZ = false;
		modify_yCXZLX = false;
		modify_sFADDPZMJ = false;
		modify_dATASTYLE = false;
		modify_sFJCCQDQLR = false;
		modify_iSREMOVESEAL = false;
		modify_iSUNLOCKYCHDY = false;
		modify_iSINITATATUS = false;
		modify_cERTMODE=false;
		modify_dELYCDY=false;
		modify_sPBSJR = false;

		modify_deltdql=false;

		modify_iSEXTENDMORTGAGEINFO = false;

		modify_dEFAULTFJ = false;
		modify_dEFAULTDJYY = false;

		
		modify_uSESPBDHTM = false;
		modify_uSEAPPROVALHTM = false;
		modify_uSEFJHTM = false;
		modify_uSESQRHTM = false;
		modify_cFCONFIG = false;
		modify_iSALLOWADDSQR =false;
		modify_iSCOPYYCF2CF = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_wORKFLOWCODE)
			listStrings.add("wORKFLOWCODE");
		if (!modify_wORKFLOWNAME)
			listStrings.add("wORKFLOWNAME");
		if (!modify_wORKFLOWCAPTION)
			listStrings.add("wORKFLOWCAPTION");
		if (!modify_hOUSEEDIT)
			listStrings.add("hOUSEEDIT");
		if (!modify_lANDEDIT)
			listStrings.add("lANDEDIT");
		if (!modify_nEWQZH)
			listStrings.add("nEWQZH");
		if (!modify_sHOWDATAREPORTBTN)
			listStrings.add("sHOWDATAREPORTBTN");
		if (!modify_sHOWBUILDINGTABLE)
			listStrings.add("sHOWBUILDINGTABLE");
		if (!modify_dYFS)
			listStrings.add("dYFS");
		if (!modify_cZFS)
			listStrings.add("cZFS");
		if (!modify_sFHBZS)
			listStrings.add("sFHBZS");
		if (!modify_uNITPAGEID) {
			listStrings.add("uNITPAGEID");
		}
		if (!modify_rIGHTPAGEID) {
			listStrings.add("rIGHTPAGEID");
		}
		if (!modify_pUSHTOZJK) {
			listStrings.add("pUSHTOZJK");
		}
		if (!modify_sFYCXZ) {
			listStrings.add("sFYCXZ");
		}
		if (!modify_yCXZLX) {
			listStrings.add("yCXZLX");
		}
		if (!modify_sFADDPZMJ) {
			listStrings.add("sFADDPZMJ");
		}
		if (!modify_dATASTYLE) {
			listStrings.add("dATASTYLE");
		}
		if (!modify_sFJCCQDQLR) {
			listStrings.add("sFJCCQDQLR");
		}
		if (!modify_iSREMOVESEAL) {
			listStrings.add("iSREMOVESEAL");
		}
		if(!modify_iSUNLOCKYCHDY)
			listStrings.add("iSUNLOCKYCHDY");
		if(!modify_iSINITATATUS)
			listStrings.add("iSINITATATUS");
		if(!modify_cERTMODE)
			listStrings.add("cERTMODE");
		if(!modify_dELYCDY){
			listStrings.add("dELYCDY");
		}
		if(!modify_deltdql){
			listStrings.add("deltdql");
		}
		if(!modify_sPBSJR) {
			listStrings.add("sPBSJR");
		}
		if (modify_iSEXTENDMORTGAGEINFO) {
			listStrings.add("iSEXTENDMORTGAGEINFO");
		}
		if (modify_dEFAULTFJ) {
			listStrings.add("dEFAULTFJ"); 	
		}
		if (modify_dEFAULTDJYY ) {
			listStrings.add("dEFAULTDJYY"); 	
		}
		if (!modify_uSESPBDHTM) {
			listStrings.add("uSESPBDHTM");
		}
		if (!modify_uSEAPPROVALHTM) {
			listStrings.add("uSEAPPROVALHTM");
		}
		if (!modify_uSEFJHTM) {
			listStrings.add("uSEFJHTM");
		}
		if (!modify_uSESQRHTM) {
			listStrings.add("uSESQRHTM");
		}
		if(!modify_cFCONFIG){
			listStrings.add("cFCONFIG");
		}
		if(!modify_iSALLOWADDSQR){
			listStrings.add("iSALLOWADDSQR");
		}
		if(!modify_iSCOPYYCF2CF){
			listStrings.add("iSCOPYYCF2CF");
		}
		return StringHelper.ListToStrings(listStrings);
	}

	
}
