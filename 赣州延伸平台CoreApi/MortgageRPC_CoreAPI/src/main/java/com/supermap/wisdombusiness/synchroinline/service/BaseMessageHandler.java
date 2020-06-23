package com.supermap.wisdombusiness.synchroinline.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

/**
 * 消息处理器基类，封装一些通用的操作
 * 
 * @author pukx by 2017年7月4日
 */
public abstract class BaseMessageHandler
{
	public static final String INLINE_SMS_HANDLER_NAME = "inline_sms_handler";

	protected CommonDao dao = null;
	protected SmStaff smStaff = null;

	/**
	 * 配置参数
	 */
	protected HashMap<String, String> option = new HashMap<String, String>();

	/**
	 * 
	 * @param smStaff
	 *            当前登录用户对象
	 * @param commonDao
	 *            数据库访问对象
	 */
	public BaseMessageHandler(SmStaff smStaff, CommonDao commonDao)
	{
		this.smStaff = smStaff;
		this.dao = commonDao;
		this.initOption();
	}

	/**
	 * 初始化配置参数
	 */
	private void initOption()
	{
		String inline_sms_url = ConfigHelper.getNameByValue("inline_sms_url");
		option.put("inline_sms_url", inline_sms_url);
		String inline_sms_option = ConfigHelper.getNameByValue("inline_sms_option");
		inline_sms_option = inline_sms_option == null ? "" : inline_sms_option.trim().toLowerCase();
		if (!inline_sms_option.isEmpty())
		{
			String[] params = StringUtils.split(inline_sms_option, "&");
			for (String p : params)
			{
				String[] keyval = p.split("=");
				if (keyval.length > 0)
				{
					String key = keyval[0].trim();
					String value = keyval.length >= 2 ? keyval[1].trim() : "";
					option.put("inline_sms_option_" + key, value);
				}
			}
		}
	}

	/**
	 * 通过项目编号获取权利人信息，权利人为非个人的，取代理人信息
	 * 
	 * @param xmbh
	 * @return
	 */
	protected List<Qlr> getReceiverByXmbh(String xmbh)
	{
		List<Qlr> qlrs = new ArrayList<BaseMessageHandler.Qlr>();
		BDCS_XMXX xmxx = this.dao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null)
		{
			List<BDCS_QLR_GZ> qlr_gzs = this.dao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmbh + "'");
			for (BDCS_QLR_GZ qlr_gz : qlr_gzs)
			{
				if ("1".equals(qlr_gz.getQLRLX()))
				{
					// 发送对象为个人
					Qlr qlr = new Qlr(xmxx.getYWLSH(), qlr_gz.getQLRMC(), qlr_gz.getDH());
					qlrs.add(qlr);
				}
				else
				{
					// 发送对象为代理人
					Qlr qlr = new Qlr(xmxx.getYWLSH(), qlr_gz.getDLRXM(), qlr_gz.getDLRLXDH());
					qlrs.add(qlr);
				}
			}
		}
		return qlrs;
	}

	/**
	 * 根据配置创建消息处理器实例
	 * 
	 * @param smStaff
	 * @param commonDao
	 * @return
	 * @throws Exception
	 */
	public static MessageHandler create(SmStaff smStaff, CommonDao commonDao) throws Exception
	{
		if(smStaff==null || smStaff.getCurrentWorkStaff()==null)
			throw new Exception("登录过期，请重新登录。");
		String handler_name = ConfigHelper.getNameByValue(INLINE_SMS_HANDLER_NAME);
		if (handler_name == null || handler_name.isEmpty())
			throw new Exception("未配置消息处理器，请检查。");
		String class_name = "com.supermap.wisdombusiness.synchroinline.service." + handler_name;
		Class<?> cls = Class.forName(class_name);
		Constructor<?> con = cls.getConstructor(SmStaff.class, CommonDao.class);
		return (MessageHandler) con.newInstance(smStaff, commonDao);
	}

	/**
	 * 消息接收对象
	 * 
	 * @author Administrator
	 *
	 */
	public static class Qlr
	{
		private String ywh;
		private String qlrxm;
		private String qlrdh;

		/**
		 * @return the ywh
		 */
		public String getYwh()
		{
			return ywh;
		}

		/**
		 * @param ywh
		 *            the ywh to set
		 */
		public void setYwh(String ywh)
		{
			this.ywh = ywh;
		}

		/**
		 * @return the qlrxm
		 */
		public String getQlrxm()
		{
			return qlrxm;
		}

		/**
		 * @param qlrxm
		 *            the qlrxm to set
		 */
		public void setQlrxm(String qlrxm)
		{
			this.qlrxm = qlrxm;
		}

		/**
		 * @return the qlrdh
		 */
		public String getQlrdh()
		{
			return qlrdh;
		}

		/**
		 * @param qlrdh
		 *            the qlrdh to set
		 */
		public void setQlrdh(String qlrdh)
		{
			this.qlrdh = qlrdh;
		}

		public Qlr(String ywh, String qlrxm, String qlrdh)
		{
			this.ywh = ywh == null ? "" : ywh;
			this.qlrxm = qlrxm == null ? "" : qlrxm;
			this.qlrdh = qlrdh == null ? "" : qlrdh;
		}

	}
}
