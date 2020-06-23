package com.supermap.wisdombusiness.framework.service.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;

public class LoginStatusTask {
	
	@Autowired
	private CommonDao commonDao;
	
	public void ResetStatus(){
		System.out.println("执行一次更新");
		int count = commonDao.excuteQuery("UPDATE T_USER SET ISLAND='false' WHERE ISLAND !='false'"); 
		System.out.println(count);
	}
}
