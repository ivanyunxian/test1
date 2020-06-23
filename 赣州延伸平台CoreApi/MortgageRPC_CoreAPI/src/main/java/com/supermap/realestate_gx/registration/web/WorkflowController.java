package com.supermap.realestate_gx.registration.web;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.ViewClass.DJInfoExtend;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.QRCodeHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate_gx.registration.service.ConverterService;
import com.supermap.realestate_gx.registration.service.GX_Service;
import com.supermap.realestate_gx.registration.service.WorkflowService;
import com.supermap.realestate_gx.registration.service.impl.SQSPServiceImpl;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

@Controller
@RequestMapping("/workflow_gx")
public class WorkflowController {

	@Autowired
	private WorkflowService workflowService;

	/**详细页面
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "acceptprojectdetails", method = RequestMethod.GET)
	public  @ResponseBody Map  acceptprojectdetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return workflowService.acceptprojectdetails( request,  response);
	}
	/**匹配登记系统信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "search/{pageindex}", method = RequestMethod.POST)
	public  @ResponseBody Message  search(@PathVariable("pageindex") String pageindex,HttpServletRequest request, HttpServletResponse response) throws IOException {
		//桂林和贺州新版远程报件界面，可以做新版页面使用 
//		if("450300".equals(xzqhdm)||"451100".equals(xzqhdm)|| "450500".equals(xzqhdm)){
		return workflowService.guilinsearch(pageindex, request,response);
//		}
//		return workflowService.search( request,  response);
		
	}
	/**匹配登记系统信息
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "acceptproject", method = RequestMethod.GET)
	public  @ResponseBody Message  acceptproject(HttpServletRequest request, HttpServletResponse response) {
		Message acceptproject = null;
		try {
			acceptproject = workflowService.acceptproject(request, response);
		} catch (Exception e){
			e.printStackTrace();

		}
		return acceptproject;
	}
	/**匹配登记系统信息
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "{xmbh}/dyq", method = RequestMethod.GET)
	public  @ResponseBody Map  acceptproject(@PathVariable("xmbh") String xmbh,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return workflowService.getDYQInfo(xmbh);
	}
	
	/**匹配登记系统信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "modeldetalis/{ywlx}/{xmbh}/{modify}", method = RequestMethod.GET)
	public  @ResponseBody List<Map>  modeldetalis(@PathVariable("ywlx") String ywlx,@PathVariable("xmbh") String xmbh,@PathVariable("modify") String modify,HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		return workflowService.modeldetalis( ywlx,xmbh,modify);
		
	}
	@RequestMapping(value = "/searchSPS/fjszpage", method = RequestMethod.POST)
	@ResponseBody
	public Page fjszpage(HttpServletRequest request,HttpServletResponse response){
		String pagein=request.getParameter("pageindex");
		String id=request.getParameter("id");
		String count=request.getParameter("count");
		String xmbh=request.getParameter("xmbh");
		Page page=workflowService.fjylpage(pagein, id,xmbh,Integer.parseInt(count));
		return page;
	}
	/**
	 * 单张显示前端图片
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/gzpt/show/{id}", method = RequestMethod.GET)
	public void showImg(HttpServletResponse response,@PathVariable("id")String id) {
		workflowService.showImg(id,response);
	}
	
	@RequestMapping(value = "/fjbh/{xmbh}/{value}", method = RequestMethod.POST)
	@ResponseBody
	public Message fjbh(@PathVariable("xmbh")String xmbh,@PathVariable("value")String value){
		return workflowService.fjbh(xmbh,value);
		
	}
}