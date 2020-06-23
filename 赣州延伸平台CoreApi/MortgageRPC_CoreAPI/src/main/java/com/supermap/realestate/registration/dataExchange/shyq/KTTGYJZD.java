package com.supermap.realestate.registration.dataExchange.shyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:界址点
 * @author diaoliwei
 * @date 2015年10月17日 下午8:38:01
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_GY_JZD")
public class KTTGYJZD {
	
	@XmlAttribute(name = "BSM", required = true)
	protected int bsm;
	@XmlAttribute(name = "ZDZHDM", required = true)
	protected String zdzhdm;
	@XmlAttribute(name = "YSDM", required = true)
	protected String ysdm;
    @XmlAttribute(name = "JZDH", required = true)
    protected String jzdh;
    @XmlAttribute(name = "JBLX", required = true)
    protected String jblx;
    @XmlAttribute(name = "JZDLX", required = true)
    protected String jzdlx;
	@XmlAttribute(name = "XZBZ")
	protected String xzbz;
	@XmlAttribute(name = "YZBZ")
	protected String yzbz;
	@XmlAttribute(name = "SXH")
	protected String sxh;	
	
	public int getBsm() {
		return bsm;
	}
	public void setBsm(int bsm) {
		this.bsm = bsm;
	}
	public String getZdzhdm() {
		return zdzhdm;
	}
	public void setZdzhdm(String zdzhdm) {
		this.zdzhdm = zdzhdm;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getJzdh() {
		return jzdh;
	}
	public void setJzdh(String jzdh) {
		this.jzdh = jzdh;
	}
	public String getJblx() {
		return jblx;
	}
	public void setJblx(String jblx) {
		this.jblx = jblx;
		this.jblx=ConstHelper.getReportValueByValue("JBLX", jblx);
	}
	public String getJzdlx() {
		return jzdlx;
	}
	public void setJzdlx(String jzdlx) {
		this.jzdlx = jzdlx;
		this.jzdlx=ConstHelper.getReportValueByValue("JZDLX", jzdlx);
	}
	public String getXzbz() {
		return xzbz;
	}
	public void setXzbz(String xzbz) {
		this.xzbz = xzbz;
	}
	public String getYzbz() {
		return yzbz;
	}
	public void setYzbz(String yzbz) {
		this.yzbz = yzbz;
	}
	public String getSxh() {
		return sxh;
	}
	public void setSxh(String sxh) {
		this.sxh = sxh;
	}
	
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.jblx)){
			this.jblx=null;
		}
		if(StringHelper.isEmpty(this.jzdh)){
			this.jzdh=null;
		}
		if(StringHelper.isEmpty(this.jzdlx)){
			this.jzdlx=null;
		}
		if(StringHelper.isEmpty(this.sxh)){
			this.sxh=null;
		}
		if(StringHelper.isEmpty(this.xzbz)){
			this.xzbz=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.yzbz)){
			this.yzbz=null;
		}
		if(StringHelper.isEmpty(this.zdzhdm)){
			this.zdzhdm=null;
		}
	}

}
