package com.supermap.wisdombusiness.synchroinline.web;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.supermap.wisdombusiness.synchroinline.service.XmslshService;

@Controller
@RequestMapping(value = "/realestate/registration/inline")
public class XmslshController {
    @Autowired
    private XmslshService xmslsh;

    @RequestMapping(value = "/xmslsh")
    public String qjdc_sh(Model model) {
	return "/realestate/registration/inline/xmslsh";
    }

    @RequestMapping("/xmsl_sh")
    @ResponseBody
    public JSONArray qjdcsh_Data(HttpServletRequest req) {
	JSONArray jsonArr = new JSONArray();
	try {
	    jsonArr = xmslsh.xmslshData(req);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return jsonArr;
    }

    @RequestMapping("/xmsl_name")
    @ResponseBody
    public JSONArray qjdcsh_shry(HttpServletRequest req) {
	JSONArray jsonArr = new JSONArray();
	try {
	    jsonArr = xmslsh.xmslshry(req);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return jsonArr;
    }

    @RequestMapping("/xmsl_up")
    @ResponseBody
    public String qjdcsh_ed(HttpServletRequest req) {
	String types = null;
	try {
	    types = xmslsh.xmslsh_up(req);
	} catch (Exception e) {
		types = "请重新登录你的账号！";
	    e.printStackTrace();
	}
	return types;
    }
    
    /**
     * 智能审批驳回外网
     * @param req
     * @return
     */
    @RequestMapping("/Intelligent/approval")
    @ResponseBody
    public String IntelligentApproval(HttpServletRequest req) {
	String types = null;
	try {
	    types = xmslsh.IntelligentApproval(req);
	} catch (Exception e) {
		types = "请重新登录你的账号！";
	    e.printStackTrace();
	}
	return types;
    }

}
