package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("ZRZ")
public class ZRZExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "自然幢";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo ZDDM = new PropertyInfo("宗地代码");
	private PropertyInfo ZRZH = new PropertyInfo("自然幢号");
	private PropertyInfo XMMC = new PropertyInfo("项目名称");
	private PropertyInfo JZWMC = new PropertyInfo("建筑物名称");
	private PropertyInfo JGRQ = new PropertyInfo("竣工日期");
	private PropertyInfo JZWGD = new PropertyInfo("建筑物高度");
	private PropertyInfo ZZDMJ = new PropertyInfo("幢占地面积");
	private PropertyInfo ZYDMJ = new PropertyInfo("幢用地面积");
	private PropertyInfo YCJZMJ = new PropertyInfo("预测建筑面积");
	private PropertyInfo SCJZMJ = new PropertyInfo("实测建筑面积");
	private PropertyInfo ZCS = new PropertyInfo("总层数");
	private PropertyInfo DSCS = new PropertyInfo("地上层数");
	private PropertyInfo DXCS = new PropertyInfo("地下层数");
	private PropertyInfo DXSD = new PropertyInfo("地下深度");
	private PropertyInfo GHYT = new PropertyInfo("规划用途");
	private PropertyInfo GHYTMC = new PropertyInfo("规划用途名称");
	private PropertyInfo FWJG = new PropertyInfo("房屋结构");
	private PropertyInfo FWJGMC = new PropertyInfo("房屋结构名称");
	private PropertyInfo ZTS = new PropertyInfo("总套数");
	private PropertyInfo JZWJBYT = new PropertyInfo("建筑物基本用途");
	private PropertyInfo BZ = new PropertyInfo("备注");
	private PropertyInfo ZT = new PropertyInfo("状态");
	private PropertyInfo ZTMC = new PropertyInfo("状态名称");
	@XStreamAlias("GLID")
	private PropertyInfo RelationID = new PropertyInfo("关联ID");

	public PropertyInfo getRelationID() {
		return RelationID;
	}

	public void setRelationID(Object _Value) {
		RelationID.setvalue(_Value);
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

	public PropertyInfo getZRZH() {
		return ZRZH;
	}

	public void setZRZH(Object _Value) {
		ZRZH.setvalue(_Value);
	}

	public PropertyInfo getXMMC() {
		return XMMC;
	}

	public void setXMMC(Object _Value) {
		XMMC.setvalue(_Value);
	}

	public PropertyInfo getJZWMC() {
		return JZWMC;
	}

	public void setJZWMC(Object _Value) {
		JZWMC.setvalue(_Value);
	}

	public PropertyInfo getJGRQ() {
		return JGRQ;
	}

	public void setJGRQ(Object _Value) {
		JGRQ.setvalue(_Value);
	}

	public PropertyInfo getJZWGD() {
		return JZWGD;
	}

	public void setJZWGD(Object _Value) {
		JZWGD.setvalue(_Value);
	}

	public PropertyInfo getZZDMJ() {
		return ZZDMJ;
	}

	public void setZZDMJ(Object _Value) {
		ZZDMJ.setvalue(_Value);
	}

	public PropertyInfo getZYDMJ() {
		return ZYDMJ;
	}

	public void setZYDMJ(Object _Value) {
		ZYDMJ.setvalue(_Value);
	}

	public PropertyInfo getYCJZMJ() {
		return YCJZMJ;
	}

	public void setYCJZMJ(Object _Value) {
		YCJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCJZMJ() {
		return SCJZMJ;
	}

	public void setSCJZMJ(Object _Value) {
		SCJZMJ.setvalue(_Value);
	}

	public PropertyInfo getZCS() {
		return ZCS;
	}

	public void setZCS(Object _Value) {
		ZCS.setvalue(_Value);
	}

	public PropertyInfo getDSCS() {
		return DSCS;
	}

	public void setDSCS(Object _Value) {
		DSCS.setvalue(_Value);
	}

	public PropertyInfo getDXCS() {
		return DXCS;
	}

	public void setDXCS(Object _Value) {
		DXCS.setvalue(_Value);
	}

	public PropertyInfo getDXSD() {
		return DXSD;
	}

	public void setDXSD(Object _Value) {
		DXSD.setvalue(_Value);
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

	public PropertyInfo getZTS() {
		return ZTS;
	}

	public void setZTS(Object _Value) {
		ZTS.setvalue(_Value);
	}

	public PropertyInfo getJZWJBYT() {
		return JZWJBYT;
	}

	public void setJZWJBYT(Object _Value) {
		JZWJBYT.setvalue(_Value);
	}

	public PropertyInfo getBZ() {
		return BZ;
	}

	public void setBZ(Object _Value) {
		BZ.setvalue(_Value);
	}

	public PropertyInfo getZT() {
		return ZT;
	}

	public void setZT(Object _Value) {
		ZT.setvalue(_Value);
	}

	public PropertyInfo getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(Object _Value) {
		BDCDYID.setvalue(_Value);
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

	public PropertyInfo getZTMC() {
		return ZTMC;
	}

	public void setZTMC(Object _Value) {
		ZTMC.setvalue(_Value);
	}
}
