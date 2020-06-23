package com.supermap.realestate.registration.model.xmlmodel.bdcdy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DZDZW")
public class DZDZW {
    private String BDCDYID;// 不动产单元ID
    private String BDCDYH;
    private String ZDZHDM;
    private String MZDZWLX;
    private String DZWMC;
    private String MJDW;
    private String MJDWMC;// 面积单位名称
    private String MJ;
    private String ZT;
    private String ZTMC;// 状态名称
    private String DZDZWLX;// 
    private String GLID;
    
    public String getDZDZWLX() {
        return DZDZWLX;
    }

    public void setDZDZWLX(String dZDZWLX) {
        DZDZWLX = dZDZWLX;
    }

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

    public String getZDZHDM() {
	return ZDZHDM;
    }

    public void setZDZHDM(String zDZHDM) {
	ZDZHDM = zDZHDM;
    }

    public String getMZDZWLX() {
	return MZDZWLX;
    }

    public void setMZDZWLX(String mZDZWLX) {
	MZDZWLX = mZDZWLX;
    }

    public String getDZWMC() {
	return DZWMC;
    }

    public void setDZWMC(String dZWMC) {
	DZWMC = dZWMC;
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
