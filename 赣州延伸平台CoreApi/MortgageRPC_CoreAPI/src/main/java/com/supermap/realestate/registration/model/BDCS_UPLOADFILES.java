
/**
 * 
 * 代码生成器自动生成[WFI_MATERDATA]
 * 
 */

package com.supermap.realestate.registration.model;

import java.util.UUID;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDCS_UPLOADFILES",schema = "BDCK")
public class BDCS_UPLOADFILES  implements Serializable{

	private static final long serialVersionUID = 1L;
	private String File_Id;//文件编号
	private String Filetype_Id;//文件分类ID
	private String File_Pid;//文件上级分类ID
	private String Upload_Name;//上传人姓名
	private String Upload_Id;//上传人ID
	private Date Upload_Date;//上传日期
	private String File_Name;//文件名称
	private String File_Postfix;//文件格式
	private String File_Year;//文件年份
	private Short Storage_Type;//存储类型
	private String File_Path;//文件路径
	private Long File_Index;//文件序号
	private String Thumb;//缩略图
	private String File_Number;//项目编码
	private Integer Upload_Status;//上传状态（1、整理，2上传未整理）
	private String Path;//
	@Id
	@Column(name = "File_Id", length = 400)
	public String getFile_Id() {
		if (File_Id == null)
			File_Id = UUID.randomUUID().toString().replace("-", "");
		return File_Id;
	}

	public void setFile_Id(String File_Id) {
		this.File_Id = File_Id;
	}
	
	@Column(name = "Filetype_Id", length = 400)
	public String getFiletype_Id() {
		return Filetype_Id;
	}

	public void setFiletype_Id(String Filetype_Id) {
		this.Filetype_Id = Filetype_Id;
	}

	@Column(name = "File_Pid", length = 400)
	public String getFile_Pid() {
		return File_Pid;
	}

	public void setFile_Pid(String File_Pid) {
		this.File_Pid = File_Pid;
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

	public void setFile_Index(java.lang.Long l) {
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


}
