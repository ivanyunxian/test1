package com.supermap.realestate.registration.maintain;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;



public class realestateWebHelper {
	@Autowired
	private static CommonDao commonDao;
	static {
		if (commonDao == null) {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			commonDao=wac.getBean(CommonDao.class); 
		}
	}
	private static final Log logger = LogFactory.getLog(realestateWebHelper.class);
	
	/**
	 * 通过选择器类型活选择器
	 * @param selectortype 选择器类型
	 * @return
	 * @throws IOException
	 */
	public static String loadselector(String selectortype,HttpServletRequest request){		
		String url;
		try {
			String servername = request.getServerName();
			if ("1".equals(ConfigHelper.getNameByValue("UsedLocalIP"))) {
				servername = StringHelper.formatObject(InetAddress.getLocalHost().getHostAddress());
			}
			String basePath = request.getScheme() + "://" + servername + ":" + request.getLocalPort() + request.getContextPath() + "/";
			url = basePath+"app/query/queryindex2/" + selectortype+"/";
			String selector = WebHelper.sendHttpRequest(url, RequestMethod.GET);
			if(!StringHelper.isEmpty(selector)){
				return selector;
			}
		} catch (IOException e) {
			logger.trace("调用选择器服务失败");
			e.printStackTrace();
		}	
		return "";
	}
	/**
	 * 通过本地化配置中名称获取对应的值信息
	 * @param configname
	 * @return
	 */
	public static String loadconfig(String configname){
		String configvalue="";
		try {
			String url = WebHelper.getServerSchema()+"://"+WebHelper.getServerName()+":"+WebHelper.getServerPort()+"/realestate/app/survey/loadconfig/" + configname;
			configvalue = WebHelper.sendHttpRequest(url, RequestMethod.GET);
			if(!StringHelper.isEmpty(configvalue)){
				if(configvalue.startsWith("\"") && configvalue.lastIndexOf("\"")>0){
					configvalue=configvalue.substring(1,configvalue.length()-1);
				}
//				JSONObject attrJson = ProjectHelper.getJsonObject(configvalue);
				return configvalue;
			}
		} catch (IOException e) {
			logger.trace("调用本地化配置服务失败");
			e.printStackTrace();
		}
		return configvalue ;
	}
}

