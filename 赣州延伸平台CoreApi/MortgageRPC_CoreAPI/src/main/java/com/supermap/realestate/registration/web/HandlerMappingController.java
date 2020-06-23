package com.supermap.realestate.registration.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.model.T_BASEWORKFLOW;
import com.supermap.realestate.registration.model.T_DETAIL_SELECTOR;
import com.supermap.realestate.registration.model.T_GRIDCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.T_HANDLER;
import com.supermap.realestate.registration.model.T_QUERYCONFIG_SELECTOR;
import com.supermap.realestate.registration.model.T_RESULT_SELECTOR;
import com.supermap.realestate.registration.model.T_SELECTOR;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 登簿检查Controller，流程相关的登簿检查放在这里边
 * @ClassName: ConfigController
 * @author 俞学斌
 * @date 2015年11月07日 16:52:28
 */
@Controller
@RequestMapping("/handlermapping")
public class HandlerMappingController {

	@Autowired
	private CommonDao basecommondao;

	private final String prefix = "/realestate/registration/";

	/*****************************************基准流程定义*****************************************/
	/**
	 * 基准流程定义页面(URL:"/baseworkflow/index/",Method：GET)
	 * @Title: getBaseWorkflowIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/baseworkflow/index/")
	public String getBaseWorkflowIndex() {
		return prefix + "handlermapping/baseworkflow";
	}
	
	/**
	 * 获取基准流程定义列表(URL:"/baseworkflow/",Method：GET)
	 * @Title: GetBaseWorklfowList
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/baseworkflow/", method = RequestMethod.GET)
	public @ResponseBody Message GetBaseWorklfowList(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (ID LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%')");
		}
		hqlBuilder.append(" ORDER BY ID");
		Page p = basecommondao.getPageDataByHql(T_BASEWORKFLOW.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 保存基准流程配置 (URL:"/baseworkflow/{baseworkflowid}/",Method：POST)
	 * @Title: AddOrUpdateResultField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/baseworkflow/{baseworkflowid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateBaseWorkFlow(@PathVariable("baseworkflowid") String baseworkflowid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringHelper.isEmpty(baseworkflowid)) {
			msg.setSuccess("false");
			msg.setMsg("保存失败！未获取到基准流程定义！");
			return msg;
		}
		String selectorid = request.getParameter("SELECTORID");
		T_BASEWORKFLOW baseworkflow=basecommondao.get(T_BASEWORKFLOW.class, baseworkflowid);
		if(baseworkflow!=null){
			baseworkflow.setSELECTORID(selectorid);
			basecommondao.update(baseworkflow);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("保存成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("保存失败！未获取到基准流程定义！");
		}
		HandlerFactory.reloadMappingConfig();
		return msg;
	}
	/*****************************************基准流程定义*****************************************/
	/*****************************************操作类定义*****************************************/
	/**
	 * 操作类定义页面(URL:"/handler/index/",Method：GET)
	 * @Title: getHandlerIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/handler/index/")
	public String getHandlerIndex() {
		return prefix + "handlermapping/handler";
	}
	
	/**
	 * 获取操作类定义列表(URL:"/handler/",Method：GET)
	 * @Title: GetHandlerList
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/handler/", method = RequestMethod.GET)
	public @ResponseBody Message GetHandlerList(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (ID LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%')");
		}
		hqlBuilder.append(" ORDER BY ID");
		Page p = basecommondao.getPageDataByHql(T_HANDLER.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	/*****************************************操作类定义*****************************************/
	/*****************************************选择器定义*****************************************/
	/**
	 * 选择器定义页面(URL:"/selector/index/",Method：GET)
	 * @Title: getSelectorIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/index/")
	public String getSelectorIndex() {
		return prefix + "handlermapping/selector";
	}
	
	/**
	 * 获取选择器定义列表(URL:"/selector/",Method：GET)
	 * @Title: GetSelectorList
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/", method = RequestMethod.GET)
	public @ResponseBody Message GetSelectorList(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		String bdcdylx="";
		try {
			bdcdylx = RequestHelper.getParam(request, "bdcdylx");
		} catch (Exception e) {
		}
		
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(bdcdylx)) {
			hqlBuilder.append(" AND BDCDYLX='"+bdcdylx+"'");
		}
		
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (ID LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%')");
		}
		hqlBuilder.append(" ORDER BY ID");
		Page p = basecommondao.getPageDataByHql(T_SELECTOR.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 获取选择器定义列表(URL:"/selector/",Method：GET)
	 * @Title: GetSelectorList
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/allselector/", method = RequestMethod.GET)
	public @ResponseBody List<T_SELECTOR> GetAllSelectorList(HttpServletRequest request, HttpServletResponse response) {
		List<T_SELECTOR> list=basecommondao.getDataList(T_SELECTOR.class, " 1>0 ORDER BY id,NAME");
		return list;
	}
	/*****************************************选择器定义*****************************************/
	
	/*****************************************选择器查询条件配置*****************************************/

	/**
	 * 选择器查询条件配置页面(URL:"/selector/querymanager/index/",Method：GET)
	 * @Title: getQueryManagerIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/querymanager/index/")
	public String getQueryManagerIndex() {
		return prefix + "handlermapping/querymanager";
	}
	
	/**
	 * 获取选择器查询条件列表(URL:"/selector/querymanager/",Method：GET)
	 * @Title: GetQueryFields
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/querymanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetQueryFields(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_QUERYCONFIG_SELECTOR> list=basecommondao.getDataList(T_QUERYCONFIG_SELECTOR.class, hqlBuilder.toString());
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
	 * 新增或保存选择器查询条件 (URL:"/selector/querymanager/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateQueryField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/selector/querymanager/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateQueryField(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname");
		String entityname = request.getParameter("entityname");
		String fieldcaption = request.getParameter("fieldcaption");
		T_QUERYCONFIG_SELECTOR queryconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			queryconfig = basecommondao.get(T_QUERYCONFIG_SELECTOR.class, id);
			bnew = false;
		}
		if (queryconfig == null) {
			queryconfig = new T_QUERYCONFIG_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			queryconfig.setId(id);
			List<Map> list_sxh=basecommondao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_QUERYCONFIG_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			queryconfig.setSXH(sxh);
		}
		queryconfig.setFIELDNAME(fieldname);
		queryconfig.setENTITYNAME(entityname);
		queryconfig.setFIELDCAPTION(fieldcaption);
		if (bnew)
			basecommondao.save(queryconfig);
		else
			basecommondao.update(queryconfig);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}
	
	/**
	 * 删除选择器查询条件(URL:"/selector/querymanager/{id}",Method：DELETE)
	 * @Title: RemoveModuleManager
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/querymanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveQueryField(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_QUERYCONFIG_SELECTOR queryconfig = basecommondao.get(T_QUERYCONFIG_SELECTOR.class, id);
		if(queryconfig!=null){
			basecommondao.deleteEntity(queryconfig);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除查询条件定义！");
		}
		return msg;
	}
	
	/**
	 * 重置选择器查询条件顺序 (URL:"/selector/querymanager/{selectorid}/resetsxh/",Method：POST)
	 * @Title: ResetSXHOnModule
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/querymanager/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnQuery(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
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
			T_QUERYCONFIG_SELECTOR queryconfig = basecommondao.get(T_QUERYCONFIG_SELECTOR.class, id);
			if(queryconfig!=null){
				queryconfig.setSXH(sxh+1);
				basecommondao.update(queryconfig);
			}
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		return msg;
	}
	/*****************************************选择器查询条件配置*****************************************/
	
	/*****************************************选择器结果字段配置*****************************************/

	/**
	 * 选择器结果列表配置页面(URL:"/selector/gridmanager/index/",Method：GET)
	 * @Title: getGridManagerIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/gridmanager/index/")
	public String getGridManagerIndex() {
		return prefix + "handlermapping/gridmanager";
	}
	
	/**
	 * 获取选择器结果列表字段列表(URL:"/selector/gridmanager/",Method：GET)
	 * @Title: GetGridFields
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/gridmanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetGridFields(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_GRIDCONFIG_SELECTOR> list=basecommondao.getDataList(T_GRIDCONFIG_SELECTOR.class, hqlBuilder.toString());
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
	 * 新增或保存选择器结果列表字段 (URL:"/selector/gridmanager/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateGridField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/selector/gridmanager/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateGridField(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname");
		String columntext = request.getParameter("columntext");
		String width = request.getParameter("width");
		T_GRIDCONFIG_SELECTOR gridconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			gridconfig = basecommondao.get(T_GRIDCONFIG_SELECTOR.class, id);
			bnew = false;
		}
		if (gridconfig == null) {
			gridconfig = new T_GRIDCONFIG_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			gridconfig.setId(id);
			List<Map> list_sxh=basecommondao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_GRIDCONFIG_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			gridconfig.setSXH(sxh);
		}
		gridconfig.setFIELDNAME(fieldname);
		gridconfig.setCOLUMNTEXT(columntext);
		gridconfig.setWIDTH(width);
		if (bnew)
			basecommondao.save(gridconfig);
		else
			basecommondao.update(gridconfig);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}
	
	/**
	 * 删除选择器结果列表字段(URL:"/selector/gridmanager/{id}",Method：DELETE)
	 * @Title: RemoveGridField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/gridmanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveGridField(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_GRIDCONFIG_SELECTOR gridconfig = basecommondao.get(T_GRIDCONFIG_SELECTOR.class, id);
		if(gridconfig!=null){
			basecommondao.deleteEntity(gridconfig);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除结果字段定义！");
		}
		return msg;
	}
	
	/**
	 * 重置选择器结果字段顺序 (URL:"/selector/gridmanager/{selectorid}/resetsxh/",Method：POST)
	 * @Title: ResetSXHOnGrid
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/gridmanager/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnGrid(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
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
			T_GRIDCONFIG_SELECTOR gridconfig = basecommondao.get(T_GRIDCONFIG_SELECTOR.class, id);
			if(gridconfig!=null){
				gridconfig.setSXH(sxh+1);
				basecommondao.update(gridconfig);
			}
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		return msg;
	}
	/*****************************************选择器结果字段配置*****************************************/
	/*****************************************选择器结果详情配置*****************************************/

	/**
	 * 选择器结果详情配置页面(URL:"/selector/detailmanager/index/",Method：GET)
	 * @Title: getDetailManagerIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/detailmanager/index/")
	public String getDetailManagerIndex() {
		return prefix + "handlermapping/detailmanager";
	}
	
	/**
	 * 获取选择器结果详情列表(URL:"/selector/detailmanager/",Method：GET)
	 * @Title: GetDetailFields
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/detailmanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetDetailFields(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_DETAIL_SELECTOR> list=basecommondao.getDataList(T_DETAIL_SELECTOR.class, hqlBuilder.toString());
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
	 * 新增或保存选择器结果详情字段 (URL:"/selector/detailmanager/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateDetailField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/selector/detailmanager/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateDetailField(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname");
		String fieldcolor = request.getParameter("fieldcolor");
		String fieldtext = request.getParameter("fieldtext");
		T_DETAIL_SELECTOR detailconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			detailconfig = basecommondao.get(T_DETAIL_SELECTOR.class, id);
			bnew = false;
		}
		if (detailconfig == null) {
			detailconfig = new T_DETAIL_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			detailconfig.setId(id);
			List<Map> list_sxh=basecommondao.getDataListByFullSql("SELECT MAX(SXH) AS SXH FROM BDCK.T_DETAIL_SELECTOR WHERE SELECTORID='"+selectorid+"'");
			Integer sxh=1;
			if(list_sxh!=null&&list_sxh.size()>0&&!StringHelper.isEmpty(list_sxh.get(0).get("SXH"))){
				sxh=StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh=sxh+1;
			}
			detailconfig.setSXH(sxh);
		}
		detailconfig.setFIELDNAME(fieldname);
		detailconfig.setFIELDCOLOR(fieldcolor);
		detailconfig.setFIELDTEXT(fieldtext);
		if (bnew)
			basecommondao.save(detailconfig);
		else
			basecommondao.update(detailconfig);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}
	
	/**
	 * 删除选择器结果详情字段(URL:"/selector/detailmanager/{id}",Method：DELETE)
	 * @Title: RemoveDetailField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/detailmanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveDetailField(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_DETAIL_SELECTOR detailconfig = basecommondao.get(T_DETAIL_SELECTOR.class, id);
		if(detailconfig!=null){
			basecommondao.deleteEntity(detailconfig);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除结果字段定义！");
		}
		return msg;
	}
	
	/**
	 * 重置选择器结果详情顺序 (URL:"/selector/detailmanager/{selectorid}/resetsxh/",Method：POST)
	 * @Title: ResetSXHOnDetail
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/detailmanager/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnDetail(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
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
			T_DETAIL_SELECTOR detailconfig = basecommondao.get(T_DETAIL_SELECTOR.class, id);
			if(detailconfig!=null){
				detailconfig.setSXH(sxh+1);
				basecommondao.update(detailconfig);
			}
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		return msg;
	}
	/*****************************************选择器结果详情配置*****************************************/
	/*****************************************选择器结果字典转换配置*****************************************/

	/**
	 * 选择器结果字典转换配置页面(URL:"/selector/resultmanager/index/",Method：GET)
	 * @Title: getResultManagerIndex
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/resultmanager/index/")
	public String getResultManagerIndex() {
		return prefix + "handlermapping/resultmanager";
	}
	
	/**
	 * 获取选择器结果字典转换列表(URL:"/selector/resultmanager/",Method：GET)
	 * @Title: GetResultFields
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/resultmanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetResultFields(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND SELECTORID='"+selectorid+"'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<T_RESULT_SELECTOR> list=basecommondao.getDataList(T_RESULT_SELECTOR.class, hqlBuilder.toString());
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
	 * 新增或保存选择器结果字典转换字段 (URL:"/selector/resultmanager/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateResultField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/resultmanager/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateResultField(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(selectorid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到选择器定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname");
		String deflaultvalue = request.getParameter("deflaultvalue");
		String newfieldendwithname = request.getParameter("newfieldendwithname");
		String consttype = request.getParameter("consttype");
		T_RESULT_SELECTOR resultconfig = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			resultconfig = basecommondao.get(T_RESULT_SELECTOR.class, id);
			bnew = false;
		}
		if (resultconfig == null) {
			resultconfig = new T_RESULT_SELECTOR();
			id=SuperHelper.GeneratePrimaryKey();
			resultconfig.setId(id);
		}
		resultconfig.setFIELDNAME(fieldname);
		resultconfig.setDEFLAULTVALUE(deflaultvalue);
		resultconfig.setNEWFIELDENDWITHNAME(newfieldendwithname);
		resultconfig.setCONSTTYPE(consttype);
		if (bnew)
			basecommondao.save(resultconfig);
		else
			basecommondao.update(resultconfig);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}
	
	/**
	 * 删除选择器结果字典转换字段(URL:"/selector/resultmanager/{id}",Method：DELETE)
	 * @Title: RemoveResultField
	 * @author:俞学斌
	 * @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/selector/resultmanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveResultField(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_RESULT_SELECTOR resultconfig = basecommondao.get(T_RESULT_SELECTOR.class, id);
		if(resultconfig!=null){
			basecommondao.deleteEntity(resultconfig);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除结果字段定义！");
		}
		return msg;
	}
	/*****************************************选择器结果详情配置*****************************************/
}
