package com.supermap.wisdombusiness.synchroinline.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.synchroinline.dao.TimerDao;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

/**
 * 短信推送配置
 * 
 * @author duanyf
 */

@Service
public class DxtspzService
{

	private static Boolean is_initTableAndView = false;

	@Autowired
	private CommonDao comDao;
	@Autowired
	private SmStaff smStaff;

	/**
	 * 获取短信推送配置项
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getDxpzS() throws Exception
	{
		TimerDao dao = null;
		List<Map> dxtsp = new ArrayList();
		try
		{
			dao = new TimerDao();
			String sql = "select t.id,t.activityname ,to_char(t.tbstarttime,'YYYY-MM-DD HH24:MI:SS') TBSTARTTIME,t.bz from BDC_WORKFLOW.INLINE_DXTSPZ t ";
			dxtsp = dao.getDataListByFullSql(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dao != null)
			{
				dao.close();
				dao = null;
			}
		}
		return dxtsp;
	}

	/**
	 * 保存或新增短信推送配置，不存在则新增，反之更新
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveOrUpDxpz(String id, String activityname, String tbstarttime, String bz) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			sdf.parse(tbstarttime);
		}
		catch (Exception e)
		{
			System.out.println("短信推送起始时间格式不正确，应为： yyyy-MM-dd HH:mm:ss ");
		}
		TimerDao dao = null;
		dao = new TimerDao();
		try
		{
			if (id == "")
			{
				id = StringUtil.getUUID().replace("-", "");
			}

			List<Map> dxtid = new ArrayList();
			String sql_id = "select t.* from BDC_WORKFLOW.INLINE_DXTSPZ t where t.id ='" + id + "'";
			dxtid = dao.getDataListByFullSql(sql_id);
			String sql_ids = "";
			if (dxtid == null || dxtid.isEmpty())
			{
				String rq = "to_date ( '" + tbstarttime + "' , 'YYYY-MM-DD HH24:MI:SS' )";
				if (bz != "")
				{
					sql_ids = "insert into BDC_WORKFLOW.INLINE_DXTSPZ  (id,activityname,tbstarttime,bz)  values('" + id + "','" + activityname + "'," + rq + ",'" + bz + "')";
				}
				else
				{
					sql_ids = "insert into BDC_WORKFLOW.INLINE_DXTSPZ  (id,activityname,tbstarttime)  values('" + id + "','" + activityname + "'," + rq + ")";
				}
			}
			else
			{
				String rq = "to_date ( '" + tbstarttime + "' , 'YYYY-MM-DD HH24:MI:SS' )";
				if (bz != "")
				{
					sql_ids = "update BDC_WORKFLOW.INLINE_DXTSPZ  set id ='" + id + "',activityname ='" + activityname + "',tbstarttime =" + rq + ",bz ='" + bz + "'  where id ='" + id + "'";
				}
				else
				{
					sql_ids = "update BDC_WORKFLOW.INLINE_DXTSPZ  set id ='" + id + "',activityname ='" + activityname + "',tbstarttime =" + rq + "  where id ='" + id + "'";
				}
			}
			dao.getSession().createSQLQuery(sql_ids).executeUpdate();
			dao.flush();
		}
		catch (Exception e)
		{
			System.out.println("短信推送配置失败，详情：");
			e.printStackTrace();
		}
		finally
		{
			if (dao != null)
			{
				dao.close();
				dao = null;
			}
		}
	}

	/**
	 * 删除短信配置模板
	 */
	@SuppressWarnings("null")
	public void del_Dxpz(String id)
	{
		if (id != "")
		{
			TimerDao dao = null;
			try
			{
				dao = new TimerDao();
				String sql = "delete  from BDC_WORKFLOW.INLINE_DXTSPZ t where t.id ='" + id + "'";
				dao.getSession().createSQLQuery(sql).executeUpdate();
				dao.flush();
			}
			catch (Exception e)
			{
				System.out.println("短信推送配置失败，详情：");
				e.printStackTrace();
			}
			finally
			{
				if (dao != null)
				{
					dao.close();
					dao = null;
				}
			}
		}
	}

	// 海口短信通知相关

	/**
	 * 初始化短信发送相关的表和视图，
	 */
	public void initTableAndView()
	{
		String sql = "select count(*) from all_all_tables t where t.tablespace_name='BDC_WORKFLOW' and t.table_name='INLINE_PROJECT_DXFS'";
		if (!is_initTableAndView)
		{
			long count = this.comDao.getCountByCFullSql(sql);
			if (count < 1)
			{
				//
				String create_sql = "CREATE TABLE BDC_WORKFLOW.inline_project_dxfs ( id VARCHAR2(36) NOT NULL,  xmbh VARCHAR2(36),  userid VARCHAR2(36),  username VARCHAR2(100),  fsrq DATE )";
				String create_sql1 = "ALTER TABLE BDC_WORKFLOW.inline_project_dxfs ADD CONSTRAINT PK_inline_project_dxfs_id PRIMARY KEY (id)";
				this.comDao.getCurrentSession().createSQLQuery(create_sql).executeUpdate();
				this.comDao.getCurrentSession().createSQLQuery(create_sql1).executeUpdate();
				// String fzhj = "缮证";
				// String view_sql =
				// "CREATE OR REPLACE VIEW BDC_WORKFLOW.v_linline_dxfs AS SELECT t.* FROM (SELECT t.xmbh, t.project_id, t.ywlsh, t.xmmc, t.djlx , t.qllx, t.slsj FROM bdck.bdcs_xmxx t INNER JOIN bdc_workflow.wfi_proinst t1 ON t.project_id = t1.file_number WHERE t.sfdb = 1 AND t1.proinst_id IN (SELECT t2.proinst_id FROM BDC_WORKFLOW.WFI_ACTINST t2 WHERE t2.actinst_status = 2 AND t2.actinst_name LIKE '%"
				// + fzhj +
				// "%') ORDER BY t.slsj DESC ) t LEFT JOIN bdc_workflow.inline_project_dxfs a ON t.xmbh = a.xmbh WHERE a.id IS NULL";
				// this.comDao.getCurrentSession().createSQLQuery(view_sql).executeUpdate();
			}
			String sql1="select count(*) from all_all_tables t where t.tablespace_name='BDC_WORKFLOW' and t.table_name='INLINE_DX_TEMPLET'";
			long count1 = this.comDao.getCountByCFullSql(sql1);
			if (count1 < 1)
			{
				String s1 = "CREATE TABLE BDC_WORKFLOW.inline_dx_templet ( id VARCHAR2(36) NOT NULL,  userid VARCHAR2(36),  username VARCHAR2(50),  templet VARCHAR2(200) )";
				String s2 = "ALTER TABLE BDC_WORKFLOW.inline_dx_templet ADD CONSTRAINT PK_inline_dx_templet_id PRIMARY KEY (id)";
				this.comDao.getCurrentSession().createSQLQuery(s1).executeUpdate();
				this.comDao.getCurrentSession().createSQLQuery(s2).executeUpdate();
			}
			is_initTableAndView = true;
			this.rebuildView(this.getFshj());
		}
	}

	public void rebuildView(String fshj)
	{
		fshj = fshj == null || fshj.isEmpty() ? "缮证" : fshj;
		String[] nodes = StringUtils.split(fshj, "|");
		List<String> filters = new ArrayList<String>();
		for (String node : nodes)
		{
			filters.add("t2.actinst_name LIKE '%" + node + "%'");
		}
		String where = StringUtils.join(filters, " or ");
		String view_sql = "CREATE OR REPLACE VIEW BDC_WORKFLOW.v_linline_dxfs AS SELECT t.*,decode(nvl(a.id,'0'),'0','0','1') as fszt FROM (SELECT t.xmbh, t.project_id, t.ywlsh, t.xmmc, t.djlx , t.qllx, t.slsj FROM bdck.bdcs_xmxx t INNER JOIN bdc_workflow.wfi_proinst t1 ON t.project_id = t1.file_number WHERE t.sfdb = 1 AND t1.proinst_id IN (SELECT t2.proinst_id FROM BDC_WORKFLOW.WFI_ACTINST t2 WHERE t2.actinst_status = 2 AND ( " + where + " )) ORDER BY t.slsj DESC ) t LEFT JOIN bdc_workflow.inline_project_dxfs a ON t.xmbh = a.xmbh";
		this.comDao.getCurrentSession().createSQLQuery(view_sql).executeUpdate();
	}

	/**
	 * 获取发送环节
	 * 
	 * @return
	 */
	private String getFshj()
	{
		String fshj = null;
		String inline_sms_option = ConfigHelper.getNameByValue("inline_sms_option");
		inline_sms_option = inline_sms_option == null ? "" : inline_sms_option.trim().toLowerCase();
		if (!inline_sms_option.isEmpty())
		{
			String[] params = StringUtils.split(inline_sms_option, "&");
			for (String p : params)
			{
				String[] keyval = p.split("=");
				if (keyval.length > 0)
				{
					String key = keyval[0].trim();
					String value = keyval.length >= 2 ? keyval[1].trim() : "";
					if ("fshj".equals(key))
					{
						fshj = value;
						break;
					}
				}
			}
		}
		return fshj;
	}

	/**
	 * 发送短信，
	 * 
	 * @param xmbh
	 *            待发送短信通知的项目编号
	 */
	public void send_dx(String xmbh) throws Exception
	{
		BaseMessageHandler.create(this.smStaff, this.comDao).send(xmbh);
	}

	/**
	 * 批量发送短信
	 * 
	 * @param xmbhs
	 *            待发送短信通知的项目编号集合
	 * @return
	 */
	public int send_dx(String[] xmbhs) throws Exception
	{
		return BaseMessageHandler.create(this.smStaff, this.comDao).send(xmbhs);
	}
	
	/**
	 * 获取个人的短信模板内容
	 * @return
	 */
	public String getTemplet()
	{
		String text = "";
		String uid = this.smStaff.getCurrentWorkStaff().getId();
		if (uid != null)
		{
			List<Map> maps = this.comDao.getDataListByFullSql("select TEMPLET from bdc_workflow.INLINE_DX_TEMPLET t where t.userid='" + uid + "'");
			if (!maps.isEmpty())
			{
				text = String.valueOf(maps.get(0).get("TEMPLET"));
			}
		}
		if(text==null || text.isEmpty())
		{
			text="{xm} 你好，您申请办理业务号为{ywh}的业务已完成缮证，请你于3个工作日内到不动产登记中心领取不动产权属证书。";
		}
		return text;
	}
	
	/**
	 * 编辑个人短信模板内容
	 * @param templet
	 * @throws Exception
	 */
	public void editTemplet(String templet) throws Exception
	{
		if (templet == null || templet.trim().isEmpty())
			throw new Exception("短信模板信息不能为空。");
		if (this.smStaff == null || this.smStaff.getCurrentWorkStaff() == null)
			throw new Exception("登录过期，请重新登录。");
		String uid = this.smStaff.getCurrentWorkStaff().getId();
		String username = this.smStaff.getCurrentWorkStaff().getUserName();
		long count = this.comDao.getCountByCFullSql("select count(*) from bdc_workflow.INLINE_DX_TEMPLET t where t.userid='" + uid + "'");
		if (count < 1)
		{
			// 新增
			String id = SuperHelper.GeneratePrimaryKey();
			String insert_sql = "insert into bdc_workflow.INLINE_DX_TEMPLET values ('" + id + "','" + uid + "','" + username + "','" + templet + "')";
			this.comDao.getCurrentSession().createSQLQuery(insert_sql).executeUpdate();
		}
		else
		{
			// 编辑
			String update_sql = "update  bdc_workflow.INLINE_DX_TEMPLET set templet='" + templet + "' where userid='" + uid + "' ";
			this.comDao.getCurrentSession().createSQLQuery(update_sql).executeUpdate();
		}
	}

}
