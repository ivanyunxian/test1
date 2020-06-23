package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("GZW")
public class GZWExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "构筑物";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo ZDDM = new PropertyInfo("宗地代码");
	private PropertyInfo GZWMC = new PropertyInfo("构筑物名称");
	private PropertyInfo ZL = new PropertyInfo("坐落");
	private PropertyInfo MJDW = new PropertyInfo("面积单位");
	private PropertyInfo MJDWMC = new PropertyInfo("面积单位名称");
	private PropertyInfo MJ = new PropertyInfo("面积");
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
	
	public PropertyInfo getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(Object _Value) {
		BDCDYID.setvalue(_Value);
	}

	public PropertyInfo getMJDWMC() {
		return MJDWMC;
	}

	public void setMJDWMC(Object _Value) {
		MJDWMC.setvalue(_Value);
	}

	public PropertyInfo getZTMC() {
		return ZTMC;
	}

	public void setZTMC(Object _Value) {
		ZTMC.setvalue(_Value);
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

	public PropertyInfo getGZWMC() {
		return GZWMC;
	}

	public void setGZWMC(Object _Value) {
		GZWMC.setvalue(_Value);
	}

	public PropertyInfo getZL() {
		return ZL;
	}

	public void setZL(Object _Value) {
		ZL.setvalue(_Value);
	}

	public PropertyInfo getMJDW() {
		return MJDW;
	}

	public void setMJDW(Object _Value) {
		MJDW.setvalue(_Value);
	}

	public PropertyInfo getMJ() {
		return MJ;
	}

	public void setMJ(Object _Value) {
		MJ.setvalue(_Value);
	}

	public PropertyInfo getZT() {
		return ZT;
	}

	public void setZT(Object _Value) {
		ZT.setvalue(_Value);
	}
}
