package com.supermap.intelligent.dao;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author duanyf
 */

@Transactional
@Repository("baseCommonDaoMrpc")
public class CommonDaoMRPC {

    @Autowired
    private SessionFactory sessionFactoryMRPC;

    public Session getCurrentSession() {
        return this.sessionFactoryMRPC.getCurrentSession();
    }

    public Session OpenSession() {
        return this.sessionFactoryMRPC.openSession();
    }


    /**
     * 暂时只支持视图和单表字符串字段的的自定义查询
     *
     * @param FieldsName
     *            String 查询字段，逗号隔开（A,B,C,D,...）
     * @param ViewOrTableName
     *            String 数据库表或视图名称
     * @param NoWhereSql
     *            String 不带where字段的Sql关系语句
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map> getDataList(String FieldsName, String ViewOrTableName,
                                 String NoWhereSql) {
        StringBuilder Sql = new StringBuilder();
        String[] _fieldsName = FieldsName.split(",");
        List<Map> mapList = new ArrayList<Map>();
        Sql.append("select ");
        Sql.append(FieldsName);
        Sql.append(" from ");
        Sql.append(ViewOrTableName);
        Sql.append(" where ");
        Sql.append(NoWhereSql);
        Session session=this.OpenSession();
        SQLQuery q = session.createSQLQuery(Sql.toString());

        // addScalar("PRODEF_CODE",StandardBasicTypes.STRING)
        // for (String key : map.keySet())
        // {
        // if(map.get(key).equals(""))
        // q.addScalar(key,StandardBasicTypes.STRING);
        // }
        for (int i = 0; i < _fieldsName.length; i++) {
            q.addScalar(_fieldsName[i].trim().toUpperCase(),
                    StandardBasicTypes.STRING);
        }
        mapList = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        session.close();
        return mapList;
    }

    /**
     * 根据hql条件语句(where后面的部分)查询数据，不分页，非参数化，慎用
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:11:35
     * @param t
     * @param hqlCondition
     *            hql条件语句
     * @return
     */
    public <T> List<T> getDataList(Class<T> t, String hqlCondition) {
        String className = t.getName();
        String str = " from " + className + " where " + hqlCondition;
        System.out.println(str);
        @SuppressWarnings("unchecked")
        List<T> contList = this.getCurrentSession().createQuery(str).setCacheable(true).list();

        return contList;
    }

    /**
     * 根据hql条件语句(where后面的部分)查询数据，不分页，参数化查询
     * @Title: getDataList
     * @author:liushufeng
     * @date：2016年1月14日 上午12:13:44
     * @param t
     *            实体类
     * @param hqlCondition
     *            查询条件
     * @param map
     *            参数map
     * @return
     */
    public <T> List<T> getDataList(Class<T> t, String hqlCondition, Map<String, String> map) {
        String className = t.getName();
        String str = " from " + className + " where " + hqlCondition;
        System.out.println(str);
        Query query = this.getCurrentSession().createQuery(str).setCacheable(true);
        query.setProperties(map);
        @SuppressWarnings("unchecked")
        List<T> contList = query.list();
        return contList;
    }

    /**
     * 更新实体
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:11:27
     * @param t
     */
    public <T> void update(T t) {
        this.getCurrentSession().merge(t);
    }
    
    /**
     * 提交事务
     *
     * @作者 李诺
     * @创建时间 2019年10月18日上午10:11:27
     * @param t
     */
    public void commit() {
        this.getCurrentSession().getTransaction().commit();
    }

    /**
     * 根据ID查询实体
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:11:06
     * @param entityClass
     *            实体类
     * @param id
     *            主键值
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> entityClass, String id) {
        return (T) this.getCurrentSession().get(entityClass, id);
    }

    /**根据Integer型ID查询实体
     * @param entityClass
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> entityClass, Integer id) {
        return (T) this.getCurrentSession().get(entityClass, id);
    }

    /**根据Long型ID查询实体
     * @param entityClass
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> entityClass, Long id) {
        return (T) this.getCurrentSession().get(entityClass, id);
    }

    /**
     * 保存实体
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:12:51
     * @param t
     * @return
     */
    public <T> String save(T t) {
        this.getCurrentSession().merge(t);
        return null;
    }

    /**
     * 保存或更新一个实体，调用的是hibernate的saveOrUpdate方法
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:10:38
     * @param t
     */
    public <T> void saveOrUpdate(T t) {
        this.getCurrentSession().saveOrUpdate(t);
    }

    /**
     * 删除一个实体
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:10:12
     * @param t
     *            泛型类实体对象
     */
    public <T> void deleteEntity(T t) {
        getCurrentSession().delete(t);
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
        if (entity != null) {
            this.getCurrentSession().delete(entity);
        }
    }

    /**
     * 根据hql条件删除实体，非参数化
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:08:12
     * @param t
     *            泛型类
     * @param condition
     *            hql条件语句
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> int deleteEntitysByHql(Class<T> t, String condition) {
        Session session = this.getCurrentSession();
        String className = t.getName();
        Query qry = session.createQuery(" from  " + className + " where " + condition);
        List<T> list = qry.list();
        for (int i = 0; i < list.size(); i++) {
            this.deleteEntity(list.get(i));
        }
        int count = list.size();
        return count;
    }

    /**
     * 根据HQL条件语句(where后面的部分)分页查询数据，不用参数化查询
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:05:00
     * @param t
     *            泛型类
     * @param hqlCondition
     *            where后面的条件语句
     * @param pageIndex
     *            当前页数
     * @param pageSize
     *            每页记录数
     * @return
     */
    public <T> Page getPageDataByHql(Class<T> t, String hqlCondition, int pageIndex, int pageSize) {
        String className = t.getName();
        int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        String str = " from " + className + " where ";
        String fullHQLQuery = str + hqlCondition;
        Query queryCount = this.getCurrentSession().createQuery("SELECT COUNT(1) " + fullHQLQuery);
        Long count = Long.parseLong(queryCount.uniqueResult().toString());
        if (count < 1)
            return new Page();
        Query query = this.getCurrentSession().createQuery(fullHQLQuery);
        query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        if (pageSize > 0)
            query.setMaxResults(pageSize);
        return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, query.list());
    }

    /**
     * 根据完整的sql语句分页查询数据f返回Page
     *（用于筛选重复项）
     * @作者 段怡凡
     * @创建时间 2017年12月22日上午10:00:26
     * @param fulSql
     *            完整的 sql查询语句，可以跨多个表，非参数化查询，应慎用，防止SQL注入
     * @param pageIndex
     *            当前页
     * @param pageSize
     *            每页记录数
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> Page getPageByFullSql(String fulSql, int pageIndex, int pageSize) {
        String fullSQLQuery = " from ("+fulSql+")";
        Query queryCount = this.getCurrentSession().createSQLQuery("SELECT COUNT(1) " + fullSQLQuery);
        Long count = Long.parseLong(queryCount.uniqueResult().toString());
        if (count < 1)
            return new Page();
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        pageSize = pageSize < 0 ? 10 : pageSize;
        query.setMaxResults(pageSize);
        return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, query.list());
    }



    /**
     * 根据hql条件语句分页查询数据
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午9:49:34
     * @param t
     *            泛型类
     * @param hqlCondition
     *            hql语句中where后面的部分，应写成参数化的格式,例如等于查询：ZL=:ZLPARAM,其中ZLPARAM是参数，
     *            应在map中存在键为ZLPARAM的值
     * @param map
     *            参数键值对，每个出现在hqlCondition中的参数都必须能在map中找到相应的键值对
     * @param pageIndex
     *            当前页数
     * @param pageSize
     *            每页记录数
     * @return
     */
    public <T> Page getPageDataByHql(Class<T> t, String hqlCondition, Map<String, String> map, int pageIndex, int pageSize) {
        String className = t.getName();
        int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        String str = " from " + className + " where ";
        String fullHQLQuery = str + hqlCondition;
        Query queryCount = this.getCurrentSession().createQuery("SELECT COUNT(1) " + fullHQLQuery);
        queryCount.setProperties(map);
        Long count = Long.parseLong(queryCount.uniqueResult().toString());
        if (count < 1)
            return new Page();
        Query query = this.getCurrentSession().createQuery(fullHQLQuery);
        query.setProperties(map);
        query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        if (pageSize > 0)
            query.setMaxResults(pageSize);
        return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, query.list());
    }


    /**
     * 根据完整的sql语句分页查询数据
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:00:26
     * @param fulSql
     *            完整的 sql查询语句，可以跨多个表，应使用参数化查询例如：select * from BDCS_ZS_GZ WHERE
     *            ZSBH LIKE:ZSBHPARAM
     * @param paramMap
     *            参数名-参数值 映射
     * @param pageIndex
     *            当前页
     * @param pageSize
     *            每页记录数
     * @return 返回值是键值对List，每个map对应一行记录，获取某列数据使用 map.get("列名");
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getPageDataByFullSql(String fulSql, Map<String, String> paramMap, int pageIndex, int pageSize) {
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setProperties(paramMap);
        int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        pageSize = pageSize < 0 ? 10 : pageSize;
        query.setMaxResults(pageSize);
        // query.setFirstResult(firstResult).setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Map> list = query.list();
        return list;
    }

    /**
     * 根据完整的sql语句分页查询数据
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日上午10:00:26
     * @param fulSql
     *            完整的 sql查询语句，可以跨多个表，非参数化查询，应慎用，防止SQL注入
     *            参数名-参数值 映射
     * @param pageIndex
     *            当前页
     * @param pageSize
     *            每页记录数
     * @return 返回值是键值对List，每个map对应一行记录，获取某列数据使用 map.get("列名");
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getPageDataByFullSql(String fulSql, int pageIndex, int pageSize) {
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        pageSize = pageSize < 0 ? 10 : pageSize;
        query.setMaxResults(pageSize);
        // query.setFirstResult(firstResult).setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Map> list = query.list();
        return list;
    }

    /*
     * 数据上报查询分页
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getPageDataByFullSqlsjsb(String fulSql, int pageIndex, int pageSize) {
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        //int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        //query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        // pageSize = pageSize < 0 ? 10 : pageSize;
        // query.setMaxResults(pageSize);
        // query.setFirstResult(firstResult).setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Map> list = query.list();

        return list;
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
        System.out.println("=========================");

        System.out.println(fullSql);
        System.out.println("=========================");
        SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
        String strCount = query.uniqueResult().toString();
        Long longcount = Long.valueOf(strCount);
        return (long) longcount;
    }

    /**
     * 查询总数count，参数化不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.XX=B.xxx
     * WHERE XXXXX XXXXXXX
     *
     * @作者 刘树峰
     * @创建时间 2015年6月11日下午9:33:51
     * @param fromSql
     * @return
     */
    public long getCountByFullSql(String fromSql, Map<String, String> map) {
        String fullSql = "select count(1) " + fromSql;

        SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
        query.setProperties(map);
        String strCount = query.uniqueResult().toString();
        Long longcount = Long.valueOf(strCount);
        return (long) longcount;
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

    public List<Map> getDataListByFullSqlObject(String fulSql, Map<String, Object> paramMap) {
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
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getDataListByFullSql(String fulSql) {
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        @SuppressWarnings("unchecked")

        List<Map> list = query.list();
        return list;
    }


    public void flush() {
        this.getCurrentSession().flush();
    }

    /**
     * 根据完整sql删除
     * @param delsql
     * @author diaoliwei
     * @return
     */
    public int updateBySql(String delsql){
        int result = 0;
        result = this.getCurrentSession().createSQLQuery(delsql).executeUpdate();
        this.getCurrentSession().flush();
        this.getCurrentSession().clear();
        return result;
    }

    public  ResultSet excuteQuery(String sql)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 创建该连接下的PreparedStatement对象
        pstmt = SessionFactoryUtils.getDataSource(this.sessionFactoryMRPC).getConnection().prepareStatement(sql);
        // 传递第一个参数值 root，代替第一个问号
        //pstmt.setString(1, "root");
        // 执行查询语句，将数据保存到ResultSet对象中
        rs = pstmt.executeQuery();
        if(rs != null){
            rs.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }

        return rs;
    }
    /**
     * 该方法专门用在上传宗地图分层分户图的
     * WHERE XXXXX XXXXXXX
     * @param  sql 更新的SQL语句
     * @param  fis 二进制流
     */
    public  ResultSet updateBinarys(String sql, InputStream fis, InputStream fis2)
            throws SQLException, IOException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 创建该连接下的PreparedStatement对象
        pstmt = SessionFactoryUtils.getDataSource(this.sessionFactoryMRPC).getConnection().prepareStatement(sql);
        // 传递第一个参数值 root，代替第一个问号
        pstmt.setBinaryStream(1,fis,fis.available());
        pstmt.setBinaryStream(2,fis2,fis2.available());
        // 执行查询语句，将数据保存到ResultSet对象中
        rs = pstmt.executeQuery();
        // if(rs != null){
        // rs.close();
        // }
//		if (pstmt != null) {
//			pstmt.close();
//		}

        return rs;
    }

    /** 保存二进制流
     * @param sql
     * @param fis
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public  ResultSet updateBinary(String sql, InputStream fis) throws SQLException, IOException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 创建该连接下的PreparedStatement对象
        pstmt = SessionFactoryUtils.getDataSource(this.sessionFactoryMRPC).getConnection().prepareStatement(sql);
        // 传递第一个参数值 root，代替第一个问号
        pstmt.setBinaryStream(1,fis,fis.available());
        // 执行查询语句，将数据保存到ResultSet对象中
        rs = pstmt.executeQuery();
        return rs;
    }

    /**
     * 查询总数count，参数化不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.XX=B.xxx
     * WHERE XXXXX XXXXXXX
     *
     * @作者 俞学斌
     * @创建时间 2016年5月5日 8:49:51
     * @param fromSql
     * @return
     */
    public long getCountByFullSqlEx(String fromSql, HashMap<String,Object> map) {
        String fullSql = "select count(1) " + fromSql;
        SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);
        query.setProperties(map);
        String strCount = query.uniqueResult().toString();
        Long longcount = Long.valueOf(strCount);
        return (long) longcount;
    }

    /**
     * 查询总数count，传完整的自定义sql语句
     *
     * @作者 hks
     * @创建时间 2016年7月28日 10:02:51
     * @param fullSql
     * @return
     */
    public long getCountByCFullSql(String fullSql) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(fullSql);

        String strCount = query.uniqueResult().toString();
        Long longcount = Long.valueOf(strCount);
        System.out.println("自然幢总数："+ longcount);
        return (long) longcount;
    }

    /**
     * 根据完整的sql语句分页查询数据
     *
     * @作者 俞学斌
     * @创建时间 2016年5月5日 8:49:51
     * @param fulSql
     *            完整的 sql查询语句，可以跨多个表，应使用参数化查询例如：select * from BDCS_ZS_GZ WHERE
     *            ZSBH LIKE:ZSBHPARAM
     * @param paramMap
     *            参数名-参数值 映射
     * @param pageIndex
     *            当前页
     * @param pageSize
     *            每页记录数
     * @return 返回值是键值对List，每个map对应一行记录，获取某列数据使用 map.get("列名");
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getPageDataByFullSqlEx(String fulSql, Map<String, Object> paramMap, int pageIndex, int pageSize) {
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setProperties(paramMap);
        int firstResult = Page.getStartOfPage(pageIndex, pageSize);
        query.setFirstResult(firstResult < 0 ? 0 : firstResult);
        pageSize = pageSize < 0 ? 10 : pageSize;
        query.setMaxResults(pageSize);
        // query.setFirstResult(firstResult).setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Map> list = query.list();
        return list;
    }

    /**
     * 根据完整SQL语句一次新查询所有数据，参数化查询
     *
     * @作者 刘树峰
     * @创建时间 2016年5月5日 8:49:51
     * @param fulSql
     * @param paramMap
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getDataListByFullSqlEx(String fulSql, Map<String, Object> paramMap) {
        Query query = this.getCurrentSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setProperties(paramMap);
        @SuppressWarnings("unchecked")
        List<Map> list = query.list();
        return list;
    }

    public void  excuteQueryNoResult(String sql)
            throws SQLException {
        this.getCurrentSession().createSQLQuery(sql).executeUpdate();
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

    @SuppressWarnings("rawtypes")
    public void setValue(Map map, Object thisObj) {
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            Object val = map.get(obj);
            setMethod(obj, val, thisObj);
        }
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setMethod(Object method, Object value, Object thisObj) {
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            String met = (String) method;
            met = met.trim();
            if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase())) {
                met = met.toUpperCase();
            }
            if (!String.valueOf(method).startsWith("set")) {
                met = "set" + met;
            }
            Class types[] = getMethodParamTypes(thisObj, met);
            if (types != null && types.length > 0) {
                Method m = c.getMethod(met, types);
                if (types[0].getName().contains("String")) {
                    String strValue = StringHelper.formatObject(value);
                    m.invoke(thisObj, strValue);
                } else if (types[0].getName().contains("Double")) {

                    Double doubleValue = 0.0;
                    if (!StringHelper.isEmpty(value)) {
                        doubleValue = StringHelper.getDouble(value);
                    }
                    m.invoke(thisObj, doubleValue);
                } else if (types[0].getName().contains("Date")) {
                    Object obj = null;
                    if (!StringHelper.isEmpty(value)) {
                        m.invoke(thisObj, StringHelper.FormatByDate(value));
                    } else {
                        m.invoke(thisObj, obj);
                    }
                } else if (types[0].getName().contains("Integer")) {
                    int intValue = 0;
                    if (!StringHelper.isEmpty(value)) {
                        intValue = (int) StringHelper.getDouble(value);
                    }
                    m.invoke(thisObj, intValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public Class[] getMethodParamTypes(Object classInstance, String methodName) throws ClassNotFoundException {
        Class[] paramTypes = null;
        Method[] methods = classInstance.getClass().getMethods();// 全部方法
        for (int i = 0; i < methods.length; i++) {
            if (methodName.equals(methods[i].getName())) {// 和传入方法名匹配
                Class[] params = methods[i].getParameterTypes();
                paramTypes = new Class[params.length];
                for (int j = 0; j < params.length; j++) {
                    paramTypes[j] = Class.forName(params[j].getName());
                }
                break;
            }
        }
        return paramTypes;
    }
}
