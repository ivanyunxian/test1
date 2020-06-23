package com.supermap.wisdombusiness.synchroinline.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.impl.OnlineServiceImpl;
import com.supermap.realestate.registration.util.StringHelper;

import com.supermap.wisdombusiness.synchroinline.service.ExtractDataAnnouncesService;
import com.supermap.wisdombusiness.synchroinline.service.SychroCommonService;
import com.supermap.wisdombusiness.synchroinline.util.InlineTools;
import com.supermap.wisdombusiness.web.ResultMessage;

@Controller
@RequestMapping("/synchro/project")
public class ProjectSnychroController {

	@Autowired
	private ExtractDataAnnouncesService extractDataAnnouncesService;
	@Autowired
	private OnlineServiceImpl onlineServiceImpl;
	
	@Autowired
	private ProjectService  projectService;
	
	@Autowired
	private SychroCommonService sychroCommonService;
	
	@RequestMapping(value = "/sychroprojectinfo" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessage> createXMXXAndSqrXX(
			HttpServletRequest request,HttpServletResponse response
			) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		String proisntId  = request.getParameter("proisntId");
		String fileNumber = request.getParameter("fileNumber");
		String sqrlist = request.getParameter("sqrlist");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(sqrlist!=null&&!sqrlist.equals("")){
			JSONArray  applyPsnList  =   JSONArray.parseArray(sqrlist);
			if(applyPsnList!=null&&applyPsnList.size()>0){
				Object obj = null;
				JSONObject tempobj = null;
				for(int i=0;i<applyPsnList.size();i++){
					obj  = applyPsnList.get(i);
					tempobj = JSONObject.parseObject(obj.toString());
					if(!tempobj.isEmpty()){
						Map<String,String> hashMap = new HashMap<String, String>();
						hashMap.put("SQRXM", tempobj.getString("SQRXM"));
						hashMap.put("ZJLX", tempobj.getString("ZJLX"));
						hashMap.put("ZJH", tempobj.getString("ZJH"));
						hashMap.put("TXDZ", tempobj.getString("TXDZ"));
						hashMap.put("LXDH", tempobj.getString("LXDH"));
						list.add(hashMap);
					}
				}
			}
		}
		ResultMessage res  = projectService.createXmxxAndSqr(proisntId, fileNumber, list, request);
		return new ResponseEntity<ResultMessage>(res, HttpStatus.OK);
	}
	@RequestMapping(value="/getnotices",method=RequestMethod.POST)
	@ResponseBody
	public List<HashMap<String, String>> getNotices(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		String ywlsh=request.getParameter("ywlsh");
		if(!StringHelper.isEmpty(ywlsh)){
			List<HashMap<String, String>> list = InlineTools.GetXMInfo(ywlsh);
			return list;
		}
		return null;
	}
	//抽取公告的服务
	@RequestMapping(value="/notice/test",method=RequestMethod.POST)
	@ResponseBody
	public  List<HashMap<String, String>> fectchNotice(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		String ywlsh=request.getParameter("ywlsh");
		if(!StringHelper.isEmpty(ywlsh)){
			List<HashMap<String, String>> list= InlineTools.GetXMInfo(ywlsh);
			 return list;
		}
		return null;
	}
	/**
	 *  通过服务方式调用获取本次同步的日期
	 * （非服务方式，会导致异步线程被锁死）
	 * */
	@RequestMapping(value="/sychrodate",method=RequestMethod.POST)
	@ResponseBody
	public  Date querySychroDate(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		String sql=request.getParameter("sql");
		return sychroCommonService.getEstateInlineLogs(sql);
	}
	
	/**
	 * 通过服务方式调用获取项目信息
	 * （非服务方式，会导致异步线程被锁死）
	 * */
	@RequestMapping(value="/xmxx",method=RequestMethod.POST)
	@ResponseBody
	public  List<BDCS_XMXX> queryXmxxs(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		String sql=request.getParameter("sql");
		return sychroCommonService.getBDCS_XMXXS(sql);
	}
	
	
	/**
	 * 
	 *通过服务方式调用获取对象集合
	 *（非服务方式，会导致异步线程被锁死）
	 * */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/objectlist",method=RequestMethod.POST)
	@ResponseBody
	public  List<Map> queryObjectList(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		String sql=request.getParameter("sql");
		return sychroCommonService.getObjectList(sql);
	}
	
	
	
	
}
