package com.supermap.realestate.registration.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.LogInfoService;
import com.supermap.realestate.registration.util.LogConstValue.DLMSG;
import com.supermap.realestate.registration.util.LogConstValue.LOGINTYPE;
import com.supermap.realestate.registration.util.LogConstValue.QUERYTYPE;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

@Controller
@RequestMapping("/loginfo")
@Component("LogServiceController")
public class LogServiceController {

	@Autowired
	private LogInfoService logInfoService;
	@Autowired
	private CommonDao baseCommonDao;
	
	@RequestMapping(value = "/datamaintain", method = RequestMethod.POST)
	@ResponseBody
	public void getDataMaintenaceLog(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String content = RequestHelper.getParam(request, "content");
		logInfoService.dataMaintenaceLog(content);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querylog", method = RequestMethod.POST)
	@ResponseBody
	public void getQueryLog(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String iflikeStr = RequestHelper.getParam(request, "iflike");
		String values = RequestHelper.getParam(request, "queryvalues");
		JSONObject queryvalues = JSONObject.fromObject(values);
		String type = RequestHelper.getParam(request, "querytype");
		QUERYTYPE querytype = null;
		Boolean iflike = false;
		if (type.equals(QUERYTYPE.DJCXZM.Name)) {
			querytype = QUERYTYPE.DJCXZM;
		}else if (type.equals(QUERYTYPE.DJDJB.Name)) {
			querytype = QUERYTYPE.DJDJB;
		}else if (type.equals(QUERYTYPE.DJDJB.Name)) {
			querytype = QUERYTYPE.DJDJB;
		}else if (type.equals(QUERYTYPE.DJFW.Name)) {
			querytype = QUERYTYPE.DJFW;
		}else if (type.equals(QUERYTYPE.DJLD.Name)) {
			querytype = QUERYTYPE.DJLD;
		}else if (type.equals(QUERYTYPE.DJNYD.Name)) {
			querytype = QUERYTYPE.DJNYD;
		}else if (type.equals(QUERYTYPE.DJZD.Name)) {
			querytype = QUERYTYPE.DJZD;
		}else if (type.equals(QUERYTYPE.DJZH.Name)) {
			querytype = QUERYTYPE.DJZH;
		}else if (type.equals(QUERYTYPE.DJZRZ.Name)) {
			querytype = QUERYTYPE.DJZRZ;
		}else if (type.equals(QUERYTYPE.DJXTDZ.Name)) {
			querytype = QUERYTYPE.DJXTDZ;
		}else if (type.equals(QUERYTYPE.DJZRT.Name)) {
			querytype = QUERYTYPE.DJZRT;
		}else {
			querytype = QUERYTYPE.QTQYTYPE;
		}
		if (iflikeStr != null && iflikeStr.equals("true")) {
			iflike = true;
		}
		logInfoService.queryLog(iflike, queryvalues, querytype);
	}
	
	@RequestMapping(value = "/loginlog", method = RequestMethod.POST)
	@ResponseBody
	public void getLoginLog(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String type = RequestHelper.getParam(request, "querytype");
		String dlmsg = RequestHelper.getParam(request, "msg");
		LOGINTYPE loginType = null;
		DLMSG msg = null;
		if (type.equals(LOGINTYPE.DJDL.Name)) {
			loginType = LOGINTYPE.DJDL;
		}else if (type.equals(LOGINTYPE.DJTC.Name)) {
			loginType = LOGINTYPE.DJTC;
		}
		if(dlmsg.equals(DLMSG.DLCG.Name)){
			msg = DLMSG.DLCG;
		}else if (dlmsg.equals(DLMSG.DLSB.Name)) {
			msg = DLMSG.DLSB;
		}else if (dlmsg.equals(DLMSG.ZHTY.Name)) {
			msg = DLMSG.ZHTY;
		}else if (dlmsg.equals(DLMSG.CSXZ.Name)) {
			msg = DLMSG.CSXZ;
		}else if (dlmsg.equals(DLMSG.WZCW.Name)) {
			msg = DLMSG.WZCW;
		}else if (dlmsg.equals(DLMSG.TCCG.Name)) {
			msg = DLMSG.TCCG;
		}else if (dlmsg.equals(DLMSG.TCSB.Name)) {
			msg = DLMSG.TCSB;
		}else {
			msg = DLMSG.QTMSG;
		}
		
		logInfoService.loginLog(loginType, msg);
	}

}
