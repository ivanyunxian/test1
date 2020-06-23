package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-3 
//* ----------------------------------------
//* Internal Entity BDCS_DCXM 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateDCS_DCXM implements SuperModel<String> {

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

	private String dCXMMC;
	private boolean modify_dCXMMC = false;

	public String getDCXMMC() {
		return dCXMMC;
	}

	public void setDCXMMC(String dCXMMC) {
		if (this.dCXMMC != dCXMMC) {
			this.dCXMMC = dCXMMC;
			modify_dCXMMC = true;
		}
	}

	private Integer cJRYID;
	private boolean modify_cJRYID = false;

	public Integer getCJRYID() {
		return cJRYID;
	}

	public void setCJRYID(Integer cJRYID) {
		if (this.cJRYID != cJRYID) {
			this.cJRYID = cJRYID;
			modify_cJRYID = true;
		}
	}

	private String cJRY;
	private boolean modify_cJRY = false;

	public String getCJRY() {
		return cJRY;
	}

	public void setCJRY(String cJRY) {
		if (this.cJRY != cJRY) {
			this.cJRY = cJRY;
			modify_cJRY = true;
		}
	}

	private Date cJSJ;
	private boolean modify_cJSJ = false;

	public Date getCJSJ() {
		return cJSJ;
	}

	public void setCJSJ(Date cJSJ) {
		if (this.cJSJ != cJSJ) {
			this.cJSJ = cJSJ;
			modify_cJSJ = true;
		}
	}

	private String dCRY;
	private boolean modify_dCRY = false;

	public String getDCRY() {
		return dCRY;
	}

	public void setDCRY(String dCRY) {
		if (this.dCRY != dCRY) {
			this.dCRY = dCRY;
			modify_dCRY = true;
		}
	}

	private Date dCSJ;
	private boolean modify_dCSJ = false;

	public Date getDCSJ() {
		return dCSJ;
	}

	public void setDCSJ(Date dCSJ) {
		if (this.dCSJ != dCSJ) {
			this.dCSJ = dCSJ;
			modify_dCSJ = true;
		}
	}

	private String xMLB;
	private boolean modify_xMLB = false;

	public String getXMLB() {
		return xMLB;
	}

	public void setXMLB(String xMLB) {
		if (this.xMLB != xMLB) {
			this.xMLB = xMLB;
			modify_xMLB = true;
		}
	}

	private Integer yXBZ;
	private boolean modify_yXBZ = false;

	public Integer getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(Integer yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	private Integer xMZT;
	private boolean modify_xMZT = false;

	public Integer getXMZT() {
		return xMZT;
	}

	public void setXMZT(Integer xMZT) {
		if (this.xMZT != xMZT) {
			this.xMZT = xMZT;
			modify_xMZT = true;
		}
	}

	private Integer xMLX;
	private boolean modify_xMLX = false;
	
	public Integer getXMLX() {
		return xMLX;
	}

	public void setXMLX(Integer xMLX) {
		if (this.xMLX != xMLX) {
			this.xMLX = xMLX;
			modify_xMLX = true;
		}
	}
	
	/**	数量 */
	private String countTotal;
	
	@Transient
	public String getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(String countTotal) {
		this.countTotal = countTotal;
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_dCXMMC = false;
		modify_cJRYID = false;
		modify_cJRY = false;
		modify_cJSJ = false;
		modify_dCRY = false;
		modify_dCSJ = false;
		modify_xMLB = false;
		modify_yXBZ = false;
		modify_xMZT = false;
		modify_xMLX = false;
	}

	@Override
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_dCXMMC)
			listStrings.add("dCXMMC");
		if (!modify_cJRYID)
			listStrings.add("cJRYID");
		if (!modify_cJRY)
			listStrings.add("cJRY");
		if (!modify_cJSJ)
			listStrings.add("cJSJ");
		if (!modify_dCRY)
			listStrings.add("dCRY");
		if (!modify_dCSJ)
			listStrings.add("dCSJ");
		if (!modify_xMLB)
			listStrings.add("xMLB");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_xMZT)
			listStrings.add("xMZT");
		if (!modify_xMLX)
			listStrings.add("xMLX");

		return StringHelper.ListToStrings(listStrings);
	}
}
