/**   
 * @Title: TDYD.java 
 * @Package com.supermap.realestate.registration.model.genrt 
 * @author liushufeng 
 * @date 2015年10月27日 上午9:34:52 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

/**
 * 用海用地坐标类
 * 
 * @ClassName: YHYDZB
 * @author yuxuebin
 * @date 2016年07月05日 11:34:52
 */
public interface YHYDZB {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getBDCDYID();

	public abstract void setBDCDYID(String bDCDYID);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getZHDM();

	public abstract void setZHDM(String zHDM);

	public abstract Integer getXH();

	public abstract void setXH(Integer xH);

	public abstract Double getBHYH();

	public abstract void setBHYH(Double bHYH);

	public abstract Double getBHNR();

	public abstract void setBHNR(Double bHNR);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);
}