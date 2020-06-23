package com.supermap.realestate.registration.model.xmlmodel.bdcdy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("GZW")
public class GZW {
    private String BDCDYID;// 不动产单元ID
    private String BDCDYH;
    private String ZDDM;
    private String GZWMC;
    private String ZL;
    private String MJDW;
    private String MJDWMC;// 面积单位名称
    private String MJ;
    private String ZT;
    private String ZTMC;// 状态名称
    private String GLID;
    
    public String getBDCDYID() {
	return BDCDYID;
    }

    public void setBDCDYID(String bDCDYID) {
	BDCDYID = bDCDYID;
    }

    public String getMJDWMC() {
	return MJDWMC;
    }

    public void setMJDWMC(String mJDWMC) {
	MJDWMC = mJDWMC;
    }

    public String getZTMC() {
	return ZTMC;
    }

    public void setZTMC(String zTMC) {
	ZTMC = zTMC;
    }

    public String getBDCDYH() {
	return BDCDYH;
    }

    public void setBDCDYH(String bDCDYH) {
	BDCDYH = bDCDYH;
    }

    public String getZDDM() {
	return ZDDM;
    }

    public void setZDDM(String zDDM) {
	ZDDM = zDDM;
    }

    public String getGZWMC() {
	return GZWMC;
    }

    public void setGZWMC(String gZWMC) {
	GZWMC = gZWMC;
    }

    public String getZL() {
	return ZL;
    }

    public void setZL(String zL) {
	ZL = zL;
    }

    public String getMJDW() {
	return MJDW;
    }

    public void setMJDW(String mJDW) {
	MJDW = mJDW;
    }

    public String getMJ() {
	return MJ;
    }

    public void setMJ(String mJ) {
	MJ = mJ;
    }

    public String getZT() {
	return ZT;
    }

    public void setZT(String zT) {
	ZT = zT;
    }

    public String getGLID() {
    	return GLID;
        }

        public void setGLID(String gLID) {
    	GLID = gLID;
        }


}
