package com.supermap.realestate.registration.model.xmlmodel.bdcdy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ZHBHQK")
public class ZHBHQK {
    private String BDCDYID;
    private String ZHDM;
    private String BHYY;
    private String BHNR;
    private String DJSJ;
    private String DBR;
    private String GLID;
    
    public String getBDCDYID() {
        return BDCDYID;
    }

    public void setBDCDYID(String bDCDYID) {
        BDCDYID = bDCDYID;
    }

    public String getZHDM() {
	return ZHDM;
    }

    public void setZHDM(String zHDM) {
	ZHDM = zHDM;
    }

    public String getBHYY() {
	return BHYY;
    }

    public void setBHYY(String bHYY) {
	BHYY = bHYY;
    }

    public String getBHNR() {
	return BHNR;
    }

    public void setBHNR(String bHNR) {
	BHNR = bHNR;
    }

    public String getDJSJ() {
	return DJSJ;
    }

    public void setDJSJ(String dJSJ) {
	DJSJ = dJSJ;
    }

    public String getDBR() {
	return DBR;
    }

    public void setDBR(String dBR) {
	DBR = dBR;
    }

    public String getGLID() {
    	return GLID;
        }

        public void setGLID(String gLID) {
    	GLID = gLID;
        }


}
