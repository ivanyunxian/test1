package com.supermap.intelligent.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  不动产权证表
 * @author luml 2019-08-20
 */
@Entity
@Table(name = "BDC_ZS",schema = "BDC_MRPC")
public class BDC_ZS implements Serializable {
    private static final long serialVersionUID = 1L;

    private String zsid;
    private String qlid;
    /**
     * 申请人id
     */
    private String sqrid;

    /**
     * 流水号 外网流水号
     */
    private String prolsh;

    /**
     * 区划代码
     */
    private String divisionCode;

    /**
     * 证书编号
     */
    private String zsbh;

    /**
     * 不动产权证号
     */
    private String bdcqzh;

    /**
     * 缮证时间,成功生成证书的时间
     */
    private Date szsj;

    /**
     * 版本号
     */
    private String versionno;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifytime;

    /**
     * 不动产单元号
     */
    private String bdcdyh;

    /**
     * 产权证号
     */
    private String cqzh;

    /**
     * 附记
     */
    private String fj;

    /**
     * 证书封面_日
     */
    private String fm_day;

    /**
     * 证书封面_月
     */
    private String fm_month;

    /**
     * 证书封面_年
     */
    private String fm_year;

    /**
     * 共有情况
     */
    private String gyqk;

    /**
     * 面积
     */
    private String mj;

    /**
     * 年度
     */
    private String nd;

    /**
     * 区划简称
     */
    private String qhjc;

    /**
     * 区划名称
     */
    private String qhmc;

    /**
     * 权利类型
     */
    private String qllx;

    /**
     * 权利其他状况
     */
    private String qlqtzk;

    /**
     * 权利人
     */
    private String qlr;

    /**
     * 权利性质
     */
    private String qlxz;

    /**
     * 使用期限
     */
    private String syqx;

    /**
     * 原不动产权证号
     */
    private String ybdcqzh;

    /**
     * 用途
     */
    private String yt;

    /**
     * 坐落
     */
    private String zl;

    /**
     * 证书证明类型（zs,证书；zm,证明）
     */
    private String type;

    /**
     * 证明权利或事项
     */
    private String zmqlhsx;

    /**
     * 义务人（证明）
     */
    private String ywr;

    /**
     * 其他（证明上的“其他”）
     */
    private String qt;
    /**
     * 登簿结果
     */
    private String dbjg;

    /**
     * 证明-年
     */
    private String year;

    /**
     * 证明-月
     */
    private String month;

    /**
     * 证明-日
     */
    private String day;

    /**
     * 证明-区划简称
     */
    private String sjc;

    /**
     * 证明-不动产权证-序号
     */
    private String sxh;
    
    private String mongoid;
    
    private String filename;
    
    private String pdf;

    @Column(name = "year")
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Column(name = "month")
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Column(name = "day")
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Column(name = "sjc")
    public String getSjc() {
        return sjc;
    }

    public void setSjc(String sjc) {
        this.sjc = sjc;
    }

    @Column(name = "sxh")
    public String getSxh() {
        return sxh;
    }

    public void setSxh(String sxh) {
        this.sxh = sxh;
    }

    @Column(name = "dbjg")
    public String getDbjg() {
        return dbjg;
    }

    public void setDbjg(String dbjg) {
        this.dbjg = dbjg;
    }

    @Id
    @Column(name = "zsid")
    public String getZsid() {
        return zsid;
    }

    public void setZsid(String zsid) {
        this.zsid = zsid;
    }
    @Column(name = "qlid")
    public String getQlid() {
        return qlid;
    }

    public void setQlid(String qlid) {
        this.qlid = qlid;
    }

    @Column(name = "sqrid")
    public String getSqrid() {
        return sqrid;
    }

    public void setSqrid(String sqrid) {
        this.sqrid = sqrid;
    }
    @Column(name = "prolsh")
    public String getProlsh() {
        return prolsh;
    }

    public void setProlsh(String prolsh) {
        this.prolsh = prolsh;
    }
    @Column(name = "DIVISION_CODE")
    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }
    @Column(name = "zsbh")
    public String getZsbh() {
        return zsbh;
    }

    public void setZsbh(String zsbh) {
        this.zsbh = zsbh;
    }
    @Column(name = "bdcqzh")
    public String getBdcqzh() {
        return bdcqzh;
    }

    public void setBdcqzh(String bdcqzh) {
        this.bdcqzh = bdcqzh;
    }
    @Column(name = "szsj")
    public Date getSzsj() {
        return szsj;
    }

    public void setSzsj(Date szsj) {
        this.szsj = szsj;
    }
    @Column(name = "versionno")
    public String getVersionno() {
        return versionno;
    }

    public void setVersionno(String versionno) {
        this.versionno = versionno;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "modifytime")
    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }
    @Column(name = "bdcdyh")
    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }
    @Column(name = "cqzh")
    public String getCqzh() {
        return cqzh;
    }

    public void setCqzh(String cqzh) {
        this.cqzh = cqzh;
    }
    @Column(name = "fj")
    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }
    @Column(name = "fm_day")
    public String getFm_day() {
        return fm_day;
    }

    public void setFm_day(String fm_day) {
        this.fm_day = fm_day;
    }
    @Column(name = "fm_month")
    public String getFm_month() {
        return fm_month;
    }

    public void setFm_month(String fm_month) {
        this.fm_month = fm_month;
    }
    @Column(name = "fm_year")
    public String getFm_year() {
        return fm_year;
    }

    public void setFm_year(String fm_year) {
        this.fm_year = fm_year;
    }
    @Column(name = "gyqk")
    public String getGyqk() {
        return gyqk;
    }

    public void setGyqk(String gyqk) {
        this.gyqk = gyqk;
    }
    @Column(name = "mj")
    public String getMj() {
        return mj;
    }

    public void setMj(String mj) {
        this.mj = mj;
    }
    @Column(name = "nd")
    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }
    @Column(name = "qhjc")
    public String getQhjc() {
        return qhjc;
    }

    public void setQhjc(String qhjc) {
        this.qhjc = qhjc;
    }
    @Column(name = "qhmc")
    public String getQhmc() {
        return qhmc;
    }

    public void setQhmc(String qhmc) {
        this.qhmc = qhmc;
    }
    @Column(name = "qllx")
    public String getQllx() {
        return qllx;
    }

    public void setQllx(String qllx) {
        this.qllx = qllx;
    }
    @Column(name = "qlqtzk")
    public String getQlqtzk() {
        return qlqtzk;
    }

    public void setQlqtzk(String qlqtzk) {
        this.qlqtzk = qlqtzk;
    }
    @Column(name = "qlr")
    public String getQlr() {
        return qlr;
    }

    public void setQlr(String qlr) {
         this.qlr = qlr;
    }
    @Column(name = "qlxz")
    public String getQlxz() {
        return qlxz;
    }

    public void setQlxz(String qlxz) {
        this.qlxz = qlxz;
    }
    @Column(name = "syqx")
    public String getSyqx() {
        return syqx;
    }

    public void setSyqx(String syqx) {
        this.syqx = syqx;
    }
    @Column(name = "ybdcqzh")
    public String getYbdcqzh() {
        return ybdcqzh;
    }

    public void setYbdcqzh(String ybdcqzh) {
        this.ybdcqzh = ybdcqzh;
    }
    @Column(name = "yt")
    public String getYt() {
        return yt;
    }

    public void setYt(String yt) {
        this.yt = yt;
    }
    @Column(name = "zl")
    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "zmqlhsx")
    public String getZmqlhsx() {
        return zmqlhsx;
    }

    public void setZmqlhsx(String zmqlhsx) {
        this.zmqlhsx = zmqlhsx;
    }
    @Column(name = "ywr")
    public String getYwr() {
        return ywr;
    }

    public void setYwr(String ywr) {
        this.ywr = ywr;
    }
    @Column(name = "qt")
    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    @Column(name="mongoid")
	public String getMongoid() {
		return mongoid;
	}

	public void setMongoid(String mongoid) {
		this.mongoid = mongoid;
	}

	@Column(name="filename")
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name="pdf")
	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
}