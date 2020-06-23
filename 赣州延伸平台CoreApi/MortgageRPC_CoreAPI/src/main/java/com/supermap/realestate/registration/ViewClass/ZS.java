package com.supermap.realestate.registration.ViewClass;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @Description:证书
 * @author txd
 * @date 2019年8月22日 下午2:55:35
 * @Copyright SuperMap
 */
public class ZS {
	
	/** 证书编号 */
	@JSONField(name = "zsbh") 
    private String zsbh;
    
    /** 不动产权证号 */
	@JSONField(name = "bdcqzh") 
    private String bdcqzh;
    
    /** 权利人 */
	@JSONField(name = "qlr") 
    private String qlr;
    
    /** 共有情况 */
	@JSONField(name = "gyqk") 
    private String gyqk;
    
    /** 坐落 */
	@JSONField(name = "zl") 
    private String zl;
    
    /** 不动产单元号 */
	@JSONField(name = "bdcdyh") 
    private String bdcdyh;
    
    /** 权利类型 */
	@JSONField(name = "qllx") 
    private String qllx;
    
    /** 权利性质 */
	@JSONField(name = "qlxz") 
    private String qlxz;
    
    /** 用途 */
	@JSONField(name = "yt") 
    private String yt;
    
    /** 面积 */
	@JSONField(name = "mj") 
    private String mj;
    
    /** 使用期限xxxx年xx月xx日起xxxx年xx月xx日止 */
	@JSONField(name = "syqx") 
    private String syqx;
    
    /** 权利其他状况 */
	@JSONField(name = "qlqtzk") 
    private String qlqtzk;
    
    /** 区划代码简称 */
	@JSONField(name = "qhjc") 
    private String qhjc;
    
    /** 年度 */
	@JSONField(name = "nd") 
    private String nd;
    
    /** 区划名称 */
	@JSONField(name = "qhmc") 
    private String qhmc;
    
    /** 产权流水号 */
	@JSONField(name = "cqzh") 
    private String cqzh;
    
    /** 附记 */
	@JSONField(name = "fj") 
    private String fj;
    
    /**登簿的年份*/
	@JSONField(name = "fm_year") 
    private String fm_year;
    
    /**二维码*/
	@JSONField(name = "qrcode") 
    private String qrcode;
    
    /**证书编号*/
	@JSONField(name = "id_zsbh") 
    private String id_zsbh;
    
    /**证明权利或事项*/
	@JSONField(name = "zmqlhsx") 
    private String zmqlhsx;
    
    /**其他*/
	@JSONField(name = "qt") 
    private String qt;
    
    /**义务人*/
	@JSONField(name = "ywr") 
    private String ywr;
    
    /**原不动产权证号*/
	@JSONField(name = "ybdcqzh") 
    private String ybdcqzh;
    
    /**区分证书还是证明*/
	@JSONField(name = "type") 
    private String type;
	

	/**登簿时的月份*/
	@JSONField(name = "fm_day") 
    private String fm_month;
    /**登簿时的日*/
	@JSONField(name = "fm_day") 
    private String fm_day;
	
	public String getBdcqzh() {
		return bdcqzh;
	}
	public String getZsbh() {
		return zsbh;
	}
	public void setZsbh(String zsbh) {
		this.zsbh = zsbh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getQlr() {
		return qlr;
	}
	public void setQlr(String qlr) {
		this.qlr = qlr;
	}
	public String getGyqk() {
		return gyqk;
	}
	public void setGyqk(String gyqk) {
		this.gyqk = gyqk;
	}
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
	}
	public String getQlxz() {
		return qlxz;
	}
	public void setQlxz(String qlxz) {
		this.qlxz = qlxz;
	}
	public String getYt() {
		return yt;
	}
	public void setYt(String yt) {
		this.yt = yt;
	}
	public String getMj() {
		return mj;
	}
	public void setMj(String mj) {
		this.mj = mj;
	}
	public String getSyqx() {
		return syqx;
	}
	public void setSyqx(String syqx) {
		this.syqx = syqx;
	}
	public String getQlqtzk() {
		return qlqtzk;
	}
	public void setQlqtzk(String qlqtzk) {
		this.qlqtzk = qlqtzk;
	}
	public String getQhjc() {
		return qhjc;
	}
	public void setQhjc(String qhjc) {
		this.qhjc = qhjc;
	}
	public String getNd() {
		return nd;
	}
	public void setNd(String nd) {
		this.nd = nd;
	}
	public String getQhmc() {
		return qhmc;
	}
	public void setQhmc(String qhmc) {
		this.qhmc = qhmc;
	}
	public String getCqzh() {
		return cqzh;
	}
	public void setCqzh(String cqzh) {
		this.cqzh = cqzh;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	public String getFm_year() {
		return fm_year;
	}
	public void setFm_year(String fm_year) {
		this.fm_year = fm_year;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getId_zsbh() {
		return id_zsbh;
	}
	public void setId_zsbh(String id_zsbh) {
		this.id_zsbh = id_zsbh;
	}
	public String getZmqlhsx() {
		return zmqlhsx;
	}
	public void setZmqlhsx(String zmqlhsx) {
		this.zmqlhsx = zmqlhsx;
	}
	public String getQt() {
		return qt;
	}
	public void setQt(String qt) {
		this.qt = qt;
	}
	public String getYwr() {
		return ywr;
	}
	public void setYwr(String ywr) {
		this.ywr = ywr;
	}
	public String getYbdcqzh() {
		return ybdcqzh;
	}
	public void setYbdcqzh(String ybdcqzh) {
		this.ybdcqzh = ybdcqzh;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFm_month() {
		return fm_month;
	}
	public void setFm_month(String fm_month) {
		this.fm_month = fm_month;
	}
	public String getFm_day() {
		return fm_day;
	}
	public void setFm_day(String fm_day) {
		this.fm_day = fm_day;
	}
}
