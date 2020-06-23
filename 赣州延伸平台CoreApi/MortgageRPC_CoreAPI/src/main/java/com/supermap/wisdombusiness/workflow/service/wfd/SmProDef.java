package com.supermap.wisdombusiness.workflow.service.wfd;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProMater;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfd_Tr_ActToMod;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;

@Component("smProDef")
public class SmProDef {

	public static List<TreeInfo> CACHE_ALLFLOW = null;

	@Autowired
	CommonDao _CommonDao;
	@Autowired
	private SmActDef smActDef;

	/**
	 * 通过员工ID获取员工可受理的业务Json
	 * 
	 * @param Staff_ID
	 * @return String
	 */
	public String GetProdefJson(String Staff_ID) {
		StringBuilder rtStr = new StringBuilder();
		rtStr.append("{");
		List<Wfd_ProClass> _Wfd_ProClassList = _CommonDao.findList(Wfd_ProClass.class, "PRODEFCLASS_PID IS NULL");
		int Count = 0;
		for (int i = 0; i < _Wfd_ProClassList.size(); i++) {
			String _str = GetProClassJsonBeginClass(_Wfd_ProClassList.get(i).getProdefclass_Id(), _Wfd_ProClassList.get(i).getProdefclass_Name(), Staff_ID);
			if (!_str.toString().equals("")) {
				if (Count != 0)
					rtStr.append(",");
				rtStr.append(_str);
				Count++;
			}

		}
		rtStr.append("}");

		if (!rtStr.toString().equals("") && rtStr != null) {
			String tem = rtStr.substring(0, 1);
			if (tem.equals(",")) {
				rtStr.delete(0, 1);
			}
		}
		return rtStr.toString();
	}

	/**
	 * 获取单个分类的具体业务
	 * 
	 * @param ProClassID
	 *            String
	 * @param ProClass_Name
	 *            String
	 * @param Staff_ID
	 *            String
	 * @return
	 */
	public String GetProClassJsonBeginClass(String ProClassID, String ProClass_Name, String Staff_ID) {
		// if (ProClassID.equals("28")) {
		// String aa = "";
		// String bb = aa;
		// }
		StringBuilder returnStr = new StringBuilder();

		// 判断父节点下级联是否关联有业务定义
		if (ProClassHasProdef(ProClassID, Staff_ID)) {
			// returnStr.append(",");
			returnStr.append("\"");
			returnStr.append(ProClassID);
			returnStr.append("\"");
			returnStr.append(": {\"name\": \"");
			returnStr.append(ProClass_Name);
			returnStr.append("\", \"cell\": {");

			// 取出下级目录
			List<Wfd_ProClass> _Wfd_ProClassList = _CommonDao.findList(Wfd_ProClass.class, "PRODEFCLASS_PID='" + ProClassID + "' ORDER BY PRODEFCLASS_INDEX");
			// 如果下级目录为空直接取出下级关联业务定义
			if (_Wfd_ProClassList.size() == 0) {
				/*
				 * List<Wfd_Prodef> _Wfd_ProdefList =
				 * _CommonDao.getDataList(Wfd_Prodef
				 * .class,Common.WORKFLOWDB+"WFD_PRODEF" ,"PRODEFCLASS_ID='" +
				 * ProClassID+
				 * "' and PRODEF_ID IN (SELECT PRODEF_ID FROM "+Common
				 * .WORKFLOWDB+"V_ACT_PRO_ROLE_STAFF WHERE STAFF_ID='"+Staff_ID+
				 * "') ORDER BY PRODEF_INDEX");
				 */
				List<Wfd_Prodef> _Wfd_ProdefList = _CommonDao.getDataList(Wfd_Prodef.class, Common.WORKFLOWDB + "WFD_PRODEF", "PRODEFCLASS_ID='" + ProClassID
						+ "' ORDER BY PRODEF_INDEX");
				for (int i = 0; i < _Wfd_ProdefList.size(); i++) {
					if (i > 0)
						returnStr.append(",");
					Wfd_Prodef _Wfd_Prodef = _Wfd_ProdefList.get(i);
					returnStr.append("\"");
					returnStr.append(_Wfd_Prodef.getProdef_Id());
					returnStr.append("\"");
					returnStr.append(": {\"name\": \"");
					returnStr.append(_Wfd_Prodef.getProdef_Name());
					returnStr.append("\",\"ID\": \"");
					returnStr.append(_Wfd_Prodef.getProdef_Id());
					returnStr.append("\"}");
				}
			} else // 否则继续取下级目录
			{

				List<Wfd_Prodef> _Wfd_ProdefList = _CommonDao.getDataList(Wfd_Prodef.class, Common.WORKFLOWDB + "WFD_PRODEF", "PRODEFCLASS_ID='" + ProClassID
						+ "' ORDER BY PRODEF_INDEX");
				StringBuilder returnStr2 = new StringBuilder();
				for (int i = 0; i < _Wfd_ProdefList.size(); i++) {
					if (i > 0)
						returnStr2.append(",");
					Wfd_Prodef _Wfd_Prodef = _Wfd_ProdefList.get(i);
					returnStr2.append("\"");
					returnStr2.append(_Wfd_Prodef.getProdef_Id());
					returnStr2.append("\"");
					returnStr2.append(": {\"name\": \"");
					returnStr2.append(_Wfd_Prodef.getProdef_Name());
					returnStr2.append("\",\"ID\": \"");
					returnStr2.append(_Wfd_Prodef.getProdef_Id());
					returnStr2.append("\"}");
				}

				int Count = 0;
				for (int i = 0; i < _Wfd_ProClassList.size(); i++) {
					String _Str = GetProClassJsonBeginClass(_Wfd_ProClassList.get(i).getProdefclass_Id(), _Wfd_ProClassList.get(i).getProdefclass_Name(), Staff_ID);

					if (!_Str.equals("")) {
						if (Count == 0) {
							if (!returnStr2.toString().equals("")) {
								returnStr.append(returnStr2);
								returnStr.append(",");
							}
						} else {
							returnStr.append(",");

						}
						Count++;
					}
					returnStr.append(_Str);
				}
				if (Count == 0) {
					returnStr.append(returnStr2);
				}
				/*
				 * String _str=returnStr.toString();
				 * if(!_str.toString().equals("")&&_str!=null) { String
				 * tem=_str.substring(0, 1); if(tem.equals(",")) {
				 * returnStr.delete(0, 1); } }
				 */
			}

			String _str = returnStr.toString();
			if (!_str.toString().equals("")) {
				String tem = _str.substring(0, 1);
				if (tem.equals(",")) {
					returnStr.delete(0, 1);
				}
			}

			returnStr.append("}}");
		}
		String _str = returnStr.toString();
		if (!_str.toString().equals("")) {
			String tem = _str.substring(0, 1);
			if (tem.equals(",")) {
				returnStr.delete(0, 1);
			}
		}
		return returnStr.toString();
	}

	/**
	 * 判断流程分类下是否挂有具体的业务流程
	 * 
	 * @param ProClassID
	 *            String
	 * @param Staff_ID
	 *            String
	 * @return Boolean
	 */
	public Boolean ProClassHasProdef(String ProClassID, String Staff_ID) {
		return true;
		/*
		 * List<Wfd_ProClass> _Wfd_ProClassList = _CommonDao.findList(
		 * Wfd_ProClass.class, "PRODEFCLASS_PID='" + ProClassID + "'"); if
		 * (_Wfd_ProClassList.size() == 0) { //List<Wfd_Prodef> _Wfd_ProdefList
		 * =
		 * _CommonDao.getDataList(Wfd_Prodef.class,Common.WORKFLOWDB+"WFD_PRODEF"
		 * // , "PRODEFCLASS_ID='" + ProClassID +
		 * "' and PRODEF_ID IN (SELECT PRODEF_ID FROM "
		 * +Common.WORKFLOWDB+"V_ACT_PRO_ROLE_STAFF WHERE STAFF_ID='"
		 * +Staff_ID+"')"); // List<Wfd_Prodef> _Wfd_ProdefList =
		 * _CommonDao.getDataList
		 * (Wfd_Prodef.class,Common.WORKFLOWDB+"WFD_PRODEF" , "PRODEFCLASS_ID='"
		 * + ProClassID + "'"); if (_Wfd_ProdefList.size() == 0) { return false;
		 * } else { return true; } } else { for (int i = 0; i <
		 * _Wfd_ProClassList.size(); i++) { if
		 * (ProClassHasProdef(_Wfd_ProClassList.get(i)
		 * .getProdefclass_Id(),Staff_ID)) return true; } return false; }
		 */
	}

	/**
	 * 通过ProdefID获取该流程定义的所有活动
	 * 
	 * @param ProdefID
	 * @return
	 */
	public List<Wfd_Actdef> ActdefList(String ProdefID) {
		StringBuilder noWhereSql = new StringBuilder();
		noWhereSql.append("PRODEF_ID='");
		noWhereSql.append(ProdefID);
		noWhereSql.append("'");
		return _CommonDao.getDataList(Wfd_Actdef.class, Common.WORKFLOWDB + "WFD_ACTDEF", noWhereSql.toString());
	}

	/**
	 * 通过ProdefID获取该流程定义的所有路由
	 * 
	 * @param ProdefID
	 * @return
	 */
	public List<Wfd_Route> RouteList(String ProdefID) {
		StringBuilder noWhereSql = new StringBuilder();
		noWhereSql.append("PRODEF_ID='");
		noWhereSql.append(ProdefID);
		noWhereSql.append("'");
		return _CommonDao.getDataList(Wfd_Route.class, Common.WORKFLOWDB + "WFD_ROUTE", noWhereSql.toString());
	}

	/**
	 * 获取流程目录树【用于左边树】
	 * 
	 * @return
	 */
	public List<TreeInfo> GetProdefTree() {
		if (Global.USECACHE_CONFIG_WORKFLOW && CACHE_ALLFLOW != null) {
		} else {
			CACHE_ALLFLOW = GetProClassChildrenTree(null);
		}
		return CACHE_ALLFLOW;
	}
	public List<TreeInfo> GetProdefAsyncTree(String ID,String ISRoute,String showAll) {
		return GetProClassChildrenAsyncTree(ID,ISRoute,showAll);
		
	}
	
	/**
	 * 重新加载流程缓存
	 * @Title: reloadConfigFlowCache 
	 * @author:liushufeng
	 * @date：2015年7月31日 下午9:37:19
	 */
	public void reloadConfigFlowCache()
	{
		CACHE_ALLFLOW = GetProClassChildrenTree(null);
	}
	
	/**
	 * 获取流程定义目录
	 * 
	 * @param ProClass
	 * @return
	 */
	public List<TreeInfo> GetProClassChildrenTree(Wfd_ProClass ProClass) {
		StringBuilder noWhereSql = new StringBuilder();
		if (ProClass == null) {
			noWhereSql.append("Prodefclass_Pid is null or Prodefclass_Pid='0' ");
		} else {
			noWhereSql.append("Prodefclass_Pid='");
			noWhereSql.append(ProClass.getProdefclass_Id());
			noWhereSql.append("'");
		}
		noWhereSql.append(" order by Prodefclass_Index");
		List<Wfd_ProClass> list = _CommonDao.findList(Wfd_ProClass.class, noWhereSql.toString());
		List<TreeInfo> TreeList = new ArrayList<TreeInfo>();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Wfd_ProClass _ProClass = list.get(i);
				TreeInfo tree = new TreeInfo();
				tree.setId(_ProClass.getProdefclass_Id());
				tree.setText(_ProClass.getProdefclass_Name());
				tree.setType("catalog");
				tree.setState("closed");
				tree.children = GetProClassChildrenTree(list.get(i));
				TreeList.add(tree);
			}

		}

		if (ProClass != null) {
			StringBuilder noWhereSql2 = new StringBuilder();
			noWhereSql2.append("Prodefclass_Id='");
			noWhereSql2.append(ProClass.getProdefclass_Id());
			noWhereSql2.append("' order by Prodef_Index");
			List<Wfd_Prodef> list2 = _CommonDao.findList(Wfd_Prodef.class, noWhereSql2.toString());
			for (int j = 0; j < list2.size(); j++) {
				Wfd_Prodef _Prodef = list2.get(j);
				TreeInfo tree = new TreeInfo();
				tree.setId(_Prodef.getProdef_Id());
				tree.setText(_Prodef.getProdef_Name());
				tree.setType("data");
				TreeList.add(tree);
			}
		}

		return TreeList;

	}
	 /* 获取流程定义目录
	 * 
	 * @param ProClass
	 * @return
	 */
	public List<TreeInfo> GetProClassChildrenAsyncTree(String ID,String ISRoute,String showAll) {
		StringBuilder noWhereSql = new StringBuilder();
		List<TreeInfo> TreeList = new ArrayList<TreeInfo>();
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
/*		if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
				if(ID != null&&ID.contains("qjcx")){
					ID=ID.substring(0, ID.indexOf("qjcx"));
					if (ID == null||ID.length()==0) {
						noWhereSql.append("Prodefclass_Pid is null or Prodefclass_Pid='0' ");
					} else {
						noWhereSql.append("Prodefclass_Pid='");
						noWhereSql.append(ID);
						noWhereSql.append("'");
					}
					noWhereSql.append(" order by Prodefclass_Index");
					List<Wfd_ProClass> list = _CommonDao.findList(Wfd_ProClass.class, noWhereSql.toString());
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							Wfd_ProClass _ProClass = list.get(i);
							TreeInfo tree = new TreeInfo();
							tree.setId(_ProClass.getProdefclass_Id());
							tree.setText(_ProClass.getProdefclass_Name());
							tree.setType("catalog");
							tree.setState("closed");
							TreeList.add(tree);
						}
		
					}
					StringBuilder noWhereSql2 = new StringBuilder();
					noWhereSql2.append("Prodefclass_Id='");
					noWhereSql2.append(ID);
					noWhereSql2.append("' order by Prodef_Index");
					List<Wfd_Prodef> list2 = _CommonDao.findList(Wfd_Prodef.class, noWhereSql2.toString());
					if(list2!=null&&list2.size()>0){
						for (int j = 0; j < list2.size(); j++) {
							Wfd_Prodef _Prodef = list2.get(j);
							TreeInfo tree = new TreeInfo();
							//当为在全局查询的过程中，孝感地区无效流程不显示
							Integer proSta=_Prodef.getProdef_Status();
							String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
							if("420900".equals(xzqhdm)){
								if(1==proSta){//&&"qjcx".equals(ISRoute)
									tree.setId(_Prodef.getProdef_Id());
									tree.setText(_Prodef.getProdef_Name());
									tree.setDesc(_Prodef.getProdef_Code());
									tree.setType("data");
								}
							}else{
								tree.setId(_Prodef.getProdef_Id());
								tree.setText(_Prodef.getProdef_Name());
								tree.setDesc(_Prodef.getProdef_Code());
								tree.setType("data");
							}
							if(ISRoute==null){
								tree.setisParent(false);
							}
							if(tree.getText()!=null){
								TreeList.add(tree);
							}
						}
					}
					else if (ISRoute!=null&&ISRoute.equals("true")) {
						StringBuilder sbBuilder=new StringBuilder();
						sbBuilder.append(" prodef_id='");
						sbBuilder.append(ID);
						sbBuilder.append("'");
						List<Wfd_Route> list3=_CommonDao.findList(Wfd_Route.class, sbBuilder.toString());
						if(list3!=null&&list3.size()>0){
							for (Wfd_Route wfd_Route : list3) {
								TreeInfo tree = new TreeInfo();
								tree.setId(wfd_Route.getRoute_Id());
								tree.setText(wfd_Route.getRoute_Name());
								tree.setType("route");
								tree.setisParent(false);
								TreeList.add(tree);
							}
						}
					} 
				}
		
		}else{*/
		if (ID == null) {
			noWhereSql.append("Prodefclass_Pid is null or Prodefclass_Pid='0' ");
		} else {
			noWhereSql.append("Prodefclass_Pid='");
			noWhereSql.append(ID);
			noWhereSql.append("'");
		}
		noWhereSql.append(" order by Prodefclass_Index");
		List<Wfd_ProClass> list = _CommonDao.findList(Wfd_ProClass.class, noWhereSql.toString());
		
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Wfd_ProClass _ProClass = list.get(i);
				TreeInfo tree = new TreeInfo();
				tree.setId(_ProClass.getProdefclass_Id());
				tree.setText(_ProClass.getProdefclass_Name());
				tree.setType("catalog");
				tree.setState("closed");
				TreeList.add(tree);
			}

		}
		StringBuilder noWhereSql2 = new StringBuilder();
		//在办箱和全局查询隐藏流程
		if(StringHelper.isEmpty(showAll)){
			noWhereSql2.append(" (show_status is null or  show_status <1) and ");
		}else{
			String[] showAllArray = showAll.split(",");
			if(showAllArray.length>1&&!StringHelper.isEmpty(showAllArray[1])){
				if("1".equals(showAllArray[1])){
					noWhereSql2.append(" prodef_status='1' and ");
				}
			}
		}
		noWhereSql2.append("Prodefclass_Id='");
		noWhereSql2.append(ID);
		noWhereSql2.append("' order by Prodef_Index");
		List<Wfd_Prodef> list2 = _CommonDao.findList(Wfd_Prodef.class, noWhereSql2.toString());
		if(list2!=null&&list2.size()>0){
			for (int j = 0; j < list2.size(); j++) {
				Wfd_Prodef _Prodef = list2.get(j);
				TreeInfo tree = new TreeInfo();
				tree.setId(_Prodef.getProdef_Id());
				tree.setText(_Prodef.getProdef_Name());
				tree.setDesc(_Prodef.getProdef_Code());
				tree.setType("data");
				if(ISRoute==null){
					tree.setisParent(false);
				}
				TreeList.add(tree);
			}
		}
		else if (ISRoute!=null&&ISRoute.equals("true")) {
			StringBuilder sbBuilder=new StringBuilder();
			sbBuilder.append(" prodef_id='");
			sbBuilder.append(ID);
			sbBuilder.append("'");
			List<Wfd_Route> list3=_CommonDao.findList(Wfd_Route.class, sbBuilder.toString());
			if(list3!=null&&list3.size()>0){
				for (Wfd_Route wfd_Route : list3) {
					TreeInfo tree = new TreeInfo();
					tree.setId(wfd_Route.getRoute_Id());
					tree.setText(wfd_Route.getRoute_Name());
					tree.setType("route");
					tree.setisParent(false);
					TreeList.add(tree);
				}
			}
		} else if (ISRoute != null && ISRoute.equals("Version")) {
			Wfd_Prodef p = _CommonDao.get(Wfd_Prodef.class, ID);
			String proCopy = "SELECT prodef_id,prodef_status, case when  prodef_status=0 then  '版本号'||version||':'||prodef_Name ||'(无效)' else  '版本号'||version||':'||prodef_Name||'(有效)' end prodef_name\n"
					+ " FROM bdc_workflow.wfd_prodef where prodef_code='" + p.getProdef_Code() + "'\n"
					+ " order by cast(version as int) asc";
			List<Map> listCopProcess = _CommonDao.getDataListByFullSql(proCopy);
			if (listCopProcess != null && listCopProcess.size() > 0) {
				for (int a = 0; a < listCopProcess.size(); a++) {
					TreeInfo tree = new TreeInfo();
					tree.setId(listCopProcess.get(a).get("PRODEF_ID").toString());
					tree.setText(listCopProcess.get(a).get("PRODEF_NAME").toString());
					tree.setType("version");
					tree.setisParent(false);
					tree.setState(listCopProcess.get(a).get("PRODEF_STATUS").toString());
					TreeList.add(tree);
				}
			}

		}
		
		

		return TreeList;

	}

	/**
	 * 通过主键ID获取Prodef
	 * 
	 * @param id
	 * @return
	 */
	public Wfd_Prodef GetProdefById(String id) {
		return _CommonDao.get(Wfd_Prodef.class, id);
	}

	public void UpdateWfd_Prodef(Wfd_Prodef Prodef) {
		_CommonDao.update(Prodef);
		_CommonDao.flush();
	}

	/**
	 * 保存或更新对象
	 * 
	 * @param Prodef
	 */
	public SmObjInfo SaveOrUpdate_Prodef(Wfd_Prodef Prodef) {
		SmObjInfo sminfoInfo = new SmObjInfo();
		if (Prodef.getProdef_Id().equals("")) {
			Prodef.setProdef_Id(Common.CreatUUID());
		}
		_CommonDao.saveOrUpdate(Prodef);
		_CommonDao.flush();
		sminfoInfo.setID(Prodef.getProdef_Id());
		sminfoInfo.setDesc("保存成功");
		sminfoInfo.setName(Prodef.getProdef_Name());
		return sminfoInfo;
	}

	/**
	 * 
	 * @param id
	 */
	public void Delete_Prodef(String id) {
		_CommonDao.delete(Wfd_Prodef.class, id);
	}

	public String CreateNewProdefByName(String name, String pid, int index) {
		Wfd_Prodef _Prodef = new Wfd_Prodef();
		_Prodef.setProdef_Name(name);
		_Prodef.setProdef_Index(index);
		_Prodef.setProdefclass_Id(pid);
		_CommonDao.save(_Prodef);
		_CommonDao.flush();
		return _Prodef.getProdef_Id();
	}

	/**
	 * 删除流程
	 * 
	 * */
	public SmObjInfo delProdef(String Prodef_id) {
		SmObjInfo smObjInfo = new SmObjInfo();
		if (!Prodef_id.equals("")) {
			Delete_Prodef(Prodef_id);
			smActDef.delActdef(Prodef_id);
			_CommonDao.flush();
			smObjInfo.setID(Prodef_id);
			smObjInfo.setDesc("删除成功");
		}
		return smObjInfo;
	}

	public SmObjInfo DelectProdef(String prodefid) {

		StringBuilder str = new StringBuilder();
		str.append(" Prodef_Id='");
		str.append(prodefid);
		str.append("'");

		SmObjInfo smObjInfo = new SmObjInfo();
		List<Wfi_ProInst> proList = _CommonDao.findList(Wfi_ProInst.class, str.toString());
		if (proList.size() > 0) {
			smObjInfo.setID(prodefid);
			smObjInfo.setDesc("该流程业务办理中，不可删除！");
			return smObjInfo;
		}
		// 删除活动
		List<Wfd_Actdef> list1 = _CommonDao.findList(Wfd_Actdef.class, str.toString());
		for (int i = 0; i < list1.size(); i++) {
			// 删除表单
			Wfd_Actdef Actdef = list1.get(i);
			List<Wfd_Tr_ActToMod> tr_ActToModsList = _CommonDao.findList(Wfd_Tr_ActToMod.class, " Actdef_Id='" + Actdef.getActdef_Id() + "'");
			for (int j = 0; j < tr_ActToModsList.size(); j++) {
				_CommonDao.delete(tr_ActToModsList.get(j));
			}
			_CommonDao.delete(Actdef);

		}
		// 删除路由
		List<Wfd_Route> list2 = _CommonDao.findList(Wfd_Route.class, str.toString());
		for (int i = 0; i < list2.size(); i++) {
			_CommonDao.delete(list2.get(i));
		}

		// 删除资料信息
		List<Wfd_ProMater> list3 = _CommonDao.findList(Wfd_ProMater.class, str.toString());
		for (int i = 0; i < list3.size(); i++) {
			_CommonDao.delete(list3.get(i));
		}

		_CommonDao.delete(Wfd_Prodef.class, prodefid);
		_CommonDao.flush();
		smObjInfo.setID(prodefid);
		smObjInfo.setDesc("删除成功");
		return smObjInfo;
	}

	// 通过活动定义ID获取流程
	public Wfd_Prodef GetProDefByActDefID(String actdef_id) {
		Wfd_Prodef prodef = null;
		Wfd_Actdef actdef = smActDef.GetActDefByID(actdef_id);
		if (actdef != null) {
			prodef = GetProdefById(actdef.getActdef_Id());
		}
		return prodef;
	}

	/* 刘树峰 2015年7月30日，修改获取可受理流程的时候，加上当前用户的过滤 */

	public Map<String, Object> getAcceptProdef2(String Staff_ID) {
		Map<String, Object> defs = new HashMap<String, Object>();
		List<Wfd_ProClass> _Wfd_ProClassList = _CommonDao.findList(Wfd_ProClass.class, "PRODEFCLASS_PID IS NULL order by prodefclass_index");
		for (int i = 0; i < _Wfd_ProClassList.size(); i++) {
			LinkedHashMap<String, Object> def = new LinkedHashMap<String, Object>();
			def.put("name", _Wfd_ProClassList.get(i).getProdefclass_Name());
			addChild2(def, _Wfd_ProClassList.get(i).getProdefclass_Id(), Staff_ID);
			if (def.containsKey("cell") && def.get("cell") != null) {
				defs.put(_Wfd_ProClassList.get(i).getProdefclass_Id(), def);
			}
		}
		return defs;
	}

	private void addChild2(LinkedHashMap<String, Object> def, String defid, String staffid) {
		List<Wfd_ProClass> list = _CommonDao.findList(Wfd_ProClass.class, "PRODEFCLASS_PID='" + defid + "' order by prodefclass_index");
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("name", list.get(i).getProdefclass_Name());
				addChild2(map, list.get(i).getProdefclass_Id(), staffid);
				if (map.containsKey("cell")) {
					LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
					map2.put(list.get(i).getProdefclass_Id(), map);
					if (def.containsKey("cell")) {
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, Object> c = (LinkedHashMap<String, Object>) def.get("cell");
						c.put(list.get(i).getProdefclass_Id(), map);
					} else {
						def.put("cell", map2);
					}
				}
			}
			
			
			String strsql = " SELECT DISTINCT A.PRODEF_ID as ID ,A.PRODEF_NAME ,A.PRODEF_INDEX AS NAME FROM BDC_WORKFLOW.WFD_PRODEF A LEFT JOIN BDC_WORKFLOW.WFD_ACTDEF B ON A.PRODEF_ID=B.PRODEF_ID AND B.ACTDEF_TYPE='1010' LEFT JOIN SMWB_FRAMEWORK.RT_USERROLE C ON C.ROLEID=B.ROLE_ID WHERE C.USERID=''{0}'' AND B.ROLE_ID IS NOT NULL AND B.ACTDEF_ID IS NOT NULL  AND A.Prodefclass_Id=''{1}'' and A.prodef_status=1 order by A.PRODEF_INDEX";
			strsql = MessageFormat.format(strsql, staffid, defid);
			@SuppressWarnings("rawtypes")
			List<Map> mps = _CommonDao.getDataListByFullSql(strsql);
			if (mps != null && mps.size() > 0) {
				for (@SuppressWarnings("rawtypes")
				Map map : mps) {
					String id = (String) map.get("ID");
					String name = (String) map.get("NAME");
					LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();
					obj.put("id", id);
					obj.put("name", name);
					LinkedHashMap<String, Object> objroot = new LinkedHashMap<String, Object>();
					objroot.put(id, obj);
					if (!def.containsKey("cell")) {
						def.put("cell", objroot);

					} else {
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, Object> aaa = (LinkedHashMap<String, Object>) def.get("cell");
						aaa.put(id, obj);
					}
				}
			}
		} else {
			String strsql = " SELECT DISTINCT A.PRODEF_ID as ID ,A.PRODEF_NAME AS NAME ,A.PRODEF_INDEX FROM BDC_WORKFLOW.WFD_PRODEF A LEFT JOIN BDC_WORKFLOW.WFD_ACTDEF B ON A.PRODEF_ID=B.PRODEF_ID AND B.ACTDEF_TYPE='1010' LEFT JOIN SMWB_FRAMEWORK.RT_USERROLE C ON C.ROLEID=B.ROLE_ID WHERE C.USERID=''{0}'' AND B.ROLE_ID IS NOT NULL AND B.ACTDEF_ID IS NOT NULL  AND A.Prodefclass_Id=''{1}''  and A.prodef_status=1 ORDER BY A.PRODEF_INDEX";
			strsql = MessageFormat.format(strsql, staffid, defid);
			@SuppressWarnings("rawtypes")
			List<Map> mps = _CommonDao.getDataListByFullSql(strsql);
			if (mps != null && mps.size() > 0) {
				for (@SuppressWarnings("rawtypes")
				Map map : mps) {
					String id = (String) map.get("ID");
					String name = (String) map.get("NAME");
					Map<String, Object> obj = new HashMap<String, Object>();
					obj.put("id", id);
					obj.put("name", name);
					LinkedHashMap<String, Object> objroot = new LinkedHashMap<String, Object>();
					objroot.put(id, obj);
					if (!def.containsKey("cell")) {
						def.put("cell", objroot);

					} else {
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, Object> aaa = (LinkedHashMap<String, Object>) def.get("cell");
						aaa.put(id, obj);
					}
				}
			}
		}
	}

	public List<Wfd_Prodef> getProdefByCord(String proCord){
		return _CommonDao.findList(Wfd_Prodef.class, " PRODEF_CODE = '"+proCord+"'");
	}
	
	//根据prodefid获取prodefname
	public String getproDefName(String prodefid){
		if(!StringHelper.isEmpty(prodefid)){
			Wfd_Prodef prodef = GetProdefById(prodefid);
			String defclassid = prodef.getProdefclass_Id();
			String result = prodef.getProdef_Name();
			return getProdefName(result,defclassid);
		}
		return"";
	}
	
	private String getProdefName (String result, String proClassId ){
		Wfd_ProClass proclass = _CommonDao.get(Wfd_ProClass.class, proClassId);
		String padefid = proclass.getProdefclass_Pid();
		String defname = proclass.getProdefclass_Name()+","+result;
		if(null==padefid){
			return defname;
		}else{
			return getProdefName(defname,padefid);
		}
	}
	
}
