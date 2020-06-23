package com.supermap.realestate.registration.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.LogExchangeInfoService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

@Controller
@RequestMapping("/logexchange")
public class LogExchangeInfoController {

    @Autowired
    private CommonDao dao;
    @Autowired
    private LogExchangeInfoService logExchangeService;
    
    @RequestMapping(value="/exchangeinfouplaod" ,method =RequestMethod.GET)
    public @ResponseBody String exchangeInfo(HttpServletRequest request ,HttpServletResponse response ){
//    	String registrationLogXml = request.getParameter("registrationLogXml");
    	String path = request.getRealPath("/");
    	System.err.print("-------------------"+path+"-----------------------------------------------");
    	String uploadresult =  logExchangeService.exchangeInfo(path) ;
    	return uploadresult;
    }
    /**
     * 指定日期手动上报日志信息  
     * 获取日期参数放入会话session中  by zhw
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/appointinfouplaod" ,method =RequestMethod.GET)
    @ResponseBody
    public  String appointInfo(HttpServletRequest request ,HttpServletResponse response ){
    	String path = request.getRealPath("/");
    	System.err.print("-------------------"+path+"-----------------------------------------------");
    	String date = request.getParameter("sbdate");
    	request.getSession().setAttribute("sbdate", date);
    	String uploadresult =  logExchangeService.exchangeInfo(path) ;
    	return uploadresult;
    }
}
