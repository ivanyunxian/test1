package com.supermap.wisdombusiness.synchroinline.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.synchroinline.model.DicInfo;
import com.supermap.wisdombusiness.synchroinline.model.JsonMessage;
import com.supermap.wisdombusiness.synchroinline.service.DxtspzService;
import com.supermap.wisdombusiness.synchroinline.service.InlineProjectService;

@Controller
@RequestMapping(value = "/inline")
public class DxtspzController
{
	@Autowired
	private DxtspzService dxtspzservice;
	@Autowired
	InlineProjectService inlineService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/dxtspz")
	public String sms(HttpServletRequest req) throws Exception
	{
		List<Map> dxtsp = new ArrayList();
		dxtsp = dxtspzservice.getDxpzS();
		if (dxtsp != null && dxtsp.size() > 0)
		{
			req.setAttribute("tspzs", dxtsp);
		}
		else
		{
			req.setAttribute("tspzs", "");
		}
		return "/realestate/registration/inline/dxtspz";
	}

	@RequestMapping(value = "/save_dxtspz", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage save_dxpz(HttpServletRequest req, String id, String activityname, String tbstarttime, String bz)
	{
		JsonMessage msg = new JsonMessage();

		try
		{
			dxtspzservice.saveOrUpDxpz(id, activityname, tbstarttime, bz);
			msg.setMsg("已保存。");
			msg.setState(true);
		}
		catch (Exception ex)
		{
			msg.setMsg(ex.getMessage());
			msg.setState(false);
			ex.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value = "/del_dxtspz", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage del_dxtspz(HttpServletRequest req, String id)
	{
		JsonMessage msg = new JsonMessage();

		try
		{
			dxtspzservice.del_Dxpz(id);
			msg.setMsg("已删除。");
			msg.setState(true);
		}
		catch (Exception e)
		{
			msg.setMsg(e.getMessage());
			msg.setState(false);
			e.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value = "/dxtz", method = RequestMethod.GET)
	public String dxtz(HttpServletRequest req, Model model)
	{
		DicInfo dic_djlx = inlineService.getDicInfo("DJLX");
		DicInfo dic_qllx = inlineService.getDicInfo("QLLX");
		model.addAttribute("djlx", dic_djlx);
		model.addAttribute("qllx", dic_qllx);
		model.addAttribute("templet", dxtspzservice.getTemplet());
		this.dxtspzservice.initTableAndView();
		return "/realestate/registration/inline/dxtz";
	}

	@RequestMapping(value = "/dxtz_send", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage dxtz_send(String xmbh)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			if (xmbh == null || xmbh.isEmpty())
				throw new Exception("请求参数无效。");
			dxtspzservice.send_dx(xmbh);
			msg.setMsg("短信已发送。");
			msg.setState(true);
		}
		catch (Exception e)
		{
			msg.setMsg(e.getMessage());
			msg.setState(false);
			e.printStackTrace();
		}
		return msg;
	}

	@RequestMapping(value = "/dxtz_send/batch", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage batch_dxtz_send(String xmbhs)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			if (xmbhs == null || xmbhs.isEmpty())
				throw new Exception("请求参数无效。");
			String[] xmbhArr=StringUtils.split(xmbhs,",");
			int success_total= dxtspzservice.send_dx(xmbhArr);
			msg.setMsg("发送短信 "+xmbhArr.length+"，其中成功 "+success_total+"。");
			msg.setState(true);
		}
		catch (Exception e)
		{
			msg.setMsg(e.getMessage());
			msg.setState(false);
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping(value = "/editTemplet", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage editTemplet(String templet)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			dxtspzservice.editTemplet(templet);
			msg.setMsg("编辑成功。");
			msg.setState(true);
		}
		catch (Exception e)
		{
			msg.setMsg(e.getMessage());
			msg.setState(false);
			e.printStackTrace();
		}
		return msg;
	}

}
