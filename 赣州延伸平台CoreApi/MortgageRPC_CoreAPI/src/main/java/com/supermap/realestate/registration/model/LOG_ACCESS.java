package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateLOG_ACCESS;

@Entity
@Table(name = "log_access", schema = "LOG")
public class LOG_ACCESS extends GenerateLOG_ACCESS{
	@Override
	@Id
	@Column(name = "id" )
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Column(name = "accessname")
	public String getACCESSNAME() {
		return super.getACCESSNAME();
	}

	@Override
	@Column(name = "password")
	public String getPASSWORD() {
		return super.getPASSWORD();
	}

	@Override
	@Column(name = "accessparam")
	public String getACCESSPARAM() {
		return super.getACCESSPARAM();
	}

	@Override
	@Column(name = "uselesscount")
	public Integer getUSELESSCOUNT() {
		return super.getUSELESSCOUNT();
	}
	
	@Override
	@Column(name = "lgmachine")
	public String getLGMACHINE() {
		return super.getLGMACHINE();
	}
	
	@Override
	@Column(name = "lgmac")
	public String getLGMAC() {
		return super.getLGMAC();
	}

	@Override
	@Column(name = "accesstime")
	public Date getACCESSTIME() {
		return super.getACCESSTIME();
	}

	@Override
	@Column(name = "uselesscause")
	public String getUSELESSCAUSE() {
		return super.getUSELESSCAUSE();
	}
	
	@Override
	@Column(name = "sfcg")
	public String getSFCG() {
		return super.getSFCG();
	}
	
	@Override
	@Column(name = "allowaccesstime")
	public Date getALLOWACCESSTIME() {
		return super.getALLOWACCESSTIME();
	}
	@Override
	@Column(name = "returnresult")
	public String getRETURNRESULT() {
		return super.getRETURNRESULT();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}
}
