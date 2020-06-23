package com.supermap.realestate.registration.model.xmlmodel.bdcql;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CFDJ")
public class CFDJ {
	private String BDCDYID;// 不动产单元ID
	private String QLID;// 权利ID
	private String BDCDYH;// 不动产单元号
	private String YWH;// 业务号
	private String CFJG;// 查封机关
	private String CFLX;// 查封类型
	private String CFLXMC;// 查封类型名称
	private String CFWJ;// 查封文件
	private String CFWH;// 查封文号
	private String CFQSSJ;// 查封起始时间
	private String CFJSSJ;// 查封结束时间
	private String CFFW;// 查封范围
	private String JFYWH;// 解封业务号
	private String JFJG;// 解封机构
	private String JFWJ;// 解封文件
	private String JFWH;// 解封文号
	private String JFDBR;// 解封登簿人
	private String JFDJSJ;// 解封登记时间
	private String FJ;// 附记
	private String QSZT;// 权属状态
	private String QSZTMC;// 权属状态名称

	public String getCFLXMC() {
		return CFLXMC;
	}

	public void setCFLXMC(String cFLXMC) {
		CFLXMC = cFLXMC;
	}

	public String getQSZTMC() {
		return QSZTMC;
	}

	public void setQSZTMC(String qSZTMC) {
		QSZTMC = qSZTMC;
	}

	public String getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		BDCDYID = bDCDYID;
	}

	public String getQLID() {
		return QLID;
	}

	public void setQLID(String qLID) {
		QLID = qLID;
	}

	public String getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}

	public String getYWH() {
		return YWH;
	}

	public void setYWH(String yWH) {
		YWH = yWH;
	}

	public String getCFJG() {
		return CFJG;
	}

	public void setCFJG(String cFJG) {
		CFJG = cFJG;
	}

	public String getCFLX() {
		return CFLX;
	}

	public void setCFLX(String cFLX) {
		CFLX = cFLX;
	}

	public String getCFWJ() {
		return CFWJ;
	}

	public void setCFWJ(String cFWJ) {
		CFWJ = cFWJ;
	}

	public String getCFWH() {
		return CFWH;
	}

	public void setCFWH(String cFWH) {
		CFWH = cFWH;
	}

	public String getCFQSSJ() {
		return CFQSSJ;
	}

	public void setCFQSSJ(String cFQSSJ) {
		CFQSSJ = cFQSSJ;
	}

	public String getCFJSSJ() {
		return CFJSSJ;
	}

	public void setCFJSSJ(String cFJSSJ) {
		CFJSSJ = cFJSSJ;
	}

	public String getCFFW() {
		return CFFW;
	}

	public void setCFFW(String cFFW) {
		CFFW = cFFW;
	}

	public String getJFYWH() {
		return JFYWH;
	}

	public void setJFYWH(String jFYWH) {
		JFYWH = jFYWH;
	}

	public String getJFJG() {
		return JFJG;
	}

	public void setJFJG(String jFJG) {
		JFJG = jFJG;
	}

	public String getJFWJ() {
		return JFWJ;
	}

	public void setJFWJ(String jFWJ) {
		JFWJ = jFWJ;
	}

	public String getJFWH() {
		return JFWH;
	}

	public void setJFWH(String jFWH) {
		JFWH = jFWH;
	}

	public String getJFDBR() {
		return JFDBR;
	}

	public void setJFDBR(String jFDBR) {
		JFDBR = jFDBR;
	}

	public String getJFDJSJ() {
		return JFDJSJ;
	}

	public void setJFDJSJ(String jFDJSJ) {
		JFDJSJ = jFDJSJ;
	}

	public String getFJ() {
		return FJ;
	}

	public void setFJ(String fJ) {
		FJ = fJ;
	}

	public String getQSZT() {
		return QSZT;
	}

	public void setQSZT(String qSZT) {
		QSZT = qSZT;
	}
}
