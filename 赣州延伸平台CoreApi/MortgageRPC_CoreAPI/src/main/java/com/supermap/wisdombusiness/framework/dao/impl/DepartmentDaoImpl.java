package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.supermap.wisdombusiness.framework.dao.DepartmentDao;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.web.ui.Page;

@Repository("departmentDao")
public class DepartmentDaoImpl implements DepartmentDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Department load(String id) {
		return (Department) this.getCurrentSession().load(Department.class, id);
	}

	@Override
	public Department get(String id) {
		return (Department) this.getCurrentSession().get(Department.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> findAll() {
		List<Department> departmentList = this.getCurrentSession().createQuery("from Department").setCacheable(true).list();
		return departmentList;
	}

	@Override
	public void persist(Department entity) {
		this.getCurrentSession().persist(entity);
	}

	@Override
	public String save(Department entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(Department entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		Department entity = this.load(id);
		this.getCurrentSession().delete(entity);
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> getPagedDepartment(int pageIndex, int pageSize, Map<String, Object> mapCondition, boolean isParent) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String hqlStr = " from Department where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		if (mapCondition == null){
			mapCondition = new HashMap<String, Object>();
		}
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("departmentName"))
				if (value != null) {
					if (!value.toString().equals("")){
					    conditionBuilder.append(" and ").append(key).append(" like '%").append(value).append("%' ");
					}
				}
		}
		if(isParent){
			conditionBuilder.append(" and parentId is null ");
		}else {
			conditionBuilder.append(" and parentId is not null ");
		}
		hqlStr += conditionBuilder.toString();
		String totalHql = " select count(1) from Department where 1=1 ";
		totalHql += conditionBuilder.toString();
		Query total = this.getCurrentSession().createQuery(totalHql);
		int totalCount = new Long((Long) total.uniqueResult()).intValue();
		if (totalCount < 1){
			return new ArrayList<Department>();
		}
		Query query = this.getCurrentSession().createQuery(hqlStr);
		
		if(isParent){
			query.setFirstResult(firstResult < 0 ? 0 : firstResult);
			if (pageSize > 0){
				query.setMaxResults(pageSize);
			}
		}
		
		return query.list();
	}

	@Override
	public int deleteByIds(List<Department> list) {
		if(list.size() == 0){
			return 0;
		}
		StringBuilder sqlBuilder = new StringBuilder();
		StringBuilder conditionSql = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Department model = list.get(i);
			if(list.size() - 1 == i){
				conditionSql.append("'").append(model.getId()).append("'");
			}else{
				conditionSql.append("'").append(model.getId()).append("',");
			}
		}
		sqlBuilder.append(" delete from T_DEPARTMENT where id in (");
		sqlBuilder.append(conditionSql);
		sqlBuilder.append(") ");
		sqlBuilder.append(" or parentid in (");
		sqlBuilder.append(conditionSql);
		sqlBuilder.append(") ");
		return this.getCurrentSession().createSQLQuery(sqlBuilder.toString()).executeUpdate();
	}
	
	@Override
	public List<Department> queryDepartments(String id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" from Department ");
        if(!StringUtils.isEmpty(id)){
        	sqlBuilder.append(" where id not in (")
        	.append("select id from Department where id ='").append(id)
        	.append("' or parentId='").append(id).append("')");
		}
		@SuppressWarnings("unchecked")
		List<Department> list = this.getCurrentSession().createQuery(sqlBuilder.toString()).list();
		return list;
	}
	@Override
	public String  getArea_CodeByDeptmentID(String deptmentid){
		String AreaCode="";
		if(deptmentid!=null&&!deptmentid.equals("")){
			Department dept=this.load(deptmentid);
			if(dept!=null){
				AreaCode=dept.getAreaCode();
			}
		}
		return AreaCode;
		
	}

}
