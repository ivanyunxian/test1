package com.supermap.realestate.registration.model;

//liangcheng 2019-05-23 10:32 am


import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SFXX;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "bdcs_sfxx", schema = "bdck")
public class BDCS_SFXX extends GenerateBDCS_SFXX {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}
	
	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}
	
	@Override
	@Column(name = "jkry")
	public String getJKRY() {
		return super.getJKRY();
	}
	
	@Override
	@Column(name = "fkfs")
	public String getFKFS() {
		return super.getFKFS();
	}
	
	@Override
	@Column(name = "sfry")
	public String getSFRY() {
		return super.getSFRY();
	}
	
	@Override
	@Column(name = "sfsj")
	public Date getSFSJ() {
		return super.getSFSJ();
	}
	
	@Override
	@Column(name = "sfzt")
	public String getSFZT() {
		return super.getSFZT();
	}
	
	@Override
	@Column(name = "sfje")
	public Double getSFJE() {
		return super.getSFJE();
	}

	@Override
	@Column(name = "ywlsh")
	public String getYWLSH() {
		return super.getYWLSH();
	}

	@Override
	@Column(name = "jfdh")
	public String getJFDH() {
		return super.getJFDH();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}
	
	@Override
	@Column(name = "createdate")
	public Date getCREATEDATE() {
		return super.getCREATEDATE();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}
	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}
	@Override
	@Column(name = "pjzt")
	public String getPJZT() {
		return super.getPJZT();
	}
	@Override
	@Column(name = "pjbz")
	public String getPJBZ() {
		return super.getPJBZ();
	}

}
