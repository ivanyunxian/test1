package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016-3-5 
//* ----------------------------------------
//* Public Entity bdcs_sllm_gz 
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

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

@Entity
@Table(name = "bdcs_sllm_gz", schema = "bdck")
public class BDCS_SLLM_GZ extends GenerateBDCS_SLLM_GZ implements Forest{

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
	@Column(name = "zdbdcdyid")
	public String getZDBDCDYID() {
		return super.getZDBDCDYID();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "zysz")
	public String getZYSZ() {
		return super.getZYSZ();
	}

	@Override
	@Column(name = "zs")
	public Integer getZS() {
		return super.getZS();
	}

	@Override
	@Column(name = "lz")
	public String getLZ() {
		return super.getLZ();
	}

    private String lzname;
	@Transient
	public String getLZName() {
		if (lzname == null) {
			if (this.getLZ() != null) {
				lzname = ConstHelper.getNameByValue("LZ", this.getLZ());
			}
		}
		return lzname;
	}
	private String  qyname;
	@Transient
	public String getQYName()
	{
		if(qyname==null)
		{
			if(this.getQY() !=null)
			{
				qyname=ConstHelper.getNameByValue("QY", this.getQY());
			}
		}
		return qyname;
	}

	@Override
	@Column(name = "qy")
	public String getQY() {
		return super.getQY();
	}

	@Override
	@Column(name = "zlnd")
	public Integer getZLND() {
		return super.getZLND();
	}

	@Override
	@Column(name = "lb")
	public String getLB() {
		return super.getLB();
	}

	@Override
	@Column(name = "xb")
	public String getXB() {
		return super.getXB();
	}

	@Override
	@Column(name = "xdm")
	public String getXDM() {
		return super.getXDM();
	}

	@Override
	@Column(name = "syqmj")
	public Double getSYQMJ() {
		return super.getSYQMJ();
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
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
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
	@Column(name = "tfh")
	public String getTFH() {
		return super.getTFH();
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
	@Column(name = "tdyt")
	public String getTDYT() {
		return super.getTDYT();
	}

    private String tdytname;
	@Transient
	public String getTDYTName() {
		if (tdytname == null) {
			if (this.getTDYT() != null) {
				tdytname = ConstHelper.getNameByValue("TDYT", this.getTDYT());
			}
		}
		return tdytname;
	}


	/******************* 自定义部分 ****************/
	@Transient
	public BDCDYLX getBDCDYLX() {
		return BDCDYLX.LD;
	}

	@Transient
	public DJDYLY getLY() {
		return DJDYLY.GZ;
	}

	@Transient
	public double getMJ() {
		double d = 0;
		if (super.getSYQMJ() != null)
			d = super.getSYQMJ();
		return d;
	}

	@Override
	public void setYT(String yt) {
		// TODO Auto-generated method stub
		
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
}

