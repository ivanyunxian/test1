package com.supermap.realestate.registration.dataExchange.nydsyq;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:农用地
 * @author 李鑫
 * @date 2018年8月21日 下午8:53:46
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_NYDSYQ")
public class QLNYDSYQ {
	 //要素代码
	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 //不动产单元号
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
//	 业务号
	 @XmlAttribute(name = "YWH")
	 private String ywh;
//	 权利类型
	 @XmlAttribute(name = "QLLX", required = true)
	 private String qllx;
//	 登记类型
	 @XmlAttribute(name = "DJLX", required = true)
	 private String djlx;
//	 登记原因
	 @XmlAttribute(name = "DJYY", required = true)
	 private String djyy;
//	 坐落
	 @XmlAttribute(name = "ZL", required = true)
	 protected String zl;
//	 发包方代码
	 @XmlAttribute(name = "FBFDM", required = true)
	 private String fbfdm;
//	 发包名称
	 @XmlAttribute(name = "FBFMC", required = true)
	 private String fbfmc;
//	 承包面积
	 @XmlAttribute(name = "CBMJ", required = true)
	 private double cbmj;
//	 承包起始时间
	 @XmlAttribute(name = "CBQSSJ", required = true)
	 private String cbqssj;
//	 承包结束时间
	 @XmlAttribute(name = "CBJSSJ",required = true)
	 private String cbjssj;
//	 土地所有权性质
	 @XmlAttribute(name = "TDSYQXZ",required = true)
	 private String tdsyqxz;
//	 水域滩涂类型
	 @XmlAttribute(name = "SYTTLX")
	 private String syttlx;
//	 养殖方式
	 @XmlAttribute(name = "YZYFS")
	 private String yzyfs;
//	 草原质量
	 @XmlAttribute(name = "CYZL")
	 private String cyzl;
//	 适宜载畜量
	 @XmlAttribute(name = "SYZCL")
	 private String syzcl;
//	 不动产权证号
	 @XmlAttribute(name = "BDCQZH", required = true)
	 private String bdcqzh;
//	 区县代码
	 @XmlAttribute(name = "QXDM", required = true)
	 private String  qxdm;
//	 登记机构
	 @XmlAttribute(name = "DJJG", required = true)
	 private String djjg;
//	 登簿人
	 @XmlAttribute(name = "DBR", required = true)
	 private String dbr;
//	 登记时间
	 @XmlAttribute(name = "DJSJ")
	 private String djsj;
//	 附记
	 @XmlAttribute(name = "FJ", required = true)
	 private String fj;
//	 权属状态
	 @XmlAttribute(name = "QSZT", required = true)
	 private String qszt;
	 
	 @XmlAttribute(name = "QLID")
	 private String qlid;
	 @XmlAttribute(name = "XMBH")
	 private String xmbh;
	 @XmlAttribute(name = "BDCDYID")
	 private String bdcdyid;
	 
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
	}
	public String getDjlx() {
		return djlx;
	}
	public void setDjlx(String djlx) {
		this.djlx = djlx;
	}
	public String getDjyy() {
		return djyy;
	}
	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
	public String getFbfdm() {
		return fbfdm;
	}
	public void setFbfdm(String fbfdm) {
		this.fbfdm = fbfdm;
	}
	public String getFbfmc() {
		return fbfmc;
	}
	public void setFbfmc(String string) {
		this.fbfmc = string;
	}
	public double getCbmj() {
		return cbmj;
	}
	public void setCbmj(double cbmj) {
		this.cbmj = cbmj;
	}
	public String getCbqssj() {
		return cbqssj;
	}
	public void setCbqssj(String cbqssj) {
		this.cbqssj = cbqssj;
	}
	public String getCbjssj() {
		return cbjssj;
	}
	public void setCbjssj(String cbjssj) {
		this.cbjssj = cbjssj;
	}
	public String getTdsyqxz() {
		return tdsyqxz;
	}
	public void setTdsyqxz(String tdsyqxz) {
		this.tdsyqxz = tdsyqxz;
	}
	public String getSyttlx() {
		return syttlx;
	}
	public void setSyttlx(String syttlx) {
		this.syttlx = syttlx;
	}
	public String getYzyfs() {
		return yzyfs;
	}
	public void setYzyfs(String yzyfs) {
		this.yzyfs = yzyfs;
	}
	public String getCyzl() {
		return cyzl;
	}
	public void setCyzl(String cyzl) {
		this.cyzl = cyzl;
	}
	public String getSyzcl() {
		return syzcl;
	}
	public void setSyzcl(String syzcl) {
		this.syzcl = syzcl;
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getDjjg() {
		return djjg;
	}
	public void setDjjg(String djjg) {
		this.djjg = djjg;
	}
	public String getDbr() {
		return dbr;
	}
	public void setDbr(String dbr) {
		this.dbr = dbr;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
	}
	
	public String getQlid() {
		return qlid;
	}
	public void setQlid(String qlid) {
		this.qlid = qlid;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}

}
	 
	
	 
	