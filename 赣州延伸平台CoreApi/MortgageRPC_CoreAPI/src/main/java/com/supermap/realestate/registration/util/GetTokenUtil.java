package com.supermap.realestate.registration.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class GetTokenUtil {

	private static String USER;
	private static String PWD;
	private static String accessTokenUrl;

	static {
		USER = GetProperties.getValueByFileName("sms_config.properties", "username");
		PWD = GetProperties.getValueByFileName("sms_config.properties", "password");
		accessTokenUrl = GetProperties.getValueByFileName("sms_config.properties", "token_url");
	}

	public static String getAccessToken() throws Exception{
	     URL url = new URL(accessTokenUrl);
		 HttpURLConnection connection;
		 try {
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				DataOutputStream out = new DataOutputStream(
						connection.getOutputStream());
				String content = "grant_type=password";
				content += "&UserName=" + URLEncoder.encode(USER,"UTF-8");
				content += "&Password=" + URLEncoder.encode(PWD,"UTF-8");
//				out.writeBytes(content);
				out.write(content.getBytes());
				out.flush();
				out.close();
				connection.connect();
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
				String accessToken = "";
				if (line != null) {
					json = json.append(line);
					jsonObj = JSONObject.fromObject(json.toString());
					Map map=jsonObj;
					if(jsonObj.get("access_token")!=null){
						accessToken= null==jsonObj.getString("access_token")?"":jsonObj.getString("access_token");
					}
					else{
						accessToken = null == accessToken?"":accessToken;
					}
					return accessToken;
				}
				reader.close();
				connection.disconnect();
				return accessToken;
		}catch(ConnectException c){
			throw c;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
