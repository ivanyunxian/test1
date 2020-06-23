package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-24 
//* ----------------------------------------
//* Public Entity bdcs_fsql_gz 
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

import com.supermap.realestate.registration.model.genrt.GenerateDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_fsql_gz", schema = "bdcdck")
public class DCS_FSQL_GZ extends GenerateDCS_FSQL_GZ implements SubRights {

	@Override
	@Id
	@Column(name = "fsqlid", length = 50)
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
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "nydmj")
	public Double getNYDMJ() {
		return super.getNYDMJ();
	}

	@Override
	@Column(name = "gdmj")
	public Double getGDMJ() {
		return super.getGDMJ();
	}

	@Override
	@Column(name = "ldmj")
	public Double getLDMJ() {
		return super.getLDMJ();
	}

	@Override
	@Column(name = "cdmj")
	public Double getCDMJ() {
		return super.getCDMJ();
	}
	
	@Override
	@Column(name = "dypgjz")
	public Double getDYPGJZ() {
		return super.getDYPGJZ();
	}

	@Override
	@Column(name = "qtnydmj")
	public Double getQTNYDMJ() {
		return super.getQTNYDMJ();
	}

	@Override
	@Column(name = "jsydmj")
	public Double getJSYDMJ() {
		return super.getJSYDMJ();
	}

	@Override
	@Column(name = "wlydmj")
	public Double getWLYDMJ() {
		return super.getWLYDMJ();
	}

	@Override
	@Column(name = "syqmj")
	public Double getSYQMJ() {
		return super.getSYQMJ();
	}

	@Override
	@Column(name = "syjze")
	public Double getSYJZE() {
		return super.getSYJZE();
	}

	@Override
	@Column(name = "syjbzyj")
	public String getSYJBZYJ() {
		return super.getSYJBZYJ();
	}

	@Override
	@Column(name = "syjjnqk")
	public String getSYJJNQK() {
		return super.getSYJJNQK();
	}

	@Override
	@Column(name = "hymjdw")
	public String getHYMJDW() {
		return super.getHYMJDW();
	}

	@Override
	@Column(name = "hymj")
	public Double getHYMJ() {
		return super.getHYMJ();
	}

	@Override
	@Column(name = "jzmj")
	public Double getJZMJ() {
		return super.getJZMJ();
	}

	@Override
	@Column(name = "fdzl")
	public String getFDZL() {
		return super.getFDZL();
	}

	@Override
	@Column(name = "tdsyqr")
	public String getTDSYQR() {
		return super.getTDSYQR();
	}

	@Override
	@Column(name = "dytdmj")
	public Double getDYTDMJ() {
		return super.getDYTDMJ();
	}

	@Override
	@Column(name = "fttdmj")
	public Double getFTTDMJ() {
		return super.getFTTDMJ();
	}

	@Override
	@Column(name = "fdcjyjg")
	public Double getFDCJYJG() {
		return super.getFDCJYJG();
	}

	@Override
	@Column(name = "jgsj")
	public Date getJGSJ() {
		return super.getJGSJ();
	}

	@Override
	@Column(name = "zh")
	public String getZH() {
		return super.getZH();
	}

	@Override
	@Column(name = "zcs")
	public Integer getZCS() {
		return super.getZCS();
	}

	@Override
	@Column(name = "ghyt")
	public String getGHYT() {
		return super.getGHYT();
	}

	@Override
	@Column(name = "fwjg")
	public String getFWJG() {
		return super.getFWJG();
	}

	@Override
	@Column(name = "fwxz")
	public String getFWXZ() {
		return super.getFWXZ();
	}

	@Override
	@Column(name = "szc")
	public Integer getSZC() {
		return super.getSZC();
	}

	@Override
	@Column(name = "zyjzmj")
	public Double getZYJZMJ() {
		return super.getZYJZMJ();
	}

	@Override
	@Column(name = "ftjzmj")
	public Double getFTJZMJ() {
		return super.getFTJZMJ();
	}

	@Override
	@Column(name = "lmmj")
	public Double getLMMJ() {
		return super.getLMMJ();
	}

	@Override
	@Column(name = "fbf")
	public String getFBF() {
		return super.getFBF();
	}

	@Override
	@Column(name = "zs")
	public String getZS() {
		return super.getZS();
	}

	@Override
	@Column(name = "lz")
	public String getLZ() {
		return super.getLZ();
	}

	@Override
	@Column(name = "zlnd")
	public String getZLND() {
		return super.getZLND();
	}

	@Override
	@Column(name = "xdm")
	public String getXDM() {
		return super.getXDM();
	}

	@Override
	@Column(name = "gydr")
	public String getGYDR() {
		return super.getGYDR();
	}

	@Override
	@Column(name = "dyqnr")
	public String getDYQNR() {
		return super.getDYQNR();
	}

	@Override
	@Column(name = "dywlx")
	public String getDYWLX() {
		return super.getDYWLX();
	}

    private String dywlxname;
	@Transient
	public String getDYWLXName() {
		if (dywlxname == null) {
			if (this.getDYWLX() != null) {
				dywlxname = ConstHelper.getNameByValue("DYBDCLX", this.getDYWLX());
			}
		}
		return dywlxname;
	}

	@Override
	@Column(name = "dysw")
	public Integer getDYSW() {
		return super.getDYSW();
	}

	@Override
	@Column(name = "grdr1")
	public String getGRDR1() {
		return super.getGRDR1();
	}

	@Override
	@Column(name = "dyqnr1")
	public String getDYQNR1() {
		return super.getDYQNR1();
	}

	@Override
	@Column(name = "scyqmj")
	public Double getSCYQMJ() {
		return super.getSCYQMJ();
	}

	@Override
	@Column(name = "dyfs")
	public String getDYFS() {
		return super.getDYFS();
	}

    private String dyfsname;
	@Transient
	public String getDYFSName() {
		if (dyfsname == null) {
			if (this.getDYFS() != null) {
				dyfsname = ConstHelper.getNameByValue("DYFS", this.getDYFS());
			}
		}
		return dyfsname;
	}

	@Override
	@Column(name = "zjgcjd")
	public String getZJGCJD() {
		return super.getZJGCJD();
	}

	@Override
	@Column(name = "bdbzzqse")
	public Double getBDBZZQSE() {
		return super.getBDBZZQSE();
	}

	@Override
	@Column(name = "dbfw")
	public String getDBFW() {
		return super.getDBFW();
	}

	@Override
	@Column(name = "zgzqqdss")
	public String getZGZQQDSS() {
		return super.getZGZQQDSS();
	}

	@Override
	@Column(name = "zgzqse")
	public Double getZGZQSE() {
		return super.getZGZQSE();
	}

	@Override
	@Column(name = "ygdjzl")
	public String getYGDJZL() {
		return super.getYGDJZL();
	}

	@Override
	@Column(name = "ywr")
	public String getYWR() {
		return super.getYWR();
	}

	@Override
	@Column(name = "ywrzjzl")
	public String getYWRZJZL() {
		return super.getYWRZJZL();
	}

	@Override
	@Column(name = "yysx")
	public String getYYSX() {
		return super.getYYSX();
	}

	@Override
	@Column(name = "zxyyyy")
	public String getZXYYYY() {
		return super.getZXYYYY();
	}

	@Override
	@Column(name = "cflx")
	public String getCFLX() {
		return super.getCFLX();
	}

	@Override
	@Column(name = "lhsx")
	public Integer getLHSX() {
		return super.getLHSX();
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cfsj")
	public Date getCFSJ() {
		return super.getCFSJ();
	}

	@Override
	@Column(name = "cfjg")
	public String getCFJG() {
		return super.getCFJG();
	}

	@Override
	@Column(name = "cfwh")
	public String getCFWH() {
		return super.getCFWH();
	}

	@Override
	@Column(name = "cfwj")
	public String getCFWJ() {
		return super.getCFWJ();
	}

	@Override
	@Column(name = "cffw")
	public String getCFFW() {
		return super.getCFFW();
	}

	@Override
	@Column(name = "jfjg")
	public String getJFJG() {
		return super.getJFJG();
	}

	@Override
	@Column(name = "jfwh")
	public String getJFWH() {
		return super.getJFWH();
	}

	@Override
	@Column(name = "jfwj")
	public String getJFWJ() {
		return super.getJFWJ();
	}

	@Override
	@Column(name = "zysz")
	public String getZYSZ() {
		return super.getZYSZ();
	}

	@Override
	@Column(name = "zwrzjh")
	public String getZWRZJH() {
		return super.getZWRZJH();
	}

	@Override
	@Column(name = "dbrzjh")
	public String getDBRZJH() {
		return super.getDBRZJH();
	}

	@Override
	@Column(name = "htbh")
	public String getHTBH() {
		return super.getHTBH();
	}

	@Override
	@Column(name = "dymj")
	public Double getDYMJ() {
		return super.getDYMJ();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "zxdyywh")
	public String getZXDYYWH() {
		return super.getZXDYYWH();
	}

	@Override
	@Column(name = "zxdyyy")
	public String getZXDYYY() {
		return super.getZXDYYY();
	}

	@Override
	@Column(name = "zxsj")
	public Date getZXSJ() {
		return super.getZXSJ();
	}

	@Override
	@Column(name = "jgzwbh")
	public String getJGZWBH() {
		return super.getJGZWBH();
	}

	@Override
	@Column(name = "jgzwmc")
	public String getJGZWMC() {
		return super.getJGZWMC();
	}

	@Override
	@Column(name = "jgzwsl")
	public Integer getJGZWSL() {
		return super.getJGZWSL();
	}

	@Override
	@Column(name = "jgzwmj")
	public Double getJGZWMJ() {
		return super.getJGZWMJ();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "tdhysyqr")
	public String getTDHYSYQR() {
		return super.getTDHYSYQR();
	}

	@Override
	@Column(name = "tdhysymj")
	public Double getTDHYSYMJ() {
		return super.getTDHYSYMJ();
	}

	@Override
	@Column(name = "gjzwlx")
	public String getGJZWLX() {
		return super.getGJZWLX();
	}

	@Override
	@Column(name = "gjzwghyt")
	public String getGJZWGHYT() {
		return super.getGJZWGHYT();
	}

	@Override
	@Column(name = "gjzwmj")
	public Double getGJZWMJ() {
		return super.getGJZWMJ();
	}

	@Override
	@Column(name = "cbsyqmj")
	public Double getCBSYQMJ() {
		return super.getCBSYQMJ();
	}

	@Override
	@Column(name = "tdsyqxz")
	public String getTDSYQXZ() {
		return super.getTDSYQXZ();
	}

	@Override
	@Column(name = "syttlx")
	public String getSYTTLX() {
		return super.getSYTTLX();
	}

	@Override
	@Column(name = "yzyfs")
	public String getYZYFS() {
		return super.getYZYFS();
	}

	@Override
	@Column(name = "cyzl")
	public String getCYZL() {
		return super.getCYZL();
	}

	@Override
	@Column(name = "syzcl")
	public Integer getSYZCL() {
		return super.getSYZCL();
	}

	@Override
	@Column(name = "ldsyqxz")
	public String getLDSYQXZ() {
		return super.getLDSYQXZ();
	}

	@Override
	@Column(name = "sllmsyqr1")
	public String getSLLMSYQR1() {
		return super.getSLLMSYQR1();
	}

	@Override
	@Column(name = "sllmsyqr2")
	public String getSLLMSYQR2() {
		return super.getSLLMSYQR2();
	}

	@Override
	@Column(name = "qy")
	public String getQY() {
		return super.getQY();
	}

	@Override
	@Column(name = "lb")
	public String getLB() {
		return super.getLB();
	}

	@Override
	@Column(name = "xb")
	public String getXB() {
		return super.getXB();
	}

	@Override
	@Column(name = "qsfs")
	public String getQSFS() {
		return super.getQSFS();
	}

	@Override
	@Column(name = "sylx")
	public String getSYLX() {
		return super.getSYLX();
	}

	@Override
	@Column(name = "qsl")
	public Double getQSL() {
		return super.getQSL();
	}

	@Override
	@Column(name = "qsyt")
	public String getQSYT() {
		return super.getQSYT();
	}

	@Override
	@Column(name = "kcmj")
	public Double getKCMJ() {
		return super.getKCMJ();
	}

	@Override
	@Column(name = "kcfs")
	public String getKCFS() {
		return super.getKCFS();
	}

	@Override
	@Column(name = "kckz")
	public String getKCKZ() {
		return super.getKCKZ();
	}

	@Override
	@Column(name = "scgm")
	public String getSCGM() {
		return super.getSCGM();
	}

	@Override
	@Column(name = "xydzl")
	public String getXYDZL() {
		return super.getXYDZL();
	}

	@Override
	@Column(name = "gydqlr")
	public String getGYDQLR() {
		return super.getGYDQLR();
	}

	@Override
	@Column(name = "gydqlrzjh")
	public String getGYDQLRZJH() {
		return super.getGYDQLRZJH();
	}

	@Override
	@Column(name = "dybdclx")
	public String getDYBDCLX() {
		return super.getDYBDCLX();
	}

	@Override
	@Column(name = "zjjzwzl")
	public String getZJJZWZL() {
		return super.getZJJZWZL();
	}

	@Override
	@Column(name = "zjjzwdyfw")
	public String getZJJZWDYFW() {
		return super.getZJJZWDYFW();
	}

	@Override
	@Column(name = "bdczl")
	public String getBDCZL() {
		return super.getBDCZL();
	}

	@Override
	@Column(name = "ywrzjh")
	public String getYWRZJH() {
		return super.getYWRZJH();
	}

	@Override
	@Column(name = "qdjg")
	public Double getQDJG() {
		return super.getQDJG();
	}

	@Override
	@Column(name = "gydqlrzjzl")
	public String getGYDQLRZJZL() {
		return super.getGYDQLRZJZL();
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
	@Column(name = "zxdbr")
	public String getZXDBR() {
		return super.getZXDBR();
	}

	@Override
	@Column(name = "zxfj")
	public String getZXFJ() {
		return super.getZXFJ();
	}

	@Override
	@Column(name = "dyr")
	public String getDYR() {
		return super.getDYR();
	}
	
	@Override
	@Column(name = "casenum")
	public String getCASENUM() {
		return super.getCASENUM();
	}
	
	@Override
	@Column(name = "gyrqk")
	public String getGYRQK() {
		return super.getGYRQK();
	}
	
	@Override
	@Column(name = "zqdw")
	public String getZQDW() {
		return super.getZQDW();
	}
	
	@Override
	@Column(name = "ydzmj")
	public Double getYDZMJ() {
		return super.getYDZMJ();
	}
	
	@Override
	@Column(name = "dyydmj")
	public Double getDYYDMJ() {
		return super.getDYYDMJ();
	}
	
	@Override
	@Column(name = "zjzmj")
	public Double getZJZMJ() {
		return super.getZJZMJ();
	}
	
	@Override
	@Column(name = "dyjzmj")
	public Double getDYJZMJ() {
		return super.getDYJZMJ();
	}
	
	@Override
	@Column(name = "dytdyt")
	public String getDYTDYT() {
		return super.getDYTDYT();
	}

	@Override
	public String getWSPZH() {
		return null;
	}

	@Override
	public void setWSPZH(String wSPZH) {

	}

	@Transient
	public String getTDPGJZ() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setTDPGJZ(String tdpgjz) {
		// TODO Auto-generated method stub
		
	}
	@Transient
	public void setZWR(String zwr) {
		// TODO Auto-generated method stub
		
	}
}
