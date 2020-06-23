package com.supermap.realestate.registration.model.genrt;

/**
 * 数据上报结果表
 * @author diaoliwei
 * @date 2016-1-17
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_SJSB implements SuperModel<String> {

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

	private String bDCDYH;
	private boolean modify_bDCDYH = false;

	public String getBDCDYH() {
		return bDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		if (this.bDCDYH != bDCDYH) {
			this.bDCDYH = bDCDYH;
			modify_bDCDYH = true;
		}
	}

	private String oPERATEUSER;
	private boolean modify_oPERATEUSER = false;

	public String getOPERATEUSER() {
		return oPERATEUSER;
	}

	public void setOPERATEUSER(String oPERATEUSER) {
		if (this.oPERATEUSER != oPERATEUSER) {
			this.oPERATEUSER = oPERATEUSER;
			modify_oPERATEUSER = true;
		}
	}

	private String bWMC;
	private boolean modify_bWMC = false;

	public String getBWMC() {
		return bWMC;
	}

	public void setBWMC(String bWMC) {
		if (this.bWMC != bWMC) {
			this.bWMC = bWMC;
			modify_bWMC = true;
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

	private Date oPERATETIME;
	private boolean modify_oPERATETIME = false;

	public Date getOPERATETIME() {
		return oPERATETIME;
	}

	public void setOPERATETIME(Date oPERATETIME) {
		if (this.oPERATETIME != oPERATETIME) {
			this.oPERATETIME = oPERATETIME;
			modify_oPERATETIME = true;
		}
	}
	
	private String lOGINNAME;
	private boolean modify_lOGINNAME = false;
	
	public String getLOGINNAME(){
		return lOGINNAME;
	}

	public void setLOGINNAME(String lOGINNAME){
		if (this.lOGINNAME != lOGINNAME) {
			this.lOGINNAME = lOGINNAME;
			modify_lOGINNAME = true;
		}
	}
	
	private String rECCODE;
	private boolean modify_rECCODE = false;
	
	public String getRECCODE(){
		return rECCODE;
	}
	public void setRECCODE(String rECCODE){
		if (this.rECCODE != rECCODE) {
			this.rECCODE = rECCODE;
			modify_rECCODE = true;
		}
	}
	
	private String pROINSTID;
	private boolean modify_pROINSTID = false;
	
	public String getPROINSTID(){
		return pROINSTID;
	}
	public void setPROINSTID(String pROINSTID){
		if (this.pROINSTID != pROINSTID) {
			this.pROINSTID = pROINSTID;
			modify_pROINSTID = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYH = false;
		modify_oPERATEUSER = false;
		modify_bWMC = false;
		modify_sUCCESSFLAG = false;
		modify_rESPONSEINFO = false;
		modify_pATH = false;
		modify_oPERATETIME = false;
		modify_lOGINNAME = false;
		modify_rECCODE = false;
		modify_pROINSTID=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_oPERATEUSER)
			listStrings.add("oPERATEUSER");
		if (!modify_bWMC)
			listStrings.add("bWMC");
		if (!modify_sUCCESSFLAG)
			listStrings.add("sUCCESSFLAG");
		if (!modify_rESPONSEINFO)
			listStrings.add("rESPONSEINFO");
		if (!modify_pATH)
			listStrings.add("pATH");
		if (!modify_oPERATETIME)
			listStrings.add("oPERATETIME");
		if (!modify_lOGINNAME)
			listStrings.add("lOGINNAME");
		if (!modify_rECCODE)
			listStrings.add("rECCODE");
		if(!modify_pROINSTID){
			listStrings.add("pROINSTID");
		}

		return StringHelper.ListToStrings(listStrings);
	}
}
