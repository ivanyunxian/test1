package com.supermap.wisdombusiness.workflow.web.wfd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.workflow.model.Wfd_Holiday;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.util.Message;

@Controller
@RequestMapping("/holiday")
public class HolidayController {

	@Autowired
	private SmHoliday _SmHoliday;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowIndex(Model model) {
		return "/workflow/holiday/index";
	}

	@RequestMapping(value = "/batthmain", method = RequestMethod.GET)
	public String Batthmain(Model model) {
		return "/workflow/holiday/batthmain";
	}

	@RequestMapping(value = "/holiday/{id}", method = RequestMethod.GET)
	public String GetHistory(Model model, @PathVariable String id) {
		if (id == null || id.equals("0")) {
			model.addAttribute("holiday", new Wfd_Holiday());
		} else {
			model.addAttribute("holiday", _SmHoliday.GetHolidayById(id));
		}
		return "/workflow/holiday/edit";
	}

	
	@RequestMapping(value = "/holidaydata", method = RequestMethod.POST)
	public @ResponseBody Message Wfd_ProMaterList(HttpServletRequest request,
			HttpServletResponse response) {
		String pageString = request.getParameter("page");
		String rowString = request.getParameter("rows");
		String nameString = request.getParameter("name");
		String startString = request.getParameter("startdate");
		String endString = request.getParameter("enddate");
		if(!pageString.equals("")&&!rowString.equals("")){
			int pageIndex = Integer.parseInt(pageString);
			int pageSize = Integer.parseInt(rowString);
			return _SmHoliday.GetHolidayList(pageIndex, pageSize, nameString,
					startString, endString);
		}
		else{
			return new Message();
		}
		
	}

	@RequestMapping(value = "/holidayinfo", method = RequestMethod.GET)
	public @ResponseBody Message GetHistory(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		return _SmHoliday.GetHolidayInfo(id);
	}

	@RequestMapping(value = "/delholiday", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DelHistory(HttpServletRequest request,
			HttpServletResponse response) {
		String json = request.getParameter("json");
		JSONArray array = JSONArray.fromObject(json);
		return _SmHoliday.DeleteHolidayByIds(array);
	}

	@RequestMapping(value = "/batth", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo InsertHoliday(HttpServletRequest request,
			HttpServletResponse response) {
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		return _SmHoliday.InsertHoliday(startdate, enddate, name,type);
	}
	/*
	 * @RequestMapping(value = "/holiday/{id}", method = RequestMethod.POST)
	 * public String SaveOrUpdate(Model model,
	 * 
	 * @ModelAttribute("holiday") Wfd_Holiday Holiday,
	 * 
	 * @PathVariable String id, HttpServletRequest request, HttpServletResponse
	 * response) { _SmHoliday.SaveOrUpdate(Holiday); return
	 * "redirect:/app/holiday/holiday/" + Holiday.getHoliday_Id(); }
	 */
	@RequestMapping(value = "/holiday/{id}", method = RequestMethod.POST)
	 @ResponseBody
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("holiday") Wfd_Holiday holiday,
			HttpServletRequest request, HttpServletResponse response) {
		_SmHoliday.SaveOrUpdate(holiday);
		return "redirect:/app/holiday/holiday/" + holiday.getHoliday_Id();
	}

}
