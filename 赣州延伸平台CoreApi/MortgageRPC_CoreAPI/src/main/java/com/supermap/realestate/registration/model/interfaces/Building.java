/**   
 * 自然幢不动产单元接口
 * @Title: Building.java 
 * @Package com.supermap.realestate.registration.model.interfaces.newer 
 * @author liushufeng 
 * @date 2015年7月11日 下午4:58:10 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

import javax.persistence.Column;

/**
 * 自然幢不动产单元接口
 * @ClassName: Building
 * @author liushufeng
 * @date 2015年7月11日 下午4:58:10
 */
public interface Building extends LandAttach {
	// 要素代码
	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	// 项目名称
	public abstract String getXMMC();

	public abstract void setXMMC(String xMMC);

	// 不动产单元号
	public abstract String getBDCDYH();

	public abstract void setBDCDYH(String bDCDYH);

	// 项目编号
	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	// 宗地代码
	public abstract String getZDDM();

	public abstract void setZDDM(String zDDM);

	// 宗地单元ID
	public abstract String getZDBDCDYID();

	public abstract void setZDBDCDYID(String zDBDCDYID);

	// 自然幢号
	public abstract String getZRZH();

	public abstract void setZRZH(String zRZH);

	// 坐落
	public abstract String getZL();

	public abstract void setZL(String zL);

	// 建筑物名称
	public abstract String getJZWMC();

	public abstract void setJZWMC(String jZWMC);

	// 竣工日期
	public abstract Date getJGRQ();

	public abstract void setJGRQ(Date jGRQ);

	// 建筑物高度
	public abstract Double getJZWGD();

	public abstract void setJZWGD(Double jZWGD);

	// 幢占地面积
	public abstract Double getZZDMJ();

	public abstract void setZZDMJ(Double zZDMJ);

	// 幢用地面积
	public abstract Double getZYDMJ();

	public abstract void setZYDMJ(Double zYDMJ);

	// 预测建筑面积
	public abstract Double getYCJZMJ();

	public abstract void setYCJZMJ(Double yCJZMJ);

	// 实测建筑面积
	public abstract Double getSCJZMJ();

	public abstract void setSCJZMJ(Double sCJZMJ);

	// 土地使用权人
	public abstract String getTDSYQR();

	public abstract void setTDSYQR(String tDSYQR);

	// 独用土地面积
	public abstract Double getDYTDMJ();

	public abstract void setDYTDMJ(Double dYTDMJ);

	// 分摊土地面积
	public abstract Double getFTTDMJ();

	public abstract void setFTTDMJ(Double fTTDMJ);

	// 房地产交易价格
	public abstract Double getFDCJYJG();

	public abstract void setFDCJYJG(Double fDCJYJG);

	// 总层数
	public abstract Integer getZCS();

	public abstract void setZCS(Integer zCS);

	// 地上层数
	public abstract Integer getDSCS();

	public abstract void setDSCS(Integer dSCS);

	// 地下层数
	public abstract Integer getDXCS();

	public abstract void setDXCS(Integer dXCS);

	// 地下深度
	public abstract Double getDXSD();

	public abstract void setDXSD(Double dXSD);

	// A.17房屋用途字典表
	public abstract String getGHYT();

	public abstract void setGHYT(String gHYT);

	// A.46房屋结构字典表
	public abstract String getFWJG();

	public abstract void setFWJG(String fWJG);

	// 总套数
	public abstract Integer getZTS();

	public abstract void setZTS(Integer zTS);

	// 建筑物基本用途
	public abstract String getJZWJBYT();

	public abstract void setJZWJBYT(String jZWJBYT);

	// 备注
	public abstract String getBZ();

	public abstract void setBZ(String bZ);

	// 状态
	public abstract String getZT();

	public abstract void setZT(String zT);

	// 区县代码
	public abstract String getQXDM();

	public abstract void setQXDM(String qXDM);

	// 区县名称
	public abstract String getQXMC();

	public abstract void setQXMC(String qXMC);

	// 地籍区
	public abstract String getDJQDM();

	public abstract void setDJQDM(String dJQDM);

	// 地籍区名称
	public abstract String getDJQMC();

	public abstract void setDJQMC(String dJQMC);

	// 地籍子区
	public abstract String getDJZQDM();

	public abstract void setDJZQDM(String dJZQDM);

	// 地籍子区名称
	public abstract String getDJZQMC();

	public abstract void setDJZQMC(String dJZQMC);

	// 调查项目编号
	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	// 有效标志
	public abstract String getYXBZ();

	public abstract void setYXBZ(String yXBZ);

	// A.63 登记状态字典表，登记中，已登记，未登记%DJZT%
	public abstract String getDJZT();

	public abstract void setDJZT(String dJZT);

	public String getDJZTName();

	// 记录创建时间
	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	// 记录修改时间
	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);
	
	public abstract String getNBDCDYH();
	public abstract void setNBDCDYH(String nBDCDYH);
	
	public abstract String getNZDBDCDYID();
	public abstract void setNZDBDCDYID(String nZDBDCDYID);
	
	public abstract String getKFSMC();
	public abstract void setKFSMC(String kFSMC);
	
	public abstract String getKFSZJH();
	public abstract void setKFSZJH(String kFSZJH);	
	

}
