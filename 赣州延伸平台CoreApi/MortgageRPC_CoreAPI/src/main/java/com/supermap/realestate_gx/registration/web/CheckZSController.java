package com.supermap.realestate_gx.registration.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate_gx.registration.service.CheckZSService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Controller
@RequestMapping("/query_zsxx")
public class CheckZSController {
	/** 查询service */
	@Autowired
	private CheckZSService checkzsService;
	
	@Autowired
	private CommonDao baseCommonDao;
	
	
	 
	 /**
	  * 查询证书表记录是否存在
	  * @author liangc
	  * @创建时间 2017年8月29日
	  */
	 @RequestMapping(value="/zsxx",method = RequestMethod.POST)
	 public @ResponseBody Message checkzs(HttpServletRequest request,HttpServletResponse response) 
	 		{
		String para = request.getParameter("para");
	 	return checkzsService.checkzs(para);
	 }
}
