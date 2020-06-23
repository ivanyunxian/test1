package com.supermap.wisdombusiness.framework.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.wisdombusiness.framework.dao.DepartmentDao;
import com.supermap.wisdombusiness.framework.dao.UserDao;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.web.ui.tree.TreeUtil;

/**
 * 
 * @Description:部门serviceImpl
 * @author diaoliwei
 * @date 2015年7月10日 下午2:04:31
 * @Copyright SuperMap
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	//部门dao
	@Autowired
	private DepartmentDao departmentDao;
	
	//用户dao
	@Autowired
	private UserDao userDao;
	
	@Override
	public Department findById(String id) {
		return departmentDao.get(id);
	}

	@Override
	public List<Department> findAll() {
		return departmentDao.findAll();
	}

	@Override
	public List<Department> getPagedDepartment(int pageIndex, int pageSize, Map<String, Object> mapCondition, boolean isParent) {
		Map<String, Object> map = new HashMap<String,Object>();
		for(Iterator<String> iterator = mapCondition.keySet().iterator();iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if(StringUtils.hasLength(value.toString())) {
				map.put(key, value);
			}
		}
		return departmentDao.getPagedDepartment(pageIndex, pageSize, map, isParent);
	}

	@Override
	public void saveDepartment(Department department) {
		departmentDao.saveOrUpdate(department);
	}

	@Override
	public int delDepartment(String id) {
		String[] idString = id.split("#");
		List<Department> list = new ArrayList<Department>();
		Department model = null;
		for (int i = 0; i < idString.length; i++) {
			model = new Department();
			model.setId(idString[i]);
			list.add(model);
		}
		//查询该部门是否关联用户
		int result = userDao.findByDepartment(list.get(0).getId());
		if(result > 0){
			return -1;//已关联用户
		}else{
			return departmentDao.deleteByIds(list);
		}
	}

	@Override
	public void updateDepartment(Department department) {
		departmentDao.saveOrUpdate(department);
	}

	@Transactional(readOnly = true)
	public List<Tree> getDepartmentComboTree(String id) {
		List<Tree> treelist = new ArrayList<Tree>();
		List<Department> departments = departmentDao.queryDepartments(id);
		
		for(Iterator<Department> iterator = departments.iterator();iterator.hasNext();){
			Department department = (Department)iterator.next();
			Tree tree = transform(department);
			treelist.add(tree);
		}
		return TreeUtil.build(treelist);
	}
	
	@Transactional(readOnly = true)
	public List<Tree> setDepartmentComboTree(String id) {
		List<Tree> treelist = new ArrayList<Tree>();
		List<Department> departments = departmentDao.findAll();
		for(Iterator<Department> iterator = departments.iterator();iterator.hasNext();){
			Department department = (Department)iterator.next();
			Tree tree = transform(department);
			if(department.getId().equals(id))
				tree.setChecked(true);
			treelist.add(tree);
		}
		return TreeUtil.build(treelist);
	}
	
	/**
	 * Department转换Tree对象
	 * @param department
	 * @return
	 */
	private Tree transform(Department department) {
		Tree tree = new Tree();
		tree.setId(department.getId());
		tree.setText(department.getDepartmentName());
		tree.setParentid(department.getParentId());
		return tree;
	}

}
