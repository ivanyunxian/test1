package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.supermap.realestate.registration.service.DealService;
import com.supermap.realestate.registration.util.RequestHelper;

/**
 * 交易控制层
 * @author weilb
 */
@Controller
@RequestMapping("/deal")

public class DealController {
   
	@Autowired
	private DealService dealService;
	
	/**
	 * 获取交易详细信息
	 * @return Map<String,Object>
	 * Object一个是Map<String,String> 存土地单元
	 * Object另一个是List<Map>存土地上的多个权利人
	 */
	@RequestMapping(value = "/details",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getDealDetails(HttpServletRequest request ,HttpServletResponse response){
		String BDCDYH = request.getParameter("BDCDYH");
		String BDCDYID = request.getParameter("BDCDYID");
		return dealService.getDealDetails(BDCDYH,BDCDYID);
	}
	
	/**
     * 通过权证号精确获取信息
     * @param QZH
     * @return Map<String,List<Map>> 
     */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/getdetailsbyqzh",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getZDInfoByQZH(HttpServletRequest request ,HttpServletResponse response){
		RequestHelper rh = new RequestHelper();
		String QZH="";
		try {
			QZH = rh.getParam(request,"QZH");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dealService.getZDInfoByQZH(QZH);
	}

}
