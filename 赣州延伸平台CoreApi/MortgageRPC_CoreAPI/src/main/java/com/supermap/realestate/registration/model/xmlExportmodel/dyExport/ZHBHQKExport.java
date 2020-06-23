package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("ZHBHQK")
public class ZHBHQKExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "宗海变化情况";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo ZHDM = new PropertyInfo("宗海代码");
	private PropertyInfo BHYY = new PropertyInfo("变化原因");
	private PropertyInfo BHNR = new PropertyInfo("变化内容");
	private PropertyInfo DJSJ = new PropertyInfo("登记时间");
	private PropertyInfo DBR = new PropertyInfo("登簿人");
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

	public PropertyInfo getZHDM() {
		return ZHDM;
	}

	public void setZHDM(Object _Value) {
		ZHDM.setvalue(_Value);
	}

	public PropertyInfo getBHYY() {
		return BHYY;
	}

	public void setBHYY(Object _Value) {
		BHYY.setvalue(_Value);
	}

	public PropertyInfo getBHNR() {
		return BHNR;
	}

	public void setBHNR(Object _Value) {
		BHNR.setvalue(_Value);
	}

	public PropertyInfo getDJSJ() {
		return DJSJ;
	}

	public void setDJSJ(Object _Value) {
		DJSJ.setvalue(_Value);
	}

	public PropertyInfo getDBR() {
		return DBR;
	}

	public void setDBR(Object _Value) {
		DBR.setvalue(_Value);
	}
}
