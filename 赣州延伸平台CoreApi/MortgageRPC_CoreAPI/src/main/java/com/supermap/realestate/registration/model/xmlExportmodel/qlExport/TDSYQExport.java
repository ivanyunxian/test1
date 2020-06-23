package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("TDSYQ")
public class TDSYQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "土地所有权";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo QLID = new PropertyInfo("权利ID");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo ZDDM = new PropertyInfo("宗地代码");
	private PropertyInfo YWH = new PropertyInfo("业务号");
	private PropertyInfo QLLX = new PropertyInfo("权利类型");
	private PropertyInfo QLLXMC = new PropertyInfo("权利类型名称");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo MJDW = new PropertyInfo("面积单位");
	private PropertyInfo MJDWMC = new PropertyInfo("面积单位名称");
	private PropertyInfo NYDMJ = new PropertyInfo("农用地面积");
	private PropertyInfo GDMJ = new PropertyInfo("耕地面积");
	private PropertyInfo LDMJ = new PropertyInfo("林地面积");
	private PropertyInfo CDMJ = new PropertyInfo("草地面积");
	private PropertyInfo QTNYDMJ = new PropertyInfo("其它农用地面积");
	private PropertyInfo JSYDMJ = new PropertyInfo("建设用地面积");
	private PropertyInfo WLYDMJ = new PropertyInfo("未利用地面积");
	private PropertyInfo BDCQZH = new PropertyInfo("不动产权证号");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

	public PropertyInfo getQSZTMC() {
		return QSZTMC;
	}

	public void setQSZTMC(Object _Value) {
		QSZTMC.setvalue(_Value);
	}

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

	public PropertyInfo getMJDWMC() {
		return MJDWMC;
	}

	public void setMJDWMC(Object _Value) {
		MJDWMC.setvalue(_Value);
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

	public PropertyInfo getZDDM() {
		return ZDDM;
	}

	public void setZDDM(Object _Value) {
		ZDDM.setvalue(_Value);
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

	public PropertyInfo getMJDW() {
		return MJDW;
	}

	public void setMJDW(Object _Value) {
		MJDW.setvalue(_Value);
	}

	public PropertyInfo getNYDMJ() {
		return NYDMJ;
	}

	public void setNYDMJ(Object _Value) {
		NYDMJ.setvalue(_Value);
	}

	public PropertyInfo getGDMJ() {
		return GDMJ;
	}

	public void setGDMJ(Object _Value) {
		GDMJ.setvalue(_Value);
	}

	public PropertyInfo getLDMJ() {
		return LDMJ;
	}

	public void setLDMJ(Object _Value) {
		LDMJ.setvalue(_Value);
	}

	public PropertyInfo getCDMJ() {
		return CDMJ;
	}

	public void setCDMJ(Object _Value) {
		CDMJ.setvalue(_Value);
	}

	public PropertyInfo getQTNYDMJ() {
		return QTNYDMJ;
	}

	public void setQTNYDMJ(Object _Value) {
		QTNYDMJ.setvalue(_Value);
	}

	public PropertyInfo getJSYDMJ() {
		return JSYDMJ;
	}

	public void setJSYDMJ(Object _Value) {
		JSYDMJ.setvalue(_Value);
	}

	public PropertyInfo getWLYDMJ() {
		return WLYDMJ;
	}

	public void setWLYDMJ(Object _Value) {
		WLYDMJ.setvalue(_Value);
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
