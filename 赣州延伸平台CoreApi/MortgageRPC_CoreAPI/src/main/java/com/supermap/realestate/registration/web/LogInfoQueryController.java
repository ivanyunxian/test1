package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.LogInfoQueryService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Controller
@RequestMapping("/loginfoquery")
@Component("LogInfoQueryController")
public class LogInfoQueryController {
	@Autowired
	private LogInfoQueryService queryService;
	@Autowired
	private CommonDao baseCommonDao;
	
	/** 日志查询
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/querylog", method = RequestMethod.POST)
	public @ResponseBody Message GetForestQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String rzlx = RequestHelper.getParam(request, "RZLX");// 日志类型
		String mtrzlx = RequestHelper.getParam(request, "MTRZLX");// 数据维护日志类型
		String mtoprlx = RequestHelper.getParam(request, "MTOPRLX");// 数据维护操作类型
		String cxlx = RequestHelper.getParam(request, "CXLX");// 系统查询日志类型
		String dllx = RequestHelper.getParam(request, "DLLX");// 登录类型
		String rymc = RequestHelper.getParam(request, "RYMC");// 操作人员名称
		String ms = RequestHelper.getParam(request, "MS");// 日志描述
		String sj_q = RequestHelper.getParam(request, "SJ_Q");//操作时间起始
		String sj_z = RequestHelper.getParam(request, "SJ_Z");// 操作时间终止
		queryvalues.put("RZLX", rzlx);
		queryvalues.put("MTRZLX", mtrzlx);
		queryvalues.put("MTOPRLX", mtoprlx);
		queryvalues.put("CXLX", cxlx);
		queryvalues.put("DLLX", dllx);
		queryvalues.put("RYMC", rymc);
		queryvalues.put("MS", ms);
		queryvalues.put("SJ_Q", sj_q);
		queryvalues.put("SJ_Z", sj_z);
		YwLogUtil.addYwLog("日志查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryLog(queryvalues, page, rows, iflike, sort, order);
	}
}
