
/**
 * 
 * 代码生成器自动生成[WFI_SPDY]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_SPDY",schema = "BDC_WORKFLOW")
public class Wfi_Spdy {

	private String Spdy_Id;
	private String Spmc;
	private String Splx;
	private Integer Signtype;
	private Integer Status;
	private String Mryj;

	@Id
	@Column(name = "SPDY_ID", length = 200)
	public String getSpdy_Id() {
		if (Spdy_Id == null)
			Spdy_Id = UUID.randomUUID().toString().replace("-", "");
		return Spdy_Id;
	}

	public void setSpdy_Id(String Spdy_Id) {
		this.Spdy_Id = Spdy_Id;
	}

	@Column(name = "SPMC", length = 400)
	public String getSpmc() {
		return Spmc;
	}

	public void setSpmc(String Spmc) {
		this.Spmc = Spmc;
	}

	@Column(name = "SPLX", length = 200)
	public String getSplx() {
		return Splx;
	}

	public void setSplx(String Splx) {
		this.Splx = Splx;
	}

	@Column(name = "SIGNTYPE")
	public Integer getSigntype() {
		return Signtype;
	}

	public void setSigntype(Integer Signtype) {
		this.Signtype = Signtype;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}
	@Column(name = "MRYJ", length = 200)
	public String getMryj() {
		return Mryj;
	}

	public void setMryj(String mryj) {
		Mryj = mryj;
	}

}
