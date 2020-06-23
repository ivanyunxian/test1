package org.jeecg.modules.mortgagerpc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.mortgagerpc.service.ICoreAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MortgagerpcCoreAPI
 * @Description 不动产抵押登记风险防控平台接口，在此路径的均为提供给第三方调用的接口
 * @Author notebao
 * @Date 2019/9/2 14:45
 */
@Slf4j
@Api(tags="不动产抵押登记风险防控平台接口")
@RestController
@RequestMapping("/mortgagerpc")
public class MortgagerpcCoreAPI {

    @Autowired
    ICoreAPIService coreAPIService;

    /**获取token
     * @param requestJson 请求json
     * @param response
     * logType 3 标识为接口调用日志
     * requestcode 1000
     * @return
     */
    @AutoLog(value = "不动产抵押登记风险防控平台接口-获取会话授权信息接口" ,logType = CommonConstant.INTERFACE_TYPE_3)
    @ApiOperation(value="不动产抵押登记风险防控平台接口-获取会话授权信息接口", notes="不动产抵押登记风险防控平台接口-获取会话授权信息接口")
    @PostMapping(value = "/applicationToken")
    public String applicationToken(@RequestBody String requestJson, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
        response.setContentType("application/json;charset=utf-8");	//编码设置
        return coreAPIService.applicationToken(requestJson);
    }

    /**对外查询接口接口，
     * 不动产查询服务
     * requestcode 1001-1004
     * @param requestJson
     * @param response
     * @return
     */
    @AutoLog(value = "不动产抵押登记风险防控平台接口-对外查询接口接口" ,logType = CommonConstant.INTERFACE_TYPE_3)
    @ApiOperation(value="不动产抵押登记风险防控平台接口-对外查询接口接口", notes="不动产抵押登记风险防控平台接口-对外查询接口接口")
    @PostMapping(value = "/mrpcAPI_search")
    public String mrpcAPI_search(@RequestBody String requestJson,HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
        response.setContentType("application/json;charset=utf-8");	//编码设置
        return coreAPIService.finalResultModule(requestJson,request);
    }

    /**对外业务申报接口，
     * 不动产申报服务
     * requestcode 2001-2006
     * @param requestJson
     * @param response
     * @return
     */
    @AutoLog(value = "不动产抵押登记风险防控平台接口-对外业务申报接口" ,logType = CommonConstant.INTERFACE_TYPE_3)
    @ApiOperation(value="不动产抵押登记风险防控平台接口-对外业务申报接口", notes="不动产抵押登记风险防控平台接口-对外业务申报接口")
    @PostMapping(value = "/mrpcAPI_declare")
    public String mrpcAPI_declare(@RequestBody String requestJson,HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
        response.setContentType("application/json;charset=utf-8");	//编码设置
        return coreAPIService.declareProject(requestJson,request);
    }

   /**
    * @Author taochunda
    * @Description 对内业务接口，供登记系统登簿后回调，如：推送抵押权登记簿结果信息等
    * @Date 2019-09-11 10:35
    * @Param [requestJson, request, response]
    * @return java.lang.String
    **/
    @AutoLog(value = "不动产抵押登记风险防控平台接口-对内业务接口" ,logType = CommonConstant.INTERFACE_TYPE_3)
    @ApiOperation(value="不动产抵押登记风险防控平台接口-对内业务接口", notes="不动产抵押登记风险防控平台接口-对内业务接口")
    @GetMapping(value = "/mrpcAPI_DBCallBack")
    public String mrpcAPI_DBCallBack(HttpServletRequest request){
        return coreAPIService.DBCallBack(request);
    }
}
