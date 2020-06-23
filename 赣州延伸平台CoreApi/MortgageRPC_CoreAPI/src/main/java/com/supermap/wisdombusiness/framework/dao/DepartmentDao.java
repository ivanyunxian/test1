package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.Department;

/**
 * 
 * @Description:部门管理dao接口
 * @author diaoliwei
 * @date 2015年7月6日 上午10:46:39
 * @Copyright SuperMap
 */
public interface DepartmentDao extends GenericDao<Department, String> {

	/**
	 * 获取部门分页列表
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @author diaoliwei
	 * @return
	 */
	public List<Department> getPagedDepartment(int pageIndex, int pageSize, Map<String, Object> mapCondition, boolean isParent);
	
	/**
	 * 删除多条记录
	 * @param list
	 * @author diaoliwei
	 * @return
	 */
	public int deleteByIds(List<Department> list);
	
	/**
	 * 获取id下的子部门列表
	 * @作者 diaoliwei
	 * @创建时间 2015年7月6日上午10:47:36
	 * @param id
	 * @return
	 */
	public List<Department> queryDepartments(String id);

	String getArea_CodeByDeptmentID(String deptmentid);
}
