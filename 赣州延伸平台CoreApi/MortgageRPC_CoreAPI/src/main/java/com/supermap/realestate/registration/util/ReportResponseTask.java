package com.supermap.realestate.registration.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import com.supermap.realestate.registration.service.LogExchangeInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.supermap.realestate.registration.model.BDCS_REPORTINFO;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.Sender.dataReport;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * @Description:获取字典表信息
 * @author 刘树峰
 * @date 2015年6月12日 上午11:53:14
 * @Copyright SuperMap
 */
@Transactional
public class ReportResponseTask{
	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private LogExchangeInfoService logExchangeService;
	
	public Logger logger = Logger.getLogger(ReportResponseTask.class);

	public void LogReport(){
		logExchangeService.exchangeInfo("");
	}

	/*
	 * 获取数据上报响应信息
	 */
	public void ReportReponse() {
		///本地化配置：是否获取数据上报响应信息
		String GetReportReponse = ConfigHelper
				.getNameByValue("GetReportReponse");
		if (!"1".equals(GetReportReponse)) {
			return;
		}
		///本地化配置：数据上报方式
		//1、上报到国家接入系统（sftp方式）
		//2、上报到超图接入系统
		//3、同时上报到国家接入系统（sftp方式）和超图接入系统
		//4、上报到新疆省级接入系统
		///初始化相应报文列表
		ReportTool.InitReportInfoList();
		if (ReportTool.list_reportinfo == null
				|| ReportTool.list_reportinfo.size() <= 0) {
			try {
				Thread.sleep(60000);
			} catch (Exception e) {
			}
			return;
		}
		Date dt=new Date();
		System.out.println(StringHelper.FormatDateOnType(dt, "yyyy-MM-dd HH:mm:ss 待处理上报报文个数:")+ReportTool.list_reportinfo.size());
		while (ReportTool.list_reportinfo.size() > 0) {
			try{
				BDCS_REPORTINFO info = null;
				info = ReportTool.list_reportinfo.get(0);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.MILLISECOND,
						calendar.get(Calendar.MILLISECOND) - 100000);// 减100秒
				if (!StringHelper.isEmpty(info.getREPORTTIME())) {
					Calendar calendar_reporttime = Calendar.getInstance();
					calendar_reporttime.setTime(info.getREPORTTIME());
					if (calendar_reporttime.after(calendar)) {
						logger.info("报文生成时间在" + 100 + "秒内！不进行响应信息获取！");
						System.out.println(StringHelper.FormatDateOnType(dt, "yyyy-MM-dd HH:mm:ss 报文生成时间在" + 100 + "秒内！不进行响应信息获取！"));
						ReportTool.list_reportinfo.remove(info);
						continue;
					}
				}
				Calendar calendar1 = Calendar.getInstance();
				calendar1.setTime(new Date());
				calendar1.set(Calendar.MILLISECOND,
						calendar1.get(Calendar.MILLISECOND) - 600000);// 减600秒
				if (!StringHelper.isEmpty(info.getLASTRESTIME())) {
					Calendar calendar_respnesetime = Calendar.getInstance();
					calendar_respnesetime.setTime(info.getLASTRESTIME());
					if (calendar_respnesetime.after(calendar1)) {
						logger.info("报文上次获取响应信息在" + 600 + "秒内！不进行响应信息获取！");
						System.out.println(StringHelper.FormatDateOnType(dt, "yyyy-MM-dd HH:mm:ss 报文上次获取响应信息在" + 600 + "秒内！不进行响应信息获取！"));
						ReportTool.list_reportinfo.remove(info);
						continue;
					}
				}
				if("1".equals(info.getREPORTTYPE())){
					StartReponseFromInspurJointSystem(info);
				}else if ("2".equals(info.getREPORTTYPE())) {
					StartReponseFromSupermapJointSystem(info);
				}else if ("31".equals(info.getREPORTTYPE())) {
					boolean success=StartReponseFromInspurJointSystem(info);
					if(success){
						info.setREPORTTYPE("32");
						String result_super= uploadToSupermapJointSystem(info.getREPORTCONTENT());
						JSONObject json_super=JSONObject.fromObject(result_super);
						String status="false";
						String code="2";
						String message="上报省厅介入系统失败！";
						if(json_super!=null){
							if(json_super.containsKey("status")){
								status=StringHelper.formatObject(json_super.get("status"));
							}
							if(json_super.containsKey("code")){
								code=StringHelper.formatObject(json_super.get("code"));
							}
							if(json_super.containsKey("message")){
								message=StringHelper.formatObject(json_super.get("message"));
							}
						}
						info.setRESPENSECONTENT(result_super);
						info.setRESPONSECODE(code);
						info.setRESPONSEINFO(message);
						
						if("true".equals(status)&&"2".equals(code)){
							info.setSUCCESSFLAG("1");
						}else if("true".equals(status)&&"1".equals(code)){
							info.setSUCCESSFLAG("1");
						}else{
							info.setSUCCESSFLAG("2");
						}
						baseCommonDao.update(info);
					}
				}
				ReportTool.list_reportinfo.remove(info);
			}catch(Exception e){
				
			}
		}
	}
	
	public boolean StartReponseFromInspurJointSystem(BDCS_REPORTINFO info) {
		boolean success=false;
		ChannelSftp sftp = null;
		try {
			sftp = getSftp();
		} catch (JSchException e1) {
		} catch (SftpException e1) {
		}
		if (sftp == null) {
			logger.info("初始化sftp连接失败！");
			return success;
		}
		if (!sftp.isConnected()) {
			try {
				sftp.connect();
			} catch (JSchException e) {
			}
		}
		if (!sftp.isConnected()) {
			logger.info("sftp连接失败！");
			return success;
		}
		// SFTP中响应报文相对路径
		String SFTPREPMSGPATH = ConfigHelper.getNameByValue("SFTPREPMSGPATH");
		// 系统根目录
		String path = this.getClass().getClassLoader().getResource("/").getPath();
		File file = new File(path);
		path=file.getParentFile().getParentFile().getPath();
		String returnpath = "/resources/changefiles/";
		path = path + returnpath;
		GetResponseFromInspurJointSystem(info, sftp, SFTPREPMSGPATH, path);
		try {
			sftp.getSession().disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		sftp = null;
		return success;
	}

	@SuppressWarnings("rawtypes")
	public boolean GetResponseFromInspurJointSystem(BDCS_REPORTINFO info,
			ChannelSftp sftp, String SFTPREPMSGPATH, String path) {
		boolean success=false;
		try {
			String bizmsgid = info.getBIZMSGID();
			String filepath = download(SFTPREPMSGPATH, "Rep" + bizmsgid
					+ ".xml", path, "Rep" + bizmsgid + ".xml", sftp);
			File fileinfo = new File(filepath);
			if (fileinfo != null && fileinfo.exists()) {
				String responsexml = FileUtils.readFileToString(new File(
						filepath), "UTF-8");
				String successflag = "2";
				String responseinfo = "";
				String responsecode = "";
				String certid = "";
				String qrcode = "";
				Map resultmap =XmlUtil.xml2map(responsexml, true);
				if (resultmap != null && resultmap.containsKey("respond")) {
					Map respond=(Map)resultmap.get("respond");
					successflag = StringHelper.formatObject(respond.get("SuccessFlag"));
					responseinfo = StringHelper.formatObject(respond.get("ResponseInfo"));
					responsecode = StringHelper.formatObject(respond.get("ResponseCode"));
					certid = StringHelper.formatObject(respond.get("CertID"));
					qrcode = StringHelper.formatObject(respond.get("QRCode"));
				}
				if("1".equals(successflag)){
					success=true;
					info.setSUCCESSFLAG(successflag);
				}else{
					info.setSUCCESSFLAG("0");
				}
				info.setRESPONSECODE(responsecode);
				info.setRESPONSEINFO(responseinfo);
				info.setCERTID(certid);
				info.setCERTID(qrcode);
				Integer rescount = info.getRESCOUNT();
				if (rescount == null) {
					rescount = 0;
				}
				rescount++;
				info.setRESCOUNT(rescount);
				info.setLASTRESTIME(new Date());
				info.setRESPENSECONTENT(responsexml);
				baseCommonDao.update(info);
				String sfsb="1";
				List<BDCS_REPORTINFO> list_report=baseCommonDao.getDataList(BDCS_REPORTINFO.class, " XMBH='"+info.getXMBH()+"' AND YXBZ='1'");
				if(list_report!=null&&list_report.size()>0){
					for(BDCS_REPORTINFO report:list_report){
						if("0".equals(report.getSUCCESSFLAG())){
							sfsb="0";
							break;
						}else if("2".equals(report.getSUCCESSFLAG())){
							sfsb="2";
						}
					}
				}else{
					sfsb="-1";
				}
				BDCS_XMXX xmxx=Global.getXMXXbyXMBH(info.getXMBH());
				if(xmxx!=null){
					xmxx.setSFSB(sfsb);
					BDCS_XMXX xmxx_1=Global.getXMXX(xmxx.getPROJECT_ID());
					if(xmxx_1!=null){
						xmxx_1.setSFSB(sfsb);
					}
					baseCommonDao.update(xmxx);
				}
			} else {
				logger.info("未获取到响应报文！");
				Integer rescount = info.getRESCOUNT();
				if (rescount == null) {
					rescount = 0;
				}
				rescount++;
				info.setRESCOUNT(rescount);
				info.setLASTRESTIME(new Date());
				baseCommonDao.update(info);
			}
		} catch (Exception e) {
		} finally {
		}
		return success;
	}

	public ChannelSftp getSftp() throws JSchException, SftpException {
		ChannelSftp sftp = null;
		// String host=ConfigHelper.getNameByValue("SFTPIP");
		// int
		// port=StringHelper.getInt(ConfigHelper.getNameByValue("SFTPPORT"));
		// String host=ConfigHelper.getNameByValue("SFTPUSERNAME");
		// String password=ConfigHelper.getNameByValue("SFTPPASSWORD");
		dataReport report=new dataReport();
		String host = report.getIp();
		String userName = report.getUserName();
		String password = report.getPassword();
		int port = StringHelper.getInt(report.getPort());
		String root = "/";
		// SFTP中响应报文相对路径
		root = ConfigHelper.getNameByValue("SFTPREPMSGPATH");
		JSch jsch = new JSch();
		com.jcraft.jsch.Session _sshSession = jsch.getSession(userName, host,
				port);

		_sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		_sshSession.setConfig(sshConfig);
		_sshSession.connect();
		Channel channel = _sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		sftp.cd(root);
		return sftp;
	}

	public String download(String dir, String downloadFile,
			String localTempDir, String newFilename, ChannelSftp sftp)
			throws SftpException, Exception {
		String tempDir = "";
		try {
			if (true) {
				if (!localTempDir.endsWith("/") && !localTempDir.endsWith("\\")) {
					localTempDir += "/";
				}
				File file = new File(localTempDir);
				if (!file.exists()) {
					file.mkdir();
				}
				tempDir = localTempDir + newFilename;
				sftp.get(downloadFile, tempDir);
			}
		} catch (Exception e) {
		}
		return tempDir;
	}
	
	public void StartReponseFromSupermapJointSystem(BDCS_REPORTINFO reportinfo){
		String response_url = ConfigHelper
				.getNameByValue("SuperReponseUrl");
		response_url="http://192.168.38.232:8080/jointclient/receive/queryReportStatus";
		String result_super="";
		try {
			HashMap<String, Object> paraminfo=new HashMap<String, Object>();
			paraminfo.put("ids", reportinfo.getBIZMSGID());
			result_super = ProjectHelper.httpGet(response_url, paraminfo);
		} catch (Exception e) {
		}
		String code="1";
		String message="报文已转发至国家级平台前置端，待返回响应报文。";
		if(!StringHelper.isEmpty(result_super)){
			JSONArray json_super_array=JSONArray.fromObject(result_super);
			if(json_super_array!=null&&json_super_array.size()>0){
				JSONObject json_super=(JSONObject)json_super_array.get(0);
				if(json_super.containsKey("code")){
					code=StringHelper.formatObject(json_super.get("code"));
				}
				if(json_super.containsKey("msg")){
					message=StringHelper.formatObject(json_super.get("msg"));
				}
			}
			reportinfo.setRESPENSECONTENT(result_super);
		}
		reportinfo.setRESPONSECODE(code);
		reportinfo.setRESPONSEINFO(message);
		
		if("2".equals(code)){
			reportinfo.setSUCCESSFLAG("1");
		}else if("1".equals(code)){
			reportinfo.setSUCCESSFLAG("2");
		}else{
			reportinfo.setSUCCESSFLAG("0");
		}
		
		Integer rescount = reportinfo.getRESCOUNT();
		if (rescount == null) {
			rescount = 0;
		}
		rescount++;
		reportinfo.setRESCOUNT(rescount);
		reportinfo.setLASTRESTIME(new Date());
		baseCommonDao.update(reportinfo);
	}

	private String uploadToSupermapJointSystem(String strxml) {
		String sresult = "";
		String strurl =ConfigHelper.getNameByValue("OurJointClientUrl");// "http://localhost:8085/jointserver/receive/xmlfile";
		HttpURLConnection connet = null;
		try {
			StringBuilder sb=new StringBuilder();
			
			String xml=URLEncoder.encode(URLEncoder.encode(strxml,"utf-8"),"utf-8");
			String content =MessageFormat.format("XMLCONTENT={0}",xml);
			
			URL url = new URL(strurl);
			connet = (HttpURLConnection) url.openConnection();
			connet.setDoInput(true);
			connet.setDoOutput(true);
			connet.setRequestMethod("POST");
			//connet.setConnectTimeout(20 * 1000);// 设置连接超时时间为5秒
			//connet.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
			connet.setRequestProperty("Accept-Charset", "UTF-8");
			connet.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        OutputStream os = connet.getOutputStream();
	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
	        pw.write(content);
	        pw.close();
			BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
			String line;
			while ((line = brd.readLine()) != null)
			{
				sb.append(line);
			}
			brd.close();
			sresult=sb.toString();
		} catch (Exception e) {
		}
		return sresult;
	}
}