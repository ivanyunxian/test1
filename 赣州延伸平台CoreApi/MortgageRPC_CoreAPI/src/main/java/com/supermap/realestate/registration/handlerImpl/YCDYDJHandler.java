package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、在建工程抵押登记（待测）
 */
/**
 * 
 * 在建工程抵押处理类
 * 
 * @ClassName: YCDYDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:38:54
 */
public class YCDYDJHandler extends DYDJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YCDYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加现状预测户的不动产权单元
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日下午2:10:44
	 * @param bdcdyid
	 * @param bdcs_h_xzy
	 * @return
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bSuccess = true;
		if (StringHelper.isEmpty(bdcdyid)) {
			bSuccess = false;
		}
		String[] ids = bdcdyid.split(",");
		if (ids == null || ids.length <= 0)
			return false;
		CommonDao dao = this.getCommonDao();
		// if (IsCreateDJDY(bdcdyid))// 若创建，直接跳出
		// {
		// return bSuccess;
		// }
		// BDCS_H_XZY bdcs_h_xzy = dao.get(BDCS_H_XZY.class, bdcdyid);
		// if (bdcs_h_xzy != null) {
		// BDCS_DJDY_GZ djdy = createDJDYfromXZ(bdcdyid, bdcs_h_xzy);
		// if (djdy != null) {
		//
		// RealUnit unit = null;
		// try {
		// UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()),
		// djdy.getBDCDYID());
		// } catch (Exception e) {
		// }
		//
		// // 生成权利信息
		// BDCS_QL_GZ ql = super.createQL(djdy,unit);
		// // 生成附属权利
		// BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
		// ql.setFSQLID(fsql.getId());
		// fsql.setQLID(ql.getId());
		// // 设置抵押物类型
		// fsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
		// // 保存
		// dao.update(bdcs_h_xzy);
		// dao.save(djdy);
		// dao.save(ql);
		// dao.save(fsql);
		// bSuccess = true;
		// }
		// }

		for (String id : ids) {
			if (IsCreateDJDY(id))// 若创建，直接跳出
			{
				continue;
			} else {
				addbdcdy(id);
				dao.flush();
			}
		}
		return bSuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 20) {
			return writeDJBEx();
		}
		if (super.isCForCFING()) {
			return false;
		}

		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());// 拷贝登记单元

					// 拷贝预测户
					super.CopyYXZHToAndLS(bdcs_djdy_gz.getBDCDYID());
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	public boolean writeDJBEx() {
		if (isCForCFINGEx()) {
			return false;
		}
		try {
			CopyQLEX();// 拷贝权利
			CopyFSQLEX();// 拷贝权利
			CopyQLREX();// 拷贝权利人
			CopyQDZREX();// 拷贝权地证人
			CopyZSEX();// 拷贝证书
			CopyDJDYEX();// 拷贝登记单元
			CopyDYEX();// 拷贝单元
			SetDYZTEX();// 设定抵押状态
		} catch (Exception e) {
			throw new RuntimeException(); 
		}
		this.SetSFDB();// 设定登簿
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	@SuppressWarnings("rawtypes")
	public boolean isCForCFINGEx() {
		boolean b = false;
		StringBuilder builder_indeal = new StringBuilder();
		builder_indeal.append("SELECT DJDY.BDCDYLX,DJDY.BDCDYID,DJDY.LY ");
		builder_indeal
				.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
		builder_indeal
				.append("WHERE QL.QLID IS NOT NULL AND QL.DJLX='800' AND DJDY.XMBH='");
		builder_indeal.append(super.getXMBH());
		builder_indeal.append("'");

		StringBuilder builder_deal = new StringBuilder();
		builder_deal.append("SELECT DJDY.BDCDYLX,DJDY.BDCDYID,DJDY.LY ");
		builder_deal
				.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
		builder_deal
				.append("WHERE QL.DJSJ IS NULL AND QL.QLID IS NOT NULL AND  QL.DJLX='800' AND QL.QLLX='99' AND  DJDY.XMBH='");
		builder_deal.append(super.getXMBH());
		builder_deal.append("'");

		List<Map> list_deal = getCommonDao().getDataListByFullSql(
				builder_deal.toString());
		if (list_deal != null && list_deal.size() > 0) {
			for (Map djdy : list_deal) {
				String bdcdylx = StringHelper.formatObject(djdy.get("BDCDYLX"));
				String bdcdyid = StringHelper.formatObject(djdy.get("BDCDYID"));
				String djdyly = StringHelper.formatObject(djdy.get("ly"));
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),
						DJDYLY.initFrom(djdyly), bdcdyid);
				if (unit != null) {
					b = true;
					super.setErrMessage("不动产单元号为" + unit.getBDCDYH()
							+ "的单元已经被查封或正在办理查封，不能登簿！");
					break;
				}
			}
		} else {
			List<Map> list_indeal = getCommonDao().getDataListByFullSql(
					builder_indeal.toString());
			if (list_indeal != null && list_indeal.size() > 0) {
				for (Map djdy : list_indeal) {
					String bdcdylx = StringHelper.formatObject(djdy
							.get("BDCDYLX"));
					String bdcdyid = StringHelper.formatObject(djdy
							.get("BDCDYID"));
					String djdyly = StringHelper.formatObject(djdy.get("ly"));
					RealUnit unit = UnitTools.loadUnit(
							BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly),
							bdcdyid);
					if (unit != null) {
						b = true;
						super.setErrMessage("不动产单元号为" + unit.getBDCDYH()
								+ "的单元已经被查封或正在办理查封，不能登簿！");
						break;
					}
				}
			} else {
				if (BDCDYLX.H.equals(super.getBdcdylx())) {
					StringBuilder builder_deal_yc = new StringBuilder();
					builder_deal_yc
							.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_deal_yc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_deal_yc
							.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=GZDJDY.BDCDYID ");
					builder_deal_yc
							.append("LEFT JOIN BDCK.BDCS_DJDY_XZ GLDJDY ON GX.SCBDCDYID=GLDJDY.BDCDYID ");
					builder_deal_yc
							.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_deal_yc
							.append("WHERE QL.QLID IS NOT NULL AND  QL.DJLX='800' AND GZDJDY.XMBH='");
					builder_deal_yc.append(super.getXMBH());
					builder_deal_yc.append("'");

					StringBuilder builder_indeal_yc = new StringBuilder();
					builder_indeal_yc
							.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_indeal_yc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_indeal_yc
							.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID=GZDJDY.BDCDYID ");
					builder_indeal_yc
							.append("LEFT JOIN BDCK.BDCS_DJDY_GZ GLDJDY ON GX.YCBDCDYID=GLDJDY.BDCDYID ");
					builder_indeal_yc
							.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_indeal_yc
							.append("WHERE QL.QLID IS NOT NULL AND QL.DJSJ IS NOT NULL AND  QL.DJLX='800' AND QL.QLLX='99' AND GZDJDY.XMBH='");
					builder_indeal_yc.append(super.getXMBH());
					builder_indeal_yc.append("'");

					List<Map> list_deal_yc = getCommonDao()
							.getDataListByFullSql(builder_deal_yc.toString());
					if (list_deal_yc != null && list_deal_yc.size() > 0) {
						for (Map djdy : list_deal_yc) {
							String bdcdylx = StringHelper.formatObject(djdy
									.get("BDCDYLX"));
							String bdcdyid = StringHelper.formatObject(djdy
									.get("BDCDYID"));
							String djdyly = StringHelper.formatObject(djdy
									.get("ly"));
							RealUnit unit = UnitTools.loadUnit(
									BDCDYLX.initFrom(bdcdylx),
									DJDYLY.initFrom(djdyly), bdcdyid);
							if (unit != null) {
								b = true;
								super.setErrMessage("该房屋对应预测户的不动产单元号为"
										+ unit.getBDCDYH()
										+ "的单元已经被查封或正在办理查封，不能登簿！");
								break;
							}
						}
					} else {
						List<Map> list_indeal_yc = getCommonDao()
								.getDataListByFullSql(
										builder_indeal_yc.toString());
						if (list_indeal_yc != null && list_indeal_yc.size() > 0) {
							for (Map djdy : list_indeal) {
								String bdcdylx = StringHelper.formatObject(djdy
										.get("BDCDYLX"));
								String bdcdyid = StringHelper.formatObject(djdy
										.get("BDCDYID"));
								String djdyly = StringHelper.formatObject(djdy
										.get("ly"));
								RealUnit unit = UnitTools.loadUnit(
										BDCDYLX.initFrom(bdcdylx),
										DJDYLY.initFrom(djdyly), bdcdyid);
								if (unit != null) {
									b = true;
									super.setErrMessage("该房屋对应预测户的不动产单元号为"
											+ unit.getBDCDYH()
											+ "的单元已经被查封或正在办理查封，不能登簿！");
									break;
								}
							}
						}
					}
				} else if (BDCDYLX.YCH.equals(super.getBdcdylx())) {
					StringBuilder builder_deal_sc = new StringBuilder();
					builder_deal_sc
							.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_deal_sc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_deal_sc
							.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=GZDJDY.BDCDYID ");
					builder_deal_sc
							.append("LEFT JOIN BDCK.BDCS_DJDY_XZ GLDJDY ON GX.SCBDCDYID=GLDJDY.BDCDYID ");
					builder_deal_sc
							.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_deal_sc
							.append("WHERE QL.QLID IS NOT NULL AND  QL.DJLX='800' AND GZDJDY.XMBH='");
					builder_deal_sc.append(super.getXMBH());
					builder_deal_sc.append("'");

					StringBuilder builder_indeal_sc = new StringBuilder();
					builder_indeal_sc
							.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_indeal_sc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_indeal_sc
							.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=GZDJDY.BDCDYID ");
					builder_indeal_sc
							.append("LEFT JOIN BDCK.BDCS_DJDY_GZ GLDJDY ON GX.SCBDCDYID=GLDJDY.BDCDYID ");
					builder_indeal_sc
							.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_indeal_sc
							.append("WHERE QL.QLID IS NOT NULL AND QL.DJSJ IS NOT NULL AND  QL.DJLX='800' AND QL.QLLX='99' AND GZDJDY.XMBH='");
					builder_indeal_sc.append(super.getXMBH());
					builder_indeal_sc.append("'");

					List<Map> list_deal_yc = getCommonDao()
							.getDataListByFullSql(builder_deal_sc.toString());
					if (list_deal_yc != null && list_deal_yc.size() > 0) {
						for (Map djdy : list_deal_yc) {
							String bdcdylx = StringHelper.formatObject(djdy
									.get("BDCDYLX"));
							String bdcdyid = StringHelper.formatObject(djdy
									.get("BDCDYID"));
							String djdyly = StringHelper.formatObject(djdy
									.get("ly"));
							RealUnit unit = UnitTools.loadUnit(
									BDCDYLX.initFrom(bdcdylx),
									DJDYLY.initFrom(djdyly), bdcdyid);
							if (unit != null) {
								b = true;
								super.setErrMessage("该房屋对应实测户的不动产单元号为"
										+ unit.getBDCDYH()
										+ "的单元已经被查封或正在办理查封，不能登簿！");
								break;
							}
						}
					} else {
						List<Map> list_indeal_yc = getCommonDao()
								.getDataListByFullSql(
										builder_indeal_sc.toString());
						if (list_indeal_yc != null && list_indeal_yc.size() > 0) {
							for (Map djdy : list_indeal) {
								String bdcdylx = StringHelper.formatObject(djdy
										.get("BDCDYLX"));
								String bdcdyid = StringHelper.formatObject(djdy
										.get("BDCDYID"));
								String djdyly = StringHelper.formatObject(djdy
										.get("ly"));
								RealUnit unit = UnitTools.loadUnit(
										BDCDYLX.initFrom(bdcdylx),
										DJDYLY.initFrom(djdyly), bdcdyid);
								if (unit != null) {
									b = true;
									super.setErrMessage("该房屋对应实测户的不动产单元号为"
											+ unit.getBDCDYH()
											+ "的单元已经被查封或正在办理查封，不能登簿！");
									break;
								}
							}
						}
					}
				}
			}
		}
		return b;
	}

	/**
	 * 批量拷贝权利
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQL() {
		String dbr = Global.getCurrentUserName();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class,
				xmbhFilter);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 登记时间，登簿人，登记机构
				bdcs_ql_gz.setDBR(dbr);
				bdcs_ql_gz.setDJSJ(new Date());
				bdcs_ql_gz.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
				getCommonDao().getCurrentSession().update(bdcs_ql_gz);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				getCommonDao().save(bdcs_ql_xz);
				BDCS_QL_LS bdcs_ql_ls = ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
				getCommonDao().save(bdcs_ql_ls);
			}
		}
		return true;
	}

	/**
	 * 批量拷贝附属权利
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyFSQL() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_FSQL_GZ> fsqls = getCommonDao().getDataList(
				BDCS_FSQL_GZ.class, xmbhFilter);
		if (fsqls != null && fsqls.size() > 0) {
			for (int ifsql = 0; ifsql < fsqls.size(); ifsql++) {
				BDCS_FSQL_GZ bdcs_fsql_gz = fsqls.get(ifsql);
				BDCS_FSQL_XZ bdcs_fsql_xz = ObjectHelper
						.copyFSQL_GZToXZ(bdcs_fsql_gz);
				getCommonDao().save(bdcs_fsql_xz);
				BDCS_FSQL_LS bdcs_fsql_ls = ObjectHelper
						.copyFSQL_XZToLS(bdcs_fsql_xz);
				getCommonDao().save(bdcs_fsql_ls);
			}
		}
		return true;
	}

	/**
	 * 批量拷贝权利人
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQLR() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QLR_GZ> qlrs = getCommonDao().getDataList(BDCS_QLR_GZ.class,
				xmbhFilter);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_GZ bdcs_qlr_gz = qlrs.get(iqlr);
				BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper
						.copyQLR_GZToXZ(bdcs_qlr_gz);
				getCommonDao().save(bdcs_qlr_xz);
				BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper
						.copyQLR_XZToLS(bdcs_qlr_xz);
				getCommonDao().save(bdcs_qlr_ls);
			}
		}
		return true;
	}

	/**
	 * 批量拷贝权地证人
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQDZR() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QDZR_GZ> qdzrs = getCommonDao().getDataList(
				BDCS_QDZR_GZ.class, xmbhFilter);
		if (qdzrs != null && qdzrs.size() > 0) {
			for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
				BDCS_QDZR_GZ bdcs_qdzr_gz = qdzrs.get(iqdzr);
				BDCS_QDZR_XZ bdcs_qdzr_xz = ObjectHelper
						.copyQDZR_GZToXZ(bdcs_qdzr_gz);
				getCommonDao().save(bdcs_qdzr_xz);
				BDCS_QDZR_LS bdcs_qdzr_ls = ObjectHelper
						.copyQDZR_XZToLS(bdcs_qdzr_xz);
				getCommonDao().save(bdcs_qdzr_ls);
			}
		}
		return true;
	}

	/**
	 * 批量拷贝证书
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyZS() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_ZS_GZ> zss = getCommonDao().getDataList(BDCS_ZS_GZ.class,
				xmbhFilter);
		if (zss != null && zss.size() > 0) {
			for (int izs = 0; izs < zss.size(); izs++) {
				BDCS_ZS_GZ bdcs_zs_gz = zss.get(izs);
				BDCS_ZS_XZ bdcs_zs_xz = ObjectHelper.copyZS_GZToXZ(bdcs_zs_gz);
				getCommonDao().save(bdcs_zs_xz);
				BDCS_ZS_LS bdcs_zs_ls = ObjectHelper.copyZS_XZToLS(bdcs_zs_xz);
				getCommonDao().save(bdcs_zs_ls);
			}
		}
		return true;
	}

	/**
	 * 批量拷贝登记单元
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	@SuppressWarnings("unchecked")
	protected boolean CopyDJDY() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		String strsql = "SELECT * FROM BDCK.BDCS_DJDY_GZ WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_DJDY_XZ WHERE BDCS_DJDY_GZ.DJDYID=BDCS_DJDY_XZ.DJDYID AND BDCDYLX='"
				+ super.getBdcdylx().Value + "') AND " + xmbhFilter;
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getCurrentSession()
				.createSQLQuery(strsql).addEntity(BDCS_DJDY_GZ.class).list();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				BDCS_DJDY_XZ bdcs_djdy_xz = ObjectHelper
						.copyDJDY_GZToXZ(bdcs_djdy_gz);
				getCommonDao().save(bdcs_djdy_xz);
				BDCS_DJDY_LS bdcs_djdy_ls = ObjectHelper
						.copyDJDY_XZToLS(bdcs_djdy_xz);
				getCommonDao().save(bdcs_djdy_ls);
			}
		}
		return true;
	}

	/**
	 * 批量设定抵押状态
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean SetDYZT() {
		List<BDCS_H_XZY> dys_xz = getCommonDao().getDataList(
				BDCS_H_XZY.class,
				"id IN (SELECT BDCDYID FROM BDCS_DJDY_GZ WHERE XMBH='"
						+ super.getXMBH() + "')");
		if (dys_xz != null && dys_xz.size() > 0) {
			for (int idy = 0; idy < dys_xz.size(); idy++) {
				BDCS_H_XZY bdcs_dy_xz = dys_xz.get(idy);
				bdcs_dy_xz.setDYZT("0");
				getCommonDao().update(bdcs_dy_xz);
			}
		}
		List<BDCS_H_LSY> dys_ls = getCommonDao().getDataList(
				BDCS_H_LSY.class,
				"id IN (SELECT BDCDYID FROM BDCS_DJDY_GZ WHERE XMBH='"
						+ super.getXMBH() + "')");
		if (dys_ls != null && dys_ls.size() > 0) {
			for (int idy = 0; idy < dys_ls.size(); idy++) {
				BDCS_H_LSY bdcs_dy_ls = dys_ls.get(idy);
				bdcs_dy_ls.setDYZT("0");
				getCommonDao().update(bdcs_dy_ls);
			}
		}
		return true;
	}

	/**
	 * 批量拷贝登记单元
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	@SuppressWarnings("unchecked")
	protected boolean CopyDY() {
		String strsql = "SELECT * FROM BDCK.BDCS_H_XZY XZDY WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_H_LSY LSDY WHERE XZDY.BDCDYID=LSDY.BDCDYID) AND EXISTS (SELECT 1 FROM BDCK.BDCS_DJDY_GZ GZDJDY WHERE XZDY.BDCDYID=GZDJDY.BDCDYID AND XMBH='"
				+ super.getXMBH() + "')";
		List<BDCS_H_XZY> dys = getCommonDao().getCurrentSession()
				.createSQLQuery(strsql).addEntity(BDCS_H_XZY.class).list();
		if (dys != null && dys.size() > 0) {
			for (int idy = 0; idy < dys.size(); idy++) {
				BDCS_H_XZY bdcs_dy_xz = dys.get(idy);
				BDCS_H_LSY bdcs_dy_ls = new BDCS_H_LSY();
				UnitTools.copyUnit(bdcs_dy_xz, bdcs_dy_ls);
			}
		}
		return true;
	}

	private ResultMessage addbdcdy(String bdcdyid) {
		ResultMessage msg = new ResultMessage();
		CommonDao dao = this.getCommonDao();
		RealUnit _srcUnit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ,
				bdcdyid);
		if (_srcUnit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元");
		}
		if (_srcUnit != null) {
			BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
			if (djdy == null) {
				djdy = createDJDYfromXZ(bdcdyid, _srcUnit);
			}
			if (djdy != null) {
				// 生成权利信息
				BDCS_QL_GZ ql = super.createQL(djdy, _srcUnit);
				// 生成附属权利
				BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
				ql.setFSQLID(fsql.getId());
				fsql.setQLID(ql.getId());
				ql.setCZFS(CZFS.GTCZ.Value);
				// 设置抵押物类型
				fsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				fsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				fsql.setZJJZWZL(_srcUnit.getZL());
				int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
				fsql.setDYSW(dysw+1);
				// 保存
				dao.update(_srcUnit);
				dao.save(djdy);
				dao.save(ql);
				dao.save(fsql);
			}
			dao.flush();
		}
		return msg;
	}

	/**
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:11:43
	 * @return
	 */
	@Override
	public String getError() {
		return super.getErrMessage();
	}

	/**
	 * 判断是否当前已经创建登记单元，若创建，就不再创建
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月14日下午4:33:37
	 * @param bdcdyid
	 * @return
	 */
	private Boolean IsCreateDJDY(String bdcdyid) {
		Boolean result = false;
		String hqlCondition = MessageFormat.format(
				"BDCDYID=''{0}'' and xmbh=''{1}''", bdcdyid, this.getXMBH());
		List<BDCS_DJDY_GZ> bdcs_djdy_gz = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, hqlCondition);
		if (bdcs_djdy_gz != null && bdcs_djdy_gz.size() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 * 添加登记单元信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日上午11:47:23
	 * @param bdcdyid
	 * @param bdcs_h_xzy
	 * @return
	 */
	protected BDCS_DJDY_GZ createDJDYfromXZ(String bdcdyid, RealUnit realUnit) {
		BDCS_DJDY_GZ gzdjdy = new BDCS_DJDY_GZ();
		String gzdjdyid = getPrimaryKey();
		gzdjdy.setXMBH(this.getXMBH());
		gzdjdy.setDJDYID(gzdjdyid);
		gzdjdy.setBDCDYID(realUnit.getId());
		gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
		gzdjdy.setBDCDYH(realUnit.getBDCDYH());
		gzdjdy.setLY(DJDYLY.XZ.Value);
		// gzdjdy.setDCXMID(bdcs_h_xzy.getDCXMID());

		// 设置预测户的项目编号
		realUnit.setXMBH(this.getXMBH());
		// getCommonDao().update(bdcs_h_xzy);
		return gzdjdy;
	}
	
	protected boolean CopyQLEX() throws Exception {
		String dbr = Global.getCurrentUserName();
		String xmbh2 = super.getXMBH();
		String djjdmc = ConfigHelper.getNameByValue("DJJGMC");
		SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		String date = format.format(new Date());
		//获取权利工作层数据
		StringBuilder ql_gz_sql = new StringBuilder("UPDATE BDCK.BDCS_QL_GZ QL SET QL.DBR='"+dbr+"' ,QL.DJSJ=TO_DATE('" + date + "','YYYY-MM-DD HH24:MI:SS'),QL.DJJG='"+djjdmc+"' WHERE QL.XMBH='"+xmbh2+"'");
		 getCommonDao().excuteQueryNoResult(ql_gz_sql.toString());
		//创建现状层权利数据
		StringBuilder ql_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QL_XZ  (QLID, DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX,");
				ql_xz_sql.append(" DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, ");
				ql_xz_sql.append(" DCXMID, CreateTime,ModifyTime, QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, TDZH, TDZHXH, CASENUM, ");
				ql_xz_sql.append(" ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_xz_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ, ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ");
				ql_xz_sql.append(" ZXARCHIVES_BOOKNO, GYRQK, DYQX, LUZHOU_HTH, JGH )");
				ql_xz_sql.append(" SELECT QLID,DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX, DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, ");
				ql_xz_sql.append(" QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, DCXMID, CreateTime,ModifyTime, QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, ");
				ql_xz_sql.append(" TDZH, TDZHXH, CASENUM, ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_xz_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ,  ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ZXARCHIVES_BOOKNO,");
				ql_xz_sql.append(" GYRQK, DYQX, LUZHOU_HTH, JGH  FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(ql_xz_sql.toString());
		//历史层
		StringBuilder ql_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QL_LS  (QLID, DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX,");
				ql_ls_sql.append(" DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, ");
				ql_ls_sql.append(" DCXMID, CreateTime, ModifyTime, QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, TDZH, TDZHXH, CASENUM, ");
				ql_ls_sql.append(" ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_ls_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ, ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ");
				ql_ls_sql.append(" ZXARCHIVES_BOOKNO, GYRQK, DYQX, LUZHOU_HTH, JGH ) ");
				ql_ls_sql.append(" SELECT QLID,DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX, DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, ");
				ql_ls_sql.append(" QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, DCXMID, CreateTime, ModifyTime,  QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, ");
				ql_ls_sql.append(" TDZH, TDZHXH, CASENUM, ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_ls_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ,  ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ZXARCHIVES_BOOKNO,");
				ql_ls_sql.append(" GYRQK, DYQX, LUZHOU_HTH, JGH  FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(ql_ls_sql.toString());
		return true;
	}
	protected boolean CopyFSQLEX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层附属权利
		StringBuilder fsql_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_FSQL_XZ (FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ, ");
			    fsql_xz_sql.append("SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_xz_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_xz_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_xz_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_xz_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_xz_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,  SFTGHL,");
			    fsql_xz_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH)");
			    fsql_xz_sql.append(" SELECT FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ,");
			    fsql_xz_sql.append(" SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_xz_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_xz_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_xz_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_xz_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_xz_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,  SFTGHL,");
			    fsql_xz_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH FROM BDCK.BDCS_FSQL_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(fsql_xz_sql.toString());
	   //拷贝历史层附属权利
	   StringBuilder fsql_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_FSQL_LS (FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ, ");
			    fsql_ls_sql.append("SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_ls_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_ls_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_ls_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_ls_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_ls_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,  SFTGHL,");
			    fsql_ls_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH)");
			    fsql_ls_sql.append(" SELECT FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ,");
			    fsql_ls_sql.append(" SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_ls_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_ls_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_ls_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_ls_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_ls_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,  SFTGHL,");
			    fsql_ls_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH FROM BDCK.BDCS_FSQL_GZ WHERE XMBH='"+xmbh2+"'");    
		getCommonDao().excuteQueryNoResult(fsql_ls_sql.toString());
			 return true;
	}
	protected boolean CopyQLREX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝权利人现状层
		StringBuilder qlr_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QLR_XZ (QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_xz_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_xz_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,  BZ1)");
		  qlr_xz_sql.append(" SELECT QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_xz_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_xz_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,  BZ1 FROM BDCK.BDCS_QLR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qlr_xz_sql.toString());
		//拷贝权利人历史层
		StringBuilder qlr_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QLR_LS (QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_ls_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_ls_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,  BZ1)");
		  qlr_ls_sql.append(" SELECT QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_ls_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_ls_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,  BZ1 FROM BDCK.BDCS_QLR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qlr_ls_sql.toString());
		return true;
	}
	protected boolean CopyQDZREX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层层
		StringBuilder qdzr_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QDZR_XZ (ID, XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME)");
			  qdzr_xz_sql.append(" SELECT ID,XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME");
			  qdzr_xz_sql.append(" FROM BDCK.BDCS_QDZR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qdzr_xz_sql.toString());	 
		//拷贝历史层
		StringBuilder qdzr_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QDZR_LS (ID, XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME)");
		  qdzr_ls_sql.append(" SELECT ID,XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME");
		  qdzr_ls_sql.append(" FROM BDCK.BDCS_QDZR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qdzr_ls_sql.toString());	 
		return true;
	
	}
	protected boolean CopyZSEX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层数据
		StringBuilder zs_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_ZS_XZ (XMBH, ZSID, ZSBH, BDCQZH, SZSJ, CREATETIME, MODIFYTIME, CFDAGH, ZSDATA)");
			  zs_xz_sql.append(" SELECT XMBH, ZSID, ZSBH, BDCQZH, SZSJ,  CREATETIME, MODIFYTIME, CFDAGH, ZSDATA");
			  zs_xz_sql.append(" FROM BDCK.BDCS_ZS_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(zs_xz_sql.toString());
		//拷贝历史层数据
		StringBuilder zs_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_ZS_LS (XMBH, ZSID, ZSBH, BDCQZH, SZSJ, CREATETIME, MODIFYTIME, CFDAGH, ZSDATA)");
		  zs_ls_sql.append(" SELECT XMBH, ZSID, ZSBH, BDCQZH, SZSJ, CREATETIME, MODIFYTIME, CFDAGH, ZSDATA");
		  zs_ls_sql.append(" FROM BDCK.BDCS_ZS_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(zs_ls_sql.toString());
		return true;
	}
	protected boolean CopyDJDYEX() throws Exception {
		String xmbh2 = super.getXMBH();
		String bdcdylx = super.getBdcdylx().Value;
		//拷贝工作层数据到现状层
		StringBuilder djdy_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_DJDY_XZ  (ID, DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID)");
			  djdy_xz_sql.append(" SELECT ID,DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID");
			  djdy_xz_sql.append(" FROM BDCK.BDCS_DJDY_GZ DJDY WHERE  NOT EXISTS (SELECT 1 FROM BDCK.BDCS_DJDY_XZ XZDJDY WHERE DJDY.DJDYID=XZDJDY.DJDYID AND BDCDYLX='"+bdcdylx+"') "
			  		+ "AND DJDY.XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(djdy_xz_sql.toString());
		//拷贝现状层数据到历史层
		StringBuilder djdy_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_DJDY_LS (ID, DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID)");
			  djdy_ls_sql.append(" SELECT ID,DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID");
			  djdy_ls_sql.append(" FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(djdy_ls_sql.toString());
		return true;
	}
	protected boolean CopyDYEX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层数据到历史层
		StringBuilder h_lsy_sql = new StringBuilder(" insert into bdck.BDCS_H_LSY (BDCDYID, YSDM, BDCDYH, XMBH, FWBM, ZRZBDCDYID, ZDDM, ZDBDCDYID,");
			h_lsy_sql.append(" ZRZH, LJZID, LJZH, CID, CH, ZL, MJDW, ZCS, HH, SHBW, HX, HXJG, FWYT1, FWYT2, FWYT3, YCJZMJ, ");
			h_lsy_sql.append(" YCTNJZMJ, YCFTJZMJ, YCDXBFJZMJ, YCQTJZMJ, YCFTXS, SCJZMJ, SCTNJZMJ, SCFTJZMJ, SCDXBFJZMJ,");
			h_lsy_sql.append(" SCQTJZMJ, SCFTXS, GYTDMJ, FTTDMJ, DYTDMJ, TDSYQR, FDCJYJG, GHYT, FWJG, JGSJ, FWLX, FWXZ,");
			h_lsy_sql.append(" ZDMJ, SYMJ, CQLY, QTGSD, QTGSX, QTGSN, QTGSB, FCFHT, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM,");
			h_lsy_sql.append(" DJZQMC, YXBZ, CQZT, DYZT, XZZT, BLZT, YYZT, DCXMID, FH, DJZT, BGZT, CREATETIME, MODIFYTIME,");
			h_lsy_sql.append(" FCFHTWJ, YFWXZ, YFWYT, YGHYT, YFWJG, YZL, XMZL, PACTNO, XMMC, FWCB, GZWLX, YFWCB, RELATIONID,");
			h_lsy_sql.append(" SFLJQPJYC, QLXZ, TDSYQQSRQ, TDSYQZZRQ, TDSYNX, FCFHTSLTX, FWTDYT, CRJJE, QSC, ZZC, SZC, DYH,");
			h_lsy_sql.append(" BZ, NBDCDYH, FCFHTWJLX, YZRZBDCDYID, NZDBDCDYID, XZB, YZB, MARKERZT, MARKERZTMC, MARKERSM,");
			h_lsy_sql.append(" MARKERTIME, SEARCHSTATE, SFSZZ, MSR, HTH, HJSON, CREATOR, MODIFYER, ISJG,  SFCT,");
			h_lsy_sql.append(" CTXMBH, FWJG1, FWJG2, FWJG3, GJZLX, ISPACKAGED, PACKAGESTAFF, PACKAGETIME)");
			h_lsy_sql.append(" select BDCDYID, YSDM, BDCDYH, XMBH, FWBM, ZRZBDCDYID, ZDDM, ZDBDCDYID, ZRZH, LJZID, LJZH,");
			h_lsy_sql.append(" CID, CH, ZL, MJDW, ZCS, HH, SHBW, HX, HXJG, FWYT1, FWYT2, FWYT3, YCJZMJ, YCTNJZMJ, YCFTJZMJ,");
			h_lsy_sql.append(" YCDXBFJZMJ, YCQTJZMJ, YCFTXS, SCJZMJ, SCTNJZMJ, SCFTJZMJ, SCDXBFJZMJ, SCQTJZMJ, SCFTXS, GYTDMJ,");
			h_lsy_sql.append(" FTTDMJ, DYTDMJ, TDSYQR, FDCJYJG, GHYT, FWJG, JGSJ, FWLX, FWXZ, ZDMJ, SYMJ, CQLY, QTGSD, QTGSX,");
			h_lsy_sql.append(" QTGSN, QTGSB, FCFHT, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, YXBZ, CQZT, DYZT, XZZT, BLZT,");
			h_lsy_sql.append(" YYZT, DCXMID, FH, DJZT, BGZT, CREATETIME, MODIFYTIME, FCFHTWJ, YFWXZ, YFWYT, YGHYT, YFWJG, YZL, XMZL, ");
			h_lsy_sql.append(" PACTNO, XMMC, FWCB, GZWLX, YFWCB, RELATIONID, SFLJQPJYC, QLXZ, TDSYQQSRQ, TDSYQZZRQ, TDSYNX, FCFHTSLTX,");
			h_lsy_sql.append(" FWTDYT, CRJJE, QSC, ZZC, SZC, DYH, BZ, NBDCDYH, FCFHTWJLX, YZRZBDCDYID, NZDBDCDYID, XZB, YZB, MARKERZT,");
			h_lsy_sql.append(" MARKERZTMC, MARKERSM, MARKERTIME, SEARCHSTATE, SFSZZ, MSR, HTH, HJSON, CREATOR, MODIFYER, ISJG, ");
			h_lsy_sql.append(" SFCT, CTXMBH, FWJG1, FWJG2, FWJG3, GJZLX, ISPACKAGED , PACKAGESTAFF,PACKAGETIME  from bdck.bdcs_h_xzy XZDY ");
			h_lsy_sql.append(" WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_H_LSY LSDY WHERE XZDY.BDCDYID=LSDY.BDCDYID) ");
			h_lsy_sql.append(" AND EXISTS (SELECT 1 FROM BDCK.BDCS_DJDY_GZ GZDJDY WHERE XZDY.BDCDYID=GZDJDY.BDCDYID AND XMBH='"+xmbh2+"')");
				getCommonDao().excuteQueryNoResult(h_lsy_sql.toString());
		return true;
	}
	protected boolean SetDYZTEX() throws Exception {
		String xmbh2 = super.getXMBH();
		//更新现状层抵押信息
		StringBuilder h_xzy_sql = new StringBuilder("UPDATE BDCK.BDCS_H_XZY  H SET H.DYZT ='0' WHERE EXISTS(SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY WHERE DJDY.BDCDYID=H.BDCDYID AND DJDY.XMBH='"+xmbh2+"')");
		getCommonDao().excuteQueryNoResult(h_xzy_sql.toString());
		//更新现状层抵押信息
		StringBuilder h_lsy_sql = new StringBuilder("UPDATE BDCK.BDCS_H_LSY  H SET H.DYZT ='0' WHERE EXISTS(SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY WHERE DJDY.BDCDYID=H.BDCDYID AND DJDY.XMBH='"+xmbh2+"')");
		getCommonDao().excuteQueryNoResult(h_lsy_sql.toString());
		return true;
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
				List<RightsHolder> bdcqlrs = RightsHolderTools
						.loadRightsHolders(ConstValue.DJDYLY.GZ,
								djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy,
						bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1,
						djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
