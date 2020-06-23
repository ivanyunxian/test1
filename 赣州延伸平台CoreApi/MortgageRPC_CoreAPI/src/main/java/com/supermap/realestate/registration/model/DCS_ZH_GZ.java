package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-25 
//* ----------------------------------------
//* Public Entity bdcs_zh_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_ZH_GZ;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.YHYDZB;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

@Entity
@Table(name = "bdcs_zh_gz", schema = "bdcdck")
public class DCS_ZH_GZ extends GenerateDCS_ZH_GZ implements Sea {

	@Override
	@Id
	@Column(name = "bdcdyid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "zhdm")
	public String getZHDM() {
		return super.getZHDM();
	}

	@Override
	@Column(name = "zhtzm")
	public String getZHTZM() {
		return super.getZHTZM();
	}

	private String zhtzmname;

	@Transient
	public String getZHTZMName() {
		if (zhtzmname == null) {
			if (this.getZHTZM() != null) {
				zhtzmname = ConstHelper.getNameByValue("TZM", this.getZHTZM());
			}
		}
		return zhtzmname;
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "xmxx")
	public String getXMXX() {
		return super.getXMXX();
	}

	private String xmxxname;

	@Transient
	public String getXMXXName() {
		if (xmxxname == null) {
			if (this.getXMXX() != null) {
				xmxxname = ConstHelper.getNameByValue("XMXZ", this.getXMXX());
			}
		}
		return xmxxname;
	}

	@Override
	@Column(name = "yhzmj")
	public Double getYHZMJ() {
		return super.getYHZMJ();
	}

	@Override
	@Column(name = "zhmj")
	public Double getZHMJ() {
		return super.getZHMJ();
	}

	@Override
	@Column(name = "db")
	public String getDB() {
		return super.getDB();
	}

	private String dbname;

	@Transient
	public String getDBName() {
		if (dbname == null) {
			if (this.getDB() != null) {
				dbname = ConstHelper.getNameByValue("HYDB", this.getDB());
			}
		}
		return dbname;
	}

	@Override
	@Column(name = "zhax")
	public Double getZHAX() {
		return super.getZHAX();
	}

	@Override
	@Column(name = "yhlxa")
	public String getYHLXA() {
		return super.getYHLXA();
	}

	private String yhlxaname;

	@Transient
	public String getYHLXAName() {
		if (yhlxaname == null) {
			if (this.getYHLXA() != null) {
				yhlxaname = ConstHelper.getNameByValue("HYSYLXA", this.getYHLXA());
			}
		}
		return yhlxaname;
	}

	@Override
	@Column(name = "yhlxb")
	public String getYHLXB() {
		return super.getYHLXB();
	}

	private String yhlxbname;

	@Transient
	public String getYHLXBName() {
		if (yhlxbname == null) {
			if (this.getYHLXB() != null) {
				yhlxbname = ConstHelper.getNameByValue("HYSYLXB", this.getYHLXB());
			}
		}
		return yhlxbname;
	}

	@Override
	@Column(name = "yhwzsm")
	public String getYHWZSM() {
		return super.getYHWZSM();
	}

	@Override
	@Column(name = "hddm")
	public String getHDDM() {
		return super.getHDDM();
	}

	@Override
	@Column(name = "hdmc")
	public String getHDMC() {
		return super.getHDMC();
	}

	@Override
	@Column(name = "ydfw")
	public String getYDFW() {
		return super.getYDFW();
	}

	@Override
	@Column(name = "ydmj")
	public Double getYDMJ() {
		return super.getYDMJ();
	}

	@Override
	@Column(name = "hdwz")
	public String getHDWZ() {
		return super.getHDWZ();
	}

	@Override
	@Column(name = "hdyt")
	public String getHDYT() {
		return super.getHDYT();
	}

	@Override
	@Column(name = "syqmj")
	public Double getSYQMJ() {
		return super.getSYQMJ();
	}

	@Override
	@Column(name = "syjze")
	public Double getSYJZE() {
		return super.getSYJZE();
	}

	@Override
	@Column(name = "syjbzyj")
	public String getSYJBZYJ() {
		return super.getSYJBZYJ();
	}

	@Override
	@Column(name = "syjjnqk")
	public String getSYJJNQK() {
		return super.getSYJJNQK();
	}

	@Override
	@Column(name = "zht")
	public String getZHT() {
		return super.getZHT();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	private String ztname;

	@Transient
	public String getZTName() {
		if (ztname == null) {
			if (this.getZT() != null) {
				ztname = ConstHelper.getNameByValue("BDCDYZT", this.getZT());
			} else {
				return "有效";
			}
		}
		return ztname;
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "hcjs")
	public String getHCJS() {
		return super.getHCJS();
	}

	@Override
	@Column(name = "hcr")
	public String getHCR() {
		return super.getHCR();
	}

	@Override
	@Column(name = "hcrq")
	public Date getHCRQ() {
		return super.getHCRQ();
	}

	@Override
	@Column(name = "cljs")
	public String getCLJS() {
		return super.getCLJS();
	}

	@Override
	@Column(name = "clr")
	public String getCLR() {
		return super.getCLR();
	}

	@Override
	@Column(name = "clrq")
	public Date getCLRQ() {
		return super.getCLRQ();
	}

	@Override
	@Column(name = "shyj")
	public String getSHYJ() {
		return super.getSHYJ();
	}

	@Override
	@Column(name = "shr")
	public String getSHR() {
		return super.getSHR();
	}

	@Override
	@Column(name = "shrq")
	public Date getSHRQ() {
		return super.getSHRQ();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	@Override
	@Column(name = "djzt")
	public String getDJZT() {
		return super.getDJZT();
	}

	private String djztname;

	@Transient
	public String getDJZTName() {
		if (djztname == null) {
			if (this.getDJZT() != null) {
				djztname = ConstHelper.getNameByValue("DJZT", this.getDJZT());
			} else {
				return "未登记";
			}
		}
		return djztname;
	}
	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}
	
	@Override
	@Column(name = "syrqzd")
	public String getSYRQZD() {
		return super.getSYRQZD();
	}
	
	@Override
	@Column(name = "syrqzx")
	public String getSYRQZX() {
		return super.getSYRQZX();
	}
	
	@Override
	@Column(name = "syrqzn")
	public String getSYRQZN() {
		return super.getSYRQZN();
	}
	
	@Override
	@Column(name = "syrqzb")
	public String getSYRQZB() {
		return super.getSYRQZB();
	}
	
	@Override
	@Column(name = "qdfs")
	public String getQDFS() {
		return super.getQDFS();
	}
	
	private String qdfsname;
	@Transient
	public String getQDFSName() {
		if (qdfsname == null) {
			if (this.getQDFS() != null) {
				qdfsname = ConstHelper.getNameByValue("HYQDFS", this.getQDFS());
			}
		}
		return qdfsname;
	}
	
	@Override
	@Column(name = "gzwlx")
	public String getGZWLX() {
		return super.getGZWLX();
	}
	
	private String gzwlxname;
	@Transient
	public String getGZWLXName() {
		if (gzwlxname == null) {
			if (this.getGZWLX() != null) {
				gzwlxname = ConstHelper.getNameByValue("GZWLX", this.getGZWLX());
			}
		}
		return gzwlxname;
	}
	/******************* 自定义部分 ****************/
	@Transient
	public BDCDYLX getBDCDYLX() {
		return BDCDYLX.HY;
	}

	@Transient
	public DJDYLY getLY() {
		return DJDYLY.DC;
	}


	@Transient
	public double getMJ() {
		double d = 0;
		if (super.getZHMJ() != null)
			d = super.getZHMJ();
		return d;
	}
	
	/**
	 * 用海状况接口
	 */

	private List<YHZK> yhzks;

	@Transient
	public List<YHZK> getYHZKS() {
		return yhzks;
	}

	public void setYHZKS(List<YHZK> yHZKS) {
		yhzks = yHZKS;
	}
	
	/**
	 * 用海用地坐标接口
	 */

	private List<YHYDZB> yhydzbs;

	@Transient
	public List<YHYDZB> getYHYDZBS() {
		return yhydzbs;
	}

	public void setYHYDZBS(List<YHYDZB> yHYDZBS) {
		yhydzbs = yHYDZBS;
	}

	@Override
	public void setYT(String yt) {
		// TODO Auto-generated method stub
		
	}
}