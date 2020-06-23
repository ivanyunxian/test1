package com.supermap.wisdombusiness.framework.model;

import java.io.Serializable;
import java.util.List;
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
@Table(name = "T_DEPARTMENT")
public class Department implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7764347302540933057L;
	
	private String id;
	private String departmentName;
	private String parentId;
	private String areaCode;
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
	
	@Column(name = "DEPARTMENTNAME", length = 200)
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	@Column(name = "PARENTID", length = 32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "AREACODE", length = 20)
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@Column(name = "REMARK",  length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public List<Department> children;
}
