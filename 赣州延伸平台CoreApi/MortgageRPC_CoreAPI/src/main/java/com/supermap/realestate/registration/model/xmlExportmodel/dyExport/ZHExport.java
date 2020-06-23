package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("ZH")
public class ZHExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "宗海";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo ZHDM = new PropertyInfo("宗海代码");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo ZHTZM = new PropertyInfo("宗海特征码");
	private PropertyInfo ZHTZMMC = new PropertyInfo("宗海特征码名称");
	private PropertyInfo XMMC = new PropertyInfo("项目名称");
	private PropertyInfo XMXZ = new PropertyInfo("项目性质");
	private PropertyInfo XMXZMC = new PropertyInfo("项目性质名称");
	private PropertyInfo YHZMJ = new PropertyInfo("用海总面积");
	private PropertyInfo ZHMJ = new PropertyInfo("宗海面积");
	private PropertyInfo DB = new PropertyInfo("等别");
	private PropertyInfo DBMC = new PropertyInfo("海域等别名称");
	private PropertyInfo ZHAX = new PropertyInfo("占海岸线");
	private PropertyInfo YHLXA = new PropertyInfo("用海类型A");
	private PropertyInfo YHLXAMC = new PropertyInfo("用海类型A名称");
	private PropertyInfo YHLXB = new PropertyInfo("用海类型B");
	private PropertyInfo YHLXBMC = new PropertyInfo("用海类型B名称");
	private PropertyInfo YHWZSM = new PropertyInfo("用海位置说明");
	private PropertyInfo HDMC = new PropertyInfo("海岛名称");
	private PropertyInfo HDDM = new PropertyInfo("海岛代码");
	private PropertyInfo YDFW = new PropertyInfo("用岛范围");
	private PropertyInfo YDMJ = new PropertyInfo("用岛面积");
	private PropertyInfo HDWZ = new PropertyInfo("海岛位置");
	private PropertyInfo HDYT = new PropertyInfo("海岛用途");
	private PropertyInfo ZHT = new PropertyInfo("宗海图");
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

	public PropertyInfo getZHDM() {
		return ZHDM;
	}

	public void setZHDM(Object _Value) {
		ZHDM.setvalue(_Value);
	}

	public PropertyInfo getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(Object _Value) {
		BDCDYH.setvalue(_Value);
	}

	public PropertyInfo getZHTZM() {
		return ZHTZM;
	}

	public void setZHTZM(Object _Value) {
		ZHTZM.setvalue(_Value);
	}

	public PropertyInfo getZHTZMMC() {
		return ZHTZMMC;
	}

	public void setZHTZMMC(Object _Value) {
		ZHTZMMC.setvalue(_Value);
	}

	public PropertyInfo getXMMC() {
		return XMMC;
	}

	public void setXMMC(Object _Value) {
		XMMC.setvalue(_Value);
	}

	public PropertyInfo getXMXZ() {
		return XMXZ;
	}

	public void setXMXZ(Object _Value) {
		XMXZ.setvalue(_Value);
	}

	public PropertyInfo getXMXZMC() {
		return XMXZMC;
	}

	public void setXMXZMC(Object _Value) {
		XMXZMC.setvalue(_Value);
	}

	public PropertyInfo getYHZMJ() {
		return YHZMJ;
	}

	public void setYHZMJ(Object _Value) {
		YHZMJ.setvalue(_Value);
	}

	public PropertyInfo getZHMJ() {
		return ZHMJ;
	}

	public void setZHMJ(Object _Value) {
		ZHMJ.setvalue(_Value);
	}

	public PropertyInfo getDB() {
		return DB;
	}

	public void setDB(Object _Value) {
		DB.setvalue(_Value);
	}

	public PropertyInfo getDBMC() {
		return DBMC;
	}

	public void setDBMC(Object _Value) {
		DBMC.setvalue(_Value);
	}

	public PropertyInfo getZHAX() {
		return ZHAX;
	}

	public void setZHAX(Object _Value) {
		ZHAX.setvalue(_Value);
	}

	public PropertyInfo getYHLXA() {
		return YHLXA;
	}

	public void setYHLXA(Object _Value) {
		YHLXA.setvalue(_Value);
	}

	public PropertyInfo getYHLXAMC() {
		return YHLXAMC;
	}

	public void setYHLXAMC(Object _Value) {
		YHLXAMC.setvalue(_Value);
	}

	public PropertyInfo getYHLXB() {
		return YHLXB;
	}

	public void setYHLXB(Object _Value) {
		YHLXB.setvalue(_Value);
	}

	public PropertyInfo getYHLXBMC() {
		return YHLXBMC;
	}

	public void setYHLXBMC(Object _Value) {
		YHLXBMC.setvalue(_Value);
	}

	public PropertyInfo getYHWZSM() {
		return YHWZSM;
	}

	public void setYHWZSM(Object _Value) {
		YHWZSM.setvalue(_Value);
	}

	public PropertyInfo getHDMC() {
		return HDMC;
	}

	public void setHDMC(Object _Value) {
		HDMC.setvalue(_Value);
	}

	public PropertyInfo getHDDM() {
		return HDDM;
	}

	public void setHDDM(Object _Value) {
		HDDM.setvalue(_Value);
	}

	public PropertyInfo getYDFW() {
		return YDFW;
	}

	public void setYDFW(Object _Value) {
		YDFW.setvalue(_Value);
	}

	public PropertyInfo getYDMJ() {
		return YDMJ;
	}

	public void setYDMJ(Object _Value) {
		YDMJ.setvalue(_Value);
	}

	public PropertyInfo getHDWZ() {
		return HDWZ;
	}

	public void setHDWZ(Object _Value) {
		HDWZ.setvalue(_Value);
	}

	public PropertyInfo getHDYT() {
		return HDYT;
	}

	public void setHDYT(Object _Value) {
		HDYT.setvalue(_Value);
	}

	public PropertyInfo getZHT() {
		return ZHT;
	}

	public void setZHT(Object _Value) {
		ZHT.setvalue(_Value);
	}

	public PropertyInfo getZT() {
		return ZT;
	}

	public void setZT(Object _Value) {
		ZT.setvalue(_Value);
	}

	public PropertyInfo getZTMC() {
		return ZTMC;
	}

	public void setZTMC(Object _Value) {
		ZTMC.setvalue(_Value);
	}
}
