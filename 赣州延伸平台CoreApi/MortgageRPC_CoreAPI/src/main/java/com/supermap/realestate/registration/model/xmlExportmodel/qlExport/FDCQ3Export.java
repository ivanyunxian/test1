package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("FDCQ3")
public class FDCQ3Export {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "建筑物区分所有权业主共有部分";

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
	private PropertyInfo JGZWBH = new PropertyInfo("建(构)筑物编号");
	private PropertyInfo JGZWMC = new PropertyInfo("建(构)筑物名称");
	private PropertyInfo JGZWSL = new PropertyInfo("建(构)筑物数量");
	private PropertyInfo JGZWMJ = new PropertyInfo("建(构)筑物面积");
	private PropertyInfo FTTDMJ = new PropertyInfo("分摊土地面积");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

	public PropertyInfo getQLLXMC() {
		return QLLXMC;
	}

	public void setQLLXMC(Object _Value) {
		QLLXMC.setvalue(_Value);
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

	public PropertyInfo getJGZWBH() {
		return JGZWBH;
	}

	public void setJGZWBH(Object _Value) {
		JGZWBH.setvalue(_Value);
	}

	public PropertyInfo getJGZWMC() {
		return JGZWMC;
	}

	public void setJGZWMC(Object _Value) {
		JGZWMC.setvalue(_Value);
	}

	public PropertyInfo getJGZWSL() {
		return JGZWSL;
	}

	public void setJGZWSL(Object _Value) {
		JGZWSL.setvalue(_Value);
	}

	public PropertyInfo getJGZWMJ() {
		return JGZWMJ;
	}

	public void setJGZWMJ(Object _Value) {
		JGZWMJ.setvalue(_Value);
	}

	public PropertyInfo getFTTDMJ() {
		return FTTDMJ;
	}

	public void setFTTDMJ(Object _Value) {
		FTTDMJ.setvalue(_Value);
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
