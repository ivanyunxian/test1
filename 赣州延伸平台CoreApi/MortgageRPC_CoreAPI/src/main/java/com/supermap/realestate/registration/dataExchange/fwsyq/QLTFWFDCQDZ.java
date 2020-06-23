package com.supermap.realestate.registration.dataExchange.fwsyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:房地产权_多幢
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午10:14:30
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLT_FW_FDCQ_DZ")
public class QLTFWFDCQDZ {
	
	@XmlAttribute(name = "BDCDYH", required = true)
    protected String bdcdyh;
	@XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
	@XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
	@XmlAttribute(name = "DYTDMJ")
    protected double dytdmj;
	@XmlAttribute(name = "FTTDMJ")
    protected double fttdmj;
	@XmlAttribute(name = "BDCQZH", required = true)
    protected String bdcqzh;
	@XmlAttribute(name = "QSZT", required = true)
    protected String qszt;
	@XmlAttribute(name = "FJ")
    protected String fj;
	@XmlAttribute(name = "DJSJ", required = true)
    protected String djsj;
	@XmlAttribute(name = "DBR", required = true)
    protected String dbr;
	@XmlAttribute(name = "DJJG", required = true)
    protected String djjg;
	@XmlAttribute(name = "QLLX", required = true)
    protected String qllx;
	@XmlAttribute(name = "YWH")
    protected String ywh;
	@XmlAttribute(name = "FDCJYJG")
    protected double fdcjyjg;
	@XmlAttribute(name = "TDSYJSSJ", required = true)
    protected String tdsyjssj;
	@XmlAttribute(name = "TDSYQSSJ", required = true)
    protected String tdsyqssj;
	@XmlAttribute(name = "TDSYQR", required = true)
	protected String tdsyqr;
	@XmlAttribute(name = "FDZL", required = true)
	protected String fdzl;
	@XmlAttribute(name = "DJYY", required = true)
	protected String djyy;
	@XmlAttribute(name = "DJLX", required = true)
	protected String djlx;
	
   
    @XmlAttribute(name = "QLID")
    protected String qlid;
    @XmlAttribute(name = "XMBH")
    protected String xmbh;
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;
	
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public double getDytdmj() {
		return dytdmj;
	}
	public void setDytdmj(double dytdmj) {
		this.dytdmj = StringHelper.getDouble(StringHelper.cut(dytdmj, 2));
	}
	public double getFttdmj() {
		return fttdmj;
	}
	public void setFttdmj(double fttdmj) {
		this.fttdmj = StringHelper.getDouble(StringHelper.cut(fttdmj, 2));
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
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
	public String getDjjg() {
		return djjg;
	}
	public void setDjjg(String djjg) {
		this.djjg = djjg;
	}
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
		this.qllx=ConstHelper.getReportValueByValue("QLLX", qllx);
	}
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	public double getFdcjyjg() {
		return fdcjyjg;
	}
	public void setFdcjyjg(double fdcjyjg) {
		this.fdcjyjg = StringHelper.getDouble(StringHelper.cut(fdcjyjg, 4));
	}
	public String getTdsyjssj() {
		return tdsyjssj;
	}
	public void setTdsyjssj(String tdsyjssj) {
		this.tdsyjssj = tdsyjssj;
	}
	public String getTdsyqssj() {
		return tdsyqssj;
	}
	public void setTdsyqssj(String tdsyqssj) {
		this.tdsyqssj = tdsyqssj;
	}
	public String getTdsyqr() {
		return tdsyqr;
	}
	public void setTdsyqr(String tdsyqr) {
		this.tdsyqr = tdsyqr;
	}
	public String getFdzl() {
		return fdzl;
	}
	public void setFdzl(String fdzl) {
		this.fdzl = fdzl;
	}
	public String getDjyy() {
		return djyy;
	}
	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}
	public String getDjlx() {
		return djlx;
	}
	public void setDjlx(String djlx) {
		this.djlx = djlx;
		this.djlx=ConstHelper.getReportValueByValue("DJLX", djlx);
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
		if(StringHelper.isEmpty(this.fdzl)){
			this.fdzl=null;
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
		if(StringHelper.isEmpty(this.tdsyjssj)){
			this.tdsyjssj=null;
		}
		if(StringHelper.isEmpty(this.tdsyqr)){
			this.tdsyqr=null;
		}
		if(StringHelper.isEmpty(this.tdsyqssj)){
			this.tdsyqssj=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.ywh)){
			this.ywh=null;
		}
	}
	
}
