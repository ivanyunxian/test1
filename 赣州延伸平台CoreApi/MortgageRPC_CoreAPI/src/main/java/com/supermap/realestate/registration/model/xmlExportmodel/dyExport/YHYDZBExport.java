package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("YHYDZB")
public class YHYDZBExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "用海用地坐标";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo ZHHDDM = new PropertyInfo("宗海/海岛代码");
	private PropertyInfo XH = new PropertyInfo("序号");
	private PropertyInfo BW = new PropertyInfo("Y坐标");
	private PropertyInfo DJ = new PropertyInfo("X坐标");
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

	public PropertyInfo getZHHDDM() {
		return ZHHDDM;
	}

	public void setZHHDDM(Object _Value) {
		ZHHDDM.setvalue(_Value);
	}

	public PropertyInfo getXH() {
		return XH;
	}

	public void setXH(Object _Value) {
		XH.setvalue(_Value);
	}

	public PropertyInfo getBW() {
		return BW;
	}

	public void setBW(Object _Value) {
		BW.setvalue(_Value);
	}

	public PropertyInfo getDJ() {
		return DJ;
	}

	public void setDJ(Object _Value) {
		DJ.setvalue(_Value);
	}

}
