package com.supermap.wisdombusiness.workflow.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.supermap.wisdombusiness.synchroinline.util.ThreadLocal;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;
import com.supermap.wisdombusiness.workflow.util.Page;

/**
 * @author 李想
 * @action 一个通用的Hibernate的dao
 *
 */
@Repository("commonDao")
public class CommonDao {

	public static int icount = 0;
	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
	   Session s = null;
	   try{
		   s=this.sessionFactory.getCurrentSession();
	   }catch(Exception e){
		   s= this.sessionFactory.openSession();
	   }
	   return s;
}

	/**
	 * 根据主键加载一个实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param id
	 *            String 实体内码
	 * @return T 实体对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T load(Class<T> t, String id) {
		return (T) this.getCurrentSession().get(t, id);
	}

	/**
	 * 根据主键获取一个实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param id
	 *            String 实体内码
	 * @return T 实体对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> t, String id) {
		return (T) this.getCurrentSession().get(t, id);
	}

	/**
	 * 获取所有实体对象
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @return List<T> 实体对象List
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> t) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" from ");
		sqlBuilder.append(t.getName());
		List<T> list = this.getCurrentSession().createQuery(sqlBuilder.toString()).list();
		return list;
	}

	/**
	 * 根据类名和where的查询条件获取该类的所有实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param nowhereSql
	 *            String 没有where语句的hql
	 * @return List<T> 实体对象List
	 */
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

	/**
	 * 根据类名、表名和where的查询条件获取该类的所有实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param tableName
	 *            String 数据库表名
	 * @param nowhereSql
	 *            String 没有where语句的sql
	 * @return List<T> 实体对象List
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataList(Class<T> t, String tableName, String noWhereSql) {
		StringBuilder conditionSql = new StringBuilder();
		conditionSql.append("select * from ");
		conditionSql.append(tableName);
		conditionSql.append(" where ");
		conditionSql.append(noWhereSql);
		Query q = this.getCurrentSession().createSQLQuery(conditionSql.toString()).addEntity(t);
		List<T> list = new ArrayList<T>();
		list = q.list();
		return list;
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
	public <T> List<T> getDataList(Class<T> t, String Sql) {
		Query q = this.getCurrentSession().createSQLQuery(Sql).addEntity(t);
		List<T> list = new ArrayList<T>();
		list = q.list();
		return list;
	}

	public <T> void persist(T entity) {
		this.getCurrentSession().persist(entity);
	}

	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 *            T
	 * @return
	 */
	public <T> String save(T entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	/**
	 * 保存或更新实体对象
	 * 
	 * @param entity
	 *            T
	 * @return
	 */
	public <T> void saveOrUpdate(T entity) {
		// this.getCurrentSession().clear();
		this.getCurrentSession().saveOrUpdate(entity);
	}

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            T
	 * @return
	 */
	public <T> void update(T entity) {
		if (entity != null)
			this.getCurrentSession().update(entity);
	}

	/**
	 * 根据主键删除一个实体
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param id
	 *            String 实体内码
	 * @return
	 */
	public <T> void delete(Class<T> t, String id) {
		T entity = this.get(t, id);
		if (entity != null)
			// this.getCurrentSession().clear();
			this.getCurrentSession().delete(entity);
	}

	public <T> void delete(T entity) {
		// this.getCurrentSession().clear();
		this.getCurrentSession().delete(entity);
	}

	public void flush() {
		this.getCurrentSession().flush();
		// this.getCurrentSession().close();
	}

	public void clear() {
		this.getCurrentSession().clear();
	}

	public <T> void merge(T entity) {
		this.getCurrentSession().merge(entity);
		;
	}

	/**
	 * 执行删除sql语句
	 * 
	 * @param Sql
	 *            String SQL语句（delete from tablename where ...）
	 * @return
	 */
	public int deleteQuery(String Sql) {
		return this.getCurrentSession().createSQLQuery(Sql).executeUpdate();
	}

	/**
	 * HQL分页函数
	 * 
	 * @param t
	 *            Class<T> 类的class属性
	 * @param pageIndex
	 *            int 开始页索引
	 * @param pageIndex
	 *            int 每页数据数量
	 * @param noWhereHql
	 *            String 没有where条件Hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page GetPagedData(Class<T> t, int pageIndex, int pageSize, String noWhereHql) {
		StringBuilder _hql = new StringBuilder();
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		_hql.append(" from ");
		_hql.append(t.getName());
		_hql.append(" where ");
		_hql.append(noWhereHql);
		List<T> contList = this.getCurrentSession().createQuery(_hql.toString()).list();
		if (contList.size() < 1)
			return new Page();
		Query query = this.getCurrentSession().createQuery(_hql.toString());
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize), contList.size(), pageSize, query.list());
	}

	/**
	 * SQL分页函数
	 * 
	 * @param TableName
	 *            String 数据库表名
	 * @param pageIndex
	 *            int 开始页索引
	 * @param pageSize
	 *            int 每页数据数量
	 * @param noWhereSql
	 *            String 没有where条件Sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page GetPagedData(Class<T> t, String TableName, int pageIndex, int pageSize, String noWhereSql) {
		StringBuilder _Sql = new StringBuilder();
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		_Sql.append("select * from ");
		_Sql.append(TableName);
		_Sql.append(" where ");
		_Sql.append(noWhereSql);
		List<T> contList = this.getCurrentSession().createSQLQuery(_Sql.toString()).addEntity(t).list();
		if (contList.size() < 1)
			return new Page();
		Query query = this.getCurrentSession().createSQLQuery(_Sql.toString()).addEntity(t);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return new Page(Page.getStartOfPage(pageIndex, pageSize), contList.size(), pageSize, query.list());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> GetPagedListData(Class<T> t, String TableName, int pageIndex, int pageSize, String noWhereSql) {
		StringBuilder _Sql = new StringBuilder();
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		_Sql.append("select * from ");
		_Sql.append(TableName);
		_Sql.append(" where ");
		_Sql.append(noWhereSql);
		List<T> contList = this.getCurrentSession().createSQLQuery(_Sql.toString()).addEntity(t).list();
		if (contList.size() < 1)
			return contList;
		Query query = this.getCurrentSession().createSQLQuery(_Sql.toString()).addEntity(t);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return query.list();
	}

	/**
	 * 暂时只支持视图和单表字符串字段的的自定义查询
	 * 
	 * @param FieldsName
	 *            String 查询字段，逗号隔开（A,B,C,D,...）
	 * @param ViewOrTableName
	 *            String 数据库表或视图名称
	 * @param noWhereSql
	 *            String 不带where字段的Sql关系语句
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getDataList(String FieldsName, String ViewOrTableName, String NoWhereSql) {
		StringBuilder Sql = new StringBuilder();
		String[] _fieldsName = FieldsName.split(",");
		List<Map> mapList = new ArrayList<Map>();
		Sql.append("select ");
		Sql.append(FieldsName);
		Sql.append(" from ");
		Sql.append(ViewOrTableName);
		Sql.append(" where ");
		Sql.append(NoWhereSql);

		SQLQuery q = this.getCurrentSession().createSQLQuery(Sql.toString());
		// addScalar("PRODEF_CODE",StandardBasicTypes.STRING)
		// for (String key : map.keySet())
		// {
		// if(map.get(key).equals(""))
		// q.addScalar(key,StandardBasicTypes.STRING);
		// }
		for (int i = 0; i < _fieldsName.length; i++) {
			q.addScalar(_fieldsName[i].trim().toUpperCase(), StandardBasicTypes.STRING);
		}
		mapList = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return mapList;
	}
	public  int excuteQuery(String sql) {
		int result = 0;
		result = this.getCurrentSession().createSQLQuery(sql).executeUpdate();
		this.getCurrentSession().flush();
		this.getCurrentSession().clear();
		return result;
	}
	/**
	 * 调用ORACLE函数的方法
	 * 
	 * @para FuncString String ORACLE函数
	 * @return String 函数返回值 暂时只支持字符串
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String ExecuteOraFunc(String FuncString) {
		StringBuilder sql = new StringBuilder();
		List<Map> mapList = new ArrayList<Map>();
		sql.append("select ");
		sql.append(FuncString);
		sql.append(" as FUNCVALUE from dual ");
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql.toString());
		q.addScalar("FUNCVALUE", StandardBasicTypes.STRING);
		mapList = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return mapList.get(0).get("FUNCVALUE").toString();
	}

	/**
	 * 调用存储过程
	 */
	public Boolean ExecuteOraProc(String ProcString, ArrayList<String> paralist) {
		StringBuilder str = new StringBuilder();
		str.append("{ Call ");
		str.append(ProcString);
		str.append("(");
		for (int i = 0; i < paralist.size(); i++) {
			if (i == 0) {
				str.append("?");
			} else {
				str.append(",?");
			}
		}
		str.append(") }");
		CallableStatement statement;
		try {
			statement = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection().prepareCall(str.toString());
			for (int i = 0; i < paralist.size(); i++) {
				String value = paralist.get(i);
				statement.setString(i + 1, value);
			}
			// ResultSet rs =statement.executeQuery();
			// rs.getString(1);
			return statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String CreatFile_Number(String Dist_ID, String BH) {

		final String dq = Dist_ID;
		final String bh = BH;
		Session session = this.getCurrentSession();

		String filenumber = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				String q = dq;
				String h = bh;

				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append(Common.WORKFLOWDB);
				str.append("GETFILE_NUMBER");
				str.append("(");
				str.append("?,?,?,?");
				str.append(") }");
				String filrnumberString = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				Integer year = Integer.parseInt(DateUtil.getYear());
				statement.setInt(1, year);
				statement.setString(2, q);
				statement.setString(3, h);
				statement.registerOutParameter(4, Types.VARCHAR);
				statement.execute();
				filrnumberString = statement.getString(4);
				statement.close();
				return filrnumberString;
			}
		});
		return filenumber;
	}
	public String CreatMaxID(String Dist_ID, String BH,String MType) {

		final String dq = Dist_ID;
		final String bh = BH;
		final String mtype = MType;
		Session session = this.getCurrentSession();

		String maxid = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				String q = dq;
				String h = bh;
                String t=mtype;
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append(Common.WORKFLOWDB);
				str.append("PROC_GETMAXID");
				str.append("(");
				str.append("?,?,?,?,?");
				str.append(") }");
				String MaxidString = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				Integer year = Integer.parseInt(DateUtil.getYear());
				statement.setInt(1, year);
				statement.setString(2, q);
				statement.setString(3, h);
				statement.setString(4, t);
				statement.registerOutParameter(5, Types.VARCHAR);
				statement.execute();
				MaxidString = statement.getString(5);
				statement.close();
				return MaxidString;
			}
		});
		return maxid;
	}
    //维护老数据增加流水号
	public String CreatMaxID(String realYear,String Dist_ID, String BH,String MType) {
		final String dq = Dist_ID;
		final String bh = BH;
		final String mtype = MType;
		final String realy= realYear;
		Session session = this.getCurrentSession();
		String maxid = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				String q = dq;
				String h = bh;
                String t=mtype;
                String ry = realy;
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append(Common.WORKFLOWDB);
				str.append("PROC_GETMAXID");
				str.append("(");
				str.append("?,?,?,?,?");
				str.append(") }");
				String MaxidString = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				Integer year = Integer.parseInt(ry);
				statement.setInt(1, year);
				statement.setString(2, q);
				statement.setString(3, h);
				statement.setString(4, t);
				statement.registerOutParameter(5, Types.VARCHAR);
				statement.execute();
				MaxidString = statement.getString(5);
				statement.close();
				return MaxidString;
			}
		});
		return maxid;
	}
	/**
	 * 根据完整SQL语句一次新查询所有数据，参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql, Map<String, String> paramMap) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setProperties(paramMap);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	/**
	 * 根据完整SQL语句一次新查询所有数据，非参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
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

	/**
	 * 根据完整SQL语句一次新查询所有数据，非参数化查询，分页
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月15日下午1:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getDataListByFullSql(String fulSql, int pageIndex, int pageSize) {
		int firstResult = (pageIndex - 1) * pageSize;
		/*List<Map> list = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() < 1)
			return list;*/
		Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0)
			query.setMaxResults(pageSize);
		return query.list();
	}

	/**
	 * 查询总数count，不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.xx=B.xxx WHERE
	 * 
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日下午9:33:51
	 * @param fromSql
	 * @return
	 */
	public long getCountByFullSql(String fromSql) {
		String fullSql = "select count(1) " + fromSql;
		SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
		String strCount = query.uniqueResult().toString();
		Long longcount = Long.valueOf(strCount);
		return (long) longcount;
	}
	/**
	 * 
	 * 为了优化资料移交查询
	 * @作者 纪海想
	 * @创建时间 2016年7月7日下午16:23:51
	 * @param fullsql
	 * @return
	 */
	public long getCountByFullSqlCustom(String fullsql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(fullsql);
		String strCount = query.uniqueResult().toString();
		Long longcount = Long.valueOf(strCount);
		return (long) longcount;
	}

	public void commit() {
		this.getCurrentSession().getTransaction().commit();
		
	}

	public void close() {
		this.getCurrentSession().close();
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> getPageDataByFullSql(String fulSql, int pageIndex,
			int pageSize) {
		Query query = this.getCurrentSession().createSQLQuery(fulSql)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		pageSize = pageSize < 0 ? 10 : pageSize;
		query.setMaxResults(pageSize);
		// query.setFirstResult(firstResult).setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
	
	
	public String SkillSession() {
		 
		Session session = this.getCurrentSession();
		String num_of_kills = session.doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
			 
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append("BDC_DAK.");
				str.append("DB_KILL_IDLE_CLIENTS");
				str.append(" }");
				String num_of_kills = "";
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				statement.execute();
				statement.close();
				return num_of_kills;
			}
		});
		return num_of_kills;
	}

	public int updateBySql(String delsql) {
		int result = 0;
		result = this.getCurrentSession().createSQLQuery(delsql).executeUpdate();
		this.getCurrentSession().flush();
		this.getCurrentSession().clear();
		return result;
	}

}
