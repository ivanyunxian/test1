/**   
 * TODO:@liushufeng:请描述这个文件
 * @Title: MyThread.java 
 * @Package com.supermap.realestate.registration.handlerImpl 
 * @author liushufeng 
 * @date 2016年4月14日 上午10:13:07 
 * @version V1.0   
 */

package com.supermap.realestate.registration.handlerImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

/**
 * TODO:@liushufeng:请描述这个类或接口"MyThread"
 * @ClassName: MyThread
 * @author liushufeng
 * @date 2016年4月14日 上午10:13:07
 */
public class MyThread implements Runnable {

	private String _filePath;
	private String _urlStr;
	@SuppressWarnings("unused")
	private Map<String, String> _urlParam;

	/**
	 * TODO:@liushufeng:请描述这个方法的作用
	 * @Title: run
	 * @author:liushufeng
	 * @date：2016年4月14日 上午10:13:07
	 */
	@SuppressWarnings("resource")
	@Override
	public void run() {
		
		
		HttpURLConnection connet = null;
		try
		{
			StringBuilder sb=new StringBuilder();
			String bwid = URLEncoder.encode(_urlParam.get("BWID"),"utf-8");
			String xzqdm =URLEncoder.encode(_urlParam.get("XZQHDM"),"utf-8");
			String xzqmc = URLEncoder.encode(_urlParam.get("XZQHMC"),"utf-8");
			String djjg = URLEncoder.encode(_urlParam.get("DJJG"),"utf-8");
			String ywbh = URLEncoder.encode(_urlParam.get("YWBH"),"utf-8");
			String qllx = URLEncoder.encode(_urlParam.get("QLLX"),"utf-8");
			String djlx = URLEncoder.encode(_urlParam.get("DJLX"),"utf-8");
			String cjr = URLEncoder.encode(_urlParam.get("CJR"),"utf-8");
			String ywh = URLEncoder.encode(_urlParam.get("BWID"),"utf-8");
			String bdcdyh = URLEncoder.encode(_urlParam.get("BDCDYH"),"utf-8");
			String bdczl = URLEncoder.encode(_urlParam.get("BDCZL"),"utf-8");
			
			FileInputStream instream = new FileInputStream(_filePath);
			int len = 0;
			byte[] bts=new  byte[0];
			byte[] buf = new byte[1024];
			while ((len = instream.read(buf)) != -1) {
				bts=byteMerger(bts,buf,len);
			}
			String xml =new String(bts,"utf-8");
			xml=URLEncoder.encode(URLEncoder.encode(xml,"utf-8"),"utf-8");
			String content =MessageFormat.format("XMLCONTENT={0}",xml);
			
			
			URL url = new URL(_urlStr);
			connet = (HttpURLConnection) url.openConnection();
			connet.setDoInput(true);
			connet.setDoOutput(true);
			connet.setRequestMethod("POST");
			//connet.setConnectTimeout(20 * 1000);// 设置连接超时时间为5秒
			//connet.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
			connet.setRequestProperty("Accept-Charset", "UTF-8");
			connet.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			connet.addRequestProperty("BWID",bwid);
			connet.addRequestProperty("XZQHDM",xzqdm);
			connet.addRequestProperty("XZQHMC",xzqmc);
			connet.addRequestProperty("DJJG",djjg);
			connet.addRequestProperty("YWBH",ywbh);
			connet.addRequestProperty("QLLX",qllx);
			connet.addRequestProperty("DJLX",djlx);
			connet.addRequestProperty("CJR",cjr);
			connet.addRequestProperty("YWH",ywh);
			connet.addRequestProperty("BDCDYH",bdcdyh);
			connet.addRequestProperty("BDCZL",bdczl);
	        OutputStream os = connet.getOutputStream();
	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
	        pw.write(content);
	        pw.close();
			BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
			String line;
			while ((line = brd.readLine()) != null)
			{
				sb.append(line);
			}
			brd.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 public  static byte[] byteMerger(byte[] byte_1, byte[] byte_2,int length){  
	        byte[] byte_3 = new byte[byte_1.length+length];  
	        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
	        System.arraycopy(byte_2, 0, byte_3, byte_1.length, length);  
	        return byte_3;  
	    }  

	public MyThread(String filepath, String urlStr, Map<String, String> urlParam) {
		this._filePath = filepath;
		this._urlStr = urlStr;
		this._urlParam = urlParam;
	}

}
