package com.supermap.wisdombusiness.workflow.service.relationmaintain;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;

public interface IRecordsMaintain {
	
	/**
	 * 维护角色和员工关系
	 * @param roleid
	 * @param relation
	 * @return
	 */
	public Boolean MaintainRoleStaff(String roleid,Map<String,List<String>> relation);
	/**
	 * 流程设计中环节定义的办理角色发生变化
	 * @date 2016年11月15日 上午11:46:54
	 * @author JHX
	 * @param Actdef
	 * @return
	 */
	public Boolean mainTainFile(Wfd_Actdef Actdef);
	/**
	 * 维护员工和角色关系
	 * @author zhangp
	 * @data 2017年3月29日下午6:05:59
	 * @param staffid
	 * @param roles
	 * @return
	 */
	public Boolean MaintainStaffRole(String staffid,Map<String,List<String>> diff);
	
}
