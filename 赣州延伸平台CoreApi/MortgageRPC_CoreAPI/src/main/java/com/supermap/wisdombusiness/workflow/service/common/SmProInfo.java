package com.supermap.wisdombusiness.workflow.service.common;

import java.util.Date;

public class SmProInfo {

    //申请受理时间
    private String SQStartTime;
	// / <summary>
	// / 流程定义。
	// / </summary>
	private String ProDef_ID;
	// / <summary>
	// / 流程类型，在常量中定义，为流程类型长号和短号的组合。
	// / </summary>
	private String ProDef_Type;
	// / <summary>
	// / 流程类型，在常量中定义。
	// / </summary>
	private String ProType_ID;
	// / <summary>
	// / 流程编号
	// / </summary>
	private String LCBH;
	// / <summary>
	// / 流程定义名称。
	// / </summary>
	private String ProDef_Name;
	// / <summary>
		// / 流程朱类名称。
		// / </summary>
		private String ProDef_Main_Name;
	// / <summary>
	// / 流程定义的序号（定义在工具中）。
	// / </summary>
	private int ProDef_Order;
	// / <summary>
	// / 流程实例名称，指项目名称。
	// / </summary>
	private String ProInst_Name;
	
	private String Batch;
	
	private String Ywh;
	
	private String AreaCode;
	
	private String Prodef_Tpl;
	
	private int FromInline;
	
	public SmProInfo() {

	}
	public SmProInfo(String proDef_ID, String proDef_Type, String proType_ID,
                     String lCBH, String proDef_Name, int proDef_Order,
                     String proInst_Name, String proInst_UserNumber,
                     String staffDist_ID, String district_ID, String acceptType_ID,
                     String file_Number, String acceptor, String staffID, String remark,
                     String proInst_ID, String actInst_ID, String message,
                     String proInst_Time, String finishDate, String distModify,
                     Boolean haveDone, String file_Urgency, String batch, String ywh,
                     String areaCode, int fromInline) {
		super();
		ProDef_ID = proDef_ID;
		ProDef_Type = proDef_Type;
		ProType_ID = proType_ID;
		LCBH = lCBH;
		ProDef_Name = proDef_Name;
		ProDef_Order = proDef_Order;
		ProInst_Name = proInst_Name;
		ProInst_UserNumber = proInst_UserNumber;
		StaffDist_ID = staffDist_ID;
		District_ID = district_ID;
		AcceptType_ID = acceptType_ID;
		File_Number = file_Number;
		Acceptor = acceptor;
		StaffID = staffID;
		Remark = remark;
		ProInst_ID = proInst_ID;
		ActInst_ID = actInst_ID;
		Message = message;
		ProInst_Time = proInst_Time;
		FinishDate = finishDate;
		DistModify = distModify;
		HaveDone = haveDone;
		File_Urgency = file_Urgency;
		setYwh(ywh);
		setBatch(batch);
		AreaCode=areaCode;
		FromInline=fromInline;
	}

	// / <summary>
	// / 用户编号。
	// / </summary>
	private String ProInst_UserNumber;
	// / <summary>
	// / 员工所在地区ID。
	// / </summary>
	private String StaffDist_ID;
	// / <summary>
	// / 项目所在地区ID。
	// / </summary>
	private String District_ID;
	// / <summary>
	// / 受理类型ID，与项目资料有关。
	// / </summary>
	private String AcceptType_ID;
	// / <summary>
	// / 流程实例（项目）编号。
	// / </summary>
	private String File_Number;
	// / <summary>
	// / 接收者。
	// / </summary>
	private String Acceptor;
	// / <summary>
	// / 接收者ID。
	// / </summary>
	private String StaffID;
	// / <summary>
	// / 备注。
	// / </summary>
	private String Remark;
	// / <summary>
	// / 流程编号
	// / </summary>

	private String ProInst_ID;
	// / <summary>
	// / 活动编号
	// / </summary>
	private String ActInst_ID;
	// / <summary>
	// / 受理项目后返回描述
	// / </summary>
	private String Message;
	// / <summary>
	// / 项目受理时填写的项目用时(分钟)
	// / </summary>
	private String ProInst_Time;
	// / <summary>
	// / 截止日期。
	// / </summary>
	private String FinishDate;
	// / <summary>
	// / 项目地区是否可修改
	// / </summary>
	private String DistModify;
	// / <summary>
	// / 市场项目是否已办结
	// / </summary>
	private Boolean HaveDone;
	// / <summary>
	// / 紧急程度
	// / </summary>
	private String File_Urgency;

	// / <summary>
	// / 流程名称
	// / </summary>
	private String ActDef_Name;

	private String Status;

	private String StartTime;

	private String EndTime;

	// / <summary>
	// / 活动开始时间
	// / </summary>
	private String ProStartTime;

	// / <summary>
	// / 活动期望结束时间
	// / </summary>

	private String ActWillFinishTime;
	private String ProWillFinishTime;
	
	//实例类型
	private int Instance_Type;
    //活动类型
	private String ActDef_Type;
	
	//活动是否是退件环节
	private int IsReturnAct;
	
	//挂起时间
	private Integer ProInst_Status;
	private String HangUp_Time;
	
	private String ProLSH;
	
	private Integer Uploadfile;
	private Integer Codeal;
	private Integer DefinedCodeal;
	private String Prodef_code;
	
	
	
	
	//新增加字段
    private String ProjectName ;
	//
    //流程定义是否可以上传附件
	private Integer ActDef_updaloadfile;
	
	//按钮控制2017-08-01
	private String BtnPermission;
	//从当前日期进行计算，项目的办结日期
	private Date cProFinish;
	//实例默认审批意见
	private String DefaultOpinion;
	//是否邮寄件
	private String IsMail;
	//邮寄单号
	private String MailNumber;
	private String creat_date;
	private String gxzlbm;
	//是否按小时计算流程以及环节时长：0否，1是
	private Integer house_status;
	
	//存储智能审批或者最多跑一次WLSH
	private String wlsh;

	//业务来源（0：登记系统，1：最多跑一次平台，2：远程报件，3：抵押平台，4：微信，5：金融机构api，6：公积金api ）
	private String YWLY;

	public String getYWLY() {
		return YWLY;
	}

	public void setYWLY(String YWLY) {
		this.YWLY = YWLY;
	}

	public String getWlsh() {
		return wlsh;
	}
	public void setWlsh(String wlsh) {
		this.wlsh = wlsh;
	}
	public String getGxzlbm() {
		return gxzlbm;
	}
	public void setGxzlbm(String gxzlbm) {
		this.gxzlbm = gxzlbm;
	}
	public String getMailNumber() {
		return MailNumber;
	}
	public void setMailNumber(String mailNumber) {
		MailNumber = mailNumber;
	}
	public String getIsMail() {
		return IsMail;
	}
	public void setIsMail(String isMail) {
		IsMail = isMail;
	}
	public String getDefaultOpinion() {
		return DefaultOpinion;
	}
	public void setDefaultOpinion(String defaultOpinion) {
		DefaultOpinion = defaultOpinion;
	}
	public String getBtnPermission() {
		return BtnPermission;
	}
	public void setBtnPermission(String btnPermission) {
		BtnPermission = btnPermission;
	}
	public Integer getActDef_updaloadfile() {
		return ActDef_updaloadfile;
	}
	public void setActDef_updaloadfile(Integer actDef_updaloadfile) {
		ActDef_updaloadfile = actDef_updaloadfile;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	public String getActWillFinishTime() {
		return ActWillFinishTime;
	}
	
	public String getHangUp_Time() {
		return HangUp_Time;
	}
	public void setHangUp_Time(String hangUp_Time) {
		HangUp_Time = hangUp_Time;
	}
	public void setActWillFinishTime(String actWillFinishTime) {
		ActWillFinishTime = actWillFinishTime;
	}
	/**
	 * @return the proType_ID
	 */
	public String getProType_ID() {
		return ProType_ID;
	}

	/**
	 * @param proType_ID
	 *            the proType_ID to set
	 */
	public void setProType_ID(String proType_ID) {
		ProType_ID = proType_ID;
	}

	/**
	 * @return the proDef_ID
	 */
	public String getProDef_ID() {
		return ProDef_ID;
	}

	/**
	 * @param proDef_ID
	 *            the proDef_ID to set
	 */
	public void setProDef_ID(String proDef_ID) {
		ProDef_ID = proDef_ID;
	}

	/**
	 * @return the proDef_Type
	 */
	public String getProDef_Type() {
		return ProDef_Type;
	}

	/**
	 * @param proDef_Type
	 *            the proDef_Type to set
	 */
	public void setProDef_Type(String proDef_Type) {
		ProDef_Type = proDef_Type;
	}

	/**
	 * @return the proDef_Order
	 */
	public int getProDef_Order() {
		return ProDef_Order;
	}

	/**
	 * @param proDef_Order
	 *            the proDef_Order to set
	 */
	public void setProDef_Order(int proDef_Order) {
		ProDef_Order = proDef_Order;
	}

	/**
	 * @return the lCBH
	 */
	public String getLCBH() {
		return LCBH;
	}

	/**
	 * @param lCBH
	 *            the lCBH to set
	 */
	public void setLCBH(String lCBH) {
		LCBH = lCBH;
	}

	/**
	 * @return the proDef_Name
	 */
	public String getProDef_Name() {
		return ProDef_Name;
	}

	/**
	 * @param proDef_Name
	 *            the proDef_Name to set
	 */
	public void setProDef_Name(String proDef_Name) {
		ProDef_Name = proDef_Name;
	}

	/**
	 * @return the proInst_Name
	 */
	public String getProInst_Name() {
		return ProInst_Name;
	}

	/**
	 * @param proInst_Name
	 *            the proInst_Name to set
	 */
	public void setProInst_Name(String proInst_Name) {
		ProInst_Name = proInst_Name;
	}

	/**
	 * @return the proInst_UserNumber
	 */
	public String getProInst_UserNumber() {
		return ProInst_UserNumber;
	}

	/**
	 * @param proInst_UserNumber
	 *            the proInst_UserNumber to set
	 */
	public void setProInst_UserNumber(String proInst_UserNumber) {
		ProInst_UserNumber = proInst_UserNumber;
	}

	/**
	 * @return the staffDist_ID
	 */
	public String getStaffDist_ID() {
		return StaffDist_ID;
	}

	/**
	 * @param staffDist_ID
	 *            the staffDist_ID to set
	 */
	public void setStaffDist_ID(String staffDist_ID) {
		StaffDist_ID = staffDist_ID;
	}

	/**
	 * @return the district_ID
	 */
	public String getDistrict_ID() {
		return District_ID;
	}

	/**
	 * @param district_ID
	 *            the district_ID to set
	 */
	public void setDistrict_ID(String district_ID) {
		District_ID = district_ID;
	}

	/**
	 * @return the acceptType_ID
	 */
	public String getAcceptType_ID() {
		return AcceptType_ID;
	}

	/**
	 * @param acceptType_ID
	 *            the acceptType_ID to set
	 */
	public void setAcceptType_ID(String acceptType_ID) {
		AcceptType_ID = acceptType_ID;
	}

	/**
	 * @return the file_Number
	 */
	public String getFile_Number() {
		return File_Number;
	}

	/**
	 * @param file_Number
	 *            the file_Number to set
	 */
	public void setFile_Number(String file_Number) {
		File_Number = file_Number;
	}

	/**
	 * @return the acceptor
	 */
	public String getAcceptor() {
		return Acceptor;
	}

	/**
	 * @param acceptor
	 *            the acceptor to set
	 */
	public void setAcceptor(String acceptor) {
		Acceptor = acceptor;
	}

	/**
	 * @return the staffID
	 */
	public String getStaffID() {
		return StaffID;
	}

	/**
	 * @param staffID
	 *            the staffID to set
	 */
	public void setStaffID(String staffID) {
		StaffID = staffID;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return Remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		Remark = remark;
	}

	/**
	 * @return the proInst_ID
	 */
	public String getProInst_ID() {
		return ProInst_ID;
	}

	/**
	 * @param proInst_ID
	 *            the proInst_ID to set
	 */
	public void setProInst_ID(String proInst_ID) {
		ProInst_ID = proInst_ID;
	}

	/**
	 * @return the actInst_ID
	 */
	public String getActInst_ID() {
		return ActInst_ID;
	}

	/**
	 * @param actInst_ID
	 *            the actInst_ID to set
	 */
	public void setActInst_ID(String actInst_ID) {
		ActInst_ID = actInst_ID;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}

	/**
	 * @return the proInst_Time
	 */
	public String getProInst_Time() {
		return ProInst_Time;
	}

	/**
	 * @param proInst_Time
	 *            the proInst_Time to set
	 */
	public void setProInst_Time(String proInst_Time) {
		ProInst_Time = proInst_Time;
	}

	/**
	 * @return the finishDate
	 */
	public String getFinishDate() {
		return FinishDate;
	}

	/**
	 * @param finishDate
	 *            the finishDate to set
	 */
	public void setFinishDate(String finishDate) {
		FinishDate = finishDate;
	}

	/**
	 * @return the distModify
	 */
	public String getDistModify() {
		return DistModify;
	}

	/**
	 * @param distModify
	 *            the distModify to set
	 */
	public void setDistModify(String distModify) {
		DistModify = distModify;
	}

	/**
	 * @return the haveDone
	 */
	public Boolean getHaveDone() {
		return HaveDone;
	}

	/**
	 * @param haveDone
	 *            the haveDone to set
	 */
	public void setHaveDone(Boolean haveDone) {
		HaveDone = haveDone;
	}

	/**
	 * @return the file_Urgency
	 */
	public String getFile_Urgency() {
		if(File_Urgency==null||File_Urgency.equals("")){
			return "1";
		}else{
			return File_Urgency;
		}
	}

	/**
	 * @param file_Urgency
	 *            the file_Urgency to set
	 */
	public void setFile_Urgency(String file_Urgency) {
		File_Urgency = file_Urgency;
	}
	/**
	 * @return the actDef_Name
	 */
	public String getActDef_Name() {
		return ActDef_Name;
	}
	/**
	 * @param actDef_Name
	 *            the actDef_Name to set
	 */
	public void setActDef_Name(String actDef_Name) {
		ActDef_Name = actDef_Name;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return StartTime;
	}
	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return EndTime;
	}
	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	/**
	 * @return the proStartTime
	 */
	public String getProStartTime() {
		return ProStartTime;
	}
	/**
	 * @param proStartTime
	 *            the proStartTime to set
	 */
	public void setProStartTime(String proStartTime) {
		ProStartTime = proStartTime;
	}
	/**
	 * @return the instance_Type
	 */
	public int getInstance_Type() {
		return Instance_Type;
	}
	/**
	 * @param instance_Type the instance_Type to set
	 */
	public void setInstance_Type(int instance_Type) {
		Instance_Type = instance_Type;
	}
	/**
	 * @return the proDef_Main_Name
	 */
	public String getProDef_Main_Name() {
		return ProDef_Main_Name;
	}
	/**
	 * @param proDef_Main_Name the proDef_Main_Name to set
	 */
	public void setProDef_Main_Name(String proDef_Main_Name) {
		ProDef_Main_Name = proDef_Main_Name;
	}
	/**
	 * @return the actDef_Type
	 */
	public String getActDef_Type() {
		return ActDef_Type;
	}
	/**
	 * @param actDef_Type the actDef_Type to set
	 */
	public void setActDef_Type(String actDef_Type) {
		ActDef_Type = actDef_Type;
	}
	public Integer getProInst_Status() {
		return ProInst_Status;
	}
	public void setProInst_Status(Integer proInst_Status) {
		ProInst_Status = proInst_Status;
	}
	public String getProLSH() {
		return ProLSH;
	}
	public void setProLSH(String proLSH) {
		ProLSH = proLSH;
	}
	public Integer getUploadfile() {
		return Uploadfile;
	}
	public void setUploadfile(Integer uploadfile) {
		Uploadfile = uploadfile;
	}
	public Integer getCodeal() {
		return Codeal;
	}
	public void setCodeal(Integer codeal) {
		Codeal = codeal;
	}
	public Integer getDefinedCodeal() {
		return DefinedCodeal;
	}
	public void setDefinedCodeal(Integer definedCodeal) {
		DefinedCodeal = definedCodeal;
	}
	public String getProWillFinishTime() {
		return ProWillFinishTime;
	}
	public void setProWillFinishTime(String proWillFinishTime) {
		ProWillFinishTime = proWillFinishTime;
	}
	public String getBatch() {
		return Batch;
	}
	public void setBatch(String batch) {
		Batch = batch;
	}
		public String getSQStartTime() {
	    return SQStartTime;
	}
	public void setSQStartTime(String sQStartTime) {
	    SQStartTime = sQStartTime;
	}
	public int getIsReturnAct() {
		return IsReturnAct;
	}
	public void setIsReturnAct(int isReturnAct) {
		IsReturnAct = isReturnAct;
	}
	public String getYwh() {
		return Ywh;
	}
	public void setYwh(String ywh) {
		Ywh = ywh;
	}

	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public String getProdef_Tpl() {
		return Prodef_Tpl;
	}
	public void setProdef_Tpl(String prodef_Tpl) {
		Prodef_Tpl = prodef_Tpl;
	}
	public int getFromInline() {
		return FromInline;
	}
	public void setFromInline(int fromInline) {
		FromInline = fromInline;
	}
	public String getProdef_code() {
		return Prodef_code;
	}
	public void setProdef_code(String prodef_code) {
		Prodef_code = prodef_code;
	}
	public Date getcProFinish() {
		return cProFinish;
	}
	public void setcProFinish(Date cProFinish) {
		this.cProFinish = cProFinish;
	}
	public String getCreat_date() {
		return creat_date;
	}
	public void setCreat_date(String creat_date) {
		this.creat_date = creat_date;
	}

	public Integer getHouse_status() {
		return house_status;
	}

	public void setHouse_status(Integer house_status) {
		this.house_status = house_status;
	}
}
