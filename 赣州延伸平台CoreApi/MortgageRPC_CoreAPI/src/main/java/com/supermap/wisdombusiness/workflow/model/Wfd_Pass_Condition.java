package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_PASS_CONDITION",schema = "BDC_WORKFLOW")
public class Wfd_Pass_Condition {

	private String Pass_Condition_Id;
	private String Actdef_Id;
	private String Prodef_Id;
	private String Route_Id;
	private String Condition_Name;
	private String Condition_Type;
	private String Condition_Param;
	private String Route_Type;

	@Id
	@Column(name = "PASS_CONDITION_ID", length = 400)
	public String getPass_Condition_Id() {
		if (Pass_Condition_Id == null)
			Pass_Condition_Id = UUID.randomUUID().toString().replace("-", "");
		return Pass_Condition_Id;
	}

	public void setPass_Condition_Id(String Pass_Condition_Id) {
		this.Pass_Condition_Id = Pass_Condition_Id;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

	@Column(name = "ROUTE_ID", length = 200)
	public String getRoute_Id() {
		return Route_Id;
	}

	public void setRoute_Id(String Route_Id) {
		this.Route_Id = Route_Id;
	}

	@Column(name = "CONDITION_NAME", length = 400)
	public String getCondition_Name() {
		return Condition_Name;
	}

	public void setCondition_Name(String Condition_Name) {
		this.Condition_Name = Condition_Name;
	}

	@Column(name = "CONDITION_TYPE", length = 200)
	public String getCondition_Type() {
		return Condition_Type;
	}

	public void setCondition_Type(String Condition_Type) {
		this.Condition_Type = Condition_Type;
	}

	@Column(name = "CONDITION_PARAM", length = 400)
	public String getCondition_Param() {
		return Condition_Param;
	}

	public void setCondition_Param(String Condition_Param) {
		this.Condition_Param = Condition_Param;
	}
	
	@Column(name = "ROUTE_TYPE", length = 10)
	public String getRoute_Type() {
		return Route_Type;
	}

	public void setRoute_Type(String Route_Type) {
		this.Route_Type = Route_Type;
	}

}
