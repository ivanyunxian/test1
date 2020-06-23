package com.supermap.wisdombusiness.synchroinline.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.synchroinline.util.HttpRequestHelper;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.thoughtworks.xstream.XStream;

/**
 * 海口消息处理器 海口调用web service进行消息发送，消息配置格式为： inline_sms_handler：HkMessageHandler
 * inline_sms_url：http://192.40.40.6:8111/CommonWebservice.asmx?op=SendSMSJson
 * inline_sms_option：token=&sender=海口市不动产登记中心&fshj=缮证|发证&templet={xm}
 * 你好，您申请办理业务号为{ywh}的业务已完成缮证，请你于3个工作日内到海口市不动产登记中心领取不动产权属证书。
 * 
 * @author pukx by 2017年7月4日
 */
public class HkMessageHandler extends BaseMessageHandler implements MessageHandler
{

	private String inline_sms_url = null;
	// 令牌
	private String token = "";
	// 发送人
	private String sender = "";
	// 短信模板
	private String templet = "";

	public HkMessageHandler(SmStaff smStaff, CommonDao commonDao)
	{
		super(smStaff, commonDao);
		this.token = this.option.get("inline_sms_option_token");
		this.sender = this.option.get("inline_sms_option_sender");
		this.inline_sms_url = this.option.get("inline_sms_url");
		// this.templet = this.option.get("inline_sms_option_templet");
		// this.templet = this.templet == null ? "" : this.templet;
		String uid = this.smStaff.getCurrentWorkStaff().getId();
		if (uid != null)
		{
			List<Map> maps = this.dao.getDataListByFullSql("select TEMPLET from bdc_workflow.INLINE_DX_TEMPLET t where t.userid='" + uid + "'");
			if (!maps.isEmpty())
			{
				this.templet = String.valueOf(maps.get(0).get("TEMPLET"));
			}
		}
	}

	@Override
	public void send(String xmbh) throws Exception
	{
		List<Qlr> qlrs = this.getReceiverByXmbh(xmbh);
		SMSContentBodys bodys = new SMSContentBodys();
		for (Qlr qlr : qlrs)
		{
			SMSContentBody body = new SMSContentBody(qlr.getQlrxm(), qlr.getQlrdh(), this.fillTemplet(qlr));
			if (body.getTelPhone() == null || body.getTelPhone().isEmpty())
			{
				System.out.println("手机号为null，忽略");
				continue;
			}
			bodys.add(body);
		}
		if (!bodys.isEmpty())
		{
			this.doPost(bodys);
		}
		// 记录发送成功
		String query_sql = "select count(*) from bdc_workflow.inline_project_dxfs where xmbh='" + xmbh + "'";
		long count = this.dao.getCountByCFullSql(query_sql);
		if (count > 0)
		{
			// 更新
			String update_sql = "update bdc_workflow.inline_project_dxfs set USERID='" + this.smStaff.getCurrentWorkStaff().getId() + "',USERNAME='" + this.smStaff.getCurrentWorkStaff().getUserName() + "',FSRQ=sysdate where xmbh='" + xmbh + "'";
			this.dao.getCurrentSession().createSQLQuery(update_sql).executeUpdate();
		}
		else
		{
			String id = SuperHelper.GeneratePrimaryKey();
			String log_sql = "insert into bdc_workflow.inline_project_dxfs values ('" + id + "','" + xmbh + "','" + this.smStaff.getCurrentWorkStaff().getId() + "','" + this.smStaff.getCurrentWorkStaff().getUserName() + "',sysdate)";
			this.dao.getCurrentSession().createSQLQuery(log_sql).executeUpdate();
		}
	}

	@Override
	public int send(String[] xmbhs) throws Exception
	{
		int success = 0;
		for (String xmbh : xmbhs)
		{
			try
			{
				this.send(xmbh);
				success++;
			}
			catch (Exception ex)
			{
				System.out.print("短信发送失败，详情：");
				ex.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public void test(String tel, String msg) throws Exception
	{
		// TODO Auto-generated method stub

	}

	private void doPost(SMSContentBodys bodys) throws Exception
	{
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("TokenID", this.token);
		postParams.put("UserName", this.smStaff.getCurrentWorkStaff().getUserName());
		postParams.put("SMSContentBodys", bodys.toJson());
		String json = HttpRequestHelper.doPost(this.inline_sms_url, postParams);
		SMSResponse response = SMSResponse.fromJosn(json);
		if (response == null)
			throw new Exception("短信服务未响应消息。");
		if (!response.getSuccess())
			throw new Exception("短信发送失败，" + response.Msg);
	}

	/**
	 * 填充短信模板占位符，返回完整的短信内容
	 * 
	 * @param qlr
	 * @return
	 */
	private String fillTemplet(Qlr qlr) throws Exception
	{
		if (this.templet == null || this.templet.isEmpty())
			throw new Exception("请先配置短信模板。");
		return this.templet.replace("{xm}", qlr.getQlrxm() == null ? "" : qlr.getQlrxm()).replace("{ywh}", qlr.getYwh() == null ? "" : qlr.getYwh());
	}

	public static class SMSContentBodys extends ArrayList<SMSContentBody>
	{
		public String toJson()
		{
			return JSONArray.toJSONString(this);
		}
	}

	public static class SMSContentBody
	{
		@JSONField(name = "Content")
		private String content;
		@JSONField(name = "TelPhone")
		private String telPhone;
		@JSONField(name = "Receiver")
		private String receiver;

		/**
		 * @return the content
		 */
		public String getContent()
		{
			return content;
		}

		/**
		 * @param content
		 *            the content to set
		 */
		public void setContent(String content)
		{
			this.content = content;
		}

		/**
		 * @return the telPhone
		 */
		public String getTelPhone()
		{
			return telPhone;
		}

		/**
		 * @param telPhone
		 *            the telPhone to set
		 */
		public void setTelPhone(String telPhone)
		{
			this.telPhone = telPhone;
		}

		/**
		 * @return the receiver
		 */
		public String getReceiver()
		{
			return receiver;
		}

		/**
		 * @param receiver
		 *            the receiver to set
		 */
		public void setReceiver(String receiver)
		{
			this.receiver = receiver;
		}

		/**
		 * 消息对象
		 * 
		 * @param user
		 *            接收人
		 * @param tel
		 *            接收号码
		 * @param msg
		 *            消息内容
		 */
		public SMSContentBody(String user, String tel, String msg)
		{
			this.receiver = user == null ? "" : user;
			this.telPhone = tel == null ? "" : tel;
			this.content = msg == null ? "" : msg;
		}

	}

	public static class SMSResponse
	{
		private Boolean Success;
		private String Msg;

		/**
		 * @return the success
		 */
		public Boolean getSuccess()
		{
			return Success;
		}

		/**
		 * @param success
		 *            the success to set
		 */
		public void setSuccess(Boolean success)
		{
			Success = success;
		}

		/**
		 * @return the msg
		 */
		public String getMsg()
		{
			return Msg;
		}

		/**
		 * @param msg
		 *            the msg to set
		 */
		public void setMsg(String msg)
		{
			Msg = msg;
		}

		public static SMSResponse fromJosn(String json)
		{
			return JSONObject.parseObject(json, SMSResponse.class);
		}
	}
}
