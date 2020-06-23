package com.supermap.wisdombusiness.framework.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.wisdombusiness.framework.dao.RoleGroupDao;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.framework.service.RoleGroupService;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:角色分组serviceImpl
 * @author 杨鹏
 * @date 2015年7月10日 下午2:00:31
 * @Copyright SuperMap
 */
@Service("RoleGroupService")
public class RoleGroupServiceImpl implements RoleGroupService {
	
	//角色分组dao
	@Autowired
	private RoleGroupDao roleGroupDao;
	
	@Override
	public RoleGroup findById(String id) {
		return roleGroupDao.get(id);
	}
	
	@Override
	public List<RoleGroup> findAll() {
		return roleGroupDao.findAll();
	}
	
	@Override
	public void saveRoleGroup(RoleGroup role){
		 roleGroupDao.saveOrUpdate(role);
	}
	
	@Override
	public void deleteRoleGroupById(String id){
		 roleGroupDao.delete(id);
	}
	
	@Override
	public void updateRoleGroup(RoleGroup roleGroup){
		 roleGroupDao.saveOrUpdate(roleGroup);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page getPagedRoleGroup(int pageIndex, int pageSize,
			Map<String, Object> mapCondition) {
		Map<String, Object> map = new HashMap<String,Object>();
		for(Iterator<String> iterator = mapCondition.keySet().iterator();iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if(StringUtils.hasLength(value.toString())) {
				map.put(key, value);
			}
		}
		return roleGroupDao.getPagedRoleGroup(pageIndex, pageSize, map);
	}

}
