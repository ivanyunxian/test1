package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QYRY;

@Entity
@Table(name="bdcs_qyry",schema="bdck")
public class BDCS_QYRY extends GenerateBDCS_QYRY{

	@Override
	@Column(name="qyryid",length = 50)
	@Id
	public String getId(){
		return super.getId();
	}
	
	@Override
	@Column(name="qyid")
	public String getQYID(){
		return super.getQYID();
	}
	
	@Override
	@Column(name="rymc")
	public String getRYMC(){
		return super.getRYMC();
	}
	
	@Override
	@Column(name="lxdh")
	public String getLXDH(){
		return super.getLXDH();
	}
	
	@Override
	@Column(name="txdz")
	public String getTXDZ(){
		return super.getTXDZ();
	}
	
	@Override
	@Column(name="xb")
	public String getXB(){
		return super.getXB();
	}
	
	@Override
	@Column(name="zjh")
	public String getZJH(){
		return super.getZJH();
	}
	
	@Override
	@Column(name="zjlx")
	public String getZJLX(){
		return super.getZJLX();
	}
	
	@Override
	@Column(name="loginname")
	public String getLOGINNAME(){
		return super.getLOGINNAME();
	}
	
	@Override
	@Column(name="shzt")
	public String getSHZT(){
		return super.getSHZT();
	}

	@Override
	@Column(name="project_id")
	public String getPROJECT_ID(){
		return super.getPROJECT_ID();
	}
}
