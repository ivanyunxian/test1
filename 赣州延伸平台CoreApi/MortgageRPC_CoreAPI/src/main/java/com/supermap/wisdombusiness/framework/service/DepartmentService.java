package com.supermap.wisdombusiness.framework.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

/**
 * 
 * @Description:部门管理service接口
 * @author diaoliwei
 * @date 2015年7月6日 上午10:44:53
 * @Copyright SuperMap
 */
public interface DepartmentService {
	
	/**
	 * 根据id查询部门对象
	 * @param id
	 * @author diaoliwei
	 * @return
	 */
	public Department findById(String id);
	
	/**
	 * 查询全部
	 * @author diaoliwei
	 * @return
	 */
	public List<Department> findAll();
	
	/**
	 * 查询分页列表
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @author diaoliwei
	 * @return
	 */
	public List<Department> getPagedDepartment(int pageIndex, int pageSize, Map<String, Object> mapCondition, boolean isParent);
	
	/**
	 * 新增部门
	 * @param department
	 * @author diaoliwei
	 */
	public void saveDepartment(Department department);
	
	/**
	 * 删除多条记录
	 * @param id
	 * @author diaoliwei
	 * @return
	 */
	public int delDepartment(String id);
	
	/**
	 * 更新部门信息
	 * @param department
	 * @author diaoliwei
	 */
	public void updateDepartment(Department department);
	
	/**
	 * 	查询部门树
	 * @return
	 */
	public List<Tree> getDepartmentComboTree(String id);
	
	/**
	 * 设置部门树
	 * @创建时间 2015年7月10日下午1:44:55
	 * @param id
	 * @return
	 */
	public List<Tree> setDepartmentComboTree(String id);

}
