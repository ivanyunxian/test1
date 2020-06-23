package com.supermap.realestate.registration.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.check.CheckItem;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoJY;
import com.supermap.wisdombusiness.web.ResultMessage;

public class SignRecordCheck implements CheckItem{
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage check(HashMap<String,String> params) {
		String xmbh=params.get("XMBH");
		String bdcdyid=params.get("BDCDYID");
		//String qlid=params.get("QLID");
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("true");
		String bdcdylx="031";
		StringBuilder builderLX=new StringBuilder();
		builderLX.append("SELECT BASEWORKFLOW.UNITTYPE FROM BDCK.BDCS_XMXX XMXX ");
		builderLX.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ");
		builderLX.append("LEFT JOIN BDC_WORKFLOW.WFD_MAPPING GX ON GX.WORKFLOWCODE=PROINST.PROINST_CODE ");
		builderLX.append("LEFT JOIN BDCK.T_BASEWORKFLOW BASEWORKFLOW ON BASEWORKFLOW.ID=GX.WORKFLOWNAME ");
		builderLX.append("WHERE XMXX.XMBH='");
		builderLX.append(xmbh);
		builderLX.append("'");
		List<Map> list_lx=dao.getDataListByFullSql(builderLX.toString());
		if(list_lx!=null&&list_lx.size()>0){
			bdcdylx=StringHelper.formatObject(list_lx.get(0).get("UNITTYPE"));
		}
		if(!"031".equals(bdcdylx)&&!"032".equals(bdcdylx)){
			ms.setSuccess("false");
			ms.setMsg("非房屋，无法校验网签信息！");
			return ms;
		}
		List<String> list_bdcdyid=new ArrayList<String>();
		list_bdcdyid.add(bdcdyid);
		List<String> list_fwbm=new ArrayList<String>();
		if("031".equals(bdcdylx)){
			List<Map> list_sch=dao.getDataListByFullSql("SELECT FWBM FROM BDCK.BDCS_H_XZ WHERE BDCDYID='"+bdcdyid+"'");
			if(list_sch!=null&&list_sch.size()>0){
				String fwbm=StringHelper.formatObject(list_sch.get(0).get("FWBM"));
				if(!StringHelper.isEmpty(fwbm)){
					list_fwbm.add(fwbm);
				}
			}
		}else{
			List<Map> list_sch=dao.getDataListByFullSql("SELECT FWBM FROM BDCK.BDCS_H_XZY WHERE BDCDYID='"+bdcdyid+"'");
			if(list_sch!=null&&list_sch.size()>0){
				String fwbm=StringHelper.formatObject(list_sch.get(0).get("FWBM"));
				if(!StringHelper.isEmpty(fwbm)){
					list_fwbm.add(fwbm);
				}
			}
		}
		if("031".equals(bdcdylx)){
			StringBuilder ychsql=new StringBuilder();
			ychsql.append("SELECT YCH.BDCDYID,FWBM FROM BDCK.BDCS_H_XZY YCH ");
			ychsql.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=YCH.BDCDYID ");
			ychsql.append("WHERE GX.SCBDCDYID='");
			ychsql.append(bdcdyid);
			ychsql.append("'");
			List<Map> list_ych=dao.getDataListByFullSql(ychsql.toString());
			if(list_ych!=null&&list_ych.size()>0){
				for(Map ych:list_ych){
					String ycbdcdyid=StringHelper.formatObject(ych.get("BDCDYID"));
					String fwbm=StringHelper.formatObject(ych.get("FWBM"));
					if(!StringHelper.isEmpty(fwbm)&&!list_fwbm.contains(fwbm)){
						list_fwbm.add(fwbm);
					}
					if(!StringHelper.isEmpty(ycbdcdyid)&&!list_bdcdyid.contains(ycbdcdyid)){
						list_bdcdyid.add(ycbdcdyid);
					}
				}
			}
		}else{
			StringBuilder schsql=new StringBuilder();
			schsql.append("SELECT SCH.BDCDYID,SCH.FWBM FROM BDCK.BDCS_H_XZ SCH ");
			schsql.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID=SCH.BDCDYID ");
			schsql.append("WHERE GX.YCBDCDYID='");
			schsql.append(bdcdyid);
			schsql.append("'");
			List<Map> list_sch=dao.getDataListByFullSql(schsql.toString());
			if(list_sch!=null&&list_sch.size()>0){
				for(Map sch:list_sch){
					String scbdcdyid=StringHelper.formatObject(sch.get("BDCDYID"));
					String fwbm=StringHelper.formatObject(sch.get("FWBM"));
					if(!StringHelper.isEmpty(fwbm)&&!list_fwbm.contains(fwbm)){
						list_fwbm.add(fwbm);
					}
					if(!StringHelper.isEmpty(scbdcdyid)&&!list_bdcdyid.contains(scbdcdyid)){
						list_bdcdyid.add(scbdcdyid);
					}
				}
			}
		}
		
		List<String> list_casenum=new ArrayList<String>();
		StringBuilder qlsql=new StringBuilder();
		qlsql.append("SELECT QL.CASENUM FROM BDCK.BDCS_DJDY_XZ DJDY ");
		qlsql.append("LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID ");
		qlsql.append("WHERE QL.CASENUM IS NOT NULL ");
		qlsql.append("AND DJDY.BDCDYID IN ('");
		qlsql.append(StringHelper.formatList(list_bdcdyid, "','"));
		qlsql.append("')");
		
		List<Map> list_qlinfo=dao.getDataListByFullSql(qlsql.toString());
		if(list_qlinfo!=null&&list_qlinfo.size()>0){
			for(Map qlinfo:list_qlinfo){
				String casenum=StringHelper.formatObject(qlinfo.get("CASENUM"));
				if(!StringHelper.isEmpty(casenum)&&!list_casenum.contains(casenum)){
					list_casenum.add(casenum);
				}
			}
		}
		CommonDaoJY daojy = SuperSpringContext.getContext().getBean(CommonDaoJY.class);
		StringBuilder signsql=new StringBuilder();
		signsql.append("SELECT QYBAXX.YWH,HTH,QLRMC,QLRLX,QLRFL ");
		signsql.append("FROM QYBAXX QYBAXX ");
		signsql.append("LEFT JOIN QLR QLR ON QLR.YWH=QYBAXX.YWH ");
		signsql.append("WHERE QYBAXX.ZT='1' AND QYBAXX.FWBM in ('");
		signsql.append(StringHelper.formatList(list_fwbm, "','"));
		signsql.append("')");
//		StringBuilder signsql=new StringBuilder();
//		signsql.append("SELECT QYBAXX.YWH,HTH,QLRMC,QLRLX,QLRFL ");
//		signsql.append("FROM QYBAXX@LINKZJK QYBAXX ");
//		signsql.append("LEFT JOIN QLR@LINKZJK QLR ON QLR.YWH=QYBAXX.YWH ");
//		signsql.append("WHERE QYBAXX.ZT='1' AND QYBAXX.FWBM in ('");
//		signsql.append(StringHelper.formatList(list_fwbm, "','"));
//		signsql.append("')");
		List<Map> list_signinfo=daojy.getDataListByFullSql(signsql.toString());
		HashMap<String,List<String>> list_sign=new HashMap<String,List<String>>();
		if(list_signinfo!=null&&list_signinfo.size()>0){
			for(Map signinfo:list_signinfo){
				String hth=StringHelper.formatObject(signinfo.get("HTH")); 
				String qlrmc=StringHelper.formatObject(signinfo.get("QLRMC")); 
				String qlrfl=StringHelper.formatObject(signinfo.get("QLRFL"));
				if(list_casenum.contains(hth)){
					continue;
				}
				ms.setSuccess("false");
				if(StringHelper.isEmpty(hth)){
					continue;
				}
				if(!StringHelper.isEmpty(qlrfl)&&("出卖人".equals(qlrfl))){
					continue;
				}
				if(list_sign.containsKey(hth)){
					List<String> list_qlrmc=list_sign.get(hth);
					if(!StringHelper.isEmpty(qlrmc)&&!list_qlrmc.contains(qlrmc)){
						list_qlrmc.add(qlrmc);
					}
					list_sign.put(hth, list_qlrmc);
				}else{
					List<String> list_qlrmc=new ArrayList<String>();
					if(!StringHelper.isEmpty(qlrmc)){
						list_qlrmc.add(qlrmc);
					}
					list_sign.put(hth, list_qlrmc);
				}
			}
		}
		StringBuilder builder=new StringBuilder();
		if(list_sign.size()>0){
			builder.append("该房屋有网签：");
			int ss=1;
			for (Map.Entry<String, List<String>> entry : list_sign.entrySet()) {
				String hth=entry.getKey();
				List<String> list_qlr=entry.getValue();
				if(ss==list_sign.size()){
					builder.append(hth).append(":").append(StringHelper.formatList(list_qlr, "、")).append(";");
				}else{
					builder.append(hth).append(":").append(StringHelper.formatList(list_qlr, "、")).append(";").append("<br/>");
				}
				ss++;
			} 
		}
		ms.setMsg(builder.toString());
		return ms;
	}
}
