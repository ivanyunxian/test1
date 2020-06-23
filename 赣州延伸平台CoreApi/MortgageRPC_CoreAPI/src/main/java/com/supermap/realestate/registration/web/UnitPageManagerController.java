package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.tools.EntityTools;
import com.supermap.realestate.registration.tools.HistoryBackdateTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 登簿检查Controller，流程相关的登簿检查放在这里边
 * 
 * @ClassName: ConfigController
 * @author 俞学斌
 * @date 2015年11月07日 16:52:28
 */
@Controller
@RequestMapping("/unitpagemanager")
public class UnitPageManagerController {

	/**
	 * DYService
	 */
	@Autowired
	private DYService dyService;

	@Autowired
	private QueryService queryService;

	@Autowired
	private CommonDao basecommondao;

	private final String prefix = "/realestate/registration/";

	/*****************************************
	 * 单元字段定义
	 * 
	 * @throws Exception
	 * @throws IOException
	 *****************************************/

	@RequestMapping(value = "/test/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> Test(HttpServletRequest request, HttpServletResponse response) {
		return null;
		// HashMap<String,Object> map=BatchProjectTools.AnalysisXML("");
		// return map;
	}

	/**
	 * 单元字段定义页面(URL:"/fielddefine/index/",Method：GET)
	 * 
	 * @Title: getFieldDefineIndex
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/fielddefine/index/")
	public String getFieldDefineIndex() {
		return prefix + "unitpagemanager/fielddefine";
	}

	/**
	 * 获取单元字段定义列表(URL:"/fielddefine/",Method：GET)
	 * 
	 * @Title: GetFieldDefine
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/fielddefine/", method = RequestMethod.GET)
	public @ResponseBody Message GetFieldDefine(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter = "";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		String bdcdylx = "";
		try {
			bdcdylx = RequestHelper.getParam(request, "bdcdylx");
		} catch (Exception e) {
		}

		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(bdcdylx)) {
			hqlBuilder.append(" AND BDCDYLX='" + bdcdylx + "'");
		}
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (FIELDNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR FIELDDESCRIPTION LIKE '%").append(filter).append("%')");
		}
		hqlBuilder.append(" ORDER BY BDCDYLX,FIELDNAME ");
		Page p = basecommondao.getPageDataByHql(T_UNIT_FIELD.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 新增或保存单元字段定义 (URL:"/fielddefine/",Method：POST)
	 * 
	 * @Title: AddOrUpdateFieldDefine
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/fielddefine/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateFieldDefine(HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String id = request.getParameter("id");
		String bdcdylx = request.getParameter("bdcdylx");
		String fieldname = request.getParameter("fieldname").toUpperCase();
		String fielddescription = request.getParameter("fielddescription");
		String fieldtype = request.getParameter("fieldtype").toLowerCase();
		String fieldoption = request.getParameter("fieldoption").toUpperCase();
		String number_l = request.getParameter("number_L");
		String number_r = request.getParameter("number_R");
		T_UNIT_FIELD field = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			field = basecommondao.get(T_UNIT_FIELD.class, id);
			bnew = false;
		}
		if (field == null) {
			String tableName = "";
			if ("tdyt".equals(bdcdylx)) {
				tableName = "BDCS_TDYT_XZ";
			} else if ("yhzk".equals(bdcdylx)) {
				tableName = "BDCS_YHZK_XZ";
			} else if ("ljz".equals(bdcdylx)) {
				tableName = "BDCS_LJZ_XZ";
			} else if ("ycljz".equals(bdcdylx)) {
				tableName = "BDCS_LJZ_XZY";
			} else if ("c".equals(bdcdylx)) {
				tableName = "BDCS_C_XZ";
			} else if ("ycc".equals(bdcdylx)) {
				tableName = "BDCS_C_XZY";
			} else {
				tableName = BDCDYLX.initFrom(bdcdylx).XZTableName;
			}
			String strsql = "SELECT COLUMN_NAME FROM DBA_TAB_COLUMNS WHERE OWNER='BDCK' AND TABLE_NAME='" + tableName
					+ "' AND COLUMN_NAME='" + fieldname + "'";
			List<Map> list1 = basecommondao.getDataListByFullSql(strsql);
			if (list1 == null || list1.size() <= 0) {
				if ("02".equals(bdcdylx) && "ZDBDCQZH".equals(fieldname)) {

				} else if ("tdyt".equals(bdcdylx) && "TDYTNAME".equals(fieldname)) {
					fieldname = "TDYTName";
				} else if ("tdyt".equals(bdcdylx) && "TDDJNAME".equals(fieldname)) {
					fieldname = "TDDJName";
				} else if ("yhzk".equals(bdcdylx) && "YHFSNAME".equals(fieldname)) {
					fieldname = "YHFSName";
				} else {
					msg.setSuccess("false");
					msg.setMsg("此单元字段不存在！");
					return msg;
				}
			}
			List<T_UNIT_FIELD> list = basecommondao.getDataList(T_UNIT_FIELD.class,
					"BDCDYLX='" + bdcdylx + "' AND FIELDNAME='" + fieldname + "'");
			if (list != null && list.size() > 0) {
				msg.setSuccess("false");
				msg.setMsg("此单元字段定义已存在！");
				return msg;
			}
			field = new T_UNIT_FIELD();
			field.setId(fieldname + "_" + bdcdylx);
		}
		field.setBDCDYLX(bdcdylx);
		field.setFIELDNAME(fieldname);
		field.setFIELDDESCRIPTION(fielddescription);
		field.setFIELDOPTION(fieldoption);
		field.setFIELDTYPE(fieldtype);
		if (!StringHelper.isEmpty(number_l)) {
			field.setNUMBER_L(StringHelper.getInt(number_l));
		}
		if (!StringHelper.isEmpty(number_r)) {
			field.setNUMBER_R(StringHelper.getInt(number_r));
		}
		if (bnew)
			basecommondao.save(field);
		else
			basecommondao.update(field);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除单元字段定义 (URL:"/fielddefine/{id}",Method：DELETE)
	 * 
	 * @Title: RemoveFieldDefine
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/fielddefine/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveFieldDefine(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_UNIT_FIELD field = basecommondao.get(T_UNIT_FIELD.class, id);
		if (field != null) {
			basecommondao.deleteEntity(field);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元字段定义！");
		}
		return msg;
	}

	/***************************************** 单元字段定义 *****************************************/

	/***************************************** 单元模块定义 *****************************************/

	/**
	 * 单元模块定义页面(URL:"/moduledefine/index/",Method：GET)
	 * 
	 * @Title: getModuleDefineIndex
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/moduledefine/index/")
	public String getModuleDefineIndex() {
		return prefix + "unitpagemanager/moduledefine";
	}

	/**
	 * 获取单元模块定义列表(URL:"/moduledefine/",Method：GET)
	 * 
	 * @Title: GetModuleDefine
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/moduledefine/", method = RequestMethod.GET)
	public @ResponseBody Message GetModuleDefine(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter = "";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		String bdcdylx = "";
		try {
			bdcdylx = RequestHelper.getParam(request, "bdcdylx");
		} catch (Exception e) {
		}

		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(bdcdylx)) {
			hqlBuilder.append(" AND BDCDYLX='" + bdcdylx + "'");
		}
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND NAME LIKE '%").append(filter).append("%'");
		}
		hqlBuilder.append(" ORDER BY NAME ");
		Page p = basecommondao.getPageDataByHql(T_UNIT_MODULE.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	@RequestMapping(value = "/modulecopy/{id}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage Getmoduledefine(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		T_UNIT_MODULE module  = basecommondao.get(T_UNIT_MODULE.class, id);//id获取数据
		T_UNIT_MODULE newmod = new T_UNIT_MODULE();
		boolean flg = ObjectHelper.copyObject(module, newmod);//拷贝
		if(flg){
			String moduleid = SuperHelper.GeneratePrimaryKey();
			newmod.setId(moduleid);
			basecommondao.save(newmod);
			basecommondao.flush();
		}
		List<RT_UNIT_MODULEMANAGER> aa =basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class, " moduleid = '" +module.getId()+ "'");
		for (int i = 0; i < aa.size(); i++){
			RT_UNIT_MODULEMANAGER profj = aa.get(i);
			RT_UNIT_MODULEMANAGER modulemanager = new RT_UNIT_MODULEMANAGER();
			boolean cc = ObjectHelper.copyObject(profj, modulemanager);//拷贝
			if(cc){
				String modulemanagerid = SuperHelper.GeneratePrimaryKey();
				modulemanager.setId(modulemanagerid);
				modulemanager.setMODULEID(newmod.getId());
				basecommondao.save(modulemanager);
				basecommondao.flush();
			}
		}
    	msg.setSuccess("true");
    	msg.setMsg("复制成功");
		return msg;
	}
	/**
	 * 新增或保存单元模块定义 (URL:"/moduledefine/",Method：POST)
	 * 
	 * @Title: AddOrUpdateModuleDefine
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/moduledefine/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateModuleDefine(HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String id = request.getParameter("id");
		String bdcdylx = request.getParameter("bdcdylx");
		String name = request.getParameter("name");
		T_UNIT_MODULE module = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			module = basecommondao.get(T_UNIT_MODULE.class, id);
			bnew = false;
		}
		if (module == null) {
			module = new T_UNIT_MODULE();
			id = SuperHelper.GeneratePrimaryKey();
			module.setId(id);
			module.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			module.setCREATETIME(new Date());
		} else {
			module.setLASTMODIFIER(Global.getCurrentUserInfo().getLoginName());
			module.setMODIFYTIME(new Date());
		}
		module.setBDCDYLX(bdcdylx);
		module.setNAME(name);
		if (bnew)
			basecommondao.save(module);
		else
			basecommondao.update(module);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除单元模块定义 (URL:"/moduledefine/{id}",Method：DELETE)
	 * 
	 * @Title: RemoveModuleDefine
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/moduledefine/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveModuleDefine(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_UNIT_MODULE module = basecommondao.get(T_UNIT_MODULE.class, id);
		if (module != null) {
			basecommondao.deleteEntity(module);
			List<RT_UNIT_MODULEMANAGER> list_modulemanager = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
					"MODULEID='" + id + "'");
			if (list_modulemanager != null && list_modulemanager.size() > 0) {
				for (RT_UNIT_MODULEMANAGER manager : list_modulemanager) {
					basecommondao.deleteEntity(manager);
				}
			}
			List<RT_UNIT_PAGEMANAGER> list_pagemanager = basecommondao.getDataList(RT_UNIT_PAGEMANAGER.class,
					"MODULEID='" + id + "'");
			if (list_pagemanager != null && list_pagemanager.size() > 0) {
				for (RT_UNIT_PAGEMANAGER manager : list_pagemanager) {
					basecommondao.deleteEntity(manager);
				}
			}
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元模块定义！");
		}
		return msg;
	}

	/***************************************** 单元模块定义 *****************************************/

	/***************************************** 单元模块配置 *****************************************/

	/**
	 * 单元模块配置页面(URL:"/modulemanager/index/",Method：GET)
	 * 
	 * @Title: getModuleManagerIndex
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/modulemanager/index/")
	public String getModuleManagerIndex() {
		return prefix + "unitpagemanager/modulemanager";
	}

	/**
	 * 获取单元模块字段列表(URL:"/modulemanager/",Method：GET)
	 * 
	 * @Title: GetModuleManager
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/modulemanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetModuleManager(HttpServletRequest request, HttpServletResponse response) {
		String moduleid = "";
		try {
			moduleid = RequestHelper.getParam(request, "moduleid");
		} catch (Exception e) {
		}

		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND MODULEID='" + moduleid + "'");
		hqlBuilder.append(" ORDER BY SXH ");
		List<RT_UNIT_MODULEMANAGER> list = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
				hqlBuilder.toString());
		long l = 0;
		if (list != null && list.size() > 0) {
			l = list.size();
		}
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 新增或保存单元模块字段 (URL:"/modulemanager/{moduleid}/",Method：POST)
	 * 
	 * @Title: AddOrUpdateModuleManager
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/modulemanager/{moduleid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateModuleManager(@PathVariable("moduleid") String moduleid,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(moduleid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到单元模块定义！");
		}
		String id = request.getParameter("id");
		String bdcdylx = request.getParameter("bdcdylx");
		String fieldname = request.getParameter("fieldname");
		String fielddescription = request.getParameter("fielddescription");
		String fieldtype = request.getParameter("fieldtype").toLowerCase();
		String fieldoption = request.getParameter("fieldoption").toUpperCase();
		String number_l = request.getParameter("number_L");
		String number_r = request.getParameter("number_R");
		String visible = request.getParameter("visible");
		String editable = request.getParameter("editable");
		String colspan = request.getParameter("colspan");
		String required = request.getParameter("requierd");
		RT_UNIT_MODULEMANAGER modulemanager = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			modulemanager = basecommondao.get(RT_UNIT_MODULEMANAGER.class, id);
			bnew = false;
		}
		if (modulemanager == null) {
			modulemanager = new RT_UNIT_MODULEMANAGER();
			id = SuperHelper.GeneratePrimaryKey();
			modulemanager.setId(id);
			List<Map> list_sxh = basecommondao.getDataListByFullSql(
					"SELECT MAX(SXH) AS SXH FROM BDCK.RT_UNIT_MODULEMANAGER WHERE MODULEID='" + moduleid + "'");
			Integer sxh = 1;
			if (list_sxh != null && list_sxh.size() > 0 && !StringHelper.isEmpty(list_sxh.get(0).get("SXH"))) {
				sxh = StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh = sxh + 1;
			}
			modulemanager.setSXH(sxh);
		}
		modulemanager.setBDCDYLX(bdcdylx);
		modulemanager.setFIELDNAME(fieldname);
		modulemanager.setFIELDDESCRIPTION(fielddescription);
		modulemanager.setFIELDTYPE(fieldtype);
		modulemanager.setFIELDOPTION(fieldoption);
		modulemanager.setMODULEID(moduleid);
		if (!StringHelper.isEmpty(number_l)) {
			modulemanager.setNUMBER_L(StringHelper.getInt(number_l));
		}
		if (!StringHelper.isEmpty(number_r)) {
			modulemanager.setNUMBER_R(StringHelper.getInt(number_r));
		}
		modulemanager.setVISIBLE(visible);
		modulemanager.setEDITABLE(editable);
		modulemanager.setREQUIERD(required);
		if (!StringHelper.isEmpty(colspan)) {
			modulemanager.setCOLSPAN(StringHelper.getInt(colspan));
		}
		if (bnew)
			basecommondao.save(modulemanager);
		else
			basecommondao.update(modulemanager);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除单元模块字段 (URL:"/modulemanager/{id}",Method：DELETE)
	 * 
	 * @Title: RemoveModuleManager
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/modulemanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveModuleManager(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		RT_UNIT_MODULEMANAGER modulemanager = basecommondao.get(RT_UNIT_MODULEMANAGER.class, id);
		if (modulemanager != null) {
			basecommondao.deleteEntity(modulemanager);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元字段定义！");
		}
		return msg;
	}

	/**
	 * 重置模块配置中字段顺序 (URL:"/modulemanager/{moduleid}/resetsxh、",Method：POST)
	 * 
	 * @Title: ResetSXHOnModule
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/modulemanager/{moduleid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnModule(@PathVariable("moduleid") String moduleid,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(moduleid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到单元模块定义！");
		}

		String json = request.getParameter("info");
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String id = StringHelper.formatObject(object.get("id"));
			Integer sxh = StringHelper.getInt(object.get("sxh"));
			RT_UNIT_MODULEMANAGER modulemanager = basecommondao.get(RT_UNIT_MODULEMANAGER.class, id);
			if (modulemanager != null) {
				modulemanager.setSXH(sxh + 1);
				basecommondao.update(modulemanager);
			}
		}

		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		return msg;
	}

	/**
	 * 获取单元字段定义列表(URL:"/fielddefineoption/",Method：GET)
	 * 
	 * @Title: GetFieldDefineOption
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/fielddefineoption/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, List<T_UNIT_FIELD>> GetFieldDefineOption(HttpServletRequest request,
			HttpServletResponse response) {
		HashMap<String, List<T_UNIT_FIELD>> map = new HashMap<String, List<T_UNIT_FIELD>>();
		List<T_UNIT_FIELD> list_field = basecommondao.getDataList(T_UNIT_FIELD.class, " 1>0 ORDER BY BDCDYLX");
		if (list_field != null && list_field.size() > 0) {
			for (T_UNIT_FIELD field : list_field) {
				String bdcylx = field.getBDCDYLX();
				if (StringHelper.isEmpty(bdcylx)) {
					continue;
				}
				if (map.containsKey(bdcylx)) {
					List<T_UNIT_FIELD> list = map.get(bdcylx);
					list.add(field);
					map.remove(bdcylx);
					map.put(bdcylx, list);
				} else {
					List<T_UNIT_FIELD> list = new ArrayList<T_UNIT_FIELD>();
					list.add(field);
					map.put(bdcylx, list);
				}
			}
		}
		return map;
	}

	/***************************************** 单元模块配置 *****************************************/

	/***************************************** 单元页面定义 *****************************************/

	/**
	 * 单元页面定义页面(URL:"/pagedefine/index/",Method：GET)
	 * 
	 * @Title: getPageDefineIndex
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagedefine/index/")
	public String getPageDefineIndex() {
		return prefix + "unitpagemanager/pagedefine";
	}

	/**
	 * 获取单元页面定义列表(URL:"/pagedefine/",Method：GET)
	 * 
	 * @Title: GetPageDefine
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagedefine/", method = RequestMethod.GET)
	public @ResponseBody Message GetPageDefine(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter = "";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}

		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND NAME LIKE '%").append(filter).append("%'");
		}
		hqlBuilder.append(" ORDER BY NAME ");
		Page p = basecommondao.getPageDataByHql(T_UNIT_PAGE.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 获取单元页面定义列表(URL:"/pagedefine/",Method：GET)
	 * 
	 * @Title: GetPageDefine
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagedefinelist/", method = RequestMethod.GET)
	public @ResponseBody List<T_UNIT_PAGE> GetPageDefineList(HttpServletRequest request, HttpServletResponse response) {
		List<T_UNIT_PAGE> list = basecommondao.getDataList(T_UNIT_PAGE.class, "1>0 ORDER BY ID");
		return list;
	}

	/**
	 * 新增或保存单元页面定义 (URL:"/pagedefine/",Method：POST)
	 * 
	 * @Title: AddOrUpdatePageDefine
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagedefine/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdatePageDefine(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		T_UNIT_PAGE module = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			module = basecommondao.get(T_UNIT_PAGE.class, id);
			bnew = false;
		}
		if (module == null) {
			module = new T_UNIT_PAGE();
			id = SuperHelper.GeneratePrimaryKey();
			module.setId(id);
		}
		module.setNAME(name);
		if (bnew)
			basecommondao.save(module);
		else
			basecommondao.update(module);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除单元页面定义 (URL:"/pagedefine/{id}",Method：DELETE)
	 * 
	 * @Title: RemovePageDefine
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagedefine/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemovePageDefine(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_UNIT_PAGE page = basecommondao.get(T_UNIT_PAGE.class, id);
		if (page != null) {
			basecommondao.deleteEntity(page);
			List<RT_UNIT_PAGEMANAGER> list = basecommondao.getDataList(RT_UNIT_PAGEMANAGER.class,
					"PAGEID='" + id + "'");
			if (list != null && list.size() > 0) {
				for (RT_UNIT_PAGEMANAGER manager : list) {
					basecommondao.deleteEntity(manager);
				}
			}
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元页面定义！");
		}
		return msg;
	}

	/***************************************** 单元页面定义 *****************************************/

	/***************************************** 单元页面配置 *****************************************/

	/**
	 * 单元页面配置页面(URL:"/pagemanager/index/",Method：GET)
	 * 
	 * @Title: getPageManagerIndex
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagemanager/index/")
	public String getPageManagerIndex() {
		return prefix + "unitpagemanager/pagemanager";
	}

	/**
	 * 获取单元页面定义列表(URL:"/pagemanager/",Method：GET)
	 * 
	 * @Title: GetPageManager
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pagemanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetPageManager(HttpServletRequest request, HttpServletResponse response) {
		String pageid = "";
		try {
			pageid = RequestHelper.getParam(request, "pageid");
		} catch (Exception e) {
		}

		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(
				"SELECT RT.ID,RT.MODULEID,RT.SXH,RT.TITLEVISIBLE,RT.TITLE,T.NAME AS MODULENAME,RT.EDITABLE AS EDITABLE ");
		hqlBuilder.append("FROM BDCK.RT_UNIT_PAGEMANAGER RT ");
		hqlBuilder.append("LEFT JOIN BDCK.T_UNIT_MODULE T ");
		hqlBuilder.append("ON RT.MODULEID=T.ID ");
		hqlBuilder.append("WHERE 1>0 AND RT.PAGEID='" + pageid + "'");
		hqlBuilder.append(" ORDER BY RT.SXH ");
		List<Map> list = basecommondao.getDataListByFullSql(hqlBuilder.toString());
		long l = 0;
		if (list != null && list.size() > 0) {
			l = list.size();
		}
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 新增或保存单元页面模块 (URL:"/pagemanager/{moduleid}/",Method：POST)
	 * 
	 * @Title: AddOrUpdatePageManager
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pagemanager/{pageid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdatePageManager(@PathVariable("pageid") String pageid,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(pageid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到单元页面定义！");
		}
		String id = request.getParameter("ID");
		String titlevisible = request.getParameter("TITLEVISIBLE");
		String editable = request.getParameter("EDITABLE");
		String title = request.getParameter("TITLE");
		String moduleid = request.getParameter("MODULEID");
		RT_UNIT_PAGEMANAGER modulemanager = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			modulemanager = basecommondao.get(RT_UNIT_PAGEMANAGER.class, id);
			bnew = false;
		}
		if (modulemanager == null) {
			modulemanager = new RT_UNIT_PAGEMANAGER();
			id = SuperHelper.GeneratePrimaryKey();
			modulemanager.setId(id);
			modulemanager.setPAGEID(pageid);
			List<Map> list_sxh = basecommondao.getDataListByFullSql(
					"SELECT MAX(SXH) AS SXH FROM BDCK.RT_UNIT_PAGEMANAGER WHERE PAGEID='" + pageid + "'");
			Integer sxh = 1;
			if (list_sxh != null && list_sxh.size() > 0 && !StringHelper.isEmpty(list_sxh.get(0).get("SXH"))) {
				sxh = StringHelper.getInt(list_sxh.get(0).get("SXH"));
				sxh = sxh + 1;
			}
			modulemanager.setSXH(sxh);
		}
		modulemanager.setTITLE(title);
		modulemanager.setMODULEID(moduleid);
		modulemanager.setTITLEVISIBLE(titlevisible);
		modulemanager.setEDITABLE(editable);
		if (bnew)
			basecommondao.save(modulemanager);
		else
			basecommondao.update(modulemanager);
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除单元模块(URL:"/pagemanager/{id}",Method：DELETE)
	 * 
	 * @Title: RemovePageManager
	 * @author:俞学斌 @date：2016年05月11日 11:22:28
	 * @return
	 */
	@RequestMapping(value = "/pagemanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemovePageManager(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		RT_UNIT_PAGEMANAGER modulemanager = basecommondao.get(RT_UNIT_PAGEMANAGER.class, id);
		if (modulemanager != null) {
			basecommondao.deleteEntity(modulemanager);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元模块！");
		}
		return msg;
	}

	/**
	 * 重置页面配置中模块顺序 (URL:"/pagemanager/{pageid}/resetsxh、",Method：POST)
	 * 
	 * @Title: ResetSXHOnPage
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/pagemanager/{pageid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnPage(@PathVariable("pageid") String pageid, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(pageid)) {
			msg.setSuccess("true");
			msg.setMsg("保存失败！未获取到单元页面定义！");
		}

		String json = request.getParameter("info");
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String id = StringHelper.formatObject(object.get("id"));
			Integer sxh = StringHelper.getInt(object.get("sxh"));
			RT_UNIT_PAGEMANAGER pagemanager = basecommondao.get(RT_UNIT_PAGEMANAGER.class, id);
			if (pagemanager != null) {
				pagemanager.setSXH(sxh + 1);
				basecommondao.update(pagemanager);
			}
		}

		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("重置顺序成功！");
		return msg;
	}

	/**
	 * 获取单元字段定义列表(URL:"/fielddefineoption/",Method：GET)
	 * 
	 * @Title: GetFieldDefineOption
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@RequestMapping(value = "/moduledefineoption/", method = RequestMethod.GET)
	public @ResponseBody List<T_UNIT_MODULE> GetModuleDefineOption(HttpServletRequest request,
			HttpServletResponse response) {
		List<T_UNIT_MODULE> list_module = basecommondao.getDataList(T_UNIT_MODULE.class, " 1>0 ORDER BY BDCDYLX,NAME");
		return list_module;
	}

	/***************************************** 单元页面模块配置 *****************************************/

	/**
	 * 获取模块配置信息(URL:"/moduleinfo/{moduleid}/",Method：GET)
	 * 
	 * @Title: GetModuleInfo
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/moduleinfo/{moduleid}/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> GetModuleInfo(@PathVariable("moduleid") String moduleid,
			HttpServletRequest request, HttpServletResponse response) {
		String readonly = "true";
		try {
			readonly = RequestHelper.getParam(request, "readonly");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Map> consts = new HashMap<String, Map>();
		T_UNIT_MODULE module = basecommondao.get(T_UNIT_MODULE.class, moduleid);
		if (module != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("TITLE", module.getNAME());
			map.put("TITLEVISIBLE", 1);
			map.put("MODULEID", module.getId());
			boolean ViewSaveBtn = false;

			List<RT_UNIT_MODULEMANAGER> list_showfield = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
					"VISIBLE='1' AND MODULEID='" + moduleid + "' ORDER BY SXH");
			if (list_showfield != null && list_showfield.size() > 0) {
				List<HashMap<String, String>> showfields = new ArrayList<HashMap<String, String>>();
				int long_tr = 0;
				for (RT_UNIT_MODULEMANAGER field : list_showfield) {
					HashMap<String, String> showfield = new HashMap<String, String>();
					if (StringHelper.isEmpty(field.getREQUIERD())) {
						showfield.put("REQUIERD", "0");
					} else {
						showfield.put("REQUIERD", field.getREQUIERD());
					}

					if (StringHelper.isEmpty(field.getREQUIERD()) || "0".equals(field.getREQUIERD())) {
						showfield.put("COLOR", "black");
					} else {
						showfield.put("COLOR", "red");
					}

					if ("true".equals(readonly)) {
						showfield.put("EDITABLE", "0");
					} else {
						if (StringHelper.isEmpty(field.getEDITABLE())) {
							showfield.put("EDITABLE", "0");
						} else {
							showfield.put("EDITABLE", field.getEDITABLE());
							if ("1".equals(field.getEDITABLE())) {
								ViewSaveBtn = true;
							}
						}
					}

					if (StringHelper.isEmpty(field.getVISIBLE())) {
						showfield.put("VISIBLE", "0");
					} else {
						showfield.put("VISIBLE", field.getVISIBLE());
					}
					showfield.put("BDCDYLX", field.getBDCDYLX());
					String entityname = getEntityName(field.getBDCDYLX());
					showfield.put("ENTITYNAME", entityname);
					showfield.put("FIELDNAME", field.getFIELDNAME());
					showfield.put("FIELDTYPE", field.getFIELDTYPE());
					showfield.put("FIELDDESCRIPTION", field.getFIELDDESCRIPTION());
					showfield.put("FIELDOPTION", field.getFIELDOPTION());
					showfield.put("NUMBER_L", StringHelper.formatObject(field.getNUMBER_L()));
					showfield.put("NUMBER_R", StringHelper.formatObject(field.getNUMBER_R()));
					showfield.put("COLSPAN", StringHelper.formatObject(field.getCOLSPAN()));
					showfield.put("ID", field.getId());
					if ("ZL".equals(field.getFIELDNAME())) {
						if (BDCDYLX.H.Value.equals(field.getBDCDYLX())
								|| BDCDYLX.YCH.Value.equals(field.getBDCDYLX())
								|| BDCDYLX.SHYQZD.Value.equals(field.getBDCDYLX())
								|| BDCDYLX.SYQZD.Value.equals(field.getBDCDYLX())
								|| BDCDYLX.LD.Value.equals(field.getBDCDYLX())) {
							showfield.put("SHOWHISTORY", "true");
							showfield.put("SHOWHISTORY", "true");
						} else {
							showfield.put("SHOWHISTORY", "false");
						}
					} else {
						showfield.put("SHOWHISTORY", "false");
					}
					if ("combox".equals(field.getFIELDTYPE())) {
						if (!consts.containsKey(field.getFIELDOPTION())) {
							Map _const = ConstHelper.getDictionary(field.getFIELDOPTION());
							consts.put(field.getFIELDOPTION(), _const);
						}
					}
					if (long_tr == 0) {
						showfield.put("STATUS", "start");
						long_tr = long_tr + 1 + field.getCOLSPAN();
					} else {
						if ((long_tr + 1 + field.getCOLSPAN()) > 4) {
							long_tr = 1 + field.getCOLSPAN();
							showfield.put("STATUS", "start");
						} else {
							long_tr = long_tr + 1 + field.getCOLSPAN();
						}
					}
					showfields.add(showfield);
				}
				map.put("SHOWFIELDS", showfields);
			}
			map.put("VIEWSAVEBTN", ViewSaveBtn);

			List<RT_UNIT_MODULEMANAGER> list_hiddenfield = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
					"(VISIBLE IS NULL OR VISIBLE='0') AND MODULEID='" + moduleid + "' ORDER BY SXH");
			if (list_hiddenfield != null && list_hiddenfield.size() > 0) {
				List<HashMap<String, String>> hiddenfields = new ArrayList<HashMap<String, String>>();
				for (RT_UNIT_MODULEMANAGER field : list_hiddenfield) {
					HashMap<String, String> hiddenfield = new HashMap<String, String>();
					String entityname = getEntityName(field.getBDCDYLX());
					hiddenfield.put("ENTITYNAME", entityname);
					hiddenfield.put("FIELDNAME", field.getFIELDNAME());
					hiddenfield.put("ID", field.getId());
					hiddenfields.add(hiddenfield);
				}
				map.put("HIDDENFIELDS", hiddenfields);
			}

			T_UNIT_MODULE module_define = basecommondao.get(T_UNIT_MODULE.class, moduleid);
			map.put("ENTITYNAME", getEntityName(module_define.getBDCDYLX()));
			map.put("BDCDYLX", module_define.getBDCDYLX());
			map.put("VIEWSAVEBTN", ViewSaveBtn);
			list.add(map);
		}

		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("success", "true");
		m.put("total", list.size());
		m.put("rows", list);
		m.put("msg", "成功！");
		m.put("consts", consts);
		return m;
	}

	/**
	 * 获取页面配置信息(URL:"/pageinfo/{pageid}/",Method：GET)
	 * 
	 * @Title: GetPageInfo
	 * @author:俞学斌 @date：2016年04月12日 17:34:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pageinfo/{pageid}/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> GetPageInfo(@PathVariable("pageid") String pageid,
			HttpServletRequest request, HttpServletResponse response) {
		String readonly = "true";
		String bdcdylx = "";
		String xmbh = "";
		String xmlb = "";
		try {
			readonly = RequestHelper.getParam(request, "readonly");
			bdcdylx = RequestHelper.getParam(request, "bdcdylx");
			xmbh = RequestHelper.getParam(request, "xmbh");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!StringHelper.isEmpty(bdcdylx) && !StringHelper.isEmpty(xmbh)) {
			if (bdcdylx.equals("99")) {
				xmlb = getXMLB(xmbh);
			}
		}
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Map> consts = new HashMap<String, Map>();
		List<RT_UNIT_PAGEMANAGER> list_module = basecommondao.getDataList(RT_UNIT_PAGEMANAGER.class,
				"PAGEID='" + pageid + "' ORDER BY SXH");
		if (list_module != null && list_module.size() > 0) {
			String truelx = "";
			for (RT_UNIT_PAGEMANAGER module : list_module) {
				String moduleid = module.getMODULEID();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("TITLE", module.getTITLE());
				map.put("TITLEVISIBLE", module.getTITLEVISIBLE());
				map.put("MODULEID", moduleid);
				boolean ViewSaveBtn = false;
				boolean ViewTDYTList = false;
				boolean EditTDYTList = false;
				boolean ViewYHZKList = false;
				boolean EditYHZKList = false;
				boolean ViewYHYDZBList = false;
				boolean EditYHYDZBList = false;

				List<RT_UNIT_MODULEMANAGER> list_showfield = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
						"VISIBLE='1' AND MODULEID='" + moduleid + "' ORDER BY SXH");
				if (list_showfield != null && list_showfield.size() > 0) {
					List<HashMap<String, String>> showfields = new ArrayList<HashMap<String, String>>();
					List<HashMap<String, String>> tdytfields = new ArrayList<HashMap<String, String>>();
					List<HashMap<String, String>> yhzkfields = new ArrayList<HashMap<String, String>>();
					List<HashMap<String, String>> yhydzbfields = new ArrayList<HashMap<String, String>>();
					int long_tr = 0;
					int long_tdyt_tr = 0;
					int long_yhzk_tr = 0;
					int long_yhydzb_tr = 0;
					truelx = list_showfield.get(0).getBDCDYLX();
					for (RT_UNIT_MODULEMANAGER field : list_showfield) {
						HashMap<String, String> showfield = new HashMap<String, String>();
						if (StringHelper.isEmpty(field.getREQUIERD())) {
							showfield.put("REQUIERD", "0");
						} else {
							showfield.put("REQUIERD", field.getREQUIERD());
						}

						if (StringHelper.isEmpty(field.getREQUIERD()) || "0".equals(field.getREQUIERD())) {
							showfield.put("COLOR", "black");
						} else {
							showfield.put("COLOR", "red");
						}

						if ("true".equals(readonly) || (!"1".equals(module.getEDITABLE()))) {
							showfield.put("EDITABLE", "0");
						} else {
							if (StringHelper.isEmpty(field.getEDITABLE())) {
								showfield.put("EDITABLE", "0");
							} else {
								showfield.put("EDITABLE", field.getEDITABLE());
							}
						}

						if (StringHelper.isEmpty(field.getVISIBLE())) {
							showfield.put("VISIBLE", "0");
						} else {
							showfield.put("VISIBLE", field.getVISIBLE());
						}
						showfield.put("BDCDYLX", field.getBDCDYLX());
						String entityname = getEntityName(field.getBDCDYLX());
						showfield.put("ENTITYNAME", entityname);
						showfield.put("FIELDNAME", field.getFIELDNAME());
						showfield.put("FIELDTYPE", field.getFIELDTYPE());
						showfield.put("FIELDDESCRIPTION", field.getFIELDDESCRIPTION());
						showfield.put("FIELDOPTION", field.getFIELDOPTION());
						showfield.put("NUMBER_L", StringHelper.formatObject(field.getNUMBER_L()));
						showfield.put("NUMBER_R", StringHelper.formatObject(field.getNUMBER_R()));
						showfield.put("COLSPAN", StringHelper.formatObject(field.getCOLSPAN()));
						showfield.put("ID", field.getId());
						if ("ZL".equals(field.getFIELDNAME())) {
							if (BDCDYLX.H.Value.equals(field.getBDCDYLX())
									|| BDCDYLX.YCH.Value.equals(field.getBDCDYLX())
									|| BDCDYLX.SHYQZD.Value.equals(field.getBDCDYLX())
									|| BDCDYLX.SYQZD.Value.equals(field.getBDCDYLX())
									|| BDCDYLX.LD.Value.equals(field.getBDCDYLX())
									|| BDCDYLX.NYD.Value.equals(field.getBDCDYLX())) {
								showfield.put("SHOWHISTORY", "true");
								showfield.put("SHOWHISTORY", "true");
							} else {
								showfield.put("SHOWHISTORY", "false");
							}
						} else {
							showfield.put("SHOWHISTORY", "false");
						}
						if ("combox".equals(field.getFIELDTYPE())) {
							if (!consts.containsKey(field.getFIELDOPTION())) {
								Map _const = ConstHelper.getDictionary(field.getFIELDOPTION());
								consts.put(field.getFIELDOPTION(), _const);
							}
						}
						if (!"tdyt".equals(field.getBDCDYLX()) && !"yhzk".equals(field.getBDCDYLX())
								&& !"yhydzb".equals(field.getBDCDYLX())) {
							if (long_tr == 0) {
								showfield.put("STATUS", "start");
								long_tr = long_tr + 1 + field.getCOLSPAN();
							} else {
								if ((long_tr + 1 + field.getCOLSPAN()) > 4) {
									long_tr = 1 + field.getCOLSPAN();
									showfield.put("STATUS", "start");
								} else {
									long_tr = long_tr + 1 + field.getCOLSPAN();
								}
							}
						}
						if ("tdyt".equals(field.getBDCDYLX())) {
							if (long_tdyt_tr == 0) {
								showfield.put("STATUS", "start");
								long_tdyt_tr = long_tdyt_tr + 1 + field.getCOLSPAN();
							} else {
								if ((long_tdyt_tr + 1 + field.getCOLSPAN()) > 4) {
									long_tdyt_tr = 1 + field.getCOLSPAN();
									showfield.put("STATUS", "start");
								} else {
									long_tdyt_tr = long_tdyt_tr + 1 + field.getCOLSPAN();
								}
							}
							ViewTDYTList = true;
							if ("1".equals(showfield.get("EDITABLE"))) {
								EditTDYTList = true;
							}
							tdytfields.add(showfield);
							continue;
						}
						if ("yhzk".equals(field.getBDCDYLX())) {
							if (long_yhzk_tr == 0) {
								showfield.put("STATUS", "start");
								long_yhzk_tr = long_yhzk_tr + 1 + field.getCOLSPAN();
							} else {
								if ((long_yhzk_tr + 1 + field.getCOLSPAN()) > 4) {
									long_yhzk_tr = 1 + field.getCOLSPAN();
									showfield.put("STATUS", "start");
								} else {
									long_yhzk_tr = long_yhzk_tr + 1 + field.getCOLSPAN();
								}
							}
							ViewYHZKList = true;
							if ("1".equals(showfield.get("EDITABLE"))) {
								EditYHZKList = true;
							}
							yhzkfields.add(showfield);
							continue;
						}
						if ("yhydzb".equals(field.getBDCDYLX())) {
							if (long_yhydzb_tr == 0) {
								showfield.put("STATUS", "start");
								long_yhydzb_tr = long_yhydzb_tr + 1 + field.getCOLSPAN();
							} else {
								if ((long_yhydzb_tr + 1 + field.getCOLSPAN()) > 4) {
									long_yhydzb_tr = 1 + field.getCOLSPAN();
									showfield.put("STATUS", "start");
								} else {
									long_yhydzb_tr = long_yhydzb_tr + 1 + field.getCOLSPAN();
								}
							}
							ViewYHYDZBList = true;
							if ("1".equals(showfield.get("EDITABLE"))) {
								EditYHYDZBList = true;
							}
							yhydzbfields.add(showfield);
							continue;
						}
						if ("1".equals(showfield.get("EDITABLE"))) {
							ViewSaveBtn = true;
						}
						showfields.add(showfield);
					}
					map.put("SHOWFIELDS", showfields);
					map.put("TDYTFIELDS", tdytfields);
					map.put("YHZKFIELDS", yhzkfields);
					map.put("YHYDZBFIELDS", yhydzbfields);
				}
				map.put("VIEWSAVEBTN", ViewSaveBtn);
				map.put("VIEWTDYTLIST", ViewTDYTList);
				map.put("EDITTDYTLIST", EditTDYTList);
				map.put("VIEWYHZKLIST", ViewYHZKList);
				map.put("EDITYHZKLIST", EditYHZKList);
				map.put("VIEWYHYDZBLIST", ViewYHYDZBList);
				map.put("EDITYHYDZBLIST", EditYHYDZBList);

				List<RT_UNIT_MODULEMANAGER> list_hiddenfield = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
						"(VISIBLE IS NULL OR VISIBLE='0') AND MODULEID='" + moduleid + "' ORDER BY SXH");
				if (list_hiddenfield != null && list_hiddenfield.size() > 0) {
					List<HashMap<String, String>> hiddenfields = new ArrayList<HashMap<String, String>>();
					for (RT_UNIT_MODULEMANAGER field : list_hiddenfield) {
						HashMap<String, String> hiddenfield = new HashMap<String, String>();
						String entityname = getEntityName(field.getBDCDYLX());
						hiddenfield.put("ENTITYNAME", entityname);
						hiddenfield.put("FIELDNAME", field.getFIELDNAME());
						hiddenfield.put("ID", field.getId());
						hiddenfields.add(hiddenfield);
					}
					map.put("HIDDENFIELDS", hiddenfields);
				}
				T_UNIT_MODULE module_define = basecommondao.get(T_UNIT_MODULE.class, moduleid);
				map.put("ENTITYNAME", getEntityName(module_define.getBDCDYLX()));
				map.put("BDCDYLX", module_define.getBDCDYLX());
				list.add(map);
				if (!StringHelper.isEmpty(xmlb) && !xmlb.equals(truelx)) {
					list.remove(list.size()-1);
					if (xmlb.equals("03") && truelx.equals("031") || xmlb.equals("031") && truelx.equals("03")) {
						list.add(map);
					}
				}
			}
		}
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("success", "true");
		m.put("total", list.size());
		m.put("rows", list);
		m.put("msg", "成功！");
		m.put("consts", consts);
		return m;
	}

	public String getXMLB (String xmbh){
		String xmlb = "";
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		DCS_DCXM dcxm = null;
		if (xmxx != null) {
			dcxm = basecommondao.get(DCS_DCXM.class, xmxx.getDCXMID());
			if (dcxm != null) {
				xmlb = dcxm.getXMLB();
			}
		}
		return xmlb;
	}
	
	/**
	 * 检查不动产单元是否匹配并更新
	 * @param bdcdylx
	 * @param bdcdyid
	 * @param ly
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value="/checkbdcdyh/{bdcdylx}/{bdcdyid}/{ly}/{xmbh}",method =RequestMethod.GET)
	public @ResponseBody Message checkBdcdyh(@PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("bdcdyid") String bdcdyid,@PathVariable("ly") String ly,
			@PathVariable("xmbh") String xmbh){
		Message msg=new Message();
	    msg.setMsg("更新失败");
	    msg.setSuccess("false");
		RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
		if(unit !=null){
			House house=(House) unit;
			String zrzbdcdyid=house.getZRZBDCDYID();
			String zdbdcdyid=house.getZDBDCDYID();
			String bdcdyh=house.getBDCDYH();
			String zrzbdcdyh="";
			String zdbdcdyh="";
			BDCDYLX lx=BDCDYLX.YCZRZ;
			if(BDCDYLX.H.Value.equals(bdcdylx)){
				lx=BDCDYLX.ZRZ;
			}
			Building building= (Building)UnitTools.loadUnit(lx, DJDYLY.LS, zrzbdcdyid);
			if(building ==null){
				if(BDCDYLX.H.Value.equals(bdcdylx)){
					building= (Building)UnitTools.loadUnit(lx, DJDYLY.GZ, zrzbdcdyid);
				}else{
					building= (Building)UnitTools.loadUnit(lx, DJDYLY.XZ, zrzbdcdyid);
				}
			}
			if(building !=null){
				zrzbdcdyh=building.getBDCDYH();
			}else{
				msg.setSuccess("false");
				msg.setMsg("不存在自然幢数据");
				return msg;
			}
			RealUnit  land = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
			if(land ==null){
				land =UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
			}
			if(land !=null){
				zdbdcdyh=land.getBDCDYH();
			}else{
				msg.setSuccess("false");
				msg.setMsg("不存在使用权宗地数据");
				return msg;
			}
			/*地bdcdyh前19位=幢bdcdyh前19位
			        户bdcdyh前24位=幢bdcdyh前24位
			   bdcdyh生成规则与权集系统生成bdcdyh的规则保持一致。*/
			if(StringHelper.isEmpty(zdbdcdyh)){
				msg.setSuccess("false");
				msg.setMsg("宗地的不动产单元号为空，不能重新生成户的不动产单元号");
				return msg;
			}else{
				if(StringHelper.isEmpty(zrzbdcdyh)){
					msg.setSuccess("false");
					msg.setMsg("自然幢的不动产单元号为空，不能重新生成户的不动产单元号");
					return msg;
				}else{
					if(zdbdcdyh.length()>=19 && zrzbdcdyh.length()>=19){
						String tempzrzbdcdyh=zrzbdcdyh.substring(0,19);
						String tempzdbdcdyh=zrzbdcdyh.substring(0,19);
						if(tempzrzbdcdyh.equals(tempzdbdcdyh)){
							if(zrzbdcdyh.length()>=24 && bdcdyh.length()>=24){
								tempzrzbdcdyh=zrzbdcdyh.substring(0,24);
								String tembdcdyh=bdcdyh.substring(0,24);
								if(tempzrzbdcdyh.equals(tembdcdyh)){
									msg.setSuccess("false");
									msg.setMsg("户幢地不动产单元号生成是正确的");
									return msg;
								}else{
									msg.setMsg("户幢的不动产单元号关联有问题，是否从新生成户的不动产单元号");
									msg.setSuccess("true");
									return msg;
								}
							}else{
								if(zrzbdcdyh.length()<24){
								msg.setMsg("false");
								msg.setMsg("幢不动产单元有问题，请维护");
								}else {
									msg.setMsg("户幢的不动产单元号关联有问题，是否从新生成户的不动产单元号");
									msg.setSuccess("true");
									return msg;
								}
							}
						}else{
							msg.setSuccess("false");
							msg.setMsg("地和幢不动产单元号关联错误，请维护数据");
							return msg;
						}
					}else{
						msg.setSuccess("false");
						msg.setMsg("地和幢不动产单元号关联错误，请维护数据");
						return msg;
					}
			}
		}
		}else{
			msg.setSuccess("false");
			msg.setMsg("不存在户现状信息");
			return msg;
		}
	    return msg;
	}
	@RequestMapping(value="/updatebdcdyh/{bdcdylx}/{bdcdyid}/{ly}/{xmbh}",method =RequestMethod.GET)
	public @ResponseBody Message UpdateBdcdyh(@PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("bdcdyid") String bdcdyid,@PathVariable("ly") String ly,
			@PathVariable("xmbh") String xmbh){
		Message msg=new Message();
	    msg.setMsg("不动产单元号维护失败");
	    msg.setSuccess("false");
	    RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
		if(unit !=null){
			 if(BDCDYLX.H.Value.equals(bdcdylx) && BDCDYLX.YCH.Value.equals(bdcdylx)){
				 House house=(House) unit;
					String zrzbdcdyid=house.getZRZBDCDYID();
					String zrzbdcdyh="";
					BDCDYLX lx=BDCDYLX.YCZRZ;
					if(BDCDYLX.H.Value.equals(bdcdylx)){
						lx=BDCDYLX.ZRZ;
					}
					Building building= (Building)UnitTools.loadUnit(lx, DJDYLY.LS, zrzbdcdyid);
					if(building ==null){
						if(BDCDYLX.YCH.Value.equals(bdcdylx)){
							building= (Building)UnitTools.loadUnit(lx, DJDYLY.XZ, zrzbdcdyid);	
						}else{
							building= (Building)UnitTools.loadUnit(lx, DJDYLY.GZ, zrzbdcdyid);
						}
					}
					if(building !=null){
						zrzbdcdyh=building.getBDCDYH();
						String tempzrzbdcdyh=zrzbdcdyh.substring(0,24);
						String  tembdcdyh=ProjectHelper.CreatBDCDYH(tempzrzbdcdyh,"04");
						 boolean flag= BackBDCDYInfo(xmbh,BDCDYLX.initFrom(bdcdylx),DJDYLY.initFrom(ly),bdcdyid);
					     if(flag){
					    	 house.setBDCDYH(tembdcdyh);
					    	 basecommondao.update(house);
					    	 House lsunit=(House)UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
					    	 if(lsunit !=null){
					    		 lsunit.setBDCDYH(tembdcdyh);
					    		 basecommondao.update(lsunit);
					    	 }
					    	 basecommondao.flush();
					    	   msg.setSuccess("true");
								msg.setMsg("不动产单元号维护成功");
								return msg;
					     }
					}
			    }else if(BDCDYLX.SHYQZD.Value.equals(bdcdylx)){
			    	UseLand land=(UseLand) unit;
			    	String zdbdcdyh=land.getBDCDYH().substring(0,14);
					String  tembdcdyh=ProjectHelper.CreatBDCDYH(zdbdcdyh,"01");
					 boolean flag= BackBDCDYInfo(xmbh,BDCDYLX.initFrom(bdcdylx),DJDYLY.initFrom(ly),bdcdyid);
				     if(flag){
				    	 land.setBDCDYH(tembdcdyh);
				    	 basecommondao.update(unit);
				    	 UseLand lsunit=(UseLand)UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
				    	 if(lsunit !=null){
				    		 lsunit.setBDCDYH(tembdcdyh);
				    		 basecommondao.update(lsunit);
				    	 }
				    	 basecommondao.flush();
				    	   msg.setSuccess("true");
							msg.setMsg("不动产单元号维护成功");
							return msg;
				     }
			    }
			
		}
	    return msg;
	}
	
	@RequestMapping(value="/updateGZbdcdyh/{bdcdylx}/{bdcdyid}/{ly}/{xmbh}",method =RequestMethod.GET)
	public @ResponseBody Message UpdateGZBdcdyh(@PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("bdcdyid") String bdcdyid,@PathVariable("ly") String ly,
			@PathVariable("xmbh") String xmbh){
		Message msg=new Message();
	    msg.setMsg("生成不动产单元号失败");
	    msg.setSuccess("false");
	    RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, bdcdyid);	   
		if(unit !=null){
			 if(BDCDYLX.SHYQZD.Value.equals(bdcdylx)){
			    	UseLand land=(UseLand) unit;
			    	String djzqdm=land.getDJZQDM();				    	
			    	if(!StringHelper.isEmpty(djzqdm)){
			    		String syqlx=land.getSYQLX();
			    		String zdtzm=land.getZDTZM();
			    		if(StringHelper.isEmpty(syqlx)){
			    			msg.setMsg("所有权类型为空，不能生成不动产单元号");
				    		return msg;	
			    		}
			    		if(StringHelper.isEmpty(zdtzm)){
			    			msg.setMsg("宗地特征码为空，不能生成不动产单元号");
				    		return msg;	
			    		}
			    		djzqdm=djzqdm+syqlx+zdtzm;
			    	}
			    	else{
			    		msg.setMsg("地籍子区代码为空，不能生成不动产单元号");
			    		return msg;
			    	}
					String  tembdcdyh=ProjectHelper.CreatBDCDYH(djzqdm,"01");
					 boolean flag= BackBDCDYInfo(xmbh,BDCDYLX.initFrom(bdcdylx),DJDYLY.initFrom(ly),bdcdyid);
				     if(flag){
				    	 land.setBDCDYH(tembdcdyh);
				    	 String zddm=tembdcdyh.substring(0,19);
				    	 land.setZDDM(zddm);				    	 
				    	 basecommondao.update(unit);
				    	 basecommondao.flush();
				    	   msg.setSuccess("true");
							msg.setMsg("生成不动产单元号成功");
							return msg;
				     }
			    }
			
		}
	    return msg;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pagedata/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String, Object>> LoadDYInfo(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> enum_status = new ArrayList<String>();
		enum_status.add("SeizureState");
		enum_status.add("MortgageState");
		enum_status.add("ObjectionState");
		enum_status.add("LimitState");
		enum_status.add("TransferNoticeState");
		enum_status.add("MortgageNoticeState");
		enum_status.add("AssociatedPeriodRoom");
		enum_status.add("AssociatedReadyRoom");
		enum_status.add("DYSW");
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String bdcdyid = "";
		try {
			bdcdyid = RequestHelper.getParam(request, "bdcdyid");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String bdcdylx = "";
		try {
			bdcdylx = RequestHelper.getParam(request, "bdcdylx");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String djdyly = "";
		try {
			djdyly = RequestHelper.getParam(request, "djdyly");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ("ls".equals(djdyly)) {
			djdyly = "03";
		} else if ("xz".equals(djdyly)) {
			djdyly = "02";
		} else if ("gz".equals(djdyly)) {
			djdyly = "01";
		}else if ("dc".equals(djdyly)) {
			djdyly = "04";
		}

		String pageid = "";
		try {
			pageid = RequestHelper.getParam(request, "pageid");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String xmbh = "";
		try {
			xmbh = RequestHelper.getParam(request, "xmbh");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (!StringHelper.isEmpty(xmbh) && !StringHelper.isEmpty(bdcdylx) && bdcdylx.equals("99")) { //审核项目
			bdcdylx = getXMLB(xmbh);
		}
		
		if (StringHelper.isEmpty(bdcdyid)) {
			return list;
		}
		if (StringHelper.isEmpty(djdyly)) {
			return list;
		}
		if (StringHelper.isEmpty(bdcdylx)) {
			return list;
		}
		HashMap<String, RealUnit> list_unit = getUnitList(bdcdylx, djdyly, bdcdyid, xmbh);
		HashMap<String, Object> list_unit_other = getOtherUnitList(bdcdylx, djdyly, bdcdyid, xmbh);
		HashMap<String, UnitStatus> list_status = null;

		List<RT_UNIT_PAGEMANAGER> list_module = basecommondao.getDataList(RT_UNIT_PAGEMANAGER.class,
				"PAGEID='" + pageid + "' ORDER BY SXH");
		if (list_module != null && list_module.size() > 0) {
			for (RT_UNIT_PAGEMANAGER module : list_module) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String moduleid = module.getMODULEID();

				T_UNIT_MODULE module_define = basecommondao.get(T_UNIT_MODULE.class, moduleid);
				if (module_define != null) {
					String module_bdcdylx = module_define.getBDCDYLX();
					map.put("BDCDYLX", module_bdcdylx);
					if (list_unit.containsKey(module_bdcdylx)) {
						RealUnit unit = list_unit.get(module_bdcdylx);
						if (unit != null) {
							map.put("DJDYLY", unit.getLY().Value);
							map.put("BDCDYID", unit.getId());
							if(BDCDYLX.H.equals(unit.getBDCDYLX())){
								House hhh=(House)unit;
								map.put("ZDBDCDYID", hhh.getZDBDCDYID());
							}
						}
					}else if (list_unit_other.containsKey(module_bdcdylx)) {
						Object unit = list_unit_other.get(module_bdcdylx);
						if(unit instanceof LogicBuilding){
							LogicBuilding logicbuilding=(LogicBuilding)unit;
							map.put("BDCDYID", logicbuilding.getId());
						}else if(unit instanceof Floor){
							Floor floor=(Floor)unit;
							map.put("BDCDYID", floor.getId());
						}
						if(unit instanceof BDCS_LJZ_GZ||unit instanceof BDCS_C_GZ){
							map.put("DJDYLY", DJDYLY.GZ.Value);
						}else if(unit instanceof BDCS_LJZ_XZ||unit instanceof BDCS_LJZ_XZY||unit instanceof BDCS_C_XZ||unit instanceof BDCS_C_XZY){
							map.put("DJDYLY", DJDYLY.XZ.Value);
						}else if(unit instanceof BDCS_LJZ_LS||unit instanceof BDCS_LJZ_LSY||unit instanceof BDCS_C_LS||unit instanceof BDCS_C_LSY){
							map.put("DJDYLY", DJDYLY.LS.Value);
						}
					}
				}

				map.put("MODULEID", moduleid);
				HashMap<String, Object> info = new HashMap<String, Object>();
				HashMap<String, HashMap<String, String>> map_zl = new HashMap<String, HashMap<String, String>>();
				List<RT_UNIT_MODULEMANAGER> list_tdytfield = new ArrayList<RT_UNIT_MODULEMANAGER>();
				List<RT_UNIT_MODULEMANAGER> list_yhzkfield = new ArrayList<RT_UNIT_MODULEMANAGER>();
				List<RT_UNIT_MODULEMANAGER> list_yhydzbfield = new ArrayList<RT_UNIT_MODULEMANAGER>();
				List<String> list_statusfield = new ArrayList<String>();
				List<RT_UNIT_MODULEMANAGER> list_field = basecommondao.getDataList(RT_UNIT_MODULEMANAGER.class,
						"MODULEID='" + moduleid + "' ORDER BY SXH");
				if (list_field != null && list_field.size() > 0) {
					for (RT_UNIT_MODULEMANAGER field : list_field) {
						String field_bdcdylx = field.getBDCDYLX();
						String field_name = field.getFIELDNAME();
						String field_type = field.getFIELDTYPE();
						String name = getEntityName(field_bdcdylx) + "-" + field_name;
						if ("tdyt".equals(field_bdcdylx)) {
							if ("1".equals(field.getVISIBLE())) {
								list_tdytfield.add(field);
							}
							continue;
						}
						if ("yhzk".equals(field_bdcdylx)) {
							if ("1".equals(field.getVISIBLE())) {
								list_yhzkfield.add(field);
							}
							continue;
						}
						if ("yhydzb".equals(field_bdcdylx)) {
							if ("1".equals(field.getVISIBLE())) {
								list_yhydzbfield.add(field);
							}
							continue;
						}

						if (!list_unit.containsKey(field_bdcdylx)&&!list_unit_other.containsKey(field_bdcdylx)) {
							continue;
						}

						if (enum_status.contains(field_name)) {
							if (list_status == null) {
								list_status = getUnitStatusList(list_unit, xmbh);
							}
							Object value = getFieldValueByName(field_name, list_status.get(field_bdcdylx));
							info.put(name, StringHelper.formatObject(value));
							list_statusfield.add(field.getId());
							continue;
						}

						RealUnit field_unit = list_unit.get(field_bdcdylx);
						Object field_unit_other = list_unit_other.get(field_bdcdylx);
						Object value=null;
						if(field_unit!=null){
							value = getFieldValueByName(field_name, field_unit);
						}else{
							value = getFieldValueByName(field_name, field_unit_other);
						}
						if ("YT".equals(field_name)) {
							if (!StringHelper.isEmpty(value)&&(value.toString().indexOf("0")!=-1 || value.toString().indexOf("1")!=-1)) {
								value = ConstHelper.getNameByValue("TDYT", getFieldValueByName(field_name, field_unit).toString());
							}else {
								List<BDCS_TDYT_XZ> lsttdyt = basecommondao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + bdcdyid + "' ");
								boolean flag = true;
								String strtdyt = "";
								if (!StringHelper.isEmpty(lsttdyt) && !(lsttdyt.size() > 0)) {
									CommonDao commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
									String ytClassName = EntityTools.getEntityName("BDCS_TDYT", ConstValue.DJDYLY.initFrom(djdyly));
									Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
									List<TDYT> listyts = (List<TDYT>) commonDao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "'");
									for (int i = 0; i < listyts.size(); i++) {
										strtdyt += listyts.get(i).getTDYTMC();
										if (i+1 < listyts.size()) {
											strtdyt +="、";
										}
									}
								}else {
									for (TDYT tdyt : lsttdyt) {
										if (flag) {
											strtdyt = tdyt.getTDYTMC();
											flag = false;
										} else {
											strtdyt = strtdyt + "、" + tdyt.getTDYTMC();
										}
									}
								}
								value = strtdyt;
							}
						}
						if ("ZL".equals(field.getFIELDNAME())) {
							HashMap<String, String> info_zl = new HashMap<String, String>();
							if (field_unit != null) {
                                info_zl.put("BDCDYID", field_unit.getId());
                                info_zl.put("BDCDYLX", field_unit.getBDCDYLX().Value);
                                info_zl.put("ZL", field_unit.getZL());
                                if(BDCDYLX.H.equals(field_unit.getBDCDYLX())){
                                    House hhh=(House)field_unit;
                                    info_zl.put("ZDBDCDYID", hhh.getZDBDCDYID());
                                }
                                map_zl.put(field.getId(), info_zl);
                            }
						}
						if (StringHelper.isEmpty(value) || "null".equals(value)) {
							info.put(name, "");
						} else {
							if ("text".equals(field_type) || "combox".equals(field_type)|| "span".equals(field_type)) {
								
			                /*楚雄-户：单元信息界面中的RelationId调用现状层中的RelationId*/
								String xzqhdm= ConfigHelper.getNameByValue("XZQHDM");
								if(("RELATIONID".equals(field.getFIELDNAME()))
									&& (xzqhdm!=null && xzqhdm.startsWith("5323"))
									&& (bdcdylx!=null && bdcdylx.equals("031"))){
									
									List<BDCS_H_XZ> hlist= basecommondao.getDataList(BDCS_H_XZ.class, "bdcdyh='"+getFieldValueByName("BDCDYH", field_unit)+"'");
									if(hlist.size()>0)
									{
										info.put(name, hlist.get(0).getRELATIONID());
									}
									
								}else{
										info.put(name, StringHelper.formatObject(value));
								}		
								

							} else if ("number".equals(field_type)) {
								info.put(name, FormatDouble(value, field.getNUMBER_L(), field.getNUMBER_R()));
							} else if ("date".equals(field_type)) {
								try {
									info.put(name, StringHelper.FormatDateOnType(
											StringHelper.FormatByDate(value, "yyyy-MM-dd"), "yyyy-MM-dd"));
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}
						if ("HTH".equals(field.getFIELDNAME())) {
							info.put(name, StringHelper.formatObject(value));
							String qlsql = " SELECT QL.HTH FROM BDCK.BDCS_DJDY_XZ DJDY "
									+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID " + " WHERE DJDY.BDCDYID='"
									+ bdcdyid + "' and QL.QLLX in('4','6','8')";
							List<Map> qllst = basecommondao.getDataListByFullSql(qlsql);
							if (!StringHelper.isEmpty(qllst) && qllst.size() > 0) {
								info.put(name, StringHelper.isEmpty(qllst.get(0).get("HTH")) ? null
										: qllst.get(0).get("HTH").toString());								
							}
							if("".equals(info.get("HTH")) || info.get("HTH") ==null){
								info.put(name, StringHelper.formatObject(value));
							}
						}
						if ("MSR".equals(field.getFIELDNAME())) {
							info.put(name, StringHelper.formatObject(value));
							String qlsql = " SELECT QL.MSR FROM BDCK.BDCS_DJDY_XZ DJDY "
									+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID " + " WHERE DJDY.BDCDYID='"
									+ bdcdyid + "' and QL.QLLX in('4','6','8')";
							List<Map> qllst = basecommondao.getDataListByFullSql(qlsql);
							if (!StringHelper.isEmpty(qllst) && qllst.size() > 0) {								
								info.put(name, StringHelper.isEmpty(qllst.get(0).get("MSR")) ? null
										: qllst.get(0).get("MSR").toString());
							}
							if("".equals(info.get("MSR")) || info.get("MSR") ==null){
								info.put(name, StringHelper.formatObject(value));
							}
						}
					}
				}
				if (list_tdytfield != null && list_tdytfield.size() > 0 && list_unit.containsKey("02")) {

				}
				map.put("INFO", info);
				map.put("ZLINFO", map_zl);
				map.put("TDYTFIELDS", list_tdytfield);
				map.put("YHZKFIELDS", list_yhzkfield);
				map.put("YHYDZBFIELDS", list_yhydzbfield);
				map.put("STATUSFIELDS", list_statusfield);
				if (list_tdytfield != null && list_tdytfield.size() > 0) {
					if (list_unit.containsKey("02")) {
						RealUnit unit_zd = list_unit.get("02");
						UseLand land = (UseLand) unit_zd;
						if (land != null) {
							map.put("ZDBDCDYID", land.getId());
							map.put("ZDBDCDYLX", land.getBDCDYLX().Value);
							map.put("ZDDJDYLY", land.getLY().Value);
							if (land.getTDYTS() != null && land.getTDYTS().size() > 0) {
								List<HashMap<String, String>> tdyts = new ArrayList<HashMap<String, String>>();
								for (TDYT tdyt : land.getTDYTS()) {
									HashMap<String, String> m = new HashMap<String, String>();
									for (RT_UNIT_MODULEMANAGER field_tdyt : list_tdytfield) {
										m.put("ID", tdyt.getId());
										m.put("BDCDYID", tdyt.getBDCDYID());
										String fieldname = field_tdyt.getFIELDNAME();
										String fieldtype = field_tdyt.getFIELDTYPE();
										Object value = getFieldValueByName(fieldname, tdyt);
										if (StringHelper.isEmpty(value)) {
											m.put(fieldname, "");
										} else {
											if ("text".equals(fieldtype) || "combox".equals(fieldtype)) {
												m.put(fieldname, StringHelper.formatObject(value));

											} else if ("number".equals(fieldtype)) {
												m.put(fieldname, FormatDouble(value, field_tdyt.getNUMBER_L(),
														field_tdyt.getNUMBER_R()));
											} else if ("date".equals(fieldtype) || fieldtype.contains("-")) {
												try {
													m.put(fieldname,
															StringHelper.FormatDateOnType(
																	StringHelper.FormatByDate(value, "yyyy-MM-dd"),
																	"yyyy-MM-dd"));
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
										}
									}
									tdyts.add(m);
								}
								map.put("TDYTS", tdyts);
							} else {
								map.put("TDYTS", null);
							}
						} else {
							map.put("ZDBDCDYID", "");
							map.put("ZDBDCDYLX", "");
							map.put("ZDDJDYLY", "");
						}
					}else if("1".equals(ConfigHelper.getNameByValue("ISOPENTDYT_SYQZD"))  && list_unit.containsKey("01")){//所有权宗地
						RealUnit unit_zd = list_unit.get("01");
						OwnerLand land = (OwnerLand) unit_zd;
						if (land != null) {
							map.put("ZDBDCDYID", land.getId());
							map.put("ZDBDCDYLX", land.getBDCDYLX().Value);
							map.put("ZDDJDYLY", land.getLY().Value);
							String ytClassName = "BDCS_TDYT" + land.getLY().TableSuffix;
							String packageName = "com.supermap.realestate.registration.model.";
							Class<?> ytClass = null;
							try {
								ytClass = Class.forName(packageName + ytClassName);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							@SuppressWarnings("unchecked")
							List<TDYT> listyts = (List<TDYT>) basecommondao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "' ORDER BY SFZYT DESC");
							if (listyts != null && listyts.size() > 0) {
								List<HashMap<String, String>> tdyts = new ArrayList<HashMap<String, String>>();
								for (TDYT tdyt : listyts) {
									HashMap<String, String> m = new HashMap<String, String>();
									for (RT_UNIT_MODULEMANAGER field_tdyt : list_tdytfield) {
										m.put("ID", tdyt.getId());
										m.put("BDCDYID", tdyt.getBDCDYID());
										String fieldname = field_tdyt.getFIELDNAME();
										String fieldtype = field_tdyt.getFIELDTYPE();
										Object value = getFieldValueByName(fieldname, tdyt);
										if (StringHelper.isEmpty(value)) {
											m.put(fieldname, "");
										} else {
											if ("text".equals(fieldtype) || "combox".equals(fieldtype)) {
												m.put(fieldname, StringHelper.formatObject(value));

											} else if ("number".equals(fieldtype)) {
												m.put(fieldname, FormatDouble(value, field_tdyt.getNUMBER_L(),
														field_tdyt.getNUMBER_R()));
											} else if ("date".equals(fieldtype) || fieldtype.contains("-")) {
												try {
													m.put(fieldname,
															StringHelper.FormatDateOnType(
																	StringHelper.FormatByDate(value, "yyyy-MM-dd"),
																	"yyyy-MM-dd"));
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
										}
									}
									tdyts.add(m);
								}
								map.put("TDYTS", tdyts);
							} else {
								map.put("TDYTS", null);
							}
						} else {
							map.put("ZDBDCDYID", "");
							map.put("ZDBDCDYLX", "");
							map.put("ZDDJDYLY", "");
						}
					
					} else if( list_unit.containsKey("09")){//农用地
						RealUnit unit_zd = list_unit.get("09");
						AgriculturalLand land = (AgriculturalLand) unit_zd;
						if (land != null) {
							map.put("BDCDYID", land.getId());
							map.put("BDCDYLX", land.getBDCDYLX().Value);
							map.put("DJDYLY", land.getLY().Value);
							String ytClassName = "BDCS_TDYT" + land.getLY().TableSuffix;
							String packageName = "com.supermap.realestate.registration.model.";
							Class<?> ytClass = null;
							try {
								ytClass = Class.forName(packageName + ytClassName);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							@SuppressWarnings("unchecked")
							List<TDYT> listyts = (List<TDYT>) basecommondao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "' ORDER BY SFZYT DESC");
							if (listyts != null && listyts.size() > 0) {
								List<HashMap<String, String>> tdyts = new ArrayList<HashMap<String, String>>();
								for (TDYT tdyt : listyts) {
									HashMap<String, String> m = new HashMap<String, String>();
									for (RT_UNIT_MODULEMANAGER field_tdyt : list_tdytfield) {
										m.put("ID", tdyt.getId());
										m.put("BDCDYID", tdyt.getBDCDYID());
										String fieldname = field_tdyt.getFIELDNAME();
										String fieldtype = field_tdyt.getFIELDTYPE();
										Object value = getFieldValueByName(fieldname, tdyt);
										if (StringHelper.isEmpty(value)) {
											m.put(fieldname, "");
										} else {
											if ("text".equals(fieldtype) || "combox".equals(fieldtype)) {
												m.put(fieldname, StringHelper.formatObject(value));

											} else if ("number".equals(fieldtype)) {
												m.put(fieldname, FormatDouble(value, field_tdyt.getNUMBER_L(),
														field_tdyt.getNUMBER_R()));
											} else if ("date".equals(fieldtype) || fieldtype.contains("-")) {
												try {
													m.put(fieldname,
															StringHelper.FormatDateOnType(
																	StringHelper.FormatByDate(value, "yyyy-MM-dd"),
																	"yyyy-MM-dd"));
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
										}
									}
									tdyts.add(m);
								}
								map.put("TDYTS", tdyts);
							} else {
								map.put("TDYTS", null);
							}
						} else {
							map.put("BDCDYID", "");
							map.put("BDCDYLX", "");
							map.put("DJDYLY", "");
						}
					
					
						
					}else if (list_unit.containsKey("05")) {//林地 
						RealUnit unit_zd = list_unit.get("05");
						Forest forest = (Forest) unit_zd;
						if (forest != null) {
							map.put("BDCDYID", forest.getId());
							map.put("BDCDYLX", forest.getBDCDYLX().Value);
							map.put("DJDYLY", forest.getLY().Value);
							String ytClassName = "BDCS_TDYT" + forest.getLY().TableSuffix;
							String packageName = "com.supermap.realestate.registration.model.";
							Class<?> ytClass = null;
							try {
								ytClass = Class.forName(packageName + ytClassName);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							@SuppressWarnings("unchecked")
							List<TDYT> listyts = (List<TDYT>) basecommondao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "' ORDER BY SFZYT DESC");
							if (listyts != null && listyts.size() > 0) {
								List<HashMap<String, String>> tdyts = new ArrayList<HashMap<String, String>>();
								for (TDYT tdyt : listyts) {
									HashMap<String, String> m = new HashMap<String, String>();
									for (RT_UNIT_MODULEMANAGER field_tdyt : list_tdytfield) {
										m.put("ID", tdyt.getId());
										m.put("BDCDYID", tdyt.getBDCDYID());
										String fieldname = field_tdyt.getFIELDNAME();
										String fieldtype = field_tdyt.getFIELDTYPE();
										Object value = getFieldValueByName(fieldname, tdyt);
										if (StringHelper.isEmpty(value)) {
											m.put(fieldname, "");
										} else {
											if ("text".equals(fieldtype) || "combox".equals(fieldtype)) {
												m.put(fieldname, StringHelper.formatObject(value));

											} else if ("number".equals(fieldtype)) {
												m.put(fieldname, FormatDouble(value, field_tdyt.getNUMBER_L(),
														field_tdyt.getNUMBER_R()));
											} else if ("date".equals(fieldtype) || fieldtype.contains("-")) {
												try {
													m.put(fieldname,
															StringHelper.FormatDateOnType(
																	StringHelper.FormatByDate(value, "yyyy-MM-dd"),
																	"yyyy-MM-dd"));
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
										}
									}
									tdyts.add(m);
								}
								map.put("TDYTS", tdyts);
							} else {
								map.put("TDYTS", null);
							}
						} else {
							map.put("BDCDYID", "");
							map.put("BDCDYLX", "");
							map.put("DJDYLY", "");
						}
						
					}else {
						map.put("TDYTS", null);
					}
				}
				if (list_yhzkfield != null && list_yhzkfield.size() > 0) {
					if (list_unit.containsKey("04")) {
						RealUnit unit_zh = list_unit.get("04");
						Sea sea = (Sea) unit_zh;
						if (sea != null) {
							map.put("ZHBDCDYID", sea.getId());
							map.put("ZHBDCDYLX", sea.getBDCDYLX().Value);
							map.put("ZHDJDYLY", sea.getLY().Value);
							if (sea.getYHZKS() != null && sea.getYHZKS().size() > 0) {
								List<HashMap<String, String>> yhzks = new ArrayList<HashMap<String, String>>();
								for (YHZK yhzk : sea.getYHZKS()) {
									HashMap<String, String> m = new HashMap<String, String>();
									for (RT_UNIT_MODULEMANAGER field_yhzk : list_yhzkfield) {
										m.put("ID", yhzk.getId());
										m.put("BDCDYID", yhzk.getBDCDYID());
										String fieldname = field_yhzk.getFIELDNAME();
										String fieldtype = field_yhzk.getFIELDTYPE();
										Object value = getFieldValueByName(fieldname, yhzk);
										if (StringHelper.isEmpty(value)) {
											m.put(fieldname, "");
										} else {
											if ("text".equals(fieldtype) || "combox".equals(fieldtype)) {
												m.put(fieldname, StringHelper.formatObject(value));

											} else if ("number".equals(fieldtype)) {
												m.put(fieldname, FormatDouble(value, field_yhzk.getNUMBER_L(),
														field_yhzk.getNUMBER_R()));
											} else if ("date".equals(fieldtype)) {
												try {
													m.put(fieldname,
															StringHelper.FormatDateOnType(
																	StringHelper.FormatByDate(value, "yyyy-MM-dd"),
																	"yyyy-MM-dd"));
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
										}
									}
									yhzks.add(m);
								}
								map.put("YHZKS", yhzks);
							} else {
								map.put("YHZKS", null);
							}
						} else {
							map.put("ZHBDCDYID", "");
							map.put("ZHBDCDYLX", "");
							map.put("ZHDJDYLY", "");
						}
					} else {
						map.put("YHZKS", null);
					}
				}
				if (list_yhydzbfield != null && list_yhydzbfield.size() > 0) {
					if (list_unit.containsKey("04")) {
						RealUnit unit_zh = list_unit.get("04");
						Sea sea = (Sea) unit_zh;
						if (sea != null) {
							map.put("ZHBDCDYID", sea.getId());
							map.put("ZHBDCDYLX", sea.getBDCDYLX().Value);
							map.put("ZHDJDYLY", sea.getLY().Value);
							if (sea.getYHYDZBS() != null && sea.getYHYDZBS().size() > 0) {
								List<HashMap<String, String>> yhydzbs = new ArrayList<HashMap<String, String>>();
								for (YHYDZB yhydzb : sea.getYHYDZBS()) {
									HashMap<String, String> m = new HashMap<String, String>();
									for (RT_UNIT_MODULEMANAGER field_yhydzb : list_yhydzbfield) {
										m.put("ID", yhydzb.getId());
										m.put("BDCDYID", yhydzb.getBDCDYID());
										String fieldname = field_yhydzb.getFIELDNAME();
										String fieldtype = field_yhydzb.getFIELDTYPE();
										Object value = getFieldValueByName(fieldname, yhydzb);
										if (StringHelper.isEmpty(value)) {
											m.put(fieldname, "");
										} else {
											if ("text".equals(fieldtype) || "combox".equals(fieldtype)) {
												m.put(fieldname, StringHelper.formatObject(value));

											} else if ("number".equals(fieldtype)) {
												m.put(fieldname, FormatDouble(value, field_yhydzb.getNUMBER_L(),
														field_yhydzb.getNUMBER_R()));
											} else if ("date".equals(fieldtype)) {
												try {
													m.put(fieldname,
															StringHelper.FormatDateOnType(
																	StringHelper.FormatByDate(value, "yyyy-MM-dd"),
																	"yyyy-MM-dd"));
												} catch (ParseException e) {
													e.printStackTrace();
												}
											}
										}
									}
									yhydzbs.add(m);
								}
								map.put("YHYDZBS", yhydzbs);
							} else {
								map.put("YHYDZBS", null);
							}
						} else {
							map.put("ZHBDCDYID", "");
							map.put("ZHBDCDYLX", "");
							map.put("ZHDJDYLY", "");
						}
					} else {
						map.put("YHYDZBS", null);
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 更新不动产单元信息，并记录变更前单元信息
	 * 
	 * @Title: UpdateBDCDYInfoEx
	 * @author:yuxuebin @date：2016年03月24日 10:31:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updatedyinfo/{xmbh}/{bdcdyid}/{bdcdylx}/{djdyly}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateBDCDYInfoEx(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, @PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("djdyly") String djdyly, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setMsg("保存失败！");
		msg.setSuccess("false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		if (StringHelper.isEmpty(bdcdyid) || StringHelper.isEmpty(bdcdylx) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		HashMap<String, RealUnit> list_unit = getUnitList(bdcdylx, djdyly, bdcdyid, xmbh);
		
		HashMap<String, Object> list_unit_other=getOtherUnitList(bdcdylx, djdyly, bdcdyid, xmbh);
		
		HashMap<String, Map> bdcdylx_map = new HashMap<String, Map>();
		Set set_field = map.keySet();
		Iterator iterator_field = set_field.iterator();
		while (iterator_field.hasNext()) {
			Object obj = iterator_field.next();
			String field_name = StringHelper.formatObject(obj);
			String field_entityname = "";
			String field_fieldname = "";
			String field_bdcdylx = "";
			String[] strs = field_name.split("-");
			if (strs != null && strs.length == 2) {
				field_entityname = strs[0];
				field_fieldname = strs[1];
				field_bdcdylx = getBDCDYLX(field_entityname);
			} else {
				continue;
			}
			Object val = map.get(obj);
			if (bdcdylx_map.containsKey(field_bdcdylx)) {
				Map m_dy = bdcdylx_map.get(field_bdcdylx);
				m_dy.put(field_fieldname, val);
				bdcdylx_map.remove(field_bdcdylx);
				bdcdylx_map.put(field_bdcdylx, m_dy);
			} else {
				Map m_dy = new HashMap<String, Object>();
				m_dy.put(field_fieldname, val);
				bdcdylx_map.put(field_bdcdylx, m_dy);
			}
		}

		Set set_dy = bdcdylx_map.keySet();
		Iterator iterator_dy = set_dy.iterator();
		while (iterator_dy.hasNext()) {
			Object obj = iterator_dy.next();
			String dy_bdcdylx = StringHelper.formatObject(obj);
			Map dy_map = (Map) bdcdylx_map.get(dy_bdcdylx);
			if (list_unit.containsKey(dy_bdcdylx)) {
				RealUnit unit_update = list_unit.get(dy_bdcdylx);
				if (IsDyChange(dy_map, unit_update)) {
					if (BackBDCDYInfo(xmbh, unit_update.getBDCDYLX(), unit_update.getLY(), unit_update.getId())) {
						UpdateBDCDYInfo(dy_map, unit_update, true);
					}
				}
			}else if (list_unit_other.containsKey(dy_bdcdylx)) {
				Object unit_update = list_unit_other.get(dy_bdcdylx);
				if (IsDyChange(dy_map, unit_update)) {
					UpdateBDCDYInfoEx(dy_map, unit_update, true);
				}
			}
		}
		return msg;
	}
	

	/**
	 * 更新土地用途信息
	 * 
	 * @Title: UpdateTDYTInfoEx
	 * @author:yuxuebin @date：2016年07月27日 11:25:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updatetdytinfo/{xmbh}/{bdcdyid}/{bdcdylx}/{djdyly}/{tdytmc}", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> UpdateTDYTInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, @PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("djdyly") String djdyly, @PathVariable("tdytmc") String tdytmc,HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("msg", "保存失败！");
		msg.put("success", "false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		if (StringHelper.isEmpty(bdcdyid) || StringHelper.isEmpty(bdcdylx) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		if (!map.containsKey("ID")) {
			return msg;
		}
		String id = StringHelper.formatObject(map.get("ID"));
		DJDYLY dyly = DJDYLY.initFrom(djdyly);
		boolean badd = false;
		TDYT tdyt = null;
		if (StringHelper.isEmpty(id)) {
			badd = true;
			tdyt = UnitTools.newTDYTUnit(dyly);
		} else {
			tdyt = UnitTools.loadTDYT(dyly, id);
		}
		
		tdyt.setXMBH(xmbh);
		StringHelper.setValue(map, tdyt);
		tdyt.setTDYTMC(tdytmc);
		if ("05".equals(bdcdylx)) {
			tdyt.setZYSZ(tdyt.getZYSZ());
		}
		if (badd) {
			tdyt.setBDCDYID(bdcdyid);
			basecommondao.save(tdyt);
			map.put("BDCDYID", bdcdyid);
			map.put("ID", tdyt.getId());
			msg.put("type", "add");
			if (DJDYLY.XZ.equals(dyly)) {
				TDYT tdyt_ls = UnitTools.newTDYTUnit(DJDYLY.LS);
				if (tdyt_ls != null) {
					try {
						PropertyUtils.copyProperties(tdyt_ls, tdyt);
					} catch (Exception e) {
					}
					basecommondao.save(tdyt_ls);
				}
			}
			if (DJDYLY.LS.equals(dyly)) {
				TDYT tdyt_xz = UnitTools.newTDYTUnit(DJDYLY.XZ);
				if (tdyt_xz != null) {
					try {
						PropertyUtils.copyProperties(tdyt_xz, tdyt);
					} catch (Exception e) {
					}
					basecommondao.save(tdyt_xz);
				}
			}
		} else {
			basecommondao.update(tdyt);
			if (DJDYLY.XZ.equals(dyly)) {
				TDYT tdyt_ls = UnitTools.loadTDYT(DJDYLY.LS, id);
				if (tdyt_ls != null) {
					try {
						PropertyUtils.copyProperties(tdyt_ls, tdyt);
					} catch (Exception e) {
					}
					basecommondao.update(tdyt_ls);
				}
			}
			if (DJDYLY.LS.equals(dyly)) {
				TDYT tdyt_xz = UnitTools.loadTDYT(DJDYLY.XZ, id);
				if (tdyt_xz != null) {
					try {
						PropertyUtils.copyProperties(tdyt_xz, tdyt);
					} catch (Exception e) {
					}
					basecommondao.update(tdyt_xz);
				}
			}
//			List<Map> nyd_gz = basecommondao.getDataListByFullSql("select * from BDCK.bdcs_nyd_gz  where bdcdyid='"+ bdcdyid +
//					"' and xmbh='"+xmbh+"'");
			if( "09".equals(bdcdylx)){
				BDCS_NYD_GZ nyd_gz = (BDCS_NYD_GZ) UnitTools.loadUnit(BDCDYLX.NYD, dyly, bdcdyid);
				List<BDCS_TDYT_GZ> listyts = (List<BDCS_TDYT_GZ>) basecommondao.getDataList(BDCS_TDYT_GZ.class, "BDCDYID='" + bdcdyid + "'");
				String yt_nyd = "";
				for (int i = 0; i < listyts.size(); i++) {
					yt_nyd += listyts.get(i).getTDYTMC();
					if (i+1 < listyts.size()) {
						yt_nyd +="、";
					}
				}
				nyd_gz.setYT(yt_nyd);
			}
			
			
			msg.put("type", "edit");
		}
		basecommondao.flush();
		msg.put("msg", "保存成功！");
		msg.put("success", "true");
		msg.put("tdyt", map);
		return msg;
	}

	/**
	 * 删除土地用途信息
	 * 
	 * @Title: DeleteTDYTInfo
	 * @author:yuxuebin @date：2016年07月27日 15:53:29
	 * @param id
	 * @param djdyly
	 * @return
	 */
	@RequestMapping(value = "/updatetdytinfo/{id}/{djdyly}/", method = RequestMethod.DELETE)
	public @ResponseBody HashMap<String, Object> DeleteTDYTInfo(@PathVariable("id") String id,
			@PathVariable("djdyly") String djdyly, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("msg", "删除失败！");
		msg.put("success", "false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringHelper.isEmpty(id) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		DJDYLY dyly = DJDYLY.initFrom(djdyly);
		TDYT tdyt = UnitTools.loadTDYT(dyly, id);
		if (tdyt != null) {
			basecommondao.deleteEntity(tdyt);
		}
		if (DJDYLY.XZ.equals(dyly)) {
			TDYT tdyt_ls = UnitTools.loadTDYT(DJDYLY.LS, id);
			if (tdyt_ls != null) {
				basecommondao.deleteEntity(tdyt_ls);
			}
		}
		if (DJDYLY.LS.equals(dyly)) {
			TDYT tdyt_xz = UnitTools.loadTDYT(DJDYLY.XZ, id);
			if (tdyt_xz != null) {
				basecommondao.deleteEntity(tdyt_xz);
			}
		}
		basecommondao.flush();
		msg.put("msg", "删除成功！");
		msg.put("success", "true");
		return msg;
	}

	/**
	 * 更新用海状况信息
	 * 
	 * @Title: UpdateYHZKInfo
	 * @author:yuxuebin @date：2016年07月28日 01:13:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updateyhzkinfo/{xmbh}/{bdcdyid}/{bdcdylx}/{djdyly}/", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> UpdateYHZKInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, @PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("djdyly") String djdyly, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("msg", "保存失败！");
		msg.put("success", "false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		if (StringHelper.isEmpty(bdcdyid) || StringHelper.isEmpty(bdcdylx) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		if (!map.containsKey("ID")) {
			return msg;
		}
		String id = StringHelper.formatObject(map.get("ID"));
		DJDYLY dyly = DJDYLY.initFrom(djdyly);
		boolean badd = false;
		YHZK yhzk = null;
		if (StringHelper.isEmpty(id)) {
			badd = true;
			yhzk = UnitTools.newYHZKUnit(dyly);
		} else {
			yhzk = UnitTools.loadYHZK(dyly, id);
		}
		yhzk.setXMBH(xmbh);
		StringHelper.setValue(map, yhzk);
		if (badd) {
			yhzk.setBDCDYID(bdcdyid);
			basecommondao.save(yhzk);
			map.put("BDCDYID", bdcdyid);
			map.put("ID", yhzk.getId());
			msg.put("type", "add");
			if (DJDYLY.XZ.equals(dyly)) {
				YHZK yhzk_ls = UnitTools.newYHZKUnit(DJDYLY.LS);
				if (yhzk_ls != null) {
					try {
						PropertyUtils.copyProperties(yhzk_ls, yhzk);
					} catch (Exception e) {
					}
					basecommondao.save(yhzk_ls);
				}
			}
			if (DJDYLY.LS.equals(dyly)) {
				YHZK yhzk_xz = UnitTools.newYHZKUnit(DJDYLY.XZ);
				if (yhzk_xz != null) {
					try {
						PropertyUtils.copyProperties(yhzk_xz, yhzk);
					} catch (Exception e) {
					}
					basecommondao.save(yhzk_xz);
				}
			}
		} else {
			basecommondao.update(yhzk);
			if (DJDYLY.XZ.equals(dyly)) {
				YHZK yhzk_ls = UnitTools.loadYHZK(DJDYLY.LS, id);
				if (yhzk_ls != null) {
					try {
						PropertyUtils.copyProperties(yhzk_ls, yhzk);
					} catch (Exception e) {
					}
					basecommondao.update(yhzk_ls);
				}
			}
			if (DJDYLY.LS.equals(dyly)) {
				YHZK yhzk_xz = UnitTools.loadYHZK(DJDYLY.XZ, id);
				if (yhzk_xz != null) {
					try {
						PropertyUtils.copyProperties(yhzk_xz, yhzk);
					} catch (Exception e) {
					}
					basecommondao.update(yhzk_xz);
				}
			}
			msg.put("type", "edit");
		}
		basecommondao.flush();
		msg.put("msg", "保存成功！");
		msg.put("success", "true");
		msg.put("yhzk", map);
		return msg;
	}

	/**
	 * 删除用海状况信息
	 * 
	 * @Title: DeleteYHZKInfo
	 * @author:yuxuebin @date：2016年07月28日 01:12:29
	 * @param id
	 * @param djdyly
	 * @return
	 */
	@RequestMapping(value = "/updateyhzkinfo/{id}/{djdyly}/", method = RequestMethod.DELETE)
	public @ResponseBody HashMap<String, Object> DeleteYHZKInfo(@PathVariable("id") String id,
			@PathVariable("djdyly") String djdyly, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("msg", "删除失败！");
		msg.put("success", "false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringHelper.isEmpty(id) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		DJDYLY dyly = DJDYLY.initFrom(djdyly);
		YHZK yhzk = UnitTools.loadYHZK(dyly, id);
		if (yhzk != null) {
			basecommondao.deleteEntity(yhzk);
		}
		if (DJDYLY.XZ.equals(dyly)) {
			YHZK yhzk_ls = UnitTools.loadYHZK(DJDYLY.LS, id);
			if (yhzk_ls != null) {
				basecommondao.deleteEntity(yhzk_ls);
			}
		}
		if (DJDYLY.LS.equals(dyly)) {
			YHZK yhzk_xz = UnitTools.loadYHZK(DJDYLY.XZ, id);
			if (yhzk_xz != null) {
				basecommondao.deleteEntity(yhzk_xz);
			}
		}
		basecommondao.flush();
		msg.put("msg", "删除成功！");
		msg.put("success", "true");
		return msg;
	}

	/**
	 * 更新土地用途信息
	 * 
	 * @Title: UpdateYHYDZBInfo
	 * @author:yuxuebin @date：2016年07月28日 01:17:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updateyhydzbinfo/{xmbh}/{bdcdyid}/{bdcdylx}/{djdyly}/", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> UpdateYHYDZBInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, @PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("djdyly") String djdyly, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("msg", "保存失败！");
		msg.put("success", "false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		if (StringHelper.isEmpty(bdcdyid) || StringHelper.isEmpty(bdcdylx) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		if (!map.containsKey("ID")) {
			return msg;
		}
		String id = StringHelper.formatObject(map.get("ID"));
		DJDYLY dyly = DJDYLY.initFrom(djdyly);
		boolean badd = false;
		YHYDZB yhydzb = null;
		if (StringHelper.isEmpty(id)) {
			badd = true;
			yhydzb = UnitTools.newYHYDZBUnit(dyly);
		} else {
			yhydzb = UnitTools.loadYHYDZB(dyly, id);
		}
		yhydzb.setXMBH(xmbh);
		StringHelper.setValue(map, yhydzb);
		if (badd) {
			yhydzb.setBDCDYID(bdcdyid);
			basecommondao.save(yhydzb);
			map.put("BDCDYID", bdcdyid);
			map.put("ID", yhydzb.getId());
			msg.put("type", "add");
			if (DJDYLY.XZ.equals(dyly)) {
				YHYDZB yhydzb_ls = UnitTools.newYHYDZBUnit(DJDYLY.LS);
				if (yhydzb_ls != null) {
					try {
						PropertyUtils.copyProperties(yhydzb_ls, yhydzb);
					} catch (Exception e) {
					}
					basecommondao.save(yhydzb_ls);
				}
			}
			if (DJDYLY.LS.equals(dyly)) {
				YHYDZB yhydzb_xz = UnitTools.newYHYDZBUnit(DJDYLY.XZ);
				if (yhydzb_xz != null) {
					try {
						PropertyUtils.copyProperties(yhydzb_xz, yhydzb);
					} catch (Exception e) {
					}
					basecommondao.save(yhydzb_xz);
				}
			}
		} else {
			basecommondao.update(yhydzb);
			if (DJDYLY.XZ.equals(dyly)) {
				YHYDZB yhydzb_ls = UnitTools.loadYHYDZB(DJDYLY.LS, id);
				if (yhydzb_ls != null) {
					try {
						PropertyUtils.copyProperties(yhydzb_ls, yhydzb);
					} catch (Exception e) {
					}
					basecommondao.update(yhydzb_ls);
				}
			}
			if (DJDYLY.LS.equals(dyly)) {
				YHYDZB yhydzb_xz = UnitTools.loadYHYDZB(DJDYLY.XZ, id);
				if (yhydzb_xz != null) {
					try {
						PropertyUtils.copyProperties(yhydzb_xz, yhydzb);
					} catch (Exception e) {
					}
					basecommondao.update(yhydzb_xz);
				}
			}
			msg.put("type", "edit");
		}
		basecommondao.flush();
		msg.put("msg", "保存成功！");
		msg.put("success", "true");
		msg.put("yhydzb", map);
		return msg;
	}

	/**
	 * 删除土地用途信息
	 * 
	 * @Title: DeleteYHYDZBInfo
	 * @author:yuxuebin @date：2016年07月28日 01:18:29
	 * @param id
	 * @param djdyly
	 * @return
	 */
	@RequestMapping(value = "/updateyhydzbinfo/{id}/{djdyly}/", method = RequestMethod.DELETE)
	public @ResponseBody HashMap<String, Object> DeleteYHYDZBInfo(@PathVariable("id") String id,
			@PathVariable("djdyly") String djdyly, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("msg", "删除失败！");
		msg.put("success", "false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringHelper.isEmpty(id) || StringHelper.isEmpty(djdyly)) {
			return msg;
		}
		DJDYLY dyly = DJDYLY.initFrom(djdyly);
		YHYDZB yhydzb = UnitTools.loadYHYDZB(dyly, id);
		if (yhydzb != null) {
			basecommondao.deleteEntity(yhydzb);
		}
		if (DJDYLY.XZ.equals(dyly)) {
			YHYDZB yhydzb_ls = UnitTools.loadYHYDZB(DJDYLY.LS, id);
			if (yhydzb_ls != null) {
				basecommondao.deleteEntity(yhydzb_ls);
			}
		}
		if (DJDYLY.LS.equals(dyly)) {
			YHYDZB yhydzb_xz = UnitTools.loadYHYDZB(DJDYLY.XZ, id);
			if (yhydzb_xz != null) {
				basecommondao.deleteEntity(yhydzb_xz);
			}
		}
		basecommondao.flush();
		msg.put("msg", "删除成功！");
		msg.put("success", "true");
		return msg;
	}

	@SuppressWarnings("rawtypes")
	public boolean IsDyChange(Map map, Object unit_update) {
		boolean b = false;
		Set set_dy = map.keySet();
		Iterator iterator_dy = set_dy.iterator();
		while (iterator_dy.hasNext()) {
			Object obj = iterator_dy.next();
			String name = StringHelper.formatObject(obj);
			Object val = map.get(obj);
			Object val_old = getFieldValueByName(name, unit_update);
			if (StringHelper.isEmpty(val_old) && !StringHelper.isEmpty(val)) {
				b = true;
				break;
			}
			if (!StringHelper.isEmpty(val_old) && StringHelper.isEmpty(val)) {
				b = true;
				break;
			}
			if (!StringHelper.isEmpty(val_old) && !StringHelper.isEmpty(val)) {
				if (val_old instanceof Double) {
					if ((StringHelper.getDouble(val_old)) != (StringHelper.getDouble(val))) {
						b = true;
						break;
					}
				} else if (val_old instanceof Date) {
					Date d_old = null;
					try {
						d_old = StringHelper.FormatByDate(val_old);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Date d_new = null;
					try {
						d_new = StringHelper.FormatByDate(val);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (!d_old.equals(d_new)) {
						b = true;
						break;
					}
				} else if (val_old instanceof String) {
					if (!(StringHelper.formatObject(val_old)).equals(StringHelper.formatObject(val))) {
						b = true;
						break;
					}
				} else if (val_old instanceof Integer) {
					if ((StringHelper.getInt(val_old)) != (StringHelper.getInt(val))) {
						b = true;
						break;
					}
				}
			}
		}
		return b;
	}
	

	public boolean BackBDCDYInfo(String xmbh, BDCDYLX dylx, DJDYLY dyly, String bdcdyid) {
		if (DJDYLY.XZ.equals(dyly) || DJDYLY.LS.equals(dyly)) {
			RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.LS, bdcdyid);
			if (unit != null) {
				List<BDCS_DYBG> dybgs = basecommondao.getDataList(BDCS_DYBG.class,
						"XMBH='" + xmbh + "' AND XBDCDYID='" + bdcdyid + "'");
				if (dybgs != null && dybgs.size() > 0) {
					return true;
				}
				String old_bdcdyid = SuperHelper.GeneratePrimaryKey();
				String dybgid = SuperHelper.GeneratePrimaryKey();
				String djdyid = "";
				List<BDCS_DJDY_LS> djdys = basecommondao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyid + "'");
				if (djdys != null && djdys.size() > 0) {
					djdyid = djdys.get(0).getDJDYID();
				}
				BDCS_DYBG dybg = new BDCS_DYBG();
				dybg.setId(dybgid);
				dybg.setLBDCDYID(old_bdcdyid);
				dybg.setXBDCDYID(bdcdyid);
				dybg.setLDJDYID(djdyid);
				dybg.setXDJDYID(djdyid);
				dybg.setXMBH(xmbh);
				basecommondao.save(dybg);

				RealUnit unit_back = UnitTools.newRealUnit(dylx, DJDYLY.LS);
				ObjectHelper.copyObject(unit, unit_back);
				unit_back.setId(old_bdcdyid);
				basecommondao.save(unit_back);
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public ResultMessage UpdateBDCDYInfo(Map map, RealUnit unit_update, boolean bUpdateRe) {
		ResultMessage msg = new ResultMessage();
		if (unit_update != null) {
			if (BDCDYLX.H.equals(unit_update.getBDCDYLX()) || BDCDYLX.YCH.equals(unit_update.getBDCDYLX())) {
				House h = (House) unit_update;
				StringHelper.setValue(map, h);
				basecommondao.update(h);
			} else if (BDCDYLX.ZRZ.equals(unit_update.getBDCDYLX()) || BDCDYLX.YCZRZ.equals(unit_update.getBDCDYLX())) {
				Building building = (Building) unit_update;
				StringHelper.setValue(map, building);
				basecommondao.update(building);
			} else if (BDCDYLX.SHYQZD.equals(unit_update.getBDCDYLX())) {
				UseLand useland = (UseLand) unit_update;
				StringHelper.setValue(map, useland);
				basecommondao.update(useland);
			} else if (BDCDYLX.SYQZD.equals(unit_update.getBDCDYLX())) {
				OwnerLand ownerland = (OwnerLand) unit_update;
				StringHelper.setValue(map, ownerland);
				basecommondao.update(ownerland);
			} else if (BDCDYLX.NYD.equals(unit_update.getBDCDYLX())) {
				AgriculturalLand AgriculturalLand = (AgriculturalLand) unit_update;
				StringHelper.setValue(map, AgriculturalLand);
				basecommondao.update(AgriculturalLand);
			} else if (BDCDYLX.LD.equals(unit_update.getBDCDYLX())) {
				Forest forest = (Forest) unit_update;
				StringHelper.setValue(map, forest);
				basecommondao.update(forest);
			} else if (BDCDYLX.HY.equals(unit_update.getBDCDYLX())) {
				Sea sea = (Sea) unit_update;
				StringHelper.setValue(map, sea);
				basecommondao.update(sea);
			}
			if (bUpdateRe) {
				if (DJDYLY.LS.equals(unit_update.getLY())) {
					RealUnit unit_update_1 = UnitTools.loadUnit(unit_update.getBDCDYLX(), DJDYLY.XZ,
							unit_update.getId());
					this.UpdateBDCDYInfo(map, unit_update_1, false);
				}
				if (DJDYLY.XZ.equals(unit_update.getLY())) {
					RealUnit unit_update_1 = UnitTools.loadUnit(unit_update.getBDCDYLX(), DJDYLY.LS,
							unit_update.getId());
					this.UpdateBDCDYInfo(map, unit_update_1, false);
				}
			}

			basecommondao.flush();
		} else {
			msg.setSuccess("false");
			msg.setMsg("更新失败!未找到不动产单元");
			YwLogUtil.addYwLog("更新单元信息-更新失败!未找到不动产单元", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			return msg;
		}
		msg.setSuccess("true");
		msg.setMsg("更新成功!");
		return msg;
	}
	
	@SuppressWarnings("rawtypes")
	public ResultMessage UpdateBDCDYInfoEx(Map map, Object unit_update, boolean bUpdateRe) {
		ResultMessage msg = new ResultMessage();
		if (unit_update != null) {
			if (unit_update instanceof LogicBuilding|| unit_update instanceof Floor) {
				StringHelper.setValue(map, unit_update);
				basecommondao.update(unit_update);
			}
			if (bUpdateRe) {
				if (unit_update instanceof BDCS_LJZ_LS) {
					LogicBuilding logicbuilding=(LogicBuilding)unit_update;
					LogicBuilding logicbuilding_1=UnitTools.loadLogicBuilding(BDCDYLX.H, DJDYLY.XZ, logicbuilding.getId());
					this.UpdateBDCDYInfoEx(map, logicbuilding_1, false);
				}else if (unit_update instanceof BDCS_LJZ_LSY) {
					LogicBuilding logicbuilding=(LogicBuilding)unit_update;
					LogicBuilding logicbuilding_1=UnitTools.loadLogicBuilding(BDCDYLX.YCH, DJDYLY.XZ, logicbuilding.getId());
					this.UpdateBDCDYInfoEx(map, logicbuilding_1, false);
				}else if (unit_update instanceof BDCS_LJZ_XZ) {
					LogicBuilding logicbuilding=(LogicBuilding)unit_update;
					LogicBuilding logicbuilding_1=UnitTools.loadLogicBuilding(BDCDYLX.H, DJDYLY.LS, logicbuilding.getId());
					this.UpdateBDCDYInfoEx(map, logicbuilding_1, false);
				}else if (unit_update instanceof BDCS_LJZ_XZY) {
					LogicBuilding logicbuilding=(LogicBuilding)unit_update;
					LogicBuilding logicbuilding_1=UnitTools.loadLogicBuilding(BDCDYLX.YCH, DJDYLY.LS, logicbuilding.getId());
					this.UpdateBDCDYInfoEx(map, logicbuilding_1, false);
				}else if (unit_update instanceof BDCS_C_LS) {
					Floor floor=(Floor)unit_update;
					Floor floor_1=UnitTools.loadFloor(BDCDYLX.H, DJDYLY.XZ, floor.getId());
					this.UpdateBDCDYInfoEx(map, floor_1, false);
				}else if (unit_update instanceof BDCS_C_LSY) {
					Floor floor=(Floor)unit_update;
					Floor floor_1=UnitTools.loadFloor(BDCDYLX.YCH, DJDYLY.XZ, floor.getId());
					this.UpdateBDCDYInfoEx(map, floor_1, false);
				}else if (unit_update instanceof BDCS_C_XZ) {
					Floor floor=(Floor)unit_update;
					Floor floor_1=UnitTools.loadFloor(BDCDYLX.H, DJDYLY.LS, floor.getId());
					this.UpdateBDCDYInfoEx(map, floor_1, false);
				}else if (unit_update instanceof BDCS_C_XZY) {
					Floor floor=(Floor)unit_update;
					Floor floor_1=UnitTools.loadFloor(BDCDYLX.YCH, DJDYLY.LS, floor.getId());
					this.UpdateBDCDYInfoEx(map, floor_1, false);
				}
			}

			basecommondao.flush();
		} else {
			msg.setSuccess("false");
			msg.setMsg("更新失败!未找到不动产单元");
			YwLogUtil.addYwLog("更新单元信息-更新失败!未找到不动产单元", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			return msg;
		}
		msg.setSuccess("true");
		msg.setMsg("更新成功!");
		return msg;
	}

	/**
	 * 从历史回溯方法中获取具体单元信息
	 * @param bdcdylx
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private HashMap<String,RealUnit> getUnitListEx(String bdcdylx, String djdyly,String bdcdyid,String xmbh){
		HashMap<String, RealUnit> modifyunitafter=new HashMap<String, RealUnit>();
		String qlid="";
		String fulSql= " SELECT QLID,DJDY.LY FROM BDCK.BDCS_QL_GZ QL "
				     + " LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID = QL.DJDYID "
				     + " WHERE DJDY.BDCDYID='" + bdcdyid + "' "
				     + " AND QL.XMBH='" + xmbh + "' AND DJDY.XMBH='" + xmbh + "'";
		List<Map>  lstmap= basecommondao.getDataListByFullSql(fulSql);
		if(lstmap !=null && lstmap.size()>0){
			qlid=StringHelper.FormatByDatatype(lstmap.get(0).get("QLID"));
		}else{
			return getUnit( bdcdylx,  djdyly,  bdcdyid,  xmbh);
		}
		if(StringHelper.isEmpty(qlid)){
			return modifyunitafter;
		}
		 modifyunitafter=HistoryBackdateTools.QueryUnitInfoByQlid(qlid);
		return modifyunitafter;
	}
	/**
	 * 
	 * @param bdcdylx
	 * @param djdyly
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	private HashMap<String, RealUnit> getUnitList(String bdcdylx, String djdyly, String bdcdyid, String xmbh) {
		
		BDCS_XMXX bdcs_xmxx=Global.getXMXXbyXMBH(xmbh);
		if(bdcs_xmxx !=null && SFDB.YES.Value.equals(bdcs_xmxx.getSFDB()) && !djdyly.equals("04")){
			return getUnitListEx(bdcdylx,djdyly, bdcdyid, xmbh);
		}else{
			return getUnit(bdcdylx,  djdyly,  bdcdyid,  xmbh);
		}		
	}
	
	private HashMap<String,RealUnit> getUnit(String bdcdylx, String djdyly, String bdcdyid, String xmbh){
		BDCDYLX lx = BDCDYLX.initFrom(bdcdylx);
		HashMap<String, RealUnit> list_unit = new HashMap<String, RealUnit>();
		if (BDCDYLX.H.equals(lx)) {
			RealUnit unit_h = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_h != null) {
				list_unit.put(bdcdylx, unit_h);
				if (djdyly.equals("04")) {
					DCS_H_GZ h = (DCS_H_GZ) unit_h;
					String dcxmid = h.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "'";
						List<DCS_ZRZ_GZ> zrzs = basecommondao.getDataList(DCS_ZRZ_GZ.class, sql);
						if (zrzs != null && zrzs.size() > 0) {
							RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.initFrom(djdyly), zrzs.get(0).getId());
							if (unit_zrz != null) {
								list_unit.put(BDCDYLX.ZRZ.Value, unit_zrz);
							}
						}
					}
				}else {
					House h = (House) unit_h;
					String zrzbdcdyid = h.getZRZBDCDYID();
					if (!StringHelper.isEmpty(zrzbdcdyid)) {
						RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, zrzbdcdyid);
						if (unit_zrz == null) {
							unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, zrzbdcdyid);
						}
						if (unit_zrz != null) {
							list_unit.put("03", unit_zrz);
						}
					}

					String zdbdcdyid = h.getZDBDCDYID();//获取宗地信息不动产权证号
					if (!StringHelper.isEmpty(zdbdcdyid)) {
						RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
						if(unit_zd!=null){
							StringBuilder bdcqzh=new StringBuilder();
							List<BDCS_DJDY_GZ> djdys=basecommondao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"+bdcdyid+"'");
							if(djdys!=null&&djdys.size()>0){
								String djdyid=djdys.get(0).getDJDYID();
								if(!StringHelper.isEmpty(djdyid)){
									List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')");
									if(qls!=null&&qls.size()>0){
										 int i=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
										for(Rights ql:qls){
											i++;
											bdcqzh.append(ql.getBDCQZH());
											if(i<qls.size())
												bdcqzh.append(",");
										}
									}
								}
							}
							if(!StringHelper.isEmpty(bdcqzh.toString())){
								UseLand useland=(UseLand)unit_zd;
								useland.setZDBDCQZH(bdcqzh.toString());
							}
						}
						if (unit_zd == null || StringHelper.isEmpty(xmbh) || !xmbh.equals(unit_zd.getXMBH())) {
							unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
						}
						if (unit_zd != null) {
							list_unit.put("02", unit_zd);
						}
					}
				}
			}else if (djdyly.equals("04")) {
				RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.initFrom(djdyly), bdcdyid);
				if (unit_zrz != null) {
					list_unit.put(BDCDYLX.ZRZ.Value, unit_zrz);
					DCS_ZRZ_GZ zrz = (DCS_ZRZ_GZ) unit_zrz;
					String dcxmid = zrz.getDCXMID();
					String sql = "";
					if (!StringHelper.isEmpty(dcxmid)) {
						 sql = " dcxmid = '" + dcxmid + "' and zrzbdcdyid ='" + bdcdyid + "'";
						 List<DCS_H_GZ> houses = basecommondao.getDataList(DCS_H_GZ.class, sql);
						 if (houses != null && houses.size() > 0) {
							 for (int j = 0; j < houses.size(); j++) {
								 DCS_H_GZ h = houses.get(j);
								 RealUnit unit_dch = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.initFrom(djdyly), h.getId());
								 if (unit_dch != null) {
									 list_unit.put(BDCDYLX.H.Value, unit_zrz);
								}
							}
						 }
					}
				}
			}
		} else if (BDCDYLX.YCH.equals(lx)) {
			RealUnit unit_h = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_h != null) {
				list_unit.put(bdcdylx, unit_h);
				if (djdyly.equals("04")) {
					DCS_H_GZY h = (DCS_H_GZY) unit_h;
					String dcxmid = h.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "'";;
						List<DCS_ZRZ_GZY> zrzs = basecommondao.getDataList(DCS_ZRZ_GZY.class, sql);
						if (zrzs != null && zrzs.size() > 0) {
							RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.initFrom(djdyly), zrzs.get(0).getId());
							if (unit_zrz != null) {
								list_unit.put(BDCDYLX.YCZRZ.Value, unit_zrz);
							}
						}
					}
				}else {
					House h = (House) unit_h;
					String zrzbdcdyid = h.getZRZBDCDYID();
					if (!StringHelper.isEmpty(zrzbdcdyid)) {
						RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.GZ, zrzbdcdyid);
						if (unit_zrz == null || StringHelper.isEmpty(xmbh) || !xmbh.equals(unit_zrz.getXMBH())) {
							unit_zrz = UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.LS, zrzbdcdyid);
						}
						if (unit_zrz != null) {
							list_unit.put("08", unit_zrz);
						}
					}

					String zdbdcdyid = h.getZDBDCDYID();
					if (!StringHelper.isEmpty(zdbdcdyid)) {
						RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
						if (unit_zd == null || StringHelper.isEmpty(xmbh) || !xmbh.equals(unit_zd.getXMBH())) {
							unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
						}
						if (unit_zd != null) {
							list_unit.put("02", unit_zd);
						}
					}
				}
			}else if (djdyly.equals("04")) {
				RealUnit unit_zrz = UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.initFrom(djdyly), bdcdyid);
				if (unit_zrz != null) {
					list_unit.put(BDCDYLX.YCZRZ.Value, unit_zrz);
					DCS_ZRZ_GZY zrz = (DCS_ZRZ_GZY) unit_zrz;
					String dcxmid = zrz.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "' and zrzbdcdyid ='" + bdcdyid + "'";
						 List<DCS_H_GZY> houses = basecommondao.getDataList(DCS_H_GZY.class, sql);
						 if (houses != null && houses.size() > 0) {
							 for (int j = 0; j < houses.size(); j++) {
								 DCS_H_GZY h = houses.get(j);
								 RealUnit unit_dch = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.initFrom(djdyly), h.getId());
								 if (unit_dch != null) {
									 list_unit.put(BDCDYLX.YCH.Value, unit_zrz);
								}
							}
						 }
					}
				}
			}
		} else if (BDCDYLX.ZRZ.equals(lx)) {
			RealUnit unit_zrz = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_zrz != null) {
				list_unit.put(bdcdylx, unit_zrz);
				if (djdyly.equals("04")) {
					DCS_ZRZ_GZ zrz = (DCS_ZRZ_GZ) unit_zrz;
					String dcxmid = zrz.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "'";;
						List<DCS_H_GZ> houses = basecommondao.getDataList(DCS_H_GZ.class, sql);
						if (houses != null && houses.size() > 0) {
							for (int i = 0; i < houses.size(); i++) {
								 DCS_H_GZ h = houses.get(i);
								 RealUnit unit_dch = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.initFrom(djdyly), h.getId());
								 if (unit_dch != null) {
									 list_unit.put(BDCDYLX.H.Value, unit_dch);
								}
							}
						}
					}
				}else {
					Building h = (Building) unit_zrz;
					String zdbdcdyid = h.getZDBDCDYID();
					if (!StringHelper.isEmpty(zdbdcdyid)) {
						RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
						if (unit_zd == null || StringHelper.isEmpty(xmbh) || !xmbh.equals(unit_zd.getXMBH())) {
							unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
						}
						if (unit_zd != null) {
							list_unit.put("02", unit_zd);
						}
					}
				}
			}else if(djdyly.equals("04")) {
				RealUnit unit_h = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.initFrom(djdyly), bdcdyid);
				if (unit_h != null) {
					list_unit.put(BDCDYLX.H.Value, unit_h);
					DCS_H_GZ h = (DCS_H_GZ) unit_h;
					String dcxmid = h.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "'";;
						List<DCS_ZRZ_GZ> zrzs = basecommondao.getDataList(DCS_ZRZ_GZ.class, sql);
						if (zrzs != null && zrzs.size() > 0) {
							 DCS_ZRZ_GZ zrz = zrzs.get(0);
							 RealUnit unit_yczrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.initFrom(djdyly), zrz.getId());
							 if (unit_yczrz != null) {
								 list_unit.put(BDCDYLX.ZRZ.Value, unit_yczrz);
							}
						}
					}
				}
			}
		} else if (BDCDYLX.YCZRZ.equals(lx)) {
			RealUnit unit_zrz = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_zrz != null) {
				list_unit.put(bdcdylx, unit_zrz);
				if (djdyly.equals("04")) {
					DCS_ZRZ_GZY zrz = (DCS_ZRZ_GZY) unit_zrz;
					String dcxmid = zrz.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "'";;
						List<DCS_H_GZY> houses = basecommondao.getDataList(DCS_H_GZY.class, sql);
						if (houses != null && houses.size() > 0) {
							for (int i = 0; i < houses.size(); i++) {
								DCS_H_GZY h = houses.get(i);
								 RealUnit unit_dch = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.initFrom(djdyly), h.getId());
								 if (unit_dch != null) {
									 list_unit.put(BDCDYLX.YCH.Value, unit_dch);
								}
							}
						}
					}
				}else {
					Building h = (Building) unit_zrz;
					String zdbdcdyid = h.getZDBDCDYID();
					if (!StringHelper.isEmpty(zdbdcdyid)) {
						RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
						if (unit_zd == null || StringHelper.isEmpty(xmbh) || !xmbh.equals(unit_zd.getXMBH())) {
							unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
						}
						if (unit_zd != null) {
							list_unit.put("02", unit_zd);
						}
					}
				}
			}else if(djdyly.equals("04")) {
				RealUnit unit_h = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.initFrom(djdyly), bdcdyid);
				if (unit_h != null) {
					list_unit.put(BDCDYLX.YCH.Value, unit_h);
					DCS_H_GZY h = (DCS_H_GZY) unit_h;
					String dcxmid = h.getDCXMID();
					if (!StringHelper.isEmpty(dcxmid)) {
						String sql = " dcxmid = '" + dcxmid + "'";;
						List<DCS_ZRZ_GZY> zrzs = basecommondao.getDataList(DCS_ZRZ_GZY.class, sql);
						if (zrzs != null && zrzs.size() > 0) {
							DCS_ZRZ_GZY zrz = zrzs.get(0);
							 RealUnit unit_yczrz = UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.initFrom(djdyly), zrz.getId());
							 if (unit_yczrz != null) {
								 list_unit.put(BDCDYLX.YCZRZ.Value, unit_yczrz);
							}
						}
					}
				}
			}
		} else if (BDCDYLX.LD.equals(lx)) {
			RealUnit unit_sllm = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_sllm != null) {
				list_unit.put(bdcdylx, unit_sllm);
				if (!djdyly.equals("04")) {
					Forest h = (Forest) unit_sllm;
					String zdbdcdyid = h.getZDBDCDYID();
					if (!StringHelper.isEmpty(zdbdcdyid)) {
						RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
						if (unit_zd == null || StringHelper.isEmpty(xmbh) || !xmbh.equals(unit_zd.getXMBH())) {
							unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, zdbdcdyid);
						}
						if (unit_zd != null) {
							list_unit.put("02", unit_zd);
						}
					}
				}
			}
		} else if (BDCDYLX.SHYQZD.equals(lx)) {
			RealUnit unit_zd = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_zd != null && djdyly.equals("04")) {
				list_unit.put(bdcdylx, unit_zd);
			}else if(unit_zd != null) {
				String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
				if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
					//为了显示宗地转移之前土地信息
					List<BDCS_DJDY_GZ> DY=basecommondao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"+bdcdyid+"'");
					List<BDCS_XMXX> xmxx =null;
					if(DY.size()>1){
						xmxx = basecommondao.getDataList(BDCS_XMXX.class, "xmbh='" + DY.get(1).getXMBH() +"'");
					}else{
						xmxx = basecommondao.getDataList(BDCS_XMXX.class, "xmbh='" + DY.get(0).getXMBH() +"'");
					}
					//String xmbh=djdy.getXMBH();
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.get(0).getPROJECT_ID());
					String sqls = " WORKFLOWCODE='" + workflowcode + "'";
					List<WFD_MAPPING> mappings = basecommondao.getDataList(WFD_MAPPING.class, sqls);
					WFD_MAPPING wm=mappings.get(0);
					String lcbm=wm.getWORKFLOWNAME();
					if(lcbm.contains("ZY001")){
						djdyly="03";
					}
					RealUnit unit_sllm = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
					if (unit_sllm != null) {
						list_unit.put(bdcdylx, unit_sllm);
					}
				}else{
					if("01".equals(djdyly)){
						StringBuilder bdcqzh=new StringBuilder();
						List<BDCS_DJDY_GZ> djdys=basecommondao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"+bdcdyid+"' AND XMBH='"+xmbh+"'");
						if(djdys!=null&&djdys.size()>0){
							String djdyid=djdys.get(0).getDJDYID();
							if(!StringHelper.isEmpty(djdyid)){
								List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')");
								if(qls!=null&&qls.size()>0){
									 int i=0;//兼容一处不动产单元有多条所有权权利的情况，把所有的权证号都加上WUZ
									for(Rights ql:qls){
										i++;
										bdcqzh.append(ql.getBDCQZH());
										if(i<qls.size())
											bdcqzh.append(",");
									}
								}
							}
						}
						if(!StringHelper.isEmpty(bdcqzh.toString())){
							UseLand useland=(UseLand)unit_zd;
							useland.setZDBDCQZH(bdcqzh.toString());
						}
					}
					list_unit.put(bdcdylx, unit_zd);
				}
			}
		} else if (BDCDYLX.SYQZD.equals(lx)) {
			RealUnit unit_sllm = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_sllm != null) {
				list_unit.put(bdcdylx, unit_sllm);
			}
		} else if (BDCDYLX.HY.equals(lx)) {
			RealUnit unit_sllm = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_sllm != null) {
				list_unit.put(bdcdylx, unit_sllm);
			}
		} else if (BDCDYLX.NYD.equals(lx)) {
			RealUnit unit_sllm = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_sllm != null) {
				list_unit.put(bdcdylx, unit_sllm);
			}
		}else if (BDCDYLX.GZW.equals(lx)) {
			RealUnit unit_sllm = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_sllm != null) {
				list_unit.put(bdcdylx, unit_sllm);
			}
		}
		return list_unit;
	}
	
	private HashMap<String, Object> getOtherUnitList(String bdcdylx, String djdyly, String bdcdyid, String xmbh) {
		BDCDYLX lx = BDCDYLX.initFrom(bdcdylx);
		HashMap<String, Object> list_unit = new HashMap<String, Object>();
		if (BDCDYLX.H.equals(lx)||BDCDYLX.YCH.equals(lx)) {
			RealUnit unit_h = UnitTools.loadUnit(lx, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_h != null) {
				House h = (House) unit_h;
				String pre_str="";
				if(BDCDYLX.YCH.equals(lx)){
					pre_str="yc";
				}
				String ljzid = h.getLJZID();
				if (!StringHelper.isEmpty(ljzid)) {
					LogicBuilding unit_ljz = UnitTools.loadLogicBuilding(lx, DJDYLY.XZ, ljzid);
					if (unit_ljz == null) {
						unit_ljz = UnitTools.loadLogicBuilding(lx, DJDYLY.GZ, ljzid);
					}
					if (unit_ljz != null) {
						list_unit.put(pre_str+"ljz", unit_ljz);
					}
				}
				String cid = h.getLJZID();
				if (!StringHelper.isEmpty(cid)) {
					Floor unit_c = UnitTools.loadFloor(lx, DJDYLY.XZ, cid);
					if (unit_c == null) {
						unit_c = UnitTools.loadFloor(lx, DJDYLY.GZ, cid);
					}
					if (unit_c != null) {
						list_unit.put(pre_str+"c", unit_c);
					}
				}
			}
		}else if("c".equals(bdcdylx)){
			Floor unit_c = UnitTools.loadFloor(BDCDYLX.H, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_c != null) {
				list_unit.put("c", unit_c);
			}
		}else if("ycc".equals(bdcdylx)){
			Floor unit_c = UnitTools.loadFloor(BDCDYLX.YCH, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_c != null) {
				list_unit.put("ycc", unit_c);
			}
		}else if("ljz".equals(bdcdylx)){
			LogicBuilding unit_ljz = UnitTools.loadLogicBuilding(BDCDYLX.H, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_ljz != null) {
				list_unit.put("ljz", unit_ljz);
			}
		}else if("ycljz".equals(bdcdylx)){
			LogicBuilding unit_ljz = UnitTools.loadLogicBuilding(BDCDYLX.YCH, DJDYLY.initFrom(djdyly), bdcdyid);
			if (unit_ljz != null) {
				list_unit.put("ycljz", unit_ljz);
			}
		}
		
		return list_unit;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private HashMap<String, UnitStatus> getUnitStatusList(HashMap<String, RealUnit> list_unit, String xmbh) {
		HashMap<String, UnitStatus> list_status = new HashMap<String, UnitStatus>();
		Iterator entries = list_unit.entrySet().iterator();
		Map.Entry entry;
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			String bdcdylx = (String) entry.getKey();
			Object valueObj = entry.getValue();
			RealUnit unit = null;
			if (null == valueObj) {
				continue;
			} else {
				unit = (RealUnit) valueObj;
			}
			if (unit != null) {
				if (BDCDYLX.H.equals(unit.getBDCDYLX())) {
					List<UnitStatus> list_status_sch = new ArrayList<UnitStatus>();
					UnitStatus status_sch = new UnitStatus();
					String djdyid = "";
					List<BDCS_DJDY_XZ> djdys_xz = basecommondao.getDataList(BDCS_DJDY_XZ.class,
							"BDCDYID='" + unit.getId() + "' AND BDCDYLX='" + unit.getBDCDYLX().Value + "'");
					if (djdys_xz != null && djdys_xz.size() > 0) {
						djdyid = djdys_xz.get(0).getDJDYID();
					}
					if (StringHelper.isEmpty(djdyid)) {
						List<BDCS_DJDY_GZ> djdys_gz = basecommondao.getDataList(BDCS_DJDY_GZ.class,
								"BDCDYID='" + unit.getId() + "' AND BDCDYLX='" + unit.getBDCDYLX().Value + "'");
						if (djdys_gz != null && djdys_gz.size() > 0) {
							djdyid = djdys_gz.get(0).getDJDYID();
						}
					}
					if (!StringHelper.isEmpty(djdyid)) {
						status_sch = getStatus(djdyid, unit.getId(), unit.getBDCDYLX().Value, xmbh);
					}
					list_status_sch.add(status_sch);
					String associatedPeriodRoom = "未关联";
					List<UnitStatus> list_status_ych = new ArrayList<UnitStatus>();
					List<YC_SC_H_XZ> list=new ArrayList<YC_SC_H_XZ>();
					List<Map> bdcdyids_ls = basecommondao.getDataListByFullSql("select t.bdcdyid from bdck.BDCS_DJDY_LS t where t.djdyid='" + djdyid + "' and rownum=1");
					List<Map> bdcdyids = basecommondao.getDataListByFullSql("select t.bdcdyid from bdck.BDCS_DJDY_GZ t where t.djdyid='" + djdyid + "' and rownum=1");
					if (!bdcdyids_ls.isEmpty())
					{
						list = basecommondao.getDataList(YC_SC_H_XZ.class, "SCBDCDYID='" + bdcdyids_ls.get(0).get("BDCDYID") + "'");
					}else{
						if (!bdcdyids.isEmpty()){
							list = basecommondao.getDataList(YC_SC_H_XZ.class, "SCBDCDYID='" + bdcdyids.get(0).get("BDCDYID") + "'");
						}
					}
					if (list != null && list.size() > 0) {
						for (YC_SC_H_XZ gx : list) {
							associatedPeriodRoom = "已关联";
							String ycbdcdyid = gx.getYCBDCDYID();
							UnitStatus status_ych = new UnitStatus();
							String ycdjdyid = "";
							List<BDCS_DJDY_XZ> yc_djdys_xz = basecommondao.getDataList(BDCS_DJDY_XZ.class,
									"BDCDYID='" + ycbdcdyid + "' AND BDCDYLX='" + BDCDYLX.YCH.Value + "'");
							if (yc_djdys_xz != null && yc_djdys_xz.size() > 0) {
								ycdjdyid = yc_djdys_xz.get(0).getDJDYID();
							}
							if (StringHelper.isEmpty(ycdjdyid)) {
								List<BDCS_DJDY_GZ> yc_djdys_gz = basecommondao.getDataList(BDCS_DJDY_GZ.class,
										"BDCDYID='" + ycbdcdyid + "' AND BDCDYLX='" + BDCDYLX.YCH.Value + "'");
								if (yc_djdys_gz != null && yc_djdys_gz.size() > 0) {
									ycdjdyid = yc_djdys_gz.get(0).getDJDYID();
								}
							}
							if (!StringHelper.isEmpty(ycdjdyid)) {
								status_ych = getStatus(ycdjdyid, unit.getId(), BDCDYLX.YCH.Value, xmbh);
							}
							list_status_ych.add(status_ych);
						}
					}
					UnitStatus status = getStatusFromYCSCStatus(list_status_sch, list_status_ych, BDCDYLX.H);
					status.setAssociatedPeriodRoom(associatedPeriodRoom);
					list_status.put(BDCDYLX.H.Value, status);

				} else if (BDCDYLX.YCH.equals(unit.getBDCDYLX())) {
					List<UnitStatus> list_status_ych = new ArrayList<UnitStatus>();
					UnitStatus status_ych = new UnitStatus();
					String djdyid = "";
					List<BDCS_DJDY_XZ> djdys_xz = basecommondao.getDataList(BDCS_DJDY_XZ.class,
							"BDCDYID='" + unit.getId() + "' AND BDCDYLX='" + unit.getBDCDYLX().Value + "'");
					if (djdys_xz != null && djdys_xz.size() > 0) {
						djdyid = djdys_xz.get(0).getDJDYID();
					}
					if (StringHelper.isEmpty(djdyid)) {
						List<BDCS_DJDY_GZ> djdys_gz = basecommondao.getDataList(BDCS_DJDY_GZ.class,
								"BDCDYID='" + unit.getId() + "' AND BDCDYLX='" + unit.getBDCDYLX().Value + "'");
						if (djdys_gz != null && djdys_gz.size() > 0) {
							djdyid = djdys_gz.get(0).getDJDYID();
						}
					}
					if (!StringHelper.isEmpty(djdyid)) {
						status_ych = getStatus(djdyid, unit.getId(), unit.getBDCDYLX().Value, xmbh);
					}
					list_status_ych.add(status_ych);
					String associatedReadyRoom = "未关联";
					List<UnitStatus> list_status_sch = new ArrayList<UnitStatus>();
					List<YC_SC_H_XZ> list = basecommondao.getDataList(YC_SC_H_XZ.class,
							"YCBDCDYID='" + unit.getId() + "'");
					if (list != null && list.size() > 0) {
						for (YC_SC_H_XZ gx : list) {
							associatedReadyRoom = "已关联";
							String scbdcdyid = gx.getSCBDCDYID();
							UnitStatus status_sch = new UnitStatus();
							String scdjdyid = "";
							List<BDCS_DJDY_XZ> sc_djdys_xz = basecommondao.getDataList(BDCS_DJDY_XZ.class,
									"BDCDYID='" + scbdcdyid + "' AND BDCDYLX='" + BDCDYLX.H.Value + "'");
							if (sc_djdys_xz != null && sc_djdys_xz.size() > 0) {
								scdjdyid = sc_djdys_xz.get(0).getDJDYID();
							}
							if (StringHelper.isEmpty(scdjdyid)) {
								List<BDCS_DJDY_GZ> sc_djdys_gz = basecommondao.getDataList(BDCS_DJDY_GZ.class,
										"BDCDYID='" + scbdcdyid + "' AND BDCDYLX='" + BDCDYLX.H.Value + "'");
								if (sc_djdys_gz != null && sc_djdys_gz.size() > 0) {
									scdjdyid = sc_djdys_gz.get(0).getDJDYID();
								}
							}
							if (!StringHelper.isEmpty(scdjdyid)) {
								status_sch = getStatus(scdjdyid, unit.getId(), BDCDYLX.YCH.Value, xmbh);
							}
							list_status_sch.add(status_sch);
						}
					}
					UnitStatus status = getStatusFromYCSCStatus(list_status_sch, list_status_ych, BDCDYLX.YCH);
					status.setAssociatedReadyRoom(associatedReadyRoom);
					list_status.put(BDCDYLX.YCH.Value, status);
				} else if (BDCDYLX.ZRZ.equals(unit.getBDCDYLX()) || BDCDYLX.YCZRZ.equals(unit.getBDCDYLX())) {

				} else {
					String djdyid = "";
					List<BDCS_DJDY_XZ> djdys_xz = basecommondao.getDataList(BDCS_DJDY_XZ.class,
							"BDCDYID='" + unit.getId() + "' AND BDCDYLX='" + unit.getBDCDYLX().Value + "'");
					if (djdys_xz != null && djdys_xz.size() > 0) {
						djdyid = djdys_xz.get(0).getDJDYID();
					}
					if (StringHelper.isEmpty(djdyid)) {
						List<BDCS_DJDY_GZ> djdys_gz = basecommondao.getDataList(BDCS_DJDY_GZ.class,
								"BDCDYID='" + unit.getId() + "' AND BDCDYLX='" + unit.getBDCDYLX().Value + "'");
						if (djdys_gz != null && djdys_gz.size() > 0) {
							djdyid = djdys_gz.get(0).getDJDYID();
						}
					}
					if (!StringHelper.isEmpty(djdyid)) {
						UnitStatus status = getStatus(djdyid, unit.getId(), unit.getBDCDYLX().Value, xmbh);
						list_status.put(unit.getBDCDYLX().Value, status);
					} else {
						list_status.put(unit.getBDCDYLX().Value, new UnitStatus());
					}
				}
			}
		}
		return list_status;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private UnitStatus getStatus(String djdyid, String bdcdyid, String bdcdylx, String xmbh) {
		UnitStatus status = new UnitStatus();
		// 在办状态
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.YWLSH AS YWLSH,XMXX.DJLX AS XMDJLX,XMXX.QLLX AS XMQLLX, ");
		builder.append("QL.DJLX AS QLDJLX,QL.QLLX AS QLQLLX ");
		builder.append("FROM BDCK.BDCS_QL_GZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ");
		builder.append("ON QL.XMBH=XMXX.XMBH ");
		builder.append("WHERE XMXX.DJLX<>'400' AND NVL2(XMXX.SFDB,'1','0')<>'1' ");
		builder.append("AND QL.DJDYID='" + djdyid + "' ");
		if (!StringHelper.isEmpty(xmbh)) {
			builder.append("AND XMXX.XMBH<>'" + xmbh + "'");
		}
		List<Map> qls_gz = basecommondao.getDataListByFullSql(builder.toString());
		for (Map ql : qls_gz) {
			String ywlsh = StringHelper.formatDouble(ql.get("YWLSH"));
			String xmdjlx = StringHelper.formatDouble(ql.get("XMDJLX"));
			String xmqllx = StringHelper.formatDouble(ql.get("XMQLLX"));
			String qldjlx = StringHelper.formatDouble(ql.get("QLDJLX"));
			String qlqllx = StringHelper.formatDouble(ql.get("QLQLLX"));
			if (DJLX.CFDJ.Value.equals(xmdjlx)) {
				if ("98".equals(xmqllx)) {
					status.setSeizureState("正在办理查封");
				}
			}
			if (DJLX.YYDJ.Value.equals(xmdjlx)) {
				status.setObjectionState("正在办理异议");
			} else if (DJLX.YGDJ.Value.equals(xmdjlx)) {
				if (QLLX.QTQL.Value.equals(xmqllx)) {
					status.setTransferNoticeState("正在办理转移预告");
				} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("正在办理抵押");
					} else {
						status.setMortgageNoticeState("正在办理抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
				status.setMortgageState("正在办理抵押");
			}
		}
		// 已办状态
		List<Rights> qls_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "'");
		for (Rights ql : qls_xz) {
			String djlx = ql.getDJLX();
			String qllx = ql.getQLLX();
			if (DJLX.CFDJ.Value.equals(djlx)) {
				status.setSeizureState("已查封");
			}
			if (DJLX.YYDJ.Value.equals(djlx)) {
				status.setObjectionState("已异议");
			} else if (DJLX.YGDJ.Value.equals(djlx)) {
				if (QLLX.QTQL.Value.equals(qllx)) {
					status.setTransferNoticeState("已转移预告");
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("已抵押");
					} else {
						status.setMortgageNoticeState("已抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(qllx)) {
				status.setMortgageState("已抵押");
			}
		}

		List<BDCS_DYXZ> list_limit = basecommondao.getDataList(BDCS_DYXZ.class,
				"YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "' ORDER BY YXBZ ");
		if (list_limit != null && list_limit.size() > 0) {
			for (BDCS_DYXZ limit : list_limit) {
				if ("1".equals(limit.getYXBZ())) {
					status.setLimitState("已限制");
				} else {
					status.setLimitState("正在办理限制");
				}
			}
		}

		String sql1 = " FROM BDCK.BDCS_QL_XZ QL WHERE QLLX='23' AND   DJDYID ='" + djdyid
				+ "' AND QLID NOT IN (SELECT LYQLID FROM BDCK.BDCS_QL_GZ WHERE XMBH='" + xmbh
				+ "')  AND QLID NOT IN (SELECT QLID FROM BDCK.BDCS_QL_GZ WHERE XMBH='" + xmbh + "')";
		long count1 = basecommondao.getCountByFullSql(sql1);
		count1++;
		Map<String, String> map = new HashMap<String, String>();
		status.setDYSW(Long.toString(count1));
		return status;
	}

	private UnitStatus getStatusFromYCSCStatus(List<UnitStatus> list_status_sch, List<UnitStatus> list_status_ych,
			BDCDYLX bdcdylx) {
		UnitStatus status = new UnitStatus();
		if (BDCDYLX.H.equals(bdcdylx)) {
			UnitStatus status_sch = list_status_sch.get(0);
			status_sch.setLimitState("现房" + status_sch.getLimitState());
			status_sch.setMortgageNoticeState("现房" + status_sch.getMortgageNoticeState());
			status_sch.setMortgageState("现房" + status_sch.getMortgageState());
			status_sch.setObjectionState("现房" + status_sch.getObjectionState());
			status_sch.setSeizureState("现房" + status_sch.getSeizureState());
			status_sch.setTransferNoticeState("现房" + status_sch.getTransferNoticeState());
			if (list_status_ych != null && list_status_ych.size() > 0) {
				UnitStatus status_ych = new UnitStatus();
				status_ych.setLimitState("期房" + status_ych.getLimitState());
				status_ych.setMortgageNoticeState("期房" + status_ych.getMortgageNoticeState());
				status_ych.setMortgageState("期房" + status_ych.getMortgageState());
				status_ych.setObjectionState("期房" + status_ych.getObjectionState());
				status_ych.setSeizureState("期房" + status_ych.getSeizureState());
				status_ych.setTransferNoticeState("期房" + status_ych.getTransferNoticeState());
				for (UnitStatus status_ych_1 : list_status_ych) {
					if (!status_ych_1.getLimitState().contains("无")) {
						if (!status_ych.getLimitState().contains("已")) {
							status_ych.setLimitState("期房" + status_ych_1.getLimitState());
						}
					}
					if (!status_ych_1.getMortgageState().contains("无")) {
						if (!status_ych.getMortgageState().contains("已")) {
							status_ych.setMortgageState("期房" + status_ych_1.getMortgageState());
						}
					}
					if (!status_ych_1.getObjectionState().contains("无")) {
						if (!status_ych.getObjectionState().contains("已")) {
							status_ych.setObjectionState("期房" + status_ych_1.getObjectionState());
						}
					}
					if (!status_ych_1.getSeizureState().contains("无")) {
						if (!status_ych.getSeizureState().contains("已")) {
							status_ych.setSeizureState("期房" + status_ych_1.getSeizureState());
						}
					}
				}
				status.setLimitState(status_sch.getLimitState() + " " + status_ych.getLimitState());
				status.setMortgageNoticeState(
						status_sch.getMortgageNoticeState() + " ");
				//+ status_ych.getMortgageNoticeState());//只有现房有抵押预告
				status.setMortgageState(status_sch.getMortgageState() + " " + status_ych.getMortgageState());
				status.setObjectionState(status_sch.getObjectionState() + " " + status_ych.getObjectionState());
				status.setSeizureState(status_sch.getSeizureState() + " " + status_ych.getSeizureState());
				status.setTransferNoticeState(
						status_sch.getTransferNoticeState() + " ");
				//+ status_ych.getTransferNoticeState());//只有现房有转移预告
			} else {
				status = status_sch;
			}
		} else {
			UnitStatus status_ych = list_status_ych.get(0);
			status_ych.setLimitState("期房" + status_ych.getLimitState());
			status_ych.setMortgageNoticeState("期房" + status_ych.getMortgageNoticeState());
			status_ych.setMortgageState("期房" + status_ych.getMortgageState());
			status_ych.setObjectionState("期房" + status_ych.getObjectionState());
			status_ych.setSeizureState("期房" + status_ych.getSeizureState());
			status_ych.setTransferNoticeState("期房" + status_ych.getTransferNoticeState());
			if (list_status_sch != null && list_status_sch.size() > 0) {
				UnitStatus status_sch = new UnitStatus();
				for (UnitStatus status_sch_1 : list_status_sch) {
					if (!status_sch_1.getLimitState().contains("无")) {
						if (!status_sch.getLimitState().contains("已")) {
							status_sch.setLimitState("现房" + status_sch_1.getLimitState());
						}
					}
					if (!status_sch_1.getMortgageState().contains("无")) {
						if (!status_sch.getMortgageState().contains("已")) {
							status_sch.setMortgageState("现房" + status_sch_1.getMortgageState());
						}
					}
					if (!status_sch_1.getObjectionState().contains("无")) {
						if (!status_sch.getObjectionState().contains("已")) {
							status_sch.setObjectionState("现房" + status_sch_1.getObjectionState());
						}
					}
					if (!status_sch_1.getSeizureState().contains("无")) {
						if (!status_sch.getSeizureState().contains("已")) {
							status_sch.setSeizureState("现房" + status_sch_1.getSeizureState());
						}
					}
					if (!status_sch_1.getMortgageNoticeState().contains("无")) {
						if (!status_sch.getMortgageNoticeState().contains("已")) {
							status_sch.setMortgageNoticeState("现房" + status_sch_1.getMortgageNoticeState());
						}
					}
					if (!status_sch_1.getTransferNoticeState().contains("无")) {
						if (!status_sch.getTransferNoticeState().contains("已")) {
							status_sch.setTransferNoticeState("现房" + status_sch_1.getTransferNoticeState());
						}
					}
				}
				status.setLimitState(status_ych.getLimitState() + " " + status_sch.getLimitState());
				status.setMortgageNoticeState(
						status_ych.getMortgageNoticeState() + " " + status_sch.getMortgageNoticeState());
				status.setMortgageState(status_ych.getMortgageState() + " " + status_sch.getMortgageState());
				status.setObjectionState(status_ych.getObjectionState() + " " + status_sch.getObjectionState());
				status.setSeizureState(status_ych.getSeizureState() + " " + status_sch.getSeizureState());
				status.setTransferNoticeState(
						status_ych.getTransferNoticeState() + " " + status_sch.getTransferNoticeState());
			} else {
				status = status_ych;
			}
		}
		return status;
	}

	public String FormatDouble(Object obj, Integer number_L, Integer number_R) {
		String value = "";
		if (!StringHelper.isEmpty(obj)) {
			Double d = StringHelper.getDouble(obj);
			if (d == 0) {
				return "";
			}
			String formattype = "";
			if (!StringHelper.isEmpty(number_L) && number_L > 0) {
				for (int i = 0; i < number_L; i++) {
					if (i == number_L - 1) {
						formattype = formattype + "0";
					} else {
						formattype = formattype + "#";
					}
				}
			}
			if (!StringHelper.isEmpty(number_R) && number_R > 0) {
				formattype = formattype + ".";
				for (int i = 0; i < number_R; i++) {
					formattype = formattype + "0";
				}
			}
			if (StringHelper.isEmpty(formattype)) {
				formattype = "##################.###";
			}
			DecimalFormat df = new DecimalFormat(formattype);
			df.setRoundingMode(RoundingMode.HALF_UP);
			value = df.format(d);
		}
		return value;
	}

	/**
	 * 根据属性名获取属性值
	 */
	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	private String getEntityName(String bdcdylx) {
		String entityname = "";
		if ("01".equals(bdcdylx)) {
			entityname = "SYQZD";
		} else if ("02".equals(bdcdylx)) {
			entityname = "SHYQZD";
		} else if ("03".equals(bdcdylx)) {
			entityname = "SCZRZ";
		} else if ("08".equals(bdcdylx)) {
			entityname = "YCZRZ";
		} else if ("031".equals(bdcdylx)) {
			entityname = "SCH";
		} else if ("032".equals(bdcdylx)) {
			entityname = "YCH";
		} else if ("04".equals(bdcdylx)) {
			entityname = "HY";
		} else if ("05".equals(bdcdylx)) {
			entityname = "SLLM";
		} else if ("09".equals(bdcdylx)) {
			entityname = "NYD";
		} else if ("tdyt".equals(bdcdylx)) {
			entityname = "TDYTLIST";
		} else if ("yhzk".equals(bdcdylx)) {
			entityname = "YHZKLIST";
		} else if ("yhydzb".equals(bdcdylx)) {
			entityname = "YHYDZBLIST";
		} else if ("ljz".equals(bdcdylx)) {
			entityname = "LJZ";
		} else if ("c".equals(bdcdylx)) {
			entityname = "C";
		} else if ("ycljz".equals(bdcdylx)) {
			entityname = "YCLJZ";
		} else if ("ycc".equals(bdcdylx)) {
			entityname = "YCC";
		}
		return entityname;
	}

	private String getBDCDYLX(String entityname) {
		String bdcdylx = "";
		if ("SYQZD".equals(entityname)) {
			bdcdylx = "01";
		} else if ("SHYQZD".equals(entityname)) {
			bdcdylx = "02";
		} else if ("SCZRZ".equals(entityname)) {
			bdcdylx = "03";
		} else if ("YCZRZ".equals(entityname)) {
			bdcdylx = "08";
		} else if ("NYD".equals(entityname)) {
			bdcdylx = "09";
		} else if ("SCH".equals(entityname)) {
			bdcdylx = "031";
		} else if ("YCH".equals(entityname)) {
			bdcdylx = "032";
		} else if ("SLLM".equals(entityname)) {
			bdcdylx = "05";
		} else if ("HY".equals(entityname)) {
			bdcdylx = "04";
		} else if ("TDYTLIST".equals(entityname)) {
			bdcdylx = "tdyt";
		} else if ("YHZKLIST".equals(entityname)) {
			bdcdylx = "yhzk";
		} else if ("YHYDZBLIST".equals(entityname)) {
			bdcdylx = "yhydzb";
		} else if ("LJZ".equals(entityname)) {
			bdcdylx = "ljz";
		} else if ("C".equals(entityname)) {
			bdcdylx = "c";
		} else if ("YCLJZ".equals(entityname)) {
			bdcdylx = "ycljz";
		} else if ("YCC".equals(entityname)) {
			bdcdylx = "ycc";
		}
		return bdcdylx;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	@RequestMapping(value = "/ttt/index/")
	public String geTTTIndex() {
		return prefix + "checkrulemanage/ttt";
	}

	@RequestMapping(value = "/unitpagedemo/index/")
	public String geUnitPageDemoIndex() {
		return prefix + "unitpagemanager/unitpagedemo";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/houseconfig/", method = RequestMethod.GET)
	public @ResponseBody List<Map> GetHouseConfig(HttpServletRequest request, HttpServletResponse response) {
		List<Map> list = basecommondao.getDataListByFullSql(
				"SELECT RTMODULE.* FROM BDCK.RT_UNIT_PAGEMANAGER RTPAGE LEFT JOIN BDCK.RT_UNIT_MODULEMANAGER RTMODULE ON RTPAGE.MODULEID=RTMODULE.MODULEID WHERE RTPAGE.PAGEID='12e800f3c7b34db89da2430956b85256'");
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updateunitinfo/{qlid}/{xmbh}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateUnitInfo(
			@PathVariable("qlid") String qlid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("更新成功！");
		List<BDCS_DJDY_GZ> list_djdy=basecommondao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND DJDYID IN (SELECT DJDYID FROM BDCS_QL_GZ WHERE id='"+qlid+"')");
		if(list_djdy!=null&&list_djdy.size()>0){
			RealUnit unit_update = UnitTools.loadUnit(BDCDYLX.initFrom(list_djdy.get(0).getBDCDYLX()),
					DJDYLY.initFrom(list_djdy.get(0).getLY()), list_djdy.get(0).getBDCDYID());
			String info = request.getParameter("info");
			JSONObject jsonInfo = JSONObject.fromObject(info);
			
			Map dy_map = new HashMap<String, Object>();
			Iterator it = jsonInfo.keys();
			// 遍历jsonObject数据，添加到Map对象
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = StringHelper.formatObject(jsonInfo.get(key));
				dy_map.put(key, value);
			}
			if (IsDyChange(dy_map, unit_update)) {
				if (BackBDCDYInfo(xmbh, unit_update.getBDCDYLX(),
						unit_update.getLY(), unit_update.getId())) {
					UpdateBDCDYInfo(dy_map, unit_update, true);
				}
			}
		}
		return msg;
	}

	/***************************************** 单元模块管理 *****************************************/
}
