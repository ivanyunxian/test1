
/**
 * 
 * 代码生成器自动生成[WFI_ROUTE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_ROUTE",schema = "BDC_WORKFLOW")
public class Wfi_Route {

	private String Route_Id;
	private String Routeinst_Id;
	private String Next_Actinst_Id;
	private String Route_Name;
	private Integer Route_Status;
	private String Route_Value;
	private String Proinst_Id;
	private String Actdef_Id;
	private String Fromactinst_Id;
	private String Next_Actdef_Id;
	private Integer Instance_Type;
	private Date Craet_Time;

	@Id
	@Column(name = "ROUTEINST_ID", length = 400)
	public String getRouteinst_Id() {
		return Routeinst_Id;
	}

	public void setRouteinst_Id(String Routeinst_Id) {
		this.Routeinst_Id = Routeinst_Id;
	}
	@Column(name = "ROUTE_ID", length = 400)
	public String getRoute_Id() {
		if (Route_Id == null)
			Route_Id = UUID.randomUUID().toString().replace("-", "");
		return Route_Id;
	}
	

	public void setRoute_Id(String Route_Id) {
		this.Route_Id = Route_Id;
	}
	@Column(name = "CREAT_TIME", length = 7)
	public Date getCraet_Time() {
		return Craet_Time;
	}

	public void setCraet_Time(Date craet_Time) {
		Craet_Time = craet_Time;
	}
	

	@Column(name = "NEXT_ACTINST_ID", length = 400)
	public String getNext_Actinst_Id() {
		return Next_Actinst_Id;
	}

	public void setNext_Actinst_Id(String Next_Actinst_Id) {
		this.Next_Actinst_Id = Next_Actinst_Id;
	}

	@Column(name = "ROUTE_NAME", length = 600)
	public String getRoute_Name() {
		return Route_Name;
	}

	public void setRoute_Name(String Route_Name) {
		this.Route_Name = Route_Name;
	}

	@Column(name = "ROUTE_STATUS")
	public Integer getRoute_Status() {
		return Route_Status;
	}

	public void setRoute_Status(Integer Route_Status) {
		this.Route_Status = Route_Status;
	}

	@Column(name = "ROUTE_VALUE", length = 600)
	public String getRoute_Value() {
		return Route_Value;
	}

	public void setRoute_Value(String Route_Value) {
		this.Route_Value = Route_Value;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "FROMACTINST_ID", length = 400)
	public String getFromactinst_Id() {
		return Fromactinst_Id;
	}

	public void setFromactinst_Id(String Fromactinst_Id) {
		this.Fromactinst_Id = Fromactinst_Id;
	}

	@Column(name = "NEXT_ACTDEF_ID", length = 400)
	public String getNext_Actdef_Id() {
		return Next_Actdef_Id;
	}

	public void setNext_Actdef_Id(String Next_Actdef_Id) {
		this.Next_Actdef_Id = Next_Actdef_Id;
	}

	@Column(name = "INSTANCE_TYPE")
	public Integer getInstance_Type() {
		return Instance_Type;
	}

	public void setInstance_Type(Integer Instance_Type) {
		this.Instance_Type = Instance_Type;
	}

}
