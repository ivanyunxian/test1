package com.supermap.realestate.registration.model.genrt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.utility.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateLOG_API implements SuperModel<String> {

	private CommonDao dao;
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
	


	private String aPIURL;
	private boolean modify_aPIURL= false;

	public String getAPIURL() {
		return aPIURL;
	}

	public void setAPIURL(String aPIURL) {
		if (this.aPIURL != aPIURL) {
			this.aPIURL = aPIURL;
			modify_aPIURL = true;
		}
	}

	private String aPIPARAM;
	private boolean modify_aPIPARAM = false;

	public String getAPIPARAM() {
		return aPIPARAM;
	}

	public void setAPIPARAM(String aPIPARAM) {
		if (this.aPIPARAM != aPIPARAM) {
			this.aPIPARAM = aPIPARAM;
			modify_aPIPARAM = true;
		}
	}

	private String aPITYPE;
	private boolean modify_aPITYPE = false;

	public String getAPITYPE() {
		return aPITYPE;
	}

	public void setAPITYPE(String aPITYPE) {
		if (this.aPITYPE != aPITYPE) {
			this.aPITYPE = aPITYPE;
			modify_aPITYPE = true;
		}
	}
	
	private String sUCCESS;
	private boolean modify_sUCCESS = false;

	public String getSUCCESS() {
		return sUCCESS;
	}

	public void setSUCCESS(String sUCCESS) {
		if (this.sUCCESS != sUCCESS) {
			this.sUCCESS = sUCCESS;
			modify_sUCCESS = true;
		}
	}
	
	private String eRROR;
	private boolean modify_eRROR = false;

	public String getERROR() {
		return eRROR;
	}

	public void setERROR(String eRROR) {
		if (this.eRROR != eRROR) {
			this.eRROR = eRROR;
			modify_eRROR = true;
		}
	}

	private Date oPTIME;
	private boolean modify_oPTIME = false;

	public Date getOPTIME() {
		return oPTIME;
	}

	public void setOPTIME(Date oPTIME) {
		if (this.oPTIME != oPTIME) {
			this.oPTIME = oPTIME;
			modify_oPTIME = true;
		}
	}

	private String sYTYPE;
	private boolean modify_sYTYPE = false;

	public String getSYTYPE() {
		return sYTYPE;
	}

	public void setSYTYPE(String sYTYPE) {
		if (this.sYTYPE != sYTYPE) {
			this.sYTYPE = sYTYPE;
			modify_sYTYPE = true;
		}
	}

	private String bz;
	private boolean modify_bz = false;

	public String getBZ() {
		return bz;
	}

	public void setBZ(String bz) {
		if (this.bz != bz) {
			this.bz = bz;
			modify_bz = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_aPIURL  = false;
		modify_aPIPARAM  = false;
		modify_aPITYPE  = false;
		modify_sUCCESS  = false;
		modify_eRROR  = false;
		modify_oPTIME  = false;
		modify_sYTYPE  = false;
		modify_bz  = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_aPIURL )
			listStrings.add("aPIURL");
		if (!modify_aPIPARAM )
			listStrings.add("aPIPARAM");
		if (!modify_aPITYPE )
			listStrings.add("aPITYPE");
		if (!modify_sUCCESS )
			listStrings.add("sUCCESS");
		if (!modify_eRROR )
			listStrings.add("eRROR");
		if (!modify_oPTIME )
			listStrings.add("oPTIME");
		if (!modify_sYTYPE )
			listStrings.add("sYTYPE");
		if (!modify_bz )
			listStrings.add("bz");

		return StringHelper.ListToStrings(listStrings);
	}
}
