package com.supermap.realestate.registration.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * 登记簿Controller 跟登记簿相关的都放在这里边
 * 
 * @author 刘树峰
 * @date 2015年6月12日 上午11:46:55
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/boardbook")
@Component("DBController")
public class DBController {
	public Logger logger = Logger.getLogger(DBController.class);
	@Autowired
	private CommonDao dao;

	/** 登薄service */
	@Autowired
	private DBService dbService;

	/** 登记薄service */
	@Autowired
	private DJBService djbService;

	/**
	 * 根据项目编号登簿（URL:"/{xmbh}/boardbook",Method:GET）
	 * 
	 * @作者：俞学斌
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{xmbh}/boardbook", method = RequestMethod.GET)
	public @ResponseBody ResultMessage dbrk(@PathVariable("xmbh") String xmbh) throws Exception {
		double startTime = System.currentTimeMillis();
		ResultMessage ms = new ResultMessage();
		ms = dbService.BoardBook(xmbh);
		alterCachedXMXX(xmbh);
		double endTime = System.currentTimeMillis();
		System.out.println("登簿用时:" + (endTime - startTime));
		return ms;
	}

	/**
	 * 根据项目编号登簿（忽略登簿过程中的警告）（URL:"/{xmbh}/boardbooknocheck",Method:GET）
	 * 
	 * @作者：俞学斌
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{xmbh}/boardbooknocheck", method = RequestMethod.GET)
	public @ResponseBody ResultMessage dbrknocheck(@PathVariable("xmbh") String xmbh) throws Exception {
		double startTime = System.currentTimeMillis();
		ResultMessage ms = new ResultMessage();
		ms = dbService.boardBookIgnorWarning(xmbh);
		alterCachedXMXX(xmbh);
		double endTime = System.currentTimeMillis();
		System.out.println("登簿用时:" + (endTime - startTime));
		return ms;
	}

	/**
	 * 共享信息
	 * 
	 * @作者：俞学斌
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{projectID}/sendmsg/{bljc}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage SendMsg(@PathVariable("projectID") String projectID,
			@PathVariable("bljc") String bljc) throws Exception {
		ResultMessage ms = new ResultMessage();
		if (projectID.length() == 28) {// 受理转出时传过来的流程编码
			List<BDCS_XMXX> xmxxs = dao.getDataList(BDCS_XMXX.class, "project_id = '" + projectID + "'");
			if (xmxxs.size() > 0) {
				BDCS_XMXX xmxx = xmxxs.get(0);
				projectID = xmxx.getId();
			}
		}
		//登薄时不重复推送
		if(bljc.equals("3")){
//			return ms;
		}
		ms = dbService.SendMsg(projectID, bljc);

		return ms;
	}
	
	/**
	 * 共享土地信息
	 * @作者：卜晓波
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{projectID}/bdckpushtodjk/{bljc}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage BDCKPushToDJK(@PathVariable("projectID") String projectID,
			@PathVariable("bljc") String bljc) throws Exception {
		ResultMessage ms = new ResultMessage();
		if (projectID.length() == 28) {// 受理转出时传过来的流程编码
			List<BDCS_XMXX> xmxxs = dao.getDataList(BDCS_XMXX.class, "project_id = '" + projectID + "'");
			if (xmxxs.size() > 0) {
				BDCS_XMXX xmxx = xmxxs.get(0);
				projectID = xmxx.getId();
			}
		}
		//登薄时不重复推送
		if(bljc.equals("3")){
			return ms;
		}
		ms = dbService.BDCKPushToDJK(projectID, bljc);
		return ms;
	}
	/**
	 * 自动关联未关联项目 孝感特殊服务
	 * @作者：卜晓波
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/relationdata", method = RequestMethod.GET)
	public @ResponseBody ResultMessage Relationdata() throws Exception {
		ResultMessage ms = new ResultMessage();
		ms = dbService.Relationdata();
		return ms;
	}
	@RequestMapping(value = "/{xmbh}/zssendmsg/{zsid}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage PrintZSSendMsg(@PathVariable("xmbh") String xmbh,
			@PathVariable("zsid") String zsid) throws Exception {
		ResultMessage ms = new ResultMessage();
		ms = dbService.PrintZSSendMsg(xmbh, zsid);
		return ms;
	}

	@RequestMapping(value = "/{project_id}/sendsmsmsg/{smsip}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage SendSmsMsg(@PathVariable("project_id") String project_id,
			@PathVariable("smsip") String smsip) throws Exception {
		ResultMessage ms = new ResultMessage();
		ms = dbService.SendSmsMsg(project_id, smsip);
		return ms;
	}

	/**
	 * 共享信息
	 * 
	 * @作者：李堃
	 * @param bljc
	 *            办理进度
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pushdata/{type}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage pushData(@PathVariable("type") String type,
			@RequestParam("projectid") String projectid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage ms = new ResultMessage();
		ms.setMsg("正在后台多线程推送中，此页面不必等待可以关闭");
		ms.setSuccess("发送成功");
		logger.info("推送页面推送：" + request.getRemoteHost());
		if (type.equals("") || type.equals("0")||type.equals("4")) {
			// 批量推送、定时补推，先补推失败列表中数据，再比较项目编号补推
			dbService.PushBatchDataInFail("1>0 order by slsj");
			dbService.PushBatchData();
		} else if (type.equals("1")) {
			projectid = new String(projectid.getBytes("iso-8859-1"), "UTF-8");
			// 单个推送
			dbService.Pushsingledata(projectid);
		} else if (type.equals("2")) {
			// 按受理转出推送
			dbService.pushdata(projectid, "1");
		} else if (type.equals("3")) {
			// 按登薄
			dbService.pushdata(projectid, "2");
		}else if (type.equals("6")) {
			// 按缮证
			dbService.pushdata(projectid, "3");
		}
		
//		else if (type.equals("4")) {
//			// 批量推送pushfail表，定时补推触发
//			dbService.PushBatchDataInFail("1>0 order by slsj");
//			dbService.PushBatchData();
//		}
		else if (type.equals("5")) {
			//用轮询补推触发
			//" (DJLX ='800' AND (QLLX='99' OR QLLX='98')) OR (DJLX='100' AND QLLX='23') OR (DJLX='700' AND (QLLX='4' OR QLLX='99'))";
			String sql="  (DJLX ='800' AND (QLLX='99' OR QLLX='98')) OR  QLLX='23'  order by slsj";
					// 只推送pushfail表中限制类
			dbService.PushBatchDataInFail(sql);
		}

		return ms;
	}
	
	/**
	 * 地籍补推页面服务
	 * @param bljc
	 *            办理进度
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bdckpushtodjk/{type}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage bdckpushtodjk(@PathVariable("type") String type,
			@RequestParam("projectid") String projectID, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage ms = new ResultMessage();
		logger.info("地籍推送页面推送：" + request.getRemoteHost());
		if (projectID.length() >0) {// 受理转出时传过来的流程编码
			projectID = new String(projectID.getBytes("iso-8859-1"), "UTF-8");
			ms = dbService.BDCKPushToDJK(projectID, "2");
		}else{
			ms.setSuccess("失败！");
			ms.setMsg("请输入正确Project_id！");
		}
		return ms;
	}

	/**
	 * 根据项目编号配号并登簿（URL:"/boardbook",Method:GET）
	 * 
	 * @作者：俞学斌
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/boardbook", method = RequestMethod.POST)
	public @ResponseBody List<HashMap<String, String>> dbrk2(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String actinstids = RequestHelper.getParam(request, "actinstids");
		String[] list_actinstid = actinstids.split(",");
		if (list_actinstid != null && list_actinstid.length > 0) {
			for (String actinst_id : list_actinstid) {
				if (StringHelper.isEmpty(actinst_id)) {
					continue;
				}
				HashMap<String, String> m = new HashMap<String, String>();
				String xmbh = dbService.getXMBHFromActinst_Id(actinst_id);
				m.put("actinstid", actinst_id);
				ResultMessage ms = djbService.getQZHORZMHByXMBH(xmbh);
				if ("false".equals(ms.getSuccess())) {
					m.put("success", ms.getSuccess());
					m.put("msg", ms.getMsg());
				} else {
					ms = dbService.BoardBook(xmbh);
					alterCachedXMXX(xmbh);
					m.put("success", ms.getSuccess());
					m.put("msg", ms.getMsg());
				}
				list.add(m);
			}
		}
		return list;
	}

	/**
	 * 刷新推送失败数据
	 * 
	 * @作者：李堃
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refreshfail", method = RequestMethod.GET)
	public @ResponseBody ResultMessage refreshfail() throws Exception {
		ResultMessage ms = new ResultMessage();
		dbService.refreshfail();
		return ms;
	}

	/**
	 * 
	 * @作者 李堃
	 * @创建时间 2016年5月31日下午4:43:04
	 * @param projectids，多个projectID用“，”隔开
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pushdatabatch", method = RequestMethod.POST)
	public @ResponseBody ResultMessage pushDataBatch(@RequestParam("projectids") String projectids) throws Exception {
		ResultMessage ms = new ResultMessage();
		// 批量推送
		if (projectids != null && !projectids.equals("")) {
			String projectidS = projectids.replace(",", ",");
			String[] prjStrings = projectidS.split(",");
			if (prjStrings != null && prjStrings.length > 0) {
				for (int i = 0; i < prjStrings.length; i++) {
					String prj = prjStrings[i];
					dbService.Pushsingledata(prj);
				}
			}

		}

		return ms;
	}

	/**
	 * 供齐齐哈尔房产系统调用回写更新新Relationid
	 * 
	 * @作者：卜晓波
	 * @param fwzt
	 *            房屋状态
	 * @param relationid
	 *            旧relationid
	 * @param nrelationid
	 *            新relationid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updaterelationid/{fwzt}/{relationid}/{nrelationid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> UPDATERELATIONID(@PathVariable("fwzt") String fwzt,
			@PathVariable("relationid") String relationid, @PathVariable("nrelationid") String nrelationid)
			throws Exception {
		Map<String, String> resultmap = new HashMap<String, String>();
		try {
			resultmap = dbService.UPDATERELATIONID(fwzt, relationid, nrelationid);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fctime = sdf1.format(new Date());
			logger.info("调用回写接口结束：齐齐哈尔市房产系统调用Relationid回写接口日志记录：调用结束时间：" + fctime);
		} catch (Exception e) {
			resultmap.put("401", "程序错误");
		}
		return resultmap;

	}

	/**
	 * 刷新推送失败数据
	 * 
	 * @作者：李堃
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refreshxzfail", method = RequestMethod.GET)
	public @ResponseBody ResultMessage refreshfailex() throws Exception {
		ResultMessage ms = new ResultMessage();
		dbService.refreshXZfail();
		return ms;
	}
	
	public void alterCachedXMXX(String xmbh) {
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null) {
			BDCS_XMXX cachedXMXX = Global.getXMXX(xmxx.getPROJECT_ID());
			if (cachedXMXX != null) {
				cachedXMXX.setSFDB(xmxx.getSFDB());
			}
		}
	}

	/**
	 * 供权籍系统限制登记时调用，按照共享逻辑，推送gxjhxm、h、xzdj表
	 * 
	 * @作者：卜晓波 2017年5月24日 11:08:57
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qjsendxzdj")
	public @ResponseBody ResultMessage qjSendMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage ms = new ResultMessage();
		String xzly =RequestHelper.getParam(request, "xzly");
		String xzdj =RequestHelper.getParam(request, "dyxzinfos");
//		String xzdj="[{\"YWH\":\"\",\"BXZRZJZL\":\"\",\"MODIFYTIME\":\"\",\"ZXDBR\":\"\",\"CREATETIME\":{\"date\":11,\"day\":2,\"hours\":1,\"minutes\":15,\"month\":9,\"nanos\":0,\"seconds\":0,\"time\":1476119700000,\"timezoneOffset\":-480,\"year\":116},\"XMBH\":\"201012100044\",\"BZ\":\"\",\"XZLX\":\"02\",\"BXZRZJHM\":\"\",\"ZXYJ\":\"\",\"XZZZRQ\":\"\",\"ZXXZWJHM\":\"\",\"BXZRMC\":\"\",\"ID\":\"SCHXZ657243\",\"SLRYJ\":\"自2010年8月9日起对你单位价值相当于应纳税款、滞纳金1，409，902.00元的商品、货物或者其他。１＃13号\",\"SLR\":\"\",\"ZXBZ\":\"\",\"LSXZ\":\"\",\"XZWJHM\":\"延市地税协一〔2010〕5号\",\"ZXYWH\":\"\",\"XZDW\":\"延吉市地方税务局\",\"BDCDYID\":\"SCH287526\",\"XZFW\":\"欠税查封、扣押\",\"SDTZRQ\":\"\",\"BDCDYH\":\"\",\"YXBZ\":\"1\",\"ZXXZDW\":\"\",\"BDCDYLX\":\"031\",\"DBR\":\"\",\"ZXDJSJ\":\"\",\"BDCQZH\":\"\",\"XZQSRQ\":{\"date\":1,\"day\":3,\"hours\":0,\"minutes\":0,\"month\":11,\"nanos\":0,\"seconds\":0,\"time\":1291132800000,\"timezoneOffset\":-480,\"year\":110},\"DJSJ\":\"\"},{\"YWH\":\"\",\"BXZRZJZL\":\"\",\"MODIFYTIME\":\"\",\"ZXDBR\":\"\",\"CREATETIME\":{\"date\":11,\"day\":2,\"hours\":1,\"minutes\":15,\"month\":9,\"nanos\":0,\"seconds\":0,\"time\":1476119700000,\"timezoneOffset\":-480,\"year\":116},\"XMBH\":\"201202220096\",\"BZ\":\"\",\"XZLX\":\"02\",\"BXZRZJHM\":\"\",\"ZXYJ\":\"\",\"XZZZRQ\":\"\",\"ZXXZWJHM\":\"\",\"BXZRMC\":\"\",\"ID\":\"SCHXZ1632633\",\"SLRYJ\":\"张永群    13180731638\",\"SLR\":\"\",\"ZXBZ\":\"\",\"LSXZ\":\"\",\"XZWJHM\":\"拆迁办提供的明细\",\"ZXYWH\":\"\",\"XZDW\":\"监察办\",\"BDCDYID\":\"SCH469495\",\"XZFW\":\"拆迁冻结\",\"SDTZRQ\":\"\",\"BDCDYH\":\"\",\"YXBZ\":\"1\",\"ZXXZDW\":\"\",\"BDCDYLX\":\"031\",\"DBR\":\"\",\"ZXDJSJ\":\"\",\"BDCQZH\":\"\",\"XZQSRQ\":\"\",\"DJSJ\":\"\"},{\"YWH\":\"\",\"BXZRZJZL\":\"\",\"MODIFYTIME\":\"\",\"ZXDBR\":\"\",\"CREATETIME\":{\"date\":11,\"day\":2,\"hours\":1,\"minutes\":15,\"month\":9,\"nanos\":0,\"seconds\":0,\"time\":1476119700000,\"timezoneOffset\":-480,\"year\":116},\"XMBH\":\"201510140014\",\"BZ\":\"\",\"XZLX\":\"02\",\"BXZRZJHM\":\"\",\"ZXYJ\":\"\",\"XZZZRQ\":\"\",\"ZXXZWJHM\":\"\",\"BXZRMC\":\"\",\"ID\":\"SCHXZ1940358\",\"SLRYJ\":\"\",\"SLR\":\"\",\"ZXBZ\":\"\",\"LSXZ\":\"\",\"XZWJHM\":\"延热函(2015)44号\",\"ZXYWH\":\"\",\"XZDW\":\"延吉市燃气和供热管理中心\",\"BDCDYID\":\"SCH503765\",\"XZFW\":\"根据延吉市燃气和供热管理中心文件进行查封\",\"SDTZRQ\":\"\",\"BDCDYH\":\"\",\"YXBZ\":\"1\",\"ZXXZDW\":\"\",\"BDCDYLX\":\"031\",\"DBR\":\"\",\"ZXDJSJ\":\"\",\"BDCQZH\":\"\",\"XZQSRQ\":{\"date\":14,\"day\":3,\"hours\":0,\"minutes\":0,\"month\":9,\"nanos\":0,\"seconds\":0,\"time\":1444752000000,\"timezoneOffset\":-480,\"year\":115},\"DJSJ\":\"\"},{\"YWH\":\"\",\"BXZRZJZL\":\"\",\"MODIFYTIME\":\"\",\"ZXDBR\":\"\",\"CREATETIME\":{\"date\":11,\"day\":2,\"hours\":1,\"minutes\":15,\"month\":9,\"nanos\":0,\"seconds\":0,\"time\":1476119700000,\"timezoneOffset\":-480,\"year\":116},\"XMBH\":\"201510140009\",\"BZ\":\"\",\"XZLX\":\"02\",\"BXZRZJHM\":\"\",\"ZXYJ\":\"\",\"XZZZRQ\":\"\",\"ZXXZWJHM\":\"\",\"BXZRMC\":\"\",\"ID\":\"SCHXZ1940362\",\"SLRYJ\":\"\",\"SLR\":\"\",\"ZXBZ\":\"\",\"LSXZ\":\"\",\"XZWJHM\":\"延热函(2015)44号\",\"ZXYWH\":\"\",\"XZDW\":\"延吉市燃气和供热管理中心\",\"BDCDYID\":\"SCH503766\",\"XZFW\":\"根据延吉市燃气和供热管理中心文件进行查封\",\"SDTZRQ\":\"\",\"BDCDYH\":\"\",\"YXBZ\":\"1\",\"ZXXZDW\":\"\",\"BDCDYLX\":\"031\",\"DBR\":\"\",\"ZXDJSJ\":\"\",\"BDCQZH\":\"\",\"XZQSRQ\":{\"date\":14,\"day\":3,\"hours\":0,\"minutes\":0,\"month\":9,\"nanos\":0,\"seconds\":0,\"time\":1444752000000,\"timezoneOffset\":-480,\"year\":115},\"DJSJ\":\"\"}]";
//		String xzdj="[{\"BDCDYID\":\"SCH42832-YMXXZ\",\"BDCDYLX\":\"031\",\"BDCQZH\":\"云(2016)楚雄市不动产权第0002665号\",\"BDCDYH\":\"532301002001GB00736F00040001\",\"XMBH\":null,\"BXZRMC\":\"付家银,李靳云,付国强\",\"BXZRZJZL\":\"1\",\"BXZRZJHM\":\"532328199303210013,532328196404250029,532328196210221318\",\"XZWJHM\":\"\",\"XZDW\":\"\",\"SDTZRQ\":\"0001-01-01T00:00:00\",\"XZQSRQ\":\"2017-05-09T00:00:00\",\"XZZZRQ\":\"2017-05-15T00:00:00\",\"SLR\":\"\",\"SLRYJ\":\"\",\"XZLX\":\"03\",\"YXBZ\":\"1\",\"LSXZ\":\"\",\"XZFW\":\"\",\"CREATETIME\":\"0001-01-01T00:00:00\",\"MODIFYTIME\":\"0001-01-01T00:00:00\",\"DJSJ\":\"0001-01-01T00:00:00\",\"DBR\":null,\"YWH\":null,\"BZ\":\"\",\"ZXDJSJ\":\"0001-01-01T00:00:00\",\"ZXDBR\":null,\"ZXYWH\":null,\"ZXBZ\":null,\"ZXYJ\":null,\"ZXXZWJHM\":null,\"ZXXZDW\":null,\"ID\":\"388b6736-3221-440b-a111-dae774f81824\",\"DatasourceAlia\":\"BDCK\",\"Datasource\":null},{\"BDCDYID\":\"SCH42862-YMXXZ\",\"BDCDYLX\":\"031\",\"BDCQZH\":\"云(2016)楚雄市不动产权第0002252号\",\"BDCDYH\":\"532301002001GB00736F00020002\",\"XMBH\":null,\"BXZRMC\":\"仲加翠,闻国武\",\"BXZRZJZL\":\"1\",\"BXZRZJHM\":\"532328197808060525,532328197110141331\",\"XZWJHM\":\"\",\"XZDW\":\"\",\"SDTZRQ\":\"0001-01-01T00:00:00\",\"XZQSRQ\":\"2017-05-09T00:00:00\",\"XZZZRQ\":\"2017-05-15T00:00:00\",\"SLR\":\"\",\"SLRYJ\":\"\",\"XZLX\":\"03\",\"YXBZ\":\"1\",\"LSXZ\":\"\",\"XZFW\":\"\",\"CREATETIME\":\"0001-01-01T00:00:00\",\"MODIFYTIME\":\"0001-01-01T00:00:00\",\"DJSJ\":\"0001-01-01T00:00:00\",\"DBR\":null,\"YWH\":null,\"BZ\":\"\",\"ZXDJSJ\":\"0001-01-01T00:00:00\",\"ZXDBR\":null,\"ZXYWH\":null,\"ZXBZ\":null,\"ZXYJ\":null,\"ZXXZWJHM\":null,\"ZXXZDW\":null,\"ID\":\"475aea5e-9d49-413c-adfe-cf41714941e4\",\"DatasourceAlia\":\"BDCK\",\"Datasource\":null},{\"BDCDYID\":\"SCH42866-YMXXZ\",\"BDCDYLX\":\"031\",\"BDCQZH\":\"云(2016)楚雄市不动产权第0003730号\",\"BDCDYH\":\"532301002001GB00736F00020003\",\"XMBH\":null,\"BXZRMC\":\"周艳,刘光金\",\"BXZRZJZL\":\"1\",\"BXZRZJHM\":\"532328197901200529,532328197606280714\",\"XZWJHM\":\"\",\"XZDW\":\"\",\"SDTZRQ\":\"0001-01-01T00:00:00\",\"XZQSRQ\":\"2017-05-09T00:00:00\",\"XZZZRQ\":\"2017-05-15T00:00:00\",\"SLR\":\"\",\"SLRYJ\":\"\",\"XZLX\":\"03\",\"YXBZ\":\"1\",\"LSXZ\":\"\",\"XZFW\":\"\",\"CREATETIME\":\"0001-01-01T00:00:00\",\"MODIFYTIME\":\"0001-01-01T00:00:00\",\"DJSJ\":\"0001-01-01T00:00:00\",\"DBR\":null,\"YWH\":null,\"BZ\":\"\",\"ZXDJSJ\":\"0001-01-01T00:00:00\",\"ZXDBR\":null,\"ZXYWH\":null,\"ZXBZ\":null,\"ZXYJ\":null,\"ZXXZWJHM\":null,\"ZXXZDW\":null,\"ID\":\"55e73cc3-32c1-4894-9b37-1a532ea22cb2\",\"DatasourceAlia\":\"BDCK\",\"Datasource\":null},{\"BDCDYID\":\"SCH201556-YMXXZ\",\"BDCDYLX\":\"031\",\"BDCQZH\":\"东瓜字第00156166号,楚开国用2016第001033号\",\"BDCDYH\":\"532301002001GB00736F00020001\",\"XMBH\":null,\"BXZRMC\":\"王应华,倪怀琼\",\"BXZRZJZL\":\"1\",\"BXZRZJHM\":\"532301197205102191,53230119740130214X\",\"XZWJHM\":\"\",\"XZDW\":\"\",\"SDTZRQ\":\"0001-01-01T00:00:00\",\"XZQSRQ\":\"2017-05-09T00:00:00\",\"XZZZRQ\":\"2017-05-15T00:00:00\",\"SLR\":\"\",\"SLRYJ\":\"\",\"XZLX\":\"03\",\"YXBZ\":\"1\",\"LSXZ\":\"\",\"XZFW\":\"\",\"CREATETIME\":\"0001-01-01T00:00:00\",\"MODIFYTIME\":\"0001-01-01T00:00:00\",\"DJSJ\":\"0001-01-01T00:00:00\",\"DBR\":null,\"YWH\":null,\"BZ\":\"\",\"ZXDJSJ\":\"0001-01-01T00:00:00\",\"ZXDBR\":null,\"ZXYWH\":null,\"ZXBZ\":null,\"ZXYJ\":null,\"ZXXZWJHM\":null,\"ZXXZDW\":null,\"ID\":\"572ee065-c30a-4d3b-b42f-3f45c394027a\",\"DatasourceAlia\":\"BDCK\",\"Datasource\":null}]";
//		xzly="1";
		if (xzdj != null && xzdj.length() > 0) {
			if(xzly.equals("1")){
				xzdj=xzdj.replace("null", "\"\"");
			}
			ms = dbService.qjSendMessage(xzdj,xzly);
		}else{
			ms.setSuccess("失败");
			ms.setMsg("权籍限制调用协同接口传入数据为空或异常！");
		}
		return ms;
	}
	
	/**
	 * 判断当前户是否为关联宗地上的最后一个单元（URL:"/{xmbh}/isthelastdy",Method:GET）
	 * 
	 * @作者：heks
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{xmbh}/isthelastdy", method = RequestMethod.GET)
	public @ResponseBody ResultMessage isTheLastDY(@PathVariable("xmbh") String xmbh) throws Exception {
		ResultMessage ms = new ResultMessage();
		ms = dbService.isTheLastDY(xmbh);
		return ms;
	}
}
