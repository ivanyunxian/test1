package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("H")
public class HExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "户";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo BDCDYH = new PropertyInfo("不动产单元号");
	private PropertyInfo FWBM = new PropertyInfo("房屋编码");
	private PropertyInfo ZRZH = new PropertyInfo("自然幢号");
	private PropertyInfo LJZH = new PropertyInfo("逻辑幢号");
	private PropertyInfo CH = new PropertyInfo("层号");
	private PropertyInfo ZL = new PropertyInfo("坐落");
	private PropertyInfo MJDW = new PropertyInfo("面积单位");
	private PropertyInfo MJDWMC = new PropertyInfo("面积单位名称");
	private PropertyInfo SJCS = new PropertyInfo("实际层数");
	private PropertyInfo HH = new PropertyInfo("户号");
	private PropertyInfo SHBW = new PropertyInfo("室户部位");
	private PropertyInfo HX = new PropertyInfo("户型");
	private PropertyInfo HXMC = new PropertyInfo("户型名称");
	private PropertyInfo HXJG = new PropertyInfo("户型结构");
	private PropertyInfo HXJGMC = new PropertyInfo("户型结构名称");
	private PropertyInfo FWYT1 = new PropertyInfo("房屋用途1");
	private PropertyInfo FWYT1MC = new PropertyInfo("房屋用途1名称");
	private PropertyInfo FWYT2 = new PropertyInfo("房屋用途2");
	private PropertyInfo FWYT2MC = new PropertyInfo("房屋用途2名称");
	private PropertyInfo FWYT3 = new PropertyInfo("房屋用途3");
	private PropertyInfo FWYT3MC = new PropertyInfo("房屋用途3名称");
	private PropertyInfo YCJZMJ = new PropertyInfo("预测建筑面积");
	private PropertyInfo YCTNJZMJ = new PropertyInfo("预测套内建筑面积");
	private PropertyInfo YCFTJZMJ = new PropertyInfo("预测分摊建筑面积");
	private PropertyInfo YCDXBFJZMJ = new PropertyInfo("预测地下部分建筑面积");
	private PropertyInfo YCQTJZMJ = new PropertyInfo("预测其他建筑面积");
	private PropertyInfo YCFTXS = new PropertyInfo("预测分摊系数");
	private PropertyInfo SCJZMJ = new PropertyInfo("实测建筑面积");
	private PropertyInfo SCTNJZMJ = new PropertyInfo("实测套内建筑面积");
	private PropertyInfo SCFTJZMJ = new PropertyInfo("实测分摊建筑面积");
	private PropertyInfo SCDXBFJZMJ = new PropertyInfo("实测地下部分建筑面积");
	private PropertyInfo SCQTJZMJ = new PropertyInfo("实测其他建筑面积");
	private PropertyInfo SCFTXS = new PropertyInfo("实测分摊系数");
	private PropertyInfo GYTDMJ = new PropertyInfo("共用建筑面积");
	private PropertyInfo FTTDMJ = new PropertyInfo("分摊建筑面积");
	private PropertyInfo DYTDMJ = new PropertyInfo("独用建筑面积");
	private PropertyInfo FWLX = new PropertyInfo("房屋类型");
	private PropertyInfo FWLXMC = new PropertyInfo("房屋类型名称");
	private PropertyInfo FWXZ = new PropertyInfo("房屋性质");
	private PropertyInfo FWXZMC = new PropertyInfo("房屋性质名称");
	private PropertyInfo FCFHT = new PropertyInfo("房屋分户图");
	private PropertyInfo ZT = new PropertyInfo("状态");
	private PropertyInfo ZTMC = new PropertyInfo("状态名称");
	private PropertyInfo XZZT = new PropertyInfo("限制状态");
	@XStreamAlias("GLID")
	private PropertyInfo RelationID = new PropertyInfo("关联ID");

	public PropertyInfo getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(Object _Value) {
		BDCDYID.setvalue(_Value);
	}

	public PropertyInfo getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(Object _Value) {
		BDCDYH.setvalue(_Value);
	}

	public PropertyInfo getFWBM() {
		return FWBM;
	}

	public void setFWBM(Object _Value) {
		FWBM.setvalue(_Value);
	}

	public PropertyInfo getZRZH() {
		return ZRZH;
	}

	public void setZRZH(Object _Value) {
		ZRZH.setvalue(_Value);
	}

	public PropertyInfo getLJZH() {
		return LJZH;
	}

	public void setLJZH(Object _Value) {
		LJZH.setvalue(_Value);
	}

	public PropertyInfo getCH() {
		return CH;
	}

	public void setCH(Object _Value) {
		CH.setvalue(_Value);
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

	public PropertyInfo getMJDWMC() {
		return MJDWMC;
	}

	public void setMJDWMC(Object _Value) {
		MJDWMC.setvalue(_Value);
	}

	public PropertyInfo getSJCS() {
		return SJCS;
	}

	public void setSJCS(Object _Value) {
		SJCS.setvalue(_Value);
	}

	public PropertyInfo getHH() {
		return HH;
	}

	public void setHH(Object _Value) {
		HH.setvalue(_Value);
	}

	public PropertyInfo getSHBW() {
		return SHBW;
	}

	public void setSHBW(Object _Value) {
		SHBW.setvalue(_Value);
	}

	public PropertyInfo getHX() {
		return HX;
	}

	public void setHX(Object _Value) {
		HX.setvalue(_Value);
	}

	public PropertyInfo getHXMC() {
		return HXMC;
	}

	public void setHXMC(Object _Value) {
		HXMC.setvalue(_Value);
	}

	public PropertyInfo getHXJG() {
		return HXJG;
	}

	public void setHXJG(Object _Value) {
		HXJG.setvalue(_Value);
	}

	public PropertyInfo getHXJGMC() {
		return HXJGMC;
	}

	public void setHXJGMC(Object _Value) {
		HXJGMC.setvalue(_Value);
	}

	public PropertyInfo getFWYT1() {
		return FWYT1;
	}

	public void setFWYT1(Object _Value) {
		FWYT1.setvalue(_Value);
	}

	public PropertyInfo getFWYT1MC() {
		return FWYT1MC;
	}

	public void setFWYT1MC(Object _Value) {
		FWYT1MC.setvalue(_Value);
	}

	public PropertyInfo getFWYT2() {
		return FWYT2;
	}

	public void setFWYT2(Object _Value) {
		FWYT2.setvalue(_Value);
	}

	public PropertyInfo getFWYT2MC() {
		return FWYT2MC;
	}

	public void setFWYT2MC(Object _Value) {
		FWYT2MC.setvalue(_Value);
	}

	public PropertyInfo getFWYT3() {
		return FWYT3;
	}

	public void setFWYT3(Object _Value) {
		FWYT3.setvalue(_Value);
	}

	public PropertyInfo getFWYT3MC() {
		return FWYT3MC;
	}

	public void setFWYT3MC(Object _Value) {
		FWYT3MC.setvalue(_Value);
	}

	public PropertyInfo getYCJZMJ() {
		return YCJZMJ;
	}

	public void setYCJZMJ(Object _Value) {
		YCJZMJ.setvalue(_Value);
	}

	public PropertyInfo getYCTNJZMJ() {
		return YCTNJZMJ;
	}

	public void setYCTNJZMJ(Object _Value) {
		YCTNJZMJ.setvalue(_Value);
	}

	public PropertyInfo getYCFTJZMJ() {
		return YCFTJZMJ;
	}

	public void setYCFTJZMJ(Object _Value) {
		YCFTJZMJ.setvalue(_Value);
	}

	public PropertyInfo getYCDXBFJZMJ() {
		return YCDXBFJZMJ;
	}

	public void setYCDXBFJZMJ(Object _Value) {
		YCDXBFJZMJ.setvalue(_Value);
	}

	public PropertyInfo getYCQTJZMJ() {
		return YCQTJZMJ;
	}

	public void setYCQTJZMJ(Object _Value) {
		YCQTJZMJ.setvalue(_Value);
	}

	public PropertyInfo getYCFTXS() {
		return YCFTXS;
	}

	public void setYCFTXS(Object _Value) {
		YCFTXS.setvalue(_Value);
	}

	public PropertyInfo getSCJZMJ() {
		return SCJZMJ;
	}

	public void setSCJZMJ(Object _Value) {
		SCJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCTNJZMJ() {
		return SCTNJZMJ;
	}

	public void setSCTNJZMJ(Object _Value) {
		SCTNJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCFTJZMJ() {
		return SCFTJZMJ;
	}

	public void setSCFTJZMJ(Object _Value) {
		SCFTJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCDXBFJZMJ() {
		return SCDXBFJZMJ;
	}

	public void setSCDXBFJZMJ(Object _Value) {
		SCDXBFJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCQTJZMJ() {
		return SCQTJZMJ;
	}

	public void setSCQTJZMJ(Object _Value) {
		SCQTJZMJ.setvalue(_Value);
	}

	public PropertyInfo getSCFTXS() {
		return SCFTXS;
	}

	public void setSCFTXS(Object _Value) {
		SCFTXS.setvalue(_Value);
	}

	public PropertyInfo getGYTDMJ() {
		return GYTDMJ;
	}

	public void setGYTDMJ(Object _Value) {
		GYTDMJ.setvalue(_Value);
	}

	public PropertyInfo getFTTDMJ() {
		return FTTDMJ;
	}

	public void setFTTDMJ(Object _Value) {
		FTTDMJ.setvalue(_Value);
	}

	public PropertyInfo getDYTDMJ() {
		return DYTDMJ;
	}

	public void setDYTDMJ(Object _Value) {
		DYTDMJ.setvalue(_Value);
	}

	public PropertyInfo getFWLX() {
		return FWLX;
	}

	public void setFWLX(Object _Value) {
		FWLX.setvalue(_Value);
	}

	public PropertyInfo getFWLXMC() {
		return FWLXMC;
	}

	public void setFWLXMC(Object _Value) {
		FWLXMC.setvalue(_Value);
	}

	public PropertyInfo getFWXZ() {
		return FWXZ;
	}

	public void setFWXZ(Object _Value) {
		FWXZ.setvalue(_Value);
	}

	public PropertyInfo getFWXZMC() {
		return FWXZMC;
	}

	public void setFWXZMC(Object _Value) {
		FWXZMC.setvalue(_Value);
	}

	public PropertyInfo getFCFHT() {
		return FCFHT;
	}

	public void setFCFHT(Object _Value) {
		FCFHT.setvalue(_Value);
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
	
	public PropertyInfo getRelationID() {
		return RelationID;
	}

	public void setRelationID(Object _Value) {
		RelationID.setvalue(_Value);
	}

	public PropertyInfo getXZZT() {
		return XZZT;
	}

	public void setXZZT(Object _Value) {
		XZZT.setvalue(_Value);
	}
}
