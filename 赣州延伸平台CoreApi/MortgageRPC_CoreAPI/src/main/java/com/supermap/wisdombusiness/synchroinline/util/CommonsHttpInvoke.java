package com.supermap.wisdombusiness.synchroinline.util;

import com.supermap.realestate.registration.util.StringHelper;
import net.sf.json.JSONArray;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

public class CommonsHttpInvoke
{
	/**
	 * commonHttpClient 调用post
	 * 
	 * @throws IOException
	 * 
	 */
	public String commonHttpDoPost(HttpServletRequest request, HttpServletResponse responst, String url, NameValuePair[] param)
	{
		HttpClient httpClient = new HttpClient();
		String body = "";
		try
		{
			PostMethod httppost = new PostMethod(url);
			httppost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			httppost.getParams().setContentCharset("utf-8");
			if(!StringHelper.isEmpty(param)){
				httppost.setRequestBody(param);
			}else{
				httppost.setRequestBody("");
			}
			
			httpClient.executeMethod(httppost);
			if (httppost.getStatusCode() == HttpStatus.SC_OK)
			{
				byte[] bytes = httppost.getResponseBody();
				body = new String(bytes, "utf-8");
			}
			httppost.releaseConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return body;
	}

	public String commonHttpDoPostNotice(HttpServletRequest request, HttpServletResponse response, String url, String ywlsh)
	{
		HttpClient httpClient = new HttpClient();
		String body = "";
		try
		{
			PostMethod httppost = new PostMethod(url);
			httppost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			httppost.getParams().setContentCharset("utf-8");
			NameValuePair[] param = { new NameValuePair("ywlsh", ywlsh.toString()) };
			httppost.setRequestBody(param);
			httpClient.executeMethod(httppost);
			byte[] bytes = httppost.getResponseBody();
			body = new String(bytes, "utf-8");
			httppost.releaseConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 执行get请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception
	{
		// 定义HttpClient
		HttpClient client = new HttpClient();
		// 实例化HTTP方法
		GetMethod get = new GetMethod(url);
		client.executeMethod(get);
		byte[] bytes = get.getResponseBody();
		String body = new String(bytes, "utf-8");
		get.releaseConnection();
		return body;
	}
	
	
	@SuppressWarnings({ "deprecation", "resource" })
	public String commonHttpDoGet(HttpServletRequest request, HttpServletResponse responst, String url, Map<String, String> param) throws HttpException, IOException
	{
		// 定义HttpClient
				DefaultHttpClient client = new DefaultHttpClient();
				// 实例化HTTP方法
				String lasturl = null;
				if(param!=null&&param.size()>0){
					if(param.size()>1){
						int i=0;
						for (Entry<String, String> ent : param.entrySet()) {
							i++;
							String name = ent.getKey();
							String value = ent.getValue();

							if(i==1){
								lasturl=url+"?"+name+"="+value;
							}else {
								lasturl=lasturl+"&"+name+"="+value;
							}

						}
					}else{
						for (Entry<String, String> ent : param.entrySet()) {
							String name = ent.getKey();
							String value = ent.getValue();
							lasturl=url+"?"+name+"="+value;
						}
					}

					
				}else{
					lasturl=url;
				}
				 
				HttpGet httpgets = new HttpGet(lasturl);    
				httpgets.addHeader( "User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
				HttpResponse response = client.execute(httpgets);    
		        HttpEntity entity = response.getEntity();    
		        if (entity != null) {    
		        	String respContent = EntityUtils.toString(entity , "GBK").trim();  
		        	httpgets.abort();  
		            client.getConnectionManager().shutdown();  
		  
		            return respContent;  
		        }  
		        return null;  
				
			 
	}

	
	 public static String convertStreamToString(InputStream is) {      
	        StringBuilder sb1 = new StringBuilder();      
	        byte[] bytes = new byte[4096];    
	        int size = 0;    
	          
	        try {      
	            while ((size = is.read(bytes)) > 0) {    
	                String str = new String(bytes, 0, size, "UTF-8");    
	                sb1.append(str);    
	            }    
	        } catch (IOException e) {      
	            e.printStackTrace();      
	        } finally {      
	            try {      
	                is.close();      
	            } catch (IOException e) {      
	               e.printStackTrace();      
	            }      
	        }      
	        return sb1.toString();      
	    }  
	 
	 /**
		 * commonHttpClient 调用post
		 * 
		 * @throws IOException
		 * 
		 */
		public String commonHttpDoPostIO(HttpServletRequest request, HttpServletResponse responst, String url, NameValuePair[] param)
		{
			HttpClient httpClient = new HttpClient();
			String body = "";
			try
			{
				 
				PostMethod httppost = new PostMethod(url);
				httppost.setRequestHeader("Content-Type", "text/plain;charset=utf-8");
				httppost.getParams().setContentCharset("utf-8");
				if(!StringHelper.isEmpty(param)){
					httppost.setRequestEntity(new ByteArrayRequestEntity(JSONArray.fromObject(param).toString().getBytes("UTF-8"), "text/plain; charset=utf-8"));
				 
					 
				}else{
					httppost.setRequestBody("");
				}
				
				httpClient.executeMethod(httppost);
				if (httppost.getStatusCode() == HttpStatus.SC_OK)
				{
					byte[] bytes = httppost.getResponseBody();
					body = new String(bytes, "utf-8");
				}
				httppost.releaseConnection();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return body;
		}

	
}
