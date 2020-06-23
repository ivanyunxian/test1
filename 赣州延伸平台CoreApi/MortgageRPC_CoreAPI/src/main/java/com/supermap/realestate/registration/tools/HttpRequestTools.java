package com.supermap.realestate.registration.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送HTTP请求
 * 
 * @author OuZhanrong
 *
 */
public class HttpRequestTools {
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */

    public static String sendGet(String url, String param) {
	String result = "";
	BufferedReader in = null;
	try {
	    String urlName = url + "?" + param;
	    URL realUrl = new URL(urlName);
	    // 打开和URL之间的连接
	    URLConnection conn = realUrl.openConnection();
	    // 设置通用的请求属性
	    conn.setRequestProperty("accept", "*/*");
	    conn.setRequestProperty("connection", "Keep-Alive");
	    conn.setRequestProperty("user-agent",
		    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//	    conn.setRequestProperty("Accept-Charset", "UTF-8");
	    // 建立实际的连接
	    conn.connect();
	    // 获取所有响应头字段
	    Map<String, List<String>> map = conn.getHeaderFields();
	    // 遍历所有的响应头字段
	    // for (String key : map.keySet()) {
	    // System.out.println(key + "--->" + map.get(key));
	    // }
	    // 定义BufferedReader输入流来读取URL的响应
	    in = new BufferedReader(new InputStreamReader(
		    conn.getInputStream()));
	    String line;
	    while ((line = in.readLine()) != null) {
		result += "\n" + line;
	    }
	    if (result != null) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(result);
		result = m.replaceAll("");
		result = result.substring(1, result.length()-1);
	    }
	} catch (Exception e) {
	    System.out.println("HttpRequestTools.java 发送GET请求出现异常！" + e);
	    e.printStackTrace();
	    return null;
	}
	// 使用finally块来关闭输入流
	finally {
	    try {
		if (in != null) {
		    in.close();
		}
	    } catch (IOException ex) {
		ex.printStackTrace();
		return null;
	    }
	}
	return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String param) {
	BufferedWriter out = null;
	BufferedReader in = null;
	String result = "";
	try {
	    URL realUrl = new URL(url);
	    // 打开和URL之间的连接
	    URLConnection conn = realUrl.openConnection();
	    // 设置通用的请求属性
	    conn.setRequestProperty("accept", "*/*");
	    conn.setRequestProperty("connection", "Keep-Alive");
	    conn.setRequestProperty("user-agent",
		    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
	    // 发送POST请求必须设置如下两行
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    // 获取URLConnection对象对应的输出流
	     out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8")); 
	    // 发送请求参数
	    out.write(param);
	    // flush输出流的缓冲
	    out.flush();
	    // 定义BufferedReader输入流来读取URL的响应
	    in = new BufferedReader(new InputStreamReader(
		    conn.getInputStream(), "utf-8"));
	    String line;
	    while ((line = in.readLine()) != null) {
		result += "\n" + line;
	    }
	    if (result != null) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(result);
		result = m.replaceAll("");
//		result = result.substring(1, result.length()-1);
	    }
	    
	} catch (Exception e) {
	    System.out.println("HttpRequestTools.java 发送POST请求出现异常！" + e);
	    e.printStackTrace();
	    return null;
	}
	// 使用finally块来关闭输出流、输入流
	finally {
	    try {
		if (out != null) {
		    out.close();
		}
		if (in != null) {
		    in.close();
		}
	    } catch (IOException ex) {
		ex.printStackTrace();
		return null;
	    }
	}
	return result;
    }
}