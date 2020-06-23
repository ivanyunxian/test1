package com.supermap.realestate.registration.model;

import com.supermap.realestate.registration.model.genrt.GenerateLOG_API;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "log_api", schema = "LOG")
public class LOG_API extends GenerateLOG_API {
	@Override
	@Id
	@Column(name = "id" )
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "apiurl")
	public String getAPIURL() {
		return super.getAPIURL();
	}

	@Override
	@Column(name = "apiparam")
	public String getAPIPARAM() {
		return super.getAPIPARAM();
	}

	@Override
	@Column(name = "apitype")
	public String getAPITYPE() {
		return super.getAPITYPE();
	}

	@Override
	@Column(name = "success")
	public String getSUCCESS() {
		return super.getSUCCESS();
	}
	
	@Override
	@Column(name = "error")
	public String getERROR() {
		return super.getERROR();
	}
	
	@Override
	@Column(name = "optime")
	public Date getOPTIME() {
		return super.getOPTIME();
	}


	@Override
	@Column(name = "sytype")
	public String getSYTYPE() {
		return super.getSYTYPE();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}


}
