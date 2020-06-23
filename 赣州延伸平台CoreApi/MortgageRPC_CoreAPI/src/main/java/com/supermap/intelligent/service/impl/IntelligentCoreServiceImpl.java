package com.supermap.intelligent.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.getjson.json;
import com.supermap.intelligent.dao.CommonDaoMRPC;
import com.supermap.intelligent.model.LOG_DECLARE_RECORD_LOG;
import com.supermap.intelligent.model.Mortgage_proinst;
import com.supermap.intelligent.service.InsertQLForData;
import com.supermap.intelligent.service.IntelligentCoreService;
import com.supermap.intelligent.util.AfterBoardException;
import com.supermap.intelligent.util.BeresolvException;
import com.supermap.intelligent.util.ManualException;
import com.supermap.internetbusiness.model.AUTOPROJECTCONFIG;
import com.supermap.internetbusiness.model.ENTERPRISEPRODEF;
import com.supermap.internetbusiness.service.IAutoQueryService;
import com.supermap.internetbusiness.service.impl.AutoOperationService;
import com.supermap.internetbusiness.service.impl.AutoSmActInst;
import com.supermap.intelligent.util.ResultData;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.UserRole;
import com.supermap.wisdombusiness.synchroinline.model.JsonMessage;
import com.supermap.wisdombusiness.synchroinline.util.CommonsHttpInvoke;
import com.supermap.wisdombusiness.synchroinline.util.SelectorTools;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;
import org.apache.log4j.Logger;
import org.apache.xalan.transformer.ResultNameSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service("intelligentCoreService")
public class IntelligentCoreServiceImpl implements IntelligentCoreService {

    @Autowired
    CommonDao baseCommonDao;
    @Autowired
    SmProDef smProDef;
    @Autowired
    private IAutoQueryService autoQueryService;
    @Autowired
    private DBService dbService;
    @Autowired
    private SmProInstService smProInstService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DYService dyService;
    @Autowired
    private InsertQLForData insertQLForData;
    @Autowired
    private DJBService djbService;
    @Autowired
    SmProSPService smProSPService;
    @Autowired
    private AutoOperationService autoOperationService;
    @Autowired
    private AutoSmActInst autoSmActInst;
    @Autowired
    private SmProInstService ProInstService;
    @Autowired
    CommonDaoMRPC baseCommonDaoMrpc;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Logger logger = Logger.getLogger(IntelligentCoreServiceImpl.class);

    @Override
    public ResultData declare(HttpServletRequest req) throws UnsupportedEncodingException {
        ResultData result = new ResultData();
        LOG_DECLARE_RECORD_LOG recordLog = new LOG_DECLARE_RECORD_LOG();
        //获取参数
        String requestcode = "", requestseq = "", ywlsh = "", qxdm = "", ywlcid="";

        JSONObject httpBodyParams = null;
        try {
            httpBodyParams = RequestHelper.getHTTPBodyParams(req);
            if (httpBodyParams.isEmpty()) {
                throw new Exception ("未获取到请求申报数据");
            }
            requestcode = httpBodyParams.getString("requestcode");
            requestseq = httpBodyParams.getString("requestseq");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("获取申报数据出现异常，详情：" + e.getMessage());
            result.setState(false);
            result.setErrorcode("3001");
        }
        JSONObject jsonObject = null;
        try {
            // ----------------------- 1.解析数据 -----------------------
            JSONObject data = httpBodyParams.getJSONObject("data");
            if (data.isEmpty()) {
                throw new Exception ("申报数据 data 节点不能为空");
            }
            jsonObject = AnalysisData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("解析数据出现异常，详情：" + e.getMessage());
            result.setState(false);
            result.setErrorcode("3001");
        }

        logger.info(String.format("抵押业务申报开始，申报编码【%s】，申报标识【%s】", requestcode, requestseq));
        try {

            assert jsonObject != null;
            JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");

            // ----------------------- 2.各种数据核验操作 -----------------------
            ywlsh = basicInfo.getString("YWLSH");
            qxdm = basicInfo.getString("QXDM");
            ywlcid = basicInfo.getString("YWLCID");
            if (StringHelper.isEmpty(requestcode)) {
                throw new BeresolvException("获取申报业务类型异常", "3001");
            }
            if (StringHelper.isEmpty(ywlsh)) {
                throw new BeresolvException("获取申报流水号异常", "3001");
            }
            if (StringHelper.isEmpty(qxdm)) {
                throw new BeresolvException("获取申报区县代码异常", "3001");
            }
            if (StringHelper.isEmpty(ywlcid)) {
                throw new BeresolvException("获取申报业务流程ID异常", "3001");
            }
            // ----------------------- 2.1创建日志记录 -----------------------
            List<LOG_DECLARE_RECORD_LOG> record_logs = baseCommonDao.getDataList(LOG_DECLARE_RECORD_LOG.class, "SBLSH='" + ywlsh + "'");
            if (record_logs != null && record_logs.size() > 0) {
                recordLog = record_logs.get(0);
                recordLog.setSBCS(recordLog.getSBCS() + 1);
                recordLog.setSBZT("1");
                recordLog.setMODIFYTIME(new Date());
                recordLog.setREMARK("");
            } else {
                recordLog.setBSM(Common.CreatUUID());
                recordLog.setSBLSH(ywlsh);
                recordLog.setYWLY("3");
                recordLog.setSBCS(1);
                recordLog.setSBZT("1");
                recordLog.setSFDB("0");
                recordLog.setYWLSH(ywlsh);
                recordLog.setCREATEDATE(new Date());
                recordLog.setMODIFYTIME(new Date());
                recordLog.setYWLCID(ywlcid);
                recordLog.setTENANT_ID(qxdm);
            }
            //----------------------- 2.2 检查业务是否已经创建 -----------------------
            createdExamine(ywlsh, recordLog);
            //----------------------- 2.3 是否与不动产库数据匹配 -----------------------
            checkAccectConstraint(jsonObject, recordLog);

            // ----------------------- 3.创建项目操作 -----------------------
            String prolsh = accectProject(jsonObject, recordLog, requestcode, req);
            updataLsh(ywlsh, prolsh);
            result.setMsg("业务申报成功！");
            result.setState(true);
            result.setErrorcode("0000");
        } catch (ManualException e) {
            // 手动抛出的异常，利用校验不通过或为可判断的，需要用户解决的提示错误
            result.setMsg(e.getRecordLog().getREMARK());
            result.setState(false);
            result.setErrorcode(e.getRecordLog().getERRORCODE());
            saveRecordLog(e.getRecordLog());
        } catch (BeresolvException e) {
            // 需要解决的异常，标识知道异常原因但需要人工解决的异常,比如登簿失败，把登簿失败原因记录到日志中，方便工作人员排查
            result.setMsg(e.getRecordLog().getREMARK());
            result.setState(false);
            result.setErrorcode(e.getRecordLog().getERRORCODE());
            saveRecordLog(e.getRecordLog());
        }  catch (Exception e) {
            // 未知错误，这种错误往往是代码错误
            e.printStackTrace();
            result.setMsg(e.getMessage());
            result.setState(false);
            result.setErrorcode("3001");
            saveErrorLog(qxdm, ywlsh, e);
        }
        logger.info(String.format("抵押业务申报结束，申报编码【%s】，申报标识【%s】", requestcode, requestseq));
        return result;
    }

    /**
     * @Author taochunda
     * @Description 申报成功更新一波外网的流水号字段
     * @Date 2019-09-27 22:14
     * @Param [wlsh, lsh]
     * @return void
     **/
    private void updataLsh(String wlsh, String lsh) {
        if (StringHelper.isEmpty(lsh)) {
            return;
        }
        try {
            List<Mortgage_proinst> proinsts = baseCommonDaoMrpc.getDataList(Mortgage_proinst.class, " PROLSH='" + wlsh + "'");
            if (proinsts != null && !proinsts.isEmpty()) {
                Mortgage_proinst proinst = proinsts.get(0);
                proinst.setLsh(lsh);
                baseCommonDaoMrpc.update(proinst);
                //baseCommonDaoMrpc.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************1.解析数据 start*****************************************/
    /**
     * @Author taochunda
     * @Description 解析抵押申报业务JSON串，格式示例详见：互联网+不动产抵押登记风险防控平台接口标准v1.2
     * @Date 2019-07-29 10:53
     * @Param [data]
     * @return void
     **/
    public static JSONObject AnalysisData(JSONObject jsonObject) {
        JSONObject object = new JSONObject();
        //基本信息
        Map<String, Object> basicInfo = getBasicInfo(jsonObject);
        //申请人集合
        String sqrlist = jsonObject.getString("SQRLIST");
        List<Map> sqrList = new ArrayList<Map>();
        List<Map> qlrList = new ArrayList<Map>();
        List<Map> ywrList = new ArrayList<Map>();
        List<Map> dyqrList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(sqrlist)){
            sqrList = getSqrList(sqrlist, qlrList, ywrList, dyqrList);
        }
        //抵押单元集合
        String dydylist = jsonObject.getString("DYDYLIST");
        List<Map> dydyList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(dydylist)){
            dydyList = getDydyList(dydylist);
        }
        /*//附件集合
        String promater = jsonObject.getString("PROMATER");
        List<Map> promaterList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(promater)){
            promaterList = getPromaterList(promater);
        }*/
        //资料目录实例
        String materclass = jsonObject.getString("MATERCLASS");
        JSONArray arrayMaterclass = new JSONArray();
        if (!StringHelper.isEmpty(materclass)) {
            arrayMaterclass = JSONArray.parseArray(materclass);
        }
        //资料文件
        String materdata = jsonObject.getString("MATERDATA");
        JSONArray arrayMaterdata = new JSONArray();
        if (!StringHelper.isEmpty(materclass)) {
            arrayMaterdata = JSONArray.parseArray(materdata);
        }

        //登记收费list
        String djsfs = jsonObject.getString("DJSFS");
        JSONArray djsfList = new JSONArray();
        if(!StringHelper.isEmpty(djsfList)){
            djsfList = JSONArray.parseArray(djsfs);
        }

        object.put("basicInfo", basicInfo);
        object.put("sqrList", sqrList);
        object.put("qlrList", qlrList);
        object.put("ywrList", ywrList);
        object.put("dyqrList", dyqrList);
        object.put("dydyList", dydyList);
        object.put("djsfList", djsfList);
//        object.put("promaterList", promaterList);
        object.put("arrayMaterclass", arrayMaterclass);
        object.put("arrayMaterdata", arrayMaterdata);
        return object;
    }
    
    /**
     * @Author taochunda
     * @Description 解析抵押申报业务JSON串，格式示例详见：互联网+不动产抵押登记风险防控平台接口标准v1.2
     * @Date 2019-07-29 10:53
     * @Param [data]
     * @return void
     **/
    public static JSONObject AnalysisData_enterprise(JSONObject jsonObject,String reString) {
        JSONObject object = new JSONObject();
        //基本信息
        Map<String, Object> basicInfo = getBasicInfo_Enterprise(jsonObject,reString);
        //申请人集合
        String sqrlist = jsonObject.getString("SQRLIST");
        List<Map> sqrList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(sqrlist)){
            sqrList = getSqrList_enterprise(sqrlist);
        }
        //抵押单元集合
        String dydylist = jsonObject.getString("ZDLIST");
        List<Map> dydyList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(dydylist)){
            dydyList = getDydyList_enterprise(dydylist);
        }
        /*//附件集合
        String promater = jsonObject.getString("PROMATER");
        List<Map> promaterList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(promater)){
            promaterList = getPromaterList(promater);
        }*/
        //资料目录实例
        String materclass = jsonObject.getString("MATERCLASS");
        JSONArray arrayMaterclass = new JSONArray();
        if (!StringHelper.isEmpty(materclass)) {
            arrayMaterclass = JSONArray.parseArray(materclass);
        }
        //资料文件
        String materdata = jsonObject.getString("MATERDATA");
        JSONArray arrayMaterdata = new JSONArray();
        if (!StringHelper.isEmpty(materclass)) {
            arrayMaterdata = JSONArray.parseArray(materdata);
        }


        object.put("basicInfo", basicInfo);
        object.put("sqrList", sqrList);
        object.put("dydyList", dydyList);
//        object.put("promaterList", promaterList);
        object.put("arrayMaterclass", arrayMaterclass);
        object.put("arrayMaterdata", arrayMaterdata);
        return object;
    }

    public  static Map<String, Object> getBasicInfo(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("YWLSH", jsonObject.getString("YWLSH"));
        map.put("QXDM", jsonObject.getString("QXDM"));
        map.put("DJLX", jsonObject.getString("DJLX"));
        map.put("QLLX", jsonObject.getString("QLLX"));
        map.put("BDCDYLX", jsonObject.getString("BDCDYLX"));
        map.put("YWLCID", jsonObject.getString("YWLCID"));
        map.put("YWLCMC", jsonObject.getString("YWLCMC"));
        map.put("SBRYMC", jsonObject.getString("SBRYMC"));
        map.put("SRYZJH", jsonObject.getString("SRYZJH"));
        map.put("SBRYLXDH", jsonObject.getString("SBRYLXDH"));
        map.put("SBJGMC", jsonObject.getString("SBJGMC"));
        map.put("SBJGZJH", jsonObject.getString("SBJGZJH"));
        map.put("DYFS", jsonObject.getInteger("DYFS"));
        map.put("ZQSE", jsonObject.getDouble("ZQSE"));
        map.put("DYPGJZ", jsonObject.getDouble("DYPGJZ"));
        map.put("CZFS", jsonObject.getString("CZFS"));
        map.put("SFHBZS", jsonObject.getString("SFHBZS"));
        map.put("DJYY", jsonObject.getString("DJYY"));
        map.put("FJ", jsonObject.getString("FJ"));
        map.put("ZXDJYY", jsonObject.getString("ZXDJYY"));
        map.put("ZXFJ", jsonObject.getString("ZXFJ"));
        map.put("ZQDW", jsonObject.getString("ZQDW"));
        map.put("REMARKS", jsonObject.getString("REMARKS"));
        map.put("ZSBS", jsonObject.getString("ZSBS"));
        map.put("TDSHYQR", jsonObject.getString("TDSHYQR"));
        map.put("HTH", jsonObject.getString("HTH"));
        map.put("QDJG",jsonObject.getDouble("QDJG"));
        map.put("FWXZ", jsonObject.getString("FWXZ"));
        return map;
    }
    
    public  static Map<String, Object> getBasicInfo_Enterprise(JSONObject jsonObject,String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ENTERPRISEADDRESS", jsonObject.getString("ENTERPRISEADDRESS"));
        map.put("REGISTERPHONE", jsonObject.getString("REGISTERPHONE"));
        map.put("ENTERPRISENAME", jsonObject.getString("ENTERPRISENAME"));
        map.put("FRDBXM", jsonObject.getString("FRDBXM"));
        map.put("FRDBZJHM", jsonObject.getString("FRDBZJHM"));
        map.put("ENTERPRISECODE", jsonObject.getString("ENTERPRISECODE"));
        map.put("REGISTERNAME", jsonObject.getString("REGISTERNAME"));
        map.put("REGISTERZJHM", jsonObject.getString("REGISTERZJHM"));
        map.put("ENTERPRISEID", jsonObject.getString("ENTERPRISEID"));
        map.put("TYPE", type); 
        map.put("QXDM", ConfigHelper.getNameByValue("XZQHDM"));
        return map;
    }

    public static List<Map> getSqrList(String sqrlist, List<Map> qlrList, List<Map> ywrList, List<Map> dyqrList) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(sqrlist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            String sqrlb = jsonObject.getString("SQRLB");
            map.put("SQRMC", jsonObject.getString("SQRMC"));
            map.put("SQRZJH", jsonObject.getString("SQRZJH"));
            map.put("SQRZJZL", jsonObject.getString("SQRZJZL"));
            map.put("SQRLXDH", jsonObject.getString("SQRLXDH"));
            map.put("DLRMC", jsonObject.getString("DLRMC"));
            map.put("DLRZJH", jsonObject.getString("DLRZJH"));
            map.put("DLRZJZL", jsonObject.getString("DLRZJZL"));
            map.put("DLRLXDH", jsonObject.getString("DLRLXDH"));
            map.put("FRMC", jsonObject.getString("FRMC"));
            map.put("FRZJH", jsonObject.getString("FRZJH"));
            map.put("FRZJZL", jsonObject.getString("FRZJZL"));
            map.put("FRLXDH", jsonObject.getString("FRLXDH"));
            map.put("GYFS", jsonObject.getString("GYFS"));
            map.put("QLBL", jsonObject.getString("QLBL"));
            map.put("SQRLB", sqrlb);
            map.put("SQRLX", jsonObject.getString("SQRLX"));
            map.put("SXH", jsonObject.getString("SXH"));
            map.put("SQRJZH", jsonObject.getString("SQRJZH"));
            map.put("BDCQZH", jsonObject.getString("BDCQZH"));
            list.add(map);
            if (ConstValue.SQRLB.JF.Value.equals(sqrlb)) {
                qlrList.add(map);
            } else if (ConstValue.SQRLB.YF.Value.equals(sqrlb)) {
                ywrList.add(map);
            } else if ("10".equals(sqrlb)) {
                dyqrList.add(map);
            }

        }
        return list;
    }
    
    public static List<Map> getSqrList_enterprise(String sqrlist) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(sqrlist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            map.put("NAME", jsonObject.getString("NAME"));
            map.put("USERNAME", jsonObject.getString("USERNAME"));
            map.put("PHONE", jsonObject.getString("PHONE"));
            map.put("ZJHM", jsonObject.getString("ZJHM"));
            list.add(map);
        }
        return list;
    }

    public static List<Map> getDydyList(String dydylist) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(dydylist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));

            //单元信息
            map.put("BDCDYH", jsonObject.getString("BDCDYH"));
            map.put("ZL", jsonObject.getString("ZL"));
            map.put("DYQLQSSJ", jsonObject.getString("DYQLQSSJ"));
            map.put("DYQLJSSJ", jsonObject.getString("DYQLJSSJ"));
//            map.put("DGBDBZZQSE", jsonObject.getDouble("DGZQSE"));

            // 附属权利信息
            map.put("DYR", jsonObject.getString("DYR"));
            map.put("ZGZQQDSS", jsonObject.getString("ZGZQQDSS"));
            map.put("ZJJZWDYFW", jsonObject.getString("ZJJZWDYFW"));
            map.put("ZWR", jsonObject.getString("ZWR"));
            map.put("ZWRZJH", jsonObject.getString("ZWRZJH"));
            map.put("DGBDBZZQSE", jsonObject.getDouble("DGBDBZZQSE"));
            list.add(map);
        }
        return list;
    }
    
    public static List<Map> getDydyList_enterprise(String dydylist) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(dydylist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));

            //单元信息
            map.put("BDCDYH", jsonObject.getString("bdcdyh"));
            map.put("ZL", jsonObject.getString("zl"));
            map.put("ENTERPRISEID", jsonObject.getString("enterpriseid"));
            map.put("ID", jsonObject.getString("id"));
            list.add(map);
        }
        return list;
    }

    public static List<Map> getPromaterList(String promater) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(promater);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            String material_name = jsonObject.getString("MATERIAL_NAME");
            String material_count = jsonObject.getString("MATERIAL_COUNT");
            String material_desc = jsonObject.getString("MATERIAL_DESC");
            String materdata = jsonObject.getString("MATERDATA");

            JSONArray array = JSONArray.parseArray(materdata);
            List<Map> map_materdata = new ArrayList<Map>();
            for (int j = 0; j < array.size(); j++) {
                Map<String, Object> info = new HashMap<String, Object>();
                JSONObject object = JSONObject.parseObject(StringHelper.formatObject(array.get(i)));
                String file_id = object.getString("FILE_ID");
                String file_name = object.getString("FILE_NAME");
                String file_postfix = object.getString("FILE_POSTFIX");
                String file_index = object.getString("FILE_INDEX");
                String file_url = object.getString("FILE_URL");
                info.put("FILE_ID", file_id);
                info.put("FILE_NAME", file_name);
                info.put("FILE_POSTFIX", file_postfix);
                info.put("FILE_INDEX", file_index);
                info.put("FILE_URL", file_url);
                map_materdata.add(info);
            }

            map.put("MATERIAL_NAME", material_name);
            map.put("MATERIAL_COUNT", material_count);
            map.put("MATERIAL_DESC", material_desc);
            map.put("MATERDATA", map_materdata);
            list.add(map);
        }
        return list;
    }
    /*****************************************1.解析数据 end*****************************************/

    /*****************************************2.数据核验 start*****************************************/
    /**
     * @Author taochunda
     * @Description 检查该流水号是否已经在登记系统创建项目
     * @Date 2019-07-29 17:04
     * @Param
     * @return
     **/
    public void createdExamine(String ywlsh, LOG_DECLARE_RECORD_LOG recordLog) {
        List<Map> xmxx = baseCommonDao.getDataListByFullSql("SELECT XM.*,PRO.PROINST_ID FROM  BDC_WORKFLOW.WFI_PROINST PRO LEFT JOIN BDCK.BDCS_XMXX XM ON PRO.FILE_NUMBER=XM.PROJECT_ID WHERE PRO.WLSH='"+ywlsh+"'");

        if (xmxx.size() > 0 && "1".equals(StringHelper.formatObject(xmxx.get(0).get("SFDB")))) {
            logger.info(String.format("申报流水号为【%s】的业务已经办理并登簿，请勿重复申报！", ywlsh));
            throw new BeresolvException(recordLog, "0", String.format("申报流水号为【%s】的业务已经办理并登簿，请勿重复申报！", ywlsh), "1001");
        }

        //已经创建但未登簿，重新创建项目，类似于更新
        if(xmxx.size() > 0) {
            String proinstid = StringHelper.formatObject(xmxx.get(0).get("PROINST_ID"));
            ProInstService.deleteProInst(proinstid, "", "系统自动删除", "抵押平台智能审批过程出现异常");
        }
    }

    public void checkAccectConstraint(JSONObject jsonObject, LOG_DECLARE_RECORD_LOG recordLog) throws Exception {
        JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
        JSONArray sqrList = jsonObject.getJSONArray("sqrList");
        JSONArray qlrList = jsonObject.getJSONArray("qlrList");
        JSONArray ywrList = jsonObject.getJSONArray("ywrList");
        JSONArray dydyList = jsonObject.getJSONArray("dydyList");
        JSONArray promaterList = jsonObject.getJSONArray("promaterList");

        String ywlsh = basicInfo.getString("YWLSH");
        String qxdm = basicInfo.getString("QXDM");
        String ywlcid = basicInfo.getString("YWLCID");
        String djlx = basicInfo.getString("DJLX");
        String qllx = basicInfo.getString("QLLX");
        String bdcdylx = basicInfo.getString("BDCDYLX");

        if (StringHelper.isEmpty(ywlsh)) {
            throw new BeresolvException(recordLog, "2", String.format("data 节点 YWLSH 属性值不能为空！"), "1002");
        }
        if (StringHelper.isEmpty(qxdm)) {
            throw new BeresolvException(recordLog, "2", String.format("data 节点 QXDM 属性值不能为空！"), "1002");
        }
        if (StringHelper.isEmpty(ywlcid)) {
            throw new BeresolvException(recordLog, "2", String.format("data 节点 YWLCID 属性值不能为空！"), "1002");
        }
        if (StringHelper.isEmpty(djlx)) {
            throw new BeresolvException(recordLog, "2", String.format("data 节点 DJLX 属性值不能为空！"), "1002");
        }
        if (StringHelper.isEmpty(qllx)) {
            throw new BeresolvException(recordLog, "2", String.format("data 节点 QLLX 属性值不能为空！"), "1002");
        }
        if (StringHelper.isEmpty(bdcdylx)) {
            throw new BeresolvException(recordLog, "2", String.format("data 节点 BDCDYLX 属性值不能为空！"), "1002");
        }
        if (dydyList == null || dydyList.size() <= 0) {
            throw new BeresolvException(recordLog, "2", String.format("该业务丢失单元信息，请检查 DYDYLIST 节点数据是否无误"), "1002");
        }
        if (sqrList == null || sqrList.size() <= 0) {
            throw new BeresolvException(recordLog, "2", String.format(" ，请检查 SQRLIST 节点数据是否无误"), "1002");
        }

        Wfd_Prodef prodef = smProDef.GetProdefById(ywlcid);
        if (prodef == null) {
            throw new BeresolvException(recordLog, "2", String.format("内网流程匹配错误，流程id为【%s】的记录在Wfd_Prodef表中未找到！", ywlcid), "2001");
        }

        List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + prodef.getProdef_Code() + "'");
        if (mappings == null || mappings.size() != 1) {
            throw new BeresolvException(recordLog, "2", String.format("该业务流程配置错误，请检查，WFD_MAPPING表中WORKFLOWCODE=%s的数据有%s条记录！", prodef.getProdef_Code(), mappings.size()), "2001");
        }
        String workflowname = mappings.get(0).getWORKFLOWNAME();
        // 获取基准流程
        T_BASEWORKFLOW base_wf = baseCommonDao.get(T_BASEWORKFLOW.class, workflowname);
        if (base_wf == null) {
            throw new BeresolvException(recordLog, "2", String.format("未获取到基准流程，请检查 T_BASEWORKFLOW 表中是否存在ID=%s 的记录", workflowname), "2001");
        }
        String selectorid = base_wf.getSELECTORID();
        if (StringHelper.isEmpty(selectorid)) {
            throw new BeresolvException(recordLog, "2", String.format("未获取到选择器，请检查 T_BASEWORKFLOW 表中ID=%s 的 SELECTORID 字段配置", workflowname), "2001");
        }
        //支持的业务类型集合
        String[] workflownames = {"CS010","CS011","CS034","ZY007","YG002","YG003","ZX003","ZX004","ZX006","ZX009","BG005","BG006","BG053","ZY002"};
        if(Arrays.asList(workflownames).contains(workflowname)) {
            if ("CS010".equals(workflowname) || "CS011".equals(workflowname) || "CS034".equals(workflowname) ) {
                checkDyAndSQR(dydyList, ywrList, "100", "4", recordLog);
            } else if("ZY002".equals(workflowname)) {
            	  checkZYDJInfo(dydyList, ywrList, qlrList, recordLog);
            } else if ("ZY007".equals(workflowname)){
                checkZYDJInfo(dydyList, ywrList, qlrList, recordLog);
            } else if ("ZX003".equals(workflowname) || "ZX004".equals(workflowname) || "ZX006".equals(workflowname) || "ZX009".equals(workflowname)) {
                /**
                 * ZX003 注销登记_抵押权_使用权宗地
                 * ZX004 注销登记_抵押权_户
                 * ZX006 注销登记_抵押权_预测户（预抵押注销）
                 * ZX009 注销登记_抵押权_在建工程抵押注销
                 **/
                if ("400".equals(djlx) && "23".equals(qllx)) {//抵押注销
                    //校验房屋及权利信息
                    checkDYZXInfo(qlrList, ywrList, recordLog);
                } else {
                    throw new ManualException(recordLog, "2", "该注销业务非抵押注销，暂不支持！", "1005");
                }
            } else if ("BG005".equals(workflowname) || "BG006".equals(workflowname)||"BG053".equals(workflowname)){
                checkDyAndSQR(dydyList, ywrList, "300", "23", recordLog);
            }
        } else {
            throw new ManualException(recordLog, "2", "该业务类型不支持！", "1005");
        }
    }

    /**
     * 抵押注销验证
     */
    @SuppressWarnings("rawtypes")
    private void checkDYZXInfo(JSONArray qlrList, JSONArray ywrList, LOG_DECLARE_RECORD_LOG recordLog) {
        if (ywrList.isEmpty()) {
            throw new ManualException(recordLog, "2", "请填写抵押人信息", "4001");
        }
        if (qlrList.isEmpty()) {
            throw new ManualException(recordLog, "2", "请填写抵押权人信息", "4001");
        }
        //校验抵押人房屋及权利信息
        List list_bdczmhdy = new ArrayList<String>();
        for (int i = 0; i < qlrList.size(); i++) {
            String BDCQZH = qlrList.getJSONObject(i).getString("BDCQZH");
            if (list_bdczmhdy.contains(BDCQZH)) {
                continue;
            }
            list_bdczmhdy.add(BDCQZH);
            //先校验权利人和这个证号的关系
            if (!StringHelper.isEmpty(BDCQZH)) {
                String djdyid = "";
                long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QLR_XZ QL WHERE TRIM(QL.BDCQZH)='" + BDCQZH.trim() + "' ");
                if (count == 0) {
                    throw new ManualException(recordLog, "2", String.format("填写的不动产权证号(证明号)【%s】查询失败，请核查信息是否有误", BDCQZH), "4001");
                }
                List<BDCS_QLR_XZ> dyqrlist = baseCommonDao.getDataList(BDCS_QLR_XZ.class, " TRIM(BDCQZH)='" + BDCQZH.trim() + "' AND  TRIM(QLRMC)='" + qlrList.getJSONObject(i).getString("SQRMC") + "' AND  TRIM(ZJH)='" + qlrList.getJSONObject(i).getString("SQRZJH") +"'");
                if(dyqrlist.isEmpty()){
                    throw new ManualException(recordLog, "2", String.format("填写的抵押权人信息有误，请核查数据是否正确"), "4001");
                }


                List<Map> djdys = baseCommonDao.getDataListByFullSql("SELECT QL.DJDYID FROM BDCK.BDCS_QL_XZ QL WHERE QL.QLID=(SELECT QL.QLID FROM BDCK.BDCS_QLR_XZ QL WHERE TRIM(QL.BDCQZH)='" + BDCQZH.trim() + "' )");
                djdyid = StringHelper.formatObject(djdys.get(0).get("DJDYID"));
                //再验证义务人与这个单元是否有关系
                String fullsql = "SELECT DYR.QLRMC,DYR.ZJH FROM BDCK.BDCS_QLR_XZ DYR WHERE DYR.QLID IN (" +
                        "SELECT DYRQL.QLID FROM BDCK.BDCS_QL_XZ DYRQL WHERE DYRQL.DJDYID='" + djdyid + "' AND QLLX<>'23') ";
                List<Map> dyrs = baseCommonDao.getDataListByFullSql(fullsql);
                //外网不一定把所有抵押人都填完，但是不能填多，所以以外网填写的抵押人即义务人为循环点，分别跟登记数据对比,只要有一个外网填写的义务人跟在登记里没有，就返回false
                boolean flag = false;//每个义务人都判断，是否在登记有数据
                for (int j = 0; j < ywrList.size(); j++) {
                    for (Map dyr : dyrs) {
                        String ywrmc = ywrList.getJSONObject(j).getString("SQRMC");
                        String ywrzjh = ywrList.getJSONObject(j).getString("SQRZJH");
                        // 申请人旧证号
                        String sqrjzh = ywrList.getJSONObject(j).getString("SQRJZH");
                        String dyrmc = StringHelper.formatObject(dyr.get("QLRMC"));
                        String dyrzjh = StringHelper.formatObject(dyr.get("ZJH"));
                        if (ywrmc.equals(dyrmc) && (ywrzjh.equals(dyrzjh) || dyrzjh.equals(sqrjzh))) {
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    throw new ManualException(recordLog, "2", "填写的申请人与登记信息不符，请检查申请人信息及房屋权证号（证明号）是否正确", "4001");
                }
            } else {
                throw new ManualException(recordLog, "2", "不动产权证号(证明号)不能为空", "1005");
            }
        }


    }

    /**
     * 转移登记验证
     */
    @SuppressWarnings("rawtypes")
    private void checkZYDJInfo(JSONArray dydyList, JSONArray ywrList, JSONArray qlrList, LOG_DECLARE_RECORD_LOG recordLog) {
        List<String> list_qlrmc_zjh=new ArrayList<String>();
        if (qlrList != null && qlrList.size() > 0) {
            for (int i = 0; i < qlrList.size(); i++) {
                list_qlrmc_zjh.add(StringHelper.formatObject(qlrList.getJSONObject(i).getString("QLRMC"))+"&"+ StringHelper.formatObject(qlrList.getJSONObject(i).getString("QLRZJH")));
            }
        }

        //校验房屋及权利信息
        for (int i = 0; i < dydyList.size(); i++) {
            String BDCDYH = dydyList.getJSONObject(i).getString("BDCDYH");
            String BDCQZH = null;
            //验证申报义务人信息是否和登记数据相同,只要有一个申报填写的义务人跟在登记里没有，就返回false
            if (ywrList.size() == 0) {
                throw new ManualException(recordLog, "2", "请填写义务人信息", "4001");
            }
            //先校验权利人和这个证号的关系
            if (!StringHelper.isEmpty(BDCQZH) || !StringHelper.isEmpty(BDCDYH)) {
                String djdyid = "";
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT QL.DJDYID FROM BDCK.BDCS_QLR_XZ QLR LEFT JOIN BDCK.BDCS_QL_XZ QL ON QLR.QLID=QL.QLID WHERE 1=1 AND QL.DJLX<>'700' ");
                if(!StringHelper.isEmpty(BDCQZH)) {
                    sb.append("AND TRIM(QLR.BDCQZH)='" + BDCQZH.trim() + "'");
                    long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QLR_XZ QL WHERE TRIM(QL.BDCQZH)='" + BDCQZH.trim() + "' ");
                    if (count == 0) {
                        logger.info(String.format("填写的不动产权证号【%s】查询失败，请核查信息是否有误", BDCQZH));
                        throw new ManualException(recordLog, "2", String.format("填写的不动产权证号【%s】查询失败，请核查信息是否有误", BDCQZH), "4001");
                    }
                }
                if(!StringHelper.isEmpty(BDCDYH)) {
                    long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QL_XZ QL WHERE TRIM(QL.BDCDYH)='" + BDCDYH.trim() + "' ");
                    if (count == 0) {
                        logger.info(String.format("填写的不动产单元号【%s】查询失败，请核查信息是否有误", BDCDYH));
                        throw new ManualException(recordLog, "2", String.format("填写的不动产单元号【%s】查询失败，请核查信息是否有误", BDCDYH), "4001");
                    }
                    sb.append("AND TRIM(QL.BDCDYH)='" + BDCDYH.trim() + "'");
                }
                List<Map> djdys = baseCommonDao.getDataListByFullSql(sb.toString());
                if(djdys.isEmpty() || StringHelper.isEmpty(djdys.get(0).get("DJDYID"))) {
                    throw new ManualException(recordLog, "2", "查不到单元信息，请检查数据是否完整", "4001");
                }
                djdyid = StringHelper.formatObject(djdys.get(0).get("DJDYID"));
                //获取该不动产单元的权利人信息 用于和外网填写的义务人对比
                String fullsql = "SELECT QLR.QLRMC,QLR.ZJH,QLR.QLRLX FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID IN (" +
                        "SELECT SYQ.QLID FROM BDCK.BDCS_QL_XZ SYQ WHERE SYQ.DJDYID='" + djdyid + "' AND SYQ.QLLX in('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18') )";
                List<Map> qlrs = baseCommonDao.getDataListByFullSql(fullsql);
                //验证外网填写的义务人信息是否和办理的类型匹配
                boolean flag = false;//每个义务人都判断，是否在登记有数据
                for (int j = 0; j < ywrList.size(); j++) {
                    for (Map qlr : qlrs) {
                        String ywrmc = ywrList.getJSONObject(j).getString("SQRMC");
                        String ywrzjh = ywrList.getJSONObject(j).getString("SQRZJH");
                        // TODO 申请人旧证号，解析那块还得加上
                        String sqrjzh = ywrList.getJSONObject(j).getString("SQRJZH");
                        String qlrmc = StringHelper.formatObject(qlr.get("QLRMC"));
                        String qlrzjh = StringHelper.formatObject(qlr.get("ZJH"));
                        if (ywrmc.equals(qlrmc) && (ywrzjh.equals(qlrzjh) || qlrzjh.equals(sqrjzh))) {
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    throw new ManualException(recordLog, "2", "填写的申请人与登记信息不符，请检查申请人姓名、证件号和房屋单元号、权证号（证明号）是否正确", "4001");
                }

                //转移的权利人要和预告的权利人一致
                List<Map> listGX=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.YC_SC_H_XZ WHERE SCBDCDYID in (SELECT BDCDYID FROM BDCK.BDCS_DJDY_XZ  WHERE DJDYID='"+djdyid+"')");
                if(listGX!=null&&listGX.size()>0){
                    for(Map gx:listGX){
                        String YCBDCDYID= StringHelper.formatObject(gx.get("YCBDCDYID"));
                        if(StringHelper.isEmpty(YCBDCDYID)){
                            continue;
                        }
                        List<BDCS_DJDY_XZ> djdys_yc=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+YCBDCDYID+"'");
                        if(djdys_yc==null||djdys_yc.size()<=0){
                            continue;
                        }
                        String ycdjdyid=djdys_yc.get(0).getDJDYID();
                        List<Rights> ycyg_qls= RightsTools.loadRightsByCondition(ConstValue.DJDYLY.XZ, "DJDYID='"+ycdjdyid+"' AND DJLX='700' AND QLLX='4'");
                        if(ycyg_qls!=null&&ycyg_qls.size()>0){
                            for(Rights ycyg_ql:ycyg_qls){
                                List<RightsHolder> zyyg_qlrs= RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.XZ, ycyg_ql.getId());
                                if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
                                    for(RightsHolder zyyg_qlr:zyyg_qlrs){
                                        String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+ StringHelper.formatObject(zyyg_qlr.getZJH());
                                        if(!list_qlrmc_zjh.contains(str_qlrmc_zjh)){
                                            throw new ManualException(recordLog, "2", "单元已办理商品房预告登记，但商品房预告权利人与转移后权利人不一致", "4001");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                List<Rights> zyyg_qls= RightsTools.loadRightsByCondition(ConstValue.DJDYLY.XZ, "DJDYID='"+djdyid+"' AND DJLX='700' AND QLLX='99' ");
                if(zyyg_qls!=null&&zyyg_qls.size()>0){
                    for(Rights zyyg_ql:zyyg_qls){
                        List<RightsHolder> zyyg_qlrs= RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.XZ, zyyg_ql.getId());
                        if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
                            for(RightsHolder zyyg_qlr:zyyg_qlrs){
                                String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+ StringHelper.formatObject(zyyg_qlr.getZJH());
                                if(!list_qlrmc_zjh.contains(str_qlrmc_zjh)){
                                    throw new ManualException(recordLog, "2", "单元已办理预告登记，但预告权利人与转移后权利人不一致", "4001");
                                }
                            }
                        }
                    }
                }
            } else {
                throw new ManualException(recordLog, "2", "请填写不动产权证号或不动产单元号", "1005");
            }
        }
    }

    /**
     * 校验义务人和单元是否相符
     */
    private void checkDyAndSQR(JSONArray dydyList, JSONArray ywrList, String djlx, String qllx, LOG_DECLARE_RECORD_LOG recordLog) {
        for (int i = 0; i < dydyList.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < ywrList.size(); j++) {
                //获取填写的权利人对应的单元
                String BDCDYH = dydyList.getJSONObject(i).getString("BDCDYH");
                String BDCQZH = null;//除注销业务外，其他抵押业务都只用单元号选择单元与核验

                String qlrmc = ywrList.getJSONObject(j).getString("SQRMC");
                String zjh = ywrList.getJSONObject(j).getString("SQRZJH");
                if (!StringHelper.isEmpty(BDCQZH)) {
                    //如果权证号不为空，先验证身份和权证号是否相符
                    long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QLR_XZ QL WHERE  TRIM(QL.BDCQZH)='" + BDCQZH.trim() + "' ");
                    if (count == 0) {
                        throw new ManualException(recordLog, "2", String.format("填写不动产权证号(证明号)【%s】查无数据，请核查是否填写有误！", BDCQZH), "4001");
                    }
                }
                Map map = autoQueryService.queryHouse(null, qlrmc, zjh, BDCQZH, BDCDYH);
                Object resultlist = map.get("list");
                if (resultlist == null) {
                    continue;
                }
                List<Map> dylist = (List<Map>) resultlist;
                for (Map dy : dylist) {
                    String dyh = StringHelper.formatObject(dy.get("BDCDYH"));
                    if (!StringHelper.isEmpty(BDCDYH)) {
                        //如果填了单元号，返回结果中没有单元号或者单元号对不上就是填写得不对
                        if (!BDCDYH.equals(dyh)) {
                            throw new ManualException(recordLog, "2", String.format("填写的申请人【%s】信息与不动产单元号【%s】匹配失败，请核查是否填写有误！", qlrmc, BDCDYH), "4001");
                        }
                    }
                }
                flag = true;
            }
            if (!flag) {
                throw new ManualException(recordLog, "2", "填写的申请人与登记信息不符，请检查申请人姓名、证件号和房屋单元号、权证号（证明号）是否正确", "4001");
            }
        }
    }


    /*****************************************2.数据核验 end*****************************************/
    /*****************************************特殊.数据映射 start*****************************************/
/*    private String getoutinnerconfig(String qxdm,String ywid,final LOG_DECLARE_RECORD_LOG recordLog) {
    	String innerywid = "";
    	List<AUTOPROJECTCONFIG> apcs = baseCommonDao.getDataList(AUTOPROJECTCONFIG.class, " PRODEF_ID = '" + qxdm + "' AND SFQY = '1'");
        if (apcs == null || apcs.size() == 0) {
            throw new BeresolvException(recordLog, "2", "读取映射流程配置失败", "2001");
        }
        
    	if(StringHelper.isEmpty(innerywid)) {
    		throw new BeresolvException(recordLog, "2", "读取映射流程配置失败", "2001");
    	}
    	return innerywid;
    }*/
    
    /*****************************************特殊.数据映射end*****************************************/
    
    /*****************************************3.创建项目 start*****************************************/
    /**
     * 创建项目一直到登簿
     */
    public String accectProject(JSONObject jsonObject, final LOG_DECLARE_RECORD_LOG recordLog, String requestcode, HttpServletRequest request) throws Exception {
        String prolsh = "";
        JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
        JSONArray sqrList = jsonObject.getJSONArray("sqrList");
        JSONArray qlrList = jsonObject.getJSONArray("qlrList");
        JSONArray ywrList = jsonObject.getJSONArray("ywrList");
        JSONArray dyqrList = jsonObject.getJSONArray("dyqrList");
        JSONArray dydyList = jsonObject.getJSONArray("dydyList");
//        JSONArray promaterList = jsonObject.getJSONArray("promaterList");
        JSONArray arrayMaterclass = jsonObject.getJSONArray("arrayMaterclass");
        JSONArray arrayMaterdata = jsonObject.getJSONArray("arrayMaterdata");
        JSONArray djsfList = jsonObject.getJSONArray("djsfList");

        String ywlsh = basicInfo.getString("YWLSH");
        String qxdm = basicInfo.getString("QXDM");
        //修改对应映射关系
        String outywlcid = basicInfo.getString("YWLCID");
        String djlx = basicInfo.getString("DJLX");
        String qllx = basicInfo.getString("QLLX");
        String bdcdylx = basicInfo.getString("BDCDYLX");
        String remarks = basicInfo.getString("REMARKS");
        String djproinst_id = "";
        String actinstid = "";
        String blry = "", blryid = "";
        try {
            List<AUTOPROJECTCONFIG> apcs = baseCommonDao.getDataList(AUTOPROJECTCONFIG.class, " OLD_PRODEF_ID = '" + outywlcid + "' AND SFQY = '1' AND DIVISIONCODE='"+qxdm+"'");
            if (apcs == null || apcs.size() == 0) {
                throw new BeresolvException(recordLog, "2", "读取自动办理流程配置失败", "2001");
            }
            String djyy = apcs.get(0).getDJYY(); //
            String fj = apcs.get(0).getFJ(); //获取配置中的附记
            String shyj = apcs.get(0).getSPYJ();    //获取配置中的审核意见
            blry = apcs.get(0).getSTAFFNAME(); //获取配置中的办理人员
            blryid = apcs.get(0).getSTAFFID(); //办理人员id
            String dbr = apcs.get(0).getDBR();//获取配置中的登簿人
            String dbrid = apcs.get(0).getDBRID();//获取配置中的登簿人id
            String isYDB = apcs.get(0).getISYDB();//是否属于预登簿模式
            String isHangUp = apcs.get(0).getISHANGUP();//是否挂起
            String szry = apcs.get(0).getSZRY();//获取配置中的缮证人
            String szryid = apcs.get(0).getSZRYID();//获取配置中的缮证人id
            String routeid = apcs.get(0).getROUTEID(); //获取缮证人的角色id
            String zxdbr = StringHelper.formatObject(apcs.get(0).getZXDBR());//注销登簿人
            String sfzdhqqzh = apcs.get(0).getSFZDHQQZH();//是否自动获取权证号
            String roleid = apcs.get(0).getROLEID();
            updateRecordLog(recordLog, "1", "", "0008");

            // 若附记与登记原因为空，将提取自动办理流程配置的默认附记与默认登记原因
            if (StringHelper.isEmpty(basicInfo.getString("FJ"))) {
                basicInfo.put("FJ", fj);
            }
            if (StringHelper.isEmpty(basicInfo.getString("DJYY"))) {
                basicInfo.put("DJYY", djyy);
            }
            if (StringHelper.isEmpty(basicInfo.getString("ZXFJ"))) {
                basicInfo.put("ZXFJ", fj);
            }
            if (StringHelper.isEmpty(basicInfo.getString("ZXDJYY"))) {
                basicInfo.put("ZXDJYY", djyy);
            }
            jsonObject.put("basicInfo", basicInfo);
            String ywlcid = apcs.get(0).getPRODEF_ID();
            //----------------------- 1.创建项目信息实体 -----------------------
            Wfd_Prodef prodef = smProDef.GetProdefById(ywlcid);
            if (prodef != null) {
                SmProInfo info = new SmProInfo();
                info.setProDef_ID(ywlcid);
                info.setBatch(UUID.randomUUID().toString().replace("-", ""));
                String proDefName = smProDef.getproDefName(ywlcid);
                info.setProDef_Name(proDefName);
                logger.info("REmarks_________+++++"+remarks);
                if(remarks!=null&&!"".equals(remarks)){
                	info.setMessage("抵押平台驳回件,驳回原因："+remarks);
                }else{
                	info.setMessage("由抵押平台核心API自动创建");
                }
                info.setFile_Urgency("1");
                info.setLCBH(prodef.getProdef_Code());
                List<SmObjInfo> staffList = null;
                if(roleid!=null){
                	staffList = getSmobjInfos(roleid);
                }else{
                  SmObjInfo smObjInfo = new SmObjInfo();
                  smObjInfo.setID(blryid);
                  smObjInfo.setName(blry);
                  staffList = new ArrayList<SmObjInfo>();
                  staffList.add(smObjInfo); 
                }
                info.setAcceptor(blry);
                info.setStaffID(blryid);
                info.setFile_Urgency("1");
                info.setYWLY("3");

                StringBuilder sqr = new StringBuilder();
                for (int j = 0; j < sqrList.size(); j++) {
                    String sqrmc = sqrList.getJSONObject(j).getString("SQRMC");
                    if (!StringHelper.isEmpty(sqrmc)) {
                        if (sqr.length() > 0) {
                            sqr.append("、").append(sqrmc);
                        } else {
                            sqr.append(sqrmc);
                        }
                        if (j > 2) {
                            sqr.append(" 等");
                            break;
                        }
                    }
                }
                info.setProjectName(sqr.toString());
                info.setSQStartTime(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd HH:mm:ss"));
                // ----------------------- 2.创建受理项目 -----------------------
                SmObjInfo returnSmObj = smProInstService.Accept(info, staffList);
                djproinst_id = returnSmObj.getID();
                SmObjInfo smInfo = returnSmObj.getChildren().get(0);
                prolsh = smInfo.getID();
                Wfi_ProInst inst = baseCommonDao.get(Wfi_ProInst.class, djproinst_id);
                inst.setProdef_Name(proDefName);
                inst.setWLSH(ywlsh);
                inst.setYWLY("3");
                baseCommonDao.update(inst);
                ProjectInfo projectInfo = projectService.getProjectInfo(inst.getFile_Number(), request);
                // 项目编号
                returnSmObj.setDesc(projectInfo.getXmbh());
                // ----------------------- 3.创建申请人信息 -----------------------
                createSqr(sqrList, projectInfo.getXmbh());
                // 导入申报材料,调用登记系统附件下载接口，将申报材料信息传递过去，由登记系统直接连 mongodb下载图片到本地服务器
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("PROINST_ID", inst.getProinst_Id());
                map.put("userId", blryid);
                map.put("userName", blry);
                // 资料目录实例
                map.put("MATERCLASS", arrayMaterclass);
                // 资料文件
                map.put("MATERDATA", arrayMaterdata);
                final JSONObject object = new JSONObject();
                object.put("PROMATER", map);
                // 附件下载地址（登记系统接口）：http://127.0.0.1:8080/realestate/app/intelligentcore/attachmentDownload
                final String attachment_url = ConfigHelper.getNameByValue("ATTACHMENT_URL");
                if (StringHelper.isEmpty(attachment_url)) {
                    throw new ManualException(recordLog, "0", "获取附件下载接口地址异常，请联系管理员进行配置", "2004");
                }
                String result = HttpClientUtil.requestPost(object.toJSONString(), attachment_url);
                if (StringHelper.isEmpty(result)) {
                    // 附件下载接口接口暂时无法访问
                    throw new ManualException(recordLog, "0", "附件下载接口接口暂时无法访问，请联系管理员", "2004");
                }
                JSONObject parseObject = new JSONObject();
                try{
                	parseObject = JSONObject.parseObject(result);
                }catch (Exception ex) {
                	throw new ManualException(recordLog, "0", "调用附件下载接口发生异常，接口返回值异常", "2004");
                }
                if (parseObject.isEmpty()) {
                    // 调用附件下载接口发生异常，返回null
                    throw new ManualException(recordLog, "0", "调用附件下载接口发生异常，无返回值", "2004");
                }
                Boolean state = parseObject.getBoolean("state");
                String msg1 = parseObject.getString("msg");
                if (!state) {
                    // 附件下载失败
                    throw new ManualException(recordLog, "0", "附件下载失败，详情：" + msg1, "2004");
                }
                updateRecordLog(recordLog, "1", "附件下载成功", "0001");
                /*taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 是否异步下载附件，异步下载失败存在不中断项目继续创建的情况，待确定
                        String result = HttpClientUtil.requestPost(object.toJSONString(), attachment_url);
                    }
                });*/

                actinstid = returnSmObj.getName();
                //创建成功后添加业务号到日志
                recordLog.setYWLSH(inst.getProlsh());
                updateRecordLog(recordLog, "1", "创建申报项目成功", "0002");
                // ----------------------- 4.选择单元 -----------------------
                JsonMessage msg = selectDY(projectInfo.getXmbh(), dydyList, sqrList, prodef, requestcode, qlrList);
                
                if (!msg.getState()) {
                	System.out.println(msg.getMsg());
                    throw new ManualException(recordLog, "0", "受理单元异常：" + msg.getMsg(), "2004");
                }
                updateRecordLog(recordLog, "1", "受理选择单元成功", "0003");

                // ----------------------- 5.插入权利 -----------------------
                insertQLForData.inserQLByData(projectInfo.getXmbh(), jsonObject, prodef);
                updateRecordLog(recordLog, "1", "插入权利成功", "0004");

                List<Wfi_ActInst> yact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "'  order by actinst_start  desc ");
                List<Wfd_Route> routes = baseCommonDao.getDataList(Wfd_Route.class, " prodef_id = '" + inst.getProdef_Id() + "' and  actdef_id = '" + yact.get(0).getActdef_Id() + "'");
                List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, " project_id = '" + inst.getFile_Number() + "'");
                String xmbh = xmxxs.get(0).getId();
                if (routes.size() > 0) {
                    routeid = routes.get(0).getRoute_Id();
                }

                BDCS_XMXX xmxx = xmxxs.get(0);
                if (xmxx != null) {
                    xmxx.setXMMC(sqr.toString());
                    xmxx.setWLSH(ywlsh);
                    xmxx.setSLRY(staffList.get(0).getName());
                    xmxx.setSLRYID(staffList.get(0).getID());
                    xmxx.setYWLSH(smInfo.getID());
//                    baseCommonDao.getCurrentSession().clear();
                    baseCommonDao.saveOrUpdate(xmxx);
//                    baseCommonDao.flush();
                }

                // ----------------------- 6.生成产权证号 -----------------------
                List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='" + xmbh + "' ");

                if(djdys.isEmpty()) {
                    throw new BeresolvException(recordLog, "2", "项目信息关联的djdy查无数据", "2003");
                }

                // 默认自动获取
                if (!"0".equals(sfzdhqqzh)) {
                    List<BDCS_QL_GZ> qlgzs = baseCommonDao.getDataList(BDCS_QL_GZ.class, " djdyid = '" + djdys.get(0).getDJDYID() + "' and xmbh='" + xmbh + "'");
                    //2017年9月19日10:25:31 添加自动获取权证号
                    for (BDCS_QL_GZ ql_gz : qlgzs) {
                        if ("23".equals(ql_gz.getQLLX()) || "700".equals(ql_gz.getDJLX())) {
                            String type = "GETBDCDJZM"; // 标示执行存储过程时生成不动产证明号
                            String bdcqzh = djbService.createBDCZMHByQLLX(xmbh, ql_gz.getQLLX(), type);
                        } else {
                            String type = "GETBDCQZH"; // 标示执行存储过程时生成不动产权证号
                            String bdcqzh = djbService.createBDCQZHByQLLX(xmbh, ql_gz.getQLLX(), type);
                        }
                    }
                    updateRecordLog(recordLog, "1", "生成权证号成功", "0005");
                } else {
                    updateRecordLog(recordLog, "1", "不自动生成权证号", "0005");
                }

                //==========================添加审批意见=========================================
                //（1）初审意见
                smProSPService.SaveApproval("", "1", "CS", actinstid, shyj, "", "", "", null, "", blryid, blry);
                //（2）复审意见
                smProSPService.SaveApproval("", "2", "FS", actinstid, shyj, "", "", "", null, "", blryid, blry);
                //（1）核定意见
                smProSPService.SaveApproval("", "3", "HD", actinstid, shyj, "", "", "", null, "", blryid, blry);

                //---------------------------------添加收费项--------------------------------------

                insertDJSF(xmxx,djsfList,qlrList);
                //暂时不转出
                if(false) {
                	// ----------------------- 7.登簿转出区(转出到登簿环节) -----------------------
                    String msgtext = "转出附言";
                    boolean more = false;
                    List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
                    SmObjInfo objInfo = new SmObjInfo();
                    objInfo.setID(dbrid);// 设置staffed
                    objInfo.setName(dbr);// 设置staffName
                    objInfos.add(objInfo);
                    //调用转出方法
                    SmObjInfo successString = exePassover(actinstid, routeid,
                            msgtext, objInfos, more, blryid);
                    if (!"转出成功".equals(successString.getDesc())) {
                        YwLogUtil.addYwLog("自动办理转出登簿失败" + ywlsh, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
                        throw new BeresolvException(recordLog, "2", "自动办理成功-但转出登簿失败", "2006");
                    } else {
                        YwLogUtil.addYwLog("自动办理转出登簿成功" + ywlsh, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
                    }

                    updateRecordLog(recordLog, "1", "自动办理转出登簿成功", "0006");
                    //生成智能审批报告
                    final String file_number = inst.getFile_Number();
                    if (!StringHelper.isEmpty(file_number)) {
                        try {
                            //获取当前IP跟端口
                            String host = request.getLocalAddr();
                            int port = request.getLocalPort();
                            final String url = "http://" + host + ":" + port + "/realestate/app/intelligent/createReport?file_number=" + file_number;
                            taskExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    //intelligentService.createReport(file_number);
                                    CommonsHttpInvoke chi = new CommonsHttpInvoke();
                                    String result = chi.commonHttpDoPostNotice(null, null, url, file_number);
                                    System.out.println("调用智能审批报告接口返回："+result);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("生成智能审批报告失败");
                        }
                    }

                    //预登簿，到此结束
                    if("1".equals(isYDB)) {
                        try {
                            List<Wfi_ActInst> dbyact = baseCommonDao.getDataList(Wfi_ActInst.class, " PROINST_ID = '" + djproinst_id + "' AND ACTINST_STATUS='1'  ORDER BY ACTINST_START  DESC ");
                            //是否自动挂起
                            if("1".equals(isHangUp)){
                                autoSmActInst.setAutoHangUp(dbyact.get(0).getActinst_Id(), "系统自动挂起操作", blryid, blry);
                            }
                        } catch(Exception e) {
                            throw new AfterBoardException(recordLog, "2", "登簿后出现异常:" + e.getMessage(), "3002");
                        }

                        updateRecordLog(recordLog, "2", "已完成预登簿，等待审核登簿", "0000");
                        return prolsh;
                    }
                    // ----------------------- 8.自动登簿区 -----------------------
                    ResultMessage rmdb = dbService.BoardBook(xmbh);
                    if ("false".equals(rmdb.getSuccess()) || rmdb == null || "warning".equals(rmdb.getSuccess())) {
                        YwLogUtil.addYwLog("登簿-失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
                        if (rmdb.getMsg().contains("null")) {
                            throw new BeresolvException(recordLog, "2", "登簿失败原因:登簿发生未知错误。null", "2007");
                        } else {
                            throw new BeresolvException(recordLog, "2", "登簿失败原因:" + rmdb.getMsg(), "2007");

                        }
                    }
                    //登簿后异常单独捕获，否则登簿完成报错，登簿不会回滚，但项目会被删除
                    try {

                        //登簿完成后更新是否登簿状态
                        if (!"1".equals(xmxx.getSFDB())) {
                            xmxx.setSFDB("1");
//                            baseCommonDao.getCurrentSession().clear();
                            baseCommonDao.saveOrUpdate(xmxx);
                            baseCommonDao.flush();
                        }

                        //更新日志登簿状态
                        List<LOG_DECLARE_RECORD_LOG> dataList = baseCommonDao.getDataList(LOG_DECLARE_RECORD_LOG.class, " SBYWH ='" + ywlsh + "' AND (SFDB <> '1' OR SFDB IS NULL) ");
                        if (dataList != null && dataList.size() > 0) {
                            LOG_DECLARE_RECORD_LOG record_log = dataList.get(0);
                            record_log.setSFDB("1");
//                            baseCommonDao.getCurrentSession().clear();
                            baseCommonDao.saveOrUpdate(record_log);
                            baseCommonDao.flush();
                        }

                        //登簿后更新登簿人
                        if (djdys.size() > 0) {
                            for (BDCS_DJDY_GZ djdygz : djdys) {
                                List<BDCS_QL_GZ> xqlgzs = baseCommonDao.getDataList(BDCS_QL_GZ.class, " DJDYID = '" + djdygz.getDJDYID() + "' AND XMBH='" + xmbh + "'");
                                for (BDCS_QL_GZ qlgz : xqlgzs) {
                                    qlgz.setDBR(dbr);
                                    baseCommonDao.saveOrUpdate(qlgz);
                                    baseCommonDao.flush();
                                    if ((!"".equals(zxdbr)) && zxdbr != null && (!"null".equals(zxdbr))) {

                                        //登簿后更新注销登簿人 -- 更新当前权利的注销登簿人，用于前端显示
                                        List<BDCS_FSQL_GZ> fsqlgzs = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, " QLID = '" + qlgz.getId() + "' AND ZXSJ IS NOT NULL "
                                                + " AND ZXDYYWH = '" + xmxxs.get(0).getPROJECT_ID() + "'");
                                        if (fsqlgzs.size() > 0) {
                                            BDCS_FSQL_GZ fsqlgz = fsqlgzs.get(0);
                                            fsqlgz.setZXDBR(zxdbr);
                                            baseCommonDao.saveOrUpdate(fsqlgz);
                                            baseCommonDao.flush();
                                        }
                                        //登簿后更新注销登簿人 -- 更新来源权利的注销登簿人
                                        List<BDCS_FSQL_LS> fsqllss = baseCommonDao.getDataList(BDCS_FSQL_LS.class, " QLID = '" + qlgz.getLYQLID() + "' AND ZXSJ IS NOT NULL "
                                                + " AND ZXDYYWH = '" + xmxxs.get(0).getPROJECT_ID() + "'");
                                        if (fsqllss.size() > 0) {
                                            BDCS_FSQL_LS fsqlls = fsqllss.get(0);
                                            fsqlls.setZXDBR(zxdbr);
                                            baseCommonDao.saveOrUpdate(fsqlls);
                                            baseCommonDao.flush();
                                        }

                                    }
                                }
                            }
                        }

                        List<BDCS_QL_XZ> qlxzs = baseCommonDao.getDataList(BDCS_QL_XZ.class, " XMBH = '" + xmbh + "'");
                        if (qlxzs.size() > 0) {
                            for (BDCS_QL_XZ qlxz : qlxzs) {
                                qlxz.setDBR(dbr);
                                baseCommonDao.saveOrUpdate(qlxz);
                                baseCommonDao.flush();
                            }
                        }
                        List<BDCS_QL_LS> qllss = baseCommonDao.getDataList(BDCS_QL_LS.class, " XMBH = '" + xmbh + "'");
                        if (qllss.size() > 0) {
                            for (BDCS_QL_LS qlls : qllss) {
                                qlls.setDBR(dbr);
                                baseCommonDao.saveOrUpdate(qlls);
                                baseCommonDao.flush();
                            }
                        }
                        updateRecordLog(recordLog, "1", "自动办理自动登簿成功", "0006");
                        //==========================9.转出善证=========================================
                        List<Wfi_ProInst> szpro = baseCommonDao.getDataList(Wfi_ProInst.class, " proinst_id = '" + djproinst_id + "'");
                        List<Wfi_ActInst> szyact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "' and ACTINST_STATUS='1'  order by actinst_start  desc ");
                        List<Wfd_Route> szroutes = baseCommonDao.getDataList(Wfd_Route.class, " prodef_id = '" + inst.getProdef_Id() + "' and  actdef_id = '" + szyact.get(0).getActdef_Id() + "'");
                        String szrouteid = "";
                        String szactinstid = "";
                        if (szyact.size() > 0) {
                            szactinstid = szyact.get(0).getActinst_Id();
                        }
                        if (szroutes.size() > 0) {
                            szrouteid = szroutes.get(0).getRoute_Id();
                        }
                        SmObjInfo szsuccessString = null;
                        List<SmObjInfo> szobjInfos = new ArrayList<SmObjInfo>();
                        SmObjInfo szobjInfo = new SmObjInfo();
                        szobjInfo.setID(szryid);// 设置缮证人staffid
                        szobjInfo.setName(szry);// 设置缮证人staffName
                        szobjInfos.add(szobjInfo);
                        successString = exePassover(szactinstid, szrouteid,
                                msgtext, szobjInfos, more, dbrid);
                        if (!"转出成功".equals(successString.getDesc())) {
                            YwLogUtil.addYwLog("自动办理转出善证失败" + ywlsh, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
                            throw new BeresolvException(recordLog, "2", "自动办理成功-但转出善证失败", "2006");
                        } else {
                            updateRecordLog(recordLog, "2", "自动办理成功！", "0000");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new AfterBoardException(recordLog, "2", "登簿后出现异常:" + rmdb.getMsg(), "3002");
                    }
                }

                
            } else {
                throw new BeresolvException(recordLog, "0", "获取流程信息异常！", "2002");
            }
        } catch (AfterBoardException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            //未知错误，这种错误往往是代码错误
            if (!StringHelper.isEmpty(djproinst_id)) {
                ProInstService.deleteProInst(djproinst_id, blryid, blry, "抵押平台智能审批过程出现异常");
            }
            throw e;
        }
        return prolsh;
    }
    
    
    /*****************************************3.创建项目 start*****************************************/
    /**
     * 创建项目一直到登簿
     */
    public String accectProject_enterprise(JSONObject jsonObject, final LOG_DECLARE_RECORD_LOG recordLog, String requestcode, HttpServletRequest request) throws Exception {
        String prolsh = "";
        JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
        JSONArray sqrList = jsonObject.getJSONArray("sqrList");
        JSONArray dydyList = jsonObject.getJSONArray("dydyList");
//        JSONArray promaterList = jsonObject.getJSONArray("promaterList");
        JSONArray arrayMaterclass = jsonObject.getJSONArray("arrayMaterclass");
        JSONArray arrayMaterdata = jsonObject.getJSONArray("arrayMaterdata");

        String ywlsh = basicInfo.getString("ENTERPRISEID");
        String projectname = basicInfo.getString("ENTERPRISENAME");
        String qxdm = basicInfo.getString("QXDM");
        //修改对应映射关系
        String outywlcid = basicInfo.getString("TYPE");
        String djproinst_id = "";
        String actinstid = "";
        String blry = "", blryid = "";
        String enterpriseid=basicInfo.getString("ENTERPRISEID");;
        try {
            List<ENTERPRISEPRODEF> apcs = baseCommonDao.getDataList(ENTERPRISEPRODEF.class, " type = '" + outywlcid + "'");
            if (apcs == null || apcs.size() == 0) {
                throw new BeresolvException(recordLog, "2", "读取自动办理流程配置失败", "2001");
            }
            //String djyy = apcs.get(0).getDJYY(); //
            //String fj = apcs.get(0).getFJ(); //获取配置中的附记
            //String shyj = apcs.get(0).getSPYJ();    //获取配置中的审核意见
            blry = apcs.get(0).getUSERNAME(); //获取配置中的办理人员
            blryid = apcs.get(0).getUSERID(); //办理人员id
            //String dbr = apcs.get(0).getDBR();//获取配置中的登簿人
            //String dbrid = apcs.get(0).getDBRID();//获取配置中的登簿人id
            //String isYDB = apcs.get(0).getISYDB();//是否属于预登簿模式
            //String isHangUp = apcs.get(0).getISHANGUP();//是否挂起
            //String szry = apcs.get(0).getSZRY();//获取配置中的缮证人
            //String szryid = apcs.get(0).getSZRYID();//获取配置中的缮证人id
            String routeid = ""; //获取缮证人的角色id
            //String zxdbr = StringHelper.formatObject(apcs.get(0).getZXDBR());//注销登簿人
           // String sfzdhqqzh = apcs.get(0).getSFZDHQQZH();//是否自动获取权证号
            updateRecordLog(recordLog, "1", "", "0008");

            // 若附记与登记原因为空，将提取自动办理流程配置的默认附记与默认登记原因
//            if (StringHelper.isEmpty(basicInfo.getString("FJ"))) {
//                basicInfo.put("FJ", fj);
//            }
//            if (StringHelper.isEmpty(basicInfo.getString("DJYY"))) {
//                basicInfo.put("DJYY", djyy);
//            }
//            if (StringHelper.isEmpty(basicInfo.getString("ZXFJ"))) {
//                basicInfo.put("ZXFJ", fj);
//            }
//            if (StringHelper.isEmpty(basicInfo.getString("ZXDJYY"))) {
//                basicInfo.put("ZXDJYY", djyy);
//            }
            jsonObject.put("basicInfo", basicInfo);
            String ywlcid = apcs.get(0).getPRODEF_ID();
            //----------------------- 1.创建项目信息实体 -----------------------
            Wfd_Prodef prodef = smProDef.GetProdefById(ywlcid);
            if (prodef != null) {
                SmProInfo info = new SmProInfo();
                info.setProDef_ID(ywlcid);
                info.setBatch(UUID.randomUUID().toString().replace("-", ""));
                String proDefName = smProDef.getproDefName(ywlcid);
                info.setProDef_Name(proDefName);
//                logger.info("REmarks_________+++++"+remarks);
//                if(remarks!=null&&!"".equals(remarks)){
//                	info.setMessage("抵押平台驳回件,驳回原因："+remarks);
//                }else{
//                	info.setMessage("由抵押平台核心API自动创建");
//                }
                info.setFile_Urgency("1");
                info.setLCBH(prodef.getProdef_Code());
                SmObjInfo smObjInfo = new SmObjInfo();
                smObjInfo.setID(blryid);
                smObjInfo.setName(blry);
                List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
                staffList.add(smObjInfo);
                info.setAcceptor(blry);
                info.setStaffID(blryid);
                info.setFile_Urgency("1");
                info.setYWLY("4");
                info.setYwh(outywlcid);
                StringBuilder sqr = new StringBuilder();
                for (int j = 0; j < sqrList.size(); j++) {
                    String sqrmc = sqrList.getJSONObject(j).getString("SQRMC");
                    if (!StringHelper.isEmpty(sqrmc)) {
                        if (sqr.length() > 0) {
                            sqr.append("、").append(sqrmc);
                        } else {
                            sqr.append(sqrmc);
                        }
                        if (j > 2) {
                            sqr.append(" 等");
                            break;
                        }
                    }
                }
                info.setProjectName(projectname);
                info.setSQStartTime(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd HH:mm:ss"));
                // ----------------------- 2.创建受理项目 -----------------------
                SmObjInfo returnSmObj = smProInstService.Accept(info, staffList);
                djproinst_id = returnSmObj.getID();
                SmObjInfo smInfo = returnSmObj.getChildren().get(0);
                prolsh = smInfo.getID();
                Wfi_ProInst inst = baseCommonDao.get(Wfi_ProInst.class, djproinst_id);
                inst.setProdef_Name(proDefName);
                inst.setWLSH(ywlsh);
                inst.setYWLY("4");
                baseCommonDao.update(inst);
                if("1".equals(outywlcid)){
                	insertQYXX(inst.getFile_Number(), basicInfo);
                }
                
                // 项目编号
                
                // ----------------------- 3.创建申请人信息 -----------------------
                if(!"3".equals(outywlcid)){
                	createSqr_enterprise(sqrList,enterpriseid,inst.getFile_Number());
                }
                // 导入申报材料,调用登记系统附件下载接口，将申报材料信息传递过去，由登记系统直接连 mongodb下载图片到本地服务器
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("PROINST_ID", inst.getProinst_Id());
                map.put("userId", blryid);
                map.put("userName", blry);
                // 资料目录实例
                map.put("MATERCLASS", arrayMaterclass);
                // 资料文件
                map.put("MATERDATA", arrayMaterdata);
                final JSONObject object = new JSONObject();
                object.put("PROMATER", map);
                // 附件下载地址（登记系统接口）：http://127.0.0.1:8080/realestate/app/intelligentcore/attachmentDownload
                final String attachment_url = ConfigHelper.getNameByValue("ATTACHMENT_URL");
                if (StringHelper.isEmpty(attachment_url)) {
                    throw new ManualException(recordLog, "0", "获取附件下载接口地址异常，请联系管理员进行配置", "2004");
                }
                String result = HttpClientUtil.requestPost(object.toJSONString(), attachment_url);
                if (StringHelper.isEmpty(result)) {
                    // 附件下载接口接口暂时无法访问
                    throw new ManualException(recordLog, "0", "附件下载接口接口暂时无法访问，请联系管理员", "2004");
                }
                JSONObject parseObject = new JSONObject();
                try{
                	parseObject = JSONObject.parseObject(result);
                }catch (Exception ex) {
                	throw new ManualException(recordLog, "0", "调用附件下载接口发生异常，接口返回值异常", "2004");
                }
                if (parseObject.isEmpty()) {
                    // 调用附件下载接口发生异常，返回null
                    throw new ManualException(recordLog, "0", "调用附件下载接口发生异常，无返回值", "2004");
                }
                Boolean state = parseObject.getBoolean("state");
                String msg1 = parseObject.getString("msg");
                if (!state) {
                    // 附件下载失败
                    throw new ManualException(recordLog, "0", "附件下载失败，详情：" + msg1, "2004");
                }
                updateRecordLog(recordLog, "1", "附件下载成功", "0001");
                /*taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 是否异步下载附件，异步下载失败存在不中断项目继续创建的情况，待确定
                        String result = HttpClientUtil.requestPost(object.toJSONString(), attachment_url);
                    }
                });*/

                actinstid = returnSmObj.getName();
                //创建成功后添加业务号到日志
                recordLog.setYWLSH(inst.getProlsh());
                updateRecordLog(recordLog, "1", "创建申报项目成功", "0002");
                // ----------------------- 4.选择单元 -----------------------
                JsonMessage msg = new JsonMessage();
                if(!"2".equals(outywlcid)){
                	msg = selectDY_enterprise(ywlsh,inst.getFile_Number(), dydyList);
                	if (!msg.getState()) {
                    	System.out.println(msg.getMsg());
                        throw new ManualException(recordLog, "0", "受理单元异常：" + msg.getMsg(), "2004");
                    }
                }
                
                updateRecordLog(recordLog, "1", "受理选择单元成功", "0003");

                // ----------------------- 5.插入权利 -----------------------
                //insertQLForData.inserQLByData(projectInfo.getXmbh(), jsonObject, prodef);
                updateRecordLog(recordLog, "1", "插入权利成功", "0004");

                List<Wfi_ActInst> yact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "'  order by actinst_start  desc ");
                List<Wfd_Route> routes = baseCommonDao.getDataList(Wfd_Route.class, " prodef_id = '" + inst.getProdef_Id() + "' and  actdef_id = '" + yact.get(0).getActdef_Id() + "'");
//                List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, " project_id = '" + inst.getFile_Number() + "'");
//                String xmbh = xmxxs.get(0).getId();
//                if (routes.size() > 0) {
//                    routeid = routes.get(0).getRoute_Id();
//                }
//
//                BDCS_XMXX xmxx = xmxxs.get(0);
//                if (xmxx != null) {
//                    xmxx.setXMMC(sqr.toString());
//                    xmxx.setWLSH(ywlsh);
//                    xmxx.setSLRY(staffList.get(0).getName());
//                    xmxx.setSLRYID(staffList.get(0).getID());
//                    xmxx.setYWLSH(smInfo.getID());
////                    baseCommonDao.getCurrentSession().clear();
//                    baseCommonDao.saveOrUpdate(xmxx);
////                    baseCommonDao.flush();
//                }

                // ----------------------- 6.生成产权证号 -----------------------
//                List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='" + xmbh + "' ");
//
//                if(djdys.isEmpty()) {
//                    throw new BeresolvException(recordLog, "2", "项目信息关联的djdy查无数据", "2003");
//                }
//
//                // 默认自动获取
//                if (!"0".equals(sfzdhqqzh)) {
//                    List<BDCS_QL_GZ> qlgzs = baseCommonDao.getDataList(BDCS_QL_GZ.class, " djdyid = '" + djdys.get(0).getDJDYID() + "' and xmbh='" + xmbh + "'");
//                    //2017年9月19日10:25:31 添加自动获取权证号
//                    for (BDCS_QL_GZ ql_gz : qlgzs) {
//                        if ("23".equals(ql_gz.getQLLX()) || "700".equals(ql_gz.getDJLX())) {
//                            String type = "GETBDCDJZM"; // 标示执行存储过程时生成不动产证明号
//                            String bdcqzh = djbService.createBDCZMHByQLLX(xmbh, ql_gz.getQLLX(), type);
//                        } else {
//                            String type = "GETBDCQZH"; // 标示执行存储过程时生成不动产权证号
//                            String bdcqzh = djbService.createBDCQZHByQLLX(xmbh, ql_gz.getQLLX(), type);
//                        }
//                    }
//                    updateRecordLog(recordLog, "1", "生成权证号成功", "0005");
//                } else {
//                    updateRecordLog(recordLog, "1", "不自动生成权证号", "0005");
//                }

                //==========================添加审批意见=========================================
//                //（1）初审意见
//                smProSPService.SaveApproval("", "1", "CS", actinstid, shyj, "", "", "", null, "", blryid, blry);
//                //（2）复审意见
//                smProSPService.SaveApproval("", "2", "FS", actinstid, shyj, "", "", "", null, "", blryid, blry);
//                //（1）核定意见
//                smProSPService.SaveApproval("", "3", "HD", actinstid, shyj, "", "", "", null, "", blryid, blry);

                //---------------------------------添加收费项--------------------------------------
               // insertDJSF(xmxx,djsfList,qlrList);
                //暂时不转出
//                if(false) {
//                	// ----------------------- 7.登簿转出区(转出到登簿环节) -----------------------
//                    String msgtext = "转出附言";
//                    boolean more = false;
//                    List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
//                    SmObjInfo objInfo = new SmObjInfo();
//                    objInfo.setID(dbrid);// 设置staffed
//                    objInfo.setName(dbr);// 设置staffName
//                    objInfos.add(objInfo);
//                    //调用转出方法
//                    SmObjInfo successString = exePassover(actinstid, routeid,
//                            msgtext, objInfos, more, blryid);
//                    if (!"转出成功".equals(successString.getDesc())) {
//                        YwLogUtil.addYwLog("自动办理转出登簿失败" + ywlsh, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
//                        throw new BeresolvException(recordLog, "2", "自动办理成功-但转出登簿失败", "2006");
//                    } else {
//                        YwLogUtil.addYwLog("自动办理转出登簿成功" + ywlsh, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
//                    }
//
//                    updateRecordLog(recordLog, "1", "自动办理转出登簿成功", "0006");
//                    //生成智能审批报告
//                    final String file_number = inst.getFile_Number();
//                    if (!StringHelper.isEmpty(file_number)) {
//                        try {
//                            //获取当前IP跟端口
//                            String host = request.getLocalAddr();
//                            int port = request.getLocalPort();
//                            final String url = "http://" + host + ":" + port + "/realestate/app/intelligent/createReport?file_number=" + file_number;
//                            taskExecutor.execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //intelligentService.createReport(file_number);
//                                    CommonsHttpInvoke chi = new CommonsHttpInvoke();
//                                    String result = chi.commonHttpDoPostNotice(null, null, url, file_number);
//                                    System.out.println("调用智能审批报告接口返回："+result);
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            System.out.println("生成智能审批报告失败");
//                        }
//                    }
//
//                    //预登簿，到此结束
//                    if("1".equals(isYDB)) {
//                        try {
//                            List<Wfi_ActInst> dbyact = baseCommonDao.getDataList(Wfi_ActInst.class, " PROINST_ID = '" + djproinst_id + "' AND ACTINST_STATUS='1'  ORDER BY ACTINST_START  DESC ");
//                            //是否自动挂起
//                            if("1".equals(isHangUp)){
//                                autoSmActInst.setAutoHangUp(dbyact.get(0).getActinst_Id(), "系统自动挂起操作", blryid, blry);
//                            }
//                        } catch(Exception e) {
//                            throw new AfterBoardException(recordLog, "2", "登簿后出现异常:" + e.getMessage(), "3002");
//                        }
//
//                        updateRecordLog(recordLog, "2", "已完成预登簿，等待审核登簿", "0000");
//                        return prolsh;
//                    }
//                    // ----------------------- 8.自动登簿区 -----------------------
//                    ResultMessage rmdb = dbService.BoardBook(xmbh);
//                    if ("false".equals(rmdb.getSuccess()) || rmdb == null || "warning".equals(rmdb.getSuccess())) {
//                        YwLogUtil.addYwLog("登簿-失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
//                        if (rmdb.getMsg().contains("null")) {
//                            throw new BeresolvException(recordLog, "2", "登簿失败原因:登簿发生未知错误。null", "2007");
//                        } else {
//                            throw new BeresolvException(recordLog, "2", "登簿失败原因:" + rmdb.getMsg(), "2007");
//
//                        }
//                    }
//                    //登簿后异常单独捕获，否则登簿完成报错，登簿不会回滚，但项目会被删除
//                    try {
//
//                        //登簿完成后更新是否登簿状态
//                        if (!"1".equals(xmxx.getSFDB())) {
//                            xmxx.setSFDB("1");
////                            baseCommonDao.getCurrentSession().clear();
//                            baseCommonDao.saveOrUpdate(xmxx);
//                            baseCommonDao.flush();
//                        }
//
//                        //更新日志登簿状态
//                        List<LOG_DECLARE_RECORD_LOG> dataList = baseCommonDao.getDataList(LOG_DECLARE_RECORD_LOG.class, " SBYWH ='" + ywlsh + "' AND (SFDB <> '1' OR SFDB IS NULL) ");
//                        if (dataList != null && dataList.size() > 0) {
//                            LOG_DECLARE_RECORD_LOG record_log = dataList.get(0);
//                            record_log.setSFDB("1");
////                            baseCommonDao.getCurrentSession().clear();
//                            baseCommonDao.saveOrUpdate(record_log);
//                            baseCommonDao.flush();
//                        }
//
//                        //登簿后更新登簿人
//                        if (djdys.size() > 0) {
//                            for (BDCS_DJDY_GZ djdygz : djdys) {
//                                List<BDCS_QL_GZ> xqlgzs = baseCommonDao.getDataList(BDCS_QL_GZ.class, " DJDYID = '" + djdygz.getDJDYID() + "' AND XMBH='" + xmbh + "'");
//                                for (BDCS_QL_GZ qlgz : xqlgzs) {
//                                    qlgz.setDBR(dbr);
//                                    baseCommonDao.saveOrUpdate(qlgz);
//                                    baseCommonDao.flush();
//                                    if ((!"".equals(zxdbr)) && zxdbr != null && (!"null".equals(zxdbr))) {
//
//                                        //登簿后更新注销登簿人 -- 更新当前权利的注销登簿人，用于前端显示
//                                        List<BDCS_FSQL_GZ> fsqlgzs = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, " QLID = '" + qlgz.getId() + "' AND ZXSJ IS NOT NULL "
//                                                + " AND ZXDYYWH = '" + xmxxs.get(0).getPROJECT_ID() + "'");
//                                        if (fsqlgzs.size() > 0) {
//                                            BDCS_FSQL_GZ fsqlgz = fsqlgzs.get(0);
//                                            fsqlgz.setZXDBR(zxdbr);
//                                            baseCommonDao.saveOrUpdate(fsqlgz);
//                                            baseCommonDao.flush();
//                                        }
//                                        //登簿后更新注销登簿人 -- 更新来源权利的注销登簿人
//                                        List<BDCS_FSQL_LS> fsqllss = baseCommonDao.getDataList(BDCS_FSQL_LS.class, " QLID = '" + qlgz.getLYQLID() + "' AND ZXSJ IS NOT NULL "
//                                                + " AND ZXDYYWH = '" + xmxxs.get(0).getPROJECT_ID() + "'");
//                                        if (fsqllss.size() > 0) {
//                                            BDCS_FSQL_LS fsqlls = fsqllss.get(0);
//                                            fsqlls.setZXDBR(zxdbr);
//                                            baseCommonDao.saveOrUpdate(fsqlls);
//                                            baseCommonDao.flush();
//                                        }
//
//                                    }
//                                }
//                            }
//                        }
//
//                        List<BDCS_QL_XZ> qlxzs = baseCommonDao.getDataList(BDCS_QL_XZ.class, " XMBH = '" + xmbh + "'");
//                        if (qlxzs.size() > 0) {
//                            for (BDCS_QL_XZ qlxz : qlxzs) {
//                                qlxz.setDBR(dbr);
//                                baseCommonDao.saveOrUpdate(qlxz);
//                                baseCommonDao.flush();
//                            }
//                        }
//                        List<BDCS_QL_LS> qllss = baseCommonDao.getDataList(BDCS_QL_LS.class, " XMBH = '" + xmbh + "'");
//                        if (qllss.size() > 0) {
//                            for (BDCS_QL_LS qlls : qllss) {
//                                qlls.setDBR(dbr);
//                                baseCommonDao.saveOrUpdate(qlls);
//                                baseCommonDao.flush();
//                            }
//                        }
//                        updateRecordLog(recordLog, "1", "自动办理自动登簿成功", "0006");
//                        //==========================9.转出善证=========================================
//                        List<Wfi_ProInst> szpro = baseCommonDao.getDataList(Wfi_ProInst.class, " proinst_id = '" + djproinst_id + "'");
//                        List<Wfi_ActInst> szyact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "' and ACTINST_STATUS='1'  order by actinst_start  desc ");
//                        List<Wfd_Route> szroutes = baseCommonDao.getDataList(Wfd_Route.class, " prodef_id = '" + inst.getProdef_Id() + "' and  actdef_id = '" + szyact.get(0).getActdef_Id() + "'");
//                        String szrouteid = "";
//                        String szactinstid = "";
//                        if (szyact.size() > 0) {
//                            szactinstid = szyact.get(0).getActinst_Id();
//                        }
//                        if (szroutes.size() > 0) {
//                            szrouteid = szroutes.get(0).getRoute_Id();
//                        }
//                        SmObjInfo szsuccessString = null;
//                        List<SmObjInfo> szobjInfos = new ArrayList<SmObjInfo>();
//                        SmObjInfo szobjInfo = new SmObjInfo();
//                        szobjInfo.setID(szryid);// 设置缮证人staffid
//                        szobjInfo.setName(szry);// 设置缮证人staffName
//                        szobjInfos.add(szobjInfo);
//                        successString = exePassover(szactinstid, szrouteid,
//                                msgtext, szobjInfos, more, dbrid);
//                        if (!"转出成功".equals(successString.getDesc())) {
//                            YwLogUtil.addYwLog("自动办理转出善证失败" + ywlsh, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
//                            throw new BeresolvException(recordLog, "2", "自动办理成功-但转出善证失败", "2006");
//                        } else {
//                            updateRecordLog(recordLog, "2", "自动办理成功！", "0000");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new AfterBoardException(recordLog, "2", "登簿后出现异常:" + rmdb.getMsg(), "3002");
//                    }
//                }

                
            } else {
                throw new BeresolvException(recordLog, "0", "获取流程信息异常！", "2002");
            }
        } catch (AfterBoardException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            //未知错误，这种错误往往是代码错误
            if (!StringHelper.isEmpty(djproinst_id)) {
                ProInstService.deleteProInst(djproinst_id, blryid, blry, "抵押平台智能审批过程出现异常");
            }
            throw e;
        }
        return prolsh;
    }
    
    private List<SmObjInfo> getSmobjInfos(String roleid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		List<UserRole> users = baseCommonDao.getDataList(UserRole.class, " roleid='"+roleid+"'");
		if(users!=null&&users.size()>0){
			for(UserRole user : users){
				SmObjInfo info = new SmObjInfo();
				info.setID(user.getUser().getId());
                info.setName(user.getUser().getUserName());
                list.add(info);
			}
		}
		return list;
	}

    /**
     * 插入djsf项
     * @param xmxx
     * @param djsfList
     * @param qlrList
     */
    private void insertDJSF(BDCS_XMXX xmxx, JSONArray djsfList, JSONArray qlrList) {
        StringBuilder qlrmcs = new StringBuilder();
        for(int i=0;i<qlrList.size();i++) {
            JSONObject qlr = qlrList.getJSONObject(i);
            qlrmcs.append(qlr.getString("SQRMC") + ",");
        }
        qlrmcs.deleteCharAt(qlrmcs.lastIndexOf(","));

        for(int i=0;i<djsfList.size();i++) {
            JSONObject sfdy = djsfList.getJSONObject(i);

            BDCS_DJSF sf = new BDCS_DJSF();
            sf.setYWH(xmxx.getPROJECT_ID());
            sf.setSFDW(sfdy.getString("sfdw"));
            // 实收应收都取传递过来的收费基数字段
            sf.setSSJE(sfdy.getString("sfjs"));
            sf.setYSJE(sfdy.getDouble("sfjs"));
            sf.setSFJS(sfdy.getDouble("sfjs"));
            sf.setSFLX(sfdy.getString("sflx"));
            sf.setSFKMMC(sfdy.getString("sfkmmc"));
            sf.setXMBH(xmxx.getId());
            sf.setMJJS(sfdy.getDouble("mjjs"));
            sf.setMJZL(sfdy.getDouble("mjzl"));
            sf.setSFZL(sfdy.getDouble("sfzl"));
            sf.setSFSX(sfdy.getDouble("zlfysx"));
            sf.setSFBL(sfdy.getDouble("sfbl"));
            sf.setSFBMMC(sfdy.getString("sfbmmc"));
            sf.setJFDW("元");
            sf.setSFEWSF(ConstValue.SF.NO.Value);
            sf.setSFDYID(sfdy.getString("id"));
            sf.setSFDW(ConfigHelper.getNameByValue("djjgmc"));
            sf.setJSGS(sfdy.getString("jsgs"));
            sf.setBZ(sfdy.getString("bz"));
            sf.setCALTYPE(sfdy.getString("caltype"));
            sf.setSQLEXP(sfdy.getString("sqlexp"));
            sf.setXSGS(sfdy.getString("jsgs"));
            sf.setTS(1);
            sf.setQLRMC(qlrmcs.toString());

            baseCommonDao.save(sf);
        }
    }

    /**
     * 转出
     *
     * @param actinstid
     * @param routeidString
     * @param msg
     * @param staffobjInfos
     * @param more
     * @return
     */
    private SmObjInfo exePassover(String actinstid, String routeidString,
                                  String msg, List<SmObjInfo> staffobjInfos, boolean more, String blryid) {
        SmObjInfo successString = null;
        String operaStaffString = blryid;// 当前操作人员

        if (autoOperationService.BeforePassOver()) {// 转出前
            successString = autoOperationService.PassOver(routeidString, actinstid,
                    staffobjInfos, operaStaffString, msg, more);
        }
        return successString;
    }

    /**
     * 选择单元
     * @return
     */
    public JsonMessage selectDY(String xmbh, JSONArray dydyList, JSONArray sqrList, Wfd_Prodef prodef, String requestcode, JSONArray qlrList) throws Exception {
        JsonMessage msg = new JsonMessage();
        // 上面已经做过校验，这里就直接取了
        // 1、根据流程获取到对应的选择器
        List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + prodef.getProdef_Code() + "'");
        // 获取基准流程
        T_BASEWORKFLOW base_wf = baseCommonDao.get(T_BASEWORKFLOW.class, mappings.get(0).getWORKFLOWNAME());
        String selectorid = base_wf.getSELECTORID();
        ResultMessage resultMessage = new ResultMessage();
        List list_bdcdyh = new ArrayList();
        if (requestcode.contains("2002") || requestcode.contains("2004")) {
            //抵押变更、抵押注销用权证号选择单元
            for (int i = 0; i < qlrList.size(); i++) {
                String bdcdyh = null;
                String bdcqzh = qlrList.getJSONObject(i).getString("BDCQZH");
                if(!StringHelper.isEmpty(bdcqzh)) {
                    // 如果是分别持证的权证号，他们的单元都是同一个，这里需要进行重复选择单元过滤
                    String sql = "SELECT DISTINCT BDCDYH FROM BDCK.BDCS_QL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ T WHERE T.BDCQZH='" + bdcqzh + "') ";
                    List<Map> bdcdyhs = baseCommonDao.getDataListByFullSql(sql);
                    if(bdcdyhs.isEmpty()) {
                        throw new ManualException("无法从填写的权证号检索到单元，请联系办理所在地不动产登记中心工作人员排查数据是否异常");
                    }
                    if(list_bdcdyh.contains(bdcdyhs.get(0).get("BDCDYH"))) {
                        continue;
                    } else {
                        list_bdcdyh.add(bdcdyhs.get(0).get("BDCDYH"));
                    }
                    resultMessage = selectDYByQZHAndBdcdyh(xmbh, bdcqzh, bdcdyh, selectorid);
                    if ("false".equals(resultMessage.getSuccess())) {
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < dydyList.size(); i++) {
                String bdcdyh = dydyList.getJSONObject(i).getString("BDCDYH");
                String bdcqzh = null;
                if (list_bdcdyh.contains(bdcdyh)) {
                    continue;
                }
                list_bdcdyh.add(bdcdyh);
                resultMessage = selectDYByQZHAndBdcdyh(xmbh, bdcqzh, bdcdyh, selectorid);
                if ("false".equals(resultMessage.getSuccess())) {
                    break;
                }
            }
        }
        msg.setState(Boolean.parseBoolean(resultMessage.getSuccess()));
        msg.setMsg(resultMessage.getMsg());
        return msg;
    }
    
    @Transactional
    public JsonMessage selectDY_enterprise(String qyid,String project_id, JSONArray dydyList) throws Exception {
        JsonMessage msg = new JsonMessage();
        ResultMessage resultMessage = new ResultMessage();
        // 上面已经做过校验，这里就直接取了
        // 1、根据流程获取到对应的选择器
        // 获取基准流程
        BDCS_QYXX qyxxs = baseCommonDao.get(BDCS_QYXX.class, qyid);
        if(qyxxs!=null){
        	for(int i = 0;i<dydyList.size();i++){
            	List<BDCS_QYDYGX> gxs = baseCommonDao.getDataList(BDCS_QYDYGX.class, "wzdid='"+dydyList.getJSONObject(i).getString("id")+"'");
            	if(gxs==null||gxs.size()==0){
            		BDCS_QYDYGX gx = new BDCS_QYDYGX();
            		gx.setQYID(qyid);
            		gx.setBDCDYH(dydyList.getJSONObject(i).getString("BDCDYH"));
            		gx.setWZDID(dydyList.getJSONObject(i).getString("ID"));
            		gx.setPROJECT_ID(project_id);
            		gx.setSHZT("0");
            		baseCommonDao.save(gx);
            		resultMessage.setMsg("申请成功");
            		resultMessage.setSuccess("true");
            	}else{
            		resultMessage.setMsg("该土地已申请");
            		resultMessage.setSuccess("false");
            	}
            }
        }else{
        	resultMessage.setMsg("没有企业信息");
    		resultMessage.setSuccess("false");
        }
        baseCommonDao.flush();
        msg.setState(Boolean.parseBoolean(resultMessage.getSuccess()));
        msg.setMsg(resultMessage.getMsg());
        return msg;
    }
    
    @Transactional
    public JsonMessage insertQYXX(String project_id,JSONObject jsonobject){
    	JsonMessage msg = new JsonMessage();
        ResultMessage resultMessage = new ResultMessage();
        // 上面已经做过校验，这里就直接取了
        // 1、根据流程获取到对应的选择器
        // 获取基准流程
        BDCS_QYXX qyxx = baseCommonDao.get(BDCS_QYXX.class, jsonobject.getString("ENTERPRISEID"));
        if(qyxx!=null){
        	resultMessage.setMsg("该企业已申请过");
    		resultMessage.setSuccess("false");
        }else{
        	BDCS_QYXX qyxxnew = new BDCS_QYXX();
        	qyxxnew.setId(jsonobject.getString("ENTERPRISEID"));
        	qyxxnew.setPROJECT_ID(project_id);
        	qyxxnew.setQYDZ(jsonobject.getString("ENTERPRISEADDRESS"));
        	qyxxnew.setQYMC(jsonobject.getString("ENTERPRISENAME"));
        	qyxxnew.setTYSHXYDM(jsonobject.getString("ENTERPRISECODE"));
        	qyxxnew.setFDDBR(jsonobject.getString("FRDBXM"));
        	qyxxnew.setFDDBRZJH(jsonobject.getString("FRDBZJHM"));
        	qyxxnew.setZCZXM(jsonobject.getString("REGISTERNAME"));
        	qyxxnew.setZCZZJH(jsonobject.getString("REGISTERZJHM"));
        	qyxxnew.setZCZSJH(jsonobject.getString("REGISTERPHONE"));
        	qyxxnew.setSHZT("0");
        	baseCommonDao.save(qyxxnew);
        }
        baseCommonDao.flush();
        msg.setState(Boolean.parseBoolean(resultMessage.getSuccess()));
        msg.setMsg(resultMessage.getMsg());
        return msg;
    } 

    /**
     * 通过通过不动产单元号和权证号进行选择单元
     *
     * @param xmbh
     * @param qzh
     * @param selectorid
     * @return
     */
    @Transactional
    public ResultMessage selectDYByQZHAndBdcdyh(String xmbh, String qzh, String bdcdyh, String selectorid)
    {
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setSuccess("true");
        try
        {
            Map<String,String> map = new HashMap<String, String>();
            map.put("BDCQZH",qzh);
            map.put("BDCDYH",bdcdyh);

            Message msg = SelectorTools.queryDataForRunOnce(selectorid,map);
            List<Map> list = null;
            if(msg!=null && msg.getRows()!=null){
                list=(List<Map>)msg.getRows();
            }
            String idKey = "";
            T_SELECTOR selector = baseCommonDao.get(T_SELECTOR.class, selectorid);
            if (selector != null) {
                String idfieldname = selector.getIDFIELDNAME();
                if (!StringHelper.isEmpty(idfieldname)) {
                    idKey = idfieldname;
                } else {
                    throw new Exception("选择器配置错误，标识字段IDFIELDNAME不能为空，ID=" + selectorid);
                }
            } else {
                throw new Exception("未获取到对应选择器，ID=" + selectorid);
            }
            net.sf.json.JSONArray rows = net.sf.json.JSONArray.fromObject(list);
            StringBuilder ids = new StringBuilder();
            if (rows.size() > 0) {
                for (int i = 0; i < rows.size(); i++) {
                    net.sf.json.JSONObject object = (net.sf.json.JSONObject) rows.get(i);
                    String id = object.getString(idKey);
                    if (ids.length() == 0) {
                        ids.append(id);
                    } else {
                        ids.append("," + id);
                    }
                }
            }
            // 3、添加单元
            if (ids.length() > 0) {
                resultMessage = dyService.addBDCDY(xmbh, ids.toString());
            } else {
                resultMessage.setSuccess("false");
                resultMessage.setMsg("未找到对应单元！");
            }
            
        } catch (Exception ex) {
            resultMessage.setSuccess("false");
            resultMessage.setMsg("添加单元失败，失败详情："+ex.getMessage());
            ex.printStackTrace();
        }
        return resultMessage;
    }

    @Transactional
    public void createSqr_enterprise(JSONArray sqrList,String qyid,String project_id) {
        for (int i = 0; i < sqrList.size(); i++) {
        	List<BDCS_QYRY> qyrys = baseCommonDao.getDataList(BDCS_QYRY.class, "loginname='"+sqrList.getJSONObject(i).getString("USERNAME")+"'");
        	if(qyrys==null||qyrys.size()==0){
        		BDCS_QYRY sqr = new BDCS_QYRY();
        		sqr.setQYID(qyid);
        		sqr.setRYMC(sqrList.getJSONObject(i).getString("NAME"));
        		sqr.setLXDH(sqrList.getJSONObject(i).getString("PHONE"));
        		sqr.setLOGINNAME(sqrList.getJSONObject(i).getString("USERNAME"));
        		sqr.setZJH(sqrList.getJSONObject(i).getString("ZJHM"));
        		sqr.setZJLX("1");
        		sqr.setPROJECT_ID(project_id);
        		sqr.setSHZT("0");
                baseCommonDao.save(sqr);
                baseCommonDao.flush();
        	}
        	
        }

    }
    
    public void createSqr(JSONArray sqrList, String xmbh) {
        for (int i = 0; i < sqrList.size(); i++) {
            BDCS_SQR sqr = new BDCS_SQR();
            sqr.setCREATETIME(new Date());
            sqr.setXMBH(xmbh);
            // 申请人信息
            sqr.setSQRXM(sqrList.getJSONObject(i).getString("SQRMC"));
            sqr.setZJLX(sqrList.getJSONObject(i).getString("SQRZJZL"));
            sqr.setZJH(sqrList.getJSONObject(i).getString("SQRZJH"));
            sqr.setLXDH(sqrList.getJSONObject(i).getString("SQRLXDH"));
            sqr.setTXDZ(sqrList.getJSONObject(i).getString("TXDZ"));
            sqr.setGYFS(sqrList.getJSONObject(i).getString("GYFS"));
            sqr.setQLBL(sqrList.getJSONObject(i).getString("QLBL"));
            sqr.setSXH(sqrList.getJSONObject(i).getString("SXH"));
            // 抵押平台的抵押权人申请人类别为10
            if ("10".equals(sqrList.getJSONObject(i).getString("SQRLB"))) {
                sqr.setSQRLB("1");
            } else {
                sqr.setSQRLB(sqrList.getJSONObject(i).getString("SQRLB"));
            }

            sqr.setSQRLX(sqrList.getJSONObject(i).getString("SQRLX"));
            // 法人信息
            sqr.setFDDBR(sqrList.getJSONObject(i).getString("FRMC"));
            sqr.setFDDBRDH(sqrList.getJSONObject(i).getString("FRLXDH"));
            sqr.setFDDBRZJHM(sqrList.getJSONObject(i).getString("FRZJH"));
            sqr.setFDDBRZJLX(sqrList.getJSONObject(i).getString("FRZJZL"));
            //代理人信息
            sqr.setDLRXM(sqrList.getJSONObject(i).getString("DLRMC"));
            sqr.setDLRZJLX(sqrList.getJSONObject(i).getString("DLRZJZL"));
            sqr.setDLRZJHM(sqrList.getJSONObject(i).getString("DLRZJH"));
            sqr.setDLRLXDH(sqrList.getJSONObject(i).getString("DLRLXDH"));
            baseCommonDao.save(sqr);
            baseCommonDao.flush();
        }

    }
    /*****************************************3.创建项目 end*****************************************/

    /***************************************** 保存申报业务日志 start*****************************************/

    private LOG_DECLARE_RECORD_LOG updateRecordLog(LOG_DECLARE_RECORD_LOG recordLog, String sbzt, String msg, String errorcode) {
        recordLog.setSBZT(sbzt);
        if (!StringHelper.isEmpty(msg)) {
            recordLog.setREMARK(msg);
        }
        recordLog.setERRORCODE(errorcode);
//        baseCommonDao.getCurrentSession().clear();
        baseCommonDao.saveOrUpdate(recordLog);
        baseCommonDao.flush();
        return recordLog;
    }

    public LOG_DECLARE_RECORD_LOG saveRecordLog(LOG_DECLARE_RECORD_LOG recordLog) {
        baseCommonDao.saveOrUpdate(recordLog);
        baseCommonDao.flush();
        return recordLog;
    }

    /**
     * 申报失败，记录报错日志
     */
    public LOG_DECLARE_RECORD_LOG saveErrorLog(String qxdm, String ywlsh, Exception e) {
        LOG_DECLARE_RECORD_LOG recordLog = new LOG_DECLARE_RECORD_LOG();
        List<LOG_DECLARE_RECORD_LOG> record_logs = baseCommonDao.getDataList(LOG_DECLARE_RECORD_LOG.class, "SBLSH='" + ywlsh + "'");
        StackTraceElement traceElement = e.getStackTrace()[0];
        String errMsg = null;
        if (!StringHelper.isEmpty(e.getCause())) {
            errMsg = e.getCause().getMessage();
        } else {
            errMsg = e.getMessage();
        }
        if (record_logs != null && record_logs.size() > 0) {
            recordLog = record_logs.get(0);
            recordLog.setSBCS(recordLog.getSBCS() + 1);
            recordLog.setSBZT("0");
            recordLog.setMODIFYTIME(new Date());
            recordLog.setERRORCODE("3001");
            recordLog.setREMARK("出现异常，文件名：" + traceElement.getFileName() + "，异常行数：" + traceElement.getLineNumber() + "，错误消息:" + errMsg);

        } else {
            recordLog.setBSM(Common.CreatUUID());
            recordLog.setSBLSH(ywlsh);
            recordLog.setTENANT_ID(qxdm);
            recordLog.setYWLY("3");
            recordLog.setSBCS(1);
            recordLog.setSBZT("0");
            recordLog.setSFDB("0");
            recordLog.setYWLSH(ywlsh);
            recordLog.setCREATEDATE(new Date());
            recordLog.setMODIFYTIME(new Date());
            recordLog.setERRORCODE("3001");
            recordLog.setREMARK("出现异常，文件名：" + traceElement.getFileName() + "，异常行数：" + traceElement.getLineNumber() + "，错误消息:" + errMsg);

        }

//        baseCommonDao.getCurrentSession().clear();
        baseCommonDao.saveOrUpdate(recordLog);
        baseCommonDao.flush();
        return recordLog;
    }
    /***************************************** 保存申报业务日志 end*****************************************/

	@Override
	public ResultData enterpriseDeclare(HttpServletRequest req) throws UnsupportedEncodingException{
		ResultData result = new ResultData();
        LOG_DECLARE_RECORD_LOG recordLog = new LOG_DECLARE_RECORD_LOG();
        //获取参数
        String requestcode = "", requestseq = "", ywlsh = "", qxdm = "", ywlcid="";
        
        JSONObject httpBodyParams = null;
        try {
            httpBodyParams = RequestHelper.getHTTPBodyParams(req);
            if (httpBodyParams.isEmpty()) {
                throw new Exception ("未获取到请求申报数据");
            }
            requestcode = httpBodyParams.getString("requestcode");
            requestseq = httpBodyParams.getString("requestseq");
            //20200001  业务申报 暂时不用
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("获取申报数据出现异常，详情：" + e.getMessage());
            result.setState(false);
            result.setErrorcode("3001");
        }
        JSONObject jsonObject = null;
        try {
            // ----------------------- 1.解析数据 -----------------------
            JSONObject data = httpBodyParams.getJSONObject("data");
            if (data.isEmpty()) {
                throw new Exception ("申报数据 data 节点不能为空");
            }
            
            jsonObject = AnalysisData_enterprise(data,requestcode);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("解析数据出现异常，详情：" + e.getMessage());
            result.setState(false);
            result.setErrorcode("3001");
        }
        logger.info(String.format("抵押业务申报开始，申报编码【%s】，申报标识【%s】", requestcode, requestseq));
        try {

            assert jsonObject != null;
            JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");

            // ----------------------- 2.各种数据核验操作 -----------------------
            ywlsh = basicInfo.getString("ENTERPRISEID");
            qxdm = basicInfo.getString("QXDM");
            ywlcid = basicInfo.getString("YWLCID");
            if (StringHelper.isEmpty(requestcode)) {
                throw new BeresolvException("获取申报业务类型异常", "3001");
            }
            if (StringHelper.isEmpty(ywlsh)) {
                throw new BeresolvException("获取申报流水号异常", "3001");
            }
            if (StringHelper.isEmpty(qxdm)) {
                throw new BeresolvException("获取申报区县代码异常", "3001");
            }
//            if (StringHelper.isEmpty(ywlcid)) {
//                throw new BeresolvException("获取申报业务流程ID异常", "3001");
//            }
            // ----------------------- 2.1创建日志记录 -----------------------
            List<LOG_DECLARE_RECORD_LOG> record_logs = baseCommonDao.getDataList(LOG_DECLARE_RECORD_LOG.class, "SBLSH='" + ywlsh + "'");
            if (record_logs != null && record_logs.size() > 0) {
                recordLog = record_logs.get(0);
                recordLog.setSBCS(recordLog.getSBCS() + 1);
                recordLog.setSBZT("1");
                recordLog.setMODIFYTIME(new Date());
                recordLog.setREMARK("");
            } else {
                recordLog.setBSM(Common.CreatUUID());
                recordLog.setSBLSH(ywlsh);
                recordLog.setYWLY("3");
                recordLog.setSBCS(1);
                recordLog.setSBZT("1");
                recordLog.setSFDB("0");
                recordLog.setYWLSH(ywlsh);
                recordLog.setCREATEDATE(new Date());
                recordLog.setMODIFYTIME(new Date());
                recordLog.setYWLCID(ywlcid);
                recordLog.setTENANT_ID(qxdm);
            }
            //----------------------- 2.2 检查业务是否已经创建 -----------------------
            //createdExamine(ywlsh, recordLog);
            //----------------------- 2.3 是否与不动产库数据匹配 -----------------------
            //checkAccectConstraint(jsonObject, recordLog);

            // ----------------------- 3.创建项目操作 -----------------------
            String prolsh = accectProject_enterprise(jsonObject, recordLog, requestcode, req);
            updataLsh(ywlsh, prolsh);
            result.setMsg("业务申报成功！");
            result.setState(true);
            result.setErrorcode("0000");
        } catch (ManualException e) {
            // 手动抛出的异常，利用校验不通过或为可判断的，需要用户解决的提示错误
            result.setMsg(e.getRecordLog().getREMARK());
            result.setState(false);
            result.setErrorcode(e.getRecordLog().getERRORCODE());
            saveRecordLog(e.getRecordLog());
        } catch (BeresolvException e) {
            // 需要解决的异常，标识知道异常原因但需要人工解决的异常,比如登簿失败，把登簿失败原因记录到日志中，方便工作人员排查
            result.setMsg(e.getRecordLog().getREMARK());
            result.setState(false);
            result.setErrorcode(e.getRecordLog().getERRORCODE());
            saveRecordLog(e.getRecordLog());
        }  catch (Exception e) {
            // 未知错误，这种错误往往是代码错误
            e.printStackTrace();
            result.setMsg(e.getMessage());
            result.setState(false);
            result.setErrorcode("3001");
            saveErrorLog(qxdm, ywlsh, e);
        }
        logger.info(String.format("抵押业务申报结束，申报编码【%s】，申报标识【%s】", requestcode, requestseq));
        return result;
	}

}
