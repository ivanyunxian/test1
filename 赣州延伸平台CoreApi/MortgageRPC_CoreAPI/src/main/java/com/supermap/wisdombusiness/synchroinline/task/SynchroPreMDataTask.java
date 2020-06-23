package com.supermap.wisdombusiness.synchroinline.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.common.util.StringHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.synchroinline.dao.TimerDao;
import com.supermap.wisdombusiness.synchroinline.dao.synchroDao;
import com.supermap.wisdombusiness.synchroinline.model.EstateInlineLog;
import com.supermap.wisdombusiness.synchroinline.model.Inline_dxts;
import com.supermap.wisdombusiness.synchroinline.model.SynchroInlineEnum;
import com.supermap.wisdombusiness.synchroinline.model.T_SCHEDULE;
import com.supermap.wisdombusiness.synchroinline.model.T_certificate_ls;
import com.supermap.wisdombusiness.synchroinline.model.T_deleted_proinst;
import com.supermap.wisdombusiness.synchroinline.model.T_project_qlr;
import com.supermap.wisdombusiness.synchroinline.service.ExtractDataAnnouncesService;
import com.supermap.wisdombusiness.synchroinline.service.ExtractDataZhengsService;
import com.supermap.wisdombusiness.synchroinline.service.SynchroPreMData;
import com.supermap.wisdombusiness.synchroinline.util.CommonsHttpInvoke;
import com.supermap.wisdombusiness.synchroinline.util.DButil;

@Component
public class SynchroPreMDataTask
{
	@Autowired
	private SynchroPreMData synchroPreMData;
	@Autowired
	private ExtractDataAnnouncesService extractDataAnnouncesService;
	@Autowired
	private ExtractDataZhengsService extractDataZhengsService;
	private static Logger logger = Logger.getLogger(SynchroPreMDataTask.class);

	public static final String SCGGCHANNELID = "scgg";
	public static final String GZGGCHANNELID = "gzgg";
	public static final String ZXGGCHANNELID = "zxgg";
	public static final String ZFGGCHANNELID = "zfgg";

	/**
	 * spring task2 推送 流程办理进度信息到前置机数据库中
	 * */
	public synchronized void synchroProcessSchedule()
	{
		project_Qlr();
		synchroProgress();
	}

	private void synchroProgress()
	{
		// 从日志表获取最后一次同步时间
		String sql_date = "select * from (select to_char(OPERATION_DATE,'yyyy-mm-dd HH24:mi:ss') as MAX_RQ from bdc_workflow.T_LOG_ESATEINLINE where OPERATION_TYPE ='1' and OPERATION_RES = 'SUCCESS' order by OPERATION_DATE desc) where  rownum=1";
		TimerDao timerDao = null;
		try
		{
			timerDao = new TimerDao();
			List<Map> maps = timerDao.getDataListByFullSql(sql_date);
			if (maps == null || maps.isEmpty())
			{
				System.out.println("进度同步日志表未指定初始时间，无法同步。");
			}
			else
			{
				String str_max_rq = String.valueOf(maps.get(0).get("MAX_RQ"));
				if (str_max_rq != null)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// 最后一次同步时间
					Date max_rq = sdf.parse(str_max_rq);
					List<String[]> days = this.getDates(max_rq);
					for (String[] day : days)
					{
						this.synchroProgressOfDay(timerDao, day[0], day[1]);
					}
					// 清理日志，以免日志过大
					String sql_del = "delete bdc_workflow.T_LOG_ESATEINLINE a where a.operation_type='1' and a.OPERATION_DATE < to_date('" + str_max_rq + "','yyyy-mm-dd HH24:mi:ss')";
					timerDao.getSession().createSQLQuery(sql_del).executeUpdate();
					timerDao.flush();
				}
				// 同步最后一个节点状态
				this.syncEndpoint(timerDao);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (timerDao != null)
			{
				timerDao.close();
				timerDao = null;
			}
		}
	}

	private void synchroProgressOfDay(TimerDao dao, String start_rq, String end_rq) throws Exception
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM (");
		sql.append(" SELECT row_number() over (partition by A.proinst_id, A.actdef_id order BY A.actinst_start desc) rn, ");
		sql.append(" A.ACTINST_ID,ACTINST_STATUS,A.ACTINST_NAME,A.ACTINST_START,A.ACTINST_END,A.STAFF_NAME,C.ACTDEF_TYPE ,");
		sql.append(" A.PROINST_ID,A.ACTDEF_ID,B.PROLSH,B.PRODEF_ID,B.PRODEF_NAME,B.PROJECT_NAME");
		sql.append(" from bdc_workflow.wfi_actinst A  INNER JOIN (select PROINST_ID,PRODEF_ID,PRODEF_NAME,PROLSH,PROJECT_NAME from bdc_workflow.wfi_proinst where prodef_id in (");
		sql.append(" select PRODEF_ID  from bdc_workflow.wfd_prodef)");
		sql.append(" ) B ON A.PROINST_ID = B.PROINST_ID  inner  join bdc_workflow.wfd_actdef C ON  A.ACTDEF_ID = C.ACTDEF_ID ");
		sql.append(" WHERE A.ACTINST_START>= to_date('" + start_rq + "','yyyy-MM-dd HH24:mi:ss') and A.ACTINST_START< to_date('" + end_rq + "','yyyy-MM-dd HH24:mi:ss')  ORDER BY A.ACTINST_START DESC");
		sql.append(") WHERE RN = 1");
		sql.append(" ORDER BY ACTINST_START asc");
		List<Map> maps = dao.getDataListByFullSql(sql.toString());
		Session session = null;
		Transaction tra = null;
		try
		{
			session = DButil.newGetMscSession();
			// 开启事务
			tra = session.beginTransaction();
			for (Map map : maps)
			{
				T_SCHEDULE obj = new T_SCHEDULE();
				obj.setId(String.valueOf(map.get("ACTINST_ID")));
				obj.setSerialnumber(String.valueOf(map.get("PROLSH")));
				obj.setProcessdefid(String.valueOf(map.get("PRODEF_ID")));
				obj.setProcessname(String.valueOf(map.get("PRODEF_NAME")));
				obj.setActivityname(String.valueOf(map.get("ACTINST_NAME")));
				Timestamp start = (Timestamp) map.get("ACTINST_START");
				obj.setActstart(start);
				Timestamp end = (Timestamp) map.get("ACTINST_END");
				obj.setActend(end);
				obj.setStaffname(null);
				obj.setActstatus(Integer.parseInt(map.get("ACTINST_STATUS").toString()));
				obj.setIssynchro(0);
				obj.setNewprodefinfo("");
				if (map.get("ACTDEF_TYPE") != null && map.get("ACTDEF_TYPE").toString().equals("1010"))
				{
					String prodefStr = synchroPreMData.getJSONProdef(dao, map.get("PRODEF_ID").toString(), map.get("ACTDEF_ID").toString());
					obj.setNewprodefinfo(prodefStr);
				}
				obj.setProjectname(String.valueOf(map.get("PROJECT_NAME")));
				obj.setProinst_id(String.valueOf(map.get("PROINST_ID")));
				session.saveOrUpdate(obj);
			}
			EstateInlineLog log = new EstateInlineLog();
			log.setOperation_Content("同步进度信息");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			log.setOperation_Date(sdf.parse(end_rq));
			log.setOperation_Type("1");
			log.setOperation_Res("SUCCESS");
			session.flush();
			tra.commit();
			dao.save(log);
			dao.flush();
		}
		catch (Exception ex)
		{
			tra.rollback();
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

	private List<String[]> getDates(Date max_rq)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long day_hms = 1000 * 60 * 60 * 24;
		long day_ms = 1000;
		List<String[]> dayArr = new ArrayList<String[]>();
		Date now = new Date();
		long diff = now.getTime() - max_rq.getTime();// 这样得到的差值是微秒级别
		// 得到相差天数
		long days = diff / day_hms;
		if (days >= 0)
		{
			if (days <= 1)
			{
				long ms = diff / day_ms;
				if (ms > 60)
				{
					Calendar c = Calendar.getInstance();
					c.setTime(now);
					c.add(Calendar.MINUTE, -1);
					now = c.getTime();
					dayArr.add(new String[] { sdf.format(max_rq), sdf.format(now) });
				}
			}
			else
			{
				//
				Date start_rq = max_rq;
				for (int i = 1; i <= days; i++)
				{
					Date end_rq = new Date(start_rq.getYear(), start_rq.getMonth(), start_rq.getDate(), 23, 59, 59);
					String[] ds = new String[] { sdf.format(start_rq), sdf.format(end_rq) };
					if (ds[0].equals(ds[1]))
					{
						Calendar c = Calendar.getInstance();
						c.setTime(end_rq);
						c.add(Calendar.SECOND, 1);
						start_rq = c.getTime();
						continue;
					}
					dayArr.add(ds);
					Calendar c = Calendar.getInstance();
					c.setTime(end_rq);
					c.add(Calendar.SECOND, 1);
					start_rq = c.getTime();
				}
				dayArr.add(new String[] { sdf.format(start_rq), sdf.format(new Date()) });
			}
		}
		return dayArr;
	}

	/**
	 * 同步终结点状态
	 * 
	 * @param dao
	 * @throws Exception
	 */
	private void syncEndpoint(TimerDao dao) throws Exception
	{
		String sql = "select t.actinst_id as id,t.actinst_end from bdc_workflow.wfi_actinst t where t.actdef_type='5010' and t.actinst_status='0' and t.actinst_id not in ( select a.id from bdc_workflow.T_LOG_ESATEINLINE a where a.operation_type='100' ) order by t.actinst_start asc";
		List<Map> maps = dao.getDataListByFullSql(sql);
		Session session = null;
		Transaction tra = null;
		try
		{
			session = DButil.newGetMscSession();
			// 开启事务
			tra = session.beginTransaction();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map map : maps)
			{
				String id = map.get("ID").toString();
				Timestamp end = (Timestamp) map.get("ACTINST_END");
				String str_end = sdf.format(end);
				List list = session.createSQLQuery("select t.id from smwb_inline.T_SCHEDULE t where t.id='" + id + "'").list();
				if (list.isEmpty())
				{
					continue;
				}
				String edit_sql = "update smwb_inline.T_SCHEDULE t set t.actdef_type='5010',t.tb_endpoint='0',actStatus=0,t.actend=to_date('" + str_end + "','yyyy-mm-dd hh24:mi:ss') where t.id='" + id + "'";
				int rows = session.createSQLQuery(edit_sql).executeUpdate();
				// 记录日志
				if (rows > 0)
				{
					EstateInlineLog log = new EstateInlineLog();
					log.setId(id);
					log.setOperation_Content("同步进度信息");
					log.setOperation_Date(new Date());
					log.setOperation_Type("100");
					log.setOperation_Res("SUCCESS");
					dao.saveOrUpdate(log);
					dao.flush();
				}
			}
			session.flush();
			tra.commit();
		}
		catch (Exception ex)
		{
			tra.rollback();
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
	 * spring task3 推送 证书信息到前置机数据库中
	 * 
	 * */
	public synchronized void synchroCertificateInfo() throws SQLException
	{
		// TODO:缮证之后,xz层才会有完整的证书信息（拥有证书编号），先登簿后缮证
		Date date = getSynchroDate("2");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sdf.format(date);
		String sql = "SELECT A.* FROM BDCK.BDCS_XMXX A LEFT JOIN bdck.bdcs_djsz B ON A.XMBH = B.XMBH WHERE A.SFDB=1 AND  B.SZSJ > to_date('" + strdate + "','yyyy/mm/dd hh24:mi:ss')";
		List<BDCS_XMXX> lists = null;
		String url = ConfigHelper.getNameByValue("SYNCHROXMXXURL");
		NameValuePair[] param = { new NameValuePair("sql", sql) };
		String xmxxs = new CommonsHttpInvoke().commonHttpDoPost(null, null, url, param);
		if (!StringHelper.isEmpty(xmxxs) && !xmxxs.equals("[]"))
		{
			lists = com.alibaba.fastjson.JSONArray.parseArray(xmxxs, BDCS_XMXX.class);
		}
		String[] strs = null;
		if (lists != null && lists.size() > 0)
		{
			BDCS_XMXX xmxx = null;
			strs = new String[lists.size()];
			for (int i = 0; i < lists.size(); i++)
			{
				xmxx = lists.get(i);
				strs[i] = xmxx.getPROJECT_ID();
			}
		}
		if (strs != null && strs.length > 0)
		{
			List<HashMap<String, Object>> zs = extractDataZhengsService.getZSInfos(strs);
			HashMap<String, Object> hashMap = null;
			Connection conn = null;
			JSONObject zsdata = null;
			if (zs != null && zs.size() > 0)
			{
				conn = DButil.getConnection();
				PreparedStatement ps = null;
				for (int i = 0; i < zs.size(); i++)
				{
					hashMap = zs.get(i);
					ps = conn.prepareStatement("insert into SMWB_INLINE.T_CERTIFICATE_INLINE " + "(ESTATENO,SEQNO,CERTIFICATENO,MONTH,DAY,YEAR," + "PROSHOR,ADMINAREA,OBLIGEE,OTHER,OBLIGOR," + "LOCATION,RIGHTTYPE,ID,COMSITUATION,RIGHTNATURE,USEFOR,AREA,TIMELIMITE," + " RIGHTOTHER,RIGHTCLASS,FJ,ZJH)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					try
					{
						if (hashMap.get("ZSDATA") != null)
						{
							zsdata = new JSONObject(hashMap.get("ZSDATA").toString());
						}
						else
						{
							zsdata = null;
						}
					}
					catch (JSONException e1)
					{
						e1.printStackTrace();
					}
					if (hashMap.get("BDCQZH") != null)
					{
						ps.setString(1, hashMap.get("BDCQZH").toString());
					}
					else
					{
						ps.setString(1, "");
					}
					if (hashMap.get("ZSBH") != null)
					{
						ps.setString(3, hashMap.get("ZSBH").toString());
					}
					else if (hashMap.get("zsbh") != null)
					{
						ps.setString(3, hashMap.get("zsbh").toString());
					}
					else
					{
						ps.setString(3, "");
					}

					ps.setString(23, hashMap.get("ZJH").toString());
					ps.setString(9, hashMap.get("QLRMC").toString());
					if (zsdata != null && zsdata.length() > 0)
					{
						try
						{
							if (zsdata.has("BDCDYH") && zsdata.get("BDCDYH") != null)
							{
								ps.setString(2, zsdata.get("BDCDYH").toString());
							}
							else if (zsdata.has("bdcdyh") && zsdata.get("bdcdyh") != null)
							{
								ps.setString(2, zsdata.get("bdcdyh").toString());
							}
							else
							{
								ps.setString(2, "");
							}
							if (zsdata.has("ZMQLHSX") && zsdata.get("ZMQLHSX") != null)
							{
								ps.setString(13, zsdata.get("ZMQLHSX").toString());
							}
							else if (zsdata.has("zmqlhsx") && zsdata.get("zmqlhsx") != null)
							{
								ps.setString(13, zsdata.get("zmqlhsx").toString());
							}
							else
							{
								ps.setString(13, "");
							}
							if (zsdata.has("FM_MONTH") && zsdata.get("FM_MONTH") != null)
							{
								ps.setInt(4, Integer.parseInt(zsdata.getString("FM_MONTH")));
							}
							else if (zsdata.has("month") && zsdata.get("month") != null)
							{
								ps.setInt(4, Integer.parseInt(zsdata.getString("month")));
							}
							else
							{
								ps.setObject(4, null);
							}
							if (zsdata.has("FM_DAY") && zsdata.get("FM_DAY") != null)
							{
								ps.setInt(5, Integer.parseInt(zsdata.getString("FM_DAY")));
							}
							else if (zsdata.has("day") && zsdata.get("day") != null)
							{
								ps.setInt(5, Integer.parseInt(zsdata.getString("day")));
							}
							else
							{
								ps.setObject(5, null);
							}
							if (zsdata.has("FM_YEAR") && zsdata.get("FM_YEAR") != null)
							{
								ps.setInt(6, Integer.parseInt(zsdata.getString("FM_YEAR")));
							}
							else if (zsdata.has("year") && zsdata.get("year") != null)
							{
								ps.setInt(6, Integer.parseInt(zsdata.getString("year")));
							}
							else
							{
								ps.setObject(6, null);
							}
							if (zsdata.has("QHJC") && zsdata.get("QHJC") != null)
							{
								ps.setString(7, zsdata.getString("QHJC"));
							}
							else if (zsdata.has("sjc") && zsdata.get("sjc") != null)
							{
								ps.setString(7, zsdata.getString("sjc"));
							}
							else
							{
								ps.setString(7, "");
							}
							if (zsdata.has("QHMC") && zsdata.get("QHMC") != null)
							{
								ps.setString(8, zsdata.getString("QLXZ"));
							}
							else if (zsdata.has("qhmc") && zsdata.get("qhmc") != null)
							{
								ps.setString(8, zsdata.getString("qhmc"));
							}
							else
							{
								ps.setString(8, "");
							}
							/*
							 * 权利人和证件号获取方式必须一致，否则会导致错位
							 * if(zsdata.has("QLR")&&zsdata.get("QLR")!=null){
							 * ps.setString(9, zsdata.getString("QLR")); }else
							 * if(zsdata.has("qlr")&&zsdata.get("qlr")!=null){
							 * ps.setString(9, zsdata.getString("qlr")); }else{
							 * ps.setString(9,""); }
							 */
							if (zsdata.has("QT") && zsdata.get("QT") != null)
							{
								ps.setString(10, zsdata.getString("QT"));
							}
							else if (zsdata.has("qt") && zsdata.get("qt") != null)
							{
								ps.setString(10, zsdata.getString("qt"));
							}
							else
							{
								ps.setString(10, "");
							}
							if (zsdata.has("YWR") && zsdata.get("YWR") != null)
							{
								ps.setString(11, zsdata.getString("YWR"));
							}
							else if (zsdata.has("ywr") && zsdata.get("ywr") != null)
							{
								ps.setString(11, zsdata.getString("ywr"));
							}
							else
							{
								ps.setString(11, "");
							}
							if (zsdata.has("ZL") && zsdata.get("ZL") != null)
							{
								ps.setString(12, zsdata.getString("ZL"));
							}
							else if (zsdata.has("zl") && zsdata.get("zl") != null)
							{
								ps.setString(12, zsdata.getString("zl"));
							}
							else
							{
								ps.setString(12, "");
							}
							if (zsdata.has("GYQK") && !zsdata.isNull("GYQK"))
							{
								ps.setString(15, zsdata.get("GYQK").toString());
							}
							else
							{
								ps.setString(15, "");
							}
							if (zsdata.has("QLXZ") && !zsdata.isNull("QLXZ"))
							{
								ps.setString(16, zsdata.get("QLXZ").toString());
							}
							else
							{
								ps.setString(16, "");
							}
							if (zsdata.has("YT") && !zsdata.isNull("YT"))
							{
								ps.setString(17, zsdata.get("YT").toString());
							}
							else
							{
								ps.setString(17, "");
							}
							if (zsdata.has("MJ") && !zsdata.isNull("MJ"))
							{
								ps.setString(18, zsdata.get("MJ").toString());
							}
							else
							{
								ps.setString(18, "");
							}
							if (zsdata.has("SYQX") && !zsdata.isNull("SYQX"))
							{
								ps.setString(19, zsdata.get("SYQX").toString());
							}
							else
							{
								ps.setString(19, "");
							}
							if (zsdata.has("QLQTZK") && !zsdata.isNull("QLQTZK"))
							{
								ps.setString(20, zsdata.get("QLQTZK").toString());
							}
							else
							{
								ps.setString(20, "");
							}
							if (zsdata.has("QLLX") && !zsdata.isNull("QLLX"))
							{
								ps.setString(21, zsdata.get("QLLX").toString());
							}
							else
							{
								ps.setString(21, "");
							}
							// 附记
							if (zsdata.has("FJ") && !zsdata.isNull("FJ"))
							{
								ps.setString(22, zsdata.get("FJ").toString());
							}
							else if (zsdata.has("fj") && !zsdata.isNull("fj"))
							{
								ps.setString(22, zsdata.get("fj").toString());
							}
							else
							{
								ps.setString(22, "");
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						ps.setString(2, "");
						ps.setInt(4, 12);
						ps.setInt(5, 01);
						ps.setInt(6, 2016);
						ps.setString(7, "");
						ps.setString(8, "");
						ps.setString(9, "");
						ps.setString(10, "");
						ps.setString(11, "");
						ps.setString(12, "");
						ps.setString(15, "");
						ps.setString(16, "");
						ps.setString(17, "");
						ps.setString(18, "");
						ps.setString(19, "");
						ps.setString(20, "");
						ps.setString(21, "");
						ps.setString(22, "");
						ps.setString(23, "");
					}
					ps.setString(14, UUID.randomUUID().toString().replace("-", ""));
					try
					{
						ps.execute();
						ps.close();
						synchroPreMData.createLog("2", "推送证书信息至前置机,不动产权证号为：" + hashMap.get("BDCQZH").toString(), SynchroInlineEnum.valueOfString(SynchroInlineEnum.SUCCESS));
					}
					catch (Exception e)
					{
						try
						{
							synchroPreMData.createLog("2", "推送证书信息至前置机,不动产权证号为：" + hashMap.get("BDCQZH").toString(), SynchroInlineEnum.valueOfString(SynchroInlineEnum.ERROR));
						}
						catch (Exception e1)
						{
						}
					}
				}
				DButil.close(null, ps, conn);
			}
			else
			{
				logger.info("证书信息没有找到！");
			}
		}

	}

	/**
	 * spring task4 推送 公告到前置机数据库中 Operation_type
	 * 0从前置机同步数据到登记1将办理进度推送到前置机2将公告推送至前置机3将证书信息推送至前置机
	 * */
	public synchronized void synchroNotice() throws SQLException
	{
		Date date = getSynchroDate("3");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = df.format(date);
		// TODO:登簿之后
		List<BDCS_XMXX> lists = null;
		String sql = "select * from bdck.bdcs_xmxx where SFDB=1 AND SLSJ > to_date('" + strdate + "','yyyy/mm/dd hh24:mi:ss')";
		String url = ConfigHelper.getNameByValue("SYNCHROXMXXURL");
		NameValuePair[] param = { new NameValuePair("sql", sql) };
		String xmxxs = new CommonsHttpInvoke().commonHttpDoPost(null, null, url, param);
		if (!StringHelper.isEmpty(xmxxs) && !xmxxs.equals("[]"))
		{
			lists = com.alibaba.fastjson.JSONArray.parseArray(xmxxs, BDCS_XMXX.class);
		}
		Connection conn = null;
		if (lists != null && lists.size() > 0)
		{
			BDCS_XMXX xmxx = null;
			String xmmc = "";
			String lsh = "";
			String filenumber = "";
			Date slsj = null;
			String urlNotice = ConfigHelper.getNameByValue("SYNCHRONOTICESURL");
			JSONArray proinsts = null;
			for (int i = 0; i < lists.size(); i++)
			{
				xmxx = lists.get(i);
				xmmc = xmxx.getXMMC();
				slsj = xmxx.getSLSJ();
				lsh = xmxx.getYWLSH();
				filenumber = xmxx.getPROJECT_ID();
				String url2 = ConfigHelper.getNameByValue("SYNCHROOBJLISTURL");
				NameValuePair[] param2 = { new NameValuePair("sql", "select * from bdc_workflow.wfi_proinst where file_number = '" + filenumber + "'") };
				String wfiproinst = new CommonsHttpInvoke().commonHttpDoPost(null, null, url2, param2);
				if (!StringHelper.isEmpty(wfiproinst) && !wfiproinst.equals("[]"))
				{
					proinsts = com.alibaba.fastjson.JSONArray.parseArray(wfiproinst);
					if (proinsts != null && proinsts.size() > 0)
					{
						lsh = proinsts.getJSONObject(0).getString("PROLSH");
					}
				}
				if (lsh != null && !lsh.equals(""))
				{
					CommonsHttpInvoke chi = new CommonsHttpInvoke();
					String notice = chi.commonHttpDoPostNotice(null, null, urlNotice, filenumber);
					if (notice != null && !notice.equals("") && notice.indexOf("[") == 0)
					{
						JSONArray noticeContent = JSONArray.parseArray(notice);
						String DJLX = "";
						Object tempObj = null;
						if (noticeContent != null && noticeContent.size() > 0)
						{
							tempObj = noticeContent.get(0);
							com.alibaba.fastjson.JSONObject jsonObj = com.alibaba.fastjson.JSONObject.parseObject(tempObj.toString());
							if (!jsonObj.isEmpty())
							{
								if (jsonObj.containsKey("GGLX"))
								{
									DJLX = jsonObj.getString("GGLX");
								}
							}
						}
						if (tempObj != null)
						{
							com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(tempObj.toString());
							if (!obj.isEmpty())
							{
								conn = DButil.getConnection();
								PreparedStatement ps = null;
								ps = conn.prepareStatement("insert into SMWB_INLINE.CMS_ARTICLE" + "(ID,NOTICESTART,QLR,RIGHTTYPE,RIGHTTYPENAME,ESTATENO,AREA,USEFOR,LOCATE,PROVENO,CTATEPRONO,NOTICETYPE,NOTICETYPENAME," + "CHANNELID,NOTICECONTENT,YWLSH,PROJECTNAME,SLSJ)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?)");
								ps.setString(1, UUID.randomUUID().toString().replace("-", ""));
								Date noticestart = null;
								// 登簿时间
								if (obj.getString("DJSJ") != null && !obj.getString("DJSJ").equals(""))
								{
									try
									{
										noticestart = df.parse(obj.getString("DJSJ"));
									}
									catch (ParseException e)
									{
										e.printStackTrace();
									}
								}
								else
								{
									noticestart = new Date();
								}
								ps.setTimestamp(2, new java.sql.Timestamp(noticestart.getTime()));
								ps.setString(3, obj.getString("QLR"));
								ps.setString(4, obj.getString("QLLX"));
								ps.setString(5, obj.getString("QLLXMC"));
								ps.setString(6, obj.getString("BDCDYH"));
								ps.setString(7, obj.getString("AREA"));
								ps.setString(8, obj.getString("YT"));
								ps.setString(9, obj.getString("ZL"));
								ps.setString(10, obj.getString("BDCZMH"));
								ps.setString(11, obj.getString("BDCQZH"));
								ps.setString(15, notice);
								ps.setString(16, lsh);
								ps.setString(17, xmmc);
								if (slsj != null)
								{
									ps.setTimestamp(18, new java.sql.Timestamp(slsj.getTime()));
								}
								else
								{
									ps.setTimestamp(18, null);
								}
								if (DJLX.equals("scgg"))
								{
									ps.setString(12, "scgg");
									ps.setString(13, "首次公告");
									ps.setString(14, SCGGCHANNELID);
								}
								else if (DJLX.equals("gzgg"))
								{
									ps.setString(12, "gzgg");
									ps.setString(13, "更正公告");
									ps.setString(14, GZGGCHANNELID);
								}
								else if (DJLX.equals("zxgg"))
								{
									ps.setString(12, "zxgg");
									ps.setString(13, "注销公告");
									ps.setString(14, ZXGGCHANNELID);
								}
								else if (DJLX.equals("zfgg"))
								{
									ps.setString(12, "zfgg");
									ps.setString(13, "作废公告");
									ps.setString(14, ZFGGCHANNELID);
								}
								try
								{
									ps.execute();

									try
									{
										synchroPreMData.createLog("3", "推送公告至前置机不动产单元号：" + obj.getString("BDCDYH").toString(), SynchroInlineEnum.valueOfString(SynchroInlineEnum.SUCCESS));
									}
									catch (Exception commitError)
									{
									}
								}
								catch (Exception e)
								{
									try
									{
										synchroPreMData.createLog("3", "推送公告至前置机不动产单元号：" + obj.getString("BDCDYH").toString(), SynchroInlineEnum.valueOfString(SynchroInlineEnum.ERROR));
									}
									catch (Exception e1)
									{
									}
								}
								DButil.close(null, ps, conn);
							}
						}
					}
				}
			}
		}
	}

	public Date getSynchroDate(String operationtype)
	{
		String url = ConfigHelper.getNameByValue("SYNCHRODATEURL");
		NameValuePair[] param = { new NameValuePair("sql", " select * from bdc_workflow.T_LOG_ESATEINLINE where OPERATION_TYPE ='" + operationtype + "' and OPERATION_RES = 'SUCCESS' order by OPERATION_DATE desc") };
		String sychroDate = new CommonsHttpInvoke().commonHttpDoPost(null, null, url, param);
		Date extractDate = new Date();
		if (!StringHelper.isEmpty(sychroDate))
		{
			extractDate = new Date(Long.parseLong(sychroDate));
		}
		return extractDate;
	}

	/**
	 * 同步被销毁的信息（包括注销的证书、证明信息，被删除项目的进度信息）
	 */
	public synchronized void synchroDestroyedInfo()
	{
		synchroDeletedProject();
		synchrotbCertificate_Ls();
	}

	/**
	 * 同步被删除项目信息到前置机，同时删除前置机被删除项目的进度信息。
	 */
	private void synchroDeletedProject()
	{
		Session session = null;
		Transaction tran = null;
		TimerDao timerDao = null;
		try
		{
			timerDao = new TimerDao();
			session = DButil.newGetMscSession();
			tran = session.beginTransaction();
			String sql = "select t.proinst_id as PROINST_ID,t.operation_msg as DEL_MSG,t.staff_id as DEL_UID,t.staff_name as DEL_UNAME,sysdate as TBRQ from bdc_workflow.WFI_ABNORMAL t where t.proinst_id is not null and t.proinst_id not in (select t1.PROINST_ID from bdc_workflow.T_DELETED_PROINST t1) and rownum<1000 order by t.opeartion_date asc";
			List<Map> maps = timerDao.getDataListByFullSql(sql);
			List<String> ids = new ArrayList<String>();
			if (maps != null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (Map map : maps)
				{
					String proinst_id = String.valueOf(map.get("PROINST_ID"));
					if (!ids.contains(proinst_id))
					{
						ids.add(proinst_id);
						String del_msg = String.valueOf(map.get("DEL_MSG"));
						del_msg = del_msg == null ? "" : del_msg;
						del_msg = del_msg.replace("'", "''");
						String del_uid = String.valueOf(map.get("DEL_UID"));
						del_uid = del_uid == null ? "" : del_uid;
						del_uid = del_uid.replace("'", "''");
						String del_uname = String.valueOf(map.get("DEL_UNAME"));
						del_uname = del_uname == null ? "" : del_uname;
						del_uname = del_uname.replace("'", "''");
						Date tbrq = (Date) map.get("TBRQ");
						T_deleted_proinst obj = new T_deleted_proinst();
						obj.setProinst_id(proinst_id);
						obj.setDel_msg(del_msg);
						obj.setDel_uid(del_uid);
						obj.setDel_uname(del_uname);
						obj.setTbrq(tbrq);
						obj.setIs_del(0);
						// 被删除项目同步到前置机
						session.save(obj);
						session.flush();
						String rq = "to_date ( '" + sdf.format(tbrq) + "' , 'YYYY-MM-DD HH24:MI:SS' )";
						// 把信息同时记录到内网表里
						String insert_sql = "insert into bdc_workflow.t_deleted_proinst (PROINST_ID,DEL_MSG,DEL_UID,DEL_UNAME,TBRQ) values ('" + proinst_id + "','" + del_msg + "','" + del_uid + "','" + del_uname + "'," + rq + ")";
						timerDao.getSession().createSQLQuery(insert_sql).executeUpdate();
						timerDao.flush();
					}
				}
			}
			// 把前置机上被删除项目进度信息删除
			session.createSQLQuery("delete T_SCHEDULE t where t.proinst_id in (select t1.proinst_id from T_DELETED_PROINST t1)").executeUpdate();
			session.flush();
			tran.commit();
		}
		catch (Exception ex)
		{
			tran.rollback();
			System.out.println("同步被删除项目信息到前置机失败，详情：");
			ex.printStackTrace();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
			if (timerDao != null)
			{
				timerDao.close();
				timerDao = null;
			}
		}
	}

	/**
	 * 同步历史证书数据到前置机数据库中
	 */
	@SuppressWarnings("rawtypes")
	private void synchrotbCertificate_Ls()
	{
		Session session = null;
		Transaction tra = null;
		TimerDao timerDao = null;
		try
		{
			timerDao = new TimerDao();
			List<Map> lsData = getCertificate_Ls(timerDao);
			if (lsData != null && lsData.size() > 0)
			{
				Date date = new Date(); // 获取同步时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				session = DButil.newGetMscSession();
				tra = session.beginTransaction();// 开启事务
				for (int j = 0; j < lsData.size(); j++)
				{
					// 前置端历史信息表
					T_certificate_ls ls = new T_certificate_ls();
					String zsbh = String.valueOf(lsData.get(j).get("ZSBH")); // 证书编号
					String qzh = String.valueOf(lsData.get(j).get("BDCQZH"));// 不动产权证号
					String id = UUID.randomUUID().toString();
					ls.setId(id);
					ls.setZsbh(zsbh);
					ls.setBdcqzh(qzh);
					ls.setTbsj(date);
					ls.setIs_del(0);
					session.save(ls);
					session.flush();
					String rq = "to_date ( '" + sdf.format(date) + "' , 'YYYY-MM-DD HH24:MI:SS' )";
					String sql_rz = "insert into BDC_WORKFLOW.T_CERTIFICATE_LS (id,zsbh,BDCQZH,TBSJ,is_del) values('" + id + "','" + zsbh + "','" + qzh + "'," + rq + ",0)";
					timerDao.getSession().createSQLQuery(sql_rz).executeUpdate();
					timerDao.flush();
				}
				// 删除前置机上历史证书信息
				session.createSQLQuery("delete T_CERTIFICATE_INLINE t where t.certificateno in (select b.zsbh from T_CERTIFICATE_LS  b)").executeUpdate();
				session.flush();
				tra.commit();
			}
		}
		catch (Exception ex)
		{
			tra.rollback();
			System.out.println("同步历史证书信息到前置机失败，详情：");
			ex.printStackTrace();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
			if (timerDao != null)
			{
				timerDao.close();
				timerDao = null;
			}
		}
	}

	/**
	 * 获取历史证书信息
	 * 
	 * @author duanyf
	 */
	@SuppressWarnings({ "unused", "rawtypes", "null" })
	private List<Map> getCertificate_Ls(TimerDao dao) throws Exception
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct t.zsbh, t.bdcqzh,t.szsj from BDCK.BDCS_ZS_LS t where  t.zsbh is not null and t.szsj is not null ");
		sql.append("and not exists (select a.zsbh from BDCK.BDCS_ZS_XZ a where a.zsbh is not null  and t.zsbh = a.zsbh) ");
		sql.append(" and not exists (select b.zsbh from BDC_WORKFLOW.T_CERTIFICATE_LS b where b.zsbh is not null and t.zsbh =b.zsbh ) ");
		sql.append("and rownum < 1000  order by t.szsj");
		return dao.getDataListByFullSql(sql.toString());
	}

	/**
	 * 获取权利人短信推送信息
	 * 
	 * @author duanyf
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private List<Map> getDx_Qlr(TimerDao dao) throws Exception
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY F.PROINST_ID, F.ACTDEF_ID, F.ACTINST_NAME, F.QLRMC ORDER BY F.ACTINST_START DESC) RT,");
		sql.append("F.PROLSH, F.QLRMC,F.DH, F.ACTINST_NAME,F.ACTINST_START,F.ACTINST_END ");
		sql.append("FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROLSH, D.QLRMC, A.ACTINST_NAME ORDER BY B.PROLSH ASC) RN,");
		sql.append(" B.PROLSH,D.QLRMC, D.DH, A.ACTINST_NAME, A.ACTINST_START,A.ACTINST_END,A.ACTDEF_ID,A.PROINST_ID FROM BDC_WORKFLOW.WFI_ACTINST A ");
		sql.append("INNER JOIN (SELECT PROINST_ID,PRODEF_ID, PRODEF_NAME,PROLSH,PROJECT_NAME FROM BDC_WORKFLOW.WFI_PROINST WHERE PRODEF_ID IN (SELECT PRODEF_ID FROM BDC_WORKFLOW.WFD_PRODEF)) B ");
		sql.append(" ON A.PROINST_ID = B.PROINST_ID  INNER JOIN (SELECT T1.XMBH,T1.PROJECT_ID,T1.YWLSH,T2.QLRMC,T2.DH  FROM BDCK.BDCS_XMXX T1 INNER JOIN BDCK.BDCS_QLR_GZ T2 ");
		sql.append("ON T1.XMBH = T2.XMBH  WHERE T2.DH IS NOT NULL AND T1.YWLSH IS NOT NULL AND T2.QLRMC IS NOT NULL ) D  ON B.PROLSH = D.YWLSH  WHERE EXISTS (SELECT ACTIVITYNAME FROM  BDC_WORKFLOW.INLINE_DXTSPZ ");
		sql.append("WHERE A.ACTINST_NAME = ACTIVITYNAME AND  A.ACTINST_START > TBSTARTTIME)) F  WHERE RN = 1 AND NOT EXISTS (SELECT E.ACTIVITYNAME FROM BDC_WORKFLOW.INLINE_DXTS E WHERE F.ACTINST_NAME = E.ACTIVITYNAME AND F.PROLSH =E.YWLSH AND F.QLRMC =E.QLR )");
		sql.append(" AND F.ACTINST_START IS NOT NULL AND F.ACTINST_NAME IS NOT NULL ORDER BY PROLSH, QLRMC, ACTINST_START DESC)  WHERE RT = 1  ORDER BY PROLSH, QLRMC, ACTINST_START ASC ");
		return dao.getDataListByFullSql(sql.toString());
	}

	/**
	 * 同步权利人短信推送信息至前置端
	 * 
	 * @author daunyf
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void synchroDx_qlr()
	{
		Session session = null;
		Transaction tra = null;
		TimerDao timerDao = null;
		try
		{
			timerDao = new TimerDao();
			List<Map> lsData = getDx_Qlr(timerDao);
			if (lsData != null && lsData.size() > 0)
			{
				// 获取同步时间
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				session = DButil.newGetMscSession();
				// 开启事务
				tra = session.beginTransaction();
				for (int j = 0; j < lsData.size(); j++)
				{
					// 前置端权利人短信推送信息表
					Inline_dxts dxts = new Inline_dxts();
					String ywlsh = String.valueOf(lsData.get(j).get("PROLSH"));
					String qlr = String.valueOf(lsData.get(j).get("QLRMC"));
					String dh = String.valueOf(lsData.get(j).get("DH"));
					String hj = String.valueOf(lsData.get(j).get("ACTINST_NAME"));
					String ids = UUID.randomUUID().toString();
					String id = ids.replace("-", "");
					String qssj = sdf.format(lsData.get(j).get("ACTINST_START"));
					String zzsj = "";
					dxts.setProstart(sdf.parse(qssj));
					dxts.setId(id);
					dxts.setYwlsh(ywlsh);
					dxts.setQlr(qlr);
					dxts.setQlr_tel(dh);
					dxts.setActivityname(hj);
					dxts.setTszt(0);
					dxts.setTbsj(date);
					if (lsData.get(j).get("ACTINST_END") == null)
					{
						session.save(dxts);
						session.flush();
					}
					else
					{
						zzsj = sdf.format(lsData.get(j).get("ACTINST_END"));
						dxts.setProend(sdf.parse(zzsj));
						session.save(dxts);
						session.flush();
					}

					String rq = "to_date ( '" + sdf.format(date) + "' , 'YYYY-MM-DD HH24:MI:SS' )";
					String start = "to_date ( '" + qssj + "' , 'YYYY-MM-DD HH24:MI:SS' )";
					if (zzsj != "")
					{
						String end = "to_date ( '" + zzsj + "' , 'YYYY-MM-DD HH24:MI:SS' )";
						String sql_rz = "insert into BDC_WORKFLOW.INLINE_DXTS (id,ywlsh,qlr,qlr_tel,activityname,prostart,proend,tszt,tbsj) values('" + id + "','" + ywlsh + "','" + qlr + "','" + dh + "','" + hj + "'," + start + "," + end + ",1," + rq + ")";
						timerDao.getSession().createSQLQuery(sql_rz).executeUpdate();
					}
					else
					{
						String sql_rz = "insert into BDC_WORKFLOW.INLINE_DXTS (id,ywlsh,qlr,qlr_tel,activityname,prostart,tszt,tbsj) values('" + id + "','" + ywlsh + "','" + qlr + "','" + dh + "','" + hj + "'," + start + ",1," + rq + ")";
						timerDao.getSession().createSQLQuery(sql_rz).executeUpdate();
					}
					timerDao.flush();
				}
				tra.commit();
				System.out.println("同步权利人短信推送信息至前置机成功，共计" + lsData.size() + "条。");
			}
			else
			{
				System.out.println("无可同步权利人短信推送信息至前置机！");
			}
		}
		catch (Exception e)
		{
			tra.rollback();
			System.out.println("同步权利人短信推送信息到前置机失败，详情：");
			e.printStackTrace();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
			if (timerDao != null)
			{
				timerDao.close();
				timerDao = null;
			}
		}
	}

	/**
	 * 获取登记系统受理项目与权利人关系信息
	 * 
	 * @author duanyf
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getXm_qlr(TimerDao dao) throws Exception
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *  FROM (SELECT ROW_NUMBER() OVER(PARTITION BY T1.YWLSH, T2.QLRMC, T2.ZJH ORDER BY T1.YWLSH ASC) RY,");
		sql.append("T1.XMBH,T1.XMBH||'-'||T2.QLRID AS XM,T1.YWLSH,T2.QLRMC,T2.ZJH, T2.ZJZL FROM BDCK.BDCS_XMXX T1  INNER JOIN BDCK.BDCS_QLR_GZ T2  ON T1.XMBH = T2.XMBH WHERE T2.ZJH IS NOT NULL ");
		sql.append(" AND T2.ZJZL IS NOT NULL AND T1.XMBH IS NOT NULL AND T1.YWLSH IS NOT NULL AND T2.QLRMC IS NOT NULL) F   WHERE RY = 1 ");
		sql.append("  AND NOT EXISTS (SELECT E.XMBH  FROM BDC_WORKFLOW.INLINE_PROJECT_QLR_LOG E WHERE F.XM = E.XMBH)  ORDER BY F.YWLSH ASC");
		return dao.getDataListByFullSql(sql.toString());
	}

	/**
	 * 同步登记系统受理项目与权利人关系信息同步至外网(桂林接口）
	 * 
	 * @author duanyf
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void project_Qlr()
	{
		Session session = null;
		Transaction tra = null;
		TimerDao timerDao = null;
		try
		{
			timerDao = new TimerDao();
			List<Map> lsData = getXm_qlr(timerDao);
			if (lsData != null && lsData.size() > 0)
			{
				// 获取同步时间
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				session = DButil.newGetMscSession();
				// 开启事务
				tra = session.beginTransaction();
				for (int j = 0; j < lsData.size(); j++)
				{
					// 前置端项目权利人关系表
					T_project_qlr qlrs = new T_project_qlr();
					String xmbh = String.valueOf(lsData.get(j).get("XMBH"));
					String xm = String.valueOf(lsData.get(j).get("XM"));//XMBH-QLRID
					String ywh = String.valueOf(lsData.get(j).get("YWLSH"));
					String qlr = String.valueOf(lsData.get(j).get("QLRMC"));
					String zjh = String.valueOf(lsData.get(j).get("ZJH"));// 证件号
					String zjlx = String.valueOf(lsData.get(j).get("ZJZL"));// 证件类型
					String ids = UUID.randomUUID().toString();
					String id = ids.replace("-", "");
					qlrs.setId(id);
					qlrs.setQlr(qlr);
					qlrs.setXmbh(xmbh);
					qlrs.setYwh(ywh);
					qlrs.setZjh(zjh);
					qlrs.setZjlx(zjlx);
					qlrs.setZt(0);
					session.save(qlrs);
					session.flush();
					String tbrq = "to_date ( '" + sdf.format(date) + "' , 'YYYY-MM-DD HH24:MI:SS' )";
					String sql_rz = "insert into BDC_WORKFLOW.INLINE_PROJECT_QLR_LOG (id,xmbh,tbrq) values('" + id + "','" + xm + "'," + tbrq + ")";
					timerDao.getSession().createSQLQuery(sql_rz).executeUpdate();
					timerDao.flush();
				}
				tra.commit();
			}
		}
		catch (Exception e)
		{
			tra.rollback();
			e.printStackTrace();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
			if (timerDao != null)
			{
				timerDao.close();
				timerDao = null;
			}
		}
	}
}
