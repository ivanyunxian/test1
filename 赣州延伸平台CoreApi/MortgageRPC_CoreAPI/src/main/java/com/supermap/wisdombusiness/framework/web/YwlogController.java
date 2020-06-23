package com.supermap.wisdombusiness.framework.web;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.framework.model.Ywlog;
import com.supermap.wisdombusiness.framework.service.YwlogService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:业务日志
 * @author diaoliwei
 * @date 2016年1月15日 上午10:48:43
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework/ywlog")
public class YwlogController {
	
	private static final Log logger = LogFactory.getLog(YwlogController.class);

	@Autowired
	private YwlogService ywlogService;
	
	private final String prefix = "/framework/ywlog/";
	
	/**
	 * 业务日志管理首页
	 * @author diaoliwei
	 * @date 2016年1月15日 上午10:50:13
	 * @return
	 */
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String ShowManageIndex() {

		return prefix + "ywlogManage";
	}
	
	/**
	 * 获取业务日志分页列表
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @date 2016年1月15日 上午10:51:10
	 * @return
	 */
	@RequestMapping(value = "/ywlogs",method = RequestMethod.GET)
	@ResponseBody
	public Message getLogsList(HttpServletRequest request, HttpServletResponse response){
		Integer page = 1;
		if(request.getParameter("page")!=null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if(request.getParameter("rows")!=null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		try {
			if(null != request.getParameter("operateUser")){
				String operateUser = RequestHelper.getParam(request, "operateUser");
			    mapCondition.put("operateUser", operateUser);
			}
			if(null != request.getParameter("logLevel")){
				String logLevel = request.getParameter("logLevel");
				mapCondition.put("logLevel", logLevel);
			}
			if(null != request.getParameter("sd")){
				String sd = request.getParameter("sd");
				mapCondition.put("sd", sd);
			}
			if(null != request.getParameter("ed")){
				String ed = request.getParameter("ed");
				mapCondition.put("ed", ed);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Page logPaged = ywlogService.getPagedYwlog(page, rows, mapCondition);
		Message m = new Message();
		m.setTotal(logPaged.getTotalCount());
		m.setRows(logPaged.getResult());
		return m;
	}
	
	/**
	 * 获取日志级别
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/ywlogLevel",method = RequestMethod.GET)
	@ResponseBody
	public String getYwlogLevel(HttpServletRequest request, HttpServletResponse response){
		StringBuffer strBuffer = new StringBuffer();
		for (ConstValue.LOG log : ConstValue.LOG.values()) {
			strBuffer.append(log.Value + "," + log.Name + "#");
		}
		String str = strBuffer.toString();
		if(str.endsWith("#")){
			str = str.substring(0, str.lastIndexOf("#"));
		}
		return str;
	}
	
	@RequestMapping(value = "/logDownload", method = RequestMethod.GET)
	@ResponseBody
	public String logDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String operateUser = "";
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		if(null != request.getParameter("operateUser")){
			operateUser = RequestHelper.getParam(request, "operateUser");
			mapCondition.put("operateUser", operateUser);
		}
		if(null != request.getParameter("logLevel")){
			String logLevel = request.getParameter("logLevel");
			mapCondition.put("logLevel", logLevel);
		}
		List<Ywlog> list = new ArrayList<Ywlog>();
		list = ywlogService.logList(mapCondition);
		String tmpLog = request.getRealPath("/WEB-INF/jsp/wjmb/ywlog.xls");
		InputStream input = new FileInputStream(tmpLog);
		FileOutputStream outstream = null;
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = basePath + "\\tmp\\ywlog.xls";
		String url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywlog.xls";
		outstream = new FileOutputStream(outpath); 
		HSSFWorkbook wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		MapCol.put("操作人员", 0);
		MapCol.put("操作内容", 1);
		MapCol.put("操作时间", 2);
		MapCol.put("操作结果", 3);
		MapCol.put("日志等级", 4);
		int rownum = 1;
		for (Ywlog log : list) {
			HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	        HSSFCell cell0 = row.createCell(MapCol.get("操作人员"));
	        cell0.setCellValue(log.getOperateUser());
	        HSSFCell cell1 = row.createCell(MapCol.get("操作内容"));
	        cell1.setCellValue(log.getOperateContent());
	        HSSFCell cell2 = row.createCell(MapCol.get("操作时间"));
	        cell2.setCellValue(log.getOperateTimeStr());
	        HSSFCell cell3 = row.createCell(MapCol.get("操作结果"));
	        cell3.setCellValue(log.getSuccessText());
	        HSSFCell cell4 = row.createCell(MapCol.get("日志等级"));
	        cell4.setCellValue(log.getLevelName());
	        rownum += 1;
		}
		wb.write(outstream); 
		outstream.flush(); 
		outstream.close();
		return url;
	}
	
}
