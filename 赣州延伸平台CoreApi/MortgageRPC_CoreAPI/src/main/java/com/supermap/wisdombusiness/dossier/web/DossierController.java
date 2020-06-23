package com.supermap.wisdombusiness.dossier.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supemap.mns.client.AsyncCallback;
import com.supemap.mns.client.AsyncResult;
import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.dossier.web.common.BookMapping;
import com.supermap.wisdombusiness.dossier.web.common.DossierOption;
import com.supermap.wisdombusiness.dossier.web.common.WorkFlow2Dossier;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.WFI_TRANSFERLIST;
import com.supermap.wisdombusiness.workflow.service.common.SFTPEx;

@Controller
@RequestMapping("/bdc/dossier")
public class DossierController {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private WorkFlow2Dossier workFlow2Dossier;
	@Autowired
	private BookMapping bookMapping;
	@Autowired
	private DossierOption dossierOption;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowIndex(Model model) {

		return "";
	}

	@RequestMapping(value = "/GD/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public Map CreatDA(@PathVariable String file_number,
			HttpServletRequest request, HttpServletResponse response) {
		String actinst_id = StringHelper.ObjToString(request
				.getParameter("actinstid"));
//		String AJH = StringHelper.ObjToString(request.getParameter("AJH"));
//		String WJJS = StringHelper.ObjToString(request.getParameter("WJJS"));
//		String WJYS = StringHelper.ObjToString(request.getParameter("WJYS"));
//		String LRR = StringHelper.ObjToString(request.getParameter("LRR"));
//		String CDSJ = StringHelper.ObjToString(request.getParameter("CDSJ"));
//		String BZ = StringHelper.ObjToString(request.getParameter("BZ"));
//		Map<String, String> ajjbxxmap = new HashMap<String, String>();
//		ajjbxxmap.put("AJH", AJH);
//		ajjbxxmap.put("WJJS", WJJS);
//		ajjbxxmap.put("WJYS", WJYS);
//		ajjbxxmap.put("LRR", LRR);
//		ajjbxxmap.put("CDSJ", CDSJ);
//		ajjbxxmap.put("BZ", BZ);
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("AJH", "");
		ajjbxxmap.put("WJJS", "");
		ajjbxxmap.put("WJYS", "");
		ajjbxxmap.put("LRR", "熊惠");
		ajjbxxmap.put("CDSJ", StringHelper.DateTimeToStr((new Date())));
		ajjbxxmap.put("BZ", "");
		return GD(file_number, actinst_id, ajjbxxmap);
	}

	@RequestMapping(value = "/GD/asyn/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public Map CreatAysnDA(@PathVariable String file_number,
			HttpServletRequest request, HttpServletResponse response) {
		SFTPEx ex = new SFTPEx();
		ex.timerrunner();
		String actinst_id = StringHelper.ObjToString(request
				.getParameter("actinstid"));
		String AJH = StringHelper.ObjToString(request.getParameter("AJH"));
		String WJJS = StringHelper.ObjToString(request.getParameter("WJJS"));
		String WJYS = StringHelper.ObjToString(request.getParameter("WJYS"));
		String LRR = StringHelper.ObjToString(request.getParameter("LRR"));
		String CDSJ = StringHelper.ObjToString(request.getParameter("CDSJ"));
		String BZ = StringHelper.ObjToString(request.getParameter("BZ"));
		String id = StringHelper.ObjToString(request.getParameter("id"));
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("AJH", AJH);
		ajjbxxmap.put("WJJS", WJJS);
		ajjbxxmap.put("WJYS", WJYS);
		ajjbxxmap.put("LRR", LRR);
		ajjbxxmap.put("CDSJ", CDSJ);
		ajjbxxmap.put("BZ", BZ);
		Map map = new HashMap();
		String AJID = "";
		String DAH = "";
		Message projectinfo = workFlow2Dossier.workflowDossierInfo(file_number,
				actinst_id);
		if (projectinfo != null) {
			String msg = projectinfo.getMessageBodyAsString();
			// Message del = workFlow2Dossier.DeleteAj(file_number);
			Map<String, String> DA = workFlow2Dossier.Setmaterial2DossierEx(
					msg, ajjbxxmap);
			Message creatResult = workFlow2Dossier.CreatAJ(DA);
			if (creatResult != null) {
				String returnvalue = creatResult.getMessageBodyAsString();
				if (returnvalue != null && !returnvalue.equals("")) {
					JSONObject jobj = JSONObject.fromObject(returnvalue);
					AJID = jobj.getString("success");
					map.put("AJID", AJID);
					DAH = jobj.getString("msg");
					map.put("DAH", DAH);
					dossierOption.GDOptionEx(file_number, actinst_id, AJID, DA);
					if (id != null) {
						workFlow2Dossier.MaintainDAH(id, DAH, AJID);
					}
				}
			}
		}
		return map;

	}

	// 档案数据抽取服务
	@ResponseBody
	@RequestMapping(value = "/swap", method = RequestMethod.GET)
	public String DossierSwap(HttpServletRequest request,
			HttpServletResponse response) {
		String swapcount = request.getParameter("count");
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("AJH", "");
		ajjbxxmap.put("WJJS", "");
		ajjbxxmap.put("WJYS", "");
		ajjbxxmap.put("LRR", "熊惠");
		//ajjbxxmap.put("CDSJ", "2016-01-26");
		ajjbxxmap.put("CDSJ", StringHelper.DateTimeToStr((new Date())));
		ajjbxxmap.put("BZ", "");
		Integer count = 0;
		Message msg = workFlow2Dossier.GetAllSwapDossier();
		if (msg != null) {
			JSONArray jobj = JSONArray.fromObject(msg.getMessageBodyAsString());
			count = jobj.size();
			for (int i = 0; i < count; i++) {
				JSONObject json = jobj.getJSONObject(i);
				if (json != null) {
					String f = json.get("FILE_NUMBER").toString();
					String actid = json.get("ACTINST_ID").toString();
					if (StringHelper.isNotNull(actid, f)) {
						GD(f, actid, ajjbxxmap);
					}
				}
			}
		}
		return "抽取完毕，抽取" + count;
	}

	// 档案数据抽取服务,不重复抽取数据的服务
	@ResponseBody
	@RequestMapping(value = "/swapnorepeat", method = RequestMethod.GET)
	public String DossierSwapNoRepeat(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		ajjbxxmap.put("AJH", "");
		ajjbxxmap.put("WJJS", "");
		ajjbxxmap.put("WJYS", "");
		ajjbxxmap.put("LRR", "熊惠");
		ajjbxxmap.put("CDSJ", "2016-01-26");
		ajjbxxmap.put("BZ", "");
		Integer count = 0;
		Integer GDcount = 0;
		Message msg = workFlow2Dossier.GetAllSwapDossier();
		if (msg != null) {
			JSONArray jobj = JSONArray.fromObject(msg.getMessageBodyAsString());
			count = jobj.size();
			for (int i = 0; i < count; i++) {
				JSONObject json = jobj.getJSONObject(i);
				if (json != null) {
					String f = json.get("FILE_NUMBER").toString();
					String actid = json.get("ACTINST_ID").toString();
					if (StringHelper.isNotNull(actid, f)) {
						Message isGDAJ = workFlow2Dossier.isGDAj(f);
						if (isGDAJ != null) {
							String isGDAJvalue = isGDAJ
									.getMessageBodyAsString();
							if (StringHelper.isNotNull(isGDAJvalue)) {
								JSONObject jogdbj = JSONObject
										.fromObject(isGDAJvalue);
								String gdzt = jogdbj.getString("msg");
								if (StringHelper.isNotNull(gdzt)
 										&& gdzt.equals("已归档")) {
								} else {									
									GD(f, actid, ajjbxxmap);
									GDcount++;
								}
							}

						}
					}
				}
			}
		}
		return "抽取完毕，抽取" + GDcount;
	}

	private Map GD(String file_number, String actinst_id,
			Map<String, String> ajjbxxmap) {
		Map map = new HashMap();
		String AJID = "";
		String DAH = "";
		Message projectinfo = workFlow2Dossier.GetProjectInfo(file_number);
		if (projectinfo != null) {
			Message materials = workFlow2Dossier
					.GetWorkFlowMaterial(file_number);
			String meterial = materials.getMessageBodyAsString();
			Message del = workFlow2Dossier.DeleteAj(file_number);
			Map DA = workFlow2Dossier.Setmaterial2DossierEx(
					projectinfo.getMessageBodyAsString(), meterial, ajjbxxmap);
			Message creatResult = workFlow2Dossier.CreatAJ(DA);
			// 发送业务数据
			if (creatResult != null) {
				String returnvalue = creatResult.getMessageBodyAsString();
				if (returnvalue != null && !returnvalue.equals("")) {
					JSONObject jobj = JSONObject.fromObject(returnvalue);
					AJID = jobj.getString("success");
					map.put("AJID", AJID);
					DAH = jobj.getString("msg");
					map.put("DAH", DAH);
					if (!AJID.equals("-1")) {
						Message business = bookMapping.GetDJBXX(file_number);
						if (business != null) {
							workFlow2Dossier.SendBusiness(
									business.getMessageBodyAsString(), AJID);
						}
						// 发送资料详细数据--同步测试，可以考虑开始使用异步
						JSONArray JNWJ_FJ = JSONArray.fromObject(DA
								.get("JNWJ_FJ"));
						if (JNWJ_FJ != null && JNWJ_FJ.size() > 0) {
							for (int i = 0; i < JNWJ_FJ.size(); i++) {
								JSONObject json = JNWJ_FJ.getJSONObject(i);
								Object Materid = json.get("FJ_ID");
								if (Materid != null) {
									String imagebase64 = "";

									if (json.get("desc") == null) {
										Message m = workFlow2Dossier
												.MaterialData(Materid
														.toString());
										byte[] image = m
												.getMessageBodyAsBytes();
										if (image != null) {
											imagebase64 = Base64
													.encodeBase64String(image);
										} else {
											imagebase64 = "";
										}

									} else {
										String desc = json.get("desc")
												.toString();
										if (desc.equals("sqspb")) {// 发送额外资料信息
											// 1、申请审批表信息
											Message sqspb = bookMapping
													.GetSQSPB(file_number,
															actinst_id);
											if (sqspb != null) {
												byte[] image = sqspb
														.getMessageBodyAsBytes();
												imagebase64 = Base64
														.encodeBase64String(image);
											}
										} else if (desc.equals("zszm")) {
											// 2、不动产权证存根
											String workflowcode = ConfigHelper
													.getNameByValue("SFGDCODE");
											String sfgdcodes = ProjectHelper
													.getWorkflowCodeByProjectID(file_number);
											boolean flag = true;//
											if (sfgdcodes != ""
													&& workflowcode != "") {
												if (sfgdcodes
														.contains(workflowcode)) {
													flag = false;
												}
											}
											if (flag) {
												Message zszm = bookMapping
														.GetZSZM(file_number);
												if (zszm != null) {
													byte[] image = zszm
															.getMessageBodyAsBytes();
													imagebase64 = Base64
															.encodeBase64String(image);
												}
											}
										}
									}
									if (!imagebase64.equals("")) {
										Message fjResult = workFlow2Dossier
												.UploadFJ(Materid.toString(),
														imagebase64);
									}

								}
							}
						}
					}
				}
			}

		}
		return map;
	}

	// 已归档的档案补抽取不动产权证存根、受理单
	@ResponseBody
	@RequestMapping(value = "/swapqzcg", method = RequestMethod.GET)
	public String DossierSwapQZCG(HttpServletRequest request,
			HttpServletResponse response) {
		Integer GDcount = 0;
		Integer count = 0;
		Message msg = workFlow2Dossier.GetAllSwapDossier();
		if (msg != null) {
			JSONArray jobj = JSONArray.fromObject(msg.getMessageBodyAsString());
			count = jobj.size();
			for (int i = 0; i < count; i++) {
				JSONObject json = jobj.getJSONObject(i);
				if (json != null) {
					String file_number = json.get("FILE_NUMBER").toString(); //
					String actid = json.get("ACTINST_ID").toString();
					if (StringHelper.isNotNull(actid, file_number)) {
						Message isGDAJ = workFlow2Dossier.isGDAj(file_number);
						if (isGDAJ != null) {
							String isGDAJvalue = isGDAJ
									.getMessageBodyAsString();
							if (StringHelper.isNotNull(isGDAJvalue)) {
								JSONObject jogdbj = JSONObject
										.fromObject(isGDAJvalue);
								String gdzt = jogdbj.getString("msg");
								if (StringHelper.isNotNull(gdzt)
										&& gdzt.equals("已归档")) {
									//已归档的项目
									String ajid = jogdbj.getString("success"); //获取已归档的案卷ID
									if(StringHelper.isNotNull(ajid)){
										
										String imagebase64 = "";
										//不动产权证存根
										String workflowcode = ConfigHelper
												.getNameByValue("SFGDCODE");
										String sfgdcodes = ProjectHelper
												.getWorkflowCodeByProjectID(file_number);
										boolean flag = true;//
										if (sfgdcodes != ""
												&& workflowcode != "") {
											if (sfgdcodes
													.contains(workflowcode)) {
												flag = false;
											}
										}
										if (flag) {
											Message zszm = bookMapping
													.GetZSZM(file_number);
											if (zszm != null) {
												byte[] image = zszm
														.getMessageBodyAsBytes();
												imagebase64 = Base64
														.encodeBase64String(image);
											}
										}
									
										if (!imagebase64.equals("")) {
											//UploadFJByFJMC(String ajid,String wjtm,String fjid, String file)
											Message fjResult = workFlow2Dossier
													.UploadFJByTM(ajid,
															"BDCQZCG",imagebase64);
										}
										//受理单,受理单为网页暂时无法提取保存为空
										Message fjResult = workFlow2Dossier
												.UploadFJByTM(ajid,
														"SLD","");
									}
									
								} 
							}

						}
					}
				}
			}
		}
		return "抽取完毕，抽取" + GDcount;
	}
	
}
