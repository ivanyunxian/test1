package com.supermap.realestate.registration.util;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.LOG_DXTS;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.synchroinline.util.HttpRequestHelper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @Description:短信定时推送
 * @author taochunda
 * @date 2019年04月23日 16:18:00
 */
@Component
public class SmsPush extends HttpServlet {

	private static CommonDao baseCommonDao;
	private static String appid;
	private static String appkey;
	private static String smsSign;
	private static String smsSign_rx;
	private static String smsSign_lc;
	private static String smsSign_xy;
	private static String smsSign_bl;
	private static String param;
	private static String param_rx;
	private static String param_lc;
	private static String param_xy;
	private static String param_bl;
	private static String sendMessage;
	private static String dyzx_template;
	private static String cqzx_template;
	private static String am_template;
	private static String pm_template;

	static {
		if (baseCommonDao == null) {
			baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
		appid = GetProperties.getValueByFileName("sms_config.properties", "appid");
		appkey = GetProperties.getValueByFileName("sms_config.properties", "appkey");
		smsSign = GetProperties.getValueByFileName("sms_config.properties", "smsSign");
		smsSign_rx = GetProperties.getValueByFileName("sms_config.properties", "smsSign_rx");
		smsSign_lc = GetProperties.getValueByFileName("sms_config.properties", "smsSign_lc");
		smsSign_xy = GetProperties.getValueByFileName("sms_config.properties", "smsSign_xy");
		smsSign_bl = GetProperties.getValueByFileName("sms_config.properties", "smsSign_bl");
		sendMessage = GetProperties.getValueByFileName("sms_config.properties", "sendMessage");
		dyzx_template = GetProperties.getValueByFileName("sms_config.properties", "dyzx_template");
		cqzx_template = GetProperties.getValueByFileName("sms_config.properties", "cqzx_template");
		am_template = GetProperties.getValueByFileName("sms_config.properties", "am_template");
		pm_template = GetProperties.getValueByFileName("sms_config.properties", "pm_template");
		//模板参数{3}、{4}
		param = GetProperties.getValueByFileName("sms_config.properties", "param");
		param_rx = GetProperties.getValueByFileName("sms_config.properties", "param_rx");
		param_lc = GetProperties.getValueByFileName("sms_config.properties", "param_lc");
		param_xy = GetProperties.getValueByFileName("sms_config.properties", "param_xy");
		param_bl = GetProperties.getValueByFileName("sms_config.properties", "param_bl");
	}

	public Logger logger = Logger.getLogger(SmsPush.class);

	@Override
	public void init(){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				SmsPush();
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 60, 600, TimeUnit.SECONDS);
	}
	/*
	 * 获取待推送短信列表
	 */
    public void SmsPush() {

		//本地化配置：是否发送短信
		String SENDMESSAG =ConfigHelper.getNameByValue("SENDMESSAGE");
		if (!"1".equals(SENDMESSAG)) {
			return;
		}

		///初始化相应待推送列表
		SmsPushTool.InitDxtsInfoList();
		if (SmsPushTool.list_dxtsinfo == null || SmsPushTool.list_dxtsinfo.size() <= 0) {
			return;
		}

		Date dt=new Date();
		System.out.println(StringHelper.FormatDateOnType(dt, "yyyy-MM-dd HH:mm:ss 待推送短信条数:")+SmsPushTool.list_dxtsinfo.size());
		LOG_DXTS info = null;
		while (SmsPushTool.list_dxtsinfo.size() > 0) {
			try{
				info = SmsPushTool.list_dxtsinfo.get(0);

				Long now = System.currentTimeMillis();//当前时间戳
				//本地化配置：短信推送延迟时间
				String lodtime =ConfigHelper.getNameByValue("LODTIME");
				if (StringHelper.isEmpty(lodtime)) {
					lodtime = "1.5";
				}
				double sendtime = 60 * 60 * Double.parseDouble(lodtime);//延迟时间
				Long djsj = info.getDJSJ().getTime();
				if((now - djsj) < sendtime) {
					logger.info("未到达设定的延迟发送时间，不进行发送！");
					SmsPushTool.list_dxtsinfo.remove(info);
					continue;
				}

				info.setSEND_STATUS("1");
				info.setSEND_NUMBER(info.getSEND_NUMBER()+1);
				baseCommonDao.update(info);
				StartPush(info);
				SmsPushTool.list_dxtsinfo.remove(info);
			} catch (Exception e) {
				SmsPushTool.list_dxtsinfo.remove(info);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行推送
	 * @param info
	 * @return
	 */
	public boolean StartPush(LOG_DXTS info) {
		boolean success=false;
		//短信相关参数
		String tencentcode = "";

		//玉林根据区划获取签名
		String sign = smsSign;
		String _param = param;
		if ("450921".equals(info.getTENANT_ID())) {
			sign = smsSign_rx;
			_param = param_rx;
		} else if ("450922".equals(info.getTENANT_ID())) {
			sign = smsSign_lc;
			_param = param_lc;
		} else if ("450924".equals(info.getTENANT_ID())) {
			sign = smsSign_xy;
			_param = param_xy;
		} else if ("450981".equals(info.getTENANT_ID())) {
			sign = smsSign_bl;
			_param = param_bl;
		}

		String[] qllxs = {"4","6","8"};//产权权利类型

		StringBuilder sb = new StringBuilder();
		sb.append(info.getRECEIVE_NAME());
		sb.append("&" + info.getRECEIVE_INFO());

		//
		if ("400".equals(info.getDJLX())) {
			if (Arrays.asList(qllxs).contains(info.getQLLX())) {
				tencentcode = cqzx_template;
			} else if ("23".equals(info.getQLLX())) {
				tencentcode = dyzx_template;
			}
		} else {
			//其他登簿的模板区分上午下午，0 上午，1 下午
			GregorianCalendar ca = new GregorianCalendar();
			if ("0".equals(StringHelper.formatObject(ca.get(GregorianCalendar.AM_PM)))) {
				tencentcode = am_template;
			} else {
				tencentcode = pm_template;
			}
			//上下午模板还要加上参数{3}、{4}
			sb.append("&" + _param);
		}

		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("appid", String.valueOf(appid));
		postParams.put("appkey", appkey);
		postParams.put("smsSign", sign);
		postParams.put("mobile", info.getRECEIVE_PHONE());
		postParams.put("name", info.getRECEIVE_NAME());
		postParams.put("message", info.getRECEIVE_INFO());
		postParams.put("info", sb.toString());
		postParams.put("tencentcode", tencentcode);
		String jsonString;
		try {
			jsonString = HttpRequestHelper.doPost(sendMessage, postParams);
			if (!StringHelper.isEmpty(jsonString)) {
				JSONObject json = JSONObject.parseObject(jsonString);
				System.out.println("短信接口返回数据：" + json);
				String result = StringHelper.formatObject(json.get("result"));
				if (!StringHelper.isEmpty(result) && "0".equals(result)) {
					info.setSEND_TYPE("0");
					info.setSEND_DATE(new Date());
					info.setSEND_STATUS("2");
					baseCommonDao.update(info);
					success=true;
				} else {
					throw new Exception(StringHelper.formatObject(json));
				}
			} else {
				throw new Exception(jsonString);
			}
		} catch (Exception e) {
			info.setSEND_TYPE("0");
			info.setSEND_DATE(new Date());
			info.setSEND_STATUS("3");
			baseCommonDao.update(info);
			e.printStackTrace();
		}
		return success;
	}

}