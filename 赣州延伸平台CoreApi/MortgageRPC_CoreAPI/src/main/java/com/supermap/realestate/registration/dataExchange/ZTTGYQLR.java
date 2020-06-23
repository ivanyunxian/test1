package com.supermap.realestate.registration.dataExchange;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.internal.util.StringHelper;

import com.supermap.realestate.registration.util.ConstHelper;


/**
 * 
 * @Description:权利人表
 * @author diaoliwei
 * @date 2015年10月26日 下午4:01:10
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ZTT_GY_QLR")
public class ZTTGYQLR {

    @XmlAttribute(name = "DZYJ")
    protected String dzyj;
    @XmlAttribute(name = "ZJH", required = true)
    protected String zjh;
    @XmlAttribute(name = "DZ")
    protected String dz;
    @XmlAttribute(name = "FZJG")
    protected String fzjg;
    @XmlAttribute(name = "YB")
    protected String yb;
    @XmlAttribute(name = "XB", required = true)
    protected String xb;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "GZDW")
    protected String gzdw;
    @XmlAttribute(name = "GJ", required = true)
    protected String gj;
    @XmlAttribute(name = "QLRMC", required = true)
    protected String qlrmc;
    @XmlAttribute(name = "ZJZL", required = true)
    protected String zjzl;
    @XmlAttribute(name = "DH")
    protected String dh;
    @XmlAttribute(name = "HJSZSS")
    protected String hjszss;
    @XmlAttribute(name = "SSHY")
    protected String sshy;

    @XmlAttribute(name = "YSDM")
    protected String ysdm;
    @XmlAttribute(name = "BDCDYH")
    protected String bdcdyh;
    @XmlAttribute(name = "BDCQZH")
    protected String bdcqzh;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "GYQK")
    protected String gyqk;
    @XmlAttribute(name = "GYFS")
    protected String gyfs;
    @XmlAttribute(name = "QLBL")
    protected String qlbl;
    @XmlAttribute(name = "QLRLX", required = true)
    protected String qlrlx;
    @XmlAttribute(name = "SFCZR")
    protected String sfczr;
    @XmlAttribute(name = "QZYSXLH")
    protected String qzysxlh;
    @XmlAttribute(name = "SXH")
    protected String sxh;
    
    @XmlAttribute(name = "QLID")
    protected String qlid;
	@XmlAttribute(name = "XMBH")
	protected String xmbh;
	@XmlAttribute(name = "SQRID")
    protected String sqrid;
	@XmlAttribute(name = "QLRID")
    protected String qlrid;
    
    
    public String getSXH() {
        return sxh;
    }

    /**
     * Sets the value of the sxh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSXH(String value) {
        this.sxh = value;
    }
    
    /**
     * Gets the value of the qzysxlh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQZYSXLH() {
        return qzysxlh;
    }

    /**
     * Sets the value of the qzysxlh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQZYSXLH(String value) {
        this.qzysxlh = value;
    }
    
    /**
     * Gets the value of the sfczr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFCZR() {
        return sfczr;
    }

    /**
     * Sets the value of the sfczr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFCZR(String value) {
        this.sfczr = value;
        this.sfczr=ConstHelper.getReportValueByValue("SFZD", sfczr);
    }
    
    public String getQLRLX() {
        return qlrlx;
    }

    public void setQLRLX(String value) {
        this.qlrlx = value;
        this.qlrlx=ConstHelper.getReportValueByValue("QLRLX", qlrlx);
    }
    
    public String getQLBL() {
        return qlbl;
    }

    public void setQLBL(String value) {
        this.qlbl = value;
    }
    
    public String getGYFS() {
        return gyfs;
    }

    public void setGYFS(String value) {
        this.gyfs = value;
        this.gyfs=ConstHelper.getReportValueByValue("GYFS", gyfs);
    }
    
    public String getGYQK() {
        return gyqk;
    }

    public void setGYQK(String value) {
        this.gyqk = value;
    }
    
    public String getBZ() {
        return bz;
    }

    public void setBZ(String value) {
        this.bz = value;
    }
    
    public String getBDCQZH() {
        return bdcqzh;
    }

    public void setBDCQZH(String value) {
        this.bdcqzh = value;
    }

    public String getDZYJ() {
        return dzyj;
    }

    public void setDZYJ(String value) {
        this.dzyj = value;
    }

    public String getZJH() {
        return zjh;
    }

    public void setZJH(String value) {
        this.zjh = value;
    }

    public String getDZ() {
        return dz;
    }

    public void setDZ(String value) {
        this.dz = value;
    }

    public String getFZJG() {
        return fzjg;
    }

    public void setFZJG(String value) {
        this.fzjg = value;
    }

    public String getYB() {
        return yb;
    }

    public void setYB(String value) {
        this.yb = value;
    }

    public String getXB() {
        return xb;
    }

    public void setXB(String value) {
        this.xb = value;
        this.xb=ConstHelper.getReportValueByValue("XB", xb);
    }

    public String getQXDM() {
        return qxdm;
    }

    public void setQXDM(String value) {
        this.qxdm = value;
    }

    public String getGZDW() {
        return gzdw;
    }

    public void setGZDW(String value) {
        this.gzdw = value;
    }

    public String getGJ() {
        return gj;
    }

    public void setGJ(String value) {
        this.gj = value;
        this.gj=ConstHelper.getReportValueByValue("GJDQ", gj);
    }

    public String getQLRMC() {
        return qlrmc;
    }

    public void setQLRMC(String value) {
        this.qlrmc = value;
    }

    public String getZJZL() {
        return zjzl;
    }

    public void setZJZL(String value) {
        this.zjzl = value;
        this.zjzl=ConstHelper.getReportValueByValue("ZJLX", zjzl);
    }

    public String getDH() {
        return dh;
    }
 
    public void setDH(String value) {
        this.dh = value;
    }

    public String getHJSZSS() {
        return hjszss;
    }

    public void setHJSZSS(String value) {
        this.hjszss = value;
        this.hjszss=ConstHelper.getReportValueByValue("SS", hjszss);
    }

    public String getSSHY() {
        return sshy;
    }

    public void setSSHY(String value) {
        this.sshy = value;
        this.sshy=ConstHelper.getReportValueByValue("SSHY", sshy);
    }
    
    public String getYSDM() {
        return ysdm;
    }

    public void setYSDM(String value) {
        this.ysdm = value;
    }
    
    public String getBDCDYH() {
        return bdcdyh;
    }

    public void setBDCDYH(String value) {
        this.bdcdyh = value;
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
	public String getSqrid() {
		return sqrid;
	}
	public void setSqrid(String sqrid) {
		this.sqrid = sqrid;
	}
	public String getQlrid() {
		return qlrid;
	}
	public void setQlrid(String qlrid) {
		this.qlrid = qlrid;
	}
  
    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.bdcdyh)){
    		this.bdcdyh=null;
    	}
    	if(StringHelper.isEmpty(this.bdcqzh)){
    		this.bdcqzh=null;
    	}
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.dh)){
    		this.dh=null;
    	}
    	if(StringHelper.isEmpty(this.dz)){
    		this.dz=null;
    	}
    	if(StringHelper.isEmpty(this.dzyj)){
    		this.dzyj=null;
    	}
    	if(StringHelper.isEmpty(this.fzjg)){
    		this.fzjg=null;
    	}
    	if(StringHelper.isEmpty(this.gj)){
    		this.gj=null;
    	}
    	if(StringHelper.isEmpty(this.gyfs)){
    		this.gyfs=null;
    	}
    	if(StringHelper.isEmpty(this.gyqk)){
    		this.gyqk=null;
    	}
    	if(StringHelper.isEmpty(this.gzdw)){
    		this.gzdw=null;
    	}
    	if(StringHelper.isEmpty(this.hjszss)){
    		this.hjszss=null;
    	}
    	if(StringHelper.isEmpty(this.qlbl)){
    		this.qlbl=null;
    	}
    	if(StringHelper.isEmpty(this.qlrlx)){
    		this.qlrlx=null;
    	}
    	if(StringHelper.isEmpty(this.qlrmc)){
    		this.qlrmc=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.qzysxlh)){
    		this.qzysxlh=null;
    	}
    	if(StringHelper.isEmpty(this.sfczr)){
    		this.sfczr=null;
    	}
    	if(StringHelper.isEmpty(this.sshy)){
    		this.sshy=null;
    	}
    	if(StringHelper.isEmpty(this.sxh)){
    		this.sxh=null;
    	}
    	if(StringHelper.isEmpty(this.xb)){
    		this.xb=null;
    	}
    	if(StringHelper.isEmpty(this.yb)){
    		this.yb=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.zjh)){
    		this.zjh=null;
    	}
    	if(StringHelper.isEmpty(this.zjzl)){
    		this.zjzl=null;
    	}
    }
}
