package com.supermap.wisdombusiness.workflow.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.gxdjk.BDC2FC_DIRECTORY;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;

import javax.servlet.http.HttpServletRequest;

@Component("SqlFactory")
public class SqlFactory {
	
	@Autowired
	private   CommonDao commonDao;
	@Autowired
	private   UserService userService;
	
	@Autowired
	private SmStaff smStaff;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private SmHoliday smHoliday;
	
	/**
	 * 获取项目列表查询sql
	 * 
	 * */
	public  Map<String,String> getHandingProjectListSql(String staff_Id, int actInst_Status,
														String sarchString, int CurrentPageIndex, int pageSize,
														String pronames, String sqr, String startlsh, String endlsh,
														String actinst_name, String order,
														String starttime, String endtime, String handlepsersons, String roleid, String ywh, String prolshs, String outTimeStatus, String sfjsbl, HttpServletRequest request){
		String taxstatus = request.getParameter("taxstatus");//完税状态
		String netstatus = request.getParameter("netstatus");//网签状态
		String tax_netstatus = request.getParameter("tax_netstatus");//已网签或已完税
		Map<String,String > sqlmap = new HashMap<String, String>(); 
		StringBuilder handingsql =new StringBuilder();
		String regex = "^[s,S][0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sarchString);
		boolean regexResult=matcher.find();
		System.out.println("是否符合正则表达式判断："+regexResult);
		if (actInst_Status ==WFConst.Instance_Status.Instance_doAndDoing.value) {// 待办、在办
			if("1".equals(sfjsbl)){
				String whereSql = "and po.sfjsbl='2' ";
				handingsql.append(
						"SELECT\n" +
						"PO.PROJECT_NAME,\n" + 
						"AI.ACTDEF_TYPE,\n" + 
						"PO.FILE_NUMBER,\n" + 
						"PO.INSTANCE_TYPE,\n" +
						"ACTSTAFF.STAFF_ID AS ACTSTAFF,\n" + 
						"ACTSTAFF.Staff_Name as actstaffname,\n" + 
						"PO.PROINST_ID,\n" + 
						"PO.PRODEF_ID,\n" + 
						"PO.PRODEF_NAME,\n" + 
						"PO.PROINST_STATUS,\n" + 
						"PO.PROINST_END,\n" + 
						"PO.PROINST_START,\n" + 
						"PO.YWH,\n" +
						"PO.PROINST_WILLFINISH,\n" + 
						"PO.PROINST_CODE,\n" + 
						"AI.ACTINST_ID,\n" + 
						"AI.ISAPPLYHANGUP,\n" + 
						"AI.NOTAPPPASSRES,\n" + 
						"AI.OPERATION_TYPE,\n" + 
						"AI.STAFF_ID,\n" + 
						"AI.staff_name,\n" + 
						"AI.ACTDEF_ID,\n" + 
						"AI.ACTINST_STATUS,\n" + 
						"AI.ACTINST_START,\n" + 
						"AI.ACTINST_END,\n" + 
						"AI.ACTINST_WILLFINISH,\n" + 
						"AI.ACTINST_NAME,\n" + 
						"AI.ACTINST_MSG,\n" + 
						"AI.ISTRANSFER,\n" + 
						"AI.UNLOCKDATE,\n" +
						"AI.LOCKTYPE,\n" +
						"AI.TRANSFER_TIME,\n" +
						"AI.MSG,\n"  +
						"PO.PROLSH,\n" + 
						"AI.CODEAL,\n" + 
						"PO.Urgency,\n" + 
						"PO.Proinst_Time,\n" + 
						"PO.FROMINLINE,\n" + 
						"PO.PROINST_WEIGHT,\n" +  
						"PO.SFJSBL,\n" + 
						"PO.WLSH\n" +
						"      FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF ACTSTAFF\n" +
						"      INNER JOIN BDC_WORKFLOW.WFI_NOWACTINST AI ON ACTSTAFF.ACTINST_ID=AI.ACTINST_ID\n" + 
						"      INNER JOIN BDC_WORKFLOW.WFI_PROINST PO ON AI.PROINST_ID=PO.PROINST_ID\n" +
						"      where AI.Passedroute_Count is null  and AI.ACTINST_STATUS = '1' and "
						+ "ACTSTAFF.STAFF_ID='"+staff_Id+"' and ( AI.STAFF_ID is null or AI.STAFF_ID='"+staff_Id+"' or  AI.CODEAL =1) " + whereSql
								+ " ");
			}else{
				handingsql.append(
						"SELECT\n" +
						"PO.PROJECT_NAME,\n" + 
						"AI.ACTDEF_TYPE,\n" + 
						"PO.FILE_NUMBER,\n" + 
						"PO.INSTANCE_TYPE,\n" +
						"ACTSTAFF.STAFF_ID AS ACTSTAFF,\n" + 
						"ACTSTAFF.Staff_Name as actstaffname,\n" + 
						"PO.PROINST_ID,\n" + 
						"PO.PRODEF_ID,\n" + 
						"PO.PRODEF_NAME,\n" + 
						"PO.PROINST_STATUS,\n" + 
						"PO.PROINST_END,\n" + 
						"PO.PROINST_START,\n" + 
						"PO.YWH,\n" +
						"PO.PROINST_WILLFINISH,\n" + 
						"PO.PROINST_CODE,\n" + 
						"AI.ACTINST_ID,\n" + 
						"AI.ISAPPLYHANGUP,\n" + 
						"AI.NOTAPPPASSRES,\n" + 
						"AI.OPERATION_TYPE,\n" + 
						"AI.STAFF_ID,\n" + 
						"AI.staff_name,\n" + 
						"AI.ACTDEF_ID,\n" + 
						"AI.ACTINST_STATUS,\n" + 
						"AI.ACTINST_START,\n" + 
						"AI.ACTINST_END,\n" + 
						"AI.ACTINST_WILLFINISH,\n" + 
						"AI.ACTINST_NAME,\n" + 
						"AI.ACTINST_MSG,\n" + 
						"AI.ISTRANSFER,\n" + 
						"AI.UNLOCKDATE,\n" +
						"AI.LOCKTYPE,\n" +
						"AI.TRANSFER_TIME,\n" +
						"AI.MSG,\n"  +
						"PO.PROLSH,\n" + 
						"AI.CODEAL,\n" + 
						"PO.Urgency,\n" + 
						"PO.Proinst_Time,\n" + 
						"PO.FROMINLINE,\n" + 
						"PO.PROINST_WEIGHT,\n" +  
						"PO.SFJSBL,\n" +
						"PO.WLSH,\n" +
						"PRO_DATE.TAXCONFIRMTIME,\n" +
						"PRO_DATE.NETSIGNCONFIRMTIME,\n" +
						"PRO_DATE.FILE_NUMBER AS DATEFILENUMBER\n" +
						"      FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF ACTSTAFF\n" + 
						"      INNER JOIN BDC_WORKFLOW.WFI_NOWACTINST AI ON ACTSTAFF.ACTINST_ID=AI.ACTINST_ID\n" + 
						"      INNER JOIN BDC_WORKFLOW.WFI_PROINST PO ON AI.PROINST_ID=PO.PROINST_ID\n" +
						"       LEFT JOIN BDC_WORKFLOW.WFI_PROINSTDATE PRO_DATE ON PRO_DATE.FILE_NUMBER=PO.FILE_NUMBER \n" +
						"      where AI.Passedroute_Count is null  and AI.ACTINST_STATUS in (1,2,14,15) and "
						+ "ACTSTAFF.STAFF_ID='"+staff_Id+"' and ( AI.STAFF_ID is null or AI.STAFF_ID='"+staff_Id+"' or  AI.CODEAL =1) "
								+ " ");
			}


			
			
		} else if (actInst_Status == WFConst.Instance_Status.Instance_NotAccept.value 
				|| actInst_Status == WFConst.Instance_Status.Instance_doing.value) {
			handingsql.append("SELECT\n" +
					"PO.PROJECT_NAME,\n" + 
					"AI.ACTDEF_TYPE,\n" + 
					"PO.FILE_NUMBER,\n" + 
					"ACTSTAFF.STAFF_ID AS ACTSTAFF,\n" + 
					"ACTSTAFF.Staff_Name as actstaffname,\n" + 
					"PO.PROINST_ID,\n" + 
					"PO.INSTANCE_TYPE,\n" +
					"PO.PRODEF_ID,\n" + 
					"PO.PRODEF_NAME,\n" + 
					"PO.PROINST_STATUS,\n" + 
					"PO.PROINST_END,\n" + 
					"PO.PROINST_START,\n" + 
					"PO.YWH,\n" +
					"PO.PROINST_WILLFINISH,\n" + 
					"PO.PROINST_CODE,\n" +
					"AI.ISAPPLYHANGUP,\n" + 
					"AI.STATUSEXT,\n"+
					"AI.ACTINST_ID,\n" + 
					"AI.OPERATION_TYPE,\n" + 
					"AI.STAFF_ID,\n" + 
					"AI.staff_name,\n" + 
					"AI.ACTDEF_ID,\n" + 
					"AI.ACTINST_STATUS,\n" + 
					"AI.ACTINST_START,\n" + 
					"AI.ACTINST_END,\n" + 
					"AI.ACTINST_WILLFINISH,\n" + 
					"AI.ACTINST_NAME,\n" + 
					"AI.ACTINST_MSG,\n" + 
					"AI.ISTRANSFER,\n" + 
					"AI.TRANSFER_TIME,\n" + 
					"AI.UNLOCKDATE,\n" +
					"AI.LOCKTYPE,\n" +
					"AI.MSG,\n" +
					"(ceil ((sysdate-AI.actinst_willfinish)*24)) as outtime,\n" + 
					"PO.PROLSH,\n" + 
					"PO.SFJSBL,\n" + 
					"AI.CODEAL,\n" + 
					"PO.Urgency,\n" + 
					"PO.Proinst_Time,\n" +
					"PO.FROMINLINE,\n" + 
					"PO.PROINST_WEIGHT,\n" +
					"PRO_DATE.TAXCONFIRMTIME,\n" +
					"PRO_DATE.NETSIGNCONFIRMTIME,\n" +
					"PRO_DATE.FILE_NUMBER AS DATEFILENUMBER,\n" +
					"(ceil ((sysdate-PO.PROINST_WILLFINISH)*24*60*60)) as proouttime\n" + 
					"      FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF ACTSTAFF\n" + 
					"      INNER JOIN BDC_WORKFLOW.WFI_NOWACTINST AI ON ACTSTAFF.ACTINST_ID=AI.ACTINST_ID\n" + 
					"      INNER JOIN BDC_WORKFLOW.WFI_PROINST PO ON AI.PROINST_ID=PO.PROINST_ID\n" +
					"       LEFT JOIN BDC_WORKFLOW.WFI_PROINSTDATE PRO_DATE ON PRO_DATE.FILE_NUMBER=PO.FILE_NUMBER \n" +
					"      where AI.Passedroute_Count is null  and AI.ACTINST_STATUS in(14,15,"+actInst_Status+") and "
					+ "ACTSTAFF.STAFF_ID='"+staff_Id+"' and ( AI.STAFF_ID is null or AI.STAFF_ID='"+staff_Id+"' or  AI.CODEAL =1)  "
							+ " ");
		} else if(actInst_Status==9){
			//领导办件查当前人员所在部门的所有人员的在办和待办件
			if(handlepsersons!=null&&!handlepsersons.trim().equals("")){
				staff_Id=handlepsersons;
			}else{
				String deptid = smStaff.getCurrentWorkStaff().getDepartment().getId();
				if(deptid!=null&&!deptid.equals("")){
					String staffids ="";
					List<User>  users = userService.findUserByDepartmentId(deptid);
					if(users!=null&&users.size()>0){
						for(int i = 0,j=users.size();i<j;i++){
							if(i==(j-1)){
								staffids+="'"+users.get(i).getId()+"'";
							}else{
								staffids+="'"+users.get(i).getId()+"',";
							}
						}
						staff_Id=staffids;
					}
				}
				if(staff_Id.indexOf(",")<0&&staff_Id.indexOf("'")<0){
					staff_Id="'"+staff_Id+"'";
				}
			}
			handingsql.append(
					"SELECT\n" +
					"PO.PROJECT_NAME,\n" + 
					"AI.ACTDEF_TYPE,\n" + 
					"PO.FILE_NUMBER,\n" + 
					"PO.PROINST_ID,\n" + 
					"PO.PRODEF_ID,\n" + 
					"PO.PRODEF_NAME,\n" + 
					"PO.PROINST_STATUS,\n" + 
					"PO.PROINST_END,\n" + 
					"PO.PROINST_START,\n" + 
					"PO.PROINST_WILLFINISH,\n" + 
					"PO.PROINST_CODE,\n" + 
					"AI.ACTINST_ID,\n" + 
					"AI.ISAPPLYHANGUP,\n" + 
					"AI.STATUSEXT,\n"+
					"AI.OPERATION_TYPE,\n" + 
					"AI.STAFF_ID,\n" + 
					"AI.staff_name,\n" + 
					"AI.ACTDEF_ID,\n" + 
					"AI.ACTINST_STATUS,\n" + 
					"AI.ACTINST_START,\n" + 
					"AI.ACTINST_END,\n" + 
					"AI.ACTINST_WILLFINISH,\n" + 
					"AI.ACTINST_NAME,\n" + 
					"AI.ACTINST_MSG,\n" + 
					"AI.ISTRANSFER,\n" + 
					"AI.TRANSFER_TIME,\n" +
					"AI.MSG,\n" +
					"(ceil ((sysdate-AI.actinst_willfinish)*24)) as outtime,\n" + 
					"PO.PROLSH,\n" + 
					"PO.SFJSBL,\n" + 
					"AI.CODEAL,\n" + 
					"PO.Urgency,\n" + 
					"PO.Proinst_Time,\n" + 
					"PO.PROINST_WEIGHT,\n" +
					"PRO_DATE.TAXCONFIRMTIME,\n" +
					"PRO_DATE.NETSIGNCONFIRMTIME,\n" +
					"PRO_DATE.FILE_NUMBER AS DATEFILENUMBER,\n" +
					"(ceil ((sysdate-PO.PROINST_WILLFINISH)*24*60*60)) as proouttime\n" + 
					"      FROM BDC_WORKFLOW.WFI_ACTINST AI \n" + 
					"      LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON AI.PROINST_ID=PO.PROINST_ID\n" +
					"       LEFT JOIN BDC_WORKFLOW.WFI_PROINSTDATE PRO_DATE ON PRO_DATE.FILE_NUMBER=PO.FILE_NUMBER \n" +
					"      where AI.Passedroute_Count is null and PO.PROINST_ID IS NOT NULL AND ( AI.ACTINST_STATUS =2 and AI.STAFF_ID in ("+staff_Id+")\n"+
					" or AI.ACTINST_STATUS =1 and exists(select actinst_id from bdc_workflow.wfi_actinststaff actstaff where actstaff.actinst_id = ai.actinst_id and actstaff.staff_id in ("+staff_Id+"))) ");
		}else{// 已办
			handingsql.append(
					"SELECT\n" +
							"PO.PROJECT_NAME,\n" + 
							"AI.ACTDEF_TYPE,\n" + 
							"PO.FILE_NUMBER,\n" + 
							"ACTSTAFF.STAFF_ID AS ACTSTAFF,\n" + 
							"ACTSTAFF.Staff_Name as actstaffname,\n" + 
							"PO.PROINST_ID,\n" + 
							"PO.PRODEF_ID,\n" + 
							"PO.PRODEF_NAME,\n" + 
							"PO.PROINST_STATUS,\n" + 
							"PO.PROINST_END,\n" + 
							"AI.ISAPPLYHANGUP,\n" +
							"AI.STATUSEXT,\n"+
							"PO.PROINST_START,\n" + 
							"PO.PROINST_WILLFINISH,\n" + 
							"PO.PROINST_CODE,\n" + 
							"AI.ACTINST_ID,\n" + 
							"AI.OPERATION_TYPE,\n" + 
							"AI.STAFF_ID,\n" + 
							"AI.staff_name,\n" + 
							"AI.ACTDEF_ID,\n" + 
							"AI.ACTINST_STATUS,\n" + 
							"AI.ACTINST_START,\n" + 
							"AI.ACTINST_END,\n" + 
							"AI.ACTINST_WILLFINISH,\n" + 
							"AI.ACTINST_NAME,\n" + 
							"AI.ACTINST_MSG,\n" + 
							"AI.ISTRANSFER,\n" + 
							"AI.TRANSFER_TIME,\n" + 
							"(ceil ((sysdate-ai.actinst_willfinish)*24)) as outtime,\n" + 
							"PO.PROLSH,\n" + 
							"PO.SFJSBL,\n" + 
							"AI.CODEAL,\n" + 
							"PO.Urgency,\n" + 
							"PO.Proinst_Time,\n" + 
							"PO.PROINST_WEIGHT,\n" +
							"PRO_DATE.TAXCONFIRMTIME,\n" +
							"PRO_DATE.NETSIGNCONFIRMTIME,\n" +
							"PRO_DATE.FILE_NUMBER AS DATEFILENUMBER,\n" +
							"(ceil ((sysdate-PO.PROINST_WILLFINISH)*24*60*60)) as proouttime\n" + 
							"      FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF ACTSTAFF\n" + 
							"      INNER JOIN BDC_WORKFLOW.WFI_ACTINST AI ON ACTSTAFF.ACTINST_ID=AI.ACTINST_ID\n" + 
							"      INNER JOIN BDC_WORKFLOW.WFI_PROINST PO ON AI.PROINST_ID=PO.PROINST_ID\n" +
							"       LEFT JOIN BDC_WORKFLOW.WFI_PROINSTDATE PRO_DATE ON PRO_DATE.FILE_NUMBER=PO.FILE_NUMBER \n" +
							"      where AI.Passedroute_Count is null  and "
							+ " ACTSTAFF.STAFF_ID='"+staff_Id+"' and (AI.ACTINST_STATUS ="+actInst_Status+"  or "
									+ " ( PO.proinst_status=0 and AI.actinst_status=0 ) )  "
					);
		}
		if (pronames != null && pronames.split(",").length == 1&&!pronames.equals("")) {
			if (pronames.endsWith("'")) {
				pronames = pronames.substring(1, pronames.length() - 1);
			}
			pronames = pronames.replace("-", ",");
			List<Wfi_ProInst> list = commonDao.findList(Wfi_ProInst.class, " prodef_name = '"+pronames+"'");
			if(null!=list&&list.size()>0){
				handingsql.append( " and prodef_name = '"+pronames+"' ");
			}else{
				handingsql.append( " and prodef_name like '"+pronames+"%' ");
			}

		} else if (pronames != null && !pronames.equals("")) {
			handingsql.append(" and prodef_name in ("+pronames+" )");
		}

		if(!StringHelper.isEmpty(sarchString)&&sarchString.contains(",")){
			sarchString = sarchString.replaceAll(",|，", "','");
			sarchString = "'" +sarchString + "'";
//			if("450400".equals(ConfigHelper.getNameByValue("XZQHDM"))){
//				handingsql.append(" and (PROJECT_NAME in ("+sarchString+") "
//						+ " or PROLSH in ("+sarchString+")"
//						+ " or  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM in ("+sarchString+") AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER) "
//						+ " or  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.ZJH in ("+sarchString+") AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER) "
//						+ ")");
//			}else{
				handingsql.append(" and (PROJECT_NAME in ("+sarchString+") "
						+ " or PROLSH in ("+sarchString+"))");
			
//			}
		}else if(!"".equals(sarchString)){
			//梧州市需求
//			if("450400".equals(ConfigHelper.getNameByValue("XZQHDM"))){
//				handingsql.append(" and (PROJECT_NAME like '%"+sarchString+"%' "
//						+ " or FILE_NUMBER like '%"+sarchString+"%' or"
//						+ " BATCH like '%"+sarchString+"%' or"
//						+ "  PROLSH like '%"+sarchString+"%' "
//						+ " or  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM like '%"+sarchString+"%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER) "
//						+ " or  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.ZJH like '%"+sarchString+"%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER) "
//						+ ")");
//			}else{
//				if(regexResult) {
//					handingsql.append(" and ( WLSH like '%"+sarchString+"%' )");
//				}else {
					handingsql.append(" and (PROJECT_NAME like '%"+sarchString+"%' "
//							+ " or FILE_NUMBER like '%"+sarchString+"%' or"
//							+ " BATCH like '%"+sarchString+"%' or"
							+ " or PROLSH like '%"+sarchString+"%' " +
							" or WLSH like '%"+sarchString+"%' )");
//				}
			
				// 支持权利人查询
//			}
		}
		
		// 申请人
		if (sqr != null && !sqr.equals("")) {
			// 判断汉字----
			sqr=sqr.replace(" ", "");
			if (isChinese(sqr)) {
				handingsql.append("and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%"
						+ sqr
						+ "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER)");
			}
		}

		//完税状态搜索
		if(!StringHelper.isEmpty(taxstatus)) {
			if("0".equals(taxstatus)) {
				handingsql.append(" AND PRO_DATE.TAXCONFIRMTIME IS NULL AND PRO_DATE.FILE_NUMBER IS NOT NULL ");
			} else if("1".equals(taxstatus)) {
				handingsql.append(" AND PRO_DATE.TAXCONFIRMTIME IS NOT NULL ");
			} else if("2".equals(taxstatus)){
				handingsql.append(" AND PRO_DATE.FILE_NUMBER IS NOT NULL ");
			}
		}

		//网签状态搜索
		if(!StringHelper.isEmpty(netstatus)) {
			if("0".equals(netstatus)) {
				handingsql.append(" AND PRO_DATE.NETSIGNCONFIRMTIME IS NULL AND PRO_DATE.FILE_NUMBER IS NOT NULL ");
			} else if("1".equals(netstatus)) {
				handingsql.append(" AND PRO_DATE.NETSIGNCONFIRMTIME IS NOT NULL ");
			} else if("2".equals(netstatus)){
				handingsql.append(" AND PRO_DATE.FILE_NUMBER IS NOT NULL ");
			}
		}

		//首页弹窗，已完税或已网签
		if(!StringHelper.isEmpty(tax_netstatus)) {
			if("1".equals(tax_netstatus)) {
				handingsql.append(" AND （PRO_DATE.NETSIGNCONFIRMTIME IS NOT NULL OR PRO_DATE.TAXCONFIRMTIME IS NOT NULL ) ");
			}
		}

		// 流水起始号
		if (startlsh != null && !startlsh.equals("")) {
			handingsql.append("and PROLSH>='"+startlsh+"'");
		}
		// 流水起始号
		if (endlsh != null && !endlsh.equals("")) {
			handingsql.append("and PROLSH<='"+endlsh+"'");
		}
		if (ywh != null && !ywh.equals("")) {
			handingsql.append(" and ywh like '%"+ywh+"%' ");
		}
		if (actinst_name != null && !actinst_name.equals("")) {
			handingsql.append(" and AI.actinst_name in ( "+actinst_name+")");
		}
		if(prolshs!=null&&!prolshs.equals("")){
			handingsql.append(" and prolsh in("+prolshs+") ");
		}
		if(outTimeStatus!=null&&!outTimeStatus.equals("")){
			Date nowDate = new Date();
			GregorianCalendar gre  = new GregorianCalendar();
			gre.setTime(nowDate);
			Date date = smHoliday.addDateByWorkDay(gre, 2);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sqlNowDate= format.format(nowDate);
			String sqlDate= format.format(date);
			if(outTimeStatus.equals("jjcq")){
				handingsql.append(" and proinst_willfinish > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') and proinst_willfinish< TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(outTimeStatus.equals("ycq")){
				handingsql.append(" and proinst_willfinish < TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(outTimeStatus.equals("jjcq,ycq")){
				handingsql.append(" and proinst_willfinish < TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else{
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(nowDate);
				Date outDate=smHoliday.addDateByWorkDay(cal, Integer.parseInt(outTimeStatus));
				handingsql.append(" and proinst_willfinish < TO_DATE('"+new java.sql.Date(outDate.getTime())+"','yyyy-mm-dd HH24:mi:ss') ");
			}
		}
		if(actInst_Status==9){
			if(starttime!=null&&!starttime.equals("")&&
					(endtime==null||endtime.equals(""))){
				handingsql.append(" and AI.ACTINST_START>to_date('"+starttime+"','yyyy-MM-dd')");
			}
			if(endtime!=null&&!endtime.equals("")&&
					(starttime==null||starttime.equals(""))){
				handingsql.append(" and AI.ACTINST_START<=to_date('"+endtime+"','yyyy-MM-dd')");
			}	
			if(starttime!=null&&endtime!=null&&!endtime.equals("")
					&&!starttime.equals("")){
				handingsql.append(" and AI.ACTINST_START>to_date('"+starttime+"','yyyy-MM-dd')"
						+ " and  AI.ACTINST_START<=to_date('"+endtime+"','yyyy-MM-dd') ");
			}
		}
		
		sqlmap.put("COUNT", "FROM  ("+handingsql.toString()+")");
		sqlmap.put("CONTENT", handingsql.toString());
		return sqlmap;
	}
	
	/**
	 * @author dff
	 * 在办箱查询nowactinst
	 * @return
	 */
	public  Map<String,String> getNowActinstSql(String staff_Id, int actInst_Status,
			String sarchString, int CurrentPageIndex, int pageSize,
			String pronames, String sqr, String startlsh, String endlsh,
			String actinst_name, String order,
			String starttime,String endtime,String handlepsersons,String roleid,String ywh,String prolshs,String outTimeStatus){
		Map<String,String > sqlmap = new HashMap<String, String>(); 
		StringBuilder handingsql =new StringBuilder();
		handingsql.append(" select * from ");
		handingsql.append(Common.WORKFLOWDB);
		handingsql.append("wfi_nowactinst where PASSEDROUTE_COUNT is null ");
		if(!StringHelper.isEmpty(staff_Id)){
			handingsql.append(" and staff_id ='");
			handingsql.append(staff_Id + "' ");
		}
		if(actInst_Status == 8){
			handingsql.append(" and actInst_Status in (1,2,14,15) ");
		} else {
			handingsql.append(" and actInst_Status in(14,15,"+actInst_Status+")");
		}
		
		if (!StringHelper.isEmpty(sarchString)) {
			handingsql.append(" and (PROJECT_NAME like '%"+sarchString+"%' "
					+ " or FILE_NUMBER like '%"+sarchString+"%' or"
					+ "  PROLSH like '%"+sarchString+"%')");
			// 支持权利人查询
		}
		
		if (pronames != null && pronames.split(",").length == 1&&!pronames.equals("")) {
			if (pronames.endsWith("'")) {
				pronames = pronames.substring(1, pronames.length() - 1);
			}
			pronames = pronames.replace("-", ",");
			handingsql.append( " and prodef_name like '"+pronames+"%' ");

		} else if (pronames != null && !pronames.equals("")) {
			handingsql.append(" and prodef_name in ("+pronames+" )");
		}
		
		if (!StringHelper.isEmpty(sqr)) {
			// 判断汉字----
			sqr=sqr.replace(" ", "");
			if (isChinese(sqr)) {
				handingsql.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM LIKE '%"
						+ sqr
						+ "%' AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=FILE_NUMBER)");
			}
		}
		
		// 流水起始号
		if (startlsh != null && !startlsh.equals("")) {
			handingsql.append(" and PROLSH>='"+startlsh+"'");
		}
		// 流水起始号
		if (endlsh != null && !endlsh.equals("")) {
			handingsql.append(" and PROLSH<='"+endlsh+"'");
		}
		
		if (actinst_name != null && !actinst_name.equals("")) {
			handingsql.append(" and actinst_name in ( "+actinst_name+")");
		}
		if(prolshs!=null&&!prolshs.equals("")){
			handingsql.append(" and prolsh in("+prolshs+") ");
		}
		
		if(outTimeStatus!=null&&!outTimeStatus.equals("")){
			Date nowDate = new Date();
			Date date=smHoliday.FactDate(nowDate,2);
			java.sql.Date sqlNowDate=new java.sql.Date(nowDate.getTime());
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			if(outTimeStatus.equals("jjcq")){
				handingsql.append(" and proinst_willfinish > TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') and proinst_willfinish< TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else if(outTimeStatus.equals("ycq")){
				handingsql.append(" and proinst_willfinish < TO_DATE('"+sqlNowDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}else{
				handingsql.append(" and proinst_willfinish < TO_DATE('"+sqlDate+"','yyyy-mm-dd HH24:mi:ss') ");
			}
		}
		
		if (ywh != null && !ywh.equals("")) {
			handingsql.append(" and ywh like '%"+ywh+"%' ");
		}
		
		sqlmap.put("COUNT", "FROM  ("+handingsql.toString()+")");
		sqlmap.put("CONTENT", handingsql.toString());
		return sqlmap;
	}
	
	/**
	 * 档案移交查询,归档项目查询sql
	 * @author JHX
	 * 
	 * */
	public  Map<String,String> getFileProjectSQL(String Status, String staff_id, int page,
			int size, String key, String actinstname, String staffs,String proname, String starttime, String endtime,
			String deptname, String areas , String hk, String lshstart, String lshend,String actinstids,String sorttype){
		       Map<String,String > sqlmap = new HashMap<String, String>(); 
		       //判断是否是包含中文
				String regEx = "[\\u4e00-\\u9fa5]";
				Pattern p = Pattern.compile(regEx); 
				StringBuilder sb = new StringBuilder();
				Message msg = new Message();				
				StringBuilder Actinst = new StringBuilder();
				StringBuilder Actstaff = new StringBuilder();
				StringBuilder Route = new StringBuilder();
				StringBuilder Proinst = new StringBuilder();
				StringBuilder res = new StringBuilder();
				StringBuilder area = new StringBuilder();
				Actinst.append(
						"with Actinst AS(\n" +
						"                      SELECT\n" +
						"						* FROM (SELECT ROW_NUMBER() OVER(PARTITION BY AI.PROINST_ID ORDER BY  AI.ACTINST_END DESC) rn,\n" + 
						"                       AI.PROINST_ID,\n" + 
						"                       AI.ACTINST_ID,\n" + 
						"                       AI.ACTDEF_TYPE,\n" + 
						"                       AI.OPERATION_TYPE,\n" + 
						"                       AI.STAFF_ID,\n" + 
						"                       AI.staff_name,\n" + 
						"                       AI.ACTDEF_ID,\n" + 
						"                       AI.ACTINST_END,\n" + 
						"                       AI.ACTINST_START,\n" + 
						"                       AI.ACTINST_STATUS,\n" + 
						"                       AI.ACTINST_WILLFINISH,\n" + 
						"                       AI.ACTINST_NAME,\n" + 
						"                       AI.ACTINST_MSG,\n" + 
						"                       AI.ISTRANSFER,\n" + 
						"                       AI.TRANSFER_TIME,\n" + 
						"						AI.TOSTAFFNAME,\n" +
						"                        AI.CODEAL\n" + 
						"                   FROM BDC_WORKFLOW.WFI_ACTINST AI  where AI.Passedroute_Count is null\n" + 
						"                        and AI.ACTINST_STATUS in (3, 0, 14)\n" 
						//"),"
						);
				
				Actstaff.append(
						"where rn=1),Actstaff as(\n" +
								"  select\n" + 
								"   ACTSTAFF.ACTINST_ID,\n" + 
								"   ACTSTAFF.STAFF_ID AS ACTSTAFF,\n" + 
								"   ACTSTAFF.Staff_Name as actstaffname\n" + 
								"  FROM BDC_WORKFLOW.WFI_ACTINSTSTAFF ACTSTAFF\n" + 
								" ),");
				Route.append(
						"route as(\n" +
								" select\n" +
								"* FROM (SELECT ROW_NUMBER() OVER(PARTITION BY RO.PROINST_ID ORDER BY  RO.creat_time DESC) r,\n" +
								" RO.CREAT_TIME,\n" +
								" RO.PROINST_ID as ROU\n" + 
								" from BDC_WORKFLOW.WFI_ROUTE RO  WHERE ROUTE_NAME='转到归档'\n" +
								 ") where r=1),"
						);
				Proinst.append(
						"Proinst as (\n" +
								"  select\n" + 
								"                      PO.PROINST_ID,\n" + 
								"                      PO.PROJECT_NAME,\n" + 
								"                       PO.FILE_NUMBER,\n" + 
								"                       PO.PRODEF_ID,\n" + 
								"                       PO.PRODEF_NAME,\n" + 
								"                       PO.PROINST_STATUS,\n" + 
								"                       PO.PROINST_END,\n" + 
								"                       PO.PROINST_START,\n" + 
								"                       PO.PROINST_WILLFINISH,\n" + 
								"                       PO.PROINST_CODE,\n" + 
								"                       PO.PROLSH,\n" + 
								"                       PO.Urgency,\n" + 
								"                       PO.acceptor,\n" + 
								"                       PO.Proinst_Time,\n" + 
								"                       PO.PROINST_WEIGHT\n" + 
								"  from BDC_WORKFLOW.WFI_PROINST PO  where 1=1\n" 
								//"),"
								);
				if(hk!=null){
				area.append(
						"info as(\n" +
					  "select\n" +
					                       "IO.FILE_NUMBER,\n" +
					                       "IO.USERNAME,\n" +
					                       "IO.TEL,\n" +
					                       "IO.AREA,\n" +
					                       "IO.ADDRESS,\n" +
					                       "IO.PROTYPE,\n" +
					                       "IO.BDCQZH,\n" +
					                       "IO.BDCDYZL\n" +
					  "from BDC_WORKFLOW.WFI_PROUSERINFO IO\n"
						);
				};
				res.append(
					"res as (\n" +
					"                       SELECT row_number() over(partition by  PO.file_number,AI.actdef_id order by AI.actinst_start asc) rn,\n");
				if(hk!=null){
					res.append("				IO.USERNAME,\n" +
	                    "						IO.TEL,\n" +
	                    "						IO.AREA,\n" +
	                    "						IO.ADDRESS,\n" +
	                    "						IO.PROTYPE,\n" +
	                    "						IO.BDCQZH,\n" +
	                    "						IO.BDCDYZL,\n");
				};
				res.append("                PO.PROJECT_NAME,\n" + 
					"                       AI.ACTDEF_TYPE,\n" + 
					"                       PO.FILE_NUMBER,\n" + 
					"                       ACTSTAFF.ACTSTAFF,\n" + 
					"                       ACTSTAFF.actstaffname,\n" + 
					"                       PO.PROINST_ID,\n" + 
					"                       PO.PRODEF_ID,\n" + 
					"                       PO.PRODEF_NAME,\n" + 
					"                       PO.PROINST_STATUS,\n" + 
					"                       PO.PROINST_END,\n" + 
					"                       PO.PROINST_START,\n" + 
					"                       PO.PROINST_WILLFINISH,\n" + 
					"                       PO.PROINST_CODE,\n" + 
					"                       PO.ACCEPTOR,\n" + 
					"                       AI.ACTINST_ID,\n" + 
					"                       AI.OPERATION_TYPE,\n" + 
					"                       AI.STAFF_ID,\n" + 
					"                       AI.staff_name,\n" + 
					"                       AI.ACTDEF_ID,\n" + 
					"                       AI.ACTINST_STATUS,\n" + 
					"                       AI.ACTINST_START,\n" + 
					"                       AI.ACTINST_END,\n" + 
					"                       AI.ACTINST_WILLFINISH,\n" + 
					"                       AI.ACTINST_NAME,\n" + 
					"                       AI.ACTINST_MSG,\n" + 
					"                       AI.ISTRANSFER,\n" + 
					"                       AI.TRANSFER_TIME,\n" +
					"						AI.TOSTAFFNAME,\n" +
					"                       PO.PROLSH,\n" + 
					"                       AI.CODEAL,\n" + 
					"                       PO.Urgency,\n" + 
					"                       PO.Proinst_Time,\n" + 
					"                       PO.PROINST_WEIGHT\n" + 
					"                --ACTSTAFF.TENANT_ID--视图更改位置\n" + 
					"                  FROM Actstaff ACTSTAFF\n" + 
					"                 INNER JOIN Actinst AI\n" + 
					"                    ON ACTSTAFF.ACTINST_ID = AI.ACTINST_ID\n" + 
					"                 INNER JOIN Proinst PO\n" + 
					"                    ON AI.PROINST_ID = PO.PROINST_ID\n"
						);
				
				if (Status != null && !Status.equals("")) {
					if (!Status.equals("0")) {
						Actinst.append(" and istransfer = "+Status);
					}
				} else {
					Actinst.append(" and istransfer is null ");

				}
				if(actinstids!=null){
					Actinst.append(" and actinst_id in ('" + actinstids + "')");
				}
				Matcher m = p.matcher(key);
				if (key != null && !key.equals("")) {
					if(m.find()){
						Proinst.append(" and (project_name like '%"+key+"%' or file_number like '%"+key+"%'");
					}else{
						Proinst.append(" and (project_name = '"+key+"' or file_number = '"+key+"'");
					}
					Proinst.append(" or acceptor='"+key+"' ");
					Proinst.append(" or prolsh='"+key+"')");
				}
				
				// 流水起始号
				if (lshstart != null && !lshstart.equals("")) {
					Proinst.append("and PROLSH>='"+lshstart+"'");
				}
				// 流水起始号
				if (lshend != null && !lshend.equals("")) {
					Proinst.append("and PROLSH<='"+lshend+"'");
				}
				
				if (actinstname != null && !actinstname.equals("")) {
					Actinst.append(" and AI.ACTINST_NAME in ("+actinstname+") ");
				}
				if (staffs != null && !staffs.equals("")) {
					Actinst.append(" and AI.staff_id in ( "+staffs+") ");
				} else {
					if (deptname != null && !deptname.equals("")) {
						StringBuilder sbs = new StringBuilder();
						sbs.append(" select id from ");
						sbs.append("SMWB_FRAMEWORK.");
						sbs.append("t_department  where departmentname='" + deptname
								+ "'");
						List<Map> list = commonDao.getDataListByFullSql(sbs.toString());
						if (list != null && list.size() > 0) {
							for (Map map : list) {
								Set set = map.keySet();
								Iterator it = set.iterator();
								while (it.hasNext()) {
									Object keys = it.next();
									Object value = map.get(keys);
									if (value != null && value != "") {
										List<User> users = userService
												.findUserByDepartmentId((String) value);
										if (users != null && users.size() > 0) {
											String sql = "'0'";
											for (User u : users) {
												sql += ",'" + u.getId() + "'";
											}
											Actinst.append(" and AI.staff_id in( ");
											Actinst.append(sql);
											Actinst.append(")");
										}
									}
								}
							}
						}
					} else {
						User user = userService.findById(staff_id);
						if (user != null) {
							List<User> users = userService.findUserByDepartmentId(user
									.getDepartment().getId());
							if (users != null && users.size() > 0) {
								String sql = "'0'";
								for (User u : users) {
									sql += ",'" + u.getId() + "'";
								}
								Actinst.append(" and AI.staff_id in( ");
								Actinst.append(sql);
								Actinst.append(")");
							}
						}
					}

				}
				if (proname != null && !proname.equals("")) {
					Proinst.append(" and PRODEF_NAME in ( "+proname+") ");
				}
				if (starttime != null && !starttime.equals("")) {
					Actinst.append(" and AI.ACTINST_END>to_date('" + starttime
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}
				if (endtime != null && !endtime.equals("")) {
					Actinst.append(" and AI.ACTINST_END<to_date('" + endtime
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}
				if(hk!=null){
					if(areas!=null&&!areas.equals("")){
					area.append(" where area in ( "+areas+")) ,");
					res.append("INNER JOIN Info IO ON PO.PROLSH = IO.FILE_NUMBER) ");
					}else{
						area.append("),");
						res.append("LEFT JOIN Info IO ON PO.PROLSH = IO.FILE_NUMBER) ");
					};
				}else{
					res.append(")");
				}
				String sqlinfo;
				sqlmap.put("COUNT", Actinst.toString()+")"
				+Actstaff.toString()+Proinst.toString()+"),"+area.toString()+res.toString()+
						"select count(1) from res where rn=1");
				 sqlinfo=Actinst.toString()+")"+Actstaff.toString()+Route.toString()+Proinst.toString()+"),"+area.toString()+res.toString()+
						"select * from res LEFT JOIN route ON route.ROU=RES.PROINST_ID where rn=1";
//				if (actinstname.indexOf("发证")>=0){
//					sqlinfo+=" ORDER BY route.CREAT_TIME";
//				}else{
//					sqlinfo+=" ORDER BY res.PROLSH";
//				}
				if (sorttype!=null&&sorttype.equals("1")) {
					sqlinfo+=" ORDER BY res.PROLSH";
				}else {
					sqlinfo+=" ORDER BY res.ACTINST_END";
				}
			
				System.out.println("我的查询sql："+sqlinfo);
				sqlmap.put("CONTENT",sqlinfo);
		return sqlmap;
	}
	
	/**
	 * 优化档案移交查询速度
	 * @author dff
	 * @param sorttype 
	 * @data 2017-07-06
	 * @return
	 */
	public  Map<String,String> getFileProjectSqlEx(String Status, String staff_id, int page,
			int size, String key, String actinstname, String staffs,String proname, String starttime, String endtime,
			String deptname, String areas , String hk, String lshstart, String lshend,String actinstids, String sorttype){
		 Map<String,String > sqlmap = new HashMap<String, String>();
		 
		 String regEx = "[\\u4e00-\\u9fa5]";
			Pattern p = Pattern.compile(regEx); 
		 StringBuilder sb = new StringBuilder("from ");
		 sb.append(Common.WORKFLOWDB);
		 sb.append("WFI_PROINST PO LEFT JOIN  ");
		 sb.append("(select * from ");
		 sb.append("(SELECT AI.*,ROW_NUMBER() OVER(PARTITION BY PROINST_ID ORDER BY ACTINST_END DESC ) RN FROM ");
		 sb.append(Common.WORKFLOWDB);
		 sb.append("WFI_ACTINST AI WHERE AI.Passedroute_Count is null ");
		 sb.append("AND AI.ACTINST_STATUS in (3, 0, 14) ");
		 if (Status != null && !Status.equals("")) {
				if (!Status.equals("0")) {
					sb.append(" and istransfer = "+Status);
				}
			} else {
				sb.append(" and istransfer is null ");

			}
			if(actinstids!=null){
				sb.append(" and actinst_id in ('" + actinstids + "')");
			}
			
			if (actinstname != null && !actinstname.equals("")) {
				sb.append(" and AI.ACTINST_NAME in ("+actinstname+") ");
			}
			if (staffs != null && !staffs.equals("")) {
				sb.append(" and AI.staff_id in ( "+staffs+") ");
			} else {
				if (deptname != null && !deptname.equals("")) {
					StringBuilder sbs = new StringBuilder();
					sbs.append(" select id from ");
					sbs.append("SMWB_FRAMEWORK.");
					sbs.append("t_department  where departmentname='" + deptname
							+ "'");
					List<Map> list = commonDao.getDataListByFullSql(sbs.toString());
					if (list != null && list.size() > 0) {
						for (Map map : list) {
							Set set = map.keySet();
							Iterator it = set.iterator();
							while (it.hasNext()) {
								Object keys = it.next();
								Object value = map.get(keys);
								if (value != null && value != "") {
									List<User> users = userService
											.findUserByDepartmentId((String) value);
									if (users != null && users.size() > 0) {
										String sql = "'0'";
										for (User u : users) {
											sql += ",'" + u.getId() + "'";
										}
										sb.append(" and AI.staff_id in( ");
										sb.append(sql);
										sb.append(")");
									}
								}
							}
						}
					}
				} else {
					User user = userService.findById(staff_id);
					if (user != null) {
						List<User> users = userService.findUserByDepartmentId(user
								.getDepartment().getId());
						if (users != null && users.size() > 0) {
							String sql = "'0'";
							for (User u : users) {
								sql += ",'" + u.getId() + "'";
							}
							sb.append(" and AI.staff_id in( ");
							sb.append(sql);
							sb.append(")");
						}
					}
				}

			}
			
			if (starttime != null && !starttime.equals("")) {
				sb.append(" and AI.ACTINST_END>to_date('" + starttime
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			if (endtime != null && !endtime.equals("")) {
				sb.append(" and AI.ACTINST_END<to_date('" + endtime
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
		 sb.append(") WHERE RN = 1 ) A ");
		 sb.append("ON PO.PROINST_ID = A.PROINST_ID ");
		 sb.append("WHERE A.ACTINST_ID IS NOT NULL ");
		 sb.append("AND PO.PROINST_STATUS!='0' ");
		 Matcher m = p.matcher(key);
			if (key != null && !key.equals("")) {
				if(m.find()){
					sb.append(" and (project_name like '%"+key+"%' or file_number like '%"+key+"%'");
				}else{
					sb.append(" and (project_name = '"+key+"' or file_number = '"+key+"'");
				}
				sb.append(" or acceptor='"+key+"' ");
				sb.append(" or prolsh='"+key+"')");
			}
			
			// 流水起始号
			if (lshstart != null && !lshstart.equals("")) {
				sb.append("and PROLSH>='"+lshstart+"'");
			}
			// 流水起始号
			if (lshend != null && !lshend.equals("")) {
				sb.append("and PROLSH<='"+lshend+"'");
			}
			if (proname != null && !proname.equals("")) {
				sb.append(" and PRODEF_NAME in ( "+proname+") ");
			}
			String brfore = "select PO.PROJECT_NAME,\n" + 
					"                       A.ACTDEF_TYPE,\n" + 
					"                       PO.FILE_NUMBER,\n" + 
					"                       PO.PROINST_ID,\n" + 
					"                       PO.PRODEF_ID,\n" + 
					"                       PO.PRODEF_NAME,\n" + 
					"                       PO.PROINST_STATUS,\n" + 
					"                       PO.PROINST_END,\n" + 
					"                       PO.PROINST_START,\n" + 
					"                       PO.PROINST_WILLFINISH,\n" + 
					"                       PO.PROINST_CODE,\n" + 
					"                       PO.ACCEPTOR,\n" + 
					"                       A.ACTINST_ID,\n" + 
					"                       A.OPERATION_TYPE,\n" + 
					"                       A.STAFF_ID,\n" + 
					"                       A.staff_name,\n" + 
					"                       A.ACTDEF_ID,\n" + 
					"                       A.ACTINST_STATUS,\n" + 
					"                       A.ACTINST_START,\n" + 
					"                       A.ACTINST_END,\n" + 
					"                       A.ACTINST_WILLFINISH,\n" + 
					"                       A.ACTINST_NAME,\n" + 
					"                       A.ACTINST_MSG,\n" + 
					"                       A.ISTRANSFER,\n" + 
					"                       A.TRANSFER_TIME,\n" +
					"						A.TOSTAFFNAME,\n" +
					"                       PO.PROLSH,\n" + 
					"                       A.CODEAL,\n" + 
					"                       PO.Urgency,\n" + 
					"                       PO.Proinst_Time,\n" + 
					"                       PO.PROINST_WEIGHT\n ";
			if (sorttype!=null&&sorttype.equals("1")) {
				sb.append(" ORDER BY  PO.PROLSH  ");
			}else if (sorttype!=null&&sorttype.equals("2")) {
				sb.append("  ORDER BY  A.ACTINST_END   ");
			}
		
		sqlmap.put("CONTENT",brfore + sb.toString());
		sqlmap.put("COUNT", "select count(1) "+sb.toString());
		return sqlmap;
	}
	
	private static boolean  isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

}
