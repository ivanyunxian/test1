/**
 * 续封登记处理类
 */
package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、查封_续封登记（现房）
 */

/**
 * 续封登记处理类
 * 
 * @ClassName: CFDJ_XF_HouseHandler
 * @author liushufeng
 * @date 2015年8月21日 下午7:53:04
 */
public class CFDJ_XF_HouseHandler extends DJHandlerBase implements DJHandler {

	public CFDJ_XF_HouseHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String qlid) {
		boolean bSuccess = true;

		if (!StringHelper.isEmpty(qlid)) {
			String[] qlids = qlid.split(",");
			for (int i = 0; i < qlids.length; i++) {
				String id = qlids[i];
				if (!StringHelper.isEmpty(id))
					if (bSuccess) {
						bSuccess = addbdcdy(id);
					}
			}
		} else {
			bSuccess = false;
			setErrMessage("请选择一条查封信息");
		}
		return bSuccess;
	}

	private boolean addbdcdy(String qlid) {
		boolean bSuccess = false;
		CommonDao dao = this.getCommonDao();
		BDCS_QL_XZ xz_ql = getQLXZbyQLID(dao, qlid);
		BDCS_FSQL_XZ xz_fsql = getFSQLXZbyQLID(dao, qlid);
		BDCS_DJDY_XZ xz_djdy = getDJDYXZbyDJDYID(dao, xz_ql.getDJDYID());

		if (xz_djdy != null) {
			if (ValidateDup(dao, xz_djdy.getBDCDYID()))// 严重是否重复插入
				return true;

			// 现状库拷贝到工作库
			BDCS_QL_GZ gz_ql = new BDCS_QL_GZ();
			BDCS_FSQL_GZ gz_fsql = new BDCS_FSQL_GZ();
			BDCS_DJDY_GZ gz_djdy = new BDCS_DJDY_GZ();

			ObjectHelper.copyObject(xz_ql, gz_ql);
			ObjectHelper.copyObject(xz_fsql, gz_fsql);

			gz_ql.setDJLX(super.getDjlx().Value);
			gz_ql.setQLLX(super.getQllx().Value);
			gz_ql.setXMBH(super.getXMBH());
			gz_ql.setLYQLID(qlid);
			gz_ql.setCASENUM("");

			// 工作库附属权利信息重新赋值，参考super.createFSQL写法
			if (StringHelper.isEmpty(gz_fsql.getCFWJ())) {
				gz_fsql.setCFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));//查封文件
			}
			if (StringHelper.isEmpty(gz_fsql.getCFFW())) {
				gz_fsql.setCFFW(ConfigHelper.getNameByValue("DEFAULTCFFW"));//查封范围
			}

			gz_fsql.setXMBH(super.getXMBH());
			gz_fsql.setCFLX(CFLX.XF.Value);
			String newqlid = SuperHelper.GeneratePrimaryKey();
			String newfsqlid = SuperHelper.GeneratePrimaryKey();
			gz_ql.setId(newqlid);
			gz_ql.setFSQLID(newfsqlid);
			gz_fsql.setQLID(newqlid);
			gz_fsql.setId(newfsqlid);

			BDCS_XMXX xmxx=dao.get(BDCS_XMXX.class, super.getXMBH());
			if(xmxx!=null&&(!"1".equals(ConfigHelper.getNameByValue("InheritCFSJ")))){
				gz_fsql.setCFSJ(xmxx.getSLSJ());//查封时间
			}
			// 工作库登记单元信息重新赋值，参考super.createDJDYfromXZ写法
			gz_djdy = new BDCS_DJDY_GZ();
			gz_djdy.setXMBH(super.getXMBH());
			gz_djdy.setDJDYID(xz_djdy.getDJDYID());
			gz_djdy.setBDCDYID(xz_djdy.getBDCDYID());
			gz_djdy.setBDCDYLX(super.getBdcdylx().Value);
			gz_djdy.setBDCDYH(xz_djdy.getBDCDYH());
			gz_djdy.setLY(DJDYLY.XZ.Value);

			// 保存
			dao.save(gz_djdy);
			dao.save(gz_ql);
			dao.save(gz_fsql);
			bSuccess = true;
		}
		dao.flush();
		return bSuccess;
	}

	// 验证是否重复 重复返回TRUE否则返回FALSE
	private boolean ValidateDup(CommonDao dao, String bdcdyid) {
		String hql = "BDCDYID='" + bdcdyid + "' AND XMBH='" + super.getXMBH()
				+ "'";// 通过不动产单元ID和项目编号判断是否重复

		List<BDCS_DJDY_GZ> list = dao.getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	// 根据权利ID返回现状库权利
	private BDCS_QL_XZ getQLXZbyQLID(CommonDao dao, String qlid) {
		String hql = MessageFormat.format(" QLID=''{0}''", qlid);
		List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BDCS_QL_XZ();
	}

	// 根据权利ID返回现状库附属权利
	private BDCS_FSQL_XZ getFSQLXZbyQLID(CommonDao dao, String qlid) {
		String hql = MessageFormat.format(" QLID=''{0}''", qlid);
		List<BDCS_FSQL_XZ> list = dao.getDataList(BDCS_FSQL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 根据权利ID返回现状库登记单元
	private BDCS_DJDY_XZ getDJDYXZbyDJDYID(CommonDao dao, String djdyid) {
		String hql = MessageFormat.format(" DJDYID=''{0}''", djdyid);
		List<BDCS_DJDY_XZ> list_djdy = dao.getDataList(BDCS_DJDY_XZ.class, hql);
		if (list_djdy != null && list_djdy.size() > 0) {
			return list_djdy.get(0);// 一个登记ID对应多个不动产单元的先不考虑
		}
		return null;
	}

	@Override
	public boolean writeDJB() {
		String dbr = Global.getCurrentUserName();
		Date date = new Date();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					// 工作层查封权利
					Rights gzrights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ,
							getXMBH(), djdyid);
					if (gzrights != null) {
						// 来源权利ID
						String lyqlid = gzrights.getLYQLID();
						if (!StringHelper.isEmpty(lyqlid)) {

							//是否自动解除之前的查封,0:解除，1：不解除
							String isremoveseal = "";
							String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
							List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
							if ((listCode != null) && (listCode.size() > 0)) {
								isremoveseal = ((WFD_MAPPING) listCode.get(0)).getISREMOVESEAL();
							}
							
							if ("0".equals(isremoveseal)) {
								// 先删除现状层的权利
								Rights xzrights = RightsTools.deleteRights(
										DJDYLY.XZ, lyqlid);
								RightsTools.deleteSubRights(DJDYLY.XZ,
										xzrights.getFSQLID());

								// 再修改历史层的权利							
								SubRights lssubrights = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, lyqlid);
								if (lssubrights != null) {
									lssubrights.setZXFJ("由于续封，本查封自动注销");
									lssubrights.setZXDBR(dbr);
									lssubrights.setZXSJ(date);
									lssubrights.setZXDYYWH(super.getProject_id());
									lssubrights.setZXDYYY("由于续封，本查封自动注销");
									getCommonDao().update(lssubrights);
								}
							}
							

							gzrights.setDBR(dbr);
							gzrights.setDJSJ(date);
							gzrights.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));

							// 再把工作层拷贝到现状层和历史层
							Rights newxzrights = RightsTools.copyRightsOnly(
									DJDYLY.GZ, DJDYLY.XZ, gzrights);
							Rights newlsrights = RightsTools.copyRightsOnly(
									DJDYLY.GZ, DJDYLY.LS, gzrights);
							getCommonDao().save(newxzrights);
							getCommonDao().save(newlsrights);
							getCommonDao().update(gzrights);
						}
					}
					this.SetXMCFZT(djdyid, "01");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}

	/**
	 * 拷贝工作库的权利表，权利附属表到现状库和历史库中。比较特殊是，这里的登薄人，登薄时间是保存在附属权利表的注销人，注销时间
	 * 里作为解封人，解封时间使用。登记机构是放在权利附属表的解封机构里面 参考SUPER的CopyGZQLToXZAndLS写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean CopyGZQLToXZAndLS(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(this.getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class,
				xmbhFilter + " and " + strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				getCommonDao().save(bdcs_ql_xz);
			}
		}
		return true;
	}

	/**
	 * 移除现状层中的查封登记权利 参考SUPER的removeQLFromXZByALL写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean removeQLFromXZByCFDJ(String qlid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID='").append(qlid).append("'");		
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class,
				strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ bdcs_ql_xz = qls.get(iql);
				if (bdcs_ql_xz != null) {
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(
							BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						getCommonDao().deleteEntity(bdcs_fsql_xz);
					}
					getCommonDao().deleteEntity(bdcs_ql_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 移除历史层中的查封登记权利，在现在层中只保留合并后的解封信息 参考SUPER的removeQLFromXZByALL写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean removeQLFromLSByCFDJ(String qlid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID='").append(qlid).append("' ");
		// 在权利历史层中获取到查封的信息
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_LS> qls = getCommonDao().getDataList(BDCS_QL_LS.class,
				strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_LS bdcs_ql_ls = qls.get(iql);
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(
							BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
					if (bdcs_fsql_ls != null) {
						getCommonDao().deleteEntity(bdcs_fsql_ls);
					}
					getCommonDao().deleteEntity(bdcs_ql_ls);// 删除掉 历史相关的权利和附属权利
				}
			}
		}
		return true;
	}

	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除登记单元索引
			baseCommonDao.deleteEntity(djdy);

			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format(
					"XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);

		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {

	}

	@Override
	public void removeQLR(String qlid, String qlrid) {

	}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return super.exportXML_CF(path, actinstID, null);
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy
						.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy
						.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,
						djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(
						ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(
						ConstValue.DJDYLY.GZ, bdcql.getId());
//				String lyqlid = bdcql.getLYQLID();//使用事物以后出错，此处注释，在同通用方法里面给导出对象赋值时 qlid=lyqlid
//				bdcql.setId(lyqlid);
				List<RightsHolder> bdcqlrs = null;
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy,
						bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1,
						djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
