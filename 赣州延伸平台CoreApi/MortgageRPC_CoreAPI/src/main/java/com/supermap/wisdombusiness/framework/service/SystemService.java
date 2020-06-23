package com.supermap.wisdombusiness.framework.service;

import java.util.List;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.framework.model.System;

/**
 * 
 * @Description:系统定义service
 * @author 刘树峰
 * @date 2015年7月10日 下午1:51:26
 * @Copyright SuperMap
 */
public interface SystemService {

	/**
	 * 获取全部系统list
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:51:55
	 * @return
	 */
	public List<System> findAll();

	/**
	 * 分页查询系统
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:52:19
	 * @param pageIndex
	 * @param pageSize
	 * @param condition
	 * @return
	 */
	public Page getPagedSystem(int pageIndex, int pageSize, String condition);

	/**
	 * 根据id获取系统信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:52:32
	 * @param id
	 * @return
	 */
	public System getSystem(String id);

	/**
	 * 保存系统信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:52:47
	 * @param system
	 */
	public void saveSystem(System system);

	/**
	 * 删除根据id系统信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:52:58
	 * @param id
	 */
	public void deleteSystemById(String id);

	/**
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:53:18
	 * @param systemId
	 */
	public void deleteSystemIncludeModuleTree(String systemId);

	/**
	 * 更新系统信息
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午1:53:47
	 * @param system
	 */
	public void updateSystem(System system);

	/**
	 * 根据用户获取系统
	 * 
	 * @param user
	 * @author diaoliwei
	 * @return
	 */
	public List<System> getSystemsByUser(User user);
}
