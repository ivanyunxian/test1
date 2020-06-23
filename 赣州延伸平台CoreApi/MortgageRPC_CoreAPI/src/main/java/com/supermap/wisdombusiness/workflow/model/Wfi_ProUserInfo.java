package com.supermap.wisdombusiness.workflow.model;

import java.io.Serializable;
import java.util.UUID;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_PROUSERINFO",schema = "BDC_WORKFLOW")
public class Wfi_ProUserInfo {

	private String ProUserInfo_Id;
	private String File_Number;
	private String UserName;
	private String Tel;
	private String Area;
	private String Address;
	private String ProType;
	private String Bdcqzh;
	private String Bdcdyzl;

	
	public Wfi_ProUserInfo(String proUserInfo_Id, String file_Number,
			String userName, String tel, String area, String address,
			String proType, String bdcqzh, String bdcdyzl) {
		super();
		ProUserInfo_Id = proUserInfo_Id;
		File_Number = file_Number;
		UserName = userName;
		Tel = tel;
		Area = area;
		Address = address;
		ProType = proType;
		Bdcqzh = bdcqzh;
		Bdcdyzl = bdcdyzl;
	}
	public Wfi_ProUserInfo () {
		
	}

	@Id
	@Column(name = "PROUSERINFO_ID", length = 400)
	public String getProUserInfo_Id() {
		if (ProUserInfo_Id == null){
			ProUserInfo_Id = UUID.randomUUID().toString().replace("-", "");
		}
		return ProUserInfo_Id;
	}
	
	public void setProUserInfo_Id(String ProUserInfo_Id) {
		this.ProUserInfo_Id = ProUserInfo_Id;
	}
	
	@Column(name = "FILE_NUMBER", length = 400)
	public String getFile_Number() {
		return File_Number;
	}

	public void setFile_Number(String File_Number) {
		this.File_Number = File_Number;
	}
	
	@Column(name = "USERNAME", length = 400)
	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}
	
	@Column(name = "TEL", length = 400)
	public String getTel() {
		return Tel;
	}

	public void setTel(String Tel) {
		this.Tel = Tel;
	}
	
	@Column(name = "AREA", length = 400)
	public String getArea() {
		return Area;
	}

	public void setArea(String Area) {
		this.Area = Area;
	}
	
	@Column(name = "ADDRESS", length = 400)
	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}
	
	@Column(name = "PROTYPE", length = 400)
	public String getProType() {
		return ProType;
	}

	public void setProType(String ProType) {
		this.ProType = ProType;
	}
	
	@Column(name = "BDCQZH", length = 400)
	public String getBdcqzh() {
		return Bdcqzh;
	}

	public void setBdcqzh(String Bdcqzh) {
		this.Bdcqzh = Bdcqzh;
	}
	
	@Column(name = "BDCDYZL", length = 400)
	public String getBdcdyzl() {
		return Bdcdyzl;
	}

	public void setBdcdyzl(String Bdcdyzl) {
		this.Bdcdyzl = Bdcdyzl;
	}
}
