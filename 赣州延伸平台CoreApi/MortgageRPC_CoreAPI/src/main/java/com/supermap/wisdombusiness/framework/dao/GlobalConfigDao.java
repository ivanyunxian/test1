package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.GlobalConfig;

public interface GlobalConfigDao {
	
	void saveConfigurations (GlobalConfig globalconfig);
	List<Map> queryConfiguration();
	void saveOrUpdateConfiguration(GlobalConfig globalconfig);
}
