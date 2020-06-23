package com.supermap.intelligent.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/**
 * 税务对接中使用到的XML处理
 * 除特殊说明外，通用
 * @author txd
 *
 */
public class XMLHelp {
	
	/**
	 * 根据xml读取指定节点,返回list<Map>
	 * @param node 格式"/list/fcxx"
	 * * @param Stringxml String类型的xml
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map> readxmlbyString(String node,String Stringxml){
		List<Map> xmldata =new ArrayList<Map>();
		Map<String, Object> elementMap = new HashMap<String, Object>();
		try {
			Document document = DocumentHelper.parseText(Stringxml);
			List<Element> elements=document.selectNodes(node);
			for (int i = 0; i < elements.size(); i++) {
				for(Iterator it=elements.get(i).elementIterator();it.hasNext();){       
					Element element = (Element) it.next(); 
					elementMap.put(element.getName(),element.getText());
				}
				xmldata.add(elementMap);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return xmldata;
		
	}
	/**
	 * 根据xml读取指定节点,返回list<Map>
	 * @param node 格式"/list/fcxx"
	 * @param searchfilepath xml文件完整路径
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> readxml(String node,String searchfilepath){
		List<Map> xmldata =new ArrayList<Map>();
		Map<String, Object> elementMap = new HashMap<String, Object>();
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(searchfilepath));
			List<Element> elements=document.selectNodes(node);
			for (int i = 0; i < elements.size(); i++) {
				for(Iterator it=elements.get(i).elementIterator();it.hasNext();){       
					Element element = (Element) it.next(); 
					elementMap.put(element.getName(),element.getText());
				}
				xmldata.add(elementMap);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return xmldata;
		
	}

	  /**获取指定节点的Document
	 * @param cxnodes 
	 * @param jgdm
	 * @return 
	 */
	public static String[] GetValueByNodename(String cxnodes, String jgdm){
		  if(StringHelper.isEmpty(jgdm)){
			  try {
				  SAXReader reader = new SAXReader();
				  Document doc =reader.read(new File(XMLHelp.class.getResource(("/resultConfig2.xml")).getPath()));
				  return doc.selectSingleNode("//JG[@JGDM='"+jgdm+"']/"+cxnodes+"").getText().split(",");
			  } catch (DocumentException e) {
				  //
			  }
		  }
		return null;
  }
	  public static void SetValueByNodename(String cxnodes,String jgdm,String cxnodesText){
			  try {
				  SAXReader reader = new SAXReader();
				  Document doc =reader.read(new File(XMLHelp.class.getResource(("/resultConfig2.xml")).getPath()));
				  doc.selectSingleNode("//JG[@JGDM='"+jgdm+"']/"+cxnodes+"").getParent().setText(cxnodesText);
			  } catch (DocumentException e) {
				  //
			  }
  }
		/**
		 * 保存xml到指定位置
		 * @param filename 路径
		 * @param doc xml
		 */
		public static void DocsaveXML(String filename,Document doc){
			try{
				 OutputFormat formater = OutputFormat.createPrettyPrint();
					formater.setEncoding("utf-8");
					XMLWriter writer = new XMLWriter(new FileOutputStream(new File(filename)), formater);
					writer.write(doc);
					writer.close();
					System.out.println("保存XML文件成功!");
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
		/**
		 * 保存xml到指定位置
		 * @param filename 路径
		 * @param xml xml
		 */
		public static void StringsaveXML(String filename,String xml){
			try{
				 OutputFormat formater = OutputFormat.createPrettyPrint();
					formater.setEncoding("utf-8");
					XMLWriter writer = new XMLWriter(new FileOutputStream(new File(filename)), formater);
					writer.write(xml);
					writer.close();
					System.out.println("保存XML文件成功!");
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	 /**根据机构代码读取xxx.xml节点
	 * @param jgdm  机构代码
	 * @param resultConfigname  格式： /xxx.xml
	 * @return  某机构的Element
	 */
	public  static Element  getElementByJgdm(String jgdm,String resultConfigname){
		 SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(new File(URLDecoder.decode(XMLHelp.class.getResource((resultConfigname)).getPath(),"UTF-8")));
				return  doc.selectSingleNode("//JG[@JGDM='"+jgdm+"']/fieldconfig").getParent();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			return null;
	 }
	
	/**读取配置文件处理后wheresql
	 * @param element
	 * @param table
	 * @return ALLField所有字段(配置的字段)
	 * ALLField拼接比如： FDCQ.FWXZ FDCQ_FWXZ,FDCQ.JZMJ FDCQ_JZMJ,
	 * _ALLField拼接比如： FDCQ_FWXZ,FDCQ_JZMJ
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, List<String>> formatSelectSql(Element element, String table, Boolean boo) {
		List<Element> elements = element.elements("table");
		Set<String> tablelist = new HashSet<String>(elements.size());
		List<String> _tablelist = new ArrayList<String>(elements.size());
		List<String> leftjoinlist = new ArrayList<String>(16);
		List<String> extraList = new ArrayList<String>(16);
		Map<String, List<String>> map = new HashMap<String, List<String>>(4);
		if (boo) {
			if ("FDCQ".equals(table)) {
				for (Element e : elements) {
					List<String> list = new ArrayList<String>(1);
					List<String> _list = new ArrayList<String>(1);
					String tablename = e.attributeValue("name");
					String alias = tablename.substring(10, tablename.length());
					String[] getnodes = e.element("fields").getText().split(",");
					if ("BDCK.BDCS_H_XZ".equals(tablename)) {
						for (int i = 0; i < getnodes.length; i++) {
							list.add("H." + getnodes[i] + " " + "H_" + getnodes[i]);
							_list.add("H_" + getnodes[i]);
							if (!"BDCDYLX".equals(getnodes[i])) {
								extraList.add("H." + getnodes[i]);
							}
						}
						tablelist.addAll(list);
						_tablelist.addAll(_list);
					} else if ("BDCK.BDCS_H_XZY".equals(tablename)) {
						// skip
					} else {
						for (int i = 0; i < getnodes.length; i++) {
							list.add(alias + "." + getnodes[i] + " " + alias + "_" + getnodes[i]);
							_list.add(alias + "_" + getnodes[i]);
						}
						tablelist.addAll(list);
						_tablelist.addAll(_list);
					}
				}
				// map.put("ALLField",tablelist);
				// map.put("_ALLField",_tablelist);
//				map.put("leftjoinSQL", leftjoinlist);
				// map.put("extraSql", extraList);
			} else if ("JSYDSYQ".equals(table)) {
				for (Element e : elements) {
					List<String> list = new ArrayList<String>(1);
					List<String> _list = new ArrayList<String>(1);
					String tablename = e.attributeValue("name");
					String alias = tablename.substring(10, tablename.length());
					String[] getnodes = e.element("fields").getText().split(",");
					if ("BDCK.BDCS_SHYQZD_XZ".equals(tablename)) {
						for (int i = 0; i < getnodes.length; i++) {
							list.add("ZD." + getnodes[i] + " " + "ZD_" + getnodes[i]);
							_list.add("ZD_" + getnodes[i]);
							if (!"BDCDYLX".equals(getnodes[i])) {
								extraList.add("ZD." + getnodes[i]);
							}
						}
						tablelist.addAll(list);
						_tablelist.addAll(_list);
					} else {
						for (int i = 0; i < getnodes.length; i++) {
							list.add(alias + "." + getnodes[i] + " " + alias + "_" + getnodes[i]);
							_list.add(alias + "_" + getnodes[i]);
						}
						tablelist.addAll(list);
						_tablelist.addAll(_list);
					}
				}
				// map.put("ALLField",tablelist);
				// map.put("_ALLField",_tablelist);
//				map.put("leftjoinSQL", leftjoinlist);
				// map.put("extraSql", extraList);
			} else {
				for (Element e : elements) {
					List<String> list = new ArrayList<String>(1);
					List<String> _list = new ArrayList<String>(1);
					String tablename = e.attributeValue("name");
					String alias = tablename.substring(10, tablename.length());
					String[] getnodes = e.element("fields").getText().split(",");
					for (int i = 0; i < getnodes.length; i++) {
						list.add(alias + "." + getnodes[i] + " " + alias + "_" + getnodes[i]);
						_list.add(alias + "_" + getnodes[i]);
					}
					tablelist.addAll(list);
					_tablelist.addAll(_list);

				}
				// map.put("ALLField",tablelist);
				// map.put("_ALLField",_tablelist);
				// if("FDCQ".equals(alias)&&!"FDCQ".equals(table)){
				// leftjoinlist.add("LEFT JOIN BDCJCS_FDCQ2 FDCQ ON
				// FDCQ.BSM=QLR.QLBSM");
				// map.put("leftjoinSQL", leftjoinlist);
				// }
				// map.put("extraSql", extraList);
			}
		} else {
			for (Element e : elements) {
				List<String> _list = new ArrayList<String>(1);
				String getnode = e.element("fields").getText();
				String[] getnodes = getnode.split(",");
				for (String fields : getnodes) {
					String tablename = e.attributeValue("name");
					String[] _field=fields.trim().toUpperCase().split(" ");
					_list.add(_field[_field.length-1]);
					if ("BDCK.BDCS_H_XZ".equals(tablename) || "BDCK.BDCS_SHYQZD_XZ".equals(tablename)) {
						extraList.add(_field[0]);
					}
				}
				if("FDCQ".equals(table)){
					tablelist.add("H.BDCDYLX");
				}
				if("JSYDSYQ".equals(table)){
					tablelist.add("ZD.BDCDYLX");
				}
				tablelist.add(getnode);
				_tablelist.addAll(_list);
			}
		}
		map.put("ALLField", new ArrayList(tablelist));
		map.put("_ALLField", _tablelist);
		map.put("extraSql", extraList);
		map.put("leftjoinSQL", leftjoinlist);
		return map;
	}

}
