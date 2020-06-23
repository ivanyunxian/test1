package com.supermap.realestate.registration.maintain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * web工具类
 * @ClassName: WebHelper
 * @author liushufeng
 * @date 2016年7月6日 上午10:57:34
 */
public class WebHelper {

	/**
	 * 简单的发送一个http GET请求，TODO 刘树峰：待优化，同时支持GET和POST
	 * @Title: sendHttpRequest
	 * @author:liushufeng
	 * @date：2016年7月6日 上午11:04:22
	 * @param urlStr
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public static String sendHttpRequest(String urlStr, RequestMethod method) throws IOException {
		HttpURLConnection conn = null;
		URL url = new URL(urlStr);
		conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		String sessionid = getSessionID("realestate");
		conn.setRequestMethod(method.name());
		conn.setRequestProperty("Accept-Charset", "utf-8");
		conn.addRequestProperty("Cookie", "JSESSIONID=" + sessionid);
		conn.setRequestProperty("contentType", "utf-8");
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}
		// 将返回的值存入到String中
		BufferedReader brd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = brd.readLine()) != null) {
			sb.append(line);
		}
		brd.close();
		conn.disconnect();
		return sb.toString();
	}

	/**
	 * 获取机器名或者IP
	 * @Title: getServerNameAndPort
	 * @author:liushufeng
	 * @date：2016年7月7日 下午2:21:33
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getServerName() throws UnknownHostException {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		String servername = (String) servletContext.getAttribute("webserver_servname");
		return servername;
	}

	/**
	 * 获取schema(http或者https)
	 * @Title: getServerIpAndPort
	 * @author:liushufeng
	 * @date：2016年7月7日 下午2:21:19
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getServerSchema() throws UnknownHostException {
		
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		String servschema = (String) servletContext.getAttribute("webserver_schema");
		return servschema;
	}

	/**
	 * 获取端口号
	 * @Title: getServerPort
	 * @author:liushufeng
	 * @date：2016年7月7日 下午2:40:40
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getServerPort() throws UnknownHostException {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		//String servsport = (String) servletContext.getAttribute("webserver_port");
		String servsport =StringHelper.formatObject(servletContext.getAttribute("webserver_port"));
		return servsport;
	}

	/**
	 * 根据应用名称获取sessionid（前提是配置了HttpSession的侦听，
	 * 在session创建的时候将sessionid写入到servletContext当中）
	 * @Title: getSessionID
	 * @author:liushufeng
	 * @date：2016年7月7日 下午2:20:26
	 * @param appname
	 * @return
	 */
	public static String getSessionID(String appname) {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		String userid="";
		String sessionid = (String) servletContext.getContext("/" + appname).getAttribute(appname + "_"+userid);
		return sessionid;
	}
}
