package com.supermap.wisdombusiness.framework.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chenhl
 * 
 */

@Entity
@Table(name = "T_SYSTEM")
public class System {
	
	private String id;
    private String sysName;
    private String description;
	private String sysUrl;
	private Date createTime;
	private String createUserId;
	private String createUserName;
	private String remark;
	private String iconname;
	
	@Id
	@Column(name = "ID", length = 32)
	public String getId() {
		if (id == null) id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "SYSTEMNAME",  length = 200)
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	
	@Column(name = "DESCRIPTION",  length = 200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "SYSTEMURL",  length = 200)
	public String getSysUrl() {
		return sysUrl;
	}
	public void setSysUrl(String sysUrl) {
		this.sysUrl = sysUrl;
	}
	
	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "CREATEUSERID" ,  length = 32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATEUSERNAME" ,  length = 32)
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
	@Column(name = "REMARK",  length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return iconname
	 */
	@Column(name = "ICONNAME",  length = 50)
	public String getIconname() {
		return iconname;
	}

	/**
	 * @param iconname 要设置的 iconname
	 */
	public void setIconname(String iconname) {
		this.iconname = iconname;
	}
}
