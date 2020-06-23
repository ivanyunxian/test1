package com.supermap.realestate.registration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_OPINION;
import com.supermap.realestate.registration.model.BDCS_TEMPLATECLASS;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;

import net.sf.json.JSONArray;

@Service("opinionManagerService")
public class OpinionManagerService {
	@Autowired
	CommonDao _CommonDao;

	public void DeleteTplClassById(String id) {
		// 同时删除子目录数据
		StringBuilder str = new StringBuilder();
		StringBuilder str2 = new StringBuilder();
		str.append("OPINIONTYPE_ID='");
		str.append(id);
		str.append("'");
		str2.append("TYPE_PID='");
		str2.append(id);
		str2.append("'");
		List<BDCS_OPINION> list = _CommonDao.findList(BDCS_OPINION.class,
				str.toString());

		for (int i = 0; i < list.size(); i++) {
			BDCS_OPINION Opinion = list.get(i);
			_CommonDao.delete(Opinion);
		}

		List<BDCS_TEMPLATECLASS> list2 = _CommonDao.findList(BDCS_TEMPLATECLASS.class,
				str2.toString());
		for (int i = 0; i < list2.size(); i++) {
			BDCS_TEMPLATECLASS template = list2.get(i);
			DeleteTplClassById(template.getTYPE_ID());
			_CommonDao.delete(template);
		}

		_CommonDao.delete(BDCS_TEMPLATECLASS.class, id);
		_CommonDao.flush();
	}

	public void DeleteOpinionById(String id) {
		_CommonDao.delete(BDCS_OPINION.class, id);
		_CommonDao.flush();
	}
	
	public List<BDCS_OPINION> FindAllOpinionInfo(String id) {
		StringBuilder str = new StringBuilder();
		str.append("OPINIONTYPE_ID='");
		str.append(id);
		str.append("'");
		str.append(" order by OPINION_INDEX");
		return _CommonDao.findList(BDCS_OPINION.class, str.toString());
	}
	
	public Message FindAllOpinionInfo(String id, int CurrentPageIndex, int pageSize, String opinion_name) {
		
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("from BDCK.BDCS_OPINION where 1=1 ");
		str.append("and OPINIONTYPE_ID='");
		str.append(id);
		str.append("'");
		
		if (!StringUtils.isEmpty(opinion_name)) {
			str.append(" and opinion_name like ");
			str.append("'%" + opinion_name + "%'");
		}
		str.append(" order by OPINION_INDEX");
		
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

		if (StringUtils.isEmpty(Pid)) {
			str.append(" TYPE_PID is null or TYPE_PID='0' ");
		} else {
			str.append(" TYPE_PID ='");
			str.append(Pid);
			str.append("'");
		}

		str.append(" and MBBS = '1'");
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

		if (!StringUtils.isEmpty(Pid)) {
			StringBuilder str2 = new StringBuilder();
			str2.append(" OPINIONTYPE_ID ='");
			str2.append(Pid);
			str2.append("' order by OPINION_INDEX");

			List<BDCS_OPINION> list2 = _CommonDao.findList(BDCS_OPINION.class,
					str2.toString());
			for (int i = 0; i < list2.size(); i++) {
				TreeInfo tree = new TreeInfo();
				tree = new TreeInfo();
				tree.setId(list2.get(i).getOPINIONDEF_ID());
				tree.setText(list2.get(i).getOPINION_NAME());
				tree.setType("data");
				_treelist.add(tree);
			}
		}

		return _treelist;
	}

	public BDCS_OPINION GetOpinionById(String id) {
		return _CommonDao.get(BDCS_OPINION.class, id);
	}

	public BDCS_TEMPLATECLASS GetTplClassById(String id) {
		return _CommonDao.get(BDCS_TEMPLATECLASS.class, id);
	}

	public SmObjInfo SaveOrUpdate(BDCS_OPINION opinion) {
		SmObjInfo info = new SmObjInfo();
		if (StringUtils.isEmpty(opinion.getOPINIONDEF_ID().toString())) {
			opinion.setOPINIONDEF_ID(Common.CreatUUID());
		}
		_CommonDao.saveOrUpdate(opinion);
		_CommonDao.flush();
		info.setID(opinion.getOPINIONDEF_ID());
		info.setName(opinion.getOPINION_NAME());
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
		TemplateClass.setMBBS("1");
		_CommonDao.saveOrUpdate(TemplateClass);
		_CommonDao.flush();
		return TemplateClass.getTYPE_ID();
	}

	public SmObjInfo RenameTplClass(String id, String name) {
		SmObjInfo smInfo = new SmObjInfo();
		if (!StringUtils.isEmpty(id)) {
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

	public Page FindAllOpinions(int page, int rows, String OPINIONTYPE_ID) {
		StringBuilder str = new StringBuilder();
		str.append("1=1");
		if (!StringUtils.isEmpty(OPINIONTYPE_ID)){
			str.append(" and OPINIONTYPE_ID = ");
			str.append("'" + OPINIONTYPE_ID + "'");
		}
		str.append(" order by OPINION_INDEX");
		return _CommonDao.GetPagedData(BDCS_OPINION.class, page, rows, str.toString());
	}
	
	public List<Map> FindListbox() {
		StringBuilder str = new StringBuilder();
		str.append("SELECT t.TYPE_ID id,t.TYPE_NAME name FROM BDCK.BDCS_TEMPLATECLASS t");
		str.append(" WHERE t.TYPE_PID <> '0' ");
		str.append(" AND t.MBBS='1' ");
		str.append(" ORDER BY t.TYPE_INDEX");
		List<Map> list = _CommonDao.getDataListByFullSql(str.toString());
		JSONArray array = JSONArray.fromObject(list);
		return array;
	}
	
}
