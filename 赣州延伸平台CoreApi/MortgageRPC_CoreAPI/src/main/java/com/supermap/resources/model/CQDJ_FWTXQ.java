package com.supermap.resources.model;

import java.io.Serializable;
import java.util.Date;

public class CQDJ_FWTXQ implements Serializable{

	private String QLR	;//VARCHAR2(400)	Y			
	private String QLZL;//	VARCHAR2(30)	Y			
	private String QLFW;//	VARCHAR2(200)	Y			
	private double JZMJ;//	NUMBER(14,4)	Y			
	private double QLJZ;//	NUMBER(12,2)	Y			
	private double QS;//	NUMBER(12,2)	Y			
	private int ALLTXQFLAG;//	NUMBER	Y	1		
	private Date SDQX1;//	DATE	Y			
	private Date SDQX2;//	DATE	Y			
	private String DJZH1;//	VARCHAR2(20)	Y			
	private String DJZH2;//	VARCHAR2(20)	Y			
	private Date HZ_DATE;//	DATE	Y			
	private Date TF_DATE;//	DATE	Y			
	private String TXQZH;//	VARCHAR2(50)	Y			
	private String JDR;//	VARCHAR2(10)	Y			
	private Date JD_DATE;//	DATE	Y			
	private int ZX_FLAG_FWTXQ;//	NUMBER	Y	1		
	private String FJ;//	VARCHAR2(2000)	Y			
	private String BZ;//	VARCHAR2(200)	Y			
	private String ZXSM;//	VARCHAR2(200)	Y			
	private String ZX_JBR;//	VARCHAR2(10)	Y			
	private Date ZX_DATE;//	DATE	Y			
	private String CXQX;//	VARCHAR2(20)	Y			
	private String JBR;//	VARCHAR2(30)	Y			
	private Date JB_DATE;//	DATE	Y			
	private String JBRYJ;//	VARCHAR2(200)	Y			
	private int CQDJ_FWCQSN_TXQ;//	NUMBER	Y			
	private int CQDJ_FWTXQSN;//	NUMBER	Y			
	private String SFZHM;//	VARCHAR2(200)	Y			
	private String DBR;//	VARCHAR2(100)	Y			
	private int BWASN_CQDJ_FWTXQ;//	NUMBER	Y	0		
	private Date DJ_DATE;//	DATE;//	Y			
	private String FWLX;//	VARCHAR2(30)	Y			
	private String YWR	;//	VARCHAR2(100)	Y			
	private String SFZHM_DBR;//	VARCHAR2(200)	Y			
	private String SFZHM_YWR;//	VARCHAR2(200)	Y			
	private String SZR;//	VARCHAR2(30)	Y			
	private String SUBDEPT_FWTXQ;//	VARCHAR2(30)	Y			
	private double DKJE;//	NUMBER(16,4)	Y			
	private int DASN_CQDJ_FWTXQ;//	NUMBER	Y	0		
	private double ZYDMJ;//	NUMBER(16,4)	Y			
	private double GYDMJ;//	NUMBER(16,4)	Y			
	private String QSRGYR;//	VARCHAR2(2000)	Y			
	private String FCZH;//	VARCHAR2(2000)	Y			
	private String FWZL_TXQ;//	VARCHAR2(2000)	Y			
	private int CQDJ_OLD_FWTXQSN;//	NUMBER	Y			
	private String ID;//	VARCHAR2(32)	Y			
	private String CLASS;//	VARCHAR2(32)	Y	'TCQDJ_FWTXQDAO'		
	private int UPDATECOUNT;//	NUMBER(18)	Y	1		
	private String QUOTIENT1;//	VARCHAR2(50)	Y			
	private String CORPORATION1;//	VARCHAR2(50)	Y			
	private String AGENT1;//	VARCHAR2(50)	Y			
	private String PHONE1;//	VARCHAR2(50)	Y			
	private String MAIL1;//	VARCHAR2(50)	Y			
	private String IDCARD1;//	VARCHAR2(20)	Y			
	private String ADDRESS1;//	VARCHAR2(100)	Y			
	private String QUOTIENT2;//	VARCHAR2(50)	Y			
	private String CORPORATION2;//	VARCHAR2(50)	Y			
	private String AGENT2;//	VARCHAR2(50)	Y			
	private String PHONE2;//	VARCHAR2(50)	Y			
	private String MAIL2;//	VARCHAR2(50)	Y			
	private String ADDRESS2;//	VARCHAR2(100)	Y			
	private String IDCARD2;//	VARCHAR2(20)	Y			
	private Date LOANTERM1;//	DATE	Y			
	private Date LOANTERM2;//	DATE	Y			
	private Date MORTGAGETERM1;//	DATE	Y			
	private Date MORTGAGETERM2;//	DATE	Y			
	private String LOANTERMCONTRACTNO;//	VARCHAR2(100)	Y			
	private String MORTGAGETERMCONTRACTNO;//	VARCHAR2(50)	Y			
	private String PGJZ;//	VARCHAR2(50)	Y			
	private int ISHIGHEST;//	NUMBER	Y			
	private Date PROMISEDATEBEGIN;//	DATE	Y			
	private Date PROMISEDATEEND;//	DATE	Y			
	private String HIGHESTLOAN;//	VARCHAR2(50)	Y			
	private int SZ_FLAG_TXQ;//	NUMBER	Y			
	private int YG_FLAG;//	NUMBER(4)	Y			
	private int SP_FLAG;//	NUMBER(1)	Y			
	private String DJZH_CQ;//	VARCHAR2(2000)	Y			
	private String DYSW;//	VARCHAR2(50)	Y			
	private String TXQZSHM;//	VARCHAR2(50)	Y			
	private String ZGEQDSS;//	VARCHAR2(100)	Y			
	private int ZGEZQSE;//	NUMBER	Y			
	private String BH;//	VARCHAR2(50)	Y			
	private String HOUSENO	;//VARCHAR2(32)	Y			
	private String DAH;//	VARCHAR2(30)	Y			
	private String AJCFC;//	VARCHAR2(50)	Y			
	private String JKR;//	VARCHAR2(100)	Y			
	private String JKRSFZHM;//	VARCHAR2(50)	Y	
	public String getQLR() {
		return QLR;
	}
	public void setQLR(String qLR) {
		QLR = qLR;
	}
	public String getQLZL() {
		return QLZL;
	}
	public void setQLZL(String qLZL) {
		QLZL = qLZL;
	}
	public String getQLFW() {
		return QLFW;
	}
	public void setQLFW(String qLFW) {
		QLFW = qLFW;
	}
	public double getJZMJ() {
		return JZMJ;
	}
	public void setJZMJ(double jZMJ) {
		JZMJ = jZMJ;
	}
	public double getQLJZ() {
		return QLJZ;
	}
	public void setQLJZ(double qLJZ) {
		QLJZ = qLJZ;
	}
	public double getQS() {
		return QS;
	}
	public void setQS(double qS) {
		QS = qS;
	}
	public int getALLTXQFLAG() {
		return ALLTXQFLAG;
	}
	public void setALLTXQFLAG(int aLLTXQFLAG) {
		ALLTXQFLAG = aLLTXQFLAG;
	}
	public Date getSDQX1() {
		return SDQX1;
	}
	public void setSDQX1(Date sDQX1) {
		SDQX1 = sDQX1;
	}
	public Date getSDQX2() {
		return SDQX2;
	}
	public void setSDQX2(Date sDQX2) {
		SDQX2 = sDQX2;
	}
	public String getDJZH1() {
		return DJZH1;
	}
	public void setDJZH1(String dJZH1) {
		DJZH1 = dJZH1;
	}
	public String getDJZH2() {
		return DJZH2;
	}
	public void setDJZH2(String dJZH2) {
		DJZH2 = dJZH2;
	}
	public Date getHZ_DATE() {
		return HZ_DATE;
	}
	public void setHZ_DATE(Date hZ_DATE) {
		HZ_DATE = hZ_DATE;
	}
	public Date getTF_DATE() {
		return TF_DATE;
	}
	public void setTF_DATE(Date tF_DATE) {
		TF_DATE = tF_DATE;
	}
	public String getTXQZH() {
		return TXQZH;
	}
	public void setTXQZH(String tXQZH) {
		TXQZH = tXQZH;
	}
	public String getJDR() {
		return JDR;
	}
	public void setJDR(String jDR) {
		JDR = jDR;
	}
	public Date getJD_DATE() {
		return JD_DATE;
	}
	public void setJD_DATE(Date jD_DATE) {
		JD_DATE = jD_DATE;
	}
	public int getZX_FLAG_FWTXQ() {
		return ZX_FLAG_FWTXQ;
	}
	public void setZX_FLAG_FWTXQ(int zX_FLAG_FWTXQ) {
		ZX_FLAG_FWTXQ = zX_FLAG_FWTXQ;
	}
	public String getFJ() {
		return FJ;
	}
	public void setFJ(String fJ) {
		FJ = fJ;
	}
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
	public String getZXSM() {
		return ZXSM;
	}
	public void setZXSM(String zXSM) {
		ZXSM = zXSM;
	}
	public String getZX_JBR() {
		return ZX_JBR;
	}
	public void setZX_JBR(String zX_JBR) {
		ZX_JBR = zX_JBR;
	}
	public Date getZX_DATE() {
		return ZX_DATE;
	}
	public void setZX_DATE(Date zX_DATE) {
		ZX_DATE = zX_DATE;
	}
	public String getCXQX() {
		return CXQX;
	}
	public void setCXQX(String cXQX) {
		CXQX = cXQX;
	}
	public String getJBR() {
		return JBR;
	}
	public void setJBR(String jBR) {
		JBR = jBR;
	}
	public Date getJB_DATE() {
		return JB_DATE;
	}
	public void setJB_DATE(Date jB_DATE) {
		JB_DATE = jB_DATE;
	}
	public String getJBRYJ() {
		return JBRYJ;
	}
	public void setJBRYJ(String jBRYJ) {
		JBRYJ = jBRYJ;
	}
	public int getCQDJ_FWCQSN_TXQ() {
		return CQDJ_FWCQSN_TXQ;
	}
	public void setCQDJ_FWCQSN_TXQ(int cQDJ_FWCQSN_TXQ) {
		CQDJ_FWCQSN_TXQ = cQDJ_FWCQSN_TXQ;
	}
	public int getCQDJ_FWTXQSN() {
		return CQDJ_FWTXQSN;
	}
	public void setCQDJ_FWTXQSN(int cQDJ_FWTXQSN) {
		CQDJ_FWTXQSN = cQDJ_FWTXQSN;
	}
	public String getSFZHM() {
		return SFZHM;
	}
	public void setSFZHM(String sFZHM) {
		SFZHM = sFZHM;
	}
	public String getDBR() {
		return DBR;
	}
	public void setDBR(String dBR) {
		DBR = dBR;
	}
	public int getBWASN_CQDJ_FWTXQ() {
		return BWASN_CQDJ_FWTXQ;
	}
	public void setBWASN_CQDJ_FWTXQ(int bWASN_CQDJ_FWTXQ) {
		BWASN_CQDJ_FWTXQ = bWASN_CQDJ_FWTXQ;
	}
	public Date getDJ_DATE() {
		return DJ_DATE;
	}
	public void setDJ_DATE(Date dJ_DATE) {
		DJ_DATE = dJ_DATE;
	}
	public String getFWLX() {
		return FWLX;
	}
	public void setFWLX(String fWLX) {
		FWLX = fWLX;
	}
	public String getYWR() {
		return YWR;
	}
	public void setYWR(String yWR) {
		YWR = yWR;
	}
	public String getSFZHM_DBR() {
		return SFZHM_DBR;
	}
	public void setSFZHM_DBR(String sFZHM_DBR) {
		SFZHM_DBR = sFZHM_DBR;
	}
	public String getSFZHM_YWR() {
		return SFZHM_YWR;
	}
	public void setSFZHM_YWR(String sFZHM_YWR) {
		SFZHM_YWR = sFZHM_YWR;
	}
	public String getSZR() {
		return SZR;
	}
	public void setSZR(String sZR) {
		SZR = sZR;
	}
	public String getSUBDEPT_FWTXQ() {
		return SUBDEPT_FWTXQ;
	}
	public void setSUBDEPT_FWTXQ(String sUBDEPT_FWTXQ) {
		SUBDEPT_FWTXQ = sUBDEPT_FWTXQ;
	}
	public double getDKJE() {
		return DKJE;
	}
	public void setDKJE(double dKJE) {
		DKJE = dKJE;
	}
	public int getDASN_CQDJ_FWTXQ() {
		return DASN_CQDJ_FWTXQ;
	}
	public void setDASN_CQDJ_FWTXQ(int dASN_CQDJ_FWTXQ) {
		DASN_CQDJ_FWTXQ = dASN_CQDJ_FWTXQ;
	}
	public double getZYDMJ() {
		return ZYDMJ;
	}
	public void setZYDMJ(double zYDMJ) {
		ZYDMJ = zYDMJ;
	}
	public double getGYDMJ() {
		return GYDMJ;
	}
	public void setGYDMJ(double gYDMJ) {
		GYDMJ = gYDMJ;
	}
	public String getQSRGYR() {
		return QSRGYR;
	}
	public void setQSRGYR(String qSRGYR) {
		QSRGYR = qSRGYR;
	}
	public String getFCZH() {
		return FCZH;
	}
	public void setFCZH(String fCZH) {
		FCZH = fCZH;
	}
	public String getFWZL_TXQ() {
		return FWZL_TXQ;
	}
	public void setFWZL_TXQ(String fWZL_TXQ) {
		FWZL_TXQ = fWZL_TXQ;
	}
	public int getCQDJ_OLD_FWTXQSN() {
		return CQDJ_OLD_FWTXQSN;
	}
	public void setCQDJ_OLD_FWTXQSN(int cQDJ_OLD_FWTXQSN) {
		CQDJ_OLD_FWTXQSN = cQDJ_OLD_FWTXQSN;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCLASS() {
		return CLASS;
	}
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}
	public int getUPDATECOUNT() {
		return UPDATECOUNT;
	}
	public void setUPDATECOUNT(int uPDATECOUNT) {
		UPDATECOUNT = uPDATECOUNT;
	}
	public String getQUOTIENT1() {
		return QUOTIENT1;
	}
	public void setQUOTIENT1(String qUOTIENT1) {
		QUOTIENT1 = qUOTIENT1;
	}
	public String getCORPORATION1() {
		return CORPORATION1;
	}
	public void setCORPORATION1(String cORPORATION1) {
		CORPORATION1 = cORPORATION1;
	}
	public String getAGENT1() {
		return AGENT1;
	}
	public void setAGENT1(String aGENT1) {
		AGENT1 = aGENT1;
	}
	public String getPHONE1() {
		return PHONE1;
	}
	public void setPHONE1(String pHONE1) {
		PHONE1 = pHONE1;
	}
	public String getMAIL1() {
		return MAIL1;
	}
	public void setMAIL1(String mAIL1) {
		MAIL1 = mAIL1;
	}
	public String getIDCARD1() {
		return IDCARD1;
	}
	public void setIDCARD1(String iDCARD1) {
		IDCARD1 = iDCARD1;
	}
	public String getADDRESS1() {
		return ADDRESS1;
	}
	public void setADDRESS1(String aDDRESS1) {
		ADDRESS1 = aDDRESS1;
	}
	public String getQUOTIENT2() {
		return QUOTIENT2;
	}
	public void setQUOTIENT2(String qUOTIENT2) {
		QUOTIENT2 = qUOTIENT2;
	}
	public String getCORPORATION2() {
		return CORPORATION2;
	}
	public void setCORPORATION2(String cORPORATION2) {
		CORPORATION2 = cORPORATION2;
	}
	public String getAGENT2() {
		return AGENT2;
	}
	public void setAGENT2(String aGENT2) {
		AGENT2 = aGENT2;
	}
	public String getPHONE2() {
		return PHONE2;
	}
	public void setPHONE2(String pHONE2) {
		PHONE2 = pHONE2;
	}
	public String getMAIL2() {
		return MAIL2;
	}
	public void setMAIL2(String mAIL2) {
		MAIL2 = mAIL2;
	}
	public String getADDRESS2() {
		return ADDRESS2;
	}
	public void setADDRESS2(String aDDRESS2) {
		ADDRESS2 = aDDRESS2;
	}
	public String getIDCARD2() {
		return IDCARD2;
	}
	public void setIDCARD2(String iDCARD2) {
		IDCARD2 = iDCARD2;
	}
	public Date getLOANTERM1() {
		return LOANTERM1;
	}
	public void setLOANTERM1(Date lOANTERM1) {
		LOANTERM1 = lOANTERM1;
	}
	public Date getLOANTERM2() {
		return LOANTERM2;
	}
	public void setLOANTERM2(Date lOANTERM2) {
		LOANTERM2 = lOANTERM2;
	}
	public Date getMORTGAGETERM1() {
		return MORTGAGETERM1;
	}
	public void setMORTGAGETERM1(Date mORTGAGETERM1) {
		MORTGAGETERM1 = mORTGAGETERM1;
	}
	public Date getMORTGAGETERM2() {
		return MORTGAGETERM2;
	}
	public void setMORTGAGETERM2(Date mORTGAGETERM2) {
		MORTGAGETERM2 = mORTGAGETERM2;
	}
	public String getLOANTERMCONTRACTNO() {
		return LOANTERMCONTRACTNO;
	}
	public void setLOANTERMCONTRACTNO(String lOANTERMCONTRACTNO) {
		LOANTERMCONTRACTNO = lOANTERMCONTRACTNO;
	}
	public String getMORTGAGETERMCONTRACTNO() {
		return MORTGAGETERMCONTRACTNO;
	}
	public void setMORTGAGETERMCONTRACTNO(String mORTGAGETERMCONTRACTNO) {
		MORTGAGETERMCONTRACTNO = mORTGAGETERMCONTRACTNO;
	}
	public String getPGJZ() {
		return PGJZ;
	}
	public void setPGJZ(String pGJZ) {
		PGJZ = pGJZ;
	}
	public int getISHIGHEST() {
		return ISHIGHEST;
	}
	public void setISHIGHEST(int iSHIGHEST) {
		ISHIGHEST = iSHIGHEST;
	}
	public Date getPROMISEDATEBEGIN() {
		return PROMISEDATEBEGIN;
	}
	public void setPROMISEDATEBEGIN(Date pROMISEDATEBEGIN) {
		PROMISEDATEBEGIN = pROMISEDATEBEGIN;
	}
	public Date getPROMISEDATEEND() {
		return PROMISEDATEEND;
	}
	public void setPROMISEDATEEND(Date pROMISEDATEEND) {
		PROMISEDATEEND = pROMISEDATEEND;
	}
	public String getHIGHESTLOAN() {
		return HIGHESTLOAN;
	}
	public void setHIGHESTLOAN(String hIGHESTLOAN) {
		HIGHESTLOAN = hIGHESTLOAN;
	}
	public int getSZ_FLAG_TXQ() {
		return SZ_FLAG_TXQ;
	}
	public void setSZ_FLAG_TXQ(int sZ_FLAG_TXQ) {
		SZ_FLAG_TXQ = sZ_FLAG_TXQ;
	}
	public int getYG_FLAG() {
		return YG_FLAG;
	}
	public void setYG_FLAG(int yG_FLAG) {
		YG_FLAG = yG_FLAG;
	}
	public int getSP_FLAG() {
		return SP_FLAG;
	}
	public void setSP_FLAG(int sP_FLAG) {
		SP_FLAG = sP_FLAG;
	}
	public String getDJZH_CQ() {
		return DJZH_CQ;
	}
	public void setDJZH_CQ(String dJZH_CQ) {
		DJZH_CQ = dJZH_CQ;
	}
	public String getDYSW() {
		return DYSW;
	}
	public void setDYSW(String dYSW) {
		DYSW = dYSW;
	}
	public String getTXQZSHM() {
		return TXQZSHM;
	}
	public void setTXQZSHM(String tXQZSHM) {
		TXQZSHM = tXQZSHM;
	}
	public String getZGEQDSS() {
		return ZGEQDSS;
	}
	public void setZGEQDSS(String zGEQDSS) {
		ZGEQDSS = zGEQDSS;
	}
	public int getZGEZQSE() {
		return ZGEZQSE;
	}
	public void setZGEZQSE(int zGEZQSE) {
		ZGEZQSE = zGEZQSE;
	}
	public String getBH() {
		return BH;
	}
	public void setBH(String bH) {
		BH = bH;
	}
	public String getHOUSENO() {
		return HOUSENO;
	}
	public void setHOUSENO(String hOUSENO) {
		HOUSENO = hOUSENO;
	}
	public String getDAH() {
		return DAH;
	}
	public void setDAH(String dAH) {
		DAH = dAH;
	}
	public String getAJCFC() {
		return AJCFC;
	}
	public void setAJCFC(String aJCFC) {
		AJCFC = aJCFC;
	}
	public String getJKR() {
		return JKR;
	}
	public void setJKR(String jKR) {
		JKR = jKR;
	}
	public String getJKRSFZHM() {
		return JKRSFZHM;
	}
	public void setJKRSFZHM(String jKRSFZHM) {
		JKRSFZHM = jKRSFZHM;
	}
	
	
	
}
