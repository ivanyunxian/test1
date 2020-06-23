/**
 * 系统修正值模块
 */
package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.RESULT_FIXED;
import com.supermap.realestate.registration.service.RESULT_FIXEDService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * @author zhaomengfan
 * @Description: 修正值
 */
@Controller
@RequestMapping("/RESULTFIXED")
public class RESULT_FIXEDController {

	@Autowired
	private RESULT_FIXEDService result_fixedservice;
	
	/**
	 * @Description: 页面跳转
	 * @Title: ShowSFTJIndex
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:51:03
	 * @return String
	 */
	@RequestMapping(value = "/Index", method = RequestMethod.GET)
	public String ShowRESULT_FIXEDIndex() {
		return "/realestate/registration/config/RESULTFIXED";
	}
	

	/**
	 * @Description: 收费统计数据获取
	 * @Title: GetSFTJ
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:48:12
	 * @param request
	 * @param response
	 * @return Message
	 */
	@RequestMapping(value = "/GetRESULT_FIXED", method = RequestMethod.GET)
	public @ResponseBody Message GetRESULT_FIXED(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> Param = new HashMap<String, String>();
		try {
			Param.put("page",RequestHelper.getParam(request, "page"));
			Param.put("rows",RequestHelper.getParam(request, "rows"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result_fixedservice.GetRESULT_FIXED(Param);
	}
	
	@RequestMapping(value = "/UpdateRESULT_FIXED", method = RequestMethod.PUT)
	public @ResponseBody Message AddRESULT_FIXED(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> Param = new HashMap<String, String>();
		try {
			Param.put("jsonEnity", RequestHelper.getParam(request, "row"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result_fixedservice.AddRESULT_FIXED(Param);
	}
	
	@RequestMapping(value = "/UpdateRESULT_FIXED", method = RequestMethod.DELETE)
	public @ResponseBody Message DelRESULT_FIXED(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> Param = new HashMap<String, String>();
		try {
			Param.put("jsonEnity", RequestHelper.getParam(request, "row"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result_fixedservice.DelRESULT_FIXED(Param);
	}
	
	@RequestMapping(value = "/UpdateRESULT_FIXED", method = RequestMethod.POST)
	public @ResponseBody Message RESULT_FIXEDModify(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> Param = new HashMap<String, String>();
		try {
			Param.put("jsonEnity", RequestHelper.getParam(request, "row"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result_fixedservice.RESULT_FIXEDModify(Param);
	}
	
	@RequestMapping(value = "/GetCombobox", method = RequestMethod.GET)
	public @ResponseBody List<Object> GetCombobox(HttpServletRequest request, HttpServletResponse response) {
		return result_fixedservice.GetCombobox();
	}
	
}
