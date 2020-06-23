package com.supermap.intelligent.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "LOG_DECLARE_RECORD_LOG",schema = "LOG")
public class LOG_DECLARE_RECORD_LOG implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String BSM;
    private String SBLSH;
    private String YWLY;
    private String SFDB;
    private String SBZT;
    private Integer SBCS;
    private String REMARK;
    private String YWLSH;
    private String YWLCID;
    private Date CREATEDATE;
    private Date MODIFYTIME;
    private String ERRORLOG;
    private String ERRORCODE;
    private String TENANT_ID;

    @Id
    @Column(name = "BSM")
    public String getBSM() {
        if (BSM == null) {
            BSM = UUID.randomUUID().toString().replace("-", "");
        }
        return BSM;
    }

    public void setBSM(String BSM) {
        this.BSM = BSM;
    }

    @Column(name = "YWLY")
    public String getYWLY() {
        return YWLY;
    }

    public void setYWLY(String YWLY) {
        this.YWLY = YWLY;
    }

    @Column(name = "SBLSH")
    public String getSBLSH() {
        return SBLSH;
    }

    public void setSBLSH(String SBLSH) {
        this.SBLSH = SBLSH;
    }

    @Column(name = "SFDB")
    public String getSFDB() {
        return SFDB;
    }

    public void setSFDB(String SFDB) {
        this.SFDB = SFDB;
    }

    @Column(name = "SBZT")
    public String getSBZT() {
        return SBZT;
    }

    public void setSBZT(String SBZT) {
        this.SBZT = SBZT;
    }

    @Column(name = "SBCS")
    public Integer getSBCS() {
        return SBCS;
    }

    public void setSBCS(Integer SBCS) {
        this.SBCS = SBCS;
    }

    @Column(name = "REMARK")
    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    @Column(name = "YWLSH")
    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    @Column(name = "YWLCID")
    public String getYWLCID() {
        return YWLCID;
    }

    public void setYWLCID(String YWLCID) {
        this.YWLCID = YWLCID;
    }

    @Column(name = "CREATEDATE")
    public Date getCREATEDATE() {
        return CREATEDATE;
    }

    public void setCREATEDATE(Date CREATEDATE) {
        this.CREATEDATE = CREATEDATE;
    }

    @Column(name = "MODIFYTIME")
    public Date getMODIFYTIME() {
        return MODIFYTIME;
    }

    public void setMODIFYTIME(Date MODIFYTIME) {
        this.MODIFYTIME = MODIFYTIME;
    }

    @Column(name = "ERRORLOG")
    public String getERRORLOG() {
        return ERRORLOG;
    }

    public void setERRORLOG(String ERRORLOG) {
        this.ERRORLOG = ERRORLOG;
    }

    @Column(name = "ERRORCODE")
    public String getERRORCODE() {
        return ERRORCODE;
    }

    public void setERRORCODE(String ERRORCODE) {
        this.ERRORCODE = ERRORCODE;
    }

    @Column(name = "TENANT_ID")
    public String getTENANT_ID() {
        return TENANT_ID;
    }

    public void setTENANT_ID(String TENANT_ID) {
        this.TENANT_ID = TENANT_ID;
    }
}
