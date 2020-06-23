package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("FDCQ1")
public class FDCQ1Export {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "房地产权（项目内多幢）";

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
	private PropertyInfo FDCJYJG = new PropertyInfo("房地产交易价格");
	private PropertyInfo BDCQZH = new PropertyInfo("不动产权证号");
	private PropertyInfo FJ = new PropertyInfo("附记");// 附记
	private PropertyInfo FCFHT = new PropertyInfo("房产分户图");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");
	private PropertyInfo XMMC = new PropertyInfo("项目名称");
	private PropertyInfo ZH = new PropertyInfo("幢号");
	private PropertyInfo ZCS = new PropertyInfo("总层数");
	private PropertyInfo GHYT = new PropertyInfo("规划用途");
	private PropertyInfo GHYTMC = new PropertyInfo("规划用途名称");
	private PropertyInfo FWJG = new PropertyInfo("房屋结构");
	private PropertyInfo FWJGMC = new PropertyInfo("房屋结构名称");
	private PropertyInfo JZMJ = new PropertyInfo("建筑面积");
	private PropertyInfo JGSJ = new PropertyInfo("竣工时间");
	private PropertyInfo ZTS = new PropertyInfo("总套数");

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

	public PropertyInfo getGHYTMC() {
		return GHYTMC;
	}

	public void setGHYTMC(Object _Value) {
		GHYTMC.setvalue(_Value);
	}

	public PropertyInfo getFWJGMC() {
		return FWJGMC;
	}

	public void setFWJGMC(Object _Value) {
		FWJGMC.setvalue(_Value);
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

	public PropertyInfo getFDCJYJG() {
		return FDCJYJG;
	}

	public void setFDCJYJG(Object _Value) {
		FDCJYJG.setvalue(_Value);
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

	public PropertyInfo getFCFHT() {
		return FCFHT;
	}

	public void setFCFHT(Object _Value) {
		FCFHT.setvalue(_Value);
	}

	public PropertyInfo getQSZT() {
		return QSZT;
	}

	public void setQSZT(Object _Value) {
		QSZT.setvalue(_Value);
	}

	public PropertyInfo getXMMC() {
		return XMMC;
	}

	public void setXMMC(Object _Value) {
		XMMC.setvalue(_Value);
	}

	public PropertyInfo getZH() {
		return ZH;
	}

	public void setZH(Object _Value) {
		ZH.setvalue(_Value);
	}

	public PropertyInfo getZCS() {
		return ZCS;
	}

	public void setZCS(Object _Value) {
		ZCS.setvalue(_Value);
	}

	public PropertyInfo getGHYT() {
		return GHYT;
	}

	public void setGHYT(Object _Value) {
		GHYT.setvalue(_Value);
	}

	public PropertyInfo getFWJG() {
		return FWJG;
	}

	public void setFWJG(Object _Value) {
		FWJG.setvalue(_Value);
	}

	public PropertyInfo getJZMJ() {
		return JZMJ;
	}

	public void setJZMJ(Object _Value) {
		JZMJ.setvalue(_Value);
	}

	public PropertyInfo getJGSJ() {
		return JGSJ;
	}

	public void setJGSJ(Object _Value) {
		JGSJ.setvalue(_Value);
	}

	public PropertyInfo getZTS() {
		return ZTS;
	}

	public void setZTS(Object _Value) {
		ZTS.setvalue(_Value);
	}
}
