package com.supermap.wisdombusiness.workflow.service.wfd;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Role;
import com.supermap.wisdombusiness.workflow.model.Wfd_RoleClass;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

/**
 * 关于角色的一些操作
 * */
@Component("smRoleDef")
public class SmRoleDef {

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private RoleService roleservice;

	/**
	 * 通过角色获取拥有给角色的员工
	 * 
	 * */
	public List<SmObjInfo> GetStaffByRoleID(String Roleid) {
		List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
		List<User> users = roleservice.findUsersByRoleIdAndCode(Roleid,smStaff.GetCurrentAreaCode());
		if (users != null && users.size() > 0) {
			for (User user : users) {
				SmObjInfo objInfo = new SmObjInfo();
				objInfo.setID(user.getId());
				objInfo.setName(user.getUserName());
				objInfo.setDesc("Staff");
				objInfos.add(objInfo);
			}
		}
		return objInfos;
	}
	/**
	 * @author JHX
	 * @DATE:2016-08-15
	 * 重载方法GetStaffByRoleID 增加参数行政区代码(扩展字段)
	 * 通过角色获取拥有给角色的员工
	 * */
	public List<SmObjInfo> GetStaffByRoleID(String Roleid,String areacode) {
		List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
		List<User> users = roleservice.findUsersByRoleIdAndCode(Roleid,areacode);
		if (users != null && users.size() > 0) {
			for (User user : users) {
				SmObjInfo objInfo = new SmObjInfo();
				objInfo.setID(user.getId());
				objInfo.setName(user.getUserName());
				objInfo.setDesc("Staff");
				objInfos.add(objInfo);
			}
		}
		return objInfos;
	}
	/**
	 * 通过活动定义ID获取可以办理给活动的员工
	 * 
	 * @param actdefID
	 *            活动定义ID @
	 * */
	public List<SmObjInfo> GetStaffByActDefID(String actdefID) {
		List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
		if (actdefID != null && actdefID != "") {
			Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class, actdefID);
			if (actdef != null) {
				objInfos = GetStaffByRoleID(actdef.getRole_Id());
			}

		}

		return objInfos;

	}

	/**
	 * 获取角色树
	 * 
	 * @return
	 */
	public List<TreeInfo> GetRoleTree() {
		return GetRoleChildTree(null);
	}

	/**
	 * 获取角色子树
	 * 
	 * @param RoleClass
	 * @return
	 */
	public List<TreeInfo> GetRoleChildTree(Wfd_RoleClass RoleClass) {
		StringBuilder str = new StringBuilder();
		if (RoleClass == null) {
			str.append(" Parentclassid is null or Parentclassid='0' ");
		} else {
			str.append(" Parentclassid ='");
			str.append(RoleClass.getRoleclass_Id());
			str.append("'");
		}
		str.append(" order by Class_Index");
		List<Wfd_RoleClass> list = commonDao.findList(Wfd_RoleClass.class,
				str.toString());
		List<TreeInfo> treelist = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			Wfd_RoleClass _RoleClass = list.get(i);
			TreeInfo tree = new TreeInfo();
			tree.setPreid(_RoleClass.getParentclassid());
			tree.setId(_RoleClass.getRoleclass_Id());
			tree.setText(_RoleClass.getClass_Name());
			tree.children = GetRoleChildTree(_RoleClass);
			tree.setType("catalog");
			treelist.add(tree);
		}

		if (RoleClass != null) {
			StringBuilder str2 = new StringBuilder();
			str2.append(" Roleclass_Id='");
			str2.append(RoleClass.getRoleclass_Id());
			str2.append("' order by Role_Index");

			List<Wfd_Role> list2 = commonDao.findList(Wfd_Role.class,
					str2.toString());
			for (int i = 0; i < list2.size(); i++) {
				Wfd_Role _Role = list2.get(i);
				TreeInfo tree = new TreeInfo();
				tree.setPreid(_Role.getRoleclass_Id());
				tree.setId(_Role.getRole_Id());
				tree.setText(_Role.getRole_Name());
				tree.setType("data");
				treelist.add(tree);
			}
		}

		return treelist;
	}

	/**
	 * 更新
	 * 
	 * @param RoleClass
	 */
	public void SaveOrUpdate(Wfd_RoleClass RoleClass) {
		commonDao.saveOrUpdate(RoleClass);
		commonDao.flush();
	}

	public void SaveOrUpdate(Wfd_Role Role) {
		commonDao.saveOrUpdate(Role);
		commonDao.flush();
	}

	public Wfd_Role GetWfdRoleById(String id) {
		return commonDao.get(Wfd_Role.class, id);
	}

	public Wfd_RoleClass GetWfdRoleClassById(String id) {
		return commonDao.get(Wfd_RoleClass.class, id);
	}

	public String CreateWfdRoleClassByName(String name, String pid, int index) {
		Wfd_RoleClass _RoleClass = new Wfd_RoleClass();
		_RoleClass.setClass_Name(name);
		_RoleClass.setParentclassid(pid);
		_RoleClass.setClass_Index(index);
		commonDao.save(_RoleClass);
		commonDao.flush();
		return _RoleClass.getRoleclass_Id();
	}

	public String CreateWfdRoleByName(String name, String pid, int index) {
		Wfd_Role _Role = new Wfd_Role();
		_Role.setRole_Name(name);
		_Role.setRoleclass_Id(pid);
		_Role.setRole_Index(index);
		commonDao.save(_Role);
		commonDao.flush();
		return _Role.getRole_Id();
	}

	public void DeleteWfdRoleById(String id) {
		commonDao.delete(Wfd_Role.class, id);
		commonDao.flush();
	}

	public void DeleteWfdRoleClassById(String id) {
		StringBuilder str = new StringBuilder();
		StringBuilder str2 = new StringBuilder();
		str.append("Roleclass_Id='");
		str.append(id);
		str.append("'");
		str2.append("Parentclassid='");
		str2.append(id);
		str2.append("'");
		List<Wfd_Role> list = commonDao
				.findList(Wfd_Role.class, str.toString());
		for (int i = 0; i < list.size(); i++) {
			Wfd_Role Role = list.get(i);
			commonDao.delete(Role);
		}
		List<Wfd_RoleClass> list2 = commonDao.findList(Wfd_RoleClass.class,
				str2.toString());
		for (int i = 0; i < list2.size(); i++) {
			Wfd_RoleClass RoleClass = list2.get(i);
			DeleteWfdRoleClassById(RoleClass.getRoleclass_Id());
			commonDao.delete(RoleClass);
		}
		commonDao.delete(Wfd_RoleClass.class, id);
		commonDao.flush();
	}

}
