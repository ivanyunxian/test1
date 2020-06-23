package com.supermap.wisdombusiness.synchroinline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/* 房屋信息 */
@Entity
@Table(name = "Pro_fwxx")
public class Pro_fwxx
{
	@Column
	@Id
	protected String id;
	@Column
	protected String bdcdyh;
	@Column
	protected String proinst_id;
	@Column
	protected String prodef_id;
	@Column
	protected String prodefcode;
	@Column
	protected String fwbm;
	@Column
	protected String zl;
	@Column
	protected String mjdw;
	@Column
	protected Integer hh;
	@Column
	protected String hx;
	@Column
	protected String hxjg;
	@Column
	protected String fwyt;
	@Column
	protected String ghyt;
	@Column
	protected double fwsyqmj;
	@Column
	protected String fwjg;
	@Column
	protected Date jgsj;
	@Column
	protected String fwlx;
	@Column
	protected String fwxz;
	@Column
	protected String yxbz;
	@Column
	protected String xmmc;
	@Column
	protected String qlxz;
	@Column
	protected String dyh;
	@Column
	protected String bz;
	@Column
	protected String user_id;
	@Column
	protected String user_name;
	@Column
	protected Date createdate;
	@Column
	protected String zhxgry;
	@Column
	protected String creator;
	@Column
	protected String modifyer;
	@Column
	protected String areacode;
	@Column
	protected String areaname;
	@Column
	protected Date qlqssj;
	@Column
	protected Date qljssj;
	@Column
	protected String dyfs;
	@Column
	protected Double zgzqse;
	@Column
	protected Double bdbzzqse;
	@Column
	protected String bdcqzmh;
	@Column
	protected String zxyy;
	@Column
	protected String zxfj;
	/**
	 * 查封信息
	 */
	@Column
	protected String cfjg;
	@Column
	protected String cfwj;
	@Column
	protected String cffw;
	@Column
	protected String jfwj;
	@Column
	protected String jfwh;
	@Column
	protected String jfjg;
	@Column
	protected String bcfqlr;
	@Column
	protected String cffj;
	@Column
	protected Date fysdsj;
	@Column
	protected String cfwh;
	@Column
	protected String jffj;

	/**
	 * 申请人拓展字段
	 */
	@Column
	protected String sqrmc;
	@Column
	protected String zjh;
	@Column
	protected String sqrzjlx;
	@Column
	protected String gyfs;
	@Column
	protected String qlbl;
	@Column
	protected String lxdh;

	@Column
	protected double qdjg;//取得价格
	@Column
	protected double dypgjz;//抵押评估价值

	@Column
	protected String zl_bgh;
	@Column
	protected String tdsyq_yt;
	@Column
	protected double tdsyq_mj;

	@Column
	protected String yt_bgh;
	@Column
	protected double mj_bgh;

	@Column
	protected String fj;
	@Column
	protected String djyy;
	@Column
	protected String qzlx;
	@Column
	protected String chid;

	@Column
	protected Double dywjz;//抵押物价值

	public Double getDywjz() {
		return dywjz;
	}

	public void setDywjz(Double dywjz) {
		this.dywjz = dywjz;
	}

	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	public String getQzlx() {
		return qzlx;
	}
	public void setQzlx(String qzlx) {
		this.qzlx = qzlx;
	}

	public String getSqrzjlx() {
		return sqrzjlx;
	}
	public void setSqrzjlx(String sqrzjlx) {
		this.sqrzjlx = sqrzjlx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getProinst_id() {
		return proinst_id;
	}
	public void setProinst_id(String proinst_id) {
		this.proinst_id = proinst_id;
	}
	public String getProdef_id() {
		return prodef_id;
	}
	public void setProdef_id(String prodef_id) {
		this.prodef_id = prodef_id;
	}
	public String getProdefcode() {
		return prodefcode;
	}
	public void setProdefcode(String prodefcode) {
		this.prodefcode = prodefcode;
	}
	public String getFwbm() {
		return fwbm;
	}
	public void setFwbm(String fwbm) {
		this.fwbm = fwbm;
	}
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
	public String getMjdw() {
		return mjdw;
	}
	public void setMjdw(String mjdw) {
		this.mjdw = mjdw;
	}
	public Integer getHh() {
		return hh;
	}
	public void setHh(Integer hh) {
		this.hh = hh;
	}
	public String getHx() {
		return hx;
	}
	public void setHx(String hx) {
		this.hx = hx;
	}
	public String getHxjg() {
		return hxjg;
	}
	public void setHxjg(String hxjg) {
		this.hxjg = hxjg;
	}
	public String getFwyt() {
		return fwyt;
	}
	public void setFwyt(String fwyt) {
		this.fwyt = fwyt;
	}
	public String getGhyt() {
		return ghyt;
	}
	public void setGhyt(String ghyt) {
		this.ghyt = ghyt;
	}
	public double getFwsyqmj() {
		return fwsyqmj;
	}
	public void setFwsyqmj(double fwsyqmj) {
		this.fwsyqmj = fwsyqmj;
	}
	public String getFwjg() {
		return fwjg;
	}
	public void setFwjg(String fwjg) {
		this.fwjg = fwjg;
	}
	public Date getJgsj() {
		return jgsj;
	}
	public void setJgsj(Date jgsj) {
		this.jgsj = jgsj;
	}
	public String getFwlx() {
		return fwlx;
	}
	public void setFwlx(String fwlx) {
		this.fwlx = fwlx;
	}
	public String getFwxz() {
		return fwxz;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
	}
	public String getYxbz() {
		return yxbz;
	}
	public void setYxbz(String yxbz) {
		this.yxbz = yxbz;
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public String getQlxz() {
		return qlxz;
	}
	public void setQlxz(String qlxz) {
		this.qlxz = qlxz;
	}
	public String getDyh() {
		return dyh;
	}
	public void setDyh(String dyh) {
		this.dyh = dyh;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getZhxgry() {
		return zhxgry;
	}
	public void setZhxgry(String zhxgry) {
		this.zhxgry = zhxgry;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifyer() {
		return modifyer;
	}
	public void setModifyer(String modifyer) {
		this.modifyer = modifyer;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public Date getQlqssj() {
		return qlqssj;
	}
	public void setQlqssj(Date qlqssj) {
		this.qlqssj = qlqssj;
	}
	public Date getQljssj() {
		return qljssj;
	}
	public void setQljssj(Date qljssj) {
		this.qljssj = qljssj;
	}
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}
	public Double getZgzqse() {
		return zgzqse;
	}
	public void setZgzqse(Double zgzqse) {
		if (zgzqse == null) {
			this.zgzqse = 0.0;
		} else {
			this.zgzqse = zgzqse;
		}
	}
	public Double getBdbzzqse() {
		return bdbzzqse;
	}
	public void setBdbzzqse(Double bdbzzqse) {
		if (bdbzzqse == null) {
			this.bdbzzqse = 0.0;
		} else {
			this.bdbzzqse = bdbzzqse;
		}
	}
	public String getBdcqzmh() {
		return bdcqzmh;
	}
	public void setBdcqzmh(String bdcqzmh) {
		this.bdcqzmh = bdcqzmh;
	}
	public String getZxyy() {
		return zxyy;
	}
	public void setZxyy(String zxyy) {
		this.zxyy = zxyy;
	}
	public String getZxfj() {
		return zxfj;
	}
	public void setZxfj(String zxfj) {
		this.zxfj = zxfj;
	}
	public String getCfjg() {
		return cfjg;
	}
	public void setCfjg(String cfjg) {
		this.cfjg = cfjg;
	}
	public String getCfwj() {
		return cfwj;
	}
	public void setCfwj(String cfwj) {
		this.cfwj = cfwj;
	}
	public String getCffw() {
		return cffw;
	}
	public void setCffw(String cffw) {
		this.cffw = cffw;
	}
	public String getJfwj() {
		return jfwj;
	}
	public void setJfwj(String jfwj) {
		this.jfwj = jfwj;
	}
	public String getJfwh() {
		return jfwh;
	}
	public void setJfwh(String jfwh) {
		this.jfwh = jfwh;
	}
	public String getJfjg() {
		return jfjg;
	}
	public void setJfjg(String jfjg) {
		this.jfjg = jfjg;
	}
	public String getBcfqlr() {
		return bcfqlr;
	}
	public void setBcfqlr(String bcfqlr) {
		this.bcfqlr = bcfqlr;
	}
	public String getCffj() {
		return cffj;
	}
	public void setCffj(String cffj) {
		this.cffj = cffj;
	}
	public Date getFysdsj() {
		return fysdsj;
	}
	public void setFysdsj(Date fysdsj) {
		this.fysdsj = fysdsj;
	}
	public String getCfwh() {
		return cfwh;
	}
	public void setCfwh(String cfwh) {
		this.cfwh = cfwh;
	}
	public String getJffj() {
		return jffj;
	}
	public void setJffj(String jffj) {
		this.jffj = jffj;
	}
	public String getSqrmc() {
		return sqrmc;
	}
	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}
	public String getZjh() {
		return zjh;
	}
	public void setZjh(String zjh) {
		this.zjh = zjh;
	}
	public String getGyfs() {
		return gyfs;
	}
	public void setGyfs(String gyfs) {
		this.gyfs = gyfs;
	}
	public String getQlbl() {
		return qlbl;
	}
	public void setQlbl(String qlbl) {
		this.qlbl = qlbl;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public double getQdjg() {
		return qdjg;
	}
	public void setQdjg(double qdjg) {
		this.qdjg = qdjg;
	}

	public String getZl_bgh() {
		return zl_bgh;
	}

	public void setZl_bgh(String zl_bgh) {
		this.zl_bgh = zl_bgh;
	}

	public String getTdsyq_yt() {
		return tdsyq_yt;
	}

	public void setTdsyq_yt(String tdsyq_yt) {
		this.tdsyq_yt = tdsyq_yt;
	}

	public double getTdsyq_mj() {
		return tdsyq_mj;
	}

	public void setTdsyq_mj(double tdsyq_mj) {
		this.tdsyq_mj = tdsyq_mj;
	}

	public String getYt_bgh() {
		return yt_bgh;
	}

	public void setYt_bgh(String Yt_bgh) {
		this.yt_bgh = yt_bgh;
	}

	public double getMj_bgh() {
		return mj_bgh;
	}

	public void setMj_bgh(double mj_bgh) {
		this.mj_bgh = mj_bgh;
	}

	public String getDjyy() {
		return djyy;
	}

	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}

	public double getDypgjz() {
		return dypgjz;
	}

	public void setDypgjz(double dypgjz) {
		this.dypgjz = dypgjz;
	}

	public String getChid() {
		return chid;
	}

	public void setChid(String chid) {
		this.chid = chid;
	}
}
