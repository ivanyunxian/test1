package com.supermap.wisdombusiness.framework.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.wisdombusiness.framework.dao.SystemModuleDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.RoleModule;
import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.SystemModuleService;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:系统模块serviceImpl
 * @author 刘树峰
 * @date 2015年7月10日 下午2:03:20
 * @Copyright SuperMap
 */
@Service("moduleService")
public class SystemModuleServiceImpl implements SystemModuleService {

	@Autowired
	private SystemModuleDao systemModuleDao;
	
	@Autowired
	private CommonDao baseCommonDao;
	
	@Transactional
	@Override
	public List<SystemModule> findAll() {
		return systemModuleDao.findAll();
	}
	
	@Transactional
	@Override
	public Page getPagedModule(int pageIndex, int pageSize,
			Map<String, Object> mapCondition) {
		return systemModuleDao
				.getPagedModule(pageIndex, pageSize, mapCondition);
	}
	
	@Transactional
	@Override
	public Page getPagedModule(int pageIndex, int pageSize, String condition) {
		return systemModuleDao.getPagedModule(pageIndex, pageSize, condition);
	}
	
	@Transactional
	@Override
	public void saveModule(SystemModule module) {
		systemModuleDao.save(module);
		systemModuleDao.flush();
	}

	/**
	 * 根据ID删除一个模块
	 */
	@Transactional
	@Override
	public void deleteModuleById(String id) {
		systemModuleDao.delete(id);
	}

	/**
	 * 更新一个模块
	 */
	@Transactional
	@Override
	public void updateModule(SystemModule module) {
		systemModuleDao.saveOrUpdate(module);
	}

	/**
	 * 根据SystemID获取模块列表
	 */
	@Transactional
	@Override
	public List<SystemModule> getSysModules(String systemId) {
		return systemModuleDao.getSysModules(systemId);
	}

	/**
	 * 删除模块以及子模块
	 */
	@Transactional
	@Override
	public void deleteModuleTree(String id) {
		// 递归删除该模块树
		List<String> ids = new ArrayList<String>();
		GetIDs(ids, id);
		for (int i = 0; i < ids.size(); i++) {
			try {
				String strsql = " SYSMODULEID='" + ids.get(i) + "'";
				List<RoleModule> rolemodulelist = baseCommonDao.getDataList(
						RoleModule.class, strsql);
				if (rolemodulelist != null && rolemodulelist.size() > 0) {
					for (int k = 0; k < rolemodulelist.size(); k++) {
						baseCommonDao.deleteEntity(rolemodulelist.get(k));
					}
				}
				systemModuleDao.delete(ids.get(i));
			} catch (Exception e) {
				System.out.println("大事不好啦!:" + e.getMessage());
			}
		}
	}

	/**
	 * 递归获取模块和子模块的ID列表
	 * 
	 * @param ids
	 * @param id
	 */
	@Transactional
	private void GetIDs(List<String> ids, String id) {
		if (!ids.contains(id)) {
			ids.add(id);
		}
		List<SystemModule> cmodules = systemModuleDao.getChildModules(id);
		if (cmodules != null && cmodules.size() > 0) {
			for (int i = 0; i < cmodules.size(); i++) {
				GetIDs(ids, cmodules.get(i).getId());
			}
		}
	}
	
	@Transactional
	@Override
	public List<SystemModule> getSysModulesByUser(User user, String sysId) {
		return systemModuleDao.getSysModulesByUser(user, sysId);
	}
	
	@Override
	public SystemModule getSysModule(String moduleId) {
		return systemModuleDao.get(moduleId);
	}
}
