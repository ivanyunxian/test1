
/**
 * 
 * 代码生成器自动生成[BDCS_COMPACT_OWNERINFO]
 * 
 */

package com.supermap.luzhouothers.model;

import java.util.UUID;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDCS_COMPACT_OWNERINFO", schema = "BDCK")
public class BDCS_COMPACT_OWNERINFO {

	private String Ownerid;
	private Integer Id;
	private String Owner_Name;
	private Date Birthday;
	private String Idtype;
	private String Idno;
	private String Buyerattr;
	private String Address;
	private Integer Owner_Type;

	@Id
	@Column(name = "OWNERID", length = 32)
	public String getOwnerid() {
		if (Ownerid == null)
			Ownerid = UUID.randomUUID().toString().replace("-", "");
		return Ownerid;
	}

	public void setOwnerid(String Ownerid) {
		this.Ownerid = Ownerid;
	}

	@Column(name = "ID")
	public Integer getId() {
		return Id;
	}

	public void setId(Integer Id) {
		this.Id = Id;
	}

	@Column(name = "OWNER_NAME", length = 200)
	public String getOwner_Name() {
		return Owner_Name;
	}

	public void setOwner_Name(String Owner_Name) {
		this.Owner_Name = Owner_Name;
	}

	@Column(name = "BIRTHDAY", length = 7)
	public Date getBirthday() {
		return Birthday;
	}

	public void setBirthday(Date Birthday) {
		this.Birthday = Birthday;
	}

	@Column(name = "IDTYPE", length = 80)
	public String getIdtype() {
		return Idtype;
	}

	public void setIdtype(String Idtype) {
		this.Idtype = Idtype;
	}

	@Column(name = "IDNO", length = 80)
	public String getIdno() {
		return Idno;
	}

	public void setIdno(String Idno) {
		this.Idno = Idno;
	}

	@Column(name = "BUYERATTR", length = 200)
	public String getBuyerattr() {
		return Buyerattr;
	}

	public void setBuyerattr(String Buyerattr) {
		this.Buyerattr = Buyerattr;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	@Column(name = "OWNER_TYPE")
	public Integer getOwner_Type() {
		return Owner_Type;
	}

	public void setOwner_Type(Integer Owner_Type) {
		this.Owner_Type = Owner_Type;
	}

}
