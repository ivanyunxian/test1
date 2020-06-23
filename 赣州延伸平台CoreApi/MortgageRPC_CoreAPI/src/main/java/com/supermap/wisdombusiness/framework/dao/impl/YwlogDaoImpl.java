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

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.wisdombusiness.framework.dao.YwlogDao;
import com.supermap.wisdombusiness.framework.model.Ywlog;
import com.supermap.wisdombusiness.web.ui.Page;

@Repository("ywlogDao")
public class YwlogDaoImpl implements YwlogDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Ywlog load(String id) {
		return (Ywlog) this.getCurrentSession().load(Ywlog.class, id);
	}

	@Override
	public Ywlog get(String id) {
		return (Ywlog) this.getCurrentSession().get(Ywlog.class, id);
	}

	@Override
	public void persist(Ywlog entity) {
		this.getCurrentSession().persist(entity);
	}

	@Override
	public String save(Ywlog entity) {
		return (String) this.getCurrentSession().save(entity);
	}

	@Override
	public void saveOrUpdate(Ywlog entity) {
		this.getCurrentSession().saveOrUpdate(entity);
		this.flush();
	}

	@Override
	public void delete(String id) {
		Ywlog entity = this.load(id);
		this.getCurrentSession().delete(entity);
	}

	@Override
	public void flush() {
		this.getCurrentSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPagedYwlog(int pageIndex, int pageSize, Map<String, Object> mapCondition) {
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		String count_str = "select count(*)  from Ywlog where 1=1 ";
		String str = " from Ywlog where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		conditionBuilder = this.getMapCondition(mapCondition, conditionBuilder);
		String countStr = count_str + conditionBuilder.toString();
		String sqlStr = str + conditionBuilder.toString();
		long totalCount = (Long)this.getCurrentSession().createQuery(countStr).setCacheable(true).list().get(0);
		if (totalCount < 1){
			return new Page();
		}	
		Query query = this.getCurrentSession().createQuery(sqlStr );
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (pageSize > 0) query.setMaxResults(pageSize);
		List<Ywlog> logList = query.list();
		for (Ywlog ywlog : logList) {
			ywlog.setOperateTimeStr(DateUtil.getDateTime("yyyy-MM-dd HH:mm",ywlog.getOperateTime()));
			ywlog.setIsSuccess(ConstValue.SF.YES.Value.equals(ywlog.getIsSuccess()) ? "成功" : "失败");
		}
		return new Page(Page.getStartOfPage(pageIndex, pageSize), totalCount, pageSize, logList);
	}

	@Override
	public List<Ywlog> findAll() {
		return null;
	}
	
	@Override
	public List<Ywlog> logList(Map<String, Object> mapCondition){
		int firstResult = Page.getStartOfPage(1, 50000);
		String str = " from Ywlog where 1=1 ";
		StringBuilder conditionBuilder = new StringBuilder();
		conditionBuilder = this.getMapCondition(mapCondition, conditionBuilder);
		String sqlStr = str + conditionBuilder.toString();
		Query query = this.getCurrentSession().createQuery(sqlStr );
		query.setFirstResult(firstResult < 0 ? 0 : firstResult);
		if (50000 > 0) query.setMaxResults(50000);
		List<Ywlog> logList = query.list();
		for (Ywlog ywlog : logList) {
			ywlog.setOperateTimeStr(DateUtil.getDateTime("yyyy-MM-dd HH:mm",ywlog.getOperateTime()));
			ywlog.setSuccessText(ConstValue.SF.YES.Value.equals(ywlog.getIsSuccess()) ? "成功" : "失败");
		}
		return logList;
	}
	
	private StringBuilder getMapCondition(Map<String, Object> mapCondition, StringBuilder conditionBuilder){
		if (mapCondition == null){
			mapCondition = new HashMap<String, Object>();
		}	
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("operateUser"))
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " like '%" +  value + "%' ");
				}
			if (key.equals("logLevel")){
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and " + key + " = '" + value + "' ");
				}
			}
			if(key.equals("sd")){
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and operatetime >= to_date('" + value + "','yyyy-MM-dd') ");
				}
			}
			if(key.equals("ed")){
				if (value != null) {
					if (!value.toString().equals(""))
						conditionBuilder.append(" and operatetime <= to_date('" + value + "','yyyy-MM-dd') ");
				}
			}
		}
		conditionBuilder.append(" order by operatetime desc");
		return conditionBuilder;
	}
}
