package com.supermap.realestate_gx.registration.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.StringHelper;
/**
 * 广西统计
 */
import com.supermap.realestate_gx.registration.service.GX_GZTJService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.realestate.registration.util.ConstValue.DJLX;

@Service("gx_gztjService")
public class GX_GZTJServiceImpl implements GX_GZTJService {
	@Autowired
	private CommonDao baseCommonDao;

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	public Message GetDJZXYWTJ(String tjsjks, String tjsjjz) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			//StringBuilder builder_start = new StringBuilder();//流程开始时间
			StringBuilder builder_end = new StringBuilder();//流程开始时间
			if(tjsjks != null || !"".equals(tjsjks)){
				builder_end.append(" AND ACTINST_END >= TO_DATE(");
				builder_end.append("'"+tjsjks+" " +"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND ACTINST_END <= TO_DATE( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
			}
		
			

			String sql = "SELECT * FROM SMWB_FRAMEWORK.T_DEPARTMENT";
			List<Map> deptList = baseCommonDao.getDataListByFullSql(sql);
			List<Map> list = new ArrayList<Map>();

			if(deptList.size() > 0){
				for(int i = 0;i < deptList.size();i++){
					Map map = new HashMap();
					sql = "SELECT count(*) FROM  ( SELECT * FROM (SELECT ROW_NUMBER() "
							+ "OVER(PARTITION BY file_number ORDER BY B.ACTINST_START ASC)"
							+ " RN,B.*  FROM BDC_WORKFLOW.V_PROJECTLIST B LEFT JOIN "
							+ " SMWB_FRAMEWORK.T_USER A  ON b.STAFF_ID=A.ID LEFT JOIN "
							+ " SMWB_FRAMEWORK.T_DEPARTMENT D ON A.DEPARTMENTID = D.ID "
							+ " WHERE D.ID = '"+deptList.get(i).get("ID")+"' "
							+ " AND A.STATUS = 'NORMAL'"
							+ " AND ACTINST_STATUS in(3) "+builder_end.toString()+") WHERE RN = 1)";
					long count = baseCommonDao.getCountByCFullSql(sql);
					map.put("DEPARTMENTNAME", deptList.get(i).get("DEPARTMENTNAME"));
					map.put("ID", deptList.get(i).get("ID"));
					map.put("YBJS", count);
					list.add(map);
				}
			}
 
			m.setRows(list);
			m.setTotal(list.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	public Message GetKSYWTJ(String tjsjks, String tjsjjz, String deptid, String username) {
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
				
				builder_end.append(" AND ACTINST_START >= TO_DATE(");
				builder_end.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND ACTINST_START <= TO_DATE( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss'))");
			}


			//查出部门下的人员
			String sql = "select * from SMWB_FRAMEWORK.T_USER u where u.departmentid = '"+deptid+"'";
			if (!StringUtils.isEmpty(username)) {
				sql += " and  id = '" + username + "'";
			}
			List<Map> userList = baseCommonDao.getDataListByFullSql(sql);

			List<Map> list = new ArrayList<Map>();
			if(userList.size() > 0){
				for(int i = 0;i < userList.size();i++){
					Map map = new HashMap();
					sql = " select count(1)  from (select  row_number() "
							+ "over (partition by file_number order by actinst_start desc) "
							+ "rn,file_number, project_name, PROINST_START,proinst_status,Staff_Name "
							+ "from BDC_WORKFLOW.V_PROJECTLIST_2 where 1>0"
							+ " and ACTINST_STATUS = 3 "
							+ " AND staff_name like '%"+userList.get(i).get("USERNAME")+"%' "+builder_end.toString()+" WHERE rn=1";
					long count = baseCommonDao.getCountByCFullSql(sql);
					map.put("STAFF_ID", userList.get(i).get("ID"));
					map.put("STAFF_NAME", userList.get(i).get("USERNAME"));
					map.put("YBJS", count);
					list.add(map);
				}
			} 

			m.setRows(list);
			m.setTotal(list.size());

		}catch(Exception e){
			m=null;
		}
		return m;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Map> GetDeptStaffname(String deptid) {
		StringBuilder staffnames = new StringBuilder();
		List<Map> maps = null;
		try{
			staffnames.append(" SELECT T.ID,T.USERNAME AS TEXT FROM SMWB_FRAMEWORK.T_DEPARTMENT A LEFT JOIN SMWB_FRAMEWORK.T_USER T ON T.DEPARTMENTID=A.ID WHERE "
					+ " A.ID='"+deptid+"' ");
			maps = baseCommonDao.getDataListByFullSql(staffnames.toString());
		}
		catch(Exception e){
			maps = null;
		}
		return maps;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	public Message GetBJLTJ(Map<String, String> conditionParameter) {
		StringBuilder fullsql = new StringBuilder();
		StringBuilder withsql = new StringBuilder();
		StringBuilder wheresql = new StringBuilder();
		Message m = new Message();
		
		String type = conditionParameter.get("type");
		String startDate = conditionParameter.get("startDate");
		String endDate = conditionParameter.get("endDate");
		String deptid = conditionParameter.get("deptid");
		String staffid = conditionParameter.get("staffid");
		
		try{
			if (!StringHelper.isEmpty(startDate)) {
				wheresql.append(" AND ACTINST_START >= TO_DATE('"+startDate+"','YYYY-MM-DD')");
			}
			if (!StringHelper.isEmpty(endDate)) {
				wheresql.append(" AND ACTINST_START <= TO_DATE('"+endDate+"','YYYY-MM-DD')");
			}
			String sql = "";
			List<Map> list = new ArrayList<Map>();
			if ("KS".equals(type)) {//科室
				sql = "SELECT * FROM SMWB_FRAMEWORK.T_DEPARTMENT";
				List<Map> ListMap = baseCommonDao.getDataListByFullSql(sql);
				if(ListMap.size() > 0){
					for(int i = 0;i < ListMap.size();i++){
						Map map = new HashMap();
					sql = "SELECT COUNT(1)  FROM ( "
							+"SELECT  ROW_NUMBER() OVER (PARTITION BY FILE_NUMBER ORDER BY ACTINST_START DESC) RN, "
							+"B.*  FROM BDC_WORKFLOW.V_PROJECTLIST B LEFT JOIN "
							+"SMWB_FRAMEWORK.T_USER A  ON b.STAFF_ID=A.ID LEFT JOIN "
							+"SMWB_FRAMEWORK.T_DEPARTMENT D ON A.DEPARTMENTID = D.ID "
							+"WHERE (ACTINST_STATUS = 3 OR (PROINST_STATUS = 0 AND ACTINST_STATUS = 0)) "
							+"AND D.ID = '"+ListMap.get(i).get("ID")+"' "
							+"AND A.STATUS = 'NORMAL' "
							+wheresql.toString()
							+") WHERE RN=1";
						long count = baseCommonDao.getCountByCFullSql(sql);
						// 在办件数量统计
						String sql_zb = "SELECT COUNT(1)  FROM ( "
								+"SELECT  ROW_NUMBER() OVER (PARTITION BY FILE_NUMBER ORDER BY ACTINST_START DESC) RN, "
								+"B.*  FROM BDC_WORKFLOW.V_PROJECTLIST B LEFT JOIN "
								+"SMWB_FRAMEWORK.T_USER A  ON b.STAFF_ID=A.ID LEFT JOIN "
								+"SMWB_FRAMEWORK.T_DEPARTMENT D ON A.DEPARTMENTID = D.ID "
								+"WHERE ACTINST_STATUS = 2  "
								+"AND D.ID = '"+ListMap.get(i).get("ID")+"' "
								+"AND A.STATUS = 'NORMAL' "
								+wheresql.toString()
								+") WHERE RN=1";
						long count_zb = baseCommonDao.getCountByCFullSql(sql_zb);
						map.put("ID", ListMap.get(i).get("ID"));
						map.put("DEPARTMENTNAME", ListMap.get(i).get("DEPARTMENTNAME"));
						map.put("YBJS", count);
						map.put("ZBJS", count_zb);
						list.add(map);
					}
				}
			} else {//科室人员
				//查出部门下的人员
				sql = "SELECT * FROM SMWB_FRAMEWORK.T_USER U WHERE U.DEPARTMENTID = '"+deptid+"'";
				if (!StringUtils.isEmpty(staffid)) {
					sql += " AND ID = '" + staffid + "'";
				}
				List<Map> ListMap = baseCommonDao.getDataListByFullSql(sql);
				if(ListMap.size() > 0){
					for(int i = 0;i < ListMap.size();i++){
						Map map = new HashMap();
					sql = "SELECT COUNT(1)  FROM (SELECT  ROW_NUMBER()"
							+ " OVER (PARTITION BY FILE_NUMBER ORDER BY ACTINST_START DESC)"
							+ " RN,FILE_NUMBER, PROJECT_NAME, PROINST_START,PROINST_STATUS,STAFF_NAME,STAFF_ID"
							+ " FROM BDC_WORKFLOW.V_PROJECTLIST WHERE 1>0"
							+ " AND ACTINST_STATUS = 3"
							+ " AND STAFF_ID ='"+ListMap.get(i).get("ID")+"' "
							+wheresql.toString()+") WHERE RN=1";
						long count = baseCommonDao.getCountByCFullSql(sql);
						// 在办件数量统计
						String sql_zb = "SELECT COUNT(1)  FROM (SELECT  ROW_NUMBER() "
								+ " OVER (PARTITION BY FILE_NUMBER ORDER BY ACTINST_START DESC) "
								+ " RN,FILE_NUMBER, PROJECT_NAME, PROINST_START,PROINST_STATUS,STAFF_NAME,STAFF_ID "
								+ " FROM BDC_WORKFLOW.V_PROJECTLIST WHERE 1>0 "
								+ " AND ACTINST_STATUS = 2 "
								+ " AND STAFF_ID ='"+ListMap.get(i).get("ID")+"' "
								+wheresql.toString()+") WHERE RN=1";
						long count_zb = baseCommonDao.getCountByCFullSql(sql_zb);
						map.put("STAFF_ID", ListMap.get(i).get("ID"));
						map.put("STAFF_NAME", ListMap.get(i).get("USERNAME"));
						map.put("YBJS", count);
						map.put("ZBJS", count_zb);
						list.add(map);
					}
				} 
			}
			m.setRows(list);
			m.setTotal(list.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	
	/**
	  * 柳州月业务办理量统计获取数据
	  * 
	  * @return
	  * @author hpf
	  * @date 20180328
	  */
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		public Message getDataYYWBLLTJ(Map<String, Object> mapCondition){
			Message msg = new Message();
			List<Map> results = new ArrayList<Map>();
			String _qssj = StringHelper.formatObject(mapCondition.get("QSSJ"));//开始时间
			String _jssj = StringHelper.formatObject(mapCondition.get("JSSJ"));//结束时间	
			long sllzs = 0;//受理量总数
			long slldyzs = 0;//受理抵押量总数
			long dblzs = 0;//登簿量总数
			long dbldyzs = 0;//登簿量抵押总数
			long szlzs = 0;//缮证量总数
			long szldyzs = 0;//缮证量抵押总数
			long fzlzs = 0;//发证量证书总数
			long fzldyzs = 0;//发证量证明总数
			int xh = 1;//序号
			String djlx = "";
			for(int i=1 ; i<=17 ; i++){
				String qllx = "";
				if(i==1)
					djlx = "100";
				if(i==2)
					qllx ="23";
				//根据序号获取djlx，前面11次循环奇数才获取，后面12-14偶数才获取
				if(i!=1 && i!=2 && i<=11 && i%2!=0){
					xh++;	
					int intdjlx = xh*100;	
					djlx = Integer.toString(intdjlx);
				}else if( i>=12 && i<=14 && i%2==0){
					xh++;	
					int intdjlx = xh*100;
					djlx = Integer.toString(intdjlx);
				}else if(i==15){
					djlx="900";
				}
				//获取qllx，前面11次循环偶数才获取，后面12-13奇数才获取
				if(i>=4  && i<=11 && i%2==0){
					qllx ="23";
				}else if( (i==12 || i==13) && i%2!=0){
					qllx ="23";
				}
				//先查询变更，再查转移
				if(i==3 || i==4)
					djlx = "300";
				if(i==5 || i==6)
					djlx = "200";
				if(i>15){
					djlx="";
				}
				Map row = this.getRowDataYYWBLLTJ(djlx, qllx, _qssj, _jssj, null);
				if(row != null && !row.isEmpty()){
					//统计总数
					if(!StringHelper.isEmpty(row.get("SLL"))){//受理量
						if("23".equals(qllx))
							slldyzs += Long.valueOf(row.get("SLL").toString());
						else
							sllzs += Long.valueOf(row.get("SLL").toString());
					}
					if(!StringHelper.isEmpty(row.get("DBL"))){//登簿量
						if("23".equals(qllx))
							dbldyzs += Long.valueOf(row.get("DBL").toString());
						else
							dblzs += Long.valueOf(row.get("DBL").toString());
					}		
					if(!StringHelper.isEmpty(row.get("ZS_SZL")))//证书缮证量
						szlzs += Long.valueOf(row.get("ZS_SZL").toString());
					if(!StringHelper.isEmpty(row.get("ZM_SZL")))//证明缮证量
						szldyzs += Long.valueOf(row.get("ZM_SZL").toString());
					if(!StringHelper.isEmpty(row.get("ZS_FZL")))//证书发证量
						fzlzs += Long.valueOf(row.get("ZS_FZL").toString());
					if(!StringHelper.isEmpty(row.get("ZM_FZL")))//证明发证量
						fzldyzs += Long.valueOf(row.get("ZM_FZL").toString());
			
				}
				if(i==16){//总计数量
					xh++;
					row.put("DJLX", "总计");
					row.put("SLL", sllzs );
					row.put("DBL", dblzs);
					row.put("ZS_SZL", szlzs);
					row.put("ZM_SZL", szldyzs);
					row.put("ZS_FZL", fzlzs);
					row.put("ZM_FZL", fzldyzs);
				}
				if(i==17){//总计上述各类抵押权登记
					xh++;
					row.put("DJLX", "总计上述各类抵押权登记");
					row.put("SLL", slldyzs );
					row.put("DBL", dbldyzs);
					row.put("ZS_SZL", "0");
					row.put("ZM_SZL", szldyzs);
					row.put("ZS_FZL", "0");
					row.put("ZM_FZL", fzldyzs);
				}
				if(i==1){//查询时间
					row.put("QSSJ", _qssj);
					row.put("JSSJ", _jssj);
				}
				row.put("XH", xh);//序号
				results.add(row);
			}
			msg.setRows(results);
			msg.setTotal(results.size());
			
			return msg;
		}
		/**
		 * 
		 * @param djlx 登记类型
		 * @param qllx 权利类型
		 * @param qssj 开始时间
		 * @param jssj 结束时间
		 * @param assign 指定只查某个类型的数量,1查受理量\2登簿量\3缮证量\4发证量,为空默认查全部。
		 * 
		 * @return Map<String, Object>
		 */
		private Map<String, Object> getRowDataYYWBLLTJ(String djlx, String qllx, String qssj, String jssj, 
				String assign){
			
			Map<String, Object> row = new HashMap<String, Object>();
			if(StringHelper.isEmpty(djlx))
				return row;
			
			StringBuilder whereCondition = new StringBuilder();
			whereCondition.append(" xmxx.djlx='").append(djlx).append("' and")
								.append(" to_char(xmxx.slsj,'yyyy-mm-dd') between '").append(qssj).append("' and '")
								.append(jssj).append("'");
			String strqllx = "";
			if(!StringHelper.isEmpty(qllx)){
					strqllx = " and xmxx.qllx in('" + qllx + "')";
					whereCondition.append(strqllx);
			}
			String strdjlxname = DJLX.initFrom(djlx)==null?"" : DJLX.initFrom(djlx).Name;	
			
			//按单元统计，受理量
			long sllcount = 0;
			if(StringHelper.isEmpty(assign) || "1".equals(assign)){
				StringBuilder derSqlSLL = new StringBuilder();
				derSqlSLL.append("from bdck.bdcs_xmxx xmxx")
							  .append(" left join bdck.bdcs_djdy_gz djdy on xmxx.xmbh=djdy.xmbh")
							  .append(" left join bdck.bdcs_ql_gz ql on xmxx.xmbh=ql.xmbh")
							  .append(" where 1=1");
				if("300".equals(djlx)){
					derSqlSLL.insert(derSqlSLL.length()-10, " left join bdc_workflow.wfi_proinst pt on xmxx.project_id=pt.file_number");
					derSqlSLL.append(" and pt.prodef_name not like '%特殊变更%'");//变更登记排除特殊变更
				}
				String strSqlSLL = "";
				if("700".equals(djlx)&&"23".equals(qllx)){//因预告+预抵押合并流程的qllx不是23,查抵押的时候需多or该合并流程。
							String _sqlSLL1 = derSqlSLL.toString().replace("1=1", "(1=1") + " and " + whereCondition.toString() + ")";
							String _sqlSLL2 = " or(ql.qllx ='23' and ql.djlx='700' and to_char(xmxx.slsj,'yyyy-mm-dd') between '"
																							+ qssj + "' and '"+jssj+"')";
							strSqlSLL = _sqlSLL1 + _sqlSLL2;
				}else{
							strSqlSLL = derSqlSLL.toString()+ " and " + whereCondition.toString();
				}
				
					sllcount = baseCommonDao.getCountByFullSql(strSqlSLL);
			}
					
			//按单元统计登簿量sql
			long dblcount = 0;
			long dbldy700 = 0;
			if(StringHelper.isEmpty(assign) || "2".equals(assign)){
				String whereConditionDBL = whereCondition.toString().replace("xmxx.slsj", "ql.djsj");
				StringBuilder derSqlDBL = new StringBuilder();
				derSqlDBL.append("from bdck.bdcs_xmxx xmxx")
							   .append(" left join bdck.bdcs_ql_gz ql on xmxx.xmbh=ql.xmbh")
							   .append(" where ql.qlid is not null and xmxx.sfdb='1'");
				if("300".equals(djlx)){
					derSqlDBL.insert(derSqlDBL.length()-44, " left join bdc_workflow.wfi_proinst pt on xmxx.project_id=pt.file_number");
					derSqlDBL.append(" and pt.prodef_name not like '%特殊变更%'");//变更登记排除特殊变更
				}else if("400".equals(djlx)){
					derSqlDBL.insert(derSqlDBL.length()-44, " left join bdck.bdcs_fsql_gz fsql on fsql.qlid=ql.qlid");
					whereConditionDBL = whereCondition.toString().replace("xmxx.slsj", "fsql.zxsj");
				}else if("700".equals(djlx)){
					derSqlDBL.insert(0, "select count(distinct ql.xmbh) ");
					derSqlDBL.insert(derSqlDBL.length()-44, " left join bdck.bdcs_zs_gz zs on zs.xmbh=xmxx.xmbh");
					derSqlDBL.append(" and  zs.zsbh is not null");
					if("23".equals(qllx)){//因预告+预抵押合并流程的qllx不是23,查抵押的时候需多or该合并流程。
							String strSqlDBL = derSqlDBL.toString()  + " and " +
									whereConditionDBL.replace(strqllx, " and ql.qllx ='23' and ql.djlx='700'");
							dbldy700 = baseCommonDao.getCountByCFullSql(strSqlDBL);
					}
				}
				
				if("700".equals(djlx))
					dblcount = baseCommonDao.getCountByCFullSql(derSqlDBL.toString() + " and " + whereConditionDBL);
				else
					dblcount = baseCommonDao.getCountByFullSql(derSqlDBL.toString() + " and " + whereConditionDBL);
			}
			
			//缮证量，发证量Sql
			StringBuilder derSql = new StringBuilder();
			derSql.append("from bdck.bdcs_xmxx xmxx")
						  .append(" left join bdc_workflow.wfi_proinst pt on pt.file_number=xmxx.project_id")
						  .append(" left join bdc_workflow.wfi_actinst ai on ai.proinst_id=pt.proinst_id")
						  .append(" left join bdck.bdcs_zs_gz zs on zs.xmbh=xmxx.xmbh")
						  .append(" where 1=1");
			if("300".equals(djlx))
				derSql.append(" and pt.prodef_name not like '%特殊变更%'");
			String newwhereCondition = "";
			if("23".equals(qllx))
				newwhereCondition = whereCondition.substring(0,whereCondition.length()-strqllx.length());
			else
				newwhereCondition = whereCondition.toString();
			String whereConditionSZL = newwhereCondition.toString().replace("xmxx.slsj", "ai.actinst_end");
			long szlcount = 0;
			long fzlcount = 0;
			if(!"23".equals(qllx)){
				derSql.append(" and zs.bdcqzh like '%产权%'");
				//缮证量（证书）按缮证转出时间统计
				if(StringHelper.isEmpty(assign) || "3".equals(assign))
					szlcount = baseCommonDao.getCountByFullSql(derSql.toString() + " and ai.actinst_name ='缮证' and " + whereConditionSZL);
				//发证量（证书）按缴费发证环节转出时间统计
				if(StringHelper.isEmpty(assign) || "4".equals(assign))
					fzlcount = baseCommonDao.getCountByFullSql(derSql.toString() + " and (ai.actinst_name ='缴费发证' or ai.actinst_name ='发证' ) and " + whereConditionSZL);
				row.put("ZS_SZL", szlcount);
				row.put("ZS_FZL", fzlcount);
			}else{
				//缮证量（证明）按缮证转出时间统计
				derSql.append(" and zs.bdcqzh like '%证明%'");
				if(StringHelper.isEmpty(assign) || "3".equals(assign))
					szlcount = baseCommonDao.getCountByFullSql(derSql.toString() + " and ai.actinst_name ='缮证' and " + whereConditionSZL);
				//发证量（证明）按缴费发证转出时间统计
				if(StringHelper.isEmpty(assign) || "4".equals(assign))
					fzlcount = baseCommonDao.getCountByFullSql(derSql.toString() + " and (ai.actinst_name ='缴费发证' or ai.actinst_name ='发证' ) and " + whereConditionSZL);
				row.put("ZM_SZL", szlcount);
				row.put("ZM_FZL", fzlcount);		
			}
			if("23".equals(qllx))
				row.put("DJLX", strdjlxname+"抵押权");
			else
				row.put("DJLX", strdjlxname);
			row.put("SLL", sllcount);
			row.put("DBL", dblcount+dbldy700);
			baseCommonDao.flush();
			
			
			return row;
		}
		
		public Message getDataBYBJLTJ(Map<String, Object> mapCondition){
			
				Message msg = new Message();
				List<Map> results = new ArrayList<Map>();
				Map<String, Object> row = new HashMap<String, Object>();
				String _qssj = StringHelper.formatObject(mapCondition.get("QSSJ"));//开始时间
				String _jssj = StringHelper.formatObject(mapCondition.get("JSSJ"));//结束时间	
				
				StringBuilder zszmSql = new StringBuilder();
				zszmSql.append("select count(distinct zs.bdcqzh) from bdck.bdcs_zs_ls zs")
					   .append(" left join bdck.bdcs_qdzr_ls qdzr on zs.zsid=qdzr.zsid")
					   .append(" left join bdck.bdcs_ql_ls ql on ql.qlid=qdzr.qlid")
					   .append(" where ql.qlid is not null and to_char(ql.djsj,'yyyy-mm-dd')")
					   .append(" between '").append(_qssj).append("' and '").append(_jssj).append("'");
				long zscount = baseCommonDao.getCountByCFullSql(zszmSql.toString() + " and zs.bdcqzh like '%产权%'");
				long zmcount = baseCommonDao.getCountByCFullSql(zszmSql.toString() + " and zs.bdcqzh like '%证明%'");
				row.put("ZSZL", zscount);
				row.put("ZMZL", zmcount);
				long sllcount = 0;
				long dblcount = 0;
				for(int i=0; i<2; i++){
					for(int j=1; j<=9; j++){
						int intdjlx = j*100;
						String djlx = Integer.toString(intdjlx);
				/*		if(i==0){//受理量
							Map map = this.getRowDataYYWBLLTJ(djlx, null, _qssj, _jssj, "1");
							if(map!=null){
								if(!StringHelper.isEmpty(map.get("SLL"))){
									sllcount += Long.valueOf(map.get("SLL").toString());
								}
							}
						}*/
						if(i==1){//登簿量
							Map map = this.getRowDataYYWBLLTJ(djlx, null, _qssj, _jssj, "2");
							if(map!=null){
								if(!StringHelper.isEmpty(map.get("DBL"))){
									dblcount += Long.valueOf(map.get("DBL").toString());
								}
							}
						}
					}
				}
				row.put("XZQH", ConfigHelper.getNameByValue("XZQHMC"));//行政区划
				row.put("DBL", dblcount);
			//	row.put("ZMZL", zmcount);
				row.put("QSSJ", _qssj);
				row.put("JSSJ", _jssj);
				results.add(row);
				msg.setRows(results);
				msg.setTotal(results.size());
				baseCommonDao.flush();
				
				return msg;
		}
		/**
	  * 获取中心业务登簿量
	  * 
	  * @param mapCondition 条件集
	  * @return Message
	  * @author hpf
	  * @date 20180402
	  */
		public Message getDataZXYWDBLTJ(Map<String, Object> mapCondition){
				Message msg = new Message();
				List<Map> results = new ArrayList<Map>();
				String _qssj = StringHelper.formatObject(mapCondition.get("QSSJ"));//开始时间
				String _jssj = StringHelper.formatObject(mapCondition.get("JSSJ"));//结束时间	
				for(int i=1; i<=9; i++){
					int intdjlx = i*100;	
					String djlx = Integer.toString(intdjlx);
					Map<String, Object> row = this.getRowDataYYWBLLTJ(djlx, null, _qssj, _jssj, "2");
					if(i==1){
						row.put("QSSJ", _qssj);
						row.put("JSSJ", _jssj);
					}
					results.add(row);
				}
				msg.setRows(results);
				msg.setTotal(results.size());
				
				return msg;
		}
		public Message getDataTSSJZJTJ(Map<String, Object> mapCondition){
				
				Message msg = new Message();
				List<Map> results = new ArrayList<Map>();
				String strqssj = StringHelper.formatObject(mapCondition.get("QSSJ"));//开始时间
				String strjssj = StringHelper.formatObject(mapCondition.get("JSSJ"));//结束时间	
				String formatQssj = strqssj.replace("-", "/");
				String formatJssj = strjssj.replace("-", "/");
				String grtjSql = "SELECT COUNT(1) FROM BDCK.V_YYTJ WHERE TO_CHAR(djsj,'yyyy-mm-dd')"
																			+ " BETWEEN '" + strqssj + "' AND '" + strjssj +"'";
				for(int i=0; i<=2; i++){
						Map<String, Object> row = new HashMap<String, Object>();
						String whereSql = "";
						String strTJXM = "";
						if(i==0){//第一先查推送到税局的个人数量
								strTJXM = "推送税局个人数据量";
								whereSql = "ZJZL IN('1','2','3','4','5','99')";
						}
								
						if(i==1){//第二查推送到税局的企业数量
								strTJXM = "推送税局单位数据量";
								whereSql = "ZJZL IN('6','7')";
						}
						if(i==2){//推送到住建
								Connection gxdjkconn = JH_DBHelper.getConnect_gxdjk();
								if(gxdjkconn != null){
										String sql = "select count(distinct xmmc) as zjcount from gxdjk.gxjhxm"
																					+ " where to_char(tssj,'yyyy-mm-dd') between '" + strqssj + "' and '" + strjssj +"'";
										try {
												long zjcount = 0;
												ResultSet rs = JH_DBHelper.excuteQuery(gxdjkconn, sql);
												if(rs.next())
														zjcount = rs.getLong("zjcount");																							
												row.put("SL", zjcount);//数量
												strTJXM = "推送住建房地一体信息条数";
												rs.close();
												gxdjkconn.close();
										} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
										}
								}
						}
						if(i!=2){
								long tscount = baseCommonDao.getCountByCFullSql(grtjSql+ " AND "+whereSql);
								row.put("SL", tscount);//数量
						}				
						row.put("SJ", formatQssj+"-"+formatJssj);//时间
						row.put("TJXM", strTJXM);//统计项目			
							
						results.add(row);
				}
				msg.setRows(results);
				msg.setTotal(results.size());
				baseCommonDao.flush();
				
				return msg;
		}
		/**
	  * 柳州报件、自助、批量统计
	  * 
	  *	@param mapCondition 条件集
	  * @author hpf
	  * @date 20180402
	  */
		public Message getDataBJZZPLTJ(Map<String, Object> mapCondition){
				
				Message msg = new Message();
				List<Map> results = new ArrayList<Map>();
				
				String strqssj = StringHelper.formatObject(mapCondition.get("QSSJ"));//开始时间
				String strjssj = StringHelper.formatObject(mapCondition.get("JSSJ"));//结束时间	
				String formatQssj = strqssj.replace("-", "/");
				String formatJssj = strjssj.replace("-", "/");
				String bjzzSql = "from sharesearch.t_project pt left join sharesearch.users us on pt.slry=us.yhm"
																			+ " where to_char(pt.createtime,'yyyy-mm-dd') between '" + strqssj + "' and '" + strjssj + "'";
				String strTjxmName = "";
				for(int i=0; i<=3; i++){
						Map<String, Object> row = new HashMap<String, Object>();
						long count = 0;					
						if(i==0){//远程报件统计
									count = baseCommonDao.getCountByFullSql(bjzzSql + " and us.ssjgdm not like '%11%'");
									strTjxmName = "远程报件";
						}
						if(i==1){//自助受理统计
									count = baseCommonDao.getCountByFullSql(bjzzSql + " and us.ssjgdm like '%11%'");
									strTjxmName = "自助受理";
						}
						if(i==2){//批量（按项目）统计
									String plxmSql = "from bdc_workflow.wfi_proinst where batch like 'PC%'"
																							+ " and to_char(creat_date,'yyyy-mm-dd') between '"+ strqssj + "' and '" + strjssj + "'";
									count = baseCommonDao.getCountByFullSql(plxmSql);
									strTjxmName = "批量受理（项目）";
						}
						if(i==3){//批量（按单元）统计
								String pldySql = "from bdc_workflow.wfi_proinst pr inner join bdck.bdcs_xmxx xmxx on xmxx.project_id=pr.file_number"
																						+ " inner join bdck.bdcs_djdy_gz djdy on djdy.xmbh=xmxx.xmbh"
																						+ " where pr.batch like 'PC%' and to_char(pr.creat_date,'yyyy-mm-dd') between '"+ strqssj 
																						+ "' and '" + strjssj + "'";
								count = baseCommonDao.getCountByFullSql(pldySql);
								strTjxmName = "批量受理（单元）";
						}
						row.put("TJXM", strTjxmName);//统计项目
						row.put("SL", count);//数量
						row.put("SJ", formatQssj+"-"+formatJssj);//时间
						results.add(row);
				}
				msg.setRows(results);
				msg.setTotal(results.size());
				baseCommonDao.flush();
				
				return msg;
			
		}
		public Message getDataLRLDNTTJ(Map<String, Object> mapCondition){
				
				Message msg = new Message();
				List<Map> results = new ArrayList<Map>();
			
				String strqssj = StringHelper.formatObject(mapCondition.get("QSSJ"));//开始时间
				String strjssj = StringHelper.formatObject(mapCondition.get("JSSJ"));//结束时间
				String formatQssj = strqssj.replace("-", "/");
				String formatJssj = strjssj.replace("-", "/");
				StringBuilder derSqlSLL = new StringBuilder();
				derSqlSLL.append("from bdck.bdcs_xmxx xmxx")
							  				.append(" left join bdck.bdcs_djdy_gz djdy on xmxx.xmbh=djdy.xmbh")
							  				.append(" left join bdc_workflow.wfi_proinst pt on xmxx.project_id=pt.file_number")
							  				.append(" where xmxx.djlx='900' and to_char(xmxx.slsj,'yyyy-mm-dd') between '")
							  				.append(strqssj).append("' and '").append(strjssj).append("'");
				StringBuilder derSqlSZL = new StringBuilder();
				derSqlSZL.append("from bdck.bdcs_xmxx xmxx")
													.append(" left join bdc_workflow.wfi_proinst pt on pt.file_number=xmxx.project_id")
													.append(" left join bdc_workflow.wfi_actinst ai on ai.proinst_id=pt.proinst_id")
													.append(" left join bdck.bdcs_zs_gz zs on zs.xmbh=xmxx.xmbh")
													.append(" where xmxx.djlx='900' and ai.actinst_name ='缮证' and to_char(xmxx.slsj,'yyyy-mm-dd') between '")
													.append(strqssj).append("' and '").append(strjssj).append("'");
				String strTjxmName = "";
				for(int i=0; i<=2; i++){
						Map<String, Object> row = new HashMap<String, Object>();
						long sllcount = 0;		
						long szlcount = 0;
						String strSqlSLL = "";
						String strSqlSZL = "";
						if(i==0){//雒容
								strSqlSLL = derSqlSLL.toString()+" and pt.prodef_name like '%雒容%'";
								strSqlSZL = derSqlSZL.toString()+" and pt.prodef_name like '%雒容%'";
								strTjxmName = "雒容";
						}
						if(i==1){//柳地
							strSqlSLL = derSqlSLL.toString()+" and pt.prodef_name like '%柳地%'";
							strSqlSZL = derSqlSZL.toString()+" and pt.prodef_name like '%柳地%'";
							strTjxmName = "柳地";
					}
						if(i==2){//南铁房
							strSqlSLL = derSqlSLL.toString()+" and pt.prodef_name like '%南铁%'";
							strSqlSZL = derSqlSZL.toString()+" and pt.prodef_name like '%南铁%'";
							strTjxmName = "南铁房";
					}
						sllcount = baseCommonDao.getCountByFullSql(strSqlSLL);
						szlcount = baseCommonDao.getCountByFullSql(strSqlSZL);
						row.put("TJXM", strTjxmName);//统计项目
						row.put("SLL", sllcount);//受理量
						row.put("SZL", szlcount);//缮证量
						row.put("SJ", formatQssj+"-"+formatJssj);//时间
						results.add(row);
				}
				msg.setRows(results);
				msg.setTotal(results.size());
				baseCommonDao.flush();
				
				return msg;
		}
		
		/**
		 * 办结统计（按月） luml
		 */
		@Override
	public Message getBanJie_TJ(String startTime ,String endTime) {

		Message m = new Message();
		try {
			if (!StringHelper.isEmpty(startTime)) {
				String ab_sql =  " with a as " + " (select h.holiday_startdate, h.holiday_enddate "
						+ "    from bdc_workflow.Wfd_HolidayBB h " + "   where h.holiday_status = '1' "
						+ "     and h.holiday_type <> 1), " + " b as " + " (select p.prolsh, "
						+ "         sum(t.hangdowm_time - t.hangup_time - " + "             (select count(0) "
						+ "                from bdc_workflow.Wfd_HolidayBB h "
						+ "               where to_char(h.holiday_enddate, 'yyyy-mm-dd') between "
						+ "                     to_char(t.hangup_time, 'yyyy-mm-dd') and "
						+ "                     to_char(t.hangdowm_time, 'yyyy-mm-dd') "
						+ "                 and h.holiday_status = '1' "
						+ "                 and h.holiday_type <> 1)) as hanguptime "
						+ "    from bdc_workflow.wfi_actinst t " + "   inner join bdc_workflow.wfi_proinst p "
						+ "      on p.proinst_id = t.proinst_id " + "   inner join bdck.bdcs_xmxx x "
						+ "      on x.ywlsh = p.prolsh " + "   where t.hangup_time is not null "
						+ "     and t.hangdowm_time is not null " + "     and t.hangdowm_time > t.hangup_time "
						+ "     and t.hangdowm_time < x.djsj " + "   group by p.prolsh) " ;
				String select_sql ="	select xx.djlx, "
						+ "       xx.qllx, " + "       count(0) as totalcount, "
						+ "       round(avg(nvl(to_date(to_char(xx.djsj, 'yyyy-MM-dd hh24:mi:ss'), "
						+ "                       'yyyy-MM-dd hh24:mi:ss') - "
						+ "               to_date(to_char(xx.slsj, 'yyyy-MM-dd hh24:mi:ss'), "
						+ "                       'yyyy-MM-dd hh24:mi:ss') - "
						+ "               (select count(1) from a "
						+ "                 where A.holiday_enddate > xx.slsj "
						+ "                   and A.holiday_enddate < xx.djsj) - nvl(b.hanguptime, 0), "
						+ "               0)),2) as avgtime " + "  from bdck.bdcs_xmxx xx  "
						+ "  left join b  on b.prolsh = xx.ywlsh "
						+ " where  xx.djsj >= to_Date('"+ startTime +" 00:00:00', 'yyyy-MM-dd hh24:mi:ss') "
						+ "   and xx.djsj <= to_Date('"+ endTime +" 23:59:59', 'yyyy-MM-dd hh24:mi:ss') ";
					String qt_sql =	"   and xx.xmbh in (select xmbh from bdck.bdcs_qlr_gz where qlrlx = '2' ) "
						+ " and djlx in ('100','200')  and qllx in ('4','3') group by xx.qllx, xx.djlx  order by xx.djlx, xx.qllx ";
					
				String diya_sql ="   and xx.xmbh in (select xmbh from bdck.bdcs_sqr sqr where sqr.sqrlx = '2' and sqr.sqrlb='2'  ) "
						+ " and djlx ='100'  and qllx ='23'  group by xx.qllx, xx.djlx  order by xx.djlx, xx.qllx ";
				
				List<Map> tj_ListMap = baseCommonDao.getDataListByFullSql(ab_sql + select_sql + qt_sql);
				List<Map> diya_ListMap = baseCommonDao.getDataListByFullSql(ab_sql + select_sql + diya_sql);
				Map<String, String> map = new HashMap();
				if (!StringHelper.isEmpty(diya_ListMap) && diya_ListMap.size() > 0) {
					for (int i = 0; i < diya_ListMap.size(); i++) {
						if ("100".equals(diya_ListMap.get(i).get("DJLX")) && "23".equals(diya_ListMap.get(i).get("QLLX"))) {
							map.put("SC_DIYA_SL", diya_ListMap.get(i).get("TOTALCOUNT").toString());
							map.put("SC_DIYA_PJSJ", diya_ListMap.get(i).get("AVGTIME")== null ? "0" :diya_ListMap.get(i).get("AVGTIME").toString());
						} 
					}
				}	
				if (!StringHelper.isEmpty(tj_ListMap) && tj_ListMap.size() > 0) {
					for (int i = 0; i < tj_ListMap.size(); i++) {
						if("100".equals(tj_ListMap.get(i).get("DJLX")) && "3".equals(tj_ListMap.get(i).get("QLLX"))) {
							map.put("SC_JSYD_SL", tj_ListMap.get(i).get("TOTALCOUNT").toString());
							map.put("SC_JSYD_PJSJ", tj_ListMap.get(i).get("AVGTIME")== null ? "0" :tj_ListMap.get(i).get("AVGTIME").toString());
						}
						if ("100".equals(tj_ListMap.get(i).get("DJLX")) && "4".equals(tj_ListMap.get(i).get("QLLX"))) {
							map.put("SC_DHF_SL", tj_ListMap.get(i).get("TOTALCOUNT").toString());
							map.put("SC_DHF_PJSJ",tj_ListMap.get(i).get("AVGTIME") == null ? "0" :tj_ListMap.get(i).get("AVGTIME").toString());
						}
						if ("200".equals(tj_ListMap.get(i).get("DJLX")) && "4".equals(tj_ListMap.get(i).get("QLLX"))) {
							map.put("ZY_DHF_SL", tj_ListMap.get(i).get("TOTALCOUNT").toString());
							map.put("ZY_DHF_PJSJ", tj_ListMap.get(i).get("AVGTIME")== null ? "0" :tj_ListMap.get(i).get("AVGTIME").toString());
						}
					}
				}
				// 超期总数
				String cqzs_select_sql ="	select xx.djlx, xx.qllx, count(0) as totalcount "
						+ " from bdck.bdcs_xmxx xx  left join b  on b.prolsh = xx.ywlsh "
						+ "  where  xx.djsj >= to_Date('"+ startTime +" 00:00:00', 'yyyy-MM-dd hh24:mi:ss') "
						+ " and xx.djsj <= to_Date('"+ endTime +" 23:59:59', 'yyyy-MM-dd hh24:mi:ss') "
						+ " and to_date(to_char(xx.djsj, 'yyyy-MM-dd hh24:mi:ss'), 'yyyy-MM-dd hh24:mi:ss') - "
						+ " to_date(to_char(xx.slsj, 'yyyy-MM-dd hh24:mi:ss'), 'yyyy-MM-dd hh24:mi:ss') - "
						+ " (select count(1) from a where A.holiday_enddate > xx.slsj and A.holiday_enddate < xx.djsj) - nvl(b.hanguptime, 0)>=30 ";
				// 超期总数 条件部分
				String cqzs_diya = " and xx.xmbh in (select xmbh from bdck.bdcs_sqr sqr where sqr.sqrlx = '2' and sqr.sqrlb='2' ) "
						+ " and djlx ='100'  and qllx ='23' group by xx.qllx, xx.djlx   order by xx.djlx, xx.qllx ";
				String cqzs_qt = " and xx.xmbh in (select xmbh from bdck.bdcs_sqr sqr where sqr.sqrlx = '2'  ) "
						+ "  and djlx in ('100','200')  and qllx in ('3','4') group by xx.qllx, xx.djlx order by xx.djlx, xx.qllx ";
				
				// 超期总数  抵押
				List<Map> cqzs_diya_ListMap = baseCommonDao.getDataListByFullSql(ab_sql + cqzs_select_sql + cqzs_diya);
				// 超期总数  其他
				List<Map> cqzs_qt_ListMap = baseCommonDao.getDataListByFullSql(ab_sql + cqzs_select_sql + cqzs_qt);
				
				if (!StringHelper.isEmpty(cqzs_diya_ListMap) && cqzs_diya_ListMap.size() > 0) {
					for (int i = 0; i < cqzs_diya_ListMap.size(); i++) {
						if ("100".equals(cqzs_diya_ListMap.get(i).get("DJLX")) && "23".equals(cqzs_diya_ListMap.get(i).get("QLLX"))) {
							map.put("CQ_SC_DIYA", cqzs_diya_ListMap.get(i).get("TOTALCOUNT").toString());
						} 
					}
				}
				if (!StringHelper.isEmpty(cqzs_qt_ListMap) && cqzs_qt_ListMap.size() > 0) {
					for (int i = 0; i < cqzs_qt_ListMap.size(); i++) {
						if("100".equals(cqzs_qt_ListMap.get(i).get("DJLX")) && "3".equals(cqzs_qt_ListMap.get(i).get("QLLX"))) {
							map.put("CQ_SC_JSYD", cqzs_qt_ListMap.get(i).get("TOTALCOUNT").toString());
						}
						if ("100".equals(cqzs_qt_ListMap.get(i).get("DJLX")) && "4".equals(cqzs_qt_ListMap.get(i).get("QLLX"))) {
							map.put("CQ_SC_DHF", cqzs_qt_ListMap.get(i).get("TOTALCOUNT").toString());
						}
						if ("200".equals(cqzs_qt_ListMap.get(i).get("DJLX")) && "4".equals(cqzs_qt_ListMap.get(i).get("QLLX"))) {
							map.put("CQ_ZY_DHF", cqzs_qt_ListMap.get(i).get("TOTALCOUNT").toString());
						}
					}
				}
				map.put("YF", startTime.substring(0, 7));
				List<Map> list = new ArrayList<Map>();
				list.add(map);

				m.setRows(list);
				m.setTotal(list.size());
			}
		} catch (Exception e) {
			m = null;
		}
		return m;
	}
		/**
		 * 办结-超期详情
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Message getBanJie_CQXQ(String startTime ,String endTime,String djlx,String qllx) {
			Message m = new Message();
			if (!StringHelper.isEmpty(startTime)) {
				String ab_sql =  " with a as " + " (select h.holiday_startdate, h.holiday_enddate "
						+ "    from bdc_workflow.Wfd_HolidayBB h " + "   where h.holiday_status = '1' "
						+ "     and h.holiday_type <> 1), " + " b as " + " (select p.prolsh, "
						+ "         sum(t.hangdowm_time - t.hangup_time - " + "             (select count(0) "
						+ "                from bdc_workflow.Wfd_HolidayBB h "
						+ "               where to_char(h.holiday_enddate, 'yyyy-mm-dd') between "
						+ "                     to_char(t.hangup_time, 'yyyy-mm-dd') and "
						+ "                     to_char(t.hangdowm_time, 'yyyy-mm-dd') "
						+ "                 and h.holiday_status = '1' "
						+ "                 and h.holiday_type <> 1)) as hanguptime "
						+ "    from bdc_workflow.wfi_actinst t " + "   inner join bdc_workflow.wfi_proinst p "
						+ "      on p.proinst_id = t.proinst_id " + "   inner join bdck.bdcs_xmxx x "
						+ "      on x.ywlsh = p.prolsh " + "   where t.hangup_time is not null "
						+ "     and t.hangdowm_time is not null " + "     and t.hangdowm_time > t.hangup_time "
						+ "     and t.hangdowm_time < x.djsj " + "   group by p.prolsh) "
						+ "	 select xx.djlx, xx.qllx, "
						+ "       round(nvl(to_date(to_char(xx.djsj, 'yyyy-MM-dd hh24:mi:ss'), "
						+ "                       'yyyy-MM-dd hh24:mi:ss') - "
						+ "               to_date(to_char(xx.slsj, 'yyyy-MM-dd hh24:mi:ss'), "
						+ "                       'yyyy-MM-dd hh24:mi:ss') - "
						+ "               (select count(1) from a "
						+ "                 where A.holiday_enddate > xx.slsj "
						+ "                   and A.holiday_enddate < xx.djsj) - nvl(b.hanguptime, 0), "
						+ "               0),2) as protime , xx.ywlsh, xx.slsj, xx.djsj  from bdck.bdcs_xmxx xx  "
						+ "  left join b  on b.prolsh = xx.ywlsh "
						+ " where  xx.djsj >= to_Date('"+ startTime +" 00:00:00', 'yyyy-MM-dd hh24:mi:ss') "
						+ "   and xx.djsj <= to_Date('"+ endTime +" 23:59:59', 'yyyy-MM-dd hh24:mi:ss') "
						+ " and to_date(to_char(xx.djsj, 'yyyy-MM-dd hh24:mi:ss'), 'yyyy-MM-dd hh24:mi:ss') - "
						+ " to_date(to_char(xx.slsj, 'yyyy-MM-dd hh24:mi:ss'), 'yyyy-MM-dd hh24:mi:ss') - "
						+ " (select count(1) from a where A.holiday_enddate > xx.slsj and A.holiday_enddate < xx.djsj) - nvl(b.hanguptime, 0)>=30 ";
				String cqxq ="";
				if ("100".equals(djlx)&&"23".equals(qllx)) {// 抵押
					cqxq =" and xx.xmbh in (select xmbh from bdck.bdcs_sqr sqr where sqr.sqrlx = '2' and sqr.sqrlb='2' )"
							+ "  and djlx ='100'  and qllx ='23' group by xx.qllx, xx.djlx,xx.ywlsh,xx.djsj,xx.slsj,b.hanguptime "
							+ "   order by xx.djlx, xx.qllx,xx.ywlsh,xx.djsj,xx.slsj,b.hanguptime ";
				}else  {
					cqxq =" and xx.xmbh in (select xmbh from bdck.bdcs_sqr sqr where sqr.sqrlx = '2'  )"
							+ "  and djlx ='"+djlx+"'  and qllx ='"+qllx+"' group by xx.qllx, xx.djlx,xx.ywlsh,xx.djsj,xx.slsj,b.hanguptime "
							+ "   order by xx.djlx, xx.qllx,xx.ywlsh,xx.djsj,xx.slsj,b.hanguptime ";
				}
				// 超期详情
				List<Map> cqxq_ListMap = baseCommonDao.getDataListByFullSql(ab_sql + cqxq);
				List<Map> result_new = new ArrayList<Map>();
				if (cqxq_ListMap != null && cqxq_ListMap.size() > 0) {
					for (Map m_cqxq : cqxq_ListMap) {
						String _djlx = StringHelper.formatObject(m_cqxq.get("DJLX"));
						m_cqxq.put("DJLX", ConstHelper.getNameByValue("DJLX", _djlx));
						String _qllx = StringHelper.formatObject(m_cqxq.get("QLLX"));
						m_cqxq.put("QLLX", ConstHelper.getNameByValue("QLLX", _qllx));
						result_new.add(m_cqxq);
					}
				}
				m.setRows(cqxq_ListMap);
				m.setTotal(cqxq_ListMap.size());
			}	
			return m;
		}
		
}
