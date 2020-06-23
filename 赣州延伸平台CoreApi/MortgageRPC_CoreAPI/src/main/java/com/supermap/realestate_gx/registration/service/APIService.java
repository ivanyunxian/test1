package com.supermap.realestate_gx.registration.service;

import java.util.List;




import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.wisdombusiness.web.Message;


public abstract interface APIService
{
	public Message queryLand(Map<String, String> conditionmap, int page, int rows,boolean iflike,String tdzt,String sort,String desc);
	public Message queryHouse(Map<String, String> conditionmap, int page, int rows,boolean iflike,String fwzt,String sort,String desc);
	public Map updatehth(String houses);
}