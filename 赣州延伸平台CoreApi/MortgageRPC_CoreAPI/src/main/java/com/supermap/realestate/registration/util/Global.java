package com.supermap.realestate.registration.util;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.T_HANDLER;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {

	private static CommonDao dao;

	static {
		if (dao == null) {
			dao = SuperSpringContext.getContext().getBean(CommonDao.class); 
		}
	}

	/* 系统开关项 */
	// 根据流程编号获取流程处理器的时候，是否使用编码缓存
	public static boolean USECACHEFORREGISTERWORKFLOW = false;

	// 受理的时候，是否对每个人能够受理的业务使用缓存，减少数据库压力
	public static boolean USECACHE_ACCEPT_WORKFLOW = false;

	// 读取整个流程树的时候，是否使用缓存
	public static boolean USECACHE_CONFIG_WORKFLOW = false;

	// 是否对BDCS_XMXX使用缓存
	public static boolean USECACHE_XMXX = true;

	// 是否关联宗地
	public static boolean ISRELATIONLAND = false;

	// 是否发送消息和生成XML
	public static boolean SENDMESSAGE = false;
	
	//是否启用数据操作日志记录
	public static boolean USEDATAOPERATELOG = true;
	
	// 实体权利类型
	public static String REALQLLX = " ('1','2','3,','4','5','6','7','8','11','12') ";

	private static Map<String, BDCS_XMXX> projectMap = new HashMap<String, BDCS_XMXX>();

	private static Map<String, BDCS_XMXX> xmbhMap = new HashMap<String, BDCS_XMXX>();

	public static BDCS_XMXX getXMXX(String project_id) {
		BDCS_XMXX xmxx = null;
		if (projectMap != null && projectMap.containsKey(project_id) && USECACHE_XMXX) {
			xmxx = projectMap.get(project_id);
		} else {
			String hql = MessageFormat.format(" PROJECT_ID=''{0}''", project_id);
			List<BDCS_XMXX> list = dao.getDataList(BDCS_XMXX.class, hql);
			if (list != null && list.size() > 0) {
				xmxx = list.get(0);
				if (projectMap != null && !projectMap.containsKey(project_id))
					projectMap.put(project_id, xmxx);
				if (xmbhMap != null && !xmbhMap.containsKey(xmxx.getId())) {
					xmbhMap.put(xmxx.getId(), xmxx);
				}
			}
		}
		return xmxx;
	}

	public static BDCS_XMXX getXMXXbyXMBH(String xmbh) {
		BDCS_XMXX xmxx = null;
		if (xmbhMap != null && xmbhMap.containsKey(xmbh) && USECACHE_XMXX) {
			xmxx = xmbhMap.get(xmbh);
		} else {
			xmxx = dao.get(BDCS_XMXX.class, xmbh);
			if (xmbhMap != null && !xmbhMap.containsKey(xmbh)) {
				xmbhMap.put(xmbh, xmxx);
			}
			if (projectMap != null && xmxx!= null && !projectMap.containsKey(xmxx.getPROJECT_ID()))
				projectMap.put(xmxx.getPROJECT_ID(), xmxx);
		}
		return xmxx;
	}

	public static void clearXMXXCache() {
		projectMap.clear();
		xmbhMap.clear();
	}

	public static void addXMXX(String project_id, BDCS_XMXX xmxx) {
		if (!projectMap.containsKey(project_id)) {
			projectMap.put(project_id, xmxx);
		}
		if (!xmbhMap.containsKey(xmxx.getId())) {
			xmbhMap.put(xmxx.getId(), xmxx);
		}
	}

	public static void removeXMXX(String project_id) {
		if (projectMap.containsKey(project_id)) {
			if (xmbhMap.containsKey(projectMap.get(project_id).getId())) {
				xmbhMap.remove(projectMap.get(project_id).getId());
			}
			projectMap.remove(project_id);
		}
	}

	public static String getCurrentUserName() {
		String currentUserName = "";
		try {
			Subject user = SecurityUtils.getSubject();
			if (user != null) {
				User u ;
				if (user.getSession().getAttribute("uid") != null) {
					String uid = String.valueOf(user.getSession().getAttribute("uid"));
					u = dao.get(User.class, uid);
				} else {
					u = (User) user.getPrincipal();
				}
				if(null!=u){
					currentUserName = u.getUserName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentUserName;
	}

	/**
	 * 得到当前登录用户的信息
	 * @作者 diaoliwei
	 * @创建时间 2015年7月6日下午12:15:12
	 * @return
	 */
	public static User getCurrentUserInfo() {
		Subject userInfo = SecurityUtils.getSubject();
		User u = null;
		if (null != userInfo) { 
			if (userInfo.getSession().getAttribute("uid") != null) {
				String uid = String.valueOf(userInfo.getSession().getAttribute("uid"));
				u = dao.get(User.class, uid);
			} else {
				u = (User) userInfo.getPrincipal();
			}
 		}
		return u;
	}
	
	/**
	 * 获取handler名称
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getHandlerName(String xmbh){
		Map map=new HashMap<String, String>();
		String handlername="";
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
		if(xmxx !=null){
			CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
			String fulSql= " SELECT B.HANDLERID FROM BDCK.T_BASEWORKFLOW B "
					  + " LEFT JOIN BDC_WORKFLOW.WFD_MAPPING M ON M.WORKFLOWNAME=B.ID "
					  + " WHERE M.WORKFLOWCODE= '" + workflowcode + "' ";
		  List<Map> lstmap=	dao.getDataListByFullSql(fulSql);
			if(lstmap !=null && lstmap.size()>0){
				Map m=lstmap.get(0);
				if(m !=null){
					String handlerid=StringHelper.FormatByDatatype(m.get("HANDLERID"));
					if(!StringHelper.isEmpty(handlerid)){
						T_HANDLER handler=dao.get(T_HANDLER.class, handlerid);
						if(handler !=null){
							handlername= handler.getCLASSNAME().toUpperCase();
						}
					}
				}
			}
		}
		return handlername;
	}
	
	/**
	 * 判断是否带抵押的组合登记
	 * @param xmbh
	 * @return
	 */
	public static boolean SfComboDJ(String xmbh){
		boolean comboDJFlag=false;
		String handlername=getHandlerName(xmbh);
		String strgzdj="BZ_DYZXANDBZ_DJHandler,ZY_DYZXANDZY_DJHandler,"
				 + "ZY_DY_DJHandler,CSDYHandler,ZY_YDYTODY_DJHandler,"
				 + "YGYDYDJHandler,ZY_DYQBG_DJHandler,HZ_SYQ_DYQ_DJHandler,ZY_YGDYTODY_DJHandler";	
		strgzdj=strgzdj.toUpperCase();
		String [] lstgzdj=strgzdj.split(",");
		for(int i=0;i<lstgzdj.length;i++){
			if(lstgzdj[i].trim().equals(handlername))
			{
				comboDJFlag=true;
				break;
			}
		}
		return comboDJFlag;
	}
	
	/**
	 * 可通用，sql 语句in函数中表达式大于100以上的，按100分割，再or columnName in (.....)
	 * @author LML  2017-8-22 11:46
	 * @param sqlParam
	 * @param columnName
	 * @return
	 */
	public static String GetSqlIn( String sqlParam, String columnName ){
		String paraArray[] = sqlParam.split(",");
		String temp = "";
		for (int m = 0; m < paraArray.length; m++) {
			if ((m + 1) % 100 == 0 && m != paraArray.length - 1) {
				temp = temp + paraArray[m] + ") OR " + columnName + " IN (";
			} else if (m == paraArray.length - 1) {
				temp = temp + paraArray[m];
			} else {
				temp += paraArray[m] + ",";
			}
		}
		return temp;
	}

}
