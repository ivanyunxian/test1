package com.supermap.yingtanothers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月13日 下午2:31:41
 * 功能：鹰潭不动产共享中间库权利人表
 */
@Entity
@Table(name = "QLR", schema = "GXFCK")
public class QLR {

	//权利人ID
	private String QLRID;
	//权利ID
	private String QLID;
	//顺序号
	private Integer SXH;
	//权利人名称
	private String QLRMC;
	//不动产权证号
	private String BDCQZH;
	//A.30 证件种类
	private String ZJZL;
	//证件号
	private String ZJH;
	//发证机关
	private String FZJG;
	//A.41 所属行业
	private String SSHY;
	//A.35 国家
	private String GJ;
	//A.38 户籍所在省
	private String HJSZSS;
	//A.43 性别
	private String XB;
	//电话
	private String DH;
	//地址
	private String DZ;
	//邮编
	private String YB;
	//工作单位
	private String GZDW;
	//电子邮件
	private String DZYJ;
	//A.36 权利人类型
	private String QLRLX;
	//权利比例
	private String QLBL;
	//A.34 共有方式
	private String GYFS;
	//共有情况
	private String GYQK;
	//备注
	private String BZ;
	//是否持证人
	private String ISCZR;
	//要素代码
	private String YSDM;
	//不动产单元号
	private String BDCDYH;
	//权证印刷序列号
	private String QZYSXLH;
	//共享类型
	private String GXLX;
	//共享项目编号
	private String GXXMBH;
	//主键
	private String QLR_ID;
	
	@Id
	@Column(name = "QLR_ID")
	public String getQLR_ID() {
		return QLR_ID;
	}
	public void setQLR_ID(String qLR_ID) {
		QLR_ID = qLR_ID;
	}
	@Column(name = "QLRID")
	public String getQLRID() {
		return QLRID;
	}
	public void setQLRID(String qLRID) {
		QLRID = qLRID;
	}
	
	@Column(name = "QLID")
	public String getQLID() {
		return QLID;
	}
	public void setQLID(String qLID) {
		QLID = qLID;
	}
	
	@Column(name = "SXH")
	public Integer getSXH() {
		return SXH;
	}
	public void setSXH(Integer sXH) {
		SXH = sXH;
	}
	
	@Column(name = "QLRMC")
	public String getQLRMC() {
		return QLRMC;
	}
	public void setQLRMC(String qLRMC) {
		QLRMC = qLRMC;
	}
	
	@Column(name = "BDCQZH")
	public String getBDCQZH() {
		return BDCQZH;
	}
	public void setBDCQZH(String bDCQZH) {
		BDCQZH = bDCQZH;
	}
	
	@Column(name = "ZJZL")
	public String getZJZL() {
		return ZJZL;
	}
	public void setZJZL(String zJZL) {
		ZJZL = zJZL;
	}
	
	@Column(name = "ZJH")
	public String getZJH() {
		return ZJH;
	}
	public void setZJH(String zJH) {
		ZJH = zJH;
	}
	
	@Column(name = "FZJG")
	public String getFZJG() {
		return FZJG;
	}
	public void setFZJG(String fZJG) {
		FZJG = fZJG;
	}
	
	@Column(name = "SSHY")
	public String getSSHY() {
		return SSHY;
	}
	public void setSSHY(String sSHY) {
		SSHY = sSHY;
	}
	
	@Column(name = "GJ")
	public String getGJ() {
		return GJ;
	}
	public void setGJ(String gJ) {
		GJ = gJ;
	}
	
	@Column(name = "HJSZSS")
	public String getHJSZSS() {
		return HJSZSS;
	}
	public void setHJSZSS(String hJSZSS) {
		HJSZSS = hJSZSS;
	}
	
	@Column(name = "XB")
	public String getXB() {
		return XB;
	}
	public void setXB(String xB) {
		XB = xB;
	}
	
	@Column(name = "DH")
	public String getDH() {
		return DH;
	}
	public void setDH(String dH) {
		DH = dH;
	}
	
	@Column(name = "DZ")
	public String getDZ() {
		return DZ;
	}
	public void setDZ(String dZ) {
		DZ = dZ;
	}
	
	@Column(name = "YB")
	public String getYB() {
		return YB;
	}
	public void setYB(String yB) {
		YB = yB;
	}
	
	@Column(name = "GZDW")
	public String getGZDW() {
		return GZDW;
	}
	
	public void setGZDW(String gZDW) {
		GZDW = gZDW;
	}
	
	@Column(name = "DZYJ")
	public String getDZYJ() {
		return DZYJ;
	}
	public void setDZYJ(String dZYJ) {
		DZYJ = dZYJ;
	}
	
	@Column(name = "QLRLX")
	public String getQLRLX() {
		return QLRLX;
	}
	public void setQLRLX(String qLRLX) {
		QLRLX = qLRLX;
	}
	
	@Column(name = "QLBL")
	public String getQLBL() {
		return QLBL;
	}
	public void setQLBL(String qLBL) {
		QLBL = qLBL;
	}
	
	@Column(name = "GYFS")
	public String getGYFS() {
		return GYFS;
	}
	public void setGYFS(String gYFS) {
		GYFS = gYFS;
	}
	
	@Column(name = "GYQK")
	public String getGYQK() {
		return GYQK;
	}
	public void setGYQK(String gYQK) {
		GYQK = gYQK;
	}
	
	@Column(name = "BZ")
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
	
	@Column(name = "ISCZR")
	public String getISCZR() {
		return ISCZR;
	}
	public void setISCZR(String iSCZR) {
		ISCZR = iSCZR;
	}
	
	@Column(name = "YSDM")
	public String getYSDM() {
		return YSDM;
	}
	public void setYSDM(String ySDM) {
		YSDM = ySDM;
	}
	
	@Column(name = "BDCDYH")
	public String getBDCDYH() {
		return BDCDYH;
	}
	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}
	
	@Column(name = "QZYSXLH")
	public String getQZYSXLH() {
		return QZYSXLH;
	}
	public void setQZYSXLH(String qZYSXLH) {
		QZYSXLH = qZYSXLH;
	}
	
	@Column(name = "GXLX")
	public String getGXLX() {
		return GXLX;
	}
	public void setGXLX(String gXLX) {
		GXLX = gXLX;
	}
	
	@Column(name = "GXXMBH")
	public String getGXXMBH() {
		return GXXMBH;
	}
	public void setGXXMBH(String gXXMBH) {
		GXXMBH = gXXMBH;
	}
	
}
