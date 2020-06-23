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

public class GenerateLOG_SENDMSG implements SuperModel<String>{

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

	private String lXDH;
	private boolean modify_lXDH = false;

	public String getLXDH() {
		return lXDH;
	}

	public void setLXDH(String lXDH) {
		if (this.lXDH != lXDH) {
			this.lXDH = lXDH;
			modify_lXDH = true;
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
	
	private String dLRLXDH;
	private boolean modify_dLRLXDH = false;

	public String getDLRLXDH() {
		return dLRLXDH;
	}
	public void setDLRLXDH(String dLRLXDH) {
		if (this.dLRLXDH != dLRLXDH) {
			this.dLRLXDH = dLRLXDH;
			modify_dLRLXDH = true;
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
	
	private String xMMC;
	private boolean modify_xMMC = false;

	public String getXMMC() {
		return xMMC;
	}

	public void setXMMC(String xMMC) {
		if (this.xMMC != xMMC) {
			this.xMMC = xMMC;
			modify_xMMC = true;
		}
	}
	
	private String zSBH;
	private boolean modify_zSBH = false;

	public String getZSBH() {
		return zSBH;
	}

	public void setZSBH(String zSBH) {
		if (this.zSBH != zSBH) {
			this.zSBH = zSBH;
			modify_zSBH = true;
		}
	}
	
	private String bDCQZH;
	private boolean modify_bDCQZH = false;

	public String getBDCQZH() {
		return bDCQZH;
	}

	public void setBDCQZH(String bDCQZH) {
		if (this.bDCQZH != bDCQZH) {
			this.bDCQZH = bDCQZH;
			modify_bDCQZH = true;
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
	
	private Date sZSJ;
	private boolean modify_sZSJ = false;

	public Date getSZSJ() {
		return sZSJ;
	}

	public void setSZSJ(Date sZSJ) {
		if (this.sZSJ != sZSJ) {
			this.sZSJ = sZSJ;
			modify_sZSJ = true;
		}
	}
	
	private Date sENDTIME;
	private boolean modify_sENDTIME = false;

	public Date getSENDTIME() {
		return sENDTIME;
	}

	public void setSENDTIME(Date sENDTIME) {
		if (this.sENDTIME != sENDTIME) {
			this.sENDTIME = sENDTIME;
			modify_sENDTIME = true;
		}
	}
	
	private String sENDUSER;
	private boolean modify_sENDUSER = false;

	public String getSENDUSER() {
		return sENDUSER;
	}

	public void setSENDUSER(String sENDUSER) {
		if (this.sENDUSER != sENDUSER) {
			this.sENDUSER = sENDUSER;
			modify_sENDUSER = true;
		}
	}
	
	private String sENDSQRSTATUS;
	private boolean modify_sENDSQRSTATUS = false;

	public String getSENDSQRSTATUS() {
		return sENDSQRSTATUS;
	}
	public void setSENDSQRSTATUS(String sENDSQRSTATUS) {
		if (this.sENDSQRSTATUS != sENDSQRSTATUS) {
			this.sENDSQRSTATUS = sENDSQRSTATUS;
			modify_sENDSQRSTATUS = true;
		}
	}
	
	private String sENDDLRSTATUS;
	private boolean modify_sENDDLRSTATUS = false;

	public String getSENDDLRSTATUS() {
		return sENDDLRSTATUS;
	}
	public void setSENDDLRSTATUS(String sENDDLRSTATUS) {
		if (this.sENDDLRSTATUS != sENDDLRSTATUS) {
			this.sENDDLRSTATUS = sENDDLRSTATUS;
			modify_sENDDLRSTATUS = true;
		}
	}
	
	private String sENDLX;
	private boolean modify_sENDLX = false;

	public String getSENDLX() {
		return sENDLX;
	}
	public void setSENDLX(String sENDLX) {
		if (this.sENDLX != sENDLX) {
			this.sENDLX = sENDLX;
			modify_sENDLX = true;
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
	
	private String sQRID;
	private boolean modify_sQRID = false;

	public String getSQRID() {
		return sQRID;
	}
	public void setSQRID(String sQRID) {
		if (this.sQRID != sQRID) {
			this.sQRID = sQRID;
			modify_sQRID = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_sQRXM = false;
		modify_lXDH = false;
		modify_dLRXM = false;
		modify_dLRLXDH = false;
		modify_yWLSH = false;
		modify_xMMC = false;
		modify_bDCQZH = false;
		modify_dJSJ = false;
		modify_sZSJ = false;
		modify_sENDTIME = false;
		modify_sENDUSER = false;
		modify_sENDSQRSTATUS = false;
		modify_sENDDLRSTATUS = false;
		modify_sENDLX = false;
		modify_xMBH = false;
		modify_sQRID = false;
		modify_bZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("intId");
		if (!modify_sQRXM)
			listStrings.add("sQRXM");
		if (!modify_lXDH)
			listStrings.add("lXDH");
		if (!modify_dLRXM)
			listStrings.add("dLRXM");
		if (!modify_dLRLXDH)
			listStrings.add("dLRLXDH");
		if (!modify_yWLSH)
			listStrings.add("yWLSH");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_dJSJ)
			listStrings.add("dJSJ");
		if (!modify_sZSJ)
			listStrings.add("sZSJ");
		if (!modify_sENDTIME)
			listStrings.add("sENDTIME");
		if (!modify_sENDUSER)
			listStrings.add("sENDUSER");
		if (!modify_sENDSQRSTATUS)
			listStrings.add("sENDSQRSTATUS");
		if (!modify_sENDDLRSTATUS)
			listStrings.add("sENDDLRSTATUS");
		if (!modify_sENDLX)
			listStrings.add("sENDLX");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_sQRID)
			listStrings.add("sQRID");
		if (!modify_bZ)
			listStrings.add("bZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
