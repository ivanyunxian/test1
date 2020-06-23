package com.supermap.wisdombusiness.workflow.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;

@Component("smLshHelper")
public class SmLshHelper {

	@Autowired
	CommonDao _CommonDao;

	
}
