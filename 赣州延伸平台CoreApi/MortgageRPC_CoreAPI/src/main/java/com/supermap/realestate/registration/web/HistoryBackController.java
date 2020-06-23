package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.ViewClass.UnitInfo;
import com.supermap.realestate.registration.service.HistoryBackService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.web.Message;

/**
 * 
 * @Description：历史回溯服务类
 * @author yuxuebin
 * @date 2017年02月28日 10:11:43
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/historyback")
public class HistoryBackController {
	
	@Autowired
	private HistoryBackService historybackService;
	
	
	/**
	 * 获取回溯单元列表
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/unit/",method = RequestMethod.GET)
	@ResponseBody
	public List<UnitInfo> getUnitHistoryList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<UnitInfo> list=historybackService.getUnitHistoryList(request);
		return list;
	}
	
	@RequestMapping(value = "/unitex/",method = RequestMethod.GET)
	@ResponseBody
	public List<UnitInfo> getUnitHistoryListEx(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<UnitInfo> list=historybackService.getUnitHistoryListEx(request);
		return list;
	}
	
	/**
	 * 仅获取单元信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="onlyunit/",method=RequestMethod.GET)
    @ResponseBody public List<UnitInfo> getOnlyUnit(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<UnitInfo> list=historybackService.getOnlyUnit(request);
		return list;
	}
	
	/**
	 * 获取回溯权利列表
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rights/",method = RequestMethod.GET)
	@ResponseBody
	public List<List<HashMap<String, String>>> getRightsHistoryList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<List<HashMap<String, String>>> list=historybackService.getRightHistoryList(request);
		return list;
	}
	
	/***
	 * 获取自然幢列表信息
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/buildingQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetbuildingQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		@SuppressWarnings("unused")
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String zdbdcdyid = RequestHelper.getParam(request, "ZDBDCDYID");// 宗地不动产单元ID		
		Message msg = historybackService.querybuilding(page, rows, zdbdcdyid);
		return msg;
	}
	
	@RequestMapping(value = "/building/", method = RequestMethod.GET)
	public String workBookbuildingQuery(Model model) {

		return "/realestate/registration/historyback/building";
	}
	
	@RequestMapping(value = "/detil/", method = RequestMethod.GET)
	public String detil(Model model) {

		return "/realestate/registration/historyback/detil";
	}
	
	@RequestMapping(value = "/righthistory/", method = RequestMethod.GET)
	public String righthistory(Model model) {

		return "/realestate/registration/historyback/righthistory";
	}
}
