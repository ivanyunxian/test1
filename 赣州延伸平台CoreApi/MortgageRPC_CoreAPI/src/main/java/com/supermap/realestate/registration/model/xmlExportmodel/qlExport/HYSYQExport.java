package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("HYSYQ")
public class HYSYQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "海域（含无居民海岛）使用权";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo QLID = new PropertyInfo("权利ID");
	private PropertyInfo ZHHDDM = new PropertyInfo("宗海/海岛代码");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo YWH = new PropertyInfo("业务号");
	private PropertyInfo QLLX = new PropertyInfo("权利类型");
	private PropertyInfo QLLXMC = new PropertyInfo("权利类型名称");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo SYQMJ = new PropertyInfo("使用权面积");
	private PropertyInfo SYQQSSJ = new PropertyInfo("使用权起始时间");
	private PropertyInfo SYQJSSJ = new PropertyInfo("使用权结束时间");
	private PropertyInfo SYJZE = new PropertyInfo("使用金总额");
	private PropertyInfo SYJBZYJ = new PropertyInfo("使用金标准依据");
	private PropertyInfo SYJJNQK = new PropertyInfo("使用金缴纳情况");
	private PropertyInfo BDCQZH = new PropertyInfo("不动产权证号");
	private PropertyInfo FJ = new PropertyInfo("附记");
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

	public PropertyInfo getZHHDDM() {
		return ZHHDDM;
	}

	public void setZHHDDM(Object _Value) {
		ZHHDDM.setvalue(_Value);
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

	public PropertyInfo getSYQMJ() {
		return SYQMJ;
	}

	public void setSYQMJ(Object _Value) {
		SYQMJ.setvalue(_Value);
	}

	public PropertyInfo getSYQQSSJ() {
		return SYQQSSJ;
	}

	public void setSYQQSSJ(Object _Value) {
		SYQQSSJ.setvalue(_Value);
	}

	public PropertyInfo getSYQJSSJ() {
		return SYQJSSJ;
	}

	public void setSYQJSSJ(Object _Value) {
		SYQJSSJ.setvalue(_Value);
	}

	public PropertyInfo getSYJZE() {
		return SYJZE;
	}

	public void setSYJZE(Object _Value) {
		SYJZE.setvalue(_Value);
	}

	public PropertyInfo getSYJBZYJ() {
		return SYJBZYJ;
	}

	public void setSYJBZYJ(Object _Value) {
		SYJBZYJ.setvalue(_Value);
	}

	public PropertyInfo getSYJJNQK() {
		return SYJJNQK;
	}

	public void setSYJJNQK(Object _Value) {
		SYJJNQK.setvalue(_Value);
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

	public PropertyInfo getQSZT() {
		return QSZT;
	}

	public void setQSZT(Object _Value) {
		QSZT.setvalue(_Value);
	}
}
