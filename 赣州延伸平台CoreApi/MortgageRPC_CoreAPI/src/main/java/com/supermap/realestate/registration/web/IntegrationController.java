package com.supermap.realestate.registration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.supermap.realestate.registration.service.DJSFService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * @ClassName:
 * @author mss
 * @date
 */
@Controller
@RequestMapping("/config")
public class IntegrationController {

	@Autowired
	private CommonDao dao;
	@Autowired
	private DJSFService djsfService;
 
	private final String prefix = "/realestate/registration/";

 

	 
	@RequestMapping(value = "/integrationconfig/index/")
	public String getConstraintsConfigIndex() {
		return prefix + "config/configuration";
	}
	
	@RequestMapping(value = "/constraintsconfig/djsf")
	public String getConstraintsConfigDJSF() {
		return prefix + "djsf/djsfpz";
	}
}
