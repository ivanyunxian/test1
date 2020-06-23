package com.supermap.wisdombusiness.framework.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.RoleDao;
import com.supermap.wisdombusiness.framework.dao.RoleGroupDao;
import com.supermap.wisdombusiness.framework.dao.UserDao;
import com.supermap.wisdombusiness.framework.dao.UserRoleDao;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.UserRole;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.web.ui.tree.TreeUtil;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_PassWork;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.util.MD5Util;

/**
 * 
 * @Description:用户serviceImpl
 * @author chenhl
 * @date 2015年7月10日 下午2:05:41
 * @Copyright SuperMap
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RoleGroupDao roleGroupDao;
	
	@Autowired
	private SmActInst smActInst;
	
	@Autowired
	private RoleService roleService;

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	@Transactional
	public void saveUser(User user) {
		userDao.save(user);
		userDao.flush();
		String roleIds = user.getRoleId();
		if (roleIds != null && !roleIds.equals("")) {
			String roleId[] = user.getRoleId().split(",");
			for (int i = 0; i < roleId.length; i++) {
				UserRole userRole = new UserRole();
				userRole.setUser(user);
				userRole.setRole(roleDao.get(roleId[i]));
				userRoleDao.save(userRole);
				userRoleDao.flush();
			}
		}
	}

	@Override
	@Transactional
	public void deleteUserById(String id) {
		
		List<String> userRoles = userRoleDao.findUserRoleIdByUserId(id);
		for (String userRoleId : userRoles) {
			userRoleDao.delete(userRoleId);
		}
		userDao.delete(id);
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userDao.update(user);
		userDao.flush();
//		List<String> userRoles = userRoleDao.findUserRoleIdByUserId(user.getId());
//		for (String userRoleId : userRoles) {
//			userRoleDao.delete(userRoleId);
//		}
//		String roleIds = user.getRoleId();
//		if (roleIds != null && !roleIds.equals("")) {
//			String roleId[] = user.getRoleId().split(",");
//			for (int i = 0; i < roleId.length; i++) {
//				UserRole userRole = new UserRole();
//				userRole.setUser(user);
//				userRole.setRole(roleDao.get(roleId[i]));
//				userRoleDao.save(userRole);
//				userRoleDao.flush();
//			}
//		}
	}

	@Override
	@Transactional
	public void saveUserRole(User user, String roleIds) {
		List<String> userRoles = userRoleDao.findUserRoleIdByUserId(user.getId());
		for (String userRoleId : userRoles) {
			userRoleDao.delete(userRoleId);
		}
		if (roleIds != null && !roleIds.equals("")) {
			String roleId[] = roleIds.split(",");
			for (int i = 0; i < roleId.length; i++) {
				UserRole userRole = new UserRole();
				userRole.setUser(user);
				userRole.setRole(roleDao.get(roleId[i]));
				userRoleDao.save(userRole);
				userRoleDao.flush();
			}
		}
	}

	@Override
	public void resetPassword(User user) {
		userDao.update(user);
	}

	@Override
	@Transactional(readOnly = true)
	public Page getPagedUser(int pageIndex, int pageSize, Map<String, Object> mapCondition) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (StringUtils.hasLength(value.toString())) {
				map.put(key, value);
			}
		}
		return userDao.getPagedUser(pageIndex, pageSize, map);
	}

	@Override
	public User findByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	@Override
	public User findById(String id) {
		return userDao.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tree> setRoleTree(String id) {
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
				if (id != null && !id.equals("")) {
					String roleId[] = id.split(",");
					for (int r = 0; r < roleId.length; r++) {
						if (roleEn.getId().equals(roleId[r]))
							treechild.setChecked(true);
					}
				}
				treelist.add(treechild);
			}

		}
		return TreeUtil.build(treelist);
	}

	@Override
	public List<User> findUserByDepartmentId(String departmentId) {
		List<User> users = userDao.findUserByDepartmentId(departmentId);		
		return users;
	}
	
	@Override
	public List<User> findUserWithNoDepartmentId() {
		List<User> users = userDao.findUserWithNoDepartmentId();	
		return users;
	}
	
	public User findUserByCN(String userCN){
		return userDao.findUserByCN(userCN);
	}

	@Override
	public User getCurrentUserInfo() {
		Subject userInfo = SecurityUtils.getSubject();
		if(null != userInfo){
			User user = new User();
			user = (User) userInfo.getPrincipal();
			if(null != user){
				return user;
			}
		}
		return null;
	}
 
	@Override
	 
	/**建立新的关系。将这个用户所在角色下所有代办件转到这个用户下
	 *  通过staff--role--wfd_act---wfI-actist---actstaff
	 */
	public ResultMessage addRelation(String staffid){
	    ResultMessage msg=new ResultMessage();
		List<String> roles = userRoleDao.findRoleIdByUserId(staffid);
		if(roles.size()>0){
			for(int j=0;j<roles.size();j++){
				String roleId=roles.get(j);
				if(roleId!=null && roleId!=""){
					List<Wfd_Actdef> actdefList = commonDao.findList(Wfd_Actdef.class, "ROLE_ID='"+roleId+"'");
					if(actdefList.size()>0){
						for(int k=0;k<actdefList.size();k++){
							Wfd_Actdef	oneActdef=actdefList.get(k);
							if(oneActdef.getActdef_Id()!=null && !oneActdef.getActdef_Id().equals("")){
								StringBuilder sb=new StringBuilder();
								sb.append(" ACTDEF_ID='");
								sb.append(oneActdef.getActdef_Id());
								sb.append("' and ACTINST_STATUS =1");
								List<Wfi_ActInst> actinstList=commonDao.findList(Wfi_ActInst.class, sb.toString());
								if(actinstList.size()>0){
									for(int w=0;w<actinstList.size();w++){
										Wfi_ActInstStaff actstaff=new Wfi_ActInstStaff();
										if(actinstList.get(w).getActinst_Id()!=null &&!"".equals(actinstList.get(w).getActinst_Id())){
											actstaff.setActinst_Id(actinstList.get(w).getActinst_Id());
											actstaff.setStaff_Id(staffid);
											commonDao.save(actstaff);
											commonDao.flush();
											msg.setMsg("部门更换维护成功！");
											msg.setSuccess("true");
										}
									}
								}
								
							}
						}
					}
				}
			}
		}else{
			msg.setMsg("新部门下没有待办件");
			msg.setSuccess("false");
		}
		
		return msg;
	}
	
	//删除关系
	/**
	 *  删除这个用户下的所有在办件
	 */
	@Override
	public void delRelation(String staffid){
		StringBuilder sb=new StringBuilder();
		sb.append(" STAFF_ID='");
		sb.append(staffid);
		sb.append("' and ACTINST_STATUS =2");
		List<Wfi_ActInst> actinstList=commonDao.findList(Wfi_ActInst.class, sb.toString());
		  
		 if(actinstList.size()>0){
			for(int i=0;i<actinstList.size();i++){
				 if(actinstList.get(i).getActinst_Id()!=null &&!"".equals(actinstList.get(i).getActinst_Id())){
					 String actinst_id=actinstList.get(i).getActinst_Id();
					 StringBuilder str=new StringBuilder();
					 str.append(" STAFF_ID='");
					 str.append(staffid);
					 str.append("' and ACTINST_ID ='");
					 str.append(actinst_id);
					 str.append("'");
					 List<Wfi_ActInstStaff> wfi_ActInstStaff = commonDao.findList(Wfi_ActInstStaff.class, str.toString());
					 if(wfi_ActInstStaff!=null && wfi_ActInstStaff.size()>0){
						 for(int j=0;j<wfi_ActInstStaff.size();j++){
								 commonDao.delete(wfi_ActInstStaff.get(j));
						 }
						
					 }
				 }
			} 
			 commonDao.flush();
		 }
	}
    /**
     * 获取员工的直接领导
     */
	@Override
	public List<User> getLeaderByStaffid(String staffid) {
		// TODO Auto-generated method stub
		//1。通过员工获取单位，
		//2、获取单位下的leader
		StringBuilder str=new StringBuilder();
		str.append("departmentid in(select departmentid from t_user where id='"
				+ staffid
				+ "') and isleader='1'");
		List<User> lists=commonDao.getDataList(User.class, "T_User", str.toString());
		
		return lists;
	}
	
	/**
     * 获取员工的委托信息
     */
	@Override
	public List<Wfi_PassWork> GetPassWorkByStaffID(String staffid){
		StringBuilder str=new StringBuilder();
		str.append("STAFF_ID='"+staffid+"'");
		List<Wfi_PassWork> lists=commonDao.getDataList(Wfi_PassWork.class,Common.WORKFLOWDB + "wfi_passwork",str.toString());
		return lists;
	}
	/**
     * 获取委托员工ID
     */
	public String isPassWork(String staffid,Wfd_Actdef def){
		StringBuilder str=new StringBuilder();
		str.append("select * from ");
		str.append(Common.WORKFLOWDB+"wfi_passwork ");
		str.append(" where staff_id='"+staffid+"' ");
		str.append(" and STATUS=1 ");
		Date nowDate = new Date();
		java.sql.Date sqlNowDate=new java.sql.Date(nowDate.getTime());
			str.append(" and PASS_START <= TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss')  ");
			str.append(" and PASS_END > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
		List<Map> staffids=commonDao.getDataListByFullSql(str.toString());
		
		String roleid= def.getRole_Id();
		String prodefid=def.getProdef_Id();
		if(staffids.size()>0){
			for (Map ma :staffids){
				staffid=ma.get("TOSTAFF_ID").toString();
				if(ma.get("ISALLPROWER")!=null||
						(ma.get("ROLE_IDS")!=null&&ma.get("ROLE_IDS").toString().contains(roleid))||
						(ma.get("PRODEF_IDS")!=null&&ma.get("PRODEF_IDS").toString().contains(prodefid))
						){				
					return staffid;
				}
			}
			
		}
			return null;
	}

	@Override
	public Map<String, List<String>> CompareDiffRoleInStaff(String staffid,
			String roleids) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		List<Role> allroles = roleService.getAllRoleByUserId(staffid);//获取之前用户所拥有角色
		List<String> add = new ArrayList<String>();
		List<String> remove = new ArrayList<String>();
		//if (allroles != null && allroles.size() > 0) {
			String[] roles = roleids.split(",");//页面勾选角色id数组
			if (roles.length > 0) {
				List<String> role = new ArrayList<String>();
				for (int i = 0; i < roles.length; i++) {
					String temp = roles[i];
					if (temp != null && !"".equals(temp)) {
						role.add(temp);//role为页面勾选id（list）
					}
				}
				if(allroles != null && allroles.size() > 0){
					for (Role arole : allroles) {
						String item = arole.getId();
						if (!role.contains(item)) {//勾选角色id是否包含之前所拥有角色id
							remove.add(arole.getId());//未包含则说明为取消的角色id，放在remove中处理
						}
						for (int i = 0; i < role.size(); i++) {
							if (role.get(i).equals(item)) {
								role.remove(i);//页面勾选角色id中移除之前所拥有角色id
								break;
							}
						}

					}					
				}
				if (role.size() > 0) {
					for (String r : role) {
						Role rl = roleDao.get(r);
						if (rl != null) {
							add.add(r);//剩下角色id（添加的角色）放在add中处理
						}
					}
				}

			}

		//}
		map.put("remove", remove);
		map.put("add", add);
		return map;
	}
	
	
	public Message certinfodownload(HttpServletRequest request,
			HttpServletResponse response) {
		String qzbh = "";
		if (request.getParameter("qzbh") != null) {
			qzbh = request.getParameter("qzbh");
		}
		
		String qzlx = "";
		if (request.getParameter("qzlx") != null) {
			qzlx = request.getParameter("qzlx");
		}
		String sfzf="";
		if(request.getParameter("sfzf")!=null){
			sfzf = request.getParameter("sfzf");
		}
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("select * from bdck.bdcs_rkqzb where 1>0 ");
		if (!StringHelper.isEmpty(qzbh)) {
			hqlBuilder.append(" and QZBH LIKE '%").append(qzbh).append("%'");
		}
		if (!StringHelper.isEmpty(qzlx)&&!("2").equals(qzlx)) {
			hqlBuilder.append(" and QZLX='").append(qzlx).append("'");
		}
		if(!StringHelper.isEmpty(sfzf)&&!("2").equals(sfzf)){
			hqlBuilder.append(" and SFZF='").append(sfzf).append("'");
		}
		List<Map> maps = commonDao.getDataListByFullSql(hqlBuilder.toString());
		Message m=new Message();
		m.setSuccess("true");
		m.setRows(maps);
		m.setMsg("成功！");
		return m;
	}

	@Override
	public String getPassWord(String loginName, String password) {
		User user = findByLoginName(loginName);
		if(null!=user){
			String relPassWord = user.getPassword();
			if(relPassWord.length()<48){
				//未加盐密码
				if(relPassWord.equals(com.supermap.wisdombusiness.utility.StringHelper.encryptMD5(password))){
					user.setPassword(MD5Util.generate(password));
					commonDao.update(user);
					commonDao.flush();
					return user.getPassword();
				}
			}else{
				if(MD5Util.verify(password, relPassWord)){
					return relPassWord;
				}
			}
		}
		return MD5Util.generate(password);
	}
}
