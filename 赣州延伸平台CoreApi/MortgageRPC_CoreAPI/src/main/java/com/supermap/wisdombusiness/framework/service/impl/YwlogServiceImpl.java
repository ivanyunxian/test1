package com.supermap.wisdombusiness.framework.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.wisdombusiness.framework.dao.YwlogDao;
import com.supermap.wisdombusiness.framework.model.Ywlog;
import com.supermap.wisdombusiness.framework.service.YwlogService;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:部门serviceImpl
 * @author diaoliwei
 * @date 2015年7月10日 下午2:04:31
 * @Copyright SuperMap
 */
@Service("ywlogService")
public class YwlogServiceImpl implements YwlogService {

	@Autowired
	private YwlogDao ywlogDao;
	
	@Override
	public Ywlog findById(String id) {
		return ywlogDao.get(id);
	}

	@Override
	public List<Ywlog> findAll() {
		return ywlogDao.findAll();
	}

	@Override
	public Page getPagedYwlog(int pageIndex, int pageSize, Map<String, Object> mapCondition) {
		Map<String, Object> map = this.getMap(mapCondition);
		return ywlogDao.getPagedYwlog(pageIndex, pageSize, map);
	}
	
	@Override
	public List<Ywlog> logList(Map<String, Object> mapCondition){
		Map<String, Object> map = this.getMap(mapCondition);
		return ywlogDao.logList(map);
	}
	
	
	private Map<String, Object> getMap(Map<String, Object> mapCondition){
		Map<String, Object> map = new HashMap<String,Object>();
		for(Iterator<String> iterator = mapCondition.keySet().iterator();iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if(StringUtils.hasLength(value.toString())) {
				map.put(key, value);
			}
		}
		return map;
	}
	
}
