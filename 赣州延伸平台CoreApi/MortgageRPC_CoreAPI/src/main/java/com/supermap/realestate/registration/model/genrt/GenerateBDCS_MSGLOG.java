package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_MSGLOG  implements SuperModel<String>{
	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
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

	private String YWLSH;
	private boolean modify_YWLSH = false;

	public String getYWLSH() {
		return YWLSH;
	}

	public void setYWLSH(String YWLSH) {
		if (this.YWLSH != YWLSH) {
			this.YWLSH = YWLSH;
			modify_YWLSH = true;
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

	private String PROJECT_ID;
	private boolean modify_PROJECT_ID = false;

	public String getPROJECT_ID() {
		return PROJECT_ID;
	}

	public void setPROJECT_ID(String PROJECT_ID) {
		if (this.PROJECT_ID != PROJECT_ID) {
			this.PROJECT_ID = PROJECT_ID;
			modify_PROJECT_ID = true;
		}
	}

	private String XMMC;
	private boolean modify_XMMC = false;

	public String getXMMC() {
		return XMMC;
	}

	public void setXMMC(String XMMC) {
		if (this.XMMC != XMMC) {
			this.XMMC = XMMC;
			modify_XMMC = true;
		}
	}

	private String QLLX;
	private boolean modify_QLLX = false;

	public String getQLLX() {
		return QLLX;
	}

	public void setQLLX(String QLLX) {
		if (this.QLLX != QLLX) {
			this.QLLX = QLLX;
			modify_QLLX = true;
		}
	}

	private String DJLX;
	private boolean modify_DJLX = false;

	public String getDJLX() {
		return DJLX;
	}

	public void setDJLX(String DJLX) {
		if (this.DJLX != DJLX) {
			this.DJLX = DJLX;
			modify_DJLX = true;
		}
	}

	private Date DXFSSJ;
	private boolean modify_DXFSSJ = false;

	public Date getDXFSSJ() {
		return DXFSSJ;
	}

	public void setDXFSSJ(Date DXFSSJ) {
		if (this.DXFSSJ != DXFSSJ) {
			this.DXFSSJ = DXFSSJ;
			modify_DXFSSJ = true;
		}
	}

	private Date YWBJSJ;
	private boolean modify_YWBJSJ = false;

	public Date getYWBJSJ() {
		return YWBJSJ;
	}

	public void setYWBJSJ(Date YWBJSJ) {
		if (this.YWBJSJ != YWBJSJ) {
			this.YWBJSJ = YWBJSJ;
			modify_YWBJSJ = true;
		}
	}

	private String JSDXRMC;
	private boolean modify_JSDXRMC = false;

	public String getJSDXRMC() {
		return JSDXRMC;
	}

	public void setJSDXRMC(String JSDXRMC) {
		if (this.JSDXRMC != JSDXRMC) {
			this.JSDXRMC = JSDXRMC;
			modify_JSDXRMC = true;
		}
	}
	
	private String JSDH ;
	private boolean modify_JSDH = false;

	public String getJSDH() {
		return JSDH;
	}

	public void setJSDH(String JSDH) {
		if (this.JSDH != JSDH&&!org.springframework.util.StringUtils.isEmpty(JSDH)) {
			this.JSDH = JSDH;
			modify_JSDH = true;
		}
	}

	private String FLAG ;
	private boolean modify_FLAG = false;

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String FLAG) {
		if (this.FLAG != FLAG&&!org.springframework.util.StringUtils.isEmpty(FLAG)) {
			this.FLAG = FLAG;
			modify_FLAG = true;
		}
	}
	private String DXNR ;
	private boolean modify_DXNR = false;

	public String getDXNR() {
		return DXNR;
	}

	public void setDXNR(String DXNR) {
		if (this.DXNR != DXNR&&!org.springframework.util.StringUtils.isEmpty(DXNR)) {
			this.DXNR = DXNR;
			modify_DXNR = true;
		}
	}
	
	private String MESSAGE ;
	private boolean modify_MESSAGE = false;

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String MESSAGE) {
		if (this.MESSAGE != MESSAGE&&!org.springframework.util.StringUtils.isEmpty(MESSAGE)) {
			this.MESSAGE = MESSAGE;
			modify_MESSAGE = true;
		}
	}
	
	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_YWLSH = false;
		modify_xMBH = false;
		modify_PROJECT_ID = false;
		modify_XMMC = false;
		modify_QLLX = false;
		modify_DJLX = false;
		modify_DXFSSJ = false;
		modify_YWBJSJ = false;
		modify_JSDXRMC = false;
		modify_JSDH=false;
		modify_FLAG=false;
		modify_DXNR=false;
		modify_MESSAGE=false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_YWLSH)
			listStrings.add("YWLSH");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_PROJECT_ID)
			listStrings.add("PROJECT_ID");
		if (!modify_XMMC)
			listStrings.add("XMMC");
		if (!modify_QLLX)
			listStrings.add("QLLX");
		if (!modify_DJLX)
			listStrings.add("DJLX");
		if (!modify_DXFSSJ)
			listStrings.add("DXFSSJ");
		if (!modify_YWBJSJ)
			listStrings.add("YWBJSJ");
		if (!modify_JSDXRMC)
			listStrings.add("JSDXRMC");
		if (!modify_JSDH)
			listStrings.add("JSDH");
		if (!modify_FLAG)
			listStrings.add("FLAG");
		if (!modify_DXNR)
			listStrings.add("DXNR");
		if (!modify_MESSAGE)
			listStrings.add("MESSAGE");

		return StringHelper.ListToStrings(listStrings);
	}
}
