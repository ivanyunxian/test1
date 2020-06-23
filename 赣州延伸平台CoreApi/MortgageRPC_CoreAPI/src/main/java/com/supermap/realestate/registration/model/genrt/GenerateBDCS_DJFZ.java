package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Internal Entity bdcs_djfz 
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

public class GenerateBDCS_DJFZ implements SuperModel<String> {

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

	private String fZRY;
	private boolean modify_fZRY = false;

	public String getFZRY() {
		return fZRY;
	}

	public void setFZRY(String fZRY) {
		if (this.fZRY != fZRY) {
			this.fZRY = fZRY;
			modify_fZRY = true;
		}
	}

	private Date fZSJ;
	private boolean modify_fZSJ = false;

	public Date getFZSJ() {
		return fZSJ;
	}

	public void setFZSJ(Date fZSJ) {
		if (this.fZSJ != fZSJ) {
			this.fZSJ = fZSJ;
			modify_fZSJ = true;
		}
	}

	private String fZMC;
	private boolean modify_fZMC = false;

	public String getFZMC() {
		return fZMC;
	}

	public void setFZMC(String fZMC) {
		if (this.fZMC != fZMC) {
			this.fZMC = fZMC;
			modify_fZMC = true;
		}
	}

	private Integer fZSL;
	private boolean modify_fZSL = false;

	public Integer getFZSL() {
		return fZSL;
	}

	public void setFZSL(Integer fZSL) {
		if (this.fZSL != fZSL) {
			this.fZSL = fZSL;
			modify_fZSL = true;
		}
	}

	private String hFZSH;
	private boolean modify_hFZSH = false;

	public String getHFZSH() {
		return hFZSH;
	}

	public void setHFZSH(String hFZSH) {
		if (this.hFZSH != hFZSH) {
			this.hFZSH = hFZSH;
			modify_hFZSH = true;
		}
	}

	private String lZRXM;
	private boolean modify_lZRXM = false;

	public String getLZRXM() {
		return lZRXM;
	}

	public void setLZRXM(String lZRXM) {
		if (this.lZRXM != lZRXM) {
			this.lZRXM = lZRXM;
			modify_lZRXM = true;
		}
	}

	private String lZRZJLB;
	private boolean modify_lZRZJLB = false;

	public String getLZRZJLB() {
		return lZRZJLB;
	}

	public void setLZRZJLB(String lZRZJLB) {
		if (this.lZRZJLB != lZRZJLB) {
			this.lZRZJLB = lZRZJLB;
			modify_lZRZJLB = true;
		}
	}

	private String lZRZJLBMC;
	private boolean modify_lZRZJLBMC = false;

	public String getLZRZJLBMC() {
		return lZRZJLBMC;
	}

	public void setLZRZJLBMC(String lZRZJLBMC) {
		if (this.lZRZJLBMC != lZRZJLBMC) {
			this.lZRZJLBMC = lZRZJLBMC;
			modify_lZRZJLBMC = true;
		}
	}

	private String lZRZJHM;
	private boolean modify_lZRZJHM = false;

	public String getLZRZJHM() {
		return lZRZJHM;
	}

	public void setLZRZJHM(String lZRZJHM) {
		if (this.lZRZJHM != lZRZJHM) {
			this.lZRZJHM = lZRZJHM;
			modify_lZRZJHM = true;
		}
	}

	private String lZRDH;
	private boolean modify_lZRDH = false;

	public String getLZRDH() {
		return lZRDH;
	}

	public void setLZRDH(String lZRDH) {
		if (this.lZRDH != lZRDH) {
			this.lZRDH = lZRDH;
			modify_lZRDH = true;
		}
	}

	private String lZRDZ;
	private boolean modify_lZRDZ = false;

	public String getLZRDZ() {
		return lZRDZ;
	}

	public void setLZRDZ(String lZRDZ) {
		if (this.lZRDZ != lZRDZ) {
			this.lZRDZ = lZRDZ;
			modify_lZRDZ = true;
		}
	}

	private String lZRYB;
	private boolean modify_lZRYB = false;

	public String getLZRYB() {
		return lZRYB;
	}

	public void setLZRYB(String lZRYB) {
		if (this.lZRYB != lZRYB) {
			this.lZRYB = lZRYB;
			modify_lZRYB = true;
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
		modify_fZRY = false;
		modify_fZSJ = false;
		modify_fZMC = false;
		modify_fZSL = false;
		modify_hFZSH = false;
		modify_lZRXM = false;
		modify_lZRZJLB = false;
		modify_lZRZJLBMC = false;
		modify_lZRZJHM = false;
		modify_lZRDH = false;
		modify_lZRDZ = false;
		modify_lZRYB = false;
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
		if (!modify_fZRY)
			listStrings.add("fZRY");
		if (!modify_fZSJ)
			listStrings.add("fZSJ");
		if (!modify_fZMC)
			listStrings.add("fZMC");
		if (!modify_fZSL)
			listStrings.add("fZSL");
		if (!modify_hFZSH)
			listStrings.add("hFZSH");
		if (!modify_lZRXM)
			listStrings.add("lZRXM");
		if (!modify_lZRZJLB)
			listStrings.add("lZRZJLB");
		if (!modify_lZRZJLBMC)
			listStrings.add("lZRZJLBMC");
		if (!modify_lZRZJHM)
			listStrings.add("lZRZJHM");
		if (!modify_lZRDH)
			listStrings.add("lZRDH");
		if (!modify_lZRDZ)
			listStrings.add("lZRDZ");
		if (!modify_lZRYB)
			listStrings.add("lZRYB");
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
