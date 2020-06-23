package com.supermap.wisdombusiness.framework.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.supermap.realestate.registration.util.Global;
import com.supermap.wisdombusiness.framework.dao.UserRoleDao;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.UserRole;

@Repository("userRoleDao")
public class UserRoleDaoImpl implements UserRoleDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public UserRole load(String id) {
		return (UserRole) this.getCurrentSession().load(UserRole.class, id);
	}

	@Override
	public UserRole get(String id) {
		return (UserRole) this.getCurrentSession().get(UserRole.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> findAll() {
		List<UserRole> userRoleList = this.getCurrentSession()
				.createQuery("from UserRole").setCacheable(true).list();
		return userRoleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findRoleIdByUserId(String userId) {
		List<String> userRoleList = this
				.getCurrentSession()
				.createQuery(
						"select  t.role.id from UserRole t where t.user.id = '"
								+ userId + "'").setCacheable(true).list();
		return userRoleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUserRoleIdByUserId(String userId) {
		List<String> userRoleList = this
				.getCurrentSession()
				.createQuery(
						"select  t.id from UserRole t where t.user.id = '"
								+ userId + "'").setCacheable(true).list();
		return userRoleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUserRoleIdByRoleId(String roleId) {
		List<String> userRoleList = this
				.getCurrentSession()
				.createQuery(
						"select  t.id from UserRole t where t.role.id = '"
								+ roleId + "'").setCacheable(true).list();
		return userRoleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUserIdByRoleId(String roleId) {
		List<String> userList = this
				.getCurrentSession()
				.createQuery(
						"select t.user.id from UserRole t where t.role.id = '"
								+ roleId + "'").setCacheable(true).list();
		return userList;
	}

	@Override
	public void persist(UserRole entity) {
		this.getCurrentSession().persist(entity);

	}

	@Override
	public String save(UserRole entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(UserRole entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		UserRole entity = this.load(id);
		this.getCurrentSession().delete(entity);
		this.flush();
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUsersByRoleId(String roleId) {
		//获取人员时过滤掉已经被禁用的人员
		List<User> userList = this.getCurrentSession().createQuery(
						"select t.user from UserRole t where t.role.id = :roleId and t.user.status ='NORMAL'").setString("roleId", roleId).setCacheable(true).list();
		return userList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUsersByRoleIdAndCode(String roleId,String Area_code) {
		//查询人员的时候需要过滤掉被禁用的人员；
	/*	String[] areas = null;
		if(Area_code!=null&&!Area_code.equals("")){
			areas=Area_code.split(",");
		}
		List<User> userList=null;
		if(areas.length>0){
			userList = this.getCurrentSession().createQuery(
					"select t.user from UserRole t where t.role.id = :roleId and t.user.status ='NORMAL' and t.user.areaCode in( :areaCode)").setString("roleId", roleId).setParameterList("areaCode", areas).list();
		}else{
			userList = this.getCurrentSession().createQuery(
					"select t.user from UserRole t where t.role.id = :roleId and t.user.status ='NORMAL' and t.user.areaCode in( :areaCode)").setString("roleId", roleId).setString("areaCode", Area_code).list();
		}
		*/
		List<User> userList = this.getCurrentSession().createQuery(
					"select t.user from UserRole t where t.role.id = :roleId and t.user.status ='NORMAL' and t.user.areaCode =trim(:areaCode)").setString("roleId", roleId).setString("areaCode", Area_code).list();
		return userList;
	}
	
	@Override
	public List<Role> findAllRoleByUserId(String userid) {
		List<Role> roles=this.getCurrentSession().createQuery("select t.role from UserRole t where t.user.id= :userid ").setString("userid",userid).list();
		return roles;
	}

}
