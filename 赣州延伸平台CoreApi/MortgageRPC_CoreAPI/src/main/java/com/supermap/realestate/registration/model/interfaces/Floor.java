/**   
 * 楼层接口
 * @Title: Building.java 
 * @Package com.supermap.realestate.registration.model.interfaces.newer 
 * @author liushufeng 
 * @date 2015年7月11日 下午4:58:10 
 * @version V1.0   
 */
package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

/**
 * 楼层接口
 * @ClassName: Building
 * @author liushufeng
 * @date 2015年7月11日 下午4:58:10
 */
public interface Floor {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	public abstract String getCH();

	public abstract void setCH(String cH);

	public abstract String getZRZBDCDYID();

	public abstract void setZRZBDCDYID(String zRZBDCDYID);

	public abstract String getZRZH();

	public abstract void setZRZH(String zRZH);

	public abstract Integer getSJC();

	public abstract void setSJC(Integer sJC);

	public abstract String getMYC();

	public abstract void setMYC(String mYC);

	public abstract Double getCJZMJ();

	public abstract void setCJZMJ(Double cJZMJ);

	public abstract Double getCTNJZMJ();

	public abstract void setCTNJZMJ(Double cTNJZMJ);

	public abstract Double getCYTMJ();

	public abstract void setCYTMJ(Double cYTMJ);

	public abstract Double getCGYJZMJ();

	public abstract void setCGYJZMJ(Double cGYJZMJ);

	public abstract Double getCFTJZMJ();

	public abstract void setCFTJZMJ(Double cFTJZMJ);

	public abstract Double getCG();

	public abstract void setCG(Double cG);

	public abstract Double getSPTYMJ();

	public abstract void setSPTYMJ(Double sPTYMJ);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);

}