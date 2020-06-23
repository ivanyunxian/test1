package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-10 
//* ----------------------------------------
//* Internal Entity bdcs_gzxs 
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

public class GenerateBDCS_GZXS implements SuperModel<String> {

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

	private Double qS;
	private boolean modify_qS = false;

	public Double getQS() {
		return qS;
	}

	public void setQS(Double qS) {
		if (this.qS != qS) {
			this.qS = qS;
			modify_qS = true;
		}
	}

	private Double yYS;
	private boolean modify_yYS = false;

	public Double getYYS() {
		return yYS;
	}

	public void setYYS(Double yYS) {
		if (this.yYS != yYS) {
			this.yYS = yYS;
			modify_yYS = true;
		}
	}

	private Double gRSDS;
	private boolean modify_gRSDS = false;

	public Double getGRSDS() {
		return gRSDS;
	}

	public void setGRSDS(Double gRSDS) {
		if (this.gRSDS != gRSDS) {
			this.gRSDS = gRSDS;
			modify_gRSDS = true;
		}
	}

	private Double yHS;
	private boolean modify_yHS = false;

	public Double getYHS() {
		return yHS;
	}

	public void setYHS(Double yHS) {
		if (this.yHS != yHS) {
			this.yHS = yHS;
			modify_yHS = true;
		}
	}

	private Double tDZZS;
	private boolean modify_tDZZS = false;

	public Double getTDZZS() {
		return tDZZS;
	}

	public void setTDZZS(Double tDZZS) {
		if (this.tDZZS != tDZZS) {
			this.tDZZS = tDZZS;
			modify_tDZZS = true;
		}
	}

	private Double qT1;
	private boolean modify_qT1 = false;

	public Double getQT1() {
		return qT1;
	}

	public void setQT1(Double qT1) {
		if (this.qT1 != qT1) {
			this.qT1 = qT1;
			modify_qT1 = true;
		}
	}

	private Double qT2;
	private boolean modify_qT2 = false;

	public Double getQT2() {
		return qT2;
	}

	public void setQT2(Double qT2) {
		if (this.qT2 != qT2) {
			this.qT2 = qT2;
			modify_qT2 = true;
		}
	}

	private Double qT3;
	private boolean modify_qT3 = false;

	public Double getQT3() {
		return qT3;
	}

	public void setQT3(Double qT3) {
		if (this.qT3 != qT3) {
			this.qT3 = qT3;
			modify_qT3 = true;
		}
	}

	private Double qT4;
	private boolean modify_qT4 = false;

	public Double getQT4() {
		return qT4;
	}

	public void setQT4(Double qT4) {
		if (this.qT4 != qT4) {
			this.qT4 = qT4;
			modify_qT4 = true;
		}
	}

	private Double qT5;
	private boolean modify_qT5 = false;

	public Double getQT5() {
		return qT5;
	}

	public void setQT5(Double qT5) {
		if (this.qT5 != qT5) {
			this.qT5 = qT5;
			modify_qT5 = true;
		}
	}
	private String fWBM;
	private boolean modify_fWBM = false;
	public String getFWBM() {
		return fWBM;
	}

	public void setFWBM(String fWBM) {
		if (this.fWBM != fWBM) {
			this.fWBM = fWBM;
			modify_fWBM = true;
		}
	}

	private String sZMC;
	private boolean modify_sZMC = false;
	public String getSZMC() {
		return sZMC;
	}

	public void setSZMC(String sZMC) {
		if (this.sZMC != sZMC) {
			this.sZMC = sZMC;
			modify_sZMC = true;
		}
	}
	
	private Double sE;
	private boolean modify_sE = false;
	public Double getSE() {
		return sE;
	}

	public void setSE(Double sE) {
		if (this.sE != sE) {
			this.sE = sE;
			modify_sE = true;
		}
	}
	
	private String sMMC;
	private boolean modify_sMMC = false;
	public String getSMMC() {
		return sMMC;
	}

	public void setSMMC(String sMMC) {
		if (this.sMMC != sMMC) {
			this.sMMC = sMMC;
			modify_sMMC = true;
		}
	}
	
	
	private String hTBH;
	private boolean modify_hTBH = false;
	public String getHTBH() {
		return hTBH;
	}

	public void setHTBH(String hTBH) {
		if (this.hTBH != hTBH) {
			this.hTBH = hTBH;
			modify_hTBH= true;
		}
	}
	
	

	private String nSRMC;
	private boolean modify_nSRMC = false;
	public String getNSRMC() {
		return nSRMC;
	}

	public void setNSRMC(String nSRMC) {
		if (this.nSRMC != nSRMC) {
			this.nSRMC = nSRMC;
			modify_nSRMC= true;
		}
	}
	
	private Double sL;
	private boolean modify_sL = false;
	public Double getSL() {
		return sL;
	}

	public void setSL(Double sL) {
		if (this.sL != sL) {
			this.sL = sL;
			modify_sL= true;
		}
	}
	
	
	private String zJH;
	private boolean modify_zJH = false;
	public String getZJH() {
		return zJH;
	}

	public void setZJH(String zJH) {
		if (this.zJH != zJH) {
			this.zJH = zJH;
			modify_zJH= true;
		}
	}
	
	private Date jSRQ;
	private boolean modify_jSRQ = false;
	public Date getJSRQ() {
		return jSRQ;
	}

	public void setJSRQ(Date jSRQ) {
		if (this.jSRQ != jSRQ) {
			this.jSRQ = jSRQ;
			modify_jSRQ= true;
		}
	}
	
	private String jSYJ;
	private boolean modify_jSYJ = false;
	public String getJSYJ() {
		return jSYJ;
	}

	public void setJSYJ(String jSYJ) {
		if (this.jSYJ != jSYJ) {
			this.jSYJ = jSYJ;
			modify_jSYJ= true;
		}
	}
	
	private String sFWS;
	private boolean modify_sFWS = false;
	public String getSFWS() {
		return sFWS;
	}

	public void setSFWS(String sFWS) {
		if (this.sFWS != sFWS) {
			this.sFWS = sFWS;
			modify_sFWS= true;
		}
	}
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_qS = false;
		modify_yYS = false;
		modify_gRSDS = false;
		modify_yHS = false;
		modify_tDZZS = false;
		modify_qT1 = false;
		modify_qT2 = false;
		modify_qT3 = false;
		modify_qT4 = false;
		modify_qT5 = false;
		modify_fWBM = false;
		modify_sZMC = false;
		modify_sE = false;
		modify_sMMC = false;
		modify_hTBH = false;
		modify_nSRMC = false;
		modify_sL =false;
		modify_zJH = false;
		modify_jSRQ = false;
		modify_jSYJ = false;
		modify_sFWS = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_qS)
			listStrings.add("qS");
		if (!modify_yYS)
			listStrings.add("yYS");
		if (!modify_gRSDS)
			listStrings.add("gRSDS");
		if (!modify_yHS)
			listStrings.add("yHS");
		if (!modify_tDZZS)
			listStrings.add("tDZZS");
		if (!modify_qT1)
			listStrings.add("qT1");
		if (!modify_qT2)
			listStrings.add("qT2");
		if (!modify_qT3)
			listStrings.add("qT3");
		if (!modify_qT4)
			listStrings.add("qT4");
		if (!modify_qT5)
			listStrings.add("qT5");
		if(!modify_fWBM)
			listStrings.add("fWBM");
		if(!modify_sZMC)
			listStrings.add("sZMC");
		if(!modify_sE)
			listStrings.add("sE");
		if(!modify_sMMC)
			listStrings.add("sMMC");
		if(!modify_hTBH)
			listStrings.add("hTBH");
		if(!modify_nSRMC)
			listStrings.add("nSRMC");
		if(!modify_sL)
			listStrings.add("sL");
		if(!modify_zJH)
			listStrings.add("zJH");
		if(!modify_jSRQ)
			listStrings.add("jSRQ");
		if(!modify_jSYJ)
			listStrings.add("jSYJ");
		if(!modify_sFWS)
			listStrings.add("SFWS");

		return StringHelper.ListToStrings(listStrings);
	}
}
