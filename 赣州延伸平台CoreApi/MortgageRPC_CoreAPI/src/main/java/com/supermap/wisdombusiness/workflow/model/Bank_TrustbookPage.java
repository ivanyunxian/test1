package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Bank_TrustbookPage",schema = "BDC_WORKFLOW")
public class Bank_TrustbookPage {

	private String Trustbookdata_Id;
	private String Trustbookpage_Id;
	private String Upload_Name;
	private Date Upload_Date;
	private String Trustbookpage_Path;
	private Short Trustbookpage_Index;
	private String Thumb;
	@Id
	@Column(name = "TRUSTBOOKDATA_ID", length = 400)
	public String getTrustbookdata_id() {
		if (Trustbookdata_Id == null)
			Trustbookdata_Id = UUID.randomUUID().toString().replace("-", "");
		return Trustbookdata_Id;
		
	}

	public void setTrustbookdata_id(String Trustbookdata_Id) {
		this.Trustbookdata_Id = Trustbookdata_Id;
	}

	@Column(name = "TRUSTBOOKPAGE_ID", length = 400)
	public String getTrustbookpage_Id() {
		return Trustbookpage_Id;
	}

	public void setTrustbookpage_Id(String Trustbookpage_Id) {
		this.Trustbookpage_Id = Trustbookpage_Id;
	}

	@Column(name = "UPLOAD_NAME", length = 400)
	public String getUpload_Name() {
		return Upload_Name;
	}

	public void setUpload_Name(String Upload_Name) {
		this.Upload_Name = Upload_Name;
	}


	@Column(name = "UPLOAD_DATE", length = 7)
	public Date getUpload_Date() {
		return Upload_Date;
	}

	public void setUpload_Date(Date Upload_Date) {
		this.Upload_Date = Upload_Date;
	}

	@Column(name = "TRUSTBOOKPAGE_PATH", length = 400)
	public String getTrustbookpage_Path() {
		return Trustbookpage_Path;
	}

	public void setTrustbookpage_Path(String Trustbookpage_Path) {
		this.Trustbookpage_Path = Trustbookpage_Path;
	}

	@Column(name = "TRUSTBOOKPAGE_INDEX")
	public Short getTrustbookpage_Index() {
		return Trustbookpage_Index;
	}

	public void setTrustbookpage_Index(Short Trustbookpage_Index) {
		this.Trustbookpage_Index = Trustbookpage_Index;
	}

	/**
	 * @return the thumb
	 */
	@Column(name = "THUMB")
	public String getThumb() {
		return Thumb;
	}

	/**
	 * @param thumb the thumb to set
	 */
	public void setThumb(String thumb) {
		Thumb = thumb;
	}
}
