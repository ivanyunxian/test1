package com.supermap.realestate_gx.registration.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.wisdombusiness.synchroinline.util.SelectorTools;
import net.sf.json.JSONArray;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.supermap.bdcapi.projectAPI;
import com.supermap.bdcapi.workflowAPI;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.service.impl.QLServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.model.Unit_EX;
import com.supermap.realestate_gx.registration.model.Unit_EX.QL_EX;
import com.supermap.realestate_gx.registration.service.WorkflowService;
import com.supermap.realestate_gx.registration.util.ConverterUtil;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.services.rest.util.BracketFilter;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.util.FileUpload;

@Component
public class WorkflowServiceImpl implements WorkflowService {
    public static JSONArray zdmapobj = null;//映射表的对象
    @Autowired
    private CommonDao baseCommonDao;
    @Autowired
    private QLServiceImpl qlServiceimpl;
    public Message acceptProject(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String xmbh = RequestHelper.getParam(request, "xmbh");
        String mapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳州协同共享数据与登记系统数据映射关系.txt", request.getRealPath("/"));
        String beforetime = StringHelper.FormatYmdhmsByDate(new Date());

        //初始化工具类
        ConverterUtil util = new ConverterUtil(mapfilepath);
        String bdcdyh = "";
        List<Map> projects = get_sharesearchData("select * from sharesearch.t_project  where xmbh='" + xmbh + "'");
        List<Map> houses = get_sharesearchData("select * from sharesearch.t_house where xmbh='" + xmbh + "'");
        //预告登记与抵押合并登记的时候，银行方不能拿到不动产单元号,则通过坐落查找单元号
        if (houses.get(0).get("ZL") != null && !"".equals(houses.get(0).get("ZL")) && houses.get(0).get("BDCDYH") == null) {
            String findBdcdyh = "";
            //截取坐落最后一位"号"字进行模糊搜索
            String zl = (String) houses.get(0).get("ZL");
            if (zl.endsWith("号")) {
                zl = zl.substring(0, zl.length() - 1);
            }

            if ("032".equals(String.valueOf(houses.get(0).get("BDCDYLX")))) {
                findBdcdyh = "select * from bdcdck.bdcs_h_gzy where zl like '" + zl + "%'";
            } else {
                findBdcdyh = "select * from bdcdck.bdcs_h_xzy where zl like '" + zl + "%'";
            }
            List<Map> list = this.baseCommonDao.getDataListByFullSql(findBdcdyh);
            if (list.size() > 0) {

                houses.get(0).put("BDCDYH", list.get(0).get("BDCDYH"));
            }
        }
        List<Map> holders = get_sharesearchData("select * from sharesearch.t_holder where xmbh='" + xmbh + "'");
        List<Map> allMap = new ArrayList<Map>();
        Map result = new HashMap();
        Map share = new HashMap();
        Map djxt = new HashMap();
        if (houses != null && houses.size() > 0)
            bdcdyh = String.valueOf(houses.get(0).get("BDCDYH"));
        util.AddPrefix(projects, "sharesearch.t_project");
        util.AddPrefix(houses, "sharesearch.t_house");
        util.AddPrefix(holders, "sharesearch.t_holder");
        allMap.addAll(projects);
        allMap.addAll(houses);
        //共享库填的信息
        BDCS_H_XZ BDCS_H_XZ_share = util.CreateSingleClass(BDCS_H_XZ.class, allMap);
        BDCS_QL_XZ BDCS_QL_XZ_share = util.CreateSingleClass(BDCS_QL_XZ.class, allMap);
        BDCS_FSQL_XZ BDCS_FSQL_XZ_share = util.CreateSingleClass(BDCS_FSQL_XZ.class, allMap);
        BDCS_QLR_XZ qlr_share = util.CreateSingleClass(BDCS_QLR_XZ.class, allMap);//所有权人
        List<BDCS_QLR_XZ> qlrs_share = util.CreateClass(BDCS_QLR_XZ.class, holders);//共有权人
        qlrs_share.add(qlr_share);
        List<Map<String, Object>> qlrs = new ArrayList<Map<String, Object>>();
        for (BDCS_QLR_XZ _qlr : qlrs_share) {
            qlrs.add(transBean2Map(_qlr));
        }
        Unit_EX share_unit = new Unit_EX();
        Unit_EX.QL_EX share_ql = new Unit_EX().new QL_EX();
        List<QL_EX> qls = new ArrayList<QL_EX>();
        share_unit.setQls(qls);
        //登记库填的信息
        if (!StringUtils.isEmpty(bdcdyh) && !bdcdyh.toLowerCase().equals("null")) {
            BDCS_H_XZ BDCS_H_XZ_djxt = null;
            BDCS_QL_XZ BDCS_QL_XZ_djxt = null;
            BDCS_FSQL_XZ BDCS_FSQL_XZ_djxt = null;
            BDCS_QLR_XZ qlr_djxt = null;//所有权人
            List<BDCS_QLR_XZ> qlrs_djxt = null;//共有权人
            List<BDCS_H_XZ> BDCS_H_XZs = baseCommonDao.getDataList(BDCS_H_XZ.class, "BDCDYH='" + bdcdyh + "'");
            for (BDCS_H_XZ h : BDCS_H_XZs) {
                List<BDCS_DJDY_XZ> bdcs_djdy_xzs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYH='" + bdcdyh + "'");
                if (bdcs_djdy_xzs != null && bdcs_djdy_xzs.size() > 0) {
                    List<BDCS_QL_XZ> dyqs = baseCommonDao.getDataList(BDCS_QL_XZ.class, "DJDYID='" + bdcs_djdy_xzs.get(0).getDJDYID() + "' AND QLLX='23'");
                    for (BDCS_QL_XZ dyq : dyqs) {
                        List<BDCS_FSQL_XZ> fsql_dyqs = baseCommonDao.getDataList(BDCS_FSQL_XZ.class, "QLID='" + dyq.getId() + "'");
                    }
                }
            }
        } else {

        }

        return new Message();
    }

    /**
     * 将对象转换成MAP
     *
     * @param obj
     * @return
     * @Title: transBean2Map
     * @author:wuzhu
     * @date：
     *///将对象转换成MAP
    public Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("对象转换为MAP失败： " + e);
        }
        return map;
    }


    public Message search2(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        List<Map> ResultMaps = new ArrayList<Map>();
        Map validate = new HashMap();
        String ywh = RequestHelper.getParam(request, "ywh");
        String bdcdyh = "", cqzh = "", bdcdylx = "", share_zl = "";
        String shareFullSql = "select project.CQRXM,project.CQZH,project.CQRZJHM,project.FWZL,house.BDCDYH,house.ZL,house.BDCDYLX from sharesearch.t_project project left join sharesearch.t_house house on project.xmbh=house.xmbh  where project.xmbh='" + ywh + "'";
        String djxtFullSql = "select djdy.bdcdyid,djdy.djdyid,ql.qlid,ql.bdcqzh from bdck.bdcs_djdy_xz djdy left join bdck.BDCS_QL_XZ ql on djdy.djdyid=ql.djdyid ";
        List<Map> xmxx = get_sharesearchData(shareFullSql);
        if (xmxx != null && xmxx.size() > 0) {
            bdcdyh = String.valueOf(xmxx.get(0).get("BDCDYH")).trim();
            cqzh = String.valueOf(xmxx.get(0).get("CQZH")).trim();
            bdcdylx = String.valueOf(xmxx.get(0).get("BDCDYLX")).trim();
            share_zl = String.valueOf(xmxx.get(0).get("FWZL")).trim();
        }
        if (!StringUtils.isEmpty(bdcdyh)) {
            StringBuilder bdcqzh = new StringBuilder();
            StringBuilder qlrmc = new StringBuilder();
            StringBuilder qlrzjh = new StringBuilder();
            List<RealUnit> units = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, "BDCDYH='" + bdcdyh + "'");
            List<Map> xx = this.baseCommonDao.getDataListByFullSql(djxtFullSql + " where djdy.bdcdylx='" + bdcdylx + "' and djdy.bdcdyh='" + bdcdyh + "' and ql.qllx='4' ");
            int indexi = 0;
            for (Map r : xx) {
                indexi++;
                bdcqzh.append(r.get("BDCQZH"));
                if (indexi < xx.size())
                    bdcqzh.append(",");
                List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, String.valueOf(r.get("QLID")));
                int indexj = 0;
                for (RightsHolder qlr : qlrs) {
                    indexj++;
                    qlrmc.append(qlr.getQLRMC());
                    if (indexj < qlrs.size())
                        qlrmc.append(",");
                    qlrzjh.append(qlr.getZJH());
                    if (indexj < qlrs.size())
                        qlrzjh.append(",");
                }
            }

            Map bdcdyh_map = new HashMap();
            //bdcdyh_map.put("ZL", units!=null&&units.size()>0?units.get(0).getZL():"");
            bdcdyh_map.put("ZL", share_zl);
            bdcdyh_map.put("BDCDYID", units != null && units.size() > 0 ? units.get(0).getId() : "");
            bdcdyh_map.put("BDCDYLX", bdcdylx);
            bdcdyh_map.put("QLRMC", qlrmc.toString());
            bdcdyh_map.put("ZJH", qlrzjh.toString());
            bdcdyh_map.put("BDCDYH", bdcdyh);
            bdcdyh_map.put("BDCQZH", bdcqzh.toString());
            bdcdyh_map.put("YWH", ywh);
            if (!validate.containsKey(bdcdyh)) {
                ResultMaps.add(bdcdyh_map);
                validate.put(bdcdyh, "");
            }
        }
        if (!StringUtils.isEmpty(cqzh)) {
            StringBuilder bdcqzh = new StringBuilder();
            StringBuilder qlrmc = new StringBuilder();
            StringBuilder qlrzjh = new StringBuilder();
            String bdcdyh2 = "", qlid = "";
            List<Map> xx = this.baseCommonDao.getDataListByFullSql("select qdzr.DJDYID,qdzr.QLID,qdzr.QLRID,qdzr.ZSID,qdzr.BDCDYH from bdck.bdcs_qdzr_xz qdzr left join bdck.bdcs_zs_xz  zs on qdzr.zsid=zs.zsid where zs.bdcqzh='" + cqzh + "'");
            List<RealUnit> units = null;
            if (xx != null && xx.size() > 0) {
                bdcdyh2 = String.valueOf(xx.get(0).get("BDCDYH"));
                qlid = String.valueOf(xx.get(0).get("QLID"));

                units = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, "BDCDYH='" + bdcdyh2 + "'");
                List<Rights> rights = RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLID = '" + qlid + "'");
                int indexi = 0;
                for (Rights r : rights) {
                    indexi++;
                    bdcqzh.append(r.getBDCQZH());
                    if (indexi < rights.size())
                        bdcqzh.append(",");
                    List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, r.getId());
                    int indexj = 0;
                    for (RightsHolder qlr : qlrs) {
                        indexj++;
                        qlrmc.append(qlr.getQLRMC());
                        if (indexj < qlrs.size())
                            qlrmc.append(",");
                        qlrzjh.append(qlr.getZJH());
                        if (indexj < qlrs.size())
                            qlrzjh.append(",");
                    }
                }
            }
            Map bdcdyh_map = new HashMap();
            bdcdyh_map.put("ZL", units != null && units.size() > 0 ? units.get(0).getZL() : "");
            bdcdyh_map.put("BDCDYID", units != null && units.size() > 0 ? units.get(0).getId() : "");
            bdcdyh_map.put("BDCDYLX", bdcdylx);
            bdcdyh_map.put("QLRMC", qlrmc.toString());
            bdcdyh_map.put("ZJH", qlrzjh.toString());
            bdcdyh_map.put("BDCDYH", bdcdyh2);
            bdcdyh_map.put("BDCQZH", bdcqzh.toString());
            bdcdyh_map.put("YWH", ywh);
            if (!validate.containsKey(bdcdyh2) && !StringUtils.isEmpty(bdcdyh_map.get("BDCDYID"))) {
                ResultMaps.add(bdcdyh_map);
                validate.put(bdcdyh2, "");
            }
        }
        Message r = new Message();
        r.setRows(ResultMaps);
        r.setSuccess("true");
        r.setTotal(ResultMaps.size());
        return r;
    }

    @Override
    public Message search(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        String xzqh = ConfigHelper.getNameByValue("XZQHDM");

        List<Map> ResultMaps = new ArrayList<Map>();
        Map validate = new HashMap();
        String ywh = RequestHelper.getParam(request, "ywh");
        String bdcdyh = "", bdcdylx = "", share_zl = "", ywlx = "";//---ywlx 包基杰
        StringBuilder qlrmc = new StringBuilder();
        StringBuilder cqzh = new StringBuilder();

        StringBuilder zjh = new StringBuilder();
        String shareHolderSql = "select qlrmc,bdcqzh,zjh,ygsqr from sharesearch.t_holder where xmbh='" + ywh + "'";
        String shareFullSql = "select project.CREATETIME,project.CQCS,project.CQRXM,project.CQZH,project.CQRZJHM,project.FWZL,project.SLRYID,project.YWLX,project.cqcs,house.BDCDYH,house.ZL,house.BDCDYLX from sharesearch.t_project project left join sharesearch.t_house house on project.xmbh=house.xmbh  where project.xmbh='" + ywh + "'";//----添加查找业务类型字段 2017.03.29包基杰
        String djxtFullSql = "select djdy.bdcdyid,djdy.djdyid,ql.qlid,ql.bdcqzh from bdck.bdcs_djdy_xz djdy left join bdck.BDCS_QL_XZ ql on djdy.djdyid=ql.djdyid ";
        List<Map> xmxx = get_sharesearchData(shareFullSql);
        //贺州不允许重复创建
        Message r = new Message();
        if (xmxx.size() == 0) {//没找到直接返回了

            r.setRows(ResultMaps);
            r.setMsg("true");
            r.setTotal(ResultMaps.size());
            return r;
        }


        if ("451102".equals(xzqh) && xmxx.size() > 0 && !StringHelper.isEmpty(xmxx.get(0).get("CQCS")) && Integer.parseInt(String.valueOf(xmxx.get(0).get("CQCS"))) > 0) {
            r.setRows(ResultMaps);
            r.setMsg("dealted");
            r.setTotal(ResultMaps.size());
            return r;
        }


        //获取机构，房开填写远程报件时，房开的信息存放在dept中
        List<Map> fk = null;
        if (xmxx != null && xmxx.size() > 0) {
            String shareDeptSql = "select dept.* from sharesearch.T_PROJECT t join sharesearch.USERS u on u.id=t.slryid join sharesearch.dept dept on dept.jgbh=u.ssjgdm where t.slryid = '" + xmxx.get(0).get("SLRYID") + "'";
            String jgdm = ywh.substring(7, 9);
            if ("11".equals(jgdm)) {
                fk = get_sharesearchData(shareDeptSql);
            }
        }
        if (xmxx != null && xmxx.size() > 0) {
            if (!StringUtils.isEmpty(xmxx.get(0).get("BDCDYH")))
                bdcdyh = String.valueOf(xmxx.get(0).get("BDCDYH")).trim();
            if (!StringUtils.isEmpty(xmxx.get(0).get("CQRXM"))) {
                qlrmc.append(String.valueOf(xmxx.get(0).get("CQRXM")).trim());
                qlrmc.append(",");
            }
            if (fk != null && fk.size() > 0) {
                qlrmc.append(fk.get(0).get("JGMC"));
                qlrmc.append(",");
            }
            if (!StringUtils.isEmpty(xmxx.get(0).get("CQZH"))) {
                cqzh.append(String.valueOf(xmxx.get(0).get("CQZH")).trim());
                cqzh.append(",");
            }
            if (!StringUtils.isEmpty(xmxx.get(0).get("CQRZJHM"))) {
                zjh.append(String.valueOf(xmxx.get(0).get("CQRZJHM")).trim());
                zjh.append(",");
            }
            if (fk != null && fk.size() > 0) {
                zjh.append(fk.get(0).get("ZZJGDM"));
                zjh.append(",");
            }
            if (!StringUtils.isEmpty(xmxx.get(0).get("BDCDYLX")))
                bdcdylx = String.valueOf(xmxx.get(0).get("BDCDYLX")).trim();
            if (!StringUtils.isEmpty(xmxx.get(0).get("ZL")))
                share_zl = String.valueOf(xmxx.get(0).get("ZL")).trim();
            //增加业务类型------2017.03.29 包基杰
            if (!StringUtils.isEmpty(xmxx.get(0).get("YWLX")))
                ywlx = String.valueOf(xmxx.get(0).get("YWLX")).trim();
        }
        List<Map> holders = get_sharesearchData(shareHolderSql);
        for (Map h : holders) {
            if (!"2".equals(h.get("YGSQR"))) {
                if (!StringUtils.isEmpty(h.get("QLRMC"))) {
                    qlrmc.append(String.valueOf(h.get("QLRMC")).trim());
                    qlrmc.append(",");
                }
                if (!StringUtils.isEmpty(h.get("BDCQZH"))) {
                    cqzh.append(String.valueOf(h.get("BDCQZH")).trim());
                    cqzh.append(",");
                }
                if (!StringUtils.isEmpty(h.get("ZJH"))) {
                    zjh.append(String.valueOf(h.get("ZJH")).trim());
                    zjh.append(",");
                }
            }
        }
        Map bdcdyh_map = new HashMap();
        bdcdyh_map.put("ZL", share_zl);
        bdcdyh_map.put("BDCDYLX", bdcdylx);
        bdcdyh_map.put("QLRMC", trimLastChar(qlrmc));
        bdcdyh_map.put("ZJH", trimLastChar(zjh));
        bdcdyh_map.put("BDCDYH", bdcdyh);
        bdcdyh_map.put("BDCQZH", trimLastChar(cqzh));
        bdcdyh_map.put("YWH", ywh);
        bdcdyh_map.put("YWLX", ywlx);
        bdcdyh_map.put("CREATETIME", StringUtil.valueOf(xmxx.get(0).get("CREATETIME")));
        bdcdyh_map.put("CQCS", StringUtil.valueOf(xmxx.get(0).get("CQCS")));

        if (!StringUtils.isEmpty(bdcdyh)) {
            List<RealUnit> units = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, "BDCDYH='" + bdcdyh + "'");
            bdcdyh_map.put("BDCDYID", units != null && units.size() > 0 ? units.get(0).getId() : "");
        } else if (!StringUtils.isEmpty(trimLastChar(cqzh))) {
            String[] cqzhs = trimLastChar(cqzh).split(",");
            StringBuilder cqzhwhere = new StringBuilder("(");
            int i = 0;
            for (String c : cqzhs) {
                i++;
                cqzhwhere.append("zs.bdcqzh='" + c.trim() + "'");
                if (i < cqzhs.length)
                    cqzhwhere.append(" or ");

            }
            cqzhwhere.append(")");

            String bdcdyh2 = "", qlid = "";
            List<Map> xx = this.baseCommonDao.getDataListByFullSql("select qdzr.DJDYID,qdzr.QLID,qdzr.QLRID,qdzr.ZSID,qdzr.BDCDYH from bdck.bdcs_qdzr_xz qdzr left join bdck.bdcs_zs_xz  zs on qdzr.zsid=zs.zsid where 1=1 and " + cqzhwhere.toString());
            List<RealUnit> units = null;
            if (xx != null && xx.size() > 0) {
                bdcdyh2 = String.valueOf(xx.get(0).get("BDCDYH"));
                qlid = String.valueOf(xx.get(0).get("QLID"));

                units = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, "BDCDYH='" + bdcdyh2 + "'");
                bdcdyh_map.put("BDCDYID", units != null && units.size() > 0 ? units.get(0).getId() : "");
            }
        } else {
            bdcdyh_map.put("BDCDYID", "");
        }
        ResultMaps.add(bdcdyh_map);
        r.setRows(ResultMaps);
        r.setSuccess("true");
        r.setTotal(ResultMaps.size());
        return r;
    }

    private String trimLastChar(StringBuilder sb) {
        String resultstring = "";
        if (sb.toString().length() > 0)
            resultstring = sb.toString().substring(0, sb.toString().length() - 1);
        return resultstring;
    }

    private Unit_EX parseShareInfo(String ywh, HttpServletRequest request) {
        String mapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳州协同共享数据与登记系统数据映射关系.txt", request.getRealPath("/"));
        String beforetime = StringHelper.FormatYmdhmsByDate(new Date());

        //初始化工具类
        ConverterUtil util = new ConverterUtil(mapfilepath);
        List<Map> projects = get_sharesearchData("select xmbh, ywdl, ywlx, cqrxm, cqrzjlx, cqrzjhm, cqzh, fwzl, sqsj, slry, zt, htbh, dyfw, dyfs, to_char(ydrqq,'yyyy-MM-dd HH24:mi:ss') as ydrqq , to_char(ydrqz,'yyyy-MM-dd HH24:mi:ss') as ydrqz, dyjz, dyqr, zwr, dyqrzjlx, dyqrzjhm, zgzqse, jedw, dyqrdlr, description, remark, createtime, modifytime, slryid, blryid, xmzt, iconcls, dlrxm, dlrzjlx, dlrzjhm, dlrlxdh, cqrlxfs, cqrtxdz, cqrfrdb, cqrfrdh, cqrdlr, cqrdlrdh, cqrzjzl, cqrzjh, cqrdljgmc, cqrdljgyb, dypgjz, zgzqqdss, zsgs, sqrlx, sqrlb, gyfs, qlbl, cqrfrzjh, dz, qdjg ,dybdclx, czfs,sfpldy from sharesearch.t_project  where xmbh='" + ywh + "'");
        List<Map> houses = get_sharesearchData("select * from sharesearch.t_house where xmbh='" + ywh + "'");
        List<Map> holders = get_sharesearchData("select * from sharesearch.t_holder where xmbh='" + ywh + "'");
        String slry = projects.size() > 0 ? String.valueOf(projects.get(0).get("SLRYID")) : "-1";
        List<Map> depts = get_sharesearchData(" select dept.*,u.XM,u.SFZH,u.LXFS from sharesearch.T_PROJECT t join sharesearch.USERS u on u.id=t.slryid join sharesearch.dept dept on dept.jgbh=u.ssjgdm where t.slryid='" + slry + "'");

        //获取机构，房开填写远程报件时，房开的信息存放在dept中
        String shareDeptSql = "select dept.* from sharesearch.T_PROJECT t join sharesearch.USERS u on u.id=t.slryid join sharesearch.dept dept on dept.jgbh=u.ssjgdm where t.slryid = '" + projects.get(0).get("SLRYID") + "'";
        List<Map> fk = null;

        String jgdm = ywh.substring(7, 9);
        if ("11".equals(jgdm)) {
            fk = get_sharesearchData(shareDeptSql);
        }

        StringBuilder qlrmc = new StringBuilder();
        if (!StringUtils.isEmpty(projects.size() > 0 ? projects.get(0).get("CQRXM") : "")) {
            qlrmc.append(String.valueOf(projects.get(0).get("CQRXM")).trim());
            qlrmc.append(",");
        }
        if (fk != null && fk.size() > 0 && projects.get(0).get("CQRXM") == null) {
            qlrmc.append(fk.get(0).get("JGMC"));
            qlrmc.append(",");
        }

        for (Map h : holders) {
            //房开属于抵押人
            if (!"2".equals(h.get("YGSQR"))) {
                if (!StringUtils.isEmpty(h.get("QLRMC"))) {
                    qlrmc.append(String.valueOf(h.get("QLRMC")).trim());
                    qlrmc.append(",");
                }
            }
        }
        List<Map> allMap = new ArrayList<Map>();
        Map result = new HashMap();
        Map share = new HashMap();
        Map djxt = new HashMap();
        util.AddPrefix(projects, "sharesearch.t_project");
        util.AddPrefix(houses, "sharesearch.t_house");
        util.AddPrefix(holders, "sharesearch.t_holder");
        util.AddPrefix(depts, "sharesearch.dept");
        allMap.addAll(projects);
        allMap.addAll(houses);
        allMap.addAll(holders);
        allMap.addAll(depts);
        //共享库填的信息
        BDCS_H_XZ BDCS_H_XZ_share = util.CreateSingleClass(BDCS_H_XZ.class, allMap);
        BDCS_QL_XZ BDCS_QL_XZ_share = util.CreateSingleClass(BDCS_QL_XZ.class, allMap);
        BDCS_FSQL_XZ BDCS_FSQL_XZ_share = util.CreateSingleClass(BDCS_FSQL_XZ.class, allMap);
        BDCS_SQR sqr_share = util.CreateSingleClass(BDCS_SQR.class, allMap);
        BDCS_QLR_XZ syqlr_share = util.CreateSingleClass(BDCS_QLR_XZ.class, projects);//所有权人
        BDCS_QLR_XZ syqlr_sharefk = util.CreateSingleClass(BDCS_QLR_XZ.class, depts);//所有权人
        for (int i = 0; i < holders.size(); i++) {
            if ("2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
                holders.remove(i);
            }
        }
        List<BDCS_QLR_XZ> qlrs_share = util.CreateClass(BDCS_QLR_XZ.class, holders);//共有权人

        List<BDCS_QLR_XZ> share_qlrs = new ArrayList<BDCS_QLR_XZ>();
        String pldy = "";//批量
        if (!StringUtils.isEmpty(projects.size() > 0 ? projects.get(0).get("SHARESEARCH.T_PROJECT.CQRXM") : "") && !StringUtils.isEmpty(projects.size() > 0 ? projects.get(0).get("SHARESEARCH.T_PROJECT.CQRZJHM") : "")) {
            pldy = StringHelper.formatObject(projects.get(0).get("SHARESEARCH.T_PROJECT.SFPLDY"));
            if (!"1".equals(pldy)) {
                share_qlrs.add(syqlr_share);
            }
        } else {
            share_qlrs.add(syqlr_sharefk);
        }
        for (BDCS_QLR_XZ _qlr : qlrs_share) {
            share_qlrs.add(_qlr);
        }
        //共享的数据

        BDCS_FSQL_XZ_share.setDYR(trimLastChar(qlrmc));
        Unit_EX share_unit = new Unit_EX();
        Unit_EX.QL_EX share_ql = new Unit_EX().new QL_EX();
        share_ql.setQl(BDCS_QL_XZ_share);
        share_ql.setFsql(BDCS_FSQL_XZ_share);
        share_ql.setQlrs(share_qlrs);
        //如果是批量抵押，有批量单元，则在坐落那加上等**个
        if ("1".equals(pldy)) {
            BDCS_H_XZ_share.setZL(BDCS_H_XZ_share.getZL() + "等" + houses.size() + "处");
        }
        share_unit.setUnit(BDCS_H_XZ_share);
        List<QL_EX> share_qls = new ArrayList<QL_EX>();
        share_qls.add(share_ql);
        share_unit.setQls(share_qls);
        share_unit.setSqr(sqr_share);
        return share_unit;
    }

    @Override
    public Map acceptprojectdetails(HttpServletRequest request,
                                    HttpServletResponse response) throws UnsupportedEncodingException {
        String ywh = RequestHelper.getParam(request, "ywh");
        String bdcdyid = RequestHelper.getParam(request, "bdcdyid");
        String bdcdylx = RequestHelper.getParam(request, "bdcdylx");
        //共享数据
        Unit_EX share_unit = parseShareInfo(ywh, request);
        //预告登记与抵押合并登记的时候，银行方不能拿到不动产单元号,则通过坐落查找单元号
        List<Map> houses = get_sharesearchData("select * from sharesearch.t_house  where xmbh='" + ywh + "'");
        if (houses.get(0).get("ZL") != null && !"".equals(houses.get(0).get("ZL")) && houses.get(0).get("BDCDYH") == null) {
            String findBdcdyh = "";
            //截取坐落最后一位"号"字进行模糊搜索
            String zl = (String) houses.get(0).get("ZL");
            if (zl.endsWith("号")) {
                zl = zl.substring(0, zl.length() - 1);
            }

            if ("032".equals(String.valueOf(houses.get(0).get("BDCDYLX")))) {
                findBdcdyh = "select * from bdcdck.bdcs_h_gzy where zl like '" + zl + "%'";

            } else {
                findBdcdyh = "select * from bdcdck.bdcs_h_xzy where zl like '" + zl + "%'";
            }
            List<Map> id = this.baseCommonDao.getDataListByFullSql(findBdcdyh);
            if (id.size() > 0 && (bdcdyid == null || "".equals(bdcdyid))) {
                bdcdyid = (String) id.get(0).get("BDCDYID");
            } else if (id.size() == 0 && (bdcdyid == null || "".equals(bdcdyid))) {
                findBdcdyh = "select * from bdck.bdcs_h_xz where zl like '" + zl + "%'";
                List<Map> bdcdy = this.baseCommonDao.getDataListByFullSql(findBdcdyh);
                if (bdcdy.size() > 0) {
                    bdcdyid = (String) bdcdy.get(0).get("BDCDYID");
//				 bdcdyh = (String)bdcdy.get(0).get("BDCDYH");
                }
            }
        } else if (houses.size() > 1) {
            //批量的业务，单元显示一个好了，其他用等表示，页面也挑一个出来对比就行
            List<Map> bdcqzhids = baseCommonDao.getDataListByFullSql("select ql.bdcdyid from bdck.BDCS_QL_XZ ql where ql.bdcdyh='" + share_unit.getUnit().getBDCDYH() + "'");
            if (bdcqzhids.size() > 0) {
                bdcdyid = StringUtil.valueOf(bdcqzhids.get(0).get("BDCDYID"));
            }
        }

        //登记库填的信息
        Unit_EX djxt_unit = new Unit_EX();
        List<QL_EX> djdy_qls = new ArrayList<QL_EX>();
        //如果bdcdyid为空 随机给个只
        if (StringUtils.isEmpty(bdcdyid))
            bdcdyid = UUID.randomUUID().toString();
        //RealUnit U=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),DJDYLY.XZ,bdcdyid);
        if (!StringUtils.isEmpty(bdcdyid) && !bdcdyid.toLowerCase().equals("null")) {
            //---------2017.03.28 根据不动产单元类型（期房/现房）从不同的表中取数据 包基杰
            if ("031".equals(bdcdylx)) {
                BDCS_H_XZ BDCS_H_XZ_djxt = new BDCS_H_XZ();
                BDCS_H_XZ_djxt = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
                List<BDCS_DJDY_XZ> bdcs_djdy_xzs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "'");
                if (bdcs_djdy_xzs != null && bdcs_djdy_xzs.size() > 0) {

                    List<BDCS_QL_XZ> syqs = baseCommonDao.getDataList(BDCS_QL_XZ.class, "DJDYID='" + bdcs_djdy_xzs.get(0).getDJDYID() + "' AND QLLX in(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18)");
                    for (BDCS_QL_XZ syq : syqs) {
                        Unit_EX.QL_EX djdy_ql = new Unit_EX().new QL_EX();
                        djdy_ql.setQl(syq);
                        List<BDCS_FSQL_XZ> fsql_dyqs = baseCommonDao.getDataList(BDCS_FSQL_XZ.class, "QLID='" + syq.getId() + "'");
                        if (fsql_dyqs != null && fsql_dyqs.size() > 0)
                            djdy_ql.setFsql(fsql_dyqs.get(0));
                        List<BDCS_QLR_XZ> dyqr = baseCommonDao.getDataList(BDCS_QLR_XZ.class, "QLID='" + syq.getId() + "' order by qlrmc,zjh");
                        djdy_ql.setQlrs(dyqr);
                        djdy_qls.add(djdy_ql);
                        break;//只获取一条所有权信息
                    }

                }
                djxt_unit.setQls(djdy_qls);
                if (BDCS_H_XZ_djxt == null)
                    BDCS_H_XZ_djxt = new BDCS_H_XZ();
                djxt_unit.setUnit(BDCS_H_XZ_djxt);
            } else if ("032".equals(bdcdylx)) {
                BDCS_H_XZY BDCS_H_XZY_djxt = new BDCS_H_XZY();
                BDCS_H_XZY_djxt = baseCommonDao.get(BDCS_H_XZY.class, bdcdyid);
                List<BDCS_DJDY_XZ> bdcs_djdy_xzs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "'");
                if (bdcs_djdy_xzs != null && bdcs_djdy_xzs.size() > 0) {

                    List<BDCS_QL_XZ> syqs = baseCommonDao.getDataList(BDCS_QL_XZ.class, "DJDYID='" + bdcs_djdy_xzs.get(0).getDJDYID() + "' AND QLLX in(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18)");
                    for (BDCS_QL_XZ syq : syqs) {
                        Unit_EX.QL_EX djdy_ql = new Unit_EX().new QL_EX();
                        djdy_ql.setQl(syq);
                        List<BDCS_FSQL_XZ> fsql_dyqs = baseCommonDao.getDataList(BDCS_FSQL_XZ.class, "QLID='" + syq.getId() + "'");
                        if (fsql_dyqs != null && fsql_dyqs.size() > 0)
                            djdy_ql.setFsql(fsql_dyqs.get(0));
                        List<BDCS_QLR_XZ> dyqr = baseCommonDao.getDataList(BDCS_QLR_XZ.class, "QLID='" + syq.getId() + "' order by qlrmc,zjh");
                        djdy_ql.setQlrs(dyqr);
                        djdy_qls.add(djdy_ql);
                        break;//只获取一条所有权信息
                    }

                }

                djxt_unit.setQls(djdy_qls);
                if (BDCS_H_XZY_djxt == null)
                    BDCS_H_XZY_djxt = new BDCS_H_XZY();
                djxt_unit.setUnit(BDCS_H_XZY_djxt);
            }
        }
        String compareField = "BDCS_H_XZ.bdcdyh,BDCS_H_XZ.fh,BDCS_H_XZ.fwyt1,BDCS_H_XZ.zl,BDCS_H_XZ.szc,BDCS_H_XZ.SCJZMJ,BDCS_H_XZ.DYTDMJ,BDCS_H_XZ.qdjg,BDCS_H_XZ.bdcqzh,BDCS_H_XZ.bdcqzh,BDCS_H_XZ.ghyt,";
        compareField += "BDCS_QL_XZ.BDCQZH,BDCS_QL_XZ.QDJG,BDCS_QL_XZ.QDJG,BDCS_QL_XZ.qlqssj,BDCS_QL_XZ.qljssj,";
        compareField += "BDCS_FSQL_XZ.DYR,BDCS_FSQL_XZ.DYFS,BDCS_FSQL_XZ.dybdclx,BDCS_FSQL_XZ.bdbzzqse,BDCS_FSQL_XZ.zjjzwdyfw,";
        compareField += "BDCS_QLR_XZ.QLRMC,BDCS_QLR_XZ.ZJZL,BDCS_QLR_XZ.XB,BDCS_QLR_XZ.ZJH,BDCS_QLR_XZ.GYFS,BDCS_QLR_XZ.QLMJ,BDCS_QLR_XZ.QLRLX";
        Map r = CompareData(share_unit, djxt_unit, compareField);
        r.put("bdcdylx", bdcdylx);//将不动产单元类型传回前端，用于判断建筑面积为实测或预测，预抵押都为预测---20170401包基杰
        r.put("jgdm", ywh.substring(7, 9));
        return r;
    }

    /**
     * @param obj          UNIT_Ex对象
     * @param isshare      是否共享库
     * @param compareField 对比的字段 格式：BDCS_QL_XZ.bdcdyh,BDCS_QL_XZ.bdcqzh
     * @return 转换后的Map对象
     */
    private Map ObjectConverToMap(Unit_EX obj, boolean isshare, String compareField) {
        compareField = compareField.toLowerCase();
        String pre_compare = "compare_", pre_unit = "unit_", pre_ql = "ql_", pre_fsql = "fsql_", pre_qlr = "qlr_", pre_database;
        if (isshare)
            pre_database = "share_";
        else
            pre_database = "djxt_";
        if (obj == null) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RealUnit unit = obj.getUnit();
            BeanInfo beanInfo = Introspector.getBeanInfo(unit.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class") && !key.equals("ignoreproperties")) {
                    Object value = null;
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    //遇到getter方法为null的情况,请检查key对应getter方法
                    if (null == getter || "".equals(getter)) {

                    } else {
                        value = getter.invoke(unit);
                    }
                    key = key.toLowerCase();
                    value = paravalue(value, key);
                    map.put(pre_database + pre_unit + key, value);
                    //需要对比的字段
                    if (compareField.contains("bdcs_h_xz." + key)) {
                        map.put(pre_compare + pre_database + pre_unit + key, value);
                    }
                }

            }
            List<QL_EX> qls = obj.getQls();
            map.put(pre_database + pre_ql + "length", qls.size());
            for (int i = 0; i < qls.size(); i++) {
                BeanInfo beanInfo_ql = Introspector.getBeanInfo(qls.get(i).getQl().getClass());
                BeanInfo beanInfo_fsql = Introspector.getBeanInfo(qls.get(i).getFsql().getClass());
                List<BDCS_QLR_XZ> qlrs = qls.get(i).getQlrs();
                PropertyDescriptor[] propertyDescriptors_ql = beanInfo_ql.getPropertyDescriptors();
                for (PropertyDescriptor property : propertyDescriptors_ql) {
                    String key = property.getName();

                    // 过滤class属性
                    if (!key.equals("class") && !key.equals("ignoreproperties")) {
                        // 得到property对应的getter方法
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(qls.get(i).getQl());
                        key = key.toLowerCase();
                        value = paravalue(value, key);
                        map.put(pre_database + pre_ql + key + "_" + i, value);
                        //需要对比的字段
                        if (compareField.contains("bdcs_ql_xz." + key))
                            map.put(pre_compare + pre_database + pre_ql + key + "_" + i, value);
                    }

                }
                PropertyDescriptor[] propertyDescriptors_fsql = beanInfo_fsql.getPropertyDescriptors();
                for (PropertyDescriptor property : propertyDescriptors_fsql) {
                    String key = property.getName();

                    // 过滤class属性
                    if (!key.equals("class") && !key.equals("ignoreproperties")) {
                        // 得到property对应的getter方法
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(qls.get(i).getFsql());
                        key = key.toLowerCase();
                        value = paravalue(value, key);
                        map.put(pre_database + pre_fsql + key + "_" + i, value);
                        //需要对比的字段
                        if (compareField.contains("bdcs_fsql_xz." + key))
                            map.put(pre_compare + pre_database + pre_fsql + key + "_" + i, value);
                    }

                }
                map.put(pre_database + pre_qlr + "length", qlrs.size());
                for (int j = 0; j < qlrs.size(); j++) {
                    BeanInfo beanInfo_qlr = Introspector.getBeanInfo(qlrs.get(j).getClass());
                    PropertyDescriptor[] propertyDescriptors_qlr = beanInfo_qlr.getPropertyDescriptors();
                    for (PropertyDescriptor property : propertyDescriptors_qlr) {
                        String key = property.getName();

                        // 过滤class属性
                        if (!key.equals("class")) {
                            // 得到property对应的getter方法
                            Method getter = property.getReadMethod();
                            Object value = getter.invoke(qlrs.get(j));
                            key = key.toLowerCase();
                            value = paravalue(value, key);
                            map.put(pre_database + pre_qlr + key + "_" + i + "_" + j, value);
                            //需要对比的字段
                            if (compareField.contains("bdcs_qlr_xz." + key))
                                map.put(pre_compare + pre_database + pre_qlr + key + "_" + i + "_" + j, value);
                        }

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("对象转换为MAP失败： " + e);
        }
        return map;
    }

    private Object paravalue(Object value, String key) {
        if (value instanceof Date)
            return StringHelper.FormatDateOnType(value, "yyyy-MM-dd");
        if (key.equals("dybdclx"))
            return ConstHelper.getNameByValue("DYBDCLX", String.valueOf(value));
        if (key.equals("dyfs"))
            return ConstHelper.getNameByValue("DYFS", String.valueOf(value));
        if (key.equals("ghyt"))
            return ConstHelper.getNameByValue("FWYT", String.valueOf(value));
        if (key.equals("fwlx"))
            return ConstHelper.getNameByValue("FWLX", String.valueOf(value));
        if (key.equals("fwxz"))
            return ConstHelper.getNameByValue("FWXZ", String.valueOf(value));
        if (key.equals("fwyt1"))
            return ConstHelper.getNameByValue("FWYT", String.valueOf(value));
        if (key.equals("fwyt2"))
            return ConstHelper.getNameByValue("FWYT", String.valueOf(value));
        if (key.equals("fwyt3"))
            return ConstHelper.getNameByValue("FWYT", String.valueOf(value));
        if (key.equals("qlrlx"))
            return ConstHelper.getNameByValue("QLRLX", String.valueOf(value));
        if (key.equals("gyfs"))
            return ConstHelper.getNameByValue("GYFS", String.valueOf(value));
        if (key.equals("dlrzjlx"))
            return ConstHelper.getNameByValue("ZJLX", String.valueOf(value));
        if (key.equals("fddbrzjlx"))
            return ConstHelper.getNameByValue("ZJLX", String.valueOf(value));
        if (key.equals("xb"))
            return ConstHelper.getNameByValue("XB", String.valueOf(value));
        if (key.equals("zjzl"))
            return ConstHelper.getNameByValue("ZJLX", String.valueOf(value));
        if (key.equals("hx")) {
            return ConstHelper.getNameByValue("HX", String.valueOf(value));
        }
        if (key.equals("hxjg")) {
            return ConstHelper.getNameByValue("HXJG", String.valueOf(value));
        }
        if (key.equals("mjdw")) {
            return ConstHelper.getNameByValue("MJDW", String.valueOf(value));
        }
        return value;
    }

    private Map CompareMap(Map sharemap, Map djxtmap) {
        Map r = new HashMap();
        for (Object key : sharemap.keySet()) {
            if (key.toString().contains("compare_")) {
                //如果对比中有的 不用对比
                if (r.containsKey(key.toString()))
                    continue;
                String djxtkey = key.toString().replace("share_", "djxt_");
                if (djxtmap.containsKey(djxtkey)) {
                    if (sharemap.get(key) != null && djxtmap.get(djxtkey) != null && !sharemap.get(key).equals(djxtmap.get(djxtkey))) {
                        //sharemap.put(key, "$");
                        // djxtmap.put(djxtkey, "$");
                        r.put(key, "1");
                        r.put(djxtkey, "1");
                    } else if (StringUtils.isEmpty(sharemap.get(key)) || StringUtils.isEmpty(djxtmap.get(djxtkey))) {
                        r.put(key, "1");
                        r.put(djxtkey, "1");
                    }

                } else {
                    // sharemap.put(key, "$");
                    r.put(key, "1");
                }
            }
        }
        for (Object key : djxtmap.keySet()) {
            if (key.toString().contains("compare_")) {
                //如果对比中有的 不用对比
                if (r.containsKey(key.toString()))
                    continue;
                String sharekey = key.toString().replace("djxt_", "share_");
                if (sharemap.containsKey(sharekey)) {
                    if (sharemap.get(sharekey) != null && djxtmap.get(key) != null && !sharemap.get(sharekey).equals(djxtmap.get(key))) {
                        //djxtmap.put(key, "$");
                        // sharemap.put(sharekey, "$");
                        r.put(key, "1");
                        r.put(sharekey, "1");
                    } else if (StringUtils.isEmpty(sharemap.get(sharekey)) || StringUtils.isEmpty(djxtmap.get(key))) {
                        r.put(key, "1");
                        r.put(sharekey, "1");
                    }
                } else {
                    // djxtmap.put(key, "1");
                    r.put(key, "1");
                }
            }
        }
        return r;
    }

    private Map CompareData(Unit_EX share, Unit_EX djxt, String compareField) {
        Map result = new HashMap();
        Map shareMap = ObjectConverToMap(share, true, compareField);
        Map djxtMap = ObjectConverToMap(djxt, false, compareField);
        Map compareresult = CompareMap(shareMap, djxtMap);
        for (Object key : shareMap.keySet()) {
            if (!key.toString().contains("compare_"))
                result.put(key, shareMap.get(key));
        }
        for (Object key : djxtMap.keySet()) {
            if (!key.toString().contains("compare_"))
                result.put(key, djxtMap.get(key));
        }
        result.putAll(compareresult);
        return result;
    }

    @Override
    @Transactional
    public Message acceptproject(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Message r = new Message();
        String ywh = RequestHelper.getParam(request, "ywh");
        String jgdm = ywh.substring(7, 9);//通过机构代码判断该项目是房开填写还是银行填写
        String bdcdyid = RequestHelper.getParam(request, "bdcdyid");
        String bdcdylx = RequestHelper.getParam(request, "bdcdylx");
        List<Map> projects = get_sharesearchData("select * from sharesearch.t_project  where xmbh='" + ywh + "'");
        List<Map> houses = get_sharesearchData("select * from sharesearch.t_house  where xmbh='" + ywh + "'");
        //预告登记与抵押合并登记的时候，银行方不能拿到不动产单元号,则通过坐落查找单元号
        String bdcdyh = StringHelper.isEmpty(houses.get(0).get("BDCDYH"))?"":houses.get(0).get("BDCDYH").toString();
        if ((!StringHelper.isEmpty(houses.get(0).get("ZL")))&&(StringHelper.isEmpty(houses.get(0).get("BDCDYH"))||StringHelper.isEmpty(bdcdyid))) {
            String findBdcdyh = "";
            //截取坐落最后一位"号"字进行模糊搜索
            String zl = (String) houses.get(0).get("ZL");
            if (zl.endsWith("号")) {
                zl = zl.substring(0, zl.length() - 1);
            }

            if ("032".equals(String.valueOf(houses.get(0).get("BDCDYLX")))) {
                findBdcdyh = "select * from bdcdck.bdcs_h_gzy where zl like '" + zl + "%'";
            } else {
                findBdcdyh = "select * from bdcdck.bdcs_h_xzy where zl like '" + zl + "%'";
            }
            List<Map> id = this.baseCommonDao.getDataListByFullSql(findBdcdyh);
            if (id.size() > 0 && (bdcdyid == null || "".equals(bdcdyid))) {
                bdcdyid = (String) id.get(0).get("BDCDYID");
                bdcdyh = (String) id.get(0).get("BDCDYH");
            } else if (id.size() == 0 && (bdcdyid == null || "".equals(bdcdyid))) {
                findBdcdyh = "select * from bdck.bdcs_h_xz where zl like '" + zl + "%'";
                List<Map> bdcdy = this.baseCommonDao.getDataListByFullSql(findBdcdyh);
                if (bdcdy.size() > 0) {
                    bdcdyid = (String) bdcdy.get(0).get("BDCDYID");
                    bdcdyh = (String) bdcdy.get(0).get("BDCDYH");
                }
            }
        }
        String mapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳州协同共享数据与登记系统数据映射关系.txt", request.getRealPath("/"));
        List<Map> holders = get_sharesearchData("select * from sharesearch.t_holder where xmbh='" + ywh + "'");

        ConverterUtil util = new ConverterUtil(mapfilepath);
        /*************强行插入，这里搞批量业务，搞完直接返回结果****************************/

        if (projects.size() > 0 && "1".equals(StringHelper.formatObject(projects.get(0).get("SFPLDY")))) {
            Message result = acceptProject_batch(ywh,projects,util,houses,holders,request,response);
            return result;
        }

        /***********************************************************************************/

        String slry = projects.size() > 0 ? String.valueOf(projects.get(0).get("SLRYID")) : "-1";
        List<Map> depts = get_sharesearchData(" select dept.*,u.XM,u.SFZH,u.LXFS from sharesearch.T_PROJECT t join sharesearch.USERS u on u.id=t.slryid join sharesearch.dept dept on dept.jgbh=u.ssjgdm where t.slryid='" + slry + "'");

        if (projects.size() == 0)
            return new Message();
        //将上面查询分为两部分，先从sharesearch查出业务类型，再根据业务类型获取模板
        List<Map> cons = get_sharesearchData("SELECT BZ,CONSTTRANS FROM SHARESEARCH.SYS_CONST WHERE CONSTVALUE='" + projects.get(0).get("YWLX") + "'");
        List<Map> wfd_prodef = this.baseCommonDao.getDataListByFullSql("SELECT PRODEF_ID, PRODEFCLASS_ID FROM BDC_WORKFLOW.WFD_PRODEF WHERE PRODEF_ID= '" + cons.get(0).get("BZ") + "' AND PRODEF_ID IS NOT NULL");
        //再将结果拼接给sys
        List<Map> sys = new ArrayList<Map>();
        //获取当前业务流程
        List<Map> workflows = null;
        String ywdh = "";
        if (wfd_prodef.size() > 0) {
            workflows = this.baseCommonDao.getDataListByFullSql("select b.workflowname,b.workflowcaption  FROM BDC_WORKFLOW.WFD_PRODEF a  left join bdc_workflow.wfd_mapping b on a.prodef_code=b.workflowcode where  a.prodef_id='" + wfd_prodef.get(0).get("PRODEF_ID") + "'");
            if (workflows != null && workflows.size() > 0) {
                ywdh = (String) workflows.get(0).get("WORKFLOWNAME");
                System.out.println("当前流程是：" + workflows.get(0).get("WORKFLOWCAPTION"));
            }

            cons.get(0).put("PRODEF_ID", wfd_prodef.get(0).get("PRODEF_ID"));
            cons.get(0).put("PRODEFCLASS_ID", wfd_prodef.get(0).get("PRODEFCLASS_ID"));
            sys.addAll(cons);
        }
        if (sys.size() == 0)
            return new Message();
        if (StringUtils.isEmpty(sys.get(0).get("BZ")))
            throw new Exception("无匹配流程定义。");
        SmProInfo proinfo = new SmProInfo();
        SmObjInfo smObjInfo = new SmObjInfo();
        if (Global.getCurrentUserInfo() == null)
            throw new Exception("请重新登陆。");
        smObjInfo.setID(Global.getCurrentUserInfo().getId());
        smObjInfo.setName(Global.getCurrentUserInfo().getUserName());
        List<Object> staffList = new ArrayList<Object>();
        staffList.add(smObjInfo);
        proinfo.setFile_Urgency("1");
        proinfo.setAcceptor(smObjInfo.getName());
        proinfo.setProDef_ID(sys.get(0).get("BZ").toString());//个人房产抵押
        Stack<String> defname = new Stack<String>();
        defname.push(String.valueOf(sys.get(0).get("CONSTTRANS")));
        StringBuilder def_name_sb = new StringBuilder();
        StringBuilder dyr = new StringBuilder();
        String def_name_string = "";
        getWorkflowDefName(defname, String.valueOf(sys.get(0).get("PRODEFCLASS_ID")));
        while (!defname.isEmpty()) {
            String v = defname.pop();
            def_name_sb.append(v + ",");
        }
        if (def_name_sb.toString().length() > 0)
            def_name_string = def_name_sb.toString().substring(0, def_name_sb.toString().length() - 1);
        for (int i = 0; i < projects.size(); i++) {
            dyr.append(projects.get(0).get("CQRXM") == null ? depts.get(0).get("JGMC") : projects.get(0).get("CQRXM"));
            if (i < projects.size() - 1)
                dyr.append(",");
        }
        if (holders.size() > 0)
            dyr.append(",");
        for (int i = 0; i < holders.size(); i++) {
            if (!"2".equals(holders.get(i).get("YGSQR"))) {//
                dyr.append(holders.get(i).get("QLRMC"));
                if (i < holders.size() - 1)
                    dyr.append(",");
            }
        }
        //把后面的多余，去掉
        String dyrstr = dyr.toString();
        if (dyr.toString().endsWith(",")) dyrstr = dyr.toString().substring(0, dyr.toString().length() - 1);
        proinfo.setProDef_Name(def_name_string);
//	proinfo.setProInst_Name(dyr.toString()+"  "+String.valueOf(projects.get(0).get("FWZL")));
        //改成把t_house中的坐落放到项目名称中+"  "+String.valueOf(projects.get(0).get("FWZL"))
        String xzqh = ConfigHelper.getNameByValue("XZQHDM");
        //贺州只要权利人作为项目名称
        if ("451102".equals(xzqh)) {
            proinfo.setProInst_Name(dyrstr);
        } else {
            proinfo.setProInst_Name(dyrstr + "  " + String.valueOf(houses.get(0).get("ZL") == null ? "" : houses.get(0).get("ZL")));
        }
        //流程受理项目接口
        Object returnsmObjInfo = new workflowAPI().Accept(proinfo, staffList);
        SmObjInfo sm = (SmObjInfo) returnsmObjInfo;
        if (!StringUtils.isEmpty(sm.getFile_number())) {
            projectAPI projectapi = new projectAPI();
            //获取项目信息接口
            Object projectinfo = new projectAPI().getProjectInfo(sm.getFile_number(), request);
            ProjectInfo p = (ProjectInfo) projectinfo;
            String newxmbh = p.getXmbh().substring(0, p.getXmbh().length() - 5) + "@" + ywh;
            //桂林地区受理时，workflowAPI().Accept环节时xmxx.ywlsh丢失
            List<Map> m=  baseCommonDao.getDataListByFullSql("select prolsh from BDC_workflow.Wfi_Proinst p where p.file_number='" +sm.getFile_number()+ "' ");
            //更新项目编号，该项目编号包含了银行的业务号
            baseCommonDao.excuteQueryNoResult("update bdck.bdcs_xmxx set xmbh='" + newxmbh + "', ywlsh='"+m.get(0).get("PROLSH")+"' where xmbh='" + p.getXmbh() + "'");
            p.setXmbh(newxmbh);
            //桂林远程受理，只取最后一次受理保存到查询共享库
            JH_DBHelper.excuteQuery(JH_DBHelper.getConnect_sharesearch(), "update SHARESEARCH.T_PROJECT sh set sh.PROINST_ID='" + newxmbh + "'where sh.XMBH='" + ywh + "'");
            //添加单元信息接口
            Object resultMessage = null ;
            if(ywdh!=null && ywdh.contains("ZX")) {
                //抵押权注销权利是通过权利id选择单元的,银行信息存在holder中，从中取到权证号
                String bdcqzh = "";
                if(holders.size()>0) {
                    bdcqzh = StringHelper.formatObject(holders.get(0).get("BDCQZH"));
                }

                List<Map> zxqlids = baseCommonDao.getDataListByFullSql("select t.qlid from bdck.bdcs_ql_xz t where t.bdcqzh='"+bdcqzh+"' ");
                if(zxqlids.size()>0) {
                    resultMessage = projectapi.addBDCDY(p.getXmbh(), StringHelper.formatObject(zxqlids.get(0).get("QLID")));
                } else {
                    resultMessage = projectapi.addBDCDY(p.getXmbh(), bdcdyid);
                }
            } else {
                resultMessage = projectapi.addBDCDY(p.getXmbh(), bdcdyid);
            }
            ResultMessage re = (ResultMessage) resultMessage;
            //添加申请人信息接口
            //if(r.getSuccess().equals("true"))
            //{
            util.AddPrefix(holders, "sharesearch.t_holder");
            util.AddPrefix(projects, "SHARESEARCH.T_PROJECT");
            util.AddPrefix(depts, "SHARESEARCH.dept");
            //生成申请人集合
            List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
            List<String> dyqrids = new ArrayList<String>();

            if ("YG002".equals(ywdh)) {//------------预告登记+预抵押合并登记
                BDCS_SQR sqrFromProjects = util.CreateSingleClass(BDCS_SQR.class, projects);
                if (sqrFromProjects != null) {
                    //受理抵押所有权人为甲方
                    sqrFromProjects.setSQRLB(sqrFromProjects.getSQRLB());
                    sqrs.add(sqrFromProjects);
                }
                //从共有人表生成申请人
                List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
                if (sqrsFromholders != null) {
                    for (BDCS_SQR sqr : sqrsFromholders) {
                    	if("10".equals(sqr.getSQRLB())){
                    		sqr.setSQRLB(ConstValue.SQRLB.JF.Value);
                    		dyqrids.add(sqr.getId());
                    		sqrs.add(sqr);
                    	}else{
                    		//受理抵押所有权人为甲方
                    		sqr.setSQRLB(sqr.getSQRLB());
                    		sqrs.add(sqr);
                    	}
                    }
                }

//                BDCS_SQR sqrsFromDept = util.CreateSingleClass(BDCS_SQR.class, depts);
//
//
//                if (sqrsFromDept != null) {
//                    if ("11".equals(jgdm)) {
//                        sqrsFromDept.setSQRLB(ConstValue.SQRLB.YF.Value);
//                        sqrs.add(sqrsFromDept);
//                    } else {
//                        sqrsFromDept.setSQRLB(ConstValue.SQRLB.JF.Value);
//                        sqrs.add(sqrsFromDept);
//                    }
//                }

            } else if ("YG001".equals(ywdh)) {    //-------------预告登记,开发商和业主,不抽银行
            	List<BDCS_SQR> sqrFromProjects = util.CreateClass(BDCS_SQR.class, projects);
            	  for (BDCS_SQR sqr : sqrFromProjects) {
	                if (sqrFromProjects != null) {
	                    sqrs.add( sqr);
	                }
            	  }
                //从共有人表生成申请人
                List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
                if (sqrsFromholders != null) {
                    for (BDCS_SQR sqr : sqrsFromholders) {
                    	if("10".equals(sqr.getSQRLB())){
                    		//预告登记，银行不抽取过去，但是房开需要抽取
                    	}else{
                    		//受理抵押所有权人为甲方
//                    		sqr.setSQRLB(sqr.getSQRLB());
                    		sqrs.add(sqr);
                    	}
                    }
                }

//                //预告登记，银行不抽取过去，但是房开需要抽取
//                if ("11".equals(jgdm)) {
//                    for (int i = 0; i < holders.size(); i++) {
//                        if ("2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
//                            holders.remove(i);
//                        }
//                    }
//                }

//                //从共有人表生成申请人
//                List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
//                if (sqrsFromholders != null) {
//                    for (BDCS_SQR sqr : sqrsFromholders) {
//                        //受理抵押所有权人为甲方
//                        sqr.setSQRLB(sqr.getSQRLB());
//                        sqrs.add(sqr);
//                    }
//                }

//                BDCS_SQR sqrsFromDept = util.CreateSingleClass(BDCS_SQR.class, depts);
//
//                if (sqrsFromDept != null) {
//                    if ("11".equals(jgdm)) {
//                        sqrsFromDept.setSQRLB(ConstValue.SQRLB.YF.Value);
//                        sqrs.add(sqrsFromDept);
//                    }
//                }

            } else if ("YG003".equals(ywdh)) {//--------------预购商品房抵押权预告登记(一般为银行和个人办理(权利人)),排除开发商(义务人)
//                if ("450300".equals(xzqh) || "451102".equals(xzqh) || "450500".equals(xzqh)) {
                    //从共有人表生成申请人
                    List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
                    if (sqrsFromholders != null) {
                        for (BDCS_SQR sqr : sqrsFromholders) {
                        	if("2".equals(sqr.getSQRLB())){
                        	}else{
                        		sqrs.add(sqr);
                        	}
                        }
                    }
                	List<BDCS_SQR> sqrFromProjects = util.CreateClass(BDCS_SQR.class, projects);
	              	  for (BDCS_SQR sqr : sqrFromProjects) {
	              		if("2".equals(sqr.getSQRLB())){
                    	}else{
                    		sqrs.add(sqr);
                    	}
	              	 }
//                } else {
//                    BDCS_SQR sqrsFromDept = util.CreateSingleClass(BDCS_SQR.class, depts);
//
//                    if (sqrsFromDept != null) {
//                        if ("11".equals(jgdm) || "12".equals(jgdm)) {
//
//                            //只抽取银行
//                            for (int i = 0; i < holders.size(); i++) {
//                                if (!"2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
//                                    holders.remove(i);
//                                }
//                            }
//                            //从共有人表生成申请人
//                            List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
//                            if (sqrsFromholders != null) {
//                                for (BDCS_SQR sqr : sqrsFromholders) {
//                                    //受理抵押所有权人为甲方
//                                    sqr.setSQRLB(sqr.getSQRLB());
//                                    sqrs.add(sqr);
//                                }
//                            }
//
//                        } else {
//                            sqrsFromDept.setSQRLB(ConstValue.SQRLB.JF.Value);
//                            sqrs.add(sqrsFromDept);
//                        }
//                    }
//                }
            } else if ("ZY002".equals(ywdh)) {//---------------转移登记

                if (projects.get(0).get("SHARESEARCH.T_PROJECT.CQRXM") != null && !"12".equals(jgdm)) {//如果所有权人为空，则房开自己就是所有权人,个人办理时，不抽取所有权人
                    BDCS_SQR sqrFromProjects = util.CreateSingleClass(BDCS_SQR.class, projects);
                    if (sqrFromProjects != null) {
                        sqrFromProjects.setSQRLB(sqrFromProjects.getSQRLB());
                        sqrs.add(sqrFromProjects);
                    }

                }

                //房开个人填写，没有银行，申请人是买方，抽取过去
                if ("11".equals(jgdm) || "12".equals(jgdm)) {
                    for (int i = 0; i < holders.size(); i++) {
                        if (!"2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
                            holders.remove(i);
                        }
                    }
                }

                //从共有人表生成申请人
                List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
                if (sqrsFromholders != null) {
                    for (BDCS_SQR sqr : sqrsFromholders) {
                        //受理抵押所有权人为甲方
                        sqr.setSQRLB(sqr.getSQRLB());
                        sqrs.add(sqr);
                    }
                }


            } else if ("ZX004".equals(ywdh)) {//------------------注销登记（解押）,无需抽取权利人
                //解押只需展示填写内容，受理无需抽取数据


            } else if ("CS011".equals(ywdh) || "CS034".equals(ywdh)) {
//                if ("450300".equals(xzqh) || "451102".equals(xzqh) || "450500".equals(xzqh)) {
                    //只抽取银行(权利人),义务人登记系统已拥有
                    List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
                    if (sqrsFromholders != null) {
                        for (BDCS_SQR sqr : sqrsFromholders) {
                        	if("10".equals(sqr.getSQRLB())){
                        		sqr.setSQRLB("1");
                        		dyqrids.add(sqr.getId());
                        		sqrs.add(sqr);
                        		continue;
                        	}
                        }
                    }
//                }

            } else {//------------其他业务，现房的不需要抽取权利人过去，只需要抽取银行，并且银行是抵押权人

                BDCS_SQR sqrsFromDept = util.CreateSingleClass(BDCS_SQR.class, depts);

                if (sqrsFromDept != null) {
                    if ("11".equals(jgdm) || "12".equals(jgdm)) {

                        for (int i = 0; i < holders.size(); i++) {//把共有人去掉，保留银行
                            if (!"2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
                                holders.remove(i);
                            }
                        }

                        List<BDCS_SQR> sqrsFromholders = util.CreateClass(BDCS_SQR.class, holders);
                        if (sqrsFromholders != null) {
                            for (BDCS_SQR sqr : sqrsFromholders) {

                                sqr.setSQRLB(sqr.getSQRLB());
                                sqrs.add(sqr);
                            }
                        }
                    } else {

                        sqrsFromDept.setSQRLB(ConstValue.SQRLB.JF.Value);
                        sqrs.add(sqrsFromDept);
                    }
                }
            }

            //将抽取的申请人抽取过来

            for (BDCS_SQR s : sqrs) {
                s.setXMBH(p.getXmbh());
                projectapi.addSQRXX(s);
            }


            //由于出现权利表中单元号比正式单元号少一位的情况，故截掉最后一位，采用模糊搜索方式
            if ("".equals(bdcdyh) && !"".equals(bdcdyid)) {
                List<Map> bdcdyhs = baseCommonDao.getDataListByFullSql("select bdcdyh from bdcdck.bdcs_h_gzy where bdcdyid='" + bdcdyid + "'");
                if (bdcdyhs.size() > 0) {
                    bdcdyh = (String) bdcdyhs.get(0).get("BDCDYH");

                } else {
                    List<Map> djdybdcdyhs = baseCommonDao.getDataListByFullSql("select bdcdyh from bdck.bdcs_djdy_gz where  bdcdyid='" + bdcdyid + "'");
                    if (djdybdcdyhs.size() > 0) {
                        bdcdyh = (String) djdybdcdyhs.get(0).get("BDCDYH");
                    }
                }
            }
            //获取单元非抵押权的权利id
            List<Map> qlid = baseCommonDao.getDataListByFullSql("select qlid from bdck.bdcs_ql_gz where BDCDYH like '" + bdcdyh + "%' and xmbh='" + newxmbh + "' and QLLX <> '23'");
            //找出预告权利人
            List<String> list = new ArrayList<String>();
            
            if("YG002".equals(ywdh)){
            	//不需要抽取房开
            }else{
            	List<Map> syqr = baseCommonDao.getDataListByFullSql("select sqrid from bdck.bdcs_sqr where xmbh='" + newxmbh + "' and"
            			+ " sqrxm = '" + String.valueOf(projects.get(0).get("SHARESEARCH.T_PROJECT.CQRXM")) + "' and zjh = '" + String.valueOf(projects.get(0).get("SHARESEARCH.T_PROJECT.CQRZJHM")) + "'");
            	//个人办理的转移不抽取所有权人，房开办理的要抽取
            	if (syqr.size() > 0 && (!"ZY002".equals(ywdh) || !"12".equals(jgdm))) {
            		list.add((String) syqr.get(0).get("SQRID"));
            	}
            }
            //所有权人

            for (int i = 0; i < holders.size(); i++) {
                if (!"ZY002".equals(ywdh) && !"2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
                    List<Map> sqrid = baseCommonDao.getDataListByFullSql("select sqrid from bdck.bdcs_sqr where xmbh='" + newxmbh + "' and"
                            + " sqrxm = '" + String.valueOf(holders.get(i).get("SHARESEARCH.T_HOLDER.QLRMC")) + "' and zjh = '" + String.valueOf(holders.get(i).get("SHARESEARCH.T_HOLDER.ZJH")) + "'");
                    if (sqrid.size() > 0) {
                        list.add((String) sqrid.get(0).get("SQRID"));
                    }
                } else if ("ZY002".equals(ywdh) && "2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {
                    List<Map> sqrid = baseCommonDao.getDataListByFullSql("select sqrid from bdck.bdcs_sqr where xmbh='" + newxmbh + "' and"
                            + " sqrxm = '" + String.valueOf(holders.get(i).get("SHARESEARCH.T_HOLDER.QLRMC")) + "' and zjh = '" + String.valueOf(holders.get(i).get("SHARESEARCH.T_HOLDER.ZJH")) + "' and sqrlb='1' ");
                    if (sqrid.size() > 0) {
                        list.add((String) sqrid.get(0).get("SQRID"));
                    }
                }

            }
            Object[] sqrids = list.toArray();
            if (qlid.size() > 0 && sqrids.length > 0) {
            	projectapi.addQLRfromSQR(newxmbh, (String) qlid.get(0).get("QLID"), sqrids);
            }

            Object[] dyqrid = dyqrids.toArray();

            //预告抵押人
            //获取单元的权利id
            List<Map> dyqlid = baseCommonDao.getDataListByFullSql("select qlid from bdck.bdcs_ql_gz where BDCDYH like '" + bdcdyh + "%' and xmbh='" + newxmbh + "' and QLLX = '23'");
            List<String> dyrlist = new ArrayList<String>();
            //银行/房开
            List<Map> dept = null;
            if ("11".equals(jgdm) || "12".equals(jgdm)) {

                for (int i = 0; i < holders.size(); i++) {
                    if ("2".equals(holders.get(i).get("SHARESEARCH.T_HOLDER.YGSQR"))) {//

                        dept = baseCommonDao.getDataListByFullSql("select sqrid from bdck.bdcs_sqr where xmbh='" + newxmbh + "' and sqrxm = '" + holders.get(i).get("SHARESEARCH.T_HOLDER.QLRMC") + "' and "
                                + " zjh = '" + holders.get(i).get("SHARESEARCH.T_HOLDER.ZJH") + "'");
                    }
                }

            } else {

                dept = baseCommonDao.getDataListByFullSql("select sqrid from bdck.bdcs_sqr where xmbh='" + newxmbh + "' and"
                        + " sqrxm = '" + String.valueOf(depts.get(0).get("SHARESEARCH.DEPT.JGMC")) + "' and zjh = '" + String.valueOf(depts.get(0).get("SHARESEARCH.DEPT.ZJH")) + "'");
            }

            if (dept != null && dept.size() > 0) {
                dyrlist.add((String) dept.get(0).get("SQRID"));
            }
            Object[] dyrids = dyrlist.toArray();
            if (dyqlid.size() > 0 && dyrids.length > 0) {
                projectapi.addQLRfromSQR(newxmbh, (String) dyqlid.get(0).get("QLID"), dyrids);
            }

            //抽取抵押权信息
            Map dymsg = getDYQInfo(newxmbh);
            List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + newxmbh + "'");
            if (qls != null && qls.size() > 0) {
                for (BDCS_QL_GZ ql : qls) {
                    //将房产交易价格抽取到ql_gz
                    if (projects.get(0).get("SHARESEARCH.T_PROJECT.FDCJYJG") != null && !"".equals(projects.get(0).get("SHARESEARCH.T_PROJECT.FDCJYJG"))) {
                        String qdjg = String.valueOf(projects.get(0).get("SHARESEARCH.T_PROJECT.FDCJYJG"));
                        ql.setQDJG(Double.parseDouble(qdjg));
                    }
                    //将持证方式抽取到ql_gz
                    if (projects.get(0).get("SHARESEARCH.T_PROJECT.CZFS") != null && !"".equals(projects.get(0).get("SHARESEARCH.T_PROJECT.CZFS"))) {
                        String czfs = String.valueOf(projects.get(0).get("SHARESEARCH.T_PROJECT.CZFS"));
                        ql.setCZFS(czfs);
                    }


                    BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
                    if (ql.getQLLX() != null && "23".equals(ql.getQLLX())) {
                        qlServiceimpl.setValue(dymsg, ql);
                        qlServiceimpl.setValue(dymsg, fsql);
                        //添加抵押权人
                        qlServiceimpl.addQLRfromSQR(newxmbh, ql.getId(),dyqrid);
                    }
                    //最高债券数额和一般抵押数额若为空，则为空，不能存入0.0，因存入方法在qlserviceimpl不能更改，故在此赋值为空
                    if (dymsg.get("ZGZQSE") == null) {
                        fsql.setZGZQSE(null);
                    }
                    if (dymsg.get("BDBZZQSE") == null) {
                        fsql.setBDBZZQSE(null);
                    }
                    baseCommonDao.update(ql);
                    baseCommonDao.update(fsql);
                }
            }
            baseCommonDao.flush();

            //}
            //拷贝附件到资料中
            workflowAPI _workflowAPI = new workflowAPI();
            Connection conn = JH_DBHelper.getConnect_sharesearch();
            ResultSet sqlDataRSet = JH_DBHelper.excuteQuery(conn, "select mlid,wjnr,remark from sharesearch.t_file  where xmbh='" + ywh + "'");
            String MATERIAL = ConfigHelper.getNameByValue("MATERIAL");//扫描件存储位置
            while (sqlDataRSet.next()) {
                //文件内容
                Blob blob = sqlDataRSet.getBlob("wjnr");
                //文件名
                String orifilename = sqlDataRSet.getString("remark");
                //目录Id
                String mlid = sqlDataRSet.getString("mlid");
                List<Map> wfi_ProMaters = this.baseCommonDao.getDataListByFullSql("select MATERILINST_ID from BDC_WORKFLOW.Wfi_ProMater where MATERIAL_ID='" + mlid + "' AND PROINST_ID='" + sm.getID() + "'");
                Wfi_MaterData wfi_materData = new Wfi_MaterData();
                String materilinst_id = wfi_ProMaters.size() > 0 ? String.valueOf(wfi_ProMaters.get(0).get("MATERILINST_ID")) : "";
                wfi_materData.setMaterialdata_Id(Common.CreatUUID());
                wfi_materData.setFile_Name(orifilename);
                wfi_materData.setMaterilinst_Id(materilinst_id);
                wfi_materData.setFile_Index(new Date().getTime());
                wfi_materData.setUpload_Id(Global.getCurrentUserInfo().getId());
                wfi_materData.setDisc(MATERIAL);
                wfi_materData.setUpload_Name(Global.getCurrentUserInfo().getUserName());
                wfi_materData.setThumb("");
                DiskFileItem fileItem = new DiskFileItem("file", "image/jpeg", false, orifilename, -1, null);
                fileItem.getOutputStream();
                File f = fileItem.getStoreLocation();
                BlobToFile(f.getAbsolutePath(), blob);
                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                response.reset();

                _workflowAPI.ModifyMaterialPath(materilinst_id, wfi_materData.getMaterialdata_Id());
                updateLoadImage(multipartFile, wfi_materData, false);
            }
            sqlDataRSet.close();
            //将远程报件中手动填写的份数和资料类型存入项目对应的wfi_promater中
            List<Map> catalog = get_sharesearchData("select * from SHARESEARCH.T_CATALOG where xmbh = '" + ywh + "'");
            for (Map map : catalog) {
                baseCommonDao.excuteQueryNoResult("update BDC_WORKFLOW.Wfi_ProMater set MATERIAL_COUNT=" + map.get("MATERIAL_COUNT") + " , MATERIAL_TYPE = " + map.get("MATERIAL_TYPE") + " ,MATERIAL_STATUS= " + map.get("MATERIAL_NEED") + " where MATERIAL_ID='" + map.get("MATERIAL_ID") + "' AND PROINST_ID='" + sm.getID() + "'");
            }
            //更新抽取次数
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            // 创建该连接下的PreparedStatement对象
            pstmt = conn.prepareStatement("update sharesearch.t_project set cqcs=NVL(cqcs,0)+1 where xmbh='" + ywh + "'");
            pstmt.executeQuery();
            //关闭数据库连接
            if (conn != null) conn.close();

            r.setSuccess(re.getSuccess());
            r.setMsg(sm.getName() + "@" + re.getMsg());
        }


        return r;
    }

    private Message acceptProject_batch(String ywh, List<Map> projects,ConverterUtil util, List<Map> houses, List<Map> holders, HttpServletRequest request,HttpServletResponse response) throws Exception {
        Message result = new Message();
        List<Map> qllists = get_sharesearchData("SELECT * FROM SHARESEARCH.T_BDCS_QL  WHERE XMBH='" + ywh + "'");
        List<Map> cons = get_sharesearchData("SELECT BZ,CONSTTRANS FROM SHARESEARCH.SYS_CONST WHERE CONSTVALUE='" + projects.get(0).get("YWLX") + "'");
        List<Map> wfd_prodef = this.baseCommonDao.getDataListByFullSql("SELECT PRODEF_ID, PRODEFCLASS_ID, PRODEF_CODE FROM BDC_WORKFLOW.WFD_PRODEF WHERE PRODEF_ID= '" + cons.get(0).get("BZ") + "' AND PRODEF_ID IS NOT NULL");
        //再将结果拼接给sys
        List<Map> sys = new ArrayList<Map>();
        //获取当前业务流程
        List<Map> workflows = null;
        String ywdh = "";
        if (wfd_prodef.size() > 0) {
            workflows = this.baseCommonDao.getDataListByFullSql("select b.workflowname,b.workflowcaption  FROM BDC_WORKFLOW.WFD_PRODEF a  left join bdc_workflow.wfd_mapping b on a.prodef_code=b.workflowcode where  a.prodef_id='" + wfd_prodef.get(0).get("PRODEF_ID") + "'");
            if (workflows != null && workflows.size() > 0) {
                ywdh = (String) workflows.get(0).get("WORKFLOWNAME");
                System.out.println("当前流程是：" + workflows.get(0).get("WORKFLOWCAPTION"));
            }
            cons.get(0).put("PRODEF_ID", wfd_prodef.get(0).get("PRODEF_ID"));
            cons.get(0).put("PRODEFCLASS_ID", wfd_prodef.get(0).get("PRODEFCLASS_ID"));
            sys.addAll(cons);
        }
        if (sys.size() == 0)
            return new Message();
        if (StringUtils.isEmpty(sys.get(0).get("BZ")))
            throw new Exception("无匹配流程定义。");
        SmProInfo proinfo = new SmProInfo();
        SmObjInfo smObjInfo = new SmObjInfo();
        if (Global.getCurrentUserInfo() == null)
            throw new Exception("请重新登陆。");
        smObjInfo.setID(Global.getCurrentUserInfo().getId());
        smObjInfo.setName(Global.getCurrentUserInfo().getUserName());
        List<Object> staffList = new ArrayList<Object>();
        staffList.add(smObjInfo);
        proinfo.setFile_Urgency("1");
        proinfo.setAcceptor(smObjInfo.getName());
        proinfo.setProDef_ID(sys.get(0).get("BZ").toString());//个人房产抵押
        Stack<String> defname = new Stack<String>();
        defname.push(String.valueOf(sys.get(0).get("CONSTTRANS")));
        StringBuilder def_name_sb = new StringBuilder();
        StringBuilder dyr = new StringBuilder();
        String def_name_string = "";
        getWorkflowDefName(defname, String.valueOf(sys.get(0).get("PRODEFCLASS_ID")));
        while (!defname.isEmpty()) {
            String v = defname.pop();
            def_name_sb.append(v + ",");
        }
        if (def_name_sb.toString().length() > 0)
            def_name_string = def_name_sb.toString().substring(0, def_name_sb.toString().length() - 1);
        for (int i = 0; i < projects.size(); i++) {
            if (i < projects.size() - 1)
                dyr.append(",");
        }
        if (holders.size() > 0)
            dyr.append(",");
        //把后面的多余，去掉
        String dyrstr = dyr.toString();
        if (dyr.toString().endsWith(",")) dyrstr = dyr.toString().substring(0, dyr.toString().length() - 1);
        proinfo.setProDef_Name(def_name_string);
        //改成把t_house中的坐落放到项目名称中+"  "+String.valueOf(projects.get(0).get("FWZL"))
        String xzqh = ConfigHelper.getNameByValue("XZQHDM");
        proinfo.setProInst_Name(dyrstr + "  " + String.valueOf(houses.get(0).get("ZL") == null ? "" : houses.get(0).get("ZL")));
        //流程受理项目接口
        Object returnsmObjInfo = new workflowAPI().Accept(proinfo, staffList);
        SmObjInfo sm = (SmObjInfo) returnsmObjInfo;
        if (!StringUtils.isEmpty(sm.getFile_number())) {
            projectAPI projectapi = new projectAPI();
            //获取项目信息接口
            Object projectinfo = new projectAPI().getProjectInfo(sm.getFile_number(), request);
            ProjectInfo p = (ProjectInfo) projectinfo;
            String newxmbh = p.getXmbh().substring(0, p.getXmbh().length() - 5) + "@" + ywh;
            //桂林地区受理时，workflowAPI().Accept环节时xmxx.ywlsh丢失
            List<Map> m=  baseCommonDao.getDataListByFullSql("select prolsh from BDC_workflow.Wfi_Proinst p where p.file_number='" +sm.getFile_number()+ "' ");
            //更新项目编号，该项目编号包含了银行的业务号
            this.baseCommonDao.excuteQueryNoResult("update bdck.bdcs_xmxx set xmbh='" + newxmbh + "', ywlsh='"+m.get(0).get("PROLSH")+"' where xmbh='" + p.getXmbh() + "'");
            //更新项目编号，该项目编号包含了银行的业务号
//            this.baseCommonDao.excuteQueryNoResult("update bdck.bdcs_xmxx set xmbh='" + newxmbh + "' where xmbh='" + p.getXmbh() + "'");
            p.setXmbh(newxmbh);
            //桂林远程受理，只取最后一次受理保存到查询共享库
            JH_DBHelper.excuteQuery(JH_DBHelper.getConnect_sharesearch(), "update SHARESEARCH.T_PROJECT sh set sh.PROINST_ID='" + newxmbh + "'where sh.XMBH='" + ywh + "'");

            List<WFD_MAPPING> mappings = this.baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='" + wfd_prodef.get(0).get("PRODEF_CODE") + "'");
            WFD_MAPPING mapping = mappings == null || mappings.isEmpty() ? null : mappings.get(0);
            if (mapping == null)
                throw new Exception("未获取到WFD_MAPPING。");
            // 获取基准流程
            T_BASEWORKFLOW base_wf = this.baseCommonDao.get(T_BASEWORKFLOW.class, mapping.getWORKFLOWNAME());
            if (base_wf == null)
                throw new Exception("未获取到基准流程，ID=" + mapping.getWORKFLOWNAME());
            String selector = base_wf.getSELECTORID();

            //批量选择单元
            for(Map house : houses) {
                //选择单元同时把house和qllist相关数据关联起来
                for(Map ql : qllists) {
                    String houseqlid = StringHelper.formatObject(house.get("QLID"));
                    if(!StringHelper.isEmpty(houseqlid) && houseqlid.equals(ql.get("ID"))) {
                        house.put("qlmsg", ql);
                    }
                }
                String bdcdyh = StringHelper.formatObject(house.get("BDCDYH"));
                Map<String,String> map = new HashMap<String, String>();
                map.put("BDCDYH",bdcdyh);

                Message msg = SelectorTools.queryDataForRunOnce(selector,map);
                List<Map> list = null;
                if(msg!=null && msg.getRows()!=null){
                    list=(List<Map>)msg.getRows();
                }
                net.sf.json.JSONArray rows = net.sf.json.JSONArray.fromObject(list);
                StringBuilder ids = new StringBuilder();
                if (rows.size() > 0)
                {
                    net.sf.json.JSONObject object = (net.sf.json.JSONObject) rows.get(0);
                    String bdcdyid = object.getString("BDCDYID");
                    ids.append(bdcdyid);
                }

                Object resultMessage = projectapi.addBDCDY(p.getXmbh(), ids.toString());
                ResultMessage re = (ResultMessage) resultMessage;

                result.setSuccess("true");
                result.setMsg(sm.getName() + "@" + re.getMsg());
                if(!"成功".equals(re.getMsg())) {
                    result.setMsg(house.get("ZL")+"选择单元失败");
                    result.setSuccess("false");
                    throw new RuntimeException(result.getMsg());
                }
            }
            util.AddPrefix(holders, "sharesearch.t_holder");
            util.AddPrefix(projects, "SHARESEARCH.T_PROJECT");
            //抽取银行作为抵押权人
            BDCS_SQR dyqr = util.CreateSingleClass(BDCS_SQR.class, projects);
            dyqr.setXMBH(p.getXmbh());
            dyqr.setSQRLB(ConstValue.SQRLB.JF.Value);
            projectapi.addSQRXX(dyqr);

            List<String> dyqridlist = new ArrayList<String>();
            dyqridlist.add(dyqr.getId());
            Object[] dyrids = dyqridlist.toArray();
            //按单元抽取抵押信息
            //构建登记单元id和不动产单元id关系
            HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
            List<BDCS_DJDY_GZ> djdys=this.baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+newxmbh+"'");
            for(BDCS_DJDY_GZ djdy:djdys){
                if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
                    djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
                }
            }
            List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+newxmbh+"'");
            List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+newxmbh+"'");
            for(Rights ql:qls){
                String djdyid=ql.getDJDYID();
                String bdcdyh = ql.getBDCDYH();
                if (houses != null && houses.size() > 0) {
                    for(Map fwxx : houses) {
                        if(!StringHelper.isEmpty(fwxx.get("BDCDYH")) && !StringHelper.formatObject(fwxx.get("BDCDYH")).contains(bdcdyh)){
                            continue;
                        }

                        if(!djdyid_bdcdyid.containsKey(djdyid)){
                            continue;
                        }

                        Map qlmsg = (Map)fwxx.get("qlmsg");
                        if(ConstValue.QLLX.DIYQ.Value.equals(ql.getQLLX())){
                            ///更新权利
                            ql.setQLQSSJ( StringHelper.FormatByDate(qlmsg.get("QLQSSJ"), "yyyy-MM-dd"));
                            ql.setQLJSSJ(StringHelper.FormatByDate(qlmsg.get("QLJSSJ"), "yyyy-MM-dd") );
                            this.baseCommonDao.update(ql);
                            //更新附属权利
                            for(SubRights fsql:fsqls){
                                fsql.setDYFS(StringHelper.formatObject(qlmsg.get("DYFS")));
                                if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
                                    if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
                                        fsql.setBDBZZQSE(StringHelper.getDouble(qlmsg.get("BDBSE")));
                                        fsql.setZGZQQDSS(null);
                                    }else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
                                        fsql.setZGZQSE(StringHelper.getDouble(qlmsg.get("ZGZQSE")));
                                    }
                                    this.baseCommonDao.update(fsql);
                                }
                            }
                            this.baseCommonDao.flush();
                            //添加抵押权人
                            qlServiceimpl.addQLRfromSQR(newxmbh, ql.getId(),dyrids);
                        }else{
                            ///更新权利
                            ql.setQLQSSJ( StringHelper.FormatByDate(qlmsg.get("QLQSSJ"), "yyyy-MM-dd"));
                            ql.setQLJSSJ(StringHelper.FormatByDate(qlmsg.get("QLJSSJ"), "yyyy-MM-dd") );
                            this.baseCommonDao.update(ql);
                            //更新附属权利
                            for(SubRights fsql:fsqls){
                                fsql.setDYFS(StringHelper.formatObject(qlmsg.get("DYFS")));
                                if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
                                    if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
                                        fsql.setBDBZZQSE(StringHelper.getDouble(qlmsg.get("BDBSE")));
                                        fsql.setZGZQQDSS(null);
                                    }else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
                                        fsql.setZGZQSE(StringHelper.getDouble(qlmsg.get("ZGZQSE")));
                                    }
                                    this.baseCommonDao.update(fsql);
                                }
                            }
                            this.baseCommonDao.flush();
                        }
                    }
                    this.baseCommonDao.flush();
                }
            }
            //拷贝附件到资料中
            workflowAPI _workflowAPI = new workflowAPI();
            Connection conn = JH_DBHelper.getConnect_sharesearch();
            ResultSet sqlDataRSet = JH_DBHelper.excuteQuery(conn, "select mlid,wjnr,remark from sharesearch.t_file  where xmbh='" + ywh + "'");
            String MATERIAL = ConfigHelper.getNameByValue("MATERIAL");//扫描件存储位置
            while (sqlDataRSet.next()) {
                //文件内容
                Blob blob = sqlDataRSet.getBlob("wjnr");
                //文件名
                String orifilename = sqlDataRSet.getString("remark");
                //目录Id
                String mlid = sqlDataRSet.getString("mlid");
                List<Map> wfi_ProMaters = this.baseCommonDao.getDataListByFullSql("select MATERILINST_ID from BDC_WORKFLOW.Wfi_ProMater where MATERIAL_ID='" + mlid + "' AND PROINST_ID='" + sm.getID() + "'");
                Wfi_MaterData wfi_materData = new Wfi_MaterData();
                String materilinst_id = wfi_ProMaters.size() > 0 ? String.valueOf(wfi_ProMaters.get(0).get("MATERILINST_ID")) : "";
                wfi_materData.setMaterialdata_Id(Common.CreatUUID());
                wfi_materData.setFile_Name(orifilename);
                wfi_materData.setMaterilinst_Id(materilinst_id);
                wfi_materData.setFile_Index(new Date().getTime());
                wfi_materData.setUpload_Id(Global.getCurrentUserInfo().getId());
                wfi_materData.setDisc(MATERIAL);
                wfi_materData.setUpload_Name(Global.getCurrentUserInfo().getUserName());
                wfi_materData.setThumb("");
                DiskFileItem fileItem = new DiskFileItem("file", "image/jpeg", false, orifilename, -1, null);
                fileItem.getOutputStream();
                File f = fileItem.getStoreLocation();
                BlobToFile(f.getAbsolutePath(), blob);
                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                response.reset();

                _workflowAPI.ModifyMaterialPath(materilinst_id, wfi_materData.getMaterialdata_Id());
                updateLoadImage(multipartFile, wfi_materData, false);
            }
            sqlDataRSet.close();
            //将远程报件中手动填写的份数和资料类型存入项目对应的wfi_promater中
            List<Map> catalog = get_sharesearchData("select * from SHARESEARCH.T_CATALOG where xmbh = '" + ywh + "'");
            for (Map map : catalog) {
                baseCommonDao.excuteQueryNoResult("update BDC_WORKFLOW.Wfi_ProMater set MATERIAL_COUNT=" + map.get("MATERIAL_COUNT") + " , MATERIAL_TYPE = " + map.get("MATERIAL_TYPE") + " ,MATERIAL_STATUS= " + map.get("MATERIAL_NEED") + " where MATERIAL_ID='" + map.get("MATERIAL_ID") + "' AND PROINST_ID='" + sm.getID() + "'");
            }
            //更新抽取次数
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            // 创建该连接下的PreparedStatement对象
            pstmt = conn.prepareStatement("update sharesearch.t_project set cqcs=NVL(cqcs,0)+1 where xmbh='" + ywh + "'");
            pstmt.executeQuery();
            //关闭数据库连接
            if (conn != null) conn.close();

        }
        return result;
    }

    /**
     * 从申请人中抽取权利人
     *
     */
//protected void addQLRbySQRs(String qlid, Object[] sqrids, String xmbh, boolean ishebing) {
//
//	boolean existholder = false;
//	int count = 0;
//	BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
//	// 获取第一个证书
//	String hqlCondition = " id IN (SELECT ZSID FROM BDCS_QDZR_GZ QDZR WHERE QDZR.QLID=''{0}'' AND XMBH=''{1}'' ) AND XMBH=''{1}''";
//	hqlCondition = MessageFormat.format(hqlCondition, qlid, xmbh);
//	List<BDCS_ZS_GZ> zslist = baseCommonDao.getDataList(BDCS_ZS_GZ.class, hqlCondition);
//	// 获取当前权利的所有权利人
//	List<BDCS_QLR_GZ> qlrlist = baseCommonDao.getDataList(BDCS_QLR_GZ.class, " XMBH='" + xmbh + "' AND QLID='" + qlid + "' ORDER BY SXH");
//	// 根据权利人数量判断是否已经存在权利人
//	existholder = (qlrlist == null || qlrlist.size() < 1) ? false : true;
//	// 新生成的证书ID
//	String newzsid = "";
//	// 循环每个申请人ID
//	if (sqrids != null && sqrids.length > 0) {
//		for (Object sqridobj : sqrids) {
//
//			String sqrid = StringHelper.formatObject(sqridobj);
//			if (!StringHelper.isEmpty(sqrid)) {
//				boolean exists = false;
//				// 判断该申请人是否已经添加过权利人
//				if (qlrlist != null) {
//					for (BDCS_QLR_GZ qlr : qlrlist) {
//						if (!StringUtils.isEmpty(qlr.getSQRID()) && qlr.getSQRID().equals(sqrid)) {
//							exists = true;
//							break;
//						}
//					}
//				}
//				// 如果没有添加过
//				if (!exists) {
//					// 先添加权利人
//					BDCS_SQR sqr = baseCommonDao.get(BDCS_SQR.class, sqrid);
//					BDCS_QLR_GZ qlr = ObjectHelper.CopySQRtoQLR(sqr);
//					qlr.setQLID(qlid);
//					if (sqr != null) {
//						qlr.setSQRID(sqr.getId());
//					}
//
//					qlr.setXMBH(xmbh);
//					baseCommonDao.save(qlr);
//
//					// 添加权地证人关系记录
//					BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
//					qdzr.setBDCDYH(ql.getBDCDYH());
//					qdzr.setQLRID(qlr.getId());
//					qdzr.setDJDYID(ql.getDJDYID());
//					qdzr.setFSQLID(ql.getFSQLID());
//					qdzr.setQLID(ql.getId());
//					qdzr.setXMBH(xmbh);
//					baseCommonDao.save(qdzr);
//					// 判断是否需要添加证书，两种情况
//					// 1：分别持证
//					// 2:共同持证且当前没有权利人并且这是第一个sqrid
//					if (ql.getCZFS() == null || ql.getCZFS().equals(CZFS.FBCZ.Value) || (ql.getCZFS().equals(CZFS.GTCZ.Value) && !existholder && count < 1)) {
//						BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
//						zs.setId((String) SuperHelper.GeneratePrimaryKey());
//						zs.setXMBH(xmbh);
//						ql.setBDCQZH("");
//						qdzr.setZSID(zs.getId());
//						newzsid = zs.getId();
//						baseCommonDao.save(zs);
//					} else // 这种情况就是共同持证并且已经有证书了，只需要找到一个证书，然后把证书ID写到上面的qdzr里就行了
//					{
//
//						if (zslist.size() > 0) {
//							qdzr.setZSID(zslist.get(0).getId());
//						} else {
//							qdzr.setZSID(newzsid);
//						}
//					}
//				}
//			}
//			count++;
//		}
//
//		// 预告登记需要加上义务人
//		if (ishebing) {
//
//			List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='" + xmbh + "'");
//			if (fsqlList.size() > 0) {
//
//				StringBuilder ywr = new StringBuilder();
//				StringBuilder ywrzjlx = new StringBuilder();
//				StringBuilder ywrzjh = new StringBuilder();
//				List<BDCS_SQR> ywrlist = baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='" + xmbh + "' AND SQRLB='2'");
//				for (int i = 0; i < ywrlist.size(); i++) {
//					if (i != 0) {
//						ywr.append("/");
//						ywrzjlx.append("/");
//						ywrzjh.append("/");
//					}
//					ywr.append(ywrlist.get(i).getSQRXM());
//					ywrzjlx.append(ywrlist.get(i).getZJLX());
//					ywrzjh.append(ywrlist.get(i).getZJH());
//				}
//
//				for (int i = 0; i < fsqlList.size(); i++) {
//					BDCS_FSQL_GZ fsql_GZ = fsqlList.get(i);
//					fsql_GZ.setYWR(ywr.toString());
//					fsql_GZ.setYWRZJZL(ywrzjlx.toString());
//					fsql_GZ.setYWRZJH(ywrzjh.toString());
//					baseCommonDao.update(fsql_GZ);
//				}
//			}
//		}
//		baseCommonDao.flush();
//	}
//}
    public void updateLoadImage(MultipartFile file, Wfi_MaterData materData, boolean thumb) throws IOException {
        String materilinst_id = materData.getMaterilinst_Id();
        // 先将文件保存到服务器
        Wfi_ProMater Wfi_ProMater = baseCommonDao.get(Wfi_ProMater.class,
                materilinst_id);
        List<Map> fileName = null;
        if (Wfi_ProMater != null) {
            Wfi_ProInst inst = baseCommonDao.get(Wfi_ProInst.class,
                    Wfi_ProMater.getProinst_Id());
            fileName = FileUpload.uploadFile(file, materilinst_id, inst);
        }

        if (fileName != null && fileName.size() > 0) {
            boolean update = true;
            for (int i = 0; i < fileName.size(); i++) {
                Map name = fileName.get(i);
                // 在数据库中记录文件存储的路径
                //检测是否是覆盖
                Wfi_MaterData olddata = getMaterData(materData
                        .getMaterialdata_Id());
                if (olddata != null && update) {
                    olddata.setFile_Name(name.get("filename")
                            .toString());
                    olddata.setFile_Path(name.get("filename").toString());
                    olddata.setPath(name.get("filepath").toString());
                    //olddata.setUpload_Date(new Date());
                    baseCommonDao.update(olddata);
                } else {
                    update = false;
                    Wfi_MaterData materData_add = new Wfi_MaterData();
                    if (i == 0) {
                        materData_add.setMaterialdata_Id(materData
                                .getMaterialdata_Id());
                    } else {
                        materData_add.setMaterialdata_Id(Common.CreatUUID());
                    }
                    materData_add.setMaterilinst_Id(materData
                            .getMaterilinst_Id());
                    if (materData.getFile_Name() != null
                            && !materData.getFile_Name().equals("")) {
                        materData_add.setFile_Name(materData.getFile_Name());
                    } else {
                        materData_add.setFile_Name(name.get("filename")
                                .toString());
                    }
                    materData_add.setFile_Index(materData.getFile_Index());
                    materData_add.setUpload_Name(materData.getUpload_Name());
                    materData_add.setUpload_Id(materData.getUpload_Id());
                    materData_add.setFile_Path(name.get("filename").toString());
                    materData_add.setPath(name.get("filepath").toString());
                    materData_add.setDisc(materData.getDisc());
                    if (thumb) {
                        try {
                            // materData_add.setThumb(CreatThumb(file));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        // materData_add.setThumb(materData.getThumb());
                    }

                    materData_add.setUpload_Date(new Date());
                    baseCommonDao.save(materData_add);

                }
                baseCommonDao.flush();
            }

        } else {

        }
    }

    private Wfi_MaterData getMaterData(String id) {
        return baseCommonDao.get(Wfi_MaterData.class, id);
    }

    private File BytesToFile(byte[] content, String filename) {
        BufferedOutputStream bs = null;
        File f = new File(filename);
        try {

            FileOutputStream fs = new FileOutputStream(f);
            bs = new BufferedOutputStream(fs);
            bs.write(content);
            bs.close();
            bs = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bs != null) try {
            bs.close();
        } catch (Exception e) {
        }
        return f;
    }

    private File BlobToFile(String filename, Blob blob) {
        File blobFile = null;
        try {
            blobFile = new File(filename);
            FileOutputStream outStream = new FileOutputStream(blobFile);
            InputStream inStream = blob.getBinaryStream();

            int length = -1;
            int size = (int) blob.length();
            // int     size    = blob.ggetBufferSize();
            byte[] buffer = new byte[size];

            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }

            inStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR(djv_exportBlob) Unable to export:" + filename);

        } finally {
            return blobFile;
        }
    }

    /**
     * 将blob转化为byte[],可以转化二进制流的
     *
     * @param blob
     * @return
     */
    private byte[] blobToBytes(Blob blob) {
        if (blob == null)
            return null;
        InputStream is = null;
        byte[] b = null;
        try {
            is = blob.getBinaryStream();
            b = new byte[(int) blob.length()];
            is.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    private void getWorkflowDefName(Stack<String> names, String id) {
        Wfd_ProClass w = this.baseCommonDao.get(Wfd_ProClass.class, id);
        if (w != null) {
            names.push(w.getProdefclass_Name());
            //result.append(w.getProdefclass_Name()+",");
            if (!StringUtils.isEmpty(w.getProdefclass_Pid())) {
                getWorkflowDefName(names, w.getProdefclass_Pid());
            }
        }
    }

    @Override
    public Map getDYQInfo(String xmbh) throws Exception {
        String ywh = xmbh;
        if (xmbh.contains("@"))
            ywh = xmbh.split("@")[1];
        Message r = new Message();
        String jgdm = ywh.substring(7, 9);
        List<Map> projects = get_sharesearchData("select * from sharesearch.t_project  where xmbh='" + ywh + "'");
        List<Map> holders = get_sharesearchData("select * from sharesearch.t_holder where xmbh='" + ywh + "'");
        String slry = projects.size() > 0 ? String.valueOf(projects.get(0).get("SLRYID")) : "-1";
        List<Map> depts = get_sharesearchData(" select dept.*,u.XM,u.SFZH,u.LXFS from sharesearch.T_PROJECT t join sharesearch.USERS u on u.id=t.slryid join sharesearch.dept dept on dept.jgbh=u.ssjgdm where t.slryid='" + slry + "'");

        Map result = new HashMap();
        StringBuilder dyr = new StringBuilder();
        for (int i = 0; i < projects.size(); i++) {
        	//房开不是抵押人
        	if(!"6001".equals(projects.get(i).get("YWLX"))){
        		if (projects.get(0).get("CQRXM") == null && "11".equals(jgdm)) {
        			dyr.append(projects.get(0).get("CQRXM") == null ? depts.get(0).get("JGMC") : projects.get(0).get("CQRXM"));
        			if (i < projects.size() - 1)
        				dyr.append(",");
        		} else {
        			dyr.append(projects.get(0).get("CQRXM"));
        			if (i < projects.size() - 1)
        				dyr.append(",");
        		}
        	}

        }
        if (StringHelper.isEmpty(dyr)&&holders.size() > 0)
            dyr.append(",");
        for (int i = 0; i < holders.size(); i++) {
            if (!"2".equals(holders.get(i).get("YGSQR"))) {//房开不是抵押人

                dyr.append(holders.get(i).get("QLRMC"));
                if (i < holders.size() - 1)
                    dyr.append(",");
            }
        }
        String dyrstr = dyr.toString();
        if (dyrstr.endsWith(",")) {
            dyrstr = dyrstr.substring(0, dyrstr.length() - 1);
        }
//        result.put("DYR", dyrstr); //抵押人由不动产单元带过来 不提取银行填写的
        result.put("id_xmxx_zsgs_dyq", projects.size() > 0 ? projects.get(0).get("ZSGS") : "");
        result.put("ZJJZWZL", projects.size() > 0 ? projects.get(0).get("FWZL") : "");
        //result.put("BDBZZQSE",   projects.size()>0?StringHelper.formatDouble(projects.get(0).get("QDJG")):"");
        result.put("BDBZZQSE", projects.size() > 0 ? projects.get(0).get("QDJG") : "");
        result.put("ZJJZWDYFW", projects.size() > 0 ? projects.get(0).get("DYFW") : "");
        result.put("QLQSSJ", projects.size() > 0 ? StringHelper.FormatDateOnType(projects.get(0).get("YDRQQ"), "yyyy-MM-dd") : "");
        result.put("QLJSSJ", projects.size() > 0 ? StringHelper.FormatDateOnType(projects.get(0).get("YDRQZ"), "yyyy-MM-dd") : "");
        //result.put("ZGZQSE",   projects.size()>0?StringHelper.formatDouble(projects.get(0).get("ZGZQSE")):"");
        result.put("ZGZQSE", projects.size() > 0 ? projects.get(0).get("ZGZQSE") : "");
        result.put("DYPGJZ", projects.size() > 0 ? projects.get(0).get("DYPGJZ") : "");
        result.put("ZGZQQDSS", projects.size() > 0 ? projects.get(0).get("ZGZQQDSS") : "");
        result.put("DYFS", projects.size() > 0 ? projects.get(0).get("DYFS") : "");

        return result;
    }

    /**
     * 远程获取sharesearch库的数据
     *
     * @param sql
     * @return 获取的数据
     * @author 包基杰
     */
    public List<Map> get_sharesearchData(String sql) {
        List<Map> list = null;
        Map map = null;
        Connection conn = JH_DBHelper.getConnect_sharesearch();
        ResultSet excuteQuery = null;
        try {
            excuteQuery = JH_DBHelper.excuteQuery(conn, sql);
            if (excuteQuery != null) {
                ResultSetMetaData rs = excuteQuery.getMetaData();
                int columnCount = rs.getColumnCount();//列数
                list = new ArrayList<Map>();
                while (excuteQuery.next()) {
                    map = new HashMap();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = rs.getColumnName(i + 1);
                        Object value = excuteQuery.getObject(columnName);
                        map.put(columnName, value);
                    }
                    list.add(map);
                }

                return list;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (excuteQuery != null) {
                try {
                    ResultSet unwrap = excuteQuery.unwrap(ResultSet.class);
                    unwrap.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    Connection unwrap = conn.unwrap(Connection.class);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ArrayList<Map>();
    }

    /**
     * 桂林远程报件箱添加：受理状态和提交时间查询条件
     */
    public Message guilinsearch(String pageindex, HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        String xzqh = ConfigHelper.getNameByValue("XZQHDM");
        Message r = new Message();
        String ywh = RequestHelper.getParam(request, "ywh");
        String starttime = RequestHelper.getParam(request, "starttime");
        String endtime = RequestHelper.getParam(request, "endtime");
        String iscqcs = RequestHelper.getParam(request, "iscqcs");
        String shareFullSql = "from sharesearch.T_PROJECT p left join (select xmbh,wmsys.wm_concat(zl) zl, min(bdcdylx) bdcdylx from sharesearch.T_HOUSE group by xmbh ) h on h.xmbh = p.xmbh where xmzt=5 ";
        String selectlztsql = "select * from(select a.*,rownum rn from (select p.CREATETIME,p.CQRXM QLRMC,p.CQZH BDCQZH,p.CQCS,p.CQRZJHM ZJH,p.XMBH YWH,h.ZL,h.BDCDYLX,p.PROINST_ID ";
        StringBuilder shareslzt = new StringBuilder(shareFullSql);
        //根据3个不同条件去查询
        if (!StringUtils.isEmpty(ywh)) {
            shareslzt.append(" and p.xmbh='").append(ywh).append("'");
        }
        if (!StringUtils.isEmpty(iscqcs) && "1".equals(iscqcs)) {
            shareslzt.append(" and p.cqcs is not null");
        } else if (!StringUtils.isEmpty(iscqcs) && "0".equals(iscqcs)) {
            shareslzt.append(" and p.cqcs is null");
        }
        if (!StringUtils.isEmpty(starttime)) {
            shareslzt.append(" and p.createtime>to_date('" + starttime + "','yyyy-mm-dd hh24:mi:ss')");
        }
        if (!StringUtils.isEmpty(endtime)) {
            shareslzt.append(" and p.createtime<to_date('" + endtime + "','yyyy-mm-dd hh24:mi:ss')");
        }
        if ("undefined".equals(pageindex) || StringUtils.isEmpty(pageindex)) {
            pageindex = "1";
        }
        Connection conn = JH_DBHelper.getConnect_sharesearch();
        ResultSet rs = null;
        try {
            rs = JH_DBHelper.excuteQuery(conn, "select count(1) NUM " + shareslzt.toString());
            while (rs.next()) {
                r.setTotal(Long.valueOf(rs.getString("NUM")));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    ResultSet unwrap = rs.unwrap(ResultSet.class);
                    unwrap.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    Connection unwrap = conn.unwrap(Connection.class);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        
        
        
        shareslzt.append(" order by p.createtime desc) a where rownum <=10*" + Integer.valueOf(pageindex) + ") where rn> 10*(" + Integer.valueOf(pageindex) + "-1)");
        List<Map> xmxx = get_sharesearchData(selectlztsql + shareslzt.toString());
        List<Map> holders = new ArrayList<Map>();
        List<Map> bdcqzhids = new ArrayList<Map>();
        List<Map> actinst_ids = new ArrayList<Map>();
        Set<Object> qzhSet =new HashSet<Object>();
        Set<Object> proidSet =new HashSet<Object>();
        Set<Object> ywhSet =new HashSet<Object>();
        if(xmxx.size()>0){
        for (Map map:xmxx) {
        	addDataToSet(map.get("BDCQZH"),qzhSet);
        	addDataToSet(map.get("PROINST_ID"),proidSet);
        	addDataToSet(map.get("YWH"),ywhSet);
		}
        bdcqzhids = baseCommonDao.getDataListByFullSql("select h.bdcdyid,qlr.bdcqzh from bdck.bdcs_h_xz h left join bdck.bdcs_qlr_xz qlr on qlr.xmbh=h.xmbh  where qlr.bdcqzh in ("+org.apache.commons.lang.StringUtils.join(qzhSet,",")+ ")");
        actinst_ids=baseCommonDao.getDataListByFullSql("select w.ACTINST_ID,XMXX.XMBH from BDC_workflow.Wfi_Proinst w inner join bdck.bdcs_xmxx xmxx  on w.file_number=xmxx.project_id  where xmxx.xmbh in("+org.apache.commons.lang.StringUtils.join(proidSet,",")+ ")");
        holders = get_sharesearchData("select xmbh,wm_concat(to_char(qlrmc)) qlrmc,wm_concat(to_char(bdcqzh)) bdcqzh,wm_concat(to_char(zjh)) zjh from sharesearch.t_holder where ygsqr!=2 and xmbh in(" +org.apache.commons.lang.StringUtils.join(ywhSet,",")+ ") group by xmbh");
        for (int i = 0; i < xmxx.size(); i++) {
        	for (Map map : bdcqzhids) {
				if(map.get("BDCQZH").equals(xmxx.get(i).get("BDCQZH"))){
					xmxx.get(i).put("BDCDYID", map.get("BDCDYID"));
					break;
				   }else{
					   xmxx.get(i).put("BDCDYID", "");
				   }
			}
		}
        for (int i = 0; i < xmxx.size(); i++) {
        	for (Map map : actinst_ids) {
				if(map.get("XMBH").equals(xmxx.get(i).get("PROINST_ID"))){
					xmxx.get(i).put("ACTINST_ID", map.get("ACTINST_ID"));
					break;
				   }else{
					   xmxx.get(i).put("ACTINST_ID", "");
				   }
			}
		}
        for (int i = 0; i < xmxx.size(); i++) {
        	for (Map map : holders) {
				if(map.get("XMBH").equals(xmxx.get(i).get("YWH"))){
    				xmxx.get(i).put("QLRMC",StringHelper.isEmpty(map.get("QLRMC"))?xmxx.get(i).get("QLRMC"):xmxx.get(i).get("QLRMC")+","+map.get("QLRMC"));
    				xmxx.get(i).put("BDCQZH",StringHelper.isEmpty(map.get("BDCQZH"))?xmxx.get(i).get("BDCQZH"):xmxx.get(i).get("BDCQZH")+","+map.get("BDCQZH"));
    				xmxx.get(i).put("ZJH",StringHelper.isEmpty(map.get("ZJH"))?xmxx.get(i).get("ZJH"):xmxx.get(i).get("ZJH")+","+map.get("ZJH"));
					break;
			}
		  }
        }

        r.setRows(xmxx);
        r.setSuccess("true");
        r.setMsg("true");
        }else{
        r.setRows(xmxx);
        r.setSuccess("查询失败");
        r.setMsg("false");
        }
        return r;
    }

    public void sharesearchqlr(String xmbh) {
        String shxmbh = xmbh.substring(xmbh.indexOf("@"), xmbh.length() - 1);
        List<Map> qlrmap = get_sharesearchData("select * from SHARESEARCH.T_HOLDER t where xmbh=" + shxmbh + "");

    }
    public List<Map>  modeldetalis(String xl,String xmbh, String modify){
    	List<Map> data= get_sharesearchData("select XMBH, MLMC MATERIAL_NAME,MATERIAL_COUNT NUM,MATERIAL_TYPE,MATERIAL_ID  from sharesearch.T_CATALOG t where t.xmbh = '"+xmbh+"' ");
    	for (int i = 0; i < data.size(); i++) {
    		data.get(i).put("COUNT", JH_DBHelper.get_sharesearchCount("select count(1) from sharesearch.T_CATALOG t inner join sharesearch.t_file f on f.xmbh=t.xmbh and f.mlid=t.material_id where t.xmbh = '"+xmbh+"' and t.material_id='"+data.get(i).get("MATERIAL_ID")+"' "));
		}
    	return data;
	}
       
    public Page fjylpage(String pagein,String id,String xmbh,int count){
		int pageIndex=Integer.parseInt(pagein);
		Page page=new Page();
		List<Map> promater=promater= get_sharesearchData("SELECT * FROM (SELECT tt.*, ROWNUM AS rowno FROM (select ID from SHARESEARCH.t_file where xmbh='"+xmbh+"' and mlid='"+id+"')tt WHERE ROWNUM <("+pageIndex+"+1)) a WHERE a.rowno >= "+pageIndex+"");
		page.setData(promater);
		page.setTotalCount(count);
		return page;
	}
	public void showImg(String id,HttpServletResponse response){
		response.setContentType("img/jpeg");  
        response.setCharacterEncoding("utf-8");  
      Connection conn = JH_DBHelper.getConnect_sharesearch();
      ResultSet excuteQuery = null;
      InputStream in=null;
      try {
    	  OutputStream outputStream=response.getOutputStream();  
          excuteQuery = JH_DBHelper.excuteQuery(conn, "select WJNR from SHARESEARCH.T_FILE where id='"+id+"'");
          if (excuteQuery != null) {
              if (excuteQuery.next()) {
            	  //单张图片
            	  //excuteQuery.getMetaData().getColumnCount()
            	  in=excuteQuery.getBlob(1).getBinaryStream();
              }
              int len=0;  
              byte[]buf=new byte[1024];  
              while((len=in.read(buf,0,1024))!=-1){  
                  outputStream.write(buf, 0, len);  
              }  
              outputStream.close();  
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } catch (IOException e) {
		e.printStackTrace();
	} finally {
          if (excuteQuery != null) {
              try {
                  ResultSet unwrap = excuteQuery.unwrap(ResultSet.class);
                  unwrap.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
          if (conn != null) {
              try {
                  Connection unwrap = conn.unwrap(Connection.class);
                  conn.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
      }
	}
	public Message fjbh(String ywh,String bhly){
		Message msg=new Message();
		try {
			JH_DBHelper.excuteQuery(JH_DBHelper.getConnect_sharesearch(), "update SHARESEARCH.T_PROJECT sh set sh.xmzt='4',sh.remark='"+bhly+"'where sh.XMBH='"+ywh+"'");
		} catch (SQLException e) {
			e.printStackTrace();
			msg.setMsg("驳回失败");
			return msg;
		}
		msg.setMsg("驳回成功");
		return msg;
	}
   public void addDataToSet(Object obj,Set set){
	   if(StringHelper.isEmpty(obj)){
		   set.add("'1111111'");
	   }else{
		   set.add("'"+obj+"'");
	   }
   }
   public void addDataToXmxx(String name,String putName,Map map,Map imageMap){
	   if(map.get(name).equals(imageMap.get(name))){
		   imageMap.put(putName, map.get(putName));
	   }else{
		   imageMap.put(putName, "");
	   }
   }
   public  String valueOf(Object obj) {
	   return StringHelper.isEmpty(obj) ? "" : obj.toString();
   }
}