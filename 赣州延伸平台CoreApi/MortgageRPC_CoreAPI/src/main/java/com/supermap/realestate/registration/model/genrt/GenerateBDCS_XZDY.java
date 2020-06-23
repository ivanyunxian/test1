package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/14 
//* ----------------------------------------
//* Internal Entity bdcs_xzdy 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;

import com.supermap.wisdombusiness.core.SuperModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_XZDY implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}

	@Override
	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String qLID;
	private boolean modify_qLID = false;

	public String getQLID() {
		return qLID;
	}

	public void setQLID(String qLID) {
		if (this.qLID != qLID) {
			this.qLID = qLID;
			modify_qLID = true;
		}
	}

	private String wXQLID;
	private boolean modify_wXQLID = false;

	public String getWXQLID() {
		return wXQLID;
	}

	public void setWXQLID(String wXQLID) {
		if (this.wXQLID != wXQLID) {
			this.wXQLID = wXQLID;
			modify_wXQLID = true;
		}
	}

	private Integer qLNBBS;
	private boolean modify_qLNBBS = false;

	public Integer getQLNBBS() {
		return qLNBBS;
	}

	public void setQLNBBS(Integer qLNBBS) {
		if (this.qLNBBS != qLNBBS) {
			this.qLNBBS = qLNBBS;
			modify_qLNBBS = true;
		}
	}

	private String dYZFDDBR;
	private boolean modify_dYZFDDBR = false;

	public String getDYZFDDBR() {
		return dYZFDDBR;
	}

	public void setDYZFDDBR(String dYZFDDBR) {
		if (this.dYZFDDBR != dYZFDDBR) {
			this.dYZFDDBR = dYZFDDBR;
			modify_dYZFDDBR = true;
		}
	}

	private String dYZLXR;
	private boolean modify_dYZLXR = false;

	public String getDYZLXR() {
		return dYZLXR;
	}

	public void setDYZLXR(String dYZLXR) {
		if (this.dYZLXR != dYZLXR) {
			this.dYZLXR = dYZLXR;
			modify_dYZLXR = true;
		}
	}

	private String dYZDH;
	private boolean modify_dYZDH = false;

	public String getDYZDH() {
		return dYZDH;
	}

	public void setDYZDH(String dYZDH) {
		if (this.dYZDH != dYZDH) {
			this.dYZDH = dYZDH;
			modify_dYZDH = true;
		}
	}

	private String dYZTXDZ;
	private boolean modify_dYZTXDZ = false;

	public String getDYZTXDZ() {
		return dYZTXDZ;
	}

	public void setDYZTXDZ(String dYZTXDZ) {
		if (this.dYZTXDZ != dYZTXDZ) {
			this.dYZTXDZ = dYZTXDZ;
			modify_dYZTXDZ = true;
		}
	}

	private String dYQZFDDBR;
	private boolean modify_dYQZFDDBR = false;

	public String getDYQZFDDBR() {
		return dYQZFDDBR;
	}

	public void setDYQZFDDBR(String dYQZFDDBR) {
		if (this.dYQZFDDBR != dYQZFDDBR) {
			this.dYQZFDDBR = dYQZFDDBR;
			modify_dYQZFDDBR = true;
		}
	}

	private String dYQZLXR;
	private boolean modify_dYQZLXR = false;

	public String getDYQZLXR() {
		return dYQZLXR;
	}

	public void setDYQZLXR(String dYQZLXR) {
		if (this.dYQZLXR != dYQZLXR) {
			this.dYQZLXR = dYQZLXR;
			modify_dYQZLXR = true;
		}
	}

	private String dYQZDH;
	private boolean modify_dYQZDH = false;

	public String getDYQZDH() {
		return dYQZDH;
	}

	public void setDYQZDH(String dYQZDH) {
		if (this.dYQZDH != dYQZDH) {
			this.dYQZDH = dYQZDH;
			modify_dYQZDH = true;
		}
	}

	private String dYQZTXDZ;
	private boolean modify_dYQZTXDZ = false;

	public String getDYQZTXDZ() {
		return dYQZTXDZ;
	}

	public void setDYQZTXDZ(String dYQZTXDZ) {
		if (this.dYQZTXDZ != dYQZTXDZ) {
			this.dYQZTXDZ = dYQZTXDZ;
			modify_dYQZTXDZ = true;
		}
	}

	private Double dYZMJ;
	private boolean modify_dYZMJ = false;

	public Double getDYZMJ() {
		return dYZMJ;
	}

	public void setDYZMJ(Double dYZMJ) {
		if (this.dYZMJ != dYZMJ) {
			this.dYZMJ = dYZMJ;
			modify_dYZMJ = true;
		}
	}

	private Double dYJE;
	private boolean modify_dYJE = false;

	public Double getDYJE() {
		return dYJE;
	}

	public void setDYJE(Double dYJE) {
		if (this.dYJE != dYJE) {
			this.dYJE = dYJE;
			modify_dYJE = true;
		}
	}

	private Double kDYTDZC;
	private boolean modify_kDYTDZC = false;

	public Double getKDYTDZC() {
		return kDYTDZC;
	}

	public void setKDYTDZC(Double kDYTDZC) {
		if (this.kDYTDZC != kDYTDZC) {
			this.kDYTDZC = kDYTDZC;
			modify_kDYTDZC = true;
		}
	}

	private String dYQX;
	private boolean modify_dYQX = false;

	public String getDYQX() {
		return dYQX;
	}

	public void setDYQX(String dYQX) {
		if (this.dYQX != dYQX) {
			this.dYQX = dYQX;
			modify_dYQX = true;
		}
	}

	private String jBYJ;
	private boolean modify_jBYJ = false;

	public String getJBYJ() {
		return jBYJ;
	}

	public void setJBYJ(String jBYJ) {
		if (this.jBYJ != jBYJ) {
			this.jBYJ = jBYJ;
			modify_jBYJ = true;
		}
	}

	private String sHR;
	private boolean modify_sHR = false;

	public String getSHR() {
		return sHR;
	}

	public void setSHR(String sHR) {
		if (this.sHR != sHR) {
			this.sHR = sHR;
			modify_sHR = true;
		}
	}

	private Date sHRQ;
	private boolean modify_sHRQ = false;

	public Date getSHRQ() {
		return sHRQ;
	}

	public void setSHRQ(Date sHRQ) {
		if (this.sHRQ != sHRQ) {
			this.sHRQ = sHRQ;
			modify_sHRQ = true;
		}
	}

	private String xMBH;
	private boolean modify_xMBH = false;

	public String getXMBH() {
		return xMBH;
	}

	public void setXMBH(String xMBH) {
		if (this.xMBH != xMBH) {
			this.xMBH = xMBH;
			modify_xMBH = true;
		}
	}

	private Integer dYSX;
	private boolean modify_dYSX = false;

	public Integer getDYSX() {
		return dYSX;
	}

	public void setDYSX(Integer dYSX) {
		if (this.dYSX != dYSX) {
			this.dYSX = dYSX;
			modify_dYSX = true;
		}
	}

	private String jBR;
	private boolean modify_jBR = false;

	public String getJBR() {
		return jBR;
	}

	public void setJBR(String jBR) {
		if (this.jBR != jBR) {
			this.jBR = jBR;
			modify_jBR = true;
		}
	}

	private Date jBRQ;
	private boolean modify_jBRQ = false;

	public Date getJBRQ() {
		return jBRQ;
	}

	public void setJBRQ(Date jBRQ) {
		if (this.jBRQ != jBRQ) {
			this.jBRQ = jBRQ;
			modify_jBRQ = true;
		}
	}

	private String sHYJ;
	private boolean modify_sHYJ = false;

	public String getSHYJ() {
		return sHYJ;
	}

	public void setSHYJ(String sHYJ) {
		if (this.sHYJ != sHYJ) {
			this.sHYJ = sHYJ;
			modify_sHYJ = true;
		}
	}

	private Integer tDZNBBS;
	private boolean modify_tDZNBBS = false;

	public Integer getTDZNBBS() {
		return tDZNBBS;
	}

	public void setTDZNBBS(Integer tDZNBBS) {
		if (this.tDZNBBS != tDZNBBS) {
			this.tDZNBBS = tDZNBBS;
			modify_tDZNBBS = true;
		}
	}

	private String dYR;
	private boolean modify_dYR = false;

	public String getDYR() {
		return dYR;
	}

	public void setDYR(String dYR) {
		if (this.dYR != dYR) {
			this.dYR = dYR;
			modify_dYR = true;
		}
	}

	private String dYQR;
	private boolean modify_dYQR = false;

	public String getDYQR() {
		return dYQR;
	}

	public void setDYQR(String dYQR) {
		if (this.dYQR != dYQR) {
			this.dYQR = dYQR;
			modify_dYQR = true;
		}
	}

	private Date kSSJ;
	private boolean modify_kSSJ = false;

	public Date getKSSJ() {
		return kSSJ;
	}

	public void setKSSJ(Date kSSJ) {
		if (this.kSSJ != kSSJ) {
			this.kSSJ = kSSJ;
			modify_kSSJ = true;
		}
	}

	private Date jSSJ;
	private boolean modify_jSSJ = false;

	public Date getJSSJ() {
		return jSSJ;
	}

	public void setJSSJ(Date jSSJ) {
		if (this.jSSJ != jSSJ) {
			this.jSSJ = jSSJ;
			modify_jSSJ = true;
		}
	}

	private String tDZ;
	private boolean modify_tDZ = false;

	public String getTDZ() {
		return tDZ;
	}

	public void setTDZ(String tDZ) {
		if (this.tDZ != tDZ) {
			this.tDZ = tDZ;
			modify_tDZ = true;
		}
	}

	private String tXZH;
	private boolean modify_tXZH = false;

	public String getTXZH() {
		return tXZH;
	}

	public void setTXZH(String tXZH) {
		if (this.tXZH != tXZH) {
			this.tXZH = tXZH;
			modify_tXZH = true;
		}
	}

	private String bLRQ;
	private boolean modify_bLRQ = false;

	public String getBLRQ() {
		return bLRQ;
	}

	public void setBLRQ(String bLRQ) {
		if (this.bLRQ != bLRQ) {
			this.bLRQ = bLRQ;
			modify_bLRQ = true;
		}
	}

	private Integer iSZX;
	private boolean modify_iSZX = false;

	public Integer getISZX() {
		return iSZX;
	}

	public void setISZX(Integer iSZX) {
		if (this.iSZX != iSZX) {
			this.iSZX = iSZX;
			modify_iSZX = true;
		}
	}

	private Integer mANUALINSERT;
	private boolean modify_mANUALINSERT = false;

	public Integer getMANUALINSERT() {
		return mANUALINSERT;
	}

	public void setMANUALINSERT(Integer mANUALINSERT) {
		if (this.mANUALINSERT != mANUALINSERT) {
			this.mANUALINSERT = mANUALINSERT;
			modify_mANUALINSERT = true;
		}
	}

	private String aJBH;
	private boolean modify_aJBH = false;

	public String getAJBH() {
		return aJBH;
	}

	public void setAJBH(String aJBH) {
		if (this.aJBH != aJBH) {
			this.aJBH = aJBH;
			modify_aJBH = true;
		}
	}

	private String tDYT;
	private boolean modify_tDYT = false;

	public String getTDYT() {
		return tDYT;
	}

	public void setTDYT(String tDYT) {
		if (this.tDYT != tDYT) {
			this.tDYT = tDYT;
			modify_tDYT = true;
		}
	}

	private String bZ;
	private boolean modify_bZ = false;

	public String getBZ() {
		return bZ;
	}

	public void setBZ(String bZ) {
		if (this.bZ != bZ) {
			this.bZ = bZ;
			modify_bZ = true;
		}
	}

	private String dJH;
	private boolean modify_dJH = false;

	public String getDJH() {
		return dJH;
	}

	public void setDJH(String dJH) {
		if (this.dJH != dJH) {
			this.dJH = dJH;
			modify_dJH = true;
		}
	}

	private String jYAJBH;
	private boolean modify_jYAJBH = false;

	public String getJYAJBH() {
		return jYAJBH;
	}

	public void setJYAJBH(String jYAJBH) {
		if (this.jYAJBH != jYAJBH) {
			this.jYAJBH = jYAJBH;
			modify_jYAJBH = true;
		}
	}

	private String tH;
	private boolean modify_tH = false;

	public String getTH() {
		return tH;
	}

	public void setTH(String tH) {
		if (this.tH != tH) {
			this.tH = tH;
			modify_tH = true;
		}
	}

	private Double jZMJ;
	private boolean modify_jZMJ = false;

	public Double getJZMJ() {
		return jZMJ;
	}

	public void setJZMJ(Double jZMJ) {
		if (this.jZMJ != jZMJ) {
			this.jZMJ = jZMJ;
			modify_jZMJ = true;
		}
	}

	private String zZRQ;
	private boolean modify_zZRQ = false;

	public String getZZRQ() {
		return zZRQ;
	}

	public void setZZRQ(String zZRQ) {
		if (this.zZRQ != zZRQ) {
			this.zZRQ = zZRQ;
			modify_zZRQ = true;
		}
	}

	private String tDSYZ;
	private boolean modify_tDSYZ = false;

	public String getTDSYZ() {
		return tDSYZ;
	}

	public void setTDSYZ(String tDSYZ) {
		if (this.tDSYZ != tDSYZ) {
			this.tDSYZ = tDSYZ;
			modify_tDSYZ = true;
		}
	}

	private Date dYZXSJ;
	private boolean modify_dYZXSJ = false;

	public Date getDYZXSJ() {
		return dYZXSJ;
	}

	public void setDYZXSJ(Date dYZXSJ) {
		if (this.dYZXSJ != dYZXSJ) {
			this.dYZXSJ = dYZXSJ;
			modify_dYZXSJ = true;
		}
	}

	private Integer jYXMBH;
	private boolean modify_jYXMBH = false;

	public Integer getJYXMBH() {
		return jYXMBH;
	}

	public void setJYXMBH(Integer jYXMBH) {
		if (this.jYXMBH != jYXMBH) {
			this.jYXMBH = jYXMBH;
			modify_jYXMBH = true;
		}
	}

	private String sYQLX;
	private boolean modify_sYQLX = false;

	public String getSYQLX() {
		return sYQLX;
	}

	public void setSYQLX(String sYQLX) {
		if (this.sYQLX != sYQLX) {
			this.sYQLX = sYQLX;
			modify_sYQLX = true;
		}
	}

	private Double dYJZMJ;
	private boolean modify_dYJZMJ = false;

	public Double getDYJZMJ() {
		return dYJZMJ;
	}

	public void setDYJZMJ(Double dYJZMJ) {
		if (this.dYJZMJ != dYJZMJ) {
			this.dYJZMJ = dYJZMJ;
			modify_dYJZMJ = true;
		}
	}

	private Double pGDYJK;
	private boolean modify_pGDYJK = false;

	public Double getPGDYJK() {
		return pGDYJK;
	}

	public void setPGDYJK(Double pGDYJK) {
		if (this.pGDYJK != pGDYJK) {
			this.pGDYJK = pGDYJK;
			modify_pGDYJK = true;
		}
	}

	private String tDZZZRQ;
	private boolean modify_tDZZZRQ = false;

	public String getTDZZZRQ() {
		return tDZZZRQ;
	}

	public void setTDZZZRQ(String tDZZZRQ) {
		if (this.tDZZZRQ != tDZZZRQ) {
			this.tDZZZRQ = tDZZZRQ;
			modify_tDZZZRQ = true;
		}
	}

	private String hTZZRQ;
	private boolean modify_hTZZRQ = false;

	public String getHTZZRQ() {
		return hTZZRQ;
	}

	public void setHTZZRQ(String hTZZRQ) {
		if (this.hTZZRQ != hTZZRQ) {
			this.hTZZRQ = hTZZRQ;
			modify_hTZZRQ = true;
		}
	}

	private Double zDMJ;
	private boolean modify_zDMJ = false;

	public Double getZDMJ() {
		return zDMJ;
	}

	public void setZDMJ(Double zDMJ) {
		if (this.zDMJ != zDMJ) {
			this.zDMJ = zDMJ;
			modify_zDMJ = true;
		}
	}

	private String yQSJ;
	private boolean modify_yQSJ = false;

	public String getYQSJ() {
		return yQSJ;
	}

	public void setYQSJ(String yQSJ) {
		if (this.yQSJ != yQSJ) {
			this.yQSJ = yQSJ;
			modify_yQSJ = true;
		}
	}

	private String bZXG;
	private boolean modify_bZXG = false;

	public String getBZXG() {
		return bZXG;
	}

	public void setBZXG(String bZXG) {
		if (this.bZXG != bZXG) {
			this.bZXG = bZXG;
			modify_bZXG = true;
		}
	}

	private String bZJY;
	private boolean modify_bZJY = false;

	public String getBZJY() {
		return bZJY;
	}

	public void setBZJY(String bZJY) {
		if (this.bZJY != bZJY) {
			this.bZJY = bZJY;
			modify_bZJY = true;
		}
	}

	private String tDDYRXZ;
	private boolean modify_tDDYRXZ = false;

	public String getTDDYRXZ() {
		return tDDYRXZ;
	}

	public void setTDDYRXZ(String tDDYRXZ) {
		if (this.tDDYRXZ != tDDYRXZ) {
			this.tDDYRXZ = tDDYRXZ;
			modify_tDDYRXZ = true;
		}
	}

	private String zDID;
	private boolean modify_zDID = false;

	public String getZDID() {
		return zDID;
	}

	public void setZDID(String zDID) {
		if (this.zDID != zDID) {
			this.zDID = zDID;
			modify_zDID = true;
		}
	}

	private String zDDJH;
	private boolean modify_zDDJH = false;

	public String getZDDJH() {
		return zDDJH;
	}

	public void setZDDJH(String zDDJH) {
		if (this.zDDJH != zDDJH) {
			this.zDDJH = zDDJH;
			modify_zDDJH = true;
		}
	}

	private String zL;
	private boolean modify_zL = false;

	public String getZL() {
		return zL;
	}

	public void setZL(String zL) {
		if (this.zL != zL) {
			this.zL = zL;
			modify_zL = true;
		}
	}

	private String zDTYBM;
	private boolean modify_zDTYBM = false;

	public String getZDTYBM() {
		return zDTYBM;
	}

	public void setZDTYBM(String zDTYBM) {
		if (this.zDTYBM != zDTYBM) {
			this.zDTYBM = zDTYBM;
			modify_zDTYBM = true;
		}
	}

	private Double zDZDMJ;
	private boolean modify_zDZDMJ = false;

	public Double getZDZDMJ() {
		return zDZDMJ;
	}

	public void setZDZDMJ(Double zDZDMJ) {
		if (this.zDZDMJ != zDZDMJ) {
			this.zDZDMJ = zDZDMJ;
			modify_zDZDMJ = true;
		}
	}

	private String qLR;
	private boolean modify_qLR = false;

	public String getQLR() {
		return qLR;
	}

	public void setQLR(String qLR) {
		if (this.qLR != qLR) {
			this.qLR = qLR;
			modify_qLR = true;
		}
	}

	private String yTDZID;
	private boolean modify_yTDZID = false;

	public String getYTDZID() {
		return yTDZID;
	}

	public void setYTDZID(String yTDZID) {
		if (this.yTDZID != yTDZID) {
			this.yTDZID = yTDZID;
			modify_yTDZID = true;
		}
	}

	private String gXDJH;
	private boolean modify_gXDJH = false;

	public String getGXDJH() {
		return gXDJH;
	}

	public void setGXDJH(String gXDJH) {
		if (this.gXDJH != gXDJH) {
			this.gXDJH = gXDJH;
			modify_gXDJH = true;
		}
	}

	private String zDTDZ;
	private boolean modify_zDTDZ = false;

	public String getZDTDZ() {
		return zDTDZ;
	}

	public void setZDTDZ(String zDTDZ) {
		if (this.zDTDZ != zDTDZ) {
			this.zDTDZ = zDTDZ;
			modify_zDTDZ = true;
		}
	}

	private String tDZID;
	private boolean modify_tDZID = false;

	public String getTDZID() {
		return tDZID;
	}

	public void setTDZID(String tDZID) {
		if (this.tDZID != tDZID) {
			this.tDZID = tDZID;
			modify_tDZID = true;
		}
	}

	private String zDBDCDYID;
	private boolean modify_zDBDCDYID = false;

	public String getZDBDCDYID() {
		return zDBDCDYID;
	}

	public void setZDBDCDYID(String zDBDCDYID) {
		if (this.zDBDCDYID != zDBDCDYID) {
			this.zDBDCDYID = zDBDCDYID;
			modify_zDBDCDYID = true;
		}
	}

	private String yXBZ;
	private boolean modify_yXBZ = false;

	public String getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(String yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_qLID = false;
		modify_wXQLID = false;
		modify_qLNBBS = false;
		modify_dYZFDDBR = false;
		modify_dYZLXR = false;
		modify_dYZDH = false;
		modify_dYZTXDZ = false;
		modify_dYQZFDDBR = false;
		modify_dYQZLXR = false;
		modify_dYQZDH = false;
		modify_dYQZTXDZ = false;
		modify_dYZMJ = false;
		modify_dYJE = false;
		modify_kDYTDZC = false;
		modify_dYQX = false;
		modify_jBYJ = false;
		modify_sHR = false;
		modify_sHRQ = false;
		modify_xMBH = false;
		modify_dYSX = false;
		modify_jBR = false;
		modify_jBRQ = false;
		modify_sHYJ = false;
		modify_tDZNBBS = false;
		modify_dYR = false;
		modify_dYQR = false;
		modify_kSSJ = false;
		modify_jSSJ = false;
		modify_tDZ = false;
		modify_tXZH = false;
		modify_bLRQ = false;
		modify_iSZX = false;
		modify_mANUALINSERT = false;
		modify_aJBH = false;
		modify_tDYT = false;
		modify_bZ = false;
		modify_dJH = false;
		modify_jYAJBH = false;
		modify_tH = false;
		modify_jZMJ = false;
		modify_zZRQ = false;
		modify_tDSYZ = false;
		modify_dYZXSJ = false;
		modify_jYXMBH = false;
		modify_sYQLX = false;
		modify_dYJZMJ = false;
		modify_pGDYJK = false;
		modify_tDZZZRQ = false;
		modify_hTZZRQ = false;
		modify_zDMJ = false;
		modify_yQSJ = false;
		modify_bZXG = false;
		modify_bZJY = false;
		modify_tDDYRXZ = false;
		modify_zDID = false;
		modify_zDDJH = false;
		modify_zL = false;
		modify_zDTYBM = false;
		modify_zDZDMJ = false;
		modify_qLR = false;
		modify_yTDZID = false;
		modify_gXDJH = false;
		modify_zDTDZ = false;
		modify_tDZID = false;
		modify_zDBDCDYID = false;
		modify_yXBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_qLID)
			listStrings.add("qLID");
		if (!modify_wXQLID)
			listStrings.add("wXQLID");
		if (!modify_qLNBBS)
			listStrings.add("qLNBBS");
		if (!modify_dYZFDDBR)
			listStrings.add("dYZFDDBR");
		if (!modify_dYZLXR)
			listStrings.add("dYZLXR");
		if (!modify_dYZDH)
			listStrings.add("dYZDH");
		if (!modify_dYZTXDZ)
			listStrings.add("dYZTXDZ");
		if (!modify_dYQZFDDBR)
			listStrings.add("dYQZFDDBR");
		if (!modify_dYQZLXR)
			listStrings.add("dYQZLXR");
		if (!modify_dYQZDH)
			listStrings.add("dYQZDH");
		if (!modify_dYQZTXDZ)
			listStrings.add("dYQZTXDZ");
		if (!modify_dYZMJ)
			listStrings.add("dYZMJ");
		if (!modify_dYJE)
			listStrings.add("dYJE");
		if (!modify_kDYTDZC)
			listStrings.add("kDYTDZC");
		if (!modify_dYQX)
			listStrings.add("dYQX");
		if (!modify_jBYJ)
			listStrings.add("jBYJ");
		if (!modify_sHR)
			listStrings.add("sHR");
		if (!modify_sHRQ)
			listStrings.add("sHRQ");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_dYSX)
			listStrings.add("dYSX");
		if (!modify_jBR)
			listStrings.add("jBR");
		if (!modify_jBRQ)
			listStrings.add("jBRQ");
		if (!modify_sHYJ)
			listStrings.add("sHYJ");
		if (!modify_tDZNBBS)
			listStrings.add("tDZNBBS");
		if (!modify_dYR)
			listStrings.add("dYR");
		if (!modify_dYQR)
			listStrings.add("dYQR");
		if (!modify_kSSJ)
			listStrings.add("kSSJ");
		if (!modify_jSSJ)
			listStrings.add("jSSJ");
		if (!modify_tDZ)
			listStrings.add("tDZ");
		if (!modify_tXZH)
			listStrings.add("tXZH");
		if (!modify_bLRQ)
			listStrings.add("bLRQ");
		if (!modify_iSZX)
			listStrings.add("iSZX");
		if (!modify_mANUALINSERT)
			listStrings.add("mANUALINSERT");
		if (!modify_aJBH)
			listStrings.add("aJBH");
		if (!modify_tDYT)
			listStrings.add("tDYT");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_dJH)
			listStrings.add("dJH");
		if (!modify_jYAJBH)
			listStrings.add("jYAJBH");
		if (!modify_tH)
			listStrings.add("tH");
		if (!modify_jZMJ)
			listStrings.add("jZMJ");
		if (!modify_zZRQ)
			listStrings.add("zZRQ");
		if (!modify_tDSYZ)
			listStrings.add("tDSYZ");
		if (!modify_dYZXSJ)
			listStrings.add("dYZXSJ");
		if (!modify_jYXMBH)
			listStrings.add("jYXMBH");
		if (!modify_sYQLX)
			listStrings.add("sYQLX");
		if (!modify_dYJZMJ)
			listStrings.add("dYJZMJ");
		if (!modify_pGDYJK)
			listStrings.add("pGDYJK");
		if (!modify_tDZZZRQ)
			listStrings.add("tDZZZRQ");
		if (!modify_hTZZRQ)
			listStrings.add("hTZZRQ");
		if (!modify_zDMJ)
			listStrings.add("zDMJ");
		if (!modify_yQSJ)
			listStrings.add("yQSJ");
		if (!modify_bZXG)
			listStrings.add("bZXG");
		if (!modify_bZJY)
			listStrings.add("bZJY");
		if (!modify_tDDYRXZ)
			listStrings.add("tDDYRXZ");
		if (!modify_zDID)
			listStrings.add("zDID");
		if (!modify_zDDJH)
			listStrings.add("zDDJH");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_zDTYBM)
			listStrings.add("zDTYBM");
		if (!modify_zDZDMJ)
			listStrings.add("zDZDMJ");
		if (!modify_qLR)
			listStrings.add("qLR");
		if (!modify_yTDZID)
			listStrings.add("yTDZID");
		if (!modify_gXDJH)
			listStrings.add("gXDJH");
		if (!modify_zDTDZ)
			listStrings.add("zDTDZ");
		if (!modify_tDZID)
			listStrings.add("tDZID");
		if (!modify_zDBDCDYID)
			listStrings.add("zDBDCDYID");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
