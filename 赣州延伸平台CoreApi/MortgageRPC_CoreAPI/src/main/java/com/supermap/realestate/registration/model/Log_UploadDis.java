package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.workflow.service.common.Common;


/**
 * 上传分布式修改日志
 * @author lx
 *2018年10月22号
 */
@Entity
@Table(name = "log_uploaddis", schema = "log")
public class Log_UploadDis {
	private String id ; //主键
	private  String nowDis;//使用的盘符
	private  String newDis;//新的盘符
	private  String cause;//原因
	private  String fullis;//是否全满
	private  Date lgtime;//日志形成时间
	private  String lgdescription;//日志内容
	private  String lgcontent;//日志详情
	public Log_UploadDis() {
		this.id=Common.CreatUUID();
		this.nowDis=ConfigHelper.getNameByValue("MATERIAL");
		this.lgtime=new Date();
	}
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "nowDis")
	public String getNowDis() {
		return nowDis;
	}
	public void setNowDis(String nowDis) {
		this.nowDis = nowDis;
	}
	@Column(name = "newDis")
	public String getNewDis() {
		return newDis;
	}
	public void setNewDis(String newDis) {
		this.newDis = newDis;
	}
	
	@Column(name = "cause")
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	@Column(name = "fullis")
	public String getFullis() {
		return fullis;
	}
	public void setFullis(String fullis) {
		this.fullis = fullis;
	}
	
	@Column(name = "lgtime")
	public Date getLgtime() {
		return lgtime;
	}
	public void setLgtime(Date lgtime) {
		this.lgtime = lgtime;
	}
	
	@Column(name = "lgdescription")
	public String getLgdescription() {
		return lgdescription;
	}
	public void setLgdescription(String lgdescription) {
		this.lgdescription = lgdescription;
	}
	
	@Column(name = "lgcontent")
	public String getLgcontent() {
		return lgcontent;
	}
	public void setLgcontent(String lgcontent) {
		this.lgcontent = lgcontent;
	}
	

	
}
