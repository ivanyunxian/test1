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

import com.supermap.wisdombusiness.framework.dao.UserDao;
import com.supermap.wisdombusiness.framework.dao.UserRoleDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserRoleDao userRoleDao;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public User load(String id) {
		if (null == id) {
			return null;
		} else {
			return (User) this.getCurrentSession().load(User.class, id);
		}

	}

	@Override
	public User get(String id) {
		if (null == id) {
			return null;
		} else {
			return (User) this.getCurrentSession().get(User.class, id);
		}
	}

	public void update(User user) {
		if(user==null){
			
		}else{
			this.getCurrentSession().update(user);
			this.getCurrentSession().flush();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		List<User> userList = this.getCurrentSession().createQuery("from User").setCacheable(true).list();
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findByLoginName(String loginName) {
		List<User> userList = this.getCurrentSession().createQuery("from User where loginName = '" + loginName + "'")
				.setCacheable(true).list();
		if (userList.size() > 0)
			return userList.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedUser(int pageIndex, int pageSize, Map<String, Object> mapCondition) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String count_str = "select count(*)  from User where 1=1 ";
		String str = "  from User where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		if (mapCondition == null)
			mapCondition = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("userName"))
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " like '%" + value + "%' ");
				}

			if (key.equals("loginName"))
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " like '%" + value + "%' ");
				}
		}
		String countStr = count_str + conditionBuilder.toString();
		String sqlStr = str + conditionBuilder.toString();
		long totalCount = (Long) this.getCurrentSession().createQuery(countStr).setCacheable(true).list().get(0);
		if (totalCount < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(sqlStr);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		List<User> usersList = query.list();
		List<User> updateList = new ArrayList<User>();
		for (User user : usersList) {
			if (user.getDepartment() != null) {
				user.setDepartmentId(user.getDepartment().getId());
				user.setDepartmentName(user.getDepartment().getDepartmentName());
			}
			List<String> roleList = userRoleDao.findRoleIdByUserId(user.getId());
			String roleIds = "";
			for (int j = 0; j < roleList.size(); j++) {
				if (j == roleList.size() - 1)
					roleIds += roleList.get(j);
				else
					roleIds += roleList.get(j) + ",";
			}
			user.setRoleId(roleIds);
			updateList.add(user);
		}
		return new Page(Page.getStartOfPage(pageIndex, pageSize), totalCount, pageSize, updateList);
	}

	@Override
	public void persist(User entity) {
		this.getCurrentSession().persist(entity);

	}

	@Override
	public String save(User entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(User entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		User entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();
	}

	@Override
	public int findByDepartment(String departmentId) {
		int totalCount = 0;
		if (!StringUtils.isEmpty(departmentId)) {
			String totalHql = " select count(1) from User where departmentId = '" + departmentId + "' ";
			Query total = this.getCurrentSession().createQuery(totalHql);
			totalCount = new Long((Long) total.uniqueResult()).intValue();
		}
		return totalCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByDepartmentId(String departmentId) {
		// 过滤掉被禁用的人员
		List<User> userList = this.getCurrentSession()
				.createQuery("from User t where t.department.id = '" + departmentId + "' and t.status ='NORMAL'")
				.setCacheable(true).list();
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserWithNoDepartmentId() {
		// 过滤掉被禁用的人员
		List<User> userList = this.getCurrentSession()
				.createQuery("from User t where t.department.id is null  and t.status ='NORMAL' ").setCacheable(true)
				.list();
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserByCN(String userCN) {
		List<User> userList = this.getCurrentSession()
				.createQuery("from User t where t.identifyCn = '" + userCN + "' and t.status ='NORMAL'")
				.setCacheable(true).list();
		if (userList.size() > 0)
			return userList.get(0);
		return null;
	}

}
