package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DBJS;

@Entity
@Table(name = "bdcs_dbjs", schema = "bdck")
public class BDCS_DBJS extends GenerateBDCS_DBJS {

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
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}
	
	@Override
	@Column(name = "actinst_id")
	public String getACTINST_ID() {
		return super.getACTINST_ID();
	}
	
	@Override
	@Column(name = "user_id")
	public String getUSER_ID() {
		return super.getUSER_ID();
	}
	
	@Override
	@Column(name = "js")
	public String getJS() {
		return super.getJS();
	}
}
