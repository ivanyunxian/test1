package com.supermap.realestate.registration.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DXSENDUtil {

	private static String URL;

	static {
		URL = GetProperties.getValueByFileName("sms_config.properties", "url");
	}
	
	public static boolean  readContentFromPost(String content,String tokenstr) throws ConnectException, MalformedURLException {
		// 打开连接
		boolean issuccess = false;
		URL postUrl = new URL(URL);
		//URL postUrl = new URL("http://10.1.5.25:83/api/Message/InsertAsync");
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			connection.setRequestProperty("Authorization", "Bearer " + tokenstr);
			connection.connect();
			//DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			String jsonpar = content.toString();
			out.write(jsonpar);
			out.flush(); 
			out.close();
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			InputStream is = null;
			int status = connection.getResponseCode();
			if(status>= HttpStatus.SC_BAD_REQUEST){  //此处一定要根据返回的状态码state来初始化输入流。如果为错误
			    is = connection.getErrorStream();
			}else{
			    is = connection.getInputStream();//如果正确
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is,"UTF-8"));
			String line = null;
			line = reader.readLine();
			if (line != null) {
				json = json.append(line);
				String ds=new String(json.toString().getBytes("iso8859-1"),"UTF-8");
			    System.out.println("====================2222222222222222222222222===="+jsonObj.toString());
				jsonObj = JSONObject.fromObject(json.toString());
				System.out.println("===============================================");
    			System.out.println("调用短信接口返回的数据:"+jsonObj);
    			System.out.println("===============================================");
				Map map=jsonObj;
				/*String detailString ="";
				String Detailadd  ="";
				if(jsonObj.get("Detailadd")!=null){
					 Detailadd= null==jsonObj.getString("Detailadd")?"":jsonObj.getString("Detailadd");
				}
				else{
					 Detailadd = null == detailString?"":detailString;
				}
				jsonObj.put("Detailadd", URLDecoder.decode(URLDecoder.decode(Detailadd,"UTF-8"),"UTF-8"));*/
				issuccess = true;
			}
			reader.close();
			connection.disconnect();
			return true;
		}catch(ConnectException c){
			throw c;
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return issuccess;
	}

	/**
	 *
	 * @param content
	 * @param tokenstr
	 * @return
	 * @throws ConnectException
	 * @throws MalformedURLException
	 */
	public static Map smsSend(String content, String tokenstr) throws Exception {

		Map map = new HashMap();
		map.put("Data", "短信发送失败！");
		map.put("Succeed", false);

		// 打开连接
		URL postUrl = new URL(URL);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			connection.setRequestProperty("Authorization", "Bearer " + tokenstr);
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			String jsonpar = content.toString();
			out.write(jsonpar);
			out.flush();
			out.close();
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			InputStream is = null;
			int status = connection.getResponseCode();
			if(status>= HttpStatus.SC_BAD_REQUEST){  //此处一定要根据返回的状态码state来初始化输入流。如果为错误
				is = connection.getErrorStream();
			}else{
				is = connection.getInputStream();//如果正确
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			String line = null;
			line = reader.readLine();
			if (line != null) {
				json = json.append(line);
				jsonObj = JSONObject.fromObject(json.toString());
				if (jsonObj != null && jsonObj.containsKey("Succeed")) {
					String ResultType = StringHelper.formatObject(jsonObj.get("Succeed"));
					String Data = StringHelper.formatObject(jsonObj.get("Data"));
					String Succeed = StringHelper.formatObject(jsonObj.get("Succeed"));
					String Message = StringHelper.formatObject(jsonObj.get("Message"));
					map.put("ResultType", ResultType);
					map.put("Data", Data);
					map.put("Succeed", Succeed);
					map.put("Message", Message);
				}
			}
			reader.close();
			connection.disconnect();
			return map;
		} catch (Exception e) {
			map.put("Data", "短信发送异常，详情：" + e.getMessage());
			map.put("Succeed", false);
			e.printStackTrace();
		}
		return map;
	}
	
}
