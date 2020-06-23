package com.supermap.realestate.registration.model.xmlExportmodel;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Data")
public class DataExport {
	private HTNRExport HTNR;
	private List<Object> BDCDY;
	private List<Object> BDCQL;
	private List<Object> BDCQLRS;
	private List<Object> SFRS;

	public HTNRExport getHtnr() {
		return HTNR;
	}

	public void setHtnr(HTNRExport htnr) {
		HTNR = htnr;
	}

	public List<Object> getSFRS() {
		return SFRS;
	}

	public void setSFRS(List<Object> sFRS) {
		SFRS = sFRS;
	}

	public List<Object> getBDCDY() {
		return BDCDY;
	}

	public void setBDCDY(List<Object> bDCDY) {
		BDCDY = bDCDY;
	}

	public List<Object> getBDCQL() {
		return BDCQL;
	}

	public void setBDCQL(List<Object> bDCQL) {
		BDCQL = bDCQL;
	}

	public List<Object> getBDCQLRS() {
		return BDCQLRS;
	}

	public void setBDCQLRS(List<Object> bDCQLRS) {
		BDCQLRS = bDCQLRS;
	}
}
