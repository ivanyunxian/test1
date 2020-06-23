package com.supermap.realestate.registration.dataExchange.hy;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:用海状况表
 * @author diaoliwei
 * @date 2015年9月21日 下午10:24:59
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTF_ZHBHQK")
public class KTFZHYHZK {

    @XmlAttribute(name = "YHFS", required = true)
    private String yhfs;
    @XmlAttribute(name = "ZHDM", required = true)
    private String zhdm;
    @XmlAttribute(name = "QXDM", required = true)
    private String qxdm;
    @XmlAttribute(name = "YHMJ", required = true)
    private BigDecimal yhmj;
    @XmlAttribute(name = "JTYT", required = true)
    private String jtyt;
    @XmlAttribute(name = "SYJES", required = true)
    private BigDecimal syjes;
    @XmlAttribute(name = "BDCDYID")
    private String bdcdyid;
    
    
	public String getYhfs() {
		return yhfs;
	}
	public void setYhfs(String yhfs) {
		this.yhfs = yhfs;
	}
	public String getZhdm() {
		return zhdm;
	}
	public void setZhdm(String zhdm) {
		this.zhdm = zhdm;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public BigDecimal getYhmj() {
		return yhmj;
	}
	public void setYhmj(BigDecimal yhmj) {
		this.yhmj = StringHelper.cutBigDecimal(yhmj, 4);
	}
	public String getJtyt() {
		return jtyt;
	}
	public void setJtyt(String jtyt) {
		this.jtyt = jtyt;
	}
	public BigDecimal getSyjes() {
		return syjes;
	}
	public void setSyjes(BigDecimal syjes) {
		this.syjes = StringHelper.cutBigDecimal(syjes, 4);
	}
	
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.jtyt)){
			this.jtyt=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.yhfs)){
			this.yhfs=null;
		}
		if(StringHelper.isEmpty(this.zhdm)){
			this.zhdm=null;
		}
	}

}
