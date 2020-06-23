package com.supermap.realestate.registration.service.impl;

import com.supermap.realestate.registration.ViewClass.SmsPushInfo;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.LOG_SMS_PUSHINFO;
import com.supermap.realestate.registration.model.LOG_SMS_TEMPLATE;
import com.supermap.realestate.registration.service.SmsManagerService;
import com.supermap.realestate.registration.util.*;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.synchroinline.util.HttpRequestHelper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;

@Service("smsManagerService")
public class SmsManagerServiceImpl implements SmsManagerService {

    @Autowired
    private CommonDao baseCommonDao;

    private static String appid;
    private static String appkey;
    private static String smsSign;
    private static String sendMessage;
    private static String sendContent;

    static {
        appid = GetProperties.getValueByFileName("sms_config.properties", "appid");
        appkey = GetProperties.getValueByFileName("sms_config.properties", "appkey");
        smsSign = GetProperties.getValueByFileName("sms_config.properties", "smsSign");
        sendMessage = GetProperties.getValueByFileName("sms_config.properties", "sendMessage");
        sendContent = GetProperties.getValueByFileName("sms_config.properties", "sendContent");
    }

    /**
     * 新增短信模板
     */
    @Override
    public ResultMessage AddTemplate(HttpServletRequest request) throws UnsupportedEncodingException {
        ResultMessage msg = new ResultMessage();

        String template_id = RequestHelper.getParam(request, "templateid");
        String template_name = RequestHelper.getParam(request, "templatename");
        String template_content = RequestHelper.getParam(request, "templatecontent");
        String bz = RequestHelper.getParam(request, "bz");

        try {
            if (StringHelper.isEmpty(template_id)) {
                msg.setSuccess("false");
                msg.setMsg("保存失败，模板ID不能为空！");
                return msg;
            }
            LOG_SMS_TEMPLATE log_sms_template = baseCommonDao.get(LOG_SMS_TEMPLATE.class, template_id);
            if (log_sms_template != null) {
                msg.setSuccess("false");
                msg.setMsg("保存失败，系统已存在相同模板ID的记录，无法新增！");
                return msg;
            }
            LOG_SMS_TEMPLATE template = new LOG_SMS_TEMPLATE();
            template.setTEMPLATEID(template_id.trim());
            template.setTEMPLATENAME(template_name.trim());
            template.setTEMPLATECONTENT(template_content);
            template.setCREATETIME(new Date());
            template.setBZ(bz);
            baseCommonDao.save(template);
            baseCommonDao.flush();
            msg.setSuccess("true");
            msg.setMsg("保存成功");
        } catch (Exception e) {
            msg.setSuccess("false");
            msg.setMsg("保存失败，请联系管理员！");
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 编辑短信模板
     */
    @Override
    public ResultMessage UpdateTemplate(HttpServletRequest request) throws UnsupportedEncodingException {
        String template_id = RequestHelper.getParam(request, "templateid");
        String template_name = RequestHelper.getParam(request, "templatename");
        String template_content = RequestHelper.getParam(request, "templatecontent");
        String bz = RequestHelper.getParam(request, "bz");

        ResultMessage msg = new ResultMessage();
        try {
            if (StringHelper.isEmpty(template_id)) {
                msg.setSuccess("false");
                msg.setMsg("更新失败，模板ID不能为空！");
                return msg;
            }
            LOG_SMS_TEMPLATE template = baseCommonDao.get(LOG_SMS_TEMPLATE.class, template_id);
            if (template != null) {
                template.setTEMPLATENAME(template_name);
                template.setTEMPLATECONTENT(template_content);
                template.setCREATETIME(new Date());
                template.setBZ(bz);
            } else {
                msg.setSuccess("false");
                msg.setMsg("更新失败，未获取到要更新模板！");
                return msg;
            }
            baseCommonDao.update(template);
            baseCommonDao.flush();
            msg.setSuccess("true");
            msg.setMsg("更新成功");
        } catch (Exception e) {
            msg.setSuccess("false");
            msg.setMsg("更新失败，请联系管理员！");
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 删除短信模板
     */
    @Override
    public ResultMessage RemoveTemplate(String templateid, HttpServletRequest request) {
        ResultMessage msg = new ResultMessage();
        msg.setSuccess("false");
        msg.setMsg("删除失败");
        LOG_SMS_TEMPLATE log_sms_template = baseCommonDao.get(LOG_SMS_TEMPLATE.class, templateid);
        if (log_sms_template != null) {
            baseCommonDao.deleteEntity(log_sms_template);
            baseCommonDao.flush();
            msg.setSuccess("true");
            msg.setMsg("删除成功");
        } else {
            msg.setSuccess("false");
            msg.setMsg("未获取到要删除模板！");
        }
        return msg;
    }

    /**
     * 分页获取短信模板
     */
    @Override
    public Message GetSmsTempletList(HttpServletRequest request) throws UnsupportedEncodingException {
        Integer page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        Integer rows = 10;
        if (request.getParameter("rows") != null) {
            rows = Integer.parseInt(request.getParameter("rows"));
        }
        String template_name = RequestHelper.getParam(request, "template_name");
        StringBuilder conditionBuilder = new StringBuilder("1 = 1");
        if (!StringHelper.isEmpty(template_name)) {
            conditionBuilder.append(" AND TEMPLATE_NAME LIKE '%").append(template_name)
                    .append("%'");
        }

        Page p = baseCommonDao.getPageDataByHql(LOG_SMS_TEMPLATE.class, conditionBuilder.toString(), page, rows);
        Message msg = new Message();
        msg.setSuccess("true");
        msg.setTotal(p.getTotalCount());
        msg.setRows(p.getResult());
        msg.setMsg("成功！");
        return msg;
    }

    /**
     * 获取短信推送记录列表
     * @param xmbh
     * @param request
     * @return
     */
    @Override
    public Message GetSmsPushList(String xmbh, HttpServletRequest request) {
        Integer page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        Integer rows = 10;
        if (request.getParameter("rows") != null) {
            rows = Integer.parseInt(request.getParameter("rows"));
        }

        List<SmsPushInfo> smsPushList = new ArrayList<SmsPushInfo>();

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT * FROM ( ");
        builder.append("SELECT SQR.SQRID AS SQRID, SQR.SQRXM AS SQRXM, SQR.LXDH AS LXDH, SQR.ZJH AS ZJH, SQR.SQRLB AS SQRLB, '0' AS ISDLR ");
        builder.append("FROM BDCK.BDCS_SQR SQR ");
        builder.append("WHERE SQR.XMBH = ''{0}'' ");
        builder.append("UNION ALL ");
        builder.append("SELECT DLR.SQRID AS SQRID, DLR.DLRXM AS SQRXM, DLR.DLRLXDH AS LXDH, DLR.DLRZJHM AS ZJH, DLR.SQRLB AS SQRLB, '1' AS ISDLR ");
        builder.append("FROM BDCK.BDCS_SQR DLR ");
        builder.append("WHERE DLR.XMBH = ''{0}'' ");
        builder.append("AND DLR.DLRXM IS NOT NULL ");
        builder.append(")");

        String fullsql = MessageFormat.format(builder.toString(), xmbh);

        BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
        List<Wfi_ProInst> wfiProInsts = baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='" + xmxx.getPROJECT_ID() + "'");
        String ywlsh="", wlsh="";
        if (wfiProInsts != null && wfiProInsts.size() > 0) {
            ywlsh = wfiProInsts.get(0).getProlsh();
            wlsh = wfiProInsts.get(0).getWLSH();
        }

        List<Map> maps = baseCommonDao.getDataListByFullSql(fullsql);
        for (Map map : maps) {
            SmsPushInfo smsPushInfo = new SmsPushInfo();
            smsPushInfo.setXMBH(xmbh);
            smsPushInfo.setSQRID(StringHelper.formatObject(map.get("SQRID")));
            smsPushInfo.setSQRXM(StringHelper.formatObject(map.get("SQRXM")));
            smsPushInfo.setZJH(StringHelper.formatObject(map.get("ZJH")));
            smsPushInfo.setLXDH(StringHelper.formatObject(map.get("LXDH")));
            smsPushInfo.setSQRLB(StringHelper.formatObject(map.get("SQRLB")));
            smsPushInfo.setSQRLBMC(ConstHelper.getNameByValue("SQRLB", StringHelper.formatObject(map.get("SQRLB"))));
            smsPushInfo.setISDLR(StringHelper.formatObject(map.get("ISDLR")));
            smsPushInfo.setYWLSH(ywlsh);
            smsPushInfo.setWLSH(wlsh);
            //关联短信推送表获取发送详情信息
            String hql = MessageFormat.format(" SQRID=''{0}'' AND ISDLR=''{1}'' ORDER BY CREATETIME DESC", StringHelper.formatObject(map.get("SQRID")), StringHelper.formatObject(map.get("ISDLR")));
            List<LOG_SMS_PUSHINFO> list = baseCommonDao.getDataList(LOG_SMS_PUSHINFO.class, hql);
            if (list != null && list.size() > 0) {
                smsPushInfo.setUSERID(StringHelper.formatObject(list.get(0).getUSERID()));
                smsPushInfo.setUSERNAME(StringHelper.formatObject(list.get(0).getUSERNAME()));
                smsPushInfo.setSEND_DATE(list.get(0).getSEND_DATE());
                smsPushInfo.setSEND_STATUS(StringHelper.formatObject(list.get(0).getSEND_STATUS()));
                smsPushInfo.setSEND_NUMBER(StringHelper.getInt(list.size()));
            }
            smsPushList.add(smsPushInfo);
        }

        Page resultPage = new Page(Page.getStartOfPage(page, rows),
                smsPushList.size(), rows, smsPushList);
        Message msg = new Message();
        msg.setTotal(smsPushList.size());
        msg.setRows(resultPage.getResult());
        return msg;
    }

    /**
     * 短信推送
     * @param xmbh
     * @param request
     * @return
     */
    @Override
    public Message SmsPush(String xmbh, HttpServletRequest request) throws UnsupportedEncodingException {
        Message msg = new Message();
        msg.setMsg("推送失败！");
        msg.setSuccess("false");

        BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
        if (xmxx == null) {
            msg.setMsg("推送失败！未获取到项目信息。");
            return  msg;
        }
        String template_id = RequestHelper.getParam(request, "template_id");
        String fullcontent = RequestHelper.getParam(request, "fullcontent");
        String params = RequestHelper.getParam(request, "param");
        String lxdh = RequestHelper.getParam(request, "lxdh");
        String sqrid = RequestHelper.getParam(request, "sqrid");
        String sqrxm = RequestHelper.getParam(request, "sqrxm");
        String isdlr = RequestHelper.getParam(request, "isdlr");
        String sqrlb = RequestHelper.getParam(request, "sqrlb");
        String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
        String tokenstr="";

        //必要参数由前端校验控制
        if (!StringHelper.isEmpty(params)) {
            String[] xzqdms = {"450300","450902","450921","450922","450923","450924","450981"};
            //桂林市及玉林市（包括玉林下面几个县）使用本地短信平台
            if(!Arrays.asList(xzqdms).contains(xzqhdm)) {
                try {
                    tokenstr = GetTokenUtil.getAccessToken();
                    if (StringHelper.isEmpty(tokenstr)) {
                        throw new Exception("推送失败！获取区厅短信平台token异常。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setMsg("推送失败！获取区厅短信平台token异常。");
                    return msg;
                }
            }
            User user = Global.getCurrentUserInfo();
            String userid = "";
            String username = "";
            if(user != null){
                userid = user.getId();
                username = user.getLoginName();
            }

            //日志详情实体
            LOG_SMS_PUSHINFO log_sms_pushinfo = new LOG_SMS_PUSHINFO();
            log_sms_pushinfo.setID((String) SuperHelper.GeneratePrimaryKey());
            log_sms_pushinfo.setSQRID(sqrid);
            log_sms_pushinfo.setXMBH(xmbh);
            log_sms_pushinfo.setTEMPLATE_ID(template_id);
            log_sms_pushinfo.setUSERID(userid);
            log_sms_pushinfo.setUSERNAME(username);
            log_sms_pushinfo.setRECEIVE_NAME(sqrxm);
            log_sms_pushinfo.setRECEIVE_PHONE(lxdh);
            //接收参数详情，多个参数以&符号分割
            log_sms_pushinfo.setRECEIVE_INFO(params);
            log_sms_pushinfo.setCREATETIME(new Date());
            log_sms_pushinfo.setSEND_DATE(new Date());
            log_sms_pushinfo.setSEND_NUMBER(0);
            log_sms_pushinfo.setSEND_TYPE("1");
            log_sms_pushinfo.setSEND_STATUS("0");
            log_sms_pushinfo.setISDLR(isdlr);
            log_sms_pushinfo.setSQRLB(sqrlb);
            log_sms_pushinfo.setYXBZ("0");
            log_sms_pushinfo.setSEND_CONTENT(fullcontent);

            try {
                //桂林市及玉林市（包括玉林下面几个县）使用本地短信平台
                if(Arrays.asList(xzqdms).contains(xzqhdm)) {
                    //组装短信参数（桂林、玉林等本地短信平台）
                    HashMap<String, String> postParams = new HashMap<String, String>();
                    postParams.put("appid", String.valueOf(appid));
                    postParams.put("appkey", appkey);
                    postParams.put("smsSign", smsSign);
                    postParams.put("mobile", lxdh);
                    postParams.put("info", params);
                    postParams.put("tencentcode", template_id);
                    String jsonString = HttpRequestHelper.smsSend(sendMessage, postParams);
                    System.out.println("短信接口返回数据：" + jsonString);
                    if (!StringHelper.isEmpty(jsonString)) {
                        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(jsonString);
                        /**
                         * result：腾讯云返回码，直接根据返回码获取枚举返回码说明
                         * 失败返回示例：{"result":1014,"errmsg":"package format error, sdkappid not have this tpl_id","ext":""}
                         * 成功返回示例：{"result": 0,"errmsg": "OK","ext": "","fee": 1,"sid": "xxxxxxx"}
                         **/
                        String result = StringHelper.formatObject(json.get("result"));
                        String errmsg = StringHelper.formatObject(json.get("errmsg"));
                        if (!StringHelper.isEmpty(result) && "0".equals(result)) {
                            msg.setSuccess("true");
                            msg.setMsg(" 短信推送成功！");
                            log_sms_pushinfo.setSEND_STATUS("2");
                            log_sms_pushinfo.setSEND_NUMBER(log_sms_pushinfo.getSEND_NUMBER()+1);
                            log_sms_pushinfo.setRESULT_CODE(result);
                            log_sms_pushinfo.setRESULT_MSG(ConstValue.SMSERRORTYPE.initFrom(result));
                            baseCommonDao.save(log_sms_pushinfo);
                            baseCommonDao.flush();
                        } else {
                            msg.setSuccess("false");
                            msg.setMsg("短信推送失败！详情：" + ConstValue.SMSERRORTYPE.initFrom(result));
                            log_sms_pushinfo.setRESULT_CODE(result);
                            log_sms_pushinfo.setRESULT_MSG(ConstValue.SMSERRORTYPE.initFrom(result));
                            throw new Exception(StringHelper.formatObject(json));
                        }
                    } else {
                        throw new Exception(jsonString);
                    }
                } else {
                    //组装短信参数（区厅内网短信平台）
                    String[] split = params.split("&");
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < split.length; i++) {
                        if (StringHelper.isEmpty(builder.toString())) {
                            builder.append("Content=[\"").append(URLEncoder.encode(split[i],"UTF-8")).append("\"");
                        } else {
                            builder.append(",\"").append(URLEncoder.encode(split[i],"UTF-8")).append("\"");
                        }
                    }
                    if (!StringHelper.isEmpty(builder.toString())) {
                        builder.append("]");
                    }
                    builder.append("&PlanOfSendTime=null");
                    builder.append("&ToStaffPhone=").append(lxdh);
                    builder.append("&Level=4");
                    builder.append("&SendImmediately=true");
                    builder.append("&TemplateCode=").append(template_id);

                    Map map = DXSENDUtil.smsSend(builder.toString(), tokenstr);

                    /**
                     * ResultType：区厅短信接口平台返回码，有bug，无论成功失败都是11
                     * 失败返回示例：{"ResultType":11,"Data":["编号1【13557777777】的短信错误。参数个数不匹配，无法套用模板！"],"Succeed":false,"Message":"共1条信息，成功入库0条，失败1条！"}
                     * 成功返回示例：{"ResultType":11,"Data":[],"Succeed":true,"Message":"共1条信息，成功入库1条，失败0条！"}
                     **/
                    String resultType = StringHelper.formatObject(map.get("ResultType"));
                    String data = StringHelper.formatObject(map.get("Data"));
                    String succeed = StringHelper.formatObject(map.get("Succeed"));
                    // 成功失败记录日志详情
                    if ("true".equals(succeed)) {
                        msg.setSuccess("true");
                        msg.setMsg("短信推送成功！");
                        log_sms_pushinfo.setSEND_STATUS("2");
                        log_sms_pushinfo.setSEND_NUMBER(log_sms_pushinfo.getSEND_NUMBER()+1);
                        log_sms_pushinfo.setRESULT_CODE(resultType);
                        log_sms_pushinfo.setRESULT_MSG("短信推送成功！");
                        baseCommonDao.save(log_sms_pushinfo);
                        baseCommonDao.flush();
                    } else {
                        msg.setSuccess("false");
                        msg.setMsg("短信推送失败！详情：" + data);
                        log_sms_pushinfo.setRESULT_CODE(resultType);
                        log_sms_pushinfo.setRESULT_MSG(data);
                        throw new Exception("短信推送失败！详情：" + data);
                    }
                }
            } catch (Exception e) {
                msg.setSuccess("false");
                msg.setMsg(e.getMessage());
                log_sms_pushinfo.setSEND_STATUS("3");
                log_sms_pushinfo.setSEND_NUMBER(log_sms_pushinfo.getSEND_NUMBER()+1);
                baseCommonDao.save(log_sms_pushinfo);
                baseCommonDao.flush();
                e.printStackTrace();
            }
        }
        return msg;
    }

    @Override
    public JSONObject sendSms(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            // 短信应用SDK AppID
            String appid = RequestHelper.getParam(request,"appid");
            // 短信应用SDK AppKey
            String appkey = RequestHelper.getParam(request,"appkey");
            // 签名
            String smsSign =RequestHelper.getParam(request, "smsSign");
            //手机号码
            String mobile = RequestHelper.getParam(request,"mobile");
            //申请人姓名
            String name = RequestHelper.getParam(request, "name");
            //参数2
            String message = RequestHelper.getParam(request, "message");
            //模板ID
            String tencentcode = RequestHelper.getParam(request,"tencentcode");

            HashMap<String, String> postParams = new HashMap<String, String>();
            postParams.put("appid", appid);
            postParams.put("appkey", appkey);
            postParams.put("smsSign", smsSign);
            postParams.put("mobile", mobile);
            postParams.put("name", name);
            postParams.put("message", message);
            postParams.put("tencentcode", tencentcode);
            StringBuilder sb = new StringBuilder();
            sb.append(name+","+message);
            postParams.put("info", sb.toString());
            String jsonString = HttpRequestHelper.smsSend(sendMessage, postParams);
            jsonObject.put("data", jsonString);
            jsonObject.put("code", 0000);
            jsonObject.put("msg", "短信推送成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("data", null);
            jsonObject.put("code", 0001);
            jsonObject.put("msg", "短信推送失败");
        }
        return jsonObject;
    }

    @Override
    public Message GetSmsPushList(HttpServletRequest request) throws UnsupportedEncodingException {
        Message msg = new Message();
        String ywlsh = RequestHelper.getParam(request, "YWLSH");
        String djlx = RequestHelper.getParam(request, "DJLX");
        String send_status = RequestHelper.getParam(request, "SEND_STATUS");
        String xmmc = RequestHelper.getParam(request, "XMMC");
        String djsj_q = RequestHelper.getParam(request, "DJSJ_Q");
        String djsj_z = RequestHelper.getParam(request, "DJSJ_Z");
        String tssj_q = RequestHelper.getParam(request, "TSSJ_Q");
        String tssj_z = RequestHelper.getParam(request, "TSSJ_Z");

        String _pageIndex = request.getParameter("page");
        String _pageSize = request.getParameter("rows");
        int pageIndex=1;
        int pageSize=10;
        if(!StringHelper.isEmpty(_pageIndex)){
            pageIndex=StringHelper.getInt(_pageIndex);
        }
        if(!StringHelper.isEmpty(_pageSize)){
            pageSize=StringHelper.getInt(_pageSize);
        }

        StringBuilder formsql = new StringBuilder();
        formsql.append(" FROM BDCK.BDCS_XMXX XMXX ");
        formsql.append("LEFT JOIN LOG.LOG_SMS_PUSHINFO SP ");
        formsql.append("ON XMXX.XMBH = SP.XMBH ");
        formsql.append("WHERE EXISTS (SELECT 1 FROM LOG.LOG_SMS_PUSHINFO T WHERE T.XMBH = XMXX.XMBH) ");
        if(!StringHelper.isEmpty(ywlsh)){
            formsql.append("AND XMXX.YWLSH like '%" + ywlsh + "%' ");
        }
        if(!StringHelper.isEmpty(xmmc)){
            formsql.append("AND XMXX.XMMC like '%" + xmmc + "%' ");
        }
        if(!StringHelper.isEmpty(djlx)){
            formsql.append("AND XMXX.DJLX = '" + djlx + "' ");
        }
        if(!StringHelper.isEmpty(send_status)){
            formsql.append("AND SP.SEND_STATUS = '" + send_status + "' ");
        }
        if(!StringHelper.isEmpty(djsj_q)){
            formsql.append("AND XMXX.DJSJ >= TO_DATE('" + djsj_q + " 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
        }
        if(!StringHelper.isEmpty(djsj_z)){
            formsql.append("AND XMXX.DJSJ <= TO_DATE('" + djsj_z + " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
        }
        if(!StringHelper.isEmpty(tssj_q)){
            formsql.append("AND SP.SEND_DATE >= TO_DATE('" + tssj_q + " 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
        }
        if(!StringHelper.isEmpty(tssj_z)){
            formsql.append("AND SP.SEND_DATE <= TO_DATE('" + tssj_z + " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
        }
        long total=baseCommonDao.getCountByFullSql(formsql.toString());
        msg.setTotal(total);
        if(total>0){
            StringBuilder selectSql=new StringBuilder();
            selectSql.append("SELECT ROW_NUMBER() OVER(ORDER BY XMXX.YWLSH,SP.SEND_DATE ASC) AS XH,XMXX.YWLSH, XMXX.XMMC, SP.TEMPLATE_ID, SP.USERNAME, SP.RECEIVE_PHONE, SP.RESULT_MSG, ");
            selectSql.append("TO_CHAR(XMXX.DJSJ,'YYYY-MM-DD HH24:MI:SS') AS DJSJ, TO_CHAR(SP.SEND_DATE,'YYYY-MM-DD HH24:MI:SS') AS SEND_DATE, ");
            selectSql.append("(CASE WHEN SP.SEND_STATUS='1' THEN '正在推送' WHEN SP.SEND_STATUS='2' THEN '推送成功' WHEN SP.SEND_STATUS='3' THEN '推送失败' ELSE '待推送' END) AS SEND_STATUS ");

            String fullsql = selectSql.append(formsql).toString();
            List<Map> list =baseCommonDao.getPageDataByFullSql(fullsql, pageIndex, pageSize);
            msg.setRows(list);
        }
        msg.setSuccess("success");
        return msg;
    }

    @Override
    public JSONObject GetSmsPushList_HZ(HttpServletRequest request) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        String ywlsh = RequestHelper.getParam(request, "YWLSH");
        String djlx = RequestHelper.getParam(request, "DJLX");
        String send_status = RequestHelper.getParam(request, "SEND_STATUS");
        String xmmc = RequestHelper.getParam(request, "XMMC");
        String djsj_q = RequestHelper.getParam(request, "DJSJ_Q");
        String djsj_z = RequestHelper.getParam(request, "DJSJ_Z");
        String tssj_q = RequestHelper.getParam(request, "TSSJ_Q");
        String tssj_z = RequestHelper.getParam(request, "TSSJ_Z");

        StringBuilder formsql = new StringBuilder();
        formsql.append(" FROM BDCK.BDCS_XMXX XMXX ");
        formsql.append("LEFT JOIN LOG.LOG_SMS_PUSHINFO SP ");
        formsql.append("ON XMXX.XMBH = SP.XMBH ");
        formsql.append("WHERE EXISTS (SELECT 1 FROM LOG.LOG_SMS_PUSHINFO T WHERE T.XMBH = XMXX.XMBH) ");
        if(!StringHelper.isEmpty(ywlsh)){
            formsql.append("AND XMXX.YWLSH like '%" + ywlsh + "%' ");
        }
        if(!StringHelper.isEmpty(xmmc)){
            formsql.append("AND XMXX.XMMC like '%" + xmmc + "%' ");
        }
        if(!StringHelper.isEmpty(djlx)){
            formsql.append("AND XMXX.DJLX = '" + djlx + "' ");
        }
        if(!StringHelper.isEmpty(send_status)){
            formsql.append("AND SP.SEND_STATUS = '" + send_status + "' ");
        }
        if(!StringHelper.isEmpty(djsj_q)){
            formsql.append("AND XMXX.DJSJ >= TO_DATE('" + djsj_q + " 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
        }
        if(!StringHelper.isEmpty(djsj_z)){
            formsql.append("AND XMXX.DJSJ <= TO_DATE('" + djsj_z + " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
        }
        if(!StringHelper.isEmpty(tssj_q)){
            formsql.append("AND SP.SEND_DATE >= TO_DATE('" + tssj_q + " 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
        }
        if(!StringHelper.isEmpty(tssj_z)){
            formsql.append("AND SP.SEND_DATE <= TO_DATE('" + tssj_z + " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
        }
        StringBuilder selectSql=new StringBuilder();
        selectSql.append("SELECT SP.SEND_STATUS  ");

        String fullsql = selectSql.append(formsql).toString();
        List<Map> list = baseCommonDao.getDataListByFullSql(fullsql);
        //推送成功与失败次数
        int ERRORCOUNT = 0;
        int SUCCESSCOUNT = 0;
        for (Map xmxx : list) {
            if ("2".equals(xmxx.get("SEND_STATUS"))) {
                SUCCESSCOUNT++;
            } else if ("3".equals(xmxx.get("SEND_STATUS"))) {
                ERRORCOUNT++;
            }
        }
        jsonObject.put("COUNT", StringHelper.formatObject(list.size()));
        jsonObject.put("ERRORCOUNT", StringHelper.formatObject(ERRORCOUNT));
        jsonObject.put("SUCCESSCOUNT", StringHelper.formatObject(SUCCESSCOUNT));
        return jsonObject;
    }
}
