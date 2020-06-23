package com.supermap.realestate.registration.tools;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_LOG_NEW;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;



/**
 * 日志类对象，主要记录添加单元，删除单元，添加权利人，移除权利人，获取权证号日志及权证号赋值信息及修改持证方式等
 * @author shb
 *
 */
public class NewLogTools {

	/**
	 * 给日志配置信息赋值
	 * @param logcontext：日志内容
	 * @param xmbh：项目编号
	 * @param type：日志类型1-添加单元，2-删除单元，3-添加权利人，4-删除权利人，5-持证方式，6-获取权证号日志及权证号赋值信息
	 * @param typename：日志类型名称
	 * @return
	 */
	public static BDCS_LOG_NEW  saveLog(String logcontext,String xmbh,String type,String typename){
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		BDCS_LOG_NEW log=new BDCS_LOG_NEW();
        log.setLOGCONTEXT(logcontext);	
        log.setOPERATETIME(new Date());
        log.setOPERATETYPENAME(typename);
        log.setXMBH(xmbh);
        log.setOPERATETYPE(type);
        log.setOPERATEUSER(Global.getCurrentUserName());
        dao.save(log);
        dao.flush();
		return log;
	}
	
	public static JSONObject getJSONByXMBH(String xmbh){
		JSONObject jsonobj=new JSONObject();
		jsonobj.put("XMBH", xmbh);
		ProjectInfo info= ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(info !=null){
			jsonobj.put("DJLX", info.getDjlx());
			jsonobj.put("QLLX", info.getQllx());
			jsonobj.put("WorkflowCode", info.getBaseworkflowcode());
			jsonobj.put("BDCDYLX", info.getBdcdylx());
			jsonobj.put("SelectorName", info.getSelectorname());
		}
		return jsonobj;
	}
}
