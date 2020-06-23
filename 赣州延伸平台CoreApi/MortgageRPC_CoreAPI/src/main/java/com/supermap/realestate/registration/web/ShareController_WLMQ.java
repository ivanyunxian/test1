package com.supermap.realestate.registration.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.shareService_WLMQ;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:乌鲁木齐共享服务接口
 * @author yuxuebin
 * @date 2017年6月25日 15:12:22
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/share_wlmq")
public class ShareController_WLMQ {

	/** 共享接口service */
	@Autowired
	private shareService_WLMQ shareService;

	/**
	 * 服务接口：出图服务
	 * 
	 * @Title: getXmxxInfo
	 * @author:yuxuebin
	 * @date：2017年6月25日 15:26:20
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/xmxxinfo/{slrq}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> getXmxxInfo(@PathVariable("slrq") String slrq,
			HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,Object> result=new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 设置日期转化成功标识
		boolean dateflag = true;
		Date date=null;
		// 这里要捕获一下异常信息
		try {
			date = format.parse(slrq);
		} catch (Exception e) {
			dateflag = false;
		} finally {
			// 成功：true ;失败:false
			System.out.println("日期是否满足要求" + dateflag);
		}
		if(!dateflag||StringHelper.isEmpty(date)){
			result.put("success", "false");
			result.put("msg", "日期不符合格式要求：yyyy-MM-dd");
		}else{
			result.put("success", "true");
			List<HashMap<String,Object>> msg=shareService.getXmxxInfo(slrq);
			result.put("msg", msg);
		}
		return result;
	}
}
