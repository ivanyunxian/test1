package com.supermap.wisdombusiness.dossier.web.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import oracle.net.aso.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.common.HttpMethod;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ProjectHelper;

@Component("workFlow2Dossier")
public class WorkFlow2Dossier {
	// 获取收件资料
	private final String getMeterial = "frame/hasacceptmater/";
	// 获取项目信息
	private final String getProject = "frame/getprojectinfo/";
	// 创建档案
	private final String creatAJ = "EditAjjbxx/insertAj";

	// 获取档案资料
	private final String MaterialData = "frame/wfipromater/imagedownload/";

	// 上传文件

	private final String Uploadurl = "EditAjjbxx/insertFj";

	// 传递业务信息
	private final String Businessurl = "EditAjjbxx/insertBdcywxx";

	// 通过FILENUMBER查询AJID
	private final String Searchajidurl = "EditAjjbxx/Search_Ajid";
	
	// 通过XMBH查询AJID
	private final String Searchajidbyxmbhurl = "EditAjjbxx/Search_Ajidbyxmbh";

	// 通过FILENUMBER删除AJID
	private final String DelAjurl = "EditAjjbxx/deleteGDAJ/";
	
	// 通过FILENUMBER判断AJ是否已归档
	private final String isGDPAjurl = "EditAjjbxx/isGDAJ/";
	// 抽取档案数据
	private final String swapDossier = "/bdc/dossier/GD/";

	// 获取所有等待抽取档案
	private final String alldossierProject = "frame/all/dossierproject";
	
	private final String workflowDossier="frame/workflow/dossier/";

	//抽取不动产权证存根
	private final String Uploadfjbymcurl = "EditAjjbxx/insertFjByFjmc/";
	
	public Message GetProjectInfo(String File_Number) {
		if (File_Number != null && !File_Number.equals("")) {
			return Basic.WorkFlowRequest(getProject + File_Number,
					new HashMap<String, String>(), HttpMethod.GET);
		} else {
			return null;
		}
	}

	public Message GetAllSwapDossier() {
		return Basic.WorkFlowRequest(alldossierProject,
				new HashMap<String, String>(), HttpMethod.GET);

	}
	public void MaintainDAH(String id,String dah,String ajid){
		Map map=new HashMap<String, String>();
		map.put("dah", dah);
		map.put("ajid", ajid);
		Basic.aysnWorkflowPost(map,"material/transferlist/adddah/"+id);
	}
	
	public void MaintainFJ(String file_number){
		Map map=new HashMap<String, String>();
		Basic.aysnWorkflowPost(map,"material/transferlist/fj/"+file_number);
	}
	public Message workflowDossierInfo(String File_Number,String actinst){
		if (File_Number != null && !File_Number.equals("")) {
			Map map=new HashMap<String, String>();
			map.put("actinstid", actinst);
			return Basic.WorkFlowRequest(workflowDossier + File_Number,
					map, HttpMethod.POST);
		} else {
			return null;
		}
	}

	public Message GetWorkFlowMaterial(String File_Number) {

		if (File_Number != null && !File_Number.equals("")) {
			return Basic.WorkFlowRequest(getMeterial + File_Number,
					new HashMap<String, String>(), HttpMethod.GET);
		} else {
			return null;
		}
	}

	/**
	 * 创建案卷
	 * 
	 * @param params
	 * @return
	 */
	public Message CreatAJ(Map<String, String> params) {
		return Basic.DossierRequest(creatAJ, params, HttpMethod.POST);

	}

	/**
	 * 获取创建档案返回的
	 * 
	 * @param info
	 * @return
	 */
	public String GetAJID(String info) {
		JSONObject jsonobj = JSONObject.fromObject(info);
		if (jsonobj != null) {
			return jsonobj.get("success").toString();
		} else {
			return "";
		}

	}

	/**
	 * 转换项目信息
	 * 
	 * @param projectinfo
	 * @return
	 */

	public Map<String, String> ConvertProjectToAJ(String projectinfo) {
		Map<String, String> mapping = SetWorkflow2Dossier(projectinfo);
		// Object name=jsonobj.get("proInst_Name");
		return mapping;
	}

	/**
	 * 基于各个信息合并信息
	 * 
	 * @return
	 */
	private Map<String, String> CollectData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AJJBXX", "");
		map.put("Material", "");
		map.put("Business", "");
		return map;
	}

	/**
	 * 案件相关
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> SetWorkflow2Dossier(String projectinfo) {
		// 字段映射关系
		Map<String, String> map = new HashMap<String, String>();
		map.put("proInst_Name", "AJBT");
		map.put("file_Number", "FILE_NUMBER");
		map.put("proLSH", "PROLSH");
		// 装载对象
		return Basic.GetMappingData(projectinfo, map);
	}
	public Map<String, String> Setmaterial2DossierEx(String info,Map<String, String> ajjbxx){
		JSONObject ajjbxxJson = JSONObject.fromObject(info);
		String project=ajjbxxJson.getString("project");
		String materil=ajjbxxJson.getString("materil");
		return Setmaterial2DossierEx(project,materil,ajjbxx);
	}
	/**
	 * 资料相关
	 * 
	 * @return
	 */

	public Map<String, String> Setmaterial2DossierEx(String projectinfo,
			String info, Map<String, String> ajjbxx) {
		// 字段映射关系
		Map<String, String> result = new HashMap<String, String>();
		// AJJBXX
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("proInst_Name", "AJBT");
		data.put("file_Number", "FILE_NUMBER");
		data.put("proLSH", "PROLSH");
		ajjbxxmap = Basic.GetMappingData(projectinfo, data);
		if (ajjbxx != null && ajjbxx.size() > 0) {
			ajjbxxmap.put("AJH", ajjbxx.get("AJH").toString());
			ajjbxxmap.put("WJJS", ajjbxx.get("WJJS").toString());
			ajjbxxmap.put("WJYS", ajjbxx.get("WJYS").toString());
			ajjbxxmap.put("LRR", ajjbxx.get("LRR").toString());
			ajjbxxmap.put("CDSJ", ajjbxx.get("CDSJ").toString());
			ajjbxxmap.put("BZ", ajjbxx.get("BZ").toString());
		}

		// JNWJ
		Map<String, String> jnwjMapping = new HashMap<String, String>();
		jnwjMapping.put("id", "WJID");
		jnwjMapping.put("text", "TM");
		jnwjMapping.put("children", "WJ_FJ");
		// WJ_FJ
		Map<String, String> wj_fjmap = new HashMap<String, String>();
		wj_fjmap.put("id", "FJ_ID");
		wj_fjmap.put("text", "FJMC");
		// 装载对象
		// 获取卷内文件信息
		List<Map> jnwjfjs = new ArrayList<Map>();
		List<Map> jnwjs = Basic.GetArraryMappingData(info, jnwjMapping);
		if (jnwjs != null && jnwjs.size() > 0) {
			for (Map m : jnwjs) {
				Map<String, String> exData = new HashMap<String, String>();
				exData.put("WJID", m.get("WJID").toString());
				List<Map> JNWJFJ = Basic.GetArraryMappingData(m.get("WJ_FJ")
						.toString(), wj_fjmap, exData);
				for (Map jnwjfjMap : JNWJFJ) {
					String filename = jnwjfjMap.get("FJMC").toString();
					String filetype = filename.substring(filename
							.lastIndexOf("."));
					jnwjfjMap.put("FILETYPE", filetype);
				}
				jnwjfjs.addAll(JNWJFJ);
				m.remove("WJ_FJ");
			}
		}
		String sqspb=ConfigHelper.getNameByValue("SFGDSQSPB");
		if(sqspb!=null&&!sqspb.equals("")&&sqspb.equals("1")){
			AddExFile2Dossier(jnwjs, jnwjfjs); //申请审批表
		}
		String zszm=ConfigHelper.getNameByValue("SFGDZSZM");
		if(zszm!=null&&!zszm.equals("")&&zszm.equals("1")){
			AddZSZMFile2Dossier(jnwjs, jnwjfjs);//不动产权证明证书存根
		}
		String sld=ConfigHelper.getNameByValue("SFGDSLD");
		if(sld!=null&&!sld.equals("")&&sld.equals("1")){
			AddSLDFile2Dossier(jnwjs, jnwjfjs); //添加受理单
		}
		JSONObject ajjbxxJson = JSONObject.fromObject(ajjbxxmap);
		JSONArray jnwjJson = JSONArray.fromObject(jnwjs);
		JSONArray jnwjfjJson = JSONArray.fromObject(jnwjfjs);
		result.put("AJJBXX", ajjbxxJson.toString());
		result.put("JNWJ", jnwjJson.toString());
		result.put("JNWJ_FJ", jnwjfjJson.toString());
		return result;
	}

	/**
	 * 添加额外的文件信息
	 * 
	 * @param jnwjs
	 * @param jnwjfjs
	 */
	@SuppressWarnings("unchecked")
	private void AddExFile2Dossier(List<Map> jnwjs, List<Map> jnwjfjs) {
		String[] files = new String[] { "申请审批表.pdf" };

		for (String file : files) {
			@SuppressWarnings("rawtypes")
			Map jnwj = new HashMap();
			String wjid = Basic.CreatUUID();
			jnwj.put("WJID", wjid);
			jnwj.put("TM", file.substring(0, file.indexOf(".")));
			jnwjs.add(jnwj);

			Map wjfjs = new HashMap();
			wjfjs.put("FJ_ID", Basic.CreatUUID());
			wjfjs.put("FJMC", file);
			wjfjs.put("FILETYPE", ".pdf");
			wjfjs.put("WJID", wjid);
			wjfjs.put("desc", "sqspb");// 标示这个件不是获取的是创建的
			jnwjfjs.add(wjfjs);
		}

	}

	/**
	 * 添加额外的文件信息
	 * 
	 * @param jnwjs
	 * @param jnwjfjs
	 */
	@SuppressWarnings("unchecked")
	private void AddZSZMFile2Dossier(List<Map> jnwjs, List<Map> jnwjfjs) {
		String[] files = new String[] { "不动产权证存根.pdf" };

		for (String file : files) {
			@SuppressWarnings("rawtypes")
			Map jnwj = new HashMap();
			String wjid = Basic.CreatUUID();
			jnwj.put("WJID", wjid);
			jnwj.put("TM", file.substring(0, file.indexOf(".")));
			jnwjs.add(jnwj);

			Map wjfjs = new HashMap();

			wjfjs.put("FJ_ID", Basic.CreatUUID());
			wjfjs.put("FJMC", file);
			wjfjs.put("FILETYPE", ".pdf");
			wjfjs.put("WJID", wjid);
			wjfjs.put("desc", "zszm");// 标示这个件不是获取的是创建的
			jnwjfjs.add(wjfjs);
		}

	}
	/**
	 * 添加额外的文件信息
	 * 
	 * @param jnwjs
	 * @param jnwjfjs
	 */
	@SuppressWarnings("unchecked")
	private void AddSLDFile2Dossier(List<Map> jnwjs, List<Map> jnwjfjs) {
		String[] files = new String[] { "受理单.pdf" };

		for (String file : files) {
			@SuppressWarnings("rawtypes")
			Map jnwj = new HashMap();
			String wjid = Basic.CreatUUID();
			jnwj.put("WJID", wjid);
			jnwj.put("TM", file.substring(0, file.indexOf(".")));
			jnwjs.add(jnwj);

			/*****受理单暂时无法获取，目前不提取受理单********/
//			Map wjfjs = new HashMap();
//
//			wjfjs.put("FJ_ID", Basic.CreatUUID());
//			wjfjs.put("FJMC", file);
//			wjfjs.put("FILETYPE", ".pdf");
//			wjfjs.put("WJID", wjid);
//			wjfjs.put("desc", "sld");// 标示这个件不是获取的是创建的
//			jnwjfjs.add(wjfjs);
		}

	}
	/**
	 * 业务相关
	 * 
	 * @return
	 */

	private Map<String, String> SetBusiness2Dossier() {

		return null;
	}

	public Message MaterialData(String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileName", "");
		return Basic.WorkFlowRequest(MaterialData + id, map, HttpMethod.GET);

	}
	public Message UploadFJ(String fjid, String file) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FJID", fjid);
		map.put("Filedata", file);
		return Basic.DossierRequest(Uploadurl, map, HttpMethod.POST);
	}
	public Message UploadFJ(String fjid, String file,String dossierService) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FJID", fjid);
		map.put("Filedata", file);
		return Basic.DossierRequest(dossierService,Uploadurl, map, HttpMethod.POST);
	}

	/**
	 * 
	 * @param fjid
	 * @param jnwjmc
	 * @param file
	 * @return
	 */
	public Message UploadFJEx(String fjid, String jnwjmc, String file) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FJID", fjid);
		map.put("Filedata", file);
		map.put("jnwjmc", jnwjmc);
		return Basic.DossierRequest(Uploadurl, map, HttpMethod.POST);
	}

	/**
	 * 发送业务数据
	 * 
	 * @param business
	 * @param ajid
	 * @return
	 */
	public Message SendBusiness(String business, String ajid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AJID", ajid);
		map.put("Business", business);
		return Basic.DossierRequest(Businessurl, map, HttpMethod.POST);
	}

	/**
	 * 通过FileNumber删除AJ
	 * 
	 * @param params
	 * @return
	 */
	public Message DeleteAj(String File_Number) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("filenumber", File_Number);
		Message sMessage = null;
		sMessage = Basic.DossierRequest(DelAjurl + File_Number, map,
				HttpMethod.POST);
		return sMessage;
	}

	/**
	 * 通过FileNumber查询AJID
	 * 
	 * @param params
	 * @return
	 */
	public Message SearchAjid(String File_Number) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("file_number", File_Number);
		Message sMessage = null;
		sMessage = Basic.DossierRequest(Searchajidurl, map, HttpMethod.GET);
		return sMessage;
	}

	//SearchAjidByXMBH
	public Message SearchAjidByXMBH(String XMBH) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("xmbh", XMBH);
		Message sMessage = null;
		sMessage = Basic.DossierRequest(Searchajidbyxmbhurl, map, HttpMethod.GET);
		return sMessage;
	}
	/**
	 * 
	 * @return
	 */
	public Message SwapDossier(String file_numner, String actinstid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("actinstid", actinstid);
		return Basic.WorkFlowRequest(swapDossier + file_numner, map,
				HttpMethod.GET);
	}
	
	/**
	 * 通过FileNumber判断AJ是否已归档
	 * 
	 * @param params
	 * @return
	 */
	public Message isGDAj(String File_Number) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("filenumber", File_Number);
		Message sMessage = null;
		sMessage = Basic.DossierRequest(isGDPAjurl + File_Number, map,
				HttpMethod.POST);
		return sMessage;
	}

	
	public Message UploadFJByTM(String ajid,String tm, String file) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AJID", ajid);
		map.put("WJTM", tm);
		map.put("Filedata", file);
		return Basic.DossierRequest(Uploadfjbymcurl, map, HttpMethod.POST);
	}
}
