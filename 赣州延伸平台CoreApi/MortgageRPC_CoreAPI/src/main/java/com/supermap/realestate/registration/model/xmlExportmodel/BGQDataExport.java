package com.supermap.realestate.registration.model.xmlExportmodel;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BGQData")
public class BGQDataExport {
	private List<Object> BDCDY;
	private List<Object> BDCQL;
	private List<Object> BDCQLRS;

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
