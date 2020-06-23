package com.supermap.realestate.registration.model.xmlExportmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("HTNR")
public class HTNRExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "合同内容";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo HTH = new PropertyInfo("合同号");// 合同号
	private PropertyInfo QDSJ = new PropertyInfo("签订时间");// 签订时间
	private PropertyInfo BASJ = new PropertyInfo("备案时间");// 备案时间
	private PropertyInfo JYSJ = new PropertyInfo("交易时间");// 交易时间
	private PropertyInfo HTZT = new PropertyInfo("合同状态");// 合同状态

	public PropertyInfo getHTH() {
		return HTH;
	}

	public void setHTH(Object _Value) {
		HTH.setvalue(_Value);
	}

	public PropertyInfo getQDSJ() {
		return QDSJ;
	}

	public void setQDSJ(Object _Value) {
		QDSJ.setvalue(_Value);
	}

	public PropertyInfo getBASJ() {
		return BASJ;
	}

	public void setBASJ(Object _Value) {
		BASJ.setvalue(_Value);
	}

	public PropertyInfo getJYSJ() {
		return JYSJ;
	}

	public void setJYSJ(Object _Value) {
		JYSJ.setvalue(_Value);
	}

	public PropertyInfo getHTZT() {
		return HTZT;
	}

	public void setHTZT(Object _Value) {
		HTZT.setvalue(_Value);
	}

}
