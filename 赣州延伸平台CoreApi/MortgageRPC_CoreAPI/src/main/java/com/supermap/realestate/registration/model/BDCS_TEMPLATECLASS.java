package com.supermap.realestate.registration.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "BDCS_TEMPLATECLASS", schema = "BDCK")
public class BDCS_TEMPLATECLASS implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String TYPE_ID;//分类编号
	private String TYPE_PID;//上级分类
	private String TYPE_NAME;//分类名称
	private Integer TYPE_INDEX;//分类索引
	private String TYPE_DESC;//分类描述
	private String MBBS;//模板标识，1表示意见，2表示标签
	
	
	@Id
	@Column(name = "TYPE_ID")
	public String getTYPE_ID() {
		if (TYPE_ID == null)
			TYPE_ID = UUID.randomUUID().toString().replace("-", "");
		return TYPE_ID;
	}
	public void setTYPE_ID(String tYPE_ID) {
		TYPE_ID = tYPE_ID;
	}
	
	@Column(name = "TYPE_PID")
	public String getTYPE_PID() {
		return TYPE_PID;
	}
	public void setTYPE_PID(String tYPE_PID) {
		TYPE_PID = tYPE_PID;
	}
	
	@Column(name = "TYPE_NAME")
	public String getTYPE_NAME() {
		return TYPE_NAME;
	}
	public void setTYPE_NAME(String tYPE_NAME) {
		TYPE_NAME = tYPE_NAME;
	}
	
	@Column(name = "TYPE_INDEX")
	public Integer getTYPE_INDEX() {
		return TYPE_INDEX;
	}
	public void setTYPE_INDEX(Integer tYPE_INDEX) {
		TYPE_INDEX = tYPE_INDEX;
	}
	
	@Column(name = "TYPE_DESC")
	public String getTYPE_DESC() {
		return TYPE_DESC;
	}
	public void setTYPE_DESC(String tYPE_DESC) {
		TYPE_DESC = tYPE_DESC;
	}
	
	@Column(name = "MBBS")
	public String getMBBS() {
		return MBBS;
	}
	public void setMBBS(String mBBS) {
		MBBS = mBBS;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
