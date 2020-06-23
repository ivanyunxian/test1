package com.supermap.realestate.registration.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//import com.supermap.realestate.registration.model.BDCS_REPORTLOG;
import com.supermap.realestate.registration.service.LogExchangeInfoService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.HttpRequest;
import com.supermap.realestate.registration.util.HttpRequest.ParamInfo;
import com.supermap.realestate.registration.util.HttpRequest.WSDLInfo;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.XmlUtil;
//import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

import net.sf.json.JSONObject;

@Service("logExchangeInfoService")
public class LogExchangeInfoServiceImpl implements LogExchangeInfoService {

	@Autowired
	private CommonDao dao;

	/**
	 * 日志上报
	 */
	public String exchangeInfo(String path) {
		HashMap<String, Object> logInfo = createLogInfo();
		String logInfoStr = StringHelper
				.formatObject(logInfo.get("logInfoStr"));
		System.out.println("logInfoStr" + logInfoStr);
		// List<JSONObject>
		// logInfoDetailList=(List<JSONObject>)logInfo.get("logInfoDetailList");
		HashMap<String, String> uploadresult = logExchangeInfoUpload(logInfoStr); // 进行webService上报同时获取返回的结果
		String code = uploadresult.get("code");
		// String msg=uploadresult.get("msg");
		// for(JSONObject logInfoDetail:logInfoDetailList){
		// BDCS_REPORTLOG reportlog=new BDCS_REPORTLOG();
		// String reportlogid=SuperHelper.GeneratePrimaryKey();
		// reportlog.setId(reportlogid);;
		// reportlog.setREPORTDATE(logInfoDetail.getString("AccessDate"));
		// reportlog.setREPORTTIME(new Date());
		// reportlog.setLOGINFO(logInfoDetail.getString("AccessLog"));
		// if("2".equals(uploadresult)){
		// reportlog.setSUCCESSFLAG("1");
		// }else{
		// reportlog.setSUCCESSFLAG("0");
		// }
		// reportlog.setXZQHDM(logInfoDetail.getString("AreaCode"));
		// reportlog.setXZQHMC(logInfoDetail.getString("AreaName"));
		// reportlog.setRESLOGINFO(msg);
		// dao.save(reportlog);
		// }
		System.out.println("code" + code);
		return code;
	}

	/**
	 * 生成日志上报信息
	 */
	private HashMap<String, Object> createLogInfo() {
		HashMap<String, Object> logInfo = new HashMap<String, Object>();
		// 创建流程环节报文DocumentXML
		Element root = DocumentHelper.createElement("AccessLogs"); // 根元素节点

		Document document = DocumentHelper.createDocument(root); // 创建文档

		List<JSONObject> logInfoDetailList = new ArrayList<JSONObject>();
		  String sbdate=null;
			try {
				HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
				sbdate = (String) request.getSession().getAttribute("sbdate");
			} catch (Exception e) {
				System.out.println("自动上报时无request请求信息--忽略此异常");
			}

		HashMap<String, HashMap<String, Integer>> listReportLogInfo = getReportLogInfoList();

		String reportLogXZQHList = ConfigHelper
				.getNameByValue("reportLogXZQHList");
		List<String> listreportLogXZQH = new ArrayList<String>();
		if (!StringHelper.isEmpty(reportLogXZQHList)) {
			listreportLogXZQH = Arrays.asList(reportLogXZQHList.split(";"));
		} else {
			for (String xzqh : listReportLogInfo.keySet()) {
				listreportLogXZQH.add(xzqh);
			}
		}
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		for (String xzqh : listreportLogXZQH) {
			if(StringHelper.isEmpty(xzqh)||StringHelper.isEmpty(xzqh.trim())){
				continue;
			}
			Element accesslog = root.addElement("AccessLog");
			if (xzqhdm.startsWith("41")) {
				// 接入单位所在行政编码
				Element accessPointCode = accesslog
						.addElement("AccessPointCode");
				accessPointCode.setText(xzqhdm);
			}
			// 行政编码
			Element areacode = accesslog.addElement("AreaCode");
			areacode.setText(xzqh);
			// 行政名称 XX区县
			Element areaname = accesslog.addElement("AreaName");
			String xzqhmc = ConstHelper.getNameByValue("XZQH", xzqh);
			areaname.setText(xzqhmc);
			// 日期
			Date dt = new Date();
			Element accessdate = accesslog.addElement("AccessDate");
			String date = new SimpleDateFormat("yyyyMMdd").format(dt);
			if(!StringHelper.isEmpty(sbdate)) {
				sbdate =sbdate.replaceAll("-", "");
				date=sbdate;
			}
			accessdate.setText(date);

			// 备注
			Element remark = accesslog.addElement("Remark");
			remark.setText("备注");
			JSONObject logInfoDetail = new JSONObject();
			logInfoDetail.put("AreaCode", xzqh);
			logInfoDetail.put("AreaName", xzqhmc);
			logInfoDetail.put("AccessDate", date);

			HashMap<String, Integer> reportLogInfo = new HashMap<String, Integer>();
			if (listReportLogInfo.containsKey(xzqh)) {
				reportLogInfo = listReportLogInfo.get(xzqh);
			}
			/************************* 区县登簿信息记录 *******************************/
			if ("460000".equals(xzqhdm)||xzqhdm.startsWith("46")) {
				addElement(accesslog, "totalNum_RegisterInfo", reportLogInfo, "xmDbCount");
				addElement(accesslog, "firstReg_RegisterInfo", reportLogInfo, "firstReg_db");
				addElement(accesslog, "transferReg_RegisterInfo", reportLogInfo,
						"transferReg_db");
				addElement(accesslog, "changeReg_RegisterInfo", reportLogInfo,
						"changeReg_db");
				addElement(accesslog, "logoutReg_RegisterInfo", reportLogInfo,
						"logoutReg_db");
				addElement(accesslog, "riviseReg_RegisterInfo", reportLogInfo,
						"riviseReg_db");
				addElement(accesslog, "dissentingReg_RegisterInfo", reportLogInfo,
						"dissentingReg_db");
				addElement(accesslog, "advanceReg_RegisterInfo", reportLogInfo,
						"advanceReg_db");
				addElement(accesslog, "seizeReg_RegisterInfo", reportLogInfo, "seizeReg_db");
				addElement(accesslog, "easementReg_RegisterInfo", reportLogInfo,
						"easementReg_db");
				addElement(accesslog, "mortgageReg_RegisterInfo", reportLogInfo,
						"mortgageReg_db");
				addElement(accesslog, "businessTypeCount_RegisterInfo", reportLogInfo,
						"businessTypeCount");
				addElement(accesslog, "totalNum_AccessInfo", reportLogInfo,
						"suceessReportCount");
				addElement(accesslog, "firstReg_AccessInfo", reportLogInfo, "firstReg_suc");
				addElement(accesslog, "transferReg_AccessInfo", reportLogInfo,
						"transferReg_suc");
				addElement(accesslog, "changeReg_AccessInfo", reportLogInfo,
						"changeReg_suc");
				addElement(accesslog, "logoutReg_AccessInfo", reportLogInfo,
						"logoutReg_suc");
				addElement(accesslog, "riviseReg_AccessInfo", reportLogInfo,
						"riviseReg_suc");
				addElement(accesslog, "dissentingReg_AccessInfo", reportLogInfo,
						"dissentingReg_suc");
				addElement(accesslog, "advanceReg_AccessInfo", reportLogInfo,
						"advanceReg_suc");
				addElement(accesslog, "seizeReg_AccessInfo", reportLogInfo, "seizeReg_suc");
				addElement(accesslog, "easementReg_AccessInfo", reportLogInfo,
						"easementReg_suc");
				addElement(accesslog, "mortgageReg_AccessInfo", reportLogInfo,
						"mortgageReg_suc");
			}else{
				Element registerinfo = accesslog.addElement("RegisterInfo");
				addAttribute(registerinfo, "totalNum", reportLogInfo, "xmDbCount");
				addAttribute(registerinfo, "firstReg", reportLogInfo, "firstReg_db");
				addAttribute(registerinfo, "transferReg", reportLogInfo,
						"transferReg_db");
				addAttribute(registerinfo, "changeReg", reportLogInfo,
						"changeReg_db");
				addAttribute(registerinfo, "logoutReg", reportLogInfo,
						"logoutReg_db");
				addAttribute(registerinfo, "riviseReg", reportLogInfo,
						"riviseReg_db");
				addAttribute(registerinfo, "dissentingReg", reportLogInfo,
						"dissentingReg_db");
				addAttribute(registerinfo, "advanceReg", reportLogInfo,
						"advanceReg_db");
				addAttribute(registerinfo, "seizeReg", reportLogInfo, "seizeReg_db");
				addAttribute(registerinfo, "easementReg", reportLogInfo,
						"easementReg_db");
				addAttribute(registerinfo, "mortgageReg", reportLogInfo,
						"mortgageReg_db");
				addAttribute(registerinfo, "businessTypeCount", reportLogInfo,
						"businessTypeCount");

				/************************* 接入上报信息记录 *******************************/

				Element accessinfo = accesslog.addElement("AccessInfo");
				addAttribute(accessinfo, "totalNum", reportLogInfo,
						"suceessReportCount");
				addAttribute(accessinfo, "firstReg", reportLogInfo, "firstReg_suc");
				addAttribute(accessinfo, "transferReg", reportLogInfo,
						"transferReg_suc");
				addAttribute(accessinfo, "changeReg", reportLogInfo,
						"changeReg_suc");
				addAttribute(accessinfo, "logoutReg", reportLogInfo,
						"logoutReg_suc");
				addAttribute(accessinfo, "riviseReg", reportLogInfo,
						"riviseReg_suc");
				addAttribute(accessinfo, "dissentingReg", reportLogInfo,
						"dissentingReg_suc");
				addAttribute(accessinfo, "advanceReg", reportLogInfo,
						"advanceReg_suc");
				addAttribute(accessinfo, "seizeReg", reportLogInfo, "seizeReg_suc");
				addAttribute(accessinfo, "easementReg", reportLogInfo,
						"easementReg_suc");
				addAttribute(accessinfo, "mortgageReg", reportLogInfo,
						"mortgageReg_suc");
			}
			logInfoDetail.put("AccessLog", accesslog.asXML());
			logInfoDetailList.add(logInfoDetail);
		}

		String logInfoStr = document.asXML();
		if ("460000".equals(xzqhdm)||xzqhdm.startsWith("46")) {
			logInfoStr=document.getRootElement().asXML(); 
		}
		logInfo.put("logInfoStr", logInfoStr);
		logInfo.put("logInfoDetailList", logInfoDetailList);
		return logInfo;
	}
	
	private void addElement(Element info, String name,
			HashMap<String, Integer> map, String key){
		Element remark = info.addElement(name);
		remark.setText(getValue(map, key));
	}

	private void addAttribute(Element info, String name,
			HashMap<String, Integer> map, String key) {
		info.addAttribute(name, getValue(map, key));
	}

	private String getValue(HashMap<String, Integer> map, String key) {
		String value = "0";
		if (map.containsKey(key)) {
			value = StringHelper.formatObject(map.get(key));
		}
		return value;
	}

	/**
	 * 获取日志上报数据
	 */
	@SuppressWarnings("rawtypes")
	private HashMap<String, HashMap<String, Integer>> getReportLogInfoList() {
		StringBuilder builder = new StringBuilder();
		Date dt = new Date();
		String dtStr = StringHelper.FormatDateOnType(dt, "yyyy-MM-dd");
		HttpServletRequest request;
		String sbdate=null;
		try {
			request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			sbdate = (String) request.getSession().getAttribute("sbdate");
			if(!StringHelper.isEmpty(sbdate)) {
				dtStr=sbdate;
				request.getSession().removeAttribute("sbdate");//移除日期参数
			}
		} catch (Exception e) {
			System.out.println("自动上报时无request请求信息--忽略此异常");
		}
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		builder.append("SELECT XZQH,XMDJLX,QLLX,SUCCESSFLAG,RECTYPE,COUNT(*) GS FROM ");
		if ("460000".equals(xzqhdm)||xzqhdm.startsWith("46")) {
			builder.append("(SELECT ");
			builder.append("(SELECT MAX(SUBSTR(DY.BDCDYH,0,6)) ");
			builder.append("FROM BDCK.BDCS_DJDY_LS DJDY ");
			builder.append("LEFT JOIN ");
			builder.append("(SELECT BDCDYID,BDCDYH,'01' FROM BDCK.BDCS_SYQZD_LS ");
			builder.append("UNION ");
			builder.append("SELECT BDCDYID,BDCDYH,'02' FROM BDCK.BDCS_SHYQZD_LS ");
			builder.append("UNION SELECT BDCDYID,BDCDYH,'031' FROM BDCK.BDCS_H_LS ");
			builder.append("UNION SELECT BDCDYID,BDCDYH,'032' FROM BDCK.BDCS_H_LSY ");
			builder.append("UNION SELECT BDCDYID,BDCDYH,'04' FROM BDCK.BDCS_ZH_LS ");
			builder.append("UNION SELECT BDCDYID,BDCDYH,'05' FROM BDCK.BDCS_SLLM_LS ");
			builder.append(") DY ON DY.BDCDYID=DJDY.BDCDYID AND DJDY.BDCDYLX=DJDY.BDCDYLX ");
			builder.append("WHERE DJDY.DJDYID=INFO.DJDYID) XZQH,");
			builder.append("INFO.SUCCESSFLAG,XMXX.DJLX XMDJLX, QL.QLLX,INFO.RECTYPE ");
		}else{
			builder.append("(SELECT SUBSTR(INFO.BIZMSGID,0,6) XZQH,INFO.SUCCESSFLAG,XMXX.DJLX XMDJLX, QL.QLLX,INFO.RECTYPE ");
		}
		
		builder.append("FROM BDCK.BDCS_REPORTINFO INFO ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=INFO.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.QLID=INFO.QLID ");
		builder.append("WHERE INFO.BIZMSGID IS NOT NULL AND INFO.BIZMSGID NOT IN ('null') ");
		builder.append("AND XMXX.DJSJ BETWEEN TO_DATE('");
		builder.append(dtStr);
		builder.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('");
		builder.append(dtStr);
		builder.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')) T ");
		builder.append("GROUP BY XZQH,XMDJLX,QLLX,SUCCESSFLAG,RECTYPE ");
		builder.append("ORDER BY XZQH,XMDJLX,QLLX,SUCCESSFLAG,RECTYPE");
		List<Map> list_reportinfo = dao
				.getDataListByFullSql(builder.toString());
		HashMap<String, List<String>> xzqhRecTypeList = new HashMap<String, List<String>>();
		for (Map reportinfo : list_reportinfo) {
			String xzqh = StringHelper.formatObject(reportinfo.get("XZQH"));
			String recType = StringHelper.formatObject(reportinfo
					.get("RECTYPE"));
			if (StringHelper.isEmpty(xzqh)) {
				continue;
			}
			if (!xzqhRecTypeList.containsKey(xzqh)) {
				List<String> recTypeList = new ArrayList<String>();
				if (!StringHelper.isEmpty(recType)) {
					recTypeList.add(recType);
				}
				xzqhRecTypeList.put(xzqh, recTypeList);
			} else {
				List<String> recTypeList = xzqhRecTypeList.get(xzqh);
				if (!StringHelper.isEmpty(recType)
						&& !recTypeList.contains(recType)) {
					recTypeList.add(recType);
				}
				xzqhRecTypeList.put(xzqh, recTypeList);
			}
		}
		HashMap<String, HashMap<String, Integer>> listReportLogInfo = new HashMap<String, HashMap<String, Integer>>();
		for (String xzqh : xzqhRecTypeList.keySet()) {
			HashMap<String, Integer> reportLogInfo = new HashMap<String, Integer>();
			reportLogInfo.put("suceessReportCount", 0);
			reportLogInfo.put("xmDbCount", 0);
			reportLogInfo.put("businessTypeCount", xzqhRecTypeList.get(xzqh)
					.size());

			reportLogInfo.put("mortgageReg_db", 0);
			reportLogInfo.put("firstReg_db", 0);
			reportLogInfo.put("transferReg_db", 0);
			reportLogInfo.put("changeReg_db", 0);
			reportLogInfo.put("logoutReg_db", 0);
			reportLogInfo.put("riviseReg_db", 0);
			reportLogInfo.put("dissentingReg_db", 0);
			reportLogInfo.put("advanceReg_db", 0);
			reportLogInfo.put("seizeReg_db", 0);
			reportLogInfo.put("easementReg_db", 0);

			reportLogInfo.put("mortgageReg_suc", 0);
			reportLogInfo.put("firstReg_suc", 0);
			reportLogInfo.put("transferReg_suc", 0);
			reportLogInfo.put("changeReg_suc", 0);
			reportLogInfo.put("logoutReg_suc", 0);
			reportLogInfo.put("riviseReg_suc", 0);
			reportLogInfo.put("dissentingReg_suc", 0);
			reportLogInfo.put("advanceReg_suc", 0);
			reportLogInfo.put("seizeReg_suc", 0);
			reportLogInfo.put("easementReg_suc", 0);
			listReportLogInfo.put(xzqh, reportLogInfo);
		}
		if(xzqhdm.startsWith("1507") || xzqhdm.startsWith("1508") ) {//登簿记录和上报记录分开统计
	    	getSuccessResportEx( listReportLogInfo,  list_reportinfo , builder);
	    }else {
		for (Map reportinfo : list_reportinfo) {
			String xzqh = StringHelper.formatObject(reportinfo.get("XZQH"));
			String xmdjlx = StringHelper.formatObject(reportinfo.get("XMDJLX"));
			String qllx = StringHelper.formatObject(reportinfo.get("QLLX"));
			String successflag = StringHelper.formatObject(reportinfo
					.get("SUCCESSFLAG"));
			Integer gs = StringHelper.getInt(reportinfo.get("GS"));
			if (StringHelper.isEmpty(xzqh)) {
				continue;
			}
			HashMap<String, Integer> reportLogInfo = listReportLogInfo
					.get(xzqh);
			reportLogInfo.put("xmDbCount", reportLogInfo.get("xmDbCount") + gs);
			if ("1".equals(successflag) || "2".equals(successflag)) {
				reportLogInfo.put("suceessReportCount",
						reportLogInfo.get("suceessReportCount") + gs);
			}
			if ("23".equals(qllx)) {
				reportLogInfo.put("mortgageReg_db",
						reportLogInfo.get("mortgageReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("mortgageReg_suc",
							reportLogInfo.get("mortgageReg_suc") + gs);
				}
				continue;
			}
			if ("100".equals(xmdjlx)) {
				reportLogInfo.put("firstReg_db",
						reportLogInfo.get("firstReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("firstReg_suc",
							reportLogInfo.get("firstReg_suc") + gs);
				}
			} else if ("200".equals(xmdjlx)) {
				reportLogInfo.put("transferReg_db",
						reportLogInfo.get("transferReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("transferReg_suc",
							reportLogInfo.get("transferReg_suc") + gs);
				}
			} else if ("300".equals(xmdjlx)) {
				reportLogInfo.put("changeReg_db",
						reportLogInfo.get("changeReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("changeReg_suc",
							reportLogInfo.get("changeReg_suc") + gs);
				}
			} else if ("400".equals(xmdjlx)) {
				reportLogInfo.put("logoutReg_db",
						reportLogInfo.get("logoutReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("logoutReg_suc",
							reportLogInfo.get("logoutReg_suc") + gs);
				}
			} else if ("500".equals(xmdjlx)) {
				reportLogInfo.put("riviseReg_db",
						reportLogInfo.get("riviseReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("riviseReg_suc",
							reportLogInfo.get("riviseReg_suc") + gs);
				}
			} else if ("600".equals(xmdjlx)) {
				reportLogInfo.put("dissentingReg_db",
						reportLogInfo.get("dissentingReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("dissentingReg_suc",
							reportLogInfo.get("dissentingReg_suc") + gs);
				}
			} else if ("700".equals(xmdjlx)) {
				reportLogInfo.put("advanceReg_db",
						reportLogInfo.get("advanceReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("advanceReg_suc",
							reportLogInfo.get("advanceReg_suc") + gs);
				}
			} else if ("800".equals(xmdjlx)) {
				reportLogInfo.put("seizeReg_db",
						reportLogInfo.get("seizeReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("seizeReg_suc",
							reportLogInfo.get("seizeReg_suc") + gs);
				}
			} else {
				reportLogInfo.put("changeReg_db",
						reportLogInfo.get("changeReg_db") + gs);
				if ("1".equals(successflag) || "2".equals(successflag)) {
					reportLogInfo.put("changeReg_suc",
							reportLogInfo.get("changeReg_suc") + gs);
				}
			}
		 }
	   }
		return listReportLogInfo;
	}

	/**
	 * 日志上报
	 */
	public HashMap<String, String> logExchangeInfoUpload(String logInfo) {
		HashMap<String, String> resInfo = new HashMap<String, String>();
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String UoloadDestination = ConfigHelper
				.getNameByValue("UoloadDestination");
		String code = "-1";
		String msg = "上报失败！";
		System.out.println("本地化配置行政区划：" + xzqhdm);
		if (xzqhdm.startsWith("65")) {
			String result_super = uploadToProvincial650000(logInfo);
			resInfo.put("resInfo", result_super);
			if ("2".equals(result_super)) {
				code = "2";
				msg = result_super;
			}
		} else if ("411623".equals(xzqhdm)) {
			String result_super = uploadToProvincial410000(logInfo);
			resInfo.put("resInfo", result_super);
			if ("2".equals(result_super)) {
				code = "2";
				msg = result_super;
			}
		} else if ("460000".equals(xzqhdm)||xzqhdm.startsWith("46")) {
			String result_super = uploadToProvincial460000(logInfo);
			resInfo.put("resInfo", result_super);
			if ("2".equals(result_super)) {
				code = "2";
				msg = result_super;
			}
		}
		else if(xzqhdm!=null &&  xzqhdm.startsWith("15"))
		{
			//呼伦贝尔往特力惠上报
			String result_tlh = uploadTlhJointSystem(logInfo);
			resInfo.put("resInfo", result_tlh);
			if (result_tlh!=null && result_tlh.contains("0000")) {
				code = "2";
				msg = result_tlh;
			}
		}
		else if ("3".equals(UoloadDestination)) {
			String code_Inspur="-1";
			String msg_Inspur="上报部里失败！";
			String result_Inspur = uploadToInspurJointSystem(logInfo);
			resInfo.put("resInfo", result_Inspur);
			if ("success".equals(result_Inspur)) {
				code_Inspur = "2";
				msg_Inspur = result_Inspur;
			}
			
			String code_Supermap="-1";
			String msg_Supermap="上报省厅失败！";
			String result_Supermap = uploadToSupermapJointSystem(logInfo);
			JSONObject json_super = JSONObject.fromObject(result_Supermap);
			if (json_super != null) {
				if (json_super.containsKey("code")) {
					code_Supermap = StringHelper.formatObject(json_super.get("code"));
				}
				if (json_super.containsKey("message")) {
					msg_Supermap = StringHelper.formatObject(json_super.get("msg"));

				}
			}
			
			if("2".equals(code_Inspur)&&"2".equals(code_Supermap)){
				code="2";
				msg="日志上报省厅、国家成功！";
			}else if(!"2".equals(code_Inspur)&&!"2".equals(code_Supermap)){
				code="-1";
				msg="日志上报省厅、国家失败！";
			}else if("2".equals(code_Inspur)){
				code="-1";
				msg="日志上报省厅失败！日志上报国家成功！";
			}else if("2".equals(code_Supermap)){
				code="-1";
				msg="日志上报省厅成功！日志上报国家失败！";
			}else{
				code="-1";
				msg="日志上报省厅、国家失败！";
			}
		} else if ("1".equals(UoloadDestination) || xzqhdm.startsWith("22")) {
			String result_super = uploadToInspurJointSystem(logInfo);
			resInfo.put("resInfo", result_super);
			if ("success".equals(result_super)) {
				code = "2";
				msg = result_super;
			}
		} else if ("2".equals(UoloadDestination)) {
			String result_super = uploadToSupermapJointSystem(logInfo);
			JSONObject json_super = JSONObject.fromObject(result_super);
			if (json_super != null) {
				if (json_super.containsKey("code")) {
					code = StringHelper.formatObject(json_super.get("code"));
				}
				if (json_super.containsKey("message")) {
					msg = StringHelper.formatObject(json_super.get("msg"));

				}
			}
		}
		resInfo.put("code", code);
		resInfo.put("msg", msg);
		return resInfo;
	}

	/*
	 * 日志上报到超图省（市）级平台
	 */
	private String uploadToSupermapJointSystem(String strxml) {
		System.out.println("日志上报xml：" + strxml);
		System.out.println("开始日志上报超图省（市）级平台：");
		String sresult = "";
		String strurl = ConfigHelper.getNameByValue("LogJointClientUrl");// "http://localhost:8085/jointserver/receive/xmlfile";
		System.out.println("日志上报地址：" + strurl);
		HttpURLConnection connet = null;
		try {
			StringBuilder sb = new StringBuilder();

			String xml = URLEncoder.encode(URLEncoder.encode(strxml, "utf-8"),
					"utf-8");
			String content = MessageFormat.format("logxml={0}", xml);

			URL url = new URL(strurl);
			connet = (HttpURLConnection) url.openConnection();
			connet.setDoInput(true);
			connet.setDoOutput(true);
			connet.setRequestMethod("POST");
			// connet.setConnectTimeout(20 * 1000);// 设置连接超时时间为5秒
			// connet.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
			connet.setRequestProperty("Accept-Charset", "UTF-8");
			connet.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			OutputStream os = connet.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
			pw.write(content);
			pw.close();
			BufferedReader brd = new BufferedReader(new InputStreamReader(
					connet.getInputStream(), "utf-8"));
			String line;
			while ((line = brd.readLine()) != null) {
				sb.append(line);
			}
			brd.close();
			sresult = sb.toString();
			System.out.println("日志上报结果：" + sresult);
		} catch (Exception e) {
			sresult = e.getMessage();
			System.out.println("日志上报异常：" + sresult);
		}
		return sresult;
	}

	/*
	 * 日志上报到国家
	 */
	private String uploadToInspurJointSystem(String strxml) {
		System.out.println("日志上报xml：" + strxml);
		System.out.println("开始日志上报国家：");
		String sresult = "";
		String soapaction = "urn:exchangeInfo";
		String targetnamespace = "http://loushang.ws";
		String strurl = ConfigHelper.getNameByValue("LogJointClientUrl");
		if(ConfigHelper.getNameByValue("XZQHDM").startsWith("230")) {//齐齐哈尔
			strurl = ConfigHelper.getNameByValue("LogSuperMapJointClientUrl");
		}
		String methodname = "exchangeInfo";
		System.out.println("日志上报地址：" + strurl);
		List<ParamInfo> params = new ArrayList<ParamInfo>();

		ParamInfo param_bizmsg = new ParamInfo();
		param_bizmsg.setParamName("args0");
		param_bizmsg.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_bizmsg.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_bizmsg.setParamValue(strxml);
		params.add(param_bizmsg);

		WSDLInfo wsdlinfo = new WSDLInfo();
		wsdlinfo.setMethodName(methodname);
		wsdlinfo.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		wsdlinfo.setSoapAction(soapaction);
		wsdlinfo.setTargetNamespace(targetnamespace);
		wsdlinfo.setUrl(strurl);
		Object obj = null;
		try {
			obj = HttpRequest.sendWSDL(wsdlinfo, params);
			sresult = StringHelper.formatObject(obj);
			System.out.println("日志上报结果：" + sresult);
		} catch (Exception e) {
			sresult = e.getMessage();
			System.out.println("日志上报异常：" + sresult);
		}
		return sresult;
	}

	private String uploadTlhJointSystem(String strxml) {
		System.out.println("日志上报xml：" + strxml);
		System.out.println("开始日志上报特力惠省级平台：");
		String sresult = "";
		String soapaction = "http://tempuri.org/exchangeInfo";
		String targetnamespace = "http://tempuri.org/";
		String strurl = ConfigHelper.getNameByValue("LogJointClientUrl");
		String methodname = "exchangeInfo";
		System.out.println("日志上报地址：" + strurl);
		List<ParamInfo> params = new ArrayList<ParamInfo>();

		ParamInfo param_bizmsg = new ParamInfo();
		param_bizmsg.setParamName("biz");
		param_bizmsg.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_bizmsg.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_bizmsg.setParamValue(strxml);
		params.add(param_bizmsg);

		WSDLInfo wsdlinfo = new WSDLInfo();
		wsdlinfo.setMethodName(methodname);
		wsdlinfo.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		wsdlinfo.setSoapAction(soapaction);
		wsdlinfo.setTargetNamespace(targetnamespace);
		wsdlinfo.setUrl(strurl);
		Object obj = null;
		try {
			obj = HttpRequest.sendWSDL(wsdlinfo, params);
			sresult = StringHelper.formatObject(obj);
			System.out.println("日志上报结果：" + sresult);
		} catch (Exception e) {
			sresult = e.getMessage();
			System.out.println("日志上报异常：" + sresult);
		}
		return sresult;
	}
	
	/*
	 * 新疆日志上报
	 */
	private String uploadToProvincial650000(String strxml) {
		System.out.println("日志上报xml：" + strxml);
		System.out.println("开始日志上报新疆省级平台：");
		String sresult = "";
		String soapaction = "XJBDCLogWebService/exchangeInfo";
		String targetnamespace = "XJBDCLogWebService";
		String strurl = ConfigHelper.getNameByValue("LogJointClientUrl");
		System.out.println("日志上报地址：" + strurl);
		String methodname = "exchangeInfo";
		String username = ConfigHelper
				.getNameByValue("UserNameReportWSDLForLog");
		String password = ConfigHelper
				.getNameByValue("PassWordReportWSDLForLog");

		List<ParamInfo> params = new ArrayList<ParamInfo>();
		ParamInfo param_bizmsg = new ParamInfo();

		param_bizmsg.setParamName("registrationLogXml");
		param_bizmsg.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_bizmsg.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_bizmsg.setParamValue(strxml);
		params.add(param_bizmsg);

		ParamInfo param_user = new ParamInfo();
		param_user.setParamName("username");
		param_user.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_user.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_user.setParamValue(username);
		params.add(param_user);

		ParamInfo param_pwd = new ParamInfo();
		param_pwd.setParamName("password");
		param_pwd.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_pwd.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_pwd.setParamValue(password);
		params.add(param_pwd);

		WSDLInfo wsdlinfo = new WSDLInfo();
		wsdlinfo.setMethodName(methodname);
		wsdlinfo.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		wsdlinfo.setSoapAction(soapaction);
		wsdlinfo.setTargetNamespace(targetnamespace);
		wsdlinfo.setUrl(strurl);
		wsdlinfo.setUser(username);
		wsdlinfo.setPassWord(password);
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		Object obj = null;
		try {
			obj = HttpRequest.sendWSDL(wsdlinfo, params);
			String resutnInfo = StringHelper.formatObject(obj);
			System.out.println("日志上报结果：" + resutnInfo);
			if (!StringHelper.isEmpty(resutnInfo)
					&& resutnInfo.contains("success")) {
				sresult = "2";
			} else {
				sresult = resutnInfo;
			}
		} catch (Exception e) {
			sresult = e.getMessage();
			System.out.println("日志上报异常：" + sresult);
		}
		return sresult;
	}

	/**
	 * 河南日志上报
	 */
	@SuppressWarnings("rawtypes")
	private String uploadToProvincial410000(String strxml) {
		System.out.println("日志上报xml-utf-8：" + strxml);
		strxml = parseUnicode(strxml);
		System.out.println("日志上报xml-unicode：" + strxml);
		System.out.println("开始日志上报河南省级平台：");
		String sresult = "";
		String soapaction = "http://gd.bdcdataar.org/ReportRegLog";
		String targetnamespace = "http://gd.bdcdataar.org/";
		String strurl = ConfigHelper.getNameByValue("LogJointClientUrl");
		System.out.println("日志上报地址：" + strurl);
		String methodname = "ReportRegLog";
		String username = ConfigHelper
				.getNameByValue("UserNameReportWSDLForLog");
		String password = ConfigHelper
				.getNameByValue("PassWordReportWSDLForLog");
		String password_md5 = com.supermap.wisdombusiness.utility.StringHelper
				.encryptMD5(password);

		List<ParamInfo> params = new ArrayList<ParamInfo>();
		ParamInfo param_bizmsg = new ParamInfo();

		param_bizmsg.setParamName("message");
		param_bizmsg.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_bizmsg.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_bizmsg.setParamValue(strxml);
		params.add(param_bizmsg);

		ParamInfo param_user = new ParamInfo();
		param_user.setParamName("userName");
		param_user.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_user.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_user.setParamValue(username);
		params.add(param_user);

		ParamInfo param_pwd = new ParamInfo();
		param_pwd.setParamName("pwd");
		param_pwd.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_pwd.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_pwd.setParamValue(password_md5);
		params.add(param_pwd);

		WSDLInfo wsdlinfo = new WSDLInfo();
		wsdlinfo.setMethodName(methodname);
		wsdlinfo.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		wsdlinfo.setSoapAction(soapaction);
		wsdlinfo.setTargetNamespace(targetnamespace);
		wsdlinfo.setUrl(strurl);
		Object obj = null;
		try {
			obj = HttpRequest.sendWSDL(wsdlinfo, params);
			String resutnInfo = StringHelper.formatObject(obj);

			System.out.println("日志上报结果：" + resutnInfo);
			if (!StringHelper.isEmpty(resutnInfo)) {
				Map resultMap = XmlUtil.xml2map(resutnInfo, false);
				if (resultMap.containsKey("SuccessFlag")) {
					if ("1".equals(resultMap.get("SuccessFlag"))) {
						sresult = "2";
					} else {
						if (resultMap.containsKey("ResultContent")) {
							sresult = StringHelper.formatObject(resultMap
									.get("ResultContent"));
						}
					}
				}
			}
		} catch (Exception e) {
			sresult = e.getMessage();
			System.out.println("日志上报异常：" + sresult);
		}
		return sresult;
	}
	
	/**
	 * 海南日志上报
	 */
	private String uploadToProvincial460000(String strxml) {
		System.out.println("日志上报xml-utf-8：" + strxml);
		System.out.println("开始日志上报海南省级平台：");
		String sresult = "";
		String strurl = ConfigHelper.getNameByValue("LogJointClientUrl");
		System.out.println("日志上报地址：" + strurl);
		String methodname = "exchangeInfo";
		try {
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(strurl);
			call.setOperationName(new QName(strurl, methodname));
			String resutnInfo = (String) call.invoke(new Object[]{strxml});
			System.out.println("日志上报结果：" + resutnInfo);
			if (!StringHelper.isEmpty(resutnInfo)
					&& resutnInfo.contains("success")) {
				sresult = "2";
			} else {
				sresult = resutnInfo;
			}
		} catch (Exception e) {
			sresult = e.getMessage();
			System.out.println("日志上报异常：" + sresult);
		}
		return sresult;
	}

	/**
	 * 字符串转换unicode
	 */
	public static String parseUnicode(String str) {
		char[] charbuffer = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < charbuffer.length; i++) {
			char c = charbuffer[i];
			String s1 = String
					.format("%2s", Integer.toHexString(c & 0xFF).toUpperCase())
					.replaceAll("\\s", "0").substring(0, 2);
			String s2 = String
					.format("%2s",
							Integer.toHexString(c & 0xFF00).toUpperCase())
					.replaceAll("\\s", "0").substring(0, 2);
			sb.append(MessageFormat.format("\\u{0}{1}", s2, s1));
		}
		return sb.toString();
	}
	
	 /**
	  * 登簿数据和上报日志数据分开统计
	 * @param listReportLogInfo
	 * @param list_reportinfo
	 * @param builder
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private void getSuccessResportEx(HashMap<String, HashMap<String, Integer>> listReportLogInfo, List<Map> list_reportinfo ,StringBuilder builder) {
		    for (Map reportinfo : list_reportinfo)
		    {
		      String xzqh = StringHelper.formatObject(reportinfo.get("XZQH"));
		      String xmdjlx = StringHelper.formatObject(reportinfo.get("XMDJLX"));
		      String qllx = StringHelper.formatObject(reportinfo.get("QLLX"));
		      String successflag = StringHelper.formatObject(reportinfo .get("SUCCESSFLAG"));
		      Integer gs = StringHelper.getInt(reportinfo.get("GS"));
		      if (!StringHelper.isEmpty(xzqh))
		      {
		        HashMap<String, Integer> reportLogInfo =  listReportLogInfo.get(xzqh);
		        reportLogInfo.put("xmDbCount", Integer.valueOf(((Integer)reportLogInfo.get("xmDbCount")).intValue() + gs.intValue()));
		        if ("23".equals(qllx))
		        {
		          reportLogInfo.put("mortgageReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("mortgageReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("100".equals(xmdjlx))
		        {
		          reportLogInfo.put("firstReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("firstReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("200".equals(xmdjlx))
		        {
		          reportLogInfo.put("transferReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("transferReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("300".equals(xmdjlx))
		        {
		          reportLogInfo.put("changeReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("changeReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("400".equals(xmdjlx))
		        {
		          reportLogInfo.put("logoutReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("logoutReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("500".equals(xmdjlx))
		        {
		          reportLogInfo.put("riviseReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("riviseReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("600".equals(xmdjlx))
		        {
		          reportLogInfo.put("dissentingReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("dissentingReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("700".equals(xmdjlx))
		        {
		          reportLogInfo.put("advanceReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("advanceReg_db")).intValue() + gs.intValue()));
		        }
		        else if ("800".equals(xmdjlx))
		        {
		          reportLogInfo.put("seizeReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("seizeReg_db")).intValue() + gs.intValue()));
		        }
		        else
		        {
		          reportLogInfo.put("changeReg_db", 
		            Integer.valueOf(((Integer)reportLogInfo.get("changeReg_db")).intValue() + gs.intValue()));
		        }
		      }
		    }
		   
		    String builder_sql = builder.toString();
		    String replace_sql= builder_sql.replace("XMXX.DJSJ", "INFO.REPORTTIME");
		    List<Map> list_reportinfo1 = this.dao.getDataListByFullSql(replace_sql);//使用上报时间作为查询条件
		    for (Map reportinfo : list_reportinfo1)
		    {
		      String xzqh = com.supermap.realestate.registration.util.StringHelper.formatObject(reportinfo.get("XZQH"));
		      String xmdjlx = com.supermap.realestate.registration.util.StringHelper.formatObject(reportinfo.get("XMDJLX"));
		      String qllx = com.supermap.realestate.registration.util.StringHelper.formatObject(reportinfo.get("QLLX"));
		      String successflag = com.supermap.realestate.registration.util.StringHelper.formatObject(reportinfo .get("SUCCESSFLAG"));
		      Integer gs = Integer.valueOf(com.supermap.realestate.registration.util.StringHelper.getInt(reportinfo.get("GS")));
		      if (!com.supermap.realestate.registration.util.StringHelper.isEmpty(xzqh))
		      {
		        HashMap<String, Integer> reportLogInfo =  listReportLogInfo.get(xzqh);
		        if( reportLogInfo == null || reportLogInfo.size() == 0 ) {
		        	 reportLogInfo = new HashMap<String, Integer>();
		             reportLogInfo.put("suceessReportCount", Integer.valueOf(0));
		             reportLogInfo.put("businessTypeCount",  Integer.valueOf(0));
		             reportLogInfo.put("mortgageReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("firstReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("transferReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("changeReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("logoutReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("riviseReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("dissentingReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("advanceReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("seizeReg_suc", Integer.valueOf(0));
		             reportLogInfo.put("easementReg_suc", Integer.valueOf(0));
		        	listReportLogInfo.put(xzqh, reportLogInfo);
		        }
		        	if (("1".equals(successflag)) || ("2".equals(successflag))) {
		        		reportLogInfo.put("suceessReportCount", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("suceessReportCount")).intValue() + gs.intValue()));
		        	}
		        	if ("23".equals(qllx) &&("1".equals(successflag)) || ("2".equals(successflag)))
		        	{
		        		reportLogInfo.put("mortgageReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("mortgageReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("100".equals(xmdjlx) && ("1".equals(successflag)) || ("2".equals(successflag)))
		        	{
		        		reportLogInfo.put("firstReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("firstReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("200".equals(xmdjlx) && ("1".equals(successflag)) || ("2".equals(successflag)))
		        	{
		        		reportLogInfo.put("transferReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("transferReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("300".equals(xmdjlx) &&("1".equals(successflag)) || ("2".equals(successflag)) )
		        	{
		        		reportLogInfo.put("changeReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("changeReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("400".equals(xmdjlx) && ("1".equals(successflag)) || ("2".equals(successflag)))
		        	{
		        		reportLogInfo.put("logoutReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("logoutReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("500".equals(xmdjlx) &&("1".equals(successflag)) || ("2".equals(successflag)) )
		        	{
		        		reportLogInfo.put("riviseReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("riviseReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("600".equals(xmdjlx) && ("1".equals(successflag)) || ("2".equals(successflag)))
		        	{
		        		reportLogInfo.put("dissentingReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("dissentingReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("700".equals(xmdjlx) && ("1".equals(successflag)) || ("2".equals(successflag)) )
		        	{
		        		reportLogInfo.put("advanceReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("advanceReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else if ("800".equals(xmdjlx) && ("1".equals(successflag)) || ("2".equals(successflag)))
		        	{
		        		reportLogInfo.put("seizeReg_suc", 
		        				Integer.valueOf(((Integer)reportLogInfo.get("seizeReg_suc")).intValue() + gs.intValue()));
		        	}
		        	else
		        	{
		        		if (("1".equals(successflag)) || ("2".equals(successflag))) {
		        			reportLogInfo.put("changeReg_suc", 
		        					Integer.valueOf(((Integer)reportLogInfo.get("changeReg_suc")).intValue() + gs.intValue()));
		        		}
		        	}
		      }
		    }
	  }
}
