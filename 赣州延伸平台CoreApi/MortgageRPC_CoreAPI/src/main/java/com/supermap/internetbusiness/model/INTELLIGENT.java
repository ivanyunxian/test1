package com.supermap.internetbusiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "INTELLIGENT", schema = "BDCK")
public class INTELLIGENT {

  private String id;
  private String ywlsh;
  private String xmbh;
  private String wlsh;
  private String bdcdyh;
  private String proName;
  private String bdcqzh;
  private String zl;
  private String sqr;
  private String bglx;
  private String shjg;
  private String shjgxx;
  private String content;
  private Date createdtime;
  private String file_number;

  @Id
  @Column(name = "id")
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString().replace("-", "");
    }
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(name = "ywlsh")
  public String getYwlsh() {
    return ywlsh;
  }

  public void setYwlsh(String ywlsh) {
    this.ywlsh = ywlsh;
  }

  @Column(name = "xmbh")
  public String getXmbh() {
    return xmbh;
  }

  public void setXmbh(String xmbh) {
    this.xmbh = xmbh;
  }

  @Column(name = "wlsh")
  public String getWlsh() {
    return wlsh;
  }

  public void setWlsh(String wlsh) {
    this.wlsh = wlsh;
  }

  @Column(name = "bdcdyh")
  public String getBdcdyh() {
    return bdcdyh;
  }

  public void setBdcdyh(String bdcdyh) {
    this.bdcdyh = bdcdyh;
  }

  @Column(name = "pro_name")
  public String getProName() {
    return proName;
  }

  public void setProName(String proName) {
    this.proName = proName;
  }

  @Column(name = "bdcqzh")
  public String getBdcqzh() {
    return bdcqzh;
  }

  public void setBdcqzh(String bdcqzh) {
    this.bdcqzh = bdcqzh;
  }

  @Column(name = "zl")
  public String getZl() {
    return zl;
  }

  public void setZl(String zl) {
    this.zl = zl;
  }

  @Column(name = "sqr")
  public String getSqr() {
    return sqr;
  }

  public void setSqr(String sqr) {
    this.sqr = sqr;
  }

  @Column(name = "")
  public String getBglx() {
    return bglx;
  }

  public void setBglx(String bglx) {
    this.bglx = bglx;
  }

  @Column(name = "shjg")
  public String getShjg() {
    return shjg;
  }

  public void setShjg(String shjg) {
    this.shjg = shjg;
  }

  @Column(name = "shjgxx")
  public String getShjgxx() {
    return shjgxx;
  }

  public void setShjgxx(String shjgxx) {
    this.shjgxx = shjgxx;
  }

  @Column(name = "content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "createdtime")
  public Date getCreatedtime() {
    return createdtime;
  }

  public void setCreatedtime(Date createdtime) {
    this.createdtime = createdtime;
  }

  public String getFile_number() {
    return file_number;
  }

  public void setFile_number(String file_number) {
    this.file_number = file_number;
  }
}
