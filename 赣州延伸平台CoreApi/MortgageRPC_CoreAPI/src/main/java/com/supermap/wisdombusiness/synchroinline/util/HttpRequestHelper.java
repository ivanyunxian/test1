package com.supermap.wisdombusiness.synchroinline.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pukx
 */
public class HttpRequestHelper
{

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            请求地址。
	 * @return 返回响应的消息
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws Exception
	 *             url为空时抛出异常
	 */
	public static String doGet(String url) throws MalformedURLException, IOException, Exception
	{
		if (url == null || url.isEmpty())
		{
			throw new Exception("请求地址不能为空。");
		}
		System.out.println("发送get请求：" + url);
		URL realUrl = new URL(url);
		URLConnection conn = realUrl.openConnection();
		conn.connect();
		InputStream inputStream = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null)
		{
			sb.append(line);
		}
		inputStream.close();
		String response = sb.length() == 0 ? null : sb.toString();
		System.out.println("响应消息：" + response);
		return response;
	}

	/**
	 * 以form data方式发送post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            键值对形式post参数
	 * @return 响应信息
	 * @throws Exception
	 */
	public static String doPost(String url, HashMap<String, String> params) throws Exception
	{
		if (url == null || url.isEmpty())
		{
			throw new Exception("请求地址不能为空。");
		}
		System.out.println("发送post请求：" + url);
		URL realurl = new URL(url);
		URLConnection con = realurl.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		if (params != null && !params.isEmpty())
		{
			List<String> values = new ArrayList<String>();
			for (Map.Entry entry : params.entrySet())
			{
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				if (key != null && !key.isEmpty())
				{
					values.add(key + "=" + URLEncoder.encode(value,"UTF-8"));
				}
			}
			String postParam = StringUtils.join(values.toArray(), '&');
			System.out.println("post参数：" + postParam);
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(postParam);
			osw.flush();
			osw.close();
		}
		InputStream inputStream = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null)
		{
			sb.append(line);
		}
		inputStream.close();
		String response = sb.length() == 0 ? null : sb.toString();
		System.out.println("响应消息：" + response);
		return response;
	}

	/**
	 * 以payload形式发送post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param payload
	 *            请求参数
	 * @return 响应信息
	 * @throws Exception
	 */
	public static String doPost(String url, String payload) throws Exception
	{
		if (url == null || url.isEmpty())
		{
			throw new Exception("请求地址不能为空。");
		}
		System.out.println("发送post请求：" + url);
		System.out.println("post参数：" + payload);
		URL realurl = new URL(url);
		URLConnection con = realurl.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		con.connect();
		OutputStream os = con.getOutputStream();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
		pw.write(payload);
		pw.close();

		InputStream inputStream = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null)
		{
			sb.append(line);
		}
		inputStream.close();
		String response = sb.length() == 0 ? null : sb.toString();
		System.out.println("响应消息：" + response);
		return response;
	}

	/**
	 *
	 * @return
	 * @throws ConnectException
	 * @throws MalformedURLException
	 */
	public static String smsSend(String url, HashMap<String, String> params) throws Exception {

		// 打开连接
		URL postUrl = new URL(url);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			connection.connect();
			if (params != null && !params.isEmpty())
			{
				List<String> values = new ArrayList<String>();
				for (Map.Entry entry : params.entrySet())
				{
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();
					if (key != null && !key.isEmpty())
					{
						values.add(key + "=" + URLEncoder.encode(value,"UTF-8"));
					}
				}
				String postParam = StringUtils.join(values.toArray(), '&');
				System.out.println("post参数：" + postParam);
				OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				osw.write(postParam);
				osw.flush();
				osw.close();
			}
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			InputStream is = null;
			int status = connection.getResponseCode();
			if(status>= HttpStatus.SC_BAD_REQUEST){  //此处一定要根据返回的状态码state来初始化输入流。如果为错误
				is = connection.getErrorStream();
			}else{
				is = connection.getInputStream();//如果正确
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
