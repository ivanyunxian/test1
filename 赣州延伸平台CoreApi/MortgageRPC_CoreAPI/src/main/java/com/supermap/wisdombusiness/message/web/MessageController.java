package com.supermap.wisdombusiness.message.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowIndex(Model model) {

		return "/message/index";
	}

}
