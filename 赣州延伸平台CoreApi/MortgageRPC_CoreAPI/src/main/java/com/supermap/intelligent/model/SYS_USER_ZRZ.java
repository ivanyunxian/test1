package com.supermap.intelligent.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_USER_ZRZ")
public class SYS_USER_ZRZ {
	
	private String ID;
	private String USER_ID;
	private String ZRZ_ID;
	
	@Id
	@Column(name="ID")
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name="USER_ID")
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	
	@Column(name="ZRZ_ID")
	public String getZRZ_ID() {
		return ZRZ_ID;
	}
	public void setZRZ_ID(String zRZ_ID) {
		ZRZ_ID = zRZ_ID;
	}
}
