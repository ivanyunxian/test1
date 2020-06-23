package com.supermap.wisdombusiness.workflow.service.wfi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;

import javax.servlet.http.HttpServletRequest;

/**
 * 流程组件 标签
 * 
 * @author wangjz
 *
 */
@Service("smSysMod")
public class SmSysMod {
	@Autowired
	private CommonDao _CommonDao;
	@Autowired
	private SmActInst smActInst;
	public List<Map> getProjectDetail(String actinst_id, HttpServletRequest request) {
		/*List<Wfd_SysMod> sysmod = _CommonDao
				.getDataList(
						Wfd_SysMod.class,
						"select a.*,b.readonly  from "
								+ Common.WORKFLOWDB
								+ "WFD_SYSMOD a,"
								+ Common.WORKFLOWDB
								+ "WFD_TR_ACTTOMOD b,"
								+ Common.WORKFLOWDB
								+ "WFI_ACTINST c where a.SYSMOD_ID = b.SYSMODID  and b.ACTDEF_ID = c.ACTDEF_ID and c.ACTINST_ID ='"
								+ actinst_id + "' order by b.MODE_INDEX");
		return sysmod;*/
		List<Map> result = _CommonDao.getDataListByFullSql("select a.*,b.readonly  from "
				+ Common.WORKFLOWDB
				+ "WFD_SYSMOD a,"
				+ Common.WORKFLOWDB
				+ "WFD_TR_ACTTOMOD b,"
				+ Common.WORKFLOWDB
				+ "WFI_ACTINST c where a.SYSMOD_ID = b.SYSMODID  and b.ACTDEF_ID = c.ACTDEF_ID and c.ACTINST_ID ='"
				+ actinst_id + "' order by b.MODE_INDEX");
		//智能审批件，登簿后，登簿环节的组件全部设为只读
		List<Map> actinst_name = _CommonDao.getDataListByFullSql("SELECT ACTINST_NAME FROM " + Common.WORKFLOWDB + "WFI_ACTINST WHERE ACTINST_ID='" + actinst_id + "'");
		if(!actinst_name.isEmpty() && !StringHelper.isEmpty(actinst_name.get(0).get("ACTINST_NAME")) && StringHelper.formatObject(actinst_name.get(0).get("ACTINST_NAME")).contains("登簿")){
			Wfi_ActInst ActInst = _CommonDao.load(Wfi_ActInst.class, actinst_id);
			Wfi_ProInst ProInst = _CommonDao.load(Wfi_ProInst.class, ActInst.getProinst_Id());
			List<Map> SFDB = _CommonDao.getDataListByFullSql("SELECT SFDB FROM BDCK.BDCS_XMXX WHERE PROJECT_ID='" + ProInst.getFile_Number() + "'");
			if(!SFDB.isEmpty() && "1".equals(SFDB.get(0).get("SFDB"))) {
				//组件设为只读，但不包括证书和登簿两个组件
				List<Map> readonly_result = new ArrayList<Map>();
				for(Map map : result) {
					String sysmod_path = StringHelper.formatObject(map.get("SYSMOD_PATH"));
					if(sysmod_path.contains("qlxx") || sysmod_path.contains("dyxx") || sysmod_path.contains("sqr")) {
						map.put("READONLY","1");
					}
					readonly_result.add(map);
				}
				return readonly_result;
			}
		}
		return result;
	}

	public List<TreeInfo> GetSysModTree() {
		return GetModClassChildrenTree(null);
	}

	public List<TreeInfo> GetModClassChildrenTree(Wfd_ModClass ModClass) {
		StringBuilder noWhereSql = new StringBuilder();
		if (ModClass == null) {
			noWhereSql
					.append(" Parentclass_Id is NULL or Parentclass_Id='0' order by Modclass_Index ");
		} else {
			noWhereSql.append(" Parentclass_Id='");
			noWhereSql.append(ModClass.getModclass_Id());
			noWhereSql.append("'");
		}

		List<Wfd_ModClass> list = _CommonDao.findList(Wfd_ModClass.class,
				noWhereSql.toString());
		List<TreeInfo> TreeList = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			Wfd_ModClass _ModClass = list.get(i);
			TreeInfo tree = new TreeInfo();
			tree.setId(_ModClass.getModclass_Id());
			tree.setText(_ModClass.getModclass_Name());
			tree.setType("catalog");
			tree.children = GetModClassChildrenTree(_ModClass);
			TreeList.add(tree);

		}
		if (ModClass != null) {
			StringBuilder noWhereSql2 = new StringBuilder();
			noWhereSql2.append(" Modclass_Id='");
			noWhereSql2.append(ModClass.getModclass_Id());
			noWhereSql2.append("'");
			List<Wfd_SysMod> list2 = _CommonDao.findList(Wfd_SysMod.class,
					noWhereSql2.toString());
			for (int i = 0; i < list2.size(); i++) {
				Wfd_SysMod _SysMod = list2.get(i);
				TreeInfo tree = new TreeInfo();
				tree.setId(_SysMod.getSysmod_Id());
				tree.setText(_SysMod.getSysmod_Name());
				tree.setType("data");
				TreeList.add(tree);
			}
		}
		return TreeList;

	}

	public List<TreeInfo> ActdefMod(String actdefid) {
		StringBuilder str = new StringBuilder();
		str.append("Actdef_Id='");
		str.append(actdefid);
		str.append("' order by ModeIndex");
		List<Wfd_Tr_ActToMod> list = _CommonDao.findList(Wfd_Tr_ActToMod.class,
				str.toString());
		List<TreeInfo> TreeList = new ArrayList<TreeInfo>();
		for (int i = 0; i < list.size(); i++) {
			Wfd_Tr_ActToMod _trActToMod = list.get(i);
			TreeInfo tree = new TreeInfo();
			tree.setId(_trActToMod.getSysmodid());
			Wfd_SysMod _sysMod = _CommonDao.get(Wfd_SysMod.class,
					_trActToMod.getSysmodid());
			if (_sysMod != null) {
				tree.setText(_sysMod.getSysmod_Name());
				TreeList.add(tree);
			}
		}

		return TreeList;
	}

	public Message GetActdefMod(String actdefid) {
		StringBuilder str = new StringBuilder();
		str.append("Actdef_Id='");
		str.append(actdefid);
		str.append("' order by Mode_Index");
		List<Wfd_Tr_ActToMod> list = _CommonDao.findList(Wfd_Tr_ActToMod.class,
				str.toString());

		List<Wfd_SysMod> TreeList = new ArrayList<Wfd_SysMod>();
		for (int i = 0; i < list.size(); i++) {
			Wfd_Tr_ActToMod _trActToMod = list.get(i);
			if (_trActToMod != null) {
				Wfd_SysMod _sysMod = _CommonDao.get(Wfd_SysMod.class,
						_trActToMod.getSysmodid());
				//借用Wfd_SysMod 中的mod_id  暂时保存关系表总的主键
				if (_sysMod != null) {
					_sysMod.setMod_Id(_trActToMod.getActtomodid());
					//借用Wfd_SysMod中的sysmod_desc 保存组件是否只读
					_sysMod.setSysmod_Desc(""+_trActToMod.getReadonly());
					TreeList.add(_sysMod);
				}
			}
		}
		Message msg = new Message();
		msg.setRows(TreeList);
		msg.setTotal(TreeList.size());
		return msg;
	}

	public void UpdateActToMod(JSONArray array, String actdefid) {
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
//			StringBuilder _Str = new StringBuilder();
//			_Str.append("Actdef_Id='");
//			_Str.append(actdefid);
//			_Str.append("' and Sysmodid='");
//			_Str.append(object.get("modid"));
//			_Str.append("'");
			Wfd_Tr_ActToMod list = _CommonDao.get(Wfd_Tr_ActToMod.class, object.get("modid").toString());
			if (list!=null) {
				list.setMode_Index(Integer.parseInt(object.get("index")
						.toString()));
				_CommonDao.update(list);
			}
		}
		_CommonDao.flush();
	}

	public String CreateWfd_Tr_ActToSysMod(String actdefid, String sysmodid,
			int index) {
		Wfd_Tr_ActToMod _Wfd_Tr_ActToMod = new Wfd_Tr_ActToMod();
		_Wfd_Tr_ActToMod.setActtomodid(Common.CreatUUID());
		_Wfd_Tr_ActToMod.setActdef_Id(actdefid);
		_Wfd_Tr_ActToMod.setSysmodid(sysmodid);
		_Wfd_Tr_ActToMod.setMode_Index(index);
		_Wfd_Tr_ActToMod.setReadonly(0);
		_CommonDao.saveOrUpdate(_Wfd_Tr_ActToMod);
		_CommonDao.flush();
		return _Wfd_Tr_ActToMod.getActtomodid();
	}

	public String DeleteWfd_Tr_ActToSysMod(String acttomodid) {
//		StringBuilder str = new StringBuilder();
//		str.append(" Actdef_Id='");
//		str.append(actdefid);
//		str.append("' and Sysmodid='");
//		str.append(sysmodid);
//		str.append("'");
//		List<Wfd_Tr_ActToMod> list = _CommonDao.findList(Wfd_Tr_ActToMod.class,
//				str.toString());
//		if (list.size() > 0) {
//			_CommonDao.delete(Wfd_Tr_ActToMod.class, list.get(0)
//					.getActtomodid());
//		}
		_CommonDao.delete(Wfd_Tr_ActToMod.class, acttomodid);
		_CommonDao.flush();
		return "删除成功!";
	}
    public String setModReadonly(String acttomodid,int readonly){
    	Wfd_Tr_ActToMod actmod=_CommonDao.get(Wfd_Tr_ActToMod.class, acttomodid);
    	if(actmod!=null){
    		actmod.setReadonly(readonly);
    		_CommonDao.update(actmod);
    		_CommonDao.flush();
    	}
    	return "设置成功";
    }
	public Wfd_SysMod GetSysModById(String id) {
		return _CommonDao.get(Wfd_SysMod.class, id);
	}

	public SmObjInfo SaveOrUpdate(Wfd_SysMod SysMod) {
		SmObjInfo sminfoInfo = new SmObjInfo();
		if (SysMod.getSysmod_Id().equals("")) {
			SysMod.setSysmod_Id(Common.CreatUUID());
		}
		_CommonDao.saveOrUpdate(SysMod);
		_CommonDao.flush();
		sminfoInfo.setID(SysMod.getSysmod_Id());
		sminfoInfo.setDesc("保存成功");
		sminfoInfo.setName(SysMod.getSysmod_Name());
		return sminfoInfo;
	}

	public SmObjInfo DeleteSysMod(String id) {
		StringBuilder str = new StringBuilder();
		str.append("Sysmodid='");
		str.append(id);
		str.append("'");
		SmObjInfo sminfoInfo = new SmObjInfo();
		List<Wfd_Tr_ActToMod> listActToMod = _CommonDao.findList(
				Wfd_Tr_ActToMod.class, str.toString());
		if (listActToMod.size() > 0) {
			sminfoInfo.setDesc("该组件关联" + listActToMod.size() + "个活动，不能删除！");
			sminfoInfo.setID(id);
		} else {
			Wfd_SysMod SysMod = _CommonDao.get(Wfd_SysMod.class, id);
			_CommonDao.delete(SysMod);
			_CommonDao.flush();
			sminfoInfo.setID("0");
			sminfoInfo.setDesc("删除成功");
		}

		return sminfoInfo;
	}
	
	public String CreateModClassByName(String name,String pid,int index)
	{
		Wfd_ModClass _Wfd_ModClass=new Wfd_ModClass();
		_Wfd_ModClass.setModclass_Name(name);
		_Wfd_ModClass.setModclass_Index(index);
		_Wfd_ModClass.setParentclass_Id(pid);
		_CommonDao.save(_Wfd_ModClass);
		_CommonDao.flush();
		return _Wfd_ModClass.getModclass_Id();
	}
	
	public SmObjInfo DeleteModClass(String id)
	{
		StringBuilder str=new StringBuilder();
		str.append("Modclass_Id='");
		str.append(id);
		str.append("'");
		SmObjInfo info=new SmObjInfo();
		List<Wfd_SysMod> list=_CommonDao.findList(Wfd_SysMod.class, str.toString());
		for(int i=0;i<list.size();i++)
		{
			Wfd_SysMod SysMod=list.get(i);
			_CommonDao.delete(SysMod);
		}
		_CommonDao.delete(Wfd_ModClass.class, id); 
		_CommonDao.flush();
		info.setID("0");
		info.setDesc("删除成功!");
		return info;
	}
	
	public SmObjInfo RenameModClass(String id,String name){
		SmObjInfo smInfo=new SmObjInfo();
		if(id!=null&&!id.equals("")){
			Wfd_ModClass ModClass=_CommonDao.get(Wfd_ModClass.class, id);
			ModClass.setModclass_Name(name);
			_CommonDao.update(ModClass);
			_CommonDao.flush();
			smInfo.setID(ModClass.getParentclass_Id());
			smInfo.setDesc("更新成功");
		}
		return smInfo;
	}

}