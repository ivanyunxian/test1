package com.supermap.yingtanothers.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.service.QueryCFXXService;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月22日 下午2:47:51
 * 功能：鹰潭市不动产查询查封信息
 */
@Controller
@RequestMapping("/querycfxx")
public class QueryCFXXController {

	@Autowired
	private QueryCFXXService q_QueryCFXXService;
	
	@RequestMapping(value = "/querycfxxlist/", method = RequestMethod.GET)
	public @ResponseBody Message QueryQlxxInfo( HttpServletRequest request,
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
		String bdcdyh = RequestHelper.getParam(request, "bdcdyh");
		String gxxmbh = RequestHelper.getParam(request, "gxxmbh");

		Message m = new Message();
		m = q_QueryCFXXService.QueryCfxxInfoByBDCDYH(bdcdyh, gxxmbh, page, rows);
				
		return m;
	}
}
