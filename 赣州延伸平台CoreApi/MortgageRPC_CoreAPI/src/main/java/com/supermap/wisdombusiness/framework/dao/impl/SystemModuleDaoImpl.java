package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.supermap.wisdombusiness.framework.dao.SystemModuleDao;
import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;

@Repository("systemModuleDao")
public class SystemModuleDaoImpl implements SystemModuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public SystemModule load(String id) {
		SystemModule module = (SystemModule) this.getCurrentSession().load(
				SystemModule.class, id);
		return module;
	}

	@Override
	public SystemModule get(String id) {
		SystemModule module = (SystemModule) this.getCurrentSession().load(
				SystemModule.class, id);
		return module;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemModule> findAll() {
		List<SystemModule> moduleList = this.getCurrentSession()
				.createQuery("from SystemModule").list();
		return moduleList;
	}

	@Override
	public void persist(SystemModule entity) {
		this.getCurrentSession().persist(entity);
	}

	@Override
	public String save(SystemModule entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(SystemModule entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		SystemModule entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> findSysModuleByLoginName(String loginName) {
		List <String> RoleList = this.getCurrentSession().createQuery(
				" select sm.id   from UserRole ur ,User u ,Role r , RoleMoudle rm, SystemModule sm where u.loginName = " + loginName + " and ur.user.id = u.id  and ur.role.id = r.id and r.id = rm.role.id and rm.systemModule.id = sm.id")
				.setCacheable(true).list();
		return new HashSet<String>(RoleList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedModule(int pageIndex, int pageSize,
			Map<String, Object> mapCondition) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String str = " from SystemModule where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		if (mapCondition == null)
			mapCondition = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("Remark") || key.equals("SysModuleName")
					|| key.equals("SysModuleUrl") || key.equals("Description")) {
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " like :" + "%"
								+ value + "% ");
				}
			}
		}
		String countStr = str + conditionBuilder.toString();
		List<SystemModule> contList = this.getCurrentSession()
				.createQuery(countStr).setCacheable(true).list();
		if (contList.size() < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(countStr);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize),
				contList.size(), pageSize, query.list());

	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedModule(int pageIndex, int pageSize, String condition) {
		String countStr = null;
		if (condition.trim().toUpperCase().startsWith("AND")) {
			countStr = " from SystemModule where 1=1 " + condition;
		} else {
			countStr = " from SystemModule where " + condition;
		}
		List<SystemModule> contList = this.getCurrentSession()
				.createQuery(countStr).setCacheable(true).list();
		if (contList.size() < 1)
			return new Page();
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		Query query = this.getCurrentSession().createQuery(countStr);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize),
				contList.size(), pageSize, query.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemModule> getSysModules(String sysID) {
		String sqlStr = " from SystemModule where SystemID='" + sysID + "' ORDER BY SORT";
		List<SystemModule> contList = this.getCurrentSession()
				.createQuery(sqlStr).list();
		return contList;
	}

	@Override
	public List<SystemModule> getChildModules(String pid) {
		String sqlStr = " from SystemModule where parentId='" + pid + "'";
		@SuppressWarnings("unchecked")
		List<SystemModule> contList = this.getCurrentSession()
				.createQuery(sqlStr).list();
		return contList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemModule> getSysModulesByUser(User user, String sysId) {
		StringBuilder conditionSql = new StringBuilder();
		conditionSql.append(" SELECT T.SYSMODULEID FROM RT_ROLEMODULE T WHERE T.ROLEID IN ( ");
		conditionSql.append(" SELECT RU.ROLEID FROM RT_USERROLE RU WHERE RU.USERID =( ");
		conditionSql.append(" SELECT U.ID FROM T_USER U WHERE U.LOGINNAME='").append(user.getLoginName()).append("')))");
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT * FROM T_SYSTEMMODULE M WHERE (M.ID IN (");
		sqlBuilder.append(conditionSql);
		sqlBuilder.append(" OR ");
		sqlBuilder.append(" M.ID IN(");
		sqlBuilder.append(" SELECT M.PARENTID FROM T_SYSTEMMODULE M WHERE M.ID IN (");
		sqlBuilder.append(conditionSql).append(")) ");
		sqlBuilder.append(" AND M.SYSTEMID = '").append(sysId).append("' ORDER BY SORT");
		Query q = this.getCurrentSession().createSQLQuery(sqlBuilder.toString()).addEntity(SystemModule.class);
		List<SystemModule> list = new ArrayList<SystemModule>();
		list = q.list();
		return list;
	}
}
