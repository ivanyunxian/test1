package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_djfz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJFZ;

@Entity
@Table(name = "bdcs_djfz", schema = "bdck")
public class BDCS_DJFZ extends GenerateBDCS_DJFZ {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "fzry")
	public String getFZRY() {
		return super.getFZRY();
	}

	@Override
	@Column(name = "fzsj")
	public Date getFZSJ() {
		return super.getFZSJ();
	}

	@Override
	@Column(name = "fzmc")
	public String getFZMC() {
		return super.getFZMC();
	}

	@Override
	@Column(name = "fzsl")
	public Integer getFZSL() {
		return super.getFZSL();
	}

	@Override
	@Column(name = "hfzsh")
	public String getHFZSH() {
		return super.getHFZSH();
	}

	@Override
	@Column(name = "lzrxm")
	public String getLZRXM() {
		return super.getLZRXM();
	}

	@Override
	@Column(name = "lzrzjlb")
	public String getLZRZJLB() {
		return super.getLZRZJLB();
	}

	@Override
	@Column(name = "lzrzjlbmc")
	public String getLZRZJLBMC() {
		return super.getLZRZJLBMC();
	}

	@Override
	@Column(name = "lzrzjhm")
	public String getLZRZJHM() {
		return super.getLZRZJHM();
	}

	@Override
	@Column(name = "lzrdh")
	public String getLZRDH() {
		return super.getLZRDH();
	}

	@Override
	@Column(name = "lzrdz")
	public String getLZRDZ() {
		return super.getLZRDZ();
	}

	@Override
	@Column(name = "lzryb")
	public String getLZRYB() {
		return super.getLZRYB();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "createtime")
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@Column(name = "modifytime")
	public Date getModifyTime() {
		return super.getModifyTime();
	}
}
