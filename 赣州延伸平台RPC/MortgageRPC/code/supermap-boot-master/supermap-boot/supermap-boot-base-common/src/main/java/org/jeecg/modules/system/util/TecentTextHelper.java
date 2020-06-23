package org.jeecg.modules.system.util;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;

public class TecentTextHelper {

	private final static String SecretId = "AKIDA6rNeYodpk7NPBGk36uFLCoH5hAN7vwi";
	private final static String SecretKey = "pZ8SVYNtAJk13poGVVFsYzBAuSlpIbgX";
	private final static String SmsSdkAppid = "1400340195";

	public static JSONObject sendMsg(String phonenum, String templateId, String sign, String param) {
		JSONObject result = new JSONObject();
		try {

			Credential cred = new Credential(SecretId, SecretKey);

			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("sms.tencentcloudapi.com");

			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);

			SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

			String params = "{\"PhoneNumberSet\":[\"+86" + phonenum + "\"],\"TemplateID\":\"" + templateId
					+ "\",\"Sign\":\"" + sign + "\",\"TemplateParamSet\":[\"" + param + "\"],\"SmsSdkAppid\":\""
					+ SmsSdkAppid + "\"}";
			SendSmsRequest req = SendSmsRequest.fromJsonString(params, SendSmsRequest.class);
			SendSmsResponse resp = client.SendSms(req);

			System.out.println(SendSmsRequest.toJsonString(resp));
			result = JSONObject.parseObject(SendSmsRequest.toJsonString(resp));
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
		}
		return result;
	}

}
