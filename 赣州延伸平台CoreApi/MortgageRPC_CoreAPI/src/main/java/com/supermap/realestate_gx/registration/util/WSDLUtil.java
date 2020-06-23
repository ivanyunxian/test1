package com.supermap.realestate_gx.registration.util;

import java.net.URL;
import org.codehaus.xfire.client.Client;

/**
 * WSDL操作工具类
 * 2019年7月23日17:46:21
 * @author liangqin
 *
 */
public class WSDLUtil {
	
	/**
	 * 通过接口地址和参数执行
	 * 2019年7月23日17:47:00
	 * liangqin
	 * @param pathurl = 接口完整地址
	 * @param requestMsg = 请求参数
	 * @return
	 */
	public static String callService(String pathurl,String requestMsg) {
		Client client = null;
		try{
//			  URL url = new URL(pathurl);
//			  HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
//			  httpConnection.setRequestMethod("POST");
//			  httpConnection.setReadTimeout(65000);//设置http连接的读超时,单位是毫秒  
//			  httpConnection.connect();
//			  Client client = new Client(url);
			client = new Client(new URL(pathurl));  
			client.setTimeout(65000);
			Object[] params = new Object[] { requestMsg };
			Object[] ret = null;
			ret = client.invoke("getMarry", params);
			client.close();
			if (ret != null) {
				return ret[0].toString();
			}
			return "";
		}catch(Exception e){
			e.printStackTrace();
			if(client != null){
				client.close();
			}
			return ""; 
		}finally{
			if(client != null){
				client.close();
			}
		}
	}
}
