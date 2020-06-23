
/**
 * 
 * 代码生成器自动生成[WFD_PRODEF]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "WFD_PRODEF",schema = "BDC_WORKFLOW")
public class Wfd_Prodef {

	private String Prodef_Id;
	private String Prodef_Code;
	private String Prodef_Name;
	private Integer Prodef_Status;
	private Integer Prodef_Index;
	private Integer Transaction_Type;
	private Double Prodef_Time;
	private String Operation_Type;
	private Integer Dept_Code;
	private String Prodef_Desc;
	private String Version;
	private String Prodefclass_Id;
	private String Prodef_Mapping;
	
	
	private Integer ToInlin;
	private String Prodef_Tpl;
	private Integer Show_Status;
	private String Sourse_Id;

	//区分天数计算工作流与小时计算工作流标志
	private Integer House_Status;
	
	@Column(name = "HOUSE_STATUS")
	public Integer getHouse_Status() {
		return House_Status;
	}

	public void setHouse_Status(Integer house_Status) {
		this.House_Status = house_Status;
	}

	@Id
	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		if (Prodef_Id == null)
			Prodef_Id = UUID.randomUUID().toString().replace("-", "");
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

	@Column(name = "PRODEF_CODE", length = 600)
	public String getProdef_Code() {
		return Prodef_Code;
	}

	public void setProdef_Code(String Prodef_Code) {
		this.Prodef_Code = Prodef_Code;
	}

	@Column(name = "PRODEF_NAME", length = 600)
	public String getProdef_Name() {
		return Prodef_Name;
	}

	public void setProdef_Name(String Prodef_Name) {
		this.Prodef_Name = Prodef_Name;
	}

	@Column(name = "PRODEF_STATUS")
	public Integer getProdef_Status() {
		return Prodef_Status;
	}

	public void setProdef_Status(Integer Prodef_Status) {
		this.Prodef_Status = Prodef_Status;
	}

	@Column(name = "PRODEF_INDEX")
	public Integer getProdef_Index() {
		return Prodef_Index;
	}

	public void setProdef_Index(Integer Prodef_Index) {
		this.Prodef_Index = Prodef_Index;
	}

	@Column(name = "TRANSACTION_TYPE")
	public Integer getTransaction_Type() {
		return Transaction_Type;
	}

	public void setTransaction_Type(Integer Transaction_Type) {
		this.Transaction_Type = Transaction_Type;
	}

	@Column(name = "PRODEF_TIME")
	public Double getProdef_Time() {
		return Prodef_Time;
	}

	public void setProdef_Time(Double Prodef_Time) {
		this.Prodef_Time = Prodef_Time;
	}

	@Column(name = "OPERATION_TYPE", length = 600)
	public String getOperation_Type() {
		return Operation_Type;
	}

	public void setOperation_Type(String Operation_Type) {
		this.Operation_Type = Operation_Type;
	}

	@Column(name = "DEPT_CODE")
	public Integer getDept_Code() {
		return Dept_Code;
	}

	public void setDept_Code(Integer Dept_Code) {
		this.Dept_Code = Dept_Code;
	}

	@Column(name = "PRODEF_DESC", length = 2000)
	public String getProdef_Desc() {
		return Prodef_Desc;
	}

	public void setProdef_Desc(String Prodef_Desc) {
		this.Prodef_Desc = Prodef_Desc;
	}

	@Column(name = "VERSION", length = 600)
	public String getVersion() {
		return Version;
	}
	public void setVersion(String Version) {
		this.Version = Version;
	}

	@Column(name = "PRODEFCLASS_ID", length = 400)
	public String getProdefclass_Id() {
		return Prodefclass_Id;
	}

	public void setProdefclass_Id(String Prodefclass_Id) {
		this.Prodefclass_Id = Prodefclass_Id;
	}
	@Column(name = "PRODEF_MAPPING", length = 400)
	public String getProdef_Mapping() {
		return Prodef_Mapping;
	}

	public void setProdef_Mapping(String prodef_Mapping) {
		Prodef_Mapping = prodef_Mapping;
	}
	
	@Column(name = "TOINLIN")
	public Integer getToInlin() {
		return ToInlin;
	}

	public void setToInlin(Integer toInlin) {
		ToInlin = toInlin;
	}

	@Column(name = "PRODEF_TPL",length = 400)
	public String getProdef_Tpl() {
		return Prodef_Tpl;
	}

	public void setProdef_Tpl(String prodef_Tpl) {
		Prodef_Tpl = prodef_Tpl;
	}

	@Column(name = "SHOW_STATUS")
	public Integer getShow_Status() {
		return Show_Status;
	}

	public void setShow_Status(Integer show_Status) {
		Show_Status = show_Status;
	}

	@Column(name = "SOURSE_ID")
	public String getSourse_Id() {
		return Sourse_Id;
	}

	public void setSourse_Id(String sourse_Id) {
		Sourse_Id = sourse_Id;
	}

}
