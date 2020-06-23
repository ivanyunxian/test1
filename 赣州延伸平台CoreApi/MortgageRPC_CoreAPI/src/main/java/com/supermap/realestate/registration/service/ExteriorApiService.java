package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

public interface ExteriorApiService {
	
	/**
	 * 根据登记系统的登陆名及密码判断是否存在该用户
	 * 
	 * @author huangpeifeng
	 * @date 20180622
	 * @param loginname 登记系统的用户登陆名
	 * @param password 用户密码
	 * @return boolean
	 */
	public boolean isExistsUserDJXT(String loginname, String password);
	/**
	 * 判断access用户是否被锁
	 * 
	 * @author huangpeifeng
	 * @date 20180622
	 * @param accessname 用户名称
	 * @param locknumber 锁定数量,超过这个数量就算锁定
	 * @return boolean
	 */
	public boolean isLockedUser(String accessname, int locknumber);
}
