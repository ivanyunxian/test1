package com.supermap.wisdombusiness.synchroinline.service;

import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.synchroinline.dao.TimerDao;
import com.supermap.wisdombusiness.synchroinline.dao.synchroDao;
import com.supermap.wisdombusiness.synchroinline.model.EstateInlineLog;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SynchroPreMData
{
	@Autowired
	private synchroDao dao;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AcceptProjectService acceptProjectService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	SmProDef smProDef;
	@Autowired
	SmStaff smStaff;
	private Log log = LogFactory.getLog(SynchroPreMData.class);


	/**
	 * 获取登记系统数据库处理在线受理的人员集合
	 * 
	 * @author JHX
	 * */
	public List<User> getInlineAccpetors()
	{
		// TODO:在线受理角色ID需要配置"4b01d0d316da4e8a9e196c3fc85b9642";
		String roleid = ConfigHelper.getNameByValue("INLINEROLEID");
		List<User> users = roleService.findUsersByRoleId(roleid);
		return users;
	}

	/**
	 * 创建日志信息
	 * */
	@javax.transaction.Transactional
	public void createLog(String operationtype, String operationcontent, String res)
	{
		EstateInlineLog log = new EstateInlineLog();
		log.setOperation_Content(operationcontent);
		log.setOperation_Date(new Date());
		log.setOperation_Type(operationtype);
		log.setOperation_Res(res);
		dao.save(log);
		dao.flush();
	}

	/**
	 * @author JHX 根据流程定义id获取流程对应的环节以及正确的排序，用于在线受理进度查询所需要的时间轴
	 * @date 2017-01-03 17:17
	 * */
	@SuppressWarnings("rawtypes")
	public String getJSONProdef(TimerDao timerDao, String prodefid, String startactdefid)
	{
		String jsonStr = "";
		String sql = "select A.actdef_id,a.prodef_id,a.actdef_name,a.actdef_type,case when n.rn is null then 100 else n.rn end rn  from  bdc_workflow.wfd_actdef A  left join (\n" + "select actdef_id,rownum rn  from bdc_workflow.wfd_route\n" + "start with  actdef_id = '" + startactdefid + "'\n" + "connect by prior  next_actdef_id = actdef_id ) n  on  A.ACTDEF_ID = n.actdef_id WHERE a.prodef_id = '" + prodefid + "'";;
		List<Map> lists = timerDao.getDataListByFullSql(sql);
		net.sf.json.JSONArray JSONArray = new net.sf.json.JSONArray();
		if (lists != null && lists.size() > 0)
		{
			HashMap map = null;
			for (int i = 0; i < lists.size(); i++)
			{
				map = (HashMap) lists.get(i);
				net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(map);
				if (!obj.isEmpty())
				{
					JSONArray.add(obj);
				}
			}
			jsonStr = JSONArray.toString();
		}
		return jsonStr;
	}

}
