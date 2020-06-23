package com.supermap.yingtanothers.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月21日 上午9:33:41
 * 功能：鹰潭不动产共享中间库查封登记权属性结构表
 */
@Entity
@Table(name = "CFDJ", schema = "GXFCK")
public class CFDJ {

	
	//不动产单元号
	private String BDCDYH;
	//业务号
	private String YWH;
	//权利ID
	private String QLID;
	//A.32查封类型
	private String CFLX;
	//查封机构
	private String CFJG;
	//查封文号
	private String CFWH;
	//查封文件
	private String CFWJ;
	//查封范围
	private String CFFW;
	//解封机构
	private String JFJG;
	//解封文号
	private String JFWH;
	//解封文件
	private String JFWJ;
	//查封开始时间
	private Date CFQSSJ;
	//查封结束时间
	private Date CFJSSJ;
	//区县代码
	private String QXDM;
	//登记机构
	private String DJJG;
	//登簿人
	private String DBR;
	//登记时间
	private Date DJSJ;
	//解封业务号
	private String JFYWH;
	//解封登薄人
	private String JFDBR;
	//解封登记时间
	private Date JFDJSJ;	
	//附记
	private String FJ;	
	//权属状态
	private String QSZT;		
	//共享项目编号
	private String GXXMBH;
	//来源权利ID
	private String LYQLID;
	//关联ID
	private String RELATIONID;
	//查封登记ID
	private String CFDJID;
	
	@Column(name = "RELATIONID")
	public String getRELATIONID() {
		return RELATIONID;
	}
	public void setRELATIONID(String rELATIONID) {
		RELATIONID = rELATIONID;
	}
	@Column(name = "BDCDYH")
	public String getBDCDYH() {
		return BDCDYH;
	}
	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}
	
	@Column(name = "YWH")
	public String getYWH() {
		return YWH;
	}
	public void setYWH(String yWH) {
		YWH = yWH;
	}
	
	@Column(name = "QLID")
	public String getQLID() {
		return QLID;
	}
	public void setQLID(String qLID) {
		QLID = qLID;
	}
	
	@Column(name = "CFLX")
	public String getCFLX() {
		return CFLX;
	}
	public void setCFLX(String cFLX) {
		CFLX = cFLX;
	}
	
	@Column(name = "CFJG")
	public String getCFJG() {
		return CFJG;
	}
	public void setCFJG(String cFJG) {
		CFJG = cFJG;
	}
	
	@Column(name = "CFWH")
	public String getCFWH() {
		return CFWH;
	}
	public void setCFWH(String cFWH) {
		CFWH = cFWH;
	}
	
	@Column(name = "CFWJ")
	public String getCFWJ() {
		return CFWJ;
	}
	public void setCFWJ(String cFWJ) {
		CFWJ = cFWJ;
	}
	
	@Column(name = "CFFW")
	public String getCFFW() {
		return CFFW;
	}
	public void setCFFW(String cFFW) {
		CFFW = cFFW;
	}
	
	@Column(name = "JFJG")
	public String getJFJG() {
		return JFJG;
	}
	public void setJFJG(String jFJG) {
		JFJG = jFJG;
	}
	
	@Column(name = "JFWH")
	public String getJFWH() {
		return JFWH;
	}
	public void setJFWH(String jFWH) {
		JFWH = jFWH;
	}
	
	@Column(name = "JFWJ")
	public String getJFWJ() {
		return JFWJ;
	}
	public void setJFWJ(String jFWJ) {
		JFWJ = jFWJ;
	}
	
	@Column(name = "CFQSSJ")
	public Date getCFQSSJ() {
		return CFQSSJ;
	}
	public void setCFQSSJ(Date cFQSSJ) {
		CFQSSJ = cFQSSJ;
	}
	
	@Column(name = "CFJSSJ")
	public Date getCFJSSJ() {
		return CFJSSJ;
	}
	public void setCFJSSJ(Date cFJSSJ) {
		CFJSSJ = cFJSSJ;
	}
	
	@Column(name = "QXDM")
	public String getQXDM() {
		return QXDM;
	}
	public void setQXDM(String qXDM) {
		QXDM = qXDM;
	}
	
	@Column(name = "DJJG")
	public String getDJJG() {
		return DJJG;
	}
	public void setDJJG(String dJJG) {
		DJJG = dJJG;
	}
	
	@Column(name = "DBR")
	public String getDBR() {
		return DBR;
	}
	public void setDBR(String dBR) {
		DBR = dBR;
	}
	
	@Column(name = "DJSJ")
	public Date getDJSJ() {
		return DJSJ;
	}
	public void setDJSJ(Date dJSJ) {
		DJSJ = dJSJ;
	}
	
	@Column(name = "JFYWH")
	public String getJFYWH() {
		return JFYWH;
	}
	public void setJFYWH(String jFYWH) {
		JFYWH = jFYWH;
	}
	
	@Column(name = "JFDBR")
	public String getJFDBR() {
		return JFDBR;
	}
	public void setJFDBR(String jFDBR) {
		JFDBR = jFDBR;
	}
	
	@Column(name = "JFDJSJ")
	public Date getJFDJSJ() {
		return JFDJSJ;
	}
	public void setJFDJSJ(Date jFDJSJ) {
		JFDJSJ = jFDJSJ;
	}
	
	@Column(name = "FJ")
	public String getFJ() {
		return FJ;
	}
	public void setFJ(String fJ) {
		FJ = fJ;
	}
	
	@Column(name = "QSZT")
	public String getQSZT() {
		return QSZT;
	}
	public void setQSZT(String qSZT) {
		QSZT = qSZT;
	}
	
	@Column(name = "GXXMBH")
	public String getGXXMBH() {
		return GXXMBH;
	}
	public void setGXXMBH(String gXXMBH) {
		GXXMBH = gXXMBH;
	}
	@Id
	@Column(name = "LYQLID")
	public String getLYQLID() {
		return LYQLID;
	}
	public void setLYQLID(String lYQLID) {
		LYQLID = lYQLID;
	}
	
	
}
