package com.supermap.realestate.registration.dataExchange.hy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:宗海变化情况表
 * @author diaoliwei
 * @date 2015年9月21日 下午9:24:59
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTF_ZHBHQK")
public class KTFZHBHQK {

    @XmlAttribute(name = "DBR", required = true)
    private String dbr;
    @XmlAttribute(name = "ZHDM", required = true)
    private String zhdm;
    @XmlAttribute(name = "QXDM", required = true)
    private String qxdm;
    @XmlAttribute(name = "BHYY", required = true)
    private String bhyy;
    @XmlAttribute(name = "DJSJ", required = true)
    private String djsj;
    @XmlAttribute(name = "BHNR", required = true)
    private String bhnr;

    public String getDBR() {
        return dbr;
    }

    public void setDBR(String value) {
        this.dbr = value;
    }

    public String getZHDM() {
        return zhdm;
    }

    public void setZHDM(String value) {
        this.zhdm = value;
    }

    public String getQXDM() {
        return qxdm;
    }

    public void setQXDM(String value) {
        this.qxdm = value;
    }

    public String getBHYY() {
        return bhyy;
    }

    public void setBHYY(String value) {
        this.bhyy = value;
    }

    public String getDJSJ() {
        return djsj;
    }

    public void setDJSJ(String value) {
        this.djsj = value;
    }

    public String getBHNR() {
        return bhnr;
    }

    public void setBHNR(String value) {
        this.bhnr = value;
    }
    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.bhnr)){
    		this.bhnr=null;
    	}
    	if(StringHelper.isEmpty(this.bhyy)){
    		this.bhyy=null;
    	}
    	if(StringHelper.isEmpty(this.dbr)){
    		this.dbr=null;
    	}
    	if(StringHelper.isEmpty(this.djsj)){
    		this.djsj=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.zhdm)){
    		this.zhdm=null;
    	}
    }

}
