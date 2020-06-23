package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/26 
//* ----------------------------------------
//* Internal Entity bdcs_idcard_pic 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_IDCARD_PIC implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
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

	private String zJH;
	private boolean modify_zJH = false;

	public String getZJH() {
		return zJH;
	}

	public void setZJH(String zJH) {
		if (this.zJH != zJH) {
			this.zJH = zJH;
			modify_zJH = true;
		}
	}

	private String pIC1;
	private boolean modify_pIC1 = false;

	public String getPIC1() {
		return pIC1;
	}

	public void setPIC1(String pIC1) {
		if (this.pIC1 != pIC1) {
			this.pIC1 = pIC1;
			modify_pIC1 = true;
		}
	}

	private String pIC2;
	private boolean modify_pIC2 = false;

	public String getPIC2() {
		return pIC2;
	}

	public void setPIC2(String pIC2) {
		if (this.pIC2 != pIC2) {
			this.pIC2 = pIC2;
			modify_pIC2 = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_zJH = false;
		modify_pIC1 = false;
		modify_pIC2 = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_zJH)
			listStrings.add("zJH");
		if (!modify_pIC1)
			listStrings.add("pIC1");
		if (!modify_pIC2)
			listStrings.add("pIC2");

		return StringHelper.ListToStrings(listStrings);
	}
}
