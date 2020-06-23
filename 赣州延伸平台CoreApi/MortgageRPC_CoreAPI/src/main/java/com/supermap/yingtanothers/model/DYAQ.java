package com.supermap.yingtanothers.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月16日 下午3:05:50
 * 功能：鹰潭不动产共享中间库房抵押权属性结构表
 */
@Entity
@Table(name = "DYAQ", schema = "GXFCK")
public class DYAQ {

			//要素代码
			private String YSDM;
			//不动产单元号
			private String BDCDYH;
			//业务号
			private String YWH;
			//A.27抵押不动产类型
			private String DYBDCLX;
			//抵押人
			private String DYR;
			//A.28抵押方式
			private String DYFS;
			//表A.21 登记类型
			private String DJLX;
			//登记原因
			private String DJYY;
			//在建建筑物坐落
			private String ZJJZWZL;
			//在建建筑物抵押范围
			private String ZJJZWDYFW;
			//被担保主债权数额
			private Double BDBZZQSE;
			//债务履行起始时间
			private Date ZWLXQSSJ;
			//债务履行结束时间
			private Date ZWLXJSSJ;
			//最高债权确定事实
			private String ZGZQQDSS;
			//最高债权数额
			private Double ZGZQSE;
			//注销抵押业务号
			private String ZXDYYWH;
			//注销抵押原因
			private String ZXDYYY;
			//注销时间
			private Date ZXSJ;
			//不动产登记证明号
			private String BDCDJZMH;
			//区县代码
			private String QXDM;
			//登记机构
			private String DJJG;
			//登簿人
			private String DBR;
			//登记时间
			private Date DJSJ;
			//附记
			private String FJ;
			//A.22权属状态
			private String QSZT;			
			//权利ID
			private String QLID;
			//来源权利ID
			private String LYQLID;
			//关联ID
			private String RELATIONID;
			//共享项目编号
			private String GXXMBH;
			//抵押面积
			private Double DYMJ;
			//抵押权ID
			private String DYAQID;
			
			
				
			@Column(name = "RELATIONID")
			public String getRELATIONID() {
				return RELATIONID;
			}
			public void setRELATIONID(String rELATIONID) {
				RELATIONID = rELATIONID;
			}
			@Column(name = "YWH")
			public String getYWH() {
				return YWH;
			}
			public void setYWH(String yWH) {
				YWH = yWH;
			}
			
			@Column(name = "BDCDYH")
			public String getBDCDYH() {
				return BDCDYH;
			}
			public void setBDCDYH(String bDCDYH) {
				BDCDYH = bDCDYH;
			}
						
			@Column(name = "QLID")
			public String getQLID() {
				return QLID;
			}
			public void setQLID(String qLID) {
				QLID = qLID;
			}
			
			@Column(name = "DJLX")
			public String getDJLX() {
				return DJLX;
			}
			public void setDJLX(String dJLX) {
				DJLX = dJLX;
			}
			
			@Column(name = "DJYY")
			public String getDJYY() {
				return DJYY;
			}
			public void setDJYY(String dJYY) {
				DJYY = dJYY;
			}
			
			@Column(name = "ZWLXQSSJ")
			public Date getZWLXQSSJ() {
				return ZWLXQSSJ;
			}
			public void setZWLXQSSJ(Date zWLXQSSJ) {
				ZWLXQSSJ = zWLXQSSJ;
			}
			
			@Column(name = "ZWLXJSSJ")
			public Date getZWLXJSSJ() {
				return ZWLXJSSJ;
			}
			public void setZWLXJSSJ(Date zWLXJSSJ) {
				ZWLXJSSJ = zWLXJSSJ;
			}
			
			@Column(name = "BDCDJZMH")
			public String getBDCDJZMH() {
				return BDCDJZMH;
			}
			public void setBDCDJZMH(String bDCDJZMH) {
				BDCDJZMH = bDCDJZMH;
			}
			
			@Column(name = "QXDM")
			public String getQXDM() {
				return QXDM;
			}
			public void setQXDM(String qXDM) {
				QXDM = qXDM;
			}
			
			@Column(name = "DJJG")
			public String getDJJG() {
				return DJJG;
			}
			public void setDJJG(String dJJG) {
				DJJG = dJJG;
			}
			
			@Column(name = "DBR")
			public String getDBR() {
				return DBR;
			}
			public void setDBR(String dBR) {
				DBR = dBR;
			}
			
			@Column(name = "DJSJ")
			public Date getDJSJ() {
				return DJSJ;
			}
			public void setDJSJ(Date dJSJ) {
				DJSJ = dJSJ;
			}
			
			@Column(name = "FJ")
			public String getFJ() {
				return FJ;
			}
			public void setFJ(String fJ) {
				FJ = fJ;
			}
			
			@Column(name = "QSZT")
			public String getQSZT() {
				return QSZT;
			}
			public void setQSZT(String qSZT) {
				QSZT = qSZT;
			}
						
			@Column(name = "GXXMBH")
			public String getGXXMBH() {
				return GXXMBH;
			}
			public void setGXXMBH(String gXXMBH) {
				GXXMBH = gXXMBH;
			}
			
			@Column(name = "DYFS")
			public String getDYFS() {
				return DYFS;
			}
			public void setDYFS(String dYFS) {
				DYFS = dYFS;
			}
			
			@Column(name = "BDBZZQSE")
			public Double getBDBZZQSE() {
				return BDBZZQSE;
			}
			public void setBDBZZQSE(Double bDBZZQSE) {
				BDBZZQSE = bDBZZQSE;
			}
			
			@Column(name = "ZGZQQDSS")
			public String getZGZQQDSS() {
				return ZGZQQDSS;
			}
			public void setZGZQQDSS(String zGZQQDSS) {
				ZGZQQDSS = zGZQQDSS;
			}
			
			@Column(name = "ZGZQSE")
			public Double getZGZQSE() {
				return ZGZQSE;
			}
			public void setZGZQSE(Double zGZQSE) {
				ZGZQSE = zGZQSE;
			}
			
			@Column(name = "ZXDYYWH")
			public String getZXDYYWH() {
				return ZXDYYWH;
			}
			public void setZXDYYWH(String zXDYYWH) {
				ZXDYYWH = zXDYYWH;
			}
			
			@Column(name = "ZXDYYY")
			public String getZXDYYY() {
				return ZXDYYY;
			}
			public void setZXDYYY(String zXDYYY) {
				ZXDYYY = zXDYYY;
			}
			
			@Column(name = "ZXSJ")
			public Date getZXSJ() {
				return ZXSJ;
			}
			public void setZXSJ(Date zXSJ) {
				ZXSJ = zXSJ;
			}
			
			@Column(name = "DYBDCLX")
			public String getDYBDCLX() {
				return DYBDCLX;
			}
			public void setDYBDCLX(String dYBDCLX) {
				DYBDCLX = dYBDCLX;
			}
			
			@Column(name = "ZJJZWZL")
			public String getZJJZWZL() {
				return ZJJZWZL;
			}
			public void setZJJZWZL(String zJJZWZL) {
				ZJJZWZL = zJJZWZL;
			}
			
			@Column(name = "ZJJZWDYFW")
			public String getZJJZWDYFW() {
				return ZJJZWDYFW;
			}
			public void setZJJZWDYFW(String zJJZWDYFW) {
				ZJJZWDYFW = zJJZWDYFW;
			}
			
			@Column(name = "DYR")
			public String getDYR() {
				return DYR;
			}
			public void setDYR(String dYR) {
				DYR = dYR;
			}
			
			@Column(name = "LYQLID")
			public String getLYQLID() {
				return LYQLID;
			}
			public void setLYQLID(String lYQLID) {
				LYQLID = lYQLID;
			}
			
			@Column(name = "YSDM")
			public String getYSDM() {
				return YSDM;
			}
			public void setYSDM(String ySDM) {
				YSDM = ySDM;
			}
			@Column(name = "DYMJ")
			public Double getDYMJ() {
				return DYMJ;
			}
			public void setDYMJ(Double dYMJ) {
				DYMJ = dYMJ;
			}
			@Id
			@Column(name = "DYAQID")
			public String getDYAQID() {
				return DYAQID;
			}
			public void setDYAQID(String dYAQID) {
				DYAQID = dYAQID;
			}
			
		
}
