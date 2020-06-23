package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_djsz 
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

public class GenerateBDCS_DJSZ implements SuperModel<String> {

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

	private String sZZH;
	private boolean modify_sZZH = false;

	public String getSZZH() {
		return sZZH;
	}

	public void setSZZH(String sZZH) {
		if (this.sZZH != sZZH) {
			this.sZZH = sZZH;
			modify_sZZH = true;
		}
	}

	private String ySXLH;
	private boolean modify_ySXLH = false;

	public String getYSXLH() {
		return ySXLH;
	}

	public void setYSXLH(String ySXLH) {
		if (this.ySXLH != ySXLH) {
			this.ySXLH = ySXLH;
			modify_ySXLH = true;
		}
	}

	private String sZRY;
	private boolean modify_sZRY = false;

	public String getSZRY() {
		return sZRY;
	}

	public void setSZRY(String sZRY) {
		if (this.sZRY != sZRY) {
			this.sZRY = sZRY;
			modify_sZRY = true;
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
		modify_sZMC = false;
		modify_sZZH = false;
		modify_ySXLH = false;
		modify_sZRY = false;
		modify_sZSJ = false;
		modify_bZ = false;
		modify_xMBH = false;
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
		if (!modify_sZMC)
			listStrings.add("sZMC");
		if (!modify_sZZH)
			listStrings.add("sZZH");
		if (!modify_ySXLH)
			listStrings.add("ySXLH");
		if (!modify_sZRY)
			listStrings.add("sZRY");
		if (!modify_sZSJ)
			listStrings.add("sZSJ");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");

		return StringHelper.ListToStrings(listStrings);
	}
}
