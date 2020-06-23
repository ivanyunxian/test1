 package com.supermap.intelligent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.intelligent.dao.CommonDaoMRPC;
import com.supermap.intelligent.model.JsonMessage;
import com.supermap.intelligent.model.SYS_USER;
import com.supermap.intelligent.service.BdcInterfaceService;
import com.supermap.intelligent.util.ConstValue;
import com.supermap.intelligent.util.ConstValue.MrpccodingEnum;
import com.supermap.intelligent.util.ConstValue.RequestcodeEnum;
import com.supermap.intelligent.util.ConstValue.SQLEnum;
import com.supermap.intelligent.util.HttpClientUtil;
import com.supermap.intelligent.util.JwtUtils;
import com.supermap.intelligent.util.RSACoder;
import com.supermap.intelligent.util.StringHelper;
import com.supermap.intelligent.util.XMLHelp;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate_gx.registration.util.Base64Util;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoJY;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 *
 */
@Service
public class BdcInterfaceServiceImpl implements BdcInterfaceService {
	@Autowired
	private CommonDao baseCommonDao;
	@Autowired
	private CommonDaoMRPC baseCommonDaoMRPC;
	@Autowired
	private CommonDaoJY baseCommonDaoJY;
	@Autowired
	private com.supermap.realestate.registration.service.ZSService2 ZSService;
	
	@Autowired
	private QueryService queryService;
	// 50分钟
	private final static long TIME_OUT = 50 * 60 * 1000; 
	/**
	 *读取配置文件，获取 权利对应的节点
	 */
	private final static String[] GETTABLES;
	private final static String[] GETTABLES_EX;
	private final static Map<String, Object> GETTABLES_OTHER = new HashMap<String, Object>();
	/**
	 * 读取配置文件，获取查询字段
	 */
	private final  static Map<String, Map<String, List<String>>> WHOSELECTSQL;
	private final  static Map<String, Map<String, List<String>>> WHOSELECTSQL_EX;
	private final  static Map<String, Map<String, Map<String, List<String>>>> WHOSELECTSQL_OTHER = new HashMap<String, Map<String, Map<String, List<String>>>>();
	private static  Map<String,QueryModule> queryModuleMap; 
	
	
		
	static{
		Element rootElement = XMLHelp.getElementByJgdm("99", "/apiResultConfig.xml");
		GETTABLES = rootElement.element("nodes").getText().split(",");
		WHOSELECTSQL = new HashMap<String, Map<String, List<String>>>(GETTABLES.length);
		for (String str:GETTABLES) {
			String nondes_tablename = str.toUpperCase();
			Element element = rootElement.element("fieldconfig").element(nondes_tablename);
			WHOSELECTSQL.put(nondes_tablename, XMLHelp.formatSelectSql(element, nondes_tablename,true));
		}
		Element rootElement1 = XMLHelp.getElementByJgdm("90", "/apiResultConfig.xml");
		GETTABLES_EX = rootElement1.element("nodes").getText().split(",");
		WHOSELECTSQL_EX = new HashMap<String, Map<String, List<String>>>(GETTABLES_EX.length);
		for (String str:GETTABLES_EX) {
			String nondes_tablename = str.toUpperCase();
			Element element = rootElement1.element("fieldconfig").element(nondes_tablename);
			WHOSELECTSQL_EX.put(nondes_tablename, XMLHelp.formatSelectSql(element, nondes_tablename,false));
		}
	}

	/**
	 * @Author taochunda
	 * @Description 根据机构代码获取配置详情
	 * @Date 2019-09-20 10:48
	 * @Param []
	 * @return void
	 **/
	public static void getResyltConfigInfo(String jgdm) {
		if (StringHelper.isEmpty(jgdm)) {
			return;
		}
		if (!GETTABLES_OTHER.containsKey(jgdm)) {
			Element rootElement = XMLHelp.getElementByJgdm(jgdm, "/apiResultConfig.xml");
			String[] nodes = rootElement.element("nodes").getText().split(",");
			GETTABLES_OTHER.put(jgdm, nodes);
			HashMap<String, Map<String, List<String>>> mapHashMap = new HashMap<String, Map<String, List<String>>>(nodes.length);
			for (String str : nodes) {
				String nondes_tablename = str.toUpperCase();
				Element element = rootElement.element("fieldconfig").element(nondes_tablename);
				mapHashMap.put(nondes_tablename, XMLHelp.formatSelectSql(element, nondes_tablename,false));
			}
			if (!WHOSELECTSQL_OTHER.containsKey(jgdm)) {
				WHOSELECTSQL_OTHER.put(jgdm, mapHashMap);
			}
		}

	}

	/**
	 * 获取不动产登记证明 
	 * 调用登记系统证书证明接口，注意依赖路径
	 * @param zsidMaps 查询条件zsid,xmbh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public JSONArray ZM_JSONArray(List<Map> zsidMaps) {
		if (zsidMaps.size() > 0) {
			JSONArray jsonArray=new JSONArray(zsidMaps.size());
			for (Map zsidMap : zsidMaps) {
				jsonArray.add(ZSService.getBDCDJZM(StringHelper.formatObject(zsidMap.get("XMBH")), StringHelper.formatObject(zsidMap.get("ZSID"))));
			}
			return jsonArray;
		}
		return null;
	}
	/**
	 * 获取不动产登记证书
	 * 调用登记系统证书证明接口，注意依赖路径
	 * @param zsidMaps
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public JSONArray ZS_JSONArray(List<Map> zsidMaps) {
		if (zsidMaps.size() > 0) {
			JSONArray jsonArray=new JSONArray(zsidMaps.size());
			for (Map zsidMap : zsidMaps) {
				jsonArray.add(ZSService.getZSForm(StringHelper.formatObject(zsidMap.get("XMBH")), StringHelper.formatObject(zsidMap.get("ZSID"))));
			}
			return jsonArray;
		}
		return null;
	}

	/**
	 * 构造证书证明返回的json格式
	 * @param code
	 *            相应编码
	 * @param queryResultsDate
	 *            查询结果
	 * @param extraDate
	 *            额外的值
	 * @return
	 */
	public String resultsJson(String code, String requestcode, JSONArray queryResultsDate, Object extraDate) {
		JSONObject resultsJson = new JSONObject(6);
		resultsJson.put("code", code);
		resultsJson.put("msg", MrpccodingEnum.initFrom(code).Name);
		resultsJson.put("requestcode", requestcode);
		resultsJson.put("requestseq", System.currentTimeMillis());
		resultsJson.put("data", queryResultsDate);
		resultsJson.put("extra", extraDate);

		try {
			return RSACoder.encryptByPrivateKeyBase64(resultsJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resultsJson.put("code", MrpccodingEnum.ERROR);
				resultsJson.put("msg", MrpccodingEnum.ERROR.Name);
				resultsJson.put("data", "");
				return RSACoder.encryptByPrivateKeyBase64(resultsJson.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	public void getQueryModuleMap(){
		//后续添加其他查询模块需要添加对应的策略
		if(null==queryModuleMap){
			queryModuleMap=new HashMap<String,QueryModule>(5);
			queryModuleMap.put(RequestcodeEnum.ZS.Value, new ZS());
			queryModuleMap.put(RequestcodeEnum.ZM.Value, new ZM());
			queryModuleMap.put(RequestcodeEnum.FW.Value, new FW());
			queryModuleMap.put(RequestcodeEnum.JD.Value, new JD());
			queryModuleMap.put(RequestcodeEnum.HJ.Value, new HJ());
			queryModuleMap.put(RequestcodeEnum.QZ.Value, new QZ());
			queryModuleMap.put(RequestcodeEnum.DY.Value, new DY());
			queryModuleMap.put(RequestcodeEnum.DYXZ.Value, new DYXZ());
			queryModuleMap.put(RequestcodeEnum.DYZT.Value, new DYZT());
			queryModuleMap.put(RequestcodeEnum.DZZZ.Value, new DZZZ());
			queryModuleMap.put(RequestcodeEnum.ZFJK.Value, new ZFJK());
			queryModuleMap.put(RequestcodeEnum.ZD.Value, new ZD());
		}
	}
	/**查询模块
	 * @param requestDate
	 * @return
	 */
	public JsonMessage queryModule(JSONObject requestDate) throws Exception {
		String requestcode = StringHelper.formatObject(requestDate.get("requestcode"));
		requestDate.get("requestseq");
		JSONArray datajson = requestDate.getJSONArray("data");
		JSONArray jsonArray = new JSONArray(datajson.size());
		String xzqdm = StringHelper.formatObject(datajson.getJSONObject(0).get("xzqdm"));
		getQueryModuleMap();
		for (int i = 0; i < datajson.size(); i++) {
			queryModuleMap.get(requestcode).run(datajson.getJSONObject(i),jsonArray);
		}
		if (jsonArray.size()==1&&null==jsonArray.get(0)) {
			return new JsonMessage(false, xzqdm, "");
		}
		return new JsonMessage(true, xzqdm, jsonArray);
		    
	}
	
	/**接口请求处理模块
	 * @param request
	 * @return
	 */
	@Override
	public String finalResultModule(HttpServletRequest request) throws Exception {
		//TODO日志保存处理
		String tokenCode = JwtUtils.verifyToken(request.getParameter("token"));
		if (MrpccodingEnum.SUCCESS.Value.equals(tokenCode)) {
			String decryDate = decryptRequest(request);
			if(MrpccodingEnum.FAIL.Value.equals(decryDate)){
				return resultsJson(MrpccodingEnum.FAIL.Value, RequestcodeEnum.TOKEN.Value, null, "");
			}
			JSONObject decryptJson= JSONObject.parseObject(decryDate);
			String requestcode = StringHelper.formatObject(decryptJson.get("requestcode"));
			JsonMessage resultjson = queryModule(decryptJson);
//			log.setXzqdm(resultjson.getMsg());
			if (resultjson.getState()) {
//				log.setReturndata(resultjson.getData().toString());
				return resultsJson(MrpccodingEnum.SUCCESS.Value, requestcode, (JSONArray) resultjson.getData(), "");
			} else {
				return resultsJson(MrpccodingEnum.NOINFORMATION.Value, requestcode, null, "");
			}
		}
		return resultsJson(tokenCode, RequestcodeEnum.TOKEN.Value, null, "");
	}

	/**读取数据流
	 * @param request
	 * @return
	 */
	public String getHttpServletRequestDate(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer(256);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	/**
	 * 根据报文JSON生成（证书证明接口）查询语句 
	 * 单人查询时兼容15和18位身份证
	 * @param qlrjson
	 * @param bdcqzh
	 * @return  
	 */
	public String getZS_ZM_SQL(JSONArray qlrjson, Object bdcqzh) {
		StringBuffer cxsql=new StringBuffer(256);
		int qlrSize = qlrjson.size();
		if (1 == qlrSize) {
			String qlrmc = StringHelper.formatObject(qlrjson.getJSONObject(0).get("qlrmc"));
			String qlrzjh = StringHelper.formatObject(qlrjson.getJSONObject(0).get("zjhm"));
			String zjh15or18 =18== qlrzjh.length()? ConverIdCard.getOldIDCard(qlrzjh): ConverIdCard.getNewIDCard(qlrzjh);
			cxsql.append("SELECT DISTINCT ZS.ZSID,ZS.XMBH FROM BDCK.BDCS_ZS_XZ ZS INNER JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.XMBH=ZS.XMBH  WHERE QLR.QLRMC='").append(qlrmc)
			.append("' and qlr.zjh='").append(qlrzjh).append("' and zs.bdcqzh='").append(bdcqzh)
			.append("' union all SELECT DISTINCT ZS.ZSID,ZS.XMBH FROM BDCK.BDCS_ZS_XZ ZS INNER JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.XMBH=ZS.XMBH  WHERE QLR.QLRMC='")
			.append(qlrmc).append("' and qlr.zjh='").append(zjh15or18).append("' and zs.bdcqzh='" + bdcqzh+ "'");
		} else {
			Set<Object> zjhs = new HashSet<Object>(qlrSize);
			for (int i = 0; i < qlrSize; i++) {
				zjhs.add("'" + qlrjson.getJSONObject(0).get("zjhm") + "'");
			}
			cxsql.append("SELECT DISTINCT ZS.ZSID,ZS.XMBH FROM BDCK.BDCS_ZS_XZ ZS INNER JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.XMBH=ZS.XMBH  WHERE qlr.zjh in(")
			.append(StringUtils.join(zjhs, ",") ).append( ") and zs.bdcqzh='" ).append(bdcqzh).append( "'");
		}
		return cxsql.toString();
	}

	/**获取token
	 * @param request
	 * @return
	 */
	@Override
	public String applicationToken(HttpServletRequest request) {
		String requestcode= RequestcodeEnum.TOKEN.Value;
		String decryDate = decryptRequest(request);
		if(MrpccodingEnum.FAIL.Value.equals(decryDate)){
			return resultsJson(decryDate, requestcode, null, "");
		}
		try {
			JSONObject requestDate=JSONObject.parseObject(decryDate);
			JSONObject json=new JSONObject(2);
			json.put("timeout", TIME_OUT);
			//TODO第二版加上用户，账号密码校验，未授权 41
//			json.put("token",createToken(requestDate.get("username"),requestDate.get("appcode")));
			json.put("token", JwtUtils.createToken(StringHelper.formatObject(requestDate.get("username")), StringHelper.formatObject(requestDate.get("appcode")), new Date(), TIME_OUT));
			return 	RSACoder.encryptByPrivateKeyBase64(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return resultsJson(decryDate, requestcode, null, "");
		}
	}

	/**解密模块
	 * @param request
	 * @return
	 */
	public String decryptRequest(HttpServletRequest request){
		try {
			return RSACoder.decryptByPrivateKeyBase64(getHttpServletRequestDate(request));
		} catch (Exception e) {
			e.printStackTrace();
			return MrpccodingEnum.FAIL.Value;
		}
	}
	/**获取接口请求IP地址
	 * @param request
	 * @return
	 */
	public  String getRemortIP(HttpServletRequest request) { 
	    if (request.getHeader("x-forwarded-for") == null) { 
	        return request.getRemoteAddr(); 
	    } 
	    return request.getHeader("x-forwarded-for"); 
	}
	
	/**业务进度查询
	 * 兼容内网业务流水号
	 * @param ywlsh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray getDjywjdxxJSONArray(String ywlsh){
		 //使用UNION ALL是因为内外网流水号不等，不需要去重
		List<Map> xmxxs = baseCommonDao.getDataListByFullSql("SELECT XM.YWLSH,PRO.PROJECT_NAME,PRO.PRODEF_ID,PRO.PROINST_ID FROM BDCK.BDCS_XMXX XM " 
                +" INNER JOIN (SELECT PROJECT_NAME,PRODEF_ID,PROINST_ID,FILE_NUMBER FROM BDC_WORKFLOW.WFI_PROINST WHERE PROLSH='"+ywlsh
                +"'UNION ALL SELECT PROJECT_NAME,PRODEF_ID,PROINST_ID,FILE_NUMBER FROM BDC_WORKFLOW.WFI_PROINST WHERE WLSH='"+ywlsh+"') PRO ON XM.PROJECT_ID = PRO.FILE_NUMBER ");
        if(xmxxs.isEmpty()) {
        	return null;
        }
        //理论上只会有一个项目的信息
        JSONArray jsonArray=new JSONArray(xmxxs.size());
        for(Map xmxx : xmxxs) {
            String prodefid = StringHelper.formatObject(xmxx.get("PRODEF_ID"));
            String proinstid = StringHelper.formatObject(xmxx.get("PROINST_ID"));
            //查出所有环节，路由，当前环节及已经进行了的环节
            List<Map> wfd_actdef = baseCommonDao.getDataListByFullSql("SELECT t.ACTDEF_ID,t.ACTDEF_NAME FROM BDC_WORKFLOW.WFD_ACTDEF t WHERE PRODEF_ID='"+prodefid+"' AND t.ACTDEF_TYPE<>'3020' ");
            List<Map> wfd_route = baseCommonDao.getDataListByFullSql("SELECT t.ACTDEF_ID,t.NEXT_ACTDEF_ID FROM BDC_WORKFLOW.WFD_ROUTE t WHERE PRODEF_ID='"+prodefid+"'");
            List<Map> actinst = baseCommonDao.getDataListByFullSql("SELECT t.ACTINST_ID,t.ACTDEF_ID,t.STAFF_NAME,t.ACTINST_END,t.ACTINST_START FROM BDC_WORKFLOW.WFI_ACTINST t WHERE PROINST_ID='"+proinstid+"' order by t.ACTINST_START asc");

            //第一个环节，也为循环里的当前环节
            String actdefid = StringHelper.formatObject(actinst.get(0).get("ACTDEF_ID"));
            //当前环节
            String nowactdefid = StringHelper.formatObject(actinst.get(actinst.size()-1).get("ACTDEF_ID"));
            //环节顺序
            int index = 0;
            //是否还存在下一个环节
            boolean flag = true;
            while (flag) {
            	JSONObject json=new JSONObject(7);
            	json.put("ywlsh", xmxx.get("YWLSH"));
                for(Map actdef : wfd_actdef) {
                    String thisactdefid = StringHelper.formatObject(actdef.get("ACTDEF_ID"));
                    if(thisactdefid.equals(actdefid)){
                        json.put("actdef_name", actdef.get("ACTDEF_NAME"));
                        if(!thisactdefid.equals(nowactdefid)){
                        	//待进行
                            json.put("actinsttype", 2);
                        }
                        for(Map act : actinst) {
                            //如果已经办理了，则加入转出时间，受理时间，办理人
                            if(thisactdefid.equals(act.get("ACTDEF_ID"))) {
                                json.put("blry", act.get("STAFF_NAME"));
                                json.put("actinst_start", act.get("ACTINST_START"));
                                json.put("actinst_end", act.get("ACTINST_END"));
                                //结束
                                json.put("actinsttype", 3);
                            }
                        }
                        if(thisactdefid.equals(nowactdefid)){
                            //如果为当前环节，加个标识
                            json.put("actinsttype", 1);
                        }
                        json.put("sxh", ++index);
                        break;
                    }
                }
                //假设没有下一环节了
                flag = false;
                for(Map route : wfd_route) {
                    String thisact = StringHelper.formatObject(route.get("ACTDEF_ID"));
                    String nextact = StringHelper.formatObject(route.get("NEXT_ACTDEF_ID"));
                    if(thisact.equals(actdefid)){
                        flag = true;
                        actdefid = nextact;
                        break;
                    }

                }
                jsonArray.add(json);
            }
        }
		return jsonArray;
	}
	@SuppressWarnings("rawtypes")
	public JSONArray getDjywjdxx(String ywlsh){
		 //使用UNION ALL是因为内外网流水号不等，不需要去重
		List<Map> xmxxs = baseCommonDao.getDataListByFullSql("SELECT XM.YWLSH,PRO.PROJECT_NAME,PRO.PRODEF_ID,PRO.PROINST_ID FROM BDCK.BDCS_XMXX XM " 
                +" INNER JOIN (SELECT PROJECT_NAME,PRODEF_ID,PROINST_ID,FILE_NUMBER FROM BDC_WORKFLOW.WFI_PROINST WHERE PROLSH='"+ywlsh
                +"'UNION ALL SELECT PROJECT_NAME,PRODEF_ID,PROINST_ID,FILE_NUMBER FROM BDC_WORKFLOW.WFI_PROINST WHERE WLSH='"+ywlsh+"') PRO ON XM.PROJECT_ID = PRO.FILE_NUMBER ");
        if(xmxxs.isEmpty()) {
        	return null;
        }
      //  List<Map> actinst = baseCommonDao.getDataListByFullSql("select row_number() over (order by t.ACTINST_START) SXH,'"+xmxxs.get(0).get("YWLSH")+"' YWLSH, decode(t.actdef_type,'1010','1','2010','2','5010','3') ACTINSTTYPE,t.STAFF_NAME BLRY,t.ACTINST_END,t.ACTINST_START,t.ACTINST_NAME  from BDC_WORKFLOW.WFI_ACTINST t  WHERE PROINST_ID='"+xmxxs.get(0).get("PROINST_ID")+"'");
        List<Map> actinst = baseCommonDao.getDataListByFullSql("select row_number() over (order by t.ACTINST_START) SXH,'"+xmxxs.get(0).get("YWLSH")+"' YWLSH, t.ACTINST_STATUS ACTINSTTYPE,t.STAFF_NAME BLRY,t.ACTINST_END,t.ACTINST_START,t.ACTINST_NAME  from BDC_WORKFLOW.WFI_ACTINST t  WHERE PROINST_ID='"+xmxxs.get(0).get("PROINST_ID")+"'");
		return JSONArray.parseArray(JSONObject.toJSONString(actinst));
	}
	
	/**
	 * 获取内外网业务流水号
	 * 
	 * @param wlsh 外网业务流水号,目前以S开头
	 * @return 下标0为内网流水号，1为外网流水号
	 */
	@SuppressWarnings("rawtypes")
	public String[] getYWLSH(String wlsh) {
		String[] ywlshs={wlsh,""};
		if (wlsh.startsWith("S")||wlsh.startsWith("D")) {
			List<Map> nw_ywlsh = baseCommonDao.getDataListByFullSql("SELECT t.prolsh,t.wlsh FROM BDC_WORKFLOW.WFI_PROINST WHERE PROLSH='"+wlsh+"'UNION ALL SELECT t.prolsh,t.wlsh FROM BDC_WORKFLOW.WFI_PROINST WHERE WLSH='"+wlsh+"'");
			if (null == nw_ywlsh || nw_ywlsh.size() == 0) {
				return ywlshs;
			} else {
				String[] ywlshAndWlsh={StringHelper.formatObject(nw_ywlsh.get(0).get("PROLSH")), StringHelper.formatObject(nw_ywlsh.get(0).get("WLSH"))};
				return ywlshAndWlsh;
			}
		}
		return ywlshs;
	}
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public JSONObject ZD_JSONArray(JSONArray qlrjson, Object bdcdyh,Object jgdm) throws Exception {
		JSONObject finaljson = new JSONObject();
		if (StringHelper.isEmpty(bdcdyh)) {
			finaljson.put("code", MrpccodingEnum.REQUIRED.Value);
			finaljson.put("msg", "不动产权证号/证明不能为空");
			return finaljson;
		}
		if (StringHelper.isEmpty(jgdm)) {
			finaljson.put("code", MrpccodingEnum.REQUIRED.Value);
			finaljson.put("msg", "机构代码不能为空");
			return finaljson;
		}
		StringBuffer cxsql=new StringBuffer(256);
		int qlrSize = qlrjson.size();
		if (1 == qlrSize) {
			String qlrmc = StringHelper.formatObject(qlrjson.getJSONObject(0).get("qlrmc"));
			String qlrzjh = StringHelper.formatObject(qlrjson.getJSONObject(0).get("zjhm"));
			// 查出所有权：FDCQ4,6,8 ;预告4;抵押23;查封异议99;
			cxsql.append("SELECT QL.DJDYID,QL.QLLX,QLR.ZJH,QLR.QLRMC,QLR.DH,QLR.DZ,QLR.ZJZL,QLR.BDCQZH,QLR.GYFS FROM bdck.bdcs_djdy_xz djdy inner join BDCK.BDCS_QL_XZ QL on djdy.djdyid = ql.djdyid INNER JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE QL.QLLX IN(1,2,3,4,5,6,7,8,23,99) AND QLR.QLRMC='")
				 .append(qlrmc).append("' and QLR.ZJH='").append(qlrzjh).append("' and DJDY.BDCDYH='").append(bdcdyh).append("'");
			// 如果是身份证，进行转换新旧证件号关联查询
			boolean b = ConverIdCard.checkIDCard(qlrzjh);
			if (b) {
				String zjh15or18 = 18 == qlrzjh.length() ? ConverIdCard.getOldIDCard(qlrzjh): ConverIdCard.getNewIDCard(qlrzjh);
				cxsql.append(" UNION SELECT QL.DJDYID,QL.QLLX,QLR.ZJH,QLR.QLRMC,QLR.DH,QLR.DZ,QLR.ZJZL,QLR.BDCQZH,QLR.GYFS FROM bdck.bdcs_djdy_xz djdy inner join BDCK.BDCS_QL_XZ QL on djdy.djdyid = ql.djdyid INNER JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE QL.QLLX IN(1,2,3,4,5,6,7,8,23,99) AND QLR.QLRMC='")
						.append(qlrmc).append("' and QLR.ZJH='").append(zjh15or18).append("' and DJDY.BDCDYH='").append(bdcdyh).append("'");
			}
		} else {
			Set<Object> zjhs = new HashSet<Object>(qlrSize);
			for (int i = 0; i < qlrSize; i++) {
				zjhs.add("'" + qlrjson.getJSONObject(i).get("zjhm") + "'");
			}
			cxsql.append("SELECT QL.DJDYID,QL.QLLX,QLR.ZJH,QLR.QLRMC,QLR.DH,QLR.DZ,QLR.ZJZL,QLR.BDCQZH,QLR.GYFS FROM BDCK.BDCS_QL_XZ QL INNER JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE QL.QLLX IN(1,2,3,4,5,6,7,8,23,99) AND QLR.ZJH IN(")
				 .append(StringUtils.join(zjhs, ",")).append(") and DJDY.BDCDYH='").append(bdcdyh).append("'");
		}
		List<Map> _list_QL = baseCommonDao.getDataListByFullSql(cxsql.toString());
		if(_list_QL==null||_list_QL.isEmpty()){
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "获取权利信息失败");
			return finaljson;
		}
		String qllx = StringHelper.formatObject(_list_QL.get(0).get("QLLX"));
		Set<Object> djdyid_List = new HashSet<Object>(_list_QL.size());
		for (Map djdyids : _list_QL) {
			djdyid_List.add("'" + djdyids.get("DJDYID") + "'");
		}
		if (djdyid_List.isEmpty()) {
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "获取登记单元ID失败");
			return finaljson;
		}
		String _temp_DJDYID = StringUtils.join(djdyid_List, ",");
		// 登记单元
		List<Map> _listDJDY = baseCommonDao.getDataListByFullSql(" select distinct BDCDYID from BDCK.BDCS_DJDY_XZ where DJDYID in("+_temp_DJDYID+ ")");
		if (_listDJDY == null || _listDJDY.isEmpty()) {
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "获取登记单元失败");
			return finaljson;
		}
		Set<Object> bdcdyid_List = new HashSet<Object>(_listDJDY.size());
		for (Map bdcdyids : _listDJDY) {
				bdcdyid_List.add("'" + bdcdyids.get("BDCDYID") + "'");
		}
		String _temp_BDCDYID= StringUtils.join(bdcdyid_List, ",");
		Map<String, Object> dataALLMap = new HashMap<String, Object>(GETTABLES.length);
		Map<String, Object> putwheresql = new HashMap<String, Object>(6);
		//查询语句从where后面写起,1个单元多本证时只显示传进来的bdcqzh
		putwheresql.put("FDCQ", "H.BDCDYID IN(" + _temp_BDCDYID + ") ");
		putwheresql.put("JSYDSYQ", "ZD.BDCDYID IN(" +_temp_BDCDYID+ ")  ");
		putwheresql.put("TDYTLIST", "TDYT.BDCDYID IN(select zdbdcdyid from (select zdbdcdyid from bdck.bdcs_h_xz hxz where hxz.bdcdyid in("+_temp_BDCDYID+") union all select zdbdcdyid from bdck.bdcs_h_xzy hxzy where hxzy.bdcdyid in("+_temp_BDCDYID+") )) ");
		putwheresql.put("YGDJ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		if ("23".equals(qllx)) {
			putwheresql.put("DYAQ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ")");
		} else {
			putwheresql.put("DYAQ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		}
		putwheresql.put("CFDJ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		putwheresql.put("YYDJ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		// 上面加上权证号过滤在分别持证的情况就查不出所有对应的数据了，所以去掉
		putwheresql.put("QLRLIST", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") AND QL_XZ.QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18') ");
		getResyltConfigInfo(jgdm.toString());
		String[] node_tables;
		try {
			node_tables = (String[]) GETTABLES_OTHER.get(jgdm);
			if (node_tables.length < 1) {
				finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
				finaljson.put("msg", "未获取到该机构配置信息，请联系中心维护人员核查");
				return finaljson;
			}
		} catch (Exception e) {
			e.printStackTrace();
			finaljson.put("code", MrpccodingEnum.OTHERERRORS.Value);
			finaljson.put("msg", "获取到该机构配置信息失败，请联系中心维护人员核查");
			return finaljson;
		}
		Map<String, Map<String, List<String>>> _WHOSELECTSQL = WHOSELECTSQL_OTHER.get(jgdm);
		for (String tablename : node_tables) {
			String nondes_tablename = tablename.toUpperCase();
			String _sql_final = SQLEnum.valueOf(nondes_tablename).getSQL(
					StringUtils.join(_WHOSELECTSQL.get(nondes_tablename).get("ALLField").toArray(), ","),
					formatLeftjoinSQL(_WHOSELECTSQL.get(nondes_tablename).get("leftjoinSQL")), putwheresql,
					formatExtraSql(_WHOSELECTSQL.get(nondes_tablename).get("extraSql")));
			dataALLMap.put(nondes_tablename, baseCommonDao.getDataListByFullSql(_sql_final));
		}

		finaljson.put("cqzlx", "");
		finaljson.put("bdcdyh", bdcdyh);
		//翻译成文档对应的名称
//		List<Map> zdMaps=translate(jgdm,"FDCQ",dataALLMap);
//		for (Map zdMap:zdMaps) {
//			zdMap.put("cflist", translate(jgdm,"CFDJ",dataALLMap));
//			zdMap.put("dylist", translate(jgdm,"DYAQ",dataALLMap));
//			zdMap.put("yylist", translate(jgdm,"YYDJ",dataALLMap));
//		}
		List<Map> landMaps=translate(jgdm,"JSYDSYQ",dataALLMap);
		for (Map landMap:landMaps) {
			landMap.put("cflist", translate(jgdm,"CFDJ",dataALLMap));
			landMap.put("dylist", translate(jgdm,"DYAQ",dataALLMap));
			landMap.put("yylist", translate(jgdm,"YYDJ",dataALLMap));
			landMap.put("tdytlist", translate(jgdm,"TDYTLIST",dataALLMap));
		}
//		zdMaps.addAll(landMaps);
		finaljson.put("zdlist",landMaps);
		finaljson.put("qlrlist", translate(jgdm,"QLRLIST",dataALLMap));
		return finaljson;
	}

	/**获取房屋权属信息
	 * @param qlrjson
	 * @param bdcqzh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public JSONObject FW_JSONArray(JSONArray qlrjson, Object bdcqzh,Object jgdm,Object username) throws Exception {
		JSONObject finaljson = new JSONObject();
		if (StringHelper.isEmpty(bdcqzh)) {
			finaljson.put("code", MrpccodingEnum.REQUIRED.Value);
			finaljson.put("msg", "合同号不能为空");
			return finaljson;
		}
		if (StringHelper.isEmpty(jgdm)) {
			finaljson.put("code", MrpccodingEnum.REQUIRED.Value);
			finaljson.put("msg", "机构代码不能为空");
			return finaljson;
		}
		//先通过合同号、买方姓名、买方证件号去交易库读取房屋及买受人信息
		int qlrSize = qlrjson.size();
		String qlrmc = StringHelper.formatObject(qlrjson.getJSONObject(0).get("qlrmc"));
		String qlrzjh = StringHelper.formatObject(qlrjson.getJSONObject(0).get("zjhm"));
		StringBuffer jycxsql=new StringBuffer(256);
		//通过合同号抽取合同相关买受人、产权交易
		jycxsql.append("SELECT DISTINCT QLR.QLRMC,QLR.ZJZL,QLR.ZJH,QLR.DH,QLR.QLRLX,QLR.SQRLB,QL.FDCJYJG,XM.CASENUM ,H.RELATIONID FROM GXJYK.QLR  QLR INNER JOIN GXJYK.GXJHXM  XM ON QLR.GXXMBH = XM.GXXMBH INNER JOIN GXJYK.FDCQ QL ON QL.GXXMBH = XM.GXXMBH INNER JOIN GXJYK.H ON H.GXXMBH=XM.GXXMBH WHERE XM.CASENUM = '"+bdcqzh+"'AND XM.DJDL='200' AND QLR.SQRLB = '1' ");
		List<Map> fcinfos = baseCommonDaoJY.getDataListByFullSql(jycxsql.toString());
		if(fcinfos==null || fcinfos.size()<=0) {
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "相关备案信息未推送或不存在");
			return finaljson;
		}
		//核验买受人
		int qlrcount=0;
		//新旧身份证转换
		boolean b = ConverIdCard.checkIDCard(qlrzjh);
		String zjh15or18 = qlrzjh;
		if(b) {
			 zjh15or18 = 18 == qlrzjh.length() ? ConverIdCard.getOldIDCard(qlrzjh): ConverIdCard.getNewIDCard(qlrzjh);
		}
		for(Map fcinfo:fcinfos) {
			if((!StringHelper.isEmpty(fcinfo.get("QLRMC")))&&StringHelper.formatObject(fcinfo.get("QLRMC")).equals(qlrmc)&&(!StringHelper.isEmpty(fcinfo.get("ZJH")))&&(StringHelper.formatObject(fcinfo.get("ZJH")).equals(zjh15or18) || StringHelper.formatObject(fcinfo.get("ZJH")).equals(qlrzjh))) {
				qlrcount++;
			}
		}
		if(qlrcount<=0) {
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "买受人信息核验失败");
			return finaljson;
		}
		
		
		StringBuffer cxsql=new StringBuffer(256);
		if (1 == qlrSize) {
			// 查出所有权：FDCQ4,6,8 ;预告4;抵押23;查封异议99;
			cxsql.append("SELECT QL.DJDYID,QL.QLLX,QLR.ZJH,QLR.QLRMC,QLR.DH,QLR.DZ,QLR.ZJZL,QLR.BDCQZH,QLR.GYFS FROM bdck.bdcs_djdy_xz djdy inner join BDCK.BDCS_QL_XZ QL on djdy.djdyid = ql.djdyid INNER JOIN BDCK.BDCS_H_XZ H ON djdy.bdcdyid = H.bdcdyid INNER JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE djdy.bdcdylx in('031','032','06') and QL.QLLX IN(4,6,8) AND H.RELATIONID='"+fcinfos.get(0).get("RELATIONID")+"'");
			// 如果是身份证，进行转换新旧证件号关联查询
//			boolean b = ConverIdCard.checkIDCard(qlrzjh);
//			if (b) {
//				String zjh15or18 = 18 == qlrzjh.length() ? ConverIdCard.getOldIDCard(qlrzjh): ConverIdCard.getNewIDCard(qlrzjh);
//				cxsql.append(" UNION SELECT QL.DJDYID,QL.QLLX,QLR.ZJH,QLR.QLRMC,QLR.DH,QLR.DZ,QLR.ZJZL,QLR.BDCQZH,QLR.GYFS FROM bdck.bdcs_djdy_xz djdy inner join BDCK.BDCS_QL_XZ QL on djdy.djdyid = ql.djdyid INNER JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE djdy.bdcdylx in('031','032','06') and QL.QLLX IN(4,6,8,23,99) AND QLR.QLRMC='")
//						.append(qlrmc).append("' and QLR.ZJH='").append(zjh15or18).append("' and QLR.BDCQZH='").append(bdcqzh).append("'");
//			}
		} else {
			Set<Object> zjhs = new HashSet<Object>(qlrSize);
			for (int i = 0; i < qlrSize; i++) {
				zjhs.add("'" + qlrjson.getJSONObject(i).get("zjhm") + "'");
			}
			cxsql.append("SELECT QL.DJDYID,QL.QLLX,QLR.ZJH,QLR.QLRMC,QLR.DH AS LXDH,QLR.DZ,QLR.ZJZL,QLR.BDCQZH,QLR.GYFS FROM BDCK.BDCS_QL_XZ QL INNER JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE QL.QLLX IN(1,2,3,4,5,6,7,8,23,99) AND QLR.ZJH IN(")
				 .append(StringUtils.join(zjhs, ",")).append(") and QLR.BDCQZH='").append(bdcqzh).append("'");
		}
		List<Map> _list_QL = baseCommonDao.getDataListByFullSql(cxsql.toString());
		if(_list_QL==null||_list_QL.isEmpty()){
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "获取权利信息失败");
			return finaljson;
		}
		String qllx = StringHelper.formatObject(_list_QL.get(0).get("QLLX"));
		Set<Object> djdyid_List = new HashSet<Object>(_list_QL.size());
		for (Map djdyids : _list_QL) {
			djdyid_List.add("'" + djdyids.get("DJDYID") + "'");
		}
		if (djdyid_List.isEmpty()) {
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "获取登记单元ID失败");
			return finaljson;
		}
		String _temp_DJDYID = StringUtils.join(djdyid_List, ",");
		// 登记单元(判断zrz)
		//  查询出自然幢的不动产单元ID
		String userid = "";
		List<SYS_USER> sysuser = baseCommonDaoMRPC.getDataList(SYS_USER.class, "username='"+username+"'");
		if(sysuser!=null&&sysuser.size()>0){
			userid = sysuser.get(0).getID();
		}
		List<Map> _listZRZBDCDY = baseCommonDaoMRPC.getDataListByFullSql(" SELECT DISTINCT ZRZ.BDCDYID AS BDCDYID FROM BDC_MRPC.BDC_ZRZ ZRZ LEFT JOIN BDC_MRPC.SYS_USER_ZRZ userzrz on zrz.id = userzrz.zrz_id WHERE userzrz.user_id ='"+userid+"' ");
		//将自然幢不动产单元ID集合转化为查询String
		Set<Object> zrzbdcdyid_List = new HashSet<Object>(_listZRZBDCDY.size());
		for (Map zrzbdcdyids : _listZRZBDCDY) {
				zrzbdcdyid_List.add("'" + zrzbdcdyids.get("BDCDYID") + "'");
		}
		String _temp_ZRZBDCDYID= StringUtils.join(zrzbdcdyid_List, ",");
		List<Map> _listDJDY = baseCommonDao.getDataListByFullSql(" select distinct DJDY.BDCDYID from BDCK.BDCS_DJDY_XZ  DJDY LEFT JOIN BDCK.BDCS_H_XZ  H ON  H.BDCDYID = DJDY.BDCDYID where DJDY.DJDYID in ("+_temp_DJDYID+ ") AND H.ZRZBDCDYID IN ("+_temp_ZRZBDCDYID+") ");
//		List<Map> _listDJDY = baseCommonDao.getDataListByFullSql(" select distinct DJDY.BDCDYID from BDCK.BDCS_DJDY_XZ  DJDY LEFT JOIN BDCK.BDCS_H_XZ  H ON  H.BDCDYID = DJDY.BDCDYID where DJDY.DJDYID in ("+_temp_DJDYID+ ")  ");
		if (_listDJDY == null || _listDJDY.isEmpty()) {
			finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
			finaljson.put("msg", "获取登记单元失败");
			return finaljson;
		}
		Set<Object> bdcdyid_List = new HashSet<Object>(_listDJDY.size());
		for (Map bdcdyids : _listDJDY) {
				bdcdyid_List.add("'" + bdcdyids.get("BDCDYID") + "'");
		}
		String _temp_BDCDYID= StringUtils.join(bdcdyid_List, ",");
		Map<String, Object> dataALLMap = new HashMap<String, Object>(GETTABLES.length);
		Map<String, Object> putwheresql = new HashMap<String, Object>(6);
		//查询语句从where后面写起,1个单元多本证时只显示传进来的bdcqzh
		putwheresql.put("FDCQ", "H.BDCDYID IN(" + _temp_BDCDYID + ") ");
		putwheresql.put("JSYDSYQ", "ZD.BDCDYID IN(" +_temp_BDCDYID+ ")  ");
		putwheresql.put("TDYTLIST", "TDYT.BDCDYID IN(select zdbdcdyid from (select zdbdcdyid from bdck.bdcs_h_xz hxz where hxz.bdcdyid in("+_temp_BDCDYID+") union all select zdbdcdyid from bdck.bdcs_h_xzy hxzy where hxzy.bdcdyid in("+_temp_BDCDYID+") )) ");
		putwheresql.put("YGDJ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		if ("23".equals(qllx)) {
			putwheresql.put("DYAQ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") AND QLR_XZ.BDCQZH='" + bdcqzh + "'");
		} else {
			putwheresql.put("DYAQ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		}
		putwheresql.put("CFDJ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		putwheresql.put("YYDJ", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") ");
		// 上面加上权证号过滤在分别持证的情况就查不出所有对应的数据了，所以去掉
		putwheresql.put("QLRLIST", "QL_XZ.DJDYID IN (" + _temp_DJDYID + ") AND QL_XZ.QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18') ");
		getResyltConfigInfo(jgdm.toString());
		String[] node_tables;
		//添加上手权利的使用权起止时间,权证号
		String cqsql = "SELECT QLJSSJ,QLQSSJ ,BDCQZH FROM BDCK.BDCS_QL_XZ WHERE DJDYID IN (" + _temp_DJDYID + ") ";
		List<Map>  cqlist = baseCommonDao.getDataListByFullSql(cqsql);
		try {
			node_tables = (String[]) GETTABLES_OTHER.get(jgdm);
			if (node_tables.length < 1) {
				finaljson.put("code", MrpccodingEnum.NOINFORMATION.Value);
				finaljson.put("msg", "未获取到该机构配置信息，请联系中心维护人员核查");
				return finaljson;
			}
		} catch (Exception e) {
			e.printStackTrace();
			finaljson.put("code", MrpccodingEnum.OTHERERRORS.Value);
			finaljson.put("msg", "获取到该机构配置信息失败，请联系中心维护人员核查");
			return finaljson;
		}
		Map<String, Map<String, List<String>>> _WHOSELECTSQL = WHOSELECTSQL_OTHER.get(jgdm);
		for (String tablename : node_tables) {
			String nondes_tablename = tablename.toUpperCase();
			String _sql_final = SQLEnum.valueOf(nondes_tablename).getSQL(
					StringUtils.join(_WHOSELECTSQL.get(nondes_tablename).get("ALLField").toArray(), ","),
					formatLeftjoinSQL(_WHOSELECTSQL.get(nondes_tablename).get("leftjoinSQL")), putwheresql,
					formatExtraSql(_WHOSELECTSQL.get(nondes_tablename).get("extraSql")));
			dataALLMap.put(nondes_tablename, baseCommonDao.getDataListByFullSql(_sql_final));
		}

		finaljson.put("cqzlx", "");
		
		finaljson.put("bcxzmh", translate(jgdm,"QLRLIST",dataALLMap).get(0).get("BDCQZH"));
		//翻译成文档对应的名称
		List<Map> houseMaps=translate(jgdm,"FDCQ",dataALLMap);
		for (Map houseMap:houseMaps) {
			houseMap.put("cflist", translate(jgdm,"CFDJ",dataALLMap));
			houseMap.put("dylist", translate(jgdm,"DYAQ",dataALLMap));
			houseMap.put("yylist", translate(jgdm,"YYDJ",dataALLMap));
		}
		List<Map> landMaps=translate(jgdm,"JSYDSYQ",dataALLMap);
		for (Map landMap:landMaps) {
			landMap.put("cflist", translate(jgdm,"CFDJ",dataALLMap));
			landMap.put("dylist", translate(jgdm,"DYAQ",dataALLMap));
			landMap.put("yylist", translate(jgdm,"YYDJ",dataALLMap));
			landMap.put("tdytlist", translate(jgdm,"TDYTLIST",dataALLMap));
		}
		houseMaps.addAll(landMaps);
		finaljson.put("houselist",houseMaps);
		finaljson.put("qlrlist", translate(jgdm,"QLRLIST",dataALLMap));
		finaljson.put("msrlist",fcinfos);
		finaljson.put("cqlist", cqlist);
		return finaljson;
	}	
	
	public String formatLeftjoinSQL(List<String> leftjoinSQL) {
		return leftjoinSQL == null || leftjoinSQL.size() == 0 ? "" : leftjoinSQL.get(0);
	}

	public String formatExtraSql(List<String> extraSql) {
		if (extraSql.size() > 1) {
			return StringUtils.join(extraSql, ",");
		}
		return extraSql == null || extraSql.size() == 0 ? "" : extraSql.get(0);
		
	}

	public JSONArray getBdcqzh(String bdcqzh){
		List<Map> listBdcqzh = new ArrayList<Map>();
		if(!StringHelper.isEmpty(bdcqzh)){
			listBdcqzh = baseCommonDao.getDataListByFullSql(" select distinct BDCQZH from BDCK.BDCS_QLR_XZ where  instr(to_char(BDCQZH),'"+bdcqzh+"')>0");
		}
		return JSONArray.parseArray(JSONObject.toJSONString(listBdcqzh));
	}

	
	public JSONArray getBdcdyh(String bdcdyh){
		List<Map> listBdcqzh = new ArrayList<Map>();
		if(!StringHelper.isEmpty(bdcdyh)){
			listBdcqzh = baseCommonDao.getDataListByFullSql(" select distinct BDCDYH from BDCK.BDCS_ShYQZD_XZ where  instr(to_char(BDCDYH),'"+bdcdyh+"')>0");
		}
		return JSONArray.parseArray(JSONObject.toJSONString(listBdcqzh));
	}

	/**内部使用，未加密处理
	 * @param 
	 * @param 
	 * @return
	 */
	@Override
	public JSONObject resultsDecryptJson(HttpServletRequest request){
		JSONObject resultsJson = new JSONObject(6);
		try {
			JSONObject decryptJson= JSONObject.parseObject(getHttpServletRequestDate(request));
			String requestcode = StringHelper.formatObject(decryptJson.get("requestcode"));
			JSONArray datajson = decryptJson.getJSONArray("data");
			for (int i = 0; i < datajson.size(); i++) {
				datajson.getJSONObject(i).put("jgdm", "99");
			}
			resultsJson.put("code", MrpccodingEnum.SUCCESS.Value);
			resultsJson.put("msg", MrpccodingEnum.initFrom(MrpccodingEnum.SUCCESS.Value).Name);
			resultsJson.put("requestcode", requestcode);
			resultsJson.put("requestseq", System.currentTimeMillis());
			resultsJson.put("data", queryModule(decryptJson).getData());
			resultsJson.put("extra", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultsJson;
	}
	
	@Override
	public JSONObject coreQueryAlias(HttpServletRequest request) throws Exception {
		JSONObject decryptJson= JSONObject.parseObject(getHttpServletRequestDate(request));
		String requestcode = StringHelper.formatObject(decryptJson.get("requestcode"));
		JSONObject resultsJson = new JSONObject(6);
		resultsJson.put("code", MrpccodingEnum.SUCCESS.Value);
		resultsJson.put("msg", MrpccodingEnum.initFrom(MrpccodingEnum.SUCCESS.Value).Name);
		resultsJson.put("requestcode", requestcode);
		resultsJson.put("requestseq", System.currentTimeMillis());
		resultsJson.put("data", queryModule(decryptJson).getData());
		resultsJson.put("extra", "");
		return resultsJson;
	}
	
	/**
	 * 翻译字段
	 * Map的key大写转小写
	 * @param jgdm
	 * @param notesName
	 * @param dataALLMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> translate(Object jgdm,String notesName, Map<String, Object> dataALLMap) {
		if("99".equals(jgdm)){
			List<Map> notelist = (List<Map>) dataALLMap.get(notesName);
			if (notelist.size() == 0 || notelist == null) {
				return new ArrayList<Map>();
			} else {
				List<Map> data = new ArrayList<Map>(notelist.size());
				List<String> eletexts = WHOSELECTSQL.get(notesName).get("_ALLField");
				for (Map map : notelist) {
					Map<Object, Object> map_temp = new HashMap<Object, Object>(eletexts.size());
					for (String eletext : eletexts) {
						map_temp.put(eletext.substring(eletext.lastIndexOf("_") + 1, eletext.length()).toLowerCase(), map.get(eletext));
					}
					map_temp.put("djdyid", map.get("DJDYID"));
					data.add(map_temp);
				}
				return data;
			}
		}else{
			//key大写转小写
			List<Map> dataMap=(List<Map>) dataALLMap.get(notesName);
				if(dataMap.size()>0){
					List<Map> temListMap=new ArrayList<Map>(dataMap.size());
					for (Map map : dataMap) {
						Map<Object, Object> temMap=new HashMap<Object, Object>(map.size());
						for (Object key:map.keySet()) {
							temMap.put(key.toString().toLowerCase(), map.get(key));
						}
						temListMap.add(temMap);
					}
					return	temListMap;
				}
			return dataMap;
		}
	}
	public String createToken(Object username,Object password){
		long count=	baseCommonDao.getCountByFullSql("");
		if(count>0){
		    return JwtUtils.createToken(StringHelper.formatObject(username), StringHelper.formatObject(password), new Date(), TIME_OUT);
		}
		return MrpccodingEnum.UNAUTHORIZED.Value;
		
	}
	//*********************************************单元状态限制查询模块 ↓*********************************************
	/**
	 * @Author linuo
	 * @Description 根据不动产权证号查询，存在限制的单元（查封、异议）
	 * @Date 2019-09-27 13:30
	 * @Param [parameters, currentpage, pageSize]
	 * @return JSONArray
	 **/
	public JSONArray getDyztInfo(JSONObject parameterJson) {
		JSONArray jsonArray = new JSONArray();
		String bdcqzh = parameterJson.getString("bdcqzh");
		String bdcdylx = parameterJson.getString("bdcdylx");
		String sort = parameterJson.getString("sort");
		String order = parameterJson.getString("order");
		// 类型：【0，查询；1，导出（所有）】 可扩展
		String type = parameterJson.getString("type");
		int currentpage = 0;
		int pageSize = 10;
		if (!StringHelper.isEmpty(parameterJson.getString("currentpage"))) {
			currentpage = parameterJson.getInteger("currentpage");
		}
		if (!StringHelper.isEmpty(parameterJson.getString("pageSize"))) {
			pageSize = parameterJson.getInteger("pageSize");
		}
		List<HashMap<String, String>>  map = queryService.HouseStatusQuery_QZH(bdcqzh, bdcdylx);
		jsonArray = JSONArray.parseArray(JSON.toJSONString(map));
		return jsonArray;
	}
	/**
	 * @Author linuo
	 * @Description 获取电子证照接口
	 * @Date 2019-09-27 13:30
	 * @param parameterJson
	 * @return
	 */
	public JSONArray getDzzzInfo(JSONObject parameterJson) {
		JSONArray array = new JSONArray();
		String url = GetProperties.getConstValueByKey("zfurl");
		String zjh = parameterJson.getString("zjh");
		String zzlx = parameterJson.getString("ecertcode");
		JSONObject json = new JSONObject();
		json.put("zjh", zjh);
		json.put("zzlx", zzlx);
		json.put("requestcode", ConstValue.RequestzfcodeEnum.YZ.Value);
		
		try {
			String jsonobj = HttpClientUtil.requestPost(json.toJSONString(), url);
			if(jsonobj == null) {
				return array;
			}
			String base64 = JSONObject.parseObject(jsonobj).getString("data");
			array.add(base64);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			array.add(Base64Util.encodeFile("C:\\Users\\Administrator\\Desktop\\2019体检报告.pdf"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return array;
	}
	
	public JSONArray zfjkInfo(JSONObject parameterJson) throws Exception {
		JSONArray arry = new JSONArray();
		String url = GetProperties.getConstValueByKey("zfurl");
		String data = parameterJson.getString("data");
		String zfurl = parameterJson.getString("url");
		JSONObject json = new JSONObject();
		json.put("requestcode", ConstValue.RequestzfcodeEnum.ZZ.Value);//通用接口  命名错误
		json.put("data", JSONObject.parseObject(data));
		json.put("url", zfurl);
		String result = HttpClientUtil.requestPost(json.toJSONString(), url);
		JSONObject obj  = JSONObject.parseObject(result);
		arry.add(obj);
		return arry;
	}

	//*********************************************单元限制查询模块 ↓*********************************************
	 /**
	  * @Author taochunda
	  * @Description 获取指定机构名下所有抵押单元中，存在限制的单元（查封、异议）
	  * @Date 2019-09-27 13:30
	  * @Param [parameters, currentpage, pageSize]
	  * @return com.supermap.wisdombusiness.web.Message
	  **/
	public JSONArray getLimitUnitInfo(JSONObject parameterJson) {
		JSONArray jsonArray = new JSONArray();
		try {
			String qlrmc = parameterJson.getString("qlrmc");
			String zjh = parameterJson.getString("zjh");
			if (StringHelper.isEmpty(qlrmc) || StringHelper.isEmpty(zjh)) {
				throw new Exception("未获取到机构/部门名称或部门证件号，请检查部门信息是否缺失。");
			}
			String bdcqzh = parameterJson.getString("bdcqzh");
			String dyr = parameterJson.getString("dyr");
			String slsj_q = parameterJson.getString("slsj_q");
			String slsj_z = parameterJson.getString("slsj_z");
			String sort = parameterJson.getString("sort");
			String order = parameterJson.getString("order");
			// 类型：【0，查询；1，导出（所有）】 可扩展
			String type = parameterJson.getString("type");
			int currentpage = 0;
			int pageSize = 10;
			if (!StringHelper.isEmpty(parameterJson.getString("currentpage"))) {
				currentpage = parameterJson.getInteger("currentpage");
			}
			if (!StringHelper.isEmpty(parameterJson.getString("pageSize"))) {
				pageSize = parameterJson.getInteger("pageSize");
			}

			StringBuilder fromsql = new StringBuilder(" FROM  BDCK.BDCS_QL_XZ QL ")
					.append("LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID=FSQL.QLID ")
					.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID ")
					.append("LEFT JOIN ")
					.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,GHYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ,NULL AS ZDMJ,FWTDYT AS TDYT,QLXZ,FTTDMJ FROM BDCK.BDCS_H_XZY ")
					.append("UNION  ")
					.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,GHYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ,NULL AS ZDMJ,FWTDYT AS TDYT,QLXZ,FTTDMJ FROM BDCK.BDCS_H_XZ ")
					.append("UNION  ")
					.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ , ZDMJ,YT AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SHYQZD_XZ ")
					.append("UNION  ")
					.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZDMJ,YT AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SYQZD_XZ ")
					.append(") DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
					.append("LEFT JOIN (SELECT DISTINCT WM_CONCAT(TO_CHAR(QLRMC)) AS QLRMC,WM_CONCAT(TO_CHAR(ZJH)) AS ZJH,QLID FROM BDCK.BDCS_QLR_XZ GROUP BY QLID) QLR ON QLR.QLID=QL.QLID  ")
					.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH ")
					// 只查查封、限制状态的单元
					.append("WHERE DJDY.DJDYID IN ( ")
//					.append("SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDY.DJDYID AND DYQ.QLLX='23' ")
//					.append("UNION ")
					.append("SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDY.DJDYID AND CF.QLLX='99' AND CF.DJLX='800' ")
					.append("UNION ")
					.append("SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDY.DJDYID AND YY.DJLX='600') ")
					.append("AND QL.QLLX='23' ");

			Map<String, String> newpara = new HashMap<String, String>();
			if (!StringHelper.isEmpty(bdcqzh)) {
				fromsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
				newpara.put("BDCQZH", "%" + bdcqzh + "%");
			}
			if (!StringHelper.isEmpty(dyr)) {
				fromsql.append("  AND FSQL.DYR LIKE:DYR ");
				newpara.put("DYR", "%" + dyr + "%");
			}

			fromsql.append(" AND QLR.QLRMC =:QLRMC ");
			newpara.put("QLRMC", qlrmc);
			fromsql.append(" AND QLR.ZJH =:ZJH ");
			newpara.put("ZJH", zjh);

			if (!StringHelper.isEmpty(slsj_q)) {
				fromsql.append("  AND XMXX.SLSJ >= TO_DATE('" + slsj_q + " 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
			}
			if (!StringHelper.isEmpty(slsj_z)) {
				fromsql.append("  AND XMXX.SLSJ <= TO_DATE('" + slsj_z + " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
			}
			Long total = baseCommonDao.getCountByFullSql(fromsql.toString(), newpara);
			if (total == 0) {
				return jsonArray;
			}
			StringBuilder selectSql = new StringBuilder();
			selectSql
					.append(" SELECT TO_CHAR(QL.DJSJ,'YYYY-MM-DD HH24:MM:SS') AS DJSJ,DY.ZL,DY.BDCDYH,DJDY.BDCDYLX,QLR.QLRMC,QLR.ZJH,QL.BDCQZH,QL.DJLX,TO_CHAR(XMXX.SLSJ,'YYYY-MM-DD HH24:MM:SS') AS SLSJ,XMXX.YWLSH,XMXX.PROJECT_ID,")
					.append("FSQL.DYR,FSQL.DYFS,TO_CHAR(QL.QLQSSJ,'YYYY-MM-DD') AS QLQSSJ,TO_CHAR(QL.QLJSSJ,'YYYY-MM-DD') AS QLJSSJ,QL.DJYY ,DY.ZDMJ AS SYQMJ,DY.JZMJ,FSQL.DYPGJZ,DY.GHYT,DY.TDYT,DY.BDCDYID,DY.QLXZ,DY.FTTDMJ,")
					.append("NVL(FSQL.ZGZQSE,0)+NVL(FSQL.BDBZZQSE,0) AS DYE,DJDY.DJDYID ");
			String fullSql = "";
			if (!StringHelper.isEmpty(sort) && !StringHelper.isEmpty(order)) {
				fullSql = selectSql.append(fromsql).append(" ORDER BY " + sort + " " + order).toString();
			} else {
				fullSql = selectSql.append(fromsql).toString();
			}
			List<Map> result = new ArrayList<Map>();
			// 目前导出时查询所有
			if ("1".equals(type)) {
				result = baseCommonDao.getDataListByFullSql(fullSql, newpara);
			} else {
				result = baseCommonDao.getPageDataByFullSql(fullSql, newpara, currentpage, pageSize);
			}

			for (Map map : result) {
				String bdcdylx = StringHelper.formatObject(map.get("BDCDYLX"));
				String djdyid = StringHelper.formatObject(map.get("DJDYID"));
				String file_number = StringHelper.formatObject(map.get("PROJECT_ID"));
				if (!StringHelper.isEmpty(bdcdylx) && !StringHelper.isEmpty(djdyid) && !StringHelper.isEmpty(file_number)) {
					if(com.supermap.realestate.registration.util.ConstValue.BDCDYLX.H.Value.equals(bdcdylx) || com.supermap.realestate.registration.util.ConstValue.BDCDYLX.YCH.Value.equals(bdcdylx)){
						getHouseLimitInfo(map, djdyid, file_number);
					} else if (com.supermap.realestate.registration.util.ConstValue.BDCDYLX.SHYQZD.Value.equals(bdcdylx) || com.supermap.realestate.registration.util.ConstValue.BDCDYLX.SYQZD.Value.equals(bdcdylx)) {
						String bdcdyid = StringHelper.formatObject(map.get("BDCDYID"));
						if (!StringHelper.isEmpty(bdcdyid)) {
							getLandLimitInfo(map, djdyid, file_number);
							//国有土地使用权时，显示出土地上房屋状态
							addLimitZDStausByFw(map, bdcdylx, bdcdyid);
						}
					}
				}
				map.put("total", total);
				jsonArray.add(map);
			}

			/*Message msg = new Message();
			msg.setRows(result);
			msg.setTotal(total);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}

	//*********************************************房屋限制状态 ↓*********************************************

	/**
	 * 获取房屋权利限制信息：抵押、查封、异议
	 * @param djdyid
	 * @param file_number
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> getHouseLimitInfo(Map map, String djdyid, String file_number) {
		if (!com.supermap.realestate.registration.util.StringHelper.isEmpty(djdyid)) {
			String ycdjdyid = "";
			String scdjdyid = "";
			List<BDCS_DJDY_XZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"
					+ djdyid + "'");
			if (listdjdy != null && listdjdy.size() > 0) {
				com.supermap.realestate.registration.util.ConstValue.BDCDYLX lx = com.supermap.realestate.registration.util.ConstValue.BDCDYLX.initFrom(listdjdy.get(0).getBDCDYLX());
				if (lx != null) {
					if (lx.Value.equals("031")) {
						String valuedyxz = "";
						String valuecfxz = "";
						String valueyyxz = "";
						String valuexzxz = "";
						String valueygxz = "";

						String dyztcode = "0";
						String cfztcode = "0";
						String xzztcode = "0";
						String yyztcode = "0";
						String dyztcode_qf = "0";
						String cfztcode_qf = "0";
						String xzztcode_qf = "0";
						String yyztcode_qf = "0";

						List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
								"SCBDCDYID='" + listdjdy.get(0).getBDCDYID() + "'");
						if (listycsc != null && listycsc.size() > 0
								&& listycsc.get(0).getYCBDCDYID() != null) {
							List<BDCS_DJDY_XZ> listdjdyyc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
									"BDCDYID='" + listycsc.get(0).getYCBDCDYID() + "'");
							if (listdjdyyc != null && listdjdyyc.size() > 0) {
								ycdjdyid = listdjdyyc.get(0).getDJDYID();
								Map<String, String> mapxzy = new HashMap<String, String>();
								mapxzy = getDYandCFandYY_XZY(ycdjdyid);
								for (Map.Entry<String, String> ent : mapxzy.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + "、" + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + "、" + ent.getValue();
										}
									} else if (name.equals("YYZT")){
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + "、" + ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + "、" + ent.getValue();
										}
									}else if (name.equals("DYZTCODE")){
										dyztcode_qf = ent.getValue();
									}else if (name.equals("CFZTCODE")){
										cfztcode_qf = ent.getValue();
									}else if (name.equals("XZZTCODE")){
										xzztcode_qf = ent.getValue();
									}else if (name.equals("YYZTCODE")){
										yyztcode_qf = ent.getValue();
									}
								}
							}
						}
						Map<String, String> mapxz = new HashMap<String, String>();
						mapxz = getDYandCFandYY_XZ(djdyid,file_number);
						for (Map.Entry<String, String> ent : mapxz.entrySet()) {
							String name = ent.getKey();
							if (name.equals("DYZT")) {
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuedyxz)) {
									valuedyxz = ent.getValue();
								} else {
									valuedyxz = valuedyxz + " " + ent.getValue();
								}
							} else if (name.equals("CFZT")) {
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuecfxz)) {
									valuecfxz = ent.getValue();
								} else {
									valuecfxz = valuecfxz + " " + ent.getValue();
								}
							} else if (name.equals("YYZT")){
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valueyyxz)) {
									valueyyxz = ent.getValue();
								} else {
									valueyyxz = valueyyxz + " " + ent.getValue();
								}
							}else if (name.equals("YGDJZT")){
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valueyyxz)) {
									valueygxz = ent.getValue();
								} else {
									valueygxz = valueygxz + " "
											+ ent.getValue();
								}
							}else if (name.equals("XZZT")){
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuexzxz)) {
									valuexzxz = ent.getValue();
								} else {
									valuexzxz = valuexzxz + " " + ent.getValue();
								}
							}else if (name.equals("DYZTCODE")){
								dyztcode = ent.getValue();
							}else if (name.equals("CFZTCODE")){
								cfztcode = ent.getValue();
							}else if (name.equals("XZZTCODE")){
								xzztcode = ent.getValue();
							}else if (name.equals("YYZTCODE")){
								yyztcode = ent.getValue();
							}
						}
						map.put("DYZT", valuedyxz);
						map.put("CFZT", valuecfxz);
						map.put("YYZT", valueyyxz);
						map.put("XZZT", valuexzxz);
						map.put("YGDJZT", valueygxz);

						map.put("DYZTCODE", "0");
						map.put("CFZTCODE", "0");
						map.put("YYZTCODE", "0");
						map.put("XZZTCODE", "0");
						if (!"0".equals(dyztcode) || !"0".equals(dyztcode_qf)) {
							map.put("DYZTCODE", "1");
						}
						if (!"0".equals(cfztcode) || !"0".equals(cfztcode_qf)) {
							map.put("CFZTCODE", "1");
						}
						if (!"0".equals(yyztcode) || !"0".equals(yyztcode_qf)) {
							map.put("YYZTCODE", "1");
						}
						if (!"0".equals(xzztcode) || !"0".equals(xzztcode_qf)) {
							map.put("XZZTCODE", "1");
						}

					} else if (lx.Value.equals("032")) {
						List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
								"YCBDCDYID='" + listdjdy.get(0).getBDCDYID() + "'");
						String valuedyxz = "";
						String valuecfxz = "";
						String valueyyxz = "";
						String valuexzxz = "";
						String valueygxz = "";//不动产权利查档时需要查预告登记

						String dyztcode = "0";
						String cfztcode = "0";
						String xzztcode = "0";
						String yyztcode = "0";
						String dyztcode_qf = "0";
						String cfztcode_qf = "0";
						String xzztcode_qf = "0";
						String yyztcode_qf = "0";

						if (listycsc != null && listycsc.size() > 0 && listycsc.get(0).getSCBDCDYID() != null) {
							List<BDCS_DJDY_XZ> listdjdysc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
									"BDCDYID='" + listycsc.get(0).getSCBDCDYID() + "'");
							if (listdjdysc != null && listdjdysc.size() > 0) {
								scdjdyid = listdjdysc.get(0).getDJDYID();
								Map<String, String> mapxz = new HashMap<String, String>();
								mapxz = getDYandCFandYY_XZ(scdjdyid,file_number);
								for (Map.Entry<String, String> ent : mapxz.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " " + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " " + ent.getValue();
										}
									} else  if (name.equals("YYZT")) {
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " " + ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + "、" + ent.getValue();
										}
									}else if (name.equals("DYZTCODE")){
										dyztcode = ent.getValue();
									}else if (name.equals("CFZTCODE")){
										cfztcode = ent.getValue();
									}else if (name.equals("XZZTCODE")){
										xzztcode = ent.getValue();
									}else if (name.equals("YYZTCODE")){
										yyztcode = ent.getValue();
									}
								}
							}
						}
						Map<String, String> mapxzy = new HashMap<String, String>();
						mapxzy = getDYandCFandYY_XZY(djdyid);
						for (Map.Entry<String, String> ent : mapxzy.entrySet()) {
							String name = ent.getKey();
							if (name.equals("DYZT")) {
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuedyxz)) {
									valuedyxz = ent.getValue();
								} else {
									valuedyxz = valuedyxz + " " + ent.getValue();
								}
							} else if (name.equals("CFZT")) {
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuecfxz)) {
									valuecfxz = ent.getValue();
								} else {
									valuecfxz = valuecfxz + " " + ent.getValue();
								}
							} else  if (name.equals("YYZT")) {
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valueyyxz)) {
									valueyyxz = ent.getValue();
								} else {
									valueyyxz = valueyyxz + " " + ent.getValue();
								}
							}else  if (name.equals("YGDJZT")) {
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valueyyxz)) {
									valueygxz = ent.getValue();
								} else {
									valueygxz = valueygxz + " "
											+ ent.getValue();
								}
							}else if (name.equals("XZZT")){
								if (com.supermap.realestate.registration.util.StringHelper.isEmpty(valuexzxz)) {
									valuexzxz = ent.getValue();
								} else {
									valuexzxz = valuexzxz + " " + ent.getValue();
								}
							}else if (name.equals("DYZTCODE")){
								dyztcode_qf = ent.getValue();
							}else if (name.equals("CFZTCODE")){
								cfztcode_qf = ent.getValue();
							}else if (name.equals("XZZTCODE")){
								xzztcode_qf = ent.getValue();
							}else if (name.equals("YYZTCODE")){
								yyztcode_qf = ent.getValue();
							}
						}
						map.put("DYZT", valuedyxz);
						map.put("CFZT", valuecfxz);
						map.put("YYZT", valueyyxz);
						map.put("XZZT", valuexzxz);
						map.put("YGDJZT", valueygxz);

						map.put("DYZTCODE", "0");
						map.put("CFZTCODE", "0");
						map.put("YYZTCODE", "0");
						map.put("XZZTCODE", "0");
						if (!"0".equals(dyztcode) || !"0".equals(dyztcode_qf)) {
							map.put("DYZTCODE", "1");
						}
						if (!"0".equals(cfztcode) || !"0".equals(cfztcode_qf)) {
							map.put("CFZTCODE", "1");
						}
						if (!"0".equals(yyztcode) || !"0".equals(yyztcode_qf)) {
							map.put("YYZTCODE", "1");
						}
						if (!"0".equals(xzztcode) || !"0".equals(xzztcode_qf)) {
							map.put("XZZTCODE", "1");
						}
					}
				}
			}
		}
		return map;
	}

	public Map<String, String> getDYandCFandYY_XZY(String djdyid) {
		List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
		String bdcdyid = null;
		if (dy != null && dy.size() > 0) {
			bdcdyid = dy.get(0).getBDCDYID();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sqlMortgage = MessageFormat.format(
				" FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND QLLX=''23''",
				djdyid);
		String sqlSeal = MessageFormat
				.format(" FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''800'' AND QLLX=''99''",
						djdyid);
		String sqlObjection = MessageFormat
				.format("  FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''600'' ",
						djdyid);
		String sqlLimit = MessageFormat
				.format("  FROM BDCK.BDCS_DYXZ WHERE BDCDYID=''{0}'' AND YXBZ=''1'' ",
						bdcdyid);
		String qfygdj = MessageFormat
				.format("  FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''700'' ",
						djdyid);

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
		long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
		long qfygdjCount = baseCommonDao.getCountByFullSql(qfygdj)/2;//查预告登记

		String sealStatus = "";
		String mortgageStatus = "";
		String LimitStatus = "";
		String objectionStatus = "";
		String ygdjStatus = "";

		mortgageStatus = mortgageCount > 0 ? "期房已抵押" : "期房无抵押";
		sealStatus = SealCount > 0 ? "期房已查封" : "期房无查封";
		LimitStatus = LimitCount > 0 ? "期房已限制" : "期房无限制";
		objectionStatus = ObjectionCount > 0 ? "期房有异议" : "期房无异议";
		ygdjStatus = qfygdjCount > 0 ? "期房已预告登记" : "期房无预告登记";

		String DYZTCODE = "";
		String CFZTCODE = "";
		String XZZTCODE = "";
		String YYZTCODE = "";
		String YGDJZTCODE = "";

		DYZTCODE = mortgageCount > 0 ? "1" : "0";
		CFZTCODE = SealCount > 0 ? "1" : "0";
		XZZTCODE = LimitCount > 0 ? "1" : "0";
		YYZTCODE = ObjectionCount > 0 ? "1" : "0";
		YGDJZTCODE = qfygdjCount > 0 ? "1" : "0";

		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(SealCount > 0)) {
			String sqlSealing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND  B.DJDYID=C.DJDYID WHERE C.DJLX='800' AND C.QLLX='99' AND C.DJDYID='"
					+ djdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlSealing);
			sealStatus = count > 0 ? "期房查封办理中" : "期房无查封";
			CFZTCODE = count > 0 ? "2" : "0";
		}

		if (!(LimitCount > 0)) {
			String sqlLimiting = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_DYXZ C ON B.XMBH=C.XMBH AND B.BDCDYID=C.BDCDYID WHERE C.YXBZ='0' AND C.BDCDYID='"
					+ bdcdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlLimiting);
			LimitStatus = count > 0 ? "期房限制办理中" : "期房无限制";
			XZZTCODE = count > 0 ? "2" : "0";
		}

		// 判断完现状层中的抵押信息，接着判断办理中的抵押信息
		if (!(mortgageCount > 0)) {
			String sqlmortgageing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND B.DJDYID=C.DJDYID WHERE C.DJLX='100' AND C.QLLX='23' AND C.DJDYID='"
					+ djdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
			mortgageStatus = count > 0 ? "期房抵押办理中" : "期房无抵押";
			DYZTCODE = count > 0 ? "2" : "0";
		}

		if (!(qfygdjCount > 0)) {
			String sqlmortgageing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND B.DJDYID=C.DJDYID WHERE C.DJLX='700' AND C.QLLX='4' AND C.DJDYID='"
					+ djdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
			ygdjStatus = count > 0 ? "期房预告登记办理中" : "期房无办理中的预告登记";
			YGDJZTCODE = count > 0 ? "2" : "0";
		}


		map.put("DYZTCODE", DYZTCODE);
		map.put("CFZTCODE", CFZTCODE);
		map.put("XZZTCODE", XZZTCODE);
		map.put("YYZTCODE", YYZTCODE);
		map.put("YGDJZTCODE", YGDJZTCODE);
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);
		map.put("XZZT", LimitStatus);
		map.put("YGDJZT", ygdjStatus);

		return map;

	}

	public Map<String, String> getDYandCFandYY_XZ(String djdyid,String file_number) {
		List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
		String bdcdyid = null;
		if (dy != null && dy.size() > 0) {
			bdcdyid = dy.get(0).getBDCDYID();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sqlMortgage = MessageFormat.format(
				" FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND QLLX=''23''",
				djdyid);
		String sqlSeal = MessageFormat
				.format(" FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''800'' AND QLLX=''99''",
						djdyid);
		String sqlObjection = MessageFormat
				.format("  FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''600'' ",
						djdyid);
		String sqlYgdj = MessageFormat
				.format("  FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''700'' ",
						djdyid);//查预告登记

		String sqlLimit = MessageFormat
				.format("  FROM BDCK.BDCS_DYXZ WHERE BDCDYID=''{0}'' AND YXBZ=''1'' ",
						bdcdyid);

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
		long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
		long ygdjCount = baseCommonDao.getCountByFullSql(sqlYgdj);

		String sealStatus = "";
		String mortgageStatus = "";
		String ygdjStatus = "";
		String LimitStatus = "";
		String objectionStatus = "";

		mortgageStatus = mortgageCount > 0 ? "现房已抵押" : "现房无抵押";
		sealStatus = SealCount > 0 ? "现房已查封" : "现房无查封";
		LimitStatus = LimitCount > 0 ? "现房已限制" : "现房无限制";
		ygdjStatus = ygdjCount > 0 ? "现房已预告登记" : "现房无预告登记";
		objectionStatus = ObjectionCount > 0 ? "现房有异议" : "现房无异议";

		String DYZTCODE = "";
		String CFZTCODE = "";
		String XZZTCODE = "";
		String YYZTCODE = "";
		String YGDJZTCODE = "";

		DYZTCODE = mortgageCount > 0 ? "1" : "0";
		CFZTCODE = SealCount > 0 ? "1" : "0";
		XZZTCODE = LimitCount > 0 ? "1" : "0";
		YYZTCODE = ObjectionCount > 0 ? "1" : "0";
		YGDJZTCODE = ygdjCount > 0 ? "1" : "0";


		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(SealCount > 0)) {
			String sqlSealing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND  B.DJDYID=C.DJDYID WHERE C.DJLX='800' AND C.QLLX='99' AND C.DJDYID='"
					+ djdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlSealing);
			sealStatus = count > 0 ? "现房查封办理中" : "现房无查封";
			CFZTCODE = count > 0 ? "2" : "0";
		}

		// 改为判断完查封 人后判断限制
		if (!(LimitCount > 0)) {
			String sqlLimiting = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_DYXZ C ON B.XMBH=C.XMBH AND B.BDCDYID=C.BDCDYID WHERE C.YXBZ='0' AND C.BDCDYID='"
					+ bdcdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlLimiting);
			LimitStatus = count > 0 ? "现房限制办理中" : "现房无限制";
			XZZTCODE = count > 0 ? "2" : "0";
		}

		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(mortgageCount > 0)) {
			String sqlmortgageing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND B.DJDYID=C.DJDYID WHERE C.DJLX='100' AND C.QLLX='23' AND C.DJDYID='"
					+ djdyid + "' AND A.SFDB='0' AND A.PROJECT_ID <> '"+file_number+"' ";
			long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
			mortgageStatus = count > 0 ? "现房抵押办理中" : "现房无抵押";
			DYZTCODE = count > 0 ? "2" : "0";
		}

		// 预告登记
		if (!(ygdjCount > 0)) {
			String sqlygdj = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND B.DJDYID=C.DJDYID WHERE C.DJLX='700' AND C.DJDYID='"
					+ djdyid + "' AND A.SFDB='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlygdj);
			ygdjStatus = count > 0 ? "现房预告登记中" : "现房无预告登记中";
			YGDJZTCODE = count > 0 ? "2" : "0";
		}

		map.put("DYZTCODE", DYZTCODE);
		map.put("CFZTCODE", CFZTCODE);
		map.put("XZZTCODE", XZZTCODE);
		map.put("YYZTCODE", YYZTCODE);
		map.put("YGDJZTCODE", YGDJZTCODE);
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);
		map.put("XZZT", LimitStatus);
		map.put("YGDJZT", ygdjStatus);

		return map;
	}

	//*********************************************房屋限制状态 ↑*********************************************

	//*********************************************土地限制状态 ↓*********************************************

	/**
	 * 获取土地权利限制信息：抵押、查封、异议
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> getLandLimitInfo(Map map,String djdyid,String file_number) {
		if (!com.supermap.realestate.registration.util.StringHelper.isEmpty(djdyid)) {
			String sqlMortgage = MessageFormat
					.format(" FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND QLLX=''23''",
							djdyid);
			String sqlSeal = MessageFormat
					.format(" FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''800'' AND QLLX=''99''",
							djdyid);
			String sqlObjection = MessageFormat
					.format("  FROM BDCK.BDCS_QL_XZ WHERE DJDYID=''{0}'' AND DJLX=''600'' ",
							djdyid);
			String sqlLimit = MessageFormat.format("  FROM BDCK.BDCS_DYXZ WHERE BDCDYID=''{0}'' AND YXBZ=''1'' ",
					map.get("BDCDYID"));
			long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
			long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
			long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
			long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);

			String mortgageStatus = mortgageCount > 0 ? "土地已抵押" : "土地无抵押";
			String sealStatus = SealCount > 0 ? "土地已查封" : "土地无查封";
			String LimitStatus = LimitCount > 0 ? "土地已限制" : "土地无限制";
			String objectionStatus = ObjectionCount > 0 ? "土地有异议" : "土地无异议";

			String DYZTCODE = "";
			String CFZTCODE = "";
			String XZZTCODE = "";
			String YYZTCODE = "";

			DYZTCODE = mortgageCount > 0 ? "1" : "0";
			CFZTCODE = SealCount > 0 ? "1" : "0";
			XZZTCODE = LimitCount > 0 ? "1" : "0";
			YYZTCODE = ObjectionCount > 0 ? "1" : "0";

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND  B.DJDYID=C.DJDYID WHERE C.DJLX='800' AND C.QLLX='99' AND C.DJDYID='"
						+ djdyid + "' AND A.SFDB='0' ";
				long count = baseCommonDao
						.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "土地查封办理中" : "土地无查封";
			}

			// 判断完现状层中的抵押信息，接着判断办理中的抵押信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_QL_GZ C ON B.XMBH=C.XMBH AND B.DJDYID=C.DJDYID WHERE C.DJLX='100' AND C.QLLX='23' AND C.DJDYID='"
						+ djdyid + "' AND A.SFDB='0' AND A.PROJECT_ID <>'"+file_number+"' ";
				long count = baseCommonDao
						.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "土地抵押办理中" : "土地无抵押";
			}

			// 改为判断完查封 后判断限制
			if (!(LimitCount > 0)) {
				String sqlLimiting = " FROM BDCK.BDCS_XMXX A LEFT JOIN BDCK.BDCS_DJDY_GZ B ON A.XMBH=B.XMBH AND A.SFDB='0' LEFT JOIN BDCK.BDCS_DYXZ C ON B.XMBH=C.XMBH AND B.BDCDYID=C.BDCDYID WHERE C.YXBZ='0' AND C.BDCDYID='"
						+ map.get("BDCDYID") + "' AND A.SFDB='0' ";
				long countxz = baseCommonDao.getCountByFullSql(sqlLimiting);
				LimitStatus = countxz > 0 ? "土地限制办理中" : "土地无限制";
			}
			map.put("DYZT", mortgageStatus);
			map.put("CFZT", sealStatus);
			map.put("YYZT", objectionStatus);
			map.put("XZZT", LimitStatus);
			map.put("DYZTCODE", DYZTCODE);
			map.put("CFZTCODE", CFZTCODE);
			map.put("YYZTCODE", YYZTCODE);
			map.put("XZZTCODE", XZZTCODE);
		}
		return map;
	}

	/**
	 * 国有土地使用权时，添加土地上房屋状态。
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> addLimitZDStausByFw(Map<String, Object> map, String bdcdylx, String zdbdcdyid) {
		long mortgageCount = 0,mortgagingCount=0;
		List<String> lstmortgage=new ArrayList<String>();
		List<String> lstmortgageing=new ArrayList<String>();
		long SealCount = 0,SealingCount=0;
		List<String> lstseal=new ArrayList<String>();
		List<String> lstsealing=new ArrayList<String>();
		long ObjectionCount = 0,ObjectioningCount=0;
		List<String> lstObjection=new ArrayList<String>();
		List<String> lstObjectioning=new ArrayList<String>();
		long LimitCount = 0,LimitingCount=0;
		long housecount = 0;
		//只有使用权宗地才有房屋
		if(com.supermap.realestate.registration.util.ConstValue.BDCDYLX.SHYQZD.Value.equals(bdcdylx) && !StringHelper.isEmpty(zdbdcdyid)){
			//已办理的业务
			StringBuilder strdealed=new StringBuilder();
			strdealed.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID FROM  ( ");
			strdealed.append("SELECT BDCDYID,'031' BDCDYLX,ZDBDCDYID FROM BDCK.BDCS_H_XZ UNION  ALL  ");
			strdealed.append("SELECT BDCDYID,'032' BDCDYLX,ZDBDCDYID FROM BDCK.BDCS_H_XZY ) DY   ");
			strdealed.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX ");
			strdealed.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
			strdealed.append("WHERE QL.QLID IS NOT NULL AND DY.ZDBDCDYID='");
			strdealed.append(zdbdcdyid).append("'");
			String dealedSql=strdealed.toString();
			List<Map> dealedmap=baseCommonDao.getDataListByFullSql(dealedSql);
			if(!com.supermap.realestate.registration.util.StringHelper.isEmpty(dealedmap) && dealedmap.size()>0){
				for(Map m : dealedmap){
					String qllx= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("QLLX"));
					String djlx= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("DJLX"));
					String bdcdyid= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("BDCDYID"));
					if(com.supermap.realestate.registration.util.ConstValue.DJLX.CFDJ.Value.equals(djlx) && com.supermap.realestate.registration.util.ConstValue.QLLX.QTQL.Value.equals(qllx)){
						if(!lstseal.contains(bdcdyid)){
							lstseal.add(bdcdyid);
							SealCount++;
						}
					}else if(com.supermap.realestate.registration.util.ConstValue.DJLX.YYDJ.Value.equals(djlx)){
						if(!lstObjection.contains(bdcdyid)){
							lstObjection.add(bdcdyid);
							ObjectionCount++;
						}
					}else if(com.supermap.realestate.registration.util.ConstValue.QLLX.DIYQ.Value.equals(qllx)){
						if(!lstmortgage.contains(bdcdyid)){
							lstmortgage.add(bdcdyid);
							mortgageCount++;
						}
					}
				}
			}
			//正在办理的业务
			StringBuilder strdealing=new StringBuilder();
			strdealing.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID,XMXX.QLLX XMQLLX,XMXX.DJLX XMDJLX  FROM  ( ");
			strdealing.append("SELECT BDCDYID,'031' BDCDYLX,ZDBDCDYID FROM BDCK.BDCS_H_XZ UNION  ALL  ");
			strdealing.append("SELECT BDCDYID,'032' BDCDYLX,ZDBDCDYID FROM BDCK.BDCS_H_XZY ) DY   ");
			strdealing.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX  ");
			strdealing.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
			strdealing.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=QL.XMBH  ");
			strdealing.append("WHERE (XMXX.SFDB IS NULL OR XMXX.SFDB<>'1') AND QL.QLID IS NOT NULL  ");
			strdealing.append("AND DY.ZDBDCDYID= '");
			strdealing.append(zdbdcdyid).append("'");
			String dealingsql=strdealing.toString();
			List<Map> dealingmap=baseCommonDao.getDataListByFullSql(dealingsql);
			if(!com.supermap.realestate.registration.util.StringHelper.isEmpty(dealingmap) && dealingmap.size()>0){
				for(Map m :dealingmap){
					String qllx= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("XMQLLX"));
					String djlx= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("XMDJLX"));
					String bdcdyid= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("BDCDYID"));
					if(com.supermap.realestate.registration.util.ConstValue.DJLX.CFDJ.Value.equals(djlx) && com.supermap.realestate.registration.util.ConstValue.QLLX.QTQL.Value.equals(qllx)){
						if(!lstseal.contains(bdcdyid) && !lstsealing.contains(bdcdyid)){
							SealingCount++;
							lstsealing.add(bdcdyid);
						}
					}else if(com.supermap.realestate.registration.util.ConstValue.DJLX.YYDJ.Value.equals(djlx)){
						if(!lstObjection.contains(bdcdyid) && !lstObjectioning.contains(bdcdyid)){
							ObjectioningCount++;
							lstObjectioning.add(bdcdyid);
						}
					}else if(com.supermap.realestate.registration.util.ConstValue.QLLX.DIYQ.Value.equals(qllx) && !com.supermap.realestate.registration.util.ConstValue.DJLX.ZXDJ.Value.equals(djlx)){
						if(!lstmortgage.contains(bdcdyid) && !lstmortgageing.contains(bdcdyid)){
							mortgagingCount++;
							lstmortgageing.add(bdcdyid);
						}
					}
				}
			}
			//限制的业务
			StringBuilder strlimit=new StringBuilder();
			strlimit.append("SELECT DYXZ.YXBZ FROM (SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZ UNION ALL  ");
			strlimit.append("SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZY ) DY ");
			strlimit.append("LEFT JOIN BDCK.BDCS_DYXZ DYXZ ON DYXZ.BDCDYID=DY.BDCDYID ");
			strlimit.append("WHERE DYXZ.ID IS NOT NULL  ");
			strlimit.append(" AND DY.ZDBDCDYID='").append(zdbdcdyid).append("'");
			String limitsql=strlimit.toString();
			List<Map> limitmap = baseCommonDao.getDataListByFullSql(limitsql);
			//商品房的土地抵消状态赋值
			if(limitmap != null && limitmap.size() > 0){
				for(Map m :limitmap){
					String yxbz= com.supermap.realestate.registration.util.StringHelper.FormatByDatatype(m.get("YXBZ"));
					if("1".equals(yxbz)){
						LimitCount++;
					}else{
						LimitingCount++;
					}
				}

			}
			String mortgageStatus =  MessageFormat.format("{0};地上房屋已抵押{1}起,正在抵押{2}起",map.get("DYZT"),mortgageCount,mortgagingCount);
			String sealStatus = MessageFormat.format("{0};地上房屋已查封{1}起,正在查封{2}起",map.get("CFZT"),SealCount,SealingCount);
			String objectionStatus = MessageFormat.format("{0};地上房屋有异议{1}起",map.get("YYZT"),ObjectionCount);
			String LimitStatus = MessageFormat.format("{0};地上房屋有限制{1}起,正在限制{2}起",map.get("XZZT"),LimitCount,LimitingCount);
			map.put("DYZT", mortgageStatus);
			map.put("CFZT", sealStatus);
			map.put("YYZT", objectionStatus);
			map.put("XZZT", LimitStatus);
		}
		return map;
	}

	//*********************************************土地限制状态 ↑*********************************************
	//*********************************************单元限制查询模块 ↑*********************************************

	public interface QueryModule{
		void run(JSONObject parameterJson, JSONArray jsonArray) throws Exception;
	}
	public class ZS implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.add(ZS_JSONArray(baseCommonDao.getDataListByFullSql(getZS_ZM_SQL(parameterJson.getJSONArray("qlr"),parameterJson.get("bdcqzh")))));
		}
	}
	public class ZM implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.add(ZM_JSONArray(baseCommonDao.getDataListByFullSql(getZS_ZM_SQL(parameterJson.getJSONArray("qlr"),parameterJson.get("bdcqzh")))));
		}
	}
	public class FW implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) throws Exception {
			jsonArray.add(FW_JSONArray(parameterJson.getJSONArray("qlr"), parameterJson.get("bdcqzh"),parameterJson.get("jgdm"),parameterJson.get("username")));
		}
	}
	public class JD implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.add(getDjywjdxxJSONArray(parameterJson.getString("ywlsh")));
		}
	}
	public class HJ implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.add(getDjywjdxx(parameterJson.getString("ywlsh")));
		}
		
	}
	public class QZ implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.addAll(getBdcqzh(parameterJson.getString("bdcqzh")));
		}

	}
	
	public class DY implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.addAll(getBdcdyh(parameterJson.getString("bdcdyh")));
		}

	}
	public class DYXZ implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.addAll(getLimitUnitInfo(parameterJson));
		}

	}
	
	public class DYZT implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.addAll(getDyztInfo(parameterJson));
		}

	}
	
	public class DZZZ implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) {
			jsonArray.addAll(getDzzzInfo(parameterJson));
		}

	}
	
	public class ZFJK implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) throws Exception {
			jsonArray.addAll(zfjkInfo(parameterJson));
		}

	}
	
	public class ZD implements QueryModule{
		@Override
		public void run(JSONObject parameterJson,JSONArray jsonArray) throws Exception {
			jsonArray.add(ZD_JSONArray(parameterJson.getJSONArray("qlr"), parameterJson.get("bdcdyh"),parameterJson.get("jgdm")));
		}
	}

}
