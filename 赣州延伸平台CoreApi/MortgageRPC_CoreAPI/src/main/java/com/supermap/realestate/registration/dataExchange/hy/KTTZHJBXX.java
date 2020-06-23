package com.supermap.realestate.registration.dataExchange.hy;

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
 * @Description:宗海基本信息表
 * @author diaoliwei
 * @date 2015年9月21日 下午9:11:37
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_ZHJBXX")
public class KTTZHJBXX {

	 @XmlAttribute(name = "ZHDM", required = true)
	 private String zhdm;
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "ZHTZM", required = true)
	 private String zhtzm;
	 @XmlAttribute(name = "XMMC", required = true)
	 private String xmmc;
	 @XmlAttribute(name = "XMXZ")
	 private String xmxz;
	 @XmlAttribute(name = "YHZMJ")
	 private BigDecimal yhzmj;
	 @XmlAttribute(name = "ZHMJ")
	 private BigDecimal zhmj;
	 @XmlAttribute(name = "DB")
	 private String db;
	 @XmlAttribute(name = "ZHAX")
	 private BigDecimal zhax;
	 @XmlAttribute(name = "YHLXA")
	 private String yhlxa;
	 @XmlAttribute(name = "YHLXB")
	 private String yhlxb;
	 @XmlAttribute(name = "YHWZSM")
	 private String yhwzsm;
	 @XmlAttribute(name = "HDMC")
	 private String hdmc;
	 @XmlAttribute(name = "HDDM")
	 private String hddm;
	 @XmlAttribute(name = "YDFW")
	 private String ydfw;
	 @XmlAttribute(name = "YDMJ")
	 private BigDecimal ydmj;
	 @XmlAttribute(name = "HDWZ")
	 private String hdwz;
	 @XmlAttribute(name = "HDYT")
	 private String hdyt;
	 @XmlAttribute(name = "ZHT", required = true)
	 private String zht;
	 @XmlAttribute(name = "ZT", required = true)
	 private String zt;
	 @XmlAttribute(name = "QXDM", required = true)
	 private String qxdm;
	 @XmlAttribute(name = "DJSJ", required = true)
	 private String djsj;
	 @XmlAttribute(name = "DBR", required = true)
	 private String dbr;
	 @XmlAttribute(name = "FJ")
	 private String fj;
	 
	 @XmlAttribute(name = "BSM", required = true)
	 private int bsm;
	 @XmlAttribute(name = "DAH", required = true)
	 protected String dah;
	 @XmlAttribute(name = "DJJGBM", required = true)
	 protected String djjgbm;
	 @XmlAttribute(name = "DJJGMC", required = true)
	 protected String djjgmc;
	 @XmlAttribute(name = "JDH", required = true)
	 protected String jdh;
	 @XmlAttribute(name = "JFH", required = true)
	 protected String jfh;
	 @XmlAttribute(name = "ZH", required = true)
	 protected String zh;
	 
	 @XmlAttribute(name = "BDCDYID")
	 protected String bdcdyid;
	 
	 
	public int getBsm() {
		return bsm;
	}
	public void setBsm(int bsm) {
		this.bsm = bsm;
	}
	public String getDah() {
		return dah;
	}
	public void setDah(String dah) {
		this.dah = dah;
	}
	public String getDjjgbm() {
		return djjgbm;
	}
	public void setDjjgbm(String djjgbm) {
		this.djjgbm = djjgbm;
	}
	public String getDjjgmc() {
		return djjgmc;
	}
	public void setDjjgmc(String djjgmc) {
		this.djjgmc = djjgmc;
	}
	public String getJdh() {
		return jdh;
	}
	public void setJdh(String jdh) {
		this.jdh = jdh;
	}
	public String getJfh() {
		return jfh;
	}
	public void setJfh(String jfh) {
		this.jfh = jfh;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public String getZhdm() {
		return zhdm;
	}
	public void setZhdm(String zhdm) {
		this.zhdm = zhdm;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getZhtzm() {
		return zhtzm;
	}
	public void setZhtzm(String zhtzm) {
		this.zhtzm = zhtzm;
		this.zhtzm=ConstHelper.getReportValueByValue("TZM", zhtzm);
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public String getXmxz() {
		return xmxz;
	}
	public void setXmxz(String xmxz) {
		this.xmxz = xmxz;
		this.xmxz=ConstHelper.getReportValueByValue("XMXZ", xmxz);
	}
	public BigDecimal getYhzmj() {
		return yhzmj;
	}
	public void setYhzmj(BigDecimal yhzmj) {
		this.yhzmj = StringHelper.cutBigDecimal(yhzmj, 4);
	}
	public BigDecimal getZhmj() {
		return zhmj;
	}
	public void setZhmj(BigDecimal zhmj) {
		this.zhmj = StringHelper.cutBigDecimal(zhmj, 4);
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
		this.db=ConstHelper.getReportValueByValue("HYDB", db);
	}
	public BigDecimal getZhax() {
		return zhax;
	}
	public void setZhax(BigDecimal zhax) {
		this.zhax = zhax;
	}
	public String getYhlxa() {
		return yhlxa;
	}
	public void setYhlxa(String yhlxa) {
		this.yhlxa = yhlxa;
		this.yhlxa=ConstHelper.getReportValueByValue("HYSYLXA", yhlxa);
	}
	public String getYhlxb() {
		return yhlxb;
	}
	public void setYhlxb(String yhlxb) {
		this.yhlxb = yhlxb;
		this.yhlxb=ConstHelper.getReportValueByValue("HYSYLXB", yhlxb);
	}
	public String getYhwzsm() {
		return yhwzsm;
	}
	public void setYhwzsm(String yhwzsm) {
		this.yhwzsm = yhwzsm;
	}
	public String getHdmc() {
		return hdmc;
	}
	public void setHdmc(String hdmc) {
		this.hdmc = hdmc;
	}
	public String getHddm() {
		return hddm;
	}
	public void setHddm(String hddm) {
		this.hddm = hddm;
	}
	public String getYdfw() {
		return ydfw;
	}
	public void setYdfw(String ydfw) {
		this.ydfw = ydfw;
	}
	public BigDecimal getYdmj() {
		return ydmj;
	}
	public void setYdmj(BigDecimal ydmj) {
		this.ydmj = StringHelper.cutBigDecimal(ydmj, 4); //用岛面积
	}
	public String getHdwz() {
		return hdwz;
	}
	public void setHdwz(String hdwz) {
		this.hdwz = hdwz;
	}
	public String getHdyt() {
		return hdyt;
	}
	public void setHdyt(String hdyt) {
		this.hdyt = hdyt;
	}
	public String getZht() {
		return zht;
	}
	public void setZht(String zht) {
		this.zht = zht;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public String getDbr() {
		return dbr;
	}
	public void setDbr(String dbr) {
		this.dbr = dbr;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	 
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bdcdyh)){
			this.bdcdyh=null;
		}
		if(StringHelper.isEmpty(this.dah)){
			this.dah=null;
		}
		if(StringHelper.isEmpty(this.db)){
			this.db=null;
		}
		if(StringHelper.isEmpty(this.dbr)){
			this.dbr=null;
		}
		if(StringHelper.isEmpty(this.djjgbm)){
			this.djjgbm=null;
		}
		if(StringHelper.isEmpty(this.djsj)){
			this.djsj=null;
		}
		if(StringHelper.isEmpty(this.fj)){
			this.fj=null;
		}
		if(StringHelper.isEmpty(this.hddm)){
			this.hddm=null;
		}
		if(StringHelper.isEmpty(this.hdmc)){
			this.hdmc=null;
		}
		if(StringHelper.isEmpty(this.hdwz)){
			this.hdwz=null;
		}
		if(StringHelper.isEmpty(this.hdyt)){
			this.hdyt=null;
		}
		if(StringHelper.isEmpty(this.jdh)){
			this.jdh=null;
		}
		if(StringHelper.isEmpty(this.jfh)){
			this.jfh=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.xmmc)){
			this.xmmc=null;
		}
		if(StringHelper.isEmpty(this.xmxz)){
			this.xmxz=null;
		}
		if(StringHelper.isEmpty(this.ydfw)){
			this.ydfw=null;
		}
		if(StringHelper.isEmpty(this.yhlxa)){
			this.yhlxa=null;
		}
		if(StringHelper.isEmpty(this.yhlxb)){
			this.yhlxb=null;
		}
		if(StringHelper.isEmpty(this.yhwzsm)){
			this.yhwzsm=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.zh)){
			this.zh=null;
		}
		if(StringHelper.isEmpty(this.zhdm)){
			this.zhdm=null;
		}
		if(StringHelper.isEmpty(this.zht)){
			this.zht=null;
		}
		if(StringHelper.isEmpty(this.zhtzm)){
			this.zhtzm=null;
		}
		if(StringHelper.isEmpty(this.zt)){
			this.zt=null;
		}
	}
}
