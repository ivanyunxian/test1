package com.supermap.realestate.registration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

/**
 * 
 * @Description:解决编码
 * @author 刘树峰
 * @date 2015年6月12日 上午11:56:12
 * @Copyright SuperMap
 */
public class RequestHelper {

	/**
	 * 获取经过UTF-8解码的请求参数
	 * @Title: GetParam
	 * @author:liushufeng
	 * @date：2015年7月12日 下午10:14:21
	 * @param request
	 *            HttpServletRequest对象
	 * @param paramName
	 *            参数名
	 * @return 经过解码后的参数值
	 * @throws UnsupportedEncodingException
	 */
	public static String getParam(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
		String value = request.getParameter(paramName);
//		if (!StringUtils.isEmpty(value)) {
//			value = new String(value.getBytes("iso8859-1"), "utf-8");
//		}
		
		if (!StringUtils.isEmpty(value)) {
			if(value.equals(new String(value.getBytes("iso8859-1"), "iso8859-1")))
	        {
				value=new String(request.getParameter(paramName).getBytes("iso8859-1"),"utf-8");
	        }
		}
		return value;
	}

	/**
	 * 获取请求中的page参数值
	 * @Title: getPage 
	 * @author:liushufeng
	 * @date：2015年7月15日 下午3:09:06
	 * @param request
	 * @return
	 */
	public static Integer getPage(HttpServletRequest request) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		return page;
	}

	/**
	 * 获取请求中的rows参数值
	 * @Title: getRows 
	 * @author:liushufeng
	 * @date：2015年7月15日 下午3:09:18
	 * @param request
	 * @return
	 */
	public static Integer getRows(HttpServletRequest request) {
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		return rows;
	}

	/**
	 * 将请求参数封装成Map<String, String>
	 * @param request
	 * @return
	 *
	 * @Author YuGuowei
	 */
	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String paramName = (String) e.nextElement();
			String paramValue = request.getParameter(paramName);
			//形成键值对应的map
			params.put(paramName, paramValue);
		}
		return params;
	}

	public static String getRequestBodyAsString(HttpServletRequest request) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder("");
		try
		{
			br = request.getReader();
			String str;
			while ((str = br.readLine()) != null)
			{
				sb.append(str);
			}
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != br)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @Author taochunda
	 * @Description 获取请求body数据
	 * @Date 2019-08-03 15:56
	 * @Param [request]
	 * @return com.alibaba.fastjson.JSONObject
	 **/
	public static JSONObject getHTTPBodyParams(HttpServletRequest request){
		InputStream is = null;
		String paramsjson = "";
		try {
			is = request.getInputStream();
			int size = request.getContentLength();
			byte[] reqBodyBytes = readBytes(is, size);
			paramsjson = new String(reqBodyBytes,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject data = JSONObject.parseObject(paramsjson);
		return data;
	}

	public static final byte[] readBytes(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen
							- readLen);
					if (readLengthThisTime == -1) {
						break;
					}
					readLen += readLengthThisTime;
				}
				return message;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new byte[] {};
	}
}
