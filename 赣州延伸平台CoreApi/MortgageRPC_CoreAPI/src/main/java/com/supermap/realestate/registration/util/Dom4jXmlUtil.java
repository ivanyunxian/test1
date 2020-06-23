package com.supermap.realestate.registration.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 
 * @Description:数据转化为xml文件工具类
 * @author diaoliwei
 * @date 2015年10月10日 下午3:06:48
 * @Copyright SuperMap
 */
public class Dom4jXmlUtil {

	/**
	 * 包括内容的document对象 作用：将doc输出为 xml文件
	 * @作者 diaoliwei
	 * @创建时间 2015年10月10日下午3:09:11
	 * @param doc
	 * @param filePath 为String类型的路径+文件名
	 */
	public static void OutputXmlFile(Document doc, String filePath) {
		XMLWriter writer = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8"); // 设置XML文件的编码格式,如果有中文可设置为GBK或UTF-8
		File file = new File(filePath);
		// 如果上面设置的xml编码类型为GBK，或设为UTF-8但其中有中文则应当用FileWriter来构建xml文件（使用以下代码），否则会出现中文乱码问题
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			writer = new XMLWriter(fos, format);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据路径读取xml文件到Document对象中
	 * @作者 diaoliwei
	 * @创建时间 2015年10月10日下午4:43:11
	 * @param filePath
	 * @return
	 */
	public static Document readXmlContent(String filePath){
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(filePath));
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据xml的节点名称获取该节点的文本
	 * @作者 diaoliwei
	 * @创建时间 2015年10月10日下午5:11:52
	 * @param filePath
	 * @param nodeName
	 * @return
	 */
	public static String readXmlByNodeName(String filePath, String nodeName){
		String nodeText = "";
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(filePath));
			// 获取根元素
			Element root = document.getRootElement();
			System.out.println("Root: " + root.getName());
//			nodeText = getNodes(root, nodeName);
			Element element1 = root.element("Head");
			//根据节点名称获取节点文本
			Element element = element1.element(nodeName);
			if(null != element){
				nodeText = element.getTextTrim();
				System.out.println("不动产权证号：" + element.getTextTrim());
			}
			System.out.println("不动产权证号：" + nodeText);
		} catch (DocumentException e) {
			System.out.println("读取xml出错");
			e.printStackTrace();
		}
        return nodeText;
	}
	
	/**
	 * 递归  获取到当前nodeName的节点文本为止
	 * @作者 diaoliwei
	 * @创建时间 2015年10月10日下午5:59:22
	 * @param node
	 * @param nodeName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private static String getNodes(Element node, String nodeName){
		String text = "";
		if(node != null){
			 System.out.println("当前节点名称：" + node.getName());//当前节点名称
			 System.out.println("当前节点的内容：" + node.getTextTrim());//当前节点名称
			 if(nodeName.equals(node.getName())){
				 text = node.getTextTrim();
				 return text;
			 }
			 //递归遍历当前节点所有的子节点  
			 List<Element> listElement = node.elements();//所有一级子节点的list
			 for(Element e : listElement){ //遍历所有一级子节点
			    getNodes(e, nodeName);//递归
			 }
		}
		return text;
	}

}
