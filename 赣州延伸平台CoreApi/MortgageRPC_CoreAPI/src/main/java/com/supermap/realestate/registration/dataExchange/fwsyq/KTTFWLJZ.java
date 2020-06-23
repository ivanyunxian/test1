package com.supermap.realestate.registration.dataExchange.fwsyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:逻辑幢
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午9:41:00
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_FW_LJZ")
public class KTTFWLJZ {
	
	@XmlAttribute(name = "ZRZH", required = true)
    protected String zrzh;
	@XmlAttribute(name = "JZWZT")
    protected String jzwzt;
    @XmlAttribute(name = "FWJG3")
    protected String fwjg3;
    @XmlAttribute(name = "FWJG2")
    protected String fwjg2;
    @XmlAttribute(name = "FWJG1")
    protected String fwjg1;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "DXCS")
    protected int dxcs;
    @XmlAttribute(name = "DSCS")
    protected int dscs;
    @XmlAttribute(name = "ZCS")
    protected int zcs;
    @XmlAttribute(name = "SCQTMJ")
    protected double scqtmj;
    @XmlAttribute(name = "SCDXMJ")
    protected double scdxmj;
    @XmlAttribute(name = "YCQTMJ")
    protected double ycqtmj;
    @XmlAttribute(name = "JGRQ")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar jgrq;
    @XmlAttribute(name = "YCDXMJ")
    protected double ycdxmj;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "LJZH", required = true)
    protected String ljzh;
    @XmlAttribute(name = "MPH")
    protected String mph;
    @XmlAttribute(name = "FWYT1")
    protected String fwyt1;
    @XmlAttribute(name = "FWYT2")
    protected String fwyt2;
    @XmlAttribute(name = "FWYT3")
    protected String fwyt3;
    @XmlAttribute(name = "YCJZMJ")
    protected double ycjzmj;
    @XmlAttribute(name = "SCJZMJ")
    protected double scjzmj;
    
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;
    
	public String getZrzh() {
		return zrzh;
	}
	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public int getDxcs() {
		return dxcs;
	}
	public void setDxcs(int dxcs) {
		this.dxcs = dxcs;
	}
	public int getDscs() {
		return dscs;
	}
	public void setDscs(int dscs) {
		this.dscs = dscs;
	}
	public int getZcs() {
		return zcs;
	}
	public void setZcs(int zcs) {
		this.zcs = zcs;
	}
	public XMLGregorianCalendar getJgrq() {
		return jgrq;
	}
	public void setJgrq(XMLGregorianCalendar jgrq) {
		this.jgrq = jgrq;
	}
	public String getJzwzt() {
		return jzwzt;
	}
	public void setJzwzt(String jzwzt) {
		this.jzwzt = jzwzt;
		this.jzwzt=ConstHelper.getReportValueByValue("JZWZT", jzwzt);
	}
	public String getFwjg3() {
		return fwjg3;
	}
	public void setFwjg3(String fwjg3) {
		this.fwjg3 = fwjg3;
		this.fwjg3=ConstHelper.getReportValueByValue("FWJG", fwjg3);
	}
	public String getFwjg2() {
		return fwjg2;
	}
	public void setFwjg2(String fwjg2) {
		this.fwjg2 = fwjg2;
		this.fwjg2=ConstHelper.getReportValueByValue("FWJG", fwjg2);
	}
	public String getFwjg1() {
		return fwjg1;
	}
	public void setFwjg1(String fwjg1) {
		this.fwjg1 = fwjg1;
		this.fwjg1=ConstHelper.getReportValueByValue("FWJG", fwjg1);
	}
	public double getScqtmj() {
		return scqtmj;
	}
	public void setScqtmj(double scqtmj) {
		this.scqtmj = StringHelper.getDouble(StringHelper.cut(scqtmj, 3));
	}
	public double getScdxmj() {
		return scdxmj;
	}
	public void setScdxmj(double scdxmj) {
		this.scdxmj = StringHelper.getDouble(StringHelper.cut(scdxmj, 3));
	}
	public double getYcqtmj() {
		return ycqtmj;
	}
	public void setYcqtmj(double ycqtmj) {
		this.ycqtmj = StringHelper.getDouble(StringHelper.cut(ycqtmj, 3));
	}
	public double getYcdxmj() {
		return ycdxmj;
	}
	public void setYcdxmj(double ycdxmj) {
		this.ycdxmj = StringHelper.getDouble(StringHelper.cut(ycdxmj, 3));
	}
	public String getLjzh() {
		return ljzh;
	}
	public void setLjzh(String ljzh) {
		this.ljzh = ljzh;
	}
	public String getMph() {
		return mph;
	}
	public void setMph(String mph) {
		this.mph = mph;
	}
	public String getFwyt1() {
		return fwyt1;
	}
	public void setFwyt1(String fwyt1) {
		this.fwyt1 = fwyt1;
		this.fwyt1=ConstHelper.getReportValueByValue("FWYT", fwyt1);
	}
	public String getFwyt2() {
		return fwyt2;
	}
	public void setFwyt2(String fwyt2) {
		this.fwyt2 = fwyt2;
		this.fwyt2=ConstHelper.getReportValueByValue("FWYT", fwyt2);
	}
	public String getFwyt3() {
		return fwyt3;
	}
	public void setFwyt3(String fwyt3) {
		this.fwyt3 = fwyt3;
		this.fwyt3=ConstHelper.getReportValueByValue("FWYT", fwyt3);
	}
	public double getYcjzmj() {
		return ycjzmj;
	}
	public void setYcjzmj(double ycjzmj) {
		this.ycjzmj = StringHelper.getDouble(StringHelper.cut(ycjzmj, 3));
	}
	public double getScjzmj() {
		return scjzmj;
	}
	public void setScjzmj(double scjzmj) {
		this.scjzmj = StringHelper.getDouble(StringHelper.cut(scjzmj, 3));
	}
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	
    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.fwjg1)){
    		this.fwjg1=null;
    	}
    	if(StringHelper.isEmpty(this.fwjg2)){
    		this.fwjg2=null;
    	}
    	if(StringHelper.isEmpty(this.fwjg3)){
    		this.fwjg3=null;
    	}
    	if(StringHelper.isEmpty(this.fwyt1)){
    		this.fwyt1=null;
    	}
    	if(StringHelper.isEmpty(this.fwyt2)){
    		this.fwyt2=null;
    	}
    	if(StringHelper.isEmpty(this.fwyt3)){
    		this.fwyt3=null;
    	}
    	if(StringHelper.isEmpty(this.jgrq)){
    		this.jgrq=null;
    	}
    	if(StringHelper.isEmpty(this.jzwzt)){
    		this.jzwzt=null;
    	}
    	if(StringHelper.isEmpty(this.ljzh)){
    		this.ljzh=null;
    	}
    	if(StringHelper.isEmpty(this.mph)){
    		this.mph=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.zrzh)){
    		this.zrzh=null;
    	}
    }
}
