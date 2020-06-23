package com.supermap.realestate.registration.model.xmlmodel.bdcql;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FDCQ3")
public class FDCQ3 {
	private String BDCDYID;//不动产单元ID
	private String QLID;//权利ID
	private String BDCDYH;//不动产单元号
	private String YWH;//业务号
	private String QLLX;//权利类型
	private String QLLXMC;//权利类型名称
	private String JGZWBH;//建筑物编号
	private String JGZWMC;//建筑物名称
	private String JGZWSL;//建筑物数量
	private String JGZWMJ;//建筑物面积
	private String FTTDMJ;//分摊土地面积
	private String FJ;//附记
	private String QSZT;//权属状态
	private String QSZTMC;//权属状态名称

	public String getQLLXMC() {
		return QLLXMC;
	}

	public void setQLLXMC(String qLLXMC) {
		QLLXMC = qLLXMC;
	}

	public String getQSZTMC() {
		return QSZTMC;
	}

	public void setQSZTMC(String qSZTMC) {
		QSZTMC = qSZTMC;
	}

	public String getBDCDYID() {
		return BDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		BDCDYID = bDCDYID;
	}

	public String getQLID() {
		return QLID;
	}

	public void setQLID(String qLID) {
		QLID = qLID;
	}

	public String getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}

	public String getYWH() {
		return YWH;
	}

	public void setYWH(String yWH) {
		YWH = yWH;
	}

	public String getQLLX() {
		return QLLX;
	}

	public void setQLLX(String qLLX) {
		QLLX = qLLX;
	}

	public String getJGZWBH() {
		return JGZWBH;
	}

	public void setJGZWBH(String jGZWBH) {
		JGZWBH = jGZWBH;
	}

	public String getJGZWMC() {
		return JGZWMC;
	}

	public void setJGZWMC(String jGZWMC) {
		JGZWMC = jGZWMC;
	}

	public String getJGZWSL() {
		return JGZWSL;
	}

	public void setJGZWSL(String jGZWSL) {
		JGZWSL = jGZWSL;
	}

	public String getJGZWMJ() {
		return JGZWMJ;
	}

	public void setJGZWMJ(String jGZWMJ) {
		JGZWMJ = jGZWMJ;
	}

	public String getFTTDMJ() {
		return FTTDMJ;
	}

	public void setFTTDMJ(String fTTDMJ) {
		FTTDMJ = fTTDMJ;
	}

	public String getFJ() {
		return FJ;
	}

	public void setFJ(String fJ) {
		FJ = fJ;
	}

	public String getQSZT() {
		return QSZT;
	}

	public void setQSZT(String qSZT) {
		QSZT = qSZT;
	}
}
