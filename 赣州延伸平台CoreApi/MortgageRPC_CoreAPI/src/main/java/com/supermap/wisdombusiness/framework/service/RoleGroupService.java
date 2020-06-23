package com.supermap.wisdombusiness.framework.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:角色分组service
 * @author 杨鹏
 * @date 2015年7月10日 下午1:45:17
 * @Copyright SuperMap
 */
public interface RoleGroupService {

	/**
	 * 根据id获取角色分组
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:45:39
	 * @param id
	 * @return
	 */
	public RoleGroup findById(String id);
	
	/**
	 * 查询角色分组List
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:45:55
	 * @return
	 */
	public List<RoleGroup> findAll();
	
	/**
	 * 分页查询角色分组
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:46:24
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedRoleGroup(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 保存角色分组信息
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:46:41
	 * @param roleGroup
	 */
	public void saveRoleGroup(RoleGroup roleGroup);
	
	/**
	 * 删除角色分组信息
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:46:54
	 * @param id
	 */
	public void deleteRoleGroupById(String id);
	
	/**
	 * 更新角色分组信息
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:47:07
	 * @param roleGroup
	 */
	public void updateRoleGroup(RoleGroup roleGroup);
}
