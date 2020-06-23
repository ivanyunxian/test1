package com.supermap.wisdombusiness.synchroinline.model;

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
}
