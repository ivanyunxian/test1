package com.supermap.realestate.registration.model.xmlmodel.bdcdy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ZDBHQK")
public class ZDBHQK {
    private String BDCDYID;// 不动产单元ID
    private String GLID;
    
    public String getBDCDYID() {
	return BDCDYID;
    }

    public void setBDCDYID(String bDCDYID) {
	BDCDYID = bDCDYID;
    }

    private String ZDDM;

    public String getZDDM() {
	return ZDDM;
    }

    public void setZDDM(String zDDM) {
	ZDDM = zDDM;
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

    public String getFJ() {
	return FJ;
    }

    public void setFJ(String fJ) {
	FJ = fJ;
    }
    
    public String getGLID() {
    	return GLID;
        }

        public void setGLID(String gLID) {
    	GLID = gLID;
        }



    private String BHYY;
    private String BHNR;
    private String DJSJ;
    private String DBR;
    private String FJ;
}
