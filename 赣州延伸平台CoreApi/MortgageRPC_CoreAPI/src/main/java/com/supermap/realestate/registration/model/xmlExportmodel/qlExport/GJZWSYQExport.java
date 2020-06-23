package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("GJZWSYQ")
public class GJZWSYQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "构建筑物所有权";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo QLID = new PropertyInfo("权利ID");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo YWH = new PropertyInfo("业务号");
	private PropertyInfo QLLX = new PropertyInfo("权利类型");
	private PropertyInfo QLLXMC = new PropertyInfo("权利类型名称");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo ZL = new PropertyInfo("坐落");
	private PropertyInfo TDHYSYQR = new PropertyInfo("土地/海域使用权人");
	private PropertyInfo TDHYSYMJ = new PropertyInfo("土地/海域使用面积");
	private PropertyInfo TDHYSYQSSJ = new PropertyInfo("土地/海域使用起始时间");
	private PropertyInfo TDHYSYJSSJ = new PropertyInfo("土地/海域使用结束时间");
	private PropertyInfo GJZWLX = new PropertyInfo("构（建）筑物类型");
	private PropertyInfo GJZWLXMC = new PropertyInfo("构（建）筑物类型名称");
	private PropertyInfo GJZWGHYT = new PropertyInfo("构（建）筑物规划用途");
	private PropertyInfo GJZWMJ = new PropertyInfo("构(建)筑物面积");
	private PropertyInfo JGSJ = new PropertyInfo("竣工时间");
	private PropertyInfo BDCQZH = new PropertyInfo("不动产权证号");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo GJZWPMT = new PropertyInfo("构（建）筑物平面图");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

	public PropertyInfo getQLLXMC() {
		return QLLXMC;
	}

	public void setQLLXMC(Object _Value) {
		QLLXMC.setvalue(_Value);
	}

	public PropertyInfo getDJLXMC() {
		return DJLXMC;
	}

	public void setDJLXMC(Object _Value) {
		DJLXMC.setvalue(_Value);
	}

	public PropertyInfo getGJZWLXMC() {
		return GJZWLXMC;
	}

	public void setGJZWLXMC(Object _Value) {
		GJZWLXMC.setvalue(_Value);
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

	public PropertyInfo getQLLX() {
		return QLLX;
	}

	public void setQLLX(Object _Value) {
		QLLX.setvalue(_Value);
	}

	public PropertyInfo getDJLX() {
		return DJLX;
	}

	public void setDJLX(Object _Value) {
		DJLX.setvalue(_Value);
	}

	public PropertyInfo getDJYY() {
		return DJYY;
	}

	public void setDJYY(Object _Value) {
		DJYY.setvalue(_Value);
	}

	public PropertyInfo getZL() {
		return ZL;
	}

	public void setZL(Object _Value) {
		ZL.setvalue(_Value);
	}

	public PropertyInfo getTDHYSYQR() {
		return TDHYSYQR;
	}

	public void setTDHYSYQR(Object _Value) {
		TDHYSYQR.setvalue(_Value);
	}

	public PropertyInfo getTDHYSYMJ() {
		return TDHYSYMJ;
	}

	public void setTDHYSYMJ(Object _Value) {
		TDHYSYMJ.setvalue(_Value);
	}

	public PropertyInfo getTDHYSYQSSJ() {
		return TDHYSYQSSJ;
	}

	public void setTDHYSYQSSJ(Object _Value) {
		TDHYSYQSSJ.setvalue(_Value);
	}

	public PropertyInfo getTDHYSYJSSJ() {
		return TDHYSYJSSJ;
	}

	public void setTDHYSYJSSJ(Object _Value) {
		TDHYSYJSSJ.setvalue(_Value);
	}

	public PropertyInfo getGJZWLX() {
		return GJZWLX;
	}

	public void setGJZWLX(Object _Value) {
		GJZWLX.setvalue(_Value);
	}

	public PropertyInfo getGJZWGHYT() {
		return GJZWGHYT;
	}

	public void setGJZWGHYT(Object _Value) {
		GJZWGHYT.setvalue(_Value);
	}

	public PropertyInfo getGJZWMJ() {
		return GJZWMJ;
	}

	public void setGJZWMJ(Object _Value) {
		GJZWMJ.setvalue(_Value);
	}

	public PropertyInfo getJGSJ() {
		return JGSJ;
	}

	public void setJGSJ(Object _Value) {
		JGSJ.setvalue(_Value);
	}

	public PropertyInfo getBDCQZH() {
		return BDCQZH;
	}

	public void setBDCQZH(Object _Value) {
		BDCQZH.setvalue(_Value);
	}

	public PropertyInfo getFJ() {
		return FJ;
	}

	public void setFJ(Object _Value) {
		FJ.setvalue(_Value);
	}

	public PropertyInfo getGJZWPMT() {
		return GJZWPMT;
	}

	public void setGJZWPMT(Object _Value) {
		GJZWPMT.setvalue(_Value);
	}

	public PropertyInfo getQSZT() {
		return QSZT;
	}

	public void setQSZT(Object _Value) {
		QSZT.setvalue(_Value);
	}
}
