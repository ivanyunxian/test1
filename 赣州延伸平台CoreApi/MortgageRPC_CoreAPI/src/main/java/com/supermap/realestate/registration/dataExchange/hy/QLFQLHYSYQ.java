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
 * @Description:海域（含无居民海岛）使用权
 * @author diaoliwei
 * @date 2015年9月21日 下午9:30:11
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_HYSYQ")
public class QLFQLHYSYQ {

	@XmlAttribute(name = "YSDM", required = true)
	private String ysdm;
	@XmlAttribute(name = "ZHHDDM", required = true)
	private String zhhddm;
	@XmlAttribute(name = "BDCDYH", required = true)
	private String bdcdyh;
    @XmlAttribute(name = "YWH")
    private String ywh;
    @XmlAttribute(name = "QLLX", required = true)
    private String qllx;
    @XmlAttribute(name = "DJLX", required = true)
    private String djlx;
    @XmlAttribute(name = "DJYY", required = true)
    private String djyy;
    @XmlAttribute(name = "SYQMJ")
    private BigDecimal syqmj;
    @XmlAttribute(name = "SYQQSSJ")
    private String syqqssj;
    @XmlAttribute(name = "SYQJSSJ")
    private String syqjssj;
    @XmlAttribute(name = "SYJZE")
    private double syjze;
    @XmlAttribute(name = "SYJBZYJ")
    private String syjbzyj;
    @XmlAttribute(name = "SYJJNQK")
    private String syjjnqk;
    @XmlAttribute(name = "BDCQZH", required = true)
    private String bdcqzh;
    @XmlAttribute(name = "QXDM", required = true)
    private String qxdm;
    @XmlAttribute(name = "DJJG", required = true)
    private String djjg;
    @XmlAttribute(name = "DBR", required = true)
    private String dbr;
    @XmlAttribute(name = "DJSJ", required = true)
    private String djsj;
    @XmlAttribute(name = "QSZT", required = true)
    private String qszt;
    @XmlAttribute(name = "FJ")
    private String fj;
    
    @XmlAttribute(name = "QLID")
    protected String qlid;
    @XmlAttribute(name = "XMBH")
    protected String xmbh;
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;
    
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getZhhddm() {
		return zhhddm;
	}
	public void setZhhddm(String zhhddm) {
		this.zhhddm = zhhddm;
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
		this.qllx=ConstHelper.getReportValueByValue("QLLX", qllx);
	}
	public String getDjlx() {
		return djlx;
	}
	public void setDjlx(String djlx) {
		this.djlx = djlx;
		this.djlx=ConstHelper.getReportValueByValue("DJLX", djlx);
	}
	public String getDjyy() {
		return djyy;
	}
	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}
	public BigDecimal getSyqmj() {
		return syqmj;
	}
	public void setSyqmj(BigDecimal syqmj) {
		this.syqmj = StringHelper.cutBigDecimal(syqmj, 2);
	}
	public String getSyqqssj() {
		return syqqssj;
	}
	public void setSyqqssj(String syqqssj) {
		this.syqqssj = syqqssj;
	}
	public String getSyqjssj() {
		return syqjssj;
	}
	public void setSyqjssj(String syqjssj) {
		this.syqjssj = syqjssj;
	}
	public double getSyjze() {
		return syjze;
	}
	public void setSyjze(double syjze) {
		this.syjze = StringHelper.getDouble(StringHelper.cut(syjze, 4)); //使用金总额
	}
	public String getSyjbzyj() {
		return syjbzyj;
	}
	public void setSyjbzyj(String syjbzyj) {
		this.syjbzyj = syjbzyj;
	}
	public String getSyjjnqk() {
		return syjjnqk;
	}
	public void setSyjjnqk(String syjjnqk) {
		this.syjjnqk = syjjnqk;
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
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
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
	
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bdcdyh)){
			this.bdcdyh=null;
		}
		if(StringHelper.isEmpty(this.bdcqzh)){
			this.bdcqzh=null;
		}
		if(StringHelper.isEmpty(this.dbr)){
			this.dbr=null;
		}
		if(StringHelper.isEmpty(this.djjg)){
			this.djjg=null;
		}
		if(StringHelper.isEmpty(this.djlx)){
			this.djlx=null;
		}
		if(StringHelper.isEmpty(this.djsj)){
			this.djsj=null;
		}
		if(StringHelper.isEmpty(this.djyy)){
			this.djyy=null;
		}
		if(StringHelper.isEmpty(this.fj)){
			this.fj=null;
		}
		if(StringHelper.isEmpty(this.qllx)){
			this.qllx=null;
		}
		if(StringHelper.isEmpty(this.qszt)){
			this.qszt=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.syjbzyj)){
			this.syjbzyj=null;
		}
		if(StringHelper.isEmpty(this.syjjnqk)){
			this.syjjnqk=null;
		}
		if(StringHelper.isEmpty(this.syqjssj)){
			this.syqjssj=null;
		}
		if(StringHelper.isEmpty(this.syqqssj)){
			this.syqqssj=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.ywh)){
			this.ywh=null;
		}
		if(StringHelper.isEmpty(this.zhhddm)){
			this.zhhddm=null;
		}
	}
}
