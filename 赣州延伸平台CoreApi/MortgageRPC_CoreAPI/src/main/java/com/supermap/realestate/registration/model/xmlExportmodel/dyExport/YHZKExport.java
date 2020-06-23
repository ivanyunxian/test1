package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("YHZK")
public class YHZKExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "用海状况";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo ZHDM = new PropertyInfo("宗海代码");
	private PropertyInfo YHFS = new PropertyInfo("用海方式");
	private PropertyInfo YHFSMC = new PropertyInfo("用海方式名称");
	private PropertyInfo YHMJ = new PropertyInfo("用海面积");
	private PropertyInfo JTYT = new PropertyInfo("具体用途");
	private PropertyInfo SYJSE = new PropertyInfo("使用金数额");
	@XStreamAlias("GLID")
	private PropertyInfo RelationID = new PropertyInfo("关联ID");

	public PropertyInfo getRelationID() {
		return RelationID;
	}

	public void setRelationID(Object _Value) {
		RelationID.setvalue(_Value);
	}
	
	public PropertyInfo getZHDM() {
		return ZHDM;
	}

	public void setZHDM(Object _Value) {
		ZHDM.setvalue(_Value);
	}

	public PropertyInfo getYHFS() {
		return YHFS;
	}

	public void setYHFS(Object _Value) {
		YHFS.setvalue(_Value);
	}

	public PropertyInfo getYHMJ() {
		return YHMJ;
	}

	public void setYHMJ(Object _Value) {
		YHMJ.setvalue(_Value);
	}

	public PropertyInfo getJTYT() {
		return JTYT;
	}

	public void setJTYT(Object _Value) {
		JTYT.setvalue(_Value);
	}

	public PropertyInfo getSYJSE() {
		return SYJSE;
	}

	public void setSYJSE(Object _Value) {
		SYJSE.setvalue(_Value);
	}

	public PropertyInfo getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(Object _Value) {
		BDCDYID.setvalue(_Value);
	}

	public PropertyInfo getYHFSMC() {
		return YHFSMC;
	}

	public void setYHFSMC(Object _Value) {
		YHFSMC.setvalue(_Value);
	}
}
