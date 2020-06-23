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

import com.supermap.wisdombusiness.framework.dao.RoleDao;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User.UserStatus;
import com.supermap.wisdombusiness.web.ui.Page;


@Repository("RoleDao")
public class RoleDaoImpl implements RoleDao{

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Role load(String id) {
		return (Role) this.getCurrentSession().load(Role.class, id);
	}

	@Override
	public Role get(String id) {
		return (Role) this.getCurrentSession().get(Role.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findAll() {
		List<Role> RoleList = this.getCurrentSession().createQuery("from Role")
				.setCacheable(true).list();
		return RoleList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List <String> getRolesByLoginName(String loginName) {
		List <String> roleList = this.getCurrentSession().createQuery(
				" select r.id   from UserRole ur ,User u ,Role r where u.loginName = " + loginName + " and ur.user.id = u.id "+ " and ur.role.id = r.id ")
				.setCacheable(true).list();
		return roleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedRole(int pageIndex, int pageSize,
			Map<String, Object> mapCondition) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String count_str = "select count(*)  from Role where 1=1 ";
		String str = " from Role where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		if (mapCondition == null)
			mapCondition = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("roleName"))
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " like :" + "%"
								+ value + "% ");
				}
		}
		String countStr = count_str + conditionBuilder.toString();
		String sqlStr = str + conditionBuilder.toString();
		long totalCount = (Long)this.getCurrentSession().createQuery(countStr).setCacheable(true).list().get(0);
		if (totalCount < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(sqlStr);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0) query.setMaxResults(pageSize);
			
		List<Role> rolesList = query.list();
		List<Role> updateList = new ArrayList<Role>();
		for(Role role:rolesList){ 
			if(role.getRoleGroup() !=null){
				role.setGroupId(role.getRoleGroup().getId());
				role.setGroupName(role.getRoleGroup().getGroupName());
			}
			updateList.add(role);
		}
		
		return new Page(Page.getStartOfPage(pageIndex, pageSize),
				totalCount, pageSize, updateList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedUserByRole(int pageIndex, int pageSize,
			Map<String, Object> mapCondition) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		
		String str = "SELECT A.id,A.department,A.loginName,A.password,A.userName,A.male,A.mobile,"
				+ "A.tel,A.areaCode,A.status,A.remark "
				+ "FROM User A, UserRole B, Role C WHERE A.id = B.user AND B.role = C.id";
		StringBuilder conditionBuilder = new StringBuilder();
		if (mapCondition == null)
			mapCondition = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("role_id"))
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + "C.id" + " = " + "'"
								+ value + "' ");
				}
		}
		String countStr = str + conditionBuilder.toString();
		List<Role> contList = this.getCurrentSession().createQuery(countStr)
				.setCacheable(true).list();
		if (contList.size() < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(countStr );
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		
		////
		List<Object[]> users = query.list(); 
		List<Map<String,String>> data = new ArrayList<Map<String,String>>(); 
		for (Object[] object : users) 
		{			
			Map<String,String> item = new HashMap<String,String>(); 
			
			item.put("id", (String)object[0]);
			Department department = (Department)object[1];
			item.put("departmentName", department.getDepartmentName());
			item.put("loginName", (String)object[2]);
			item.put("password", (String)object[3]);
			item.put("userName", (String)object[4]);
			Integer maleInteger = (Integer)object[5];
			item.put("male", maleInteger.toString());
			item.put("mobile", (String)object[6]);
			item.put("tel", (String)object[7]);
			item.put("areaCode", (String)object[8]);
			UserStatus userStatus = (UserStatus)object[9];
			item.put("status", userStatus.getValue());
			item.put("remark", (String)object[10]);
			
			data.add(item); 
		}
			
		
		////
		
		if (pageSize > 0) query.setMaxResults(pageSize);
//		return new Page(Page.getStartOfPage(pageIndex, pageSize),
//				contList.size(), pageSize, query.list());
		return new Page(Page.getStartOfPage(pageIndex, pageSize),
				contList.size(), pageSize, data);
	}
	
	@Override
	public void persist(Role entity) {
		this.getCurrentSession().persist(entity);

	}

	@Override
	public String save(Role entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(Role entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		Role entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List <Role> getRolesByGroupId(String id){
		List<Role> RoleList = this.getCurrentSession().createQuery("from Role r where r.roleGroup.id =  '"+id+"'").list();
		return RoleList;
	}
	//end

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRoleByRoleCode(String roleCode) {
		List<Role> RoleList = this.getCurrentSession().createQuery("from Role r where r.roleCode =  '"+roleCode+"'").list();
		return RoleList;
	}
}
