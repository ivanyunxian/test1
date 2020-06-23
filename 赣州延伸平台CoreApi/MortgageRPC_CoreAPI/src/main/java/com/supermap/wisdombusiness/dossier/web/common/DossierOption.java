package com.supermap.wisdombusiness.dossier.web.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

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
import com.supermap.realestate.registration.util.GetProperties;

@Component("dossierOption")
public class DossierOption {
	@Autowired
	private WorkFlow2Dossier workFlow2Dossier;
	@Autowired
	private BookMapping bookMapping;
	// 获取收件资料
	private final String getMeterial = "frame/hasacceptmater/";
	// 获取项目信息
	private final String getProject = "frame/getprojectinfo/";
	// 获取项目信息和收件
	private final String getProjectAndMateril = "frame/workflow/dossier/";
	// 创建档案
	private final String creatAJurl = "EditAjjbxx/insertAj";

	// 获取档案资料
	private final String MaterialData = "frame/wfipromater/imagedownload/";

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
	private String DJB = "project/";
	private String SQSPB = "project/getsqspbstream/";
	// 获取所有等待抽取档案
	private final String alldossierProject = "frame/all/dossierproject";
	private static CloudHttp http = new CloudHttp();
	private static List<Tasks> DAs = new ArrayList<Tasks>();
	private static boolean runner2 = false;

	/**
	 * 归档操作
	 * 
	 * @param fileNumber
	 * @param actinst_id
	 * @param ajjbxxmap
	 */
	public void GDOption(String fileNumber, String actinst_id, String AJID,
			Message business, Map<String, String> DA) {
		// 1、获取项目信息

		Tasks task = new Tasks();
		task.setActinstid(actinst_id);
		task.setFilenumber(fileNumber);
		task.setMap(DA);
		DAs.add(task);
		if (!runner2) {
			timerrunner();
			runner2 = true;
		}
	}

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
					FJContent(task.getFilenumber(), task.getMap(),
							task.getActinstid(),dossierService);
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
		Message business = bookMapping.GetDJBXX(file_number,registService);
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
	/**
	 * 卷内文件
	 * 
	 * @param file_number
	 * @param DA
	 * @param actinst_id
	 */
	private void FJContent(String file_number, Map DA, String actinst_id,String dossierService) {
		// 发送资料详细数据--同步测试，可以考虑开始使用异步
		JSONArray JNWJ_FJ = JSONArray.fromObject(DA.get("JNWJ_FJ"));
		if (JNWJ_FJ != null && JNWJ_FJ.size() > 0) {
			for (int i = 0; i < JNWJ_FJ.size(); i++) {
				JSONObject json = JNWJ_FJ.getJSONObject(i);
				Object Materid = json.get("FJ_ID");
				if (Materid != null) {
					final String materil = Materid.toString();
					final String _file_number=file_number;
					final String _dossierService=dossierService;
					String imagebase64 = "";
					if (json.get("desc") == null) {
						AsyncCallback materilCallback = new AsyncCallback() {

							public void onFail(Exception ex) {
								System.out.println("获取资料失败");
								ex.printStackTrace();
							}

							public void onSuccess(Object paramT) {
								@SuppressWarnings("unchecked")
								Message result = (Message) paramT;
								if (result != null) {
									String imagebase64 = "";
									byte[] image = result
											.getMessageBodyAsBytes();
									if (image != null) {
										imagebase64 = Base64
												.encodeBase64String(image);
									} else {
										imagebase64 = "";
									}
									workFlow2Dossier.UploadFJ(materil,
											imagebase64,_dossierService);
									workFlow2Dossier.MaintainFJ(_file_number);

								}
							}
						};
						Map<String, String> map = new HashMap<String, String>();
						map.put("fileName", "");
						String worflowBasic =ConfigHelper.getNameByValue("worflow2dossier");
						AsyncResult asyncBatchPopMessage = http.asyncGetSend(
								worflowBasic, map, MaterialData + materil,
								materilCallback);
						asyncBatchPopMessage.getResult();
					}

				}
			}
		}

	}
}
