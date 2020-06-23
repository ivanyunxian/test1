package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/8 
//* ----------------------------------------
//* Public Entity bdcs_dyxz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DYXZ;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

@Entity
@Table(name = "bdcs_dyxz", schema = "bdck")
public class BDCS_DYXZ extends GenerateBDCS_DYXZ {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "bdcdylx")
	public String getBDCDYLX() {
		return super.getBDCDYLX();
	}

	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
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
	@Column(name = "bxzrmc")
	public String getBXZRMC() {
		return super.getBXZRMC();
	}

	@Override
	@Column(name = "bxzrzjzl")
	public String getBXZRZJZL() {
		return super.getBXZRZJZL();
	}

	@Override
	@Column(name = "bxzrzjhm")
	public String getBXZRZJHM() {
		return super.getBXZRZJHM();
	}

	@Override
	@Column(name = "xzwjhm")
	public String getXZWJHM() {
		return super.getXZWJHM();
	}

	@Override
	@Column(name = "xzdw")
	public String getXZDW() {
		return super.getXZDW();
	}

	@Override
	@Column(name = "slr")
	public String getSLR() {
		return super.getSLR();
	}

	@Override
	@Column(name = "slryj")
	public String getSLRYJ() {
		return super.getSLRYJ();
	}

	@Override
	@Column(name = "xzlx")
	public String getXZLX() {
		return super.getXZLX();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "lsxz")
	public String getLSXZ() {
		return super.getLSXZ();
	}

	@Override
	@Column(name = "xzfw")
	public String getXZFW() {
		return super.getXZFW();
	}

	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}

	@Override
	@Column(name = "dbr")
	public String getDBR() {
		return super.getDBR();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "zxdjsj")
	public Date getZXDJSJ() {
		return super.getZXDJSJ();
	}

	@Override
	@Column(name = "zxdbr")
	public String getZXDBR() {
		return super.getZXDBR();
	}

	@Override
	@Column(name = "zxywh")
	public String getZXYWH() {
		return super.getZXYWH();
	}

	@Override
	@Column(name = "zxbz")
	public String getZXBZ() {
		return super.getZXBZ();
	}

	@Override
	@Column(name = "zxyj")
	public String getZXYJ() {
		return super.getZXYJ();
	}

	@Override
	@Column(name = "zxxzwjhm")
	public String getZXXZWJHM() {
		return super.getZXXZWJHM();
	}

	@Override
	@Column(name = "zxxzdw")
	public String getZXXZDW() {
		return super.getZXXZDW();
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
	@Column(name = "sdtzrq")
	@Temporal(TemporalType.DATE)
	public Date getSDTZRQ() {
		return super.getSDTZRQ();
	}

	@Override
	@Column(name = "xzqsrq")
	@Temporal(TemporalType.DATE)
	public Date getXZQSRQ() {
		return super.getXZQSRQ();
	}
	
	@Override
	@Column(name = "xzzzrq")
	@Temporal(TemporalType.DATE)
	public Date getXZZZRQ() {
		return super.getXZZZRQ();
	}
	
	private String zxdjsjname;
	@Transient
	public String getZXDJSJName() {
		if (zxdjsjname == null) {
			if (this.getZXDJSJ() != null) {
				zxdjsjname = StringHelper.FormatYmdhmsByDate(this.getZXDJSJ());
			}
		}
		return zxdjsjname;
	}
	
	private String bxzrzjzlname;
	@Transient
	public String getBXZRZJZLName() {
		if (bxzrzjzlname == null) {
			if (this.getBXZRZJZL() != null) {
				bxzrzjzlname = ConstHelper.getNameByValue("ZJLX", this.getBXZRZJZL());
			}
		}
		return bxzrzjzlname;
	}
	
	private String sdtzrqname;
	@Transient
	public String getSDTZRQName() {
		if (sdtzrqname == null) {
			if (this.getSDTZRQ() != null) {
				sdtzrqname = StringHelper.FormatByDatetime(this.getSDTZRQ());
			}
		}
		return sdtzrqname;
	}
	
	private String xzqsrqname;
	@Transient
	public String getXZQSRQName() {
		if (xzqsrqname == null) {
			if (this.getXZQSRQ() != null) {
				xzqsrqname = StringHelper.FormatByDatetime(this.getXZQSRQ());
			}
		}
		return xzqsrqname;
	}
	
	private String xzzzrqname;
	@Transient
	public String getXZZZRQName() {
		if (xzzzrqname == null) {
			if (this.getXZZZRQ() != null) {
				xzzzrqname = StringHelper.FormatByDatetime(this.getXZZZRQ());
			}
		}
		return xzzzrqname;
	}
	
	private String xzlxname;
	@Transient
	public String getXZLXName() {
		if (xzlxname == null) {
			if (this.getXZLX() != null) {
				xzlxname = ConstHelper.getNameByValue("XZLX", this.getXZLX());
			}
		}
		return xzlxname;
	}
	
	private String djsjname;
	@Transient
	public String getDJSJName() {
		if (djsjname == null) {
			if (this.getDJSJ() != null) {
				djsjname = StringHelper.FormatYmdhmsByDate(this.getDJSJ());
			}
		}
		return djsjname;
	}
	
	private String ywlsh;
	@Transient
	public String getYWLSH() {
		return ywlsh;
	}

	public void setYWLSH(String ywlsh) {
		this.ywlsh = ywlsh;
	}
	
	private String zxywlsh;
	@Transient
	public String getZXYWLSH() {
		return zxywlsh;
	}
	
	public void setZXYWLSH(String zxywlsh) {
		this.zxywlsh = zxywlsh;
	}
}
