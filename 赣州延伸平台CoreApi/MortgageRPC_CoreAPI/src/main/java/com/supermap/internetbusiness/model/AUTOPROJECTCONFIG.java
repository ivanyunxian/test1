package com.supermap.internetbusiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "AUTOPROJECTCONFIG", schema = "BDCK")
public class AUTOPROJECTCONFIG {
	private String ID;
	private String PRODEF_ID;
	private String OLD_PRODEF_ID;
	private String DIVISIONCODE;
	private String PRODEF_NAME;
	private String PRODEF_CODE;
	private String DJYY;
	private String FJ;
	private String SPYJ;
	private String STAFFID;
	private String STAFFNAME;
	private String SFQY;
	private String SYQR;
	private String BZ;
	private String DBR;
	private String DBRID;
	private String SZRYID;
	private String SZRY;
	private String ROUTEID;
	private String CZFS;
	private String ZXDBR;
	private String ISHANGUP;
	private String ISYDB;
	private Date CREATETIME;
	private Date MODIFY_TIME;
	private String MODIFY_USERID;
	private String MODIFY_USERNAME;
	private String SFZDHQQZH;
	private String ROLEID;

	@Id
	@Column(name = "ID", length = 50)
	public String getID() {
		if (ID == null) {
			ID = UUID.randomUUID().toString().replace("-", "");
		}
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	@Column(name = "PRODEF_ID")
	public String getPRODEF_ID() {
		return PRODEF_ID;
	}

	public void setPRODEF_ID(String PRODEF_ID) {
		this.PRODEF_ID = PRODEF_ID;
	}

	@Column(name = "PRODEF_NAME")
	public String getPRODEF_NAME() {
		return PRODEF_NAME;
	}

	public void setPRODEF_NAME(String PRODEF_NAME) {
		this.PRODEF_NAME = PRODEF_NAME;
	}

	@Column(name = "PRODEF_CODE")
	public String getPRODEF_CODE() {
		return PRODEF_CODE;
	}

	public void setPRODEF_CODE(String PRODEF_CODE) {
		this.PRODEF_CODE = PRODEF_CODE;
	}

	@Column(name = "DJYY")
	public String getDJYY() {
		return DJYY;
	}

	public void setDJYY(String DJYY) {
		this.DJYY = DJYY;
	}

	@Column(name = "FJ")
	public String getFJ() {
		return FJ;
	}

	public void setFJ(String FJ) {
		this.FJ = FJ;
	}

	@Column(name = "SPYJ")
	public String getSPYJ() {
		return SPYJ;
	}

	public void setSPYJ(String SPYJ) {
		this.SPYJ = SPYJ;
	}

	@Column(name = "STAFFID")
	public String getSTAFFID() {
		return STAFFID;
	}

	public void setSTAFFID(String STAFFID) {
		this.STAFFID = STAFFID;
	}

	@Column(name = "STAFFNAME")
	public String getSTAFFNAME() {
		return STAFFNAME;
	}

	public void setSTAFFNAME(String STAFFNAME) {
		this.STAFFNAME = STAFFNAME;
	}

	@Column(name = "SFQY")
	public String getSFQY() {
		return SFQY;
	}

	public void setSFQY(String SFQY) {
		this.SFQY = SFQY;
	}

	@Column(name = "SYQR")
	public String getSYQR() {
		return SYQR;
	}

	public void setSYQR(String SYQR) {
		this.SYQR = SYQR;
	}

	@Column(name = "BZ")
	public String getBZ() {
		return BZ;
	}

	public void setBZ(String BZ) {
		this.BZ = BZ;
	}

	@Column(name = "DBR")
	public String getDBR() {
		return DBR;
	}

	public void setDBR(String DBR) {
		this.DBR = DBR;
	}

	@Column(name = "DBRID")
	public String getDBRID() {
		return DBRID;
	}

	public void setDBRID(String DBRID) {
		this.DBRID = DBRID;
	}

	@Column(name = "SZRYID")
	public String getSZRYID() {
		return SZRYID;
	}

	public void setSZRYID(String SZRYID) {
		this.SZRYID = SZRYID;
	}

	@Column(name = "SZRY")
	public String getSZRY() {
		return SZRY;
	}

	public void setSZRY(String SZRY) {
		this.SZRY = SZRY;
	}

	@Column(name = "ROUTEID")
	public String getROUTEID() {
		return ROUTEID;
	}

	public void setROUTEID(String ROUTEID) {
		this.ROUTEID = ROUTEID;
	}

	@Column(name = "CZFS")
	public String getCZFS() {
		return CZFS;
	}

	public void setCZFS(String CZFS) {
		this.CZFS = CZFS;
	}

	@Column(name = "ZXDBR")
	public String getZXDBR() {
		return ZXDBR;
	}

	public void setZXDBR(String ZXDBR) {
		this.ZXDBR = ZXDBR;
	}

	@Column(name = "ISHANGUP")
	public String getISHANGUP() {
		return ISHANGUP;
	}

	public void setISHANGUP(String ISHANGUP) {
		this.ISHANGUP = ISHANGUP;
	}

	@Column(name = "ISYDB")
	public String getISYDB() {
		return ISYDB;
	}

	public void setISYDB(String ISYDB) {
		this.ISYDB = ISYDB;
	}

	@Column(name = "CREATETIME")
	public Date getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Date CREATETIME) {
		this.CREATETIME = CREATETIME;
	}

	@Column(name = "MODIFY_TIME")
	public Date getMODIFY_TIME() {
		return MODIFY_TIME;
	}

	public void setMODIFY_TIME(Date MODIFY_TIME) {
		this.MODIFY_TIME = MODIFY_TIME;
	}

	@Column(name = "MODIFY_USERID")
	public String getMODIFY_USERID() {
		return MODIFY_USERID;
	}

	public void setMODIFY_USERID(String MODIFY_USERID) {
		this.MODIFY_USERID = MODIFY_USERID;
	}

	@Column(name = "MODIFY_USERNAME")
	public String getMODIFY_USERNAME() {
		return MODIFY_USERNAME;
	}

	public void setMODIFY_USERNAME(String MODIFY_USERNAME) {
		this.MODIFY_USERNAME = MODIFY_USERNAME;
	}

	@Column(name = "SFZDHQQZH")
	public String getSFZDHQQZH() {
		return SFZDHQQZH;
	}

	public void setSFZDHQQZH(String SFZDHQQZH) {
		this.SFZDHQQZH = SFZDHQQZH;
	}

	@Column(name="ROLEID")
	public String getROLEID() {
		return ROLEID;
	}

	public void setROLEID(String rOLEID) {
		ROLEID = rOLEID;
	}
	
	public String getOLD_PRODEF_ID() {
		return OLD_PRODEF_ID;
	}

	public void setOLD_PRODEF_ID(String oLD_PRODEF_ID) {
		OLD_PRODEF_ID = oLD_PRODEF_ID;
	}

	public String getDIVISIONCODE() {
		return DIVISIONCODE;
	}

	public void setDIVISIONCODE(String dIVISIONCODE) {
		DIVISIONCODE = dIVISIONCODE;
	}
}
