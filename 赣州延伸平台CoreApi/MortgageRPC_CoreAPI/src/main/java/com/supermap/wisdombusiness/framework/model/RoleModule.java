package com.supermap.wisdombusiness.framework.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author chenhl
 * 
 */

@Entity
@Table(name = "RT_ROLEMODULE")
public class RoleModule {
	
	private String id;
    private SystemModule systemModule;
    private Role role;
    private Date createTime;
	private String remark;
	
	@Id
	@Column(name = "ID", length = 32)
	public String getId() {
		if (id == null) id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "SYSMODULEID")
	public SystemModule getSystemModule() {
		return systemModule;
	}
	public void setSystemModule(SystemModule systemModule) {
		this.systemModule = systemModule;
	}
	
	@ManyToOne
	@JoinColumn(name = "ROLEID")
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "REMARK",  length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
