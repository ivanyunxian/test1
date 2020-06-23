package com.supermap.realestate.registration.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @Author taochunda
 * @Description http请求工具类
 * @Date 2019-08-04 12:04
 **/
public class HttpClientUtil {

    /** 链接超时时间 **/
    private static final Integer CONNECT_TIME_OUT = 10 * 1000;

    /** 数据传输超时时间 **/
    private static final Integer SOCKET_TIME_OUT = 40 * 1000;

    private static Logger logger = Logger.getLogger(HttpClientUtil.class);


    public static HttpClient getHttpClient(){
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(CONNECT_TIME_OUT);
        // TODO 先不设置数据传输超时，根据实际情况可放开
//        configBuilder.setSocketTimeout(SOCKET_TIME_OUT);

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        HttpClient httpClient = clientBuilder.build();

        return httpClient;
    }

    /**
     * @Author taochunda
     * @Description 普通 post请求
     * @Date 2019-08-04 11:52
     * @param data       请求参数
     * @param requestUrl 请求地址
     * @return 请求返回结果
     **/
    public static String requestPost(final String data, final String requestUrl) {
        logger.info(String.format("发送 POST 请求--请求地址【%s】，请求参数【%s】", requestUrl, data));
        HttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(requestUrl);
        StringEntity stringEntity = new StringEntity(data, "UTF-8");
        httpPost.setEntity(stringEntity);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            logger.info(String.format("发送 POST 请求--请求返回结果【%s】", result));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("发送 POST 请求--请求失败！ 详情：%s", e));
        }
        return null;
    }

    /**
     * @Author taochunda
     * @Description 普通 get 请求
     * @Date 2019-08-04 12:04
     * @Param requestUrl 请求地址
     * @return java.lang.String
     **/
    public static String requestGet(final String requestUrl) {
        logger.info(String.format("发送 GET 请求--请求地址【%s】", requestUrl));
        HttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            logger.info(String.format("发送 GET 请求--请求返回结果【%s】", result));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("发送 GET 请求--请求失败！ 详情：%s", e));
        }
        return null;
    }

}
