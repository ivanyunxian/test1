package com.supermap.intelligent.model;

/**
 * 抵押附件实体类
 * @author lx
 *
 */
public class MaterModel {

	//文件id
	private String file_id;
	//文件名称
	private String file;
	//文件类型
	private String file_postfix;
	//文件序号
	private String file_index;
	//文件下载地址
	private String file_url;
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFile_postfix() {
		return file_postfix;
	}
	public void setFile_postfix(String file_postfix) {
		this.file_postfix = file_postfix;
	}
	public String getFile_index() {
		return file_index;
	}
	public void setFile_index(String file_index) {
		this.file_index = file_index;
	}
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	
	
}
