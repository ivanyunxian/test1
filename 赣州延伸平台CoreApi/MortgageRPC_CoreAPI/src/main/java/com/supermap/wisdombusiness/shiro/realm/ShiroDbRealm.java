package com.supermap.wisdombusiness.shiro.realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.utility.Helper;

public class ShiroDbRealm extends AuthorizingRealm {

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "roleService")
	private RoleService roleService;

	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 获取当前登录的用户名
		String account = (String) super.getAvailablePrincipal(principals);
		List<String> roles = roleService.getRolesByLoginName(account);
		List<String> permissions = new ArrayList<String>();
		User user = userService.findByLoginName(account);
		if (user != null) {
			// if (user.getRoles() != null && user.getRoles().size() > 0) {
			// for (Role role : user.getRoles()) {
			// roles.add(role.getName());
			// if (role.getPmss() != null && role.getPmss().size() > 0) {
			// for (Permission pmss : role.getPmss()) {
			// if(!StringUtils.isEmpty(pmss.getPermission())){
			// permissions.add(pmss.getPermission());
			// }
			// }
			// }
			// }
			// }
		} else {
			throw new AuthorizationException();
		}
		// 给当前用户设置角色
		info.addRoles(roles);
		// 给当前用户设置权限
		info.addStringPermissions(permissions);
		return info;

	}

	/**
	 * 认证回调函数,登录时调用.
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
	/*	String sso = GetProperties.getConstValueByKey("HKSSO");
		if (sso != null&&!sso.equals("")) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("_userName", token.getUsername());
			m.put("_password", new String(token.getPassword(),0,token.getPassword().length));

			Vector info =(Vector) Helper.WebService(m, sso, "CheckUser",false);
			if(info!=null){
				User currUser = new User();
				currUser.setId(info.get(0).toString());
				currUser.setLoginName(info.get(0).toString());
				currUser.setUserName(info.get(1).toString());
				currUser.setPassword(m.get("_password"));
				userService.updateUser(currUser);
				return new SimpleAuthenticationInfo(currUser, currUser.getPassword(), currUser.getUserName());
			}
			else{
				return null;
			}
			
		} else {*/
			User user = userService.findByLoginName(token.getUsername());

			if (user != null) {

				return new SimpleAuthenticationInfo(user, user.getPassword(), user.getUserName());
			} else {
				return null;
			}
		//}

	}
}
