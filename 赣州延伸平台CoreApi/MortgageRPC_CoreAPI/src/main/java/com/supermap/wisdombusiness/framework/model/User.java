package com.supermap.wisdombusiness.framework.model;

import java.io.Serializable;
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

import com.supermap.realestate.registration.util.ConstHelper;

/**
 * 用户
 * 
 * @author chenhl
 * 
 */

@Entity
@Table(name = "T_USER")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8123496068624886983L;

	private String id;
	private Department department;
	private String loginName;
	private String password;
	private String userName;
	private int male;
	private String mobile;
	private String tel;
	private String areaCode;
	private UserStatus status = UserStatus.NORMAL;
	private String remark;
	private String departmentId;
	private String departmentName;
	private String roleId;
	private String sIGN;
	private String isLeader = "0";

	private String maleName;
	private String isLand;
	private String auserIp;

	private String orderIndex;
	private String identifyCn;
	
	private String ispj;
	
	
	@Column(name="ORDINDEX")
	public String getOrderIndex() {
			return orderIndex;
	}
	public void setOrderIndex(String orderIndex) {
			this.orderIndex = orderIndex;
	}
	@Transient
	public String getMaleName() {

		if (maleName == null) {

			maleName = ConstHelper.getNameByValue("XB",
					String.valueOf(this.getMale()));

		}

		return maleName;
	}

	private String statusName;

	@Transient
	public String getStatusName() {

		if (statusName == null) {
			if (this.getStatus() != null) {

				statusName = this.getStatus().value; // ConstHelper.getNameByValue("XB",
														// String.valueOf(this.getMale()));
			}
		}

		return statusName;
	}

	@Id
	@Column(name = "ID", length = 32)
	public String getId() {
		if (id == null)
			id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "DEPARTMENTID")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "LOGINNAME", length = 32)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "PASSWORD", length = 64)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "USERNAME", length = 50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "SIGN", length = 50)
	public String getSIGN() {
		return sIGN;
	}

	public void setSIGN(String sIGN) {
		this.sIGN = sIGN;
	}

	@Column(name = "MALE")
	public int getMale() {
		return male;
	}

	public void setMale(int male) {
		this.male = male;
	}

	@Column(name = "MOBILE", length = 20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "TEL", length = 20)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "AREACODE", length = 20)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20)
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Transient
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Transient
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ISLEADER")
	public String getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}
	@Column(name = "ISLAND", length = 20)
	public String getIsLand() {
		return isLand;
	}

	public void setIsLand(String isLand) {
		this.isLand = isLand;
	}
	
	public enum UserStatus {
		NORMAL("正常") {
		},
		INVALID("禁用") {
		};

		private String value;

		private UserStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	@Column(name = "AUSERIP", length = 2000)
	public String getAuserIp() {
		return auserIp;
	}
	public void setAuserIp(String auserIp) {
		this.auserIp = auserIp;
	}
	
	@Column(name = "IDENTIFYCN", length = 2000)
	public String getIdentifyCn() {
		return identifyCn;
	}
	public void setIdentifyCn(String identifyCn) {
		this.identifyCn = identifyCn;
	}
	
	public String getIspj() {
		return ispj;
	}
	public void setIspj(String ispj) {
		this.ispj = ispj;
	}
	
}
