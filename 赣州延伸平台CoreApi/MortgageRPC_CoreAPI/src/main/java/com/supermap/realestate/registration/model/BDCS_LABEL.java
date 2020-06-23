package com.supermap.realestate.registration.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "BDCS_LABEL", schema = "BDCK")
public class BDCS_LABEL implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String BQBS;//标签标识
	private String BQMC;//标签名称
	private String BQNR;//标签内容
	private String LABELDESC;//标签描述
	private String LABELTYPE;//标签类型
	private String LABELTYPEID;//标签分类ID
	private String LABELINDEX;//标签索引

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Id
	@Column(name = "BQBS")
	public String getBQBS() {
		if (BQBS == null)
			BQBS = UUID.randomUUID().toString().replace("-", "");
		return BQBS;
	}
	public void setBQBS(String bQBS) {
		BQBS = bQBS;
	}
	
	@Column(name = "BQMC")
	public String getBQMC() {
		return BQMC;
	}
	public void setBQMC(String bQMC) {
		BQMC = bQMC;
	}
	
	@Column(name = "BQNR")
	public String getBQNR() {
		return BQNR;
	}
	public void setBQNR(String bQNR) {
		BQNR = bQNR;
	}
	
	@Column(name = "LABELDESC")
	public String getLABELDESC() {
		return LABELDESC;
	}

	public void setLABELDESC(String lABELDESC) {
		LABELDESC = lABELDESC;
	}

	@Column(name = "LABELTYPE")
	public String getLABELTYPE() {
		return LABELTYPE;
	}
	public void setLABELTYPE(String lABELTYPE) {
		LABELTYPE = lABELTYPE;
	}

	@Column(name = "LABELTYPEID")
	public String getLABELTYPEID() {
		return LABELTYPEID;
	}

	public void setLABELTYPEID(String lABELTYPEID) {
		LABELTYPEID = lABELTYPEID;
	}
	
	@Column(name = "LABELINDEX")
	public String getLABELINDEX() {
		return LABELINDEX;
	}

	public void setLABELINDEX(String lABELINDEX) {
		LABELINDEX = lABELINDEX;
	}

	
}
