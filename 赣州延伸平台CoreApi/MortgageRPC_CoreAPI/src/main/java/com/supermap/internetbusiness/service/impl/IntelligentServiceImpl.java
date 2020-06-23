package com.supermap.internetbusiness.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.internetbusiness.model.INTELLIGENT;
import com.supermap.internetbusiness.service.IntelligentService;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.check.CheckEngine;
import com.supermap.realestate.registration.check.CheckRule;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

import com.supermap.wisdombusiness.workflow.model.Wfi_ProinstDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("intelligentService")
public class IntelligentServiceImpl implements IntelligentService {

    private final static int CHECK_TRUE = 0,CHECK_FALSE = 1,CHECK_WARNING = 2;

    @Autowired
    CommonDao baseCommonDao;
    @Autowired
    private ProjectService projectService;


    @Override
    public Map<String, Object> createReport(String file_number)  {

        //项目信息
        ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(file_number);
        if (info == null) {
            return null;
        }
        List<Wfi_ProInst> proinst = baseCommonDao.getDataList(Wfi_ProInst.class, "file_number = '" + file_number + "'");
        boolean flag = true;
        String prodef_name = proinst.get(0).getProdef_Name();
        String [] splitprodef_name = prodef_name.split(",");
        String prodef_nameResult = "";
        for(String prodef_names : splitprodef_name){
        	if(flag){
        		prodef_nameResult = prodef_names;
        		flag = false;
        	}else{
        		prodef_nameResult = prodef_nameResult + "-" + prodef_names;
        	}
        }
        String code = info.getBaseworkflowcode();
        String sqrmc = "", bdcdyh = "", bdcqzh = "", zl = "";

        //审批报告json结果集
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("YWLSH",info.getYwlsh());
        jsonObject.put("XMBH",info.getXmbh());
//      jsonObject.put("DJLXMC",info.getDjlxmc());
//      jsonObject.put("QLLXMC",info.getQllxmc());
      jsonObject.put("YWLXMC",prodef_nameResult);
        //获取申请人信息
        List<BDCS_SQR> sqrList = projectService.getSQRList(info.getXmbh());
        JSONArray jsonArray_sqr = new JSONArray();
        int count = 0;
        for (BDCS_SQR sqr : sqrList) {
            if (count == 0) {
                sqrmc = sqr.getSQRXM();
            } else if (count < 2) {
                sqrmc += "," + sqr.getSQRXM();
            } else if (count < 3) {
                sqrmc += "," + sqr.getSQRXM() + " 等" + sqrList.size() + "个";
            }
            count++;
            JSONObject json_sqr = new JSONObject();
            Map<String, Object> sqr_map= transBean2Map(sqr);
            try {
                //申请人校验
                List<Map<String, Object>> checkSQR = CheckSQR(sqr);
                sqr_map.put("check_sqr", checkSQR);
                json_sqr.put("sqr", sqr_map);
                jsonArray_sqr.add(sqr_map);
            } catch (Exception e) {
                json_sqr.put("sqr", sqr_map);
                jsonArray_sqr.add(sqr_map);
                e.printStackTrace();
            }
        }
        jsonObject.put("sqrs", jsonArray_sqr);

        //单元列表
        //获取bdcdyid
        List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,"XMBH='" + info.getXmbh()  +"'");
        if (djdys != null && djdys.size() > 0) {
            String bdcdylx = info.getBdcdylx();
            ConstValue.DJDYLY djdyly = ConstValue.DJDYLY.initFrom("01");
            ConstValue.BDCDYLX bDCDYLX = null;
            if (StringHelper.isEmpty(bdcdylx)) {
                bDCDYLX =ProjectHelper.GetBDCDYLX(info.getXmbh());
            }else {
                bDCDYLX = ConstValue.BDCDYLX.initFrom(bdcdylx);
            }

            JSONArray jsonArray = new JSONArray();
            count = 0;
            for (BDCS_DJDY_GZ djdy : djdys) {
                String bdcdyid = djdy.getBDCDYID();
                // 获取单元信息、权利、附属权利、权利人
                RealUnit unit = UnitTools.loadUnit(bDCDYLX, djdyly, bdcdyid);
                Rights bdcql= RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, info.getXmbh(), djdy.getDJDYID());
                SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
                List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), info.getXmbh());
                Map<String, Object> unit_map= transBean2Map(unit);
                Map<String, Object> ql_map= transBean2Map(bdcql);
                Map<String, Object> fsql_map= transBean2Map(bdcfsql);
                Map<String, Object> qlr_map= transBean2Map(bdcqlrs);
                Map<String, Object> limitInfo = new HashMap<String, Object>();
                if(ConstValue.BDCDYLX.H.equals(bDCDYLX) || ConstValue.BDCDYLX.YCH.equals(bDCDYLX)){
                    limitInfo = getHouseLimitInfo(bdcql,file_number);
                } else if (ConstValue.BDCDYLX.SHYQZD.equals(bDCDYLX) || ConstValue.BDCDYLX.SYQZD.equals(bDCDYLX)) {
                    limitInfo = getLandLimitInfo(bdcql,file_number);
                    //国有土地使用权时，显示出土地上房屋状态
                    addLimitZDStausByFw(limitInfo, bDCDYLX, bdcdyid);
                }

                // 是否网签，附属权利表合同编号判断
                if (!StringHelper.isEmpty(bdcfsql.getHTBH())) {
                    fsql_map.put("SFWQ", "true");
                } else {
                    fsql_map.put("SFWQ", "false");
                }
                // 是否完税，附属权利表完税凭证号判断
                if (!StringHelper.isEmpty(bdcfsql.getWSPZH())) {
                    fsql_map.put("SFWS", "true");
                } else {
                    fsql_map.put("SFWS", "false");
                }

                unit_map.put("ql",ql_map);
                unit_map.put("fsql",fsql_map);
                unit_map.put("qlr",qlr_map);
                unit_map.put("limit",limitInfo);
                jsonArray.add(unit_map);

                if (count == 0) {
                    bdcdyh = unit.getBDCDYH();
                    zl = unit.getZL();
                    bdcqzh = bdcql.getBDCQZH();
                } else if (count < 2) {
                    bdcdyh += " 等" + djdys.size() + "个";
                    zl += " 等" + djdys.size() + "个";
                    bdcqzh += " 等" + djdys.size() + "个";
                }
                count++;
            }

            jsonObject.put("units",jsonArray);

            //登簿约束校验结果集
            List<Map<String, Object>> checks = null;
            if(djdys.size() > 1) {
                checks = checkEX(info);
            }else {
                checks = check(info);
            }
            String[] codes = {"ZY001", "ZY002", "ZY003", "ZY009", "ZY012", "ZY013", "ZY014", "ZY00115", "ZY016", "ZY017", "ZY018", "ZY019", "ZY020", "ZY021", "ZY022", "ZY023", "ZY024", "ZY025", "ZY026", "ZY040", "ZY201"};
            if(Arrays.asList(codes).contains(code)) {
                Map<String, Object> map = ZYYGCanecl(info.getXmbh());
                if (map.size() > 0) {
                    checks.add(map);
                }
            }
            jsonObject.put("bookCheck", checks);
            System.out.println(jsonObject);
        }

        //对校验的数据进行分析
        Map resultData = analyzeData(jsonObject,info);

        //智能审批结果固化存储
        INTELLIGENT intelligent = new INTELLIGENT();
        intelligent.setYwlsh(info.getYwlsh());
        intelligent.setXmbh(info.getXmbh());
        intelligent.setWlsh(info.getWlsh());
        intelligent.setSqr(sqrmc);
        intelligent.setBdcdyh(bdcdyh);
        intelligent.setBdcqzh(bdcqzh);
        intelligent.setZl(zl);
//      intelligent.setProName(info.getDjlxmc()+"-"+info.getQllxmc());
      intelligent.setProName(prodef_nameResult);
        intelligent.setFile_number(file_number);
        //报告类型：1-线下；2-线上
        intelligent.setBglx("1");
        intelligent.setContent(JSONObject.toJSONString(jsonObject));
        intelligent.setShjgxx(JSONObject.toJSONString(resultData));
        intelligent.setShjg(StringHelper.formatObject(resultData.get("Result")));
        intelligent.setCreatedtime(new Date());

        baseCommonDao.saveOrUpdate(intelligent);
        baseCommonDao.flush();

        return resultData;
    }

    /**
     * 分析校验数据，统计出审批报告内容及结果
     * @param jsonObject
     * @param info
     * @return
     */
    public Map analyzeData(JSONObject jsonObject, ProjectInfo info) {
        Map<String,Object> checkResult = new HashMap<String,Object>();

        Map<String,Object> dycheckResult = new HashMap<String,Object>();//单元信息检测结果
        Map<String,Object> sqrcheckResult = new HashMap<String,Object>();//申请人信息检测结果
        Map<String,Object> dbcheckResult = new HashMap<String,Object>();//登簿约束检测结果
        Map<String,Object> taxcheckResult = new HashMap<String,Object>();//缴税情况
        Map<String,Object> wqcheckResult = new HashMap<String,Object>();//网签情况

        //记录各种状态数：通过，警告，严重警告
        int check_true_num = 0,check_false_num = 0,check_warning_num = 0;
        String djlx = info.getDjlx();
        String baseworkflowcode = info.getBaseworkflowcode();

        //单元的信息统计，暂时只统计抵押，查封，异议，限制等问题数据
        String units = jsonObject.getString("units");
        if(!StringHelper.isEmpty(units)){
            List<Map<String,Object>> dylist = new ArrayList<Map<String, Object>>();
            List<Map<String,Object>> cflist = new ArrayList<Map<String, Object>>();
            List<Map<String,Object>> xzlist = new ArrayList<Map<String, Object>>();
            List<Map<String,Object>> yylist = new ArrayList<Map<String, Object>>();
            List<Map<String,Object>> wqlist = new ArrayList<Map<String, Object>>();
            List<Map<String,Object>> taxlist = new ArrayList<Map<String, Object>>();
            JSONArray unitArray = JSONArray.parseArray(units);
            for(int i=0;i<unitArray.size();i++) {
                JSONObject unitmsg = JSONObject.parseObject(StringHelper.formatObject(unitArray.get(i)));
//                JSONObject unitmsg = JSONObject.parseObject(unit.getString("unit"));
                String zl = unitmsg.getString("ZL");
                //限制状态集合
                JSONObject limit = JSONObject.parseObject(unitmsg.getString("limit"));
                String DYZTCODE = limit.getString("DYZTCODE");
                String CFZTCODE = limit.getString("CFZTCODE");
                String YYZTCODE = limit.getString("YYZTCODE");
                String XZZTCODE = limit.getString("YYZTCODE");
                //code为0 则没有限制，为1则是有限制， 为2则是正在办理限制
                if(!StringHelper.isEmpty(DYZTCODE) && !"0".equals(DYZTCODE)) {
                    Map<String,Object> msg = new HashMap<String,Object>();
                    msg.put("zl",zl);
                    if("ZY007".equals(baseworkflowcode)){
                        msg.put("dyztcode","0");
                        msg.put("check_code", CHECK_TRUE);
                        msg.put("dyzt", limit.getString("DYZT"));
                        dylist.add(msg);
                    } else {
                        msg.put("dyztcode",DYZTCODE);
                        msg.put("check_code", CHECK_FALSE);
                        msg.put("dyzt", limit.getString("DYZT"));
                        dylist.add(msg);
                        check_false_num++;
                    }
                }
                if(!StringHelper.isEmpty(CFZTCODE) &&!"0".equals(CFZTCODE)) {
                    Map<String,Object> msg = new HashMap<String,Object>();
                    msg.put("zl",zl);
                    msg.put("cfztcode",CFZTCODE);
                    msg.put("check_code", CHECK_FALSE);
                    msg.put("cfzt", limit.getString("CFZT"));
                    cflist.add(msg);
                    check_false_num++;
                }
                if(!StringHelper.isEmpty(XZZTCODE) &&!"0".equals(XZZTCODE)) {
                    Map<String,Object> msg = new HashMap<String,Object>();
                    msg.put("zl",zl);
                    msg.put("xzztcode",XZZTCODE);
                    msg.put("check_code", CHECK_FALSE);
                    msg.put("xzzt", limit.getString("XZZT"));
                    xzlist.add(msg);
                    check_false_num++;
                }
                if(!StringHelper.isEmpty(YYZTCODE) &&!"0".equals(YYZTCODE)) {
                    Map<String,Object> msg = new HashMap<String,Object>();
                    msg.put("zl",zl);
                    msg.put("yyztcode",YYZTCODE);
                    msg.put("check_code", CHECK_FALSE);
                    msg.put("yyzt", limit.getString("YYZT"));
                    msg.put("msg", "该单元存在异议，请检查是否上传相关异议材料！");
                    yylist.add(msg);
                    check_false_num++;
                }
                if("200".equals(djlx)) {
                    //网签、完税状态集合
                    JSONObject fsql = JSONObject.parseObject(unitmsg.getString("fsql"));
                    String sfwq = fsql.getString("SFWQ");
                    String sfws = fsql.getString("SFWS");
                    if (!"true".equals(sfwq)) {
                        Map<String,Object> msg = new HashMap<String,Object>();
                        msg.put("zl",zl);
                        msg.put("sfwq",sfwq);
                        msg.put("check_code", CHECK_WARNING);
                        List<Wfi_ProinstDate> wfi_pro = baseCommonDao.getDataList(Wfi_ProinstDate.class, " FILE_NUMBER='" + info.getProject_id() + "' and NETAUDITOPINIONS is not null  ");
                        if(wfi_pro.isEmpty()) {
                            msg.put("msg", "该单元未网签！");
                        } else {
                            msg.put("nonsupport","true");
                            msg.put("msg",wfi_pro.get(0).getNetAuditOpinions());
                        }
                        wqlist.add(msg);
                        check_warning_num++;
                    }
                    if (!"true".equals(sfws)) {
                        Map<String,Object> msg = new HashMap<String,Object>();
                        msg.put("zl",zl);
                        msg.put("sfws",sfws);
                        msg.put("check_code", CHECK_FALSE);
                        msg.put("msg", "该单元未完税！");
                        taxlist.add(msg);
                        check_false_num++;
                    }
                }
            }
            dycheckResult.put("bdcsize", unitArray.size());
            dycheckResult.put("dylist", dylist);
            dycheckResult.put("cflist", cflist);
            dycheckResult.put("xzlist", xzlist);
            dycheckResult.put("yylist", yylist);
            wqcheckResult.put("wqlist", wqlist);
            taxcheckResult.put("taxlist", taxlist);
            if(taxlist.isEmpty()) {
                List<Wfi_ProinstDate> wfi_pro = baseCommonDao.getDataList(Wfi_ProinstDate.class, " FILE_NUMBER='" + info.getProject_id() + "' ");
                if(!wfi_pro.isEmpty()) {
                    Date taxConfirmTime = wfi_pro.get(0).getTaxConfirmTime();
                    if(taxConfirmTime!=null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String taxtime = sdf.format(taxConfirmTime);
                        taxcheckResult.put("taxtime", taxtime);
                    }
                }
            }
        }

        //申请人信息统计，统计校验了多少个申请人，是否都通过了公安厅和工商局的校验
        String sqrs = jsonObject.getString("sqrs");
        if(!StringHelper.isEmpty(sqrs)){
            JSONArray sqrArray = JSONArray.parseArray(sqrs);
            List<Map> checktrue = new ArrayList<Map>();
            List<Map> checkwarning = new ArrayList<Map>();
            for(int i=0;i<sqrArray.size();i++) {
                JSONObject sqr = JSONObject.parseObject(StringHelper.formatObject(sqrArray.get(i)));
                //申请人调用公安工商接口校验，暂时没做，先固定值
                Object check_sqr = sqr.get("check_sqr");
                String sqrname = sqr.getString("SQRXM");
                String sqrzjh = sqr.getString("ZJH");
                if (!StringHelper.isEmpty(check_sqr)) {
                    List<Map> list = (List<Map>)check_sqr;
                    JSONArray Array = JSONArray.parseArray(StringHelper.formatObject(JSONObject.toJSON(list)));
                    for (int j = 0; j < Array.size(); j++) {
                        Map<String, Object> nopass = new HashMap<String, Object>();
                        JSONObject checksqr = JSONObject.parseObject(StringHelper.formatObject(Array.get(j)));
                        //警告
                        if ("2".equals(checksqr.getString("check_code"))) {
                            nopass.put("check_code", checksqr.getString("check_code"));
                            nopass.put("check_msg", checksqr.getString("check_msg"));
                            checkwarning.add(nopass);
                        }
                        //通过
                        if ("0".equals(checksqr.getString("check_code"))) {
                            nopass.put("check_code", checksqr.getString("check_code"));
                            nopass.put("check_msg", checksqr.getString("check_msg"));
                            checktrue.add(nopass);
                        }
                    }
                }
            }
            sqrcheckResult.put("sqrsize",checktrue.size() + checkwarning.size());
            sqrcheckResult.put("checktrue",checktrue);
            sqrcheckResult.put("checkwarning",checkwarning);
        }

        //登簿约束校验，分别存储严重，警告两种校验结果即可
        Object bookChecks = jsonObject.get("bookCheck");
        if(!StringHelper.isEmpty(bookChecks)){
            List<Map> bclist = (List<Map>)bookChecks;
            JSONArray bcArray = JSONArray.parseArray(StringHelper.formatObject(JSONObject.toJSON(bclist)));
            List<Map> checkfalse = new ArrayList<Map>();
            List<Map> checkwarning = new ArrayList<Map>();

            for(int i=0;i<bcArray.size();i++) {
                JSONObject checkrule = JSONObject.parseObject(StringHelper.formatObject(bcArray.get(i)));
                Map rule = new HashMap();
                if("1".equals(checkrule.getString("checklevel")) && "false".equals(checkrule.getString("state"))) {
                    //严重登簿约束
                    rule.put("name", checkrule.getString("resulttip"));
                    rule.put("level", checkrule.getString("checklevel"));
                    rule.put("state", checkrule.getString("state"));
                    checkfalse.add(rule);
                    check_false_num++;
                }
                if("2".equals(checkrule.getString("checklevel")) && "false".equals(checkrule.getString("state"))) {
                    //警告登簿约束
                    rule.put("name", checkrule.getString("resulttip"));
                    rule.put("level", checkrule.getString("checklevel"));
                    rule.put("state", checkrule.getString("state"));
                    checkwarning.add(rule);
                    check_warning_num++;
                }
            }

            dbcheckResult.put("rulesize", bcArray.size());
            dbcheckResult.put("checkfalse", checkfalse);
            dbcheckResult.put("checkwarning", checkwarning);
        }

        checkResult.put("dycheckResult", dycheckResult);
        checkResult.put("sqrcheckResult", sqrcheckResult);
        checkResult.put("dbcheckResult", dbcheckResult);
        if("200".equals(djlx)) {
            checkResult.put("taxcheckResult", taxcheckResult);
            checkResult.put("wqcheckResult", wqcheckResult);
        }

        //有一条严重则整个结果都为严重，其次到警告
        if(check_false_num>0) {
            checkResult.put("Result", "false");
        } else if(check_warning_num>0) {
            checkResult.put("Result", "warning");
        } else {
            checkResult.put("Result", "true");
        }
        System.out.println(checkResult);
        return checkResult;
    }

    //*********************************************房屋限制状态 ↓*********************************************

    /**
     * 获取房屋权利限制信息：抵押、查封、异议
     * @param ql
     * @param file_number
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> getHouseLimitInfo(Rights ql, String file_number) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ql != null) {
            String djdyid = ql.getDJDYID();
            if (!StringHelper.isEmpty(djdyid)) {
                String ycdjdyid = "";
                String scdjdyid = "";
                List<BDCS_DJDY_XZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
                        + djdyid + "'");
                if (listdjdy != null && listdjdy.size() > 0) {
                    ConstValue.BDCDYLX lx = ConstValue.BDCDYLX.initFrom(listdjdy.get(0).getBDCDYLX());
                    if (lx != null) {
                        if (lx.Value.equals("031")) {
                            String valuedyxz = "";
                            String valuecfxz = "";
                            String valueyyxz = "";
                            String valuexzxz = "";
                            String valueygxz = "";

                            String dyztcode = "0";
                            String cfztcode = "0";
                            String xzztcode = "0";
                            String yyztcode = "0";
                            String dyztcode_qf = "0";
                            String cfztcode_qf = "0";
                            String xzztcode_qf = "0";
                            String yyztcode_qf = "0";

                            List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
                                    "scbdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
                            if (listycsc != null && listycsc.size() > 0
                                    && listycsc.get(0).getYCBDCDYID() != null) {
                                List<BDCS_DJDY_XZ> listdjdyyc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
                                        "BDCDYID='" + listycsc.get(0).getYCBDCDYID() + "'");
                                if (listdjdyyc != null && listdjdyyc.size() > 0) {
                                    ycdjdyid = listdjdyyc.get(0).getDJDYID();
                                    Map<String, String> mapxzy = new HashMap<String, String>();
                                    mapxzy = getDYandCFandYY_XZY(ycdjdyid);
                                    for (Map.Entry<String, String> ent : mapxzy.entrySet()) {
                                        String name = ent.getKey();
                                        if (name.equals("DYZT")) {
                                            if (StringHelper.isEmpty(valuedyxz)) {
                                                valuedyxz = ent.getValue();
                                            } else {
                                                valuedyxz = valuedyxz + "、" + ent.getValue();
                                            }
                                        } else if (name.equals("CFZT")) {
                                            if (StringHelper.isEmpty(valuecfxz)) {
                                                valuecfxz = ent.getValue();
                                            } else {
                                                valuecfxz = valuecfxz + "、" + ent.getValue();
                                            }
                                        } else if (name.equals("YYZT")){
                                            if (StringHelper.isEmpty(valueyyxz)) {
                                                valueyyxz = ent.getValue();
                                            } else {
                                                valueyyxz = valueyyxz + "、" + ent.getValue();
                                            }
                                        }else if (name.equals("XZZT")){
                                            if (StringHelper.isEmpty(valuexzxz)) {
                                                valuexzxz = ent.getValue();
                                            } else {
                                                valuexzxz = valuexzxz + "、" + ent.getValue();
                                            }
                                        }else if (name.equals("DYZTCODE")){
                                            dyztcode_qf = ent.getValue();
                                        }else if (name.equals("CFZTCODE")){
                                            cfztcode_qf = ent.getValue();
                                        }else if (name.equals("XZZTCODE")){
                                            xzztcode_qf = ent.getValue();
                                        }else if (name.equals("YYZTCODE")){
                                            yyztcode_qf = ent.getValue();
                                        }
                                    }
                                }
                            }
                            Map<String, String> mapxz = new HashMap<String, String>();
                            mapxz = getDYandCFandYY_XZ(djdyid,file_number);
                            for (Map.Entry<String, String> ent : mapxz.entrySet()) {
                                String name = ent.getKey();
                                if (name.equals("DYZT")) {
                                    if (StringHelper.isEmpty(valuedyxz)) {
                                        valuedyxz = ent.getValue();
                                    } else {
                                        valuedyxz = valuedyxz + " " + ent.getValue();
                                    }
                                } else if (name.equals("CFZT")) {
                                    if (StringHelper.isEmpty(valuecfxz)) {
                                        valuecfxz = ent.getValue();
                                    } else {
                                        valuecfxz = valuecfxz + " " + ent.getValue();
                                    }
                                } else if (name.equals("YYZT")){
                                    if (StringHelper.isEmpty(valueyyxz)) {
                                        valueyyxz = ent.getValue();
                                    } else {
                                        valueyyxz = valueyyxz + " " + ent.getValue();
                                    }
                                }else if (name.equals("YGDJZT")){
                                    if (StringHelper.isEmpty(valueyyxz)) {
                                        valueygxz = ent.getValue();
                                    } else {
                                        valueygxz = valueygxz + " "
                                                + ent.getValue();
                                    }
                                }else if (name.equals("XZZT")){
                                    if (StringHelper.isEmpty(valuexzxz)) {
                                        valuexzxz = ent.getValue();
                                    } else {
                                        valuexzxz = valuexzxz + " " + ent.getValue();
                                    }
                                }else if (name.equals("DYZTCODE")){
                                    dyztcode = ent.getValue();
                                }else if (name.equals("CFZTCODE")){
                                    cfztcode = ent.getValue();
                                }else if (name.equals("XZZTCODE")){
                                    xzztcode = ent.getValue();
                                }else if (name.equals("YYZTCODE")){
                                    yyztcode = ent.getValue();
                                }
                            }
                            map.put("DYZT", valuedyxz);
                            map.put("CFZT", valuecfxz);
                            map.put("YYZT", valueyyxz);
                            map.put("XZZT", valuexzxz);
                            map.put("YGDJZT", valueygxz);

                            map.put("DYZTCODE", "0");
                            map.put("CFZTCODE", "0");
                            map.put("YYZTCODE", "0");
                            map.put("XZZTCODE", "0");
                            if (!"0".equals(dyztcode) || !"0".equals(dyztcode_qf)) {
                                map.put("DYZTCODE", "1");
                            }
                            if (!"0".equals(cfztcode) || !"0".equals(cfztcode_qf)) {
                                map.put("CFZTCODE", "1");
                            }
                            if (!"0".equals(yyztcode) || !"0".equals(yyztcode_qf)) {
                                map.put("YYZTCODE", "1");
                            }
                            if (!"0".equals(xzztcode) || !"0".equals(xzztcode_qf)) {
                                map.put("XZZTCODE", "1");
                            }

                        } else if (lx.Value.equals("032")) {
                            List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
                                    "ycbdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
                            String valuedyxz = "";
                            String valuecfxz = "";
                            String valueyyxz = "";
                            String valuexzxz = "";
                            String valueygxz = "";//不动产权利查档时需要查预告登记

                            String dyztcode = "0";
                            String cfztcode = "0";
                            String xzztcode = "0";
                            String yyztcode = "0";
                            String dyztcode_qf = "0";
                            String cfztcode_qf = "0";
                            String xzztcode_qf = "0";
                            String yyztcode_qf = "0";

                            if (listycsc != null && listycsc.size() > 0 && listycsc.get(0).getSCBDCDYID() != null) {
                                List<BDCS_DJDY_XZ> listdjdysc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
                                        "BDCDYID='" + listycsc.get(0).getSCBDCDYID() + "'");
                                if (listdjdysc != null && listdjdysc.size() > 0) {
                                    scdjdyid = listdjdysc.get(0).getDJDYID();
                                    Map<String, String> mapxz = new HashMap<String, String>();
                                    mapxz = getDYandCFandYY_XZ(scdjdyid,file_number);
                                    for (Map.Entry<String, String> ent : mapxz.entrySet()) {
                                        String name = ent.getKey();
                                        if (name.equals("DYZT")) {
                                            if (StringHelper.isEmpty(valuedyxz)) {
                                                valuedyxz = ent.getValue();
                                            } else {
                                                valuedyxz = valuedyxz + " " + ent.getValue();
                                            }
                                        } else if (name.equals("CFZT")) {
                                            if (StringHelper.isEmpty(valuecfxz)) {
                                                valuecfxz = ent.getValue();
                                            } else {
                                                valuecfxz = valuecfxz + " " + ent.getValue();
                                            }
                                        } else  if (name.equals("YYZT")) {
                                            if (StringHelper.isEmpty(valueyyxz)) {
                                                valueyyxz = ent.getValue();
                                            } else {
                                                valueyyxz = valueyyxz + " " + ent.getValue();
                                            }
                                        }else if (name.equals("XZZT")){
                                            if (StringHelper.isEmpty(valuexzxz)) {
                                                valuexzxz = ent.getValue();
                                            } else {
                                                valuexzxz = valuexzxz + "、" + ent.getValue();
                                            }
                                        }else if (name.equals("DYZTCODE")){
                                            dyztcode = ent.getValue();
                                        }else if (name.equals("CFZTCODE")){
                                            cfztcode = ent.getValue();
                                        }else if (name.equals("XZZTCODE")){
                                            xzztcode = ent.getValue();
                                        }else if (name.equals("YYZTCODE")){
                                            yyztcode = ent.getValue();
                                        }
                                    }
                                }
                            }
                            Map<String, String> mapxzy = new HashMap<String, String>();
                            mapxzy = getDYandCFandYY_XZY(djdyid);
                            for (Map.Entry<String, String> ent : mapxzy.entrySet()) {
                                String name = ent.getKey();
                                if (name.equals("DYZT")) {
                                    if (StringHelper.isEmpty(valuedyxz)) {
                                        valuedyxz = ent.getValue();
                                    } else {
                                        valuedyxz = valuedyxz + " " + ent.getValue();
                                    }
                                } else if (name.equals("CFZT")) {
                                    if (StringHelper.isEmpty(valuecfxz)) {
                                        valuecfxz = ent.getValue();
                                    } else {
                                        valuecfxz = valuecfxz + " " + ent.getValue();
                                    }
                                } else  if (name.equals("YYZT")) {
                                    if (StringHelper.isEmpty(valueyyxz)) {
                                        valueyyxz = ent.getValue();
                                    } else {
                                        valueyyxz = valueyyxz + " " + ent.getValue();
                                    }
                                }else  if (name.equals("YGDJZT")) {
                                    if (StringHelper.isEmpty(valueyyxz)) {
                                        valueygxz = ent.getValue();
                                    } else {
                                        valueygxz = valueygxz + " "
                                                + ent.getValue();
                                    }
                                }else if (name.equals("XZZT")){
                                    if (StringHelper.isEmpty(valuexzxz)) {
                                        valuexzxz = ent.getValue();
                                    } else {
                                        valuexzxz = valuexzxz + " " + ent.getValue();
                                    }
                                }else if (name.equals("DYZTCODE")){
                                    dyztcode_qf = ent.getValue();
                                }else if (name.equals("CFZTCODE")){
                                    cfztcode_qf = ent.getValue();
                                }else if (name.equals("XZZTCODE")){
                                    xzztcode_qf = ent.getValue();
                                }else if (name.equals("YYZTCODE")){
                                    yyztcode_qf = ent.getValue();
                                }
                            }
                            map.put("DYZT", valuedyxz);
                            map.put("CFZT", valuecfxz);
                            map.put("YYZT", valueyyxz);
                            map.put("XZZT", valuexzxz);
                            map.put("YGDJZT", valueygxz);

                            map.put("DYZTCODE", "0");
                            map.put("CFZTCODE", "0");
                            map.put("YYZTCODE", "0");
                            map.put("XZZTCODE", "0");
                            if (!"0".equals(dyztcode) || !"0".equals(dyztcode_qf)) {
                                map.put("DYZTCODE", "1");
                            }
                            if (!"0".equals(cfztcode) || !"0".equals(cfztcode_qf)) {
                                map.put("CFZTCODE", "1");
                            }
                            if (!"0".equals(yyztcode) || !"0".equals(yyztcode_qf)) {
                                map.put("YYZTCODE", "1");
                            }
                            if (!"0".equals(xzztcode) || !"0".equals(xzztcode_qf)) {
                                map.put("XZZTCODE", "1");
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    public Map<String, String> getDYandCFandYY_XZY(String djdyid) {
        List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
        String bdcdyid = null;
        if (dy != null && dy.size() > 0) {
            bdcdyid = dy.get(0).getBDCDYID();
        }
        Map<String, String> map = new HashMap<String, String>();
        String sqlMortgage = MessageFormat.format(
                " from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
                djdyid);
        String sqlSeal = MessageFormat
                .format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
                        djdyid);
        String sqlObjection = MessageFormat
                .format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
                        djdyid);
        String sqlLimit = MessageFormat
                .format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
                        bdcdyid);
        String qfygdj = MessageFormat
                .format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
                        djdyid);

        long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
        long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
        long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
        long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
        long qfygdjCount = baseCommonDao.getCountByFullSql(qfygdj)/2;//查预告登记

        String sealStatus = "";
        String mortgageStatus = "";
        String LimitStatus = "";
        String objectionStatus = "";
        String ygdjStatus = "";

        mortgageStatus = mortgageCount > 0 ? "期房已抵押" : "期房无抵押";
        sealStatus = SealCount > 0 ? "期房已查封" : "期房无查封";
        LimitStatus = LimitCount > 0 ? "期房已限制" : "期房无限制";
        objectionStatus = ObjectionCount > 0 ? "期房有异议" : "期房无异议";
        ygdjStatus = qfygdjCount > 0 ? "期房已预告登记" : "期房无预告登记";

        String DYZTCODE = "";
        String CFZTCODE = "";
        String XZZTCODE = "";
        String YYZTCODE = "";
        String YGDJZTCODE = "";

        DYZTCODE = mortgageCount > 0 ? "1" : "0";
        CFZTCODE = SealCount > 0 ? "1" : "0";
        XZZTCODE = LimitCount > 0 ? "1" : "0";
        YYZTCODE = ObjectionCount > 0 ? "1" : "0";
        YGDJZTCODE = qfygdjCount > 0 ? "1" : "0";

        // 判断完现状层中的查封信息，接着判断办理中的查封信息
        if (!(SealCount > 0)) {
            String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
                    + djdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlSealing);
            sealStatus = count > 0 ? "期房查封办理中" : "期房无查封";
            CFZTCODE = count > 0 ? "2" : "0";
        }

        if (!(LimitCount > 0)) {
            String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
                    + bdcdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlLimiting);
            LimitStatus = count > 0 ? "期房限制办理中" : "期房无限制";
            XZZTCODE = count > 0 ? "2" : "0";
        }

        // 判断完现状层中的抵押信息，接着判断办理中的抵押信息
        if (!(mortgageCount > 0)) {
            String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
                    + djdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
            mortgageStatus = count > 0 ? "期房抵押办理中" : "期房无抵押";
            DYZTCODE = count > 0 ? "2" : "0";
        }

        if (!(qfygdjCount > 0)) {
            String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' AND c.qllx='4' and c.djdyid='"
                    + djdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
            ygdjStatus = count > 0 ? "期房预告登记办理中" : "期房无办理中的预告登记";
            YGDJZTCODE = count > 0 ? "2" : "0";
        }


        map.put("DYZTCODE", DYZTCODE);
        map.put("CFZTCODE", CFZTCODE);
        map.put("XZZTCODE", XZZTCODE);
        map.put("YYZTCODE", YYZTCODE);
        map.put("YGDJZTCODE", YGDJZTCODE);
        map.put("DYZT", mortgageStatus);
        map.put("CFZT", sealStatus);
        map.put("YYZT", objectionStatus);
        map.put("XZZT", LimitStatus);
        map.put("YGDJZT", ygdjStatus);

        return map;

    }

    public Map<String, String> getDYandCFandYY_XZ(String djdyid,String file_number) {
        List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
        String bdcdyid = null;
        if (dy != null && dy.size() > 0) {
            bdcdyid = dy.get(0).getBDCDYID();
        }
        Map<String, String> map = new HashMap<String, String>();
        String sqlMortgage = MessageFormat.format(
                " from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
                djdyid);
        String sqlSeal = MessageFormat
                .format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
                        djdyid);
        String sqlObjection = MessageFormat
                .format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
                        djdyid);
        String sqlYgdj = MessageFormat
                .format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
                        djdyid);//查预告登记

        String sqlLimit = MessageFormat
                .format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
                        bdcdyid);

        long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
        long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
        long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
        long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
        long ygdjCount = baseCommonDao.getCountByFullSql(sqlYgdj);

        String sealStatus = "";
        String mortgageStatus = "";
        String ygdjStatus = "";
        String LimitStatus = "";
        String objectionStatus = "";

        mortgageStatus = mortgageCount > 0 ? "现房已抵押" : "现房无抵押";
        sealStatus = SealCount > 0 ? "现房已查封" : "现房无查封";
        LimitStatus = LimitCount > 0 ? "现房已限制" : "现房无限制";
        ygdjStatus = ygdjCount > 0 ? "现房已预告登记" : "现房无预告登记";
        objectionStatus = ObjectionCount > 0 ? "现房有异议" : "现房无异议";

        String DYZTCODE = "";
        String CFZTCODE = "";
        String XZZTCODE = "";
        String YYZTCODE = "";
        String YGDJZTCODE = "";

        DYZTCODE = mortgageCount > 0 ? "1" : "0";
        CFZTCODE = SealCount > 0 ? "1" : "0";
        XZZTCODE = LimitCount > 0 ? "1" : "0";
        YYZTCODE = ObjectionCount > 0 ? "1" : "0";
        YGDJZTCODE = ygdjCount > 0 ? "1" : "0";


        // 判断完现状层中的查封信息，接着判断办理中的查封信息
        if (!(SealCount > 0)) {
            String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
                    + djdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlSealing);
            sealStatus = count > 0 ? "现房查封办理中" : "现房无查封";
            CFZTCODE = count > 0 ? "2" : "0";
        }

        // 改为判断完查封 人后判断限制
        if (!(LimitCount > 0)) {
            String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
                    + bdcdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlLimiting);
            LimitStatus = count > 0 ? "现房限制办理中" : "现房无限制";
            XZZTCODE = count > 0 ? "2" : "0";
        }

        // 判断完现状层中的查封信息，接着判断办理中的查封信息
        if (!(mortgageCount > 0)) {
            String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
                    + djdyid + "' and a.sfdb='0' and a.PROJECT_ID <> '"+file_number+"' ";
            long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
            mortgageStatus = count > 0 ? "现房抵押办理中" : "现房无抵押";
            DYZTCODE = count > 0 ? "2" : "0";
        }

        // 预告登记
        if (!(ygdjCount > 0)) {
            String sqlygdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' and c.djdyid='"
                    + djdyid + "' and a.sfdb='0' ";
            long count = baseCommonDao.getCountByFullSql(sqlygdj);
            ygdjStatus = count > 0 ? "现房预告登记中" : "现房无预告登记中";
            YGDJZTCODE = count > 0 ? "2" : "0";
        }

        map.put("DYZTCODE", DYZTCODE);
        map.put("CFZTCODE", CFZTCODE);
        map.put("XZZTCODE", XZZTCODE);
        map.put("YYZTCODE", YYZTCODE);
        map.put("YGDJZTCODE", YGDJZTCODE);
        map.put("DYZT", mortgageStatus);
        map.put("CFZT", sealStatus);
        map.put("YYZT", objectionStatus);
        map.put("XZZT", LimitStatus);
        map.put("YGDJZT", ygdjStatus);

        return map;
    }

    //*********************************************房屋限制状态 ↑*********************************************

    //*********************************************土地限制状态 ↓*********************************************

    /**
     * 获取土地权利限制信息：抵押、查封、异议
     * @param ql
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> getLandLimitInfo(Rights ql,String file_number) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ql != null) {
            String djdyid = ql.getDJDYID();
            if (!StringHelper.isEmpty(djdyid)) {
                String sqlMortgage = MessageFormat
                        .format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
                                djdyid);
                String sqlSeal = MessageFormat
                        .format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
                                djdyid);
                String sqlObjection = MessageFormat
                        .format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
                                djdyid);
                String sqlLimit = MessageFormat.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
                        map.get("BDCDYID"));
                long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
                long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
                long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
                long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);

                String mortgageStatus = mortgageCount > 0 ? "土地已抵押" : "土地无抵押";
                String sealStatus = SealCount > 0 ? "土地已查封" : "土地无查封";
                String LimitStatus = LimitCount > 0 ? "土地已限制" : "土地无限制";
                String objectionStatus = ObjectionCount > 0 ? "土地有异议" : "土地无异议";

                String DYZTCODE = "";
                String CFZTCODE = "";
                String XZZTCODE = "";
                String YYZTCODE = "";

                DYZTCODE = mortgageCount > 0 ? "1" : "0";
                CFZTCODE = SealCount > 0 ? "1" : "0";
                XZZTCODE = LimitCount > 0 ? "1" : "0";
                YYZTCODE = ObjectionCount > 0 ? "1" : "0";

                // 判断完现状层中的查封信息，接着判断办理中的查封信息
                if (!(SealCount > 0)) {
                    String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
                            + djdyid + "' and a.sfdb='0' ";
                    long count = baseCommonDao
                            .getCountByFullSql(sqlSealing);
                    sealStatus = count > 0 ? "土地查封办理中" : "土地无查封";
                }

                // 判断完现状层中的抵押信息，接着判断办理中的抵押信息
                if (!(mortgageCount > 0)) {
                    String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
                            + djdyid + "' and a.sfdb='0' and a.PROJECT_ID<>'"+file_number+"' ";
                    long count = baseCommonDao
                            .getCountByFullSql(sqlmortgageing);
                    mortgageStatus = count > 0 ? "土地抵押办理中" : "土地无抵押";
                }

                // 改为判断完查封 后判断限制
                if (!(LimitCount > 0)) {
                    String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
                            + map.get("BDCDYID") + "' and a.sfdb='0' ";
                    long countxz = baseCommonDao.getCountByFullSql(sqlLimiting);
                    LimitStatus = countxz > 0 ? "土地限制办理中" : "土地无限制";
                }
                map.put("DYZT", mortgageStatus);
                map.put("CFZT", sealStatus);
                map.put("YYZT", objectionStatus);
                map.put("XZZT", LimitStatus);
                map.put("DYZTCODE", DYZTCODE);
                map.put("CFZTCODE", CFZTCODE);
                map.put("YYZTCODE", YYZTCODE);
                map.put("XZZTCODE", XZZTCODE);
            }
        }
        return map;
    }

    /**
     * 国有土地使用权时，添加土地上房屋状态。
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> addLimitZDStausByFw(Map<String, Object> map, ConstValue.BDCDYLX bDCDYLX, String zdbdcdyid) {
        long mortgageCount = 0,mortgagingCount=0;
        List<String> lstmortgage=new ArrayList<String>();
        List<String> lstmortgageing=new ArrayList<String>();
        long SealCount = 0,SealingCount=0;
        List<String> lstseal=new ArrayList<String>();
        List<String> lstsealing=new ArrayList<String>();
        long ObjectionCount = 0,ObjectioningCount=0;
        List<String> lstObjection=new ArrayList<String>();
        List<String> lstObjectioning=new ArrayList<String>();
        long LimitCount = 0,LimitingCount=0;
        long housecount = 0;
        //只有使用权宗地才有房屋
        if(ConstValue.BDCDYLX.SHYQZD.equals(bDCDYLX) && !StringHelper.isEmpty(zdbdcdyid)){
            //已办理的业务
            StringBuilder strdealed=new StringBuilder();
            strdealed.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID FROM  ( ");
            strdealed.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
            strdealed.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
            strdealed.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX ");
            strdealed.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
            strdealed.append("WHERE QL.QLID IS NOT NULL AND DY.ZDBDCDYID='");
            strdealed.append(zdbdcdyid).append("'");
            String dealedSql=strdealed.toString();
            List<Map> dealedmap=baseCommonDao.getDataListByFullSql(dealedSql);
            if(!StringHelper.isEmpty(dealedmap) && dealedmap.size()>0){
                for(Map m :dealedmap){
                    String qllx= StringHelper.FormatByDatatype(m.get("QLLX"));
                    String djlx=StringHelper.FormatByDatatype(m.get("DJLX"));
                    String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
                    if(ConstValue.DJLX.CFDJ.Value.equals(djlx) && ConstValue.QLLX.QTQL.Value.equals(qllx)){
                        if(!lstseal.contains(bdcdyid)){
                            lstseal.add(bdcdyid);
                            SealCount++;
                        }
                    }else if(ConstValue.DJLX.YYDJ.Value.equals(djlx)){
                        if(!lstObjection.contains(bdcdyid)){
                            lstObjection.add(bdcdyid);
                            ObjectionCount++;
                        }

                    }else if(ConstValue.QLLX.DIYQ.Value.equals(qllx)){
                        if(!lstmortgage.contains(bdcdyid)){
                            lstmortgage.add(bdcdyid);
                            mortgageCount++;
                        }

                    }
                }
            }
            //正在办理的业务
            StringBuilder strdealing=new StringBuilder();
            strdealing.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID,XMXX.QLLX XMQLLX,XMXX.DJLX XMDJLX  FROM  ( ");
            strdealing.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
            strdealing.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
            strdealing.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX  ");
            strdealing.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
            strdealing.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=QL.XMBH  ");
            strdealing.append("WHERE (XMXX.SFDB IS NULL OR XMXX.SFDB<>'1') AND QL.QLID IS NOT NULL  ");
            strdealing.append("AND DY.ZDBDCDYID= '");
            strdealing.append(zdbdcdyid).append("'");
            String dealingsql=strdealing.toString();
            List<Map> dealingmap=baseCommonDao.getDataListByFullSql(dealingsql);
            if(!StringHelper.isEmpty(dealingmap) && dealingmap.size()>0){
                for(Map m :dealingmap){
                    String qllx= StringHelper.FormatByDatatype(m.get("XMQLLX"));
                    String djlx=StringHelper.FormatByDatatype(m.get("XMDJLX"));
                    String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
                    if(ConstValue.DJLX.CFDJ.Value.equals(djlx) && ConstValue.QLLX.QTQL.Value.equals(qllx)){
                        if(!lstseal.contains(bdcdyid) && !lstsealing.contains(bdcdyid)){
                            SealingCount++;
                            lstsealing.add(bdcdyid);
                        }
                    }else if(ConstValue.DJLX.YYDJ.Value.equals(djlx)){
                        if(!lstObjection.contains(bdcdyid) && !lstObjectioning.contains(bdcdyid)){
                            ObjectioningCount++;
                            lstObjectioning.add(bdcdyid);
                        }
                    }else if(ConstValue.QLLX.DIYQ.Value.equals(qllx) && !ConstValue.DJLX.ZXDJ.Value.equals(djlx)){
                        if(!lstmortgage.contains(bdcdyid) && !lstmortgageing.contains(bdcdyid)){
                            mortgagingCount++;
                            lstmortgageing.add(bdcdyid);
                        }
                    }
                }
            }
            //限制的业务
            StringBuilder strlimit=new StringBuilder();
            strlimit.append("SELECT DYXZ.YXBZ FROM (SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZ UNION ALL  ");
            strlimit.append("SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZY ) DY ");
            strlimit.append("LEFT JOIN BDCK.BDCS_DYXZ DYXZ ON DYXZ.BDCDYID=DY.BDCDYID ");
            strlimit.append("WHERE DYXZ.ID IS NOT NULL  ");
            strlimit.append(" AND DY.ZDBDCDYID='").append(zdbdcdyid).append("'");
            String limitsql=strlimit.toString();
            List<Map> limitmap = baseCommonDao.getDataListByFullSql(limitsql);
            //商品房的土地抵消状态赋值
            if(limitmap != null && limitmap.size() > 0){
                for(Map m :limitmap){
                    String yxbz= StringHelper.FormatByDatatype(m.get("YXBZ"));
                    if("1".equals(yxbz)){
                        LimitCount++;
                    }else{
                        LimitingCount++;
                    }
                }

            }
            String mortgageStatus =  MessageFormat.format("{0};地上房屋已抵押{1}起,正在抵押{2}起",map.get("DYZT"),mortgageCount,mortgagingCount);
            String sealStatus = MessageFormat.format("{0};地上房屋已查封{1}起,正在查封{2}起",map.get("CFZT"),SealCount,SealingCount);
            String objectionStatus = MessageFormat.format("{0};地上房屋有异议{1}起",map.get("YYZT"),ObjectionCount);
            String LimitStatus = MessageFormat.format("{0};地上房屋有限制{1}起,正在限制{2}起",map.get("XZZT"),LimitCount,LimitingCount);
            map.put("DYZT", mortgageStatus);
            map.put("CFZT", sealStatus);
            map.put("YYZT", objectionStatus);
            map.put("XZZT", LimitStatus);
        }
        return map;
    }


    //*********************************************土地限制状态 ↑*********************************************

    /**
     * 校验申请人身份信息
     * @param
     * @return
     * @throws IOException
     */
    public List<Map<String,Object>> CheckSQR(BDCS_SQR sqr) throws IOException {
        String postUrlCompany = "http://20.0.6.7:6062/developer-api/sync/resources/007566146170601F0002-1/records/query";
        String postUrlPerson ="http://20.0.6.7:6062/developer-api/sync/resources/89822217818050B00001-6/1/requests";
        String postUrlPerson_gj = "http://localhost:8081/InterfacePlatform/Validate/Validateinfo_JZCX";
        String postUrlCompany_gj = "http://localhost:8081/InterfacePlatform/Validate/ValidateinfoForGS";

        List<Map<String,Object>> check_list = new ArrayList<Map<String, Object>>();
        String[] errCodes = {"02", "03", "04", "05", "06", "07", "08", "09", "java.lang.NullPointerException", "java.net.SocketTimeoutException: Read timed out"};
        String check_msg = "";

        String sqrxm = sqr.getSQRXM();
        String zjh = sqr.getZJH();
        String sqrlx = sqr.getSQRLX();
        String dlrxm = sqr.getDLRXM();
        String dlrzjhm = sqr.getDLRZJHM();
        String fddbr = sqr.getFDDBR();

        //校验个人身份信息
        if("1".equals(sqrlx)){
            try {
                if(!StringHelper.isEmpty(sqrxm) && !StringHelper.isEmpty(zjh)){
                    Map m = null;
                    if(zjh.indexOf("45") == 0){
                        m =	URLUtil.sandForPersonXX(postUrlPerson,sqrxm,zjh);
                    }else{
                        String content = "sqrxm="+sqrxm;
                        content += "&zjh="+zjh;
                        m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
                    }

                    Map<String, Object> map = new HashMap<String, Object>();

                    if (sqrxm.equals(m.get("name")) && zjh.equals(m.get("zjhm"))) {
                        if (zjh.indexOf("45") == -1) {
                            check_msg = String.format("申请人【%s】校验结果：通过公安部个人基本信息接口核验。", sqrxm);
                        } else {
                            check_msg = String.format("申请人【%s】校验结果：通过自治区公安厅个人基本信息接口核验。", sqrxm);
                        }
                        map.put("check_code", CHECK_TRUE);
                    } else {
                        if(Arrays.asList(errCodes).contains(m.get("code")) || !StringHelper.isEmpty(m.get("errstr"))) {
                            check_msg = String.format("申请人【%s】校验结果：%s。", sqrxm, m.get("errstr"));
                        } else if (zjh.indexOf("45") == -1) {
                            check_msg = String.format("申请人【%s】校验结果：信息有误或非中国户籍人口，无法通过公安部个人基本信息接口核验。", sqrxm);
                        } else {
                            check_msg = String.format("申请人【%s】校验结果：信息有误或非自治区户籍人口，无法通过自治区公安厅个人基本信息接口核验。", sqrxm);
                        }
                        map.put("check_code", CHECK_WARNING);
                    }
                    map.put("check_msg", check_msg);
                    check_list.add(map);
                }
            } catch (Exception e) {
                check_msg = String.format("申请人【%s】校验结果：网络或未知异常，校验失败！", sqrxm);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("check_msg", check_msg);
                map.put("check_code", CHECK_WARNING);
                check_list.add(map);
                e.printStackTrace();
            }

            try {
                if(!StringHelper.isEmpty(dlrxm)&&!StringHelper.isEmpty(dlrzjhm)){
                    Map m = null;
                    if(dlrzjhm.indexOf("45") == 0){
                        m =	URLUtil.sandForPersonXX(postUrlPerson,dlrxm,dlrzjhm);
                    }else{
                        String content = "sqrxm="+dlrxm;
                        content += "&zjh="+dlrzjhm;
                        m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
                    }

                    Map<String, Object> map = new HashMap<String, Object>();

                    if (dlrxm.equals(m.get("name")) && dlrzjhm.equals(m.get("zjhm"))) {
                        if (dlrzjhm.indexOf("45") == -1) {
                            check_msg = String.format("代理人【%s】校验结果：通过公安部个人基本信息接口核验。", dlrxm);
                        } else {
                            check_msg = String.format("代理人【%s】校验结果：通过自治区公安厅个人基本信息接口核验。", dlrxm);
                        }
                        map.put("check_code", CHECK_TRUE);
                    } else {
                        if(Arrays.asList(errCodes).contains(m.get("code")) || !StringHelper.isEmpty(m.get("errstr"))) {
                            check_msg = String.format("代理人【%s】校验结果：%s。", dlrxm, m.get("errstr"));
                        } else if (dlrzjhm.indexOf("45") == -1) {
                            check_msg = String.format("代理人【%s】校验结果：信息有误或非中国户籍人口，无法通过公安部个人基本信息接口核验。", dlrxm);
                        } else {
                            check_msg = String.format("代理人【%s】校验结果：信息有误或非自治区户籍人口，无法通过自治区公安厅个人基本信息接口核验。", dlrxm);
                        }
                        map.put("check_code", CHECK_WARNING);
                    }
                    map.put("check_msg", check_msg);
                    check_list.add(map);
                }
            } catch (Exception e) {
                check_msg = String.format("代理人【%s】校验结果：网络或未知异常，校验失败！", dlrxm);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("check_msg", check_msg);
                map.put("check_code", CHECK_WARNING);
                check_list.add(map);
                e.printStackTrace();
            }

        }else if("2".equals(sqrlx)){
            //企业
            try {
                if(!StringHelper.isEmpty(sqrxm) && !StringHelper.isEmpty(zjh)){
                    Map<String,Object> jsonparam = new HashMap<String,Object>();
                    jsonparam.put("table", "GXSJZX_LZ.REG_LZHYQYJBXXB");
                    Map<String,Object> values = new HashMap<String,Object>();
                    List<String> vallsit = new ArrayList<String>();
                    vallsit.add("QYMC");
                    vallsit.add("TYSHXYDM");//91450100MA5KA1CLXJ
                    vallsit.add("FDDBR");
                    values.put("columns", vallsit);
                    jsonparam.put("values", values);
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("column", "QYMC");
                    map.put("mode", "is");
                    map.put("value", sqrxm);
                    Map<String,String> map2 = new HashMap<String, String>();
                    map2.put("column", "TYSHXYDM");
                    map2.put("mode", "is");
                    map2.put("value", zjh);
                    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                    list.add(map);
                    list.add(map2);
                    List<Object> listpara = new ArrayList<Object>();
                    listpara.add(list);
                    jsonparam.put("conditions",listpara);
                    net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonparam);
                    Map m = null;
                    if (zjh.indexOf("9145") == -1) {
                        String content = "sqrxm="+sqrxm;
                        content += "&zjh="+zjh;
                        m = URLUtil.sandJSONPOST_GJGS(postUrlCompany_gj, content);
                    } else {
                        m = URLUtil.sandJSONPOST(postUrlCompany, jsonObject);
                    }

                    Map<String, Object> _map = new HashMap<String, Object>();

                    if (sqrxm.equals(m.get("QYMC")) && zjh.equals(m.get("TYSHXYDM")) && fddbr.equals(m.get("FDDBR"))) {
                        if (zjh.indexOf("9145") == -1) {
                            check_msg = String.format("申请人【%s】校验结果：通过国家工商总局企业信息认证。", sqrxm);
                        } else {
                            check_msg = String.format("申请人【%s】校验结果：通过自治区工商局企业信息认证。", sqrxm);
                        }
                        _map.put("check_code", CHECK_TRUE);
                    } else {
                        if (zjh.indexOf("9145") == -1) {
                            check_msg = String.format("申请人【%s】校验结果：信息有误或非中国注册的企业，无法通过国家工商总局企业信息核验。", sqrxm);
                        } else {
                            check_msg = String.format("申请人【%s】校验结果：信息有误或非自治区注册的企业，无法通过自治区工商局企业信息核验。", sqrxm);
                        }
                        _map.put("check_code", CHECK_WARNING);
                    }
                    _map.put("check_msg", check_msg);
                    check_list.add(_map);
                }
            } catch (Exception e) {
                check_msg = String.format("申请人【%s】校验结果：网络或未知异常，校验失败！", sqrxm);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("check_msg", check_msg);
                map.put("check_code", CHECK_WARNING);
                check_list.add(map);
                e.printStackTrace();
            }

            try {
                if(!StringHelper.isEmpty(dlrxm)&&!StringHelper.isEmpty(dlrzjhm)){
                    Map m = null;
                    if(dlrzjhm.indexOf("45") == 0){
                        m =	URLUtil.sandForPersonXX(postUrlPerson,dlrxm,dlrzjhm);
                    }else{
                        String content = "sqrxm="+dlrxm;
                        content += "&zjh="+dlrzjhm;
                        m =	URLUtil.sandForPersonXX_GJ(postUrlPerson_gj,content);
                    }

                    Map<String, Object> map = new HashMap<String, Object>();

                    if (dlrxm.equals(m.get("name")) && dlrzjhm.equals(m.get("zjhm")) && StringHelper.isEmpty(m.get("errstr"))) {
                        if (dlrzjhm.indexOf("45") == -1) {
                            check_msg = String.format("代理人【%s】校验结果：通过公安部个人基本信息接口核验。", dlrxm);
                        } else {
                            check_msg = String.format("代理人【%s】校验结果：通过自治区公安厅个人基本信息接口核验。", dlrxm);
                        }
                        map.put("check_code", CHECK_TRUE);
                    } else {
                        if(Arrays.asList(errCodes).contains(m.get("code"))  || !StringHelper.isEmpty(m.get("errstr"))) {
                            check_msg = String.format("代理人【%s】校验结果：%s。", dlrxm, m.get("errstr"));
                        } else if (dlrzjhm.indexOf("45") == -1) {
                            check_msg = String.format("代理人【%s】校验结果：信息有误或非中国户籍人口，无法通过公安部个人基本信息接口核验。", dlrxm);
                        } else {
                            check_msg = String.format("代理人【%s】校验结果：信息有误或非自治区户籍人口，无法通过自治区公安厅个人基本信息接口核验。", dlrxm);
                        }
                        map.put("check_code", CHECK_WARNING);
                    }
                    map.put("check_msg", check_msg);
                    check_list.add(map);
                }
            } catch (Exception e) {
                check_msg = String.format("代理人【%s】校验结果：网络或未知异常，校验失败！", dlrxm);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("check_msg", check_msg);
                map.put("check_code", CHECK_WARNING);
                check_list.add(map);
                e.printStackTrace();
            }
        }else{
            check_msg = String.format("申请人【%s】校验结果：申请人类型非个人或企业，当前无法校验。", sqrxm);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("check_msg", check_msg);
            map.put("check_code", CHECK_WARNING);
            check_list.add(map);
        }

        return check_list;
    }

    /**
     * 将对象转换成MAP
     * @Title: transBean2Map
     * @author:wuzhu
     * @date：
     * @param obj
     * @return
     *///将对象转换成MAP
    public  Map<String, Object> transBean2Map(Object obj) {
        if(obj == null){
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
                    if (StringHelper.isEmpty(getter)) {
                        continue;
                    }
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("对象转换为MAP失败： " + e);
        }
        return map;
    }

    //*******************************************登簿约束校验开始***********************************************
    /**
     * 执行登簿检查
     * @Title: Check
     * @author:liushufeng
     * @date：2015年11月7日 下午7:00:56
     * @param info
     * @return
     */
    public List<Map<String,Object>> check(ProjectInfo info) {
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("XMBH", info.getXmbh());
        paramMap.put("PROJECT_ID", info.getProject_id());
        List<T_CHECKRULE> checkRules = getCheckRules(info);
        for (CheckRule rule : checkRules) {
            Map<String, Object> map = new HashMap<String, Object>();
            ResultMessage checkMsg= CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
            //检查级别，1-严重；2-警告
            map.put("checklevel", "1");
            //检查状态
            map.put("state", checkMsg.getSuccess());
            //结果提示语句
            if(StringHelper.isEmpty(checkMsg.getMsg())){
                map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
            }else{
               map.put("resulttip", checkMsg.getMsg());
            }
            maps.add(map);
        }
        List<T_CHECKRULE> checkRulesWarning = getCheckRulesWarning(info);
        if (checkRulesWarning != null && checkRulesWarning.size() > 0) {
            for (CheckRule rule : checkRulesWarning) {
                Map<String, Object> map = new HashMap<String, Object>();
                //检查级别，1-严重；2-警告
                map.put("checklevel", "2");
                //检查状态
                ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
                map.put("state", checkMsg.getSuccess());
                //结果提示语句
                if(StringHelper.isEmpty(checkMsg.getMsg())){
                    map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
                }else{
                    map.put("resulttip", checkMsg.getMsg());
                }
                maps.add(map);
            }
        }
        return maps;
    }

    @SuppressWarnings("rawtypes")
    public List<Map<String,Object>> checkEX(ProjectInfo info) {
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("XMBH", info.getXmbh());
        paramMap.put("PROJECT_ID", info.getProject_id());
        List<T_CHECKRULE> checkRules = getCheckRules(info);
        List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='"+info.getXmbh()+"'");
        String ly = djdys.get(0).getLY();
        String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
        if(baseworkflowname.contains("BG")){
            ly="02";
        }
        for (CheckRule rule : checkRules) {
            Map<String, Object> _map = new HashMap<String, Object>();
            ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
            if ("false".equals(checkMsg.getSuccess())) {
                ConstValue.BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(info.getXmbh());
                String dystr="bdck.bdcs_h_gz";
                if("02".equals(ly)) {
                    dystr="bdck.bdcs_h_xz";
                }
                if(bdcdylx.equals(ConstValue.BDCDYLX.SHYQZD)) {
                    dystr="bdck.bdcs_shyqzd_gz";
                    if("02".equals(ly)) {
                        dystr="bdck.bdcs_shyqzd_xz";
                    }
                }else if(bdcdylx.equals(ConstValue.BDCDYLX.YCH)) {
                    dystr="bdck.bdcs_h_xzy";
                    if("02".equals(ly)) {
                        dystr="bdck.bdcs_h_lsy";
                    }
                }
                if(rule.getCLASSNAME().contains("单元相关")) {//LZCK609 LZCK610
                    String sql = rule.getEXECUTESQL();
                    String querysql ="select h.bdcdyh,h.zl "+sql;
                    if(dystr.contains("shyqzd")) {
                        querysql ="select zd.bdcdyh,zd.zl "+sql;
                    }
                    List<Map> dataList = baseCommonDao.getDataListByFullSql(querysql, paramMap);
                    if(dataList != null && dataList.size()>0) {
                        StringBuilder result = new StringBuilder();
                        for (Map map : dataList) {
                            String bdcdyh = (String) map.get("BDCDYH");
                            String zl = (String) map.get("ZL");
                            result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                        }
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        //对应单元
                        _map.put("result", result);
                        maps.add(_map);
                    }else {
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        maps.add(_map);
                    }
                } else if(rule.getCLASSNAME().contains("权利人相关")){
                    if("CK1001".equals(rule.getId())) {
                        //权利人不为空
                        String sql = rule.getEXECUTESQL();
                        sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    }else if("CK1012".equals(rule.getId())){
                        //持证方式为共同持证并且有多个权利人的情况下必须设置持证人
                        String sql = rule.getEXECUTESQL();
                        sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
                                + " ql.qlid in (select  qlr.qlid "+sql+"))";
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    }else if("CK1008".equals(rule.getId())){
                        //多个权利人共有方式必须一致
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        maps.add(_map);
                    }
                    else {
                        String sql = rule.getEXECUTESQL();
                        sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
                                + " ql.qlid in (select  qlr.qlid "+sql+" ))";
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    }
                } else if(rule.getCLASSNAME().contains("权利相关")){
                    if("CK2104".equals(rule.getId())) {
                        //证书编号不能为空
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        maps.add(_map);
                    }  else {
                        String sql = rule.getEXECUTESQL();
                        sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    }
                }  else if(rule.getCLASSNAME().contains("业务逻辑")){
                    if("CK4001".equals(rule.getId()) ||"CK4002".equals(rule.getId())) {
                        String sql = rule.getEXECUTESQL();
                        sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    }else if("CK4003".equals(rule.getId())) {
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        maps.add(_map);
                    }else {
                        String sql = rule.getEXECUTESQL();
                        String querysql ="select h.bdcdyh,h.zl "+sql;
                        if(dystr.contains("shyqzd")) {
                            querysql ="select zd.bdcdyh,zd.zl "+sql;
                        }
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(querysql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "1");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    }
                }  else if(rule.getCLASSNAME().contains("限制类")){
                    String sql = rule.getEXECUTESQL();
                    String querysql ="select h.bdcdyh,h.zl "+sql;
                    if(dystr.contains("shyqzd")) {
                        querysql ="select zd.bdcdyh,zd.zl "+sql;
                    }
                    List<Map> dataList = baseCommonDao.getDataListByFullSql(  querysql, paramMap);
                    if(dataList != null && dataList.size()>0) {
                        StringBuilder result = new StringBuilder();
                        int count = 0;
                        for (Map map : dataList) {
                            String bdcdyh = (String) map.get("BDCDYH");
                            String zl = (String) map.get("ZL");
                            if (count < 2) {
                                result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                            } else {
                                result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                break;
                            }
                            count++;
                        }
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        //对应单元
                        _map.put("result", result);
                        maps.add(_map);
                    }else {
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "1");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        maps.add(_map);
                    }
                } else {
                    //检查级别，1-严重；2-警告
                    _map.put("checklevel", "1");
                    //检查状态
                    _map.put("state", checkMsg.getSuccess());
                    //结果提示语句
                    _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                    maps.add(_map);
                }
            } else {
                //检查级别，1-严重；2-警告
                _map.put("checklevel", "1");
                //检查状态
                _map.put("state", checkMsg.getSuccess());
                //结果提示语句
                if(StringHelper.isEmpty(checkMsg.getMsg())){
                    _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
                }else{
                    _map.put("resulttip", checkMsg.getMsg());
                }
                maps.add(_map);
            }
        }

        List<T_CHECKRULE> checkRulesWarning = getCheckRulesWarning(info);
        if (checkRulesWarning != null && checkRulesWarning.size() > 0) {
            for (CheckRule rule : checkRulesWarning) {
                Map<String, Object> _map = new HashMap<String, Object>();
                ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
                if ("false".equals(checkMsg.getSuccess())) {
                    ConstValue.BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(info.getXmbh());
                    String dystr="bdck.bdcs_h_gz";
                    if("02".equals(ly)) {
                        dystr="bdck.bdcs_h_xz";
                    }
                    if(bdcdylx.equals(ConstValue.BDCDYLX.SHYQZD)) {
                        dystr="bdck.bdcs_shyqzd_gz";
                        if("02".equals(ly)) {
                            dystr="bdck.bdcs_shyqzd_xz";
                        }
                    }else if(bdcdylx.equals(ConstValue.BDCDYLX.YCH)) {
                        dystr="bdck.bdcs_h_xzy";
                        if("02".equals(ly)) {
                            dystr="bdck.bdcs_h_lsy";
                        }
                    }
                    if(rule.getCLASSNAME().contains("单元相关")) {//LZCK609 LZCK610
                        String sql = rule.getEXECUTESQL();
                        String querysql ="select h.bdcdyh,h.zl "+sql;
                        if(dystr.contains("shyqzd")) {
                            querysql ="select zd.bdcdyh,zd.zl "+sql;
                        }
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(querysql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "2");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "2");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    } else if(rule.getCLASSNAME().contains("权利人相关")){
                        if("CK1001".equals(rule.getId())) {
                            //权利人不为空
                            String sql = rule.getEXECUTESQL();
                            sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }else if("CK1012".equals(rule.getId())){
                            //持证方式为共同持证并且有多个权利人的情况下必须设置持证人
                            String sql = rule.getEXECUTESQL();
                            sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
                                    + " ql.qlid in (select  qlid "+sql+"))";
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }else if("CK1008".equals(rule.getId())){
                            //多个权利人共有方式必须一致
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "2");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        } else {
                            String sql = rule.getEXECUTESQL();
                            sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
                                    + " ql.qlid in (select  qlr.qlid "+sql+" ))";
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }
                    } else if(rule.getCLASSNAME().contains("权利相关")){
                        if("CK2104".equals(rule.getId())) {
                            //证书编号不能为空
                            String sql = rule.getEXECUTESQL();
                            sql=" select bdcdyh ,zl from "+dystr+" where bdcdyid in (select bdcdyid from bdck.bdcs_qdzr_gz where zsid in (select zsid "+sql+"))";
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }  else {
                            String sql = rule.getEXECUTESQL();
                            sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }
                    }  else if(rule.getCLASSNAME().contains("业务逻辑")){
                        if("CK4001".equals(rule.getId()) ||"CK4002".equals(rule.getId())) {
                            String sql = rule.getEXECUTESQL();
                            sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }else if("CK4003".equals(rule.getId())) {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "2");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }else {
                            String sql = rule.getEXECUTESQL();
                            String querysql ="select h.bdcdyh,h.zl "+sql;
                            if(dystr.contains("shyqzd")) {
                                querysql ="select zd.bdcdyh,zd.zl "+sql;
                            }
                            List<Map> dataList = baseCommonDao.getDataListByFullSql(  querysql, paramMap);
                            if(dataList != null && dataList.size()>0) {
                                StringBuilder result = new StringBuilder();
                                int count = 0;
                                for (Map map : dataList) {
                                    String bdcdyh = (String) map.get("BDCDYH");
                                    String zl = (String) map.get("ZL");
                                    if (count < 2) {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                    } else {
                                        result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                        break;
                                    }
                                    count++;
                                }
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                //对应单元
                                _map.put("result", result);
                                maps.add(_map);
                            }else {
                                //检查级别，1-严重；2-警告
                                _map.put("checklevel", "2");
                                //检查状态
                                _map.put("state", checkMsg.getSuccess());
                                //结果提示语句
                                _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                                maps.add(_map);
                            }
                        }
                    }  else if(rule.getCLASSNAME().contains("限制类")){
                        String sql = rule.getEXECUTESQL();
                        String querysql ="select h.bdcdyh,h.zl "+sql;
                        if(dystr.contains("shyqzd")) {
                            querysql ="select zd.bdcdyh,zd.zl "+sql;
                        }
                        List<Map> dataList = baseCommonDao.getDataListByFullSql(  querysql, paramMap);
                        if(dataList != null && dataList.size()>0) {
                            StringBuilder result = new StringBuilder();
                            int count = 0;
                            for (Map map : dataList) {
                                String bdcdyh = (String) map.get("BDCDYH");
                                String zl = (String) map.get("ZL");
                                if (count < 2) {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
                                } else {
                                    result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("  等" + dataList.size() + "个");
                                    break;
                                }
                                count++;
                            }
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "2");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            //对应单元
                            _map.put("result", result);
                            maps.add(_map);
                        }else {
                            //检查级别，1-严重；2-警告
                            _map.put("checklevel", "2");
                            //检查状态
                            _map.put("state", checkMsg.getSuccess());
                            //结果提示语句
                            _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                            maps.add(_map);
                        }
                    } else {
                        //检查级别，1-严重；2-警告
                        _map.put("checklevel", "2");
                        //检查状态
                        _map.put("state", checkMsg.getSuccess());
                        //结果提示语句
                        _map.put("resulttip",rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
                        maps.add(_map);
                    }
                }
            }
        }
        return maps;
    }

    /**
     * 获取流程对应的检查项列表,这个获取的是严重的
     * @Title: getCheckRules
     * @author:liushufeng
     * @date：2015年11月7日 下午9:22:23
     * @param info
     * @return
     */
    public List<T_CHECKRULE> getCheckRules(ProjectInfo info) {
        String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
        String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
        String hql = MessageFormat
                .format(" (id IN (SELECT CHECKRULEID FROM RT_BASEBOARDCHECK WHERE CHECKLEVEL='1' AND BASEWORKFLOWID=''{0}'') AND id NOT IN(SELECT CHECKRULEID FROM RT_BOARDCHECKEXP WHERE WORKFLOWCODE=''{1}'')) OR id IN (SELECT CHECKRULEID FROM RT_BOARDCHECK WHERE (CHECKLEVEL IS NULL OR CHECKLEVEL<>'2') AND WORKFLOWCODE=''{1}'')",
                        baseworkflowname, workflowcode);
        List<T_CHECKRULE> checkRules = baseCommonDao.getDataList(T_CHECKRULE.class, hql);
        return checkRules;
    }

    /**
     * 获取流程对应的检查项列表,这个获取的是警告的
     * @Title: getCheckRulesWarning
     * @author:liushufeng
     * @date：2016年1月11日 下午2:35:15
     * @param info
     * @return
     */
    public List<T_CHECKRULE> getCheckRulesWarning(ProjectInfo info) {
        String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
        String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
        String hql = MessageFormat
                .format(" (id IN (SELECT CHECKRULEID FROM RT_BASEBOARDCHECK WHERE CHECKLEVEL='2' AND BASEWORKFLOWID=''{0}'') AND id NOT IN(SELECT CHECKRULEID FROM RT_BOARDCHECKEXP WHERE WORKFLOWCODE=''{1}'')) OR id IN (SELECT CHECKRULEID FROM RT_BOARDCHECK WHERE CHECKLEVEL='2' AND WORKFLOWCODE=''{1}'')",
                        baseworkflowname, workflowcode);
        List<T_CHECKRULE> checkRules = baseCommonDao.getDataList(T_CHECKRULE.class, hql);
        return checkRules;
    }

    private Map<String, Object> ZYYGCanecl(String xmbh) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,"XMBH='" + xmbh  +"'");
        if(djdys!=null&&djdys.size()>0){
            for(BDCS_DJDY_GZ djdy:djdys){
                List<String> list_qlrmc_zjh=new ArrayList<String>();
                List<RightsHolder> qlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), xmbh);
                if(qlrs!=null&&qlrs.size()>0){
                    for(RightsHolder qlr:qlrs){
                        list_qlrmc_zjh.add(qlr.getQLRMC()+"&"+StringHelper.formatObject(qlr.getZJH()));
                    }
                }
                List<Rights> zyyg_qls=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.XZ, "DJDYID='"+djdy.getDJDYID()+"' AND DJLX='700' AND QLLX='99'");
                if(zyyg_qls!=null&&zyyg_qls.size()>0){
                    for(Rights zyyg_ql:zyyg_qls){
                        List<RightsHolder> zyyg_qlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.XZ, zyyg_ql.getId());
                        if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
                            for(RightsHolder zyyg_qlr:zyyg_qlrs){
                                String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+StringHelper.formatObject(zyyg_qlr.getZJH());
                                if (!list_qlrmc_zjh.contains(str_qlrmc_zjh)) {
                                    //检查级别，1-严重；2-警告
                                    map.put("checklevel", "1");
                                    //检查状态
                                    map.put("state", false);
                                    //结果提示语句
                                    map.put("resulttip", "单元已办理转移预告登记，且转移预告权利人不在转移后权利人中");
                                } else {
                                    //检查级别，1-严重；2-警告
                                    map.put("checklevel", "1");
                                    //检查状态
                                    map.put("state", true);
                                    //结果提示语句
                                    map.put("resulttip", "单元已办理转移预告登记，且转移预告权利人不在转移后权利人中");
                                }
                            }
                        }
                    }
                }

            }
        }
        return map;
    }
    //*******************************************登簿约束校验结束***********************************************


    @Override
    public void getQrcode(HttpServletRequest req, HttpServletResponse rep) throws IOException {
        String file_number = req.getParameter("file_number");
        List<Map> intelligents = baseCommonDao.getDataListByFullSql("SELECT YWLSH,WLSH,PRO_NAME,ZL,SQR,SHJG FROM BDCK.INTELLIGENT WHERE FILE_NUMBER= '" + file_number + "' ORDER BY CREATEDTIME DESC");
        if(!intelligents.isEmpty()) {
            Map intelligent = intelligents.get(0);
            String shjg = StringHelper.formatObject(intelligent.get("SHJG"));
            if("false".equals(shjg)) {
                shjg = "存在严重警告，驳回";
            } else if("warning".equals(shjg)) {
                shjg = "存在警告，请核查";
            } else if("true".equals(shjg)) {
                    shjg = "校验无误，通过";
            }
            StringBuilder content = new StringBuilder();
            if(!StringHelper.isEmpty(intelligent.get("WLSH"))) {
                content.append("WLSH="+intelligent.get("WLSH")+"&");
            }
            content.append("YWLSH="+intelligent.get("YWLSH")+"&").append("SQR="+intelligent.get("SQR")+"&").append("SHJG="+shjg);
            BufferedImage image = QRCodeHelper.CreateQRCode(content.toString(), "png", 100, 100);
            ImageIO.write(image, "png", rep.getOutputStream());
        }
    }


    @Override
    public Map<String, Object> getReport(HttpServletRequest req)  {
        String file_number = req.getParameter("file_number");
        //先判断有没有生成审批报告，没有则直接生成一个新的审批报告
        long count = baseCommonDao.getCountByFullSql(" FROM BDCK.INTELLIGENT WHERE FILE_NUMBER= '" + file_number + "'");
        if(count==0) {
            createReport(file_number);
        }
        ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(file_number);
        List<Map> intelligents = baseCommonDao.getDataListByFullSql("SELECT YWLSH,XMBH,WLSH,BDCDYH,PRO_NAME,BDCQZH,ZL,SQR,SHJG,SHJGXX,CREATEDTIME FROM BDCK.INTELLIGENT WHERE FILE_NUMBER= '" + file_number + "' ORDER BY CREATEDTIME DESC");
        if(!intelligents.isEmpty()) {
            Map intelligent = intelligents.get(0);
            intelligent.put("sfdb", info.getSfdb());
            return intelligent;
        }
        return null;
    }


}
