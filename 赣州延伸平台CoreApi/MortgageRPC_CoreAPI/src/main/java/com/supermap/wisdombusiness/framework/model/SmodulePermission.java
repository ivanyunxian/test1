package com.supermap.wisdombusiness.framework.model;

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
@Table(name = "T_SYSTEMMODULEPERMISSION")
public class SmodulePermission {
	
	private String id;
    private SystemModule systemModule;
    private String permissionName ;
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
	
	@Column(name = "PERMISSIONNAME", length = 200)
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	
	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
