package com.supermap.realestate.registration.model.xmlmodel.bdcdy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("YHZK")
public class YHZK {
    private String BDCDYID;
    private String ZHDM;
    private String YHFS;
    private String YHFSMC;
    private String YHMJ;
    private String JTYT;
    private String SYJSE;
    private String GLID;
    
    public String getZHDM() {
	return ZHDM;
    }

    public void setZHDM(String zHDM) {
	ZHDM = zHDM;
    }

    public String getYHFS() {
	return YHFS;
    }

    public void setYHFS(String yHFS) {
	YHFS = yHFS;
    }

    public String getYHMJ() {
	return YHMJ;
    }

    public void setYHMJ(String yHMJ) {
	YHMJ = yHMJ;
    }

    public String getJTYT() {
	return JTYT;
    }

    public void setJTYT(String jTYT) {
	JTYT = jTYT;
    }

    public String getSYJSE() {
	return SYJSE;
    }

    public void setSYJSE(String sYJSE) {
	SYJSE = sYJSE;
    }

    public String getBDCDYID() {
	return BDCDYID;
    }

    public void setBDCDYID(String bDCDYID) {
	BDCDYID = bDCDYID;
    }

    public String getYHFSMC() {
	return YHFSMC;
    }

    public void setYHFSMC(String yHFSMC) {
	YHFSMC = yHFSMC;
    }

    public String getGLID() {
    	return GLID;
        }

        public void setGLID(String gLID) {
    	GLID = gLID;
        }


}
