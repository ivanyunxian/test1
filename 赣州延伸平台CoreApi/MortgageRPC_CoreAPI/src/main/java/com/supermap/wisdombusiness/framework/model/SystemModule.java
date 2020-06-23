package com.supermap.wisdombusiness.framework.model;

import java.util.List;
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
@Table(name = "T_SYSTEMMODULE")
public class SystemModule {
	
	private String id;
    private System system;
	private String sysModuleName;
	private String sysModuleUrl;
	private String description;
	private String parentId;
	private String remark;
	private String iconname;
	private String sort;
	
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
	@JoinColumn(name = "SYSTEMID")
	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}
	

	@Column(name = "SYSMODULENAME",  length = 200)
	public String getSysModuleName() {
		return sysModuleName;
	}
	public void setSysModuleName(String sysModuleName) {
		this.sysModuleName = sysModuleName;
	}
	
	@Column(name = "SYSMODULEURL",  length = 200)
	public String getSysModuleUrl() {
		return sysModuleUrl;
	}
	public void setSysModuleUrl(String sysModuleUrl) {
		this.sysModuleUrl = sysModuleUrl;
	}
	
	@Column(name = "DESCRIPTION",  length = 200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "PARENTID",  length = 32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "REMARK",  length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "SORT",  length = 50)
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public List<SystemModule> children;

}
