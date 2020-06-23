
/**
 * 
 * 代码生成器自动生成[WFI_TR_ACTINSTTOSPDY]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_TR_ACTDEFTOSPDY",schema = "BDC_WORKFLOW")
public class Wfi_Tr_ActDefToSpdy {

	private String Actdefspdy_Id;
	private String Spdy_Id;
	private String Actdef_Id;
	private Integer Show_Index;
	private Integer Status;
	private Integer readonly;

	@Id
	@Column(name = "ACTDEFSPDY_ID", length = 200)
	public String getActdefspdy_Id() {
		if (Actdefspdy_Id == null)
			Actdefspdy_Id = UUID.randomUUID().toString().replace("-", "");
		return Actdefspdy_Id;
	}

	public void setActdefspdy_Id(String Actdefspdy_Id) {
		this.Actdefspdy_Id = Actdefspdy_Id;
	}

	@Column(name = "SPDY_ID", length = 200)
	public String getSpdy_Id() {
		return Spdy_Id;
	}

	public void setSpdy_Id(String Spdy_Id) {
		this.Spdy_Id = Spdy_Id;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "SHOW_INDEX")
	public Integer getShow_Index() {
		return Show_Index;
	}

	public void setShow_Index(Integer Show_Index) {
		this.Show_Index = Show_Index;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	/**
	 * @return the readonly
	 */
	@Column(name = "READONLY")
	public Integer getReadonly() {
		return readonly;
	}

	/**
	 * @param readonly the readonly to set
	 */
	public void setReadonly(Integer readonly) {
		this.readonly = readonly;
	}

}
