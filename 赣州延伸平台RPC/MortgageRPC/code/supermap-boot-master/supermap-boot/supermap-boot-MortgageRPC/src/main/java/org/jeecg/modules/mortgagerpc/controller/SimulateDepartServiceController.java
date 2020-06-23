package org.jeecg.modules.mortgagerpc.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.ConstValue;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.common.util.RSACoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @ClassName SimulateDepartServiceController
 * @Description 模拟金融机构提供的接收登记信息服务，提供抵押平台进行接口服务回写测试（能正常加解密即可）
 * @Author notebao
 * @Date 2019/9/11 11:16
 */
@Slf4j
@Api(tags="金融机构接口服务模拟测试")
@RestController
@RequestMapping("/simulatedepart")
public class SimulateDepartServiceController {

    /**获取token
     * logType 3 标识为接口调用日志
     * requestcode 3001
     * @return
     */
    @ApiOperation(value="不动产抵押登记风险防控平台接口-金融机构接口服务模拟测试-获取token", notes="不动产抵押登记风险防控平台接口-金融机构接口服务模拟测试-获取token")
    @PostMapping(value = "/applicationToken")
    public String applicationToken(HttpServletResponse response, @RequestBody String requestJson){
        response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
        response.setContentType("application/json;charset=utf-8");	//编码设置
        try {
            String jsonstr = RSACoder.decryptByPublicKeyBase64(requestJson);
            System.out.println(jsonstr);
            JSONObject json = new JSONObject();
            json.put("timeout","123456789");
            json.put("token","efddki83iedbald09d023344566");
            return resultsJson(ConstValueMrpc.MrpccodingEnum.SUCCESS.Value, "3001", json, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsJson(ConstValueMrpc.MrpccodingEnum.FAIL.Value, "3001", "", "");
    }



    /**更新业务状态
     * logType 3 标识为接口调用日志
     * requestcode 3002
     * @return
     */
    @ApiOperation(value="不动产抵押登记风险防控平台接口-金融机构接口服务模拟测试-更新业务状态", notes="不动产抵押登记风险防控平台接口-金融机构接口服务模拟测试-更新业务状态")
    @PostMapping(value = "/updateProjectstate")
    public String updateProjectstate(HttpServletRequest request, @RequestBody String requestJson, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
        response.setContentType("application/json;charset=utf-8");	//编码设置
        try {
            String token = request.getParameter("token");
            System.out.println(token);
            String jsonstr = RSACoder.decryptByPublicKeyBase64(requestJson);
            System.out.println(jsonstr);
            JSONObject json = new JSONObject();
            json.put("state","00");
            json.put("msg","更新业务状态成功");
            return resultsJson(ConstValueMrpc.MrpccodingEnum.SUCCESS.Value, "3002", json, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsJson(ConstValueMrpc.MrpccodingEnum.FAIL.Value, "3002", "", "");
    }

    /**接收登簿结果
     * logType 3 标识为接口调用日志
     * requestcode 3002
     * @return
     */
    @ApiOperation(value="不动产抵押登记风险防控平台接口-金融机构接口服务模拟测试-接收登簿结果", notes="不动产抵押登记风险防控平台接口-金融机构接口服务模拟测试-接收登簿结果")
    @PostMapping(value = "/receiveDbinfo")
    public String receiveDbinfo(HttpServletRequest request, @RequestBody String requestJson, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");	//跨域设置
        response.setContentType("application/json;charset=utf-8");	//编码设置
        try {
            String token = request.getParameter("token");
            System.out.println(token);
            String jsonstr = RSACoder.decryptByPublicKeyBase64(requestJson);
            System.out.println(jsonstr);
            JSONObject json = new JSONObject();
            json.put("state","00");
            json.put("msg","登簿结果接收成功");
            return resultsJson(ConstValueMrpc.MrpccodingEnum.SUCCESS.Value, "3003", json, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsJson(ConstValueMrpc.MrpccodingEnum.FAIL.Value, "3003", "", "");
    }



    /**
     * 构建加密的返回结果json
     * @param code
     *            相应编码
     * @param queryResultsDate
     *            查询结果
     * @param extraDate
     *            额外的值
     * @return
     */
    public static String resultsJson(String code, String requestcode, Object queryResultsDate, Object extraDate) {
        JSONObject resultsJson = new JSONObject(6);
        resultsJson.put("code", code);
        resultsJson.put("msg", ConstValueMrpc.MrpccodingEnum.initFrom(code).Name);
        resultsJson.put("requestcode", requestcode);
        resultsJson.put("requestseq", System.currentTimeMillis());
        resultsJson.put("data", queryResultsDate);
        resultsJson.put("extra", extraDate);

        try {
            return RSACoder.encryptByPublicKeyBase64(resultsJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultsJson.put("code", ConstValueMrpc.MrpccodingEnum.ERROR);
                resultsJson.put("msg", ConstValueMrpc.MrpccodingEnum.ERROR.Name);
                resultsJson.put("data", "");
                return RSACoder.encryptByPublicKeyBase64(resultsJson.toString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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
