package com.supermap.realestate.registration.model.xmlExportmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Head")
public class HeadExport {
	@XStreamAsAttribute
	@XStreamOmitField
	@XStreamAlias("name")
	private String NAME = "基本信息";

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String _Value) {
		NAME = _Value;
	}

	private PropertyInfo BizMsgID = new PropertyInfo("流程号");
	private PropertyInfo BusinessID = new PropertyInfo("合同号");
	private PropertyInfo ProjectName = new PropertyInfo("项目名称");
	private PropertyInfo ASID = new PropertyInfo("共享标准");
	private PropertyInfo AreaCode = new PropertyInfo("行政区代码");
	private PropertyInfo RecType = new PropertyInfo("登记类型");
	private PropertyInfo RecSubType = new PropertyInfo("登记细类");
	private PropertyInfo RecSubTypeName = new PropertyInfo("登记细类名称");
	private PropertyInfo RightType = new PropertyInfo("权利类型");
	private PropertyInfo RightNature = new PropertyInfo("权利性质");
	private PropertyInfo CreateDate = new PropertyInfo("创建时间");
	private PropertyInfo RegOrgID = new PropertyInfo("登记机构名称");
	private PropertyInfo ParcelID = new PropertyInfo("宗地/宗海代码");
	private PropertyInfo EstateNum = new PropertyInfo("不动产单元号");
	private PropertyInfo TradeDataType = new PropertyInfo("交易数据类型");
	private PropertyInfo BusinessProcess = new PropertyInfo("业务办理进程");
	private PropertyInfo RealEstateID = new PropertyInfo("不动产单元ID");
	private PropertyInfo RelationID = new PropertyInfo("关联ID");

	
	public PropertyInfo getRelationID() {
		return RelationID;
	}

	public void setRelationID(Object _Value) {
		RelationID.setvalue(_Value);
	}
	
	public PropertyInfo getRealEstateID() {
		return RealEstateID;
	}

	public void setRealEstateID(Object _Value) {
		RealEstateID.setvalue(_Value);
	}

	public PropertyInfo getBizMsgID() {
		return BizMsgID;
	}

	public void setBizMsgID(Object _Value) {
		BizMsgID.setvalue(_Value);
	}

	public PropertyInfo getBusinessID() {
		return BusinessID;
	}

	public void setBusinessID(Object _Value) {
		BusinessID.setvalue(_Value);
	}

	public PropertyInfo getASID() {
		return ASID;
	}

	public void setASID(Object _Value) {
		ASID.setvalue(_Value);
	}

	public PropertyInfo getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(Object _Value) {
		AreaCode.setvalue(_Value);
	}

	public PropertyInfo getRecType() {
		return RecType;
	}

	public void setRecType(Object _Value) {
		RecType.setvalue(_Value);
	}

	public PropertyInfo getRecSubType() {
		return RecSubType;
	}

	public void setRecSubType(Object _Value) {
		RecSubType.setvalue(_Value);
	}

	public PropertyInfo getRightType() {
		return RightType;
	}

	public void setRightType(Object _Value) {
		RightType.setvalue(_Value);
	}

	public PropertyInfo getRightNature() {
		return RightNature;
	}

	public void setRightNature(Object _Value) {
		RightNature.setvalue(_Value);
	}

	public PropertyInfo getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Object _Value) {
		CreateDate.setvalue(_Value);
	}

	public PropertyInfo getRegOrgID() {
		return RegOrgID;
	}

	public void setRegOrgID(Object _Value) {
		RegOrgID.setvalue(_Value);
	}

	public PropertyInfo getParcelID() {
		return ParcelID;
	}

	public void setParcelID(Object _Value) {
		ParcelID.setvalue(_Value);
	}

	public PropertyInfo getEstateNum() {
		return EstateNum;
	}

	public void setEstateNum(Object _Value) {
		EstateNum.setvalue(_Value);
	}

	public PropertyInfo getTradeDataType() {
		return TradeDataType;
	}

	public void setTradeDataType(Object _Value) {
		TradeDataType.setvalue(_Value);
	}

	public PropertyInfo getBusinessProcess() {
		return BusinessProcess;
	}

	public void setBusinessProcess(Object _Value) {
		BusinessProcess.setvalue(_Value);
	}

	public PropertyInfo getRecSubTypeName() {
		return RecSubTypeName;
	}

	public PropertyInfo getProjectName() {
		return ProjectName;
	}

	public void setRecSubTypeName(Object _Value) {
		RecSubTypeName.setvalue(_Value);
	}

	public void setProjectName(Object _Value) {
		ProjectName.setvalue(_Value);
	}
}
