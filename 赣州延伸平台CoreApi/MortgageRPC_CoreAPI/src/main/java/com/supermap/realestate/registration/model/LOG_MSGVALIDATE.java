package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateLOG_MSGVALIDATE;

@Entity
@Table(name = "log_msgvalidate", schema = "BDCK")
public class LOG_MSGVALIDATE extends GenerateLOG_MSGVALIDATE{
	@Override
	@Id
	@Column(name = "id" )
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "sqrxm")
	public String getSQRXM() {
		return super.getSQRXM();
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
	@Column(name = "sqrlb")
	public String getSQRLB() {
		return super.getSQRLB();
	}
	
	@Override
	@Column(name = "sqrlx")
	public String getSQRLX() {
		return super.getSQRLX();
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
	@Column(name = "fddbrzjhm")
	public String getFDDBRZJHM() {
		return super.getFDDBRZJHM();
	}
	
	@Override
	@Column(name = "dlrxm")
	public String getDLRXM() {
		return super.getDLRXM();
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
	@Column(name = "vlidatetime")
	public Date getVLIDATETIME() {
		return super.getVLIDATETIME();
	}
	
	@Override
	@Column(name = "vlidateuser")
	public String getVLIDATEUSER() {
		return super.getVLIDATEUSER();
	}
	
	@Override
	@Column(name = "ywlsh")
	public String getYWLSH() {
		return super.getYWLSH();
	}
	
	@Override
	@Column(name = "sqrxm_jk")
	public String getSQRXM_JK() {
		return super.getSQRXM_JK();
	}

	@Override
	@Column(name = "zjlx_jk")
	public String getZJLX_JK() {
		return super.getZJLX_JK();
	}

	@Override
	@Column(name = "zjh_jk")
	public String getZJH_JK() {
		return super.getZJH_JK();
	}

	@Override
	@Column(name = "sqrlb_jk")
	public String getSQRLB_JK() {
		return super.getSQRLB_JK();
	}
	
	@Override
	@Column(name = "sqrlx_jk")
	public String getSQRLX_JK() {
		return super.getSQRLX_JK();
	}
	
	@Override
	@Column(name = "fddbr_jk")
	public String getFDDBR_JK() {
		return super.getFDDBR_JK();
	}

	@Override
	@Column(name = "fddbrzjlx_jk")
	public String getFDDBRZJLX_JK() {
		return super.getFDDBRZJLX_JK();
	}

	@Override
	@Column(name = "fddbrzjhm_jk")
	public String getFDDBRZJHM_JK() {
		return super.getFDDBRZJHM_JK();
	}
	
	@Override
	@Column(name = "dlrxm_jk")
	public String getDLRXM_JK() {
		return super.getDLRXM_JK();
	}
	
	@Override
	@Column(name = "dlrzjlx_jk")
	public String getDLRZJLX_JK() {
		return super.getDLRZJLX_JK();
	}
	@Override
	@Column(name = "dlrzjhm_jk")
	public String getDLRZJHM_JK() {
		return super.getDLRZJHM_JK();
	}
	
	@Override
	@Column(name = "validatestatus")
	public String getVALIDATESTATUS() {
		return super.getVALIDATESTATUS();
	}
	
	@Override
	@Column(name = "codeqlr")
	public String getCODEQLR() {
		return super.getCODEQLR();
	}
	
	@Override
	@Column(name = "jkyccodeqlr")
	public String getJKYCCODEQLR() {
		return super.getJKYCCODEQLR();
	}
	
	@Override
	@Column(name = "codedlr")
	public String getCODEDLR() {
		return super.getCODEDLR();
	}
	
	@Override
	@Column(name = "jkyccodedlr")
	public String getJKYCCODEDLR() {
		return super.getJKYCCODEDLR();
	}
}
