package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.supermap.realestate.registration.model.xmlExportmodel.PropertyInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("C")
public class CExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "层";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCDYID = new PropertyInfo("不动产单元ID");
	private PropertyInfo CH = new PropertyInfo("层号");
	private PropertyInfo ZRZH = new PropertyInfo("自然幢号 ");
	private PropertyInfo SJC = new PropertyInfo("实际层");
	private PropertyInfo MYC = new PropertyInfo("名义层");
	private PropertyInfo CJZMJ = new PropertyInfo("层建筑面积");
	private PropertyInfo CTNJZMJ = new PropertyInfo("层套内建筑面积");
	private PropertyInfo CYTMJ = new PropertyInfo("层阳台面积");
	private PropertyInfo CGYJZMJ = new PropertyInfo("层共有建筑面积");
	private PropertyInfo CFTJZMJ = new PropertyInfo("层分摊建筑面积");
	private PropertyInfo CBQMJ = new PropertyInfo("层半墙面积");
	private PropertyInfo CG = new PropertyInfo("层高");
	private PropertyInfo SPTYMJ = new PropertyInfo("层水平投影面积");
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

	public PropertyInfo getCH() {
		return CH;
	}

	public void setCH(Object _Value) {
		CH.setvalue(_Value);
	}

	public PropertyInfo getZRZH() {
		return ZRZH;
	}

	public void setZRZH(Object _Value) {
		ZRZH.setvalue(_Value);
	}

	public PropertyInfo getSJC() {
		return SJC;
	}

	public void setSJC(Object _Value) {
		SJC.setvalue(_Value);
	}

	public PropertyInfo getMYC() {
		return MYC;
	}

	public void setMYC(Object _Value) {
		MYC.setvalue(_Value);
	}

	public PropertyInfo getCJZMJ() {
		return CJZMJ;
	}

	public void setCJZMJ(Object _Value) {
		CJZMJ.setvalue(_Value);
	}

	public PropertyInfo getCTNJZMJ() {
		return CTNJZMJ;
	}

	public void setCTNJZMJ(Object _Value) {
		CTNJZMJ.setvalue(_Value);
	}

	public PropertyInfo getCYTMJ() {
		return CYTMJ;
	}

	public void setCYTMJ(Object _Value) {
		CYTMJ.setvalue(_Value);
	}

	public PropertyInfo getCGYJZMJ() {
		return CGYJZMJ;
	}

	public void setCGYJZMJ(Object _Value) {
		CGYJZMJ.setvalue(_Value);
	}

	public PropertyInfo getCFTJZMJ() {
		return CFTJZMJ;
	}

	public void setCFTJZMJ(Object _Value) {
		CFTJZMJ.setvalue(_Value);
	}

	public PropertyInfo getCBQMJ() {
		return CBQMJ;
	}

	public void setCBQMJ(Object _Value) {
		CBQMJ.setvalue(_Value);
	}

	public PropertyInfo getCG() {
		return CG;
	}

	public void setCG(Object _Value) {
		CG.setvalue(_Value);
	}

	public PropertyInfo getSPTYMJ() {
		return SPTYMJ;
	}

	public void setSPTYMJ(Object _Value) {
		SPTYMJ.setvalue(_Value);
	}
}
