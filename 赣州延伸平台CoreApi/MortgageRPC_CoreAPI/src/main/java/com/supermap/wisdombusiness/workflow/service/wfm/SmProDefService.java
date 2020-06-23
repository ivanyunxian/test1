package com.supermap.wisdombusiness.workflow.service.wfm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.supermap.realestate.registration.util.Global;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Pass_Condition;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProClass;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmRouteDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSysMod;

@Service("smProDefService")
public class SmProDefService {

	@Autowired
	private SmProDef _SmProDef;
	@Autowired
	private SmSysMod _SmSysMod;
	@Autowired
	private SmProClass _SmProClass;
	@Autowired
	private SmActDef smActDef;
	@Autowired
	private SmRouteDef smRouteDef;
	@Autowired
	private SmActInst smActInst;
	@Autowired
	private RoleService roleService;
	public static Map<String, String> CACHE_ACCEPTFLOW = null;

	   /**
     * 通过员工获取可受理的流程,南宁添加
     * */
    public String GetProDefInfos_NN(String Staff_ID) {
        if("".equals(Staff_ID)){
            Staff_ID = Global.getCurrentUserInfo().getId();
        }
        
        String str = "";
        if (Global.USECACHE_ACCEPT_WORKFLOW && CACHE_ACCEPTFLOW != null && CACHE_ACCEPTFLOW.containsKey(Staff_ID)) {
            str = CACHE_ACCEPTFLOW.get(Staff_ID);
        } else {
            Map<String, Object> map = _SmProDef.getAcceptProdef2(Staff_ID);
            str = JSONArray.toJSONString(map);
            if (CACHE_ACCEPTFLOW == null)
                CACHE_ACCEPTFLOW = new HashMap<String, String>();
            if (CACHE_ACCEPTFLOW != null && !CACHE_ACCEPTFLOW.containsKey(CACHE_ACCEPTFLOW)) {
                CACHE_ACCEPTFLOW.put(Staff_ID, str);
            }
        }
        return str;
        // return _SmProDef.GetProdefJson(Staff_ID);
    }
    
	/**
	 * 通过员工获取可受理的流程
	 * */
	public String GetProDefInfos(String Staff_ID) {
		Staff_ID = Global.getCurrentUserInfo().getId();
		String str = "";
		if (Global.USECACHE_ACCEPT_WORKFLOW && CACHE_ACCEPTFLOW != null && CACHE_ACCEPTFLOW.containsKey(Staff_ID)) {
			str = CACHE_ACCEPTFLOW.get(Staff_ID);
		} else {
			Map<String, Object> map = _SmProDef.getAcceptProdef2(Staff_ID);
			str = JSONArray.toJSONString(map);
			if (CACHE_ACCEPTFLOW == null)
				CACHE_ACCEPTFLOW = new HashMap<String, String>();
			if (CACHE_ACCEPTFLOW != null && !CACHE_ACCEPTFLOW.containsKey(CACHE_ACCEPTFLOW)) {
				CACHE_ACCEPTFLOW.put(Staff_ID, str);
			}
		}
		return str;
		// return _SmProDef.GetProdefJson(Staff_ID);
	}

	public List<TreeInfo> GetProdefTree() {
		return _SmProDef.GetProdefTree();
	}
	public List<TreeInfo> GetProdefAsyncTree(String ID,String ISRoute,String showAll) {
		return _SmProDef.GetProdefAsyncTree(ID,ISRoute,showAll);
	}
	
	public List<TreeInfo> ProClassTree(String Pid, String Pname, ArrayList<TreeInfo> TreeList) {
		return _SmProClass.ProClassTree(Pid, Pname, TreeList);
	}

	public List<TreeInfo> GetSysModTree() {
		return _SmSysMod.GetSysModTree();
	}

	public Wfd_Prodef GetProdefById(String id) {
		return _SmProDef.GetProdefById(id);
	}

	public void UpdateWfd_Prodef(Wfd_Prodef Prodef) {
		_SmProDef.UpdateWfd_Prodef(Prodef);
	}

	public SmObjInfo SaveOrUpdate_Prodef(Wfd_Prodef Prodef) {
		return _SmProDef.SaveOrUpdate_Prodef(Prodef);
	}

	public void SaveOrUpdate_ProClass(Wfd_ProClass ProClass) {
		_SmProClass.SaveOrUpdate_ProClass(ProClass);
	}

	public String CreateNewProdefByName(String name, String pid, int index) {
		return _SmProDef.CreateNewProdefByName(name, pid, index);
	}

	public String CreateNewProClassByName(String name, String pid, int index) {
		return _SmProClass.CreateNewProClassByName(name, pid, index);
	}

	public SmObjInfo RenameProClass(String proclassid, String name) {
		return _SmProClass.RenameProClass(proclassid, name);
	}

	public SmObjInfo DelectProClass(String proclassid) {
		return _SmProClass.DelectProClass(proclassid);
	}

	public SmObjInfo DelectProdef(String prodefid) {
		return _SmProDef.DelectProdef(prodefid);
	}

	/**
	 * 流程删除
	 * */
	public SmObjInfo delProdef(String prodef_id) {
		return _SmProDef.delProdef(prodef_id);

	}

	/**
	 * 获取工作流图形图形数据
	 * */
	public Map<String, Object> GetWorkFlow(String atinstid) {
		Map<String, Object> map = new HashMap<String, Object>();
		Wfd_Actdef actdef = smActInst.GetActDef(atinstid);
		if (actdef != null) {
			List<Wfd_Actdef> actdefs = smActDef.GetALLActDefByProdefID(actdef.getProdef_Id());
			List<Wfd_Route> routes = smRouteDef.GetAllRouteByProinstID(actdef.getProdef_Id());
			List<Wfi_ActInst> actInsts = smActInst.GetActInstsbyactactid(atinstid);
			Wfi_ActInst currentHandingAct = smActInst.GetActInst(atinstid);
			map.put("actdef", actdefs);
			map.put("route", routes);
			map.put("curactdef", actdef);
			map.put("history", actInsts);
			map.put("currentHandingAct", currentHandingAct);

		}
		return map;
	}
	public List<User> GetACTSaffByactdefID(String actintid){
		Wfd_Actdef actdef =smActInst.GetActDef(actintid);
		if(actdef!=null){
		   String roleid=smActDef.GetRoleByActDefId(actdef.getActdef_Id());
		  return  roleService.findUsersByRoleId(roleid);
		}
		return null;
	}
    /**
     * 路由转出控制
     * */
    public  List<Wfd_Pass_Condition> GetRouteCondition(String routeid){
    	return smRouteDef.getRouteConditions(routeid);
    	
    }
    
    public  List<Wfd_Actdef> getActdefListByActdefId(String Actdef_Id){
    	List<Wfd_Actdef> list=null;
    	if(Actdef_Id!=null&&!"".equals(Actdef_Id)){
    	  Wfd_Actdef actdef=	smActDef.GetActDefByID(Actdef_Id);
    	  if(actdef!=null)
    		list=smActDef.GetActDefByProdefIDNoSelf(actdef.getProdef_Id(),Actdef_Id);
    	}
    	return list;
    }
    
    public List<Map> GetActDefName(){
    	return smActDef.GetActDefName();
    	
    }
    
    public boolean  cordCheck(String proDefId,String proDefCord){
    	List<Wfd_Prodef> defList = _SmProDef.getProdefByCord(proDefCord);
    	if(null!=defList&&defList.size()>0){
    		for(Wfd_Prodef def : defList){
    			if(!def.getProdef_Id().equals(proDefId)){
    				return false;
    			}
    		}
    	}
		return true;
    }
}
