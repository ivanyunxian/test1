package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class XmlUtil {

    /**
     * xmlString 转换json
     *
     * @param xmlStr
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static JSONObject xml2JSON(String xmlStr) throws JDOMException, IOException {
        byte[] xml = xmlStr.getBytes();
        return xml2JSON(xml);
    }

    /**
     * xml 转换Json
     *
     * @param xml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static JSONObject xml2JSON(byte[] xml) throws JDOMException, IOException {
        JSONObject json = new JSONObject();
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder sb = new SAXBuilder();
        org.jdom2.Document doc = sb.build(is);
        Element root = doc.getRootElement();
        json.put(root.getName(), iterateElement(root));
        return json;
    }

    private static JSONObject iterateElement(Element element) {
        List node = element.getChildren();
        Element et = null;
        JSONObject obj = new JSONObject();
        List list = null;
        for (int i = 0; i < node.size(); i++) {
            list = new LinkedList();
            et = (Element) node.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0) {
                    continue;
                }
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

    /**
     * @Description 测试样例
     **/
    public static void main(String[] args) throws JDOMException, IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><UpdateRecognitionJarRequest><delayend>0</delayend><delaystart>0</delaystart></UpdateRecognitionJarRequest>";
//        JSONObject json = XmlUtil.xml2JSON(xml.getBytes());
//        System.out.println(json.toJSONString());
        JSONObject jsonObject = XmlUtil.xml2JSON(xml);
        System.out.println("xml : " + xml);
        System.out.println("json : " + jsonObject.toJSONString());
    }
}
