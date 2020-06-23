package com.supermap.realestate_gx.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate_gx.registration.service.Xf2QfService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Controller
@RequestMapping("/query_house")
public class Xf2QfController {
	/** 查询service */
	@Autowired
	private Xf2QfService xf2qfService;
	
	@Autowired
	private CommonDao baseCommonDao;
	
	
	/**
     * 查询需要转换的现房
     * @author liangc
     * @创建时间 2017年8月29日
     */
    @RequestMapping(value="/xf2qfQuery",method = RequestMethod.GET)
	public @ResponseBody Message getxf(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String bdcdyh = request.getParameter("BDCDYH").trim();
		String zl = request.getParameter("ZL").trim();
		String zrzh = request.getParameter("ZRZZRZH").trim();
		String zrzbdcdyh = request.getParameter("ZRZBDCDYH").trim();
		String zrzzl = request.getParameter("ZRZZL").trim();
		queryvalues.put("BDCDYH", bdcdyh);
		queryvalues.put("ZL", zl);
		queryvalues.put("ZRZZRZH", zrzh);
		queryvalues.put("ZRZBDCDYH", zrzbdcdyh);
		queryvalues.put("ZRZZL", zrzzl);

		return xf2qfService.getxf(queryvalues, page, rows);

	 }
    
	 /**
	  * 现房转为期房
	  * @author liangc
	  * @创建时间 2017年8月29日
	  */
	 @RequestMapping(value="/xf2qf/{bdcdyid}",method = RequestMethod.POST)
	 public @ResponseBody Message xf2qf(@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request,HttpServletResponse response) 
	 		{
		String bdcdyh = request.getParameter("bdcdyh");
	 	return xf2qfService.putxf2qf(bdcdyid,bdcdyh);
	 }
	 
	 /**
	  * 现房转为期房
	  * @author liangc
	  * @创建时间 2017年8月29日
	  */
	 @RequestMapping(value="/plxf2qf",method = RequestMethod.POST)
	 public @ResponseBody Message plxf2qf(HttpServletRequest request,HttpServletResponse response) 
	 		{
		String bdcdyids = request.getParameter("bdcdyids");
	 	return xf2qfService.plputxf2qf(bdcdyids);
	 }
}
