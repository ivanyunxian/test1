package com.supermap.wisdombusiness.archives.web;

import com.supemap.mns.common.HttpMethod;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.ViewClass.SQSPBex.XMXX;
import com.supermap.realestate.registration.dataExchange.Data;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.ProjectService_DA;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.wisdombusiness.archives.service.SmProInstService_DA;
import com.supermap.wisdombusiness.archives.web.common.Basic;
import com.supermap.wisdombusiness.archives.web.common.BookMapping;
import com.supermap.wisdombusiness.archives.web.common.DossierOption;
import com.supermap.wisdombusiness.archives.web.common.WorkFlow2Dossier;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import com.supermap.wisdombusiness.workflow.util.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bdc/archives")
public class ArchivesController {
	public Logger logger = Logger.getLogger(ArchivesController.class);
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private WorkFlow2Dossier archivesworkFlow2Dossier;
	@Autowired
	private BookMapping archivesbookMapping;
	@Autowired
	private DossierOption archivesdossierOption;
	@Autowired
	private DBService dbService;
	@Autowired
	private ProjectService_DA projectService_da;
	@Autowired
	private SmMaterialService smMaterialService;
	@Autowired
	private SmProInstService_DA smProInstService_DA;
	private static final Integer UNIT_COUNT = 100;//档案是否是大件的判断标识
	private static final String getajinfo = "pigeonhole";
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowIndex(Model model) {
		model.addAttribute("djgdAttribute", new BDCS_DJGD());
		String systemType=ConfigHelper.getNameByValue("SYSTEMTYPE");
		if(systemType!=null&&"1".equals(systemType)) {
			return	"/workflow/archives/archive_BeiHai";
		}
		return "/workflow/archives/archive";
	}

	@RequestMapping(value = "/searchbox", method = RequestMethod.GET)
	public String searchbox(Model model) {

		return "/workflow/archives/definebox";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/GD/asyn/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public Map CreatAysnDA(@PathVariable String file_number, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultmsg = new ResultMessage();
		String actinst_id = StringHelper.ObjToString(request.getParameter("actinstid"));
		String DJDL = StringHelper.ObjToString(request.getParameter("DJDL"));
		String DJXL = StringHelper.ObjToString(request.getParameter("DJXL"));
		String AJH = StringHelper.ObjToString(request.getParameter("AJH"));
		String QZHM = StringHelper.ObjToString(request.getParameter("QZHM"));
		String CH = StringHelper.ObjToString(request.getParameter("CH"));
		String DAGH = StringHelper.ObjToString(request.getParameter("GH"));
		String DAHH = StringHelper.ObjToString(request.getParameter("HH"));
		String WJJS = StringHelper.ObjToString(request.getParameter("WJJS"));
		String ZYS = StringHelper.ObjToString(request.getParameter("ZYS"));
		String LRR = StringHelper.ObjToString(request.getParameter("LRR"));
		String CDSJ = StringHelper.ObjToString(request.getParameter("CDSJ"));
		String BZ = StringHelper.ObjToString(request.getParameter("BZ"));
		String id = StringHelper.ObjToString(request.getParameter("id"));
		String MLBH = StringHelper.ObjToString(request.getParameter("MLBH"));
		//产别
		String CB = StringHelper.ObjToString(request.getParameter("CB"));
		String LSH = StringHelper.ObjToString(request.getParameter("LSH"));
		String SFQYXDA =ConfigHelper.getNameByValue("SFQYXDA");//新旧判断依据,本地化配置中设置
		String backwrite = request.getParameter("backwrite");
		boolean bw = true;
		if (backwrite != null && !backwrite.equals("")) {
			bw = Boolean.parseBoolean(backwrite);
		}
		if (DJDL != null && DJDL != "" && DJXL != null && DJXL != "") {
			Map megmap = new HashMap();
			Wfi_ActInst actinst = commonDao.get(Wfi_ActInst.class, actinst_id);
			if (actinst != null && StringHelper.isNotNull(actinst.getProinst_Id())) {
				Wfi_ProInst proinst = commonDao.get(Wfi_ProInst.class, actinst.getProinst_Id());
				if (actinst != null && StringHelper.isNotNull(proinst.getProdef_Id())) {
					StringBuilder hsb = new StringBuilder();
					
					if(SFQYXDA.equals("2")){
						hsb.append("SELECT * FROM BDC_DAK.DAS_DALB_LC WHERE prodefmainid='");
					}else {
						hsb.append("SELECT * FROM SMWB_DAK.DAS_DALB_LC WHERE prodefmainid='");
					}
					hsb.append(proinst.getProdef_Id());
					hsb.append("'");
					List<Map> dalblist = commonDao.getDataListByFullSql(hsb.toString());
					if (dalblist.size() > 0) {
						Map mp = dalblist.get(0);
						if (mp != null && mp.keySet() != null && mp.keySet().size() > 0) {
							for (Object keyname : mp.keySet()) {
								String key = keyname.toString();
								if (StringHelper.isNotNull(key) && key.equals("BM")) {
									String bm = mp.get(keyname).toString();
									if (bm.contains("DAGH") || bm.contains("DAHH")) {
										if (!StringHelper.isNotNull(DAGH)) {
											resultmsg.setMsg("案卷号定义中包含柜号，柜号不能为空。请输入柜号！");
											megmap.put("tip", resultmsg);
											megmap.put("success", false);
											return megmap;
										}
										if (!StringHelper.isNotNull(DAHH)) {
											resultmsg.setMsg("案卷号定义中包含盒号，盒号不能为空。请输入盒号！");
											megmap.put("tip", resultmsg);
											megmap.put("success", false);
											return megmap;
										}

									}
								} else {
									continue;
								}
							}
						} else {
							resultmsg.setMsg("流程配置有问题！");
							megmap.put("tip", resultmsg);
							megmap.put("success", false);
							return megmap;
						}
					} else {
						resultmsg.setMsg("没有配置对应流程的编号！");
						megmap.put("tip", resultmsg);
						megmap.put("success", false);
						return megmap;
					}
				}
			}
		} else {
			BDCS_XMXX XMXX = Global.getXMXX(file_number);
			String xmbh = XMXX.getId();
			BDCS_DJGD GDInfo = projectService_da.getDjgdInfo(xmbh, file_number);
			DJDL = GDInfo.getDJDL();
			DJXL = GDInfo.getDJXL();

		}
		//新归档
		if(SFQYXDA.equals("1")){
			Map map = new HashMap();
			Map<String, String> param = new HashMap();
			param.put("cb", CB);
			param.put("lsh", LSH);
			param.put("gdfs", "1");//页面归档 1
			param.put("gh", DAGH);
			param.put("hh", DAHH);
			param.put("lrr", LRR);
			param.put("ajh", AJH);
			try {
				Message message=Basic.DossierRequest(getajinfo, param, HttpMethod.GET);
				JSONObject jobj=JSONObject.fromObject(message.getMessageBodyAsString());
				map.put("GDJG", jobj.getString("GDJG"));
				map.put("ajh", jobj.getString("ajh"));
				map.put("gh", jobj.getString("gh"));
				map.put("hh", jobj.getString("hh"));
				map.put("ajhcf", jobj.getString("GDJG"));
				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
			}
			
			
			return map;
			
		}
		//2.0归档
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("LRR", LRR);
		ajjbxxmap.put("BZ", BZ);
		ajjbxxmap.put("DACBH", CH);
		ajjbxxmap.put("DAGBH", DAGH);
		ajjbxxmap.put("DAHBH", DAHH);
		ajjbxxmap.put("WJJS", WJJS);
		ajjbxxmap.put("WJYS", ZYS);
		ajjbxxmap.put("CDSJ", CDSJ);
		ajjbxxmap.put("AJH", AJH);
		ajjbxxmap.put("MLBH", MLBH);
		return GD(file_number, ajjbxxmap, id, actinst_id, bw, DJDL, DJXL);
	}

	// 档案数据抽取服务
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping(value = "/swap", method = RequestMethod.POST)
	public String DossierSwap(HttpServletRequest request, HttpServletResponse response) {
		String swapcount = request.getParameter("count");
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("LRR", "");
		ajjbxxmap.put("BZ", "系统抽取");
		Integer count = 0;
		Message msg = archivesworkFlow2Dossier.GetAllSwapDossier();
		if (msg != null) {
			JSONArray jobj = JSONArray.fromObject(msg.getMessageBodyAsString());
			count = jobj.size();
			for (int i = 0; i < count; i++) {
				JSONObject json = jobj.getJSONObject(i);
				if (json != null) {
					String f = json.get("FILE_NUMBER").toString();
					// String actid = json.get("ACTINST_ID").toString();
					String id = json.get("TRANSFERLISTID").toString();
					if (StringHelper.isNotNull(f)) {
						GD(f, ajjbxxmap, id, "", true, "", "");
					}
					System.out.print("完成" + i + "/" + count);
				}
			}
		}
		return "抽取完毕，抽取" + count;
	}

	// 抽取项目归档环节的件

	@ResponseBody
	@RequestMapping(value = "/swap/actinst", method = RequestMethod.POST)
	public String ArchivesSwapAct(HttpServletRequest request, HttpServletResponse response) {
		String actinstname = request.getParameter("actname");
		String status = request.getParameter("status");
		Integer count = 0;
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("LRR", "");
		ajjbxxmap.put("BZ", "系统抽取");
		Message msg = archivesworkFlow2Dossier.GetAllProject(actinstname, status);
		if (msg != null) {
			String ajlist = msg.getMessageBodyAsString();
			if (ajlist != null && !ajlist.equals("")) {
				JSONObject obj = JSONObject.fromObject(ajlist);
				if (obj != null) {
					Object list = obj.get("rows");
					if (list != null && !list.equals("")) {
						JSONArray jobj = JSONArray.fromObject(list);
						count = jobj.size();
						for (int i = 0; i < count; i++) {
							JSONObject json = jobj.getJSONObject(i);
							if (json != null) {
								String f = json.get("FILE_NUMBER").toString();
								String actinstid = json.get("ACTINST_ID").toString();
								if (StringHelper.isNotNull(f)) {
									GD(f, ajjbxxmap, "", actinstid, false, "", "");
									System.out.print("完成" + i + "/" + count);
								}
							}
						}
					}
				}
			}
		}
		return "抽取完毕，抽取" + count;
	}

	@ResponseBody
	@RequestMapping(value = "/swap/null/archives", method = RequestMethod.POST)
	public String exctNullArchives(HttpServletRequest request, HttpServletResponse response) {
		int count = 0;
		Message msg = archivesworkFlow2Dossier.GetNullArchives();
		if (msg != null) {
			Map<String, String> ajjbxxmap = new HashMap<String, String>();
			ajjbxxmap.put("LRR", "");
			ajjbxxmap.put("BZ", "系统补充抽取");
			ajjbxxmap.put("MLBH", "S");
			String projectlist = msg.getMessageBodyAsString();
			if (projectlist != null && !projectlist.equals("")) {
				JSONArray obj = JSONArray.fromObject(projectlist);
				if (obj != null && obj.size() > 0) {
					for (int i = 0; i < obj.size(); i++) {
						JSONObject jsonobj = obj.getJSONObject(i);
						if (jsonobj != null) {
							String file_number = jsonobj.getString("FILE_NUMBER");
							GD(file_number, ajjbxxmap, "", "", false, "", "");
							count++;
							System.out.println("完成" + count + "/" + obj.size());
						}

					}
				}
			}
		}
		return "抽取完成" + count;
	}

	@ResponseBody
	@RequestMapping(value = "/swap/null/archives/ddd", method = RequestMethod.POST)
	public String exctNullArchivesDDD(HttpServletRequest request, HttpServletResponse response) {
		int count = 0;
		Message msg = archivesworkFlow2Dossier.GetNullArchivesDDD();
		if (msg != null) {
			Map<String, String> ajjbxxmap = new HashMap<String, String>();
			ajjbxxmap.put("LRR", "");
			ajjbxxmap.put("BZ", "系统补充抽取");
			ajjbxxmap.put("MLBH", null);
			String projectlist = msg.getMessageBodyAsString();
			if (projectlist != null && !projectlist.equals("")) {
				JSONArray obj = JSONArray.fromObject(projectlist);
				if (obj != null && obj.size() > 0) {
					for (int i = 0; i < obj.size(); i++) {
						JSONObject jsonobj = obj.getJSONObject(i);
						if (jsonobj != null) {
							String file_number = jsonobj.getString("FILE_NUMBER");
							String actid = "";
							if (jsonobj.get("ACTINST_ID") != null) {
								actid = jsonobj.get("ACTINST_ID").toString();
							}
							String id = "";
							if (jsonobj.get("TRANSFERLISTID") != null) {
								id = jsonobj.get("TRANSFERLISTID").toString();
							}
							GD(file_number, ajjbxxmap, id, actid, true, "", "");
							count++;
							System.out.println("完成" + count + "/" + obj.size());
						}

					}
				}
			}
		}
		return "抽取完成" + count;
	}

	// 这个归档方法是组件归档用到的（也就是归档页面归档的）
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map GD(String file_number, Map<String, String> ajjbxxmap, String id, String actinst_id, boolean backwrite,
			String DJDL, String DJXL) {
		Map map = new HashMap();
		String AJID = "";
		String DAH = "";
		String OLDDAH = "";
		String qlr = "";
		Message projectinfo = archivesworkFlow2Dossier.GetProjectInfo(file_number);

		// ----------打印工程信息日志
		// logger.info(projectinfo.getMessageBodyAsString());
		if (projectinfo != null) {
			Message materials = archivesworkFlow2Dossier.GetWorkFlowMaterial(file_number);
			String meterial = materials.getMessageBodyAsString();
			Map DA = archivesworkFlow2Dossier.Setmaterial2DossierEx(projectinfo.getMessageBodyAsString(), meterial,
					ajjbxxmap, DJDL, DJXL);
			Message creatResult = archivesworkFlow2Dossier.CreatAJ(DA);
			// ----------打印创建档案的日志信息
			// logger.info(creatResult.getMessageBodyAsString());
			// 发送业务数据
			if (creatResult != null) {
				String returnvalue = creatResult.getMessageBodyAsString();
				if (returnvalue != null && !returnvalue.equals("")) {
					JSONObject jobj = JSONObject.fromObject(returnvalue);
					String resultdata = jobj.getString("msg");
					String[] strarr = resultdata.split("-");
					AJID = strarr[0];
					map.put("AJID", AJID);
					map.put("ID", id);
					map.put("ACTINST_ID", actinst_id);
					map.put("OLDDAH", OLDDAH);
					map.put("backwrite", backwrite);
					if (strarr.length > 1) {
						map.put("DAH", strarr[1]);
					} else {
						map.put("DAH", "");
					}

					if (AJID != null && !AJID.equals("")) {
						Message business = archivesbookMapping.GetDJBXX(file_number);

						// logger.info(business.getMessageBodyAsString());
						if (business != null) {
							Message businessmsg = archivesworkFlow2Dossier
									.SendBusiness(business.getMessageBodyAsString(), AJID);
							// ----------打印发送业务信息的日志信息
							// logger.info(businessmsg.getMessageBodyAsString());
							if (businessmsg != null&&StringHelper.isNotNull(businessmsg.getMessageBodyAsString())) {
								String s=businessmsg.getMessageBodyAsString();
								jobj = JSONObject.fromObject(businessmsg.getMessageBodyAsString());
								OLDDAH = jobj.getString("msg");
								qlr = jobj.getString("tag");
								if (strarr.length > 1) {
									map.put("DAH", strarr[1]);
								} else {
									map.put("DAH", "");
								}

								if (OLDDAH != null && !OLDDAH.equals("null")) {
									map.put("OLDDAH", OLDDAH);
								}
							}
						}
						if (backwrite) {
							// logger.info("开始维护工作了的数据了！");
							archivesworkFlow2Dossier.MaintainDAH(id, DAH, AJID, actinst_id, OLDDAH, qlr);
							// dbService.PushArchivesState(file_number);
						}
					}
				}else {
					map.put("success", false);
					return map;
				}
				
			}
		}
		map.put("success", true);
		return map;
	}

	// 档案编号

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/createajh", method = RequestMethod.POST)
	public Map createAJH(HttpServletRequest request, HttpServletResponse response) {
		Map map = new HashMap();
		String ml = request.getParameter("ml");
		String flbh = request.getParameter("flbh");
		String lshs = request.getParameter("lshs");
		String sjys = request.getParameter("sjys");
		String isBox = request.getParameter("isBox");
		String staffid = request.getParameter("staffid");
		String staffname = request.getParameter("staffname");
		String currdah = request.getParameter("currdah");
		String currdag = request.getParameter("currdag");
		String ls = request.getParameter("ls");
		String type = request.getParameter("type");
		String NF = request.getParameter("NF");
		Map<String, String> param = new HashMap();
		param.put("ml", ml);
		param.put("flbh", flbh);
		param.put("lshs", lshs);
		param.put("sjys", sjys);
		param.put("isbox", isBox);
		param.put("staffid", staffid);
		param.put("staffname", staffname);
		param.put("dah", currdah);
		param.put("dag", currdag);
		param.put("auto", "true");
		param.put("zh", "");
		param.put("ls", ls);
		param.put("type", type);
		param.put("NF", NF);
		Message msg = archivesworkFlow2Dossier.CreatAJH(param);
		JSONObject jobj;
		String tip = null;
		String rows;
		if (msg != null&&StringHelper.isNotNull(msg.getMessageBodyAsString())) {
			jobj = JSONObject.fromObject(msg.getMessageBodyAsString());
			tip = jobj.getString("msg");
			rows = jobj.getString("rows");
			if (rows != null && !rows.equals("null")) {
				String lsh = null;
				String ajh = null;
				JSONArray jsonarr = JSONArray.fromObject(rows);
				String jsonstr = jsonarr.get(0).toString();
				JSONObject jsonObject = JSONObject.fromObject(jsonstr);
				if (StringHelper.isNotNull(jsonObject.getString("lsh"))) {
					lsh = jsonObject.getString("lsh");
				}
				if (StringHelper.isNotNull(jsonObject.getString("ajh"))) {
					ajh = jsonObject.getString("ajh");
				}

				if (StringHelper.isNotNull(ajh)) {
					map.put("AJH", ajh);
					projectService_da.UpdateXMXX(lsh, ajh);
				} else {
					map.put("AJH", "");
				}
			} else {
				map.put("AJH", "");
			}
		}
		map.put("tip", tip);
		return map;
	}

	/**
	 * 按照条件获取盒子
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/searchboxs", method = RequestMethod.POST)
	@ResponseBody
	public Page getboxByType(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		String Staff_ID = request.getParameter("Staff_ID");

		List<Map> list = null;
		Page page = null;
		StringBuilder sb = new StringBuilder();
		long totalcount = 0;
		if (StringHelper.isNotNull(Staff_ID)) {
			if (StringHelper.isNotNull(type)) {
				sb.append("from BDC_DAK.DAS_DAH where ZH like '" + "%" + type + "%" + "'  and STAFF_ID='" + Staff_ID
						+ "' and SFCM=0 ");
				totalcount = commonDao.getCountByFullSql(sb.append("  ").toString());
				if (totalcount > 0) {
					sb.append("    order by CREATE_TIME asc");
					if (pageIndex != null && pageSize != null) {
						int pageIndexs = Integer.parseInt(pageIndex);
						int pageSizes = Integer.parseInt(pageSize);
						list = commonDao.getPageDataByFullSql("select * " + sb.toString(), pageIndexs, pageSizes);
						page = new Page(pageIndexs, totalcount, pageSizes, list);
					} else {
						list = commonDao.getDataListByFullSql("select * " + sb.toString());
						page = new Page(1, totalcount, 1, list);

					}

					return page;

				} else {

					StringBuilder sbS = new StringBuilder();
					sbS.append("from BDC_DAK.DAS_DAH where ZH like '" + "%" + type + "%"
							+ "' AND STAFF_ID IS NULL  and SFCM=0 ");
					sbS.append("    order by CREATE_TIME asc");
					if (pageIndex != null && pageSize != null) {
						int pageIndexs = Integer.parseInt(pageIndex);
						int pageSizes = Integer.parseInt(pageSize);
						list = commonDao.getPageDataByFullSql("select * " + sbS.toString(), pageIndexs, pageSizes);
						page = new Page(pageIndexs, totalcount, pageSizes, list);
					} else {
						list = commonDao.getDataListByFullSql("select * " + sbS.toString());
						page = new Page(1, totalcount, 1, list);

					}

					return page;
				}
			} else {
				sb.append("from BDC_DAK.DAS_DAH WHERE STAFF_ID='" + Staff_ID + "' and SFCM=0 ");
				totalcount = commonDao.getCountByFullSql(sb.append("  ").toString());
			}
		}

		if (totalcount > 0) {

			sb.append("    order by CREATE_TIME asc");
			if (pageIndex != null && pageSize != null) {
				int pageIndexs = Integer.parseInt(pageIndex);
				int pageSizes = Integer.parseInt(pageSize);
				list = commonDao.getPageDataByFullSql("select * " + sb.toString(), pageIndexs, pageSizes);
				page = new Page(pageIndexs, totalcount, pageSizes, list);
			} else {
				list = commonDao.getDataListByFullSql("select * " + sb.toString());
				page = new Page(1, totalcount, 1, list);

			}

		} else {
			StringBuilder sbstr = new StringBuilder();

			sbstr.append("from BDC_DAK.DAS_DAH where STAFF_ID IS NULL and SFCM=0 ");

			long totalcounts = commonDao.getCountByFullSql(sbstr.append("  ").toString());
			if (totalcounts > 0) {

				sbstr.append("    order by CREATE_TIME asc");
				if (pageIndex != null && pageSize != null) {
					int pageIndexs = Integer.parseInt(pageIndex);
					int pageSizes = Integer.parseInt(pageSize);
					list = commonDao.getPageDataByFullSql("select * " + sbstr.toString(), pageIndexs, pageSizes);
					page = new Page(pageIndexs, totalcount, pageSizes, list);
				} else {
					list = commonDao.getDataListByFullSql("select * " + sbstr.toString());
					page = new Page(1, totalcount, 1, list);

				}

			}
		}
		return page;

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/GD/asyntask", method = RequestMethod.POST)
	@ResponseBody
	public void CreatAysnDATask(HttpServletRequest request, HttpServletResponse response) {

		StringBuilder sb = new StringBuilder();
		sb.append("select pinst.* from " + Common.WORKFLOWDB + "wfi_proinst pinst left join " + Common.WORKFLOWDB
				+ "wfi_actinst inst on pinst.proinst_id=inst.proinst_id where inst.actinst_status in (1,2)");
		sb.append(
				"and inst.actdef_type='5010' and inst.actinst_name='归档' and pinst. proinst_status<>0 and pinst.status is null");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");// 设置日期格式
		ajjbxxmap.put("CDSJ", df.format(new Date()));
		ajjbxxmap.put("BZ", "抽取归档");
		ajjbxxmap.put("backwrite", "false");
		ajjbxxmap.put("MLBH", "S");
		if (list != null && list.size() > 0) {
			for (Map p : list) {
				Object filenumber = p.get("FILE_NUMBER");
				Object id = p.get("PROINST_ID");
				if (filenumber != null && id != null) {
					smMaterialService.sendDossier(ajjbxxmap, filenumber.toString());
					Wfi_ProInst proinst = commonDao.get(Wfi_ProInst.class, id.toString());
					if (proinst != null) {
						proinst.setStatus(1);
						commonDao.update(proinst);
					}
				}
			}
			commonDao.flush();
		}

	}

	@RequestMapping(value = "/{file_number}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getArchivesInfos(@PathVariable String file_number, HttpServletRequest request,
												HttpServletResponse response) {
		Map<String, Object> workflow = smProInstService_DA.getArchivesInfos(file_number);
		Message business = archivesbookMapping.GetDJBXX(file_number);
		workflow.put("business", com.alibaba.fastjson.JSONObject.parseObject(business.getMessageBodyAsString()));
		return workflow;
	}

	@RequestMapping(value = "/2/{lsh}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getArchivesInfos2(@PathVariable String lsh, HttpServletRequest request,
												HttpServletResponse response) throws UnsupportedEncodingException {
		if(StringHelper.isNotNull(lsh)){
			List<Wfi_ProInst> listsproinst = commonDao.findList(Wfi_ProInst.class,"PROLSH='"+lsh+"'");
			if(listsproinst.size()>0){
				Wfi_ProInst proinst=listsproinst.get(0);
				Map<String, Object> workflow = smProInstService_DA.getArchivesInfos(proinst.getFile_Number());
				Message business = archivesbookMapping.GetDJBXX(proinst.getFile_Number());
				if (StringHelper.isNotNull(business.getMessageBodyAsString())) {
					String  businessstr = new String(business.getMessageBodyAsString().getBytes("iso8859-1"), "utf-8");
					workflow.put("business", com.alibaba.fastjson.JSONObject.parseObject(businessstr));
				}
				com.alibaba.fastjson.JSONObject businessinfo=com.alibaba.fastjson.JSONObject.parseObject(business.getMessageBodyAsString());
				JSONArray jsonarr = JSONArray.fromObject(businessinfo.get("CommonInfo"));
				workflow.put("business", businessinfo);
				if(jsonarr.size()>UNIT_COUNT){
					StringToFile(JSONObject.fromObject(workflow).toString(),lsh);
					workflow.put("business", "many");
				}
				workflow.put("TENANT_ID", "");
				return workflow;

			}
		}
		return null;
	}
	/**
	 * 
	 * @param file_number
	 * @param request
	 * @param response
	 * @return  当前年份 ,发证年份,登簿年份
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/getfzsj/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public Map getfzsj(@PathVariable String file_number, HttpServletRequest request, HttpServletResponse response) {
		Date data=null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat();  
	    formater.applyPattern("yyyy");  
		int year = cal.get(Calendar.YEAR);
		Map<String, String> yearMap=new HashMap<String, String>();
		try {
			if(file_number!=null) {
				List<BDCS_DJFZ>  djfz=commonDao.findList(BDCS_DJFZ.class,"YWH='"+file_number+"'");
				if(djfz!=null&&djfz.size()>0) {
					data=djfz.get(0).getFZSJ();
				}
			}
			if(data==null) {
				 List<BDCS_QL_GZ>  ql=commonDao.findList(BDCS_QL_GZ.class," XMBH ='"+Global.getXMXX(file_number).getId()+"'");
				 if(ql!=null&&ql.size()>0) {
					 data=ql.get(0).getDJSJ();
					 yearMap.put("dbsj", formater.format(data));
				 }
			}else {
				 yearMap.put("fzsj", formater.format(data));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 yearMap.put("year", year+"");
		return yearMap;
		
	}
	/**
	 * 档案归档大件生成数据摆渡文件
	 * @param fileContent
	 * @param lsh
	 */
	public  static void StringToFile(String fileContent,String lsh)
	{

		try
		{
			File f = new File("D:\\"+lsh+".txt");
			if (!f.exists())
			{
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"gbk");
			BufferedWriter writer=new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

