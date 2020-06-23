package com.supermap.realestate.registration.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.http.util.TextUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpRequest {

	public static final String POST = "POST";
	public static final String GET = "GET";

	/**
	 * 获取url资源
	 * 
	 * @作者 Ouzr
	 * @创建时间 2016年5月17日下午4:30:43
	 * @param url
	 * @param data
	 * @return String
	 * @throws Exception
	 */

	public static String getUrlResource(String urlStr,Map<String, String> data, String requestType) throws Exception {
		String paramJson = "";// 参数
		if (data != null) {
			paramJson = JSONObject.toJSONString(data);// 参数
		}

		URL url = null;
		HttpURLConnection http = null;
		// 实例一个URL资源
		url = new URL(urlStr);
		http = (HttpURLConnection) url.openConnection();
		http.setDoInput(true);
		http.setDoOutput(true);
		http.setUseCaches(false);
		http.setConnectTimeout(50000);// 设置连接超时
		http.setReadTimeout(50000);// 设置读取超时
		// http.setRequestMethod(requestType);
		// http.setRequestProperty("Content-Type", "application/json");
		http.setRequestProperty("Accept-Charset", "utf-8");
		http.setRequestProperty("contentType", "utf-8");
		http.connect();
		OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(),"utf-8");
		osw.write(paramJson);
		osw.flush();
		osw.close();
		if (http.getResponseCode() != 200) {
			throw new IOException(http.getResponseMessage());
		}

		// 将返回的值存入到String中
		BufferedReader brd = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
		StringBuilder resultStr = new StringBuilder();
		String line;
		while ((line = brd.readLine()) != null) {
			resultStr.append(line);
		}

		brd.close();
		http.disconnect();
		return resultStr.toString();
	}

	public static String getUrlResource(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		try {
			HttpURLConnection connet = (HttpURLConnection) url.openConnection();
			connet.setRequestProperty("Accept-Charset", "utf-8");
			connet.setRequestProperty("contentType", "utf-8");
			if (connet.getResponseCode() != 200) {
				throw new IOException(connet.getResponseMessage());
			}
			/* 将返回的值存入到String中 */
			BufferedReader brd = new BufferedReader(new InputStreamReader(
					connet.getInputStream(), "utf-8"));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = brd.readLine()) != null) {
				result.append(line);
			}
			brd.close();
			connet.disconnect();

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String TsfjPost(String actionUrl, Map<String, String> headParams,  
            Map<String, String> params,  Map<String, File> files) throws IOException {  
        String BOUNDARY = java.util.UUID.randomUUID().toString();  
        String PREFIX = "--", LINEND = "\r\n";  
        String MULTIPART_FROM_DATA = "multipart/form-data";  
        String CHARSET = "UTF-8";  
  
        URL uri = new URL(actionUrl);  
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();  
        conn.setReadTimeout(30 * 1000); // 缓存的最长时间  
        conn.setDoInput(true);// 允许输入  
        conn.setDoOutput(true);// 允许输出  
        conn.setUseCaches(false); // 不允许使用缓存  
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("connection", "keep-alive");  
        conn.setRequestProperty("Charsert", "UTF-8");  
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA  
                + ";boundary=" + BOUNDARY);  
        if(headParams!=null){  
            for(String key : headParams.keySet()){  
                conn.setRequestProperty(key, headParams.get(key));  
            }  
        }  
        StringBuilder sb = new StringBuilder();  
        if (params!=null) {  
            // 首先组拼文本类型的参数  
            for (Map.Entry<String, String> entry : params.entrySet()) {  
                sb.append(PREFIX);  
                sb.append(BOUNDARY);  
                sb.append(LINEND);  
                sb.append("Content-Disposition: form-data; name=\""  
                        + entry.getKey() + "\"" + LINEND);  
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);  
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);  
                sb.append(LINEND);  
                sb.append(entry.getValue());  
                sb.append(LINEND);  
            }  
              
        }  
          
        DataOutputStream outStream = new DataOutputStream(   conn.getOutputStream());  
        if (!TextUtils.isEmpty(sb.toString())) {  
            outStream.write(sb.toString().getBytes());  
        }  
          
        // 发送文件数据  
        if (files != null)  
            for (Map.Entry<String, File> file : files.entrySet()) {  
                StringBuilder sb1 = new StringBuilder();  
                sb1.append(PREFIX);  
                sb1.append(BOUNDARY);  
                sb1.append(LINEND);  
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""  
                        + file.getKey() + "\"" + LINEND);  
                sb1.append("Content-Type: application/octet-stream; charset="  
                        + CHARSET + LINEND);  
                sb1.append(LINEND);  
                outStream.write(sb1.toString().getBytes()); 
                InputStream is = new FileInputStream(file.getValue()+"\\"+params.get("uploadFileName"));  
                byte[] buffer = new byte[1024];  
                int len = 0;  
                while ((len = is.read(buffer)) != -1) {  
                    outStream.write(buffer, 0, len);  
                }  
                is.close();  
                outStream.write(LINEND.getBytes());  
            }  
  
        // 请求结束标志  
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();  
        outStream.write(end_data);  
        outStream.flush();  
        // 得到响应码  
        int res = conn.getResponseCode();  
        InputStream in = conn.getInputStream();  
        if (res == 200) {  
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));    
                   StringBuffer buffer = new StringBuffer();    
                 String line = "";    
             while ((line = bufferedReader.readLine()) != null){    
                   buffer.append(line);    
             }    
  
            return buffer.toString();  
        }  
        outStream.close();  
        conn.disconnect();  
        return in.toString();  
  
    }  
	
	public static File download(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close();  
        return sf;  
    }   
	 
	
	
	
	
	
	
	
	
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, Map<String, String> param) {
		String paramStr = getParam(param);
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + paramStr;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("contentType", "utf-8");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, Map<String, String> param) {
		String paramStr = getParam(param);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Charset", "utf-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(paramStr);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	private static String getParam(Map<String, String> paramMap) {
		String paramStr = "";
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			try {
				paramStr += entry.getKey() + "="
						+ URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (paramStr.endsWith("&")) {
			paramStr = paramStr.substring(0, paramStr.length() - 1);
		}
		return paramStr;
	}

	/**
	 * 发送WSDL请求，并获取返回值
	 * 
	 * @param wsdlinfo wsdl请求信息
	 * @param params 请求参数信息
	 * @return 请求结果
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public static Object sendWSDL(WSDLInfo wsdlinfo, List<ParamInfo> params) throws Exception {
		// 返回值
		Object return_obj = null;
		Service service = new Service();
		Call call = (Call) service.createCall();
		// 用户名密码验证
		call.setUsername(wsdlinfo.getUser());
		call.setPassword(wsdlinfo.getPassWord());
		// call.getMessageContext().setUsername(user);
		// call.getMessageContext().setPassword(pwd);

		call.setTargetEndpointAddress(wsdlinfo.getUrl());
		// a：首行targetNamespace值 b：调用方法名
		call.setOperationName(new QName(wsdlinfo.getTargetNamespace(), wsdlinfo
				.getMethodName()));
		call.setUseSOAPAction(true);
		// 所调用方法里面的wsdl:input wsaw:Action值
		call.setSOAPActionURI(wsdlinfo.getSoapAction());

		// 设定输入参数和输入值
		List<Object> list_value = new ArrayList<Object>();
		if (params != null && params.size() > 0) {
			for (ParamInfo param : params) {
				// a：首行targetNamespace值 b：参数名，必须与.net设置的参数名相同
				call.addParameter(new QName(wsdlinfo.getTargetNamespace(),
						param.getParamName()), param.getParamType(), param
						.getParamMode());// 接口的参数
				list_value.add(param.getParamValue());
			}
		}
		Object[] objs = list_value.toArray();
		// 设置返回类型
		call.setReturnType(wsdlinfo.getReturnType());
		// 给方法传递参数，并且调用方法
		return_obj = call.invoke(objs);
		return return_obj;
	}
	
	public static class ParamInfo {
		private QName paramType = org.apache.axis.encoding.XMLType.XSD_STRING;
		private javax.xml.rpc.ParameterMode paramMode = javax.xml.rpc.ParameterMode.IN;
		private String paramName = "";
		private Object paramValue = null;

		public javax.xml.rpc.ParameterMode getParamMode() {
			return paramMode;
		}

		public void setParamMode(javax.xml.rpc.ParameterMode paramMode) {
			this.paramMode = paramMode;
		}

		public QName getParamType() {
			return paramType;
		}

		public void setParamType(QName paramType) {
			this.paramType = paramType;
		}

		public String getParamName() {
			return paramName;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

		public Object getParamValue() {
			return paramValue;
		}

		public void setParamValue(Object paramValue) {
			this.paramValue = paramValue;
		}
	}

	public static class WSDLInfo {
		private String url = "";
		private String user = "";
		private String passWord = "";
		private String soapAction = "";
		private String targetNamespace = "";
		private String methodName = "";
		private QName returnType = org.apache.axis.encoding.XMLType.XSD_STRING;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPassWord() {
			return passWord;
		}

		public void setPassWord(String passWord) {
			this.passWord = passWord;
		}

		public String getSoapAction() {
			return soapAction;
		}

		public void setSoapAction(String soapAction) {
			this.soapAction = soapAction;
		}

		public String getTargetNamespace() {
			return targetNamespace;
		}

		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public QName getReturnType() {
			return returnType;
		}

		public void setReturnType(QName returnType) {
			this.returnType = returnType;
		}
	}

	/**      
     * 描述:获取 post 请求的 byte[] 数组
     * @param request
     * @return
     * @throws IOException      
     */
     static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength<0){
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**      
     * 描述:获取 post 请求内容
     * @param request
     * @return
     * @throws IOException      
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }
}
