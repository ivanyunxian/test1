package com.supermap.realestate.registration.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.config.CommonCheck;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.CheckConfig;
import com.supermap.realestate.registration.util.*;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import parsii.tokenizer.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

/**
 * 配置Controller，流程相关的配置放在这里边
 * @ClassName: ConfigController
 * @author liushufeng
 * @date 2015年7月23日 下午11:10:04
 */
@Controller
@RequestMapping("/manage")
public class ManageController {

	@Autowired
	private CommonDao dao;

	// @Autowired
	// private ConfigService configservice;

	private final String prefix = "/realestate/registration/manage";

	/**
	 * 系统后台管理首页
	 * @Title: getConstraintsIndex
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:29:53
	 * @return
	 */
	@RequestMapping(value = "/")
	public String getConstraintsIndex() {
		return prefix + "/index";
	}

	/**
	 * 禁用项目信息缓存成功
	 * @Title: disablexmxxcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:06
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/disablexmxxcache", method = RequestMethod.GET)
	public @ResponseBody String disablexmxxcache(HttpServletRequest request, HttpServletResponse response) {
		Global.USECACHE_XMXX = false;
		return "禁用项目信息缓存成功";
	}

	/**
	 * 禁用可受理流程缓存成功
	 * @Title: disableacceptableflowcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:14
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/disableacceptableflowcache", method = RequestMethod.GET)
	public @ResponseBody String disableacceptableflowcache(HttpServletRequest request, HttpServletResponse response) {
		Global.USECACHE_ACCEPT_WORKFLOW = false;
		return "禁用可受理流程缓存成功";
	}

	/**
	 * 禁用配置流程缓存成功
	 * @Title: disableconfigflowcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:23
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/disableconfigflowcache", method = RequestMethod.GET)
	public @ResponseBody String disableconfigflowcache(HttpServletRequest request, HttpServletResponse response) {
		Global.USECACHE_CONFIG_WORKFLOW = false;
		return "禁用配置流程缓存成功";
	}

	/**
	 * 启用项目信息缓存成功
	 * @Title: enablexmxxcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/enablexmxxcache", method = RequestMethod.GET)
	public @ResponseBody String enablexmxxcache(HttpServletRequest request, HttpServletResponse response) {
		Global.USECACHE_XMXX = true;
		return "启用项目信息缓存成功";
	}

	/**
	 * 启用可受理流程缓存成功
	 * @Title: enableacceptableflowcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:40
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/enableacceptableflowcache", method = RequestMethod.GET)
	public @ResponseBody String enableacceptableflowcache(HttpServletRequest request, HttpServletResponse response) {
		Global.USECACHE_ACCEPT_WORKFLOW = true;
		return "启用可受理流程缓存成功";
	}

	/**
	 * 启用配置流程缓存成功
	 * @Title: enableconfigflowcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:48
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/enableconfigflowcache", method = RequestMethod.GET)
	public @ResponseBody String enableconfigflowcache(HttpServletRequest request, HttpServletResponse response) {
		Global.USECACHE_CONFIG_WORKFLOW = true;
		return "启用配置流程缓存成功";
	}

	/**
	 * 清空人员可受理流程缓存成功
	 * @Title: clearacceptableflowcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:48:55
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clearacceptableflowcache", method = RequestMethod.GET)
	public @ResponseBody String clearacceptableflowcache(HttpServletRequest request, HttpServletResponse response) {
		if (SmProDefService.CACHE_ACCEPTFLOW != null)
			SmProDefService.CACHE_ACCEPTFLOW.clear();
		return "清空人员可受理流程缓存成功";
	}

	/**
	 * 清空所有流程缓存
	 * @Title: clearconfigflowcache
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:49:03
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clearconfigflowcache", method = RequestMethod.GET)
	public @ResponseBody String clearconfigflowcache(HttpServletRequest request, HttpServletResponse response) {
		if (SmProDef.CACHE_ALLFLOW != null)
			SmProDef.CACHE_ALLFLOW.clear();
		return "清空所有流程缓存成功";
	}

	/**
	 * 清空项目信息缓存
	 * @Title: clearxmxxcache
	 * @author:liushufeng
	 * @date：2016年2月2日 下午3:46:03
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clearxmxxcache", method = RequestMethod.GET)
	public @ResponseBody String clearxmxxcache(HttpServletRequest request, HttpServletResponse response) {
		Global.clearXMXXCache();
		return "清空项目信息缓存";
	}
	
	/**
	 * 重新加载HandlerMapping.xml配置文件
	 * @Title: reloadMappingConfig
	 * @author:liushufeng
	 * @date：2015年8月1日 上午3:47:35
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reloadmappingconfig", method = RequestMethod.GET)
	public @ResponseBody String reloadMappingConfig(HttpServletRequest request, HttpServletResponse response) {
		HandlerFactory.reloadMappingConfig();
		return "重新加载HandlerMapping.xml配置文件成功!";
	}

	/**
	 * 禁止关联宗地
	 * @作者 海豹
	 * @创建时间 2015年8月1日下午11:04:52
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/disablerelationland", method = RequestMethod.GET)
	public @ResponseBody String disablerelationland(HttpServletRequest request, HttpServletResponse response) {
		Global.ISRELATIONLAND = false;
		return "取消宗地关联";
	}

	/**
	 * 启用关联宗地
	 * @作者 海豹
	 * @创建时间 2015年8月1日下午11:04:47
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/enablerelationland", method = RequestMethod.GET)
	public @ResponseBody String enablerelationland(HttpServletRequest request, HttpServletResponse response) {
		Global.ISRELATIONLAND = true;
		return "启用宗地关联";
	}

	/**
	 * 启用发送消息
	 * @Title: enableSendmessage
	 * @author:liushufeng
	 * @date：2015年8月10日 上午2:28:37
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/enablesendmessage")
	public @ResponseBody String enableSendmessage(HttpServletRequest request, HttpServletResponse response) {
		Global.SENDMESSAGE = true;
		return "启用发送消息成功";
	}

	/**
	 * 禁用发送消息
	 * @Title: disableSendmessage
	 * @author:liushufeng
	 * @date：2015年8月10日 上午2:28:48
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/disablesendmessage")
	public @ResponseBody String disableSendmessage(HttpServletRequest request, HttpServletResponse response) {
		Global.SENDMESSAGE = false;
		return "禁用发送消息成功";
	}

	@RequestMapping(value = "/test/{result}/{expr}", method = RequestMethod.GET)
	public @ResponseBody boolean test(HttpServletRequest request, HttpServletResponse response, @PathVariable("result") String result, @PathVariable("expr") String expr)
			throws ParseException {
		@SuppressWarnings("unused")
		CheckConfig config = HandlerFactory.getCheckConfig();
		long dd = Long.parseLong(result);
		boolean b = CommonCheck.calculateExpr(dd, expr);
		return b;
	}

	/**
	 * 保存打印模板
	 * @Title: disablegeooperate
	 * @author:WUZ
	 * @date：2015年10月21日 上午11:15:48
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{tpl}/saveprinttpl", method = RequestMethod.POST)
	public @ResponseBody String saveprinttpl(@PathVariable("tpl") String tpl, HttpServletRequest request, HttpServletResponse response) {
		FileOutputStream fop = null;
		File file = null;
		String r = "true";
		String filepath = "";
		String dataString = "";
		StringBuilder tplHtml = new StringBuilder();
		String tplHtmlString = "";
		if (StringHelper.isEmpty(tpl))
			return "false";
		try {
			dataString = request.getParameter("info");
			JSONObject htmlobject = JSON.parseObject(dataString);

			if (StringHelper.isEmpty(dataString))
				return "false";
			filepath = String.format("%s\\resources\\template\\%s.html", request.getRealPath("/"), tpl);
			if (tpl.equals("zszm")) {
				tplHtml.append("{{# function _getValue(v){ if(v==undefined){return '';} return v;} }}");
				tplHtml.append("<div id=\"mytplrang\" style='#mytplrang");
				tplHtml.append("{{# if(d.is_td==false) {}} background: url(../../resources/images/zszm.png) 0px 0px / cover no-repeat; {{# } }} '>");
				tplHtml.append("<div id=\"id_zsbh\" class=\"drag noprint\" style='#id_zsbh'>{{_getValue(d?d.id_zsbh:'') }}</div>");
				tplHtml.append("<div id=\"qhjc\" class=\"drag noprint\" style='#qhjc'>{{_getValue(d?d.qhjc:'') }}</div>");
				tplHtml.append("<div id=\"nd\" class=\"drag noprint\" style='#nd'>{{_getValue(d?d.nd:'') }}</div>");
				tplHtml.append("<div id=\"qhmc\" class=\"drag noprint\" style='#qhmc'>{{_getValue(d?d.qhmc:'') }}</div>");
				tplHtml.append("<div id=\"cqzh\" class=\"drag noprint\" style='#cqzh'>{{_getValue(d?d.cqzh:'') }}</div>");
				tplHtml.append("<div id=\"zmqlhsx\" class=\"drag noprint\" style='#zmqlhsx'>{{_getValue(d?d.zmqlhsx:'') }}</div>");
				tplHtml.append("<div id=\"qlr\" class=\"drag noprint\" style='#qlr'>{{_getValue(d?d.qlr:'') }}</div>");
				tplHtml.append("<div id=\"ywr\" class=\"drag noprint\" style='#ywr'>{{_getValue(d?d.ywr:'') }}</div>");
				tplHtml.append("<div id=\"zl\" class=\"drag noprint\" style='#zl'>{{_getValue(d?d.zl:'') }}</div>");
				tplHtml.append("<div id=\"bdcdyh\" class=\"drag noprint\" style='#bdcdyh'>{{_getValue(d?d.bdcdyh:'') }}</div>");
				tplHtml.append("<div id=\"qt\" class=\"drag noprint\" style='#qt'>{{_getValue(d?d.qt:'') }}</div>");
				tplHtml.append("<div id=\"fj\" class=\"drag noprint\" style='#fj'>{{_getValue(d?d.fj:'') }}</div>");
				tplHtml.append("<div id=\"qrcode\" class=\"drag2 noprint\" style='#qrcode'><img  style=\"width:140px;height:140px\" alt=\"{{_getValue(d?d.qrcodename:'') }}\" src=\"{{_getValue(d?d.qrcode:'') }}\"/></div>");
				tplHtml.append("<div id=\"fm_year\" class=\"drag noprint\" style='#fm_year'>{{_getValue(d?d.fm_year:'') }}</div>");
				tplHtml.append("<div id=\"fm_month\" class=\"drag noprint\" style='#fm_month'>{{_getValue(d?d.fm_month:'') }}</div>");
				tplHtml.append("<div id=\"fm_day\" class=\"drag noprint\" style='#fm_day'>{{_getValue(d?d.fm_day:'') }}</div>");
				tplHtml.append("</div>");
			}
			if (tpl.equals("zsxx_nr")) {
				tplHtml.append("{{# function _getValue(v){ if(v==undefined){return '';} return v;} }}");
				tplHtml.append("<div id=\"mytplrang\" style='#mytplrang");
				tplHtml.append("{{# if(d.is_td==false) {}}  background: url(../../resources/images/zsxx_nr.png) 0px 0px / cover no-repeat; {{# } }} '>");
				tplHtml.append("<div id=\"syqx\" class=\"drag noprint\" style='#syqx'>{{_getValue(d?d.syqx:'') }}</div>");
				tplHtml.append("<div id=\"qhjc\" class=\"drag noprint\" style='#qhjc'>{{_getValue(d?d.qhjc:'') }}</div>");
				tplHtml.append("<div id=\"nd\" class=\"drag noprint\" style='#nd'>{{_getValue(d?d.nd:'') }}</div>");
				tplHtml.append("<div id=\"qhmc\" class=\"drag noprint\" style='#qhmc'>{{_getValue(d?d.qhmc:'') }}</div>");
				tplHtml.append("<div id=\"cqzh\" class=\"drag noprint\" style='#cqzh'>{{_getValue(d?d.cqzh:'') }}</div>");
				tplHtml.append("<div id=\"mj\" class=\"drag noprint\" style='#mj'>{{_getValue(d?d.mj:'') }}</div>");
				tplHtml.append("<div id=\"qlr\" class=\"drag noprint\" style='#qlr'>{{_getValue(d?d.qlr:'') }}</div>");
				tplHtml.append("<div id=\"yt\" class=\"drag noprint\" style='#yt'>{{_getValue(d?d.yt:'') }}</div>");
				tplHtml.append("<div id=\"zl\" class=\"drag noprint\" style='#zl'>{{_getValue(d?d.zl:'') }}</div>");
				tplHtml.append("<div id=\"bdcdyh\" class=\"drag noprint\" style='#bdcdyh'>{{_getValue(d?d.bdcdyh:'') }}</div>");
				tplHtml.append("<div id=\"gyqk\" class=\"drag noprint\" style='#gyqk'>{{_getValue(d?d.gyqk:'') }}</div>");
				tplHtml.append("<div id=\"fj\" class=\"drag noprint\" style='#fj'>{{_getValue(d?d.fj:'') }}</div>");
				tplHtml.append("<div id=\"qllx\" class=\"drag noprint\" style='#qllx'>{{_getValue(d?d.qllx:'') }}</div>");
				tplHtml.append("<div id=\"qlxz\" class=\"drag noprint\" style='#qlxz'>{{_getValue(d?d.qlxz:'') }}</div>");
				tplHtml.append("<div id=\"qlqtzk\" class=\"drag noprint\" style='#qlqtzk'>{{_getValue(d?d.qlqtzk:'') }}</div>");
				tplHtml.append("</div>");
			}
			if (tpl.equals("zsxx_fm")) {
				tplHtml.append("{{# function _getValue(v){ if(v==undefined){return '';} return v;} }}");
				tplHtml.append("<div id=\"mytplrang\" style='#mytplrang");
				tplHtml.append("{{# if(d.is_td==false) {}}   background: url(../../resources/images/zsxx_fm.png) 0px 0px / cover no-repeat; {{# } }} '>");
				tplHtml.append("<div id=\"id_zsbh\" class=\"drag noprint\" style='#id_zsbh'>{{_getValue(d?d.id_zsbh:'') }}</div>");
				tplHtml.append("<div id=\"fm_year\" class=\"drag noprint\" style='#fm_year'>{{_getValue(d?d.fm_year:'') }}</div>");
				tplHtml.append("<div id=\"fm_month\" class=\"drag noprint\" style='#fm_month'>{{_getValue(d?d.fm_month:'') }}</div>");
				tplHtml.append("<div id=\"fm_day\" class=\"drag noprint\" style='#fm_day'>{{_getValue(d?d.fm_day:'') }}</div>");
				tplHtml.append("<div id=\"qrcode\" class=\"drag2 noprint\" style='#qrcode'><img  style=\"width:140px;height:140px\" alt=\"{{_getValue(d?d.qrcodename:'') }}\" src=\"{{_getValue(d?d.qrcode:'') }}\"/></div>");
				tplHtml.append("</div>");
			}
			tplHtmlString = tplHtml.toString();
			for (Entry<String, Object> entry : htmlobject.entrySet()) {
				String _value = String.valueOf(entry.getValue()).replace("'", "");// 除去样式中的‘号
				tplHtmlString = tplHtmlString.replace("#" + entry.getKey(), _value);
			}
			file = new File(filepath);
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = tplHtmlString.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			r = "false";
			YwLogUtil.addYwLog("保存打印模板-失败", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			e.printStackTrace();
		} finally {
			try {
				if (fop != null){
					fop.close();
					YwLogUtil.addYwLog("保存打印模板-成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
				}
			} catch (IOException e) {
				YwLogUtil.addYwLog("保存打印模板-失败", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
				r = "false";
				e.printStackTrace();
			}
		}
		return r;
	}

	/**
	 * 启用发送消息
	 * @Title: enableSendmessage
	 * @author:liushufeng
	 * @date：2015年8月10日 上午2:28:37
	 * @param request
	 * @param response
	 * @return
	 */
	/**
	 * 启用数据操作日志记录服务
	 * @Title: enableDataLog
	 * @author:liushufeng
	 * @date：2015年11月15日 下午8:28:21
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/enabledatalog")
	public @ResponseBody String enableDataLog(HttpServletRequest request, HttpServletResponse response) {
		Global.USEDATAOPERATELOG = true;
		return "启用发送消息成功";
	}

	/**
	 * 禁用数据操作日志记录服务
	 * @Title: disableDataLog
	 * @author:liushufeng
	 * @date：2015年11月15日 下午8:28:45
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/disabledatalog")
	public @ResponseBody String disableDataLog(HttpServletRequest request, HttpServletResponse response) {
		Global.USEDATAOPERATELOG = false;
		return "禁用发送消息成功";
	}

	/**
	 * 清空数据字典缓存
	 * @Title: reloaddictionary
	 * @author:liushufeng
	 * @date：2015年11月26日 上午1:34:18
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reloaddictionary")
	public @ResponseBody String reloaddictionary(HttpServletRequest request, HttpServletResponse response) {
		ConstHelper.reload();
		return "重新加载数据字典成功";
	}
}
