package com.supermap.realestate.registration.service;

import java.util.Map;

import com.supermap.wisdombusiness.web.Message;


public interface FCQueryService {

	public Message getFcInfo(Map<String, String> params)throws Exception;
}
