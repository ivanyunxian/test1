package com.supermap.realestate.registration.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.huawei.eidc.slee.security.Base64;
import com.huawei.eidc.slee.security.MD5;
import com.supermap.realestate.registration.model.Sms.Client;
import com.supermap.realestate.registration.model.Sms.Sms;
import com.supermap.realestate.registration.model.Sms.SmsMessage;
import com.supermap.realestate.registration.model.Sms.Smsinfo;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * 调用移动Sms服务发送短信
 * @author 胡加红
 *
 */
@Component("SafeHttpSms")
@Scope("prototype")
public class SafeHttpSms {

	@Autowired
    private CommonDao commonDao;
	
	 private static final String KEY = "937CF2988EE6FC1B";
	 private String m_project_id="";
	 private String m_smsIp = "";
	 public SafeHttpSms(){
		 
	 }
	 @SuppressWarnings("rawtypes")
	private SmsMessage getSmsMessage(){
		 StringBuilder sb = new StringBuilder();
		 sb.append(" SELECT a.ywlsh,a.project_id,a.xmmc,a.xmbh,a.djlx,a.qllx,b.sqrxm,b.lxdh FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_sqr b ON a.xmbh=b.xmbh ");
		 sb.append(" WHERE  b.sqrlb is not null ");
		 sb.append(" AND a.project_id='").append(this.m_project_id).append("'");
		 SmsMessage sms = new SmsMessage();
		 try{
			 List<Map> sqrxx = commonDao.getDataListByFullSql(sb.toString());
			 String lxdh="",ywlsh="",project_id="",xmbh="",djlx="",qllx="",dxnr="",phone="";
			 if(sqrxx != null && sqrxx.size()>0){
				 for(Map map: sqrxx){					 
					 if(map.get("LXDH")!=null && !map.get("LXDH").toString().equals("") 
					 && map.get("LXDH").toString().length() >= 11){//只能给手机发短信
						 if(map.get("LXDH").toString().contains("-")||map.get("LXDH").toString().contains("——")){
							 //电话号码中间有横杠就认为是座机
							 continue;
						 }else{
							 lxdh += map.get("LXDH").toString()+",";
							 phone = map.get("LXDH").toString();
						 }
					 }
					 if(map.get("YWLSH")!=null && !map.get("YWLSH").toString().equals("")){
						 ywlsh=map.get("YWLSH").toString();
					 } 
					 if(map.get("PROJECT_ID")!=null && !map.get("PROJECT_ID").toString().equals("")){
						 project_id=map.get("PROJECT_ID").toString();
					 } 
					 if(map.get("XMBH")!=null && !map.get("XMBH").toString().equals("")){
						 xmbh=map.get("XMBH").toString();
					 } 
					 if(map.get("DJLX")!=null && !map.get("DJLX").toString().equals("")){
						 djlx=map.get("DJLX").toString();
					 } 
					 if(map.get("QLLX")!=null && !map.get("QLLX").toString().equals("")){
						 qllx=map.get("QLLX").toString();
					 } 
					if(djlx.equals("100") || djlx.equals("200") || djlx.equals("300") || djlx.equals("700"))
				     dxnr= "您好，您申请的不动产登记业务已办结。请您于24小时后携带身份证及受理单（单号："+ywlsh+"）缴费领证。祝您愉快。（市不动产登记中心）";
				 }
			 }
			 Smsinfo sms_info = new Smsinfo();
			 if(!lxdh.equals("") ){
				 sms_info.setPhone(lxdh.substring(0, lxdh.length()-1));//移除逗号
				 sms_info.setContent("您好，您申请的不动产登记业务已办结。请您于24小时后携带身份证及受理单（单号："+ywlsh+"）缴费领证。祝您愉快。（市不动产登记中心）");
				 sms_info.setSsid("");
			 }
			 Client client = new Client();
			 client.setId("MAS分配编号");
			 client.setPwd("MAS分配密钥");
			 client.setServiceid("短信代码扩展码");
			 Sms _sms = new Sms();
			 _sms.setClient(client);
			 _sms.setSms_info(sms_info);
			 _sms.setVer("2.0.0");
			 sms.setSms(_sms);
			 sms.setVer("2.0.0");			 
		 }
		 catch(Exception ex){
			 sms=null;
		 }
		 return sms;
	 }
	 
	 private String createXML(SmsMessage msg) {
		// 创建xml操作对象
		XStream xstream = new XStream(new DomDriver("utf8"));
		xstream.processAnnotations(SmsMessage.class);
		String top = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		
		xstream.alias("svc_init", SmsMessage.class);
		xstream.alias("sms",Sms.class);
		xstream.alias("client",Client.class);
		xstream.alias("sms_info",Smsinfo.class);
	
		// 启用Annotation
		xstream.autodetectAnnotations(true);
		String xmlStr = xstream.toXML(msg);
		xmlStr = top + xmlStr;
		String tempPath = GetProperties.getConstValueByKey("xmlPath");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(new Date());
		String fileName = m_project_id + "-" + dateStr + ".xml";
		File _dir = new File(tempPath);
		if (!_dir.exists()) {
		    _dir.mkdirs();
		}
		File file = new File(_dir, fileName);
		if (file.exists()) {
		    file.delete();
		}
		try {
		    file.createNewFile();
		    OutputStreamWriter output = new OutputStreamWriter(
			    new FileOutputStream(file), "utf-8");

		    output.write(xmlStr.replaceAll("__", "_"));
		    output.close();
		    return file.getPath();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
    }
	 public  String SendSMSMsg(String project_id,String smsIp) throws IOException{
		 this.m_project_id = project_id;
		 this.m_smsIp = "http://218.204.142.67:18080";
		SmsMessage sms = getSmsMessage();
		String m_fileName = createXML(sms);
	//String s = FileUtils.readFileToString(new File("D:\\1.xml"), "UTF-8");
		String s = FileUtils.readFileToString(new File(m_fileName), "UTF-8");
		String req = encryptReponseMsg(s);
		
		System.out.println(req);
		
		String result = "";//postXMLSendSMSRequest("http://"+m_smsIp+"/sjb/SafeHttpSendSMSService",req);
		
		System.out.println(result);
		result = decryptRequestMsg(result);
		System.out.println(decryptRequestMsg(result));
		return result;
	}
	 private  String postXMLSendSMSRequest(String servletUrl , String content) 
	    {
	        String result = null;  
	      
	        BufferedReader br = null;
	        OutputStreamWriter out = null; 
	        HttpURLConnection con = null;
	        
	        try
	        {
	            URL url= new URL(servletUrl);  
	            
	            con=(HttpURLConnection)url.openConnection();   
	            con.setDoOutput(true);       
	            con.setRequestMethod("POST");
	            
	            out=new OutputStreamWriter(con.getOutputStream(),"UTF-8");    
	            
	            out.write(content);
	            
	            out.flush();     
	            
	            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
	            
	            String line = null;
	            
	            StringBuilder sb = new StringBuilder();
	            
	            while((line = br.readLine())!=null)
	            {
	                sb.append(line);
	            }
	            
	            result = sb.toString();
	            
	            System.out.println(result);
	        }
	        catch(IOException e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            if(br != null)
	            {    
	                try
	                {
	                    br.close();
	                }
	                catch (IOException e)
	                {
	                    e.printStackTrace();
	                }
	            }
	            
	            if(out != null)
	            {    
	                try
	                {
	                    out.close();
	                }
	                catch (IOException e)
	                {
	                    e.printStackTrace();
	                }
	            }
	            
	            if(con != null)
	            {    
	                con.disconnect();
	                con = null;
	            }    
	        }    
	        
	        return result;
	    }

	 private  String encryptReponseMsg(String str) {
	     byte[] crypted = null;
	     System.out.println("加密前的消息体：");
	     System.out.println(str);
	     
	     try {
	         String md5str = MD5.md5(str);
	         System.out.println("MD5后的签名：");
	         System.out.println(md5str);
	         
	         SecretKeySpec skey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
	         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	         cipher.init(Cipher.ENCRYPT_MODE, skey);
	         
	         String enStr = md5str + str;
	         crypted = cipher.doFinal(enStr.getBytes("UTF-8"));
	         
	         System.out.println("AES加密后的字符数组：");
	     } catch (Exception e) {
	         System.out.println(e.toString());
	     }
	     
	     String body = new String(Base64.encode(crypted));
	     System.out.println("BASE64后的字符串：");
	     System.out.println(body);
	     
	     return body;
	 }

	 private  String decryptRequestMsg(String input) {
	     byte[] output = null;
	     String body = null;
	     
	     System.out.println("对方传来的消息源串：");
	     System.out.println(input);
	     
	     try {
	         SecretKeySpec skey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
	         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	         cipher.init(Cipher.DECRYPT_MODE, skey);
	         
	         byte[] b = Base64.decode(input);
	         System.out.println("Base64解码之后的结果：");
	         //解密
	         output = cipher.doFinal(b);
	         System.out.println("AES解密后的结果：");
	         String md5body = new String(output, "UTF-8");
	         System.out.println(md5body);
	         
	         if(md5body.length()<32)
	         {
	             System.out.println("错误！消息体长度小于数字签名长度!");
	             return null;
	         }
	         
	         String md5Client = md5body.substring(0,32);
	         System.out.println("对方传来的数字签名：");
	         System.out.println(md5Client);
	         
	         body = md5body.substring(32);
	         System.out.println("对方传来的消息体：");
	         System.out.println(body);
	         
	         String md5Server = MD5.md5(body);
	         System.out.println("我方对传来消息的数字签名：");
	         System.out.println(md5Server);
	         
	         if(!md5Client.equals(md5Server))
	         {
	             System.out.println("错误！数字签名不匹配:");
	             return null;
	         }
	     } catch (Exception e) {
	         System.out.println(e.toString());
	     }
	     return body;
	 }
}
