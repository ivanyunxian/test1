
/**
 * 
 * 代码生成器自动生成[WFI_MATERDATA]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_MATERDATA",schema = "BDC_WORKFLOW")
public class Wfi_MaterData implements Cloneable{

	private String Materialdata_Id;
	private String Materilinst_Id;
	private String Upload_Name;
	private String Upload_Id;
	private Date Upload_Date;
	private String File_Name;
	private String File_Postfix;
	private String File_Year;
	private Short Storage_Type;
	private String File_Path;
	private Long File_Index;
	private String Thumb;
	private String File_Number;
	private Integer Upload_Status;
	private String Path;
	private Integer Upload_Class;
	private String Disc;
	private String Upload_Type;
	@Id
	@Column(name = "MATERIALDATA_ID", length = 400)
	public String getMaterialdata_Id() {
		if (Materialdata_Id == null)
			Materialdata_Id = UUID.randomUUID().toString().replace("-", "");
		return Materialdata_Id;
	}

	public void setMaterialdata_Id(String Materialdata_Id) {
		this.Materialdata_Id = Materialdata_Id;
	}

	@Column(name = "MATERILINST_ID", length = 400)
	public String getMaterilinst_Id() {
		return Materilinst_Id;
	}

	public void setMaterilinst_Id(String Materilinst_Id) {
		this.Materilinst_Id = Materilinst_Id;
	}

	@Column(name = "UPLOAD_NAME", length = 400)
	public String getUpload_Name() {
		return Upload_Name;
	}

	public void setUpload_Name(String Upload_Name) {
		this.Upload_Name = Upload_Name;
	}

	@Column(name = "UPLOAD_ID", length = 400)
	public String getUpload_Id() {
		return Upload_Id;
	}

	public void setUpload_Id(String Upload_Id) {
		this.Upload_Id = Upload_Id;
	}

	@Column(name = "UPLOAD_DATE", length = 7)
	public Date getUpload_Date() {
		return Upload_Date;
	}

	public void setUpload_Date(Date Upload_Date) {
		this.Upload_Date = Upload_Date;
	}

	@Column(name = "FILE_NAME", length = 400)
	public String getFile_Name() {
		return File_Name;
	}

	public void setFile_Name(String File_Name) {
		this.File_Name = File_Name;
	}

	@Column(name = "FILE_POSTFIX", length = 40)
	public String getFile_Postfix() {
		return File_Postfix;
	}

	public void setFile_Postfix(String File_Postfix) {
		this.File_Postfix = File_Postfix;
	}

	@Column(name = "FILE_YEAR", length = 8)
	public String getFile_Year() {
		return File_Year;
	}

	public void setFile_Year(String File_Year) {
		this.File_Year = File_Year;
	}

	@Column(name = "STORAGE_TYPE")
	public Short getStorage_Type() {
		return Storage_Type;
	}

	public void setStorage_Type(Short Storage_Type) {
		this.Storage_Type = Storage_Type;
	}

	@Column(name = "FILE_PATH", length = 400)
	public String getFile_Path() {
		return File_Path;
	}

	public void setFile_Path(String File_Path) {
		this.File_Path = File_Path;
	}

	@Column(name = "FILE_INDEX")
	public Long getFile_Index() {
		return File_Index;
	}

	public void setFile_Index(long l) {
		this.File_Index = l;
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

	/**
	 * @return the file_Number
	 */
	@Column(name = "FILE_NUMBER")
	public String getFile_Number() {
		return File_Number;
	}

	/**
	 * @param file_Number the file_Number to set
	 */
	public void setFile_Number(String file_Number) {
		File_Number = file_Number;
	}

	/**
	 * @return the upload_Status
	 */
	@Column(name = "UPLOAD_STATUS")
	public Integer getUpload_Status() {
		return Upload_Status;
	}

	/**
	 * @param upload_Status the upload_Status to set
	 */
	public void setUpload_Status(Integer upload_Status) {
		Upload_Status = upload_Status;
	}
	@Column(name = "PATH", length = 1000)
	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}
	@Column(name = "UPLOAD_CLASS")
	public Integer getUpload_Class() {
		return Upload_Class;
	}

	public void setUpload_Class(Integer upload_Class) {
		Upload_Class = upload_Class;
	}

	@Column(name = "DISC")
	public String getDisc() {
		return Disc;
	}

	public void setDisc(String disc) {
		Disc = disc;
	}
	@Column(name = "UPLOAD_TYPE")
	public String getUpload_Type() {
		return Upload_Type;
	}

	public void setUpload_Type(String upload_Type) {
		Upload_Type = upload_Type;
	}

	@Override
	public Object clone(){
		Wfi_MaterData materdata = null;
		try {
			materdata = (Wfi_MaterData) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return materdata;
	}

}
