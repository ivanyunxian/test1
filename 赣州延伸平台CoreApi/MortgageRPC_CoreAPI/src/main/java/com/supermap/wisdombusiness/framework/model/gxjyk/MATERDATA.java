package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/9 
//* ----------------------------------------
//* Public Entity materdata 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import oracle.sql.BLOB;

import java.sql.Blob;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateMATERDATA;

@Entity
@Table(name = "materdata", schema = "gxjyk")
public class MATERDATA extends GenerateMATERDATA {

//	@Override
//	@Id
//	@Column(name = "id", length = 4000)
//	public String getId() {
//		return super.getId();
//	}

	@Override
	@Id
	@Column(name = "materialdata_id")
	public String getMATERIALDATA_ID() {
		return super.getMATERIALDATA_ID();
	}

	@Override
	@Column(name = "materilinst_id")
	public String getMATERILINST_ID() {
		return super.getMATERILINST_ID();
	}

	@Override
	@Column(name = "upload_name")
	public String getUPLOAD_NAME() {
		return super.getUPLOAD_NAME();
	}

	@Override
	@Column(name = "upload_id")
	public String getUPLOAD_ID() {
		return super.getUPLOAD_ID();
	}

	@Override
	@Column(name = "upload_date")
	public Date getUPLOAD_DATE() {
		return super.getUPLOAD_DATE();
	}

	@Override
	@Column(name = "file_name")
	public String getFILE_NAME() {
		return super.getFILE_NAME();
	}

	@Override
	@Column(name = "file_postfix")
	public String getFILE_POSTFIX() {
		return super.getFILE_POSTFIX();
	}

	@Override
	@Column(name = "file_year")
	public String getFILE_YEAR() {
		return super.getFILE_YEAR();
	}

	@Override
	@Column(name = "storage_type")
	public Integer getSTORAGE_TYPE() {
		return super.getSTORAGE_TYPE();
	}

	@Override
	@Column(name = "file_path")
	public String getFILE_PATH() {
		return super.getFILE_PATH();
	}

	@Override
	@Column(name = "file_index")
	public Integer getFILE_INDEX() {
		return super.getFILE_INDEX();
	}

	@Override
	@Column(name = "thumb")
	public Blob getTHUMB() {
		return super.getTHUMB();
	}

	@Override
	@Column(name = "file_number")
	public String getFILE_NUMBER() {
		return super.getFILE_NUMBER();
	}

	@Override
	@Column(name = "upload_status")
	public Integer getUPLOAD_STATUS() {
		return super.getUPLOAD_STATUS();
	}

	@Override
	@Column(name = "path")
	public String getPATH() {
		return super.getPATH();
	}
	
	@Override
	@Column(name = "relativeurl")
	public String getRELATIVEURL() {
		return super.getRELATIVEURL();
	}
}
