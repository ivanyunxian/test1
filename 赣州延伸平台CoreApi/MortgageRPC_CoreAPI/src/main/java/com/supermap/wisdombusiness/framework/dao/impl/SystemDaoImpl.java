package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.supermap.wisdombusiness.framework.dao.SystemDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.System;

/**
 * 
 * @author 刘树峰
 *
 */
@Repository("systemDao")
public class SystemDaoImpl implements SystemDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public System load(String id) {
		return (System) this.getCurrentSession().load(System.class, id);
	}

	@Override
	public System get(String id) {
		return (System) this.getCurrentSession().get(System.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<System> findAll() {
		List<System> systemList = this.getCurrentSession().createQuery("from System")
				.setCacheable(true).list();
		return systemList;
	}

	@Override
	public void persist(System entity) {
		this.getCurrentSession().persist(entity);

	}

	@Override
	public String save(System entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(System entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		System entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<System> getSystemsByUser(User user) {
		StringBuilder conditionSql = new StringBuilder();
		conditionSql.append(" SELECT T.SYSMODULEID FROM RT_ROLEMODULE T WHERE T.ROLEID IN ( ");
		conditionSql.append(" SELECT RU.ROLEID FROM RT_USERROLE RU WHERE RU.USERID =( ");
		conditionSql.append(" SELECT U.ID FROM T_USER U WHERE U.LOGINNAME='")
				.append(user.getLoginName()).append("')))");

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from T_SYSTEM t where t.id in( SELECT systemid FROM T_SYSTEMMODULE M WHERE (M.ID IN (");
		sqlBuilder.append(conditionSql);
		sqlBuilder.append(" OR ");
		sqlBuilder.append(" M.ID IN(");
		sqlBuilder.append(" SELECT M.PARENTID FROM T_SYSTEMMODULE M WHERE M.ID IN (");
		sqlBuilder.append(conditionSql).append(")) ");
		sqlBuilder.append(" group by systemid ) order by t.createtime desc ");
		Query q = this.getCurrentSession().createSQLQuery(sqlBuilder.toString()).addEntity(System.class);
		List<System> list = new ArrayList<System>();
		list = q.list();
		return list;
	}

}
