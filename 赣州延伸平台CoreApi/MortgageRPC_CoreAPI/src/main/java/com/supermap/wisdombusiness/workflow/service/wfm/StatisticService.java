package com.supermap.wisdombusiness.workflow.service.wfm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;

@Service("statisticService")
public class StatisticService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProInst _SmProInst;
	
	/**
	 * 通过员工ID 获取员工的办件数量
	 * @param StaffID
	 */
	private void GetProjectCountByStaffID(String StaffID){
		
	}
    /**
     * 获取全年办件数据
     * @param Year 年份
     * @param staff_id 员工ID 可以是多个员工，“，”分割
     * @return
     */
	public List<Map> GetYearProject(int Year,String staff_id ) {
	    StringBuilder SQLBuilder=new StringBuilder();
	    StringBuilder tmpTable=_SmProInst.ConactDistinctCondition("actinst_start" ,"asc");
	    SQLBuilder.append("select to_char(actinst_end,'MM')  as month  ,sum(1) as count from ");
	    SQLBuilder.append(tmpTable.toString());
	      //办结项目
	    SQLBuilder.append("  where actinst_status=3 and actinst_end is not null ");
	    //员工
	    SQLBuilder.append(" and staff_id in ( ");
	    SQLBuilder.append(staff_id+") ");
	    //年份
	    SQLBuilder.append(" and to_char(actinst_end,'yyyy')='"+Year+"'");
	    //分组
	    SQLBuilder.append(" group by to_char(actinst_end,'MM') order by month");
 
	    List<Map> list=	commonDao.getDataListByFullSql(SQLBuilder.toString());
	    return list;
	}
	/**
	 * 获取员工超期案卷
	 * @param Year 年
	 * @param month 月
	 * @param staff_id 员工ID
	 * @return
	 */
	public Map GetOverDueProject(int Year,String staff_id){
		Map Result=new HashMap();
		   StringBuilder SQLBuilder=new StringBuilder();
		    StringBuilder tmpTable=_SmProInst.ConactDistinctCondition("actinst_start" ,"asc");
		    SQLBuilder.append("select  to_char(actinst_end,'MM')  as month  ,sum(1) as count from ");
		    SQLBuilder.append(tmpTable.toString());
		      //办结项目
		    SQLBuilder.append("  where actinst_status=3 and actinst_end is not null ");
		    //员工
		    SQLBuilder.append(" and staff_id in ( ");
		    SQLBuilder.append(staff_id+") ");
		    //年份
		    SQLBuilder.append(" and to_char(actinst_end,'yyyy')='"+Year+"'");
		    
		    //超期
		    SQLBuilder.append("  and actinst_end>=actinst_willfinish");
		    //分组
		    SQLBuilder.append(" group by  to_char(actinst_end,'MM') order by month");
	        
		    List<Map> list=	commonDao.getDataListByFullSql(SQLBuilder.toString());
		    Result.put("overdue", list);
		    
		    StringBuilder OverDueSQLBuilder=new StringBuilder();
		    OverDueSQLBuilder.append("select  to_char(actinst_end,'MM')  as month  ,sum(1) as count from ");
		    OverDueSQLBuilder.append(tmpTable.toString());
		      //办结项目
		    OverDueSQLBuilder.append("  where actinst_status=3 and actinst_end is not null ");
		    //员工
		    OverDueSQLBuilder.append(" and staff_id in ( ");
		    OverDueSQLBuilder.append(staff_id+") ");
		    //年份
		    OverDueSQLBuilder.append(" and to_char(actinst_end,'yyyy')='"+Year+"'");
		    
		    //超期
		    OverDueSQLBuilder.append("  and actinst_end<actinst_willfinish");
		    //分组
		    OverDueSQLBuilder.append(" group by to_char(actinst_end,'MM') order by month");
	        
		    List<Map> overDuelist=	commonDao.getDataListByFullSql(OverDueSQLBuilder.toString());
		    Result.put("normal",overDuelist);
		    return Result;
	}
	
	private void  GetMonthProjectCount(String StaffID,String Mouth){
		StringBuilder sb=new StringBuilder();
		sb.append(" Staff_id='");
		sb.append(StaffID);
		sb.append("'");
		sb.append(" and actinst_status=3 ");
		
		StringBuilder tmpTable=_SmProInst.ConactDistinctCondition("actinst_start" ,"asc");
		
		long tatalCount = commonDao.getCountByFullSql(" from " + tmpTable.toString() + " where " + sb.toString());
	}
	
	/**
	 * 获取员工各种类型的件统计比例，已办，待办,在办，驳回,办结
	 * @param staff_id
	 * @param start_time
	 * @param end_time
	 */
	public void GetProjectStatus(String staff_id,String start_time,String end_time){
		
	}
	
	
	public Message GetKSYWTJ(String tjsjks, String tjsjjz, String deptid) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builder_start = new StringBuilder();//流程开始时间
			StringBuilder builder_end = new StringBuilder();//流程开始时间
			StringBuilder con_mc= new StringBuilder();//科室名称
			if(!"".equals(deptid) && deptid != null){
				con_mc.append(" AND B.ID ='").append(deptid).append("' ");
			}
			if(tjsjks != null || !"".equals(tjsjks)){
				builder_end.append(" AND V.ACTINST_END BETWEEN TO_DATE(");
				builder_end.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
			}
			if("0".equals(ConfigHelper.getNameByValue("STATISTICS"))){
				build.append(" SELECT V.STAFF_NAME,COUNT( V.PROINST_ID) AS YBJS  FROM (");
				build.append(" SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROINST_ID, B.ACTDEF_ID ORDER BY B.ACTINST_START ASC) RN,B.* FROM BDC_WORKFLOW.WFI_ACTINST B)");
			}else{
				build.append(" SELECT V.STAFF_NAME,COUNT( DY.ID) AS YBJS  FROM (");
				build.append(" SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROINST_ID, B.STAFF_ID ORDER BY B.ACTINST_START ASC) RN,B.* FROM BDC_WORKFLOW.V_PROJECTLIST B)");
			}
			build.append(" WHERE RN = 1 AND (ACTINST_STATUS = 3 OR ACTINST_STATUS = 0) ) V LEFT JOIN SMWB_FRAMEWORK.T_USER A ON V.STAFF_ID=A.ID LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT B ON A.DEPARTMENTID=B.ID");
			if("1".equals(ConfigHelper.getNameByValue("STATISTICS"))){
				build.append(" LEFT JOIN BDCK.BDCS_XMXX XX ON XX.PROJECT_ID=V.FILE_NUMBER LEFT JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH=XX.XMBH ");
			}
			build.append(" WHERE (V.ACTINST_STATUS = 3 OR ( V.ACTINST_STATUS=0 AND V.ACTINST_STATUS=0 )) ").append(con_mc).append(builder_end);	
			build.append(" GROUP BY STAFF_NAME");			
			@SuppressWarnings("rawtypes")
			List<Map> maps = commonDao.getDataListByFullSql(build.toString());
			Map countMap = new HashMap();
			int count = 0;
			if(!StringHelper.isEmpty(maps) && maps.size()>0){
				for(Map map :maps){
					if (!StringHelper.isEmpty(map.get("YBJS"))) {
						 count += Integer.parseInt(map.get("YBJS").toString());
					}
				}
			}
			countMap.put("STAFF_NAME", "总计");
			countMap.put("YBJS",count);
			maps.add(countMap);
			m.setRows(maps);
			m.setTotal(maps.size());
		
			}catch(Exception e){
			m=null;
		}
		return m;
	}

	
	public Message GetDJZXYWTJ(String tjsjks, String tjsjjz) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builder_end = new StringBuilder();//流程开始时间
			if(tjsjks != null || !"".equals(tjsjks)){
				builder_end.append(" AND V.ACTINST_END BETWEEN TO_DATE(");
				builder_end.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
			}
			
			build.append("SELECT B.DEPARTMENTNAME,COUNT( V.ACTINST_ID) AS YBJS ");
			build.append(" FROM ( SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROINST_ID, B.ACTDEF_ID ORDER BY B.ACTINST_START ASC) RN,B.* ");
			build.append(" FROM BDC_WORKFLOW.WFI_ACTINST B) WHERE RN = 1) V "
					+ " LEFT JOIN SMWB_FRAMEWORK.T_USER A ON V.STAFF_ID=A.ID "
					+ " LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT B ON A.DEPARTMENTID=B.ID "
					+ " WHERE (V.ACTINST_STATUS = 3 OR V.ACTINST_STATUS=0 ) ");
			build.append(builder_end);
			build.append(" GROUP BY B.DEPARTMENTNAME   ");
			List<Map> maps = commonDao.getDataListByFullSql(build.toString());
			Map countMap = new HashMap();
			int count = 0;
			if(!StringHelper.isEmpty(maps) && maps.size()>0){
				for(Map map :maps){
					if (!StringHelper.isEmpty(map.get("YBJS"))) {
						 count += Integer.parseInt(map.get("YBJS").toString());
					}
				}
			}
			countMap.put("DEPARTMENTNAME", "总计");
			countMap.put("YBJS",count);
			maps.add(countMap);
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	
	
	public List<Map> getAcceptCount(String qssj, String zzsj, String pronames) {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) as count , areacode as name from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("WFI_PROINST WHERE　PROINST_START BETWEEN TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
		if(!StringHelper.isEmpty(pronames)){
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" and prodef_name='"+pronames+"'");
			}else{
				
				sb.append(" and prodef_name like ('"+pronames+"%')");
			}
		}
		sb.append(" group by areacode ");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
	
	public List<Map> getEndCount(String qssj, String zzsj, String pronames) {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) as count, areacode as name from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("WFI_PROINST WHERE　PROINST_END BETWEEN TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
		if(!StringHelper.isEmpty(pronames)){
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" and prodef_name='"+pronames+"'");
			}else{
				
				sb.append(" and prodef_name like ('"+pronames+"%')");
			}
		}
		sb.append(" group by areacode ");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
	
	public List<Map> getCancleCount(String qssj, String zzsj, String pronames) {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) as count, areacode as name from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_proinst where proinst_status = '0'");
		sb.append("and proinst_id in (select proinst_id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_actinst where actdef_id in (select actdef_id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append( "wfd_actdef where isreturnact = '1') ");
		sb.append("and ACTINST_END BETWEEN TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss'))");
		if(!StringHelper.isEmpty(pronames)){
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" and prodef_name='"+pronames+"'");
			}else{
				
				sb.append(" and prodef_name like ('"+pronames+"%')");
			}
		}
		sb.append(" group by areacode ");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
	
	public List<Map> getActendCount(String tjlx, String depid, String qssj,
			String zzsj, String pronames) {
		boolean flag = "2".equals(tjlx)&&!StringHelper.isEmpty(depid);
		StringBuilder sb = new StringBuilder();
		sb.append(flag ? "select staff_name as name," : " select departmentname as name,");
		sb.append("count(1)  as count from (select distinct  actdef_id, a.proinst_id,");
		sb.append(flag ? " a.staff_id ,a.staff_name from " : " d.departmentname,d.id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_actinst a  left join t_user u on a.staff_id = u.id ");
		sb.append(flag ? " " : " left join t_department d on u.departmentid = d.id ");
		if(!StringHelper.isEmpty(pronames)){
			sb.append(" left join "+Common.WORKFLOWDB+"wfi_proinst pr on a.proinst_id = pr.proinst_id ");
			
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" where prodef_name='"+pronames+"' and ");
			}else{
				
				sb.append(" where prodef_name like ('"+pronames+"%') and ");
			
			}
		}else{
			sb.append(" where ");
		}
		sb.append(" (a.actinst_status = 0 or a.actinst_status = 3) and a.actinst_end between TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and a.staff_name not like '撤回人员%' ");
		sb.append(flag ? "  and  u.departmentid = '"+depid+"') group by staff_name,staff_id " :
						 ")group by departmentname,id");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
	
	public List<Map> getActouttimeCount(String tjlx, String depid, String qssj,
			String zzsj, String pronames) {
		boolean flag = "2".equals(tjlx)&&!StringHelper.isEmpty(depid);
		StringBuilder sb = new StringBuilder();
		sb.append(flag ? "select staff_name as name," : " select departmentname as name,");
		sb.append("count(1)  as count from (select distinct  actdef_id, a.proinst_id,");
		sb.append(flag ? " a.staff_id ,a.staff_name from " : " d.departmentname,d.id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_actinst a  left join t_user u on a.staff_id = u.id ");
		sb.append(flag ? " " : " left join t_department d on u.departmentid = d.id ");
		
		if(!StringHelper.isEmpty(pronames)){
			sb.append(" left join "+Common.WORKFLOWDB+"wfi_proinst pr on a.proinst_id = pr.proinst_id ");
			
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" where prodef_name='"+pronames+"' and ");
			}else{
				
				sb.append(" where prodef_name like ('"+pronames+"%') and ");
			
			}
		}else{
			sb.append(" where ");
		}
		
		sb.append(" (a.actinst_status = 0 or a.actinst_status = 3) and a.actinst_end between TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and a.actinst_end > a.actinst_willfinish ");
		sb.append(flag ? "  and  u.departmentid = '"+depid+"') group by staff_name,staff_id " :
						 ")group by departmentname,id");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
	
	public List<Map> getActpassbackCount(String tjlx, String depid, String qssj,
			String zzsj, String pronames) {
		boolean flag = "2".equals(tjlx)&&!StringHelper.isEmpty(depid);
		StringBuilder sb = new StringBuilder();
		sb.append(flag ? "select staff_name as name," : " select departmentname as name,");
		sb.append("count(1) as count from (select distinct  actdef_id, a.proinst_id,");
		sb.append(flag ? " a.staff_id ,a.staff_name from " : " d.departmentname,d.id from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfi_actinst a  left join t_user u on a.staff_id = u.id ");
		sb.append(flag ? " " : " left join t_department d on u.departmentid = d.id ");
		
		if(!StringHelper.isEmpty(pronames)){
			sb.append(" left join "+Common.WORKFLOWDB+"wfi_proinst pr on a.proinst_id = pr.proinst_id ");
			
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" where prodef_name='"+pronames+"' and ");
			}else{
				
				sb.append(" where prodef_name like ('"+pronames+"%') and ");
			
			}
		}else{
			sb.append(" where ");
		}
		
		sb.append(" (a.actinst_status = 0 or a.actinst_status = 3) and a.actinst_end between TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and a.operation_type='9' ");
		sb.append(flag ? "  and  u.departmentid = '"+depid+"') group by staff_name,staff_id " :
						 ")group by departmentname,id");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
	
	public List<Map> getActavgtimeCount(String tjlx, String depid, String qssj,
			String zzsj, String pronames) {
		boolean flag = "2".equals(tjlx)&&!StringHelper.isEmpty(depid);
		StringBuilder sb = new StringBuilder();
		sb.append(flag ? "select staff_name as name" : "select d.departmentname as name");
		sb.append(",avg(actinst_end - actinst_start) as count from (");
		sb.append("select a.staff_id,a.staff_name,a.actinst_end,a.actinst_start,");
		sb.append(" row_number() over（partition by a.proinst_id,a.actdef_id order by a.actinst_end desc）as rn from ");
		sb.append(Common.WORKFLOWDB + "wfi_actinst a ");
		
		if(!StringHelper.isEmpty(pronames)){
			sb.append(" left join "+Common.WORKFLOWDB+"wfi_proinst pr on a.proinst_id = pr.proinst_id ");
			
			long count = commonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST where prodef_name = '"+pronames+"'");
			if(count>0){
				sb.append(" where prodef_name='"+pronames+"' and ");
			}else{
				
				sb.append(" where prodef_name like ('"+pronames+"%') and ");
			
			}
		}else{
			sb.append(" where ");
		}
		
		sb.append(" (a.actinst_status = 0 or a.actinst_status = 3) ");
		sb.append(" and a.actinst_end between TO_DATE(");
		sb.append("'"+qssj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
		sb.append("'"+zzsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" ) left join smwb_framework.t_user u on staff_id = u.id ");
		sb.append(flag ? " " : "left join smwb_framework.t_department d on u.departmentid = d.id ");
		sb.append(" where rn < 2 ");
		sb.append(flag ? "  and u.departmentid='" + depid + "' group by staff_name,staff_id " : " group by d.id,d.departmentname ");
		return	commonDao.getDataListByFullSql(sb.toString());
	}
	
}
