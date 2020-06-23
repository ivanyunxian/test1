package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.supermap.wisdombusiness.framework.dao.RoleModuleDao;
import com.supermap.wisdombusiness.framework.model.RoleModule;


@Repository("RoleModuleDao")
public class RoleModuleDaoImpl implements RoleModuleDao{

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public RoleModule load(String id) {
		return (RoleModule) this.getCurrentSession().load(RoleModule.class, id);
	}

	@Override
	public RoleModule get(String id) {
		return (RoleModule) this.getCurrentSession().get(RoleModule.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleModule> findAll() {
		List<RoleModule> RoleModuleList = this.getCurrentSession().createQuery("from RoleModule")
				.setCacheable(true).list();
		return RoleModuleList;
	}
	
	@Override
	public void persist(RoleModule entity) {
		this.getCurrentSession().persist(entity);

	}

	@Override
	public String save(RoleModule entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(RoleModule entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		RoleModule entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRoleModuleIdByRoleId(String roleId){
		List<String> RoleModuleList = this.getCurrentSession().createQuery("select  a.id from RoleModule a where a.role.id = '"+roleId+"'")
				.setCacheable(true).list();
		return RoleModuleList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findModuleIdByRoleId(String roleId){
		List<String> ModuleList = this.getCurrentSession().createQuery("select  a.systemModule.id from RoleModule a where a.role.id = '"+roleId+"'")
				.setCacheable(true).list();
		return ModuleList;
	}
}



