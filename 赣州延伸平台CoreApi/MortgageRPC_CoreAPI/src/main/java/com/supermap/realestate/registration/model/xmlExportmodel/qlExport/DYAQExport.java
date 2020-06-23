package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("DYAQ")
public class DYAQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "抵押权";

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
	private PropertyInfo ZXYWH = new PropertyInfo("注销业务号");
	private PropertyInfo DYBDCLX = new PropertyInfo("抵押不动产类型");
	private PropertyInfo DYBDCLXMC = new PropertyInfo("抵押不动产类型名称");
	private PropertyInfo DYR = new PropertyInfo("抵押人");
	private PropertyInfo DYFS = new PropertyInfo("抵押方式");
	private PropertyInfo DYFSMC = new PropertyInfo("抵押方式名称");
	private PropertyInfo DJLX = new PropertyInfo("登记类型");
	private PropertyInfo DJLXMC = new PropertyInfo("登记类型名称");
	private PropertyInfo DJYY = new PropertyInfo("登记原因");
	private PropertyInfo ZJJZWZL = new PropertyInfo("在建建筑物坐落");
	private PropertyInfo ZJJZWDYFW = new PropertyInfo("在建建筑物抵押范围");
	private PropertyInfo BDBZZQSE = new PropertyInfo("被担保主债权数额");
	private PropertyInfo ZWLXQSSJ = new PropertyInfo("债务履行起始时间");
	private PropertyInfo ZWLXJSSJ = new PropertyInfo("债务履行结束时间");
	private PropertyInfo ZGZQQDSS = new PropertyInfo("最高债权确定事实");
	private PropertyInfo ZGZQSE = new PropertyInfo("最高债权数额");
	private PropertyInfo ZXDYYWH = new PropertyInfo("注销抵押业务号");
	private PropertyInfo ZXDYYY = new PropertyInfo("注销抵押原因");
	private PropertyInfo ZXSJ = new PropertyInfo("注销时间");
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

	public PropertyInfo getZXYWH() {
		return ZXYWH;
	}

	public void setZXYWH(Object _Value) {
		ZXYWH.setvalue(_Value);
	}

	public PropertyInfo getDYBDCLX() {
		return DYBDCLX;
	}

	public void setDYBDCLX(Object _Value) {
		DYBDCLX.setvalue(_Value);
	}

	public PropertyInfo getDYR() {
		return DYR;
	}

	public void setDYR(Object _Value) {
		DYR.setvalue(_Value);
	}

	public PropertyInfo getDYFS() {
		return DYFS;
	}

	public void setDYFS(Object _Value) {
		DYFS.setvalue(_Value);
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

	public PropertyInfo getZJJZWZL() {
		return ZJJZWZL;
	}

	public void setZJJZWZL(Object _Value) {
		ZJJZWZL.setvalue(_Value);
	}

	public PropertyInfo getZJJZWDYFW() {
		return ZJJZWDYFW;
	}

	public void setZJJZWDYFW(Object _Value) {
		ZJJZWDYFW.setvalue(_Value);
	}

	public PropertyInfo getBDBZZQSE() {
		return BDBZZQSE;
	}

	public void setBDBZZQSE(Object _Value) {
		BDBZZQSE.setvalue(_Value);
	}

	public PropertyInfo getZWLXQSSJ() {
		return ZWLXQSSJ;
	}

	public void setZWLXQSSJ(Object _Value) {
		ZWLXQSSJ.setvalue(_Value);
	}

	public PropertyInfo getZWLXJSSJ() {
		return ZWLXJSSJ;
	}

	public void setZWLXJSSJ(Object _Value) {
		ZWLXJSSJ.setvalue(_Value);
	}

	public PropertyInfo getZGZQQDSS() {
		return ZGZQQDSS;
	}

	public void setZGZQQDSS(Object _Value) {
		ZGZQQDSS.setvalue(_Value);
	}

	public PropertyInfo getZGZQSE() {
		return ZGZQSE;
	}

	public void setZGZQSE(Object _Value) {
		ZGZQSE.setvalue(_Value);
	}

	public PropertyInfo getZXDYYWH() {
		return ZXDYYWH;
	}

	public void setZXDYYWH(Object _Value) {
		ZXDYYWH.setvalue(_Value);
	}

	public PropertyInfo getZXDYYY() {
		return ZXDYYY;
	}

	public void setZXDYYY(Object _Value) {
		ZXDYYY.setvalue(_Value);
	}

	public PropertyInfo getZXSJ() {
		return ZXSJ;
	}

	public void setZXSJ(Object _Value) {
		ZXSJ.setvalue(_Value);
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

	public PropertyInfo getDYBDCLXMC() {
		return DYBDCLXMC;
	}

	public void setDYBDCLXMC(Object _Value) {
		DYBDCLXMC.setvalue(_Value);
	}

	public PropertyInfo getDYFSMC() {
		return DYFSMC;
	}

	public void setDYFSMC(Object _Value) {
		DYFSMC.setvalue(_Value);
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

}
