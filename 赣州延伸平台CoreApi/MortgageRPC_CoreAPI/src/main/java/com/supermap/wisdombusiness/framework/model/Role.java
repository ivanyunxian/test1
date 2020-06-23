package com.supermap.wisdombusiness.framework.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author chenhl
 * 
 */

@Entity
@Table(name = "T_ROLE")
public class Role {
	
	private String id;
    private RoleGroup roleGroup;
    private String roleName;
    private RoleType roleType = RoleType.YB;
    private String areaCode;
    private String description;
    private Date createTime;
	private String remark;
	private String groupId;
	private String groupName;
	
	private String roleCode;
	
	
	
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
	@JoinColumn(name = "GROUPID")
	public RoleGroup getRoleGroup() {
		return roleGroup;
	}

	public void setRoleGroup(RoleGroup roleGroup) {
		this.roleGroup = roleGroup;
	}

	@Column(name = "ROLENAME",  length = 50)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ROLETYPE", length = 32)
	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	@Column(name = "AREACODE",  length = 50)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "DESCRIPTION",  length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Transient
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Transient
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name = "ROLECODE")
	public String getRoleCode() {
		return roleCode;
	}
    
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public enum RoleType
	{
		YB("一般角色") {},
		BM("部门角色") {};

		private String value;

		private RoleType(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

}
