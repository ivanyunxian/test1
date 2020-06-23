package com.supermap.wisdombusiness.framework.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.wisdombusiness.framework.dao.RoleDao;
import com.supermap.wisdombusiness.framework.dao.RoleGroupDao;
import com.supermap.wisdombusiness.framework.dao.RoleModuleDao;
import com.supermap.wisdombusiness.framework.dao.SystemModuleDao;
import com.supermap.wisdombusiness.framework.dao.UserDao;
import com.supermap.wisdombusiness.framework.dao.UserRoleDao;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.framework.model.RoleModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.UserRole;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.web.ui.tree.TreeUtil;

/**
 * 
 * @Description:角色serviceImpl
 * @author 杨鹏
 * @date 2015年7月10日 下午2:01:16
 * @Copyright SuperMap
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private RoleModuleDao roleModuleDao;

	@Autowired
	private SystemModuleDao systemModuleDao;

	@Autowired
	private RoleGroupDao roleGroupDao;

	@Override
	public Role findById(String id) {
		return roleDao.get(id);
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAll();
	}

	@Override
	public void saveRole(Role role) {
		roleDao.saveOrUpdate(role);
	}

	@Override
	public void deleteRoleById(String id) {
		// 删除角色系统模块关联
		List<String> roleModules = roleModuleDao.findRoleModuleIdByRoleId(id);
		for (String roleModuleId : roleModules) {
			roleModuleDao.delete(roleModuleId);
		}
		// 删除角色用户关联
		List<String> userRoles = userRoleDao.findUserRoleIdByRoleId(id);
		for (String userRoleId : userRoles) {
			userRoleDao.delete(userRoleId);
		}
		// 删除角色
		roleDao.delete(id);
	}

	@Override
	public void updateRole(Role role) {
		roleDao.saveOrUpdate(role);
	}

	@Override
	@Transactional(readOnly = true)
	public Page getPagedRole(int pageIndex, int pageSize, Map<String, Object> mapCondition) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (StringUtils.hasLength(value.toString())) {
				map.put(key, value);
			}
		}
		return roleDao.getPagedRole(pageIndex, pageSize, map);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getRolesByLoginName(String loginName) {
		return roleDao.getRolesByLoginName(loginName);
	}

	@Override
	@Transactional(readOnly = true)
	public Page getPagedUserByRole(int pageIndex, int pageSize, Map<String, Object> mapCondition) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (StringUtils.hasLength(value.toString())) {
				map.put(key, value);
			}
		}
		return roleDao.getPagedUserByRole(pageIndex, pageSize, map);
	}

	// @Override
	// public void createRoleUserRecords(String roleId, String userIds)
	// {
	// List<String> userRoles = userRoleDao.findUserRoleIdByRoleId(roleId);
	// for (String userRoleId : userRoles) {
	// userRoleDao.delete(userRoleId);
	// }
	//
	// String userId[] = userIds.split(",");
	// for (int i = 0; i < userId.length; i++) {
	// UserRole userRole = new UserRole();
	// userRole.setUser(userDao.get(userId[i]));
	// userRole.setRole(roleDao.get(roleId));
	// userRoleDao.save(userRole);
	// }
	// }

	@Override
	public void createRoleUserRecords(String roleId, String userIds) {
		List<String> userRoles = userRoleDao.findUserRoleIdByRoleId(roleId);
		for (String userRoleId : userRoles) {
			userRoleDao.delete(userRoleId);
		}

		String userId[] = userIds.split(";");
		for (int i = 0; i < userId.length; i++) {
			String arrString[] = userId[i].split(",");
			String mId = arrString[0];
			String mType = arrString[1];
			if (mType.toUpperCase().equals("USER")) {
				UserRole userRole = new UserRole();
				userRole.setUser(userDao.get(mId));
				userRole.setRole(roleDao.get(roleId));
				userRoleDao.save(userRole);
				userRoleDao.flush();
			}
		}

	}

	@Override
	public void createRoleModuleRecords(String roleId, String moduleIds) {
		List<String> roleModules = roleModuleDao.findRoleModuleIdByRoleId(roleId);
		for (String roleModuleId : roleModules) {
			roleModuleDao.delete(roleModuleId);
		}

		String moduleId[] = moduleIds.split(";");
		for (int i = 0; i < moduleId.length; i++) {
			String arrString[] = moduleId[i].split(",");
			String mId = arrString[0];
			String mType = arrString[1];
			if (mType.toUpperCase().equals("MODULE")) {
				RoleModule roleModule = new RoleModule();
				roleModule.setRole(roleDao.get(roleId));
				roleModule.setSystemModule(systemModuleDao.get(mId));
				Date createTime = new Date();
				roleModule.setCreateTime(createTime);
				roleModuleDao.save(roleModule);
				roleModuleDao.flush();
			}
		}
	}

	@Override
	public List<String> findModuleIdByRoleId(String roleId) {
		List<String> Modules = roleModuleDao.findModuleIdByRoleId(roleId);
		return Modules;
	}

	@Override
	public List<String> findUserIdByRoleId(String roleId) {
		List<String> users = userRoleDao.findUserIdByRoleId(roleId);
		return users;
	}

	@Override
	public List<Tree> getRoleTree() {
		List<Tree> treelist = new ArrayList<Tree>();
		List<RoleGroup> roleGroups = roleGroupDao.findAll();
		for (RoleGroup roleGroup : roleGroups) {
			Tree tree = new Tree();
			tree.setId(roleGroup.getId());
			tree.setText(roleGroup.getGroupName());
			tree.setParentid(null);
			tree.setTypeStr("jsz");
			treelist.add(tree);
			List<Role> roles = roleDao.getRolesByGroupId(roleGroup.getId());
			for (Role roleEn : roles) {
				Tree treechild = new Tree();
				treechild.setId(roleEn.getId());
				treechild.setText(roleEn.getRoleName());
				treechild.setParentid(roleGroup.getId());
				treechild.setTypeStr("js");
				treelist.add(treechild);
			}
		}
		return TreeUtil.build(treelist);
	}

	@Override
	public List<String> getRolesByUserId(String id) {
		return userRoleDao.findRoleIdByUserId(id);
	}

	@Override
	public List<User> findUsersByRoleId(String roleId) {
		List<User> users = userRoleDao.findUsersByRoleId(roleId);
		return users;
	}

	@Override
	public List<User> findUsersByRoleIdAndCode(String roleId, String Areacode) {
		List<User> users = userRoleDao.findUsersByRoleIdAndCode(roleId, Areacode);
		return users;
	}

	@Override
	public List<Role> getAllRoleByUserId(String id) {
		return userRoleDao.findAllRoleByUserId(id);

	}

	@Override
	public List<User> getUsers() {
		List<User> lists = userDao.findAll();
		return lists;
	}

	@Override
	public Map<String, List<String>> CompareDiffStaffInRole(String roleid, String staffids) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<User> users = userRoleDao.findUsersByRoleId(roleid);
		List<String> add = new ArrayList<String>();
		List<String> remove = new ArrayList<String>();
		//if (users != null && users.size() > 0) {
			String[] staffs = staffids.split(";");
			if (staffs.length > 0) {
				List<String> staff = new ArrayList<String>();
				for (int i = 0; i < staffs.length; i++) {
					String[] tmp = staffs[i].split(",");
					if (tmp != null && tmp.length > 0)
						staff.add(tmp[0]);
				}
				if (users != null && users.size() > 0) {
					for (User user : users) {
						String item = user.getId();
						if (!staff.contains(item)) {
							remove.add(user.getId());
						}
						for (int i = 0; i < staff.size(); i++) {
							if (staff.get(i).equals(item)) {
								staff.remove(i);
								break;
							}
						}
					}					
				}
				if (staff.size() > 0) {
					for (String s : staff) {
						//判定人是否存在
						User u=userDao.get(s);
						if(u!=null){
							add.add(s);
						}
						
					}
				}
			}
		//}
		map.put("remove", remove);
		map.put("add", add);
		return map;
	}

	@Override
	public List<Role> getRoleByRoleCode(String roleCode) {
		return roleDao.getRoleByRoleCode(roleCode);
	}
}
