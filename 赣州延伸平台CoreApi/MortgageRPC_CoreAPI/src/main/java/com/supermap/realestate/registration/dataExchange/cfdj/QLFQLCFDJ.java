package com.supermap.realestate.registration.dataExchange.cfdj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:查封登记表
 * @author diaoliwei
 * @date 2015年8月31日 下午8:38:43
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_CFDJ")
public class QLFQLCFDJ {

    @XmlAttribute(name = "YSDM", required = true)
    private String ysdm;
    @XmlAttribute(name = "BDCDYH", required = true)
    private String bdcdyh;
    @XmlAttribute(name = "YWH")
    private String ywh;
    @XmlAttribute(name = "CFJG", required = true)
    private String cfjg;
    @XmlAttribute(name = "CFLX", required = true)
    private String cflx;
    @XmlAttribute(name = "CFWJ", required = true)
    private String cfwj;
    @XmlAttribute(name = "CFWH", required = true)
    private String cfwh;
    @XmlAttribute(name = "CFQSSJ", required = true)
    @XmlSchemaType(name = "dateTime")
    private String cfqssj;
    @XmlAttribute(name = "CFJSSJ", required = true)
    private String cfjssj;
    @XmlAttribute(name = "CFFW", required = true)
    private String cffw;
    @XmlAttribute(name = "QXDM", required = true)
    private String qxdm;
    @XmlAttribute(name = "DJJG", required = true)
    private String djjg;
    @XmlAttribute(name = "DBR", required = true)
    private String dbr;
    @XmlAttribute(name = "DJSJ", required = true)
    private String djsj;
    @XmlAttribute(name = "JFYWH")
    private String jfywh;
    @XmlAttribute(name = "JFJG")
    private String jfjg;
    @XmlAttribute(name = "JFWJ")
    private String jfwj;
    @XmlAttribute(name = "JFWH")
    private String jfwh;
    @XmlAttribute(name = "JFDBR")
    private String jfdbr;
    @XmlAttribute(name = "JFDJSJ")
    @XmlSchemaType(name = "dateTime")
    private String jfdjsj;
    @XmlAttribute(name = "FJ")
    private String fj;
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
	public String getCfjg() {
		return cfjg;
	}
	public void setCfjg(String cfjg) {
		this.cfjg = cfjg;
	}
	public String getCflx() {
		return cflx;
	}
	public void setCflx(String cflx) {
		this.cflx = cflx;
		this.cflx=ConstHelper.getReportValueByValue("CFLX", cflx);
	}
	public String getCfwj() {
		return cfwj;
	}
	public void setCfwj(String cfwj) {
		this.cfwj = cfwj;
	}
	public String getCfwh() {
		return cfwh;
	}
	public void setCfwh(String cfwh) {
		this.cfwh = cfwh;
	}
	public String getCfqssj() {
		return cfqssj;
	}
	public void setCfqssj(String cfqssj) {
		this.cfqssj = cfqssj;
	}
	public String getCfjssj() {
		return cfjssj;
	}
	public void setCfjssj(String cfjssj) {
		this.cfjssj = cfjssj;
	}
	public String getCffw() {
		return cffw;
	}
	public void setCffw(String cffw) {
		this.cffw = cffw;
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
	public String getJfywh() {
		return jfywh;
	}
	public void setJfywh(String jfywh) {
		this.jfywh = jfywh;
	}
	public String getJfjg() {
		return jfjg;
	}
	public void setJfjg(String jfjg) {
		this.jfjg = jfjg;
	}
	public String getJfwj() {
		return jfwj;
	}
	public void setJfwj(String jfwj) {
		this.jfwj = jfwj;
	}
	public String getJfwh() {
		return jfwh;
	}
	public void setJfwh(String jfwh) {
		this.jfwh = jfwh;
	}
	public String getJfdbr() {
		return jfdbr;
	}
	public void setJfdbr(String jfdbr) {
		this.jfdbr = jfdbr;
	}
	public String getJfdjsj() {
		return jfdjsj;
	}
	public void setJfdjsj(String jfdjsj) {
		this.jfdjsj = jfdjsj;
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
    public void replaceEmpyt(){
    	if(StringHelper.isEmpty(this.bdcdyh)){
    		this.bdcdyh=null;
    	}
    	if(StringHelper.isEmpty(this.cffw)){
    		this.cffw=null;
    	}
    	if(StringHelper.isEmpty(this.cfjg)){
    		this.cfjg=null;
    	}
    	if(StringHelper.isEmpty(this.cfjssj)){
    	this.cfjssj=null;
    	}
    	if(StringHelper.isEmpty(this.cflx)){
    		this.cflx=null;
    	}
    	if(StringHelper.isEmpty(this.cfqssj)){
    		this.cfqssj=null;
    	}
    	if(StringHelper.isEmpty(this.cfwh)){
    		this.cfwh=null;
    	}
    	if(StringHelper.isEmpty(this.dbr)){
    		this.dbr=null;
    	}
    	if(StringHelper.isEmpty(this.djjg)){
    		this.djjg=null;
    	}
    	if(StringHelper.isEmpty(this.djsj)){
    		this.djsj=null;
    	}
    	if(StringHelper.isEmpty(this.fj)){
    		this.fj=null;
    	}
    	if(StringHelper.isEmpty(this.jfdbr)){
    		this.jfdbr=null;
    	}
    	if(StringHelper.isEmpty(this.jfdjsj)){
    		this.jfdjsj=null;
    	}
    	if(StringHelper.isEmpty(this.jfjg)){
    		this.jfjg=null;
    	}
    	if(StringHelper.isEmpty(this.jfwh)){
    		this.jfwh=null;
    	}
    	if(StringHelper.isEmpty(this.jfwj)){
    		this.jfwj=null;
    	}
    	if(StringHelper.isEmpty(this.jfywh)){
    		this.jfywh=null;
    	}
    	if(StringHelper.isEmpty(this.qszt)){
    		this.qszt=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    }
}
