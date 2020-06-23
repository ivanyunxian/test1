package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QYXX;

@Entity
@Table(name="bdcs_qyxx",schema="bdck")
public class BDCS_QYXX extends GenerateBDCS_QYXX{
	
	@Override
	@Id
	@Column(name="qyxxid",length=50)
	public String getId(){
		return super.getId();
	}
	
	@Override
	@Column(name="project_id")
	public String getPROJECT_ID(){
		return super.getPROJECT_ID();
	}
	
	@Override
	@Column(name="qymc")
	public String getQYMC(){
		return super.getQYMC();
	}
	
	@Override
	@Column(name="tyshxydm")
	public String getTYSHXYDM(){
		return super.getTYSHXYDM();
	}
	
	@Override
	@Column(name="qydz")
	public String getQYDZ(){
		return super.getQYDZ();
	}
	
	@Override
	@Column(name="fddbr")
	public String getFDDBR(){
		return super.getFDDBR();
	}
	
	@Override
	@Column(name="fddbrzjh")
	public String getFDDBRZJH(){
		return super.getFDDBRZJH();
	}
	
	@Override
	@Column(name="zczxm")
	public String getZCZXM(){
		return super.getZCZXM();
	}
	
	@Override
	@Column(name="zczzjh")
	public String getZCZZJH(){
		return super.getZCZZJH();
	}
	
	@Override
	@Column(name="zczsjh")
	public String getZCZSJH(){
		return super.getZCZSJH();
	}
	
	@Override
	@Column(name="shzt")
	public String getSHZT(){
		return super.getSHZT();
	}
}
