package com.supermap.realestate.registration.model;

/**
 * 数据上报结果表
 * @author diaoliwei
 * @date 2016-1-17
 */
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SJSB;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;

@Entity
@Table(name = "bdcs_sjsb", schema = "bdck")
public class BDCS_SJSB extends GenerateBDCS_SJSB {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "operateuser")
	public String getOPERATEUSER() {
		return super.getOPERATEUSER();
	}

	@Override
	@Column(name = "bwmc")
	public String getBWMC() {
		return super.getBWMC();
	}

	@Override
	@Column(name = "successflag")
	public String getSUCCESSFLAG() {
		return super.getSUCCESSFLAG();
	}

	@Override
	@Column(name = "responseinfo")
	public String getRESPONSEINFO() {
		return super.getRESPONSEINFO();
	}
 
	@Override
	@Column(name = "path")
	public String getPATH() {
		return super.getPATH();
	}

	@Override
	@Column(name = "operatetime")
	public Date getOPERATETIME() {
		return super.getOPERATETIME();
	}
	
	@Override
	@Column(name = "loginname")
	public String getLOGINNAME(){
		return super.getLOGINNAME();
	}
	
	@Override
	@Column(name = "reccode")
	public String getRECCODE(){
		return super.getRECCODE();
	}
	
	@Override
	@Column(name = "proinstid")
	public String getPROINSTID(){
		return super.getPROINSTID();
	}
	
	//时间字符类型
	private String operateTimeStr;

	@Transient
	public String getOperateTimeStr() {
		return operateTimeStr;
	}
	
	public void setOperateTimeStr(String operateTimeStr) {
		this.operateTimeStr = operateTimeStr;
	}
	
	//接入业务名称
	private String recCodeName;
	@Transient
	public String getRecCodeName() {
		if(recCodeName == null){
			if(this.getRECCODE() != null){
				recCodeName = ConstValue.RECCODE.initFromName(this.getRECCODE());
			}
		}
		return recCodeName;
	}

	public void setRecCodeName(String recCodeName) {
		this.recCodeName = recCodeName;
	}
}
