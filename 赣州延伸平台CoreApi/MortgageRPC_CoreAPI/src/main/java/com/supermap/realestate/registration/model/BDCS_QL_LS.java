package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-25 
//* ----------------------------------------
//* Public Entity bdcs_ql_ls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QL_LS;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.DateUtil;

@Entity
@Table(name = "bdcs_ql_ls", schema = "bdck")
public class BDCS_QL_LS extends GenerateBDCS_QL_LS implements Rights{

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

	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
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

	@Override
	@Column(name = "zsbs")
	public String getZSBS() {
		return super.getZSBS();
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
			}else{return "未登记";}
		}
		return djztname;
	}

	@Override
	@Column(name = "tdzh")
	public String getTDZH() {
		return super.getTDZH();
	}

	@Override
	@Column(name = "tdzhxh")
	public String getTDZHXH() {
		return super.getTDZHXH();
	}

	@Override
	@Column(name = "tdshyqr")
	public String getTDSHYQR() {
		return super.getTDSHYQR();
	}

	@Override
	@Column(name = "pactno")
	public String getPACTNO() {
		return super.getPACTNO();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "casenum")
	public String getCASENUM() {
		return super.getCASENUM();
	}

	@Override
	@Column(name = "iscancel")
	public String getISCANCEL() {
		return super.getISCANCEL();
	}

	@Override
	@Column(name = "bdcqzhxh")
	public String getBDCQZHXH() {
		return super.getBDCQZHXH();
	}
	
	@Override
	@Column(name = "ispartial")
	public String getISPARTIAL() {
		return super.getISPARTIAL();
	}
	
	@Override
	@Column(name = "archives_classno")
	public String getARCHIVES_CLASSNO() {
		return super.getARCHIVES_CLASSNO();
	}
	
	@Override
	@Column(name = "archives_bookno")
	public String getARCHIVES_BOOKNO() {
		return super.getARCHIVES_BOOKNO();
	}
	
	@Override
	@Column(name = "hth")
	public String getHTH() {
		return super.getHTH();
	}
	
	@Override
	@Column(name = "msr")
	public String getMSR() {
		return super.getMSR();
	}
	
	@Override
	@Column(name = "tdzsrelationid")
	public String getTDZSRELATIONID() {
		return super.getTDZSRELATIONID();
	}

	@Override
	@Column(name = "tdcasenum")
	public String getTDCASENUM() {
		return super.getTDCASENUM();
	}
	
	/**
	 * 鐧昏绫诲瀷鍚嶇О
	 */
	private String djlxname;
	@Transient
	public String getDJLXName(){
		if (djlxname == null) {
			if (this.getDJLX() != null) {
				djlxname = ConstHelper.getNameByValue("DJLX", this.getDJLX());
			} else {
				return "";
			}
		}
		return djlxname;
		
	}
	
	/**
	 * 鐧昏绫诲瀷鍚嶇О
	 */
	private String czfsxname;
	@Transient
	public String getCZFSName(){
		if (czfsxname == null) {
			if (this.getCZFS() != null) {
				czfsxname = ConstHelper.getNameByValue("CZFS", this.getCZFS());
			} else {
				return "";
			}
		}
		return czfsxname;
		
	}
	
	/**
	 * 鐧昏绫诲瀷鍚嶇О
	 */
	private String zsbsname;
	@Transient
	public String getZSBSName(){
		if (zsbsname == null) {
			if (this.getZSBS() != null) {
				zsbsname = ConstHelper.getNameByValue("ZSBS", this.getZSBS());
			} else {
				return "";
			}
		}
		return zsbsname;
		
	}
	
	/**
	 * 鏉冨埄绫诲瀷鍚嶇О
	 */
	private String qllxname;
	@Transient
	public String getQLLXName(){
		if (qllxname == null) {
			if (this.getQLLX() != null) {
				qllxname = ConstHelper.getNameByValue("QLLX", this.getQLLX());
			} else {
				return "";
			}
		}
		return qllxname;
		
	}
	
	
	private String djsjdate;
	@Transient
	public String getdjsjdate(){
		//getDJSJ()
		if (djsjdate == null) {
			if (this.getDJSJ() != null) {
				djsjdate =DateUtil.getDate(this.getDJSJ());
			} else {
				return "";
			}
		}
		return djsjdate;
	}
	
	@Override
	@Column(name = "mainqlid")
	public String getMAINQLID() {
		return super.getMAINQLID();
	}
	
	@Override
	@Column(name = "qs")
	public String getQS() {
		return super.getQS();
	}
	
	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}
	@Override
	@Column(name = "groupid")
	public Integer getGROUPID() {
		return super.getGROUPID();
	}
	@Override
	@Column(name = "gydbdcdyid")
	public String getGYDBDCDYID() {
		return super.getGYDBDCDYID();
	}

	@Override
	@Column(name = "gydbdcdylx")
	public String getGYDBDCDYLX() {
		return super.getGYDBDCDYLX();
	}
	
	@Override
	@Column(name = "gyrqk")
	public String getGYRQK() {
		return super.getGYRQK();
	}
}
