/**
 * 收费统计
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.DYJETJService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.web.Message;

/**
 * @author zhaomengfan
 * @Description: 收费统计
 */
@Controller
@RequestMapping("/DYJETJ")
public class DYJETJController {

	@Autowired
	private DYJETJService dyjetjservice;
	
	/**
	 * @Description: 页面跳转
	 * @Title: ShowSFTJIndex
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:51:03
	 * @return String
	 */
	@RequestMapping(value = "/Index", method = RequestMethod.GET)
	public String ShowSFTJIndex() {
		return "/realestate/registration/tj/DYJETJIndex";
	}
	
	/**
	 * @Description: 获取查询界面的下拉框内容
	 * @Title: GetCombobox
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午5:32:58
	 * @param request
	 * @param response
	 * @return
	 * @return List<Message>
	 */
	@RequestMapping(value = "/GetCombobox", method = RequestMethod.GET)
	public @ResponseBody Message GetCombobox(HttpServletRequest request, HttpServletResponse response) {
		return dyjetjservice.GetCombobox();
	}

	/**
	 * @Description: 收费统计数据获取
	 * @Title: GetSFTJ
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:48:12
	 * @param request
	 * @param response
	 * @return Message
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/GetSFTJ", method = RequestMethod.GET)
	public @ResponseBody Message GetSFTJ(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String,String> Param = new HashMap<String, String>();
		Param.put("DYWLX", RequestHelper.getParam(request, "DYWLX"));
		Param.put("DBSJ_Q", RequestHelper.getParam(request, "DBSJ_Q")+" 00:00:00");
		Param.put("DBSJ_Z", RequestHelper.getParam(request, "DBSJ_Z")+" 23:59:59");
		Param.put("QLR", RequestHelper.getParam(request, "QLR"));
		Param.put("YWR", RequestHelper.getParam(request, "YWR"));
		return dyjetjservice.GetDYJETJ(Param);
	}
}
