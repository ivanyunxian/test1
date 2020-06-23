package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.System;

/**
 * 
 * @Description:系统管理dao接口
 * @author 刘树峰
 * @date 2015年7月10日 下午2:40:14
 * @Copyright SuperMap
 */
public interface SystemDao extends GenericDao<System, String> {

	public List<System> findAll();
	
	/**
	 * 根据用户获取系统
	 * @param user
	 * @author diaoliwei
	 * @return
	 */
	public List<System> getSystemsByUser(User user);
	
}
