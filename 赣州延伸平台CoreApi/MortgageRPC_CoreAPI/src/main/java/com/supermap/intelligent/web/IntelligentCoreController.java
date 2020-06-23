package com.supermap.intelligent.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.supermap.intelligent.service.IntelligentCoreMQService;
import com.supermap.intelligent.service.IntelligentCoreService;
import com.supermap.intelligent.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @Author taochunda
 * @Description 抵押风险防控平台核心控制层
 * @Date 2019-07-28 15:52
 * @Param
 * @return
 **/
@Controller
@RequestMapping(value = "/intelligentcore")
public class IntelligentCoreController {
    @Autowired
    IntelligentCoreService intelligentCoreService;

    @Autowired
    IntelligentCoreMQService IntelligentCoreMQService;

    /**
     * @return com.supermap.wisdombusiness.synchroinline.model.JsonMessage
     * @Author taochunda
     * @Description 抵押登记申报
     * @Date 2019-07-28 15:53
     * @Param [req]
     **/
    @RequestMapping(value = "/declare", method = RequestMethod.POST)
    @ResponseBody
    public ResultData declare(HttpServletRequest req) throws UnsupportedEncodingException {
        ResultData result = new ResultData();
        result = intelligentCoreService.declare(req);
        if (result.isState()) {
            result.setMsg("业务申报成功！");
            result.setState(true);
            result.setErrorcode("0000");
        }
        return result;
    }
    
    /**
     * @return com.supermap.wisdombusiness.synchroinline.model.JsonMessage
     * @Author taochunda
     * @Description 企业信息申报
     * @Date 2019-07-28 15:53
     * @Param [req]
     **/
    @RequestMapping(value = "/enterprisedeclare", method = RequestMethod.POST)
    @ResponseBody
    public ResultData enterpriseDeclare(HttpServletRequest req) throws UnsupportedEncodingException {
        ResultData result = new ResultData();
        result = intelligentCoreService.enterpriseDeclare(req);
        if (result.isState()) {
            result.setMsg("业务申报成功！");
            result.setState(true);
            result.setErrorcode("0000");
        }
        return result;
    }

    @RequestMapping(value = "/pushZSToMRPC/{wlsh}/{ywly}",method = RequestMethod.GET)
    @ResponseBody
    public ResultData pushZSToMRPC(@PathVariable("ywly") String ywly,@PathVariable("wlsh") String wlsh,HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        ResultData result = new ResultData();
        result = IntelligentCoreMQService.pushZSToMRPC(ywly,wlsh);
        return result;
    }
}
