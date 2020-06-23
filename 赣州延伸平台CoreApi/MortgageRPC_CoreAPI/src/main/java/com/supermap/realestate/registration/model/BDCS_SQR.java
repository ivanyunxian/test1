package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/7/30 
//* ----------------------------------------
//* Public Entity bdcs_sqr 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SQR;

@Entity
@Table(name = "bdcs_sqr", schema = "bdck")
public class BDCS_SQR extends GenerateBDCS_SQR {

	@Override
	@Id
	@Column(name = "sqrid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "sqrxm")
	public String getSQRXM() {
		return super.getSQRXM();
	}

	@Override
	@Column(name = "xb")
	public String getXB() {
		return super.getXB();
	}

	@Override
	@Column(name = "zjlx")
	public String getZJLX() {
		return super.getZJLX();
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
	@Column(name = "gjdq")
	public String getGJDQ() {
		return super.getGJDQ();
	}

	@Override
	@Column(name = "hjszss")
	public String getHJSZSS() {
		return super.getHJSZSS();
	}

	@Override
	@Column(name = "gzdw")
	public String getGZDW() {
		return super.getGZDW();
	}

	@Override
	@Column(name = "sshy")
	public String getSSHY() {
		return super.getSSHY();
	}

	@Override
	@Column(name = "lxdh")
	public String getLXDH() {
		return super.getLXDH();
	}

	@Override
	@Column(name = "txdz")
	public String getTXDZ() {
		return super.getTXDZ();
	}

	@Override
	@Column(name = "yzbm")
	public String getYZBM() {
		return super.getYZBM();
	}

	@Override
	@Column(name = "dzyj")
	public String getDZYJ() {
		return super.getDZYJ();
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

	@Override
	@Column(name = "fddbrdh")
	public String getFDDBRDH() {
		return super.getFDDBRDH();
	}

	@Override
	@Column(name = "sqrlx")
	public String getSQRLX() {
		return super.getSQRLX();
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
	@Column(name = "dlrlxdh")
	public String getDLRLXDH() {
		return super.getDLRLXDH();
	}

	@Override
	@Column(name = "dlrzjlx")
	public String getDLRZJLX() {
		return super.getDLRZJLX();
	}

	@Override
	@Column(name = "dlrzjhm")
	public String getDLRZJHM() {
		return super.getDLRZJHM();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "sxh")
	public String getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "qlmj")
	public String getQLMJ() {
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
	@Column(name = "sqrlb")
	public String getSQRLB() {
		return super.getSQRLB();
	}

	@Override
	@Column(name = "sqrlbmc")
	public String getSQRLBMC() {
		return super.getSQRLBMC();
	}

	@Override
	@Column(name = "glqlid")
	public String getGLQLID() {
		return super.getGLQLID();
	}

	@Override
	@Column(name = "isczr")
	public String getISCZR() {
		return super.getISCZR();
	}

	@Override
	@Column(name = "fddbrzjhm")
	public String getFDDBRZJHM() {
		return super.getFDDBRZJHM();
	}
	
	@Override
	@Column(name = "zwr")
	public String getZWR() {
		return super.getZWR();
	}
	
	@Override
	@Column(name = "nation")
	public String getNATION() {
		return super.getNATION();
	}
	
	@Override
	@Column(name = "dlrnation")
	public String getDLRNATION() {
		return super.getDLRNATION();
	}
}
