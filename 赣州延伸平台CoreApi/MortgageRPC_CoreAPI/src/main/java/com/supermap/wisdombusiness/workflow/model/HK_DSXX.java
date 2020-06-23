package com.supermap.wisdombusiness.workflow.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HK_DSXX", schema = "BDC_WORKFLOW")
public class HK_DSXX {
	private String ID;
	private String SLBH;
	private String WQHTBH;
	private String FPDM;
	private String FPHM;
	private String SFWS;
	private String BJZT;
	private Date WSSJ;
	private Double WSJE;
	@Id
	@Column(name = "ID",length = 50)
	public String getID() {
		if (ID == null)
			ID = UUID.randomUUID().toString().replace("-", "");
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	@Column(name = "SLBH",length = 50)
	public String getSLBH() {
		return SLBH;
	}
	public void setSLBH(String sLBH) {
		SLBH = sLBH;
	}
	@Column(name = "WQHTBH",length = 50)
	public String getWQHTBH() {
		return WQHTBH;
	}
	public void setWQHTBH(String wQHTBH) {
		WQHTBH = wQHTBH;
	}
	@Column(name = "FPDM",length = 50)
	public String getFPDM() {
		return FPDM;
	}
	public void setFPDM(String fPDM) {
		FPDM = fPDM;
	}
	@Column(name = "FPHM",length = 50)
	public String getFPHM() {
		return FPHM;
	}
	public void setFPHM(String fPHM) {
		FPHM = fPHM;
	}
	@Column(name = "SFWS",length = 50)
	public String getSFWS() {
		return SFWS;
	}
	public void setSFWS(String sFWS) {
		SFWS = sFWS;
	}
	@Column(name = "BJZT",length = 50)
	public String getBJZT() {
		return BJZT;
	}
	public void setBJZT(String bJZT) {
		BJZT = bJZT;
	}
	@Column(name = "WSSJ",length = 50)
	public Date getWSSJ() {
		return WSSJ;
	}
	public void setWSSJ(Date wSSJ) {
		WSSJ = wSSJ;
	}
	@Column(name = "WSJE",length = 50)
	public Double getWSJE() {
		return WSJE;
	}
	public void setWSJE(Double wSJE) {
		WSJE = wSJE;
	}

}
