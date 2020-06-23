package com.supermap.wisdombusiness.workflow.service.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;

public class Http {

	public static List<Map> postFile2(MultipartFile file, String fileName,
			String filePath) throws ParseException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<Map> Result = new ArrayList<Map>();
		try {
			// String uploadserver =
			// ;
			String uploadserver =ConfigHelper.getNameByValue("uploadserver");// "http://localhost:8080/file/app/file/upload";
			uploadserver=uploadserver+"file/upload";
			CommonsMultipartFile cf = (CommonsMultipartFile) file;
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File f = fi.getStoreLocation();
			// 要上传的文件的路径
			// 把一个普通参数和文件上传给下面这个地址 是一个servlet
			HttpPost httpPost = new HttpPost(uploadserver);
			// 把文件转换成流对象FileBody
			FileBody bin = new FileBody(f);
			StringBody uploadFileName = new StringBody(fileName,
					ContentType.create("text/plain", "UTF-8"));
			StringBody uploadPath = new StringBody(filePath,
					ContentType.create("text/plain", "UTF-8"));
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("file", bin)
					// uploadFile对应服务端类的同名属性<File类型>
					.addPart("uploadFileName", uploadFileName)
					// uploadFileName对应服务端类的同名属性<String类型>
					.addPart("uploadPath", uploadPath)
					.setCharset(CharsetUtils.get("UTF-8")).build();

			httpPost.setEntity(reqEntity);

			// System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				if (HttpStatus.SC_OK == response.getStatusLine()
						.getStatusCode()) {
					//System.out
						//	.println("----------------------------------------");
					// 打印响应状态
					//System.out.println(response.getStatusLine());
					// 获取响应对象
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						// 打印响应长度
						System.out.println("Response content length: "
								+ resEntity.getContentLength());
						String result = EntityUtils.toString(resEntity,
								Charset.forName("UTF-8"));
						JSONArray json=JSONArray.fromObject(result);
						if(json!=null&&json.size()>0){
							for(int i=0;i<json.size();i++){
								Map map=new HashMap();
								JSONObject obj=json.getJSONObject(i);
								if(obj!=null){
									map.put("filepath", obj.get("filepath"));
									map.put("filename", obj.get("filename"));
									Result.add(map);
								}
							}
						}
					}
					// 销毁
					EntityUtils.consume(resEntity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return Result;
	}

	/**
	 * 根据Wfi_materdata上传文件
	 * @param file
	 * @param Wfi_MaterData
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static List<Map> postFileByWfi_materdata(File file ,Wfi_MaterData Wfi_MaterData) throws ParseException, IOException {
		FileBody bin = new FileBody(file);
		String fileName =Wfi_MaterData.getFile_Name();
		String filePath=Wfi_MaterData.getPath();
		String disksymbol=Wfi_MaterData.getDisc();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<Map> Result = new ArrayList<Map>();
		try {
			// String uploadserver =
			// ;
			String uploadserver =ConfigHelper.getNameByValue("uploadserver");// "http://localhost:8080/file/app/file/upload";
			uploadserver=uploadserver+"file/upload";
			// 要上传的文件的路径
			// 把一个普通参数和文件上传给下面这个地址 是一个servlet
			HttpPost httpPost = new HttpPost(uploadserver);
			// 把文件转换成流对象FileBody
			StringBody uploadFileName = new StringBody(fileName,
					ContentType.create("text/plain", "UTF-8"));
			StringBody uploadPath = new StringBody(filePath,
					ContentType.create("text/plain", "UTF-8"));
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("file", bin)
					// uploadFile对应服务端类的同名属性<File类型>
					.addPart("uploadFileName", uploadFileName)
					// uploadFileName对应服务端类的同名属性<String类型>
					.addPart("uploadPath", uploadPath)
					.addPart("disksymbol", new StringBody(disksymbol,ContentType.create("text/plain", "UTF-8")))
					.setCharset(CharsetUtils.get("UTF-8")).build();

			httpPost.setEntity(reqEntity);
			httpClient.execute(httpPost);
		} finally {
			httpClient.close();
		}
		return Result;
	}
	
	
	public static List<Map> postFile(File file, String fileName,
			String filePath) throws ParseException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<Map> Result = new ArrayList<Map>();
		try {
			// String uploadserver =
			// ;
			String uploadserver =ConfigHelper.getNameByValue("uploadserver");// "http://localhost:8080/file/app/file/upload";
			uploadserver=uploadserver+"file/upload";
		
			// 要上传的文件的路径
			// 把一个普通参数和文件上传给下面这个地址 是一个servlet
			HttpPost httpPost = new HttpPost(uploadserver);
			// 把文件转换成流对象FileBody
			FileBody bin = new FileBody(file);
			StringBody uploadFileName = new StringBody(fileName,
					ContentType.create("text/plain", "UTF-8"));
			StringBody uploadPath = new StringBody(filePath,
					ContentType.create("text/plain", "UTF-8"));
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("file", bin)
					// uploadFile对应服务端类的同名属性<File类型>
					.addPart("uploadFileName", uploadFileName)
					// uploadFileName对应服务端类的同名属性<String类型>
					.addPart("uploadPath", uploadPath)
					.setCharset(CharsetUtils.get("UTF-8")).build();

			httpPost.setEntity(reqEntity);

			// System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				if (HttpStatus.SC_OK == response.getStatusLine()
						.getStatusCode()) {
					//System.out
						//	.println("----------------------------------------");
					// 打印响应状态
					//System.out.println(response.getStatusLine());
					// 获取响应对象
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						// 打印响应长度
						System.out.println("Response content length: "
								+ resEntity.getContentLength());
						String result = EntityUtils.toString(resEntity,
								Charset.forName("UTF-8"));
						JSONArray json=JSONArray.fromObject(result);
						if(json!=null&&json.size()>0){
							for(int i=0;i<json.size();i++){
								Map map=new HashMap();
								JSONObject obj=json.getJSONObject(i);
								if(obj!=null){
									map.put("filepath", obj.get("filepath"));
									map.put("filename", obj.get("filename"));
									Result.add(map);
								}
							}
						}
					}
					// 销毁
					EntityUtils.consume(resEntity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		return Result;
	}
	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param destFileName
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] getFile(String path, String FileName)
			throws ClientProtocolException, IOException {
		// 生成一个httpclient对象
		byte[] buff=null;
		String uploadserver =ConfigHelper.getNameByValue("uploadserver");
		if(path!=null){
			uploadserver=uploadserver+"file/down?path="+URLEncoder.encode(path,"utf-8")+"&filename="+URLEncoder.encode(FileName,"utf-8");
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(uploadserver);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();
			buff=input2byte(in);
			in.close();
			httpclient.close();
			return buff;
		}
		else{
			return null;
		}
	
	}
	 private static  byte[] input2byte(InputStream inStream)  
	            throws IOException {  
	        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
	        byte[] buff = new byte[100];  
	        int rc = 0;  
	        while ((rc = inStream.read(buff, 0, 100)) > 0) {  
	            swapStream.write(buff, 0, rc);  
	        }  
	        byte[] in2b = swapStream.toByteArray();  
	        return in2b;  
	    }  
}
