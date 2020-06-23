package com.supermap.realestate.registration.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.model.T_DETAIL_SELECTOR;
import com.supermap.realestate.registration.model.T_GRIDCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.T_QUERYCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.T_RESULT_SELECTOR;
import com.supermap.realestate.registration.model.T_SELECTOR;
import com.supermap.realestate.registration.model.T_SORTCONFIG_SELECTOR;
import com.supermap.realestate.registration.service.SelectorManagerService;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

@Service("selectormanagerService")
public class SelectorManagerServiceImpl implements SelectorManagerService {

	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 获取选择器信息 
	 * @param: GetSelectorInfo
	 * @author:俞学斌
	 * @date：2016年08月25日 16:36:28
	 * @return
	 */
	@Override
	public HashMap<String, String> GetSelectorInfo(String selectorid) {
		HashMap<String, String> selectorinfo=new HashMap<String, String>();
		if(StringHelper.isEmpty(selectorid)){
			return selectorinfo;
		}
		T_SELECTOR selector=baseCommonDao.get(T_SELECTOR.class, selectorid);
		if(selector==null){
			return selectorinfo;
		}
		selectorinfo.put("id", selectorid);
		selectorinfo.put("name", selector.getNAME());
		selectorinfo.put("selectbdcdy", selector.getSELECTBDCDY());
		selectorinfo.put("selectql", selector.getSELECTQL());
		selectorinfo.put("selectqllx", "");
		if(!StringHelper.isEmpty(selector.getSELECTQLLX())){
			QLLX qllx=QLLX.initFrom(selector.getSELECTQLLX());
			if(qllx!=null){
				selectorinfo.put("selectqllx", qllx.Name);
			}
		}
		selectorinfo.put("bdcdylx", "");
		if(!StringHelper.isEmpty(selector.getBDCDYLX())){
			BDCDYLX bdcdylx=BDCDYLX.initFrom(selector.getBDCDYLX());
			if(bdcdylx!=null){
				selectorinfo.put("bdcdylx", bdcdylx.Name);
			}
		}
		selectorinfo.put("ly", "");
		if(!StringHelper.isEmpty(selector.getLY())){
			DJDYLY djdyly=DJDYLY.initFrom(selector.getLY());
			if(djdyly!=null){
				if(DJDYLY.XZ.equals(djdyly)){
					selectorinfo.put("ly", "现状");
				}else if(DJDYLY.LS.equals(djdyly)){
					selectorinfo.put("ly", "历史");
				}else if(DJDYLY.GZ.equals(djdyly)){
					selectorinfo.put("ly", "工作");
				}else if(DJDYLY.DC.equals(djdyly)){
					selectorinfo.put("ly", "调查");
				}
			}
		}
		selectorinfo.put("condition", selector.getCONDITION());
		selectorinfo.put("idfieldname", selector.getIDFIELDNAME());
		selectorinfo.put("useconfigsql", selector.getUSECONFIGSQL());
		selectorinfo.put("configsql", selector.getCONFIGSQL());
		selectorinfo.put("singleselect", selector.getSINGLESELECT());
		selectorinfo.put("defaultselectfirt", selector.getDEFAULTSELECTFIRT());
		selectorinfo.put("showdetailaltersleect", selector.getSHOWDETAILALTERSELECT());
		return selectorinfo;
	}

	/**
	 * 更新选择器信息 
	 * @Title: UpdateSelectorInfo
	 * @author:俞学斌
	 * @date：2016年08月25日 16:36:28
	 * @return
	 */
	@Override
	public ResultMessage UpdateSelectorInfo(String selectorid,
			HttpServletRequest request) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("false");
		
		if(StringHelper.isEmpty(selectorid)){
			ms.setMsg("选择器标识为空！");
			return ms;
		}
		T_SELECTOR selector=baseCommonDao.get(T_SELECTOR.class, selectorid);
		if(selector==null){
			ms.setMsg("未找到选择器！");
			return ms;
		}
		String name=request.getParameter("name");
		String singleselect=request.getParameter("singleselect");
		String defaultselectfirt=request.getParameter("defaultselectfirt");
		String showdetailaltersleect=request.getParameter("showdetailaltersleect");
		selector.setNAME(name);
		selector.setSINGLESELECT(singleselect);
		selector.setDEFAULTSELECTFIRT(defaultselectfirt);
		selector.setSHOWDETAILALTERSELECT(showdetailaltersleect);
		baseCommonDao.update(selector);
		ms.setSuccess("true");
		ms.setMsg("保存成功！");
		HandlerFactory.reloadMappingConfig();
		return ms;
	}
	
	/**
	 * 获取选择器查询条件列表
	 * @Title: GetQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:10:28
	 * @return
	 */
	@Override
	public Message GetQueryConfig(String selectorid, HttpServletRequest request) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_QUERYCONFIG_SELECTOR> list=baseCommonDao.getDataList(T_QUERYCONFIG_SELECTOR.class, hqlBuilder.toString());
		long l=0;
		if(list!=null&&list.size()>0){
			l=list.size();
		}
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 新增或保存选择器查询条件 
	 * @Title: AddOrUpdateQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:14:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddOrUpdateQueryConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String entityname = request.getParameter("entityname");
		String fieldname = request.getParameter("fieldname");
		String fieldcaption = request.getParameter("fieldcaption");
		T_QUERYCONFIG_SELECTOR queryconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			queryconfig = baseCommonDao.get(T_QUERYCONFIG_SELECTOR.class, id);
			bnew = false;
		}
		if (queryconfig == null) {
			queryconfig = new T_QUERYCONFIG_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			queryconfig.setId(id);
			List<Map> list_sxh=baseCommonDao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_QUERYCONFIG_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			queryconfig.setSXH(sxh);
		}
		queryconfig.setENTITYNAME(entityname);
		queryconfig.setFIELDNAME(fieldname);
		queryconfig.setFIELDCAPTION(fieldcaption);
		queryconfig.setSELECTORID(selectorid);
		if (bnew)
			baseCommonDao.save(queryconfig);
		else
			baseCommonDao.update(queryconfig);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 删除选择器查询条件
	 * @Title: RemoveQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:13:28
	 * @return
	 */
	@Override
	public ResultMessage RemoveQueryConfig(String id, HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_QUERYCONFIG_SELECTOR queryconfig = baseCommonDao.get(T_QUERYCONFIG_SELECTOR.class, id);
		if(queryconfig!=null){
			baseCommonDao.deleteEntity(queryconfig);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除选择器查询条件！");
		}
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
	
	/**
	 * 重置选择器查询条件顺序 
	 * @Title: ResetSXHOnQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:12:28
	 * @return
	 */
	@Override
	public ResultMessage ResetSXHOnQueryConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		
		String json = request.getParameter("info");
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String id=StringHelper.formatObject(object.get("id"));
			Integer sxh=StringHelper.getInt(object.get("sxh"));
			T_QUERYCONFIG_SELECTOR queryconfig = baseCommonDao.get(T_QUERYCONFIG_SELECTOR.class, id);
			if(queryconfig!=null){
				queryconfig.setSXH(sxh+1);
				baseCommonDao.update(queryconfig);
			}
		}
		
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 获取选择器查询条件列表
	 * @Title: GetSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public Message GetSortConfig(String selectorid, HttpServletRequest request) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_SORTCONFIG_SELECTOR> list=baseCommonDao.getDataList(T_SORTCONFIG_SELECTOR.class, hqlBuilder.toString());
		long l=0;
		if(list!=null&&list.size()>0){
			l=list.size();
		}
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 新增或保存选择器查询条件 
	 * @Title: AddOrUpdateSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddOrUpdateSortConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String entityname = request.getParameter("entityname");
		String fieldname = request.getParameter("fieldname");
		String sorttype = request.getParameter("sorttype");
		T_SORTCONFIG_SELECTOR sortconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			sortconfig = baseCommonDao.get(T_SORTCONFIG_SELECTOR.class, id);
			bnew = false;
		}
		if (sortconfig == null) {
			sortconfig = new T_SORTCONFIG_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			sortconfig.setId(id);
			List<Map> list_sxh=baseCommonDao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_SORTCONFIG_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			sortconfig.setSXH(sxh);
		}
		sortconfig.setENTITYNAME(entityname);
		sortconfig.setFIELDNAME(fieldname);
		sortconfig.setSORTTYPE(sorttype);
		sortconfig.setSELECTORID(selectorid);
		if (bnew)
			baseCommonDao.save(sortconfig);
		else
			baseCommonDao.update(sortconfig);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 删除选择器查询条件
	 * @Title: RemoveSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage RemoveSortConfig(String id, HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_SORTCONFIG_SELECTOR sortconfig = baseCommonDao.get(T_SORTCONFIG_SELECTOR.class, id);
		if(sortconfig!=null){
			baseCommonDao.deleteEntity(sortconfig);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除选择器查询排序！");
		}
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
	
	/**
	 * 重置选择器查询条件顺序 
	 * @Title: ResetSXHOnSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage ResetSXHOnSortConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		
		String json = request.getParameter("info");
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String id=StringHelper.formatObject(object.get("id"));
			Integer sxh=StringHelper.getInt(object.get("sxh"));
			T_SORTCONFIG_SELECTOR sortconfig = baseCommonDao.get(T_SORTCONFIG_SELECTOR.class, id);
			if(sortconfig!=null){
				sortconfig.setSXH(sxh+1);
				baseCommonDao.update(sortconfig);
			}
		}
		
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 获取选择器查询结果列表
	 * @Title: GetGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public Message GetGridConfig(String selectorid, HttpServletRequest request) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_GRIDCONFIG_SELECTOR> list=baseCommonDao.getDataList(T_GRIDCONFIG_SELECTOR.class, hqlBuilder.toString());
		long l=0;
		if(list!=null&&list.size()>0){
			l=list.size();
		}
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 新增或保存选择器查询结果 
	 * @Title: AddOrUpdateGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddOrUpdateGridConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String width = request.getParameter("width");
		String fieldname = request.getParameter("fieldname");
		String columntext = request.getParameter("columntext");
		T_GRIDCONFIG_SELECTOR gridconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			gridconfig = baseCommonDao.get(T_GRIDCONFIG_SELECTOR.class, id);
			bnew = false;
		}
		if (gridconfig == null) {
			gridconfig = new T_GRIDCONFIG_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			gridconfig.setId(id);
			List<Map> list_sxh=baseCommonDao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_GRIDCONFIG_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			gridconfig.setSXH(sxh);
		}
		gridconfig.setWIDTH(width);
		gridconfig.setFIELDNAME(fieldname);
		gridconfig.setCOLUMNTEXT(columntext);
		gridconfig.setSELECTORID(selectorid);
		if (bnew)
			baseCommonDao.save(gridconfig);
		else
			baseCommonDao.update(gridconfig);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 删除选择器查询结果
	 * @Title: RemoveGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage RemoveGridConfig(String id, HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_GRIDCONFIG_SELECTOR gridconfig = baseCommonDao.get(T_GRIDCONFIG_SELECTOR.class, id);
		if(gridconfig!=null){
			baseCommonDao.deleteEntity(gridconfig);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除选择器查询结果！");
		}
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
	
	/**
	 * 重置选择器查询结果顺序 
	 * @Title: ResetSXHOnGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage ResetSXHOnGridConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		
		String json = request.getParameter("info");
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String id=StringHelper.formatObject(object.get("id"));
			Integer sxh=StringHelper.getInt(object.get("sxh"));
			T_GRIDCONFIG_SELECTOR gridconfig = baseCommonDao.get(T_GRIDCONFIG_SELECTOR.class, id);
			if(gridconfig!=null){
				gridconfig.setSXH(sxh+1);
				baseCommonDao.update(gridconfig);
			}
		}
		
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 获取选择器查询结果列表
	 * @Title: GetResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public Message GetResultConfig(String selectorid, HttpServletRequest request) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		List<T_RESULT_SELECTOR> list=baseCommonDao.getDataList(T_RESULT_SELECTOR.class, hqlBuilder.toString());
		long l=0;
		if(list!=null&&list.size()>0){
			l=list.size();
		}
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 新增或保存选择器查询结果 
	 * @Title: AddOrUpdateResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage AddOrUpdateResultConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname");
		String deflautvalue = request.getParameter("deflautvalue");
		String newfieldendwithname = request.getParameter("newfieldendwithname");
		String consttype = request.getParameter("consttype");
		T_RESULT_SELECTOR resultconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			resultconfig = baseCommonDao.get(T_RESULT_SELECTOR.class, id);
			bnew = false;
		}
		if (resultconfig == null) {
			resultconfig = new T_RESULT_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			resultconfig.setId(id);
		}
		resultconfig.setCONSTTYPE(consttype);
		resultconfig.setDEFLAULTVALUE(deflautvalue);
		resultconfig.setFIELDNAME(fieldname);
		resultconfig.setNEWFIELDENDWITHNAME(newfieldendwithname);
		resultconfig.setSELECTORID(selectorid);
		if (bnew)
			baseCommonDao.save(resultconfig);
		else
			baseCommonDao.update(resultconfig);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 删除选择器查询结果
	 * @Title: RemoveResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage RemoveResultConfig(String id, HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_RESULT_SELECTOR resultconfig = baseCommonDao.get(T_RESULT_SELECTOR.class, id);
		if(resultconfig!=null){
			baseCommonDao.deleteEntity(resultconfig);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除选择器结果常量！");
		}
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
	

	/**
	 * 获取选择器结果详情列表
	 * @Title: GetDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public Message GetDetailConfig(String selectorid, HttpServletRequest request) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_DETAIL_SELECTOR> list=baseCommonDao.getDataList(T_DETAIL_SELECTOR.class, hqlBuilder.toString());
		long l=0;
		if(list!=null&&list.size()>0){
			l=list.size();
		}
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 新增或保存选择器结果详情
	 * @Title: AddOrUpdateDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddOrUpdateDetailConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String fieldcolor = request.getParameter("fieldcolor");
		String fieldname = request.getParameter("fieldname");
		String fieldtext = request.getParameter("fieldtext");
		T_DETAIL_SELECTOR detailconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			detailconfig = baseCommonDao.get(T_DETAIL_SELECTOR.class, id);
			bnew = false;
		}
		if (detailconfig == null) {
			detailconfig = new T_DETAIL_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			detailconfig.setId(id);
			List<Map> list_sxh=baseCommonDao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_DETAIL_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			detailconfig.setSXH(sxh);
		}
		detailconfig.setFIELDCOLOR(fieldcolor);
		detailconfig.setFIELDNAME(fieldname);
		detailconfig.setFIELDTEXT(fieldtext);
		detailconfig.setSELECTORID(selectorid);
		if (bnew)
			baseCommonDao.save(detailconfig);
		else
			baseCommonDao.update(detailconfig);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	/**
	 * 删除选择器结果详情
	 * @Title: RemoveDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage RemoveDetailConfig(String id, HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_DETAIL_SELECTOR detailconfig = baseCommonDao.get(T_DETAIL_SELECTOR.class, id);
		if(detailconfig!=null){
			baseCommonDao.deleteEntity(detailconfig);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除选择器结果详情！");
		}
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
	
	/**
	 * 重置选择器结果详情顺序 
	 * @Title: ResetSXHOnDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@Override
	public ResultMessage ResetSXHOnDetailConfig(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		
		String json = request.getParameter("info");
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String id=StringHelper.formatObject(object.get("id"));
			Integer sxh=StringHelper.getInt(object.get("sxh"));
			T_DETAIL_SELECTOR detailconfig = baseCommonDao.get(T_DETAIL_SELECTOR.class, id);
			if(detailconfig!=null){
				detailconfig.setSXH(sxh+1);
				baseCommonDao.update(detailconfig);
			}
		}
		
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}

	@Override
	public ResultMessage CopySelector(String selectorid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("false");
			msg.setMsg("保存失败！未获取到选择器定义！");
			return msg;
		}
		String new_id = request.getParameter("new_id");
		String new_name = request.getParameter("new_name");
		if(!StringHelper.isEmpty(new_id)){
			T_SELECTOR selectorinfo = baseCommonDao.get(T_SELECTOR.class, new_id);
			if(selectorinfo!=null){
				msg.setSuccess("false");
				msg.setMsg("保存失败！选择器标识已存在！");
				return msg;
			}
		}
		if(StringHelper.isEmpty(new_id)){
			new_id=SuperHelper.GeneratePrimaryKey();
		}
		//复制选择器
		T_SELECTOR selectorinfo_from = baseCommonDao.get(T_SELECTOR.class, selectorid);
		T_SELECTOR selectorinfo_to=new T_SELECTOR();
		try {
			PropertyUtils.copyProperties(selectorinfo_to, selectorinfo_from);
		} catch (Exception e) {
		}
		selectorinfo_to.setId(new_id);
		selectorinfo_to.setNAME(new_name);
		baseCommonDao.save(selectorinfo_to);
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		//复制选择器查询条件
		List<T_QUERYCONFIG_SELECTOR> list_queryconfig=baseCommonDao.getDataList(T_QUERYCONFIG_SELECTOR.class, hqlBuilder.toString());
		if(list_queryconfig!=null&&list_queryconfig.size()>0){
			for(T_QUERYCONFIG_SELECTOR queryconfig_from:list_queryconfig){
				String configid=SuperHelper.GeneratePrimaryKey();
				T_QUERYCONFIG_SELECTOR queryconfig_to=new T_QUERYCONFIG_SELECTOR();
				try {
					PropertyUtils.copyProperties(queryconfig_to, queryconfig_from);
				} catch (Exception e) {
				}
				queryconfig_to.setSELECTORID(new_id);
				queryconfig_to.setId(configid);
				baseCommonDao.save(queryconfig_to);
			}
		}
		//复制选择器查询排序
		List<T_SORTCONFIG_SELECTOR> list_sortconfig=baseCommonDao.getDataList(T_SORTCONFIG_SELECTOR.class, hqlBuilder.toString());
		if(list_sortconfig!=null&&list_sortconfig.size()>0){
			for(T_SORTCONFIG_SELECTOR sortconfig_from:list_sortconfig){
				String configid=SuperHelper.GeneratePrimaryKey();
				T_SORTCONFIG_SELECTOR sortconfig_to=new T_SORTCONFIG_SELECTOR();
				try {
					PropertyUtils.copyProperties(sortconfig_to, sortconfig_from);
				} catch (Exception e) {
				}
				sortconfig_to.setSELECTORID(new_id);
				sortconfig_to.setId(configid);
				baseCommonDao.save(sortconfig_to);
			}
		}
		//复制选择器查询结果
		List<T_GRIDCONFIG_SELECTOR> list_gridconfig=baseCommonDao.getDataList(T_GRIDCONFIG_SELECTOR.class, hqlBuilder.toString());
		if(list_gridconfig!=null&&list_gridconfig.size()>0){
			for(T_GRIDCONFIG_SELECTOR gridconfig_from:list_gridconfig){
				String configid=SuperHelper.GeneratePrimaryKey();
				T_GRIDCONFIG_SELECTOR gridconfig_to=new T_GRIDCONFIG_SELECTOR();
				try {
					PropertyUtils.copyProperties(gridconfig_to, gridconfig_from);
				} catch (Exception e) {
				}
				gridconfig_to.setSELECTORID(new_id);
				gridconfig_to.setId(configid);
				baseCommonDao.save(gridconfig_to);
			}
		}
		//复制选择器常量配置
		List<T_RESULT_SELECTOR> list_resultconfig=baseCommonDao.getDataList(T_RESULT_SELECTOR.class, hqlBuilder.toString());
		if(list_resultconfig!=null&&list_resultconfig.size()>0){
			for(T_RESULT_SELECTOR resultconfig_from:list_resultconfig){
				String configid=SuperHelper.GeneratePrimaryKey();
				T_RESULT_SELECTOR resultconfig_to=new T_RESULT_SELECTOR();
				try {
					PropertyUtils.copyProperties(resultconfig_to, resultconfig_from);
				} catch (Exception e) {
				}
				resultconfig_to.setSELECTORID(new_id);
				resultconfig_to.setId(configid);
				baseCommonDao.save(resultconfig_to);
			}
		}
		//复制选择器结果详情
		List<T_DETAIL_SELECTOR> list_detailconfig=baseCommonDao.getDataList(T_DETAIL_SELECTOR.class, hqlBuilder.toString());
		if(list_resultconfig!=null&&list_resultconfig.size()>0){
			for(T_DETAIL_SELECTOR detailconfig_from:list_detailconfig){
				String configid=SuperHelper.GeneratePrimaryKey();
				T_DETAIL_SELECTOR detailconfig_to=new T_DETAIL_SELECTOR();
				try {
					PropertyUtils.copyProperties(detailconfig_to, detailconfig_from);
				} catch (Exception e) {
				}
				detailconfig_to.setSELECTORID(new_id);
				detailconfig_to.setId(configid);
				baseCommonDao.save(detailconfig_to);
			}
		}
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("复制成功");
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
}
