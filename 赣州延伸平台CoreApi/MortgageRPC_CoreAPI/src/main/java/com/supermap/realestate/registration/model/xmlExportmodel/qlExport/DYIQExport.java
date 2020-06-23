package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("DYIQ")
public class DYIQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "地役权";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo QLID = new PropertyInfo("权利ID");
	private PropertyInfo YWH = new PropertyInfo("业务号");
	private PropertyInfo GYDBDCDYH = new PropertyInfo("供役地不动产单元号");
	private PropertyInfo GYDQLR = new PropertyInfo("供役地权利人");
	private PropertyInfo GYDQLRZJZL = new PropertyInfo("供役地权利人证件种类");
	private PropertyInfo GYDQLRZJZLMC = new PropertyInfo("供役地权利人证件种类名称");
	private PropertyInfo GYDQLRZJH = new PropertyInfo("供役地权利人证件号");
	private PropertyInfo XYDBDCDYH = new PropertyInfo("需役地不动产单元号");
	private PropertyInfo XYDZL = new PropertyInfo("需役地坐落");
	private PropertyInfo XYDQLR = new PropertyInfo("需役地权利人");
	private PropertyInfo XYDQLRZJZL = new PropertyInfo("需役地权利人证件种类");
	private PropertyInfo XYDQLRZJZLMC = new PropertyInfo("需役地权利人证件种类名称");
	private PropertyInfo XYDQLRZJH = new PropertyInfo("需役地权利人证件号");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo DYQNR = new PropertyInfo("地役权内容");
	private PropertyInfo BDCDJZMH = new PropertyInfo("不动产登记证明号");
	private PropertyInfo QLQSSJ = new PropertyInfo("权利起始时间");
	private PropertyInfo QLJSSJ = new PropertyInfo("权利结束时间");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

	public PropertyInfo getGYDQLRZJZLMC() {
		return GYDQLRZJZLMC;
	}

	public void setGYDQLRZJZLMC(Object _Value) {
		GYDQLRZJZLMC.setvalue(_Value);
	}

	public PropertyInfo getXYDQLRZJZLMC() {
		return XYDQLRZJZLMC;
	}

	public void setXYDQLRZJZLMC(Object _Value) {
		XYDQLRZJZLMC.setvalue(_Value);
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

	public PropertyInfo getYWH() {
		return YWH;
	}

	public void setYWH(Object _Value) {
		YWH.setvalue(_Value);
	}

	public PropertyInfo getGYDBDCDYH() {
		return GYDBDCDYH;
	}

	public void setGYDBDCDYH(Object _Value) {
		GYDBDCDYH.setvalue(_Value);
	}

	public PropertyInfo getGYDQLR() {
		return GYDQLR;
	}

	public void setGYDQLR(Object _Value) {
		GYDQLR.setvalue(_Value);
	}

	public PropertyInfo getGYDQLRZJZL() {
		return GYDQLRZJZL;
	}

	public void setGYDQLRZJZL(Object _Value) {
		GYDQLRZJZL.setvalue(_Value);
	}

	public PropertyInfo getGYDQLRZJH() {
		return GYDQLRZJH;
	}

	public void setGYDQLRZJH(Object _Value) {
		GYDQLRZJH.setvalue(_Value);
	}

	public PropertyInfo getXYDBDCDYH() {
		return XYDBDCDYH;
	}

	public void setXYDBDCDYH(Object _Value) {
		XYDBDCDYH.setvalue(_Value);
	}

	public PropertyInfo getXYDZL() {
		return XYDZL;
	}

	public void setXYDZL(Object _Value) {
		XYDZL.setvalue(_Value);
	}

	public PropertyInfo getXYDQLR() {
		return XYDQLR;
	}

	public void setXYDQLR(Object _Value) {
		XYDQLR.setvalue(_Value);
	}

	public PropertyInfo getXYDQLRZJZL() {
		return XYDQLRZJZL;
	}

	public void setXYDQLRZJZL(Object _Value) {
		XYDQLRZJZL.setvalue(_Value);
	}

	public PropertyInfo getXYDQLRZJH() {
		return XYDQLRZJH;
	}

	public void setXYDQLRZJH(Object _Value) {
		XYDQLRZJH.setvalue(_Value);
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

	public PropertyInfo getDYQNR() {
		return DYQNR;
	}

	public void setDYQNR(Object _Value) {
		DYQNR.setvalue(_Value);
	}

	public PropertyInfo getBDCDJZMH() {
		return BDCDJZMH;
	}

	public void setBDCDJZMH(Object _Value) {
		BDCDJZMH.setvalue(_Value);
	}

	public PropertyInfo getQLQSSJ() {
		return QLQSSJ;
	}

	public void setQLQSSJ(Object _Value) {
		QLQSSJ.setvalue(_Value);
	}

	public PropertyInfo getQLJSSJ() {
		return QLJSSJ;
	}

	public void setQLJSSJ(Object _Value) {
		QLJSSJ.setvalue(_Value);
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
