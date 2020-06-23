package com.supermap.wisdombusiness.workflow.service.wfd;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;

@Component("smProClass")
public class SmProClass {

	@Autowired
	CommonDao _CommonDao;

	public Wfd_ProClass GetProClassByID(String id) {
		return _CommonDao.get(Wfd_ProClass.class, id);
	}

	public List<Wfd_ProClass> FindAll() {
		return _CommonDao.findList(Wfd_ProClass.class,
				" 1=1 order by Prodefclass_Index");
	}

	public void SaveOrUpdate_ProClass(Wfd_ProClass ProClass) {
		StringBuilder _str = new StringBuilder();
		_str.append("Prodefclass_Id='");
		_str.append(ProClass.getProdefclass_Id());
		_str.append("'");
		List<Wfd_ProClass> list = _CommonDao.findList(Wfd_ProClass.class,
				_str.toString());
		if (list.size() > 0) {
			_CommonDao.clear();
			_CommonDao.update(ProClass);

		} else {
			_CommonDao.save(ProClass);
		}
		_CommonDao.flush();

	}

	public List<TreeInfo> ProClassTree(String Pid, String Pname,
			ArrayList<TreeInfo> TreeList) {
		StringBuilder _str = new StringBuilder();
		if (Pid == null) {
			Pname="--";
			TreeList = new ArrayList<TreeInfo>();
			_str.append("Prodefclass_Pid is null order by Prodefclass_Index ");
		} else {
			_str.append("Prodefclass_Pid = '");
			_str.append(Pid);
			_str.append("' order by Prodefclass_Index");
			if (Pname != null)
				Pname = Pname+"--";
		}

		List<Wfd_ProClass> list = _CommonDao.findList(Wfd_ProClass.class,
				_str.toString());
		for (int i = 0; i < list.size(); i++) {
			Wfd_ProClass _ProClass = list.get(i);
			TreeInfo _tree = new TreeInfo();
			_tree.setId(_ProClass.getProdefclass_Id());
			_tree.setText(Pname+_ProClass.getProdefclass_Name());
			TreeList.add(_tree);
			ProClassTree(_ProClass.getProdefclass_Id(),
					Pname, TreeList);
		}
		return TreeList;
	}
	
	public String CreateNewProClassByName(String name,String pid,int index)
	{
		Wfd_ProClass _ProClass=new Wfd_ProClass();
		_ProClass.setProdefclass_Name(name);
		_ProClass.setProdefclass_Index(index);
		_ProClass.setProdefclass_Pid(pid);
		_CommonDao.save(_ProClass);
		_CommonDao.flush();
		return _ProClass.getProdefclass_Id();
	}
	
	
	public SmObjInfo RenameProClass(String proclassid,String name){
		SmObjInfo smInfo=new SmObjInfo();
		if(proclassid!=null&&!proclassid.equals("")){
			Wfd_ProClass wfdclassClass=_CommonDao.get(Wfd_ProClass.class, proclassid);
			wfdclassClass.setProdefclass_Name(name);
			_CommonDao.update(wfdclassClass);
			_CommonDao.flush();
			smInfo.setID(wfdclassClass.getProdefclass_Id());
			smInfo.setDesc("更新成功");
		}
		return smInfo;
	}
    public SmObjInfo DelectProClass(String proclassid ){
    	//删除流程分类需要删除 1.分类信息  流程信息  活动信息 资料信息 活动表单
    	//当前先直接切断关系，后续补充
    	SmObjInfo smObjInfo=new SmObjInfo();
    	_CommonDao.delete(Wfd_ProClass.class,proclassid);
    	_CommonDao.flush();
    	smObjInfo.setID(proclassid);
    	smObjInfo.setDesc("删除成功");
    	return smObjInfo;
    }
    /**
     * 
     *
     * @return
     * 2015年10月15日
     */
    public boolean RebuildWorkflowNodeIndex(String from,String to){
    	boolean Result=false;
    	if(from!=null&&!from.equals("")){
    		Wfd_Prodef prodef=_CommonDao.get(Wfd_Prodef.class, from);
    		if(prodef!=null){
    			//检测目录是否存在
    			Wfd_ProClass proclass=_CommonDao.get(Wfd_ProClass.class, to);
    			if(proclass!=null){
    				prodef.setProdefclass_Id(to);
    				_CommonDao.update(prodef);
    				_CommonDao.flush();
    				Result=true;
    			}
    		}
    	}
    	
	    return Result;
	}
}
