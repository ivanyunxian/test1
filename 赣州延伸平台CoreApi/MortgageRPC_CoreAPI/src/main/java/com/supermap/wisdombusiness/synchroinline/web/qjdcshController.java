package com.supermap.wisdombusiness.synchroinline.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.supermap.wisdombusiness.synchroinline.service.qjdcshService;

@Controller
@RequestMapping(value = "/realestate/registration/inline")
public class qjdcshController {
    @Autowired
    private qjdcshService qjdcsh;

    @RequestMapping(value = "/qjdcsh")
    public String qjdc_sh(Model model) {
	return "/realestate/registration/inline/qjdcsh";
    }

    @RequestMapping("/qjdc_sh")
    @ResponseBody
    public JSONArray qjdcsh_Data(HttpServletRequest req) {
	JSONArray jsonArr = new JSONArray();
	try {
	    jsonArr = qjdcsh.qjdcshData(req);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return jsonArr;
    }

    @RequestMapping("/qjdc_name")
    @ResponseBody
    public JSONArray qjdcsh_shry(HttpServletRequest req) {
	JSONArray jsonArr = new JSONArray();
	try {
	    jsonArr = qjdcsh.qjdcshry(req);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return jsonArr;
    }

    @RequestMapping("/qjdc_up")
    @ResponseBody
    public String qjdcsh_ed(HttpServletRequest req) {
	String types = null;
	try {
	    types = qjdcsh.qjdcsh_up(req);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return types;
    }
}
