package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-30 
//* ----------------------------------------
//* Public Entity bdcs_ql_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_QL_GZ;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_ql_gz", schema = "bdcdck")
public class DCS_QL_GZ extends GenerateDCS_QL_GZ implements Rights {

	@Override
	@Id
	@Column(name = "qlid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "djdyid")
	public String getDJDYID() {
		return super.getDJDYID();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "fsqlid")
	public String getFSQLID() {
		return super.getFSQLID();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	private String qllxname;

	@Transient
	public String getQLLXName() {
		if (qllxname == null) {
			if (this.getQLLX() != null) {
				qllxname = ConstHelper.getNameByValue("QLLX", this.getQLLX());
			}
		}
		return qllxname;
	}

	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
	}

	private String djlxname;

	@Transient
	public String getDJLXName() {
		if (djlxname == null) {
			if (this.getDJLX() != null) {
				djlxname = ConstHelper.getNameByValue("DJLX", this.getDJLX());
			}
		}
		return djlxname;
	}

	@Override
	@Column(name = "djyy")
	public String getDJYY() {
		return super.getDJYY();
	}

	@Override
	@Column(name = "qlqssj")
	public Date getQLQSSJ() {
		return super.getQLQSSJ();
	}

	@Override
	@Column(name = "qljssj")
	public Date getQLJSSJ() {
		return super.getQLJSSJ();
	}

	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "djjg")
	public String getDJJG() {
		return super.getDJJG();
	}

	@Override
	@Column(name = "dbr")
	public String getDBR() {
		return super.getDBR();
	}

	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
	}

	@Override
	@Column(name = "qszt")
	public Integer getQSZT() {
		return super.getQSZT();
	}

	private String qsztname;

	@Transient
	public String getQSZTName() {
		if (qsztname == null) {
			if (this.getQSZT() != null) {
				qsztname = ConstHelper.getNameByValue("QSZT", this.getQSZT()
						.toString());
			}
		}
		return qsztname;
	}

	@Override
	@Column(name = "qllxmc")
	public String getQLLXMC() {
		return super.getQLLXMC();
	}

	@Override
	@Column(name = "zsewm")
	public Byte[] getZSEWM() {
		return super.getZSEWM();
	}

	@Override
	@Column(name = "zsbh")
	public String getZSBH() {
		return super.getZSBH();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "czfs")
	public String getCZFS() {
		return super.getCZFS();
	}

	private String czfsname;

	@Transient
	public String getCZFSName() {
		if (czfsname == null) {
			if (this.getCZFS() != null) {
				czfsname = ConstHelper.getNameByValue("CZFS", this.getCZFS());
			}
		}
		return czfsname;
	}

	@Override
	@Column(name = "zsbs")
	public String getZSBS() {
		return super.getZSBS();
	}

	private String zsbsname;

	@Transient
	public String getZSBSName() {
		if (zsbsname == null) {
			if (this.getZSBS() != null) {
				zsbsname = ConstHelper.getNameByValue("ZSBS", this.getZSBS());
			}
		}
		return zsbsname;
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	@Override
	@Column(name = "qdjg")
	public Double getQDJG() {
		return super.getQDJG();
	}

	@Override
	@Column(name = "lyqlid")
	public String getLYQLID() {
		return super.getLYQLID();
	}

	
	@Override
	@Column(name = "tdshyqr")
	public String getTDSHYQR() {
		return super.getTDSHYQR();
	}
	
	@Override
	@Column(name = "djzt")
	public String getDJZT() {
		return super.getDJZT();
	}

	private String djztname;

	@Transient
	public String getDJZTName() {
		if (djztname == null) {
			if (this.getDJZT() != null) {
				djztname = ConstHelper.getNameByValue("DJZT", this.getDJZT());
			} else {
				return "未登记";
			}
		}
		return djztname;
	}
	@Override
	@Column(name = "groupid")
	public Integer getGROUPID() {
		return super.getGROUPID();
	}

	@Override
	public String getCASENUM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCASENUM(String cASENUM) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getISCANCEL() {
		return null;
	}

	@Override
	public void setISCANCEL(String iSCANCEL) {
	}
	
	@Override
	public String getHTH() {
		return null;
	}

	@Override
	public void setHTH(String hTH) {
	}
	
	@Override
	public String getMSR() {
		return null;
	}

	@Override
	public void setMSR(String mSR) {
	}
	
	
	@Transient
	public String getISPARTIAL() {
		return null;
	}

	@Transient
	public void setISPARTIAL(String iSPARTIAL) {
	}
	

	@Override
	public String getARCHIVES_BOOKNO() {
		return null;
	}

	@Override
	public void setARCHIVES_BOOKNO(String aRCHIVES_BOOKNO) {
	}

	@Transient
	public String getMAINQLID() {
		return null;
	}

	@Transient
	public String getQS() {
		return null;
	}

	@Transient
	public void setQS(String qS) {
	}

	@Transient
	public String getBDCDYID() {
		return null;
	}

	@Transient
	public void setBDCDYID(String bDCDYID) {
	}
	
	@Transient
	public String getGYRQK() {
		return null;
	}

	@Transient
	public void setGYRQK(String gYRQK) {
	}
}
