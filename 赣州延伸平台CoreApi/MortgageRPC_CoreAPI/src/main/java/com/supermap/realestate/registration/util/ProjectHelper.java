package com.supermap.realestate.registration.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目工具类，主要功能：获取ProjectInfo、提取流程编号、根据XMBH获取不动产单元类型等。
 * @author 刘树峰
 * @date 2015年6月12日 上午11:55:48
 * @Copyright SuperMap
 */
public class ProjectHelper {

	private static CommonDao commonDao;

	private static final Log logger = LogFactory.getLog(ProjectHelper.class);
	static {
		if (commonDao == null) {
			commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
	}

	/**
	 * 对外接口：根据项目编号获取查询条件
	 * @Title: GetXMBHCondition
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:36:59
	 * @param xmbh
	 * @return
	 */
	public static String GetXMBHCondition(String xmbh) {
		StringBuilder builder = new StringBuilder();
		builder.append(" xmbh='").append(xmbh).append("' ");
		String sql = builder.toString();
		return sql;
	}

	/**
	 * 对外接口：根据projectId获取project信息
	 * @Title: GetPrjInfoByPrjID
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:28:45
	 * @param project_id
	 * @return
	 */
	public static ProjectInfo GetPrjInfoByPrjID(String project_id) {
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
//		if (flow == null)
//			return null;
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx == null)
			return null;
		ProjectInfo info = new ProjectInfo();
		info = getProjectInfoInternal(xmxx, flow);
		return info;
	}

	/**
	 * 对外接口：根据项目编号获取project信息
	 * @Title: GetPrjInfoByXMBH
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:32:10
	 * @param xmbh
	 * @return
	 */
	public static ProjectInfo GetPrjInfoByXMBH(String xmbh) {
		// BDCS_XMXX xmxx = commonDao.get(BDCS_XMXX.class, xmbh);
		if (xmbh.equals("null")||xmbh==null||xmbh.length()<=0) {return null;}
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (xmxx == null)
			return null;
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
		if (flow == null)
			return null;
		ProjectInfo info = getProjectInfoInternal(xmxx, flow);
		return info;
	}
	
	/**
	 * 对外接口：根据actinstID获取project信息
	 
	 * @return
	 */
	public static ProjectInfo GetPrjInfoByActinstID(String actinstID) {
		List<Map> maplist=commonDao.getDataListByFullSql("SELECT XMBH FROM BDCK.BDCS_XMXX X WHERE X.YWLSH IN (" + 
				"SELECT PROLSH FROM BDC_WORKFLOW.WFI_PROINST P WHERE P.PROINST_ID IN  (SELECT PROINST_ID FROM BDC_WORKFLOW.WFI_ACTINST A WHERE A.ACTINST_ID='"+actinstID+"'))");
		if (actinstID.equals("null")||actinstID==null||actinstID.length()<=0||
				maplist==null||
						maplist.size()<=0
						||StringHelper.isEmpty(maplist.get(0).get("XMBH"))) {return null;}
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(maplist.get(0).get("XMBH").toString());
		if (xmxx == null)
			return null;
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
		if (flow == null)
			return null;
		ProjectInfo info = getProjectInfoInternal(xmxx, flow);
		return info;
	}

	/**
	 * 对外接口：调用工作流接口，获取项目信息
	 * @Title: GetProjectFromRest
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:32:37
	 * @param project_id
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static ProjectInfo GetProjectFromRest(String project_id, HttpServletRequest request) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String servername = request.getServerName();
		if ("1".equals(ConfigHelper.getNameByValue("UsedLocalIP"))) {
			servername = StringHelper.formatObject(InetAddress.getLocalHost().getHostAddress());
		}
		String basePath = request.getScheme() + "://" + servername + ":" + request.getLocalPort() + request.getContextPath() + "/";
		String jsonresult = httpGet(basePath + "app/frame/getprojectinfo/" + project_id, map);
		JSONObject object = JSON.parseObject(jsonresult);
		object.get("proInst_ID");

		if (!StringHelper.isEmpty(project_id) && project_id.equals("undefined")) {
			logger.error("project_id为undefined");
			return null;
		}

		if (StringUtils.isEmpty(project_id)) {
			logger.error("问题：project_id为空！！！！！");
			return null;
		}
		// TODO 重点修改 2015-7-31
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
		// 项目名称
		String xmmc = object.containsKey("proInst_Name") ? StringHelper.formatObject(object.get("proInst_Name")) : "";
		// 受理人员
		String slry = object.containsKey("acceptor") ? StringHelper.formatObject(object.get("acceptor")) : "";
		String djlx = "";
		String qllx = "";
		if (flow != null) {
			// 登记类型
			djlx = flow.getDjlx();
			// 权利类型
			qllx = flow.getQllx();
		}
		// 受理类型1
		// String sllx1 = usefulValue.substring(5, 7);
		// 受理类型2
		// String sllx2 = usefulValue.substring(7, 9);
		String ywlsh = "";
		if (!StringHelper.isEmpty(object.get("proLSH"))) {
			ywlsh = object.get("proLSH").toString();
		}
		String fcywh = "";
		if (!StringHelper.isEmpty(object.get("ywh"))) {
			fcywh = object.get("ywh").toString();
		}
		BDCS_XMXX xmxx = new BDCS_XMXX();
		xmxx.setSLRY(slry);
		xmxx.setSLSJ(new Date());
		// xmxx.setSLLX1(sllx1);
		// xmxx.setSLLX2(sllx2);
		xmxx.setSFDB(ConstValue.SFDB.NO.Value);
		xmxx.setYXBZ(YXBZ.有效.Value);
		xmxx.setQLLX(qllx);
		xmxx.setDJLX(djlx);
		xmxx.setPROJECT_ID(project_id);
		xmxx.setXMMC(xmmc);
		xmxx.setFCYWH(fcywh);
		xmxx.setSFHBZS(SF.NO.Value);
		// 刘树峰 2015.12.27 是否配置了默认多个单元一本证书
		// 刘树峰 2016.04.05 还得看单元类型是啥，如果是宗地，就不能多个单元一本证。
		if (flow != null) {
			if  ((BDCDYLX.H.Value.equals(flow.getUnittype()) || BDCDYLX.YCH.Value.equals(flow.getUnittype()))
					&& !DJLX.YYDJ.Value.equals(djlx)) { 
				String DEFAULTDGDYYBZ = ConfigHelper.getNameByValue("DEFAULTDGDYYBZ");
				if (!StringHelper.isEmpty(DEFAULTDGDYYBZ) && DEFAULTDGDYYBZ.equals("1")) {
					xmxx.setSFHBZS(SF.YES.Value);
				}
			}
			if(flow.getName().equals("BG211")||flow.getName().equals("BG212")||flow.getName().equals("BG213")){
				xmxx.setSFHBZS(SF.YES.Value);
			}
		}
		xmxx.setYWLSH(ywlsh);
		commonDao.save(xmxx);
		// 拷贝收费信息
		copyChargeList(xmxx);
		commonDao.flush();
		ProjectInfo info = getProjectInfoInternal(xmxx, flow);
		return info;
	}

	/**
	 * 根据流程编号拷贝收费项
	 * @Title: copyChargeList
	 * @author:liushufeng
	 * @date：2015年7月26日 上午3:46:49
	 * @param xmxx
	 */
	public static void copyChargeList(BDCS_XMXX xmxx) {
		String workflowid = getWorkflowIdByProjectID(xmxx.getPROJECT_ID());
		if (!StringHelper.isEmpty(workflowid)) {
			String hql = " id in (select distinct SFDYID from BDCS_SFRELATION WHERE PRODEF_ID='" + workflowid + "')";
			List<BDCS_SFDY> sfs = commonDao.getDataList(BDCS_SFDY.class, hql);
			if (sfs != null && sfs.size() > 0) {
				for (BDCS_SFDY sfdy : sfs) {
					BDCS_DJSF sf = new BDCS_DJSF();
					sf.setYWH(xmxx.getPROJECT_ID());
					sf.setSFDW(sfdy.getSFDW());
					sf.setSFJS(sfdy.getSFJS());
					sf.setSFLX(sfdy.getSFLX());
					sf.setSFKMMC(sfdy.getSFXLMC() + "(" + sfdy.getSFKMMC() + ")");
					sf.setXMBH(xmxx.getId());
					sf.setMJJS(sfdy.getMJJS());
					sf.setMJZL(sfdy.getMJZL());
					sf.setSFZL(sfdy.getSFZL());
					sf.setSFSX(sfdy.getZLFYSX());
					sf.setSFBL(sfdy.getSFBL());
					sf.setJFDW("元");
					sf.setSFEWSF(SF.NO.Value);
					sf.setSFDYID(sfdy.getId());
					sf.setSFDW(ConfigHelper.getNameByValue("DJJGMC"));
					sf.setJSGS(sfdy.getJSGS());
					sf.setBZ(sfdy.getBZ());
					sf.setCALTYPE(sfdy.getCALTYPE());
					sf.setSQLEXP(sfdy.getSQLEXP());
					sf.setTS(1);
					sf.setSFBMMC(sfdy.getSFBMMC());
					commonDao.save(sf);
				}
			}
		}
	}

	/**
	 * 对外接口：从project_id中提取流程编号
	 * @Title: getWorkflowCodeByProjectID
	 * @author:liushufeng
	 * @date：2015年7月17日 下午4:24:40
	 * @param project_id
	 * @return
	 */
	public static String getWorkflowCodeByProjectID(String project_id) {
		String code = "";
		// sunhb -2015-07-19 在流程编号不为NULL时再执行
		if (project_id != null) {
			if (project_id.contains("-")) {
				String[] strs = project_id.split("-");
				if (strs != null && strs.length > 0) {
					code = strs[2];
				}
			}
		}
		return code;
	}

	/**
	 * 对外接口：根据项目编号获取项目对应的不动产单元类型
	 * @Title: GetBDCDYLX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:33:56
	 * @param xmbh
	 * @return
	 */
	public static BDCDYLX GetBDCDYLX(String xmbh) {
		ProjectInfo info = GetPrjInfoByXMBH(xmbh);
		if (info == null)
			return null;
		return BDCDYLX.initFrom(info.getBdcdylx());
	}

	/**
	 * 内部方法：根据项目信息BDCS_XMXX和配置文件中的流程信息获取项目projectinfo
	 * @Title: getProjectInfoInternal
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:27:44
	 * @param xmxx
	 * @param workflow
	 * @return
	 */
	private static ProjectInfo getProjectInfoInternal(BDCS_XMXX xmxx, RegisterWorkFlow workflow) {
		ProjectInfo info = new ProjectInfo();
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
		if (flow != null) {
			info.setDjlx(flow.getDjlx());
			info.setQllx(flow.getQllx());
			info.setBdcdylx(flow.getUnittype());
			info.setSelectorname(flow.getSelector());
			info.setBaseworkflowcode(flow.getName());
			//用于“2018权利类型国标调整支持新旧枚举显示”
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date slsj = xmxx.getSLSJ();
			String slsjstr = sdf.format(slsj);
			info.setQllxmc(ConstHelper.getNameByValue_new("QLLX", flow.getQllx(),StringHelper.formatObject(slsjstr)));
			info.setDjlxmc(ConstHelper.getNameByValue("DJLX", flow.getDjlx()));
		}
		info.setProject_id(xmxx.getPROJECT_ID());
		info.setSllx1(xmxx.getSLLX1());
		info.setSllx2(xmxx.getSLLX2());
		info.setXmbh(xmxx.getId());
		info.setXmmc(xmxx.getXMMC());
		info.setSlry(xmxx.getSLRY());
		info.setSlsj(xmxx.getSLSJ());
		info.setYwlsh(xmxx.getYWLSH());
		info.setFcywh(xmxx.getFCYWH());
		info.setDjsj(xmxx.getDJSJ());
		info.setSfhbzs(xmxx.getSFHBZS() == null || xmxx.getSFHBZS().equals(SF.NO.Value) ? "0" : "1");
		//info.setCurrentuser(Global.getCurrentUserName());
		info.setWlsh(xmxx.getWLSH());
		if(!StringHelper.isEmpty(xmxx.getZDBTN())){
			info.setZdbtn(xmxx.getZDBTN());
		}else{
			info.setZdbtn(false);
		}
		if (xmxx.getSFDB().equals(SFDB.YES.Value))
			info.setReadonly(true);
		if (SF.YES.Value.equals(xmxx.getSFFBGG()))
			info.setSendNotice(true);
		info.setSfdb(xmxx.getSFDB());
		return info;
	}

	/**
	 * 内部方法：实现对REST服务的请求
	 * @Title: httpGet
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:33:38
	 * @param urlStr
	 * @param urlParam
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static String httpGet(String urlStr, Map urlParam) throws IOException {
		String urlParamStr = "?";
		if (!urlParam.isEmpty()) {
			// 定义一个迭代器，并将MAP值的集合赋值
			Iterator ups = urlParam.entrySet().iterator();
			while (ups.hasNext()) {
				Map.Entry MUPS = (Map.Entry) ups.next();
				urlParamStr += MUPS.getKey() + "=" + MUPS.getValue().toString().trim() + "&";
			}
			urlParamStr = urlParamStr.substring(0, urlParamStr.length() - 1);
		}
		// 实例一个URL资源
		URL url = new URL(urlStr + urlParamStr);
		// 实例一个HTTP CONNECT
		HttpURLConnection connet = (HttpURLConnection) url.openConnection();
		connet.setRequestProperty("Accept-Charset", "utf-8");
		connet.setRequestProperty("contentType", "utf-8");
		if (connet.getResponseCode() != 200) {
			throw new IOException(connet.getResponseMessage());
		}
		// 将返回的值存入到String中
		BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = brd.readLine()) != null) {
			sb.append(line);
		}
		brd.close();
		connet.disconnect();
		return sb.toString();
	}

	/**
	 * 获取预告不动产登记单元类型 TODO 想哥，这玩意还有用么
	 * 
	 * @作者李想
	 * @创建时间 2015年6月7日下午20:39:20
	 * @param djlx
	 *            登记类型
	 * @param qllx
	 *            权利类型
	 * @param sllx1
	 *            受理类型1
	 * @param sllx2
	 *            受理类型2
	 * @return
	 */
	public static BDCDYLX GetNoticeBDCDYLX(String djlx, String qllx, String sllx1, String sllx2) {
		// TODO 刘树峰修改获取登记类型权利类型不动产单元类型方式
		qllx = qllx.replaceFirst("^0*", "");
		ConstValue.BDCDYLX value = ConstValue.BDCDYLX.H;
		if (qllx.equals("01")) {
			if (sllx1.equals("01")) {
				value = ConstValue.BDCDYLX.H;
			} else if (sllx1.equals("02") || sllx1.equals("03")) {
				value = ConstValue.BDCDYLX.ZRZ;
			}
		} else if (qllx.equals("02")) {
			if (sllx1.equals("01")) {
				if (sllx2.equals("01")) {
					value = ConstValue.BDCDYLX.SYQZD;
				} else if (sllx2.equals("02")) {
					value = ConstValue.BDCDYLX.SHYQZD;
				}
			} else if (sllx1.equals("02")) {
				if (sllx2.equals("01")) {
					value = ConstValue.BDCDYLX.H;
				} else if (sllx2.equals("02") || sllx2.equals("03")) {
					value = ConstValue.BDCDYLX.ZRZ;
				}
			} else if (sllx1.equals("03")) {
				value = ConstValue.BDCDYLX.LD;
			}
		} else if (qllx.equals(QLLX.QTQL.Value)) { // 暂时加上WUZHU
			if (sllx1.equals("01")) {
				value = BDCDYLX.SHYQZD;
			} else {
				value = BDCDYLX.H;
			}
		} else if (qllx.equals(QLLX.CFZX.Value)) { // 暂时加上WUZHU
			if (sllx1.equals("01")) {
				value = BDCDYLX.SHYQZD;
			} else {
				value = BDCDYLX.H;
			}
		}
		// TODO 李想：把权利类型和受理类型对应的不动产单元类型映射关系补充完整
		return value;
	}

	/**
	 * 根据流程编号获取流程ID
	 * @Title: getWorkflowIdByProjectID
	 * @author:liushufeng
	 * @date：2015年7月26日 上午3:33:31
	 * @param project_id
	 * @return
	 */
	public static String getWorkflowIdByProjectID(String project_id) {
		String workflowid = "";
		String workflowcode = getWorkflowCodeByProjectID(project_id);
		String sql = "select prodef_id from bdc_workflow.wfd_prodef where prodef_code='" + workflowcode + "'";
		@SuppressWarnings("rawtypes")
		List<Map> maps = commonDao.getDataListByFullSql(sql);
		if (maps != null && maps.size() > 0) {
			workflowid = StringHelper.formatObject(maps.get(0).get("PRODEF_ID"));
		}
		return workflowid;
	}

	/**
	 * 根据流程编号获取流程
	 * @Title: getWorkflowByProjectID
	 * @author:taochunda
	 * @return
	 */
	public static Wfd_Prodef getWorkflowByProjectID(String project_id) {
		Wfd_Prodef prodef  = new Wfd_Prodef();
		String workflowcode = getWorkflowCodeByProjectID(project_id);
		String wheresql = MessageFormat.format( "PRODEF_CODE=''{0}''", workflowcode);
		List<Wfd_Prodef> list = commonDao.getDataList(Wfd_Prodef.class, wheresql);
		if (list != null && list.size() > 0) {
			prodef = list.get(0);
		}
		return prodef;
	}

	/**
	 * 根据活动流程活动id获取流程id
	 * @Title: getpRroinstIDByActinstID
	 * @author:yuxuebin
	 * @date：2016年04月08日 15:04:31
	 * @param actinstid
	 * @return
	 */
	public static String getpRroinstIDByActinstID(String actinstid) {
		if(!StringHelper.isEmpty(actinstid)){
			Wfi_ActInst actinst=commonDao.get(Wfi_ActInst.class, actinstid);
			if(actinst!=null){
				return actinst.getProinst_Id();
			}
		}
		return "";
	}
	
	/**
	 * 对外接口：根据projectId获取档案移交信息
	 * @Title: GetFileTransferInfo
	 * @author:俞学斌
	 * @date：2015年11月22日 下午3:18:45
	 * @param project_id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map GetFileTransferInfo(String project_id) {
		Map m = new HashMap<String, String>();
		// 业务号
		m.put("FILE_NUMBER", project_id);
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx == null) {
			m.put("ZL", "");
			m.put("SLRY", "");
			m.put("SQR", "");
			return m;
		}
		// 受理人员
		String slry_info = xmxx.getSLRY() != null ? xmxx.getSLRY() : "";
		m.put("SLRY", slry_info);
		// 申请人
		String sqr_info = "";
		List<BDCS_SQR> sqrs = commonDao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "'");
		List<String> listsqr = new ArrayList<String>();
		if (sqrs != null && sqrs.size() > 0) {
			for (BDCS_SQR sqr : sqrs) {
				if (StringHelper.isEmpty(sqr.getSQRXM())) {
					continue;
				}
				if (!listsqr.contains(sqr.getSQRXM())) {
					sqr_info = sqr_info + sqr.getSQRXM() + ",";
					listsqr.add(sqr.getSQRXM());
				}
			}
		}
		if (sqr_info.endsWith(",")) {
			sqr_info = sqr_info.substring(0, sqr_info.length() - 1);
		}
		m.put("SQR", sqr_info);
		// 坐落
		List<BDCS_QL_GZ> qls = commonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmxx.getId() + "'");
		List<String> listdjdyid = new ArrayList<String>();
		String zl_info = "";
		String fwbm_info = "";
		if (qls != null && qls.size() > 0) {
			for (BDCS_QL_GZ ql : qls) {
				if (StringHelper.isEmpty(ql.getDJDYID())) {
					continue;
				}
				List<BDCS_DJDY_GZ> djdys = commonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmxx.getId() + "' AND DJDYID='" + ql.getDJDYID() + "'");
				if (djdys != null && djdys.size() > 0) {
					DJDYLY ly = DJDYLY.XZ;
					if (DJDYLY.XZ.Value.equals(djdys.get(0).getLY())) {
						ly = DJDYLY.LS;
					} else {
						ly = DJDYLY.initFrom(djdys.get(0).getLY());
					}
					RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()), ly, djdys.get(0).getBDCDYID());
					if (unit != null && !listdjdyid.contains(unit.getId())) {
						listdjdyid.add(unit.getId());
					}
					if (unit != null && !StringHelper.isEmpty(unit.getZL()) && StringHelper.isEmpty(zl_info)) {
						zl_info = unit.getZL();
						if (BDCDYLX.H.equals(unit.getBDCDYLX()) || BDCDYLX.HY.equals(unit.getBDCDYLX())) {
							House h = (House) unit;
							if (h != null) {
								fwbm_info = h.getFWBM();
							}
						}
					}
				}
			}
		}
		if (!StringHelper.isEmpty(zl_info) && listdjdyid.size() > 1) {
			zl_info = zl_info + "等";
		}
		if (!StringHelper.isEmpty(fwbm_info) && listdjdyid.size() > 1) {
			fwbm_info = fwbm_info + "等";
		}
		m.put("ZL", zl_info);
		m.put("FWBM", fwbm_info);
		return m;
	}
 
	/**
	 * lixin_20190221
       * 档案移交,获取信息
   * @param project_id
   * @return
   */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map GetFileTransferInforDayj(String project_id) {
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		Map m = new HashMap<String, String>();
		if(xmxx!=null) {
			// 业务号
			m.put("FILE_NUMBER", project_id);
			m.put("PROLSH", xmxx.getYWLSH());
			
			// 坐落
			List<BDCS_QL_GZ> qls = commonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmxx.getId() + "'");
			List<String> listdjdyid = new ArrayList<String>();
			String zl_info = "";
			String fwbm_info = "";
			if (qls != null && qls.size() > 0) {
				for (BDCS_QL_GZ ql : qls) {
					if (StringHelper.isEmpty(ql.getDJDYID())) {
						continue;
					}
					List<BDCS_DJDY_GZ> djdys = commonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmxx.getId() + "' AND DJDYID='" + ql.getDJDYID() + "'");
					if (djdys != null && djdys.size() > 0) {
						DJDYLY ly = DJDYLY.XZ;
						if (DJDYLY.XZ.Value.equals(djdys.get(0).getLY())) {
							ly = DJDYLY.LS;
						} else {
							ly = DJDYLY.initFrom(djdys.get(0).getLY());
						}
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()), ly, djdys.get(0).getBDCDYID());
						if (unit != null && !listdjdyid.contains(unit.getId())) {
							listdjdyid.add(unit.getId());
						}
						if (unit != null && !StringHelper.isEmpty(unit.getZL()) && StringHelper.isEmpty(zl_info)) {
							zl_info = unit.getZL();
							if (BDCDYLX.H.equals(unit.getBDCDYLX()) || BDCDYLX.HY.equals(unit.getBDCDYLX())) {
								House h = (House) unit;
								if (h != null) {
									fwbm_info = h.getFWBM();
								}
							}
						}
					}
				}
			}
			if (!StringHelper.isEmpty(zl_info) && listdjdyid.size() > 1) {
				zl_info = zl_info + "等";
			}
			if (!StringHelper.isEmpty(fwbm_info) && listdjdyid.size() > 1) {
				fwbm_info = fwbm_info + "等";
			}
			m.put("ZL", zl_info);
			//义务人和权利人
			List<BDCS_SQR> sqrs = commonDao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "'");
			List<String>  qlrlist=new ArrayList<String>();
			List<String>  ywrList=new ArrayList<String>();
			for(BDCS_SQR sqr:sqrs) {
				String sqrlb=sqr.getSQRLB();
				if(!StringHelper.isEmpty(sqrlb)&&sqrlb.equals("1")) {
					qlrlist.add(sqr.getSQRXM());
				}
				if(!StringHelper.isEmpty(sqrlb)&&sqrlb.equals("2")) {
					ywrList.add(sqr.getSQRXM());
				}
			}
			if(qlrlist!=null&&qlrlist.size()>0) {
				m.put("QLR", StringHelper.formatList(qlrlist));
			}else {
				m.put("QLR", "");
			}
			if(ywrList!=null&&ywrList.size()>0) {
				m.put("YWR", StringHelper.formatList(ywrList));
			}else {
				m.put("YWR", "");
			}
			
			//权证号
			List<String>  qzhlist=new ArrayList<String>();
			List<BDCS_QLR_GZ> qlrs = commonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmxx.getId() + "'");
			for (BDCS_QLR_GZ qlr:qlrs) {
				if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
					qzhlist.add(qlr.getBDCQZH());
				}
				
			}
			if (qzhlist!=null&&qzhlist.size()>0&&qzhlist.size()<=2) {
				m.put("BDCQZH", StringHelper.formatList(qzhlist));
			}else if (qzhlist!=null&&qzhlist.size()>2) {
				String qzh="";
				for(int i=0;i<2;i++) {
					qzh+=qzhlist.get(i);
				}
				m.put("BDCQZH", qzh+"等");
			}else {
				m.put("BDCQZH", "");
			}
			//登记原因和登记时间
			List<BDCS_QL_GZ>  qllist = commonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmxx.getId() + "'");
			if(qllist!=null&&qllist.size()>0) {
				BDCS_QL_GZ ql =qllist.get(0);
				m.put("DJYY", ql.getDJYY());
				m.put("DJSJ", ql.getDJSJ());	
			}else {
				m.put("DJYY", "");
				m.put("DJSJ", "");	
			}
			
			
		}else {
			m.put("ZL", "");
			m.put("BDCQZH", "");
			m.put("SLRY", "");
			m.put("SQR", "");
			m.put("QLRDH", "");
			m.put("DLR", "");
			m.put("DLRDH", "");
			m.put("QLR", "");
			m.put("YWR", "");
			m.put("DJYY", "");
			m.put("DJSJ", "");
			return m;
		}
		return m;
	
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map GetFileTransferInfoEx(String project_id) {
		Map m = new HashMap<String, String>();
		// 业务号
		m.put("FILE_NUMBER", project_id);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.SLRY,XMXX.XMBH,SQR.SQRXM AS SQRXM ,SQR.LXDH AS LXDH ,SQR.DLRXM AS DLRXM ,SQR.SQRLB AS SQRLB,");
		builder.append("SQR.DLRLXDH AS DLRLXDH ,QLR.BDCQZH AS BDCQZH,QL.FJ ");
		builder.append("FROM BDCK.BDCS_XMXX XMXX ");
		builder.append("LEFT JOIN BDCK.BDCS_SQR SQR ");
		builder.append("ON SQR.XMBH=XMXX.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ");
		builder.append("ON QL.XMBH=XMXX.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_QLR_GZ QLR ");
		builder.append("ON QLR.QLID=QL.QLID ");
		builder.append("WHERE XMXX.PROJECT_ID='");
		builder.append(project_id);
		builder.append("'");
		String strsql = builder.toString();
		List<Map> list = commonDao.getDataListByFullSql(strsql);
		if (list == null || list.size() <= 0) {
			m.put("ZL", "");
			m.put("BDCQZH", "");
			m.put("SLRY", "");
			m.put("SQR", "");
			m.put("QLRDH", "");
			m.put("DLR", "");
			m.put("DLRDH", "");
			m.put("QLR", "");
			m.put("YWR", "");
			return m;
		}
		String xmbh = StringHelper.formatObject(list.get(0).get("XMBH"));
		// 受理人员
		String slry_info = StringHelper.formatObject(list.get(0).get("SLRY"));
		m.put("SLRY", slry_info);
		// 不动产权证号
		String bdcqzh_info = StringHelper.formatObject(list.get(0).get("BDCQZH"));
		m.put("BDCQZH", bdcqzh_info);
		// 不动产权证号
		String fj_info = StringHelper.formatObject(list.get(0).get("FJ"));
		m.put("FJ", fj_info);
		// 申请人
		List<String> listSQR = new ArrayList<String>();
		// 权利人
		List<String> listQLR = new ArrayList<String>();
		// 义务人
		List<String> listYWR = new ArrayList<String>();
		//权利人电话
		List<String> listQLRDH = new ArrayList<String>();
		//权利人代理人
		List<String> listDLR = new ArrayList<String>();
		//权利人代理人电话
		List<String> listDLRDH = new ArrayList<String>();
		
		if (list != null && list.size() > 0) {
			for (Map msqr : list) {
				String SQRXM = StringHelper.formatObject(msqr.get("SQRXM"));
				String M_SQRLB = StringHelper.formatObject(msqr.get("SQRLB"));
				String QLRDH = StringHelper.formatObject(msqr.get("LXDH"));
				String DLR = StringHelper.formatObject(msqr.get("DLRXM"));
				String DLRDH = StringHelper.formatObject(msqr.get("DLRLXDH"));
				if (!StringHelper.isEmpty(SQRXM) && !listSQR.contains(SQRXM)) {
					listSQR.add(SQRXM);
				}
				if(SQRLB.JF.Value.equals(M_SQRLB)){
					if (!StringHelper.isEmpty(QLRDH) && !listQLRDH.contains(QLRDH)) {
						listQLRDH.add(QLRDH);
					}
					if (!StringHelper.isEmpty(DLR) && !listDLR.contains(DLR)) {
						listDLR.add(DLR);
					}
					if (!StringHelper.isEmpty(DLRDH) && !listDLRDH.contains(DLRDH)) {
						listDLRDH.add(DLRDH);
					}
					if (!StringHelper.isEmpty(SQRXM) && !listQLR.contains(SQRXM)) {
						listQLR.add(SQRXM);
					}
				}else{
					if (!StringHelper.isEmpty(SQRXM) && !listYWR.contains(SQRXM)) {
						listYWR.add(SQRXM);
					}
				}
			}
		}
		String sqr_info = StringHelper.formatList(listSQR);
		m.put("SQR", sqr_info);
		String qlr_info = StringHelper.formatList(listQLR);
		m.put("QLR", qlr_info);
		String ywr_info = StringHelper.formatList(listYWR);
		m.put("YWR", ywr_info);
		String qlrdh_info = StringHelper.formatList(listQLRDH);
		m.put("QLRDH", qlrdh_info);
		String dlr_info = StringHelper.formatList(listDLR);
		m.put("DLR", dlr_info);
		String dlrdh_info = StringHelper.formatList(listDLRDH);
		m.put("DLRDH", dlrdh_info);
		String  _xmbh ="";
		String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
		if (Baseworkflow_ID.equals("XZ001")) {//XZ001流程
			String sql= "SELECT QL.XMBH FROM BDCK.BDCS_QL_GZ QL  left join BDCK.BDCS_DJDY_GZ DJDY  ON  QL.DJDYID = DJDY.DJDYID  WHERE   DJDY.XMBH = '"
					+xmbh+"'";
			 _xmbh = commonDao.getDataListByFullSql(sql).get(0).get("XMBH").toString();
		}else {
			 _xmbh= xmbh;
		}
		// 获取坐落
		StringBuilder builderDJDY = new StringBuilder();
		builderDJDY.append("SELECT DJDY.BDCDYID,DJDY.LY,DJDY.BDCDYLX ");
		builderDJDY.append("FROM BDCK.BDCS_QL_GZ QL ");
		builderDJDY.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON QL.DJDYID=QL.DJDYID ");
		builderDJDY.append("WHERE QL.XMBH='");
		builderDJDY.append(_xmbh);
		builderDJDY.append("' AND DJDY.XMBH='");
		builderDJDY.append(xmbh);
		builderDJDY.append("'");
		String strsqldjdy = builderDJDY.toString();
		List<Map> listDJDY = commonDao.getDataListByFullSql(strsqldjdy);
		if (listDJDY == null || listDJDY.size() <= 0) {
			m.put("ZL", "");
			return m;
		}
		String ly = StringHelper.formatObject(listDJDY.get(0).get("LY"));
		String bdcdylx = StringHelper.formatObject(listDJDY.get(0).get("BDCDYLX"));
		String bdcdyid = StringHelper.formatObject(listDJDY.get(0).get("BDCDYID"));
		DJDYLY djdyly = DJDYLY.XZ;
		if (DJDYLY.XZ.Value.equals(ly)) {
			djdyly = DJDYLY.LS;
		} else {
			djdyly = DJDYLY.initFrom(ly);
		}
		// 坐落
		String zl_info = "";
		String bdcdyh_info = "";
		String fwbm_info = "";
		String fh_info = "";
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), djdyly, bdcdyid);
		if (unit != null && !StringHelper.isEmpty(unit.getZL())) {
			zl_info = unit.getZL();
			bdcdyh_info = unit.getBDCDYH();
			if (BDCDYLX.H.equals(unit.getBDCDYLX()) || BDCDYLX.YCH.equals(unit.getBDCDYLX())) {
				House h = (House) unit;
				if (h != null) {
					fwbm_info = h.getFWBM();
					fh_info = h.getFH();
				}
			}
		}

		if (!StringHelper.isEmpty(zl_info) && listDJDY.size() > 1) {
			zl_info = zl_info + "等";
		}
		if (!StringHelper.isEmpty(bdcdyh_info) && listDJDY.size() > 1) {
			bdcdyh_info = bdcdyh_info + "等";
		}
		if (!StringHelper.isEmpty(fwbm_info) && listDJDY.size() > 1) {
			fwbm_info = fwbm_info + "等";
		}
		m.put("ZL", zl_info);
		m.put("BDCDYH", bdcdyh_info);
		m.put("FWBM", fwbm_info);
		m.put("FH", fh_info);
		return m;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map GetFileTransferInfoEx2(String project_id) {
		Map m = new HashMap<String, String>();
		// 业务号
		m.put("FILE_NUMBER", project_id);

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.SLRY,XMXX.XMBH,SQR.SQRXM AS SQRXM ,QLR.BDCQZH AS BDCQZH ");
		builder.append("FROM BDCK.BDCS_XMXX XMXX ");
		builder.append("LEFT JOIN BDCK.BDCS_SQR SQR ");
		builder.append("ON SQR.XMBH=XMXX.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_QLR_GZ QLR ");
		builder.append("ON QLR.XMBH=XMXX.XMBH ");
		builder.append("WHERE XMXX.PROJECT_ID='");
		builder.append(project_id);
		builder.append("'");
		String strsql = builder.toString();
		List<Map> list = commonDao.getDataListByFullSql(strsql);
		if (list == null || list.size() <= 0) {
			m.put("ZL", "");
			m.put("BDCQZH", "");
			m.put("SLRY", "");
			m.put("SQR", "");
			// m.put("SYQR", "");
			return m;
		}
		String xmbh = StringHelper.formatObject(list.get(0).get("XMBH"));
		// 受理人员
		String slry_info = StringHelper.formatObject(list.get(0).get("SLRY"));
		m.put("SLRY", slry_info);
		// 不动产权证号
		String bdcqzh_info = StringHelper.formatObject(list.get(0).get("BDCQZH"));
		m.put("BDCQZH", bdcqzh_info);
		// 申请人
		String sqr_info = StringHelper.formatObject(list.get(0).get("SQRXM"));
		String sqr_infoEx = "";
		if (!StringHelper.isEmpty(sqr_info)) {
			List<String> listSqr = new ArrayList<String>();
			String[] strs_sqr = sqr_info.split(",");
			if (strs_sqr != null && strs_sqr.length > 0) {
				for (String str_sqr : strs_sqr) {
					if (!StringHelper.isEmpty(str_sqr) && !listSqr.contains(str_sqr)) {
						listSqr.add(str_sqr);
						sqr_infoEx = sqr_infoEx + str_sqr + ",";
					}
				}
			}
		}

		if (sqr_infoEx.endsWith(",")) {
			sqr_infoEx = sqr_infoEx.substring(0, sqr_infoEx.length() - 1);
		}

		// m.put("SQR", sqr_infoEx);
		// 获取坐落
		StringBuilder builderDJDY = new StringBuilder();
		builderDJDY.append("SELECT DJDY.BDCDYID,DJDY.DJDYID,DJDY.LY,DJDY.BDCDYLX ");
		builderDJDY.append("FROM BDCK.BDCS_QL_GZ QL ");
		builderDJDY.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON QL.DJDYID=QL.DJDYID ");
		builderDJDY.append("WHERE QL.XMBH='");
		builderDJDY.append(xmbh);
		builderDJDY.append("' AND DJDY.XMBH='");
		builderDJDY.append(xmbh);
		builderDJDY.append("'");
		String strsqldjdy = builderDJDY.toString();
		List<Map> listDJDY = commonDao.getDataListByFullSql(strsqldjdy);
		if (listDJDY == null || listDJDY.size() <= 0) {
			m.put("ZL", "");
			m.put("SQR", "");
			return m;
		}
		String ly = StringHelper.formatObject(listDJDY.get(0).get("LY"));
		String bdcdylx = StringHelper.formatObject(listDJDY.get(0).get("BDCDYLX"));
		String bdcdyid = StringHelper.formatObject(listDJDY.get(0).get("BDCDYID"));
		String djdyid = StringHelper.formatObject(listDJDY.get(0).get("DJDYID"));
		DJDYLY djdyly = DJDYLY.XZ;
		if (DJDYLY.XZ.Value.equals(ly)) {
			djdyly = DJDYLY.LS;
		} else {
			djdyly = DJDYLY.initFrom(ly);
		}
		// 坐落
		String zl_info = "";
		String fwbm_info = "";
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), djdyly, bdcdyid);
		if (unit != null && !StringHelper.isEmpty(unit.getZL())) {
			zl_info = unit.getZL();
			if (BDCDYLX.H.equals(unit.getBDCDYLX()) || BDCDYLX.HY.equals(unit.getBDCDYLX())) {
				House h = (House) unit;
				if (h != null) {
					fwbm_info = h.getFWBM();
				}
			}
		}

		if (!StringHelper.isEmpty(zl_info) && listDJDY.size() > 1) {
			zl_info = zl_info + "等";
		}
		if (!StringHelper.isEmpty(fwbm_info) && listDJDY.size() > 1) {
			fwbm_info = fwbm_info + "等";
		}
		m.put("ZL", zl_info);
		m.put("FWBM", fwbm_info);

		StringBuilder builderSYQR = new StringBuilder();
		builderSYQR.append("SELECT QLR.QLRMC AS SYQR ");
		builderSYQR.append("FROM BDCK.BDCS_QLR_XZ QLR ");
		builderSYQR.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QLR.QLID ");
		builderSYQR.append("WHERE QL.QLLX IN ('1','2','3','4','5','6','7','8') ");
		builderSYQR.append("AND QL.DJDYID='");
		builderSYQR.append(djdyid);
		builderSYQR.append("'");
		List<Map> listSYQR = commonDao.getDataListByFullSql(builderSYQR.toString());
		if (listSYQR != null && listSYQR.size() > 0) {
			List<String> listQLR = new ArrayList<String>();
			for (Map mqlr : listSYQR) {
				String QLRMC = StringHelper.formatObject(mqlr.get("SYQR"));
				if (!StringHelper.isEmpty(QLRMC)) {
					listQLR.add(QLRMC);
				}
			}
			String SYQR = StringHelper.formatList(listQLR);
			m.put("SQR", SYQR);
		} else {
			m.put("SQR", "");
		}
		return m;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map GetFileTransferInfoEx3(String project_id) {
		Map m = new HashMap<String, String>();
		// 业务号
		m.put("FILE_NUMBER", project_id);
		String zl_info = "";
		String bdcqzh_info = "";
		String slry_info = "";
		String qlr_info = "";
		List<String> qlr_list = new ArrayList<String>();
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx != null) {
			String xmbh = xmxx.getId();
			slry_info = xmxx.getSLRY();
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT QLR.QLRMC,ZS.BDCQZH,DJDY.BDCDYID,DJDY.BDCDYLX,DJDY.LY FROM BDCK.BDCS_QLR_XZ QLR ");
			builder.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QLR.QLID ");
			builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
			builder.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.XMBH=DJDY.XMBH ");
			builder.append("WHERE QL.QLLX IN ('1','2','3','4','5','6','7','8','23') ");
			builder.append("AND QLR.QLID IS NOT NULL ");
			builder.append("AND DJDY.XMBH='" + xmbh + "'");
			List<Map> list = commonDao.getDataListByFullSql(builder.toString());
			if (list == null || list.size() <= 0) {
				StringBuilder builderEx = new StringBuilder();
				builderEx.append("SELECT QLR.QLRMC,QLR.BDCQZH,DJDY.BDCDYID,DJDY.BDCDYLX,DJDY.LY FROM BDCK.BDCS_QLR_LS QLR ");
				builderEx.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.LYQLID=QLR.QLID ");
				builderEx.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
				builderEx.append("WHERE QL.QLLX IN ('1','2','3','4','5','6','7','8','23') ");
				builderEx.append("AND QLR.QLID IS NOT NULL ");
				builderEx.append("AND DJDY.XMBH='" + xmbh + "' AND QL.XMBH='" + xmbh + "'");
				list = commonDao.getDataListByFullSql(builderEx.toString());
				if (list == null || list.size() <= 0) {
					StringBuilder builderEx1 = new StringBuilder();
					builderEx1.append("SELECT QLR.QLRMC,QLR.BDCQZH,DJDY.BDCDYID,DJDY.BDCDYLX,DJDY.LY FROM BDCK.BDCS_QLR_LS QLR ");
					builderEx1.append("LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.QLID=QLR.QLID ");
					builderEx1.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builderEx1.append("WHERE QL.QLLX IN ('1','2','3','4','5','6','7','8','23') ");
					builderEx1.append("AND QLR.QLID IS NOT NULL ");
					builderEx1.append("AND DJDY.XMBH='" + xmbh + "' ORDER BY QL.DJSJ");
					list = commonDao.getDataListByFullSql(builderEx1.toString());
				}
			}
			if (list != null && list.size() > 0) {
				for (Map mmm : list) {
					String qlr = StringHelper.formatObject(mmm.get("QLRMC"));
					if(StringHelper.isEmpty(qlr)){
						List<BDCS_SQR> sqrlist = new ArrayList<BDCS_SQR>();
					    sqrlist = commonDao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "' AND SQRLB='1' ");
					    if(sqrlist.size()>0){
					    	for (BDCS_SQR sqr : sqrlist) {
					    		if (!qlr_list.contains(sqr.getSQRXM())) {
									qlr_list.add(sqr.getSQRXM());
								}
					    	}
					    }
					}
					if (!qlr_list.contains(qlr)) {
						qlr_list.add(qlr);
					}
					if (StringHelper.isEmpty(bdcqzh_info)) {
						bdcqzh_info = StringHelper.formatObject(mmm.get("BDCQZH"));
					}
					if (StringHelper.isEmpty(zl_info)) {
						String bdcdylx = StringHelper.formatObject(mmm.get("BDCDYLX"));
						String ly = StringHelper.formatObject(mmm.get("LY"));
						String bdcdyid = StringHelper.formatObject(mmm.get("BDCDYID"));
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(ly), bdcdyid);
						if (unit != null) {
							zl_info = unit.getZL();
						}
					}
				}
				if (qlr_list.size() > 0) {
					 HashSet h  =   new  HashSet(qlr_list); 
					 qlr_list.clear(); 
					 qlr_list.addAll(h); 
					qlr_info = StringHelper.formatList(qlr_list, "、");
				}
			}
			
			List<BDCS_SQR> sqrlist = new ArrayList<BDCS_SQR>();
			String djlx=xmxx.getDJLX();
			String qllx=xmxx.getQLLX();
		    sqrlist = commonDao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "'");
		    if (QLLX.DIYQ.Value.equals(qllx)){
		    	List<String> newqlr_list = new ArrayList<String>();
		    	if (sqrlist.size() > 0) {
					for (BDCS_SQR sqr : sqrlist) {
						if (sqr.getSQRLB().equals("2") && !newqlr_list.contains(sqr.getSQRXM())) {
							 
							newqlr_list.add(sqr.getSQRXM());
						}
					}
				}
		    	if (newqlr_list.size() > 0) {
		    		 HashSet h  =   new  HashSet(newqlr_list); 
		    		 newqlr_list.clear(); 
		    		 newqlr_list.addAll(h); 
					qlr_info = StringHelper.formatList(newqlr_list, "、");
				}
		    } 
		    
		}
		m.put("ZL", zl_info);
		m.put("BDCQZH", bdcqzh_info);
		m.put("SLRY", slry_info);
		m.put("SQR", qlr_info);
		return m;
	}

	public static List<String> GetFileTransferInfoEx4(String project_id) {
		List<String> listQlr = new ArrayList<String>();
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx != null) {
			List<BDCS_QLR_GZ> qlrs = commonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmxx.getId() + "'");
			if (qlrs != null && qlrs.size() > 0) {
				for (BDCS_QLR_GZ qlr : qlrs) {
					if (!StringHelper.isEmpty(qlr.getQLRMC()) && !listQlr.contains(qlr.getQLRMC())) {
						listQlr.add(qlr.getQLRMC());
					}
				}
			}
		}
		return listQlr;
	}
	
	/**
	 * 根据单元号生成依赖值，单元类型生成不动产单元号 单元类型：宗地：01;宗海：02;自然幢：03;户04；森林、林木：05
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日上午11:04:53
	 * @param RelyOnValue
	 *            单元号生成依赖值
	 * @param DYLX
	 *            单元类型
	 * @return
	 */
	public static String CreatBDCDYH(String RelyOnValue, String DYLX) {
		final String m_producename = "GETDYHEX";
		final String m_relyonvalue = RelyOnValue;
		final String m_dylx = DYLX;
		Session session = commonDao.getCurrentSession();
		String filenumber = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append("BDCDCK.");
				str.append(m_producename);
				str.append("(");
				str.append("?,?,?");
				str.append(") }");
				String filrnumberString = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				statement.setString(1, m_relyonvalue);
				statement.setString(2, m_dylx);
				statement.registerOutParameter(3, Types.NVARCHAR);
				statement.execute();
				filrnumberString = statement.getString(3);
				statement.close();
				return filrnumberString;
			}
		});
		if (StringHelper.isEmpty(filenumber)) {
			return "";
		}
		String BDCDYH = "";
		if ("01".equals(m_dylx)) {// 宗地
			//BDCDYH=14(ZD的宗地代码的前14位)+5(最大序号位)+9(W00000000)
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0') + "W00000000";
		}
		if ("011".equals(m_dylx)) {// 宗地
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0');
		}
		if ("02".equals(m_dylx)) {// 宗海
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0') + "H00000000";
		}
		if ("03".equals(m_dylx)) {// 自然幢
			//BDCDYH=19(宗地代码)+1(F)+4(最大序号位)+4(0000)
			BDCDYH = RelyOnValue + "F" + StringHelper.PadLeft(filenumber, 4, '0') + "0000";
		}
		if ("04".equals(m_dylx)) {// 户
			 //BDCDYH=24(ZRZ不动产单元号的前24位)+4(最大序号位)
			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 4, '0');
		}
		if ("05".equals(m_dylx)) {// 森林林木
			BDCDYH = RelyOnValue +StringHelper.PadLeft(filenumber, 5, '0')+ "L" + StringHelper.PadLeft(filenumber, 8, '0');
		}
		return BDCDYH;
	}
	
	public static String getMaxnum(String RelyOnValue, String DYLX) {
		final String m_producename = "GETDYHEX";
		final String m_relyonvalue = RelyOnValue;
		final String m_dylx = DYLX;
		Session session = commonDao.getCurrentSession();
		String filenumber = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append("BDCDCK.");
				str.append(m_producename);
				str.append("(");
				str.append("?,?,?");
				str.append(") }");
				String filrnumberString = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				statement.setString(1, m_relyonvalue);
				statement.setString(2, m_dylx);
				statement.registerOutParameter(3, Types.NVARCHAR);
				statement.execute();
				filrnumberString = statement.getString(3);
				statement.close();
				return filrnumberString;
			}
		});
		if (StringHelper.isEmpty(filenumber)) {
			return "";
		}
		return filenumber;
	}
}
