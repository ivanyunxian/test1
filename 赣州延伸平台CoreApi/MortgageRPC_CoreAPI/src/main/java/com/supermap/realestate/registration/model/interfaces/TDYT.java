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
 * 土地用途类
 * 
 * @ClassName: TDYD
 * @author liushufeng
 * @date 2015年10月27日 上午9:34:52
 */
public interface TDYT {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getBDCDYID();

	public abstract void setBDCDYID(String bDCDYID);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getSFZYT();

	public abstract void setSFZYT(String sFZYT);

	public abstract String getTDYT();

	public abstract void setTDYT(String tDYT);

	public abstract String getTDYTMC();

	public abstract void setTDYTMC(String tDYTMC);

	public abstract String getTDDJ();

	public abstract void setTDDJ(String tDDJ);

	public abstract Double getTDJG();

	public abstract void setTDJG(Double tDJG);

	public abstract Date getQSRQ();

	public abstract void setQSRQ(Date qSRQ);

	public abstract Date getZZRQ();

	public abstract void setZZRQ(Date zZRQ);

	public abstract String getCRJBZ();

	public abstract void setCRJBZ(String cRJBZ);
	
	public abstract String getQLXZ();

	public abstract void setQLXZ(String qLXZ);
	
	public abstract Double getTDMJ();

	public abstract void setTDMJ(Double tDMJ);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);

	public abstract String getTDYTName();
	
	public abstract String getTDDJName();
	
	public abstract String getQLXZName();
	
	public abstract String getZYSZ();

	public abstract void setZYSZ(String zYSZ);
}