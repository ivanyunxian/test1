package com.supermap.wisdombusiness.framework.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.GlobalConfig;

public interface GlobalConfigService {
	
	
	String excuteConfigurationStorage (String configurations);
	void saveConfigurations (GlobalConfig globalconfig);
	List<Map> getConfiguration();
	boolean  chekIsStorage ();
	void saveOrUpdateConfiguration(GlobalConfig globalconfig);
	//配置信息入库完成之后更改configure.js的名称：
	void modfifyConfigureJsFileName();
	//数据库配置更新之后，需要更新配置version.js文件的版本号
	void updateConfigureInfoVesion(String version);

}
