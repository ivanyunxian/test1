package com.supermap.wisdombusiness.synchroinline.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.model.DatuminstInfo;
import com.supermap.wisdombusiness.synchroinline.model.DicInfo;
import com.supermap.wisdombusiness.synchroinline.model.DicInfo.DicItem;
import com.supermap.wisdombusiness.synchroinline.model.EasyuiDataGrid;
import com.supermap.wisdombusiness.synchroinline.model.PageResult;
import com.supermap.wisdombusiness.synchroinline.model.Pro_attachment;
import com.supermap.wisdombusiness.synchroinline.model.Pro_datuminst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_fwxx;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proinst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proposerinfo;
import com.supermap.wisdombusiness.synchroinline.model.Pro_slxmsh;
import com.supermap.wisdombusiness.synchroinline.model.SqrInfo;
import com.supermap.wisdombusiness.synchroinline.util.FTPUtils;
import com.supermap.wisdombusiness.workflow.model.WFD_SPMB;

@Service
public class InlineProjectService
{

	@Autowired
	CommonDao dao;
	
	@Autowired
	CommonDaoInline baseCommonDaoInline;
	
	String spyj_flid = "70c51143056a45b998bfbd3d75d2ab8d";

	public InlineProjectService()
	{
	}

	public Session getSession()
	{
//		return DButil.GetMscSession();
		return baseCommonDaoInline.getCurrentSession();
	}

	/**
	 * 根据受理申请id获取受理项目
	 *
	 * @param slsqId
	 *            受理申请id
	 * @return
	 * @throws Exception
	 */
	public Pro_proinst getProinst(String slsqId) throws Exception
	{
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		return (Pro_proinst) this.getSession().get(Pro_proinst.class, slsqId);
	}

	public Pro_slxmsh getSlxmsh(String slsqId) throws Exception
	{
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		List<Pro_slxmsh> shs = this.getSession().createQuery("from Pro_slxmsh where slxm_id='" + slsqId + "'").list();
		return shs == null || shs.isEmpty() ? null : shs.get(0);
	}

	/**
	 * 根据受理申请id获取申请人列表
	 *
	 * @param slsqId
	 *            slsqId 受理申请id
	 * @return
	 */
	public List<Pro_proposerinfo> getSqrsBySlsqId(String slsqId)
	{
		return this.getSession().createQuery("from Pro_proposerinfo where proinst_id='" + slsqId + "' order by sqr_sxh asc").list();
	}

	/**
	 * 通过受理申请id获取附件模板资料
	 *
	 * @param slsqId
	 * @return
	 */
	public List<Pro_datuminst> getDatuminsts(String slsqId)
	{
		return this.getSession().createQuery("from Pro_datuminst where proinst_id='" + slsqId + "' order by code asc").list();
	}

	/**
	 * 将附件模板实例转换为视图对象，附带当前模板下文件存在数量
	 *
	 * @param datuminsts
	 * @return
	 */
	public List<DatuminstInfo> getByDatuminsts(List<Pro_datuminst> datuminsts)
	{
		List<DatuminstInfo> result = new ArrayList<DatuminstInfo>();
		for (Pro_datuminst datuminst : datuminsts)
		{
			DatuminstInfo info = new DatuminstInfo();
			info.setBz(datuminst.getBz());
			info.setCode(datuminst.getCode());
			info.setCount(datuminst.getCount());
			info.setId(datuminst.getId());
			info.setName(datuminst.getName());
			info.setProinst_id(datuminst.getName());
			info.setRequired(datuminst.getRequired());
			List<Pro_attachment> files = getFilesBydatuminst(datuminst.getId());
			info.setCur_count(files.size());
			result.add(info);
		}
		return result;
	}

	/**
	 * 获取指定资料模板实例的附件列表
	 *
	 * @param datuminst_id
	 *            资料模板实例id
	 * @return
	 */
	public List<Pro_attachment> getFilesBydatuminst(String datuminst_id)
	{
		return this.getSession().createQuery("from Pro_attachment where datuminst_id='" + datuminst_id + "' order by created asc").list();
	}

	/**
	 * 格式化申请人信息，字典类型转换
	 *
	 * @param sqrs
	 * @return
	 */
	public List<SqrInfo> formatSqr(List<Pro_proposerinfo> sqrs)
	{
		List<SqrInfo> new_sqrs = new ArrayList<SqrInfo>();
		for (Pro_proposerinfo sqr : sqrs)
		{
			SqrInfo new_sqr = new SqrInfo();
			new_sqr.setSqr_adress(sqr.getSqr_adress());
			new_sqr.setSqr_gyfs(sqr.getSqr_gyfs());
			new_sqr.setSqr_gyfe(sqr.getSqr_gyfe());
			new_sqr.setSqr_lx(sqr.getSqr_lx());
			new_sqr.setSqr_name(sqr.getSqr_name());
			new_sqr.setSqr_qt_adress(sqr.getSqr_qt_adress());
			new_sqr.setSqr_qt_dlr_name(sqr.getSqr_qt_dlr_name());
			new_sqr.setSqr_qt_fr_name(sqr.getSqr_qt_fr_name());
			new_sqr.setSqr_qt_tel(sqr.getSqr_qt_tel());
			new_sqr.setSqr_qt_zjh(sqr.getSqr_qt_zjh());
			new_sqr.setSqr_qt_zjlx(sqr.getSqr_qt_zjlx());
			new_sqr.setSqr_sxh(sqr.getSqr_sxh());
			new_sqr.setSqr_tel(sqr.getSqr_tel());
			new_sqr.setSqr_zjh(sqr.getSqr_zjh());
			new_sqr.setSqr_zjlx(sqr.getSqr_zjlx());
			// 格式化共有方式
			String gyfs = sqr.getSqr_gyfs();
			String gyfs_name = ConstHelper.getNameByValue("GYFS", gyfs);
			new_sqr.setSqr_gyfs_format(gyfs_name);
			// 格式化证件类型
			String zjlx = sqr.getSqr_zjlx();
			String zjlx_name = ConstHelper.getNameByValue("ZJLX", zjlx);
			new_sqr.setSqr_zjlx_format(zjlx_name);
			// 格式化其他人证件类型
			String qt_zjlx = sqr.getSqr_qt_zjlx();
			String qt_zjlx_name = ConstHelper.getNameByValue("ZJLX", qt_zjlx);
			new_sqr.setSqr_qt_zjlx_format(qt_zjlx_name);
			new_sqrs.add(new_sqr);
		}
		return new_sqrs;
	}

	/**
	 * 通过文件id从ftp下载文件，返回输出流。
	 * (广西区厅不适用FTP)
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream getFileOutputStream(String fileId) throws Exception
	{
		ByteArrayOutputStream outStream = null;
		try
		{
			Pro_attachment file = (Pro_attachment) this.getSession().get(Pro_attachment.class, fileId);
			String syspath = ConfigHelper.getNameByValue("filepath");
			String fileName = syspath+File.separator + file.getPath() +File.separator + file.getName() + file.getSuffix();
			File this_file = new File(fileName);
		    FileInputStream in = new FileInputStream(this_file);
		    outStream = new ByteArrayOutputStream();
		    byte [] buffer = new byte[1024];  
		    int len = 0;
		    while( (len = in.read(buffer)) != -1){  
		    	outStream.write(buffer, 0, len);  
		    }  
//			outStream = FTPUtils.downloadFile(file.getPath(), fileName);
//			FTPUtils.closeFtp();
		}
		catch (Exception ex)
		{
			FTPUtils.closeFtp();
			throw ex;
		}
		return outStream;
	}

	/**
	 * 通过文件id获取资料模板对象
	 *
	 * @param fileId
	 * @return
	 */
	public Pro_datuminst getDatuminstByFileId(String fileId)
	{
		Pro_attachment file = (Pro_attachment) this.getSession().get(Pro_attachment.class, fileId);
		return (Pro_datuminst) this.getSession().get(Pro_datuminst.class, file.getDatuminst_id());
	}

	public Pro_attachment getFileId(String fileId)
	{
		return (Pro_attachment) this.getSession().get(Pro_attachment.class, fileId);
	}

	public DicInfo getDicInfo(String dic_key)
	{
		DicInfo dic = new DicInfo();
		Map<String, String> map = ConstHelper.getDictionary(dic_key);
		if (map != null)
		{
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				DicItem item = new DicItem();
				item.setCode(entry.getKey());
				item.setText(entry.getValue());
				dic.getItems().add(item);
			}
		}
		return dic;
	}

	public EasyuiDataGrid QueryEasyuiDataGrid(String sql)
	{
		List<Map> data = this.Query(sql);
		EasyuiDataGrid datagrid = new EasyuiDataGrid(data.size(), data);
		return datagrid;
	}

	public PageResult<EasyuiDataGrid> QueryEasyuiDataGrid(String sql, int pageIndex, int pageSize)
	{
		PageResult<List<Map>> data = this.Query(sql, pageIndex, pageSize);
		EasyuiDataGrid grid = new EasyuiDataGrid(data.getTotal(), data.getData());
		PageResult<EasyuiDataGrid> datagrid = new PageResult<EasyuiDataGrid>(data.getTotal(), data.getPageIndex(), data.getPageSize(), grid);
		return datagrid;
	}

	public List<Map> Query(String sql)
	{
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}

	public PageResult<List<Map>> Query(String sql, int pageIndex, int pageSize)
	{
		String[] sqls = this.GetPagingQuerySql(sql, pageIndex, pageSize);
		List<Map> data = this.Query(sqls[0]);
		List<Map> countData = this.Query(sqls[1]);
		int total = Integer.parseInt(countData.get(0).get("COUNT").toString());
		PageResult<List<Map>> result = new PageResult<List<Map>>(total, pageIndex, pageSize, data);
		return result;
	}

	/*
	 * 传入完整的sql查询语句，返回分页sql数组，第一个元素为分页查询sql,第二个元素为查询总数的sql。
	 */
	private String[] GetPagingQuerySql(String sql, int pageIndex, int pageSize)
	{

		int startIndex = (pageIndex - 1) * pageSize + 1;
		int endIndex = startIndex + pageSize - 1;
		String innerSql = sql;
		String middelSql = MessageFormat.format("SELECT A.*, ROWNUM RN FROM ({0}) A WHERE ROWNUM <= {1}", innerSql, String.format("%d", endIndex));
		String outerSql = MessageFormat.format("SELECT * FROM ({0}) WHERE RN >= {1}", middelSql, String.format("%d", startIndex));
		String out_total_sql = MessageFormat.format("select count(*) as count from ({0})", sql);
		return new String[] { outerSql, out_total_sql };
	}

	/**
	 * 获取在线受理项目审核意见模板，按序号升序排序
	 *
	 * @return
	 */
	public List<WFD_SPMB> getSpyjs()
	{
		return dao.getDataList(WFD_SPMB.class, "Staff_Id='" + spyj_flid + "' order by xh");
	}

	/**
	 * 保存或更新审批意见
	 *
	 * @param id
	 * @param spyj
	 */
	public WFD_SPMB saveOrUpdateSpyj(String id, String spyj)
	{
		id = id == null ? "" : id;
		// 新增
		WFD_SPMB obj = null;
		spyj = spyj.replace("'", "''");
		List<WFD_SPMB> spmbs = dao.getDataList(WFD_SPMB.class, "Staff_Id='" + spyj_flid + "' and Spcontent='" + spyj + "'");
		obj = spmbs == null || spmbs.size() == 0 ? null : spmbs.get(0);
		if (obj == null)
		{
			obj = new WFD_SPMB();
			// 获取最大的序号
			List<Map> xh_maps = dao.getDataListByFullSql("select nvl(max(xh),0) as xh from bdc_workflow.WFD_SPMB t where t.staff_id='" + spyj_flid + "'");
			String xh = xh_maps.get(0).get("XH").toString();
			Integer new_xh = Integer.valueOf(xh) + 1;
			obj.setXh(String.valueOf(new_xh));
		}
		obj.setSpcontent(spyj);
		obj.setStaff_Id(spyj_flid);
		obj.setYxbz(1);
		dao.saveOrUpdate(obj);
		dao.flush();
		return obj;
	}

	/**
	 * 删除审批意见
	 *
	 * @param id
	 *            审批意见id
	 */
	public void delSpyj(String id)
	{
		if (id != null)
		{
			dao.delete(WFD_SPMB.class, id);
			dao.flush();
		}
	}
	/**
	 * 根据完整SQL语句一次新查询所有数据，参数化查询
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午5:09:02
	 * @param fulSql
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDataListByFullSql(String fulSql, Map<String, String> paramMap) {
		Query query = this.getSession().createSQLQuery(fulSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setProperties(paramMap);
		@SuppressWarnings("unchecked")
		List<Map> list = query.list();
		return list;
	}
	
	/**
	 * 查询总数count，参数化不能传完整的sql语句，要从from开始写例如： from A LEFT JOIN B ON A.XX=B.xxx
	 * WHERE XXXXX XXXXXXX
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月11日下午9:33:51
	 * @param fromSql
	 * @return
	 */
	public long getCountByFullSql(String fromSql, Map<String, String> map) {
		String fullSql = "select count(1) " + fromSql;

		SQLQuery query = this.getSession().createSQLQuery(fullSql);
		query.setProperties(map);
		String strCount = query.uniqueResult().toString();
		Long longcount = Long.valueOf(strCount);
		return (long) longcount;
	}

	/**
     * 通过受理项目编号获取房屋信息
     *
     * @param slsqId
     * @return
     */
    public List<Pro_fwxx> getFwxxBySlsqId(String slsqId) {
    	List<Pro_fwxx> fwxxs = new ArrayList<Pro_fwxx>();
    	if (slsqId != null && !slsqId.isEmpty()) {
    		fwxxs = this.getSession().createQuery(" from Pro_fwxx where proinst_id='" + slsqId + "' " ).list();
    	}
    	return fwxxs;
    }
    /**
     * 通过受理项目编号获取审核意见信息
     *
     * @param slsqId
     * @return
     */
    public List<Pro_slxmsh> getShxxBySlsqId(String slsqId) {
    	List<Pro_slxmsh> shxxInfo = new ArrayList<Pro_slxmsh>();
    	if (slsqId != null && !slsqId.isEmpty()) {
    		shxxInfo = this.getSession().createQuery("FROM Pro_slxmsh WHERE SLXM_ID='" + slsqId + "' ORDER BY SH_QJDC_RQ,SH_XMSL_RQ DESC").list();
    	}
    	return shxxInfo;
    }


}
