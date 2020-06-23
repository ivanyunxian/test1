package com.supermap.realestate.registration.model.xmlmodel;

import java.util.List;

import com.supermap.realestate.registration.model.xmlmodel.bdcdy.BDCDY;
import com.supermap.realestate.registration.model.xmlmodel.bdcql.BDCQL;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Data")
public class Data {
    private HTNR HTNR;
    private BDCDY BDCDY;
    private BDCQL BDCQL;
    @XStreamAlias("BDCQLRS")
    private List<BDCQLR> BDCQLRS;
    @XStreamAlias("SFRS")
	private List<SFR> SFRS;
    public HTNR getHTNR() {
        return HTNR;
    }
    public void setHTNR(HTNR hTNR) {
        HTNR = hTNR;
    }
    public BDCDY getBDCDY() {
        return BDCDY;
    }
    public void setBDCDY(BDCDY bDCDY) {
        BDCDY = bDCDY;
    }
    public BDCQL getBDCQL() {
        return BDCQL;
    }
    public void setBDCQL(BDCQL bDCQL) {
        BDCQL = bDCQL;
    }
    public List<BDCQLR> getBDCQLRS() {
        return BDCQLRS;
    }
    public void setBDCQLRS(List<BDCQLR> bDCQLRS) {
        BDCQLRS = bDCQLRS;
    }
    public List<SFR> getSFRS() {
        return SFRS;
    }
    public void setSFRS(List<SFR> sFRS) {
        SFRS = sFRS;
    }

}
