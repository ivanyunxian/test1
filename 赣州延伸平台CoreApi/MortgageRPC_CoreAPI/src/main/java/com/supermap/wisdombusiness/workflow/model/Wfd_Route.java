
/**
 * 
 * 代码生成器自动生成[WFD_ROUTE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_ROUTE",schema = "BDC_WORKFLOW")
public class Wfd_Route {

	private String Route_Id;
	private String Route_Name;
	private String Actdef_Id;
	private String Next_Actdef_Id;
	private String Route_Value;
	private Integer Route_Status;
	private String Prodef_Id;

	@Id
	@Column(name = "ROUTE_ID", length = 400)
	public String getRoute_Id() {
		if (Route_Id == null)
			Route_Id = UUID.randomUUID().toString().replace("-", "");
		return Route_Id;
	}

	public void setRoute_Id(String Route_Id) {
		this.Route_Id = Route_Id;
	}

	@Column(name = "ROUTE_NAME", length = 600)
	public String getRoute_Name() {
		return Route_Name;
	}

	public void setRoute_Name(String Route_Name) {
		this.Route_Name = Route_Name;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "NEXT_ACTDEF_ID", length = 400)
	public String getNext_Actdef_Id() {
		return Next_Actdef_Id;
	}

	public void setNext_Actdef_Id(String Next_Actdef_Id) {
		this.Next_Actdef_Id = Next_Actdef_Id;
	}

	@Column(name = "ROUTE_VALUE", length = 600)
	public String getRoute_Value() {
		return Route_Value;
	}

	public void setRoute_Value(String Route_Value) {
		this.Route_Value = Route_Value;
	}

	@Column(name = "ROUTE_STATUS")
	public Integer getRoute_Status() {
		return Route_Status;
	}

	public void setRoute_Status(Integer Route_Status) {
		this.Route_Status = Route_Status;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

}
