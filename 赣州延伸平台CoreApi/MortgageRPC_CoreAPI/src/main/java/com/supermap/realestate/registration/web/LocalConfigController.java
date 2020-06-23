package com.supermap.realestate.registration.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.T_CONFIG;
import com.supermap.realestate.registration.model.T_CONFIGOPTION;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 登簿检查Controller，流程相关的登簿检查放在这里边
 * @ClassName: ConfigController
 * @author 俞学斌
 * @date 2015年11月07日 16:52:28
 */
@Controller
@RequestMapping("/localconfig")
public class LocalConfigController {

	@Autowired
	private CommonDao basecommondao;

	private final String prefix = "/realestate/registration/";

	/***************************************** 登记系统配置 *****************************************/

	/**
	 * 本地化配置页面(URL:"/configmanager/index/",Method：POST)
	 * @Title: getConfigManagerIndex
	 * @author:俞学斌
	 * @date：2015年11月07日 16:52:28
	 * @return
	 */
	@RequestMapping(value = "/index/")
	public String getConfigManagerIndex() {
		return prefix + "config/configmanager";
	}

	@RequestMapping(value = "/frame/")
	public @ResponseBody List<LocalConfigController.ConfigJson> getConfigManagerFrame() {
		List<T_CONFIG> list = basecommondao.getDataList(T_CONFIG.class, "YXBZ>0 ORDER BY FIRSTINDEX,SECONDINDEX,CLASSNAME");
		List<LocalConfigController.ConfigJson> listjson = new ArrayList<LocalConfigController.ConfigJson>();
		Map<String, LocalConfigController.ConfigJson> maps = new HashMap<String, LocalConfigController.ConfigJson>();

		for (T_CONFIG config : list) {
			LocalConfigController.ConfigJson json = null;
			if (!maps.containsKey(config.getCLASSNAME())) {
				json = new ConfigJson();
				listjson.add(json);
				List<ConfigItem> items = new ArrayList<LocalConfigController.ConfigItem>();
				json.setConfigitems(items);
				maps.put(config.getCLASSNAME(), json);
			} else {
				json = maps.get(config.getCLASSNAME());
			}
			json.setClassname(config.getCLASSNAME());
			List<ConfigItem> items = json.getConfigitems();
			ConfigItem item = new ConfigItem();
			items.add(item);
			item.configname = config.getCONFIGNAME();
			item.configurl = config.getURL();
			item.paramname = config.getNAME();
			item.description = config.getDESCRIPTION();
			item.value = config.getVALUE() == null ? "" : config.getVALUE();
			item.valuetype = config.getVALUETYPE();
			if (config.getCONFIGED() != null && config.getCONFIGED() > 0) {
				item.configed = true;
			} else {
				item.configed = false;
			}
			if ("2".equals(item.valuetype)) {
				// 加载选项
				List<option> options = new ArrayList<LocalConfigController.option>();
				item.setOptions(options);
				String optionclass = config.getOPTIONCLASS();
				if (!StringHelper.isEmpty(optionclass)) {
					List<T_CONFIGOPTION> opts = basecommondao.getDataList(T_CONFIGOPTION.class, "OPTIONCLASS='" + optionclass + "' AND YXBZ>=0");
					if (opts != null && opts.size() > 0) {
						for (T_CONFIGOPTION opt : opts) {
							option op = new option(opt.getOPTIONVALUE(), opt.getOPTIONTEXT());
							options.add(op);
						}
					}
				}
			}
		}
		return listjson;
	}

	/**
	 * 获取登记系统配置(URL:"/configmanager/",Method：GET)
	 * @Title: LoadConfigManager
	 * @author:俞学斌
	 * @date：2015年12月22日 17:38:50
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/configmanager/", method = RequestMethod.GET)
	public @ResponseBody Map LoadConfigManager(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String, String>();
		List<T_CONFIG> configs = basecommondao.getDataList(T_CONFIG.class, "1>0");
		if (configs != null && configs.size() > 0) {
			for (T_CONFIG config : configs) {
				if (!StringHelper.isEmpty(config.getNAME())) {
					if (!map.containsKey(config.getNAME())) {
						map.put(config.getNAME(), config.getVALUE());
					}
					if (!map.containsKey(config.getNAME() + "_Url")) {
						map.put(config.getNAME() + "_Url", StringHelper.formatObject(config.getURL()));
					}
				}
			}
		}
		return map;
	}

	/**
	 * 更新登记系统配置 (URL:"/configmanager/",Method：POST)
	 * @Title: UpdateConfigManager
	 * @author:俞学斌
	 * @date：2015年12月22日 17:38:50
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/configmanager/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateConfigManager(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("保存失败！");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			String name = StringHelper.formatObject(obj);
			Object val = map.get(obj);
			String value = StringHelper.formatObject(val);
			if (!StringHelper.isEmpty(name)) {
				List<T_CONFIG> configs = basecommondao.getDataList(T_CONFIG.class, " NAME='" + name + "'");
				if (configs != null && configs.size() > 0) {
					T_CONFIG config = configs.get(0);
					config.setCONFIGED(1);
					config.setVALUE(value);
					basecommondao.update(config);
				}
			}
		}
		basecommondao.flush();
		ConfigHelper.reload();
		msg.setSuccess("true");
		msg.setMsg("保存成功！");
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	/***************************************** 登记系统配置 *****************************************/

	public static class ConfigJson {
		private String classname;
		private List<ConfigItem> configitems;

		public String getClassname() {
			return classname;
		}

		public void setClassname(String classname) {
			this.classname = classname;
		}

		public List<ConfigItem> getConfigitems() {
			return configitems;
		}

		public void setConfigitems(List<ConfigItem> configitems) {
			this.configitems = configitems;
		}
	}

	public static class ConfigItem {
		private String configname;
		private String paramname;
		private String configurl;
		private String description;
		private String valuetype;
		private String value;
		private List<option> options;
		private boolean configed;

		/**
		 * @return configed
		 */
		public boolean isConfiged() {
			return configed;
		}

		/**
		 * @param configed
		 *            要设置的 configed
		 */
		public void setConfiged(boolean configed) {
			this.configed = configed;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getConfigname() {
			return configname;
		}

		public void setConfigname(String configname) {
			this.configname = configname;
		}

		public String getParamname() {
			return paramname;
		}

		public void setParamname(String paramname) {
			this.paramname = paramname;
		}

		public String getConfigurl() {
			return configurl;
		}

		public void setConfigurl(String configurl) {
			this.configurl = configurl;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getValuetype() {
			return valuetype;
		}

		public void setValuetype(String valuetype) {
			this.valuetype = valuetype;
		}

		public List<option> getOptions() {
			return options;
		}

		public void setOptions(List<option> options) {
			this.options = options;
		}
	}

	public static class option {

		public option(String _value, String _text) {
			this.value = _value;
			this.text = _text;
		}

		private String value;
		private String text;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

}
