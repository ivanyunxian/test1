package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:系统模块管理dao接口
 * @author 刘树峰
 * @date 2015年7月10日 下午2:37:21
 * @Copyright SuperMap
 */
public interface SystemModuleDao extends GenericDao<SystemModule, String> {

	/**
	 * 分页查询模块
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午2:37:55
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition  map参数
	 * @return
	 */
	public Page getPagedModule(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);

	/**
	 * 分页查询模块
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午2:38:07
	 * @param pageIndex
	 * @param pageSize
	 * @param condition String参数
	 * @return
	 */
	public Page getPagedModule(int pageIndex, int pageSize, String condition);

	/**
	 * 根据SystemID获取Modules
	 * 
	 * @param sysID
	 *            系统ID
	 * @return Module列表
	 */
	public List<SystemModule> getSysModules(String sysID);

	/**
	 * 根据父id获取子模块列表
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午2:38:56
	 * @param pid
	 * @return
	 */
	public List<SystemModule> getChildModules(String pid);

	/**
	 * @作者 刘树峰
	 * @创建时间 2015年7月10日下午2:39:41
	 * @param loginName
	 * @return
	 */
	public Set<String> findSysModuleByLoginName(String loginName);

	/**
	 * 根据登陆者获取模块列表
	 * 
	 * @param user
	 * @param sysId
	 * @author diaoliwei
	 * @return
	 */
	public List<SystemModule> getSysModulesByUser(User user, String sysId);

}
