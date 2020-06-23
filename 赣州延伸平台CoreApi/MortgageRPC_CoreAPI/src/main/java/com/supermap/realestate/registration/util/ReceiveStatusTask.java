package com.supermap.realestate.registration.util;

import javax.transaction.Transactional;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * @Description:获取字典表信息
 * @author 刘树峰
 * @date 2015年6月12日 上午11:53:14
 * @Copyright SuperMap
 */
@Transactional
public class ReceiveStatusTask{
	
	public static String receivestatus="";
	public static String receiveXml="";
	
	public Logger logger = Logger.getLogger(ReportResponseTask.class);
	
	/*
	 * 状态上报
	 */
	public void ReceiveStatus() {
		if(StringHelper.isEmpty(receiveXml)){
			Element Message = DocumentHelper.createElement("Message");
			Document doucment = DocumentHelper.createDocument(Message);
			Element AreaCode=Message.addElement("AreaCode");
			AreaCode.setText(ConfigHelper.getNameByValue("XZQHDM"));
			receiveXml=doucment.asXML();
		}
		if(StringHelper.isEmpty(receiveXml)){
			receiveXml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Message><AreaCode>"+ConfigHelper.getNameByValue("XZQHDM")+"</AreaCode></Message>";
		}
		
		
		String username=ConfigHelper.getNameByValue("UserNameReportWSDL");
		String password=ConfigHelper.getNameByValue("PassWordReportWSDL");
		
		String targetnamespace="http://gd.bdcdataar.org/";
		String url=ConfigHelper.getNameByValue("UrlReportWSDL").replaceAll("ReceiveData", "ReceiveStatus");
		String methodname="ReportStatus";
		
		try {
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if("411623".equals(xzqhdm)){
				//状态上报方法
				String soapActionURI = targetnamespace+methodname;
				Service service = new Service();
				Call call;
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(url);
				call.setUseSOAPAction(true);
				call.setSOAPActionURI(soapActionURI);
				call.setOperationName(new QName(targetnamespace, methodname));
				//定义参数1：报文内容
				call.addParameter(new QName(targetnamespace, "message"), XMLType.XSD_STRING,ParameterMode.IN);
				//定义参数2：用户名
				call.addParameter(new QName(targetnamespace, "userName"), XMLType.XSD_STRING,ParameterMode.IN);
				//定义参数3：密码
				call.addParameter(new QName(targetnamespace, "pwd"), XMLType.XSD_STRING, ParameterMode.IN);
				//返回类型为字符串（响应报文内容JSON字符串）
				call.setReturnType(XMLType.XSD_STRING);
				
				String[] str = new String[3];
				//报文内容
				str[0] = receiveXml;
				System.out.print("receiveXml："+receiveXml);
				str[1] = username;
				System.out.print("username："+username);
				str[2] = password;
				String password_md5=com.supermap.wisdombusiness.utility.StringHelper.encryptMD5(password);
				str[2] = password_md5;
				System.out.print("password_md5："+password_md5);
				Object obj = call.invoke(str);
				System.out.print("obj："+obj);
			}
		} catch (Exception e) {
			System.out.print("Exception："+e.getMessage());
			e.printStackTrace();
		}
	}
	
}