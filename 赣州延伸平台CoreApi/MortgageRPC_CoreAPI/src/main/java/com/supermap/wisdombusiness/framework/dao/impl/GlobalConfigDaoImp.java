package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.supermap.wisdombusiness.framework.dao.GlobalConfigDao;
import com.supermap.wisdombusiness.framework.model.GlobalConfig;

/**
 * @author JHX
 * 保存配置文件信息
 * */
@Repository("GlobalConfigDao")
public class GlobalConfigDaoImp implements GlobalConfigDao{
	@Autowired
	private CommonDao commonDao;
	@Override
	public void saveConfigurations(GlobalConfig globalconfig) {
		commonDao.save(globalconfig);
		commonDao.flush();
	}
	@Override
	public List<Map> queryConfiguration() {
		return commonDao.getDataListByFullSql("select * from smwb_framework.T_GLOBALCONFIG");
	}
	@Override
	public void saveOrUpdateConfiguration(GlobalConfig globalconfig) {
		commonDao.saveOrUpdate(globalconfig);
		commonDao.flush();
	}

}
