package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.service.DXSendService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sendmsg")
public class DXSendController {
	/** 查询service */
	@Autowired
	private DXSendService dXSendService;
	
	@Autowired
	private CommonDao baseCommonDao;
	
	private final String prefix = "/realestate/registration/";
	
	/**
	 * 发送短信查询
	 * @author liangc
	 * @date 2018-09-05 16:37:30
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendmsgquery", method = RequestMethod.GET)
	public String xf2qfmethod(Model model) {
		return prefix+"/dxfs/dxsend";
	}
	/**
	 * 短信推送记录:针对博白模式另外整了一套
	 * @author taochunda
	 * @date 2019-04-23 20:23:00
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dxts/index", method = RequestMethod.GET)
	public String dxtsinfo(Model model) {
		return prefix+"/dxfs/dxtsinfo";
	}

	/**
     * 查询需要发送短信的项目
     * @author liangc
     * @date 2018-09-06 15:50:30
     */
    @RequestMapping(value="/dxsendquery",method = RequestMethod.GET)
	public @ResponseBody Message getdxsendquery(HttpServletRequest request,
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

		String cxfs = request.getParameter("CXFS").trim();
		String ywlsh = request.getParameter("YWLSH").trim();
		String zsbh = request.getParameter("ZSBH").trim();
		String bdcqzh = request.getParameter("BDCQZH").trim();
		queryvalues.put("CXFS", cxfs);
		queryvalues.put("YWLSH", ywlsh);
		queryvalues.put("ZSBH", zsbh);
		queryvalues.put("ZRZZRZH", bdcqzh);
		queryvalues.put("BDCQZH", request.getParameter("SZSJ_Q"));
		queryvalues.put("SZSJ_Z", request.getParameter("SZSJ_Z"));
		queryvalues.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		queryvalues.put("DJSJ_Z", request.getParameter("DJSJ_Z"));

		return dXSendService.getdxsendquery(queryvalues, page, rows);

	 }

	/**
	 * 短信推送详情
	 * @author taochunda
	 * @date 2019-04-23 20:23:00
	 */
	@RequestMapping(value="/dxtsinfo",method = RequestMethod.GET)
	public @ResponseBody Message getDxtsInfo(HttpServletRequest request,
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

		String cxfs = RequestHelper.getParam(request, "CXFS").trim();
		String ywlsh = RequestHelper.getParam(request, "YWLSH").trim();
		String zsbh = RequestHelper.getParam(request, "ZSBH").trim();
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH").trim();
		queryvalues.put("CXFS", cxfs);
		queryvalues.put("YWLSH", ywlsh);
		queryvalues.put("ZSBH", zsbh);
		queryvalues.put("ZRZZRZH", bdcqzh);
		queryvalues.put("BDCQZH", RequestHelper.getParam(request, "SZSJ_Q"));
		queryvalues.put("SZSJ_Z", RequestHelper.getParam(request, "SZSJ_Z"));
		queryvalues.put("DJSJ_Q", RequestHelper.getParam(request, "DJSJ_Q"));
		queryvalues.put("DJSJ_Z", RequestHelper.getParam(request, "DJSJ_Z"));

		return dXSendService.getDxtsInfo(queryvalues, page, rows);

	}
    
	 /**
	  * 单条发送短信
	  * @author liangc
	 * @throws MalformedURLException 
	 * @throws ConnectException 
	  * @创建时间 2017年8月29日
	  */
	 @RequestMapping(value="/dxsend/{xmbh}/{sqrid}/{cxfs}",method = RequestMethod.POST)
	 public @ResponseBody Message dxsend(@PathVariable("xmbh") String xmbh,@PathVariable("sqrid") String sqrid,@PathVariable("cxfs") String cxfs,
			 HttpServletRequest request,HttpServletResponse response) throws ConnectException, MalformedURLException 
	 		{
	 	return dXSendService.dxsend(xmbh, sqrid, cxfs);
	 }
	 
	 /**
	  * 批量发送短信
	  * @author liangc
	 * @throws MalformedURLException 
	 * @throws ConnectException 
	  * @创建时间 2017年8月29日
	  */
	 @RequestMapping(value="/pldxsend",method = RequestMethod.POST)
	 public @ResponseBody Message pldxsend(HttpServletRequest request,HttpServletResponse response) throws ConnectException, MalformedURLException 
	 		{
		String sqrids = request.getParameter("sqrids");
		String cxfs = request.getParameter("cxfs");
	 	return dXSendService.pldxsend(sqrids, cxfs);
	 }
}
