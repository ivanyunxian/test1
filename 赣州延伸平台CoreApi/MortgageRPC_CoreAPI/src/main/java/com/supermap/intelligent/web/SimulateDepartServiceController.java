package com.supermap.intelligent.web;

import com.alibaba.fastjson.JSONObject;
import com.supermap.intelligent.util.ConstValue;
import com.supermap.intelligent.util.RSACoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @ClassName SimulateDepartServiceController
 * @Description 模拟金融机构提供的接收登记信息服务，提供抵押平台进行接口服务回写测试（能正常加解密即可）
 * @Author notebao
 * @Date 2019/9/11 11:16
 */
@Controller
@RequestMapping("/simulatedepart")
public class SimulateDepartServiceController {

    /**获取token
     * @param response
     * logType 3 标识为接口调用日志
     * requestcode 3001
     * @return
     */
    @RequestMapping(value = "/applicationToken", method = RequestMethod.POST)
    @ResponseBody
    public String applicationToken(HttpServletRequest request,HttpServletResponse response){
        try {
            String requestJson = getHttpServletRequestDate(request);
            String jsonstr = RSACoder.decryptByPublicKeyBase64(requestJson);
            System.out.println(jsonstr);
            JSONObject json = new JSONObject();
            json.put("timeout","123456789");
            json.put("token","efddki83iedbald09d023344566");
            return resultsJson(ConstValue.MrpccodingEnum.SUCCESS.Value, "3001", json, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsJson(ConstValue.MrpccodingEnum.FAIL.Value, "3001", "", "");
    }



    /**更新业务状态
     * @param response
     * logType 3 标识为接口调用日志
     * requestcode 3002
     * @return
     */
    @RequestMapping(value = "/updateProjectstate", method = RequestMethod.POST)
    @ResponseBody
    public String updateProjectstate( HttpServletRequest request, HttpServletResponse response){
        try {
            String token = request.getParameter("token");
            System.out.println(token);
            String requestJson = getHttpServletRequestDate(request);
            String jsonstr = RSACoder.decryptByPublicKeyBase64(requestJson);
            System.out.println(jsonstr);
            JSONObject json = new JSONObject();
            json.put("state","00");
            json.put("msg","更新业务状态成功");
            return resultsJson(ConstValue.MrpccodingEnum.SUCCESS.Value, "3002", json, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsJson(ConstValue.MrpccodingEnum.FAIL.Value, "3002", "", "");
    }

    /**接收登簿结果
     * @param response
     * logType 3 标识为接口调用日志
     * requestcode 3002
     * @return
     */
    @RequestMapping(value = "/receiveDbinfo", method = RequestMethod.POST)
    @ResponseBody
    public String receiveDbinfo(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = request.getParameter("token");
            System.out.println(token);
            String requestJson = getHttpServletRequestDate(request);
            String jsonstr = RSACoder.decryptByPublicKeyBase64(requestJson);
            System.out.println(jsonstr);
            JSONObject json = new JSONObject();
            json.put("state","00");
            json.put("msg","登簿结果接收成功");
            return resultsJson(ConstValue.MrpccodingEnum.SUCCESS.Value, "3003", json, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsJson(ConstValue.MrpccodingEnum.FAIL.Value, "3003", "", "");
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
        resultsJson.put("msg", ConstValue.MrpccodingEnum.initFrom(code).Name);
        resultsJson.put("requestcode", requestcode);
        resultsJson.put("requestseq", System.currentTimeMillis());
        resultsJson.put("data", queryResultsDate);
        resultsJson.put("extra", extraDate);

        try {
            return RSACoder.encryptByPublicKeyBase64(resultsJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultsJson.put("code", ConstValue.MrpccodingEnum.ERROR);
                resultsJson.put("msg", ConstValue.MrpccodingEnum.ERROR.Name);
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
