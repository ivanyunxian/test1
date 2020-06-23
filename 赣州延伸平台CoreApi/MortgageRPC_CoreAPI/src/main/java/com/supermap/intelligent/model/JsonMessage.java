package com.supermap.intelligent.model;

public class JsonMessage {
	private boolean state;
	private String msg;
	private Object data;
	
	public boolean getState()
	{
		return state;
	}
	
	public void setState(Boolean bState)
	{
		 state=bState;
	}
	
	public String getMsg()
	{
		return msg;
	}
	
	public void setMsg(String message)
	{
		 msg=message;
	}
	
	public Object getData()
	{
		return data;
	}
	
	public void setData(Object odata)
	{
		 data=odata;
	}
	
	public JsonMessage() {
		super();
	}

	public JsonMessage(boolean state, String msg, Object data) {
		super();
		this.state = state;
		this.msg = msg;
		this.data = data;
	}
	
}
