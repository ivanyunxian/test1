package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_djgd 
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

public class GenerateBDCS_DJGD implements SuperModel<String> {

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

	private String dJDL;
	private boolean modify_dJDL = false;

	public String getDJDL() {
		return dJDL;
	}

	public void setDJDL(String dJDL) {
		if (this.dJDL != dJDL) {
			this.dJDL = dJDL;
			modify_dJDL = true;
		}
	}

	private String dJXL;
	private boolean modify_dJXL = false;

	public String getDJXL() {
		return dJXL;
	}

	public void setDJXL(String dJXL) {
		if (this.dJXL != dJXL) {
			this.dJXL = dJXL;
			modify_dJXL = true;
		}
	}

	private String zL;
	private boolean modify_zL = false;

	public String getZL() {
		return zL;
	}

	public void setZL(String zL) {
		if (this.zL != zL) {
			this.zL = zL;
			modify_zL = true;
		}
	}

	private String qZHM;
	private boolean modify_qZHM = false;

	public String getQZHM() {
		return qZHM;
	}

	public void setQZHM(String qZHM) {
		if (this.qZHM != qZHM) {
			this.qZHM = qZHM;
			modify_qZHM = true;
		}
	}

	private String jZH;
	private boolean modify_jZH = false;

	public String getJZH() {
		return jZH;
	}

	public void setJZH(String jZH) {
		if (this.jZH != jZH) {
			this.jZH = jZH;
			modify_jZH = true;
		}
	}

	private Integer wJJS;
	private boolean modify_wJJS = false;

	public Integer getWJJS() {
		return wJJS;
	}

	public void setWJJS(Integer wJJS) {
		if (this.wJJS != wJJS) {
			this.wJJS = wJJS;
			modify_wJJS = true;
		}
	}

	private Integer zYS;
	private boolean modify_zYS = false;

	public Integer getZYS() {
		return zYS;
	}

	public void setZYS(Integer zYS) {
		if (this.zYS != zYS) {
			this.zYS = zYS;
			modify_zYS = true;
		}
	}

	private String gDZR;
	private boolean modify_gDZR = false;

	public String getGDZR() {
		return gDZR;
	}

	public void setGDZR(String gDZR) {
		if (this.gDZR != gDZR) {
			this.gDZR = gDZR;
			modify_gDZR = true;
		}
	}

	private Date gDSJ;
	private boolean modify_gDSJ = false;

	public Date getGDSJ() {
		return gDSJ;
	}

	public void setGDSJ(Date gDSJ) {
		if (this.gDSJ != gDSJ) {
			this.gDSJ = gDSJ;
			modify_gDSJ = true;
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
		modify_dJDL = false;
		modify_dJXL = false;
		modify_zL = false;
		modify_qZHM = false;
		modify_jZH = false;
		modify_wJJS = false;
		modify_zYS = false;
		modify_gDZR = false;
		modify_gDSJ = false;
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
		if (!modify_dJDL)
			listStrings.add("dJDL");
		if (!modify_dJXL)
			listStrings.add("dJXL");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_qZHM)
			listStrings.add("qZHM");
		if (!modify_jZH)
			listStrings.add("jZH");
		if (!modify_wJJS)
			listStrings.add("wJJS");
		if (!modify_zYS)
			listStrings.add("zYS");
		if (!modify_gDZR)
			listStrings.add("gDZR");
		if (!modify_gDSJ)
			listStrings.add("gDSJ");
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
