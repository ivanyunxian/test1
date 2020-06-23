package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_djsh 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_DJSH implements SuperModel<Integer> {

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

	private String jDMC;
	private boolean modify_jDMC = false;

	public String getJDMC() {
		return jDMC;
	}

	public void setJDMC(String jDMC) {
		if (this.jDMC != jDMC) {
			this.jDMC = jDMC;
			modify_jDMC = true;
		}
	}

	private Integer sXH;
	private boolean modify_sXH = false;

	public Integer getSXH() {
		return sXH;
	}

	public void setSXH(Integer sXH) {
		if (this.sXH != sXH) {
			this.sXH = sXH;
			modify_sXH = true;
		}
	}

	private String sHRYXM;
	private boolean modify_sHRYXM = false;

	public String getSHRYXM() {
		return sHRYXM;
	}

	public void setSHRYXM(String sHRYXM) {
		if (this.sHRYXM != sHRYXM) {
			this.sHRYXM = sHRYXM;
			modify_sHRYXM = true;
		}
	}

	private Date sHKSSJ;
	private boolean modify_sHKSSJ = false;

	public Date getSHKSSJ() {
		return sHKSSJ;
	}

	public void setSHKSSJ(Date sHKSSJ) {
		if (this.sHKSSJ != sHKSSJ) {
			this.sHKSSJ = sHKSSJ;
			modify_sHKSSJ = true;
		}
	}

	private Date sHJSSJ;
	private boolean modify_sHJSSJ = false;

	public Date getSHJSSJ() {
		return sHJSSJ;
	}

	public void setSHJSSJ(Date sHJSSJ) {
		if (this.sHJSSJ != sHJSSJ) {
			this.sHJSSJ = sHJSSJ;
			modify_sHJSSJ = true;
		}
	}

	private String sHYJ;
	private boolean modify_sHYJ = false;

	public String getSHYJ() {
		return sHYJ;
	}

	public void setSHYJ(String sHYJ) {
		if (this.sHYJ != sHYJ) {
			this.sHYJ = sHYJ;
			modify_sHYJ = true;
		}
	}

	private String cZJG;
	private boolean modify_cZJG = false;

	public String getCZJG() {
		return cZJG;
	}

	public void setCZJG(String cZJG) {
		if (this.cZJG != cZJG) {
			this.cZJG = cZJG;
			modify_cZJG = true;
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
		modify_jDMC = false;
		modify_sXH = false;
		modify_sHRYXM = false;
		modify_sHKSSJ = false;
		modify_sHJSSJ = false;
		modify_sHYJ = false;
		modify_cZJG = false;
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
		if (!modify_jDMC)
			listStrings.add("jDMC");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_sHRYXM)
			listStrings.add("sHRYXM");
		if (!modify_sHKSSJ)
			listStrings.add("sHKSSJ");
		if (!modify_sHJSSJ)
			listStrings.add("sHJSSJ");
		if (!modify_sHYJ)
			listStrings.add("sHYJ");
		if (!modify_cZJG)
			listStrings.add("cZJG");
		if (!modify_createTime)
			listStrings.add("createTime");
		if (!modify_modifyTime)
			listStrings.add("modifyTime");

		return StringHelper.ListToStrings(listStrings);
	}
}
