
/**
 * 
 * 代码生成器自动生成[WFD_CONST_VALUE]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_CONST_VALUE",schema = "BDC_WORKFLOW")
public class Wfd_Const_Value {

	private String Constvalue_Id;
	private String Const_Value;
	private String Const_Desc;
	private String Const_Id;
	private Integer Const_Order;

	@Id
	@Column(name = "CONSTVALUE_ID", length = 400)
	public String getConstvalue_Id() {
		if (Constvalue_Id == null)
			Constvalue_Id = UUID.randomUUID().toString().replace("-", "");
		return Constvalue_Id;
	}

	public void setConstvalue_Id(String Constvalue_Id) {
		this.Constvalue_Id = Constvalue_Id;
	}

	@Column(name = "CONST_VALUE", length = 400)
	public String getConst_Value() {
		return Const_Value;
	}

	public void setConst_Value(String Const_Value) {
		this.Const_Value = Const_Value;
	}

	@Column(name = "CONST_DESC", length = 400)
	public String getConst_Desc() {
		return Const_Desc;
	}

	public void setConst_Desc(String Const_Desc) {
		this.Const_Desc = Const_Desc;
	}

	@Column(name = "CONST_ID", length = 400)
	public String getConst_Id() {
		return Const_Id;
	}

	public void setConst_Id(String Const_Id) {
		this.Const_Id = Const_Id;
	}

	@Column(name = "CONST_ORDER")
	public Integer getConst_Order() {
		return Const_Order;
	}

	public void setConst_Order(Integer Const_Order) {
		this.Const_Order = Const_Order;
	}

}
