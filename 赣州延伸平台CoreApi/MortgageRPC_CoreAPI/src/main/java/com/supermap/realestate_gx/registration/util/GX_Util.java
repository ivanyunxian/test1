package com.supermap.realestate_gx.registration.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.context.ApplicationContext;

public class GX_Util {
	public static Object findbyid(String id) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		GX_CONFIG gxconfig = (GX_CONFIG) cdao.get(GX_CONFIG.class, id);
		return gxconfig;
	}

	public static void update(GX_CONFIG gxconfig) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		cdao.update(gxconfig);
	}

	public static void saveOrUpdate(GX_CONFIG gxconfig) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		cdao.saveOrUpdate(gxconfig);
	}

	public static List<GX_CONFIG> getListByHQL(Class<GX_CONFIG> gxconfig,
			String hqlCondition, Map<String, String> map) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		List configlist = cdao.getDataList(gxconfig, hqlCondition, map);
		return configlist;
	}

	public static String save(GX_CONFIG entity) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		cdao.save(entity);
		return null;
	}

	public static GX_CONFIG findByFile_number(String FILE_NUMBER) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		StringBuffer hqlCondition = new StringBuffer();
		hqlCondition.append("file_number = '");
		hqlCondition.append(FILE_NUMBER);
		hqlCondition.append("'");
		String condition = hqlCondition.toString();
		List gxconfig = cdao.getDataList(GX_CONFIG.class, condition);
		if ((gxconfig != null) && (gxconfig.size() > 0)) {
			return (GX_CONFIG) gxconfig.get(0);
		}
		return null;
	}

	public static GX_CONFIG findByXmbh(String XMBH) {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		List gxconfig = cdao
				.getDataList(GX_CONFIG.class, "XMBH='" + XMBH + "'");
		if ((gxconfig != null) && (gxconfig.size() > 0)) {
			return (GX_CONFIG) gxconfig.get(0);
		}
		return null;
	}

	public static void gxFlush() {
		CommonDao cdao = (CommonDao) SuperSpringContext.getContext().getBean(
				CommonDao.class);
		cdao.flush();
	}

	public static void saveTdywByfile_Number(String FILE_NUMBER,
			String td_status) {
		GX_CONFIG gxconfig = findByFile_number(FILE_NUMBER);
		gxconfig.setTD_STATUS(td_status);
		save(gxconfig);
	}

	public static void saveTDYW(String FILE_NUMBER, String td_status) {
		GX_CONFIG gxconfig = new GX_CONFIG();
		gxconfig.setFILE_NUMBER(FILE_NUMBER);

		gxconfig.setTD_STATUS(td_status);
		save(gxconfig);
		System.out.println(gxconfig);
	}
	 public static String deUnicode(String content){//将16进制数转换为汉字
		  String enUnicode=null;
		  String deUnicode=null;
		  for(int i=0;i<content.length();i++){
		      if(enUnicode==null){
		       enUnicode=String.valueOf(content.charAt(i));
		      }else{
		       enUnicode=enUnicode+content.charAt(i);
		      }
		      if(i%4==3){
		       if(enUnicode!=null){
		        if(deUnicode==null){
		         deUnicode=String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
		        }else{
		         deUnicode=deUnicode+String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
		        }
		       }
		       enUnicode=null;
		      }
		      
		     }
		  return deUnicode;
		 }
		 public static String enUnicode(String content){//将汉字转换为16进制数
		  String enUnicode=null;
		  for(int i=0;i<content.length();i++){
		   if(i==0){
		       enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
		      }else{
		       enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
		      }
		  }
		  return enUnicode;
		 }
		 private static String getHexString(String hexString){
		      String hexStr="";
		      for(int i=hexString.length();i<4;i++){
		       if(i==hexString.length())
		        hexStr="0";
		       else
		        hexStr=hexStr+"0";
		      }
		      return hexStr+hexString;
		 }
		 
			/**
			 * 通用版获取接口，接口url不带参数,POST
			 * liangq
			 */
		 public static  JSONObject getHttpURLConnection(String name,Map<String, Object> map){
				
				// 调用接口，传递参数给接口
				String Path = GetProperties
						.getConstValueByKey(name);
				String json = JSONObject.toJSONString(map);
				URL url = null;
				HttpURLConnection connet = null;
				OutputStreamWriter osw = null;
				BufferedReader brd = null;
				try {
					// 实例一个URL资源
					url = new URL(Path);
					connet = (HttpURLConnection) url.openConnection();
					connet.setDoInput(true);
					connet.setDoOutput(true);
					connet.setUseCaches(false);
					connet.setConnectTimeout(60000);// 设置连接超时
					connet.setReadTimeout(60000);// 设置读取超时
					connet.setRequestMethod("POST");
					connet.setRequestProperty("Content-Type", "application/json");
					connet.setRequestProperty("Accept-Charset", "utf-8");
					connet.setRequestProperty("contentType", "utf-8");
					osw = new OutputStreamWriter(connet.getOutputStream(), "utf-8");
					osw.write(json);
					osw.flush();
					osw.close();
					connet.connect();
					if (connet.getResponseCode() != 200) {
						throw new IOException(connet.getResponseMessage());
					}
					// 将返回的值存入到String中
					brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = brd.readLine()) != null) {
						sb.append(line);
					}
					brd.close();
					connet.disconnect();
					JSONObject object = JSON.parseObject(sb.toString());
					return object;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
					return null;
				}
				// 使用finally块来关闭输出流、输入流
				finally {
						try {
							if (osw != null) {
								osw.close();
							}
							if (brd != null) {
								brd.close();
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						}
				}
			} 
		    
}
