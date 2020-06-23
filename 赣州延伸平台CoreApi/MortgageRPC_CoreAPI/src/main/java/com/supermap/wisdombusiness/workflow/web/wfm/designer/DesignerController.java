package com.supermap.wisdombusiness.workflow.web.wfm.designer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/designer")
public class DesignerController {
	
	/*
	 *显示默认页面 
	 * */
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String ShowIndex(Model model) {
		return "/designer/index";
	}

}
