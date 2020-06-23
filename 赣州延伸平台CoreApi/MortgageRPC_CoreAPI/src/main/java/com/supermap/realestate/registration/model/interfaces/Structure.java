///*****************************************
//* AutoGenerate by CodeTools 2016/12/27 
//* ----------------------------------------
//* Entity Interface bdcs_gzw_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/
package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;
public interface Structure extends RealUnit {

	//要素代码
	public abstract String getYSDM();
	public abstract void setYSDM(String ySDM);

	//项目编号
	public abstract String getXMBH();
	public abstract void setXMBH(String xMBH);

	//不动产单元号
	public abstract String getBDCDYH();
	public abstract void setBDCDYH(String bDCDYH);

	//宗地单元ID
	public abstract String getZDBDCDYID();
	public abstract void setZDBDCDYID(String zDBDCDYID);

	//宗地代码
	public abstract String getZDDM();
	public abstract void setZDDM(String zDDM);

	//构筑物名称
	public abstract String getGZWMC();
	public abstract void setGZWMC(String gZWMC);

	//坐落
	public abstract String getZL();
	public abstract void setZL(String zL);

	//土地海域使用权人
	public abstract String getTDHYSYQR();
	public abstract void setTDHYSYQR(String tDHYSYQR);

	//土地海域使用面积
	public abstract Double getTDHYSYMJ();
	public abstract void setTDHYSYMJ(Double tDHYSYMJ);

	//构建物类型
	public abstract String getGJZWLX();
	public abstract void setGJZWLX(String gJZWLX);

	//构建筑物规划用途
	public abstract String getGJZWGHYT();
	public abstract void setGJZWGHYT(String gJZWGHYT);

	//竣工时间
	public abstract Date getJGSJ();
	public abstract void setJGSJ(Date jGSJ);

	//面积单位
	public abstract String getMJDW();
	public abstract void setMJDW(String mJDW);

	//面积
	public abstract double getMJ();
	public abstract void setMJ(double mJ);

	//状态
	public abstract String getZT();
	public abstract void setZT(String zT);

	//??????
	public abstract String getDCXMID();
	public abstract void setDCXMID(String dCXMID);

	//有效标志
	public abstract String getYXBZ();
	public abstract void setYXBZ(String yXBZ);

	//CREATETIME
	public abstract Date getCREATETIME();
	public abstract void setCREATETIME(Date cREATETIME);

	//MODIFYTIME
	public abstract Date getMODIFYTIME();
	public abstract void setMODIFYTIME(Date mODIFYTIME);

	//业务号
	public abstract String getYWH();
	public abstract void setYWH(String yWH);

	//权利类型
	public abstract String getQLLX();
	public abstract void setQLLX(String qLLX);

	//构(建)筑物面积
	public abstract Double getGJZWMJ();
	public abstract void setGJZWMJ(Double gJZWMJ);

	//区县代码
	public abstract String getQXDM();
	public abstract void setQXDM(String qXDM);

	//构（建）筑物平面图
	public abstract byte[] getGJZWPMT();
	public abstract void setGJZWPMT(byte[] gJZWPMT);

	//权属状态
	public abstract String getQSZT();
	public abstract void setQSZT(String qSZT);
}
