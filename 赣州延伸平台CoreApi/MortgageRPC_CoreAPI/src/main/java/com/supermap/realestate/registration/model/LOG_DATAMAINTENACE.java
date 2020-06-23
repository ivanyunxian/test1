package com.supermap.realestate.registration.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateLOG_DATAMAINTENACE;

@Entity
@Table(name = "log_datamaintenace", schema = "LOG")
public class LOG_DATAMAINTENACE extends GenerateLOG_DATAMAINTENACE{
	@Override
	@Id
	@Column(name = "id" )
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Column(name = "lgtype")
	public Integer getLGTYPE() {
		return super.getLGTYPE();
	}

	@Override
	@Column(name = "lgtname")
	public String getLGTNAME() {
		return super.getLGTNAME();
	}

	@Override
	@Column(name = "mtlx")
	public Integer getMTLX() {
		return super.getMTLX();
	}

	@Override
	@Column(name = "mtlxname")
	public String getMTLXNAME() {
		return super.getMTLXNAME();
	}

	@Override
	@Column(name = "lgrymc")
	public String getLGRYMC() {
		return super.getLGRYMC();
	}

	@Override
	@Column(name = "lgryid")
	public String getLGRYID() {
		return super.getLGRYID();
	}
	
	@Override
	@Column(name = "lgmachine")
	public String getLGMACHINE() {
		return super.getLGMACHINE();
	}
	
	@Override
	@Column(name = "lgmac")
	public Double getLGMAC() {
		return super.getLGMAC();
	}


	@Override
	@Column(name = "lgtime")
	public Date getLGTIME() {
		return super.getLGTIME();
	}

	@Override
	@Column(name = "lgdescription")
	public String getLGDESCRIPTION() {
		return super.getLGDESCRIPTION();
	}

	@Override
	@Column(name = "lgcontent")
	public String getLGCONTENT() {
		return super.getLGCONTENT();
	}
}
