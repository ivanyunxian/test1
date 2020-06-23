/**   
 * 宗海
 * @Title: Sea.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午9:48:54 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;
import java.util.List;

/**
 * 宗海
 * @ClassName: Sea
 * @author liushufeng
 * @date 2015年7月11日 下午9:48:54
 */
public interface Sea extends RealUnit {
	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getBDCDYH();

	public abstract void setBDCDYH(String bDCDYH);

	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getZHDM();

	public abstract void setZHDM(String zHDM);

	public abstract String getZHTZM();

	public abstract void setZHTZM(String zHTZM);

	public abstract String getQXDM();

	public abstract void setQXDM(String qXDM);

	public abstract String getZL();

	public abstract void setZL(String zL);

	public abstract String getXMMC();

	public abstract void setXMMC(String xMMC);

	public abstract String getXMXX();

	public abstract void setXMXX(String xMXX);

	public abstract Double getYHZMJ();

	public abstract void setYHZMJ(Double yHZMJ);

	public abstract Double getZHMJ();

	public abstract void setZHMJ(Double zHMJ);

	public abstract String getDB();

	public abstract void setDB(String dB);

	public abstract Double getZHAX();

	public abstract void setZHAX(Double zHAX);

	public abstract String getYHLXA();

	public abstract void setYHLXA(String yHLXA);

	public abstract String getYHLXB();

	public abstract void setYHLXB(String yHLXB);

	public abstract String getYHWZSM();

	public abstract void setYHWZSM(String yHWZSM);

	public abstract String getHDDM();

	public abstract void setHDDM(String hDDM);

	public abstract String getHDMC();

	public abstract void setHDMC(String hDMC);

	public abstract String getYDFW();

	public abstract void setYDFW(String yDFW);

	public abstract Double getYDMJ();

	public abstract void setYDMJ(Double yDMJ);	

	public abstract String getHDWZ();

	public abstract void setHDWZ(String hDWZ);

	public abstract String getHDYT();

	public abstract void setHDYT(String hDYT);

	public abstract Double getSYQMJ();

	public abstract void setSYQMJ(Double sYQMJ);

	public abstract Double getSYJZE();

	public abstract void setSYJZE(Double sYJZE);

	public abstract String getSYJBZYJ();

	public abstract void setSYJBZYJ(String sYJBZYJ);

	public abstract String getSYJJNQK();

	public abstract void setSYJJNQK(String sYJJNQK);

	public abstract String getZHT();

	public abstract void setZHT(String zHT);

	public abstract String getZT();

	public abstract void setZT(String zT);

	public abstract String getYXBZ();

	public abstract void setYXBZ(String yXBZ);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract String getHCJS();

	public abstract void setHCJS(String hCJS);

	public abstract String getHCR();

	public abstract void setHCR(String hCR);

	public abstract Date getHCRQ();

	public abstract void setHCRQ(Date hCRQ);

	public abstract String getCLJS();

	public abstract void setCLJS(String cLJS);

	public abstract String getCLR();

	public abstract void setCLR(String cLR);

	public abstract Date getCLRQ();

	public abstract void setCLRQ(Date cLRQ);

	public abstract String getSHYJ();

	public abstract void setSHYJ(String sHYJ);

	public abstract String getSHR();

	public abstract void setSHR(String sHR);

	public abstract Date getSHRQ();

	public abstract void setSHRQ(Date sHRQ);

	public abstract String getBZ();

	public abstract void setBZ(String bZ);
	
	public abstract String getSYRQZD();

	public abstract void setSYRQZD(String SYRQZD);
	
	public abstract String getSYRQZX();

	public abstract void setSYRQZX(String sYRQZX);
	
	public abstract String getSYRQZN();

	public abstract void setSYRQZN(String sYRQZN);
	
	public abstract String getSYRQZB();

	public abstract void setSYRQZB(String sYRQZB);
	
	public abstract String getQDFS();

	public abstract void setQDFS(String qDFS);
	
	public abstract String getQDFSName();
	
	public abstract String getGZWLX();

	public abstract void setGZWLX(String gZWLX);
	
	public abstract String getGZWLXName();

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);

	public abstract String getDJZT();

	public abstract void setDJZT(String dJZT);
	
	public abstract List<YHZK> getYHZKS();

	public abstract void setYHZKS(List<YHZK> yHZKS);
	
	public abstract List<YHYDZB> getYHYDZBS();

	public abstract void setYHYDZBS(List<YHYDZB> yHYDZBs);
}
