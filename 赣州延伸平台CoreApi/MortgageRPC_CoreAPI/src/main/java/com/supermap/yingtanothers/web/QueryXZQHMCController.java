package com.supermap.yingtanothers.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月20日 下午4:40:38
 * 功能：获取配置文件（SMWB_SUPPORT库中的T_CONFIG表）中的XZQHMC（行政区划名称）
 */
@Controller
@RequestMapping("/queryxzqhmc")
public class QueryXZQHMCController {

	@RequestMapping(value = "/queryvalue/", method = RequestMethod.POST)
	public @ResponseBody Message QueryXZQHMCValue(HttpServletRequest request,HttpServletResponse response) {
		Message m = new Message();
		String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
		m.setDesc(XZQHDM);	    
		return m;		
	}
}
