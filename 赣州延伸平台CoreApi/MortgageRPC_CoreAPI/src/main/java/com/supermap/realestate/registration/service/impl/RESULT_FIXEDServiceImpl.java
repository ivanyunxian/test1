package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.RESULT_FIXED;
import com.supermap.realestate.registration.service.RESULT_FIXEDService;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;

@Service("RESULT_FIXEDService")
public class RESULT_FIXEDServiceImpl implements RESULT_FIXEDService {

	@Autowired
	private CommonDao Dao;

	@Override
	public Message GetRESULT_FIXED(Map<String, String> param) {
		Page page = Dao.getPageDataByHql(RESULT_FIXED.class, "1>0", Integer.parseInt(param.get("page")), Integer.parseInt(param.get("rows")));
		List<?> rows = page.getResult();
		Message res = new Message();
		if(rows.size()>0){
			res.setRows(rows);
		}else{
			res.setRows(new ArrayList<RESULT_FIXED>());
		}
		res.setMsg("true");
		return res;
	}

	@Override
	public Message AddRESULT_FIXED(Map<String, String> param) {
		Message msg = new Message();
		List<RESULT_FIXED> Result = JSONObject.parseArray(param.get("jsonEnity"),RESULT_FIXED.class);
		for (RESULT_FIXED result_FIXED : Result) {
			result_FIXED.setId(SuperHelper.GeneratePrimaryKey().toString());
			result_FIXED.setCREATETIME(new Date());
			Dao.save(result_FIXED);
		}
		Dao.flush();
		msg.setMsg("添加成功！");
		return msg;
	}
	
	@Override
	public Message DelRESULT_FIXED(Map<String, String> param) {
		Message msg = new Message();
		List<RESULT_FIXED> Result = JSONObject.parseArray(param.get("jsonEnity"),RESULT_FIXED.class);
		for (RESULT_FIXED result_FIXED : Result) {
			Dao.deleteEntity(result_FIXED);
		}
		Dao.flush();
		msg.setMsg("删除成功！");
		return msg;
	}

	@Override
	public Message RESULT_FIXEDModify(Map<String, String> param) {
		Message msg = new Message();
		List<RESULT_FIXED> Result = JSONObject.parseArray(param.get("jsonEnity"), RESULT_FIXED.class);
		for (RESULT_FIXED result_FIXED : Result) {
			result_FIXED.setLASTMODIFIER(Global.getCurrentUserName());
			result_FIXED.setMODIFYTIME(new Date());
			Dao.update(result_FIXED);
		}
		Dao.flush();
		msg.setMsg("更新成功！");
		return msg;
	}

	@Override
	public List<Object> GetCombobox() {
		List<Map> reslist = Dao.getDataListByFullSql("SELECT U.ID USERID, U.USERNAME, D.DEPARTMENTNAME,D.ID DEPARTMENTID FROM SMWB_FRAMEWORK.T_DEPARTMENT D "
				+ " LEFT JOIN SMWB_FRAMEWORK.T_USER U ON U.DEPARTMENTID = D.ID WHERE U.ID IS NOT NULL");
		Map<String,String> hs = new HashMap<String, String>();
		for (Map map : reslist) {
			hs.put(StringHelper.formatObject(map.get("DEPARTMENTID")),StringHelper.formatObject(map.get("DEPARTMENTNAME")));
		}
		List<Object> tree = new ArrayList<Object>();
		for (Map.Entry<String, String> entry : hs.entrySet()) {
			Map<String,Object> lv = new HashMap<String, Object>();
			lv.put("id", entry.getValue());
			lv.put("text", entry.getValue());
			ArrayList<Map> childrens = new ArrayList<Map>();
			for (Map map : reslist) {
				if(entry.getKey().equals(map.get("DEPARTMENTID"))){
					Map<String,Object> lv2 = new HashMap<String, Object>();
					lv2.put("id", map.get("USERNAME"));
					lv2.put("text", map.get("USERNAME"));
					childrens.add(lv2);
				}
			}
			lv.put("children", childrens);
			tree.add(lv);
		}
		return tree;
	}

}
