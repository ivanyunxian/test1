package com.supermap.realestate.registration.model.genrt;

//liangcheng 2019-05-23 10:32 am

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateBDCS_SFXX implements SuperModel<String> {

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
	
	private String jKRY;
	private boolean modify_jKRY = false;

	public String getJKRY() {
		return jKRY;
	}

	public void setJKRY(String jKRY) {
		if (this.jKRY != jKRY) {
			this.jKRY = jKRY;
			modify_jKRY = true;
		}
	}
	
	private String fKFS;
	private boolean modify_fKFS = false;

	public String getFKFS() {
		return fKFS;
	}

	public void setFKFS(String fKFS) {
		if (this.fKFS != fKFS) {
			this.fKFS = fKFS;
			modify_fKFS = true;
		}
	}
	
	private String sFRY;
	private boolean modify_sFRY = false;

	public String getSFRY() {
		return sFRY;
	}

	public void setSFRY(String sFRY) {
		if (this.sFRY != sFRY) {
			this.sFRY = sFRY;
			modify_sFRY = true;
		}
	}
	
	private Date sFSJ;
	private boolean modify_sFSJ = false;

	public Date getSFSJ() {
		return sFSJ;
	}

	public void setSFSJ(Date sFSJ) {
		if (this.sFSJ != sFSJ) {
			this.sFSJ = sFSJ;
			modify_sFSJ = true;
		}
	}
	
	private String sFZT;
	private boolean modify_sFZT = false;

	public String getSFZT() {
		if (sFZT == null ||"".equals(sFZT)) {
			sFZT = "0";
		}
		return sFZT;
	}

	public void setSFZT(String sFZT) {
		if (this.sFZT != sFZT) {
			this.sFZT = sFZT;
			modify_sFZT = true;
		}
	}
	
	private Double sFJE;
	private boolean modify_sFJE = false;

	public Double getSFJE() {
		return sFJE;
	}

	public void setSFJE(Double sFJE) {
		if (this.sFJE != sFJE) {
			this.sFJE = sFJE;
			modify_sFJE = true;
		}
	}

	private String yWLSH;
	private boolean modify_yWLSH = false;

	public String getYWLSH() {
		return yWLSH;
	}

	public void setYWLSH(String yWLSH) {
		if (this.yWLSH != yWLSH) {
			this.yWLSH = yWLSH;
			modify_yWLSH = true;
		}
	}

	private String jFDH;
	private boolean modify_jFDH = false;

	public String getJFDH() {
		return jFDH;
	}

	public void setJFDH(String jFDH) {
		if (this.jFDH != jFDH) {
			this.jFDH = jFDH;
			modify_jFDH = true;
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
	private String pJZT;
	private boolean modify_pJZT = false;

	public String getPJZT() {
		return pJZT;
	}

	public void setPJZT(String pJZT) {
		if (this.pJZT != pJZT) {
			this.pJZT = pJZT;
			modify_pJZT = true;
		}
	}
	private String pJBZ;
	private boolean modify_pJBZ = false;

	public String getPJBZ() {
		return pJBZ;
	}

	public void setPJBZ(String pJBZ) {
		if (this.pJBZ != pJBZ) {
			this.pJBZ = pJBZ;
			modify_pJBZ = true;
		}
	}

	private Date cREATEDATE;
	private boolean modify_cREATEDATE = false;

	public Date getCREATEDATE() {
		return cREATEDATE;
	}

	public void setCREATEDATE(Date cREATEDATE) {
		if (this.cREATEDATE != cREATEDATE) {
			this.cREATEDATE = cREATEDATE;
			modify_cREATEDATE = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_jKRY = false;
		modify_fKFS = false;
		modify_sFRY = false;
		modify_sFSJ = false;
		modify_sFZT = false;
		modify_sFJE = false;
		modify_yWLSH = false;
		modify_jFDH = false;
		modify_bZ = false;
		modify_qXDM = false;
		modify_yXBZ = false;
		modify_cREATEDATE = false;
		modify_pJZT = false;
		modify_pJBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_jKRY)
			listStrings.add("jKRY");
		if (!modify_fKFS)
			listStrings.add("fKFS");
		if (!modify_sFRY)
			listStrings.add("sFRY");
		if (!modify_sFSJ)
			listStrings.add("sFSJ");
		if (!modify_sFZT)
			listStrings.add("sFZT");
		if (!modify_sFJE)
			listStrings.add("sFJE");
		if (!modify_yWLSH)
			listStrings.add("yWLSH");
		if (!modify_jFDH)
			listStrings.add("jFDH");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_qXDM)
			listStrings.add("qXDM");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");
		if (!modify_cREATEDATE)
			listStrings.add("cREATEDATE");
		if (!modify_pJZT)
			listStrings.add("pJZT");
		if (!modify_pJBZ)
			listStrings.add("pJBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
