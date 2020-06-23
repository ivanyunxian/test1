package com.supermap.realestate.registration.util;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.AsyncRestTemplate;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestAsync {
	protected static Logger logger = Logger.getLogger(HttpRequestAsync.class);
    public StringBuffer postHttpJsonDataAsyn(String URL, @SuppressWarnings("rawtypes") Map params, final JSONObject responseJsonMap) throws IOException {
        // 发送异步post , 非阻塞, 发送完无需等待结果返回
        AsyncRestTemplate client = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<String>> listenableFuture = client.postForEntity(URL, new HttpEntity<Object>(params), String.class);
        listenableFuture.addCallback(new SuccessCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                System.out.println("("+result.getStatusCode()+ ":"+result.getStatusCode().getReasonPhrase()+ "):"+result.getBody());
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println(ex);
            }
        });
        return new StringBuffer("finish postHttpJsonDataAsyn post");
    } 
}
