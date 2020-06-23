package com.supermap.realestate.registration.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;

public class URLUtil {
	
	public static boolean  readContentFromPost(URL postUrl,String content,String tokenstr) throws ConnectException {
		// 打开连接
		boolean issuccess = false;
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			connection.setRequestProperty("Authorization", "Bearer " + tokenstr);
			connection.connect();
			//DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			String jsonpar = content.toString();
			out.write(jsonpar);
			out.flush(); 
			out.close();
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			InputStream is = null;
			int status = connection.getResponseCode();
			if(status>= HttpStatus.SC_BAD_REQUEST){  //此处一定要根据返回的状态码state来初始化输入流。如果为错误
			    is = connection.getErrorStream();
			}else{
			    is = connection.getInputStream();//如果正确
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is,"UTF-8"));
			String line = null;
			line = reader.readLine();
			if (line != null) {
				json = json.append(line);
//				String ds=new String(json.toString().getBytes("iso8859-1"),"UTF-8");
//				System.out.println("====================2222222222222222222222222===="+jsonObj.toString());
				jsonObj = JSONObject.fromObject(json.toString());
				System.out.println("===============================================");
    			System.out.println("调用短信接口返回的数据:"+jsonObj);
    			System.out.println("===============================================");
				Map map=jsonObj;
				/*String detailString ="";
				String Detailadd  ="";
				if(jsonObj.get("Detailadd")!=null){
					 Detailadd= null==jsonObj.getString("Detailadd")?"":jsonObj.getString("Detailadd");
				}
				else{
					 Detailadd = null == detailString?"":detailString;
				}
				jsonObj.put("Detailadd", URLDecoder.decode(URLDecoder.decode(Detailadd,"UTF-8"),"UTF-8"));*/
				issuccess = true;
			}
			reader.close();
			connection.disconnect();
			return true;
		}catch(ConnectException c){
			throw c;
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return issuccess;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static  Map sandJSONPOST(String postUrl, JSONObject jsonObject) throws IOException {
		JSONObject jsonObj = new JSONObject();
		Map<String,String> map = new HashMap<String, String>();
		String session = new SessionUtils("20.0.6.7","007565194-G1001","bdcdjwsbl","666666").getSession();
		String resultString = null;
        HttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(10000).setSocketTimeout(30000).build();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("az-session",session);
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(jsonObject.toString(), charSet);
        httpPost.setEntity(entity);
        try{
        	HttpResponse execute = httpClient.execute(httpPost);
        	if(execute.getStatusLine().getStatusCode() == 200){
        		resultString = EntityUtils.toString(execute.getEntity(), charSet);
        		jsonObj = JSONObject.fromObject(resultString);
        		List<List<String>> s = (List<List<String>>)jsonObj.get("rows");
        		if(s != null && s.size()>0){
        			map.put("QYMC", s.get(0).get(0));
        			map.put("TYSHXYDM", s.get(0).get(1));
        			map.put("FDDBR", s.get(0).get(2));
        			System.out.println("===============================================");
        			System.out.println("调用自治区工商局接口返回的数据:"+map);
        			System.out.println("===============================================");
        		}
        	}
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("===============================================");
			System.out.println("调用自治区工商局接口异常:"+e);
			System.out.println("===============================================");
        }finally{
        	if(httpPost != null){
        		httpPost.releaseConnection();
        	}
        }
		return map;
    }
	
	/**
	 * 验证个人身份信息接口实现
	 * @author liangc
	 * @date 2018-08-09 
	 * @param postUrl
	 * @param xm
	 * @param zjh
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unused", "static-access" })
	public static  Map sandForPersonXX(String postUrl,String xm,String zjh) throws IOException {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonPerson = new JSONObject();
		Map<String,String> map = new HashMap<String, String>();
		String session = new SessionUtils("20.0.6.7","007565194-G1001","bdcdjwsbl","666666").getSession();
		/*String resultString = null;
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setHeader("az-session",session);
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";*/
        String keycode = "110e19a0c14a7b4a4807c150dd4ce493";
        String personresult = ForPersonXX(xm,zjh,keycode,session);
        String name = "";
        String zjhm = "";
        String photo = "";
        String resultstatus ="";
        String resultecpstr ="";
        String errstr = "";
        if(!StringHelper.isEmpty(personresult)){
        	jsonPerson = jsonPerson.fromObject(personresult);
        	if(jsonPerson.containsKey("code")){
        		resultstatus = jsonPerson.getString("code");
        		map.put("code", resultstatus);
        	}
        	if(jsonPerson.containsKey("ecpstr")){
        		resultecpstr = jsonPerson.getString("ecpstr");
        		map.put("jkyccode", resultecpstr);
        	}
        }
        if(resultstatus.equals("01")){
        	name = jsonPerson.getString("name");
        	zjhm = jsonPerson.getString("idCardNo");
        	//photo = jsonPerson.getString("photo");
        }else if(resultstatus.equals("02")){
        	errstr = "查无此证";
        }else if(resultstatus.equals("03")){
        	errstr = "查询超时或查询异常";
        }else if(resultstatus.equals("04")){
        	errstr = "信息有误或非自治区户籍人口，无法通过自治区公安厅个人基本信息接口核验";
        }else if(resultstatus.equals("05")){
        	errstr = "自治区公安厅个人基本信息接口密匙错误";
        }else if(resultstatus.equals("06")){
        	errstr = "自治区公安厅个人基本信息接口暂停服务";
        }else if(resultstatus.equals("07")){
        	errstr = "自治区公安厅个人基本信息接口非开放查询时间段";
        }else if(resultstatus.equals("08")){
        	errstr = "无权访问自治区公安厅个人基本信息接口";
        }else if(resultstatus.equals("09")){
        	errstr = "自治区公安厅个人基本信息接口服务异常";
        }else if(resultecpstr.equals("java.lang.NullPointerException")||resultecpstr.equals("java.net.SocketTimeoutException: Read timed out")){
        	errstr = "自治区公安厅个人基本信息接口调用异常";
        }
        map.put("errstr", errstr);
        map.put("name", name);
        map.put("zjhm", zjhm);
        //map.put("photo", photo);

		return map;
    }
	
	/**
	 * 获取公安接口需要用到的验证码
	 * @author liangc
	 * @date 2018-08-09 15:30:30
	 * @param session
	 * @return
	 */
	public static String ForMIYAOWsdl(String session){
		// 开始调用接口
		Call call = null;
		String str = "";
		try {
			/*
			 *  要调用的接口地址
			 *  调用之前先在地址后面加上 '?wsdl' 然后在浏览器访问该地址是否能正常访问，
			 *  地址加完'?wsdl'如下:
			 *  http://localhost:80/shareplatform/services/ServicePublish?wsdl					 */
			String wsUrl = "http://20.0.6.7:6062/developer-api/sync/resources/89822217818050B00001-6/1/requests";
			Service service = new Service ();
			Hashtable<String,String> hearder = new Hashtable<String,String>();
			hearder.put("az-session",session);
			call = (Call) service.createCall();
			call.setProperty("HTTP-Request-Headers", hearder);
			call.setProperty(org.apache.axis.MessageContext.HTTP_TRANSPORT_VERSION, HTTPConstants.HEADER_PROTOCOL_V11);
			call.setTargetEndpointAddress(new java.net.URL(wsUrl));
			// 设置20秒超时
			call.setTimeout(new Integer("30000"));
			// 设置操作的名称  
			// identityQuery是调用的方法名
			call.setOperationName(new QName("http://publish.sinosoft.com", "register"));
			// 参数的类型
			call.addParameter("identifier", XMLType.XSD_STRING, ParameterMode.IN); 
			call.addParameter("deptname", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("SystemID", XMLType.XSD_STRING, ParameterMode.IN);
			// 返回的数据类型
			call.setReturnType(XMLType.XSD_STRING); 
			// 执行方法 , "22222" 是 identityQuery要传的参数
			str =  (String) call.invoke(new Object[] {"11450000007565194H","自治区国土厅","bdcdjxt"});
		} catch (Exception e) {
			System.out.println(e);
		}
		return str;
	}
	
	/**
	 * 获取公安接口个人信息数据
	 * @author liangc
	 * @date 2018-08-09 15:30:30
	 * @param xm
	 * @param zjh
	 * @param key
	 * @param session
	 * @return
	 */
	public static String ForPersonXX(String xm,String zjh,String key,String session){
		// 开始调用接口
		Call call = null;
		String str = "";
		Map ecpstr = new HashMap<String,String>();
		String jsonstr="";
		try {
			/*
			 *  要调用的接口地址
			 *  调用之前先在地址后面加上 '?wsdl' 然后在浏览器访问该地址是否能正常访问，
			 *  地址加完'?wsdl'如下:
			 *  http://localhost:80/shareplatform/services/ServicePublish?wsdl					 */
			//String wsUrl = "http://20.0.6.33:8080/shareplatform/services/ServicePublish";
			String wsUrl = "http://20.0.6.7:6062/developer-api/sync/resources/89822217818050B00001-6/1/requests";
			Service service = new Service ();
			Hashtable<String,String> hearder = new Hashtable<String,String>();
			hearder.put("az-session",session);
			call = (Call) service.createCall();
			call.setProperty("HTTP-Request-Headers", hearder);
			call.setProperty(org.apache.axis.MessageContext.HTTP_TRANSPORT_VERSION, HTTPConstants.HEADER_PROTOCOL_V11);
			call.setTargetEndpointAddress(new java.net.URL(wsUrl));
			// 设置20秒超时
			call.setTimeout(new Integer("30000"));
			// 设置操作的名称  
			// identityQuery是调用的方法名
			call.setOperationName(new QName("http://publish.sinosoft.com", "identityQuery"));
			// 参数的类型
			call.addParameter("idCardNo", XMLType.XSD_STRING, ParameterMode.IN); 
			//call.addParameter("name", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("key", XMLType.XSD_STRING, ParameterMode.IN);
			// 返回的数据类型
			call.setReturnType(XMLType.XSD_STRING); 
			// 执行方法 , "22222" 是 identityQuery要传的参数
			str =  (String) call.invoke(new Object[] {zjh,key});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			if(e.toString().equals("java.lang.NullPointerException")){
				ecpstr.put("ecpstr", "java.lang.NullPointerException");
			}else if(e.toString().equals("java.net.SocketTimeoutException: Read timed out")){
				ecpstr.put("ecpstr", "java.net.SocketTimeoutException: Read timed out");
			}
			JSONObject jsonObject = JSONObject.fromObject(ecpstr);
			jsonstr = jsonObject.toString();
			System.out.println("===============================================");
			System.out.println("调用公安厅接口异常:"+jsonstr);
			System.out.println("===============================================");
			return jsonstr;
		}
		System.out.println("===============================================");
		System.out.println("调用公安厅接口正常:"+str);
		System.out.println("===============================================");
		return str;
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	public static Map  sandForPersonXX_GJ(String strpostUrl,String content) throws ConnectException, MalformedURLException {
			// 打开连接
		    URL postUrl = new URL(strpostUrl);
			HttpURLConnection connection;
			Map<String,String> map = new HashMap<String, String>();
			try {
				connection = (HttpURLConnection) postUrl.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
				connection.connect();
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				String jsonpar = content.toString();
				out.write(jsonpar);
				out.flush(); 
				out.close();
				JSONObject jsonObj = new JSONObject();
				StringBuilder json = new StringBuilder();
				InputStream is = null;
				int status = connection.getResponseCode();
				if(status>= HttpStatus.SC_BAD_REQUEST){  //此处一定要根据返回的状态码state来初始化输入流。如果为错误
				    is = connection.getErrorStream();
				}else{
				    is = connection.getInputStream();//如果正确
				}
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is,"UTF-8"));
				String line = null;
				line = reader.readLine();
				if (line != null) {
					json = json.append(line);
					jsonObj = JSONObject.fromObject(json.toString());
					System.out.println("===============================================");
	    			System.out.println("调用公安部接口返回的数据:"+jsonObj);
	    			System.out.println("===============================================");
	    			String data = jsonObj.getString("data");
	    			String errstr = jsonObj.getString("errmsg");
	    			JSONArray jsonarray = JSONArray.fromObject(data);
	    			List<Map> list = JSONArray.toList(jsonarray,Map.class);
					map.put("name", list.get(8).get("XM").toString()); 
        			map.put("zjhm", list.get(3).get("GMSFHM").toString());
        			map.put("errstr", errstr);
				}
				reader.close();
				connection.disconnect();
			}catch(ConnectException c){
				throw c;
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			return map;
		}
	
	
	@SuppressWarnings({ "rawtypes", "unused", "static-access" })
	public static Map  sandJSONPOST_GJGS(String strpostUrl,String content) throws ConnectException, MalformedURLException {
		// 打开连接
	    URL postUrl = new URL(strpostUrl);
		HttpURLConnection connection;
		Map<String,String> map = new HashMap<String, String>();
		try {
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			String jsonpar = content.toString();
			out.write(jsonpar);
			out.flush(); 
			out.close();
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			InputStream is = null;
			int status = connection.getResponseCode();
			if(status>= HttpStatus.SC_BAD_REQUEST){  //此处一定要根据返回的状态码state来初始化输入流。如果为错误
			    is = connection.getErrorStream();
			}else{
			    is = connection.getInputStream();//如果正确
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is,"UTF-8"));
			String line = null;
			line = reader.readLine();
			if (line != null) {
				json = json.append(line);
				jsonObj = JSONObject.fromObject(json.toString());
				System.out.println("===============================================");
    			System.out.println("调用国家工商总局接口返回的数据:"+jsonObj);
    			System.out.println("===============================================");
    			//net.sf.json.JSONArray jsonObj1 =jsonObj.getJSONArray("data");
    			//String data = "[{\"apprdate\":1529596800000,\"dom\":\"成都市成华区成华大道龙潭路1号5栋1层101号102号\",\"entname\":\"中国农业银行股份有限 公司成都龙潭支行\",\"enttypeCn\":\"股份有限公司分公司(上市、国有控股)\",\"estdate\":516211200000,\"name\":\"姜扬\",\"opfrom\":516211200000,\"opscope\":\"吸收公众存款；发放短期、中期和长期贷款；办理国内外结算；办理票据承兑与贴现；发行金融债券；代理发行、代 理兑付、承销政府债券；买卖政府债券、金融债券；从事同业拆借；从事银行卡业务；提供信用证服务及担保；代理收付款项及代理保险业务；按规定经中国银行业监督管理委员会批准或有权上级行授权开办的其他业务。（依法须经批准的项目，经相关部门批准后方可开展经营活动）。\",\"opto\":null,\"regcap\":null,\"regcapcurCn\":\"人民币\",\"regno\":\"510100000074247\",\"regorgCn\":\"成都市工商行政管理 局\",\"regstateCn\":\"存续（在营、开业、在册）\",\"sExtNodenum\":\"510000\",\"uniscid\":\"91510100743619867A\"}]";
    			String data = jsonObj.getString("data");
    			String errstr = jsonObj.getString("errmsg");
    			JSONArray jsonarray = JSONArray.fromObject(data);
    			List<Map> list = JSONArray.toList(jsonarray,Map.class);
				map.put("QYMC", list.get(0).get("entname").toString()); 
    			map.put("TYSHXYDM", list.get(0).get("uniscid").toString());
    			map.put("FDDBR", list.get(0).get("name").toString());
    			map.put("errstr", errstr);
			}
			reader.close();
			connection.disconnect();
		}catch(ConnectException c){
			throw c;
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	
	  public static String readContentFromPost2(URL postUrl, String jmStr){
		    try {
		      HttpURLConnection connection = (HttpURLConnection)postUrl.openConnection();
		      connection.setDoOutput(true);
		      connection.setDoInput(true);

		      connection.setUseCaches(false);
		      connection.setInstanceFollowRedirects(true);
		      connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");
		      connection.connect();
		      DataOutputStream out = new DataOutputStream(
		        connection.getOutputStream());
		      out.writeBytes(jmStr);
		      out.flush();
		      out.close();
		      JSONObject jsonObj = new JSONObject();
		      StringBuilder json = new StringBuilder();
		      BufferedReader reader = new BufferedReader(
		        new InputStreamReader(connection.getInputStream(),"UTF-8"));
		      String line = null;
		      line = reader.readLine();
		      if (line != null) {
		        json = json.append(line);
		        jsonObj = JSONObject.fromObject(json.toString());
		        return json.toString();
		      }
		      reader.close();
		      connection.disconnect();
		      return json.toString();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    return null;
		  }
	public static String startGet(String urls){
        BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        try {
            //GET请求直接在链接后面拼上请求参数
//            String mPath = path + "?";
//            for(String key:mData.keySet()){
//                mPath += key + "=" + mData.get(key) + "&";
//            }
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接服务器  
            conn.connect();  
        	JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			line = reader.readLine();
			if (line != null) {
				json = json.append(line);
//				String ds=new String(json.toString().getBytes("iso8859-1"),"UTF-8");
//				System.out.println("====================2222222222222222222222222===="+jsonObj.toString());
				jsonObj = JSONObject.fromObject(json.toString());
//				Map map=jsonObj;
				String Detailadd= jsonObj.getString("data");
//				jsonObj.put("Detailadd", URLDecoder.decode(URLDecoder.decode(Detailadd,"UTF-8"),"UTF-8"));
				return Detailadd;
			}
			reader.close();
			conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
      
    }
	/**
	 * linux系统文件路径符号是“/”，windows是“\”，根据所在系统替换路径符号
	 * @param path
	 * @return
	 */
	public static String PathReplaceSymbol(String path){
		String symbol=File.separator;
		if(path.indexOf("\\\\")!=-1){
			path=path.replaceAll("\\\\", symbol);
		}
		if(path.indexOf("\\")!=-1){
			path=path.replace("\\", symbol);
		}
		if(path.indexOf("//")!=-1){
			path=path.replaceAll("//",symbol);
		}
		return path;
	}
	
	/**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     * 
     * @param actionUrl 访问的服务器URL
     * @param params 普通参数
     * @param files 文件参数
     * @return
     * @throws IOException
     */
    public static void post(String actionUrl, Map<String, String> params, Map<String, File> files) throws IOException
    {

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        InputStream in = null;
        // 发送文件数据
        if (files != null)
        {
            for (Map.Entry<String, File> file : files.entrySet())
            {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                // name是post中传参的键 filename是文件的名称
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1)
                {
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
            if (res == 200)
            {
                in = conn.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                while ((ch = in.read()) != -1)
                {
                    sb2.append((char) ch);
                }
            }
            outStream.close();
            conn.disconnect();
        }
        // return in.toString();
    }
private static byte[] input2byte(File file) throws IOException {
	
	 byte[] buffer = null;  
     try  
     {  
         FileInputStream fis = new FileInputStream(file);  
         ByteArrayOutputStream bos = new ByteArrayOutputStream();  
         byte[] b = new byte[1024];  
         int n;  
         while ((n = fis.read(b)) != -1)  
         {  
             bos.write(b, 0, n);  
         }  
         fis.close();  
         bos.close();  
         buffer = bos.toByteArray();  
     }  
      
     catch (Exception e)  
     {  
         e.printStackTrace();  
     }  
     return buffer;  
}
/**
 * 同步之前用于判断IP、端口、数据库是否是连通的
 * @param commonDao
 * @return	将不连通的ip地址的市县名称传递回去
 */
public static String iptest(CommonDao commonDao){
	String sql = "select * from xzqh where IP is not null and ip<>':8080'";
	List list = commonDao.getDataListByFullSql(sql);
	String iperrname = "";
	Map<String,String> map2 = new HashMap<String,String>();
	if(list.size()==0){return null;}
	else{
		for(int i = 0;i<list.size();i++){		//遍历每一个ip
			Map map = (Map)list.get(i);			
			String ip = map.get("IP").toString();		//获取IP加端口
			String host = ip.split(":")[0];				//IP
			int port = Integer.parseInt(ip.split(":")[1]);	//端口
			int flag =ipporttest(host, port);				//判断IP端口是否连通
			System.out.println(map);
			if(flag!=2){
				iperrname=iperrname+
						map.get("MC").toString()+
						"、";}
			else{/*
				String ipString="http://"+ip+"/sharesearch/app/search/querytestEX";
				try {
					String list2 ="";// SmOthersService.sendGetRequest(ipString, map2);		//判断数据库是否连通
					if(list2 =="[]"){						
						iperrname+=map.get("MC").toString()+"、";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			*/}
		}
		if(iperrname==""){return iperrname;}
		else{
			return iperrname.substring(0,iperrname.length()-1);}
		}
}
public static int ipporttest(String host, int port) {
	int flag=0;
	int timeout = 3000;
	boolean status = false;
	try {
		status = InetAddress.getByName(host).isReachable(timeout);
		if(status){
			flag++;
		}
	} catch (UnknownHostException e1) {
		// TODO Auto-generated catch block
		flag=0;
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		flag=0;
	}
	
	if(flag==1){
	// TODO Auto-generated method stub
	Socket socket = new Socket();
    try {
        socket.connect(new InetSocketAddress(host, port));
        flag++;
    } catch (IOException e) {
    	flag=1;
    } finally {
        try {
            socket.close();
        } catch (IOException e) {
        	flag=1;
        }
    }
    }
	return flag;
}
}
