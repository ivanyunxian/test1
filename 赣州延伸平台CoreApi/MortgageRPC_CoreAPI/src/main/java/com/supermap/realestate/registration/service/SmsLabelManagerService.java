package com.supermap.realestate.registration.service;

import com.supermap.realestate.registration.model.SMS_LABEL;
import com.supermap.realestate.registration.model.SMS_TEMPLATECLASS;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("smsLabelManagerService")
public class SmsLabelManagerService {
	@Autowired
	CommonDao _CommonDao;

	public void DelTplClassById(String id) {
		// 同时删除子目录数据
		StringBuilder str = new StringBuilder();
		str.append("CLASS_ID='");
        str.append(id);
        str.append("'");
        List<SMS_LABEL> list = _CommonDao.findList(SMS_LABEL.class,
                str.toString());

        for (SMS_LABEL label : list) {
			_CommonDao.delete(label);
		}

        StringBuilder str2 = new StringBuilder();
        str2.append("CLASS_PID='");
        str2.append(id);
        str2.append("'");
        List<SMS_TEMPLATECLASS> templateclassList = _CommonDao.findList(SMS_TEMPLATECLASS.class,
                str2.toString());
        for (SMS_TEMPLATECLASS  templateclass :templateclassList) {
            DelTplClassById(templateclass.getCLASSID());
            _CommonDao.delete(templateclass);
        }

		_CommonDao.delete(SMS_TEMPLATECLASS.class, id);
		_CommonDao.flush();
	}

	public void DeleteLabelInfoById(String id) {
		_CommonDao.delete(SMS_LABEL.class, id);
		_CommonDao.flush();
	}
	
	public Page FindAllLabels(int page , int rows) {
		StringBuilder str = new StringBuilder();
		str.append("1=1");
		str.append(" ORDER BY LABEL_INDEX DESC");
		return _CommonDao.GetPagedData(SMS_LABEL.class, page, rows, str.toString());
	}
	
	public Message FindAllLabelInfo(String id, int CurrentPageIndex, int pageSize, String label_name) {
		Message msg = new Message();
		StringBuilder wherehql = new StringBuilder();
		wherehql.append("CLASS_ID='");
		wherehql.append(id);
		wherehql.append("'");
		
		if (!StringHelper.isEmpty(label_name)) {
			wherehql.append(" AND LABEL_NAME LIKE ");
			wherehql.append("'%" + label_name + "%'");
		}
		wherehql.append(" ORDER BY LABEL_INDEX DESC");
        Page page = _CommonDao.GetPagedData(SMS_LABEL.class, CurrentPageIndex, pageSize, wherehql.toString());
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
		str.append(" AND CLASS_TYPE = '2'");
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
			str2.append("' ORDER BY LABEL_INDEX DESC");

			List<SMS_LABEL> list2 = _CommonDao.findList(SMS_LABEL.class,
					str2.toString());
			for (int i = 0; i < list2.size(); i++) {
				TreeInfo tree = new TreeInfo();
				tree = new TreeInfo();
				tree.setId(list2.get(i).getLABELID());
				tree.setText(list2.get(i).getLABELNAME());
				tree.setType("data");
				_treelist.add(tree);
			}
		}

		return _treelist;
	}

	public SMS_LABEL GetLabelInfoById(String id) {
		return _CommonDao.get(SMS_LABEL.class, id);
	}

	public SMS_TEMPLATECLASS GetTemplateClassById(String id) {
		return _CommonDao.get(SMS_TEMPLATECLASS.class, id);
	}

	public SmObjInfo SaveOrUpdate(SMS_LABEL label) {
		SmObjInfo info = new SmObjInfo();
		if (StringHelper.isEmpty(label.getLABELID())) {
			label.setLABELID(Common.CreatUUID());
		}
		if (StringHelper.isEmpty(label.getTENANTID())) {
			label.setTENANTID(ConfigHelper.getNameByValue("XZQHDM"));
		}
		_CommonDao.saveOrUpdate(label);
		_CommonDao.flush();
		info.setID(label.getLABELID());
		info.setName(label.getLABELNAME());
		info.setDesc("保存成功");
		return info;
	}

	public void SaveOrUpdate(SMS_TEMPLATECLASS templateclass) {
		_CommonDao.saveOrUpdate(templateclass);
		_CommonDao.flush();
	}

	public SmObjInfo CreateTplClassByName(String name, String pid, Long index) {
		SmObjInfo info = new SmObjInfo();
		SMS_TEMPLATECLASS templateclass = new SMS_TEMPLATECLASS();
		templateclass.setCLASSNAME(name);
		templateclass.setCLASSPID(pid);
		templateclass.setCLASSTYPE("2");
		templateclass.setCREATEDATE(new Date());
		templateclass.setCLASSINDEX(index);
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


	
}
