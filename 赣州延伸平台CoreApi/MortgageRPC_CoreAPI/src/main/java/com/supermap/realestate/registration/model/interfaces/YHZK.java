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
 * 用海状况类
 * 
 * @ClassName: YHZK
 * @author yuxuebin
 * @date 2016年07月05日 11:13:52
 */
public interface YHZK {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getBDCDYID();

	public abstract void setBDCDYID(String bDCDYID);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getZHDM();

	public abstract void setZHDM(String zHDM);

	public abstract String getYHFS();

	public abstract void setYHFS(String yHFS);

	public abstract Double getYHMJ();

	public abstract void setYHMJ(Double yHMJ);

	public abstract String getJTYT();

	public abstract void setJTYT(String jTYT);

	public abstract Double getSYJSE();

	public abstract void setSYJSE(Double sYJSE);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);
}