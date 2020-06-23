package com.supermap.wisdombusiness.workflow.service.common;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class Common {
	public Common() {

	}
	public static final String WORKFLOWDB = "BDC_WORKFLOW.";
	public static final String SMWBSUPPORT="SMWB_SUPPORT.";
	public static String CreatUUID() {

		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String temp = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp;
	}

	public static int GetRamdom(double randomvalue) {
		if (randomvalue > 1) {
			randomvalue = randomvalue / 100;
		}

		double random = Math.random();
		if (random > randomvalue) {
			return 0;
		}
		return 1;
	}

	public static String PadLeft(String input, int size, char symbol) {
		while (input.length() < size) {
			input = symbol + input;
		}
		return input;
	}
	public static String getIp(HttpServletRequest request) throws Exception {
      /*  String ip = request.getHeader("X-Forwarded-For");
       if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
          }
       }
       ip = request.getHeader("X-Real-IP");
       if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
           return ip;
       }
       ip = request.getRemoteAddr();
       if(StringUtils.isNotEmpty(ip) &&ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
       	ip = "127.0.0.1";
       	return ip;
       }
       return ip;
   }*/
		 if (request == null) {  
		        throw (new Exception("getIpAddr method HttpServletRequest Object is null"));  
		    }  
		    String ipString = request.getHeader("x-forwarded-for"); 
		    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {  
		        ipString = request.getHeader("Proxy-Client-IP"); 
		    }  
		    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
		        ipString = request.getHeader("WL-Proxy-Client-IP");  
		    }  
		    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {  
		        ipString = request.getRemoteAddr(); 
		        if(StringUtils.isNotEmpty(ipString) &&ipString.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
		        	ipString = "127.0.0.1";
		           }
		    }  
		  
		    // 多个路由时，取第一个非unknown的ip  
		    final String[] arr = ipString.split(",");  
		    for (final String str : arr) {  
		        if (!"unknown".equalsIgnoreCase(str)) {  
		            ipString = str;  
		            System.out.println("多路由ip：：：："+ipString);
		            break;  
		        }  
		    }  
		  
		    return ipString;  
	}
}