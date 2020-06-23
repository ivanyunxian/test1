package com.supermap.wisdombusiness.framework.model;

/**
 * 业务日志表
 * @author DIAOLIWEI
 * @date 2016-1-14
 */
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_YW_LOG")
public class Ywlog implements Serializable{
	
	private String id;
	private String operateUser;
	private Date operateTime;
	private String operateContent;
	private String isSuccess;
	private String ip;
	private String hostname;
	private String loginName;
	private String logLevel;
	private String levelName;
	private String operateTimeStr;
	private String successText;
	
	
	@Id
	@Column(name = "ID", length = 50)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "OPERATEUSER",  length = 50)
	public String getOperateUser() {
		return operateUser;
	}
	
	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}
	
	@Column(name = "OPERATETIME")
	public Date getOperateTime() {
		return operateTime;
	}
	
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	@Column(name = "OPERATECONTENT", length = 100)
	public String getOperateContent() {
		return operateContent;
	}
	
	public void setOperateContent(String operateContent) {
		this.operateContent = operateContent;
	}
	
	@Column(name = "ISSUCCESS", length = 10)
	public String getIsSuccess() {
		return isSuccess;
	}
	
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	@Column(name = "HOSTNAME", length = 500)
	public String getHostName() {
		return hostname;
	}
	
	public void setHostName(String hostname) {
		this.hostname = hostname;
	}
	
	@Column(name = "IP", length = 50)
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "LOGINNAME", length = 50)
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name = "LOGLEVEL", length = 10)
	public String getLogLevel() {
		return logLevel;
	}
	
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	
	@Column(name = "LEVELNAME", length = 30)
	public String getLevelName() {
		return levelName;
	}
	
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@Transient
	public String getOperateTimeStr() {
		return operateTimeStr;
	}

	public void setOperateTimeStr(String operateTimeStr) {
		this.operateTimeStr = operateTimeStr;
	}

	@Transient
	public String getSuccessText() {
		return successText;
	}

	public void setSuccessText(String successText) {
		this.successText = successText;
	}
	
}
