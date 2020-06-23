package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QYDYGX;

@Entity
@Table(name="bdcs_qydygx",schema="bdck")
public class BDCS_QYDYGX extends GenerateBDCS_QYDYGX{

	@Override
	@Column(name="qydygxid")
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
	@Column(name="bdcdyh")
	public String getBDCDYH(){
		return super.getBDCDYH();
	}
	
	@Override
	@Column(name="wzdid")
	public String getWZDID(){
		return super.getWZDID();
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
