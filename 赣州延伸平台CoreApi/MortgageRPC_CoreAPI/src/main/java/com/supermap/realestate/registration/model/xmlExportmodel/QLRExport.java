package com.supermap.realestate.registration.model.xmlExportmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("BDCQLR")
public class QLRExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "权利人";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BDCQZH = new PropertyInfo("不动产权证号");
	private PropertyInfo DLRXM = new PropertyInfo("代理人姓名");
	private PropertyInfo DLRLXDH = new PropertyInfo("代理人联系电话");
	private PropertyInfo DLRZJHM = new PropertyInfo("代理人证件号码");
	private PropertyInfo DLRZJLX = new PropertyInfo("代理人证件类型");
	private PropertyInfo DLRZJLXMC = new PropertyInfo("代理人证件类型名称");
	private PropertyInfo DLJGMC = new PropertyInfo("代理机构名称");
	private PropertyInfo GYQK = new PropertyInfo("共有情况");
	private PropertyInfo GYFS = new PropertyInfo("共有方式");
	private PropertyInfo GYFSMC = new PropertyInfo("共有方式名称");
	private PropertyInfo FZJG = new PropertyInfo("发证机关");
	private PropertyInfo GJ = new PropertyInfo("国家/地区");
	private PropertyInfo GJMC = new PropertyInfo("国家/地区名称");
	private PropertyInfo DZ = new PropertyInfo("地址");
	private PropertyInfo BZ = new PropertyInfo("备注");
	private PropertyInfo GZDW = new PropertyInfo("工作单位");
	private PropertyInfo XB = new PropertyInfo("性别");
	private PropertyInfo XBMC = new PropertyInfo("性别名称");
	private PropertyInfo HJSZSS = new PropertyInfo("户籍所在省市");
	private PropertyInfo HJSZSSMC = new PropertyInfo("户籍所在省市名称");
	private PropertyInfo SSHY = new PropertyInfo("所属行业");
	private PropertyInfo SSHYMC = new PropertyInfo("所属行业名称");
	private PropertyInfo YXBZ = new PropertyInfo("有效标识");
	private PropertyInfo QLID = new PropertyInfo("权利ID");
	private PropertyInfo QLRID = new PropertyInfo("权利人ID");
	private PropertyInfo QLRMC = new PropertyInfo("权利人名称");
	private PropertyInfo QLRLX = new PropertyInfo("权利人类型");
	private PropertyInfo QLRLXMC = new PropertyInfo("权利人类型名称");
	private PropertyInfo QLBL = new PropertyInfo("权利比例");
	private PropertyInfo QLMJ = new PropertyInfo("权利面积");
	private PropertyInfo FDDBR = new PropertyInfo("法定代表人");
	private PropertyInfo FDDBRDH = new PropertyInfo("法定代表人电话");
	private PropertyInfo FDDBRZJHM = new PropertyInfo("法定代表人证件号码");
	private PropertyInfo FDDBRZJLX = new PropertyInfo("法定代表人证件类型");
	private PropertyInfo FDDBRZJLXMC = new PropertyInfo("法定代表人证件类型名称");
	private PropertyInfo SQRID = new PropertyInfo("申请人ID");
	private PropertyInfo DZYJ = new PropertyInfo("电子邮件");
	private PropertyInfo DH = new PropertyInfo("电话");
	private PropertyInfo ZJH = new PropertyInfo("证件号");
	private PropertyInfo ZJZL = new PropertyInfo("证件类型");
	private PropertyInfo ZJZLMC = new PropertyInfo("证件类型名称");
	private PropertyInfo DCXMID = new PropertyInfo("调查项目ID");
	private PropertyInfo YB = new PropertyInfo("邮编");
	private PropertyInfo XMBH = new PropertyInfo("项目编号");
	private PropertyInfo SXH = new PropertyInfo("顺序号");

	//增加证书编号，打证环节赋值
	private PropertyInfo ZSBH = new PropertyInfo("证书编号");

	public PropertyInfo getDLRZJLXMC() {
		return DLRZJLXMC;
	}

	public void setDLRZJLXMC(Object _Value) {
		DLRZJLXMC.setvalue(_Value);
	}

	public PropertyInfo getGYFSMC() {
		return GYFSMC;
	}

	public void setGYFSMC(Object _Value) {
		GYFSMC.setvalue(_Value);
	}

	public PropertyInfo getGJMC() {
		return GJMC;
	}

	public void setGJMC(Object _Value) {
		GJMC.setvalue(_Value);
	}

	public PropertyInfo getXBMC() {
		return XBMC;
	}

	public void setXBMC(Object _Value) {
		XBMC.setvalue(_Value);
	}

	public PropertyInfo getHJSZSSMC() {
		return HJSZSSMC;
	}

	public void setHJSZSSMC(Object _Value) {
		HJSZSSMC.setvalue(_Value);
	}

	public PropertyInfo getSSHYMC() {
		return SSHYMC;
	}

	public void setSSHYMC(Object _Value) {
		SSHYMC.setvalue(_Value);
	}

	public PropertyInfo getQLRLXMC() {
		return QLRLXMC;
	}

	public void setQLRLXMC(Object _Value) {
		QLRLXMC.setvalue(_Value);
	}

	public PropertyInfo getFDDBRZJLXMC() {
		return FDDBRZJLXMC;
	}

	public void setFDDBRZJLXMC(Object _Value) {
		FDDBRZJLXMC.setvalue(_Value);
	}

	public PropertyInfo getZJZLMC() {
		return ZJZLMC;
	}

	public void setZJZLMC(Object _Value) {
		ZJZLMC.setvalue(_Value);
	}

	public PropertyInfo getQLID() {
		return QLID;
	}

	public void setQLID(Object _Value) {
		QLID.setvalue(_Value);
	}

	public PropertyInfo getBDCQZH() {
		return BDCQZH;
	}

	public void setBDCQZH(Object _Value) {
		BDCQZH.setvalue(_Value);
	}

	public PropertyInfo getDLRXM() {
		return DLRXM;
	}

	public void setDLRXM(Object _Value) {
		DLRXM.setvalue(_Value);
	}

	public PropertyInfo getDLRLXDH() {
		return DLRLXDH;
	}

	public void setDLRLXDH(Object _Value) {
		DLRLXDH.setvalue(_Value);
	}

	public PropertyInfo getDLRZJHM() {
		return DLRZJHM;
	}

	public void setDLRZJHM(Object _Value) {
		DLRZJHM.setvalue(_Value);
	}

	public PropertyInfo getDLRZJLX() {
		return DLRZJLX;
	}

	public void setDLRZJLX(Object _Value) {
		DLRZJLX.setvalue(_Value);
	}

	public PropertyInfo getDLJGMC() {
		return DLJGMC;
	}

	public void setDLJGMC(Object _Value) {
		DLJGMC.setvalue(_Value);
	}

	public PropertyInfo getGYQK() {
		return GYQK;
	}

	public void setGYQK(Object _Value) {
		GYQK.setvalue(_Value);
	}

	public PropertyInfo getGYFS() {
		return GYFS;
	}

	public void setGYFS(Object _Value) {
		GYFS.setvalue(_Value);
	}

	public PropertyInfo getFZJG() {
		return FZJG;
	}

	public void setFZJG(Object _Value) {
		FZJG.setvalue(_Value);
	}

	public PropertyInfo getGJ() {
		return GJ;
	}

	public void setGJ(Object _Value) {
		GJ.setvalue(_Value);
	}

	public PropertyInfo getDZ() {
		return DZ;
	}

	public void setDZ(Object _Value) {
		DZ.setvalue(_Value);
	}

	public PropertyInfo getBZ() {
		return BZ;
	}

	public void setBZ(Object _Value) {
		BZ.setvalue(_Value);
	}

	public PropertyInfo getGZDW() {
		return GZDW;
	}

	public void setGZDW(Object _Value) {
		GZDW.setvalue(_Value);
	}

	public PropertyInfo getXB() {
		return XB;
	}

	public void setXB(Object _Value) {
		XB.setvalue(_Value);
	}

	public PropertyInfo getHJSZSS() {
		return HJSZSS;
	}

	public void setHJSZSS(Object _Value) {
		HJSZSS.setvalue(_Value);
	}

	public PropertyInfo getSSHY() {
		return SSHY;
	}

	public void setSSHY(Object _Value) {
		SSHY.setvalue(_Value);
	}

	public PropertyInfo getYXBZ() {
		return YXBZ;
	}

	public void setYXBZ(Object _Value) {
		YXBZ.setvalue(_Value);
	}

	public PropertyInfo getQLRID() {
		return QLRID;
	}

	public void setQLRID(Object _Value) {
		QLRID.setvalue(_Value);
	}

	public PropertyInfo getQLRMC() {
		return QLRMC;
	}

	public void setQLRMC(Object _Value) {
		QLRMC.setvalue(_Value);
	}

	public PropertyInfo getQLRLX() {
		return QLRLX;
	}

	public void setQLRLX(Object _Value) {
		QLRLX.setvalue(_Value);
	}

	public PropertyInfo getQLBL() {
		return QLBL;
	}

	public void setQLBL(Object _Value) {
		QLBL.setvalue(_Value);
	}

	public PropertyInfo getQLMJ() {
		return QLMJ;
	}

	public void setQLMJ(Object _Value) {
		QLMJ.setvalue(_Value);
	}

	public PropertyInfo getFDDBR() {
		return FDDBR;
	}

	public void setFDDBR(Object _Value) {
		FDDBR.setvalue(_Value);
	}

	public PropertyInfo getFDDBRDH() {
		return FDDBRDH;
	}

	public void setFDDBRDH(Object _Value) {
		FDDBRDH.setvalue(_Value);
	}

	public PropertyInfo getFDDBRZJHM() {
		return FDDBRZJHM;
	}

	public void setFDDBRZJHM(Object _Value) {
		FDDBRZJHM.setvalue(_Value);
	}

	public PropertyInfo getFDDBRZJLX() {
		return FDDBRZJLX;
	}

	public void setFDDBRZJLX(Object _Value) {
		FDDBRZJLX.setvalue(_Value);
	}

	public PropertyInfo getSQRID() {
		return SQRID;
	}

	public void setSQRID(Object _Value) {
		SQRID.setvalue(_Value);
	}

	public PropertyInfo getDZYJ() {
		return DZYJ;
	}

	public void setDZYJ(Object _Value) {
		DZYJ.setvalue(_Value);
	}

	public PropertyInfo getDH() {
		return DH;
	}

	public void setDH(Object _Value) {
		DH.setvalue(_Value);
	}

	public PropertyInfo getZJH() {
		return ZJH;
	}

	public void setZJH(Object _Value) {
		ZJH.setvalue(_Value);
	}

	public PropertyInfo getZJZL() {
		return ZJZL;
	}

	public void setZJZL(Object _Value) {
		ZJZL.setvalue(_Value);
	}

	public PropertyInfo getDCXMID() {
		return DCXMID;
	}

	public void setDCXMID(Object _Value) {
		DCXMID.setvalue(_Value);
	}

	public PropertyInfo getYB() {
		return YB;
	}

	public void setYB(Object _Value) {
		YB.setvalue(_Value);
	}

	public PropertyInfo getXMBH() {
		return XMBH;
	}

	public void setXMBH(Object _Value) {
		XMBH.setvalue(_Value);
	}

	public PropertyInfo getSXH() {
		return SXH;
	}

	public void setSXH(Object _Value) {
		SXH.setvalue(_Value);
	}
	public PropertyInfo getZSBH() {
		return ZSBH;
	}

	public void setZSBH(Object _Value) {
		ZSBH.setvalue(_Value);
	}
}
