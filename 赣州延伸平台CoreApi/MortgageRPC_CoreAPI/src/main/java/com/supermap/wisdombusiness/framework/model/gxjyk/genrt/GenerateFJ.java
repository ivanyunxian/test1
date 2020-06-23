package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity fj 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;


public class GenerateFJ implements SuperModel<String> {

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

	private String fL1;
	private boolean modify_fL1 = false;

	public String getFL1() {
		return fL1;
	}

	public void setFL1(String fL1) {
		if (this.fL1 != fL1) {
			this.fL1 = fL1;
			modify_fL1 = true;
		}
	}

	private String fL2;
	private boolean modify_fL2 = false;

	public String getFL2() {
		return fL2;
	}

	public void setFL2(String fL2) {
		if (this.fL2 != fL2) {
			this.fL2 = fL2;
			modify_fL2 = true;
		}
	}

	private String wJMC;
	private boolean modify_wJMC = false;

	public String getWJMC() {
		return wJMC;
	}

	public void setWJMC(String wJMC) {
		if (this.wJMC != wJMC) {
			this.wJMC = wJMC;
			modify_wJMC = true;
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

	private String hZM;
	private boolean modify_hZM = false;

	public String getHZM() {
		return hZM;
	}

	public void setHZM(String hZM) {
		if (this.hZM != hZM) {
			this.hZM = hZM;
			modify_hZM = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_gXXMBH = false;
		modify_fL1 = false;
		modify_fL2 = false;
		modify_wJMC = false;
		modify_sXH = false;
		modify_hZM = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_gXXMBH)
			listStrings.add("gXXMBH");
		if (!modify_fL1)
			listStrings.add("fL1");
		if (!modify_fL2)
			listStrings.add("fL2");
		if (!modify_wJMC)
			listStrings.add("wJMC");
		if (!modify_sXH)
			listStrings.add("sXH");
		if (!modify_hZM)
			listStrings.add("hZM");

		return StringHelper.ListToStrings(listStrings);
	}
}
