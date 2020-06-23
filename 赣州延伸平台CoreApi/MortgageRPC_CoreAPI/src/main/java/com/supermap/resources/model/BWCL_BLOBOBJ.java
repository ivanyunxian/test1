package com.supermap.resources.model;

import java.io.Serializable;

public class BWCL_BLOBOBJ implements Serializable{

	private String ID;	//VARCHAR2(32)	N			
	private String PARENTCLASS;	//VARCHAR2(32)	Y	'TBWCLDAO'		
	private String PARENTID;	//VARCHAR2(32)	Y			
	private String CHILDCLASS;	//VARCHAR2(32)	Y	'TBLOBOBJDAO'		
	private String CHILDID;	//VARCHAR2(32)	Y			
	private int SEQUENCENO;	//NUMBER(10)	Y	1		
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getPARENTCLASS() {
		return PARENTCLASS;
	}
	public void setPARENTCLASS(String pARENTCLASS) {
		PARENTCLASS = pARENTCLASS;
	}
	public String getPARENTID() {
		return PARENTID;
	}
	public void setPARENTID(String pARENTID) {
		PARENTID = pARENTID;
	}
	public String getCHILDCLASS() {
		return CHILDCLASS;
	}
	public void setCHILDCLASS(String cHILDCLASS) {
		CHILDCLASS = cHILDCLASS;
	}
	public String getCHILDID() {
		return CHILDID;
	}
	public void setCHILDID(String cHILDID) {
		CHILDID = cHILDID;
	}
	public int getSEQUENCENO() {
		return SEQUENCENO;
	}
	public void setSEQUENCENO(int sEQUENCENO) {
		SEQUENCENO = sEQUENCENO;
	}

}
