/**
 * 
 * 代码生成器自动生成[WFI_PROINST]
 * 
 */

package com.supermap.wisdombusiness.workflow.service.wfm;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.supermap.internetbusiness.util.BeresolvException;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.workflow.model.*;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.supermap.realestate.registration.ViewClass.DJFZXX;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.LOG_SENDMSG;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.XMLService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.QueryCriteria;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmQueryPara;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmAbnormal;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmRouteInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import com.supermap.wisdombusiness.workflow.util.SqlFactory;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;

@Service("SmProInstService")
public class SmProInstService {
	@Autowired
	private SqlFactory sqlFactory;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProMater _SmProMater;
	@Autowired
	private SmProInst _SmProInst;
	@Autowired
	private SmActInst _smActInst;
	@Autowired
	private SmRouteInst _smRouteInst;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SmAbnormal smAbnormal;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private SmProMaterService ProMaterService;
	@Autowired
	private ProjectServiceImpl projectServiceImpl;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private ZSService zsService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private XMLService xmlService;
	@Autowired
    private SmProDef smProdef;
	@Autowired
    private proinstStateModify proinstStateModify;

	@Transactional
	public SmObjInfo Accept(SmProInfo proinfo) {
		SmObjInfo smInfo = new SmObjInfo();
		SmObjInfo objinfo = new SmObjInfo();
		User user = smStaff.getCurrentWorkStaff();
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if(user!=null){
			objinfo.setID(user.getId());
			objinfo.setName(user.getUserName());
			list.add(objinfo);
		}else{
			smInfo.setDesc("");
		}
		proinfo.setAcceptor(user.getUserName());
		proinfo.setDistrict_ID(user.getAreaCode());
		try{
			smInfo=Accept(proinfo, list);
		}catch(Exception e){
		}
		return smInfo;
		
	}

	/**
	 * 受理项目
	 * 
	 * @param proinfo
	 *            SmProInfo 实例相关信息
	 * @return SmProInfo 返回加工后的实例信息
	 * */
	@Transactional
	public SmObjInfo Accept(SmProInfo proinfo, List<SmObjInfo> smObjInfo) {
		SmObjInfo smInfo = new SmObjInfo();
		if (proinfo.getProInst_ID() != null && proinfo.getProInst_ID() != "") {
			return UpdateProject(proinfo, smObjInfo);
		} else {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("prodef_id='");
			sBuilder.append(proinfo.getProDef_ID());
			sBuilder.append("'");
			sBuilder.append(" and actdef_type='");
			sBuilder.append(WFConst.ActDef_Type.ProcessStart.value);
			sBuilder.append("'");
			List<Wfd_Actdef> actdefs = commonDao.findList(Wfd_Actdef.class, sBuilder.toString());
			if (actdefs != null && actdefs.size() > 0) {
				Wfd_Prodef wfd_prodef = commonDao.get(Wfd_Prodef.class, proinfo.getProDef_ID());
				if("1".equals(StringHelper.formatObject(wfd_prodef.getHouse_Status()))){
					Wfi_ProInst proInst = _SmProInst.CreatProInst(proinfo,wfd_prodef.getHouse_Status());
					if (proInst != null) {
						Wfi_ActInst actInst = _smActInst.AddNewActInst(actdefs.get(0), smObjInfo, "受理项目备注", proInst,wfd_prodef.getHouse_Status(), true);
						if (actInst != null) {
							Wfi_NowActInst nowActInst = _smActInst.CreatNowActInst(actInst);
							//更新环节实例中当前环节的信息：
							proInst.setActdef_Type(actInst.getActdef_Type());
							proInst.setActinst_Id(actInst.getActinst_Id());
							proInst.setIsApplyHangup(actInst.getIsApplyHandup());
							proInst.setStatusext(actInst.getStatusExt());
							proInst.setOperation_Type_Nact(actInst.getOperation_Type());
							proInst.setStaff_Id_Nact(actInst.getStaff_Id());
							proInst.setActinst_Status(actInst.getActinst_Status());
							GregorianCalendar calendar = new GregorianCalendar();
							calendar.setTime(actInst.getActinst_Start());
							if(null==proInst.getProinst_Time()){
								Wfd_Prodef def = smProdef.GetProdefById(proInst.getProdef_Id());
								proInst.setProinst_Time(null!=def.getProdef_Time()?def.getProdef_Time():0);
							}
							//proInst.setProinst_Willfinish(smHoliday.addDateByWorkDay(calendar, proInst.getProinst_Time()));
							proInst.setActinst_Willfinish(actInst.getActinst_Willfinish());
							proInst.setActinst_Name(actInst.getActinst_Name());
							proInst.setMsg(actInst.getMsg());
							proInst.setCodeal(actInst.getCodeal());
							proInst.setACTINST_START(actInst.getActinst_Start());
							proInst.setACTINST_END(actInst.getActinst_End());
							proInst.setStaff_Name_Nact(actInst.getStaff_Name());
							if(StringHelper.isEmpty(proInst.getStaff_Id())){
								proInst.setAreaCode(userService.findById(proinfo.getStaffID()).getAreaCode());
							}else{
								proInst.setAreaCode(userService.findById(proInst.getStaff_Id()).getAreaCode());
							}
							
							if(proinfo.getAreaCode()!=null&&!proinfo.getAreaCode().isEmpty()){
								proInst.setAreaCode(proinfo.getAreaCode());
							}
							Wfi_ActInstStaff actinstroute = _smActInst.CreatActStaff(actInst.getActinst_Id(), smObjInfo.get(0), "");
							if (nowActInst != null) {
								commonDao.save(proInst);
								commonDao.save(actInst);
								commonDao.save(nowActInst);
								commonDao.save(actinstroute);
								smInfo.setID(proInst.getProinst_Id());
								smInfo.setDesc("受理成功");
								smInfo.setName(actInst.getActinst_Id());
								smInfo.setFile_number(proInst.getFile_Number());
								//SmObjInfo字段不太够用，先存到children里边吧
								List<SmObjInfo> infoList = new ArrayList<SmObjInfo>();
								SmObjInfo _info = new SmObjInfo();
								_info.setID(proInst.getProlsh());
								_info.setName(proInst.getProject_Name());
								infoList.add(_info);
								smInfo.setChildren(infoList);
							}
						}
					}
					if (proInst != null) {
						proinfo.setProInst_ID(proInst.getProinst_Id());
						// 导入收件资料模版
						//如果受理的时候前台传入actinstid，将actinst对应的proinst的资料文件夹复制一份给新受理项目
						if(!StringHelper.isEmpty(proinfo.getActInst_ID())){
							CloneMtrs(proinfo.getActInst_ID(), proInst.getProinst_Id());
						}else{
							AddNewMtrs(proInst.getProdef_Id(), proInst.getProinst_Id());
						}
					}

					try {
						//需要网签及税务确认的流程，添加时间记录
						List<WFD_MAPPING> mps = commonDao.findList(WFD_MAPPING.class, "workflowcode='" + wfd_prodef.getProdef_Code() + "'");
						if(!mps.isEmpty()) {
							String baseWorkflowName = mps.get(0).getWORKFLOWNAME();
							String wcodes= ConfigHelper.getNameByValue("TAXSET-WORKFLOWCODE");
							if(wcodes!=null && baseWorkflowName!=null && wcodes.contains(baseWorkflowName)) {
								List<Wfi_ProinstDate> proinstDates = commonDao.findList(Wfi_ProinstDate.class, MessageFormat.format("FILE_NUMBER=''{0}''", proInst.getFile_Number()));
								Wfi_ProinstDate proinstDate = null;
								if(proinstDates.size()>0) {
									proinstDate = proinstDates.get(0);
								} else {
									proinstDate = new Wfi_ProinstDate();
									proinstDate.setId(StringUtil.getUUID());
									proinstDate.setFile_number(proInst.getFile_Number());
									proinstDate.setActinst_start_time(new Date());
								}
								commonDao.saveOrUpdate(proinstDate);
							}
						}
					} catch(Exception e){
						e.printStackTrace();
					}

				}else{
					Wfi_ProInst proInst = _SmProInst.CreatProInst(proinfo);
					if (proInst != null) {
						Wfi_ActInst actInst = new Wfi_ActInst();
						if ("3".equals(proinfo.getYWLY())) {
							actInst = _smActInst.AddNewActInst(actdefs.get(0), smObjInfo, "受理项目备注", proInst,3, true);
						} else {
							actInst = _smActInst.AddNewActInst(actdefs.get(0), smObjInfo, "受理项目备注", proInst,wfd_prodef.getHouse_Status());
						}
						if (actInst != null) {
							Wfi_NowActInst nowActInst = _smActInst.CreatNowActInst(actInst);
							//更新环节实例中当前环节的信息：
							proInst.setActdef_Type(actInst.getActdef_Type());
							proInst.setActinst_Id(actInst.getActinst_Id());
							proInst.setIsApplyHangup(actInst.getIsApplyHandup());
							proInst.setStatusext(actInst.getStatusExt());
							proInst.setOperation_Type_Nact(actInst.getOperation_Type());
							proInst.setStaff_Id_Nact(actInst.getStaff_Id());
							proInst.setActinst_Status(actInst.getActinst_Status());
							GregorianCalendar calendar = new GregorianCalendar();
							calendar.setTime(actInst.getActinst_Start());
							if(null==proInst.getProinst_Time()){
								Wfd_Prodef def = smProdef.GetProdefById(proInst.getProdef_Id());
								proInst.setProinst_Time(null!=def.getProdef_Time()?def.getProdef_Time():0);
							}
							//proInst.setProinst_Willfinish(smHoliday.addDateByWorkDay(calendar, proInst.getProinst_Time()));
							proInst.setActinst_Willfinish(actInst.getActinst_Willfinish());
							proInst.setActinst_Name(actInst.getActinst_Name());
							proInst.setMsg(actInst.getMsg());
							proInst.setCodeal(actInst.getCodeal());
							proInst.setACTINST_START(actInst.getActinst_Start());
							proInst.setACTINST_END(actInst.getActinst_End());
							proInst.setStaff_Name_Nact(actInst.getStaff_Name());
							if(StringHelper.isEmpty(proInst.getStaff_Id())){
								proInst.setAreaCode(userService.findById(proinfo.getStaffID()).getAreaCode());
							}else{
								proInst.setAreaCode(userService.findById(proInst.getStaff_Id()).getAreaCode());
							}
							
							if(proinfo.getAreaCode()!=null&&!proinfo.getAreaCode().isEmpty()){
								proInst.setAreaCode(proinfo.getAreaCode());
							}
							Wfi_ActInstStaff actinstroute = _smActInst.CreatActStaff(actInst.getActinst_Id(), smObjInfo.get(0), "");
							if (nowActInst != null) {
								commonDao.save(proInst);
								commonDao.save(actInst);
								commonDao.save(nowActInst);
								commonDao.save(actinstroute);
								smInfo.setID(proInst.getProinst_Id());
								smInfo.setDesc("受理成功");
								smInfo.setName(actInst.getActinst_Id());
								smInfo.setFile_number(proInst.getFile_Number());
								//SmObjInfo字段不太够用，先存到children里边吧
								List<SmObjInfo> infoList = new ArrayList<SmObjInfo>();
								SmObjInfo _info = new SmObjInfo();
								_info.setID(proInst.getProlsh());
								_info.setName(proInst.getProject_Name());
								infoList.add(_info);
								smInfo.setChildren(infoList);
							}
						}
					}
					if (proInst != null) {
						proinfo.setProInst_ID(proInst.getProinst_Id());
						// 导入收件资料模版
						//如果受理的时候前台传入actinstid，将actinst对应的proinst的资料文件夹复制一份给新受理项目
						if(!StringHelper.isEmpty(proinfo.getActInst_ID())){
							CloneMtrs(proinfo.getActInst_ID(), proInst.getProinst_Id());
						}else{
							AddNewMtrs(proInst.getProdef_Id(), proInst.getProinst_Id());
						}
					}
				
				}
			}


		}
		commonDao.flush();
		return smInfo;
	}

	@Transactional
	public SmObjInfo UpdateProject(SmProInfo proinfo, List<SmObjInfo> smObjInfo) {
		SmObjInfo smInfo = new SmObjInfo();
		String proinstidString = proinfo.getProInst_ID();
		Wfi_ProInst wfi_ProInst = commonDao.get(Wfi_ProInst.class, proinstidString);
		if (wfi_ProInst != null) {
			wfi_ProInst.setProject_Name(proinfo.getProInst_Name());
			commonDao.update(wfi_ProInst);
			commonDao.flush();
			smInfo.setID(proinstidString);
			smInfo.setDesc("修改成功");
			smInfo.setName("修改项目");
		}
		return smInfo;

	}

	public List<?> AddNewMtrs(String ProDefID, String ProInstID) {
		return _SmProMater.CreateProMaterInst(ProDefID, ProInstID, null);
	}
	
	public List<?> CloneMtrs(String ActinstID, String ProInstID) {
		return _SmProMater.CloneProMaterInst(ActinstID, ProInstID);
	}
	
	public List<?> GetMtrsByProInstID(String ProInstID) {
		return _SmProMater.GetProMaterInfo(ProInstID);
	}

	public void GetMtr(String MtrID) {
	}

	public void DelMtr(String MtrID) {

	}

	public void DoneProInst(String ProInstID) {

	}

	public void SetLogOut() {

	}

	public void CanLogOut() {

	}

	public void CreateForms(String ProDefID, String ProInstID) {

	}

	public List<SmProInfo> GetProjectListJson(SmQueryPara param, String listType, String staff_id, int CurrentPageIndex, int PageSize, int ASC) {
		List<Wfi_ActInst> list = commonDao.getDataList(Wfi_ActInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ActInst where staff_ids like '%" + staff_id + "%'");
		List<SmProInfo> listProInfos = new ArrayList<SmProInfo>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (list != null && list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {
				SmProInfo proInfo = new SmProInfo();
				Wfi_ActInst lActInst = list.get(i);
				if (lActInst != null) {
					Wfi_ProInst pinfo = commonDao.load(Wfi_ProInst.class, lActInst.getProinst_Id());
					if (pinfo != null) {
						// 项目名称
						proInfo.setProInst_Name(pinfo.getProject_Name());
						// 紧急程度
						proInfo.setFile_Urgency(pinfo.getUrgency() + "");
						// 流程名称
						proInfo.setProDef_Name(pinfo.getProdef_Name());

					}
					// 活动名称
					proInfo.setActDef_Name(lActInst.getActinst_Name());
					// 剩余时间
					proInfo.setFinishDate(formatter.format(lActInst.getActinst_End()));
					// 案卷状态
					proInfo.setStatus(lActInst.getActinst_Status() + "");
					// 收件时间
					proInfo.setStartTime(formatter.format(lActInst.getActinst_Start()));
					proInfo.setActInst_ID(lActInst.getActinst_Id());
					listProInfos.add(proInfo);
				}
			}
		}
		return listProInfos;
	}

	@SuppressWarnings("unchecked")
	public List<SmProInfo> GetProjectListJsonBySearch(SmQueryPara param, int listType, String staff_id, int CurrentPageIndex, int PageSize, Message msg) {
		String noWhereSql = _SmProInst.GetProInstListNoWhereSql(param, staff_id, listType);
		Page _page = commonDao.GetPagedData(vProInstInfo.class, Common.WORKFLOWDB + "V_PROINSTINFO", CurrentPageIndex, PageSize, noWhereSql);
		List<vProInstInfo> list = (List<vProInstInfo>) _page.getResult();
		List<SmProInfo> listProInfos = new ArrayList<SmProInfo>();
		for (int i = 0; i < list.size(); i++) {
			vProInstInfo _vinfo = list.get(i);
			SmProInfo proInfo = new SmProInfo();
			_SmProInst.ProInfoFromView(proInfo, _vinfo);
			listProInfos.add(proInfo);
		}
		msg.setTotal(_page.getTotalCount());
		msg.setRows(listProInfos);
		return listProInfos;
	}

	/**
	 * 获取项目列表
	 * 
	 * @param staff_Id
	 * @param actInst_Status
	 * @param sarchString
	 * @param CurrentPageIndex
	 * @param pageSize
	 * @return
	 */
	public Message GetProjectList(String staff_Id, int actInst_Status, String sarchString, int CurrentPageIndex, int pageSize, String pronames, String sqr, String startlsh, String endlsh, String actinst_name,
			String starttime,String endtime,String handlepsersons,String roleid,String descData,String ywh,HttpServletRequest request,String sfjsbl) {
		return GetProjectList(staff_Id, actInst_Status, 
				sarchString, CurrentPageIndex, pageSize, 
				pronames, sqr, startlsh, endlsh, actinst_name, " actinst_start asc",
				starttime, endtime, handlepsersons, roleid,descData,ywh,request,sfjsbl);
	}

	/**
	 * 获取查询项目雷彪通用查询sql
	 * 
	 * @author JHX
	 * */
	public Map<String, String> getHandingProjectListSQL(String staff_Id, int actInst_Status, String sarchString,
														int CurrentPageIndex, int pageSize, String pronames, String sqr,
														String startlsh, String endlsh, String actinst_name, String order, String starttime, String endtime, String handlepsersons, String roleid, String ywh, String prolshs, String outTimeStatus, String sfjsbl, HttpServletRequest request) {
		
			return sqlFactory.getHandingProjectListSql(
					staff_Id, actInst_Status, sarchString, 
					CurrentPageIndex, pageSize, pronames, sqr, 
					startlsh, endlsh, actinst_name, order,
					 starttime, endtime, handlepsersons, roleid ,ywh,prolshs,outTimeStatus,sfjsbl,request
					);
	}

	public Message GetProjectList(String staff_Id, int actInst_Status, 
			String sarchString, int CurrentPageIndex, int pageSize, 
			String pronames, String sqr, String startlsh, 
			String endlsh, String actinst_name, String order,
			String starttime,String endtime,String handlepsersons,String roleid,String descData,String ywh,HttpServletRequest request,String sfjsbl) {
			String prolshs="";
			String outTimeStatus= request.getParameter("prostatus");
			if(request!=null){
				//TODO:获取权利人、义务人、证件号、权证号、坐落信息
				String qlr = request.getParameter("qlr");
				String ywr = request.getParameter("ywr");
				String zjh = request.getParameter("zjh");
				String qzh = request.getParameter("qzh");
				String zl = request.getParameter("zl");
				String PreciseQueryQLR = request.getParameter("isvaguequery");
				/*
				*查询条件为中文时，出现乱码
				*/
				try {
					if(qlr != null && !qlr.trim().isEmpty()) {
						qlr = new String(qlr.getBytes("iso-8859-1"), "utf-8");
					}
					
					if(ywr != null && !ywr.trim().isEmpty()) {
						ywr = new String(ywr.getBytes("iso-8859-1"), "utf-8");
					}
					
					if(zjh != null && !zjh.trim().isEmpty()) {
						zjh = new String(zjh.getBytes("iso-8859-1"), "utf-8");
					}
					
					if(qzh != null && !qzh.trim().isEmpty()) {
						qzh = new String(qzh.getBytes("iso-8859-1"), "utf-8");
					}
					
					if(zl != null && !zl.trim().isEmpty()) {
						zl = new String(zl.getBytes("iso-8859-1"), "utf-8");
					}
					
					if(PreciseQueryQLR != null && !PreciseQueryQLR.trim().isEmpty()) {
						PreciseQueryQLR = new String(PreciseQueryQLR.getBytes("iso-8859-1"), "utf-8");
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				boolean isVagueQuery = false;
				if(!StringHelper.isEmpty(PreciseQueryQLR)){
					isVagueQuery =Boolean.parseBoolean(PreciseQueryQLR);
				}
				boolean flag = false;
				if(!StringHelper.isEmpty(qlr)
						||!StringHelper.isEmpty(ywr)
						||!StringHelper.isEmpty(zjh)
						||!StringHelper.isEmpty(qzh)
						||!StringHelper.isEmpty(zl)){
				 flag=true;	
				 prolshs=projectService.getYwlshByCondtionsEx(qlr, ywr, zjh, qzh, zl,isVagueQuery);
				}
				if(!StringHelper.isEmpty(prolshs)){
					String replaceend = prolshs.replaceAll(",", "','");
					prolshs = "'"+replaceend+"'";
				}
				if(flag&&prolshs.length()==0){
					return new Message();
				}
			}
		Map<String, String> SQLmap = getHandingProjectListSQL(staff_Id, actInst_Status, sarchString, CurrentPageIndex, pageSize, pronames, 
				sqr, startlsh, endlsh, actinst_name, order,starttime, endtime, handlepsersons,roleid,ywh,prolshs,outTimeStatus,sfjsbl,request);
		Message msg = new Message();
		long tatalCount = commonDao.getCountByFullSql(SQLmap.get("COUNT"));
		msg.setTotal(tatalCount);
		if (tatalCount > 0) {
			// str.append(" order by ACTINST_START desc ");
			List<Map> list;
			if (actInst_Status == 3) {
				list = commonDao.getDataListByFullSql(SQLmap.get("CONTENT") + " order by  actinst_end desc," + order, CurrentPageIndex, pageSize);
			} else {
				if(descData!=null&&!descData.equals("")){
					list = commonDao.getDataListByFullSql(SQLmap.get("CONTENT") + " order by  " +descData, CurrentPageIndex, pageSize);
				}else{
					list = commonDao.getDataListByFullSql(SQLmap.get("CONTENT") + " order by  proinst_weight desc," + order, CurrentPageIndex, pageSize);
				}

			}
			
			
			
			
			
			// 计算剩余时间
			for (Map m : list) {
				Object actinst= m.get("ACTINST_ID");
				Date start = new Date();
				//计算锁定剩余时间
				Date lockend = (Date)m.get("UNLOCKDATE");
				if(null!=lockend&&new Date().before(lockend)){
					long restTime =  smHoliday.ComputerDiff(start,lockend);
					GregorianCalendar cal3 = new GregorianCalendar();
					cal3.setTime(start);
					if (-2147483648 < restTime && restTime < 2147483647) {
						cal3.add(Calendar.MILLISECOND, (int) restTime);
					} else {
						cal3.add(Calendar.MINUTE, (int) (restTime / (1000 * 60)));
					}
					m.put("UNLOCKDATE", cal3.getTime());
				}else if(null!=lockend){
					Object operation_Type= m.get("OPERATION_TYPE");
						
					if(null!=operation_Type&&(WFConst.Operate_Type.HaveLock.value+"").equals(operation_Type)){
						String actinstid = actinst.toString();
						operationService.cancleLock(actinstid);
						Wfi_ActInst inst= _smActInst.GetActInst(actinstid);
						Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
						m.put("OPERATION_TYPE","1");
						m.put("PROINST_WILLFINISH", proinst.getProinst_Willfinish());
						m.put("ACTINST_WILLFINISH", inst.getActinst_Willfinish());
					}
				}
				
				
				if(actinst!=null){
					Wfd_Actdef actdef=_smActInst.GetActDef(actinst.toString());
					if(actdef!=null){
						Double time=actdef.getActdef_Time();
						if(time!=null&&time==0){
							m.put("OUTTIME", "uncertainties");
							continue;
						}
					}

				}
				Date proend = (Date) m.get("PROINST_WILLFINISH");
				Date actend = (Date) m.get("ACTINST_WILLFINISH");
				if (actend == null) {
					actend = new Date();
				}
				
				String prodef = m.get("PRODEF_ID").toString();
				String proinst = m.get("PROINST_ID").toString();
				Wfd_Prodef def = commonDao.get(Wfd_Prodef.class, prodef);
				Object proinst_time = m.get("PROINST_TIME");
				if (proend == null || (def.getProdef_Time()!=null&&!def.getProdef_Time().equals(proinst_time))) {
					Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proinst);
					// 计算时间并插入
					if (def != null) {
						GregorianCalendar calother = new GregorianCalendar();
						calother.setTime(inst.getProinst_Start());
						if (def.getProdef_Time() != null) {
							if (inst.getProinst_Willfinish() == null) {
								inst.setProinst_Willfinish(smHoliday.addDateByWorkDay(calother, def.getProdef_Time()));
								inst.setProinst_Time(def.getProdef_Time());
								commonDao.update(inst);
								commonDao.flush();
							}
							proend = inst.getProinst_Willfinish();
						} else {
							proend = new Date();
						}
					}
				}
				//TODO:计算OUTTIME 和 PROOUTTIME
				Date currentDate = new Date();
				long ss=currentDate.getTime()-actend.getTime();
				long pro = currentDate.getTime()-proend.getTime();
				int OUTTIME =(int)Math.ceil(ss/(24*60*60*60));
				int PROOUTTIME =(int)Math.ceil(pro/60);
				m.put("OUTTIME", OUTTIME);
				m.put("PROOUTTIME", PROOUTTIME);
				
				long mscond = smHoliday.ComputerDiff(start, proend);
				GregorianCalendar cal = new GregorianCalendar();
				GregorianCalendar cal2 = new GregorianCalendar();
				cal.setTime(start);
				if (-2147483648 < mscond && mscond < 2147483647) {
					cal.add(Calendar.MILLISECOND, (int) mscond);
				} else {
					cal.add(Calendar.MINUTE, (int) (mscond / (1000 * 60)));
				}
				m.put("PROINST_WILLFINISH", cal.getTime());
				// smHoliday.addDateByWorkDay(cal,acttime)
				long mscond2 = smHoliday.ComputerDiff(start, actend);
				cal2.setTime(start);
				if (-2147483648 < mscond && mscond < 2147483647) {
					cal2.add(Calendar.MILLISECOND, (int) mscond2);
				} else {
					cal2.add(Calendar.MINUTE, (int) (mscond2 / (1000 * 60)));
				}
				m.put("ACTINST_WILLFINISH", cal2.getTime());
				m.put("SERVERTIME", new Date());
				/*应广西市县的需求，查询某项目是否打印过证书，并查询打印的次数heks*/
				String project_id = StringHelper.formatObject(m.get("FILE_NUMBER"));
				if(!StringHelper.isEmpty(project_id)){
					String fromSql = " FROM BDCK.BDCS_PRINTRECORD WHERE PRINTTYPE='SZDJ"+"' AND PROJECT_ID like'" + project_id+ "%'";
					long count = commonDao.getCountByFullSql(fromSql);
					m.put("SZDJ_COUNT", count);
				}
				/*查询受理单打印次数*/
				if(!StringHelper.isEmpty(project_id)){
					String fromSql = " FROM BDCK.BDCS_PRINTRECORD WHERE PRINTTYPE='SLD"+"' AND PROJECT_ID like'" + project_id+ "%'";
					long count = commonDao.getCountByFullSql(fromSql);
					m.put("SLD_COUNT", count);
				}
			}
			msg.setRows(list);
		}
		return msg;
	}

	/* 默认排序为降序 */
	public Message getAllProjectList(QueryCriteria query){
			//String key, String status, String start, String end, int CurrentPageIndex, int pageSize, String actdef_name, String prodefname, String staff, String sqr, String urgency, String outtime, String passback, String proStatus,HttpServletRequest request) {
		//TODO:获取权利人、义务人、证件号、权证号、坐落信息
		String qlr = query.getQlr();
		String ywr = query.getYwr();
		String zjh = query.getZjh();
		String qzh = query.getQzh();
		String zl = query.getZl();
		String PreciseQueryQLR = query.getIsVagueQuery();
		String start=query.getActinstStart();
		String end=query.getActinstEnd();
		String staff=query.getStaff();
		String actdef_name=query.getActdefName();
		String deadline_s = query.getDeadline_s();
		String deadline_e = query.getDeadline_e();
		boolean isVagueQuery = false;
		if(!StringHelper.isEmpty(PreciseQueryQLR)){
			isVagueQuery =Boolean.parseBoolean(PreciseQueryQLR);
		}
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
			String res=getlogmsg( query.getValue(),qlr,ywr,zjh,qzh,zl,query.getYwh(),PreciseQueryQLR,"",  query.getStatus(),  start,  end,  actdef_name, query.getProdefName(),  staff,  query.getSqr(),  query.getUrgency(),  query.getOutTime(),  query.getPassBack(),  query.getProStatus());
			YwLogUtil.addYwLog("全局查询功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		}
		String prolshs="";
		boolean flag = false;
		if(!StringHelper.isEmpty(qlr)
				||!StringHelper.isEmpty(ywr)
				||!StringHelper.isEmpty(zjh)
				||!StringHelper.isEmpty(qzh)
				||!StringHelper.isEmpty(zl)){
		 flag=true;	
		 prolshs=projectService.getYwlshByCondtionsEx(qlr, ywr, zjh, qzh, zl,isVagueQuery);
		}
		if(!StringHelper.isEmpty(prolshs)){
			String replaceend = "";
			String[] arrs = prolshs.split(",");
			if(arrs.length>50){
				prolshs = "";
				for(int i=0;i<50;i++){
					if(i==49){
						prolshs+="'"+arrs[i]+"'";
					}else{
						prolshs+="'"+arrs[i]+"',";
					}
				}
			}else{
				replaceend = prolshs.replaceAll(",", "','");
				prolshs = "'"+replaceend+"'";
			}
		}
		if(flag&&prolshs.length()==0){
			return new Message();
		}else {
			//如果查询条件包含活动类型，办理时间，办理人员，则采用视图查询
			if(start!=null&&!start.isEmpty()||
					end!=null&&!end.isEmpty()||
					deadline_s!=null&&!deadline_s.isEmpty()||
					deadline_e!=null&&!deadline_e.isEmpty()||
					staff!=null&&!staff.isEmpty()||
					actdef_name!=null&&!actdef_name.isEmpty()){
				return getAllProjectListByView(query,prolshs,"DESC");
			}
			return getAllProjectList(query,prolshs,"DESC");
		}
		
	}

	private boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 项目全局查询
	 * */
	public Message getAllProjectList(QueryCriteria query,String prolshs,String order) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		String key =query.getValue();
		String areaCode=query.getAreaCode();
		String ywh=query.getYwh();
		String lshstart=query.getLshStart();
		String lshend=query.getLshEnd();
		String status=query.getStatus();
		String prodefname=query.getProdefName();
		String urgency=query.getUrgency();
		String outtime=query.getOutTime();
		String proStatus=query.getProStatus();
		String passback=query.getPassBack();
		String sqr="";
		if(!StringHelper.isEmpty(query.getSqr())){
			 sqr=query.getSqr().replace(" ", "");
		}
		
		//String deadline = query.getDeadline();
		String departmt = query.getDepartmt();
		
		int CurrentPageIndex=Integer.parseInt(query.getCurrentPageIndex());
		int pageSize=Integer.parseInt(query.getPageSize());
		str.append("1>0 ");
		
		String regex = "^[s,S][0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(key);
		boolean regexResult=matcher.find();
		System.out.println("是否符合正则表达式判断："+regexResult);
		
		if (key != null && !"".equals(key)) {
			if(regexResult) {
				str.append(" and ( WLSH like '%");
				str.append(key);
				str.append("%') ");
			}else {
				str.append(" and (PROJECT_NAME like '%");
				str.append(key);
	/*			str.append("%' or FILE_NUMBER like '%");
				str.append(key);*/
				str.append("%' ");
				str.append(" or PROLSH like '%");
				str.append(key);
				str.append("%' ");
				str.append(" or BATCH like '%");
				str.append(key);
				str.append("%'");
				str.append(" or WLSH like '%");
				str.append(key);
				str.append("%'");
				str.append(")");
				// 支持权利人查询
			}

		}
		if(areaCode!=null&&!areaCode.isEmpty()){
			str.append(" and AREACODE = '"+areaCode+"' ");
		}
		if(!StringHelper.isEmpty(prolshs)){
			str.append(" and PROLSH in ("+prolshs+")");
		}
		
		if(!StringHelper.isEmpty(departmt)){
			
			str.append(" and staff_id in (select u.id from smwb_framework.t_user u left join smwb_framework.t_department dept on u.departmentid=dept.id where dept.departmentname='"+departmt+"')");
		}
		
		if(!StringHelper.isEmpty(ywh)){
			str.append(" and YWH like '%"+ywh+"%' ");
		}
		if(lshstart != null && !lshstart.equals("")){
			str.append(" and PROLSH>='"+lshstart+"' ");
		}
		if(lshend != null && !lshend.equals("")){
			str.append(" and PROLSH<='"+lshend+"' ");
		}
		if (status != null && !"".equals(status)) {
			str.append(" and ACTINST_STATUS in (");
			str.append(status);
			str.append(")");
		}
		// 流程名称
		if (prodefname != null && !"".equals(prodefname)) {
			str.append(" and prodef_name like'");
			str.append(prodefname);
			str.append("%'");
		}
		if (urgency != null && !urgency.equals("")) {
			str.append(" and urgency=");
			str.append(urgency);
		}
		if (outtime != null && !outtime.equals("")) {
			str.append(" and proouttime>0");
		}
		
/*		if(deadline != null&&!"".equals(deadline)){
			str.append(" and proinst_willfinish=");
			str.append(deadline);
		}*/
			
		if(proStatus!=null&&!proStatus.equals("")){
			str.append(" and proinst_status  not in('0') ");
			Date nowDate = new Date();
			Date date=smHoliday.FactDate(nowDate,2);
			java.sql.Date sqlNowDate=new java.sql.Date(nowDate.getTime());
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			if(proStatus.equals("jjcq")){
				str.append(" and proinst_willfinish > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') and proinst_willfinish< TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(proStatus.equals("ycq")){
				str.append(" and proinst_willfinish < TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else{
				str.append(" and proinst_willfinish < TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			
		}
		if (passback != null && !passback.equals("")) {
			str.append(" and operation_type=");
			str.append(WFConst.Operate_Type.PRODUCT_anglesPicture.value);
		}
        String tabel =
			"SELECT\n" +
			"             PO.PROJECT_NAME,\n" + 
			"             PO.ACTDEF_TYPE,\n" + 
			"             PO.INSTANCE_TYPE,\n" +
			"             PO.FILE_NUMBER,\n" + 
			"             PO.STAFF_ID_NACT AS ACTSTAFF,\n" + 
			"             PO.STAFF_NAME_NACT as actstaffname,\n" + 
			"             PO.YWH,\n" + 
			"             PO.MSG,\n" +
			"             PO.BATCH,\n" +
			"             PO.PROINST_ID,PO.PRODEF_ID,\n" + 
			"             PO.PRODEF_NAME,PO.PROINST_STATUS,\n" + 
			"             PO.PROINST_END,PO.PROINST_START,PO.PROINST_WILLFINISH,\n" + 
			"             PO.PROINST_CODE,\n" + 
			"             PO.ACTINST_ID,\n" + 
			"             PO.OPERATION_TYPE_NACT AS OPERATION_TYPE,\n" + 
			"             PO.STAFF_ID_NACT AS STAFF_ID,\n" + 
			"             PO.ACTINST_NAME,\n" + 
			"             PO.sfjsbl,\n" +
			"			  PO.WLSH,\n" +
			"             PO.STAFF_NAME_NACT AS Staff_Name ,PO.AREACODE,\n" + 
			"             PO.ACTINST_STATUS,PO.ACTINST_START,PO.ACTINST_END,PO.ACTINST_WILLFINISH,\n" + 
			"             PO.PROLSH,PO.CODEAL,PO.Urgency,PO.Proinst_Time,PO.PROINST_WEIGHT\n" + 
			"             FROM  BDC_WORKFLOW.WFI_PROINST PO ";
        if (sqr != null && !"".equals(sqr)) {
			if (isChinese(sqr)) {
				str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%" + sqr + "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=PO.FILE_NUMBER) ");
			}
		}
        long tatalCount = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST po where " + str.toString());
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { 
			//桂林需求，按受理编号排序
			if("450300".equals(ConfigHelper.getNameByValue("XZQHDM"))) {
				str.append(" order by PROLSH  " + order);
			}else {
			str.append(" order by ACTINST_START  " + order);
			}
			List<Map> resMap = commonDao.getDataListByFullSql(tabel+" where " + str.toString(), CurrentPageIndex, pageSize);
			msg.setRows(resMap);
		}
		return msg;
	}
	/**
	 * 用视图进行全局查询
	 * */
	public Message getAllProjectListByView(QueryCriteria query,String prolshs,String order){
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		String key =query.getValue();
		String areaCode=query.getAreaCode();
		String ywh=query.getYwh();
		String lshstart=query.getLshStart();
		String lshend=query.getLshEnd();
		String status=query.getStatus();
		String prodefname=query.getProdefName();
		String urgency=query.getUrgency();
		String outtime=query.getOutTime();
		String proStatus=query.getProStatus();
		String passback=query.getPassBack();
		String sqr=query.getSqr();
		String actdef_name=query.getActdefName();
		String start=query.getActinstStart();
		String end=query.getActinstEnd();
		String staff=query.getStaff();
		
		String deadline_s = query.getDeadline_s();
		String deadline_e = query.getDeadline_e();
		
		int CurrentPageIndex=Integer.parseInt(query.getCurrentPageIndex());
		int pageSize=Integer.parseInt(query.getPageSize());
		str.append("1>0 ");
		if (key != null && !"".equals(key)) {
			str.append(" and (PROJECT_NAME like '%");
			str.append(key);
			str.append("%' or FILE_NUMBER like '%");
			str.append(key);
			str.append("%' ");
			str.append(" or BATCH like '%");
			str.append(key);
			str.append("%' ");
			str.append(" or PROLSH like '%");
			str.append(key);
			str.append("%'");
			str.append(" or WLSH like '%");
			str.append(key);
			str.append("%'");

			str.append(")");
			// 支持权利人查询

		}
		
		if(deadline_s!=null&&!"".equals(deadline_s)){
			str.append(" and PROINST_WILLFINISH >= to_date('");
			str.append(deadline_s);
			str.append("','yyyy-MM-dd')");
		}
		if(deadline_e!=null&&!"".equals(deadline_e)){
			str.append(" and PROINST_WILLFINISH <= to_date('");
			str.append(deadline_e);
			str.append("','yyyy-MM-dd')");
		}
		
		if (prodefname != null && !"".equals(prodefname)) {
			str.append(" and prodef_name like'");
			str.append(prodefname);
			str.append("%'");
		}
		if(areaCode!=null&&!areaCode.isEmpty()){
			str.append(" and AREACODE = '"+areaCode+"' ");
		}
		if(!StringHelper.isEmpty(ywh)){
			str.append(" and YWH like '%"+ywh+"%' ");
		}
		if(lshstart != null && !lshstart.equals("")){
			str.append(" and PROLSH>='"+lshstart+"' ");
		}
		if(lshend != null && !lshend.equals("")){
			str.append(" and PROLSH<='"+lshend+"' ");
		}
		if(proStatus!=null&&!proStatus.equals("")){
			str.append(" and proinst_status  not in('0') ");
			Date nowDate = new Date();
			Date date=smHoliday.FactDate(nowDate,2);
			java.sql.Date sqlNowDate=new java.sql.Date(nowDate.getTime());
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			if(proStatus.equals("jjcq")){
				str.append(" and proinst_willfinish > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') and proinst_willfinish< TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(proStatus.equals("ycq")){
				str.append(" and proinst_willfinish < TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else{
				str.append(" and proinst_willfinish < TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			
		}
		if(!StringHelper.isEmpty(prolshs)){
			str.append(" and PROLSH in ("+prolshs+")");
		}
		if (urgency != null && !urgency.equals("")) {
			str.append(" and urgency=");
			str.append(urgency);
		}
		if (passback != null && !passback.equals("")) {
			str.append(" and operation_type=");
			str.append(WFConst.Operate_Type.PRODUCT_anglesPicture.value);
		}
		
		
		StringBuilder sb=new StringBuilder();
		sb.append("and proinst_id in (select proinst_id po from "+Common.WORKFLOWDB+"wfi_actinst where 1>0 ");
		if (actdef_name != null && !actdef_name.equals("")) {
			sb.append(" and ACTINST_NAME in (");
			sb.append(actdef_name);
			sb.append(" )");
		}
		if (status != null && !"".equals(status)) {
			sb.append(" and ACTINST_STATUS in (");
			sb.append(status);
			sb.append(")");
		}
		if (start != null && !"".equals(start)) {
			sb.append(" and ACTINST_END >= to_date('");
			sb.append(start);
			sb.append("','yyyy-MM-dd')");
		}
		if (end != null && !"".equals(end)) {
			sb.append(" and ACTINST_END < to_date('");
			sb.append(end);
			sb.append("','yyyy-MM-dd')");
		}
		
		
		// 办理人员
		if (staff != null && !"".equals(staff)) {
			sb.append("  AND ACTINST_ID IN ( SELECT ACTINST_ID FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF WHERE STAFF_NAME LIKE '%");
			sb.append(staff);
			sb.append("%')");
		}
		sb.append(")");
	
		if (outtime != null && !outtime.equals("")) {
			str.append(" and proouttime>0");
		}
		
		if (sqr != null && !"".equals(sqr)) {
			if (isChinese(sqr)) {
				str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%" + sqr + "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=wfi_proinst.FILE_NUMBER) ");
			}
		}
		str.append(sb.toString());
		long tatalCount = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"wfi_proinst where "+str.toString());
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { //
			str.append(" order by ACTINST_START  " + order);
			msg.setRows(commonDao.getDataListByFullSql("select * from "+Common.WORKFLOWDB+"wfi_proinst where "+str.toString(), CurrentPageIndex, pageSize));
		}
		return msg;
	}
	/**
	 * 根据人员获取所有已办项目列表 不分页
	 * 
	 * @param staff_Id
	 * @param actInst_Status
	 * @return
	 */
	public Message GetProjectList(String staff_Id, int actInst_Status) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append(" ACTSTAFF='");
		str.append(staff_Id);
		if (actInst_Status == 8) {
			str.append("' and ACTINST_STATUS in (1,2)");
		} else {
			str.append("' and ACTINST_STATUS=");
			str.append(actInst_Status);
		}
		// 过滤已经被收件的件
		str.append(" and ( staff_id  is null or staff_id='");
		str.append(staff_Id);
		str.append("')");
		long tatalCount = commonDao.getCountByFullSql(" from " + Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString());
		msg.setTotal(tatalCount);
		if (tatalCount > 0) {
			msg.setRows(commonDao.getDataListByFullSql("select * from " + Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString()));
		}
		return msg;
	}

	/**
	 * 判断给业务类别是否存在
	 * 
	 * @param Prodef_ID
	 * */
	public Boolean HasProdef(String Prodef_ID) {
		StringBuilder noWhereSql = new StringBuilder();
		noWhereSql.append("Prodef_ID='");
		noWhereSql.append(Prodef_ID);
		noWhereSql.append("'");
		List<Wfd_Prodef> list = commonDao.findList(Wfd_Prodef.class, noWhereSql.toString());
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean CheckAcceptType(int proDefID, int acceptTypeID) {
		// List<SmObjInfo>=
		return false;
	}
	
	/**
	 *根据流水号获取proinst 
	 */
	public Wfi_ProInst GetProInstByLsh(String lsh){
		StringBuilder sql = new StringBuilder();
		sql.append(" prolsh='");
		sql.append(lsh+"' ");
		List<Wfi_ProInst> list = commonDao.findList(Wfi_ProInst.class, sql.toString());
		if(null!=list&&1==list.size()){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据proinstid获取项目当前环节
	 */
	public Wfi_ActInst GetNewActInst(String proinstid){
		StringBuilder sql = new StringBuilder();
		sql.append(" proinst_id='");
		sql.append(proinstid+"' ");
		sql.append(" order by actinst_end desc ");
		List<Wfi_ActInst> list = commonDao.findList(Wfi_ActInst.class, sql.toString());
		if(null!=list&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取单个项目的详细信息
	 * */
	public SmProInfo GetProjectInfo(String actinst) {
		Wfi_ActInst ActInst = commonDao.load(Wfi_ActInst.class, actinst);
		SmProInfo ProInfo = new SmProInfo();
		if (ActInst != null) {
			Wfi_ProInst ProInst = commonDao.load(Wfi_ProInst.class, ActInst.getProinst_Id());
			Wfd_Prodef  prodef = smProdef.GetProdefById(ProInst.getProdef_Id());
			if (ProInst != null) {
				Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class, ActInst.getActdef_Id());
				_SmProInst.ProInfoFromProInst(ProInfo, ProInst);
				_smActInst.ProInfoFromActInst(ProInfo, ActInst);
				//ProInfo.setDefinedCodeal(_smActInst.GetActDef(actinst).getCodeal());
				if (actdef != null) {
					ProInfo.setDefinedCodeal(actdef.getCodeal());
					ProInfo.setIsReturnAct(actdef.getIsReturnAct()==null?0:actdef.getIsReturnAct());
					ProInfo.setActDef_updaloadfile(actdef.getUploadfile()==null?0:actdef.getUploadfile());
					//按钮权限控制
					ProInfo.setBtnPermission(actdef.getBtnPermission()==null?"":actdef.getBtnPermission());
				}
				if (prodef != null) {
					ProInfo.setProdef_Tpl(prodef.getProdef_Tpl());
					ProInfo.setHouse_status(prodef.getHouse_Status());
				}
				if(!StringHelper.isEmpty(StringHelper.FormatByDatatype(ProInst.getWLSH()))) {
					ProInfo.setWlsh(ProInst.getWLSH());
				}
				//审批默认意见
				String fulSql = 
						"  select *\n" +
						"  from "+Common.WORKFLOWDB+"wfi_spdy\n" + 
						"  where spdy_id in (select spdy_id\n" + 
						"  from "+Common.WORKFLOWDB+"Wfi_Tr_ActDefToSpdy\n" + 
						"  where actdef_id = '"+ActInst.getActdef_Id()+"'\n" + 
						"  and readonly = '0')";
				List<Map> list = commonDao.getDataListByFullSql(fulSql);
				if(list.size()>0){
					Map spdy = list.get(0);
					String defaultOpinion = spdy.get("MRYJ")!=null?spdy.get("MRYJ").toString():"";
					ProInfo.setDefaultOpinion(defaultOpinion);
				}
			}
		}
		return ProInfo;
	}

	public List<Wfi_ActInst> GetProInstProcess(String actinstid) {
		return _smActInst.GetActInstsbyactactid(actinstid);
	}

	/**
	 * 按照项目编号获取项目信息
	 * 
	 * */
	public SmProInfo GetProjectInfoByFileNumber(String FileNumber) {
		SmProInfo ProInfo = new SmProInfo();
		Wfi_ProInst proindtInst = _SmProInst.GetProInstByFileNumber(FileNumber);
		if (proindtInst != null) {
			_SmProInst.ProInfoFromProInst(ProInfo, proindtInst);
		}
		return ProInfo;
	}

	/**
	 * 根据id删除ProInst
	 */
	public SmObjInfo deleteProInst(String id) {
		SmObjInfo info = new SmObjInfo();
		StringBuilder str = new StringBuilder();
		str.append(" Proinst_Id='");
		str.append(id);
		str.append("'");
		List<Wfi_ActInst> actInstList = commonDao.findList(Wfi_ActInst.class, str.toString());
		StringBuilder checkstr = new StringBuilder();
		Wfi_ProInst proInst = commonDao.get(Wfi_ProInst.class, id);
		String checkid=proInst.getFile_Number();
		checkstr.append(" FROM BDCK.BDCS_DJSF WHERE YWH='").append(checkid).append("'")
		.append(" AND (SSJE IS NOT NULL OR TO_NUMBER(SSJE) <> 0)");
		long count=commonDao.getCountByFullSql(checkstr.toString());
		if(count>0){
			info.setID("checkfail");
			info.setDesc("该项目已缴费!");
			return info;
		}
		
		for (int i = 0; i < actInstList.size(); i++) {
			String actInstIDString = actInstList.get(i).getActinst_Id();
			_smActInst.DelActInst(actInstIDString);
			_smActInst.DelNowActinst(actInstIDString, actInstList.get(i).getStaff_Id());
		}
		
		_smRouteInst.DelSmRouteInst(proInst.getFile_Number());
		projectService.deleteXmxxByPrjID(proInst.getFile_Number());
		_SmProInst.delProinstDate(proInst.getFile_Number());
		_SmProMater.DeleteProMaterByProInst(id);
		_SmProInst.DelProInst(id);
		commonDao.flush();
		info.setID(id);
		info.setDesc("删除成功!");
		String Staffid = smStaff.getCurrentWorkStaffID();
		String staff_name = smStaff.GetStaffName(Staffid);
		smAbnormal.AddAbnormInfo(Staffid, staff_name, "", "", id, "删除项目-" + proInst.getProlsh() + "-" + proInst.getProject_Name(), WFConst.Abnormal_Type.DeleteProject.value);
		YwLogUtil.addYwLog("在办箱：删除项目：项目名称：" + proInst.getProject_Name(), ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		return info;
	}

	/**
	 * 根据id删除ProInst
	 */
	public SmObjInfo deleteProInst(String id, String staffId, String staffName, String reason) {
		SmObjInfo info = new SmObjInfo();
		try {
			StringBuilder str = new StringBuilder();
			str.append(" Proinst_Id='");
			str.append(id);
			str.append("'");
			List<Wfi_ActInst> actInstList = commonDao.findList(Wfi_ActInst.class, str.toString());
			StringBuilder checkstr = new StringBuilder();
			Wfi_ProInst proInst = commonDao.get(Wfi_ProInst.class, id);
			String checkid=proInst.getFile_Number();
			checkstr.append(" FROM BDCK.BDCS_DJSF WHERE YWH='").append(checkid).append("'")
					.append(" AND (SSJE IS NOT NULL OR TO_NUMBER(SSJE) <> 0)");
			long count=commonDao.getCountByFullSql(checkstr.toString());
			if(count>0){
				info.setID("checkfail");
				info.setDesc("该项目已缴费!");
				return info;
			}

			for (int i = 0; i < actInstList.size(); i++) {
				String actInstIDString = actInstList.get(i).getActinst_Id();
				_smActInst.DelActInst(actInstIDString);
				_smActInst.DelNowActinst(actInstIDString, actInstList.get(i).getStaff_Id());
			}

			_smRouteInst.DelSmRouteInst(proInst.getFile_Number());
			projectService.deleteXmxxByPrjID(proInst.getFile_Number());
			_SmProInst.delProinstDate(proInst.getFile_Number());
			_SmProMater.DeleteProMaterByProInst(id);
			_SmProInst.DelProInst(id);
			commonDao.flush();
			info.setID(id);
			info.setDesc("删除成功!");
			smAbnormal.AddAbnormInfo(staffId, staffName, "", "", id, "删除项目-" + proInst.getProlsh() + "-" + proInst.getProject_Name(), WFConst.Abnormal_Type.DeleteProject.value, reason);
//		YwLogUtil.addYwLog("在办箱：删除项目：项目名称：" + proInst.getProject_Name(), ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 为了兼容重载方法 根据id删除ProInst
	 */
	public SmObjInfo deleteProInst(String id, String reason) {
		SmObjInfo info = new SmObjInfo();
		Wfi_ProInst proInst = commonDao.get(Wfi_ProInst.class, id);
		Boolean flag = isXmxxByPrjID(proInst.getFile_Number());
		if(flag == false){
			info.setDesc("单元未移除，无法删除项目！");
		}else{
			StringBuilder str = new StringBuilder();
			str.append(" Proinst_Id='");
			str.append(id);
			str.append("'");
			List<Wfi_ActInst> actInstList = commonDao.findList(Wfi_ActInst.class, str.toString());
			for (int i = 0; i < actInstList.size(); i++) {
				String actInstIDString = actInstList.get(i).getActinst_Id();
				_smActInst.DelActInst(actInstIDString);
				_smActInst.DelNowActinst(actInstIDString, actInstList.get(i).getStaff_Id());
			}
			_smRouteInst.DelSmRouteInst(proInst.getFile_Number());
			projectService.deleteXmxxByPrjID(proInst.getFile_Number());
			_SmProMater.DeleteProMaterByProInst(id);
			_SmProInst.DelProInst(id);
			commonDao.flush();
			info.setID(id);
			info.setDesc("删除成功!");
			String Staffid = smStaff.getCurrentWorkStaffID();
			String staff_name = smStaff.GetStaffName(Staffid);
			if (reason != null) {
				smAbnormal.AddAbnormInfo(Staffid, staff_name, "", "", id, "删除项目-" + proInst.getProlsh() + "-" + proInst.getProject_Name(), WFConst.Abnormal_Type.DeleteProject.value, reason);
			} else {
				smAbnormal.AddAbnormInfo(Staffid, staff_name, "", "", id, "删除项目-" + proInst.getProlsh() + "-" + proInst.getProject_Name(), WFConst.Abnormal_Type.DeleteProject.value);
			}
			YwLogUtil.addYwLog("在办箱：删除项目：项目名称：" + proInst.getProject_Name(), ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		}
		return info;
	}
	/**
	 *根据id注销ProInst
	 */
	public boolean ZxProject(String actinstid, String reason) {
		Wfi_ProInst proInst =_SmProInst.GetProInstByActInstId(actinstid);
		String id= proInst.getProinst_Id();
		StringBuilder str = new StringBuilder();
		str.append(" Proinst_Id='");
		str.append(id);
		str.append("'");
		Wfi_ActInst actinst=commonDao.get(Wfi_ActInst.class, actinstid);
		try{			
				actinst.setActinst_Status(WFConst.Instance_Status.Instance_LogOut.value);
				proInst.setInstance_Type(WFConst.Instance_Type.Instance_LogOut.value);
				proInst.setProinst_Status(WFConst.Instance_Status.Instance_LogOut.value);
				 commonDao.delete(Wfi_NowActInst.class, actinstid);
				Boolean delet=projectService.deleteXmxxByPrjID(proInst.getFile_Number());	
			}catch(Exception e){
		}
		//TODO:转办后更新环节实例中当前环节的信息：
		if(proInst!=null){
			proInst.setActinst_Status(actinst.getActinst_Status());
		}
		commonDao.flush();
		User staff = smStaff.getCurrentWorkStaff();
		String staff_name=staff.getUserName();
		String Staffid=staff.getId();
		if (reason != null) {
			smAbnormal.AddAbnormInfo(Staffid, staff_name, "", actinstid, id, "注销项目-" + proInst.getProlsh() + "-" + proInst.getProject_Name(), WFConst.Abnormal_Type.LogOutProject.value, reason);
		} else {
			smAbnormal.AddAbnormInfo(Staffid, staff_name, "", actinstid, id, "注销项目-" + proInst.getProlsh() + "-" + proInst.getProject_Name(), WFConst.Abnormal_Type.LogOutProject.value);
		}		
		return true;
	}
	/*
	 * 获取员工的在办事项
	 */
	public List<SmObjInfo> GetProjectTypeAndCountByStaffID(String staff_id) {
		List<SmObjInfo> list = null;
		list = _SmProInst.GetProjectTypeAndCountByStaffID(staff_id);
		return list;

	}

	public List<SmObjInfo> ProjectStatic(String staff_id) {
		return _SmProInst.ProjectStatic(staff_id);
	}

	public Map<String, Object> publicSearch(String filenumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		SmProInfo projectInfo = GetProjectInfoByFileNumber(filenumber);
		if (projectInfo != null) {
			List<Wfi_ActInst> noeactInst = commonDao.getDataList(Wfi_ActInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ActInst where proinst_id='" + projectInfo.getProInst_ID() + "'");
			if (noeactInst != null && noeactInst.size() > 0) {
				String actinst_id = noeactInst.get(0).getActinst_Id();
				List<Wfi_ActInst> actInsts = GetProInstProcess(actinst_id);
				map.put("process", actInsts);
				map.put("proinfo", GetProjectInfo(actinst_id));
			}
		}
		return map;
	}
	
	public List<Wfi_ActInst> publicSearchEx(String file_number) {
		if (file_number.equals("")) {
			return null;
		}
		List<Wfi_ActInst> list = new ArrayList<Wfi_ActInst>();
		Map<String, String> paramMap = new HashMap<String, String>();
		String proinstid = "";
		String prodefid = "";
		List<Map> proinstmap = 
		commonDao.getDataListByFullSql("select proinst_id,prodef_id from BDC_WORKFLOW.WFI_PROINST where prolsh='"+file_number+"'");
		if(proinstmap!=null&&proinstmap.size()>0){
			prodefid=proinstmap.get(0).get("PRODEF_ID").toString();
			proinstid=proinstmap.get(0).get("PROINST_ID").toString();
		}
		String jdquerysql = 
				"select\n" +
						"t.actdef_name as ACTINST_NAME,\n" + 
						"h.STAFF_NAME,\n" + 
						"h.ACTINST_START,\n" + 
						"h.ACTINST_END,\n" + 
						"h.OPERATION_TYPE,\n" + 
						"h.ACTINST_STATUS\n" + 
						"from bdc_workflow.wfd_actdef t\n" + 
						"left join (\n" + 
						"select * from (\n" + 
						"select t.*, row_number() over(partition by proinst_id, actdef_id order by actinst_start asc) rn from\n" + 
						"bdc_workflow.wfi_actinst t\n" + 
						")\n" + 
						"where rn=1 and proinst_id = '"+proinstid+"'\n" + 
						") h\n" + 
						"on t.actdef_id = h.actdef_id\n" + 
						"where t.prodef_id = '"+prodefid+"' order by h.actinst_start";

	
		
		List<Map> map =commonDao.getDataListByFullSql(jdquerysql);
		
		if (map.size() > 0) {
			Wfi_ActInst actInst = null;
			for (Map map2 : map) {
				actInst = new Wfi_ActInst();
				String name="";
				if(map2.get("STAFF_NAME")!=null && map2.get("STAFF_NAME") !=""){
					name=map2.get("STAFF_NAME").toString();
				}
				actInst.setActinst_Name(map2.get("ACTINST_NAME").toString());
				actInst.setStaff_Name(name);
				actInst.setOperation_Type(
						map2.get("OPERATION_TYPE")==null?"":map2.get("OPERATION_TYPE").toString());
				actInst.setActinst_Status(Integer.valueOf(map2.get(
						"ACTINST_STATUS")==null?"-1":map2.get(
								"ACTINST_STATUS").toString()));
				list.add(actInst);
			}
		}
		return list;
	}

	/**
	 * 档案的移交清单 type 1、按照员工个人
	 * */
	public Message getDossierList(Integer type, String id, int page, int pagesize, String actname, Boolean isLoadHavaDoneBoolean) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		String sqlString = "";
		if (type == 1) {
			str.append(" ACTSTAFF='");
			str.append(id);
			str.append("' and actinst_status=");
			str.append(WFConst.Instance_Status.Instance_Passing.value);
			str.append(" and actinst_name='");
			str.append(actname);
			str.append("'");
			if (!isLoadHavaDoneBoolean) {
				str.append(" and istransfer is null");
			}

			sqlString = str.toString();
		} else if (type == 2) {
			sqlString = _smActInst.getActinstByStatus(id, isLoadHavaDoneBoolean);
		}

		long tatalCount = commonDao.getCountByFullSql(" from " + Common.WORKFLOWDB + "V_PROJECTLIST where " + sqlString);
		msg.setTotal(tatalCount);
		if (tatalCount > 0) {
			 //str.append(" order by ACTINST_START desc ");
			msg.setRows(commonDao.getDataListByFullSql("select * from " + Common.WORKFLOWDB + "V_PROJECTLIST where " + sqlString, page, pagesize));
		}
		return msg;
	}

	public SmObjInfo SignDossierTransfer(String proinstids, String actdefname) {
		SmObjInfo info = new SmObjInfo();
		String[] proinst_idsString = proinstids.split(",");
		if (proinst_idsString.length > 0) {
			for (String proinstid : proinst_idsString) {
				if (!proinstid.equals("")) {
					Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proinstid);
					StringBuilder sbBuilder = new StringBuilder();
					sbBuilder.append("select * from ");
					sbBuilder.append(Common.WORKFLOWDB);
					sbBuilder.append("Wfi_ActInst where proinst_id='");
					sbBuilder.append(proinstid);
					if (!actdefname.equals("")) {
						sbBuilder.append("' and ACTINST_NAME='");
						sbBuilder.append(actdefname);
						sbBuilder.append("'");
					} else {
						sbBuilder.append("' and  actinst_status=");
						sbBuilder.append(WFConst.Instance_Status.Instance_Passing.value);
					}
					sbBuilder.append(" order by actinst_end desc");
					List<Wfi_ActInst> actInst = commonDao.getDataList(Wfi_ActInst.class, sbBuilder.toString());
					if (actInst != null && actInst.size() > 0) {
						Wfi_ActInst inst2 = actInst.get(0);
						inst2.setIstransfer(1);
						inst2.setTransfer_Time(new Date());
						commonDao.update(inst);
					}
				}
			}
			commonDao.flush();
			info.setID("1");
		}
		return info;

	}

	public boolean ModiftyProjectName(String proinstid, String Projectname) {
		Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proinstid);
		if (inst != null) {
			List<BDCS_XMXX> xmxx = commonDao.findList(BDCS_XMXX.class, "project_id = '" +inst.getFile_Number()+ "'");
			if(xmxx.size()>0) {
				xmxx.get(0).setXMMC(Projectname);
				commonDao.update(xmxx.get(0));
				commonDao.flush();
			}
			inst.setProject_Name(Projectname);
			commonDao.update(inst);
			commonDao.flush();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取一个项目中的活动环节，每个定义环节只包含一个实例（不包含）驳回实例
	 * 
	 * @param proinstid
	 */
	public List<Map> GetActinstNotPassback(String proinstid) {
		if (proinstid != null && !"".equals(proinstid)) {
			StringBuilder sb = new StringBuilder();
			sb.append("select * from ");
			sb.append(Common.WORKFLOWDB);
			sb.append("WFI_ACTINST where proinst_id='");
			sb.append(proinstid);
			sb.append("' and (operation_type<>");
			sb.append(WFConst.Operate_Type.PRODUCT_anglesPicture.value);
			sb.append(" or operation_type is null)");
			sb.append(" order by actinst_end");
			return commonDao.getDataListByFullSql(sb.toString());

		} else {
			return null;
		}

	}

	// 获取办件过程的proinstid
	public List<Map> GetBJGC(String ywh) {
		String proinstid = null;
		if (ywh != null && !"".equals(ywh)) {
			Wfi_ProInst proinst = _SmProInst.GetProInstByFileNumber(ywh);
			if (proinst != null && !"".equals(proinst)) {
				proinstid = proinst.getProinst_Id();
			}
			if (proinstid != null && !"".equals(proinstid)) {
				return GetActinstNotPassback(proinstid);
			} else {
				return null;
			}

		}

		return null;
	}
	
	// 获取办件过程的proinstid
		public List<Map> GetBJGCBySLBH(String slbh) {
			String proinstid = null;
			if (slbh != null && !"".equals(slbh)) {
				List<Wfi_ProInst> proinsts = _SmProInst.GetProInstByLshs(slbh);
				if (proinsts!=null && proinsts.size()>0) {
					Wfi_ProInst proinst = proinsts.get(0);
					if (proinst != null && !"".equals(proinst)) {
						proinstid = proinst.getProinst_Id();
					}
					if (proinstid != null && !"".equals(proinstid)) {
						return GetActinstNotPassback(proinstid);
					} else {
						return null;
					}
				}
			}
			return null;
		}
	/**
	 * 获取异常项目记录
	 */
	public Page GetAbnormalProject(String searchkey, String type, int pageindex, int pagesize) {
		String sql = "1>0";
		if (searchkey != null && !searchkey.equals("")) {
			sql += " and operation_msg like '%" + searchkey + "%'";
		}
		if (type != null && !type.equals("")) {
			sql += " and operation_type in (" + type + ")";

		}
		Page page = commonDao.GetPagedData(Wfi_Abnormal.class, Common.WORKFLOWDB + "Wfi_Abnormal", pageindex, pagesize, sql);

		return page;

	}

	/**
	 * 质检
	 * 
	 * @param actinst_id
	 * @return
	 */
	public boolean QualityTest(String actinst_id, User user) {

		Wfi_ProInst inst = _SmProInst.GetProInstByActInstId(actinst_id);
		if (inst != null) {
			inst.setProinst_Status(WFConst.Instance_Status.QualityTest.value);
			commonDao.update(inst);
			Wfi_ActInst ainst = _smActInst.GetActInst(actinst_id);
			if (ainst != null) {
				operationService.delLockProject(actinst_id, "质检中", WFConst.LockedType.QualityTest.value+"", null);
			}
			commonDao.flush();
		}

		return true;
	}

	/**
	 * 取消质检
	 * 
	 * @param actinst_id
	 * @return
	 */
	public boolean CancelQualityTest(String actinst_id, User user) {
		Wfi_ProInst inst = _SmProInst.GetProInstByActInstId(actinst_id);
		if (inst != null) {
			inst.setProinst_Status(WFConst.Instance_Status.Instance_doing.value);
			commonDao.update(inst);
			Wfi_ActInst ainst = _smActInst.GetActInst(actinst_id);
			if (ainst != null) {
				operationService.cancleLock(actinst_id , new Date());
			}
			commonDao.flush();
		}
		return true;
	}

	public SmObjInfo SubmitQualityApproval(String actinst_id, User user, String yjcontent) {
		SmObjInfo info = new SmObjInfo();
		Wfi_Spdy spdy = null;
		List<Wfi_Spdy> list = commonDao.findList(Wfi_Spdy.class, " SPLX='ZJYJ' and spdy_id='af84440bf6f44c5ab9b11fa1a0db583b'");
		if (list != null && list.size() > 0) {
			spdy = list.get(0);
		} else {
			spdy = new Wfi_Spdy();
			spdy.setSpdy_Id("af84440bf6f44c5ab9b11fa1a0db583b");
			spdy.setSpmc("质检意见");
			spdy.setSplx("ZJYJ");
			spdy.setStatus(1);
			spdy.setSigntype(1);
			commonDao.save(spdy);
			commonDao.flush();
		}
		Wfi_ProInst inst = _SmProInst.GetProInstByActInstId(actinst_id);
		Wfi_ActInst actinst = _smActInst.GetActInst(actinst_id);
		if (inst != null) {
			yjcontent = actinst.getActinst_Name() + "质检意见：" + yjcontent;
			Wfi_Spyj spyj = new Wfi_Spyj();
			spyj.setSpdy_Id(Common.CreatUUID());
			spyj.setActinst_Id(actinst_id);
			spyj.setProinst_Id(inst.getProinst_Id());
			spyj.setSpdy_Id(spdy.getSpdy_Id());
			spyj.setSplx(spdy.getSplx());
			spyj.setSpr_Name(user.getUserName());
			spyj.setSpsj(new Date());
			spyj.setSpyj(yjcontent);
			spyj.setSpr_Id(user.getId());
			commonDao.save(spyj);
			commonDao.flush();
			info.setID(spyj.getSpdy_Id());
			info.setDesc("保存成功");
		}
		return info;

	}

	/**
	 * 获取部门能够办理的活动
	 * 
	 * @param staffid
	 * @return
	 */
	public Map GetActNameByDeptID(String staffid) {

		Map map = new HashMap();

		User user = userService.findById(staffid);
		if (user != null) {
			List<User> users = userService.findUserByDepartmentId(user.getDepartment().getId());
			map.put("staff", users);
			if (users != null && users.size() > 0) {
				List<Map> list = _smActInst.GetActinstNameByStaffId(users);
				map.put("actname", list);
			}
			List<Map> list = _smActInst.GetProinstNameByStaffId(users);
			map.put("proname", list);
		}
		return map;

	}

	public Map GetActName() {
		Map map = new HashMap();
		map.put("actname", _smActInst.GetAllActDefName());
		return map;

	}

	public List<Map> GetActNameByStaffID(String staff_id) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct(actdef_name) from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfd_actdef where actdef_type <> 3010 and actdef_type <> 3020");
		return commonDao.getDataListByFullSql(sb.toString());

	}

	public List<Map> GetProName() {
		Map map = new HashMap();
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct prodef_name from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_proinst  where prodef_name is not null  order by prodef_name desc");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());

		return list;
	}

	public Map GetProNameByDeptID(String staffid) {
		Map map = new HashMap();
		User user = userService.findById(staffid);
		if (user != null) {
			List<User> users = userService.findUserByDepartmentId(user.getDepartment().getId());
			map.put("staff", users);
			if (users != null && users.size() > 0) {
				List<Map> list = _smActInst.GetProinstNameByStaffId(users);
				map.put("actname", list);
			}
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map GetProNameByUserID(User user, String status) {
		Map map = new HashMap();
		List<User> users = new ArrayList<User>();
		users.add(user);
		List<Map> list = _smActInst.GetProinstNameByStaffId(users, status);
		map.put("proname", list);
		return map;
	}

	public List<Map> GetAllProjectByActinstName() {
		String str = "select actinst_id,actinst_name,file_number from (select distinct proinst_id, actinst_id,actinst_name from " + Common.WORKFLOWDB + "wfi_actinst where actinst_name='归档') act";
		str += " left join " + Common.WORKFLOWDB + "wfi_proinst pro  on  pro.proinst_id=act.proinst_id where pro.proinst_status=0";
		return commonDao.getDataListByFullSql(str);
	}

	public Map VerifyDossier(String lsh, User user) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("v_projectlist where  prolsh in (");
		sb.append("'");
		sb.append(lsh);
		sb.append("'");
		sb.append(")");
		sb.append("and actinst_name like'%归档%' and actinst_status in(");
		sb.append(WFConst.Instance_Status.Instance_NotAccept.value + "," + WFConst.Instance_Status.Instance_doing.value);
		sb.append(")");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		if (list != null && list.size() > 0) {
			Map item = list.get(0);

			Wfi_ActInst inst = commonDao.get(Wfi_ActInst.class, item.get("ACTINST_ID").toString());
			if (inst != null) {
				inst.setStaff_Id(user.getId());
				inst.setStaff_Name(user.getUserName());
				inst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
				commonDao.update(inst);
				commonDao.flush();
			}
			StringBuilder strxmxx = new StringBuilder();
			strxmxx.append("select * from ");
			strxmxx.append("BDCK.");
			strxmxx.append("BDCS_XMXX where  YWLSH ='");
			strxmxx.append(lsh);
			strxmxx.append("'");
			List<Map> listXMXX = commonDao.getDataListByFullSql(strxmxx.toString());
			if (listXMXX != null && listXMXX.size() > 0) {
				Map mapxmxx = listXMXX.get(0);
				String djlx = mapxmxx.get("DJLX").toString();
				String qllx = mapxmxx.get("QLLX").toString();
				String filenumber = mapxmxx.get("PROJECT_ID").toString();
				String xmbh = mapxmxx.get("XMBH").toString();
				if ((DJLX.ZXDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)) || (DJLX.CFDJ.Value.equals(djlx) && QLLX.CFZX.Value.equals(qllx))) {
					StringBuilder djdystr = new StringBuilder();
					djdystr.append("select * from ");
					djdystr.append("BDCK.");
					djdystr.append("BDCS_DJDY_GZ where  XMBH ='");
					djdystr.append(xmbh);
					djdystr.append("'");
					List<Map> djdys = commonDao.getDataListByFullSql(djdystr.toString());
					if (djdys.size() > 0 && djdys != null) {
						Map djdy = djdys.get(0);
						String djdyid = djdy.get("DJDYID").toString();
						StringBuilder qlstr = new StringBuilder();
						qlstr.append("select * from ");
						qlstr.append("BDCK.");
						qlstr.append("BDCS_QL_GZ where  DJDYID ='");
						qlstr.append(djdyid);
						qlstr.append("'");
						qlstr.append("and xmbh='");
						qlstr.append(xmbh);
						qlstr.append("'");
						List<Map> qls = commonDao.getDataListByFullSql(qlstr.toString());
						if (qls.size() > 0 && qls != null) {
							Map ql = qls.get(0);
							String lyqlid = ql.get("LYQLID").toString();

							StringBuilder lyqlstr = new StringBuilder();
							lyqlstr.append("select * from ");
							lyqlstr.append("BDCK.");
							lyqlstr.append("BDCS_QL_LS where  QLID ='");
							lyqlstr.append(lyqlid);
							lyqlstr.append("'");
							List<Map> lyqls = commonDao.getDataListByFullSql(lyqlstr.toString());
							if (lyqls.size() > 0 && lyqls != null) {
								String lyxmbh = null;
								Map lyql = lyqls.get(0);
								if(lyql.get("XMBH") != null){
									  lyxmbh = lyql.get("XMBH").toString();	
								}else{
									return item;
								}
								
								StringBuilder strlyxmxx = new StringBuilder();
								strlyxmxx.append("select * from ");
								strlyxmxx.append("BDCK.");
								strlyxmxx.append("BDCS_XMXX where  XMBH ='");
								strlyxmxx.append(lyxmbh);
								strlyxmxx.append("'");
								List<Map> listLYXMXX = commonDao.getDataListByFullSql(strlyxmxx.toString());
								if (listLYXMXX != null && listLYXMXX.size() > 0) {
									Map lyxmxx = listLYXMXX.get(0);
									String lyfilenumber = lyxmxx.get("PROJECT_ID").toString();
									String lyywlsh = lyxmxx.get("YWLSH").toString();
									StringBuilder proinst = new StringBuilder();
									proinst.append("select * from ");
									proinst.append(Common.WORKFLOWDB);
									proinst.append("Wfi_ProInst where  file_number ='");
									proinst.append(lyfilenumber);
									proinst.append("'");
									List<Map> proinsts = commonDao.getDataListByFullSql(proinst.toString());
									if (proinsts.size() > 0 && proinsts != null) {
										Map wfi_proinst = proinsts.get(0);
										String proinst_status = wfi_proinst.get("PROINST_STATUS").toString();
										item.put("proinst_status", proinst_status);
										item.put("lyywlsh", lyywlsh);
										
									}
								}

							}

						}
					}
				}
				item.put("djlx",djlx);
			}

			return item;
		} else {
			return new HashMap();
		}

	}

	public long GetWaitDossierCount(String staffid) {
		long Result = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("v_projectlist");
		sb.append(" where actstaff='" + staffid + "'");
		sb.append(" and(staff_id is null or staff_id='" + staffid + "') ");
		sb.append(" and actinst_name='归档' and actinst_status in (1,2)");
		Result = commonDao.getCountByFullSql(sb.toString());
		return Result;

	}

	public Map worlflowDossier(String actinstid, String file_number) {
		Map result = new HashMap();
		List<Tree> materils = ProMaterService.GetMaterDataTree(file_number, "true");
		result.put("project", GetProjectInfo(actinstid));
		result.put("materil", materils);
		return result;

	}

	public List<Map> getAllProjectByActName(String actinstname) {
		List<Map> maps = new ArrayList<Map>();
		List<Wfi_ProInst> insts = commonDao.getDataList(Wfi_ProInst.class, "select * from " + Common.WORKFLOWDB + "wfi_proinst");
		if (insts != null && insts.size() > 0) {
			for (Wfi_ProInst inst : insts) {
				List<Wfi_ActInst> actinsts = commonDao.getDataList(Wfi_ActInst.class, "Wfi_ActInst", " actinst_name='" + actinstname + "' and proinst_id='" + inst.getProinst_Id() + "' order by actinst_start desc");
				if (actinsts != null && actinsts.size() > 0) {
					Map map = new HashMap();
					map.put("FILE_NUMBER", inst.getFile_Number());

				}
			}
		}
		return maps;
	}

	public Message getAllProjectByActName(String actdef_name, String status) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("1>0 ");
		if (actdef_name != null && !actdef_name.equals("")) {
			str.append(" and ACTINST_NAME in (");
			str.append(actdef_name);
			str.append(" )");
		}
		if (status.equals("8")) {
			status = "1,2";
		}
		if (status != null && !"".equals(status)) {
			str.append(" and ACTINST_STATUS in (");
			str.append(status);
			str.append(")");
		}
		long tatalCount = commonDao.getCountByFullSql(" from (select  row_number() over(partition by file_number order by actinst_start desc" + ") rn,file_number, project_name, PROINST_START,proinst_status,Staff_Name from " + Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString() + ") where rn=1  ");
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { //
			str.append(" order by ACTINST_START  desc");
			msg.setRows(commonDao.getDataListByFullSql("select * from (select  row_number() over(partition by file_number order by actinst_start desc" + ") rn,file_number,actinst_start,proinst_id,prodef_name,project_name,proinst_status,actinst_id,PROINST_START,Staff_Name,actinst_name,prolsh,actinst_status,codeal,urgency,actinst_willfinish,proinst_willfinish,proouttime,operation_type   from " + Common.WORKFLOWDB + "V_PROJECTLIST where " + str.toString() + " ) where rn=1 "));
		}
		return msg;
	}

	public Message getBatchBoardSearch(String operaStaffString, String actinstname, int protype, String searchvalue, int page, int parseInt) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		// _________________________过滤驳回产生的一些重复的件—————————————————————————
		StringBuilder tmpTable = new StringBuilder();
		StringBuilder xmxxconfig = new StringBuilder();
		str.append(" ACTSTAFF='");
		str.append(operaStaffString);
		str.append("' and ACTINST_STATUS in (1,2)");
		if (actinstname != null && !actinstname.equals("")) {
			str.append(" and actinst_name in (");
			str.append(actinstname);
			str.append(")");
		}
		if (!searchvalue.equals("")) {
			str.append("and instr(" + "YWLSH" + ",'" + searchvalue + "')>0 ");

		}
		str.append(" and sfdb='0' ");
		tmpTable.append(Common.WORKFLOWDB + "v_projectlist" + " " + "v_p" + " ");
		xmxxconfig.append("left join BDCK.BDCS_XMXX xmxx on v_p.file_number=xmxx.project_id ");
		long tatalCount = commonDao.getCountByFullSql(" from " + tmpTable.toString() + xmxxconfig + " where " + str.toString());
		msg.setTotal(tatalCount);
		List<Map> list = null;
		if (tatalCount > 0) {
			list = commonDao.getDataListByFullSql("select * from " + tmpTable.toString() + xmxxconfig + " where " + str.toString() + " order by  YWLSH asc");
		}
		msg.setRows(list);
		return msg;
	}

	/**
	 * 获取批量发证信息
	 */

	@SuppressWarnings({ "null", "rawtypes", "unchecked" })
	public Message getBatchCertSearch(String operaStaffString, String actinstname, int protype, String searchvalue, int page, int parseInt) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		// _________________________过滤驳回产生的一些重复的件—————————————————————————
		StringBuilder tmpTable = new StringBuilder();
		StringBuilder userstr = new StringBuilder();
		StringBuilder xmxxconfig = new StringBuilder();
		String username = null;
		str.append(" ACTSTAFF='");
		str.append(operaStaffString);
		str.append("' and ACTINST_STATUS in (1,2)");
		if (actinstname != null && !actinstname.equals("")) {
			str.append(" and actinst_name in (");
			str.append(actinstname);
			str.append(")");
		}
		if(searchvalue != null&&!searchvalue.equals("")){
			str.append(" and prolsh='"+searchvalue+"'");
		}
		tmpTable.append(Common.WORKFLOWDB + "v_projectlist" + " " + "v_p" + " ");
		xmxxconfig.append("left join BDCK.BDCS_XMXX xmxx on v_p.file_number=xmxx.project_id ");
		long tatalCount = commonDao.getCountByFullSql(" from " + tmpTable.toString() + xmxxconfig + " where " + str.toString());
		msg.setTotal(tatalCount);
		List<Map> list = null;
		List<Map> newlist = new ArrayList();
		userstr.append("SMWB_FRAMEWORK.t_user");
		List<Map> users = commonDao.getDataListByFullSql("select * from " + userstr.toString() + " " + "where id='" + operaStaffString + "'");
		if (users.size() > 0) {
			Map usermap = users.get(0);
			username = usermap.get("USERNAME").toString();
		}
		List listpage = new ArrayList();
		if (tatalCount > 0) {
			list = commonDao.getDataListByFullSql("select * from " + tmpTable.toString() + xmxxconfig + " where " + str.toString() + " order by  proinst_weight desc");
			if (list.size() > 0) {
				List<DJFZXX> listfzxxs = null;
				for (int i = 0; i < list.size(); i++) {
					@SuppressWarnings("rawtypes")
					Map mapi = list.get(i);
					if (mapi.containsKey("XMBH")) {
						String xmbh = mapi.get("XMBH").toString();
						String ywlsh = mapi.get("YWLSH").toString();
						com.supermap.wisdombusiness.web.ui.Page pagei = zsService.GetPagedFZList(xmbh, page, parseInt);
						List<DJFZXX> zsList = (List<DJFZXX>) pagei.getResult();
						listfzxxs = new ArrayList();
						if (zsList.size() > 0) {
							for (int z = 0; z < zsList.size(); z++) {
								DJFZXX fzxx = zsList.get(z);
								fzxx.setYwlsh(ywlsh);
								if (fzxx.getCZLX().equals("发证")) {
									listfzxxs.add(fzxx);
								} else {
									continue;
								}

							}
						}

					}
					listpage.add(listfzxxs);
				}
				if (listpage.size() > 0) {
					for (int j = 0; j < listpage.size(); j++) {
						List<DJFZXX> onefzxx = (List<DJFZXX>) listpage.get(j);
						if (onefzxx.size() > 0) {
							Object[] objs = onefzxx.toArray();
							for (int q = 0; q < objs.length; q++) {
								Map<String, String> mapdata = new HashMap();
								mapdata = toHashMap(objs[q]);
								mapdata.put("staffname", username);
								newlist.add(mapdata);
							}
						}
					}
				}
			}
		}

		msg.setRows(newlist);
		return msg;
	}
	
	/**
	 * 获取批量发证信息
	 * created by liuxingrong 2017-11-8
	 */

	@SuppressWarnings({ "null", "rawtypes", "unchecked" })
	public Message getNewBatchCertSearch(String operaStaffString, String actinstname, int protype, String searchvalue, int page, int parseInt,String pronames,String zl,String lshstart ,String lshend) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		// _________________________过滤驳回产生的一些重复的件—————————————————————————
		StringBuilder tmpTable = new StringBuilder();
		StringBuilder userstr = new StringBuilder();
		StringBuilder xmxxconfig = new StringBuilder();
		String username = null;
		str.append(" ACTSTAFF='");
		str.append(operaStaffString);
		str.append("' and ACTINST_STATUS in (1,2)");
		if (actinstname != null && !actinstname.equals("")) {
			str.append(" and actinst_name in (");
			str.append(actinstname);
			str.append(")");
		}
		if(searchvalue != null&&!searchvalue.equals("")){
			str.append(" and prolsh='"+searchvalue+"'");
		}
		if (pronames != null && pronames.split(",").length == 1&&!pronames.equals("")) {
			if (pronames.endsWith("'")) {
				pronames = pronames.substring(1, pronames.length() - 1);
			}
			pronames = pronames.replace("-", ",");
			List<Wfi_ProInst> list1 = commonDao.findList(Wfi_ProInst.class, " prodef_name = '"+pronames+"'");
			if(null!=list1&&list1.size()>0){
				str.append( " and prodef_name = '"+pronames+"' ");
			}else{
				str.append( " and prodef_name like '"+pronames+"%' ");
			}

		} else if (pronames != null && !pronames.equals("")) {
			str.append(" and prodef_name in ("+pronames+" )");
		}
		if (lshstart != null && !lshstart.equals("")) {
			str.append(" and PROLSH>='"+lshstart+"'");
		}
		// 流水起始号
		if (lshend != null && !lshend.equals("")) {
			str.append(" and PROLSH<='"+lshend+"'");
		}
		tmpTable.append(Common.WORKFLOWDB + "v_projectlist" + " " + "v_p" + " ");
		xmxxconfig.append("left join BDCK.BDCS_XMXX xmxx on v_p.file_number=xmxx.project_id ");
		long tatalCount = commonDao.getCountByFullSql(" from " + tmpTable.toString() + xmxxconfig + " where " + str.toString());
		msg.setTotal(tatalCount);
		List<Map> list = null;
		List<Map> newlist = new ArrayList();
		userstr.append("SMWB_FRAMEWORK.t_user");
		List<Map> users = commonDao.getDataListByFullSql("select * from " + userstr.toString() + " " + "where id='" + operaStaffString + "'");
		if (users.size() > 0) {
			Map usermap = users.get(0);
			username = usermap.get("USERNAME").toString();
		}
		List listpage = new ArrayList();
		if (tatalCount > 0) {
			list = commonDao.getDataListByFullSql("select * from " + tmpTable.toString() + xmxxconfig + " where " + str.toString() + " order by  proinst_weight desc",page,parseInt);
			if (list.size() > 0) {
				List<DJFZXX> listfzxxs = null;
				for (int i = 0; i < list.size(); i++) {
					@SuppressWarnings("rawtypes")
					Map mapi = list.get(i);
					if (mapi.containsKey("XMBH")) {
						String xmbh = mapi.get("XMBH").toString();
						String ywlsh = mapi.get("YWLSH").toString();
						com.supermap.wisdombusiness.web.ui.Page pagei = zsService.GetPagedFZList(xmbh, 0, 10000);
						List<DJFZXX> zsList = (List<DJFZXX>) pagei.getResult();
						listfzxxs = new ArrayList();
						if (zsList.size() > 0) {
							for (int z = 0; z < zsList.size(); z++) {
								DJFZXX fzxx = zsList.get(z);
								fzxx.setYwlsh(ywlsh);
								if (fzxx.getCZLX().equals("发证")) {
									listfzxxs.add(fzxx);
								} else {
									continue;
								}

							}
						}

					}
					//listpage.add(listfzxxs);
				}
				if (listpage.size() > 0) {
					for (int j = 0; j < listpage.size(); j++) {
						List<DJFZXX> onefzxx = (List<DJFZXX>) listpage.get(j);
						if (onefzxx.size() > 0) {
							Object[] objs = onefzxx.toArray();
							for (int q = 0; q < objs.length; q++) {
								Map<String, String> mapdata = new HashMap();
								mapdata = toHashMap(objs[q]);
								mapdata.put("staffname", username);
								newlist.add(mapdata);
							}
						}
					}
				}
			}
		}

		msg.setRows(newlist);
		return msg;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map toHashMap(Object object) {
		Map map = new HashMap();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.fromObject(object);
		Iterator it = jsonObject.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value;
			if (jsonObject.get(key) instanceof JSONNull) {
				value = "";
			} else {
				value = jsonObject.get(key).toString();

			}

			map.put(key, value);
		}
		return map;
	}

	/**
	 * 挂起项目
	 * 
	 * @param actinstid
	 * @param msg
	 * @return
	 */
	public boolean SetHangUp(String actinstid, String msg) {
		boolean Result = false;
		if (actinstid != null && !actinstid.equals("")) {
			Wfi_ActInst inst = _smActInst.GetActInst(actinstid);
			if (inst != null) {
				User user = smStaff.getCurrentWorkStaff();
				inst.setHangup_Time(new Date());
				if(null==inst.getHangpausetime())
					inst.setHangpausetime(inst.getHangup_Time());
				inst.setHangup_Staff_Id(user.getId());
				inst.setHangup_Staff_Name(user.getUserName());
				inst.setMsg(msg);
				inst.setInstance_Type(WFConst.Instance_Type.Instance_HangingUp.value);
				inst.setOperation_Type(WFConst.Operate_Type.ApplyHungUp.value + "");
				//TODO:挂起设置最新的活动实例状态
				Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
				if(proinst!=null){
					proinst.setOperation_Type_Nact(inst.getOperation_Type());
					proinst.setMsg(inst.getMsg());
				}
				commonDao.update(inst);
				commonDao.flush();
				Result = true;
			}
		}
		return Result;

	}

	public boolean SetHangDown(String actinstid) {
		boolean Result = false;
		if (actinstid != null && !actinstid.equals("")) {
			Wfi_ActInst inst = _smActInst.GetActInst(actinstid);
			Wfd_Actdef actdef = _smActInst.GetActDef(actinstid);
			Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
			Wfd_Prodef prodef = commonDao.get(Wfd_Prodef.class, actdef.getProdef_Id());
			if (inst != null) {
				User user = smStaff.getCurrentWorkStaff();
				long bettime = smHoliday.ComputerDiff(inst.getHangup_Time(),inst.getHangdowm_Time());
				inst.setHangdowm_Time(new Date());
				inst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
				inst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value + "");
				GregorianCalendar cal2 = new GregorianCalendar();
				cal2.setTime(new Date());
				if(null!=inst.getHangpausetime())cal2.setTime(inst.getHangpausetime());
				inst.setHangpausetime(smHoliday.addDateByWorkDay(cal2, (double)bettime));
				//TODO:解挂后更新环节实例中当前环节的信息：
				proinst.setOperation_Type_Nact(inst.getOperation_Type());
				if("1".equals(StringHelper.formatObject(prodef.getHouse_Status()))){
					// 首先计算当前项目可用的工作日还有几天 , 当前解挂的日日期加上工作日计算期望时间
					if (actdef.getActdef_Time() != null) {
						double diff = (double)(smHoliday.ComputerDiff(inst.getHangup_Time(), inst.getActinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
						cal2.setTime(new Date());
						
						if(diff > 0){
							try {
							GregorianCalendar date = _smActInst.getTime(actdef);
							inst.setActinst_Willfinish(date.getTime());
							} catch (ParseException e) {
								e.printStackTrace();
							}
//							Date date = smHoliday.addDateByWorkDay2(cal2, diff);
//							inst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
						}
						//TODO:解挂后更新环节实例中当前环节的信息：
						if(proinst!=null){
							proinst.setActinst_Willfinish(inst.getActinst_Willfinish());
						}
						commonDao.update(inst);
					}
					if (proinst.getProinst_Time() != null) {
						double diff = (double)(smHoliday.ComputerDiff(inst.getHangup_Time(), proinst.getProinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
						cal2.setTime(new Date());
						if(diff > 0){
							proinst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
						}
						commonDao.update(proinst);
					}
				}else{
					// 首先计算当前项目可用的工作日还有几天 , 当前解挂的日日期加上工作日计算期望时间
					if (actdef.getActdef_Time() != null) {
						double diff = (double)(smHoliday.ComputerDiff(inst.getHangup_Time(), inst.getActinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
						cal2.setTime(new Date());
						
						if(diff > 0){
							inst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
						}
						//TODO:解挂后更新环节实例中当前环节的信息：
						if(proinst!=null){
							proinst.setActinst_Willfinish(inst.getActinst_Willfinish());
						}
						commonDao.update(inst);
					}
					if (proinst.getProinst_Time() != null) {
						double diff = (double)(smHoliday.ComputerDiff(inst.getHangup_Time(), proinst.getProinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
						cal2.setTime(new Date());
						if(diff > 0){
							proinst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
						}
						commonDao.update(proinst);
					}
				}
				
				// 计算挂起时间
				commonDao.flush();
				Result = true;
			}
		}
		return Result;

	}
	
	/**
	 * 挂起项目海口项目，电子政务调用挂起接口执行挂起操作
	 * 
	 * @param actinstid
	 * @param msg
	 * @return
	 */
	public boolean SetHangUpHK(String actinstid, String msg,String staffid) {
		boolean Result = false;
		if (actinstid != null && !actinstid.equals("")) {
			Wfi_ActInst inst = _smActInst.GetActInst(actinstid);
			if (inst != null) {
				User user = commonDao.get(User.class, staffid);
				inst.setHangup_Time(new Date());
				inst.setHangup_Staff_Id(user.getId());
				inst.setHangup_Staff_Name(user.getUserName());
				inst.setMsg(msg);
				inst.setInstance_Type(WFConst.Instance_Type.Instance_HangingUp.value);
				inst.setOperation_Type(WFConst.Operate_Type.ApplyHungUp.value + "");
				//TODO:挂起设置最新的活动实例状态
				Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
				if(proinst!=null){
					proinst.setOperation_Type_Nact(inst.getOperation_Type());
					proinst.setMsg(inst.getMsg());
				}
				commonDao.update(inst);
				commonDao.flush();
				Result = true;
			}
		}
		return Result;

	}
	/***
	 * 海口市电子政务调用解挂接口执行的service方法
	 * 
	 * */
	public boolean SetHangDownHK(String actinstid,String staffid,String downtime) {
		   boolean Result = false;
		if (actinstid != null && !actinstid.equals("")) {
			Wfi_ActInst inst = _smActInst.GetActInst(actinstid);
			Wfd_Actdef actdef = _smActInst.GetActDef(actinstid);
			Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
			if (inst != null) {
				User user = commonDao.get(User.class, staffid);
				inst.setHangdowm_Time(new Date());
				inst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
				inst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value + "");
				//TODO:解挂后更新环节实例中当前环节的信息：
				proinst.setOperation_Type_Nact(inst.getOperation_Type());
				// 首先计算当前项目可用的工作日还有几天
				// 当前解挂的日日期加上工作日计算期望时间
				GregorianCalendar cal2 = new GregorianCalendar();
				if (actdef.getActdef_Time() != null) {
					double diff = (double)(smHoliday.ComputerDiff(inst.getHangup_Time(), inst.getActinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
					//cal2.setTime(new Date());
					cal2.setTime(new Date());
					inst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
					if(proinst!=null){
						proinst.setActinst_Willfinish(inst.getActinst_Willfinish());
					}
					commonDao.update(inst);
				}
			    Date willfinish = new Date();
				if(downtime!=null&&!downtime.equals("")){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						willfinish=df.parse(downtime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (proinst.getProinst_Time() != null) {
					double diff = (double)(smHoliday.ComputerDiff(inst.getHangup_Time(), proinst.getProinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
					//cal2.setTime(new Date());
					cal2.setTime(new Date());
					//smHoliday.addDateByWorkDay(cal2, diff)
					proinst.setProinst_Willfinish(willfinish);
					commonDao.update(proinst);
				}
				// 计算挂起时间
				commonDao.flush();
				Result = true;
			}
		}
		return Result;

	}

	/***
	 * 通过项目编号获取bdcqzh
	 * 
	 * @param xmbh
	 * @return
	 */
	public String getQZHORZMHByXMBH(String xmbh) {
		List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
		String bdcqzh = null;
		if (list.size() > 0) {
			BDCS_QL_GZ ql = (BDCS_QL_GZ) list.get(0);
			if (!StringHelper.isEmpty(ql.getBDCQZH())) {
				bdcqzh = ql.getBDCQZH();
			} else {

			}

		}
		return bdcqzh;
	}

	/**
	 * 通过业务流水号获取对应的项目编号
	 * 
	 * @param ywlsh
	 * @return
	 */
	public String getXMBHByYWLSH(String ywlsh) {
		String xmbh = null;
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("BDCK.BDCS_XMXX");
		if (!StringHelper.isEmpty(ywlsh)) {
			List<Map> xmxxs = commonDao.getDataListByFullSql("select * from " + sBuilder.toString() + " " + " where " + " YWLSH='" + ywlsh + "'");
			if (xmxxs.size() > 0) {
				for (Map m : xmxxs) {
					if (!StringHelper.isEmpty(m.get("XMBH").toString())) {
						xmbh = m.get("XMBH").toString();
					} else {

					}
				}

			}

		}

		return xmbh;
	}

	/**
	 * 批量收费的项目信息
	 * 
	 * @param operaStaffString
	 * @param actinstname
	 * @param protype
	 * @param searchvalue
	 * @param page
	 * @param parseInt
	 * @return
	 */
	public Message getBatchChargeSearch(String operaStaffString, String actinstname, int protype, String searchvalue, int page, int parseInt,String pronames,String zl,String lshstart,String lshend) {

		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		// _________________________过滤驳回产生的一些重复的件—————————————————————————
		StringBuilder tmpTable = new StringBuilder();
		StringBuilder xmxxconfig = new StringBuilder();
		str.append(" ACTSTAFF='");
		str.append(operaStaffString);
		str.append("' and ACTINST_STATUS in (1,2)");
		if (actinstname != null && !actinstname.equals("")) {
			str.append(" and actinst_name in (");
			str.append(actinstname);
			str.append(")");
		}
		if(searchvalue != null&&!searchvalue.equals("")){
			str.append(" and prolsh='"+searchvalue+"'");
		}
		if (pronames != null && pronames.split(",").length == 1&&!pronames.equals("")) {
			if (pronames.endsWith("'")) {
				pronames = pronames.substring(1, pronames.length() - 1);
			}
			pronames = pronames.replace("-", ",");
			List<Wfi_ProInst> list1 = commonDao.findList(Wfi_ProInst.class, " prodef_name = '"+pronames+"'");
			if(null!=list1&&list1.size()>0){
				str.append( " and prodef_name = '"+pronames+"' ");
			}else{
				str.append( " and prodef_name like '"+pronames+"%' ");
			}

		} else if (pronames != null && !pronames.equals("")) {
			str.append(" and prodef_name in ("+pronames+" )");
		}
		if (lshstart != null && !lshstart.equals("")) {
			str.append(" and PROLSH>='"+lshstart+"'");
		}
		// 流水起始号
		if (lshend != null && !lshend.equals("")) {
			str.append(" and PROLSH<='"+lshend+"'");
		}
		tmpTable.append(Common.WORKFLOWDB + "v_projectlist" + " " + "v_p" + " ");
		xmxxconfig.append("left join BDCK.BDCS_XMXX xmxx on v_p.file_number=xmxx.project_id ");
		long tatalCount = commonDao.getCountByFullSql(" from " + tmpTable.toString() + xmxxconfig + " where " + str.toString());
		msg.setTotal(tatalCount);
		List<Map> list = null;
		if (tatalCount > 0) {
			list = commonDao.getDataListByFullSql("select * from " + tmpTable.toString() + xmxxconfig + " where " + str.toString() + " order by  proinst_weight desc",page,parseInt);
			if (list.size() > 0) {
				msg.setRows(list);
			} else {

			}
		}

		return msg;
	}

	public Wfi_ProInst getInfoByLsh(String lsh) {
		List<Wfi_ProInst> insts = commonDao.getDataList(Wfi_ProInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ProInst where prolsh='" + lsh + "'");
		if (insts != null && insts.size() > 0) {
			return insts.get(0);
		} else {
			return null;
		}
	}
	
	
	
	public List<Map<String,String>> GetProdefTreeByMapping(String mapping){
		List<Map<String,String>>  processlist = new ArrayList<Map<String,String>>();
		//递归查询所有的大类Prodef_Mapping
		//查询所有的流程定义
		List<Wfd_Prodef>  prodeflist = commonDao.getDataList(Wfd_Prodef.class,
				"select * from "+Common.WORKFLOWDB+"Wfd_Prodef where Prodef_Mapping = '"+mapping+"'");
		String conditions = "";
		String name = "";
		if(prodeflist.size()>0){
			for(int i=0;i<prodeflist.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				name=operationService.getProName (prodeflist.get(i).getProdefclass_Id());
				String str = name.substring(0, name.lastIndexOf(","));
				String[] straar = str.split(",");
				String nameend ="";
				for (int k=straar.length-1;k>=0;k--){
					nameend+=straar[k]+"-->";
				}
				nameend+=prodeflist.get(i).getProdef_Name();
				map.put("name", nameend);
				map.put("id", prodeflist.get(i).getProdef_Id());
				processlist.add(map);
			/*	if(i==prodeflist.size()-1){
					conditions +="'"+prodeflist.get(i).getProdefclass_Id()+"'";
				}else{
					conditions +="'"+prodeflist.get(i).getProdefclass_Id()+"',";
				}*/
			}
		}
	/*	List<Wfd_ProClass> list = commonDao.getDataList(Wfd_ProClass.class,
				"select * from "+Common.WORKFLOWDB+"Wfd_ProClass where  PRODEFCLASS_ID in ( "+conditions+") ");
		if(list.size()>0){
			for(int j=0;j<list.size();j++){
				treelist=getTreePro (list.get(j));
			}
		}*/
		return processlist;
	}
	
	
	public List<TreeInfo> getTreePro (Wfd_ProClass ProClass){
		StringBuilder noWhereSql = new StringBuilder();
		if (ProClass == null) {
			noWhereSql.append("Prodefclass_Pid is null or Prodefclass_Pid='0' ");
		} else {
			noWhereSql.append("Prodefclass_id='");
			noWhereSql.append(ProClass.getProdefclass_Pid());
			noWhereSql.append("'");
		}
		noWhereSql.append(" order by Prodefclass_Index");
		List<Wfd_ProClass> list = commonDao.findList(Wfd_ProClass.class, noWhereSql.toString());
		List<TreeInfo> TreeList = new ArrayList<TreeInfo>();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Wfd_ProClass _ProClass = list.get(i);
				TreeInfo tree = new TreeInfo();
				tree.setId(_ProClass.getProdefclass_Id());
				tree.setText(_ProClass.getProdefclass_Name());
				tree.setType("catalog");
				tree.setState("closed");
				tree.children = getTreePro(list.get(i));
				TreeList.add(tree);
			}

		}
		if (ProClass != null) {
			StringBuilder noWhereSql2 = new StringBuilder();
			noWhereSql2.append("Prodefclass_Id='");
			noWhereSql2.append(ProClass.getProdefclass_Id());
			noWhereSql2.append("' order by Prodef_Index");
			List<Wfd_Prodef> list2 = commonDao.findList(Wfd_Prodef.class, noWhereSql2.toString());
			for (int j = 0; j < list2.size(); j++) {
				Wfd_Prodef _Prodef = list2.get(j);
				TreeInfo tree = new TreeInfo();
				tree.setId(_Prodef.getProdef_Id());
				tree.setText(_Prodef.getProdef_Name());
				tree.setType("data");
				TreeList.add(tree);
			}
		}
		return TreeList;
	}
	/**
	 * 海口项目受理 添加申請人
	 * @author JHX
	 * */
    public  SmObjInfo addSqr (BDCS_SQR sqr){
    	SmObjInfo info = new SmObjInfo();
    	try{
    		commonDao.save(sqr);
    		info.setDesc("申请人创建成功");
    		commonDao.flush();
    	}catch(Exception e){
    		e.printStackTrace();
    		info.setDesc("申请人创建失败");
    	}
    	return info;
    }
	
	
	
	/**
	 * 项目全局查询导出
	 * */
	public Message getAllProjectInfo(String key, String status, String start,
			String end, int CurrentPageIndex, int pageSize, String actdef_name,
			String order, String prodefname, String staff, String sqr,
			String urgency, String outtime, String passback) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("1>0 ");
		if (key != null && !"".equals(key)) {
			str.append(" and (PROJECT_NAME like '%");
			str.append(key);
			str.append("%' or FILE_NUMBER like '%");
			str.append(key);
			str.append("%' ");
			str.append(" or PROLSH='");
			str.append(key);
			str.append("'");

			str.append(")");
			// 支持权利人查询

		}
		if (actdef_name != null && !actdef_name.equals("")) {
			str.append(" and ACTINST_NAME in (");
			str.append(actdef_name);
			str.append(" )");
		}
		if (status != null && !"".equals(status)) {
			str.append(" and ACTINST_STATUS in (");
			str.append(status);
			str.append(")");
		}
		if (start != null && !"".equals(start)) {
			str.append(" and ACTINST_END >= to_date('");
			str.append(start);
			str.append("','yyyy-MM-dd')");
		}
		if (end != null && !"".equals(end)) {
			str.append(" and ACTINST_END < to_date('");
			str.append(end);
			str.append("','yyyy-MM-dd')");
		}
		// 流程名称
		if (prodefname != null && !"".equals(prodefname)) {
			str.append(" and prodef_name in (");
			str.append(prodefname);
			str.append(")");
		}
		// 办理人员
		if (staff != null && !"".equals(staff) && status.contains("1")) {
			str.append(" and actstaffname like '%");
			str.append(staff);
			str.append("%' and actinst_status not in (2,3)");
		} else if (staff != null && !"".equals(staff)) {
			str.append(" and staff_name like '%");
			str.append(staff);
			str.append("%'");
		}
		if (urgency != null && !urgency.equals("")) {
			str.append(" and urgency=");
			str.append(urgency);
		}
		if (outtime != null && !outtime.equals("")) {
			str.append(" and proouttime>0");
		}
		if (passback != null && !passback.equals("")) {
			str.append(" and operation_type=");
			str.append(WFConst.Operate_Type.PRODUCT_anglesPicture.value);
		}
		if (sqr != null && !"".equals(sqr)) {
			if (isChinese(sqr)) {
				str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%"
						+ sqr
						+ "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=v_projectlist.FILE_NUMBER) ");
			}
		}

		long tatalCount = commonDao
				.getCountByFullSql(" from (select  row_number() over(partition by file_number order by actinst_start "
						+ order
						+ ") rn,file_number, project_name, PROINST_START,proinst_status,Staff_Name from "
						+ Common.WORKFLOWDB
						+ "V_PROJECTLIST where "
						+ str.toString() + ") where rn=1  ");
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { //
			str.append(" order by ACTINST_START  " + order);
			msg.setRows(commonDao
					.getDataListByFullSql(
							" select dbr,bdcdyh,djsj ,slsj ,ywlsh ,Staff_Name,PRODEF_NAME,PROLSH from (select * from bdck. bdcs_ql_gz a left join bdck.bdcs_xmxx  b on a.xmbh = b.xmbh) c left join（select * from (select  row_number() over(partition by file_number order by actinst_start "
									+ order
									+ ") rn,file_number,actinst_start,proinst_id,prodef_name,project_name,proinst_status,actinst_id,PROINST_START,Staff_Name,actinst_name,prolsh,actinst_status,codeal,urgency,actinst_willfinish,proinst_willfinish,proouttime,operation_type   from "
									+ Common.WORKFLOWDB
									+ "V_PROJECTLIST where "
									+ str.toString()
									+ " ) where rn=1) d on c.project_id = d.file_number WHERE d.file_number IS NOT NULL"));
		}
		return msg;
	}
	public Message getAllProjectInfo(String key, String status, String start,
			String end, int CurrentPageIndex, int pageSize, String actdef_name,
			String prodefname, String staff, String sqr, String urgency,
			String outtime, String passback) {
		return getAllProjectInfo(key, status, start, end, CurrentPageIndex,
				pageSize, actdef_name, "desc", prodefname, staff, sqr, urgency,
				outtime, passback);
	}
	/**
	 * @author JHX
	 * @DATE:2016-08-12
	 * 根据受理编号获取项目最新的状态
	 * */
	public String getProcessState(String slbh){
		String result="1";
		if(slbh!=null&&!slbh.equals("")){
			List<Wfi_ProInst> list =
					commonDao.getDataList(Wfi_ProInst.class, "select * from "+Common.WORKFLOWDB+"Wfi_ProInst where prolsh='"+slbh+"'");
			String proinstid =null;
			if(list.size()>0){
				list.get(0);
				proinstid = list.get(0).getProinst_Id();
			}
			List<Wfi_ActInst> listactinst =null;
			if(proinstid!=null){
				listactinst=commonDao.getDataList(Wfi_ActInst.class, 
						"select * from "+Common.WORKFLOWDB+"Wfi_Actinst where proinst_id='"+proinstid+"' order by actinst_start desc");
			}
			if(listactinst.size()>0){
				Wfi_ActInst actinst=listactinst.get(0);
				if(actinst.getOperation_Type().equals("40")&&
						actinst.getInstance_Type()==4){
					result= "4";
				}
			}
		}
		return result;
	}
	/**
	 * @author JHX
	 * @DATE:2016-08-12
	 * 根据受理编号获取项目最新的状态
	 * */
	public String getAllHangupProject(){
		List<Wfi_ActInst> actinstlist=commonDao.getDataList(Wfi_ActInst.class, 
				"select * from "+Common.WORKFLOWDB+"Wfi_Actinst where ACTINST_STATUS='2' and OPERATION_TYPE='40' order by proinst_id");
		StringBuilder sb = new StringBuilder();
		Map<String,String> map = new HashMap<String,String>();
		if(actinstlist.size()>0){
			sb.append("in ( ");
			Wfi_ActInst actinst = null;
			for (int i = 0; i < actinstlist.size(); i++) {
				actinst=actinstlist.get(i);
				map.put(actinst.getProinst_Id(), actinst.getActinst_Id());
				if(i==(actinstlist.size()-1)){
					sb.append("'"+actinst.getProinst_Id()+"')");
				}else{
					sb.append("'"+actinst.getProinst_Id()+"',");
				}
			}
		 }
		 //查询所有的流程实例
		 JSONArray arr = new JSONArray();
		 com.alibaba.fastjson.JSONObject jsonobj = null;
		if(!StringHelper.isEmpty(sb.toString())){
			List<Wfi_ProInst> listproinst = 
			commonDao.getDataList(Wfi_ProInst.class, 
					"select * from "+Common.WORKFLOWDB+"Wfi_ProInst where proinst_id "+sb.toString()+" order by proinst_id");
			Wfi_ProInst pro = null;
			for(int j=0,n=listproinst.size();j<n;j++){
				pro= listproinst.get(j);
		    	jsonobj = new  com.alibaba.fastjson.JSONObject();
		    	jsonobj.put("slbh", pro.getProlsh());
		    	jsonobj.put("actinstid", map.get(pro.getProinst_Id()));
		    	arr.add(jsonobj);
		    }
		   
		}
		return arr.toJSONString();
	}
	/**
	 * @author JHX
	 * @DATE:2016-08-12
	 * 海口电子政务：挂起审批未通过，需要更改环节实例状态，更新未通过原因
	 * */
	public boolean setActinstProperty(String actinstid,String msg){
		boolean flag = false;
		if(actinstid!=null&&!actinstid.equals("")){
			Wfi_ActInst actinst = commonDao.get(Wfi_ActInst.class, actinstid);
			if(actinst!=null){
				actinst.setNotAppPassRes(msg);
				actinst.setIsApplyHandup(0);
				Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
				if(proinst!=null){
					proinst.setIsApplyHangup(0);
				}
				commonDao.update(proinst);
				commonDao.update(actinst);
				commonDao.flush();
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * @author duff
	 * @DATE
	 * 取消未通过标记
	 * */
	public boolean DeleNotPass(String actinstid){
		boolean flag = false;
		if(actinstid!=null&&!actinstid.equals("")){
			Wfi_ActInst actinst = commonDao.get(Wfi_ActInst.class, actinstid);
			if(actinst!=null){
				actinst.setNotAppPassRes("");
				actinst.setIsApplyHandup(null);
				commonDao.flush();
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 
	 * @作者 蓝剑锋修改
	 * @创建时间 2017年5月4日上午11:23:11
	 * @param qzh
	 * @param qlrmc
	 * @param cslx
	 * @param rwlsh
	 * @param ywmc
	 * @param username
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getAccobj(String qzh,String qlrmc,String cslx,String rwlsh,String ywmc,String username,HttpServletRequest request,HttpServletResponse response)throws Exception {
		SmObjInfo rf = null;
		StringBuilder strh = new StringBuilder();
		StringBuilder strtd = new StringBuilder();
		String bdcdylx="";
		String bdcdyid="",status="fail";
		strh.append("select djdy.bdcdyid,h.bdcdylx from bdck.bdcs_djdy_xz djdy left join ( select hxz.bdcdyid as bdcdyid,'031' as bdcdylx from bdck.bdcs_h_xz hxz ")
		.append("union select hxzy.bdcdyid as bdcdyid,'032' as bdcdylx from bdck.bdcs_h_xzy hxzy)h  on djdy.bdcdyid=h.bdcdyid")
		.append(" and h.bdcdylx=djdy.bdcdylx left join bdck.bdcs_ql_xz ql on djdy.djdyid=ql.djdyid")
		.append(" left join bdck.bdcs_qlr_xz qlr on ql.qlid=qlr.qlid where h.bdcdyid is not null and qlr.qlrmc='").append(qlrmc)
		.append("' and qlr.bdcqzh='").append(qzh).append("'");
		List<Map> maps = commonDao.getDataListByFullSql(strh.toString());
		if(maps!=null&&maps.size()>0){
			bdcdylx=maps.get(0).get("BDCDYLX")==null?"":maps.get(0).get("BDCDYLX").toString();
			bdcdyid=maps.get(0).get("BDCDYID")==null?"":maps.get(0).get("BDCDYID").toString();
			rf=getParam(rwlsh, qlrmc, qzh, bdcdyid, bdcdylx, cslx,ywmc,username, request);
			if(rf!=null){
				status= "success";
			}
			return status;
		}
		strtd.append("select ql.ywh,qlr.qlrmc,djdy.bdcdylx,shy.bdcdyid from bdck.bdcs_djdy_xz djdy left join bdck.bdcs_shyqzd_xz shy ")
		.append(" on djdy.bdcdyid=shy.bdcdyid left join bdck.bdcs_ql_xz ql on djdy.djdyid=ql.djdyid ")
		.append(" left join bdck.bdcs_qlr_xz qlr on ql.qlid=qlr.qlid where shy.bdcdyid is not null and qlr.qlrmc='").append(qlrmc)
		.append("' and qlr.bdcqzh='").append(qzh).append("'");
		List<Map> m = commonDao.getDataListByFullSql(strtd.toString());
		if(m!=null&&m.size()>0){
			bdcdyid=m.get(0).get("BDCDYID")==null?"":m.get(0).get("BDCDYID").toString();
			bdcdylx = m.get(0).get("BDCDYLX")==null?"02":m.get(0).get("BDCDYLX").toString();
			rf=getParam(rwlsh, qlrmc, qzh, bdcdyid, bdcdylx, cslx,ywmc,username, request);
			if(rf!=null){
				status= "success";
			}
			return status;
		}
		if(m.size()==0&&maps.size()==0){
			bdcdylx=null;
			rf=getParam(rwlsh, qlrmc, qzh, bdcdyid, bdcdylx, cslx,ywmc,username,request);
			if(rf!=null){
				status= "success";
			}
			return status;
		}
		return status;
	}
	private String cslxForMatter(String cslx){
		String name="";
		if("1".equals(cslx)){
			name="查封";
		}
		else if("2".equals(cslx)){
			name="续查封";
		}
		else if("3".equals(cslx)){
			name="预查封";
		}
		else if("4".equals(cslx)){
			name="轮候查封";
		}
		else if("5".equals(cslx)){
			name="解除查封";
		}
		else if("6".equals(cslx)){
			name="解除预查封";
		}
		else if("7".equals(cslx)){
			name="解除轮候查封";
		}
		return name;
	}
	private SmObjInfo getParam(String rwlsh,String qlrmc,String qzh,String bdcdyid,String bdcdylx,String cslx,String ywmc,String username,HttpServletRequest request) throws Exception{
		SmObjInfo rf = null;
		SmObjInfo f = new SmObjInfo();
		SmProInfo info = new SmProInfo();
		info.setProDef_ID(cslx);
		ywmc=ywmc.replace("->", ",");
		info.setProDef_Name(ywmc);
		info.setProInst_Name(rwlsh+"-"+qlrmc+"-"+ywmc);
		
		List<User> list=commonDao.getDataList(User.class,"select * from smwb_framework.T_USER where loginname='"+username+"'");
		f.setID(list.get(0).getId());
		f.setName(list.get(0).getLoginName());
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(
				String.valueOf(list.get(0).getLoginName()), String.valueOf(list.get(0).getPassword()));
		token.setRememberMe(true);
		try{
			user.login(token);
		}catch(Exception e){
			e.printStackTrace();
		}
		Wfd_Prodef prodef = smProdef.GetProdefById(info.getProDef_ID());
		info.setLCBH(prodef.getProdef_Code());
		info.setFile_Urgency("2");
		info.setInstance_Type(1);
		info.setStaffID(list.get(0).getId());
		
		List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
		staffList.add(f);
		info.setAcceptor(f.getName());
		rf = Accept(info, staffList);
		ProjectInfo pro = ProjectHelper.GetProjectFromRest(rf.getFile_number(), request);
		List<BDCS_XMXX> xmxx=commonDao.getDataList(BDCS_XMXX.class,"select * from bdck.bdcs_xmxx where project_id='"+rf.getFile_number()+"'");

	//	xmlService.CFDYFromShareDB(bdcdyid, bdcdylx, rwlsh,pro.getXmbh());
		xmlService.CFDYFromShareDB(bdcdyid, bdcdylx, rwlsh,xmxx.get(0).getId());

		return rf;
	}
	public List<Wfi_ProUserInfo> GetInfo(String lsh){
		List<Wfi_ProUserInfo> list=commonDao.getDataList(Wfi_ProUserInfo.class,"select * from "+ Common.WORKFLOWDB + "wfi_prouserinfo where file_number='"+lsh+"'");
		return list;
	}
	/**
	 * 检测是否可以流程重指
	 * 
	 * 
	 * */
	public ResultMessage canRevice(String actinstid){
		ResultMessage message=new ResultMessage();
		String type=_smActInst.GetActInst(actinstid).getActdef_Type();
		if(type!=null&&type.equals("1010")){
//			String proinstid=_SmProInst.GetProInstIDByActInstId(actinstid);
			//应该取FILE_NUMBER
			Wfi_ProInst inst = _SmProInst.GetProInstByActInstId(actinstid);
			if (inst != null) {
				String proinstid = inst.getFile_Number();
				ResultMessage result=projectServiceImpl.getCanResetWorkflowCode(proinstid);
				return result;
			}
		}else{
			message.setSuccess("false");
			message.setMsg("请先驳回到受理环节!");
		}
		return message;
	}
	/**
	 * 流程重指
	 * 
	 * 
	 * */
	public ResultMessage revice(Wfi_ProInst inst,String prodefid,String prodefname){
		ResultMessage message =new ResultMessage();
		StringBuilder sBuilder = new StringBuilder();
		Wfd_Prodef prodef=smProdef.GetProdefById(prodefid);
		sBuilder.append("prodef_id='");
		sBuilder.append(prodefid);
		sBuilder.append("'");
		sBuilder.append(" and actdef_type='");
		sBuilder.append(WFConst.ActDef_Type.ProcessStart.value);
		sBuilder.append("'");
		List<Wfd_Actdef> actdefs = commonDao.findList(Wfd_Actdef.class, sBuilder.toString());
		if (actdefs != null && actdefs.size() > 0) {
			//创建流程实例
			Wfi_ProInst proInst = new Wfi_ProInst();
			proInst.setProinst_Id(Common.CreatUUID());
			proInst.setProdef_Id(prodefid);
			proInst.setAcceptor(inst.getAcceptor());
			proInst.setCreat_Date(inst.getCreat_Date());
			proInst.setDistrict_Id(inst.getDistrict_Id());
			proInst.setProdef_Name(prodefname);
			proInst.setProinst_Start(inst.getProinst_Start());
			proInst.setProinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			proInst.setProject_Name(inst.getProject_Name());
			proInst.setUrgency(inst.getUrgency());
			proInst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
			proInst.setFile_Number(_SmProInst.GetFileNumber(prodef.getProdef_Code()));
			proInst.setProinst_Time(prodef.getProdef_Time());
			proInst.setProlsh(inst.getProlsh());
			proInst.setProinst_Willfinish(inst.getProinst_Willfinish());
			proInst.setProinst_Code(prodef.getProdef_Code());
			proInst.setAreaCode(inst.getAreaCode());
			proInst.setStaff_Id(inst.getStaff_Id());
			//创建actInst  nowActInst  ActInstStaff
			Wfi_ActInst actInst = _smActInst.AddNewActInst(actdefs.get(0), null, "受理项目备注", proInst,prodef.getHouse_Status());
			//修正actinst时间
			actInst.setActinst_Start(inst.getProinst_Start());
			if (actInst != null) {
				//proinst添加当前环节信息
				proInst.setActdef_Type(actInst.getActdef_Type());
				proInst.setActinst_Id(actInst.getActinst_Id());
				proInst.setIsApplyHangup(actInst.getIsApplyHandup());
				proInst.setStatusext(actInst.getStatusExt());
				proInst.setOperation_Type_Nact(actInst.getOperation_Type());
				proInst.setStaff_Id_Nact(actInst.getStaff_Id());
				proInst.setActinst_Status(actInst.getActinst_Status());
				proInst.setActinst_Willfinish(actInst.getActinst_Willfinish());
				proInst.setActinst_Name(actInst.getActinst_Name());
				proInst.setMsg(actInst.getMsg());
				proInst.setCodeal(actInst.getCodeal());
				proInst.setACTINST_START(actInst.getActinst_Start());
				proInst.setACTINST_END(actInst.getActinst_End());
				proInst.setStaff_Name_Nact(actInst.getStaff_Name());
				Wfi_NowActInst nowActInst = _smActInst.CreatNowActInst(actInst);
				SmObjInfo info=new SmObjInfo();
				info.setID(proInst.getStaff_Id());
				info.setName(proInst.getAcceptor());
				Wfi_ActInstStaff actinstroute = _smActInst.CreatActStaff(actInst.getActinst_Id(),info, "");
				//获取原流程资料    新流程挂接资料
				String oldProinstid = inst.getProinst_Id();
				List<Wfi_ProMater> promaters = _SmProMater.GetProMaterInfo(oldProinstid);
				if(null!=promaters&&promaters.size()>0){
					for(Wfi_ProMater promater:promaters){
						promater.setProinst_Id(proInst.getProinst_Id());
					}
				}
				if (nowActInst != null) {
					commonDao.save(proInst);
					commonDao.save(actInst);
					commonDao.save(nowActInst);
					commonDao.save(actinstroute);
					for(Wfi_ProMater promater:promaters){
						commonDao.saveOrUpdate(promater);
					}
					commonDao.flush();
				}
			}
			if (proInst != null) {
				deleteProInst(inst.getProinst_Id(),"流程重指");
				// 导入收件资料模版
				//AddNewMtrs(proInst.getProdef_Id(), proInst.getProinst_Id());
				//调用登记系统接口
				boolean result=projectServiceImpl.ResetWorkflowCode(inst.getFile_Number(), proInst.getFile_Number());
				
				if(result){
					message.setSuccess("true");
					message.setMsg("流程重指成功！");
				}
			}
		}		
		return message;
	}
	/**
	 * 根据部门id获取该部门下所有用户及角色信息
	 * @return 
	 * 
	 * 
	 * */
	public List<Map> getUserRole(String deptid){
		StringBuilder sql = new StringBuilder();
		sql.append("select u.username,u.id as userid,r.rolename,r.id as roleid ");
		sql.append("from rt_userrole rt ");
		sql.append("inner join t_role r ");
		sql.append("on rt.roleid=r.id ");
		sql.append("inner join t_user u ");
		sql.append("on rt.userid=u.id ");
		sql.append("where u.departmentid='"+deptid+"'");
		List<Map> list=commonDao.getDataListByFullSql(sql.toString());
		return list;
	}
	 public SmObjInfo Addpasswork(Wfi_PassWork passwork){	   
		   SmObjInfo info=new SmObjInfo();
		   passwork.setPasswork_Id(Common.CreatUUID());
		   passwork.setStatus(1);
		   passwork.setPasstime(new Date());
		   commonDao.save(passwork);
		   commonDao.flush();
		   info.setID(passwork.getPasswork_Id());
		   info.setDesc("委托成功");
		   return info;
	   }
	/**
	 * 获取当前委托信息
	 * @param staff_id
	 * @return
	 */
	public Message getCurPassWork(String staff_id,String type) {
		 Message msg=new Message();
		 StringBuilder str = new StringBuilder();
		 StringBuilder tmpTable = new StringBuilder();
		 tmpTable.append(Common.WORKFLOWDB + "WFI_PASSWORK");
		 if(!type.equals("1")){
			 str.append("TO");
		 }
		 str.append("STAFF_ID='");	 
		 str.append(staff_id);
		 str.append("' and STATUS='");
		 str.append(1);
		 str.append("'");
		 long tatalCount = commonDao
					.getCountByFullSql(" from " +  tmpTable.toString()
							+ " where " + str.toString());
			msg.setTotal(tatalCount);
			List<Map> list = null;
			if (tatalCount > 0) {
				list = commonDao.getDataListByFullSql("select * from "
						+ tmpTable.toString() + " where "
						+ str.toString() );
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("PRODEF_IDS")!=null){
						list.get(i).put("PRODEF_IDS", list.get(i).get("PRODEF_IDS").toString());
					}
					if(list.get(i).get("ROLE_IDS")!=null){
						list.get(i).put("ROLE_IDS", list.get(i).get("ROLE_IDS").toString());
					}
				}
			}
			
			msg.setRows(list);
		 return msg;
	}

	/**
	 * 获取历史委托信息
	 * @param staff_id
	 * @return
	 */
	public Message getHistoryPassWork(String staff_id,String type) {
		 Message msg=new Message();
		 StringBuilder str = new StringBuilder();
		 StringBuilder tmpTable = new StringBuilder();
		 tmpTable.append(Common.WORKFLOWDB + "WFI_PASSWORK");
		 if(!type.equals("1")){
			 str.append("TO");
		 }
		 str.append("STAFF_ID='");
		 str.append(staff_id);
		 str.append("' and STATUS<>'");
		 str.append(1);
		 str.append("'");
		 long tatalCount = commonDao
					.getCountByFullSql(" from " +  tmpTable.toString()
							+ " where " + str.toString());
			msg.setTotal(tatalCount);
			List<Map> list = null;
			if (tatalCount > 0) {
				list = commonDao.getDataListByFullSql("select * from "
						+ tmpTable.toString() + " where "
						+ str.toString() );
			}
			
			msg.setRows(list);
		 return msg;
	}

	public Message cancelPasswork(String id) {
		Message msg=new Message();
		Wfi_PassWork  passwork=commonDao.get(Wfi_PassWork.class, id);
		if(passwork != null){
			passwork.setStatus(0);
			commonDao.update(passwork);
			commonDao.flush();
			msg.setDesc("删除成功！");
		}
		return msg;
	}
	/**
	 * 检查actinst是否有转办，有则添加，（流程信息时间轴）
	 * @param 
	 * @return
	 */
	public List<Wfi_ActInst> checkTurnProject(List<Wfi_ActInst> list){
		String sqlbefor="ActInst_Id = '";
		String sqlafter="' and status = '1' order by TurnDate desc";
		boolean isLast=true;
		List<Wfi_ActInst> actList=new ArrayList<Wfi_ActInst>();
		for(Wfi_ActInst actinst : list){
			List<Wfi_TurnList> turnlist=commonDao.getDataList(Wfi_TurnList.class,
					Common.WORKFLOWDB + "WFI_TURNLIST",sqlbefor+actinst.getActinst_Id()+sqlafter);
			if(!turnlist.isEmpty()){
				for(Wfi_TurnList turn : turnlist){
					Wfi_ActInst inst=new Wfi_ActInst();
					inst.setActinst_Id(turn.getActInst_Id());
					inst.setStaff_Name(smStaff.GetStaffName(turn.getToStaffId()));
					inst.setActinst_Name(actinst.getActinst_Name());
					inst.setActinst_End(actinst.getActinst_End());
					inst.setActinst_Start(turn.getTurnDate());
					
					inst.setActinst_Msg(turn.getTurnMsg());
					
					if(isLast){
						inst.setActinst_Status(actinst.getActinst_Status());
						inst.setOperation_Type(actinst.getOperation_Type());
					}else{
						inst.setActinst_Status(WFConst.Instance_Status.Instance_Passing.value);
						inst.setOperation_Type(WFConst.Operate_Type.HaveTurn.value+"");
						isLast=false;
					}
					actinst.setStaff_Name(smStaff.GetStaffName(turn.getFromStaffId()));
					actinst.setActinst_End(turn.getTurnDate());
					actinst.setActinst_Status(WFConst.Instance_Status.Instance_Passing.value);
					actinst.setOperation_Type(WFConst.Operate_Type.HaveTurn.value+"");
					actList.add(inst);
				}			
			}
		}
		list.addAll(actList);
		return list;
	}
	/**
	 * 根据流水号获取正在办理的发证环节进行转出操作
	 * @date   2016年10月31日 下午3:17:21
	 * @author JHX
	 * @param lsh
	 * @return
	 */
	public Wfi_ActInst getFzActinst(String lsh){
		Wfi_ActInst wfi_actinst = null;
		List<Wfi_ProInst> listproinst = commonDao.getDataList(Wfi_ProInst.class, "select * from "+Common.WORKFLOWDB+"Wfi_ProInst where prolsh='"+lsh+"'");
		if(listproinst!=null&&listproinst.size()>0){
			Wfi_ProInst proinst = listproinst.get(0);
			List<Wfi_ActInst> listactinst = 
					commonDao.getDataList(Wfi_ActInst.class, 
						"select * from "+Common.WORKFLOWDB+"Wfi_ActInst where proinst_id='"
					+proinst.getProinst_Id()+"' AND ACTINST_NAME like '%发证%' AND ACTINST_STATUS IN (1,2) ORDER BY ACTINST_START DESC");
			if(listactinst!=null&&listactinst.size()>0){
				wfi_actinst = listactinst.get(0);
			}
		}
		return wfi_actinst;
	}
	/**
	 * 获取批量转出人员信息
	 * @date   2016年10月31日 下午5:33:01
	 * @author JHX
	 * @param infos
	 * @return
	 */
	public  SmObjInfo getBatchPassoverStaff(List<SmObjInfo> infos) {
		if (infos != null && infos.size() > 0) {
			SmObjInfo info = infos.get(0);
			if (info.getChildren() == null) {
				info.setDesc(info.getDesc());
				info.setID(info.getID());
				info.setName(info.getName());
				return info;
			} else {
				return getBatchPassoverStaff(info.getChildren());
			}
		}
		return null;
	}
	/**
	 * 根据流水号获取最新环节信息
	 * @author JHX
	 * @param prolsh
	 * @return
	 */
	public Map<String ,Object> getProcessScheduleOnline(String prolsh){
		Map<String ,Object> map = new HashMap<String,Object>();
		if(!StringHelper.isEmpty(prolsh)){
			String sql =
			"select\n" +
			"   prolsh,\n" + 
			"   substr(prodef_name,1,instr(prodef_name,',',1,1)-1) as djlx,\n" + 
			"   substr(prodef_name,instr(prodef_name,',',1,1)+1) as djlx2,\n" + 
			"   project_name,\n" + 
			"   acceptor,\n" + 
			"   proinst_start,\n" + 
			"   proinst_willfinish,\n" + 
			"   B.ACTINST_NAME\n" + 
			" from "+Common.WORKFLOWDB+"wfi_proinst A \n" + 
			" left join\n" + 
			" (select * from (select  row_number() over( partition by proinst_id,prodef_id order by actinst_start desc ) rn ,t.* from "+Common.WORKFLOWDB+"wfi_actinst t) where rn=1) B\n" + 
			" ON A.PROINST_ID = B.PROINST_ID WHERE PROLSH='"+prolsh+"'";
			List<Map>  listmap =commonDao.getDataListByFullSql(sql);
			if(listmap!=null&&listmap.size()>0){
				map.put("xmxx", listmap.get(0));
			}
			List<Wfi_ProInst> proinsts = commonDao.getDataList(
					Wfi_ProInst.class, " select * from " + Common.WORKFLOWDB
							+ "Wfi_ProInst where prolsh='"+prolsh+"'");
			Wfi_ProInst proinst = null;
			if (proinsts != null && proinsts.size() > 0) {
				proinst = proinsts.get(0);
			String sqlprocess=
					"select\n" +
					"            t.actdef_name as ACTINST_NAME,\n" + 
					"            h.STAFF_NAME,\n" + 
					"            h.ACTINST_START,\n" + 
					"            h.ACTINST_END,\n" + 
					"            h.OPERATION_TYPE,\n" + 
					"            h.ACTINST_STATUS\n" + 
					"            from bdc_workflow.wfd_actdef t\n" + 
					"            left join (\n" + 
					"            select * from (\n" + 
					"            select t.*, row_number() over(partition by proinst_id, actdef_id order by actinst_start asc) rn from\n" + 
					"            bdc_workflow.wfi_actinst t\n" + 
					"            )\n" + 
					"            where rn=1 and proinst_id = '"+proinst.getProinst_Id()+"'\n" + 
					"            ) h\n" + 
					"            on t.actdef_id = h.actdef_id\n" + 
					"            where t.prodef_id = '"+proinst.getProdef_Id()+"'\n" + 
					"            and t.actdef_type <>'3020' and t.actdef_type <>'3010' order by h.actinst_start ";
				List<Map> listproact = commonDao
						.getDataListByFullSql(sqlprocess);
				if (listproact != null && listproact.size() > 0) {
					map.put("jd", listproact);
				}
			}
			
		}
		return map;
	}
	public boolean ModiftyProjectInfo(String actinstid,String projectinfo){
		Wfi_ProInst inst=_SmProInst.GetProInstByActInstId(actinstid);
		inst.setRemarks(projectinfo);
		commonDao.update(inst);
		commonDao.flush();
		return true;
	}
	
	/**
	 * 根据流程 proinst 获取所有活动环节最早信息（不含驳回环节）
	 * partation by 不用更改本身已使用proinst_id 过滤
	 * @author zhangpeng
	 * @param proinst
	 * @return 
	 */
	public List<Map> getProcessSchedule(Wfi_ProInst proinst) {
		String prodef_id = proinst.getProdef_Id();	
		String proinst_id = proinst.getProinst_Id();
		String sql = "select actdef_id from "+Common.WORKFLOWDB+"wfd_actdef where prodef_id='"+prodef_id+"' and actdef_type='1010'";
		List<Map> actdef_id = commonDao.getDataListByFullSql(sql);
		if (actdef_id != null && actdef_id.size() > 0) {
			Map actdefid = actdef_id.get(0);
			String adf_id = actdefid.get("ACTDEF_ID").toString();
			String fulSql = 
					"	select actinst_id,actdef_id,actinst_name as actdef_name,actinst_start,actinst_end,staff_name,rn\n" +
					"   from (select t.actinst_id,t.actdef_id,t.actinst_name,t.actinst_start,t.actinst_end,t.staff_name, ROW_NUMBER() over (partition by t.actdef_id order by t.actinst_start asc) rn\n" + 
					"   from bdc_workflow.wfi_actinst t\n" + 
					"   where proinst_id='"+proinst_id+"' )\n" + 
					"   where rn =1 order by actinst_start";
			List<Map> actinsts = commonDao.getDataListByFullSql(fulSql);
			return actinsts;
		}
		return null;
	}
	
	
	/**
	 *针对部门的办件统计查询
	 *@author heks
	 *@date 2017-03-09 16:00
	 * @return
	 */
	public Message getDeptProjects(Map<String, String> conditionParameter,String pageIndex,String pageSize){
		
		String type = conditionParameter.get("type");
		String startDate = conditionParameter.get("startDate");
		String endDate = conditionParameter.get("endDate");
		String deptid = conditionParameter.get("deptid");
		String staffid = conditionParameter.get("staffid");
		String cxlx = conditionParameter.get("cxlx");
		
		Message msg = new Message();
		String select = "SELECT * ";
		String fromsql = "";
		StringBuilder wheresql = new StringBuilder();
		if (cxlx.equals("yb")) {
			wheresql.append(" AND (B.ACTINST_STATUS = 3 OR (B.PROINST_STATUS = 0 AND B.ACTINST_STATUS = 0)) ");
		}else if(cxlx.equals("zb")){
			wheresql.append(" AND B.ACTINST_STATUS = 2 ");
		}
		if (!StringHelper.isEmpty(startDate)) {
			wheresql.append(" AND ACTINST_START >= TO_DATE('"+startDate+"','YYYY-MM-DD')");
		}
		if (!StringHelper.isEmpty(endDate)) {
			wheresql.append(" AND ACTINST_START <= TO_DATE('"+endDate+"','YYYY-MM-DD')");
		}
		if ("KS".equals(type)) {//科室
			if (!StringHelper.isEmpty(deptid)) {
				wheresql.append(" AND D.ID = '"+deptid+"'");
			}
			fromsql = "FROM (SELECT ROW_NUMBER() "
					+ "OVER(PARTITION BY file_number ORDER BY B.ACTINST_START ASC) RN,B.*,D.ID AS DEPTID "
					+ " FROM BDC_WORKFLOW.V_PROJECTLIST B LEFT JOIN  SMWB_FRAMEWORK.T_USER A  ON b.STAFF_ID=A.ID "
					+ " LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT D ON A.DEPARTMENTID = D.ID "
					+ " WHERE 1=1 "
					+ wheresql.toString()
					+ " ) WHERE RN = 1";
		} else {
			if (!StringHelper.isEmpty(staffid)) {
				wheresql.append(" AND STAFF_ID = '"+staffid+"'");
			}
			fromsql = "FROM (SELECT  ROW_NUMBER()"
					+ " OVER (PARTITION BY FILE_NUMBER ORDER BY ACTINST_START DESC) RN,B.*"
					+ " FROM BDC_WORKFLOW.V_PROJECTLIST B"
					+ " WHERE 1=1 "
					+ wheresql.toString()
					+" ) WHERE RN=1";
		}
		List<Map> list = commonDao.getDataListByFullSql(select + fromsql, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
		long count = commonDao.getCountByFullSql(fromsql);
		msg.setTotal(count);
		msg.setRows(list);
		
		return msg;
	}
	/**
	 * 鹰潭短信发送
	 * @param filenumber
	 * @param project_name
	 * @param groupname
	 * @param remark
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ConnectException 
	 * @throws MalformedURLException 
	 */
	public boolean Senddx(String filenumber,String project_name,String groupname,String remark) throws UnsupportedEncodingException, ConnectException, MalformedURLException {
		ResultSet result= null;
		boolean f = false;
		String dxnl = null;
		String qhmc =ConfigHelper.getNameByValue("XZQHMC");
		String lodtime = ConfigHelper.getNameByValue("LODTIME");
		List<BDCS_XMXX> xmxx = commonDao.findList(BDCS_XMXX.class, " PROJECT_ID='" + filenumber + "'");
		String tokenstr="";
		try {
			tokenstr = GetTokenUtil.getAccessToken();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean issuccessqlr = false;
		boolean issuccessdlr = false;
		User user = Global.getCurrentUserInfo();
		String senduser="";
		if(user != null){
			senduser = user.getLoginName();
		}
		if(xmxx.size()>0){
			for(BDCS_XMXX xm : xmxx){
				if(xm.getDJLX()!=null && !xm.getDJLX().equals("400")){
					//SQLLB 1:权利人 2:义务人
					String sq = "XMBH ='"+xm.getId()+"' AND SQRLB = 1 and (LXDH IS NOT NULL or DLRLXDH IS NOT NULL)" ;
					List<BDCS_SQR> sqrs = commonDao.findList(BDCS_SQR.class, sq);
					for(BDCS_SQR sqr :sqrs){
						String qlrid = "";
						String zsid = "";
						String bdcqzh = "";
						String zsbh = "";
						String qlr = "XMBH ='"+xm.getId()+"' AND SQRID ='"+sqr.getId()+"'" ;
						Date szsj = null;
						List<BDCS_QLR_GZ> qlrs = commonDao.findList(BDCS_QLR_GZ.class, qlr);
						if(qlrs != null && qlrs.size()>0){
							qlrid = qlrs.get(0).getId();
							bdcqzh = qlrs.get(0).getBDCQZH();
						}
						String qdlr = "XMBH ='"+xm.getId()+"' AND QLRID ='"+qlrid+"'" ;
						List<BDCS_QDZR_GZ> qdzrs = commonDao.findList(BDCS_QDZR_GZ.class, qdlr);
						if(qdzrs != null && qdzrs.size()>0){
							zsid = qdzrs.get(0).getZSID();
						}
						String zs = "XMBH ='"+xm.getId()+"' AND ZSID ='"+zsid+"'" ;
						List<BDCS_ZS_GZ> zss = commonDao.findList(BDCS_ZS_GZ.class, zs);
						if(zss != null && zss.size()>0){
							zsbh = zss.get(0).getZSBH();
						}
						List<BDCS_DJSZ> szs = commonDao.findList(BDCS_DJSZ.class, "XMBH ='"+xm.getId()+"'");
						if(szs != null && szs.size()>0){
							szsj = szs.get(0).getSZSJ();
						}
						
						String exitsql = "XMBH ='"+xm.getId()+"' AND SQRID ='"+sqr.getId()+"'" ;
						List<LOG_SENDMSG> logexit = commonDao.findList(LOG_SENDMSG.class, exitsql);
						LOG_SENDMSG dxlog = new LOG_SENDMSG();
						dxlog.setSQRXM(sqr.getSQRXM());
						dxlog.setLXDH(sqr.getLXDH());
						dxlog.setDLRXM(sqr.getDLRXM());
						dxlog.setDLRLXDH(sqr.getDLRLXDH());
						dxlog.setYWLSH(xm.getYWLSH());
						dxlog.setXMMC(xm.getXMMC());
						dxlog.setZSBH(zsbh);
						dxlog.setBDCQZH(bdcqzh);
						dxlog.setDJSJ(xm.getDJSJ());
						dxlog.setSZSJ(szsj);
						dxlog.setSENDTIME(new Date());
						dxlog.setSENDLX("自动发送");
						dxlog.setXMBH(xm.getId());
						dxlog.setSQRID(sqr.getId());
						dxlog.setSENDUSER(senduser);
						if(!StringHelper.isEmpty(sqr.getLXDH())){
							String content = "Content=[\""+xm.getYWLSH()+"\",\""+qhmc+"不动产登记中心"+"\"]";
							content += "&PlanOfSendTime=null";
							content += "&ToStaff="+sqr.getSQRXM();
							content += "&ToStaffPhone="+sqr.getLXDH();
							content += "&Level=1";
							content += "&SendImmediately=true";
							content += "&TemplateCode=102555";
							issuccessqlr = DXSENDUtil.readContentFromPost(content,tokenstr);
							if(issuccessqlr == true){
								if(logexit != null && logexit.size()>0){
									logexit.get(0).setSENDTIME(new Date());
									logexit.get(0).setBZ("重新发送");
									logexit.get(0).setSENDUSER(senduser);
									commonDao.update(logexit.get(0));
									commonDao.flush();
								}else{
									dxlog.setSENDSQRSTATUS("发送成功");
									commonDao.save(dxlog);
									commonDao.flush();
								}
							}else{
								if(logexit != null && logexit.size()>0){
									logexit.get(0).setSENDTIME(new Date());
									logexit.get(0).setBZ("重新发送");
									logexit.get(0).setSENDUSER(senduser);
									commonDao.update(logexit.get(0));
									commonDao.flush();
								}else{
									dxlog.setSENDSQRSTATUS("发送失败");
									commonDao.save(dxlog);
									commonDao.flush();
								}
							}			
						}
						if(!StringHelper.isEmpty(sqr.getDLRLXDH())){
							String content = "Content=[\""+xm.getYWLSH()+"\",\""+qhmc+"不动产登记中心"+"\"]";
							content += "&PlanOfSendTime=null";
							content += "&ToStaff="+sqr.getDLRXM();
							content += "&ToStaffPhone="+sqr.getDLRLXDH();
							content += "&Level=1";
							content += "&SendImmediately=true";
							content += "&TemplateCode=102555";
							issuccessdlr = DXSENDUtil.readContentFromPost(content,tokenstr);
							if(issuccessdlr == true){
								if(logexit != null && logexit.size()>0){
									logexit.get(0).setSENDTIME(new Date());
									logexit.get(0).setBZ("重新发送");
									logexit.get(0).setSENDUSER(senduser);
									commonDao.update(logexit.get(0));
									commonDao.flush();
								}else{
									dxlog.setSENDDLRSTATUS("发送成功");
									commonDao.save(dxlog);
									commonDao.flush();
								}
							}else{
								if(logexit != null && logexit.size()>0){
									logexit.get(0).setSENDTIME(new Date());
									logexit.get(0).setBZ("重新发送");
									logexit.get(0).setSENDUSER(senduser);
									commonDao.update(logexit.get(0));
									commonDao.flush();
								}else{
									dxlog.setSENDDLRSTATUS("发送失败");
									commonDao.save(dxlog);
									commonDao.flush();
								}
							}
						}
					}
				}
			}
			if(issuccessqlr == true||issuccessdlr == true){
				f = true;
			}
		}
		
		return f;
	}
	
	public  String getlogmsg(String key,String qlr,String ywr,String zjh,String qzh,String zl,String ywh,String qhmc,String PreciseQueryQLR,String  status, String start,String  end,String  actdef_name,String  prodefname,String  staff,String  sqr,String  urgency,String  outtime,String  passback,String  proStatus){
		String sql="";
		//查询内容
		sql=sql+((key+"").length()>0?"查询内容："+key+"/":"");
		//活动类型
		sql=sql+((actdef_name+"").length()>0?"活动类型:"+actdef_name+"/":"");
		sql=sql+(((start+"").length()>0||(end+"").length()>0)?"办理时间:":"")+(start.length()==0?"":start+(end.length()==0?"/":"-")+end+"/");
		sql=sql+((staff+"").length()>0?"办理人员:"+staff+"/":"");
		sql=sql+((sqr+"").length()>0?"申请人:"+sqr+"/":"");
		sql=sql+((zl+"").length()>0?"坐落:"+zl+"/":"");
		sql=sql+((qlr+"").length()>0?"权利人:"+qlr+"/":"");
		sql=sql+((ywr+"").length()>0?"义务人:"+ywr+"/":"");
		sql=sql+((zjh+"").length()>0?"证件号:"+zjh+"/":"");
		sql=sql+((qzh+"").length()>0?"不动产权证号:"+qzh+"/":"");
		sql=sql+((ywh+"").length()>0?"房产业务号:"+ywh+"/":"");
		//sql=sql+((qhmc+"").length()>0?"区划名称:"+qhmc+"/":"");
		sql=sql+((proStatus+"").length()>0?"项目状态:"+(proStatus.toString().contains("ycq")?"已超期":"即将超期")+"/":"");
		sql=sql+((status+"").length()>0?"办理状态:"+(("1").equals(status)?"待办":("2".equals(status)?"在办":"已办"))+"/":"");
		return sql;
	}
	
	public List<Map> getBatchProject(String batchNum){
		if(!StringHelper.isEmpty(batchNum)){
			return commonDao.getDataListByFullSql("select FILE_NUMBER,ACTINST_ID,PROLSH from "+Common.WORKFLOWDB+"WFI_PROINST where BATCH = '"+batchNum+"'");
		}
		return null;
	}
	public Map getProjectList( HttpServletRequest request ){
		Map result = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( select row_number() over(partition by PO.PROLSH order by PO.PROLSH ) as rn,");
		sb.append("PO.PROINST_STATUS , PO.PROJECT_NAME,PO.PRODEF_NAME,PO.URGENCY,");
		sb.append("AI.ACTINST_NAME,AI.ACTINST_STATUS,AI.OPERATION_TYPE,AI.ACTINST_ID FROM　");
		sb.append(Common.WORKFLOWDB);
		sb.append("WFI_PROINST　PO LEFT JOIN ");
		sb.append(Common.WORKFLOWDB);
		sb.append("WFI_ACTINST AI ON　AI.PROINST_ID = PO.PROINST_ID ");
		sb.append("WHERE　1>0 ");
		String actinstname = request.getParameter("actdefName");
		if(!StringHelper.isEmpty(actinstname)){
			sb.append("and AI.ACTINST_NAME IN (");
			sb.append(actinstname + ") ");
		}
		String searchKey = request.getParameter("value");
		if(!StringHelper.isEmpty(searchKey)){
			sb.append("and (PO.PROJECT_NAME LIKE ('%");
			sb.append(searchKey);
			sb.append("%') or PO.PROLSH LIKE ('%");
			sb.append(searchKey + "%')) ");
		}
		sb.append(") where rn <2 ");
		long count = commonDao.getCountByFullSqlCustom("SELECT COUNT(1) FROM ("+sb.toString()+")");
		if(count!=0){
			String  currentPageIndex = request.getParameter("currentPageIndex");
			if(StringHelper.isEmpty(currentPageIndex))currentPageIndex="1";
			int pageIndex = Integer.parseInt(currentPageIndex);
			String  pageSize = request.getParameter("pageSize");
			if(StringHelper.isEmpty(pageSize))pageSize="15";
			int size = Integer.parseInt(pageSize);
			List<Map> data = commonDao.getPageDataByFullSql(sb.toString(), pageIndex , size);
			result.put("rows", data);
		}
		result.put("total", count);
		return result;
	}
	
	public List<String> getActinstid(String prolsh){
		String actinstid = null;
		String actinst_status = null;
		String sql = "SELECT a.actinst_id,a.actinst_status FROM bdc_workflow.wfi_actinst a left join bdc_workflow.wfi_proinst p on a.proinst_id = p.proinst_id "
				+ "WHERE p.prolsh='"+prolsh+"' and a.ACTINST_STATUS in(1,2) AND a.actinst_name ='发证' ";
		List<Map> list =commonDao.getDataListByFullSql(sql);
		if (list != null && list.size() > 0) {
			Map map = list.get(0);
			actinstid = map.get("ACTINST_ID").toString();
			actinst_status = map.get("ACTINST_STATUS").toString();
		}
		List<String> listactinst = new ArrayList();
		listactinst.add(actinstid);
		listactinst.add(actinst_status);
		return listactinst;
	}
	
	public Message getPLFZProjectList(String operaStaffString, String actinstname, int protype, String searchvalue, int page, int parseInt,String bdcdyh,QueryCriteria query){
		//String key, String status, String start, String end, int CurrentPageIndex, int pageSize, String actdef_name, String prodefname, String staff, String sqr, String urgency, String outtime, String passback, String proStatus,HttpServletRequest request) {
	//TODO:获取权利人、义务人、证件号、权证号、坐落信息
	
	
		String ywr = null;
		String zjh = query.getZjh();
		String qzh = null;
		String zl = null;
		String PreciseQueryQLR = null;
		String start=query.getActinstStart();
		String end=query.getActinstEnd();
		String staff =null;
		String qlr =null ;
		String actdef_name= null;
	try {
		
		staff = query.getStaff()==null?"": new String(query.getStaff().getBytes("iso8859-1"), "utf-8");
		qlr = query.getQlr()==null?"": new String(query.getQlr().getBytes("iso8859-1"), "utf-8");
		ywr = query.getYwr()==null?"":  new String( query.getYwr().getBytes("iso8859-1"), "utf-8");
		qzh = query.getQzh()==null?"": new String( query.getQzh().getBytes("iso8859-1"), "utf-8");
		zl = query.getZl()==null?"": new String( query.getZl().getBytes("iso8859-1"), "utf-8");
		PreciseQueryQLR = query.getIsVagueQuery()==null?"": new String( query.getIsVagueQuery().getBytes("iso8859-1"), "utf-8");
		qzh = query.getQzh()==null?"": new String( query.getQzh().getBytes("iso8859-1"), "utf-8");
		if(query.getActdefName()!=null && !query.getActdefName().equals("")){
			actdef_name = new String( query.getActdefName().getBytes("iso8859-1"), "utf-8");
		}
		
	} catch (UnsupportedEncodingException e) {
		
		e.printStackTrace();
	}
	

	boolean isVagueQuery = false;
	if(!StringHelper.isEmpty(PreciseQueryQLR)){
		isVagueQuery =Boolean.parseBoolean(PreciseQueryQLR);
	}
	String prolshs="";
	boolean flag = false;
	if(!StringHelper.isEmpty(qlr)
			||!StringHelper.isEmpty(ywr)
			||!StringHelper.isEmpty(zjh)
			||!StringHelper.isEmpty(qzh)
			||!StringHelper.isEmpty(zl)
			||!StringHelper.isEmpty(bdcdyh)){
	 flag=true;	
	 prolshs=projectService.getYwlshByCondtionsPl(operaStaffString,protype,qlr, ywr, zjh, qzh, zl,bdcdyh,isVagueQuery);
	}
	String prolshss = "";
	String[] arrs = prolshs.split(",");
	if(!StringHelper.isEmpty(prolshs)){
		String replaceend = "";
		
		
		if(arrs.length>999){
			
			prolshs = "";
			for(int i=0;i<999;i++){
				if(i==998){
					prolshs+="'"+arrs[i]+"'";
				}else{
					prolshs+="'"+arrs[i]+"',";
				}
			}
			System.out.println();
			for(int i=999;i<arrs.length;i++){
				
				if(i==arrs.length){
					
					prolshss+="'"+arrs[i]+"'";
				}else{
					prolshss+="'"+arrs[i]+"',";
				}
			}
		}else{
		replaceend = prolshs.replaceAll(",", "','");
		prolshs = "'"+replaceend+"'";
		}
	}
	if(flag&&prolshs.length()==0){
		return new Message();
	}else {
		
		if(start!=null&&!start.isEmpty()||
				end!=null&&!end.isEmpty()||
				staff!=null&&!staff.isEmpty()||
				actdef_name!=null&&!actdef_name.isEmpty()){
			return getBatchCertSearchPL( operaStaffString, actinstname, protype, searchvalue, page, parseInt,query,arrs,prolshs,prolshss,"DESC");
		}
		return getBatchCertSearchPL(operaStaffString, actinstname, protype, searchvalue, page, parseInt,query,arrs,prolshs,prolshss,"DESC");
	}
	
}
	
	@SuppressWarnings({ "null", "rawtypes", "unchecked" })
	public Message getBatchCertSearchPL(String operaStaffString,String actinstname,int protype, String searchvalue, int page, int parseInt,QueryCriteria query, String[] arrs ,String prolshs,String prolshss,String order) {
		Message msg = new Message();
		String key =null;
		try {
			key = query.getValue()==null?"": new String(query.getValue().getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String areaCode=query.getAreaCode();
		String ywh=query.getYwh();
		String lshstart=query.getLshStart();
		String lshend=query.getLshEnd();
		String status=query.getStatus();
		String prodefname=query.getProdefName();
		String urgency=query.getUrgency();
		String outtime=query.getOutTime();
		String proStatus=query.getProStatus();
		String passback=query.getPassBack();
 		String sqr= null;
		try {
			if( query.getSqr() != null &&! query.getSqr().equals("")) {
				sqr = new String( query.getSqr().getBytes("iso8859-1"), "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		StringBuilder str = new StringBuilder();
		// _________________________过滤驳回产生的一些重复的件—————————————————————————
		StringBuilder tmpTable = new StringBuilder();
		StringBuilder userstr = new StringBuilder();
		StringBuilder xmxxconfig = new StringBuilder();
		
		String username = null;
		str.append(" ACTSTAFF='");
		str.append(operaStaffString);
		if(protype ==3){
			str.append("' and ACTINST_STATUS in (1,2,3)");
		}else{
			str.append("' and ACTINST_STATUS in (1,2)");
		}
		
//		str.append(" ACTINST_STATUS in (1,2)");
		if (actinstname != null && !actinstname.equals("")) {
			str.append(" and actinst_name in (");
			str.append(actinstname);
			str.append(")");
		}
	
		if (key != null && !"".equals(key)) {
			str.append(" and (PROJECT_NAME like '%");
			str.append(key);
			str.append("%' or FILE_NUMBER like '%");
			str.append(key);
			str.append("%' ");
//			str.append(" or PROLSH='");
			str.append(" or PROLSH like'%");
			str.append(key);
			str.append("%'");
			str.append(")");
			// 支持权利人查询
		}else{
			if(searchvalue != null&&!searchvalue.equals("")){
				str.append(" and prolsh='"+searchvalue+"'");
			}
		}
		if(areaCode!=null&&!areaCode.isEmpty()){
			str.append(" and AREACODE = '"+areaCode+"' ");
		}
		if(!StringHelper.isEmpty(prolshs)){
			if(arrs.length>999){
				str.append(" and (PROLSH in ("+prolshs+") or PROLSH in("+prolshss+"))");
			}else{
				str.append(" and PROLSH in ("+prolshs+")");
			}
			
		}
		if(!StringHelper.isEmpty(ywh)){
			str.append(" and YWH like '%"+ywh+"%' ");
		}
		if(lshstart != null && !lshstart.equals("")){
			str.append(" and PROLSH>='"+lshstart+"' ");
		}
		if(lshend != null && !lshend.equals("")){
			str.append(" and PROLSH<='"+lshend+"' ");
		}
//		if (status != null && !"".equals(status)) {
//		str.append(" and ACTINST_STATUS in (");
//		str.append("1,2,14,15");
//		str.append(")");
//		}
		// 流程名称
		if (prodefname != null && !"".equals(prodefname)) {
			str.append(" and prodef_name like'");
			str.append(prodefname);
			str.append("%'");
		}
		if (urgency != null && !urgency.equals("")) {
			str.append(" and urgency=");
			str.append(urgency);
		}
		if (outtime != null && !outtime.equals("")) {
			str.append(" and proouttime>0");
		}
		if(proStatus!=null&&!proStatus.equals("")){
			str.append(" and proinst_status  not in('0') ");
			Date nowDate = new Date();
			Date date=smHoliday.FactDate(nowDate,2);
			java.sql.Date sqlNowDate=new java.sql.Date(nowDate.getTime());
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			if(proStatus.equals("jjcq")){
				str.append(" and proinst_willfinish > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') and proinst_willfinish< TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(proStatus.equals("ycq")){
				str.append(" and proinst_willfinish < TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else{
				str.append(" and proinst_willfinish < TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			
		}
		if (passback != null && !passback.equals("")) {
			str.append(" and operation_type=");
			str.append(WFConst.Operate_Type.PRODUCT_anglesPicture.value);
		}
        if (sqr != null && !"".equals(sqr)) {
			if (isChinese(sqr)) {
				str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%" + sqr + "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER) ");
			}
		}
		tmpTable.append(Common.WORKFLOWDB + "v_projectlist" + " " + "v_p" + " ");
		xmxxconfig.append("left join BDCK.BDCS_XMXX xmxx on v_p.file_number=xmxx.project_id ");
		long tatalCount =0;
		if( "".equals(key) && "".equals(searchvalue) &&  "".equals(prolshs) &&  "".equals(ywh)  && "".equals(lshstart)  && "".equals(lshend) || 
			 "".equals(prodefname) && sqr != null  && urgency  != null  && outtime  != null   && "".equals(proStatus) && passback  != null  ){
			tatalCount =0;
		}else{
			tatalCount = commonDao.getCountByFullSql(" from " + tmpTable.toString() + xmxxconfig + " where " + str.toString());

			
		}
		
		msg.setTotal(tatalCount);
		List<Map> list = null;
		List<Map> newlist = new ArrayList();
		userstr.append("SMWB_FRAMEWORK.t_user");
		List<Map> users = commonDao.getDataListByFullSql("select * from " + userstr.toString() + " " + "where id='" + operaStaffString + "'");
		if (users.size() > 0) {
			Map usermap = users.get(0);
			username = usermap.get("USERNAME").toString();
		}
		List listpage = new ArrayList();
		if (tatalCount > 0) {
			list = commonDao.getDataListByFullSql("select * from " + tmpTable.toString() + xmxxconfig + " where " + str.toString() + " order by  proinst_weight desc");
//			list = commonDao.getDataListByFullSql("select * from " + tmpTable.toString() + xmxxconfig + " where " + str.toString() + " order by  proinst_weight desc",page,parseInt);
			if (list.size() > 0) {
				List<DJFZXX> listfzxxs = null;
				for (int i = 0; i < list.size(); i++) {
					@SuppressWarnings("rawtypes")
					Map mapi = list.get(i);
					if (mapi.containsKey("XMBH")) {
						String xmbh = mapi.get("XMBH").toString();
						String ywlsh = mapi.get("YWLSH")==null?"": mapi.get("YWLSH").toString();
						com.supermap.wisdombusiness.web.ui.Page pagei = zsService.GetPagedFZList(xmbh, page, parseInt);
						List<DJFZXX> zsList = (List<DJFZXX>) pagei.getResult();
						listfzxxs = new ArrayList();
						if (zsList.size() > 0) {
							for (int z = 0; z < zsList.size(); z++) {
								DJFZXX fzxx = zsList.get(z);
								fzxx.setYwlsh(ywlsh);
//								if (fzxx.getCZLX().equals("发证")) {
									listfzxxs.add(fzxx);
//								} else {
//									continue;
//								}

							}
						}

					}
					listpage.add(listfzxxs);
				}
				if (listpage.size() > 0) {
					for (int j = 0; j < listpage.size(); j++) {
						List<DJFZXX> onefzxx = (List<DJFZXX>) listpage.get(j);
						if (onefzxx.size() > 0) {
							Object[] objs = onefzxx.toArray();
							for (int q = 0; q < objs.length; q++) {
								Map<String, String> mapdata = new HashMap();
								mapdata = toHashMap(objs[q]);
								mapdata.put("staffname", username);
								newlist.add(mapdata);
							}
						}
					}
				}
			}
		}
		
		msg.setRows(newlist);
		return msg;
	}
	
	public Message getDbprojectList(String searchValue, String pagesize, String currentPage){
		String staffid = smStaff.getCurrentWorkStaffID();
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("Wfi_proinst p where proinst_status = '13' and ");
		sb.append(" exists(select proinst_id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("Wfi_Dblist where proinst_id = p.proinst_id and staffid = '");
		sb.append(staffid+"')");
		if(!StringHelper.isEmpty(searchValue)){
			sb.append(" and prolsh like '%");
			sb.append(searchValue);
			sb.append("%' or project_name like '%");
			sb.append(searchValue + "%'");
		}
		long count = commonDao.getCountByFullSql(sb.toString());
		List<Map> data = commonDao.getPageDataByFullSql("select *" + sb.toString(), 
				Integer.parseInt(currentPage), Integer.parseInt(pagesize));
		Message result = new Message();
		result.setRows(data);
		result.setTotal(count);
		return result;
		
	}
	
	/**
	 * 通过流程编号查询该项目下是否还有单元存在
	 * 
	 * @Title: isXmxxByPrjID
	 * @author:heks
	 * @date：2017年10月18日 下午16:45:17
	 * @param project_id
	 * @return
	 */
	private Boolean isXmxxByPrjID(String project_id) {
		Boolean flag = false;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM BDCK.BDCS_XMXX WHERE");
		builder.append(" PROJECT_ID = '").append(project_id).append("'");
		String sql = builder.toString();
		List<BDCS_XMXX> lstbdcs_xmxx = commonDao.getDataList(BDCS_XMXX.class, sql);
		if (lstbdcs_xmxx == null || lstbdcs_xmxx.size() <= 0) {
			flag = true;
		} else {
			
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(lstbdcs_xmxx.get(
					0).getId());
			if (info != null) {
				String xmbh = info.getXmbh();// 获取项目编号
				String xmbhsql = "SELECT * FROM BDCK.BDCS_DJDY_GZ WHERE XMBH = '"+xmbh+"'";
				List<BDCS_DJDY_GZ> lstDjdy_gz = commonDao.getDataList(BDCS_DJDY_GZ.class, xmbhsql);
				if(lstDjdy_gz == null || lstDjdy_gz.size() <=0){
					flag = true;
				}
				else{
					flag = false;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 办件量统计（广西）个人统计
	 * @author liangc
	 * @param staffid
	 * @param pageIndex
	 * @param pageSize
	 * @param startString
	 * @param endString
	 * @return
	 */
	public Message getPersonProjects(String cxlx,String staffid,String pageIndex,String pageSize,String startString,String endString){
		Message msg = new Message();
		String xmstatus = "";
		if(cxlx.equals("yb")){
			xmstatus = "3";
		}else if(cxlx.equals("zb")){
			xmstatus = "2";
		}
		String sql = "SELECT * FROM  ( SELECT * FROM (SELECT ROW_NUMBER() "
				+ "OVER(PARTITION BY file_number ORDER BY B.ACTINST_START ASC) RN,B.* "
				+ " FROM BDC_WORKFLOW.V_PROJECTLIST B LEFT JOIN  SMWB_FRAMEWORK.T_USER A  ON b.STAFF_ID=A.ID "
				+ " LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT D ON A.DEPARTMENTID = D.ID "
				+ " WHERE b.STAFF_ID = '"+staffid+"'  AND ACTINST_STATUS = '"+xmstatus+"' "
				+ " AND ACTINST_END >= TO_DATE('"+startString+" "+"00:00:00"+" ','yyyy-mm-dd hh24:mi:ss') "
				+ " AND ACTINST_END <= TO_DATE( '"+endString+" "+"23:59:59"+" ','yyyy-mm-dd hh24:mi:ss')) WHERE RN = 1)";
		List<Map> list = commonDao.getDataListByFullSql(sql, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
		long count = commonDao.getCountByFullSql(" FROM (SELECT ROW_NUMBER() "
				+ "OVER(PARTITION BY file_number ORDER BY B.ACTINST_START ASC) RN,B.* "
				+ " FROM BDC_WORKFLOW.V_PROJECTLIST B LEFT JOIN  SMWB_FRAMEWORK.T_USER A  ON b.STAFF_ID=A.ID "
				+ " LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT D ON A.DEPARTMENTID = D.ID "
				+ " WHERE b.STAFF_ID = '"+staffid+"'  AND ACTINST_STATUS = '"+xmstatus+"' "
				+ " AND ACTINST_END >= TO_DATE ('"+startString+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')"
				+ " AND ACTINST_END <= TO_DATE( '"+endString+" "+"23:59:59"+" ','yyyy-mm-dd hh24:mi:ss')) WHERE RN = 1");
		msg.setTotal(count);
		msg.setRows(list);
		return msg;
	}

//	public int  uploadDisk(long fileSize) {
//		//空间对比,接近上限的时候,跟换盘符
////		1.判断是否是硬盘上传
//		   String type = ConfigHelper.getNameByValue("MATERIALTYPE");
//			if(type.equals("1")) {
//				//说明是收件资料是硬盘 上传模式
////				2.做出对比,当上传的文件大于盘符数据的时候,更换盘符
//				List<T_CONFIG> hardDisksUse =commonDao.getDataList(T_CONFIG.class, "select * from "+Common.SMWBSUPPORT+" T_CONFIG where classname='工作流上传资料配置'  and name='MATERIAL'");
//				T_CONFIG  hardDisksConfig =hardDisksUse.get(0);
//				String [] hardDisksConfigsUseStr=hardDisksConfig.getVALUE().split(":");
//				File diskPartition = new File(hardDisksConfigsUseStr[0]+":");
//				long freePartitionSpace = diskPartition.getFreeSpace(); //硬盘可用空间g
//				String limit=ConfigHelper.getNameByValue("LIMIT");
//				long limitnum=Integer.valueOf(limit);
//				if(fileSize>(freePartitionSpace-limitnum*1024*1024)) {
////				if(true) {//模拟硬盘已满
//					//说明空间不足,更换盘符
//					String hardDisksConfigs = ConfigHelper.getNameByValue("harddisks");
//					String[]  hardDisks =hardDisksConfigs.split(",");
//					for(String hardDisk: hardDisks) {
//						diskPartition=new File(hardDisk+":");
//						if(fileSize<(freePartitionSpace-limitnum*1024*1024)) {
//							hardDisksConfig.setVALUE( hardDisk+":"+hardDisksConfigsUseStr[1]);
//							commonDao.update(hardDisksConfig);
//							System.out.println("空间不足,更换存蓄硬盘成功");
//							return 1;
//						}
//					}
//					System.out.println("空间不足,更换存蓄硬盘失败");
//					return 3;
//		        }
//				System.out.println("硬盘空间足够,可以上传");
//				return 2;
//			}
//			System.out.println("非硬盘上传模式,不进行空间对比");
//			return -1; 
//	
//		}
	
	/**
	 * 写入网签信息。JOE
	 * */
	public Map writehouseinfo(String xmbh,Map<String,String> houseinfo,String qlid) {
		Map<String,String> msg = new HashMap<String,String>();
		try{
			String wheresql = MessageFormat.format("QLID=''{0}'' AND XMBH=''{1}''" , qlid, xmbh);
			List<BDCS_FSQL_GZ> list = commonDao.findList(BDCS_FSQL_GZ.class, wheresql);
			if (list != null && list.size() > 0) {
				BDCS_FSQL_GZ fsql = list.get(0);
				fsql.setHTBH(StringHelper.formatObject(houseinfo.get("htbh")));
				fsql.setHTSJ(StringHelper.FormatByDate(houseinfo.get("htsj")));
				fsql.setBASJ(StringHelper.FormatByDate(houseinfo.get("basj")));
				fsql.setHTCJJG(StringHelper.getDouble(houseinfo.get("htcjjg")));
				commonDao.save(fsql);
				commonDao.flush();
				//同时保存当前时间为网签确认时间
				ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
				if (!StringHelper.isEmpty(info.getProject_id())) {
					List<Wfi_ProinstDate> proinstDates = commonDao.findList(Wfi_ProinstDate.class, MessageFormat.format("FILE_NUMBER=''{0}''", info.getProject_id()));
					if (proinstDates != null && proinstDates.size() > 0) {
						Wfi_ProinstDate proinstDate = proinstDates.get(0);
						proinstDate.setNetSignConfirmTime(new Date());
						commonDao.update(proinstDate);
						commonDao.flush();
					}
				}
				msg.put("success", "true");
				msg.put("msg", "更新成功");
			} else {
				msg.put("success", "false");
				msg.put("msg", "更新失败，未找到对应附属权利!");
			}
		} catch(Exception ex){
			msg.put("success", "false");
			msg.put("msg", "更新失败："+ex.getMessage());
		}
		return msg;
	}
	
	/**
	 * 
	 * @Description: 写入网签审核意见 
	 * @author suhaibin
	 * @date 2019年6月6日 下午4:20:46
	 */
	public Map updateAuditOpinions(String xmbh,String AuditOpinions){
		
		Map<String,String> msg = new HashMap<String,String>();
		
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(!StringHelper.isEmpty(info.getProject_id())){
			List<Wfi_ProinstDate> proinstDates = commonDao.findList(Wfi_ProinstDate.class, MessageFormat.format("FILE_NUMBER=''{0}''", info.getProject_id()));
			if(proinstDates != null && proinstDates.size() > 0){
			
				Wfi_ProinstDate proinstDate = proinstDates.get(0);
				proinstDate.setNetAuditOpinions(AuditOpinions);
				proinstDate.setNetSignConfirmTime(new Date());
				commonDao.update(proinstDate);
				commonDao.flush();
				
				msg.put("success", "true");
				msg.put("msg", "驳回成功");
				
			}else{
				msg.put("success", "false");
				msg.put("msg", "驳回失败：查无网签信息");
			}
			
		}else{
			msg.put("success", "false");
			msg.put("msg", "驳回失败");
		}
		return msg;
	} 
	
	/**
	 * 
	 * @Description: 查看网签审核意见 
	 * @author suhaibin
	 * @date 2019年6月6日 下午4:21:09
	 */
	public Wfi_ProinstDate loadProinstDate(String xmbh){
		
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(!StringHelper.isEmpty(info.getProject_id())){
			List<Wfi_ProinstDate> proinstDates = commonDao.findList(Wfi_ProinstDate.class, MessageFormat.format("FILE_NUMBER=''{0}''", info.getProject_id()));
			if(proinstDates != null && proinstDates.size() > 0){
				Wfi_ProinstDate proinstDate = proinstDates.get(0);
				if(proinstDate != null){
					return proinstDate;
				}else{
					return null;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 写入税务信息。
	 * */
	public Map writetaxinfo(String xmbh, String qlid, String data) {
		Map<String,String> msg = new HashMap<String,String>();
		JSONArray jsonArray = com.alibaba.fastjson.JSONObject.parseArray(data);
		try{
			User user = Global.getCurrentUserInfo();
			String wheresql = MessageFormat.format("QLID=''{0}'' AND XMBH=''{1}''" , qlid, xmbh);
			List<BDCS_FSQL_GZ> list = commonDao.findList(BDCS_FSQL_GZ.class, wheresql);
			if (list != null && list.size() > 0) {
				BDCS_FSQL_GZ fsql = list.get(0);
				String delsql = MessageFormat.format("DELETE BDCK.BDCS_WSXX T WHERE T.FSQLID=''{0}''",fsql.getId());
				int deleteQuery = commonDao.deleteQuery(delsql);
				commonDao.flush();
				String wspzh = "";
				String wsje = "";
				String wssj = "";
				//一套房子有可能缴纳多个税种，存储fsqlid进行关联
				for (int i=0; i<jsonArray.size(); i++) {
					com.alibaba.fastjson.JSONObject obj = jsonArray.getJSONObject(i);
					wspzh = obj.getString("wspzh");
					wsje = obj.getString("wsje");
					wssj = obj.getString("wssj");
					BDCS_WSXX wsxx = new BDCS_WSXX();
					wsxx.setFSQLID(fsql.getId());
					wsxx.setXMBH(xmbh);
					wsxx.setWSPZH(wspzh);
					wsxx.setWSJE(StringHelper.getDouble(obj.getString("wsje")));
					wsxx.setWSSJ(StringHelper.FormatByDate(obj.getString("wssj")));
					wsxx.setSSZL(obj.getString("sszl"));
					wsxx.setSSZLMC(StringHelper.formatObject(ConstValue.SSZL.initFrom(obj.getString("sszl"))));
					wsxx.setCREATETIME(new Date());
					wsxx.setYXBZ("0");
					wsxx.setTIME(new Date());
					if(user != null){
						wsxx.setUSERID(user.getId());
						wsxx.setUSERNAME(user.getUserName());
					}
					commonDao.save(wsxx);
					commonDao.flush();
				}
				//附属权利只保存一个，方便用来判断是否完税
				fsql.setWSPZH(wspzh);
				fsql.setWSSJ(StringHelper.FormatByDate(wssj));
				fsql.setWSJE(StringHelper.getDouble(wsje));
				commonDao.save(fsql);
				commonDao.flush();
				//同时保存当前时间为税务确认完税时间
				ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
				if (!StringHelper.isEmpty(info.getProject_id())) {
					List<Wfi_ProinstDate> proinstDates = commonDao.findList(Wfi_ProinstDate.class, MessageFormat.format("FILE_NUMBER=''{0}''", info.getProject_id()));
					if (proinstDates != null && proinstDates.size() > 0) {
						Wfi_ProinstDate proinstDate = proinstDates.get(0);
						proinstDate.setTaxConfirmTime(new Date());
						commonDao.update(proinstDate);
						commonDao.flush();
					}
				}
				msg.put("success", "true");
				msg.put("msg", "更新成功");
			} else {
				msg.put("success", "false");
				msg.put("msg", "更新失败，未找到对应附属权利!");
			}
		} catch(Exception ex){
			msg.put("success", "false");
			msg.put("msg", "更新失败："+ex.getMessage());
		}
		return msg;
	}
	/**
	 * 给房产看的项目列表，用于网签，包括已经办在办。可过滤具体流程。JOE
	 * */
	public Message getAllProjectListForHouseOffice(QueryCriteria query, List<String> workflowname, String flag) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		String qlr = query.getQlr();
		String ywr = query.getYwr();
		String zjh = query.getZjh();
		String qzh = query.getQzh();
		String zl = query.getZl();
		String prolshs = "";
		if(!StringHelper.isEmpty(zl)){
			prolshs=projectService.getYwlshByCondtionsEx("", "", "", "", zl,true);
		}

		if(!StringHelper.isEmpty(prolshs)){
			String replaceend = "";
			String[] arrs = prolshs.split(",");
			if(arrs.length>50){
				prolshs = "";
				for(int i=0;i<50;i++){
					if(i==49){
						prolshs+="'"+arrs[i]+"'";
					}else{
						prolshs+="'"+arrs[i]+"',";
					}
				}
			}else{
				replaceend = prolshs.replaceAll(",", "','");
				prolshs = "'"+replaceend+"'";
			}
		}


		String key =query.getValue();
		String areaCode=query.getAreaCode();
		String ywh=query.getYwh();
		String lshstart=query.getLshStart();
		String lshend=query.getLshEnd();
		String status=query.getStatus();
		String prodefname=query.getProdefName();
		String urgency=query.getUrgency();
		String outtime=query.getOutTime();
		String proStatus=query.getProStatus();
		String passback=query.getPassBack();
		String sqr=query.getSqr();
		if(!StringHelper.isEmpty(query.getSqr())){
			 sqr=query.getSqr().replace(" ", "");
		}
		
		//String deadline = query.getDeadline();
		String departmt = query.getDepartmt();
		
		int CurrentPageIndex=Integer.parseInt(query.getCurrentPageIndex());
		int pageSize=Integer.parseInt(query.getPageSize());
		str.append("1>0 ");
		if (key != null && !"".equals(key)) {
			str.append(" and (PROJECT_NAME like '%");
			str.append(key);
/*			str.append("%' or FILE_NUMBER like '%");
			str.append(key);*/
			str.append("%' ");
			str.append(" or PROLSH like '%");
			str.append(key);
			str.append("%' ");
			str.append(" or BATCH like '%");
			str.append(key);
			str.append("%'");
			str.append(")");
			// 支持权利人查询
		}
		
		// 流水起始号
		if (lshstart != null && !lshstart.equals("")) {
			str.append("and PROLSH>='"+lshstart+"'");
		}
		// 流水起始号
		if (lshend != null && !lshend.equals("")) {
			str.append("and PROLSH<='"+lshend+"'");
		}

		if(!StringHelper.isEmpty(prolshs)){
			str.append(" and PROLSH in ("+prolshs+")");
		}

		if(areaCode!=null&&!areaCode.isEmpty()){
			str.append(" and AREACODE = '"+areaCode+"' ");
		}
		
		
	
		
		if(!StringHelper.isEmpty(ywh)){
			str.append(" and YWH like '%"+ywh+"%' ");
		}
		if(lshstart != null && !lshstart.equals("")){
			str.append(" and PROLSH>='"+lshstart+"' ");
		}
		if(lshend != null && !lshend.equals("")){
			str.append(" and PROLSH<='"+lshend+"' ");
		}
		if (status != null && !"".equals(status)) {
			str.append(" and ACTINST_STATUS in (");
			str.append(status);
			str.append(")");
		}
		// 流程名称
		if (prodefname != null && !"".equals(prodefname)) {
			str.append(" and prodef_name like'");
			str.append(prodefname);
			str.append("%'");
		}
		if (urgency != null && !urgency.equals("")) {
			str.append(" and urgency=");
			str.append(urgency);
		}
		if (outtime != null && !outtime.equals("")) {
			str.append(" and proouttime>0");
		}
			
		if(proStatus!=null&&!proStatus.equals("")){
			str.append(" and proinst_status  not in('0') ");
			Date nowDate = new Date();
			Date date=smHoliday.FactDate(nowDate,2);
			java.sql.Date sqlNowDate=new java.sql.Date(nowDate.getTime());
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			if(proStatus.equals("jjcq")){
				str.append(" and proinst_willfinish > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') and proinst_willfinish< TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(proStatus.equals("ycq")){
				str.append(" and proinst_willfinish < TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else{
				str.append(" and proinst_willfinish < TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			
		}
		if (passback != null && !passback.equals("")) {
			str.append(" and operation_type=");
			str.append(WFConst.Operate_Type.PRODUCT_anglesPicture.value);
		}
        String tabel =
			"SELECT\n" +
			"             PO.PROJECT_NAME,\n" + 
			"             PO.ACTDEF_TYPE,\n" + 
			"             PO.INSTANCE_TYPE,\n" +
			"             PO.FILE_NUMBER,\n" + 
			"             PO.STAFF_ID_NACT AS ACTSTAFF,\n" + 
			"             PO.STAFF_NAME_NACT as actstaffname,\n" + 
			"             PO.YWH,\n" + 
			"             PO.MSG,\n" +
			"             PO.BATCH,\n" +
			"             PO.PROINST_ID,PO.PRODEF_ID,\n" + 
			"             PO.PRODEF_NAME,PO.PROINST_STATUS,\n" + 
			"             PO.PROINST_END,PO.PROINST_START,PO.PROINST_WILLFINISH,\n" + 
			"             PO.PROINST_CODE,\n" + 
			"             PO.ACTINST_ID,\n" + 
//			"			  PO.PRODEF_ID,\n" + 
			"             PO.OPERATION_TYPE_NACT AS OPERATION_TYPE,\n" + 
			"             PO.STAFF_ID_NACT AS STAFF_ID,\n" + 
			"             PO.ACTINST_NAME,\n" + 
			"             PO.STAFF_NAME_NACT AS Staff_Name ,PO.AREACODE,\n" + 
			"             PO.ACTINST_STATUS,PO.ACTINST_START,PO.ACTINST_END,PO.ACTINST_WILLFINISH,\n" + 
			"             PO.PROLSH,PO.CODEAL,PO.Urgency,PO.Proinst_Time,PO.PROINST_WEIGHT\n" + 
			"             FROM  BDC_WORKFLOW.WFI_PROINST PO ";
        if (sqr != null && !"".equals(sqr)) {
			if (isChinese(sqr)) {
				str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%" + sqr + "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=PO.FILE_NUMBER) ");
			}
		}


        if("swdb".equals(flag)) {
            str.append("and EXISTS (SELECT 1\n" +
                    " FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL\n" +
                    " ON FSQL.XMBH = XMXX.XMBH\n" +
                    " WHERE FSQL.WSPZH IS NULL AND XMXX.PROJECT_ID = PO.FILE_NUMBER  )");
        }
        if("swyb".equals(flag)) {
            str.append("and EXISTS (SELECT 1\n" +
                    " FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL\n" +
                    " ON FSQL.XMBH = XMXX.XMBH\n" +
                    " WHERE FSQL.WSPZH IS NOT NULL AND XMXX.PROJECT_ID = PO.FILE_NUMBER  )");
        }

        if("wqdb".equals(flag)) {
            str.append("and EXISTS (SELECT 1 FROM \n" +
                    " BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL\n" +
                    " ON FSQL.XMBH = XMXX.XMBH\n" +
					" LEFT JOIN BDC_WORKFLOW.WFI_PROINSTDATE PRODATE\n" +
					" ON XMXX.PROJECT_ID = PRODATE.FILE_NUMBER" +
                    " WHERE (FSQL.HTBH IS NULL AND PRODATE.NETAUDITOPINIONS IS NULL)  and XMXX.PROJECT_ID = PO.FILE_NUMBER )") ;
        }
        if("wqyb".equals(flag)) {
            str.append("and EXISTS (SELECT 1 FROM \n" +
                    " BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL\n" +
                    " ON FSQL.XMBH = XMXX.XMBH\n" +
					" LEFT JOIN BDC_WORKFLOW.WFI_PROINSTDATE PRODATE\n" +
					" ON XMXX.PROJECT_ID = PRODATE.FILE_NUMBER " +
                    " WHERE (FSQL.HTBH IS NOT NULL OR PRODATE.NETAUDITOPINIONS IS NOT NULL)  and XMXX.PROJECT_ID = PO.FILE_NUMBER )") ;
        }

//        str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX where XMXX.SFDB<>1 and XMXX.PROJECT_ID=PO.FILE_NUMBER) ");
		if(workflowname!=null&&workflowname.size()>0)
		{
		str.append(" and exists  (select 1 from bdc_workflow.WFD_MAPPING maping left join BDC_WORKFLOW.WFD_PRODEF PRODEF on maping.WORKFLOWCODE=PRODEF.PRODEF_CODE where PRODEF.HOUSE_STATUS='1' and instr(PO.FILE_NUMBER,'-'||maping.workflowcode||'-')>0 and maping.WORKFLOWNAME in (");
            for(int i=0;i<workflowname.size();i++)
            {
                String name = workflowname.get(i);
                str.append("'"+name+"'");
                if(i!=(workflowname.size()-1)) {
                    str.append(",");
                }
            }
            str.append(") )");
        }
        long tatalCount = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST po where " + str.toString());
		msg.setTotal(tatalCount);
		if (tatalCount > 0) {  
			
			str.append(" ORDER BY PO.ACTINST_START DESC ");
			List<Map> resMap = commonDao.getDataListByFullSql(tabel+" where " + str.toString(), CurrentPageIndex, pageSize);
			List<Map> result = new ArrayList<Map>();
            for (Map m : resMap) {
                //判断改项目 所有单元是否全部填了合同编号。0为否 1为是
//                Long r=	commonDao.getCountByFullSqlCustom("SELECT CASE WHEN COUNT(HTBH)=0 THEN 0 ELSE 1 END AS R FROM BDCK.BDCS_FSQL_GZ FSQL LEFT JOIN BDCK.BDCS_XMXX XMXX ON FSQL.XMBH=XMXX.XMBH WHERE XMXX.PROJECT_ID='"+m.get("FILE_NUMBER")+"' AND FSQL.HTBH IS NOT NULL");
//                String taxsql = "SELECT WSPZH FROM BDCK.BDCS_FSQL_GZ FSQL  LEFT JOIN BDCK.BDCS_XMXX XMXX ON FSQL.XMBH=XMXX.XMBH WHERE XMXX.PROJECT_ID='"+m.get("FILE_NUMBER")+"' AND FSQL.WSPZH IS NOT NULL";
//                List<Map> taxlist = commonDao.getDataListByFullSql(taxsql);

				Long netbh = commonDao.getCountByFullSql(" from BDC_WORKFLOW.WFI_PROINSTDATE t where t.FILE_NUMBER='"+m.get("FILE_NUMBER")+"'");
                List<LOG_API> apilogs=commonDao.getDataList(LOG_API.class,"select * from log.log_api where BZ='"+m.get("FILE_NUMBER")+"' ORDER BY optime desc ");
                //是否成功推送信息给主键部门
                Boolean successpush=false;
                //API信息 ，用于前端重新推送。
                Map<String,String> apiinfo=new HashMap<String, String>();
                m.put("APIINFO", "false");
                 if(apilogs!=null&&apilogs.size()>0&&apilogs.get(0).getSUCCESS().equalsIgnoreCase("true"))
                 {
                     successpush=true;
                     m.put("APIINFO", "true");
                 }


				if("swdb".equals(flag)) {
					m.put("WSPZH", "未完税");
                    m.put("taxflag", false);
				}
				if("swyb".equals(flag)) {
					m.put("WSPZH", "已完税");
                    m.put("taxflag", true);
				}

				if("wqdb".equals(flag)) {
					m.put("HTBH", "未网签");
					m.put("netflag", false);
				}
				if("wqyb".equals(flag)) {
					if(netbh>0) {
						m.put("HTBH", "已驳回");
					} else {
						m.put("HTBH", "已网签");
					}
					m.put("netflag", true);
				}

//				m.put("HTBH", r==0?"未网签":"已网签");
//                 if(r!=0) {
//                     m.put("netflag", true);
//                 }else {
//                	 m.put("netflag", false);
//                 }

//                if (taxlist != null && taxlist.size() > 0 && !StringHelper.isEmpty(taxlist.get(0).get("WSPZH"))) {
//                    m.put("WSPZH", "已完税");
//                    m.put("taxflag", true);
//                } else {
//                    m.put("WSPZH", "未完税");
//                    m.put("taxflag", false);
//                }






                if(successpush) {
                    m.put("PUSHSUCCESS", "已推送");
                } else {
                    m.put("PUSHSUCCESS", "未推送");
                }

                Date proend = (Date) m.get("PROINST_WILLFINISH");
				Date actend = (Date) m.get("ACTINST_WILLFINISH");
				Date start = new Date();
				if (actend == null) {
					actend = new Date();
				}
				
				String prodef = m.get("PRODEF_ID").toString();
				String proinst = m.get("PROINST_ID").toString();
				Wfd_Prodef def = commonDao.get(Wfd_Prodef.class, prodef);
				Object proinst_time = m.get("PROINST_TIME");
				if (proend == null || (def.getProdef_Time()!=null&&!def.getProdef_Time().equals(proinst_time))) {
					Wfi_ProInst inst = commonDao.get(Wfi_ProInst.class, proinst);
					// 计算时间并插入
					if (def != null) {
						GregorianCalendar calother = new GregorianCalendar();
						calother.setTime(inst.getProinst_Start());
						if (def.getProdef_Time() != null) {
							if (inst.getProinst_Willfinish() == null) {
								inst.setProinst_Willfinish(smHoliday.addDateByWorkDay(calother, def.getProdef_Time()));
								inst.setProinst_Time(def.getProdef_Time());
								commonDao.update(inst);
								commonDao.flush();
							}
							proend = inst.getProinst_Willfinish();
						} else {
							proend = new Date();
						}
					}
				}
				//TODO:计算OUTTIME 和 PROOUTTIME
				Date currentDate = new Date();
				long ss=currentDate.getTime()-actend.getTime();
				long pro = currentDate.getTime()-proend.getTime();
				int OUTTIME =(int)Math.ceil(ss/(24*60*60*60));
				int PROOUTTIME =(int)Math.ceil(pro/60);
				m.put("OUTTIME", OUTTIME);
				m.put("PROOUTTIME", PROOUTTIME);
				
				long mscond = smHoliday.ComputerDiff(start, proend);
				GregorianCalendar cal = new GregorianCalendar();
				GregorianCalendar cal2 = new GregorianCalendar();
				cal.setTime(start);
				if (-2147483648 < mscond && mscond < 2147483647) {
					cal.add(Calendar.MILLISECOND, (int) mscond);
				} else {
					cal.add(Calendar.MINUTE, (int) (mscond / (1000 * 60)));
				}
				m.put("PROINST_WILLFINISH", cal.getTime());
				// smHoliday.addDateByWorkDay(cal,acttime)
				long mscond2 = smHoliday.ComputerDiff(start, actend);
				cal2.setTime(start);
				if (-2147483648 < mscond && mscond < 2147483647) {
					cal2.add(Calendar.MILLISECOND, (int) mscond2);
				} else {
					cal2.add(Calendar.MINUTE, (int) (mscond2 / (1000 * 60)));
				}
				m.put("ACTINST_WILLFINISH", cal2.getTime());
				m.put("SERVERTIME", new Date());
                
                
              }
			msg.setRows(resMap);
		}
		return msg;
	}
	/**
	 * 重新推送信息给住建部门
	 * */
	public Map refreshapi(String ywh) {
		Map<String,String> msg = new HashMap<String,String>();
		try{
			List<LOG_API> apilogs=commonDao.getDataList(LOG_API.class,"select * from log.log_api where BZ='"+ywh+"'");
			if(apilogs!=null&&apilogs.size()>0)
			{
				com.alibaba.fastjson.JSONObject  jsonObject = com.alibaba.fastjson.JSONObject.parseObject(apilogs.get(0).getAPIPARAM());
				Map<String,Object> map = (Map<String,Object>)jsonObject;
				Map<String,String> param = new HashMap<String,String>();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					param.put(entry.getKey(),(String)entry.getValue());

				}
				String error="";
				String r = HttpRequest.sendPost(apilogs.get(0).getAPIURL(),param );
				if (r.equals("")) {
					error = "推送失败";
				} else {
					com.alibaba.fastjson.JSONObject o = JSON.parseObject(r);
					if (o != null && o.getString("success") != null && o.getString("success").equalsIgnoreCase("true")) {

					} else
						error = o.getString("msg");
				}
				if(error.equals("")) {
					msg.put("success", "true");
					msg.put("msg", "推送成功");
					apilogs.get(0).setSUCCESS("true");
					apilogs.get(0).setOPTIME(new Date());
					apilogs.get(0).setERROR("");
					commonDao.saveOrUpdate(apilogs.get(0));
					commonDao.flush();
				}
				else
				{
					msg.put("success", "false");
					msg.put("msg", error);
				}
			}
			else {
				msg.put("success", "false");
				msg.put("msg", "推送失败，未找到对应接口记录!");
			}
		} catch(Exception ex){
			msg.put("success", "false");
			msg.put("msg", "推送失败："+ex.getMessage());
		}
		return msg;
	}
	/**
	 * 只查询及时办结的项目
	 * @param query
	 * @return
	 */
	public Message getProjectjsbjList(QueryCriteria query) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		String key =query.getValue();		
		int CurrentPageIndex=Integer.parseInt(query.getCurrentPageIndex());
		int pageSize=Integer.parseInt(query.getPageSize());
		str.append("1>0 and PO.sfjsbl in ('1','2')");
		if (key != null && !"".equals(key)) {
			str.append(" and (PO.PROJECT_NAME like '%");
			str.append(key);
			str.append("%' ");
			str.append(" or PO.PROLSH like '%");
			str.append(key);
			str.append("%' )");
		}
		
		String tabel =
				"select\n "
				+ "po.project_name,\n"
				+ "po.staff_name_nact,\n"
				+ "po.prolsh,pr.smssendingtime,\n"
				+ "pr.actinst_start_time,\n"
				+ "pr.actinst_end_time,\n"
				+ "pr.netsignconfirmtime,\n"
				+ "pr.taxconfirmtime,\n"
				+ "pr.dbendtime,\n"
				+ "pr.fzstarttime,\n"
				+ "pr.fzendtime from bdc_workflow.wfi_proinst po left join bdc_workflow.wfi_proinstdate pr on po.file_number = pr.file_number ";
		long tatalCount = commonDao.getCountByFullSql("from bdc_workflow.wfi_proinst po left join bdc_workflow.wfi_proinstdate pr on po.file_number = pr.file_number  where " + str.toString());		
		msg.setTotal(tatalCount);
		if(tatalCount > 0){
			str.append(" order by PO.PROLSH DESC " );
			List<Map> resMap = commonDao.getDataListByFullSql(tabel+" where " + str.toString(), CurrentPageIndex, pageSize);
			msg.setRows(resMap);
		}
		
		return msg;
	}

	/**
	 * 短信发送:针对玉林、博白模式另外整了一套
	 * @param filenumber
	 * @return
	 */
	public boolean SmsSend(String filenumber){

		boolean succcess = false;
		if (StringHelper.isEmpty(filenumber)) {
			return succcess;
		}
		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		User user = Global.getCurrentUserInfo();
		String userid="";
		String username="";
		if(user != null){
			username = user.getLoginName();
			userid = user.getId();
		}
		List<BDCS_XMXX> xmxx = commonDao.findList(BDCS_XMXX.class, " PROJECT_ID='" + filenumber + "'");
		//项目信息
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(filenumber);
		if (info == null) {
			return false;
		}
		//查解封无需要短信通知
		if ("800".equals(info.getDjlx())) {
			return false;
		}
		String[] qllxs = {"4","6","8"};//产权权利类型

		//SQLLB 1:权利人 2:义务人
		//获取申请人信息
		List<BDCS_SQR> sqrs = projectService.getSQRList(info.getXmbh());
		for(BDCS_SQR sqr :sqrs){
			if ("2".equals(sqr.getSQRLB())) {
				if (!("400".equals(info.getDjlx()) && "23".equals(info.getQllx()))) {
					continue;
				}
			}

			String qlrid = "";
			String qlrlx = "";
			String zsid = "";
			String bdcqzh = "";
			String zsbh = "";
			String qlr = "XMBH ='"+info.getXmbh()+"' AND SQRID ='"+sqr.getId()+"'" ;
			Date szsj = null;
			List<BDCS_QLR_GZ> qlrs = commonDao.findList(BDCS_QLR_GZ.class, qlr);
			if(qlrs != null && qlrs.size()>0){
				qlrid = qlrs.get(0).getId();
				bdcqzh = qlrs.get(0).getBDCQZH();
				qlrlx = qlrs.get(0).getQLRLX();
			}
			String qdlr = "XMBH ='"+info.getXmbh()+"' AND QLRID ='"+qlrid+"'" ;
			List<BDCS_QDZR_GZ> qdzrs = commonDao.findList(BDCS_QDZR_GZ.class, qdlr);
			if(qdzrs != null && qdzrs.size()>0){
				zsid = qdzrs.get(0).getZSID();
			}
			String zs = "XMBH ='"+info.getXmbh()+"' AND ZSID ='"+zsid+"'" ;
			List<BDCS_ZS_GZ> zss = commonDao.findList(BDCS_ZS_GZ.class, zs);
			if(zss != null && zss.size()>0){
				zsbh = zss.get(0).getZSBH();
			}
			List<BDCS_DJSZ> szs = commonDao.findList(BDCS_DJSZ.class, "XMBH ='"+info.getXmbh()+"'");
			if(szs != null && szs.size()>0){
				szsj = szs.get(0).getSZSJ();
			}

			LOG_DXTS dxts = new LOG_DXTS();
			dxts.setID((String) SuperHelper.GeneratePrimaryKey());
			//个人首次产权登记的通知权利人，其他的通知代理人
			if (Arrays.asList(qllxs).contains(info.getQllx()) && "100".equals(info.getDjlx())) {
				if (StringHelper.isEmpty(sqr.getSQRXM()) || StringHelper.isEmpty(sqr.getLXDH())) {
					continue;
				}
				dxts.setRECEIVE_PHONE(sqr.getLXDH());
				dxts.setRECEIVE_NAME(sqr.getSQRXM());
			} else if ("700".equals(info.getDjlx())) {
				//预告类的业务通知开发企业的代理人
				if (!"2".equals(sqr.getSQRLX()) || StringHelper.isEmpty(sqr.getDLRXM()) || StringHelper.isEmpty(sqr.getDLRLXDH())) {
					continue;
				}
				dxts.setRECEIVE_PHONE(sqr.getDLRLXDH());
				dxts.setRECEIVE_NAME(sqr.getDLRXM());
			} else if("400".equals(info.getDjlx()) && "23".equals(info.getQllx())) {
				//抵押注销业务通知义务人
				if (!"2".equals(sqr.getSQRLB())) {
					continue;
				}
				if ("2".equals(sqr.getSQRLX())) {
					//类型为企业通知代理人
					if (StringHelper.isEmpty(sqr.getDLRXM()) || StringHelper.isEmpty(sqr.getDLRLXDH())) {
						continue;
					}
					dxts.setRECEIVE_PHONE(sqr.getDLRLXDH());
					dxts.setRECEIVE_NAME(sqr.getDLRXM());
				} else {
					if (StringHelper.isEmpty(sqr.getSQRXM()) || StringHelper.isEmpty(sqr.getLXDH())) {
						continue;
					}
					dxts.setRECEIVE_PHONE(sqr.getLXDH());
					dxts.setRECEIVE_NAME(sqr.getSQRXM());
				}
			} else {
				if (StringHelper.isEmpty(sqr.getDLRXM()) || StringHelper.isEmpty(sqr.getDLRLXDH())) {
					//先取代理人，如果代理人为空就取权利人
					if (StringHelper.isEmpty(sqr.getSQRXM()) || StringHelper.isEmpty(sqr.getLXDH())) {
						continue;
					}
					dxts.setRECEIVE_PHONE(sqr.getLXDH());
					dxts.setRECEIVE_NAME(sqr.getSQRXM());
				} else {
					dxts.setRECEIVE_PHONE(sqr.getDLRLXDH());
					dxts.setRECEIVE_NAME(sqr.getDLRXM());
				}
			}
			dxts.setRECEIVE_INFO(info.getYwlsh());
			dxts.setXMBH(info.getXmbh());
			dxts.setSQRID(sqr.getId());
			dxts.setSQRXM(sqr.getSQRXM());
			dxts.setLXDH(sqr.getLXDH());
			dxts.setDLRXM(sqr.getDLRXM());
			dxts.setDLRLXDH(sqr.getDLRLXDH());
			dxts.setYWLSH(info.getYwlsh());
			dxts.setXMMC(info.getXmmc());
			dxts.setZSBH(zsbh);
			dxts.setBDCQZH(bdcqzh);
			dxts.setDJSJ(info.getDjsj());
			dxts.setSZSJ(szsj);
			dxts.setDJLX(info.getDjlx());
			dxts.setQLLX(info.getQllx());
			dxts.setCREATETIME(new Date());
			dxts.setSEND_NUMBER(0);
			dxts.setSEND_TYPE("0");
			dxts.setYXBZ("0");
			dxts.setSEND_STATUS("0");
			dxts.setTENANT_ID(XZQHDM);

			commonDao.save(dxts);
			commonDao.flush();
			succcess = true;
			//多个权利人仅通知一个人
			break;
		}

		return succcess;
	}
}
