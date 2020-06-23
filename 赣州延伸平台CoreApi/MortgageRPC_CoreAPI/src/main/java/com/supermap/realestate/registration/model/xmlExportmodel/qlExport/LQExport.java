package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("LQ")
public class LQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "林权";

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
	private PropertyInfo FBF = new PropertyInfo("发包方");
	private PropertyInfo SYQMJ = new PropertyInfo("使用权（承包）面积");
	private PropertyInfo LDSYQSSJ = new PropertyInfo("林地使用（承包）起始时间");
	private PropertyInfo LDSYJSSJ = new PropertyInfo("林地使用（承包）结束时间");
	private PropertyInfo LDSYQXZ = new PropertyInfo("林地所有权性质");
	private PropertyInfo LDSYQXZMC = new PropertyInfo("林地所有权性质名称");
	private PropertyInfo SLLMSYQR1 = new PropertyInfo("森林、林木所有权人");
	private PropertyInfo SLLMSYQR2 = new PropertyInfo("森林、林木使用权人");
	private PropertyInfo ZYSZ = new PropertyInfo("主要树种");
	private PropertyInfo ZS = new PropertyInfo("株数");
	private PropertyInfo LZ = new PropertyInfo("林种");
	private PropertyInfo LZMC = new PropertyInfo("林种名称");
	private PropertyInfo QY = new PropertyInfo("起源");
	private PropertyInfo QYMC = new PropertyInfo("起源名称");
	private PropertyInfo ZLND = new PropertyInfo("造林年度");
	private PropertyInfo LB = new PropertyInfo("林班");
	private PropertyInfo XB = new PropertyInfo("小班");
	private PropertyInfo XDM = new PropertyInfo("小地名");
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

	public PropertyInfo getLDSYQXZMC() {
		return LDSYQXZMC;
	}

	public void setLDSYQXZMC(Object _Value) {
		LDSYQXZMC.setvalue(_Value);
	}

	public PropertyInfo getLZMC() {
		return LZMC;
	}

	public void setLZMC(Object _Value) {
		LZMC.setvalue(_Value);
	}

	public PropertyInfo getQYMC() {
		return QYMC;
	}

	public void setQYMC(Object _Value) {
		QYMC.setvalue(_Value);
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

	public PropertyInfo getFBF() {
		return FBF;
	}

	public void setFBF(Object _Value) {
		FBF.setvalue(_Value);
	}

	public PropertyInfo getSYQMJ() {
		return SYQMJ;
	}

	public void setSYQMJ(Object _Value) {
		SYQMJ.setvalue(_Value);
	}

	public PropertyInfo getLDSYQSSJ() {
		return LDSYQSSJ;
	}

	public void setLDSYQSSJ(Object _Value) {
		LDSYQSSJ.setvalue(_Value);
	}

	public PropertyInfo getLDSYJSSJ() {
		return LDSYJSSJ;
	}

	public void setLDSYJSSJ(Object _Value) {
		LDSYJSSJ.setvalue(_Value);
	}

	public PropertyInfo getLDSYQXZ() {
		return LDSYQXZ;
	}

	public void setLDSYQXZ(Object _Value) {
		LDSYQXZ.setvalue(_Value);
	}

	public PropertyInfo getSLLMSYQR1() {
		return SLLMSYQR1;
	}

	public void setSLLMSYQR1(Object _Value) {
		SLLMSYQR1.setvalue(_Value);
	}

	public PropertyInfo getSLLMSYQR2() {
		return SLLMSYQR2;
	}

	public void setSLLMSYQR2(Object _Value) {
		SLLMSYQR2.setvalue(_Value);
	}

	public PropertyInfo getZYSZ() {
		return ZYSZ;
	}

	public void setZYSZ(Object _Value) {
		ZYSZ.setvalue(_Value);
	}

	public PropertyInfo getZS() {
		return ZS;
	}

	public void setZS(Object _Value) {
		ZS.setvalue(_Value);
	}

	public PropertyInfo getLZ() {
		return LZ;
	}

	public void setLZ(Object _Value) {
		LZ.setvalue(_Value);
	}

	public PropertyInfo getQY() {
		return QY;
	}

	public void setQY(Object _Value) {
		QY.setvalue(_Value);
	}

	public PropertyInfo getZLND() {
		return ZLND;
	}

	public void setZLND(Object _Value) {
		ZLND.setvalue(_Value);
	}

	public PropertyInfo getLB() {
		return LB;
	}

	public void setLB(Object _Value) {
		LB.setvalue(_Value);
	}

	public PropertyInfo getXB() {
		return XB;
	}

	public void setXB(Object _Value) {
		XB.setvalue(_Value);
	}

	public PropertyInfo getXDM() {
		return XDM;
	}

	public void setXDM(Object _Value) {
		XDM.setvalue(_Value);
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
