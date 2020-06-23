package com.supermap.yingtanothers.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.service.QueryQLRService;


/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月13日 下午3:50:28
 * 功能：鹰潭市不动产查询共享中间库权利人信息
 */

@Controller
@RequestMapping("/queryqlrxx")
public class QueryQLRController {

	@Autowired
	private QueryQLRService q_QueryQLRService;

	/**
	 * 分页获取选择权利人信息列表
	 * 
	 * @param request
	 * @param response
	 * @return Message
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/queryqlrlist/", method = RequestMethod.GET)
	public @ResponseBody Message GetQlrInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 分页查询
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String sqrxm = RequestHelper.getParam(request, "qlrxm");
		Message m = new Message();
		m = q_QueryQLRService.QueryQLRByQlrxm(sqrxm, page, rows);
				
		return m;
	}
	/**
	 * 根据共享项目编号从中间库抽取信息的接口（转移登记商品房买卖）
	 * 
	 * @param request
	 * @param response
	 * @return Message
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/extractqlrlist/{gxxmbh}/{xmbh}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ExtractQlr(@PathVariable("xmbh") String xmbh,HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String gxxmbh = RequestHelper.getParam(request, "gxxmbh");
		ResultMessage message = new ResultMessage();
		message = q_QueryQLRService.QueryQLRByGxxmbh(gxxmbh,xmbh);
		return message;
	}
	
	/**
	 * 根据共享项目编号从中间库抽取信息的接口（转移登记商品房买卖）
	 * 
	 * @param request
	 * @param response
	 * @return Message
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/extractdylist/{gxxmbh}/{xmbh}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ExtractDy(@PathVariable("xmbh") String xmbh,HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String gxxmbh = RequestHelper.getParam(request, "gxxmbh");
		ResultMessage message = new ResultMessage();
		message = q_QueryQLRService.QueryDYByGxxmbh(gxxmbh, xmbh);
		return message;
	}
}
