package com.supermap.wisdombusiness.workflow.service.wfm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_MaterClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_RoleClass;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;

/**
 * 
 * @author DreamLi
 * @date 2015.6.18
 */
@Service("smDropdownService")
public class SmDropdownService {

	@Autowired
	private CommonDao commonDao;
    @Autowired
    private RoleService roleService;
	/**
	 * 角色类别下拉
	 * @return
	 */
	public List<TreeInfo> RoleClassDropdown() {
		ArrayList<TreeInfo> treelist = new ArrayList<TreeInfo>();
		return GetRoleClassDropdown(null, "", treelist);
	}

	private List<TreeInfo> GetRoleClassDropdown(Wfd_RoleClass RoleClass,
			String Pre, List<TreeInfo> treelist) {
		StringBuilder str = new StringBuilder();
		if (RoleClass == null) {
			str.append(" Parentclassid is null");
			treelist = new ArrayList<TreeInfo>();
		} else {
			str.append(" Parentclassid='");
			str.append(RoleClass.getRoleclass_Id());
			str.append("'");
			Pre = Pre + "--";
		}

		str.append(" order by Class_Index");
		List<Wfd_RoleClass> list = commonDao.findList(Wfd_RoleClass.class,
				str.toString());
		for (int i = 0; i < list.size(); i++) {
			Wfd_RoleClass _RoleClass = list.get(i);
			TreeInfo tree = new TreeInfo();
			tree.setId(_RoleClass.getRoleclass_Id());
			tree.setPreid(_RoleClass.getParentclassid());
			tree.setText(Pre+_RoleClass.getClass_Name());
			treelist.add(tree);
			GetRoleClassDropdown(_RoleClass, Pre, treelist);
		}

		return treelist;
	}
	
	/**
	 * 角色下拉
	 * @return
	 */
	public List<Tree> RoleDropdown()
	{
		ArrayList<TreeInfo> treelist = new ArrayList<TreeInfo>();
		return GetRoleDropdown(null, "", treelist);
	}
	
	@SuppressWarnings("unused")
	private List<Tree> GetRoleDropdown(Wfd_RoleClass RoleClass,
			String Pre, List<TreeInfo> treelist) {
//		StringBuilder str = new StringBuilder();
//		if (RoleClass == null) {
//			str.append(" Parentclassid is null");
//			treelist = new ArrayList<TreeInfo>();
//		} else {
//			str.append(" Parentclassid='");
//			str.append(RoleClass.getRoleclass_Id());
//			str.append("'");
//			Pre = Pre + "--";
//		}
//
//		str.append(" order by Class_Index");
//		List<Wfd_RoleClass> list = commonDao.findList(Wfd_RoleClass.class,
//				str.toString());
//		
//		if (RoleClass != null) {
//			
//			TreeInfo pretree = new TreeInfo();
//			pretree.setType("catalog");
//			pretree.setId(RoleClass.getRoleclass_Id());
//			pretree.setText(Pre+"　"+RoleClass.getClass_Name());
//			treelist.add(pretree);
//			//Pre = Pre + "　";
//		}
//		
//		for (int i = 0; i < list.size(); i++) {
//			Wfd_RoleClass _RoleClass = list.get(i);		
//			GetRoleDropdown(_RoleClass, Pre, treelist);
//		}
//		
//		if(RoleClass!=null)
//		{
//			StringBuilder str2=new StringBuilder();
//			str2.append("Roleclass_Id='");
//			str2.append(RoleClass.getRoleclass_Id());
//			str2.append("' order by Role_Index");
//			List<Wfd_Role> list2=commonDao.findList(Wfd_Role.class, str2.toString());
//			for(int i=0;i<list2.size();i++)
//			{
//				Wfd_Role role=list2.get(i);
//				TreeInfo tree=new TreeInfo();
//				tree.setPreid(role.getRoleclass_Id());
//				tree.setId(role.getRole_Id());
//				tree.setText(Pre+role.getRole_Name());
//				treelist.add(tree);
//			}	
//		}
		TreeInfo pretree = new TreeInfo();
		return  roleService.getRoleTree();
		//return treelist;
	}
	
	
	/**
	 * 资料类别下拉
	 */
	public List<TreeInfo> MaterClassDropdown() {
		ArrayList<TreeInfo> treelist = new ArrayList<TreeInfo>();
		return GetMaterClassDropdown(null, "", treelist);
	}
	
	private List<TreeInfo> GetMaterClassDropdown(Wfd_MaterClass MaterClass,
			String Pre, List<TreeInfo> treelist) {
		StringBuilder str = new StringBuilder();
		if (MaterClass == null) {
			str.append(" Materialtype_Pid is null");
			treelist = new ArrayList<TreeInfo>();
		} else {
			str.append(" Materialtype_Pid='");
			str.append(MaterClass.getMaterialtype_Id());
			str.append("'");
			Pre = Pre + "--";
		}

		str.append(" order by Materialtype_Index");
		List<Wfd_MaterClass> list = commonDao.findList(Wfd_MaterClass.class,
				str.toString());
		for (int i = 0; i < list.size(); i++) {
			Wfd_MaterClass _MaterClass = list.get(i);
			TreeInfo tree = new TreeInfo();
			tree.setPreid(_MaterClass.getMaterialtype_Pid());
			tree.setId(_MaterClass.getMaterialtype_Id());
			tree.setText(Pre+_MaterClass.getMaterialtype_Name());
			treelist.add(tree);
			GetMaterClassDropdown(_MaterClass, Pre, treelist);
		}

		return treelist;
	}

}
