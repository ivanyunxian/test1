package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.service.QueryService_hn;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/queryhn")
@Component("QueryController_hn")
public class QueryController_hn {
	@Autowired
	private QueryService_hn queryService;
	@Autowired
	private CommonDao baseCommonDao;
	
	/** 根据QLRMC、ZJH查询 - 海南  --目前正式用的是此接口
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/houseQueryhncs/{qlrmc}/{zjh}", method = RequestMethod.GET)
	public @ResponseBody Map GetHouseQueryListhn_cs(@PathVariable("qlrmc") String qlrmc,@PathVariable("zjh") String qlrzjh,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map result = new HashMap();
		if (!StringHelper.isEmpty(qlrmc) && !StringHelper.isEmpty(qlrzjh)) {
			result = queryService.queryHousehn(request, qlrmc, qlrzjh);
		}
		return result;
	}
	
	/** 根据QLRMC、ZJH查询 - 海南  
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/houseQueryhn", method = RequestMethod.POST)
	public @ResponseBody Map GetHouseQueryListhn(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map result = new HashMap();
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		if (!StringHelper.isEmpty(qlrmc) && !StringHelper.isEmpty(qlrzjh)) {
			result = queryService.queryHousehn(request, qlrmc, qlrzjh);
		}
		return result;
	}
	
	
}
