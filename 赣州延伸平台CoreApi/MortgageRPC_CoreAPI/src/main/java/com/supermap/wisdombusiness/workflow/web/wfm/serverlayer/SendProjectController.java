package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.wfm.SendProjectService;

@Controller
@RequestMapping("/sendproject")
public class SendProjectController {
	@Autowired
	private SendProjectService sendProjectService;

	@RequestMapping(value = "/average/{acinstid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo AverageSendProject(@PathVariable String acinstid,
			HttpServletRequest request, HttpServletResponse response) {
		return sendProjectService.AverageSendProject(acinstid);
	}

	@RequestMapping(value = "/efficient/{acinstid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo EfficientSendProject(@PathVariable String acinstid,
			HttpServletRequest request, HttpServletResponse response) {
		return sendProjectService.EfficientSendProject(acinstid);
	}
	/**
	 * 当前类型数量少者优先
	 * @param acinstid
	 * @param request
	 * @param response
	 * @return
	 * 2015年10月16日
	 */
	@RequestMapping(value = "/lessfirst/{acinstid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo LessSendProject(@PathVariable String acinstid,
			HttpServletRequest request, HttpServletResponse response) {
		return sendProjectService.LessSendProject(acinstid);
	}
	/**
	 * 工作少者优先
	 *
	 * @param acinstid
	 * @param request
	 * @param response
	 * @return
	 * 2015年10月16日
	 */
	@RequestMapping(value = "/lessworkfirst/{acinstid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo LessWorkSendProject(@PathVariable String acinstid,
			HttpServletRequest request, HttpServletResponse response) {
		return sendProjectService.LessWorkSendProject(acinstid);
	}

}
