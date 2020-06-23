/**   
 * 所有权宗地不动产单元接口
 * @Title: OwnerLand.java 
 * @Package com.supermap.realestate.registration.model.interfaces.newer 
 * @author liushufeng 
 * @date 2015年7月11日 下午5:08:36 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;
import java.util.List;

import com.supermap.realestate.registration.model.interfaces.RealUnit;

/**
 * 农用地不动产单元接口
 * 
 * @ClassName: AgriculturalLand
 * @author ranquan
 * @date 2016年9月21日
 */
public interface AgriculturalLand extends RealUnit {

	public String getId();

	public void setId(String id);

	public String getYSDM();

	public void setYSDM(String ySDM);

	public String getZDDM();

	public void setZDDM(String zDDM);

	public String getBDCDYH();

	public void setBDCDYH(String bDCDYH);

	public String getZDTZM();

	public void setZDTZM(String zDTZM);

	public String getZL();

	public void setZL(String zL);

	public String getYT();

	public void setYT(String yT);

	public String getQLLX();

	public void setQLLX(String qLLX);

	public String getQLXZ();

	public void setQLXZ(String qLXZ);

	public String getSYQLX();

	public void setSYQLX(String sYQLX);

	public String getZDSZD();

	public void setZDSZD(String zDSZD);

	public String getZDSZN();

	public void setZDSZN(String zDSZN);

	public String getZDSZX();

	public void setZDSZX(String zDSZX);

	public String getZDSZB();

	public void setZDSZB(String zDSZB);

	public String getZDT();

	public void setZDT(String zDT);

	public String getTFH();

	public void setTFH(String tFH);

	public String getBZ();

	public void setBZ(String bZ);

	public String getZT();

	public void setZT(String zT);

	public String getQXDM();

	public void setQXDM(String qXDM);

	public String getQXMC();

	public void setQXMC(String qXMC);

	public String getDJQDM();

	public void setDJQDM(String dJQDM);

	public String getDJQMC();

	public void setDJQMC(String dJQMC);

	public String getDJZQDM();

	public void setDJZQDM(String dJZQDM);

	public String getDJZQMC();

	public void setDJZQMC(String dJZQMC);

	public String getZH();

	public void setZH(String zH);

	public String getZM();

	public void setZM(String zM);

	public String getFBFDM();

	public void setFBFDM(String fBFDM);

	public String getFBFMC();

	public void setFBFMC(String fBFMC);

	public Double getCBMJ();

	public void setCBMJ(Double cBMJ);

	public Date getCBQSSJ();

	public void setCBQSSJ(Date cBQSSJ);

	public Date getCBJSSJ();

	public void setCBJSSJ(Date cBJSSJ);

	public String getTDSYQXZ();

	public void setTDSYQXZ(String tDSYQXZ);

	public String getSYTTLX();

	public void setSYTTLX(String sYTTLX);

	public String getYZYFS();

	public void setYZYFS(String yZYFS);

	public String getCYZL();

	public void setCYZL(String cYZL);

	public Integer getSYZCL();

	public void setSYZCL(Integer sYZCL);
	public abstract List<TDYT> getTDYTS();

	public abstract void setTDYTS(List<TDYT> tDYTS);
	
	public abstract String getZDBDCQZH();

	public abstract void setZDBDCQZH(String v_zdbdcqzh);

}
