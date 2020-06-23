
/**
 * 
 * 代码生成器自动生成[WFD_STAFFTOROLE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_STAFFTOROLE",schema = "BDC_WORKFLOW")
public class Wfd_StaffToRole {

	private String Staffrole_Id;
	private String Staff_Id;
	private String Role_Id;
	private String Staff_name;

	@Id
	@Column(name = "STAFFROLE_ID", length = 400)
	public String getStaffrole_Id() {
		if (Staffrole_Id == null)
			Staffrole_Id = UUID.randomUUID().toString().replace("-", "");
		return Staffrole_Id;
	}

	public void setStaffrole_Id(String Staffrole_Id) {
		this.Staffrole_Id = Staffrole_Id;
	}

	@Column(name = "STAFF_ID", length = 400)
	public String getStaff_Id() {
		return Staff_Id;
	}

	public void setStaff_Id(String Staff_Id) {
		this.Staff_Id = Staff_Id;
	}

	@Column(name = "ROLE_ID", length = 400)
	public String getRole_Id() {
		return Role_Id;
	}

	public void setRole_Id(String Role_Id) {
		this.Role_Id = Role_Id;
	}

	/**
	 * @return the staff_name
	 */
	@Column(name = "STAFF_NAME", length = 400)
	public String getStaff_name() {
		return Staff_name;
	}

	/**
	 * @param staff_name the staff_name to set
	 */
	public void setStaff_name(String staff_name) {
		Staff_name = staff_name;
	}

}
