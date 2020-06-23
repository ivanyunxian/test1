/**   
 * 使用权宗地
 * @Title: UseLand.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午10:36:13 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

/**
 * 使用权宗地
 * @ClassName: UseLand
 * @author liushufeng
 * @date 2015年7月11日 下午10:36:13
 */
public interface UseLand extends RealUnit {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getBDCDYH();

	public abstract void setBDCDYH(String bDCDYH);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	public abstract String getZDDM();

	public abstract void setZDDM(String zDDM);

	public abstract String getZDTZM();

	public abstract void setZDTZM(String zDTZM);

	public abstract String getZL();

	public abstract void setZL(String zL);

	public abstract Double getZDMJ();

	public abstract void setZDMJ(Double zDMJ);

	public abstract String getMJDW();

	public abstract void setMJDW(String mJDW);

	public abstract String getYT();

	public abstract void setYT(String yT);

	public abstract String getDJ();

	public abstract void setDJ(String dJ);

	public abstract Double getJG();

	public abstract void setJG(Double jG);

	public abstract String getQLLX();

	public abstract void setQLLX(String qLLX);

	public abstract String getQLXZ();

	public abstract void setQLXZ(String qLXZ);

	public abstract String getQLSDFS();

	public abstract void setQLSDFS(String qLSDFS);

	public abstract String getRJL();

	public abstract void setRJL(String rJL);

	public abstract String getJZMD();

	public abstract void setJZMD(String jZMD);

	public abstract Double getJZXG();

	public abstract void setJZXG(Double jZXG);

	public abstract String getZDSZD();

	public abstract void setZDSZD(String zDSZD);

	public abstract String getZDSZX();

	public abstract void setZDSZX(String zDSZX);

	public abstract String getZDSZN();

	public abstract void setZDSZN(String zDSZN);

	public abstract String getZDSZB();

	public abstract void setZDSZB(String zDSZB);

	public abstract String getZDT();

	public abstract void setZDT(String zDT);

	public abstract String getTFH();

	public abstract void setTFH(String tFH);

	public abstract String getDJH();

	public abstract void setDJH(String dJH);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract String getQXDM();

	public abstract void setQXDM(String qXDM);

	public abstract String getDJQDM();

	public abstract void setDJQDM(String dJQDM);

	public abstract String getDJZQDM();

	public abstract void setDJZQDM(String dJZQDM);

	public abstract String getZH();

	public abstract void setZH(String zH);

	public abstract String getQXMC();

	public abstract void setQXMC(String qXMC);

	public abstract String getDJQMC();

	public abstract void setDJQMC(String dJQMC);

	public abstract String getDJZQMC();

	public abstract void setDJZQMC(String dJZQMC);

	public abstract String getZM();

	public abstract void setZM(String zM);

	public abstract String getSYQLX();

	public abstract void setSYQLX(String sYQLX);

	public abstract String getTDQSLYZMCL();

	public abstract void setTDQSLYZMCL(String tDQSLYZMCL);

	public abstract String getGMJJHYFLDM();

	public abstract void setGMJJHYFLDM(String gMJJHYFLDM);

	public abstract String getYBZDDM();

	public abstract void setYBZDDM(String yBZDDM);

	public abstract String getBLC();

	public abstract void setBLC(String bLC);

	public abstract String getPZYT();

	public abstract void setPZYT(String pZYT);

	public abstract Double getPZMJ();

	public abstract void setPZMJ(Double pZMJ);

	public abstract Double getJZZDMJ();

	public abstract void setJZZDMJ(Double jZZDMJ);

	public abstract Double getJZMJ();

	public abstract void setJZMJ(Double jZMJ);

	public abstract String getZT();

	public abstract void setZT(String zT);

	public abstract String getCQZT();

	public abstract void setCQZT(String cQZT);

	public abstract String getDYZT();

	public abstract void setDYZT(String dYZT);

	public abstract String getXZZT();

	public abstract void setXZZT(String xZZT);

	public abstract String getBLZT();

	public abstract void setBLZT(String bLZT);

	public abstract String getYYZT();

	public abstract void setYYZT(String yYZT);

	public abstract String getJZDWSM();

	public abstract void setJZDWSM(String jZDWSM);

	public abstract String getJZXZXSM();

	public abstract void setJZXZXSM(String jZXZXSM);

	public abstract String getDCJS();

	public abstract void setDCJS(String dCJS);

	public abstract String getDCR();

	public abstract void setDCR(String dCR);

	public abstract Date getDCRQ();

	public abstract void setDCRQ(Date dCRQ);

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

	public abstract String getYXBZ();

	public abstract void setYXBZ(String yXBZ);

	public abstract String getBGZT();

	public abstract void setBGZT(String bGZT);

	public abstract String getDJZT();

	public abstract void setDJZT(String dJZT);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);

	public abstract List<TDYT> getTDYTS();

	public abstract void setTDYTS(List<TDYT> tDYTS);
	
	public abstract String getZDBDCQZH();

	public abstract void setZDBDCQZH(String v_zdbdcqzh);
	
	public String getGLZRZID();
	public void setGLZRZID(String gLZRZID);
	
	public abstract String getSEARCHSTATE();
	public abstract void setSEARCHSTATE(String sEARCHSTATE);
	
	public abstract String getTXWHTYPE();
	public abstract void setTXWHTYPE(String tXWHTYPE);
}
