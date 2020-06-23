package com.supermap.realestate.registration.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
//处理生成PDF文件的类 WUZHU
public class PDFHelper {
	private static String PDFTemplateFullPath="%s\\resources\\PDF\\tpl\\%s.pdf";//存放PDF模板路径
	private static String PDFTempFullPath="%s\\resources\\PDF\\tmp\\%s.pdf";//存放PDF临时文件路径
	
	//根据PDF模板创建PDF文件，返回创建文件的路径 WUZHU
	public static String CreatePdf(PDFDataContainer pdfData,String createdPDFFileName,HttpServletRequest request) throws IOException, DocumentException{
		String pdfFileUrl = String.format(PDFTempFullPath,request.getContextPath(),createdPDFFileName);
		String pdfFileFullPath = String.format(PDFTempFullPath,request.getRealPath("/"),createdPDFFileName);
		String pdfTemplateFullPath =String.format(PDFTemplateFullPath,request.getRealPath("/"),pdfData.getPdfTemplateName());
		FileOutputStream outstream = new FileOutputStream(pdfFileFullPath);
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		fos = CreatePdfStream(pdfTemplateFullPath, pdfData.getData());
		if (fos.size() > 0) {
			Document doc = new Document();
			PdfCopy pdfCopy = new PdfCopy(doc, outstream);
			doc.open();
			PdfImportedPage impPage = null;
			PdfReader reader = new PdfReader(fos.toByteArray());
			int page = reader.getNumberOfPages();
			for (int p = 1; p <= page; p++) {
				impPage = pdfCopy.getImportedPage(reader, p);
				pdfCopy.addPage(impPage);
			}
			doc.close();
		}
		return pdfFileUrl;
	}
	//根据PDF模板集创建单个PDF文件，返回创建文件的路径 WUZHU
	public static String CreatePdfs(List<PDFDataContainer> pdfDatas,String createdPDFFileName,HttpServletRequest request) throws IOException, DocumentException{
		String pdfFileUrl = String.format(PDFTempFullPath,request.getContextPath(),createdPDFFileName);
		String pdfFileFullPath = String.format(PDFTempFullPath,request.getRealPath("/"),createdPDFFileName);
		FileOutputStream outstream = new FileOutputStream(pdfFileFullPath);
		ArrayList<ByteArrayOutputStream> baos = new ArrayList<ByteArrayOutputStream>();
		for(PDFDataContainer pdfdata:pdfDatas)
		{
			String pdfTemplateFullPath =String.format(PDFTemplateFullPath,request.getRealPath("/"),pdfdata.getPdfTemplateName());
			ByteArrayOutputStream bao = null;
			bao = CreatePdfStream(pdfTemplateFullPath, pdfdata.getData());
			baos.add(bao);
		}
		if (baos.size() > 0) {
			Document doc = new Document();
			PdfCopy pdfCopy = new PdfCopy(doc, outstream);
			doc.open();
			PdfImportedPage impPage = null;
			PdfReader reader=null;
			for (ByteArrayOutputStream bao : baos) {
				 reader = new PdfReader(bao.toByteArray());
				int page = reader.getNumberOfPages();
				for (int p = 1; p <= page; p++) {
					impPage = pdfCopy.getImportedPage(reader, p);
					pdfCopy.addPage(impPage);
				}
			}
			doc.close();
		}
		return pdfFileUrl;
	}
	
	// 根据PDF模板创建流WUZHU
		public static ByteArrayOutputStream CreatePdfStream(String fulltplPathName, Map<String, Object> data) throws IOException, DocumentException {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			PdfReader reader = new PdfReader(fulltplPathName);
			PdfStamper stamp = new PdfStamper(reader, bao);
			AcroFields form = stamp.getAcroFields();

			Set<String> fields = form.getFields().keySet();
			for (String key : fields) {
				String _key = ConvertToPdfName(key);
				if (data.containsKey(_key)) {
					if (!StringUtils.isEmpty(data.get(_key)) && !data.get(_key).toString().equals("null")) {
						if (form.getFieldType(_key) == AcroFields.DA_SIZE) {//图片区域
							try {
								Image image1 = Image.getInstance(String.valueOf(data.get(_key)));
								PdfContentByte overContent = stamp.getOverContent(1);
								List<AcroFields.FieldPosition> imgPosition = form.getFieldPositions(_key);
								float x = imgPosition.get(0).position.getLeft();
								float y = imgPosition.get(0).position.getBottom();
								image1.setAbsolutePosition(x, y);
								image1.scaleAbsolute(72, 30);
								overContent.addImage(image1);
							} catch (Exception e) {
								System.out.println("图片连接为空： " + String.valueOf(data.get(_key)));
							}
						} else {
							String _value = String.valueOf(data.get(_key));
							form.setField(_key, _value);
						}
					}
				}
				if (String.valueOf(_key.charAt(0)).equals("_")&&String.valueOf(_key.charAt(1)).equals("_")) { // 有双下划线的是CHECK类型 ,CHECK的ID 包含了该CHECK值 如：_QLLX_4
					String _mapKey = ConvertMapKey(_key);
					if (data.containsKey(_mapKey)) {
						if (!StringUtils.isEmpty(data.get(_mapKey)) && !data.get(_mapKey).toString().equals("null")) {
							 if(validateCheckValue(_key,String.valueOf(data.get(_mapKey))))
							        form.setField(_key, "√");//设置CHECKBOX
						}
					}
				}
			}
			stamp.setFormFlattening(true);
			stamp.close();
			reader.close();
			return bao;
		}
		
		// 表单1[0].#subform[0].djzqmc[0] 转成djzqmc wuzhu
		private static String ConvertToPdfName(String key) {
			String result = "";
			if (!StringUtils.isEmpty(key)) {
				int start = key.lastIndexOf(".") + 1;
				int end = key.length() - 3;
				result = key.substring(start, end);
			}
			return result;
		}
		//通过模板的CHECK Name验证是否包含该CHECK值__qllx__9__8 ,用双下划线将ID和值合在一起。格式后面的数字为CHECK可选值 wuzhu
		private static boolean validateCheckValue(String checkkey,String value)
		{
			String[] checkValues=ConvertCheckNameToArray(checkkey);
			 if(checkValues!=null&&checkValues.length>0)
			 {
				 for(String v:checkValues)
				 {
					 if(v.equals(value))
						 return true;
				 }
			 }
			 return false;
		}
		//__qllx__9__6__9以__为分隔符转成数组，数组的第二项为页面ID值，后面的数字为CHECK的可选值。wuzhu
		private static String[] ConvertCheckNameToArray(String key)
		{
			if(!StringUtils.isEmpty(key))
			{
				return key.split("__");//双下划线
			}
			return null;
		}
		//__qllx__9__8转成qllx wuzhu
		 private static String ConvertMapKey(String key)
				{
					String result="";
					if(!StringUtils.isEmpty(key))
					{
						int start=2;
						int end=key.indexOf("__", 2);
					 result=key.substring(start,end);
					}
					return result;
				}
		 /* 
		  * * 合並pdf文件 
		  * @param files  "e:\\1.pdf", "e:\\2.pdf" , 
		  * newfile 新文件的路径  e:\\temp.pdf
		*  lx
	 */  
	
		
		 public static boolean mergePdfFiles(List<String> files, String newfile) { 
			 boolean retValue = false;  
			 if(files==null||files.size()<1||newfile==null||"".equals(newfile)) {
				 return retValue;
			 } 
			 Document document = null;  
			 try {  
				 document = new Document(new PdfReader(files.get(0)).getPageSize(1));  
				 PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));  
				 document.open();  
				 for (int i = 0; i < files.size(); i++) {  
					 PdfReader reader = new PdfReader(files.get(i));  
					 int n = reader.getNumberOfPages();  
					 for (int j = 1; j <= n; j++) {  
						 document.newPage();  
						 PdfImportedPage page = copy.getImportedPage(reader, j);  
						 copy.addPage(page);  
					 }  
				 }  
				 retValue = true;  
			 } catch (Exception e) {  
				 e.printStackTrace();  
			 } finally {  
	   
				 document.close();  
			 }  
		       return retValue;  
		 }  
		 
///PDF 数据容器
 public class PDFDataContainer{
	 private String pdfTemplateName;//模板名称
	 private Map<String, Object> data;//填充数据

	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getPdfTemplateName() {
		return pdfTemplateName;
	}
	public void setPdfTemplateName(String pdfTemplateName) {
		this.pdfTemplateName = pdfTemplateName;
	}
 }
}
