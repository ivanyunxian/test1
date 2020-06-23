package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.supermap.wisdombusiness.framework.dao.RoleGroupDao;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.web.ui.Page;

@Repository("RoleGroupDao")
public class RoleGroupDaoImpl implements RoleGroupDao{

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public RoleGroup load(String id) {
		return (RoleGroup) this.getCurrentSession().load(RoleGroup.class, id);
	}

	@Override
	public RoleGroup get(String id) {
		return (RoleGroup) this.getCurrentSession().get(RoleGroup.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleGroup> findAll() {
		List<RoleGroup> RoleGroupList = this.getCurrentSession().createQuery("from RoleGroup").list();
		return RoleGroupList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedRoleGroup(int pageIndex, int pageSize,
			Map<String, Object> mapCondition) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String str = " from RoleGroup where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		if (mapCondition == null)
			mapCondition = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("groupName"))
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " like :" + "%"
								+ value + "% ");
				}
		}
		String countStr = str + conditionBuilder.toString();
		List<RoleGroup> contList = this.getCurrentSession().createQuery(countStr)
				.setCacheable(true).list();
		if (contList.size() < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(countStr );
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0) query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize),
				contList.size(), pageSize, query.list());
	}

	@Override
	public void persist(RoleGroup entity) {
		this.getCurrentSession().persist(entity);

	}

	@Override
	public String save(RoleGroup entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(RoleGroup entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		RoleGroup entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();

	}
}
