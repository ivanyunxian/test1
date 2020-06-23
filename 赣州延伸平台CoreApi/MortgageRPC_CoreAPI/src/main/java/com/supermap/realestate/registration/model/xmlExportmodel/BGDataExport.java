package com.supermap.realestate.registration.model.xmlExportmodel;

import java.util.List;

import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.BGBDCDYExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.FDCQ2Export;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BGData")
public class BGDataExport {
	@XStreamAlias("BDCDY")
	private BGBDCDYExport BDCDY;
	@XStreamAlias("BDCQLS")
	private List<Object> BDCQL;
	@XStreamAlias("BDCQLRS")
	private List<QLRExport> BDCQLRS;

	public BGBDCDYExport getBDCDY() {
		return BDCDY;
	}

	public void setBDCDY(BGBDCDYExport bDCDY) {
		BDCDY = bDCDY;
	}

	public List<Object> getBDCQL() {
		return BDCQL;
	}

	public void setBDCQL(List<Object> bDCQL) {
		BDCQL = bDCQL;
	}

	public List<QLRExport> getBDCQLRS() {
		return BDCQLRS;
	}

	public void setBDCQLRS(List<QLRExport> bDCQLRS) {
		BDCQLRS = bDCQLRS;
	}
}
