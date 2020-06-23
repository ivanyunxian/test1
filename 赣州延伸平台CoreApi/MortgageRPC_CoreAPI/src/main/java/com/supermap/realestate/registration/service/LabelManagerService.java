package com.supermap.realestate.registration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_LABEL;
import com.supermap.realestate.registration.model.BDCS_TEMPLATECLASS;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;

@Service("labelManagerService")
public class LabelManagerService {
	@Autowired
	CommonDao _CommonDao;

	public void DelTplClassById(String id) {
		// 同时删除子目录数据
		StringBuilder str = new StringBuilder();
		StringBuilder str2 = new StringBuilder();
		str.append("LABELTYPEID='");
		str.append(id);
		str.append("'");
		str2.append("TYPE_PID='");
		str2.append(id);
		str2.append("'");
		List<BDCS_LABEL> list = _CommonDao.findList(BDCS_LABEL.class,
				str.toString());

		for (int i = 0; i < list.size(); i++) {
			BDCS_LABEL label = list.get(i);
			_CommonDao.delete(label);
		}

		List<BDCS_TEMPLATECLASS> list2 = _CommonDao.findList(BDCS_TEMPLATECLASS.class,
				str2.toString());
		for (int i = 0; i < list2.size(); i++) {
			BDCS_TEMPLATECLASS template = list2.get(i);
			DelTplClassById(template.getTYPE_ID());
			_CommonDao.delete(template);
		}

		_CommonDao.delete(BDCS_TEMPLATECLASS.class, id);
		_CommonDao.flush();
	}

	public void DeleteLabelInfoById(String id) {
		_CommonDao.delete(BDCS_LABEL.class, id);
		_CommonDao.flush();
	}
	
	public List<BDCS_LABEL> FindAllLabelInfo(String id) {
		StringBuilder str = new StringBuilder();
		str.append("LABELTYPEID='");
		str.append(id);
		str.append("'");
		str.append(" order by LABELINDEX");
		return _CommonDao.findList(BDCS_LABEL.class, str.toString());
	}
	
	public Page FindAllLabels(int page , int rows) {
		StringBuilder str = new StringBuilder();
		str.append("1=1");
		str.append(" order by LABELINDEX");
		return _CommonDao.GetPagedData(BDCS_LABEL.class, page, rows, str.toString());
	}
	
	public Message FindAllLabelInfo(String id, int CurrentPageIndex, int pageSize, String bqmc) {
		
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("from BDCK.BDCS_LABEL where 1=1 ");
		str.append("and LABELTYPEID='");
		str.append(id);
		str.append("'");
		
		if (!StringUtils.isEmpty(bqmc)) {
			str.append(" and BQMC like ");
			str.append("'%" + bqmc + "%'");
		}
		str.append(" order by LABELINDEX");
		
		long tatalCount = _CommonDao.getCountByFullSql(str.toString());
		msg.setTotal(tatalCount);
		if (tatalCount > 0) {
			List<Map> list;
			list = _CommonDao.getDataListByFullSql("select * " + str.toString(), CurrentPageIndex, pageSize);
			msg.setRows(list);
		}
		
		return msg;
	}

	public List<TreeInfo> TplTree() {
		return TemplateChildTree(null);
	}

	public List<TreeInfo> TemplateChildTree(String Pid) {
		StringBuilder str = new StringBuilder();

		if (Pid == null) {
			str.append(" TYPE_PID is null or TYPE_PID='0' ");
		} else {
			str.append(" TYPE_PID ='");
			str.append(Pid);
			str.append("'");
		}

		str.append(" and MBBS = '2'");
		str.append(" order by TYPE_INDEX");

		List<BDCS_TEMPLATECLASS> list = _CommonDao.findList(BDCS_TEMPLATECLASS.class,
				str.toString());

		List<TreeInfo> _treelist = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			TreeInfo tree = new TreeInfo();
			tree.setId(list.get(i).getTYPE_ID());
			tree.setText(list.get(i).getTYPE_NAME());
			tree.setType("catalog");
			tree.setState("closed");
			tree.children = TemplateChildTree(list.get(i).getTYPE_ID());
			_treelist.add(tree);

		}

		if (Pid != null) {
			StringBuilder str2 = new StringBuilder();
			str2.append(" LABELTYPEID ='");
			str2.append(Pid);
			str2.append("' order by LABELINDEX");

			List<BDCS_LABEL> list2 = _CommonDao.findList(BDCS_LABEL.class,
					str2.toString());
			for (int i = 0; i < list2.size(); i++) {
				TreeInfo tree = new TreeInfo();
				tree = new TreeInfo();
				tree.setId(list2.get(i).getBQBS());
				tree.setText(list2.get(i).getBQMC());
				tree.setType("data");
				_treelist.add(tree);
			}
		}

		return _treelist;
	}

	public BDCS_LABEL GetLabelInfoById(String id) {
		return _CommonDao.get(BDCS_LABEL.class, id);
	}

	public BDCS_TEMPLATECLASS GetTemplateClassById(String id) {
		return _CommonDao.get(BDCS_TEMPLATECLASS.class, id);
	}

	public SmObjInfo SaveOrUpdate(BDCS_LABEL label) {
		SmObjInfo info = new SmObjInfo();
		if (label.getBQBS().equals("")) {
			label.setBQBS(Common.CreatUUID());
		}
		_CommonDao.saveOrUpdate(label);
		_CommonDao.flush();
		info.setID(label.getBQBS());
		info.setName(label.getBQMC());
		info.setDesc("保存成功");
		return info;
	}

	public void SaveOrUpdate(BDCS_TEMPLATECLASS templateclass) {
		_CommonDao.saveOrUpdate(templateclass);
		_CommonDao.flush();
	}

	public String CreateTplClassByName(String name, String pid, int index) {
		BDCS_TEMPLATECLASS TemplateClass = new BDCS_TEMPLATECLASS();
		TemplateClass.setTYPE_NAME(name);
		TemplateClass.setTYPE_PID(pid);
		TemplateClass.setTYPE_INDEX(index);
		TemplateClass.setMBBS("2");
		_CommonDao.saveOrUpdate(TemplateClass);
		_CommonDao.flush();
		return TemplateClass.getTYPE_ID();
	}

	public SmObjInfo RenameTplClass(String id, String name) {
		SmObjInfo smInfo = new SmObjInfo();
		if (id != null && !id.equals("")) {
			BDCS_TEMPLATECLASS TemplateClass = _CommonDao
					.get(BDCS_TEMPLATECLASS.class, id);
			TemplateClass.setTYPE_NAME(name);
			;
			_CommonDao.update(TemplateClass);
			_CommonDao.flush();
			smInfo.setID(TemplateClass.getTYPE_ID());
			smInfo.setDesc("更新成功");
		}
		return smInfo;
	}


	
}
