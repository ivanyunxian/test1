package com.supermap.wisdombusiness.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;




import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;  

import javax.xml.namespace.QName;  
import javax.xml.soap.MessageFactory;  
import javax.xml.soap.SOAPBody;  
import javax.xml.soap.SOAPBodyElement;  
import javax.xml.soap.SOAPConstants;  
import javax.xml.soap.SOAPEnvelope;  
import javax.xml.soap.SOAPMessage;  
import javax.xml.ws.Dispatch;  
  


import org.w3c.dom.Document; 









import com.supermap.wisdombusiness.framework.model.SystemModule;

/**
 * 
 * @author 刘树峰
 *
 */
public class Helper {

	public static List<SystemModule> createSysModuleTree(List<SystemModule> list) {
		List<SystemModule> modules = new ArrayList<SystemModule>();
		for (int i = 0; i < list.size(); i++) {
			SystemModule resource = list.get(i);
			if (resource.getParentId() == null || resource.getParentId().equals("")) {
				createTree(list, resource);
				modules.add(resource);
			}
		}
		return modules;
	}

	public static void createTree(List<SystemModule> list, SystemModule currentNode) {
		for (int i = 0; i < list.size(); i++) {
			SystemModule newNode = list.get(i);
			if (newNode.getParentId() != null && newNode.getParentId().compareTo(currentNode.getId()) == 0) {
				if (currentNode.children == null) {
					currentNode.children = new ArrayList<SystemModule>();
				}
				currentNode.children.add(newNode);
				createTree(list, newNode);
			}
		}
	}
	// 登录页面用户名密码调用webservice接口
	public static Object WebService(Map<String,String> m, String url, String Method,boolean isstr) {

		String soapaction = "http://tempuri.org/";//"http://tempuri.org/"; // 域名，这是在server定义的
		Object v = null;
		Service service = new Service();
		try {

			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
			Object[] param = new Object[m.size()];
			call.setOperationName(new QName(soapaction, Method)); // 设置要调用哪个方法
			int i = 0;
			for (Map.Entry<String,String> entry : m.entrySet()) {
				call.addParameter(new QName(soapaction, entry.getKey()), // 设置要传递的参数
						org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
				param[i] = entry.getValue();
				i++;
			}
             if(isstr){
            	 call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// （标准的类型）
             }else{
            	 call.setReturnType(new QName(soapaction, Method), Vector.class);
             }
			
			// // 要返回的数据类型（自定义类型）
			//
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapaction + Method);
			v = call.invoke(param);// 调用方法并传递参数
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v;
	}
	
	//模拟soapUI调用webservice getSlxx;
	public static Object WebService2( String url, String ywuh) throws Exception  {
		String result = new String ();
		StringBuilder soapHeader = new StringBuilder();
		soapHeader.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
							"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.xzsp.com\">"+
							   "<soapenv:Header/>"+
							   "<soapenv:Body>"+
							      "<ser:getSlxx>"+
							         "<ser:in0>"+ywuh+"</ser:in0>"+
							      "</ser:getSlxx>"+
							   "</soapenv:Body>"+
							"</soapenv:Envelope>");
	/*	File file = new File ("D://soap.xml");
		BufferedReader reader = null;
		try{
			reader =  new  BufferedReader(new FileReader(file));
			String tempString = null;
			while((tempString=reader.readLine())!=null){
				soapHeader.append(tempString);
			}
			reader.close();
		}catch(Exception e){
			
		}finally{
			if(reader!=null){
				reader.close();
			}
		}*/
		System.out.println("soapHeader:"+soapHeader);
		URL U = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)U.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.setRequestProperty("Host", "10.0.8.74:8089");
		conn.setRequestProperty("Content-Type", "ext/xml;charset=UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(soapHeader.length()));
		conn.setRequestProperty("SOAPAction", "");
		conn.setRequestMethod("POST");
		OutputStream output = conn.getOutputStream();
		if(soapHeader!=null){
			byte[] b = soapHeader.toString().getBytes("UTF-8");
			output.write(b,0,b.length);
			
		}
		output.flush();
		InputStream in = conn.getInputStream();
		result=IOUtils.toString(in,"UTF-8");
		output.close();
		conn.disconnect();
	    return result;
	}
		
		
		
		
		
		
		
		
		
		
		
		

}
