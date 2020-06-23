package com.supermap.realestate.registration.util;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;

import Gd.Signature.GdRsa;

import com.inspur.uaccess.common.utils.RSASignature;

/**
 * 
 * @Description:加签工具类
 * @author diaoliwei
 * @date 2015年10月10日 下午3:34:04
 * @Copyright SuperMap
 */
public class RsaXmlUtil {
	
	/**
	 * 生成加签后的xml文件
	 * @作者 diaoliwei
	 * @创建时间 2015年10月10日下午4:15:23
	 * @param localFilePath  带文件名的路径
	 */
	public static String rsaXmlFile(String localFilePath,boolean transferred){
		String xmlstr="";
		try {
			//加签前格式化xml
			Document document = Dom4jXmlUtil.readXmlContent(localFilePath);
			Dom4jXmlUtil.OutputXmlFile(document, localFilePath);
			//XML字符串
			String xml = FileUtils.readFileToString(new File(localFilePath), "UTF-8");
			if(transferred){
				xml=xml.replaceAll("\n", "").replaceAll("\r", "");
			}
			//获取加签私钥
			String key = ConfigHelper.getNameByValue("xmlKey");
			System.out.println("加签私钥key:" + key);
			//获取加签后报文
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if("411623".equals(xzqhdm)){
				xmlstr = GdRsa.getNewMsgWithSignature(xml, key);
			}else{
				xmlstr = RSASignature.getNewMsgWithSignature(xml, key);
			}
			File file = new File(localFilePath);
			FileUtils.writeStringToFile(file, xmlstr,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlstr;
	}
	
	/**
	 * 通过文件转换xml字符串
	 * @作者 yuxuebin
	 * @创建时间 2017年17月20日17:20:23
	 * @param localFilePath  带文件名的路径
	 * @param strxml  xml字符串
	 */
	public static String formatXml(String localFilePath,String strxml){
		String xmlstr="";
		try {
			System.out.println("响应报文:" + strxml);
			FileWriter fw=new FileWriter(localFilePath); 
			fw.write(strxml);
			fw.flush();        
			fw.close();
			//XML字符串
			xmlstr = FileUtils.readFileToString(new File(localFilePath), "UTF-8");
			if(!StringHelper.isEmpty(xmlstr)&&xmlstr.startsWith("?")){
				xmlstr=xmlstr.substring(1, xmlstr.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlstr;
	}

}
