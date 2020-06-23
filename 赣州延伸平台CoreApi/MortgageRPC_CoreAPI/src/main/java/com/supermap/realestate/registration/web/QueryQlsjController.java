package com.supermap.realestate.registration.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
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

import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.service.QueryQlsjService;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.service.QueryService2;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.Message;

/**
 * 
 * 查询权利时间Controller 跟登记簿相关的都放在这里边
 * 
 * @author Yangx
 * @date 2015年8月25日 下午10:10:55
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/queryqlsj")
@Component("QueryQlsjController")
public class QueryQlsjController {
	/** 查询service */
	@Autowired
	private QueryQlsjService queryqlsjService;
	
	/**
	 * 房地产权预警日期查询
	 * @Title: GetHouseQueryList
	 * @author:Yangx
	 * @date：2016年4月25日 下午5:38:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/YJhouseTdQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetYjHouseQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String dylx = RequestHelper.getParam(request, "dylx");//单元类型
		String qssj = RequestHelper.getParam(request, "qssj");// 起始时间
	//	String cssj = RequestHelper.getParam(request, "cssj");// 初束时间
		//坐落
		String zl = RequestHelper.getParam(request,"zl");//RequestHelper.GetParam(request, "zl");
		// 不动产权证号
		String bdcqzh = RequestHelper.getParam(request,"bdcqzh");// RequestHelper.GetParam(request, "bdcqzh");
		// 不动产单元号
		String bdcdyh = RequestHelper.getParam(request,"bdcdyh");// RequestHelper.GetParam(request, "bdcdyh");

		if(dylx.equals("2")||dylx=="2"){
			return queryqlsjService.queryYjHouse(queryvalues, page, rows,qssj,zl,bdcqzh,bdcdyh);
		}else{
			return queryqlsjService.queryYjTdxx(queryvalues, page, rows,qssj,zl,bdcqzh,bdcdyh);
		}
		
	}
	//  房地抵押权预警日期查询
	@RequestMapping(value = "/DyYJhouseTdQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetDyYjHouseQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String dylx = RequestHelper.getParam(request, "dylx");//单元类型
		String qssj = RequestHelper.getParam(request, "qssj");// 起始时间
	//	String cssj = RequestHelper.getParam(request, "cssj");// 初束时间
		//坐落
		String zl = RequestHelper.getParam(request,"zl");//RequestHelper.GetParam(request, "zl");
		// 不动产权证号
		String bdcqzh = RequestHelper.getParam(request,"bdcqzh");// RequestHelper.GetParam(request, "bdcqzh");
		// 不动产单元号
		String bdcdyh = RequestHelper.getParam(request,"bdcdyh");// RequestHelper.GetParam(request, "bdcdyh");

		if(dylx.equals("2")||dylx=="2"){
			return queryqlsjService.queryDyYjHouse(queryvalues, page, rows,qssj,zl,bdcqzh,bdcdyh);
		}else{
			return queryqlsjService.queryDyYjTdxx(queryvalues, page, rows,qssj,zl,bdcqzh,bdcdyh);
		}
		
	}
//  房地查封权预警日期查询
	@RequestMapping(value = "/CfYJhouseTdQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetCfYjHouseQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String dylx = RequestHelper.getParam(request, "dylx");//单元类型
		String qssj = RequestHelper.getParam(request, "qssj");// 起始时间
	//	String cssj = RequestHelper.getParam(request, "cssj");// 初束时间
		String zl = RequestHelper.getParam(request,"zl");//RequestHelper.GetParam(request, "zl");
		// 不动产权证号
		String bdcqzh = RequestHelper.getParam(request,"bdcqzh");// RequestHelper.GetParam(request, "bdcqzh");
		// 不动产单元号
		String bdcdyh = RequestHelper.getParam(request,"bdcdyh");// RequestHelper.GetParam(request, "bdcdyh");
		

		if(dylx.equals("2")||dylx=="2"){
			return queryqlsjService.queryCfYjHouse(queryvalues, page, rows,qssj,zl,bdcqzh,bdcdyh);
		}else{
			return queryqlsjService.queryCfYjTdxx(queryvalues, page, rows,qssj,zl,bdcqzh,bdcdyh);
		}
		
	}
}
