
/**
 * 
 * 代码生成器自动生成[WFD_MODTMP]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_MODTMP",schema = "BDC_WORKFLOW")
public class Wfd_ModTmp {

	private String Mod_Id;
	private String Sysmod_Desc;
	private Integer Sysmod_Index;
	private String Sysmod_Name;
	private String Sysmod_Path;
	private String Sysmod_Sign;
	private Integer Sysmod_Status;
	private String Sysmod_Type;

	@Id
	@Column(name = "MOD_ID", length = 400)
	public String getMod_Id() {
		if (Mod_Id == null)
			Mod_Id = UUID.randomUUID().toString().replace("-", "");
		return Mod_Id;
	}

	public void setMod_Id(String Mod_Id) {
		this.Mod_Id = Mod_Id;
	}

	@Column(name = "SYSMOD_DESC", length = 600)
	public String getSysmod_Desc() {
		return Sysmod_Desc;
	}

	public void setSysmod_Desc(String Sysmod_Desc) {
		this.Sysmod_Desc = Sysmod_Desc;
	}

	@Column(name = "SYSMOD_INDEX")
	public Integer getSysmod_Index() {
		return Sysmod_Index;
	}

	public void setSysmod_Index(Integer Sysmod_Index) {
		this.Sysmod_Index = Sysmod_Index;
	}

	@Column(name = "SYSMOD_NAME", length = 600)
	public String getSysmod_Name() {
		return Sysmod_Name;
	}

	public void setSysmod_Name(String Sysmod_Name) {
		this.Sysmod_Name = Sysmod_Name;
	}

	@Column(name = "SYSMOD_PATH", length = 600)
	public String getSysmod_Path() {
		return Sysmod_Path;
	}

	public void setSysmod_Path(String Sysmod_Path) {
		this.Sysmod_Path = Sysmod_Path;
	}

	@Column(name = "SYSMOD_SIGN", length = 200)
	public String getSysmod_Sign() {
		return Sysmod_Sign;
	}

	public void setSysmod_Sign(String Sysmod_Sign) {
		this.Sysmod_Sign = Sysmod_Sign;
	}

	@Column(name = "SYSMOD_STATUS")
	public Integer getSysmod_Status() {
		return Sysmod_Status;
	}

	public void setSysmod_Status(Integer Sysmod_Status) {
		this.Sysmod_Status = Sysmod_Status;
	}

	@Column(name = "SYSMOD_TYPE", length = 400)
	public String getSysmod_Type() {
		return Sysmod_Type;
	}

	public void setSysmod_Type(String Sysmod_Type) {
		this.Sysmod_Type = Sysmod_Type;
	}

}
