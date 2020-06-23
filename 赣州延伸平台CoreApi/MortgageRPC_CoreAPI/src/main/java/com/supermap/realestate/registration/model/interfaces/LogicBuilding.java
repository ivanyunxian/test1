/**   
 * 逻辑幢接口
 * @Title: LogicBuilding.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午10:28:53 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

/**
 * 逻辑幢接口
 * @ClassName: LogicBuilding
 * @author liushufeng
 * @date 2015年7月11日 下午10:28:53
 */
public interface LogicBuilding {
	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getLJZH();

	public abstract void setLJZH(String lJZH);

	public abstract String getZRZBDCDYID();

	public abstract void setZRZBDCDYID(String zRZBDCDYID);

	public abstract String getZRZH();

	public abstract void setZRZH(String zRZH);

	public abstract String getMPH();

	public abstract void setMPH(String mPH);

	public abstract Double getYCJZMJ();

	public abstract void setYCJZMJ(Double yCJZMJ);

	public abstract Double getYCDXMJ();

	public abstract void setYCDXMJ(Double yCDXMJ);

	public abstract Double getYCQTMJ();

	public abstract void setYCQTMJ(Double yCQTMJ);

	public abstract Double getSCJZMJ();

	public abstract void setSCJZMJ(Double sCJZMJ);

	public abstract Double getSCDXMJ();

	public abstract void setSCDXMJ(Double sCDXMJ);

	public abstract Double getSCQTMJ();

	public abstract void setSCQTMJ(Double sCQTMJ);

	public abstract String getFWJG1();

	public abstract void setFWJG1(String fWJG1);

	public abstract String getFWJG2();

	public abstract void setFWJG2(String fWJG2);

	public abstract String getFWJG3();

	public abstract void setFWJG3(String fWJG3);

	public abstract String getJZWZT();

	public abstract void setJZWZT(String jZWZT);

	public abstract String getFWYT1();

	public abstract void setFWYT1(String fWYT1);

	public abstract String getFWYT2();

	public abstract void setFWYT2(String fWYT2);

	public abstract String getFWYT3();

	public abstract void setFWYT3(String fWYT3);

	public abstract Integer getZCS();

	public abstract void setZCS(Integer zCS);

	public abstract Integer getDSCS();

	public abstract void setDSCS(Integer dSCS);

	public abstract Integer getDXCS();

	public abstract void setDXCS(Integer dXCS);

	public abstract String getBZ();

	public abstract void setBZ(String bZ);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);
}
