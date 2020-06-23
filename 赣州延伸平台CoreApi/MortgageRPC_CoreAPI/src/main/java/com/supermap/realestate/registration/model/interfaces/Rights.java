/**   
 * 权利接口
 * @Title: Rights.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午10:31:43 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

/**
 * 权利接口
 * @ClassName: Rights
 * @author liushufeng
 * @date 2015年7月11日 下午10:31:43
 */
public interface Rights {
	abstract String getId();

	abstract void setId(String id);

	abstract String getDJDYID();

	abstract void setDJDYID(String dJDYID);

	abstract String getBDCDYH();

	abstract void setBDCDYH(String bDCDYH);

	abstract String getXMBH();

	abstract void setXMBH(String xMBH);

	abstract String getFSQLID();

	abstract void setFSQLID(String fSQLID);

	abstract String getYWH();

	abstract void setYWH(String yWH);

	abstract String getQLLX();

	abstract void setQLLX(String qLLX);

	abstract String getDJLX();

	abstract void setDJLX(String dJLX);

	abstract String getDJYY();

	abstract void setDJYY(String dJYY);

	abstract Date getQLQSSJ();

	abstract void setQLQSSJ(Date qLQSSJ);

	abstract Date getQLJSSJ();

	abstract void setQLJSSJ(Date qLJSSJ);

	abstract String getBDCQZH();

	abstract void setBDCQZH(String bDCQZH);

	abstract String getQXDM();

	abstract void setQXDM(String qXDM);

	abstract String getDJJG();

	abstract void setDJJG(String dJJG);

	abstract String getDBR();

	abstract void setDBR(String dBR);

	abstract Date getDJSJ();

	abstract void setDJSJ(Date dJSJ);

	abstract String getFJ();

	abstract void setFJ(String fJ);

	abstract Integer getQSZT();

	abstract void setQSZT(Integer qSZT);

	abstract String getQLLXMC();

	abstract void setQLLXMC(String qLLXMC);

	abstract Byte[] getZSEWM();

	abstract void setZSEWM(Byte[] zSEWM);

	abstract String getZSBH();

	abstract void setZSBH(String zSBH);

	abstract String getZT();

	abstract void setZT(String zT);

	abstract String getCZFS();

	abstract void setCZFS(String cZFS);

	abstract String getZSBS();

	abstract void setZSBS(String zSBS);

	abstract String getYXBZ();

	abstract void setYXBZ(String yXBZ);

	abstract String getDCXMID();

	abstract void setDCXMID(String dCXMID);

	abstract Date getCREATETIME();

	abstract void setCREATETIME(Date cREATETIME);

	abstract Date getMODIFYTIME();

	abstract void setMODIFYTIME(Date mODIFYTIME);

	abstract Double getQDJG();

	abstract void setQDJG(Double qDJG);

	abstract String getLYQLID();

	abstract void setLYQLID(String lYQLID);

	abstract String getDJZT();

	abstract void setDJZT(String dJZT);
	
	abstract String getTDSHYQR();

	abstract void setTDSHYQR(String dJZT);
	
	abstract String getCASENUM();

	abstract void setCASENUM(String cASENUM);
	
	abstract String getISCANCEL();

	abstract void setISCANCEL(String iSCANCEL);
	
	abstract String getHTH();
	
	abstract void setHTH(String hTH);
	
	abstract String getMSR();
	
	abstract void setMSR(String mSR);

	abstract void setISPARTIAL(String iSPARTIAL);
	
	abstract String getISPARTIAL();
	
	abstract String getARCHIVES_BOOKNO();
		
	abstract void setARCHIVES_BOOKNO(String aRCHIVES_BOOKNO);
	
	abstract String getMAINQLID();
	
	abstract String getQS();
	
	abstract void setQS(String qS);
	
	abstract String getBDCDYID();
	
	abstract void setBDCDYID(String bDCDYID);
	abstract void setGROUPID(Integer gROUPID);
	abstract Integer getGROUPID();
	
	abstract String getGYRQK();
	abstract void setGYRQK(String gYRQK);
}
