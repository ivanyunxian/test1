package com.supermap.wisdombusiness.synchroinline.service;

import com.alibaba.fastjson.JSONObject;
import com.supermap.internetbusiness.util.ManualException;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.YXBZ;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.model.*;
import com.supermap.wisdombusiness.synchroinline.util.FTPUtils;
import com.supermap.wisdombusiness.synchroinline.util.InlineFileItem;
import com.supermap.wisdombusiness.synchroinline.util.SelectorTools;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 * 受理接口 跟作流之前的受理独立开（）
 * 
 * */
@Service
public class AcceptProjectService
{
	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private InsertQLForProdef insertQLForProdef;
	@Autowired
	SmProDef smProDef;
	@Autowired
	private SmStaff smStaff;
	// @Autowired
	// private synchroDao commonDao;
	@Autowired
	private CommonDao dao;
	@Autowired
	private SmProInst smProInst;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private SmProMater smProMater;
	@Autowired
	private SmProMaterService proMaterService;
	@Autowired
	private DYService dyService;
	@Autowired
	private InlineProjectService inlineProjectServicee;
	
	@Autowired
	CommonDaoInline baseCommonDaoInline;
	
	
	private Log log = LogFactory.getLog(AcceptProjectService.class);

	@Transactional
	public JsonMessage accectProject(HttpServletRequest request, String slsqId, SmProInfo info,String isshowbtn,String ispass,String sftg,String shhyj){
		JsonMessage msg = new JsonMessage();
		try {
			if (info == null || slsqId == null || slsqId.isEmpty())
				throw new Exception("创建项目失败。");
			Pro_proinst proinst = baseCommonDaoInline.get(Pro_proinst.class, slsqId);
			/**
			 * 考虑手动按钮与自动匹配成功按钮同时并存的情况
			 */
			SmObjInfo smobj = null;
			if(StringHelper.getDouble(isshowbtn) !=1){//不显示手动按钮
				saveProdefInfo(slsqId, info.getProDef_ID());
				smobj = createInlineProject(info, slsqId, shhyj, request,proinst);
				msg = selectDY(smobj.getDesc(), slsqId, request);
				//插入插入填写的权利信息
				if (msg.getState()) {
					inserQLByProinst(slsqId,smobj,request);
				} else {
					throw new Exception(msg.getMsg());
				}
			}else if(StringHelper.getDouble(isshowbtn) ==1 && ispass.equals("true")){//显示按钮，考虑匹配不成功也点击的情况
				if(sftg.equals("否") || sftg.equals("")){//主要是控制匹配不成功，也使用匹配成功按钮
					msg.setState(false);
					msg.setMsg("单元信息不匹配，请选择手动添加单元功能创建！");
					return msg;
				}
				saveProdefInfo(slsqId, info.getProDef_ID());
				smobj = createInlineProject(info, slsqId, shhyj, request,proinst);
				msg = selectDY(smobj.getDesc(), slsqId, request);
				//插入插入填写的权利信息
				if (msg.getState()) {
					inserQLByProinst(slsqId,smobj,request);
				} else {
					throw new Exception(msg.getMsg());
				}
			}else{
				 saveProdefInfo(slsqId, info.getProDef_ID());
				 smobj = createInlineProject(info, slsqId, shhyj, request,proinst);
			}
			msg.setState(true);
			msg.setData(smobj);
			msg.setMsg("操作成功。");

			//完全成功后再改变状态和回写受理意见
			Pro_slxmsh slxmsh = new Pro_slxmsh();
			slxmsh.setSh_id(UUID.randomUUID().toString().replace("-", ""));
			slxmsh.setSh_xmsl_rq(new Date());
			slxmsh.setSh_xmsl_ry(smStaff.getCurrentWorkStaff().getUserName());
			slxmsh.setSh_xmsl_yj(shhyj);
			slxmsh.setSlxm_id(slsqId);
			slxmsh.setShzt(20);
			baseCommonDaoInline.save(slxmsh);
			// 设置项目受理完成审核状态
			proinst.setShzt(20);
			// 内网数据变化，设置状态告诉外网
			proinst.setTbzt(2);
			baseCommonDaoInline.save(proinst);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setState(false);
			msg.setMsg(e.getMessage());
			throw new RuntimeException(msg.getMsg());
		}
		return msg;
	}

	/**
	 * 通过流程编码获取流程对象，没获取到则返回null
	 * 
	 * @param id
	 * @return
	 */
	public Wfd_Prodef getProdefById(String id)
	{
		return smProDef.GetProdefById(id);
	}

	/**
	 * 保存选择的流程信息
	 * 
	 * @param slsqId
	 *            项目id
	 * @param prodefId
	 *            流程id
	 * @throws Exception
	 */
	public void saveProdefInfo(String slsqId, String prodefId) throws Exception
	{
		if (prodefId == null || prodefId.isEmpty())
			throw new Exception("请选择流程。");
		Wfd_Prodef prodef = smProDef.GetProdefById(prodefId);
		if (prodef == null)
			throw new Exception("选择的流程无效。");
		try
		{
			Pro_proinst proinst = baseCommonDaoInline.get(Pro_proinst.class, slsqId);
			if (proinst == null)
				throw new Exception("项目不存在，无效ID：" + slsqId);
			proinst.setWf_prodefid(prodef.getProdef_Id());
			// 递归获取父节点
			List<Map> parents = dao.getDataListByFullSql("SELECT * FROM bdc_workflow.WFD_PROCLASS START WITH prodefclass_id='" + prodef.getProdefclass_Id() + "' CONNECT BY PRIOR PRODEFCLASS_PID=PRODEFCLASS_ID");
			Collections.reverse(parents);
			StringBuilder names = new StringBuilder();
			for (Map predef : parents)
			{
				if (names.toString().isEmpty())
				{
					names.append(predef.get("PRODEFCLASS_NAME"));
				}
				else
				{
					names.append("/" + predef.get("PRODEFCLASS_NAME"));
				}
			}
			String prodef_name = names.toString() + "/" + prodef.getProdef_Name();
			proinst.setWf_prodefname(prodef_name);
			baseCommonDaoInline.update(proinst);
//			baseCommonDaoInline.flush();
		}
		catch (Exception ex)
		{
			throw new Exception("流程信息保存失败，" + ex.getMessage(), ex);
		}
	}

	public void qjdcAccept(String slsqId, String shyj) throws Exception
	{
		Session session = null;
		Transaction tran = null;
		try
		{
//			session = DButil.newGetMscSession();
			session = baseCommonDaoInline.getOpenSession();
			tran = session.beginTransaction();
			Pro_proinst proinst = (Pro_proinst) session.get(Pro_proinst.class, slsqId);
			if (proinst == null)
				throw new Exception("项目不存在，无效ID：" + slsqId);
			if (proinst.getShzt() != 0)
				throw new Exception("当前项目状态不允许此操作。");
			proinst.setTbzt(2);
			List<Pro_slxmsh> list = session.createQuery("from Pro_slxmsh where slxm_id='" + slsqId + "'").list();
			Pro_slxmsh slxmsh = list.get(0);
			proinst.setShzt(10);
			slxmsh.setSh_qjdc_rq(new Date());
			slxmsh.setSh_qjdc_ry(smStaff.getCurrentWorkStaff().getUserName());
			slxmsh.setSh_qjdc_yj(shyj);
			session.update(slxmsh);
			session.flush();
			tran.commit();
		}
		catch (Exception ex)
		{
			if (tran != null)
			{
				tran.rollback();
			}
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	/**
	 * 创建外网项目
	 * 
	 * @param slsqId
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public SmObjInfo createInlineProject(SmProInfo info, String slsqId, String shyj, HttpServletRequest request, Pro_proinst proinst) throws Exception
	{
		SmObjInfo smobj = null;
		// 从前置机获取受理项目
		try
		{

			if (proinst == null)
				throw new Exception("受理项目不存在，slsqId=" + slsqId);
			if (proinst.getWf_prodefid() == null || proinst.getWf_prodefid().isEmpty())
				throw new Exception("请先选择流程。");
			if (proinst.getLsh() != null && !proinst.getLsh().isEmpty())
				throw new Exception("不能重复创建项目。");
			// 获取权利人信息
			@SuppressWarnings("unchecked")
			List<Pro_proposerinfo> sqrs = baseCommonDaoInline.getDataList(Pro_proposerinfo.class, "PROINST_ID='" + slsqId + "'");
			// 获取资料模板信息
			@SuppressWarnings("unchecked")
			List<Pro_datuminst> datuminsts = baseCommonDaoInline.getDataList(Pro_datuminst.class, "PROINST_ID='" + slsqId + "'");
			// 获取附件资料信息
			@SuppressWarnings("unchecked")
			List<Pro_attachment> attachments = baseCommonDaoInline.getDataList(Pro_attachment.class,"PROINST_ID='" + slsqId + "' ORDER BY FILE_INDEX DESC ");
			smobj = this.acceptProject(info, proinst, sqrs, datuminsts, attachments, request, slsqId);

		}
		catch (Exception ex)
		{
			throw new Exception("创建项目失败，" + ex.getMessage(), ex);
		}
		return smobj;
	}
	
	
	
	public void inserQLByProinst(String slsqId, SmObjInfo smobj,HttpServletRequest request) throws Exception {
		try{
			Pro_proinst proinst = baseCommonDaoInline.get(Pro_proinst.class, slsqId);
			String wf_prodefid = proinst.getProdefcode();
			Wfd_Prodef prodef = smProDef.GetProdefById(wf_prodefid);
			String proinstId = smobj.getID();
			Wfi_ProInst inst = dao.get(Wfi_ProInst.class, proinstId);
			ProjectInfo projectInfo = projectService.getProjectInfo(inst.getFile_Number(), request);
			// 导入申请人信息与权利需要按具体流程进行具体操作
			List<WFD_MAPPING> mps=dao.getDataList(WFD_MAPPING.class, "workflowcode='"+prodef.getProdef_Code()+"'");
			if(mps==null||mps.size()!=1){
				throw new Exception("流程配置错误！");
			}
			// 内网申请人信息
			List<BDCS_SQR> sqrList = dao.getDataList(BDCS_SQR.class, "XMBH='"+projectInfo.getXmbh()+"'");
			// 外网申请人信息
			@SuppressWarnings("unchecked")
			List<Pro_proposerinfo> proposerinfoList = baseCommonDaoInline.getDataList(Pro_proposerinfo.class, "PROINST_ID='" + slsqId + "'");
			String sqrxm = "";
			String zjh = "";
			String sqr_name = "";
			String sqr_zjh = "";
			//内外网申请人信息匹配，重新赋值外网填写的申请人信息
			for (BDCS_SQR sqr : sqrList) {
				sqrxm = sqr.getSQRXM();
				zjh = sqr.getZJH();
				if (!StringHelper.isEmpty(sqrxm) && !StringHelper.isEmpty(zjh)) {
					for (Pro_proposerinfo inline_sqr : proposerinfoList) {
						sqr_name = StringHelper.formatObject(inline_sqr.getSqr_name());
						sqr_zjh = StringHelper.formatObject(inline_sqr.getSqr_zjh());
						String jzh = StringHelper.formatObject(inline_sqr.getSqrjzh());//旧证号，三证合一前的证号
						if (sqrxm.equals(sqr_name) && (zjh.equals(sqr_zjh) || zjh.equals(jzh))) {
							sqr.setZJLX(inline_sqr.getSqr_zjlx());
							sqr.setLXDH(inline_sqr.getSqr_tel());
							sqr.setTXDZ(inline_sqr.getSqr_adress());
							sqr.setGYFS(inline_sqr.getSqr_gyfs());
							sqr.setQLBL(inline_sqr.getSqr_gyfe());
							sqr.setSXH(String.valueOf(inline_sqr.getSqr_sxh()));
							if(zjh.equals(jzh)) {//如果是旧证号匹配成功的，用新证号替换
								sqr.setZJH(sqr_zjh);
							}
							// 证件类型1：身份证，2：港澳台身份证，3：护照，4：户口簿，5：军官证（士兵证）
							// 6：组织机构代码，7：营业执照，99：其它
							// 申请人类型1：个人，2：企业，3：事业单位，4：国家机关，99：其他
							if ("1".equals(inline_sqr.getSqr_zjlx()))
							{
								// 证件类型为身份证的，申请人类型为个人
								sqr.setSQRLX("1");
							}
							else if ("6".equals(inline_sqr.getSqr_zjlx()))
							{
								// 证件类型为组织机构代码的，申请人类型为事业单位
								sqr.setSQRLX("3");
							}
							else if ("7".equals(inline_sqr.getSqr_zjlx()))
							{
								// 证件类型为营业执照的，申请人类型为企业
								sqr.setSQRLX("2");
							}
							// 法人信息
							sqr.setFDDBR(inline_sqr.getSqr_qt_fr_name());
							sqr.setFDDBRDH(inline_sqr.getSqr_qt_fr_tel());
							sqr.setFDDBRZJHM(inline_sqr.getSqr_qt_fr_zjh());
							sqr.setFDDBRZJLX(inline_sqr.getSqr_qt_fr_zjlx());
							sqr.setDLRXM(inline_sqr.getSqr_qt_dlr_name());
							sqr.setDLRZJLX(inline_sqr.getSqr_qt_zjlx());
							sqr.setDLRZJHM(inline_sqr.getSqr_qt_zjh());
							sqr.setDLRLXDH(inline_sqr.getSqr_qt_tel());
							dao.update(sqr);
							dao.flush();
							break;
						}
					}
				}
			}
			String baseWorkflowName=mps.get(0).getWORKFLOWNAME();
			if("ZY001".equals(baseWorkflowName)||"ZY002".equals(baseWorkflowName)||"ZY003".equals(baseWorkflowName)||"ZY005".equals(baseWorkflowName)
					||"ZY009".equals(baseWorkflowName)||"ZY012".equals(baseWorkflowName)||"HZ004".equals(baseWorkflowName)
					||"HZ006".equals(baseWorkflowName)||"HZ008".equals(baseWorkflowName)||"BZ002".equals(baseWorkflowName)
					||"BZ010".equals(baseWorkflowName)||"BZ012".equals(baseWorkflowName)||"CS002".equals(baseWorkflowName)
					||"CS004".equals(baseWorkflowName)||"CS005".equals(baseWorkflowName)||"BG003".equals(baseWorkflowName)
					||"BG004".equals(baseWorkflowName)||"BG014".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForZY002(proinst, projectInfo.getXmbh());
			}else if("CS010".equals(baseWorkflowName)||"CS011".equals(baseWorkflowName)||"CS034".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForCS013(proinst, projectInfo.getXmbh());
			}else if("CS013".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForCS013(proinst, projectInfo.getXmbh());
			}else if("ZX003".equals(baseWorkflowName)||"ZX004".equals(baseWorkflowName)||"ZX006".equals(baseWorkflowName)
					||"ZX009".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForZX004(proinst, projectInfo.getXmbh());
			}else if("BG003".equals(baseWorkflowName)||"BG004".equals(baseWorkflowName)||"BG014".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForBG(proinst, projectInfo.getXmbh());
			}else if("YG002".equals(baseWorkflowName)||"YG102".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForYG002(proinst, projectInfo.getXmbh());
			}else if("YG001".equals(baseWorkflowName)){
				insertQLForProdef.insertQLForYG001(proinst, projectInfo.getXmbh());
			}else if("YG003".equals(baseWorkflowName)) {
				insertQLForProdef.insertQLForYG003(proinst, projectInfo.getXmbh());
			}else if("ZY007".equals(baseWorkflowName) ||"ZY016".equals(baseWorkflowName)) {
				insertQLForProdef.insertQLForZY007(proinst, projectInfo.getXmbh());
			}else if("ZY118".equals(baseWorkflowName) ||"ZY316".equals(baseWorkflowName)) {
				insertQLForProdef.insertQLForZY118(proinst, projectInfo.getXmbh());
			}else if("ZY010".equals(baseWorkflowName)) {
				insertQLForProdef.insertQLForZY010(proinst, projectInfo.getXmbh());
			}else {
				insertQLForProdef.insertQLForNormal(proinst, projectInfo.getXmbh());
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("插入权利信息失败，" + e.getMessage(), e);
		}
	}

	

	/**
	 * 退回项目
	 * 
	 * @param slsqId
	 * @param shyj
	 */
	public void backProject(String slsqId, String shyj) throws Exception
	{
		Session session = null;
		Transaction tran = null;
		try
		{
			session = baseCommonDaoInline.getOpenSession();
			tran = session.beginTransaction();
			Pro_proinst proinst = (Pro_proinst) session.get(Pro_proinst.class, slsqId);
			if (proinst == null)
				throw new Exception("项目不存在，无效ID：" + slsqId);
			if (proinst.getShzt() != 0 && proinst.getShzt() != 10)
				throw new Exception("当前项目状态不允许此操作。");
			proinst.setTbzt(2);
			Pro_slxmsh slxmsh = new Pro_slxmsh();
			slxmsh.setSh_id(UUID.randomUUID().toString().replace("-", ""));
			if (proinst.getShzt() == 0)
			{
				// 权籍调查驳回
				proinst.setShzt(11);
				slxmsh.setSlxm_id(slsqId);
				slxmsh.setSh_qjdc_rq(new Date());
				slxmsh.setSh_qjdc_ry(smStaff.getCurrentWorkStaff().getUserName());
				slxmsh.setSh_qjdc_yj(shyj);
				slxmsh.setShzt(11);
			}
			else if (proinst.getShzt() == 10)
			{
				// 项目受理驳回
				proinst.setShzt(21);
				slxmsh.setSlxm_id(slsqId);
				slxmsh.setSh_xmsl_rq(new Date());
				slxmsh.setSh_xmsl_ry(smStaff.getCurrentWorkStaff().getUserName());
				slxmsh.setSh_xmsl_yj(shyj);
				slxmsh.setShzt(11);
			}
			session.saveOrUpdate(slxmsh);
			tran.commit();
		}
		catch (Exception ex)
		{
			if (tran != null)
			{
				tran.rollback();
			}
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	@Transactional
	public SmObjInfo acceptProject(SmProInfo info, Pro_proinst proinst, List<Pro_proposerinfo> sqrs, List<Pro_datuminst> datuminsts, List<Pro_attachment> attachments, HttpServletRequest request, String slsqId) throws Exception
	{
		String proinstId = null;
		String wf_prodefid = proinst.getWf_prodefid();
		if (wf_prodefid == null || wf_prodefid.isEmpty())
			throw new Exception("请先选择要创建的流程。");
		Wfd_Prodef prodef = smProDef.GetProdefById(wf_prodefid);
		if (prodef == null)
			throw new Exception("选择的流程不存在，请检查。");
		// 受理人员
		SmObjInfo smObjInfo = new SmObjInfo();
		smObjInfo.setID(smStaff.getCurrentWorkStaff().getId());
		smObjInfo.setName(smStaff.getCurrentWorkStaff().getUserName());
		List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
		staffList.add(smObjInfo);
		info.setAcceptor(smObjInfo.getName());
		info.setFromInline(1);
		//地方需求：有的地方用的是申请人或权利人的名称当项目名称
		String sqrmcList = "";
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		//玉林用交易传递过来的项目名称
		if (xzqdm.contains("4509")) {
			String extend_data = proinst.getExtend_data();
			JSONObject data = JSONObject.parseObject(extend_data);
			sqrmcList = StringHelper.formatObject(data.get("xmmc"));
		}
		if (StringHelper.isEmpty(sqrmcList)) {
			for (Pro_proposerinfo sqr : sqrs) {
				String sqrmc = StringHelper.isEmpty(sqr.getSqr_name())?sqr.getSqr_qt_dlr_name():sqr.getSqr_name();
				if(!StringHelper.isEmpty(sqrmc)) {
					if(StringHelper.isEmpty(sqrmcList)) {
						sqrmcList = sqrmc;
					} else {
						sqrmcList += "、"+ sqrmc;
					}
				}
			}
		}
		info.setProjectName(sqrmcList);
		// 创建受理项目
		SmObjInfo returnSmObj = smProInstService.Accept(info, staffList);
		proinstId = returnSmObj.getID();
		SmObjInfo smInfo = returnSmObj.getChildren().get(0);
		Wfi_ProInst inst = dao.get(Wfi_ProInst.class, proinstId);
		if(!StringHelper.isEmpty(proinst.getWf_prodefname())) {
			//登记系统中受理信息，登记类型和流程类型，是取Wfi_ProInst得prodefname，登记类型和流程类型用，隔开
			String wf_prodefname = proinst.getWf_prodefname();
			wf_prodefname = wf_prodefname.replaceFirst("/", ",");
			inst.setProdef_Name(wf_prodefname);
		}
		inst.setWLSH(proinst.getProlsh());
		inst.setSfjslb(proinst.getSfjsbl());
		proinst.setProend(inst.getProinst_End());
		inst.setProject_Name(sqrmcList);
		ProjectInfo projectInfo = projectService.getProjectInfo(inst.getFile_Number(), request);
		
		// 更新项目信息，由于使用事务，当创建bdcs_xmxx记录时，需要从工作流库获取信息，但此时事务未提交导致获取不到信息。
		//String xmmc = info.getProInst_Name() == null || info.getProInst_Name().trim().isEmpty() ? smInfo.getID() : info.getProInst_Name();
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class,projectInfo.getXmbh());
		if (xmxx != null) {
			xmxx.setXMMC(sqrmcList);
			xmxx.setWLSH(proinst.getProlsh());
			xmxx.setSLRY(smObjInfo.getName());
			xmxx.setSLRYID(smObjInfo.getID());
			xmxx.setYWLSH(smInfo.getID());
			dao.save(xmxx);
			dao.flush();
		}

		// 项目编号
		returnSmObj.setDesc(projectInfo.getXmbh());
		// BDCS_XMXX xmxx = createXmxx(inst);
		// xmbh=xmxx.getId();
		// 设置内网生成的流水号
		proinst.setLsh(smInfo.getID());
		// 创建资料模板查询部分，先不实现
		// smProMater.GetWfdProMaterInfo(prodef.getProdef_Id());
		
		
		// 导入申请人信息与权利需要按具体流程进行具体操作
		List<WFD_MAPPING> mps=dao.getDataList(WFD_MAPPING.class, "workflowcode='"+prodef.getProdef_Code()+"'");
		if(mps==null||mps.size()!=1){
			throw new Exception("流程配置错误！");
		}
		String baseWorkflowName=mps.get(0).getWORKFLOWNAME();
		createSqrs(proinst, projectInfo.getXmbh(), sqrs,baseWorkflowName);
		
		// 导入申请材料
		List<Pro_datuminst> nomatchs = new ArrayList<Pro_datuminst>();// 没匹配到的存到这里
		// 返回其他资料模板，如果没有则为空
		Wfi_ProMater other = CreateWfi_ProMaters(inst, datuminsts, attachments, nomatchs);
//		if (!nomatchs.isEmpty())
//		{
//			if (other == null)
//			{
//				// 创建其他这个模板
//				other = smProMater.CreatProMater("其它必要材料", proinstId);
//				dao.save(other);
//				dao.flush();
//			}
//			for (Pro_datuminst_self mulu : nomatchs)
//			{
//				List<Pro_attachment_self> files = getFiles(attachments, mulu);
//				inputFileForGX(inst, other, files);
//			}
//		}
		return returnSmObj;
	}

	

	private BDCS_XMXX createXmxx(Wfi_ProInst inst)
	{
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(inst.getFile_Number());
		BDCS_XMXX xmxx = new BDCS_XMXX();
		xmxx.setSLRY(inst.getAcceptor());
		xmxx.setSLSJ(new Date());
		xmxx.setSFDB(ConstValue.SFDB.NO.Value);
		xmxx.setYXBZ(YXBZ.有效.Value);
		xmxx.setQLLX(flow.getDjlx());
		xmxx.setDJLX(flow.getQllx());
		xmxx.setPROJECT_ID(inst.getFile_Number());
		xmxx.setXMMC(inst.getProject_Name());
		xmxx.setSFHBZS(SF.NO.Value);
		ProjectHelper.copyChargeList(xmxx);
		dao.save(xmxx);
		dao.flush();
		return xmxx;
	}

	public Wfi_ProMater CreateWfi_ProMaters(Wfi_ProInst inst, List<Pro_datuminst> datuminsts, List<Pro_attachment> attachments, List<Pro_datuminst> nomatchs) throws Exception
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
					List<Pro_attachment> files = getFiles(attachments, datuminst);
					inputFileForGX(inst, promater, files);
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
				dao.save(proMaterOther);
				dao.flush();
				List<Pro_attachment> files = getFiles(attachments, mulu);
				inputFileForGX(inst, proMaterOther, files);
			}
		}
		return other;
	}

	/**
	 * 提取单个附件资料
	 * 
	 * @param inst
	 * @param promater
	 * @param files
	 * @throws Exception
	 */
	private void inputFile(Wfi_ProInst inst, Wfi_ProMater promater, List<Pro_attachment> files) throws Exception
	{
		for (Pro_attachment file : files)
		{
			Wfi_MaterData materData = new Wfi_MaterData();
			materData.setMaterialdata_Id(Common.CreatUUID());
			materData.setFile_Name(file.getName());
			materData.setMaterilinst_Id(promater.getMaterilinst_Id());
			if (!StringHelper.isEmpty(file.getFile_Index())) {
				materData.setFile_Index(file.getFile_Index());
			} else {
				materData.setFile_Index(new Date().getTime());
			}
			materData.setUpload_Id(smStaff.getCurrentWorkStaff().getId());
			materData.setUpload_Name(smStaff.getCurrentWorkStaff().getUserName());
			materData.setThumb(null);
			materData.setUpload_Date(new Date());
			String fileName = file.getName() + file.getSuffix();
			// 下载ftp文件流
			ByteArrayOutputStream outputStream = FTPUtils.downloadFile(file.getPath(), fileName);
			FTPUtils.closeFtp();
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
			List<Map> maps = FileUpload.uploadFile(cmf, promater.getMaterial_Id(), inst);
			if (maps != null && maps.size() > 0)
			{
				materData.setFile_Path(maps.get(0).get("filename").toString());
				materData.setPath(maps.get(0).get("filepath").toString());
			}
			promater.setImg_Path(materData.getMaterialdata_Id());
			dao.save(materData);
			dao.flush();
		}
		promater.setMaterial_Count(files.size());
		promater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
		promater.setMaterial_Count(1);
		dao.save(promater);
		dao.flush();
	}

	/**
	 * 提取单个附件资料(广西区厅)
	 * 
	 * @param inst
	 * @param promater
	 * @param files
	 * @throws Exception
	 */
	public void inputFileForGX(Wfi_ProInst inst, Wfi_ProMater promater, List<Pro_attachment> files) throws Exception
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
				materData.setFile_Index(new Date().getTime());
			}
			materData.setUpload_Id(smStaff.getCurrentWorkStaff().getId());
			materData.setUpload_Name(smStaff.getCurrentWorkStaff().getUserName());
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
			dao.save(materData);
			dao.flush();
		}
		promater.setMaterial_Count(files.size());
		promater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
		promater.setMaterial_Count(1);
		dao.save(promater);
		dao.flush();
	}
	
	
	
	/**
	 * 根据内网资料模板项匹配外网资料模板项，匹配不到则返回null
	 * 
	 * @param datuminsts
	 * @param promater
	 * @return
	 */
	private Pro_datuminst getPro_datuminstFromWfi_ProMater(List<Pro_datuminst> datuminsts, Wfi_ProMater promater)
	{
		Pro_datuminst datuminst = null;
		if (datuminsts != null && promater != null)
		{
			for (Pro_datuminst item : datuminsts)
			{
				if (promater.getMaterial_Name() != null && promater.getMaterial_Name().equals(item.getName()))
				{
					datuminst = item;
					break;
				}
			}
		}
		return datuminst;
	}

	/**
	 * 获取指定模板的附件列表
	 * 
	 * @param attachments
	 * @param datuminst
	 * @return
	 */
	public List<Pro_attachment> getFiles(List<Pro_attachment> attachments, Pro_datuminst datuminst)
	{
		List<Pro_attachment> files = new ArrayList<Pro_attachment>();
		for (Pro_attachment file : attachments)
		{
			if (file.getDatuminst_id().equals(datuminst.getId()))
			{
				files.add(file);
			}
		}
		return files;
	}

	/**
	 * 创建申请人信息
	 * 
	 * @param xmbh
	 * @param sqrs
	 */
	public void createSqrs(Pro_proinst proinst, String xmbh, List<Pro_proposerinfo> sqrs,String baseWorkflowName)
	{
		for (Pro_proposerinfo inline_sqr : sqrs){

			if("ZY007,ZY016,ZY118,ZY316".contains(baseWorkflowName)   && inline_sqr.getSqr_lx() == 10) {//转移+预转现 不抽取抵押权人信息
				continue;
			}

			//梧州需求，抵押的，非产权人也要抽取过来
			if ((inline_sqr.getSqr_lx() == 1 && (!StringHelper.isEmpty(inline_sqr.getSfcqr()) && !"0".equals(inline_sqr.getSfcqr()))) )// 只同步权利人
			{
				// 预告登记设立需要同步业务人
				/*if (!"700".equals(proinst.getDjlx()) && !"CS013".equals(baseWorkflowName))
				{
					continue;
				}*/
				if(!("700".equals(proinst.getDjlx()) || "CS013".equals(baseWorkflowName) )
					||("23".equals(proinst.getQllx()) && "700".equals(proinst.getDjlx()) )){
					continue;
				}
			}

			BDCS_SQR sqr = new BDCS_SQR();
			sqr.setCREATETIME(new Date());
			sqr.setXMBH(xmbh);
			// 申请人信息
			sqr.setSQRXM(inline_sqr.getSqr_name());
			sqr.setZJLX(inline_sqr.getSqr_zjlx());
			sqr.setZJH(inline_sqr.getSqr_zjh());
			sqr.setLXDH(inline_sqr.getSqr_tel());
			sqr.setTXDZ(inline_sqr.getSqr_adress());
			sqr.setGYFS(inline_sqr.getSqr_gyfs());
			sqr.setQLBL(inline_sqr.getSqr_gyfe());
			sqr.setSXH(String.valueOf(inline_sqr.getSqr_sxh()));
			//外网中SQRLX: 0对应权利人、1对应义务人、10对应抵押权人
			if (inline_sqr.getSqr_lx() == 0)
			{
				sqr.setSQRLB("1");
			}
			else if (inline_sqr.getSqr_lx() == 1)
			{
				sqr.setSQRLB("2");
			} else {
				sqr.setSQRLB(StringHelper.formatObject(inline_sqr.getSqr_lx()));
			}
			// 证件类型1：身份证，2：港澳台身份证，3：护照，4：户口簿，5：军官证（士兵证）
			// 6：组织机构代码，7：营业执照，99：其它
			// 申请人类型1：个人，2：企业，3：事业单位，4：国家机关，99：其他
			if ("1".equals(inline_sqr.getSqr_zjlx()))
			{
				// 证件类型为身份证的，申请人类型为个人
				sqr.setSQRLX("1");
			}
			else if ("6".equals(inline_sqr.getSqr_zjlx()))
			{
				// 证件类型为组织机构代码的，申请人类型为事业单位
				sqr.setSQRLX("3");
			}
			else if ("7".equals(inline_sqr.getSqr_zjlx()))
			{
				// 证件类型为营业执照的，申请人类型为企业
				sqr.setSQRLX("2");
			}
			// 法人信息
			sqr.setFDDBR(inline_sqr.getSqr_qt_fr_name());
			sqr.setFDDBRDH(inline_sqr.getSqr_qt_fr_tel());
			sqr.setFDDBRZJHM(inline_sqr.getSqr_qt_fr_zjh());
			sqr.setFDDBRZJLX(inline_sqr.getSqr_qt_fr_zjlx());
			sqr.setDLRXM(inline_sqr.getSqr_qt_dlr_name());
			if (!StringHelper.isEmpty(sqr.getDLRXM()))
			{
				sqr.setDLRZJLX(inline_sqr.getSqr_qt_zjlx());
				sqr.setDLRZJHM(inline_sqr.getSqr_qt_zjh());
				sqr.setDLRLXDH(inline_sqr.getSqr_qt_tel());
				//保存代理人头像
				if (!StringHelper.isEmpty(inline_sqr.getSqr_qt_zjh()) && !StringHelper.isEmpty(inline_sqr.getSqr_qt_pic1())) {
					List<BDCS_IDCARD_PIC> pics = dao.getDataList(BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getDLRZJHM() + "'");
					if (pics != null && pics.size() > 0) {
						BDCS_IDCARD_PIC pic = pics.get(0);
						pic.setZJH(inline_sqr.getSqr_qt_zjh());
						pic.setPIC1(inline_sqr.getSqr_qt_pic1());
						pic.setPIC2(inline_sqr.getSqr_qt_pic2());
						dao.update(pic);
					} else {
						BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
						String _id = SuperHelper.GeneratePrimaryKey();
						pic.setId(_id);
						pic.setZJH(inline_sqr.getSqr_qt_zjh());
						pic.setPIC1(inline_sqr.getSqr_qt_pic1());
						pic.setPIC2(inline_sqr.getSqr_qt_pic2());
						dao.save(pic);
					}
				}
			}
			//保存申请人头像
			if (!StringHelper.isEmpty(inline_sqr.getSqr_zjh()) && !StringHelper.isEmpty(inline_sqr.getSqr_pic1())) {
				List<BDCS_IDCARD_PIC> pics = dao.getDataList(BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getZJH() + "'");
				if (pics != null && pics.size() > 0) {
					BDCS_IDCARD_PIC pic = pics.get(0);
					pic.setZJH(inline_sqr.getSqr_zjh());
					pic.setPIC1(inline_sqr.getSqr_pic1());
					pic.setPIC2(inline_sqr.getSqr_pic2());
					dao.update(pic);
				} else {
					BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
					String _id = SuperHelper.GeneratePrimaryKey();
					pic.setId(_id);
					pic.setZJH(inline_sqr.getSqr_zjh());
					pic.setPIC1(inline_sqr.getSqr_pic1());
					pic.setPIC2(inline_sqr.getSqr_pic2());
					dao.save(pic);
				}
			}
			dao.save(sqr);
		}
		dao.flush();
	}

	public Wfi_ActInst addNewActInst(Wfd_Actdef actdef, String remark, Wfi_ProInst proinst)
	{
		Wfi_ActInst actInst = null;
		if (actdef != null)
		{
			actInst = new Wfi_ActInst();
			actInst.setActinst_Id(Common.CreatUUID());
			actInst.setActdef_Id(actdef.getActdef_Id());
			actInst.setActinst_Start(new Date());
			actInst.setProinst_Id(proinst.getProinst_Id());
			actInst.setActinst_Name(actdef.getActdef_Name());
			actInst.setActdef_Type(actdef.getActdef_Type());
			actInst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value + "");
			actInst.setInstance_Type(proinst.getInstance_Type());
			actInst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			actInst.setDept_Code(actdef.getActdef_Dept_Id());// 设置单位ID
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			actInst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal, actdef.getActdef_Time()));
			actInst.setActinst_Msg(remark);
			actInst.setUploadfile(actdef.getUploadfile());
			actInst.setCodeal(actdef.getCodeal());
		}
		return actInst;
	}

	/**
	 * 选择单元
	 * 
	 * @param slsqId
	 *            受理的项目
	 * @param xmbh
	 *            内网创建项目编号
	 * @return
	 */
	public JsonMessage selectDY(String xmbh, String slsqId, HttpServletRequest request) throws Exception {
		JsonMessage msg = new JsonMessage();
		Pro_proinst proinst = (Pro_proinst) baseCommonDaoInline.get(Pro_proinst.class, slsqId);
		Wfd_Prodef prodef = this.getProdefById(proinst.getProdefcode());
		if (prodef == null)
		{
			msg.setState(true);
			msg.setMsg("未获取到流程信息，流程ID：" + proinst.getProdefcode());
			return msg;
		}
		// 1、根据流程获取到对应的选择器
		List<WFD_MAPPING> mappings = this.dao.getDataList(WFD_MAPPING.class, "workflowcode='" + prodef.getProdef_Code() + "'");
		WFD_MAPPING mapping = mappings == null || mappings.isEmpty() ? null : mappings.get(0);
		if (mapping == null)
			throw new Exception("未获取到WFD_MAPPING。");
		// 获取基准流程
		T_BASEWORKFLOW base_wf = this.dao.get(T_BASEWORKFLOW.class, mapping.getWORKFLOWNAME());
		if (base_wf == null)
			throw new Exception("未获取到基准流程，ID=" + mapping.getWORKFLOWNAME());
		String selectorid = base_wf.getSELECTORID();
		if (StringHelper.isEmpty(selectorid)) {
			throw new Exception("选择器不能为空，ID=" + mapping.getWORKFLOWNAME());
		}
		// 2、根据权证获取单元，查询单元地址query/queryindex/{selector}/querydata/
//			String query_dy_url = request.getScheme().toLowerCase() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/app/query/queryindex/" + selector + "/queryindexforrunonce/";
		ResultMessage resultMessage = this.selectDYByBDCDYHOrBDCQZHTY(slsqId, xmbh, selectorid);

		msg.setState(Boolean.parseBoolean(resultMessage.getSuccess()));
		msg.setMsg(resultMessage.getMsg());
		return msg;
	}

	/**
	 * 通过不动产权证号选择单元，如果一个权证号对应多个单元则会选择多个
	 * 
	 * @param xmbh
	 * @param qzh
	 * @param selectorid
	 * @return
	 */
	@Transactional
	public ResultMessage selectDYByQZH(String xmbh, String qzh, String selectorid)
	{
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccess("true");
		try
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("BDCQZH",qzh);

			Message msg = SelectorTools.queryDataForRunOnce(selectorid,map);
			List<Map> list = null;
			if(msg!=null && msg.getRows()!=null){
				list=(List<Map>)msg.getRows();
			}
			String idKey = "";
			T_SELECTOR selector = dao.get(T_SELECTOR.class, selectorid);
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
			if (rows.size() > 0)
			{
				for (int i = 0; i < rows.size(); i++)
				{
					net.sf.json.JSONObject object = (net.sf.json.JSONObject) rows.get(i);
					String id = object.getString(idKey);
					if (ids.length() == 0)
					{
						ids.append(id);
					}
					else
					{
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
		}
		catch (Exception ex)
		{
			resultMessage.setSuccess("false");
			resultMessage.setMsg("添加单元失败，请联系管理员！");
			ex.printStackTrace();
		}
		return resultMessage;
	}
	
	/**
	 * 通过不动单元号选择单元，不考虑单元号相同的情况
	 * @author liangc
	 * @param xmbh
	 * @param bdcdyh
	 * @param selectorid
	 * @return
	 */
	@Transactional
	public ResultMessage selectDYByBDCDYH(String xmbh, String bdcdyh, String selectorid)
	{
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccess("true");
		try
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("BDCDYH",bdcdyh);

			Message msg = SelectorTools.queryDataForRunOnce(selectorid,map);
			List<Map> list = null;
			if(msg!=null && msg.getRows()!=null){
				list=(List<Map>)msg.getRows();
			}

			String idKey = "";
			T_SELECTOR selector = dao.get(T_SELECTOR.class, selectorid);
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
			if (rows.size() > 0)
			{	
				net.sf.json.JSONObject object = (net.sf.json.JSONObject) rows.get(0);
				String id = object.getString(idKey);
				ids.append(id);
			}
			// 3、添加单元
			if (ids.length() > 0) {
				resultMessage = dyService.addBDCDY(xmbh, ids.toString());
			} else {
				resultMessage.setSuccess("false");
				resultMessage.setMsg("未找到对应单元！");
			}
		}
		catch (Exception ex)
		{
			resultMessage.setSuccess("false");
			resultMessage.setMsg("添加单元失败，请联系管理员！");
			ex.printStackTrace();
		}
		return resultMessage;
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
	public ResultMessage selectDYByQZHAndBdcdyh(String xmbh, String qzh,String bdcdyh, String selectorid)
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
			T_SELECTOR selector = dao.get(T_SELECTOR.class, selectorid);
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
			if (rows.size() > 0)
			{
				for (int i = 0; i < rows.size(); i++)
				{
					net.sf.json.JSONObject object = (net.sf.json.JSONObject) rows.get(i);
					String id = object.getString(idKey);
					if (ids.length() == 0)
					{
						ids.append(id);
					}
					else
					{
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
		}
		catch (Exception ex)
		{
			resultMessage.setSuccess("false");
			resultMessage.setMsg("添加单元失败，失败详情："+ex.getMessage());
			ex.printStackTrace();
		}
		return resultMessage;
	}
	
	/**
	 * 封装通用的通过不动产单元号或者权证号来选择的单元的方法
	 * @author liangc
	 * @param slsqId
	 * @param xmbh
	 * @param selectorid
	 */
	@Transactional
	public ResultMessage selectDYByBDCDYHOrBDCQZHTY(String slsqId, String xmbh, String selectorid){
		ResultMessage resultMessage = new ResultMessage();
		List<Map> bdcdyh_bdcqzhs = baseCommonDaoInline.Query("SELECT BDCDYH,BDCQZMH FROM INLINE_INNER.PRO_FWXX WHERE PROINST_ID='"+slsqId+"'");
		if(bdcdyh_bdcqzhs !=null && bdcdyh_bdcqzhs.size()>0){
			List list = new ArrayList();
			for(Map bdcdyh_bdcqzh_:bdcdyh_bdcqzhs){
				if(!StringHelper.isEmpty(bdcdyh_bdcqzh_)) {
					if(!StringHelper.isEmpty(bdcdyh_bdcqzh_.get("BDCQZMH")) || !StringHelper.isEmpty(bdcdyh_bdcqzh_.get("BDCDYH"))){
						String bdcqzh = StringHelper.formatObject(bdcdyh_bdcqzh_.get("BDCQZMH"));
						String bdcdyh = StringHelper.formatObject(bdcdyh_bdcqzh_.get("BDCDYH"));

						String sql = "select distinct bdcdyh from bdck.bdcs_ql_xz where qlid in (select qlid from bdck.bdcs_qlr_xz t where t.bdcqzh='" + bdcqzh + "') ";
						if(!StringHelper.isEmpty(bdcdyh)) {
							sql = sql + " and bdcdyh='"+bdcdyh+"' ";
						}
						if(!StringHelper.isEmpty(bdcqzh)) {
							List<Map> bdcdyhs = dao.getDataListByFullSql(sql);
							if(bdcdyhs.isEmpty()) {
								throw new ManualException("无法从填写的权证/证明号或单元号检索到单元，请联系办理所在地不动产登记中心工作人员排查数据是否异常");
							}
							if(list.contains(bdcdyhs.get(0).get("BDCDYH"))) {
								continue;
							} else {
								list.add(bdcdyhs.get(0).get("BDCDYH"));
							}
						}
						resultMessage = this.selectDYByQZHAndBdcdyh(xmbh, bdcqzh,bdcdyh ,selectorid);
					} /*else if(!StringHelper.isEmpty(bdcdyh_bdcqzh_.get("BDCDYH"))){
						String bdcdyh =bdcdyh_bdcqzh_.get("BDCDYH").toString();
						if(!list.contains(bdcdyh)){
							resultMessage = this.selectDYByBDCDYH(xmbh, bdcdyh, selectorid);
						}
						list.add(bdcdyh);
					}*/
					if ("false".equals(resultMessage.getSuccess())) {
						break;
					}
				}
			}
		}
		return resultMessage;
	}

}
