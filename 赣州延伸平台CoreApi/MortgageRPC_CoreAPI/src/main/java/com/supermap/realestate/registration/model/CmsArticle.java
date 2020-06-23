package com.supermap.realestate.registration.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * ClassName : CmsArticle
 * <p>
 * Description :
 * </p>
 *
 * @author YuGuowei
 * @date 2017-03-20 10:01
 **/
public class CmsArticle implements Serializable {
    private String id;
    private String shopId;
    private String title;
    private String info;
    private String author;
    private String locate;
    private String usefor;
    private String qlr;
    private String fbjg;
    private String picurl;
    private String content;
    private String disabled;
    private Long publishat = 0L;
    private String channelid;
    private String opby;
    private Long opat = 0L;
    private String delflag;
    private String noticeNumber;
    private Date noticeStart;
    private Date noticeEnd;
    private String masterAddress;
    private String rightType;
    private String rightTypeName;
    private String estateNo;
    private Long area = 0L;
    private String correctContent;
    private String dissentpsn;
    private String proveno;
    private String ctateProno;
    private String remark;
    private String noticeType;
    private String noticeTypeName;
    private String landSeano;
    private Long isSynchro = 0L;
    private Long location = 0L;
    private String ywlsh;
    private String projectName;
    private Date slsj;
    private String noticeContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getUsefor() {
        return usefor;
    }

    public void setUsefor(String usefor) {
        this.usefor = usefor;
    }

    public String getQlr() {
        return qlr;
    }

    public void setQlr(String qlr) {
        this.qlr = qlr;
    }

    public String getFbjg() {
        return fbjg;
    }

    public void setFbjg(String fbjg) {
        this.fbjg = fbjg;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public Long getPublishat() {
        return publishat;
    }

    public void setPublishat(Long publishat) {
        this.publishat = publishat;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getOpby() {
        return opby;
    }

    public void setOpby(String opby) {
        this.opby = opby;
    }

    public Long getOpat() {
        return opat;
    }

    public void setOpat(Long opat) {
        this.opat = opat;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public Date getNoticeStart() {
        return noticeStart;
    }

    public void setNoticeStart(Date noticeStart) {
        this.noticeStart = noticeStart;
    }

    public Date getNoticeEnd() {
        return noticeEnd;
    }

    public void setNoticeEnd(Date noticeEnd) {
        this.noticeEnd = noticeEnd;
    }

    public String getMasterAddress() {
        return masterAddress;
    }

    public void setMasterAddress(String masterAddress) {
        this.masterAddress = masterAddress;
    }

    public String getRightType() {
        return rightType;
    }

    public void setRightType(String rightType) {
        this.rightType = rightType;
    }

    public String getRightTypeName() {
        return rightTypeName;
    }

    public void setRightTypeName(String rightTypeName) {
        this.rightTypeName = rightTypeName;
    }

    public String getEstateNo() {
        return estateNo;
    }

    public void setEstateNo(String estateNo) {
        this.estateNo = estateNo;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public String getCorrectContent() {
        return correctContent;
    }

    public void setCorrectContent(String correctContent) {
        this.correctContent = correctContent;
    }

    public String getDissentpsn() {
        return dissentpsn;
    }

    public void setDissentpsn(String dissentpsn) {
        this.dissentpsn = dissentpsn;
    }

    public String getProveno() {
        return proveno;
    }

    public void setProveno(String proveno) {
        this.proveno = proveno;
    }

    public String getCtateProno() {
        return ctateProno;
    }

    public void setCtateProno(String ctateProno) {
        this.ctateProno = ctateProno;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeTypeName() {
        return noticeTypeName;
    }

    public void setNoticeTypeName(String noticeTypeName) {
        this.noticeTypeName = noticeTypeName;
    }

    public String getLandSeano() {
        return landSeano;
    }

    public void setLandSeano(String landSeano) {
        this.landSeano = landSeano;
    }

    public Long getIsSynchro() {
        return isSynchro;
    }

    public void setIsSynchro(Long isSynchro) {
        this.isSynchro = isSynchro;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getSlsj() {
        return slsj;
    }

    public void setSlsj(Date slsj) {
        this.slsj = slsj;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmsArticle that = (CmsArticle) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (shopId != null ? !shopId.equals(that.shopId) : that.shopId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (info != null ? !info.equals(that.info) : that.info != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (locate != null ? !locate.equals(that.locate) : that.locate != null) return false;
        if (usefor != null ? !usefor.equals(that.usefor) : that.usefor != null) return false;
        if (qlr != null ? !qlr.equals(that.qlr) : that.qlr != null) return false;
        if (fbjg != null ? !fbjg.equals(that.fbjg) : that.fbjg != null) return false;
        if (picurl != null ? !picurl.equals(that.picurl) : that.picurl != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (disabled != null ? !disabled.equals(that.disabled) : that.disabled != null) return false;
        if (publishat != null ? !publishat.equals(that.publishat) : that.publishat != null) return false;
        if (channelid != null ? !channelid.equals(that.channelid) : that.channelid != null) return false;
        if (opby != null ? !opby.equals(that.opby) : that.opby != null) return false;
        if (opat != null ? !opat.equals(that.opat) : that.opat != null) return false;
        if (delflag != null ? !delflag.equals(that.delflag) : that.delflag != null) return false;
        if (noticeNumber != null ? !noticeNumber.equals(that.noticeNumber) : that.noticeNumber != null) return false;
        if (noticeStart != null ? !noticeStart.equals(that.noticeStart) : that.noticeStart != null) return false;
        if (noticeEnd != null ? !noticeEnd.equals(that.noticeEnd) : that.noticeEnd != null) return false;
        if (masterAddress != null ? !masterAddress.equals(that.masterAddress) : that.masterAddress != null)
            return false;
        if (rightType != null ? !rightType.equals(that.rightType) : that.rightType != null) return false;
        if (rightTypeName != null ? !rightTypeName.equals(that.rightTypeName) : that.rightTypeName != null)
            return false;
        if (estateNo != null ? !estateNo.equals(that.estateNo) : that.estateNo != null) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (correctContent != null ? !correctContent.equals(that.correctContent) : that.correctContent != null)
            return false;
        if (dissentpsn != null ? !dissentpsn.equals(that.dissentpsn) : that.dissentpsn != null) return false;
        if (proveno != null ? !proveno.equals(that.proveno) : that.proveno != null) return false;
        if (ctateProno != null ? !ctateProno.equals(that.ctateProno) : that.ctateProno != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (noticeType != null ? !noticeType.equals(that.noticeType) : that.noticeType != null) return false;
        if (noticeTypeName != null ? !noticeTypeName.equals(that.noticeTypeName) : that.noticeTypeName != null)
            return false;
        if (landSeano != null ? !landSeano.equals(that.landSeano) : that.landSeano != null) return false;
        if (isSynchro != null ? !isSynchro.equals(that.isSynchro) : that.isSynchro != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (ywlsh != null ? !ywlsh.equals(that.ywlsh) : that.ywlsh != null) return false;
        if (projectName != null ? !projectName.equals(that.projectName) : that.projectName != null) return false;
        if (slsj != null ? !slsj.equals(that.slsj) : that.slsj != null) return false;
        return noticeContent != null ? noticeContent.equals(that.noticeContent) : that.noticeContent == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (locate != null ? locate.hashCode() : 0);
        result = 31 * result + (usefor != null ? usefor.hashCode() : 0);
        result = 31 * result + (qlr != null ? qlr.hashCode() : 0);
        result = 31 * result + (fbjg != null ? fbjg.hashCode() : 0);
        result = 31 * result + (picurl != null ? picurl.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
        result = 31 * result + (publishat != null ? publishat.hashCode() : 0);
        result = 31 * result + (channelid != null ? channelid.hashCode() : 0);
        result = 31 * result + (opby != null ? opby.hashCode() : 0);
        result = 31 * result + (opat != null ? opat.hashCode() : 0);
        result = 31 * result + (delflag != null ? delflag.hashCode() : 0);
        result = 31 * result + (noticeNumber != null ? noticeNumber.hashCode() : 0);
        result = 31 * result + (noticeStart != null ? noticeStart.hashCode() : 0);
        result = 31 * result + (noticeEnd != null ? noticeEnd.hashCode() : 0);
        result = 31 * result + (masterAddress != null ? masterAddress.hashCode() : 0);
        result = 31 * result + (rightType != null ? rightType.hashCode() : 0);
        result = 31 * result + (rightTypeName != null ? rightTypeName.hashCode() : 0);
        result = 31 * result + (estateNo != null ? estateNo.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (correctContent != null ? correctContent.hashCode() : 0);
        result = 31 * result + (dissentpsn != null ? dissentpsn.hashCode() : 0);
        result = 31 * result + (proveno != null ? proveno.hashCode() : 0);
        result = 31 * result + (ctateProno != null ? ctateProno.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (noticeType != null ? noticeType.hashCode() : 0);
        result = 31 * result + (noticeTypeName != null ? noticeTypeName.hashCode() : 0);
        result = 31 * result + (landSeano != null ? landSeano.hashCode() : 0);
        result = 31 * result + (isSynchro != null ? isSynchro.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (ywlsh != null ? ywlsh.hashCode() : 0);
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        result = 31 * result + (slsj != null ? slsj.hashCode() : 0);
        result = 31 * result + (noticeContent != null ? noticeContent.hashCode() : 0);
        return result;
    }
}