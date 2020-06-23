package com.supermap.yingtanothers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "r_fw_ql", schema = "gxfck")
public class R_FW_QL {
	
	private String gxxmbh;
	private String qlid;
	private String houseid;
	private String r_fw_ql_id;
	
	@Column(name = "gxxmbh")
	public String getGxxmbh() {
		return gxxmbh;
	}
	public void setGxxmbh(String gxxmbh) {
		this.gxxmbh = gxxmbh;
	}
	@Column(name = "qlid")
	public String getQlid() {
		return qlid;
	}
	public void setQlid(String qlid) {
		this.qlid = qlid;
	}
	@Id
	@Column(name = "houseid")
	public String getHouseid() {
		return houseid;
	}
	public void setHouseid(String houseid) {
		this.houseid = houseid;
	}
	@Column(name = "r_fw_ql_id")
	public String getR_fw_ql_id() {
		return r_fw_ql_id;
	}
	public void setR_fw_ql_id(String r_fw_ql_id) {
		this.r_fw_ql_id = r_fw_ql_id;
	}

}
