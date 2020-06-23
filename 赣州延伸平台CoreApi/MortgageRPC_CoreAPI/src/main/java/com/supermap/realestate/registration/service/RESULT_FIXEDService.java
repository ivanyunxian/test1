package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.RESULT_FIXED;
import com.supermap.wisdombusiness.web.Message;

public interface RESULT_FIXEDService {
	
	Message GetRESULT_FIXED(Map<String, String> param);
	
	Message AddRESULT_FIXED(Map<String, String> param);

	Message DelRESULT_FIXED(Map<String, String> param);

	Message RESULT_FIXEDModify(Map<String, String> param);

	List<Object> GetCombobox();

}
