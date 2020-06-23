package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.realestate.registration.service.InlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.NoticeService;

/**
 * 
 * @Description:公告服务
 * @author yuxuebin
 * @date 2017年03月16日 10:37:22
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

	/** 公告service */
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private InlineService inlineService;
	
	/**
	 * 服务接口：获取公告信息
	 * @Title: GetNoticeInfo
	 * @author:yuxuebin
	 * @date：2017年03月16日 10:43:20
	 * @return
	 */
	@RequestMapping(value = "noticeinfo/{project_id}/{noticetype}/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> GetNoticeInfo(@PathVariable("project_id") String project_id,@PathVariable("noticetype") String noticetype,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		HashMap<String, Object> result = noticeService.GetNoticeInfo(project_id,noticetype);
		Map<String, Integer> dates = this.inlineService.getLockTime(project_id);
		result.putAll(dates);
        String year = String.valueOf(dates.get("lockYear"));
        String xzqhdm = this.inlineService.getAreaCodeByProjectID(project_id);
		result.put("noticeSn", this.inlineService.createNoticeSn(year, xzqhdm));
		return  result;
	}
}
