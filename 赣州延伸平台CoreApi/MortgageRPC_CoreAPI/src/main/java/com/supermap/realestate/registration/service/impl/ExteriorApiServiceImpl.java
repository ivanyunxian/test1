package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.LOG_ACCESS;
import com.supermap.realestate.registration.service.ExteriorApiService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;

@Service("exteriorApiService")
public class ExteriorApiServiceImpl implements ExteriorApiService{
	
	@Autowired
	private CommonDao dao;
	
	/**
	 * 根据登记系统的登陆名及密码判断是否存在该用户
	 * 
	 * @author huangpeifeng
	 * @date 20180622
	 * @param loginname 登记系统的用户登陆名
	 * @param password 用户密码
	 * @return boolean
	 */
	@Override
	public boolean isExistsUserDJXT(String loginname, String password) {
		boolean flag = false;
		if(!StringHelper.isEmpty(loginname) && !StringHelper.isEmpty(password)){
			Map<String, String> mapUserInfo = new HashMap<String, String>();
			String sql = "from smwb_framework.t_user where status='NORMAL' and loginname=:loginname and password=:password";
			mapUserInfo.put("loginname", loginname);
			mapUserInfo.put("password", password);
			long usercount = dao.getCountByFullSql(sql, mapUserInfo);
			if(usercount>0){
				flag = true;
			}
			dao.flush();
		}	
		return flag;
	}
	
	/**
	 * 判断access用户是否被锁
	 * 
	 * @author huangpeifeng
	 * @date 20180622
	 * @param accessname 用户名称
	 * @param locknumber 锁定数量,超过这个数量就算锁定
	 * @return boolean
	 */
	@Override
	public boolean isLockedUser(String accessname, int locknumber){
		boolean flag = false;
		String islock = "ACCESSNAME=:accessname AND USELESSCOUNT>="+locknumber+" AND YXBZ='1' AND SFCG='0'";
		Map<String,String> mapAccessname = new HashMap<String,String>();
		mapAccessname.put("accessname", accessname);
		List<LOG_ACCESS> accessusers = dao.getDataList(LOG_ACCESS.class, islock, mapAccessname);
		if(accessusers!=null && accessusers.size()>0){
			for(LOG_ACCESS accessuser : accessusers ){
				if(StringHelper.isEmpty(accessuser.getALLOWACCESSTIME())){
					accessuser.setYXBZ(ConstValue.SF.NO.Value);
					dao.update(accessuser);
					continue;
				}
				String newdate = StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss");
				String allowaccesstime = StringHelper.FormatDateOnType(accessuser.getALLOWACCESSTIME(), "yyyy-MM-dd HH:mm:ss");
				if(DateUtil.compareTo(newdate, allowaccesstime)){
					flag = true;
				}else{
					accessuser.setYXBZ(ConstValue.SF.NO.Value);
					dao.update(accessuser);
				}
			}
		}
		dao.flush();
		return flag;
	}
}
