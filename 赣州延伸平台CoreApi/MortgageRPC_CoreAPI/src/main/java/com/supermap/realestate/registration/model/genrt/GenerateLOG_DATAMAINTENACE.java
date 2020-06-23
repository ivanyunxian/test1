package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateLOG_DATAMAINTENACE implements SuperModel<Integer> {

	private CommonDao dao;
	private Integer id;
	private boolean modify_id = false;

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getId() {
		if (!modify_id && id == null){
			if (dao == null) {
				dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			}
			String fulSql = "SELECT LOG.LOG_DATAMAINTENACE_SEQUENCE.NEXTVAL AS ID FROM DUAL";
			List<Map> list = dao.getDataListByFullSql(fulSql);
			id = StringHelper.ObjtoInt(list.get(0).get("ID"));
			modify_id = true;
		}
		return id;
	}

	@Override
	public void setId(Integer id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}
	
	private Integer lGTYPE;
	private boolean modify_lGTYPE = false;

	public Integer getLGTYPE() {
		return lGTYPE;
	}

	public void setLGTYPE(Integer lGTYPE) {
		if (this.lGTYPE != lGTYPE) {
			this.lGTYPE = lGTYPE;
			modify_lGTYPE = true;
		}
	}

	private String lGTNAME;
	private boolean modify_lGTNAME = false;

	public String getLGTNAME() {
		return lGTNAME;
	}

	public void setLGTNAME(String lGTNAME) {
		if (this.lGTNAME != lGTNAME) {
			this.lGTNAME = lGTNAME;
			modify_lGTNAME = true;
		}
	}

	private Integer mTLX;
	private boolean modify_mTLX = false;

	public Integer getMTLX() {
		return mTLX;
	}

	public void setMTLX(Integer mTLX) {
		if (this.mTLX != mTLX) {
			this.mTLX = mTLX;
			modify_mTLX = true;
		}
	}

	private String mTLXNAME;
	private boolean modify_mTLXNAME = false;

	public String getMTLXNAME() {
		return mTLXNAME;
	}

	public void setMTLXNAME(String mTLXNAME) {
		if (this.mTLXNAME != mTLXNAME) {
			this.mTLXNAME = mTLXNAME;
			modify_mTLXNAME = true;
		}
	}

	private String lGRYMC;
	private boolean modify_lGRYMC = false;

	public String getLGRYMC() {
		return lGRYMC;
	}

	public void setLGRYMC(String lGRYMC) {
		if (this.lGRYMC != lGRYMC) {
			this.lGRYMC = lGRYMC;
			modify_lGRYMC = true;
		}
	}

	private String lGRYID;
	private boolean modify_lGRYID = false;

	public String getLGRYID() {
		return lGRYID;
	}

	public void setLGRYID(String lGRYID) {
		if (this.lGRYID != lGRYID) {
			this.lGRYID = lGRYID;
			modify_lGRYID = true;
		}
	}
	
	private String lGMACHINE;
	private boolean modify_lGMACHINE = false;

	public String getLGMACHINE() {
		return lGMACHINE;
	}

	public void setLGMACHINE(String lGMACHINE) {
		if (this.lGMACHINE != lGMACHINE) {
			this.lGMACHINE = lGMACHINE;
			modify_lGMACHINE = true;
		}
	}
	
	private Double lGMAC;
	private boolean modify_lGMAC = false;

	public Double getLGMAC() {
		return lGMAC;
	}

	public void setLGMAC(Double lGMAC) {
		if (this.lGMAC != lGMAC) {
			this.lGMAC = lGMAC;
			modify_lGMAC = true;
		}
	}

	private Date lGTIME;
	private boolean modify_lGTIME = false;

	public Date getLGTIME() {
		return lGTIME;
	}

	public void setLGTIME(Date lGTIME) {
		if (this.lGTIME != lGTIME) {
			this.lGTIME = lGTIME;
			modify_lGTIME = true;
		}
	}

	private String lGDESCRIPTION;
	private boolean modify_lGDESCRIPTION = false;

	public String getLGDESCRIPTION() {
		return lGDESCRIPTION;
	}

	public void setLGDESCRIPTION(String lGDESCRIPTION) {
		if (this.lGDESCRIPTION != lGDESCRIPTION) {
			this.lGDESCRIPTION = lGDESCRIPTION;
			modify_lGDESCRIPTION = true;
		}
	}

	private String lGCONTENT;
	private boolean modify_lGCONTENT = false;

	public String getLGCONTENT() {
		return lGCONTENT;
	}

	public void setLGCONTENT(String lGCONTENT) {
		if (this.lGCONTENT != lGCONTENT) {
			this.lGCONTENT = lGCONTENT;
			modify_lGCONTENT = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_lGTYPE = false;
		modify_lGTNAME = false;
		modify_mTLX = false;
		modify_mTLXNAME = false;
		modify_lGRYMC = false;
		modify_lGRYID = false;
		modify_lGMACHINE = false;
		modify_lGMAC = false;
		modify_lGTIME = false;
		modify_lGDESCRIPTION = false;
		modify_lGCONTENT = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("intId");
		if (!modify_lGTYPE)
			listStrings.add("lGTYPE");
		if (!modify_lGTNAME)
			listStrings.add("lGTNAME");
		if (!modify_mTLX)
			listStrings.add("mTLX");
		if (!modify_mTLXNAME)
			listStrings.add("mTLXNAME");
		if (!modify_lGRYMC)
			listStrings.add("lGRYMC");
		if (!modify_lGRYID)
			listStrings.add("lGRYID");
		if (!modify_lGMACHINE)
			listStrings.add("lGMACHINE");
		if (!modify_lGMAC)
			listStrings.add("lGMAC");
		if (!modify_lGTIME)
			listStrings.add("lGTIME");
		if (!modify_lGDESCRIPTION)
			listStrings.add("lGDESCRIPTION");
		if (!modify_lGCONTENT)
			listStrings.add("lGCONTENT");

		return StringHelper.ListToStrings(listStrings);
	}
}
