/**   
 * 森林林木接口
 * @Title: Forest.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午9:44:56 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;
import java.util.List;

/**
 * 森林林木接口
 * @ClassName: Forest
 * @author liushufeng
 * @date 2015年7月11日 下午9:44:56
 */
public interface Forest extends LandAttach {
	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getBDCDYH();

	public abstract void setBDCDYH(String bDCDYH);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	public abstract String getZDBDCDYID();

	public abstract void setZDBDCDYID(String zDBDCDYID);

	public abstract String getZDDM();

	public abstract void setZDDM(String zDDM);

	public abstract String getZL();

	public abstract void setZL(String zL);

	public abstract String getZYSZ();

	public abstract void setZYSZ(String zYSZ);

	public abstract Integer getZS();

	public abstract void setZS(Integer zS);

	public abstract String getLZ();

	public abstract void setLZ(String lZ);

	public abstract String getQY();

	public abstract void setQY(String qY);

	public abstract Integer getZLND();

	public abstract void setZLND(Integer zLND);

	public abstract String getLB();

	public abstract void setLB(String lB);

	public abstract String getXB();

	public abstract void setXB(String xB);

	public abstract String getXDM();

	public abstract void setXDM(String xDM);

	public abstract Double getSYQMJ();

	public abstract void setSYQMJ(Double sYQMJ);

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

	public abstract String getZT();

	public abstract void setZT(String zT);

	public abstract String getYXBZ();

	public abstract void setYXBZ(String yXBZ);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract String getDCJS();

	public abstract void setDCJS(String dCJS);

	public abstract String getDCR();

	public abstract void setDCR(String dCR);

	public abstract Date getDCRQ();

	public abstract void setDCRQ(Date dCRQ);

	public abstract String getSHYJ();

	public abstract void setSHYJ(String sHYJ);

	public abstract String getSHR();

	public abstract void setSHR(String sHR);

	public abstract Date getSHRQ();

	public abstract void setSHRQ(Date sHRQ);

	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);

	public abstract String getSYQLX();

	public abstract void setSYQLX(String sYQLX);
	
	public abstract String getZDTZM();

	public abstract void setZDTZM(String zDTZM);
	
	public abstract String getQLXZ();

	public abstract void setQLXZ(String qLXZ);
	
	public abstract String getZDSZD();

	public abstract void setZDSZD(String zDSZD);
	
	public abstract String getZDSZN();

	public abstract void setZDSZN(String zDSZN);
	
	public abstract String getZDSZX();

	public abstract void setZDSZX(String zDSZX);
	
	public abstract String getZDSZB();

	public abstract void setZDSZB(String zDSZB);
	
	public abstract String getTFH();

	public abstract void setTFH(String tFH);
	
	public abstract String getTDYT();

	public abstract void setTDYT(String tDYT);
	
	public abstract List<TDYT> getTDYTS();

	public abstract void setTDYTS(List<TDYT> tDYTS);
}
