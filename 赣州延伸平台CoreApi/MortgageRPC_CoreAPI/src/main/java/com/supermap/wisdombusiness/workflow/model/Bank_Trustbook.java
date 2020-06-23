package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Bank_Trustbook",schema = "BDC_WORKFLOW")
public class Bank_Trustbook {
	
	private String Trustbookpage_Id;
	private String Bank_Name;
	private String Trustbook_Id;
	private String Trustbook_Desc;
	private String Trustbook_Pagecount;
	private Date Trustbook_Date;
	private String Trustor_Name;
	private String Trustor_Tel;
	private String Trustor_Adrs;
	private String Trustor_Desc;
	private String Img_Path;
	private String Gdbz;
	
	@Id
	@Column(name = "TRUSTBOOK_ID", length = 400)
	public String getTrustbook_Id() {
		return Trustbook_Id;
	}

	public void setTrustbook_Id(String Trustbook_Id) {
		this.Trustbook_Id = Trustbook_Id;
	}
	
	@Column(name = "TRUSTBOOKPAGE_ID", length = 400)
	public String getTrustbookpage_Id() {
		if (Trustbookpage_Id == null)
			Trustbookpage_Id = UUID.randomUUID().toString().replace("-", "");
		return Trustbookpage_Id;
	}

	public void setTrustbookpage_Id(String Trustbookpage_Id) {
		this.Trustbookpage_Id = Trustbookpage_Id;
	}

	@Column(name = "BANK_NAME", length = 400)
	public String getBank_Name() {
		return Bank_Name;
	}

	public void setBank_Name(String Bank_Name) {
		this.Bank_Name = Bank_Name;
	}

	@Column(name = "TRUSTBOOK_DESC", length = 400)
	public String getTrustbook_Desc() {
		return Trustbook_Desc;
	}

	public void setTrustbook_Desc(String Trustbook_Desc) {
		this.Trustbook_Desc = Trustbook_Desc;
	}

	@Column(name = "TRUSTBOOK_PAGECOUNT", length = 400)
	public String getTrustbook_Pagecount() {
		return Trustbook_Pagecount;
	}

	public void setTrustbook_Pagecount(String Trustbook_Pagecount) {
		this.Trustbook_Pagecount = Trustbook_Pagecount;
	}

	@Column(name = "TRUSTBOOK_DATE", length = 400)
	public Date getTrustbook_Date() {
		return Trustbook_Date;
	}

	public void setTrustbook_Date(Date Trustbook_Date) {
		this.Trustbook_Date = Trustbook_Date;
	}

	@Column(name = "TRUSTOR_NAME", length = 400)
	public String getTrustor_Name() {
		return Trustor_Name;
	}

	public void setTrustor_Name(String Trustor_Name) {
		this.Trustor_Name = Trustor_Name;
	}


	@Column(name = "TRUSTOR_TEL", length = 7)
	public String getTrustor_Tel() {
		return Trustor_Tel;
	}

	public void setTrustor_Tel(String Trustor_Tel) {
		this.Trustor_Tel = Trustor_Tel;
	}

	@Column(name = "TRUSTOR_ADRS", length = 400)
	public String getTrustor_Adrs() {
		return Trustor_Adrs;
	}

	public void setTrustor_Adrs(String Trustor_Adrs) {
		this.Trustor_Adrs = Trustor_Adrs;
	}

	@Column(name = "TRUSTOR_DESC")
	public String getTrustor_Desc() {
		return Trustor_Desc;
	}

	public void setTrustor_Desc(String Trustor_Desc) {
		this.Trustor_Desc = Trustor_Desc;
	}
	
	@Column(name = "IMG_PATH")
	public String getImg_Path() {
		return Img_Path;
	}

	public void setImg_Path(String Img_Path) {
		this.Img_Path = Img_Path;
	}
	@Column(name = "GDBZ")
	public String getGdbz() {
		return Gdbz;
	}

	public void setGdbz(String Gdbz) {
		this.Gdbz = Gdbz;
	}
}
