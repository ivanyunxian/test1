package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Public Entity bdcs_rkqzb 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_RKQZB;

@Entity
@Table(name = "bdcs_rkqzb", schema = "bdck")
public class BDCS_RKQZB extends GenerateBDCS_RKQZB {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}
	
	@Override
	@Column(name = "xmid")
	public String getXMID() {
		return super.getXMID();
	}
	/*--------------------------------------权证信息--------------------------------------*/
	@Override
	@Column(name = "qzbh")
	public Long getQZBH() {
		return super.getQZBH();
	}
	
	@Override
	@Column(name = "qzzl")
	public String getQZZL() {
		return super.getQZZL();
	}
	
	@Override
	@Column(name = "lqry")
	public String getLQRY() {
		return super.getLQRY();
	}
	
	@Override
	@Column(name = "lqryid")
	public String getLQRYID() {
		return super.getLQRYID();
	}
	
	@Override
	@Column(name = "lqks")
	public String getLQKS() {
		return super.getLQKS();
	}
	
	@Override
	@Column(name = "lqksid")
	public String getLQKSID() {
		return super.getLQKSID();
	}
	
	@Override
	@Column(name = "syqk")
	public String getSYQK() {
		return super.getSYQK();
	}
	
	@Override
	@Column(name = "qzlx")
	public String getQZLX() {
		return super.getQZLX();
	}
	
	@Override
	@Column(name = "zszt")
	public String getZSZT() {
		return super.getZSZT();
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(name = "cjsj")
	public Date getCJSJ() {
		return super.getCJSJ();
	}
	/*--------------------------------------权证信息--------------------------------------*/
	
	/*--------------------------------------缮证情况--------------------------------------*/
	@Override
	@Column(name = "sfsz")
	public String getSFSZ() {
		return super.getSFSZ();
	}

	@Override
	@Column(name = "szr")
	public String getSZR() {
		return super.getSZR();
	}

	@Override
	@Column(name = "szsj")
	public Date getSZSJ() {
		return super.getSZSJ();
	}
	
	@Override
	@Column(name = "szry")
	public String getSZRY() {
		return super.getSZRY();
	}
	
	@Override
	@Column(name = "qlrmc")
	public String getQLRMC() {
		return super.getQLRMC();
	}
	
	@Override
	@Column(name = "qlrzjh")
	public String getQLRZJH() {
		return super.getQLRZJH();
	}
	
	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}
	/*--------------------------------------缮证情况--------------------------------------*/
	/*--------------------------------------发证情况--------------------------------------*/
	@Override
	@Column(name = "sffz")
	public String getSFFZ() {
		return super.getSFFZ();
	}
	
	@Override
	@Column(name = "lzry")
	public String getLZRY() {
		return super.getLZRY();
	}
	
	@Override
	@Column(name = "lzrzjh")
	public String getLZRZJH() {
		return super.getLZRZJH();
	}
	
	@Override
	@Column(name = "fzsj")
	public Date getFZSJ() {
		return super.getFZSJ();
	}
	
	@Override
	@Column(name = "lzrpic")
	public String getLZRPIC() {
		return super.getLZRPIC();
	}
	/*--------------------------------------发证情况--------------------------------------*/
	/*--------------------------------------作废情况--------------------------------------*/
	@Override
	@Column(name = "sfzf")
	public String getSFZF() {
		return super.getSFZF();
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(name = "zfsj")
	public Date getZFSJ() {
		return super.getZFSJ();
	}

	@Override
	@Column(name = "zfyy")
	public String getZFYY() {
		return super.getZFYY();
	}

	@Override
	@Column(name = "zfr")
	public String getZFR() {
		return super.getZFR();
	}
	/*--------------------------------------作废情况--------------------------------------*/
	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}
	@Override
	@Column(name = "slbh")
	public String getSLBH() {
		return super.getSLBH();
	}
}
