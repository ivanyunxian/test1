package com.supermap.realestate.registration.model.xmlmodel;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Data")
public class DataW {

	@XStreamAlias("HTNR")
	private HTNR htnr;
	private List<Object> BDCDY;
	private List<Object> BDCQL;
	private List<Object> BDCQLRS;
	private List<Object> SFRS;

	public HTNR getHtnr() {
		return htnr;
	}

	public void setHtnr(HTNR htnr) {
		this.htnr = htnr;
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
