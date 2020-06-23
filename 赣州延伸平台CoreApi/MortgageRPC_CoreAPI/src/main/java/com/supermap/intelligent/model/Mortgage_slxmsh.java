package com.supermap.intelligent.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "WFI_SLXMSH", schema = "BDC_MRPC")
public class Mortgage_slxmsh implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String Prolsh;
	private String shry;
	private String shyj;
	private Date shsj;
	private String shzt;

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PROLSH")
	public String getProlsh() {
		return Prolsh;
	}

	public void setProlsh(String prolsh) {
		Prolsh = prolsh;
	}

	@Column(name = "SHRY")
	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	@Column(name = "SHYJ")
	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	@Column(name = "SHSJ")
	public Date getShsj() {
		return shsj;
	}

	public void setShsj(Date shsj) {
		this.shsj = shsj;
	}

	@Column(name = "SHZT")
	public String getShzt() {
		return shzt;
	}

	public void setShzt(String shzt) {
		this.shzt = shzt;
	}
}
