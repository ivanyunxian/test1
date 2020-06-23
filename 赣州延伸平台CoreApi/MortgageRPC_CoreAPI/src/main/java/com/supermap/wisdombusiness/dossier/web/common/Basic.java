package com.supermap.wisdombusiness.dossier.web.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.supemap.mns.client.AsyncCallback;
import com.supemap.mns.client.AsyncResult;
import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.common.HttpMethod;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;

@Component("basic")
public class Basic {
	private static CloudHttp http = new CloudHttp();

	private static Message SendRequest(String basicurl, String url,
			Map<String, String> params, HttpMethod method) {

		Message msg = null;
		if (method.equals(HttpMethod.POST)) {
			msg = http.PostSend(basicurl, params, url);
		} else if (method.equals(HttpMethod.GET)) {
			msg = http.GETSend(basicurl, params, url);
		}
		return msg;
	}

	/**
	 * 创建UUID
	 * 
	 * @return
	 */
	public static String CreatUUID() {

		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String temp = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp;
	}

	/**
	 * 工作流请求数据
	 * 
	 * @param url
	 * @param params
	 * @param method
	 * @return
	 */
	public static Message WorkFlowRequest(String url,
			Map<String, String> params, HttpMethod method) {
		String worflowBasic =ConfigHelper.getNameByValue("worflow2dossier");
		return SendRequest(worflowBasic, url, params, method);

	}

	/**
	 * 登记系统请求
	 * 
	 * @param url
	 * @param params
	 * @param method
	 * @return
	 */
	public static Message RegistRequest(String url, Map<String, String> params,
			HttpMethod method) {
		String registService =ConfigHelper.getNameByValue("regist2dossier");
		return SendRequest(registService, url, params, method);

	}
	public static Message RegistRequest(String registService,String url, Map<String, String> params,
			HttpMethod method) {
		return SendRequest(registService, url, params, method);

	}

	public static Message DossierRequest(String url,
			Map<String, String> params, HttpMethod method) {
		String dossierService =ConfigHelper.getNameByValue("dossierservice");
		return SendRequest(dossierService, url, params, method);
	}
	public static Message DossierRequest(String dossierService,String url,
			Map<String, String> params, HttpMethod method) {
		return SendRequest(dossierService, url, params, method);
	}
	public static  void aysnWorkflowPost(Map<String, String> map,String url) {
		AsyncCallback materilCallback = new AsyncCallback() {

			public void onFail(Exception ex) {
				System.out.println("维护DAH失败");
				ex.printStackTrace();
			}

			public void onSuccess(Object paramT) {
				@SuppressWarnings("unchecked")
				Message result = (Message) paramT;
				if (result != null) {
					
				}
			}
		};
		String worflowBasic =ConfigHelper.getNameByValue("worflow2dossier");
		AsyncResult asyncBatchPopMessage = http.asyncPostSend(worflowBasic,
				map, url, materilCallback);
		asyncBatchPopMessage.getResult();
		
	}

	/**
	 * 获取JSON 映射数据
	 * 
	 * @param str
	 *            json 数据
	 * @param mapping
	 *            映射关系
	 * @return
	 */
	public static Map GetMappingData(String str, Map<String, String> mapping) {
		if (!str.equals("")) {
			Map<String, String> info = new HashMap<String, String>();
			JSONObject jsonobj = JSONObject.fromObject(str);
			return GetJSONMapping(jsonobj, mapping);
		} else {
			return null;
		}
	}

	private static Map GetJSONMapping(JSONObject jsonobj,
			Map<String, String> mapping) {
		Map<String, String> info = new HashMap<String, String>();
		Iterator<Map.Entry<String, String>> entries = mapping.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			Object src = jsonobj.get(entry.getKey());
			String target = entry.getValue();
			if (src != null) {
				info.put(target, src.toString());
			}
		}
		return info;

	}

	/**
	 * 渲染数组对象json
	 * 
	 * @param json
	 * @param mapping
	 * @return
	 */
	public static List<Map> GetArraryMappingData(String json,
			Map<String, String> mapping) {
		List<Map> info = new ArrayList<Map>();
		JSONArray jsonstr = JSONArray.fromObject(json);
		if (jsonstr.size() > 0) {
			for (int i = 0; i < jsonstr.size(); i++) {
				JSONObject job = jsonstr.getJSONObject(i);
				info.add(GetJSONMapping(job, mapping));
			}
		}
		return info;
	}

	public static List<Map> GetArraryMappingData(String json,
			Map<String, String> mapping, Map exData) {
		List<Map> info = new ArrayList<Map>();
		JSONArray jsonstr = JSONArray.fromObject(json);
		if (jsonstr.size() > 0) {
			for (int i = 0; i < jsonstr.size(); i++) {
				JSONObject job = jsonstr.getJSONObject(i);
				Map single = GetJSONMapping(job, mapping);
				single.putAll(exData);
				info.add(single);
			}
		}
		return info;
	}

}
