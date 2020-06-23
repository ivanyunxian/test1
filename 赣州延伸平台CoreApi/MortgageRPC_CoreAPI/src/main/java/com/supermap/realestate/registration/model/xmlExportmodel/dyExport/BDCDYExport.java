package com.supermap.realestate.registration.model.xmlExportmodel.dyExport;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BDCDY")
public class BDCDYExport {
	private SYQZDExport SYQZD;// 所有权宗地
	private SHYQZDExport SHYQZD;// 使用权宗地
	private ZDBHQKExport ZDBHQK;// 宗地变化情况
	private ZHExport ZH;// 宗海
	private YHZKExport YHZK;// 用海状况
	private YHYDZBExport YHYDZB;// 用海坐标
	private ZHBHQKExport ZHBHQK;// 宗海变化情况
	private ZRZExport ZRZ;// 自然幢
	private GZWExport GZW;// 构筑物
	private MZDZWExport MZDZW;// 面状定着物
	private XZDZWExport XZDZW;// 线状定着物
	private DZDZWExport DZDZW;// 点状定着物
	private LJZExport LJZ;// 逻辑幢
	private CExport C;// 层
	private HExport H;// 户

	public SYQZDExport getSYQZD() {
		return SYQZD;
	}

	public void setSYQZD(SYQZDExport sYQZD) {
		SYQZD = sYQZD;
	}

	public SHYQZDExport getSHYQZD() {
		return SHYQZD;
	}

	public void setSHYQZD(SHYQZDExport sHYQZD) {
		SHYQZD = sHYQZD;
	}

	public ZDBHQKExport getZDBHQK() {
		return ZDBHQK;
	}

	public void setZDBHQK(ZDBHQKExport zDBHQK) {
		ZDBHQK = zDBHQK;
	}

	public ZHExport getZH() {
		return ZH;
	}

	public void setZH(ZHExport zH) {
		ZH = zH;
	}

	public YHZKExport getYHZK() {
		return YHZK;
	}

	public void setYHZK(YHZKExport yHZK) {
		YHZK = yHZK;
	}

	public YHYDZBExport getYHYDZB() {
		return YHYDZB;
	}

	public void setYHYDZB(YHYDZBExport yHYDZB) {
		YHYDZB = yHYDZB;
	}

	public ZHBHQKExport getZHBHQK() {
		return ZHBHQK;
	}

	public void setZHBHQK(ZHBHQKExport zHBHQK) {
		ZHBHQK = zHBHQK;
	}

	public ZRZExport getZRZ() {
		return ZRZ;
	}

	public void setZRZ(ZRZExport zRZ) {
		ZRZ = zRZ;
	}

	public GZWExport getGZW() {
		return GZW;
	}

	public void setGZW(GZWExport gZW) {
		GZW = gZW;
	}

	public MZDZWExport getMZDZW() {
		return MZDZW;
	}

	public void setMZDZW(MZDZWExport mZDZW) {
		MZDZW = mZDZW;
	}

	public XZDZWExport getXZDZW() {
		return XZDZW;
	}

	public void setXZDZW(XZDZWExport xZDZW) {
		XZDZW = xZDZW;
	}

	public DZDZWExport getDZDZW() {
		return DZDZW;
	}

	public void setDZDZW(DZDZWExport dZDZW) {
		DZDZW = dZDZW;
	}

	public LJZExport getLJZ() {
		return LJZ;
	}

	public void setLJZ(LJZExport lJZ) {
		LJZ = lJZ;
	}

	public CExport getC() {
		return C;
	}

	public void setC(CExport c) {
		C = c;
	}

	public HExport getH() {
		return H;
	}

	public void setH(HExport h) {
		H = h;
	}

}
