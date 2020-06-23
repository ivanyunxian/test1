package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_shyqzd_xz 
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

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

@Entity
@Table(name = "bdcs_shyqzd_xz", schema = "bdck")
public class BDCS_SHYQZD_XZ extends GenerateBDCS_SHYQZD_XZ implements UseLand {

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
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "zdtzm")
	public String getZDTZM() {
		return super.getZDTZM();
	}

	private String zdtzmname;

	@Transient
	public String getZDTZMName() {
		if (zdtzmname == null) {
			if (this.getZDTZM() != null) {
				zdtzmname = ConstHelper.getNameByValue("TZM", this.getZDTZM());
			}
		}
		return zdtzmname;
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "zdmj")
	public Double getZDMJ() {
		return super.getZDMJ();
	}

	@Override
	@Column(name = "mjdw")
	public String getMJDW() {
		return super.getMJDW();
	}

	private String mjdwname;

	@Transient
	public String getMJDWName() {
		if (mjdwname == null) {
			if (this.getMJDW() != null) {
				mjdwname = ConstHelper.getNameByValue("MJDW", this.getMJDW());
			}
		}
		return mjdwname;
	}

	@Override
	@Column(name = "yt")
	public String getYT() {
		return super.getYT();
	}

	private String ytname;

	@Transient
	public String getYTName() {
		if (ytname == null) {
			if (this.getYT() != null) {
				ytname = ConstHelper.getNameByValue("TDYT", this.getYT());
			}
		}
		return ytname;
	}

	@Override
	@Column(name = "dj")
	public String getDJ() {
		return super.getDJ();
	}

	private String djname;

	@Transient
	public String getDJName() {
		if (djname == null) {
			if (this.getDJ() != null) {
				djname = ConstHelper.getNameByValue("TDDJ", this.getDJ());
			}
		}
		return djname;
	}

	@Override
	@Column(name = "jg")
	public Double getJG() {
		return super.getJG();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	private String qllxname;

	@Transient
	public String getQLLXName() {
		if (qllxname == null) {
			if (this.getQLLX() != null) {
				qllxname = ConstHelper.getNameByValue("QLLX", this.getQLLX());
			}
		}
		return qllxname;
	}

	@Override
	@Column(name = "qlxz")
	public String getQLXZ() {
		return super.getQLXZ();
	}

	private String qlxzname;

	@Transient
	public String getQLXZName() {
		if (qlxzname == null) {
			if (this.getQLXZ() != null) {
				qlxzname = ConstHelper.getNameByValue("QLXZ", this.getQLXZ());
			}
		}
		return qlxzname;
	}

	@Override
	@Column(name = "qlsdfs")
	public String getQLSDFS() {
		return super.getQLSDFS();
	}

	private String qlsdfsname;

	@Transient
	public String getQLSDFSName() {
		if (qlsdfsname == null) {
			if (this.getQLSDFS() != null) {
				qlsdfsname = ConstHelper.getNameByValue("QLSDFS", this.getQLSDFS());
			}
		}
		return qlsdfsname;
	}

	@Override
	@Column(name = "rjl")
	public String getRJL() {
		return super.getRJL();
	}

	@Override
	@Column(name = "jzmd")
	public String getJZMD() {
		return super.getJZMD();
	}

	@Override
	@Column(name = "jzxg")
	public Double getJZXG() {
		return super.getJZXG();
	}

	@Override
	@Column(name = "zdszd")
	public String getZDSZD() {
		return super.getZDSZD();
	}

	@Override
	@Column(name = "zdszx")
	public String getZDSZX() {
		return super.getZDSZX();
	}

	@Override
	@Column(name = "zdszn")
	public String getZDSZN() {
		return super.getZDSZN();
	}

	@Override
	@Column(name = "zdszb")
	public String getZDSZB() {
		return super.getZDSZB();
	}

	@Override
	@Column(name = "zdt")
	public String getZDT() {
		return super.getZDT();
	}

	@Override
	@Column(name = "tfh")
	public String getTFH() {
		return super.getTFH();
	}

	@Override
	@Column(name = "djh")
	public String getDJH() {
		return super.getDJH();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "djqdm")
	public String getDJQDM() {
		return super.getDJQDM();
	}

	@Override
	@Column(name = "djzqdm")
	public String getDJZQDM() {
		return super.getDJZQDM();
	}

	@Override
	@Column(name = "zh")
	public String getZH() {
		return super.getZH();
	}

	@Override
	@Column(name = "qxmc")
	public String getQXMC() {
		return super.getQXMC();
	}

	@Override
	@Column(name = "djqmc")
	public String getDJQMC() {
		return super.getDJQMC();
	}

	@Override
	@Column(name = "djzqmc")
	public String getDJZQMC() {
		return super.getDJZQMC();
	}

	@Override
	@Column(name = "zm")
	public String getZM() {
		return super.getZM();
	}

	@Override
	@Column(name = "syqlx")
	public String getSYQLX() {
		return super.getSYQLX();
	}

	private String syqlxname;

	@Transient
	public String getSYQLXName() {
		if (syqlxname == null) {
			if (this.getSYQLX() != null) {
				syqlxname = ConstHelper.getNameByValue("SYQLX", this.getSYQLX());
			}
		}
		return syqlxname;
	}

	@Override
	@Column(name = "tdqslyzmcl")
	public String getTDQSLYZMCL() {
		return super.getTDQSLYZMCL();
	}

	@Override
	@Column(name = "gmjjhyfldm")
	public String getGMJJHYFLDM() {
		return super.getGMJJHYFLDM();
	}

	@Override
	@Column(name = "ybzddm")
	public String getYBZDDM() {
		return super.getYBZDDM();
	}

	@Override
	@Column(name = "blc")
	public String getBLC() {
		return super.getBLC();
	}

	@Override
	@Column(name = "pzyt")
	public String getPZYT() {
		return super.getPZYT();
	}

	private String pzytname;

	@Transient
	public String getPZYTName() {
		if (pzytname == null) {
			if (this.getPZYT() != null) {
				pzytname = ConstHelper.getNameByValue("TDYT", this.getPZYT());
			}
		}
		return pzytname;
	}

	@Override
	@Column(name = "pzmj")
	public Double getPZMJ() {
		return super.getPZMJ();
	}

	@Override
	@Column(name = "jzzdmj")
	public Double getJZZDMJ() {
		return super.getJZZDMJ();
	}

	@Override
	@Column(name = "jzmj")
	public Double getJZMJ() {
		return super.getJZMJ();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "cqzt")
	public String getCQZT() {
		return super.getCQZT();
	}

	@Override
	@Column(name = "dyzt")
	public String getDYZT() {
		return super.getDYZT();
	}

	@Override
	@Column(name = "xzzt")
	public String getXZZT() {
		return super.getXZZT();
	}

	@Override
	@Column(name = "blzt")
	public String getBLZT() {
		return super.getBLZT();
	}

	@Override
	@Column(name = "yyzt")
	public String getYYZT() {
		return super.getYYZT();
	}

	@Override
	@Column(name = "jzdwsm")
	public String getJZDWSM() {
		return super.getJZDWSM();
	}

	@Override
	@Column(name = "jzxzxsm")
	public String getJZXZXSM() {
		return super.getJZXZXSM();
	}

	@Override
	@Column(name = "dcjs")
	public String getDCJS() {
		return super.getDCJS();
	}

	@Override
	@Column(name = "dcr")
	public String getDCR() {
		return super.getDCR();
	}

	@Override
	@Column(name = "dcrq")
	public Date getDCRQ() {
		return super.getDCRQ();
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
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "bgzt")
	public String getBGZT() {
		return super.getBGZT();
	}

	@Override
	@Column(name = "djzt")
	public String getDJZT() {
		return super.getDJZT();
	}

	@Override
	@Column(name = "zdlx")
	public String getZDLX() {
		return super.getZDLX();
	}
	private String djztname;

	@Transient
	public String getDJZTName() {
		if (djztname == null) {
			if (this.getDJZT() != null) {
				djztname = ConstHelper.getNameByValue("DJZT", this.getDJZT());
			}
		}
		return djztname;
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
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}
	
	@Override
	@Column(name = "FTXS")
	public String getFTXS() {
		return super.getFTXS();
	}
	
	@Override
	@Column(name = "MS")
	public String getMS() {
		return super.getMS();
	}
	
	@Override
	@Column(name = "BZ")
	public String getBZ() {
		return super.getBZ();
	}
	
	@Override
	@Column(name="GLZRZID")
	public String getGLZRZID(){
		return super.getGLZRZID();
	}
	
	@Override
	@Column(name = "SEARCHSTATE")
	public String getSEARCHSTATE() {
		return super.getSEARCHSTATE();
	}

	@Override
	@Column(name = "JSYDPZZSH")
	public String getJSYDPZZSH() {
		return super.getJSYDPZZSH();
	}
	/******************* 自定义部分 ****************/
	@Transient
	public BDCDYLX getBDCDYLX() {
		return BDCDYLX.SHYQZD;
	}

	@Transient
	public DJDYLY getLY() {
		return DJDYLY.XZ;
	}

	@Transient
	public double getMJ() {
		double d = 0;
		if (this.getZDMJ() != null)
			d = this.getZDMJ();
		return d;
	}

	/**
	 * 土地用途接口
	 */

	private List<TDYT> tdyts;

	@Transient
	public List<TDYT> getTDYTS() {
		return tdyts;
	}

	public void setTDYTS(List<TDYT> tDYTS) {
		tdyts = tDYTS;
	}

	/**
	 * 宗地不动产权证号接口
	 */

	private String zdbdcqzh;

	@Transient
	public String getZDBDCQZH() {
		return zdbdcqzh;
	}

	public void setZDBDCQZH(String v_zdbdcqzh) {
		zdbdcqzh = v_zdbdcqzh;
	}
	
	@Override
	@Column(name = "txwhtype")
	public String getTXWHTYPE() {
		return super.getTXWHTYPE();
	}
}
