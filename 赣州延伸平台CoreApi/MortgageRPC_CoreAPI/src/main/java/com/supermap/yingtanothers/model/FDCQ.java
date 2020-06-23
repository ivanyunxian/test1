package com.supermap.yingtanothers.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 作者： 苗利涛 时间： 2016年1月13日 下午3:05:50 功能：鹰潭不动产共享中间库房地产权属性结构表
 */

@Entity
@Table(name = "FDCQ", schema = "GXFCK")
public class FDCQ {

	// 共享项目编号
	private String GXXMBH;
	// 要素代码
	private String YSDM;
	// 不动产单元号
	private String BDCDYH;
	// 业务号
	private String YWH;
	// A.8权利类型
	private String QLLX;
	// 表A.21 登记类型
	private String DJLX;
	// 登记原因
	private String DJYY;
	// 房地坐落
	private String FDZL;
	// 房地产交易价格
	private Double FDCJYJG;
	// 规划用途
	private String GHYT;
	// A.19 房屋性质
	private String FWXZ;
	// A.46房屋结构
	private String FWJG;
	// 所在层
	private String SZC;
	// 总层数
	private Integer ZCS;
	// 建筑面积
	private Double JZMJ;
	// 专有建筑面积
	private Double ZYJZMJ;
	// 分摊建筑面积
	private Double FTJZMJ;
	// 竣工时间
	private Date JGSJ;
	// 不动产权证号
	private String BDCQZH;	
	// 区县代码
	private String QXDM;
	// 登记机构
	private String DJJG;
	// 登簿人
	private String DBR;
	// 登记时间
	private Date DJSJ;
	// 附记
	private String FJ;
	// 权属状态
	private String QSZT;
	// 权利ID
	private String QLID;
	// 关联ID
	private String RELATIONID;
	//房地产权ID
	private String FDCQID;
	
//	// 土地使用起始时间
//	private Date TDSYQSSJ;
//	// 土地使用结束时间
//	private Date TDSYJSSJ;
//	// 土地使用权人
//	private String TDSHYQR;
//
//	// 独用土地面积
//	private Double DYTDMJ;
//	// 分摊土地面积
//	private Double FTTDMJ;
//
//	// 不动产单元ID
//	private String BDCDYID;

	@Column(name = "RELATIONID")
	public String getRELATIONID() {
		return RELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		RELATIONID = rELATIONID;
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

	@Column(name = "YWH")
	public String getYWH() {
		return YWH;
	}

	public void setYWH(String yWH) {
		YWH = yWH;
	}

	@Column(name = "QLLX")
	public String getQLLX() {
		return QLLX;
	}

	public void setQLLX(String qLLX) {
		QLLX = qLLX;
	}

	@Column(name = "DJLX")
	public String getDJLX() {
		return DJLX;
	}

	public void setDJLX(String dJLX) {
		DJLX = dJLX;
	}

	@Column(name = "DJYY")
	public String getDJYY() {
		return DJYY;
	}

	public void setDJYY(String dJYY) {
		DJYY = dJYY;
	}


	@Column(name = "BDCQZH")
	public String getBDCQZH() {
		return BDCQZH;
	}

	public void setBDCQZH(String bDCQZH) {
		BDCQZH = bDCQZH;
	}

	@Column(name = "QXDM")
	public String getQXDM() {
		return QXDM;
	}

	public void setQXDM(String qXDM) {
		QXDM = qXDM;
	}

	@Column(name = "DJJG")
	public String getDJJG() {
		return DJJG;
	}

	public void setDJJG(String dJJG) {
		DJJG = dJJG;
	}

	@Column(name = "DBR")
	public String getDBR() {
		return DBR;
	}

	public void setDBR(String dBR) {
		DBR = dBR;
	}

	@Column(name = "DJSJ")
	public Date getDJSJ() {
		return DJSJ;
	}

	public void setDJSJ(Date dJSJ) {
		DJSJ = dJSJ;
	}

	@Column(name = "FJ")
	public String getFJ() {
		return FJ;
	}

	public void setFJ(String fJ) {
		FJ = fJ;
	}

	@Column(name = "QSZT")
	public String getQSZT() {
		return QSZT;
	}

	public void setQSZT(String qSZT) {
		QSZT = qSZT;
	}

	@Column(name = "FDCJYJG")
	public Double getFDCJYJG() {
		return FDCJYJG;
	}

	public void setFDCJYJG(Double fDCJYJG) {
		FDCJYJG = fDCJYJG;
	}

	@Column(name = "FDZL")
	public String getFDZL() {
		return FDZL;
	}

	public void setFDZL(String fDZL) {
		FDZL = fDZL;
	}

	@Column(name = "GHYT")
	public String getGHYT() {
		return GHYT;
	}

	public void setGHYT(String gHYT) {
		GHYT = gHYT;
	}

	@Column(name = "FWXZ")
	public String getFWXZ() {
		return FWXZ;
	}

	public void setFWXZ(String fWXZ) {
		FWXZ = fWXZ;
	}

	@Column(name = "FWJG")
	public String getFWJG() {
		return FWJG;
	}

	public void setFWJG(String fWJG) {
		FWJG = fWJG;
	}

	@Column(name = "SZC")
	public String getSZC() {
		return SZC;
	}

	public void setSZC(String sZC) {
		SZC = sZC;
	}

	@Column(name = "ZCS")
	public Integer getZCS() {
		return ZCS;
	}

	public void setZCS(Integer zCS) {
		ZCS = zCS;
	}

	@Column(name = "JZMJ")
	public Double getJZMJ() {
		return JZMJ;
	}

	public void setJZMJ(Double jZMJ) {
		JZMJ = jZMJ;
	}

	@Column(name = "ZYJZMJ")
	public Double getZYJZMJ() {
		return ZYJZMJ;
	}

	public void setZYJZMJ(Double zYJZMJ) {
		ZYJZMJ = zYJZMJ;
	}

	@Column(name = "FTJZMJ")
	public Double getFTJZMJ() {
		return FTJZMJ;
	}

	public void setFTJZMJ(Double fTJZMJ) {
		FTJZMJ = fTJZMJ;
	}

	@Column(name = "JGSJ")
	public Date getJGSJ() {
		return JGSJ;
	}

	public void setJGSJ(Date jGSJ) {
		JGSJ = jGSJ;
	}

	
	@Column(name = "QLID")
	public String getQLID() {
		return QLID;
	}

	public void setQLID(String qLID) {
		QLID = qLID;
	}

	@Column(name = "GXXMBH")
	public String getGXXMBH() {
		return GXXMBH;
	}

	public void setGXXMBH(String gXXMBH) {
		GXXMBH = gXXMBH;
	}

	@Id
	@Column(name = "FDCQID")
	public String getFDCQID() {
		return FDCQID;
	}

	public void setFDCQID(String fDCQID) {
		FDCQID = fDCQID;
	}

}
