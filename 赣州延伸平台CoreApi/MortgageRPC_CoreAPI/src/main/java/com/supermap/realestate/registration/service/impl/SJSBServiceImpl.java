package com.supermap.realestate.registration.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_REPORTINFO;
import com.supermap.realestate.registration.model.BDCS_SJSB;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.SJSBService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

@Service("sjsbService")
public class SJSBServiceImpl implements SJSBService {

	@Autowired
	private CommonDao baseCommonDao;
	
	@Autowired
	private ProjectService projectService;

	@SuppressWarnings("unchecked")
	@Override
	public Message getPagedList(int page, int rows, Map<String, Object> mapCondition) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (mapCondition == null){
			mapCondition = new HashMap<String, Object>();
		}
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (key.equals("operateUser")){
				if (value != null && !value.toString().equals("")) {
					hqlBuilder.append(" and " + key + " like '%" +  value + "%' ");
				}
			}
			if (key.equals("reccode")){
				if (value != null && !value.toString().equals("")) {
					hqlBuilder.append(" and " + key + " = '" + value + "' ");
				}
			}
			if (key.equals("successflag")){
				if (value != null && !value.toString().equals("")) {
					hqlBuilder.append(" and " + key + " = '" + value + "' ");
				}
			}
			if ("id_sjq".equals(key)){
				if (value != null && !value.toString().equals("")) {
					hqlBuilder.append(" and OPERATETIME BETWEEN TO_DATE('"+value+" 00:00:00',' yyyy-MM-dd hh24:mi:ss')");
				}
			}
			if ("id_sjz".equals(key)){
				if (value != null && !value.toString().equals("")) {
					hqlBuilder.append("  AND TO_DATE('"+value+" 23:59:59',' yyyy-MM-dd hh24:mi:ss') ");
				}
			}
		}
		hqlBuilder.append(" order by operatetime desc ");
		Page p = baseCommonDao.getPageDataByHql(BDCS_SJSB.class, hqlBuilder.toString(), page, rows);
		List<BDCS_SJSB> sjsbs = new ArrayList<BDCS_SJSB>();
		sjsbs = (List<BDCS_SJSB>) p.getResult();
		for (BDCS_SJSB bdcs_SJSB : sjsbs) {
			bdcs_SJSB.setSUCCESSFLAG(ConstValue.SF.NO.Value.equals(bdcs_SJSB.getSUCCESSFLAG()) ? "失败" : "成功");
			bdcs_SJSB.setOperateTimeStr(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",bdcs_SJSB.getOPERATETIME()));
		}
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	public Message getPagedList1(int page, int rows, Map<String, Object> mapCondition,boolean iflike) {
		StringBuilder hqlBuilder = new StringBuilder();
		StringBuilder fullhql = new StringBuilder();
		int pageCount = 0;
		List flightPageList = new ArrayList();
		if (mapCondition == null){
			mapCondition = new HashMap<String, Object>();
		}
		String slbh = mapCondition.get("SLBH").toString();//受理编号
		String reccode = mapCondition.get("reccode").toString();//业务编码
		String successflag = mapCondition.get("successflag").toString();//状态
	    String xzqh = mapCondition.get("XZQH").toString();//行政区划
	    String SBKSSJ = mapCondition.get("SBKSSJ").toString();//上报开始时间
	    String SBJSSJ = mapCondition.get("SBJSSJ").toString();//上报结束时间
	    String SBSJ = mapCondition.get("SBSJ").toString();//上报时间
	    String DJLX = mapCondition.get("DJLX").toString();//登记类型
	    
	    hqlBuilder.append(" 1>0 ");
	    
		List<Map> listresult = null;
	    if(!"".equals(successflag) && successflag!=null){
	    	hqlBuilder.append(" and t.successflag = '"+successflag+"'");
	    }
	    if(!"".equals(SBKSSJ) && !"".equals(SBJSSJ)){
	    	hqlBuilder.append(" and (t.operatetime between to_date('"+SBKSSJ+"','YYYY-MM-DD HH24:mi:ss') and to_date('"+SBJSSJ+"','YYYY-MM-DD HH24:mi:ss'))");
	    }
	    if(!"".equals(SBKSSJ) && "".equals(SBJSSJ) ){
	    	hqlBuilder.append(" and t.operatetime > to_date('"+SBKSSJ+"','YYYY-MM-DD HH24:mi:ss')");
	    }
	    if(!"".equals(SBJSSJ) && "".equals(SBKSSJ) ){
	    	hqlBuilder.append(" and t.operatetime < to_date('"+SBJSSJ+"','YYYY-MM-DD HH24:mi:ss')");
	    }
	    if(!"".equals(reccode) && reccode!=null){
	    	hqlBuilder.append(" and t.reccode = '"+reccode+"'");
	    	if(iflike){
	    		hqlBuilder.append(" and t.reccode like '%"+reccode+"%'");
	    	}
	    }
	    if(!"".equals(slbh) && slbh!=null){
	    	
	    	if(iflike){
	    		hqlBuilder.append(" and h.prolsh like '%"+slbh+"%'");
	    	}else{
	    		hqlBuilder.append(" and h.prolsh = '"+slbh+"'");
	    	}
	    }
	    if(!"".equals(DJLX) && DJLX!=null && !DJLX.equals("undefined")){
	    	
	    	if(iflike){
	    		hqlBuilder.append(" and c.consttrans like '%"+DJLX+"%'");
	    	}else{
	    		hqlBuilder.append(" and c.consttrans = '"+DJLX+"'");
	    	}
	    }
	    if(!SBSJ.equals("") && SBSJ!=null && !SBSJ.equals("undefined")){
	    	
	    	if(iflike){
	    		hqlBuilder.append(" and t.operatetime like '%"+SBSJ+"%'");
	    	}else{
	    		hqlBuilder.append(" and t.operatetime = '"+SBSJ+"'");
	    	}
	    }
	    	    
		//hqlBuilder.append(" order by operatetime desc ");
		if(xzqh.equals("0")){
			fullhql.append("select t.bwmc as BWMC,h.prolsh as slbh,t.successflag as successflag,t.reccode as reccode,t.responseinfo as responseinfo,to_char(t.operatetime,'yyyy-mm-dd') as operateTimeStr,xmxx.xmbh as XMBH from bdck.bdcs_sjsb t left join bdc_workflow.wfi_proinst h on t.proinstid = h.proinst_id left join bdck.bdcs_xmxx xmxx on xmxx.PROJECT_ID = h.FILE_NUMBER  left join bdck.bdcs_const c on xmxx.djlx = c.constvalue where ");
			fullhql.append(hqlBuilder);
			/*fullhql.append(" union all ");
			fullhql.append("select * from bdck.bdcs_sjsb@to_orcl6_bdck t left join bdc_workflow.wfi_proinst h on t.proinstid = h.proinst_id where ");
			fullhql.append(hqlBuilder);*/
			fullhql.append(" and XMXX.SFDB='1' order by t.operatetime desc ");
		}
		if(xzqh.equals("1")){
			fullhql.append("select t.bwmc as BWMC,h.prolsh as slbh,t.successflag as successflag,t.reccode as reccode,t.responseinfo as responseinfo,to_char(t.operatetime,'yyyy-mm-dd') as operateTimeStr,xmxx.xmbh as XMBH from bdck.bdcs_sjsb t left join bdc_workflow.wfi_proinst h on t.proinstid = h.proinst_id left join bdck.bdcs_xmxx xmxx on xmxx.PROJECT_ID = h.FILE_NUMBER where ");
			fullhql.append(hqlBuilder);
			fullhql.append(" and XMXX.SFDB='1' order by t.operatetime desc ");
		}
		if(xzqh.equals("2")){
			fullhql.append("select t.bwmc as BWMC,h.prolsh as slbh,t.successflag as successflag,t.reccode as reccode,t.responseinfo as responseinfo,to_char(t.operatetime,'yyyy-mm-dd')as operateTimeStr,xmxx.xmbh as XMBH from bdck.bdcs_sjsb@to_orcl6_bdck t left join bdc_workflow.wfi_proinst@to_orcl6_bdc_workflow h on t.proinstid = h.proinst_id left join bdck.bdcs_xmxx@to_orcl6_bdck xmxx on xmxx.PROJECT_ID = h.FILE_NUMBER where ");
			fullhql.append(hqlBuilder);
			fullhql.append(" and XMXX.SFDB='1' order by t.operatetime desc ");
		}
		if(xzqh.equals("3")){
			fullhql.append("select t.bwmc as BWMC,h.prolsh as slbh,t.successflag as successflag,t.reccode as reccode,t.responseinfo as responseinfo,to_char(t.operatetime,'yyyy-mm-dd')as operateTimeStr,xmxx.xmbh as XMBH from bdck.bdcs_sjsb@to_orcl7_bdck t left join bdc_workflow.wfi_proinst@to_orcl7_bdc_workflow h on t.proinstid = h.proinst_id left join bdck.bdcs_xmxx@to_orcl7_bdck xmxx on xmxx.PROJECT_ID = h.FILE_NUMBER where ");
			fullhql.append(hqlBuilder);
			fullhql.append(" and XMXX.SFDB='1' order by t.operatetime desc ");
		}
	//	Page p = baseCommonDao.getPageDataByHql(BDCS_SJSB.class, fullhql.toString(), page, rows);
		listresult = baseCommonDao.getPageDataByFullSqlsjsb(fullhql.toString(),page, rows);
		if(listresult.size()>0){
			for(Map map : listresult){
				String value = map.get("SUCCESSFLAG").toString();
				if(value.equals("1")){
					map.put("SUCCESSFLAG","成功");
				}
				if(value.equals("0")){
					map.put("SUCCESSFLAG","失败");
				}
				
				String BWMC = map.get("BWMC").toString();
				String bizMsgID=BWMC;
				String regex = "Biz(.*)\\.xml";
				Pattern pattern = Pattern.compile(regex);
				Matcher m = pattern.matcher(BWMC);
				while (m.find()) {
					bizMsgID=m.group(1);
				}
				map.put("BWMC",bizMsgID);
			}
		}
		
		 if(listresult.size()%rows==0){
      	   pageCount=listresult.size()/rows;
      	   }else{
      	   pageCount=(listresult.size()/rows)+1;
      	   }
         if(rows>pageCount){
      	   rows=pageCount;
      	   } 
	       
         
         for (int i = ((page-1) * rows);
         i < listresult.size() && i < ((page) * rows) && page > 0; i++) {
         flightPageList.add(listresult.get(i)); 
         }
	//	long count= 0;
		Message m = new Message();
		m.setTotal(pageCount);
		m.setRows(flightPageList);
		return m;
	}
	/**
	 * 分页获取数据上报项目记录
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2016年04月10日 13:51:10
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message getSJSBData(HttpServletRequest request) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		String sjsbtype="2";
		try {
			sjsbtype = RequestHelper.getParam(request, "sjsbtype");
		} catch (Exception e) {
		}
		
		String sjsbdjlx="2";
		try {
			sjsbdjlx = RequestHelper.getParam(request, "sjsbdjlx");
		} catch (Exception e) {
		}
		String cxqssj="";
		try {
			cxqssj = RequestHelper.getParam(request, "cxqssj");
		} catch (Exception e) {
		}
		
		String cxzzsj="";
		try {
			cxzzsj = RequestHelper.getParam(request, "cxzzsj");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		
		hqlBuilder.append("FROM BDCK.BDCS_XMXX XMXX ");
		hqlBuilder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ");
		hqlBuilder.append("ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ");
		hqlBuilder.append("WHERE XMXX.SFDB='1' ");
		
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (XMXX.XMMC LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR PROINST.PRODEF_NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR XMXX.YWLSH LIKE '%").append(filter).append("%')");
		}
		
		if (!StringHelper.isEmpty(cxqssj)) {
			hqlBuilder.append(" AND XMXX.DJSJ>=TO_DATE('"+cxqssj+" 00:00:00"+"','YYYY-MM-DD HH24:MI:SS')");
		}
		
		if (!StringHelper.isEmpty(cxzzsj)) {
			hqlBuilder.append(" AND XMXX.DJSJ<=TO_DATE('"+cxzzsj+" 23:59:59"+"','YYYY-MM-DD HH24:MI:SS')");
		}
		
		if (!StringHelper.isEmpty(sjsbtype)) {
			if(!"2".equals(sjsbtype)){
				if("1".equals(sjsbtype)){
					hqlBuilder.append(" AND XMXX.SFSB='1' ");
				}else{
					hqlBuilder.append(" AND (XMXX.SFSB IS NULL OR XMXX.SFSB='0') ");
				}
			}
		}
		
		if (!StringHelper.isEmpty(sjsbdjlx)) {
			if(!"2".equals(sjsbdjlx)){
				if("100".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='100' ");
				}if("200".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='200' ");
				}if("300".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='300' ");
				}if("400".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='400' ");
				}if("500".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='500' ");
				}if("600".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='600' ");
				}if("700".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='700' ");
				}if("800".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='800' ");
				}if("900".equals(sjsbdjlx)){
					hqlBuilder.append(" AND XMXX.DJLX='900' ");
				}
			}
		}
		String strAll=hqlBuilder.toString();
		
		StringBuilder hqlBuilderFull = new StringBuilder();
		hqlBuilderFull.append("SELECT XMXX.YWLSH,XMXX.XMMC,PROINST.PRODEF_NAME,XMXX.SLRY,XMXX.DJSJ,XMXX.SFSB,XMXX.XMBH,PROINST.PROINST_ID ");
		hqlBuilderFull.append(strAll);
		hqlBuilderFull.append(" ORDER BY XMXX.DJSJ");
		
		
		String strFull=hqlBuilderFull.toString();
		List<Map> list = baseCommonDao.getPageDataByFullSql(strFull, page, rows);
		List<Map> list_new=new ArrayList<Map>();
		if(list!=null&&list.size()>0){
			for(Map map:list){
				if(map.containsKey("DJSJ")){
					String slsj=StringHelper.FormatDateOnType(map.get("DJSJ"),"yyyy-MM-dd HH-mm-ss");
					map.put("DJSJ", slsj);
				}
				list_new.add(map);
			}
		}
		Long l=baseCommonDao.getCountByFullSql(strAll);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list_new);
		m.setMsg("成功！");
		return m;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResultMessage Report(String xmbhs,HttpServletRequest request) {
		
		int isuccess=0;
		int ifail=0;
		if(!StringHelper.isEmpty(xmbhs)){
			String[] strs=xmbhs.split(";");
			if(strs!=null&&strs.length>0){
				for(String xmbh:strs){
					BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
					String project_id=xmxx.getPROJECT_ID();
					String proinst_id="";
					String actinst_id="";
					if(!StringHelper.isEmpty(project_id)){
						List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+project_id+"'");
						if(list_proinst!=null&&list_proinst.size()>0){
							proinst_id=list_proinst.get(0).getProinst_Id();
						}
					}
					if(!StringHelper.isEmpty(proinst_id)){
						List<Wfi_ActInst> list_actinst=baseCommonDao.getDataList(Wfi_ActInst.class, "PROINST_ID='"+proinst_id+"' ORDER BY ACTINST_START DESC");
						if(list_actinst!=null&&list_actinst.size()>0){
							actinst_id=list_actinst.get(0).getActinst_Id();
						}
					}
					if(!StringHelper.isEmpty(actinst_id)){
						String path = request.getRealPath("/");
						Map<String, String> xmlName =projectService.exportXML(xmbh, path, actinst_id);
						if(xmlName==null||xmlName.containsKey("error")){
							ifail++;
						}else{
							isuccess++;
						}
					}
				}
			}
		}
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("true");
		ms.setMsg("上报完成！上报成功"+isuccess+"个项目，上报失败"+ifail+"个项目！");
		return ms;
	}
	
	public ResultMessage branchDelete(String ids){
		ResultMessage msg = new ResultMessage();
		msg.setMsg("删除失败");
		msg.setSuccess("false");
		if (ids != null && ids != "") {
			String[] id = ids.split(",");
			for (String _id : id) {
				baseCommonDao.delete(BDCS_SJSB.class, _id);
			}
			msg.setMsg("删除成功");
			msg.setSuccess("true");
		}
		return msg;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public HashMap<String,String> Report1(String xmbh,HttpServletRequest request) {
		HashMap<String,String> m=new HashMap<String, String>();
		m.put("XMBH", xmbh);
		m.put("REPORTSTATUS", "-1");
		if(!StringHelper.isEmpty(xmbh)){
			BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
			if("1".equals(xmxx.getSFSB())||"2".equals(xmxx.getSFSB())){
				m.put("REPORTSTATUS", xmxx.getSFSB());
				return m;
			}
			String project_id=xmxx.getPROJECT_ID();
			String proinst_id="";
			String actinst_id="";
			if(!StringHelper.isEmpty(project_id)){
				List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+project_id+"'");
				if(list_proinst!=null&&list_proinst.size()>0){
					proinst_id=list_proinst.get(0).getProinst_Id();
				}
			}
			if(!StringHelper.isEmpty(proinst_id)){
				List<Wfi_ActInst> list_actinst=baseCommonDao.getDataList(Wfi_ActInst.class, "PROINST_ID='"+proinst_id+"' ORDER BY ACTINST_START DESC");
				if(list_actinst!=null&&list_actinst.size()>0){
					actinst_id=list_actinst.get(0).getActinst_Id();
				}
			}
			if(!StringHelper.isEmpty(actinst_id)){
//				String path = request.getRealPath("/");
				String path = request.getSession().getServletContext().getRealPath("\\");
				projectService.exportXML(xmbh, path, actinst_id);
				BDCS_XMXX xmxx1=Global.getXMXXbyXMBH(xmbh);
				m.put("REPORTSTATUS", xmxx1.getSFSB());
			}
		}
		return m;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetReportInfoList(HttpServletRequest request) {
		Message m=new Message();
		String value = request.getParameter("value");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String reportstatus = request.getParameter("reportstatus");
		String djlx = request.getParameter("djlx");
		
		String _pageIndex = request.getParameter("currentPageIndex");
		String _pageSize = request.getParameter("pageSize");
		int pageIndex=1;
		int pageSize=10;
		if(!StringHelper.isEmpty(_pageIndex)){
			pageIndex=StringHelper.getInt(_pageIndex);
		}
		if(!StringHelper.isEmpty(_pageSize)){
			pageSize=StringHelper.getInt(_pageSize);
		}
		StringBuilder builder_select=new StringBuilder();
		builder_select.append("SELECT XMXX.XMBH,XMXX.SFSB,XMXX.YWLSH,XMXX.XMMC,REPLACE(PROINST.PRODEF_NAME,',','-') PRODEF_NAME,XMXX.DJSJ,XMXX.SLRY ");
		StringBuilder builder=new StringBuilder();
		builder.append("FROM BDCK.BDCS_XMXX XMXX ");
		builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ");
		builder.append("ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
		builder.append("WHERE XMXX.SFDB='1' ");
		if(!StringHelper.isEmpty(djlx)){
			builder.append("AND XMXX.DJLX='"+djlx+"' ");
		}
		if(!StringHelper.isEmpty(value)){
			builder.append("AND (XMXX.XMMC LIKE '%"+value+"%' OR XMXX. YWLSH LIKE '%"+value+"%') ");
		}
		if(!StringHelper.isEmpty(startdate)){
			builder.append("AND XMXX.DJSJ>=TO_DATE('"+startdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
		}
		if(!StringHelper.isEmpty(enddate)){
			builder.append("AND XMXX.DJSJ<=TO_DATE('"+enddate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}
		if(!StringHelper.isEmpty(reportstatus)){
			if("-1".equals(reportstatus)){
				builder.append("AND (XMXX.SFSB='-1' OR XMXX.SFSB IS NULL)");
			}else{
				builder.append("AND XMXX.SFSB='"+reportstatus+"'");
			}
			
		}
		long total=baseCommonDao.getCountByFullSql(builder.toString());
		m.setTotal(total);
		List<Map> list_xmxx=new ArrayList<Map>();
		if(total>0){
			List<Map> list =baseCommonDao.getPageDataByFullSql(builder_select.toString()+builder.toString()+" ORDER BY SLSJ", pageIndex, pageSize);
			for(Map xmxx:list){
				String xmbh=StringHelper.formatObject(xmxx.get("XMBH"));
				int reporterror=0;
				int reportsuccess=0;
				int reportwait=0;
				List<BDCS_REPORTINFO> list_reportinfo=baseCommonDao.getDataList(BDCS_REPORTINFO.class, "XMBH='"+xmbh+"' AND YXBZ='1'");
				if(list_reportinfo!=null&&list_reportinfo.size()>0){
					for(BDCS_REPORTINFO reportinfo:list_reportinfo){
						if("1".equals(reportinfo.getSUCCESSFLAG())){
							reportsuccess++;
						}else if("2".equals(reportinfo.getSUCCESSFLAG())){
							reportwait++;
						}else if("0".equals(reportinfo.getSUCCESSFLAG())){
							reporterror++;
						}
					}
				}
				xmxx.put("REPORTERROR", reporterror);
				list_xmxx.add(xmxx);
				xmxx.put("REPORTSUCCESS", reportsuccess);
				xmxx.put("REPORTWAIT", reportwait);
			}
		}
		m.setRows(list_xmxx);
		m.setSuccess("success");
		return m;
	}
/**
 * 上报统计
 * @param request
 * @return Map<String,String>
 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,String> getTJSBQK(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		int suceessReportCount = 0, waitReportCount = 0, errorReportCount = 0, elseReportCount = 0;
		int suceessProjectCount = 0, waitProjectCount = 0, errorProjectCount = 0, elseProjectCount = 0;
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//select * from bdc_workflow.Wfi_ProInst where Proinst_start Between to_date('2013/01/01 10:57:17','yyyy-mm-dd  hh24:mi:ss') And to_date('2018/01/31 10:57:17','yyyy-mm-dd  hh24:mi:ss')
		StringBuilder builder = new StringBuilder();
		StringBuilder fullbuilder = new StringBuilder();
		fullbuilder.append(" SELECT PROINST_ID,FILE_NUMBER ");
		builder.append(" FROM BDC_WORKFLOW.WFI_PROINST PROINST WHERE ");
		if(!StringHelper.isEmpty(startdate)){
			builder.append(" PROINST.PROINST_START BETWEEN TO_DATE('"+startdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
		}
		if(!StringHelper.isEmpty(enddate)){
			builder.append(" AND TO_DATE('"+enddate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}
		fullbuilder.append(builder);
		long total=baseCommonDao.getCountByFullSql(builder.toString());
		List<Map> list_proinst=new ArrayList<Map>();
		if(total>0){
			Query query = baseCommonDao.getCurrentSession().createSQLQuery(fullbuilder.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		    list_proinst = query.list();
		    if(list_proinst!=null && list_proinst.size()>0){
		    	for(Map proinst : list_proinst ){
		    		String proinst_id = proinst.get("PROINST_ID").toString();
		    		List<BDCS_REPORTINFO> reportinfo = baseCommonDao.getDataList(BDCS_REPORTINFO.class, "PROINSTID='"+proinst_id+"' AND YXBZ='1'");
		    		if(reportinfo!=null && reportinfo.size()>0){
		    			for(BDCS_REPORTINFO info : reportinfo){
					  		String flag = info.getSUCCESSFLAG();
					  		if(flag!=null){
					  			if("0".equals(flag)){
						  			errorReportCount++;
								}else if("1".equals(flag)){
									suceessReportCount++;
								}else if("2".equals(flag)){
									waitReportCount++;
								}else{
									elseReportCount++;
								}
					  		}
					  	}
		    	    }
		    		String file_number = proinst.get("FILE_NUMBER").toString();//WFI_PROINST.file_number -> XMXX.PROJECT_ID
		    		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, "PROJECT_ID='"+file_number+"'");
		    		if(xmxx!=null && xmxx.size()>0){
		    			for(BDCS_XMXX xm : xmxx){
		    				String sfsb = xm.getSFSB();
		    				if(sfsb!=null){
		    					if("0".equals(sfsb)){
			    					errorProjectCount++;
			    				}else if("1".equals(sfsb)){
			    					suceessProjectCount++;
			    				}else if("2".equals(sfsb)){
			    					waitProjectCount++;
			    				}else{
			    					elseProjectCount++;
			    				}
		    				}
		    			}
		    		}
		        }
		    	// 各类报文总数
		    	map.put("errorReportCount", String.valueOf(errorReportCount));      // 上报失败的项目总数
		    	map.put("suceessReportCount", String.valueOf(suceessReportCount));  // 已经上报的项目总数
		    	map.put("waitReportCount", String.valueOf(waitReportCount));        // 等待上报的项目总数
		    	map.put("elseReportCount", String.valueOf(elseReportCount));        // 还未上报的项目总数
		    	// 各类项目总数
		    	map.put("errorProjectCount", String.valueOf(errorProjectCount));    // 上报失败的报文总数
		    	map.put("suceessProjectCount", String.valueOf(suceessProjectCount));// 已经上报的报文总数
		    	map.put("waitProjectCount", String.valueOf(waitProjectCount));      // 等待上报的报文总数
		    	map.put("elseProjectCount", String.valueOf(elseProjectCount));      // 还未上报的报文总数
		     }
		}
		return map;
	}
//		String prodef_id = request.getParameter("prodef_id");
//		List<Wfi_ProInst> wfd_prodef = baseCommonDao.getDataList(Wfi_ProInst.class, "Prodef_Id='"+prodef_id+"'");
//		if(wfd_prodef!=null && wfd_prodef.size()>0){
//			int count_error=0;    // 上报失败报文总数
//			int count_success=0;  // 上报成功报文总数
//		    int count_wait=0;     // 上报等待报文总数
//			for(Wfi_ProInst prodef : wfd_prodef){
//				String proinst_id = prodef.getProinst_Id();
//				List<BDCS_REPORTINFO> reportinfo = baseCommonDao.getDataList(BDCS_REPORTINFO.class, "proinstid='"+proinst_id+"'");
//				if(reportinfo!=null && reportinfo.size()>0){
//				  	for(BDCS_REPORTINFO info : reportinfo){
//				  		String flag = info.getSUCCESSFLAG();
//				  		if("0".equals(flag)){
//							count_error++;
//						}else if("1".equals(flag)){
//							count_success++;
//						}else if("2".equals(flag)){
//							count_wait++;
//						}
//				  	}
//				}
//		    }
//			map.put("error", String.valueOf(count_error));
//			map.put("success", String.valueOf(count_success));
//			map.put("wait", String.valueOf(count_wait));
//	   }
	
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> GetReportDetailList(HttpServletRequest request) {
		String xmbh = request.getParameter("xmbh");
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT DY.BDCDYH,DY.ZL,QL.BDCQZH,REPORT.REPORTCONTENT,");
		builder.append("REPORT.LOCALCHECKINFO,REPORT.RESPENSECONTENT,REPORT.ID,REPORT.SUCCESSFLAG FROM BDCK.BDCS_REPORTINFO REPORT ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON REPORT.QLID=QL.QLID ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=REPORT.DJDYID ");
		builder.append("LEFT JOIN ");
		builder.append("(SELECT BDCDYH,ZL,BDCDYID,'031' AS BDCDYLX FROM BDCK.BDCS_H_LS ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'032' AS BDCDYLX FROM BDCK.BDCS_H_LSY ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_LS ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_LS ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'05' AS BDCDYLX FROM BDCK.BDCS_SLLM_LS ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_LS ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'09' AS BDCDYLX FROM BDCK.BDCS_NYD_LS ");
		builder.append("UNION SELECT BDCDYH,ZL,BDCDYID,'10' AS BDCDYLX FROM BDCK.BDCS_GZW_LS) DY ");
		builder.append("ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ");
		builder.append("WHERE REPORT.YXBZ='1' AND REPORT.XMBH='");
		builder.append(xmbh);
		builder.append("'");
		List<Map> list=new ArrayList<Map>();
		
		List<Map> list_1=baseCommonDao.getDataListByFullSql(builder.toString());
		if(list_1!=null&&list_1.size()>0){
			for(Map m:list_1){
				String reportcontent=StringHelper.formatObject(m.get("REPORTCONTENT"));
				String localcheckinfo=StringHelper.formatObject(m.get("LOCALCHECKINFO"));
				String respensecontent=StringHelper.formatObject(m.get("RESPENSECONTENT"));
				if("1".equals(localcheckinfo)){
					localcheckinfo="校验通过";
					m.put("LOCALCHECKINFO", localcheckinfo);
				}
				String btn_reportcontent="0";
				if(!StringHelper.isEmpty(reportcontent)&&reportcontent.length()>20){
					btn_reportcontent="1";
				}
				String btn_localcheckinfo="0";
				if(!StringHelper.isEmpty(localcheckinfo)&&localcheckinfo.length()>20){
					btn_localcheckinfo="1";
				}
				String btn_respensecontent="0";
				if(!StringHelper.isEmpty(respensecontent)&&respensecontent.length()>20){
					btn_respensecontent="1";
				}
				m.put("REPORTCONTENT", StringHelper.formatObject(reportcontent));
				m.put("LOCALCHECKINFO", StringHelper.formatObject(localcheckinfo));
				m.put("RESPENSECONTENT", StringHelper.formatObject(respensecontent));
				m.put("BTN_REPORTCONTENT", btn_reportcontent);
				m.put("BTN_LOCALCHECKINFO", btn_localcheckinfo);
				m.put("BTN_RESPENSECONTENT", btn_respensecontent);
				list.add(m);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<String> GetAutoReportList(HttpServletRequest request) {
		
		List<String> listXmbh=new ArrayList<String>();
		String workflowcodes = request.getParameter("workflowcodes");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		StringBuilder builder_select=new StringBuilder();
		builder_select.append("SELECT XMBH FROM BDCK.BDCS_XMXX XMXX ");
		builder_select.append("WHERE SFDB='1' AND (XMXX.SFSB='-1' OR XMXX.SFSB IS NULL) ");
		if(!StringHelper.isEmpty(startdate)){
			builder_select.append("AND XMXX.DJSJ>=TO_DATE('"+startdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') ");
		}
		if(!StringHelper.isEmpty(enddate)){
			builder_select.append("AND XMXX.DJSJ<=TO_DATE('"+enddate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}
		if(!StringHelper.isEmpty(workflowcodes)){
			List<String> list_workflowcode=Arrays.asList(workflowcodes.split(";"));
			builder_select.append("AND EXISTS(SELECT 1 FROM BDC_WORKFLOW.WFI_PROINST PROINST ");
			builder_select.append("WHERE PROINST.FILE_NUMBER=XMXX.PROJECT_ID AND PROINST.PROINST_CODE IN ('");
			builder_select.append(StringHelper.formatList(list_workflowcode, "','"));
			builder_select.append("'))");
		}
		
		if(!StringHelper.isEmpty(enddate)){
			builder_select.append(" ORDER BY DJSJ");
		}
		List<Map> list =baseCommonDao.getDataListByFullSql(builder_select.toString());
		for(Map xmxx:list){
			String xmbh=StringHelper.formatObject(xmxx.get("XMBH"));
			listXmbh.add(xmbh);
		}
		return listXmbh;
	}
}
