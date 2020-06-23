package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/14 
//* ----------------------------------------
//* Public Entity bdcs_xzdy 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConstHelper;
import javax.persistence.Transient;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_XZDY;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "xzdy", schema = "bdck")
public class BDCS_XZDY extends GenerateBDCS_XZDY {

	@Override
	@Id
	@Column(name = "id", length = 42)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "wxqlid")
	public String getWXQLID() {
		return super.getWXQLID();
	}

	@Override
	@Column(name = "qlnbbs")
	public Integer getQLNBBS() {
		return super.getQLNBBS();
	}

	@Override
	@Column(name = "dyzfddbr")
	public String getDYZFDDBR() {
		return super.getDYZFDDBR();
	}

	@Override
	@Column(name = "dyzlxr")
	public String getDYZLXR() {
		return super.getDYZLXR();
	}

	@Override
	@Column(name = "dyzdh")
	public String getDYZDH() {
		return super.getDYZDH();
	}

	@Override
	@Column(name = "dyztxdz")
	public String getDYZTXDZ() {
		return super.getDYZTXDZ();
	}

	@Override
	@Column(name = "dyqzfddbr")
	public String getDYQZFDDBR() {
		return super.getDYQZFDDBR();
	}

	@Override
	@Column(name = "dyqzlxr")
	public String getDYQZLXR() {
		return super.getDYQZLXR();
	}

	@Override
	@Column(name = "dyqzdh")
	public String getDYQZDH() {
		return super.getDYQZDH();
	}

	@Override
	@Column(name = "dyqztxdz")
	public String getDYQZTXDZ() {
		return super.getDYQZTXDZ();
	}

	@Override
	@Column(name = "dyzmj")
	public Double getDYZMJ() {
		return super.getDYZMJ();
	}

	@Override
	@Column(name = "dyje")
	public Double getDYJE() {
		return super.getDYJE();
	}

	@Override
	@Column(name = "kdytdzc")
	public Double getKDYTDZC() {
		return super.getKDYTDZC();
	}

	@Override
	@Column(name = "dyqx")
	public String getDYQX() {
		return super.getDYQX();
	}

	@Override
	@Column(name = "jbyj")
	public String getJBYJ() {
		return super.getJBYJ();
	}

	@Override
	@Column(name = "shr")
	public String getSHR() {
		return super.getSHR();
	}

	@Override
	@Column(name = "shrq")
	public Date getSHRQ() {
		return super.getSHRQ();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "dysx")
	public Integer getDYSX() {
		return super.getDYSX();
	}

	@Override
	@Column(name = "jbr")
	public String getJBR() {
		return super.getJBR();
	}

	@Override
	@Column(name = "jbrq")
	public Date getJBRQ() {
		return super.getJBRQ();
	}

	@Override
	@Column(name = "shyj")
	public String getSHYJ() {
		return super.getSHYJ();
	}

	@Override
	@Column(name = "tdznbbs")
	public Integer getTDZNBBS() {
		return super.getTDZNBBS();
	}

	@Override
	@Column(name = "dyr")
	public String getDYR() {
		return super.getDYR();
	}

	@Override
	@Column(name = "dyqr")
	public String getDYQR() {
		return super.getDYQR();
	}

	@Override
	@Column(name = "kssj")
	public Date getKSSJ() {
		return super.getKSSJ();
	}

	@Override
	@Column(name = "jssj")
	public Date getJSSJ() {
		return super.getJSSJ();
	}

	@Override
	@Column(name = "tdz")
	public String getTDZ() {
		return super.getTDZ();
	}

	@Override
	@Column(name = "txzh")
	public String getTXZH() {
		return super.getTXZH();
	}

	@Override
	@Column(name = "blrq")
	public String getBLRQ() {
		return super.getBLRQ();
	}

	@Override
	@Column(name = "iszx")
	public Integer getISZX() {
		return super.getISZX();
	}

	@Override
	@Column(name = "manualinsert")
	public Integer getMANUALINSERT() {
		return super.getMANUALINSERT();
	}

	@Override
	@Column(name = "ajbh")
	public String getAJBH() {
		return super.getAJBH();
	}

	@Override
	@Column(name = "tdyt")
	public String getTDYT() {
		return super.getTDYT();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "djh")
	public String getDJH() {
		return super.getDJH();
	}

	@Override
	@Column(name = "jyajbh")
	public String getJYAJBH() {
		return super.getJYAJBH();
	}

	@Override
	@Column(name = "th")
	public String getTH() {
		return super.getTH();
	}

	@Override
	@Column(name = "jzmj")
	public Double getJZMJ() {
		return super.getJZMJ();
	}

	@Override
	@Column(name = "zzrq")
	public String getZZRQ() {
		return super.getZZRQ();
	}

	@Override
	@Column(name = "tdsyz")
	public String getTDSYZ() {
		return super.getTDSYZ();
	}

	@Override
	@Column(name = "dyzxsj")
	public Date getDYZXSJ() {
		return super.getDYZXSJ();
	}

	@Override
	@Column(name = "jyxmbh")
	public Integer getJYXMBH() {
		return super.getJYXMBH();
	}

	@Override
	@Column(name = "syqlx")
	public String getSYQLX() {
		return super.getSYQLX();
	}

	@Override
	@Column(name = "dyjzmj")
	public Double getDYJZMJ() {
		return super.getDYJZMJ();
	}

	@Override
	@Column(name = "pgdyjk")
	public Double getPGDYJK() {
		return super.getPGDYJK();
	}

	@Override
	@Column(name = "tdzzzrq")
	public String getTDZZZRQ() {
		return super.getTDZZZRQ();
	}

	@Override
	@Column(name = "htzzrq")
	public String getHTZZRQ() {
		return super.getHTZZRQ();
	}

	@Override
	@Column(name = "zdmj")
	public Double getZDMJ() {
		return super.getZDMJ();
	}

	@Override
	@Column(name = "yqsj")
	public String getYQSJ() {
		return super.getYQSJ();
	}

	@Override
	@Column(name = "bzxg")
	public String getBZXG() {
		return super.getBZXG();
	}

	@Override
	@Column(name = "bzjy")
	public String getBZJY() {
		return super.getBZJY();
	}

	@Override
	@Column(name = "tddyrxz")
	public String getTDDYRXZ() {
		return super.getTDDYRXZ();
	}

	@Override
	@Column(name = "zdid")
	public String getZDID() {
		return super.getZDID();
	}

	@Override
	@Column(name = "zddjh")
	public String getZDDJH() {
		return super.getZDDJH();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "zdtybm")
	public String getZDTYBM() {
		return super.getZDTYBM();
	}

	@Override
	@Column(name = "zdzdmj")
	public Double getZDZDMJ() {
		return super.getZDZDMJ();
	}

	@Override
	@Column(name = "qlr")
	public String getQLR() {
		return super.getQLR();
	}

	@Override
	@Column(name = "ytdzid")
	public String getYTDZID() {
		return super.getYTDZID();
	}

	@Override
	@Column(name = "gxdjh")
	public String getGXDJH() {
		return super.getGXDJH();
	}

	@Override
	@Column(name = "zdtdz")
	public String getZDTDZ() {
		return super.getZDTDZ();
	}

	@Override
	@Column(name = "tdzid")
	public String getTDZID() {
		return super.getTDZID();
	}

	@Override
	@Column(name = "zdbdcdyid")
	public String getZDBDCDYID() {
		return super.getZDBDCDYID();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}
}
