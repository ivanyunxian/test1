package com.supermap.realestate.registration.ViewClass;

import java.util.List;

/**
 * 
 * @Description:缴款书信息
 * @author buxiaobo
 * @date 2015年12月5日 22:23:18
 * @Copyright SuperMap
 */
public class JKSInfo {
	private String DTBM;// 大厅编码
	private String DWBM;// 单位编码
	private String DWMC;// 单位名称
	private String CURYEAR;// 当前年
	private String CURMONTH;// 当前月
	private String CURDAY;// 当前日
	private String FKRMC;// 付款人名称
	private String SKRMC;// 收款人名称
	private String SKRZH;// 收款人帐号
	private String SKRYH;// 收款人银行
 	private List<SRXM> SRXMS;// 收入项目list
	private String ZJEDX;// 总金额大写
	private String ZJE;// 总金额
	private String ZL;
	private Double MJ;
	private String DM;

	// 收入项目
	public static class SRXM {
		
		private String BM;// 编码
		private String SRXMMC;// 收入项目
		private String JE;// 金额
		private String DM;
	
		private String SFJS;//收费标准
		private String TS;//套数/数量
		private String XSGS;//计算公式
		private String JSGS;//计算公式
		public String getBM() {
			return BM;
		}
		public void setBM(String bM) {
			BM = bM;
		}
		
		public String getSRXMMC() {
			return SRXMMC;
		}
		public void setSRXMMC(String sRXMMC) {
			SRXMMC = sRXMMC;
		}
		
		public String getJE() {
			return JE;
		}
		public void setJE(String jE) {
			JE = jE;
		}
		public String getDM() {
			return DM;
		}
		public void setDM(String dM) {
			DM = dM;
		}
		public String getSFJS() {
			return SFJS;
		}
		public void setSFJS(String sFJS) {
			SFJS = sFJS;
		}
		public String getTS() {
			return TS;
		}
		public void setTS(String tS) {
			TS = tS;
		}
		public String getXSGS() {
			return XSGS;
		}
		public void setXSGS(String xSGS) {
			XSGS = xSGS;
		}
		public String getJSGS() {
			return JSGS;
		}
		public void setJSGS(String jSGS) {
			JSGS = jSGS;
		}
		
	}

	
	public String getDTBM() {
		return DTBM;
	}
	public void setDTBM(String dTBM) {
		DTBM = dTBM;
	}
	
	public String getDWMC() {
		return DWMC;
	}
	public void setDWMC(String dWMC) {
		DWMC = dWMC;
	}
	
	public String getDWBM() {
		return DWBM;
	}
	public void setDWBM(String dWBM) {
		DWBM = dWBM;
	}
	
	public String getCURYEAR() {
		return CURYEAR;
	}
	public void setCURYEAR(String cURYEAR) {
		CURYEAR = cURYEAR;
	}
	
	public String getCURMONTH() {
		return CURMONTH;
	}
	public void setCURMONTH(String cURMONTH) {
		CURMONTH = cURMONTH;
	}
	
	public String getCURDAY() {
		return CURDAY;
	}
	public void setCURDAY(String cURDAY) {
		CURDAY = cURDAY;
	}
	
	public String getFKRMC() {
		return FKRMC;
	}
	public void setFKRMC(String fKRMC) {
		FKRMC = fKRMC;
	}
	
	public String getSKRMC() {
		return SKRMC;
	}
	public void setSKRMC(String sKRMC) {
		SKRMC = sKRMC;
	}
	
	public String getSKRZH() {
		return SKRZH;
	}
	public void setSKRZH(String sKRZH) {
		SKRZH = sKRZH;
	}
	
	public String getSKRYH() {
		return SKRYH;
	}
	public void setSKRYH(String sKRYH) {
		SKRYH = sKRYH;
	}
	
	public List<SRXM> getSRXMS() {
		return SRXMS;
	}
	public void setSRXMS(List<SRXM> sRXMS) {
		SRXMS = sRXMS;
	}

	public String getZJEDX() {
		return ZJEDX;
	}
	public void setZJEDX(String zJEDX) {
		ZJEDX = zJEDX;
	}
	
	public String getZJE() {
		return ZJE;
	}
	public void setZJE(String zJE) {
		ZJE = zJE;
	}
	public String getZL() {
		return ZL;
	}
	public void setZL(String zL) {
		ZL = zL;
	}
	public Double getMJ() {
		return MJ;
	}
	public void setMJ(Double mJ) {
		MJ = mJ;
	}
	public String getDM() {
		return DM;
	}
	public void setDM(String dM) {
		DM = dM;
	}

}