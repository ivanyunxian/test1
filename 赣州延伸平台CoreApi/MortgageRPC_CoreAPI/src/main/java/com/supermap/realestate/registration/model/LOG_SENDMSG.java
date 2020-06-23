package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateLOG_SENDMSG;

@Entity
@Table(name = "log_sendmsg", schema = "LOG")
public class LOG_SENDMSG extends GenerateLOG_SENDMSG{
	@Override
	@Id
	@Column(name = "id" )
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "sqrxm")
	public String getSQRXM() {
		return super.getSQRXM();
	}

	@Override
	@Column(name = "lxdh")
	public String getLXDH() {
		return super.getLXDH();
	}
	
	@Override
	@Column(name = "dlrxm")
	public String getDLRXM() {
		return super.getDLRXM();
	}
	
	@Override
	@Column(name = "dlrlxdh")
	public String getDLRLXDH() {
		return super.getDLRLXDH();
	}
	
	@Override
	@Column(name = "ywlsh")
	public String getYWLSH() {
		return super.getYWLSH();
	}
	
	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}
	
	@Override
	@Column(name = "zsbh")
	public String getZSBH() {
		return super.getZSBH();
	}
	
	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}
	
	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}
	
	@Override
	@Column(name = "szsj")
	public Date getSZSJ() {
		return super.getSZSJ();
	}

	@Override
	@Column(name = "sendtime")
	public Date getSENDTIME() {
		return super.getSENDTIME();
	}
	
	@Override
	@Column(name = "senduser")
	public String getSENDUSER() {
		return super.getSENDUSER();
	}
	
	@Override
	@Column(name = "sendsqrstatus")
	public String getSENDSQRSTATUS() {
		return super.getSENDSQRSTATUS();
	}
	
	@Override
	@Column(name = "senddlrstatus")
	public String getSENDDLRSTATUS() {
		return super.getSENDDLRSTATUS();
	}
	
	@Override
	@Column(name = "sendlx")
	public String getSENDLX() {
		return super.getSENDLX();
	}
	
	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}
	
	@Override
	@Column(name = "sqrid")
	public String getSQRID() {
		return super.getSQRID();
	}
	
	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

}
