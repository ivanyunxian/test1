package com.supermap.internetbusiness.service.impl;

import com.supermap.internetbusiness.model.AUTOPROJECTCONFIG;
import com.supermap.internetbusiness.model.XS24HZDBJ;
import com.supermap.internetbusiness.service.IAutoAccectProjectService;
import com.supermap.internetbusiness.service.IAutoQueryService;
import com.supermap.internetbusiness.service.IntelligentService;
import com.supermap.internetbusiness.util.AfterBoardException;
import com.supermap.internetbusiness.util.BeresolvException;
import com.supermap.internetbusiness.util.ManualException;
import com.supermap.internetbusiness.util.ResultData;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.resources.util.Message;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.synchroinline.model.*;
import com.supermap.wisdombusiness.synchroinline.service.AcceptProjectService;
import com.supermap.wisdombusiness.synchroinline.util.CommonsHttpInvoke;
import com.supermap.wisdombusiness.synchroinline.util.InlineFileItem;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.*;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.*;

@Service
public class AutoAccectProjectServiceimpl implements IAutoAccectProjectService {

    @Autowired
    CommonDaoInline baseCommonDaoInline;
    @Autowired
    private CommonDao baseCommonDao;
    @Autowired
    SmProDef smProdef;
    @Autowired
    SmProDef smProDef;
    @Autowired
    AcceptProjectService acceptProjectService;
    @Autowired
    SmProSPService smProSPService;
    @Autowired
    private AutoOperationService autoOperationService;
    @Autowired
    private DBService dbService;
    @Autowired
    private SmProInstService smProInstService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SmProMater smProMater;
    @Autowired
    private DJBService djbService;
    @Autowired
    private SmProInst smProInst;
    @Autowired
    private SmStaff smStaff;
    @Autowired
    private SmProInstService ProInstService;
    @Autowired
    private IAutoQueryService autoQueryService;
    @Autowired
    private AutoSmActInst autoSmActInst;
    @Autowired
    IntelligentService intelligentService;
    @Autowired
    proinstStateModify proinstStateModify;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
  
    @Override
    public ResultData autoCreateProject(String wlsh, HttpServletRequest request) {
        ResultData result = new ResultData();
//        String file_number = createFile_number(wlsh);
        XS24HZDBJ bjlog = new XS24HZDBJ();
        Pro_proinst proinst = new Pro_proinst();
        try {
            if (StringHelper.isEmpty(wlsh)) {
                throw new BeresolvException("获取外网流水号异常", "3001");
            }
            List<Pro_proinst> proinsts = baseCommonDaoInline.getDataList(Pro_proinst.class, " PROLSH='" + wlsh + "' ");
            if (proinsts != null && proinsts.size() > 0) {
                proinst = proinsts.get(0);
            } else {
                throw new BeresolvException("未获取到外网项目信息", "3001");
            }
            //写日志
            List<XS24HZDBJ> uselogs = baseCommonDao.getDataList(XS24HZDBJ.class, " ZWLSH ='" + wlsh + "' ");
            if (uselogs.size() > 0) {
                XS24HZDBJ uselog = uselogs.get(0);
                uselog.setDQCS(uselogs.get(0).getDQCS() + 1);
                uselog.setDQSJ(new Date());
                uselog.setDQZT("1");
                uselog.setDQSBYY("");
                bjlog = uselog;
            } else {
                XS24HZDBJ onlinelog = new XS24HZDBJ();
                onlinelog.setId(Common.CreatUUID());
                onlinelog.setZWLSH(wlsh);
                onlinelog.setDQSJ(new Date());
                onlinelog.setSCDQSJ(new Date());
                onlinelog.setDQCS(1);
                onlinelog.setDQZT("1");
                onlinelog.setDJCODE("");
                onlinelog.setSFDB("0");
                onlinelog.setSFXXBL("0"); //是否已转线下办理
                bjlog = onlinelog;
            }
            
            //判断附件是否存在
            boolean flag = fjIsExists(proinsts,bjlog);
            if(!flag) {
                result.setMsg("未获取到附件资料，请检查附件资料是否已同步。");
                result.setErrorcode("1006");
                bjlog.setErrorCode("1006");
                bjlog.setDQSBYY("等待图片同步后调用");
                saveXS24HZDBJ(bjlog);
                return result;//false的话，不做任何处理
            }
            //调用自动办理接口成功的同时，要把proinst表的sfjsbl状态给改变，不然轮训机制会继续调用
            proinsts.get(0).setSfjsbl("2");
            baseCommonDaoInline.saveOrUpdate(proinsts.get(0));
        	 //-----------------------1.检查该流水号的项目是否已经创建-----------------
            createdExamine(wlsh, bjlog);
            //-----------------------2.内网外数据匹配---------------------------------
            checkAccectConstraint(wlsh, request, bjlog);
            //-----------------------3.创建项目一直到自动登簿---------------------------------------
            accectInlineProject(wlsh, bjlog, request);
        } catch (ManualException e) {
            result.setMsg(e.getXs24HZDBJ().getDQSBYY());
            result.setState(false);
            //手动抛出的异常，利用校验不通过或为可判断的，需要用户解决的提示错误
            saveXS24HZDBJ(e.getXs24HZDBJ());
            //填驳回意见
            saveSlxmsh(wlsh, e.getXs24HZDBJ().getDQSBYY());
            //调用自动办理接口成功的同时，要把proinst表的sfjsbl状态给改变，不然轮训机制会继续调用
            proinst.setSfjsbl("2");
            // 设置项目受理完成审核状态
            proinst.setShzt(11);
            // 内网数据变化，设置状态告诉外网
            proinst.setTbzt(2);
            baseCommonDaoInline.saveOrUpdate(proinst);
            baseCommonDaoInline.flush();
        } catch (BeresolvException e) {
            //需要解决的异常，标识知道异常原因但需要人工解决的异常,比如登簿失败，把登簿失败原因记录到日志中，方便工作人员排查
            //通知工作人员
            result.setMsg(e.getXs24HZDBJ().getDQSBYY());
            result.setState(false);
            saveXS24HZDBJ(e.getXs24HZDBJ());
            //调用自动办理接口成功的同时，要把proinst表的sfjsbl状态给改变，不然轮训机制会继续调用
            proinst.setSfjsbl("2");
            baseCommonDaoInline.saveOrUpdate(proinst);
            baseCommonDaoInline.flush();
        } catch (Exception e) {
            //未知错误，这种错误往往是代码错误
            e.printStackTrace();
            result.setMsg(e.getMessage());
            result.setState(false);
            errorLogzt(wlsh, e);
            //调用自动办理接口成功的同时，要把proinst表的sfjsbl状态给改变，不然轮训机制会继续调用
            proinst.setSfjsbl("2");
            baseCommonDaoInline.saveOrUpdate(proinst);
            baseCommonDaoInline.flush();
        }
        return result;
    }


    /**
     * 判断附件是否存在
     * @param proinsts
     * @param bjlog
     * @return
     */
    private boolean fjIsExists(List<Pro_proinst> proinsts, XS24HZDBJ bjlog) {
        // 获取附件资料信息
        @SuppressWarnings("unchecked")
        List<Pro_attachment> attachments = baseCommonDaoInline.getDataList(Pro_attachment.class, "PROINST_ID='" + proinsts.get(0).getId() + "' order by created desc ");
        String syspath = ConfigHelper.getNameByValue("filepath");
        for(Pro_attachment attachment : attachments){
        	 String fileNamepath = syspath + File.separator + attachment.getPath() + File.separator + attachment.getName() + attachment.getSuffix();
        	 File file =new File(fileNamepath);
        	 if(!file.exists()){
        		 return false;
        	 }
        }
        
		return true;
	}


	/**
     * 创建项目一直到登簿
     *
     * @param wlsh
     * @param bjlog
     */
    public void accectInlineProject(String wlsh, XS24HZDBJ bjlog, HttpServletRequest request) throws Exception {
        List<Pro_proinst> proinsts = baseCommonDaoInline.getDataList(Pro_proinst.class, " PROLSH='" + wlsh + "' ");
        String xmbh = "";
        String ywlsh = "";
        String actinstid = "";
        String djproinst_id = "";
        try {
            String prodef_code = proinsts.get(0).getProdefcode();
            List<AUTOPROJECTCONFIG> apcs = baseCommonDao.getDataList(AUTOPROJECTCONFIG.class, " PRODEF_ID = '" + prodef_code + "' and sfqy = '1'");
            if (apcs == null || apcs.size() == 0) {
                throw new BeresolvException(bjlog, "2", "读取自动办理流程配置失败", "2001");
            }
            String djyy = apcs.get(0).getDJYY(); //
            String fj = apcs.get(0).getFJ(); //获取配置中的附记
            String shyj = apcs.get(0).getSPYJ();    //获取配置中的审核意见
            String blry = apcs.get(0).getSTAFFNAME(); //获取配置中的办理人员
            String blryid = apcs.get(0).getSTAFFID(); //办理人员id
            String dbr = apcs.get(0).getDBR();//获取配置中的登簿人
            String dbrid = apcs.get(0).getDBRID();//获取配置中的登簿人id
            String isYDB = apcs.get(0).getISYDB();//是否属于预登簿模式
            String isHangUp = apcs.get(0).getISHANGUP();//是否挂起
            String szry = apcs.get(0).getSZRY();//获取配置中的缮证人
            String szryid = apcs.get(0).getSZRYID();//获取配置中的缮证人id
            String routeid = apcs.get(0).getROUTEID(); //获取缮证人的角色id
            String zxdbr = StringHelper.formatObject(apcs.get(0).getZXDBR());//注销登簿人
            setUseLogDQZT(bjlog, "1", "", "0008");
            List<Wfd_Prodef> prodeflist = baseCommonDao.getDataList(Wfd_Prodef.class, "prodef_id='" + prodef_code + "'");
            if (prodeflist.size() > 0) {
                String prodefid = prodeflist.get(0).getProdef_Id();
                if (prodefid != null && !prodefid.equals("")) {

                    //-------------------------------1.创建项目信息实体-------------------------
                    SmProInfo info = new SmProInfo();
                    info.setProDef_ID(prodefid);
                    info.setBatch(UUID.randomUUID().toString().replace("-", ""));
                    String prodefclassid = prodeflist.get(0).getProdefclass_Id();
                    Wfd_Prodef prodef = smProdef.GetProdefById(prodefid);
                    String proDefName = getproDefName(prodefid, prodefclassid);
                    info.setProDef_Name(proDefName);
                    info.setMessage("由不动产登记系统自动创建");
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

                    //--------------------------2.保存外网表相关流程数据------------
                    acceptProjectService.saveProdefInfo(proinsts.get(0).getId(), info.getProDef_ID());
                    //--------------------------3.创建项目------------------------------

                    // 从前置机获取受理项目
                    Pro_proinst proinst = proinsts.get(0);
//                    if (proinst.getLsh() != null && !proinst.getLsh().isEmpty()) {
//                        throw new BeresolvException(bjlog, "2", "自动办理失败-不能重复创建项目", "1001");
//                    }
                    // 获取权利人信息
                    @SuppressWarnings("unchecked")
                    List<Pro_proposerinfo> sqrs = baseCommonDaoInline.getDataList(Pro_proposerinfo.class, "PROINST_ID='" + proinsts.get(0).getId() + "'");
                    // 获取资料模板信息
                    @SuppressWarnings("unchecked")
                    List<Pro_datuminst> datuminsts = baseCommonDaoInline.getDataList(Pro_datuminst.class, "PROINST_ID='" + proinsts.get(0).getId() + "'");
                    // 获取附件资料信息
                    @SuppressWarnings("unchecked")
                    List<Pro_attachment> attachments = baseCommonDaoInline.getDataList(Pro_attachment.class, "PROINST_ID='" + proinsts.get(0).getId() + "' order by created desc ");

                    String sqrmcList = "";
                    for (Pro_proposerinfo sqr : sqrs) {
                        String sqrmc = StringHelper.isEmpty(sqr.getSqr_name()) ? sqr.getSqr_qt_dlr_name() : sqr.getSqr_name();
                        if (!StringHelper.isEmpty(sqrmc)) {
                            if (StringHelper.isEmpty(sqrmcList)) {
                                sqrmcList = sqrmc;
                            } else {
                                sqrmcList += "、" + sqrmc;
                            }
                        }
                    }
                    info.setProjectName(sqrmcList);
                    info.setSQStartTime(StringHelper.formatObject(proinst.getTjsj()));
                    // 创建受理项目
                    SmObjInfo returnSmObj = smProInstService.Accept(info, staffList);
                    djproinst_id = returnSmObj.getID();
                    SmObjInfo smInfo = returnSmObj.getChildren().get(0);
                    Wfi_ProInst inst = baseCommonDao.get(Wfi_ProInst.class, djproinst_id);
//                    if (StringHelper.isEmpty(inst.getFile_Number())) {
//                        inst.setFile_Number(file_number);
//                        baseCommonDao.saveOrUpdate(inst);
//                        baseCommonDao.flush();
//                    }
                    if (!StringHelper.isEmpty(proinst.getWf_prodefname())) {
                        //登记系统中受理信息，登记类型和流程类型，是取Wfi_ProInst得prodefname，登记类型和流程类型用，隔开
                        String wf_prodefname = proinst.getWf_prodefname();
                        wf_prodefname = wf_prodefname.replaceFirst("/", ",");
                        inst.setProdef_Name(wf_prodefname);
                    }
                    inst.setWLSH(proinst.getProlsh());
                    proinst.setProend(inst.getProinst_End());
                    inst.setProject_Name(sqrmcList);
                    inst.setSfjslb(proinst.getSfjsbl());
                    ProjectInfo projectInfo = projectService.getProjectInfo(inst.getFile_Number(), request);

                    // 更新项目信息，由于使用事务，当创建bdcs_xmxx记录时，需要从工作流库获取信息，但此时事务未提交导致获取不到信息。
                    //String xmmc = info.getProInst_Name() == null || info.getProInst_Name().trim().isEmpty() ? smInfo.getID() : info.getProInst_Name();

                    // 项目编号
                    returnSmObj.setDesc(projectInfo.getXmbh());
                    // 设置内网生成的流水号
                    proinst.setLsh(smInfo.getID());

                    // 导入申请人信息与权利需要按具体流程进行具体操作
                    List<WFD_MAPPING> mps = baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='" + prodef.getProdef_Code() + "'");
                    if (mps == null || mps.size() != 1) {
                        throw new BeresolvException(bjlog, "2", "自动办理失败-流程配置错误", "2001");
                    }
                    String baseWorkflowName = mps.get(0).getWORKFLOWNAME();
                    acceptProjectService.createSqrs(proinst, projectInfo.getXmbh(), sqrs, baseWorkflowName);

                    // 导入申请材料
                    List<Pro_datuminst> nomatchs = new ArrayList<Pro_datuminst>();// 没匹配到的存到这里
                    // 返回其他资料模板，如果没有则为空
                    Wfi_ProMater other = CreateWfi_ProMaters(inst, datuminsts, attachments, nomatchs, staffList);
                    if (!nomatchs.isEmpty()) {
                        if (other == null) {
                            // 创建其他这个模板
                            other = smProMater.CreatProMater("其它必要材料", djproinst_id);
                            baseCommonDao.save(other);
                            baseCommonDao.flush();
                        }
                        for (Pro_datuminst mulu : nomatchs) {
                            List<Pro_attachment> files = acceptProjectService.getFiles(attachments, mulu);
                            inputFileForGX(inst, other, files, staffList);
                        }
                    }


                    Map projectDatas = getYwLsh(djproinst_id);
                    actinstid = returnSmObj.getName();
                    ywlsh = (String) projectDatas.get("ywlsh");
                    djproinst_id = (String) projectDatas.get("proinstid");
                    //创建成功后添加业务号到日志
                    bjlog.setDJLSH(ywlsh);
                    setUseLogDQZT(bjlog, "1", "创建项目成功", "0001");
                    YwLogUtil.addYwLog("自动创建项目-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);

                    //--------------------------4.选择单元------------------------------
                    JsonMessage msg = acceptProjectService.selectDY(returnSmObj.getDesc(), proinsts.get(0).getId(), request);
                    if (!msg.getState()) {
                        throw new ManualException(bjlog, "0", "受理单元异常：" + msg.getMsg(), "2004");
                    }
                    setUseLogDQZT(bjlog, "1", "受理选择单元成功", "0002");
                    //--------------------------5.插入权利------------------------------
                    acceptProjectService.inserQLByProinst(proinsts.get(0).getId(), returnSmObj, request);
                    setUseLogDQZT(bjlog, "1", "插入权利成功", "0003");

                    List<Wfi_ProInst> pro = baseCommonDao.getDataList(Wfi_ProInst.class, " proinst_id = '" + djproinst_id + "'");
                    List<Wfi_ActInst> yact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "'  order by actinst_start  desc ");
                    List<Wfd_Route> routes = baseCommonDao.getDataList(Wfd_Route.class, " prodef_id = '" + pro.get(0).getProdef_Id() + "' and  actdef_id = '" + yact.get(0).getActdef_Id() + "'");
                    List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, " project_id = '" + pro.get(0).getFile_Number() + "'");
                    xmbh = xmxxs.get(0).getId();
                    if (routes.size() > 0) {
                        routeid = routes.get(0).getRoute_Id();
                    }

                    BDCS_XMXX xmxx = xmxxs.get(0);
                    if (xmxx != null) {
                        xmxx.setXMMC(sqrmcList);
                        xmxx.setWLSH(proinst.getProlsh());
                        xmxx.setSLRY(staffList.get(0).getName());
                        xmxx.setSLRYID(staffList.get(0).getID());
                        xmxx.setYWLSH(smInfo.getID());
                        baseCommonDao.getCurrentSession().clear();
                        baseCommonDao.saveOrUpdate(xmxx);
                        baseCommonDao.flush();
                    }

                    //--------------------------6.生成产权证号------------------------------
                    List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='" + xmbh + "' ");

                    if(djdys.isEmpty()) {
                        throw new BeresolvException(bjlog, "2", "项目信息关联的djdy查无数据", "2003");
                    }

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

                    setUseLogDQZT(bjlog, "1", "生成权证号成功", "0004");


                    //==========================添加审批意见=========================================
                    //（1）初审意见
                    smProSPService.SaveApproval("", "1", "CS", actinstid, shyj, "", "", "", null, "");
                    //（2）复审意见
                    smProSPService.SaveApproval("", "2", "FS", actinstid, shyj, "", "", "", null, "");
                    //（1）核定意见
                    smProSPService.SaveApproval("", "3", "HD", actinstid, shyj, "", "", "", null, "");

                    //==========================7.登簿转出区(转出到登簿环节)=========================================
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
                        throw new BeresolvException(bjlog, "2", "自动办理成功-但转出登簿失败", "2006");
                    } else {
                        YwLogUtil.addYwLog("自动办理转出登簿成功" + ywlsh, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
                    }

                    setUseLogDQZT(bjlog, "1", "自动办理转出登簿成功", "0005");

                    //生成智能审批报告
                    final String file_number = inst.getFile_Number();
                    /*try {
                        intelligentService.createReport(file_number);
                    } catch (Exception e) {
                        System.out.println("生成智能审批报告失败");
                        e.printStackTrace();
                    }*/
                    //调用网签接口推送相关数据
                    try {
                        final   String a=actinstid;
                        final    String b=request.getRequestURL().toString();
                        taskExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    proinstStateModify.sendInfoToHouse(a ,b);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //生成智能审批报告、记录税务网签时间
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

//                            //需要网签税务确认的流程添加时间记录
//                            String wcodes= ConfigHelper.getNameByValue("TAXSET-WORKFLOWCODE");
//                            if(wcodes!=null && baseWorkflowName!=null && wcodes.contains(baseWorkflowName)) {
//                                Wfi_ProinstDate proinstDate = new Wfi_ProinstDate();
//                                proinstDate.setId(StringUtil.getUUID());
//                                proinstDate.setAreaCode(proinst.getAreacode());
//                                proinstDate.setFile_number(file_number);
//                                proinstDate.setActinst_start_time(new Date());
//                                baseCommonDao.saveOrUpdate(proinstDate);
//                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("生成智能审批报告失败");
                        }
                    }


                    //预登簿，到此结束
                    if("1".equals(isYDB)) {
                        Pro_slxmsh slxmsh = new Pro_slxmsh();
                        slxmsh.setSh_id(UUID.randomUUID().toString().replace("-", ""));
                        slxmsh.setSh_xmsl_rq(new Date());
                        slxmsh.setSh_xmsl_ry(staffList.get(0).getName());
                        slxmsh.setSh_xmsl_yj(shyj);
                        slxmsh.setSlxm_id(proinsts.get(0).getId());
                        baseCommonDaoInline.saveOrUpdate(slxmsh);
                        // 设置项目受理完成审核状态
                        proinst.setShzt(20);
                        // 内网数据变化，设置状态告诉外网
                        proinst.setTbzt(2);
                        proinst.setLsh(ywlsh);
                        baseCommonDaoInline.saveOrUpdate(proinst);
                        try {
                            List<Wfi_ActInst> dbyact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "' and ACTINST_STATUS='1'  order by actinst_start  desc ");
                            //是否自动挂起
                            if("1".equals(isHangUp)){
                                autoSmActInst.setAutoHangUp(dbyact.get(0).getActinst_Id(), "系统自动挂起操作", blryid, blry);
                            }
                        } catch(Exception e) {
                            throw new AfterBoardException(bjlog, "2", "登簿后出现异常:" + e.getMessage(), "3002");
                        }

                        setUseLogDQZT(bjlog, "2", "已完成预登簿，等待审核登簿", "0000");
                        return ;
                    }
                    //==========================8.自动登簿区=========================================
                    ResultMessage rmdb = dbService.BoardBook(xmbh);
                    if ("false".equals(rmdb.getSuccess()) || rmdb == null || "warning".equals(rmdb.getSuccess())) {
                        YwLogUtil.addYwLog("登簿-失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
                        if (rmdb.getMsg().contains("null")) {
                            throw new BeresolvException(bjlog, "2", "登簿失败原因:登簿发生未知错误。null", "2007");
                        } else {
                            throw new BeresolvException(bjlog, "2", "登簿失败原因:" + rmdb.getMsg(), "2007");

                        }
                    }

                    //登簿后异常单独捕获，否则登簿完成报错，登簿不会回滚，但项目会被删除
                    try {

                        //登簿完成后更新是否登簿状态
                        if (!"1".equals(xmxx.getSFDB())) {
                            xmxx.setSFDB("1");
                            baseCommonDao.getCurrentSession().clear();
                            baseCommonDao.saveOrUpdate(xmxx);
                            baseCommonDao.flush();
                        }

                        //更新日志登簿状态
                        List<XS24HZDBJ> uselogdbs = baseCommonDao.getDataList(XS24HZDBJ.class, " ZWLSH ='" + wlsh + "' and (sfdb <> '1' or sfdb is null) ");
                        if (uselogdbs.size() > 0) {
                            XS24HZDBJ uselogdb = uselogdbs.get(0);
                            uselogdb.setSFDB("1");
                            baseCommonDao.getCurrentSession().clear();
                            baseCommonDao.saveOrUpdate(uselogdb);
                            baseCommonDao.flush();
                        }

                        //登簿后更新登簿人
                        if (djdys.size() > 0) {
                            for (BDCS_DJDY_GZ djdygz : djdys) {
                                List<BDCS_QL_GZ> xqlgzs = baseCommonDao.getDataList(BDCS_QL_GZ.class, " djdyid = '" + djdygz.getDJDYID() + "' and xmbh='" + xmbh + "'");
                                for (BDCS_QL_GZ qlgz : xqlgzs) {
                                    qlgz.setDBR(dbr);
                                    baseCommonDao.saveOrUpdate(qlgz);
                                    baseCommonDao.flush();
                                    if ((!"".equals(zxdbr)) && zxdbr != null && (!"null".equals(zxdbr))) {

                                        //登簿后更新注销登簿人 -- 更新当前权利的注销登簿人，用于前端显示
                                        List<BDCS_FSQL_GZ> fsqlgzs = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, " qlid = '" + qlgz.getId() + "' and zxsj is not null "
                                                + " and zxdyywh = '" + xmxxs.get(0).getPROJECT_ID() + "'");
                                        if (fsqlgzs.size() > 0) {
                                            BDCS_FSQL_GZ fsqlgz = fsqlgzs.get(0);
                                            fsqlgz.setZXDBR(zxdbr);
                                            baseCommonDao.saveOrUpdate(fsqlgz);
                                            baseCommonDao.flush();
                                        }
                                        //登簿后更新注销登簿人 -- 更新来源权利的注销登簿人
                                        List<BDCS_FSQL_LS> fsqllss = baseCommonDao.getDataList(BDCS_FSQL_LS.class, " qlid = '" + qlgz.getLYQLID() + "' and zxsj is not null "
                                                + " and zxdyywh = '" + xmxxs.get(0).getPROJECT_ID() + "'");
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

                        List<BDCS_QL_XZ> qlxzs = baseCommonDao.getDataList(BDCS_QL_XZ.class, " xmbh = '" + xmbh + "'");
                        if (qlxzs.size() > 0) {
                            for (BDCS_QL_XZ qlxz : qlxzs) {
                                qlxz.setDBR(dbr);
                                baseCommonDao.saveOrUpdate(qlxz);
                                baseCommonDao.flush();
                            }
                        }
                        List<BDCS_QL_LS> qllss = baseCommonDao.getDataList(BDCS_QL_LS.class, " xmbh = '" + xmbh + "'");
                        if (qllss.size() > 0) {
                            for (BDCS_QL_LS qlls : qllss) {
                                qlls.setDBR(dbr);
                                baseCommonDao.saveOrUpdate(qlls);
                                baseCommonDao.flush();
                            }
                        }
                        setUseLogDQZT(bjlog, "1", "自动办理自动登簿成功", "0006");
                        //==========================9.转出善证=========================================
                        List<Wfi_ProInst> szpro = baseCommonDao.getDataList(Wfi_ProInst.class, " proinst_id = '" + djproinst_id + "'");
                        List<Wfi_ActInst> szyact = baseCommonDao.getDataList(Wfi_ActInst.class, " proinst_id = '" + djproinst_id + "' and ACTINST_STATUS='1'  order by actinst_start  desc ");
                        List<Wfd_Route> szroutes = baseCommonDao.getDataList(Wfd_Route.class, " prodef_id = '" + pro.get(0).getProdef_Id() + "' and  actdef_id = '" + szyact.get(0).getActdef_Id() + "'");
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
                            throw new BeresolvException(bjlog, "2", "自动办理成功-但转出善证失败", "2006");
                        } else {

                            //================================10.成功，保存审批意见到外网==================================
                            Pro_slxmsh slxmsh = new Pro_slxmsh();
                            slxmsh.setSh_id(UUID.randomUUID().toString().replace("-", ""));
                            slxmsh.setSh_xmsl_rq(new Date());
                            slxmsh.setSh_xmsl_ry(staffList.get(0).getName());
                            slxmsh.setSh_xmsl_yj(shyj);
                            slxmsh.setSlxm_id(proinsts.get(0).getId());
                            slxmsh.setShzt(20);
                            baseCommonDaoInline.saveOrUpdate(slxmsh);
                            // 设置项目受理完成审核状态
                            proinst.setShzt(20);
                            // 内网数据变化，设置状态告诉外网
                            proinst.setTbzt(2);
                            proinst.setLsh(ywlsh);
                            baseCommonDaoInline.saveOrUpdate(proinst);
                            setUseLogDQZT(bjlog, "2", "自动办理成功！", "0000");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new AfterBoardException(bjlog, "2", "登簿后出现异常:" + rmdb.getMsg(), "3002");
                    }
                } else {
                    throw new BeresolvException(bjlog, "0", "项目流程code不存在！", "2002");
                }

            } else {
                throw new BeresolvException(bjlog, "0", "获取流程信息异常！", "2002");
            }
        } catch (AfterBoardException e) {
            throw e;
        } catch (Exception e) {
            //未知错误，这种错误往往是代码错误
            if (!StringHelper.isEmpty(djproinst_id)) {
                ProInstService.deleteProInst(djproinst_id);
            }
            throw e;
        }
    }


    public SmObjInfo acceptProject(SmProInfo info, String file_number, List<SmObjInfo> staffList, Pro_proinst proinst, List<Pro_proposerinfo> sqrs, List<Pro_datuminst> datuminsts, List<Pro_attachment> attachments, HttpServletRequest request, String slsqId) throws Exception {
        String proinstId = null;
        SmObjInfo returnSmObj = null;
        try {
            String wf_prodefid = proinst.getWf_prodefid();
            if (wf_prodefid == null || wf_prodefid.isEmpty()) {
                throw new Exception("请先选择要创建的流程。");
            }
            Wfd_Prodef prodef = smProDef.GetProdefById(wf_prodefid);
            if (prodef == null) {
                throw new Exception("选择的流程不存在，请检查。");
            }
            //地方需求：有的地方用的是申请人或权利人的名称当项目名称
            String sqrmcList = "";
            for (Pro_proposerinfo sqr : sqrs) {
                String sqrmc = StringHelper.isEmpty(sqr.getSqr_name()) ? sqr.getSqr_qt_dlr_name() : sqr.getSqr_name();
                if (!StringHelper.isEmpty(sqrmc)) {
                    if (StringHelper.isEmpty(sqrmcList)) {
                        sqrmcList = sqrmc;
                    } else {
                        sqrmcList += "、" + sqrmc;
                    }
                }
            }
            info.setProjectName(sqrmcList);
            // 创建受理项目
            returnSmObj = smProInstService.Accept(info, staffList);
            proinstId = returnSmObj.getID();
            SmObjInfo smInfo = returnSmObj.getChildren().get(0);
            Wfi_ProInst inst = baseCommonDao.get(Wfi_ProInst.class, proinstId);
            if (StringHelper.isEmpty(inst.getFile_Number())) {
                inst.setFile_Number(file_number);
                baseCommonDao.saveOrUpdate(inst);
                baseCommonDao.flush();
            }
            if (!StringHelper.isEmpty(proinst.getWf_prodefname())) {
                //登记系统中受理信息，登记类型和流程类型，是取Wfi_ProInst得prodefname，登记类型和流程类型用，隔开
                String wf_prodefname = proinst.getWf_prodefname();
                wf_prodefname = wf_prodefname.replaceFirst("/", ",");
                inst.setProdef_Name(wf_prodefname);
            }
            inst.setWLSH(proinst.getProlsh());
            proinst.setProend(inst.getProinst_End());
            inst.setProject_Name(sqrmcList);
            ProjectInfo projectInfo = projectService.getProjectInfo(inst.getFile_Number(), request);

            // 更新项目信息，由于使用事务，当创建bdcs_xmxx记录时，需要从工作流库获取信息，但此时事务未提交导致获取不到信息。
            //String xmmc = info.getProInst_Name() == null || info.getProInst_Name().trim().isEmpty() ? smInfo.getID() : info.getProInst_Name();
            BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, projectInfo.getXmbh());
            if (xmxx != null) {
                xmxx.setXMMC(sqrmcList);
                xmxx.setWLSH(proinst.getProlsh());
                xmxx.setSLRY(staffList.get(0).getName());
                xmxx.setSLRYID(staffList.get(0).getID());
                xmxx.setYWLSH(smInfo.getID());
                baseCommonDao.save(xmxx);
                baseCommonDao.flush();
            }

            // 项目编号
            returnSmObj.setDesc(projectInfo.getXmbh());
            // 设置内网生成的流水号
            proinst.setLsh(smInfo.getID());

            // 导入申请人信息与权利需要按具体流程进行具体操作
            List<WFD_MAPPING> mps = baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='" + prodef.getProdef_Code() + "'");
            if (mps == null || mps.size() != 1) {
                throw new Exception("流程配置错误！");
            }
            String baseWorkflowName = mps.get(0).getWORKFLOWNAME();
            acceptProjectService.createSqrs(proinst, projectInfo.getXmbh(), sqrs, baseWorkflowName);

            // 导入申请材料
            List<Pro_datuminst> nomatchs = new ArrayList<Pro_datuminst>();// 没匹配到的存到这里
            // 返回其他资料模板，如果没有则为空
            Wfi_ProMater other = CreateWfi_ProMaters(inst, datuminsts, attachments, nomatchs, staffList);
//            if (!nomatchs.isEmpty()) {
//                if (other == null) {
//                    // 创建其他这个模板
//                    other = smProMater.CreatProMater("其它必要材料", proinstId);
//                    baseCommonDao.save(other);
//                    baseCommonDao.flush();
//                }
//                for (Pro_datuminst mulu : nomatchs) {
//                    List<Pro_attachment> files = acceptProjectService.getFiles(attachments, mulu);
//                    inputFileForGX(inst, other, files, staffList);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeresolvException(e, proinstId);
        }
        return returnSmObj;
    }

    /**
     * 生成file_number
     *
     * @param wlsh
     * @return
     */
    public String createFile_number(String wlsh) {
        List<Pro_proinst> proinsts = baseCommonDaoInline.getDataList(Pro_proinst.class, " PROLSH='" + wlsh + "' ");
        String xmbh = "";
        String ywlsh = "";
        String actinstid = "";
        String djproinst_id = "";
        if (proinsts.size() > 0) {
            String prodefid = proinsts.get(0).getProdefcode();
            Wfd_Prodef prodef = smProDef.GetProdefById(prodefid);
            return smProInst.GetFileNumber(prodef.getProdef_Code());
        }
        return "";
    }

    /**
     * 受理项目审核意见
     */
    public void saveSlxmsh(String wlsh, String msg) {
        List<Pro_proinst> proinsts = baseCommonDaoInline.getDataList(Pro_proinst.class, " PROLSH='" + wlsh + "' ");
        String shry = "";
        List<AUTOPROJECTCONFIG> apcs = baseCommonDao.getDataList(AUTOPROJECTCONFIG.class, " prodef_code = '" + proinsts.get(0).getProdefcode() + "' and sfqy = '1'");
        if (apcs.size() > 0) {
            shry = apcs.get(0).getDBR();
        } else {
            shry = "自动审核";
        }
        Pro_slxmsh slxmsh = new Pro_slxmsh();
        slxmsh.setSh_id(UUID.randomUUID().toString().replace("-", ""));
        slxmsh.setSh_xmsl_rq(new Date());
        slxmsh.setSh_xmsl_ry(shry);
        slxmsh.setSh_xmsl_yj(msg);
        slxmsh.setSlxm_id(proinsts.get(0).getId());
        slxmsh.setShzt(11);
        baseCommonDaoInline.saveOrUpdate(slxmsh);

    }


    public Wfi_ProMater CreateWfi_ProMaters(Wfi_ProInst inst, List<Pro_datuminst> datuminsts, List<Pro_attachment> attachments, List<Pro_datuminst> nomatchs, List<SmObjInfo> staffList) throws Exception
    {
        Wfi_ProMater other = null;
        List<Wfi_ProMater> promaters = smProMater.GetProMaterInfo(inst.getProinst_Id());
        if (promaters != null)
        {
            for (Wfi_ProMater promater : promaters)
            {
                Pro_datuminst datuminst = getPro_datuminstFromWfi_ProMater(datuminsts, promater);
                if (datuminst != null)
                {
                    // 匹配到，抽取资料
                    List<Pro_attachment> files = acceptProjectService.getFiles(attachments, datuminst);
                    inputFileForGX(inst, promater, files, staffList);
                }
//				if (other == null && (promater.getMaterial_Name().contains("其他") || promater.getMaterial_Name().contains("其它")))
//				{
//					other = promater;// 记录其他这个资料模板
//				}
            }
        }
        // 记录没匹配的信息
        for (Pro_datuminst mulu : datuminsts)
        {
            if (nomatchs == null)
            {
                nomatchs = new ArrayList<Pro_datuminst>();
            }
            Boolean matched = false;
            for (Wfi_ProMater promater : promaters)
            {
                if (promater.getMaterial_Name().equals(mulu.getName()))
                {
                    matched = true;
                    break;
                }
            }

            if (!matched)
            {
//				nomatchs.add(mulu);
                //匹配不上的资料，创建自定义模板
                Wfi_ProMater proMaterOther = smProMater.CreatProMater(mulu.getName(), inst.getProinst_Id());
                baseCommonDao.save(proMaterOther);
                baseCommonDao.flush();
                List<Pro_attachment> files = acceptProjectService.getFiles(attachments, mulu);
                inputFileForGX(inst, proMaterOther, files, staffList);
            }
        }
        return other;
    }


    /**
     * 根据内网资料模板项匹配外网资料模板项，匹配不到则返回null
     *
     * @param datuminsts
     * @param promater
     * @return
     */
    private Pro_datuminst getPro_datuminstFromWfi_ProMater(List<Pro_datuminst> datuminsts, Wfi_ProMater promater) {
        Pro_datuminst datuminst = null;
        if (datuminsts != null && promater != null) {
            for (Pro_datuminst item : datuminsts) {
                if (promater.getMaterial_Name() != null && promater.getMaterial_Name().equals(item.getName())) {
                    datuminst = item;
                    break;
                }
            }
        }
        return datuminst;
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
     * 获取流程名称
     *
     * @param prodefid
     * @param prodefclassid
     * @return
     */
    public String getproDefName(String prodefid, String prodefclassid) {
        String prosql = "select nvl2(ps2.prodefclass_name,ps2.prodefclass_name||',','') || nvl2(ps1.prodefclass_name,ps1.prodefclass_name||',','')"
                + "|| nvl2(ps.prodefclass_name,ps.prodefclass_name||',','')|| nvl2(p.prodefclass_name,p.prodefclass_name||',','')|| t.prodef_name proDefName "
                + " from BDC_WORKFLOW.WFD_PRODEF t inner join BDC_WORKFLOW.WFD_PROCLASS p on p.prodefclass_id=t.prodefclass_id "
                + "inner join BDC_WORKFLOW.WFD_PROCLASS ps on ps.prodefclass_id=p.prodefclass_pid "
                + " left join BDC_WORKFLOW.WFD_PROCLASS ps1 on ps1.prodefclass_id=ps.prodefclass_pid "
                + "left join BDC_WORKFLOW.WFD_PROCLASS ps2 on ps2.prodefclass_id=ps1.prodefclass_pid "
                + "where t.prodefclass_id='" + prodefclassid + "' and t.prodef_id='" + prodefid + "'";
        List<Map> prolsit = baseCommonDao.getDataListByFullSql(prosql);
        if (prolsit != null && prolsit.size() > 0) {
            return StringUtil.valueOf(prolsit.get(0).get("PRODEFNAME"));
        }
        return "";
    }

    /**
     * 提取单个附件资料(广西区厅)
     *
     * @param inst
     * @param promater
     * @param files
     * @param staffList
     * @throws Exception
     */
    public void inputFileForGX(Wfi_ProInst inst, Wfi_ProMater promater, List<Pro_attachment> files, List<SmObjInfo> staffList) throws Exception
    {
        String MATERIAL = ConfigHelper.getNameByValue("MATERIAL");//扫描件存储位置
        String syspath = ConfigHelper.getNameByValue("filepath");
        for (Pro_attachment file : files)
        {
            Wfi_MaterData materData = new Wfi_MaterData();
            materData.setMaterialdata_Id(Common.CreatUUID());
            materData.setFile_Name(file.getName()+ file.getSuffix().toLowerCase());
            materData.setMaterilinst_Id(promater.getMaterilinst_Id());
            if (!StringHelper.isEmpty(file.getFile_Index())) {
                materData.setFile_Index(file.getFile_Index());
            } else {
                materData.setFile_Index(System.currentTimeMillis());
            }
            materData.setUpload_Id(staffList.get(0).getID());
            materData.setUpload_Name(staffList.get(0).getName());
            materData.setThumb(null);
            materData.setUpload_Date(new Date());
            materData.setDisc(MATERIAL);
            String fileName = file.getName() + file.getSuffix();
            // 下载ftp文件流
//			ByteArrayOutputStream outputStream = FTPUtils.downloadFile(file.getPath(), fileName);
//			FTPUtils.closeFtp();
            String fileNamepath = syspath+File.separator + file.getPath() +File.separator + file.getName() + file.getSuffix();
            File this_file = new File(fileNamepath);
            FileInputStream in = new FileInputStream(this_file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte [] buffer = new byte[1024];
            int len = 0;
            while( (len = in.read(buffer)) != -1){
                outputStream.write(buffer, 0, len);
            }

            File tempFile = new File("C:\\Windows\\Temp\\" + Common.CreatUUID() + "_" + fileName);
            tempFile.createNewFile();
            java.io.FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(outputStream.toByteArray());
            fos.flush();
            fos.close();
            InlineFileItem dfi = new InlineFileItem(fileName, tempFile);
            dfi.getOutputStream();
            CommonsMultipartFile cmf = new CommonsMultipartFile(dfi);
            @SuppressWarnings("rawtypes")
            List<Map> maps = FileUpload.uploadFile(cmf, promater.getMaterilinst_Id(), inst);
            if (maps != null && maps.size() > 0)
            {
                materData.setFile_Path(maps.get(0).get("filename").toString());
                materData.setPath(maps.get(0).get("filepath").toString());
            }
            promater.setImg_Path(materData.getMaterialdata_Id());
            baseCommonDao.save(materData);
            baseCommonDao.flush();
        }
        promater.setMaterial_Count(files.size());
        promater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
        promater.setMaterial_Count(1);
        baseCommonDao.save(promater);
        baseCommonDao.flush();
    }


    /**
     * 获取登记业务号
     *
     * @param proinstid
     * @return
     */
    private Map getYwLsh(String proinstid) {
        List<Wfi_ProInst> proilist = baseCommonDao.getDataList(Wfi_ProInst.class, "proinst_id='" + proinstid + "'");
        Map map = new HashMap();
        if (proilist != null && proilist.size() > 0) {
            map.put("ywlsh", proilist.get(0).getProlsh());
            map.put("proinstid", proilist.get(0).getProinst_Id());
            return map;
        }
        return map;
    }


    /**
     * 检查该流水号的外网项目是否已经在登记系统创建项目
     *
     * @param wlsh
     * @param bjlog
     */
    public void createdExamine(String wlsh, XS24HZDBJ bjlog) {
        List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, " wlsh = '" + wlsh + "' ");
        if (xmxxs.size() > 0) {
            throw new BeresolvException(bjlog, "0", "该业务已经办理，请勿重新办理！", "1001");
        }
    }

    private XS24HZDBJ setUseLogDQZT(XS24HZDBJ bjlog, String zt, String msg, String errorcode) {
        bjlog.setDQZT(zt);
        if (!StringHelper.isEmpty(msg)) {
            bjlog.setDQSBYY(msg);
        }
        bjlog.setErrorCode(errorcode);
        baseCommonDao.getCurrentSession().clear();
        baseCommonDao.saveOrUpdate(bjlog);
        baseCommonDao.flush();
        return bjlog;
    }

    @Override
    public XS24HZDBJ saveXS24HZDBJ(XS24HZDBJ bjlog) {
        baseCommonDao.saveOrUpdate(bjlog);
        baseCommonDao.flush();
        return bjlog;
    }

    /**
     * 自动办理失败，记录报错日志
     *
     * @param wlsh
     */
    public XS24HZDBJ errorLogzt(String wlsh, Exception e) {
        List<XS24HZDBJ> uselogs = baseCommonDao.getDataList(XS24HZDBJ.class, " ZWLSH ='" + wlsh + "' ");
        StackTraceElement test = e.getStackTrace()[0];
        XS24HZDBJ uselog = null;
        String errMsg = null;
        if (!StringHelper.isEmpty(e.getCause())) {
            errMsg = e.getCause().getMessage();
        } else {
            errMsg = e.getMessage();
        }
        if (uselogs.size() > 0) {
            uselog = uselogs.get(0);
            uselog.setDQZT("0");
            uselog.setErrorCode("3001");
            uselog.setDQSBYY("出现异常，文件名：" + test.getFileName() + "，异常行数：" + test.getLineNumber() + "，错误消息:" + errMsg);
        } else {
            uselog = new XS24HZDBJ();
            uselog.setId(Common.CreatUUID());
            uselog.setZWLSH(wlsh);
            uselog.setDQSJ(new Date());
            uselog.setSCDQSJ(new Date());
            uselog.setDQCS(1);
            uselog.setDQZT("0");
//            uselog.setSFDB("0");
            uselog.setErrorCode("3001");
            uselog.setDQSBYY("出现异常，文件名：" + test.getFileName() + "，异常行数：" + test.getLineNumber() + "，错误消息:" + errMsg);
        }

        baseCommonDao.getCurrentSession().clear();
        baseCommonDao.saveOrUpdate(uselog);
        baseCommonDao.flush();
        return uselog;
    }

    /**
     * 通过外网流水号更改日志
     */
    private XS24HZDBJ setUseLogDQZT(String zwlcid, String zt, String msg) {
        List<XS24HZDBJ> uselogs = baseCommonDao.getDataList(XS24HZDBJ.class, " ZWLSH ='" + zwlcid + "' "); //日志
        XS24HZDBJ uselog = new XS24HZDBJ();
        if (uselogs.size() > 0) {
            uselog = uselogs.get(0);
            uselog.setDQZT(zt);
            uselog.setDQSBYY(msg);
            baseCommonDao.saveOrUpdate(uselog);
            baseCommonDao.flush();
        }
        return uselog;
    }

    //*********************************配置模块Start****************************************

    /**
     * 配置表新增
     */
    @Override
    public Message addAutoProjectConfig(Map<String, String> mapCondition) {
        Message msg = new Message();
        String prodef_id = mapCondition.get("PRODEF_ID");
        if (StringHelper.isEmpty(prodef_id)) {
            msg.setMsg("登记流程ID不能为空!");
            msg.setSuccess("false");
            return msg;
        }
        Wfd_Prodef prodef = baseCommonDao.get(Wfd_Prodef.class, prodef_id);
        if (prodef == null) {
            msg.setMsg("未找到对应流程!");
            msg.setSuccess("false");
            return msg;
        }
        String prodefCode = prodef.getProdef_Code();
        if (StringHelper.isEmpty(prodefCode)) {
            msg.setMsg("流程解析错误，Prodef_Code为空!");
            msg.setSuccess("false");
            return msg;
        }
        List<WFD_MAPPING> mps = baseCommonDao.getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + prodefCode + "'");
        if (mps == null || mps.size() != 1) {
            msg.setMsg("流程配置错误，WFD_MAPPING找不到或重复!");
            msg.setSuccess("false");
            return msg;
        }

        String czfs = mapCondition.get("CZFS");
        String urlnametype = mapCondition.get("URLNAMETYPE");
        String dbr = mapCondition.get("DBR");
        String dbrid = mapCondition.get("DBRID");
        String slry = mapCondition.get("SLRY");
        String slryid = mapCondition.get("SLRYID");
        String routeid = mapCondition.get("ROUTEID");
        String djyy = mapCondition.get("DJYY");
        String szry = mapCondition.get("SZRY");
        String szryid = mapCondition.get("SZRYID");
        String fj = mapCondition.get("FJ");
        String ismult = mapCondition.get("ISMULT");
        String syqr = mapCondition.get("TDSHYQR");
        String spjy = mapCondition.get("SPYJ");
        String qlgettype = mapCondition.get("QLGETTYPE");
        String SFSCQLR = mapCondition.get("SFSCQLR");
        String SFJZQLR = mapCondition.get("SFJZQLR");
        String sfbdcqzh = mapCondition.get("SFJZBDCQZH");
        String zxdbr = mapCondition.get("ZXDBR");
        String ishangup = mapCondition.get("ISHANGUP");
        String isydb = mapCondition.get("ISYDB");
        String sfqy = mapCondition.get("SFQY");
        //新增
        String sql = " FROM BDCK.AUTOPROJECTCONFIG WHERE PRODEF_ID='" + prodef_id + "'";
        long count = baseCommonDao.getCountByFullSql(sql);
        if (count > 0) {
            msg.setMsg("该登记流程ID已经存在，无法新增!");
            msg.setSuccess("false");
        } else {
            AUTOPROJECTCONFIG autoprojectconfig = new AUTOPROJECTCONFIG();
            autoprojectconfig.setID(Common.CreatUUID());
            autoprojectconfig.setPRODEF_ID(prodef_id);
            autoprojectconfig.setPRODEF_CODE(prodef.getProdef_Code());
            autoprojectconfig.setPRODEF_NAME(mps.get(0).getWORKFLOWCAPTION());
            autoprojectconfig.setDBR(dbr);
            autoprojectconfig.setDBRID(dbrid);
            autoprojectconfig.setSPYJ(spjy);
            autoprojectconfig.setSTAFFID(slryid);
            autoprojectconfig.setSTAFFNAME(slry);
            autoprojectconfig.setSZRY(szry);
            autoprojectconfig.setSZRYID(szryid);
            autoprojectconfig.setDJYY(djyy);
            autoprojectconfig.setFJ(fj);
            autoprojectconfig.setCZFS(czfs);
            autoprojectconfig.setROUTEID(routeid);
            autoprojectconfig.setSYQR(syqr);
            autoprojectconfig.setCREATETIME(new Date());
            autoprojectconfig.setZXDBR(zxdbr);
            autoprojectconfig.setISHANGUP(ishangup);
            autoprojectconfig.setISYDB(isydb);
            autoprojectconfig.setSFQY(sfqy);
            autoprojectconfig.setBZ(DateUtil.getYear() + "年" + DateUtil.getMonth() + "月" + DateUtil.getDay() + "日" + DateUtil.getTime() + "新增");
            baseCommonDao.save(autoprojectconfig);
            baseCommonDao.flush();
            msg.setMsg("新增成功!");
            msg.setSuccess("true");
        }
        return msg;

    }

    /**
     * 配置表查询
     */
    @Override
    public Message autoProjectConfigQueryService(String djcode, int page, int size) {
        Message msg = new Message();
        String wheresql = " 1=1 ";
        if (djcode != null && !"".equals(djcode)) {
            wheresql += " AND PRODEF_CODE ='" + djcode + "'";
        }
        String sql = "from bdck.AUTOPROJECTCONFIG where " + wheresql;
        long count = baseCommonDao.getCountByFullSql(sql);
        if (count > 0) {
            List<Map> datas = baseCommonDao.getPageDataByFullSql("select * " + sql, page, size);
            for (int i = 0; i < datas.size(); i++) {
                Map map = datas.get(i);
                String sfqy = StringHelper.formatObject(map.get("SFQY")); //是否有效 1启用 0关闭
                String czfs = StringHelper.formatObject(map.get("CZFS")); //  01共同持证，02分别持证

                if ("0".equals(sfqy)) {
                    datas.get(i).put("SFQY", "未启用");
                } else if ("1".equals(sfqy)) {
                    datas.get(i).put("SFQY", "启用");
                } else {
                    datas.get(i).put("SFQY", "");
                }

                if ("01".equals(czfs)) {
                    datas.get(i).put("CZFS", "共同持证");
                } else if ("02".equals(czfs)) {
                    datas.get(i).put("CZFS", "分别持证");
                } else {
                    datas.get(i).put("CZFS", "");
                }
            }
            msg.setRows(datas);
            msg.setTotal(count);
            msg.setSuccess("true");
            msg.setMsg("查询成功");
        } else {
            msg.setSuccess("false");
            msg.setMsg("没有查询到数据");
        }
        return msg;
    }

    /**
     * 配置表修改
     */
    @Override
    public Message autoProjectConfigUpdateService(Map<String, String> mapCondition) {
        Message msg = new Message();
        String prodef_id = mapCondition.get("PRODEF_ID");
        if (StringHelper.isEmpty(prodef_id)) {
            msg.setMsg("登记流程ID不能为空!");
            msg.setSuccess("false");
            return msg;
        }
        Wfd_Prodef prodef = baseCommonDao.get(Wfd_Prodef.class, prodef_id);
        if (prodef == null) {
            msg.setMsg("未找到对应流程!");
            msg.setSuccess("false");
            return msg;
        }
        String prodefCode = prodef.getProdef_Code();
        if (StringHelper.isEmpty(prodefCode)) {
            msg.setMsg("流程解析错误，Prodef_Code为空!");
            msg.setSuccess("false");
            return msg;
        }
        List<WFD_MAPPING> mps = baseCommonDao.getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + prodefCode + "'");
        if (mps == null || mps.size() != 1) {
            msg.setMsg("流程配置错误，WFD_MAPPING找不到或重复!");
            msg.setSuccess("false");
            return msg;
        }
        String id = mapCondition.get("ID");
        String czfs = mapCondition.get("CZFS");
        String dbr = mapCondition.get("DBR");
        String urlnametype = mapCondition.get("URLNAMETYPE");
        String dbrid = mapCondition.get("DBRID");
        String slry = mapCondition.get("SLRY");
        String slryid = mapCondition.get("SLRYID");
        String routeid = mapCondition.get("ROUTEID");
        String djyy = mapCondition.get("DJYY");
        String szry = mapCondition.get("SZRY");
        String szryid = mapCondition.get("SZRYID");
        String fj = mapCondition.get("FJ");
        String ismult = mapCondition.get("ISMULT");
        String syqr = mapCondition.get("TDSHYQR");
        String spjy = mapCondition.get("SPYJ");
        String qlgettype = mapCondition.get("QLGETTYPE");
        String SFSCQLR = mapCondition.get("SFSCQLR");
        String SFJZQLR = mapCondition.get("SFJZQLR");
        String sfbdcqzh = mapCondition.get("SFJZBDCQZH");
        String zxdbr = mapCondition.get("ZXDBR");
        String ishangup = mapCondition.get("ISHANGUP");
        String isydb = mapCondition.get("ISYDB");
        String sfqy = mapCondition.get("SFQY");
        //修改数据
        if (StringHelper.isEmpty(id)) {
            msg.setMsg("该配置不存在，无法编辑！");
            msg.setSuccess("false");
        }
        AUTOPROJECTCONFIG autoprojectconfig = baseCommonDao.get(AUTOPROJECTCONFIG.class, id);
        if (autoprojectconfig != null) {
            autoprojectconfig.setPRODEF_ID(prodef_id);
            autoprojectconfig.setPRODEF_CODE(prodef.getProdef_Code());
            autoprojectconfig.setPRODEF_NAME(mps.get(0).getWORKFLOWCAPTION());
            autoprojectconfig.setDBR(dbr);
            autoprojectconfig.setDBRID(dbrid);
            autoprojectconfig.setSPYJ(spjy);
            autoprojectconfig.setSTAFFID(slryid);
            autoprojectconfig.setSTAFFNAME(slry);
            autoprojectconfig.setSZRY(szry);
            autoprojectconfig.setSZRYID(szryid);
            autoprojectconfig.setDJYY(djyy);
            autoprojectconfig.setFJ(fj);
            autoprojectconfig.setCZFS(czfs);
            autoprojectconfig.setROUTEID(routeid);
            autoprojectconfig.setSYQR(syqr);
            autoprojectconfig.setZXDBR(zxdbr);
            autoprojectconfig.setISHANGUP(ishangup);
            autoprojectconfig.setISYDB(isydb);
            autoprojectconfig.setSFQY(sfqy);
            autoprojectconfig.setBZ(DateUtil.getYear() + "年" + DateUtil.getMonth() + "月" + DateUtil.getDay() + "日" + DateUtil.getTime() + "编辑");
            //最后修改时间
            User user = Global.getCurrentUserInfo();
            autoprojectconfig.setMODIFY_TIME(new Date());
            autoprojectconfig.setMODIFY_USERID(user.getId());
            autoprojectconfig.setMODIFY_USERNAME(user.getUserName());
            baseCommonDao.saveOrUpdate(autoprojectconfig);
            baseCommonDao.flush();
            msg.setMsg("编辑成功");
            msg.setSuccess("true");
            return msg;
        } else {
            msg.setMsg("该配置不存在，无法编辑！");
            msg.setSuccess("false");
            return msg;
        }
    }

    /**
     * 配置表删除
     */
    @Override
    public Message autoProjectConfigDeleteService(String id) {
        Message msg = new Message();
        //删除数据
        String sql = " FROM BDCK.AUTOPROJECTCONFIG WHERE ID='" + id + "'";
        long count = baseCommonDao.getCountByFullSql(sql);
        if (count > 0) {
            baseCommonDao.delete(AUTOPROJECTCONFIG.class, id);
            msg.setSuccess("true");
            msg.setMsg("删除成功");
        } else {
            msg.setSuccess("false");
            msg.setMsg("未获取到需要删除的数据！");
        }
        return msg;
    }

    //*********************************配置模块END****************************************

    /**
     * 受理信息校验
     *
     * @param wlsh 外网受理流水号
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void checkAccectConstraint(String wlsh, HttpServletRequest request, XS24HZDBJ bjlog) throws Exception {
        //1.通过wlsh获取inline_inner.Pro_proinst
        List<Pro_proinst> proinstList = baseCommonDaoInline.getDataList(Pro_proinst.class, "PROLSH='" + wlsh + "' ");
        if (proinstList.size() > 0) {
            if (proinstList.size() > 1) {
                throw new BeresolvException(bjlog, "2", String.format("该业务重号，请检查，pro_proinst中prolsh=%s的数据有%s条记录！", wlsh, proinstList.size()), "0014");
            } else {
                Pro_proinst proinst = proinstList.get(0);
                String djlx = StringHelper.formatObject(proinst.getDjlx());
                String qllx = StringHelper.formatObject(proinst.getQllx());
                String wf_prodefid = proinst.getProdefcode();
                String proinstId = proinst.getId();

                if (StringHelper.isEmpty(wf_prodefid)) {
                    throw new BeresolvException(bjlog, "2", String.format("外网流程配置错误，请检查pro_proinst中prolsh=%s的Prodefcode字段是否为空!", wlsh), "2001");
                }

                Wfd_Prodef prodef = smProDef.GetProdefById(wf_prodefid);
                if (prodef == null) {
                    throw new BeresolvException(bjlog, "2", String.format("内网流程匹配错误，id为%s的记录在Wfd_Prodef表中未找到！", wf_prodefid), "2001");
                }

                List<Map> proFwxxList = baseCommonDaoInline.getDataListByFullSql(MessageFormat.format("select ID,BDCDYH,BDCQZMH,FWBM,ZL from  INLINE_INNER.PRO_FWXX where PROINST_ID=''{0}'' ", proinstId));
                if (proFwxxList.isEmpty()) {
                    throw new BeresolvException(bjlog, "2", String.format("该业务丢失房屋信息，请检查PRO_FWXX，pro_proinst中prolsh=%s，PROINST_ID=%s", wlsh, proinstId), "1002");
                }

                // 导入申请人信息与权利需要按具体流程进行具体操作
                List<WFD_MAPPING> mps = baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='" + prodef.getProdef_Code() + "'");
                if (mps == null || mps.size() != 1) {
                    throw new BeresolvException(bjlog, "2", String.format("该业务流程配置错误！，请检查，pro_proinst中prolsh=%s的数据有%s条记录！", wlsh, proinstList.size()), "2001");
                }

                //项目对应的申请人信息中的义务人
                List<Map> proposerinfoYWRList = baseCommonDaoInline.getDataListByFullSql(MessageFormat.format("select SQR_NAME,SQR_ZJH,PROINST_ID,FWXX_ID,SQRJZH from  INLINE_INNER.PRO_PROPOSERINFO where PROINST_ID=''{0}'' and SQR_LX=1 AND (SFCQR IS NULL OR SFCQR=1)  ", proinstId));
                //当前申请项目对应的申请人信息中的权利人
                List<Map> proposerinfoQLRList = baseCommonDaoInline.getDataListByFullSql(MessageFormat.format("select SQR_NAME,SQR_ZJH,PROINST_ID,FWXX_ID,SQRJZH from  INLINE_INNER.PRO_PROPOSERINFO where PROINST_ID=''{0}'' and SQR_LX=0 AND (SFCQR IS NULL OR SFCQR=1)  ", proinstId));

                String baseWorkflowName = mps.get(0).getWORKFLOWNAME();
                if ("ZY001".equals(baseWorkflowName) || "ZY003".equals(baseWorkflowName) || "ZY005".equals(baseWorkflowName)
                        || "ZY009".equals(baseWorkflowName) || "ZY012".equals(baseWorkflowName) || "HZ004".equals(baseWorkflowName)
                        || "HZ006".equals(baseWorkflowName) || "HZ008".equals(baseWorkflowName) || "BZ002".equals(baseWorkflowName)
                        || "BZ010".equals(baseWorkflowName) || "BZ012".equals(baseWorkflowName) || "CS002".equals(baseWorkflowName)
                         || "BG003".equals(baseWorkflowName)
                        || "BG004".equals(baseWorkflowName) || "BG014".equals(baseWorkflowName)) {
//                    throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                } else if("CS004".equals(baseWorkflowName) || "CS005".equals(baseWorkflowName)) {

                }
                //转移登记的校验
                else if ("ZY002".equals(baseWorkflowName) || "ZY007".equals(baseWorkflowName) || "ZY010".equals(baseWorkflowName)   || "ZY020".equals(baseWorkflowName)){
                	checkZYDJInfo(proFwxxList, proposerinfoYWRList, proposerinfoQLRList, proinst.getYwlx(), bjlog);
                }
                else if ("CS010".equals(baseWorkflowName) || "CS011".equals(baseWorkflowName) || "CS034".equals(baseWorkflowName) ) {
//                    throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                    checkDyAndSQR(proFwxxList, proposerinfoYWRList, "100", "4", bjlog, request);
                } else if ("CS013".equals(baseWorkflowName)) {
//                    throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                } else if ("ZX003".equals(baseWorkflowName) || "ZX004".equals(baseWorkflowName) || "ZX006".equals(baseWorkflowName)
                        || "ZX009".equals(baseWorkflowName)) {
                    //ZX003 注销登记_抵押权_使用权宗地
                    //ZX004 注销登记_抵押权_户
                    //ZX006 注销登记_抵押权_预测户（预抵押注销）
                    //ZX009 注销登记_抵押权_在建工程抵押注销
                    if ("400".equals(djlx) && "23".equals(qllx)) {//抵押注销
                        //校验房屋及权利信息
                        checkDYQLInfo(proFwxxList, proposerinfoYWRList, proposerinfoQLRList, "400", "23", bjlog);
                    } else {
                        throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                    }
                } else if ("BG003".equals(baseWorkflowName) || "BG004".equals(baseWorkflowName) || "BG014".equals(baseWorkflowName)) {
                    throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                } else if ("YG001".equals(baseWorkflowName) || "YG002".equals(baseWorkflowName) || "YG102".equals(baseWorkflowName) || "YG003".equals(baseWorkflowName)) {
                    //预告登记_预购商品房预告
                    //YG002 预告登记_预告登记+预抵押登记
                    //YG102 预告登记_预告登记+预抵押登记(泸州版本)
                    //YG003 预告登记_预抵押登记
                    if ("700".equals(djlx) && ("4".equals(qllx) || "423".equals(qllx)) ) {//预告登记
                        //预告登记，只校验BDCDYH，坐落暂时不校验
                        for (Map fw : proFwxxList) {
                            String BDCDYH = StringHelper.formatObject(fw.get("BDCDYH"));
                            if (!"".equals(BDCDYH)) {
                                String fulSqlH = MessageFormat.format("select bdcdyh,zl,bdcdyid from BDCK.BDCS_H_XZY where bdcdyh=''{0}'' ", BDCDYH);
                                List<Map> resultH = baseCommonDao.getDataListByFullSql(fulSqlH);
                                if (resultH.size() < 1) {
                                    throw new ManualException(bjlog, "2", String.format("房屋信息错误，不动产单元号【%s】找不到相应房屋", BDCDYH), "1005");
                                }
                            } else {
                                throw new ManualException(bjlog, "2", String.format("房屋信息错误，不动产单元号不允许为空", BDCDYH), "1005");
                            }
                        }
                    } else if ("700".equals(djlx) && "23".equals(qllx)) {//预抵押
                        //预抵押，校验义务人是否和房子信息相符
                        checkDyAndSQR(proFwxxList, proposerinfoYWRList, "700", "4", bjlog, request);
                    } else {
                        throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                    }
                } else {
                    throw new ManualException(bjlog, "2", "该业务类型不支持！", "1005");
                }
            }
        } else {
            throw new BeresolvException(bjlog, "2", String.format("该业务不存在，请检查，pro_proinst中prolsh=%s的数据不存在！", wlsh), "1002");
        }
    }

    /**
     * 校验权利人和单元是否相符
     *
     * @param proFwxxList
     * @param djlx
     * @param qllx
     * @param bjlog
     */
    private void checkDyAndSQR(List<Map> proFwxxList, List<Map> qlrlist, String djlx, String qllx, XS24HZDBJ bjlog, HttpServletRequest request) {
        //由申请人出发，查其房产及填写的信息与查询结果是否匹配
        for (Map fw : proFwxxList) {
            boolean flag = false;
            for (Map qlr : qlrlist) {
                //获取填写的权利人对应的单元
                String BDCDYH = StringHelper.formatObject(fw.get("BDCDYH"));
                String BDCQZMH = StringHelper.formatObject(fw.get("BDCQZMH"));
                String qlrmc = StringHelper.formatObject(qlr.get("SQR_NAME"));
                String zjh = StringHelper.formatObject(qlr.get("SQR_ZJH"));
                if (!StringHelper.isEmpty(BDCQZMH)) {
                    //如果权证号不为空，先验证身份和权证号是否相符
                    long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QLR_XZ QL WHERE  TRIM(QL.BDCQZH)='" + BDCQZMH.trim() + "' ");
                    if (count == 0) {
                        throw new ManualException(bjlog, "2", "填写不动产权证号(证明号)：" + BDCQZMH + "查无数据，请核查是否填写有误", "4001");
                    }
                }

                Map map = autoQueryService.queryHouse(request, qlrmc, zjh, BDCQZMH, BDCDYH);
                Object resultlist = map.get("list");
                if (resultlist == null) {
//                    throw new ManualException(bjlog, "2", "填写的申请人:" + qlr.get("SQR_NAME") + "信息与不动产权证明号：" + BDCQZMH + "匹配失败，请核查信息是否有误", "4001");
                    continue;
                }
                List<Map> dylist = (List<Map>) resultlist;
                for (Map dy : dylist) {
                    String dyh = StringHelper.formatObject(dy.get("BDCDYH"));
                    if (!StringHelper.isEmpty(BDCDYH)) {
                        //如果填了单元号，返回结果中没有单元号或者单元号对不上就是填写得不对
                        if (!BDCDYH.equals(dyh)) {
                            throw new ManualException(bjlog, "2", "填写的申请人:" + qlr.get("SQR_NAME") + "信息与不动产单元号：" + BDCDYH + "匹配失败，请核查信息是否有误", "4001");
                        }
                    }
                }
                flag = true;
            }
            if (!flag) {
                throw new ManualException(bjlog, "2", "填写的申请人与登记信息不符，请检查申请人姓名、证件号和房屋单元号、权证号（证明号）是否正确", "4001");
            }
        }

    }


    @SuppressWarnings("rawtypes")
    @Override
    public Message search24Hzdbjxx(String zwlsh, String djlsh, String sjpx, String dqzt, String sfxxbl,
                                   String cssj, String zzsj, String pageIndex, String pageSize) {
        Message msg = new Message();
        StringBuilder sqlstb = new StringBuilder();
        sqlstb.append("select t.bsm,t.zwlsh,t.dqsj,t.sfdb,t.dqzt,t.dqcs,t.scdqsj,t.sfxxbl,t.djlsh,t.dqsbyy,t.errorcode from bdck.XS24HZDBJ t where 1=1 ");
        //线上受理号
        if (!"".equals(zwlsh)) {
            sqlstb.append("and zwlsh='").append(zwlsh).append("' ");
        }
        //线上受理号
        if (!"".equals(djlsh)) {
            sqlstb.append("and djlsh='").append(djlsh).append("' ");
        }
        //调取状态
        if (!"".equals(dqzt)) {
            if (!"99".equals(dqzt)) {
                sqlstb.append("and dqzt='").append(dqzt).append("' ");
            }
        }
        //是否转线下办理
        if (!"".equals(sfxxbl)) {
            if (!"99".equals(sfxxbl)) {
                sqlstb.append("and sfxxbl='").append(sfxxbl).append("' ");
            }
        }
        //调取起始时间
        if (!"".equals(cssj)) {
            sqlstb.append("and dqsj >= to_date('").append(cssj).append("','yyyy-mm-dd hh24:mi:ss') ");
        }
        //调取终止时间
        if (!"".equals(zzsj)) {
            sqlstb.append("and dqsj <= to_date('").append(zzsj).append("','yyyy-mm-dd hh24:mi:ss') ");
        }
        //时间排序
        if (!"".equals(sjpx)) {
            if ("1".equals(sjpx)) {
                sqlstb.append("order by dqsj desc ");
            } else {
                sqlstb.append("order by dqsj asc ");
            }
        }
        try {
            List<Map> list = baseCommonDao.getPageDataByFullSql(sqlstb.toString(), new Integer(pageIndex), new Integer(pageSize));
            if (list != null) {
                if (list.size() == 0) {
                    msg.setTotal(0);
                } else {
                    msg.setTotal(baseCommonDao.getCountByFullSql("from (" + sqlstb.toString() + ")"));
                }
                msg.setSuccess("true");
                msg.setRows(list);
                msg.setMsg("查询成功！");
                return msg;
            }
            msg.setSuccess("false");
            msg.setMsg("查询失败！查询结果为null");
            return msg;
        } catch (Exception e) {
            msg.setSuccess("false");
            msg.setMsg("查询失败！查询结果为:" + e.getMessage());
            return msg;
        }
    }

    /**
     * 查询失败原因
     */
    @Override
    public String failresult(String zwlsh) {
        List<XS24HZDBJ> xlist = baseCommonDao.getDataList(XS24HZDBJ.class, "zwlsh='" + zwlsh + "'");
        return xlist.get(0).getDQSBYY() == null ? "无" : xlist.get(0).getDQSBYY();
    }

    /**
     * 抵押注销验证
     */
    @SuppressWarnings("rawtypes")
    private void checkDYQLInfo(List<Map> proFwxxList, List<Map> proposerinfoYWRList, List<Map> proposerinfoQLRList, String djlx, String qllx, XS24HZDBJ bjlog) {
        //校验房屋及权利信息
        for (Map fw : proFwxxList) {
            String BDCQZMH = StringHelper.formatObject(fw.get("BDCQZMH"));
            //先校验权利人和这个证号的关系
            if (!StringHelper.isEmpty(BDCQZMH)) {
                String djdyid = "";
                long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QLR_XZ QL WHERE TRIM(QL.BDCQZH)='" + BDCQZMH.trim() + "' ");
                if (count == 0) {
                    throw new ManualException(bjlog, "2", "填写的不动产权证号(证明号)：" + BDCQZMH + "查询失败，请核查信息是否有误", "4001");
                }
                List<Map> djdys = baseCommonDao.getDataListByFullSql("SELECT QL.DJDYID FROM BDCK.BDCS_QL_XZ QL WHERE QL.QLID=(SELECT QL.QLID FROM BDCK.BDCS_QLR_XZ QL WHERE TRIM(QL.BDCQZH)='" + BDCQZMH.trim() + "' )");
                djdyid = StringHelper.formatObject(djdys.get(0).get("DJDYID"));
                //再验证义务人与这个单元是否有关系
                String fullsql = "SELECT DYR.QLRMC,DYR.ZJH FROM BDCK.BDCS_QLR_XZ DYR WHERE DYR.QLID IN (" +
                        "SELECT DYRQL.QLID FROM BDCK.BDCS_QL_XZ DYRQL WHERE DYRQL.DJDYID='" + djdyid + "' AND QLLX<>'23') ";
                List<Map> dyrs = baseCommonDao.getDataListByFullSql(fullsql);
                //外网不一定把所有抵押人都填完，但是不能填多，所以以外网填写的抵押人即义务人为循环点，分别跟登记数据对比,只要有一个外网填写的义务人跟在登记里没有，就返回false
                if (proposerinfoYWRList.size() == 0) {
                    throw new ManualException(bjlog, "2", "请填写抵押人信息", "4001");
                }
                boolean flag = false;//每个义务人都判断，是否在登记有数据
                for (Map ywr : proposerinfoYWRList) {
                    for (Map dyr : dyrs) {
                        String ywrmc = StringHelper.formatObject(ywr.get("SQR_NAME"));
                        String ywrzjh = StringHelper.formatObject(ywr.get("SQR_ZJH"));
                        String sqrjzh = StringHelper.formatObject(ywr.get("SQRJZH"));
                        String dyrmc = StringHelper.formatObject(dyr.get("QLRMC"));
                        String dyrzjh = StringHelper.formatObject(dyr.get("ZJH"));
                        if (ywrmc.equals(dyrmc) && (ywrzjh.equals(dyrzjh) || dyrzjh.equals(sqrjzh))) {
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    throw new ManualException(bjlog, "2", "填写的申请人与登记信息不符，请检查申请人信息及房屋单元号、权证号（证明号）是否正确", "4001");
                }
            } else {
                throw new ManualException(bjlog, "2", "不动产权证号(证明号)不能为空", "1005");
            }
        }
    }
    

    /**
     * 转移登记验证
     */
    @SuppressWarnings("rawtypes")
    private void checkZYDJInfo(List<Map> proFwxxList, List<Map> proposerinfoYWRList, List<Map> proposerinfoQLRList,String ywlx, XS24HZDBJ bjlog) {
        List<String> list_qlrmc_zjh=new ArrayList<String>();
        if(proposerinfoQLRList!=null&&proposerinfoQLRList.size()>0){
            for(Map qlr : proposerinfoQLRList){
                list_qlrmc_zjh.add(StringHelper.formatObject(qlr.get("SQR_NAME"))+"&"+StringHelper.formatObject(qlr.get("SQR_ZJH")));
            }
        }
        //校验房屋及权利信息
        for (Map fw : proFwxxList) {
            String BDCQZMH = StringHelper.formatObject(fw.get("BDCQZMH"));
            String BDCDYH = StringHelper.formatObject(fw.get("BDCDYH"));
            //验证外网义务人信息是否和登记数据相同,只要有一个外网填写的义务人跟在登记里没有，就返回false
            if (proposerinfoYWRList.size() == 0) {
                throw new ManualException(bjlog, "2", "请填写义务人信息", "4001");
            }
            //先校验权利人和这个证号的关系
            if (!StringHelper.isEmpty(BDCQZMH) || !StringHelper.isEmpty(BDCDYH)) {
                String djdyid = "";
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT QL.DJDYID FROM BDCK.BDCS_QLR_XZ QLR LEFT JOIN BDCK.BDCS_QL_XZ QL ON QLR.QLID=QL.QLID WHERE 1=1 AND QL.DJLX<>'700' ");
                if(!StringHelper.isEmpty(BDCQZMH)) {
                    sb.append("AND TRIM(QLR.BDCQZH)='" + BDCQZMH.trim() + "'");
                    long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QLR_XZ QL WHERE TRIM(QL.BDCQZH)='" + BDCQZMH.trim() + "' ");
                    if (count == 0) {
                        throw new ManualException(bjlog, "2", "填写的不动产权证号：" + BDCQZMH + "查询失败，请核查信息是否有误", "4001");
                    }
//                    sb.append("AND TRIM(QL.BDCQZH)='" + BDCQZMH.trim() + "'");
                }
                if(!StringHelper.isEmpty(BDCDYH)) {
                    long count = baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QL_XZ QL WHERE TRIM(QL.BDCDYH)='" + BDCDYH.trim() + "' ");
                    if (count == 0) {
                        throw new ManualException(bjlog, "2", "填写的不动产单元号：" + BDCDYH + "查询失败，请核查信息是否有误", "4001");
                    }
                    sb.append("AND TRIM(QL.BDCDYH)='" + BDCDYH.trim() + "'");
                }

                List<Map> djdys = baseCommonDao.getDataListByFullSql(sb.toString());
                if(djdys.isEmpty() || StringHelper.isEmpty(djdys.get(0).get("DJDYID"))) {
                    throw new ManualException(bjlog, "2", "查不到单元信息，请检查数据是否完整", "4001");
                }

                djdyid = StringHelper.formatObject(djdys.get(0).get("DJDYID"));
                //获取该不动产单元的权利人信息 用于和外网填写的义务人对比
                String fullsql = "SELECT QLR.QLRMC,QLR.ZJH,QLR.QLRLX FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID IN (" +
                        "SELECT SYQ.QLID FROM BDCK.BDCS_QL_XZ SYQ WHERE SYQ.DJDYID='" + djdyid + "' AND SYQ.QLLX in('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18') )";
                List<Map> qlrs = baseCommonDao.getDataListByFullSql(fullsql);

                //验证外网填写的义务人信息是否和办理的类型匹配
                boolean flag = false;//每个义务人都判断，是否在登记有数据
                for (Map ywr : proposerinfoYWRList) {
                    for (Map qlr : qlrs) {
                        String ywrmc = StringHelper.formatObject(ywr.get("SQR_NAME"));
                        String ywrzjh = StringHelper.formatObject(ywr.get("SQR_ZJH"));
                        String sqrjzh = StringHelper.formatObject(ywr.get("SQRJZH"));
                        String qlrmc = StringHelper.formatObject(qlr.get("QLRMC"));
                        String qlrzjh = StringHelper.formatObject(qlr.get("ZJH"));
                        if (ywrmc.equals(qlrmc) && (ywrzjh.equals(qlrzjh) || qlrzjh.equals(sqrjzh))) {
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    throw new ManualException(bjlog, "2", "填写的申请人与登记信息不符，请检查申请人姓名、证件号和房屋单元号、权证号（证明号）是否正确", "4001");
                }


                //转移的权利人要和预告的权利人一致
                List<Map> listGX=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.YC_SC_H_XZ WHERE SCBDCDYID in (SELECT BDCDYID FROM BDCK.BDCS_DJDY_XZ  WHERE DJDYID='"+djdyid+"')");
                if(listGX!=null&&listGX.size()>0){
                    for(Map gx:listGX){
                        String YCBDCDYID=StringHelper.formatObject(gx.get("YCBDCDYID"));
                        if(StringHelper.isEmpty(YCBDCDYID)){
                            continue;
                        }
                        List<BDCS_DJDY_XZ> djdys_yc=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+YCBDCDYID+"'");
                        if(djdys_yc==null||djdys_yc.size()<=0){
                            continue;
                        }
                        String ycdjdyid=djdys_yc.get(0).getDJDYID();
                        List<Rights> ycyg_qls=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.XZ, "DJDYID='"+ycdjdyid+"' AND DJLX='700' AND QLLX='4'");
                        if(ycyg_qls!=null&&ycyg_qls.size()>0){
                            for(Rights ycyg_ql:ycyg_qls){
                                List<RightsHolder> zyyg_qlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.XZ, ycyg_ql.getId());
                                if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
                                    for(RightsHolder zyyg_qlr:zyyg_qlrs){
                                        String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+StringHelper.formatObject(zyyg_qlr.getZJH());
                                        if(!list_qlrmc_zjh.contains(str_qlrmc_zjh)){
                                            throw new ManualException(bjlog, "2", "单元已办理商品房预告登记，但商品房预告权利人与转移后权利人不一致", "4001");
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
                                String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+StringHelper.formatObject(zyyg_qlr.getZJH());
                                if(!list_qlrmc_zjh.contains(str_qlrmc_zjh)){
                                    throw new ManualException(bjlog, "2", "单元已办理预告登记，但预告权利人与转移后权利人不一致", "4001");
                                }
                            }
                        }
                    }
                }
            } else {
                throw new ManualException(bjlog, "2", "请填写不动产权证号或不动产单元号", "1005");
            }
        }

    }
}
