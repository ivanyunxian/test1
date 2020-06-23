package com.supermap.realestate_gx.registration.service;


import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.wisdombusiness.web.Message;

public interface IAccumufundService {
	
	Map GetGjjList();
	
    public Map GjjdetailsList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;

    Message GjjaccectProject(HttpServletRequest request, HttpServletResponse response);

	Map Gjjcondition(HttpServletRequest request, HttpServletResponse response) ;
	
	Map GjjfjList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
}
