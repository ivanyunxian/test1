package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Public Entity bdcs_qlr_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QLR_D;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_qlr_d", schema = "bdck")
public class BDCS_QLR_D extends GenerateBDCS_QLR_D implements RightsHolder{

	@Override
	@Id
	@Column(name = "qlrid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "sqrid")
	public String getSQRID() {
		return super.getSQRID();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "qlrmc")
	public String getQLRMC() {
		return super.getQLRMC();
	}

	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}

	@Override
	@Column(name = "zjzl")
	public String getZJZL() {
		return super.getZJZL();
	}

    private String zjzlname;
	@Transient
	public String getZJZLName() {
		if (zjzlname == null) {
			if (this.getZJZL() != null) {
				zjzlname = ConstHelper.getNameByValue("ZJLX", this.getZJZL());
			}
		}
		return zjzlname;
	}

	@Override
	@Column(name = "zjh")
	public String getZJH() {
		return super.getZJH();
	}

	@Override
	@Column(name = "fzjg")
	public String getFZJG() {
		return super.getFZJG();
	}

	@Override
	@Column(name = "sshy")
	public String getSSHY() {
		return super.getSSHY();
	}

    private String sshyname;
	@Transient
	public String getSSHYName() {
		if (sshyname == null) {
			if (this.getSSHY() != null) {
				sshyname = ConstHelper.getNameByValue("SSHY", this.getSSHY());
			}
		}
		return sshyname;
	}

	@Override
	@Column(name = "gj")
	public String getGJ() {
		return super.getGJ();
	}

    private String gjname;
	@Transient
	public String getGJName() {
		if (gjname == null) {
			if (this.getGJ() != null) {
				gjname = ConstHelper.getNameByValue("GJDQ", this.getGJ());
			}
		}
		return gjname;
	}

	@Override
	@Column(name = "hjszss")
	public String getHJSZSS() {
		return super.getHJSZSS();
	}

    private String hjszssname;
	@Transient
	public String getHJSZSSName() {
		if (hjszssname == null) {
			if (this.getHJSZSS() != null) {
				hjszssname = ConstHelper.getNameByValue("SS", this.getHJSZSS());
			}
		}
		return hjszssname;
	}

	@Override
	@Column(name = "xb")
	public String getXB() {
		return super.getXB();
	}

    private String xbname;
	@Transient
	public String getXBName() {
		if (xbname == null) {
			if (this.getXB() != null) {
				xbname = ConstHelper.getNameByValue("XB", this.getXB());
			}
		}
		return xbname;
	}

	@Override
	@Column(name = "dh")
	public String getDH() {
		return super.getDH();
	}

	@Override
	@Column(name = "dz")
	public String getDZ() {
		return super.getDZ();
	}

	@Override
	@Column(name = "yb")
	public String getYB() {
		return super.getYB();
	}

	@Override
	@Column(name = "gzdw")
	public String getGZDW() {
		return super.getGZDW();
	}

	@Override
	@Column(name = "dzyj")
	public String getDZYJ() {
		return super.getDZYJ();
	}

	@Override
	@Column(name = "qlrlx")
	public String getQLRLX() {
		return super.getQLRLX();
	}

    private String qlrlxname;
	@Transient
	public String getQLRLXName() {
		if (qlrlxname == null) {
			if (this.getQLRLX() != null) {
				qlrlxname = ConstHelper.getNameByValue("QLRLX", this.getQLRLX());
			}
		}
		return qlrlxname;
	}

	@Override
	@Column(name = "qlmj")
	public Double getQLMJ() {
		return super.getQLMJ();
	}

	@Override
	@Column(name = "qlbl")
	public String getQLBL() {
		return super.getQLBL();
	}

	@Override
	@Column(name = "gyfs")
	public String getGYFS() {
		return super.getGYFS();
	}

    private String gyfsname;
	@Transient
	public String getGYFSName() {
		if (gyfsname == null) {
			if (this.getGYFS() != null) {
				gyfsname = ConstHelper.getNameByValue("GYFS", this.getGYFS());
			}
		}
		return gyfsname;
	}

	@Override
	@Column(name = "gyqk")
	public String getGYQK() {
		return super.getGYQK();
	}

	@Override
	@Column(name = "fddbr")
	public String getFDDBR() {
		return super.getFDDBR();
	}

	@Override
	@Column(name = "fddbrzjlx")
	public String getFDDBRZJLX() {
		return super.getFDDBRZJLX();
	}

    private String fddbrzjlxname;
	@Transient
	public String getFDDBRZJLXName() {
		if (fddbrzjlxname == null) {
			if (this.getFDDBRZJLX() != null) {
				fddbrzjlxname = ConstHelper.getNameByValue("ZJLX", this.getFDDBRZJLX());
			}
		}
		return fddbrzjlxname;
	}

	@Override
	@Column(name = "fddbrzjhm")
	public String getFDDBRZJHM() {
		return super.getFDDBRZJHM();
	}

	@Override
	@Column(name = "fddbrdh")
	public String getFDDBRDH() {
		return super.getFDDBRDH();
	}

	@Override
	@Column(name = "dlrxm")
	public String getDLRXM() {
		return super.getDLRXM();
	}

	@Override
	@Column(name = "dljgmc")
	public String getDLJGMC() {
		return super.getDLJGMC();
	}

	@Override
	@Column(name = "dlrzjlx")
	public String getDLRZJLX() {
		return super.getDLRZJLX();
	}

    private String dlrzjlxname;
	@Transient
	public String getDLRZJLXName() {
		if (dlrzjlxname == null) {
			if (this.getDLRZJLX() != null) {
				dlrzjlxname = ConstHelper.getNameByValue("ZJLX", this.getDLRZJLX());
			}
		}
		return dlrzjlxname;
	}

	@Override
	@Column(name = "dlrzjhm")
	public String getDLRZJHM() {
		return super.getDLRZJHM();
	}

	@Override
	@Column(name = "dlrlxdh")
	public String getDLRLXDH() {
		return super.getDLRLXDH();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
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
	@Column(name = "isczr")
	public String getISCZR() {
		return super.getISCZR();
	}
}
