package com.supermap.realestate.registration.service;

import com.supermap.realestate.registration.model.SMS_PARAM_TEMPLATE;
import com.supermap.realestate.registration.model.SMS_TEMPLATE;
import com.supermap.realestate.registration.model.SMS_TEMPLATECLASS;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("smsTemplateManagerService")
public class SmsTemplateManagerService {
	@Autowired
	CommonDao _CommonDao;
	@Autowired
	private SmStaff smStaff;

	public void DeleteTplClassById(String id) {
		// 同时删除子目录数据
		StringBuilder str = new StringBuilder();
		str.append("CLASS_ID='");
		str.append(id);
		str.append("'");
		List<SMS_TEMPLATE> list = _CommonDao.findList(SMS_TEMPLATE.class,
				str.toString());

		for (SMS_TEMPLATE template : list) {
			_CommonDao.delete(template);
		}

		StringBuilder str2 = new StringBuilder();
		str2.append("CLASS_PID='");
		str2.append(id);
		str2.append("'");
		List<SMS_TEMPLATECLASS> templateclassList = _CommonDao.findList(SMS_TEMPLATECLASS.class,
				str2.toString());
		for (SMS_TEMPLATECLASS  templateclass :templateclassList) {
			DeleteTplClassById(templateclass.getCLASSID());
			_CommonDao.delete(templateclass);
		}

		_CommonDao.delete(SMS_TEMPLATECLASS.class, id);
		_CommonDao.flush();
	}

	public void DeleteTemplateById(String id) {
		_CommonDao.delete(SMS_TEMPLATE.class, id);
		_CommonDao.flush();
	}
	
	public Message FindAllTemplateInfo(String id, int CurrentPageIndex, int pageSize, String template_name) {
		Message msg = new Message();
		StringBuilder wherehql = new StringBuilder();
		wherehql.append("CLASS_ID='");
		wherehql.append(id);
		wherehql.append("'");

		if (!StringHelper.isEmpty(template_name)) {
			wherehql.append(" AND TEMPLATE_NAME LIKE ");
			wherehql.append("'%" + template_name + "%'");
		}
		wherehql.append(" ORDER BY TEMPLATE_INDEX DESC");
		Page page = _CommonDao.GetPagedData(SMS_TEMPLATE.class, CurrentPageIndex, pageSize, wherehql.toString());
		msg.setRows(page.getResult());
		msg.setTotal(page.getTotalCount());
		
		return msg;
	}

	public Message FindAllTemplates(int CurrentPageIndex, int pageSize, String template_name) {
		Message msg = new Message();
		StringBuilder wherehql = new StringBuilder();
		wherehql.append("1=1");
		if (!StringHelper.isEmpty(template_name)) {
			wherehql.append(" AND LABEL_NAME LIKE ");
			wherehql.append("'%" + template_name + "%'");
		}
		wherehql.append(" ORDER BY TEMPLATE_INDEX DESC");
		Page page = _CommonDao.GetPagedData(SMS_TEMPLATE.class, CurrentPageIndex, pageSize, wherehql.toString());
		msg.setRows(page.getResult());
		msg.setTotal(page.getTotalCount());

		return msg;
	}

	public List<TreeInfo> TplTree() {
		return TemplateChildTree(null);
	}

	public List<TreeInfo> TemplateChildTree(String Pid) {
		StringBuilder str = new StringBuilder();

		if (Pid == null) {
			str.append(" (CLASS_PID IS NULL OR CLASS_PID='0') ");
		} else {
			str.append(" CLASS_PID ='");
			str.append(Pid);
			str.append("'");
		}

		//获取类型为标签的
		str.append(" AND CLASS_TYPE = '1'");
		str.append(" ORDER BY CLASS_INDEX DESC");

		List<SMS_TEMPLATECLASS> list = _CommonDao.findList(SMS_TEMPLATECLASS.class,
				str.toString());

		List<TreeInfo> _treelist = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			TreeInfo tree = new TreeInfo();
			tree.setId(list.get(i).getCLASSID());
			tree.setText(list.get(i).getCLASSNAME());
			tree.setType("catalog");
			tree.setState("closed");
			tree.children = TemplateChildTree(list.get(i).getCLASSID());
			_treelist.add(tree);
		}

		if (Pid != null) {
			StringBuilder str2 = new StringBuilder();
			str2.append(" CLASS_ID ='");
			str2.append(Pid);
			str2.append("' ORDER BY TEMPLATE_INDEX DESC");

			List<SMS_TEMPLATE> list2 = _CommonDao.findList(SMS_TEMPLATE.class,
					str2.toString());
			for (int i = 0; i < list2.size(); i++) {
				TreeInfo tree = new TreeInfo();
				tree = new TreeInfo();
				tree.setId(list2.get(i).getTEMPLATEID());
				tree.setText(list2.get(i).getTEMPLATENAME());
				tree.setType("data");
				_treelist.add(tree);
			}
		}

		return _treelist;
	}

	public SMS_TEMPLATE GetTemplateById(String id) {
		return _CommonDao.get(SMS_TEMPLATE.class, id);
	}

	public SMS_TEMPLATECLASS GetTplClassById(String id) {
		return _CommonDao.get(SMS_TEMPLATECLASS.class, id);
	}

	public SmObjInfo SaveOrUpdate(SMS_TEMPLATE template) {
		SmObjInfo info = new SmObjInfo();
		if (StringHelper.isEmpty(template.getTEMPLATEID())) {
			template.setTEMPLATEID(Common.CreatUUID());
		}
		if (StringHelper.isEmpty(template.getTENANTID())) {
			template.setTENANTID(ConfigHelper.getNameByValue("XZQHDM"));
		}
		if (StringHelper.isEmpty(template.getTEMPLATECODE())) {
			info.setDesc("保存失败，模板代码不能为空！");
			return info;
		}
		_CommonDao.saveOrUpdate(template);
		_CommonDao.flush();
		info.setID(template.getTEMPLATEID());
		info.setName(template.getTEMPLATENAME());
		info.setDesc("保存成功");
		return info;
	}

	public void SaveOrUpdate(SMS_TEMPLATECLASS templateclass) {
		_CommonDao.saveOrUpdate(templateclass);
		_CommonDao.flush();
	}

	public SmObjInfo CreateTplClassByName(String name, String pid) {
		SmObjInfo info = new SmObjInfo();
		SMS_TEMPLATECLASS templateclass = new SMS_TEMPLATECLASS();
		templateclass.setCLASSNAME(name);
		templateclass.setCLASSPID(pid);
		templateclass.setCLASSTYPE("1");
		templateclass.setCREATEDATE(new Date());
		templateclass.setTENANTID(ConfigHelper.getNameByValue("XZQHDM"));

		_CommonDao.saveOrUpdate(templateclass);
		_CommonDao.flush();
		info.setID(templateclass.getCLASSID());
		info.setName(templateclass.getCLASSNAME());
		info.setDesc("新增成功");
		return info;
	}

	public SmObjInfo RenameTplClass(String id, String name) {
		SmObjInfo smInfo = new SmObjInfo();
		if (!StringHelper.isEmpty(id)) {
			SMS_TEMPLATECLASS templateclass = _CommonDao.get(SMS_TEMPLATECLASS.class, id);
			templateclass.setCLASSNAME(name);
			_CommonDao.update(templateclass);
			_CommonDao.flush();
			smInfo.setID(templateclass.getCLASSID());
			smInfo.setDesc("更新成功");
		}
		return smInfo;
	}

	public List<Map> GetTplClassData() {
		StringBuilder str = new StringBuilder();
		str.append("SELECT T.CLASS_ID ID, T.CLASS_NAME NAME FROM BDCK.SMS_TEMPLATECLASS T");
		str.append(" WHERE T.CLASS_TYPE='1' ");
		str.append(" ORDER BY T.CLASS_INDEX DESC");
		List<Map> list = _CommonDao.getDataListByFullSql(str.toString());
		JSONArray array = JSONArray.fromObject(list);
		return list;
	}

	public List<Map> GetTemplateData(String classId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT T.TEMPLATE_ID ID, T.TEMPLATE_CODE CODE, T.TEMPLATE_NAME NAME, TEMPLATE_CONTENT CONTENT FROM BDCK.SMS_TEMPLATE T");
		str.append(" WHERE T.CLASS_ID='");
		str.append(classId).append("'");
		str.append(" ORDER BY T.TEMPLATE_INDEX DESC");
		List<Map> list = _CommonDao.getDataListByFullSql(str.toString());
		JSONArray array = JSONArray.fromObject(list);
		return list;
	}

	/****************************************常用参数模板相关 START********************************************/
	public Message GetParamTpl(HttpServletRequest request) {
		Message msg = new Message();
		try {
			String currpage = request.getParameter("currpage");
			String pagesize = request.getParameter("pagesize");
			if (StringUtils.isEmpty(currpage)) {
				currpage = "1";
			}
			if (StringHelper.isEmpty(pagesize)) {
				pagesize = "10";
			}
			String staffid = smStaff.getCurrentWorkStaffID();
			if (staffid != null && !"".equals(staffid)) {
				StringBuilder wherehql = new StringBuilder();
				wherehql.append("STAFF_ID='");
				wherehql.append(staffid);
				wherehql.append("' AND YXBZ='0' ORDER BY XH");
				Page page = _CommonDao.GetPagedData(SMS_PARAM_TEMPLATE.class, StringHelper.getInt(currpage), StringHelper.getInt(pagesize), wherehql.toString());
				msg.setTotal(page.getTotalCount());
				msg.setRows(page.getResult());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public Message AddParamTpl(HttpServletRequest request) {
		Message msg = new Message();
		msg.setSuccess("false");
		msg.setMsg("新增失败");
		try {
			String staffid = smStaff.getCurrentWorkStaffID();
			String content = request.getParameter("content");
			if (StringHelper.isEmpty(content)) {
				msg.setMsg("参数模板内容不能为空！");
				return msg;
			}
			SMS_PARAM_TEMPLATE smsParamTemplate = null;
			smsParamTemplate = new SMS_PARAM_TEMPLATE();
			smsParamTemplate.setID(Common.CreatUUID());
			smsParamTemplate.setCONTENT(content);
			smsParamTemplate.setSTAFF_ID(staffid);
			smsParamTemplate.setTENANTID(ConfigHelper.getNameByValue("XZQHDM"));
			smsParamTemplate.setXH(System.currentTimeMillis());
			_CommonDao.save(smsParamTemplate);
			_CommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("新增成功");
		} catch (Exception e) {
			msg.setMsg("新增失败,详情：" + e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	public Message ModifyParamTpl(String id, HttpServletRequest request) {
		Message msg = new Message();
		msg.setSuccess("false");
		msg.setMsg("更新失败!");
		try {
			if (StringHelper.isEmpty(id)) {
				msg.setMsg("待更新记录ID不能为空！");
				return msg;
			}
			String content = request.getParameter("content");
			if (StringHelper.isEmpty(content)) {
				msg.setMsg("参数模板内容不能为空！");
				return msg;
			}
			SMS_PARAM_TEMPLATE smsParamTemplate = _CommonDao.get(SMS_PARAM_TEMPLATE.class, id);
			if (smsParamTemplate == null) {
				msg.setMsg("未找到ID为"+ id +"的参数模版，无法更新！");
				return msg;
			}
			smsParamTemplate.setCONTENT(content);
			_CommonDao.update(smsParamTemplate);
			_CommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("更新成功");
		} catch (Exception e) {
			msg.setMsg("更新失败,详情：" + e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	public Message DeleteParamTpl(String id, HttpServletRequest request) {
		Message msg = new Message();
		msg.setSuccess("false");
		msg.setMsg("删除失败!");
		try {
			if (StringHelper.isEmpty(id)) {
				msg.setMsg("待删除记录ID不能为空！");
				return msg;
			}
			SMS_PARAM_TEMPLATE smsParamTemplate = _CommonDao.get(SMS_PARAM_TEMPLATE.class, id);
			if (smsParamTemplate == null) {
				msg.setMsg("未找到ID为"+ id +"的参数模版，无法删除！");
				return msg;
			}
			_CommonDao.delete(smsParamTemplate);
			_CommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} catch (Exception e) {
			msg.setMsg("删除失败,详情：" + e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}
	/****************************************常用参数模板相关 END********************************************/

}
