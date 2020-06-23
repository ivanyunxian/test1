package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity zrz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GenerateZRZ implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;


	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}


	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String bDCDYID;
	private boolean modify_bDCDYID = false;

	public String getBDCDYID() {
		return bDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		if (this.bDCDYID != bDCDYID) {
			this.bDCDYID = bDCDYID;
			modify_bDCDYID = true;
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

	private String bDCDYH;
	private boolean modify_bDCDYH = false;

	public String getBDCDYH() {
		return bDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		if (this.bDCDYH != bDCDYH) {
			this.bDCDYH = bDCDYH;
			modify_bDCDYH = true;
		}
	}

	private String zDDM;
	private boolean modify_zDDM = false;

	public String getZDDM() {
		return zDDM;
	}

	public void setZDDM(String zDDM) {
		if (this.zDDM != zDDM) {
			this.zDDM = zDDM;
			modify_zDDM = true;
		}
	}

	private String zRZH;
	private boolean modify_zRZH = false;

	public String getZRZH() {
		return zRZH;
	}

	public void setZRZH(String zRZH) {
		if (this.zRZH != zRZH) {
			this.zRZH = zRZH;
			modify_zRZH = true;
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

	private String jZWMC;
	private boolean modify_jZWMC = false;

	public String getJZWMC() {
		return jZWMC;
	}

	public void setJZWMC(String jZWMC) {
		if (this.jZWMC != jZWMC) {
			this.jZWMC = jZWMC;
			modify_jZWMC = true;
		}
	}

	private Date jGRQ;
	private boolean modify_jGRQ = false;

	public Date getJGRQ() {
		return jGRQ;
	}

	public void setJGRQ(Date jGRQ) {
		if (this.jGRQ != jGRQ) {
			this.jGRQ = jGRQ;
			modify_jGRQ = true;
		}
	}

	private Double jZWGD;
	private boolean modify_jZWGD = false;

	public Double getJZWGD() {
		return jZWGD;
	}

	public void setJZWGD(Double jZWGD) {
		if (this.jZWGD != jZWGD) {
			this.jZWGD = jZWGD;
			modify_jZWGD = true;
		}
	}

	private Double zZDMJ;
	private boolean modify_zZDMJ = false;

	public Double getZZDMJ() {
		return zZDMJ;
	}

	public void setZZDMJ(Double zZDMJ) {
		if (this.zZDMJ != zZDMJ) {
			this.zZDMJ = zZDMJ;
			modify_zZDMJ = true;
		}
	}

	private Double zYDMJ;
	private boolean modify_zYDMJ = false;

	public Double getZYDMJ() {
		return zYDMJ;
	}

	public void setZYDMJ(Double zYDMJ) {
		if (this.zYDMJ != zYDMJ) {
			this.zYDMJ = zYDMJ;
			modify_zYDMJ = true;
		}
	}

	private Double yCJZMJ;
	private boolean modify_yCJZMJ = false;

	public Double getYCJZMJ() {
		return yCJZMJ;
	}

	public void setYCJZMJ(Double yCJZMJ) {
		if (this.yCJZMJ != yCJZMJ) {
			this.yCJZMJ = yCJZMJ;
			modify_yCJZMJ = true;
		}
	}

	private Double sCJZMJ;
	private boolean modify_sCJZMJ = false;

	public Double getSCJZMJ() {
		return sCJZMJ;
	}

	public void setSCJZMJ(Double sCJZMJ) {
		if (this.sCJZMJ != sCJZMJ) {
			this.sCJZMJ = sCJZMJ;
			modify_sCJZMJ = true;
		}
	}

	private Double fDCJYJG;
	private boolean modify_fDCJYJG = false;

	public Double getFDCJYJG() {
		return fDCJYJG;
	}

	public void setFDCJYJG(Double fDCJYJG) {
		if (this.fDCJYJG != fDCJYJG) {
			this.fDCJYJG = fDCJYJG;
			modify_fDCJYJG = true;
		}
	}

	private Integer zCS;
	private boolean modify_zCS = false;

	public Integer getZCS() {
		return zCS;
	}

	public void setZCS(Integer zCS) {
		if (this.zCS != zCS) {
			this.zCS = zCS;
			modify_zCS = true;
		}
	}

	private Integer dSCS;
	private boolean modify_dSCS = false;

	public Integer getDSCS() {
		return dSCS;
	}

	public void setDSCS(Integer dSCS) {
		if (this.dSCS != dSCS) {
			this.dSCS = dSCS;
			modify_dSCS = true;
		}
	}

	private Integer dXCS;
	private boolean modify_dXCS = false;

	public Integer getDXCS() {
		return dXCS;
	}

	public void setDXCS(Integer dXCS) {
		if (this.dXCS != dXCS) {
			this.dXCS = dXCS;
			modify_dXCS = true;
		}
	}

	private Double dXSD;
	private boolean modify_dXSD = false;

	public Double getDXSD() {
		return dXSD;
	}

	public void setDXSD(Double dXSD) {
		if (this.dXSD != dXSD) {
			this.dXSD = dXSD;
			modify_dXSD = true;
		}
	}

	private String gHYT;
	private boolean modify_gHYT = false;

	public String getGHYT() {
		return gHYT;
	}

	public void setGHYT(String gHYT) {
		if (this.gHYT != gHYT) {
			this.gHYT = gHYT;
			modify_gHYT = true;
		}
	}

	private String fWJG;
	private boolean modify_fWJG = false;

	public String getFWJG() {
		return fWJG;
	}

	public void setFWJG(String fWJG) {
		if (this.fWJG != fWJG) {
			this.fWJG = fWJG;
			modify_fWJG = true;
		}
	}

	private Integer zTS;
	private boolean modify_zTS = false;

	public Integer getZTS() {
		return zTS;
	}

	public void setZTS(Integer zTS) {
		if (this.zTS != zTS) {
			this.zTS = zTS;
			modify_zTS = true;
		}
	}

	private String jZWJBYT;
	private boolean modify_jZWJBYT = false;

	public String getJZWJBYT() {
		return jZWJBYT;
	}

	public void setJZWJBYT(String jZWJBYT) {
		if (this.jZWJBYT != jZWJBYT) {
			this.jZWJBYT = jZWJBYT;
			modify_jZWJBYT = true;
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

	private String zT;
	private boolean modify_zT = false;

	public String getZT() {
		return zT;
	}

	public void setZT(String zT) {
		if (this.zT != zT) {
			this.zT = zT;
			modify_zT = true;
		}
	}

	private String rELATIONID;
	private boolean modify_rELATIONID = false;

	public String getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
			modify_rELATIONID = true;
		}
	}

	private String gXXMBH;
	private boolean modify_gXXMBH = false;

	public String getGXXMBH() {
		return gXXMBH;
	}

	public void setGXXMBH(String gXXMBH) {
		if (this.gXXMBH != gXXMBH) {
			this.gXXMBH = gXXMBH;
			modify_gXXMBH = true;
		}
	}

	private String fWZT;
	private boolean modify_fWZT = false;

	public String getFWZT() {
		return fWZT;
	}

	public void setFWZT(String fWZT) {
		if (this.fWZT != fWZT) {
			this.fWZT = fWZT;
			modify_fWZT = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_ySDM = false;
		modify_xMMC = false;
		modify_bDCDYH = false;
		modify_zDDM = false;
		modify_zRZH = false;
		modify_zL = false;
		modify_jZWMC = false;
		modify_jGRQ = false;
		modify_jZWGD = false;
		modify_zZDMJ = false;
		modify_zYDMJ = false;
		modify_yCJZMJ = false;
		modify_sCJZMJ = false;
		modify_fDCJYJG = false;
		modify_zCS = false;
		modify_dSCS = false;
		modify_dXCS = false;
		modify_dXSD = false;
		modify_gHYT = false;
		modify_fWJG = false;
		modify_zTS = false;
		modify_jZWJBYT = false;
		modify_bZ = false;
		modify_zT = false;
		modify_rELATIONID = false;
		modify_gXXMBH = false;
		modify_fWZT = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_xMMC)
			listStrings.add("xMMC");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_zRZH)
			listStrings.add("zRZH");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_jZWMC)
			listStrings.add("jZWMC");
		if (!modify_jGRQ)
			listStrings.add("jGRQ");
		if (!modify_jZWGD)
			listStrings.add("jZWGD");
		if (!modify_zZDMJ)
			listStrings.add("zZDMJ");
		if (!modify_zYDMJ)
			listStrings.add("zYDMJ");
		if (!modify_yCJZMJ)
			listStrings.add("yCJZMJ");
		if (!modify_sCJZMJ)
			listStrings.add("sCJZMJ");
		if (!modify_fDCJYJG)
			listStrings.add("fDCJYJG");
		if (!modify_zCS)
			listStrings.add("zCS");
		if (!modify_dSCS)
			listStrings.add("dSCS");
		if (!modify_dXCS)
			listStrings.add("dXCS");
		if (!modify_dXSD)
			listStrings.add("dXSD");
		if (!modify_gHYT)
			listStrings.add("gHYT");
		if (!modify_fWJG)
			listStrings.add("fWJG");
		if (!modify_zTS)
			listStrings.add("zTS");
		if (!modify_jZWJBYT)
			listStrings.add("jZWJBYT");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_fWZT)
			listStrings.add("fWZT");

		return StringHelper.ListToStrings(listStrings);
	}
}
