package com.supermap.wisdombusiness.framework.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.wisdombusiness.synchroinline.model.DatuminstInfo;
import com.supermap.wisdombusiness.synchroinline.model.DicInfo;
import com.supermap.wisdombusiness.synchroinline.model.DicInfo.DicItem;
import com.supermap.wisdombusiness.synchroinline.model.Pro_attachment;
import com.supermap.wisdombusiness.synchroinline.model.Pro_datuminst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_fwxx;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proinst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proposerinfo;
import com.supermap.wisdombusiness.synchroinline.model.Pro_slxmsh;
import com.supermap.wisdombusiness.synchroinline.model.SqrInfo;
import com.supermap.wisdombusiness.synchroinline.util.FTPUtils;
import com.supermap.wisdombusiness.web.ui.Page;


@Repository("baseCommonDaoInline")
@Transactional
public class CommonDaoInline {

	@Autowired
	private SessionFactory sessionFactoryInline;

	 Session session;
	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	public Session getCurrentSession() {
//		session=this.sessionFactoryInline.openSession();
		session=this.sessionFactoryInline.getCurrentSession();
		return session;
	}

	public Session getOpenSession() {
		session=this.sessionFactoryInline.openSession();
		return session;
	}

	public void SetSessionFlushMede() {
		Session session =sessionFactoryInline.getCurrentSession();
		if(session.isOpen()==false){
		}
		session.setFlushMode(FlushMode.MANUAL);
	}

	public void SessionFlush() {
		Session session =sessionFactoryInline.getCurrentSession();
		session.flush();
		session.close();
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
		T t = (T) this.getCurrentSession().get(entityClass, id);

		return t;
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
		this.getCurrentSession().save(t);

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
		this.getCurrentSession().delete(t);

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
	 * @param condition
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
	 * @param paramMap
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
		pstmt = SessionFactoryUtils.getDataSource(this.sessionFactoryInline).getConnection().prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		rs = pstmt.executeQuery();

		return rs;
	}

	public void  excuteQueryNoResult(String sql)
			throws SQLException {
		this.getCurrentSession().createSQLQuery(sql).executeUpdate();
	}


	public DicInfo getDicInfo(String dic_key)
	{
		DicInfo dic = new DicInfo();
		Map<String, String> map = ConstHelper.getDictionary(dic_key);
		if (map != null)
		{
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				DicItem item = new DicItem();
				item.setCode(entry.getKey());
				item.setText(entry.getValue());
				dic.getItems().add(item);
			}
		}
		return dic;
	}

	/**
	 * 根据受理申请id获取受理项目
	 *
	 * @param slsqId
	 *            受理申请id
	 * @return
	 * @throws Exception
	 */
	public Pro_proinst getProinst(String slsqId) throws Exception
	{
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		return (Pro_proinst) this.getCurrentSession().get(Pro_proinst.class, slsqId);
	}

	public Pro_slxmsh getSlxmsh(String slsqId) throws Exception
	{
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		List<Pro_slxmsh> shs = this.getCurrentSession().createQuery("from Pro_slxmsh where slxm_id='" + slsqId + "'").list();
		return shs == null || shs.isEmpty() ? null : shs.get(0);
	}


	/**
	 * 根据受理申请id获取申请人列表
	 *
	 * @param slsqId
	 *            slsqId 受理申请id
	 * @return
	 */
	public List<Pro_proposerinfo> getSqrsBySlsqId(String slsqId)
	{
		return this.getCurrentSession().createQuery("from Pro_proposerinfo where proinst_id='" + slsqId + "' order by sqr_sxh asc").list();
	}

	/**
	 * 通过受理申请id获取附件模板资料
	 *
	 * @param slsqId
	 * @return
	 */
	public List<Pro_datuminst> getDatuminsts(String slsqId)
	{
		return this.getCurrentSession().createQuery("from Pro_datuminst where proinst_id='" + slsqId + "' order by code asc").list();
	}

	/**
	 * 将附件模板实例转换为视图对象，附带当前模板下文件存在数量
	 *
	 * @param datuminsts
	 * @return
	 */
	public List<DatuminstInfo> getByDatuminsts(List<Pro_datuminst> datuminsts)
	{
		List<DatuminstInfo> result = new ArrayList<DatuminstInfo>();
		for (Pro_datuminst datuminst : datuminsts)
		{
			DatuminstInfo info = new DatuminstInfo();
			info.setBz(datuminst.getBz());
			info.setCode(datuminst.getCode());
			info.setCount(datuminst.getCount());
			info.setId(datuminst.getId());
			info.setName(datuminst.getName());
			info.setProinst_id(datuminst.getName());
			info.setRequired(datuminst.getRequired());
			List<Pro_attachment> files = getFilesBydatuminst(datuminst.getId());
			info.setCur_count(files.size());
			result.add(info);
		}
		return result;
	}

	/**
	 * 获取指定资料模板实例的附件列表
	 *
	 * @param datuminst_id
	 *            资料模板实例id
	 * @return
	 */
	public List<Pro_attachment> getFilesBydatuminst(String datuminst_id)
	{
		return this.getCurrentSession().createQuery("from Pro_attachment where datuminst_id='" + datuminst_id + "' order by file_index asc").list();
	}

	/**
	 * 格式化申请人信息，字典类型转换
	 *
	 * @param sqrs
	 * @return
	 */
	public List<SqrInfo> formatSqr(List<Pro_proposerinfo> sqrs)
	{
		List<SqrInfo> new_sqrs = new ArrayList<SqrInfo>();
		for (Pro_proposerinfo sqr : sqrs)
		{
			SqrInfo new_sqr = new SqrInfo();
			new_sqr.setSqr_adress(sqr.getSqr_adress());
			new_sqr.setSqr_gyfs(sqr.getSqr_gyfs());
			new_sqr.setSqr_gyfe(sqr.getSqr_gyfe());
			new_sqr.setSqr_lx(sqr.getSqr_lx());
			new_sqr.setSqr_name(sqr.getSqr_name());
			new_sqr.setSqr_qt_adress(sqr.getSqr_qt_adress());
			new_sqr.setSqr_qt_dlr_name(sqr.getSqr_qt_dlr_name());
			new_sqr.setSqr_qt_fr_name(sqr.getSqr_qt_fr_name());
			new_sqr.setSqr_qt_tel(sqr.getSqr_qt_tel());
			new_sqr.setSqr_qt_zjh(sqr.getSqr_qt_zjh());
			new_sqr.setSqr_qt_zjlx(sqr.getSqr_qt_zjlx());
			new_sqr.setSqr_sxh(sqr.getSqr_sxh());
			new_sqr.setSqr_tel(sqr.getSqr_tel());
			new_sqr.setSqr_zjh(sqr.getSqr_zjh());
			new_sqr.setSqr_zjlx(sqr.getSqr_zjlx());
			// 格式化共有方式
			String gyfs = sqr.getSqr_gyfs();
			String gyfs_name = ConstHelper.getNameByValue("GYFS", gyfs);
			new_sqr.setSqr_gyfs_format(gyfs_name);
			// 格式化证件类型
			String zjlx = sqr.getSqr_zjlx();
			String zjlx_name = ConstHelper.getNameByValue("ZJLX", zjlx);
			new_sqr.setSqr_zjlx_format(zjlx_name);
			// 格式化其他人证件类型
			String qt_zjlx = sqr.getSqr_qt_zjlx();
			String qt_zjlx_name = ConstHelper.getNameByValue("ZJLX", qt_zjlx);
			new_sqr.setSqr_qt_zjlx_format(qt_zjlx_name);
			new_sqrs.add(new_sqr);
		}
		return new_sqrs;
	}

	/**
	 * 通过文件id从ftp下载文件，返回输出流。
	 * (广西区厅不适用FTP)
	 * @param fileId
	 * @throws Exception
	 */
	public void getFileOutputStream(String fileId ,HttpServletResponse response) throws Exception
	{
//		ByteArrayOutputStream outStream = null;
		try
		{
			Pro_attachment file = (Pro_attachment) this.getCurrentSession().get(Pro_attachment.class, fileId);
			String syspath = ConfigHelper.getNameByValue("filepath");
			String fileName = syspath+File.separator + file.getPath() +File.separator + file.getName() + file.getSuffix();
			
			String thisfilename =  file.getName() + file.getSuffix();
			File this_file = new File(fileName);
			byte[] f=FileUtils.readFileToByteArray(this_file); 
			if (thisfilename != null && thisfilename.toLowerCase().indexOf(".pdf") > 0) {
					response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
					response.setContentType("application/pdf; charset=UTF-8");
				} else {
					response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
					response.setContentType("image/jpeg; charset=UTF-8");
				}
			 response.getOutputStream().write(f);
			
//			File this_file = new File(fileName);
//		    FileInputStream in = new FileInputStream(this_file);
//		    outStream = new ByteArrayOutputStream();
//		    byte [] buffer = new byte[1024];
//		    int len = 0;
//		    while( (len = in.read(buffer)) != -1){
//		    	outStream.write(buffer, 0, len);
//		    }
//			outStream = FTPUtils.downloadFile(file.getPath(), fileName);
//			FTPUtils.closeFtp();
		}
		catch (Exception ex)
		{
			FTPUtils.closeFtp();
			throw ex;
		}
//		return outStream;
	}


	/**
	 * 通过文件id获取资料模板对象
	 *
	 * @param fileId
	 * @return
	 */
	public Pro_datuminst getDatuminstByFileId(String fileId)
	{
		Pro_attachment file = (Pro_attachment) this.getCurrentSession().get(Pro_attachment.class, fileId);
		return (Pro_datuminst) this.getCurrentSession().get(Pro_datuminst.class, file.getDatuminst_id());
	}

	/**
     * 通过受理项目编号获取房屋信息
     *
     * @param slsqId
     * @return
     */
    public List<Pro_fwxx> getFwxxBySlsqId(String slsqId) {
    	List<Pro_fwxx> fwxxs = new ArrayList<Pro_fwxx>();
    	if (slsqId != null && !slsqId.isEmpty()) {
    		fwxxs = this.getCurrentSession().createQuery(" from Pro_fwxx where proinst_id='" + slsqId + "' " ).list();
    	}
    	return fwxxs;
    }
    /**
     * 通过受理项目编号获取审核意见信息
     *
     * @param slsqId
     * @return
     */
    public List<Pro_slxmsh> getShxxBySlsqId(String slsqId) {
    	List<Pro_slxmsh> shxxInfo = new ArrayList<Pro_slxmsh>();
    	if (slsqId != null && !slsqId.isEmpty()) {
    		shxxInfo = this.getCurrentSession().createQuery("FROM Pro_slxmsh WHERE SLXM_ID='" + slsqId + "' ORDER BY SH_XMSL_RQ ASC").list();
    	}
    	return shxxInfo;
    }

    public Pro_attachment getFileId(String fileId)
	{
		return (Pro_attachment) this.getCurrentSession().get(Pro_attachment.class, fileId);
	}

    public List<Map> Query(String sql)
	{
		Query query = this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
}
