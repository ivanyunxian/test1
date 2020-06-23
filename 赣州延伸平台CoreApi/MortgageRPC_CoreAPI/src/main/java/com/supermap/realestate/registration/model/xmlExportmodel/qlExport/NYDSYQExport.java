package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("NYDSYQ")
public class NYDSYQExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "农用地（非林地）使用权";

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
	private PropertyInfo ZL = new PropertyInfo("坐落");
	private PropertyInfo FBFDM = new PropertyInfo("发包方代码");
	private PropertyInfo FBFMC = new PropertyInfo("发包方名称");
	private PropertyInfo CBMJ = new PropertyInfo("承包（使用权）面积");
	private PropertyInfo CBQSSJ = new PropertyInfo("承包(使用)起始时间");
	private PropertyInfo CBJSSJ = new PropertyInfo("承包(使用)结束时间");
	private PropertyInfo TDSYQXZ = new PropertyInfo("土地所有权性质");
	private PropertyInfo TDSYQXZMC = new PropertyInfo("土地所有权性质名称");
	private PropertyInfo SYTTLX = new PropertyInfo("水域滩涂类型");
	private PropertyInfo SYTTLXMC = new PropertyInfo("水域滩涂类型名称");
	private PropertyInfo YZYFS = new PropertyInfo("养殖业方式");
	private PropertyInfo YZYFSMC = new PropertyInfo("养殖业方式名称");
	private PropertyInfo CYZL = new PropertyInfo("草原质量");
	private PropertyInfo SYZCL = new PropertyInfo("适宜载畜量");
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

	public PropertyInfo getTDSYQXZMC() {
		return TDSYQXZMC;
	}

	public void setTDSYQXZMC(Object _Value) {
		TDSYQXZMC.setvalue(_Value);
	}

	public PropertyInfo getSYTTLXMC() {
		return SYTTLXMC;
	}

	public void setSYTTLXMC(Object _Value) {
		SYTTLXMC.setvalue(_Value);
	}

	public PropertyInfo getYZYFSMC() {
		return YZYFSMC;
	}

	public void setYZYFSMC(Object _Value) {
		YZYFSMC.setvalue(_Value);
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

	public PropertyInfo getZL() {
		return ZL;
	}

	public void setZL(Object _Value) {
		ZL.setvalue(_Value);
	}

	public PropertyInfo getFBFDM() {
		return FBFDM;
	}

	public void setFBFDM(Object _Value) {
		FBFDM.setvalue(_Value);
	}

	public PropertyInfo getFBFMC() {
		return FBFMC;
	}

	public void setFBFMC(Object _Value) {
		FBFMC.setvalue(_Value);
	}

	public PropertyInfo getCBMJ() {
		return CBMJ;
	}

	public void setCBMJ(Object _Value) {
		CBMJ.setvalue(_Value);
	}

	public PropertyInfo getCBQSSJ() {
		return CBQSSJ;
	}

	public void setCBQSSJ(Object _Value) {
		CBQSSJ.setvalue(_Value);
	}

	public PropertyInfo getCBJSSJ() {
		return CBJSSJ;
	}

	public void setCBJSSJ(Object _Value) {
		CBJSSJ.setvalue(_Value);
	}

	public PropertyInfo getTDSYQXZ() {
		return TDSYQXZ;
	}

	public void setTDSYQXZ(Object _Value) {
		TDSYQXZ.setvalue(_Value);
	}

	public PropertyInfo getSYTTLX() {
		return SYTTLX;
	}

	public void setSYTTLX(Object _Value) {
		SYTTLX.setvalue(_Value);
	}

	public PropertyInfo getYZYFS() {
		return YZYFS;
	}

	public void setYZYFS(Object _Value) {
		YZYFS.setvalue(_Value);
	}

	public PropertyInfo getCYZL() {
		return CYZL;
	}

	public void setCYZL(Object _Value) {
		CYZL.setvalue(_Value);
	}

	public PropertyInfo getSYZCL() {
		return SYZCL;
	}

	public void setSYZCL(Object _Value) {
		SYZCL.setvalue(_Value);
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
