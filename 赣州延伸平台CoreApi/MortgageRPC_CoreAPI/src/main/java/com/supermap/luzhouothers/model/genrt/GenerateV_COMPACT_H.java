package com.supermap.luzhouothers.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Internal Entity bdc_v_compact_h 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/


public class GenerateV_COMPACT_H {

	private String id;
	private boolean modify_id = false;

	public String getId() {
		if (!modify_id && id == null)
		{
			modify_id = true;
		}
		return id;
	}

	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String rOWNO;

	public String getROWNO() {
		return rOWNO;
	}

	public void setROWNO(String rOWNO) {
		if (this.rOWNO != rOWNO) {
			this.rOWNO = rOWNO;
		}
	}

	private Integer rELATIONID;
	public Integer getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(Integer rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
		}
	}

	private String zL;
	public String getZL() {
		return zL;
	}

	public void setZL(String zL) {
		if (this.zL != zL) {
			this.zL = zL;
		}
	}

	private String pART;
	public String getPART() {
		return pART;
	}

	public void setPART(String pART) {
		if (this.pART != pART) {
			this.pART = pART;
		}
	}

	private Double bUILDAREA;
	public Double getBUILDAREA() {
		return bUILDAREA;
	}

	public void setBUILDAREA(Double bUILDAREA) {
		if (this.bUILDAREA != bUILDAREA) {
			this.bUILDAREA = bUILDAREA;
		}
	}

	private Double uSEAREA;
	public Double getUSEAREA() {
		return uSEAREA;
	}

	public void setUSEAREA(Double uSEAREA) {
		if (this.uSEAREA != uSEAREA) {
			this.uSEAREA = uSEAREA;
		}
	}

	private Double pUBLICAREA;
	public Double getPUBLICAREA() {
		return pUBLICAREA;
	}

	public void setPUBLICAREA(Double pUBLICAREA) {
		if (this.pUBLICAREA != pUBLICAREA) {
			this.pUBLICAREA = pUBLICAREA;
		}
	}

	private Integer fLOORON;
	public Integer getFLOORON() {
		return fLOORON;
	}

	public void setFLOORON(Integer fLOORON) {
		if (this.fLOORON != fLOORON) {
			this.fLOORON = fLOORON;
		}
	}

	private Integer fLOORONEND;
	public Integer getFLOORONEND() {
		return fLOORONEND;
	}

	public void setFLOORONEND(Integer fLOORONEND) {
		if (this.fLOORONEND != fLOORONEND) {
			this.fLOORONEND = fLOORONEND;
		}
	}

	private String tYPE;
	public String getTYPE() {
		return tYPE;
	}

	public void setTYPE(String tYPE) {
		if (this.tYPE != tYPE) {
			this.tYPE = tYPE;
		}
	}

	private String sTRUCTURE;
	public String getSTRUCTURE() {
		return sTRUCTURE;
	}

	public void setSTRUCTURE(String sTRUCTURE) {
		if (this.sTRUCTURE != sTRUCTURE) {
			this.sTRUCTURE = sTRUCTURE;
		}
	}

	private String dESIGNUSAGE;
	public String getDESIGNUSAGE() {
		return dESIGNUSAGE;
	}

	public void setDESIGNUSAGE(String dESIGNUSAGE) {
		if (this.dESIGNUSAGE != dESIGNUSAGE) {
			this.dESIGNUSAGE = dESIGNUSAGE;
		}
	}

	private String uSAGE;
	public String getUSAGE() {
		return uSAGE;
	}

	public void setUSAGE(String uSAGE) {
		if (this.uSAGE != uSAGE) {
			this.uSAGE = uSAGE;
		}
	}

	private Integer bUILDID;
	public Integer getBUILDID() {
		return bUILDID;
	}

	public void setBUILDID(Integer bUILDID) {
		if (this.bUILDID != bUILDID) {
			this.bUILDID = bUILDID;
		}
	}

	private Integer iSVALID;
	public Integer getISVALID() {
		return iSVALID;
	}

	public void setISVALID(Integer iSVALID) {
		if (this.iSVALID != iSVALID) {
			this.iSVALID = iSVALID;
		}
	}

	public void resetModifyState() {
		modify_id = false;
	}
}
