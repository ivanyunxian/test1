package com.supermap.wisdombusiness.synchroinline.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("synchroDao")
public class synchroDao {
	
	@Autowired
	private  SessionFactory sessionFactory;
	synchronized  private  Session getCurrentSession() {
		   Session s = null;
		   try{
			   s=this.sessionFactory.getCurrentSession();
		   }catch(Exception e){
			   s= this.sessionFactory.openSession();
		   }
		   return s;
	}
	/**
	 * 根据SQL查询条件获取该类的所有实体
	 * @param t
	 *            Class<T> 类的class属性
	 * @param Sql
	 *            String sql语句（SELECT * FROM TABLENAME WHERE ...）
	 * @return List<T> 实体对象List
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataList(Class<T> t, String Sql) {
		Query q = this.getCurrentSession().createSQLQuery(Sql).addEntity(t);
		List<T> list = new ArrayList<T>();
		list = q.list();
		return list;
	}
	
	/**
	 * 根据完整SQL语句一次性查询所有数据，非参数化查询
	 * @作者 纪海祥
	 * @创建时间 2016年12月30日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findList(Class<T> t, String nowhereSql) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" from ");
		sqlBuilder.append(t.getName());
		sqlBuilder.append(" where ");
		sqlBuilder.append(nowhereSql);
		List<T> list = this.getCurrentSession().createQuery(sqlBuilder.toString()).list();
		return list;
	}
	
	public <T> void persist(T entity) {
		this.getCurrentSession().persist(entity);
	}
	public <T> String save(T entity) {
		return (String) this.getCurrentSession().save(entity);
	}
	public <T> void saveOrUpdate(T entity) {
		this.getCurrentSession().saveOrUpdate(entity);
	}
	public <T> void update(T entity) {
		if (entity != null)
			this.getCurrentSession().update(entity);
	}
	public void commit() {
		this.getCurrentSession().getTransaction().commit();
	}
	public void close() {
		this.getCurrentSession().close();
	}
	public void flush() {
		this.getCurrentSession().flush();
	}
	public void clear() {
		this.getCurrentSession().clear();
	}
}
