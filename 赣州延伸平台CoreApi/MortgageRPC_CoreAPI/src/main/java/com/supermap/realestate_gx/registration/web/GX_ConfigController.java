package com.supermap.realestate_gx.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import parsii.eval.Scope;
import parsii.eval.Variable;


//import com.supermap.realestate.registration.factorys.GX_HandlerFactory;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.CheckConfig;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckGroup;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckItem;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.mapping.ICheckItem;
import com.supermap.realestate.registration.model.BDCS_CONSTRAINT;
import com.supermap.realestate.registration.model.BDCS_CONSTRAINTRT;
import com.supermap.realestate.registration.util.ConstValue.ConstraintsType;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.SystemConfig;
import com.supermap.realestate_gx.registration.factorys.GX_HandlerFactory;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfd_Pass_Condition;

/**
 * 配置Controller，流程相关的配置放在这里边
 * @ClassName: ConfigController
 * @author liushufeng
 * @date 2015年7月23日 下午11:10:04
 */
@Controller
@RequestMapping("/config")
public class GX_ConfigController {
	@Autowired
	private CommonDao dao;

	private final String prefix = "/realestate/registration/";
	/**
	 * 返回页面控件配置
	 * @author wuzhu
	 * @date 2015-8-10
	 * @param request
	 * @param Route_Id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pagecontrolsconfig_gx/{project_id}/{pagetype}", method = RequestMethod.GET)
	public @ResponseBody Map getPageControlsFromConfig(HttpServletRequest request
			, @PathVariable("project_id") String project_id
			, @PathVariable("pagetype") String pagetype) {
 	RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
	Map config=new HashMap<String, String>();
 	if(flow!=null)
	 config =GX_HandlerFactory.getPageControlsConfig(flow.getName().hashCode(),pagetype);
	return config;
	}
	/**
	 * 返回提示配置数据
	 * @author wuzhu
	 * @date 2015-8-10
	 * @param request
	 * @param Route_Id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/alertconfig", method = RequestMethod.GET)
	public @ResponseBody String getAlertConfig(HttpServletRequest request) {
      return StringHelper.readFile(request.getRealPath("/")+ "WEB-INF\\classes\\提示配置.txt");
	}
}
