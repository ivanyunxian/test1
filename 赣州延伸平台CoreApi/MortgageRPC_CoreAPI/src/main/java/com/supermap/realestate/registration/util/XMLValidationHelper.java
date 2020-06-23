package com.supermap.realestate.registration.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author wuz
 *用来验证XML文件的合法性。
 */
public class XMLValidationHelper {
	private static String BaseFullPath="";//根目录物理路径
	
    public static boolean validateXMLSchema(String xsdPath, String xmlPath){
        
        try {
        	if(StringUtils.isEmpty(BaseFullPath))
        	{
        		BaseFullPath=GetClassesPath().split("WEB-INF")[0];
        	}
        	xsdPath=BaseFullPath+xsdPath;
        	xmlPath=BaseFullPath+xmlPath;
        	File xsdfile= new File(xsdPath);
        	File xmlfile=new File(xmlPath);
        	if(!xsdfile.exists())//如果校验的XSD文件不存在，则默认不校验。
        		return true;
        	if(!xmlfile.exists())//如果进行校验的xml文件不存在，则校验不通过。
        		return false;
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * 获取站点的Classes的本地路径
     * 
     * @Author：俞学斌
     */
    private static String GetClassesPath() {
	URL url = Thread.currentThread().getContextClassLoader()
		.getResource("");
	String path = url.getPath();
	path = path.substring(1);
	return path;
    }
}
