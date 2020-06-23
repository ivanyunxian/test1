package com.supermap.wisdombusiness.synchroinline.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.supermap.wisdombusiness.core.SuperSpringContext;

/**
 * 定时器使用的dao对象，用完之后要执行close释放连接，切记！！！
 * 
 * @author Administrator
 *
 */
public class TimerDao
{

	private Session session = null;
	
	/**
	 * @return the session
	 */
	public Session getSession()
	{
		return session;
	}

	public TimerDao() throws Exception
	{
		SessionFactory sessionFactory = (SessionFactory)SuperSpringContext.getContext().getBean("sessionFactory",new Object());
		session = sessionFactory.openSession();
		if (session == null)
			throw new Exception("打开Session失败。");
	}

	/**
	 * 根据SQL查询条件获取该类的所有实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param Sql
	 *            String sql语句（SELECT * FROM TABLENAME WHERE ...）
	 * @return List<T> 实体对象List
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataList(Class<T> t, String Sql)
	{
		Query q = this.session.createSQLQuery(Sql).addEntity(t);
		List<T> list = new ArrayList<T>();
		list = q.list();
		return list;
	}

	/**
	 * 根据完整SQL语句一次性查询所有数据，非参数化查询
	 * 
	 * @作者 纪海祥
	 * @创建时间 2016年12月30日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql)
	{
		Query query = this.session.createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findList(Class<T> t, String nowhereSql)
	{
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" from ");
		sqlBuilder.append(t.getName());
		sqlBuilder.append(" where ");
		sqlBuilder.append(nowhereSql);
		List<T> list = this.session.createQuery(sqlBuilder.toString()).list();
		return list;
	}

	public <T> void persist(T entity)
	{
		this.session.persist(entity);
	}

	public <T> String save(T entity)
	{
		return (String) this.session.save(entity);
	}

	public <T> void saveOrUpdate(T entity)
	{
		this.session.saveOrUpdate(entity);
	}

	public <T> void update(T entity)
	{
		if (entity != null)
			this.session.update(entity);
	}

	public void commit()
	{
		this.session.getTransaction().commit();
	}

	public void close()
	{
		this.session.close();
	}

	public void flush()
	{
		this.session.flush();
	}

	public void clear()
	{
		this.session.clear();
	}
}
