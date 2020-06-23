package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

import javax.persistence.Transient;

import java.util.Date;
import java.util.List;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_NYD_GZ;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.TDYT;

@Entity
@Table(name = "bdcs_nyd_gz", schema = "bdcdck")
public class DCS_NYD_GZ extends GenerateBDCS_NYD_GZ implements AgriculturalLand {

	@Override
	@Id
	@Column(name = "bdcdyid", length = 50)
	public String getId() {
		return super.getId();
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
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
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
	@Column(name = "yt")
	public String getYT() {
		return super.getYT();
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
	@Column(name = "zdszd")
	public String getZDSZD() {
		return super.getZDSZD();
	}

	@Override
	@Column(name = "zdszn")
	public String getZDSZN() {
		return super.getZDSZN();
	}

	@Override
	@Column(name = "zdszx")
	public String getZDSZX() {
		return super.getZDSZX();
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
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "qxmc")
	public String getQXMC() {
		return super.getQXMC();
	}

	@Override
	@Column(name = "djqdm")
	public String getDJQDM() {
		return super.getDJQDM();
	}

	@Override
	@Column(name = "djqmc")
	public String getDJQMC() {
		return super.getDJQMC();
	}

	@Override
	@Column(name = "djzqdm")
	public String getDJZQDM() {
		return super.getDJZQDM();
	}

	@Override
	@Column(name = "djzqmc")
	public String getDJZQMC() {
		return super.getDJZQMC();
	}

	@Override
	@Column(name = "zh")
	public String getZH() {
		return super.getZH();
	}

	@Override
	@Column(name = "zm")
	public String getZM() {
		return super.getZM();
	}

	@Override
	@Column(name = "fbfdm")
	public String getFBFDM() {
		return super.getFBFDM();
	}

	@Override
	@Column(name = "fbfmc")
	public String getFBFMC() {
		return super.getFBFMC();
	}

	@Override
	@Column(name = "cbmj")
	public Double getCBMJ() {
		return super.getCBMJ();
	}

	@Override
	@Column(name = "cbqssj")
	public Date getCBQSSJ() {
		return super.getCBQSSJ();
	}

	@Override
	@Column(name = "cbjssj")
	public Date getCBJSSJ() {
		return super.getCBJSSJ();
	}

	@Override
	@Column(name = "tdsyqxz")
	public String getTDSYQXZ() {
		return super.getTDSYQXZ();
	}

	@Override
	@Column(name = "syttlx")
	public String getSYTTLX() {
		return super.getSYTTLX();
	}

	@Override
	@Column(name = "yzyfs")
	public String getYZYFS() {
		return super.getYZYFS();
	}

	@Override
	@Column(name = "cyzl")
	public String getCYZL() {
		return super.getCYZL();
	}

	@Override
	@Column(name = "syzcl")
	public Integer getSYZCL() {
		return super.getSYZCL();
	}

	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}

	// RealUnit 接口中的方法
	@Transient
	public void setDJZT(String djzt) {
		// TODO Auto-generated method stub

	}

	@Transient
	public String getDJZT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public BDCDYLX getBDCDYLX() {
		return BDCDYLX.NYD;
	}

	@Transient
	public DJDYLY getLY() {
		return DJDYLY.GZ;
	}

	@Transient
	public double getMJ() {
		double d = 0;
		if (this.getCBMJ() != null)
			d = this.getCBMJ();
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
	@Column(name = "isjg")
	public String getISJG() {
		return super.getISJG();
	}

}
