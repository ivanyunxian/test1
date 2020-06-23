package com.supermap.realestate.registration.dataExchange.xzdy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 限制单元记录
 * @author 凌广清
 * @date 2019年3月14日 15点59分
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_XZDY")
public class QLFXZDY {

	@XmlAttribute(name = "YSDM")
	private String ysdm;
	@XmlAttribute(name = "YWH")
	private String ywh;
	@XmlAttribute(name = "DYBDCLX")
	private String dybdclx;
	@XmlAttribute(name = "BDCQZH")
	private String bdcqzh;
	@XmlAttribute(name = "BDCDYH")
	private String bdcdyh;
	@XmlAttribute(name = "BXZRMC")
	private String bxzrmc;
	@XmlAttribute(name = "BXZRZJZL")
	private String bxzrzjzl;
	@XmlAttribute(name = "BXZRZJHM")
	private String bxzrzjhm;
	@XmlAttribute(name = "XZWJHM")
	private String xzwjhm;
	@XmlAttribute(name = "XZDW")
	private String xzdw;
	@XmlAttribute(name = "SDTZRQ")
	private String sdtzrq;
	@XmlAttribute(name = "XZQSRQ")
	private String xzqsrq;
	@XmlAttribute(name = "XZZZRQ")
	private String xzzzrq;
	@XmlAttribute(name = "SLR")
	private String slr;
	@XmlAttribute(name = "SLRYJ")
	private String slryj;
	@XmlAttribute(name = "XZLX")
	private String xzlx;
	@XmlAttribute(name = "LSXZ")
	private String lsxz;
	@XmlAttribute(name = "XZFW")
	private String xzfw;
	@XmlAttribute(name = "DJSJ")
	private String djsj;
	@XmlAttribute(name = "DBR")
	private String dbr;
	@XmlAttribute(name = "BZ")
	private String bz;
	@XmlAttribute(name = "ZXDJSJ")
	private String zxdjsj;
	@XmlAttribute(name = "ZXDBR")
	private String zxdbr;
	@XmlAttribute(name = "ZXYWH")
	private String zxywh;
	@XmlAttribute(name = "ZXBZ")
	private String zxbz;
	@XmlAttribute(name = "ZXYJ")
	private String zxyj;
	@XmlAttribute(name = "ZXXZWJHM")
	private String zxxzwjhm;
	@XmlAttribute(name = "ZXXZDW")
	private String zxxzdw;
	@XmlAttribute(name = "BDCDYID")
	private String bdcdyid;
	@XmlAttribute(name = "DYXZID")
	private String dyxzid;
	@XmlAttribute(name = "YXBZ")
	private String yxbz;
	@XmlAttribute(name = "XMBH")
	private String xmbh;
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	public String getDybdclx() {
		return dybdclx;
	}
	public void setDybdclx(String dybdclx) {
		this.dybdclx = dybdclx;
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getBxzrmc() {
		return bxzrmc;
	}
	public void setBxzrmc(String bxzrmc) {
		this.bxzrmc = bxzrmc;
	}
	public String getBxzrzjzl() {
		return bxzrzjzl;
	}
	public void setBxzrzjzl(String bxzrzjzl) {
		this.bxzrzjzl = bxzrzjzl;
	}
	public String getBxzrzjhm() {
		return bxzrzjhm;
	}
	public void setBxzrzjhm(String bxzrzjhm) {
		this.bxzrzjhm = bxzrzjhm;
	}
	public String getXzwjhm() {
		return xzwjhm;
	}
	public void setXzwjhm(String xzwjhm) {
		this.xzwjhm = xzwjhm;
	}
	public String getXzdw() {
		return xzdw;
	}
	public void setXzdw(String xzdw) {
		this.xzdw = xzdw;
	}
	public String getSdtzrq() {
		return sdtzrq;
	}
	public void setSdtzrq(String sdtzrq) {
		this.sdtzrq = sdtzrq;
	}
	public String getXzqsrq() {
		return xzqsrq;
	}
	public void setXzqsrq(String xzqsrq) {
		this.xzqsrq = xzqsrq;
	}
	public String getXzzzrq() {
		return xzzzrq;
	}
	public void setXzzzrq(String xzzzrq) {
		this.xzzzrq = xzzzrq;
	}
	public String getSlr() {
		return slr;
	}
	public void setSlr(String slr) {
		this.slr = slr;
	}
	public String getSlryj() {
		return slryj;
	}
	public void setSlryj(String slryj) {
		this.slryj = slryj;
	}
	public String getXzlx() {
		return xzlx;
	}
	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}
	public String getLsxz() {
		return lsxz;
	}
	public void setLsxz(String lsxz) {
		this.lsxz = lsxz;
	}
	public String getXzfw() {
		return xzfw;
	}
	public void setXzfw(String xzfw) {
		this.xzfw = xzfw;
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
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getZxdjsj() {
		return zxdjsj;
	}
	public void setZxdjsj(String zxdjsj) {
		this.zxdjsj = zxdjsj;
	}
	public String getZxdbr() {
		return zxdbr;
	}
	public void setZxdbr(String zxdbr) {
		this.zxdbr = zxdbr;
	}
	public String getZxywh() {
		return zxywh;
	}
	public void setZxywh(String zxywh) {
		this.zxywh = zxywh;
	}
	public String getZxbz() {
		return zxbz;
	}
	public void setZxbz(String zxbz) {
		this.zxbz = zxbz;
	}
	public String getZxyj() {
		return zxyj;
	}
	public void setZxyj(String zxyj) {
		this.zxyj = zxyj;
	}
	public String getZxxzwjhm() {
		return zxxzwjhm;
	}
	public void setZxxzwjhm(String zxxzwjhm) {
		this.zxxzwjhm = zxxzwjhm;
	}
	public String getZxxzdw() {
		return zxxzdw;
	}
	public void setZxxzdw(String zxxzdw) {
		this.zxxzdw = zxxzdw;
	}
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	public String getDyxzid() {
		return dyxzid;
	}
	public void setDyxzid(String dyxzid) {
		this.dyxzid = dyxzid;
	}
	public String getYxbz() {
		return yxbz;
	}
	public void setYxbz(String yxbz) {
		this.yxbz = yxbz;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	
	public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    	if(StringHelper.isEmpty(this.dybdclx)){
    		this.dybdclx=null;
    	}
    	if(StringHelper.isEmpty(this.bdcqzh)){
    		this.bdcqzh=null;
    	}
    	if(StringHelper.isEmpty(this.bdcdyh)){
    		this.bdcdyh=null;
    	}
    	if(StringHelper.isEmpty(this.bxzrmc)){
    		this.bxzrmc=null;
    	}
    	if(StringHelper.isEmpty(this.bxzrzjzl)){
    		this.bxzrzjzl=null;
    	}
    	if(StringHelper.isEmpty(this.bxzrzjhm)){
    		this.bxzrzjhm=null;
    	}
    	if(StringHelper.isEmpty(this.xzwjhm)){
    		this.xzwjhm=null;
    	}
    	if(StringHelper.isEmpty(this.xzdw)){
    		this.xzdw=null;
    	}
    	if(StringHelper.isEmpty(this.sdtzrq)){
    		this.sdtzrq=null;
    	}
    	if(StringHelper.isEmpty(this.xzzzrq)){
    		this.xzzzrq=null;
    	}
    	if(StringHelper.isEmpty(this.slr)){
    		this.slr=null;
    	}
    	if(StringHelper.isEmpty(this.xzlx)){
    		this.xzlx=null;
    	}
    	if(StringHelper.isEmpty(this.lsxz)){
    		this.lsxz=null;
    	}
    	if(StringHelper.isEmpty(this.xzfw)){
    		this.xzfw=null;
    	}
    	if(StringHelper.isEmpty(this.djsj)){
    		this.djsj=null;
    	}
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.zxdjsj)){
    		this.zxdjsj=null;
    	}
    	if(StringHelper.isEmpty(this.zxdbr)){
    		this.zxdbr=null;
    	}
    	if(StringHelper.isEmpty(this.zxywh)){
    		this.zxywh=null;
    	}
    	if(StringHelper.isEmpty(this.zxbz)){
    		this.zxbz=null;
    	}
    	if(StringHelper.isEmpty(this.zxyj)){
    		this.zxyj=null;
    	}
    	if(StringHelper.isEmpty(this.zxxzwjhm)){
    		this.zxxzwjhm=null;
    	}
    	if(StringHelper.isEmpty(this.zxxzdw)){
    		this.zxxzdw=null;
    	}
    	if(StringHelper.isEmpty(this.bdcdyid)){
    		this.bdcdyid=null;
    	}
    	if(StringHelper.isEmpty(this.dyxzid)){
    		this.dyxzid=null;
    	}
    	if(StringHelper.isEmpty(this.yxbz)){
    		this.yxbz=null;
    	}
    	if(StringHelper.isEmpty(this.xmbh)){
    		this.xmbh=null;
    	}
	}
	
}
