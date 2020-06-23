package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BDCDY")
public class BGBDCDYExport {
	private ZRZExport ZRZ;// 自然幢
	@XStreamAlias("LJZS")
	private List<LJZExport> LJZ;// 逻辑幢
	@XStreamAlias("CS")
	private List<CExport> C;// 层
	@XStreamAlias("HS")
	private List<HExport> H;// 户

	public ZRZExport getZRZ() {
		return ZRZ;
	}

	public void setZRZ(ZRZExport zRZ) {
		ZRZ = zRZ;
	}


	public List<LJZExport> getLJZ() {
		return LJZ;
	}

	public void setLJZ(List<LJZExport> lJZ) {
		LJZ = lJZ;
	}

	public List<CExport> getC() {
		return C;
	}

	public void setC(List<CExport> c) {
		C = c;
	}

	public List<HExport> getH() {
		return H;
	}

	public void setH(List<HExport> h) {
		H = h;
	}

}
