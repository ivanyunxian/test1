package com.supermap.realestate.registration.model.xmlmodel.bdcql;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("YYDJ")
public class YYDJ {
    private String BDCDYID;//不动产单元ID
    private String QLID;//权利ID
    private String BDCDYH;//不动产单元号
    private String YWH;//业务号
    private String YYSX;//异议事项
    private String BDCDJZMH;//不动产登记证明号
    private String ZXYYYWH;//注销异议业务号
    private String ZXYYYY;//注销异议原因
    private String ZXYYDBR;//注销异议登簿人
    private String ZXYYDJSJ;//注销异议登记时间
    private String FJ;//附记
    private String QSZT;//权属状态
    private String QSZTMC;//权属状态名称
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
    public String getYYSX() {
        return YYSX;
    }
    public void setYYSX(String yYSX) {
        YYSX = yYSX;
    }
    public String getBDCDJZMH() {
        return BDCDJZMH;
    }
    public void setBDCDJZMH(String bDCDJZMH) {
        BDCDJZMH = bDCDJZMH;
    }
    public String getZXYYYWH() {
        return ZXYYYWH;
    }
    public void setZXYYYWH(String zXYYYWH) {
        ZXYYYWH = zXYYYWH;
    }
    public String getZXYYYY() {
        return ZXYYYY;
    }
    public void setZXYYYY(String zXYYYY) {
        ZXYYYY = zXYYYY;
    }
    public String getZXYYDBR() {
        return ZXYYDBR;
    }
    public void setZXYYDBR(String zXYYDBR) {
        ZXYYDBR = zXYYDBR;
    }
    public String getZXYYDJSJ() {
        return ZXYYDJSJ;
    }
    public void setZXYYDJSJ(String zXYYDJSJ) {
        ZXYYDJSJ = zXYYDJSJ;
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
