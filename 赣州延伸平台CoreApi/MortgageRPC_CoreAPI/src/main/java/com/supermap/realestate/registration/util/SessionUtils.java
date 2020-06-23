package com.supermap.realestate.registration.util;


import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 交换平台开发接口获取登录认证工具类
 */
public class SessionUtils  {
    private String ip;
    private int port = 6061;
    private String unitIdentifier ;
    private String username;
    private String password;

    /**
     *
     * @param ip 交换站IP
     * @param unitIdentifier 单位标识符
     * @param username 用户名
     * @param password 密码
     */
    public SessionUtils(String ip,String unitIdentifier,String username,String password){
        this.ip = ip;
        this.unitIdentifier = unitIdentifier;
        this.username = username;
        this.password = password;
    }
    private String azsession;
    private long lastInvokeTime ;
    public synchronized String getSession(){
        if(azsession == null || new Date().getTime()-lastInvokeTime>1000*60*60*24){
            try {
                this.azsession= invoke();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("获取session出错："+e.getLocalizedMessage());
            }
        }
        return azsession;
    }

    private String invoke() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(String.format("http://%s:%s/developer-api/sessions?identifier=%s",ip,port,unitIdentifier));
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        Map<String,String> map = new HashMap();
        map.put("username",username);
        map.put("password",password);
        JSONObject jsonObject = JSONObject.fromObject(map);
        StringEntity entity = new StringEntity(jsonObject.toString(), charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        if(execute.getStatusLine().getStatusCode() == 201){
            String s = EntityUtils.toString(execute.getEntity(), charSet);
            JSONObject jsonObject1 = JSONObject.fromObject(s);
            String session = jsonObject1.getString("session");
            this.azsession = session;
            this.lastInvokeTime = System.currentTimeMillis();
            return session;
        }else {
            System.out.println("获取session出错："+ EntityUtils.toString(execute.getEntity(), charSet));
            throw new RuntimeException("获取session出错："+ EntityUtils.toString(execute.getEntity(), charSet)); 
        }
    }

}

