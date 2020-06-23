package com.supermap.realestate.registration.ViewClass;

import java.util.List;

import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;

public class FDCQDB {
	
	/** 不动产单元号 */
	private String BDCDYH;
	
	/** 坐落 */
	private String ZL;
	
	/** 房屋所有权人 */
	private String FWSYQR;
	
	/** 证件种类 */
	private String ZJZL;
	
	/** 证件号 */
	private String ZJH;
	
	/** 房屋共有情况 */
	private String FWGYQK;
	
	/** 权利人类型 */
	private String QLRLX;
	
	/** 登记类型 */
	private String DJLX;
	
	/** 登记原因 */
	private String DJYY;
	
	/** 土地使用权人 */
	private String TDSYQR;
	
	/** 独用土地面积 */
	private String DYTDMJ;
	
	/** 分摊土地面积 */
	private String FTTDMJ;
	
	/** 土地使用期限 */
	private String TDSYQX;
	
	/** 房地产交易价格 */
	private String FDCJYJG;
	
	/** 规划用途 */
	private String GHYT;
	
	/** 房屋结构 */
	private String FWJG;
	
	/** 所在层/总层数 */
	private String SZC_ZCS;
	
	/** 建筑面积 */
	private String JZMJ;
	
	/** 专有建筑面积 */
	private String ZYJZMJ;
	
	/** 分摊建筑面积 */
	private String FTJZMJ;
	
	/** 竣工时间 */
	private String JGSJ;
	
	/** 不动产单元号 */
	private String BDCQZH;
	
	/** 登记时间 */
	private String DJSJ;
	
	/** 登簿人 */
	private String DBR;
	
	/** 附件 */
	private String FJ;
	
	private String QHDM;
	
	/** 项目编号 */
	private String XMBH;
	
	/** 自然幢列表(项目内多幢时使用) */
	private List<BDCS_ZRZ_GZ> zrzList;
	/**有无土地证状态标识码 */
	private String td_status;
	/**
	 * 获取不动产单元号
	 * 
	 * @return
	 */
	public String getBDCDYH() {
		return BDCDYH;
	}

	/**
	 * 设置不动产单员号
	 * 
	 * @param bDCDYH
	 */
	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}

	/**
	 * 获取房地坐落
	 * 
	 * @return
	 */
	public String getZL() {
		return ZL;
	}

	/**
	 * 设置房地坐落
	 * 
	 * @param zL
	 */
	public void setZL(String zL) {
		ZL = zL;
	}

	/**
	 * 获取房屋所有权人
	 * 
	 * @return
	 */
	public String getFWSYQR() {
		return FWSYQR;
	}

	/**
	 * 设置房屋所有权人
	 * 
	 * @param fWSYQR
	 */
	public void setFWSYQR(String fWSYQR) {
		FWSYQR = fWSYQR;
	}

	/**
	 * 获取证件种类
	 * 
	 * @return
	 */
	public String getZJZL() {
		return ZJZL;
	}

	/**
	 * 设置证件种类
	 * 
	 * @param zJZL
	 */
	public void setZJZL(String zJZL) {
		ZJZL = zJZL;
	}

	/**
	 * 获取证件号
	 * 
	 * @return
	 */
	public String getZJH() {
		return ZJH;
	}

	/**
	 * 设置证件号
	 * 
	 * @param zJH
	 */
	public void setZJH(String zJH) {
		ZJH = zJH;
	}

	/**
	 * 获取房屋共有情况
	 * 
	 * @return
	 */
	public String getFWGYQK() {
		return FWGYQK;
	}

	/**
	 * 设置房屋共有情况
	 * 
	 * @param fWGYQK
	 */
	public void setFWGYQK(String fWGYQK) {
		FWGYQK = fWGYQK;
	}

	/**
	 * 获取权利人类型
	 * 
	 * @return
	 */
	public String getQLRLX() {
		return QLRLX;
	}

	/**
	 * 设置权利人类型
	 * 
	 * @param qLRLX
	 */
	public void setQLRLX(String qLRLX) {
		QLRLX = qLRLX;
	}

	/**
	 * 获取登记类型
	 * 
	 * @return
	 */
	public String getDJLX() {
		return DJLX;
	}

	/**
	 * 设置登记类型
	 * 
	 * @param dJLX
	 */
	public void setDJLX(String dJLX) {
		DJLX = dJLX;
	}

	/**
	 * 获取登记原因
	 * 
	 * @return
	 */
	public String getDJYY() {
		return DJYY;
	}

	/**
	 * 设置登记原因
	 * 
	 * @param dJYY
	 */
	public void setDJYY(String dJYY) {
		DJYY = dJYY;
	}

	/**
	 * 获取土地使用权人
	 * 
	 * @return
	 */
	public String getTDSYQR() {
		return TDSYQR;
	}

	/**
	 * 设置土地使用权人
	 * 
	 * @param tDSYQR
	 */
	public void setTDSYQR(String tDSYQR) {
		TDSYQR = tDSYQR;
	}

	/**
	 * 获取独用土地面积（m2）
	 * 
	 * @return
	 */
	public String getDYTDMJ() {
		return DYTDMJ;
	}

	/**
	 * 设置独用土地面积（m2）
	 * 
	 * @param dYTDMJ
	 */
	public void setDYTDMJ(String dYTDMJ) {
		DYTDMJ = dYTDMJ;
	}

	/**
	 * 获取分摊土地面积（m2)
	 * 
	 * @return
	 */
	public String getFTTDMJ() {
		return FTTDMJ;
	}

	/**
	 * 设置分摊土地面积（m2）
	 * 
	 * @param fTTDMJ
	 */
	public void setFTTDMJ(String fTTDMJ) {
		FTTDMJ = fTTDMJ;
	}

	/**
	 * 获取土地使用期限(格式：2015年5月10日起/2085年5月10日止)
	 * 
	 * @return
	 */
	public String getTDSYQX() {
		return TDSYQX;
	}

	/**
	 * 设置土地使用期限(格式：2015年5月10日起/2085年5月10日止)
	 * 
	 * @param tDSYQX
	 */
	public void setTDSYQX(String tDSYQX) {
		TDSYQX = tDSYQX;
	}

	/**
	 * 获取房地产交易价格（万元）
	 * 
	 * @return
	 */
	public String getFDCJYJG() {
		return FDCJYJG;
	}

	/**
	 * 设置房地产交易价格（万元）
	 * 
	 * @param fDCJYJG
	 */
	public void setFDCJYJG(String fDCJYJG) {
		FDCJYJG = fDCJYJG;
	}

	/**
	 * 获取规划用途
	 * 
	 * @return
	 */
	public String getGHYT() {
		return GHYT;
	}

	/**
	 * 设置规划用途
	 * 
	 * @param gHYT
	 */
	public void setGHYT(String gHYT) {
		GHYT = gHYT;
	}

	/**
	 * 获取房屋结构
	 * 
	 * @return
	 */
	public String getFWJG() {
		return FWJG;
	}

	/**
	 * 设置房屋结构
	 * 
	 * @param fWJG
	 */
	public void setFWJG(String fWJG) {
		FWJG = fWJG;
	}

	/**
	 * 获取所在层/总层数
	 * 
	 * @return
	 */
	public String getSZC_ZCS() {
		return SZC_ZCS;
	}

	/**
	 * 设置所在层/总层数
	 * 
	 * @param sZC_ZCS
	 */
	public void setSZC_ZCS(String sZC_ZCS) {
		SZC_ZCS = sZC_ZCS;
	}

	/**
	 * 获取建筑面积（m2）
	 * 
	 * @return
	 */
	public String getJZMJ() {
		return JZMJ;
	}

	/**
	 * 设置建筑面积（m2）
	 * 
	 * @param jZMJ
	 */
	public void setJZMJ(String jZMJ) {
		JZMJ = jZMJ;
	}

	/**
	 * 获取专有建筑面积（m2）
	 * 
	 * @return
	 */
	public String getZYJZMJ() {
		return ZYJZMJ;
	}

	/**
	 * 设置专有建筑面积（m2）
	 * 
	 * @param zYJZMJ
	 */
	public void setZYJZMJ(String zYJZMJ) {
		ZYJZMJ = zYJZMJ;
	}

	/**
	 * 获取分摊建筑面积（m2）
	 * 
	 * @return
	 */
	public String getFTJZMJ() {
		return FTJZMJ;
	}

	/**
	 * 设置分摊建筑面积（m2）
	 * 
	 * @param fTJZMJ
	 */
	public void setFTJZMJ(String fTJZMJ) {
		FTJZMJ = fTJZMJ;
	}

	/**
	 * 获取竣工时间
	 * 
	 * @return
	 */
	public String getJGSJ() {
		return JGSJ;
	}

	/**
	 * 设置竣工时间
	 * 
	 * @param jGSJ
	 */
	public void setJGSJ(String jGSJ) {
		JGSJ = jGSJ;
	}

	/**
	 * 获取不动产单元号
	 * 
	 * @return
	 */
	public String getBDCQZH() {
		return BDCQZH;
	}

	/**
	 * 设置不动产单员号
	 * 
	 * @param bDCQZH
	 */
	public void setBDCQZH(String bDCQZH) {
		BDCQZH = bDCQZH;
	}

	/**
	 * 获取登记时间
	 * 
	 * @return
	 */
	public String getDJSJ() {
		return DJSJ;
	}

	/**
	 * 设置登记时间
	 * 
	 * @param dJSJ
	 */
	public void setDJSJ(String dJSJ) {
		DJSJ = dJSJ;
	}

	/**
	 * 获取登簿人
	 * 
	 * @return
	 */
	public String getDBR() {
		return DBR;
	}

	/**
	 * 设置登簿人
	 * 
	 * @param dBR
	 */
	public void setDBR(String dBR) {
		DBR = dBR;
	}

	/**
	 * 获取附件
	 * 
	 * @return
	 */
	public String getFJ() {
		return FJ;
	}

	/**
	 * 设置附件
	 * 
	 * @param fJ
	 */
	public void setFJ(String fJ) {
		FJ = fJ;
	}

	/**
	 * 获取附件
	 * 
	 * @return
	 */
	public String getQHDM() {
		return QHDM;
	}

	/**
	 * 设置附件
	 * 
	 * @param fJ
	 */
	public void setQHDM(String qHDM) {
		QHDM = qHDM;
	}
	
	/**
	 * 获取附件
	 * 
	 * @return
	 */
	public String getXMBH() {
		return XMBH;
	}

	/**
	 * 设置附件
	 * 
	 * @param fJ
	 */
	public void setXMBH(String xMBH) {
		XMBH = xMBH;
	}

	public List<BDCS_ZRZ_GZ> getZrzList() {
		return zrzList;
	}

	public void setZrzList(List<BDCS_ZRZ_GZ> zrzList) {
		this.zrzList = zrzList;
	}
	
	public String getTd_status() {
		return td_status;
	}

	public void setTd_status(String td_status) {
		this.td_status = td_status;
	}
	
	/*//使用权面积
	private String SYQMJ;
	
	public String getSYQMJ() {
		return SYQMJ;
	}

	public void setSYQMJ(String SYQMJ) {
		this.SYQMJ = SYQMJ;
	}*/
	
	//使用权面积
	private String QDJG;
		
	public String getQDJG() {
		return QDJG;
	}

	public void setQDJG(String QDJG) {
		this.QDJG = QDJG;
	}
	
}
