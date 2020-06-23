package com.supermap.realestate.registration.dataExchange.hy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:用海、用岛坐标表
 * @author diaoliwei
 * @date 2015年9月21日 下午9:41:47
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTF_ZH_YHYDZB")
public class KTFZHYHYDZB {

    @XmlAttribute(name = "ZHHDDM", required = true)
    private String zhhddm;
    @XmlAttribute(name = "XH", required = true)
    private int xh;
    @XmlAttribute(name = "BW", required = true)
    private double bw;
    @XmlAttribute(name = "DJ", required = true)
    private double dj;
    @XmlAttribute(name = "QXDM", required = true)
    private String qxdm;
    @XmlAttribute(name = "BDCDYID")
    private String bdcdyid;
    
	public String getZhhddm() {
		return zhhddm;
	}
	public void setZhhddm(String zhhddm) {
		this.zhhddm = zhhddm;
	}
	public int getXh() {
		return xh;
	}
	public void setXh(int xh) {
		this.xh = xh;
	}
	public double getBw() {
		return bw;
	}
	public void setBw(double bw) {
		this.bw = bw;
	}
	public double getDj() {
		return dj;
	}
	public void setDj(double dj) {
		this.dj = dj;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
    
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.zhhddm)){
			this.zhhddm=null;
		}
	}
}
