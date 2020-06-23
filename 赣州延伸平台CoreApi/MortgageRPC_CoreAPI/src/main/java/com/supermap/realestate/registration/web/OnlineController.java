package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.OnlineService;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:西安交易共享
 * @author yuxuebin
 * @date 2016年6月25日 15:27:22
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/online")
@Component("OnlineController")
public class OnlineController {

	/** 登薄service */
	@Autowired
	private OnlineService onlineService;
	
	/**
	 * 服务接口：证书（证明校验服务）
	 * @Title: GetZSInfo
	 * @author:yuxuebin
	 * @date：2016年10月13日 20:05:20
	 * @return
	 */
	@RequestMapping(value = "zsinfo/{certtype}/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage GetZSInfo(@PathVariable("certtype") String certtype,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage msg=new ResultMessage();
		request.setCharacterEncoding("UTF-8");
		
		String bdcqzh=request.getParameter("bdcqzh");
		bdcqzh = new String (bdcqzh.getBytes("iso-8859-1"), "UTF-8") ;
		String zsbh=request.getParameter("zsbh");
		zsbh = new String (zsbh.getBytes("iso-8859-1"), "UTF-8") ;
		String qlrmc=request.getParameter("qlrmc");
		qlrmc = new String (qlrmc.getBytes("iso-8859-1"), "UTF-8") ;
		String qlrzjh=request.getParameter("qlrzjh");
		qlrzjh = new String (qlrzjh.getBytes("iso-8859-1"), "UTF-8") ;
		HashMap<String,String> param=new HashMap<String, String>();
		if(StringHelper.isEmpty(bdcqzh)){
			msg.setSuccess("false");
			msg.setMsg("001");//不动产权证号或不动产证明号为空！
			return msg;
		}
		param.put("zsbh", zsbh);
		param.put("qlrmc", qlrmc);
		param.put("qlrzjh", qlrzjh);
		msg=onlineService.GetZSInfo(certtype,bdcqzh,param);
		return msg;
	}
}
