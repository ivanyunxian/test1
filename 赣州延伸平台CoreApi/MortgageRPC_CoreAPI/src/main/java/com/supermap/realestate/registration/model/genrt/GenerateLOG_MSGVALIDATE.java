package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateLOG_MSGVALIDATE implements SuperModel<String>{

	private  CommonDao dao;
	private String id;
	private boolean modify_id = false;

	@SuppressWarnings("rawtypes")
	@Override
	public String getId() {
		if (dao == null) {
			dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
		if (!modify_id && id == null){
			id = (String) SuperHelper.GeneratePrimaryKey();
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
	
	private String sQRXM;
	private boolean modify_sQRXM = false;

	public String getSQRXM() {
		return sQRXM;
	}

	public void setSQRXM(String sQRXM) {
		if (this.sQRXM != sQRXM) {
			this.sQRXM = sQRXM;
			modify_sQRXM = true;
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
			modify_zJH = true;
		}
	}

	private String zJLX;
	private boolean modify_zJLX = false;

	public String getZJLX() {
		return zJLX;
	}

	public void setZJLX(String zJLX) {
		if (this.zJLX != zJLX) {
			this.zJLX = zJLX;
			modify_zJLX = true;
		}
	}

	private String sQRLB;
	private boolean modify_sQRLB = false;

	public String getSQRLB() {
		return sQRLB;
	}

	public void setSQRLB(String sQRLB) {
		if (this.sQRLB != sQRLB) {
			this.sQRLB = sQRLB;
			modify_sQRLB = true;
		}
	}
	
	private String sQRLX;
	private boolean modify_sQRLX = false;

	public String getSQRLX() {
		return sQRLX;
	}

	public void setSQRLX(String sQRLX) {
		if (this.sQRLX != sQRLX) {
			this.sQRLX = sQRLX;
			modify_sQRLX = true;
		}
	}
	
	private String fDDBR;
	private boolean modify_fDDBR = false;

	public String getFDDBR() {
		return fDDBR;
	}
	public void setFDDBR(String fDDBR) {
		if (this.fDDBR != fDDBR) {
			this.fDDBR = fDDBR;
			modify_fDDBR = true;
		}
	}
	
	private String fDDBRZJLX;
	private boolean modify_fDDBRZJLX = false;

	public String getFDDBRZJLX() {
		return fDDBRZJLX;
	}
	public void setFDDBRZJLX(String fDDBRZJLX) {
		if (this.fDDBRZJLX != fDDBRZJLX) {
			this.fDDBRZJLX = fDDBRZJLX;
			modify_fDDBRZJLX = true;
		}
	}
	
	private String fDDBRZJHM;
	private boolean modify_fDDBRZJHM = false;

	public String getFDDBRZJHM() {
		return fDDBRZJHM;
	}
	public void setFDDBRZJHM(String fDDBRZJHM) {
		if (this.fDDBRZJHM != fDDBRZJHM) {
			this.fDDBRZJHM = fDDBRZJHM;
			modify_fDDBRZJHM = true;
		}
	}
	
	private String dLRXM;
	private boolean modify_dLRXM = false;

	public String getDLRXM() {
		return dLRXM;
	}
	public void setDLRXM(String dLRXM) {
		if (this.dLRXM != dLRXM) {
			this.dLRXM = dLRXM;
			modify_dLRXM = true;
		}
	}
	
	private String dLRZJLX;
	private boolean modify_dLRZJLX = false;

	public String getDLRZJLX() {
		return dLRZJLX;
	}
	public void setDLRZJLX(String dLRZJLX) {
		if (this.dLRZJLX != dLRZJLX) {
			this.dLRZJLX = dLRZJLX;
			modify_dLRZJLX = true;
		}
	}
	
	private String dLRZJHM;
	private boolean modify_dLRZJHM = false;

	public String getDLRZJHM() {
		return dLRZJHM;
	}
	public void setDLRZJHM(String dLRZJHM) {
		if (this.dLRZJHM != dLRZJHM) {
			this.dLRZJHM = dLRZJHM;
			modify_dLRZJHM = true;
		}
	}

	private Date vLIDATETIME;
	private boolean modify_vLIDATETIME = false;

	public Date getVLIDATETIME() {
		return vLIDATETIME;
	}

	public void setVLIDATETIME(Date vLIDATETIME) {
		if (this.vLIDATETIME != vLIDATETIME) {
			this.vLIDATETIME = vLIDATETIME;
			modify_vLIDATETIME = true;
		}
	}
	
	private String vLIDATEUSER;
	private boolean modify_vLIDATEUSER = false;

	public String getVLIDATEUSER() {
		return vLIDATEUSER;
	}

	public void setVLIDATEUSER(String vLIDATEUSER) {
		if (this.vLIDATEUSER != vLIDATEUSER) {
			this.vLIDATEUSER = vLIDATEUSER;
			modify_vLIDATEUSER = true;
		}
	}
	
	private String yWLSH;
	private boolean modify_yWLSH = false;

	public String getYWLSH() {
		return yWLSH;
	}

	public void setYWLSH(String yWLSH) {
		if (this.yWLSH != yWLSH) {
			this.yWLSH = yWLSH;
			modify_yWLSH = true;
		}
	}

	private String sQRXM_JK;
	private boolean modify_sQRXM_JK = false;

	public String getSQRXM_JK() {
		return sQRXM_JK;
	}

	public void setSQRXM_JK(String sQRXM_JK) {
		if (this.sQRXM_JK != sQRXM_JK) {
			this.sQRXM_JK = sQRXM_JK;
			modify_sQRXM_JK = true;
		}
	}

	private String zJH_JK;
	private boolean modify_zJH_JK = false;

	public String getZJH_JK() {
		return zJH_JK;
	}

	public void setZJH_JK(String zJH_JK) {
		if (this.zJH_JK != zJH_JK) {
			this.zJH_JK = zJH_JK;
			modify_zJH_JK = true;
		}
	}

	private String zJLX_JK;
	private boolean modify_zJLX_JK = false;

	public String getZJLX_JK() {
		return zJLX_JK;
	}

	public void setZJLX_JK(String zJLX_JK) {
		if (this.zJLX_JK != zJLX_JK) {
			this.zJLX_JK = zJLX_JK;
			modify_zJLX_JK = true;
		}
	}

	private String sQRLB_JK;
	private boolean modify_sQRLB_JK = false;

	public String getSQRLB_JK() {
		return sQRLB_JK;
	}

	public void setSQRLB_JK(String sQRLB_JK) {
		if (this.sQRLB_JK != sQRLB_JK) {
			this.sQRLB_JK = sQRLB_JK;
			modify_sQRLB_JK = true;
		}
	}
	
	private String sQRLX_JK;
	private boolean modify_sQRLX_JK = false;

	public String getSQRLX_JK() {
		return sQRLX_JK;
	}

	public void setSQRLX_JK(String sQRLX_JK) {
		if (this.sQRLX_JK != sQRLX_JK) {
			this.sQRLX_JK = sQRLX_JK;
			modify_sQRLX_JK = true;
		}
	}
	
	private String fDDBR_JK;
	private boolean modify_fDDBR_JK = false;

	public String getFDDBR_JK() {
		return fDDBR_JK;
	}
	public void setFDDBR_JK(String fDDBR_JK) {
		if (this.fDDBR_JK != fDDBR_JK) {
			this.fDDBR_JK = fDDBR_JK;
			modify_fDDBR_JK = true;
		}
	}
	
	private String fDDBRZJLX_JK;
	private boolean modify_fDDBRZJLX_JK = false;

	public String getFDDBRZJLX_JK() {
		return fDDBRZJLX_JK;
	}
	public void setFDDBRZJLX_JK(String fDDBRZJLX_JK) {
		if (this.fDDBRZJLX_JK != fDDBRZJLX_JK) {
			this.fDDBRZJLX_JK = fDDBRZJLX_JK;
			modify_fDDBRZJLX_JK = true;
		}
	}
	
	private String fDDBRZJHM_JK;
	private boolean modify_fDDBRZJHM_JK = false;

	public String getFDDBRZJHM_JK() {
		return fDDBRZJHM_JK;
	}
	public void setFDDBRZJHM_JK(String fDDBRZJHM_JK) {
		if (this.fDDBRZJHM_JK != fDDBRZJHM_JK) {
			this.fDDBRZJHM_JK = fDDBRZJHM_JK;
			modify_fDDBRZJHM_JK = true;
		}
	}
	
	private String dLRXM_JK;
	private boolean modify_dLRXM_JK = false;

	public String getDLRXM_JK() {
		return dLRXM_JK;
	}
	public void setDLRXM_JK(String dLRXM_JK) {
		if (this.dLRXM_JK != dLRXM_JK) {
			this.dLRXM_JK = dLRXM_JK;
			modify_dLRXM_JK = true;
		}
	}
	
	private String dLRZJLX_JK;
	private boolean modify_dLRZJLX_JK = false;

	public String getDLRZJLX_JK() {
		return dLRZJLX_JK;
	}
	public void setDLRZJLX_JK(String dLRZJLX_JK) {
		if (this.dLRZJLX_JK != dLRZJLX_JK) {
			this.dLRZJLX_JK = dLRZJLX_JK;
			modify_dLRZJLX_JK = true;
		}
	}
	
	private String dLRZJHM_JK;
	private boolean modify_dLRZJHM_JK = false;

	public String getDLRZJHM_JK() {
		return dLRZJHM_JK;
	}
	public void setDLRZJHM_JK(String dLRZJHM_JK) {
		if (this.dLRZJHM_JK != dLRZJHM_JK) {
			this.dLRZJHM_JK = dLRZJHM_JK;
			modify_dLRZJHM_JK = true;
		}
	}
	
	private String vLIDATESTATUS;
	private boolean modify_vLIDATESTATUS = false;

	public String getVALIDATESTATUS() {
		return vLIDATESTATUS;
	}
	public void setVALIDATESTATUS(String vLIDATESTATUS) {
		if (this.vLIDATESTATUS != vLIDATESTATUS) {
			this.vLIDATESTATUS = vLIDATESTATUS;
			modify_vLIDATESTATUS = true;
		}
	}
	
	private String cODEQLR;
	private boolean modify_cODEQLR = false;

	public String getCODEQLR() {
		return cODEQLR;
	}
	public void setCODEQLR(String cODEQLR) {
		if (this.cODEQLR != cODEQLR) {
			this.cODEQLR = cODEQLR;
			modify_cODEQLR = true;
		}
	}
	
	private String jKYCCODEQLR;
	private boolean modify_jKYCCODEQLR = false;

	public String getJKYCCODEQLR() {
		return jKYCCODEQLR;
	}
	public void setJKYCCODEQLR(String jKYCCODEQLR) {
		if (this.jKYCCODEQLR != jKYCCODEQLR) {
			this.jKYCCODEQLR = jKYCCODEQLR;
			modify_jKYCCODEQLR = true;
		}
	}
	
	private String cODEDLR;
	private boolean modify_cODEDLR = false;

	public String getCODEDLR() {
		return cODEDLR;
	}
	public void setCODEDLR(String cODEDLR) {
		if (this.cODEDLR != cODEDLR) {
			this.cODEDLR = cODEDLR;
			modify_cODEDLR = true;
		}
	}
	
	private String jKYCCODEDLR;
	private boolean modify_jKYCCODEDLR = false;

	public String getJKYCCODEDLR() {
		return jKYCCODEDLR;
	}
	public void setJKYCCODEDLR(String jKYCCODEDLR) {
		if (this.jKYCCODEDLR != jKYCCODEDLR) {
			this.jKYCCODEDLR = jKYCCODEDLR;
			modify_jKYCCODEDLR = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_sQRXM = false;
		modify_zJH = false;
		modify_zJLX = false;
		modify_sQRLB = false;
		modify_sQRLX = false;
		modify_fDDBR = false;
		modify_fDDBRZJLX = false;
		modify_fDDBRZJHM = false;
		modify_dLRXM = false;
		modify_dLRZJLX = false;
		modify_dLRZJHM = false;
		modify_vLIDATETIME = false;
		modify_vLIDATEUSER = false;
		modify_yWLSH = false;
		modify_sQRXM_JK = false;
		modify_zJH_JK = false;
		modify_zJLX_JK = false;
		modify_sQRLB_JK = false;
		modify_sQRLX_JK = false;
		modify_fDDBR_JK = false;
		modify_fDDBRZJLX_JK = false;
		modify_fDDBRZJHM_JK = false;
		modify_dLRXM_JK = false;
		modify_dLRZJLX_JK = false;
		modify_dLRZJHM_JK = false;
		modify_vLIDATESTATUS = false;
		modify_cODEQLR = false;
		modify_jKYCCODEQLR = false;
		modify_cODEDLR = false;
		modify_jKYCCODEDLR = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("intId");
		if (!modify_sQRXM)
			listStrings.add("sQRXM");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_zJLX)
			listStrings.add("zJLX");
		if (!modify_sQRLB)
			listStrings.add("sQRLB");
		if (!modify_sQRLX)
			listStrings.add("sQRLX");
		if (!modify_fDDBR)
			listStrings.add("fDDBR");
		if (!modify_fDDBRZJLX)
			listStrings.add("fDDBRZJLX");
		if (!modify_fDDBRZJHM)
			listStrings.add("fDDBRZJHM");
		if (!modify_dLRXM)
			listStrings.add("dLRXM");
		if (!modify_dLRZJLX)
			listStrings.add("dLRZJLX");
		if (!modify_dLRZJHM)
			listStrings.add("dLRZJHM");
		if (!modify_vLIDATETIME)
			listStrings.add("vLIDATETIME");
		if (!modify_vLIDATEUSER)
			listStrings.add("vLIDATEUSER");
		if (!modify_yWLSH)
			listStrings.add("yWLSH");
		if (!modify_sQRXM_JK)
			listStrings.add("sQRXM_JK");
		if (!modify_zJH_JK)
			listStrings.add("zJH_JK");
		if (!modify_zJLX_JK)
			listStrings.add("zJLX_JK");
		if (!modify_sQRLB_JK)
			listStrings.add("sQRLB_JK");
		if (!modify_sQRLX_JK)
			listStrings.add("sQRLX_JK");
		if (!modify_fDDBR_JK)
			listStrings.add("fDDBR_JK");
		if (!modify_fDDBRZJLX_JK)
			listStrings.add("fDDBRZJLX_JK");
		if (!modify_fDDBRZJHM_JK)
			listStrings.add("fDDBRZJHM_JK");
		if (!modify_dLRXM_JK)
			listStrings.add("dLRXM_JK");
		if (!modify_dLRZJLX_JK)
			listStrings.add("dLRZJLX_JK");
		if (!modify_dLRZJHM_JK)
			listStrings.add("dLRZJHM_JK");
		if (!modify_vLIDATESTATUS)
			listStrings.add("vLIDATESTATUS");
		if (!modify_cODEQLR)
			listStrings.add("cODEQLR");
		if (!modify_jKYCCODEQLR)
			listStrings.add("jKYCCODEQLR");
		if (!modify_cODEDLR)
			listStrings.add("cODEDLR");
		if (!modify_jKYCCODEDLR)
			listStrings.add("jKYCCODEDLR");

		return StringHelper.ListToStrings(listStrings);
	}
}
