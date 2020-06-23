
/**
 * 
 * 代码生成器自动生成[WFD_TR_ACTTOMOD]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_TR_ACTTOMOD",schema = "BDC_WORKFLOW")
public class Wfd_Tr_ActToMod {

	private String Acttomodid;
	private String Actdef_Id;
	private String Sysmodid;
	private Integer Mode_Index;
	private Integer Readonly;

	@Id
	@Column(name = "ACTTOMODID", length = 400)
	public String getActtomodid() {
		if (Acttomodid == null)
			Acttomodid = UUID.randomUUID().toString().replace("-", "");
		return Acttomodid;
	}

	public void setActtomodid(String Acttomodid) {
		this.Acttomodid = Acttomodid;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "SYSMODID", length = 400)
	public String getSysmodid() {
		return Sysmodid;
	}

	public void setSysmodid(String Sysmodid) {
		this.Sysmodid = Sysmodid;
	}
	
	@Column(name = "MODE_INDEX")
	public Integer getMode_Index() {
		return Mode_Index;
	}

	public void setMode_Index(Integer Mode_Index) {
		this.Mode_Index = Mode_Index;
	}

	/**
	 * @return the readonly
	 */
	@Column(name = "READONLY")
	public Integer getReadonly() {
		return Readonly;
	}

	/**
	 * @param readonly the readonly to set
	 */
	public void setReadonly(Integer readonly) {
		Readonly = readonly;
	}
	
}
