package com.supermap.wisdombusiness.workflow.service.wfi;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Holiday;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.vProInstInfo;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;
import com.supermap.wisdombusiness.workflow.service.common.SmLshHelper;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmQueryPara;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;

@Component("smProInst")
public class SmProInst {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmActInst _ActInst;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmLshHelper _SmLshHelper;
	@SuppressWarnings("unused")
	private SmProDef smProDef;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private UserService userService;
	
//	//工作时间
//    private final int morningBegin = 8;//上午开始时间 8点
//    private final int morningEnd   = 12;//上午结束时间 12点
// 
//    private final int    afternoonBegin = 14;//下午开始时间 14点
//    private final int    afternoonEnd   = 18;//结束时间   18点
	
	/**
	 * 办结项目
	 * 
	 * @param actInst_ID
	 *            String 活动实例ID
	 * @param staff_ID
	 *            String 员工ID
	 * */
	public void DoneProInst(String actInst_ID, String staff_ID) {
		_ActInst.PassOver(actInst_ID, staff_ID);

	}

	/**
	 * 获取活动实例的流程实例ID
	 * 
	 * @param actInstID
	 *            String 活动实例ID
	 * @return String 流程实例ID
	 * */
	public String GetProInstIDByActInstId(String actInstID) {
		if (actInstID != null && actInstID != "") {
			Wfi_ActInst inst = commonDao.load(Wfi_ActInst.class, actInstID);
			if (inst != null) {
				return inst.getProinst_Id();
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 创建项目，按小时计算
	 * @param proinfo
	 * @param house_Status
	 * @return
	 */
	public Wfi_ProInst CreatProInst(SmProInfo proinfo,Integer house_Status){

		Wfi_ProInst proInst = null;
		StringBuilder _ProjectName = new StringBuilder();
		try {

			// 组合项目名称
			_ProjectName.append(proinfo.getProInst_Name());
			proInst = new Wfi_ProInst();
			proInst.setProinst_Id(Common.CreatUUID());
			proInst.setProdef_Id(proinfo.getProDef_ID());
			proInst.setAcceptor(proinfo.getAcceptor());
			if (!StringHelper.isEmpty(proinfo.getSQStartTime())) {
				proInst.setCreat_Date(StringHelper.FormatByDate2(proinfo.getSQStartTime()));
			} else {
				proInst.setCreat_Date(new Date());
			}
			proInst.setDistrict_Id(proinfo.getDistrict_ID());
			proInst.setProdef_Name(proinfo.getProDef_Name());
			proInst.setFromInline(proinfo.getFromInline());
			//判断时间
			if(proinfo.getProStartTime()!=null&&!proinfo.getProStartTime().equals("")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    proInst.setProinst_Start(formatter.parse(proinfo.getProStartTime()));
			}else{
				if (!StringHelper.isEmpty(proinfo.getSQStartTime())) {
					proInst.setProinst_Start(StringHelper.FormatByDate2(proinfo.getSQStartTime()));
				} else {
					proInst.setProinst_Start(new Date());
				}
			}
			proInst.setProinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			if(proinfo.getProjectName()!=null&&!proinfo.getProjectName().equals("")){
				proInst.setProject_Name(proinfo.getProjectName());
			}else{
				proInst.setProject_Name(_ProjectName.toString());
			}
			proInst.setUrgency(Integer.parseInt(proinfo.getFile_Urgency()));
			proInst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
			Wfd_Prodef prodef = commonDao.get(Wfd_Prodef.class, proinfo.getProDef_ID());
			if (prodef != null && prodef.getTransaction_Type() != null && prodef.getTransaction_Type() > proInst.getUrgency()) {
				proInst.setUrgency(prodef.getTransaction_Type());
			}
			proInst.setFile_Number(GetFileNumber(prodef.getProdef_Code()));
			proInst.setProinst_Time(prodef.getProdef_Time());
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			if(proinfo.getProWillFinishTime()!=null&&!proinfo.getProWillFinishTime().equals("")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    proInst.setProinst_Willfinish(formatter.parse(proinfo.getProWillFinishTime()));
			}
			if (prodef.getProdef_Time() != null) {
				//判断时间
				if(null==proInst.getProinst_Willfinish()){
					GregorianCalendar date = getProcessTime(prodef, cal);
					proInst.setProinst_Willfinish(date.getTime());
				}
			}
            if(proinfo.getProLSH()!=null&&!proinfo.getProLSH().equals("")){
            	proInst.setProlsh(proinfo.getProLSH());
            }else{
            	//此处在调用smstaff类依据staffid获取当前用户行政区划代码时候 直接传递本次服务请求中已经通过全局获取到的staffid 否则后台调服务导致目标类无发获取浏览器缓存中的用户ID
            	String staffid=null!=proinfo.getStaffID()?proinfo.getStaffID():smStaff.getCurrentWorkStaffID();
            	String areaCode = smStaff.GetCurrentAreaCode(staffid);
            	proInst.setProlsh(CreatMaxID("0", "PROLSH"));
            	String lshConfig = ConfigHelper.getNameByValue("lshConfig");
            	if("2".equals(lshConfig)){
            		if(null!=areaCode&&!areaCode.isEmpty()){
            			String lsh = proInst.getProlsh();
            			lsh = areaCode.substring(areaCode.length()-2) + lsh.substring(2);
            			proInst.setProlsh(lsh);
            		}
            	}
            }
			if (proInst.getProject_Name() == null || proInst.getProject_Name().equals("")) {
				proInst.setProject_Name(proInst.getProlsh());
			}
			proInst.setRemarks(proinfo.getMessage());
			proInst.setProinst_Code(prodef.getProdef_Code());
			// commonDao.clear();
			// 如果自动化配置表办理人员不为空，则设置配置的办理人员
			if (!StringHelper.isEmpty(proinfo.getStaffID())) {
				proInst.setStaff_Id(proinfo.getStaffID());
			} else {
				proInst.setStaff_Id(smStaff.getCurrentWorkStaffID());
			}
			proInst.setBatch(proinfo.getBatch());
			proInst.setYwh(proinfo.getYwh());
			proInst.setSfjslb("2");
			proInst = SetWeight(proInst);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return proInst;
	
	}

	/**
	 * 一天办件（以小时的方式计算流程时长）
	 * @param prodef
	 * @param cal
	 * @return
	 */
	public GregorianCalendar getProcessTime(Wfd_Prodef prodef, GregorianCalendar cal) {
//		String strConfig = ConfigHelper.getNameByValue("WORKING");
//		String [] arrys = strConfig.split("\\,|-");
		Date date = smHoliday.addDateByWorkDay(cal, prodef.getProdef_Time());//以一天时间进行计算
//		String strs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
//		System.out.println("流程预计时长："+strs);
	
//		/**计算根据本地化配置*/
//		String[] am = arrys[0].split(":");
//		String[] an = arrys[1].split(":");
//		String [] pm = arrys[2].split(":");
//		String [] pn = arrys[3].split(":");
//		int ami=Integer.parseInt(am[1])/60;
//		int morningBegin=Integer.parseInt(am[0])+ami;//上午上班
//		int ani=Integer.parseInt(an[1])/60;
//		int morningEnd=Integer.parseInt(an[0])+ani;//上午下班
//		
//		int pmi=Integer.parseInt(pm[1])/60;
//		int afternoonBegin=Integer.parseInt(pm[0])+pmi;//下午上班
//		
//		int pni=Integer.parseInt(pn[1])/60;
//		int afternoonEnd=Integer.parseInt(pn[0])+pni;//下午下班
//		
		long freeWork = 1000*60*1020;//去除17个小时，剩下的是流程时长
//		long nightDate = 1000*60*(24-afternoonEnd+morningBegin)*60;//晚上休息的时间段
		long miss = date.getTime();
		long workTime = miss - freeWork;//创建项目时间-去非工作时间
//		Calendar c = Calendar.getInstance();
//		long pm12 = 1000*60*morningEnd*60;
//		long pm14 = 1000*60*afternoonBegin*60;	
//		int house = c.get(Calendar.HOUR_OF_DAY);
//		int minute=c.get(Calendar.MINUTE);
//		int minms =  minute/60;
//		int house_min = house+minms;
//		if(house_min >= morningBegin && house_min <= morningEnd){//如果是在12-14点休息之前创建流程，加上中午休息的2个小时；
//			if(house_min == morningBegin){//8点之前创建
//				workTime+=(pm14-pm12);
//			}else{
//				//8点之后创建
//				workTime+=(pm14-pm12+nightDate);
//			}
//		} else if(house_min > morningEnd && house_min < afternoonBegin){//加班，在中午时间段有加班的。
//			workTime+=(pm14-pm12+nightDate);
//		}else if(house >= afternoonBegin && house_min <= afternoonEnd){//下午时间段创建流程
//			if(house_min == afternoonBegin){
//				workTime+=nightDate;
//			}else{
//				//14点之后创建
//				workTime+=(pm14-pm12+nightDate);
//			}
//		}else if(house_min < morningBegin){//以防哪个不要命的凌晨提交材料，先把时间算好，要不然创建流程，时间有出入
//			long am8 = 1000*60*morningBegin*60;
//			long h = house*60*60*1000;
//			long min = c.get(Calendar.MINUTE)*60*1000;
//			long mis = c.get(Calendar.SECOND)*1000;
//			long misses =c.get(Calendar.MILLISECOND);
//			long morning = h+min+mis+misses;
//			long between = am8-morning;
//			workTime+=(pm14-pm12+between);
//		}else if(house_min > afternoonEnd){//18点之后还有件进来，并加班处理的
//			long pm18 = 1000*60*afternoonEnd*60;
//			long h = house*60*60*1000;
//			long min = c.get(Calendar.MINUTE)*60*1000;
//			long mis = c.get(Calendar.SECOND)*1000;
//			long misses =c.get(Calendar.MILLISECOND);
//			long night = h+min+mis+misses;
//			long nightbetween = night-pm18;
//			workTime+=(pm14-pm12+nightDate-nightbetween);
//		}		
		cal.setTimeInMillis(workTime);

		return cal;
	}
	
	/**
	 * 创建活动实例
	 *
	 * @param proinfo
	 *            SmProInfo 流程实例信息
	 * @return 活动实例对象
	 * */
	public Wfi_ProInst CreatProInst(SmProInfo proinfo) {
		Wfi_ProInst proInst = null;
		StringBuilder _ProjectName = new StringBuilder();
		try {

			// 组合项目名称
			_ProjectName.append(proinfo.getProInst_Name());
			// _ProjectName.append("申请");
			// _ProjectName.append(proinfo.getProDef_Name());

			proInst = new Wfi_ProInst();
			proInst.setProinst_Id(Common.CreatUUID());
			proInst.setProdef_Id(proinfo.getProDef_ID());
			proInst.setAcceptor(proinfo.getAcceptor());
			proInst.setCreat_Date(new Date());
			proInst.setDistrict_Id(proinfo.getDistrict_ID());
			proInst.setProdef_Name(proinfo.getProDef_Name());
			proInst.setFromInline(proinfo.getFromInline());
			//判断时间
			if(proinfo.getProStartTime()!=null&&!proinfo.getProStartTime().equals("")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    proInst.setProinst_Start(formatter.parse(proinfo.getProStartTime()));
			}else{
				proInst.setProinst_Start(new Date());
			}
			proInst.setProinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			if(proinfo.getProjectName()!=null&&!proinfo.getProjectName().equals("")){
				proInst.setProject_Name(proinfo.getProjectName());
			}else{
				proInst.setProject_Name(_ProjectName.toString());
			}
			proInst.setUrgency(Integer.parseInt(proinfo.getFile_Urgency()));
			proInst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
			Wfd_Prodef prodef = commonDao.get(Wfd_Prodef.class, proinfo.getProDef_ID());
			if (prodef != null && prodef.getTransaction_Type() != null && prodef.getTransaction_Type() > proInst.getUrgency()) {
				proInst.setUrgency(prodef.getTransaction_Type());
			}
			proInst.setFile_Number(GetFileNumber(prodef.getProdef_Code()));
			proInst.setProinst_Time(prodef.getProdef_Time());
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			if(proinfo.getProWillFinishTime()!=null&&!proinfo.getProWillFinishTime().equals("")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    proInst.setProinst_Willfinish(formatter.parse(proinfo.getProWillFinishTime()));
			}
			if (prodef.getProdef_Time() != null) {
				//判断时间
				if(null==proInst.getProinst_Willfinish()) {
					proInst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal, prodef.getProdef_Time()));
				}
			}
            if(proinfo.getProLSH()!=null&&!proinfo.getProLSH().equals("")){
            	proInst.setProlsh(proinfo.getProLSH());
            }else{
            	//此处在调用smstaff类依据staffid获取当前用户行政区划代码时候 直接传递本次服务请求中已经通过全局获取到的staffid 否则后台调服务导致目标类无发获取浏览器缓存中的用户ID
            	String staffid=null!=proinfo.getStaffID()?proinfo.getStaffID():smStaff.getCurrentWorkStaffID();
            	String areaCode = smStaff.GetCurrentAreaCode(staffid);
            	proInst.setProlsh(CreatMaxID("0", "PROLSH"));
            	String lshConfig = ConfigHelper.getNameByValue("lshConfig");
            	if("2".equals(lshConfig)){
            		if(null!=areaCode&&!areaCode.isEmpty()){
            			String lsh = proInst.getProlsh();
            			lsh = areaCode.substring(areaCode.length()-2) + lsh.substring(2);
            			proInst.setProlsh(lsh);
            		}
            	}
            }
			if (proInst.getProject_Name() == null || proInst.getProject_Name().equals("")) {
				proInst.setProject_Name(proInst.getProlsh());
			}
			proInst.setRemarks(proinfo.getMessage());
			proInst.setProinst_Code(prodef.getProdef_Code());
			// commonDao.clear();
			// 暂时先设置固定的
			if (StringHelper.isEmpty(smStaff.getCurrentWorkStaffID())) {
				proInst.setStaff_Id(proinfo.getStaffID());
			} else {
				proInst.setStaff_Id(smStaff.getCurrentWorkStaffID());
			}
			proInst.setBatch(proinfo.getBatch());
			proInst.setYwh(proinfo.getYwh());
			proInst = SetWeight(proInst);

		} catch (Exception e) {
		}
		return proInst;
	}

	@SuppressWarnings("unused")
	public String GetFileNumber(String bh) {
		String distId = smStaff.getDistIdByStaffIDString(smStaff.getCurrentWorkStaffID());
		Integer year = Integer.parseInt(DateUtil.getYear());
		return commonDao.CreatFile_Number(distId, bh);

	}

	public String CreatMaxID(String bh, String Type) {
		String distId = smStaff.getDistIdByStaffIDString(smStaff.getCurrentWorkStaffID());
		String result = commonDao.CreatMaxID(distId, bh, Type);
		if (result == null || result.equals("")) {
			return CreatMaxID(bh, Type);
		} else {
			return result;

		}

	}

	/**
	 * 通过活动实例ID 获取流程实例
	 * 
	 * @param actinstid
	 *            String 活动实例ID
	 * */
	public Wfi_ProInst GetProInstByActInstId(String actinstid) {
		Wfi_ActInst actinst = commonDao.get(Wfi_ActInst.class, actinstid);
		if (actinst != null) {
			return commonDao.get(Wfi_ProInst.class, actinst.getProinst_Id());
		}

		return null;
	}

	/**
	 * 项目列表
	 * 
	 * @param param
	 *            SmQueryPara 查询参数
	 * @param StaffID
	 *            String 员工ID
	 * @param ListType
	 *            int 0待办，1在办，2办结
	 * 
	 * */

	public List<Wfi_ProInst> GetProInstList(SmQueryPara param, String StaffID, int ListType) {
		StringBuilder Sql = new StringBuilder();
		Sql.append(GetProInstListNoWhereSql(param, StaffID, ListType));
		return commonDao.getDataList(Wfi_ProInst.class, Sql.toString());
	}

	/**
	 * 项目列表NoWhereSql
	 * 
	 * @param param
	 *            SmQueryPara 查询参数
	 * @param StaffID
	 *            String 员工ID
	 * @param ListType
	 *            int 0待办，1在办，2办结
	 * 
	 * */
	public String GetProInstListNoWhereSql(SmQueryPara param, String StaffID, int ListType) {
		StringBuilder noWhereSql = new StringBuilder();
		String ProdefID = param.getProDef_ID();
		String ProIns_Status = param.getProInst_Status();
		String ProInst_StatusNot = param.getProInst_StatusNot();
		String ProjectName = param.getProject_Name();

		if (ProdefID != null && ProdefID != "") {
			noWhereSql.append(" and PRODEF_ID = '");
			noWhereSql.append(ProdefID);
			noWhereSql.append("'");
		}

		if (ProIns_Status != null && ProIns_Status != "") {
			noWhereSql.append(" and PROINST_STATUS IN (");
			noWhereSql.append(ProIns_Status);
			noWhereSql.append(")");
		}

		if (ProInst_StatusNot != null && ProInst_StatusNot != "") {
			noWhereSql.append(" and PROINST_STATUS NOT IN (");
			noWhereSql.append(ProInst_StatusNot);
			noWhereSql.append(")");
		}

		if (ProjectName != null && ProjectName != "") {
			noWhereSql.append(" and (PROJECT_NAME like '%");
			noWhereSql.append(ProjectName.replace(" ", "%"));
			noWhereSql.append("%' or PROINST_CODE like '%");
			noWhereSql.append(ProjectName);
			noWhereSql.append("')");
		}

		// 在办Instance_Status 1
		if (ListType == 1) {
			noWhereSql.append(" and PROINST_ID IN (SELECT PROINST_ID FROM " + Common.WORKFLOWDB + "WFI_NOWACTINST T1," + Common.WORKFLOWDB + "V_ACT_PRO_ROLE_STAFF T2 WHERE T1.ACTDEF_ID= T2.ACTDEF_ID AND T1.STAFF_ID= T2.STAFF_ID AND T2.STAFF_ID='");
			noWhereSql.append(StaffID);
			noWhereSql.append("')");
		}
		// 待办Instance_Status 2
		else if (ListType == 2) {
			noWhereSql.append(" and PROINST_ID IN (SELECT PROINST_ID FROM " + Common.WORKFLOWDB + "WFI_NOWACTINST T1," + Common.WORKFLOWDB + "V_ACT_PRO_ROLE_STAFF T2 WHERE T1.ACTDEF_ID= T2.ACTDEF_ID AND (T1.STAFF_ID='0' or T1.STAFF_ID is null) AND T2.STAFF_ID='");
			noWhereSql.append(StaffID);
			noWhereSql.append("')");
		} else if (ListType == 8) // 在办、待办
		{
			noWhereSql.append(" and PROINST_ID IN (SELECT PROINST_ID FROM " + Common.WORKFLOWDB + "WFI_NOWACTINST T1," + Common.WORKFLOWDB + "V_ACT_PRO_ROLE_STAFF T2 WHERE T1.ACTDEF_ID= T2.ACTDEF_ID AND T2.STAFF_ID='");
			noWhereSql.append(StaffID);
			noWhereSql.append("')");
		} else {
			noWhereSql.append(" and 1=2 ");
		}

		return noWhereSql.delete(0, 4).toString();
	}

	/**
	 * 
	 */
	public void ProInfoFromProInst(SmProInfo _SmProInfo, Wfi_ProInst _Wfi_ProInst) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		_SmProInfo.setProdef_code(_Wfi_ProInst.getProinst_Code());
		_SmProInfo.setAcceptor(_Wfi_ProInst.getAcceptor());
		_SmProInfo.setFile_Urgency(_Wfi_ProInst.getUrgency().toString());
		_SmProInfo.setFile_Number(_Wfi_ProInst.getFile_Number());
		_SmProInfo.setDistrict_ID(_Wfi_ProInst.getDistrict_Id());
		// _SmProInfo.setEndTime(_Wfi_ProInst.getProinst_End().toString());
		_SmProInfo.setLCBH(_Wfi_ProInst.getProinst_Code());
		_SmProInfo.setProDef_ID(_Wfi_ProInst.getProdef_Id());
		_SmProInfo.setProInst_Name(_Wfi_ProInst.getProject_Name());
		_SmProInfo.setProDef_Name(_Wfi_ProInst.getProdef_Name());
		if(_Wfi_ProInst.getProinst_Willfinish()!=null){
			_SmProInfo.setProWillFinishTime(formatter2.format(_Wfi_ProInst.getProinst_Willfinish()));
		}
		if (_Wfi_ProInst.getProinst_Start() != null) {
			_SmProInfo.setStartTime(formatter.format(_Wfi_ProInst.getProinst_Start()).toString());
		}
		_SmProInfo.setAreaCode(_Wfi_ProInst.getAreaCode());
		_SmProInfo.setProInst_ID(_Wfi_ProInst.getProinst_Id());
		_SmProInfo.setMessage(_Wfi_ProInst.getRemarks());
		_SmProInfo.setStaffID(_Wfi_ProInst.getStaff_Id());
		_SmProInfo.setProInst_Status(_Wfi_ProInst.getProinst_Status());
		_SmProInfo.setProLSH(_Wfi_ProInst.getProlsh());
		_SmProInfo.setYwh(_Wfi_ProInst.getYwh());
		if (_Wfi_ProInst.getProinst_Time() != null) {
			_SmProInfo.setProInst_Time(_Wfi_ProInst.getProinst_Time().toString());
		}
		_SmProInfo.setSQStartTime(formatter2.format(_Wfi_ProInst.getProinst_Start()).toString());
		_SmProInfo.setProInst_Status(_Wfi_ProInst.getProinst_Status());
		_SmProInfo.setBatch(_Wfi_ProInst.getBatch());
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		if( _Wfi_ProInst.getProinst_Time()!=null){
			_SmProInfo.setcProFinish(smHoliday.addDateByWorkDay(cal, _Wfi_ProInst.getProinst_Time()));
		}
		
	}

	/**
	 * 通过视图去填充ProInfo
	 * 
	 * @param _SmProInfo
	 * @param _proInstInfo
	 */
	public void ProInfoFromView(SmProInfo _SmProInfo, vProInstInfo _proInstInfo) {
		_SmProInfo.setAcceptor(_proInstInfo.getAcceptor());
		_SmProInfo.setAcceptType_ID(_proInstInfo.getAccepttype_Id());
		_SmProInfo.setActDef_Name(_proInstInfo.getActdef_Name());
		_SmProInfo.setActInst_ID(_proInstInfo.getActinst_Id());
		_SmProInfo.setActWillFinishTime(_proInstInfo.getActwillfinishtime());
		_SmProInfo.setDistModify(_proInstInfo.getDistmodify());
		_SmProInfo.setDistrict_ID(_proInstInfo.getDistrict_Id());
		_SmProInfo.setEndTime(_proInstInfo.getEndtime());
		_SmProInfo.setFile_Number(_proInstInfo.getFile_Number());
		if (_proInstInfo.getFile_Urgency() != null) {
			_SmProInfo.setFile_Urgency(_proInstInfo.getFile_Urgency().toString());
		}
		_SmProInfo.setFinishDate(_proInstInfo.getFinishdate());

		// _SmProInfo.setHaveDone(_proInstInfo.getHavedone());
		_SmProInfo.setLCBH(_proInstInfo.getLcbh());
		_SmProInfo.setMessage(_proInstInfo.getMessage());
		_SmProInfo.setProDef_ID(_proInstInfo.getProdef_Id());
		_SmProInfo.setProDef_Name(_proInstInfo.getProdef_Name());
		_SmProInfo.setProDef_Order(_proInstInfo.getProdef_Order());
		_SmProInfo.setProDef_Type(_proInstInfo.getProdef_Type());
		_SmProInfo.setProInst_ID(_proInstInfo.getProinst_Id());
		_SmProInfo.setProInst_Name(_proInstInfo.getProinst_Name());
		_SmProInfo.setProInst_Time(_proInstInfo.getProinst_Time());
		_SmProInfo.setProInst_UserNumber(_proInstInfo.getProinst_Usernumber());
		_SmProInfo.setProStartTime(_proInstInfo.getProstarttime());
		_SmProInfo.setProType_ID(_proInstInfo.getProtype_Id());
		_SmProInfo.setRemark(_proInstInfo.getRemark());
		_SmProInfo.setStaffDist_ID(_proInstInfo.getStaffdist_Id());
		_SmProInfo.setStaffID(_proInstInfo.getStaffid());
		_SmProInfo.setStartTime(_proInstInfo.getStarttime());
		if (_proInstInfo.getStatus() != null) {
			_SmProInfo.setProInst_Status(_proInstInfo.getStatus());
		}
	}

	/**
	 * 通过流程编号获取项目信息
	 * */
	public Wfi_ProInst GetProInstByFileNumber(String FileNumber) {
		Wfi_ProInst proInst = null;
		if (!FileNumber.equals("")) {
			List<Wfi_ProInst> proinsts = commonDao.getDataList(Wfi_ProInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ProInst where FILE_NUMBER='" + FileNumber + "' or prolsh='" + FileNumber + "'");
			if (proinsts != null && proinsts.size() > 0) {
				proInst = proinsts.get(0);
			}
		}
		return proInst;
	}

	/*
	 * 删除流程
	 */
	public void DelProInst(String proinstid) {
		if (proinstid != null && !"".equals(proinstid)) {
			commonDao.delete(Wfi_ProInst.class, proinstid);
		}
	}

	/**
	 * 删除wfi_proinstdate表数据
	 * @param file_number
	 */
	public void delProinstDate(String file_number) {
		commonDao.deleteQuery("delete from bdc_workflow.wfi_proinstdate t where t.file_number = '" + file_number +"'");
		commonDao.flush();
	}
	/*
	 * 获取流程 数量
	 */
	public List<SmObjInfo> GetProjectTypeAndCountByStaffID(String staff_id) {
		long start = System.currentTimeMillis();
		List<SmObjInfo> infos = new ArrayList<SmObjInfo>();
		if (staff_id != null && !staff_id.equals("")) {
			String sBuilder = GetCountSqL(WFConst.Instance_Status.Instance_NotAccept.value, staff_id);
			long result = commonDao.getCountByFullSql(sBuilder);
			long aa = System.currentTimeMillis();
			SmObjInfo notaccept = new SmObjInfo();
			notaccept.setName("待办件");
			notaccept.setID(result + "");
			notaccept.setDesc(" fa fa-openid fa-2x blue");
			infos.add(notaccept);
			sBuilder = GetCountSqL(WFConst.Instance_Status.Instance_doing.value, staff_id);
			result = commonDao.getCountByFullSql(sBuilder);
			long bb = System.currentTimeMillis();
			SmObjInfo doing = new SmObjInfo();
			doing.setName("在办件");
			doing.setID(result + "");
			doing.setDesc("fa fa-medium fa-2x purple");
			infos.add(doing);

			sBuilder = GetCountSqL(WFConst.Instance_Status.Instance_Passing.value, staff_id);
			result = commonDao.getCountByFullSql(sBuilder);
			SmObjInfo passing = new SmObjInfo();
			passing.setName("已办件");
			passing.setID(result + "");
			passing.setDesc("fa fa-history fa-2x red");
			infos.add(passing);
			System.out.println(aa-start);
			System.out.println(bb-aa);
			System.out.println(System.currentTimeMillis()-bb);
		}
		return infos;
	}

	private String GetCountSqL(int actInst_Status, String staff_Id) {
		StringBuilder str = new StringBuilder();
		StringBuilder tmpTable = new StringBuilder();
		// _________________________________________________________________________
        String sql = "";
		str.append(" ACTSTAFF='");
		str.append(staff_Id);
		if (actInst_Status == 8) {// 待办、在办
			/*str.append("' and ACTINST_STATUS in (1,2)");
			str.append(" and ( staff_id  is null or staff_id='");
			str.append(staff_Id);
			str.append("' or codeal=1 )");
			tmpTable.append(Common.WORKFLOWDB + "v_projectlist");*/
			sql = 
			" FROM\n" +
			""+Common.WORKFLOWDB+"WFI_ACTINSTSTAFF A\n" + 
			"inner join (select actinst.actinst_id  FROM "+Common.WORKFLOWDB+"wfi_actinst actinst\n" + 
			"where (actinst.actinst_status=2 AND (actinst.staff_id ='"+staff_Id+"' or codeal=1)) OR (actinst.staff_id is null AND actinst.actinst_status=1 ) ) B\n" + 
			"ON A.ACTINST_ID = B.actinst_id WHERE A.STAFF_ID ='"+staff_Id+"'";
		} else if (actInst_Status == 2) {
			/*str.append("' and ACTINST_STATUS=" + actInst_Status);
			str.append(" and ( staff_id  is null or staff_id='");
			str.append(staff_Id);
			str.append("' or codeal=1 )");
			tmpTable.append(Common.WORKFLOWDB + "v_projectlist");*/
			sql = 
					" FROM\n" +
					" "+Common.WORKFLOWDB+"WFI_ACTINSTSTAFF A\n" + 
					"inner join (select actinst.actinst_id  FROM "+Common.WORKFLOWDB+"wfi_actinst actinst\n" + 
					"where  actinst.actinst_status=2 AND (actinst.staff_id ='"+staff_Id+"' or codeal=1)) B\n" + 
					"ON A.ACTINST_ID = B.actinst_id WHERE A.STAFF_ID ='"+staff_Id+"'";

		}else if (actInst_Status == 1){
			sql = 
					" FROM\n" +
					""+Common.WORKFLOWDB+"WFI_ACTINSTSTAFF A\n" + 
					"inner join (select actinst.actinst_id  FROM "+Common.WORKFLOWDB+"wfi_actinst actinst\n" + 
					"where actinst.staff_id is null AND actinst.actinst_status=1 ) B\n" + 
					"ON A.ACTINST_ID = B.actinst_id WHERE A.STAFF_ID ='"+staff_Id+"'";
		} else {// 已办
		/*	str.append("' and (ACTINST_STATUS=");
			str.append(actInst_Status);
			str.append(" or ( proinst_status=0 and actinst_status=0 ) )and staff_id='");
			str.append(staff_Id);
			str.append("'");*/
			sql = " FROM (SELECT  DISTINCT PROINST_ID,ACTDEF_ID FROM "+Common.WORKFLOWDB+"WFI_ACTINST A WHERE ( A.status=3 OR  actinst_status=0 ) AND A.staff_id='"+staff_Id+"')";
			//tmpTable = ConactDistinctCondition("actinst_start", "asc");
		}
		return sql;

	}

	@SuppressWarnings("rawtypes")
	public List<SmObjInfo> ProjectStatic(String staffid) {
		List<SmObjInfo> infos = new ArrayList<SmObjInfo>();
		String sql = "select proinst_code from "+Common.WORKFLOWDB +"wfi_proinst p where proinst_id in (select  proinst_id from "+Common.WORKFLOWDB +"wfi_actinst a where staff_id = '"
		+staffid+"' and ( actinst_status = 3 or actinst_status = 0) and p.proinst_id = a.proinst_id) order by proinst_code";
		List<Map> list = commonDao.getDataListByFullSql(sql);
		if (list != null && list.size() > 0) {
			String filterString = "";
			SmObjInfo info = new SmObjInfo();
			int count = 0;
			for (Map map2 : list) {

				Object prodef_codeString = map2.get("PROINST_CODE");
				if (prodef_codeString != null) {
					String djlx = prodef_codeString.toString().substring(0, 3);
					if (filterString.equals("")) {
						filterString = djlx;
					}
					if (!djlx.equals(filterString) && !filterString.equals("")) {
						info.setDesc(count + "");
						infos.add(info);
						count = 1;
						filterString = djlx;
						info = new SmObjInfo();
					} else {
						count++;
					}
					if(StringHelper.isEmpty(djlx)){
						switch (Integer.parseInt(djlx)) {
						case 100:
							info.setName("首次登记");
							break;
						case 200:
							info.setName("转移登记");
							break;
						case 300:
							info.setName("变更登记");
							break;
						case 400:
							info.setName("注销登记");
							break;
						case 500:
							info.setName("更正登记");
							break;
						case 600:
							info.setName("异议登记");
							break;
						case 700:
							info.setName("预告登记");
							break;
						case 800:
							info.setName("查封登记");
							break;
						case 900:
							info.setName("其他登记");
							break;
						default:
							break;
						}
					}
				}

			}
			info.setDesc(count + "");
			infos.add(info);
		}
		return infos;
	}

	// 组建临时表，去除多次驳回产生的重复件
	/**
	 * 
	 * @param orderfield
	 * @param ordertype
	 * @return
	 */
	public StringBuilder ConactDistinctCondition(String orderfield, String ordertype) {
		/*
		 * StringBuilder tmpTable=new StringBuilder(); tmpTable.append(
		 * " (select * from (select  row_number() over (partition by b.file_number order by b.actinst_start ) rn, b.* from ("
		 * ); tmpTable.append(
		 * " select actdef_id,proinst_id,max(actinst_start) as actinst_start  from "
		 * +Common.WORKFLOWDB+"v_projectlist");
		 * tmpTable.append(" group by actdef_id,proinst_id ) a left join "
		 * +Common.WORKFLOWDB+
		 * "v_projectlist b on a.actdef_id=b.actdef_id and a.actinst_start=b.actinst_start )  where rn=1 ) "
		 * ); return tmpTable;
		 */
		StringBuilder tmpTable = new StringBuilder();
		tmpTable.append("(select * from  ( select  row_number() over (partition by b.proinst_id, b.actdef_id order by  b." + orderfield + " " + ordertype + " ) rn,  b.* from  ");
		tmpTable.append(Common.WORKFLOWDB + "v_projectlist b )");
		tmpTable.append(" where rn=1) v_projectlist");
		return tmpTable;
	}

	/**
	 * 设置流程的权重
	 */
	public Wfi_ProInst SetWeight(Wfi_ProInst inst) {
		if (inst != null) {
			int weight = 0;
			if (inst.getUrgency() != null) {
				switch (inst.getUrgency()) {
				case 1:// 正常
					weight = WFConst.Proinst_Weight.Normal.value;
					break;
				case 2:// 紧急
					weight = WFConst.Proinst_Weight.Urgent.value;
					break;
				case 3:// 特急
					weight = WFConst.Proinst_Weight.ExtraUrgent.value;
					break;
				}
			}
			if (inst.getInstance_Type() != null && inst.getInstance_Type() == WFConst.Instance_Type.Instance_OverRule.value) {
				// 修正数据，驳回实例转出后，变成正常实例
				List<Wfi_ActInst> actinsts = commonDao.getDataList(Wfi_ActInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ActInst where proinst_id='" + inst.getProinst_Id() + "' and actinst_status in (1,2)");
				if (actinsts != null && actinsts.size() > 0) {
					Wfi_ActInst actinst = actinsts.get(0);
					if (actinst.getOperation_Type().equals(WFConst.Operate_Type.PRODUCT_anglesPicture.value + "")) {
						inst.setInstance_Type(WFConst.Instance_Type.Instance_OverRule.value);

					} else {
						inst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
					}
				}
				weight = WFConst.Proinst_Weight.PassBack.value + weight;
			}
			if(inst.getOperation_Type_Nact() != null &&inst.getOperation_Type_Nact().equals( WFConst.Operate_Type.PRODUCT_anglesPicture.value+"")){
				weight = ComputeWeight(weight, WFConst.Proinst_Weight.PassBack.value);
			}
			if (inst.getInstance_Type() != null && inst.getInstance_Type() == WFConst.Instance_Type.Instance_Normal.value) {
				weight = ComputeWeight(weight, WFConst.Proinst_Weight.Normal.value);
			}
			if (inst.getProinst_Willfinish() != null) {
				if( new Date().after(inst.getProinst_Willfinish())){// 超期
					int tmp = ComputeWeight(0/*为了重新计算权重，先去掉这玩意inst.getProinst_Weight()*/, WFConst.Proinst_Weight.OutTime.value);
					weight = ComputeWeight(tmp, weight);
				}else if(smHoliday.FactDate(new Date(),2).after(inst.getProinst_Willfinish())){
					int tmp = ComputeWeight(0/*inst.getProinst_Weight()*/, WFConst.Proinst_Weight.SoonOutTime.value);
					weight = ComputeWeight(tmp, weight);
				}
			}
			if(inst.getOperation_Type()!=null&&inst.getOperation_Type().equals(WFConst.Operate_Type.HaveSupervised.value + "")){
				weight=ComputeWeight(weight, WFConst.Proinst_Weight.Supervised.value);
			}
			inst.setProinst_Weight(weight);
		}
		return inst;
	}

	private Integer ComputeWeight(int oldValue, int newValue) {
		if (oldValue > newValue) {
			return oldValue;
		} else {
			return newValue;
		}
	}

	// 通过批次号获取项目
	public Wfi_ProInst GetProinstByBatchDM(String batch) {
		List<Wfi_ProInst> insts = commonDao.getDataList(Wfi_ProInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ProInst where batch='" + batch + "'");
		if (insts != null && insts.size() > 0) {
			return insts.get(0);
		} else {
			return null;
		}

	}
	//通过lshs获取项目
	public List<Wfi_ProInst> GetProInstByLshs(String lshs) {
		return commonDao.getDataList(Wfi_ProInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ProInst where prolsh in (" + lshs + ")");
	}
}
