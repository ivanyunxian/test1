package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_DBJS implements SuperModel<String>{
	
	//主键ID
	private String id;
	private boolean modify_id = false;
	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}
	@Override
	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	//XMBH 项目编号
	private String xmbh;
	private boolean modify_xmbh = false;

	public String getXMBH() {
		return xmbh;
	}

	public void setXMBH(String xmbh) {
		if (this.xmbh != xmbh) {
			this.xmbh = xmbh;
			modify_xmbh = true;
		}
	}
	//SXH 顺序号
	private Integer sxh;
	private boolean modify_sxh = false;

	public Integer getSXH() {
		return sxh;
	}

	public void setSXH(Integer sxh) {
		if (this.sxh != sxh) {
			this.sxh = sxh;
			modify_sxh = true;
		}
	}
	//ACTINST_ID 流程关系表 WFI_ACTINST
	private String actinst_id;
	private boolean modify_actinst_id = false;
	public String getACTINST_ID() {
		return actinst_id;
	}

	public void setACTINST_ID(String actinst_id) {
		if (this.actinst_id != actinst_id) {
			this.actinst_id = actinst_id;
			modify_actinst_id = true;
		}
	}
	//USER_ID 用户
	private String user_id;
	private boolean modify_user_id = false;
	public String getUSER_ID() {
		return user_id;
	}

	public void setUSER_ID(String user_id) {
		if (this.user_id != user_id) {
			this.user_id = user_id;
			modify_user_id = true;
		}
	}
	
	//USER_ID 用户
	private String js;
	private boolean modify_js = false;
	public String getJS() {
		return js;
	}

	public void setJS(String js) {
		if (this.js != js) {
			this.js = js;
			modify_js = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xmbh = false;
		modify_sxh = false;
		modify_actinst_id = false;
		modify_user_id = false;
		modify_js = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xmbh)
			listStrings.add("xmbh");
		if (!modify_sxh)
			listStrings.add("sxh");
		if(!modify_actinst_id)
			listStrings.add("actinst_id");
		if(!modify_user_id)
			listStrings.add("user_id");
		if(!modify_js)
			listStrings.add("js");

		return StringHelper.ListToStrings(listStrings);
	}
}
