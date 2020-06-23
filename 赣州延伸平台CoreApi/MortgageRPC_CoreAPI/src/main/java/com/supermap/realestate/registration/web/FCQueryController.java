package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.FCQueryService;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.wisdombusiness.web.Message;

/**
 * 房产查询
 * @author huangpeifeng
 * @date 20181228
 */
@Controller
@RequestMapping("/fcquery")
@Component("FCQueryController")
public class FCQueryController {

	/** 查询service */
	@Autowired
	private FCQueryService fcqueryService;
	
	/**
	 * 页面跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked"})
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public  String index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "/realestate_gx/registration/FCQuery/FCSearch";
	}
	
	/**
	 * 获取房产信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked"})
	@RequestMapping(value = "/getfcinfo", method = RequestMethod.POST)
	public @ResponseBody Message getFcInfo(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> params = ObjectHelper.transToMAP(request.getParameterMap());
		
		return fcqueryService.getFcInfo(params);
	}
}
