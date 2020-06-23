package com.supermap.resources.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oracle.sql.BLOB;

@Entity
@Table(name = "blobobj", schema = "gtfg")
public class BLOBOBJ implements Serializable {

	@Id
	@Column(name = "id", length = 50)
	private String ID;
	@Column(name = "BLOBOBJSN")
	private int BLOBOBJSN;
	
	@Column(name = "BLOBOBJ_DATA_TYPE")
	private String BLOBOBJ_DATA_TYPE;
	
	@Column(name = "BLOBOBJ_DATA_SN")
	private int BLOBOBJ_DATA_SN;
	
	@Column(name = "BLOBOBJ_DATA")
	private BLOB BLOBOBJ_DATA;
	
	@Column(name = "BLOBOBJ_FILE_TYPE")
	private String BLOBOBJ_FILE_TYPE;
	
	@Column(name = "BLOBOBJ_COMPRESS_FLAG")
	private int BLOBOBJ_COMPRESS_FLAG;
	
	@Column(name = "BLOBOBJ_DATA_DESC")
	private String BLOBOBJ_DATA_DESC;
	
	public int getBLOBOBJSN() {
		return BLOBOBJSN;
	}
	public void setBLOBOBJSN(int bLOBOBJSN) {
		BLOBOBJSN = bLOBOBJSN;
	}
	public String getBLOBOBJ_DATA_TYPE() {
		return BLOBOBJ_DATA_TYPE;
	}
	public void setBLOBOBJ_DATA_TYPE(String bLOBOBJ_DATA_TYPE) {
		BLOBOBJ_DATA_TYPE = bLOBOBJ_DATA_TYPE;
	}
	public int getBLOBOBJ_DATA_SN() {
		return BLOBOBJ_DATA_SN;
	}
	public void setBLOBOBJ_DATA_SN(int bLOBOBJ_DATA_SN) {
		BLOBOBJ_DATA_SN = bLOBOBJ_DATA_SN;
	}
	public BLOB getBLOBOBJ_DATA() {
		return BLOBOBJ_DATA;
	}
	public void setBLOBOBJ_DATA(BLOB bLOBOBJ_DATA) {
		BLOBOBJ_DATA = bLOBOBJ_DATA;
	}
	public String getBLOBOBJ_FILE_TYPE() {
		return BLOBOBJ_FILE_TYPE;
	}
	public void setBLOBOBJ_FILE_TYPE(String bLOBOBJ_FILE_TYPE) {
		BLOBOBJ_FILE_TYPE = bLOBOBJ_FILE_TYPE;
	}
	public int getBLOBOBJ_COMPRESS_FLAG() {
		return BLOBOBJ_COMPRESS_FLAG;
	}
	public void setBLOBOBJ_COMPRESS_FLAG(int bLOBOBJ_COMPRESS_FLAG) {
		BLOBOBJ_COMPRESS_FLAG = bLOBOBJ_COMPRESS_FLAG;
	}
	public String getBLOBOBJ_DATA_DESC() {
		return BLOBOBJ_DATA_DESC;
	}
	public void setBLOBOBJ_DATA_DESC(String bLOBOBJ_DATA_DESC) {
		BLOBOBJ_DATA_DESC = bLOBOBJ_DATA_DESC;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
}
