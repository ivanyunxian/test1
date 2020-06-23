package com.supermap.realestate.registration.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.realestate.registration.util.*;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_FJMB;
import com.supermap.realestate.registration.model.LOG_MSGVALIDATE;
import com.supermap.realestate_gx.registration.service.Xf2QfService;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;

@Controller
@RequestMapping("/check_sqrxx")
public class CheckSqrxxController {
	/** 查询service */
	
	@Autowired
	private CommonDao baseCommonDao;
	
	
	/**
	 * 检验申请人身份信息
	 * @author liangc
	 * @date 2018-8-6 10:59:30
	 * @param xmbh
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sqrs/checksqr", method = RequestMethod.POST)
	@AccessRequired
	public @ResponseBody Message CheckSQR(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String postUrlCompany = "http://20.0.6.7:6062/developer-api/sync/resources/007566146170601F0002-1/records/query";
		String postUrlPerson ="http://20.0.6.7:6062/developer-api/sync/resources/89822217818050B00001-6/1/requests";
		String postUrlPerson_gj = "http://localhost:8081/InterfacePlatform/Validate/Validateinfo_JZCX";
		String sqrxm = RequestHelper.getParam(request, "sqrxm");
		String zjlx = RequestHelper.getParam(request, "zjlx");
		String zjh = RequestHelper.getParam(request, "zjh");
		String sqrlb = RequestHelper.getParam(request, "sqrlb");
		String sqrlx = RequestHelper.getParam(request, "sqrlx");
		String fddbr = RequestHelper.getParam(request, "fddbr");
		String fddbrzjlx = RequestHelper.getParam(request, "fddbrzjlx");
		String fddbrzjhm = RequestHelper.getParam(request, "fddbrzjhm");
		String dlrxm = RequestHelper.getParam(request, "dlrxm");
		String dlrzjhm = RequestHelper.getParam(request, "dlrzjhm");
		Message message = new Message();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		//校验个人身份信息
		if("1".equals(sqrlx)){
			if(!StringHelper.isEmpty(sqrxm)){
				Map m =	URLUtil.sandForPersonXX(postUrlPerson,sqrxm,zjh);
				
				Map<String, Object> grxx = new HashMap<String,Object>();
				grxx.put("sqrxm", m.get("name"));
				grxx.put("zjh", m.get("zjhm"));
				//grxx.put("picturecode64", m.get("photo").toString());
				grxx.put("errstr", m.get("errstr"));
				grxx.put("code", m.get("code"));
				grxx.put("jkyccode", m.get("jkyccode"));
				result.add(grxx);
				message.setSuccess("success");
			}
			if(!StringHelper.isEmpty(dlrxm)&&!StringHelper.isEmpty(dlrzjhm)){
				Map m = null;
				if(dlrzjhm.indexOf("45") == 0){
					m =	URLUtil.sandForPersonXX(postUrlPerson,dlrxm,dlrzjhm);
				}else{
					String content = "sqrxm="+dlrxm;
					content += "&zjh="+dlrzjhm;
					m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
				}
				Map<String, Object> dlrxx = new HashMap<String,Object>();
				dlrxx.put("dlrxm", m.get("name"));
				dlrxx.put("dlrzjhm", m.get("zjhm"));
				//dlrxx.put("picturecode64_dlr", m.get("photo").toString());
				dlrxx.put("errstr", m.get("errstr"));
				dlrxx.put("code", m.get("code"));
				dlrxx.put("jkyccode", m.get("jkyccode"));
				result.add(dlrxx);
				message.setSuccess("success");
			}
		}else if("2".equals(sqrlx)){
			//企业
			if(!StringHelper.isEmpty(sqrxm)){
				Map<String,Object> jsonparam = new HashMap<String,Object>();
				jsonparam.put("table", "GXSJZX_LZ.REG_LZHYQYJBXXB");
				Map<String,Object> values = new HashMap<String,Object>();
				List<String> vallsit = new ArrayList<String>();
				vallsit.add("QYMC");
				vallsit.add("TYSHXYDM");//91450100MA5KA1CLXJ
				vallsit.add("FDDBR");
				values.put("columns", vallsit);
				jsonparam.put("values", values);
				Map<String,String> map = new HashMap<String, String>(); 
			    map.put("column", "QYMC"); 
			    map.put("mode", "is"); 
			    map.put("value", sqrxm);
			    Map<String,String> map2 = new HashMap<String, String>();
			    map2.put("column", "TYSHXYDM"); 
			    map2.put("mode", "is"); 
			    map2.put("value", zjh);
			    List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
			    list.add(map);
			    list.add(map2);
			    List<Object> listpara = new ArrayList<Object>();
			    listpara.add(list);
			    jsonparam.put("conditions",listpara);
		        JSONObject jsonObject = JSONObject.fromObject(jsonparam);
		        System.out.println(jsonObject);
		        Map m = URLUtil.sandJSONPOST(postUrlCompany, jsonObject);
		        result.add(m);
		        message.setSuccess("success");
			}
			if(!StringHelper.isEmpty(dlrxm)&&!StringHelper.isEmpty(dlrzjhm)){
				Map m = null;
				if(dlrzjhm.indexOf("45") == 0){
					m =	URLUtil.sandForPersonXX(postUrlPerson,dlrxm,dlrzjhm);
				}else{
					String content = "sqrxm="+dlrxm;
					content += "&zjh="+dlrzjhm;
					m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
				}
				Map<String, Object> dlrxx = new HashMap<String,Object>();
				dlrxx.put("dlrxm", m.get("name"));
				dlrxx.put("dlrzjhm", m.get("zjhm"));
				//dlrxx.put("picturecode64_dlr", m.get("photo").toString());
				dlrxx.put("errstr", m.get("errstr"));
				dlrxx.put("code", m.get("code"));
				dlrxx.put("jkyccode", m.get("jkyccode"));
				result.add(dlrxx);
				message.setSuccess("success");
			}
			
		}else{
			message.setSuccess("false");
		}
		message.setRows(result);
		message.setTotal(result.size());
		return message;
	}
	
	
	/**
	 * 检验申请人身份信息
	 * @author liangc
	 * @date 2018-8-6 10:59:30
	 * @param xmbh
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(value = "/sqrs/checksqr_gj", method = RequestMethod.POST)
	@AccessRequired
	public @ResponseBody Message CheckSQR_GJ(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String postUrlCompany_gj = "http://localhost:8081/InterfacePlatform/Validate/ValidateinfoForGS";
		String postUrlPerson_gj = "http://localhost:8081/InterfacePlatform/Validate/Validateinfo_JZCX";
		String postUrlPerson ="http://20.0.6.7:6062/developer-api/sync/resources/89822217818050B00001-6/1/requests";
		String sqrxm = request.getParameter("sqrxm");
		String zjlx = request.getParameter("zjlx");
		String zjh = request.getParameter("zjh");
		String sqrlb = request.getParameter("sqrlb");
		String sqrlx = request.getParameter("sqrlx");
		String fddbr = request.getParameter("fddbr");
		String fddbrzjlx = request.getParameter("fddbrzjlx");
		String fddbrzjhm = request.getParameter("fddbrzjhm");
		String dlrxm = request.getParameter("dlrxm");
		String dlrzjhm = request.getParameter("dlrzjhm");
		Message message = new Message();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if("2".equals(sqrlx)){
			//企业
			if(!StringHelper.isEmpty(sqrxm)){
				String content = "sqrxm="+sqrxm;
				content += "&zjh="+zjh;
		        Map m = URLUtil.sandJSONPOST_GJGS(postUrlCompany_gj, content);
		        result.add(m);
		        message.setSuccess("success");
			}
			if(!StringHelper.isEmpty(dlrxm)&&!StringHelper.isEmpty(dlrzjhm)){
				Map m = null;
				if(dlrzjhm.indexOf("45") == 0){
					m =	URLUtil.sandForPersonXX(postUrlPerson,dlrxm,dlrzjhm);
				}else{
					String content = "sqrxm="+dlrxm;
					content += "&zjh="+dlrzjhm;
					m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
				}
				Map<String, Object> dlrxx = new HashMap<String,Object>();
				dlrxx.put("dlrxm", m.get("name"));
				dlrxx.put("dlrzjhm", m.get("zjhm"));
				//dlrxx.put("picturecode64_dlr", m.get("photo").toString());
				dlrxx.put("errstr", m.get("errstr"));
				dlrxx.put("code", m.get("code"));
				dlrxx.put("jkyccode", m.get("jkyccode"));
				result.add(dlrxx);
				message.setSuccess("success");
			}
			
		}else if("1".equals(sqrlx)){
			if(!StringHelper.isEmpty(sqrxm)){
				String content = "sqrxm="+sqrxm;
				content += "&zjh="+zjh;
				Map m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
				
				Map<String, Object> grxx = new HashMap<String,Object>();
				grxx.put("sqrxm", m.get("name"));
				grxx.put("zjh", m.get("zjhm"));
				//grxx.put("picturecode64", m.get("photo").toString());
				grxx.put("errstr", m.get("errstr"));
				grxx.put("code", m.get("code"));
				grxx.put("jkyccode", m.get("jkyccode"));
				result.add(grxx);
				message.setSuccess("success");
			}
			if(!StringHelper.isEmpty(dlrxm)&&!StringHelper.isEmpty(dlrzjhm)){
				Map m = null;
				if(dlrzjhm.indexOf("45") == 0){
					m =	URLUtil.sandForPersonXX(postUrlPerson,dlrxm,dlrzjhm);
				}else{
					String content = "sqrxm="+dlrxm;
					content += "&zjh="+dlrzjhm;
					m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
				}
				Map<String, Object> dlrxx = new HashMap<String,Object>();
				dlrxx.put("dlrxm", m.get("name"));
				dlrxx.put("dlrzjhm", m.get("zjhm"));
				//dlrxx.put("picturecode64_dlr", m.get("photo").toString());
				dlrxx.put("errstr", m.get("errstr"));
				dlrxx.put("code", m.get("code"));
				dlrxx.put("jkyccode", m.get("jkyccode"));
				result.add(dlrxx);
				message.setSuccess("success");
			}
		}else{
			message.setSuccess("false");
		}
		message.setRows(result);
		message.setTotal(result.size());
		return message;
	}
	
	
	
	/**
	 * 增加验证申请人信息日志
	 * @date 2018-08-08 16:48:30
	 * @author liangc
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/add/validatesqrxxlog", method = RequestMethod.POST)
	public @ResponseBody Message AddValidateSQRXXLOG(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();
		User user = Global.getCurrentUserInfo();
		String sqrxm = request.getParameter("sqrxm");
		String zjlx = request.getParameter("zjlx");
		String zjh = request.getParameter("zjh");
		String sqrlb = request.getParameter("sqrlb");
		String sqrlx = request.getParameter("sqrlx");
		String fddbr = request.getParameter("fddbr");
		String fddbrzjlx = request.getParameter("fddbrzjlx");
		String fddbrzjhm = request.getParameter("fddbrzjhm");
		String dlrxm = request.getParameter("dlrxm");
		String dlrzjlx = request.getParameter("dlrzjlx");
		String dlrzjhm = request.getParameter("dlrzjhm");
		String validateuser = "";
		if(user != null){
			validateuser = user.getLoginName();
		}
		String ywlsh = request.getParameter("ywlsh");
		String sqrxm_jk = request.getParameter("sqrxm_jk");
		String zjlx_jk = request.getParameter("zjlx_jk");
		String zjh_jk = request.getParameter("zjh_jk");
		String sqrlb_jk = request.getParameter("sqrlb_jk");
		String sqrlx_jk = request.getParameter("sqrlx_jk");
		String fddbr_jk = request.getParameter("fddbr_jk");
		String fddbrzjlx_jk = request.getParameter("fddbrzjlx_jk");
		String fddbrzjhm_jk = request.getParameter("fddbrzjhm_jk");
		String dlrxm_jk = request.getParameter("dlrxm_jk");
		String dlrzjlx_jk = request.getParameter("dlrzjlx_jk");
		String dlrzjhm_jk = request.getParameter("dlrzjhm_jk");
		String validatestatus = request.getParameter("validatestatus");
		String codeqlr = request.getParameter("codeqlr");
		String jkyccodeqlr = request.getParameter("jkyccodeqlr");
		String codedlr = request.getParameter("codedlr");
		String jkyccodedlr = request.getParameter("jkyccodedlr");
		LOG_MSGVALIDATE validatexx = new LOG_MSGVALIDATE();
		validatexx.setSQRXM(StringHelper.formatObject(sqrxm));
		validatexx.setZJLX(StringHelper.formatObject(zjlx));
		validatexx.setZJH(StringHelper.formatObject(zjh));
		validatexx.setSQRLB(StringHelper.formatObject(sqrlb));
		validatexx.setSQRLX(StringHelper.formatObject(sqrlx));
		validatexx.setFDDBR(StringHelper.formatObject(fddbr));
		validatexx.setFDDBRZJLX(StringHelper.formatObject(fddbrzjlx));
		validatexx.setFDDBRZJHM(StringHelper.formatObject(fddbrzjhm));
		validatexx.setDLRXM(StringHelper.formatObject(dlrxm));
		validatexx.setDLRZJLX(StringHelper.formatObject(dlrzjlx));
		validatexx.setDLRZJHM(StringHelper.formatObject(dlrzjhm));
		validatexx.setVLIDATEUSER(StringHelper.formatObject(validateuser));
		validatexx.setVLIDATETIME(new Date());
		validatexx.setYWLSH(StringHelper.formatObject(ywlsh));
		validatexx.setSQRXM_JK(StringHelper.formatObject(sqrxm_jk));
		validatexx.setZJLX_JK(StringHelper.formatObject(zjlx_jk));
		validatexx.setZJH_JK(StringHelper.formatObject(zjh_jk));
		validatexx.setSQRLB_JK(StringHelper.formatObject(sqrlb_jk));
		validatexx.setSQRLX_JK(StringHelper.formatObject(sqrlx_jk));
		validatexx.setFDDBR_JK(StringHelper.formatObject(fddbr_jk));
		validatexx.setFDDBRZJLX_JK(StringHelper.formatObject(fddbrzjlx_jk));
		validatexx.setFDDBRZJHM_JK(StringHelper.formatObject(fddbrzjhm_jk));
		validatexx.setDLRXM_JK(StringHelper.formatObject(dlrxm_jk));
		validatexx.setDLRZJLX_JK(StringHelper.formatObject(dlrzjlx_jk));
		validatexx.setDLRZJHM_JK(StringHelper.formatObject(dlrzjhm_jk));
		validatexx.setVALIDATESTATUS(StringHelper.formatObject(validatestatus));
		validatexx.setCODEQLR(codeqlr);
		validatexx.setJKYCCODEQLR(jkyccodeqlr);
		validatexx.setCODEDLR(codedlr);
		validatexx.setJKYCCODEDLR(jkyccodedlr);
		baseCommonDao.save(validatexx);
		baseCommonDao.flush();
		
		List<LOG_MSGVALIDATE> list = new ArrayList<LOG_MSGVALIDATE>();
		list.add(validatexx);
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		msg.setRows(list);
		msg.setTotal(1);
		return msg;
	}
}
