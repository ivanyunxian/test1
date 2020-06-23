package com.supermap.wisdombusiness.framework.model.gxdjk;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "FIAL", schema = "GXDJK")
public class FAIL implements java.io.Serializable{
	private String BDCDYH;
	private String QLLX;
	private String XMMC;
	private String ZL;
	private String DJDL;
	private String DJXL;
	private String BLJD;
	private String CASENUM;
	private String QLSDFS;
	private String PROJECT_ID;
	private Date TSSJ;
	private String BSM;
	private String FALLTYPE;
	private String SLRY;
	private Date SLSJ;

	public FAIL(String bDCDYH, String qLLX, String xMMC, String zL,
			String dJDL, String dJXL, String bLJD, String cASENUM,
			String qLSDFS, String pROJECT_ID, Date tSSJ, String bSM,
			String fALLTYPE, String sLRY, Date sLSJ) {
		super();
		this.BDCDYH = bDCDYH;
		this.QLLX = qLLX;
		this.XMMC = xMMC;
		this.ZL = zL;
		this.DJDL = dJDL;
		this.DJXL = dJXL;
		this.BLJD = bLJD;
		this.CASENUM = cASENUM;
		this.QLSDFS = qLSDFS;
		this.PROJECT_ID = pROJECT_ID;
		this.TSSJ = tSSJ;
		this.BSM = bSM;
		this.FALLTYPE = fALLTYPE;
		this.SLRY = sLRY;
		this.SLSJ = sLSJ;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "SLSJ", length = 7)
	public Date getSLSJ() {
		return SLSJ;
	}
	public void setSLSJ(Date sLSJ) {
		SLSJ = sLSJ;
	}
	@Column(name = "BDCDYH", length = 50)
	public String getBDCDYH() {
		return BDCDYH;
	}
	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}
	@Column(name = "QLLX", length = 50)
	public String getQLLX() {
		return QLLX;
	}
	public void setQLLX(String qLLX) {
		QLLX = qLLX;
	}
	@Column(name = "XMMC", length =200)
	public String getXMMC() {
		return XMMC;
	}
	public void setXMMC(String xMMC) {
		XMMC = xMMC;
	}
	@Column(name = "ZL", length = 200)
	public String getZL() {
		return ZL;
	}
	public void setZL(String zL) {
		ZL = zL;
	}
	@Column(name = "DJDL", length = 10)
	public String getDJDL() {
		return DJDL;
	}
	
	public void setDJDL(String dJDL) {
		DJDL = dJDL;
	}
	@Column(name = "DJXL", length = 10)
	public String getDJXL() {
		return DJXL;
	}
	public void setDJXL(String dJXL) {
		DJXL = dJXL;
	}
	
	@Column(name = "BLJD", length = 50)
	public String getBLJD() {
		return BLJD;
	}
	public void setBLJD(String bLJD) {
		BLJD = bLJD;
	}
	@Column(name = "CASENUM", length = 50)
	public String getCASENUM() {
		return CASENUM;
	}
	public void setCASENUM(String cASENUM) {
		CASENUM = cASENUM;
	}
	@Column(name = "QLSDFS", length = 10)
	public String getQLSDFS() {
		return QLSDFS;
	}
	public void setQLSDFS(String qLSDFS) {
		QLSDFS = qLSDFS;
	}
	@Column(name = "PROJECT_ID", length = 50)
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	@Temporal(TemporalType.DATE)
    @Column(name = "TSSJ", length = 7)
	public Date getTSSJ() {
		return TSSJ;
	}
	public void setTSSJ(Date tSSJ) {
		TSSJ = tSSJ;
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
	@Column(name = "FALLTYPE", length = 10)
	public String getFALLTYPE() {
		return FALLTYPE;
	}
	public void setFALLTYPE(String fALLTYPE) {
		FALLTYPE = fALLTYPE;
	}
	@Column(name = "SLRY", length = 50)
	public String getSLRY() {
		return SLRY;
	}
	public void setSLRY(String sLRY) {
		SLRY = sLRY;
	}
	
}
