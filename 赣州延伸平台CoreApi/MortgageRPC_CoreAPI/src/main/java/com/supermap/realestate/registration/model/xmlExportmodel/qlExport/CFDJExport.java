package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("CFDJ")
public class CFDJExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "查封登记";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo QLID = new PropertyInfo("权利ID");
	private PropertyInfo LYQLID = new PropertyInfo("来源权利ID");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo YWH = new PropertyInfo("业务号");
	private PropertyInfo ZXYWH = new PropertyInfo("注销业务号");
	private PropertyInfo CFJG = new PropertyInfo("查封机关");
	private PropertyInfo CFLX = new PropertyInfo("查封类型");
	private PropertyInfo CFLXMC = new PropertyInfo("查封类型名称");
	private PropertyInfo CFWJ = new PropertyInfo("查封文件");
	private PropertyInfo CFWH = new PropertyInfo("查封文号");
	private PropertyInfo CFQSSJ = new PropertyInfo("查封起始时间");
	private PropertyInfo CFJSSJ = new PropertyInfo("查封结束时间");
	private PropertyInfo CFFW = new PropertyInfo("查封范围");
	private PropertyInfo JFYWH = new PropertyInfo("解封业务号");
	private PropertyInfo JFJG = new PropertyInfo("解封机关");
	private PropertyInfo JFWJ = new PropertyInfo("解封文件");
	private PropertyInfo JFWH = new PropertyInfo("解封文号");
	private PropertyInfo JFDBR = new PropertyInfo("解封登簿人");
	private PropertyInfo JFDJSJ = new PropertyInfo("解封登记时间");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

	public PropertyInfo getCFLXMC() {
		return CFLXMC;
	}

	public void setCFLXMC(Object _Value) {
		CFLXMC.setvalue(_Value);
	}

	public PropertyInfo getQSZTMC() {
		return QSZTMC;
	}

	public void setQSZTMC(Object _Value) {
		QSZTMC.setvalue(_Value);
	}

	public PropertyInfo getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(Object _Value) {
		BDCDYID.setvalue(_Value);
	}

	public PropertyInfo getQLID() {
		return QLID;
	}

	public void setQLID(Object _Value) {
		QLID.setvalue(_Value);
	}
	public PropertyInfo getLYQLID() {
		return LYQLID;
	}

	public void setLYQLID(Object _Value) {
		LYQLID.setvalue(_Value);
	}
	public PropertyInfo getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(Object _Value) {
		BDCDYH.setvalue(_Value);
	}

	public PropertyInfo getYWH() {
		return YWH;
	}

	public void setYWH(Object _Value) {
		YWH.setvalue(_Value);
	}

	public PropertyInfo getZXYWH() {
		return ZXYWH;
	}

	public void setZXYWH(Object _Value) {
		ZXYWH.setvalue(_Value);
	}

	public PropertyInfo getCFJG() {
		return CFJG;
	}

	public void setCFJG(Object _Value) {
		CFJG.setvalue(_Value);
	}

	public PropertyInfo getCFLX() {
		return CFLX;
	}

	public void setCFLX(Object _Value) {
		CFLX.setvalue(_Value);
	}

	public PropertyInfo getCFWJ() {
		return CFWJ;
	}

	public void setCFWJ(Object _Value) {
		CFWJ.setvalue(_Value);
	}

	public PropertyInfo getCFWH() {
		return CFWH;
	}

	public void setCFWH(Object _Value) {
		CFWH.setvalue(_Value);
	}

	public PropertyInfo getCFQSSJ() {
		return CFQSSJ;
	}

	public void setCFQSSJ(Object _Value) {
		CFQSSJ.setvalue(_Value);
	}

	public PropertyInfo getCFJSSJ() {
		return CFJSSJ;
	}

	public void setCFJSSJ(Object _Value) {
		CFJSSJ.setvalue(_Value);
	}

	public PropertyInfo getCFFW() {
		return CFFW;
	}

	public void setCFFW(Object _Value) {
		CFFW.setvalue(_Value);
	}

	public PropertyInfo getJFYWH() {
		return JFYWH;
	}

	public void setJFYWH(Object _Value) {
		JFYWH.setvalue(_Value);
	}

	public PropertyInfo getJFJG() {
		return JFJG;
	}

	public void setJFJG(Object _Value) {
		JFJG.setvalue(_Value);
	}

	public PropertyInfo getJFWJ() {
		return JFWJ;
	}

	public void setJFWJ(Object _Value) {
		JFWJ.setvalue(_Value);
	}

	public PropertyInfo getJFWH() {
		return JFWH;
	}

	public void setJFWH(Object _Value) {
		JFWH.setvalue(_Value);
	}

	public PropertyInfo getJFDBR() {
		return JFDBR;
	}

	public void setJFDBR(Object _Value) {
		JFDBR.setvalue(_Value);
	}

	public PropertyInfo getJFDJSJ() {
		return JFDJSJ;
	}

	public void setJFDJSJ(Object _Value) {
		JFDJSJ.setvalue(_Value);
	}

	public PropertyInfo getFJ() {
		return FJ;
	}

	public void setFJ(Object _Value) {
		FJ.setvalue(_Value);
	}

	public PropertyInfo getQSZT() {
		return QSZT;
	}

	public void setQSZT(Object _Value) {
		QSZT.setvalue(_Value);
	}
}
