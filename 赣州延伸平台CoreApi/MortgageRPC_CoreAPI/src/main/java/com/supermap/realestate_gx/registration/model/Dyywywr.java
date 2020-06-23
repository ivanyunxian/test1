package com.supermap.realestate_gx.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 抵押业务义务人表
 */
@Entity
@Table(name = "DYYWYWR", schema = "GLSZFGJJ")
public class Dyywywr {

	@Id
	@Column(name = "ID", length = 64)
	private String ID;
	private String GJJYWLSH;
	private String YWR;
	private String YWRZJZL;
	private String YWRZJH;
	private String BDCQZH;
	private String SQRLX;
	private String GYFS;
	private String DH;
	private String DZ;
	private String FDDBR;
	private String FDDBRDH;
	private String FDDBRZJHM;
	private String DLRXM;
	private String DLRZJHM;
	private String DLRLXDH;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getGJJYWLSH() {
		return GJJYWLSH;
	}
	public void setGJJYWLSH(String gJJYWLSH) {
		GJJYWLSH = gJJYWLSH;
	}
	public String getYWR() {
		return YWR;
	}
	public void setYWR(String yWR) {
		YWR = yWR;
	}
	public String getYWRZJZL() {
		return YWRZJZL;
	}
	public void setYWRZJZL(String yWRZJZL) {
		YWRZJZL = yWRZJZL;
	}
	public String getYWRZJH() {
		return YWRZJH;
	}
	public void setYWRZJH(String yWRZJH) {
		YWRZJH = yWRZJH;
	}
	public String getBDCQZH() {
		return BDCQZH;
	}
	public void setBDCQZH(String bDCQZH) {
		BDCQZH = bDCQZH;
	}
	public String getSQRLX() {
		return SQRLX;
	}
	public void setSQRLX(String sQRLX) {
		SQRLX = sQRLX;
	}
	public String getGYFS() {
		return GYFS;
	}
	public void setGYFS(String gYFS) {
		GYFS = gYFS;
	}
	public String getDH() {
		return DH;
	}
	public void setDH(String dH) {
		DH = dH;
	}
	public String getDZ() {
		return DZ;
	}
	public void setDZ(String dZ) {
		DZ = dZ;
	}
	public String getFDDBR() {
		return FDDBR;
	}
	public void setFDDBR(String fDDBR) {
		FDDBR = fDDBR;
	}
	public String getFDDBRDH() {
		return FDDBRDH;
	}
	public void setFDDBRDH(String fDDBRDH) {
		FDDBRDH = fDDBRDH;
	}
	public String getFDDBRZJHM() {
		return FDDBRZJHM;
	}
	public void setFDDBRZJHM(String fDDBRZJHM) {
		FDDBRZJHM = fDDBRZJHM;
	}
	public String getDLRXM() {
		return DLRXM;
	}
	public void setDLRXM(String dLRXM) {
		DLRXM = dLRXM;
	}
	public String getDLRZJHM() {
		return DLRZJHM;
	}
	public void setDLRZJHM(String dLRZJHM) {
		DLRZJHM = dLRZJHM;
	}
	public String getDLRLXDH() {
		return DLRLXDH;
	}
	public void setDLRLXDH(String dLRLXDH) {
		DLRLXDH = dLRLXDH;
	}
	
}
