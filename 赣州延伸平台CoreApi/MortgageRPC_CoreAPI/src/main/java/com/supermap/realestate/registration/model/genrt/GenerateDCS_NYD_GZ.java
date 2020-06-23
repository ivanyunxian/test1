package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateDCS_NYD_GZ implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null) {
			id = (String) SuperHelper.GeneratePrimaryKey();
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

	private String ySDM;
	private boolean modify_ySDM = false;

	public String getYSDM() {
		return ySDM;
	}

	public void setYSDM(String ySDM) {
		if (this.ySDM != ySDM) {
			this.ySDM = ySDM;
			modify_ySDM = true;
		}
	}

	private String zDDM;
	private boolean modify_zDDM = false;

	public String getZDDM() {
		return zDDM;
	}

	public void setZDDM(String zDDM) {
		if (this.zDDM != zDDM) {
			this.zDDM = zDDM;
			modify_zDDM = true;
		}
	}

	private String bDCDYH;
	private boolean modify_bDCDYH = false;

	public String getBDCDYH() {
		return bDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		if (this.bDCDYH != bDCDYH) {
			this.bDCDYH = bDCDYH;
			modify_bDCDYH = true;
		}
	}

	private String zDTZM;
	private boolean modify_zDTZM = false;

	public String getZDTZM() {
		return zDTZM;
	}

	public void setZDTZM(String zDTZM) {
		if (this.zDTZM != zDTZM) {
			this.zDTZM = zDTZM;
			modify_zDTZM = true;
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

	private String yT;
	private boolean modify_yT = false;

	public String getYT() {
		return yT;
	}

	public void setYT(String yT) {
		if (this.yT != yT) {
			this.yT = yT;
			modify_yT = true;
		}
	}

	private String qLLX;
	private boolean modify_qLLX = false;

	public String getQLLX() {
		return qLLX;
	}

	public void setQLLX(String qLLX) {
		if (this.qLLX != qLLX) {
			this.qLLX = qLLX;
			modify_qLLX = true;
		}
	}

	private String qLXZ;
	private boolean modify_qLXZ = false;

	public String getQLXZ() {
		return qLXZ;
	}

	public void setQLXZ(String qLXZ) {
		if (this.qLXZ != qLXZ) {
			this.qLXZ = qLXZ;
			modify_qLXZ = true;
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

	private String zDSZD;
	private boolean modify_zDSZD = false;

	public String getZDSZD() {
		return zDSZD;
	}

	public void setZDSZD(String zDSZD) {
		if (this.zDSZD != zDSZD) {
			this.zDSZD = zDSZD;
			modify_zDSZD = true;
		}
	}

	private String zDSZN;
	private boolean modify_zDSZN = false;

	public String getZDSZN() {
		return zDSZN;
	}

	public void setZDSZN(String zDSZN) {
		if (this.zDSZN != zDSZN) {
			this.zDSZN = zDSZN;
			modify_zDSZN = true;
		}
	}

	private String zDSZX;
	private boolean modify_zDSZX = false;

	public String getZDSZX() {
		return zDSZX;
	}

	public void setZDSZX(String zDSZX) {
		if (this.zDSZX != zDSZX) {
			this.zDSZX = zDSZX;
			modify_zDSZX = true;
		}
	}

	private String zDSZB;
	private boolean modify_zDSZB = false;

	public String getZDSZB() {
		return zDSZB;
	}

	public void setZDSZB(String zDSZB) {
		if (this.zDSZB != zDSZB) {
			this.zDSZB = zDSZB;
			modify_zDSZB = true;
		}
	}

	private String zDT;
	private boolean modify_zDT = false;

	public String getZDT() {
		return zDT;
	}

	public void setZDT(String zDT) {
		if (this.zDT != zDT) {
			this.zDT = zDT;
			modify_zDT = true;
		}
	}

	private String tFH;
	private boolean modify_tFH = false;

	public String getTFH() {
		return tFH;
	}

	public void setTFH(String tFH) {
		if (this.tFH != tFH) {
			this.tFH = tFH;
			modify_tFH = true;
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

	private String zT;
	private boolean modify_zT = false;

	public String getZT() {
		return zT;
	}

	public void setZT(String zT) {
		if (this.zT != zT) {
			this.zT = zT;
			modify_zT = true;
		}
	}

	private String qXDM;
	private boolean modify_qXDM = false;

	public String getQXDM() {
		return qXDM;
	}

	public void setQXDM(String qXDM) {
		if (this.qXDM != qXDM) {
			this.qXDM = qXDM;
			modify_qXDM = true;
		}
	}

	private String qXMC;
	private boolean modify_qXMC = false;

	public String getQXMC() {
		return qXMC;
	}

	public void setQXMC(String qXMC) {
		if (this.qXMC != qXMC) {
			this.qXMC = qXMC;
			modify_qXMC = true;
		}
	}

	private String dJQDM;
	private boolean modify_dJQDM = false;

	public String getDJQDM() {
		return dJQDM;
	}

	public void setDJQDM(String dJQDM) {
		if (this.dJQDM != dJQDM) {
			this.dJQDM = dJQDM;
			modify_dJQDM = true;
		}
	}

	private String dJQMC;
	private boolean modify_dJQMC = false;

	public String getDJQMC() {
		return dJQMC;
	}

	public void setDJQMC(String dJQMC) {
		if (this.dJQMC != dJQMC) {
			this.dJQMC = dJQMC;
			modify_dJQMC = true;
		}
	}

	private String dJZQDM;
	private boolean modify_dJZQDM = false;

	public String getDJZQDM() {
		return dJZQDM;
	}

	public void setDJZQDM(String dJZQDM) {
		if (this.dJZQDM != dJZQDM) {
			this.dJZQDM = dJZQDM;
			modify_dJZQDM = true;
		}
	}

	private String dJZQMC;
	private boolean modify_dJZQMC = false;

	public String getDJZQMC() {
		return dJZQMC;
	}

	public void setDJZQMC(String dJZQMC) {
		if (this.dJZQMC != dJZQMC) {
			this.dJZQMC = dJZQMC;
			modify_dJZQMC = true;
		}
	}

	private String zH;
	private boolean modify_zH = false;

	public String getZH() {
		return zH;
	}

	public void setZH(String zH) {
		if (this.zH != zH) {
			this.zH = zH;
			modify_zH = true;
		}
	}

	private String zM;
	private boolean modify_zM = false;

	public String getZM() {
		return zM;
	}

	public void setZM(String zM) {
		if (this.zM != zM) {
			this.zM = zM;
			modify_zM = true;
		}
	}

	private String fBFDM;
	private boolean modify_fBFDM = false;

	public String getFBFDM() {
		return fBFDM;
	}

	public void setFBFDM(String fBFDM) {
		if (this.fBFDM != fBFDM) {
			this.fBFDM = fBFDM;
			modify_fBFDM = true;
		}
	}

	private String fBFMC;
	private boolean modify_fBFMC = false;

	public String getFBFMC() {
		return fBFMC;
	}

	public void setFBFMC(String fBFMC) {
		if (this.fBFMC != fBFMC) {
			this.fBFMC = fBFMC;
			modify_fBFMC = true;
		}
	}

	private Double cBMJ;
	private boolean modify_cBMJ = false;

	public Double getCBMJ() {
		return cBMJ;
	}

	public void setCBMJ(Double cBMJ) {
		if (this.cBMJ != cBMJ) {
			this.cBMJ = cBMJ;
			modify_cBMJ = true;
		}
	}

	private Date cBQSSJ;
	private boolean modify_cBQSSJ = false;

	public Date getCBQSSJ() {
		return cBQSSJ;
	}

	public void setCBQSSJ(Date cBQSSJ) {
		if (this.cBQSSJ != cBQSSJ) {
			this.cBQSSJ = cBQSSJ;
			modify_cBQSSJ = true;
		}
	}

	private Date cBJSSJ;
	private boolean modify_cBJSSJ = false;

	public Date getCBJSSJ() {
		return cBJSSJ;
	}

	public void setCBJSSJ(Date cBJSSJ) {
		if (this.cBJSSJ != cBJSSJ) {
			this.cBJSSJ = cBJSSJ;
			modify_cBJSSJ = true;
		}
	}

	private String tDSYQXZ;
	private boolean modify_tDSYQXZ = false;

	public String getTDSYQXZ() {
		return tDSYQXZ;
	}

	public void setTDSYQXZ(String tDSYQXZ) {
		if (this.tDSYQXZ != tDSYQXZ) {
			this.tDSYQXZ = tDSYQXZ;
			modify_tDSYQXZ = true;
		}
	}

	private String sYTTLX;
	private boolean modify_sYTTLX = false;

	public String getSYTTLX() {
		return sYTTLX;
	}

	public void setSYTTLX(String sYTTLX) {
		if (this.sYTTLX != sYTTLX) {
			this.sYTTLX = sYTTLX;
			modify_sYTTLX = true;
		}
	}

	private String yZYFS;
	private boolean modify_yZYFS = false;

	public String getYZYFS() {
		return yZYFS;
	}

	public void setYZYFS(String yZYFS) {
		if (this.yZYFS != yZYFS) {
			this.yZYFS = yZYFS;
			modify_yZYFS = true;
		}
	}

	private String cYZL;
	private boolean modify_cYZL = false;

	public String getCYZL() {
		return cYZL;
	}

	public void setCYZL(String cYZL) {
		if (this.cYZL != cYZL) {
			this.cYZL = cYZL;
			modify_cYZL = true;
		}
	}

	private Integer sYZCL;
	private boolean modify_sYZCL = false;

	public Integer getSYZCL() {
		return sYZCL;
	}

	public void setSYZCL(Integer sYZCL) {
		if (this.sYZCL != sYZCL) {
			this.sYZCL = sYZCL;
			modify_sYZCL = true;
		}
	}

	private String rELATIONID;
	private boolean modify_rELATIONID = false;

	public String getRELATIONID() {
		return rELATIONID;
	}

	public void setRELATIONID(String rELATIONID) {
		if (this.rELATIONID != rELATIONID) {
			this.rELATIONID = rELATIONID;
			modify_rELATIONID = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_ySDM = false;
		modify_zDDM = false;
		modify_bDCDYH = false;
		modify_zDTZM = false;
		modify_zL = false;
		modify_yT = false;
		modify_qLLX = false;
		modify_qLXZ = false;
		modify_sYQLX = false;
		modify_zDSZD = false;
		modify_zDSZN = false;
		modify_zDSZX = false;
		modify_zDSZB = false;
		modify_zDT = false;
		modify_tFH = false;
		modify_bZ = false;
		modify_zT = false;
		modify_qXDM = false;
		modify_qXMC = false;
		modify_dJQDM = false;
		modify_dJQMC = false;
		modify_dJZQDM = false;
		modify_dJZQMC = false;
		modify_zH = false;
		modify_zM = false;
		modify_fBFDM = false;
		modify_fBFMC = false;
		modify_cBMJ = false;
		modify_cBQSSJ = false;
		modify_cBJSSJ = false;
		modify_tDSYQXZ = false;
		modify_sYTTLX = false;
		modify_yZYFS = false;
		modify_cYZL = false;
		modify_sYZCL = false;
		modify_rELATIONID = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_zDDM)
			listStrings.add("zDDM");
		if (!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if (!modify_zDTZM)
			listStrings.add("zDTZM");
		if (!modify_zL)
			listStrings.add("zL");
		if (!modify_yT)
			listStrings.add("yT");
		if (!modify_qLLX)
			listStrings.add("qLLX");
		if (!modify_qLXZ)
			listStrings.add("qLXZ");
		if (!modify_sYQLX)
			listStrings.add("sYQLX");
		if (!modify_zDSZD)
			listStrings.add("zDSZD");
		if (!modify_zDSZN)
			listStrings.add("zDSZN");
		if (!modify_zDSZX)
			listStrings.add("zDSZX");
		if (!modify_zDSZB)
			listStrings.add("zDSZB");
		if (!modify_zDT)
			listStrings.add("zDT");
		if (!modify_tFH)
			listStrings.add("tFH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_zT)
			listStrings.add("zT");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_qXMC)
			listStrings.add("qXMC");
		if (!modify_dJQDM)
			listStrings.add("dJQDM");
		if (!modify_dJQMC)
			listStrings.add("dJQMC");
		if (!modify_dJZQDM)
			listStrings.add("dJZQDM");
		if (!modify_dJZQMC)
			listStrings.add("dJZQMC");
		if (!modify_zH)
			listStrings.add("zH");
		if (!modify_zM)
			listStrings.add("zM");
		if (!modify_fBFDM)
			listStrings.add("fBFDM");
		if (!modify_fBFMC)
			listStrings.add("fBFMC");
		if (!modify_cBMJ)
			listStrings.add("cBMJ");
		if (!modify_cBQSSJ)
			listStrings.add("cBQSSJ");
		if (!modify_cBJSSJ)
			listStrings.add("cBJSSJ");
		if (!modify_tDSYQXZ)
			listStrings.add("tDSYQXZ");
		if (!modify_sYTTLX)
			listStrings.add("sYTTLX");
		if (!modify_yZYFS)
			listStrings.add("yZYFS");
		if (!modify_cYZL)
			listStrings.add("cYZL");
		if (!modify_sYZCL)
			listStrings.add("sYZCL");
		if (!modify_rELATIONID)
			listStrings.add("rELATIONID");

		return StringHelper.ListToStrings(listStrings);
	}
}
