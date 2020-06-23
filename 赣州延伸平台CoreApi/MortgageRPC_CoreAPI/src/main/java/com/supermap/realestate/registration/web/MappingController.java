package com.supermap.realestate.registration.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.service.MappingService;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @ClassName:
 * @author mss
 * @date
 */
@Controller
@RequestMapping("/processes")
public class MappingController {

	@Autowired
	private MappingService mappingService;

	// 打开主页面的映射
	@RequestMapping(value = "/mapping", method = RequestMethod.GET)
	public String ShowMappingMain(Model model) {

		return "/workflow/processes/mapping";
	}

	/**
	 * 
	 * @author mss
	 * @param id
	 * @param request
	 * @param response
	 * @return获取流程信息通过ID
	 */
	@RequestMapping(value = "/mapping/{id}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> GetWfd_Prodef(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		return mappingService.GetLcxx(id);
	}

	/**
	 * Form表单数据 保存或更新
	 * 
	 * @author mss
	 * @param id
	 * @param mater
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/mapping/form", method = { RequestMethod.POST })
	@ResponseBody
	public ResultMessage SaveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		return mappingService.SaveOrUpdate(request);
	}
	
	/**
	 * 
	 * @author mss
	 * @param id
	 * @param request
	 * @param response
	 * @return获取流程信息通过ID
	 */
	@RequestMapping(value = "/mapping/getworkflows", method = RequestMethod.GET)
	public @ResponseBody List<RegisterWorkFlow> getWorkflows(HttpServletRequest request,
			HttpServletResponse response) {
		return mappingService.getWorkflows();
	}
	
	@RequestMapping(value = "/mapping/getreadcontractbtn/{XMBH}", method = RequestMethod.GET)
	public @ResponseBody int getReadcontractbtn(@PathVariable("XMBH") String XMBH,HttpServletRequest request,
			HttpServletResponse response) {
		return mappingService.getReadcontractbtn(XMBH);
	}
	/**
	 *  获取业务流程配置的“是否允许使用申请人新增按钮”字段的值
	 *  @param request 
	 *  @param response
	 *  @return "0" (允许) 或   "1" （禁止） 
	 */
	@RequestMapping(value = "/mapping/isallowaddsqr/{XMBH}", method = RequestMethod.GET)
	public @ResponseBody int getIsAllowAddSqr(@PathVariable("XMBH") String XMBH,HttpServletRequest request,
			HttpServletResponse response) {
		return mappingService.getIsAllowAddSqr(XMBH);
	}
	
	
}
