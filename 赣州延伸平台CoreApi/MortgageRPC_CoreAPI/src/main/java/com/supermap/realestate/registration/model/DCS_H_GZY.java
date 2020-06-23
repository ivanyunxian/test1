package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/11/8 
//* ----------------------------------------
//* Public Entity bdcs_h_gzy 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_H_GZY;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

@Entity
@Table(name = "bdcs_h_gzy", schema = "bdcdck")
public class DCS_H_GZY extends GenerateDCS_H_GZY implements House{

	@Override
	@Id
	@Column(name = "bdcdyid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
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
	@Column(name = "fwbm")
	public String getFWBM() {
		return super.getFWBM();
	}

	@Override
	@Column(name = "zrzbdcdyid")
	public String getZRZBDCDYID() {
		return super.getZRZBDCDYID();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "zdbdcdyid")
	public String getZDBDCDYID() {
		return super.getZDBDCDYID();
	}

	@Override
	@Column(name = "zrzh")
	public String getZRZH() {
		return super.getZRZH();
	}

	@Override
	@Column(name = "ljzid")
	public String getLJZID() {
		return super.getLJZID();
	}

	@Override
	@Column(name = "ljzh")
	public String getLJZH() {
		return super.getLJZH();
	}

	@Override
	@Column(name = "cid")
	public String getCID() {
		return super.getCID();
	}

	@Override
	@Column(name = "ch")
	public String getCH() {
		return super.getCH();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "mjdw")
	public String getMJDW() {
		return super.getMJDW();
	}

	@Override
	@Column(name = "qsc")
	public Double getQSC() {
		return super.getQSC();
	}

	@Override
	@Column(name = "szc")
	public String getSZC() {
		return super.getSZC();
	}

	@Override
	@Column(name = "zcs")
	public Integer getZCS() {
		return super.getZCS();
	}

	@Override
	@Column(name = "hh")
	public Integer getHH() {
		return super.getHH();
	}

	@Override
	@Column(name = "shbw")
	public String getSHBW() {
		return super.getSHBW();
	}

	@Override
	@Column(name = "hx")
	public String getHX() {
		return super.getHX();
	}

	@Override
	@Column(name = "hxjg")
	public String getHXJG() {
		return super.getHXJG();
	}

	@Override
	@Column(name = "fwyt1")
	public String getFWYT1() {
		return super.getFWYT1();
	}

	@Override
	@Column(name = "fwyt2")
	public String getFWYT2() {
		return super.getFWYT2();
	}

	@Override
	@Column(name = "fwyt3")
	public String getFWYT3() {
		return super.getFWYT3();
	}

	@Override
	@Column(name = "ycjzmj")
	public Double getYCJZMJ() {
		return super.getYCJZMJ();
	}

	@Override
	@Column(name = "yctnjzmj")
	public Double getYCTNJZMJ() {
		return super.getYCTNJZMJ();
	}

	@Override
	@Column(name = "ycftjzmj")
	public Double getYCFTJZMJ() {
		return super.getYCFTJZMJ();
	}

	@Override
	@Column(name = "ycdxbfjzmj")
	public Double getYCDXBFJZMJ() {
		return super.getYCDXBFJZMJ();
	}

	@Override
	@Column(name = "ycqtjzmj")
	public Double getYCQTJZMJ() {
		return super.getYCQTJZMJ();
	}

	@Override
	@Column(name = "ycftxs")
	public Double getYCFTXS() {
		return super.getYCFTXS();
	}

	@Override
	@Column(name = "scjzmj")
	public Double getSCJZMJ() {
		return super.getSCJZMJ();
	}

	@Override
	@Column(name = "sctnjzmj")
	public Double getSCTNJZMJ() {
		return super.getSCTNJZMJ();
	}

	@Override
	@Column(name = "scftjzmj")
	public Double getSCFTJZMJ() {
		return super.getSCFTJZMJ();
	}

	@Override
	@Column(name = "scdxbfjzmj")
	public Double getSCDXBFJZMJ() {
		return super.getSCDXBFJZMJ();
	}

	@Override
	@Column(name = "scqtjzmj")
	public Double getSCQTJZMJ() {
		return super.getSCQTJZMJ();
	}

	@Override
	@Column(name = "scftxs")
	public Double getSCFTXS() {
		return super.getSCFTXS();
	}

	@Override
	@Column(name = "gytdmj")
	public Double getGYTDMJ() {
		return super.getGYTDMJ();
	}

	@Override
	@Column(name = "fttdmj")
	public Double getFTTDMJ() {
		return super.getFTTDMJ();
	}

	@Override
	@Column(name = "dytdmj")
	public Double getDYTDMJ() {
		return super.getDYTDMJ();
	}

	@Override
	@Column(name = "tdsyqr")
	public String getTDSYQR() {
		return super.getTDSYQR();
	}

	@Override
	@Column(name = "fdcjyjg")
	public Double getFDCJYJG() {
		return super.getFDCJYJG();
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
	@Column(name = "fwjg1")
	public String getFWJG1() {
		return super.getFWJG1();
	}
	
	@Override
	@Column(name = "fwjg2")
	public String getFWJG2() {
		return super.getFWJG2();
	}
	
	@Override
	@Column(name = "fwjg3")
	public String getFWJG3() {
		return super.getFWJG3();
	}

	@Override
	@Column(name = "jgsj")
	public Date getJGSJ() {
		return super.getJGSJ();
	}

	@Override
	@Column(name = "fwlx")
	public String getFWLX() {
		return super.getFWLX();
	}

	@Override
	@Column(name = "fwxz")
	public String getFWXZ() {
		return super.getFWXZ();
	}

	@Override
	@Column(name = "zdmj")
	public Double getZDMJ() {
		return super.getZDMJ();
	}

	@Override
	@Column(name = "symj")
	public Double getSYMJ() {
		return super.getSYMJ();
	}

	@Override
	@Column(name = "cqly")
	public String getCQLY() {
		return super.getCQLY();
	}

	@Override
	@Column(name = "qtgsd")
	public String getQTGSD() {
		return super.getQTGSD();
	}

	@Override
	@Column(name = "qtgsx")
	public String getQTGSX() {
		return super.getQTGSX();
	}

	@Override
	@Column(name = "qtgsn")
	public String getQTGSN() {
		return super.getQTGSN();
	}

	@Override
	@Column(name = "qtgsb")
	public String getQTGSB() {
		return super.getQTGSB();
	}

	@Override
	@Column(name = "fcfht")
	public String getFCFHT() {
		return super.getFCFHT();
	}

	@Override
	@Column(name = "zt")
	public Integer getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "qxmc")
	public String getQXMC() {
		return super.getQXMC();
	}

	@Override
	@Column(name = "djqdm")
	public String getDJQDM() {
		return super.getDJQDM();
	}

	@Override
	@Column(name = "djqmc")
	public String getDJQMC() {
		return super.getDJQMC();
	}

	@Override
	@Column(name = "djzqdm")
	public String getDJZQDM() {
		return super.getDJZQDM();
	}

	@Override
	@Column(name = "djzqmc")
	public String getDJZQMC() {
		return super.getDJZQMC();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "cqzt")
	public String getCQZT() {
		return super.getCQZT();
	}

	@Override
	@Column(name = "dyzt")
	public String getDYZT() {
		return super.getDYZT();
	}

	@Override
	@Column(name = "xzzt")
	public String getXZZT() {
		return super.getXZZT();
	}

	@Override
	@Column(name = "blzt")
	public String getBLZT() {
		return super.getBLZT();
	}

	@Override
	@Column(name = "yyzt")
	public String getYYZT() {
		return super.getYYZT();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "fh")
	public String getFH() {
		return super.getFH();
	}

	@Override
	@Column(name = "djzt")
	public String getDJZT() {
		return super.getDJZT();
	}

	@Override
	@Column(name = "bgzt")
	public String getBGZT() {
		return super.getBGZT();
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
	@Column(name = "fcfhtwj")
	public Byte[] getFCFHTWJ() {
		return super.getFCFHTWJ();
	}

	@Override
	@Column(name = "yfwxz")
	public String getYFWXZ() {
		return super.getYFWXZ();
	}

	@Override
	@Column(name = "yfwyt")
	public String getYFWYT() {
		return super.getYFWYT();
	}

	@Override
	@Column(name = "yghyt")
	public String getYGHYT() {
		return super.getYGHYT();
	}

	@Override
	@Column(name = "yfwjg")
	public String getYFWJG() {
		return super.getYFWJG();
	}

	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}

	@Override
	@Column(name = "yzl")
	public String getYZL() {
		return super.getYZL();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "fwcb")
	public String getFWCB() {
		return super.getFWCB();
	}

	@Override
	@Column(name = "gzwlx")
	public String getGZWLX() {
		return super.getGZWLX();
	}

	@Override
	@Column(name = "yfwcb")
	public String getYFWCB() {
		return super.getYFWCB();
	}

	@Override
	@Column(name = "pactno")
	public String getPACTNO() {
		return super.getPACTNO();
	}

	@Override
	@Column(name = "xmzl")
	public String getXMZL() {
		return super.getXMZL();
	}

	@Override
	@Column(name = "sfljqpjyc")
	public Integer getSFLJQPJYC() {
		return super.getSFLJQPJYC();
	}

	@Override
	@Column(name = "qlxz")
	public String getQLXZ() {
		return super.getQLXZ();
	}

	@Override
	@Column(name = "tdsyqqsrq")
	public Date getTDSYQQSRQ() {
		return super.getTDSYQQSRQ();
	}

	@Override
	@Column(name = "tdsynx")
	public Integer getTDSYNX() {
		return super.getTDSYNX();
	}

	@Override
	@Column(name = "tdsyqzzrq")
	public Date getTDSYQZZRQ() {
		return super.getTDSYQZZRQ();
	}

	@Override
	@Column(name = "fcfhtsltx")
	public String getFCFHTSLTX() {
		return super.getFCFHTSLTX();
	}

	@Override
	@Column(name = "crjje")
	public Double getCRJJE() {
		return super.getCRJJE();
	}

	@Override
	@Column(name = "fwtdyt")
	public String getFWTDYT() {
		return super.getFWTDYT();
	}

	@Override
	@Column(name = "zzc")
	public Double getZZC() {
		return super.getZZC();
	}

	@Override
	@Column(name = "dyh")
	public String getDYH() {
		return super.getDYH();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "nbdcdyh")
	public String getNBDCDYH() {
		return super.getNBDCDYH();
	}

	@Override
	@Column(name = "fcfhtwjlx")
	public String getFCFHTWJLX() {
		return super.getFCFHTWJLX();
	}

	@Override
	@Column(name = "dcxmbh")
	public String getDCXMBH() {
		return super.getDCXMBH();
	}

	private String fwcbname;
	@Transient
	public String getFWCBName() {
		if (fwcbname == null) {
			if (this.getFWCB() != null) {
				fwcbname = ConstHelper.getNameByValue("FWCB", this.getFWCB());
			}
		}
		return fwcbname;
	}
	
	@Override
	@Column(name = "SEARCHSTATE")
	public String getSEARCHSTATE() {
		return super.getSEARCHSTATE();
	}
	/******************* 自定义部分 ****************/
	@Transient
	public BDCDYLX getBDCDYLX() {
		return BDCDYLX.H;
	}

	@Transient
	public DJDYLY getLY() {
		return DJDYLY.DC;
	}
	

	@Transient
	public double getMJ() {
		double d = 0;
		if (super.getYCJZMJ() != null)
			d = super.getYCJZMJ();
		return d;
	}
	
	/**
	 * 石家庄地下层数信息
	 */
	private Integer dxcs_zrz;
  @Transient
    public Integer getDxcs_zrz() {
		return dxcs_zrz;
	}
	public void setDxcs_zrz(Integer dxcs_zrz) {
		this.dxcs_zrz = dxcs_zrz;
	}
	/**
	 * 石家庄地上层数信息
	 */
	private Integer dscs_zrz;
	 @Transient
	public Integer getDscs_zrz() {
		return dscs_zrz;
	}

	public void setDscs_zrz(Integer dscs_zrz) {
		this.dscs_zrz = dscs_zrz;
	}

	private String ghytname;
	@Transient
	public String getGHYTName() {
		if (ghytname == null) {
			if (this.getGHYT() != null) {
				ghytname = ConstHelper.getNameByValue("FWYT", this.getGHYT());
			}
		}
		return ghytname;
	}
	
	@Override
	@Column(name = "yzb")
	public Double getYZB() {
		return super.getYZB();
	}

	@Override
	@Column(name = "xzb")
	public Double getXZB() {
		return super.getXZB();
	}

	@Override
	@Column(name = "markerzt")
	public String getMARKERZT() {
		return super.getMARKERZT();
	}

	@Override
	@Column(name = "markerztmc")
	public String getMARKERZTMC() {
		return super.getMARKERZTMC();
	}
	
	@Override
	@Column(name = "markersm")
	public String getMARKERSM() {
		return super.getMARKERSM();
	}
	@Override
	@Column(name = "markertime")
	public Date getMARKERTIME() {
		return super.getMARKERTIME();
	}

	@Transient
	public String getNZDBDCDYID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setNZDBDCDYID(String nZDBDCDYID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setYT(String yt) {
		// TODO Auto-generated method stub
		
	}
}
