package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("YGDJ")
public class YGDJExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "预告登记";

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
	private PropertyInfo BDCZL = new PropertyInfo("不动产坐落");
	private PropertyInfo YWR = new PropertyInfo("义务人");
	private PropertyInfo YWRZJZL = new PropertyInfo("义务人证件种类");
	private PropertyInfo YWRZJZLMC = new PropertyInfo("义务人证件种类名称");
	private PropertyInfo YWRZJH = new PropertyInfo("义务人证件号");
	private PropertyInfo YGDJZL = new PropertyInfo("预告登记种类");
	private PropertyInfo YGDJZLMC = new PropertyInfo("预告登记种类名称");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo TDSYQR = new PropertyInfo("土地使用权人");
	private PropertyInfo GHYT = new PropertyInfo("规划用途");
	private PropertyInfo GHYTMC = new PropertyInfo("规划用途名称");
	private PropertyInfo FWXZ = new PropertyInfo("房屋性质");
	private PropertyInfo FWXZMC = new PropertyInfo("房屋性质名称");
	private PropertyInfo FWJG = new PropertyInfo("房屋结构");
	private PropertyInfo FWJGMC = new PropertyInfo("房屋结构名称");
	private PropertyInfo SZC = new PropertyInfo("所在层");
	private PropertyInfo ZCS = new PropertyInfo("总层数");
	private PropertyInfo JZMJ = new PropertyInfo("建筑面积");
	private PropertyInfo QDJG = new PropertyInfo("取得价格/被担保主债权数额");
	private PropertyInfo BDCDJZMH = new PropertyInfo("不动产登记证明号");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo QSZT = new PropertyInfo("权属状态");
	private PropertyInfo QSZTMC = new PropertyInfo("权属状态名称");

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

	public PropertyInfo getBDCZL() {
		return BDCZL;
	}

	public void setBDCZL(Object _Value) {
		BDCZL.setvalue(_Value);
	}

	public PropertyInfo getYWR() {
		return YWR;
	}

	public void setYWR(Object _Value) {
		YWR.setvalue(_Value);
	}

	public PropertyInfo getYWRZJZL() {
		return YWRZJZL;
	}

	public void setYWRZJZL(Object _Value) {
		YWRZJZL.setvalue(_Value);
	}

	public PropertyInfo getYWRZJH() {
		return YWRZJH;
	}

	public void setYWRZJH(Object _Value) {
		YWRZJH.setvalue(_Value);
	}

	public PropertyInfo getYGDJZL() {
		return YGDJZL;
	}

	public void setYGDJZL(Object _Value) {
		YGDJZL.setvalue(_Value);
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

	public PropertyInfo getTDSYQR() {
		return TDSYQR;
	}

	public void setTDSYQR(Object _Value) {
		TDSYQR.setvalue(_Value);
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

	public PropertyInfo getQDJG() {
		return QDJG;
	}

	public void setQDJG(Object _Value) {
		QDJG.setvalue(_Value);
	}

	public PropertyInfo getBDCDJZMH() {
		return BDCDJZMH;
	}

	public void setBDCDJZMH(Object _Value) {
		BDCDJZMH.setvalue(_Value);
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

	public PropertyInfo getYWRZJZLMC() {
		return YWRZJZLMC;
	}

	public void setYWRZJZLMC(Object _Value) {
		YWRZJZLMC.setvalue(_Value);
	}

	public PropertyInfo getYGDJZLMC() {
		return YGDJZLMC;
	}

	public void setYGDJZLMC(Object _Value) {
		YGDJZLMC.setvalue(_Value);
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

}
