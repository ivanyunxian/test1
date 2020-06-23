package com.supermap.realestate.registration.model.xmlmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HTNR")
public class HTNR {
    
    private String HTH;// 合同号
    private String QDSJ;// 签订时间
    private String BASJ;// 备案时间
    private String JYSJ;// 交易时间
    private String HTZT;// 合同状态
    
    public String getHTH() {
        return HTH;
    }
    public void setHTH(String hTH) {
        HTH = hTH;
    }
    public String getQDSJ() {
        return QDSJ;
    }
    public void setQDSJ(String qDSJ) {
        QDSJ = qDSJ;
    }
    public String getBASJ() {
        return BASJ;
    }
    public void setBASJ(String bASJ) {
        BASJ = bASJ;
    }
    public String getJYSJ() {
        return JYSJ;
    }
    public void setJYSJ(String jYSJ) {
        JYSJ = jYSJ;
    }
    public String getHTZT() {
        return HTZT;
    }
    public void setHTZT(String hTZT) {
        HTZT = hTZT;
    }
  
}
