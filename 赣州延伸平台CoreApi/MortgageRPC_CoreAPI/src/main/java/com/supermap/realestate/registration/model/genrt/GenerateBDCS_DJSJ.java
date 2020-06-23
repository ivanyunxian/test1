package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_djsj 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_DJSJ implements SuperModel<Integer> {

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

	private String yWH;
	private boolean modify_yWH = false;

	public String getYWH() {
		return yWH;
	}

	public void setYWH(String yWH) {
		if (this.yWH != yWH) {
			this.yWH = yWH;
			modify_yWH = true;
		}
	}

	private String ySDM;
	private boolean modify_ySDM = false;

	public String getYSDM() {
		return ySDM;
	}

	public void setYSDM(String ySDM) {
		if (this.ySDM != ySDM) {
			this.ySDM = ySDM;
			modify_ySDM = true;
		}
	}

	private Date sJSJ;
	private boolean modify_sJSJ = false;

	public Date getSJSJ() {
		return sJSJ;
	}

	public void setSJSJ(Date sJSJ) {
		if (this.sJSJ != sJSJ) {
			this.sJSJ = sJSJ;
			modify_sJSJ = true;
		}
	}

	private String sJLX;
	private boolean modify_sJLX = false;

	public String getSJLX() {
		return sJLX;
	}

	public void setSJLX(String sJLX) {
		if (this.sJLX != sJLX) {
			this.sJLX = sJLX;
			modify_sJLX = true;
		}
	}

	private String sJMC;
	private boolean modify_sJMC = false;

	public String getSJMC() {
		return sJMC;
	}

	public void setSJMC(String sJMC) {
		if (this.sJMC != sJMC) {
			this.sJMC = sJMC;
			modify_sJMC = true;
		}
	}

	private Integer sJSL;
	private boolean modify_sJSL = false;

	public Integer getSJSL() {
		return sJSL;
	}

	public void setSJSL(Integer sJSL) {
		if (this.sJSL != sJSL) {
			this.sJSL = sJSL;
			modify_sJSL = true;
		}
	}

	private Integer sFSYBZ;
	private boolean modify_sFSYBZ = false;

	public Integer getSFSYBZ() {
		return sFSYBZ;
	}

	public void setSFSYBZ(Integer sFSYBZ) {
		if (this.sFSYBZ != sFSYBZ) {
			this.sFSYBZ = sFSYBZ;
			modify_sFSYBZ = true;
		}
	}

	private Integer sFEWSJ;
	private boolean modify_sFEWSJ = false;

	public Integer getSFEWSJ() {
		return sFEWSJ;
	}

	public void setSFEWSJ(Integer sFEWSJ) {
		if (this.sFEWSJ != sFEWSJ) {
			this.sFEWSJ = sFEWSJ;
			modify_sFEWSJ = true;
		}
	}

	private Integer sFBCSJ;
	private boolean modify_sFBCSJ = false;

	public Integer getSFBCSJ() {
		return sFBCSJ;
	}

	public void setSFBCSJ(Integer sFBCSJ) {
		if (this.sFBCSJ != sFBCSJ) {
			this.sFBCSJ = sFBCSJ;
			modify_sFBCSJ = true;
		}
	}

	private Integer yS;
	private boolean modify_yS = false;

	public Integer getYS() {
		return yS;
	}

	public void setYS(Integer yS) {
		if (this.yS != yS) {
			this.yS = yS;
			modify_yS = true;
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
		modify_yWH = false;
		modify_ySDM = false;
		modify_sJSJ = false;
		modify_sJLX = false;
		modify_sJMC = false;
		modify_sJSL = false;
		modify_sFSYBZ = false;
		modify_sFEWSJ = false;
		modify_sFBCSJ = false;
		modify_yS = false;
		modify_bZ = false;
		modify_createTime = false;
		modify_modifyTime = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_sJSJ)
			listStrings.add("sJSJ");
		if (!modify_sJLX)
			listStrings.add("sJLX");
		if (!modify_sJMC)
			listStrings.add("sJMC");
		if (!modify_sJSL)
			listStrings.add("sJSL");
		if (!modify_sFSYBZ)
			listStrings.add("sFSYBZ");
		if (!modify_sFEWSJ)
			listStrings.add("sFEWSJ");
		if (!modify_sFBCSJ)
			listStrings.add("sFBCSJ");
		if (!modify_yS)
			listStrings.add("yS");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");

		return StringHelper.ListToStrings(listStrings);
	}
}
