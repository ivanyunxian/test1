package com.supermap.realestate_gx.registration.factorys;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.supermap.realestate_gx.registration.mapping.GX_PageControlsConfig;
import com.supermap.realestate_gx.registration.mapping.GX_PageControlsConfig.GX_PageControlsItem;
import com.supermap.realestate_gx.registration.mapping.GX_PageControlsConfig.GX_ids;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperXStream;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * 
 * 工厂类，主要功能：创建DJHandler、加载流程配置文件、获取选择器、获取流程配置等
 * @author 刘树峰
 * @date 2015年7月9日 下午5:31:14
 * @Copyright SuperMap
 */
public class GX_HandlerFactory {

	private static GX_PageControlsConfig _pagecontrolsconfigxml = null;


	/**
	 * 静态构造函数，读取配置文件
	 */
	static {
		//mapping = getHandlerMappingFromConfig();
		// _checkconfigxml = getCheckFromConfig();
	}



	/**
	 * 对外接口：获取XML里边配置的页面控件配置
	 * @Title: getCheckConfig
	 * @author:wuzhu
	 * @date：
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getPageControlsConfig(int nameHashCode, String pageType) {
		Map<String, String> r = new HashMap<String, String>();
		boolean defaultflag = true;// 是否用默认配置
		if (_pagecontrolsconfigxml == null) {
			_pagecontrolsconfigxml = getPageControlsFromConfig();
		}
		for (GX_PageControlsItem workflow : _pagecontrolsconfigxml.getWorkflows()) {
			if (workflow.getName().hashCode() == nameHashCode) {
				r.clear();
				GX_ids myids = new GX_PageControlsConfig().new GX_ids();
				if (pageType.equals("selectorname"))
					myids = workflow.getSelectornames();
				if (pageType.equals("unitpagejsp"))
					myids = workflow.getUnitpagejsps();
				if (pageType.equals("rightspagejsp"))
					myids = workflow.getRightspagejsps();
				if (pageType.equals("bookpagejsp"))
					myids = workflow.getBookpagejsps();
				if (!StringHelper.isEmpty(myids.getDisabledids())) {
					for (String id : myids.getDisabledids().split("#")) {
						r.put(id, "disabled");
					}
				}
				if (!StringHelper.isEmpty(myids.getHiddenids())) {
					for (String id : myids.getHiddenids().split("#")) {
						r.put(id, "hidden");
					}
				}
				if (!StringHelper.isEmpty(myids.getAbledids())) {
					for (String id : myids.getAbledids().split("#")) {
						r.put(id, "able");
					}
				}
				defaultflag = false;// 不用默认配置
				break;
			}
			if (workflow.getName().equals("default") && defaultflag)// 默认配置
			{
				GX_ids myids = new GX_PageControlsConfig().new GX_ids();
				if (pageType.equals("selectorname"))
					myids = workflow.getSelectornames();
				if (pageType.equals("unitpagejsp"))
					myids = workflow.getUnitpagejsps();
				if (pageType.equals("rightspagejsp"))
					myids = workflow.getRightspagejsps();
				if (pageType.equals("bookpagejsp"))
					myids = workflow.getBookpagejsps();
				if (!StringHelper.isEmpty(myids.getDisabledids())) {
					for (String id : myids.getDisabledids().split("#")) {
						r.put(id, "disabled");
					}
				}
				if (!StringHelper.isEmpty(myids.getHiddenids())) {
					for (String id : myids.getHiddenids().split("#")) {
						r.put(id, "hidden");
					}
				}
			}
		}
		return r;
	}

	private static GX_PageControlsConfig getPageControlsFromConfig() {
		XStream xStream = new SuperXStream();
		xStream.registerConverter(new DateConverter("yyyy-MM-dd", null, TimeZone.getTimeZone("GMT+8")));

		xStream.processAnnotations(GX_PageControlsConfig.class);
		xStream.processAnnotations(GX_PageControlsItem.class);
		xStream.processAnnotations(GX_ids.class);
		// xStream.alias("controlid", String.class);
		// xStream.alias("controlid2", String.class);

		GX_PageControlsConfig _mapping = null;
		try {
			InputStream stream = new FileInputStream(GX_HandlerFactory.class.getResource("/GX_PageControlsConfig.xml").getFile());
			_mapping = (GX_PageControlsConfig) xStream.fromXML(stream);
			stream.close();
			System.out.println("重新读取XML成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("出错啦！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		}
		return _mapping;
	}
}
