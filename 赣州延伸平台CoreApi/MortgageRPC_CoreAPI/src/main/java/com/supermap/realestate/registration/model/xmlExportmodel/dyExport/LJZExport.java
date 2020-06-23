package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("LJZ")
public class LJZExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "逻辑幢";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo LJZH = new PropertyInfo("逻辑幢号");
	private PropertyInfo ZRZH = new PropertyInfo("自然幢号");
	private PropertyInfo MPH = new PropertyInfo("门牌号");
	private PropertyInfo YCJZMJ = new PropertyInfo("预测建筑面积");
	private PropertyInfo YCDXMJ = new PropertyInfo("预测地下面积");
	private PropertyInfo YCQTMJ = new PropertyInfo("预测其它面积");
	private PropertyInfo SCJZMJ = new PropertyInfo("实测建筑面积");
	private PropertyInfo SCDXMJ = new PropertyInfo("实测地下面积");
	private PropertyInfo SCQTMJ = new PropertyInfo("实测其它面积");
	private PropertyInfo JGRQ = new PropertyInfo("竣工日期");
	private PropertyInfo FWJG1 = new PropertyInfo("房屋结构1");
	private PropertyInfo FWJG1MC = new PropertyInfo("房屋结构1名称");
	private PropertyInfo FWJG2 = new PropertyInfo("房屋结构2");
	private PropertyInfo FWJG2MC = new PropertyInfo("房屋结构2名称");
	private PropertyInfo FWJG3 = new PropertyInfo("房屋结构3");
	private PropertyInfo FWJG3MC = new PropertyInfo("房屋结构3名称");
	private PropertyInfo JZWZT = new PropertyInfo("建筑物状态");
	private PropertyInfo JZWZTMC = new PropertyInfo("建筑物状态名称");
	private PropertyInfo FWYT1 = new PropertyInfo("房屋用途1");
	private PropertyInfo FWYT1MC = new PropertyInfo("房屋用途1名称");
	private PropertyInfo FWYT2 = new PropertyInfo("房屋用途2");
	private PropertyInfo FWYT2MC = new PropertyInfo("房屋用途2名称");
	private PropertyInfo FWYT3 = new PropertyInfo("房屋用途3");
	private PropertyInfo FWYT3MC = new PropertyInfo("房屋用途3名称");
	private PropertyInfo ZCS = new PropertyInfo("总层数");
	private PropertyInfo DSCS = new PropertyInfo("地上层数");
	private PropertyInfo DXCS = new PropertyInfo("地下层数");
	private PropertyInfo BZ = new PropertyInfo("备注");
	@XStreamAlias("GLID")
	private PropertyInfo RelationID = new PropertyInfo("关联ID");

	public PropertyInfo getRelationID() {
		return RelationID;
	}

	public void setRelationID(Object _Value) {
		RelationID.setvalue(_Value);
	}
	
	public PropertyInfo getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(Object _Value) {
		BDCDYID.setvalue(_Value);
	}

	public PropertyInfo getFWJG1MC() {
		return FWJG1MC;
	}

	public void setFWJG1MC(Object _Value) {
		FWJG1MC.setvalue(_Value);
	}

	public PropertyInfo getFWJG2MC() {
		return FWJG2MC;
	}

	public void setFWJG2MC(Object _Value) {
		FWJG2MC.setvalue(_Value);
	}

	public PropertyInfo getFWJG3MC() {
		return FWJG3MC;
	}

	public void setFWJG3MC(Object _Value) {
		FWJG3MC.setvalue(_Value);
	}

	public PropertyInfo getJZWZTMC() {
		return JZWZTMC;
	}

	public void setJZWZTMC(Object _Value) {
		JZWZTMC.setvalue(_Value);
	}

	public PropertyInfo getFWYT1MC() {
		return FWYT1MC;
	}

	public void setFWYT1MC(Object _Value) {
		FWYT1MC.setvalue(_Value);
	}

	public PropertyInfo getFWYT2MC() {
		return FWYT2MC;
	}

	public void setFWYT2MC(Object _Value) {
		FWYT2MC.setvalue(_Value);
	}

	public PropertyInfo getFWYT3MC() {
		return FWYT3MC;
	}

	public void setFWYT3MC(Object _Value) {
		FWYT3MC.setvalue(_Value);
	}

	public PropertyInfo getLJZH() {
		return LJZH;
	}

	public void setLJZH(Object _Value) {
		LJZH.setvalue(_Value);
	}

	public PropertyInfo getZRZH() {
		return ZRZH;
	}

	public void setZRZH(Object _Value) {
		ZRZH.setvalue(_Value);
	}

	public PropertyInfo getMPH() {
		return MPH;
	}

	public void setMPH(Object _Value) {
		MPH.setvalue(_Value);
	}

	public PropertyInfo getYCJZMJ() {
		return YCJZMJ;
	}

	public void setYCJZMJ(Object _Value) {
		YCJZMJ.setvalue(_Value);
	}

	public PropertyInfo getYCDXMJ() {
		return YCDXMJ;
	}

	public void setYCDXMJ(Object _Value) {
		YCDXMJ.setvalue(_Value);
	}

	public PropertyInfo getYCQTMJ() {
		return YCQTMJ;
	}

	public void setYCQTMJ(Object _Value) {
		YCQTMJ.setvalue(_Value);
	}

	public PropertyInfo getSCJZMJ() {
		return SCJZMJ;
	}

	public void setSCJZMJ(Object _Value) {
		SCJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCDXMJ() {
		return SCDXMJ;
	}

	public void setSCDXMJ(Object _Value) {
		SCDXMJ.setvalue(_Value);
	}

	public PropertyInfo getSCQTMJ() {
		return SCQTMJ;
	}

	public void setSCQTMJ(Object _Value) {
		SCQTMJ.setvalue(_Value);
	}

	public PropertyInfo getJGRQ() {
		return JGRQ;
	}

	public void setJGRQ(Object _Value) {
		JGRQ.setvalue(_Value);
	}

	public PropertyInfo getFWJG1() {
		return FWJG1;
	}

	public void setFWJG1(Object _Value) {
		FWJG1.setvalue(_Value);
	}

	public PropertyInfo getFWJG2() {
		return FWJG2;
	}

	public void setFWJG2(Object _Value) {
		FWJG2.setvalue(_Value);
	}

	public PropertyInfo getFWJG3() {
		return FWJG3;
	}

	public void setFWJG3(Object _Value) {
		FWJG3.setvalue(_Value);
	}

	public PropertyInfo getJZWZT() {
		return JZWZT;
	}

	public void setJZWZT(Object _Value) {
		JZWZT.setvalue(_Value);
	}

	public PropertyInfo getFWYT1() {
		return FWYT1;
	}

	public void setFWYT1(Object _Value) {
		FWYT1.setvalue(_Value);
	}

	public PropertyInfo getFWYT2() {
		return FWYT2;
	}

	public void setFWYT2(Object _Value) {
		FWYT2.setvalue(_Value);
	}

	public PropertyInfo getFWYT3() {
		return FWYT3;
	}

	public void setFWYT3(Object _Value) {
		FWYT3.setvalue(_Value);
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

	public PropertyInfo getBZ() {
		return BZ;
	}

	public void setBZ(Object _Value) {
		BZ.setvalue(_Value);
	}
}
