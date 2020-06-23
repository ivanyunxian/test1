package com.supermap.wisdombusiness.framework.model.gxdjk;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity			
@Table(name = "CALLBACK_INFO", schema = "GXDJK")
public class CALLBACK_INFO implements java.io.Serializable{

	private String CASENUM;
	private String MESSAGE;
	private String BSM;
	private String ZT;
	public CALLBACK_INFO(){
		
	}
	public CALLBACK_INFO(String cASENUM, String mESSAGE, String bSM, String zT) {
		super(); 
		this.CASENUM = cASENUM;
		this.MESSAGE = mESSAGE;
		this.BSM = bSM;
		this.ZT = zT;
	}
	@Column(name = "CASENUM", length = 100)
	public String getCASENUM() {
		return CASENUM;
	}
	public void setCASENUM(String cASENUM) {
		CASENUM = cASENUM;
	}
	@Column(name = "MESSAGE", length = 200)
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	@Id
	@Column(name = "BSM", unique = true, nullable = false, length = 200)
	public String getBSM() {
		if (BSM == null) BSM = UUID.randomUUID().toString().replace("-", ""); 
		return BSM;
	}
	
	
	
	public void setBSM(String bSM) {
		BSM = bSM;
	}
	@Column(name = "ZT", length = 10)
	public String getZT() {
		return ZT;
	}
	public void setZT(String zT) {
		ZT = zT;
	}
	
}
