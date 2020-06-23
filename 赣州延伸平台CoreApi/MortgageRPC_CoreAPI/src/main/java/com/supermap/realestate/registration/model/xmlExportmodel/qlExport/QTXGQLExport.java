package com.supermap.realestate.registration.model.xmlExportmodel.qlExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("QTXGQL")
public class QTXGQLExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "其它相关权利";

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
	private PropertyInfo QLQSSJ = new PropertyInfo("权利起始时间");
	private PropertyInfo QLJSSJ = new PropertyInfo("权利结束时间");
	private PropertyInfo QSFS = new PropertyInfo("取水方式");
	private PropertyInfo SYLX = new PropertyInfo("水源类型");
	private PropertyInfo QSL = new PropertyInfo("取水量");
	private PropertyInfo QSYT = new PropertyInfo("取水用途");
	private PropertyInfo KCMJ = new PropertyInfo("勘查面积");
	private PropertyInfo KCFS = new PropertyInfo("开采方式");
	private PropertyInfo KCKZ = new PropertyInfo("开采矿种");
	private PropertyInfo SCGM = new PropertyInfo("生产规模");
	private PropertyInfo BDCQZH = new PropertyInfo("不动产权证号");
	private PropertyInfo FJ = new PropertyInfo("附记");
	private PropertyInfo FT = new PropertyInfo("附图");
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

	public PropertyInfo getQSFS() {
		return QSFS;
	}

	public void setQSFS(Object _Value) {
		QSFS.setvalue(_Value);
	}

	public PropertyInfo getSYLX() {
		return SYLX;
	}

	public void setSYLX(Object _Value) {
		SYLX.setvalue(_Value);
	}

	public PropertyInfo getQSL() {
		return QSL;
	}

	public void setQSL(Object _Value) {
		QSL.setvalue(_Value);
	}

	public PropertyInfo getQSYT() {
		return QSYT;
	}

	public void setQSYT(Object _Value) {
		QSYT.setvalue(_Value);
	}

	public PropertyInfo getKCMJ() {
		return KCMJ;
	}

	public void setKCMJ(Object _Value) {
		KCMJ.setvalue(_Value);
	}

	public PropertyInfo getKCFS() {
		return KCFS;
	}

	public void setKCFS(Object _Value) {
		KCFS.setvalue(_Value);
	}

	public PropertyInfo getKCKZ() {
		return KCKZ;
	}

	public void setKCKZ(Object _Value) {
		KCKZ.setvalue(_Value);
	}

	public PropertyInfo getSCGM() {
		return SCGM;
	}

	public void setSCGM(Object _Value) {
		SCGM.setvalue(_Value);
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

	public PropertyInfo getFT() {
		return FT;
	}

	public void setFT(Object _Value) {
		FT.setvalue(_Value);
	}

	public PropertyInfo getQSZT() {
		return QSZT;
	}

	public void setQSZT(Object _Value) {
		QSZT.setvalue(_Value);
	}
}
