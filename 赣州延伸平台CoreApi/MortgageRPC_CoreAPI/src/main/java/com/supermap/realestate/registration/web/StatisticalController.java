package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.StatisticalService;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.web.StatisticalMessage;

/**
 * 
    *      统计分析
 * @author wanghongchao
 *
 */
@Controller("StatisticalController")
@RequestMapping("/statistical")
public class StatisticalController { 
	
	@Autowired
	StatisticalService statisticalService;
	
	private final String prefix = "/statistical/"; 
	
	/**
	    *    博白 房地产交易信息日报统计表 
	 * <pre>商品房统计说明（说明不用打印待报表上）以期房的预告登记、现房转移预告为准的，时间以登簿时间为准，时间需精确到时分秒					
	 * 	二手房转移统计说明（说明不用打印待报表上）以申请人双方都是个人为准的，
	 * 	这样就排除开发企业转移类的业务，时间以登簿时间为准，时间需精确到时分秒，
	 * </pre>					
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fdcjyrbtjindex",method=RequestMethod.GET)
	public String showFdcjyrbtjPage(Model model, HttpServletRequest request, HttpServletResponse response) {
		return prefix + "fdcjyrbtj/fdcjyrbtjIndex";
	}
	
	
	/**
	 *       博白 房地产交易信息日报统计查询
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getfdcjyrbtjlist",method = RequestMethod.GET)
	public @ResponseBody  StatisticalMessage getFdcjyrbtjList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		Map params = StringHelper.transToMAP(request.getParameterMap());
		return statisticalService.queryFdcjyrbtjData(params);
	} 
	@RequestMapping(value = "/getDivisionCodeTreeList")
	public @ResponseBody List<Tree> getDivisionCodeTreeList() {
		return statisticalService.getDivisionCodeTreeList();
	}
}
