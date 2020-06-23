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
@Table(name = "BDCS_OPINION", schema = "BDCK")
public class BDCS_OPINION implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String OPINIONDEF_ID;//意见定义标识
	private String OPINION_DESC;//意见描述
	private String OPINION_CONTENT;//意见内容
	private Integer OPINION_INDEX;//意见索引
	private String OPINION_NAME;//意见名称
	private String OPINION_TYPE;//意见类型
	private String OPINIONTYPE_ID;//意见分类ID
	private Integer OPINION_NEED;//意见必选
	private String OPINION_BM;//文件编码
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "OPINIONDEF_ID")
	public String getOPINIONDEF_ID() {
		if (OPINIONDEF_ID == null)
			OPINIONDEF_ID = UUID.randomUUID().toString().replace("-", "");
		return OPINIONDEF_ID;
	}
	
	public void setOPINIONDEF_ID(String oPINIONDEF_ID) {
		OPINIONDEF_ID = oPINIONDEF_ID;
	}
	
	@Column(name = "OPINION_DESC")
	public String getOPINION_DESC() {
		return OPINION_DESC;
	}
	
	public void setOPINION_DESC(String oPINION_DESC) {
		OPINION_DESC = oPINION_DESC;
	}
	
	@Column(name = "OPINION_CONTENT")
	public String getOPINION_CONTENT() {
		return OPINION_CONTENT;
	}

	public void setOPINION_CONTENT(String oPINION_CONTENT) {
		OPINION_CONTENT = oPINION_CONTENT;
	}

	@Column(name = "OPINION_INDEX")
	public Integer getOPINION_INDEX() {
		return OPINION_INDEX;
	}
	
	public void setOPINION_INDEX(Integer oPINION_INDEX) {
		OPINION_INDEX = oPINION_INDEX;
	}
	
	@Column(name = "OPINION_NAME")
	public String getOPINION_NAME() {
		return OPINION_NAME;
	}
	
	public void setOPINION_NAME(String oPINION_NAME) {
		OPINION_NAME = oPINION_NAME;
	}
	
	@Column(name = "OPINION_TYPE")
	public String getOPINION_TYPE() {
		return OPINION_TYPE;
	}
	
	public void setOPINION_TYPE(String oPINION_TYPE) {
		OPINION_TYPE = oPINION_TYPE;
	}
	
	@Column(name = "OPINIONTYPE_ID")
	public String getOPINIONTYPE_ID() {
		return OPINIONTYPE_ID;
	}
	
	public void setOPINIONTYPE_ID(String oPINIONTYPE_ID) {
		OPINIONTYPE_ID = oPINIONTYPE_ID;
	}
	
	@Column(name = "OPINION_NEED")
	public Integer getOPINION_NEED() {
		return OPINION_NEED;
	}
	
	public void setOPINION_NEED(Integer oPINION_NEED) {
		OPINION_NEED = oPINION_NEED;
	}
	
	@Column(name = "OPINION_BM")
	public String getOPINION_BM() {
		return OPINION_BM;
	}
	
	public void setOPINION_BM(String oPINION_BM) {
		OPINION_BM = oPINION_BM;
	}
	
	
	
}
