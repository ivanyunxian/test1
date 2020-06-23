package com.supermap.realestate.registration.model;

import javax.persistence.*;

/**
 * ClassName : MaxNoticeSn
 * <p>
 * Description :
 * </p>
 *
 * @author YuGuowei
 * @date 2017-03-29 19:16
 **/
@Entity
@Table(name = "MAXNOTICESN", schema = "BDCK")
public class MaxNoticeSn {
    private Long bsm;
    private String xzqhdm;
    private String nd;
    private Integer xh;

    @Id
    @Column(name = "BSM")
    public Long getBsm() {
        return bsm;
    }

    public void setBsm(Long bsm) {
        this.bsm = bsm;
    }

    @Basic
    @Column(name = "XZQHDM")
    public String getXzqhdm() {
        return xzqhdm;
    }

    public void setXzqhdm(String xzqhdm) {
        this.xzqhdm = xzqhdm;
    }

    @Basic
    @Column(name = "ND")
    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    @Basic
    @Column(name = "XH")
    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaxNoticeSn that = (MaxNoticeSn) o;

        if (bsm != null ? !bsm.equals(that.bsm) : that.bsm != null) return false;
        if (xzqhdm != null ? !xzqhdm.equals(that.xzqhdm) : that.xzqhdm != null) return false;
        if (nd != null ? !nd.equals(that.nd) : that.nd != null) return false;
        if (xh != null ? !xh.equals(that.xh) : that.xh != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bsm != null ? bsm.hashCode() : 0;
        result = 31 * result + (xzqhdm != null ? xzqhdm.hashCode() : 0);
        result = 31 * result + (nd != null ? nd.hashCode() : 0);
        result = 31 * result + (xh != null ? xh.hashCode() : 0);
        return result;
    }
}
