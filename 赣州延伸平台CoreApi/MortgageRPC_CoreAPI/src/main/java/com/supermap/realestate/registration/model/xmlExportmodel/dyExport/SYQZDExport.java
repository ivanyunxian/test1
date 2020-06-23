package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("SYQZD")
public class SYQZDExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "所有权宗地";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo ZDDM = new PropertyInfo("宗地代码");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo ZDTZM = new PropertyInfo("宗地特征码");
	private PropertyInfo ZDTZMMC = new PropertyInfo("宗地特征码名称");
	private PropertyInfo ZL = new PropertyInfo("坐落");
	private PropertyInfo ZDMJ = new PropertyInfo("宗地面积");
	private PropertyInfo MJDW = new PropertyInfo("面积单位");
	private PropertyInfo MJDWMC = new PropertyInfo("面积单位名称");
	private PropertyInfo YT = new PropertyInfo("用途");
	private PropertyInfo DJ = new PropertyInfo("等级");
	private PropertyInfo DJMC = new PropertyInfo("土地等级名称");
	private PropertyInfo JG = new PropertyInfo("价格");
	private PropertyInfo QLLX = new PropertyInfo("权利类型");
	private PropertyInfo QLLXMC = new PropertyInfo("权利类型名称");
	private PropertyInfo QLXZ = new PropertyInfo("权利性质");
	private PropertyInfo QLXZMC = new PropertyInfo("权利性质名称");
	private PropertyInfo QLSDFS = new PropertyInfo("权利设定方式");
	private PropertyInfo QLSDFSMC = new PropertyInfo("权利设定方式名称");
	private PropertyInfo RJL = new PropertyInfo("容积率");
	private PropertyInfo JZMD = new PropertyInfo("建筑密度");
	private PropertyInfo JZXG = new PropertyInfo("建筑限高");
	private PropertyInfo ZDSZD = new PropertyInfo("宗地四至-东");
	private PropertyInfo ZDSZN = new PropertyInfo("宗地四至-南");
	private PropertyInfo ZDSZX = new PropertyInfo("宗地四至-西");
	private PropertyInfo ZDSZB = new PropertyInfo("宗地四至-北");
	private PropertyInfo ZDT = new PropertyInfo("宗地图");
	private PropertyInfo TFH = new PropertyInfo("图幅号");
	private PropertyInfo DJH = new PropertyInfo("地籍号");
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

	public PropertyInfo getZDDM() {
		return ZDDM;
	}

	public void setZDDM(Object _Value) {
		ZDDM.setvalue(_Value);
	}

	public PropertyInfo getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(Object _Value) {
		BDCDYH.setvalue(_Value);
	}

	public PropertyInfo getZDTZM() {
		return ZDTZM;
	}

	public void setZDTZM(Object _Value) {
		ZDTZM.setvalue(_Value);
	}

	public PropertyInfo getZDTZMMC() {
		return ZDTZMMC;
	}

	public void setZDTZMMC(Object _Value) {
		ZDTZMMC.setvalue(_Value);
	}

	public PropertyInfo getZL() {
		return ZL;
	}

	public void setZL(Object _Value) {
		ZL.setvalue(_Value);
	}

	public PropertyInfo getZDMJ() {
		return ZDMJ;
	}

	public void setZDMJ(Object _Value) {
		ZDMJ.setvalue(_Value);
	}

	public PropertyInfo getMJDW() {
		return MJDW;
	}

	public void setMJDW(Object _Value) {
		MJDW.setvalue(_Value);
	}

	public PropertyInfo getMJDWMC() {
		return MJDWMC;
	}

	public void setMJDWMC(Object _Value) {
		MJDWMC.setvalue(_Value);
	}

	public PropertyInfo getYT() {
		return YT;
	}

	public void setYT(Object _Value) {
		YT.setvalue(_Value);
	}

	public PropertyInfo getDJ() {
		return DJ;
	}

	public void setDJ(Object _Value) {
		DJ.setvalue(_Value);
	}

	public PropertyInfo getDJMC() {
		return DJMC;
	}

	public void setDJMC(Object _Value) {
		DJMC.setvalue(_Value);
	}

	public PropertyInfo getJG() {
		return JG;
	}

	public void setJG(Object _Value) {
		JG.setvalue(_Value);
	}

	public PropertyInfo getQLLX() {
		return QLLX;
	}

	public void setQLLX(Object _Value) {
		QLLX.setvalue(_Value);
	}

	public PropertyInfo getQLLXMC() {
		return QLLXMC;
	}

	public void setQLLXMC(Object _Value) {
		QLLXMC.setvalue(_Value);
	}

	public PropertyInfo getQLXZ() {
		return QLXZ;
	}

	public void setQLXZ(Object _Value) {
		QLXZ.setvalue(_Value);
	}

	public PropertyInfo getQLXZMC() {
		return QLXZMC;
	}

	public void setQLXZMC(Object _Value) {
		QLXZMC.setvalue(_Value);
	}

	public PropertyInfo getQLSDFS() {
		return QLSDFS;
	}

	public void setQLSDFS(Object _Value) {
		QLSDFS.setvalue(_Value);
	}

	public PropertyInfo getQLSDFSMC() {
		return QLSDFSMC;
	}

	public void setQLSDFSMC(Object _Value) {
		QLSDFSMC.setvalue(_Value);
	}

	public PropertyInfo getRJL() {
		return RJL;
	}

	public void setRJL(Object _Value) {
		RJL.setvalue(_Value);
	}

	public PropertyInfo getJZMD() {
		return JZMD;
	}

	public void setJZMD(Object _Value) {
		JZMD.setvalue(_Value);
	}

	public PropertyInfo getJZXG() {
		return JZXG;
	}

	public void setJZXG(Object _Value) {
		JZXG.setvalue(_Value);
	}

	public PropertyInfo getZDSZD() {
		return ZDSZD;
	}

	public void setZDSZD(Object _Value) {
		ZDSZD.setvalue(_Value);
	}

	public PropertyInfo getZDSZN() {
		return ZDSZN;
	}

	public void setZDSZN(Object _Value) {
		ZDSZN.setvalue(_Value);
	}

	public PropertyInfo getZDSZX() {
		return ZDSZX;
	}

	public void setZDSZX(Object _Value) {
		ZDSZX.setvalue(_Value);
	}

	public PropertyInfo getZDSZB() {
		return ZDSZB;
	}

	public void setZDSZB(Object _Value) {
		ZDSZB.setvalue(_Value);
	}

	public PropertyInfo getZDT() {
		return ZDT;
	}

	public void setZDT(Object _Value) {
		ZDT.setvalue(_Value);
	}

	public PropertyInfo getTFH() {
		return TFH;
	}

	public void setTFH(Object _Value) {
		TFH.setvalue(_Value);
	}

	public PropertyInfo getDJH() {
		return DJH;
	}

	public void setDJH(Object _Value) {
		DJH.setvalue(_Value);
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
