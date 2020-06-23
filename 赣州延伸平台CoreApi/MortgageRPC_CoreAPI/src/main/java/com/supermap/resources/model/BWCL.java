package com.supermap.resources.model;

import java.io.Serializable;

public class BWCL implements Serializable {

	private String BWASN_BWCL;	//VARCHAR2(32)	Y			
	private int BWCLSN;	//NUMBER	N			
	private String CUR_BM;	//VARCHAR2(20)	Y			
	private int RECORDSN;	//NUMBER	Y			
	private String TO_BM;	//VARCHAR2(20)	Y			
	private String BWCLLXSN_BWCL;	//NUMBER	Y			
	private String SMZD;	//BLOB	Y			
	private int JS;	//NUMBER	Y			
	private int YS;	//NUMBER	Y			
	private String ZH;	///VARCHAR2(50)	Y			
	private String YC;	//VARCHAR2(30)	Y			
	private String BZ_BWA;	//VARCHAR2(200)	Y			
	private String BZ_DA;	//VARCHAR2(200)	Y			
	private String CUR_CZY;	//VARCHAR2(10)	Y			
	private String TO_CZY;	//VARCHAR2(10)	Y			
	private String BWCLMC;	//VARCHAR2(200)	Y			
	private String CREATE_BM;	//VARCHAR2(10)	Y	'SJ'		
	private String DA_FLAG;	//VARCHAR2(10)	Y			
	private String SMZD2;	//BLOB	Y			
	private String SMZD3;	//BLOB	Y			
	private String SMZD4;	//BLOB	Y			
	private String SMZD5;	//BLOB	Y			
	private int SXH	;//NUMBER	Y			
	private int ZX_FLAG_BWCL;	//NUMBER	Y	1		
	private String YJ_BM;	//V/ARCHAR2(10)	Y			
	private int DASN_BWCL;	//NUMBER	Y	0		
	private String PS_FLAG;	//VARCHAR2(1)	Y			
	private String BS_FLAG;	//VARCHAR2(2)	Y			
	private String CLASS;	//VARCHAR2(32)	N	'TBWCLDAO'		
	private String ID;	//VARCHAR2(32)	N			
	private int UPDATECOUNT;	//NUMBER(38)	Y	1		
	private String CERTIFICATENO;	//VARCHAR2(200)	Y			
	private String SCANSCHEDULE;	//VARCHAR2(20)	Y			
	private String SCANSTATE;	//VARCHAR2(20)	Y			
	private String CERTIFICATETYPE;	//VARCHAR2(20)	Y			
	private String REMARK;	//VARCHAR2(100)	Y			
	private String BWCLLX_FLAG;//VARCHAR2(50)	Y	
	
	public String getBWASN_BWCL() {
		return BWASN_BWCL;
	}
	public void setBWASN_BWCL(String bWASN_BWCL) {
		BWASN_BWCL = bWASN_BWCL;
	}
	public int getBWCLSN() {
		return BWCLSN;
	}
	public void setBWCLSN(int bWCLSN) {
		BWCLSN = bWCLSN;
	}
	public String getCUR_BM() {
		return CUR_BM;
	}
	public void setCUR_BM(String cUR_BM) {
		CUR_BM = cUR_BM;
	}
	public int getRECORDSN() {
		return RECORDSN;
	}
	public void setRECORDSN(int rECORDSN) {
		RECORDSN = rECORDSN;
	}
	public String getTO_BM() {
		return TO_BM;
	}
	public void setTO_BM(String tO_BM) {
		TO_BM = tO_BM;
	}
	public String getBWCLLXSN_BWCL() {
		return BWCLLXSN_BWCL;
	}
	public void setBWCLLXSN_BWCL(String bWCLLXSN_BWCL) {
		BWCLLXSN_BWCL = bWCLLXSN_BWCL;
	}
	public String getSMZD() {
		return SMZD;
	}
	public void setSMZD(String sMZD) {
		SMZD = sMZD;
	}
	public int getJS() {
		return JS;
	}
	public void setJS(int jS) {
		JS = jS;
	}
	public int getYS() {
		return YS;
	}
	public void setYS(int yS) {
		YS = yS;
	}
	public String getZH() {
		return ZH;
	}
	public void setZH(String zH) {
		ZH = zH;
	}
	public String getYC() {
		return YC;
	}
	public void setYC(String yC) {
		YC = yC;
	}
	public String getBZ_BWA() {
		return BZ_BWA;
	}
	public void setBZ_BWA(String bZ_BWA) {
		BZ_BWA = bZ_BWA;
	}
	public String getBZ_DA() {
		return BZ_DA;
	}
	public void setBZ_DA(String bZ_DA) {
		BZ_DA = bZ_DA;
	}
	public String getCUR_CZY() {
		return CUR_CZY;
	}
	public void setCUR_CZY(String cUR_CZY) {
		CUR_CZY = cUR_CZY;
	}
	public String getTO_CZY() {
		return TO_CZY;
	}
	public void setTO_CZY(String tO_CZY) {
		TO_CZY = tO_CZY;
	}
	public String getBWCLMC() {
		return BWCLMC;
	}
	public void setBWCLMC(String bWCLMC) {
		BWCLMC = bWCLMC;
	}
	public String getCREATE_BM() {
		return CREATE_BM;
	}
	public void setCREATE_BM(String cREATE_BM) {
		CREATE_BM = cREATE_BM;
	}
	public String getDA_FLAG() {
		return DA_FLAG;
	}
	public void setDA_FLAG(String dA_FLAG) {
		DA_FLAG = dA_FLAG;
	}
	public String getSMZD2() {
		return SMZD2;
	}
	public void setSMZD2(String sMZD2) {
		SMZD2 = sMZD2;
	}
	public String getSMZD3() {
		return SMZD3;
	}
	public void setSMZD3(String sMZD3) {
		SMZD3 = sMZD3;
	}
	public String getSMZD4() {
		return SMZD4;
	}
	public void setSMZD4(String sMZD4) {
		SMZD4 = sMZD4;
	}
	public String getSMZD5() {
		return SMZD5;
	}
	public void setSMZD5(String sMZD5) {
		SMZD5 = sMZD5;
	}
	public int getSXH() {
		return SXH;
	}
	public void setSXH(int sXH) {
		SXH = sXH;
	}
	public int getZX_FLAG_BWCL() {
		return ZX_FLAG_BWCL;
	}
	public void setZX_FLAG_BWCL(int zX_FLAG_BWCL) {
		ZX_FLAG_BWCL = zX_FLAG_BWCL;
	}
	public String getYJ_BM() {
		return YJ_BM;
	}
	public void setYJ_BM(String yJ_BM) {
		YJ_BM = yJ_BM;
	}
	public int getDASN_BWCL() {
		return DASN_BWCL;
	}
	public void setDASN_BWCL(int dASN_BWCL) {
		DASN_BWCL = dASN_BWCL;
	}
	public String getPS_FLAG() {
		return PS_FLAG;
	}
	public void setPS_FLAG(String pS_FLAG) {
		PS_FLAG = pS_FLAG;
	}
	public String getBS_FLAG() {
		return BS_FLAG;
	}
	public void setBS_FLAG(String bS_FLAG) {
		BS_FLAG = bS_FLAG;
	}
	public String getCLASS() {
		return CLASS;
	}
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public int getUPDATECOUNT() {
		return UPDATECOUNT;
	}
	public void setUPDATECOUNT(int uPDATECOUNT) {
		UPDATECOUNT = uPDATECOUNT;
	}
	public String getCERTIFICATENO() {
		return CERTIFICATENO;
	}
	public void setCERTIFICATENO(String cERTIFICATENO) {
		CERTIFICATENO = cERTIFICATENO;
	}
	public String getSCANSCHEDULE() {
		return SCANSCHEDULE;
	}
	public void setSCANSCHEDULE(String sCANSCHEDULE) {
		SCANSCHEDULE = sCANSCHEDULE;
	}
	public String getSCANSTATE() {
		return SCANSTATE;
	}
	public void setSCANSTATE(String sCANSTATE) {
		SCANSTATE = sCANSTATE;
	}
	public String getCERTIFICATETYPE() {
		return CERTIFICATETYPE;
	}
	public void setCERTIFICATETYPE(String cERTIFICATETYPE) {
		CERTIFICATETYPE = cERTIFICATETYPE;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	public String getBWCLLX_FLAG() {
		return BWCLLX_FLAG;
	}
	public void setBWCLLX_FLAG(String bWCLLX_FLAG) {
		BWCLLX_FLAG = bWCLLX_FLAG;
	}
	public String getENTITYTYPE() {
		return ENTITYTYPE;
	}
	public void setENTITYTYPE(String eNTITYTYPE) {
		ENTITYTYPE = eNTITYTYPE;
	}
	private String ENTITYTYPE;	//VARCHAR2(50)	Y	'TBWA'		
}
