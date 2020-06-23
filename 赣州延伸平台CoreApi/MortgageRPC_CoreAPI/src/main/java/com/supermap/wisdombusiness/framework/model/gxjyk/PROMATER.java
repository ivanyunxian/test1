package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/9 
//* ----------------------------------------
//* Public Entity promater 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GeneratePROMATER;

@Entity
@Table(name = "promater", schema = "gxjyk")
public class PROMATER extends GeneratePROMATER {

//	@Override
//	@Id
//	@Column(name = "id", length = 4000)
//	public String getId() {
//		return super.getId();
//	}

	@Override
	@Id
	@Column(name = "materilinst_id")
	public String getMATERILINST_ID() {
		return super.getMATERILINST_ID();
	}

	@Override
	@Column(name = "material_id")
	public String getMATERIAL_ID() {
		return super.getMATERIAL_ID();
	}

	@Override
	@Column(name = "material_index")
	public Integer getMATERIAL_INDEX() {
		return super.getMATERIAL_INDEX();
	}

	@Override
	@Column(name = "material_name")
	public String getMATERIAL_NAME() {
		return super.getMATERIAL_NAME();
	}

	@Override
	@Column(name = "material_type")
	public Integer getMATERIAL_TYPE() {
		return super.getMATERIAL_TYPE();
	}

	@Override
	@Column(name = "material_need")
	public Integer getMATERIAL_NEED() {
		return super.getMATERIAL_NEED();
	}

	@Override
	@Column(name = "materialdef_id")
	public String getMATERIALDEF_ID() {
		return super.getMATERIALDEF_ID();
	}

	@Override
	@Column(name = "proinst_id")
	public String getPROINST_ID() {
		return super.getPROINST_ID();
	}

	@Override
	@Column(name = "img_path")
	public String getIMG_PATH() {
		return super.getIMG_PATH();
	}

	@Override
	@Column(name = "materialtype_id")
	public String getMATERIALTYPE_ID() {
		return super.getMATERIALTYPE_ID();
	}

	@Override
	@Column(name = "material_status")
	public Integer getMATERIAL_STATUS() {
		return super.getMATERIAL_STATUS();
	}

	@Override
	@Column(name = "material_date")
	public Date getMATERIAL_DATE() {
		return super.getMATERIAL_DATE();
	}
}
