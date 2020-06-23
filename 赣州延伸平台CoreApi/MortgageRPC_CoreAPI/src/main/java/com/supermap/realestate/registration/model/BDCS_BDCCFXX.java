package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-20 
//* ----------------------------------------
//* Public Entity BDCS_BDCCFQKB 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_BDCCFXX;

import java.util.Date;

@Entity
@Table(name = "BDCS_BDCCFXX", schema = "BDCK")
public class BDCS_BDCCFXX extends GenerateBDCS_BDCCFXX {

	@Override
	@Id
	@Column(name = "cfxxbh", length = 50)
	public String getId() { //查封信息编号 1
		return super.getId();
	}

	@Override
	@Column(name = "cfwh")
	public String getCFWH() { //查封文号 2
		return super.getCFWH();
	}
	
	@Override
	@Column(name = "jfwh")
	public String getJFWH() { //解封文号 3
		return super.getJFWH();
	}
	
	@Override
	@Column(name = "sdrxm")
	public String getSDRXM() { //送达人姓名 4
		return super.getSDRXM();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {	//创建时间 5
		return super.getCREATETIME();
	}
	@Override
	@Column(name = "sdtime") 
	public String getSDTIME() {	//送达时间 6
		return super.getSDTIME();
	}
	@Override
	@Column(name = "cfdate")
	public String getCFDATE() {	//查封时间 7
		return super.getCFDATE();
	}
	@Override
	@Column(name = "jfdate")
	public String getJFDATE() {
		return super.getJFDATE();	//解封时间 8
	}
	
	@Override
	@Column(name = "updatetime")
	public Date getUPDATETIME() {	//更新时间 9
		return super.getUPDATETIME();
	}
	
	@Override
	@Column(name = "cjr")
	public String getCJR() {	//创建人 10
		return super.getCJR();
	}
	
	@Override
	@Column(name = "bcfr")
	public String getBCFR() {	//被查封人 11
		return super.getBCFR();
	}
	
	@Override
	@Column(name = "bcfrzjhm")
	public String getBCFRZJHM(){
		return super.getBCFRZJHM();
	}
	
	@Override
	@Column(name = "msrmc")
	public String getMSRMC() {	//买受人 11
		return super.getMSRMC();
	}
	
	@Override
	@Column(name = "msrzjh")
	public String getMSRZJH() {	//买受人证件号
		return super.getMSRZJH();
	}
	
	@Override
	@Column(name = "cfbdw")	//查封标的物 12
	public String getCFBDW() {
		return super.getCFBDW();
	}
	
	@Override
	@Column(name = "tdzh")
	public String getTDZH() {	//土地证号 13
		return super.getTDZH();
	}
	
	@Override
	@Column(name = "bsm")
	public String getBSM() {	//标识码 14
		return super.getBSM();
	}
	
	@Override
	@Column(name = "tdmj")
	public String getTDMJ() {	//土地面积 15
		return super.getTDMJ();
	}
	
	@Override
	@Column(name = "fczh")
	public String getFCZH() { 	//房产证号 16
		return super.getFCZH();
	}
	
	
	@Override
	@Column(name = "fwmj")
	public String getFWMJ() {	//房屋面积 17
		return super.getFWMJ();
	}
	
	@Override
	@Column(name = "cfjg")
	public String getCFJG() {	//查封机构 18
		return super.getCFJG();
	}
	
	@Override
	@Column(name = "bz")
	public String getBZ() { //备注 19
		return super.getBZ();
	}
	
	/*@Override
	@Column(name = "zl")
	public String getZL() {	//坐落 20
		return super.getZL();
	}*/
	
	@Override
	@Column(name = "wtfycfdw")
	public String getWTFYCFDW() {	//委托法院查封单位 21
		return super.getWTFYCFDW();
	}
	
	@Override
	@Column(name = "cfqx")
	public String getCFQX() {	//查封期限 22
		return super.getCFQX();
	}
	
	@Override
	@Column(name = "cdqk")
	public String getCDQK() {	//查档情况 23
		return super.getCDQK();
	}
	
	@Override
	@Column(name = "tsclqk")
	public String getTSCLQK() {	//推送处理情况 24
		return super.getTSCLQK();
	}
	
	@Override
	@Column(name = "gxr")
	public String getGXR() { //更新人 25
		return super.getGXR();
	}
	@Override
	@Column(name = "sfjf")
	public String getSFJF(){
		return super.getSFJF();//添加是否解封
		
	}
	@Override
	@Column(name = "bh")
	public String getBH(){
		return super.getBH();//序号
	}
	@Override
	@Column(name = "filepath")
	public String getFILEPATH(){
		return super.getFILEPATH();//序号
	}
	@Override
	@Column(name = "cffw")
	public String getCFFW(){
		return super.getCFFW();//序号
	}
	@Override
	@Column(name = "qssj")
	public String getQSSJ(){
		return super.getQSSJ();//序号
	}	
	@Override
	@Column(name = "zzsj")
	public String getZZSJ(){
		return super.getZZSJ();//序号
	}
	@Override
	@Column(name = "zl")
	public String getZL(){
		return super.getZL();//序号
	}
	@Override
	@Column(name = "yg")
	public String getYG(){
		return super.getYG();//序号
	}@Override
	@Column(name = "bg")
	public String getBG(){
		return super.getBG();//序号
	}
	@Override
	@Column(name = "bzxr")
	public String getBZXR(){
		return super.getBZXR();//被执行人
	}
	@Override
	@Column(name = "bzxrzjh")
	public String getBZXRZJH(){
		return super.getBZXRZJH();//被执行人证件号
	}
	
	@Override
	@Column(name = "lrtime")
	public String getLRTIME(){
		return super.getLRTIME();//录入时间
	}
}
