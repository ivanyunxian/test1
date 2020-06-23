package com.supermap.wisdombusiness.web;

public class ResultMessage {
	private String success;
	private String msg;
	private boolean whether;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isWhether() {
		return whether;
	}

	public void setWhether(boolean whether) {
		this.whether = whether;
	}
}
