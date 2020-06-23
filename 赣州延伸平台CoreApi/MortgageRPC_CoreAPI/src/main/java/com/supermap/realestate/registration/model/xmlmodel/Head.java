package com.supermap.realestate.registration.model.xmlmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Head")
public class Head {
    
	private String BizMsgID;
	private String BusinessID;//业务号
	private String ASID;
	private String AreaCode;
	private String RecType;
	private String RecSubType;//登记细类
	private String RecSubTypeName;//细类名称
	private String RightType;
	private String RightNature;
	private String CreateDate;
	private String RegOrgID;
	private String ParcelID;
	private String EstateNum;
	private String TradeDataType;//交易数据类型
	private String BusinessProcess;//业务办理进程，办理1、退件2、登薄3，备案4，取消备案5
	private String ProjectName;//项目名称
	private String RealEstateID;//不动单元号
	private String RelationID;//关联ID
	
	public String getRealEstateID() {
	    return RealEstateID;
	}
	public void setRealEstateID(String realEstateID) {
	    RealEstateID = realEstateID;
	}
	public String getBizMsgID() {
	    return BizMsgID;
	}
	public void setBizMsgID(String bizMsgID) {
	    BizMsgID = bizMsgID;
	}
	public String getBusinessID() {
	    return BusinessID;
	}
	public void setBusinessID(String businessID) {
	    BusinessID = businessID;
	}
	public String getASID() {
	    return ASID;
	}
	public void setASID(String aSID) {
	    ASID = aSID;
	}
	public String getAreaCode() {
	    return AreaCode;
	}
	public void setAreaCode(String areaCode) {
	    AreaCode = areaCode;
	}
	public String getRecType() {
	    return RecType;
	}
	public void setRecType(String recType) {
	    RecType = recType;
	}
	public String getRecSubType() {
	    return RecSubType;
	}
	public void setRecSubType(String recSubType) {
	    RecSubType = recSubType;
	}
	public String getRightType() {
	    return RightType;
	}
	public void setRightType(String rightType) {
	    RightType = rightType;
	}
	public String getRightNature() {
	    return RightNature;
	}
	public void setRightNature(String rightNature) {
	    RightNature = rightNature;
	}
	public String getCreateDate() {
	    return CreateDate;
	}
	public void setCreateDate(String createDate) {
	    CreateDate = createDate;
	}
	public String getRegOrgID() {
	    return RegOrgID;
	}
	public void setRegOrgID(String regOrgID) {
	    RegOrgID = regOrgID;
	}
	public String getParcelID() {
	    return ParcelID;
	}
	public void setParcelID(String parcelID) {
	    ParcelID = parcelID;
	}
	public String getEstateNum() {
	    return EstateNum;
	}
	public void setEstateNum(String estateNum) {
	    EstateNum = estateNum;
	}
	public String getTradeDataType() {
	    return TradeDataType;
	}
	public void setTradeDataType(String tradeDataType) {
	    TradeDataType = tradeDataType;
	}
	public String getBusinessProcess() {
	    return BusinessProcess;
	}
	public void setBusinessProcess(String businessProcess) {
	    BusinessProcess = businessProcess;
	}

	public String getRecSubTypeName() {
	    return RecSubTypeName;
	}
	public String getProjectName() {
	    return ProjectName;
	}
	
	public void setRecSubTypeName(String recSubTypeName) {
		RecSubTypeName = recSubTypeName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
	public String getRelationID() {
	    return RelationID;
	}
	
	public void setRelationID(String relationID) {
		RelationID = relationID;
	}
	
}
