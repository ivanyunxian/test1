package com.supermap.wisdombusiness.buildingtable.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/buildingtable")
public class SmBuildingTableController {
	/**
	 * 楼盘表展示主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String buildingtable(Model model) {
		return "/workflow/buildingtable/index";
	}
	/**
	 * 历史回溯
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(Model model) {
		return "/workflow/buildingtable/history";
	}
	/**
	 * 楼盘表展示主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/geopicture", method = RequestMethod.GET)
	public String geopicture(Model model) {
		return "/workflow/buildingtable/geopicture";
	}
}
