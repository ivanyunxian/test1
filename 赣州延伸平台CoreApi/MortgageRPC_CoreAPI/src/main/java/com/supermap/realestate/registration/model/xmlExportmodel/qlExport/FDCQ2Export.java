package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("FDCQ2")
public class FDCQ2Export {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "房地产权（独幢、层、套、间）";

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
	private PropertyInfo QLLX = new PropertyInfo("权利类型");
	private PropertyInfo QLLXMC = new PropertyInfo("权利类型名称");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo FDZL = new PropertyInfo("房地坐落");
	private PropertyInfo TDSYQR = new PropertyInfo("土地使用权人");
	private PropertyInfo DYTDMJ = new PropertyInfo("独用土地面积");
	private PropertyInfo FTTDMJ = new PropertyInfo("分摊土地面积");
	private PropertyInfo TDSYQSSJ = new PropertyInfo("土地使用起始时间");
	private PropertyInfo TDSYJSSJ = new PropertyInfo("土地使用结束时间");
	private PropertyInfo DJSJ = new PropertyInfo("登记时间");
	private PropertyInfo ZXSJ = new PropertyInfo("注销时间");
	private PropertyInfo FDCJYJG = new PropertyInfo("房地产交易价格");
	private PropertyInfo GHYT = new PropertyInfo("规划用途");
	private PropertyInfo GHYTMC = new PropertyInfo("规划用途名称");
	private PropertyInfo FWXZ = new PropertyInfo("房屋性质");
	private PropertyInfo FWXZMC = new PropertyInfo("房屋性质名称");
	private PropertyInfo FWJG = new PropertyInfo("房屋结构");
	private PropertyInfo FWJGMC = new PropertyInfo("房屋结构名称");
	private PropertyInfo SZC = new PropertyInfo("所在层");
	private PropertyInfo ZCS = new PropertyInfo("总层数");
	private PropertyInfo JZMJ = new PropertyInfo("建筑面积");
	private PropertyInfo ZYJZMJ = new PropertyInfo("专有建筑面积");
	private PropertyInfo FTJZMJ = new PropertyInfo("分摊建筑面积");
	private PropertyInfo JGSJ = new PropertyInfo("竣工时间");
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

	public PropertyInfo getGHYTMC() {
		return GHYTMC;
	}

	public void setGHYTMC(Object _Value) {
		GHYTMC.setvalue(_Value);
	}

	public PropertyInfo getFWXZMC() {
		return FWXZMC;
	}

	public void setFWXZMC(Object _Value) {
		FWXZMC.setvalue(_Value);
	}

	public PropertyInfo getFWJGMC() {
		return FWJGMC;
	}

	public void setFWJGMC(Object _Value) {
		FWJGMC.setvalue(_Value);
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

	public PropertyInfo getFDZL() {
		return FDZL;
	}

	public void setFDZL(Object _Value) {
		FDZL.setvalue(_Value);
	}

	public PropertyInfo getTDSYQR() {
		return TDSYQR;
	}

	public void setTDSYQR(Object _Value) {
		TDSYQR.setvalue(_Value);
	}

	public PropertyInfo getDYTDMJ() {
		return DYTDMJ;
	}

	public void setDYTDMJ(Object _Value) {
		DYTDMJ.setvalue(_Value);
	}

	public PropertyInfo getFTTDMJ() {
		return FTTDMJ;
	}

	public void setFTTDMJ(Object _Value) {
		FTTDMJ.setvalue(_Value);
	}

	public PropertyInfo getTDSYQSSJ() {
		return TDSYQSSJ;
	}

	public void setTDSYQSSJ(Object _Value) {
		TDSYQSSJ.setvalue(_Value);
	}

	public PropertyInfo getTDSYJSSJ() {
		return TDSYJSSJ;
	}

	public void setTDSYJSSJ(Object _Value) {
		TDSYJSSJ.setvalue(_Value);
	}
	public PropertyInfo getDJSJ() {
		return DJSJ;
	}

	public void setDJSJ(Object _Value) {
		DJSJ.setvalue(_Value);
	}

	public PropertyInfo getZXSJ() {
		return ZXSJ;
	}

	public void setZXSJ(Object _Value) {
		ZXSJ.setvalue(_Value);
	}

	public PropertyInfo getFDCJYJG() {
		return FDCJYJG;
	}

	public void setFDCJYJG(Object _Value) {
		FDCJYJG.setvalue(_Value);
	}

	public PropertyInfo getGHYT() {
		return GHYT;
	}

	public void setGHYT(Object _Value) {
		GHYT.setvalue(_Value);
	}

	public PropertyInfo getFWXZ() {
		return FWXZ;
	}

	public void setFWXZ(Object _Value) {
		FWXZ.setvalue(_Value);
	}

	public PropertyInfo getFWJG() {
		return FWJG;
	}

	public void setFWJG(Object _Value) {
		FWJG.setvalue(_Value);
	}

	public PropertyInfo getSZC() {
		return SZC;
	}

	public void setSZC(Object _Value) {
		SZC.setvalue(_Value);
	}

	public PropertyInfo getZCS() {
		return ZCS;
	}

	public void setZCS(Object _Value) {
		ZCS.setvalue(_Value);
	}

	public PropertyInfo getJZMJ() {
		return JZMJ;
	}

	public void setJZMJ(Object _Value) {
		JZMJ.setvalue(_Value);
	}

	public PropertyInfo getZYJZMJ() {
		return ZYJZMJ;
	}

	public void setZYJZMJ(Object _Value) {
		ZYJZMJ.setvalue(_Value);
	}

	public PropertyInfo getFTJZMJ() {
		return FTJZMJ;
	}

	public void setFTJZMJ(Object _Value) {
		FTJZMJ.setvalue(_Value);
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

	public PropertyInfo getQSZT() {
		return QSZT;
	}

	public void setQSZT(Object _Value) {
		QSZT.setvalue(_Value);
	}
}
