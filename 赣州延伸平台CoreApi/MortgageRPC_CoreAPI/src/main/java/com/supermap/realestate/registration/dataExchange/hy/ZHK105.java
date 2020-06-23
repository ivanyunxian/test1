package com.supermap.realestate.registration.dataExchange.hy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:宗海空间属性
 * @author diaoliwei
 * @date 2015年9月21日 下午9:50:21
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ZH_K_105")
public class ZHK105 {

	@XmlAttribute(name = "YZB", required = true)
	protected String yzb;
	@XmlAttribute(name = "XZB", required = true)
	protected String xzb;
	@XmlAttribute(name = "ZDX", required = true)
	protected int zdx;
	@XmlAttribute(name = "XH", required = true)
	protected int xh;
	@XmlAttribute(name = "BDCDYH", required = true)
	protected String bdcdyh;

	public String getYZB() {
		return yzb;
	}

	public void setYZB(String value) {
		this.yzb = value;
	}
 
	public String getXZB() {
		return xzb;
	}

	public void setXZB(String value) {
		this.xzb = value;
	}

	public int getZDX() {
		return zdx;
	}

	public void setZDX(int value) {
		this.zdx = value;
	}

	public int getXH() {
		return xh;
	}

	public void setXH(int value) {
		this.xh = value;
	}

	public String getBDCDYH() {
		return bdcdyh;
	}

	public void setBDCDYH(String value) {
		this.bdcdyh = value;
	}

	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bdcdyh)){
			this.bdcdyh=null;
		}
		if(StringHelper.isEmpty(this.xzb)){
			this.xzb=null;
		}
		if(StringHelper.isEmpty(this.yzb)){
			this.yzb=null;
		}
	}
}
