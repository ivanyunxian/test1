package com.supermap.wisdombusiness.archives.web.common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supemap.mns.client.AsyncCallback;
import com.supemap.mns.client.AsyncResult;
import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.util.ConfigHelper;

@Component("archivesdossierOption")
public class DossierOption {
	@Autowired
	private WorkFlow2Dossier archivesworkFlow2Dossier;
	@Autowired
	private BookMapping archivesbookMapping;
	// 获取收件资料
	private final String getMeterial = "frame_da/hasacceptmater/";
	// 获取项目信息
	private final String getProject = "frame_da/getprojectinfo/";
	// 获取项目信息和收件
	private final String getProjectAndMateril = "frame_da/workflow/dossier/";
	// 创建档案
	private final String creatAJurl = "EditAjjbxx/insertAj";

	// 获取档案资料
	private final String MaterialData = "frame_da/wfipromater/imagedownload/";

	// 上传文件

	private final String Uploadurl = "EditAjjbxx/insertFj";

	// 传递业务信息
	private final String Businessurl = "EditAjjbxx/insertBdcywxx";

	// 通过FILENUMBER查询AJID
	private final String Searchajidurl = "EditAjjbxx/Search_Ajid";

	// 通过FILENUMBER删除AJID
	private final String DelAjurl = "EditAjjbxx/deleteGDAJ/";
	// 抽取档案数据
	private final String swapDossier = "/bdc/dossier/GD/";
	private String DJB = "project_da/";
	private String SQSPB = "project_da/getsqspbstream/";
	// 获取所有等待抽取档案
	private final String alldossierProject = "frame_da/all/dossierproject";
	private static CloudHttp http = new CloudHttp();
	private static List<Tasks> DAs = new ArrayList<Tasks>();
	private static boolean runner2 = false;

	public void GDOptionEx(String fileNumber, String actinst_id, String AJID,
			Map<String, String> DA) {
		// 1、获取项目信息
		Tasks task = new Tasks();
		if (!AJID.equals("-1")) {
			task.setActinstid(actinst_id);
			task.setFilenumber(fileNumber);
			task.setMap(DA);
			task.setAJID(AJID);
			
			DAs.add(task);
			if (!runner2) {
				timerrunner();
				runner2 = true;
			}
		}
	}

	private void timerrunner() {
		final String registService =ConfigHelper.getNameByValue("regist2dossier");
		final String dossierService =ConfigHelper.getNameByValue("dossierservice");
		//程序初始化，检查是否有为转移资料的件
		
		Runnable runnable = new Runnable() {
		
			public void run() {
				
				if (DAs != null && DAs.size() > 0) {
					// task to run goes here
					Tasks task = DAs.get(0);
					Business(task.getAJID(), task.getFilenumber(),registService);
					DAs.remove(0);
				}
				else{
					System.out.println("例行检测待归档的案卷，但是没有发现，我离开了");
				}
			}
		};
		ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 10, 30, TimeUnit.SECONDS);
	}

	/**
	 * 业务信息
	 * 
	 * @param AJID
	 * @param business
	 */
	private void Business(String AJID, String file_number,String registService) {
		Message business = archivesbookMapping.GetDJBXX(file_number,registService);
		if (business != null) {
			AsyncCallback materilCallback = new AsyncCallback() {

				public void onFail(Exception ex) {
					System.out.println("获取资料失败");
					ex.printStackTrace();
				}

				public void onSuccess(Object paramT) {
					@SuppressWarnings("unchecked")
					Message result = (Message) paramT;
					if (result != null) {
						System.out.println("发送业务成功");
					}
				}
			};
			Map<String, String> map = new HashMap<String, String>();
			map.put("AJID", AJID);
			map.put("Business", business.getMessageBodyAsString());
			String dossierService =ConfigHelper.getNameByValue("dossierservice");
			AsyncResult asyncBatchPopMessage = http.asyncPostSend(
					dossierService, map, Businessurl, materilCallback);
			asyncBatchPopMessage.getResult();
		}
	}
	
}
