package com.supermap.wisdombusiness.workflow.service.relationmaintain.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.relationmaintain.IRecordsMaintain;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
/**
 * 用于在人员、角色发生改变的情况下，维护相关的案卷信息
 * @author JHX
 */
@Service("recordsMaintian")
public class RecordsMaintian implements IRecordsMaintain{
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@SuppressWarnings("unused")
	@Override
	public Boolean MaintainRoleStaff(String roleid, Map<String, List<String>> relation) {
		// TODO relation 中包含两个属性 remove  add  两个属性分别记录角色人员的增删情况
		List<String> removeUsers = null;
		List<String> addUsers = null;
		boolean flag = true;
		if(!StringHelper.isEmpty(roleid)){
			if(relation!=null&&!relation.isEmpty()){
				removeUsers = relation.get("remove");
				addUsers = relation.get("add");
				if(removeUsers!=null&&removeUsers.size()>0){
					//TODO:将这个人的所有待办件删除，在办件干成待办件：
					String sql = "";
					List<Map> userFiles = null;
					for(int i=0;i<removeUsers.size();i++){
						 sql = 
								"select distinct(A.ACTINST_ID) ACTINST_ID , B.ACTINST_STATUS\n" +
								"  from  "+Common.WORKFLOWDB+"wfi_actinststaff A\n" + 
								" inner join  "+Common.WORKFLOWDB+"wfi_actinst B\n" + 
								"    ON A.ACTINST_ID = B.ACTINST_ID\n" + 
								"    left join "+Common.WORKFLOWDB+"wfd_actdef c on b.actdef_id = c.actdef_id\n" + 
								" WHERE B.ACTINST_STATUS IN (1, 2, 14, 15)\n" + 
								"  AND A.STAFF_ID = '"+removeUsers.get(i)+"'\n" + 
								"   AND ( B.STAFF_ID is null or B.STAFF_ID='"+removeUsers.get(i)+"' or  B.CODEAL =1)\n" + 
								"  and b.Passedroute_Count is null and c.role_id ='"+roleid+"'";
								 userFiles = commonDao.getDataListByFullSql(sql);
						 if(userFiles!=null&&userFiles.size()>0){
							 Map map = null;
							 String actisntid = "";
							 Wfi_ActInst actinst = null;
							 for(int j=0;j<userFiles.size();j++){
								 map =  userFiles.get(j);
								 if(map!=null&&!map.isEmpty()){
									 actisntid = map.get("ACTINST_ID").toString();
									 if(!StringHelper.isEmpty(actisntid)){
										 actinst = commonDao.get(Wfi_ActInst.class, actisntid);
										 if(actinst!=null){
											 actinst.setStaff_Id("");
											 actinst.setStaff_Name("");
											 actinst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
											 commonDao.saveOrUpdate(actinst);
										 }
										 List<Wfi_ActInstStaff> list = commonDao.getDataList(Wfi_ActInstStaff.class, " select * from "
										 +Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id = '"+actisntid+"' and STAFF_ID = '"+removeUsers.get(i)+"'");
									     if(list!=null&&list.size()>0){
									    	 commonDao.delete(list.get(0));
									     }
									 }
								 }
							 }
						 }
					}
				}
				String isConnect = ConfigHelper.getNameByValue("isConnectRoleAct_Role");
				if(null!=isConnect&&"1".equals(isConnect)||isConnect==null){
					if(addUsers!=null&&addUsers.size()>0){
						//添加人员之后，他需要看到该角色下所有待办的件
						String sql = "select distinct(A.ACTINST_ID) ACTINST_ID from "+Common.WORKFLOWDB+"wfi_actinst A\n" +
										"left join  "+Common.WORKFLOWDB+"wfd_actdef B ON A.ACTDEF_ID = B.ACTDEF_ID\n" + 
										"where  A.Actinst_Status = 1 AND B.ROLE_ID='"+roleid+"'";
						List<Map> maps = commonDao.getDataListByFullSql(sql);
						if(maps!=null&&maps.size()>0){
							 Map map = null;
							 String actisntid = "";
							 for(int j=0;j<maps.size();j++){
								 map =maps.get(j);
								 if(map!=null&&!map.isEmpty()){
									 actisntid=map.get("ACTINST_ID").toString();
									 if(!StringHelper.isEmpty(actisntid)){
										 User user = null;
										 for(int n=0;n<addUsers.size();n++){
											 user = userService.findById(addUsers.get(n));
											 List<Wfi_ActInstStaff> list = commonDao.getDataList(Wfi_ActInstStaff.class, " select * from "
													 +Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id = '"+actisntid+"' and STAFF_ID = '"+user.getId()+"'");
											 if(list!=null&&list.size()==0){//避免重复添加
												 Wfi_ActInstStaff  inststaff = new Wfi_ActInstStaff();
												 inststaff.setStaff_Id(addUsers.get(n));
												 inststaff.setStaff_Name(user.getUserName());
												 inststaff.setActinst_Id(actisntid);
												 commonDao.saveOrUpdate(inststaff);
											 }
											 
										 }
									 }
								 }
							 }
						}
					}					
				}
				commonDao.flush();
			}
		}
		return flag;
	}

	@Override
	public Boolean mainTainFile(Wfd_Actdef Actdef) {
		Wfd_Actdef oldActdef = null;
		String newRoleId = Actdef.getRole_Id();
		List<User> newRoleUsers = null;
		boolean transferData = false;
		if(newRoleId!=null&&!newRoleId.equals("")){
			newRoleUsers = roleService.findUsersByRoleId(newRoleId);
		}
		if(newRoleUsers==null||(newRoleUsers!=null&&newRoleUsers.size()==0)){
			commonDao.update(Actdef);
			commonDao.flush();
			return false;
		}
		if(Actdef!=null){
			oldActdef = commonDao.get(Wfd_Actdef.class, Actdef.getActdef_Id());
			commonDao.clear();
			//多加一层判断，是否开始级联跟新业务数据;配置参数
			if(Actdef.getIsMoveItem().equals("1")){
			    transferData = true;
			}
			if(oldActdef!=null&&oldActdef.getRole_Id()!=null){
				if(!oldActdef.getRole_Id().equals(Actdef.getRole_Id())&&transferData){
					//TODO:角色发生改变之后需要查看当前环节定义所有的待办和在办实例
					List<Wfi_ActInst> actinstlist = 
							commonDao.getDataList(Wfi_ActInst.class, 
									" SELECT * FROM "+Common.WORKFLOWDB+"Wfi_ActInst "
											+ " where ACTDEF_ID='"+Actdef.getActdef_Id()+"' and ACTINST_STATUS in(1,2,14,15)");
					if(actinstlist!=null&&actinstlist.size()>0){
						Wfi_ActInst actdefTemp=null;
						//Wfi_ProInst proinstTemp = null;
						for(int i=0,j = actinstlist.size();i<j;i++){
							actdefTemp = actinstlist.get(i);
							actdefTemp.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
							actdefTemp.setStaff_Id("");
							actdefTemp.setStaff_Name("");
							
							List<Wfi_ActInstStaff> actinstStaffList =  
									commonDao.getDataList(Wfi_ActInstStaff.class, 
											"SELECT * FROM "+Common.WORKFLOWDB+"Wfi_ActInstStaff "
													+ " where ACTINST_ID='"+actdefTemp.getActinst_Id()+"'");
						    if(actinstStaffList!=null&&actinstStaffList.size()>0){
						    	Iterator it = actinstStaffList.iterator();
						    	while(it.hasNext()){
						    		  commonDao.delete((Wfi_ActInstStaff)it.next());
						    	}
						    	if(newRoleUsers!=null&&newRoleUsers.size()>0){
						    		for(int n=0,m=newRoleUsers.size();n<m;n++){
						    			//TODO:是否考虑去掉受理人员:有可能一人多个角色
						    			//if(proinstTemp!=null&&proinstTemp.getStaff_Id()!=null){
						    				//if(!proinstTemp.getStaff_Id().equals(newRoleUsers.get(n).getId())){
						    			
						    			List<Wfi_ActInstStaff> list = commonDao.getDataList(Wfi_ActInstStaff.class, " select * from "
												 +Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id = '"+actdefTemp.getActinst_Id()+"' and STAFF_ID = '"+newRoleUsers.get(n).getId()+"'");
						    					if(list!=null&&list.size()==0){
						    						Wfi_ActInstStaff a = new Wfi_ActInstStaff();
						    						a.setActinst_Id(actdefTemp.getActinst_Id());
						    						a.setStaff_Id(newRoleUsers.get(n).getId());
						    						a.setStaff_Name(newRoleUsers.get(n).getUserName());
						    						a.setRole_Id(newRoleId);
						    						commonDao.saveOrUpdate(a);
						    					}
						    				//}
						    			//}
						    		}
						    	}
						    }
						    commonDao.saveOrUpdate(actdefTemp);
						}
					}
				}
			}
			commonDao.update(Actdef);
		}
		try{
			commonDao.flush();
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean MaintainStaffRole(String staffid,
			Map<String, List<String>> diff) {
		// TODO diff 中包含两个属性 remove  add  两个属性分别记录人员拥有角色的增删情况
		List<String> removeRoles = null;
		List<String> addRoles =null;
		boolean flag = true;
		if(!StringHelper.isEmpty(staffid)){
			if(diff!=null&&!diff.isEmpty()){
				removeRoles = diff.get("remove");
				addRoles = diff.get("add");	
				if(removeRoles!=null&&removeRoles.size()>0){
					String sql="";
					List<Map> roleFiles = null;
					for(int i=0;i<removeRoles.size();i++){
						sql =
								"select distinct(A.ACTINST_ID) ACTINST_ID , B.ACTINST_STATUS\n" +
										"  from  "+Common.WORKFLOWDB+"wfi_actinststaff A\n" + 
										" inner join  "+Common.WORKFLOWDB+"wfi_actinst B\n" + 
										"    ON A.ACTINST_ID = B.ACTINST_ID\n" + 
										"    left join "+Common.WORKFLOWDB+"wfd_actdef c on b.actdef_id = c.actdef_id\n" + 
										" WHERE B.ACTINST_STATUS IN (1, 2, 14, 15)\n" + 
										"  AND A.STAFF_ID = '"+staffid+"'\n" + 
										"   AND ( B.STAFF_ID is null or B.STAFF_ID='"+staffid+"' or  B.CODEAL =1)\n" + 
										"  and b.Passedroute_Count is null and c.role_id ='"+removeRoles.get(i)+"'";
						roleFiles=commonDao.getDataListByFullSql(sql);
						if(roleFiles!=null&&roleFiles.size()>0){
							Map map = null;
							String actinstid="";
							Wfi_ActInst actinst = null;
							for(int j=0;j<roleFiles.size();j++){
								map=roleFiles.get(j);
								if(map!=null&&!map.isEmpty()){
									actinstid=map.get("ACTINST_ID").toString();
									if(!StringHelper.isEmpty(actinstid)){
										actinst = commonDao.get(Wfi_ActInst.class, actinstid);
										if(actinst!=null){
											actinst.setStaff_Id("");
											actinst.setStaff_Name("");
											actinst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
											commonDao.saveOrUpdate(actinst);
										}
										List<Wfi_ActInstStaff> list = commonDao.getDataList(Wfi_ActInstStaff.class, 
												"select * from "+Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id='"+actinstid+"' and STAFF_ID='"+staffid+"'"); 
										if(list!=null&&list.size()>0){
											commonDao.delete(list.get(0));
										}
										List<Wfi_ActInstStaff> list1 = commonDao.getDataList(Wfi_ActInstStaff.class, 
												"select * from "+Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id='"+actinstid+"'"); 
										if(null==list1||list1.size()<1){
											List<User> users = roleService.findUsersByRoleId(removeRoles.get(i));
											if(null!=users&&users.size()>0){
												for(User user : users ){
													
													List<Wfi_ActInstStaff> list2 = commonDao.getDataList(Wfi_ActInstStaff.class, " select * from "
															 +Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id = '"+actinstid+"' and STAFF_ID = '"+user.getId()+"'");
													 if(list!=null&&list.size()==0){//避免重复添加
														 Wfi_ActInstStaff  inststaff = new Wfi_ActInstStaff();
														 inststaff.setStaff_Id(user.getId());
														 inststaff.setStaff_Name(user.getUserName());
														 inststaff.setActinst_Id(actinstid);
														 inststaff.setRole_Id(removeRoles.get(i));
														 commonDao.saveOrUpdate(inststaff);
													 }
												}
											}
										}
									}
								}
							}
						}
					}
				}
				String isConnect = ConfigHelper.getNameByValue("isConnectRoleAct_User");
				if(null!=isConnect&&"1".equals(isConnect)||isConnect==null){
					if(addRoles!=null&&addRoles.size()>0){					
						List<Map> maps = new ArrayList<Map>();
						for(int i=0;i<addRoles.size();i++){
							String sql = 
									"select distinct(A.ACTINST_ID) ACTINST_ID from "+Common.WORKFLOWDB+"wfi_actinst A\n" +
									"left join  "+Common.WORKFLOWDB+"wfd_actdef B ON A.ACTDEF_ID = B.ACTDEF_ID\n" + 
									"where  A.Actinst_Status = 1 AND B.ROLE_ID='"+addRoles.get(i)+"'";;
							List<Map> tempmaps = commonDao.getDataListByFullSql(sql);
							if(tempmaps!=null&&tempmaps.size()>0){
								for(int k=0;k<tempmaps.size();k++){
									maps.add(tempmaps.get(k));
								}
							}
							
						}
						if(maps!=null&&maps.size()>0){
							Map map = null;
							String actinstid="";
							User user = userService.findById(staffid);
							for(int j=0;j<maps.size();j++){
								map=maps.get(j);
								if(map!=null&&!map.isEmpty()){
									actinstid=map.get("ACTINST_ID").toString();
									if(!StringHelper.isEmpty(actinstid)){
											 List<Wfi_ActInstStaff> list2 = commonDao.getDataList(Wfi_ActInstStaff.class, " select * from "
													 +Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id = '"+actinstid+"' and STAFF_ID = '"+user.getId()+"'");
											 if(null==list2||list2.size()<1){
												 Wfi_ActInstStaff  inststaff = new Wfi_ActInstStaff();
												 inststaff.setStaff_Id(staffid);
												 inststaff.setStaff_Name(user.getUserName());
												 inststaff.setActinst_Id(actinstid);
												 commonDao.saveOrUpdate(inststaff);
											 }
									}
								}
							}
						}
					}					
				}
				commonDao.flush();
			}
		}
		return flag;
	}
   
}
