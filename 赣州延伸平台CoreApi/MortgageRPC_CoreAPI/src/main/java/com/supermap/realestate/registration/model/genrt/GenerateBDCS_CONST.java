package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_const 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_CONST implements SuperModel<Integer> {

	private Integer id;
	private boolean modify_id = false;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private Integer cONSTSLSID;
	private boolean modify_cONSTSLSID = false;

	public Integer getCONSTSLSID() {
		return cONSTSLSID;
	}

	public void setCONSTSLSID(Integer cONSTSLSID) {
		if (this.cONSTSLSID != cONSTSLSID) {
			this.cONSTSLSID = cONSTSLSID;
			modify_cONSTSLSID = true;
		}
	}

	private String cONSTVALUE;
	private boolean modify_cONSTVALUE = false;

	public String getCONSTVALUE() {
		return cONSTVALUE;
	}

	public void setCONSTVALUE(String cONSTVALUE) {
		if (this.cONSTVALUE != cONSTVALUE) {
			this.cONSTVALUE = cONSTVALUE;
			modify_cONSTVALUE = true;
		}
	}

	private String cONSTTRANS;
	private boolean modify_cONSTTRANS = false;

	public String getCONSTTRANS() {
		return cONSTTRANS;
	}

	public void setCONSTTRANS(String cONSTTRANS) {
		if (this.cONSTTRANS != cONSTTRANS) {
			this.cONSTTRANS = cONSTTRANS;
			modify_cONSTTRANS = true;
		}
	}
	
	private String gJVALUE;
	private boolean modify_gJVALUE = false;

	public String getGJVALUE() {
		return gJVALUE;
	}

	public void setGJVALUE(String gJVALUE) {
		if (this.gJVALUE != gJVALUE) {
			this.gJVALUE = gJVALUE;
			modify_gJVALUE = true;
		}
	}

	private String gJCONSTTRANS;
	private boolean modify_gJCONSTTRANS = false;

	public String getGJCONSTTRANS() {
		return gJCONSTTRANS;
	}

	public void setGJCONSTTRANS(String gJCONSTTRANS) {
		if (this.gJCONSTTRANS != gJCONSTTRANS) {
			this.gJCONSTTRANS = gJCONSTTRANS;
			modify_gJCONSTTRANS = true;
		}
	}
	
	private String sFSY;
	private boolean modify_sFSY = false;

	public String getSFSY() {
		return sFSY;
	}

	public void setSFSY(String sFSY) {
		if (this.sFSY != sFSY) {
			this.sFSY = sFSY;
			modify_sFSY = true;
		}
	}

	private Integer pARENTNODE;
	private boolean modify_pARENTNODE = false;

	public Integer getPARENTNODE() {
		return pARENTNODE;
	}

	public void setPARENTNODE(Integer pARENTNODE) {
		if (this.pARENTNODE != pARENTNODE) {
			this.pARENTNODE = pARENTNODE;
			modify_pARENTNODE = true;
		}
	}

	private Integer cONSTORDER;
	private boolean modify_cONSTORDER = false;

	public Integer getCONSTORDER() {
		return cONSTORDER;
	}

	public void setCONSTORDER(Integer cONSTORDER) {
		if (this.cONSTORDER != cONSTORDER) {
			this.cONSTORDER = cONSTORDER;
			modify_cONSTORDER = true;
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
	
	private String rEPORTVALUE;
	private boolean modify_rEPORTVALUE = false;

	public String getREPORTVALUE() {
		return rEPORTVALUE;
	}

	public void setREPORTVALUE(String rEPORTVALUE) {
		if (this.rEPORTVALUE != rEPORTVALUE) {
			this.rEPORTVALUE = rEPORTVALUE;
			modify_rEPORTVALUE = true;
		}
	}

	private Date createTime;
	private boolean modify_createTime = false;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		if (this.createTime != createTime) {
			this.createTime = createTime;
			modify_createTime = true;
		}
	}

	private Date modifyTime;
	private boolean modify_modifyTime = false;

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		if (this.modifyTime != modifyTime) {
			this.modifyTime = modifyTime;
			modify_modifyTime = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_cONSTSLSID = false;
		modify_cONSTVALUE = false;
		modify_cONSTTRANS = false;
		modify_pARENTNODE = false;
		modify_cONSTORDER = false;
		modify_bZ = false;
		modify_createTime = false;
		modify_modifyTime = false;
		modify_rEPORTVALUE=false;
		modify_gJVALUE = false;
		modify_gJCONSTTRANS = false;
		modify_sFSY = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cONSTSLSID)
			listStrings.add("cONSTSLSID");
		if (!modify_cONSTVALUE)
			listStrings.add("cONSTVALUE");
		if (!modify_cONSTTRANS)
			listStrings.add("cONSTTRANS");
		if (!modify_pARENTNODE)
			listStrings.add("pARENTNODE");
		if (!modify_cONSTORDER)
			listStrings.add("cONSTORDER");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");
		if(modify_rEPORTVALUE)
			listStrings.add("rEPORTVALUE");
		if(modify_gJVALUE)
			listStrings.add("gJVALUE");
		if(modify_gJCONSTTRANS)
			listStrings.add("gJCONSTTRANS");
		if(modify_sFSY)
			listStrings.add("sFSY");

		return StringHelper.ListToStrings(listStrings);
	}
}
