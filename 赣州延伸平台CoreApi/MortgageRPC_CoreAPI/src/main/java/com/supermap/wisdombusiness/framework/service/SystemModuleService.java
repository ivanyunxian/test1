package com.supermap.wisdombusiness.framework.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:模块管理服务
 * @author 刘树峰
 * @date 2015年7月10日 下午1:47:45
 * @Copyright SuperMap
 */
public interface SystemModuleService {

	/**
	 * 查询系统模块list
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:48:04
	 * @return
	 */
	public List<SystemModule> findAll();

	/**
	 * 分页查询系统模块
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:48:20
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition  map参数
	 * @return
	 */
	public Page getPagedModule(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);

	/**
	 * 分页查询系统模块
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:48:34
	 * @param pageIndex
	 * @param pageSize
	 * @param condition String参数
	 * @return
	 */
	public Page getPagedModule(int pageIndex, int pageSize, String condition);

	/**
	 * 根据系统id获取系统模块list
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:49:18
	 * @param systemId
	 * @return
	 */
	public List<SystemModule> getSysModules(String systemId);
	
	/**
	 * 根据id获取系统模版信息
	 * @作者 diaoliwei
	 * @创建时间 2015年7月2日下午3:30:58
	 * @param moduleId
	 * @return
	 */
	public SystemModule getSysModule(String moduleId);

	/**
	 * 保存模块信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:49:45
	 * @param module
	 */
	public void saveModule(SystemModule module);

	/**
	 * 删除模块信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:49:55
	 * @param id
	 */
	public void deleteModuleById(String id);

	/**
	 * 更新模块信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:50:05
	 * @param module
	 */
	public void updateModule(SystemModule module);

	/**
	 * 删除模块以及子模块
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:50:25
	 * @param id
	 */
	public void deleteModuleTree(String id);
	
	/**
	 * 根据登陆者获取模块列表
	 * @param user
	 * @param sysId
	 * @author diaoliwei
	 * @return
	 */
	public List<SystemModule> getSysModulesByUser(User user, String sysId);

}
