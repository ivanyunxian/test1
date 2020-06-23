package com.supermap.wisdombusiness.workflow.service.common;

import java.util.Date;

public class SmActInfo {

	/**
	 * 活动实例ID
	 * */
	private String ActInst_ID;
	
	/**
	 * 活动定义ID
	 */
	private String ActDef_ID;
	
	private  String ProInst_ID;
	
	private  String Staff_ID;
	
	private String Staff_IDs;
	
	private Date Start_Date;
	
	private Date End_Date;
	
	private String ActInst_Name;
	
	
	private Short OperateType;
	
	private Short Status;
	
	private  String OverRuleActInst_IDs;
	
	private String ActInst_Comment;
	
	private Date WillFinishDateTime;
	
	
	public SmActInfo() {
		
	}
	public SmActInfo(String actInst_ID, String actDef_ID, String proInst_ID,
			String staff_ID, String staff_IDs, Date start_Date, Date end_Date,
			String actInst_Name, Short operateType, Short status,
			String overRuleActInst_IDs, String actInst_Comment,
			Date willFinishDateTime, Boolean needToProblem, String actInst_Msg) {
		super();
		ActInst_ID = actInst_ID;
		ActDef_ID = actDef_ID;
		ProInst_ID = proInst_ID;
		Staff_ID = staff_ID;
		Staff_IDs = staff_IDs;
		Start_Date = start_Date;
		End_Date = end_Date;
		ActInst_Name = actInst_Name;
		OperateType = operateType;
		Status = status;
		OverRuleActInst_IDs = overRuleActInst_IDs;
		ActInst_Comment = actInst_Comment;
		WillFinishDateTime = willFinishDateTime;
		NeedToProblem = needToProblem;
		ActInst_Msg = actInst_Msg;
	}


	private Boolean NeedToProblem;
	
	
	private String ActInst_Msg;


	public String getActInst_ID() {
		return ActInst_ID;
	}


	public void setActInst_ID(String actInst_ID) {
		ActInst_ID = actInst_ID;
	}


	public String getActDef_ID() {
		return ActDef_ID;
	}


	public void setActDef_ID(String actDef_ID) {
		ActDef_ID = actDef_ID;
	}


	public String getProInst_ID() {
		return ProInst_ID;
	}


	public void setProInst_ID(String proInst_ID) {
		ProInst_ID = proInst_ID;
	}


	public String getStaff_ID() {
		return Staff_ID;
	}


	public void setStaff_ID(String staff_ID) {
		Staff_ID = staff_ID;
	}


	public String getStaff_IDs() {
		return Staff_IDs;
	}


	public void setStaff_IDs(String staff_IDs) {
		Staff_IDs = staff_IDs;
	}


	public Date getStart_Date() {
		return Start_Date;
	}


	public void setStart_Date(Date start_Date) {
		Start_Date = start_Date;
	}


	public Date getEnd_Date() {
		return End_Date;
	}


	public void setEnd_Date(Date end_Date) {
		End_Date = end_Date;
	}


	public String getActInst_Name() {
		return ActInst_Name;
	}


	public void setActInst_Name(String actInst_Name) {
		ActInst_Name = actInst_Name;
	}


	public Short getOperateType() {
		return OperateType;
	}


	public void setOperateType(Short operateType) {
		OperateType = operateType;
	}


	public Short getStatus() {
		return Status;
	}


	public void setStatus(Short status) {
		Status = status;
	}


	public String getOverRuleActInst_IDs() {
		return OverRuleActInst_IDs;
	}


	public void setOverRuleActInst_IDs(String overRuleActInst_IDs) {
		OverRuleActInst_IDs = overRuleActInst_IDs;
	}


	public String getActInst_Comment() {
		return ActInst_Comment;
	}


	public void setActInst_Comment(String actInst_Comment) {
		ActInst_Comment = actInst_Comment;
	}


	public Date getWillFinishDateTime() {
		return WillFinishDateTime;
	}


	public void setWillFinishDateTime(Date willFinishDateTime) {
		WillFinishDateTime = willFinishDateTime;
	}


	public Boolean getNeedToProblem() {
		return NeedToProblem;
	}


	public void setNeedToProblem(Boolean needToProblem) {
		NeedToProblem = needToProblem;
	}


	public String getActInst_Msg() {
		return ActInst_Msg;
	}


	public void setActInst_Msg(String actInst_Msg) {
		ActInst_Msg = actInst_Msg;
	}
	
	
	
}
