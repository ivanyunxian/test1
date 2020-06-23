package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.service.SmsManagerService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 短信服务Controller
 * @ClassName: SmsManagerController
 * @author taochunda
 * @date 2019年05月15日 22:34:33
 */
@Controller
@RequestMapping("/smsmanager")
public class SmsManagerController {

	@Autowired
	private SmsManagerService smsManagerService;
	@Autowired
	private SmProInstService smProInstService;

	private final String prefix = "/realestate/registration/";

	/*****************************************短信模板管理*****************************************/

	/**
	 * 短信模板管理页面(URL:"/templatemanager/index/",Method：POST)
	 * @Title: getTemplateManagerIndex
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @return
	 */
	@RequestMapping(value = "/templatemanager/index/")
	public String getTemplateManagerIndex() {
		return prefix + "smsmanage/templatemanager";
	}

	/**
	 * 新增短信模板 (URL:"/add/template/",Method：POST)
	 * @Title: AddTemplate
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add/template/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddTemplate(HttpServletRequest request) throws UnsupportedEncodingException {
		return smsManagerService.AddTemplate(request);
	}

	/**
	 * 编辑短信模板 (URL:"/update/template/",Method：POST)
	 * @Title: UpdateTemplate
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update/template/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateTemplate(HttpServletRequest request) throws UnsupportedEncodingException {
		return smsManagerService.UpdateTemplate(request);
	}

	/**
	 * 删除短信模板 (URL:"/template/{id}",Method：DELETE)
	 * @Title: RemoveTemplate
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @param request
	 * @param templateid
	 * @return
	 */
	@RequestMapping(value = "/template/{templateid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveTemplate(@PathVariable("templateid") String templateid, HttpServletRequest request) {
		return smsManagerService.RemoveTemplate(templateid, request);
	}

	/**
	 * 分页获取短信模板(URL:"/template/info",Method：GET)
	 * @Title: GetSmsTempletList
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @return
	 */
	@RequestMapping(value = "/template/info", method = RequestMethod.GET)
	public @ResponseBody Message GetSmsTempletList(HttpServletRequest request) throws UnsupportedEncodingException {
		return smsManagerService.GetSmsTempletList(request);
	}

	/*****************************************短信模板管理*****************************************/

	/*****************************************短信推送相关*****************************************/
	/**
	 * 获取短信推送记录列表（URL:"/{xmbh}/smspushinfo",Method：GET）
	 * @Title: GetSmsPushList
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/smspushinfo", method = RequestMethod.GET)
	public @ResponseBody Message GetSmsPushList(@PathVariable("xmbh") String xmbh, HttpServletRequest request) {
		return smsManagerService.GetSmsPushList(xmbh, request);
	}

	/**
	 * 短信推送（URL:"/{xmbh}/smspushinfo",Method：POST）
	 * @Title: SmsPush
	 * @author:taochunda
	 * @date：2019年05月15日 22:34:33
	 * @return
	 */
	@RequestMapping(value = "/smspush/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody Message SmsPush(@PathVariable("xmbh") String xmbh, HttpServletRequest request) throws UnsupportedEncodingException {
		return smsManagerService.SmsPush(xmbh, request);
	}

	/**
	 * 短信推送（URL:"/sendsms/",Method：POST）
	 * @Title: sendSms
	 * @author:taochunda
	 * @date：2019年06月11日 17:02:24
	 * @return
	 */
	@RequestMapping(value = "/sendsms/", method = RequestMethod.POST)
	public @ResponseBody JSONObject sendSms(HttpServletRequest request) {
		return smsManagerService.sendSms(request);
	}

	/**
	 * @Author taochunda
	 * @Description 插入自动推送短信日志表
	 * @Date 2019-06-27 11:55
	 * @Param [request]
	 * @return com.supermap.wisdombusiness.web.ResultMessage
	 **/
	@RequestMapping(value = "/insertSmsLog", method = RequestMethod.POST)
	public @ResponseBody ResultMessage insertSmsLog(HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("插入短信推送日志失败！");
		String filenumber = request.getParameter("filenumber");
		boolean b = smProInstService.SmsSend(filenumber);
		if (b) {
			msg.setSuccess("true");
			msg.setMsg("成功插入短信推送日志！");
		}
		return msg;
	}
	/*****************************************短信推送相关*****************************************/
	/*****************************************短信台账相关↓*****************************************/
	/**
	 * @Author taochunda
	 * @Description 短信推送台账页面
	 * @Date 2019-07-17 15:54
	 * @Param []
	 * @return java.lang.String
	 **/
	@RequestMapping(value = "/smspushinfoindex", method = RequestMethod.GET)
	public String SmsPushInfoIndex() {
		return prefix + "smsmanage/smsPushInfo";
	}

	/**
	 * @Author taochunda
	 * @Description 获取短信推送台账详情
	 * @Date 2019-07-17 16:18
	 * @Param [request, response]
	 * @return List<Map>
	 **/
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/smspushlist",method = RequestMethod.GET)
	@ResponseBody
	public Message GetSmsPushList(HttpServletRequest request) throws UnsupportedEncodingException {
		return smsManagerService.GetSmsPushList(request);
	}

	/**
	 * @Author taochunda
	 * @Description 获取短信推送台账汇总
	 * @Date 2019-07-17 16:18
	 * @Param [request, response]
	 * @return List<Map>
	 **/
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/smspushlist/hz",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject GetSmsPushList_HZ(HttpServletRequest request) throws UnsupportedEncodingException {
		return smsManagerService.GetSmsPushList_HZ(request);
	}
	/*****************************************短信推送相关↑*****************************************/

}
