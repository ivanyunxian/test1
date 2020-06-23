package com.supermap.wisdombusiness.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.wisdombusiness.framework.dao.SystemDao;
import com.supermap.wisdombusiness.framework.dao.SystemModuleDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.SystemService;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.framework.model.System;

/**
 * 
 * @Description:系统定义serviceImpl
 * @author 刘树峰
 * @date 2015年7月10日 下午2:04:53
 * @Copyright SuperMap
 */
@Service("systemService")
public class SystemServiceImpl implements SystemService {

	@Autowired
	private SystemDao systemDao;

	@Autowired
	private SystemModuleDao systemModuleDao;

	@Autowired
	private CommonDao baseCommonDao;

	@Override
	public List<System> findAll() {
		return this.systemDao.findAll();
	}
	
	@Transactional
	@Override
	public void saveSystem(System system) {
		this.systemDao.save(system);
		this.systemDao.flush();
	}
	
	@Transactional
	@Override
	public void deleteSystemById(String id) {
		this.systemDao.delete(id);
	}
	
	@Transactional
	@Override
	public void updateSystem(System system) {
		this.systemDao.saveOrUpdate(system);
	}
	
	@Transactional
	@Override
	public Page getPagedSystem(int pageIndex, int pageSize, String condition) {
		return baseCommonDao.getPageDataByHql(System.class, condition, pageIndex, pageSize);
	}
	
	@Transactional
	@Override
	public System getSystem(String id) {
		return systemDao.get(id);
	}
	
	@Transactional
	@Override
	public void deleteSystemIncludeModuleTree(String systemId) {
		List<SystemModule> list = systemModuleDao.getSysModules(systemId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				systemModuleDao.delete(list.get(i).getId());
			}
		}
		systemDao.delete(systemId);
	}
	
	@Transactional
	@Override
	public List<System> getSystemsByUser(User user) {
		return systemDao.getSystemsByUser(user);
	}

}
