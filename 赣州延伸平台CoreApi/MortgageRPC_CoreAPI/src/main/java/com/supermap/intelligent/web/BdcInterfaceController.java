package com.supermap.intelligent.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.intelligent.util.ConstValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.supermap.intelligent.service.BdcInterfaceService;



@Controller
@RequestMapping("/mrpc")
public class BdcInterfaceController {
	
	@Autowired
	private BdcInterfaceService bdcinterfaceservice;

	/**
	 * @Description 核心查询接口，“证书、证明、房源核验以及进度查询”，不使用别名版
	 * @Param [request, response]
	 * @return com.alibaba.fastjson.JSONObject
	 **/
	@RequestMapping(value = "/coreQuery",method = RequestMethod.POST)
	@ResponseBody
	public JSONObject coreQuery(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", ConstValue.MrpccodingEnum.OTHERERRORS.Value);
		jsonObject.put("msg", ConstValue.MrpccodingEnum.OTHERERRORS.Name);
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");    //跨域设置
			response.setContentType("application/json;charset=utf-8");    //编码设置
			jsonObject = bdcinterfaceservice.resultsDecryptJson(request);
		} catch (Exception e) {
			jsonObject.put("code", ConstValue.MrpccodingEnum.ERROR.Value);
			jsonObject.put("msg", "查询出现异常！ 详情：" + e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * @Description 核心查询接口，“证书、证明、房源核验以及进度查询”，配置自定义别名版（目前正式使用这套）
	 * @Param [request, response]
	 * @return com.alibaba.fastjson.JSONObject
	 **/
	@RequestMapping(value = "/coreQueryAlias",method = RequestMethod.POST)
	@ResponseBody
	public JSONObject coreQueryAlias(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", ConstValue.MrpccodingEnum.OTHERERRORS.Value);
		jsonObject.put("msg", ConstValue.MrpccodingEnum.OTHERERRORS.Name);
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");    //跨域设置
			response.setContentType("application/json;charset=utf-8");    //编码设置
			long start = System.currentTimeMillis();
			jsonObject = bdcinterfaceservice.coreQueryAlias(request);
			long end = System.currentTimeMillis();
			System.out.println("时长:"+(end-start));
		} catch (Exception e) {
			jsonObject.put("code", ConstValue.MrpccodingEnum.ERROR.Value);
			jsonObject.put("msg", "查询出现异常！ 详情：" + e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**获取token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/applicationToken",method = RequestMethod.POST)
	@ResponseBody
	 public String applicationToken(HttpServletRequest request,HttpServletResponse response){
		  response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
		  response.setContentType("application/json;charset=utf-8");	//编码设置
		  return bdcinterfaceservice.applicationToken(request);
	}
	/**对外接口，token认证
	 * 不动产查询服务
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/mrpcAPI",method = RequestMethod.POST)
	@ResponseBody
	 public String finalResultModule(HttpServletRequest request,HttpServletResponse response){
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
			response.setContentType("application/json;charset=utf-8");	//编码设置
			return bdcinterfaceservice.finalResultModule(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**读取数据流
	 * @param request
	 * @return
	 */
	public String getHttpServletRequestDate(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer(256);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
}
