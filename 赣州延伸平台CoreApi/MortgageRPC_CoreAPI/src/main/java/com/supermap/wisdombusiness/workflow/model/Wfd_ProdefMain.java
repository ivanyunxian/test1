
/**
 * 
 * 代码生成器自动生成[WFD_PRODEFMAIN]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_PRODEFMAIN",schema = "BDC_WORKFLOW")
public class Wfd_ProdefMain {

	private String Prodef_Id;
	private Integer Dept_Code;
	private String Operation_Type;
	private String Prodef_Code;
	private String Prodef_Desc;
	private Integer Prodef_Index;
	private String Prodef_Name;
	private Integer Prodef_Status;
	private Integer Prodef_Time;
	private Integer Transaction_Type;
	private String Version;

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

	@Column(name = "DEPT_CODE")
	public Integer getDept_Code() {
		return Dept_Code;
	}

	public void setDept_Code(Integer Dept_Code) {
		this.Dept_Code = Dept_Code;
	}

	@Column(name = "OPERATION_TYPE", length = 600)
	public String getOperation_Type() {
		return Operation_Type;
	}

	public void setOperation_Type(String Operation_Type) {
		this.Operation_Type = Operation_Type;
	}

	@Column(name = "PRODEF_CODE", length = 600)
	public String getProdef_Code() {
		return Prodef_Code;
	}

	public void setProdef_Code(String Prodef_Code) {
		this.Prodef_Code = Prodef_Code;
	}

	@Column(name = "PRODEF_DESC", length = 2000)
	public String getProdef_Desc() {
		return Prodef_Desc;
	}

	public void setProdef_Desc(String Prodef_Desc) {
		this.Prodef_Desc = Prodef_Desc;
	}

	@Column(name = "PRODEF_INDEX")
	public Integer getProdef_Index() {
		return Prodef_Index;
	}

	public void setProdef_Index(Integer Prodef_Index) {
		this.Prodef_Index = Prodef_Index;
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

	@Column(name = "PRODEF_TIME")
	public Integer getProdef_Time() {
		return Prodef_Time;
	}

	public void setProdef_Time(Integer Prodef_Time) {
		this.Prodef_Time = Prodef_Time;
	}

	@Column(name = "TRANSACTION_TYPE")
	public Integer getTransaction_Type() {
		return Transaction_Type;
	}

	public void setTransaction_Type(Integer Transaction_Type) {
		this.Transaction_Type = Transaction_Type;
	}

	@Column(name = "VERSION", length = 600)
	public String getVersion() {
		return Version;
	}

	public void setVersion(String Version) {
		this.Version = Version;
	}

}
