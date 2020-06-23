package com.supermap.realestate.registration.ViewClass;


/**
 * 
 * @Description:单元状态
 * @author 俞学斌
 * @date 2016年6月2日 17:15:12
 */
public class UnitStatus {
	
	/*
	 * 是否关联期房
	 */
	private String AssociatedPeriodRoom="未关联";
	
	public String getAssociatedPeriodRoom() {
		return AssociatedPeriodRoom;
	}
	public void setAssociatedPeriodRoom(String associatedPeriodRoom) {
		AssociatedPeriodRoom = associatedPeriodRoom;
	}
	public String getAssociatedReadyRoom() {
		return AssociatedReadyRoom;
	}
	public void setAssociatedReadyRoom(String associatedReadyRoom) {
		AssociatedReadyRoom = associatedReadyRoom;
	}
	/*
	 * 是否关联现房
	 */
	private String AssociatedReadyRoom="未关联";
	
	/*
	 * 查封状态
	 */
	private String SeizureState="无查封";
	/*
	 * 抵押状态
	 */
	private String MortgageState="无抵押";
	/*
	 * 异议状态
	 */
	private String ObjectionState="无异议";
	/*
	 * 限制状态
	 */
	private String LimitState="无限制";
	/*
	 * 抵押顺位
	 */
	private String DYSW="0";
	public String getDYSW() {
		return DYSW;
	}
	public void setDYSW(String dYSW) {
		DYSW = dYSW;
	}
	public String getLimitState() {
		return LimitState;
	}
	public void setLimitState(String limitState) {
		LimitState = limitState;
	}
	/*
	 * 转移预告状态
	 */
	private String TransferNoticeState="无转移预告";
	/*
	 * 抵押预告状态
	 */
	private String MortgageNoticeState="无抵押预告";
	public String getSeizureState() {
		return SeizureState;
	}
	public void setSeizureState(String seizureState) {
		SeizureState = seizureState;
	}
	public String getMortgageState() {
		return MortgageState;
	}
	public void setMortgageState(String mortgageState) {
		MortgageState = mortgageState;
	}
	public String getObjectionState() {
		return ObjectionState;
	}
	public void setObjectionState(String objectionState) {
		ObjectionState = objectionState;
	}
	public String getTransferNoticeState() {
		return TransferNoticeState;
	}
	public void setTransferNoticeState(String transferNoticeState) {
		TransferNoticeState = transferNoticeState;
	}
	public String getMortgageNoticeState() {
		return MortgageNoticeState;
	}
	public void setMortgageNoticeState(String mortgageNoticeState) {
		MortgageNoticeState = mortgageNoticeState;
	}
}
