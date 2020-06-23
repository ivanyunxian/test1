package com.supermap.wisdombusiness.synchroinline.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_LOG_ESATEINLINE",schema = "BDC_WORKFLOW")
public class EstateInlineLog {
	
	private String id;
	private String operation_Type;
	private Date operation_Date;
	private String operation_Content;
	private String operation_Res;
	private String operation_Ip;
	
	
	@Id
	@Column(name = "ID")
	public String getId() {
		if (id == null)
			id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "OPERATION_TYPE")
	public String getOperation_Type() {
		return operation_Type;
	}
	public void setOperation_Type(String operation_Type) {
		this.operation_Type = operation_Type;
	}
	@Column(name = "OPERATION_DATE")
	public Date getOperation_Date() {
		return operation_Date;
	}
	public void setOperation_Date(Date operation_Date) {
		this.operation_Date = operation_Date;
	}
	@Column(name = "OPERATION_CONTENT")
	public String getOperation_Content() {
		return operation_Content;
	}
	public void setOperation_Content(String operation_Content) {
		this.operation_Content = operation_Content;
	}
	@Column(name = "OPERATION_RES")
	public String getOperation_Res() {
		return operation_Res;
	}
	public void setOperation_Res(String operation_Res) {
		this.operation_Res = operation_Res;
	}
	@Column(name = "OPERATION_IP")
	public String getOperation_Ip() {
		return operation_Ip;
	}
	public void setOperation_Ip(String operation_Ip) {
		this.operation_Ip = operation_Ip;
	}

}
