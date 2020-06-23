package com.supermap.realestate.registration.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.SJSBService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import java.util.LinkedHashMap;

/**
 * 
 * @Description:数据上报结果查询
 * @author diaoliwei
 * @date 2016年1月17日 上午11:48:43
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/sjsb")
public class SJSBController {
	
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(SJSBController.class);

	@Autowired
	private SJSBService sjsbService;
	
	/**
	 * 数据上报结果查询页面
	 * @author diaoliwei
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sjsbmanager/", method = RequestMethod.GET)
	public String showDataQuery(Model model, HttpServletRequest request) {

		return "/realestate/registration/" + "sjsb/sjsbmanager";
	}
	
	/**
	 * 获取业务日志分页列表
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @date 2016年1月15日 上午10:51:10
	 * @return
	 */
	@RequestMapping(value = "/query",method = RequestMethod.GET)
	@ResponseBody
	public Message getPageList(HttpServletRequest request, HttpServletResponse response){
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String id_sjq=request.getParameter("id_sjq");
		Map<String, Object> mapCondition = new LinkedHashMap<String, Object>();
		try {
			String id_sjz = RequestHelper.getParam(request, "id_sjz");
			String operateUser = RequestHelper.getParam(request, "operateuser");
			String reccode = RequestHelper.getParam(request, "reccode");
			String successflag = RequestHelper.getParam(request, "successflag");
			mapCondition.put("id_sjq", id_sjq);
			mapCondition.put("id_sjz", id_sjz);
			mapCondition.put("operateUser", operateUser);
			mapCondition.put("reccode", reccode);
			mapCondition.put("successflag", successflag);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Message m = sjsbService.getPagedList(page, rows, mapCondition);
		return m;
	}
	@RequestMapping(value = "/querysjsb",method = RequestMethod.GET)
	@ResponseBody
	public Message getPageList1(HttpServletRequest request, HttpServletResponse response){
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		try {
			String SLBH = RequestHelper.getParam(request, "SLBH");
			String reccode = RequestHelper.getParam(request, "reccode");
			String successflag = RequestHelper.getParam(request, "successflag");
			String XZQH = RequestHelper.getParam(request, "XZQH");
			String SBKSSJ = RequestHelper.getParam(request, "SBKSSJ");
			String SBJSSJ = RequestHelper.getParam(request, "SBJSSJ");
			
			String SBSJ = RequestHelper.getParam(request, "SBSJ");
			if(SBSJ==null){
				SBSJ = "";
			}
			String DJLX = RequestHelper.getParam(request, "DJLX");
			if(DJLX==null){
				DJLX = "";
			}
			if(XZQH==null){
				XZQH = "0";
			}
			mapCondition.put("SLBH", SLBH);
			mapCondition.put("reccode", reccode);
			mapCondition.put("successflag", successflag);
			mapCondition.put("XZQH", XZQH);
			mapCondition.put("SBKSSJ", SBKSSJ);
			mapCondition.put("SBJSSJ", SBJSSJ);
			
			mapCondition.put("SBSJ", SBSJ);
			mapCondition.put("DJLX", DJLX);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Message m = sjsbService.getPagedList1(page, rows, mapCondition, iflike);
		return m;
	}
	/**
	 * 业务类型列表
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/reccode",method = RequestMethod.GET)
	@ResponseBody
	public String getYwlogLevel(HttpServletRequest request, HttpServletResponse response){
		StringBuffer strBuffer = new StringBuffer();
		for (ConstValue.RECCODE reccode : ConstValue.RECCODE.values()) {
			strBuffer.append(reccode.Value + "," + reccode.Name + "#");
		}
		String str = strBuffer.toString();
		if(str.endsWith("#")){
			str = str.substring(0, str.lastIndexOf("#"));
		}
		return str;
	}
	
	/**
	 * 读取显示响应报文
	 * @author DIAOLIWEI
	 * @date 2016-1-18
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/readRec",method = RequestMethod.POST)
	@ResponseBody
	public String readRec(HttpServletRequest request, HttpServletResponse response){
		String bwmc = request.getParameter("bwmc");
		/*if(!StringUtils.isEmpty(bwmc) && bwmc.length() > 5){
			bwmc = "Rep" + bwmc.substring(4, bwmc.length());
		}*/
		try {
			String path = request.getRealPath("/");
			String returnpath = "resources/exchangefiles/";
			path = path + returnpath + bwmc;
			File file = new File(path);
			if (file.isFile() && file.exists()){
				String xml = FileUtils.readFileToString(new File(path), "UTF-8");
				xml = xml.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r", "");
				xml = xml.replaceAll("\n", "<br>");
				return xml;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 分页获取数据上报项目记录
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2016年04月10日 13:51:10
	 * @return
	 */
	@RequestMapping(value = "/sjsbdata/",method = RequestMethod.GET)
	@ResponseBody
	public Message getSJSBData(HttpServletRequest request, HttpServletResponse response){
		return sjsbService.getSJSBData(request);
	}
	
	
	/**
	 * 分页获取数据上报项目记录
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2016年04月10日 13:51:10
	 * @return
	 */
	@RequestMapping(value = "/report/",method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage Report(HttpServletRequest request, HttpServletResponse response){
		String xmbhs = request.getParameter("info");
		ResultMessage m=sjsbService.Report(xmbhs,request);
		return m;
	}
//	branchDelete
	/**
	 * 批量删除上报结果查询里面的记录（BDCS_SJSB）
	 * @author weilb
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/branchDelete",method = RequestMethod.GET)
	@ResponseBody
	public ResultMessage branchDelete(HttpServletRequest request, HttpServletResponse response){
		String ids = request.getParameter("ids");
		ResultMessage m=sjsbService.branchDelete(ids);
		return m;
	}
	
	/**
	 * 根据xmbh进行数据上报
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月16日 17:19:10
	 * @return
	 */
	@RequestMapping(value = "/report1/",method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,String> Report1(HttpServletRequest request, HttpServletResponse response){
		String xmbh = request.getParameter("xmbh");
		HashMap<String,String> m=sjsbService.Report1(xmbh,request);
		return m;
	}
	
	/**
	 * 分页获取数据上报项目记录
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2016年04月10日 13:51:10
	 * @return
	 */
	@RequestMapping(value = "/reportinfolist/",method = RequestMethod.POST)
	@ResponseBody
	public Message GetReportInfoList(HttpServletRequest request, HttpServletResponse response){
		Message m=sjsbService.GetReportInfoList(request);
		return m;
	}
	/**
	 * 统计上报情况、
	 * @author weilb
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/TJSBQK/")
	@ResponseBody
	public Map<String,String> getTJSBQK(HttpServletRequest request){
		   Map<String,String> map =  sjsbService.getTJSBQK(request);
		   return map;
		   
	}
	/**
	 * 获取数据上报详情
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2016年04月10日 13:51:10
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/reportdetaillist/",method = RequestMethod.POST)
	@ResponseBody
	public List<Map> GetReportDetailList(HttpServletRequest request, HttpServletResponse response){
		List<Map> list=sjsbService.GetReportDetailList(request);
		return list;
	}
	
	/**
	 * 数据上报结果查询页面
	 * @author diaoliwei
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/autoreport", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage AutoReport(Model model, HttpServletRequest request) {
		ResultMessage ms=new ResultMessage();
		List<String> listXmbh=sjsbService.GetAutoReportList(request);
		String intervaltime = request.getParameter("intervaltime");
		int waittime=1;
		waittime=StringHelper.getInt(intervaltime);
		if(listXmbh!=null&&listXmbh.size()>0){
			for(String xmbh:listXmbh){
				try{
					sjsbService.Report1(xmbh,request);
				}catch(Exception e){
					
				}
				try {
					Thread.sleep(waittime*1000);
				} catch (InterruptedException e) {
				}
			}
		}
		ms.setMsg("自动上报完成！");
		ms.setSuccess("true");
		return ms;
	}
}
