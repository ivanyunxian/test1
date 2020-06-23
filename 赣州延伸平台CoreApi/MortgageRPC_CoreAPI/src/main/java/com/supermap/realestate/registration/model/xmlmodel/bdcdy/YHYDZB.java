package com.supermap.realestate.registration.model.xmlmodel.bdcdy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("YHYDZB")
public class YHYDZB {
    private String BDCDYID;//不动产单元ID
    private String ZHHDDM;
    private String GLID;
    
    public String getBDCDYID() {
        return BDCDYID;
    }

    public void setBDCDYID(String bDCDYID) {
        BDCDYID = bDCDYID;
    }

    public String getZHHDDM() {
	return ZHHDDM;
    }

    public void setZHHDDM(String zHHDDM) {
	ZHHDDM = zHHDDM;
    }

    public String getXH() {
	return XH;
    }

    public void setXH(String xH) {
	XH = xH;
    }

    public String getBW() {
	return BW;
    }

    public void setBW(String bW) {
	BW = bW;
    }

    public String getDJ() {
	return DJ;
    }

    public void setDJ(String dJ) {
	DJ = dJ;
    }

    public String getGLID() {
    	return GLID;
        }

        public void setGLID(String gLID) {
    	GLID = gLID;
        }


    private String XH;
    private String BW;
    private String DJ;
}
