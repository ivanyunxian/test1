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
@Table(name = "RT_ROLEPERMISSION")
public class RolePermission {
	
	private String id;
	private Role role;
    private SmodulePermission smodulePermission;
    private Date createTime;
	private String remark;
	private SystemModule systemModule;
	
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
	@JoinColumn(name = "ROLEID")
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	@ManyToOne
	@JoinColumn(name = "SYSTEMPERMISSIONID")
	public SmodulePermission getSmodulePermission() {
		return smodulePermission;
	}
	public void setSmodulePermission(SmodulePermission smodulePermission) {
		this.smodulePermission = smodulePermission;
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
	
	@ManyToOne
	@JoinColumn(name = "SYSMODULEID")
	public SystemModule getSystemModule() {
		return systemModule;
	}
	public void setSystemModule(SystemModule systemModule) {
		this.systemModule = systemModule;
	}
}
