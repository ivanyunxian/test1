package com.supermap.wisdombusiness.synchroinline.service;

import com.supermap.realestate.registration.model.T_CONFIG;
import com.supermap.wisdombusiness.framework.dao.RoleDao;
import com.supermap.wisdombusiness.framework.dao.RoleGroupDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.synchroinline.model.Pro_actdef;
import com.supermap.wisdombusiness.synchroinline.model.T_certificate_ls;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.web.ui.tree.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by LeiTing on 2017/7/7.
 */
@Service
public class InlineRoleService {

    @Autowired
    CommonDao dao;

    @Autowired
    private RoleGroupDao roleGroupDao;


    @Autowired
    private RoleDao roleDao;

    //声明Role表的表名
    String role_route_table = "smwb_inline.PRO_ACTDEF"; //role表

    public InlineRoleService()
    {
    }

    /**
     * 根据userid，查询userid允许受理的项目prodefId集合
     * @param userid
     * @return
     */
    public List<Map> QueryStaffProdefId(String userid)
    {
        // 根据userid查询角色路由表中，所拥有的所有角色，允许办理的业务类型
        String sql = "select a.prodefid,a.shzt from "+ role_route_table +" a where a.roleid in (select t.roleid from smwb_framework.rt_userrole t where t.userid = '" + userid + "') group by a.prodefid,a.shzt ";
        List<Map> roles = dao.getDataListByFullSql(sql);
        return roles;
    }

    /**
     * 查询角色列表
     */
    public List<Map> findAll()
    {
        String sql = "select * from "+ role_route_table ;
        List<Map> RoleList = dao.getDataListByFullSql(sql);
        return RoleList;
    }

    /**
     * 根据流程ID，审核状态，查询所有的角色组
     */
    public List<Map> findRoleByProdef (String ProdefId, int shzt){
        String sql = "select * from "+ role_route_table +" a where a.prodefid = '" + ProdefId + "' and a.shzt = '" + shzt + "'";
        List<Map> roles = dao.getDataListByFullSql(sql);
        return roles;
    }
    public List<Map> findRoleByProdef (String ProdefId){
        String sql = "select * from "+ role_route_table +" a where a.prodefid = '" + ProdefId + "'";
        List<Map> roles = dao.getDataListByFullSql(sql);
        return roles;
    }

    /**
     * 根据RoldId，查询所有流程
     */
    public List<Map> findProdefByRole (String RoleId){
        String sql = "select * from "+ role_route_table +" a where a.roleid = '" + RoleId +  "'";

        List<Map> results = dao.getDataListByFullSql(sql);
        return results;
    }


    /**
     * 返回所有的流程
     */
    public List<Pro_actdef> findAllProdef (){
        List<Pro_actdef> results = dao.getDataList(Pro_actdef.class, "roleid = '0'");
        return results;
    }

    public Pro_actdef findById( String id){
        Pro_actdef result = dao.get(Pro_actdef.class,id);
        return result;
    }

    /**
     * 保存在线受理流程
     * @param role
     */
    public void saveProdef(Pro_actdef role) {
        //dao.save(role);
        dao.saveOrUpdate(role);
    }

    /**
     * 更新在线受理流程
     * @param role
     */
    public void updateProdef(Pro_actdef role) {
        dao.saveOrUpdate(role);
    }

    /**
     * 根据pro_actdef的主键id，删除某记录
     * @param id
     */
    public void deleteProdefByActId(String id) {
        this.deleteProdefsByActId(id);
        dao.delete(Pro_actdef.class,id);
    }

    /**
     * 根据pro_actdef的主键id，删除与其具备同样流程ID及审核状态的记录
     */
    public void deleteProdefsByActId(String id){
        Pro_actdef del = dao.get(Pro_actdef.class,id);
        this.deleteProdef(del.getProdefid(),del.getShzt());
    }

    /**
     * 根据protdefID，查询所有的角色ID
     */
    public List<Map> getActdefByProdefId(String ProdefId){
        String sql = "select * from SMWB_FRAMEWORK.T_ROLE a where a.id in (select b.roleid from SMWB_INLINE.pro_actdef b where b.prodefid = '" + ProdefId + "' )";
        List<Map> results = dao.getDataListByFullSql(sql);
        return results;
    }

    /**
     * 根据RowID，查询所有的角色ID
     */
    public List<Map> getActdefByRowId(String RowId){
        Pro_actdef pro = dao.get(Pro_actdef.class, RowId);
        String ProdefId = pro.getProdefid();
        Integer shzt = pro.getShzt();
        String sql = "select * from SMWB_FRAMEWORK.T_ROLE a where a.id in (select b.roleid from SMWB_INLINE.pro_actdef b where b.prodefid = '" + ProdefId + "' and b.shzt = '" + shzt + "')";
        List<Map> results = dao.getDataListByFullSql(sql);
        return results;
    }


    /**
     * 设置角色Tree
     * @param id
     * @return
     */
    public List<Tree> setRoleTree(String id) {
        List<Tree> treelist = new ArrayList<Tree>();
        List<RoleGroup> roleGroups = roleGroupDao.findAll();
        List<Map> actdefs = this.getActdefByRowId(id);
        //String roleIds[] = new Arrays();
        for (RoleGroup roleGroup : roleGroups) {
            Tree tree = new Tree();
            tree.setId(roleGroup.getId());
            tree.setText(roleGroup.getGroupName());
            tree.setParentid(null);
            tree.setTypeStr("jsz");
            treelist.add(tree);
            List<Role> roles = roleDao.getRolesByGroupId(roleGroup.getId());
            for (Role roleEn : roles) {
                Tree treechild = new Tree();
                treechild.setId(roleEn.getId());
                treechild.setText(roleEn.getRoleName());
                treechild.setParentid(roleGroup.getId());
                treechild.setTypeStr("js");
                //设置一些为选中的
                if (id != null && !id.equals("")) {
                    for( Map roleId : actdefs){
                        /*if(roleEn.getRoleName().equals("管理员")){
                            Object tmp = roleId.get("ID");
                            String tmp2 = roleEn.getId();
                            treechild.setChecked(true);
                        }*/
                        if(roleEn.getId().equals(roleId.get("ID")) ){
                            treechild.setChecked(true);
                        }
                    }
                }
                treelist.add(treechild);
            }

        }
        return TreeUtil.build(treelist);
    }


    /**
     * 删除actdef表的数据
     */
    public void deleteProdef(String ProdefId, Integer shzt){
        //获取要删除的数据
        String sql = "select a.id from "+ role_route_table +" a where a.prodefid = '" + ProdefId + "' and a.shzt = '" + shzt + "' and a.roleid != '0'" ;
        List<Map> waitToDeletes = dao.getDataListByFullSql(sql);
        //根据主键删除
        for (Map waitToDelete : waitToDeletes){
            dao.delete(Pro_actdef.class,String.valueOf(waitToDelete.get("ID")));
        }
    }

    /**
     * 在actdef表中增加多条记录
     */
    public void saveProdefs(String ProdefId, Integer shzt, String roleIds){
        // 获取流程名称
        String ProdefName = "";
        List<Map> tmps = this.findRoleByProdef(ProdefId);
        for ( Map tmp : tmps){
            if (tmp.get("PRODEFNAME") != null){
                ProdefName = String.valueOf(tmp.get("PRODEFNAME"));
                break;
            }
        }
        // 获取唯一主键
        Long id = System.currentTimeMillis();
        if (roleIds != null && !roleIds.equals("")) {
            // 获取rolesId
            String roleId[] = roleIds.split(",");
            for (int i = 0; i < roleId.length; i++) {

                Pro_actdef proActdef = new Pro_actdef();
                proActdef.setId(String.valueOf(id));
                proActdef.setProdefid(ProdefId);
                proActdef.setProdefname(ProdefName);
                proActdef.setShzt(shzt);
                proActdef.setRoleid(roleId[i]);
                dao.save(proActdef);
                dao.flush();
                id -= 1;
            }
        }
    }

    //设置角色管理功能是否启用
    public void setIsAble(String isAble) {
        T_CONFIG config = new T_CONFIG();
        if (isAble == "" || isAble.length()<1){
            config.setId("1165");
            config.setCLASSNAME("与在线受理相关");
            config.setNAME("InlineRoleAble");
            config.setVALUE("1");  //第一次设置为启用
            config.setVALUEDESCRIPTION("是否启用在线受理的业务分发功能");
            config.setDESCRIPTION("是否启用在线受理的业务分发功能");
            config.setCONFIGNAME("是否启用业务分发");
            config.setVALUETYPE("2");
            config.setOPTIONCLASS("IFENABLE");
            config.setYXBZ(1);
            config.setCONFIGED(0);
        }else{
            config = dao.get(T_CONFIG.class,"1165");
            config.setVALUE(isAble);
        }
        dao.saveOrUpdate(config);
    }

    public String getIsAble(){
        String result = "";
        Long count = dao.getCountByFullSql( "from SMWB_SUPPORT.T_CONFIG where id = '1165'");
        if (count < 1){
            return result;
        }
        try {
            result = dao.get(T_CONFIG.class,"1165").getVALUE();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取流程分页信息
     * @param pageIndex
     * @param pageSize
     * @param mapCondition
     * @return
     */

}
