package com.supermap.wisdombusiness.archives.web.common;

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
import com.supermap.realestate.registration.util.StringHelper;
 

@Component("archivesworkFlow2Dossier")
public class WorkFlow2Dossier {
	// 获取收件资料
	private final String getMeterial = "frame_da/hasacceptmater/";
	// 获取项目信息
	private final String getProject = "frame_da/getprojectinfo/";
	// 创建档案
	private final String creatAJ = "swap/add";
	// 创建档案号
	private final String creatAJH = "arrange/setajbh";

	// 获取档案资料
	private final String MaterialData = "frame_da/wfipromater/imagedownload/";

	// 上传文件

	private final String Uploadurl = "";

	// 传递业务信息
	private final String Businessurl = "swap/add/business";

	// 通过FILENUMBER查询AJID
	private final String Searchajidurl = "EditAjjbxx/Search_Ajid";

	// 通过FILENUMBER删除AJID
	private final String DelAjurl = "";
	// 抽取档案数据
	//private final String swapDossier = "/bdc/dossier/GD/";

	// 获取所有等待抽取档案
	private final String alldossierProject = "/material/havedossier/project";
	
	private final String workflowDossier="frame_da/workflow/dossier/";
	
	private final String allProjectactName="frame_da/allproject/actinst";
	
	
	//获取办结为在档案库中检测到的项目
	private final String nullArvhices="maintain/list/project_da/nullto/archives";
	private final String nullArvhicesDDD="maintain/list/project_da/nullto/archives/ddd";

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
	public void MaintainDAH(String id,String dah,String ajid,String actinstid,String olddah,String qlr){
		Map map=new HashMap<String, String>();
		map.put("dah", dah);
		map.put("ajid", ajid);
		map.put("actinstid", actinstid);
		map.put("olddah", olddah);
		map.put("qlr", qlr);
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
			Map m=new HashMap<String, String>();
			m.put("clear", "false");
			return Basic.WorkFlowRequest(getMeterial + File_Number,
					m, HttpMethod.GET);
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
	public Map<String, String> Setmaterial2DossierEx(String info,Map<String, String> ajjbxx,String DJDL,String DJXL){
		JSONObject ajjbxxJson = JSONObject.fromObject(info);
		String project=ajjbxxJson.getString("project");
		String materil=ajjbxxJson.getString("materil");
		return Setmaterial2DossierEx(project,materil,ajjbxx,DJXL,DJXL);
	}
	/**
	 * 资料相关
	 * 
	 * @return
	 */

	@SuppressWarnings("unused")
	public Map<String, String> Setmaterial2DossierEx(String projectinfo,
			String info, Map<String, String> ajjbxx,String DJDL,String DJXL) {
		// 字段映射关系
		Map<String, String> result = new HashMap<String, String>();
		// AJJBXX
		Map<String, String> ajjbxxmap = new HashMap<String, String>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("proInst_Name", "AJBT");
		data.put("file_Number", "PROJECT_ID");
		data.put("proLSH", "YWDH");
		//data.put("proDef_ID", "PRODEFMAINID");
		data.put("acceptor", "SLRY");
		data.put("lcbh", "LCBH");
		data.put("proStartTime", "SLRQ");
		
		ajjbxxmap = Basic.GetMappingData(projectinfo, data);
		if (ajjbxx != null && ajjbxx.size() > 0) {
			//ajjbxxmap.put("AJH", ajjbxx.get("AJH").toString());
			ajjbxxmap.put("LRR", ajjbxx.get("LRR").toString());
			ajjbxxmap.put("BZ", ajjbxx.get("BZ").toString());
			if(ajjbxx.get("MLBH")!=null){
				ajjbxxmap.put("MLBH", ajjbxx.get("MLBH").toString());
			}else{
				ajjbxxmap.put("MLBH", null);
			}
			
			if(ajjbxx.get("DAGBH")!=null){
				ajjbxxmap.put("DAGBH", ajjbxx.get("DAGBH").toString());
			}else{
				ajjbxxmap.put("DAGBH", null);
			}
			if(ajjbxx.get("DAHBH")!=null){
				ajjbxxmap.put("DAHBH", ajjbxx.get("DAHBH").toString());
			}else{
				ajjbxxmap.put("DAHBH", null);
			}
			if(ajjbxx.get("DACBH")!=null){
				ajjbxxmap.put("DACBH", ajjbxx.get("DACBH").toString());
			}else{
				ajjbxxmap.put("DACBH", null);
			}
			if(ajjbxx.get("WJJS")!=null){
				ajjbxxmap.put("WJJS", ajjbxx.get("WJJS").toString());
			}else{
				ajjbxxmap.put("WJJS", null);
			}
			if(ajjbxx.get("WJYS")!=null){
				ajjbxxmap.put("WJYS", ajjbxx.get("WJYS").toString());
			}else{
				ajjbxxmap.put("WJYS", null);
			}
			if(ajjbxx.get("AJH")!=null){
				ajjbxxmap.put("AJH", ajjbxx.get("AJH").toString());
			}else{
				ajjbxxmap.put("AJH", null);
			}
			
		}

		// JNWJ
		Map<String, String> jnwjMapping = new HashMap<String, String>();
		jnwjMapping.put("id", "Id");
		jnwjMapping.put("text", "TM");
		jnwjMapping.put("tag1", "WJLX");//文件类型 2 收件资料 3 推送资料
		jnwjMapping.put("tag2", "YJFS");//文件份数
		jnwjMapping.put("flag","BLZT");//是否归档
		jnwjMapping.put("tag3","SXH");//顺序号
		jnwjMapping.put("children", "WJ_FJ");
		// WJ_FJ
		Map<String, String> wj_fjmap = new HashMap<String, String>();
		wj_fjmap.put("id", "FJID");
		//wj_fjmap.put("text", "FJMC");
		wj_fjmap.put("tag1", "PATH");//文件路径
		wj_fjmap.put("tag2","FJMC");
		wj_fjmap.put("tag4","DISC");
		// 装载对象
		// 获取卷内文件信息
		List<Map> jnwjfjs = new ArrayList<Map>();
		List<Map> jnwjs = Basic.GetArraryMappingData(info, jnwjMapping);
		if (jnwjs != null && jnwjs.size() > 0) {
			for (Map m : jnwjs) {
				Map<String, String> exData = new HashMap<String, String>();
				exData.put("WJID", m.get("Id").toString());
				int fs=1;
				if(m.get("YJFS")!=null&&!m.get("YJFS").equals("null")){
					//fs=Integer.parseInt(m.get("YJFS").toString());
					
				}
				m.put("YS", jnwjs.size()*fs);
				List<Map> JNWJFJ = Basic.GetArraryMappingData(m.get("WJ_FJ")
						.toString(), wj_fjmap, exData);
				for (Map jnwjfjMap : JNWJFJ) {
					String filename = jnwjfjMap.get("FJMC").toString();
					if(!StringHelper.isEmpty(filename)&&!filename.equals("null")){
						if(filename.contains(".")){
							String filetype = filename.substring(filename
									.lastIndexOf("."));
							jnwjfjMap.put("FILETYPE", filetype);
						}else{
							jnwjfjMap.put("FILETYPE", ".jpg");
						}
						
					}
					
				}
				
				
				jnwjfjs.addAll(JNWJFJ);
				m.remove("WJ_FJ");
			}
		}
		
		
		String sqspb=null;
		String zszm=null;
		String sld=null;
		try {
			sqspb=ConfigHelper.getNameByValue("SFGDSQSPB");
			zszm=ConfigHelper.getNameByValue("SFGDZSZM");
			sld=ConfigHelper.getNameByValue("SFGDSLD");
		} catch (Exception e) {
		}
		if(sqspb!=null&&!sqspb.equals("")&&sqspb.equals("1")){
			AddExFile2Dossier(jnwjs, jnwjfjs); //申请审批表
		}
		if(zszm!=null&&!zszm.equals("")&&zszm.equals("1")){
			AddZSZMFile2Dossier(jnwjs, jnwjfjs);//不动产权证明证书存根
		}
		
		if(sld!=null&&!sld.equals("")&&sld.equals("1")){
			AddSLDFile2Dossier(jnwjs, jnwjfjs); //添加受理单
		}
		
		JSONObject ajjbxxJson = JSONObject.fromObject(ajjbxxmap);
		JSONArray jnwjJson = JSONArray.fromObject(jnwjs);
		JSONArray jnwjfjJson = JSONArray.fromObject(jnwjfjs);
		
		String SFGDMATERIAL =ConfigHelper.getNameByValue("SFGDMATERIAL");
		if(SFGDMATERIAL.equals("1")){
			result.put("JNWJ", jnwjJson.toString());
			result.put("JNWJ_FJ", jnwjfjJson.toString());
		}else{
			result.put("JNWJ", "");
			result.put("JNWJ_FJ", "");
		}
		result.put("AJJBXX", ajjbxxJson.toString());
		result.put("DJDL", DJDL);
		result.put("DJXL", DJXL);
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
			jnwj.put("ID", wjid);
			jnwj.put("TM", file.substring(0, file.indexOf(".")));
			jnwj.put("BLZT", null);
			jnwj.put("YJFS", 1);
			jnwj.put("SX", " ");
			jnwj.put("WJLX", "2");
			jnwjs.add(jnwj);

			Map wjfjs = new HashMap();

			wjfjs.put("FJID", Basic.CreatUUID());
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
			jnwj.put("ID", wjid);
			jnwj.put("TM", file.substring(0, file.indexOf(".")));
			jnwj.put("BLZT", null);
			jnwj.put("YJFS", 1);
			jnwj.put("SX", " ");
			jnwj.put("WJLX", "2");
			jnwjs.add(jnwj);
			Map wjfjs = new HashMap();

			wjfjs.put("FJID", Basic.CreatUUID());
			wjfjs.put("FJMC", file);
			wjfjs.put("FILETYPE", ".pdf");
			wjfjs.put("WJID", wjid);
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
			jnwj.put("ID", wjid);
			jnwj.put("TM", file.substring(0, file.indexOf(".")));
			jnwj.put("BLZT", null);
			jnwj.put("YJFS", 1);
			jnwj.put("SX", " ");
			jnwj.put("WJLX", "2");
			jnwjs.add(jnwj);

			/*****受理单暂时无法获取，目前不提取受理单********/
 		    Map wjfjs = new HashMap();
 
 			wjfjs.put("FJID", Basic.CreatUUID());
  			wjfjs.put("FJMC", file);
 			wjfjs.put("FILETYPE", ".pdf");
 			wjfjs.put("WJID", wjid);
 			jnwjfjs.add(wjfjs);
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

	/**
	 * 
	 * @return
	 */
	/*public Message SwapDossier(String file_numner, String actinstid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("actinstid", actinstid);
		return Basic.WorkFlowRequest(swapDossier + file_numner, map,
				HttpMethod.GET);
	}*/
	
	/**
	 * 按照活動名稱和活動狀態獲取所有項目
	 * @param actintanme
	 * @param status
	 * @return
	 */
	public Message GetAllProject(String actintanme,String status){
		Map<String, String> map = new HashMap<String, String>();
		map.put("actname", actintanme);
		map.put("status", status);
		return Basic.WorkFlowRequest(allProjectactName, map, HttpMethod.POST);
	}
	public Message GetNullArchives(){
		Map<String, String> map = new HashMap<String, String>();
		return Basic.WorkFlowRequest(nullArvhices, map, HttpMethod.GET);
	}
	public Message GetNullArchivesDDD(){
		Map<String, String> map = new HashMap<String, String>();
		return Basic.WorkFlowRequest(nullArvhicesDDD, map, HttpMethod.GET);
	}

 
	/**
	 * 创建案卷号
	 * 
	 * @param params
	 * @return
	 */
	public Message CreatAJH(Map<String, String> params) {
		return Basic.DossierRequest(creatAJH, params, HttpMethod.POST);

	} 
	
}
