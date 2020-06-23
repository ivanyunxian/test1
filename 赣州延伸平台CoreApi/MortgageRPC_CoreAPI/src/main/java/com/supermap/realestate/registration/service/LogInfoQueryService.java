package com.supermap.realestate.registration.service;

import java.util.Map;

import com.supermap.wisdombusiness.web.Message;

public interface LogInfoQueryService {

	public Message queryLog(Map<String, String> queryvalues, int page, int rows, boolean iflike, String sort, String order);

}
