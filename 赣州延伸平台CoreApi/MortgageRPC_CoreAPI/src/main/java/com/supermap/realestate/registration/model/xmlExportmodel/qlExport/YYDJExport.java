package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("YYDJ")
public class YYDJExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "异议登记";

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
	private PropertyInfo YYSX = new PropertyInfo("异议事项");
	private PropertyInfo BDCDJZMH = new PropertyInfo("不动产登记证明号");
	private PropertyInfo ZXYYYWH = new PropertyInfo("注销异议业务号");
	private PropertyInfo ZXYYYY = new PropertyInfo("注销异议原因");
	private PropertyInfo ZXYYDBR = new PropertyInfo("注销异议登簿人");
	private PropertyInfo ZXYYDJSJ = new PropertyInfo("注销异议登记时间");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

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

	public PropertyInfo getYYSX() {
		return YYSX;
	}

	public void setYYSX(Object _Value) {
		YYSX.setvalue(_Value);
	}

	public PropertyInfo getBDCDJZMH() {
		return BDCDJZMH;
	}

	public void setBDCDJZMH(Object _Value) {
		BDCDJZMH.setvalue(_Value);
	}

	public PropertyInfo getZXYYYWH() {
		return ZXYYYWH;
	}

	public void setZXYYYWH(Object _Value) {
		ZXYYYWH.setvalue(_Value);
	}

	public PropertyInfo getZXYYYY() {
		return ZXYYYY;
	}

	public void setZXYYYY(Object _Value) {
		ZXYYYY.setvalue(_Value);
	}

	public PropertyInfo getZXYYDBR() {
		return ZXYYDBR;
	}

	public void setZXYYDBR(Object _Value) {
		ZXYYDBR.setvalue(_Value);
	}

	public PropertyInfo getZXYYDJSJ() {
		return ZXYYDJSJ;
	}

	public void setZXYYDJSJ(Object _Value) {
		ZXYYDJSJ.setvalue(_Value);
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
