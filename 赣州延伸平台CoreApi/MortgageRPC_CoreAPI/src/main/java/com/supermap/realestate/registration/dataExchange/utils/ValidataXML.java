package com.supermap.realestate.registration.dataExchange.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;

/** 
*  XML校验测试 
* @date 2015年12月30日-下午11:49:01
* @author diaoliwei
*/ 
public class ValidataXML {
	
	 public static final String ELEMENT_ALL_LIMITED = "cos-all-limited.2"; //
	    
	 public static final String ELEMENT_LOSE = "cvc-complex-type.2.4.a"; //元素缺失
	    
	 public static final String ELEMENT_PATTERN_VALID = "cvc-pattern-valid"; //正则校验错误
	    
     public static final String ELEMENT_REQUIRED_TEXT_LENGTH = "cvc-length-valid";  //元素长度不正常(固定长度)
		
	 public static final String ELEMENT_REQUIRED_TEXT_MIN_LENGTH = "cvc-minLength-valid";  //元素长度不正常(最小长度)
		
	 public static final String ELEMENT_REQUIRED_TEXT_MAX_LENGTH = "cvc-maxLength-valid";  //元素长度不正常(最大长度)
		
	 public static final String ELEMENT_TEXT_VALID = "cvc-type.3.1.3"; //元素值无效
		
	 public static final String ELEMENT_ENUMERATION="cvc-enumeration-valid"; //枚举错误

	 public static final String ELEMENT_CHILDREN_LOSE = "cvc-complex-type.2.4.b"; //子元素丢失

	 public static final String ELEMENT_REQUIERD_ATTRIBUTE_VALUE_LOSE="cvc-pattern-valid"; //
		
	 public static final String ELEMENT_OPTIONAL_ATTRIBUTE_VALUE_LOSE="cvc-attribute.3"; 
		
	 public static final String SINGLE_QUOTE = "\'";
		
	 public static final String ELEMENT_OPPTIONAL_TEXT_DATATYPE = "cvc-datatype-valid.1.2.1";
		
	 public static final String ELEMENT_WITH_ATTRIBUTE_LENGTH = "cvc-complex-type.2.2";
		
	 public static final String ELEMENT_ATTRIBUTE_UNIT="cvc-complex-type.3.1"; //元素属性重复
		 
	 public static final String ELEMENT_REQUIRED_ATTRIBUTE_LOSE = "cvc-complex-type.4";
    
    /** 
     * 通过XSD（XML Schema）校验XML
     * @param xmlScehmaName  接入业务编码
	 * @date 2015年12月30日-下午11:49:01
	 * @author diaoliwei
     */ 
    public static String validateXMLByXSD(String xmlScehmaName,String xmlFilePath) {
        File file = new File(xmlFilePath);
        String realPath = xmlFilePath.substring(0,xmlFilePath.lastIndexOf("\\") + 1);
        String xsdFileName = realPath + "schema\\" + xmlScehmaName +".xsd";
        try { 
            //创建默认的XML错误处理器 
            XMLErrorHandler errorHandler = new XMLErrorHandler(); 
            //获取基于 SAX 的解析器的实例 
            SAXParserFactory factory = SAXParserFactory.newInstance(); 
            //解析器在解析时验证 XML 内容。 
            factory.setValidating(true); 
            //指定由此代码生成的解析器将提供对 XML 名称空间的支持。 
            factory.setNamespaceAware(true); 
            //使用当前配置的工厂参数创建 SAXParser 的一个新实例。 
            SAXParser parser = factory.newSAXParser(); 
            //创建一个读取工具 
            SAXReader xmlReader = new SAXReader(); 
            //获取要校验xml文档实例 
            Document xmlDocument = (Document) xmlReader.read(file); 
            //设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在 [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。 
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); 
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",  "file:" + xsdFileName); 
            //创建一个SAXValidator校验工具，并设置校验工具的属性 
            SAXValidator validator = new SAXValidator(parser.getXMLReader()); 
            //设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。 
            validator.setErrorHandler(errorHandler); 
            //校验 
            validator.validate(xmlDocument); 
            //XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint()); 
            //如果错误信息不为空，说明校验失败，打印错误信息 
            //0:失败 1：成功
             //失败时返回信息格式：0|错误信息
             //成功时返回信息格式1：成功
            if (errorHandler.getErrors().hasContent()) { 
              //writer.write(errorHandler.getErrors());
            	return "0|" + parseSchemaErrors(errorHandler);
            } else { 
                return "1" ;// XML文件通过XSD文件校验成功 
            } 
        } catch (Exception ex) { 
             return "0|" + ex.getMessage(); // XML文件通过XSD文件校验失败 
        }
    }
    
    /** 
     * 通过XSD（XML Schema）校验XML
     */ 
    public static Map<Boolean,String> validateXMLByXSDExt(String xmlScehmaName,File file) { 
       Map<Boolean,String> result = new HashMap<Boolean,String>();
       String xsdFileName = GetProperties.getConstValueByKey("schemaPath") + xmlScehmaName + ".xsd";
        try { 
            //创建默认的XML错误处理器 
            XMLErrorHandler errorHandler = new XMLErrorHandler(); 
            //获取基于 SAX 的解析器的实例 
            SAXParserFactory factory = SAXParserFactory.newInstance(); 
            //解析器在解析时验证 XML 内容。 
            factory.setValidating(true); 
            //指定由此代码生成的解析器将提供对 XML 名称空间的支持。 
            factory.setNamespaceAware(true); 
            //使用当前配置的工厂参数创建 SAXParser 的一个新实例。 
            SAXParser parser = factory.newSAXParser(); 
            //创建一个读取工具 
            SAXReader xmlReader = new SAXReader(); 
            //获取要校验xml文档实例 
            Document xmlDocument = (Document) xmlReader.read(file); 
            //设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在 [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。 
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",  "http://www.w3.org/2001/XMLSchema"); 
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",  "file:" + xsdFileName); 
            //创建一个SAXValidator校验工具，并设置校验工具的属性 
            SAXValidator validator = new SAXValidator(parser.getXMLReader()); 
            //设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。 
            validator.setErrorHandler(errorHandler); 
            //校验 
            validator.validate(xmlDocument); 

             //XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint()); 
            //如果错误信息不为空，说明校验失败，打印错误信息 
            if (errorHandler.getErrors().hasContent()) {
            	result.put(false,parseSchemaErrors(errorHandler));
            } else { 
                 result.put(true,"");// XML文件通过XSD文件校验成功 
            } 
        } catch (Exception ex) { 
             result.put(false,ex.getMessage());// XML文件校验异常 
             return result;
        }   
    	return result;
    } 
    
    
    /**
     * 获取校验错误信息
     * @param errorHandler
     * @date 2015年12月31日-下午16:09:10
	 * @author diaoliwei
     * @return
     */
    @SuppressWarnings("unchecked")
	private static String parseSchemaErrors(XMLErrorHandler errorHandler){
    	  //存储错误信息
    	StringBuffer errors = new StringBuffer();
    	boolean trag = false;
    	Document errorDoc = null;
		try {
			errorDoc = DocumentHelper.parseText(errorHandler.getErrors().asXML());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = errorDoc.getRootElement(); //Element 文档中的元素
		for (Iterator<Element> iter = root.elementIterator("error"); iter.hasNext();) {
			if(trag == false){
				trag = true; 
			}else{
				errors.append("<br>");
			}
			Element childElement = iter.next();
			String childMessage = childElement.getText();
			if(childMessage.contains(ELEMENT_ALL_LIMITED)){
				errors.append(validateAll(childMessage));
			}else if(childMessage.contains(ELEMENT_PATTERN_VALID)){//正则校验
				String nextErrorMsg = iter.next().getText();
				errors.append(validatePatternMsg(childMessage,nextErrorMsg));
			}else if(childMessage.contains(ELEMENT_REQUIRED_TEXT_LENGTH)){//长度不正确
				String nextErrorMsg = iter.next().getText();
				errors.append(validateLengthValid(childMessage,nextErrorMsg));
			}else if(childMessage.contains(ELEMENT_REQUIRED_TEXT_MIN_LENGTH)){
				String nextErrorMsg = iter.next().getText();
				errors.append(validateMinLengthValid(childMessage,nextErrorMsg));
			}else if(childMessage.contains(ELEMENT_REQUIRED_TEXT_MAX_LENGTH)){
				String nextErrorMsg = iter.next().getText();
				errors.append(validateMaxLengthValid(childMessage,nextErrorMsg));
			}else if(childMessage.contains(ELEMENT_ENUMERATION)){
				String nextErrorMsg = iter.next().getText();
				errors.append(validateEnumeration(childMessage,nextErrorMsg));
			}else if (childMessage.contains(ELEMENT_OPPTIONAL_TEXT_DATATYPE)) {
				String nextErrorMsg = iter.next().getText();
				errors.append(validateDataType(childMessage,nextErrorMsg));
			}else if(childMessage.contains(ELEMENT_CHILDREN_LOSE)){
				errors.append(validateChildrenLose(childMessage));
			}else if(childMessage.contains(ELEMENT_REQUIRED_ATTRIBUTE_LOSE)){
				errors.append(validateRequiredAttributeLose(childMessage));//ELEMENT_REQUIRED_ATTRIBUTE_LOSE
			}else if(childMessage.contains(ELEMENT_LOSE)){
				errors.append(validateElementLose(childMessage));//ELEMENT_REQUIRED_ATTRIBUTE_LOSE
			}else {
				errors.append(childMessage);
			}
		}
    	return errors.toString();
    }
    
	private static String validateAll(String errorMsg){
		try{
			int i = errorMsg.lastIndexOf("element");
			int j = errorMsg.lastIndexOf("is invalid");
			String realMessage = errorMsg.substring(i + 9, j-2);
			return "Schema中元素" + realMessage + "选择器 all 最大/小次数设置错误";
		}catch(Exception e){
			return errorMsg;
		}
	}
	
    //正则校验不正确错误处理
	private static String validatePatternMsg(String errorMsg,String nextErrorMsg){
		try{
		    return getPrefixMsg(nextErrorMsg) + " 赋值非法,原因：正则校验不通过.正则表达式为：" + errorMsg.substring(errorMsg.lastIndexOf("pattern") + 9,
		    		errorMsg.lastIndexOf("' for type")) + " ,当前值:" + errorMsg.substring(errorMsg.lastIndexOf("Value '") + 7, errorMsg.lastIndexOf("' is not facet-valid"));
	    }catch(Exception e){
			return errorMsg;
		}
    }
	
   //长度不正确错误处理
	private static String validateLengthValid(String errorMsg,String nextErrorMsg){
		try{
		    //描述
			return getPrefixMsg(nextErrorMsg) + " 长度不正确,原因：要求固定长度为：" + errorMsg.substring(errorMsg.lastIndexOf("respect to length") + 19,
					errorMsg.lastIndexOf("' for type")) + "位" + " ,当前值:" + errorMsg.substring(errorMsg.lastIndexOf("Value '") + 7, errorMsg.lastIndexOf("' with length"));
	    }catch(Exception e){
			return errorMsg;
		}
    }
	
   //最小长度不正确处理
	private static String validateMinLengthValid(String errorMsg,String nextErrorMsg){
		try{
	    	return getPrefixMsg(nextErrorMsg) + " 长度不正确,原因：要求最小长度为：" + errorMsg.substring(errorMsg.lastIndexOf("respect to minLength '") + 22,
	    			errorMsg.lastIndexOf("' for type")) + "位" + " ,当前值:" + errorMsg.substring(errorMsg.lastIndexOf("Value '") + 7, errorMsg.lastIndexOf("' with length"));
	    }catch(Exception e){
			return errorMsg;
		}
    }
   
  //最大长度不正确处理
	private static String validateMaxLengthValid(String errorMsg,String nextErrorMsg){
		try{
			return getPrefixMsg(nextErrorMsg) + " 长度不正确,原因：要求最大长度为：" + errorMsg.substring(errorMsg.lastIndexOf("respect to maxLength '") + 22, 
					errorMsg.lastIndexOf("' for type")) + "位" + " ,当前值:" + errorMsg.substring(errorMsg.lastIndexOf("Value '") + 7, errorMsg.lastIndexOf("' with length"));
		}catch(Exception e){
			return errorMsg;
		}
    }
    
	private static String validateEnumeration(String errorMsg,String nextErrorMsg){
		try{
			//左方括号
			int i = errorMsg.indexOf("\u005B");
			int j = errorMsg.indexOf("\u005D");
	//		return getPrefixMsg(nextErrorMsg)+"赋值非法,原因：枚举类型，取值范围为："+errorMsg.substring(i+1,j);
			return getPrefixMsg(nextErrorMsg) + "赋值非法,原因：不在枚举范围内";
		}catch(Exception e){
			return errorMsg;
		}
	}
	
	private static String validateDataType(String errorMsg,String nextErrorMsg){
		try{
	    	int m = errorMsg.lastIndexOf("for");
			int n = errorMsg.lastIndexOf(SINGLE_QUOTE);
			String typeName = errorMsg.substring(m + 5,n);
			return getPrefixMsg(nextErrorMsg) + "赋值无效,原因：要求类型为：" + typeName;
		}catch(Exception e){
			return errorMsg;
		}
    }
	
	private static String validateChildrenLose(String errorMsg){
		try{
	    	//元素
			int m = errorMsg.lastIndexOf("element");
			int n = errorMsg.lastIndexOf("' is not complete");
			String element = errorMsg.substring(m + 9, n);
			// 子元素
			int i = errorMsg.lastIndexOf("\u007B");
			int j = errorMsg.lastIndexOf(SINGLE_QUOTE);
			String realMessage = errorMsg.substring(i + 1, j - 1);
			return "元素" + element + "不完整,缺少子元素" + realMessage;
		}catch(Exception e){
			return errorMsg;
		}
    }
	
    //元素必填属性缺失
	private static String validateRequiredAttributeLose(String errorMsg){
		try{
		   int i = errorMsg.indexOf(SINGLE_QUOTE);
		   int j = errorMsg.indexOf("must");
		   String attributeMessage = errorMsg.substring(i + 1, j - 2);
	
		   int m = errorMsg.indexOf("element");
		   int n = errorMsg.lastIndexOf(SINGLE_QUOTE);
		   String elementMessage = errorMsg.substring(m + 9, n);
		   return "元素" + elementMessage + "属性" + attributeMessage + "缺失";
		}catch(Exception e){
	       return errorMsg;
	    }
   }
   
	private static String validateElementLose(String errorMsg){
		try{
		   int i = errorMsg.lastIndexOf("element");
		   int j = errorMsg.lastIndexOf("One of ");
		   return "元素" + errorMsg.substring(i + 9, j - 3) + "异常,冗余或重复";
		}catch(Exception e){
			return errorMsg;
		}
   }
   
	private static String getPrefixMsg(String nextErrorMsg) throws Exception{
	    String msg = "元素 ";
	    if(nextErrorMsg.contains(ELEMENT_TEXT_VALID)){
	    	msg += validateElementTextLength(nextErrorMsg);
		}else if(nextErrorMsg.contains(ELEMENT_OPTIONAL_ATTRIBUTE_VALUE_LOSE)){
			String [] attrMgs = validateElementAttributeUnit(nextErrorMsg);
			msg += attrMgs[1] + "属性 " + attrMgs[0];
		}
	    return msg;
    }
	
	private static String validateElementTextLength(String message)  throws Exception{
		int i = message.lastIndexOf("element");
		int j = message.lastIndexOf(SINGLE_QUOTE);
		String realMessage = message.substring(i + 9, j);
		return realMessage;
	}
	
	private static String[] validateElementAttributeUnit(String message) throws Exception {
		int i = message.lastIndexOf("attribute");
		int j = message.indexOf("element");
		String attributeName = message.substring(i+11,j-5);
			
		int n = message.indexOf("is");
		String elementName = message.substring(j+9,n-2);
			
		int k = message.lastIndexOf("of");
		int l = message.lastIndexOf(SINGLE_QUOTE);
		String unit = message.substring(k+4,l);
			
		String[] realMessage = new String[]{attributeName,elementName,unit};
		return realMessage;
	}
}