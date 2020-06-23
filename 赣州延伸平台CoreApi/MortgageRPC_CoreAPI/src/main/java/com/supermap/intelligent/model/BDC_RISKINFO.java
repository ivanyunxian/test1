package com.supermap.intelligent.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
     *  抵押平台---抵押风险防控表
     * @author luml 2019-08-08
     */
    @Entity
    @Table(name = "BDC_RISKINFO", schema = "BDC_MRPC")
    public class BDC_RISKINFO implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;

        private String xzlx;//限制类型

        private Date xzqsrq;//限制起始日期

        private Date xzzzrq;//限制终止日期

        private String qlid;
        /**
         * 不动产单元号
         */
        private String bdcdyh;

        /**
         * 不动产权证号
         */
        private String bdcqzh;

        /**
         * 登记单元id
         */
        private String djdyid;

        /**
         * 坐落
         */
        private String zl;

        /**
         * 业务来源（0：登记系统,1：最多跑一次平台,2：远程报件,3：抵押平台,4：微信,5：金融机构api,6：公积金api ）
         */
        private String ywly;

        /**
         * 内网流水号
         */
        private String ywlsh;

        /**
         * 外流水号
         */
        private String wlsh;

        /**
         * 单元状态（yy,异议登记；cf,查封；xz,限制；dy抵押）
         */
        private String dyzt;

        /**
         * 登簿时间
         */
        private Date dbsj;

        /**
         * 行政区代码
         */
        private String divisionCode;

        /**
         * 部门id
         */
        private String departid;

        @Column(name = "bdcdyh")
        public String getBdcdyh() {
            return bdcdyh;
        }

        public void setBdcdyh(String bdcdyh) {
            this.bdcdyh = bdcdyh;
        }

        @Column(name="bdcqzh")
        public String getBdcqzh() {
            return bdcqzh;
        }

        public void setBdcqzh(String bdcqzh) {
            this.bdcqzh = bdcqzh;
        }

        @Column(name = "djdyid")
        public String getDjdyid() {
            return djdyid;
        }

        public void setDjdyid(String djdyid) {
            this.djdyid = djdyid;
        }

        @Column(name = "zl")
        public String getZl() {
            return zl;
        }

        public void setZl(String zl) {
            this.zl = zl;
        }

        @Column(name = "ywly")
        public String getYwly() {
            return ywly;
        }

        public void setYwly(String ywly) {
            this.ywly = ywly;
        }

        @Column(name = "ywlsh")
        public String getYwlsh() {
            return ywlsh;
        }

        public void setYwlsh(String ywlsh) {
            this.ywlsh = ywlsh;
        }

        @Column(name = "wlsh")
        public String getWlsh() {
            return wlsh;
        }

        public void setWlsh(String wlsh) {
            this.wlsh = wlsh;
        }

        @Column(name = "dyzt")
        public String getDyzt() {
            return dyzt;
        }

        public void setDyzt(String dyzt) {
            this.dyzt = dyzt;
        }

        @Column(name = "dbsj")
        public Date getDbsj() {
            return dbsj;
        }

        public void setDbsj(Date dbsj) {
            this.dbsj = dbsj;
        }

        @Column(name = "division_code")
        public String getDivisionCode() {
            return divisionCode;
        }

        public void setDivisionCode(String divisionCode) {
            this.divisionCode = divisionCode;
        }

        @Column(name = "departid")
        public String getDepartid() {
            return departid;
        }

        public void setDepartid(String departid) {
            this.departid = departid;
        }

        @Column(name = "qlid")
        public String getQlid() {
            return qlid;
        }

        public void setQlid(String qlid) {
            this.qlid = qlid;
        }

        @Column(name = "xzlx" )
        public String getXzlx() {
            return xzlx;
        }

        public void setXzlx(String xzlx) {
            this.xzlx = xzlx;
        }

        @Column(name = "xzqsrq")
        public Date getXzqsrq() {
            return xzqsrq;
        }

        public void setXzqsrq(Date xzqsrq) {
            this.xzqsrq = xzqsrq;
        }

        @Column(name = "xzzzrq")
        public Date getXzzzrq() {
            return xzzzrq;
        }

        public void setXzzzrq(Date xzzzrq) {
            this.xzzzrq = xzzzrq;
        }

        @Id
        @Column(name = "id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
