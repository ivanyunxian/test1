
/**
 * 
 * 代码生成器自动生成[WFD_TR_ACTTOCONDITION]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFD_TR_ACTTOCONDITION",schema = "BDC_WORKFLOW")
public class Wfd_Tr_ActToCondition {

	private String Actcond_Id;
	private String Pass_Condition_Id;
	private String Actdef_Id;

	@Id
	@Column(name = "ACTCOND_ID", length = 400)
	public String getActcond_Id() {
		if (Actcond_Id == null)
			Actcond_Id = UUID.randomUUID().toString().replace("-", "");
		return Actcond_Id;
	}

	public void setActcond_Id(String Actcond_Id) {
		this.Actcond_Id = Actcond_Id;
	}

	@Column(name = "PASS_CONDITION_ID", length = 400)
	public String getPass_Condition_Id() {
		return Pass_Condition_Id;
	}

	public void setPass_Condition_Id(String Pass_Condition_Id) {
		this.Pass_Condition_Id = Pass_Condition_Id;
	}

	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

}
