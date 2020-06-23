package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-20 
//* ----------------------------------------
//* Internal Entity BDCS_BDCCFQKB 
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

public class GenerateBDCS_BDCCFXX implements SuperModel<String> {

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

	private String bSM;
	private boolean modify_bSM = false;

	public String getBSM() {
		return bSM;
	}

	public void setBSM(String bSM) {
		if (this.bSM!= bSM) {
			this.bSM = bSM;
			modify_bSM = true;
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
	
	private Date uPDATETIME;
	private boolean modify_uPDATETIME = false;
	
	public Date getUPDATETIME() {
		return uPDATETIME;
	}
	
	public void setUPDATETIME(Date uPDATETIME) {
		if (this.uPDATETIME != uPDATETIME) {
			this.uPDATETIME = uPDATETIME;
			modify_uPDATETIME = true;
		}
	}

	private String cFWH;
	private boolean modify_cFWH = false;

	public String getCFWH() {
		return cFWH;
	}

	public void setCFWH(String cFWH) {
		if (this.cFWH!= cFWH) {
			this.cFWH = cFWH;
			modify_cFWH = true;
		}
	}
	
	private String jFWH;
	private boolean modify_jFWH = false;

	public String getJFWH() {
		return jFWH;
	}

	public void setJFWH(String jFWH) {
		if (this.jFWH!= jFWH) {
			this.jFWH = jFWH;
			modify_jFWH = true;
		}
	}
	
	private String sDRXM;
	private boolean modify_sDRXM = false;

	public String getSDRXM() {
		return sDRXM;
	}
	
	public void setSDRXM(String sDRXM) {
		if (this.sDRXM!= sDRXM) {
			this.sDRXM = sDRXM;
			modify_sDRXM = true;
		}
	}

	private String sDTIME;
	private boolean modify_sDTIME = false;

	public String getSDTIME() {
		return sDTIME;
	}
	
	public void setSDTIME(String sDTIME) {
		if (this.sDTIME != sDTIME) {
			this.sDTIME = sDTIME;
			modify_sDTIME = true;
		}
	}
	

	private String cFDATE;
	private boolean modify_cFDATE = false;

	public String getCFDATE() {
		return cFDATE;
	}
	
	public void setCFDATE(String cFDATE) {
		if (this.cFDATE != cFDATE) {
			this.cFDATE = cFDATE;
			modify_cFDATE = true;
		}
	}

	private String jFDATE;
	private boolean modify_jFDATE = false;

	public String getJFDATE() {
		return jFDATE;
	}
	
	public void setJFDATE(String jFDATE) {
		if (this.jFDATE != jFDATE) {
			this.jFDATE = jFDATE;
			modify_jFDATE = true;
		}
	}

	private String cJR;
	private boolean modify_cJR = false;

	public String getCJR() {
		return cJR;
	}
	
	public void setCJR(String cJR) {
		if (this.cJR!= cJR) {
			this.cJR = cJR;
			modify_cJR = true;
		}
	}

	private String bCFR;
	private boolean modify_bCFR = false;

	public String getBCFR() {
		return bCFR;
	}
	
	public void setBCFR(String bCFR) {
		if (this.bCFR!= bCFR) {
			this.bCFR = bCFR;
			modify_bCFR = true;
		}
	}
	
	private String bCFRZJHM;
	private boolean modify_bCFRZJHM = false;

	public String getBCFRZJHM() {
		return bCFRZJHM;
	}
	
	public void setBCFRZJHM(String bCFRZJHM) {
		if (this.bCFRZJHM!= bCFRZJHM) {
			this.bCFRZJHM = bCFRZJHM;
			modify_bCFRZJHM = true;
		}
	}
	
	private String mSRMC;
	private boolean modify_mSRMC = false;

	public String getMSRMC() {
		return mSRMC;
	}
	
	public void setMSRMC(String mSRMC) {
		if (this.mSRMC!= mSRMC) {
			this.mSRMC = mSRMC;
			modify_mSRMC = true;
		}
	}
	
	private String mSRZJH;
	private boolean modify_mSRZJH = false;

	public String getMSRZJH() {
		return mSRZJH;
	}
	
	public void setMSRZJH(String mSRZJH) {
		if (this.mSRZJH!= mSRZJH) {
			this.mSRZJH = mSRZJH;
			modify_mSRZJH = true;
		}
	}

	private String cFBDW;
	private boolean modify_cFBDW = false;

	public String getCFBDW() {
		return cFBDW;
	}
	
	public void setCFBDW(String cFBDW) {
		if (this.cFBDW!= cFBDW) {
			this.cFBDW = cFBDW;
			modify_cFBDW = true;
		}
	}

	private String tDZH;
	private boolean modify_tDZH = false;

	public String getTDZH() {
		return tDZH;
	}
	
	public void setTDZH(String tDZH) {
		if (this.tDZH!= tDZH) {
			this.tDZH = tDZH;
			modify_tDZH = true;
		}
	}

	private String tDMJ;
	private boolean modify_tDMJ = false;

	public String getTDMJ() {
		return tDMJ;
	}
	
	public void setTDMJ(String tDMJ) {
		if (this.tDMJ!= tDMJ) {
			this.tDMJ = tDMJ;
			modify_tDMJ = true;
		}
	}

	private String fCZH;
	private boolean modify_fCZH = false;

	public String getFCZH() {
		return fCZH;
	}
	
	public void setFCZH(String fCZH) {
		if (this.fCZH!= fCZH) {
			this.fCZH = fCZH;
			modify_fCZH = true;
		}
	}

	private String fWMJ;
	private boolean modify_fWMJ = false;

	public String getFWMJ() {
		return fWMJ;
	}
	
	public void setFWMJ(String fWMJ) {
		if (this.fWMJ!= fWMJ) {
			this.fWMJ = fWMJ;
			modify_fWMJ = true;
		}
	}

	private String cFJG;
	private boolean modify_cFJG = false;

	public String getCFJG() {
		return cFJG;
	}
	
	public void setCFJG(String cFJG) {
		if (this.cFJG!= cFJG) {
			this.cFJG = cFJG;
			modify_cFJG = true;
		}
	}

	private String bZ;
	private boolean modify_bZ = false;

	public String getBZ() {
		return bZ;
	}
	
	public void setBZ(String bZ) {
		if (this.bZ!= bZ) {
			this.bZ = bZ;
			modify_bZ = true;
		}
	}
	
	/*private String zL;
	private boolean modify_zL = false;

	public String getZL() {
		return zL;
	}
	
	public void setZL(String zL) {
		if (this.zL!= zL) {
			this.zL = zL;
			modify_zL = true;
		}
	}*/

	private String wTFYCFDW;
	private boolean modify_wTFYCFDW = false;

	public String getWTFYCFDW() {
		return wTFYCFDW;
	}
	
	public void setWTFYCFDW(String wTFYCFDW) {
		if (this.wTFYCFDW!= wTFYCFDW) {
			this.wTFYCFDW = wTFYCFDW;
			modify_wTFYCFDW = true;
		}
	}

	private String cFQX;
	private boolean modify_cFQX = false;

	public String getCFQX() {
		return cFQX;
	}
	
	public void setCFQX(String cFQX) {
		if (this.cFQX!= cFQX) {
			this.cFQX = cFQX;
			modify_cFQX = true;
		}
	}

	private String cDQK;
	private boolean modify_cDQK = false;

	public String getCDQK() {
		return cDQK;
	}
	
	public void setCDQK(String cDQK) {
		if (this.cDQK!= cDQK) {
			this.cDQK = cDQK;
			modify_cDQK = true;
		}
	}

	private String tSCLQK;
	private boolean modify_tSCLQK = false;

	public String getTSCLQK() {
		return tSCLQK;
	}
	
	public void setTSCLQK(String tSCLQK) {
		if (this.tSCLQK!= tSCLQK) {
			this.tSCLQK = tSCLQK;
			modify_tSCLQK = true;
		}
	}

	private String gXR;
	private boolean modify_gXR = false;

	public String getGXR() {
		return gXR;
	}
	
	public void setGXR(String gXR) {
		if (this.gXR!= gXR) {
			this.gXR = gXR;
			modify_gXR = true;
		}
	}
	
	private String sFJF;
	private boolean modify_sFJF = false;
	
	public String getSFJF() {
		return sFJF;
	}
	public void setSFJF(String sFJF) {
		if (this.sFJF!= sFJF) {
			this.sFJF = sFJF;
			modify_sFJF = true;
		}
	}
	
	private String bH;
	private boolean modify_bH = false;
	
	public String getBH() {
		return bH;
	}
	public void setBH(String bH) {
		if (this.bH!= bH) {
			this.bH = bH;
			modify_bH = true;
		}
	}
	
	private String fILEPATH;
	private boolean modify_fILEPATH = false;
	public String getFILEPATH() {
		return fILEPATH;
	}
	public void setFILEPATH(String fILEPATH) {
		if (this.fILEPATH!= fILEPATH) {
			this.fILEPATH = fILEPATH;
			modify_fILEPATH = true;
		}
	}
	
	private String cFFW;
	private boolean modify_cFFW = false;
	public String getCFFW() {
		return cFFW;
	}
	public void setCFFW(String cFFW) {
		if (this.cFFW!= cFFW) {
			this.cFFW = cFFW;
			modify_cFFW = true;
		}
	}
	
	private String qSSJ;
	private boolean modify_qSSJ = false;
	public String getQSSJ() {
		return qSSJ;
	}
	public void setQSSJ(String qSSJ) {
		if (this.qSSJ!= qSSJ) {
			this.qSSJ = qSSJ;
			modify_qSSJ = true;
		}
	}
	
	private String zZSJ;
	private boolean modify_zZSJ = false;
	public String getZZSJ() {
		return zZSJ;
	}
	public void setZZSJ(String zZSJ) {
		if (this.zZSJ!= zZSJ) {
			this.zZSJ = zZSJ;
			modify_zZSJ = true;
		}
	}
	
	private String zL;
	private boolean modify_zL = false;
	public String getZL() {
		return zL;
	}
	public void setZL(String zL) {
		if (this.zL!= zL) {
			this.zL = zL;
			modify_zL = true;
		}
	}
	
	private String yG;
	private boolean modify_yG = false;
	public String getYG() {
		return yG;
	}
	public void setYG(String yG) {
		if (this.yG!= yG) {
			this.yG = yG;
			modify_yG = true;
		}
	}
	
	private String bG;
	private boolean modify_bG = false;
	public String getBG() {
		return bG;
	}
	public void setBG(String bG) {
		if (this.bG!= bG) {
			this.bG = bG;
			modify_bG = true;
		}
	}
	private String bZXR;
	private boolean modify_bZXR = false;
	public String getBZXR() {
		return bZXR;
	}
	public void setBZXR(String bZXR) {
		if (this.bZXR!= bZXR) {
			this.bZXR = bZXR;
			modify_bZXR = true;
		}
	}
	private String bZXRZJH;
	private boolean modify_bZXRZJH = false;
	public String getBZXRZJH() {
		return bZXRZJH;
	}
	public void setBZXRZJH(String BZXRZJH) {
		if (this.bZXRZJH!= BZXRZJH) {
			this.bZXRZJH = BZXRZJH;
			modify_bZXRZJH = true;
		}
	}
	
	private String lRTIME;
	private boolean modify_lRTIME = false;

	public String getLRTIME() {
		return lRTIME;
	}
	
	public void setLRTIME(String lRTIME) {
		if (this.lRTIME != lRTIME) {
			this.lRTIME = lRTIME;
			modify_lRTIME = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_cREATETIME = false;
		modify_bSM  = false;
		modify_gXR  = false;
		modify_tSCLQK  = false;
		modify_cDQK  = false;
		modify_cFQX  = false;
		modify_wTFYCFDW  = false;
		modify_bZ  = false;
		modify_cFJG  = false;
		modify_fWMJ  = false;
		modify_fCZH  = false;
		modify_tDMJ  = false;
		modify_tDZH  = false;
		modify_cFBDW  = false;
		modify_bCFR  = false;
		modify_jFDATE  = false;
		modify_cJR  = false;
		modify_cFDATE  = false;
		modify_sDTIME  = false;
		modify_sDRXM  = false;
		modify_cFWH  = false;
		modify_uPDATETIME  = false;
		//modify_zL  = false;
		modify_sFJF = false;
		modify_jFWH = false;
		modify_mSRMC = false;
		modify_mSRZJH = false;
		modify_bCFRZJHM = false;
		modify_bH = false;
		modify_fILEPATH = false;
		modify_cFFW = false;
		modify_qSSJ = false;
		modify_zZSJ = false;
		modify_zL = false;
		modify_yG = false;
		modify_bG = false;
		modify_bZXR = false;
		modify_bZXRZJH = false;
		modify_lRTIME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_bSM)
			listStrings.add("bSM");
		if (!modify_gXR)
			listStrings.add("gXR");
		if (!modify_cDQK)
			listStrings.add("cDQK");
		if (!modify_cFQX)
			listStrings.add("cFQX");
		if (!modify_wTFYCFDW)
			listStrings.add("wTFYCFDW");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_cFJG)
			listStrings.add("cFJG");
		if (!modify_fWMJ)
			listStrings.add("fWMJ");
		if (!modify_fCZH)
			listStrings.add("fCZH");
		if (!modify_tDMJ)
			listStrings.add("tDMJ");
		if (!modify_tDZH)
			listStrings.add("tDZH");
		if (!modify_cFBDW)
			listStrings.add("cFBDW");
		if (!modify_bCFR)
			listStrings.add("bCFR");
		if (!modify_jFDATE)
			listStrings.add("jFDATE");
		if (!modify_cJR)
			listStrings.add("cJR");
		if (!modify_cFDATE)
			listStrings.add("cFDATE");
		if (!modify_sDTIME)
			listStrings.add("sDTIME");
		if (!modify_sDRXM)
			listStrings.add("sDRXM");
		if (!modify_cFWH)
			listStrings.add("cFWH");
		if (!modify_jFWH)
			listStrings.add("jFWH");
		if (!modify_uPDATETIME)
			listStrings.add("uPDATETIME");
/*		if (!modify_zL)
			listStrings.add("zl");
*/		if (!modify_tSCLQK)
			listStrings.add("tSCLQK");
		if(!modify_sFJF)
			listStrings.add("sFJF");
		if(!modify_mSRMC)
			listStrings.add("mSRMC");
		if(!modify_mSRZJH)
			listStrings.add("mSRZJH");
		if(!modify_bCFRZJHM)
			listStrings.add("bCFRZJHM");
		if(!modify_bH)
			listStrings.add("bH");
		if (!modify_fILEPATH) 
			listStrings.add("fILEPATH");
		if (!modify_cFFW )
			listStrings.add("cFFW");
		if (!modify_qSSJ )
			listStrings.add("qSSJ");
		if (!modify_zZSJ )
			listStrings.add("zZSJ"); 
		if (!modify_zL )
			listStrings.add("zL"); 
		if (!modify_yG )
			listStrings.add("yG"); 
		if (!modify_bG )
			listStrings.add("bG"); 
		if (!modify_bZXR )
			listStrings.add("bZXR"); 
		if (!modify_bZXRZJH )
			listStrings.add("bZXRZJH"); 
		if (!modify_lRTIME )
			listStrings.add("lRTIME"); 
		return StringHelper.ListToStrings(listStrings);
	}
}
